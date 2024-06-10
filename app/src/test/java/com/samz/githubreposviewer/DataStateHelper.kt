package com.samz.githubreposviewer

import app.cash.turbine.FlowTurbine
import com.samz.githubreposviewer.presenter.base.DataState

// There's a race condition where intermediate states can get missed if the next state comes too fast.
// This function addresses that by awaiting an item that may or may not be preceded by the specified other items
suspend fun <T> FlowTurbine<DataState<T>>.awaitItemPrecededBy(vararg items: DataState<T>): DataState<T> {
    var nextItem = awaitItem()
    for (item in items) {
        if (item == nextItem) {
            nextItem = awaitItem()
        }
    }
    return nextItem
}