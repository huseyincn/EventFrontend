package com.huseyincan.eventdriven.controller.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentEventsBinding
import com.huseyincan.eventdriven.databinding.FragmentReportBinding


class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Inflate the layout for this fragment

        val tableLayout = binding.tableLayout

        val tableRow = LayoutInflater.from(requireContext()).inflate(R.layout.table_row, null) as TableRow


        //TODO:
        tableRow.findViewById<TextView>(R.id.ticketSalesTextView).setText("")
        tableRow.findViewById<TextView>(R.id.revenueTextView).setText("")
        tableRow.findViewById<TextView>(R.id.attendanceTextView).setText("")




        tableLayout.addView(tableRow)
        return root
    }


}