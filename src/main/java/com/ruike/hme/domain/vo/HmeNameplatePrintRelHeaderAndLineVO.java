package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 铭牌打印配置属性维护历史查询显示字段
 *
 * @author wengang.qiang@hand-china.com 2021/10/12 14:24
 */
@Data
public class HmeNameplatePrintRelHeaderAndLineVO implements Serializable {

    private static final long serialVersionUID = 3076563780995398531L;

    @ApiModelProperty(value = "类型", required = true)
    @LovValue(lovCode = "HME.IDENTIFYING_CODE_TYPE", meaningField = "typeMeaning")
    private String type;

    @ApiModelProperty(value = "内部识别码", required = true)
    private String identifyingCode;

    @ApiModelProperty(value = "头表有效性", required = true)
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagHeaderMeaning")
    private String enableFlagHeader;

    @ApiModelProperty(value = "类型描述")
    private String typeMeaning;

    @ApiModelProperty(value = "头表更新人id")
    private Long lastUpdatedByHeader;

    @ApiModelProperty(value = "头表更新人")
    private String lastUpdatedByHeaderName;

    @ApiModelProperty(value = "头表最后更新时间")
    private Date lastUpdateDateHeader;

    @ApiModelProperty(value = "数量", required = true)
    private BigDecimal qty;

    @ApiModelProperty(value = "行表有效性", required = true)
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagLineMeaning")
    private String enableFlagLine;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "行表更新人id")
    private Long lastUpdatedByLine;

    @ApiModelProperty(value = "行表更新人")
    private String lastUpdatedByLineName;

    @ApiModelProperty(value = "行表最后更新时间")
    private Date lastUpdateDateLine;

    @ApiModelProperty(value = "头表是否有效")
    private String enableFlagHeaderMeaning;

    @ApiModelProperty(value = "行表是否有效")
    private String enableFlagLineMeaning;

}
