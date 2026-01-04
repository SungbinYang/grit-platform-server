package im.grit.infrastructure.config.audit

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
class AuditConfig {

    @Bean
    fun auditorAwareProvider(): AuditorAware<String> {
        return AuditorAwareImpl()
    }
}
