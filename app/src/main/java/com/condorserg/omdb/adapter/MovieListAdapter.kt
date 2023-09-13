package com.condorserg.omdb.adapter

import androidx.recyclerview.widget.DiffUtil
import com.condorserg.omdb.RemoteMovie
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MovieListAdapter(
) : AsyncListDifferDelegationAdapter<RemoteMovie>(MovieDiffUtilCallBack()) {

    init {
        delegatesManager.addDelegate(MovieListAdapterDelegate())

    }

    class MovieDiffUtilCallBack: DiffUtil.ItemCallback<RemoteMovie>(){
        override fun areItemsTheSame(oldItem: RemoteMovie, newItem: RemoteMovie): Boolean {
            return oldItem == newItem
            }

        override fun areContentsTheSame(oldItem: RemoteMovie, newItem: RemoteMovie): Boolean {
            return oldItem == newItem
        }
    }

    }
