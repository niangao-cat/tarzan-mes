package tarzan.iface.domain.entity;

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

import java.io.Serializable;

/**
 * ERP库存表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:10
 */
@ApiModel("ERP库存表")

@ModifyAudit

@Table(name = "mt_subinventory")
@CustomPrimary
public class MtSubinventory extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SUBINVENTORY_ID = "subinventoryId";
    public static final String FIELD_SUBINVENTORY_CODE = "subinventoryCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 3154357331967775669L;

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
    private String subinventoryId;
    @ApiModelProperty(value = "库存地点代码（子库存）", required = true)
    @NotBlank
    @Where
    private String subinventoryCode;
    @ApiModelProperty(value = "库存地点描述（子库存描述)")
    @Where
    private String description;
    @ApiModelProperty(value = "工厂CODE", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "有效标识", required = true)
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
     * @return 主键ID ,表示唯一一条记录
     */
    public String getSubinventoryId() {
        return subinventoryId;
    }

    public void setSubinventoryId(String subinventoryId) {
        this.subinventoryId = subinventoryId;
    }

    /**
     * @return 库存地点代码（子库存）
     */
    public String getSubinventoryCode() {
        return subinventoryCode;
    }

    public void setSubinventoryCode(String subinventoryCode) {
        this.subinventoryCode = subinventoryCode;
    }

    /**
     * @return 库存地点描述（子库存描述)
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 工厂CODE
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return 有效标识
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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
