package com.iceberry.mqtttest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iceberry.mqtttest.R
import com.iceberry.mqtttest.repository.bean.Conversation
import de.hdodenhof.circleimageview.CircleImageView

/**
 * author: Iceberry
 * email:qddwork@outlook.com
 * date: 2021/7/25
 * desc:
 *
 */
class ConversationAdapter :
    ListAdapter<Conversation, ConversationAdapter.ViewHolder>(ConversationDiffCallback) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val conversationContent: TextView = itemView.findViewById(R.id.conversationContent)
        private val profileImage: CircleImageView = itemView.findViewById(R.id.profile_image)
        private val selfConversationContent: TextView =
            itemView.findViewById(R.id.selfConversationContent)
        private val selfProfileImage: CircleImageView =
            itemView.findViewById(R.id.profile_self_image)

        private val selfConversationView: ConstraintLayout =
            itemView.findViewById(R.id.selfConversationConstrainLayout)
        private val conversationView: ConstraintLayout =
            itemView.findViewById(R.id.conversationConstrainLayout)

        fun bind(conversation: Conversation) {

            when (conversation.type) {
                Conversation.OTHER -> {
                    conversationView.visibility = View.VISIBLE
                    selfConversationView.visibility = View.GONE
                    Glide.with(profileImage.context).load(conversation.profileImgUrl)
                        .into(profileImage)
                    conversationContent.text = conversation.content
                }
                Conversation.SELF -> {
                    selfConversationView.visibility = View.VISIBLE
                    conversationView.visibility = View.GONE
                    Glide.with(selfProfileImage.context).load(conversation.profileImgUrl)
                        .into(selfProfileImage)
                    selfConversationContent.text = conversation.content
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversation_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ConversationAdapter.ViewHolder, position: Int) {
        val conversation = getItem(position)
        holder.bind(conversation)
    }

}

object ConversationDiffCallback : DiffUtil.ItemCallback<Conversation>() {
    override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
        return oldItem.dateTime == newItem.dateTime
    }

}
