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
 * 装配点实绩，记录装配组下装配点实际装配信息
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("装配点实绩，记录装配组下装配点实际装配信息")

@ModifyAudit

@Table(name = "mt_assemble_point_actual")
@CustomPrimary
public class MtAssemblePointActual extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_POINT_ACTUAL_ID = "assemblePointActualId";
    public static final String FIELD_ASSEMBLE_GROUP_ACTUAL_ID = "assembleGroupActualId";
    public static final String FIELD_ASSEMBLE_POINT_ID = "assemblePointId";
    public static final String FIELD_FEEDING_SEQUENCE = "feedingSequence";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_FEEDING_QTY = "feedingQty";
    public static final String FIELD_FEEDING_MATERIAL_LOT_SEQUENCE = "feedingMaterialLotSequence";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -5768176954145834505L;

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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String assemblePointActualId;
    @ApiModelProperty(value = "装配组ID", required = true)
    @NotBlank
    @Where
    private String assembleGroupActualId;
    @ApiModelProperty(value = "ASSEMBLE_POINT_ID", required = true)
    @NotBlank
    @Where
    private String assemblePointId;
    @ApiModelProperty(value = "装配点上料顺序（装载物料顺序？）", required = true)
    @NotNull
    @Where
    private Long feedingSequence;
    @ApiModelProperty(value = "装配点装载物料", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "装配点当前装载物料数量", required = true)
    @NotNull
    @Where
    private Double qty;
    @ApiModelProperty(value = "装配点上料初始数量", required = true)
    @NotNull
    @Where
    private Double feedingQty;
    @ApiModelProperty(value = "上料批次顺序（一个点装载多个物料批时的顺序）", required = true)
    @NotNull
    @Where
    private Long feedingMaterialLotSequence;
    @ApiModelProperty(value = "装配点装载物料批次")
    @Where
    private String materialLotId;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getAssemblePointActualId() {
        return assemblePointActualId;
    }

    public void setAssemblePointActualId(String assemblePointActualId) {
        this.assemblePointActualId = assemblePointActualId;
    }

    /**
     * @return 装配组ID
     */
    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    /**
     * @return ASSEMBLE_POINT_ID
     */
    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    /**
     * @return 装配点上料顺序（装载物料顺序？）
     */
    public Long getFeedingSequence() {
        return feedingSequence;
    }

    public void setFeedingSequence(Long feedingSequence) {
        this.feedingSequence = feedingSequence;
    }

    /**
     * @return 装配点装载物料
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 装配点当前装载物料数量
     */
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return 装配点上料初始数量
     */
    public Double getFeedingQty() {
        return feedingQty;
    }

    public void setFeedingQty(Double feedingQty) {
        this.feedingQty = feedingQty;
    }

    /**
     * @return 上料批次顺序（一个点装载多个物料批时的顺序）
     */
    public Long getFeedingMaterialLotSequence() {
        return feedingMaterialLotSequence;
    }

    public void setFeedingMaterialLotSequence(Long feedingMaterialLotSequence) {
        this.feedingMaterialLotSequence = feedingMaterialLotSequence;
    }

    /**
     * @return 装配点装载物料批次
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
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
