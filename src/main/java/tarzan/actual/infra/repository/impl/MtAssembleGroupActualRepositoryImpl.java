package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import tarzan.actual.domain.entity.MtAssembleGroupActual;
import tarzan.actual.domain.entity.MtAssembleGroupActualHis;
import tarzan.actual.domain.repository.MtAssembleGroupActualHisRepository;
import tarzan.actual.domain.repository.MtAssembleGroupActualRepository;
import tarzan.actual.domain.repository.MtAssemblePointActualRepository;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO1;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO2;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO3;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO4;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO5;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO6;
import tarzan.actual.domain.vo.MtAssemblePointActualVO1;
import tarzan.actual.infra.mapper.MtAssembleGroupActualMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.method.domain.entity.MtAssembleGroup;
import tarzan.method.domain.entity.MtAssemblePoint;
import tarzan.method.domain.repository.MtAssembleControlRepository;
import tarzan.method.domain.repository.MtAssembleGroupControlRepository;
import tarzan.method.domain.repository.MtAssembleGroupRepository;
import tarzan.method.domain.repository.MtAssemblePointRepository;
import tarzan.method.domain.vo.MtAssembleControlVO1;
import tarzan.method.domain.vo.MtAssembleGroupControlVO;
import tarzan.method.domain.vo.MtAssemblePointVO;

