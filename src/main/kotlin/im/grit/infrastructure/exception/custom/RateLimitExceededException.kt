package im.grit.infrastructure.exception.custom

import im.grit.infrastructure.exception.GlobalExceptionCode

class RateLimitExceededException(
    override val message: String = GlobalExceptionCode.RATE_LIMIT_EXCEEDED.message,
) : RuntimeException(message)
