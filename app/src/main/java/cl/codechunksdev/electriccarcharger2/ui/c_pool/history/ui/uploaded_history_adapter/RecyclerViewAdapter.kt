package cl.codechunksdev.electriccarcharger2.ui.c_pool.history.ui.uploaded_history_adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.databinding.ItemHistoryBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.history.History
import cl.codechunksdev.electriccarcharger2.ui.c_pool.history.service.transformTimeToLocalTime
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.service.getConnectorImage
import java.text.ParseException

internal class RecyclerViewAdapter(
   private val histories: List<History>,
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
      return ViewHolder(view)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val history: History = histories[position]
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         try {
            val fecha: String = transformTimeToLocalTime(history.getDate())
            holder.datetime.text = fecha
         } catch (e: ParseException) {
            throw RuntimeException(e)
         }
      }
      holder.poolName.setText(history.terminalName())
      holder.connectorName.setText(history.connectorName())
      holder.kwh.text = java.lang.String.format("%.2f", history.getKwh()) + " kWh"
      holder.timeelapse.text = java.lang.String.format("%.2f", history.tiempoCargado()) + " min"
      holder.image.setImageResource(getConnectorImage(history.connectorType()))
   }

   override fun getItemCount(): Int = histories.size

   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      var datetime: TextView
      var poolName: TextView
      var connectorName: TextView
      var kwh: TextView
      var timeelapse: TextView
      var image: ImageView
      private val binding = ItemHistoryBinding.bind(itemView)
      init {
         datetime = binding.historyItemDatetime  // itemView.findViewById<TextView>(R.id.history_item_datetime)
         poolName = binding.historyItemPoolName  // itemView.findViewById<TextView>(R.id.history_item_pool_name)
         connectorName = binding.historyItemConnectorName  // itemView.findViewById<TextView>(R.id.history_item_connector_name)
         kwh = binding.historyItemKwh  // itemView.findViewById<TextView>(R.id.history_item_kwh)
         timeelapse = binding.historyItemTimeslap  // itemView.findViewById<TextView>(R.id.history_item_timeslap)
         image = binding.historyItemImg  // itemView.findViewById<ImageView>(R.id.history_item_img)
      }
   }
}
