package tarzan.actual.domain.trans.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import tarzan.actual.domain.trans.MtAssembleProcessActualTransMapper;
import tarzan.actual.domain.vo.*;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO12.AssembleInfo;

@Component
public class MtAssembleProcessActualTransMapperImpl implements MtAssembleProcessActualTransMapper {

    @Override
    public MtAssembleProcessActualVO9 actualVO16ToActualVO9(MtAssembleProcessActualVO16 dto) {
        if (dto == null) {
            return null;
        }

        MtAssembleProcessActualVO9 mtAssembleProcessActualVO9 = new MtAssembleProcessActualVO9();

        mtAssembleProcessActualVO9.setOperationId(dto.getOperationId());
        mtAssembleProcessActualVO9.setOperationBy(dto.getOperationBy());
        mtAssembleProcessActualVO9.setWorkcellId(dto.getWorkcellId());
        mtAssembleProcessActualVO9.setParentEventId(dto.getParentEventId());
        mtAssembleProcessActualVO9.setEventRequestId(dto.getEventRequestId());
        mtAssembleProcessActualVO9.setShiftDate(dto.getShiftDate());
        mtAssembleProcessActualVO9.setShiftCode(dto.getShiftCode());
        mtAssembleProcessActualVO9
                        .setEoInfo(mtAssembleProcessActualVO17ListToMtAssembleProcessActualVO10List(dto.getEoInfo()));

        return mtAssembleProcessActualVO9;
    }

    @Override
    public MtEoComponentActualVO31 actualVO16ToEoComponentActualVO31(MtAssembleProcessActualVO16 dto) {
        if (dto == null) {
            return null;
        }

        MtEoComponentActualVO31 mtEoComponentActualVO31 = new MtEoComponentActualVO31();

        mtEoComponentActualVO31.setOperationId(dto.getOperationId());
        mtEoComponentActualVO31.setWorkcellId(dto.getWorkcellId());
        mtEoComponentActualVO31.setLocatorId(dto.getLocatorId());
        mtEoComponentActualVO31.setParentEventId(dto.getParentEventId());
        mtEoComponentActualVO31.setEventRequestId(dto.getEventRequestId());
        mtEoComponentActualVO31.setShiftDate(dto.getShiftDate());
        mtEoComponentActualVO31.setShiftCode(dto.getShiftCode());
        mtEoComponentActualVO31
                        .setEoInfo(mtAssembleProcessActualVO17ListToMtEoComponentActualVO30List(dto.getEoInfo()));

        return mtEoComponentActualVO31;
    }

    @Override
    public MtAssembleProcessActualVO13 mtAssProAcVO12TOMtAssProActVO13(AssembleInfo dto) {
        if (dto == null) {
            return null;
        }

        MtAssembleProcessActualVO13 mtAssembleProcessActualVO13 = new MtAssembleProcessActualVO13();

        mtAssembleProcessActualVO13.setAssembleConfirmActualId(dto.getAssembleConfirmActualId());
        mtAssembleProcessActualVO13.setAssembleQty(dto.getAssembleQty());
        mtAssembleProcessActualVO13.setScrappedQty(dto.getScrappedQty());
        mtAssembleProcessActualVO13.setRouterId(dto.getRouterId());
        mtAssembleProcessActualVO13.setSubstepId(dto.getSubstepId());
        mtAssembleProcessActualVO13.setWorkcellId(dto.getWorkcellId());
        mtAssembleProcessActualVO13.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssembleProcessActualVO13.setAssemblePointId(dto.getAssemblePointId());
        mtAssembleProcessActualVO13.setReferenceArea(dto.getReferenceArea());
        mtAssembleProcessActualVO13.setReferencePoint(dto.getReferencePoint());
        mtAssembleProcessActualVO13.setLocatorId(dto.getLocatorId());
        mtAssembleProcessActualVO13.setAssembleMethod(dto.getAssembleMethod());
        mtAssembleProcessActualVO13.setRouterStepId(dto.getRouterStepId());
        mtAssembleProcessActualVO13.setMaterialLotId(dto.getMaterialLotId());

        return mtAssembleProcessActualVO13;
    }

