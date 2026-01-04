package im.grit.infrastructure.exception.custom

import im.grit.infrastructure.exception.GlobalExceptionCode

class BusinessRuleViolationException(
    override val message: String = GlobalExceptionCode.BUSINESS_RULE_VIOLATION.message,
) : RuntimeException(message)
