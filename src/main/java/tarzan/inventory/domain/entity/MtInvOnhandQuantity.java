package tarzan.inventory.domain.entity;

import java.io.Serializable;

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
 * 库存量
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@ApiModel("库存量")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_inv_onhand_quantity")
@CustomPrimary
public class MtInvOnhandQuantity extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ONHAND_QUANTITY_ID = "onhandQuantityId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_ONHAND_QUANTITY = "onhandQuantity";
    public static final String FIELD_LOT_CODE = "lotCode";
    public static final String FIELD_OWNER_TYPE = "ownerType";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 5296182323407229175L;

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
    @ApiModelProperty("主键ID")
    @Id
    @Where
    private String onhandQuantityId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "库位ID", required = true)
    @NotBlank
    @Where
    private String locatorId;
    @ApiModelProperty(value = "库存现有量值", required = true)
    @NotNull
    @Where
    private Double onhandQuantity;
    @ApiModelProperty(value = "批次CODE")
    @Where
    private String lotCode;
    @ApiModelProperty(
                    value = "所有者类型\r\n包含：\r\nCUSTOMER INVENTORY:客户库存\r\nORDER INVENTORY:销售订单库存\r\nSUPPLIER INVENTORY:供应商寄售\r\nINVENTORY IN SUPPLIER:带料外协，跟踪至发给供应商的库存\r\nPROJECT INVENTORY:按项目管理的库存\r\nINVENTORY IN CUSTOMER:销售寄售，按客户消耗结算\r\n空：表示自有")
    @Where
    private String ownerType;
    @ApiModelProperty(value = "所有者ID（客户ID或供应商ID）")
    @Where
    private String ownerId;
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
     * @return 主键ID
     */
    public String getOnhandQuantityId() {
        return onhandQuantityId;
    }

    public void setOnhandQuantityId(String onhandQuantityId) {
        this.onhandQuantityId = onhandQuantityId;
    }

    /**
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
     * @return 库位ID
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 库存现有量值
     */
    public Double getOnhandQuantity() {
        return onhandQuantity;
    }

    public void setOnhandQuantity(Double onhandQuantity) {
        this.onhandQuantity = onhandQuantity;
    }

    /**
     * @return 批次CODE
     */
    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    /**
     * @return 所有者类型\r\n包含：\r\nCUSTOMER INVENTORY:客户库存\r\nORDER INVENTORY:销售订单库存\r\nSUPPLIER
     *         INVENTORY:供应商寄售\r\nINVENTORY IN SUPPLIER:带料外协，跟踪至发给供应商的库存\r\nPROJECT
     *         INVENTORY:按项目管理的库存\r\nINVENTORY IN CUSTOMER:销售寄售，按客户消耗结算\r\n空：表示自有
     */
    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * @return 所有者ID（客户ID或供应商ID）
     */
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
