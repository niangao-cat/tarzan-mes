package com.ruike.hme.domain.vo;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * cos测试良率维护页面展示字段
 *
 * @author wengang.qiang@hand-china.com 2021/09/06 13:58
 */
@Data
public class HmeCosTestPassRateVO extends AuditDomain implements Serializable {

    private static final long serialVersionUID = -2034653851756318733L;

    @ApiModelProperty("主键")
    private String testId;
    @ApiModelProperty(value = "cos类型", required = true)
    @LovValue(lovCode = "HME.COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;
    @ApiModelProperty(value = "目标良率", required = true)
    private BigDecimal targetPassRate;
    @ApiModelProperty(value = "来料良率", required = true)
    private BigDecimal inputPassRate;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否有效")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty(value = "是否有效描述")
    private String enableFlagMeaning;
    @ApiModelProperty(value = "cos类型描述")
    private String cosTypeMeaning;
}
