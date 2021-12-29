package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosWorkcellExceptionDTO
 * COS工位加工异常汇总表输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 12:38
 */
@Data
public class HmeCosWorkcellExceptionDTO implements Serializable {

    private static final long serialVersionUID = 5095294321248495473L;

    @ApiModelProperty(value = "工位")
    private String workcellId;
    @ApiModelProperty(value = "工单")
    private String workOrderNum;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "WAFER")
    private String waferNum;
    @ApiModelProperty(value = "产品编码")
    private String materialId;
    @ApiModelProperty(value = "操作人")
    private String id;
    @ApiModelProperty(value = "不良代码")
    private String ncCodeId;

}
