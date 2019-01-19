package core.com.chidori.model.common;

public class ChidoriException extends RuntimeException {

    private int errorCode;
    private String errorMessage;

    private Object payload;

    public Object getPayload() {
        return payload;
    }

    public ChidoriException(int errorCode) {
        this.errorCode = errorCode;
    }

    public ChidoriException(int errorCode, Object payload) {
        this.errorCode = errorCode;
        this.payload = payload;
    }

    public ChidoriException(int errorCode, Object payload, String errorMessage) {
        this.errorCode = errorCode;
        this.payload = payload;
        this.errorMessage = errorMessage;
    }

    public ChidoriException() {
        super();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
