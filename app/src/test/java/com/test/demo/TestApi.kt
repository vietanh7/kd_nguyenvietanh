package com.test.demo

import com.google.gson.Gson
import com.test.demo.data.DataModule
import com.test.demo.data.local.PrefsHelper
import com.test.demo.data.remote.ApiError
import com.test.demo.data.remote.model.Product
import com.test.demo.data.remote.network.NetworkModule
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@RunWith(MockitoJUnitRunner::class)
class TestApi {

    val mockPrefsHelper: PrefsHelper = mock(PrefsHelper::class.java)

    val client = NetworkModule.provideHttpClient(mockPrefsHelper)
    val retrofit = NetworkModule.provideRetrofit(client, Gson())
    val service = DataModule.provideApiService(retrofit)

    val email = "test.task@klikdokter.com"
    val password = "T3stKl1kd0kt3r"

    fun getRandomProduct(): Product {
        val uuid = UUID.randomUUID().toString()
        return Product(
            createdAt = "",
            updatedAt = "",
            productName = uuid.takeLast(5),
            price = Random.nextInt().toLong(),
            unit = "Carbon",
            qty = Random.nextInt(),
            sku = uuid.takeLast(4),
            status = 1,
            success = true,
            message = "",
            image = null,
            id = -1
        )
    }

    private fun assertProduct(expected: Product, actual: Product?) {
        assertEquals(expected.sku, actual?.sku)
        assertEquals(expected.productName, actual?.productName)
        assertEquals(expected.qty, actual?.qty)
        assertEquals(expected.price, actual?.price)
        assertEquals(expected.unit, actual?.unit)
        assertEquals(expected.status, actual?.status)
    }

    @Before
    fun setUpRxSchedulers() {
        given(mockPrefsHelper.getToken()).willReturn("")
        RxJavaPlugins.setIoSchedulerHandler { s -> Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { s -> Schedulers.trampoline() }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { s -> Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { s -> Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun test_login_api_call_success() {
        service.login(email, password)
            .blockingSubscribe { token ->
                assertNotNull(token)
                assertTrue(token.token.isNotEmpty())
            }
    }

    @Test
    fun test_login_api_fail() {
        val result = kotlin.runCatching {
            service.login("testEmail@gmail.com", "testPassword")
                .blockingGet()
        }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is HttpException)
    }

    @Test
    fun test_get_list_product() {
        service.getProductList()
            .blockingSubscribe { products ->
                assertTrue(products.isNotEmpty())
            }
    }

    @Test
    fun test_add_product_success() {
        val token = service.login(email, password).blockingGet()
        val product = getRandomProduct()
        assertTrue(token.token.isNotEmpty())

        given(mockPrefsHelper.getToken()).willReturn(token.token)

        val result = runCatching {
            service.addProduct(product.sku, product.productName,
                product.qty, product.price,
                product.unit, product.status
            ).blockingGet()
        }

        val addedProduct = result.getOrNull()
        assertTrue(result.isSuccess)
        assertProduct(product, addedProduct)

        service.deleteProduct(product.sku).blockingGet()
    }

    @Test
    fun test_add_product_fail_no_token() {
        val product = getRandomProduct()

        given(mockPrefsHelper.getToken()).willReturn("")
        val result = runCatching {
            service.addProduct(product.sku, product.productName,
                product.qty, product.price,
                product.unit, product.status
            ).blockingGet()
        }

        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is HttpException)
        assertEquals(401, exception.code())
    }

    @Test
    fun test_update_product_success() {
        val token = service.login(email, password).blockingGet()
        val product = getRandomProduct()
        assertTrue(token.token.isNotEmpty())

        given(mockPrefsHelper.getToken()).willReturn(token.token)

        val addedProduct = service.addProduct(
            product.sku,
            product.productName,
            product.qty,
            product.price,
            product.unit,
            product.status
        ).blockingGet()

        val newProduct = getRandomProduct().copy(sku = product.sku)
        val result = runCatching {
            service.updateProduct(
                product.sku,
                newProduct.productName,
                newProduct.qty,
                newProduct.price,
                newProduct.unit,
                newProduct.status
            ).blockingGet()
        }

        assertTrue(result.isSuccess)

        val updatedProduct = result.getOrNull()
        assertProduct(newProduct, updatedProduct)

        service.deleteProduct(addedProduct.sku).blockingGet()
    }

    @Test
    fun test_update_product_wrong_sku() {
        val token = service.login(email, password).blockingGet()
        val product = getRandomProduct()
        assertTrue(token.token.isNotEmpty())

        given(mockPrefsHelper.getToken()).willReturn(token.token)
        val result = runCatching {
            service.updateProduct(
                product.sku,
                product.productName,
                product.qty,
                product.price,
                product.unit,
                product.status
            ).blockingGet().validate()
        }

        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is ApiError)
        assertEquals(400, exception.code)
    }

    @Test
    fun test_update_product_fail_no_token() {
        val product = getRandomProduct()
        given(mockPrefsHelper.getToken()).willReturn("")

        val newProduct = getRandomProduct().copy(sku = product.sku)
        val result = runCatching {
            service.updateProduct(
                product.sku,
                newProduct.productName,
                newProduct.qty,
                newProduct.price,
                newProduct.unit,
                newProduct.status
            ).blockingGet()
        }

        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is HttpException)
        assertEquals(401, exception.code())
    }

