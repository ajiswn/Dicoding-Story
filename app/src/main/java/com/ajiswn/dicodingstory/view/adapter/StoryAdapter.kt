package com.ajiswn.dicodingstory.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ajiswn.dicodingstory.data.remote.response.ListStoryItem
import com.ajiswn.dicodingstory.databinding.ItemStoryBinding
import com.ajiswn.dicodingstory.view.detail.DetailActivity
import com.bumptech.glide.Glide

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StoryViewHolder(
        ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.apply {
                tvName.text = story.name
                tvDescription.text = story.description
                Glide.with(root.context).load(story.photoUrl).into(ivPhoto)

                root.setOnClickListener { navigateToDetail(story) }
            }
        }

        private fun navigateToDetail(story: ListStoryItem) {
            val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_STORY_IMAGE, story.photoUrl)
                putExtra(DetailActivity.EXTRA_STORY_NAME, story.name)
                putExtra(DetailActivity.EXTRA_STORY_DESCRIPTION, story.description)
            }

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                itemView.context as Activity,
                Pair(binding.ivPhoto, "image"),
                Pair(binding.tvName, "name"),
                Pair(binding.tvDescription, "description")
            )
            itemView.context.startActivity(intent, options.toBundle())
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem) = oldItem == newItem
        }
    }
}