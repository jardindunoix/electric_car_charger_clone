package cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail

class PoolDetailConnector(val id: String, val name: String, val type: String, val type_alias: String, val status: String) {

   val isAvailable: Boolean
      get() =
         status == "Available" ||
         status == "SuspendedEVSE" ||
         status == "SuspendedEV" ||
         status == "Preparing"
   val isOffline: Boolean
      get() = status == "Offline"
}