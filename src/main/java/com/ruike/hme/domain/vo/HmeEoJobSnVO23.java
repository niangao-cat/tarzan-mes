package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

@Data
public class HmeEoJobSnVO23 extends HmeEoJobSn implements Serializable {
    private static final long serialVersionUID = -336363853315136033L;
    @ApiModelProperty("容器编码")
    String sourceContainerCode;
}
