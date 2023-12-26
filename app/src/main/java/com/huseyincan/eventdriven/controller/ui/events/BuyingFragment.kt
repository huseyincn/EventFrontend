package com.huseyincan.eventdriven.controller.ui.events

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentBuyingBinding
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Ticket
import com.huseyincan.eventdriven.model.data.base.Saver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BuyingFragment : Fragment() {

    private var _binding: FragmentBuyingBinding? = null
    private val binding get() = _binding!!

    var totalAmount: Int = 0
    var value: Event? = null
    var liste: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuyingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expDateGenarator()
        putHypenEveryFour()
        priceCalc()
        makePayment()
    }

    fun makePayment() {
        binding.paythecost.setOnClickListener {
            if (totalAmount == 0) {
                Toast.makeText(
                    requireContext(),
                    "Please add seat to buy ticket.",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    // Get the database instance
                    val db = Saver.getInstance(requireContext())

                    val ticketDao = db.ticketDao()

                    withContext(Dispatchers.IO) {
                        if (liste != null && value != null) {
                            for (i in 0 until (liste?.size ?: 0)) {
                                val parts = liste!![i].split("X")
                                val row = parts[0]
                                val column = parts[1]
                                ticketDao.insertAll(Ticket(value!!.eid, "10", row, column))
                            }
                        }
                    }
                }
                findNavController().popBackStack(R.id.navigation_home, true)
                findNavController().navigate(R.id.navigation_tickets)
            }
        }
    }

    fun priceCalc() {
        val bundle = this.arguments
        if (bundle != null) {
            value = bundle.getParcelable("etkinlik") // replace with your key
            liste = bundle.getStringArrayList("koltuk")
        }
        if (value != null && liste != null) {
            totalAmount = (value!!.eventPrice?.toInt() ?: 0) * liste!!.size
            binding.buyPrice.text =
                "$totalAmount TRY WILL BE DEDUCTED FOR ${liste!!.size} TICKET(s).\""
        }
    }

    fun putHypenEveryFour() {
        binding.cardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Remove this TextWatcher to prevent stack overflow
                binding.cardNumber.removeTextChangedListener(this)

                // Insert hyphen every 4 characters
                val stringWithHyphen = insertHyphenEveryFourChars(s.toString())
                binding.cardNumber.setText(stringWithHyphen)

                // Set the cursor at the end of the text
                binding.cardNumber.setSelection(stringWithHyphen.length)

                // Add this TextWatcher back
                binding.cardNumber.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
    }

    fun insertHyphenEveryFourChars(input: String): String {
        val sb = StringBuilder(input.replace("-", ""))
        var i = 0
        while (i + 4 < sb.length) {
            sb.insert(i + 4, "-")
            i += 5
        }
        return sb.toString()
    }

    fun expDateGenarator() {
        val spinnerItems: MutableList<String> = ArrayList()
        val startDate = LocalDate.now()
        val endDate = startDate.plusYears(5)
        var current = startDate
        val formatter = DateTimeFormatter.ofPattern("MM/yy")
        while (current.isBefore(endDate) || current.isEqual(endDate)) {
            spinnerItems.add(current.format(formatter))
            current = current.plusMonths(1)
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.expDate.adapter = adapter
    }


}