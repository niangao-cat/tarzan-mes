package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName ItfWorkOrderTimeChangeDTO
 * @Description 工单时间变更
 * @Author lkj
 * @Date 2020/12/17
 */
@Data
public class ItfWorkOrderTimeChangeDTO {

    @ApiModelProperty(value = "工单编码", required = true)
    private String workOrderNum;
    @ApiModelProperty(value = "开始时间,格式yyyy-MM-dd", required = true)
    private String startDate;
    @ApiModelProperty(value = "结束时间,格式yyyy-MM-dd", required = true)
    private String endDate;

    @ApiModelProperty(value = "ERP返回处理状态")
    private String status;
    @ApiModelProperty(value = "ERP返回处理处理信息")
    private String message;

    public ItfWorkOrderTimeChangeDTO(Map map) {
        this.workOrderNum = map.get("AUFNR").toString().replaceAll("^(0+)", "");
        this.startDate = map.get("GSTRP").toString();
        this.endDate = map.get("GLTRP").toString();
        this.status = map.get("TYPE").toString();
        this.message = map.get("MESSAGE").toString();
    }

    public ItfWorkOrderTimeChangeDTO() {

    }
}
