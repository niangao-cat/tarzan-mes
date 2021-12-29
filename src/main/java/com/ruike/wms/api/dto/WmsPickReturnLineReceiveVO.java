package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.common.HZeroCacheKey;
import org.hzero.core.cache.CacheValue;
import tarzan.instruction.domain.entity.MtInstruction;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes
 * @description: 领退料返回的行数据
 * @author: han.zhang
 * @create: 2020/04/17 16:19
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WmsPickReturnLineReceiveVO extends MtInstruction implements Serializable {

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "执行数量")
    private BigDecimal executeQty;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "目标工厂")
    private String toSiteCode;

    @ApiModelProperty(value = "目标货位")
    private String toLocatorCode;

    @ApiModelProperty(value = "目标仓库id")
    private String toStorageId;

    @ApiModelProperty(value = "目标仓库编码")
    private String toStorageCode;

    @ApiModelProperty(value = "行号")
    private String instructionLineNum;

    private String instructionStatusMeaning;

    @ApiModelProperty(value = "执行人人姓名")
    @CacheValue(key = HZeroCacheKey.USER, primaryKey = "lastUpdatedBy", searchKey = "realName",
            structure = CacheValue.DataStructure.MAP_OBJECT)
    private String lastUpdatedUserName;

    @ApiModelProperty(value = "执行数量")
    private String actualQty;

    @ApiModelProperty(value = "来源仓库编码")
    private String fromLocatorCode;

    @ApiModelProperty(value = "来源仓库id")
    private String fromLocatorId;

    @ApiModelProperty(value = "来源仓库id")
    private String fromStorageId;

    @ApiModelProperty(value = "来源仓库编码")
    private String fromStorageCode;

    @ApiModelProperty(value = "超发设置")
    @LovValue(value = "WMS.EXCESS_SETTING", meaningField = "excessSettingMeaning")
    private String excessSetting;

    @ApiModelProperty(value = "超发设置含义")
    private String excessSettingMeaning;

    @ApiModelProperty(value = "超发值")
    private String excessValue;

    @ApiModelProperty(value = "库存量")
    private BigDecimal onhandQuantity;

    @ApiModelProperty(value = "下标")
    private Long index;
}