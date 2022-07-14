package com.geelong.user.Activity


import android.app.Dialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.waterdelivery.R
import kotlinx.android.synthetic.main.activity_web_view_pdf.*


class Payment_method : AppCompatActivity() {

    var userid:String=""
    var booking_id:String="5"
    var payment_method:String="PayPal"
    lateinit var webview_paymentt:WebView
    lateinit var customprogress:Dialog
    private var requestQueue: RequestQueue? = null
    lateinit var dialog : Dialog
    lateinit var sharedPreferences: SharedPreferences

    private var webView: WebView? = null
    private var textView: TextView? = null
    private var btnLoad: Button? = null

    var pdfFileName: String? = null

    // url of our PDF file.
    var pdfurl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_pdf)
        supportActionBar?.hide()
       // customprogress= Dialog(this)
       // customprogress.setContentView(R.layout.loader_layout)
        requestQueue = Volley.newRequestQueue(this)
        dialog = Dialog(this)
        sharedPreferences = getSharedPreferences(
            "MiiwaySharedPreference",
            Context.MODE_PRIVATE
        )

        // booking_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
           // .Booking_id,"").toString()

        webview_paymentt=findViewById(R.id.webview_payement)
        webview_paymentt.getSettings().setJavaScriptEnabled(true)
        webview_paymentt.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        val url =  intent.getStringExtra("url").toString()
       // RetrivePDFfromUrl().execute(url)

;

        startWebView(url+"pdf")


      /*  textView = findViewById(R.id.textView);
        btnLoad = findViewById(R.id.btnLoad);
        webView = findViewById(R.id.webView);
        webView!!.webViewClient = WebViewClient()
        webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, weburl: String) {
                Toast.makeText(this@Payment_method, "Your WebView is Loaded....",
                    Toast.LENGTH_LONG).show()
            }
        }
        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                textView!!.text = "Page loading : $newProgress%"
                if (newProgress == 100) {
                    textView!!.text = "Page Loaded."
                }
            }
        }
        webView!!.loadUrl(url+".pdf")*/

    }

    private fun pdfOpen(fileUrl: String) {
        webview_paymentt.getSettings().setJavaScriptEnabled(true)
        webview_paymentt.getSettings().setPluginState(WebSettings.PluginState.ON)

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        webview_paymentt.setWebViewClient(Callback())
        webview_paymentt.loadUrl(
            "http://docs.google.com/gview?embedded=true&url=$fileUrl"
        )
    }

    private class Callback : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView, url: String
        ): Boolean {
            return false
        }
    }

    private fun startWebView(url: String) {
        webview_paymentt.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }


            override fun onLoadResource(view: WebView, url: String) {
               /* Toast.makeText(this@Payment_method,"payment under process",Toast.LENGTH_LONG).show()*/

            }
            override fun onPageFinished(view: WebView, url: String) {
                try {

                    Log.d("sasa",url)


                } catch (exception: java.lang.Exception) {
                    exception.printStackTrace()
                }
            }
        })


        webview_paymentt.loadUrl(url)
    }



}