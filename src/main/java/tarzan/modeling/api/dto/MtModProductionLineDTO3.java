package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import tarzan.modeling.domain.vo.MtModProdLineManufacturingVO2;
import tarzan.modeling.domain.vo.MtModProdLineScheduleVO2;
import tarzan.modeling.domain.vo.MtModProductionLineVO4;

public class MtModProductionLineDTO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7585473048699770591L;

    @ApiModelProperty("生产线基础属性")
    private MtModProductionLineVO4 productionLine;

    @ApiModelProperty("生产线计划属性")
    private MtModProdLineScheduleVO2 prodLineSchedule;

    @ApiModelProperty("生产线生产属性")
    private MtModProdLineManufacturingVO2 prodLineManufacturing;

    @ApiModelProperty("生产线扩展属性")
    private List<MtExtendAttrDTO> prodLineAttrs;

    public MtModProductionLineVO4 getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(MtModProductionLineVO4 productionLine) {
        this.productionLine = productionLine;
    }

    public MtModProdLineScheduleVO2 getProdLineSchedule() {
        return prodLineSchedule;
    }

    public void setProdLineSchedule(MtModProdLineScheduleVO2 prodLineSchedule) {
        this.prodLineSchedule = prodLineSchedule;
    }

    public MtModProdLineManufacturingVO2 getProdLineManufacturing() {
        return prodLineManufacturing;
    }

    public void setProdLineManufacturing(MtModProdLineManufacturingVO2 prodLineManufacturing) {
        this.prodLineManufacturing = prodLineManufacturing;
    }

    public List<MtExtendAttrDTO> getProdLineAttrs() {
        return prodLineAttrs;
    }

    public void setProdLineAttrs(List<MtExtendAttrDTO> prodLineAttrs) {
        this.prodLineAttrs = prodLineAttrs;
    }

}
