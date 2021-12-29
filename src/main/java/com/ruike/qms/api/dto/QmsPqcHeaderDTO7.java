package com.ruike.qms.api.dto;

import com.ruike.qms.domain.vo.QmsPqcHeaderVO5;
import com.ruike.qms.domain.vo.QmsPqcHeaderVO8;
import com.ruike.qms.domain.vo.QmsPqcHeaderVO9;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * QmsPqcHeaderDTO7
 *
 * @author: chaonan.hu@hand-china.com 2020/8/24 21:37:25
 **/
@Data
public class QmsPqcHeaderDTO7 implements Serializable {
    private static final long serialVersionUID = -6628663566650219841L;

    @ApiModelProperty(value = "检验单头ID")
    private String pqcHeaderId;

    @ApiModelProperty(value = "检验单行和明细信息集合")
    private List<QmsPqcHeaderVO9> lineAndDetailsDataList;
}
