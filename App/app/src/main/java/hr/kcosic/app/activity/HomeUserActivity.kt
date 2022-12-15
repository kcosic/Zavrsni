package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hr.kcosic.app.R
import hr.kcosic.app.adapter.UserIssuesAdapter
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Issue
import hr.kcosic.app.model.entities.Request
import hr.kcosic.app.model.entities.Review
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.helpers.ValidationHelper
import hr.kcosic.app.model.listeners.ButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

class HomeUserActivity : ValidatedActivityWithNavigation(ActivityEnum.HOME_USER),
    OnMapReadyCallback {
    private lateinit var homeContentView: ScrollView
    private lateinit var homeNoContentView: LinearLayout
    private lateinit var nextServiceDate: CardView
    private lateinit var nextServiceMap: CardView
    private lateinit var nextServiceDescription: CardView
    private lateinit var nextServiceDetails: CardView
    private lateinit var tvDateOfRepair: TextView
    private lateinit var tvTimeOfRepair: TextView
    private lateinit var tvRepairShopName: TextView
    private lateinit var tvRepairShopVat: TextView
    private lateinit var tvRequestDescription: TextView
    private lateinit var tvEstimatedTime: TextView
    private lateinit var tvEstimatedPrice: TextView

    private lateinit var llUserConsent: LinearLayout
    private lateinit var tvShopConsent: TextView
    private lateinit var llUserConsentChoice: LinearLayout
    private lateinit var btnAcceptRequest: Button
    private lateinit var btnDeclineRequest: Button
    private lateinit var btnSubmitReview: Button
    private lateinit var cvConsents: CardView
    private lateinit var cvReview: CardView
    private lateinit var rbRating: RatingBar
    private lateinit var etComment: EditText

    private lateinit var googleMap: GoogleMap
    private var request: Request? = null
    private var latestRequest: Request? = null
    private var mapServiceLocation: SupportMapFragment? = null

    private lateinit var cvIssues: CardView
    private lateinit var rvIssues: RecyclerView
    private lateinit var userIssuesAdapter: UserIssuesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        retrieveActiveUserRequest()
        retrieveLatestRequest()
    }

    override fun initializeComponents() {
        homeContentView = findViewById(R.id.homeContentView)
        homeNoContentView = findViewById(R.id.homeNoContentView)
        nextServiceDate = findViewById(R.id.nextServiceDate)
        nextServiceMap = findViewById(R.id.nextServiceMap)
        nextServiceDescription = findViewById(R.id.nextServiceDescription)
        nextServiceDetails = findViewById(R.id.nextServiceDetails)
        tvDateOfRepair = findViewById(R.id.tvDateOfRepair)
        tvTimeOfRepair = findViewById(R.id.tvTimeOfRepair)
        tvRepairShopName = findViewById(R.id.tvRepairShopName)
        tvRepairShopVat = findViewById(R.id.tvRepairShopVat)
        tvRequestDescription = findViewById(R.id.tvRequestDescription)
        tvEstimatedTime = findViewById(R.id.tvEstimatedTime)
        tvEstimatedPrice = findViewById(R.id.tvEstimatedPrice)
        llUserConsent = findViewById(R.id.llUserConsent)
        tvShopConsent = findViewById(R.id.tvShopConsent)
        llUserConsentChoice = findViewById(R.id.llUserConsentChoice)
        btnAcceptRequest = findViewById(R.id.btnAcceptRequest)
        btnDeclineRequest = findViewById(R.id.btnDeclineRequest)
        btnSubmitReview = findViewById(R.id.btnSubmitReview)
        cvConsents = findViewById(R.id.cvConsents)
        cvReview = findViewById(R.id.cvReview)
        rbRating = findViewById(R.id.rbRating)
        etComment = findViewById(R.id.etComment)
        rvIssues = findViewById(R.id.rvIssues)
        cvIssues = findViewById(R.id.cvIssues)

        hideComponent(cvReview)

        btnAcceptRequest.setOnClickListener {
            acceptRequest()
        }

        btnDeclineRequest.setOnClickListener {
            declineRequest()
        }

        rbRating.setOnRatingBarChangeListener { _, _, _ ->
            if (ValidationHelper.validateReview(rbRating, etComment)) {
                showComponent(btnSubmitReview)
            } else {
                hideComponent(btnSubmitReview)
            }
        }


        etComment.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (ValidationHelper.validateReview(rbRating, etComment)) {
                    showComponent(btnSubmitReview)
                } else {
                    hideComponent(btnSubmitReview)
                }
            }

        })

        btnSubmitReview.setOnClickListener {
            createReview()
        }
    }


    private fun createReview() {
        val review = Review()
        review.Comment = etComment.text.toString()
        review.Rating = rbRating.rating.toDouble()
        review.ShopId = latestRequest!!.ShopId
        review.UserId = latestRequest!!.UserId
        showSpinner()
        apiService.createReview(review, latestRequest!!.Id!!).enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    hideSpinner()
                    handleApiResponseException(call, e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleCreateReviewResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    private fun handleCreateReviewResponse(response: Response) {
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<BaseResponse>>(response.body!!.string())

        if (resp.IsSuccess!!) {
            try {
                hideComponent(cvReview)
                Helper.showLongToast(this, getString(R.string.review_submitted))
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }


    private fun retrieveLatestRequest() {
        showSpinner()
        apiService.retrieveLatestRequest().enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    hideSpinner()
                    hideComponent(cvReview)
                    handleApiResponseException(call, e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveLatestRequestResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    private fun handleRetrieveLatestRequestResponse(response: Response) {
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                latestRequest = resp.Data as Request
                showComponent(cvReview)
                hideComponent(btnSubmitReview)
            } catch (e: Exception) {
                hideComponent(cvReview)
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            hideComponent(cvReview)
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun declineRequest() {
        request!!.UserAccepted = false
        request!!.UserAcceptedDate = Calendar.getInstance().time
        updateRequest(request!!)
    }

    private fun acceptRequest() {
        request!!.UserAccepted = true
        request!!.UserAcceptedDate = Calendar.getInstance().time
        updateRequest(request!!)
    }
    //#region Update Request

    private fun updateRequest(request: Request) {
        showSpinner()
        apiService.updateRequest(request).enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    hideSpinner()
                    handleApiResponseException(call, e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleUpdateRequestResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    private fun handleUpdateRequestResponse(response: Response) {
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                request = resp.Data as Request
                populateViewWithData()
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    //#endregion Update Request
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val shopLocation = request?.Shop?.Location
        val myPosition = LatLng(shopLocation?.Latitude!!, shopLocation.Longitude!!)

        googleMap.addMarker(
            MarkerOptions()
                .position(myPosition)
                .title(shopLocation.toString())
        )
        val cameraPosition2 = CameraPosition.Builder()
            .target(myPosition)
            .zoom(10.5F)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2))
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(myPosition))
    }

    private fun retrieveActiveUserRequest() {
        showSpinner()
        homeContentView.visibility = View.INVISIBLE
        hideComponent(homeNoContentView)
        apiService.retrieveActiveUserRequest(getUser().Id!!, true)
            .enqueue((object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mainHandler.post {
                        handleApiResponseException(call, e)
                        hideSpinner()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    mainHandler.post {
                        handleRetrieveActiveUserRequestResponse(response)
                        hideSpinner()
                    }
                }
            }
                    ))
    }

    fun handleRetrieveActiveUserRequestResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                this.request = resp.Data as Request
                mapServiceLocation =
                    supportFragmentManager.findFragmentById(R.id.mapServiceLocation) as? SupportMapFragment
                mapServiceLocation?.getMapAsync(this)
                populateViewWithData()
                showContent(true)
            } catch (e: Exception) {
                showContent(false)
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            showContent(false)
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateViewWithData() {
        if (request?.ShopAccepted == true && request?.UserAccepted == null) {
            showComponent(cvConsents)
            tvShopConsent.text = Helper.formatDate(request!!.ShopAcceptedDate!!)
        } else {
            hideComponent(cvConsents)
        }
        tvDateOfRepair.text = Helper.formatDate(request?.RepairDate!!)
        tvTimeOfRepair.text = Helper.formatTime(request?.RepairDate!!)
        tvRepairShopName.text = request?.Shop?.ShortName
        tvRepairShopVat.text = request?.Shop?.Vat
        tvRequestDescription.text = request?.IssueDescription
        tvEstimatedTime.text = request?.EstimatedRepairHours.toString()
        tvEstimatedPrice.text = request?.EstimatedPrice?.toString()

        if (request?.Issues != null && request!!.Issues!!.size > 0) {
            userIssuesAdapter = UserIssuesAdapter(request!!.Issues!!,
                object : ButtonClickListener {
                    override fun onClick(issue: Issue) {
                        updateIssue(issue)
                    }
                }
            )
            rvIssues.adapter = userIssuesAdapter
            rvIssues.layoutManager = LinearLayoutManager(this)
            cvIssues.visibility = View.VISIBLE
            userIssuesAdapter.notifyDataSetChanged()

        }
        else {
            cvIssues.visibility = View.INVISIBLE
        }
    }

    private fun updateIssue(issue:Issue){
        showSpinner()
        apiService.updateIssue(issue).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleIssueUpdateResponse(response)
                    hideSpinner()
                }
            }

        })
    }

    private fun handleIssueUpdateResponse(response: Response) {
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Issue>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                Helper.showLongToast(this, resp.Message!!)
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }

    }

    private fun showContent(hasContent: Boolean) {
        if (hasContent) {
            showComponent(homeContentView)
            hideComponent(homeNoContentView)
        } else {
            showComponent(homeNoContentView)
            hideComponent(homeContentView)
        }
    }
}