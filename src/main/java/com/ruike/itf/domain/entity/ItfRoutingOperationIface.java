package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * 工艺路线接口表
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@ApiModel("工艺路线接口表")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_routing_operation_iface")
@CustomPrimary
public class ItfRoutingOperationIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_ROUTER_OBJECT_TYPE = "routerObjectType";
    public static final String FIELD_ROUTER_OBJECT_CODE = "routerObjectCode";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_ROUTER_DESCRIPTION = "routerDescription";
    public static final String FIELD_ROUTER_CODE = "routerCode";
    public static final String FIELD_ROUTER_START_DATE = "routerStartDate";
    public static final String FIELD_ROUTER_END_DATE = "routerEndDate";
    public static final String FIELD_ROUTER_STATUS = "routerStatus";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_ROUTING_ALTERNATE = "routingAlternate";
    public static final String FIELD_OPERATION_SEQ_NUM = "operationSeqNum";
    public static final String FIELD_STANDARD_OPERATION_CODE = "standardOperationCode";
    public static final String FIELD_OPERATION_DESCRIPTION = "operationDescription";
    public static final String FIELD_OPERATION_START_DATE = "operationStartDate";
    public static final String FIELD_OPERATION_END_DATE = "operationEndDate";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_UPDATE_METHOD = "updateMethod";
    public static final String FIELD_PROCESS_TIME = "processTime";
    public static final String FIELD_SPECIAL_INTRUCTION = "specialIntruction";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_HEAD_ATTRIBUTE1 = "headAttribute1";
    public static final String FIELD_HEAD_ATTRIBUTE2 = "headAttribute2";
    public static final String FIELD_HEAD_ATTRIBUTE3 = "headAttribute3";
    public static final String FIELD_HEAD_ATTRIBUTE4 = "headAttribute4";
    public static final String FIELD_HEAD_ATTRIBUTE5 = "headAttribute5";
    public static final String FIELD_HEAD_ATTRIBUTE6 = "headAttribute6";
    public static final String FIELD_HEAD_ATTRIBUTE7 = "headAttribute7";
    public static final String FIELD_HEAD_ATTRIBUTE8 = "headAttribute8";
    public static final String FIELD_HEAD_ATTRIBUTE9 = "headAttribute9";
    public static final String FIELD_HEAD_ATTRIBUTE10 = "headAttribute10";
    public static final String FIELD_HEAD_ATTRIBUTE11 = "headAttribute11";
    public static final String FIELD_HEAD_ATTRIBUTE12 = "headAttribute12";
    public static final String FIELD_HEAD_ATTRIBUTE13 = "headAttribute13";
    public static final String FIELD_HEAD_ATTRIBUTE14 = "headAttribute14";
    public static final String FIELD_HEAD_ATTRIBUTE15 = "headAttribute15";
    public static final String FIELD_LINE_ATTRIBUTE1 = "lineAttribute1";
    public static final String FIELD_LINE_ATTRIBUTE2 = "lineAttribute2";
    public static final String FIELD_LINE_ATTRIBUTE3 = "lineAttribute3";
    public static final String FIELD_LINE_ATTRIBUTE4 = "lineAttribute4";
    public static final String FIELD_LINE_ATTRIBUTE5 = "lineAttribute5";
    public static final String FIELD_LINE_ATTRIBUTE6 = "lineAttribute6";
    public static final String FIELD_LINE_ATTRIBUTE7 = "lineAttribute7";
    public static final String FIELD_LINE_ATTRIBUTE8 = "lineAttribute8";
    public static final String FIELD_LINE_ATTRIBUTE9 = "lineAttribute9";
    public static final String FIELD_LINE_ATTRIBUTE10 = "lineAttribute10";
    public static final String FIELD_LINE_ATTRIBUTE11 = "lineAttribute11";
    public static final String FIELD_LINE_ATTRIBUTE12 = "lineAttribute12";
    public static final String FIELD_LINE_ATTRIBUTE13 = "lineAttribute13";
    public static final String FIELD_LINE_ATTRIBUTE14 = "lineAttribute14";
    public static final String FIELD_LINE_ATTRIBUTE15 = "lineAttribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty(value = "ROUTER类型（物料工艺写入：MATERIAL，工单工艺写入：WO）")
    private String routerObjectType;
    @ApiModelProperty(value = "ROUTER对象编码（物料编码或工单号）")
    private String routerObjectCode;
    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "ROUTER说明")
    private String routerDescription;
    @ApiModelProperty(value = "ROUTER编码")
    private String routerCode;
    @ApiModelProperty(value = "ROUTER开始日期")
    private Date routerStartDate;
    @ApiModelProperty(value = "ROUTER结束日期")
    private Date routerEndDate;
    @ApiModelProperty(value = "ROUTER状态（Oracle可不写值，SAP将激活/未激活写入分别对应ACTIVE/UNACTIVE)")
    private String routerStatus;
    @ApiModelProperty(value = "ROUTER有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")
    private String enableFlag;
    @ApiModelProperty(value = "ROUTER版本（Oracle将ROUTER替代项写入，SAP将ROUTER计数器写入）")
    private String routingAlternate;
    @ApiModelProperty(value = "工序号")
    private String operationSeqNum;
    @ApiModelProperty(value = "标准工序编码")
    private String standardOperationCode;
    @ApiModelProperty(value = "工序描述")
    private String operationDescription;
    @ApiModelProperty(value = "工序开始日期")
    private Date operationStartDate;
    @ApiModelProperty(value = "工序结束日期")
    private Date operationEndDate;
    @ApiModelProperty(value = "ERP创建日期")
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人")
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人")
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期")
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    private Double batchId;

    @ApiModelProperty(value = "数据批次时间")
    private String batchDate;

    @ApiModelProperty(value = "数据处理状态", required = true)
    @NotBlank
    private String status;
    @ApiModelProperty(value = "处理方式UPDATE/ALL")
    @Transient
    private String updateMethod;
    @Transient
    private Double processTime;
    @Transient
    private String specialIntruction;
    @ApiModelProperty(value = "数据处理消息")
    private String message;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    @Cid
    private Long cid;
    @ApiModelProperty(value = "")
    private String headAttribute1;
    @ApiModelProperty(value = "")
    private String headAttribute2;
    @ApiModelProperty(value = "")
    private String headAttribute3;
    @ApiModelProperty(value = "")
    private String headAttribute4;
    @ApiModelProperty(value = "")
    private String headAttribute5;
    @ApiModelProperty(value = "")
    private String headAttribute6;
    @ApiModelProperty(value = "")
    private String headAttribute7;
    @ApiModelProperty(value = "")
    private String headAttribute8;
    @ApiModelProperty(value = "")
    private String headAttribute9;
    @ApiModelProperty(value = "")
    private String headAttribute10;
    @ApiModelProperty(value = "")
    private String headAttribute11;
    @ApiModelProperty(value = "")
    private String headAttribute12;
    @ApiModelProperty(value = "")
    private String headAttribute13;
    @ApiModelProperty(value = "")
    private String headAttribute14;
    @ApiModelProperty(value = "")
    private String headAttribute15;
    @ApiModelProperty(value = "")
    private String lineAttribute1;
    @ApiModelProperty(value = "")
    private String lineAttribute2;
    @ApiModelProperty(value = "")
    private String lineAttribute3;
    @ApiModelProperty(value = "")
    private String lineAttribute4;
    @ApiModelProperty(value = "")
    private String lineAttribute5;
    @ApiModelProperty(value = "")
    private String lineAttribute6;
    @ApiModelProperty(value = "")
    private String lineAttribute7;
    @ApiModelProperty(value = "")
    private String lineAttribute8;
    @ApiModelProperty(value = "")
    private String lineAttribute9;
    @ApiModelProperty(value = "")
    private String lineAttribute10;
    @ApiModelProperty(value = "")
    private String lineAttribute11;
    @ApiModelProperty(value = "")
    private String lineAttribute12;
    @ApiModelProperty(value = "")
    private String lineAttribute13;
    @ApiModelProperty(value = "")
    private String lineAttribute14;
    @ApiModelProperty(value = "")
    private String lineAttribute15;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
