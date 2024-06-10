package com.samz.githubreposviewer.presenter.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.samz.githubreposviewer.R
import com.samz.githubreposviewer.domain.model.Repo
import com.samz.githubreposviewer.extension.items
import com.samz.githubreposviewer.presenter.base.EmptyScreen
import com.samz.githubreposviewer.presenter.base.ErrorScreen
import com.samz.githubreposviewer.presenter.base.SelectThemeDialog
import com.samz.githubreposviewer.theme.GithubReposViewerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoriesScreen(
    viewModel: RepositoriesViewModel,
    themeChangeViewModel: MainViewModel,
    navigate: (user: String, repo: String) -> Unit = { _, _ -> },
) {
    if (themeChangeViewModel.stateApp.openDialog) {
        SelectThemeDialog(mainViewModel = themeChangeViewModel, setShowDialog = {
            themeChangeViewModel.onEvent(MainEvent.OpenDialog(it))
        }, returnValue = {})
    }

    val lazyPagingItems = viewModel.pagingResult.collectAsLazyPagingItems()
    val state = lazyPagingItems.loadState

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.repository_list)) }, actions = {
                IconButton(onClick = {
                    themeChangeViewModel.onEvent(
                        MainEvent.OpenDialog(true)
                    )
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_theme_mode),
                        modifier = Modifier.size(20.dp),
                        contentDescription = "Theme change mode"
                    )
                }
            })
        },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Box(modifier = Modifier.fillMaxSize()) {
                    if (state.refresh is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else if (state.refresh is LoadState.Error) {
                        ErrorScreen(errorMsg = (state.refresh as LoadState.Error).error.message) {
                            lazyPagingItems.refresh()
                        }
                    } else if (lazyPagingItems.itemCount == 0) {
                        EmptyScreen()
                    } else {
                        LazyColumn {
                            items(lazyPagingItems) { repo ->
                                RepositoryRow(
                                    repo = repo,
                                    navigate = navigate
                                )
                                Divider(
                                    color = Color.LightGray,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 14.dp
                                    )
                                )
                            }

                        }
                    }
                }
            }
        })
}

@Composable
fun RepositoryRow(
    repo: Repo,
    navigate: (user: String, repo: String) -> Unit = { _, _ -> },
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable { navigate(repo.owner, repo.name) }
            .padding(10.dp),
    ) {
        Text(
            text = repo.fullRepoName,
            fontSize = 16.sp,
            color = Color.Blue,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.size(10.dp))
        Text(repo.description ?: "", fontSize = 13.sp, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(repo.owner, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.size(10.dp))
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = stringResource(id = R.string.owner_s_avatar_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                placeholder = rememberVectorPainter(image = Icons.Filled.Person),
                error = rememberVectorPainter(image = Icons.Filled.Person),
            )
        }
    }
}

@Preview
@Composable
fun BeerItemPreview() {
    GithubReposViewerTheme {
        RepositoryRow(
            repo = Repo(
                id = 1,
                name = "Beer",
                owner = "This is a cool beer",
                description = "This is a description for a beer. \nThis is the next line.",
                ownerAvatarUrl = null,
                fullRepoName = "232131/32131",
                isPrivate = false
            ),
        )
    }
}
