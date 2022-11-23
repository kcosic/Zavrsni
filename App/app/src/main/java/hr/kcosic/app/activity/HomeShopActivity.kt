package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.adapter.ReviewsAdapter
import hr.kcosic.app.model.NotificationData
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Request
import hr.kcosic.app.model.entities.Review
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

class HomeShopActivity : ValidatedActivityWithNavigation(ActivityEnum.HOME_SHOP) {

    private lateinit var notificationsCard : CardView
    private lateinit var llNotificationsSection : LinearLayout
    private lateinit var llNotificationsContent : LinearLayout
    private lateinit var ivNotificationsExpand : ImageView
    private lateinit var ivNotificationsCollapse : ImageView
    private lateinit var tvNewRequestsBadge : TextView
    private lateinit var tvUpdatedRequestsBadge : TextView

    private lateinit var currentRepairCard : CardView
    private lateinit var llCurrentRepairSection : LinearLayout
    private lateinit var tvCurrentVehicle : TextView
    private lateinit var tvDeadline : TextView
    private var currentRequest: Request? = null

    private lateinit var upcomingRepairCard : CardView
    private lateinit var llUpcomingRepairSection : LinearLayout
    private lateinit var tvUpcomingVehicle : TextView
    private lateinit var tvStartDateTime : TextView
    private var upcomingRequest: Request? = null


    private lateinit var rvReviews : RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter

    //#region Init
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        retrieveCurrentRequest()
        retrieveNotificationData()
        retrieveUpcomingRequest()
        retrieveRecentReviews()
    }
    override fun initializeComponents() {
        initializeNotificationsSection()
        initializeCurrentRepairSection()
        initializeUpcomingRepairSection()
        initializeReviewsSection()
    }

    private fun initializeNotificationsSection(){
        notificationsCard = findViewById(R.id.notificationsCard)
        llNotificationsSection = findViewById(R.id.llNotificationsSection)
        llNotificationsContent = findViewById(R.id.llNotificationsContent)
        ivNotificationsExpand = findViewById(R.id.ivNotificationsExpand)
        ivNotificationsCollapse = findViewById(R.id.ivNotificationsCollapse)
        tvNewRequestsBadge = findViewById(R.id.tvNewRequestsBadge)
        tvUpdatedRequestsBadge = findViewById(R.id.tvUpdatedRequestsBadge)

        llNotificationsSection.setOnClickListener {
            toggleHeader(
                llNotificationsContent,
                ivNotificationsExpand,
                ivNotificationsCollapse,
            )
        }
    }
    private fun initializeCurrentRepairSection(){
        currentRepairCard = findViewById(R.id.currentRepairCard)
        llCurrentRepairSection = findViewById(R.id.llCurrentRepairSection)
        tvCurrentVehicle = findViewById(R.id.tvCurrentVehicle)
        tvDeadline = findViewById(R.id.tvDeadline)

        llCurrentRepairSection.setOnClickListener {
            navigateToRequest(currentRequest)
        }
    }

    private fun initializeUpcomingRepairSection(){
        upcomingRepairCard = findViewById(R.id.upcomingRepairCard)
        llUpcomingRepairSection = findViewById(R.id.llUpcomingRepairSection)
        tvUpcomingVehicle = findViewById(R.id.tvUpcomingVehicle)
        tvStartDateTime = findViewById(R.id.tvStartDateTime)
        llUpcomingRepairSection.setOnClickListener {
            navigateToRequest(upcomingRequest)
        }
    }

    private fun initializeReviewsSection(){
        rvReviews = findViewById(R.id.rvReviews)
    }

    private fun toggleHeader(
        llContent: LinearLayout,
        ivExpand: ImageView,
        ivCollapse: ImageView,
    ) {
        when {
            ivExpand.visibility == View.VISIBLE -> {
                ivExpand.visibility = View.GONE
                ivCollapse.visibility = View.VISIBLE
                llContent.visibility = View.VISIBLE
            }
            ivCollapse.visibility == View.VISIBLE -> {
                ivExpand.visibility = View.VISIBLE
                ivCollapse.visibility = View.GONE
                llContent.visibility = View.GONE
            }
        }
    }

    private fun navigateToRequest(request: Request?){
        if(request == null){
            return
        }

        //TODO: implement redirecting to individual requests
    }


    //#endregion Init

    //#region Retrieve Current Request
    private fun retrieveCurrentRequest(){
        apiService.retrieveCurrentRequest().enqueue(object : Callback{
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call,e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveCurrentRequestResponse(response)
                }
            }
        })
    }

    private fun handleRetrieveCurrentRequestResponse(response: Response){
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                currentRequest = resp.Data as Request

                val calendar = Calendar.getInstance()
                calendar.time = currentRequest!!.RequestDate!!
                calendar.add(Calendar.HOUR, currentRequest!!.EstimatedRepairHours!!)

                tvCurrentVehicle.text = currentRequest!!.Car.toString()
                tvDeadline.text = Helper.formatDate(calendar.time)

            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)

        }
    }
    //#endregion Retrieve Current Request

    //#region Retrieve Upcoming Request
    private fun retrieveUpcomingRequest(){
        apiService.retrieveUpcomingRequest().enqueue(object : Callback{
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call,e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveUpcomingRequestResponse(response)
                }
            }
        })
    }

    private fun handleRetrieveUpcomingRequestResponse(response: Response){
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                upcomingRequest = resp.Data as Request
                tvCurrentVehicle.text = upcomingRequest!!.Car.toString()
                tvStartDateTime.text = Helper.formatDate(upcomingRequest!!.RequestDate!!)
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }
    //#endregion Retrieve Upcoming Request

    //#region Retrieve Notification Data
    private fun retrieveNotificationData(){
        apiService.retrieveShopNotificationData().enqueue(object : Callback{
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call,e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveShopNotificationData(response)
                }
            }
        })
    }

    private fun handleRetrieveShopNotificationData(response: Response){
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<NotificationData>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                val data = resp.Data as NotificationData

                if(data.NewRequests!! > 0){
                    tvNewRequestsBadge.text = data.NewRequests!!.toString()
                    tvNewRequestsBadge.visibility = View.VISIBLE
                }

                if(data.UpdatedRequests!! > 0){
                    tvUpdatedRequestsBadge.text = data.UpdatedRequests!!.toString()
                    tvUpdatedRequestsBadge.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)

        }
    }
    //#endregion Retrieve Notification Data

    //#region Retrieve Reviews
    private fun retrieveRecentReviews(){
        apiService.retrieveShopRecentReviews().enqueue(object : Callback{
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call,e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveRecentReviewsResponse(response)
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleRetrieveRecentReviewsResponse(response: Response){
        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<Review>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                @Suppress("UNCHECKED_CAST")
                val data = resp.Data as List<Review>


                reviewsAdapter = ReviewsAdapter(data)
                rvReviews.layoutManager = LinearLayoutManager(this)
                rvReviews.adapter = reviewsAdapter
                reviewsAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }
    //#endregion Retrieve Reviews
}