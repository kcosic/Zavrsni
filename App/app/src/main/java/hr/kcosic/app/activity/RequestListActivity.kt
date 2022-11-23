package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
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

class RequestListActivity : ValidatedActivityWithNavigation(ActivityEnum.REQUEST_LIST) {
    companion object{
        const val REQUEST_VIEW_KEY = "9sj0dfF3F8lsd7"
    }
    lateinit var rvRequests: RecyclerView
    private lateinit var requestsAdapter: RequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        retrieveRequests()
    }

    override fun initializeComponents() {
        rvRequests = findViewById(R.id.rvRequests)
    }

    private fun retrieveRequests() {
        showSpinner()
        apiService.retrieveUserRequests().enqueue((object :
            Callback {
            val mainHandler = Handler(applicationContext.mainLooper)

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
                    override fun onClick(data: Request) {
                        // Notify adapter
                        rvRequests.post {
                            handleMenuButtonClick(data)
                        }
                    }
                }
                requestsAdapter = RequestsAdapter(
                    data,
                    menuClickListener
                )
                rvRequests.layoutManager = LinearLayoutManager(this)
                rvRequests.adapter = requestsAdapter
                requestsAdapter.notifyDataSetChanged()

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

    private fun navigateToSingleRequestActivity(request:Request) {
        val baggage: MutableMap<String, String> = mutableMapOf()
        baggage["requestId"] = request.Id.toString()
        Helper.openActivity(this, ActivityEnum.REQUEST_VIEW, baggage, REQUEST_VIEW_KEY)
    }
}