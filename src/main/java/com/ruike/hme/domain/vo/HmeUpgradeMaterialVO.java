package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 升级物料批
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/19 16:15
 */
@Data
public class HmeUpgradeMaterialVO {
    @ApiModelProperty("物料批ID")
    private String materialId;
    @ApiModelProperty("物料批编码")
    private String materialCode;
    @ApiModelProperty("升级标志")
    private String upgradeFlag;
}
