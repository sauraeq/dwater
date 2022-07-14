package com.waterdelivery.api


import com.waterdelivery.Cart.Adapter.CountResponse
import com.waterdelivery.Cart.AddtoCartResponse
import com.waterdelivery.Cart.CartCheckoutResponse
import com.waterdelivery.Cart.CartListResponse
import com.waterdelivery.Checkout.PdfDoenloadResponce
import com.waterdelivery.Checkout.PlaceOrderResposne
import com.waterdelivery.ReturndataResponse
import com.waterdelivery.Shop.ProductListResponse
import com.waterdelivery.customer.fragement.model.AddmoneyResponse
import com.waterdelivery.customer.fragement.model.SearchResponse
import com.waterdelivery.customer.modal.AddCustomerResponse
import com.waterdelivery.customer.modal.WalletHistoryResponce
import com.waterdelivery.expense.modal.AddExpenseResponse
import com.waterdelivery.expense.modal.ExpenseTypeResponse
import com.waterdelivery.expense.modal.ExpensesListResponce
import com.waterdelivery.notification.NotificationResponse
import com.waterdelivery.orderdelivery.OrderDiliveryResponse
import com.waterdelivery.returndata.model.AddReturnResponse
import com.waterdelivery.returndata.model.ListResponse
import com.waterdelivery.store.model.OrderlistResponse
import com.waterdelivery.store.model.StoreAcceptResponse
import com.waterdelivery.store.model.StoreListResponse
import com.waterdelivery.task.adpater.TaskListUpdate
import com.waterdelivery.task.adpater.TaskUpdate
import com.waterdelivery.task.modal.TaskListResponse
import com.waterdelivery.user.modal.GetProfileResponse
import com.waterdelivery.user.modal.LoginResponse
import com.waterdelivery.user.modal.UpdateProfileResponse
import com.waterdelivery.user.modal.UserProfileUpdate
import com.waterdelivery.userfinal.ActivityRespons
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Body

import retrofit2.http.POST
import retrofit2.http.FieldMap

import retrofit2.http.FormUrlEncoded


interface APIConfiguration {

