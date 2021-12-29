package tarzan.method.infra.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtAssembleControl;
import tarzan.method.domain.entity.MtAssemblePoint;
import tarzan.method.domain.entity.MtAssemblePointControl;
import tarzan.method.domain.repository.MtAssemblePointControlRepository;
import tarzan.method.domain.repository.MtAssemblePointRepository;
import tarzan.method.domain.vo.MtAssemblePointControlVO;
import tarzan.method.domain.vo.MtAssemblePointControlVO1;
import tarzan.method.domain.vo.MtAssemblePointControlVO2;
import tarzan.method.domain.vo.MtAssemblePointControlVO3;
import tarzan.method.domain.vo.MtAssemblePointControlVO4;
import tarzan.method.domain.vo.MtAssemblePointControlVO5;
import tarzan.method.domain.vo.MtAssemblePointControlVO6;
import tarzan.method.domain.vo.MtAssemblePointControlVO7;
import tarzan.method.infra.mapper.MtAssembleControlMapper;
import tarzan.method.infra.mapper.MtAssemblePointControlMapper;
import tarzan.method.infra.mapper.MtAssemblePointMapper;

/**
 * 装配点控制，指示具体装配控制下装配点可装载的物料 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@Component
public class MtAssemblePointControlRepositoryImpl extends BaseRepositoryImpl<MtAssemblePointControl>
                implements MtAssemblePointControlRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssemblePointControlMapper mtAssemblePointControlMapper;

    @Autowired
    private MtAssembleControlMapper mtAssembleControlMapper;

    @Autowired
    private MtAssemblePointMapper mtAssemblePointMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtAssemblePointRepository mtAssemblePointRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Override
    public MtAssemblePointControl assemblePointLimitAssembleControlPropertyGet(Long tenantId,
                                                                               MtAssemblePointControlVO condition) {
        if (StringUtils.isEmpty(condition.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:assemblePointLimitAssembleControlPropertyGet】"));
        }
        if (StringUtils.isEmpty(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId",
                                            "【API:assemblePointLimitAssembleControlPropertyGet】"));
        }

        MtAssemblePointControl mtAssemblePointControl = new MtAssemblePointControl();
        mtAssemblePointControl.setTenantId(tenantId);
        mtAssemblePointControl.setAssembleControlId(condition.getAssembleControlId());
        mtAssemblePointControl.setAssemblePointId(condition.getAssemblePointId());
        return this.mtAssemblePointControlMapper.selectOne(mtAssemblePointControl);
    }

    @Override
    public Double assemblePointMaterialUnitQtyGet(Long tenantId, MtAssemblePointControlVO condition) {
        if (StringUtils.isEmpty(condition.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:assemblePointMaterialUnitQtyGet】"));
        }
        if (StringUtils.isEmpty(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId",
                                            "【API:assemblePointMaterialUnitQtyGet】"));
        }

        MtAssemblePointControl MtAssemblePointControl =
                        assemblePointLimitAssembleControlPropertyGet(tenantId, condition);
        if (null == MtAssemblePointControl) {
            return null;
        }
        return MtAssemblePointControl.getUnitQty();
    }

    @Override
    public List<String> materialLimitAvailableAssemblePointQuery(Long tenantId, MtAssemblePointControlVO1 condition) {
        if (StringUtils.isEmpty(condition.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:materialLimitAvailableAssemblePointQuery】"));
        }
        if (StringUtils.isEmpty(condition.getMaterialId()) && StringUtils.isEmpty(condition.getMaterialLotId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0023",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0023",
                                            "ASSEMBLE_CONTROL", "【API:materialLimitAvailableAssemblePointQuery】"));
        }

        if (StringUtils.isEmpty(condition.getMaterialId())) {
            MtMaterialLot lot =
                            this.mtMaterialLotRepository.materialLotPropertyGet(tenantId, condition.getMaterialLotId());
            if (lot == null) {
                throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0017", "ASSEMBLE", "【API:materialLimitAvailableAssemblePointQuery】"));
            }
            condition.setMaterialId(lot.getMaterialId());
        }

        MtAssemblePointControl mtAssemblePointControl = new MtAssemblePointControl();
        mtAssemblePointControl.setTenantId(tenantId);
        mtAssemblePointControl.setAssembleControlId(condition.getAssembleControlId());
        mtAssemblePointControl.setMaterialId(condition.getMaterialId());
        if (null != condition.getMaterialLotId()) {
            mtAssemblePointControl.setMaterialLotId(condition.getMaterialLotId());
        }
        if (null != condition.getReferencePoint()) {
            mtAssemblePointControl.setReferencePoint(condition.getReferencePoint());
        }
        mtAssemblePointControl.setEnableFlag("Y");

        List<MtAssemblePointControl> mtAssemblePointControls =
                        this.mtAssemblePointControlMapper.select(mtAssemblePointControl);
        if (CollectionUtils.isEmpty(mtAssemblePointControls)) {
            return Collections.emptyList();
        }
        return mtAssemblePointControls.stream().map(MtAssemblePointControl::getAssemblePointId)
                        .collect(Collectors.toList());
    }

    @Override
    public List<MtAssemblePointControl> assemblePointLimitAssembleControlPropertyBatchGet(Long tenantId,
                                                                                          MtAssemblePointControlVO2 condition) {
        if (StringUtils.isEmpty(condition.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:assemblePointLimitAssembleControlPropertyBatchGet】"));
        }
        if (CollectionUtils.isEmpty(condition.getAssemblePointIds())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId",
                                            "【API:assemblePointLimitAssembleControlPropertyBatchGet】"));
        }

        List<MtAssemblePointControl> mtAssemblePointControls =
                        this.mtAssemblePointControlMapper.selectByIdsCustom(tenantId, condition);
        if (CollectionUtils.isEmpty(mtAssemblePointControls)) {
            return Collections.emptyList();
        }
        return mtAssemblePointControls;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String assemblePointControlUpdate(Long tenantId, MtAssemblePointControl dto) {
        if (StringUtils.isEmpty(dto.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:assemblePointControlUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId", "【API:assemblePointControlUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "materialId", "【API:assemblePointControlUpdate】"));
        }
        if (null == dto.getUnitQty()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "unitQty", "【API:assemblePointControlUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "enableFlag", "【API:assemblePointControlUpdate】"));
        } else {
            if (!Arrays.asList("", "N", "Y").contains(dto.getEnableFlag())) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0020",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0020",
                                                "ASSEMBLE_CONTROL", "enableFlag", "【API:assemblePointControlUpdate】"));
            }
        }

        MtAssembleControl mtAssembleControl = new MtAssembleControl();
        mtAssembleControl.setTenantId(tenantId);
        mtAssembleControl.setAssembleControlId(dto.getAssembleControlId());
        mtAssembleControl = this.mtAssembleControlMapper.selectOne(mtAssembleControl);
        if (null == mtAssembleControl) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:assemblePointControlUpdate】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssemblePointId(dto.getAssemblePointId());
        mtAssemblePoint = this.mtAssemblePointMapper.selectOne(mtAssemblePoint);
        if (null == mtAssemblePoint || !"Y".equals(mtAssemblePoint.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "assemblePointId", "【API:assemblePointControlUpdate】"));
        }

        MtMaterialVO mtMaterialVO = this.mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
        if (null == mtMaterialVO || !"Y".equals(mtMaterialVO.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "materialId", "【API:assemblePointControlUpdate】"));
        }

        if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            this.mtMaterialLotRepository.materialLotEnableValidate(tenantId, dto.getMaterialLotId());
        }

        MtAssemblePointControl mtAssemblePointControl = new MtAssemblePointControl();
        mtAssemblePointControl.setTenantId(tenantId);
        mtAssemblePointControl.setAssembleControlId(dto.getAssembleControlId());
        mtAssemblePointControl.setAssemblePointId(dto.getAssemblePointId());
        mtAssemblePointControl.setMaterialId(dto.getMaterialId());
        mtAssemblePointControl.setMaterialLotId(dto.getMaterialLotId());
        mtAssemblePointControl.setUnitQty(dto.getUnitQty());
        mtAssemblePointControl.setReferencePoint(dto.getReferencePoint());
        mtAssemblePointControl.setEnableFlag(dto.getEnableFlag());
        if (StringUtils.isEmpty(dto.getAssemblePointControlId())) {
            self().insertSelective(mtAssemblePointControl);
        } else {
            MtAssemblePointControl tmpMtAssemblePointControl = new MtAssemblePointControl();
            tmpMtAssemblePointControl.setTenantId(tenantId);
            tmpMtAssemblePointControl.setAssemblePointControlId(dto.getAssemblePointControlId());
            tmpMtAssemblePointControl = this.mtAssemblePointControlMapper.selectOne(tmpMtAssemblePointControl);
            if (null == tmpMtAssemblePointControl) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                                "ASSEMBLE_CONTROL", "assemblePointControlId",
                                                "【API:assemblePointControlUpdate】"));
            }

            mtAssemblePointControl.setAssemblePointControlId(dto.getAssemblePointControlId());
            self().updateByPrimaryKeySelective(mtAssemblePointControl);
        }
        return mtAssemblePointControl.getAssemblePointControlId();
    }

    @Override
    public void materialLimitAvailableAssemblePointValidate(Long tenantId, MtAssemblePointControlVO3 condition) {
        if (StringUtils.isEmpty(condition.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:materialLimitAvailableAssemblePointValidate】"));
        }
        if (StringUtils.isEmpty(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId",
                                            "【API:materialLimitAvailableAssemblePointValidate】"));
        }
        if (StringUtils.isEmpty(condition.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "materialId",
                                            "【API:materialLimitAvailableAssemblePointValidate】"));
        }

        MtAssembleControl mtAssembleControl =
                        this.mtAssembleControlMapper.selectByEnable(tenantId, condition.getAssembleControlId());
        if (null == mtAssembleControl) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:materialLimitAvailableAssemblePointValidate】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssemblePointId(condition.getAssemblePointId());
        mtAssemblePoint = this.mtAssemblePointMapper.selectOne(mtAssemblePoint);
        if (null == mtAssemblePoint || !"Y".equals(mtAssemblePoint.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "assemblePointId",
                                            "【API:materialLimitAvailableAssemblePointValidate】"));
        }

        MtMaterialVO mtMaterialVO = this.mtMaterialRepository.materialPropertyGet(tenantId, condition.getMaterialId());
        if (null == mtMaterialVO || !"Y".equals(mtMaterialVO.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "materialId",
                                            "【API:materialLimitAvailableAssemblePointValidate】"));
        }

        if (StringUtils.isNotEmpty(condition.getMaterialLotId())) {
            mtMaterialLotRepository.materialLotEnableValidate(tenantId, condition.getMaterialLotId());
        }

        MtAssemblePointControl mtAssemblePointControl = new MtAssemblePointControl();
        mtAssemblePointControl.setTenantId(tenantId);
        mtAssemblePointControl.setAssembleControlId(condition.getAssembleControlId());
        mtAssemblePointControl.setAssemblePointId(condition.getAssemblePointId());
        mtAssemblePointControl.setMaterialId(condition.getMaterialId());
        if (null != condition.getMaterialLotId()) {
            mtAssemblePointControl.setMaterialLotId(condition.getMaterialLotId());
        }
        if (null != condition.getReferencePoint()) {
            mtAssemblePointControl.setReferencePoint(condition.getReferencePoint());
        }
        mtAssemblePointControl.setEnableFlag("Y");
        List<MtAssemblePointControl> mtAssemblePointControls =
                        this.mtAssemblePointControlMapper.select(mtAssemblePointControl);
        if (CollectionUtils.isEmpty(mtAssemblePointControls)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0016",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0016",
                                            "ASSEMBLE_CONTROL", "【API:materialLimitAvailableAssemblePointValidate】"));
        }
    }

    @Override
    public List<MtAssemblePointControlVO4> assembleGroupLimitAssembleControlPropertyQuery(Long tenantId,
                                                                                          MtAssemblePointControlVO5 condition) {
        if (StringUtils.isEmpty(condition.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:assembleGroupLimitAssembleControlPropertyQuery】"));
        }
        if (StringUtils.isEmpty(condition.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assembleGroupLimitAssembleControlPropertyQuery】"));
        }

        List<Map<String, String>> list = this.mtAssemblePointRepository
                        .assembleGroupLimitEnableAssemblePointQuery(tenantId, condition.getAssembleGroupId());
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<String> assemblePointIds = list.stream().map(c -> c.get("assemblePointId")).collect(Collectors.toList());
        MtAssemblePointControlVO2 mtAssemblePointControlVO2 = new MtAssemblePointControlVO2();
        mtAssemblePointControlVO2.setAssembleControlId(condition.getAssembleControlId());
        mtAssemblePointControlVO2.setAssemblePointIds(assemblePointIds);
        List<MtAssemblePointControl> mtAssemblePointControls =
                        assemblePointLimitAssembleControlPropertyBatchGet(tenantId, mtAssemblePointControlVO2);
        if (CollectionUtils.isEmpty(mtAssemblePointControls)) {
            return Collections.emptyList();
        }

        final List<MtAssemblePointControlVO4> result = new ArrayList<MtAssemblePointControlVO4>();
        mtAssemblePointControls.stream().forEach(c -> {
            MtAssemblePointControlVO4 mtAssemblePointControlVO4 = new MtAssemblePointControlVO4();
            mtAssemblePointControlVO4.setAssemblePointId(c.getAssemblePointId());
            mtAssemblePointControlVO4.setMaterialId(c.getMaterialId());
            mtAssemblePointControlVO4.setMaterialLotId(c.getMaterialLotId());
            mtAssemblePointControlVO4.setUnitQty(c.getUnitQty());
            mtAssemblePointControlVO4.setReferencePoint(c.getReferencePoint());
            result.add(mtAssemblePointControlVO4);
        });

        if (StringUtils.isNotEmpty(condition.getMaterialId())) {
            return result.stream().filter(c -> c.getMaterialId().equals(condition.getMaterialId()))
                            .collect(Collectors.toList());
        } else {
            return result;
        }
    }

    @Override
    public List<MtAssemblePointControlVO7> propertyLimitAssemblePointControlPropertyQuery(Long tenantId,
                                                                                          MtAssemblePointControlVO6 dto) {
        List<MtAssemblePointControlVO7> pointControlVO7List = new ArrayList<>();
        MtAssemblePointControl pointControl = new MtAssemblePointControl();
        BeanUtils.copyProperties(dto, pointControl);
        pointControl.setTenantId(tenantId);
        List<MtAssemblePointControl> pointControlList = mtAssemblePointControlMapper.select(pointControl);

        if (CollectionUtils.isEmpty(pointControlList)) {
            return Collections.emptyList();
        }
        // 获取assemblePointId列表
        List<String> assemblePointIds = pointControlList.stream().map(MtAssemblePointControl::getAssemblePointId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtAssemblePoint> assemblePointMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(assemblePointIds)) {
            // 获取装配点描述和装配点编码：
            List<MtAssemblePoint> mtAssemblePoints =
                            mtAssemblePointRepository.assemblePointPropertyBatchGet(tenantId, assemblePointIds);
            if (CollectionUtils.isNotEmpty(mtAssemblePoints)) {
                assemblePointMap = mtAssemblePoints.stream()
                                .collect(Collectors.toMap(t -> t.getAssemblePointId(), t -> t));
            }
        }

        // materialId列表
        List<String> materialIds = pointControlList.stream().map(MtAssemblePointControl::getMaterialId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtMaterialVO> mtMaterialVOMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(materialIds)) {
            // 获取物料编码和物料描述：
            List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
            if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                mtMaterialVOMap = mtMaterialVOS.stream().collect(Collectors.toMap(t -> t.getMaterialId(), t -> t));
            }
        }

        // materialLotId列表
        List<String> materialLotIds = pointControlList.stream().map(MtAssemblePointControl::getMaterialLotId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtMaterialLot> mtMaterialLotMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            // 获取物料批编码：
            List<MtMaterialLot> mtMaterialLots =
                            mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
            if (CollectionUtils.isNotEmpty(mtMaterialLots)) {
                mtMaterialLotMap = mtMaterialLots.stream().collect(Collectors.toMap(t -> t.getMaterialLotId(), t -> t));
            }
        }

        for (MtAssemblePointControl control : pointControlList) {
            MtAssemblePointControlVO7 controlVO7 = new MtAssemblePointControlVO7();
            BeanUtils.copyProperties(control, controlVO7);
            controlVO7.setAssemblePointDescription(null == assemblePointMap.get(control.getAssemblePointId()) ? null
                            : assemblePointMap.get(control.getAssemblePointId()).getDescription());
            controlVO7.setAssemblePointCode(null == assemblePointMap.get(control.getAssemblePointId()) ? null
                            : assemblePointMap.get(control.getAssemblePointId()).getAssemblePointCode());
            controlVO7.setMaterialCode(null == mtMaterialVOMap.get(control.getMaterialId()) ? null
                            : mtMaterialVOMap.get(control.getMaterialId()).getMaterialCode());
            controlVO7.setMaterialName(null == mtMaterialVOMap.get(control.getMaterialId()) ? null
                            : mtMaterialVOMap.get(control.getMaterialId()).getMaterialName());
            controlVO7.setMaterialLotCode(null == mtMaterialLotMap.get(control.getMaterialLotId()) ? null
                            : mtMaterialLotMap.get(control.getMaterialLotId()).getMaterialLotCode());
            pointControlVO7List.add(controlVO7);
        }
        return pointControlVO7List;
    }
}
