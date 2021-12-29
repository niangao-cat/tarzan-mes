package tarzan.material.domain.entity;

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
 * 物料类别集
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@ApiModel("物料类别集")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_material_category_set")
@CustomPrimary
public class MtMaterialCategorySet extends AuditDomain {

    public static final String MT_MATERIAL_CATEGORY_SET = "mt_material_category_set";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_CATEGORY_SET_ID = "materialCategorySetId";
    public static final String FIELD_CATEGORY_SET_CODE = "categorySetCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DEFAULT_SCHEDULE_FLAG = "defaultScheduleFlag";
    public static final String FIELD_DEFAULT_PURCHASE_FLAG = "defaultPurchaseFlag";
    public static final String FIELD_DEFAULT_MANUFACTURING_FLAG = "defaultManufacturingFlag";
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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String materialCategorySetId;
    @ApiModelProperty(value = "物料类别集编码", required = true)
    @NotBlank
    @Where
    private String categorySetCode;
    @ApiModelProperty(value = "物料类别集描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "是否计划默认类别集")
    @Where
    private String defaultScheduleFlag;
    @ApiModelProperty(value = "是否采购默认类别集")
    @Where
    private String defaultPurchaseFlag;
    @ApiModelProperty(value = "是否生产默认类别集")
    @Where
    private String defaultManufacturingFlag;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getMaterialCategorySetId() {
        return materialCategorySetId;
    }

    public void setMaterialCategorySetId(String materialCategorySetId) {
        this.materialCategorySetId = materialCategorySetId;
    }

    /**
     * @return 物料类别集编码
     */
    public String getCategorySetCode() {
        return categorySetCode;
    }

    public void setCategorySetCode(String categorySetCode) {
        this.categorySetCode = categorySetCode;
    }

    /**
     * @return 物料类别集描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 是否计划默认类别集
     */
    public String getDefaultScheduleFlag() {
        return defaultScheduleFlag;
    }

    public void setDefaultScheduleFlag(String defaultScheduleFlag) {
        this.defaultScheduleFlag = defaultScheduleFlag;
    }

    /**
     * @return 是否采购默认类别集
     */
    public String getDefaultPurchaseFlag() {
        return defaultPurchaseFlag;
    }

    public void setDefaultPurchaseFlag(String defaultPurchaseFlag) {
        this.defaultPurchaseFlag = defaultPurchaseFlag;
    }

    /**
     * @return 是否生产默认类别集
     */
    public String getDefaultManufacturingFlag() {
        return defaultManufacturingFlag;
    }

    public void setDefaultManufacturingFlag(String defaultManufacturingFlag) {
        this.defaultManufacturingFlag = defaultManufacturingFlag;
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
