package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.List;

import com.ruike.hme.domain.vo.HmeEoJobSnVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchDTO3 implements Serializable {
    private static final long serialVersionUID = 4913817632490762445L;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "进站SN列表")
    List<HmeEoJobSnVO> dtoList;
}
