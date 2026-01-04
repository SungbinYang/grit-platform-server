package im.grit.infrastructure.common.response.success

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val message: String,
    val data: T?,
) {
    companion object {
        fun success(message: String): ApiResponse<Nothing> {
            return ApiResponse(
                message = message,
                data = null,
            )
        }

        fun <T> success(data: T, message: String): ApiResponse<T> {
            return ApiResponse(
                message = message,
                data = data,
            )
        }
    }
}
