package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 送货单接口头表
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@ApiModel("送货单接口头表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_delivery_doc_iface")
@Data
public class ItfDeliveryDocIface extends AuditDomain {

    public static final String FIELD_INTERFACE_HEADER_ID = "interfaceHeaderId";
    public static final String FIELD_INSTRUCTION_DOC_NUM = "instructionDocNum";
    public static final String FIELD_INSTRUCTION_DOC_TYPE = "instructionDocType";
    public static final String FIELD_INSTRUCTION_DOC_TYPE_CODE = "instructionDocTypeCode";
    public static final String FIELD_INSTRUCTION_DOC_STATUS = "instructionDocStatus";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_DEMAND_TIME = "demandTime";
    public static final String FIELD_EXPECTED_ARRIVAL_TIME = "expectedArrivalTime";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
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


    @ApiModelProperty("指令头表ID，主键")
    @Id
    @GeneratedValue
    private String interfaceHeaderId;
    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型（业务类型，由项目定义）")
    private String instructionDocType;
    @ApiModelProperty(value = "单据类型编码")
    private String instructionDocTypeCode;
    @ApiModelProperty(value = "状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）")
    private String instructionDocStatus;
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "需求时间")
    private String demandTime;
    @ApiModelProperty(value = "预计送达时间")
    private String expectedArrivalTime;
    @ApiModelProperty(value = "处理时间", required = true)
    @NotNull
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "")
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

    //
    // getter/setter
    // ------------------------------------------------------------------------------

}
