package com.ruike.itf.domain.entity;

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
import org.hzero.mybatis.common.query.Where;

/**
 * BOM接口表
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@ApiModel("BOM接口表")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_bom_component_iface")
@CustomPrimary
public class ItfBomComponentIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_BOM_CODE = "bomCode";
    public static final String FIELD_BOM_ALTERNATE = "bomAlternate";
    public static final String FIELD_BOM_DESCRIPTION = "bomDescription";
    public static final String FIELD_BOM_START_DATE = "bomStartDate";
    public static final String FIELD_BOM_END_DATE = "bomEndDate";
    public static final String FIELD_BOM_STATUS = "bomStatus";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_BOM_OBJECT_TYPE = "bomObjectType";
    public static final String FIELD_BOM_OBJECT_CODE = "bomObjectCode";
    public static final String FIELD_STANDARD_QTY = "standardQty";
    public static final String FIELD_COMPONENT_LINE_NUM = "componentLineNum";
    public static final String FIELD_COMPONENT_ITEM_CODE = "componentItemCode";
    public static final String FIELD_OPERATION_SEQUENCE = "operationSequence";
    public static final String FIELD_BOM_USAGE = "bomUsage";
    public static final String FIELD_COMPONENT_SHRINKAGE = "componentShrinkage";
    public static final String FIELD_WIP_SUPPLY_TYPE = "wipSupplyType";
    public static final String FIELD_COMPONENT_START_DATE = "componentStartDate";
    public static final String FIELD_COMPONENT_END_DATE = "componentEndDate";
    public static final String FIELD_SUBSTITUTE_ITEM_CODE = "substituteItemCode";
    public static final String FIELD_SUBSTITUTE_ITEM_USAGE = "substituteItemUsage";
    public static final String FIELD_SUBSTITUTE_GROUP = "substituteGroup";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_ISSUE_LOCATOR_CODE = "issueLocatorCode";
    public static final String FIELD_ASSEMBLE_METHOD = "assembleMethod";
    public static final String FIELD_BOM_COMPONENT_TYPE = "bomComponentType";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_UPDATE_METHOD = "updateMethod";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
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
    public static final String FIELD_LINE_ATTRIBUTE16 = "lineAttribute16";
    public static final String FIELD_LINE_ATTRIBUTE17 = "lineAttribute17";
    public static final String FIELD_LINE_ATTRIBUTE18 = "lineAttribute18";
    public static final String FIELD_LINE_ATTRIBUTE19 = "lineAttribute19";
    public static final String FIELD_LINE_ATTRIBUTE20 = "lineAttribute20";
    public static final String FIELD_LINE_ATTRIBUTE21 = "lineAttribute21";
    public static final String FIELD_LINE_ATTRIBUTE22 = "lineAttribute22";
    public static final String FIELD_LINE_ATTRIBUTE23 = "lineAttribute23";
    public static final String FIELD_LINE_ATTRIBUTE24 = "lineAttribute24";
    public static final String FIELD_LINE_ATTRIBUTE25 = "lineAttribute25";
    public static final String FIELD_LINE_ATTRIBUTE26 = "lineAttribute26";
    public static final String FIELD_LINE_ATTRIBUTE27 = "lineAttribute27";
    public static final String FIELD_LINE_ATTRIBUTE28 = "lineAttribute28";
    public static final String FIELD_LINE_ATTRIBUTE29 = "lineAttribute29";
    public static final String FIELD_LINE_ATTRIBUTE30 = "lineAttribute30";

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
    @Where
    private String ifaceId;
    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "BOM编码")
    private String bomCode;
    @ApiModelProperty(value = "BOM版本（Oracle将BOM替代项写入，SAP将BOM计数器写入）")
    private String bomAlternate;
    @ApiModelProperty(value = "BOM说明")
    private String bomDescription;
    @ApiModelProperty(value = "BOM开始日期")
    private Date bomStartDate;
    @ApiModelProperty(value = "BOM结束日期")
    private Date bomEndDate;
    @ApiModelProperty(value = "BOM状态（Oracle可不写值，SAP将激活/未激活写入分别对应ACTIVE/UNACTIVE)")
    private String bomStatus;
    @ApiModelProperty(value = "BOM有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")
    private String enableFlag;
    @ApiModelProperty(value = "BOM类型（物料BOM写入：MATERIAL，工单BOM写入：WO）")
    private String bomObjectType;
    @ApiModelProperty(value = "BOM对象编码（物料编码或工单号）")
    private String bomObjectCode;
    @ApiModelProperty(value = "基准数量（Oracle物料BOM写入1，工单BOM可直接写入工单数量）")
    private Double standardQty;
    @ApiModelProperty(value = "组件行号")
    private Long componentLineNum;
    @ApiModelProperty(value = "组件物料编码")
    private String componentItemCode;
    @ApiModelProperty(value = "工序号")
    private String operationSequence;
    @ApiModelProperty(value = "组件单位用量")
    private Double bomUsage;
    @ApiModelProperty(value = "组件损耗率")
    private Double componentShrinkage;
    @ApiModelProperty(value = "组件发料类型（1推式，2，3拉式，6虚拟件）")
    private String wipSupplyType;
    @ApiModelProperty(value = "组件开始日期")
    private Date componentStartDate;
    @ApiModelProperty(value = "组件结束日期")
    private Date componentEndDate;
    @ApiModelProperty(value = "替代组件物料编码")
    private String substituteItemCode;
    @ApiModelProperty(value = "替代组件单位用量")
    private Double substituteItemUsage;
    @ApiModelProperty(value = "替代组")
    private String substituteGroup;
    @ApiModelProperty(value = "ERP创建日期")
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人")
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人")
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期")
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "")
    private String issueLocatorCode;
    @ApiModelProperty(value = "")
    private String assembleMethod;
    @ApiModelProperty(value = "")
    private String bomComponentType;
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
    @ApiModelProperty(value = "")
    private String updateMethod;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    @Cid
    private Long cid;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long objectVersionNumber;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long createdBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date creationDate;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date lastUpdateDate;
    @ApiModelProperty(value = "")
    private String attributeCategory;
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
    @ApiModelProperty(value = "")
    private String lineAttribute16;
    @ApiModelProperty(value = "")
    private String lineAttribute17;
    @ApiModelProperty(value = "")
    private String lineAttribute18;
    @ApiModelProperty(value = "")
    private String lineAttribute19;
    @ApiModelProperty(value = "")
    private String lineAttribute20;
    @ApiModelProperty(value = "")
    private String lineAttribute21;
    @ApiModelProperty(value = "")
    private String lineAttribute22;
    @ApiModelProperty(value = "")
    private String lineAttribute23;
    @ApiModelProperty(value = "")
    private String lineAttribute24;
    @ApiModelProperty(value = "")
    private String lineAttribute25;
    @ApiModelProperty(value = "")
    private String lineAttribute26;
    @ApiModelProperty(value = "")
    private String lineAttribute27;
    @ApiModelProperty(value = "")
    private String lineAttribute28;
    @ApiModelProperty(value = "")
    private String lineAttribute29;
    @ApiModelProperty(value = "")
    private String lineAttribute30;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
