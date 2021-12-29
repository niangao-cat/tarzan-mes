package com.ruike.hme.domain.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname HmeExcWkcRecordHis
 * @Description 工序异常反馈记录历史表
 * @Date 2020-06-22 10:26:26
 * @author xu.deng01@hand-china.com
 */
@ApiModel("工序异常反馈记录历史表")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_exc_wkc_record_his")
@CustomPrimary
public class HmeExcWkcRecordHis extends AuditDomain {

    public static final String FIELD_TENANID = "tenantId";
    public static final String FIELD_EXCEPTION_WKC_RECORD_HIS_ID = "exceptionWkcRecordHisId";
    public static final String FIELD_EXCEPTION_WKC_RECORD_ID = "exceptionWkcRecordId";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_LOCODE = "materialLotCode";
    public static final String FIELD_EQUIPMENID = "equipmentId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_WKC_SHIFID = "wkcShiftId";
    public static final String FIELD_SHIFID = "shiftId";
    public static final String FIELD_EXCEPTION_ID = "exceptionId";
    public static final String FIELD_EXCEPTION_STATUS = "exceptionStatus";
    public static final String FIELD_ATTACHMENUUID = "attachmentUuid";
    public static final String FIELD_EXCEPTION_GROUP_ID = "exceptionGroupId";
    public static final String FIELD_EXCEPTION_LEVEL = "exceptionLevel";
    public static final String FIELD_EXCEPTION_REMARK = "exceptionRemark";
    public static final String FIELD_RESPOND_TIME = "respondTime";
    public static final String FIELD_RESPONDED_BY = "respondedBy";
    public static final String FIELD_RESPOND_REMARK = "respondRemark";
    public static final String FIELD_CLOSE_TIME = "closeTime";
    public static final String FIELD_CLOSED_BY = "closedBy";
    public static final String FIELD_PROD_FLAG = "prodFlag";
    public static final String FIELD_PROD_BATCH_ID = "prodBatchId";
    public static final String FIELD_PROD_DATE = "prodDate";
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

    @ApiModelProperty("租户id")
    private Long tenantId;
    @ApiModelProperty(value = "主键ID，标识唯一一条记录", required = true)
    @Id
    @GeneratedValue
    private String exceptionWkcRecordHisId;
    @ApiModelProperty(value = "工序异常反馈记录ID", required = true)
    @NotBlank
    private String exceptionWkcRecordId;
    @ApiModelProperty(value = "在制品sn")
    private String snNum;
    @ApiModelProperty(value = "在制品eo")
    private String eoId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "异常物料批")
    private String materialLotCode;
    @ApiModelProperty(value = "异常设备ID")
    private String equipmentId;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "班次ID")
    private String wkcShiftId;
    @ApiModelProperty(value = "班组ID")
    private String shiftId;
    @ApiModelProperty(value = "异常ID")
    private String exceptionId;
    @ApiModelProperty(value = "异常状态")
    private String exceptionStatus;
    @ApiModelProperty(value = "附件uuid")
    private String attachmentUuid;
    @ApiModelProperty(value = "异常收集组ID")
    private String exceptionGroupId;
    @ApiModelProperty(value = "异常等级")
    private Long exceptionLevel;
    @ApiModelProperty(value = "异常备注")
    private String exceptionRemark;
    @ApiModelProperty(value = "响应时间")
    private Date respondTime;
    @ApiModelProperty(value = "响应人")
    private String respondedBy;
    @ApiModelProperty(value = "响应备注")
    private String respondRemark;
    @ApiModelProperty(value = "关闭时间")
    private Date closeTime;
    @ApiModelProperty(value = "异常关闭人")
    private Long closedBy;
    @ApiModelProperty(value = "数据处理标识",required = true)
    @NotBlank
    private String prodFlag;
    @ApiModelProperty(value = "处理批次号")
    private String prodBatchId;
    @ApiModelProperty(value = "处理时间")
    private Date prodDate;
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

}
