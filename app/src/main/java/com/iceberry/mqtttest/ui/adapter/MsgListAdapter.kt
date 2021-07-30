package com.iceberry.mqtttest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iceberry.mqtttest.R
import com.iceberry.mqtttest.repository.bean.MsgListItem
import de.hdodenhof.circleimageview.CircleImageView

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/24
 * desc:
 *
 */
class MsgListAdapter(private val onClick: (MsgListItem) -> Unit) :
    ListAdapter<MsgListItem, MsgListAdapter.ViewHolder>(MsgListItemDiffCallback) {
    class ViewHolder(itemView: View, val onClick: (MsgListItem) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val newestMsg: TextView = itemView.findViewById(R.id.newestMsg)
        private val newestMsgTime: TextView = itemView.findViewById(R.id.newestMsgTime)
        private val unReadMsgCount: TextView = itemView.findViewById(R.id.unReadMsgCount)
        private val profileImage: CircleImageView = itemView.findViewById(R.id.profile_image)

        private var currentMsgListItem: MsgListItem? = null

        init {
            itemView.setOnClickListener {
                currentMsgListItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(msgListItem: MsgListItem) {
            currentMsgListItem = msgListItem
            //profileImage.set
            Glide.with(profileImage.context)
                .load(msgListItem.profileImageUrl)
                .placeholder(R.drawable.ic_default_profile)
                .into(profileImage)
            name.text = msgListItem.name
            newestMsg.text = msgListItem.newestMsg
            newestMsgTime.text = msgListItem.newestMsgTime
            if (msgListItem.unReadMsgCount > 0) {
                unReadMsgCount.text = msgListItem.unReadMsgCount.toString()
            } else {
                unReadMsgCount.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.msg_list_item, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msgListItem = getItem(position)
        holder.bind(msgListItem)
    }


}

object MsgListItemDiffCallback : DiffUtil.ItemCallback<MsgListItem>() {
    override fun areItemsTheSame(oldItem: MsgListItem, newItem: MsgListItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MsgListItem, newItem: MsgListItem): Boolean {
        return oldItem.id == newItem.id
    }

}