package com.ajiswn.dicodingstory.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajiswn.dicodingstory.R
import com.ajiswn.dicodingstory.ViewModelFactory
import com.ajiswn.dicodingstory.databinding.ActivityMainBinding
import com.ajiswn.dicodingstory.view.adapter.StoryAdapter
import com.ajiswn.dicodingstory.view.addnewstory.AddNewStoryActivity
import com.ajiswn.dicodingstory.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private val adapter by lazy { StoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.mipmap.ic_launcher_foreground)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                navigateToWelcome()
            } else {
                viewModel.fetchStories()
                setupRecyclerView()
                observeViewModel()
            }
        }

        binding.addStoryButton.setOnClickListener{
            startActivity(Intent(this, AddNewStoryActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStories()
    }

    private fun setupRecyclerView() {
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            stories.observe(this@MainActivity) { list->
                adapter.submitList(list)
                if (list.isNullOrEmpty()) {
                    showToast(getString(R.string.no_stories_available))
                }
            }
            isLoading.observe(this@MainActivity) { binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE }
            errorMessage.observe(this@MainActivity) { message ->
                message?.let {
                    showToast(it)
                    viewModel.resetErrorMessage()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.action_logout -> {
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToWelcome() {
        Intent(this, WelcomeActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}