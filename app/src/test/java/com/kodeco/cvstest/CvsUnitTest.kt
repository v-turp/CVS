package com.kodeco.cvstest

import com.kodeco.cvstest.presentation.viewmodels.MainActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Local unit test to test viewmodel flow states
 */
class CvsUnitTest {

    @Test
    fun initialSearchingStateIsFalse(){// TODO DON'T TOUCH!!!!! WORKS!!!!!
        assertFalse(MainActivityViewModel().searchingSearchBarQuery.value)
    }

    @Test
    fun updatedSearchingValueIsTrue(){// TODO DON'T TOUCH!!!!! WORKS!!!!!
        val viewModel = MainActivityViewModel()
        viewModel.updateNetworkSearchingStatus(true)

        val expected = true
        val actual = viewModel.searchingSearchBarQuery.value

        assertEquals(expected, actual)
        assertTrue(actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun collectedUpdatedValueForSearchingStatusIsTrue(){// TODO DON'T TOUCH!!!!! WORKS!!!!!
        val viewModel = MainActivityViewModel()
        runTest(UnconfinedTestDispatcher()) {
            viewModel.updateNetworkSearchingStatus(true)
            val job = launch {
                viewModel.searchingSearchBarQuery.collect {
                    assertEquals(true, it)
                }
            }
            job.cancelAndJoin()
        }
    }
}