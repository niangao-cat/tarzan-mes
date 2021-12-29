package com.ruike.hme.domain.vo;

import java.io.Serializable;

import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HmeEoJobSnLotMaterialVO3 implements Serializable {

    private static final long serialVersionUID = -3287237969642455376L;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("事件请求ID")
    private String eventRequestId;
}