    @Test
    fun test_delete_product_success() {
        val product = getRandomProduct()
        val token = service.login(email, password).blockingGet()
        given(mockPrefsHelper.getToken()).willReturn(token.token)

        val addedProduct = service.addProduct(
            product.sku, product.productName,
            product.qty, product.price,
            product.unit, product.status
        ).blockingGet()

        val result = runCatching {
            service.deleteProduct(addedProduct.sku).blockingGet()
        }

        val deletedProduct = result.getOrNull()
        assertNotNull(deletedProduct)
        assertProduct(product, deletedProduct)
    }

    @Test
    fun test_delete_product_no_token() {
        val product = getRandomProduct()
        given(mockPrefsHelper.getToken()).willReturn("")

        val result = runCatching {
            service.deleteProduct(product.sku).blockingGet()
        }

        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is HttpException)
        assertEquals(401, exception.code())
    }

    @Test
    fun test_delete_product_wrong_sku() {
        val product = getRandomProduct()
        val token = service.login(email, password).blockingGet()
        given(mockPrefsHelper.getToken()).willReturn(token.token)

        val result = runCatching {
            service.deleteProduct(product.sku).blockingGet().validate()
        }

        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is ApiError)
        assertEquals(400, exception.code)
    }

    @Test
    fun test_search_product_success() {
        val product = getRandomProduct()
        val token = service.login(email, password).blockingGet()
        given(mockPrefsHelper.getToken()).willReturn(token.token)

        val addedProduct = service.addProduct(
            product.sku, product.productName,
            product.qty, product.price,
            product.unit, product.status
        ).blockingGet()

        val result = runCatching {
            service.searchProduct(addedProduct.sku).blockingGet()
        }

        val searchProduct = result.getOrNull()
        assertNotNull(searchProduct)
        assertProduct(product, searchProduct)

        service.deleteProduct(searchProduct.sku).blockingGet()
    }

    @Test
    fun test_search_product_fail_wrong_sku() {
        val product = getRandomProduct()
        val token = service.login(email, password).blockingGet()
        given(mockPrefsHelper.getToken()).willReturn(token.token)

        val result = runCatching {
            service.searchProduct(product.sku).blockingGet().validate()
        }

        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is ApiError)
        assertEquals(400, exception.code)
    }

    @Test
    fun test_register_success() {
        val uuid = UUID.randomUUID().toString()
        val email = uuid.takeLast(8) + "@yandex.com"
        val password = uuid.take(8)
        val result = runCatching {
            service.register(email, password)
                .flatMap { service.login(email, password) }
                .blockingGet()
        }

        val token = result.getOrNull()
        assertFalse(token?.token.isNullOrEmpty())
    }

    @Test
    fun test_register_fail_invalid_email() {
        val uuid = UUID.randomUUID().toString()
        val email = uuid.takeLast(8)
        val password = uuid.take(8)
        val result = runCatching {
            service.register(email, password)
                .flatMap { service.login(email, password) }
                .blockingGet()
        }

        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is HttpException)
        assertEquals(422, exception.code())
    }

    @Test
    fun test_register_fail_email_existed() {
        val result = runCatching {
            service.register(email, password)
                .blockingGet()
        }

        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is HttpException)
        assertEquals(422, exception.code())
    }
}