package com.waterdelivery.customer.fragement

import MyAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import com.waterdelivery.R
import com.waterdelivery.customer.fragement.ListCustomerFragment
import com.waterdelivery.customer.fragement.WalletHistoryFragment
import com.waterdelivery.notification.NotificationActivity
import kotlinx.android.synthetic.main.fragment_customer.*


class CustomerFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = MyAdapter(childFragmentManager)
        adapter.addFragment(ListCustomerFragment(), "List")
        adapter.addFragment(WalletHistoryFragment(), "Wallet History")
        viewPager_layout_home.adapter = adapter
        tab_layout.setupWithViewPager(viewPager_layout_home)

    }
}