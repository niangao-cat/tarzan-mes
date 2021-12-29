package com.ruike.itf.api.dto;

import com.ruike.hme.domain.entity.HmeEquipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * ItfMaterialLotSiteDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-10-11 17:04:54
 **/
@Data
@ToString
public class ItfMaterialLotSiteDTO implements Serializable {

    @ApiModelProperty(value = "工位")
    private String workcellCode;

    @ApiModelProperty(value = "SN号")
    private String materialLotCode;

    @ApiModelProperty(value = "员工账号")
    private String user;

    @ApiModelProperty(value = "设备,以逗号分隔的字符串")
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
