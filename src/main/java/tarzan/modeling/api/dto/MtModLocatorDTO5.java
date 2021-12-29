package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * Created by slj on 2018-12-03.
 */
public class MtModLocatorDTO5 implements Serializable {

    private static final long serialVersionUID = -7605514915514993024L;

    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String locatorId;
    
    @ApiModelProperty(value = "库位编码",required = true)
    @NotBlank
    private String locatorCode;
    
    @ApiModelProperty(value = "库位名称",required = true)
    @NotBlank
    private String locatorName;
    
    @ApiModelProperty(value = "货位位置")
    private String locatorLocation;
    
    @ApiModelProperty(value = "库位类型",required = true)
    @NotBlank
    private String locatorType;
    
    @ApiModelProperty(value = "所属库位组ID")
    private String locatorGroupId;
    
    @ApiModelProperty(value = "长")
    private Double length;
    
    @ApiModelProperty(value = "宽")
    private Double width;
    
    @ApiModelProperty(value = "高")
    private Double height;
    
    @ApiModelProperty(value = "货位的尺寸单位")
    private String sizeUomId;
    
    @ApiModelProperty(value = "最大载重重量")
    private Double maxWeight;
    
    @ApiModelProperty(value = "重量单位")
    private String weightUomId;
    
    @ApiModelProperty(value = "容量")
    private Double maxCapacity;
    
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
    
    @ApiModelProperty(value = "父层库位ID")
    private String parentLocatorId;
    
    @ApiModelProperty(value = "库位类别，区域/库存/地点类别",required = true)
    @NotBlank
    private String locatorCategory;
    
    @ApiModelProperty(value = "是否允许负库存，标识库位库存是否允许为负值",required = true)
    @NotBlank
    private String negativeFlag;
    
    @ApiModelProperty("库位扩展属性")
    private List<MtExtendAttrDTO3> locatorAttrList;
    
    @ApiModelProperty(hidden = true)
    private Map<String, Map<String, String>> _tls;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }

    public String getLocatorLocation() {
        return locatorLocation;
    }

    public void setLocatorLocation(String locatorLocation) {
        this.locatorLocation = locatorLocation;
    }

    public String getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(String locatorType) {
        this.locatorType = locatorType;
    }

    public String getLocatorGroupId() {
        return locatorGroupId;
    }

    public void setLocatorGroupId(String locatorGroupId) {
        this.locatorGroupId = locatorGroupId;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getSizeUomId() {
        return sizeUomId;
    }

    public void setSizeUomId(String sizeUomId) {
        this.sizeUomId = sizeUomId;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    public Double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getParentLocatorId() {
        return parentLocatorId;
    }

    public void setParentLocatorId(String parentLocatorId) {
        this.parentLocatorId = parentLocatorId;
    }

    public String getLocatorCategory() {
        return locatorCategory;
    }

    public void setLocatorCategory(String locatorCategory) {
        this.locatorCategory = locatorCategory;
    }

    public String getNegativeFlag() {
        return negativeFlag;
    }

    public void setNegativeFlag(String negativeFlag) {
        this.negativeFlag = negativeFlag;
    }
    
    public List<MtExtendAttrDTO3> getLocatorAttrList() {
        return locatorAttrList;
    }

    public void setLocatorAttrList(List<MtExtendAttrDTO3> locatorAttrList) {
        this.locatorAttrList = locatorAttrList;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
    
}