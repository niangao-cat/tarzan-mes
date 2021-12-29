package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeVirtualNumVO
 * @Description 虚拟号视图
 * @Date 2020/9/29 22:14
 * @Author yuchao.wang
 */
@Data
public class HmeVirtualNumVO implements Serializable {
    private static final long serialVersionUID = 2793216285732540013L;

    @ApiModelProperty(value = "主键id")
    private String virtualId;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "产品类型")
    @LovValue(lovCode = "HME_PRODUCT_TYPE", meaningField="productCodeMeaning")
    private String productCode;

    @ApiModelProperty(value = "产品类型描述")
    private String productCodeMeaning;

    @ApiModelProperty(value = "是否绑定工单")
    private String bindFlag;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "EO")
    private String eoId;

    @ApiModelProperty(value = "EO承载条码")
    private String eoIdentification;

    @ApiModelProperty(value = "数量")
    private Long quantity;

    @ApiModelProperty(value = "盒子ID")
    private String materialLotId;

    @ApiModelProperty(value = "盒子条码")
    private String materialLotCode;

    @ApiModelProperty(value = "是否有效")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField="enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "是否有效描述")
    private String enableFlagMeaning;

    @ApiModelProperty(value = "芯片位置")
    private String chipLocation;

    @ApiModelProperty(value = "预调选明细信息")
    private List<HmeSelectionDetailsVO> detailList;

    @ApiModelProperty(value = "首路热沉号")
    private String hotSinkCode;
}