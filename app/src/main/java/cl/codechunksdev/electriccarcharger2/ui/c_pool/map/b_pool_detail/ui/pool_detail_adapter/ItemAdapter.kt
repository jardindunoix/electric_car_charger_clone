package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui.pool_detail_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.databinding.ItemPoolDetailStationBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailStation

internal class ItemAdapter(
   private var stationList: List<PoolDetailStation>,
   private val poolId: String?,
   private val poolName: String?,
   private val poolAddress: String?,
   private val latitudArg: String?,
   private val longitudArg: String?,
   val context: Context,
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

   private val viewPool = RecycledViewPool()

   fun udpateList(newlist: List<PoolDetailStation>) {
      val itemDiff = ItemDiffUtil(
         stationList,
         newlist
      )
      val result = DiffUtil.calculateDiff(itemDiff)
      stationList = newlist
      result.dispatchUpdatesTo(this)
   }

   override fun onCreateViewHolder(
      viewGroup: ViewGroup,
      i: Int
   ): ItemViewHolder {
      val view: View = LayoutInflater
         .from(viewGroup.context)
         .inflate(
            R.layout.item_pool_detail_station,
            viewGroup,
            false
         )
      return ItemViewHolder(view)
   }

   override fun onBindViewHolder(
      itemViewHolder: ItemViewHolder,
      i: Int
   ) {
      val station: PoolDetailStation = stationList[i]
      val stationName: String? = if (stationList[i].alias == "") stationList[i].name
      else stationList[i].alias
      itemViewHolder.apply {
         tvItemTitle.text = stationName
         val layoutManager = LinearLayoutManager(
            rvSubItem.context,
            LinearLayoutManager.VERTICAL,
            false
         )
         layoutManager.initialPrefetchItemCount = if (station.connectors != null) station.connectors!!.size else 0
         val subItemAdapter = SubItemAdapter(
            station.connectors,
            stationName,
            poolId,
            poolName,
            poolAddress,
            latitudArg,
            longitudArg,
            station.name,
            context
         )
         rvSubItem.layoutManager = layoutManager
         rvSubItem.adapter = subItemAdapter
         rvSubItem.setRecycledViewPool(viewPool)
      }

   }

   override fun getItemCount(): Int = stationList.size

   internal inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

      val tvItemTitle: TextView
      val rvSubItem: RecyclerView
      private val binding = ItemPoolDetailStationBinding.bind(itemView)

      init {
         tvItemTitle = binding.textviewStationTitle
         rvSubItem = binding.recyclerConnectors
      }
   }
}