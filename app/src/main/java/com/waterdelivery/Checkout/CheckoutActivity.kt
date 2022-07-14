package com.waterdelivery.Checkout

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.google.gson.GsonBuilder
import com.waterdelivery.Cart.CartCheckoutResponse
import com.waterdelivery.Checkout.Adapter.CheckoutAdapter
import com.waterdelivery.R
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_checkout.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CheckoutActivity : BaseActivity() {
    var paymenttype: Int = 0
    lateinit var paymentcode: String
    lateinit var totalcost: String
    lateinit var sharprf: shareprefrences
    lateinit var productlistget:ArrayList<CartCheckoutResponse.Data.Product>
    var jsonStr:String = ""
    var map = HashMap<String, String>()
    var delivered_address:String=""
    var latitude:String=""
    var longitude:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        checkout_back.setOnClickListener {
            onBackPressed()
        }

        sharprf = shareprefrences(this)
        getdetails()
        rbtnmoneyTowallet.setOnClickListener {
            if (paymentcode != "0.00") {
                paymenttype = 1
                rbtnmoneyTowallet.isChecked = true
                btncredit.isChecked = false
                cash.isChecked = false
            } else {
                rbtnmoneyTowallet.isChecked = false
            }


        }


        btn_checkout.setOnClickListener {
           /* val lm = getSystemService<Any>(Context.LOCATION_SERVICE) as LocationManager
            val location: Location? = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val longitude: Double = location!!.getLongitude()
            val latitude: Double = location!!.getLatitude()
*/

          //  checkGPSLocationStatus()

            val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
            }else{
                if (paymenttype != 0) {
                    chckeout()
                } else {
                    Toast.makeText(this, "Please Select Payment Mode", Toast.LENGTH_SHORT).show()
                }
            }

        }

        btncredit.setOnClickListener {
            paymenttype = 2
            rbtnmoneyTowallet.isChecked = false
            btncredit.isChecked = true
            cash.isChecked = false
        }
        cash.setOnClickListener {
            paymenttype = 3
            rbtnmoneyTowallet.isChecked = false
            btncredit.isChecked = false
            cash.isChecked = true
        }

        try {
            latitude=sharprf.getStringPreference("langitude").toString()
            longitude=sharprf.getStringPreference("longitude").toString()
            getCompleteAddressString(latitude.toDouble(),longitude.toDouble())

        }catch (e:Exception){

        }

    }
    fun checkGPSLocationStatus() {
        val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }


    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage("Your GPS seems to be disabled, Please enable it.")
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        val alert = builder.create()
        alert.show()
    }

    fun getdetails() {
        showProgressDialog()
        var updateprofile: Call<CartCheckoutResponse> =
            APIUtils.getServiceAPI()!!.DriverOrderTotal(
                "Bearer " + sharprf.getStringPreference("token"),
                sharprf.getStringPreference("customer_id")!!,
                sharprf.getStringPreference("selecetshopcutomer")!!
            )
        updateprofile.enqueue(object : Callback<CartCheckoutResponse> {
            override fun onResponse(
                call: Call<CartCheckoutResponse>,
                response: Response<CartCheckoutResponse>
            ) {
                hideProgressDialog()
                try {

                    if (response.code() == 200) {
                        if (response.body()!!.status == true) {
                            hideProgressDialog()
                            checkout_recycle.layoutManager =
                                LinearLayoutManager(this@CheckoutActivity)
                            productlistget=response.body()!!.data.product as ArrayList<CartCheckoutResponse.Data.Product>
                            checkout_recycle.adapter = CheckoutAdapter(
                                this@CheckoutActivity,
                                response.body()!!.data.product as ArrayList<CartCheckoutResponse.Data.Product>
                            )
                            tvSubtotal.text = "AED " + response.body()!!.data.amount
                            tvVat.text = "AED " + response.body()!!.data.vat
                            tvGrandTotal.text = "AED " + response.body()!!.data.grandtotal
                            totalcost = response.body()!!.data.grandtotal
                            paymentcode = response.body()!!.data.walletBalance


                            rbtnmoneyTowallet.text =
                                "Apply Money to Wallet (AED " + response.body()!!.data.walletBalance + ")"

                            if (response.body()!!.data.grandtotal.toFloat()>response.body()!!.data.walletBalance.toFloat()){
                                credit_linear.visibility=View.VISIBLE
                                //Toast.makeText(this@CheckoutActivity,"true",Toast.LENGTH_SHORT).show()
                            }else{
                               // Toast.makeText(this@CheckoutActivity,"false",Toast.LENGTH_SHORT).show()
                                credit_linear.visibility=View.GONE
                            }

                            getquantity()
                        } else {
                            hideProgressDialog()
                            showToastMessage(this@CheckoutActivity, response.body()!!.message)
                        }
                    } else if (response.code() == 401) {

                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@CheckoutActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                } catch (e: Exception) {
                }
            }

            override fun onFailure(call: Call<CartCheckoutResponse>, t: Throwable) {
                hideProgressDialog()
                Toast.makeText(this@CheckoutActivity, t.message, Toast.LENGTH_SHORT).show()


            }

        })

    }

    fun getquantity(){

        val jsonArray = JSONArray()
        val studentsObj = JSONObject()

        map = HashMap<String, String>()


        for (i in 0 until productlistget.size) {
            val product_id = JSONObject()
            try {

                map["quantity"+"["+i+"]"] = productlistget.get(i).cart_quantity
                map["product_id"+"["+i+"]"] = productlistget.get(i).product_id

              //  product_id.put("product_id", productlistget.get(i).product_id)
              //  product_id.put("cart_quantity", productlistget.get(i).cart_quantity)
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            jsonArray.put(product_id)

        }


        studentsObj.put("Products", jsonArray)


        jsonStr = studentsObj.toString()
    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                // Log.w("My Current loction address", strReturnedAddress.toString())
            } else {
                // Log.w("My Current loction address", "No Address returned!")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            // Log.w("My Current loction address", "Canont get Address!")
        }
        delivered_address=strAdd
       // Toast.makeText(this,LATITUDE.toString()+" "+LONGITUDE.toString()+" "+strAdd,Toast.LENGTH_SHORT).show()
        return strAdd
    }

    fun chckeout() {
        showProgressDialog()
        var token:String="Bearer " + sharprf.getStringPreference("token")
        var selecetshopcutomer:String=sharprf.getStringPreference("selecetshopcutomer").toString()
        var customer_id:String=sharprf.getStringPreference("customer_id").toString()
        var wallet:String=paymenttype.toString()
        var grandtotal:String=totalcost
        /*try {
            var latitude:String=sharprf.getStringPreference("langitude").toString()
            var longitude:String=sharprf.getStringPreference("longitude").toString()
        }catch (e:Exception){
            var latitude:String=""
            var longitude:String=""
        }*/

       // var latitude:String="28.87594785"
       // var longitude:String="78.348263874"
        map


        var updateprofile: Call<PlaceOrderResposne> =
            APIUtils.getServiceAPI()!!.postSubmitSurvey(
                token,
                selecetshopcutomer,
                customer_id,
                wallet,
                grandtotal,
                latitude,
                longitude,
                delivered_address,
                map,
            )
        updateprofile.enqueue(object : Callback<PlaceOrderResposne> {
            override fun onResponse(
                call: Call<PlaceOrderResposne>,
                response: Response<PlaceOrderResposne>
            ) {
                hideProgressDialog()
                try {

                    if (response.code() == 200) {
                        if (response.body()!!.status == true) {
                            hideProgressDialog()
                            showToastMessage(this@CheckoutActivity, response.body()!!.message)

                           /* var numbers: ArrayList<String> = ArrayList()
                            for (i in 0 until response.body()!!.data.result.product_list.size) {
                                numbers.add("ONE")

                            }
                            numbers.add("TWO")
                            intent.putExtra("product_list",numbers)
                            */
                            //val arraylist: ArrayList<PlaceOrderResposne.Data.Result.Productlist> = ArrayList<PlaceOrderResposne.Data.Result.Productlist>()
                           // intent.putParcelableArrayListExtra("product_list",arraylist)
                         //   val arraylist: response!!.body()!!.data as ArrayList<PlaceOrderResposne.Data.Result.Productlist> = ArrayList<PlaceOrderResposne.Data.Result.Productlist>()
                            data(response!!.body()!!.data.result.product_list as ArrayList<PlaceOrderResposne.Data.Result.Productlist>,response.body()!!.data.result)

                        } else {
                            hideProgressDialog()
                            showToastMessage(this@CheckoutActivity, response.body()!!.message)

                        }
                    } else if (response.code() == 401) {

                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@CheckoutActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                } catch (e: Exception) {
                    showToastMessage(this@CheckoutActivity, e.toString())
                }
            }

            fun data( exerciseprogramlist: ArrayList<PlaceOrderResposne.Data.Result.Productlist>,data:PlaceOrderResposne.Data.Result){



                val gson = GsonBuilder().create()
                val myCustomArray = gson.toJsonTree(exerciseprogramlist).asJsonArray
                val json: String = myCustomArray.toString()
                val myPiece = json.drop(1).dropLast(1)

                var buClickValue = myPiece.replace("[", "")
                var buClickValue1 = buClickValue.replace("]", "")
                var intent =
                    Intent(this@CheckoutActivity, OrderRecipetActivity::class.java)
                intent.putExtra("product_list",buClickValue1.toString())
                intent.putExtra("size",exerciseprogramlist.size.toString())
                intent.putExtra("data", data)
                //intent.putExtra("product_list_new", data)
                startActivity(intent)

            }

            override fun onFailure(call: Call<PlaceOrderResposne>, t: Throwable) {
                hideProgressDialog()
                Toast.makeText(this@CheckoutActivity, t.message, Toast.LENGTH_SHORT).show()

            }

        })

    }


}