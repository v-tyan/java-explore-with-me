package ru.practicum.ewm.model.comments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentState {
    PUBLISHED("PUBLISHED"),
    REJECTED("REJECTED"),
    PENDING("PENDING");

    private final String state;

    public static CommentState fromState(String state) {
        for (CommentState commentState : CommentState.values()) {
            if (commentState.getState().equals(state)) {
                return commentState;
            }
        }
        return null;
    }
}
