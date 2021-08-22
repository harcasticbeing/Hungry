package com.harry.hungry.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
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

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmail : EditText
    lateinit var etMobile : EditText
    lateinit var etAddress : EditText
    lateinit var etPassword : EditText
    lateinit var etConfirmPassword : EditText
    lateinit var btnRegister : Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)
        etAddress = findViewById(R.id.etAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener{
            if(etName.text.toString().length == 0){
                Toast.makeText(this@RegisterActivity, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(etEmail.text.toString().length == 0){
                Toast.makeText(this@RegisterActivity, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(etMobile.text.toString().length != 10){
                Toast.makeText(this@RegisterActivity, "Mobile Number must be of 10 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(etAddress.text.toString().length == 0){
                Toast.makeText(this@RegisterActivity, "Address cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(etPassword.text.toString().length < 4){
                Toast.makeText(this@RegisterActivity, "Password should be at least 4 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!(etPassword.text.toString().equals(etConfirmPassword.text.toString()))){
                Toast.makeText(this@RegisterActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val queue = Volley.newRequestQueue(this@RegisterActivity)
            val url = "http://13.235.250.119/v2/register/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("name", etName.text.toString())
            jsonParams.put("mobile_number", etMobile.text.toString())
            jsonParams.put("password", etPassword.text.toString())
            jsonParams.put("address", etAddress.text.toString())
            jsonParams.put("email", etEmail.text.toString())
            if(ConnectionManager().checkConnectivity(this@RegisterActivity)){
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
                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            }
                            else{
                                Toast.makeText(this@RegisterActivity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                            }
                        }catch (e : JSONException){
                            Toast.makeText(this@RegisterActivity, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(this@RegisterActivity, "Volley Error Occurred", Toast.LENGTH_SHORT).show()
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
            else {
                val dialog = AlertDialog.Builder(this@RegisterActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@RegisterActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}
