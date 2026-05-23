package com.example.myordersapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.myordersapp.R
import com.example.myordersapp.model.Order
import com.example.myordersapp.model.OrderStatus

class OrdersAdapter(
    private var orders: List<Order>,
    private val onInvoiceClick: (Order) -> Unit,
    private val onBookAgainClick: (Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicleIcon: ImageView = itemView.findViewById(R.id.ivVehicleIcon)
        val vehicleType: TextView = itemView.findViewById(R.id.tvVehicleType)
        val dateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val orderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val pickupLocation: TextView = itemView.findViewById(R.id.tvPickupLocation)
        val dropLocation: TextView = itemView.findViewById(R.id.tvDropLocation)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val statusBadge: TextView = itemView.findViewById(R.id.tvStatusBadge)
        val btnInvoice: Button = itemView.findViewById(R.id.btnInvoice)
        val btnBookAgain: Button = itemView.findViewById(R.id.btnBookAgain)
        val menuIcon: ImageView = itemView.findViewById(R.id.ivMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.vehicleType.text = order.vehicleType
        holder.dateTime.text = order.dateTime
        holder.orderId.text = "Order ID: ${order.id}"
        holder.pickupLocation.text = order.pickupLocation
        holder.dropLocation.text = order.dropLocation
        holder.price.text = order.price

        // Render Status Badge matching screenshot visual specifications
        when (order.status) {
            OrderStatus.CANCELLED -> {
                holder.statusBadge.visibility = View.VISIBLE
                holder.statusBadge.text = "CANCELLED"
                holder.statusBadge.setBackgroundResource(R.drawable.bg_badge_cancelled)
                holder.statusBadge.setTextColor(holder.itemView.context.getColor(R.color.red_badge_text))
            }
            OrderStatus.COMPLETED -> {
                holder.statusBadge.visibility = View.GONE
            }
            OrderStatus.BOOKED_AGAIN -> {
                holder.statusBadge.visibility = View.GONE
            }
            else -> {
                holder.statusBadge.visibility = View.GONE
            }
        }

        // Action click handlers
        holder.btnInvoice.setOnClickListener {
            onInvoiceClick(order)
        }

        holder.btnBookAgain.setOnClickListener {
            onBookAgainClick(order)
        }

        // Extra action popup menu for three dots menu
        holder.menuIcon.setOnClickListener { view ->
            val context = view.context
            val popup = PopupMenu(context, view)
            popup.menu.add("View Trip Details")
            popup.menu.add("Report Delivery Issue")
            popup.menu.add("Share Live Trip Tracking")
            popup.setOnMenuItemClickListener { item ->
                android.widget.Toast.makeText(context, "${item.title} for ${order.id}", android.widget.Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}