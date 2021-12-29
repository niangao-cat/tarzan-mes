package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * HmeWkcEquSwitchDTO4
 * @author: chaonan.hu@hand-china.com 2020-06-23 14:39:25
 **/
@Data
public class HmeWkcEquSwitchDTO4 implements Serializable {
    private static final long serialVersionUID = 2631693552598411044L;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty(value = "设备类编码")
    private String equipmentCategory;

    @ApiModelProperty(value = "设备类描述")
    private String equipmentCategoryDesc;

    @ApiModelProperty(value = "更新前设备编码")
    private String assetEncodingFirst;

    @ApiModelProperty(value = "更新前设备描述")
    private String descriptionsFirst;

    @ApiModelProperty(value = "更新后设备编码")
    private String assetEncodingLast;

    @ApiModelProperty(value = "更新后设备描述")
    private String descriptionsLast;
}
