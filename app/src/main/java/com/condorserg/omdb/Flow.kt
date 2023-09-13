package com.condorserg.omdb

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textChangedFlow(): Flow<String> {
    return callbackFlow {
        val textChangeListener = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    trySendBlocking(s.toString())
                    Log.d("onTextChanged", "Text = $s")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        this@textChangedFlow.addTextChangedListener(textChangeListener)
        awaitClose {
            this@textChangedFlow.removeTextChangedListener(textChangeListener)
        }
    }
}

fun RadioGroup.checkRadioButtonFlow(): Flow<MovieType> {

    return callbackFlow {
        val checkedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = group.findViewById(checkedId)
            when (radioButton.id) {
                R.id.movieRadioButton -> {
                    trySendBlocking(MovieType.movie)
                    Log.d("checkRadioButton", "Movie type = ${MovieType.movie}")
                }
                R.id.seriesRadioButton -> {
                    trySendBlocking(MovieType.series)
                    Log.d("checkRadioButton", "Movie type = ${MovieType.series}")
                }
                R.id.episodeRadioButton -> {
                    trySendBlocking(MovieType.episode)
                    Log.d("checkRadioButton", "Movie type = ${MovieType.episode}")
                }
            }
        }
        setOnCheckedChangeListener(checkedChangeListener)
        awaitClose { setOnCheckedChangeListener(null) }
    }
}