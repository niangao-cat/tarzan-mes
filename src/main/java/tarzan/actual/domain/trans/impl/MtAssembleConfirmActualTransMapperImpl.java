package tarzan.actual.domain.trans.impl;

import org.springframework.stereotype.Component;

import tarzan.actual.domain.entity.MtAssembleConfirmActual;
import tarzan.actual.domain.trans.MtAssembleConfirmActualTransMapper;
import tarzan.actual.domain.vo.MtAssembleConfirmActualTupleVO;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO25;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO26;

@Component
public class MtAssembleConfirmActualTransMapperImpl implements MtAssembleConfirmActualTransMapper {

    @Override
    public MtAssembleConfirmActualTupleVO actualToActualTupleVO(MtAssembleConfirmActual dto) {
        if (dto == null) {
            return null;
        }

        MtAssembleConfirmActualTupleVO mtAssembleConfirmActualTupleVO = new MtAssembleConfirmActualTupleVO();

        mtAssembleConfirmActualTupleVO.setEoId(dto.getEoId());
        mtAssembleConfirmActualTupleVO.setMaterialId(dto.getMaterialId());
        mtAssembleConfirmActualTupleVO.setOperationId(dto.getOperationId());
        mtAssembleConfirmActualTupleVO.setComponentType(dto.getComponentType());
        mtAssembleConfirmActualTupleVO.setBomComponentId(dto.getBomComponentId());
        mtAssembleConfirmActualTupleVO.setBomId(dto.getBomId());
        mtAssembleConfirmActualTupleVO.setRouterStepId(dto.getRouterStepId());

        return mtAssembleConfirmActualTupleVO;
    }

    @Override
    public MtAssembleConfirmActualTupleVO actualVO25ToTupleVO(MtAssembleConfirmActualVO25 dto) {
        if (dto == null) {
            return null;
        }

        MtAssembleConfirmActualTupleVO mtAssembleConfirmActualTupleVO = new MtAssembleConfirmActualTupleVO();

        mtAssembleConfirmActualTupleVO.setEoId(dto.getEoId());
        mtAssembleConfirmActualTupleVO.setMaterialId(dto.getMaterialId());
        mtAssembleConfirmActualTupleVO.setOperationId(dto.getOperationId());
        mtAssembleConfirmActualTupleVO.setComponentType(dto.getComponentType());
        mtAssembleConfirmActualTupleVO.setBomComponentId(dto.getBomComponentId());
        mtAssembleConfirmActualTupleVO.setBomId(dto.getBomId());
        mtAssembleConfirmActualTupleVO.setRouterStepId(dto.getRouterStepId());

        return mtAssembleConfirmActualTupleVO;
    }

    @Override
    public MtAssembleConfirmActual actualVO25ToActual(MtAssembleConfirmActualVO25 dto) {
        if (dto == null) {
            return null;
        }

        MtAssembleConfirmActual mtAssembleConfirmActual = new MtAssembleConfirmActual();

        mtAssembleConfirmActual.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        mtAssembleConfirmActual.setEoId(dto.getEoId());
        mtAssembleConfirmActual.setMaterialId(dto.getMaterialId());
        mtAssembleConfirmActual.setOperationId(dto.getOperationId());
        mtAssembleConfirmActual.setComponentType(dto.getComponentType());
        mtAssembleConfirmActual.setBomComponentId(dto.getBomComponentId());
        mtAssembleConfirmActual.setBomId(dto.getBomId());
        mtAssembleConfirmActual.setRouterStepId(dto.getRouterStepId());
        mtAssembleConfirmActual.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        mtAssembleConfirmActual.setAssembleRouterType(dto.getAssembleRouterType());
        mtAssembleConfirmActual.setSubstituteFlag(dto.getSubstituteFlag());

        return mtAssembleConfirmActual;
    }

    @Override
    public MtAssembleConfirmActualVO25 cloneActualVO25(MtAssembleConfirmActualVO25 dto) {
        if (dto == null) {
            return null;
        }

        MtAssembleConfirmActualVO25 mtAssembleConfirmActualVO25 = new MtAssembleConfirmActualVO25();

        mtAssembleConfirmActualVO25.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        mtAssembleConfirmActualVO25.setEoId(dto.getEoId());
        mtAssembleConfirmActualVO25.setMaterialId(dto.getMaterialId());
        mtAssembleConfirmActualVO25.setOperationId(dto.getOperationId());
        mtAssembleConfirmActualVO25.setComponentType(dto.getComponentType());
        mtAssembleConfirmActualVO25.setBomComponentId(dto.getBomComponentId());
        mtAssembleConfirmActualVO25.setBomId(dto.getBomId());
        mtAssembleConfirmActualVO25.setRouterStepId(dto.getRouterStepId());
        mtAssembleConfirmActualVO25.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        mtAssembleConfirmActualVO25.setAssembleRouterType(dto.getAssembleRouterType());
        mtAssembleConfirmActualVO25.setSubstituteFlag(dto.getSubstituteFlag());

        return mtAssembleConfirmActualVO25;
    }

    @Override
    public MtAssembleConfirmActualVO26 actualVO25ToActualVO26(MtAssembleConfirmActualVO25 dto) {
        if (dto == null) {
            return null;
        }

        MtAssembleConfirmActualVO26 mtAssembleConfirmActualVO26 = new MtAssembleConfirmActualVO26();

        mtAssembleConfirmActualVO26.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        mtAssembleConfirmActualVO26.setEoId(dto.getEoId());
        mtAssembleConfirmActualVO26.setMaterialId(dto.getMaterialId());
        mtAssembleConfirmActualVO26.setOperationId(dto.getOperationId());
        mtAssembleConfirmActualVO26.setComponentType(dto.getComponentType());
        mtAssembleConfirmActualVO26.setBomComponentId(dto.getBomComponentId());
        mtAssembleConfirmActualVO26.setBomId(dto.getBomId());
        mtAssembleConfirmActualVO26.setRouterStepId(dto.getRouterStepId());
        mtAssembleConfirmActualVO26.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        mtAssembleConfirmActualVO26.setAssembleRouterType(dto.getAssembleRouterType());
        mtAssembleConfirmActualVO26.setSubstituteFlag(dto.getSubstituteFlag());

        return mtAssembleConfirmActualVO26;
    }
}
