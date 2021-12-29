package com.ruike.qms.domain.vo;

import io.choerodon.core.domain.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * QmsPqcHeaderVO6
 *
 * @author: chaonan.hu@hand-china.com 2020/8/18 10:51:29
 **/
@Data
public class QmsPqcHeaderVO6 implements Serializable {
    private static final long serialVersionUID = -17431501653311334L;

    @ApiModelProperty(value = "头数据")
    private QmsPqcHeaderVO4 headData;

    @ApiModelProperty(value = "行数据")
    private Page<QmsPqcHeaderVO5> lineData;
}
