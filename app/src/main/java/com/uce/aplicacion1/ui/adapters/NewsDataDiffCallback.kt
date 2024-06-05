package com.uce.aplicacion1.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.uce.aplicacion1.ui.entites.NewsDataUI

class NewsDataDiffCallback : DiffUtil.ItemCallback<NewsDataUI>() {
    override fun areItemsTheSame(oldItem: NewsDataUI, newItem: NewsDataUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NewsDataUI, newItem: NewsDataUI): Boolean {
        return oldItem == newItem
    }
}
