package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeTagDaqAttr;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * HmeTagDaqAttrVO
 *
 * @author chaonan.hu@hand-china.com 2020/07/21 12:43:19
 */
@Data
public class HmeTagDaqAttrVO2 implements Serializable {
    private static final long serialVersionUID = 4341793038265920558L;

    @ApiModelProperty("主键")
    private String tagDaqAttrId;

    @ApiModelProperty("数据项Id")
    private String tagId;

    @ApiModelProperty("设备类别")
    private String equipmentCategory;

    @ApiModelProperty("取值字段")
    private String valueField;

    @ApiModelProperty("取值字段含义")
    private String valueFieldMeaning;

    @ApiModelProperty("限制条件1")
    private String limitCond1;

    @ApiModelProperty("限制条件1含义")
    private String limitCond1Meaning;

    @ApiModelProperty("条件1限制值")
    private String cond1Value;

    @ApiModelProperty("限制条件2")
    private String limitCond2;

    @ApiModelProperty("限制条件2含义")
    private String limitCond2Meaning;

    @ApiModelProperty("条件2限制值")
    private String cond2Value;
}
