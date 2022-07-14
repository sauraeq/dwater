package com.waterdelivery.qrcode

import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.google.zxing.WriterException
import com.waterdelivery.R
import kotlinx.android.synthetic.main.activity_qr_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*

class QrProfileActivity : AppCompatActivity(),cont {
    var profileimage:String=""
    var qrgEncoder: QRGEncoder? = null
    lateinit var shpr:shareprefrences
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_profile)
        shpr= shareprefrences(this)
        if(intent.extras!=null){
            profileimage=intent.getStringExtra("profileimage").toString()
        }
        Glide.with(this!!).load(profileimage)
            .into(imageView7)
        genartecode()
        driver_name.text=shpr.getStringPreference(FIRST_NAME)
        driver_id.text=shpr.getStringPreference(USER_ID)
        racipt_back.setOnClickListener {
            onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun genartecode(){
        val manager = getSystemService(WINDOW_SERVICE) as WindowManager

        val display: Display = manager.defaultDisplay

        // creating a variable for point which
        // is to be displayed in QR Code.

        // creating a variable for point which
        // is to be displayed in QR Code.
        val point = Point()
        display.getSize(point)

        // getting width and
        // height of a point

        // getting width and
        // height of a point
        val width: Int = point.x
        val height: Int = point.y

        // generating dimension from width and height.

        // generating dimension from width and height.
        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = QRGEncoder(shpr.getStringPreference(USER_ID), null, QRGContents.Type.TEXT, dimen)
        try {
            // getting our qrcode in the form of bitmap.
            var   bitmap = qrgEncoder!!.encodeAsBitmap()
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            idIVQrcode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString())
        }
    }
}