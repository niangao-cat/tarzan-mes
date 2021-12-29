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
 * 生产指令实绩
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("生产指令实绩")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_work_order_actual")
@CustomPrimary
public class MtWorkOrderActual extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORK_ORDER_ACTUAL_ID = "workOrderActualId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_RELEASED_QTY = "releasedQty";
    public static final String FIELD_COMPLETED_QTY = "completedQty";
    public static final String FIELD_SCRAPPED_QTY = "scrappedQty";
    public static final String FIELD_HOLD_QTY = "holdQty";
    public static final String FIELD_ACTUAL_START_DATE = "actualStartDate";
    public static final String FIELD_ACTUAL_END_DATE = "actualEndDate";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 7934238771681411791L;

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
    @ApiModelProperty("主键，标识唯一一条实绩记录")
    @Id
    @Where
    private String workOrderActualId;
    @ApiModelProperty(value = "生产指令ID", required = true)
    @NotBlank
    @Where
    private String workOrderId;
    @ApiModelProperty(value = "已下达数量", required = true)
    @NotNull
    @Where
    private Double releasedQty;
    @ApiModelProperty(value = "已完成数量", required = true)
    @NotNull
    @Where
    private Double completedQty;
    @ApiModelProperty(value = "已报废数量", required = true)
    @NotNull
    @Where
    private Double scrappedQty;
    @ApiModelProperty(value = "已保留数量", required = true)
    @NotNull
    @Where
    private Double holdQty;
    @ApiModelProperty(value = "实际开始时间")
    @Where
    private Date actualStartDate;
    @ApiModelProperty(value = "实际完成时间")
    @Where
    private Date actualEndDate;
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
     * @return 主键，标识唯一一条实绩记录
     */
    public String getWorkOrderActualId() {
        return workOrderActualId;
    }

    public void setWorkOrderActualId(String workOrderActualId) {
        this.workOrderActualId = workOrderActualId;
    }

    /**
     * @return 生产指令ID
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return 已下达数量
     */
    public Double getReleasedQty() {
        return releasedQty;
    }

    public void setReleasedQty(Double releasedQty) {
        this.releasedQty = releasedQty;
    }

    /**
     * @return 已完成数量
     */
    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    /**
     * @return 已报废数量
     */
    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    /**
     * @return 已保留数量
     */
    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }

    /**
     * @return 实际开始时间
     */

    public Date getActualStartDate() {
        if (actualStartDate != null) {
            return (Date) actualStartDate.clone();
        } else {
            return null;
        }
    }

    public void setActualStartDate(Date actualStartDate) {
        if (actualStartDate == null) {
            this.actualStartDate = null;
        } else {
            this.actualStartDate = (Date) actualStartDate.clone();
        }
    }

    public Date getActualEndDate() {
        if (actualEndDate != null) {
            return (Date) actualEndDate.clone();
        } else {
            return null;
        }
    }

    public void setActualEndDate(Date actualEndDate) {
        if (actualEndDate == null) {
            this.actualEndDate = null;
        } else {
            this.actualEndDate = (Date) actualEndDate.clone();
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
