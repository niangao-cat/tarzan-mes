package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.actual.domain.entity.MtAssemblePointActual;
import tarzan.actual.domain.entity.MtAssemblePointActualHis;
import tarzan.actual.domain.repository.MtAssembleGroupActualHisRepository;
import tarzan.actual.domain.repository.MtAssembleGroupActualRepository;
import tarzan.actual.domain.repository.MtAssemblePointActualHisRepository;
import tarzan.actual.domain.repository.MtAssemblePointActualRepository;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO2;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO1;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO5;
import tarzan.actual.domain.vo.MtAssemblePointActualVO;
import tarzan.actual.domain.vo.MtAssemblePointActualVO1;
import tarzan.actual.domain.vo.MtAssemblePointActualVO2;
import tarzan.actual.domain.vo.MtAssemblePointActualVO3;
import tarzan.actual.domain.vo.MtAssemblePointActualVO4;
import tarzan.actual.domain.vo.MtAssemblePointActualVO5;
import tarzan.actual.domain.vo.MtAssemblePointActualVO6;
import tarzan.actual.domain.vo.MtAssemblePointActualVO7;
import tarzan.actual.domain.vo.MtAssemblePointActualVO8;
import tarzan.actual.domain.vo.MtAssemblePointActualVO9;
import tarzan.actual.infra.mapper.MtAssemblePointActualMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO11;
import tarzan.inventory.domain.vo.MtMaterialLotVO4;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtAssembleGroup;
import tarzan.method.domain.entity.MtAssemblePoint;
import tarzan.method.domain.repository.MtAssembleControlRepository;
import tarzan.method.domain.repository.MtAssembleGroupRepository;
import tarzan.method.domain.repository.MtAssemblePointControlRepository;
import tarzan.method.domain.repository.MtAssemblePointRepository;
import tarzan.method.domain.vo.MtAssembleControlVO1;
import tarzan.method.domain.vo.MtAssemblePointControlVO1;

