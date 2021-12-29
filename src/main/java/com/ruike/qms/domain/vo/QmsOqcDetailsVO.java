package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname QmsOqcDetailsVO
 * @Description 检验单明细视图
 * @Date 2020/8/28 15:32
 * @Author yuchao.wang
 */
@Data
public class QmsOqcDetailsVO implements Serializable {
    private static final long serialVersionUID = -5061667132256286322L;

    @ApiModelProperty(value = "检验单明细主键ID")
    private String oqcDetailsId;
    
    @ApiModelProperty(value = "检验单头表ID")
    private String oqcHeaderId;
    
    @ApiModelProperty(value = "检验单行主键ID")
    private String oqcLineId;

    @ApiModelProperty(value = "序号")
    private Long number;

    @ApiModelProperty(value = "结果值")
    private String result;

    @ApiModelProperty(value = "备注")
    private String remark;
}