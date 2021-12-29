package com.ruike.itf.api.dto;

import com.ruike.hme.domain.entity.HmeEquipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/28 16:27
 */
@Data
public class ItfTimeProcessIfaceDTO implements Serializable {

    private static final long serialVersionUID = 2281446604342009970L;

    @ApiModelProperty(value = "工位")
    private String workcellCode;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "jobType")
    private String jobType;

    @ApiModelProperty(value = "用户")
    private String user;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "设备")
    private String scanAssetEncoding;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "默认站点ID")
    private String defaultSiteId;

    @ApiModelProperty(value = "设备信息")
    private List<HmeEquipment> hmeEquipmentList;
}
