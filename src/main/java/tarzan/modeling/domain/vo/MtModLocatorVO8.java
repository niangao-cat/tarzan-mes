package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtModLocatorVO8
 * @description
 * @date 2019年09月29日 15:45
 */
public class MtModLocatorVO8 implements Serializable {
    private static final long serialVersionUID = 4309834349193761716L;

    private String locatorId;// 库位ID
    private String locatorCode;// 库位编码
    private String locatorName;// 库位名称
    private String locatorLocation;// 库位位置
    private String locatorType;// 库位类型
    private String locatorGroupId;// 库位组ID
    private String locatorGroupCode;// 库位组编码
    private String locatorGroupName;// 库位组名称
    private Double length;// 长
    private Double width;// 宽
    private Double height;// 高
    private String sizeUomId;// 尺寸单位ID
    private String sizeUomCode;// 尺寸单位编码
    private String sizeUomName;// 尺寸单位名称
    private Double maxWeight;// 最大重量
    private String weightUomId;// 重量单位ID
    private String weightUomCode;// 重量单位编码
    private String weightUomName;// 重量单位名称
    private Double maxCapacity;// 最大容量
    private String enableFlag;// 有效性
    private String parentLocatorId;// 父层库位ID
    private String parentLocatorCode;// 父层库位编码
    private String parentLocatorName;// 父层库位名称
    private String locatorCategory;// 库位类别
    private String negativeFlag;// 允许负库存标识

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

    public String getLocatorGroupCode() {
        return locatorGroupCode;
    }

    public void setLocatorGroupCode(String locatorGroupCode) {
        this.locatorGroupCode = locatorGroupCode;
    }

    public String getLocatorGroupName() {
        return locatorGroupName;
    }

    public void setLocatorGroupName(String locatorGroupName) {
        this.locatorGroupName = locatorGroupName;
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

    public String getSizeUomCode() {
        return sizeUomCode;
    }

    public void setSizeUomCode(String sizeUomCode) {
        this.sizeUomCode = sizeUomCode;
    }

    public String getSizeUomName() {
        return sizeUomName;
    }

    public void setSizeUomName(String sizeUomName) {
        this.sizeUomName = sizeUomName;
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

    public String getWeightUomCode() {
        return weightUomCode;
    }

    public void setWeightUomCode(String weightUomCode) {
        this.weightUomCode = weightUomCode;
    }

    public String getWeightUomName() {
        return weightUomName;
    }

    public void setWeightUomName(String weightUomName) {
        this.weightUomName = weightUomName;
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

    public String getParentLocatorCode() {
        return parentLocatorCode;
    }

    public void setParentLocatorCode(String parentLocatorCode) {
        this.parentLocatorCode = parentLocatorCode;
    }

    public String getParentLocatorName() {
        return parentLocatorName;
    }

    public void setParentLocatorName(String parentLocatorName) {
        this.parentLocatorName = parentLocatorName;
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

}
