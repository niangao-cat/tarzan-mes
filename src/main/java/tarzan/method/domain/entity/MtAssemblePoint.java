package tarzan.method.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 装配点，标识具体装配组下具体的装配位置
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@ApiModel("装配点，标识具体装配组下具体的装配位置")

@ModifyAudit

@Table(name = "mt_assemble_point")
@MultiLanguage
@CustomPrimary
public class MtAssemblePoint extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_POINT_ID = "assemblePointId";
    public static final String FIELD_ASSEMBLE_GROUP_ID = "assembleGroupId";
    public static final String FIELD_ASSEMBLE_POINT_CODE = "assemblePointCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_UNIQUE_MATERIAL_LOT_FLAG = "uniqueMaterialLotFlag";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_MAX_QTY = "maxQty";
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
    private String assemblePointId;
    @ApiModelProperty(value = "装配组", required = true)
    @NotBlank
    @Where
    private String assembleGroupId;
    @ApiModelProperty(value = "装配点", required = true)
    @NotBlank
    @Where
    private String assemblePointCode;
    @ApiModelProperty(value = "装配点描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "是否限制唯一物料批")
    @Where
    private String uniqueMaterialLotFlag;
    @ApiModelProperty(value = "装配点在装配组内的顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "最大装载量，如果为空则不限制")
    @Where
    private Double maxQty;
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
    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    /**
     * @return 装配组
     */
    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    /**
     * @return 装配点
     */
    public String getAssemblePointCode() {
        return assemblePointCode;
    }

    public void setAssemblePointCode(String assemblePointCode) {
        this.assemblePointCode = assemblePointCode;
    }

    /**
     * @return 装配点描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 是否限制唯一物料批
     */
    public String getUniqueMaterialLotFlag() {
        return uniqueMaterialLotFlag;
    }

    public void setUniqueMaterialLotFlag(String uniqueMaterialLotFlag) {
        this.uniqueMaterialLotFlag = uniqueMaterialLotFlag;
    }

    /**
     * @return 装配点在装配组内的顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 最大装载量，如果为空则不限制
     */
    public Double getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Double maxQty) {
        this.maxQty = maxQty;
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
