package im.grit.infrastructure.exception

import org.springframework.http.HttpStatus

interface ExceptionCode {
    val status: HttpStatus
    val code: String
    val message: String
}
