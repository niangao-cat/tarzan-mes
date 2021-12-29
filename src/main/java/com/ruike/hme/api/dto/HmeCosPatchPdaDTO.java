package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeCosPatchPdaVO5;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeCosPatchPdaDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/8/24 16:33:37
 **/
@Data
public class HmeCosPatchPdaDTO implements Serializable {
    private static final long serialVersionUID = -3910656030267159993L;

    @ApiModelProperty(value = "默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(value = "扫描条码", required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "班次Id", required = true)
    private String wkcShiftId;

    @ApiModelProperty(value = "投料数量")
    private BigDecimal assembleQty;

    @ApiModelProperty(value = "界面左边投入数据表格第一条数据，有则传")
    private HmeCosPatchPdaVO5 materialLotInfo;

}
