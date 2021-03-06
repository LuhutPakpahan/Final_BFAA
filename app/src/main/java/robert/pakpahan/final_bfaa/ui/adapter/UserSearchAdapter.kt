package robert.pakpahan.final_bfaa.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.view.*
import robert.pakpahan.final_bfaa.R
import robert.pakpahan.final_bfaa.respond.response.SearchResponse

class UserSearchAdapter (private val context: Context, private val listener: Listener) :
    RecyclerView.Adapter<UserSearchAdapter.ViewHolder> () {

    private var listUser = emptyList<SearchResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_user,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listUser[position])
        holder.itemView.setOnClickListener {
            listener.onUserClickListenre(it, listUser[position])
        }
    }

    class ViewHolder (override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(item: SearchResponse) {
            containerView.tvUsernameItemUser.text = item.login
            containerView.tvUserTypeItemUser.text = item.type
            Glide.with(containerView.context)
                .load(item.avatarUrl)
                .circleCrop()
                .into(containerView.ivItemUser)
        }
    }

    internal fun setUserSearchData(listUser : List<SearchResponse>){
        this.listUser = listUser
        notifyDataSetChanged()
    }

    interface Listener {
        fun onUserClickListenre(view: View, data: SearchResponse)
    }
}