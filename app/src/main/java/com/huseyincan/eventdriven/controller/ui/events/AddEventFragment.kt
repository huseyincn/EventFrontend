package com.huseyincan.eventdriven.controller.ui.events

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.huseyincan.eventdriven.R
import com.huseyincan.eventdriven.databinding.FragmentAddEventBinding
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.base.Saver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEventFragment : Fragment() {

    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!

    private var eventX: Event? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openDatePicker()
        openTimeDialog()
        chooseImage()

        val bundle = this.arguments
        if (bundle != null) {
            eventX = bundle.getParcelable<Event>("editEvent") // replace with your key

            if (eventX == null) {
                createEvent()
            } else {
                binding.addEventTitleText.text = "UPDATE EVENT"
                binding.eventImage.setImageBitmap(eventX!!.image)
                val instanceX = Editable.Factory.getInstance()
                binding.eventName.text = instanceX.newEditable(eventX!!.eventName)
                binding.eventDetail.text = instanceX.newEditable(eventX!!.eventDetail)
                binding.eventLocation.text = instanceX.newEditable(eventX!!.eventLocation)
                binding.eventTime.text = instanceX.newEditable(eventX!!.eventTime)
                binding.eventDate.text = instanceX.newEditable(eventX!!.eventDate)
                val parts = eventX!!.rowColumn!!.split("X")
                binding.rowNumberText.text = instanceX.newEditable(parts[0])
                binding.columnNumberText.text = instanceX.newEditable(parts[1])
                binding.ticketPrice.text = instanceX.newEditable(eventX!!.eventPrice)
                binding.submitEvent.text = "UPDATE"
                updateEvent()
            }
        }
    }

    fun createEvent() {
        binding.submitEvent.setOnClickListener {
            val event: Event = Event(
                binding.eventName.text.toString(),
                binding.eventDetail.text.toString(),
                binding.eventLocation.text.toString(),
                binding.eventTime.text.toString(),
                binding.eventDate.text.toString(),
                binding.ticketPrice.text.toString(),
                "${binding.rowNumberText.text.toString()}X${binding.columnNumberText.text.toString()}",
                binding.eventImage.drawToBitmap(),
                "10"
            )

            viewLifecycleOwner.lifecycleScope.launch {
                val db = Saver.getInstance(requireContext())

                val eventDao = db.eventDao()

                withContext(Dispatchers.IO) {
                    eventDao.insertAll(event)
                }
            }

            findNavController().navigate(R.id.navigation_home)
        }
    }

    fun openTimeDialog() {
        binding.eventTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                requireContext(), { _, selectedHour, selectedMinute ->
                    binding.eventTime.text =
                        Editable.Factory.getInstance()
                            .newEditable("$selectedHour:$selectedMinute")
                }, hour, minute, true
            )
            timePickerDialog.show()
        }
    }

    val REQUEST_CODE: Int = 42
    fun chooseImage() {
        binding.eventImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun openDatePicker() {
        binding.eventDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    binding.eventDate.text = Editable.Factory.getInstance()
                        .newEditable("$selectedDay/$selectedMonth/${selectedYear.toString().takeLast(2)}")
                }, year, month, day
            )
            datePickerDialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            binding.eventImage.setImageURI(uri)
        }
    }

    fun updateEvent() {
        binding.submitEvent.setOnClickListener {

            eventX!!.eventName = binding.eventName.text.toString()
            eventX!!.eventDetail = binding.eventDetail.text.toString()
            eventX!!.eventLocation = binding.eventLocation.text.toString()
            eventX!!.eventTime = binding.eventTime.text.toString()
            eventX!!.eventDate = binding.eventDate.text.toString()
            eventX!!.eventPrice = binding.ticketPrice.text.toString()
            eventX!!.rowColumn =
                "${binding.rowNumberText.text.toString()}X${binding.columnNumberText.text.toString()}"
            eventX!!.image = binding.eventImage.drawToBitmap()
            eventX!!.organizerId = "10"


            viewLifecycleOwner.lifecycleScope.launch {
                val db = Saver.getInstance(requireContext())

                val eventDao = db.eventDao()

                withContext(Dispatchers.IO) {
                    eventDao.updateEvent(eventX!!)
                }
            }
            val CHANNEL_ID = "30"
            var builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.khont).setContentTitle("An Event just got updated.")
                .setContentText("${eventX!!.eventName} has just got updated check the details on ticket page.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
// Create the NotificationChannel (API 26+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "channelName"
                val descriptionText = "channelDescription"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

// Show the notification
            with(NotificationManagerCompat.from(requireContext())) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    ) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            43
                        )

                        with(NotificationManagerCompat.from(requireContext())) {
                            notify(10, builder.build())
                        }
                    }
                } else {
                    // Permission has already been granted
                    with(NotificationManagerCompat.from(requireContext())) {
                        notify(10, builder.build())
                    }
                }

                findNavController().navigate(R.id.navigation_home)
            }
        }
    }
}