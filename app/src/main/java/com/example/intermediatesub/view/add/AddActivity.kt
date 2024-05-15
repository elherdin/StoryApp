package com.example.intermediatesub.view.add

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.intermediatesub.databinding.ActivityAddBinding
import com.example.intermediatesub.view.main.MainActivity
import com.example.intermediatesub.view.utils.ViewModelFactory
import com.example.intermediatesub.view.utils.getImageUri
import com.example.intermediatesub.view.utils.reduceFileImage
import com.example.intermediatesub.view.utils.uriToFile


class AddActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityAddBinding
    private val viewModel by viewModels<AddViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            loadingAdd.visibility = View.GONE
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnUpload.setOnClickListener {
                uploadImage()
                loadingAdd.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.tvDeskripsiAdd.text.toString()
            binding.loadingAdd.visibility = View.VISIBLE

            viewModel.uploadImage(imageFile, description)
            viewModel.resultUpload.observe(this) { result ->
                val alertDialog: AlertDialog.Builder?
                if (result.error == true) {
                    alertDialog = AlertDialog.Builder(this).apply {
                        setTitle("Sorry !")
                        setMessage(result.message)
                        setNegativeButton("Next") { dialog, _ ->
                            dialog.cancel()
                            dialog.dismiss()
                        }
                        create()
                    }
                    alertDialog.show()
                } else {
                    alertDialog = AlertDialog.Builder(this).apply {
                        setTitle("Yeah !")
                        setMessage(result.message)
                        setNegativeButton("Next") { dialog, _ ->
                            val act = Intent(this@AddActivity, MainActivity::class.java)
                            startActivity(act)
                            dialog.cancel()
                            dialog.dismiss()
                        }
                        create()
                    }
                    alertDialog.show()
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo pic", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivStoryAdd.setImageURI(it)
        }
    }
}