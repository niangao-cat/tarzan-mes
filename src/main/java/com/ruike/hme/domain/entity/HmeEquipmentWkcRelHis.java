package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备工位关系历史表
 *
 * @author chaonan.hu@hand-china.com 2020-06-23 15:53:04
 */
@ApiModel("设备工位关系历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_equipment_wkc_rel_his")
public class HmeEquipmentWkcRelHis extends AuditDomain {

    public static final String FIELD_EQUIPMENT_WKC_REL_HIS_ID = "equipmentWkcRelHisId";
    public static final String FIELD_EQUIPMENT_WKC_REL_ID = "equipmentWkcRelId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_EQUIPMENT_ID = "equipmentId";
    public static final String FIELD_BUSINESS_ID = "businessId";
    public static final String FIELD_WORK_SHOP_ID = "workShopId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_LINE_ID = "lineId";
    public static final String FIELD_PROCESS_ID = "processId";
    public static final String FIELD_STATION_ID = "stationId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    private String equipmentWkcRelHisId;
    @ApiModelProperty(value = "",required = true)
    @NotBlank
    private String equipmentWkcRelId;
    @ApiModelProperty(value = "站点id",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "设备ID",required = true)
    @NotBlank
    private String equipmentId;
   @ApiModelProperty(value = "部门id")    
    private String businessId;
   @ApiModelProperty(value = "车间id")    
    private String workShopId;
   @ApiModelProperty(value = "产线id")    
    private String prodLineId;
   @ApiModelProperty(value = "工段id")    
    private String lineId;
   @ApiModelProperty(value = "工序id")    
    private String processId;
   @ApiModelProperty(value = "工位id")    
    private String stationId;
    @ApiModelProperty(value = "等级编码有效性",required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "事件ID",required = true)
    @NotBlank
    private String eventId;
    @ApiModelProperty(value = "CID",required = true)
	@Cid
    private Long cid;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 主键
     */
	public String getEquipmentWkcRelHisId() {
		return equipmentWkcRelHisId;
	}

	public void setEquipmentWkcRelHisId(String equipmentWkcRelHisId) {
		this.equipmentWkcRelHisId = equipmentWkcRelHisId;
	}
    /**
     * @return 
     */
	public String getEquipmentWkcRelId() {
		return equipmentWkcRelId;
	}

	public void setEquipmentWkcRelId(String equipmentWkcRelId) {
		this.equipmentWkcRelId = equipmentWkcRelId;
	}
    /**
     * @return 站点id
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 设备ID
     */
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
    /**
     * @return 部门id
     */
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    /**
     * @return 车间id
     */
	public String getWorkShopId() {
		return workShopId;
	}

	public void setWorkShopId(String workShopId) {
		this.workShopId = workShopId;
	}
    /**
     * @return 产线id
     */
	public String getProdLineId() {
		return prodLineId;
	}

	public void setProdLineId(String prodLineId) {
		this.prodLineId = prodLineId;
	}
    /**
     * @return 工段id
     */
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
    /**
     * @return 工序id
     */
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
    /**
     * @return 工位id
     */
	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
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
     * @return 事件ID
     */
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
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
