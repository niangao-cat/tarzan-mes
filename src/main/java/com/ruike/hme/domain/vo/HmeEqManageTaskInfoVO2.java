package com.ruike.hme.domain.vo;

import io.choerodon.core.domain.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/14 13:44
 */
@Data
public class HmeEqManageTaskInfoVO2 implements Serializable {
    private static final long serialVersionUID = 5844323839089898457L;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME_MAINTENANCE_STATUS", meaningField = "docStatusMeaning")
    private String docStatus;

    @ApiModelProperty(value = "状态")
    private String docStatusMeaning;

    @ApiModelProperty(value = "已完成点检项数量")
    private int completeQty;

    @ApiModelProperty(value = "点检项总数量")
    private int totalQty;

    @ApiModelProperty(value = "分页数据")
    private Page<HmeEqManageTaskInfoVO> pageData;
}
