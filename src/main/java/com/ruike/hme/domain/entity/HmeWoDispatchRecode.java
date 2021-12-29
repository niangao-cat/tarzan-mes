package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.io.Serializable;
import java.math.BigDecimal;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单派工记录表
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@ApiModel("工单派工记录表")
@VersionAudit
@ModifyAudit
@Data
@Table(name = "hme_wo_dispatch_recode")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
public class HmeWoDispatchRecode extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WO_DISPATCH_ID = "woDispatchId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_CALENDAR_SHIFT_ID = "calendarShiftId";
    public static final String FIELD_SHIFT_COMPLETED_QTY = "shiftCompletedQty";
    public static final String FIELD_DISPATCH_QTY = "dispatchQty";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public HmeWoDispatchRecode(Long tenantId, String workOrderId, String prodLineId, String workcellId, String calendarShiftId) {
        this.workOrderId = workOrderId;
        this.prodLineId = prodLineId;
        this.workcellId = workcellId;
        this.calendarShiftId = calendarShiftId;
        this.tenantId = tenantId;
    }

    public HmeWoDispatchRecode() {
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @GeneratedValue
    private String woDispatchId;
    @ApiModelProperty(value = "生产指令ID",required = true)
    @NotBlank
    private String workOrderId;
   @ApiModelProperty(value = "生产线ID")    
    private String prodLineId;
   @ApiModelProperty(value = "工段（工作单元）ID")    
    private String workcellId;
   @ApiModelProperty(value = "班次ID")    
    private String calendarShiftId;
   @ApiModelProperty(value = "班次完工数量")    
    private BigDecimal shiftCompletedQty;
   @ApiModelProperty(value = "派工数量")    
    private BigDecimal dispatchQty;
    @ApiModelProperty(value = "",required = true)
    @NotNull
    @Cid
    private Long cid;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

}