    @POST("Auth/login")
    @Headers("Content-Type: application/json")
    fun signin(
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<LoginResponse>


    @Multipart
    @POST("User_registration/profile")
    fun updateProfileUser(
        @Header("Authapikey") Authapikey: String,
        @Part("user_id") user_id: RequestBody,
        @Part profile: MultipartBody.Part?,
    ): Call<UpdateProfileResponse>


    @FormUrlEncoded
    @POST("Payment/DriverOrderTotal")
    fun DriverOrderTotal(
        @Header("Authapikey") Authapikey: String,
        @Field("driver_id") driver_id: String,
        @Field("customer_id") customer_id: String,
    ): Call<CartCheckoutResponse>

    @FormUrlEncoded
    @POST("Payment/DriverOrderTotal")
    fun DriverOrderTotal1(
        @Header("Authapikey") Authapikey: String,
        @Field("driver_id") driver_id: String,
        @Field("customer_id") customer_id: String,
    ): Call<PlaceOrderResposne>


    @FormUrlEncoded
    @POST("Payment/DriverOrdercheckout")
    fun postSubmitSurvey(
        @Header("Authapikey") Authapikey: String,
        @Field("customer_id") customer_id: String,
        @Field("driver_id") driver_id: String,
        @Field("wallet") wallet: String,
        @Field("grandtotal") grandtotal: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("delivered_address") delivered_address: String,
        @FieldMap hashFields: HashMap<String,String>
    ):  Call<PlaceOrderResposne>

    @FormUrlEncoded
    @POST("Pdf/orderpdf")
    fun PdfGenerate(
        @Header("Authapikey") Authapikey: String,
        @Field("order_id") customer_id: String
    ):  Call<PdfDoenloadResponce>


    @FormUrlEncoded
    @POST("Payment/DriverOrdercheckout")
    fun DriverOrdercheckout(
        @Header("Authapikey") Authapikey: String,
        @Field("customer_id") customer_id: String,
        @Field("driver_id") driver_id: String,
        @Field("wallet") wallet: String,
        @Field("grandtotal") grandtotal: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
    ): Call<PlaceOrderResposne>

    @Multipart
    @POST("Expenses/add")
    fun addexpensive(
        @Header("Authapikey") Authapikey: String,
        @Part("driver_id") driver_id: RequestBody,
        @Part("type") type: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("comment") comment: RequestBody,
        @Part profile: MultipartBody.Part?,
    ): Call<AddExpenseResponse>


    @GET("User_registration/getUser/{id}")
    @Headers("Content-Type: application/json")
    fun userdetails(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<GetProfileResponse>


    @GET("Driver/activityList/{id}")
    @Headers("Content-Type: application/json")
    fun activity(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<ActivityRespons>

    @GET("Cart/driverCartTotalCount/{id}")
    @Headers("Content-Type: application/json")
    fun count(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<CountResponse>

    @GET("Common/walletBalance/{id}")
    @Headers("Content-Type: application/json")
    fun wallet(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<WalletHistoryResponce>


    @GET("Driver/getNotification/{id}/2")
    @Headers("Content-Type: application/json")
    fun getNotification(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<NotificationResponse>

    @GET("Expenses/expensesTypeList")
    @Headers("Content-Type: application/json")
    fun expensesTypeList(
        @Header("Authapikey") Authapikey: String,
    ): Call<ExpenseTypeResponse>


    @PUT("User_registration/userUpdate/{id}")
    @Headers("Content-Type: application/json")
    fun updateuser(
        @Header("Authapikey") Authapikey: String,
        @Body stringStringHashMap: HashMap<String, String>,
        @Path("id") id: String,
    ): Call<UserProfileUpdate>


    @GET("Driver/orderList/{id}")
    @Headers("Content-Type: application/json")
    fun orderlist(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<OrderlistResponse>


    @FormUrlEncoded
    @POST("customer/addCustomer")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun addcusmtomer(
        @Header("Authapikey") Authapikey: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("driver_id") driver_id: String,
    ): Call<AddCustomerResponse>

    @FormUrlEncoded
    @POST("customer/searchCustomer")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun search(
        @Header("Authapikey") Authapikey: String,
        @Field("input") email: String,
    ): Call<SearchResponse>


    @FormUrlEncoded
    @POST("Cart/addToDriverCart")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun addtocard(
        @Header("Authapikey") Authapikey: String,
        @Field("driver_id") driver_id: String,
        @Field("product_id") product_id: String,
        @Field("price") price: String,
        @Field("quantity") quantity: String,
        @Field("event") event: String,
    ): Call<AddtoCartResponse>

    @GET("Driver/taskList/{id}/{date}")
    @Headers("Content-Type: application/json")
    fun tasklist(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
         @Path("date") date: String,

    ): Call<TaskListResponse>

    @GET("Cart/DrivercartList/{id}/1")
    @Headers("Content-Type: application/json")
    fun cartlist(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<CartListResponse>


    @GET("Product/productList2/0/1/{id}")
    @Headers("Content-Type: application/json")
    fun productlist(
        @Header("Authapikey") Authapikey: String,@Path("id") id: String,
    ): Call<ProductListResponse>


    @POST("Driver/deliveryAccept")
    @Headers("Content-Type: application/json")
    fun deliveryAccept(
        @Header("Authapikey") Authapikey: String,
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<StoreAcceptResponse>


    @FormUrlEncoded
    @POST("customer/addMoney")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun addMoney(
        @Header("Authapikey") Authapikey: String,
        @Field("amount") amount: String,
        @Field("customer_id") customer_id: String,
        @Field("driver_id") driver_id: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
    ): Call<AddmoneyResponse>

   /* @FormUrlEncoded
    @POST("Driver/orderTaskListUpdate")
    @Headers("Content-Type: application/json")
    fun orderTaskListUpdate(
        @Header("Authapikey") Authapikey: String,
        @FieldMap stringStringHashMap: HashMap<String, String>,
    ): Call<TaskUpdate>*/

    @FormUrlEncoded
    @POST("Driver/orderTaskListUpdate")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun orderTaskListUpdate(
        @Header("Authapikey") Authapikey: String,
        @Field("order_id") amount: String,
        @Field("driver_id") driver_id: String,
        @Field("order_status") customer_id: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("driver_shop") driver_shop: String
    ): Call<TaskListUpdate>

    @POST("Driver/storeAccept")
    @Headers("Content-Type: application/json")
    fun storeaccept(
        @Header("Authapikey") Authapikey: String,
        @Body stringStringHashMap: HashMap<String, String>,
    ): Call<StoreAcceptResponse>

    @FormUrlEncoded
    @POST("Driver/driverStoreAccept")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun driverStoreAccept(
        @Header("Authapikey") Authapikey: String,
        @Field("driver_id") driver_id: String,
        @Field("store_id") store_id: String,
        @Field("driver_acceptance") driver_acceptance: String,
        @Field("comments") comments: String,
    ): Call<StoreAcceptResponse>


    @FormUrlEncoded
    @POST("Driver/addReturn")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun addReturn(
        @Header("Authapikey") Authapikey: String,
        @Field("driver_id") driver_id: String,
        @Field("customer_id") customer_id: String,
        @Field("product_type") product_type: String,
        @Field("return_type") return_type: String,
        @Field("narration") narration: String,
        @Field("quantity") quantity: String,
        @Field("remark") remark: String,
        @Field("serial_number") serial_number: String,
    ): Call<AddReturnResponse>

    @FormUrlEncoded
    @POST("Driver/bottleOrderListUpdate")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun bottleOrderListUpdate(
        @Header("Authapikey") Authapikey: String,
        @Field("driver_id") store_id: String,
        @Field("store_id") driver_id: String,
        @Field("return_can") driver_acceptance: String,
        @Field("broken_can") comments: String,
        @Field("stored_bottle") stored_bottle: String,
        @Field("reamining_bottle") reamining_bottle: String,
    ): Call<OrderDiliveryResponse>


    @FormUrlEncoded
    @POST("Driver/driverLocationUpdate")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun driverLocationUpdate(
        @Header("Authapikey") Authapikey: String,
        @Field("driver_id") store_id: String,
        @Field("latitude") driver_id: String,
        @Field("longitude") driver_acceptance: String,
    ): Call<OrderDiliveryResponse>

    @GET("customer/getUserWalletInfo/{id}")
    @Headers("Content-Type: application/json")
    fun getUserWalletInfo(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<WalletHistoryResponce>


    @GET("driver/returnList/{id}")
    @Headers("Content-Type: application/json")
    fun returnList(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<ReturndataResponse>


    @GET("driver/storeList/{id}")
    @Headers("Content-Type: application/json")
    fun storeList(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<StoreListResponse>

    @GET("driver/getProductType")
    @Headers("Content-Type: application/json")
    fun getProductType(
        @Header("Authapikey") Authapikey: String,
    ): Call<ListResponse>

    @GET("driver/getReturnType")
    @Headers("Content-Type: application/json")
    fun getReturnType(
        @Header("Authapikey") Authapikey: String,
    ): Call<ListResponse>

    @GET("driver/getNarrationType")
    @Headers("Content-Type: application/json")
    fun getNarrationType(
        @Header("Authapikey") Authapikey: String,
    ): Call<ListResponse>

    @GET("Expenses/expensesList/{id}")
    @Headers("Content-Type: application/json")
    fun expensesList(
        @Header("Authapikey") Authapikey: String, @Path("id") id: String,
    ): Call<ExpensesListResponce>

    /*  @POST("Expenses/add")
      @Headers("Content-Type: application/json")
      fun expense(
          @Header("Authapikey")  Authapikey:String,  @Body Expense: ExpenseRequest
      ): Call<ExpensesResponse>

      @FormUrlEncoded
      @POST("Driver/orderDeliveryUpdate")
      @Headers("Content-Type:application/x-www-form-urlencoded")
      fun orderdiliberyupdate(
          @Header("Authapikey")  Authapikey:String,
         @Field("customer_id") customer_id:String,
         @Field("order_id") order_id:String,
         @Field("return_can") return_can:String,
         @Field("broken_can") broken_can:String,
         @Field("stored_bottel") stored_bottel:String,
         @Field("remaining_bottled") remaining_bottled:String,
      ): Call<StoreAcceptResponse>

      @GET("Expenses/expensesList/{id}")
      @Headers("Content-Type: application/json")
      fun getexpensive(
          @Header("Authapikey")  Authapikey:String,   @Path("id") id: String
      ): Call<Expenselistmodel>

      @GET("Driver/viewMap/{id}")
      @Headers("Content-Type: application/json")
      fun mapview(
          @Header("Authapikey")  Authapikey:String,   @Path("id") id: String
      ): Call<MapViewResponse>

      @GET("customer/getUserWalletInfo/{id}")
      @Headers("Content-Type: application/json")
      fun getUserWalletInfo(
          @Header("Authapikey")  Authapikey:String,
          @Path("id") id: String
      ): Call<HistoryWalletResponse>*/
}