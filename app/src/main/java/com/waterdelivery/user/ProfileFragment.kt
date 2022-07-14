package com.waterdelivery.user

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.user.modal.GetProfileResponse
import com.waterdelivery.user.modal.UpdateProfileResponse
import com.waterdelivery.user.modal.UserProfileUpdate
import com.waterdelivery.userfinal.UserFinalActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*


class ProfileFragment : Fragment() {
    lateinit var sharprf: shareprefrences
    var proifileimage: String = ""
    var profileimageString: String? = null
    var picname: String? = null
    lateinit var mainactivity: DashBoardActivity
    val RECORD_REQUEST_CODE = 101
    private val TAG = "PermissionDemo"
    var imageUriSign: Uri? = null
    var mFilePathSign = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainactivity = context as DashBoardActivity
        sharprf = shareprefrences(requireActivity())
        logout.setOnClickListener {
            dialogoepn()
        }
        botton.setOnClickListener {
            startActivity(Intent(requireActivity(), UserFinalActivity::class.java))
        }
//        qr_code.setOnClickListener {
//            var intent=Intent(activity,QrProfileActivity::class.java)
//            intent.putExtra("profileimage",proifileimage)
//            startActivity(intent)
//        }
        txt_update_profile.setOnClickListener {
            if (edt_user_name.text.toString().trim().isEmpty()) {
                toasterror("Please Enter Name")
            } else if (edt_helapername.text.toString().trim().isEmpty()) {
                toasterror("Please Enter Helper  Name")
            } else if (edt_vehicle_name.text.toString().trim().isEmpty()) {
                toasterror("Please Enter Vehicle Number")
            } else {
                updateuserprofile()
            }
        }
        img_take_pic.setOnClickListener(View.OnClickListener {
            picname = "profilepic"
            if (checkpermission()!!) {
                selectImage(requireActivity())
            } else {
                setupPermissions()
            }
        })
        profile()
    }

    fun checkpermission(): Boolean? {
        val camerapermission = (ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                + ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
                + ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA))
        return camerapermission == PackageManager.PERMISSION_GRANTED
    }

    fun setupPermissions() {
        val permissions = (ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                + ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
                + ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA))
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), RECORD_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Log.i(TAG, "Permission has been granted by user")
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Log.i(TAG, "Permission has been denied by user")
                }
                return
            }
        }
    }


    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your profile picture")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 0)

                /* val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                 startActivityForResult(takePicture, 0)*/

            } else if (options[item] == "Choose from Gallery") {
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 ->
                    if (data != null) {

                        var datacamera = data.extras!!.get("data") as Bitmap


                        imageUriSign = getImageUri(requireActivity(), datacamera)
                        mFilePathSign = getAbsolutePath(imageUriSign)
                        Log.e("paths", mFilePathSign)
                        Toast.makeText(requireActivity(), "Image Saved!", Toast.LENGTH_SHORT).show()
                        if (picname == "profilepic") {
                            user_profile.setImageBitmap(datacamera)
                            val baos = ByteArrayOutputStream()
                            datacamera.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val images = baos.toByteArray()
                            // Log.e("imageses",images.toString())
                            profileimageString = Base64.encodeToString(images, Base64.DEFAULT)
                        }
                    }
                1 ->
                    try {
                        imageUriSign = data!!.data
                        val imageStream4: InputStream =
                            mainactivity.contentResolver.openInputStream(imageUriSign!!)!!
                        val selectedImage4 = BitmapFactory.decodeStream(imageStream4)
                        val selectedImageUri = data.data
                        val filePath = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = mainactivity.contentResolver.query(
                            selectedImageUri!!,
                            filePath, null, null, null
                        )
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePath[0])
                        mFilePathSign = cursor.getString(columnIndex)
                        Log.e("paths", mFilePathSign)
                        cursor.close()
                        user_profile.setImageBitmap(selectedImage4)
                        // imageView.tag = "profile";
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(requireActivity(), "Failed!", Toast.LENGTH_SHORT).show()
                    }
            }
            updateProfile()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)


        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "IMG_" + Calendar.getInstance().time,
            null
        )
        return Uri.parse(path)
    }

    fun getAbsolutePath(uri: Uri?): String {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = mainactivity.contentResolver.query(uri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }


    fun updateProfile() {
        mainactivity.showProgressDialog()

        var userid = sharprf.getStringPreference("customer_id")

        val multiPartRepeatString = "application/image"
        var facility_image: MultipartBody.Part? = null


        val user_id: RequestBody = userid.toString().toRequestBody(MultipartBody.FORM)



        if (imageUriSign != null && imageUriSign!!.path != null) {
            val file = File(mFilePathSign)
            val signPicBody = file.asRequestBody(multiPartRepeatString.toMediaTypeOrNull())
            facility_image = MultipartBody.Part.createFormData("profile", file.name, signPicBody)
            // val signPicBody = RequestBody.create(parse.parse(multiPartRepeatString), file)

            //facility_image = createFormData.createFormData("profile_image", file.name, signPicBody)
        }


        var updateprofile: Call<UpdateProfileResponse> =
            APIUtils.getServiceAPI()!!.updateProfileUser(
                "Bearer " + sharprf.getStringPreference("token"),
                user_id,
                facility_image
            )
        updateprofile.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                mainactivity.hideProgressDialog()
                try {

                    if (response.code() == 200) {
                        if (response.body()!!.status == true) {
                            mainactivity.hideProgressDialog()
                            proifileimage = response.body()!!.data
                            Glide.with(this@ProfileFragment).load(response.body()!!.data)
                                .into(user_profile)
                            mainactivity.showToastMessage(activity, response.body()!!.message)

                        } else {
                            mainactivity.hideProgressDialog()
                            mainactivity.showToastMessage(activity, response.body()!!.message)
                        }
                    } else if (response.code() == 401) {

                        sharprf.clearAllPreferences()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finishAffinity()
                    }
                } catch (e: Exception) {
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                mainactivity.hideProgressDialog()
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()

            }

        })

    }


    fun toastsucess(msg: String) {
        activity?.let {
            MotionToast.darkToast(
                it,
                "Hurray success 😍",
                msg,
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(mainactivity, R.font.helvetica_regular)
            )
        }
    }

    fun toasterror(msg: String) {
        activity?.let {
            MotionToast.darkToast(
                it,
                "Sorry ☹ ",
                msg,
                MotionToast.TOAST_ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(mainactivity, R.font.helvetica_regular)
            )
        }

    }


    fun profile() {
        mainactivity.showProgressDialog()
        var signin: Call<GetProfileResponse> = APIUtils.getServiceAPI()!!.userdetails(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString()
        )
        signin.enqueue(object : Callback<GetProfileResponse> {
            override fun onResponse(
                call: Call<GetProfileResponse>,
                response: Response<GetProfileResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        mainactivity.hideProgressDialog()
                        if (response.body()!!.status == true) {
                            edt_user_name.setText(response.body()!!.data.name)
                            txt_user_name.text = response.body()!!.data.name
                            edt_Email.setText(response.body()!!.data.email)
                            edt_phone_number.setText(response.body()!!.data.mobile_number)
                            edt_helapername.setText(response.body()!!.data.helper_name)
                            edt_vehicle_name.setText(response.body()!!.data.vehicle_number_plate)
                            proifileimage = response.body()!!.data.profile
                            if (response.body()!!.data.profile.isNotBlank()) {
                                Glide.with(activity!!).load(response.body()!!.data.profile)
                                    .into(user_profile)
                            }


                        } else {

                        }

                    } else if (response.code() == 401) {
                        mainactivity.hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finishAffinity()


                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                mainactivity.hideProgressDialog()
            }

        })
    }

    fun updateuserprofile() {
        mainactivity.showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        stringStringHashMap.put("name", edt_user_name.text.toString())
        stringStringHashMap.put("email", edt_Email.text.toString())
        stringStringHashMap.put("mobile_number", edt_phone_number.text.toString())
        stringStringHashMap.put("helper_name", edt_helapername.text.toString())
        stringStringHashMap.put("vehicle_number_plate", edt_vehicle_name.text.toString())
        var signin: Call<UserProfileUpdate> = APIUtils.getServiceAPI()!!.updateuser(
            "Bearer " + sharprf.getStringPreference("token"),
            stringStringHashMap,
            sharprf.getStringPreference("customer_id").toString()
        )
        signin.enqueue(object : Callback<UserProfileUpdate> {
            override fun onResponse(
                call: Call<UserProfileUpdate>,
                response: Response<UserProfileUpdate>
            ) {

                try {
                    if (response.code() == 200) {
                        mainactivity.hideProgressDialog()
                        profile()
                        toastsucess(response.body()!!.message)
                        if (response.body()!!.status == true) {
                            toastsucess(response.body()!!.message)
//                            mainactivity.showToastMessage(activity!!,response!!.body()!!.message)


                        } else {

                        }

                    } else if (response.code() == 401) {
                        mainactivity.hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finishAffinity()

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<UserProfileUpdate>, t: Throwable) {
                Log.e("dvhgd", t.message.toString())
                mainactivity.hideProgressDialog()
            }

        })
    }


    fun dialogoepn() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Logout")
            .setMessage("Are you sure do you want to logout ?") // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                sharprf.clearAllPreferences()
                startActivity(Intent(activity, LoginActivity::class.java))
                requireActivity().finishAffinity()
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.no, null)
//            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }


}