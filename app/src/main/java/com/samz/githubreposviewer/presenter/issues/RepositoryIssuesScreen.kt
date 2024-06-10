package com.samz.githubreposviewer.presenter.issues

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.samz.githubreposviewer.R
import com.samz.githubreposviewer.domain.model.Issue
import com.samz.githubreposviewer.presenter.base.ErrorScreen
import com.samz.githubreposviewer.theme.GithubReposViewerTheme
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryIssuesScreen(
    user: String?, repo: String?,
    viewModel: RepositoryIssuesViewModel = hiltViewModel(),
    popBack: () -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(user, repo) {
        viewModel.loadRepoIssues(user, repo)
    }

    val state = viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    ).value

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(R.string.issues, repo ?: "")) },
            navigationIcon = {
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
                        viewModel.loadRepoIssues(user, repo)
                    }
                } else {
                    ScreenContent(issues = state.data, context = context)
                }
            }
        }
    }
}

@Composable
fun ScreenContent(issues: List<Issue>, context: Context) {
    LazyColumn {
        items(issues) { item ->
            IssueItemRow(item)
            Divider(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp),
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun IssueItemRow(item: Issue) {
    Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(
                    id =
                    if (item.isClosed) R.drawable.ic_closed else R.drawable.ic_opend_issues
                ),
                contentDescription = stringResource(id = R.string.issue_state),
                tint = if (item.isClosed) Color.Magenta else Color.Green,
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = if (item.isClosed) stringResource(
                R.string.was_closed_at,
                item.id,
                item.userName,
                formatDate(item.closedDate ?: "")
            )
            else stringResource(
                R.string.was_opened_at,
                item.id,
                item.userName,
                formatDate(item.createdDate)
            ),
            modifier = Modifier.padding(start = 20.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.DarkGray
        )

    }
}

fun formatDate(date: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.UK)
    return sdf.parse(date)?.let {
        val sdf2 = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        sdf2.format(it)
    } ?: date
}

@Preview
@Composable
fun previewIssueItemRow() {
    GithubReposViewerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            IssueItemRow(
                item = Issue(
                    1L,
                    "Opened issue number 1",
                    "Ali Ahmed",
                    "",
                    state = "cldose",
                    createdDate = "2022-12-20",
                    closedDate = "2023-01-21"
                )
            )
        }
    }
}


