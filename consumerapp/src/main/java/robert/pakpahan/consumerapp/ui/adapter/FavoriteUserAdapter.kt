package robert.pakpahan.consumerapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.view.*
import robert.pakpahan.consumerapp.R
import robert.pakpahan.consumerapp.data.model.FavoriteModel

class FavoriteUserAdapter (private val context: Context, private val listener: Listener) :
    RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder> () {

    private var listUser = emptyList<FavoriteModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false))

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listUser[position])
        holder.itemView.setOnClickListener {
            listener.onUserClickListenre(it, listUser[position])
        }
    }

    class ViewHolder (override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(item: FavoriteModel) {
            containerView.tvUsernameItemUser.text = item.login
            containerView.tvUserTypeItemUser.text = item.name
            Glide.with(containerView.context)
                .load(item.avatarUrl)
                .circleCrop()
                .into(containerView.ivItemUser)
        }
    }

    internal fun setUserSearchData(listUser : List<FavoriteModel>){ this.listUser = listUser
        notifyDataSetChanged() }

    interface Listener {
        fun onUserClickListenre(view: View, data: FavoriteModel)
    }
}