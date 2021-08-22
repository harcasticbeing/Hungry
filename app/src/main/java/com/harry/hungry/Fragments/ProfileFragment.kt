package com.harry.hungry.Fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.harry.hungry.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    lateinit var imgUser : ImageView
    lateinit var txtName : TextView
    lateinit var txtMobile : TextView
    lateinit var txtEmail : TextView
    lateinit var txtAddress : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        imgUser = view.findViewById(R.id.imgUser)
        txtMobile = view.findViewById(R.id.txtMobile)
        txtName = view.findViewById(R.id.txtName)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtAddress = view.findViewById(R.id.txtAddress)
        var sharedPreferences : SharedPreferences? = activity?.getSharedPreferences("Hungry Preferences", Context.MODE_PRIVATE)
        txtName.text = sharedPreferences?.getString("name", "Name of User")
        txtMobile.text = sharedPreferences?.getString("mobile_number", "Mobile Number")
        txtEmail.text = sharedPreferences?.getString("email", "Email")
        txtAddress.text = sharedPreferences?.getString("address", "Address")

        return view
    }


}
