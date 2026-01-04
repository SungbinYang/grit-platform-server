package im.grit.infrastructure.common.response.error

import com.fasterxml.jackson.annotation.JsonInclude
import im.grit.infrastructure.exception.ExceptionCode
import org.springframework.validation.BindingResult
import java.time.LocalDateTime
import java.time.ZoneId

data class ErrorResponse(
    val message: String,
    val status: Int,
    val code: String,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val errors: List<ValidationError> = emptyList(),
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
) {
    data class ValidationError(
        val field: String,
        val value: String?,
        val reason: String?,
    ) {
        companion object {
            fun of(bindingResult: BindingResult): List<ValidationError> {
                return bindingResult.fieldErrors.map { fieldError ->
                    ValidationError(
                        field = fieldError.field,
                        value = fieldError.rejectedValue?.toString(),
                        reason = fieldError.defaultMessage,
                    )
                }
            }
        }
    }

    companion object {
        fun of(exceptionCode: ExceptionCode): ErrorResponse {
            return ErrorResponse(
                message = exceptionCode.message,
                status = exceptionCode.status.value(),
                code = exceptionCode.code,
            )
        }

        fun of(exceptionCode: ExceptionCode, message: String): ErrorResponse {
            return ErrorResponse(
                message = message,
                status = exceptionCode.status.value(),
                code = exceptionCode.code,
            )
        }

        fun of(exceptionCode: ExceptionCode, bindingResult: BindingResult): ErrorResponse {
            return ErrorResponse(
                message = exceptionCode.message,
                status = exceptionCode.status.value(),
                code = exceptionCode.code,
                errors = ValidationError.of(bindingResult),
            )
        }
    }
}
