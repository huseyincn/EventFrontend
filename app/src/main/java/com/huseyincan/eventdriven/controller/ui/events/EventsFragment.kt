package com.huseyincan.eventdriven.controller.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentEventsBinding
import com.huseyincan.eventdriven.model.adapter.AdapterX
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.base.Saver
import com.huseyincan.eventdriven.model.inMem.EventSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterX

    private val eventSystem: EventSystem by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = AdapterX()

        val recyclerView: RecyclerView = binding.recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        eventSystem.events.observe(viewLifecycleOwner) {
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

            val eventDao = db.eventDao()
            var events: List<Event> = listOf()

            withContext(Dispatchers.IO) {
                // Fetch events in IO Dispatcher
                events = eventDao.getAllEvents()
            }

            eventSystem.createModel(events)
        }
    }

    private fun addClickListenerToRecyclerView(adapterX: AdapterX) {
        adapterX.setOnItemClickListener(object : AdapterX.onItemClickListener {
            override fun onItemClick(position: Int) {
                val item = eventSystem.events.value!![position]
                val bundle = Bundle()
                bundle.putParcelable("event", item)
                findNavController().navigate(R.id.eventDetailFragment, bundle)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.events_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_event -> {
                val bundle = Bundle()
                bundle.putParcelable("editEvent", null)
                findNavController().navigate(R.id.addEventFragment, bundle)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}