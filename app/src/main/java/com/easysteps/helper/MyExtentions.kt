package com.easysteps.helper

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by NIKUNJ on 09-05-2022.
 */

fun RecyclerView.checkIfEmpty(emptyView: View?) {
    if (emptyView != null && adapter != null) {
        val emptyViewVisible = adapter?.itemCount == 0
        emptyView.isVisible = emptyViewVisible
        isInvisible = emptyViewVisible
    }
}