package github.npcamp.teamtaskflow.domain.comment.exception;

import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;

public class CommentException extends CustomException {
    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}