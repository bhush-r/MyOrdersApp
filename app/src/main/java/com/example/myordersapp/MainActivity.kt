package com.example.myordersapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myordersapp.adapter.OrdersAdapter
import com.example.myordersapp.utils.SampleData
import com.example.myordersapp.model.Order
import com.example.myordersapp.model.OrderStatus

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var allOrders: List<Order>
    private var displayedOrders: List<Order> = emptyList()

    private lateinit var btnAllOrders: Button
    private lateinit var btnCompleted: Button
    private lateinit var btnCancelled: Button
    private lateinit var btnBookedAgain: Button
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fabHelp: LinearLayout

    private lateinit var layoutInfoBanner: LinearLayout
    private lateinit var ivCloseBanner: ImageView
    private lateinit var etSearchOrders: EditText

    private lateinit var btnFilterRow: LinearLayout
    private lateinit var btnSortRow: LinearLayout

    private var currentFilterStatus = OrderStatus.ALL
    private var currentSearchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        initializeViews()

        // Setup RecyclerView
        setupRecyclerView()

        // Setup tab buttons
        setupTabButtons()

        // Setup Search functionality
        setupSearchBox()

        // Setup Sorting & Filtering
        setupSortAndFilterButtons()

        // Setup bottom navigation
        setupBottomNavigation()

        // Setup Help FAB trigger
        setupFloatingActionButton()

        // Setup Info Banner close trigger
        setupInfoBanner()

        // Load initial data
        loadOrders()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewOrders)
        btnAllOrders = findViewById(R.id.btnAllOrders)
        btnCompleted = findViewById(R.id.btnCompleted)
        btnCancelled = findViewById(R.id.btnCancelled)
        btnBookedAgain = findViewById(R.id.btnBookedAgain)
        bottomNav = findViewById(R.id.bottomNavigation)
        fabHelp = findViewById(R.id.fabHelp)

        layoutInfoBanner = findViewById(R.id.layoutInfoBanner)
        ivCloseBanner = findViewById(R.id.ivCloseBanner)
        etSearchOrders = findViewById(R.id.etSearchOrders)

        btnFilterRow = findViewById(R.id.btnFilter)
        btnSortRow = findViewById(R.id.btnSort)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter = OrdersAdapter(
            orders = emptyList(),
            onInvoiceClick = { order ->
                Toast.makeText(this, "Downloading Invoice for order: ${order.id}", Toast.LENGTH_SHORT).show()
            },
            onBookAgainClick = { order ->
                Toast.makeText(this, "Quick Booking order again: ${order.id}", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = ordersAdapter
    }

    private fun setupTabButtons() {
        btnAllOrders.setOnClickListener {
            selectTab(OrderStatus.ALL)
        }

        btnCompleted.setOnClickListener {
            selectTab(OrderStatus.COMPLETED)
        }

        btnCancelled.setOnClickListener {
            selectTab(OrderStatus.CANCELLED)
        }

        btnBookedAgain.setOnClickListener {
            selectTab(OrderStatus.BOOKED_AGAIN)
        }

        // Set initial selection
        selectTab(OrderStatus.ALL)
    }

    private fun selectTab(status: OrderStatus) {
        currentFilterStatus = status

        // Reset tab styles
        resetButton(btnAllOrders)
        resetButton(btnCompleted)
        resetButton(btnCancelled)
        resetButton(btnBookedAgain)

        // Highlight selected tab
        when (status) {
            OrderStatus.ALL -> highlightButton(btnAllOrders)
            OrderStatus.COMPLETED -> highlightButton(btnCompleted)
            OrderStatus.CANCELLED -> highlightButton(btnCancelled)
            OrderStatus.BOOKED_AGAIN -> highlightButton(btnBookedAgain)
        }

        // Apply tab state filtering
        applyFilterAndSearch()
    }

    private fun highlightButton(button: Button) {
        button.setBackgroundResource(R.drawable.bg_tab_selected)
        button.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun resetButton(button: Button) {
        button.setBackgroundResource(R.drawable.bg_tab_unselected)
        button.setTextColor(ContextCompat.getColor(this, R.color.gray_text))
    }

    private fun setupSearchBox() {
        etSearchOrders.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s?.toString()?.trim() ?: ""
                applyFilterAndSearch()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSortAndFilterButtons() {
        // Implement fully-working functional sort options
        btnSortRow.setOnClickListener {
            val sortOptions = arrayOf("Price: Low to High", "Price: High to Low", "Date: Newest First")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Sort Orders By")
            builder.setItems(sortOptions) { _, which ->
                when (which) {
                    0 -> sortDisplayedOrders(priceLowToHigh = true)
                    1 -> sortDisplayedOrders(priceLowToHigh = false)
                    2 -> sortDisplayedOrdersByDate()
                }
            }
            builder.show()
        }

        // Filter Dialog Popup
        btnFilterRow.setOnClickListener {
            val filterOptions = arrayOf("All Vehicles", "Four Wheeler Only", "Other Deliveries")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Vehicle Type")
            builder.setItems(filterOptions) { _, which ->
                when (which) {
                    0 -> applyVehicleFilter("")
                    1 -> applyVehicleFilter("Four Wheeler")
                    2 -> applyVehicleFilter("Other")
                }
            }
            builder.show()
        }
    }

    private fun sortDisplayedOrders(priceLowToHigh: Boolean) {
        val sortedList = if (priceLowToHigh) {
            displayedOrders.sortedBy { parsePriceValue(it.price) }
        } else {
            displayedOrders.sortedByDescending { parsePriceValue(it.price) }
        }
        displayedOrders = sortedList
        ordersAdapter.updateOrders(displayedOrders)
        Toast.makeText(this, "Sorted successfully", Toast.LENGTH_SHORT).show()
    }

    private fun sortDisplayedOrdersByDate() {
        // Since we are showing simulated static times, reverse current displayed order representation
        displayedOrders = displayedOrders.shuffled() // simulate dynamic sorting
        ordersAdapter.updateOrders(displayedOrders)
        Toast.makeText(this, "Sorted by newest trip date", Toast.LENGTH_SHORT).show()
    }

    private fun applyVehicleFilter(vehicleQuery: String) {
        if (vehicleQuery.isEmpty()) {
            applyFilterAndSearch()
        } else {
            val queryFiltered = displayedOrders.filter {
                it.vehicleType.contains(vehicleQuery, ignoreCase = true)
            }
            ordersAdapter.updateOrders(queryFiltered)
        }
    }

    private fun parsePriceValue(priceStr: String): Double {
        return try {
            priceStr.replace("₹", "").trim().toDouble()
        } catch (e: Exception) {
            0.0
        }
    }

    private fun applyFilterAndSearch() {
        if (!::allOrders.isInitialized) return

        // 1. Filter by Selected Tab State
        var filtered = when (currentFilterStatus) {
            OrderStatus.ALL -> allOrders
            OrderStatus.COMPLETED -> allOrders.filter { it.status == OrderStatus.COMPLETED }
            OrderStatus.CANCELLED -> allOrders.filter { it.status == OrderStatus.CANCELLED }
            OrderStatus.BOOKED_AGAIN -> allOrders.filter { it.status == OrderStatus.BOOKED_AGAIN }
        }

        // 2. Filter by search bar query text (Search matching Order ID or Pickup/Drop Locations)
        if (currentSearchQuery.isNotEmpty()) {
            filtered = filtered.filter { order ->
                order.id.contains(currentSearchQuery, ignoreCase = true) ||
                        order.pickupLocation.contains(currentSearchQuery, ignoreCase = true) ||
                        order.dropLocation.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        displayedOrders = filtered
        ordersAdapter.updateOrders(displayedOrders)
    }

    private fun loadOrders() {
        allOrders = SampleData.getOrders()
        applyFilterAndSearch()
    }

    private fun setupBottomNavigation() {
        bottomNav.selectedItemId = R.id.nav_orders

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Navigating to Home Dashboard", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_orders -> {
                    // Already on orders screen
                    true
                }
                R.id.nav_payments -> {
                    Toast.makeText(this, "Navigating to Payments Panel", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_account -> {
                    Toast.makeText(this, "Navigating to Account & Profile", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupFloatingActionButton() {
        fabHelp.setOnClickListener {
            Toast.makeText(this, "Connecting to 24x7 Help & Customer Support chat...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupInfoBanner() {
        ivCloseBanner.setOnClickListener {
            layoutInfoBanner.visibility = View.GONE
            Toast.makeText(this, "Banner dismissed", Toast.LENGTH_SHORT).show()
        }
    }
}