package tarzan.iface.domain.entity;

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
 * 采购订单头表
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
@ApiModel("采购订单头表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_po_header")
@CustomPrimary
public class MtPoHeader extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PO_HEADER_ID = "poHeaderId";
    public static final String FIELD_PO_NUMBER = "poNumber";
    public static final String FIELD_PO_RELEASE_NUM = "poReleaseNum";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_BUYER_CODE = "buyerCode";
    public static final String FIELD_PO_CATEGORY = "poCategory";
    public static final String FIELD_PO_ORDER_TYPE = "poOrderType";
    public static final String FIELD_APPROVED_FLAG = "approvedFlag";
    public static final String FIELD_CANCELED_FLAG = "canceledFlag";
    public static final String FIELD_CLOSED_CODE = "closedFlag";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CURRENCY_CODE = "currencyCode";
    public static final String FIELD_TRANSFER_SITE_ID = "transferSiteId";
    public static final String FIELD_ERP_PO_HEAD_ID = "erpPoHeadId";
    public static final String FIELD_ERP_PO_RELEASE_ID = "erpPoReleaseId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 8415411990804457704L;

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
    private String poHeaderId;
    @ApiModelProperty(value = "采购订单号，标识唯一", required = true)
    @NotBlank
    @Where
    private String poNumber;
    @ApiModelProperty(value = "一揽子发放")
    @Where
    private String poReleaseNum;
    @ApiModelProperty(value = "制造站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "供应商ID", required = true)
    @NotBlank
    @Where
    private String supplierId;
    @ApiModelProperty(value = "供应商地点ID", required = true)
    @NotBlank
    @Where
    private String supplierSiteId;
    @ApiModelProperty(value = "采购员名称", required = true)
    @NotBlank
    @Where
    private String buyerCode;
    @ApiModelProperty(value = "采购类别")
    @Where
    private String poCategory;
    @ApiModelProperty(value = "采购订单类型（标准，一揽子协议，一揽子发放）", required = true)
    @NotBlank
    @Where
    private String poOrderType;
    @ApiModelProperty(value = "审批标志", required = true)
    @NotBlank
    @Where
    private String approvedFlag;
    @ApiModelProperty(value = "取消标识")
    @Where
    private String canceledFlag;
    @ApiModelProperty(value = "关闭标志")
    @Where
    private String closedFlag;
    @ApiModelProperty(value = "订单说明")
    @Where
    private String description;
    @ApiModelProperty(value = "币种")
    @Where
    private String currencyCode;
    @ApiModelProperty(value = "转储站点")
    @Where
    private String transferSiteId;
    @ApiModelProperty(value = "ERP采购订单头ID")
    @Where
    private String erpPoHeadId;
    @ApiModelProperty(value = "ERP一揽子发放ID")
    @Where
    private String erpPoReleaseId;
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
    public String getPoHeaderId() {
        return poHeaderId;
    }

    public void setPoHeaderId(String poHeaderId) {
        this.poHeaderId = poHeaderId;
    }

    /**
     * @return 采购订单号，标识唯一
     */
    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    /**
     * @return 一揽子发放
     */
    public String getPoReleaseNum() {
        return poReleaseNum;
    }

    public void setPoReleaseNum(String poReleaseNum) {
        this.poReleaseNum = poReleaseNum;
    }

    /**
     * @return 制造站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
     * @return 采购员名称
     */
    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    /**
     * @return 采购类别
     */
    public String getPoCategory() {
        return poCategory;
    }

    public void setPoCategory(String poCategory) {
        this.poCategory = poCategory;
    }

    /**
     * @return 采购订单类型（标准，一揽子协议，一揽子发放）
     */
    public String getPoOrderType() {
        return poOrderType;
    }

    public void setPoOrderType(String poOrderType) {
        this.poOrderType = poOrderType;
    }

    /**
     * @return 审批标志
     */
    public String getApprovedFlag() {
        return approvedFlag;
    }

    public void setApprovedFlag(String approvedFlag) {
        this.approvedFlag = approvedFlag;
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
     * @return 关闭标志
     */
    public String getClosedFlag() {
        return closedFlag;
    }

    public void setClosedFlag(String closedFlag) {
        this.closedFlag = closedFlag;
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
     * @return 转储站点
     */
    public String getTransferSiteId() {
        return transferSiteId;
    }

    public void setTransferSiteId(String transferSiteId) {
        this.transferSiteId = transferSiteId;
    }

    /**
     * @return ERP采购订单头ID
     */
    public String getErpPoHeadId() {
        return erpPoHeadId;
    }

    public void setErpPoHeadId(String erpPoHeadId) {
        this.erpPoHeadId = erpPoHeadId;
    }

    /**
     * @return ERP一揽子发放ID
     */
    public String getErpPoReleaseId() {
        return erpPoReleaseId;
    }

    public void setErpPoReleaseId(String erpPoReleaseId) {
        this.erpPoReleaseId = erpPoReleaseId;
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
