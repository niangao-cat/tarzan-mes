package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/05 12:14
 */
@Data
public class HmeSignInOutRecordVO implements Serializable {

    private static final long serialVersionUID = -1598671834475952543L;

    @ApiModelProperty(value = "部门")
    private String departmentName;

    @ApiModelProperty(value = "产线")
    private String prodLineName;

    @ApiModelProperty(value = "工段")
    private String workcellName;

    @ApiModelProperty(value = "工号")
    private String loginName;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "")
    private List<HmeSignInOutRecordVO2> hmeSignInOutRecordVO2List;

    @ApiModelProperty(value = "月总计")
    private BigDecimal numOfMonth;
}
