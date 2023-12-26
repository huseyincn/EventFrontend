package com.huseyincan.eventdriven.controller.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentEventDetailBinding
import com.huseyincan.eventdriven.model.data.Event

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private var isItOwner: Boolean = false

    private var value: Event? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
            value = bundle.getParcelable<Event>("event") // replace with your key
            if (value != null) {
                binding.eventDetailImage.setImageBitmap(value!!.image)
                binding.eventDetailName.text = value!!.eventName
                binding.describ.text = value!!.eventDetail
                binding.eventDetailDate.text = value!!.eventDate
                binding.eventDetailTime.text = value!!.eventTime
                binding.eventDetailLocation.text = value!!.eventLocation
                binding.eventDetailPrice.text = "${value!!.eventPrice} TRY"

                if (value!!.organizerId == "10")
                    isItOwner = true
                binding.detailBuyButton.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable("buy", value)
                    findNavController().navigate(R.id.chooseSeatFragment, bundle)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.event_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.update_event -> {
                if (isItOwner) {
                    val bundle = Bundle()
                    bundle.putParcelable("editEvent", value)
                    findNavController().navigate(
                        R.id.addEventFragment,
                        bundle
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "You are not the organizer of this event.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                true
            }

            R.id.seek_report -> {
                if (isItOwner) {
                    val bundle = Bundle()
                    bundle.putParcelable("reportEvent", value)
                    findNavController().navigate(
                        R.id.reportFragment,
                        bundle
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "You are not the organizer of this event.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}