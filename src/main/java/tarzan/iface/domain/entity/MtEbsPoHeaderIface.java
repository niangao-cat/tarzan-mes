package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Objects;

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
 * EBS采购订单接口表
 *
 * @author guichuan.li@hand-china.com 2019-10-08 15:19:56
 */
@ApiModel("EBS采购订单接口表")
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_ebs_po_header_iface")
@CustomPrimary
public class MtEbsPoHeaderIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_PO_HEADER_ID = "poHeaderId";
    public static final String FIELD_PO_NUMBER = "poNumber";
    public static final String FIELD_PO_RELEASE_ID = "poReleaseId";
    public static final String FIELD_PO_RELEASE_NUM = "poReleaseNum";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_BUYER_CODE = "buyerCode";
    public static final String FIELD_PO_ORDER_TYPE = "poOrderType";
    public static final String FIELD_APPROVED_STATUS = "approvedStatus";
    public static final String FIELD_CANCELED_FLAG = "canceledFlag";
    public static final String FIELD_CLOSED_CODE = "closedCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CURRENCY_CODE = "currencyCode";
    public static final String FIELD_PO_ATTRIBUTE1 = "poAttribute1";
    public static final String FIELD_PO_ATTRIBUTE2 = "poAttribute2";
    public static final String FIELD_PO_ATTRIBUTE3 = "poAttribute3";
    public static final String FIELD_PO_ATTRIBUTE4 = "poAttribute4";
    public static final String FIELD_PO_ATTRIBUTE5 = "poAttribute5";
    public static final String FIELD_PO_ATTRIBUTE6 = "poAttribute6";
    public static final String FIELD_PO_ATTRIBUTE7 = "poAttribute7";
    public static final String FIELD_PO_ATTRIBUTE8 = "poAttribute8";
    public static final String FIELD_PO_ATTRIBUTE9 = "poAttribute9";
    public static final String FIELD_PO_ATTRIBUTE10 = "poAttribute10";
    public static final String FIELD_PO_ATTRIBUTE11 = "poAttribute11";
    public static final String FIELD_PO_ATTRIBUTE12 = "poAttribute12";
    public static final String FIELD_PO_ATTRIBUTE13 = "poAttribute13";
    public static final String FIELD_PO_ATTRIBUTE14 = "poAttribute14";
    public static final String FIELD_PO_ATTRIBUTE15 = "poAttribute15";
    public static final String FIELD_RELEASE_ATTRIBUTE1 = "releaseAttribute1";
    public static final String FIELD_RELEASE_ATTRIBUTE2 = "releaseAttribute2";
    public static final String FIELD_RELEASE_ATTRIBUTE3 = "releaseAttribute3";
    public static final String FIELD_RELEASE_ATTRIBUTE4 = "releaseAttribute4";
    public static final String FIELD_RELEASE_ATTRIBUTE5 = "releaseAttribute5";
    public static final String FIELD_RELEASE_ATTRIBUTE6 = "releaseAttribute6";
    public static final String FIELD_RELEASE_ATTRIBUTE7 = "releaseAttribute7";
    public static final String FIELD_RELEASE_ATTRIBUTE8 = "releaseAttribute8";
    public static final String FIELD_RELEASE_ATTRIBUTE9 = "releaseAttribute9";
    public static final String FIELD_RELEASE_ATTRIBUTE10 = "releaseAttribute10";
    public static final String FIELD_RELEASE_ATTRIBUTE11 = "releaseAttribute11";
    public static final String FIELD_RELEASE_ATTRIBUTE12 = "releaseAttribute12";
    public static final String FIELD_RELEASE_ATTRIBUTE13 = "releaseAttribute13";
    public static final String FIELD_RELEASE_ATTRIBUTE14 = "releaseAttribute14";
    public static final String FIELD_RELEASE_ATTRIBUTE15 = "releaseAttribute15";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -7482982572490845637L;

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
    @ApiModelProperty("主键")
    @Id
    @Where
    private String ifaceId;
    @ApiModelProperty(value = "采购所属组织（将ORG_ID转换为代码）", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "采购订单头ID", required = true)
    @NotBlank
    @Where
    private String poHeaderId;
    @ApiModelProperty(value = "采购订单号", required = true)
    @NotBlank
    @Where
    private String poNumber;
    @ApiModelProperty(value = "一揽子发放ID（Oracle将PO_RELEASE_ALL数据也写入此接口表）")
    @Where
    private String poReleaseId;
    @ApiModelProperty(value = "一揽子发放号（Oracle将PO_RELEASE_ALL数据也写入此接口表")
    @Where
    private String poReleaseNum;
    @ApiModelProperty(value = "供应商ID", required = true)
    @NotBlank
    @Where
    private String supplierId;
    @ApiModelProperty(value = "供应商地点ID", required = true)
    @NotBlank
    @Where
    private String supplierSiteId;
    @ApiModelProperty(value = "采购员名称（将po_agent_id转换为代码）", required = true)
    @NotBlank
    @Where
    private String buyerCode;
    @ApiModelProperty(value = "采购订单类型（STANDARD，BLANKET，BLANKET RELEASE）", required = true)
    @NotBlank
    @Where
    private String poOrderType;
    @ApiModelProperty(value = "审批状态（未完成，处理中，已批准）", required = true)
    @NotBlank
    @Where
    private String approvedStatus;
    @ApiModelProperty(value = "取消标识")
    @Where
    private String canceledFlag;
    @ApiModelProperty(value = "关闭代码（CLOSED、OPEN、FINALLY CLOSED）")
    @Where
    private String closedCode;
    @ApiModelProperty(value = "订单说明")
    @Where
    private String description;
    @ApiModelProperty(value = "币种")
    @Where
    private String currencyCode;
    @ApiModelProperty(value = "po头扩展字段1")
    @Where
    private String poAttribute1;
    @ApiModelProperty(value = "po头扩展字段2")
    @Where
    private String poAttribute2;
    @ApiModelProperty(value = "po头扩展字段3")
    @Where
    private String poAttribute3;
    @ApiModelProperty(value = "po头扩展字段4")
    @Where
    private String poAttribute4;
    @ApiModelProperty(value = "po头扩展字段5")
    @Where
    private String poAttribute5;
    @ApiModelProperty(value = "po头扩展字段6")
    @Where
    private String poAttribute6;
    @ApiModelProperty(value = "po头扩展字段7")
    @Where
    private String poAttribute7;
    @ApiModelProperty(value = "po头扩展字段8")
    @Where
    private String poAttribute8;
    @ApiModelProperty(value = "po头扩展字段9")
    @Where
    private String poAttribute9;
    @ApiModelProperty(value = "po头扩展字段10")
    @Where
    private String poAttribute10;
    @ApiModelProperty(value = "po头扩展字段11")
    @Where
    private String poAttribute11;
    @ApiModelProperty(value = "po头扩展字段12")
    @Where
    private String poAttribute12;
    @ApiModelProperty(value = "po头扩展字段13")
    @Where
    private String poAttribute13;
    @ApiModelProperty(value = "po头扩展字段14")
    @Where
    private String poAttribute14;
    @ApiModelProperty(value = "po头扩展字段15")
    @Where
    private String poAttribute15;
    @ApiModelProperty(value = "一揽子发放扩展字段1")
    @Where
    private String releaseAttribute1;
    @ApiModelProperty(value = "一揽子发放扩展字段2")
    @Where
    private String releaseAttribute2;
    @ApiModelProperty(value = "一揽子发放扩展字段3")
    @Where
    private String releaseAttribute3;
    @ApiModelProperty(value = "一揽子发放扩展字段4")
    @Where
    private String releaseAttribute4;
    @ApiModelProperty(value = "一揽子发放扩展字段5")
    @Where
    private String releaseAttribute5;
    @ApiModelProperty(value = "一揽子发放扩展字段6")
    @Where
    private String releaseAttribute6;
    @ApiModelProperty(value = "一揽子发放扩展字段7")
    @Where
    private String releaseAttribute7;
    @ApiModelProperty(value = "一揽子发放扩展字段8")
    @Where
    private String releaseAttribute8;
    @ApiModelProperty(value = "一揽子发放扩展字段9")
    @Where
    private String releaseAttribute9;
    @ApiModelProperty(value = "一揽子发放扩展字段10")
    @Where
    private String releaseAttribute10;
    @ApiModelProperty(value = "一揽子发放扩展字段11")
    @Where
    private String releaseAttribute11;
    @ApiModelProperty(value = "一揽子发放扩展字段12")
    @Where
    private String releaseAttribute12;
    @ApiModelProperty(value = "一揽子发放扩展字段13")
    @Where
    private String releaseAttribute13;
    @ApiModelProperty(value = "一揽子发放扩展字段14")
    @Where
    private String releaseAttribute14;
    @ApiModelProperty(value = "一揽子发放扩展字段15")
    @Where
    private String releaseAttribute15;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    @Where
    private Double batchId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    @Where
    private String message;
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
    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    /**
     * @return 采购所属组织（将ORG_ID转换为代码）
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return 采购订单头ID
     */
    public String getPoHeaderId() {
        return poHeaderId;
    }

    public void setPoHeaderId(String poHeaderId) {
        this.poHeaderId = poHeaderId;
    }

    /**
     * @return 采购订单号
     */
    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    /**
     * @return 一揽子发放ID（Oracle将PO_RELEASE_ALL数据也写入此接口表）
     */
    public String getPoReleaseId() {
        return poReleaseId;
    }

    public void setPoReleaseId(String poReleaseId) {
        this.poReleaseId = poReleaseId;
    }

    /**
     * @return 一揽子发放号（Oracle将PO_RELEASE_ALL数据也写入此接口表
     */
    public String getPoReleaseNum() {
        return poReleaseNum;
    }

    public void setPoReleaseNum(String poReleaseNum) {
        this.poReleaseNum = poReleaseNum;
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
     * @return 供应商地点ID
     */
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    /**
     * @return 采购员名称（将po_agent_id转换为代码）
     */
    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    /**
     * @return 采购订单类型（STANDARD，BLANKET，BLANKET RELEASE）
     */
    public String getPoOrderType() {
        return poOrderType;
    }

    public void setPoOrderType(String poOrderType) {
        this.poOrderType = poOrderType;
    }

    /**
     * @return 审批状态（未完成，处理中，已批准）
     */
    public String getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(String approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    /**
     * @return 取消标识
     */
    public String getCanceledFlag() {
        return canceledFlag;
    }

    public void setCanceledFlag(String canceledFlag) {
        this.canceledFlag = canceledFlag;
    }

    /**
     * @return 关闭代码（CLOSED、OPEN、FINALLY CLOSED）
     */
    public String getClosedCode() {
        return closedCode;
    }

    public void setClosedCode(String closedCode) {
        this.closedCode = closedCode;
    }

    /**
     * @return 订单说明
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 币种
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return po头扩展字段1
     */
    public String getPoAttribute1() {
        return poAttribute1;
    }

    public void setPoAttribute1(String poAttribute1) {
        this.poAttribute1 = poAttribute1;
    }

    /**
     * @return po头扩展字段2
     */
    public String getPoAttribute2() {
        return poAttribute2;
    }

    public void setPoAttribute2(String poAttribute2) {
        this.poAttribute2 = poAttribute2;
    }

    /**
     * @return po头扩展字段3
     */
    public String getPoAttribute3() {
        return poAttribute3;
    }

    public void setPoAttribute3(String poAttribute3) {
        this.poAttribute3 = poAttribute3;
    }

    /**
     * @return po头扩展字段4
     */
    public String getPoAttribute4() {
        return poAttribute4;
    }

    public void setPoAttribute4(String poAttribute4) {
        this.poAttribute4 = poAttribute4;
    }

    /**
     * @return po头扩展字段5
     */
    public String getPoAttribute5() {
        return poAttribute5;
    }

    public void setPoAttribute5(String poAttribute5) {
        this.poAttribute5 = poAttribute5;
    }

    /**
     * @return po头扩展字段6
     */
    public String getPoAttribute6() {
        return poAttribute6;
    }

    public void setPoAttribute6(String poAttribute6) {
        this.poAttribute6 = poAttribute6;
    }

    /**
     * @return po头扩展字段7
     */
    public String getPoAttribute7() {
        return poAttribute7;
    }

    public void setPoAttribute7(String poAttribute7) {
        this.poAttribute7 = poAttribute7;
    }

    /**
     * @return po头扩展字段8
     */
    public String getPoAttribute8() {
        return poAttribute8;
    }

    public void setPoAttribute8(String poAttribute8) {
        this.poAttribute8 = poAttribute8;
    }

    /**
     * @return po头扩展字段9
     */
    public String getPoAttribute9() {
        return poAttribute9;
    }

    public void setPoAttribute9(String poAttribute9) {
        this.poAttribute9 = poAttribute9;
    }

    /**
     * @return po头扩展字段10
     */
    public String getPoAttribute10() {
        return poAttribute10;
    }

    public void setPoAttribute10(String poAttribute10) {
        this.poAttribute10 = poAttribute10;
    }

    /**
     * @return po头扩展字段11
     */
    public String getPoAttribute11() {
        return poAttribute11;
    }

    public void setPoAttribute11(String poAttribute11) {
        this.poAttribute11 = poAttribute11;
    }

    /**
     * @return po头扩展字段12
     */
    public String getPoAttribute12() {
        return poAttribute12;
    }

    public void setPoAttribute12(String poAttribute12) {
        this.poAttribute12 = poAttribute12;
    }

    /**
     * @return po头扩展字段13
     */
    public String getPoAttribute13() {
        return poAttribute13;
    }

    public void setPoAttribute13(String poAttribute13) {
        this.poAttribute13 = poAttribute13;
    }

    /**
     * @return po头扩展字段14
     */
    public String getPoAttribute14() {
        return poAttribute14;
    }

    public void setPoAttribute14(String poAttribute14) {
        this.poAttribute14 = poAttribute14;
    }

    /**
     * @return po头扩展字段15
     */
    public String getPoAttribute15() {
        return poAttribute15;
    }

    public void setPoAttribute15(String poAttribute15) {
        this.poAttribute15 = poAttribute15;
    }

    /**
     * @return 一揽子发放扩展字段1
     */
    public String getReleaseAttribute1() {
        return releaseAttribute1;
    }

    public void setReleaseAttribute1(String releaseAttribute1) {
        this.releaseAttribute1 = releaseAttribute1;
    }

    /**
     * @return 一揽子发放扩展字段2
     */
    public String getReleaseAttribute2() {
        return releaseAttribute2;
    }

    public void setReleaseAttribute2(String releaseAttribute2) {
        this.releaseAttribute2 = releaseAttribute2;
    }

    /**
     * @return 一揽子发放扩展字段3
     */
    public String getReleaseAttribute3() {
        return releaseAttribute3;
    }

    public void setReleaseAttribute3(String releaseAttribute3) {
        this.releaseAttribute3 = releaseAttribute3;
    }

    /**
     * @return 一揽子发放扩展字段4
     */
    public String getReleaseAttribute4() {
        return releaseAttribute4;
    }

    public void setReleaseAttribute4(String releaseAttribute4) {
        this.releaseAttribute4 = releaseAttribute4;
    }

    /**
     * @return 一揽子发放扩展字段5
     */
    public String getReleaseAttribute5() {
        return releaseAttribute5;
    }

    public void setReleaseAttribute5(String releaseAttribute5) {
        this.releaseAttribute5 = releaseAttribute5;
    }

    /**
     * @return 一揽子发放扩展字段6
     */
    public String getReleaseAttribute6() {
        return releaseAttribute6;
    }

    public void setReleaseAttribute6(String releaseAttribute6) {
        this.releaseAttribute6 = releaseAttribute6;
    }

    /**
     * @return 一揽子发放扩展字段7
     */
    public String getReleaseAttribute7() {
        return releaseAttribute7;
    }

    public void setReleaseAttribute7(String releaseAttribute7) {
        this.releaseAttribute7 = releaseAttribute7;
    }

    /**
     * @return 一揽子发放扩展字段8
     */
    public String getReleaseAttribute8() {
        return releaseAttribute8;
    }

    public void setReleaseAttribute8(String releaseAttribute8) {
        this.releaseAttribute8 = releaseAttribute8;
    }

    /**
     * @return 一揽子发放扩展字段9
     */
    public String getReleaseAttribute9() {
        return releaseAttribute9;
    }

    public void setReleaseAttribute9(String releaseAttribute9) {
        this.releaseAttribute9 = releaseAttribute9;
    }

    /**
     * @return 一揽子发放扩展字段10
     */
    public String getReleaseAttribute10() {
        return releaseAttribute10;
    }

    public void setReleaseAttribute10(String releaseAttribute10) {
        this.releaseAttribute10 = releaseAttribute10;
    }

    /**
     * @return 一揽子发放扩展字段11
     */
    public String getReleaseAttribute11() {
        return releaseAttribute11;
    }

    public void setReleaseAttribute11(String releaseAttribute11) {
        this.releaseAttribute11 = releaseAttribute11;
    }

    /**
     * @return 一揽子发放扩展字段12
     */
    public String getReleaseAttribute12() {
        return releaseAttribute12;
    }

    public void setReleaseAttribute12(String releaseAttribute12) {
        this.releaseAttribute12 = releaseAttribute12;
    }

    /**
     * @return 一揽子发放扩展字段13
     */
    public String getReleaseAttribute13() {
        return releaseAttribute13;
    }

    public void setReleaseAttribute13(String releaseAttribute13) {
        this.releaseAttribute13 = releaseAttribute13;
    }

    /**
     * @return 一揽子发放扩展字段14
     */
    public String getReleaseAttribute14() {
        return releaseAttribute14;
    }

    public void setReleaseAttribute14(String releaseAttribute14) {
        this.releaseAttribute14 = releaseAttribute14;
    }

    /**
     * @return 一揽子发放扩展字段15
     */
    public String getReleaseAttribute15() {
        return releaseAttribute15;
    }

    public void setReleaseAttribute15(String releaseAttribute15) {
        this.releaseAttribute15 = releaseAttribute15;
    }

    /**
     * @return 数据批次ID
     */
    public Double getBatchId() {
        return batchId;
    }

    public void setBatchId(Double batchId) {
        this.batchId = batchId;
    }

    /**
     * @return 数据处理状态，初始为N，失败为E，成功S，处理中P
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 数据处理消息返回
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEbsPoHeaderIface that = (MtEbsPoHeaderIface) o;
        return Objects.equals(getTenantId(), that.getTenantId()) && Objects.equals(getIfaceId(), that.getIfaceId())
                        && Objects.equals(getPlantCode(), that.getPlantCode())
                        && Objects.equals(getPoHeaderId(), that.getPoHeaderId())
                        && Objects.equals(getPoNumber(), that.getPoNumber())
                        && Objects.equals(getPoReleaseId(), that.getPoReleaseId())
                        && Objects.equals(getPoReleaseNum(), that.getPoReleaseNum())
                        && Objects.equals(getSupplierId(), that.getSupplierId())
                        && Objects.equals(getSupplierSiteId(), that.getSupplierSiteId())
                        && Objects.equals(getBuyerCode(), that.getBuyerCode())
                        && Objects.equals(getPoOrderType(), that.getPoOrderType())
                        && Objects.equals(getApprovedStatus(), that.getApprovedStatus())
                        && Objects.equals(getCanceledFlag(), that.getCanceledFlag())
                        && Objects.equals(getClosedCode(), that.getClosedCode())
                        && Objects.equals(getDescription(), that.getDescription())
                        && Objects.equals(getCurrencyCode(), that.getCurrencyCode())
                        && Objects.equals(getPoAttribute1(), that.getPoAttribute1())
                        && Objects.equals(getPoAttribute2(), that.getPoAttribute2())
                        && Objects.equals(getPoAttribute3(), that.getPoAttribute3())
                        && Objects.equals(getPoAttribute4(), that.getPoAttribute4())
                        && Objects.equals(getPoAttribute5(), that.getPoAttribute5())
                        && Objects.equals(getPoAttribute6(), that.getPoAttribute6())
                        && Objects.equals(getPoAttribute7(), that.getPoAttribute7())
                        && Objects.equals(getPoAttribute8(), that.getPoAttribute8())
                        && Objects.equals(getPoAttribute9(), that.getPoAttribute9())
                        && Objects.equals(getPoAttribute10(), that.getPoAttribute10())
                        && Objects.equals(getPoAttribute11(), that.getPoAttribute11())
                        && Objects.equals(getPoAttribute12(), that.getPoAttribute12())
                        && Objects.equals(getPoAttribute13(), that.getPoAttribute13())
                        && Objects.equals(getPoAttribute14(), that.getPoAttribute14())
                        && Objects.equals(getPoAttribute15(), that.getPoAttribute15())
                        && Objects.equals(getReleaseAttribute1(), that.getReleaseAttribute1())
                        && Objects.equals(getReleaseAttribute2(), that.getReleaseAttribute2())
                        && Objects.equals(getReleaseAttribute3(), that.getReleaseAttribute3())
                        && Objects.equals(getReleaseAttribute4(), that.getReleaseAttribute4())
                        && Objects.equals(getReleaseAttribute5(), that.getReleaseAttribute5())
                        && Objects.equals(getReleaseAttribute6(), that.getReleaseAttribute6())
                        && Objects.equals(getReleaseAttribute7(), that.getReleaseAttribute7())
                        && Objects.equals(getReleaseAttribute8(), that.getReleaseAttribute8())
                        && Objects.equals(getReleaseAttribute9(), that.getReleaseAttribute9())
                        && Objects.equals(getReleaseAttribute10(), that.getReleaseAttribute10())
                        && Objects.equals(getReleaseAttribute11(), that.getReleaseAttribute11())
                        && Objects.equals(getReleaseAttribute12(), that.getReleaseAttribute12())
                        && Objects.equals(getReleaseAttribute13(), that.getReleaseAttribute13())
                        && Objects.equals(getReleaseAttribute14(), that.getReleaseAttribute14())
                        && Objects.equals(getReleaseAttribute15(), that.getReleaseAttribute15())
                        && Objects.equals(getBatchId(), that.getBatchId())
                        && Objects.equals(getStatus(), that.getStatus())
                        && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getCid(), that.getCid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantId(), getIfaceId(), getPlantCode(), getPoHeaderId(), getPoNumber(),
                        getPoReleaseId(), getPoReleaseNum(), getSupplierId(), getSupplierSiteId(), getBuyerCode(),
                        getPoOrderType(), getApprovedStatus(), getCanceledFlag(), getClosedCode(), getDescription(),
                        getCurrencyCode(), getPoAttribute1(), getPoAttribute2(), getPoAttribute3(), getPoAttribute4(),
                        getPoAttribute5(), getPoAttribute6(), getPoAttribute7(), getPoAttribute8(), getPoAttribute9(),
                        getPoAttribute10(), getPoAttribute11(), getPoAttribute12(), getPoAttribute13(),
                        getPoAttribute14(), getPoAttribute15(), getReleaseAttribute1(), getReleaseAttribute2(),
                        getReleaseAttribute3(), getReleaseAttribute4(), getReleaseAttribute5(), getReleaseAttribute6(),
                        getReleaseAttribute7(), getReleaseAttribute8(), getReleaseAttribute9(), getReleaseAttribute10(),
                        getReleaseAttribute11(), getReleaseAttribute12(), getReleaseAttribute13(),
                        getReleaseAttribute14(), getReleaseAttribute15(), getBatchId(), getStatus(), getMessage(),
                        getCid());
    }
}
