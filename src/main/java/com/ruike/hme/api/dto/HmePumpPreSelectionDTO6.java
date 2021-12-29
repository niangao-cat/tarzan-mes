package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmePumpPreSelectionVO4;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmePumpPreSelectionDTO6
 *
 * @author: chaonan.hu@hand-china.com 2021/09/09 09:30:47
 **/
@Data
public class HmePumpPreSelectionDTO6 implements Serializable {
    private static final long serialVersionUID = -5805350213654974527L;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;

    @ApiModelProperty(value = "筛选批次LOV所对应的值", required = true)
    private String pumpPreSelectionId;
}
