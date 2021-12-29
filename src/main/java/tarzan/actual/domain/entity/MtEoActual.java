package tarzan.actual.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 执行作业【执行作业需求和实绩拆分开】
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("执行作业【执行作业需求和实绩拆分开】")
@ModifyAudit
@Table(name = "mt_eo_actual")
@CustomPrimary
public class MtEoActual extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_ACTUAL_ID = "eoActualId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_ACTUAL_START_TIME = "actualStartTime";
    public static final String FIELD_ACTUAL_END_TIME = "actualEndTime";
    public static final String FIELD_COMPLETED_QTY = "completedQty";
    public static final String FIELD_SCRAPPED_QTY = "scrappedQty";
    public static final String FIELD_HOLD_QTY = "holdQty";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 3450810214449805045L;

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
    @ApiModelProperty("主键，标识唯一一条执行作业实绩记录")
    @Id
    @Where
    private String eoActualId;
    @ApiModelProperty(value = "EO主键ID，标识实绩对应的唯一执行作业", required = true)
    @NotBlank
    @Where
    private String eoId;
    @ApiModelProperty(value = "实际开始时间")
    @Where
    private Date actualStartTime;
    @ApiModelProperty(value = "实际完成时间")
    @Where
    private Date actualEndTime;
    @ApiModelProperty(value = "累计完工数量", required = true)
    @NotNull
    @Where
    private Double completedQty;
    @ApiModelProperty(value = "累计报废数量", required = true)
    @NotNull
    @Where
    private Double scrappedQty;
    @ApiModelProperty(value = "累计保留数量", required = true)
    @NotNull
    @Where
    private Double holdQty;
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
     * @return 主键，标识唯一一条执行作业实绩记录
     */
    public String getEoActualId() {
        return eoActualId;
    }

    public void setEoActualId(String eoActualId) {
        this.eoActualId = eoActualId;
    }

    /**
     * @return EO主键ID，标识实绩对应的唯一执行作业
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 实际开始时间
     */

    public Date getActualStartTime() {
        if (actualStartTime != null) {
            return (Date) actualStartTime.clone();
        } else {
            return null;
        }
    }

    public void setActualStartTime(Date actualStartTime) {
        if (actualStartTime == null) {
            this.actualStartTime = null;
        } else {
            this.actualStartTime = (Date) actualStartTime.clone();
        }
    }

    /**
     * @return 实际完成时间
     */
    public Date getActualEndTime() {
        if (actualEndTime != null) {
            return (Date) actualEndTime.clone();
        } else {
            return null;
        }
    }

    public void setActualEndTime(Date actualEndTime) {
        if (actualEndTime == null) {
            this.actualEndTime = null;
        } else {
            this.actualEndTime = (Date) actualEndTime.clone();
        }
    }

    /**
     * @return 累计完工数量
     */
    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    /**
     * @return 累计报废数量
     */
    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    /**
     * @return 累计保留数量
     */
    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
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
