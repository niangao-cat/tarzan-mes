package tarzan.pull.domain.entity;

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
 * 拉动调度结果快照
 *
 * @author peng.yuan@@hand-china.com 2020-02-04 14:38:41
 */
@ApiModel("拉动调度结果快照")
@ModifyAudit
@Table(name = "mt_pull_dispatch_snapshot")
@CustomPrimary
public class MtPullDispatchSnapshot extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DISPATCH_SNAPSHOT_ID = "dispatchSnapshotId";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_AREA_ID = "areaId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_DISPATCH_QTY = "dispatchQty";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_REVISION = "revision";
    public static final String FIELD_SNAPSHOT_REVISION = "snapshotRevision";
    public static final String FIELD_LATEST_REVISION_FLAG = "latestRevisionFlag";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键，唯一标识")
    @Id
    @Where
    private String dispatchSnapshotId;
    @ApiModelProperty(value = "拉动日期",required = true)
    @NotNull
    @Where
    private Date shiftDate;
    @ApiModelProperty(value = "班次拉动",required = true)
    @NotBlank
    @Where
    private String shiftCode;
    @ApiModelProperty(value = "WKC_ID",required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "配送路线",required = true)
    @NotBlank
    @Where
    private String areaId;
   @ApiModelProperty(value = "EO_ID")    
    @Where
    private String eoId;
   @ApiModelProperty(value = "WO_ID")    
    @Where
    private String workOrderId;
   @ApiModelProperty(value = "调度数量")    
    @Where
    private Double dispatchQty;
   @ApiModelProperty(value = "优先级")    
    @Where
    private Double priority;
   @ApiModelProperty(value = "调度版本")    
    @Where
    private Double revision;
    @ApiModelProperty(value = "快照版本：配送路线+MMDDHHMMSS",required = true)
    @NotBlank
    @Where
    private String snapshotRevision;
    @ApiModelProperty(value = "标志",required = true)
    @NotBlank
    @Where
    private String latestRevisionFlag;
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
     * @return 主键，唯一标识
     */
	public String getDispatchSnapshotId() {
		return dispatchSnapshotId;
	}

	public void setDispatchSnapshotId(String dispatchSnapshotId) {
		this.dispatchSnapshotId = dispatchSnapshotId;
	}
    /**
     * @return 拉动日期
     */
	public Date getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate;
	}
    /**
     * @return 班次拉动
     */
	public String getShiftCode() {
		return shiftCode;
	}

	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}
    /**
     * @return WKC_ID
     */
	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}
    /**
     * @return 配送路线
     */
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
    /**
     * @return EO_ID
     */
	public String getEoId() {
		return eoId;
	}

	public void setEoId(String eoId) {
		this.eoId = eoId;
	}
    /**
     * @return WO_ID
     */
	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
    /**
     * @return 调度数量
     */
	public Double getDispatchQty() {
		return dispatchQty;
	}

	public void setDispatchQty(Double dispatchQty) {
		this.dispatchQty = dispatchQty;
	}
    /**
     * @return 优先级
     */
	public Double getPriority() {
		return priority;
	}

	public void setPriority(Double priority) {
		this.priority = priority;
	}
    /**
     * @return 调度版本
     */
	public Double getRevision() {
		return revision;
	}

	public void setRevision(Double revision) {
		this.revision = revision;
	}
    /**
     * @return 快照版本：配送路线+MMDDHHMMSS
     */
	public String getSnapshotRevision() {
		return snapshotRevision;
	}

	public void setSnapshotRevision(String snapshotRevision) {
		this.snapshotRevision = snapshotRevision;
	}
    /**
     * @return 标志
     */
	public String getLatestRevisionFlag() {
		return latestRevisionFlag;
	}

	public void setLatestRevisionFlag(String latestRevisionFlag) {
		this.latestRevisionFlag = latestRevisionFlag;
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
