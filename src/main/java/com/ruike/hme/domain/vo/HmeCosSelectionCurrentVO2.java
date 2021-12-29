package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeCosSelectionCurrentVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/08/18 14:29
 **/
@Data
public class HmeCosSelectionCurrentVO2 implements Serializable {
    private static final long serialVersionUID = -7600455473802249957L;

    @ApiModelProperty("主键")
    private String cosHisId;

    @ApiModelProperty("COS筛选电流点维护表ID")
    private String cosId;

    @ApiModelProperty("COS类型")
    @LovValue(value = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty("COS类型含义")
    private String cosTypeMeaning;

    @ApiModelProperty("电流")
    private String current;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enbaleFlagMeaning")
    private String enbaleFlag;

    @ApiModelProperty("有效性含义")
    private String enbaleFlagMeaning;

    @ApiModelProperty("创建人ID")
    private Long createdBy;

    @ApiModelProperty("创建人姓名")
    private String createdByName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty("更新人ID")
    private Long lastUpdatedBy;

    @ApiModelProperty("更新人姓名")
    private String lastUpdatedByName;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;
}
