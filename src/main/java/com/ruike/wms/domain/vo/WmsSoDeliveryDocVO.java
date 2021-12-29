package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.common.HZeroCacheKey;
import org.hzero.core.cache.CacheValue;
import org.hzero.core.cache.Cacheable;
import tarzan.instruction.domain.entity.MtInstructionDoc;

/**
 * <p>
 * 出货单单据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 13:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WmsSoDeliveryDocVO extends MtInstructionDoc implements Cacheable {
    private static final long serialVersionUID = 8906973847267765367L;

    @ApiModelProperty("单据状态")
    @LovValue(lovCode = "WX.WMS.SO_DELIVERY_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("单据状态含义")
    private String instructionDocStatusMeaning;

    @ApiModelProperty("客户编码")
    private String customerCode;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("工厂编码")
    private String siteCode;

    @ApiModelProperty("工厂名称")
    private String siteName;

    @ApiModelProperty("客户地点编码")
    private String customerSiteCode;

    @ApiModelProperty("送货地址")
    private String deliveryAddress;

    @ApiModelProperty("创建人姓名")
    @CacheValue(key = HZeroCacheKey.USER, primaryKey = "createdBy", searchKey = "realName",
            structure = CacheValue.DataStructure.MAP_OBJECT, db = 1)
    private String createdByName;

    @ApiModelProperty(value = "更新人姓名")
    @CacheValue(key = HZeroCacheKey.USER, primaryKey = "lastUpdatedBy", searchKey = "realName",
            structure = CacheValue.DataStructure.MAP_OBJECT, db = 1)
    private String lastUpdatedUserName;
}
