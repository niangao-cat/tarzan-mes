package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeWoJobSnReturnDTO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/13 10:40
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnReturnDTO3 implements Serializable {
    private static final long serialVersionUID = 4264021426193159203L;

    @ApiModelProperty(value = "eoJobId")
    private String eoJobSnId;
    @ApiModelProperty(value = "wojobid")
    private String woJobSnId;
    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "芯片编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
    @ApiModelProperty(value = "cos类型")
    private String cosType;
    @ApiModelProperty(value = "已入数量")
    private String primaryUonQty;
    @ApiModelProperty(value = "来料批次")
    private String jobBatch;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "进出站数据")
    private List<HmeWoJobSnReturnDTO4> dtoList;
    @ApiModelProperty(value = "图示信息")
    private List<HmeWoJobSnReturnDTO5> HmeWoJobSnReturnDTO5List;



}
