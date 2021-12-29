package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtHoldActualDetailVO7 implements Serializable {

    private static final long serialVersionUID = -3575641339240934943L;

    private String holdDetailId;// 保留实绩明细ID
    private String objectType;// 对象类型
    private String objectId;// 对象ID
    private String originalStatus;// 初始状态

    public String getHoldDetailId() {
        return holdDetailId;
    }

    public void setHoldDetailId(String holdDetailId) {
        this.holdDetailId = holdDetailId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(String originalStatus) {
        this.originalStatus = originalStatus;
    }

}
