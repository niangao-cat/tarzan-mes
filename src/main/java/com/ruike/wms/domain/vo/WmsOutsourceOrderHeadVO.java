package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 外协管理平台 - 查询外协单头VO
 *
 * @author by Deng xu
 * @date 2020/6/11 19:54
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class WmsOutsourceOrderHeadVO implements Serializable {

    private static final long serialVersionUID = -3506862408690757496L;

    /**
     * 查询条件
     */
    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("行查询条件标识")
    private String lineQueryFlag;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("创建时间从")
    private String creationDateStart;

    @ApiModelProperty("创建时间至")
    private String creationDateEnd;

    @ApiModelProperty("单据类型集合-查询条件")
    private List<String> docTypeList;

    /** 头字段 */
    @ApiModelProperty("单头ID")
    private String instructionDocId;

    @ApiModelProperty("工厂ID")
    private String siteId;

    @ApiModelProperty("工厂")
    private String siteCode;

    @ApiModelProperty("单号")
    private String instructionDocNum;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("供应商")
    private String supplierName;

    @ApiModelProperty("供应商地址ID")
    private String supplierSiteId;

    @ApiModelProperty("单据类型")
    @LovValue(value = "WMS.OUTSOURCING_DOC_TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;

    @ApiModelProperty("单据类型描述")
    private String instructionDocTypeMeaning;

    @ApiModelProperty("单据状态")
    @LovValue(value = "WMS.OUTSOURCING_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("单据状态描述")
    private String instructionDocStatusMeaning;

    @ApiModelProperty("预计到达时间")
    private Date expectedArrivalTime;

    @ApiModelProperty("发货时间")
    private Date demandTime;

    @ApiModelProperty("收货地址ID")
    private String customerSiteId;

    @ApiModelProperty("收货地址")
    private String customerSiteDes;

    @ApiModelProperty("创建人ID")
    private String createdBy;

    @ApiModelProperty("创建人")
    private String realName;

    @ApiModelProperty("创建时间")
    private Date creationDate;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("退货单-到货时间")
    private String returnArrivalTime;

    @ApiModelProperty("退货单-收货仓库")
    private String returnLocatorId;

    @ApiModelProperty("收货地址")
    private String supplierSiteName;

    @ApiModelProperty("采购订单号")
    private String poLineNum;

    @ApiModelProperty("补货标识")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "supplyFlagMeaning")
    private String supplyFlag;

    @ApiModelProperty("补货标识含义")
    private String supplyFlagMeaning;

    @ApiModelProperty("退料原因")
    @LovValue(value = "WMS.OUTSOURCING_RETURN_REASON", meaningField = "reasonMeaning")
    private String reason;

    @ApiModelProperty("退料原因")
    private String reasonMeaning;

    @ApiModelProperty("单据行信息")
    private List<WmsOutsourceOrderLineVO> lineDataList;

    @ApiModelProperty("是否已生成补货单")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "createSupplyFlagMeaning")
    private String createSupplyFlag;

    @ApiModelProperty("是否已生成补货单含义")
    private String createSupplyFlagMeaning;

    @ApiModelProperty("补货单号")
    private String replenishmentListNum;
}
