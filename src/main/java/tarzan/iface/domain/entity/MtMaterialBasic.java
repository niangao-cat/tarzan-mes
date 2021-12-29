package tarzan.iface.domain.entity;

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
 * 物料业务属性表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:54
 */
@ApiModel("物料业务属性表")

@ModifyAudit

@Table(name = "mt_material_basic")
@CustomPrimary
public class MtMaterialBasic extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_OLD_ITEM_CODE = "oldItemCode";
    public static final String FIELD_LONG_DESCRIPTION = "longDescription";
    public static final String FIELD_ITEM_TYPE = "itemType";
    public static final String FIELD_MAKE_BUY_CODE = "makeBuyCode";
    public static final String FIELD_LOT_CONTROL_CODE = "lotControlCode";
    public static final String FIELD_QC_FLAG = "qcFlag";
    public static final String FIELD_RECEIVING_ROUTING_ID = "receivingRoutingId";
    public static final String FIELD_WIP_SUPPLY_TYPE = "wipSupplyType";
    public static final String FIELD_VMI_FLAG = "vmiFlag";
    public static final String FIELD_ITEM_GROUP = "itemGroup";
    public static final String FIELD_PRODUCT_GROUP = "productGroup";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -6013464198490435810L;

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
    @ApiModelProperty("物料站点 ID ")
    @Id
    @Where
    private String materialSiteId;
    @ApiModelProperty(value = "物料 ID ", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "旧物料号")
    @Where
    private String oldItemCode;
    @ApiModelProperty(value = "物料长描述")
    @Where
    private String longDescription;
    @ApiModelProperty(value = "物料类型")
    @Where
    private String itemType;
    @ApiModelProperty(value = "制造或采购")
    @Where
    private String makeBuyCode;
    @ApiModelProperty(value = "批次控制")
    @Where
    private String lotControlCode;
    @ApiModelProperty(value = "检验标志")
    @Where
    private String qcFlag;
    @ApiModelProperty(value = "接收方式")
    @Where
    private String receivingRoutingId;
    @ApiModelProperty(value = " wip发料类型")
    @Where
    private String wipSupplyType;
    @ApiModelProperty(value = "vmi寄售标识")
    @Where
    private String vmiFlag;
    @ApiModelProperty(value = "物料组")
    @Where
    private String itemGroup;
    @ApiModelProperty(value = "产品组")
    @Where
    private String productGroup;
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
     * @return 物料站点 ID
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 物料 ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 旧物料号
     */
    public String getOldItemCode() {
        return oldItemCode;
    }

    public void setOldItemCode(String oldItemCode) {
        this.oldItemCode = oldItemCode;
    }

    /**
     * @return 物料长描述
     */
    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * @return 物料类型
     */
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * @return 制造或采购
     */
    public String getMakeBuyCode() {
        return makeBuyCode;
    }

    public void setMakeBuyCode(String makeBuyCode) {
        this.makeBuyCode = makeBuyCode;
    }

    /**
     * @return 批次控制
     */
    public String getLotControlCode() {
        return lotControlCode;
    }

    public void setLotControlCode(String lotControlCode) {
        this.lotControlCode = lotControlCode;
    }

    /**
     * @return 检验标志
     */
    public String getQcFlag() {
        return qcFlag;
    }

    public void setQcFlag(String qcFlag) {
        this.qcFlag = qcFlag;
    }

    /**
     * @return 接收方式
     */
    public String getReceivingRoutingId() {
        return receivingRoutingId;
    }

    public void setReceivingRoutingId(String receivingRoutingId) {
        this.receivingRoutingId = receivingRoutingId;
    }

    /**
     * @return wip发料类型
     */
    public String getWipSupplyType() {
        return wipSupplyType;
    }

    public void setWipSupplyType(String wipSupplyType) {
        this.wipSupplyType = wipSupplyType;
    }

    /**
     * @return vmi寄售标识
     */
    public String getVmiFlag() {
        return vmiFlag;
    }

    public void setVmiFlag(String vmiFlag) {
        this.vmiFlag = vmiFlag;
    }

    /**
     * @return 物料组
     */
    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    /**
     * @return 产品组
     */
    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
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

}
