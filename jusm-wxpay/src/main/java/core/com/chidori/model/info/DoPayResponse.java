package core.com.chidori.model.info;

import java.util.Map;

public class DoPayResponse {
    private String recordOrderGid;
    private String prepayId;
    private Map<String, String> resultMap;

    public Map<String, String> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getRecordOrderGid() {
        return recordOrderGid;
    }

    public void setRecordOrderGid(String recordOrderGid) {
        this.recordOrderGid = recordOrderGid;
    }
}
