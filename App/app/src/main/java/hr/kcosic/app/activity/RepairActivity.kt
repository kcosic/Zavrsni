package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import hr.kcosic.app.R
import hr.kcosic.app.adapter.ShopIssuesAdapter
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Issue
import hr.kcosic.app.model.entities.Request
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.ErrorCodeEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.helpers.ValidationHelper
import hr.kcosic.app.model.listeners.ButtonClickListener
import hr.kcosic.app.model.listeners.OnPositiveButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

class RepairActivity : ValidatedActivityWithNavigation(ActivityEnum.REPAIR_VIEW) {
    companion object {
        const val REQUEST_ID_KEY = "87ioa4sd0fg8inf923af"
    }

    //#region Consent Card
    private lateinit var llShopConsentChoice: LinearLayout
    private lateinit var llShopConsent: LinearLayout
    private lateinit var llUserConsent: LinearLayout
    private lateinit var btnAcceptRequest: Button
    private lateinit var btnDeclineRequest: Button
    private lateinit var btnCompleteRequest: Button
    private lateinit var tvShopConsent: TextView
    private lateinit var tvUserConsent: TextView
    //#endregion Consent Card

    //#region Repair Date Time
    private lateinit var tvDateOfRepair: TextView
    private lateinit var tvTimeOfRepair: TextView
    private lateinit var etWorkHoursEstimate: EditText
    //#endregion Repair Date Time

    //#region Request Details
    private lateinit var tvRequestDescription: TextView
    private lateinit var tvVehicle: TextView

    //#endregion Request Details
    private lateinit var llIssues: LinearLayout
    private lateinit var ivIssuesSave: ImageView
    private lateinit var btnNewIssue: Button
    private lateinit var rvIssues: RecyclerView

    private lateinit var shopIssuesAdapter: ShopIssuesAdapter
    private var issues: MutableList<Issue> = mutableListOf()

    //#region Issues

