package im.grit.infrastructure.config.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.security.SecureRandom

class PepperPasswordEncoder(
    private val pepper: String,
    strength: Int,
) : PasswordEncoder {

    private val bcrypt = BCryptPasswordEncoder(strength, SecureRandom())

    override fun encode(rawPassword: CharSequence?): String? {
        requireNotNull(rawPassword) { "Raw password cannot be null" }
        return bcrypt.encode(rawPassword.toString() + pepper)
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        if (rawPassword == null || encodedPassword == null) {
            return false
        }

        return bcrypt.matches(rawPassword.toString() + pepper, encodedPassword)
    }
}
