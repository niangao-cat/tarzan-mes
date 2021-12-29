package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO6;
import com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 芯片转移-完成VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/18 0:50
 */
@Data
public class HmeChipTransferVO3 implements Serializable {

    private static final long serialVersionUID = -343357713278160606L;

    @ApiModelProperty("eoJobId")
    private String eoJobSnId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("来源条码ID")
    private String transMaterialLotId;

    @ApiModelProperty("来源条码编码")
    private String transMaterialLotCode;

    @ApiModelProperty("目标条码")
    private List<HmeWoJobSnReturnDTO3> targetList;

    @ApiModelProperty(value = "设备及其状态集合")
    private List<HmeWkcEquSwitchDTO6> equipmentList;

}
