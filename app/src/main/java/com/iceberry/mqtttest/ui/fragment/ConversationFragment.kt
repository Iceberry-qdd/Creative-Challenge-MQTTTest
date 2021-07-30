package com.iceberry.mqtttest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iceberry.mqtttest.databinding.ConversationFragmentBinding
import com.iceberry.mqtttest.repository.bean.Conversation
import com.iceberry.mqtttest.ui.adapter.ConversationAdapter
import com.iceberry.mqtttest.viewModel.SharedViewModel
import com.iceberry.mqtttest.viewModel.fragment.ConversationModelFactory
import com.iceberry.mqtttest.viewModel.fragment.ConversationViewModel

class ConversationFragment : Fragment() {
    private var _binding: ConversationFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ConversationViewModel> {
        ConversationModelFactory(
            requireContext()
        )
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var conversationAdapter: ConversationAdapter

    companion object {
        fun newInstance() = ConversationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = ConversationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        conversationAdapter = ConversationAdapter()
        val recyclerView = binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).also { it.stackFromEnd = true }
            adapter = conversationAdapter//.also { smoothScrollToPosition(1) }
            //smoothScrollToPosition(adapter.itemCount)
        }

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().popBackStack()
        }
        viewModel.succeedSend.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(requireContext(), "发送成功", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "发送失败", Toast.LENGTH_LONG).show()
            }
        })

        binding.msgInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND && v.text.toString().trim() != "") {
                val text = v.text.toString()
                viewModel.addConversation(
                    Conversation(
                        from = "Ben",
                        to = "Alice",
                        content = text,
                        dateTime = "18:15",
                        profileImgUrl = "https://api.multiavatar.com/1234565156216.png",
                        type = Conversation.SELF
                    )
                )
                viewModel.sendMsg(text, sharedViewModel.receiverName.value!!)
                v.text = ""
            } else {
                Toast.makeText(requireContext(), "消息为空", Toast.LENGTH_LONG).show()
            }
            return@setOnEditorActionListener false
        }

        viewModel.conversationLiveData.observe(viewLifecycleOwner, {
            it?.let {
                conversationAdapter.submitList(it as MutableList<Conversation>)
            }
        })

        sharedViewModel.receiverName.observe(viewLifecycleOwner, {
            binding.toolbar.title = it
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}