package com.example.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.asteroidradar.databinding.ListItemAsteroidBinding
import com.example.asteroidradar.databinding.PictureOfTheDayHeaderBinding
import com.example.asteroidradar.domain.Asteroid
import com.example.asteroidradar.domain.PictureOfTheDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
class AsteroidListAdapter(private val clickListener: AsteroidClickListener): ListAdapter<Item, RecyclerView.ViewHolder>(DiffCallback) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private var pictureOfTheDay: PictureOfTheDay? = null
    private var listOfAsteroid: List<Asteroid>? = null

    companion object DiffCallback: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> PictureOfTheDayHeader.from(parent)
            ITEM_VIEW_TYPE_ITEM -> AsteroidRecyclerView.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addList(list: List<Asteroid>?){
        adapterScope.launch {
            listOfAsteroid = list
            addHeaderAndSubmitList()
        }
    }

    fun addHeader(pictureOfDay: PictureOfTheDay){
        adapterScope.launch {
            pictureOfTheDay = pictureOfDay
            addHeaderAndSubmitList()
        }
    }

    private suspend fun addHeaderAndSubmitList() {
        listOfAsteroid?.let { asteroidsList ->
            pictureOfTheDay?.let { pictureOfDay ->
                val items = listOf(Item.Header(pictureOfDay)) + asteroidsList.map {
                    Item.AsteroidItem(it)
                }
                withContext(Dispatchers.Main){
                    submitList(items)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder){
            is AsteroidRecyclerView -> {
                val asteroidItem = getItem(position) as Item.AsteroidItem
                holder.bind(clickListener, asteroidItem.asteroid)
            }
            is PictureOfTheDayHeader -> {
                val pictureOfTheDayItem = getItem(position) as Item.Header
                holder.bind(pictureOfTheDayItem.pictureOfDay)
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Item.Header -> ITEM_VIEW_TYPE_HEADER
            is Item.AsteroidItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class AsteroidRecyclerView(private var binding: ListItemAsteroidBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: AsteroidClickListener, item: Asteroid) {
            binding.asteroid = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): AsteroidRecyclerView {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
                return AsteroidRecyclerView(binding)
            }
        }
    }

    class PictureOfTheDayHeader(private var binding: PictureOfTheDayHeaderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: PictureOfTheDay?) {
            item?.let {
                binding.pictureOfDay = it
                binding.executePendingBindings()
            }
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PictureOfTheDayHeaderBinding.inflate(layoutInflater,parent,false)
                return PictureOfTheDayHeader(binding)
            }
        }
    }
}

class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}

sealed class Item {
    data class AsteroidItem(val asteroid: Asteroid) : Item() {
        override val id = asteroid.id
    }

    data class Header(val pictureOfDay: PictureOfTheDay?) : Item() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}