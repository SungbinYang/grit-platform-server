package im.grit.infrastructure.exception

import im.grit.infrastructure.common.response.error.ErrorResponse
import im.grit.infrastructure.exception.custom.BusinessException
import im.grit.infrastructure.exception.custom.BusinessRuleViolationException
import im.grit.infrastructure.exception.custom.MaintenanceModeException
import im.grit.infrastructure.exception.custom.RateLimitExceededException
import im.grit.infrastructure.exception.custom.TokenSignatureException
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.core.PropertyReferenceException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.rememberme.CookieTheftException
import org.springframework.security.web.authentication.rememberme.InvalidCookieException
import org.springframework.security.web.csrf.InvalidCsrfTokenException
import org.springframework.security.web.csrf.MissingCsrfTokenException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClientException
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.NoHandlerFoundException
import java.io.IOException
import java.net.ConnectException
import java.sql.SQLException
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.TimeoutException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * INVALID_REQUEST_PARAMETER (C400-01) 처리
     * 메소드 인자 유효성 검증 실패 시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
    ): ResponseEntity<ErrorResponse> {
        log.error("유효하지 않은 요청 파라미터: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INVALID_REQUEST_PARAMETER,
            "요청 파라미터가 유효하지 않습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INVALID_REQUEST_PARAMETER.status)
    }

    /**
     * INVALID_REQUEST_PARAMETER (C400-01) 처리
     * 바인딩 실패 시 발생
     */
    @ExceptionHandler(BindException::class)
    fun handleBindException(
        e: BindException,
    ): ResponseEntity<ErrorResponse> {
        log.error("바인딩 실패: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INVALID_REQUEST_PARAMETER,
            "바인딩에 실패하였습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INVALID_REQUEST_PARAMETER.status)
    }

    /**
     * INVALID_INPUT_FORMAT (C400-04) 처리
     * 메소드 인자 타입 불일치 시 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        e: MethodArgumentTypeMismatchException,
    ): ResponseEntity<ErrorResponse> {
        log.error("메소드 인자 타입 불일치: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INVALID_INPUT_FORMAT,
            "요청 파라미터 형식이 올바르지 않습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INVALID_INPUT_FORMAT.status)
    }

    /**
     * REQUEST_SIZE_EXCEEDED (C400-06) 처리
     * 파일 업로드 크기 초과 시 발생
     */
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(
        e: MaxUploadSizeExceededException,
    ): ResponseEntity<ErrorResponse> {
        log.error("파일 업로드 크기 초과: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.REQUEST_SIZE_EXCEEDED,
            "파일 크기가 제한을 초과했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.REQUEST_SIZE_EXCEEDED.status)
    }

    /**
     * BUSINESS_RULE_VIOLATION (C422-03) 처리
     * 비즈니스 규칙 위반 시 발생
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        e: BusinessException,
    ): ResponseEntity<ErrorResponse> {
        log.error("비즈니스 예외: ", e)
        val response = ErrorResponse.of(e.exceptionCode, e.message)
        return ResponseEntity(response, e.exceptionCode.status)
    }

    /**
     * INVALID_REQUEST_BODY (C400-02) 처리
     * HTTP 메시지 본문 파싱 실패 시 발생
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        e: HttpMessageNotReadableException,
    ): ResponseEntity<ErrorResponse> {
        log.error("HTTP 메시지 본문 파싱 실패: ", e)
        val response =
            ErrorResponse.of(
                GlobalExceptionCode.INVALID_REQUEST_BODY,
                "요청 본문을 읽을 수 없습니다. 올바른 JSON 형식인지 확인하세요.",
            )
        return ResponseEntity(response, GlobalExceptionCode.INVALID_REQUEST_BODY.status)
    }

    /**
     * MISSING_REQUIRED_FIELD (C400-03) 처리
     * 필수 요청 파라미터 누락 시 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(
        e: MissingServletRequestParameterException,
    ): ResponseEntity<ErrorResponse> {
        log.error("필수 요청 파라미터 누락: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.MISSING_REQUIRED_FIELD,
            "필수 파라미터가 누락되었습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.MISSING_REQUIRED_FIELD.status)
    }

    /**
     * UNSUPPORTED_MEDIA_TYPE (C400-07) 처리
     * 지원하지 않는 미디어 타입 요청 시 발생
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotSupportedException(
        e: HttpMediaTypeNotSupportedException,
    ): ResponseEntity<ErrorResponse> {
        log.error("지원하지 않는 미디어 타입: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.UNSUPPORTED_MEDIA_TYPE,
            "지원하지 않는 미디어 타입입니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.UNSUPPORTED_MEDIA_TYPE.status)
    }

    /**
     * DATA_INTEGRITY_VIOLATION (C400-05) 처리
     * 데이터 무결성 위반 시 발생
     */
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(
        e: DataIntegrityViolationException,
    ): ResponseEntity<ErrorResponse> {
        log.error("데이터 무결성 위반: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.DATA_INTEGRITY_VIOLATION,
            "데이터 무결성 위반이 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.DATA_INTEGRITY_VIOLATION.status)
    }

    /**
     * UNAUTHORIZED_RESOURCE_OWNER (A401-01) 처리
     * 인증 실패 시 발생
     */
    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        e: AuthenticationException,
    ): ResponseEntity<ErrorResponse> {
        log.error("인증 실패: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.UNAUTHORIZED_RESOURCE_OWNER,
            "인증에 실패했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.UNAUTHORIZED_RESOURCE_OWNER.status)
    }

    /**
     * INVALID_CREDENTIALS (A401-04) 처리
     * 잘못된 인증 정보 제공 시 발생
     */
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(
        e: BadCredentialsException,
    ): ResponseEntity<ErrorResponse> {
        log.error("잘못된 인증 정보: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INVALID_CREDENTIALS,
            "아이디 또는 비밀번호가 일치하지 않습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INVALID_CREDENTIALS.status)
    }

    /**
     * TOKEN_SIGNATURE_INVALID (A401-06) 처리
     * 토큰 서명이 유효하지 않을 때 발생
     */
    @ExceptionHandler(TokenSignatureException::class)
    fun handleTokenSignatureException(
        e: TokenSignatureException,
    ): ResponseEntity<ErrorResponse> {
        log.error("토큰 서명 무효: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.TOKEN_SIGNATURE_INVALID,
            "토큰 서명이 유효하지 않습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.TOKEN_SIGNATURE_INVALID.status)
    }

    /**
     * INVALID_TOKEN (A401-02) 처리
     * 유효하지 않은 쿠키 또는 토큰
     */
    @ExceptionHandler(
        InvalidCookieException::class,
        CookieTheftException::class,
        InvalidCsrfTokenException::class,
    )
    fun handleInvalidTokenException(
        e: Exception,
    ): ResponseEntity<ErrorResponse> {
        log.error("유효하지 않은 토큰: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INVALID_TOKEN,
            "유효하지 않은 인증 토큰입니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INVALID_TOKEN.status)
    }

    /**
     * TOKEN_EXPIRED (A401-03) 처리
     * 토큰 만료 시 발생
     */
    @ExceptionHandler(CredentialsExpiredException::class)
    fun handleCredentialsExpiredException(
        e: CredentialsExpiredException,
    ): ResponseEntity<ErrorResponse> {
        log.error("토큰 만료: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.TOKEN_EXPIRED,
            "인증 토큰이 만료되었습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.TOKEN_EXPIRED.status)
    }

    /**
     * MISSING_TOKEN (A401-05) 처리
     * 토큰 누락 시 발생
     */
    @ExceptionHandler(MissingCsrfTokenException::class)
    fun handleMissingCsrfTokenException(
        e: MissingCsrfTokenException,
    ): ResponseEntity<ErrorResponse> {
        log.error("토큰 누락: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.MISSING_TOKEN,
            "인증 토큰이 제공되지 않았습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.MISSING_TOKEN.status)
    }

    /**
     * ACCESS_LIMIT_EXCEEDED (A403-03) 처리
     * 계정 잠금 시 발생
     */
    @ExceptionHandler(LockedException::class)
    fun handleLockedException(
        e: LockedException,
    ): ResponseEntity<ErrorResponse> {
        log.error("계정 잠금: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.ACCESS_LIMIT_EXCEEDED,
            "접근 제한 횟수를 초과했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.ACCESS_LIMIT_EXCEEDED.status)
    }

    /**
     * INSUFFICIENT_PERMISSIONS (A403-02) 처리
     * 불충분한 인증 시 발생
     */
    @ExceptionHandler(InsufficientAuthenticationException::class)
    fun handleInsufficientAuthenticationException(
        e: InsufficientAuthenticationException,
    ): ResponseEntity<ErrorResponse> {
        log.error("불충분한 인증: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INSUFFICIENT_PERMISSIONS,
            "인증 정보가 없거나 불충분합니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INSUFFICIENT_PERMISSIONS.status)
    }

    /**
     * INVALID_RESOURCE_OWNER (A403-01) 처리
     * 접근 권한 부족 시 발생
     */
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        e: AccessDeniedException,
    ): ResponseEntity<ErrorResponse> {
        log.error("접근 권한 부족: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INVALID_RESOURCE_OWNER,
            "해당 리소스에 접근할 권한이 없습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INVALID_RESOURCE_OWNER.status)
    }

    /**
     * INSUFFICIENT_PERMISSIONS (A403-02) 처리
     * 계정 비활성화 시 발생
     */
    @ExceptionHandler(DisabledException::class)
    fun handleDisabledException(
        e: DisabledException,
    ): ResponseEntity<ErrorResponse> {
        log.error("계정 비활성화: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INSUFFICIENT_PERMISSIONS,
            "비활성화된 계정입니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INSUFFICIENT_PERMISSIONS.status)
    }

    /**
     * NOT_FOUND_RESOURCE (R404-01) 처리
     * 요소를 찾을 수 없을 때 발생
     */
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(
        e: NoSuchElementException,
    ): ResponseEntity<ErrorResponse> {
        log.error("요소를 찾을 수 없음: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.NOT_FOUND_RESOURCE,
            "요청한 리소스를 찾을 수 없습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.NOT_FOUND_RESOURCE.status)
    }

    /**
     * NOT_FOUND_RESOURCE (R404-01) 처리
     * 엔티티를 찾을 수 없을 때 발생
     */
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(
        e: EntityNotFoundException,
    ): ResponseEntity<ErrorResponse> {
        log.error("엔티티를 찾을 수 없음: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.NOT_FOUND_RESOURCE,
            "요청한 엔티티를 찾을 수 없습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.NOT_FOUND_RESOURCE.status)
    }

    /**
     * NOT_FOUND_RESOURCE (R404-01) 처리
     * JPA 속성 참조 오류 시 발생
     */
    @ExceptionHandler(PropertyReferenceException::class)
    fun handlePropertyReferenceException(
        e: PropertyReferenceException,
    ): ResponseEntity<ErrorResponse> {
        log.error("JPA 속성 참조 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.NOT_FOUND_RESOURCE,
            "잘못된 속성 참조가 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.NOT_FOUND_RESOURCE.status)
    }

    /**
     * ENDPOINT_NOT_FOUND (R404-02) 처리
     * 요청한 엔드포인트가 없을 때 발생
     */
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(
        e: NoHandlerFoundException,
    ): ResponseEntity<ErrorResponse> {
        log.error("엔드포인트를 찾을 수 없음: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.ENDPOINT_NOT_FOUND,
            "요청을 처리할 수 없습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.ENDPOINT_NOT_FOUND.status)
    }

    /**
     * INVALID_REQUEST_METHOD (M405-01) 처리
     * 지원하지 않는 HTTP 메소드 요청 시 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(
        e: HttpRequestMethodNotSupportedException,
    ): ResponseEntity<ErrorResponse> {
        log.error("지원하지 않는 HTTP 메소드: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INVALID_REQUEST_METHOD,
            "지원하지 않는 HTTP 메소드입니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INVALID_REQUEST_METHOD.status)
    }

    /**
     * RESOURCE_CONFLICT (C409-01) 처리
     * 리소스 상태 충돌 시 발생
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException,
    ): ResponseEntity<ErrorResponse> {
        log.error("리소스 충돌: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.RESOURCE_CONFLICT,
            "리소스 충돌이 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.RESOURCE_CONFLICT.status)
    }

    /**
     * CONCURRENT_MODIFICATION (C409-02) 처리
     * 낙관적 락킹 실패 시 발생
     */
    @ExceptionHandler(OptimisticLockingFailureException::class)
    fun handleOptimisticLockingFailureException(
        e: OptimisticLockingFailureException,
    ): ResponseEntity<ErrorResponse> {
        log.error("낙관적 락킹 실패: ", e)
        val response =
            ErrorResponse.of(
                GlobalExceptionCode.CONCURRENT_MODIFICATION,
                "다른 사용자가 해당 리소스를 수정했습니다. 다시 시도해주세요.",
            )
        return ResponseEntity(response, GlobalExceptionCode.CONCURRENT_MODIFICATION.status)
    }

    /**
     * VERSION_CONFLICT (C409-03) 처리
     * 버전 충돌 시 발생
     */
    @ExceptionHandler(SQLException::class)
    fun handleSQLException(
        e: SQLException,
    ): ResponseEntity<ErrorResponse> {
        val sqlState = e.sqlState

        if (sqlState == "23000" || sqlState == "40001") {
            log.error("버전 충돌: ", e)
            val response = ErrorResponse.of(
                GlobalExceptionCode.VERSION_CONFLICT,
                "리소스 버전 충돌이 발생했습니다.",
            )
            return ResponseEntity(response, GlobalExceptionCode.VERSION_CONFLICT.status)
        }
        return handleDatabaseException(e)
    }

    /**
     * DUPLICATE_RESOURCE (C409-04) 처리
     * 리소스 충돌 발생 시
     */
    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(
        e: DuplicateKeyException,
    ): ResponseEntity<ErrorResponse> {
        log.error("중복 키 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.DUPLICATE_RESOURCE,
            "이미 존재하는 리소스입니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.DUPLICATE_RESOURCE.status)
    }

    /**
     * UNPROCESSABLE_ENTITY (C422-01) 처리
     * 요청을 처리할 수 없음
     */
    @ExceptionHandler(UnsupportedOperationException::class)
    fun handleUnsupportedOperationException(
        e: UnsupportedOperationException,
    ): ResponseEntity<ErrorResponse> {
        log.error("지원하지 않는 연산: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.UNPROCESSABLE_REQUEST,
            "요청을 처리할 수 없습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.UNPROCESSABLE_REQUEST.status)
    }

    /**
     * VALIDATION_FAILED (C422-02) 처리
     * 제약 조건 위반 시 발생
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        e: ConstraintViolationException,
    ): ResponseEntity<ErrorResponse> {
        log.error("제약 조건 위반: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.VALIDATION_FAILED,
            "요청 데이터가 유효성 규칙을 위반했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.VALIDATION_FAILED.status)
    }

    /**
     * BUSINESS_RULE_VIOLATION (C422-03) 처리
     * 비즈니스 규칙 위반 시 발생
     */
    @ExceptionHandler(BusinessRuleViolationException::class)
    fun handleBusinessRuleViolationException(
        e: BusinessRuleViolationException,
    ): ResponseEntity<ErrorResponse> {
        log.error("비즈니스 규칙 위반: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.BUSINESS_RULE_VIOLATION,
            e.message,
        )
        return ResponseEntity(response, GlobalExceptionCode.BUSINESS_RULE_VIOLATION.status)
    }

    /**
     * DATABASE_ERROR (S500-02) 처리
     * 데이터 액세스 오류 시 발생
     */
    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(
        e: DataAccessException,
    ): ResponseEntity<ErrorResponse> {
        log.error("데이터 액세스 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.DATABASE_ERROR,
            "데이터베이스 오류가 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.DATABASE_ERROR.status)
    }

    /**
     * DATABASE_ERROR (S500-02) 처리
     * 데이터베이스 예외 처리
     */
    private fun handleDatabaseException(
        e: Exception,
    ): ResponseEntity<ErrorResponse> {
        log.error("데이터베이스 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.DATABASE_ERROR,
            "데이터베이스 오류가 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.DATABASE_ERROR.status)
    }

    /**
     * EXTERNAL_API_ERROR (S500-03) 처리
     * 외부 API 호출 오류 시 발생
     */
    @ExceptionHandler(RestClientException::class)
    fun handleRestClientException(
        e: RestClientException,
    ): ResponseEntity<ErrorResponse> {
        log.error("외부 API 호출 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.EXTERNAL_API_ERROR,
            "외부 API 호출 중 오류가 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.EXTERNAL_API_ERROR.status)
    }

    /**
     * FILE_PROCESSING_ERROR (S500-05) 처리
     * 파일 처리 오류 시 발생
     */
    @ExceptionHandler(IOException::class)
    fun handleIOException(
        e: IOException,
    ): ResponseEntity<ErrorResponse> {
        log.error("파일 처리 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.FILE_PROCESSING_ERROR,
            "파일 처리 중 오류가 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.FILE_PROCESSING_ERROR.status)
    }

    /**
     * INTEGRATION_ERROR (S500-06) 처리
     * 외부 시스템 연동 오류
     */
    @ExceptionHandler(ConnectException::class)
    fun handleConnectException(
        e: ConnectException,
    ): ResponseEntity<ErrorResponse> {
        log.error("연결 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.INTEGRATION_ERROR,
            "외부 시스템 연동 중 오류가 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.INTEGRATION_ERROR.status)
    }

    /**
     * SERVICE_UNAVAILABLE (S503-01) 처리
     * 서비스 일시적 사용 불가
     */
    @ExceptionHandler(RejectedExecutionException::class)
    fun handleRejectedExecutionException(
        e: RejectedExecutionException,
    ): ResponseEntity<ErrorResponse> {
        log.error("작업 거부: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.SERVICE_UNAVAILABLE_NOW,
            "서비스를 일시적으로 사용할 수 없습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.SERVICE_UNAVAILABLE_NOW.status)
    }

    /**
     * RATE_LIMIT_EXCEEDED (S503-03) 처리
     * 요청 비율 제한 초과
     */
    @ExceptionHandler(RateLimitExceededException::class)
    fun handleRateLimitExceededException(
        e: RateLimitExceededException,
    ): ResponseEntity<ErrorResponse> {
        log.error("비율 제한 초과: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.RATE_LIMIT_EXCEEDED,
            "요청 비율 제한을 초과했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.RATE_LIMIT_EXCEEDED.status)
    }

    /**
     * UNEXPECTED_ERROR (S500-04) 처리
     * IllegalStateException 처리
     */
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        e: IllegalStateException,
    ): ResponseEntity<ErrorResponse> {
        log.error("잘못된 상태: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.UNEXPECTED_ERROR,
            "예상치 못한 오류가 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.UNEXPECTED_ERROR.status)
    }

    /**
     * TIMEOUT (G504-01) 처리
     * 리소스 접근 시간 초과 시 발생
     */
    @ExceptionHandler(ResourceAccessException::class, TimeoutException::class, AsyncRequestTimeoutException::class)
    fun handleTimeoutException(
        e: Exception,
    ): ResponseEntity<ErrorResponse> {
        log.error("시간 초과: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.TIMEOUT,
            "외부 서비스 응답 시간이 초과되었습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.TIMEOUT.status)
    }

    /**
     * UNEXPECTED_ERROR (S500-04) 처리
     * 예상치 못한 오류 발생
     */
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(
        e: RuntimeException,
    ): ResponseEntity<ErrorResponse> {
        log.error("예상치 못한 런타임 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.UNEXPECTED_ERROR,
            "예상치 못한 오류가 발생했습니다.",
        )
        return ResponseEntity(response, GlobalExceptionCode.UNEXPECTED_ERROR.status)
    }

    /**
     * MAINTENANCE_MODE (S503-02) 처리
     * 시스템 유지보수 모드일 때 발생
     */
    @ExceptionHandler(MaintenanceModeException::class)
    fun handleMaintenanceModeException(
        e: MaintenanceModeException,
    ): ResponseEntity<ErrorResponse> {
        log.warn("유지보수 모드: {}", e.message)
        val response = ErrorResponse.of(
            GlobalExceptionCode.MAINTENANCE_MODE,
            e.message,
        )
        return ResponseEntity(response, GlobalExceptionCode.MAINTENANCE_MODE.status)
    }

    /**
     * SERVER_ERROR (S500-01) 처리
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception::class)
    fun handleException(
        e: Exception,
    ): ResponseEntity<ErrorResponse> {
        log.error("서버 오류: ", e)
        val response = ErrorResponse.of(
            GlobalExceptionCode.SERVER_ERROR,
            "서버 내부 오류가 발생했습니다.",
        )
        return ResponseEntity.internalServerError().body(response)
    }
}
