package hr.kcosic.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.model.entities.Review


class ReviewsAdapter(
    private var reviews: List<Review>,
) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_list_item, parent, false)
        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = reviews[position]

        if(item.Rating != null){
            holder.rbRating.rating = item.Rating!!.toFloat()
        }
        holder.tvName.text = item.User!!.Username
        holder.tvComment.text = item.Comment

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return reviews.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val rbRating: RatingBar = itemView.findViewById(R.id.rbRating)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvComment: CheckBox = itemView.findViewById(R.id.tvComment)
    }
}