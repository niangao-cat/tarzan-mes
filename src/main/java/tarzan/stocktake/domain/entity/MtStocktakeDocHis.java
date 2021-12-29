package tarzan.stocktake.domain.entity;

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
 * 盘点单据历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
@ApiModel("盘点单据历史表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_stocktake_doc_his")
@CustomPrimary
public class MtStocktakeDocHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_STOCKTAKE_HIS_ID = "stocktakeHisId";
    public static final String FIELD_STOCKTAKE_ID = "stocktakeId";
    public static final String FIELD_STOCKTAKE_NUM = "stocktakeNum";
    public static final String FIELD_STOCKTAKE_STATUS = "stocktakeStatus";
    public static final String FIELD_STOCKTAKE_LAST_STATUS = "stocktakeLastStatus";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_AREA_LOCATOR_ID = "areaLocatorId";
    public static final String FIELD_OPEN_FLAG = "openFlag";
    public static final String FIELD_MATERIAL_RANGE_FLAG = "materialRangeFlag";
    public static final String FIELD_ADJUST_TIMELY_FLAG = "adjustTimelyFlag";
    public static final String FIELD_MATERIAL_LOT_LOCK_FLAG = "materialLotLockFlag";
    public static final String FIELD_IDENTIFICATION = "identification";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 6096860090436648422L;

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
    @ApiModelProperty("盘点单据历史ID")
    @Id
    @Where
    private String stocktakeHisId;
    @ApiModelProperty(value = "盘点单据ID", required = true)
    @NotBlank
    @Where
    private String stocktakeId;
    @ApiModelProperty(value = "盘点单据编号", required = true)
    @NotBlank
    @Where
    private String stocktakeNum;
    @ApiModelProperty(value = "状态", required = true)
    @NotBlank
    @Where
    private String stocktakeStatus;
    @ApiModelProperty(value = "上一状态，用于状态发生变更时找到上一状态的结果")
    @Where
    private String stocktakeLastStatus;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "区域类型库位ID，用于用户查看盘点当前区域", required = true)
    @NotBlank
    @Where
    private String areaLocatorId;
    @ApiModelProperty(value = "是否明盘，Y为明盘，N为盲盘", required = true)
    @NotBlank
    @Where
    private String openFlag;
    @ApiModelProperty(value = "是否按物料盘点，Y/N，不能为空", required = true)
    @NotBlank
    @Where
    private String materialRangeFlag;
    @ApiModelProperty(value = "是否允许实时调整，Y/N，不能为空", required = true)
    @NotBlank
    @Where
    private String adjustTimelyFlag;
    @ApiModelProperty(value = "物料批停用标识", required = true)
    @NotBlank
    @Where
    private String materialLotLockFlag;
    @ApiModelProperty(value = "单据条码", required = true)
    @NotBlank
    @Where
    private String identification;
    @ApiModelProperty(value = "备注")
    @Where
    private String remark;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
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
     * @return 盘点单据历史ID
     */
    public String getStocktakeHisId() {
        return stocktakeHisId;
    }

    public void setStocktakeHisId(String stocktakeHisId) {
        this.stocktakeHisId = stocktakeHisId;
    }

    /**
     * @return 盘点单据ID
     */
    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    /**
     * @return 盘点单据编号
     */
    public String getStocktakeNum() {
        return stocktakeNum;
    }

    public void setStocktakeNum(String stocktakeNum) {
        this.stocktakeNum = stocktakeNum;
    }

    /**
     * @return 状态
     */
    public String getStocktakeStatus() {
        return stocktakeStatus;
    }

    public void setStocktakeStatus(String stocktakeStatus) {
        this.stocktakeStatus = stocktakeStatus;
    }

    /**
     * @return 上一状态，用于状态发生变更时找到上一状态的结果
     */
    public String getStocktakeLastStatus() {
        return stocktakeLastStatus;
    }

    public void setStocktakeLastStatus(String stocktakeLastStatus) {
        this.stocktakeLastStatus = stocktakeLastStatus;
    }

    /**
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 区域类型库位ID，用于用户查看盘点当前区域
     */
    public String getAreaLocatorId() {
        return areaLocatorId;
    }

    public void setAreaLocatorId(String areaLocatorId) {
        this.areaLocatorId = areaLocatorId;
    }

    /**
     * @return 是否明盘，Y为明盘，N为盲盘
     */
    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    /**
     * @return 是否按物料盘点，Y/N，不能为空
     */
    public String getMaterialRangeFlag() {
        return materialRangeFlag;
    }

    public void setMaterialRangeFlag(String materialRangeFlag) {
        this.materialRangeFlag = materialRangeFlag;
    }

    /**
     * @return 是否允许实时调整，Y/N，不能为空
     */
    public String getAdjustTimelyFlag() {
        return adjustTimelyFlag;
    }

    public void setAdjustTimelyFlag(String adjustTimelyFlag) {
        this.adjustTimelyFlag = adjustTimelyFlag;
    }

    /**
     * @return 物料批停用标识
     */
    public String getMaterialLotLockFlag() {
        return materialLotLockFlag;
    }

    public void setMaterialLotLockFlag(String materialLotLockFlag) {
        this.materialLotLockFlag = materialLotLockFlag;
    }

    /**
     * @return 单据条码
     */
    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
