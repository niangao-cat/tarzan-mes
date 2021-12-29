package com.ruike.qms.api.dto;

import com.ruike.qms.domain.vo.QmsPqcHeaderVO4;
import com.ruike.qms.domain.vo.QmsPqcHeaderVO5;
import com.ruike.qms.domain.vo.QmsPqcHeaderVO8;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * QmsPqcHeaderDTO6
 *
 * @author: chaonan.hu@hand-china.com 2020/8/18 14:42:38
 **/
@Data
public class QmsPqcHeaderDTO6 implements Serializable {
    private static final long serialVersionUID = 4534611007454169439L;

    @ApiModelProperty(value = "检验单头ID", required = true)
    private String pqcHeaderId;

    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;

    @ApiModelProperty(value = "备注")
    private String remark;
}
