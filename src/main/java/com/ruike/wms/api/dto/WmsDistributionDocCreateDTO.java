package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsDistributionDemandVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.calendar.domain.entity.MtCalendarShift;

import java.util.List;

/**
 * 配送单生成参数
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/1 14:30
 */
@Data
public class WmsDistributionDocCreateDTO {

    @ApiModelProperty("班次信息")
    private List<MtCalendarShift> calendarShiftList;
    @ApiModelProperty("勾选行信息")
    private List<WmsDistributionDemandVO> demandList;
    @ApiModelProperty(value = "生成配送单方式",required = true)
    private String flag;
}
