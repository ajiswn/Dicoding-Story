package com.ajiswn.dicodingstory.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ajiswn.dicodingstory.R
import com.ajiswn.dicodingstory.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        setupActionBar()
        displayStoryDetails()
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.story_detail)
    }

    private fun displayStoryDetails() {
        val storyImage = intent.getStringExtra(EXTRA_STORY_IMAGE)
        val storyName = intent.getStringExtra(EXTRA_STORY_NAME)
        val storyDescription = intent.getStringExtra(EXTRA_STORY_DESCRIPTION)

        binding.run {
            Glide.with(root.context)
                .load(storyImage)
                .into(ivPhoto)
            tvName.text = storyName
            tvDescription.text = storyDescription
        }
    }

    companion object {
        const val EXTRA_STORY_IMAGE = "extra_story_image"
        const val EXTRA_STORY_NAME = "extra_story_name"
        const val EXTRA_STORY_DESCRIPTION = "extra_story_description"
    }
}