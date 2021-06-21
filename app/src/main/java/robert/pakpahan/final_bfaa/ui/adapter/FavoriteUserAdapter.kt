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
import robert.pakpahan.final_bfaa.source.database.FavoriteModel

class FavoriteUserAdapter (private val context: Context, private val listener: Listener) :
    RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder> (){

    private var listFavorites = emptyList<FavoriteModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_user,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listFavorites.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(listFavorites[position])
        holder.itemView.setOnClickListener {
            listener.onFavoriteClickListener(it, listFavorites[position])
        }
    }

    class ViewHolder (override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(item: FavoriteModel){
            containerView.tvUsernameItemUser.text = item.login
            containerView.tvUserTypeItemUser.text = item.type
            Glide.with(containerView.context)
                .load(item.avatar_url)
                .circleCrop()
                .into(containerView.ivItemUser)
        }
    }

    internal fun setFavoritesData(listFavorites: List<FavoriteModel>){
        this.listFavorites = listFavorites
        notifyDataSetChanged()
    }

    interface Listener {
        abstract val FavoriteFragmentDirections: Any

        fun onFavoriteClickListener(view: View, data: FavoriteModel)
    }
}