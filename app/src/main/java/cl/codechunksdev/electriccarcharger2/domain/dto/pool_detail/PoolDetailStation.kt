package cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.Marker

open class PoolDetailStation : Parcelable {

   val id: String?
   val lineas_id: Int
   val name: String?
   val alias: String?
   val status: Int
   val identifier: Long
   var connectors: List<PoolDetailConnector>? = null
   private var marker: Marker? = null

   constructor(id: String?, lineas_id: Int, name: String?, alias: String?, status: Int, identifier: Long) {
      this.id = id
      this.lineas_id = lineas_id
      this.name = name
      this.alias = alias
      this.status = status
      this.identifier = identifier
      connectors = ArrayList()
   }

   protected constructor(parcel: Parcel) {
      id = parcel.readString()
      lineas_id = parcel.readInt()
      name = parcel.readString()
      alias = parcel.readString()
      status = parcel.readInt()
      identifier = parcel.readInt().toLong()
   }

   fun setMarker(marker: Marker?) {
      this.marker = marker
   }

   fun getConnectorsLen(): Int {
      return connectors!!.size
   }

   fun connectorsAvailables(): Int {
      var count = 0
      for (i in connectors!!.indices) {
         if (connectors!![i].isAvailable) count++
      }
      return count
   }

   fun isStationOffline(): Boolean {
      var count = 0
      for (i in connectors!!.indices) {
         if (connectors!![i].isOffline) count++
      }
      return count == connectors!!.size
   }

   override fun describeContents(): Int {
      return 0
   }

   override fun writeToParcel(dest: Parcel, flags: Int) {
      dest.writeString(id)
      dest.writeInt(lineas_id)
      dest.writeString(name)
      dest.writeString(alias)
      dest.writeInt(status)
      dest.writeLong(identifier)
   }

   companion object CREATOR : Parcelable.Creator<PoolDetailStation> {

      override fun createFromParcel(parcel: Parcel): PoolDetailStation {
         return PoolDetailStation(parcel)
      }

      override fun newArray(size: Int): Array<PoolDetailStation?> {
         return arrayOfNulls(size)
      }
   }

}