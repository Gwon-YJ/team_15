package github.npcamp.teamtaskflow.domain.common.aop;

import github.npcamp.teamtaskflow.domain.log.ActivityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
// 컨트롤러 메서드에 "이 메서드는 어떤 활동이다" 라고 명시해주는 커스텀 어노테이션
public @interface Logging {
    ActivityType value(); // 어노테이션에 넘겨줄 enum 값 (ex -> TASK_CREATED)을 지정하는 것
}
