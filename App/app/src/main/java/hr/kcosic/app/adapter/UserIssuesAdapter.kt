package hr.kcosic.app.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.ContextInstance
import hr.kcosic.app.model.entities.Issue
import hr.kcosic.app.model.listeners.ButtonClickListener
import hr.kcosic.app.model.listeners.TextChangedListener


class UserIssuesAdapter(
    private var issues: MutableList<Issue>,
    private var buttonClickListener: ButtonClickListener,

    ) : RecyclerView.Adapter<UserIssuesAdapter.ViewHolder>() {

    private var selectedPosition = -1

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.issue_list_user_layout, parent, false)
        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = issues[position]
        checkButtonVisibility(holder, item)

        holder.etPrice.text = if(item.Price == null) "0" else item.Price.toString()
        holder.etPrice.isEnabled = false

        holder.etDescription.text = item.Description
        holder.etDescription.isEnabled = false

        holder.btnAcceptIssue.setOnClickListener {
            item.Accepted = true
            buttonClickListener.onClick(item)
            checkButtonVisibility(holder, item)
        }

        holder.btnDeclineIssue.setOnClickListener {
            item.Accepted = false
            buttonClickListener.onClick(item)
            checkButtonVisibility(holder, item)
        }
    }

    private fun checkButtonVisibility(holder: ViewHolder, issue: Issue){
        if(issue.Deleted == true){
            holder.btnDeclineIssue.text = ContextInstance.getContext()?.getString(R.string.deleted)
            holder.btnDeclineIssue.isEnabled = false
            holder.btnAcceptIssue.visibility = View.GONE
            return
        }
        when (issue.Accepted) {
            true -> {
                holder.btnAcceptIssue.text = ContextInstance.getContext()?.getString(R.string.accepted)
                holder.btnAcceptIssue.isEnabled = false
                holder.btnAcceptIssue.visibility = View.VISIBLE
                holder.btnDeclineIssue.visibility = View.GONE
            }
            false -> {
                holder.btnDeclineIssue.text = ContextInstance.getContext()?.getString(R.string.declined)
                holder.btnDeclineIssue.isEnabled = false
                holder.btnDeclineIssue.visibility = View.VISIBLE
                holder.btnAcceptIssue.visibility = View.GONE
            }
            else -> {
                holder.btnDeclineIssue.text = ContextInstance.getContext()?.getString(R.string.decline)
                holder.btnDeclineIssue.isEnabled = true
                holder.btnDeclineIssue.visibility = View.VISIBLE
                holder.btnAcceptIssue.text = ContextInstance.getContext()?.getString(R.string.accept)
                holder.btnAcceptIssue.isEnabled = true
                holder.btnAcceptIssue.visibility = View.VISIBLE
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return issues.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val etPrice: TextView = itemView.findViewById(R.id.etPrice)
        val btnAcceptIssue: Button = itemView.findViewById(R.id.btnAcceptIssue)
        val btnDeclineIssue: Button = itemView.findViewById(R.id.btnDeclineIssue)
        val etDescription: TextView = itemView.findViewById(R.id.etDescription)
    }
}