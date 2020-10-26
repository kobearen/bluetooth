package com.example.bluetoothsample.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.bluetoothsample.Peripheral


class PeripheralListAdapter(private val context: Context) : BaseAdapter() {
    var peripherals: List<Peripheral> = emptyList()

    override fun getCount() = peripherals.size

    override fun getItem(position: Int) = peripherals[position]

    override fun getItemId(position: Int) = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) = null
        //((convertView as? PeripheralView) ?: PeripheralView(context)).apply {
          //  setPeripheral(peripherals[position])
        //}
}