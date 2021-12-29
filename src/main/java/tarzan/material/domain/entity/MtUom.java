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
 * 单位
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:10:08
 */
@ApiModel("单位")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_uom")
@CustomPrimary
public class MtUom extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_UOM_TYPE = "uomType";
    public static final String FIELD_UOM_CODE = "uomCode";
    public static final String FIELD_UOM_NAME = "uomName";
    public static final String FIELD_PRIMARY_FLAG = "primaryFlag";
    public static final String FIELD_CONVERSION_VALUE = "conversionValue";
    public static final String FIELD_DECIMAL_NUMBER = "decimalNumber";
    public static final String FIELD_PROCESS_MODE = "processMode";
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
    @ApiModelProperty("主键ID,标识唯一一条记录")
    @Id
    @Where
    private String uomId;
    @ApiModelProperty(value = "单位类型", required = true)
    @NotBlank
    @Where
    private String uomType;
    @ApiModelProperty(value = "单位编码", required = true)
    @NotBlank
    @Where
    private String uomCode;
    @ApiModelProperty(value = "单位描述", required = true)
    @MultiLanguageField
    @NotBlank
    @Where
    private String uomName;
    @ApiModelProperty(value = "同类别主单位标识")
    @Where
    private String primaryFlag;
    @ApiModelProperty(value = "与主单位换算关系，主单位/单位", required = true)
    @NotNull
    @Where
    private Double conversionValue;
    @ApiModelProperty(value = "小数位数", required = true)
    @NotNull
    @Where
    private Long decimalNumber;
    @ApiModelProperty(value = "尾数处理方式，包括进一法、四舍五入、去尾法", required = true)
    @NotBlank
    @Where
    private String processMode;
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
     * @return 主键ID,标识唯一一条记录
     */
    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    /**
     * @return 单位类型
     */
    public String getUomType() {
        return uomType;
    }

    public void setUomType(String uomType) {
        this.uomType = uomType;
    }

    /**
     * @return 单位编码
     */
    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    /**
     * @return 单位描述
     */
    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    /**
     * @return 同类别主单位标识
     */
    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    /**
     * @return 与主单位换算关系，主单位/单位
     */
    public Double getConversionValue() {
        return conversionValue;
    }

    public void setConversionValue(Double conversionValue) {
        this.conversionValue = conversionValue;
    }

    /**
     * @return 小数位数
     */
    public Long getDecimalNumber() {
        return decimalNumber;
    }

    public void setDecimalNumber(Long decimalNumber) {
        this.decimalNumber = decimalNumber;
    }

    /**
     * @return 尾数处理方式，包括进一法、四舍五入、去尾法
     */
    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
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
