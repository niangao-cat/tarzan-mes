package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/18 14:46
 */
@Data
public class HmeDataRecordVO implements Serializable {

    private static final long serialVersionUID = 6722227912418000387L;

    @ApiModelProperty(value = "检验结果")
    public String result;

    @ApiModelProperty(value = "备注")
    public String remark;

    @ApiModelProperty(value = "检验项名称")
    private String tagName;

    @ApiModelProperty(value = "检验项编码")
    private String tagCode;

    @ApiModelProperty(value = "检验项英文名")
    private String tagAlias;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "指标值")
    private String target;
}
