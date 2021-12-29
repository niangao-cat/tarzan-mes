package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.common.HZeroCacheKey;
import org.hzero.core.cache.CacheValue;
import org.hzero.core.cache.Cacheable;
import org.hzero.mybatis.common.query.Where;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import javax.validation.constraints.NotBlank;

/**
 * @program: tarzan-mes
 * @description: 领退料平台查询返回数据
 * @author: han.zhang
 * @create: 2020/04/17 10:55
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WmsPickReturnReceiveVO extends MtInstructionDoc implements Cacheable {
    private static final long serialVersionUID = -6262683720914288403L;

    @ApiModelProperty(value = "工厂编码")
    private String siteCode;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "成本中心编码")
    private String costcenterCode;

    @ApiModelProperty(value = "成本中心类型")
    private String costCenterType;

    @ApiModelProperty(value = "成本中心类型含义")
    private String costCenterTypeMeaning;

    @ApiModelProperty(value = "// 创建人姓名")
    @CacheValue(key = HZeroCacheKey.USER, primaryKey = "createdBy", searchKey = "realName",
            structure = CacheValue.DataStructure.MAP_OBJECT)
    private String createdUserName;

    @ApiModelProperty(value = "单据类型")
    @LovValue(value = "WMS.COST_CENTER_DOCUMENT.TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;
    @ApiModelProperty(value = "领料单类型含义")
    private String instructionDocTypeMeaning;

    @ApiModelProperty(value = "领料单状态含义")
    private String instructionDocStatusMeaning;

    @ApiModelProperty(value = "结算类型")
    @LovValue(value = "WMS.CC_SETTLE_ACCOUNTS.TYPE", meaningField = "settleAccountsMeaning")
    private String settleAccounts;

    @ApiModelProperty(value = "结算类型含义")
    private String settleAccountsMeaning;

    @ApiModelProperty(value = "内部订单Id")
    private String internalOrderId;

    @ApiModelProperty(value = "内部订单")
    private String internalOrder;

    @ApiModelProperty(value = "内部订单类型")
    @LovValue(value = "WMS.INTERNAL_ORDER_TYPE", meaningField = "internalOrderTypeMeaning")
    private String internalOrderType;

    @ApiModelProperty(value = "内部订单类型含义")
    private String internalOrderTypeMeaning;

    @ApiModelProperty(value = "移动类型")
    private String moveType;

    @ApiModelProperty(value = "打印标识")
    @LovValue(value = "WMS.PRINT_FLAG", meaningField = "printFlagMeaning")
    private String printFlag;

    @ApiModelProperty(value = "打印标识含义")
    private String printFlagMeaning;

    @ApiModelProperty(value = "报废部门")
    private String scrapDepartment;

    @ApiModelProperty(value = "利润中心")
    private String profitCenter;

    @ApiModelProperty(value = "移动原因")
    private String moveReason;

    @ApiModelProperty(value = "费用类型")
    private String costType;

    @ApiModelProperty(value = "费用类型含义")
    private String costTypeMeaning;
}