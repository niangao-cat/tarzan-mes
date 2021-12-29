package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * COS检验工作台-待COS检验条码进站 返回参数
 */
@Data
public class HmeCosInspectPlatformScanMaterialLotCodeResponseVO implements Serializable {

    private static final long serialVersionUID = 7172164739993930882L;

    @ApiModelProperty("当前在制记录")
    private HmeCosInspectPlatformAutoQueryInfoResponseVO queryInfoResponseVO;

    // 以下是盒子行列信息，每个芯片的状态
    @ApiModelProperty("行数")
    private String locationRow;

    @ApiModelProperty("列数")
    private String locationColumn;

    @ApiModelProperty("芯片数")
    private String chipNum;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("装载信息")
    private List<HmeMaterialLotLoadVO2> materialLotLoadList;

}
