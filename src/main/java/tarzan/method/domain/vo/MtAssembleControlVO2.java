package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author peng.yuan
 * @ClassName MtAssembleControlVO2
 * @description
 * @date 2019年10月10日 16:22
 */
public class MtAssembleControlVO2 implements Serializable {
    private static final long serialVersionUID = 6018087626932817988L;

    private String objectType;// 对象类型
    private String objectId;// 对象ID
    private String organizationType;// 组织类型
    private String organizationId;// 组织ID
    private String referenceArea;// 参考区域
    private String assembleControlId;// 装配控制ID
    private Date dateFrom;// 失效时间从
    private Date dateTo;// 失效时间至

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

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    public Date getDateFrom() {
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }
}