/**
 * 装配组实绩，记录装配组安装的位置 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtAssembleGroupActualRepositoryImpl extends BaseRepositoryImpl<MtAssembleGroupActual>
                implements MtAssembleGroupActualRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleGroupActualMapper mtAssembleGroupActualMapper;

    @Autowired
    private MtAssembleControlRepository mtAssembleControlRepository;

    @Autowired
    private MtAssembleGroupControlRepository mtAssembleGroupControlRepository;

    @Autowired
    private MtAssembleGroupRepository mtAssembleGroupRepository;

    @Autowired
    private MtAssemblePointActualRepository mtAssemblePointActualRepository;

    @Autowired
    private MtAssemblePointRepository mtAssemblePointRepository;

    @Autowired
    private MtAssembleGroupActualHisRepository mtAssembleGroupActualHisRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Override
    public List<MtAssembleGroupActual> assembleGroupActualPropertyBatchGet(Long tenantId,
                                                                           List<String> assembleGroupActualIds) {
        if (CollectionUtils.isEmpty(assembleGroupActualIds)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleGroupActualId", "【API:assembleGroupActualPropertyBatchGet】"));
        }
        return this.mtAssembleGroupActualMapper.selectByIdsCustom(tenantId, assembleGroupActualIds);
    }

    @Override
    public List<String> assembleControlLimitAssembleGroupQuery(Long tenantId, MtAssembleGroupActualVO condition) {
        if (StringUtils.isEmpty(condition.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "workcellId", "【API:assembleControlLimitAssembleGroupQuery】"));
        }
        if (StringUtils.isEmpty(condition.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:assembleControlLimitAssembleGroupQuery】"));
        }
        if (StringUtils.isEmpty(condition.getReferenceArea())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "referenceArea", "【API:assembleControlLimitAssembleGroupQuery】"));
        }

        MtAssembleControlVO1 assembleCondition = new MtAssembleControlVO1();
        assembleCondition.setObjectType("EO");
        assembleCondition.setObjectId(condition.getEoId());
        assembleCondition.setOrganizationType("WORKCELL");
        assembleCondition.setOrganizationId(condition.getWorkcellId());
        assembleCondition.setReferenceArea(condition.getReferenceArea());
        String assembleControlId = this.mtAssembleControlRepository.assembleControlGet(tenantId, assembleCondition);

        MtAssembleGroupControlVO assembleGroupCondition = new MtAssembleGroupControlVO();
        assembleGroupCondition.setAssembleControlId(assembleControlId);
        assembleGroupCondition.setWorkcellId(condition.getWorkcellId());
        return this.mtAssembleGroupControlRepository.wkcLimitAvailableAssembleGroupQuery(tenantId,
                        assembleGroupCondition);
    }

    @Override
    public MtAssembleGroupActual assembleGroupActualPropertyGet(Long tenantId, String assembleGroupActualId) {
        if (StringUtils.isEmpty(assembleGroupActualId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleGroupActualId", "【API:assembleGroupActualPropertyGet】"));
        }

        MtAssembleGroupActual mtAssembleGroupActual = new MtAssembleGroupActual();
        mtAssembleGroupActual.setTenantId(tenantId);
        mtAssembleGroupActual.setAssembleGroupActualId(assembleGroupActualId);
        return this.mtAssembleGroupActualMapper.selectOne(mtAssembleGroupActual);
    }

    @Override
    public List<String> propertyLimitAssembleGroupActualQuery(Long tenantId, MtAssembleGroupActualVO1 condition) {
        if (null == condition.getWorkcellId() && null == condition.getAssembleGroupId()) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "{workcellId、assembleGroupId}",
                                            "【API:propertyLimitAssembleGroupActualQuery】"));
        }

        MtAssembleGroupActual assembleGroupActual = new MtAssembleGroupActual();
        if (null != condition.getWorkcellId()) {
            assembleGroupActual.setWorkcellId(condition.getWorkcellId());
        }
        if (null != condition.getAssembleGroupId()) {
            assembleGroupActual.setAssembleGroupId(condition.getAssembleGroupId());
        }
        List<MtAssembleGroupActual> assembleGroupActuals =
                        this.mtAssembleGroupActualMapper.mySelect(tenantId, assembleGroupActual);
        if (CollectionUtils.isEmpty(assembleGroupActuals)) {
            return Collections.emptyList();
        }
        return assembleGroupActuals.stream().map(MtAssembleGroupActual::getAssembleGroupActualId)
                        .collect(Collectors.toList());
    }

    @Override
    public List<MtAssembleGroupActualVO2> wkcLimitCurrentAssembleGroupQuery(Long tenantId, String workcellId) {
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workcellId", "【API:wkcLimitCurrentAssembleGroupQuery】"));
        }

        MtAssembleGroupActual assembleGroupActual = new MtAssembleGroupActual();
        assembleGroupActual.setTenantId(tenantId);
        assembleGroupActual.setWorkcellId(workcellId);
        List<MtAssembleGroupActual> assembleGroupActuals = this.mtAssembleGroupActualMapper.select(assembleGroupActual);
        if (CollectionUtils.isEmpty(assembleGroupActuals)) {
            return Collections.emptyList();
        }

        final List<MtAssembleGroupActualVO2> result = new ArrayList<MtAssembleGroupActualVO2>();
        assembleGroupActuals.stream().forEach(c -> {
            MtAssembleGroupActualVO2 vo = new MtAssembleGroupActualVO2();
            vo.setAssembleGroupActualId(c.getAssembleGroupActualId());
            vo.setAssembleGroupId(c.getAssembleGroupId());
            result.add(vo);
        });
        return result;
    }

    @Override
    public String firstEmptyAssemblePointGet(Long tenantId, String assembleGroupId) {
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "assembleGroupId", "【API:firstEmptyAssemblePointGet】"));
        }

        MtAssembleGroup mtAssembleGroup =
                        this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId, assembleGroupId);
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0012", "ASSEMBLE", "【API:firstEmptyAssemblePointGet】"));
        }
        if (StringUtils.isEmpty(mtAssembleGroup.getAssembleSequenceFlag())
                        || !"Y".equals(mtAssembleGroup.getAssembleSequenceFlag())) {
            throw new MtException("MT_ASSEMBLE_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0021", "ASSEMBLE", "【API:firstEmptyAssemblePointGet】"));
        }

        MtAssemblePointActualVO1 assemblePointCondition = new MtAssemblePointActualVO1();
        assemblePointCondition.setAssembleGroupId(assembleGroupId);
        List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                        .propertyLimitAssemblePointActualQuery(tenantId, assemblePointCondition);

        final List<String> actualAssemblePointIds = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(assemblePointActualIds)) {
            List<MtAssemblePointActualVO1> mtAssemblePointActuals = this.mtAssemblePointActualRepository
                            .assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds);
            if (CollectionUtils.isNotEmpty(mtAssemblePointActuals)) {
                mtAssemblePointActuals.stream().forEach(c -> {
                    if (null != c.getQty() && BigDecimal.valueOf(c.getQty()).compareTo(BigDecimal.ZERO) != 0) {
                        actualAssemblePointIds.add(c.getAssemblePointId());
                    }
                });
            }
        }

        List<MtAssemblePointVO> mtAssemblePoints = this.mtAssemblePointRepository
                        .assembleGroupLimitAssemblePointOrderBySequenceSort(tenantId, assembleGroupId, "");
        final List<MtAssemblePointVO> groupAssemblePointIds = new ArrayList<MtAssemblePointVO>();
        if (CollectionUtils.isNotEmpty(mtAssemblePoints)) {
            mtAssemblePoints.stream().forEach(c -> {
                MtAssemblePointVO vo = new MtAssemblePointVO();
                vo.setAssemblePointId(c.getAssemblePointId());
                vo.setSequence(c.getSequence());
                groupAssemblePointIds.add(vo);
            });
        }

        if (CollectionUtils.isEmpty(actualAssemblePointIds) && CollectionUtils.isEmpty(groupAssemblePointIds)) {
            return null;
        } else if (CollectionUtils.isEmpty(actualAssemblePointIds)
                        && CollectionUtils.isNotEmpty(groupAssemblePointIds)) {
            return groupAssemblePointIds.stream().sorted(Comparator.comparingLong(MtAssemblePointVO::getSequence))
                            .findFirst().get().getAssemblePointId();
        } else if (CollectionUtils.isNotEmpty(actualAssemblePointIds)
                        && CollectionUtils.isEmpty(groupAssemblePointIds)) {
            return null;
        } else if (CollectionUtils.isNotEmpty(actualAssemblePointIds)
                        && CollectionUtils.isNotEmpty(groupAssemblePointIds)) {
            List<MtAssemblePointVO> result = groupAssemblePointIds.stream()
                            .filter(c -> !actualAssemblePointIds.contains(c.getAssemblePointId()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(result)) {
                return null;
            }
            return result.stream().sorted(Comparator.comparingLong(MtAssemblePointVO::getSequence)).findFirst().get()
                            .getAssemblePointId();
        } else {
            return null;
        }
    }

    @Override
    public String firstAvailableFeedingAssemblePointGet(Long tenantId, MtAssembleGroupActualVO3 condition) {
        if (StringUtils.isEmpty(condition.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleGroupId", "【API:firstAvailableFeedingAssemblePointGet】"));
        }

        MtAssembleGroup mtAssembleGroup = this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId,
                        condition.getAssembleGroupId());
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0012", "ASSEMBLE", "【API:firstAvailableFeedingAssemblePointGet】"));
        }
        if (StringUtils.isEmpty(mtAssembleGroup.getAssembleSequenceFlag())
                        || !"Y".equals(mtAssembleGroup.getAssembleSequenceFlag())) {
            throw new MtException("MT_ASSEMBLE_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0021", "ASSEMBLE", "【API:firstAvailableFeedingAssemblePointGet】"));
        }

        /*
         * 若输入参数materialId、materialLotId均未输入或均输入为空; 传入输入参数assembleGroupId直接获取首个未装配的装配点
         */
        if (StringUtils.isEmpty(condition.getMaterialId()) && StringUtils.isEmpty(condition.getMaterialLotId())) {
            return firstEmptyAssemblePointGet(tenantId, condition.getAssembleGroupId());
        } else if (StringUtils.isNotEmpty(condition.getMaterialId())
                        && StringUtils.isEmpty(condition.getMaterialLotId())) {
            MtAssemblePointActualVO1 assemblePointCondition = new MtAssemblePointActualVO1();
            assemblePointCondition.setAssembleGroupId(condition.getAssembleGroupId());
            assemblePointCondition.setMaterialId(condition.getMaterialId());
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, assemblePointCondition);

            // 若获取结果为空
            if (CollectionUtils.isEmpty(assemblePointActualIds)) {
                return firstEmptyAssemblePointGet(tenantId, condition.getAssembleGroupId());
            }

            List<MtAssemblePointActualVO1> mtAssemblePointActuals = this.mtAssemblePointActualRepository
                            .assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds);

            List<String> assemblePointIds = mtAssemblePointActuals.stream()
                            .map(MtAssemblePointActualVO1::getAssemblePointId).collect(Collectors.toList());

            Map<String, BigDecimal> map = mtAssemblePointActuals.stream().collect(Collectors
                            .groupingBy(MtAssemblePointActualVO1::getAssemblePointId, CollectorsUtil.summingBigDecimal(
                                            c -> BigDecimal.valueOf(c.getQty() == null ? 0.0D : c.getQty()))));

            List<MtAssemblePoint> mtAssemblePoints =
                            this.mtAssemblePointRepository.assemblePointPropertyBatchGet(tenantId, assemblePointIds);

            final List<MtAssemblePoint> result = new ArrayList<MtAssemblePoint>();
            mtAssemblePoints.stream().forEach(c -> {
                BigDecimal sumQty = map.get(c.getAssemblePointId());
                if (null == c.getMaxQty()) {
                    // if (sumQty.compareTo(0.0D) > 0) {
                    if (sumQty.compareTo(BigDecimal.ZERO) > 0) {
                        result.add(c);
                    }
                } else {
                    // if (sumQty.compareTo(0.0D) > 0 &&
                    // sumQty.compareTo(c.getMaxQty()) < 0) {
                    if (sumQty.compareTo(BigDecimal.ZERO) > 0
                                    && sumQty.compareTo(BigDecimal.valueOf(c.getMaxQty())) < 0) {
                        result.add(c);
                    }
                }
            });

            if (CollectionUtils.isNotEmpty(result)) {
                return result.stream().sorted(Comparator.comparingLong(MtAssemblePoint::getSequence).reversed())
                                .findFirst().get().getAssemblePointId();
            } else {
                return firstEmptyAssemblePointGet(tenantId, condition.getAssembleGroupId());
            }
        } else if (StringUtils.isNotEmpty(condition.getMaterialLotId())) {
            // 获取物料批对应物料lot_materialId
            MtMaterialLot mtMaterialLot =
                            mtMaterialLotRepository.materialLotPropertyGet(tenantId, condition.getMaterialLotId());
            if (mtMaterialLot == null) {
                throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0017", "ASSEMBLE", "【API:firstAvailableFeedingAssemblePointGet】"));
            }

            if (StringUtils.isNotEmpty(condition.getMaterialId())
                            && !condition.getMaterialId().equals(mtMaterialLot.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0018", "ASSEMBLE", "【API:firstAvailableFeedingAssemblePointGet】"));
            }

            MtAssemblePointActualVO1 assemblePointCondition = new MtAssemblePointActualVO1();
            assemblePointCondition.setAssembleGroupId(condition.getAssembleGroupId());
            assemblePointCondition.setMaterialLotId(condition.getMaterialLotId());
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, assemblePointCondition);

            if (CollectionUtils.isNotEmpty(assemblePointActualIds)) {
                throw new MtException("MT_ASSEMBLE_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0022", "ASSEMBLE", "【API:firstAvailableFeedingAssemblePointGet】"));
            }

            assemblePointCondition = new MtAssemblePointActualVO1();
            assemblePointCondition.setAssembleGroupId(condition.getAssembleGroupId());
            assemblePointCondition.setMaterialId(mtMaterialLot.getMaterialId());
            assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, assemblePointCondition);

            // 若获取结果为空
            if (CollectionUtils.isEmpty(assemblePointActualIds)) {
                return firstEmptyAssemblePointGet(tenantId, condition.getAssembleGroupId());
            }

            List<MtAssemblePointActualVO1> mtAssemblePointActuals = this.mtAssemblePointActualRepository
                            .assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds);

            List<String> assemblePointIds = mtAssemblePointActuals.stream()
                            .map(MtAssemblePointActualVO1::getAssemblePointId).collect(Collectors.toList());

            Map<String, BigDecimal> map = mtAssemblePointActuals.stream().collect(Collectors
                            .groupingBy(MtAssemblePointActualVO1::getAssemblePointId, CollectorsUtil.summingBigDecimal(
                                            c -> BigDecimal.valueOf(c.getQty() == null ? 0.0D : c.getQty()))));

            List<MtAssemblePoint> mtAssemblePoints =
                            this.mtAssemblePointRepository.assemblePointPropertyBatchGet(tenantId, assemblePointIds);

            final List<MtAssemblePoint> result = new ArrayList<MtAssemblePoint>();
            mtAssemblePoints.stream().forEach(c -> {
                BigDecimal sumQty = map.get(c.getAssemblePointId());
                if (null == c.getMaxQty()) {
                    // if (sumQty.compareTo(0.0D) > 0) {
                    if (sumQty.compareTo(BigDecimal.ZERO) > 0) {
                        result.add(c);
                    }
                } else {
                    // if (sumQty.compareTo(0.0D) > 0 &&
                    // sumQty.compareTo(c.getMaxQty()) < 0) {
                    if (sumQty.compareTo(BigDecimal.ZERO) > 0
                                    && sumQty.compareTo(BigDecimal.valueOf(c.getMaxQty())) < 0) {
                        result.add(c);
                    }
                }
            });

            if (CollectionUtils.isNotEmpty(result)) {
                MtAssemblePoint mtAssemblePoint = result.stream()
                                .sorted(Comparator.comparingLong(MtAssemblePoint::getSequence).reversed()).findFirst()
                                .get();
                if (!"Y".equals(mtAssemblePoint.getUniqueMaterialLotFlag())) {
                    return mtAssemblePoint.getAssemblePointId();
                } else {
                    return firstEmptyAssemblePointGet(tenantId, condition.getAssembleGroupId());
                }
            } else {
                return firstEmptyAssemblePointGet(tenantId, condition.getAssembleGroupId());
            }
        } else {
            return null;
        }
    }

    @Override
    public void wkcAssembleGroupControlVerify(Long tenantId, MtAssembleGroupActualVO4 condition) {
        if (StringUtils.isEmpty(condition.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workcellId", "【API:wkcAssembleGroupControlVerify】"));
        }
        if (StringUtils.isEmpty(condition.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:wkcAssembleGroupControlVerify】"));
        }
        if (StringUtils.isEmpty(condition.getReferenceArea())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "referenceArea", "【API:wkcAssembleGroupControlVerify】"));
        }
        if (StringUtils.isEmpty(condition.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "assembleGroupId", "【API:wkcAssembleGroupControlVerify】"));
        }

        MtAssembleGroup mtAssembleGroup = this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId,
                        condition.getAssembleGroupId());
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0012", "ASSEMBLE", "【API:wkcAssembleGroupControlVerify】"));
        }
        if (StringUtils.isEmpty(mtAssembleGroup.getAssembleControlFlag())
                        || "N".equals(mtAssembleGroup.getAssembleControlFlag())) {
            return;
        }

        MtAssembleGroupActualVO groupActualCondition = new MtAssembleGroupActualVO();
        groupActualCondition.setWorkcellId(condition.getWorkcellId());
        groupActualCondition.setEoId(condition.getEoId());
        groupActualCondition.setReferenceArea(condition.getReferenceArea());
        List<String> assembleGroupIds = assembleControlLimitAssembleGroupQuery(tenantId, groupActualCondition);

        if (CollectionUtils.isEmpty(assembleGroupIds) || !assembleGroupIds.contains(condition.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0011", "ASSEMBLE", "【API:wkcAssembleGroupControlVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtAssembleGroupActualVO6 assembleGroupActualUpdate(Long tenantId, MtAssembleGroupActualVO5 dto) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eventId", "【API:assembleGroupActualUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssembleGroupActualId()) && StringUtils.isEmpty(dto.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "{assembleGroupActualId、assembleGroupId}",
                                            "【API:assembleGroupActualUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssembleGroupActualId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "{assembleGroupActualId、workcellId}", "【API:assembleGroupActualUpdate】"));
        }

        MtAssembleGroupActual mtAssembleGroupActual;
        if (StringUtils.isNotEmpty(dto.getAssembleGroupActualId())) {
            mtAssembleGroupActual = assembleGroupActualPropertyGet(tenantId, dto.getAssembleGroupActualId());
            if (null == mtAssembleGroupActual) {
                throw new MtException("MT_ASSEMBLE_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0015", "ASSEMBLE", "【API:assembleGroupActualUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getAssembleGroupId())) {
                mtAssembleGroupActual.setAssembleGroupId(dto.getAssembleGroupId());
            }
            if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
                mtAssembleGroupActual.setWorkcellId(dto.getWorkcellId());
            }
            self().updateByPrimaryKeySelective(mtAssembleGroupActual);
        } else {
            MtAssembleGroupActualVO1 condition = new MtAssembleGroupActualVO1();
            condition.setAssembleGroupId(dto.getAssembleGroupId());
            condition.setWorkcellId(dto.getWorkcellId());
            List<String> assembleGroupActualIds = propertyLimitAssembleGroupActualQuery(tenantId, condition);

            if (CollectionUtils.isEmpty(assembleGroupActualIds)) {
                mtAssembleGroupActual = new MtAssembleGroupActual();
                mtAssembleGroupActual.setTenantId(tenantId);
                mtAssembleGroupActual.setAssembleGroupId(dto.getAssembleGroupId());
                mtAssembleGroupActual.setWorkcellId(dto.getWorkcellId());
                self().insertSelective(mtAssembleGroupActual);
            } else {
                mtAssembleGroupActual = new MtAssembleGroupActual();
                mtAssembleGroupActual.setTenantId(tenantId);
                mtAssembleGroupActual.setAssembleGroupActualId(assembleGroupActualIds.get(0));
                mtAssembleGroupActual = this.mtAssembleGroupActualMapper.selectOne(mtAssembleGroupActual);
            }
        }

        MtAssembleGroupActualHis mtAssembleGroupActualHis = new MtAssembleGroupActualHis();
        mtAssembleGroupActualHis.setTenantId(tenantId);
        mtAssembleGroupActualHis.setAssembleGroupId(mtAssembleGroupActual.getAssembleGroupId());
        mtAssembleGroupActualHis.setAssembleGroupActualId(mtAssembleGroupActual.getAssembleGroupActualId());
        mtAssembleGroupActualHis.setWorkcellId(mtAssembleGroupActual.getWorkcellId());
        mtAssembleGroupActualHis.setEventId(dto.getEventId());
        this.mtAssembleGroupActualHisRepository.insertSelective(mtAssembleGroupActualHis);

        MtAssembleGroupActualVO6 result = new MtAssembleGroupActualVO6();
        result.setAssembleGroupActualId(mtAssembleGroupActual.getAssembleGroupActualId());
        result.setAssembleGroupActualHisId(mtAssembleGroupActualHis.getAssembleGroupActualHisId());

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assembleGroupActualDelete(Long tenantId, String assembleGroupActualId) {
        if (StringUtils.isEmpty(assembleGroupActualId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleGroupActualId", "【API:assembleGroupActualDelete】"));
        }

        MtAssemblePointActualVO1 assemblePointCondition = new MtAssemblePointActualVO1();
        assemblePointCondition.setAssembleGroupActualId(assembleGroupActualId);
        List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                        .propertyLimitAssemblePointActualQuery(tenantId, assemblePointCondition);
        if (CollectionUtils.isNotEmpty(assemblePointActualIds)) {
            this.mtAssemblePointActualRepository.assemblePointActualDeleteBatch(tenantId, assemblePointActualIds);
        }

        MtAssembleGroupActual mtAssembleGroupActual = new MtAssembleGroupActual();
        mtAssembleGroupActual.setTenantId(tenantId);
        mtAssembleGroupActual.setAssembleGroupActualId(assembleGroupActualId);
        mtAssembleGroupActual = this.mtAssembleGroupActualMapper.selectOne(mtAssembleGroupActual);
        if (null != mtAssembleGroupActual) {
            this.mtAssembleGroupActualMapper.deleteByPrimaryKey(mtAssembleGroupActual);
        }
    }

}
