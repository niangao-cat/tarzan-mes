package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.actual.domain.entity.MtWkcShift;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 班组工作台 接班取消命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/11 09:37
 */
@Data
public class HmeOpenEndShiftEndCancelCommandDTO implements Serializable {
    private static final long serialVersionUID = -3352186026279660914L;

    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("工段ID")
    @NotBlank
    private String workcellId;
    @ApiModelProperty("班次日期")
    @NotNull
    private Date shiftDate;
    @ApiModelProperty("班次编码")
    @NotBlank
    private String shiftCode;

    public MtWkcShift toWkcCondition() {
        MtWkcShift condition = new MtWkcShift();
        condition.setWorkcellId(this.getWorkcellId());
        condition.setTenantId(this.getTenantId());
        return condition;
    }
}
