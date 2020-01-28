package com.imgline.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.imgline.R
import com.imgline.data.MediaType
import com.imgline.data.Post
import com.imgline.data.SourceManager

class ListFragment: Fragment() {

    companion object {
        val TAG = ListFragment::class.java.simpleName
    }

    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_main, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        val colCount = autosizeGridLayout(activity!!.resources.getDimensionPixelSize(R.dimen.card_width), activity!!)
        recyclerView.layoutManager = GridLayoutManager(activity, colCount)
        val postAdapter = PostAdapter()
        recyclerView.adapter = postAdapter
        setHasOptionsMenu(true)
        val sourceViewModel : SourceManager by viewModels()
        sourceViewModel.mImages.observe(viewLifecycleOwner, Observer<List<Post>>{
            postAdapter.updateList(it)
        })
        sourceViewModel.requestImages()
        return view
    }
}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        if (hasAddedListener == false) {
            hasAddedListener = true
            itemView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val icon = itemView.findViewById<ImageView>(R.id.iconView)
                    iconWidth = icon.width
                    iconHeight = icon.height
                }
            })
        }
    }

    companion object {
        var iconWidth: Int? = null
        var iconHeight: Int? = null
        var hasAddedListener: Boolean = false
    }

    fun bind(post: Post) {
        Log.d(ListFragment.TAG, "Origin ${post.origin}")
        val context = itemView.context
        val imageView = itemView.findViewById<ImageView>(R.id.imageView2)
        val iconResource = Utils.getIcon(post.origin)
        val iconView = itemView.findViewById<ImageView>(R.id.iconView)
        //context.resources.getDimension()
        val drawable = scaleBitmap(context.resources, iconResource,
            iconWidth ?: context.resources.getDimensionPixelSize(R.dimen.default_icon_sample),
            iconHeight ?: context.resources.getDimensionPixelSize(R.dimen.default_icon_sample)
            )
        iconView.setImageBitmap(drawable)
        val playImage = itemView.findViewById<ImageView>(R.id.play_image)
        val galImage = itemView.findViewById<ImageView>(R.id.gallery_image)
        if (post.type == MediaType.VIDEO) {
            playImage.setImageResource(R.drawable.filled_play_button)
        } else {
            playImage.setImageDrawable(null)
        }
        if (post.isMultiple) {
            galImage.setImageResource(R.drawable.filled_collections)
        } else {
            galImage.setImageDrawable(null)
        }
        Glide
            .with(context)
            .load(post.thumbnailURL)
            .centerCrop()
            .placeholder(getCircleProgressDrawable(context))
            .error(R.drawable.ic_broken_image_24px)
            .into(imageView)
//        val pointView = itemView.findViewById<TextView>(R.id.pointsTextView)
//        pointView.text = context.resources.getQuantityString(R.plurals.pointsValue, post.rating, post.rating)
    }

}

class PostAdapter(var mItems: List<Post> = arrayListOf()) : RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostViewHolder(inflater.inflate(R.layout.post_container, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(mItems[position])
    }

    fun updateList(posts: List<Post>) {
        val oldItems = mItems
        mItems = posts
        DiffUtil.calculateDiff(PostDiffCallback(oldItems, mItems)).dispatchUpdatesTo(this)
    }
}

class PostDiffCallback(val oldPosts: List<Post>, val newPosts: List<Post>) :DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newPosts[newItemPosition].fullName == oldPosts[oldItemPosition].fullName
    }

    override fun getOldListSize(): Int {
        return oldPosts.size
    }

    override fun getNewListSize(): Int {
        return newPosts.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPosts[oldItemPosition] == newPosts[newItemPosition]
    }
}