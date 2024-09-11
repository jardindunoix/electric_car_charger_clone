package cl.codechunksdev.electriccarcharger2.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.codechunksdev.electriccarcharger2.ui.b_entry.login.ui.dataStore
import cl.codechunksdev.electriccarcharger2.ui.c_pool.activity.PoolFlowActivity
import cl.codechunksdev.electriccarcharger2.utilities.Constants.KEY_IS_LOGGED
import cl.codechunksdev.electriccarcharger2.utilities.Constants.KEY_TOKEN

suspend fun saveValues(isLoged: Boolean, token: String, context: Context) {
    val pref = context.dataStore
    pref.edit { preferences ->
        preferences[booleanPreferencesKey(KEY_IS_LOGGED)] = isLoged
        preferences[stringPreferencesKey(KEY_TOKEN)] = token
    }
}


fun gotoPoolFlowActivity(activity: Activity, isSkipped: Boolean) {
    val intent = Intent(activity, PoolFlowActivity::class.java)
    activity.startActivity(intent)
    if (!isSkipped) activity.finish()
}


