package com.carlosvaldes.viajerum.views.auth.fragments

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.carlosvaldes.viajerum.MainActivity
import com.carlosvaldes.viajerum.R
import com.carlosvaldes.viajerum.databinding.FragmentLoginBinding
import com.carlosvaldes.viajerum.helpers.AuthHelpers
import com.carlosvaldes.viajerum.helpers.Helpers
import com.carlosvaldes.viajerum.models.AuthModel
import com.carlosvaldes.viajerum.models.GenericResponse
import com.carlosvaldes.viajerum.network.RetrofitService
import com.carlosvaldes.viajerum.network.ViajerumApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        Helpers.setupUI(binding.root, activity as Activity)

        binding.btLogin.setOnClickListener {
            onClickLogin()
        }
    }

    fun onClickLogin() {
        binding.tvError.text = ""
        binding.pbLoader.visibility = View.VISIBLE
        binding.btLogin.visibility = View.INVISIBLE


        var email = binding.tietEmail.text.toString().trim()
        var password = binding.tietPassword.text.toString().trim()
        if ( !validateFields(email, password) ) {
            binding.pbLoader.visibility = View.GONE
            binding.btLogin.visibility = View.VISIBLE

            return
        }

        CoroutineScope(Dispatchers.IO).launch {

            //Llamamos al servicio de retrofit
            val call = RetrofitService.getRetrofit().create(ViajerumApi::class.java)
                .login(email, password)

            call.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {

                    if ( response.body()!!.code == 200 ) {
                        AuthHelpers.storeToken(response.body()!!.token!!, activity)

                        activity?.let{
                            val intent = Intent (it, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            it.startActivity(intent)
                            it.finish()

                        }
                    } else {
                        binding.tvError.text = response.body()!!.message
                    }

                    binding.pbLoader.visibility = View.GONE
                    binding.btLogin.visibility = View.VISIBLE


                    //Log.d("RESPUESTA", "Respuesta del servidor: ${response.toString()}")
                    //Log.d("RESPUESTA", "Datos: ${response.body().toString()}")
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    binding.pbLoader.visibility = View.GONE
                    binding.btLogin.visibility = View.VISIBLE


                    Toast.makeText(
                        context,
                        "No hay conexión. Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
        }
    }



    fun validateFields(email : String, password : String) : Boolean {
        if ( email.length <= 0 ) {
            binding.tvError.text = "Verifica tu email"
            return false
        }

        if ( password.length <= 6 ) {
            binding.tvError.text = "El password debe ser mayor a 6 caracteres"
            return false
        }

        return true
    }
}