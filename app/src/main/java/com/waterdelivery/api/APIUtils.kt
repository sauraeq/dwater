package com.example.marvelapp.api

import com.waterdelivery.api.APIClient
import com.waterdelivery.api.APIConfiguration
import com.waterdelivery.api.APIConfiguration1

class  APIUtils {

   companion object{

       private val BASE_URL = "http://demo.equalinfotech.com/Waterdelivery/api/"

       fun getServiceAPI(): APIConfiguration? {
           return  APIClient.getApiClient(BASE_URL)!!.create(APIConfiguration::class.java)


       }

   }

}