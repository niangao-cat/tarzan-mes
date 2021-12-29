package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;

/**
 * 区域维护-查询 使用DTO(包含计划属性、采购属性)
 *
 * @author benjamin
 */
public class MtModAreaDTO2 implements Serializable {
    private static final long serialVersionUID = 4844700939964802693L;

    @ApiModelProperty(value = "区域Id")
    private String areaId;

    @ApiModelProperty(value = "区域编号(数据修改时必输)")
    @NotBlank
    private String areaCode;

    @ApiModelProperty(value = "区域短描述(数据修改时必输)")
    @NotBlank
    private String areaName;

    @ApiModelProperty(value = "区域长描述")
    private String description;

    @ApiModelProperty(value = "是否有效(数据修改时必输)")
    @NotBlank
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

    @ApiModelProperty(value = "区域采购属性")
    private MtModAreaPurchaseDTO mtModAreaPurchaseDTO;

    @ApiModelProperty(value = "区域计划属性")
    private MtModAreaScheduleDTO mtModAreaScheduleDTO;

    @ApiModelProperty("区域扩展属性")
    private List<MtExtendAttrDTO> areaAttrList;


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

    public MtModAreaPurchaseDTO getMtModAreaPurchaseDTO() {
        if (this.mtModAreaPurchaseDTO == null) {
            return new MtModAreaPurchaseDTO();
        }
        return mtModAreaPurchaseDTO;
    }

    public void setMtModAreaPurchaseDTO(MtModAreaPurchaseDTO mtModAreaPurchaseDTO) {
        this.mtModAreaPurchaseDTO = mtModAreaPurchaseDTO;
    }

    public MtModAreaScheduleDTO getMtModAreaScheduleDTO() {
        if (this.mtModAreaScheduleDTO == null) {
            return new MtModAreaScheduleDTO();
        }
        return mtModAreaScheduleDTO;
    }

    public void setMtModAreaScheduleDTO(MtModAreaScheduleDTO mtModAreaScheduleDTO) {
        this.mtModAreaScheduleDTO = mtModAreaScheduleDTO;
    }

    public List<MtExtendAttrDTO> getAreaAttrList() {
        return areaAttrList;
    }

    public void setAreaAttrList(List<MtExtendAttrDTO> areaAttrList) {
        this.areaAttrList = areaAttrList;
    }

    @Override
    public String toString() {
        return "MtModAreaDTO3{" + "areaId='" + areaId + '\'' + ", areaCode='" + areaCode + '\'' + ", areaName='"
                        + areaName + '\'' + ", description='" + description + '\'' + ", enableFlag='" + enableFlag
                        + '\'' + ", country='" + country + '\'' + ", province='" + province + '\'' + ", city='" + city
                        + '\'' + ", county='" + county + '\'' + ", address='" + address + '\'' + ", areaCategory='"
                        + areaCategory + '\'' + ", mtModAreaPurchaseDTO=" + mtModAreaPurchaseDTO
                        + ", mtModAreaScheduleDTO=" + mtModAreaScheduleDTO + ", areaAttrList=" + areaAttrList + '}';
    }
}
