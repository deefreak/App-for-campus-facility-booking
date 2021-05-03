package com.example.cfb.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.cfb.R
import com.example.cfb.models.attendance


class ViewAllAttendanceAdapter(var context: Context, var attendanceList: MutableList<attendance>):
    RecyclerView.Adapter<ViewAllAttendanceAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var email: TextView = itemView.findViewById(R.id.email)
    }
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {

        holder.email.text = "• " + attendanceList[position].email

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.attendees_item,parent,false)
        return DetailsViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }

    fun setList(list: MutableList<attendance>){
        attendanceList = list

        notifyDataSetChanged()
    }
}

