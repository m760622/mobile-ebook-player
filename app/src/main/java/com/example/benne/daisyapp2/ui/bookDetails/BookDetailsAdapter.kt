package com.example.benne.daisyapp2.ui.bookDetails

import android.support.v4.media.*
import android.support.v7.widget.*
import android.view.*
import com.example.benne.daisyapp2.data.daisy202.*
import android.view.LayoutInflater
import com.example.benne.daisyapp2.databinding.*
import com.example.benne.daisyapp2.playback.MediaService.Companion.ELEMENT_TYPE_KEY
import com.example.benne.daisyapp2.ui.*


/**
 * Created by benne on 11/01/2018.
 */
class BookDetailsAdapter(private val vm: BookDetailsViewModel)
    : RecyclerView.Adapter<BookDetailsAdapter.ViewHolder>() {

    var items: List<BookSection> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: BookDetailsAdapter.ViewHolder, position: Int) {
        val item = items[position].section
        val listener = object : BookDetailsUserActionListener {
            override fun onPlaySection(item: MediaBrowserCompat.MediaItem) {
                vm.playSection(item)
            }
        }

        holder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemPlayableMediaBinding
                .inflate(layoutInflater, parent, false)
        return BookDetailsAdapter.ViewHolder(itemBinding)
    }

    class ViewHolder(private val binding: ListItemPlayableMediaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookSection, listener: BookDetailsUserActionListener) {
            binding.item = item
            binding.listener = listener
            val adapter = binding.bookPageNumberGv.adapter
            if (adapter !is PageNumbersGridAdapter) {
                binding.bookPageNumberGv.adapter = PageNumbersGridAdapter()
            }
            (binding.bookPageNumberGv.adapter as PageNumbersGridAdapter).items = item.pages

            binding.bookPageNumberGv.invalidateViews()
            //binding.bookPageNumberGv.adapter = adapter
            binding.executePendingBindings()
        }
    }
}