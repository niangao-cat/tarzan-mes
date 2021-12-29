package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * 设备管理任务单表
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:11
 */
@ApiModel("设备管理任务单表")
@VersionAudit
@ModifyAudit
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_eq_manage_task_doc")
@CustomPrimary
public class HmeEqManageTaskDoc extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TASK_DOC_ID = "taskDocId";
    public static final String FIELD_DOC_NUM = "docNum";
    public static final String FIELD_DOC_TYPE = "docType";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_EQUIPMENT_ID = "equipmentId";
    public static final String FIELD_DOC_STATUS = "docStatus";
    public static final String FIELD_TASK_CYCLE = "taskCycle";
    public static final String FIELD_CHECK_RESULT = "checkResult";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_CHECK_DATE = "checkDate";
    public static final String FIELD_WKC_ID = "wkcId";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_CHECK_BY = "checkBy";
    public static final String FIELD_CONFIRM_BY = "confirmBy";
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


    @ApiModelProperty(value = "租户id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @GeneratedValue
    private String taskDocId;
    @ApiModelProperty(value = "单据号",required = true)
    @NotBlank
    private String docNum;
    @ApiModelProperty(value = "单据类型",required = true)
    @NotBlank
    private String docType;
    @ApiModelProperty(value = "组织id",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "设备id",required = true)
    @NotBlank
    private String equipmentId;
    @ApiModelProperty(value = "单据状态",required = true)
    @NotBlank
    private String docStatus;
   @ApiModelProperty(value = "任务周期")    
    private String taskCycle;
    @ApiModelProperty(value = "检验结果")
    private String checkResult;
    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;
    @ApiModelProperty(value = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "检验日期")
    private Date checkDate;
    @ApiModelProperty(value = "点检工位")
    private String wkcId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "检验人")
    private Long checkBy;
    @ApiModelProperty(value = "确认人")
    private Long confirmBy;
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


}
