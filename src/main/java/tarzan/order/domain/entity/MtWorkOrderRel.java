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
 * 生产指令关系,标识生产指令的父子关系
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@ApiModel("生产指令关系,标识生产指令的父子关系")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_work_order_rel")
@CustomPrimary
public class MtWorkOrderRel extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORK_ORDER_REL_ID = "workOrderRelId";
    public static final String FIELD_REL_TYPE = "relType";
    public static final String FIELD_PARENT_WORK_ORDER_ID = "parentWorkOrderId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -8362886110522163927L;

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
    private String workOrderRelId;
    @ApiModelProperty(value = "关系类型：按BOM分解、按工艺路线分解、同层次拆分", required = true)
    @NotBlank
    @Where
    private String relType;
    @ApiModelProperty(value = "父生产指令ID", required = true)
    @NotBlank
    @Where
    private String parentWorkOrderId;
    @ApiModelProperty(value = "生产指令ID", required = true)
    @NotBlank
    @Where
    private String workOrderId;
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
    public String getWorkOrderRelId() {
        return workOrderRelId;
    }

    public void setWorkOrderRelId(String workOrderRelId) {
        this.workOrderRelId = workOrderRelId;
    }

    /**
     * @return 关系类型：按BOM分解、按工艺路线分解、同层次拆分
     */
    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    /**
     * @return 父生产指令ID
     */
    public String getParentWorkOrderId() {
        return parentWorkOrderId;
    }

    public void setParentWorkOrderId(String parentWorkOrderId) {
        this.parentWorkOrderId = parentWorkOrderId;
    }

    /**
     * @return 生产指令ID
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
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
