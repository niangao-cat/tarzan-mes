package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeMaterialLotVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeCosGetChipSiteOutPrintDTO
 * @Description COS取片平台-出站打印输入参数
 * @Date 2020/8/19 19:55
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipSiteOutPrintDTO implements Serializable {
    private static final long serialVersionUID = 2131775044354768109L;

    @ApiModelProperty("工单工艺工位在制记录ID")
    private String operationRecordId;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty(value = "设备及其状态集合")
    private List<HmeWkcEquSwitchDTO6> equipmentList;

    @ApiModelProperty(value = "出站信息列表")
    private List<HmeMaterialLotVO> siteOutList;
}