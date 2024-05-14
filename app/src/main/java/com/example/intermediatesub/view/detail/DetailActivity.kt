package com.example.intermediatesub.view.detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.intermediatesub.data.response.ListStoryItem
import com.example.intermediatesub.databinding.ActivityDetailBinding
import com.example.intermediatesub.view.main.StoryAdapter
import com.example.intermediatesub.view.utils.dateFormatter

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var detailStory: ListStoryItem
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailStory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(StoryAdapter.PARCEL_NAME)!!
        } else {
            intent.getParcelableExtra(StoryAdapter.PARCEL_NAME)!!
        }

        Glide.with(binding.root)
            .load(detailStory.photoUrl)
            .into(binding.ivStoryDt)
        binding.tvNameDt.text = detailStory.name
        binding.tvDescription.text = detailStory.description
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tvDate.text = dateFormatter(detailStory.createdAt)
        }
    }
}
