package com.test.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.test.demo.common.MainCoroutineRule
import com.test.demo.common.RxMainDispatcherRule
import com.test.demo.common.TestConst
import com.test.demo.data.prefs.PrefsHelper
import com.test.demo.data.api.common.ApiError
import com.test.demo.data.api.auth.AuthApi
import com.test.demo.data.api.model.RegisterResponse
import com.test.demo.data.api.model.Token
import com.test.demo.auth.ui.AuthEvent
import com.test.demo.auth.ui.AuthViewModel
import com.test.demo.utils.dispatcher.NavigationDispatcher
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    private val navigationDispatcher = NavigationDispatcher()

    @Mock
    lateinit var mockApi: AuthApi

    @Mock
    lateinit var mockPrefHelper: PrefsHelper

    @Mock
    private lateinit var mockObserver: Observer<Boolean>

    lateinit var authViewModel: AuthViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mainRxRule = RxMainDispatcherRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        authViewModel = AuthViewModel(mockApi, mockPrefHelper, navigationDispatcher)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `test login success and token is saved`() {
        val mockToken = Token("Mock token")
        `when`(mockApi.login(TestConst.TEST_EMAIL, TestConst.TEST_PASSWORD))
            .thenReturn(Single.just(mockToken))

        val isOk = authViewModel.isOk
        authViewModel.isOk.observeForever(mockObserver)
        authViewModel.setEmail(TestConst.TEST_EMAIL)
        authViewModel.setPassword("")
        assertFalse(isOk.value!!)

        authViewModel.setEmail("wrong email")
        authViewModel.setPassword(TestConst.TEST_PASSWORD)
        assertFalse(isOk.value!!)

        authViewModel.setEmail(TestConst.TEST_EMAIL)
        authViewModel.setPassword(TestConst.TEST_PASSWORD)
        assertTrue(isOk.value!!)

        authViewModel.login()
        verify(mockPrefHelper).saveToken(mockToken.token)
        runBlocking {
            val event = navigationDispatcher.eventFlow.firstOrNull()
            assertNotNull(event)
        }
    }

    @Test
    fun `test login should fail`() {
        val expectedException = ApiError("Wrong username or password", 400)
        `when`(mockApi.login(TestConst.TEST_EMAIL, TestConst.TEST_PASSWORD))
            .thenReturn(Single.error(expectedException))

        val isOk = authViewModel.isOk
        authViewModel.isOk.observeForever(mockObserver)
        authViewModel.setEmail(TestConst.TEST_EMAIL)
        authViewModel.setPassword(TestConst.TEST_PASSWORD)
        assertTrue(isOk.value!!)

        authViewModel.login()
        authViewModel.error().observeForever {  }
        val error = authViewModel.error().value
        assertNotNull(error)
        assertTrue(error is ApiError)
        assertEquals(expectedException.code, error.code)
        assertEquals(expectedException.message, error.message)
    }

    @Test
    fun `test register success`() {
        val responseData = RegisterResponse.Data(
            createdAt = "",
            email = TestConst.TEST_EMAIL,
            id = -1,
            updatedAt = ""
        )

        val mockResponse = RegisterResponse(responseData, "", true)
        `when`(mockApi.register(TestConst.TEST_EMAIL, TestConst.TEST_PASSWORD))
            .thenReturn(Single.just(mockResponse))

        val isOk = authViewModel.isOk
        authViewModel.isOk.observeForever(mockObserver)
        authViewModel.setEmail(TestConst.TEST_EMAIL)
        authViewModel.setPassword(TestConst.TEST_PASSWORD)
        assertTrue(isOk.value!!)

        authViewModel.register()
        authViewModel.event().observeForever { }
        val value = authViewModel.event().value
        assertNotNull(value)
        assertTrue(value is AuthEvent.RegisterSuccessEvent)
    }

    @Test
    fun `test register should fail`() {
        val expectedException = ApiError("Wrong username or password", 400)
        `when`(mockApi.login(TestConst.TEST_EMAIL, TestConst.TEST_PASSWORD))
            .thenReturn(Single.error(expectedException))

        authViewModel.isOk.observeForever(mockObserver)
        authViewModel.setEmail(TestConst.TEST_EMAIL)
        authViewModel.setPassword(TestConst.TEST_PASSWORD)
        assertTrue(authViewModel.isOk.observeAndGet() == true)

        authViewModel.login()
        val error = authViewModel.error().observeAndGet()
        assertNotNull(error)
        assertTrue(error is ApiError)
        assertEquals(expectedException.code, error.code)
        assertEquals(expectedException.message, error.message)
    }
}