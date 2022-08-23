package hr.kcosic.app.model.responses

import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.enums.ErrorCodeEnum
import hr.kcosic.app.model.serializer.ErrorCodeSerializer
import kotlinx.serialization.Serializable

@Serializable
class ErrorResponse : BaseResponse() {
    @Serializable(ErrorCodeSerializer::class)
    lateinit var ErrorCode: ErrorCodeEnum
}