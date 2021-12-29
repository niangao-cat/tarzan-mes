package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import com.ruike.itf.api.dto.ItfLogisticsServiceDTO;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 售后信息回传ERP接口记录表
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 10:49:32
 */
@ApiModel("售后信息回传ERP接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_logistics_service_rece_iface")
@Data
public class ItfLogisticsServiceReceIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LOGISTICS_INFO_ID = "logisticsInfoId";
    public static final String FIELD_LOGISTICS_NUMBER = "logisticsNumber";
    public static final String FIELD_LOGISTICS_COMPANY = "logisticsCompany";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_AREA_CODE = "areaCode";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_IS_FLAG = "isFlag";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CREATED_BY_NAME = "createdByName";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("租户ID")
    @Id
    @GeneratedValue
    private Long tenantId;
    @ApiModelProperty(value = "物流信息表主键")
    private String logisticsInfoId;
    @ApiModelProperty(value = "物流单号")
    private String logisticsNumber;
    @ApiModelProperty(value = "物流公司")
    private String logisticsCompany;
    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "部门")
    private String areaCode;
    @ApiModelProperty(value = "机器编码")
    private String snNum;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "Y/N是否发送成功")
    private String isFlag;
    @ApiModelProperty(value = "错误信息，无错为空")
    private String message;
    @ApiModelProperty(value = "收件人")
    private String createdByName;
    @ApiModelProperty(value = "CID")
    private Long cid;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "登记日期")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


    public ItfLogisticsServiceReceIface(ItfLogisticsServiceDTO dto, Long tenantId) {
        this.tenantId = tenantId;
        this.logisticsNumber = dto.getBOLNR();
        this.logisticsCompany = dto.getZWL();
        this.plantCode = dto.getWERKS();
        this.areaCode = dto.getPRCTR();
        this.snNum = dto.getSERNR1();
        this.materialCode = dto.getMATNR1();
        this.isFlag = dto.getZFLAG();
        this.message = dto.getZMESSAGE();
        this.createdByName = dto.getZSJR();
        this.attribute1 = dto.getDATUM();
        this.cid = 1L;
    }

    public ItfLogisticsServiceReceIface() {
    }
}