    //#endregion Issues
    //#region Properties
    private var request: Request? = null
    //#endregion Properties

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        val requestId = retrieveRequestIdFromIntent()
        if (requestId == null) {
            retrieveCurrentRequest()
        } else if (requestId != -1) {
            retrieveRequest(requestId)
        }
    }

    override fun initializeComponents() {
        initializeConsentSection()
        initializeRequestDetailsSection()
        initializeRepairDateTimeSection()
        initializeIssuesSection()

    }

    private fun initializeIssuesSection() {
        llIssues = findViewById(R.id.llIssues)
        ivIssuesSave = findViewById(R.id.ivIssuesSave)
        btnNewIssue = findViewById(R.id.btnNewIssue)
        rvIssues = findViewById(R.id.rvIssues)

        llIssues.setOnClickListener {
            if(areIssuesValid()){
                Helper.showConfirmDialog(
                    this,
                    getString(R.string.save_changes),
                    getString(R.string.issue_save_text),
                    getString(R.string.yes),
                    getString(R.string.no),
                    object : OnPositiveButtonClickListener {
                        override fun onPositiveBtnClick(dialog: DialogInterface?) {
                            updateIssues()
                        }
                    }
                )
            }
            else {
                Helper.showLongToast(this, getString(R.string.invalid_issues))
            }

        }

        btnNewIssue.setOnClickListener {
            ivIssuesSave.visibility = View.VISIBLE
            issues.add(createNewIssue())
            updateRVIssues()
        }

    }

    private fun createNewIssue(): Issue {
        val newIssue = Issue()
        newIssue.RequestId = request!!.Id
        return newIssue
    }

    private fun areIssuesValid(): Boolean {
        var isValid = true
        for (issue in issues) {
            if(!ValidationHelper.validateIssueObject(issue)){
                isValid = false
            }
        }
        return isValid
    }

    private fun updateIssues(){
        showSpinner()
        request?.Issues = issues
        updateRequest(request!!)
    }

    private fun initializeRepairDateTimeSection() {
        tvDateOfRepair = findViewById(R.id.tvDateOfRepair)
        tvTimeOfRepair = findViewById(R.id.tvTimeOfRepair)
        etWorkHoursEstimate = findViewById(R.id.etWorkHoursEstimate)
    }

    private fun initializeRequestDetailsSection() {
        tvRequestDescription = findViewById(R.id.tvRequestDescription)
        tvVehicle = findViewById(R.id.tvVehicle)
    }

    private fun initializeConsentSection() {
        llShopConsentChoice = findViewById(R.id.llShopConsentChoice)
        llShopConsent = findViewById(R.id.llShopConsent)
        llUserConsent = findViewById(R.id.llUserConsent)
        btnAcceptRequest = findViewById(R.id.btnAcceptRequest)
        btnDeclineRequest = findViewById(R.id.btnDeclineRequest)
        btnCompleteRequest = findViewById(R.id.btnCompleteRequest)
        tvShopConsent = findViewById(R.id.tvShopConsent)
        tvUserConsent = findViewById(R.id.tvUserConsent)

        btnAcceptRequest.setOnClickListener {
            if (ValidationHelper.validateCurrentRequest(etWorkHoursEstimate)) {
                acceptRequest()
            }
        }
        btnDeclineRequest.setOnClickListener {
            declineRequest()

        }
        btnCompleteRequest.setOnClickListener {
            completeRequest()

        }
    }

    private fun completeRequest() {
        request!!.Completed = true
        request!!.FinishDate = Calendar.getInstance().time
        updateRequest(request!!)
    }

    private fun declineRequest() {
        request!!.ShopAccepted = false
        request!!.ShopAcceptedDate = Calendar.getInstance().time
        updateRequest(request!!)
    }

    private fun acceptRequest() {
        request!!.EstimatedRepairHours = etWorkHoursEstimate.text.toString().toInt()
        request!!.EstimatedPrice = request!!.EstimatedRepairHours!! * getShop().HourlyRate!!
        request!!.ShopAccepted = true
        request!!.ShopAcceptedDate = Calendar.getInstance().time
        updateRequest(request!!)
    }

    //#region Retrieve Request

    private fun retrieveRequestIdFromIntent(): Int? {
        return try {
            val stringMap = intent.getStringExtra(REQUEST_ID_KEY)
            val map = Helper.deserializeObject<Map<String, String>>(stringMap!!)
            map["requestId"].toString().toInt()

        } catch (e: NumberFormatException) {
            Helper.openActivity(this, ActivityEnum.HOME_SHOP)
            Helper.showLongToast(this, "Couldn't retrieve Request, ID is missing")
            -1
        } catch (e: Exception) {
            null
        }
    }

    private fun retrieveRequest(requestId: Int) {
        showSpinner()
        apiService.retrieveRequest(requestId, true).enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveRequestResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    private fun handleRetrieveRequestResponse(response: Response) {
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {

                request = resp.Data as Request
                issues = if(request!!.Issues != null) request!!.Issues!! else mutableListOf()
                updateUI(request!!)
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }
    //#endregion Retrieve Request
    // #region Retrieve Current Request

    private fun retrieveCurrentRequest() {
        showSpinner()
        apiService.retrieveCurrentRequest().enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveCurrentRequestResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    private fun handleRetrieveCurrentRequestResponse(response: Response) {
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())
        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                request = resp.Data as Request
                issues = if(request!!.Issues != null) request!!.Issues!! else mutableListOf()
                updateUI(request!!)
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            if ((resp as ErrorResponse).ErrorCode == ErrorCodeEnum.RecordNotFound) {
                Helper.openActivity(this, ActivityEnum.HOME_SHOP)
            }
            handleApiResponseError(resp)
        }
    }
    //#endregion Retrieve Current Request
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
                issues = if(request!!.Issues != null) request!!.Issues!! else mutableListOf()
                updateUI(request!!)
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }
    //#endregion Update Request

    private fun updateUI(request: Request) {
        if (request.ShopAccepted != null) {
            //shop accepted request
            if (request.ShopAccepted!!) {
                showComponent(llShopConsent)
                showComponent(llUserConsent)
                hideComponent(llShopConsentChoice)
                tvShopConsent.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.check_green,
                    0,
                    0,
                    0
                )

                if (request.UserAccepted != null) {
                    //user accepted request
                    if (request.UserAccepted!!) {
                        tvUserConsent.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.check_green,
                            0,
                            0,
                            0
                        )
                        //show complete button if current date is equal of later than repair date
                        etWorkHoursEstimate.isEnabled = false
                        if (request.RepairDate!! <= Calendar.getInstance().time && request.Completed == false) {
                            showComponent(btnCompleteRequest)
                            //TODO: add issues RV
                        } else {
                            hideComponent(btnCompleteRequest)
                        }
                    }
                    //user declined request
                    else {
                        tvUserConsent.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.xmark_red,
                            0,
                            0,
                            0
                        )
                        hideComponent(btnCompleteRequest)
                    }
                }
                //user hasn't responded
                else {
                    tvUserConsent.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.minus_gray,
                        0,
                        0,
                        0
                    )
                    hideComponent(btnCompleteRequest)
                }
            }
            //shop declined request
            else {
                hideComponent(llShopConsentChoice)
                hideComponent(btnCompleteRequest)
                showComponent(llUserConsent)
                showComponent(llShopConsent)
                tvShopConsent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.xmark_red, 0, 0, 0)
                tvUserConsent.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.minus_gray,
                    0,
                    0,
                    0
                )
            }
        }
        //shop hasn't responded yet
        else {
            showComponent(llShopConsentChoice)
            hideComponent(btnCompleteRequest)
            hideComponent(llUserConsent)
            hideComponent(llShopConsent)
        }

        tvVehicle.text = request.Car.toString()
        tvRequestDescription.text = request.IssueDescription
        tvDateOfRepair.text = Helper.formatDate(request.RepairDate!!)
        tvShopConsent.text =
            if (request.ShopAcceptedDate != null) Helper.formatDateTime(request.ShopAcceptedDate!!) else ""
        tvUserConsent.text =
            if (request.UserAcceptedDate != null) Helper.formatDateTime(request.UserAcceptedDate!!) else ""
        tvDateOfRepair.text = Helper.formatDate(request.RepairDate!!)
        tvTimeOfRepair.text = Helper.formatTime(request.RepairDate!!)

        etWorkHoursEstimate.setText(if (request.EstimatedRepairHours != null) request.EstimatedRepairHours!!.toString() else "")

        if(request.UserAccepted == false || request.Completed == true){
            btnNewIssue.visibility = View.GONE
            ivIssuesSave.visibility = View.GONE
        }
        else{
            btnNewIssue.visibility = View.VISIBLE
        }
        updateRVIssues()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRVIssues(){
        if(issues.size > 0){
            shopIssuesAdapter = ShopIssuesAdapter(issues,
                object: ButtonClickListener{
                    override fun onClick(issue: Issue) {
                        ivIssuesSave.visibility = View.VISIBLE
                        val iss = issues.find { i -> i == issue }
                        iss?.Deleted = true
                        iss?.DateDeleted = Calendar.getInstance().time
                        updateRVIssues()
                    }
                })
            rvIssues.adapter = shopIssuesAdapter
            rvIssues.layoutManager = LinearLayoutManager(this)
            shopIssuesAdapter.notifyDataSetChanged()
        }
    }
}