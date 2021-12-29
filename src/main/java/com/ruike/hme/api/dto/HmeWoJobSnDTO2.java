package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeWoJobSnDTO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/12 18:53
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnDTO2 implements Serializable {
    private static final long serialVersionUID = 4747592960750011850L;

    @ApiModelProperty("工艺id")
    private String operationId;

    @ApiModelProperty("容器类型")
    private String containerTypeCode;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("工单Id")
    private String workOrderId;

    @ApiModelProperty("站点Id")
    private String siteId;


    @ApiModelProperty("工单Id")
    private String barNum;
}
