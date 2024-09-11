package cl.codechunksdev.electriccarcharger2.ui.c_pool.history.service;

import static cl.codechunksdev.electriccarcharger2.utilities.Constants.URL_BASE;

import org.json.JSONArray;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class
UploadedHistoryFunctions {
   private UploadedHistoryFunctions() {
   }

   public static void connectToServerTEST(String url, JSONArray header, Callback callback) {
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder()
           .url(URL_BASE + url).get().build();
      client.newCall(request).enqueue(callback);
   }

}
