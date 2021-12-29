package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeMaterialLotNcLoad;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeWoJobSnReturnDTO5
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/13 21:49
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnReturnDTO5 implements Serializable {
    private static final long serialVersionUID = -4472109342869883674L;

    @ApiModelProperty(value = "位置Id")
    private String materialLotLoadId;
    @ApiModelProperty(value = "位置序列号")
    private String loadSequence;
    @ApiModelProperty("热沉号")
    private String hotSinkCode;
    @ApiModelProperty("芯片序列")
    private String chipSequence;

    @ApiModelProperty(value = "行")
    private Long loadRow;
    @ApiModelProperty(value = "列")
    private Long loadColumn;
    @ApiModelProperty(value = "芯片数")
    private Long cosNum;

    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;

    @ApiModelProperty(value = "冻结标识含义")
    private String freezeFlagMeaning;

    @ApiModelProperty("芯片实验代码")
    private String chipLabCode;

    @ApiModelProperty("芯片实验代码备注")
    private String chipLabRemark;

    @ApiModelProperty(value = "不良位置")
    private List<HmeMaterialLotNcLoad> docList;
}
