package hr.kcosic.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.model.entities.Car
import hr.kcosic.app.model.listeners.ButtonClickListener


class VehiclesAdapter(
    private var vehicles: List<Car>,
    private var editListener: ButtonClickListener,
    private var deleteListener: ButtonClickListener,
) : RecyclerView.Adapter<VehiclesAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_list_item, parent, false)
        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = vehicles[position]

        holder.tvManufacturer.text = item.Manufacturer
        holder.tvModel.text = item.Model
        holder.tvYear.text = item.Year.toString()
        holder.tvOdometer.text = item.Odometer.toString()

        holder.btnEdit.setOnClickListener {
            editListener.onClick(item)
        }

        holder.btnDelete.setOnClickListener {
            deleteListener.onClick(item)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return vehicles.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvManufacturer: TextView = itemView.findViewById(R.id.tvManufacturer)
        val tvModel: TextView = itemView.findViewById(R.id.tvModel)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        val tvOdometer: TextView = itemView.findViewById(R.id.tvOdometer)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

    }
}