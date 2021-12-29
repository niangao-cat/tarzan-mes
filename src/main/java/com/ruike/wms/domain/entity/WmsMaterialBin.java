package com.ruike.wms.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Classname InvItemBinIface
 * @Description z_inv_item_bin_iface表映射
 * @Date 2019-09-17 09:17:31
 * @author by zhihao.sang
 */
@ApiModel("物料bin值业务表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_material_bin")
@CustomPrimary
public class WmsMaterialBin extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_BIN_ID = "materialBinId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SPECS = "specs";
    public static final String FIELD_COLOR_BIN = "colorBin";
    public static final String FIELD_LIGHT_BIN = "lightBin";
    public static final String FIELD_VOLTAGE_BIN = "voltageBin";
    public static final String FIELD_GRADE_CODE = "gradeCode";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_OWNER_BY = "ownerBy";
    public static final String FIELD_OWNER_DEPARTMENT = "ownerDepartment";
    public static final String FIELD_MODIFY_BY = "modifyBy";
    public static final String FIELD_MODIFY_DATE = "modifyDate";
    public static final String FIELD_CREATION_DEPARTMENT = "creationDepartment";
    public static final String FIELD_CREATION_BY = "creationBy";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID（企业ID）",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private String materialBinId;
    @ApiModelProperty(value = "物料ID",required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "物料编码",required = true)
    @NotBlank
    private String materialCode;
    @ApiModelProperty(value = "供应商ID",required = true)
    @NotBlank
    private String supplierId;
    @ApiModelProperty(value = "供应商编码",required = true)
    @NotBlank
    private String supplierCode;
    @ApiModelProperty(value = "规格")
    private String specs;
    @ApiModelProperty(value = "色BIN")
    private String colorBin;
    @ApiModelProperty(value = "亮BIN")
    private String lightBin;
    @ApiModelProperty(value = "电压BIN")
    private String voltageBin;
    @ApiModelProperty(value = "等级编码",required = true)
    @NotBlank
    private String gradeCode;
    @ApiModelProperty(value = "等级编码有效性",required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "资料所有者")
    private String ownerBy;
    @ApiModelProperty(value = "资料所有者部门")
    private String ownerDepartment;
    @ApiModelProperty(value = "资料修改者")
    private String modifyBy;
    @ApiModelProperty(value = "资料修改日")
    private Date modifyDate;
    @ApiModelProperty(value = "资料建立部门")
    private String creationDepartment;
    @ApiModelProperty(value = "资料建立者")
    private String creationBy;
    @ApiModelProperty(value = "预留字段")
    private String attribute1;
    @ApiModelProperty(value = "预留字段")
    private String attribute2;
    @ApiModelProperty(value = "预留字段")
    private String attribute3;
    @ApiModelProperty(value = "预留字段")
    private String attribute4;
    @ApiModelProperty(value = "预留字段")
    private String attribute5;
    @ApiModelProperty(value = "预留字段")
    private String attribute6;
    @ApiModelProperty(value = "预留字段")
    private String attribute7;
    @ApiModelProperty(value = "预留字段")
    private String attribute8;
    @ApiModelProperty(value = "预留字段")
    private String attribute9;
    @ApiModelProperty(value = "预留字段")
    private String attribute10;
    @ApiModelProperty(value = "预留字段")
    private String attribute11;
    @ApiModelProperty(value = "预留字段")
    private String attribute12;
    @ApiModelProperty(value = "预留字段")
    private String attribute13;
    @ApiModelProperty(value = "预留字段")
    private String attribute14;
    @ApiModelProperty(value = "预留字段")
    private String attribute15;
    @ApiModelProperty(value = "CID",required = true)
    @NotNull
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID（企业ID）
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    /**
     * @return
     */
    public String getMaterialBinId() {
        return materialBinId;
    }

    public void setMaterialBinId(String materialBinId) {
        this.materialBinId = materialBinId;
    }
    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
    /**
     * @return 物料编码
     */
    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
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
     * @return 供应商编码
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }
    /**
     * @return 规格
     */
    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }
    /**
     * @return 色BIN
     */
    public String getColorBin() {
        return colorBin;
    }

    public void setColorBin(String colorBin) {
        this.colorBin = colorBin;
    }
    /**
     * @return 亮BIN
     */
    public String getLightBin() {
        return lightBin;
    }

    public void setLightBin(String lightBin) {
        this.lightBin = lightBin;
    }
    /**
     * @return 电压BIN
     */
    public String getVoltageBin() {
        return voltageBin;
    }

    public void setVoltageBin(String voltageBin) {
        this.voltageBin = voltageBin;
    }
    /**
     * @return 等级编码
     */
    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }
    /**
     * @return 等级编码有效性
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
    /**
     * @return 资料所有者
     */
    public String getOwnerBy() {
        return ownerBy;
    }

    public void setOwnerBy(String ownerBy) {
        this.ownerBy = ownerBy;
    }
    /**
     * @return 资料所有者部门
     */
    public String getOwnerDepartment() {
        return ownerDepartment;
    }

    public void setOwnerDepartment(String ownerDepartment) {
        this.ownerDepartment = ownerDepartment;
    }
    /**
     * @return 资料修改者
     */
    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
    /**
     * @return 资料修改日
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
    /**
     * @return 资料建立部门
     */
    public String getCreationDepartment() {
        return creationDepartment;
    }

    public void setCreationDepartment(String creationDepartment) {
        this.creationDepartment = creationDepartment;
    }
    /**
     * @return 资料建立者
     */
    public String getCreationBy() {
        return creationBy;
    }

    public void setCreationBy(String creationBy) {
        this.creationBy = creationBy;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }
    /**
     * @return 预留字段
     */
    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
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
