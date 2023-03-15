package com.carlosvaldes.viajerum.helpers

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat

class Helpers {
    companion object {
        fun setupUI(view: View, activity: Activity) {

            // Set up touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                view.setOnTouchListener { v, event ->
                    hideSoftKeyboard(activity)
                    false
                }
            }

            //If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupUI(innerView, activity)
                }
            }
        }
        fun hideSoftKeyboard(activity: Activity) {
            activity.currentFocus?.let {
                val inputMethodManager = ContextCompat.getSystemService(activity, InputMethodManager::class.java)!!
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }
}