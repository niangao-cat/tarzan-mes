package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeWoJobSnDTO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/13 10:38
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnDTO3 implements Serializable {
    private static final long serialVersionUID = -5022246164085024602L;

    @ApiModelProperty(value = "来料记录Id")
    private String operationRecordId;

    @ApiModelProperty(value = "站点id")
    private String siteId;

    @ApiModelProperty(value = "物料批id")
    private String materialLotCode;

    @ApiModelProperty(value = "班组id")
    private String shiftId;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "工艺id")
    private String operationId;

    @ApiModelProperty(value = "工段id")
    private String wkcLinetId;

    @ApiModelProperty(value = "请求id")
    private String requestId;

    @ApiModelProperty(value = "父请求id")
    private String parentEventId;

    @ApiModelProperty(value = "分组类型")
    private String itemGroup;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "WAFER")
    private String wafer;
}
