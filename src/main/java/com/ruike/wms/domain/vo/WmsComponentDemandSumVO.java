package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 组件需求汇总数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/19 16:39
 */
@Data
public class WmsComponentDemandSumVO {
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("工段ID")
    private String workcellId;
    @ApiModelProperty("工段编码")
    private String workcellCode;
    @ApiModelProperty("工段名称")
    private String workcellName;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("班次日期")
    private Date shiftDate;
    @ApiModelProperty("汇总需求数量")
    private BigDecimal requirementQty;
    @ApiModelProperty("汇总损耗数量")
    private BigDecimal attritionQty;
    @ApiModelProperty("损耗率")
    private BigDecimal attritionChance;
    @ApiModelProperty("日需求列表")
    List<WmsComponentDemandDateVO> requirementList;

    public static WmsComponentDemandSumVO summaryDemand(WmsComponentDemandSumVO vo) {
        WmsComponentDemandSumVO result = new WmsComponentDemandSumVO();
        result.setMaterialId(vo.getMaterialId());
        result.setMaterialCode(vo.getMaterialCode());
        result.setMaterialName(vo.getMaterialName());
        result.setMaterialVersion(vo.getMaterialVersion());
        result.setWorkcellId(vo.getWorkcellId());
        result.setWorkcellCode(vo.getWorkcellCode());
        result.setWorkcellName(vo.getWorkcellName());
        result.setUomId(vo.getUomId());
        result.setUomCode(vo.getUomCode());
        result.setAttritionChance(vo.getAttritionChance());
        return result;
    }
}
