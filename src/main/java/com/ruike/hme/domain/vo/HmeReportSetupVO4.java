package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/22 17:07
 */
@Data
public class HmeReportSetupVO4 extends HmeReportSetupVO2 implements Serializable {

    private static final long serialVersionUID = 7182825405566732707L;


    @ApiModelProperty(value = "本月累计达成数量")
    private BigDecimal monthCompletedQty;

    @ApiModelProperty(value = "本月累计派工数量")
    private BigDecimal monthDispatchQty;

    @ApiModelProperty(value = "今日派工")
    private BigDecimal dayDispatchQty;

    @ApiModelProperty(value = "今日已完成")
    private BigDecimal dayCompletedQty;

    private BigDecimal dailyCompletedQty;

    private String dayTime;

    @ApiModelProperty(value = "文件地址")
    private List<HmeHzeroFileDTO> attachmentUrlList;

    @ApiModelProperty(value = "每日产量")
    private List<BigDecimal> dailyCompletedQtyList;

    @ApiModelProperty(value = "天数")
    private List<String> dayAlis;


}
