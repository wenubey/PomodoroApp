package com.wenubey.pomodoroapp.utils

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.wenubey.pomodoroapp.R
import dagger.hilt.android.qualifiers.ApplicationContext

fun makeToast(message: String, context: Context){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}

fun Fragment.addMenuProvider(@MenuRes menuRes: Int, callback: (id: Int) -> Boolean) {
    val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuRes, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem) = callback(menuItem.itemId)

    }
    (requireActivity() as MenuHost).addMenuProvider(
        menuProvider,
        viewLifecycleOwner,
        Lifecycle.State.RESUMED
    )
}

fun makeAlertDialog(context: Context) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle("How To Use")
    alertDialogBuilder.setMessage(R.string.how_to_use)
    alertDialogBuilder.create()
    alertDialogBuilder.show()
}