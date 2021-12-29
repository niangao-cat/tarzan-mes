package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.common.HZeroCacheKey;
import org.hzero.core.cache.CacheValue;
import org.hzero.core.cache.Cacheable;
import tarzan.instruction.domain.entity.MtInstruction;

import java.math.BigDecimal;

/**
 * <p>
 * 出货单行
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 15:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WmsSoDeliveryLineVO extends MtInstruction implements Cacheable {
    private static final long serialVersionUID = -1753686399697500536L;

    @ApiModelProperty("行号")
    private String instructionLineNum;
    @ApiModelProperty("状态")
    @LovValue(lovCode = "WX.WMS.SO_DELIVERY_LINE_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty("状态含义")
    private String instructionStatusMeaning;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("需求数")
    private BigDecimal demandQty;
    @ApiModelProperty("实发数")
    private BigDecimal actualQty;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("发货工厂")
    private String fromSiteCode;
    @ApiModelProperty("发货仓库")
    private String fromWarehouseCode;
    @ApiModelProperty("按单标识")
    private String soFlag;
    @ApiModelProperty("生产订单号")
    private String workOrderNum;
    @ApiModelProperty("销售订单号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    @ApiModelProperty("允差下限")
    private BigDecimal toleranceLowerLimit;
    @ApiModelProperty("允差上限")
    private BigDecimal toleranceUpperLimit;
    @ApiModelProperty("容器个数")
    private Integer containerCount;
    @ApiModelProperty("客户物料编码")
    private String customerItemCode;
    @ApiModelProperty("客户物料描述")
    private String customerItemDesc;
    @ApiModelProperty("客户采购订单")
    private String customerPo;
    @ApiModelProperty("发票号")
    private String invoiceNum;
    @ApiModelProperty("海关单号")
    private String customsBillNum;
    @ApiModelProperty("箱号")
    private String containerNum;
    @ApiModelProperty("封号")
    private String sealNum;
    @ApiModelProperty("车号")
    private String carNum;
    @ApiModelProperty("车牌号")
    private String licenceNum;
    @ApiModelProperty("赠品/计价标识")
    private String freeValuationFlag;
    @ApiModelProperty("创建人姓名")
    @CacheValue(key = HZeroCacheKey.USER, primaryKey = "createdBy", searchKey = "realName",
            structure = CacheValue.DataStructure.MAP_OBJECT, db = 1)
    private String createdByName;
    @ApiModelProperty(value = "更新人姓名")
    @CacheValue(key = HZeroCacheKey.USER, primaryKey = "lastUpdatedBy", searchKey = "realName",
            structure = CacheValue.DataStructure.MAP_OBJECT, db = 1)
    private String lastUpdatedUserName;

    @ApiModelProperty("指定SN")
    private String sn;
    @ApiModelProperty("特殊库存标识")
    private String specStockFlag;
}
