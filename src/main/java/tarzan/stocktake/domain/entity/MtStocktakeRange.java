package tarzan.stocktake.domain.entity;

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
 * 盘点范围表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
@ApiModel("盘点范围表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_stocktake_range")
@CustomPrimary
public class MtStocktakeRange extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_STOCKTAKE_RANGE_ID = "stocktakeRangeId";
    public static final String FIELD_STOCKTAKE_ID = "stocktakeId";
    public static final String FIELD_RANGE_OBJECT_TYPE = "rangeObjectType";
    public static final String FIELD_RANGE_OBJECT_ID = "rangeObjectId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 6655105108920209724L;

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
    @ApiModelProperty("单据范围ID")
    @Id
    @Where
    private String stocktakeRangeId;
    @ApiModelProperty(value = "单据ID", required = true)
    @NotBlank
    @Where
    private String stocktakeId;
    @ApiModelProperty(value = "范围类型", required = true)
    @NotBlank
    @Where
    private String rangeObjectType;
    @ApiModelProperty(value = "范围ID", required = true)
    @NotBlank
    @Where
    private String rangeObjectId;
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
     * @return 单据范围ID
     */
    public String getStocktakeRangeId() {
        return stocktakeRangeId;
    }

    public void setStocktakeRangeId(String stocktakeRangeId) {
        this.stocktakeRangeId = stocktakeRangeId;
    }

    /**
     * @return 单据ID
     */
    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    /**
     * @return 范围类型
     */
    public String getRangeObjectType() {
        return rangeObjectType;
    }

    public void setRangeObjectType(String rangeObjectType) {
        this.rangeObjectType = rangeObjectType;
    }

    /**
     * @return 范围ID
     */
    public String getRangeObjectId() {
        return rangeObjectId;
    }

    public void setRangeObjectId(String rangeObjectId) {
        this.rangeObjectId = rangeObjectId;
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
