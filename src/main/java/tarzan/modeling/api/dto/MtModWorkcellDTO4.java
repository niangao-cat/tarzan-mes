package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.List;

import com.ruike.hme.domain.vo.HmeOrganizationUnitVO;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import tarzan.modeling.domain.vo.MtModWorkcellManufacturingVO;
import tarzan.modeling.domain.vo.MtModWorkcellScheduleVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO4;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModWorkcellDTO4 implements Serializable {
    private static final long serialVersionUID = -1476069345726722709L;

    @ApiModelProperty("工作单元属性")
    private MtModWorkcellVO4 workcell;

    @ApiModelProperty("工作单元生产属性")
    private MtModWorkcellManufacturingVO workcellManufacturing;

    @ApiModelProperty("工作单元计划属性")
    private MtModWorkcellScheduleVO2 workcellSchedule;

    @ApiModelProperty("工作单元扩展属性")
    private List<MtExtendAttrDTO3> workcellAttrs;

    @ApiModelProperty("工作单元组织关系")
    private HmeOrganizationUnitVO organizationUnit;

    public MtModWorkcellVO4 getWorkcell() {
        return workcell;
    }

    public void setWorkcell(MtModWorkcellVO4 workcell) {
        this.workcell = workcell;
    }

    public MtModWorkcellManufacturingVO getWorkcellManufacturing() {
        return workcellManufacturing;
    }

    public void setWorkcellManufacturing(MtModWorkcellManufacturingVO workcellManufacturing) {
        this.workcellManufacturing = workcellManufacturing;
    }

    public MtModWorkcellScheduleVO2 getWorkcellSchedule() {
        return workcellSchedule;
    }

    public void setWorkcellSchedule(MtModWorkcellScheduleVO2 workcellSchedule) {
        this.workcellSchedule = workcellSchedule;
    }

    public List<MtExtendAttrDTO3> getWorkcellAttrs() {
        return workcellAttrs;
    }

    public void setWorkcellAttrs(List<MtExtendAttrDTO3> workcellAttrs) {
        this.workcellAttrs = workcellAttrs;
    }

    public HmeOrganizationUnitVO getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(HmeOrganizationUnitVO organizationUnit) {
        this.organizationUnit = organizationUnit;
    }
}
