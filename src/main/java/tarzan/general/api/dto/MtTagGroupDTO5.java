package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtTagGroupDTO5 implements Serializable {
    private static final long serialVersionUID = -1584735379413830966L;

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
    @ApiModelProperty(value = "业务类型")
    @NotBlank
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
    private List<MtExtendAttrDTO3> tagGroupAttrList;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();
    @ApiModelProperty("数据收集组分配数据项信息")
    private List<MtTagGroupAssignDTO> mtTagGroupAssignDTO;
    @ApiModelProperty("数据收集组关联对象信息")
    private MtTagGroupObjectDTO2 mtTagGroupObjectDTO;

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

    public List<MtExtendAttrDTO3> getTagGroupAttrList() {
        return tagGroupAttrList;
    }

    public void setTagGroupAttrList(List<MtExtendAttrDTO3> tagGroupAttrList) {
        this.tagGroupAttrList = tagGroupAttrList;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public List<MtTagGroupAssignDTO> getMtTagGroupAssignDTO() {
        return mtTagGroupAssignDTO;
    }

    public void setMtTagGroupAssignDTO(List<MtTagGroupAssignDTO> mtTagGroupAssignDTO) {
        this.mtTagGroupAssignDTO = mtTagGroupAssignDTO;
    }

    public MtTagGroupObjectDTO2 getMtTagGroupObjectDTO() {
        return mtTagGroupObjectDTO;
    }

    public void setMtTagGroupObjectDTO(MtTagGroupObjectDTO2 mtTagGroupObjectDTO) {
        this.mtTagGroupObjectDTO = mtTagGroupObjectDTO;
    }
}
