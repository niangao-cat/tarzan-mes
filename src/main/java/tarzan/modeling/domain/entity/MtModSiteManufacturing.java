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
 * 站点生产属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("站点生产属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_site_manufacturing")
@CustomPrimary
public class MtModSiteManufacturing extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_MANUFACTURING_ID = "siteManufacturingId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ATTRITION_CALCULATE_STRATEGY = "attritionCalculateStrategy";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 2711738059214216608L;

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
    private String siteManufacturingId;
    @ApiModelProperty(value = "站点ID，标识唯一站点", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "损耗计算策略")
    @Where
    private String attritionCalculateStrategy;
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
    public String getSiteManufacturingId() {
        return siteManufacturingId;
    }

    public void setSiteManufacturingId(String siteManufacturingId) {
        this.siteManufacturingId = siteManufacturingId;
    }

    /**
     * @return 站点ID，标识唯一站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 损耗计算策略
     */
    public String getAttritionCalculateStrategy() {
        return attritionCalculateStrategy;
    }

    public void setAttritionCalculateStrategy(String attritionCalculateStrategy) {
        this.attritionCalculateStrategy = attritionCalculateStrategy;
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
