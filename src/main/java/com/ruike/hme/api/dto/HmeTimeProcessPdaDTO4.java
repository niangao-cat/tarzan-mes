package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO3;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeTimeProcessPdaDTO4
 *
 * @author chaonan.hu@hand-china.com 2020-08-20 11:03:26
 **/
@Data
public class HmeTimeProcessPdaDTO4 implements Serializable {
    private static final long serialVersionUID = 4034305923049690779L;

    @ApiModelProperty(value = "物料数据", required = true)
    private List<HmeTimeProcessPdaVO3> materialDataList;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

}
