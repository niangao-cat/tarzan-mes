package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeDataCollectHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 生产数据采集请求VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/17 16:35
 */
@Data
public class HmeDataCollectLineVO3 extends HmeDataCollectHeader implements Serializable {

    @ApiModelProperty(value = "行列表信息")
    private List<HmeDataCollectLineVO2> lineContent;

    @ApiModelProperty(value = "工位描述")
    private String workCellName;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "标识 0-未采集 1-已采集")
    private String collectFlag;
}
