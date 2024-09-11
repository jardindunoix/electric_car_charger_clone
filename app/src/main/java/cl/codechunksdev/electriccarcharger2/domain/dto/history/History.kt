package cl.codechunksdev.electriccarcharger2.domain.dto.history

class History(
   private val date: String? = null,
   private val terminalName: String? = null,
   private val connectorName: String? = null,
   private val connectorType: String? = null,
   private val tiempoCargado: Double = 0.0,
   private val firstVal: Double = 0.0,
   private val secondVal: Double = 0.0,
){
   fun getKwh(): Double {
      return secondVal - firstVal
   }
   fun getDate(): String? {
      return date
   }

   fun terminalName(): String? {
      return terminalName
   }

   fun connectorName(): String? {
      return connectorName
   }
   fun tiempoCargado(): Double {
      return tiempoCargado
   }

   fun connectorType(): String? {
      return connectorType
   }
}