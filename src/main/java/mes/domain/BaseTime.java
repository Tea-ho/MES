package mes.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //AppStart의 @EnableJpaAuditing와 세트
public class BaseTime {
    @CreatedDate
    public LocalDateTime cdate; //생성날짜/시간 [호출하기 위해 public]
    @LastModifiedDate
    public LocalDateTime udate; //수정날짜/시간 [호출하기 위해 public]
}
