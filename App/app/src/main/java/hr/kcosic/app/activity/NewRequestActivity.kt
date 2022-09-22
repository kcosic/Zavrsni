package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import hr.kcosic.app.R
import hr.kcosic.app.adapter.TimeAdapter
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Location
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.listeners.RadioButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.internal.notifyAll
import java.io.IOException
import java.io.InvalidObjectException


class NewRequestActivity : ValidatedActivityWithNavigation(ActivityEnum.NEW_REQUEST) {

    private lateinit var rvRepairTime: RecyclerView
    private lateinit var timeAdapter: TimeAdapter
    private lateinit var selectedLocation: Location
    private lateinit var dateOfRepair: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_request)
        retrieveDataFromIntent()
        retrieveIssues()
        initializeComponents()
    }

    private fun retrieveDataFromIntent() {
        val stringMap = intent.getStringExtra(SearchActivity.NEW_REQUEST_KEY)
        val map = Helper.deserializeObject<Map<String, String>>(stringMap!!)
        selectedLocation = Helper.deserializeObject(map["selectedLocation"]!!)
        dateOfRepair = map["dateOfRepair"]!!
    }


    override fun initializeComponents() {
        rvRepairTime = findViewById(R.id.rvRepairTime)


    }

    private fun retrieveIssues() {
        apiService.retrieveShopAvailability(selectedLocation.Shops?.get(0)?.Id!!, dateOfRepair).enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveIssuesResponse(response)
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun handleRetrieveIssuesResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<String>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is ListResponse<*>) {

            try {
                @Suppress("UNCHECKED_CAST") val data = resp.Data as ArrayList<String>
                val radioButtonClickListener = object : RadioButtonClickListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onClick(s: String) {
                        // Notify adapter
                        rvRepairTime.post { timeAdapter.notifyDataSetChanged() }
                    }
                }
                timeAdapter = TimeAdapter(data, selectedLocation.Shops?.get(0)?.WorkHours!!, radioButtonClickListener)
                rvRepairTime.layoutManager = LinearLayoutManager(this)
                rvRepairTime.adapter = timeAdapter
                timeAdapter.notifyDataSetChanged()
            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }
}