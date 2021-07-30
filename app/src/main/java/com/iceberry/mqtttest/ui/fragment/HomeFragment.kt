package com.iceberry.mqtttest.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.exceptions.HyphenateException
import com.iceberry.mqtttest.Config
import com.iceberry.mqtttest.R
import com.iceberry.mqtttest.databinding.HomeFragmentBinding
import com.iceberry.mqtttest.repository.bean.MsgListItem
import com.iceberry.mqtttest.ui.adapter.MsgListAdapter
import com.iceberry.mqtttest.viewModel.SharedViewModel
import com.iceberry.mqtttest.viewModel.fragment.HomeViewModel
import com.iceberry.mqtttest.viewModel.fragment.HomeViewModelFactory


class HomeFragment : Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> { HomeViewModelFactory(requireContext()) }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var msgListAdapter: MsgListAdapter
    private lateinit var recyclerView: RecyclerView

    private val msgListener: EMMessageListener = object : EMMessageListener {
        override fun onMessageReceived(messages: List<EMMessage>) {
            viewModel.upDateList()
        }

        override fun onCmdMessageReceived(messages: List<EMMessage>) {
            //收到透传消息
        }

        override fun onMessageRead(messages: List<EMMessage>) {
            //收到已读回执
        }

        override fun onMessageDelivered(message: List<EMMessage>) {
            //收到已送达回执
        }

        override fun onMessageRecalled(messages: List<EMMessage>) {
            //消息被撤回
        }

        override fun onMessageChanged(message: EMMessage, change: Any) {
            //消息状态变动
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.recyclerView)

        addMsgListener()
        msgListAdapter = MsgListAdapter { msgListItem -> adapterOnClick(msgListItem) }
        recyclerView = binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = msgListAdapter
        }
        viewModel.msgListLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty() || it == null) {
                return@observe
            }
            it.let {
                msgListAdapter.submitList(it as MutableList<MsgListItem>)
            }
        })
        refreshData()

        viewModel.isRefreshing.observe(viewLifecycleOwner, {
            binding.swiperefresh.isRefreshing = it
        })

        binding.swiperefresh.setOnRefreshListener {
            Log.d("SWIPER", "刷新。。。")
            refreshData()
        }

        binding.profileImage.setOnClickListener {
            //navController.navigate(R.id.action_homeFragment_to_profileDetailFragment)
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.addFriend -> {
                    addFriend()

                    true
                }

                R.id.logout -> {
                    viewModel.logout()
                    Toast.makeText(requireContext(), "已注销", Toast.LENGTH_LONG).show()
                    sharedViewModel.isLogin.value = false
                    android.os.Process.killProcess(android.os.Process.myPid())
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }

    private fun addFriend() {
        val editText = EditText(requireContext())
        AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
            .setTitle("添加好友")
            .setView(editText)
            .setPositiveButton("添加") { _, _ ->
                Log.d("mqttTE", editText.text.toString())
                if (editText.text.toString().trim() != "") {
                    try {
                        EMClient.getInstance().contactManager()
                            .addContact(editText.text.toString(), "")
                        Toast.makeText(requireContext(), "添加失败，服务升级中", Toast.LENGTH_SHORT).show()
                    } catch (e: HyphenateException) {
                        Toast.makeText(
                            requireContext(),
                            "${e.errorCode} ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }.setNegativeButton("取消") { _, _ -> }
            .show()
    }

    private fun addMsgListener() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener)
    }

    private fun refreshData() {
        viewModel.isRefreshing.value = true
        viewModel.upDateList()
        msgListAdapter.notifyDataSetChanged()
        viewModel.isRefreshing.value = false
    }

    private fun adapterOnClick(msgListItem: MsgListItem) {
        sharedViewModel.receiverName.value = msgListItem.name
        Config.receiverName = msgListItem.name

        val conversation = EMClient.getInstance().chatManager().getConversation(msgListItem.name)

        //指定会话消息未读数清零
        //指定会话消息未读数清零
        conversation.markAllMessagesAsRead()
        navController.navigate(R.id.action_homeFragment_to_conversationFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(msgListener)
        super.onDestroy()
    }
}