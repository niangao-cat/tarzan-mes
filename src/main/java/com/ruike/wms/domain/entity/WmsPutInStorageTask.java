package com.ruike.wms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.io.Serializable;
import java.math.BigDecimal;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hzero.mybatis.common.query.Where;

/**
 * 入库上架任务表
 *
 * @author liyuan.lv@hand-china.com 2020-04-06 20:58:44
 */
@ApiModel("入库上架任务表")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "wms_put_in_storage_task")
@CustomPrimary
public class WmsPutInStorageTask extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TASK_ID = "taskId";
    public static final String FIELD_TASK_TYPE = "taskType";
    public static final String FIELD_INSTRUCTION_DOC_ID = "instructionDocId";
    public static final String FIELD_INSTRUCTION_ID = "instructionId";
    public static final String FIELD_TASK_STATUS = "taskStatus";
    public static final String FIELD_INSTRUCTION_DOC_TYPE = "instructionDocType";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_TASK_QTY = "taskQty";
    public static final String FIELD_EXECUTE_QTY = "executeQty";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Where
    @Id
    private String taskId;
    @ApiModelProperty(value = "任务类型", required = true)
    @NotBlank
    private String taskType;
    @ApiModelProperty(value = "入库上架关联单据ID", required = true)
    @NotBlank
    private String instructionDocId;
    @ApiModelProperty(value = "入库上架关联单据行ID", required = true)
    @NotBlank
    private String instructionId;
    @ApiModelProperty(value = "入库上架任务状态", required = true)
    @NotBlank
    private String taskStatus;
    @ApiModelProperty(value = "关联单据类型", required = true)
    @NotBlank
    private String instructionDocType;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "任务数量", required = true)
    @NotNull
    private BigDecimal taskQty;
    @ApiModelProperty(value = "执行数量")
    private BigDecimal executeQty;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    @Cid
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

}
