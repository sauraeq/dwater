package com.waterdelivery.Checkout

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.geelong.user.Activity.Payment_method
import com.waterdelivery.Checkout.Adapter.Movie
import com.waterdelivery.Checkout.Adapter.MoviesAdapter
import com.waterdelivery.Checkout.Adapter.ProductDetailsAdapter
import com.waterdelivery.R
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_order_recipet.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderRecipetActivity : BaseActivity() {
    lateinit var list:PlaceOrderResposne.Data.Result
    lateinit var list1:PlaceOrderResposne.Data.Result.Productlist
    lateinit var sharprf: shareprefrences
    lateinit var productlistget:PlaceOrderResposne.Data.Result.Productlist
    private var mAdapter: MoviesAdapter? = null
    private val movieList: ArrayList<Movie> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_recipet)
        sharprf = shareprefrences(this)
        recipet_back.setOnClickListener {
            startActivity(Intent(this,DashBoardActivity::class.java))
            finishAffinity()


            }
        try {
            if(intent.extras!=null){
                list= intent.getSerializableExtra("data") as PlaceOrderResposne.Data.Result
               /* val bundle = intent.extras
                val product_list = bundle!!.getString("product_list")
                for (i in 0 until product_list!!.length) {
                    val `object` = JSONObject(product_list)
                    val syncresponse: String = `object`.getString("syncresponse")
                }*/
               // var data:String= (intent.getSerializableExtra("product_list") as PlaceOrderResposne.Data.Result.Productlist).toString()
               // productlistget = intent.getSerializableExtra("product_list") as PlaceOrderResposne.Data.Result.Productlist
            }

            rechange_ammount.text="AED  "+ list.TotalCartamount
            txt_txnid.text="AED  "+ list.amount_without_vat
            txt_customername.text="AED  "+ list.TotalCartamount
            text_customerid.text="Customer ID: " + list.customer_id
            txt_ammount.text="AED  " + list.vat

            txt_trn_no.setText("Customer TRN Number: "+list.customer_trn)//
           // txt_trn_no.setText("")//
            txt_cust_name.setText("Customer Name: "+list.customer_name)
            order_no.setText("Order Number: "+list.product_list[0].order_id)



            txt_prod_name.setText("")

        }catch (e:Exception){


        }

       // getdetails()

        open_pdf.setOnClickListener {


            val urlString = "http://demo.equalinfotech.com/Waterdelivery/api/pdf/print_pdf/"+list.product_list[0].order_id.toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.android.chrome")
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                // Chrome browser presumably not installed so allow user to choose instead
                intent.setPackage(null)
                startActivity(intent)
            }
           /* val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://demo.equalinfotech.com/Waterdelivery/api/pdf/print_pdf/"+list.product_list[0].order_id.toString()))
            startActivity(browserIntent)*/
           /* val intent= Intent(this@OrderRecipetActivity, Payment_method::class.java)
            intent.putExtra("url","http://demo.equalinfotech.com/Waterdelivery/api/pdf/print_pdf/"+list.product_list[0].order_id.toString())
            startActivity(intent)*/
          //  download_pdf()
        }


        prepareMovieData()


        open_print.setOnClickListener {
            val intent = Intent(this,Print::class.java)
            startActivity(intent)
           /* Toast.makeText(this,"Please Connect to Device",Toast.LENGTH_LONG).show()*/
        }
    }

    fun getdetails() {
        checkout_recycle.layoutManager =
            LinearLayoutManager(this@OrderRecipetActivity)
        checkout_recycle.adapter = ProductDetailsAdapter(
            this@OrderRecipetActivity,
            productlistget
        )

    }

    fun download_pdf() {
        showProgressDialog()
        var token:String="Bearer " + sharprf.getStringPreference("token")
        var order_id:String=list.product_list[0].order_id.toString()



        var updateprofile: Call<PdfDoenloadResponce> =
            APIUtils.getServiceAPI()!!.PdfGenerate(
                token,
                order_id,
            )
        updateprofile.enqueue(object : Callback<PdfDoenloadResponce> {
            override fun onResponse(
                call: Call<PdfDoenloadResponce>,
                response: Response<PdfDoenloadResponce>
            ) {
                hideProgressDialog()
                try {

                    if (response.code() == 200) {
                        if (response.body()!!.status == true) {
                            hideProgressDialog()
                            val intent= Intent(this@OrderRecipetActivity, Payment_method::class.java)
                            intent.putExtra("url",response.body()!!.data.print_url)
                            startActivity(intent)
                           // showToastMessage(this@CheckoutActivity, response.body()!!.message)

                        } else {
                            hideProgressDialog()
                           // showToastMessage(this@CheckoutActivity, response.body()!!.message)

                        }
                    } else if (response.code() == 401) {

                      //  sharprf.clearAllPreferences()

                    }
                } catch (e: Exception) {
                   // showToastMessage(this@CheckoutActivity, e.toString())
                }
            }


            override fun onFailure(call: Call<PdfDoenloadResponce>, t: Throwable) {
                hideProgressDialog()
              //  Toast.makeText(this@CheckoutActivity, t.message, Toast.LENGTH_SHORT).show()

            }

        })

    }

    private fun prepareMovieData() {

        val bundle = intent.extras
       // val product_list = "{"+"\"product\":\""+"["+ bundle!!.getString("product_list")+"]}"
        val product_list = bundle!!.getString("product_list")
        var new_data:String="["+product_list+"]"
        val jsonObjectBeneficiary = JSONArray(new_data)
        val size = bundle!!.getString("size")
       // val jArray3: JSONArray =  jsonObjectBeneficiary.getJSONArray("product")
        for (i in 0 until size!!.toInt()) {

            val object3: JSONObject = jsonObjectBeneficiary.getJSONObject(i)
            val weightofJsonObject: JSONObject = jsonObjectBeneficiary.getJSONObject(i)
            val product_name: String =weightofJsonObject.getString("product_name")
            val quantity: String = object3.getString("quantity")
            val amount: String = object3.getString("amount")


            var movie = Movie(product_name, quantity, amount)
            movieList.add(movie)
        }
       /* var movie = Movie("Mad Max: Fury Road", "Action & Adventure", "2015")
        movieList.add(movie)
        movie = Movie("Inside Out", "Animation, Kids & Family", "2015")
        movieList.add(movie)
        movie = Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2015")
        movieList.add(movie)
        movie = Movie("Shaun the Sheep", "Animation", "2015")
        movieList.add(movie)
*/
        mAdapter = MoviesAdapter(movieList)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        product.setLayoutManager(mLayoutManager)
        product.setItemAnimator(DefaultItemAnimator())
        product.setAdapter(mAdapter)
    }

}
