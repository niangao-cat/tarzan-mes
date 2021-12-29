package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * HmeProductionPrintVO3
 *
 * @author chaonan.hu@hand-china.com 2021/10/15 11:19
 */
@Data
public class HmeProductionPrintVO3 implements Serializable {
    private static final long serialVersionUID = -2949024318679253694L;

    @ApiModelProperty(value = "执行作业ID")
    private String eoId;

    @ApiModelProperty(value = "执行作业编码")
    private String eoNum;

    @ApiModelProperty(value = "AC")
    private String ac;

    @ApiModelProperty(value = "DC")
    private String dc;

    @ApiModelProperty(value = "泵浦源")
    private String pump;

    @ApiModelProperty(value = "输出光缆")
    private String opticalCable;

    @ApiModelProperty(value = "航插型号")
    private String aerPlugModel;

    @ApiModelProperty(value = "上位机版本")
    private String hostComVer;

    @ApiModelProperty(value = "主控板型号")
    private String mainControlMod;

    @ApiModelProperty(value = "主控板程序")
    private String mainControlProgram;

    @ApiModelProperty(value = "光模控制板型号")
    private String optModeControlModel;

    @ApiModelProperty(value = "光模控制板程序")
    private String optModeControlProgram;

    @ApiModelProperty(value = "合束控制板型号")
    private String comBundleControlModel;

    @ApiModelProperty(value = "合束控制板程序")
    private String comBundleControlProgram;
}
