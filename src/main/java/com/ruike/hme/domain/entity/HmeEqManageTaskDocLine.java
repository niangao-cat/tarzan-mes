package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 设备管理任务单行表
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:10
 */
@ApiModel("设备管理任务单行表")
@VersionAudit
@ModifyAudit
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_eq_manage_task_doc_line")
@CustomPrimary
public class HmeEqManageTaskDocLine extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TASK_DOC_LINE_ID = "taskDocLineId";
    public static final String FIELD_TASK_DOC_ID = "taskDocId";
    public static final String FIELD_MANAGE_TAG_ID = "manageTagId";
    public static final String FIELD_CHECK_VALUE = "checkValue";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_REMARK = "remark";
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
    private String taskDocLineId;
    @ApiModelProperty(value = "单据头id", required = true)
    @NotBlank
    private String taskDocId;
    @ApiModelProperty(value = "项目ID", required = true)
    @NotBlank
    private String manageTagId;
    @ApiModelProperty(value = "检验值")
    private String checkValue;
    @ApiModelProperty(value = "结果")
    private String result;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "检验人")
    private Long checkBy;
    @ApiModelProperty(value = "检验日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;
    @ApiModelProperty(value = "点检工位")
    private String wkcId;
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
