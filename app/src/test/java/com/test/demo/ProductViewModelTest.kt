package com.test.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.demo.common.MainCoroutineRule
import com.test.demo.common.RxMainDispatcherRule
import com.test.demo.data.remote.api.ApiError
import com.test.demo.data.remote.model.Product
import com.test.demo.data.repo.ProductRepo
import com.test.demo.features.product.ProductEvent
import com.test.demo.features.product.ProductViewModel
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.*

@RunWith(MockitoJUnitRunner::class)
class ProductViewModelTest {
    @Mock
    lateinit var repo: ProductRepo

    lateinit var productViewModel: ProductViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mainRxRule = RxMainDispatcherRule()


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(repo.getListProduct()).thenReturn(Single.just(emptyList()))
        productViewModel = ProductViewModel(repo)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `test get list product success`() {
        val mockProduct = listOf<Product>()
        `when`(repo.getListProduct()).thenReturn(Single.just(mockProduct))

        productViewModel.getProductList()
        verify(repo, times(2)).getListProduct()
        val productList = productViewModel.listProductLiveData.observeAndGet()
        assertNotNull(productList)
        assertTrue(productList.isEmpty())
    }

    @Test
    fun `test init product`() {
        val product = getRandomProduct()
        productViewModel.setState(product)
        val initProduct = productViewModel.productStateLiveData.observeAndGet()

        assertProduct(product, initProduct)
    }

    @Test
    fun `test add product success`() {
        val product = getRandomProduct()
        productViewModel.setState(product)

        val currentProduct = productViewModel.productStateLiveData.observeAndGet()
        assertNotNull(currentProduct)

        `when`(repo.addProduct(currentProduct)).thenReturn(Single.just(currentProduct))
        productViewModel.addProduct()
        val event = productViewModel.event().observeAndGet()
        assertNotNull(event)
        assertTrue(event is ProductEvent.AddSuccess)

        val needReloadEvent = productViewModel.needReload.observeAndGet()
        assertNotNull(needReloadEvent)
        assertTrue(needReloadEvent)
    }


    @Test
    fun `test update product success`() {
        val product = getRandomProduct()
        productViewModel.setState(product)

        val currentProduct = productViewModel.productStateLiveData.observeAndGet()
        assertNotNull(currentProduct)

        `when`(repo.updateProduct(currentProduct)).thenReturn(Single.just(currentProduct))
        productViewModel.editProduct()
        val event = productViewModel.event().observeAndGet()
        assertNotNull(event)
        assertTrue(event is ProductEvent.EditSuccess)

        val needReloadEvent = productViewModel.needReload.observeAndGet()
        assertNotNull(needReloadEvent)
        assertTrue(needReloadEvent)
    }

    @Test
    fun `test product invalid`() {
        val product = getRandomProduct()
        productViewModel.setState(product)
        assertTrue(productViewModel.isValidProduct())

        productViewModel.setState(product.copy(price = -1, qty = 200))
        assertFalse(productViewModel.isValidProduct())

        productViewModel.setState(product.copy(price = 100, qty = -5))
        assertFalse(productViewModel.isValidProduct())

        productViewModel.setState(product.copy(price = -1, qty = -2))
        assertFalse(productViewModel.isValidProduct())
    }

    @Test
    fun `test search product success`() {
        val product = getRandomProduct()
        `when`(repo.searchProduct(product.sku)).thenReturn(Single.just(product))

        productViewModel.searchBySku(product.sku)
        val productList = productViewModel.listProductLiveData.observeAndGet()
        assertNotNull(productList)
        assertEquals(1, productList.size)
        assertProduct(product, productList.first())
    }

    @Test
    fun `test search product failed should return empty list`() {
        val mockError = ApiError("No product found", 400)
        `when`(repo.searchProduct(anyString()))
            .thenReturn(Single.error(mockError))

        productViewModel.searchBySku("sku")
        val productList = productViewModel.listProductLiveData.observeAndGet()
        assertNotNull(productList)
        assertEquals(0, productList.size)
    }

    @Test
    fun `test add invalid product`() {
        val product = getRandomProduct().copy(price = -100)
        productViewModel.setState(product)

        val currentProduct = productViewModel.productStateLiveData.observeAndGet()
        assertNotNull(currentProduct)

        `when`(repo.addProduct(currentProduct)).thenReturn(Single.just(currentProduct))
        productViewModel.addProduct()
        val error = productViewModel.error().observeAndGet()
        assertNotNull(error)
        assertTrue(error is IllegalArgumentException)
    }

    @Test
    fun `test add product fail`() {
        val mockError = ApiError("Mock error", 400)
        val product = getRandomProduct()
        productViewModel.setState(product)

        val currentProduct = productViewModel.productStateLiveData.observeAndGet()
        assertNotNull(currentProduct)

        `when`(repo.addProduct(currentProduct)).thenReturn(Single.error(mockError))
        productViewModel.addProduct()
        val error = productViewModel.error().observeAndGet()
        assertNotNull(error)
        assertTrue(error is ApiError)
        assertEquals(mockError.code, error.code)
        assertEquals(mockError.message, error.message)
    }

    @Test
    fun `test update product fail`() {
        val mockError = ApiError("Mock error", 400)
        val product = getRandomProduct()
        productViewModel.setState(product)

        val currentProduct = productViewModel.productStateLiveData.observeAndGet()
        assertNotNull(currentProduct)

        `when`(repo.updateProduct(currentProduct)).thenReturn(Single.error(mockError))
        productViewModel.editProduct()
        val error = productViewModel.error().observeAndGet()
        assertNotNull(error)
        assertTrue(error is ApiError)
        assertEquals(mockError.code, error.code)
        assertEquals(mockError.message, error.message)
    }

    @Test
    fun `test delete product success`() {
        val product = getRandomProduct()
        `when`(repo.getListProduct()).thenReturn(Single.just(listOf(product)))
        `when`(repo.deleteProduct(product.sku)).thenReturn(Single.just(product))

        productViewModel.getProductList()
        productViewModel.deleteProduct(product.sku)
        val productList = productViewModel.listProductLiveData.observeAndGet()

        assertNotNull(productList)
        assertEquals(0, productList.size)
    }
}