package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeServiceSplitRecordVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.io.Serializable;

/**
 * HmeServiceSplitRecordDTO4
 *
 * @author chaonan.hu@hand-china.com 2021-05-25 14:46:12
 */
@Data
public class HmeServiceSplitRecordDTO4 implements Serializable {

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "撤销SN", required = true)
    private String snNum;
}
