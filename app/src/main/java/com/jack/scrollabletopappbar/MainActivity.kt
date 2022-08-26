package com.jack.scrollabletopappbar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jack.scrollabletopappbar.ui.theme.ScrollableTopAppBarTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrollableTopAppBarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DefaultPreview()
                }
            }
        }
    }
}

@Composable
fun DefaultPreview(
    modifier: Modifier = Modifier
) {
    ScrollableTopAppBarTheme {


        val toolbarHeight = 48.dp
        val toolbarHeightPx = with(LocalDensity.current) {
            toolbarHeight.roundToPx().toFloat()
        }

        val toolbarOffsetHeightPx = remember {
            mutableStateOf(0f)
        }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)

                    return Offset.Zero
                }
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            Surface(modifier = modifier.fillMaxSize()) {
                Row(modifier = modifier.fillMaxSize()) {

                    Box(
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxSize()

                        ) {

                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                item {
                                    Spacer(modifier = Modifier.height(toolbarHeight))
                                }
                                items(20) { i ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        elevation = 3.dp,
                                        shape = RoundedCornerShape(50.dp)
                                    ) {
                                        Text(
                                            text = "Text $i",
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            TopAppBar(
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
                elevation = 0.dp,
                title = {},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            tint = Color.Yellow,
                            imageVector = Icons.Filled.Menu,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    }
}