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
import com.huseyincan.eventdriven.databinding.FragmentBuyingBinding
import com.huseyincan.eventdriven.model.data.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BuyingFragment : Fragment() {

    private var _binding: FragmentBuyingBinding? = null
    private val binding get() = _binding!!

    var totalAmount: Int = 0
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
        if (totalAmount == 0) {
            Toast.makeText(requireContext(), "Please add seat to buy ticket.", Toast.LENGTH_LONG)
                .show()
        } else {

            // payment loading animasyon çıkar ticket objesi oluştur db ekle
        }
    }

    fun priceCalc() {
        val bundle = this.arguments
        var value: Event? = null
        var liste: ArrayList<String>? = null
        if (bundle != null) {
            value = bundle.getParcelable("etkinlik") // replace with your key
            liste = bundle.getStringArrayList("koltuk")
        }
        if (value != null && liste != null) {
            totalAmount = (value.eventPrice?.toInt() ?: 0) * liste.size
            binding.buyPrice.text =
                "$totalAmount TRY WILL BE DEDUCTED FOR ${liste.size} TICKET(s).\""
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