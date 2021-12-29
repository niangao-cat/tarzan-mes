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
 * ERP货位业务表	
 *
 * @author guichuan.li@hand-china.com 2019-09-24 10:49:01
 */
@ApiModel("ERP货位业务表	")
@ModifyAudit

@Table(name = "mt_erp_subinv_locator")
@CustomPrimary
public class MtErpSubinvLocator extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SUBINV_LOCATOR_ID = "subinvLocatorId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_SUBINV_CODE = "subinvCode";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_LOCATOR_CODE = "locatorCode";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
	private static final long serialVersionUID = -531498657872416510L;

	//
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @Where
    private String subinvLocatorId;
    @ApiModelProperty(value = "工厂代码（EBS将ID转换成code写入)",required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "ERP库存地点代码",required = true)
    @NotBlank
    @Where
    private String subinvCode;
   @ApiModelProperty(value = "ERP货位ID")    
    @Where
    private String locatorId;
    @ApiModelProperty(value = "ERP仓库货位",required = true)
    @NotBlank
    @Where
    private String locatorCode;
   @ApiModelProperty(value = "有效性")    
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
     * @return 主键
     */
	public String getSubinvLocatorId() {
		return subinvLocatorId;
	}

	public void setSubinvLocatorId(String subinvLocatorId) {
		this.subinvLocatorId = subinvLocatorId;
	}
    /**
     * @return 工厂代码（EBS将ID转换成code写入)
     */
	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
    /**
     * @return ERP库存地点代码
     */
	public String getSubinvCode() {
		return subinvCode;
	}

	public void setSubinvCode(String subinvCode) {
		this.subinvCode = subinvCode;
	}
    /**
     * @return ERP货位ID
     */
	public String getLocatorId() {
		return locatorId;
	}

	public void setLocatorId(String locatorId) {
		this.locatorId = locatorId;
	}
    /**
     * @return ERP仓库货位
     */
	public String getLocatorCode() {
		return locatorCode;
	}

	public void setLocatorCode(String locatorCode) {
		this.locatorCode = locatorCode;
	}
    /**
     * @return 有效性
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
