package tarzan.order.domain.entity;

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
 * EO装配清单
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@ApiModel("EO装配清单")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_eo_bom")
@CustomPrimary
public class MtEoBom extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_BOM_ID = "eoBomId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -7364530678194365466L;

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
    private String eoBomId;
    @ApiModelProperty(value = "EO，EO主键，标识唯一EO", required = true)
    @NotBlank
    @Where
    private String eoId;
    @ApiModelProperty(value = "BOM，BOM主键，标识唯一BOM", required = true)
    @NotBlank
    @Where
    private String bomId;
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
    public String getEoBomId() {
        return eoBomId;
    }

    public void setEoBomId(String eoBomId) {
        this.eoBomId = eoBomId;
    }

    /**
     * @return EO，EO主键，标识唯一EO
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return BOM，BOM主键，标识唯一BOM
     */
    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
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
