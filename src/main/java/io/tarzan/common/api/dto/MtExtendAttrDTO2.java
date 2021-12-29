package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * 拓展表字段维护-保存 使用DTO
 * 
 * @author benjamin
 */
public class MtExtendAttrDTO2 implements Serializable {
    private static final long serialVersionUID = -2179895776995843929L;

    @ApiModelProperty("拓展字段主键")
    private String extendId;
    @ApiModelProperty(value = "拓展表描述主键(更新时必输)")
    @NotBlank
    private String extendTableDescId;
    @ApiModelProperty(value = "拓展表名")
    private String attrTable;
    @ApiModelProperty(value = "服务包")
    private String servicePackage;
    @ApiModelProperty(value = "服务包描述")
    private String servicePackageDesc;
    @ApiModelProperty(value = "拓展字段(更新时必输)")
    @NotBlank
    private String attrName;
    @ApiModelProperty(value = "拓展字段描述(更新时必输)")
    private String attrMeaning;
    @ApiModelProperty(value = "多语言标识(更新时必输)")
    @NotBlank
    private String tlFlag;
    @ApiModelProperty(value = "有效性(更新时必输)")
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "顺序(更新时必输)")
    @NotNull
    private Long sequence;
    @ApiModelProperty(value = "拓展表描述")
    private String attrTableDesc;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls;

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getServicePackageDesc() {
        return servicePackageDesc;
    }

    public void setServicePackageDesc(String servicePackageDesc) {
        this.servicePackageDesc = servicePackageDesc;
    }

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public String getExtendTableDescId() {
        return extendTableDescId;
    }

    public void setExtendTableDescId(String extendTableDescId) {
        this.extendTableDescId = extendTableDescId;
    }

    public String getAttrTable() {
        return attrTable;
    }

    public void setAttrTable(String attrTable) {
        this.attrTable = attrTable;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrMeaning() {
        return attrMeaning;
    }

    public void setAttrMeaning(String attrMeaning) {
        this.attrMeaning = attrMeaning;
    }

    public String getTlFlag() {
        return tlFlag;
    }

    public void setTlFlag(String tlFlag) {
        this.tlFlag = tlFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getAttrTableDesc() {
        return attrTableDesc;
    }

    public void setAttrTableDesc(String attrTableDesc) {
        this.attrTableDesc = attrTableDesc;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
