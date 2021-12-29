package com.ruike.qms.api.dto;

import com.ruike.qms.domain.entity.QmsTransitionRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @Description 检验水平转移规则 DTO
 * @Author tong.li
 * @Date 2020/5/11 11:31
 * @Version 1.0
 */
@Data
public class QmsTransitionRuleDTO extends QmsTransitionRule implements Serializable {

    private static final long serialVersionUID = -6878042401064699081L;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String flag;

    @ApiModelProperty(value = "有效性含义")
    private String enableFlagMeaning;

}
