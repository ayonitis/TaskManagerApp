package com.creativeinstitute.mytaskmanagerapp.CardWork

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.creativeinstitute.mytaskmanagerapp.databinding.ViewBinding
import java.util.*

class Adapter(private var data: List<CardInfo>) : RecyclerView.Adapter<Adapter.ViewHolder>(), Filterable {

    private var filteredList: List<CardInfo> = data

    class ViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var setTitle = binding.title
        var setDescription = binding.description
        var setPriority = binding.priority
        var layout = binding.mylayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val priority = filteredList[position].priority.trim().toLowerCase(Locale.getDefault())

        when (priority) {
            "pending" -> holder.layout.setBackgroundColor(Color.parseColor("#b31307"))
            "soon" -> holder.layout.setBackgroundColor(Color.parseColor("#b87e14"))
            "completed" -> holder.layout.setBackgroundColor(Color.parseColor("#187004"))
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateCard::class.java)
            intent.putExtra("id", position)
            holder.itemView.context.startActivity(intent)
        }

        holder.setTitle.text = filteredList[position].title
        holder.setDescription.text = filteredList[position].description
        holder.setPriority.text = filteredList[position].priority
    }

    // Add a new function to update the filtered list
    fun updateFilteredList(newList: List<CardInfo>) {
        filteredList = newList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase(Locale.getDefault())
                val filterResults = FilterResults()
                if (queryString.isNullOrBlank()) {
                    filterResults.values = data
                } else {
                    val filtered = data.filter {
                        it.title.toLowerCase(Locale.getDefault()).contains(queryString) ||
                                it.description.toLowerCase(Locale.getDefault()).contains(queryString) ||
                                it.priority.toLowerCase(Locale.getDefault()).contains(queryString)
                    }
                    filterResults.values = filtered
                }
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredList = filterResults?.values as List<CardInfo>
                notifyDataSetChanged()
            }
        }
    }
}
