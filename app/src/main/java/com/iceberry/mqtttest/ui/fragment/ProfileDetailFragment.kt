package com.iceberry.mqtttest.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMUserInfo
import com.iceberry.mqtttest.databinding.ProfileDetailFragmentBinding
import com.iceberry.mqtttest.viewModel.SharedViewModel
import com.iceberry.mqtttest.viewModel.fragment.ProfileDetailViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProfileDetailFragment : Fragment() {
    private var _binding: ProfileDetailFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ProfileDetailFragment()
    }

    private val viewModel: ProfileDetailViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().popBackStack()
        }
        sharedViewModel.curLoginUserName.observe(viewLifecycleOwner, {
            Log.d("mqttASH", it)
        })
        binding.CollapsingToolbarLayout.title = sharedViewModel.curLoginUserName.value
        binding.toolbar.title = sharedViewModel.curLoginUserName.value
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(
            arrayOf(sharedViewModel.curLoginUserName.value),
            arrayOf(EMUserInfo.EMUserInfoType.AVATAR_URL),
            object : EMValueCallBack<Map<String, EMUserInfo>> {
                override fun onSuccess(value: Map<String, EMUserInfo>?) {
                    MainScope().launch {
                    }
                }

                override fun onError(error: Int, errorMsg: String?) {
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(),
                            "失败，code:$error errorMsg:$errorMsg",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

}