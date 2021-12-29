package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description iqc检验组信息
 *
 * @author quan.luo@hand-china.com 2020/12/02 11:22
 */
@Data
public class QmsIqcTagGroupVO {

    @ApiModelProperty(value = "检验组描述")
    private String tagGroupDescription;

    @ApiModelProperty(value = "检验组编码")
    private String tagGroupCode;
}
