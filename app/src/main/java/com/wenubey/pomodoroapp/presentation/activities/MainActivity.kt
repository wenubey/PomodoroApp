package com.wenubey.pomodoroapp.presentation.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.wenubey.pomodoroapp.R
import com.wenubey.pomodoroapp.databinding.ActivityMainBinding
import com.wenubey.pomodoroapp.presentation.fragments.HomeFragmentDirections
import com.wenubey.pomodoroapp.presentation.fragments.StatisticsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            binding.fragmentContainerViewNavHost.getFragment<NavHostFragment>().navController

        setupNavigation()
        setToolbar()

    }

    private fun setupNavigation() {
        setupActionBarWithNavController(navController, binding.drawerLayout)
        binding.navView.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    if(navController.currentDestination?.label == "Home") {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        val action = StatisticsFragmentDirections.actionStatisticsFragmentToHomeFragment()
                        navController.navigate(action)
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.nav_statistics -> {
                    if(navController.currentDestination?.label == "Statistics") {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        val action = HomeFragmentDirections.actionHomeFragmentToStatisticsFragment()
                        navController.navigate(action)
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
            }
            true

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    private fun setToolbar() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            findNavController(R.id.fragmentContainerViewNavHost),
            binding.drawerLayout
        )
    }

}