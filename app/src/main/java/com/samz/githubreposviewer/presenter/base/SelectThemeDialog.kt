package com.samz.githubreposviewer.presenter.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.samz.githubreposviewer.R
import com.samz.githubreposviewer.presenter.list.MainEvent
import com.samz.githubreposviewer.presenter.list.MainViewModel
import com.samz.githubreposviewer.theme.AppTheme

@Composable
fun SelectThemeDialog(
    mainViewModel: MainViewModel,
    setShowDialog: (Boolean) -> Unit,
    returnValue: (AppTheme) -> Unit,
) {
    Dialog(
        onDismissRequest = { setShowDialog(false) },
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                ItemSelectRadioButton(
                    title = "Default Theme",
                    onClick = {
                        if (mainViewModel.stateApp.theme != AppTheme.Default) {
                            mainViewModel.onEvent(MainEvent.ThemeChange(AppTheme.Default))
                            setShowDialog(false)
                            returnValue(AppTheme.Default)
                        }
                    },
                    isSelect = mainViewModel.stateApp.theme == AppTheme.Default
                )
                ItemSelectRadioButton(
                    title = "Light Theme",
                    onClick = {
                        if (mainViewModel.stateApp.theme != AppTheme.Light) {
                            mainViewModel.onEvent(MainEvent.ThemeChange(AppTheme.Light))
                            setShowDialog(false)
                            returnValue(AppTheme.Light)
                        }
                    },
                    isSelect = mainViewModel.stateApp.theme == AppTheme.Light
                )
                ItemSelectRadioButton(
                    title = "Dark Theme",
                    onClick = {
                        if (mainViewModel.stateApp.theme != AppTheme.Dark) {
                            mainViewModel.onEvent(MainEvent.ThemeChange(AppTheme.Dark))
                            setShowDialog(false)
                            returnValue(AppTheme.Dark)
                        }
                    },
                    isSelect = mainViewModel.stateApp.theme == AppTheme.Dark
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun ItemSelectRadioButton(
    title: String,
    onClick: () -> Unit,
    isSelect: Boolean
) {
    Column(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = if (isSelect)
                    painterResource(id = R.drawable.ic_check_box) else
                    painterResource(id = R.drawable.ic_un_check_box),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1.0f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}