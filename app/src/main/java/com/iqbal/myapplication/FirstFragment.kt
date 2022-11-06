package com.iqbal.myapplication



import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iqbal.myapplication.databinding.FragmentFirstBinding
import com.mvp.handyopinion.UploadUtility
import java.net.URI


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private  var flagImage: Int = 0
    private  var uriPath: String = ""
    private  var fillName: String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Open Gallery
        binding.uploadImage.setOnClickListener {
            pickImageFromGallery()
        }

        // submit the form to Server
        binding.submitButton.setOnClickListener{
            Log.d("fillName", "fillName" + binding.fillName.text)
            //Check empty validation
            if (flagImage == 0 && binding.fillName.text.toString().trim().length == 0) {
                showToast("Please select an image and fill name")
            }else{
                uploadImage(uriPath,binding.fillName.text.toString())
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }

    }

    val pickImageFromGalleryForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
//            val intent = result.data
            binding.uploadImage.setImageURI(result.data?.data)
            binding.uploadImage.layoutParams.height = 400
            flagImage = 1
            uriPath = result.data?.data.toString()
        }
    }

    private  fun uploadImage(uriPath: String, fillName: String){
        UploadUtility(this.requireActivity()).uploadFile(uriPath, fillName) // Either Uri, File or String file path
    }

    private fun pickImageFromGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        pickImageFromGalleryForResult.launch(pickIntent)
    }


//    private fun checkPermissionForImage() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((checkSelfPermission(this.context, Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
//                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
//            ) {
//                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//                requestPermissions(permission, PERMISSION_CODE_READ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
//                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
//            } else {
//                pickImageFromGallery()
//            }
//        }
//    }

    fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText( activity, message, Toast.LENGTH_LONG ).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}