package com.waterdelivery.returndata

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.rehabcare.user.datePickerHelper
import com.waterdelivery.R
import com.waterdelivery.ReturndataResponse
import com.waterdelivery.returndata.adapter.AddRetrunActivity
import com.waterdelivery.returndata.adapter.RetrunDateAdapter
import com.waterdelivery.task.fragement.TaskListFragment
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_return.*
import kotlinx.android.synthetic.main.activity_return.select_date
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.fragment_wallet_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ReturnActivity : BaseActivity(), cont {
    lateinit var sharprf: shareprefrences

    lateinit var datePicker: datePickerHelper
    var date:String =""
    private var oneWayTripDate: Date? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return)
        sharprf = shareprefrences(this)

        datePicker = datePickerHelper(this)

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val formatted = current.format(formatter)
        date=formatted.toString()


        add_return.setOnClickListener {
            startActivity(Intent(this, AddRetrunActivity::class.java))
        }
        backreturn.setOnClickListener {
            onBackPressed()
        }
        select_date.setOnClickListener {
            showDatePickerDialog()
        }

    }
    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)

        datePicker.showDialog(d, m, y, object : datePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {


                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                var data:String
                data="${year}-${monthStr}-${dayStr}"

               // date=(data)

                val dates = data
                val input = SimpleDateFormat("yyyy-MM-dd")
                val output = SimpleDateFormat("dd MMMM yyyy")
                oneWayTripDate = input.parse(dates) // parse input

                date=(output.format(oneWayTripDate))
                text_select_date1.setText(output.format(oneWayTripDate))
                returnapi()

            }


        })
    }

    fun returnapi() {
        showProgressDialog()
        var signin: Call<ReturndataResponse> = APIUtils.getServiceAPI()!!.returnList(
            "Bearer " + sharprf.getStringPreference(Token),
            sharprf.getStringPreference(USER_ID).toString()
        )
        signin.enqueue(object : Callback<ReturndataResponse> {
            override fun onResponse(
                call: Call<ReturndataResponse>,
                response: Response<ReturndataResponse>,
            ) {
                try {
                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == true) {
                            Glide.with(this@ReturnActivity)
                                .load(response.body()!!.driver_details.profile)
                                .into(driver_profile)
                            expens_tv2.text = response.body()!!.driver_details.name
                            adddrsss.text = response.body()!!.driver_details.mobile_number
                            recy_return.layoutManager = LinearLayoutManager(this@ReturnActivity)
                            recy_return.adapter = RetrunDateAdapter(this@ReturnActivity,date,
                                response.body()!!.data as ArrayList<ReturndataResponse.Data>)

                        } else {

                        }

                    } else if (response.code() == 401) {

                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@ReturnActivity, LoginActivity::class.java))
                        finishAffinity()

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<ReturndataResponse>, t: Throwable) {
                hideProgressDialog()

            }

        })
    }

    override fun onResume() {
        super.onResume()
        returnapi()
    }
}