package com.iceberry.mqtttest.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.iceberry.mqtttest.databinding.LoginFragmentBinding
import com.iceberry.mqtttest.viewModel.SharedViewModel
import com.iceberry.mqtttest.viewModel.fragment.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.loginButton.setOnClickListener {
            val account = binding.accountInputEditText.text.toString().trim()
            val password = binding.passwordInputEditText.text.toString().trim()
            if (account != "" && password != "") {
                viewModel.accountText.value = account
                viewModel.passwordText.value = password
                viewModel.isLoading.value = true
                viewModel.tryToLogin(account, password)

            } else {
                if (account == "") {
                    binding.accountInputLayout.error = "用户名不能为空"
                }
                if (password == "") {
                    binding.passwordInputLayout.error = "密码不能为空"
                }
            }

        }

        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.loginButton.text = ""
            } else {
                binding.progressBar.visibility = View.GONE
                binding.loginButton.text = "登录"
            }

        })
        viewModel.isLogin.observe(viewLifecycleOwner, {
            if (it) {
                sharedViewModel.isLogin.value = true
                sharedViewModel.curLoginUserName.value = viewModel.accountText.value
                Log.d("mqttA", "登陆成功")
            }
        })

        viewModel.errorMsg.observe(viewLifecycleOwner, {
            if (it != "")
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

}