package com.harry.hungry.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harry.hungry.R
import com.harry.hungry.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var etMobile: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogIn: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtSignUp: TextView
    lateinit var sharedPreferences: SharedPreferences
    val validmobile: String = "8949607562"
    val validpass = arrayOf("thanos", "tony", "steve", "bruce")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        setContentView(R.layout.activity_login)
        if(isLoggedIn){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
        title = "Log In"
        etMobile = findViewById(R.id.etMobile)
        etPassword = findViewById(R.id.etPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)
        btnLogIn.setOnClickListener{
            val mobileInput : String = etMobile.text.toString()
            if(mobileInput.length != 10){
                Toast.makeText(this@LoginActivity, "Mobile Number should be of 10 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passInput : String = etPassword.text.toString()
            if(passInput.length < 4){
                Toast.makeText(this@LoginActivity, "Password should be at least 4 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobile.text.toString())
            jsonParams.put("password", etPassword.text.toString())
            if(ConnectionManager().checkConnectivity(this@LoginActivity)){
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    Response.Listener {
                        try{
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                val data2 = data.getJSONObject("data")
                                sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
                                sharedPreferences.edit().putString("user_id", data2.getString("user_id")).apply()
                                sharedPreferences.edit().putString("name", data2.getString("name")).apply()
                                sharedPreferences.edit().putString("email", data2.getString("email")).apply()
                                sharedPreferences.edit().putString("mobile_number", data2.getString("mobile_number")).apply()
                                sharedPreferences.edit().putString("address", data2.getString("address")).apply()
                                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            }
                            else{
                                Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                            }
                        }catch (e : JSONException){
                            Toast.makeText(this@LoginActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(this@LoginActivity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
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
                val dialog = AlertDialog.Builder(this@LoginActivity)
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
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
        txtSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
    fun savePreferences(){
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }
}
