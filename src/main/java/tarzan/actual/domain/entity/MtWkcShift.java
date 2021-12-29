package tarzan.actual.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 开班实绩数据表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:00:23
 */
@ApiModel("开班实绩数据表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_wkc_shift")
@CustomPrimary
public class MtWkcShift extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WKC_SHIFT_ID = "wkcShiftId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_SHIFT_START_TIME = "shiftStartTime";
    public static final String FIELD_SHIFT_END_TIME = "shiftEndTime";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -7769097252281459115L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("作为班次日历分配唯一标识，用于其他数据结构引用工作日历。")
    @Id
    @Where
    private String wkcShiftId;
    @ApiModelProperty(value = "工作单元ID", required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "班次日期", required = true)
    @NotNull
    @Where
    private Date shiftDate;
    @ApiModelProperty(value = "用于标识该日期下班次", required = true)
    @NotBlank
    @Where
    private String shiftCode;
    @ApiModelProperty(value = "班次开始时间", required = true)
    @NotNull
    @Where
    private Date shiftStartTime;
    @ApiModelProperty(value = "班次结束时间")
    @Where
    private Date shiftEndTime;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 作为班次日历分配唯一标识，用于其他数据结构引用工作日历。
     */
    public String getWkcShiftId() {
        return wkcShiftId;
    }

    public void setWkcShiftId(String wkcShiftId) {
        this.wkcShiftId = wkcShiftId;
    }

    /**
     * @return 工作单元ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 班次日期
     */
    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    /**
     * @return 用于标识该日期下班次
     */
    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    /**
     * @return 班次开始时间
     */

    public Date getShiftStartTime() {
        if (shiftStartTime != null) {
            return (Date) shiftStartTime.clone();
        } else {
            return null;
        }
    }

    public void setShiftStartTime(Date shiftStartTime) {
        if (shiftStartTime == null) {
            this.shiftStartTime = null;
        } else {
            this.shiftStartTime = (Date) shiftStartTime.clone();
        }
    }

    /**
     * @return 班次结束时间
     */
    public Date getShiftEndTime() {
        if (shiftEndTime != null) {
            return (Date) shiftEndTime.clone();
        } else {
            return null;
        }
    }

    public void setShiftEndTime(Date shiftEndTime) {
        if (shiftEndTime == null) {
            this.shiftEndTime = null;
        } else {
            this.shiftEndTime = (Date) shiftEndTime.clone();
        }
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
