package im.grit.infrastructure.exception.custom

import im.grit.infrastructure.exception.GlobalExceptionCode

class InvalidTokenException(
    override val message: String = GlobalExceptionCode.INVALID_TOKEN.message,
) : RuntimeException(message)
