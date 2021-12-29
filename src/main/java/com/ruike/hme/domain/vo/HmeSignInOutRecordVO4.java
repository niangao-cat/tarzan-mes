package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工出勤报表 对应数据库查询出的字段
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/05 14:22
 */
@Data
public class HmeSignInOutRecordVO4 implements Serializable {


    private static final long serialVersionUID = 5528446021903507837L;

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

    @ApiModelProperty(value = "事项（分为开班 OPEN、离岗OFF 、上岗ON、结班CLOSE四个状态）")
    private String operation;

    @ApiModelProperty(value = "操作时间")
    private Date operationDate;

    @ApiModelProperty(value = "累计时长")
    private String duration;

    @ApiModelProperty(value = "离岗原因")
    @LovValue(value = "HME.STAFF_OFFREASON", meaningField = "reasonMeaning")
    private String reason;

    @ApiModelProperty(value = "离岗原因")
    private String reasonMeaning;

    @ApiModelProperty(value = "日期")
    private Date creationDate;

    @ApiModelProperty(value = "日期")
    private Date date;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "班次开始时间")
    private Date shiftStartTime;

    @ApiModelProperty(value = "班次结束时间")
    private Date shiftEndTime;

    public HmeSignInOutRecordVO4(String departmentName, String prodLineName, String workcellName, String loginName, String realName, String shiftCode) {
        this.departmentName = departmentName;
        this.prodLineName = prodLineName;
        this.workcellName = workcellName;
        this.loginName = loginName;
        this.realName = realName;
        this.shiftCode = shiftCode;
    }
}
