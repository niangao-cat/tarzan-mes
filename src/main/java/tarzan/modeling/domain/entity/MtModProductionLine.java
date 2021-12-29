package tarzan.modeling.domain.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产线
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("生产线")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_mod_production_line")
@CustomPrimary
public class MtModProductionLine extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_PROD_LINE_CODE = "prodLineCode";
    public static final String FIELD_PROD_LINE_NAME = "prodLineName";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PROD_LINE_TYPE = "prodLineType";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_PROD_LINE_CATEGORY = "prodLineCategory";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -8006469602669681112L;

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
    private String prodLineId;
    @ApiModelProperty(value = "生产线编号", required = true)
    @NotBlank
    @Where
    private String prodLineCode;
    @ApiModelProperty(value = "生产线名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String prodLineName;
    @ApiModelProperty(value = "生产线描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "生产线类型，区分生产线类型为自有、外协或是采购", required = true)
    @NotBlank
    @Where
    private String prodLineType;
    @ApiModelProperty(value = "供应商ID")
    @Where
    private String supplierId;
    @ApiModelProperty(value = "供应商地点ID")
    @Where
    private String supplierSiteId;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "生产线分类")
    @Where
    private String prodLineCategory;
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
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    /**
     * @return 生产线编号
     */
    public String getProdLineCode() {
        return prodLineCode;
    }

    public void setProdLineCode(String prodLineCode) {
        this.prodLineCode = prodLineCode;
    }

    /**
     * @return 生产线名称
     */
    public String getProdLineName() {
        return prodLineName;
    }

    public void setProdLineName(String prodLineName) {
        this.prodLineName = prodLineName;
    }

    /**
     * @return 生产线描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 生产线类型，区分生产线类型为自有、外协或是采购
     */
    public String getProdLineType() {
        return prodLineType;
    }

    public void setProdLineType(String prodLineType) {
        this.prodLineType = prodLineType;
    }

    /**
     * @return 供应商ID
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 供应商地点ID
     */
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
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
     * @return 生产线分类
     */
    public String getProdLineCategory() {
        return prodLineCategory;
    }

    public void setProdLineCategory(String prodLineCategory) {
        this.prodLineCategory = prodLineCategory;
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
