package com.ruike.hme.api.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工上下岗记录表
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Data
public class HmeSignInOutRecordDTO implements Serializable {
	private static final long serialVersionUID = 7878732768964491855L;
    @ApiModelProperty(value = "租户ID")    
    private Long tenantId;
    @ApiModelProperty("下岗记录表数据主键")
    private String recordId;
    @ApiModelProperty(value = "上下岗关联主键（将每次上下岗进行关联，按照员工+日期+工位+班次维度新建",required = true)
    @NotBlank
    private String relId;
    @ApiModelProperty(value = "用户ID",required = true)
    @NotNull
    private Long userId;
    @ApiModelProperty(value = "员工ID",required = true)
    @NotNull
    private Long employeeId;
    @ApiModelProperty(value = "岗位ID",required = true)
    @NotNull
    private Long unitId;
    @ApiModelProperty(value = "工位ID",required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "日期",required = true)
    @NotNull
    private String date;
    @ApiModelProperty(value = "班次",required = true)
    @NotBlank
    private String shiftCode;
    @ApiModelProperty(value = "工作日历ID",required = true)
    @NotBlank
    private String calendarShiftId;
    @ApiModelProperty(value = "操作时间",required = true)
    @NotNull
    private String operationDate;
    @ApiModelProperty(value = "事项（分为开班 OPEN、离岗OFF 、上岗ON、结班CLOSE四个状态）",required = true)
    @NotBlank
    private String operation;
    @ApiModelProperty(value = "累计时长",required = true)
    @NotNull
    private String duration;
    @ApiModelProperty(value = "截止时间",required = true)
    @NotNull
    private Date startTime ;
}
