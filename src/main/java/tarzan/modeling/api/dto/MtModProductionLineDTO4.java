package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import tarzan.modeling.domain.vo.MtModProdLineManufacturingVO;
import tarzan.modeling.domain.vo.MtModProdLineScheduleVO;
import tarzan.modeling.domain.vo.MtModProductionLineVO5;

public class MtModProductionLineDTO4 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 3671381043938460673L;

    @ApiModelProperty("生产线基础属性")
    private MtModProductionLineVO5 productionLine;

    @ApiModelProperty("生产线计划属性")
    private MtModProdLineScheduleVO prodLineSchedule;

    @ApiModelProperty("生产线生产属性")
    private MtModProdLineManufacturingVO prodLineManufacturing;

    @ApiModelProperty("生产线扩展属性")
    private List<MtExtendAttrDTO3> prodLineAttrs;

    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();

    /**
     * @return 多语言信息
     */
    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public MtModProductionLineVO5 getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(MtModProductionLineVO5 productionLine) {
        this.productionLine = productionLine;
    }

    public MtModProdLineScheduleVO getProdLineSchedule() {
        return prodLineSchedule;
    }

    public void setProdLineSchedule(MtModProdLineScheduleVO prodLineSchedule) {
        this.prodLineSchedule = prodLineSchedule;
    }

    public MtModProdLineManufacturingVO getProdLineManufacturing() {
        return prodLineManufacturing;
    }

    public void setProdLineManufacturing(MtModProdLineManufacturingVO prodLineManufacturing) {
        this.prodLineManufacturing = prodLineManufacturing;
    }

    public List<MtExtendAttrDTO3> getProdLineAttrs() {
        return prodLineAttrs;
    }

    public void setProdLineAttrs(List<MtExtendAttrDTO3> prodLineAttrs) {
        this.prodLineAttrs = prodLineAttrs;
    }


}
