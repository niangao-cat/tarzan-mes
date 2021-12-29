package com.ruike.itf.api.dto;

import com.ruike.hme.domain.entity.HmeEquipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * ItfMaterialLotSiteDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021-10-12 15:30:13
 **/
@Data
@ToString
public class ItfMaterialLotSiteDTO2 implements Serializable {

    @ApiModelProperty(value = "工位")
    private String workcellCode;

    @ApiModelProperty(value = "SN号")
    private String materialLotCode;

    @ApiModelProperty(value = "员工账号")
    private String user;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "SNID")
    private String materialLotId;

    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @ApiModelProperty(value = "默认站点ID")
    private String defaultSiteId;
}
