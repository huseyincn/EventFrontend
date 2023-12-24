package com.huseyincan.eventdriven.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Ticket

class AdapterTicket(private var items: List<Ticket> = emptyList()) : RecyclerView.Adapter<AdapterTicket.ViewHolder>() {
    private var listenerItems: AdapterTicket.onItemClickListener? = null
    class ViewHolder(view: View, private val listener: AdapterTicket.onItemClickListener?) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val ticketName: TextView
        val ticketDate: TextView
        val ticketTime: TextView
        val ticketLocation: TextView
        val ticketImage : ImageView

        init {
            // Define click listener for the ViewHolder's View
            ticketName = view.findViewById(R.id.ticketName)
            ticketLocation = view.findViewById(R.id.ticketLocation)
            ticketDate = view.findViewById(R.id.ticketDate)
            ticketTime = view.findViewById(R.id.ticketTime)
            ticketImage = view.findViewById(R.id.ticketImage)
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
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AdapterTicket.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.event_card, viewGroup, false)

        return AdapterTicket.ViewHolder(view, listenerItems)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: AdapterTicket.ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val tmp: Ticket = items.get(position)
//        val tmpEvent: Event =  tmp.
//        viewHolder.ticketName.text = tmp.
//        viewHolder.ticketDate.text = tmp.ticketDate
//        viewHolder.ticketLocation.text = tmp.ticketLocation
//        viewHolder.ticketTime.text = tmp.ticketTime
//        viewHolder.ticketImage.setImageBitmap(tmp.ticketImage)
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

    fun updateItems(newItems: List<Ticket>) {
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