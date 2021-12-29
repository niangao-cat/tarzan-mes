package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公式头表
 *
 * @author guiming.zhou@hand-china.com 2020/9/22
 */
@Data
public class HmeTagFormulaVO implements Serializable {

    private static final long serialVersionUID = -4574430090988542733L;
    @ApiModelProperty("租户id")
    private String operationId;
    @ApiModelProperty("数据组id")
    private String tagGroupId;
    @ApiModelProperty("数据项id")
    private String tagId;


}
