package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
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
import hr.kcosic.app.model.enums.ErrorCodeEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.helpers.IconHelper
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

class HomeShopActivity : ValidatedActivityWithNavigation(ActivityEnum.HOME_SHOP) {
    companion object{
        const val REQUEST_REDIRECT = "7hf3asd9078Fa9sdf"
    }
    private lateinit var notificationsCard : CardView
    private lateinit var llNotificationsSection : LinearLayout
    private lateinit var llNotificationsContent : LinearLayout
    private lateinit var ivNotificationsExpand : ImageView
    private lateinit var ivNotificationsCollapse : ImageView
    private lateinit var tvNewRequestsBadge : TextView
    private lateinit var tvNewRequests : TextView
    private lateinit var tvUpdatedRequestsBadge : TextView
    private lateinit var tvUpdatedRequests : TextView

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
    private lateinit var notificationData: NotificationData

    private var loadedNotifications: Boolean = false;
    private var loadedCurrentRepair: Boolean = false;
    private var loadedUpcomingRepair: Boolean = false;
    private var loadedReviews: Boolean = false;

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
        tvNewRequests = findViewById(R.id.tvNewRequests)
        tvUpdatedRequestsBadge = findViewById(R.id.tvUpdatedRequestsBadge)
        tvUpdatedRequests = findViewById(R.id.tvUpdatedRequests)

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
                showComponent(llContent)
                showComponent(ivCollapse)
                hideComponent(ivExpand)
            }
            ivCollapse.visibility == View.VISIBLE -> {
                hideComponent(llContent)
                hideComponent(ivCollapse)
                showComponent(ivExpand)
            }
        }
    }

    private fun navigateToRequest(request: Request?){
        if(request == null){
            return
        }
        navigateToRequest(request.Id.toString())
    }

    private fun navigateToRequest(requestId: String){
        if(requestId.isBlank()){
            return
        }

        val baggage: MutableMap<String, String> = mutableMapOf()
        baggage["requestId"] = requestId
        Helper.openActivity(this, ActivityEnum.REPAIR_VIEW, baggage, RepairActivity.REQUEST_ID_KEY)
    }


    //#endregion Init

    //#region Retrieve Current Request
    private fun retrieveCurrentRequest(){
        showSpinner()
        apiService.retrieveCurrentRequest().enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call,e)
                    loadedCurrentRepair = true
                    hideSpinner()
                    hideComponent(upcomingRepairCard)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveCurrentRequestResponse(response)
                    loadedCurrentRepair = true
                    hideSpinner()
                }
            }
        })
    }

    private fun handleRetrieveCurrentRequestResponse(response: Response){
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess == true && resp is SingleResponse<*>) {
            try {
                showComponent(currentRepairCard)

                currentRequest = resp.Data as Request

                val calendar = Calendar.getInstance()
                calendar.time = currentRequest!!.RequestDate!!
                calendar.add(Calendar.HOUR, currentRequest!!.EstimatedRepairHours!!)

                tvCurrentVehicle.text = currentRequest!!.Car.toString()
                tvDeadline.text = Helper.formatDate(calendar.time)

            } catch (e: Exception) {
                hideComponent(currentRepairCard)
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            if((resp as ErrorResponse).ErrorCode != ErrorCodeEnum.RecordNotFound){
                handleApiResponseError(resp )
            }
            hideComponent(currentRepairCard)
        }
    }
    //#endregion Retrieve Current Request

    //#region Retrieve Upcoming Request
    private fun retrieveUpcomingRequest(){
        showSpinner()
        apiService.retrieveUpcomingRequest().enqueue(object : Callback{
            

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call,e)
                    loadedUpcomingRepair = true
                    hideSpinner()
                    hideComponent(upcomingRepairCard)

                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveUpcomingRequestResponse(response)
                    loadedUpcomingRepair = true
                    hideSpinner()

                }
            }
        })
    }

    private fun handleRetrieveUpcomingRequestResponse(response: Response){
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                showComponent(upcomingRepairCard)
                upcomingRequest = resp.Data as Request
                tvUpcomingVehicle.text = upcomingRequest!!.Car.toString()
                tvStartDateTime.text = Helper.formatDate(upcomingRequest!!.RequestDate!!)
            } catch (e: Exception) {
                hideComponent(upcomingRepairCard)
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            if((resp as ErrorResponse).ErrorCode != ErrorCodeEnum.RecordNotFound){
                handleApiResponseError(resp)
            }
            hideComponent(upcomingRepairCard)
        }
    }
    //#endregion Retrieve Upcoming Request

    //#region Retrieve Notification Data
    private fun retrieveNotificationData(){
        showSpinner()
        apiService.retrieveShopNotificationData().enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call,e)
                    loadedNotifications = true
                    hideSpinner()
                    hideComponent(notificationsCard)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveShopNotificationData(response)
                    loadedNotifications = true
                    hideSpinner()

                }
            }
        })
    }

    private fun handleRetrieveShopNotificationData(response: Response){
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<NotificationData>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                notificationData = resp.Data as NotificationData

                if(notificationData.NewRequests!!.size > 0){
                    tvNewRequestsBadge.text = notificationData.NewRequests!!.size.toString()
                    showComponent(tvNewRequestsBadge)
                    tvNewRequests.text = getString(R.string.new_requests)
                    tvNewRequests.setCompoundDrawables(null, null, IconHelper.loadVectorDrawableWithTint(R.drawable.chevron_right, R.color.primary_500), null)
                    tvNewRequests.setOnClickListener {
                        navigateToRequest(notificationData.NewRequests!![0].toString())
                    }
                }else {
                    hideComponent(tvNewRequestsBadge)
                    tvNewRequests.text = getString(R.string.no_new_requests)
                    tvNewRequests.setCompoundDrawables(null, null, null, null)
                    tvNewRequests.setOnClickListener{}
                }

                if(notificationData.UpdatedRequests!!.size > 0){
                    tvUpdatedRequestsBadge.text = notificationData.UpdatedRequests!!.size.toString()
                    showComponent(tvUpdatedRequestsBadge)
                    tvUpdatedRequests.text = getString(R.string.updated_requests)
                    tvUpdatedRequests.setCompoundDrawables(null, null, IconHelper.loadVectorDrawableWithTint(R.drawable.chevron_right, R.color.primary_500), null)
                    tvUpdatedRequests.setOnClickListener {
                        navigateToRequest(notificationData.UpdatedRequests!![0].toString())
                    }
                }else {
                    tvUpdatedRequests.text = getString(R.string.no_updated_requests)
                    hideComponent(tvUpdatedRequestsBadge)
                    tvUpdatedRequests.setCompoundDrawables(null, null, null, null)
                    tvUpdatedRequestsBadge.setOnClickListener{}
                }
            } catch (e: Exception) {
                hideComponent(notificationsCard)
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
            hideComponent(notificationsCard)

        }
    }
    //#endregion Retrieve Notification Data

    //#region Retrieve Reviews
    private fun retrieveRecentReviews(){
        showSpinner()
        apiService.retrieveShopRecentReviews().enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call,e)
                    loadedReviews = true
                    hideSpinner()
                    hideComponent(rvReviews)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveRecentReviewsResponse(response)
                    loadedReviews = true
                    hideSpinner()
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleRetrieveRecentReviewsResponse(response: Response){
        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<Review>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is ListResponse<*>) {
            try {
                @Suppress("UNCHECKED_CAST")
                val data = resp.Data as List<Review>
                showComponent(rvReviews)

                reviewsAdapter = ReviewsAdapter(data)
                rvReviews.layoutManager = LinearLayoutManager(this)
                rvReviews.adapter = reviewsAdapter
                reviewsAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                hideComponent(rvReviews)
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            hideComponent(rvReviews)
            handleApiResponseError(resp as ErrorResponse)
        }
    }
    //#endregion Retrieve Reviews

    override fun hideSpinner(){
        if(loadedReviews && loadedNotifications && loadedCurrentRepair && loadedUpcomingRepair){
            super.hideSpinner()
        }
    }
}