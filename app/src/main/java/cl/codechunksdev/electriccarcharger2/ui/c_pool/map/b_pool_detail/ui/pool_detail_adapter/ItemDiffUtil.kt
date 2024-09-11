package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui.pool_detail_adapter

import androidx.recyclerview.widget.DiffUtil
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailStation

class ItemDiffUtil(
   private val oldList: List<PoolDetailStation>,
   private val newList: List<PoolDetailStation>,
) : DiffUtil.Callback() {
   
   override fun getOldListSize(): Int = oldList.size
   
   override fun getNewListSize(): Int = newList.size
   
   override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition].id == newList[newItemPosition].id
   }
   
   override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition] == newList[newItemPosition]
   }
}