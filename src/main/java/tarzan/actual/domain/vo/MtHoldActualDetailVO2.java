package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtHoldActualDetailVO2 implements Serializable {


    private static final long serialVersionUID = -6686847413241118633L;

    private String objectType;// 对象类型

    private String objectId;// 对象ID

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
