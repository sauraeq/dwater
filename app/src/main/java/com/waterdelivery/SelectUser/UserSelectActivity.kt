package com.waterdelivery.SelectUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.Shop.ShopActivity
import com.waterdelivery.customer.fragement.model.SearchResponse
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_user_select.*
import kotlinx.android.synthetic.main.fragment_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

class UserSelectActivity : BaseActivity() {
    var input: String = ""
    lateinit var sharprf:shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_select)
        sharprf= shareprefrences(this)
        searchdta()
//        searchadd.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                validateemailandphone()
//                return@OnEditorActionListener true
//            }
//            false
//        })


        searchadd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length > 2) {
                    input = searchadd.text.toString()
                    searchdta()
                }
            }
        })
//        btn_checkout.setOnClickListener {
//            startActivity(Intent(this,ShopActivity::class.java))
//        }
        backreturnuser.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateemailandphone() {
        if(searchadd.text.toString().trim().isNotEmpty()) {
            if (searchadd.text.toString().trim().matches("-?\\d+(.\\d+)?".toRegex())) {
                if (searchadd.text.toString().trim().length != 10) {
                    Toast.makeText(this, "Please Enter Valid phone number", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    searchdta()
                }

            } else {
                if (!emailValidatogggr(searchadd.text.toString().trim())) {
                    Toast.makeText(this, "Please Enter Vaild Email", Toast.LENGTH_SHORT).show()
                }else{
                    searchdta()
                }

            }
        }else{
            Toast.makeText(this, "Please Enter Email/Phone for search", Toast.LENGTH_SHORT).show()

        }
    }

    fun emailValidatogggr(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }


    fun searchdta() {
        showProgressDialog()
        val stringStringHashMap = HashMap<String, String>()
        stringStringHashMap.put("email", "")
        var signin: Call<SearchResponse> = APIUtils.getServiceAPI()!!.search(
            "Bearer " + sharprf.getStringPreference("token"),
            searchadd.text.toString().trim()
        )
        signin.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == true) {
                            userseectrecycle.layoutManager=LinearLayoutManager(this@UserSelectActivity)
                            userseectrecycle.adapter=UserSelectionAdapter(this@UserSelectActivity,response!!.body()!!.data)
                          if(response!!.body()!!.data.size>0){
                              tv_text.visibility=View.GONE
                          }else{
                              tv_text.visibility=View.VISIBLE
                          }

                        } else {
                            toasterror(response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@UserSelectActivity, LoginActivity::class.java))
                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(
                            this@UserSelectActivity,
                            response.body()!!.message
                        )

                    } else if (response.code() == 500) {
                        hideProgressDialog()
//                        mainacivity.showToastMessage(context,response!!.body()!!.message)

                    }

                } catch (e: Exception) {


                }

            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

    fun toasterror(msg: String) {
        MotionToast.darkToast(
            this,
            "Sorry ☹ ",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular)
        )
    }

    fun toastsucess(msg: String) {
        this?.let {
            MotionToast.darkToast(
                it,
                "Hurray success 😍",
                msg,
                MotionToast.TOAST_SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.helvetica_regular)
            )
        }
    }
}