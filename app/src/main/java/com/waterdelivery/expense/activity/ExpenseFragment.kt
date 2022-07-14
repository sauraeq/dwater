package com.waterdelivery.expense.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
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
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.expense.adpater.ExpensesAdpater
import com.waterdelivery.expense.modal.AddExpenseResponse
import com.waterdelivery.expense.modal.ExpenseTypeResponse
import com.waterdelivery.expense.modal.ExpensesListResponce
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.fragment_expense.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_wallet_history.*
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


class ExpenseFragment : Fragment(), cont {
    lateinit var dashBoardActivity: DashBoardActivity
    lateinit var sharprf: shareprefrences
    lateinit var typelist: ArrayList<String>
    var selectedtype: String = ""

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
        return inflater.inflate(R.layout.fragment_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharprf = shareprefrences(requireActivity())
        dashBoardActivity = context as DashBoardActivity
        mainactivity = context as DashBoardActivity
        recy_expenses.layoutManager = LinearLayoutManager(context)
        typelist = arrayListOf()
        typelist()
        expensesList()
        add_exp.setOnClickListener {
            opendialog()
        }
    }

    fun opendialog() {
        val dialog = context?.let { Dialog(it, R.style.DialogCustomTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  dialog.setCancelable(false)
        dialog?.setContentView(R.layout.add_expenses_layout)
        dialog?.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var spinner = dialog.findViewById<Spinner>(R.id.spinner)
        val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, typelist)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa
        dialog.findViewById<TextView>(R.id.add_expensive).setOnClickListener {
            if (selectedtype.isEmpty()) {
                toasterror("Please Select Type")
            } else if (dialog.findViewById<EditText>(R.id.ammount).text.toString().trim()
                    .isEmpty()
            ) {
                toasterror("Please Enter Ammount")
            } else if (dialog.findViewById<EditText>(R.id.comment).text.toString().trim()
                    .isEmpty()
            ) {
                toasterror("Please Enter Comment")
            }else if(imageUriSign==null){
                toasterror("Please Select Image ")
            } else {
                addexpense(
                    dialog.findViewById<EditText>(R.id.ammount).text.toString().trim(),
                    dialog.findViewById<EditText>(R.id.comment).text.toString().trim(),
                    dialog
                )
            }
        }
        dialog.findViewById<ImageView>(R.id.close_dialog).setOnClickListener {
            dialog.dismiss()
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectedtype = typelist[position]
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        dialog.findViewById<ImageView>(R.id.iamgeupload).setOnClickListener {
            picname = "profilepic"
            if (checkpermission()!!) {
                selectImage(requireActivity())
            } else {
                setupPermissions()
            }
        }
        dialog.show()


    }

    fun expensesList() {
        dashBoardActivity.showProgressDialog()
        var signin: Call<ExpensesListResponce> = APIUtils.getServiceAPI()!!.expensesList(
            "Bearer " + sharprf.getStringPreference(Token),
            sharprf.getStringPreference(USER_ID).toString()
        )
        signin.enqueue(object : Callback<ExpensesListResponce> {
            override fun onResponse(
                call: Call<ExpensesListResponce>,
                response: Response<ExpensesListResponce>
            ) {
                try {
                    if (response.code() == 200) {
                        dashBoardActivity.hideProgressDialog()
                        if (response.body()!!.status == true) {
                            expens_tv2.text = response.body()!!.driver_details.driver_name
                            adddrsss.text = response.body()!!.driver_details.driver_address
                            adddrsss.text = response.body()!!.driver_details.driver_address
                            txt_total_expense.text =
                              "AED " + response.body()!!.driver_details.driver_total_amount.toString()
                            Glide.with(this@ExpenseFragment)
                                .load(response.body()!!.driver_details.driver_image)
                                .into(expense_profile)
                            var obj = response.body()!!.data
                            recy_expenses.adapter = ExpensesAdpater(
                                activity!!,
                                obj as ArrayList<ExpensesListResponce.Data>)
                            if(response!!.body()!!.data.size>0){
                                errorrrrrrr.visibility=View.GONE
                            }else{
                                errorrrrrrr.visibility=View.VISIBLE
                            }
                        } else {

                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finishAffinity()
                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<ExpensesListResponce>, t: Throwable) {
                dashBoardActivity.hideProgressDialog()

            }

        })
    }


    fun typelist() {
        dashBoardActivity.showProgressDialog()
        var signin: Call<ExpenseTypeResponse> = APIUtils.getServiceAPI()!!.expensesTypeList(
            "Bearer " + sharprf.getStringPreference("token")
        )
        signin.enqueue(object : Callback<ExpenseTypeResponse> {
            override fun onResponse(
                call: Call<ExpenseTypeResponse>,
                response: Response<ExpenseTypeResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        dashBoardActivity.hideProgressDialog()
                        if (response.body()!!.status == true) {
                            selectedtype = response.body()!!.data[0].name
                            for (i in 0..response.body()!!.data.size) {
                                typelist.add(response.body()!!.data[i].name)
                            }
                        } else {

                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finishAffinity()


                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<ExpenseTypeResponse>, t: Throwable) {

            }

        })
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
//                            user_profile.setImageBitmap(datacamera)
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
//                        user_profile.setImageBitmap(selectedImage4)
                        // imageView.tag = "profile";
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(requireActivity(), "Failed!", Toast.LENGTH_SHORT).show()
                    }
            }
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


    fun addexpense(ammount: String, comeent: String, dialog: Dialog) {
        mainactivity.showProgressDialog()

        var userid = sharprf.getStringPreference("customer_id")


        val multiPartRepeatString = "application/image"
        var facility_image: MultipartBody.Part? = null


        val user_id: RequestBody = userid.toString().toRequestBody(MultipartBody.FORM)
        val ammountrequest: RequestBody = ammount.toString().toRequestBody(MultipartBody.FORM)
        val comeentrequest: RequestBody = comeent.toString().toRequestBody(MultipartBody.FORM)
        val typerequest: RequestBody =
            selectedtype.toString().toRequestBody(MultipartBody.FORM)



        if (imageUriSign != null && imageUriSign!!.path != null) {
            val file = File(mFilePathSign)
            val signPicBody = file.asRequestBody(multiPartRepeatString.toMediaTypeOrNull())
            facility_image = MultipartBody.Part.createFormData("image", file.name, signPicBody)
            // val signPicBody = RequestBody.create(parse.parse(multiPartRepeatString), file)

            //facility_image = createFormData.createFormData("profile_image", file.name, signPicBody)
        }


        var updateprofile: Call<AddExpenseResponse> = APIUtils.getServiceAPI()!!.addexpensive(
            "Bearer " + sharprf.getStringPreference("token"),
            user_id,
            typerequest,
            ammountrequest,
            comeentrequest,
            facility_image
        )
        updateprofile.enqueue(object : Callback<AddExpenseResponse> {
            override fun onResponse(
                call: Call<AddExpenseResponse>,
                response: Response<AddExpenseResponse>
            ) {
                mainactivity.hideProgressDialog()
                try {

                    if (response.code() == 200) {
                        mainactivity.hideProgressDialog()
                        if (response.body()!!.status == true) {
                            dialog.dismiss()
                            expensesList()
                            mainactivity.showToastMessage(activity, response.body()!!.message)

                        } else {
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

            override fun onFailure(call: Call<AddExpenseResponse>, t: Throwable) {
                mainactivity.hideProgressDialog()
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()

            }

        })

    }


    fun toastsucess(msg: String) {
        MotionToast.darkToast(
            requireActivity(),
            "Hurray success 😍",
            msg,
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
        )
    }

    fun toasterror(msg: String) {

        MotionToast.darkToast(
            requireActivity(),
            "Sorry ☹ ",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
        )

    }
}