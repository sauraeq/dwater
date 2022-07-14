package com.waterdelivery.returndata.adapter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.waterdelivery.R
import kotlinx.android.synthetic.main.activity_add_retrun.*
import java.util.ArrayList
import android.view.inputmethod.EditorInfo

import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.customer.adpater.ListCustomerAdpater
import com.waterdelivery.customer.fragement.model.SearchResponse
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.returndata.model.AddReturnResponse
import com.waterdelivery.returndata.model.ListResponse
import com.waterdelivery.store.adpater.StoreAdpater
import com.waterdelivery.store.model.StoreListResponse
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_storek.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.error


class AddRetrunActivity : BaseActivity(),cont {
    lateinit var prdoctlist: ArrayList<String>
    lateinit var returnlist: ArrayList<String>
    lateinit var narratonlist: ArrayList<String>
    var regexStr = "^[0-9]*$"
    lateinit var sharprf:shareprefrences
    var selectqunaity:String=""
    var selectproducttype:String=""
    var selectreturntype:String=""
    var selectnarration:String=""
    var customerid:String=""
    var qunatity = arrayOf(
        "Quanity", "1",
        "2", "3",
        "4", "5","6","7","8","9","10")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_retrun)
        prdoctlist = arrayListOf()
        returnlist =  arrayListOf()
        narratonlist= arrayListOf()
        prdoctlist.add("Product Type")
        returnlist.add("Return Type")
        narratonlist.add("Narration")
        sharprf= shareprefrences(this)
        prdouctlist()
        returnlist()
        narrarionlist()
        backreturnadd.setOnClickListener {
            onBackPressed()
        }
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, qunatity)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerqunaty.adapter = aa
        spinnerqunaty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectqunaity = qunatity[position]
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinnerproduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectproducttype = prdoctlist[position]
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinnerreturn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectreturntype = returnlist[position]
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinnernarration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectnarration = narratonlist[position]
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        searchadd.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                validateemailandphone()
                return@OnEditorActionListener true
            }
            false
        })

        add_expensive.setOnClickListener {
            validatefrom()
        }

    }

    private fun validatefrom() {
        if(customerid.isEmpty()){
            toasterror("Please Search Customer")
        }else if(selectproducttype.isEmpty()){
            toasterror("Please Select Product Type")
        }else if(selectreturntype.isEmpty()){
            toasterror("Please Select Return Type")
        }else if(selectnarration.isEmpty()){
            toasterror("Please Select Narration ")
        }else if(selectqunaity.isEmpty()){
            toasterror("Please Select Narration ")
        }else if(serialnumber.text.toString().trim().isEmpty()){
            toasterror("Please Enter  Serial Number ")
        }else if(Remark.text.toString().trim().isEmpty()){
            toasterror("Please Enter  Serial Number ")
        }else{
            addreturn()
        }
    }

    private fun validateemailandphone() {
        if(searchadd.text.toString().trim().isNotEmpty()) {
            if (searchadd.text.toString().trim().matches("-?\\d+(.\\d+)?".toRegex())) {
                if (searchadd.text.toString().trim().length != 10) {
                    searchdta()
                  //  Toast.makeText(this, "Please Enter Valid phone number", Toast.LENGTH_SHORT).show()
                } else {
                searchdta()
                }

            } else {
                if (!emailValidatogggr(searchadd.text.toString().trim())) {
                    searchdta()
                    //Toast.makeText(this, "Please Enter Vaild Email", Toast.LENGTH_SHORT).show()
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
                            if (response.body()!!.data.size > 0) {
                                customerdata.visibility=View.VISIBLE
                                Glide.with(this@AddRetrunActivity).load(response!!.body()!!.data[0].profile).into(return_profile)
                                return_cus_name.text=response!!.body()!!.data[0].name
                                retuenphone.text=response!!.body()!!.data[0].mobile_number
                                customerid=response!!.body()!!.data[0].id

                            } else {
                                customerid=""
                                customerdata.visibility=View.GONE
                                toasterror(response!!.body()!!.message)
                            }

                        } else {
                            toasterror(response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@AddRetrunActivity, LoginActivity::class.java))
                    finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                       showToastMessage(
                            this@AddRetrunActivity,
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


    fun prdouctlist() {
       showProgressDialog()
        var signin: Call<ListResponse> = APIUtils.getServiceAPI()!!.getProductType(
            "Bearer " + sharprf.getStringPreference("token")
        )
        signin.enqueue(object : Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                            for (i in 0..response.body()!!.data.size-1) {
                                prdoctlist.add(response.body()!!.data[i].name)
                            }
                            setproductlist()
                        } else {
                            hideProgressDialog()
                        }
                    } else if (response.code() == 401) {
                       hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@AddRetrunActivity, LoginActivity::class.java))
                      finishAffinity()
                    }
                } catch (e: Exception) {
                    Log.e("erroeeee",e.toString())

                }

            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
               hideProgressDialog()
            }

        })
    }

    private fun setproductlist() {
        val aaa = ArrayAdapter(this, android.R.layout.simple_spinner_item, prdoctlist)
        aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerproduct.adapter = aaa
    }

    private fun setreturnlist() {
        val aaaa = ArrayAdapter(this, android.R.layout.simple_spinner_item, returnlist)
        aaaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerreturn.adapter = aaaa
    }

    private fun setnarrowlist() {
        val aaaaa = ArrayAdapter(this, android.R.layout.simple_spinner_item, narratonlist)
        aaaaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnernarration.adapter = aaaaa
    }

    fun returnlist() {
        showProgressDialog()
        var signin: Call<ListResponse> = APIUtils.getServiceAPI()!!.getReturnType(
            "Bearer " + sharprf.getStringPreference("token")
        )
        signin.enqueue(object : Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                            for (i in 0..response.body()!!.data.size-1) {
                                returnlist.add(response.body()!!.data[i].name)
                            }
                            setreturnlist()
                        } else {
                            hideProgressDialog()
                        }
                    } else if (response.code() == 401) {
                        hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@AddRetrunActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                } catch (e: Exception) {
                    Log.e("erroeeee",e.toString())

                }

            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }
    fun narrarionlist() {
        showProgressDialog()
        var signin: Call<ListResponse> = APIUtils.getServiceAPI()!!.getNarrationType(
            "Bearer " + sharprf.getStringPreference("token")
        )
        signin.enqueue(object : Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                            for (i in 0..response.body()!!.data.size-1) {
                                narratonlist.add(response.body()!!.data[i].name)
                            }
                            setnarrowlist()
                        } else {
                            hideProgressDialog()
                        }
                    } else if (response.code() == 401) {
                        hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@AddRetrunActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                } catch (e: Exception) {
                    Log.e("erroeeee",e.toString())

                }

            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

    fun addreturn() {
        showProgressDialog()
        var signin: Call<AddReturnResponse> = APIUtils.getServiceAPI()!!.addReturn(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference(USER_ID).toString(),
            customerid,
            selectproducttype,
            selectreturntype,
            selectnarration,
            selectqunaity,
            serialnumber.text.toString(),
            Remark.text.toString()
        )
        signin.enqueue(object : Callback<AddReturnResponse> {
            override fun onResponse(
                call: Call<AddReturnResponse>,
                response: Response<AddReturnResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                           toastsucess(response!!.body()!!.message)
                            startActivity(Intent(this@AddRetrunActivity,DashBoardActivity::class.java))
                        } else {
                            hideProgressDialog()
                        }
                    } else if (response.code() == 401) {
                        hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@AddRetrunActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                } catch (e: Exception) {
                    Log.e("erroeeee",e.toString())

                }

            }

            override fun onFailure(call: Call<AddReturnResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }
}