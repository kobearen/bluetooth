package com.example.bluetoothsample.extention

import android.widget.ListView
import androidx.databinding.BindingAdapter
import com.example.bluetoothsample.Peripheral
import com.example.bluetoothsample.adapter.PeripheralListAdapter


@BindingAdapter("bind:peripherals")
fun ListView.setPeripherals(peripheralList: MutableList<Peripheral>?) {
    peripheralList?.let { list ->
        (adapter as? PeripheralListAdapter)?.run {
            peripherals = list
            notifyDataSetChanged()
        }
    }
}
