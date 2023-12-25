package com.huseyincan.eventdriven.controller.ui.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentTicketsBinding
import com.huseyincan.eventdriven.model.adapter.AdapterTicket
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Ticket
import com.huseyincan.eventdriven.model.data.TicketForAdapter
import com.huseyincan.eventdriven.model.data.base.Saver
import com.huseyincan.eventdriven.model.inMem.TicketSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TicketsFragment : Fragment() {

    private var _binding: FragmentTicketsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: AdapterTicket

    private val ticketSystem: TicketSystem by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = AdapterTicket()
        val recyclerView: RecyclerView = binding.recyclerTicket
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        ticketSystem.tickets.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
            addClickListenerToRecyclerView(adapter)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            // Get the database instance
            val db = Saver.getInstance(requireContext())

            val ticketDao = db.ticketDao()
            val eventDao = db.eventDao()
            var tickets: List<Ticket> = listOf()
            val events: ArrayList<Event> = arrayListOf()

            val eids: ArrayList<String> = arrayListOf()
            val ticketAdapter: ArrayList<TicketForAdapter> = arrayListOf()

            withContext(Dispatchers.IO) {
                // Fetch events in IO Dispatcher
                tickets = ticketDao.getAllTickets()
                for (x: Ticket in tickets) {
                    if (!eids.contains(x.eventId)) {
                        eids.add(x.eventId)
                    }
                }
                events.addAll(eventDao.getByEID(eids.toTypedArray()))

                for (y: Ticket in tickets) {
                    for (k: Event in events) {
                        if (y.eventId == k.eid)
                            ticketAdapter.add(
                                TicketForAdapter(
                                    k.eventName,
                                    k.eventDate,
                                    k.eventLocation,
                                    k.eventTime,
                                    k.image
                                )
                            )
                    }
                }
            }
            ticketSystem.createModel(ticketAdapter.toList())
        }
    }

    private fun addClickListenerToRecyclerView(adapterTicket: AdapterTicket) {
        adapterTicket.setOnItemClickListener(object : AdapterTicket.onItemClickListener {
            override fun onItemClick(position: Int) {
                val item = ticketSystem.tickets.value!![position]
//                Toast.makeText(
//                    requireContext(),
//                    "${item.eventId}, ${item.profileId}, ${item.tid}, ${item.row}, ${item.seat}",
//                    Toast.LENGTH_LONG
//                ).show()
                showBottomDialog()
            }
        })
    }

    private fun showBottomDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.DialogAnimation)
        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottomsheetlayout,null)
        var ticketImage: ImageView
        var ticketRow: TextView
        var ticketSeat: TextView
        var ticketRate: Button
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.bottomsheetlayout)
        bottomSheetDialog.show()
        ticketImage = bottomSheetDialog.findViewById(R.id.imageViewTicketD)!!
        ticketRow = bottomSheetDialog.findViewById(R.id.textViewTicketRow)!!
        ticketSeat = bottomSheetDialog.findViewById(R.id.textViewColumn)!!
        ticketRate = bottomSheetDialog.findViewById(R.id.buttonRating)!!


        bottomSheetDialog.setContentView(R.layout.bottomsheetlayout)
        bottomSheetDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}