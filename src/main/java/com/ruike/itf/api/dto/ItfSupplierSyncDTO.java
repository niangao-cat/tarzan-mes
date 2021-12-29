package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * 物料全局替代关系表
 *
 * @author yapeng.yao@hand-china.com 2020/8/18 15:00
 */
@Data
public class ItfSupplierSyncDTO {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "供应商简称")
    private String supplierNameAlt;
    @ApiModelProperty(value = "生效日期从")
    private String supplierDateFrom;
    @ApiModelProperty(value = "失效日期至")
    private String supplierDateTo;
    @ApiModelProperty(value = "供应商类型")
    private String supplierType;
    @ApiModelProperty(value = "供应商地点编号")
    private String supplierSiteCode;
    @ApiModelProperty(value = "供应商详细地址")
    private String supplierSiteAddress;
    @ApiModelProperty(value = "国家")
    private String country;
    @ApiModelProperty(value = "省份")
    private String province;
    @ApiModelProperty(value = "城市")
    private String city;
    @ApiModelProperty(value = "联系电话")
    private String contactPhoneNumber;
    @ApiModelProperty(value = "联系人")
    private String contactPerson;
    @ApiModelProperty(value = "地点生效日期")
    private String siteDateFrom;
    @ApiModelProperty(value = "地点失效日期")
    private String siteDateTo;
    @ApiModelProperty(value = "ERP创建日期")
    private String erpCreationDate;
    @ApiModelProperty(value = "ERP创建人")
    private String erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人")
    private String erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期")
    private String erpLastUpdateDate;

    public ItfSupplierSyncDTO(ItfSupplierDTO dto) {
        this.supplierCode = Strings.isEmpty(dto.getLIFNR()) ? null : dto.getLIFNR();
        this.supplierName = Strings.isEmpty(dto.getNAME1()) ? null : dto.getNAME1();
        this.supplierNameAlt = Strings.isEmpty(dto.getSORTL()) ? null : dto.getSORTL();
        this.supplierSiteCode = Strings.isEmpty(dto.getADDRNUMBER()) ? null : dto.getADDRNUMBER();
        this.supplierSiteAddress = Strings.isEmpty(dto.getSTRAS()) ? null : dto.getSTRAS();
        this.country = Strings.isEmpty(dto.getCOUNTRY()) ? null : dto.getCOUNTRY();
        this.province = Strings.isEmpty(dto.getREGION()) ? null : dto.getREGION();
        this.city = Strings.isEmpty(dto.getORT01()) ? null : dto.getORT01();
        this.contactPhoneNumber = Strings.isEmpty(dto.getTEL_NUMBER()) ? null : dto.getTEL_NUMBER();
        this.contactPerson = Strings.isEmpty(dto.getBUILDING()) ? null : dto.getBUILDING();
        this.siteDateFrom = Strings.isEmpty(dto.getDATE_FROM()) ? null : dto.getDATE_FROM();
        this.siteDateTo = Strings.isEmpty(dto.getDATE_TO()) ? null : dto.getDATE_TO();
        this.erpCreationDate = Strings.isEmpty(dto.getERDAT()) ? null : dto.getERDAT();
    }
}
