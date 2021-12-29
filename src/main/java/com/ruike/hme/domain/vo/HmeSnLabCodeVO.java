package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.common.query.Where;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * SN工艺实验代码表资源库查询 返回
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/22 12:41
 */
@Data
public class HmeSnLabCodeVO implements Serializable {

    private static final long serialVersionUID = -1773670437638679349L;

    @ApiModelProperty(value ="主键ID，标识唯一一条记录")
    private String kid;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "工艺id")
    private String operationId;

    @ApiModelProperty(value = "工艺")
    private String operationName;

    @ApiModelProperty(value = "工艺描述")
    private String description;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "实验备注")
    private String remark;

    @ApiModelProperty(value = "是否有效")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enabledFlagMeaning")
    private String enabledFlag;

    @ApiModelProperty(value = "是否有效")
    private String enabledFlagMeaning;
}
