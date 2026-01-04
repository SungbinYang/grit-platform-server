package im.grit.infrastructure.exception.custom

import im.grit.infrastructure.exception.GlobalExceptionCode

class RateLimitExceededException(
    message: String = GlobalExceptionCode.RATE_LIMIT_EXCEEDED.message,
) : RuntimeException(message)
