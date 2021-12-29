package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.*;
import java.math.*;
import java.util.*;

/**
 * @Classname QmsOqcHeaderVO
 * @Description 检验单头视图
 * @Date 2020/8/28 15:21
 * @Author yuchao.wang
 */
@Data
public class QmsOqcHeaderVO implements Serializable {
    private static final long serialVersionUID = -3776827613752919258L;
    
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    
    @ApiModelProperty("检验单头表主键")
    private String oqcHeaderId;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
    
    @ApiModelProperty(value = "检验单号")
    private String oqcNumber;
    
    @ApiModelProperty(value = "工单")
    private String woId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    
    @ApiModelProperty(value = "EO_ID")
    private String eoId;

    @ApiModelProperty(value = "EO编号")
    private String eoNum;
    
    @ApiModelProperty(value = "销售订单头")
    private String soNumber;
    
    @ApiModelProperty(value = "销售订单行")
    private String soLineNumber;

    @LovValue(lovCode = "QMS.PQC_INSPECTION_STATUS", meaningField="inspectionStatusMeaning")
    @ApiModelProperty(value = "检验状态")
    private String inspectionStatus;

    @ApiModelProperty(value = "检验状态含义")
    private String inspectionStatusMeaning;

    @ApiModelProperty(value = "建单日期")
    private Date createdDate;

    @ApiModelProperty(value = "检验时长（单位：小时）")
    private BigDecimal inspectionTime;

    @ApiModelProperty(value = "完成时间")
    private Date inspectionFinishDate;

    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "检验单行信息")
    private List<QmsOqcLineVO> lineList;
}