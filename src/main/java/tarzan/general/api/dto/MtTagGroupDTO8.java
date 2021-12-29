package tarzan.general.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: batch-update VO
 * @Date: 2019/9/18 20:16
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtTagGroupDTO8 implements Serializable {
    private static final long serialVersionUID = -2941199970831742712L;

    @ApiModelProperty(value = "数据收集组编码")
    @NotNull
    private String tagGroupCode;
    @ApiModelProperty(value = "收集组描述")
    @NotNull
    private String tagGroupDescription;
    @ApiModelProperty(value = "状态")
    @NotNull
    private String status;
    @ApiModelProperty(value = "收集组类型")
    @NotNull
    private String tagGroupType;
    @ApiModelProperty(value = "来源数据收集组ID")
    private String sourceGroupId;
    @ApiModelProperty(value = "业务类型")
    @NotNull
    private String businessType;
    @ApiModelProperty(value = "数据收集时点")
    @NotNull
    private String collectionTimeControl;
    @ApiModelProperty(value = "需要用户验证")
    @NotNull
    private String userVerification;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagGroupType() {
        return tagGroupType;
    }

    public void setTagGroupType(String tagGroupType) {
        this.tagGroupType = tagGroupType;
    }

    public String getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(String sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCollectionTimeControl() {
        return collectionTimeControl;
    }

    public void setCollectionTimeControl(String collectionTimeControl) {
        this.collectionTimeControl = collectionTimeControl;
    }

    public String getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(String userVerification) {
        this.userVerification = userVerification;
    }
}
