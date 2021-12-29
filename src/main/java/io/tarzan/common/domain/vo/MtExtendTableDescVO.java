package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtExtendTableDescVO implements Serializable {
    private static final long serialVersionUID = -8015618240149465784L;
    @ApiModelProperty("拓展表描述主键")
    private String extendTableDescId;
    @ApiModelProperty(value = "表名", required = true)
    @NotBlank
    private String attrTable;
    @ApiModelProperty(value = "描述", required = true)
    @NotBlank
    private String attrTableDesc;
    @ApiModelProperty(value = "服务包", required = true)
    @NotBlank
    private String servicePackage;
    @ApiModelProperty(value = "服务包描述")
    private String servicePackageDesc;
    @ApiModelProperty(value = "主表", required = true)
    @NotBlank
    private String mainTable;
    @ApiModelProperty(value = "主表主键", required = true)
    @NotBlank
    private String mainTableKey;
    @ApiModelProperty(value = "历史表")
    private String hisTable;
    @ApiModelProperty(value = "历史表主键")
    private String hisTableKey;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "多语言信息，保存使用")
    private Map<String, Map<String, String>> _tls;

    @ApiModelProperty(value = "历史表扩展表")
    private String hisAttrTable;

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

    public String getAttrTableDesc() {
        return attrTableDesc;
    }

    public void setAttrTableDesc(String attrTableDesc) {
        this.attrTableDesc = attrTableDesc;
    }

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

    public String getMainTable() {
        return mainTable;
    }

    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }

    public String getMainTableKey() {
        return mainTableKey;
    }

    public void setMainTableKey(String mainTableKey) {
        this.mainTableKey = mainTableKey;
    }

    public String getHisTable() {
        return hisTable;
    }

    public void setHisTable(String hisTable) {
        this.hisTable = hisTable;
    }

    public String getHisTableKey() {
        return hisTableKey;
    }

    public void setHisTableKey(String hisTableKey) {
        this.hisTableKey = hisTableKey;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public String getHisAttrTable() {
        return hisAttrTable;
    }

    public void setHisAttrTable(String hisAttrTable) {
        this.hisAttrTable = hisAttrTable;
    }
}
