package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.LibraryViewModel
import com.example.ui.Screen
import com.example.ui.components.SidebarNav
import com.example.ui.screens.AddEditBookScreen
import com.example.ui.screens.BookCatalogScreen
import com.example.ui.screens.BookDetailScreen
import com.example.ui.screens.BorrowReturnScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OverdueListScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.StudentsScreen
import com.example.ui.theme.BgLight
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.LibraTrackTheme
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy900
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibraTrackTheme {
                val viewModel: LibraryViewModel = viewModel()
                LibraTrackApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraTrackApp(viewModel: LibraryViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val librarian by viewModel.librarian.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Handle toast messages
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
            viewModel.clearToast()
        }
    }

    // Predictive Back Handling
    BackHandler(enabled = isLoggedIn && currentScreen != Screen.DASHBOARD) {
        if (!viewModel.navigateBack()) {
            viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    if (!isLoggedIn || currentScreen == Screen.LOGIN) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = BgLight
        ) { innerPadding ->
            LoginScreen(
                onLoginSuccess = { email, role ->
                    viewModel.login(email, role)
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
        return
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTabletOrDesktop = maxWidth >= 720.dp

        if (isTabletOrDesktop) {
            // Tablet / Desktop Permanent Sidebar Layout
            Row(modifier = Modifier.fillMaxSize().background(BgLight)) {
                SidebarNav(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    overdueCount = stats.totalOverdueCount,
                    librarian = librarian,
                    onLogout = { viewModel.logout() }
                )

                Scaffold(
                    modifier = Modifier.weight(1f),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    containerColor = BgLight
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ScreenContent(
                            screen = currentScreen,
                            viewModel = viewModel
                        )
                    }
                }
            }
        } else {
            // Responsive Mobile Drawer Layout
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = Navy900,
                        modifier = Modifier.width(280.dp)
                    ) {
                        SidebarNav(
                            currentScreen = currentScreen,
                            onNavigate = { screen ->
                                viewModel.navigateTo(screen)
                                coroutineScope.launch { drawerState.close() }
                            },
                            overdueCount = stats.totalOverdueCount,
                            librarian = librarian,
                            onLogout = {
                                coroutineScope.launch { drawerState.close() }
                                viewModel.logout()
                            }
                        )
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Navy700),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.libratrack_logo),
                                            contentDescription = null,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = currentScreen.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = { coroutineScope.launch { drawerState.open() } },
                                    modifier = Modifier.testTag("drawer_menu_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Open Navigation Menu",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Navy900,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White
                            )
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    containerColor = BgLight
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        ScreenContent(
                            screen = currentScreen,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenContent(
    screen: Screen,
    viewModel: LibraryViewModel
) {
    when (screen) {
        Screen.LOGIN -> LoginScreen(onLoginSuccess = { email, role -> viewModel.login(email, role) })
        Screen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
        Screen.CATALOG -> BookCatalogScreen(viewModel = viewModel)
        Screen.STUDENTS -> StudentsScreen(viewModel = viewModel)
        Screen.BORROW_RETURN -> BorrowReturnScreen(viewModel = viewModel)
        Screen.OVERDUE -> OverdueListScreen(viewModel = viewModel)
        Screen.REPORTS -> ReportsScreen(viewModel = viewModel)
        Screen.BOOK_DETAIL -> BookDetailScreen(viewModel = viewModel)
        Screen.ADD_EDIT_BOOK -> AddEditBookScreen(viewModel = viewModel)
    }
}
