package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeCosGetChipSiteOutConfirmDTO
 * @Description COS取片平台-出站确认输入参数
 * @Date 2020/8/18 20:09
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipSiteOutConfirmDTO implements Serializable {
    private static final long serialVersionUID = 7787864873191784714L;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单工艺工位在制记录id")
    private String cosRecord;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("容器类型")
    private String containerType;

    @ApiModelProperty("班组ID")
    private String wkcShiftId;

    @ApiModelProperty("生成条码数量")
    private Integer materialLotCount;

    @ApiModelProperty("不良代码")
    private String ncCode;

    @ApiModelProperty("站点Id")
    private String siteId;

    @ApiModelProperty("批次号")
    private String lot;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("实际装载芯片数")
    private Long actualChipNum;

    @ApiModelProperty("实验代码")
    private String labCode;

    @ApiModelProperty("实验备注")
    private String labRemark;
}