package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtModAreaDTO implements Serializable {
    private static final long serialVersionUID = -2488153963636092947L;

    @ApiModelProperty(value = "区域Id")
    private String areaId;

    @ApiModelProperty(value = "区域编号(数据修改时必输)")
    private String areaCode;

    @ApiModelProperty(value = "区域短描述(数据修改时必输)")
    private String areaName;

    @ApiModelProperty(value = "区域长描述")
    private String description;

    @ApiModelProperty(value = "是否有效(数据修改时必输)")
    private String enableFlag;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "县")
    private String county;

    @ApiModelProperty(value = "除国家省市县的详细地址")
    private String address;

    @ApiModelProperty(value = "区域分类，自定义类型，后续考虑基于类型拓展对应属性表")
    private String areaCategory;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCategory() {
        return areaCategory;
    }

    public void setAreaCategory(String areaCategory) {
        this.areaCategory = areaCategory;
    }
}
