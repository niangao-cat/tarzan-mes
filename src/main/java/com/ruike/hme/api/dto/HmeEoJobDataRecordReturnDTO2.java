package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeEoTestDataRecordReturnDTO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/17 9:17
 * @Version 1.0
 **/
@Data
public class HmeEoJobDataRecordReturnDTO2 implements Serializable{
    private static final long serialVersionUID = -3811703274177663498L;

    @ApiModelProperty(value = "检验数")
    private Long userId;

    @ApiModelProperty(value = "机型描述")
    private String userName;

    @ApiModelProperty(value = "检验数")
    private Long inspectionNum;

}
