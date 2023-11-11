package com.huseyincan.eventdriven.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.eventdriven.R

class AdapterX(private var items: List<Event> = emptyList()) : RecyclerView.Adapter<AdapterX.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView
        val eventDetail: TextView
        val eventTime: TextView
        val eventLocation: TextView

        init {
            // Define click listener for the ViewHolder's View
            eventName = view.findViewById(R.id.eventName)
            eventDetail = view.findViewById(R.id.eventDetail)
            eventTime = view.findViewById(R.id.eventTime)
            eventLocation = view.findViewById(R.id.eventLocation)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.event_card, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val tmp: Event = items.get(position)
        viewHolder.eventName.text = tmp.eventName
        viewHolder.eventDetail.text = tmp.eventDetail
        viewHolder.eventLocation.text = tmp.eventLocation
        viewHolder.eventTime.text = tmp.eventTime
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

    fun updateItems(newItems: List<Event>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}