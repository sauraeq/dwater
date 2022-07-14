package com.waterdelivery.customer.fragement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.example.marvelapp.api.APIUtils
import com.waterdelivery.R
import com.waterdelivery.customer.adpater.WalletHostoryAdpater
import com.waterdelivery.customer.modal.WalletHistoryResponce
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.user.LoginActivity
import kotlinx.android.synthetic.main.fragment_wallet_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WalletHistoryFragment : Fragment(), cont {
    lateinit var dashBoardActivity: DashBoardActivity
    lateinit var sharprf: shareprefrences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet_history, container, false)
        Log.e("workibg", "pause")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharprf = shareprefrences(requireActivity())
        dashBoardActivity = context as DashBoardActivity
        recy_history.layoutManager = LinearLayoutManager(context)
        walletHistory()

    }


    fun walletHistory() {
        dashBoardActivity.showProgressDialog()
        var signin: Call<WalletHistoryResponce> = APIUtils.getServiceAPI()!!.getUserWalletInfo(
            "Bearer " + sharprf.getStringPreference(Token),
            sharprf.getStringPreference(USER_ID).toString()
        )
        signin.enqueue(object : Callback<WalletHistoryResponce> {
            override fun onResponse(
                call: Call<WalletHistoryResponce>,
                response: Response<WalletHistoryResponce>
            ) {
                try {
                    if (response.code() == 200) {
                        dashBoardActivity.hideProgressDialog()
                        if (response.body()!!.status == true) {
                            var obj = response.body()!!.data
                            txt_total_work.text = "AED " + response.body()!!.wallet_balance
                            recy_history.adapter = WalletHostoryAdpater(activity!!, obj)


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

            override fun onFailure(call: Call<WalletHistoryResponce>, t: Throwable) {
                dashBoardActivity.hideProgressDialog()

            }

        })
    }

    override fun onStart() {
        super.onStart()
        Log.e("working", "working")
        walletHistory()
    }

    override fun onResume() {
        super.onResume()
        walletHistory()
        Log.e("working", "resume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("workibg", "pause")
    }
}