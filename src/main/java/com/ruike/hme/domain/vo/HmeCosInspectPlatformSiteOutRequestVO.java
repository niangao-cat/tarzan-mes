package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO6;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * COS检验工作台-COS条码完成检验出站 输入参数
 */
@Data
public class HmeCosInspectPlatformSiteOutRequestVO implements Serializable {

    private static final long serialVersionUID = -5448928797296770520L;

    @ApiModelProperty("物料批条码ID")
    private String materialLotId;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("工单工艺工位在制记录ID")
    private String operationRecordId;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty(value = "设备及其状态集合")
    private List<HmeWkcEquSwitchDTO6> equipmentList;

    @ApiModelProperty("装载信息")
    private List<HmeMaterialLotLoadVO2> materialLotLoadList;

}
