package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * 配送单生成汇总维度
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/1 11:10
 */
@Data
public class WmsDistributionDocCreateSumVO {
    @ApiModelProperty(value = "组件物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "工段ID")
    private String workcellId;
    @ApiModelProperty(value = "班次ID")
    private String calendarShiftId;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "需求日期")
    private Date demandDate;
    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;
    @ApiModelProperty(value = "班次编码")
    private String shiftCode;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WmsDistributionDocCreateSumVO that = (WmsDistributionDocCreateSumVO) o;
        return Objects.equals(materialId, that.materialId) &&
                Objects.equals(materialVersion, that.materialVersion) &&
                Objects.equals(workcellId, that.workcellId) &&
                Objects.equals(calendarShiftId, that.calendarShiftId) &&
                Objects.equals(uomId, that.uomId) &&
                Objects.equals(locatorId, that.locatorId) &&
                Objects.equals(shiftDate, that.shiftDate) &&
                Objects.equals(demandDate, that.demandDate) &&
                Objects.equals(shiftCode, that.shiftCode) &&
                Objects.equals(soNum, that.soNum) &&
                Objects.equals(soLineNum, that.soLineNum)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, materialVersion, workcellId, calendarShiftId, uomId, locatorId, shiftDate, shiftCode, demandDate, soNum, soLineNum);
    }
}
