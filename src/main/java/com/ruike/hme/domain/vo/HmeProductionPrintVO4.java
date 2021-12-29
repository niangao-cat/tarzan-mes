package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * HmeProductionPrintVO3
 *
 * @author chaonan.hu@hand-china.com 2021/10/15 14:01
 */
@Data
public class HmeProductionPrintVO4 implements Serializable {
    private static final long serialVersionUID = -6756047881594115590L;

    @ApiModelProperty(value = "eoId")
    private String eoId;

    @ApiModelProperty(value = "铭牌打印内部识别码对应关系头表主键")
    private String nameplateHeaderId;

    @ApiModelProperty(value = "铭牌打印内部识别码对应关系头表内部识别码")
    private String identifyingCode;

    @ApiModelProperty(value = "铭牌打印内部识别码对应关系行表物料")
    private String materialId;

    @ApiModelProperty(value = "铭牌打印内部识别码对应关系行表物料")
    private String materialCode;

    @ApiModelProperty(value = "铭牌打印内部识别码对应关系行表数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "铭牌打印内部识别码对应关系行表编码")
    private String code;

    @ApiModelProperty(value = "铭牌打印内部识别码对应关系头数据下行的个数")
    private Long lineCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeProductionPrintVO4 that = (HmeProductionPrintVO4) o;
        return Objects.equals(materialId, that.materialId) && (qty.compareTo(that.qty) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, qty);
    }
}
