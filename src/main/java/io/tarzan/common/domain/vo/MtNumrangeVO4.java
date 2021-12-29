package io.tarzan.common.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/6/25 16:53
 */
public class MtNumrangeVO4 implements Serializable {

    private static final long serialVersionUID = 8195690916029505604L;
    private String objectId;
    private String objectCode;
    private String numrangeGroup;
    private String numDescription;
    private String outsideNumFlag;
    private String enableFlag;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getNumrangeGroup() {
        return numrangeGroup;
    }

    public void setNumrangeGroup(String numrangeGroup) {
        this.numrangeGroup = numrangeGroup;
    }

    public String getNumDescription() {
        return numDescription;
    }

    public void setNumDescription(String numDescription) {
        this.numDescription = numDescription;
    }

    public String getOutsideNumFlag() {
        return outsideNumFlag;
    }

    public void setOutsideNumFlag(String outsideNumFlag) {
        this.outsideNumFlag = outsideNumFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
