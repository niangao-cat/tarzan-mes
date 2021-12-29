package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/20 10:14 上午
 */
public class MtProcessWorkingDTO25 implements Serializable {
    private static final long serialVersionUID = -5389210542843653521L;

    @ApiModelProperty("收集组Id")
    private String tagGroupId;
    @ApiModelProperty("收集组编码")
    private String tagGroupCode;
    @ApiModelProperty("收集组描述")
    private String tagGroupDescription;
    @ApiModelProperty("业务类型")
    private String businessType;
    @ApiModelProperty("业务类型描述")
    private String businessTypeDesc;
    @ApiModelProperty("采集状态")
    private String status;
    @ApiModelProperty("采集状态描述")
    private String statusDesc;

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public String getTagGroupDescription() {
        return tagGroupDescription;
    }

    public void setTagGroupDescription(String tagGroupDescription) {
        this.tagGroupDescription = tagGroupDescription;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeDesc() {
        return businessTypeDesc;
    }

    public void setBusinessTypeDesc(String businessTypeDesc) {
        this.businessTypeDesc = businessTypeDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
