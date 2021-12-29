package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后返品拆机
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 17:10:57
 */
@Data
public class HmeServiceSplitRecordVO implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty("当前用户默认站点ID")
    private String siteId;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工艺路线ID")
    private String operationId;
    @ApiModelProperty("班组ID")
    private String wkcShiftId;
    @ApiModelProperty("SN编码")
    private String snNum;
    @ApiModelProperty("实物返回属性")
    private String backType;

    public HmeServiceSplitRecordVO() {
    }

    public HmeServiceSplitRecordVO(String siteId, String workcellId, String operationId, String wkcShiftId, String snNum, String backType) {
        this.siteId = siteId;
        this.workcellId = workcellId;
        this.operationId = operationId;
        this.wkcShiftId = wkcShiftId;
        this.snNum = snNum;
        this.backType = backType;
    }
}
