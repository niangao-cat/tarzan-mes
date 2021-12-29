package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * COS测试良率监控头表 查询条件
 *
 * @author wengang.qiang@hand-china.com 2021/09/16 14:48
 */
@Data
public class HmeCosTestMonitorHeaderDTO implements Serializable {

    private static final long serialVersionUID = -7081102842422206187L;

    @ApiModelProperty(value = "监控单据号,以逗号分隔的字符串")
    private String monitorDocNum;

    @ApiModelProperty(value = "监控单据号")
    private List<String> monitorDocNumList;

    @ApiModelProperty(value = "单据状态")
    private String docStatus;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "WAFER")
    private List<String> waferList;

    @ApiModelProperty(value = "WAFER,以逗号分隔的字符串")
    private String wafer;

    @ApiModelProperty(value = "创建日期从")
    private Date creationDateFrom;

    @ApiModelProperty(value = "创建日期到")
    private Date creationDateTo;

    public void initParam() {
        this.monitorDocNumList = StringUtils.isNotBlank(this.monitorDocNum) ? Arrays.asList(StringUtils.split(this.monitorDocNum, ",")) : new ArrayList<>();
    }
    public void initParamWafer(){
        this.waferList = StringUtils.isNotBlank(this.wafer) ? Arrays.asList(StringUtils.split(this.wafer, ",")) : new ArrayList<>();
    }
}
