package com.harry.hungry.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import javax.security.auth.login.LoginException

class EnterOtpActivity : AppCompatActivity() {

    lateinit var txtEnterOtpBelow : TextView
    lateinit var etOTP : EditText
    lateinit var etNewPassword : EditText
    lateinit var etConfirmNewPassword : EditText
    lateinit var btnSubmit : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_otp)
        txtEnterOtpBelow = findViewById(R.id.txtEnterOtpBelow)
        etOTP = findViewById(R.id.etOTP)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnSubmit)
        var first_try : Boolean = false
        var mobile_number : String = ""
        if(intent != null){
            first_try = intent.getBooleanExtra("first_try", false)
            mobile_number = intent.getStringExtra("mobile_number")
        }
        if(first_try){
            Toast.makeText(this@EnterOtpActivity, "OTP has been to Email", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this@EnterOtpActivity, "Enter OTP recieved in previous 24 hours", Toast.LENGTH_SHORT).show()
        }
        btnSubmit.setOnClickListener {
            if(etOTP.text.toString().length != 4){
                Toast.makeText(this@EnterOtpActivity, "OTP must be of 4 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(etNewPassword.text.toString().length != 4){
                Toast.makeText(this@EnterOtpActivity, "Password should be at least 4 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!(etNewPassword.text.toString().equals(etConfirmNewPassword.text.toString()))){
                Toast.makeText(this@EnterOtpActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val queue = Volley.newRequestQueue(this@EnterOtpActivity)
            val url = "http://13.235.250.119/v2/reset_password/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", mobile_number)
            jsonParams.put("password", etNewPassword.text.toString())
            jsonParams.put("otp", etOTP.text.toString())
            if(ConnectionManager().checkConnectivity(this@EnterOtpActivity)){
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    Response.Listener {
                        try{
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                val message = data.getString("successMessage")
                                Toast.makeText(this@EnterOtpActivity, message, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@EnterOtpActivity, LoginActivity::class.java))
                                finish()
                            }
                            else{
                                Toast.makeText(this@EnterOtpActivity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                            }
                        }catch (e : JSONException){
                            Toast.makeText(this@EnterOtpActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                        }
                    },Response.ErrorListener {
                        Toast.makeText(this@EnterOtpActivity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
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
                val dialog = AlertDialog.Builder(this@EnterOtpActivity)
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
                    ActivityCompat.finishAffinity(this@EnterOtpActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }
}
