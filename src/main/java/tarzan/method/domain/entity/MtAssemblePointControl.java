package tarzan.method.domain.entity;

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
 * 装配点控制，指示具体装配控制下装配点可装载的物料
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@ApiModel("装配点控制，指示具体装配控制下装配点可装载的物料")

@ModifyAudit

@Table(name = "mt_assemble_point_control")
@CustomPrimary
public class MtAssemblePointControl extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_POINT_CONTROL_ID = "assemblePointControlId";
    public static final String FIELD_ASSEMBLE_CONTROL_ID = "assembleControlId";
    public static final String FIELD_ASSEMBLE_POINT_ID = "assemblePointId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_UNIT_QTY = "unitQty";
    public static final String FIELD_REFERENCE_POINT = "referencePoint";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";

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
    @ApiModelProperty("主键ID,表示唯一一条记录")
    @Id
    @Where
    private String assemblePointControlId;
    @ApiModelProperty(value = "装配控制ID", required = true)
    @NotBlank
    @Where
    private String assembleControlId;
    @ApiModelProperty(value = "装配点ID", required = true)
    @NotBlank
    @Where
    private String assemblePointId;
    @ApiModelProperty(value = "装配物料", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "装配物料的物料批")
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "装配单位用量，需求量=单位用量*ASSEMBLE_BATCH_QTY", required = true)
    @NotNull
    @Where
    private Double unitQty;
    @ApiModelProperty(value = "装配参考点")
    @Where
    private String referencePoint;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
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
     * @return 主键ID,表示唯一一条记录
     */
    public String getAssemblePointControlId() {
        return assemblePointControlId;
    }

    public void setAssemblePointControlId(String assemblePointControlId) {
        this.assemblePointControlId = assemblePointControlId;
    }

    /**
     * @return 装配控制ID
     */
    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    /**
     * @return 装配点ID
     */
    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    /**
     * @return 装配物料
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 装配物料的物料批
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 装配单位用量，需求量=单位用量*ASSEMBLE_BATCH_QTY
     */
    public Double getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(Double unitQty) {
        this.unitQty = unitQty;
    }

    /**
     * @return 装配参考点
     */
    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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
