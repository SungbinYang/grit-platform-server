package im.grit.infrastructure.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    @Column(nullable = false, updatable = false, comment = "생성 날짜")
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    @Column(nullable = false, comment = "최종 수정 날짜")
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set
}
