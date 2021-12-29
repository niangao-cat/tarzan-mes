package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author MrZ
 */
public class MtAssembleControlDTO implements Serializable {

    private static final long serialVersionUID = 7818694866194174737L;
    private String assembleControlId;// 装配控制ID
    private String objectType;// 对象类型
    private String objectId;// 对象ID
    private String organizationType;// 组织类型
    private String organizationId;// 组织对象ID
    private String referenceArea;// 参考区域
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateFrom;// 生效时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateTo;// 失效时间

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
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

    public Date getDateFrom() {
        if (dateFrom == null) {
            return null;
        } else {
            return (Date) dateFrom.clone();
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
        if (dateTo == null) {
            return null;
        } else {
            return (Date) dateTo.clone();
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtAssemblePointDTO5 [assembleControlId=");
        builder.append(assembleControlId);
        builder.append(", objectType=");
        builder.append(objectType);
        builder.append(", objectId=");
        builder.append(objectId);
        builder.append(", organizationType=");
        builder.append(organizationType);
        builder.append(", organizationId=");
        builder.append(organizationId);
        builder.append(", referenceArea=");
        builder.append(referenceArea);
        builder.append(", dateFrom=");
        builder.append(dateFrom);
        builder.append(", dateTo=");
        builder.append(dateTo);
        builder.append("]");
        return builder.toString();
    }



}
