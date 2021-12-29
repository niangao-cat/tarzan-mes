package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.List;

import com.ruike.hme.domain.vo.HmeOrganizationUnitVO;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import tarzan.modeling.domain.vo.MtModWorkcellManufacturingVO;
import tarzan.modeling.domain.vo.MtModWorkcellScheduleVO;
import tarzan.modeling.domain.vo.MtModWorkcellVO3;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModWorkcellDTO3 implements Serializable {
    private static final long serialVersionUID = -1476069345726722709L;

    @ApiModelProperty("工作单元属性")
    private MtModWorkcellVO3 workcell;

    @ApiModelProperty("工作单元生产属性")
    private MtModWorkcellManufacturingVO workcellManufacturing;

    @ApiModelProperty("工作单元计划属性")
    private MtModWorkcellScheduleVO workcellSchedule;

    @ApiModelProperty("工作单元扩展属性")
    private List<MtExtendAttrDTO> workcellAttrs;

    @ApiModelProperty("工作单元组织关系")
    private HmeOrganizationUnitVO organizationUnit;

    public MtModWorkcellVO3 getWorkcell() {
        return workcell;
    }

    public void setWorkcell(MtModWorkcellVO3 workcell) {
        this.workcell = workcell;
    }

    public MtModWorkcellManufacturingVO getWorkcellManufacturing() {
        return workcellManufacturing;
    }

    public void setWorkcellManufacturing(MtModWorkcellManufacturingVO workcellManufacturing) {
        this.workcellManufacturing = workcellManufacturing;
    }

    public MtModWorkcellScheduleVO getWorkcellSchedule() {
        return workcellSchedule;
    }

    public void setWorkcellSchedule(MtModWorkcellScheduleVO workcellSchedule) {
        this.workcellSchedule = workcellSchedule;
    }

    public List<MtExtendAttrDTO> getWorkcellAttrs() {
        return workcellAttrs;
    }

    public void setWorkcellAttrs(List<MtExtendAttrDTO> workcellAttrs) {
        this.workcellAttrs = workcellAttrs;
    }

    public HmeOrganizationUnitVO getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(HmeOrganizationUnitVO organizationUnit) {
        this.organizationUnit = organizationUnit;
    }
}
