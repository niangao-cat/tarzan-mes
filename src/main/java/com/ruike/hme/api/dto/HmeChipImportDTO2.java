package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeChipImportVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeChipImportDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021/01/25 15:12:45
 **/
@Data
public class HmeChipImportDTO2 extends HmeChipImportVO implements Serializable {
    private static final long serialVersionUID = 2860894784024384418L;

    @ApiModelProperty("工单")
    private String workNum;

    @ApiModelProperty("导入时间从")
    private Date creationDateFrom;

    @ApiModelProperty("导入时间至")
    private Date creationDateTo;
}
