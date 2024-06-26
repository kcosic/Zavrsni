package hr.kcosic.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.model.entities.Request
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.listeners.ButtonClickListener


class RequestsAdapter(
    private var requests: List<Request>,
    private var menuButtonClickListener: ButtonClickListener
) : RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    private var selectedPosition = -1

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_list_item, parent, false)
        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = requests[position]

        holder.tvDate.text = Helper.formatDate(item.RequestDate!!)
        holder.tvName.text = item.Shop!!.ShortName
        holder.tvCompleted.setCompoundDrawablesWithIntrinsicBounds(
            if (item.Completed != null && item.Completed == true) R.drawable.check_green
            else R.drawable.xmark_red, 0, 0, 0
        )
        holder.tvShopConsent.setCompoundDrawablesWithIntrinsicBounds(
            if (item.ShopAccepted != null) {
                if (item.ShopAccepted == true) R.drawable.check_green
                else R.drawable.xmark_red
            } else R.drawable.minus_gray, 0, 0, 0
        )

        holder.tvUserConsent.setCompoundDrawablesWithIntrinsicBounds(
            if (item.UserAccepted != null) {
                if (item.UserAccepted == true) R.drawable.check_green
                else R.drawable.xmark_red
            } else R.drawable.minus_gray, 0, 0, 0
        )
        holder.btnMenu.setOnClickListener {
            menuButtonClickListener.onClick(
                item
            )

        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return requests.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvUserConsent: TextView = itemView.findViewById(R.id.tvUserConsent)
        val tvShopConsent: TextView = itemView.findViewById(R.id.tvShopConsent)
        val tvCompleted: TextView = itemView.findViewById(R.id.tvCompleted)
        val btnMenu: ImageButton = itemView.findViewById(R.id.btnMenu)
    }
}