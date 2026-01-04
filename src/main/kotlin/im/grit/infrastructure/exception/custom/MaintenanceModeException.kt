package im.grit.infrastructure.exception.custom

import im.grit.infrastructure.exception.GlobalExceptionCode

class MaintenanceModeException(
    override val message: String = GlobalExceptionCode.MAINTENANCE_MODE.message,
) : RuntimeException(message)
