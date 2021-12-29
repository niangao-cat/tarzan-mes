package com.ruike.itf.api.dto;

import com.ruike.hme.domain.entity.HmeEquipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ItfSingleIfaceDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-09-27 10:08:12
 **/
@Data
public class ItfSingleIfaceDTO implements Serializable {
    private static final long serialVersionUID = 1391657163724678867L;

    @ApiModelProperty(value = "工位")
    private String workcellCode;

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

    @ApiModelProperty(value = "默认站点ID")
    private String defaultSiteId;

    @ApiModelProperty(value = "设备信息")
    private List<HmeEquipment> hmeEquipmentList;
}
