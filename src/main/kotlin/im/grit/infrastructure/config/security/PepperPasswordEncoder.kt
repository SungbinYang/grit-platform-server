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
        return bcrypt.encode(rawPassword.toString() + pepper)
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return bcrypt.matches(rawPassword.toString() + pepper, encodedPassword)
    }
}
