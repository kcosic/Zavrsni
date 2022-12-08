package hr.kcosic.app.model.helpers

import android.icu.util.Calendar
import android.widget.EditText
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.ContextInstance
import hr.kcosic.app.model.entities.Location

class ValidationHelper {
    companion object {

        //#region General

        fun isStringInRange(text: String, minimum: Int, maximum: Int): Boolean {
            return text.length in minimum..maximum
        }

        fun isValueInRange(value: Int, start: Int, end: Int): Boolean {
            return value in start..end

        }

        fun isValueInRange(value: Double, start: Double, end: Double): Boolean {
            return value in start..end
        }

        //#endregion General

        fun validateCar(
            etManufacturer: EditText,
            etModel: EditText,
            etYear: EditText,
            etOdometer: EditText
        ): Boolean {
            var isValid = true
            when {
                etManufacturer.text.toString().isEmpty() -> {
                    etManufacturer.setError(
                        ContextInstance.getContext()!!.getString(R.string.required_value),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
                !isStringInRange(etManufacturer.text.toString(), 1, 50) -> {
                    etManufacturer.setError(
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
            }
            when {
                etModel.text.toString().isEmpty() -> {
                    etModel.setError(
                        ContextInstance.getContext()!!.getString(R.string.required_value),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
                !isStringInRange(etModel.text.toString(), 1, 50) -> {
                    etModel.setError(
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
            }
            when {
                etYear.text.toString().isEmpty() -> {
                    etYear.setError(
                        ContextInstance.getContext()!!.getString(R.string.required_value),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
                !isValueInRange(
                    etYear.text.toString().toInt(),
                    1960,
                    Calendar.getInstance().get(Calendar.YEAR)
                ) -> {
                    etYear.setError(
                        ContextInstance.getContext()!!.getString(R.string.value_between_1960_today),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
            }
            when {
                etOdometer.text.toString().isEmpty() -> {
                    etOdometer.setError(
                        ContextInstance.getContext()!!.getString(R.string.required_value),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
            }
            when {
                etModel.text.toString().isEmpty() -> {
                    etModel.setError(
                        ContextInstance.getContext()!!.getString(R.string.required_value),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
                !isStringInRange(etModel.text.toString(), 1, 50) -> {
                    etModel.setError(
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }
            }
            return isValid
        }

        fun validateResetPassword(
            etOldPassword: EditText,
            etNewPassword: EditText,
            etNewPasswordRepeat: EditText
        ): Boolean {
            var isValid = true
            when {
                etOldPassword.text.isNullOrEmpty() -> {
                    isValid = false
                    etOldPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etOldPassword.text.toString(), 1, 50) -> {
                    isValid = false
                    etOldPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }
            when {
                etNewPassword.text.isNullOrEmpty() -> {
                    isValid = false
                    etNewPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etNewPassword.text.toString(), 1, 50) -> {
                    isValid = false
                    etNewPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }

            when {
                etNewPasswordRepeat.text.isNullOrEmpty() -> {
                    isValid = false
                    etNewPasswordRepeat.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etNewPasswordRepeat.text.toString(), 1, 50) -> {
                    isValid = false
                    etNewPasswordRepeat.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
                etNewPasswordRepeat.text.toString() != etNewPassword.text.toString() -> {
                    isValid = false
                    etNewPasswordRepeat.error =
                        ContextInstance.getContext()!!.getString(R.string.passwords_must_match)
                }
            }
            return isValid
        }

        fun validateUser(
            etFirstName: EditText,
            etUsername: EditText,
            etDob: EditText,
            etEmail: EditText,
            etLastName: EditText,
            etPassword: EditText,
            etRepeatPassword: EditText
        ): Boolean {
            var valid = true
            when {
                etFirstName.text.isNullOrEmpty() -> {
                    valid = false
                    etFirstName.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etLastName.text.toString(), 1, 50) -> {
                    valid = false
                    etFirstName.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }

            when {
                etUsername.text.isNullOrEmpty() -> {
                    valid = false
                    etUsername.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etUsername.text.toString(), 5, 50) -> {
                    valid = false
                    etUsername.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_5_50)
                }
            }

            when {
                etDob.text.isNullOrEmpty() -> {
                    valid = false
                    etDob.error = ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etDob.text.toString(), 1, 50) -> {
                    valid = false
                    etDob.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }

            when {
                etEmail.text.isNullOrEmpty() -> {
                    valid = false
                    etEmail.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
            }

            when {
                etLastName.text.isNullOrEmpty() -> {
                    valid = false
                    etLastName.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etLastName.text.toString(), 1, 50) -> {
                    valid = false
                    etLastName.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }

            when {
                etPassword.text.isNullOrEmpty() -> {
                    valid = false
                    etPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etPassword.text.toString(), 1, 50) -> {
                    valid = false
                    etPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }

            when {
                etRepeatPassword.text.isNullOrEmpty() -> {
                    valid = false
                    etRepeatPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etRepeatPassword.text.toString(), 1, 50) -> {
                    valid = false
                    etRepeatPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }
            when {
                etRepeatPassword.text.toString() != etPassword.text.toString() -> {
                    valid = false
                    etRepeatPassword.error =
                        ContextInstance.getContext()!!.getString(R.string.passwords_must_match)
                }
            }

            return valid
        }

        fun validateShop(location: Location?, etVat: EditText, etEmail: EditText, etLegalName: EditText, etShortName: EditText, etPassword: EditText, etRepeatPassword: EditText): Boolean {
            var isValid = true

            when {
                etVat.text.toString().isEmpty() -> {
                    etVat.setError(ContextInstance.getContext()!!.getString(R.string.required_value), IconHelper.getErrorIcon())
                    isValid = false
                }
                !isStringInRange(etVat.text.toString(), 1, 50) -> {
                    etVat.setError(ContextInstance.getContext()!!.getString(R.string.length_between_1_50), IconHelper.getErrorIcon())
                    isValid = false
                }

            }

            when {
                etEmail.text.toString().isEmpty() -> {
                    etEmail.setError(ContextInstance.getContext()!!.getString(R.string.required_value), IconHelper.getErrorIcon())
                    isValid = false
                }
                !isStringInRange(etEmail.text.toString(), 1, 50) -> {
                    etEmail.setError(ContextInstance.getContext()!!.getString(R.string.length_between_1_50), IconHelper.getErrorIcon())
                    isValid = false
                }

            }

            when {
                etLegalName.text.toString().isEmpty() -> {
                    etLegalName.setError(ContextInstance.getContext()!!.getString(R.string.required_value), IconHelper.getErrorIcon())
                    isValid = false
                }
                !isStringInRange(etLegalName.text.toString(), 1, 200) -> {
                    etLegalName.setError(
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_200),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }

            }

            when {
                etShortName.text.toString().isEmpty() -> {
                    etShortName.setError(ContextInstance.getContext()!!.getString(R.string.required_value), IconHelper.getErrorIcon())
                    isValid = false
                }
                !isStringInRange(etShortName.text.toString(), 1, 50) -> {
                    etShortName.setError(
                        ContextInstance.getContext()!!.getString(R.string.length_between_1_50),
                        IconHelper.getErrorIcon()
                    )
                    isValid = false
                }

            }

            when (location) {
                null -> {
                    Helper.showLongToast(ContextInstance.getContext()!!, ContextInstance.getContext()!!.getString(R.string.location_required))
                    isValid = false
                }
            }

            when {
                etPassword.text.isNullOrEmpty() -> {
                    isValid = false
                    etPassword.error = ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etPassword.text.toString(), 1, 50) -> {
                    isValid = false
                    etPassword.error = ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }

            when {
                etRepeatPassword.text.isNullOrEmpty() -> {
                    isValid = false
                    etRepeatPassword.error = ContextInstance.getContext()!!.getString(R.string.required_value)
                }
                !isStringInRange(etRepeatPassword.text.toString(), 1, 50) -> {
                    isValid = false
                    etRepeatPassword.error = ContextInstance.getContext()!!.getString(R.string.length_between_1_50)
                }
            }

            when {
                etRepeatPassword.text.toString() != etPassword.text.toString() -> {
                    isValid = false
                    etRepeatPassword.error = ContextInstance.getContext()!!.getString(R.string.passwords_must_match)
                }
            }

            return isValid

        }

        fun validateCurrentRequest(etWorkHoursEstimate: EditText): Boolean {
            var isValid = true

            try {
                when {
                    !isValueInRange(etWorkHoursEstimate.text.toString().toInt(), 1, 999) -> {
                        isValid = false
                        etWorkHoursEstimate.error = ContextInstance.getContext()!!.getString(R.string.value_between_1_999)
                    }
                }
            }catch (e: java.lang.NumberFormatException){
                isValid = false
                etWorkHoursEstimate.error = ContextInstance.getContext()!!.getString(R.string.incorrectFormat)
            }

            return isValid
        }
    }
}