package org.techtown.stockking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import org.techtown.stockking.common.MySharedPreferences
import org.techtown.stockking.databinding.ActivityMainBinding
import org.techtown.stockking.model.UserModel
import org.techtown.stockking.adapter.ViewPagerAdapter
import org.techtown.stockking.network.ApiWrapper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var keyHash = Utility.getKeyHash(this)
        Log.d("sss",keyHash)

        val method = MySharedPreferences.getMethod(this)
        val token = MySharedPreferences.getToken(this)

        if (method == "kakao") {
            val userInfo = UserModel(
                method = "kakao",
                token= MySharedPreferences.getToken(this)
            )

            Log.i("sss","userInfo : "+userInfo)

            ApiWrapper.postToken(userInfo){
                Log.i("sss","it!!!"+it.toString())
            }

            //response 받아오기 :

            // 사용자 정보 요청 (기본)
            if(token != null){
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.d("sss", "사용자 정보 요청 실패", error)
                        val intent= Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else if (user != null) {
                        Log.d("sss",user.toString())
                        Log.d(
                            "sss", "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                        )
                        MySharedPreferences.setUserName(this,user.kakaoAccount?.profile?.nickname.toString())
                    }
                }
            }
        }else if(method == "google"){
            //response 받아오기 :

            //일단 없으니까 내가 .. 가져오기
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if(acct != null) {
                MySharedPreferences.setUserName(this,acct?.givenName.toString())
            }
        }else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val viewpager=binding.viewpager
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_one -> {
                    viewpager.currentItem = 0
                }
                R.id.item_two -> {
                    viewpager.currentItem = 1
                }
                R.id.item_three -> {
                    viewpager.currentItem = 2
                }
                else -> {
                    false
                }
            }
            true
        }

        viewpager.adapter = ViewPagerAdapter(this)

    }
}