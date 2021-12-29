package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;

/**
 * @author benjamin
 */
public class MtTagGroupDTO3 implements Serializable {
    private static final long serialVersionUID = -2913440511585093326L;

    @ApiModelProperty("数据收集组ID")
    private String tagGroupId;
    @ApiModelProperty(value = "数据收集组编码")
    @NotBlank
    private String tagGroupCode;
    @ApiModelProperty(value = "数据收集组描述")
    private String tagGroupDescription;
    @ApiModelProperty(value = "收集组类型")
    @NotBlank
    private String tagGroupType;
    @ApiModelProperty(value = "来源数据收集组ID")
    private String sourceGroupId;
    @ApiModelProperty(value = "来源数据收集组Code")
    private String sourceGroupCode;
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    @ApiModelProperty(value = "状态")
    @NotBlank
    private String status;
    @ApiModelProperty(value = "数据收集时点")
    @NotBlank
    private String collectionTimeControl;
    @ApiModelProperty(value = "需要用户验证")
    private String userVerification;
    @ApiModelProperty("数据收集组扩展属性")
    private List<MtExtendAttrDTO> tagGroupAttrList;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<MtExtendAttrDTO> getTagGroupAttrList() {
        return tagGroupAttrList;
    }

    public void setTagGroupAttrList(List<MtExtendAttrDTO> tagGroupAttrList) {
        this.tagGroupAttrList = tagGroupAttrList;
    }

    public String getSourceGroupCode() {
        return sourceGroupCode;
    }

    public void setSourceGroupCode(String sourceGroupCode) {
        this.sourceGroupCode = sourceGroupCode;
    }
}
