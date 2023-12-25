package com.huseyincan.eventdriven.controller.ui.tickets

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentTicketsBinding
import com.huseyincan.eventdriven.model.adapter.AdapterTicket
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Rating
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
                        if (y.eventId == k.eid) {
                            ticketAdapter.add(
                                TicketForAdapter(
                                    k.eventName,
                                    k.eventDate,
                                    k.eventLocation,
                                    k.eventTime,
                                    k.image,
                                    y.row,
                                    y.seat,
                                    y.eventId,
                                    y.tid
                                )
                            )
                        }
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
                showBottomDialog(item)
            }
        })
    }

    private fun showBottomDialog(item: TicketForAdapter) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.DialogAnimation)
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val bottomSheetView =
            LayoutInflater.from(requireContext()).inflate(R.layout.bottomsheetlayout, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val ticketImage: ImageView = bottomSheetView.findViewById(R.id.imageViewTicketD)
        val ticketRow: TextView = bottomSheetView.findViewById(R.id.textViewTicketRow)
        val ticketSeat: TextView = bottomSheetView.findViewById(R.id.textViewColumn)
        val ticketRate: Button = bottomSheetView.findViewById(R.id.buttonRating)
        val ratingBar: RatingBar = bottomSheetView.findViewById(R.id.ratingBar)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Get the database instance
            val db = Saver.getInstance(requireContext())

            val rateDao = db.ratingDao()

            ticketImage.setImageBitmap(encodeAsBitmap(item.toString(), 1000, 1000))
            ticketRow.text = "Row = ${(item.row!!.toInt() + 1).toString()}"
            ticketSeat.text = "Seat = ${(item.seat!!.toInt() + 1).toString()}"

            bottomSheetDialog.show()
            var ratingFlag: Boolean = false
            withContext(Dispatchers.IO) {
                // Fetch events in IO Dispatcher
                val rate = rateDao.getByEventId(item.eventId!!, "10", item.ticketId!!)

                if (rate.size > 0) {
                    ratingBar.rating = rate[0].rate!!.toFloat()
                    ratingFlag = true
                }

                ticketRate.setOnClickListener {
                    val noOfStar = ratingBar.numStars
                    val getRating = ratingBar.rating
                    Toast.makeText(it.context, "Rating: $getRating/$noOfStar", Toast.LENGTH_LONG)
                        .show()
                    if (!ratingFlag) {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            rateDao.insertAll(
                                Rating(
                                    item.eventId!!,
                                    "10",
                                    item.ticketId!!,
                                    getRating.toString()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String, width: Int, height: Int): Bitmap? {
        val result: BitMatrix
        try {
            result = MultiFormatWriter().encode(
                str,
                BarcodeFormat.QR_CODE, width, height, null
            )
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }
        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h)
        return bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}