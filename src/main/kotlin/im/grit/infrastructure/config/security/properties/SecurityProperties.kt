package im.grit.infrastructure.config.security.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.security")
data class SecurityProperties(
    val allowedOrigins: List<String>,
    val cors: CorsProperties,
    val passwordPepper: String,
) {
    data class CorsProperties(
        val maxAge: Long,
    )
}
