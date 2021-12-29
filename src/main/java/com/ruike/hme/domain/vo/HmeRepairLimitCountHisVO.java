package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yaqiong.zhou@raycus.com 2021/10/8 14:26
 */
@Data
public class HmeRepairLimitCountHisVO implements Serializable {
    private static final long serialVersionUID = 6700178522468530766L;

    @ApiModelProperty(value = "主键Id")
    private String repairLimitCountHisId;
    @ApiModelProperty("主表主键Id")
    private String repairLimitCountId;
    @ApiModelProperty("工序Id")
    private String workcellId;
    @ApiModelProperty("工序编码")
    private String workcellCode;
    @ApiModelProperty("工序名称")
    private String workcellName;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("限制次数")
    private Long limitCount;
    @LovValue(value = "Z.FLAG_YN", meaningField ="enableFlagMeaning")
    @ApiModelProperty("有效性")
    private String enableFlag;
    @ApiModelProperty("有效性含义")
    private String enableFlagMeaning;
    @ApiModelProperty("修改人userId")
    private Long lastUpdatedBy;
    @ApiModelProperty("用户姓名")
    private String realName;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;
    @ApiModelProperty("部门Id")
    private String departmentId;
    @ApiModelProperty("部门编码")
    private String departmentCode;
    @ApiModelProperty("部门名称")
    private String departmentName;
}
