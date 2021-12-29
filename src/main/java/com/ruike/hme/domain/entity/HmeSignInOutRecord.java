package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 员工上下岗记录表
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@ApiModel("员工上下岗记录表")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_sign_in_out_record")
@CustomPrimary
public class HmeSignInOutRecord extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_RECORD_ID = "recordId";
    public static final String FIELD_REL_ID = "relId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_EMPLOYEE_ID = "employeeId";
    public static final String FIELD_UNIT_ID = "unitId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_CALENDAR_SHIFT_ID = "calendarShiftId";
    public static final String FIELD_OPERATION_DATE = "operationDate";
    public static final String FIELD_OPERATION = "operation";
    public static final String FIELD_DURATION = "duration";
    public static final String FIELD_REASON = "reason";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty("下岗记录表数据主键")
    @Id
    private String recordId;
    @ApiModelProperty(value = "上下岗关联主键")
    private String relId;
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull
    private Long userId;
    @ApiModelProperty(value = "员工ID", required = true)
    @NotNull
    private Long employeeId;
    @ApiModelProperty(value = "岗位ID")
    private Long unitId;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "日期 2020-00-00 00:00:00")
    @NotNull
    private Date date;
    @ApiModelProperty(value = "班次", required = true)
    @NotBlank
    private String shiftCode;
    @ApiModelProperty(value = "工作日历ID")
    private String calendarShiftId;
    @ApiModelProperty(value = "操作时间")
    private Date operationDate;
    @ApiModelProperty(value = "事项（分为开班 OPEN、离岗OFF 、上岗ON、结班CLOSE四个状态）", required = true)
    @NotBlank
    private String operation;
    @ApiModelProperty(value = "累计时长 00:00:00", required = true)
    @NotNull
    private String duration;
    @ApiModelProperty(value = "离岗原因")
    private String reason;
    @ApiModelProperty(value = "")
    @Cid
    private Long cid;
    @ApiModelProperty(value = "预留字段1")
    private String attribute1;
    @ApiModelProperty(value = "预留字段2")
    private String attribute2;
    @ApiModelProperty(value = "预留字段3")
    private String attribute3;
    @ApiModelProperty(value = "预留字段4")
    private String attribute4;
    @ApiModelProperty(value = "预留字段5")
    private String attribute5;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private String startAgen;
}
