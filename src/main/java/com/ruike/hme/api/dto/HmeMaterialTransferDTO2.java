package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 12:00
 */
@Data
public class HmeMaterialTransferDTO2 extends HmeMaterialTransferDTO7 {

    private static final long serialVersionUID = -2950697706441942843L;

    private List<HmeMaterialTransferDTO> dtoList;

    private List<HmeMaterialTransferDTO6> targetDtoList;
}
