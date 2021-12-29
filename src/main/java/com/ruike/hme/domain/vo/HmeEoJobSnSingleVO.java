package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeRepairLimitCount;
import com.ruike.hme.domain.entity.HmeRepairRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

@Data
public class HmeEoJobSnSingleVO implements Serializable {

    private static final long serialVersionUID = -8869146263745463212L;

    @ApiModelProperty("是否容器进站")
    Boolean isContainer;

    @ApiModelProperty("进站SN")
    HmeEoJobSnVO3 snLine;

    @ApiModelProperty("SN物料信息列表")
    HmeEoJobSnVO5 snVO;

    @ApiModelProperty("EO当前步骤")
    HmeRouterStepVO5 currentStep;

    @ApiModelProperty("EO最近步骤")
    HmeRouterStepVO nearStep;

    @ApiModelProperty("EO正常所有加工步骤")
    List<HmeRouterStepVO> normalStepList;

    @ApiModelProperty("是否返修")
    Boolean isRework;

    @ApiModelProperty("是否指定工艺路线返修")
    Boolean isDesignedRework;

    @ApiModelProperty("EO 工艺路线 装配清单关系")
    HmeEoRouterBomRelVO eoRouterBomRel;

    @ApiModelProperty("是否查询")
    String isQuery;

    @ApiModelProperty("已经存在的作业")
    HmeEoJobSnVO23 exitEoJobSn;

    @ApiModelProperty("是否可以点击加工完成")
    private String isClickProcessComplete;

    @ApiModelProperty("禁止点击继续返修 Y:不允许点击继续返修")
    private String prohibitClickContinueReworkFlag;

    @ApiModelProperty("不良发起工位编码")
    private String ncRecordWorkcellCode;

    @ApiModelProperty("不良发起工位名称")
    private String ncRecordWorkcellName;

    @ApiModelProperty("完成步骤标识 Y:完成步骤 其他:非完成步骤")
    private String doneStepFlag;

    @ApiModelProperty(value = "入口步骤标识")
    private String entryStepFlag;

    @ApiModelProperty("是否可以点击抽检/全检")
    private String isClickInspectionBtn;

    @ApiModelProperty("返修进站次数限制信息")
    private HmeRepairLimitCount repairLimitCount;

    @ApiModelProperty("返修进站次数信息")
    private HmeRepairRecord repairRecord;
}
