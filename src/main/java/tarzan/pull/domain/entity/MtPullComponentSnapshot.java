package tarzan.pull.domain.entity;

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
 * 拉动订单组件快照
 *
 * @author peng.yuan@@hand-china.com 2020-02-04 14:38:42
 */
@ApiModel("拉动订单组件快照")
@ModifyAudit
@Table(name = "mt_pull_component_snapshot")
@CustomPrimary
public class MtPullComponentSnapshot extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CONPONENT_SNAPSHOT_ID = "conponentSnapshotId";
    public static final String FIELD_DISPATCH_SNAPSHOT_ID = "dispatchSnapshotId";
    public static final String FIELD_COMPONENT_ID = "componentId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_ACTUAL_QTY = "actualQty";
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
    private String conponentSnapshotId;
    @ApiModelProperty(value = "调度快照ID",required = true)
    @NotBlank
    @Where
    private String dispatchSnapshotId;
    @ApiModelProperty(value = "组件ID",required = true)
    @NotBlank
    @Where
    private String componentId;
    @ApiModelProperty(value = "物料ID",required = true)
    @NotBlank
    @Where
    private String materialId;
   @ApiModelProperty(value = "需求数")    
    @Where
    private Double qty;
   @ApiModelProperty(value = "实际装配数量")    
    @Where
    private Double actualQty;
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
	public String getConponentSnapshotId() {
		return conponentSnapshotId;
	}

	public void setConponentSnapshotId(String conponentSnapshotId) {
		this.conponentSnapshotId = conponentSnapshotId;
	}
    /**
     * @return 调度快照ID
     */
	public String getDispatchSnapshotId() {
		return dispatchSnapshotId;
	}

	public void setDispatchSnapshotId(String dispatchSnapshotId) {
		this.dispatchSnapshotId = dispatchSnapshotId;
	}
    /**
     * @return 组件ID
     */
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
    /**
     * @return 物料ID
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    /**
     * @return 需求数
     */
	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}
    /**
     * @return 实际装配数量
     */
	public Double getActualQty() {
		return actualQty;
	}

	public void setActualQty(Double actualQty) {
		this.actualQty = actualQty;
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