    protected MtAssembleProcessActualVO10 mtAssembleProcessActualVO17ToMtAssembleProcessActualVO10(
                    MtAssembleProcessActualVO17 mtAssembleProcessActualVO17) {
        if (mtAssembleProcessActualVO17 == null) {
            return null;
        }

        MtAssembleProcessActualVO10 mtAssembleProcessActualVO10 = new MtAssembleProcessActualVO10();

        mtAssembleProcessActualVO10.setEoId(mtAssembleProcessActualVO17.getEoId());
        mtAssembleProcessActualVO10.setRouterId(mtAssembleProcessActualVO17.getRouterId());
        mtAssembleProcessActualVO10.setAssembleRouterType(mtAssembleProcessActualVO17.getAssembleRouterType());
        mtAssembleProcessActualVO10.setRouterStepId(mtAssembleProcessActualVO17.getRouterStepId());
        mtAssembleProcessActualVO10.setSubstepId(mtAssembleProcessActualVO17.getSubstepId());
        mtAssembleProcessActualVO10.setBomId(mtAssembleProcessActualVO17.getBomId());
        List<MtAssembleProcessActualVO11> list = mtAssembleProcessActualVO17.getMaterialInfo();
        if (list != null) {
            mtAssembleProcessActualVO10.setMaterialInfo(new ArrayList<MtAssembleProcessActualVO11>(list));
        }

        return mtAssembleProcessActualVO10;
    }

    protected List<MtAssembleProcessActualVO10> mtAssembleProcessActualVO17ListToMtAssembleProcessActualVO10List(
                    List<MtAssembleProcessActualVO17> list) {
        if (list == null) {
            return null;
        }

        List<MtAssembleProcessActualVO10> list1 = new ArrayList<MtAssembleProcessActualVO10>(list.size());
        for (MtAssembleProcessActualVO17 mtAssembleProcessActualVO17 : list) {
            list1.add(mtAssembleProcessActualVO17ToMtAssembleProcessActualVO10(mtAssembleProcessActualVO17));
        }

        return list1;
    }

    protected MtEoComponentActualVO29 mtAssembleProcessActualVO11ToMtEoComponentActualVO29(
                    MtAssembleProcessActualVO11 mtAssembleProcessActualVO11) {
        if (mtAssembleProcessActualVO11 == null) {
            return null;
        }

        MtEoComponentActualVO29 mtEoComponentActualVO29 = new MtEoComponentActualVO29();

        mtEoComponentActualVO29.setMaterialId(mtAssembleProcessActualVO11.getMaterialId());
        mtEoComponentActualVO29.setBomComponentId(mtAssembleProcessActualVO11.getBomComponentId());
        mtEoComponentActualVO29.setTrxAssembleQty(mtAssembleProcessActualVO11.getTrxAssembleQty());
        mtEoComponentActualVO29.setAssembleExcessFlag(mtAssembleProcessActualVO11.getAssembleExcessFlag());
        mtEoComponentActualVO29.setSubstituteFlag(mtAssembleProcessActualVO11.getSubstituteFlag());

        return mtEoComponentActualVO29;
    }

    protected List<MtEoComponentActualVO29> mtAssembleProcessActualVO11ListToMtEoComponentActualVO29List(
                    List<MtAssembleProcessActualVO11> list) {
        if (list == null) {
            return null;
        }

        List<MtEoComponentActualVO29> list1 = new ArrayList<MtEoComponentActualVO29>(list.size());
        for (MtAssembleProcessActualVO11 mtAssembleProcessActualVO11 : list) {
            list1.add(mtAssembleProcessActualVO11ToMtEoComponentActualVO29(mtAssembleProcessActualVO11));
        }

        return list1;
    }

    protected MtEoComponentActualVO30 mtAssembleProcessActualVO17ToMtEoComponentActualVO30(
                    MtAssembleProcessActualVO17 mtAssembleProcessActualVO17) {
        if (mtAssembleProcessActualVO17 == null) {
            return null;
        }

        MtEoComponentActualVO30 mtEoComponentActualVO30 = new MtEoComponentActualVO30();

        mtEoComponentActualVO30.setEoId(mtAssembleProcessActualVO17.getEoId());
        mtEoComponentActualVO30.setBomId(mtAssembleProcessActualVO17.getBomId());
        mtEoComponentActualVO30.setRouterStepId(mtAssembleProcessActualVO17.getRouterStepId());
        mtEoComponentActualVO30.setAssembleRouterType(mtAssembleProcessActualVO17.getAssembleRouterType());
        mtEoComponentActualVO30.setMaterialInfo(mtAssembleProcessActualVO11ListToMtEoComponentActualVO29List(
                        mtAssembleProcessActualVO17.getMaterialInfo()));

        return mtEoComponentActualVO30;
    }

    protected List<MtEoComponentActualVO30> mtAssembleProcessActualVO17ListToMtEoComponentActualVO30List(
                    List<MtAssembleProcessActualVO17> list) {
        if (list == null) {
            return null;
        }

        List<MtEoComponentActualVO30> list1 = new ArrayList<MtEoComponentActualVO30>(list.size());
        for (MtAssembleProcessActualVO17 mtAssembleProcessActualVO17 : list) {
            list1.add(mtAssembleProcessActualVO17ToMtEoComponentActualVO30(mtAssembleProcessActualVO17));
        }

        return list1;
    }
}
