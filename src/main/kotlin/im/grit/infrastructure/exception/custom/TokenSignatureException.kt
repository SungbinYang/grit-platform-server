package im.grit.infrastructure.exception.custom

import im.grit.infrastructure.exception.GlobalExceptionCode

class TokenSignatureException(
    override val message: String = GlobalExceptionCode.TOKEN_SIGNATURE_INVALID.message,
) : RuntimeException(message)
