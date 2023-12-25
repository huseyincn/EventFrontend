package com.huseyincan.eventdriven.controller.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentEventDetailBinding
import com.huseyincan.eventdriven.model.data.Event

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createEventDetail()
    }

    fun createEventDetail() {
        val bundle = this.arguments
        if (bundle != null) {
            val value = bundle.getParcelable<Event>("event") // replace with your key
            if (value != null) {
                binding.eventDetailImage.setImageBitmap(value.image)
                binding.eventDetailName.text = value.eventName
                binding.describ.text = value.eventDetail
                binding.eventDetailDate.text = value.eventDate
                binding.eventDetailTime.text = value.eventTime
                binding.eventDetailLocation.text = value.eventLocation
                binding.eventDetailPrice.text = "${value.eventPrice} TRY"

                binding.detailBuyButton.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable("buy", value)
                    findNavController().navigate(R.id.chooseSeatFragment, bundle)
                }
            }
        }
    }
}