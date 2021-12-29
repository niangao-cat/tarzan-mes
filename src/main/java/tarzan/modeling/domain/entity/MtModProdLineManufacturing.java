package tarzan.modeling.domain.entity;

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
 * 生产线生产属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@ApiModel("生产线生产属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_prod_line_manufacturing")
@CustomPrimary
public class MtModProdLineManufacturing extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PROD_LINE_MANUFACTURING_ID = "prodLineManufacturingId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_ISSUED_LOCATOR_ID = "issuedLocatorId";
    public static final String FIELD_COMPLETION_LOCATOR_ID = "completionLocatorId";
    public static final String FIELD_INVENTORY_LOCATOR_ID = "inventoryLocatorId";
    public static final String FIELD_DISPATCH_METHOD = "dispatchMethod";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 5452840536874543417L;

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
    @ApiModelProperty("主键ID ,表示唯一一条记录")
    @Id
    @Where
    private String prodLineManufacturingId;
    @ApiModelProperty(value = "生产线ID，标识唯一一条生产线", required = true)
    @NotBlank
    @Where
    private String prodLineId;
    @ApiModelProperty(value = "默认发料库位")
    @Where
    private String issuedLocatorId;
    @ApiModelProperty(value = "默认完工库位")
    @Where
    private String completionLocatorId;
    @ApiModelProperty(value = "默认入库库位")
    @Where
    private String inventoryLocatorId;
    @ApiModelProperty(value = "调度方式")
    @Where
    private String dispatchMethod;
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
     * @return 主键ID ,表示唯一一条记录
     */
    public String getProdLineManufacturingId() {
        return prodLineManufacturingId;
    }

    public void setProdLineManufacturingId(String prodLineManufacturingId) {
        this.prodLineManufacturingId = prodLineManufacturingId;
    }

    /**
     * @return 生产线ID，标识唯一一条生产线
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    /**
     * @return 默认发料库位
     */
    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    /**
     * @return 默认完工库位
     */
    public String getCompletionLocatorId() {
        return completionLocatorId;
    }

    public void setCompletionLocatorId(String completionLocatorId) {
        this.completionLocatorId = completionLocatorId;
    }

    /**
     * @return 默认入库库位
     */
    public String getInventoryLocatorId() {
        return inventoryLocatorId;
    }

    public void setInventoryLocatorId(String inventoryLocatorId) {
        this.inventoryLocatorId = inventoryLocatorId;
    }

    /**
     * @return 调度方式
     */
    public String getDispatchMethod() {
        return dispatchMethod;
    }

    public void setDispatchMethod(String dispatchMethod) {
        this.dispatchMethod = dispatchMethod;
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
