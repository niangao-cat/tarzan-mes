package com.ruike.hme.domain.vo;

import java.io.Serializable;

import com.ruike.hme.domain.entity.HmeTagNc;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * HmeTagNcVO
 *
 * @author penglin.sui@hand-china.com 2020/09/24 11:42
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeTagNcVO extends HmeTagNc implements Serializable {
    private static final long serialVersionUID = 8180073424927971469L;

    @ApiModelProperty(value = "数据项编码")
    private String tagCode;
    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;
    @ApiModelProperty(value = "不良代码")
    private String ncCode;
    @ApiModelProperty(value = "不良代码描述")
    private String ncCodeDescription;
}
