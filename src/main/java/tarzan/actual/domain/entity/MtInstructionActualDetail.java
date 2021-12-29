package tarzan.actual.domain.entity;

import java.io.Serializable;

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
 * 指令实绩明细表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("指令实绩明细表")

@ModifyAudit

@Table(name = "mt_instruction_actual_detail")
@CustomPrimary
public class MtInstructionActualDetail extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ACTUAL_DETAIL_ID = "actualDetailId";
    public static final String FIELD_ACTUAL_ID = "actualId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_ACTUAL_QTY = "actualQty";
    public static final String FIELD_CONTAINER_ID = "containerId";
    public static final String FROM_LOCATOR_ID = "fromLocatorId";
    public static final String TOLOCATOR_ID = "toLocatorId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -7684606700401893351L;

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
    @ApiModelProperty("实绩id")
    @Id
    @Where
    private String actualDetailId;
    @ApiModelProperty(value = "汇总id", required = true)
    @NotBlank
    @Where
    private String actualId;
    @ApiModelProperty(value = "物料批ID", required = true)
    @NotBlank
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "单位", required = true)
    @NotBlank
    @Where
    private String uomId;
    @ApiModelProperty(value = "实绩数量", required = true)
    @NotNull
    @Where
    private Double actualQty;
    @ApiModelProperty(value = "器具id")
    @Where
    private String containerId;
    @ApiModelProperty(value = "源库位id")
    @Where
    private String fromLocatorId;
    @ApiModelProperty(value = "目标库位id")
    @Where
    private String toLocatorId;
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
     * @return 实绩id
     */
    public String getActualDetailId() {
        return actualDetailId;
    }

    public void setActualDetailId(String actualDetailId) {
        this.actualDetailId = actualDetailId;
    }

    /**
     * @return 汇总id
     */
    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    /**
     * @return 物料批ID
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 单位
     */
    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    /**
     * @return 实绩数量
     */
    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
    }

    /**
     * @return 器具id
     */
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getFromLocatorId() {
        return fromLocatorId;
    }

    public void setFromLocatorId(String fromLocatorId) {
        this.fromLocatorId = fromLocatorId;
    }

    public String getToLocatorId() {
        return toLocatorId;
    }

    public void setToLocatorId(String toLocatorId) {
        this.toLocatorId = toLocatorId;
    }
}
