package com.huseyincan.eventdriven.controller.ui.events

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentChooseSeatBinding
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Ticket
import com.huseyincan.eventdriven.model.data.base.Saver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChooseSeatFragment : Fragment() {

    private var _binding: FragmentChooseSeatBinding? = null
    private val binding get() = _binding!!

    val STATUS_AVAILABLE = 1
    val STATUS_BOOKED = 2
    val STATUS_RESERVED = 3
    var seats = ArrayList<String>()
    var SATIRSAYISI = 0
    var SUTUNSAYISI = 0
    var value: Event? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseSeatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createSeatingChart()
        koltuklariSatinAl()
    }

    fun createSeatingChart() {
        val bundle = this.arguments
        if (bundle != null) {
            value = bundle.getParcelable("buy") // replace with your key
        }
        if (value != null) {
            val parts = value!!.rowColumn?.split("X")
            SATIRSAYISI = parts!!.get(0).toInt() // 5
            SUTUNSAYISI = parts.get(1).toInt() // 5

            val array = Array(SATIRSAYISI) { IntArray(SUTUNSAYISI) }
            viewLifecycleOwner.lifecycleScope.launch {
                // Get the database instance
                val db = Saver.getInstance(requireContext())

                val eventDao = db.ticketDao()
                var tickets: List<Ticket> = listOf()

                withContext(Dispatchers.IO) {
                    // Fetch events in IO Dispatcher
                    tickets = eventDao.getEventTickets(value!!.eid)
                }
                for (i: Ticket in tickets) {
                    array[i.row?.toInt()!!][i.seat?.toInt()!!] = 1
                }

                val seatSize = 100
                val seatGaping = 10
                val mainLayout = binding.layoutSeat
                val layoutSeat = LinearLayout(requireContext())
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutSeat.orientation = LinearLayout.VERTICAL
                layoutSeat.layoutParams = params
                layoutSeat.setPadding(
                    8 * seatGaping,
                    8 * seatGaping,
                    8 * seatGaping,
                    8 * seatGaping
                )
                mainLayout.addView(layoutSeat)


                var satirLayout = LinearLayout(requireContext())
                satirLayout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(satirLayout);
                var count = 0
                val seatViewList = ArrayList<TextView>()
                for (i in 0 until SATIRSAYISI) {
                    for (j in 0 until SUTUNSAYISI) {
                        if (seats.contains("${i}X$j")) {
                            count++
                            val view = TextView(requireContext())
                            val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                            layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                            view.layoutParams = layoutParams
                            view.setPadding(0, 0, 0, 2 * seatGaping)
                            view.id = count
                            view.gravity = Gravity.CENTER
                            view.setBackgroundResource(R.drawable.select)
                            view.setText("$count ")
                            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                            view.setTextColor(Color.BLACK)
                            view.tag = STATUS_AVAILABLE
                            satirLayout.addView(view)
                            seatViewList.add(view)
                            view.setOnClickListener(clickListener)
                        } else if (array[i][j] == 1) {
                            count++
                            val view = TextView(requireContext())
                            val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                            layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                            view.layoutParams = layoutParams
                            view.setPadding(0, 0, 0, 2 * seatGaping)
                            view.id = count
                            view.gravity = Gravity.CENTER
                            view.setBackgroundResource(R.drawable.booked)
                            view.setTextColor(Color.WHITE)
                            view.tag = STATUS_BOOKED
                            view.setText("$count ")
                            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                            satirLayout.addView(view)
                            seatViewList.add(view)
                            view.setOnClickListener(clickListener)
                        } else if (array[i][j] == 0) {
                            count++
                            val view = TextView(requireContext())
                            val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                            layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                            view.layoutParams = layoutParams
                            view.setPadding(0, 0, 0, 2 * seatGaping)
                            view.id = count
                            view.gravity = Gravity.CENTER
                            view.setBackgroundResource(R.drawable.ic_seats_book)
                            view.setText("$count ")
                            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                            view.setTextColor(Color.BLACK)
                            view.tag = STATUS_AVAILABLE
                            satirLayout.addView(view)
                            seatViewList.add(view)
                            view.setOnClickListener(clickListener)
                        }
                    }
                    satirLayout = LinearLayout(requireContext())
                    satirLayout.setOrientation(LinearLayout.HORIZONTAL);
                    layoutSeat.addView(satirLayout);
                }
            }
        }
    }


    fun koltuklariSatinAl() {
        binding.chooseSeatBuy.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArrayList("koltuk", seats)
            bundle.putParcelable("etkinlik", value)
            findNavController().navigate(R.id.buyingFragment, bundle)
        }
    }

    val clickListener = View.OnClickListener { view ->
        val satir: Int = (view.id - 1) / SUTUNSAYISI
        val sutun: Int = (view.id - 1) % SUTUNSAYISI
        val seatStr = "${satir}X${sutun}"
        if (view.tag as Int == STATUS_AVAILABLE) {
            if (seats.contains(seatStr)) {
                seats.remove(seatStr)
                view.setBackgroundResource(R.drawable.ic_seats_book)
            } else {
                seats.add(seatStr)
                view.setBackgroundResource(R.drawable.select)
            }
        } else if (view.tag as Int == STATUS_BOOKED) {
            Toast.makeText(requireContext(), "Seat " + view.id + " is Booked", Toast.LENGTH_SHORT)
                .show()
        } else if (view.tag as Int == STATUS_RESERVED) {
            Toast.makeText(requireContext(), "Seat " + view.id + " is Reserved", Toast.LENGTH_SHORT)
                .show()
        }
    }
}