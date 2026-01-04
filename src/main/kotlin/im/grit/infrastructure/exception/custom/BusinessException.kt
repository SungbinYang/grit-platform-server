package im.grit.infrastructure.exception.custom

import im.grit.infrastructure.exception.ExceptionCode

class BusinessException(
    val exceptionCode: ExceptionCode,
    override val message: String = exceptionCode.message,
) : RuntimeException(message)
