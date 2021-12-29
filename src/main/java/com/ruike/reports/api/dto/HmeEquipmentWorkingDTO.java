package com.ruike.reports.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 设备运行情况明细
 *
 * @author li.zhang 2021/01/14 11:08
 */
@Data
public class HmeEquipmentWorkingDTO {

    @ApiModelProperty("资产编码")
    private List<String> equipmentCodeList;

    @ApiModelProperty("设备类别")
    private String equipmentCategory;

    @ApiModelProperty("部门")
    private String areaId;

    @ApiModelProperty("车间")
    private String workShopId;

    @ApiModelProperty("运行时间从")
    @NotNull
    private String creationDateFrom;

    @ApiModelProperty("运行时间至" )
    @NotNull
    private String creationDateTo;

    @ApiModelProperty("设备名称")
    private String assetName;

    @ApiModelProperty("设备ID")
    private String equipmentId;

    @ApiModelProperty("工位ID")
    private List<String> workcellIdList;
}
