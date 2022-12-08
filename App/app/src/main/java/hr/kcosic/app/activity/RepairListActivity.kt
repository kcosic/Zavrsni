package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.adapter.RepairsAdapter
import hr.kcosic.app.adapter.RequestsAdapter
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Request
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.listeners.ButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class RepairListActivity : ValidatedActivityWithNavigation(ActivityEnum.REPAIR_LIST_VIEW) {
    lateinit var rvRepairs: RecyclerView
    private lateinit var repairsAdapter: RepairsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        retrieveRequests()
    }

    override fun initializeComponents() {
        rvRepairs = findViewById(R.id.rvRepairs)
    }

    private fun retrieveRequests() {
        showSpinner()
        apiService.retrieveRequests().enqueue((object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveRequestsResponse(response)
                    hideSpinner()
                }
            }
        }))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun handleRetrieveRequestsResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is ListResponse<*>) {
            try {
                @Suppress("UNCHECKED_CAST") val data = resp.Data as MutableList<Request>

                val menuClickListener = object : ButtonClickListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onClick(o: Request) {
                        // Notify adapter
                        rvRepairs.post {
                            handleMenuButtonClick(o)
                        }
                    }
                }
                repairsAdapter = RepairsAdapter(
                    data,
                    menuClickListener
                )
                rvRepairs.layoutManager = LinearLayoutManager(this)
                rvRepairs.adapter = repairsAdapter
                repairsAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)

        }
    }

    private fun handleMenuButtonClick(request: Request) {
        navigateToSingleRequestActivity(request)
    }

    private fun navigateToSingleRequestActivity(request: Request) {
        val baggage: MutableMap<String, String> = mutableMapOf()
        baggage["requestId"] = request.Id.toString()
        Helper.openActivity(this, ActivityEnum.REPAIR_VIEW, baggage, RepairActivity.REQUEST_ID_KEY)
    }
}