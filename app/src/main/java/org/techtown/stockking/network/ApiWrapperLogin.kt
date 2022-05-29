package org.techtown.stockking.network

import android.content.Context
import android.content.Intent
import android.util.Log
import org.techtown.stockking.model.FirstLoginModel
import org.techtown.stockking.model.UserModel
import org.techtown.stockking.module.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiWrapperLogin {
    companion object {
        private val TAG = this.javaClass.simpleName
        fun postFirstLogin(
            socialName: String,
            userData: FirstLoginModel,
            callback: (UserModel?) -> Unit
        ) {
            val modelCall = NetWorkService.api2.requestFirstLogin(socialName, userData)
            modelCall.enqueue(object : Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>, response: Response<UserModel>
                ) {
                    Log.i(
                        TAG,
                        "post first login success\n response.body : " + response.body().toString()
                    )
                    val response = response.body()
                    response?.let {
                        callback.invoke(it)
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.i(TAG, "post first login fail : " + t.toString())
                    modelCall.cancel()
                }
            })
        }

        fun getAutoLogin(token: String, callback: (UserModel?) -> Unit) {
            val modelCall = NetWorkService.api2.requestAutoLogin(token)
            modelCall.enqueue(object : Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>, response: Response<UserModel>
                ) {
                    Log.i(TAG, "get auto login success\n response.body : " + response.body().toString())
                    if(response.isSuccessful){
                        callback(response.body())
                    }else{
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.i(TAG, "get auto login fail\n error msg:"+t.toString())
                    modelCall.cancel()
                }
            })
        }
    }
}