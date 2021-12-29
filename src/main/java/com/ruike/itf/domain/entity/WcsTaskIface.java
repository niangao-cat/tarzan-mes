package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 成品出库指令信息接口表
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:04:14
 */
@ApiModel("成品出库指令信息接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_wcs_task_iface")
@CustomPrimary
public class WcsTaskIface extends AuditDomain {

    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TASK_NUM = "taskNum";
    public static final String FIELD_DOC_ID = "docId";
    public static final String FIELD_DOC_LINE_ID = "docLineId";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_SO_NUM = "soNum";
    public static final String FIELD_SO_LINE_NUM = "soLineNum";
    public static final String FIELD_EXIT_NUM = "exitNum";
    public static final String FIELD_WAREHOUSE_CODE = "warehouseCode";
    public static final String FIELD_TASK_STATUS = "taskStatus";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
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

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

//
// 数据库字段
// ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "任务号，唯一性")
    private String taskNum;
    @ApiModelProperty(value = "单据头id")
    private String docId;
    @ApiModelProperty(value = "单据行id")
    private String docLineId;
    @ApiModelProperty(value = "物料")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "数量")
    private String qty;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "出口号")
    private String exitNum;
    @ApiModelProperty(value = "仓库")
    private String warehouseCode;
    @ApiModelProperty(value = "任务状态")
    private String taskStatus;
    @ApiModelProperty(value = "数据处理状态")
    private String status;
    @ApiModelProperty(value = "数据处理消息")
    private String message;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    private Long cid;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;

//
// 非数据库字段
// ------------------------------------------------------------------------------

//
// getter/setter
// ------------------------------------------------------------------------------

    /**
     * @return 主键
     */
    public String getIfaceId() {
        return ifaceId;
    }

    public WcsTaskIface setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
        return this;
    }

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public WcsTaskIface setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 任务号，唯一性
     */
    public String getTaskNum() {
        return taskNum;
    }

    public WcsTaskIface setTaskNum(String taskNum) {
        this.taskNum = taskNum;
        return this;
    }

    /**
     * @return 单据头id
     */
    public String getDocId() {
        return docId;
    }

    public WcsTaskIface setDocId(String docId) {
        this.docId = docId;
        return this;
    }

    /**
     * @return 单据行id
     */
    public String getDocLineId() {
        return docLineId;
    }

    public WcsTaskIface setDocLineId(String docLineId) {
        this.docLineId = docLineId;
        return this;
    }

    /**
     * @return 物料
     */
    public String getMaterialCode() {
        return materialCode;
    }

    public WcsTaskIface setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
        return this;
    }

    /**
     * @return 物料版本
     */
    public String getMaterialVersion() {
        return materialVersion;
    }

    public WcsTaskIface setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
        return this;
    }

    /**
     * @return 数量
     */
    public String getQty() {
        return qty;
    }

    public WcsTaskIface setQty(String qty) {
        this.qty = qty;
        return this;
    }

    /**
     * @return 销售订单号
     */
    public String getSoNum() {
        return soNum;
    }

    public WcsTaskIface setSoNum(String soNum) {
        this.soNum = soNum;
        return this;
    }

    /**
     * @return 销售订单行号
     */
    public String getSoLineNum() {
        return soLineNum;
    }

    public WcsTaskIface setSoLineNum(String soLineNum) {
        this.soLineNum = soLineNum;
        return this;
    }

    /**
     * @return 出口号
     */
    public String getExitNum() {
        return exitNum;
    }

    public WcsTaskIface setExitNum(String exitNum) {
        this.exitNum = exitNum;
        return this;
    }

    /**
     * @return 仓库
     */
    public String getWarehouseCode() {
        return warehouseCode;
    }

    public WcsTaskIface setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
        return this;
    }

    /**
     * @return 任务状态
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    public WcsTaskIface setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    /**
     * @return 数据处理状态
     */
    public String getStatus() {
        return status;
    }

    public WcsTaskIface setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * @return 数据处理消息
     */
    public String getMessage() {
        return message;
    }

    public WcsTaskIface setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public WcsTaskIface setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public WcsTaskIface setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public WcsTaskIface setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public WcsTaskIface setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public WcsTaskIface setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public WcsTaskIface setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public WcsTaskIface setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public WcsTaskIface setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public WcsTaskIface setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public WcsTaskIface setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public WcsTaskIface setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public WcsTaskIface setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public WcsTaskIface setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public WcsTaskIface setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public WcsTaskIface setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public WcsTaskIface setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public WcsTaskIface setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
