package github.npcamp.teamtaskflow.domain.log.exception;

import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActivityLogException extends CustomException {
    public ActivityLogException(ErrorCode errorCode) {
        super(errorCode);
    }
}
