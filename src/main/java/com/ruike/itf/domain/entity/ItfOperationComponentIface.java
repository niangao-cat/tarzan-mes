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

/**
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@ApiModel("工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_operation_component_iface")
@CustomPrimary
public class ItfOperationComponentIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_ROUTER_OBJECT_TYPE = "routerObjectType";
    public static final String FIELD_ROUTER_CODE = "routerCode";
    public static final String FIELD_ROUTER_ALTERNATE = "routerAlternate";
    public static final String FIELD_BOM_OBJECT_TYPE = "bomObjectType";
    public static final String FIELD_BOM_CODE = "bomCode";
    public static final String FIELD_BOM_ALTERNATE = "bomAlternate";
    public static final String FIELD_LINE_NUM = "lineNum";
    public static final String FIELD_OPERATION_SEQ_NUM = "operationSeqNum";
    public static final String FIELD_COMPONENT_ITEM_NUM = "componentItemNum";
    public static final String FIELD_BOM_USAGE = "bomUsage";
    public static final String FIELD_UOM = "uom";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_COMPONENT_START_DATE = "componentStartDate";
    public static final String FIELD_COMPONENT_END_DATE = "componentEndDate";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_UPDATE_METHOD = "updateMethod";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

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
    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "ROUTER类型（物料工艺写入：MATERIAL，工单工艺写入：WO）")
    private String routerObjectType;
    @ApiModelProperty(value = "ROUTER编码")
    private String routerCode;
    @ApiModelProperty(value = "ROUTER版本（Oracle将ROUTER替代项写入，SAP将ROUTER计数器写入）")
    private String routerAlternate;
    @ApiModelProperty(value = "BOM类型（物料BOM写入：MATERIAL，工单BOM写入：WO）")
    private String bomObjectType;
    @ApiModelProperty(value = "BOM编码")
    private String bomCode;
    @ApiModelProperty(value = "BOM版本（Oracle将BOM替代项写入，SAP将BOM计数器写入）")
    private String bomAlternate;
    @ApiModelProperty(value = "组件行号")
    private Long lineNum;
    @ApiModelProperty(value = "工序号")
    private String operationSeqNum;
    @ApiModelProperty(value = "组件物料编码")
    private String componentItemNum;
    @ApiModelProperty(value = "组件单位用量")
    private Double bomUsage;
    @ApiModelProperty(value = "组件单位用量")
    private String uom;
    @ApiModelProperty(value = "工序分配组件有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")
    private String enableFlag;
    @ApiModelProperty(value = "组件开始日期")
    private Date componentStartDate;
    @ApiModelProperty(value = "组件结束日期")
    private Date componentEndDate;
    @ApiModelProperty(value = "ERP创建日期")
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人")
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人")
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期")
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "更新方式（允许两个值UPDATE增量，ALL全量）")
    private String updateMethod;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    private Double batchId;
    @ApiModelProperty(value = "数据批次时间")
    private String batchDate;
    @ApiModelProperty(value = "数据处理状态", required = true)
    @NotBlank
    private String status;
    @ApiModelProperty(value = "数据处理消息")
    private String message;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    @Cid
    private Long cid;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

}
