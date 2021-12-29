package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HmeFreezeDocVO2 implements Serializable {
    private static final long serialVersionUID = -1514177818730627110L;

    @ApiModelProperty("行表主键")
    private String freezeDocLineId;

    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty("条码冻结标识")
    private String snFreezeFlag;

    @ApiModelProperty("条码在制标识")
    private String mfFlag;
}
