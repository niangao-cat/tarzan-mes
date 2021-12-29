package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtHoldActualDetailVO8 implements Serializable {

    private static final long serialVersionUID = 6870234855205001672L;
    private String siteId;// 站点ID

    private String objectType;// 对象类型

    private String objectId;// 对象ID

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
}
