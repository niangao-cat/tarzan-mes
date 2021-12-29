package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * HmeEoJobSnVO4
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnVO4 implements Serializable {

    private static final long serialVersionUID = -6402722042057089309L;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工位编码")
    private String workcellCode;
    @ApiModelProperty("工位名称")
    private String workcellName;
    @ApiModelProperty("速率")
    private Double rate;
    @ApiModelProperty("开动率")
    private Double activity;
    @ApiModelProperty("工位是否容器出站")
    private Integer isContainerOut;
    @ApiModelProperty("SN容器ID")
    private String jobContainerId;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("工艺路线ID列表")
    private List<String> operationIdList;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("是否首编号生成")
    private String autoNumber;
    @ApiModelProperty("位数限制")
    private String digitLimit;

    @ApiModelProperty("班组ID")
    private String wkcShiftId;

    @ApiModelProperty("工位工序名称")
    private String wkcStepName;
    @ApiModelProperty("工位工序名称")
    private String equipmentCode;
    @ApiModelProperty("工位工序名称")
    private String equipmentName;

    @ApiModelProperty("工段Id")
    private String wkcLineId;

    @ApiModelProperty("工序ID")
    private String processId;
    @ApiModelProperty("工序名称")
    private String processName;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("产线编码")
    private String ProdLineCode;
    @ApiModelProperty("产线名称")
    private String prodLineName;

    @ApiModelProperty("工序作业平台-SN作业绑定容器")
    private HmeEoJobContainerVO2 hmeEoJobContainerVO2;
    @ApiModelProperty("批量工序作业平台-SN列表")
    private List<HmeEoJobSnVO> hmeEoJobSnVOList;
    @ApiModelProperty("工序作业平台-计划外物料")
    private List<HmeEoJobBeyondMaterial> hmeEoJobBeyondMaterialList;
    @ApiModelProperty("批次物料")
    private List<HmeEoJobLotMaterialVO> lotMaterialVOList;
    @ApiModelProperty("时效物料")
    private List<HmeEoJobTimeMaterialVO> timeMaterialVOList;

    @ApiModelProperty("工艺扩展表底座二维位数下限")
    private String digitLimitUp;
    @ApiModelProperty("工序编码")
    private String processCode;
}
