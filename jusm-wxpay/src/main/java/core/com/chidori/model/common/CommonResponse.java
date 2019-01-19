package core.com.chidori.model.common;

public class CommonResponse {
    private int status;
    private String message;
    private Object content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }
}
