package entities;

import dto.Status;

public class ResponseError {
    private final Status status;
    private final String code;
    private final String message;

    public ResponseError(Status status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
