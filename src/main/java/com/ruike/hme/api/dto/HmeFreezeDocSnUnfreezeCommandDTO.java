package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeFreezeDocLine;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 条码冻结单 条码解冻命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/25 14:10
 */
@Data
public class HmeFreezeDocSnUnfreezeCommandDTO implements Serializable {

    private static final long serialVersionUID = 3346208797577366354L;

    @ApiModelProperty("冻结单ID")
    @NotBlank
    private String freezeDocId;

    @ApiModelProperty("冻结单ID")
    @NotEmpty
    private List<LineCommand> lineList;

    @Data
    public static class LineCommand {
        @ApiModelProperty("物料批ID")
        @NotBlank
        private String materialLotId;
        @ApiModelProperty("条码在制标识")
        @NotBlank
        private String mfFlag;
        @ApiModelProperty("冻结标识")
        @NotBlank
        private String freezeFlag;
        @ApiModelProperty("行id")
        private String freezeDocLineId;

        public static HmeFreezeDocLine toEntity(LineCommand dto) {
            HmeFreezeDocLine eo = new HmeFreezeDocLine();
            BeanCopierUtil.copy(dto, eo);
            return eo;
        }
    }
}
