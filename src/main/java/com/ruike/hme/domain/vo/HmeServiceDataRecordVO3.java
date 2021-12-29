package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/3 21:58
 */
@Data
public class HmeServiceDataRecordVO3 implements Serializable {

    private static final long serialVersionUID = -3508104741310042720L;

    @ApiModelProperty(value = "关联表id")
    private String serviceDataRecordLineId;

    @ApiModelProperty(value = "收集项id")
    private String tagId;

    @ApiModelProperty(value = "收集项描述")
    private String tagDescription;

    @ApiModelProperty(value = "结果")
    private String result;
}
