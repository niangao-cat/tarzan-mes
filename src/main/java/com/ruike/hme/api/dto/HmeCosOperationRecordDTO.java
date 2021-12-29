package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeIncomingRecordDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/13 9:40
 * @Version 1.0
 **/
@Data
public class HmeCosOperationRecordDTO extends HmeCosOperationRecord implements Serializable {

    private static final long serialVersionUID = -7370121689368394077L;

    @ApiModelProperty("容器类型")
    private String containerTypeCode;
}
