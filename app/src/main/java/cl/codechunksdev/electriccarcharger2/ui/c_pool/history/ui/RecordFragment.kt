package cl.codechunksdev.electriccarcharger2.ui.c_pool.history.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.codechunksdev.electriccarcharger2.databinding.FragmentRecordBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.history.History
import cl.codechunksdev.electriccarcharger2.ui.c_pool.history.service.UploadedHistoryFunctions
import cl.codechunksdev.electriccarcharger2.ui.c_pool.history.ui.uploaded_history_adapter.RecyclerViewAdapter
import cl.codechunksdev.electriccarcharger2.ui.c_pool.service.bottomMenuFunc
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class RecordFragment : Fragment() {
   private var recycler: RecyclerView? = null
   private var histories: ArrayList<History>? = null
   private var adapter: RecyclerViewAdapter? = null
   private var _binding: FragmentRecordBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
      _binding = FragmentRecordBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      histories = ArrayList()
      initComponents()
      bottomMenuFunc(requireActivity(), this) { findNavController() }
      val c: Callback = object : Callback {
         override fun onFailure(call: Call, e: IOException) {}

         @Throws(IOException::class)
         override fun onResponse(call: Call, response: Response) {
            val str = response.body!!.string()
            if (response.code == 200) {
               try {
                  val results = JSONArray(str)
                  for (i in 0 until results.length()) {
                     val result = results.getJSONObject(i)
                     val hc = History(
                        result.getString("date_init"),
                        result.getString("location"),
                        result.getString("connector_alias"),
                        result.getString("connector_type"),
                        result.getDouble("minutes"),
                        result.getDouble("first_val"),
                        result.getDouble("second_val")
                     )
                     histories!!.add(hc)
                  }
                  requireActivity().runOnUiThread(Runnable { recycler!!.adapter = adapter })
               } catch (e: JSONException) {
                  throw RuntimeException(e)
               }
            }
         }
      }
      /** TODO QUE PASA CON EL USUARIO 99999 **/
      UploadedHistoryFunctions.connectToServerTEST("pools/history/99999", null, c)
      adapter = RecyclerViewAdapter(histories!!)
   }

   fun newInstance(): RecordFragment {
      return RecordFragment()
   }

   private fun initComponents() {
      recycler = binding.historialRecycler
      recycler!!.layoutManager = LinearLayoutManager(requireContext())
   }
}