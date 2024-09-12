package cl.codechunksdev.electriccarcharger2.utilities

import cl.codechunksdev.electriccarcharger2.BuildConfig


object Constants {

   const val URL_BASE = BuildConfig.URL_BASE
   const val URL_ENDPOINT = BuildConfig.URL_ENDPOINT
   const val COMPANY_ID = BuildConfig.COMPANY_ID
   const val TAG = "TAG------>";
   const val KEY_IS_LOGGED = "isLogin"
   const val KEY_TOKEN = "token"
   const val KEY_EMAIL = "email"
   const val DELAY_SPLASH: Long = 1_000
   const val DELAY_FLOW_CHARGE: Long = 1_000
   const val DELAY_FLOW_MAP: Long = 2_000
   const val ZOOM: Float = 15F
   const val CORRIENTE_MAX = 100
   const val USER_ID_FOR_CHARGE = 99_999

   //  ******
   // bundle from Map to PoolDetail
   const val POOL_ID = "pool_id"
   const val POOL_NAME: String = "pool_name"
   const val POOL_ADDRESS: String = "pool_address"
   const val LATITUDE: String = "latitude"
   const val LONGITUDE: String = "longitude"

   // bundle constants PoolDetail -> ConnectorDetail
   const val TITLE_NAME = "title_name"
   const val TITLE = "title"
   const val PISTOLA = "pistola"
   const val STATION_IDENTIFIER_TO_BODY = "station_name_to_body"
   const val STATION_NAME = "station_name"
   const val CHARGER_NAME = "charger_name"
   const val CONNECTOR_TYPE = "connector_type"
   const val CONNECTOR_ID = "connector_id"
   const val CONNECTOR = "connector"
   const val CONNECTOR_NAME = "connector_name"

   //   sub item connector status constants
   const val AVAILABLE = "Available"
   const val SUSPENDED_EVSE = "SuspendedEVSE"
   const val SUSPENDED_EV = "SuspendedEV"
   const val PREPARING = "Preparing"
   const val CHARGING = "Charging"
   const val FINISHING = "Finishing"
   const val PFINISHING = "PFinishing"
   const val RESERVED = "Reserved"
   const val FAULTED = "Faulted"
   const val UNAVAILABLE = "Unavailable"
   const val OFFLINE = "Offline"

}
