package im.grit.infrastructure.common.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

@MappedSuperclass
abstract class BaseEntity : BaseDateTimeEntity() {

    @CreatedBy
    @Column(nullable = false, updatable = false, comment = "생성한 사용자")
    var createdBy: String = ""
        protected set

    @LastModifiedBy
    @Column(nullable = false, comment = "최종 수정한 사용자")
    var updatedBy: String = ""
        protected set
}
