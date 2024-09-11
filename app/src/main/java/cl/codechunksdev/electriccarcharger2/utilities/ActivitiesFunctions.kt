package cl.codechunksdev.electriccarcharger2.utilities

import android.app.Activity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.codechunksdev.electriccarcharger2.domain.dto.SessionPref
import cl.codechunksdev.electriccarcharger2.ui.b_entry.login.ui.dataStore
import cl.codechunksdev.electriccarcharger2.utilities.Constants.KEY_IS_LOGGED
import cl.codechunksdev.electriccarcharger2.utilities.Constants.KEY_TOKEN
import kotlinx.coroutines.flow.map


fun getSessionData(activity: Activity) = activity.dataStore.data.map { preferences ->
    SessionPref(
        isLoged =  preferences[booleanPreferencesKey(KEY_IS_LOGGED)] ?: false,
        token = preferences[stringPreferencesKey(KEY_TOKEN)] ?: ""
    )
}

