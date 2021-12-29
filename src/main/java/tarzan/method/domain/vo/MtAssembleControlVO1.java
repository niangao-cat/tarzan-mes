package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleControlVO1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5204850484419247273L;
    private String objectType; // 对象类型，包括MATERIAL、WO、EO
    private String objectId; // 对象的具体值
    private String organizationType; // 组织类型，包括SITE/PRODUCTION_LINE/WKC
    private String organizationId; // 组织的具体值
    private String referenceArea; // 装配参考区域，针对多个产品一起装配或者一个产品拆分为多次装配的情况

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

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getReferenceArea() {
        return referenceArea;
    }

    public void setReferenceArea(String referenceArea) {
        this.referenceArea = referenceArea;
    }

}
