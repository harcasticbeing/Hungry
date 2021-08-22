package com.harry.hungry.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import android.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harry.hungry.R
import com.harry.hungry.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var imgLogo : ImageView
    lateinit var txtDetail : TextView
    lateinit var etMobile : EditText
    lateinit var etEmail : EditText
    lateinit var btnNext : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        imgLogo = findViewById(R.id.imgLogo)
        txtDetail = findViewById(R.id.txtDetail)
        etMobile = findViewById(R.id.etMobile)
        etEmail = findViewById(R.id.etEmail)
        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener{
            if(etMobile.text.toString().length != 10){
                Toast.makeText(this@ForgotPasswordActivity, "Mobile Number must be of 10 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(etEmail.text.toString().length == 0){
                Toast.makeText(this@ForgotPasswordActivity, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobile.text.toString())
            jsonParams.put("email", etEmail.text.toString())
            if(ConnectionManager().checkConnectivity(this@ForgotPasswordActivity)){
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    Response.Listener {
                        try{
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                val intent = Intent(this@ForgotPasswordActivity, EnterOtpActivity::class.java)
                                intent.putExtra("first_try", data.getBoolean("first_try"))
                                intent.putExtra("mobile_number", etMobile.text.toString())
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(this@ForgotPasswordActivity, "Email/Mobile not registered", Toast.LENGTH_SHORT).show()
                            }
                        }catch (e : JSONException){
                            Toast.makeText(this@ForgotPasswordActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                        }
                    },Response.ErrorListener {
                        Toast.makeText(this@ForgotPasswordActivity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "77c7528c300d27"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }
            else{
                val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Settings"){
                        text, listener->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit"){
                        text, listener->
                    ActivityCompat.finishAffinity(this@ForgotPasswordActivity)
                }
                dialog.create()
                dialog.show()
            }


        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}
