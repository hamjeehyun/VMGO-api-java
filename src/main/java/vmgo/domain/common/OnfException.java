package vmgo.domain.common;

import lombok.Data;

@Data
public class OnfException extends RuntimeException{
    private String code;
    private String message;
    private Throwable cause;

    public OnfException(String message) {
        this.message = message;
    }
    public OnfException(String code, String message) {
        this.message = message;
        this.code = code;
    }
    public OnfException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }
}
