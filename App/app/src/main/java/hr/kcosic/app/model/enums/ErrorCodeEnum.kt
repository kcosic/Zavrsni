package hr.kcosic.app.model.enums

enum class ErrorCodeEnum(val code: Int) {
    UnexpectedError(0),
    InvalidCredentials(1),
    InvalidToken(2),
    UsernameExists(3),
    EmailExists(4),
    InvalidParameter(5),
    LegalNameExists(6),
    ShortNameExists(7),
    VatExists(8),
    RecordNotFound(9),
    NoReview(10),
}