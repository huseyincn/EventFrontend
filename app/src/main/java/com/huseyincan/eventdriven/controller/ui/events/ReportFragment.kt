package com.huseyincan.eventdriven.controller.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentReportBinding
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Rating
import com.huseyincan.eventdriven.model.data.Ticket
import com.huseyincan.eventdriven.model.data.base.Saver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var eventX: Event? = null
        val bundle = this.arguments
        if (bundle != null) {
            eventX = bundle.getParcelable<Event>("reportEvent") // replace with your key
            if (eventX != null) {
                binding.imageEventReport.setImageBitmap(eventX.image)
                val tickets: ArrayList<Ticket> = arrayListOf()
                val ratings: ArrayList<Rating> = arrayListOf()
                viewLifecycleOwner.lifecycleScope.launch {
                    // Get the database instance
                    val db = Saver.getInstance(requireContext())

                    val ticketDao = db.ticketDao()
                    val ratingDao = db.ratingDao()

                    withContext(Dispatchers.IO) {
                        tickets.addAll(ticketDao.getEventTickets(eventX.eid))
                        ratings.addAll(ratingDao.getRatingForReport(eventX.eid))
                    }
                }


                val tableRow =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.table_row, null) as TableRow


                tableRow.findViewById<TextView>(R.id.ticketSalesTextView).setText("${tickets.size}")
                tableRow.findViewById<TextView>(R.id.revenueTextView)
                    .setText("${eventX.eventPrice!!.toInt() * tickets.size} TRY")
                tableRow.findViewById<TextView>(R.id.attendanceTextView).setText("${ratings.size}")

                var rateScore : Double = 0.0
                for (x : Rating in ratings) {
                    rateScore += x.rate!!.toFloat()
                }

                binding.ratingBarReport.rating = (rateScore / ratings.size).toFloat()

                binding.tableLayout.addView(tableRow)
            }
        }
    }

}