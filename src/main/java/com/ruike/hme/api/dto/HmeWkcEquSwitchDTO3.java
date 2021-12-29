package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * HmeWkcEquSwitchDTO3
 * @author: chaonan.hu@hand-china.com 2020-06-23 13:59:28
 **/
@Data
public class HmeWkcEquSwitchDTO3 implements Serializable {
    private static final long serialVersionUID = -2691580216109807946L;

    @ApiModelProperty(value = "设备类编码")
    @LovValue(lovCode = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryDesc")
    private String equipmentCategory;

    @ApiModelProperty(value = "设备类描述")
    private String equipmentCategoryDesc;

    @ApiModelProperty(value = "设备Id")
    private String equipmentId;

    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;

    @ApiModelProperty(value = "设备描述")
    private String descriptions;

    @ApiModelProperty("工位ID")
    private String workcellId;
}
