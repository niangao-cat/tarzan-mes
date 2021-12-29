package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/6 22:44
 */
@Data
public class HmeTagCheckVO8 implements Serializable {

    private static final long serialVersionUID = -7250478890051635343L;

    @ApiModelProperty("数据项ID")
    @JsonIgnore
    private String tagId;
    @ApiModelProperty("数据项")
    private String tagCode;
    @ApiModelProperty("数据项描述")
    private String tagDescription;
    @ApiModelProperty("作业记录ID")
    private String jobId;
    @ApiModelProperty("结果")
    private String result;
}
