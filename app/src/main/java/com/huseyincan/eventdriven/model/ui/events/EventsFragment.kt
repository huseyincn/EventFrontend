package com.huseyincan.eventdriven.model.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.huseyincan.eventdriven.databinding.FragmentHomeBinding
import com.huseyincan.eventdriven.model.adapter.AdapterX
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.base.Saver
import com.huseyincan.eventdriven.model.inMem.EventSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventsFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var adapter: AdapterX

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val eventSystem: EventSystem by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = AdapterX()

        val recyclerView: RecyclerView = binding.recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            adapter.updateItems(it)
//        }
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
            val db = Room.databaseBuilder(
                requireContext(),
                Saver::class.java, "eventmanager"
            ).build()

            val eventDao = db.eventDao()
            var events: List<Event> = listOf()

            withContext(Dispatchers.IO) {
                // Fetch events in IO Dispatcher
                events = eventDao.getAllEvents()
            }

            // Use the fetched events on the main thread
            Toast.makeText(requireContext(),"db geldi",Toast.LENGTH_LONG).show()
        }
    }
    private fun addClickListenerToRecyclerView(adapterX: AdapterX) {
        adapterX.setOnItemClickListener(object : AdapterX.onItemClickListener{
            override fun onItemClick(position: Int) {
                val item = eventSystem.events.value!![position]
                Toast.makeText(requireContext(),"${item.eid}, ${item.eventName}, ${item.eventDetail}, ${item.eventTime}, ${item.eventLocation}",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}