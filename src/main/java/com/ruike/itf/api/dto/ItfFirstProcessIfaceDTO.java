package com.ruike.itf.api.dto;

import com.ruike.hme.domain.entity.HmeEquipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:10
 */
@Data
public class ItfFirstProcessIfaceDTO implements Serializable {

    private static final long serialVersionUID = -4575434436184390153L;

    @ApiModelProperty(value = "工位")
    private String workcellCode;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "SN号")
    private String materialLotCode;

    @ApiModelProperty(value = "员工账号")
    private String user;

    @ApiModelProperty(value = "设备")
    private String scanAssetEncoding;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "默认站点ID")
    private String defaultSiteId;

    @ApiModelProperty(value = "设备信息")
    private List<HmeEquipment> hmeEquipmentList;

}
