package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeEoJobTimeSnVO2
 *
 * @author liyuan.lv@hand-china.com 2020/06/19 10:59
 */
@Data
public class HmeEoJobTimeSnVO2 implements Serializable {

    private static final long serialVersionUID = 4819528358932623623L;

    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("SN编码")
    private String snNum;
    @ApiModelProperty("SN类型")
    private String snType;
    @ApiModelProperty("容器作业ID")
    private String jobContainerId;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("当前容器EO个数")
    private Integer sumEoCount;
    @ApiModelProperty("前工序")
    private String previousWkcId;
    @ApiModelProperty("货位")
    private String locatorId;
    @ApiModelProperty("标准时长")
    private BigDecimal standardReqdTimeInProcess;
    @ApiModelProperty("进站操作人ID")
    private Long siteInBy;
    @ApiModelProperty("进站操作人")
    private String siteInByName;
    @ApiModelProperty("进站时间")
    private Date siteInDate;
    @ApiModelProperty("出站操作人ID")
    private Long siteOutBy;
    @ApiModelProperty("出站操作人")
    private String siteOutByName;
    @ApiModelProperty("出站时间")
    private Date siteOutDate;
    @ApiModelProperty("EO行列表")
    private List<HmeEoJobTimeSnVO3> lineList;
    @ApiModelProperty("是否可以点击加工完成")
    private String isClickProcessComplete;
    @ApiModelProperty("返修标识")
    private String reworkFlag;

    @ApiModelProperty("不良发起工位编码")
    private String ncRecordWorkcellCode;

    @ApiModelProperty("不良发起工位名称")
    private String ncRecordWorkcellName;

    @ApiModelProperty(value = "指定工艺路线返修标识")
    private String designedReworkFlag;

    @ApiModelProperty("禁止点击继续返修 Y:不允许点击继续返修")
    private String prohibitClickContinueReworkFlag;

    @ApiModelProperty("实验代码&备注")
    List<HmeEoJobTimeSnVO5> labCodeAndRemarkList;
}
