package com.waterdelivery.task.fragement

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.rehabcare.user.datePickerHelper
import com.waterdelivery.R
import com.waterdelivery.main.DashBoardActivity
import com.waterdelivery.task.adpater.ViewpagerAdapter
import kotlinx.android.synthetic.main.fragment_task.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TaskFragment : Fragment() {
    lateinit var datePicker: datePickerHelper
    var date:String =""
    private var oneWayTripDate: Date? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initclick()
        datePicker = datePickerHelper(requireContext())

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = current.format(formatter)



        callfragment(TaskListFragment(),formatted.toString())
        date=formatted.toString()
        setbackgroundblue(listview)
        select_date.setOnClickListener {
            showDatePickerDialog()
        }

    }


    private fun initclick() {
        listview.setOnClickListener {
            callfragment(TaskListFragment(),date)
            setbackgroundblue(listview)
            setbackgroundnormal(mapview)
        }
        mapview.setOnClickListener {

            //startActivity(Intent(requireContext(), MapsActivity::class.java))
            callfragment(MapFragment(),date)
            setbackgroundblue(mapview)
            setbackgroundnormal(listview)
        }
    }


    fun callfragment(fragment: Fragment,date:String) {
        val adapter = ViewpagerAdapter(childFragmentManager)
        val ldf = fragment
        val args = Bundle()
        args.putString("date", date)
        ldf.setArguments(args)
       // requireFragmentManager().beginTransaction().replace(R.id.container, ldf).commit()
        adapter.addFragment(ldf, "Search")
        viewpager.adapter = adapter
    }

    fun setbackgroundblue(textView: TextView) {
        textView.setBackgroundResource(R.drawable.blueroundborder)
        textView.setTextColor(resources.getColor(R.color.white))
        textView.setPadding(0, 8, 0, 8)
    }

    fun setbackgroundnormal(textView: TextView) {
        textView.setBackgroundResource(R.drawable.blue_border)
        textView.setTextColor(resources.getColor(R.color.black))
        textView.setPadding(0, 8, 0, 8)
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

                date=(data)

                val date = data
                val input = SimpleDateFormat("yyyy-MM-dd")
                val output = SimpleDateFormat("MMMM dd, yyyy")
                oneWayTripDate = input.parse(date) // parse input


                text_select_date.setText(output.format(oneWayTripDate))
                callfragment(TaskListFragment(),data)


            }


        })
    }
}