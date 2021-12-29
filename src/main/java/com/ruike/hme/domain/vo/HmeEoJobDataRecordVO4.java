package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeEoJobDataRecordVO4
 * @Description 数据采集项视图
 * @Date 2020/11/23 18:37
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobDataRecordVO4 implements Serializable {
    private static final long serialVersionUID = -8834987511612761746L;

    @ApiModelProperty(value = "数据项ID")
    private String tagId;

    @ApiModelProperty(value = "数据项编码")
    private String tagCode;

    @ApiModelProperty(value = "数据项扩展属性 PROCESS_FLAG")
    private String processFlag;

    @ApiModelProperty(value = "数据项扩展属性 STANDARD")
    private String standard;

    @ApiModelProperty("tagDaqAttrId")
    private String tagDaqAttrId;

    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;

    @ApiModelProperty(value = "取值字段")
    private String valueField;

    @ApiModelProperty(value = "限制条件1")
    private String limitCond1;

    @ApiModelProperty(value = "条件1限制值")
    private String cond1Value;

    @ApiModelProperty(value = "限制条件2")
    private String limitCond2;

    @ApiModelProperty(value = "条件2限制值")
    private String cond2Value;
}
