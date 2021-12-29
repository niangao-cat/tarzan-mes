package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/6 20:44
 */
@Data
public class HmeTagCheckVO6 implements Serializable {

    private static final long serialVersionUID = -607506142213851079L;

    @ApiModelProperty("工序名称")
    private String processName;
    @ApiModelProperty("工序编码")
    private String processCode;
    @ApiModelProperty("数据项信息")
    private List<HmeTagCheckVO8> tagList;
}
