package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModAreaVO1 implements Serializable {


    private static final long serialVersionUID = -1196422856642985716L;

    private String areaCode; // 区域编号
    private String areaName; // 区域名称
    private String description; // 区域描述
    private String enableFlag; // 是否有效
    private String country; // 国家
    private String province; // 省
    private String city; // 市
    private String county; // 县
    private String address; // 除国家省市县的详细地址
    private String areaCategory; // 区域分类，自定义类型，后续考虑基于类型拓展对应属性表


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
