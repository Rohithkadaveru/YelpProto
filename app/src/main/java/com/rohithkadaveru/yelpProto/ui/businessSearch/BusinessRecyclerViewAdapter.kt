package com.rohithkadaveru.yelpProto.ui.businessSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohithkadaveru.yelpProto.R
import com.rohithkadaveru.yelpProto.databinding.BusinessListItemBinding
import com.rohithkadaveru.yelpProto.domain.model.Business

/**
 * [RecyclerView.Adapter] that can display a [Business].
 *
 * @param onClick clickListener to react when a user taps on [Business] item
 */
class BusinessRecyclerViewAdapter(private val onClick: (Business) -> Unit) :
    ListAdapter<Business, BusinessRecyclerViewAdapter.ViewHolder>(
        BusinessDiffCallback
    ) {
    private var businessList: List<Business>? = null

    inner class ViewHolder(
        itemBinding: BusinessListItemBinding,
        onClick: (Business) -> Unit
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        var currentBusiness: Business? = null
        private val binding = itemBinding

        init {
            itemBinding.root.setOnClickListener {
                currentBusiness?.let {
                    onClick(it)
                }
            }
        }

        /* Bind Business views. */
        fun bind(businessIndex: Int) {
            currentBusiness = businessList?.get(businessIndex)
            binding.nameLabel.text = currentBusiness?.name
            binding.addressLabel.text = currentBusiness?.address

            binding.ratingBar.rating = currentBusiness?.rating?.toFloat() ?: 0f
            binding.priceLabel.text = currentBusiness?.price

            Glide.with(binding.root.context)
                .load(currentBusiness?.imageUrl)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemBinding =
            BusinessListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = businessList?.size ?: 0

    /**
     * updates the list of business when we receive new data from API
     */
    fun updateBusinessList(list: List<Business>) {
        businessList = list
        notifyDataSetChanged()
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     **/
    object BusinessDiffCallback : DiffUtil.ItemCallback<Business>() {
        override fun areItemsTheSame(
            oldItem: Business,
            newItem: Business
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Business,
            newItem: Business
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
}