package io.tarzan.common.domain.vo;

import java.io.Serializable;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/17 10:24
 */
public class MtNumrangeObjectVO1 implements Serializable {

    private static final long serialVersionUID = -4256255411791289998L;
    private String objectCode;// 编码对象编码
    private String objectName;// objectName
    private String description;// description
    private String enableFlag;// 是否有效
    private String objectId;// 编码对象ID

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
