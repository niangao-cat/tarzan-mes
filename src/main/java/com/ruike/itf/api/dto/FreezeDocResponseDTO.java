package com.ruike.itf.api.dto;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import static com.ruike.hme.infra.constant.HmeConstants.StatusCode.NEW;
import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.ERROR;
import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.SUCCESS;

/**
 * <p>
 * 冻结单接口返回 响应字段
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/3 10:27
 */
@Data
public class FreezeDocResponseDTO implements Serializable {
    private static final long serialVersionUID = -1723471944879830716L;

    @ApiModelProperty(value = "冻结单号")
    private String freezeDocNum;
    @ApiModelProperty(value = "审批版本")
    private Long sequence;
    @ApiModelProperty(value = "OA返回审批状态")
    private String status;
    @ApiModelProperty(value = "处理时间")
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;

    public FreezeDocResponseDTO() {
    }

    public static FreezeDocResponseDTO newInstance(FreezeDocRequestDTO request) {
        FreezeDocResponseDTO instance = new FreezeDocResponseDTO();
        instance.freezeDocNum = request.getFreezeDocNum();
        instance.sequence = request.getSequence();
        instance.status = request.getStatus();
        instance.processDate = DateUtil.date();
        instance.processStatus = NEW;
        return instance;
    }

    public FreezeDocResponseDTO error(String message) {
        this.setProcessStatus(ERROR);
        this.setProcessMessage(message);
        this.setProcessDate(DateUtil.date());
        return this;
    }

    public FreezeDocResponseDTO success() {
        this.setProcessStatus(SUCCESS);
        this.setProcessMessage("");
        this.setProcessDate(DateUtil.date());
        return this;
    }
}
