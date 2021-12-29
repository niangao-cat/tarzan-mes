package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeEoTestDataRecordDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/16 19:42
 * @Version 1.0
 **/
@Data
public class HmeEoJobDataRecordReturnDTO implements Serializable {

    private static final long serialVersionUID = 4987232520734923034L;


    @ApiModelProperty(value = "机型")
    private String snMaterialId;

    @ApiModelProperty(value = "机型")
    private String materialCode;

    @ApiModelProperty(value = "机型描述")
    private String materialName;

    @ApiModelProperty(value = "工序")
    private String operationId;

    @ApiModelProperty(value = "工序")
    private String operationName;

    @ApiModelProperty(value = "检验数")
    private Long inspectionNum;

    @ApiModelProperty(value = "不良数")
    private Long ncNum;

    @ApiModelProperty(value = "不良率")
    private String ncRate;

    @ApiModelProperty(value = "检验员")
    private List<HmeEoJobDataRecordReturnDTO2> hmeEoJobDataRecordReturnDTO2s;

}
