package com.huseyincan.eventdriven.controller.ui.tickets

import android.app.Dialog
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentTicketsBinding
import com.huseyincan.eventdriven.model.adapter.AdapterTicket
import com.huseyincan.eventdriven.model.adapter.AdapterX
import com.huseyincan.eventdriven.model.inMem.EventSystem
import com.huseyincan.eventdriven.model.inMem.TicketSystem

class TicketsFragment : Fragment() {

    private var _binding: FragmentTicketsBinding? = null
    private lateinit var adapter: AdapterTicket
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        adapter = AdapterTicket()
        val recyclerView: RecyclerView = binding.recyclerTicket
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(context)

        ticketSystem.tickets.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
            addClickListenerToRecyclerView(adapter)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun showBottomDialog(){
        val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.DialogAnimation)
//        val bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottomsheetlayout)
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
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.events_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.create_event -> {
//                findNavController().navigate(R.id.addEventFragment)
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}

