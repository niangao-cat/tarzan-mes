package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeCosRuleHead;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName HmeCosRuleHeadDto
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/11 11:15
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class HmeCosRuleHeadDto extends HmeCosRuleHead implements Serializable {
    private static final long serialVersionUID = -5598380897076997986L;

    @ApiModelProperty(value = "料号")
    @LovValue(lovCode = "HME_PRODUCT_TYPE", meaningField = "productTypeMeaning")
    private String productType;
    @ApiModelProperty(value = "物料描述")
    private String productTypeMeaning;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;
}
