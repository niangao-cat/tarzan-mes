package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import tarzan.modeling.domain.entity.MtModLocator;

/**
 * Created by slj on 2018-12-03.
 */
public class MtModLocatorVO3 extends MtModLocator implements Serializable {

    private static final long serialVersionUID = 1017905985507558435L;
    
    @ApiModelProperty(value = "类型描述")
    private String typeDesc;
    
    @ApiModelProperty(value = "库位组编码")
    private String locatorGroupCode;
    
    @ApiModelProperty(value = "尺寸单位编码")
    private String sizeUomCode;
    
    @ApiModelProperty(value = "重量单位编码")
    private String weightUomCode;
    
    @ApiModelProperty(value = "尺寸单位描述")
    private String sizeUomDesc;
    
    @ApiModelProperty(value = "重量单位描述")
    private String weightUomDesc;
    
    @ApiModelProperty(value = "库位组名称")
    private String locatorGroupName;

    @ApiModelProperty(value = "扩展字段")
    private List<MtExtendAttrDTO> locatorAttrList;


    public List<MtExtendAttrDTO> getLocatorAttrList() {
        return locatorAttrList;
    }

    public void setLocatorAttrList(List<MtExtendAttrDTO> locatorAttrList) {
        this.locatorAttrList = locatorAttrList;
    }

    public String getLocatorGroupCode() {
        return locatorGroupCode;
    }

    public void setLocatorGroupCode(String locatorGroupCode) {
        this.locatorGroupCode = locatorGroupCode;
    }

    public String getSizeUomCode() {
        return sizeUomCode;
    }

    public void setSizeUomCode(String sizeUomCode) {
        this.sizeUomCode = sizeUomCode;
    }

    public String getWeightUomCode() {
        return weightUomCode;
    }

    public void setWeightUomCode(String weightUomCode) {
        this.weightUomCode = weightUomCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getSizeUomDesc() {
        return sizeUomDesc;
    }

    public void setSizeUomDesc(String sizeUomDesc) {
        this.sizeUomDesc = sizeUomDesc;
    }

    public String getWeightUomDesc() {
        return weightUomDesc;
    }

    public void setWeightUomDesc(String weightUomDesc) {
        this.weightUomDesc = weightUomDesc;
    }

    public String getLocatorGroupName() {
        return locatorGroupName;
    }

    public void setLocatorGroupName(String locatorGroupName) {
        this.locatorGroupName = locatorGroupName;
    }
}



