package hr.kcosic.app.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.model.entities.Issue
import hr.kcosic.app.model.listeners.ButtonClickListener


class ShopIssuesAdapter(
    private var issues: MutableList<Issue>,
    private var deleteButtonClickListener: ButtonClickListener
) : RecyclerView.Adapter<ShopIssuesAdapter.ViewHolder>() {

    private var selectedPosition = -1

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.issue_list_shop_layout, parent, false)
        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = issues[position]
        if (item.Accepted == true || item.Deleted == true) {
            holder.etPrice.isEnabled = false
            holder.etDescription.isEnabled = false
            holder.btnDeleteIssue.visibility = View.INVISIBLE
        } else {
            holder.etPrice.isEnabled = true
            holder.etDescription.isEnabled = true
            holder.btnDeleteIssue.visibility = View.VISIBLE
        }
        holder.etPrice.text = if(item.Price == null) "0" else item.Price.toString()
        holder.etDescription.text = item.Description
        holder.tvUserAccepted.setCompoundDrawablesWithIntrinsicBounds(
            if (item.Accepted != null) {
                if (item.Accepted == true) R.drawable.check_green
                else R.drawable.xmark_red
            } else R.drawable.minus_gray, 0, 0, 0
        )



        holder.btnDeleteIssue.setOnClickListener {
            deleteButtonClickListener.onClick(
                item
            )

        }
        holder.etPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if(p0.isNullOrEmpty()){
                    item.Price = 0.0
                }else {
                    item.Price = p0.toString().toDouble()

                }
            }
        })
        holder.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                item.Description = p0.toString()
            }
        })
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return issues.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val etPrice: TextView = itemView.findViewById(R.id.etPrice)
        val tvUserAccepted: TextView = itemView.findViewById(R.id.tvUserAccepted)
        val btnDeleteIssue: ImageButton = itemView.findViewById(R.id.btnDeleteIssue)
        val etDescription: TextView = itemView.findViewById(R.id.etDescription)
    }
}