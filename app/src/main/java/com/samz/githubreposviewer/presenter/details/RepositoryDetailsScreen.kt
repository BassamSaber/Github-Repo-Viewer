package com.samz.githubreposviewer.presenter.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.samz.githubreposviewer.R
import com.samz.githubreposviewer.domain.model.RepoDetails
import com.samz.githubreposviewer.presenter.base.ErrorScreen
import com.samz.githubreposviewer.theme.GithubReposViewerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailsScreen(
    user: String?, repo: String?,
    viewModel: RepositoryDetailsViewModel = hiltViewModel(),
    navigate: (user: String, repo: String) -> Unit = { _, _ -> },
    popBack: () -> Unit = {}
) {
    val context = LocalContext.current


    LaunchedEffect(user, repo) {
        viewModel.loadRepoDetails(user, repo)
    }

    val state = viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    ).value

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "$repo") }, navigationIcon = {
            IconButton(onClick = { popBack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        })
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (!state.error.isNullOrEmpty() || state.data == null) {
                    ErrorScreen(state.error) {
                        viewModel.loadRepoDetails(user, repo)
                    }
                } else {
                    ScreenContent(repo = state.data, navigate = navigate)

                }
            }
        }
    }
}

@Composable
fun ScreenContent(repo: RepoDetails, navigate: (user: String, repo: String) -> Unit = { _, _ -> }) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.htmlUrl ?: ""))
                        context.startActivity(intent)
                    },
                text = repo.fullRepoName,
                fontSize = 18.sp,
                color = Color.Blue,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(4.dp))
            Icon(
                painter = painterResource(R.drawable.ic_open),
                stringResource(R.string.open_link),
                modifier = Modifier
                    .size(14.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.htmlUrl ?: ""))
                        context.startActivity(intent)
                    },
                tint = Color.Blue
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)

            ) {
                Text(
                    text = repo.visibility ?: "",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                Icons.Sharp.Star,
                stringResource(id = R.string.stars_count),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = repo.starCount.toString(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(10.dp))
            Icon(
                painter = painterResource(R.drawable.ic_watch),
                stringResource(R.string.watchers_count),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(3.dp))
            Text(
                text = repo.watcherCount.toString(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.size(14.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = stringResource(R.string.owner_s_avatar_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                placeholder = rememberVectorPainter(image = Icons.Filled.Person),
                error = rememberVectorPainter(image = Icons.Filled.Person),
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(repo.owner, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.size(14.dp))
        Text(repo.description ?: "", fontSize = 13.sp, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.size(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.language, repo.language ?: ' '),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = stringResource(R.string.forking, repo.allowForking ?: false),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(10.dp))
            if (repo.allowForking == true) {
                Icon(
                    painter = painterResource(R.drawable.ic_git_branch),
                    stringResource(R.string.forks_count),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = repo.forkingCount.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_opend_issues),
                    "issues count",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = stringResource(id = R.string.str_issues),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.size(10.dp))
                if ((repo.openedIssuesCount ?: 0) > 0)
                    Text(
                        modifier = Modifier
                            .drawBehind {
                                drawCircle(
                                    color = Color.LightGray,
                                    radius = this.size.maxDimension
                                )
                            },
                        text = repo.openedIssuesCount?.toString() ?: "",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
            }

            Button(onClick = {
                navigate(repo.owner, repo.name)
            }) {
                Text(text = stringResource(R.string.show_issues))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun previewScreenContent() {
    GithubReposViewerTheme {
        Scaffold { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(14.dp)
            ) {
                ScreenContent(
                    repo = RepoDetails(
                        id = 1,
                        name = "grit",
                        description = "**Grit is no longer maintained. Check out libgit2/rugged.** Grit gives you object oriented read/write access to Git repositories via Ruby.",
                        owner = "mojombo",
                        ownerAvatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                        fullRepoName = "mojombo/grit",
                        isPrivate = false,
                        visibility = "public",
                        language = "Ruby",
                        htmlUrl = "https://github.com/mojombo/grit",
                        forkingCount = 1211,
                        allowForking = true,
                        openedIssuesCount = 421,
                        starCount = 1900,
                        watcherCount = 200
                    )
                )
            }
        }
    }
}