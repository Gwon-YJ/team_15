package github.npcamp.teamtaskflow.domain.comment.exception;

import github.npcamp.teamtaskflow.global.exception.CustomException;
import github.npcamp.teamtaskflow.global.exception.ErrorCode;

public class CommentNotFoundException extends CustomException {
    public CommentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}