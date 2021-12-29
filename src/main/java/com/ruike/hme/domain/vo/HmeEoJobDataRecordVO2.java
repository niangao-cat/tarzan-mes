package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;
import java.util.*;

/**
 * @Classname HmeEoJobDataRecordQueryDTO2
 * @Description 数据采集项查询结果
 * @Date 2020/11/23 18:37
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobDataRecordVO2 implements Serializable {
    private static final long serialVersionUID = 979326760535765305L;

    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String jobRecordId;

    @ApiModelProperty(value = "SN作业ID")
    private String jobId;

    @ApiModelProperty(value = "数据采集组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "数据采集项ID")
    private String tagId;

    @ApiModelProperty(value = "采集组用途")
    private String groupPurpose;

    @ApiModelProperty(value = "采集结果")
    private String result;

    @ApiModelProperty(value = "是否完成采集")
    private String hasResult;

    @ApiModelProperty(value = "数据收集项关系表ID")
    private String tagGroupAssignId;

    @ApiModelProperty(value = "下限值")
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "上限值")
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "标准值")
    private BigDecimal standardValue;

    @ApiModelProperty(value = "是否允许空值")
    private String valueAllowMissing;

    @ApiModelProperty(value = "符合值")
    private String trueValue;

    @ApiModelProperty(value = "不符合值")
    private String falseValue;

    @ApiModelProperty(value = "序号")
    private Double serialNumber;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "数据采集项")
    private String tagCode;

    @ApiModelProperty(value = "数据采集项描述")
    private String tagDescription;

    @ApiModelProperty(value = "数据采集项类型")
    private String valueType;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "数据类型")
    private String businessType;

    @ApiModelProperty(value = "序号")
    private Long orderNumber;

    /*
     * 下面是作业相关数据
     */

    @ApiModelProperty(value = "SN容器作业ID")
    private String jobContainerId;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;

    @ApiModelProperty(value = "进站时间")
    private Date siteInDate;

    @ApiModelProperty(value = "标准时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "是否时效达标")
    private String timeStandardFlag;

    @ApiModelProperty(value = "eoId")
    private String eoId;

    @ApiModelProperty(value = "工单Id")
    private String workOrderId;

    @ApiModelProperty(value = "物料Id")
    private String snMaterialId;

    @ApiModelProperty(value = "记录扩展Id")
    private String dataRecordExtendId;
}
