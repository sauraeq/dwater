package com.waterdelivery.store

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.SelectUser.UserSelectActivity
import com.waterdelivery.Shop.ShopActivity
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.returndata.ReturnActivity
import com.waterdelivery.store.adpater.StoreAdpater
import com.waterdelivery.store.model.StoreListResponse
import com.waterdelivery.user.LoginActivity
import com.waterdelivery.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_storek.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StorekFragment : Fragment(),StockInterface {

    lateinit var sharprf: shareprefrences
    lateinit var mainActivity: DashBoardActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storek, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharprf = shareprefrences(requireContext())
        mainActivity = context as DashBoardActivity
        cardlist()
        return_btn.setOnClickListener {
            startActivity(Intent(requireActivity(),ReturnActivity::class.java))
        }
      /*  val gridLayoutManager = GridLayoutManager(context, 2)
        val spanCount = 1// 2 columns
        val spacing = 0 // 50px
        val includeEdge = true
        recy_store_list.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )*/
        shop_btn.setOnClickListener {
            startActivity(Intent(activity,UserSelectActivity::class.java))
        }
       // recy_store_list.layoutManager = gridLayoutManager
        recy_store_list.layoutManager = LinearLayoutManager(context)

    }

    fun cardlist() {
        mainActivity.showProgressDialog()
        var signin: Call<StoreListResponse> = APIUtils.getServiceAPI()!!.storeList(
            "Bearer " + sharprf.getStringPreference("token"),
            sharprf.getStringPreference("customer_id").toString()
        )
        signin.enqueue(object : Callback<StoreListResponse> {
            override fun onResponse(
                call: Call<StoreListResponse>,
                response: Response<StoreListResponse>
            ) {
                try {
                    if (response.code() == 200) {
                        mainActivity.hideProgressDialog()
                        if (response.body()!!.status == true) {

                            recy_store_list.adapter = StoreAdpater(
                                mainActivity,
                                response.body()!!.data as ArrayList<StoreListResponse.Data>,this@StorekFragment, error,
                                response!!.body()!!.driver_details
                            )
                            if (response.body()!!.data.size > 0) {
                                error.visibility = View.GONE
                            } else {
                                error.visibility = View.VISIBLE
                            }
                        } else {
                            mainActivity.hideProgressDialog()
                        }
                    } else if (response.code() == 401) {
                        mainActivity.hideProgressDialog()
                        sharprf.clearAllPreferences()
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finishAffinity()
                    }
                } catch (e: Exception) {
                    Log.e("erroeeee",e.toString())

                }

            }

            override fun onFailure(call: Call<StoreListResponse>, t: Throwable) {
                mainActivity.hideProgressDialog()
            }

        })
    }

    override fun onClick() {
        cardlist()
    }
}