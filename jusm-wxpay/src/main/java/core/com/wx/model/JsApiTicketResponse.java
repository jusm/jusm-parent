package core.com.wx.model;

public class JsApiTicketResponse {
    private int errcode;
    private String errmsg;
    private Integer expires_in;
    private String ticket;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "JsApiTicketResponse{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", expires_in=" + expires_in +
                ", ticket='" + ticket + '\'' +
                '}';
    }
}
