package hr.kcosic.app.model.enums

enum class ErrorCodeEnum(val code: Int) {
    UnexpectedError(0),
    InvalidCredentials(1),
    InvalidToken(2),
}