package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.List;

/**
 * @ClassName HmeCOSMaterialReturnVO
 * @Description COS退料-工单数据
 * @Author lkj
 * @Date 2020/12/11
 */
@Data
public class HmeCosMaterialReturnVO {

    @ApiModelProperty(value = "租户")
    private String tenantId;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "工单类型")
    @LovValue(lovCode = "MT.WO_TYPE", meaningField = "workOrderTypeMeaning")
    private String workOrderType;

    @ApiModelProperty(value = "工单类型含义")
    private String workOrderTypeMeaning;

    @ApiModelProperty(value = "工单状态")
    @LovValue(lovCode = "MT.WO_STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "工单状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "工单数量")
    private String qty;

    @ApiModelProperty(value = "工单数量单位ID")
    private String uomId;

    @ApiModelProperty(value = "工单数量单位编码")
    private String uomCode;

    @ApiModelProperty(value = "生产线ID")
    private String productionLineId;

    @ApiModelProperty(value = "生产线编码")
    private String productionLineCode;

    @ApiModelProperty(value = "生产线名称")
    private String productionLineName;

    @ApiModelProperty(value = "装配清单ID")
    private String bomId;

    @ApiModelProperty(value = "装配清单名称")
    private String bomName;

    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;

    @ApiModelProperty(value = "工艺路线编码")
    private String routerCode;

    @ApiModelProperty(value = "仓库ID")
    private String locatorId;

    @ApiModelProperty(value = "完工库位编码")
    private String locatorCode;

    @ApiModelProperty(value = "完工库位名称")
    private String locatorName;

    @ApiModelProperty(value = "工单组件")
    private List<HmeCosMaterialReturnLineVO> bomComment;


}
