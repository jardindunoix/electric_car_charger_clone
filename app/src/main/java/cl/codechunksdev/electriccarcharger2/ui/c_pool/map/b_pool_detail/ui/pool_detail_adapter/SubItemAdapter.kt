package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui.pool_detail_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.databinding.ItemPoolDetailConnectorBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailConnector
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.service.getChargerImage
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.service.getConnectorImage
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.service.getConnectorName
import cl.codechunksdev.electriccarcharger2.utilities.Constants.AVAILABLE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CHARGER_NAME
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CHARGING
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CONNECTOR
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CONNECTOR_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CONNECTOR_NAME
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CONNECTOR_TYPE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.FAULTED
import cl.codechunksdev.electriccarcharger2.utilities.Constants.FINISHING
import cl.codechunksdev.electriccarcharger2.utilities.Constants.LATITUDE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.LONGITUDE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.OFFLINE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.PFINISHING
import cl.codechunksdev.electriccarcharger2.utilities.Constants.PISTOLA
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.PREPARING
import cl.codechunksdev.electriccarcharger2.utilities.Constants.RESERVED
import cl.codechunksdev.electriccarcharger2.utilities.Constants.STATION_IDENTIFIER_TO_BODY
import cl.codechunksdev.electriccarcharger2.utilities.Constants.STATION_NAME
import cl.codechunksdev.electriccarcharger2.utilities.Constants.SUSPENDED_EV
import cl.codechunksdev.electriccarcharger2.utilities.Constants.SUSPENDED_EVSE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.TITLE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.TITLE_NAME
import cl.codechunksdev.electriccarcharger2.utilities.Constants.UNAVAILABLE

internal class SubItemAdapter(
    private val subItemList: List<PoolDetailConnector>?,
    private val chargerName: String?,
    private val poolId: String?,
    private val poolName: String?,
    private val poolAddress: String?,
    private val latitudArg: String?,
    private val longitudArg: String?,
    private val stationName: String?,
    val context: Context
) : RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SubItemViewHolder {
        return SubItemViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_pool_detail_connector, viewGroup, false))
    }

    override fun onBindViewHolder(subItemViewHolder: SubItemViewHolder, i: Int) {
        val subItem = subItemList!![i]
        subItemViewHolder.tvSubItemName.text = getConnectorName(subItem.name, subItem.type_alias)
        subItemViewHolder.tvSubItemAvailable.text = subItem.status
        subItemViewHolder.connectorImage.setImageResource(getConnectorImage(subItem.type))
        when (subItem.status) {
            AVAILABLE, SUSPENDED_EVSE, SUSPENDED_EV -> {
                subItemViewHolder.tvSubItemAvailable.setText(R.string.connector_status_available_suspendedevse_suspendedev)
                subItemViewHolder.tvSubItemAvailable.setTextColor(ContextCompat.getColor(context, R.color.subitem_status_available))
                subItemViewHolder.chargerImage.setImageResource(getChargerImage("Available"))
            }

            PREPARING -> {
                subItemViewHolder.tvSubItemAvailable.setText(R.string.connector_status_preparing)
                subItemViewHolder.tvSubItemAvailable.setTextColor(ContextCompat.getColor(context, R.color.subitem_status_connected))
                subItemViewHolder.chargerImage.setImageResource(getChargerImage("Connected"))
            }

            CHARGING, FINISHING, PFINISHING, RESERVED -> {
                subItemViewHolder.tvSubItemAvailable.setText(R.string.connector_status_charging_finishing_pfinishing_reserved)
                subItemViewHolder.tvSubItemAvailable.setTextColor(ContextCompat.getColor(context, R.color.subitem_status_busy))
                subItemViewHolder.chargerImage.setImageResource(getChargerImage())
            }

            FAULTED, UNAVAILABLE, OFFLINE -> {
                subItemViewHolder.tvSubItemAvailable.setText(R.string.connector_status_faulted_uavailable_offline)
                subItemViewHolder.tvSubItemAvailable.setTextColor(ContextCompat.getColor(context, R.color.subitem_status_not_available))
                subItemViewHolder.chargerImage.setImageResource(getChargerImage())
            }

            else -> {
                subItemViewHolder.tvSubItemAvailable.setText(R.string.connector_status_default)
                subItemViewHolder.tvSubItemAvailable.setTextColor(ContextCompat.getColor(context, R.color.subitem_status_default))
                subItemViewHolder.chargerImage.setImageResource(getChargerImage())
            }
        }
        subItemViewHolder.layout.setOnClickListener { view: View ->
            val bundle = bundleOf(
                CHARGER_NAME to chargerName,
                POOL_ID to poolId,
                TITLE_NAME to poolName,
                TITLE to poolAddress,
                STATION_NAME to subItem.type_alias,
                STATION_IDENTIFIER_TO_BODY to stationName,
                CONNECTOR_NAME to "Charger 2",
                CONNECTOR to subItem.name,
                CONNECTOR_ID to subItem.id,
                CONNECTOR_TYPE to subItem.type,
                LATITUDE to latitudArg,
                LONGITUDE to longitudArg,
                PISTOLA to 1,
            )
            Navigation.findNavController(view).navigate(R.id.action_pool_detail_fragment_to_connector_detail_fragment, bundle);
        }
    }

    override fun getItemCount(): Int = subItemList?.size ?: 0

    internal inner class SubItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvSubItemName: TextView
        var tvSubItemAvailable: TextView
        var connectorImage: ImageView
        var chargerImage: ImageView
        var layout: ConstraintLayout
        private val binding = ItemPoolDetailConnectorBinding.bind(itemView)

        init {
            tvSubItemName = binding.connectorName
            tvSubItemAvailable = binding.connectorAvailabilityStatus
            connectorImage = binding.connectorImg
            chargerImage = binding.toCharge
            layout = binding.connectorLayout
        }
    }
}