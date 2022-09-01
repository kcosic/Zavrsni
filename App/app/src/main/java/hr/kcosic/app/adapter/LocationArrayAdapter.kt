package hr.kcosic.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import hr.kcosic.app.R
import hr.kcosic.app.model.entities.Location
import kotlin.collections.addAll

@SuppressLint("PrivateResource")
class LocationArrayAdapter(
    context: Context,
    resource: Int,
    objects: MutableList<Location>
): ArrayAdapter<Location>(context, com.google.android.material.R.layout.mtrl_auto_complete_simple_item, objects) {
    private var locations: MutableList<Location> = mutableListOf()

    init {
        locations.addAll(objects)
    }

    override fun addAll(vararg items: Location?) {

        super.addAll(*items)
    }

}