package hr.kcosic.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.model.listeners.ButtonClickListener


class TimeAdapter(
    private var issues: List<String>,
    private var workHours: String,
    private var radioButtonClickListener: ButtonClickListener
) : RecyclerView.Adapter<TimeAdapter.ViewHolder>() {

    var times: MutableList<String> = mutableListOf()
    private var selectedPosition = -1

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        times = mutableListOf()
        populateTimeArray()

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_selection_layout, parent, false)

        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = times[position]

        holder.tvTime.text = item
        holder.rbTime.isEnabled = issues.find { it == item } == null
        holder.rbTime.isChecked = position == selectedPosition

        holder.rbTime.setOnCheckedChangeListener { _, b ->
            // check condition
            if (b) {
                // When checked
                // update selected position
                selectedPosition = holder.adapterPosition
                // Call listener
                radioButtonClickListener.onClick(
                    holder.tvTime.text.toString()
                )
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        if (times.size == 0 && workHours.isNotEmpty()) {
            populateTimeArray()
        }

        return times.size
    }

    private fun populateTimeArray(){
        val splitHours = workHours.split('-')
        val startHourMinute = splitHours[0]
        val endHourMinute = splitHours[1]
        val startHour = getNumberFromHour(startHourMinute)
        val endHour = getNumberFromHour(endHourMinute)

        for (i in startHour..endHour step 1) {
            times.add("${if (i < 10) "0$i" else i}:00")
        }
    }

    private fun getNumberFromHour(rawHourMinute: String): Int {
        return rawHourMinute.split(':')[0].toInt()
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val rbTime: RadioButton = itemView.findViewById(R.id.rbTime)
    }
}