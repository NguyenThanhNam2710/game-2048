package com.gghez.game2048.inapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gghez.game2048.R
import com.gghez.game2048.inapp.PremiumPlan

class PremiumPlanAdapter(
    private val list: List<PremiumPlan>,
    private val onClick: (PremiumPlan) -> Unit
) : RecyclerView.Adapter<PremiumPlanAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.txtTitle)
        val price: TextView = v.findViewById(R.id.txtPrice)
        val best: TextView = v.findViewById(R.id.txtBest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_premium_plan, parent, false)
        return VH(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = list[pos]
        h.title.text = item.title
        h.price.text = item.price
        h.best.visibility = if (item.highlight) View.VISIBLE else View.GONE

        h.itemView.setOnClickListener { onClick(item) }
    }
}
