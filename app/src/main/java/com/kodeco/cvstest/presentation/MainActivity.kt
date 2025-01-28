package com.kodeco.cvstest.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.kodeco.cvstest.data.FlickrResponse
import com.kodeco.cvstest.presentation.viewmodels.MainActivityViewModel
import com.kodeco.cvstest.ui.theme.CVSTestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var vmFlickr: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CVSTestTheme {
                FlickrSearchScreen()
            }
        }
    }

    @Composable
    fun FlickrSearchScreen(){
        vmFlickr = MainActivityViewModel()

        //--- hoisted state
        var searchQueryHoisted by remember { mutableStateOf("") }
        val onQueryChange : (String) -> Unit = {
            searchQueryHoisted = it
            vmFlickr.viewModelScope.launch {
                delay(200)
                vmFlickr.updateNetworkSearchingStatus(searching = true)
                vmFlickr.getFlickerDataFlow(searchQueryHoisted).collect{}
            }
        }
        //--- Search Screen
        FlickrSearchScreenContent(query = searchQueryHoisted,
            onQueryChange = onQueryChange)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun FlickrSearchScreenContent(query: String, onQueryChange: (String) -> Unit){
        val searching by vmFlickr.searchingSearchBarQuery.collectAsStateWithLifecycle()
        val vmDataSet by vmFlickr.flickrResponse.collectAsStateWithLifecycle()

        Scaffold {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    SearchBar(modifier = Modifier.fillMaxWidth(),
                        inputField = {
                            SearchBarDefaults.InputField(
                                placeholder = { Text(text = "Search") }, // TODO internationalization
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Icon"
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear Search Icon"    // TODO internationalization
                                    )
                                },
                                query = query,
                                onQueryChange = { onQueryChange(it) },
                                onSearch = {},
                                expanded = false,
                                onExpandedChange = {}
                            )
                        },
                        expanded = false,
                        onExpandedChange = {},
                        content = {})
                    SearchResultsList(vmDataSet)
                } // end column
                if (searching)
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } // end scaffold
        }
    }  // end FlickrSearchScreenContent

    @Composable
    fun SearchResultsList(dataSet: FlickrResponse) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp))
        {
            val size = dataSet.items?.size
            if (dataSet.title!==null) {
                items(size!!){
                    RowItem(dataSet = dataSet, index = it)
                }
            }
        }
    }

    @Composable
    fun RowItem(dataSet: FlickrResponse, index: Int){

        var showImageDetails by rememberSaveable {mutableStateOf(false)}
        val onDismissRequest : () -> Unit = { showImageDetails = false}
        if (showImageDetails){
            ImageDetailsAlert(description = dataSet.items?.get(index)!!.title,
                              onDismissRequest = onDismissRequest)
        }

        AsyncImage(modifier = Modifier
            .fillMaxSize()
            .clickable { showImageDetails = true },
            model = dataSet.items?.get(index)?.media?.m,
            contentDescription = dataSet.items?.get(index)?.description,
        )
    }

    @Composable
    fun ImageDetailsAlert( description: String, onDismissRequest: () -> Unit){

        AlertDialog(onDismissRequest = {onDismissRequest.invoke()},
                    confirmButton = {},
                    text = { Text(text = description)})
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        CVSTestTheme {
            FlickrSearchScreen()
        }
    }

} // end MainActivity



