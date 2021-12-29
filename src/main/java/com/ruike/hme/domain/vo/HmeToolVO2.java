package com.ruike.hme.domain.vo;

import java.io.Serializable;

import com.ruike.hme.domain.entity.HmeTool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeToolVO2
 *
 * @author penglin.sui 2021/03/22 16:33
 */

@Data
public class HmeToolVO2 extends HmeTool implements Serializable {
    private static final long serialVersionUID = 5972235957805528902L;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
}
