package com.ruike.hme.api.dto;


import java.io.Serializable;
import java.util.Date;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工上下岗员工信息
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Data
public class HmeSignInOutRecordDTO8 implements Serializable {
	private static final long serialVersionUID = 7878732768964491855L;
	@ApiModelProperty(value = "班次")
	private String shiftCode;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
    @ApiModelProperty(value = "工位名称")
    private String workcellName;
    @ApiModelProperty(value = "生产线")
    private String prodLineName;
    @ApiModelProperty(value = "车间")
    private String areaName;
    @ApiModelProperty(value = "工厂")
    private String siteName;
    @ApiModelProperty(value = "事项（分为开班 OPEN、离岗OFF 、上岗ON、结班CLOSE四个状态）")
    private String operation;
    @ApiModelProperty(value = "操作时间")
    private Date operationDate;
    @ApiModelProperty(value = "累计时长")
    private String duration;
    @ApiModelProperty(value = "离岗原因")
    private String reason;


}
