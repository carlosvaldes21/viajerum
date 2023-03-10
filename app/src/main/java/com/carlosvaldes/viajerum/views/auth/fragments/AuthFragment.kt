package com.carlosvaldes.viajerum.views.auth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.carlosvaldes.viajerum.R
import com.carlosvaldes.viajerum.databinding.FragmentAuthBinding
import com.carlosvaldes.viajerum.views.auth.AuthActivity


class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthBinding.bind(view)

        binding.buttonLogin.setOnClickListener {
            onClickLogin()
        }

        binding.buttonRegister.setOnClickListener {
            onClickRegister()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }


    private fun onClickLogin()
    {
        val action = AuthFragmentDirections.actionAuthFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun onClickRegister()
    {
        val action = AuthFragmentDirections.actionAuthFragmentToRegisterFragment()
        findNavController().navigate(action)
    }
}