package tarzan.material.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * 单位维护-保存拓展属性 使用DTO
 * 
 * @author benjamin
 */
public class MtUomDTO7 implements Serializable {
    private static final long serialVersionUID = -1011423355605225188L;

    @ApiModelProperty("单位Id")
    private String uomId;

    @ApiModelProperty("单位扩展属性")
    private List<MtExtendAttrDTO3> uomAttrList;

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public List<MtExtendAttrDTO3> getUomAttrList() {
        return uomAttrList;
    }

    public void setUomAttrList(List<MtExtendAttrDTO3> uomAttrList) {
        this.uomAttrList = uomAttrList;
    }
}
