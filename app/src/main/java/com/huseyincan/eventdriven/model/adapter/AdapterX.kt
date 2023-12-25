package com.huseyincan.eventdriven.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.model.data.Event

class AdapterX(private var items: List<Event> = emptyList()) :
    RecyclerView.Adapter<AdapterX.ViewHolder>() {

    private var listenerItems: onItemClickListener? = null

    class ViewHolder(view: View, private val listener: onItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val eventName: TextView
        val eventDate: TextView
        val eventTime: TextView
        val eventLocation: TextView
        val image: ImageView
        val eventPrice: TextView

        init {
            // Define click listener for the ViewHolder's View
            eventName = view.findViewById(R.id.eventName)
            eventLocation = view.findViewById(R.id.eventLocation)
            eventDate = view.findViewById(R.id.eventDate)
            eventTime = view.findViewById(R.id.eventTime)
            image = view.findViewById(R.id.resimKutusu)
            eventPrice = view.findViewById(R.id.eventPrice)
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener?.onItemClick(position)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.event_card, viewGroup, false)

        return ViewHolder(view, listenerItems)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val tmp: Event = items.get(position)
        viewHolder.eventName.text = tmp.eventName
        viewHolder.eventDate.text = tmp.eventDate
        viewHolder.eventLocation.text = tmp.eventLocation
        viewHolder.eventTime.text = tmp.eventTime
        viewHolder.eventPrice.text = "${tmp.eventPrice} TRY"
        viewHolder.image.setImageBitmap(tmp.image)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

    fun updateItems(newItems: List<Event>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        listenerItems = listener
    }
}