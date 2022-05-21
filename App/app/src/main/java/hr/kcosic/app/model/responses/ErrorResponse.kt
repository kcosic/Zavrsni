package hr.kcosic.app.model.responses

import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.enums.ErrorCodeEnum

class ErrorResponse : BaseResponse() {
    lateinit var ErrorCode: ErrorCodeEnum
}