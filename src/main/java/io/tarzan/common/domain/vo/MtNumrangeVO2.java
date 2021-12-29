package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Leeloing
 * @date 2019/6/25 11:05
 */
public class MtNumrangeVO2 implements Serializable {
    private static final long serialVersionUID = -8930679690094260861L;
    private String objectCode;
    private String objectTypeId;
    private String objectTypeCode;
    private String siteId;
    private String outsideNum;
    private Map<String, String> callObjectCodeList;
    private List<String> incomingValueList;

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public Map<String, String> getCallObjectCodeList() {
        return callObjectCodeList;
    }

    public void setCallObjectCodeList(Map<String, String> callObjectCodeList) {
        this.callObjectCodeList = callObjectCodeList;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