/**
 * 装配点实绩，记录装配组下装配点实际装配信息 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtAssemblePointActualRepositoryImpl extends BaseRepositoryImpl<MtAssemblePointActual>
                implements MtAssemblePointActualRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleControlRepository mtAssembleControlRepository;

    @Autowired
    private MtAssemblePointControlRepository mtAssemblePointControlRepository;

    @Autowired
    private MtAssemblePointActualMapper assemblePointActualMapper;

    @Autowired
    private MtAssemblePointRepository mtAssemblePointRepository;

    @Autowired
    private MtAssembleGroupRepository mtAssembleGroupRepository;

    @Autowired
    private MtAssembleGroupActualRepository mtAssembleGroupActualRepository;

    @Autowired
    private MtAssemblePointActualHisRepository mtAssemblePointActualHisRepository;

    @Autowired
    private MtAssembleGroupActualHisRepository mtAssembleGroupActualHisRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Override
    public List<String> assembleControlLimitAssemblePointQuery(Long tenantId, MtAssemblePointActualVO condition) {
        if (StringUtils.isEmpty(condition.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "workcellId", "【API:assembleControlLimitAssemblePointQuery】"));
        }
        if (StringUtils.isEmpty(condition.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:assembleControlLimitAssemblePointQuery】"));
        }
        if (StringUtils.isEmpty(condition.getReferenceArea())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "referenceArea", "【API:assembleControlLimitAssemblePointQuery】"));
        }
        if (StringUtils.isEmpty(condition.getMaterialLotId()) && StringUtils.isEmpty(condition.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "{materialLotId、materialId}",
                                            "【API:assembleControlLimitAssemblePointQuery】"));
        }

        MtAssembleControlVO1 assembleCondition = new MtAssembleControlVO1();
        assembleCondition.setObjectType("EO");
        assembleCondition.setObjectId(condition.getEoId());
        assembleCondition.setOrganizationType("WORKCELL");
        assembleCondition.setOrganizationId(condition.getWorkcellId());
        assembleCondition.setReferenceArea(condition.getReferenceArea());
        String assembleControlId = this.mtAssembleControlRepository.assembleControlGet(tenantId, assembleCondition);

        MtAssemblePointControlVO1 assemblePointCondition = new MtAssemblePointControlVO1();
        assemblePointCondition.setAssembleControlId(assembleControlId);
        assemblePointCondition.setMaterialId(condition.getMaterialId());
        assemblePointCondition.setMaterialLotId(condition.getMaterialLotId());
        return this.mtAssemblePointControlRepository.materialLimitAvailableAssemblePointQuery(tenantId,
                        assemblePointCondition);
    }

    @Override
    public List<MtAssemblePointActualVO1> assemblePointActualPropertyBatchGet(Long tenantId,
                                                                              List<String> assemblePointActualIds) {
        if (CollectionUtils.isEmpty(assemblePointActualIds)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointActualId", "【API:assemblePointActualPropertyBatchGet】"));
        }
        return this.assemblePointActualMapper.selectPropertyByIds(tenantId, assemblePointActualIds);
    }

    @Override
    public MtAssemblePointActualVO1 assemblePointActualPropertyGet(Long tenantId, String assemblePointActualId) {
        if (StringUtils.isEmpty(assemblePointActualId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointActualId", "【API:assemblePointActualPropertyGet】"));
        }
        return this.assemblePointActualMapper.selectPropertyById(tenantId, assemblePointActualId);
    }

    @Override
    public List<String> propertyLimitAssemblePointActualQuery(Long tenantId, MtAssemblePointActualVO1 condition) {
        if (null == condition.getAssembleGroupActualId() && null == condition.getAssemblePointId()
                        && null == condition.getFeedingSequence() && null == condition.getMaterialId()
                        && null == condition.getFeedingMaterialLotSequence() && null == condition.getMaterialLotId()
                        && null == condition.getWorkcellId() && null == condition.getAssembleGroupId()) {
            throw new MtException("MT_ASSEMBLE_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0002", "ASSEMBLE",
                            "{assembleGroupActualId、assemblePointId、feedingSequence、materialId、feedingMaterialLotSequence、materialLotId、workcellId、assembleGroupId}",
                            "【API:propertyLimitAssemblePointActualQuery】"));
        }
        return this.assemblePointActualMapper.selectPropertyByCondition(tenantId, condition);
    }

    @Override
    public Long assemblePointFeedingNextSequenceGet(Long tenantId, MtAssemblePointActualVO6 condition) {
        if (StringUtils.isEmpty(condition.getAssembleGroupId())
                        && StringUtils.isEmpty(condition.getAssembleGroupActualId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "{assembleGroupId、assembleGroupActualId}",
                                            "【API:assemblePointFeedingNextSequenceGet】"));
        }

        MtAssemblePointActualVO1 assemblePointActualCondition = new MtAssemblePointActualVO1();
        if (StringUtils.isNotEmpty(condition.getAssembleGroupId())) {
            assemblePointActualCondition.setAssembleGroupId(condition.getAssembleGroupId());
        }
        if (StringUtils.isNotEmpty(condition.getAssembleGroupActualId())) {
            assemblePointActualCondition.setAssembleGroupActualId(condition.getAssembleGroupActualId());
        }
        List<String> assemblePointActualIds =
                        propertyLimitAssemblePointActualQuery(tenantId, assemblePointActualCondition);
        if (CollectionUtils.isEmpty(assemblePointActualIds)) {
            return 1L;
        }

        List<MtAssemblePointActualVO1> assemblePointActualList =
                        assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds);
        assemblePointActualList.stream().forEach(c -> {
            if (null == c.getFeedingSequence()) {
                c.setFeedingSequence(Long.valueOf(0L));
            }
        });

        MtAssemblePointActualVO1 vo = assemblePointActualList.stream()
                        .sorted(Comparator.comparingLong(MtAssemblePointActualVO1::getFeedingSequence).reversed())
                        .findFirst().get();
        return vo.getFeedingSequence() + 1L;
    }

    @Override
    public Double assemblePointLoadingMaterialQtyGet(Long tenantId, MtAssemblePointActualVO2 condition) {
        if (StringUtils.isEmpty(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointId", "【API:assemblePointLoadingMaterialQtyGet】"));
        }

        MtAssemblePointActual assemblePointActual = new MtAssemblePointActual();
        assemblePointActual.setTenantId(tenantId);
        assemblePointActual.setAssemblePointId(condition.getAssemblePointId());
        if (StringUtils.isNotEmpty(condition.getMaterialId())) {
            assemblePointActual.setMaterialId(condition.getMaterialId());
        }
        List<MtAssemblePointActual> assemblePointActuals = this.assemblePointActualMapper.select(assemblePointActual);
        if (CollectionUtils.isEmpty(assemblePointActuals)) {
            return 0.0D;
        }

        BigDecimal result = assemblePointActuals.stream().collect(CollectorsUtil
                        .summingBigDecimal(c -> BigDecimal.valueOf(c.getQty() == null ? 0.0D : c.getQty())));
        return result.doubleValue();
    }

    @Override
    public Long feedingLotNextSequenceGet(Long tenantId, String assemblePointId) {
        if (StringUtils.isEmpty(assemblePointId)) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "assemblePointId", "【API:feedingLotNextSequenceGet】"));
        }

        MtAssemblePointActualVO1 assemblePointActualCondition = new MtAssemblePointActualVO1();
        assemblePointActualCondition.setAssemblePointId(assemblePointId);
        List<String> assemblePointActualIds =
                        propertyLimitAssemblePointActualQuery(tenantId, assemblePointActualCondition);
        if (CollectionUtils.isEmpty(assemblePointActualIds)) {
            return 1L;
        }

        List<MtAssemblePointActualVO1> assemblePointActualList =
                        assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds);
        assemblePointActualList.stream().forEach(c -> {
            if (null == c.getFeedingMaterialLotSequence()) {
                c.setFeedingMaterialLotSequence(Long.valueOf(0L));
            }
        });

        MtAssemblePointActualVO1 vo = assemblePointActualList.stream().sorted(
                        Comparator.comparingLong(MtAssemblePointActualVO1::getFeedingMaterialLotSequence).reversed())
                        .findFirst().get();
        return vo.getFeedingMaterialLotSequence() + 1L;
    }

    @Override
    public void materialAssemblePointControlVerify(Long tenantId, MtAssemblePointActualVO3 condition) {
        if (StringUtils.isEmpty(condition.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workcellId", "【API:materialAssemblePointControlVerify】"));
        }
        if (StringUtils.isEmpty(condition.getEoId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eoId", "【API:materialAssemblePointControlVerify】"));
        }
        if (StringUtils.isEmpty(condition.getReferenceArea())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "referenceArea", "【API:materialAssemblePointControlVerify】"));
        }
        if (StringUtils.isEmpty(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointId", "【API:materialAssemblePointControlVerify】"));
        }

        MtAssemblePoint mtAssemblePoint = this.mtAssemblePointRepository.assemblePointPropertyGet(tenantId,
                        condition.getAssemblePointId());
        if (null == mtAssemblePoint || StringUtils.isEmpty(mtAssemblePoint.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0013", "ASSEMBLE", "【API:materialAssemblePointControlVerify】"));
        }

        MtAssembleGroup mtAssembleGroup = this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId,
                        mtAssemblePoint.getAssembleGroupId());
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0012", "ASSEMBLE", "【API:materialAssemblePointControlVerify】"));
        }
        if (StringUtils.isEmpty(mtAssembleGroup.getAssembleControlFlag())
                        || "N".equals(mtAssembleGroup.getAssembleControlFlag())) {
            return;
        }

        MtAssemblePointActualVO pointCondition = new MtAssemblePointActualVO();
        pointCondition.setWorkcellId(condition.getWorkcellId());
        pointCondition.setEoId(condition.getEoId());
        pointCondition.setReferenceArea(condition.getReferenceArea());
        pointCondition.setMaterialLotId(condition.getMaterialLotId());
        pointCondition.setMaterialId(condition.getMaterialId());
        List<String> assemblePointIds = assembleControlLimitAssemblePointQuery(tenantId, pointCondition);
        if (CollectionUtils.isEmpty(assemblePointIds) || !assemblePointIds.contains(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0014", "ASSEMBLE", "【API:materialAssemblePointControlVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assemblePointActualDelete(Long tenantId, String assemblePointActualId) {
        if (StringUtils.isEmpty(assemblePointActualId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointActualId", "【API:assemblePointActualDelete】"));
        }
        assemblePointActualDeleteBatch(tenantId, Arrays.asList(assemblePointActualId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assemblePointActualDeleteBatch(Long tenantId, List<String> assemblePointActualIds) {
        if (CollectionUtils.isEmpty(assemblePointActualIds)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointActualId", "【API:assemblePointActualDeleteBatch】"));
        }
        this.assemblePointActualMapper.deleteByIdsCustom(tenantId, assemblePointActualIds);
    }

    @Override
    public void assemblePointHaveMaterialVerify(Long tenantId, MtAssemblePointActualVO5 condition) {
        if (StringUtils.isEmpty(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointId", "【API:assemblePointHaveMaterialVerify】"));
        }
        if (StringUtils.isEmpty(condition.getMaterialId()) && StringUtils.isEmpty(condition.getMaterialLotId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "{materialId、materialLotId}", "【API:assemblePointHaveMaterialVerify】"));
        }

        MtAssemblePointActualVO1 pointCondition = new MtAssemblePointActualVO1();
        pointCondition.setAssemblePointId(condition.getAssemblePointId());
        if (StringUtils.isNotEmpty(condition.getMaterialId())) {
            pointCondition.setMaterialId(condition.getMaterialId());
        }
        if (null != condition.getMaterialLotId()) {
            pointCondition.setMaterialLotId(condition.getMaterialLotId());
        }

        List<String> assmeblePointActualIds = propertyLimitAssemblePointActualQuery(tenantId, pointCondition);
        if (CollectionUtils.isEmpty(assmeblePointActualIds)) {
            throw new MtException("MT_ASSEMBLE_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0032", "ASSEMBLE", "【API:assemblePointHaveMaterialVerify】"));
        }

        if (null == condition.getQty() || BigDecimal.valueOf(condition.getQty()).compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        if (BigDecimal.valueOf(condition.getQty()).compareTo(BigDecimal.ZERO) > 0) {
            List<MtAssemblePointActualVO1> mtAssemblePointActuals =
                            assemblePointActualPropertyBatchGet(tenantId, assmeblePointActualIds);

            BigDecimal currentQty = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(mtAssemblePointActuals)) {
                currentQty = mtAssemblePointActuals.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> BigDecimal.valueOf(c.getQty() == null ? Double.valueOf(0.0D) : c.getQty())));
            }
            if (BigDecimal.valueOf(condition.getQty()).compareTo(currentQty) > 0) {
                throw new MtException("MT_ASSEMBLE_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0033", "ASSEMBLE", "【API:assemblePointHaveMaterialVerify】"));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtAssemblePointActualVO7 assemblePointActualUpdate(Long tenantId, MtAssemblePointActualVO4 dto,
                                                              String fullUpdate) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "eventId", "【API:assemblePointActualUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssemblePointActualId()) && StringUtils.isEmpty(dto.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "{assemblePointActualId、assemblePointId}",
                                            "【API:assemblePointActualUpdate】"));
        }
        if (null != dto.getQty() && null != dto.getTrxQty()) {
            throw new MtException("MT_ASSEMBLE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0009", "ASSEMBLE", "{qty、trxQty}", "【API:assemblePointActualUpdate】"));
        }
        if (null != dto.getFeedingQty() && null != dto.getTrxFeedingQty()) {
            throw new MtException("MT_ASSEMBLE_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0009", "ASSEMBLE",
                                            "{feedingQty、trxFeedingQty}", "【API:assemblePointActualUpdate】"));
        }
        MtAssemblePointActualVO7 mtAssemblePointActualVO7 = new MtAssemblePointActualVO7();
        /*
         * 供后续逻辑使用
         */
        boolean matLotIdIsNotEmpty = false;
        MtMaterialLot mtMaterialLot = null;
        if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            matLotIdIsNotEmpty = true;
            mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
            if (mtMaterialLot == null || StringUtils.isEmpty(mtMaterialLot.getMaterialLotId())) {
                throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0017", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
            }
        }

        if (StringUtils.isEmpty(dto.getAssemblePointActualId())) {
            if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getMaterialLotId())) {
                throw new MtException("MT_ASSEMBLE_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                "ASSEMBLE", "{materialId、materialLotId}",
                                                "【API:assemblePointActualUpdate】"));
            }
            if (StringUtils.isNotEmpty(dto.getMaterialId()) && matLotIdIsNotEmpty) {
                // 传入materialLotId获取materialId并于输入参数materialId比较
                if (!dto.getMaterialId().equals(mtMaterialLot.getMaterialId())) {
                    throw new MtException("MT_ASSEMBLE_0018", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0018", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
                }
            }
        }

        MtAssemblePointActual mtAssemblePointActual = null;
        Double oldQty = 0.0D;
        Double oldFeedingQty = 0.0D;
        if (StringUtils.isNotEmpty(dto.getAssemblePointActualId())) {
            MtAssemblePointActualVO1 oldAssemblePointActual =
                            assemblePointActualPropertyGet(tenantId, dto.getAssemblePointActualId());
            if (null == oldAssemblePointActual) {
                throw new MtException("MT_ASSEMBLE_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0016", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
            }

            // 根据输入值更新装配点实绩
            oldQty = oldAssemblePointActual.getQty();
            oldFeedingQty = oldAssemblePointActual.getFeedingQty();

            /*
             * 逻辑变更 2019/04/26 获取 assembleGroupActualId
             */
            String assemblePointId = dto.getAssemblePointId();
            if (StringUtils.isEmpty(assemblePointId)) {
                assemblePointId = oldAssemblePointActual.getAssemblePointId();
            }

            MtAssemblePoint mtAssemblePoint =
                            mtAssemblePointRepository.assemblePointPropertyGet(tenantId, assemblePointId);
            if (mtAssemblePoint == null || StringUtils.isEmpty(mtAssemblePoint.getAssembleGroupId())) {
                throw new MtException("MT_ASSEMBLE_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0015", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
            }

            MtAssembleGroupActualVO1 groupActual = new MtAssembleGroupActualVO1();
            groupActual.setAssembleGroupId(mtAssemblePoint.getAssembleGroupId());
            List<String> assembleGroupActualIds = mtAssembleGroupActualRepository
                            .propertyLimitAssembleGroupActualQuery(tenantId, groupActual);
            if (CollectionUtils.isEmpty(assembleGroupActualIds)) {
                throw new MtException("MT_ASSEMBLE_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0015", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
            }

            // 获取数据唯一 （唯一性索引）
            oldAssemblePointActual.setAssembleGroupActualId(assembleGroupActualIds.get(0));

            if (StringUtils.isNotEmpty(dto.getAssemblePointId())) {
                oldAssemblePointActual.setAssemblePointId(dto.getAssemblePointId());
            }
            if (null != dto.getFeedingSequence()) {
                oldAssemblePointActual.setFeedingSequence(dto.getFeedingSequence());
            }

            /*
             * 逻辑变更 2019/04/26 获取 materialId
             */
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                if (!matLotIdIsNotEmpty && StringUtils.isNotEmpty(oldAssemblePointActual.getMaterialLotId())) {
                    MtMaterialLot pointMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId,
                                    oldAssemblePointActual.getMaterialLotId());

                    /*
                     * 比较 pointMaterialLot 的 materialId 跟输入materialId是否一致 若 pointMaterialLot 为空，按照不一致处理
                     */
                    if (pointMaterialLot == null || !dto.getMaterialId().equals(pointMaterialLot.getMaterialId())) {
                        throw new MtException("MT_ASSEMBLE_0018", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ASSEMBLE_0018", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
                    }
                }

                oldAssemblePointActual.setMaterialId(dto.getMaterialId());
            } else {
                if (matLotIdIsNotEmpty) {
                    // 根据 materialLotId 获取 materialId 作为更新目标值
                    oldAssemblePointActual.setMaterialId(mtMaterialLot.getMaterialId());
                }
            }

            oldAssemblePointActual.setQty(dto.getQty());
            if (null != dto.getTrxQty()) {
                oldAssemblePointActual.setQty(new BigDecimal(oldAssemblePointActual.getQty().toString())
                                .add(new BigDecimal(dto.getTrxQty().toString())).doubleValue());
            }
            oldAssemblePointActual.setFeedingQty(dto.getFeedingQty());
            if (null != dto.getTrxFeedingQty()) {
                oldAssemblePointActual.setFeedingQty(new BigDecimal(oldAssemblePointActual.getFeedingQty().toString())
                                .add(new BigDecimal(dto.getTrxFeedingQty().toString())).doubleValue());
            }
            oldAssemblePointActual.setFeedingMaterialLotSequence(dto.getFeedingMaterialLotSequence());
            if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
                oldAssemblePointActual.setMaterialLotId(dto.getMaterialLotId());
            }

            mtAssemblePointActual = new MtAssemblePointActual();
            mtAssemblePointActual.setTenantId(tenantId);
            mtAssemblePointActual.setAssemblePointActualId(oldAssemblePointActual.getAssmeblePointActualId());
            mtAssemblePointActual.setAssembleGroupActualId(oldAssemblePointActual.getAssembleGroupActualId());
            mtAssemblePointActual.setAssemblePointId(oldAssemblePointActual.getAssemblePointId());
            mtAssemblePointActual.setFeedingSequence(oldAssemblePointActual.getFeedingSequence());
            mtAssemblePointActual.setMaterialId(oldAssemblePointActual.getMaterialId());
            mtAssemblePointActual.setQty(oldAssemblePointActual.getQty());
            mtAssemblePointActual.setFeedingQty(oldAssemblePointActual.getFeedingQty());
            mtAssemblePointActual.setFeedingMaterialLotSequence(oldAssemblePointActual.getFeedingMaterialLotSequence());
            mtAssemblePointActual.setMaterialLotId(oldAssemblePointActual.getMaterialLotId());
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                mtAssemblePointActual =
                                (MtAssemblePointActual) ObjectFieldsHelper.setStringFieldsEmpty(mtAssemblePointActual);
                self().updateByPrimaryKey(mtAssemblePointActual);
                // 设置返回装配的id
                mtAssemblePointActualVO7.setAssemblePointActualId(mtAssemblePointActual.getAssemblePointActualId());
            } else {
                self().updateByPrimaryKeySelective(mtAssemblePointActual);
                // 设置返回装配的id
                mtAssemblePointActualVO7.setAssemblePointActualId(mtAssemblePointActual.getAssemblePointActualId());
            }
        } else {
            MtAssemblePointActualVO1 pointCondition = new MtAssemblePointActualVO1();
            pointCondition.setAssemblePointId(dto.getAssemblePointId());
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                pointCondition.setMaterialId(dto.getMaterialId());
            }
            if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
                pointCondition.setMaterialLotId(dto.getMaterialLotId());
            } else {
                pointCondition.setMaterialLotId("");
            }
            List<String> assmeblePointActualIds = propertyLimitAssemblePointActualQuery(tenantId, pointCondition);

            // 若获取到结果不为空，对获取到的结果进行更新
            if (CollectionUtils.isNotEmpty(assmeblePointActualIds)) {
                // assemblePointId、materialId、materialLotId确定一条记录
                String assmeblePointActualId = assmeblePointActualIds.get(0);
                MtAssemblePointActualVO1 oldAssemblePointActual =
                                assemblePointActualPropertyGet(tenantId, assmeblePointActualId);
                if (null == oldAssemblePointActual) {
                    throw new MtException("MT_ASSEMBLE_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0016", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
                }

                oldQty = oldAssemblePointActual.getQty();
                oldFeedingQty = oldAssemblePointActual.getFeedingQty();

                /*
                 * 逻辑变更 2019/04/26 获取 assembleGroupActualId
                 */
                MtAssemblePoint mtAssemblePoint =
                                mtAssemblePointRepository.assemblePointPropertyGet(tenantId, dto.getAssemblePointId());
                if (mtAssemblePoint == null || StringUtils.isEmpty(mtAssemblePoint.getAssembleGroupId())) {
                    throw new MtException("MT_ASSEMBLE_0015", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0015", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
                }

                MtAssembleGroupActualVO1 groupActual = new MtAssembleGroupActualVO1();
                groupActual.setAssembleGroupId(mtAssemblePoint.getAssembleGroupId());
                List<String> assembleGroupActualIds = mtAssembleGroupActualRepository
                                .propertyLimitAssembleGroupActualQuery(tenantId, groupActual);
                if (CollectionUtils.isEmpty(assembleGroupActualIds)) {
                    throw new MtException("MT_ASSEMBLE_0015", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0015", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
                }

                // 获取数据唯一 （唯一性索引）
                oldAssemblePointActual.setAssembleGroupActualId(assembleGroupActualIds.get(0));

                oldAssemblePointActual.setFeedingSequence(dto.getFeedingSequence());
                oldAssemblePointActual.setQty(dto.getQty());
                if (null != dto.getTrxQty()) {
                    oldAssemblePointActual.setQty(new BigDecimal(oldAssemblePointActual.getQty().toString())
                                    .add(new BigDecimal(dto.getTrxQty().toString())).doubleValue());
                }
                oldAssemblePointActual.setFeedingQty(dto.getFeedingQty());
                if (null != dto.getTrxFeedingQty()) {
                    oldAssemblePointActual
                                    .setFeedingQty(new BigDecimal(oldAssemblePointActual.getFeedingQty().toString())
                                                    .add(new BigDecimal(dto.getTrxFeedingQty().toString()))
                                                    .doubleValue());
                }
                oldAssemblePointActual.setFeedingMaterialLotSequence(dto.getFeedingMaterialLotSequence());

                mtAssemblePointActual = new MtAssemblePointActual();
                mtAssemblePointActual.setTenantId(tenantId);
                mtAssemblePointActual.setAssemblePointActualId(oldAssemblePointActual.getAssmeblePointActualId());
                mtAssemblePointActual.setAssembleGroupActualId(oldAssemblePointActual.getAssembleGroupActualId());
                mtAssemblePointActual.setAssemblePointId(oldAssemblePointActual.getAssemblePointId());
                mtAssemblePointActual.setFeedingSequence(oldAssemblePointActual.getFeedingSequence());
                mtAssemblePointActual.setMaterialId(oldAssemblePointActual.getMaterialId());
                mtAssemblePointActual.setQty(oldAssemblePointActual.getQty());
                mtAssemblePointActual.setFeedingQty(oldAssemblePointActual.getFeedingQty());
                mtAssemblePointActual
                                .setFeedingMaterialLotSequence(oldAssemblePointActual.getFeedingMaterialLotSequence());
                mtAssemblePointActual.setMaterialLotId(oldAssemblePointActual.getMaterialLotId());

                if ("Y".equalsIgnoreCase(fullUpdate)) {
                    mtAssemblePointActual = (MtAssemblePointActual) ObjectFieldsHelper
                                    .setStringFieldsEmpty(mtAssemblePointActual);
                    self().updateByPrimaryKey(mtAssemblePointActual);
                    // 设置返回装配的id
                    mtAssemblePointActualVO7.setAssemblePointActualId(mtAssemblePointActual.getAssemblePointActualId());
                } else {
                    self().updateByPrimaryKeySelective(mtAssemblePointActual);
                    // 设置返回装配的id
                    mtAssemblePointActualVO7.setAssemblePointActualId(mtAssemblePointActual.getAssemblePointActualId());
                }
            } else {
                // 若获取到结果为空，向MT_ASSEMBLE_POINT_ACTUAL中插入一条数据
                mtAssemblePointActual = new MtAssemblePointActual();
                mtAssemblePointActual.setTenantId(tenantId);

                /*
                 * 逻辑变更 2019/04/26 获取 assembleGroupActualId
                 */
                MtAssemblePoint mtAssemblePoint = this.mtAssemblePointRepository.assemblePointPropertyGet(tenantId,
                                dto.getAssemblePointId());
                if (mtAssemblePoint == null || StringUtils.isEmpty(mtAssemblePoint.getAssembleGroupId())) {
                    throw new MtException("MT_ASSEMBLE_0015", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0015", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
                }

                MtAssembleGroupActualVO1 AssembleGroupCondition = new MtAssembleGroupActualVO1();
                AssembleGroupCondition.setAssembleGroupId(mtAssemblePoint.getAssembleGroupId());
                List<String> assmebleGroupActualIds = this.mtAssembleGroupActualRepository
                                .propertyLimitAssembleGroupActualQuery(tenantId, AssembleGroupCondition);
                if (CollectionUtils.isEmpty(assmebleGroupActualIds)) {
                    throw new MtException("MT_ASSEMBLE_0015", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0015", "ASSEMBLE", "【API:assemblePointActualUpdate】"));
                }

                // 获取数据唯一 （唯一性索引）
                mtAssemblePointActual.setAssembleGroupActualId(assmebleGroupActualIds.get(0));

                mtAssemblePointActual.setAssemblePointId(dto.getAssemblePointId());

                if (null != dto.getFeedingSequence()) {
                    mtAssemblePointActual.setFeedingSequence(dto.getFeedingSequence());
                } else {
                    MtAssemblePointActualVO6 condition = new MtAssemblePointActualVO6();
                    condition.setAssembleGroupActualId(mtAssemblePointActual.getAssembleGroupActualId());
                    mtAssemblePointActual.setFeedingSequence(assemblePointFeedingNextSequenceGet(tenantId, condition));
                }

                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    mtAssemblePointActual.setMaterialId(dto.getMaterialId());
                } else {
                    // 根据materialLotId获取materialId作为初始化目标值
                    mtAssemblePointActual.setMaterialId(mtMaterialLot.getMaterialId());
                }

                if (null != dto.getQty()) {
                    mtAssemblePointActual.setQty(dto.getQty());
                }
                if (null != dto.getTrxQty()) {
                    mtAssemblePointActual.setQty(dto.getTrxQty());
                }
                if (null == dto.getQty() && null == dto.getTrxQty() && null != dto.getMaterialLotId()) {
                    // 若值qty、trxQty均未输入，但materialLotId有输入，获取数量PrimaryUomQty作为更新目标值
                    if (matLotIdIsNotEmpty) {
                        mtAssemblePointActual.setQty(mtMaterialLot.getPrimaryUomQty());
                    } else {
                        mtAssemblePointActual.setQty(Double.valueOf(0.0D));
                    }
                }
                if (null == dto.getQty() && null == dto.getTrxQty() && null == dto.getMaterialLotId()) {
                    mtAssemblePointActual.setQty(Double.valueOf(0.0D));
                }

                if (null != dto.getFeedingQty()) {
                    mtAssemblePointActual.setFeedingQty(dto.getFeedingQty());
                }
                if (null != dto.getTrxFeedingQty()) {
                    mtAssemblePointActual.setFeedingQty(dto.getTrxFeedingQty());
                }
                if (null == dto.getFeedingQty() && null == dto.getTrxFeedingQty() && null != dto.getMaterialLotId()) {
                    // 若值feedingQty、trxfeedingQty均未输入，但materialLotId有输入，获取数量PrimaryUomQty作为更新目标值
                    if (matLotIdIsNotEmpty) {
                        // 逻辑变更materialLotLimitMaterialQtyGet获取数据
                        MtMaterialLotVO11 mtMaterialLotVO11 = new MtMaterialLotVO11();
                        mtMaterialLotVO11.setPrimaryFlag("Y");
                        mtMaterialLotVO11.setMaterialLotId(dto.getMaterialLotId());
                        MtMaterialLotVO4 mtMaterialLotVO4 = mtMaterialLotRepository
                                        .materialLotLimitMaterialQtyGet(tenantId, mtMaterialLotVO11);
                        if (mtMaterialLotVO4 != null) {
                            mtAssemblePointActual.setFeedingQty(mtMaterialLotVO4.getPrimaryUomQty());
                        }
                    } else {
                        mtAssemblePointActual.setFeedingQty(Double.valueOf(0.0D));
                    }
                }
                if (null == dto.getFeedingQty() && null == dto.getTrxFeedingQty() && null == dto.getMaterialLotId()) {
                    mtAssemblePointActual.setFeedingQty(mtAssemblePointActual.getQty());
                }

                if (null != dto.getFeedingMaterialLotSequence()) {
                    mtAssemblePointActual.setFeedingMaterialLotSequence(dto.getFeedingMaterialLotSequence());
                } else {
                    mtAssemblePointActual.setFeedingMaterialLotSequence(
                                    feedingLotNextSequenceGet(tenantId, dto.getAssemblePointId()));
                }
                if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
                    mtAssemblePointActual.setMaterialLotId(dto.getMaterialLotId());
                } else {
                    mtAssemblePointActual.setMaterialLotId("");
                }

                self().insertSelective(mtAssemblePointActual);
                // 设置返回装配的id
                mtAssemblePointActualVO7.setAssemblePointActualId(mtAssemblePointActual.getAssemblePointActualId());
            }
        }

        // 记录装配点实绩历史
        // 先查询更新&新增后的数据
        MtAssemblePointActual pointActual = new MtAssemblePointActual();
        pointActual.setTenantId(tenantId);
        pointActual.setAssemblePointActualId(mtAssemblePointActual.getAssemblePointActualId());
        mtAssemblePointActual = assemblePointActualMapper.selectOne(pointActual);
        MtAssemblePointActualHis mtAssemblePointActualHis = new MtAssemblePointActualHis();
        mtAssemblePointActualHis.setTenantId(tenantId);
        mtAssemblePointActualHis.setAssemblePointActualId(mtAssemblePointActual.getAssemblePointActualId());
        mtAssemblePointActualHis.setAssembleGroupActualId(mtAssemblePointActual.getAssembleGroupActualId());
        mtAssemblePointActualHis.setAssemblePointId(mtAssemblePointActual.getAssemblePointId());
        mtAssemblePointActualHis.setFeedingSequence(mtAssemblePointActual.getFeedingSequence());
        mtAssemblePointActualHis.setMaterialId(mtAssemblePointActual.getMaterialId());
        mtAssemblePointActualHis.setQty(mtAssemblePointActual.getQty());
        mtAssemblePointActualHis.setFeedingQty(mtAssemblePointActual.getFeedingQty());
        mtAssemblePointActualHis.setFeedingMaterialLotSequence(mtAssemblePointActual.getFeedingMaterialLotSequence());
        mtAssemblePointActualHis.setMaterialLotId(mtAssemblePointActual.getMaterialLotId());
        mtAssemblePointActualHis.setEventId(dto.getEventId());
        mtAssemblePointActualHis.setTrxQty(new BigDecimal(mtAssemblePointActual.getQty().toString())
                        .subtract(new BigDecimal(oldQty.toString())).doubleValue());
        mtAssemblePointActualHis.setTrxFeedingQty(new BigDecimal(mtAssemblePointActual.getFeedingQty().toString())
                        .subtract(new BigDecimal(oldFeedingQty.toString())).doubleValue());
        this.mtAssemblePointActualHisRepository.insertSelective(mtAssemblePointActualHis);
        // 记录返回的装配点历史记录id
        mtAssemblePointActualVO7.setAssemblePointActualHisId(mtAssemblePointActualHis.getAssemblePointActualHisId());
        // 记录装配组实绩历史
        MtAssembleGroupActualHisVO groupActualCondition = new MtAssembleGroupActualHisVO();
        groupActualCondition.setAssembleGroupActualId(mtAssemblePointActual.getAssembleGroupActualId());
        groupActualCondition.setEventId(dto.getEventId());
        List<MtAssembleGroupActualHisVO2> mtAssembleGroupActualHiz = this.mtAssembleGroupActualHisRepository
                        .assembleGroupActualHisQuery(tenantId, groupActualCondition);
        if (CollectionUtils.isEmpty(mtAssembleGroupActualHiz)) {
            MtAssembleGroupActualVO5 assembleGroupActual = new MtAssembleGroupActualVO5();
            assembleGroupActual.setAssembleGroupActualId(mtAssemblePointActual.getAssembleGroupActualId());
            assembleGroupActual.setEventId(dto.getEventId());
            this.mtAssembleGroupActualRepository.assembleGroupActualUpdate(tenantId, assembleGroupActual);
        }
        return mtAssemblePointActualVO7;
    }

    @Override
    public List<MtAssemblePointActualVO9> propertyLimitAssemblePointActualPropertyQuery(Long tenantId,
                                                                                        MtAssemblePointActualVO8 dto) {
        List<MtAssemblePointActualVO9> pointActualVO9List = assemblePointActualMapper.selectCondition(tenantId, dto);

        if (CollectionUtils.isEmpty(pointActualVO9List)) {
            return Collections.emptyList();
        }
        // 获取到的materialId列表
        List<String> materialIds = pointActualVO9List.stream().map(MtAssemblePointActualVO9::getMaterialId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtMaterialVO> mtMaterialVOMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(materialIds)) {
            List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
            // 获取物料编码和物料描述：
            if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                mtMaterialVOMap = mtMaterialVOS.stream().collect(Collectors.toMap(t -> t.getMaterialId(), t -> t));
            }
        }


        // 获取到的 assemblePointId列表
        List<String> assemblePointIds = pointActualVO9List.stream().map(MtAssemblePointActualVO9::getAssemblePointId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtAssemblePoint> assemblePointMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(assemblePointIds)) {
            List<MtAssemblePoint> pointList =
                            mtAssemblePointRepository.assemblePointPropertyBatchGet(tenantId, assemblePointIds);
            // 获取装配点编码和装配点描
            if (CollectionUtils.isNotEmpty(pointList)) {
                assemblePointMap = pointList.stream().collect(Collectors.toMap(t -> t.getAssemblePointId(), t -> t));
            }
        }

        for (MtAssemblePointActualVO9 actualVO9 : pointActualVO9List) {
            actualVO9.setMaterialCode(null == mtMaterialVOMap.get(actualVO9.getMaterialId()) ? null
                            : mtMaterialVOMap.get(actualVO9.getMaterialId()).getMaterialCode());
            actualVO9.setMaterialName(null == mtMaterialVOMap.get(actualVO9.getMaterialId()) ? null
                            : mtMaterialVOMap.get(actualVO9.getMaterialId()).getMaterialName());
            actualVO9.setAssemblePointCode(null == assemblePointMap.get(actualVO9.getAssemblePointId()) ? null
                            : assemblePointMap.get(actualVO9.getAssemblePointId()).getAssemblePointCode());
            actualVO9.setAssemblePointDescription(null == assemblePointMap.get(actualVO9.getAssemblePointId()) ? null
                            : assemblePointMap.get(actualVO9.getAssemblePointId()).getDescription());
        }
        return pointActualVO9List;
    }

}
