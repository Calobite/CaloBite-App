package com.example.capstone.ui.analyze

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.capstone.data.ViewModelFactory
import com.example.capstone.data.getImageUri
import com.example.capstone.data.uriToFile
import com.example.capstone.databinding.FragmentAnalyzeBinding
import com.example.capstone.ui.ResultActivity

class AnalyzeFragment : Fragment() {
    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<AnalyzeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Analyze"

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
        observeViewModel()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun observeViewModel() {
        viewModel.predictionResult.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                val prediction = it.predictions.firstOrNull()
                if (prediction != null) {
                    // Use label to search for food
                    viewModel.searchFoodByIngredient(prediction.label)
                } else {
                    showToast("No prediction found")
                }
            }
        })

        viewModel.foodSearchResult.observe(viewLifecycleOwner, Observer { foodResponse ->
            foodResponse?.let { response ->
                if (response.status == "success" && response.data.isNotEmpty()) {
                    val intent = Intent(requireContext(), ResultActivity::class.java)
                    intent.putParcelableArrayListExtra("FOOD_DATA", ArrayList(response.data))
                    startActivity(intent)
                } else {
                    showToast("No recipes found for the predicted label")
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                showToast(it)
            }
        })
    }


    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext())
            viewModel.uploadImage(imageFile)
        } ?: showToast("No image selected")
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
