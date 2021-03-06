package tarzan.inventory.infra.repository.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.ruike.wms.infra.util.WmsCommonUtils;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.inventory.domain.entity.*;
import tarzan.inventory.domain.repository.*;
import tarzan.inventory.domain.trans.MtMaterialLotTransMapper;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.*;
import tarzan.method.domain.entity.MtAssemblePoint;
import tarzan.method.domain.repository.MtAssemblePointRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ???????????????
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
@Component
@Slf4j
public class MtMaterialLotRepositoryImpl extends BaseRepositoryImpl<MtMaterialLot> implements MtMaterialLotRepository {

    private static final Logger logger = LoggerFactory.getLogger(MtMaterialLotRepositoryImpl.class);

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;

    @Autowired
    private MtInvOnhandHoldRepository mtInvOnhandHoldRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtMaterialLotChangeHistoryRepository mtMaterialLotChangeHistoryRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtAssemblePointRepository mtAssemblePointRepository;

    @Autowired
    private MtNumrangeObjectRepository mtNumrangeObjectRepository;

    @Autowired
    private MtNumrangeObjectColumnRepository mtNumrangeObjectColumnRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private MtMaterialLotTransMapper mtMaterialLotTransMapper;

    @Autowired
    private MtEventTypeRepository mtEventTypeRepository;

    @Override
    public MtMaterialLot materialLotPropertyGet(Long tenantId, String materialLotId) {
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "???API:materialLotPropertyGet???"));
        }

        MtMaterialLot materialLot = new MtMaterialLot();
        materialLot.setTenantId(tenantId);
        materialLot.setMaterialLotId(materialLotId);
        return mtMaterialLotMapper.selectOne(materialLot);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLotConsume(Long tenantId, MtMaterialLotVO1 dto) {
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "???API:materialLotConsume???"));
        }
        if (dto.getTrxPrimaryUomQty() == null) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "trxPrimaryUomQty", "???API:materialLotConsume???"));
        }

        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, dto.getMaterialLotId());
        if (null == mtMaterialLot || !"Y".equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotConsume???"));
        }
        if ("Y".equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0072",
                                            "MATERIAL_LOT", dto.getMaterialLotId(), "???API:materialLotConsume???"));
        }

        MtMaterialVO2 mtMaterialVO2 = null;
        if (StringUtils.isNotEmpty(dto.getPrimaryUomId())) {
            mtMaterialVO2 = new MtMaterialVO2();
            mtMaterialVO2.setMaterialId(mtMaterialLot.getMaterialId());
            mtMaterialVO2.setPrimaryUomId(dto.getPrimaryUomId());
            this.mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
        }

        if (StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
            mtMaterialVO2 = new MtMaterialVO2();
            mtMaterialVO2.setMaterialId(mtMaterialLot.getMaterialId());
            mtMaterialVO2.setSecondaryUomId(dto.getSecondaryUomId());
            this.mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
        }

        if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId()) && null == dto.getTrxSecondaryUomQty()) {
            throw new MtException("MT_MATERIAL_LOT_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0017", "MATERIAL_LOT", "???API:materialLotConsume???"));
        }

        Double trxPrimaryUomQty = null;
        if (StringUtils.isEmpty(dto.getPrimaryUomId())) {
            trxPrimaryUomQty = dto.getTrxPrimaryUomQty();
        } else {
            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(dto.getPrimaryUomId());
            transferUomVO.setSourceValue(dto.getTrxPrimaryUomQty());
            transferUomVO.setTargetUomId(mtMaterialLot.getPrimaryUomId());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
            if (null != transferUomVO) {
                trxPrimaryUomQty = transferUomVO.getTargetValue();
            }
        }

        Double trxSecondaryUomQty = null;
        if (StringUtils.isEmpty(dto.getSecondaryUomId())) {
            trxSecondaryUomQty = dto.getTrxSecondaryUomQty();
        } else {
            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(dto.getSecondaryUomId());
            transferUomVO.setSourceValue(dto.getTrxSecondaryUomQty());
            transferUomVO.setTargetUomId(mtMaterialLot.getSecondaryUomId());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
            if (null != transferUomVO) {
                trxSecondaryUomQty = transferUomVO.getTargetValue();
            }
        }

        trxPrimaryUomQty = trxPrimaryUomQty == null ? Double.valueOf(0.0D) : trxPrimaryUomQty;

        String enableFlag = null;
        int result = BigDecimal.valueOf(trxPrimaryUomQty)
                        .compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
        if (result == 0) {
            enableFlag = "N";
        } else if (result > 0) {
            throw new MtException("MT_MATERIAL_LOT_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0018", "MATERIAL_LOT", "???API:materialLotConsume???"));
        }

        if ("N".equals(enableFlag)) {
            MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
            mtContLoadDtlVO5.setLoadObjectType("MATERIAL_LOT");
            mtContLoadDtlVO5.setLoadObjectId(dto.getMaterialLotId());
            mtContLoadDtlVO5.setTopLevelFlag("N");
            List<String> containerIds = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId,
                            mtContLoadDtlVO5);

            if (CollectionUtils.isNotEmpty(containerIds)) {
                for (String containerId : containerIds) {
                    if (StringUtils.isNotEmpty(containerId)) {
                        MtContainerVO25 mtContainerVO22 = new MtContainerVO25();
                        mtContainerVO22.setContainerId(containerId);
                        mtContainerVO22.setLoadObjectType("MATERIAL_LOT");
                        mtContainerVO22.setLoadObjectId(dto.getMaterialLotId());
                        mtContainerVO22.setWorkcellId(dto.getWorkcellId());
                        mtContainerVO22.setParentEventId(dto.getParentEventId());
                        mtContainerVO22.setEventRequestId(dto.getEventRequestId());
                        mtContainerVO22.setShiftDate(dto.getShiftDate());
                        mtContainerVO22.setShiftCode(dto.getShiftCode());
                        mtContainerRepository.containerUnload(tenantId, mtContainerVO22);
                    }
                }
            }
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();

        // modify by xujin 2020???2???21??? ???????????????????????? ??????ownertype????????????MATERIAL_LOT_CONSUME ??????????????? ?????????owertype
        String eventTypeCode = null;
        if (StringUtils.isEmpty(mtMaterialLot.getOwnerType())) {
            eventTypeCode = "MATERIAL_LOT_CONSUME";
        } else {
            eventTypeCode = "MATERIAL_LOT_" + mtMaterialLot.getOwnerType() + "_CONSUME";
        }

        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(mtMaterialLot.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO2.setTrxPrimaryUomQty(-trxPrimaryUomQty);
        if (null != trxSecondaryUomQty) {
            mtMaterialLotVO2.setTrxSecondaryUomQty(-trxSecondaryUomQty);
        }
        if ("N".equals(enableFlag)) {
            mtMaterialLotVO2.setEnableFlag("N");
        }
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setInstructionDocId(dto.getInstructionDocId());
        materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

        if (!"Y".equals(mtMaterialLot.getReservedFlag())) {
            MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
            updateOnhandVO.setSiteId(mtMaterialLot.getSiteId());
            updateOnhandVO.setMaterialId(mtMaterialLot.getMaterialId());
            updateOnhandVO.setLocatorId(mtMaterialLot.getLocatorId());
            updateOnhandVO.setLotCode(mtMaterialLot.getLot());
            updateOnhandVO.setOwnerId(mtMaterialLot.getOwnerId());
            updateOnhandVO.setOwnerType(mtMaterialLot.getOwnerType());

            MtMaterialVO1 mtMaterialVO1 =
                            this.mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
            if (null == mtMaterialVO1) {
                throw new MtException("MT_MATERIAL_LOT_0004",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0004",
                                                "MATERIAL_LOT", "?????????????????????", "???API:materialLotConsume???"));
            }

            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
            transferUomVO.setSourceValue(trxPrimaryUomQty);
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
            if (null != transferUomVO) {
                updateOnhandVO.setChangeQuantity(transferUomVO.getTargetValue());
            } else {
                updateOnhandVO.setChangeQuantity(0.0D);
            }
            updateOnhandVO.setEventId(eventId);
            this.mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
        } else {
            MtInvOnhandHoldVO7 onhandReleaseVO1 = new MtInvOnhandHoldVO7();
            onhandReleaseVO1.setSiteId(mtMaterialLot.getSiteId());
            onhandReleaseVO1.setMaterialId(mtMaterialLot.getMaterialId());
            onhandReleaseVO1.setLocatorId(mtMaterialLot.getLocatorId());
            onhandReleaseVO1.setLotCode(mtMaterialLot.getLot());

            MtMaterialVO1 mtMaterialVO1 =
                            this.mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
            if (null == mtMaterialVO1) {
                throw new MtException("MT_MATERIAL_LOT_0004",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0004",
                                                "MATERIAL_LOT", "?????????????????????", "???API:materialLotConsume???"));
            }

            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
            transferUomVO.setSourceValue(trxPrimaryUomQty);
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
            if (null != transferUomVO) {
                onhandReleaseVO1.setQuantity(transferUomVO.getTargetValue());
            } else {
                onhandReleaseVO1.setQuantity(0.0D);
            }
            onhandReleaseVO1.setHoldType("ORDER");
            onhandReleaseVO1.setOrderType(mtMaterialLot.getReservedObjectType());
            onhandReleaseVO1.setOrderId(mtMaterialLot.getReservedObjectId());
            onhandReleaseVO1.setEventRequestId(dto.getEventRequestId());
            onhandReleaseVO1.setShiftDate(dto.getShiftDate());
            onhandReleaseVO1.setShiftCode(dto.getShiftCode());
            onhandReleaseVO1.setOwnerId(mtMaterialLot.getOwnerId());
            onhandReleaseVO1.setOwnerType(mtMaterialLot.getOwnerType());
            mtInvOnhandHoldRepository.onhandReserveUseProcess(tenantId, onhandReleaseVO1);
        }
    }

    /**
     * sequenceLimitMaterialLotBatchConsume-?????????????????????
     *
     * @author chuang.yang
     * @date 2019/10/14
     * @param tenantId
     * @param dto
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sequenceLimitMaterialLotBatchConsume(Long tenantId, MtMaterialLotVO15 dto) {
        // 1. ?????????????????????
        if (CollectionUtils.isEmpty(dto.getMtMaterialLotSequenceList())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "{materialLotId,sequence}",
                                            "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        Optional<MtMaterialLotVO16> any = dto.getMtMaterialLotSequenceList().stream()
                        .filter(t -> StringUtils.isEmpty(t.getMaterialLotId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId",
                                            "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        if (StringUtils.isEmpty(dto.getAllConsume())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "allConsume",
                                            "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        if (!Arrays.asList("Y", "N").contains(dto.getAllConsume())) {
            throw new MtException("MT_MATERIAL_LOT_0080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0080", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        boolean isAllConsume = "Y".equals(dto.getAllConsume());

        if (!isAllConsume) {
            // ?????????????????????????????????
            any = dto.getMtMaterialLotSequenceList().stream().filter(t -> t.getSequence() == null).findAny();
            if (any.isPresent()) {
                throw new MtException("MT_MATERIAL_LOT_0081",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0081",
                                                "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
            }

            // ??????????????????
            Map<Long, List<MtMaterialLotVO16>> longListMap = dto.getMtMaterialLotSequenceList().stream()
                            .collect(Collectors.groupingBy(MtMaterialLotVO16::getSequence));
            for (Map.Entry<Long, List<MtMaterialLotVO16>> entry : longListMap.entrySet()) {
                if (entry.getValue().size() > 1) {
                    throw new MtException("MT_MATERIAL_LOT_0079",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0079",
                                                    "MATERIAL_LOT", "sequence",
                                                    "???API:sequenceLimitMaterialLotBatchConsume???"));
                }
            }

            // ???????????????????????????????????????????????????
            if (dto.getTrxPrimaryUomQty() == null) {
                throw new MtException("MT_MATERIAL_LOT_0081",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0081",
                                                "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
            }
        }

        // ???????????????????????????
        List<String> materialLotIds = dto.getMtMaterialLotSequenceList().stream()
                        .map(MtMaterialLotVO16::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> mtMaterialLotList = this.materialLotPropertyBatchGetForUpdate(tenantId, materialLotIds);

        List<String> materialIds = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialId).distinct()
                        .collect(Collectors.toList());
        if (materialIds.size() > 1) {
            throw new MtException("MT_MATERIAL_LOT_0082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0082", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        // ??????????????????
        mtMaterialLotList = mtMaterialLotList.stream().filter(t -> "Y".equals(t.getEnableFlag()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(mtMaterialLotList)) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        // ??????????????????????????????????????????
        List<MtMaterialLot> stocktakeMaterialLots = mtMaterialLotList.stream()
                        .filter(t -> "Y".equals(t.getStocktakeFlag())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(stocktakeMaterialLots)) {
            throw new MtException("MT_MATERIAL_LOT_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0072",
                                            "MATERIAL_LOT",
                                            stocktakeMaterialLots.stream().map(MtMaterialLot::getMaterialLotId)
                                                            .collect(Collectors.toList()).toString(),
                                            "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        // ?????????????????????????????????????????????????????????
        boolean isHasPrimaryUomId = StringUtils.isNotEmpty(dto.getPrimaryUomId());
        if (isHasPrimaryUomId) {
            MtMaterialVO2 mtMaterialVO2 = new MtMaterialVO2();
            mtMaterialVO2.setMaterialId(mtMaterialLotList.get(0).getMaterialId());
            mtMaterialVO2.setPrimaryUomId(dto.getPrimaryUomId());
            this.mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
        }

        boolean isHasSecondaryUomId = StringUtils.isNotEmpty(dto.getSecondaryUomId());
        if (isHasSecondaryUomId) {
            MtMaterialVO2 mtMaterialVO2 = new MtMaterialVO2();
            mtMaterialVO2.setMaterialId(mtMaterialLotList.get(0).getMaterialId());
            mtMaterialVO2.setSecondaryUomId(dto.getSecondaryUomId());
            this.mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
        }

        // ??????????????????
        Optional<MtMaterialLot> materialAny =
                        mtMaterialLotList.stream().filter(t -> StringUtils.isNotEmpty(t.getSecondaryUomId())).findAny();
        if (materialAny.isPresent() && dto.getTrxSecondaryUomQty() == null) {
            throw new MtException("MT_MATERIAL_LOT_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0017", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        // ?????????????????????
        Map<String, List<MtMaterialLot>> uomIdMap =
                        mtMaterialLotList.stream().collect(Collectors.groupingBy(MtMaterialLot::getPrimaryUomId));
        if (uomIdMap.size() > 1) {
            throw new MtException("MT_MATERIAL_LOT_0077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0077", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        uomIdMap = mtMaterialLotList.stream().collect(Collectors.groupingBy(MtMaterialLot::getSecondaryUomId));
        if (uomIdMap.size() > 1) {
            throw new MtException("MT_MATERIAL_LOT_0082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0082", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        // ?????????????????????
        MtMaterialVO1 mtMaterialVO1 =
                        mtMaterialRepository.materialUomGet(tenantId, mtMaterialLotList.get(0).getMaterialId());
        if (mtMaterialVO1 == null) {
            throw new MtException("MT_MATERIAL_LOT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0004", "?????????????????????", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        // ????????????
        BigDecimal sumPrimaryUomQty = mtMaterialLotList.stream()
                        .collect(CollectorsUtil.summingBigDecimal(t -> null == t.getPrimaryUomQty() ? BigDecimal.ZERO
                                        : BigDecimal.valueOf(t.getPrimaryUomQty())));

        // ????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_BATCH_CONSUME");
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        String materialLotBatchConsumeEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        eventCreateVO.setEventTypeCode("CONTAINER_MATERIAL_LOT_UNLOAD");
        String containerMaterialLotUnLoadEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ????????????
        List<String> sqlList = new ArrayList<>();
        Date now = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();

        List<String> materialLotCids =
                        customDbRepository.getNextKeys("mt_material_lot_cid_s", mtMaterialLotList.size());
        List<String> materialLotHisIds =
                        customDbRepository.getNextKeys("mt_material_lot_his_s", mtMaterialLotList.size());
        List<String> materialLotHisCids =
                        customDbRepository.getNextKeys("mt_material_lot_his_cid_s", mtMaterialLotList.size());

        // ???????????????????????????????????????
        List<MtMaterialLot> onhandMaterialLotList = new ArrayList<MtMaterialLot>();

        if (isAllConsume) {
            // ?????????
            // ???????????????
            int i = 0;

            // ???????????????????????????
            for (MtMaterialLotVO16 materialLotSequence : dto.getMtMaterialLotSequenceList()) {
                Optional<MtMaterialLot> materialLotOptional = mtMaterialLotList.stream()
                                .filter(t -> t.getMaterialLotId().equals(materialLotSequence.getMaterialLotId()))
                                .findFirst();
                if (!materialLotOptional.isPresent()) {
                    continue;
                }

                MtMaterialLot currentMaterialLot = new MtMaterialLot();
                BeanUtils.copyProperties(materialLotOptional.get(), currentMaterialLot);

                // ????????????????????????????????????????????????????????????
                onhandMaterialLotList.add(materialLotOptional.get());

                currentMaterialLot.setEnableFlag("N");
                currentMaterialLot.setUnloadTime(now);
                currentMaterialLot.setPrimaryUomQty(0.0D);
                currentMaterialLot.setSecondaryUomQty(0.0D);
                currentMaterialLot.setCurrentContainerId("");
                currentMaterialLot.setTopContainerId("");
                if (dto.getInstructionDocId() != null) {
                    currentMaterialLot.setInstructionDocId(dto.getInstructionDocId());
                }
                // ??????????????????
                currentMaterialLot.setLatestHisId(materialLotHisIds.get(i));
                currentMaterialLot.setCid(Long.valueOf(materialLotCids.get(i)));
                currentMaterialLot.setLastUpdatedBy(userId);
                currentMaterialLot.setLastUpdateDate(now);
                sqlList.addAll(customDbRepository.getUpdateSql(currentMaterialLot));

                MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                BeanUtils.copyProperties(currentMaterialLot, mtMaterialLotHis);
                mtMaterialLotHis.setMaterialLotHisId(materialLotHisIds.get(i));
                mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCids.get(i)));
                mtMaterialLotHis.setEventId(materialLotBatchConsumeEventId);
                mtMaterialLotHis.setTrxPrimaryQty(-materialLotOptional.get().getPrimaryUomQty());
                mtMaterialLotHis.setTrxSecondaryQty(materialLotOptional.get().getSecondaryUomQty() == null ? null
                                : -materialLotOptional.get().getSecondaryUomQty());
                mtMaterialLotHis.setCreatedBy(userId);
                mtMaterialLotHis.setCreationDate(now);
                sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));

                i++;
            }

            // ???????????????????????????????????????
            materialLotIds = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId)
                            .collect(Collectors.toList());

            List<String> containerAndLoadDetailSqlList = this.dealContainerAndLoadDetail(tenantId, userId, now,
                            materialLotIds, containerMaterialLotUnLoadEventId);
            if (CollectionUtils.isNotEmpty(containerAndLoadDetailSqlList)) {
                sqlList.addAll(containerAndLoadDetailSqlList);
            }

        } else {
            // ????????????
            // 2. ????????????????????????????????????????????????????????????????????????????????????
            // 2.1. ????????????????????????????????? trxPrimaryUomQty
            Double trxPrimaryUomQty = dto.getTrxPrimaryUomQty();
            if (isHasPrimaryUomId) {
                MtUomVO1 transferUomVO = new MtUomVO1();
                transferUomVO.setSourceUomId(dto.getPrimaryUomId());
                transferUomVO.setSourceValue(trxPrimaryUomQty);
                transferUomVO.setTargetUomId(mtMaterialLotList.get(0).getPrimaryUomId());
                transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                trxPrimaryUomQty = transferUomVO.getTargetValue();
            }

            // 3. ?????????????????????????????????????????????
            if (BigDecimal.valueOf(trxPrimaryUomQty).compareTo(sumPrimaryUomQty) > 0) {
                throw new MtException("MT_MATERIAL_LOT_0018",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0018",
                                                "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
            }

            // 3.1. ???????????????????????????????????????????????????
            List<MtMaterialLotVO16> materialLotSequenceList = dto.getMtMaterialLotSequenceList().stream()
                            .sorted(Comparator.comparing(MtMaterialLotVO16::getSequence)).collect(Collectors.toList());

            BigDecimal trxSumQty = BigDecimal.valueOf(trxPrimaryUomQty);
            BigDecimal sumQty = BigDecimal.ZERO;

            int i = 0;

            // ?????????????????????????????????????????????????????????????????????
            List<MtMaterialLot> doneMtMaterialLotList = new ArrayList<>();

            // ???????????????????????????
            for (MtMaterialLotVO16 materialLotSequence : materialLotSequenceList) {
                Optional<MtMaterialLot> materialLotOptional = mtMaterialLotList.stream()
                                .filter(t -> t.getMaterialLotId().equals(materialLotSequence.getMaterialLotId()))
                                .findFirst();
                if (!materialLotOptional.isPresent()) {
                    continue;
                }

                MtMaterialLot currentMaterialLot = new MtMaterialLot();
                BeanUtils.copyProperties(materialLotOptional.get(), currentMaterialLot);

                BigDecimal primaryUomQty = BigDecimal.valueOf(currentMaterialLot.getPrimaryUomQty());

                // ??????????????????
                BigDecimal tempSumQty = primaryUomQty.add(sumQty);

                // ?????????????????????????????????????????????????????????????????????
                if (tempSumQty.compareTo(trxSumQty) > 0) {

                    // ???????????????????????????????????????????????????????????????
                    BigDecimal updatePrimaryUomQty = tempSumQty.subtract(trxSumQty);

                    currentMaterialLot.setPrimaryUomQty(updatePrimaryUomQty.doubleValue());

                    // ???????????????????????????
                    BigDecimal currentPrimaryQtyTrxQty = trxSumQty.subtract(sumQty);
                    BigDecimal currentSecondQtyTrxQty = null;

                    // ??????????????????
                    if (StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
                        // ????????????????????????????????????????????????????????????
                        MtUomVO1 transferUomVO = new MtUomVO1();
                        transferUomVO.setSourceUomId(currentMaterialLot.getPrimaryUomId());
                        transferUomVO.setSourceValue(currentPrimaryQtyTrxQty.doubleValue());
                        transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
                        transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

                        // ????????????????????????????????????????????????????????????
                        MtUomVO3 materialTransferUomVO = new MtUomVO3();
                        materialTransferUomVO.setMaterialId(currentMaterialLot.getMaterialId());
                        materialTransferUomVO.setSourceUomId(mtMaterialVO1.getPrimaryUomId());
                        materialTransferUomVO.setSourceValue(transferUomVO.getTargetValue());
                        materialTransferUomVO = mtUomRepository.materialUomConversion(tenantId, materialTransferUomVO);

                        // ?????????????????????????????????????????????????????????????????????
                        transferUomVO.setSourceUomId(mtMaterialVO1.getSecondaryUomId());
                        transferUomVO.setSourceValue(materialTransferUomVO.getTargetValue());
                        transferUomVO.setTargetUomId(currentMaterialLot.getSecondaryUomId());
                        transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

                        currentSecondQtyTrxQty = BigDecimal.valueOf(transferUomVO.getTargetValue());
                        currentMaterialLot.setSecondaryUomQty(
                                        BigDecimal.valueOf(materialLotOptional.get().getSecondaryUomQty())
                                                        .subtract(currentSecondQtyTrxQty).doubleValue());
                    }

                    // ??????????????????
                    currentMaterialLot.setLatestHisId(materialLotHisIds.get(i));
                    currentMaterialLot.setUnloadTime(now);
                    currentMaterialLot.setCid(Long.valueOf(materialLotCids.get(i)));
                    currentMaterialLot.setLastUpdateDate(now);
                    currentMaterialLot.setLastUpdatedBy(userId);
                    if (dto.getInstructionDocId() != null) {
                        currentMaterialLot.setInstructionDocId(dto.getInstructionDocId());
                    }
                    sqlList.addAll(customDbRepository.getUpdateSql(currentMaterialLot));

                    MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                    BeanUtils.copyProperties(currentMaterialLot, mtMaterialLotHis);
                    mtMaterialLotHis.setMaterialLotHisId(materialLotHisIds.get(i));
                    mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCids.get(i)));
                    mtMaterialLotHis.setTrxPrimaryQty(-currentPrimaryQtyTrxQty.doubleValue());
                    mtMaterialLotHis.setTrxSecondaryQty(
                                    currentSecondQtyTrxQty == null ? null : -(currentSecondQtyTrxQty.doubleValue()));
                    mtMaterialLotHis.setEventId(materialLotBatchConsumeEventId);
                    mtMaterialLotHis.setCreatedBy(userId);
                    mtMaterialLotHis.setCreationDate(now);
                    sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));

                    // ????????????????????????????????????????????????????????????
                    currentMaterialLot.setPrimaryUomQty(trxSumQty.subtract(sumQty).doubleValue());
                    onhandMaterialLotList.add(currentMaterialLot);

                    break;
                } else {
                    // ????????????????????????????????????????????????????????????
                    onhandMaterialLotList.add(materialLotOptional.get());
                    doneMtMaterialLotList.add(materialLotOptional.get());

                    currentMaterialLot.setEnableFlag("N");
                    currentMaterialLot.setUnloadTime(now);
                    currentMaterialLot.setPrimaryUomQty(0.0D);
                    currentMaterialLot.setSecondaryUomQty(0.0D);
                    currentMaterialLot.setCurrentContainerId("");
                    currentMaterialLot.setTopContainerId("");
                    if (dto.getInstructionDocId() != null) {
                        currentMaterialLot.setInstructionDocId(dto.getInstructionDocId());
                    }
                    // ??????????????????Id
                    currentMaterialLot.setLatestHisId(materialLotHisIds.get(i));
                    currentMaterialLot.setCid(Long.valueOf(materialLotCids.get(i)));
                    currentMaterialLot.setLastUpdatedBy(userId);
                    currentMaterialLot.setLastUpdateDate(now);
                    sqlList.addAll(customDbRepository.getUpdateSql(currentMaterialLot));

                    MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                    BeanUtils.copyProperties(currentMaterialLot, mtMaterialLotHis);
                    mtMaterialLotHis.setMaterialLotHisId(materialLotHisIds.get(i));
                    mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCids.get(i)));
                    mtMaterialLotHis.setTrxPrimaryQty(-materialLotOptional.get().getPrimaryUomQty());
                    mtMaterialLotHis.setTrxSecondaryQty(-materialLotOptional.get().getSecondaryUomQty());
                    mtMaterialLotHis.setEventId(materialLotBatchConsumeEventId);
                    mtMaterialLotHis.setCreatedBy(userId);
                    mtMaterialLotHis.setCreationDate(now);
                    sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));
                }

                sumQty = tempSumQty;
                i++;
                if (tempSumQty.compareTo(trxSumQty) == 0) {
                    break;
                }
            }

            // ???????????????????????????????????????
            if (CollectionUtils.isNotEmpty(doneMtMaterialLotList)) {
                materialLotIds = doneMtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId)
                                .collect(Collectors.toList());

                List<String> containerAndLoadDetailSqlList = this.dealContainerAndLoadDetail(tenantId, userId, now,
                                materialLotIds, containerMaterialLotUnLoadEventId);
                if (CollectionUtils.isNotEmpty(containerAndLoadDetailSqlList)) {
                    sqlList.addAll(containerAndLoadDetailSqlList);
                }
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        // ???????????????
        // ???????????????????????????????????????????????????
        Map<ReservedMaterialLot, List<MtMaterialLot>> reservedMaterialLotsMap = onhandMaterialLotList.stream()
                        .filter(t -> "Y".equals(t.getReservedFlag()))
                        .collect(Collectors.groupingBy(b -> new ReservedMaterialLot(b.getSiteId(), b.getMaterialId(),
                                        b.getLocatorId(), b.getOwnerType(), b.getOwnerId(), b.getLot(),
                                        b.getReservedObjectType(), b.getReservedObjectId())));

        Map<UnReservedMaterialLot, List<MtMaterialLot>> unReservedMaterialLotsMap =
                        onhandMaterialLotList.stream().filter(t -> !"Y".equals(t.getReservedFlag()))
                                        .collect(Collectors.groupingBy(b -> new UnReservedMaterialLot(b.getSiteId(),
                                                        b.getMaterialId(), b.getLocatorId(), b.getOwnerType(),
                                                        b.getOwnerId(), b.getLot())));

        // ????????????????????????????????????
        for (Map.Entry<ReservedMaterialLot, List<MtMaterialLot>> entry : reservedMaterialLotsMap.entrySet()) {
            // ????????????????????????
            BigDecimal sumTrxPrimaryUomQty =
                            entry.getValue().stream()
                                            .collect(CollectorsUtil.summingBigDecimal(t -> null == t.getPrimaryUomQty()
                                                            ? BigDecimal.ZERO
                                                            : BigDecimal.valueOf(t.getPrimaryUomQty())));

            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(entry.getValue().get(0).getPrimaryUomId());
            transferUomVO.setSourceValue(sumTrxPrimaryUomQty.doubleValue());
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

            // ?????? ????????????????????????
            MtInvOnhandHoldVO7 onhandReleaseVO1 = new MtInvOnhandHoldVO7();
            onhandReleaseVO1.setSiteId(entry.getKey().getSiteId());
            onhandReleaseVO1.setMaterialId(entry.getKey().getMaterialId());
            onhandReleaseVO1.setLocatorId(entry.getKey().getLocatorId());
            onhandReleaseVO1.setLotCode(entry.getKey().getLot());
            onhandReleaseVO1.setQuantity(transferUomVO.getTargetValue());
            onhandReleaseVO1.setHoldType("ORDER");
            onhandReleaseVO1.setOrderType(entry.getKey().getReservedObjectType());
            onhandReleaseVO1.setOrderId(entry.getKey().getReservedObjectId());
            onhandReleaseVO1.setOwnerId(entry.getKey().getOwnerId());
            onhandReleaseVO1.setOwnerType(entry.getKey().getOwnerType());
            onhandReleaseVO1.setEventRequestId(dto.getEventRequestId());
            onhandReleaseVO1.setShiftCode(dto.getShiftCode());
            onhandReleaseVO1.setShiftDate(dto.getShiftDate());
            mtInvOnhandHoldRepository.onhandReserveUseProcess(tenantId, onhandReleaseVO1);
        }

        // ????????????????????????????????????
        for (Map.Entry<UnReservedMaterialLot, List<MtMaterialLot>> entry : unReservedMaterialLotsMap.entrySet()) {
            // ????????????????????????
            BigDecimal sumTrxPrimaryUomQty =
                            entry.getValue().stream()
                                            .collect(CollectorsUtil.summingBigDecimal(t -> null == t.getPrimaryUomQty()
                                                            ? BigDecimal.ZERO
                                                            : BigDecimal.valueOf(t.getPrimaryUomQty())));

            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(entry.getValue().get(0).getPrimaryUomId());
            transferUomVO.setSourceValue(sumTrxPrimaryUomQty.doubleValue());
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

            // ?????? ?????????
            MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
            updateOnhandVO.setSiteId(entry.getKey().getSiteId());
            updateOnhandVO.setMaterialId(entry.getKey().getMaterialId());
            updateOnhandVO.setLocatorId(entry.getKey().getLocatorId());
            updateOnhandVO.setLotCode(entry.getKey().getLot());
            updateOnhandVO.setChangeQuantity(transferUomVO.getTargetValue());
            updateOnhandVO.setEventId(materialLotBatchConsumeEventId);
            updateOnhandVO.setOwnerId(entry.getKey().getOwnerId());
            updateOnhandVO.setOwnerType(entry.getKey().getOwnerType());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
        }
    }

    /**
     * ???????????????????????? sequenceLimitMaterialLotBatchConsume
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param tenantId
     * @param userId
     * @param now
     * @param materialLotIds
     * @param eventId
     * @return java.util.List<java.lang.String>
     */
    public List<String> dealContainerAndLoadDetail(Long tenantId, Long userId, Date now, List<String> materialLotIds,
                    String eventId) {
        List<String> sqlList = new ArrayList<>();

        // ??????????????????????????????
        List<MtContainerLoadDetail> containerLoadDetailList = mtContainerLoadDetailRepository
                        .loadObjectLimitBatchGet(tenantId, "MATERIAL_LOT", materialLotIds);
        if (CollectionUtils.isNotEmpty(containerLoadDetailList)) {

            // ????????????????????????
            List<String> containerLoadDetailHisIds = customDbRepository.getNextKeys("mt_container_load_detail_his_s",
                            containerLoadDetailList.size());
            List<String> containerLoadDetailHisCids = customDbRepository
                            .getNextKeys("mt_container_load_detail_his_cid_s", containerLoadDetailList.size());

            // ????????????
            for (int j = 0; j < containerLoadDetailList.size(); j++) {
                MtContainerLoadDetail currentContainerLoadDetial = containerLoadDetailList.get(j);

                // ??????????????????
                MtContainerLoadDetailHis containerLoadDetailHis = new MtContainerLoadDetailHis();
                BeanUtils.copyProperties(currentContainerLoadDetial, containerLoadDetailHis);
                containerLoadDetailHis.setContainerLoadDetailHisId(containerLoadDetailHisIds.get(j));
                containerLoadDetailHis.setCid(Long.valueOf(containerLoadDetailHisCids.get(j)));
                containerLoadDetailHis.setEventId(eventId);
                sqlList.addAll(customDbRepository.getInsertSql(containerLoadDetailHis));

                // ????????????????????????
                sqlList.addAll(customDbRepository.getDeleteSql(currentContainerLoadDetial));
            }


            // ????????????
            // ????????????????????????
            List<String> containerIds = containerLoadDetailList.stream().map(MtContainerLoadDetail::getContainerId)
                            .distinct().collect(Collectors.toList());

            List<MtContainer> containerList = mtContainerRepository.containerPropertyBatchGet(tenantId, containerIds);

            if (CollectionUtils.isNotEmpty(containerList)) {
                List<String> containerCids = customDbRepository.getNextKeys("mt_container_cid_s", containerList.size());

                List<String> containerHisIds =
                                customDbRepository.getNextKeys("mt_container_his_s", containerList.size());
                List<String> containerHisCids =
                                customDbRepository.getNextKeys("mt_container_his_cid_s", containerList.size());

                // ????????????
                for (int m = 0; m < containerList.size(); m++) {
                    MtContainer currentContainer = containerList.get(m);

                    // ????????????????????????
                    currentContainer.setLastUnloadTime(now);
                    currentContainer.setCid(Long.valueOf(containerCids.get(m)));
                    currentContainer.setLastUpdateDate(now);
                    currentContainer.setLastUpdatedBy(userId);
                    // ??????????????????id
                    currentContainer.setLatestHisId(containerHisIds.get(m));
                    sqlList.addAll(customDbRepository.getUpdateSql(currentContainer));

                    // ????????????
                    MtContainerHis containerHis = new MtContainerHis();
                    BeanUtils.copyProperties(currentContainer, containerHis);
                    containerHis.setContainerHisId(containerHisIds.get(m));
                    containerHis.setCid(Long.valueOf(containerHisCids.get(m)));
                    containerHis.setEventId(eventId);
                    containerHis.setCreationDate(now);
                    containerHis.setCreatedBy(userId);
                    sqlList.addAll(customDbRepository.getInsertSql(containerHis));
                }
            }
        }

        return sqlList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtMaterialLotVO13 materialLotUpdate(Long tenantId, MtMaterialLotVO2 dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "???API:materialLotUpdate???"));
        }

        if (dto.getPrimaryUomQty() != null && dto.getTrxPrimaryUomQty() != null) {
            throw new MtException("MT_MATERIAL_LOT_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                                            "MATERIAL_LOT", "primaryUomQty???trxPrimaryUomQty",
                                            "???API:materialLotUpdate???"));
        }

        if (dto.getSecondaryUomQty() != null && dto.getTrxSecondaryUomQty() != null) {
            throw new MtException("MT_MATERIAL_LOT_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                                            "MATERIAL_LOT", "secondaryUomQty???trxSecondaryUomQty",
                                            "???API:materialLotUpdate???"));
        }

        if (StringUtils.isNotEmpty(dto.getOwnerType()) && StringUtils.isEmpty(dto.getOwnerId())) {
            throw new MtException("MT_MATERIAL_LOT_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0014", "MATERIAL_LOT", "ownerType", "ownerId", "???API:materialLotUpdate???"));
        }

        if (StringUtils.isNotEmpty(dto.getReservedObjectType()) && StringUtils.isEmpty(dto.getReservedObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "reservedObjectType", "reservedObjectId",
                                            "???API:materialLotUpdate???"));
        }

        MtMaterialLot mtMaterialLot = null;
        if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
            mtMaterialLot = mtMaterialLotMapper.selectOne(mtMaterialLot);
            if (mtMaterialLot == null) {
                throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotUpdate???"));
            }
        } else if (!dto.isOnlyInsert() // ????????????????????????????????????CODE?????? modify by yuchao.wang at 2020.10.20
                        && StringUtils.isEmpty(dto.getMaterialLotId())
                        && StringUtils.isNotEmpty(dto.getMaterialLotCode())) {
            mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
            mtMaterialLot = mtMaterialLotMapper.selectOne(mtMaterialLot);
        } else {
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "materialLotId???materialLotCode???materialId",
                                                "???API:materialLotUpdate???"));
            }

            if (StringUtils.isEmpty(dto.getSiteId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "materialLotId???materialLotCode???siteId",
                                                "???API:materialLotUpdate???"));
            }


            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "materialLotId???materialLotCode???enableFlag",
                                                "???API:materialLotUpdate???"));
            }


            if (StringUtils.isEmpty(dto.getQualityStatus())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "materialLotId???materialLotCode???qualityStatus",
                                                "???API:materialLotUpdate???"));
            }


            if (StringUtils.isEmpty(dto.getLocatorId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "materialLotId???materialLotCode???locatorId",
                                                "???API:materialLotUpdate???"));
            }

            if (StringUtils.isEmpty(dto.getCreateReason())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "materialLotId???materialLotCode???createReason",
                                                "???API:materialLotUpdate???"));
            }
        }

        MtMaterialVO2 mtMaterialVO2 = null;
        MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
        if (mtMaterialLot != null) {
            // V20201103 modify by penglin.sui ??????????????????????????????
            mtMaterialLot = mtMaterialLotMapper.selectForUpdate(tenantId, mtMaterialLot.getMaterialLotId());
            MtMaterialLot mtMaterialLotOld = new MtMaterialLot();
            BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotOld);
            mtMaterialLot.setSiteId(Strings.isNotBlank(dto.getSiteId()) ? dto.getSiteId() : mtMaterialLot.getSiteId());
            mtMaterialLot.setEnableFlag(dto.getEnableFlag());
            mtMaterialLot.setQualityStatus(Strings.isNotBlank(dto.getQualityStatus()) ? dto.getQualityStatus()
                            : mtMaterialLot.getQualityStatus());
            mtMaterialLot.setMaterialId(Strings.isNotBlank(dto.getMaterialId()) ? dto.getMaterialId()
                            : mtMaterialLot.getMaterialId());

            // ??????????????????????????????????????????????????????????????????????????????
            if (StringUtils.isNotEmpty(dto.getMaterialId()) || StringUtils.isNotEmpty(dto.getPrimaryUomId())) {
                mtMaterialVO2 = new MtMaterialVO2();

                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    mtMaterialVO2.setMaterialId(dto.getMaterialId());
                } else {
                    mtMaterialVO2.setMaterialId(mtMaterialLot.getMaterialId());
                }

                if (StringUtils.isNotEmpty(dto.getPrimaryUomId())) {
                    mtMaterialVO2.setPrimaryUomId(dto.getPrimaryUomId());
                } else {
                    mtMaterialVO2.setPrimaryUomId(mtMaterialLot.getPrimaryUomId());
                }
                this.mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
            }
            mtMaterialLot.setPrimaryUomId(dto.getPrimaryUomId());

            if (dto.getPrimaryUomQty() != null) {
                mtMaterialLot.setPrimaryUomQty(dto.getPrimaryUomQty());
            } else if (dto.getTrxPrimaryUomQty() != null) {
                mtMaterialLot.setPrimaryUomQty((BigDecimal
                                .valueOf(mtMaterialLotOld.getPrimaryUomQty() == null ? 0.0D
                                                : mtMaterialLotOld.getPrimaryUomQty())
                                .add(BigDecimal.valueOf(dto.getTrxPrimaryUomQty()))).doubleValue());
            }

            if (BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) < 0) {
                // ??????????????????????????????0??????.${1}
                throw new MtException("MT_MATERIAL_LOT_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0006",
                                                "MATERIAL_LOT", String.valueOf(mtMaterialLot.getPrimaryUomQty())));
            }

            if (StringUtils.isNotEmpty(dto.getMaterialId()) || StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
                /*
                 * ?????????????????????????????????
                 */
                String isDoubleUom = this.mtMaterialRepository.materialDualUomValidate(tenantId,
                                mtMaterialLot.getMaterialId());

                if ("Y".equals(isDoubleUom)) {
                    /*
                     * ?????????????????????????????????????????????????????????????????????????????????
                     */
                    mtMaterialVO2 = new MtMaterialVO2();
                    if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                        mtMaterialVO2.setMaterialId(dto.getMaterialId());
                    } else {
                        mtMaterialVO2.setMaterialId(mtMaterialLot.getMaterialId());
                    }

                    if (StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
                        mtMaterialVO2.setSecondaryUomId(dto.getSecondaryUomId());
                    } else if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId())) {
                        mtMaterialVO2.setSecondaryUomId(mtMaterialLot.getSecondaryUomId());
                    } else {
                        throw new MtException("MT_MATERIAL_LOT_0064",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0064", "MATERIAL_LOT",
                                                        "???API:materialLotUpdate???"));
                    }

                    mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);

                    if (StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
                        mtMaterialLot.setSecondaryUomId(dto.getSecondaryUomId());
                    }
                } else {
                    mtMaterialLot.setSecondaryUomId("");
                }
            } else {
                mtMaterialLot.setSecondaryUomId(null);
            }

            if (dto.getSecondaryUomQty() != null) {
                mtMaterialLot.setSecondaryUomQty(dto.getSecondaryUomQty());
            } else {
                if (dto.getTrxSecondaryUomQty() != null) {
                    mtMaterialLot.setSecondaryUomQty((BigDecimal
                                    .valueOf(mtMaterialLotOld.getSecondaryUomQty() == null ? 0.0D
                                                    : mtMaterialLotOld.getSecondaryUomQty())
                                    .add(BigDecimal.valueOf(dto.getTrxSecondaryUomQty()))).doubleValue());
                } else {
                    // ????????????????????????????????????????????????????????????
                    mtMaterialLot.setSecondaryUomQty(null);
                }
            }
            mtMaterialLot.setLocatorId(dto.getLocatorId());
            mtMaterialLot.setAssemblePointId(dto.getAssemblePointId());

            if (dto.getLoadTime() != null) {
                mtMaterialLot.setLoadTime(dto.getLoadTime());
            } else {
                if (!"Y".equals(mtMaterialLotOld.getEnableFlag()) && "Y".equals(mtMaterialLot.getEnableFlag())) {
                    mtMaterialLot.setLoadTime(new Date());
                } else if ("Y".equals(mtMaterialLotOld.getEnableFlag()) && "Y".equals(mtMaterialLot.getEnableFlag())
                                && mtMaterialLot.getPrimaryUomQty() != null) {
                    if (mtMaterialLotOld.getPrimaryUomQty() == null
                                    || BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).compareTo(
                                                    BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty())) > 0) {
                        mtMaterialLot.setLoadTime(new Date());
                    } else if (BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())
                                    .compareTo(BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty())) == 0
                                    && mtMaterialLot.getSecondaryUomQty() != null
                                    && (mtMaterialLotOld.getSecondaryUomQty() == null || BigDecimal
                                                    .valueOf(mtMaterialLot.getSecondaryUomQty())
                                                    .compareTo(BigDecimal.valueOf(
                                                                    mtMaterialLotOld.getSecondaryUomQty())) > 0)) {
                        mtMaterialLot.setLoadTime(new Date());
                    }
                }
            }


            if (dto.getUnloadTime() != null) {
                mtMaterialLot.setUnloadTime(dto.getUnloadTime());
            } else {
                if ("Y".equals(mtMaterialLotOld.getEnableFlag()) && !"Y".equals(mtMaterialLot.getEnableFlag())) {
                    mtMaterialLot.setUnloadTime(new Date());
                } else if ("Y".equals(mtMaterialLotOld.getEnableFlag()) && "Y".equals(mtMaterialLot.getEnableFlag())
                                && mtMaterialLot.getPrimaryUomQty() != null) {
                    if (mtMaterialLotOld.getPrimaryUomQty() == null
                                    || BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).compareTo(
                                                    BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty())) < 0) {
                        mtMaterialLot.setUnloadTime(new Date());
                    } else if (BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())
                                    .compareTo(BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty())) == 0
                                    && mtMaterialLot.getSecondaryUomQty() != null
                                    && (mtMaterialLotOld.getSecondaryUomQty() == null || BigDecimal
                                                    .valueOf(mtMaterialLot.getSecondaryUomQty())
                                                    .compareTo(BigDecimal.valueOf(
                                                                    mtMaterialLotOld.getSecondaryUomQty())) < 0)) {
                        mtMaterialLot.setUnloadTime(new Date());
                    }
                }
            }

            mtMaterialLot.setOwnerType(dto.getOwnerType());
            mtMaterialLot.setOwnerId(dto.getOwnerId());
            mtMaterialLot.setLot(dto.getLot());
            mtMaterialLot.setOvenNumber(dto.getOvenNumber());
            mtMaterialLot.setSupplierId(dto.getSupplierId());
            mtMaterialLot.setSupplierSiteId(dto.getSupplierSiteId());
            mtMaterialLot.setCustomerId(dto.getCustomerId());
            mtMaterialLot.setCustomerSiteId(dto.getCustomerSiteId());
            mtMaterialLot.setReservedFlag(dto.getReservedFlag());
            mtMaterialLot.setReservedObjectType(dto.getReservedObjectType());
            mtMaterialLot.setReservedObjectId(dto.getReservedObjectId());
            mtMaterialLot.setCreateReason(dto.getCreateReason());
            mtMaterialLot.setIdentification(dto.getIdentification());
            mtMaterialLot.setEoId(dto.getEoId());
            mtMaterialLot.setInLocatorTime(dto.getInLocatorTime());
            mtMaterialLot.setFreezeFlag(dto.getFreezeFlag());
            mtMaterialLot.setStocktakeFlag(dto.getStocktakeFlag());
            mtMaterialLot.setInSiteTime(dto.getInSiteTime());
            mtMaterialLot.setCurrentContainerId(dto.getCurrentContainerId());
            if (StringUtils.isNotEmpty(dto.getTopContainerId())) {
                mtMaterialLot.setTopContainerId(dto.getTopContainerId());
            } else if (null != dto.getCurrentContainerId()) {
                if ("".equals(dto.getCurrentContainerId())) {
                    mtMaterialLot.setTopContainerId("");
                } else {
                    MtContainer mtContainer = new MtContainer();
                    mtContainer.setTenantId(tenantId);
                    mtContainer.setContainerId(dto.getCurrentContainerId());
                    mtContainer = mtContainerRepository.selectOne(mtContainer);
                    if (null != mtContainer) {
                        mtMaterialLot.setTopContainerId(StringUtils.isEmpty(mtContainer.getTopContainerId())
                                        ? dto.getCurrentContainerId()
                                        : mtContainer.getTopContainerId());
                    } else {
                        mtMaterialLot.setTopContainerId(dto.getCurrentContainerId());
                    }
                }
            }
            mtMaterialLot.setInstructionDocId(dto.getInstructionDocId());
            mtMaterialLot.setTenantId(tenantId);

            if ("Y".equalsIgnoreCase(fullUpdate)) {
                mtMaterialLot = (MtMaterialLot) ObjectFieldsHelper.setStringFieldsEmpty(mtMaterialLot);
                self().updateByPrimaryKey(mtMaterialLot);
            } else {
                self().updateByPrimaryKeySelective(mtMaterialLot);
            }


            mtMaterialLotHis.setTrxPrimaryQty((BigDecimal
                            .valueOf(mtMaterialLot.getPrimaryUomQty() == null ? 0.0D : mtMaterialLot.getPrimaryUomQty())
                            .subtract(BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty() == null ? 0.0D
                                            : mtMaterialLotOld.getPrimaryUomQty()))).doubleValue());
            mtMaterialLotHis.setTrxSecondaryQty((BigDecimal
                            .valueOf(mtMaterialLot.getSecondaryUomQty() == null ? 0.0D
                                            : mtMaterialLot.getSecondaryUomQty())
                            .subtract(BigDecimal.valueOf(mtMaterialLotOld.getSecondaryUomQty() == null ? 0.0D
                                            : mtMaterialLotOld.getSecondaryUomQty()))).doubleValue());
        } else {
            mtMaterialLot = new MtMaterialLot();

            String nextCode = "";
            if (StringUtils.isNotEmpty(dto.getMaterialLotCode())) {
                nextCode = dto.getMaterialLotCode();
            } else {
                MtMaterialLotVO26 mtMaterialLotVO26 = new MtMaterialLotVO26();
                Map<String, String> materilaLotPropertyList = new HashMap<>(0);
                // siteCode
                if (StringUtils.isNotEmpty(dto.getSiteId())) {
                    MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
                    if (null != mtModSite) {
                        materilaLotPropertyList.put("siteCode", mtModSite.getSiteCode());
                    }
                }

                // currentContainerCode
                if (StringUtils.isNotEmpty(dto.getCurrentContainerId())) {
                    MtContainer mtContainer =
                                    mtContainerRepository.containerPropertyGet(tenantId, dto.getCurrentContainerId());
                    if (null != mtContainer) {
                        materilaLotPropertyList.put("currentContainerCode", mtContainer.getContainerCode());
                    }
                }


                // materialCode
                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    MtMaterialVO materialVO = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
                    if (null != materialVO) {
                        materilaLotPropertyList.put("materialCode", materialVO.getMaterialCode());
                    }
                }


                // ownerType
                materilaLotPropertyList.put("ownerType", dto.getOwnerType());
                // businessType
                materilaLotPropertyList.put("businessType", dto.getBusinessType());
                // lot
                materilaLotPropertyList.put("lot", dto.getLot());

                logger.debug("materilaLotPropertyList=" + materilaLotPropertyList);

                mtMaterialLotVO26.setSiteId(dto.getSiteId());
                mtMaterialLotVO26.setOutsideNum(dto.getOutsideNum());
                mtMaterialLotVO26.setMaterilaLotPropertyList(materilaLotPropertyList);
                mtMaterialLotVO26.setIncomingValueList(dto.getIncomingValueList());
                MtNumrangeVO5 mtNumrangeVO5 = materialLotNextCodeGet(tenantId, mtMaterialLotVO26);
                if (mtNumrangeVO5 != null) {
                    nextCode = mtNumrangeVO5.getNumber();
                }
            }

            mtMaterialLot.setMaterialLotCode(nextCode);
            mtMaterialLot.setSiteId(dto.getSiteId());
            mtMaterialLot.setEnableFlag(dto.getEnableFlag());
            mtMaterialLot.setQualityStatus(dto.getQualityStatus());
            mtMaterialLot.setMaterialId(dto.getMaterialId());

            if (StringUtils.isEmpty(dto.getPrimaryUomId())) {
                MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, dto.getMaterialId());
                if (mtMaterialVO1 == null) {
                    throw new MtException("MT_MATERIAL_LOT_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0016", "MATERIAL_LOT", "???API:materialLotUpdate???"));
                }
                mtMaterialLot.setPrimaryUomId(mtMaterialVO1.getPrimaryUomId());
            } else {
                mtMaterialVO2 = new MtMaterialVO2();
                mtMaterialVO2.setPrimaryUomId(dto.getPrimaryUomId());
                mtMaterialVO2.setMaterialId(dto.getMaterialId());
                mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
                mtMaterialLot.setPrimaryUomId(dto.getPrimaryUomId());
            }


            if (dto.getPrimaryUomQty() != null) {
                mtMaterialLot.setPrimaryUomQty(dto.getPrimaryUomQty());
            } else if (dto.getTrxPrimaryUomQty() != null) {
                mtMaterialLot.setPrimaryUomQty(dto.getTrxPrimaryUomQty());
            } else {
                mtMaterialLot.setPrimaryUomQty(Double.valueOf(0.0D));
            }

            if (StringUtils.isEmpty(dto.getSecondaryUomId())) {
                MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, dto.getMaterialId());
                if (null == mtMaterialVO1) {
                    throw new MtException("MT_MATERIAL_LOT_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0016", "MATERIAL_LOT", "???API:materialLotUpdate???"));
                }
                mtMaterialLot.setSecondaryUomId(mtMaterialVO1.getSecondaryUomId());
            } else {
                mtMaterialVO2 = new MtMaterialVO2();
                mtMaterialVO2.setSecondaryUomId(dto.getSecondaryUomId());
                mtMaterialVO2.setMaterialId(dto.getMaterialId());
                mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
                mtMaterialLot.setSecondaryUomId(dto.getSecondaryUomId());
            }

            if (dto.getSecondaryUomQty() != null) {
                mtMaterialLot.setSecondaryUomQty(dto.getSecondaryUomQty());
            } else if (dto.getTrxSecondaryUomQty() != null) {
                mtMaterialLot.setSecondaryUomQty(dto.getTrxSecondaryUomQty());
            }

            mtMaterialLot.setLocatorId(dto.getLocatorId());
            mtMaterialLot.setAssemblePointId(dto.getAssemblePointId());

            if (dto.getLoadTime() != null) {
                mtMaterialLot.setLoadTime(dto.getLoadTime());
            } else if ("Y".equals(dto.getEnableFlag()) && (dto.getPrimaryUomQty() != null
                            && BigDecimal.valueOf(dto.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) != 0)
                            || (dto.getSecondaryUomQty() != null && BigDecimal.valueOf(dto.getSecondaryUomQty())
                                            .compareTo(BigDecimal.ZERO) != 0)) {
                mtMaterialLot.setLoadTime(new Date());
            }

            if (dto.getUnloadTime() != null) {
                mtMaterialLot.setUnloadTime(dto.getUnloadTime());
            }

            mtMaterialLot.setOwnerType(dto.getOwnerType());
            mtMaterialLot.setOwnerId(dto.getOwnerId());

            if (null == dto.getLot()) {
                String nextLot = materialLotNextLotGet(tenantId);
                mtMaterialLot.setLot(nextLot);
            } else {
                mtMaterialLot.setLot(dto.getLot());
            }

            mtMaterialLot.setOvenNumber(dto.getOvenNumber());
            mtMaterialLot.setSupplierId(dto.getSupplierId());
            mtMaterialLot.setSupplierSiteId(dto.getSupplierSiteId());
            mtMaterialLot.setCustomerId(dto.getCustomerId());
            mtMaterialLot.setCustomerSiteId(mtMaterialLot.getCustomerSiteId());
            mtMaterialLot.setReservedFlag(dto.getReservedFlag());
            mtMaterialLot.setReservedObjectType(dto.getReservedObjectType());
            mtMaterialLot.setReservedObjectId(dto.getReservedObjectId());
            mtMaterialLot.setCreateReason(dto.getCreateReason());
            // add 2019-11-13
            mtMaterialLot.setCustomerSiteId(dto.getCustomerSiteId());

            if (StringUtils.isEmpty(dto.getIdentification())) {
                mtMaterialLot.setIdentification(mtMaterialLot.getMaterialLotCode());
            } else {
                mtMaterialLot.setIdentification(dto.getIdentification());

                // update by chuang.yang 2019-09-17
                // ?????????????????????
                MtMaterialLot verify = new MtMaterialLot();
                verify.setTenantId(tenantId);
                verify.setIdentification(dto.getIdentification());
                List<MtMaterialLot> mtMaterialLots = mtMaterialLotMapper.select(verify);
                if (CollectionUtils.isNotEmpty(mtMaterialLots)) {
                    throw new MtException("MT_MATERIAL_LOT_0075",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0075",
                                                    "MATERIAL_LOT", "MT_MATERIAL_LOT", "IDENTIFICATION",
                                                    "???API:materialLotUpdate???"));
                }
            }

            mtMaterialLot.setEoId(dto.getEoId());
            mtMaterialLot.setInLocatorTime(dto.getInLocatorTime());
            mtMaterialLot.setFreezeFlag(dto.getFreezeFlag());
            mtMaterialLot.setStocktakeFlag(dto.getStocktakeFlag());
            mtMaterialLot.setInSiteTime(dto.getInSiteTime());
            mtMaterialLot.setCurrentContainerId(dto.getCurrentContainerId());
            if (StringUtils.isNotEmpty(dto.getTopContainerId())) {
                mtMaterialLot.setTopContainerId(dto.getTopContainerId());
            } else if (null != dto.getCurrentContainerId()) {
                if ("".equals(dto.getCurrentContainerId())) {
                    mtMaterialLot.setTopContainerId("");
                } else {
                    MtContainer mtContainer = new MtContainer();
                    mtContainer.setTenantId(tenantId);
                    mtContainer.setContainerId(dto.getCurrentContainerId());
                    mtContainer = mtContainerRepository.selectOne(mtContainer);
                    if (null != mtContainer) {
                        mtMaterialLot.setTopContainerId(StringUtils.isEmpty(mtContainer.getTopContainerId())
                                        ? dto.getCurrentContainerId()
                                        : mtContainer.getTopContainerId());
                    } else {
                        mtMaterialLot.setTopContainerId(dto.getCurrentContainerId());
                    }
                }
            }
            mtMaterialLot.setInstructionDocId(dto.getInstructionDocId());
            mtMaterialLot.setTenantId(tenantId);
            self().insertSelective(mtMaterialLot);

            mtMaterialLotHis.setTrxPrimaryQty(mtMaterialLot.getPrimaryUomQty());
            mtMaterialLotHis.setTrxSecondaryQty(mtMaterialLot.getSecondaryUomQty());
        }

        // ????????????
        // ????????????
        MtMaterialLot lot = new MtMaterialLot();
        lot.setTenantId(tenantId);
        lot.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLot = mtMaterialLotMapper.selectOne(lot);
        BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotHis);
        mtMaterialLotHis.setEventId(dto.getEventId());
        mtMaterialLotHis.setTenantId(tenantId);
        mtMaterialLotHisRepository.insertSelective(mtMaterialLotHis);
        // ????????????????????????
        mtMaterialLot.setLatestHisId(mtMaterialLotHis.getMaterialLotHisId());
        self().updateByPrimaryKeySelective(mtMaterialLot);

        MtMaterialLotVO13 result = new MtMaterialLotVO13();
        result.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        result.setMaterialLotHisId(mtMaterialLotHis.getMaterialLotHisId());

        return result;
    }

    /**
     * materialLotBatchUpdate-???????????????&?????????????????????
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param tenantId
     * @param materialLotList
     * @param eventId
     * @return java.util.List<hmes.material_lot.view.MtMaterialLotVO19>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtMaterialLotVO19> materialLotBatchUpdate(Long tenantId, List<MtMaterialLotVO20> materialLotList,
                    String eventId, String fullUpdate) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "???API:materialLotBatchUpdate???"));
        }
        if (CollectionUtils.isEmpty(materialLotList)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLots", "???API:materialLotBatchUpdate???"));
        }

        this.checkUpdateData(tenantId, materialLotList);

        // 2. ????????????????????????????????????????????????????????????
        // ?????????????????????materialLotId?????????
        List<MtMaterialLotVO20> existMaterialLotInputList = materialLotList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getMaterialLotId())).collect(Collectors.toList());

        // ????????????????????????materialLotId?????????
        List<MtMaterialLotVO20> unExistMaterialLotInputList = materialLotList.stream()
                        .filter(t -> StringUtils.isEmpty(t.getMaterialLotId())).collect(Collectors.toList());

        // ????????????
        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        List<MtMaterialLotVO19> resultList = new ArrayList<>();

        // ??????????????????
        if (CollectionUtils.isNotEmpty(existMaterialLotInputList)) {
            List<String> existMaterialLotIds = existMaterialLotInputList.stream()
                            .map(MtMaterialLotVO20::getMaterialLotId).collect(Collectors.toList());

            // ???????????????????????????????????????
            // V20210421 modify by penglin.sui for hui.ma ??????????????????????????????
            // List<MtMaterialLot> existMaterialLotList = this.materialLotPropertyBatchGet(tenantId,
            // existMaterialLotIds);
            List<MtMaterialLot> existMaterialLotList =
                            this.materialLotPropertyBatchGetForUpdate(tenantId, existMaterialLotIds);
            if (existMaterialLotIds.size() != existMaterialLotList.size()) {
                throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotBatchUpdate???"));
            }

            // ?????????????????????
            List<String> materialLotCidS =
                            customDbRepository.getNextKeys("mt_material_lot_cid_s", existMaterialLotList.size());
            List<String> materialLotHisIdS =
                            customDbRepository.getNextKeys("mt_material_lot_his_s", existMaterialLotList.size());
            List<String> materialLotHisCidS =
                            customDbRepository.getNextKeys("mt_material_lot_his_cid_s", existMaterialLotList.size());

            for (int i = 0; i < existMaterialLotList.size(); i++) {
                MtMaterialLot existMaterialLot = existMaterialLotList.get(i);

                // ???????????????????????????????????????????????????
                MtMaterialLotVO20 currentInputMaterialLot = existMaterialLotInputList.stream()
                                .filter(t -> t.getMaterialLotId().equals(existMaterialLot.getMaterialLotId()))
                                .findFirst().get();

                // ?????????????????????
                BigDecimal oldPrimaryUomQty = BigDecimal.valueOf(existMaterialLot.getPrimaryUomQty());
                BigDecimal oldSecondaryUomQty = BigDecimal.ZERO;
                if (existMaterialLot.getSecondaryUomQty() != null) {
                    oldSecondaryUomQty = BigDecimal.valueOf(existMaterialLot.getSecondaryUomQty());
                }
                String oldEnableFlag = existMaterialLot.getEnableFlag();
                // ?????????????????????
                MtMaterialLot updateMaterialLotData =
                                this.convertUpdateData(tenantId, existMaterialLot, currentInputMaterialLot);

                updateMaterialLotData.setCid(Long.valueOf(materialLotCidS.get(i)));
                updateMaterialLotData.setLastUpdateDate(now);
                updateMaterialLotData.setLastUpdatedBy(userId);
                // ??????mt_material_lot_his?????????????????????????????????ID materialLotHisId???
                // ?????????mt_material_lot??????????????????????????????LATEST_HIS_ID????????????????????????materialLotHisId
                updateMaterialLotData.setLatestHisId(materialLotHisIdS.get(i));

                // ??????????????????
                if ("Y".equalsIgnoreCase(fullUpdate)) {
                    updateMaterialLotData =
                                    (MtMaterialLot) ObjectFieldsHelper.setStringFieldsEmpty(updateMaterialLotData);
                    sqlList.addAll(customDbRepository.getUpdateSql(updateMaterialLotData));
                } else {
                    sqlList.addAll(customDbRepository.getUpdateSql(updateMaterialLotData));
                }

                // ????????????
                MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                BeanUtils.copyProperties(updateMaterialLotData, mtMaterialLotHis);
                mtMaterialLotHis.setEventId(eventId);
                mtMaterialLotHis.setMaterialLotHisId(materialLotHisIdS.get(i));
                mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCidS.get(i)));
                mtMaterialLotHis.setTrxPrimaryQty(BigDecimal.valueOf(updateMaterialLotData.getPrimaryUomQty())
                                .subtract(oldPrimaryUomQty).doubleValue());
                mtMaterialLotHis.setTrxSecondaryQty((BigDecimal
                                .valueOf(updateMaterialLotData.getSecondaryUomQty() == null ? 0.0D
                                                : updateMaterialLotData.getSecondaryUomQty())
                                .subtract(oldSecondaryUomQty).doubleValue()));
                mtMaterialLotHis.setCreationDate(now);
                mtMaterialLotHis.setCreatedBy(userId);
                sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));

                MtMaterialLotVO19 result = new MtMaterialLotVO19();
                result.setMaterialLotId(mtMaterialLotHis.getMaterialLotId());

                // ???????????????????????????????????????????????????????????????????????????????????????????????????
                if (MtBaseConstants.YES.equalsIgnoreCase(currentInputMaterialLot.getEnableFlag())
                                && MtBaseConstants.NO.equalsIgnoreCase(oldEnableFlag)) {
                    result.setTrxPrimaryUomQty(mtMaterialLotHis.getPrimaryUomQty());
                } else {
                    result.setTrxPrimaryUomQty(mtMaterialLotHis.getTrxPrimaryQty());
                }
                resultList.add(result);
            }
        }

        // ??????????????????
        if (CollectionUtils.isNotEmpty(unExistMaterialLotInputList)) {
            // ??????????????????
            this.checkInsertData(tenantId, unExistMaterialLotInputList);

            // ?????????????????????
            List<String> materialLotIdS =
                            customDbRepository.getNextKeys("mt_material_lot_s", unExistMaterialLotInputList.size());
            List<String> materialLotCidS =
                            customDbRepository.getNextKeys("mt_material_lot_cid_s", unExistMaterialLotInputList.size());
            List<String> materialLotHisIdS =
                            customDbRepository.getNextKeys("mt_material_lot_his_s", unExistMaterialLotInputList.size());
            List<String> materialLotHisCidS = customDbRepository.getNextKeys("mt_material_lot_his_cid_s",
                            unExistMaterialLotInputList.size());

            for (int i = 0; i < unExistMaterialLotInputList.size(); i++) {
                MtMaterialLotVO20 currentInputMaterialLot = unExistMaterialLotInputList.get(i);

                // ????????????????????????
                MtMaterialLot mtMaterialLot = convertInsertData(tenantId, currentInputMaterialLot);
                mtMaterialLot.setMaterialLotId(materialLotIdS.get(i));
                mtMaterialLot.setCid(Long.valueOf(materialLotCidS.get(i)));
                mtMaterialLot.setCreatedBy(userId);
                mtMaterialLot.setCreationDate(now);
                mtMaterialLot.setLastUpdatedBy(userId);
                mtMaterialLot.setLastUpdateDate(now);

                // ??????mt_material_lot_his?????????????????????????????????ID materialLotHisId???
                // ?????????mt_material_lot??????????????????????????????LATEST_HIS_ID????????????????????????materialLotHisId
                mtMaterialLot.setLatestHisId(materialLotHisIdS.get(i));

                sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLot));

                // ????????????
                MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotHis);
                mtMaterialLotHis.setEventId(eventId);
                mtMaterialLotHis.setMaterialLotHisId(materialLotHisIdS.get(i));
                mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCidS.get(i)));
                mtMaterialLotHis.setTrxPrimaryQty(mtMaterialLot.getPrimaryUomQty());
                mtMaterialLotHis.setTrxSecondaryQty(mtMaterialLot.getSecondaryUomQty());
                sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));

                MtMaterialLotVO19 result = new MtMaterialLotVO19();
                result.setMaterialLotId(mtMaterialLotHis.getMaterialLotId());

                result.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty());
                resultList.add(result);
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtMaterialLotVO19> materialLotBatchUpdatePrecompile(Long tenantId,
                    List<MtMaterialLotVO20> materialLotList, String eventId) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "???API:materialLotBatchUpdate???"));
        }
        if (CollectionUtils.isEmpty(materialLotList)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLots", "???API:materialLotBatchUpdate???"));
        }

        long startDate = System.currentTimeMillis();
        this.checkUpdateData(tenantId, materialLotList);
        long endDate = System.currentTimeMillis();
        log.info("===========>????????????????????? checkUpdateData??????{}??????", endDate - startDate);

        // 2. ????????????????????????????????????????????????????????????
        // ?????????????????????materialLotId?????????
        List<MtMaterialLotVO20> existMaterialLotInputList = materialLotList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getMaterialLotId())).collect(Collectors.toList());

        // ????????????????????????materialLotId?????????
        List<MtMaterialLotVO20> unExistMaterialLotInputList = materialLotList.stream()
                        .filter(t -> StringUtils.isEmpty(t.getMaterialLotId())).collect(Collectors.toList());

        // ????????????
        List<MtMaterialLot> updateList = new ArrayList<>();
        List<MtMaterialLotHis> hisList = new ArrayList<>();
        List<MtMaterialLot> insertList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        long endDate2 = System.currentTimeMillis();
        log.info("===========>????????????????????? ????????????????????????????????????????????????{}??????", endDate2 - endDate);

        List<MtMaterialLotVO19> resultList = new ArrayList<>();

        // ??????????????????
        if (CollectionUtils.isNotEmpty(existMaterialLotInputList)) {
            List<String> existMaterialLotIds = existMaterialLotInputList.stream()
                            .map(MtMaterialLotVO20::getMaterialLotId).collect(Collectors.toList());

            // ??????????????????????????????????????? ?????????3000????????????
            // V20210421 modify by penglin.sui for hui.ma ??????????????????????????????
            // List<MtMaterialLot> existMaterialLotList = this.materialLotPropertyBatchGet(tenantId,
            // existMaterialLotIds);
            List<MtMaterialLot> existMaterialLotList = new ArrayList<>();
            List<List<String>> splitMaterialLotIdList = WmsCommonUtils.splitSqlList(existMaterialLotIds, 3000);
            for (List<String> splitMaterialLotId : splitMaterialLotIdList) {
                existMaterialLotList.addAll(this.materialLotPropertyBatchGetForUpdate(tenantId, splitMaterialLotId));
            }
            if (existMaterialLotIds.size() != existMaterialLotList.size()) {
                throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotBatchUpdate???"));
            }

            long endDate3 = System.currentTimeMillis();
            log.info("===========>????????????????????? ?????????????????????????????????????????????{}??????", endDate3 - endDate2);

            // ?????????????????????
            List<String> materialLotCidS =
                            customDbRepository.getNextKeys("mt_material_lot_cid_s", existMaterialLotList.size());
            List<String> materialLotHisIdS =
                            customDbRepository.getNextKeys("mt_material_lot_his_s", existMaterialLotList.size());
            List<String> materialLotHisCidS =
                            customDbRepository.getNextKeys("mt_material_lot_his_cid_s", existMaterialLotList.size());

            long endDate4 = System.currentTimeMillis();
            log.info("===========>????????????????????? ????????????{}??????????????????{}??????", existMaterialLotList.size() * 3, endDate4 - endDate3);

            for (int i = 0; i < existMaterialLotList.size(); i++) {
                long startDate6 = System.currentTimeMillis();
                MtMaterialLot existMaterialLot = existMaterialLotList.get(i);

                // ???????????????????????????????????????????????????
                MtMaterialLotVO20 currentInputMaterialLot = existMaterialLotInputList.stream()
                                .filter(t -> t.getMaterialLotId().equals(existMaterialLot.getMaterialLotId()))
                                .findFirst().get();
                long endDate6 = System.currentTimeMillis();
                log.info("===========>????????????????????? ????????????{}????????????{}?????????????????? ??????{}??????", i, existMaterialLot.getMaterialLotId(),
                                endDate6 - startDate6);

                // ?????????????????????
                BigDecimal oldPrimaryUomQty = BigDecimal.valueOf(existMaterialLot.getPrimaryUomQty());
                BigDecimal oldSecondaryUomQty = BigDecimal.ZERO;
                if (existMaterialLot.getSecondaryUomQty() != null) {
                    oldSecondaryUomQty = BigDecimal.valueOf(existMaterialLot.getSecondaryUomQty());
                }

                // ?????????????????????
                MtMaterialLot updateMaterialLotData =
                                this.convertUpdateData(tenantId, existMaterialLot, currentInputMaterialLot);
                long endDate7 = System.currentTimeMillis();
                log.info("===========>????????????????????? ????????????{}????????????{}??????convertUpdateData ??????{}??????", i,
                                existMaterialLot.getMaterialLotId(), endDate7 - endDate6);

                updateMaterialLotData.setCid(Long.valueOf(materialLotCidS.get(i)));
                updateMaterialLotData.setLastUpdateDate(now);
                updateMaterialLotData.setLastUpdatedBy(userId);
                // ??????mt_material_lot_his?????????????????????????????????ID materialLotHisId???
                // ?????????mt_material_lot??????????????????????????????LATEST_HIS_ID????????????????????????materialLotHisId
                updateMaterialLotData.setLatestHisId(materialLotHisIdS.get(i));

                // ???????????? ?????????????????????????????????????????????????????????????????????????????????
                updateMaterialLotData = (MtMaterialLot) ObjectFieldsHelper.setStringFieldsEmpty(updateMaterialLotData);
                updateList.add(updateMaterialLotData);
                long endDate8 = System.currentTimeMillis();
                log.info("===========>????????????????????? ????????????{}????????????{}???????????????????????? ??????{}??????", i, existMaterialLot.getMaterialLotId(),
                                endDate8 - endDate7);

                // ????????????
                MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                BeanUtils.copyProperties(updateMaterialLotData, mtMaterialLotHis);
                mtMaterialLotHis.setEventId(eventId);
                mtMaterialLotHis.setMaterialLotHisId(materialLotHisIdS.get(i));
                mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCidS.get(i)));
                mtMaterialLotHis.setTrxPrimaryQty(BigDecimal.valueOf(updateMaterialLotData.getPrimaryUomQty())
                                .subtract(oldPrimaryUomQty).doubleValue());
                mtMaterialLotHis.setTrxSecondaryQty((BigDecimal
                                .valueOf(updateMaterialLotData.getSecondaryUomQty() == null ? 0.0D
                                                : updateMaterialLotData.getSecondaryUomQty())
                                .subtract(oldSecondaryUomQty).doubleValue()));
                mtMaterialLotHis.setCreationDate(now);
                mtMaterialLotHis.setCreatedBy(userId);
                hisList.add(mtMaterialLotHis);
                long endDate9 = System.currentTimeMillis();
                log.info("===========>????????????????????? ????????????{}????????????{}?????????????????? ??????{}??????", i, existMaterialLot.getMaterialLotId(),
                                endDate9 - endDate8);

                MtMaterialLotVO19 result = new MtMaterialLotVO19();
                result.setMaterialLotId(mtMaterialLotHis.getMaterialLotId());
                resultList.add(result);
                long endDate10 = System.currentTimeMillis();
                log.info("===========>????????????????????? ????????????{}????????????{}??????{}??????", i, existMaterialLot.getMaterialLotId(),
                                endDate10 - endDate9);
            }
            long endDate5 = System.currentTimeMillis();
            log.info("===========>????????????????????? ????????????{}????????????????????????{}??????", existMaterialLotList.size(), endDate5 - endDate4);
        }

        // ??????????????????
        if (CollectionUtils.isNotEmpty(unExistMaterialLotInputList)) {
            // ??????????????????
            this.checkInsertData(tenantId, unExistMaterialLotInputList);

            // ?????????????????????
            List<String> materialLotIdS =
                            customDbRepository.getNextKeys("mt_material_lot_s", unExistMaterialLotInputList.size());
            List<String> materialLotCidS =
                            customDbRepository.getNextKeys("mt_material_lot_cid_s", unExistMaterialLotInputList.size());
            List<String> materialLotHisIdS =
                            customDbRepository.getNextKeys("mt_material_lot_his_s", unExistMaterialLotInputList.size());
            List<String> materialLotHisCidS = customDbRepository.getNextKeys("mt_material_lot_his_cid_s",
                            unExistMaterialLotInputList.size());

            for (int i = 0; i < unExistMaterialLotInputList.size(); i++) {
                MtMaterialLotVO20 currentInputMaterialLot = unExistMaterialLotInputList.get(i);

                // ????????????????????????
                MtMaterialLot mtMaterialLot = convertInsertData(tenantId, currentInputMaterialLot);
                mtMaterialLot.setMaterialLotId(materialLotIdS.get(i));
                mtMaterialLot.setCid(Long.valueOf(materialLotCidS.get(i)));
                mtMaterialLot.setCreatedBy(userId);
                mtMaterialLot.setCreationDate(now);
                mtMaterialLot.setLastUpdatedBy(userId);
                mtMaterialLot.setLastUpdateDate(now);

                // ??????mt_material_lot_his?????????????????????????????????ID materialLotHisId???
                // ?????????mt_material_lot??????????????????????????????LATEST_HIS_ID????????????????????????materialLotHisId
                mtMaterialLot.setLatestHisId(materialLotHisIdS.get(i));

                insertList.add(mtMaterialLot);

                // ????????????
                MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotHis);
                mtMaterialLotHis.setEventId(eventId);
                mtMaterialLotHis.setMaterialLotHisId(materialLotHisIdS.get(i));
                mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCidS.get(i)));
                mtMaterialLotHis.setTrxPrimaryQty(mtMaterialLot.getPrimaryUomQty());
                mtMaterialLotHis.setTrxSecondaryQty(mtMaterialLot.getSecondaryUomQty());
                hisList.add(mtMaterialLotHis);

                MtMaterialLotVO19 result = new MtMaterialLotVO19();
                result.setMaterialLotId(mtMaterialLotHis.getMaterialLotId());
                resultList.add(result);
            }
        }

        // ????????????
        long startDate12 = System.currentTimeMillis();
        if (CollectionUtils.isNotEmpty(updateList)) {
            customDbRepository.batchUpdateTarzan(updateList, 5000);
        }
        long endDate12 = System.currentTimeMillis();
        log.info("===========>????????????????????? ???????????????{}????????????????????????{}??????", updateList.size(), endDate12 - startDate12);
        if (CollectionUtils.isNotEmpty(insertList)) {
            customDbRepository.batchInsertTarzan(insertList, 5000);
        }
        long endDate13 = System.currentTimeMillis();
        log.info("===========>????????????????????? ???????????????{}????????????????????????{}??????", insertList.size(), endDate13 - endDate12);
        if (CollectionUtils.isNotEmpty(hisList)) {
            customDbRepository.batchInsertTarzan(hisList, 5000);
        }
        long endDate14 = System.currentTimeMillis();
        log.info("===========>????????????????????? ???????????????{}?????????????????????{}??????", hisList.size(), endDate14 - endDate13);
        return resultList;
    }

    /**
     * ????????????-??????????????????
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param tenantId
     * @param materialLotList
     * @return void
     */
    public void checkUpdateData(Long tenantId, List<MtMaterialLotVO20> materialLotList) {
        Optional<MtMaterialLotVO20> any = materialLotList.stream()
                        .filter(t -> t.getPrimaryUomQty() != null && t.getTrxPrimaryUomQty() != null).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                                            "MATERIAL_LOT", "primaryUomQty???trxPrimaryUomQty",
                                            "???API:materialLotBatchUpdate???"));
        }

        any = materialLotList.stream().filter(t -> t.getSecondaryUomQty() != null && t.getTrxSecondaryUomQty() != null)
                        .findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                                            "MATERIAL_LOT", "secondaryUomQty???trxSecondaryUomQty",
                                            "???API:materialLotBatchUpdate???"));
        }

        any = materialLotList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getOwnerType()) && StringUtils.isEmpty(t.getOwnerId()))
                        .findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "ownerType", "ownerId", "???API:materialLotBatchUpdate???"));
        }

        any = materialLotList.stream().filter(t -> StringUtils.isNotEmpty(t.getReservedObjectType())
                        && StringUtils.isEmpty(t.getReservedObjectId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "reservedObjectType", "reservedObjectId",
                                            "???API:materialLotBatchUpdate???"));
        }

        // ?????????????????????
        List<String> materialLotIds = materialLotList.stream().map(MtMaterialLotVO20::getMaterialLotId)
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());

        List<String> materialLotCodes = materialLotList.stream().map(MtMaterialLotVO20::getMaterialLotCode).distinct()
                        .collect(Collectors.toList());

        List<String> identifications = materialLotList.stream().map(MtMaterialLotVO20::getIdentification).distinct()
                        .collect(Collectors.toList());

        int countByMaterialLotCodes = 0;
        int countByIdentifications = 0;
        // ?????????????????????
        if (CollectionUtils.isEmpty(materialLotIds)) {
            if (CollectionUtils.isNotEmpty(materialLotCodes)) {
                SecurityTokenHelper.close();
                countByMaterialLotCodes = mtMaterialLotMapper.selectCountByCondition(Condition
                                .builder(MtMaterialLot.class)
                                .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                                                .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodes))
                                .build());
            }

            if (CollectionUtils.isNotEmpty(identifications)) {
                SecurityTokenHelper.close();
                countByIdentifications = mtMaterialLotMapper.selectCountByCondition(Condition
                                .builder(MtMaterialLot.class)
                                .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                                                .andIn(MtMaterialLot.FIELD_IDENTIFICATION, identifications))
                                .build());
            }
        } else {
            // ??????????????????
            if (CollectionUtils.isNotEmpty(materialLotCodes)) {
                SecurityTokenHelper.close();
                countByMaterialLotCodes = mtMaterialLotMapper.selectCountByCondition(Condition
                                .builder(MtMaterialLot.class)
                                .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                                                .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodes)
                                                .andNotIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, materialLotIds))
                                .build());
            }

            if (CollectionUtils.isNotEmpty(identifications)) {
                SecurityTokenHelper.close();
                countByIdentifications = mtMaterialLotMapper.selectCountByCondition(Condition
                                .builder(MtMaterialLot.class)
                                .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                                                .andIn(MtMaterialLot.FIELD_IDENTIFICATION, identifications)
                                                .andNotIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, materialLotIds))
                                .build());
            }
        }

        if (countByMaterialLotCodes > 0) {
            throw new MtException("MT_MATERIAL_LOT_0075",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0075",
                                            "MATERIAL_LOT", "mt_material_lot", "MATERIAL_LOT_CODE",
                                            "???API:materialLotBatchUpdate???"));
        }

        if (countByIdentifications > 0) {
            throw new MtException("MT_MATERIAL_LOT_0075",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0075",
                                            "MATERIAL_LOT", "mt_material_lot", "IDENTIFICATION",
                                            "???API:materialLotBatchUpdate???"));
        }
    }

    /**
     * ????????????-??????????????????
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param unExistMaterialLotInputList
     * @return void
     */
    public void checkInsertData(Long tenantId, List<MtMaterialLotVO20> unExistMaterialLotInputList) {
        Optional<MtMaterialLotVO20> any = unExistMaterialLotInputList.stream()
                        .filter(t -> StringUtils.isEmpty(t.getMaterialLotCode())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotCode", "???API:materialLotBatchUpdate???"));
        }

        any = unExistMaterialLotInputList.stream().filter(t -> StringUtils.isEmpty(t.getPrimaryUomId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "primaryUomId ", "???API:materialLotBatchUpdate???"));
        }

        any = unExistMaterialLotInputList.stream().filter(t -> StringUtils.isEmpty(t.getMaterialId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialId ", "???API:materialLotBatchUpdate???"));
        }

        any = unExistMaterialLotInputList.stream().filter(t -> StringUtils.isEmpty(t.getSiteId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "siteId ", "???API:materialLotBatchUpdate???"));
        }

        any = unExistMaterialLotInputList.stream().filter(t -> StringUtils.isEmpty(t.getEnableFlag())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "enableFlag ", "???API:materialLotBatchUpdate???"));
        }

        any = unExistMaterialLotInputList.stream().filter(t -> StringUtils.isEmpty(t.getQualityStatus())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "qualityStatus ", "???API:materialLotBatchUpdate???"));
        }

        any = unExistMaterialLotInputList.stream().filter(t -> StringUtils.isEmpty(t.getLocatorId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "locatorId ", "???API:materialLotBatchUpdate???"));
        }

        any = unExistMaterialLotInputList.stream().filter(t -> StringUtils.isEmpty(t.getCreateReason())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "createReason ", "???API:materialLotBatchUpdate???"));
        }
    }

    /**
     * ????????????-??????????????????
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param existMaterialLot
     * @param currentInputMaterialLot
     * @return hmes.material_lot.dto.MtMaterialLot
     */
    public MtMaterialLot convertUpdateData(Long tenantId, MtMaterialLot existMaterialLot,
                    MtMaterialLotVO20 currentInputMaterialLot) {
        if (currentInputMaterialLot.getSiteId() != null) {
            existMaterialLot.setSiteId(currentInputMaterialLot.getSiteId());
        }

        if (currentInputMaterialLot.getEnableFlag() != null) {
            existMaterialLot.setEnableFlag(currentInputMaterialLot.getEnableFlag());
        }

        if (currentInputMaterialLot.getQualityStatus() != null) {
            existMaterialLot.setQualityStatus(currentInputMaterialLot.getQualityStatus());
        }

        if (currentInputMaterialLot.getMaterialId() != null) {
            existMaterialLot.setMaterialId(currentInputMaterialLot.getMaterialId());
        }

        if (currentInputMaterialLot.getPrimaryUomId() != null) {
            existMaterialLot.setPrimaryUomId(currentInputMaterialLot.getPrimaryUomId());
        }

        if (currentInputMaterialLot.getPrimaryUomQty() != null) {
            existMaterialLot.setPrimaryUomQty(currentInputMaterialLot.getPrimaryUomQty());
        } else if (currentInputMaterialLot.getTrxPrimaryUomQty() != null) {
            existMaterialLot.setPrimaryUomQty((BigDecimal.valueOf(existMaterialLot.getPrimaryUomQty())
                            .add(BigDecimal.valueOf(currentInputMaterialLot.getTrxPrimaryUomQty()))).doubleValue());
        }

        if (currentInputMaterialLot.getSecondaryUomId() != null) {
            existMaterialLot.setSecondaryUomId(currentInputMaterialLot.getSecondaryUomId());
        }

        if (currentInputMaterialLot.getSecondaryUomQty() != null) {
            existMaterialLot.setSecondaryUomQty(currentInputMaterialLot.getSecondaryUomQty());
        } else if (currentInputMaterialLot.getTrxSecondaryUomQty() != null) {
            existMaterialLot.setSecondaryUomQty((BigDecimal
                            .valueOf(existMaterialLot.getSecondaryUomQty() == null ? 0.0D
                                            : existMaterialLot.getSecondaryUomQty())
                            .add(BigDecimal.valueOf(currentInputMaterialLot.getTrxSecondaryUomQty()))).doubleValue());
        }

        if (currentInputMaterialLot.getLocatorId() != null) {
            existMaterialLot.setLocatorId(currentInputMaterialLot.getLocatorId());
        }

        if (currentInputMaterialLot.getAssemblePointId() != null) {
            existMaterialLot.setAssemblePointId(currentInputMaterialLot.getAssemblePointId());
        }

        if (currentInputMaterialLot.getLoadTime() != null) {
            existMaterialLot.setLoadTime(currentInputMaterialLot.getLoadTime());
        }

        if (currentInputMaterialLot.getUnloadTime() != null) {
            existMaterialLot.setUnloadTime(currentInputMaterialLot.getUnloadTime());
        }

        if (currentInputMaterialLot.getOwnerType() != null) {
            existMaterialLot.setOwnerType(currentInputMaterialLot.getOwnerType());
        }

        if (currentInputMaterialLot.getOwnerId() != null) {
            existMaterialLot.setOwnerId(currentInputMaterialLot.getOwnerId());
        }

        if (currentInputMaterialLot.getLot() != null) {
            existMaterialLot.setLot(currentInputMaterialLot.getLot());
        }

        if (currentInputMaterialLot.getOvenNumber() != null) {
            existMaterialLot.setOvenNumber(currentInputMaterialLot.getOvenNumber());
        }

        if (currentInputMaterialLot.getSupplierId() != null) {
            existMaterialLot.setSupplierId(currentInputMaterialLot.getSupplierId());
        }

        if (currentInputMaterialLot.getSupplierSiteId() != null) {
            existMaterialLot.setSupplierSiteId(currentInputMaterialLot.getSupplierId());
        }

        if (currentInputMaterialLot.getCustomerId() != null) {
            existMaterialLot.setCustomerId(currentInputMaterialLot.getCustomerId());
        }

        if (currentInputMaterialLot.getCustomerSiteId() != null) {
            existMaterialLot.setCustomerSiteId(currentInputMaterialLot.getCustomerSiteId());
        }

        if (currentInputMaterialLot.getReservedFlag() != null) {
            existMaterialLot.setReservedFlag(currentInputMaterialLot.getReservedFlag());
        }

        if (currentInputMaterialLot.getReservedObjectType() != null) {
            existMaterialLot.setReservedObjectType(currentInputMaterialLot.getReservedObjectType());
        }

        if (currentInputMaterialLot.getReservedObjectId() != null) {
            existMaterialLot.setReservedObjectId(currentInputMaterialLot.getReservedObjectId());
        }

        if (currentInputMaterialLot.getCreateReason() != null) {
            existMaterialLot.setCreateReason(currentInputMaterialLot.getCreateReason());
        }

        if (currentInputMaterialLot.getIdentification() != null) {
            existMaterialLot.setIdentification(currentInputMaterialLot.getIdentification());
        }

        if (currentInputMaterialLot.getEoId() != null) {
            existMaterialLot.setEoId(currentInputMaterialLot.getEoId());
        }

        if (currentInputMaterialLot.getInLocatorTime() != null) {
            existMaterialLot.setInLocatorTime(currentInputMaterialLot.getInLocatorTime());
        }

        if (currentInputMaterialLot.getFreezeFlag() != null) {
            existMaterialLot.setFreezeFlag(currentInputMaterialLot.getFreezeFlag());
        }

        if (currentInputMaterialLot.getStocktakeFlag() != null) {
            existMaterialLot.setStocktakeFlag(currentInputMaterialLot.getStocktakeFlag());
        }

        // add by peng.yuan 2019-11-14
        if (null != currentInputMaterialLot.getInSiteTime()) {
            existMaterialLot.setInSiteTime(currentInputMaterialLot.getInSiteTime());
        }

        if (null != currentInputMaterialLot.getCurrentContainerId()) {
            existMaterialLot.setCurrentContainerId(currentInputMaterialLot.getCurrentContainerId());
        }

        if (null != currentInputMaterialLot.getTopContainerId()) {
            existMaterialLot.setTopContainerId(currentInputMaterialLot.getTopContainerId());
        } else {
            if ("".equals(currentInputMaterialLot.getCurrentContainerId())) {
                existMaterialLot.setTopContainerId("");
            } else if (StringUtils.isNotEmpty(currentInputMaterialLot.getCurrentContainerId())) {
                MtContainer container = new MtContainer();
                container.setTenantId(tenantId);
                container.setContainerId(currentInputMaterialLot.getCurrentContainerId());
                MtContainer mtContainer = mtContainerRepository.selectOne(container);
                if (mtContainer == null || StringUtils.isEmpty(mtContainer.getTopContainerId())) {
                    existMaterialLot.setTopContainerId(currentInputMaterialLot.getCurrentContainerId());
                } else {
                    existMaterialLot.setTopContainerId(mtContainer.getTopContainerId());
                }
            }
        }

        if (null != currentInputMaterialLot.getInstructionDocId()) {
            existMaterialLot.setInstructionDocId(currentInputMaterialLot.getInstructionDocId());
        }
        return existMaterialLot;
    }

    /**
     * ????????????-??????????????????
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param currentInputMaterialLot
     * @return hmes.material_lot.dto.MtMaterialLot
     */
    public MtMaterialLot convertInsertData(Long tenantId, MtMaterialLotVO20 currentInputMaterialLot) {
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLot.setMaterialLotCode(currentInputMaterialLot.getMaterialLotCode());
        mtMaterialLot.setSiteId(currentInputMaterialLot.getSiteId());
        mtMaterialLot.setEnableFlag(currentInputMaterialLot.getEnableFlag());
        mtMaterialLot.setQualityStatus(currentInputMaterialLot.getQualityStatus());
        mtMaterialLot.setMaterialId(currentInputMaterialLot.getMaterialId());
        mtMaterialLot.setPrimaryUomId(currentInputMaterialLot.getPrimaryUomId());

        if (currentInputMaterialLot.getPrimaryUomQty() != null) {
            mtMaterialLot.setPrimaryUomQty(currentInputMaterialLot.getPrimaryUomQty());
        } else if (currentInputMaterialLot.getTrxPrimaryUomQty() != null) {
            mtMaterialLot.setPrimaryUomQty(currentInputMaterialLot.getTrxPrimaryUomQty());
        } else {
            mtMaterialLot.setPrimaryUomQty(Double.valueOf(0.0D));
        }

        mtMaterialLot.setSecondaryUomId(currentInputMaterialLot.getSecondaryUomId());

        if (currentInputMaterialLot.getSecondaryUomQty() != null) {
            mtMaterialLot.setSecondaryUomQty(currentInputMaterialLot.getSecondaryUomQty());
        } else if (currentInputMaterialLot.getTrxSecondaryUomQty() != null) {
            mtMaterialLot.setSecondaryUomQty(currentInputMaterialLot.getTrxSecondaryUomQty());
        }

        mtMaterialLot.setLocatorId(currentInputMaterialLot.getLocatorId());
        mtMaterialLot.setAssemblePointId(currentInputMaterialLot.getAssemblePointId());

        if (currentInputMaterialLot.getLoadTime() != null) {
            mtMaterialLot.setLoadTime(currentInputMaterialLot.getLoadTime());
        } else if ("Y".equals(currentInputMaterialLot.getEnableFlag())
                        && (currentInputMaterialLot.getPrimaryUomQty() != null
                                        && BigDecimal.valueOf(currentInputMaterialLot.getPrimaryUomQty())
                                                        .compareTo(BigDecimal.ZERO) != 0)
                        || (currentInputMaterialLot.getTrxPrimaryUomQty() != null
                                        && BigDecimal.valueOf(currentInputMaterialLot.getTrxPrimaryUomQty())
                                                        .compareTo(BigDecimal.ZERO) != 0)) {
            mtMaterialLot.setLoadTime(new Date());
        }

        if (currentInputMaterialLot.getUnloadTime() != null) {
            mtMaterialLot.setUnloadTime(currentInputMaterialLot.getUnloadTime());
        }

        mtMaterialLot.setOwnerType(currentInputMaterialLot.getOwnerType());
        mtMaterialLot.setOwnerId(currentInputMaterialLot.getOwnerId());

        if (null == currentInputMaterialLot.getLot()) {
            String nextLot = materialLotNextLotGet(tenantId);
            mtMaterialLot.setLot(nextLot);
        } else {
            mtMaterialLot.setLot(currentInputMaterialLot.getLot());
        }

        mtMaterialLot.setOvenNumber(currentInputMaterialLot.getOvenNumber());
        mtMaterialLot.setSupplierId(currentInputMaterialLot.getSupplierId());
        mtMaterialLot.setSupplierSiteId(currentInputMaterialLot.getSupplierSiteId());
        mtMaterialLot.setCustomerId(currentInputMaterialLot.getCustomerId());
        mtMaterialLot.setCustomerSiteId(currentInputMaterialLot.getCustomerSiteId());
        mtMaterialLot.setReservedFlag(currentInputMaterialLot.getReservedFlag());
        mtMaterialLot.setReservedObjectType(currentInputMaterialLot.getReservedFlag());
        mtMaterialLot.setReservedObjectId(currentInputMaterialLot.getReservedObjectId());
        mtMaterialLot.setCreateReason(currentInputMaterialLot.getCreateReason());

        if (StringUtils.isEmpty(currentInputMaterialLot.getIdentification())) {
            mtMaterialLot.setIdentification(mtMaterialLot.getMaterialLotCode());
        } else {
            mtMaterialLot.setIdentification(currentInputMaterialLot.getIdentification());
        }

        mtMaterialLot.setEoId(currentInputMaterialLot.getEoId());
        mtMaterialLot.setInLocatorTime(currentInputMaterialLot.getInLocatorTime());
        mtMaterialLot.setFreezeFlag(currentInputMaterialLot.getFreezeFlag());
        mtMaterialLot.setStocktakeFlag(currentInputMaterialLot.getStocktakeFlag());
        // add by peng.yuan 2019-11-14
        if (null != currentInputMaterialLot.getInSiteTime()) {
            mtMaterialLot.setInSiteTime(currentInputMaterialLot.getInSiteTime());
        }

        mtMaterialLot.setCurrentContainerId(currentInputMaterialLot.getCurrentContainerId());

        if (null != currentInputMaterialLot.getTopContainerId()) {
            mtMaterialLot.setTopContainerId(currentInputMaterialLot.getTopContainerId());
        } else {
            if ("".equals(currentInputMaterialLot.getCurrentContainerId())) {
                mtMaterialLot.setTopContainerId("");
            } else if (StringUtils.isNotEmpty(currentInputMaterialLot.getCurrentContainerId())) {
                MtContainer container = new MtContainer();
                container.setTenantId(tenantId);
                container.setContainerId(currentInputMaterialLot.getCurrentContainerId());
                MtContainer mtContainer = mtContainerRepository.selectOne(container);
                if (mtContainer == null || StringUtils.isEmpty(mtContainer.getTopContainerId())) {
                    mtMaterialLot.setTopContainerId(currentInputMaterialLot.getCurrentContainerId());
                } else {
                    mtMaterialLot.setTopContainerId(mtContainer.getTopContainerId());
                }
            }
        }

        mtMaterialLot.setInstructionDocId(currentInputMaterialLot.getInstructionDocId());
        return mtMaterialLot;
    }

    @Override
    public List<MtMaterialLot> materialLotPropertyBatchGet(Long tenantId, List<String> materialLotIds) {
        if (CollectionUtils.isEmpty(materialLotIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "???API:materialLotPropertyBatchGet???"));
        }

        SecurityTokenHelper.close();
        return mtMaterialLotMapper.selectByMaterialLotId(tenantId, materialLotIds);
    }

    @Override
    public List<MtMaterialLot> materialLotPropertyBatchGetForUpdate(Long tenantId, List<String> materialLotIds) {
        if (CollectionUtils.isEmpty(materialLotIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "???API:materialLotPropertyBatchGet???"));
        }

        SecurityTokenHelper.close();
        return mtMaterialLotMapper.selectByMaterialLotIdForUpdate(tenantId, materialLotIds);
    }

    @Override
    public List<String> propertyLimitMaterialLotQuery(Long tenantId, MtMaterialLotVO3 dto) {
        List<MtMaterialLot> lotList = mtMaterialLotMapper.selectByPropertyLimit(tenantId, dto);
        if (CollectionUtils.isEmpty(lotList)) {
            return Collections.emptyList();
        }

        return lotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
    }

    @Override
    public MtNumrangeVO5 materialLotNextCodeGet(Long tenantId, MtMaterialLotVO26 dto) {
        // ????????????
        if (dto == null || StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "siteId", "???API:materialLotNextCodeGet???"));
        }
        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
        if (mtModSite == null) {
            throw new MtException("MT_MATERIAL_LOT_0091", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0091", "MATERIAL_LOT", "siteId", "???API:materialLotNextCodeGet???"));
        }

        // ?????????????????????
        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        numrangeObject.setObjectCode("MATERIAL_LOT_CODE");
        List<String> objectIdList =
                        mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, numrangeObject);
        if (CollectionUtils.isEmpty(objectIdList)) {
            throw new MtException("MT_MATERIAL_LOT_0090", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0090", "MATERIAL_LOT", "siteId", "???API:materialLotNextCodeGet???"));
        }

        MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
        objectColumn.setObjectId(objectIdList.get(0));
        List<MtNumrangeObjectColumn> mtNumrangeObjectColumns =
                        mtNumrangeObjectColumnRepository.propertyLimitNumrangeObjectColumnQuery(tenantId, objectColumn);
        MtNumrangeVO2 createNum = new MtNumrangeVO2();
        if (CollectionUtils.isNotEmpty(mtNumrangeObjectColumns)
                        && MapUtils.isNotEmpty(dto.getMaterilaLotPropertyList())) {
            List<String> objectColumnCode = mtNumrangeObjectColumns.stream().map(t -> t.getObjectColumnCode())
                            .collect(Collectors.toList());
            Map<String, String> tempMap = dto.getMaterilaLotPropertyList().entrySet().stream()
                            .filter(t -> objectColumnCode.contains(t.getKey())).filter(t -> t.getValue() != null)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Optional<MtNumrangeObjectColumn> first = mtNumrangeObjectColumns.stream().filter(
                            t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                            .findFirst();
            if (first.isPresent()) {
                createNum.setObjectTypeCode(first.get().getObjectColumnCode());
            }
            createNum.setCallObjectCodeList(tempMap);
        }
        createNum.setObjectCode("MATERIAL_LOT_CODE");
        createNum.setSiteId(dto.getSiteId());
        createNum.setOutsideNum(dto.getOutsideNum());
        createNum.setIncomingValueList(dto.getIncomingValueList());
        /**
         * add by liyuan.lv, 2020-03-25
         */
        createNum.setObjectTypeCode(dto.getMaterilaLotPropertyList().get("businessType"));

        return mtNumrangeRepository.numrangeGenerate(tenantId, createNum);
    }

    @Override
    public MtMaterialLotVO4 materialLotLimitMaterialQtyGet(Long tenantId, MtMaterialLotVO11 dto) {
        // Step1??????????????????????????????
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "???API:materialLotLimitMaterialQtyGet???"));
        }

        // Step2????????????????????????MT_MATERIAL_LOT???????????????
        MtMaterialLotVO4 mtMaterialLotVO4 = mtMaterialLotMapper.selectLimitQty(tenantId, dto.getMaterialLotId());

        // (????????????)
        // Step3??????????????????????????????????????????????????????????????????
        if (mtMaterialLotVO4 == null || !"Y".equals(dto.getPrimaryFlag())) {
            return mtMaterialLotVO4;
        }

        // Step3-b???????????????primaryFlag???Y???Step2???????????????????????????
        MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtMaterialLotVO4.getMaterialId());
        if (mtMaterialVO1 == null) {
            throw new MtException("MT_MATERIAL_LOT_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0016", "MATERIAL_LOT", "???API:materialLotLimitMaterialQtyGet???"));
        }

        // Step4????????????????????????????????????????????????
        String primaryUomId = mtMaterialVO1.getPrimaryUomId();
        Double primaryUomQty = null;

        if (StringUtils.isEmpty(mtMaterialLotVO4.getPrimaryUomId())
                        && StringUtils.isEmpty(mtMaterialVO1.getPrimaryUomId())
                        || StringUtils.isNotEmpty(mtMaterialLotVO4.getPrimaryUomId())
                                        && StringUtils.isNotEmpty(mtMaterialVO1.getPrimaryUomId())
                                        && mtMaterialLotVO4.getPrimaryUomId().equals(mtMaterialVO1.getPrimaryUomId())) {
            primaryUomQty = mtMaterialLotVO4.getPrimaryUomQty();
        } else {
            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtMaterialLotVO4.getPrimaryUomId());
            transferUomVO.setSourceValue(mtMaterialLotVO4.getPrimaryUomQty());
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
            if (transferUomVO != null && transferUomVO.getTargetValue() != null) {
                primaryUomQty = transferUomVO.getTargetValue();

            }
        }

        String secondaryUomId = mtMaterialVO1.getSecondaryUomId();
        Double secondaryUomQty = null;
        if (StringUtils.isNotEmpty(mtMaterialVO1.getSecondaryUomId())) {
            if (StringUtils.isEmpty(mtMaterialLotVO4.getSecondaryUomId())
                            && (mtMaterialLotVO4.getSecondaryUomQty() == null
                                            || new BigDecimal(mtMaterialLotVO4.getSecondaryUomQty().toString())
                                                            .compareTo(BigDecimal.ZERO) == 0)) {
                secondaryUomQty = Double.valueOf(0.0D);

            } else if (StringUtils.isNotEmpty(mtMaterialLotVO4.getSecondaryUomId())
                            && mtMaterialLotVO4.getSecondaryUomQty() != null
                            && new BigDecimal(mtMaterialLotVO4.getSecondaryUomQty().toString())
                                            .compareTo(BigDecimal.ZERO) != 0
                            && mtMaterialLotVO4.getSecondaryUomId().equals(mtMaterialVO1.getSecondaryUomId())) {
                secondaryUomQty = mtMaterialLotVO4.getSecondaryUomQty();
            } else if (StringUtils.isNotEmpty(mtMaterialLotVO4.getSecondaryUomId())
                            && mtMaterialLotVO4.getSecondaryUomQty() != null
                            && new BigDecimal(mtMaterialLotVO4.getSecondaryUomQty().toString())
                                            .compareTo(BigDecimal.ZERO) != 0
                            && !mtMaterialLotVO4.getSecondaryUomId().equals(mtMaterialVO1.getSecondaryUomId())) {
                MtUomVO1 transferUomVO = new MtUomVO1();
                transferUomVO.setSourceUomId(mtMaterialLotVO4.getSecondaryUomId());
                transferUomVO.setSourceValue(mtMaterialLotVO4.getSecondaryUomQty());
                transferUomVO.setTargetUomId(mtMaterialVO1.getSecondaryUomId());
                transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                if (transferUomVO != null && transferUomVO.getTargetValue() != null) {
                    secondaryUomQty = transferUomVO.getTargetValue();

                }
            }
        }

        mtMaterialLotVO4.setPrimaryUomId(primaryUomId);
        mtMaterialLotVO4.setSecondaryUomId(secondaryUomId);
        mtMaterialLotVO4.setPrimaryUomQty(primaryUomQty);
        mtMaterialLotVO4.setSecondaryUomQty(secondaryUomQty);
        return mtMaterialLotVO4;
    }

    @Override
    public void materialLotEnableValidate(Long tenantId, String materialLotId) {
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "???API:materialLotEnableValidate???"));
        }
        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, materialLotId);
        if (mtMaterialLot == null) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotEnableValidate???"));

        }
        if (!"Y".equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotEnableValidate???"));
        }
    }

    @Override
    public List<String> materialLotIdentify(Long tenantId, String identification) {
        if (StringUtils.isEmpty(identification)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "identification", "???API:materialLotIdentify???"));
        }
        MtMaterialLotVO3 dto = new MtMaterialLotVO3();
        dto.setIdentification(identification);
        List<MtMaterialLot> lotList = mtMaterialLotMapper.selectByPropertyLimit(tenantId, dto);

        return lotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
    }


    @Override
    public String materialLotNextLotGet(Long tenantId) {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHH");
        return format.format(new Date());
    }

    @Override
    public List<MtMaterialLotVO5> materialLotLimitMaterialQtyBatchGet(Long tenantId, List<String> materialLotIds) {
        // 1.?????????????????????????????????????????????
        if (CollectionUtils.isEmpty(materialLotIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId",
                                            "???API:materialLotLimitMaterialQtyBatchGet???"));
        }

        // 2.????????? ??????????????????????????????MT_MATERIAL_LOT???????????????
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotMapper.selectByMaterialLotId(tenantId, materialLotIds);
        if (CollectionUtils.isEmpty(mtMaterialLots) || materialLotIds.size() != mtMaterialLots.size()) {
            // ??????????????????
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotLimitMaterialQtyBatchGet???"));

        }

        // 3. ??????????????????????????????????????????????????????????????????????????????????????????????????????
        List<MtMaterialLotVO5> mtMaterialLotVO5s = new ArrayList<MtMaterialLotVO5>();
        for (MtMaterialLot mtMaterialLot : mtMaterialLots) {
            MtMaterialLotVO5 mtMaterialLotVO5 = new MtMaterialLotVO5();
            BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotVO5);
            MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
            if (null == mtMaterialVO1) {
                // ??????????????????
                throw new MtException("MT_MATERIAL_LOT_0016",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0016",
                                                "MATERIAL_LOT", "???API:materialLotLimitMaterialQtyBatchGet???"));

            }
            Double mPrimaryUomQty = null;
            Double mSecondaryUomQty = null;
            mtMaterialLotVO5.setmPrimaryUomId(mtMaterialVO1.getPrimaryUomId());
            mtMaterialLotVO5.setmSecondaryUomId(mtMaterialVO1.getSecondaryUomId());
            // ???
            if (mtMaterialLot.getPrimaryUomId().equals(mtMaterialVO1.getPrimaryUomId())) {
                mPrimaryUomQty = mtMaterialLot.getPrimaryUomQty();
            } else {
                MtUomVO1 transferUomVO = new MtUomVO1();
                transferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
                transferUomVO.setSourceValue(mtMaterialLot.getPrimaryUomQty());
                transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
                MtUomVO1 transferUomVO1 = mtUomRepository.uomConversion(tenantId, transferUomVO);
                if (null != transferUomVO1) {
                    // ?????????????????????????????????,??????????????????
                    mPrimaryUomQty = transferUomVO1.getTargetValue();
                }
            }
            // ????????????????????????????????????
            if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId())
                            && StringUtils.isNotEmpty(mtMaterialVO1.getSecondaryUomId())) {

                if (mtMaterialLot.getSecondaryUomId().equals(mtMaterialVO1.getSecondaryUomId())) {
                    mSecondaryUomQty = mtMaterialLot.getSecondaryUomQty();
                } else {
                    MtUomVO1 transferUomVO = new MtUomVO1();
                    transferUomVO.setSourceUomId(mtMaterialLot.getSecondaryUomId());
                    transferUomVO.setSourceValue(mtMaterialLot.getSecondaryUomQty());
                    transferUomVO.setTargetUomId(mtMaterialVO1.getSecondaryUomId());
                    MtUomVO1 transferUomVO1 = mtUomRepository.uomConversion(tenantId, transferUomVO);
                    if (null != transferUomVO1) {
                        // ?????????????????????????????????,??????????????????
                        mSecondaryUomQty = transferUomVO1.getTargetValue();
                    }
                }
            }
            mtMaterialLotVO5.setmPrimaryUomQty(mPrimaryUomQty);
            mtMaterialLotVO5.setmSecondaryUomQty(mSecondaryUomQty);
            mtMaterialLotVO5s.add(mtMaterialLotVO5);
        }
        return mtMaterialLotVO5s;
    }

    @Override
    public void materialLotReserveVerify(Long tenantId, MtMaterialLotVO6 dto) {
        // 1.?????????????????????????????????????????????
        if (dto == null || StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "???API:materialLotReserveVerify???"));
        }
        if (StringUtils.isEmpty(dto.getReservedObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "reservedObjectType", "???API:materialLotReserveVerify???"));
        }
        if (StringUtils.isEmpty(dto.getReservedObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "reservedObjectId", "???API:materialLotReserveVerify???"));
        }
        // 2.??????????????????????????????materialLotId???????????????????????????enableFlag?????????????????????reservedFlag
        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, dto.getMaterialLotId());
        if (mtMaterialLot == null) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotReserveVerify???"));

        }

        if (!"Y".equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotReserveVerify???"));


        }
        if ("Y".equals(mtMaterialLot.getReservedFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0056", "MATERIAL_LOT", "???API:materialLotReserveVerify???"));

        }

        // 3.??????????????????????????????????????????????????????
        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
        mtContLoadDtlVO5.setLoadObjectId(dto.getMaterialLotId());
        mtContLoadDtlVO5.setTopLevelFlag("Y");
        mtContLoadDtlVO5.setLoadObjectType("MATERIAL_LOT");
        List<String> containerIds =
                        mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
        if (CollectionUtils.isEmpty(containerIds)) {
            // ?????????????????????????????????????????????????????????
            return;
        }

        // ????????????????????????
        List<MtContainer> mtContainers = mtContainerRepository.containerPropertyBatchGet(tenantId, containerIds);
        if (CollectionUtils.isEmpty(mtContainers) || mtContainers.size() != containerIds.size()) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "???API:materialLotReserveVerify???"));
        }

        mtContainers.stream().filter(t -> "Y".equals(t.getReservedFlag())).forEach(container -> {
            if (!dto.getReservedObjectId().equals(container.getReservedObjectId())
                            || !dto.getReservedObjectType().equals(container.getReservedObjectType())) {
                throw new MtException("MT_MATERIAL_LOT_0057", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0057", "MATERIAL_LOT", "???API:materialLotReserveVerify???"));
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLotReserve(Long tenantId, MtMaterialLotVO7 dto) {
        // 1. ?????????????????????????????????????????????
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "???API:materialLotReserve???"));
        }
        if (StringUtils.isEmpty(dto.getReservedObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "reservedObjectType", "???API:materialLotReserve???"));
        }
        if (StringUtils.isEmpty(dto.getReservedObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "reservedObjectId", "???API:materialLotReserve???"));
        }

        // 2. ?????????????????????
        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, dto.getMaterialLotId());
        if (mtMaterialLot == null) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API???materialLotReserve???"));
        }

        // 3. ???????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_RESERVE");
        eventCreateVO.setLocatorId(mtMaterialLot.getLocatorId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. ????????????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO2.setReservedFlag("Y");
        mtMaterialLotVO2.setReservedObjectType(dto.getReservedObjectType());
        mtMaterialLotVO2.setReservedObjectId(dto.getReservedObjectId());
        mtMaterialLotVO2.setEventId(eventId);
        materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

        // 5. ?????????????????????????????????????????????
        if ("Y".equals(mtMaterialLot.getEnableFlag())) {
            MtInvOnhandHoldVO8 onhandReleaseVO2 = new MtInvOnhandHoldVO8();
            onhandReleaseVO2.setSiteId(mtMaterialLot.getSiteId());
            onhandReleaseVO2.setMaterialId(mtMaterialLot.getMaterialId());
            onhandReleaseVO2.setLocatorId(mtMaterialLot.getLocatorId());
            onhandReleaseVO2.setLotCode(mtMaterialLot.getLot());
            onhandReleaseVO2.setHoldType("ORDER");
            onhandReleaseVO2.setOrderType(dto.getReservedObjectType());
            onhandReleaseVO2.setOrderId(dto.getReservedObjectId());
            onhandReleaseVO2.setEventId(eventId);
            onhandReleaseVO2.setOwnerId(mtMaterialLot.getOwnerId());
            onhandReleaseVO2.setOwnerType(mtMaterialLot.getOwnerType());

            // ?????? ????????????
            MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
            transferUomVO.setSourceValue(mtMaterialLot.getPrimaryUomQty());
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            MtUomVO1 transferUomResult = mtUomRepository.uomConversion(tenantId, transferUomVO);
            if (transferUomResult != null && transferUomResult.getTargetValue() != null) {
                onhandReleaseVO2.setHoldQuantity(transferUomResult.getTargetValue());
            } else {
                onhandReleaseVO2.setHoldQuantity(0.0D);
            }
            mtInvOnhandHoldRepository.onhandReserveCreateProcess(tenantId, onhandReleaseVO2);
        }
    }

    @Override
    public void materialLotReserveCancelVerify(Long tenantId, String materialLotId) {
        // 1. ??????????????????????????????
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "???API:materialLotReserveCancelVerify???"));
        }

        // 2. ?????????????????????
        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, materialLotId);
        if (mtMaterialLot == null || StringUtils.isEmpty(mtMaterialLot.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotReserveCancelVerify???"));
        }

        // 2.1. ?????????????????????????????????enableFlag
        if (!"Y".equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotReserveCancelVerify???"));
        }

        // 2.2. ????????????????????????????????????
        if (!"Y".equals(mtMaterialLot.getReservedFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0019", "MATERIAL_LOT", "???API:materialLotReserveCancelVerify???"));
        }

        // 3. ??????????????????????????????????????????
        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
        mtContLoadDtlVO5.setLoadObjectType("MATERIAL_LOT");
        mtContLoadDtlVO5.setLoadObjectId(materialLotId);
        mtContLoadDtlVO5.setTopLevelFlag("N");
        List<String> containerIds =
                        mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
        if (CollectionUtils.isEmpty(containerIds)) {
            return;
        }

        // ??????????????????????????????????????????????????????
        List<MtContainer> mtContainers = mtContainerRepository.containerPropertyBatchGet(tenantId, containerIds);
        if (CollectionUtils.isEmpty(mtContainers) || mtContainers.size() != containerIds.size()) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "???API:materialLotReserveCancelVerify???"));
        }

        if (CollectionUtils.isNotEmpty(mtContainers.stream().filter(t -> "Y".equals(t.getReservedFlag()))
                        .collect(Collectors.toList()))) {
            throw new MtException("MT_MATERIAL_LOT_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0060", "MATERIAL_LOT", "???API:materialLotReserveCancelVerify???"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLotReserveCancel(Long tenantId, MtMaterialLotVO7 dto) {
        // 1. ??????????????????????????????
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "???API:materialLotReserveCancel???"));
        }

        // 2. ?????????????????????
        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, dto.getMaterialLotId());
        if (mtMaterialLot == null) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API???materialLotReserveCancel???"));
        }

        // 2.1. ????????????????????????????????????
        if (!"Y".equals(mtMaterialLot.getReservedFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0019", "MATERIAL_LOT", "???API:materialLotReserveCancel???"));
        }

        // 3. ?????????????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_RESERVE_CANCEL");
        eventCreateVO.setLocatorId(mtMaterialLot.getLocatorId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. ??????????????????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO2.setReservedFlag("N");
        mtMaterialLotVO2.setReservedObjectType("");
        mtMaterialLotVO2.setReservedObjectId("");
        mtMaterialLotVO2.setEventId(eventId);
        materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

        // 5. ?????????????????????????????????????????????
        if ("Y".equals(mtMaterialLot.getEnableFlag())) {
            MtInvOnhandHoldVO9 onhandReleaseVO3 = new MtInvOnhandHoldVO9();
            onhandReleaseVO3.setSiteId(mtMaterialLot.getSiteId());
            onhandReleaseVO3.setMaterialId(mtMaterialLot.getMaterialId());
            onhandReleaseVO3.setLocatorId(mtMaterialLot.getLocatorId());
            onhandReleaseVO3.setLotCode(mtMaterialLot.getLot());
            onhandReleaseVO3.setHoldType("ORDER");
            onhandReleaseVO3.setOrderType(mtMaterialLot.getReservedObjectType());
            onhandReleaseVO3.setOrderId(mtMaterialLot.getReservedObjectId());
            onhandReleaseVO3.setEventId(eventId);
            onhandReleaseVO3.setOwnerId(mtMaterialLot.getOwnerId());
            onhandReleaseVO3.setOwnerType(mtMaterialLot.getOwnerType());

            // ?????? ????????????
            MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
            transferUomVO.setSourceValue(mtMaterialLot.getPrimaryUomQty());
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            MtUomVO1 transferUomResult = mtUomRepository.uomConversion(tenantId, transferUomVO);
            if (transferUomResult != null && transferUomResult.getTargetValue() != null) {
                onhandReleaseVO3.setReleaseQuantity(transferUomResult.getTargetValue());
            } else {
                onhandReleaseVO3.setReleaseQuantity(0.0D);
            }

            mtInvOnhandHoldRepository.onhandReserveReleaseProcess(tenantId, onhandReleaseVO3);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLotIdentificationUpdate(Long tenantId, MtMaterialLotVO8 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "???API:materialLotIdentificationUpdate???"));
        }
        if (StringUtils.isEmpty(dto.getIdentification())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "identification", "???API:materialLotIdentificationUpdate???"));
        }

        // 2. ?????????????????????????????????????????????
        MtMaterialLotVO3 mtMaterialLotVO3 = new MtMaterialLotVO3();
        mtMaterialLotVO3.setIdentification(dto.getIdentification());
        List<String> materialLotIds = propertyLimitMaterialLotQuery(tenantId, mtMaterialLotVO3);
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            throw new MtException("MT_MATERIAL_LOT_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0020", "MATERIAL_LOT", "???API:materialLotIdentificationUpdate???"));
        }

        // 3. ???????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_IDENTIFICATION_CHANGE");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. ????????????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setIdentification(dto.getIdentification());
        materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLotTransfer(Long tenantId, MtMaterialLotVO9 dto) {
        // 1.?????????????????????????????????????????????
        if (dto == null || StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "???API:materialLotTransfer???"));
        }
        if (StringUtils.isEmpty(dto.getTargetLocatorId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "targetLocatorId", "???API:materialLotTransfer???"));
        }
        // 2.????????????????????????????????????????????????
        // 2.a ?????????????????????
        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, dto.getMaterialLotId());

        if (mtMaterialLot == null || !"Y".equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotTransfer???"));
        }

        if ("Y".equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0072",
                                            "MATERIAL_LOT", dto.getMaterialLotId(), "???API:materialLotTransfer???"));
        }

        // ???????????????????????????
        if (dto.getTargetSiteId() == null || dto.getTargetSiteId().equals(mtMaterialLot.getSiteId())) {
            String targetLocatorId = null == dto.getTargetLocatorId() ? "" : dto.getTargetLocatorId();
            String locatorId = null == mtMaterialLot.getLocatorId() ? "" : mtMaterialLot.getLocatorId();
            if (targetLocatorId.equals(locatorId)) {
                throw new MtException("MT_MATERIAL_LOT_0063", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0063", "MATERIAL_LOT", "???API:materialLotTransfer???"));
            }
        }

        // 3.???????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        if (StringUtils.isNotEmpty(dto.getTargetSiteId()) && !dto.getTargetSiteId().equals(mtMaterialLot.getSiteId())) {
            eventCreateVO.setEventTypeCode("MATERIAL_LOT_SITE_TRANSFER_OUT");
        } else {
            eventCreateVO.setEventTypeCode("MATERIAL_LOT_LOCATOR_TRANSFER_OUT");
        }
        eventCreateVO.setLocatorId(mtMaterialLot.getLocatorId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String parentEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        if (StringUtils.isNotEmpty(dto.getTargetSiteId()) && !dto.getTargetSiteId().equals(mtMaterialLot.getSiteId())) {
            eventCreateVO.setEventTypeCode("MATERIAL_LOT_SITE_TRANSFER_IN");
        } else {
            eventCreateVO.setEventTypeCode("MATERIAL_LOT_LOCATOR_TRANSFER_IN");
        }
        eventCreateVO.setLocatorId(dto.getTargetLocatorId());
        eventCreateVO.setParentEventId(parentEventId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ?????????????????????????????????????????????????????????
        MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
        Double primaryUomQty = null;
        if (mtMaterialVO1.getPrimaryUomId().equals(mtMaterialLot.getPrimaryUomId())) {
            primaryUomQty = mtMaterialLot.getPrimaryUomQty();
        } else {
            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            transferUomVO.setSourceValue(mtMaterialLot.getPrimaryUomQty());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
            primaryUomQty = transferUomVO.getTargetValue();
        }

        // 5.???????????????????????????????????????????????????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO2.setEventId(parentEventId);
        materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

        // ???????????????????????????????????????????????????
        if ("Y".equals(mtMaterialLot.getReservedFlag())) {
            // ??????????????????
            MtInvOnhandHoldVO7 onhandReleaseVO1 = new MtInvOnhandHoldVO7();
            onhandReleaseVO1.setSiteId(mtMaterialLot.getSiteId());
            onhandReleaseVO1.setMaterialId(mtMaterialLot.getMaterialId());
            onhandReleaseVO1.setLocatorId(mtMaterialLot.getLocatorId());
            onhandReleaseVO1.setLotCode(mtMaterialLot.getLot());
            onhandReleaseVO1.setQuantity(primaryUomQty);
            onhandReleaseVO1.setHoldType("ORDER");
            onhandReleaseVO1.setOrderType(mtMaterialLot.getReservedObjectType());
            onhandReleaseVO1.setOrderId(mtMaterialLot.getReservedObjectId());
            onhandReleaseVO1.setEventId(parentEventId);
            onhandReleaseVO1.setOwnerId(mtMaterialLot.getOwnerId());
            onhandReleaseVO1.setOwnerType(mtMaterialLot.getOwnerType());
            mtInvOnhandHoldRepository.onhandReserveUseProcess(tenantId, onhandReleaseVO1);
        } else {
            MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
            updateOnhandVO.setSiteId(mtMaterialLot.getSiteId());
            updateOnhandVO.setMaterialId(mtMaterialLot.getMaterialId());
            updateOnhandVO.setLocatorId(mtMaterialLot.getLocatorId());
            updateOnhandVO.setLotCode(mtMaterialLot.getLot());
            updateOnhandVO.setChangeQuantity(primaryUomQty);
            updateOnhandVO.setEventId(parentEventId);
            updateOnhandVO.setOwnerId(mtMaterialLot.getOwnerId());
            updateOnhandVO.setOwnerType(mtMaterialLot.getOwnerType());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
        }

        // ???????????????????????????????????????????????????????????????
        Date now = new Date();
        MtMaterialLotVO2 mtMaterialLotVO21 = new MtMaterialLotVO2();
        mtMaterialLotVO21.setMaterialLotId(dto.getMaterialLotId());
        if (StringUtils.isNotEmpty(dto.getTargetSiteId())) {
            mtMaterialLotVO21.setSiteId(dto.getTargetSiteId());
        }
        mtMaterialLotVO21.setLocatorId(dto.getTargetLocatorId());
        mtMaterialLotVO21.setEventId(eventId);
        mtMaterialLotVO21.setInstructionDocId(dto.getInstructionDocId());
        if (StringUtils.isNotEmpty(dto.getTargetSiteId()) && !mtMaterialLot.getSiteId().equals(dto.getTargetSiteId())) {
            mtMaterialLotVO21.setInSiteTime(now);
        }
        mtMaterialLotVO21.setInLocatorTime(now);

        materialLotUpdate(tenantId, mtMaterialLotVO21, "N");

        // ???????????????????????????????????????????????????
        MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
        if (StringUtils.isNotEmpty(dto.getTargetSiteId())) {
            updateOnhandVO.setSiteId(dto.getTargetSiteId());
        } else {
            updateOnhandVO.setSiteId(mtMaterialLot.getSiteId());
        }
        updateOnhandVO.setMaterialId(mtMaterialLot.getMaterialId());
        updateOnhandVO.setLocatorId(dto.getTargetLocatorId());
        updateOnhandVO.setLotCode(mtMaterialLot.getLot());
        updateOnhandVO.setChangeQuantity(primaryUomQty);
        updateOnhandVO.setEventId(eventId);
        updateOnhandVO.setOwnerId(mtMaterialLot.getOwnerId());
        updateOnhandVO.setOwnerType(mtMaterialLot.getOwnerType());
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);

        if ("Y".equals(mtMaterialLot.getReservedFlag())) {
            // ??????????????????
            MtInvOnhandHoldVO8 onhandReleaseVO2 = new MtInvOnhandHoldVO8();
            if (StringUtils.isNotEmpty(dto.getTargetSiteId())) {
                onhandReleaseVO2.setSiteId(dto.getTargetSiteId());
            } else {
                onhandReleaseVO2.setSiteId(mtMaterialLot.getSiteId());
            }
            onhandReleaseVO2.setMaterialId(mtMaterialLot.getMaterialId());
            onhandReleaseVO2.setLocatorId(dto.getTargetLocatorId());
            onhandReleaseVO2.setLotCode(mtMaterialLot.getLot());
            onhandReleaseVO2.setHoldQuantity(primaryUomQty);
            onhandReleaseVO2.setHoldType("ORDER");
            onhandReleaseVO2.setOrderType(mtMaterialLot.getReservedObjectType());
            onhandReleaseVO2.setOrderId(mtMaterialLot.getReservedObjectId());
            onhandReleaseVO2.setEventId(eventId);
            onhandReleaseVO2.setOwnerId(mtMaterialLot.getOwnerId());
            onhandReleaseVO2.setOwnerType(mtMaterialLot.getOwnerType());
            mtInvOnhandHoldRepository.onhandReserveCreateProcess(tenantId, onhandReleaseVO2);
        }
    }

    /**
     * materialLotBatchTransfer-?????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/10/10
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLotBatchTransfer(Long tenantId, MtMaterialLotVO14 dto) {
        // 1.?????????????????????????????????????????????
        if (CollectionUtils.isEmpty(dto.getMaterialLotIds())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotIds", "???API:materialLotBatchTransfer???"));
        }
        if (StringUtils.isEmpty(dto.getTargetLocatorId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "targetLocatorId", "???API:materialLotBatchTransfer???"));
        }
        // 2.????????????????????????????????????????????????
        // 2.a ?????????????????????
        List<MtMaterialLot> mtMaterialLots = materialLotPropertyBatchGet(tenantId, dto.getMaterialLotIds());
        if (CollectionUtils.isEmpty(mtMaterialLots)) {
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotBatchTransfer???"));
        }

        // ?????????id??????????????????????????????????????????????????????
        boolean isNotSameFlag = mtMaterialLots.size() != dto.getMaterialLotIds().size();
        Optional<MtMaterialLot> unEnableMaterialLots =
                        mtMaterialLots.stream().filter(t -> !"Y".equals(t.getEnableFlag())).findFirst();
        if (isNotSameFlag || unEnableMaterialLots.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotBatchTransfer???"));
        }

        // ???????????????????????????Y?????????????????????
        Optional<MtMaterialLot> stocktakedMaterialLots =
                        mtMaterialLots.stream().filter(t -> "Y".equals(t.getStocktakeFlag())).findFirst();
        if (stocktakedMaterialLots.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0072",
                                            "MATERIAL_LOT", stocktakedMaterialLots.get().getMaterialLotId(),
                                            "???API:materialLotBatchTransfer???"));
        }

        // ???????????????????????????
        List<String> materialLotSiteIds =
                        mtMaterialLots.stream().map(MtMaterialLot::getSiteId).collect(Collectors.toList());

        String targetLocatorId = null == dto.getTargetLocatorId() ? "" : dto.getTargetLocatorId();
        List<String> materialLotLocatorIds = new ArrayList<>();

        if (StringUtils.isEmpty(dto.getTargetSiteId())) {
            mtMaterialLots.stream().forEach(t -> t.setLocatorId(t.getLocatorId() == null ? "" : t.getLocatorId()));
            materialLotLocatorIds =
                            mtMaterialLots.stream().map(MtMaterialLot::getLocatorId).collect(Collectors.toList());
        } else {
            List<MtMaterialLot> curMaterialLots = mtMaterialLots.stream()
                            .filter(t -> dto.getTargetSiteId().equals(t.getSiteId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(curMaterialLots)) {
                materialLotLocatorIds =
                                curMaterialLots.stream().map(MtMaterialLot::getLocatorId).collect(Collectors.toList());
            }
        }

        if (CollectionUtils.isNotEmpty(materialLotLocatorIds) && materialLotLocatorIds.contains(targetLocatorId)) {
            throw new MtException("MT_MATERIAL_LOT_0063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0063", "MATERIAL_LOT", "???API:materialLotBatchTransfer???"));
        }

        // ?????????????????????????????????????????????????????????
        List<MtMaterialLot> oldMaterialLots = mtMaterialLots;

        // 3.???????????????????????????
        String materialLotOutEvent;
        String materialLotInEvent;
        if (StringUtils.isNotEmpty(dto.getTargetSiteId()) && !materialLotSiteIds.contains(dto.getTargetSiteId())) {
            materialLotOutEvent = "MATERIAL_LOT_SITE_TRANSFER_OUT";
            materialLotInEvent = "MATERIAL_LOT_SITE_TRANSFER_IN";
        } else {
            materialLotOutEvent = "MATERIAL_LOT_LOCATOR_TRANSFER_OUT";
            materialLotInEvent = "MATERIAL_LOT_LOCATOR_TRANSFER_IN";
        }

        // ????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(materialLotOutEvent);
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String outEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ????????????
        eventCreateVO.setEventTypeCode(materialLotInEvent);
        eventCreateVO.setLocatorId(dto.getTargetLocatorId());
        eventCreateVO.setParentEventId(outEventId);
        String inEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ?????????????????????????????????????????????????????????
        List<String> materialIds =
                        mtMaterialLots.stream().map(MtMaterialLot::getMaterialId).collect(Collectors.toList());

        // ????????????????????????
        List<MtMaterialVO> materialVos = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);

        // ????????????????????????????????????????????????????????????????????????
        materialVos.stream().forEach(material -> {
            Optional<MtMaterialLot> curMaterialLot = mtMaterialLots.stream()
                            .filter(materialLot -> materialLot.getMaterialId().equals(material.getMaterialId()))
                            .findFirst();

            if (curMaterialLot.isPresent()) {
                if (!curMaterialLot.get().getPrimaryUomId().equals(material.getPrimaryUomId())) {
                    MtUomVO1 transferUomVO = new MtUomVO1();
                    transferUomVO.setSourceUomId(curMaterialLot.get().getPrimaryUomId());
                    transferUomVO.setTargetUomId(material.getPrimaryUomId());
                    transferUomVO.setSourceValue(curMaterialLot.get().getPrimaryUomQty());
                    transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                    curMaterialLot.get().setPrimaryUomQty(transferUomVO.getTargetValue());
                }
            }
        });

        // ?????????????????????????????????????????????????????????????????????????????????
        Map<ReservedMaterialLot, List<MtMaterialLot>> reservedMaterialLotsMap = mtMaterialLots.stream()
                        .filter(t -> "Y".equals(t.getReservedFlag()))
                        .collect(Collectors.groupingBy(b -> new ReservedMaterialLot(b.getSiteId(), b.getMaterialId(),
                                        b.getLocatorId(), b.getOwnerType(), b.getOwnerId(), b.getLot(),
                                        b.getReservedObjectType(), b.getReservedObjectId())));

        Map<UnReservedMaterialLot, List<MtMaterialLot>> unReservedMaterialLotsMap =
                        mtMaterialLots.stream().filter(t -> !"Y".equals(t.getReservedFlag()))
                                        .collect(Collectors.groupingBy(b -> new UnReservedMaterialLot(b.getSiteId(),
                                                        b.getMaterialId(), b.getLocatorId(), b.getOwnerType(),
                                                        b.getOwnerId(), b.getLot())));

        // ??????????????????????????????????????????????????? - ??????
        // ??????????????????????????????????????????????????? - ??????
        for (Map.Entry<ReservedMaterialLot, List<MtMaterialLot>> entry : reservedMaterialLotsMap.entrySet()) {
            // ????????????????????????
            BigDecimal sumPrimaryUomQty =
                            entry.getValue().stream()
                                            .collect(CollectorsUtil.summingBigDecimal(t -> null == t.getPrimaryUomQty()
                                                            ? BigDecimal.ZERO
                                                            : BigDecimal.valueOf(t.getPrimaryUomQty())));

            // ??????????????? ????????????????????????
            MtInvOnhandHoldVO7 mtInvOnhandHoldVO7 = new MtInvOnhandHoldVO7();
            mtInvOnhandHoldVO7.setSiteId(entry.getKey().getSiteId());
            mtInvOnhandHoldVO7.setMaterialId(entry.getKey().getMaterialId());
            mtInvOnhandHoldVO7.setLocatorId(entry.getKey().getLocatorId());
            mtInvOnhandHoldVO7.setLotCode(entry.getKey().getLot());
            mtInvOnhandHoldVO7.setQuantity(sumPrimaryUomQty.doubleValue());
            mtInvOnhandHoldVO7.setHoldType("ORDER");
            mtInvOnhandHoldVO7.setOrderType(entry.getKey().getReservedObjectType());
            mtInvOnhandHoldVO7.setOrderId(entry.getKey().getReservedObjectId());
            mtInvOnhandHoldVO7.setEventId(outEventId);
            mtInvOnhandHoldVO7.setOwnerId(entry.getKey().getOwnerId());
            mtInvOnhandHoldVO7.setOwnerType(entry.getKey().getOwnerType());
            mtInvOnhandHoldRepository.onhandReserveUseProcess(tenantId, mtInvOnhandHoldVO7);

            // ???????????????/?????? ????????????????????????
            // ??????????????????
            MtInvOnhandHoldVO8 mtInvOnhandHoldVO8 = new MtInvOnhandHoldVO8();
            if (StringUtils.isNotEmpty(dto.getTargetSiteId())) {
                mtInvOnhandHoldVO8.setSiteId(dto.getTargetSiteId());
            } else {
                mtInvOnhandHoldVO8.setSiteId(entry.getKey().getSiteId());
            }
            mtInvOnhandHoldVO8.setMaterialId(entry.getKey().getMaterialId());
            mtInvOnhandHoldVO8.setLocatorId(dto.getTargetLocatorId());
            mtInvOnhandHoldVO8.setLotCode(entry.getKey().getLot());
            mtInvOnhandHoldVO8.setHoldQuantity(sumPrimaryUomQty.doubleValue());
            mtInvOnhandHoldVO8.setHoldType("ORDER");
            mtInvOnhandHoldVO8.setOrderType(entry.getKey().getReservedObjectType());
            mtInvOnhandHoldVO8.setOrderId(entry.getKey().getReservedObjectId());
            mtInvOnhandHoldVO8.setEventId(inEventId);
            mtInvOnhandHoldVO8.setOwnerId(entry.getKey().getOwnerId());
            mtInvOnhandHoldVO8.setOwnerType(entry.getKey().getOwnerType());
            mtInvOnhandHoldRepository.onhandReserveCreateProcess(tenantId, mtInvOnhandHoldVO8);

            // ??????/???????????????
            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            if (StringUtils.isNotEmpty(dto.getTargetSiteId())) {
                mtInvOnhandQuantityVO9.setSiteId(dto.getTargetSiteId());
            } else {
                mtInvOnhandQuantityVO9.setSiteId(entry.getKey().getSiteId());
            }
            mtInvOnhandQuantityVO9.setMaterialId(entry.getKey().getMaterialId());
            mtInvOnhandQuantityVO9.setLocatorId(dto.getTargetLocatorId());
            mtInvOnhandQuantityVO9.setLotCode(entry.getKey().getLot());
            mtInvOnhandQuantityVO9.setChangeQuantity(sumPrimaryUomQty.doubleValue());
            mtInvOnhandQuantityVO9.setEventId(inEventId);
            mtInvOnhandQuantityVO9.setOwnerId(entry.getKey().getOwnerId());
            mtInvOnhandQuantityVO9.setOwnerType(entry.getKey().getOwnerType());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
        }

        for (Map.Entry<UnReservedMaterialLot, List<MtMaterialLot>> entry : unReservedMaterialLotsMap.entrySet()) {
            // ????????????????????????
            BigDecimal sumPrimaryUomQty =
                            entry.getValue().stream()
                                            .collect(CollectorsUtil.summingBigDecimal(t -> null == t.getPrimaryUomQty()
                                                            ? BigDecimal.ZERO
                                                            : BigDecimal.valueOf(t.getPrimaryUomQty())));

            // ??????????????? ?????????
            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            mtInvOnhandQuantityVO9.setSiteId(entry.getKey().getSiteId());
            mtInvOnhandQuantityVO9.setMaterialId(entry.getKey().getMaterialId());
            mtInvOnhandQuantityVO9.setLocatorId(entry.getKey().getLocatorId());
            mtInvOnhandQuantityVO9.setLotCode(entry.getKey().getLot());
            mtInvOnhandQuantityVO9.setChangeQuantity(sumPrimaryUomQty.doubleValue());
            mtInvOnhandQuantityVO9.setEventId(outEventId);
            mtInvOnhandQuantityVO9.setOwnerId(entry.getKey().getOwnerId());
            mtInvOnhandQuantityVO9.setOwnerType(entry.getKey().getOwnerType());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

            // ???????????????/?????? ?????????
            mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            if (StringUtils.isNotEmpty(dto.getTargetSiteId())) {
                mtInvOnhandQuantityVO9.setSiteId(dto.getTargetSiteId());
            } else {
                mtInvOnhandQuantityVO9.setSiteId(entry.getKey().getSiteId());
            }
            mtInvOnhandQuantityVO9.setMaterialId(entry.getKey().getMaterialId());
            mtInvOnhandQuantityVO9.setLocatorId(dto.getTargetLocatorId());
            mtInvOnhandQuantityVO9.setLotCode(entry.getKey().getLot());
            mtInvOnhandQuantityVO9.setChangeQuantity(sumPrimaryUomQty.doubleValue());
            mtInvOnhandQuantityVO9.setEventId(inEventId);
            mtInvOnhandQuantityVO9.setOwnerId(entry.getKey().getOwnerId());
            mtInvOnhandQuantityVO9.setOwnerType(entry.getKey().getOwnerType());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
        }

        // 5.????????????????????????
        List<String> sqlList = new ArrayList<>();
        Date now = new Date();

        // ?????????????????????
        List<String> materialLotCids = customDbRepository.getNextKeys("mt_material_lot_cid_s", oldMaterialLots.size());
        List<String> materialLotHisIds =
                        customDbRepository.getNextKeys("mt_material_lot_his_s", oldMaterialLots.size());
        List<String> materialLotHisCids =
                        customDbRepository.getNextKeys("mt_material_lot_his_cid_s", oldMaterialLots.size());

        for (int i = 0; i < oldMaterialLots.size(); i++) {
            MtMaterialLot mtMaterialLot = oldMaterialLots.get(i);

            // ??????????????????????????????????????????
            mtMaterialLot.setCid(Long.valueOf(materialLotCids.get(i)));
            mtMaterialLot.setLocatorId(dto.getTargetLocatorId());
            mtMaterialLot.setInLocatorTime(now);
            mtMaterialLot.setInstructionDocId(
                            StringUtils.isNotEmpty(dto.getInstructionDocId()) ? dto.getInstructionDocId()
                                            : mtMaterialLot.getInstructionDocId());
            if (StringUtils.isNotEmpty(dto.getTargetSiteId())) {
                if (!dto.getTargetSiteId().equals(mtMaterialLot.getSiteId())) {
                    mtMaterialLot.setInSiteTime(now);
                }
                mtMaterialLot.setSiteId(dto.getTargetSiteId());
            }

            // ??????????????????ID
            mtMaterialLot.setLatestHisId(materialLotHisIds.get(i));
            mtMaterialLot.setLastUpdatedBy(DetailsHelper.getUserDetails().getUserId());
            mtMaterialLot.setLastUpdateDate(now);
            sqlList.addAll(customDbRepository.getUpdateSql(mtMaterialLot));

            // ????????????
            MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
            mtMaterialLotHis.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotHis.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            mtMaterialLotHis.setEnableFlag(mtMaterialLot.getEnableFlag());
            mtMaterialLotHis.setQualityStatus(mtMaterialLot.getQualityStatus());
            mtMaterialLotHis.setMaterialId(mtMaterialLot.getMaterialId());
            mtMaterialLotHis.setPrimaryUomId(mtMaterialLot.getPrimaryUomId());
            mtMaterialLotHis.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty());
            mtMaterialLotHis.setSecondaryUomId(mtMaterialLot.getSecondaryUomId());
            mtMaterialLotHis.setSecondaryUomQty(mtMaterialLot.getSecondaryUomQty());
            mtMaterialLotHis.setAssemblePointId(mtMaterialLot.getAssemblePointId());
            mtMaterialLotHis.setLoadTime(mtMaterialLot.getLoadTime());
            mtMaterialLotHis.setUnloadTime(mtMaterialLot.getUnloadTime());
            mtMaterialLotHis.setOwnerType(mtMaterialLot.getOwnerType());
            mtMaterialLotHis.setOwnerId(mtMaterialLot.getOwnerId());
            mtMaterialLotHis.setLot(mtMaterialLot.getLot());
            mtMaterialLotHis.setOvenNumber(mtMaterialLot.getOvenNumber());
            mtMaterialLotHis.setSupplierId(mtMaterialLot.getSupplierId());
            mtMaterialLotHis.setSupplierSiteId(mtMaterialLot.getSupplierSiteId());
            mtMaterialLotHis.setCustomerId(mtMaterialLot.getCustomerId());
            mtMaterialLotHis.setCustomerSiteId(mtMaterialLot.getCustomerSiteId());
            mtMaterialLotHis.setReservedFlag(mtMaterialLot.getReservedFlag());
            mtMaterialLotHis.setReservedObjectType(mtMaterialLot.getReservedObjectType());
            mtMaterialLotHis.setReservedObjectId(mtMaterialLot.getReservedObjectId());
            mtMaterialLotHis.setCreateReason(mtMaterialLot.getCreateReason());
            mtMaterialLotHis.setIdentification(mtMaterialLot.getIdentification());
            mtMaterialLotHis.setEoId(mtMaterialLot.getEoId());
            mtMaterialLotHis.setInLocatorTime(mtMaterialLot.getInLocatorTime());
            mtMaterialLotHis.setFreezeFlag(mtMaterialLot.getFreezeFlag());
            mtMaterialLotHis.setTrxPrimaryQty(mtMaterialLot.getPrimaryUomQty());
            mtMaterialLotHis.setTrxSecondaryQty(mtMaterialLot.getSecondaryUomQty());
            mtMaterialLotHis.setStocktakeFlag(mtMaterialLot.getStocktakeFlag());

            // ????????????
            mtMaterialLotHis.setMaterialLotHisId(materialLotHisIds.get(i));
            mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCids.get(i)));
            mtMaterialLotHis.setEventId(inEventId);
            mtMaterialLotHis.setSiteId(StringUtils.isEmpty(dto.getTargetSiteId()) ? mtMaterialLot.getSiteId()
                            : dto.getTargetSiteId());
            mtMaterialLotHis.setLocatorId(dto.getTargetLocatorId());
            sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialLotMerge(Long tenantId, MtMaterialLotVO10 dto) {
        // ??????????????????????????????????????????
        if (dto.getSourceMaterialLotId() == null || dto.getSourceMaterialLotId().size() == 0) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "sourceMaterialLotId", "???API:materialLotMerge???"));
        }
        // ???????????? ?????????????????????????????????
        List<MtMaterialLot> mtMaterialLots = materialLotPropertyBatchGet(tenantId, dto.getSourceMaterialLotId());
        // ??????????????????????????????????????????:???????????????????????????
        if (dto.getSourceMaterialLotId().size() != mtMaterialLots.size()) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotMerge???"));

        }
        // ??????????????????
        MtMaterialLot temp = null;
        // ?????????siteId???enableFlag???qualityStatus???materialId???locatorId???
        // ownerType???ownerId???lot???reservedFlag???reservedObjectType???reservedObjectId????????????

        // ??????ssemblePointId???????????????
        Boolean assemblePointIdIsNullFlag = true;
        // primaryUomId?????????
        Boolean supplierIdIsEqual = true;
        Boolean supplierSiteIdIsEqual = true;
        Boolean customerSiteIdIsEqual = true;
        Boolean customerIdIsEqual = true;
        // eoId?????????
        Boolean eoIdIsEqual = true;

        // primaryUomId?????????
        Boolean primaryUomIdIsEqual = true;
        BigDecimal primaryUomQtySum = BigDecimal.ZERO;

        // secondaryUomId?????????
        Boolean secondaryUomIdIsEqual = true;
        // secondaryUomId??????????????????
        Boolean secondaryUomIdIsEqualAndEmpty = false;
        BigDecimal secondaryUomQtySum = BigDecimal.ZERO;


        Map<String, MtMaterialLot> tempMap = new HashMap<String, MtMaterialLot>(0);
        temp = mtMaterialLots.get(0);
        if (StringUtils.isNotEmpty(temp.getAssemblePointId())) {
            assemblePointIdIsNullFlag = false;
        }
        tempMap.put(temp.getMaterialLotId(), temp);
        primaryUomQtySum =
                        null == temp.getPrimaryUomQty() ? BigDecimal.ZERO : BigDecimal.valueOf(temp.getPrimaryUomQty());
        secondaryUomQtySum = null == temp.getSecondaryUomQty() ? BigDecimal.ZERO
                        : BigDecimal.valueOf(temp.getSecondaryUomQty());

        for (int i = 1; i < mtMaterialLots.size(); i++) {
            MtMaterialLot lot = mtMaterialLots.get(i);
            if (temp.getSiteId() != null && !temp.getSiteId().equals(lot.getSiteId())
                            || temp.getSiteId() == null && lot.getSiteId() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0023", "MATERIAL_LOT", "siteId", "???API:materialLotMerge???"));
            }
            if (temp.getEnableFlag() != null && !temp.getEnableFlag().equals(lot.getEnableFlag())
                            || temp.getEnableFlag() == null && lot.getEnableFlag() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "enableFlag", "???API:materialLotMerge???"));
            }
            if (temp.getQualityStatus() != null && !temp.getQualityStatus().equals(lot.getQualityStatus())
                            || temp.getQualityStatus() == null && lot.getQualityStatus() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "qualityStatus", "???API:materialLotMerge???"));
            }
            if (temp.getMaterialId() != null && !temp.getMaterialId().equals(lot.getMaterialId())
                            || temp.getMaterialId() == null && lot.getMaterialId() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "materialId", "???API:materialLotMerge???"));
            }
            if (temp.getLocatorId() != null && !temp.getLocatorId().equals(lot.getLocatorId())
                            || temp.getLocatorId() == null && lot.getLocatorId() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "locatorId", "???API:materialLotMerge???"));
            }
            if (temp.getOwnerType() != null && !temp.getOwnerType().equals(lot.getOwnerType())
                            || temp.getOwnerType() == null && lot.getOwnerType() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "ownerType", "???API:materialLotMerge???"));
            }
            if (temp.getOwnerId() != null && !temp.getOwnerId().equals(lot.getOwnerId())
                            || temp.getOwnerId() == null && lot.getOwnerId() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0023", "MATERIAL_LOT", "ownerId", "???API:materialLotMerge???"));
            }
            if (temp.getLot() != null && !temp.getLot().equals(lot.getLot())
                            || temp.getLot() == null && lot.getLot() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0023", "MATERIAL_LOT", "lot", "???API:materialLotMerge???"));
            }
            if (temp.getReservedFlag() != null && !temp.getReservedFlag().equals(lot.getReservedFlag())
                            || temp.getReservedFlag() == null && lot.getReservedFlag() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "reservedFlag", "???API:materialLotMerge???"));
            }
            if (temp.getReservedObjectType() != null
                            && !temp.getReservedObjectType().equals(lot.getReservedObjectType())
                            || temp.getReservedObjectType() == null && lot.getReservedObjectType() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "reservedObjectType", "???API:materialLotMerge???"));
            }
            if (temp.getReservedObjectId() != null && !temp.getReservedObjectId().equals(lot.getReservedObjectId())
                            || temp.getReservedObjectId() == null && lot.getReservedObjectId() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "reservedObjectId", "???API:materialLotMerge???"));
            }
            if (temp.getFreezeFlag() != null && !temp.getFreezeFlag().equals(lot.getFreezeFlag())
                            || temp.getFreezeFlag() == null && lot.getFreezeFlag() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "freezeFlag", "???API:materialLotMerge???"));
            }
            if (temp.getStocktakeFlag() != null && !temp.getStocktakeFlag().equals(lot.getStocktakeFlag())
                            || temp.getStocktakeFlag() == null && lot.getStocktakeFlag() != null) {
                throw new MtException("MT_MATERIAL_LOT_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0023",
                                                "MATERIAL_LOT", "stocktakeFlag", "???API:materialLotMerge???"));
            }

            if (temp.getSupplierId() != null && !temp.getSupplierId().equals(lot.getSupplierId())
                            || temp.getSupplierId() == null && lot.getSupplierId() != null) {
                supplierIdIsEqual = false;
            }

            if (temp.getSupplierSiteId() != null && !temp.getSupplierSiteId().equals(lot.getSupplierSiteId())
                            || temp.getSupplierSiteId() == null && lot.getSupplierSiteId() != null) {
                supplierSiteIdIsEqual = false;
            }
            if (temp.getCustomerId() != null && !temp.getCustomerId().equals(lot.getCustomerId())
                            || temp.getCustomerId() == null && lot.getCustomerId() != null) {
                customerIdIsEqual = false;
            }
            if (temp.getCustomerSiteId() != null && !temp.getCustomerSiteId().equals(lot.getCustomerSiteId())
                            || temp.getCustomerSiteId() == null && lot.getCustomerSiteId() != null) {
                customerSiteIdIsEqual = false;
            }
            if (temp.getEoId() != null && !temp.getEoId().equals(lot.getEoId())
                            || temp.getEoId() == null && lot.getEoId() != null) {
                eoIdIsEqual = false;
            }

            // ???????????????????????????????????????????????????
            if ((temp.getPrimaryUomId() != null && lot.getPrimaryUomId() != null
                            && temp.getPrimaryUomId().equals(lot.getPrimaryUomId()))
                            || (temp.getPrimaryUomId() == null && lot.getPrimaryUomId() == null)) {
                primaryUomQtySum = primaryUomQtySum.add(BigDecimal.valueOf(lot.getPrimaryUomQty()));
            } else {
                primaryUomIdIsEqual = false;
            }

            // ??????????????????????????????????????????????????????????????????,?????????secondaryUomIdIsEqual=false
            if (StringUtils.isNotEmpty(temp.getSecondaryUomId()) && StringUtils.isNotEmpty(lot.getSecondaryUomId())
                            && temp.getSecondaryUomId().equals(lot.getSecondaryUomId())) {
                secondaryUomQtySum = secondaryUomQtySum.add(new BigDecimal(lot.getSecondaryUomQty().toString()));
                secondaryUomIdIsEqualAndEmpty = false;
            } else if (StringUtils.isEmpty(temp.getSecondaryUomId()) && StringUtils.isEmpty(lot.getSecondaryUomId())) {
                secondaryUomIdIsEqualAndEmpty = true;
            } else {
                secondaryUomIdIsEqualAndEmpty = false;
                secondaryUomIdIsEqual = false;
            }

            if (StringUtils.isNotEmpty(lot.getAssemblePointId())) {
                assemblePointIdIsNullFlag = false;
            }

            temp = lot;
            tempMap.put(temp.getMaterialLotId(), temp);
        }

        // ?????? assemblePointId ???????????????,??????????????????
        if (!assemblePointIdIsNullFlag) {
            throw new MtException("MT_MATERIAL_LOT_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0024", "MATERIAL_LOT", "???API:materialLotMerge???"));
        }

        // ???????????????????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_MERGE");
        eventCreateVO.setLocatorId(mtMaterialLots.get(0).getLocatorId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ?????????????????????????????????????????????????????????
        for (String materialLotId : dto.getSourceMaterialLotId()) {
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(materialLotId);
            mtMaterialLotVO2.setEnableFlag("N");
            mtMaterialLotVO2.setUnloadTime(new Date());
            mtMaterialLotVO2.setPrimaryUomQty(Double.valueOf(0D));
            MtMaterialLot lot = tempMap.get(materialLotId);
            if (StringUtils.isNotEmpty(lot.getSecondaryUomId())) {
                mtMaterialLotVO2.setSecondaryUomQty(Double.valueOf(0D));
            }
            mtMaterialLotVO2.setEventId(eventId);
            materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        }
        // ???4.1????????????????????????sourceMaterialLotId?????????MT_CONTAINER_LOAD_DETAIL???????????????
        List<MtContLoadDtlVO17> dtlVO17List =
                        mtContainerLoadDetailRepository.selectByMaterialLodIds(tenantId, dto.getSourceMaterialLotId());
        if (CollectionUtils.isNotEmpty(dtlVO17List)) {
            for (MtContLoadDtlVO17 vo17 : dtlVO17List) {
                // ??????API{ containerUnload }
                MtContainerVO25 mtContainerVO22 = new MtContainerVO25();
                mtContainerVO22.setContainerId(vo17.getContainerId());
                mtContainerVO22.setLoadObjectType("MATERIAL_LOT");
                mtContainerVO22.setLoadObjectId(vo17.getLoadObjectId());
                mtContainerVO22.setEventRequestId(dto.getEventRequestId());
                mtContainerVO22.setShiftDate(dto.getShiftDate());
                mtContainerVO22.setShiftCode(dto.getShiftCode());
                mtContainerRepository.containerUnload(tenantId, mtContainerVO22);
            }
        }
        // ?????????????????????????????????????????????????????????????????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLots.get(0);

        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotVO2);
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setMaterialLotId(null);

        MtMaterialLotVO26 mtMaterialLotVO26 = new MtMaterialLotVO26();
        mtMaterialLotVO26.setSiteId(mtMaterialLot.getSiteId());
        MtNumrangeVO5 mtNumrangeVO5 = materialLotNextCodeGet(tenantId, mtMaterialLotVO26);
        String materialLotCode = null;
        if (mtNumrangeVO5 != null) {
            materialLotCode = mtNumrangeVO5.getNumber();
        }
        mtMaterialLotVO2.setMaterialLotCode(materialLotCode);
        mtMaterialLotVO2.setAssemblePointId(null);
        mtMaterialLotVO2.setLoadTime(new Date());
        mtMaterialLotVO2.setUnloadTime(null);

        if (!primaryUomIdIsEqual) {
            mtMaterialLotVO2.setPrimaryUomId(null);
        }
        if (!secondaryUomIdIsEqual || secondaryUomIdIsEqualAndEmpty) {
            mtMaterialLotVO2.setSecondaryUomId(null);
        }

        if (!supplierIdIsEqual) {
            mtMaterialLotVO2.setSupplierId(null);
        }
        if (!supplierSiteIdIsEqual) {
            mtMaterialLotVO2.setSupplierSiteId(null);
        }
        if (!customerIdIsEqual) {
            mtMaterialLotVO2.setCustomerId(null);
        }
        if (!customerSiteIdIsEqual) {
            mtMaterialLotVO2.setCustomerSiteId(null);
        }
        mtMaterialLotVO2.setCreateReason("MERGE");
        mtMaterialLotVO2.setIdentification(null);
        if (!eoIdIsEqual) {
            mtMaterialLotVO2.setEoId(null);
        }

        // ??????????????????
        if (!primaryUomIdIsEqual) {
            mtMaterialLotVO2.setPrimaryUomId(null);
            MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
            if (mtMaterialVO1 == null) {
                throw new MtException("MT_MATERIAL_LOT_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0016", "MATERIAL_LOT", "???API:materialLotMerge???"));
            }

            primaryUomQtySum = BigDecimal.ZERO;
            for (MtMaterialLot lot : mtMaterialLots) {
                if (lot.getPrimaryUomId().equals(mtMaterialVO1.getPrimaryUomId())) {
                    primaryUomQtySum = primaryUomQtySum.add(new BigDecimal(lot.getPrimaryUomQty().toString()));
                } else {
                    // ????????????
                    MtUomVO1 transferUomVO = new MtUomVO1();
                    transferUomVO.setSourceUomId(lot.getPrimaryUomId());
                    transferUomVO.setSourceValue(lot.getPrimaryUomQty());
                    transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
                    transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                    primaryUomQtySum = primaryUomQtySum.add(new BigDecimal(transferUomVO.getTargetValue().toString()));
                }
            }
        }

        if (secondaryUomIdIsEqualAndEmpty) {
            mtMaterialLotVO2.setSecondaryUomQty(null);
        } else {
            if (!secondaryUomIdIsEqual) {
                MtMaterialVO1 mtMaterialVO1 =
                                mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
                secondaryUomQtySum = BigDecimal.ZERO;
                for (MtMaterialLot lot : mtMaterialLots) {
                    if (lot.getSecondaryUomId().equals(mtMaterialVO1.getSecondaryUomId())) {
                        secondaryUomQtySum =
                                        secondaryUomQtySum.add(new BigDecimal(lot.getSecondaryUomQty().toString()));
                    } else {
                        // ????????????
                        MtUomVO1 transferUomVO = new MtUomVO1();
                        transferUomVO.setSourceUomId(lot.getSecondaryUomId());
                        transferUomVO.setSourceValue(lot.getSecondaryUomQty());
                        transferUomVO.setTargetUomId(mtMaterialVO1.getSecondaryUomId());
                        transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                        secondaryUomQtySum = secondaryUomQtySum
                                        .add(new BigDecimal(transferUomVO.getTargetValue().toString()));
                    }
                }
            }

            secondaryUomQtySum = null == secondaryUomQtySum ? BigDecimal.ZERO : secondaryUomQtySum;
            mtMaterialLotVO2.setSecondaryUomQty(secondaryUomQtySum.doubleValue());
        }

        primaryUomQtySum = null == primaryUomQtySum ? BigDecimal.ZERO : primaryUomQtySum;
        mtMaterialLotVO2.setPrimaryUomQty(primaryUomQtySum.doubleValue());

        mtMaterialLotVO2.setCurrentContainerId("");
        mtMaterialLotVO2.setTopContainerId("");
        mtMaterialLotVO2.setInSiteTime(new Date());
        mtMaterialLotVO2.setInLocatorTime(new Date());
        mtMaterialLotVO2.setInstructionDocId("");

        MtMaterialLotVO13 mtMaterialLotVO13 = materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

        String materialLotId = mtMaterialLotVO13.getMaterialLotId();

        // ???????????????????????????????????????????????????
        Long sequence = Long.valueOf(0L);
        for (String lot : dto.getSourceMaterialLotId()) {
            sequence += 10;
            MtMaterialLotChangeHistory mtMaterialLotChangeHistory = new MtMaterialLotChangeHistory();
            mtMaterialLotChangeHistory.setMaterialLotId(materialLotId);
            mtMaterialLotChangeHistory.setSourceMaterialLotId(lot);
            mtMaterialLotChangeHistory.setReason("MERGE");
            mtMaterialLotChangeHistory.setSequence(sequence);
            mtMaterialLotChangeHistory.setEventId(eventId);
            mtMaterialLotChangeHistory.setTrxQty(primaryUomQtySum.doubleValue());
            mtMaterialLotChangeHistory.setSourceTrxQty(-tempMap.get(lot).getPrimaryUomQty());
            mtMaterialLotChangeHistoryRepository.materialLotChangeHistoryCreate(tenantId, mtMaterialLotChangeHistory);

        }
        return materialLotId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialLotSplit(Long tenantId, MtMaterialVO3 dto) {
        // 1.?????????????????????????????????????????????
        if (StringUtils.isEmpty(dto.getSourceMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "sourceMaterialLotId", "???API:materialLotSplit???"));
        }
        if (null == dto.getSplitPrimaryQty()) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "splitPrimaryQty", "???API:materialLotSplit???"));
        }
        // SplitPrimaryQty<=0??????
        if (new BigDecimal(dto.getSplitPrimaryQty().toString()).compareTo(BigDecimal.ZERO) != 1) {
            throw new MtException("MT_MATERIAL_LOT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0007", "MATERIAL_LOT", "splitPrimaryQty", "???API:materialLotSplit???"));
        }

        // ???????????? ???????????????????????????
        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, dto.getSourceMaterialLotId());
        if (mtMaterialLot == null) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotSplit???"));
        }
        // ??????
        // SplitPrimaryQty>=PrimaryUomQty??????
        // 2020-10-10 11:27 edit by chaonan.hu for yiwei.zhou SplitPrimaryQty>PrimaryUomQty??????
        if (new BigDecimal(dto.getSplitPrimaryQty().toString())
                        .compareTo(new BigDecimal(mtMaterialLot.getPrimaryUomQty().toString())) == 1) {
            throw new MtException("MT_MATERIAL_LOT_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0021", "MATERIAL_LOT", "???API:materialLotSplit???"));
        }
        // ??????????????????????????????????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId()) && null != mtMaterialLot.getSecondaryUomQty()
                        && dto.getSplitSecondaryQty() == null) {
            throw new MtException("MT_MATERIAL_LOT_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0022", "MATERIAL_LOT", "???API:materialLotSplit???"));
        }

        if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId()) && null != mtMaterialLot.getSecondaryUomQty()) {
            // ?????????0??????SplitSecondaryQty<=0
            if (new BigDecimal(dto.getSplitSecondaryQty().toString()).compareTo(BigDecimal.ZERO) != 1) {
                throw new MtException("MT_MATERIAL_LOT_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0007",
                                                "MATERIAL_LOT", "splitSecondaryQty", "???API:materialLotSplit???"));
            }
            // SplitSecondaryQty>=SecondaryUomQty??????
            // 2020-10-10 11:27 edit by chaonan.hu for yiwei.zhou SplitPrimaryQty>PrimaryUomQty??????
            if (new BigDecimal(dto.getSplitSecondaryQty().toString())
                            .compareTo(new BigDecimal(mtMaterialLot.getSecondaryUomQty().toString())) == 1) {
                throw new MtException("MT_MATERIAL_LOT_0021", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0021", "MATERIAL_LOT", "???API:materialLotSplit???"));

            }
        }

        // ???????????????????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_SPLIT");
        eventCreateVO.setLocatorId(mtMaterialLot.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ?????????????????????????????????????????????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(dto.getSourceMaterialLotId());
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setTrxPrimaryUomQty(-dto.getSplitPrimaryQty());

        if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId())) {
            mtMaterialLotVO2.setTrxSecondaryUomQty(-dto.getSplitSecondaryQty());
        }
        materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

        // ?????????????????????????????????????????????????????????????????????????????????
        mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setSiteId(mtMaterialLot.getSiteId());
        mtMaterialLotVO2.setEnableFlag(mtMaterialLot.getEnableFlag());
        mtMaterialLotVO2.setQualityStatus(mtMaterialLot.getQualityStatus());
        mtMaterialLotVO2.setMaterialId(mtMaterialLot.getMaterialId());
        mtMaterialLotVO2.setPrimaryUomId(mtMaterialLot.getPrimaryUomId());
        mtMaterialLotVO2.setTrxPrimaryUomQty(dto.getSplitPrimaryQty());
        mtMaterialLotVO2.setSecondaryUomId(mtMaterialLot.getSecondaryUomId());
        if (StringUtils.isEmpty(mtMaterialLot.getSecondaryUomId())) {
            mtMaterialLotVO2.setSecondaryUomQty(null);
        } else {
            mtMaterialLotVO2.setSecondaryUomQty(dto.getSplitSecondaryQty());
        }
        mtMaterialLotVO2.setLocatorId(mtMaterialLot.getLocatorId());
        mtMaterialLotVO2.setLoadTime(mtMaterialLot.getLoadTime());
        mtMaterialLotVO2.setUnloadTime(mtMaterialLot.getUnloadTime());
        mtMaterialLotVO2.setOwnerId(mtMaterialLot.getOwnerId());
        mtMaterialLotVO2.setOwnerType(mtMaterialLot.getOwnerType());
        mtMaterialLotVO2.setLot(mtMaterialLot.getLot());
        mtMaterialLotVO2.setOvenNumber(mtMaterialLot.getOvenNumber());
        mtMaterialLotVO2.setSupplierId(mtMaterialLot.getSupplierId());
        mtMaterialLotVO2.setSupplierSiteId(mtMaterialLot.getSupplierSiteId());
        mtMaterialLotVO2.setCustomerId(mtMaterialLot.getCustomerId());
        mtMaterialLotVO2.setCustomerSiteId(mtMaterialLot.getCustomerSiteId());
        mtMaterialLotVO2.setReservedFlag(mtMaterialLot.getReservedFlag());
        mtMaterialLotVO2.setReservedObjectId(mtMaterialLot.getReservedObjectId());
        mtMaterialLotVO2.setReservedObjectType(mtMaterialLot.getReservedObjectType());
        mtMaterialLotVO2.setCreateReason("SPLIT");
        mtMaterialLotVO2.setEoId(mtMaterialLot.getEoId());
        mtMaterialLotVO2.setInLocatorTime(mtMaterialLot.getInLocatorTime());
        mtMaterialLotVO2.setFreezeFlag(mtMaterialLot.getFreezeFlag());
        mtMaterialLotVO2.setStocktakeFlag(mtMaterialLot.getStocktakeFlag());
        mtMaterialLotVO2.setInSiteTime(mtMaterialLot.getInSiteTime());
        mtMaterialLotVO2.setInstructionDocId(mtMaterialLot.getInstructionDocId());
        mtMaterialLotVO2.setCurrentContainerId(mtMaterialLot.getCurrentContainerId());
        mtMaterialLotVO2.setOutsideNum(dto.getOutsideNum());
        mtMaterialLotVO2.setIncomingValueList(dto.getIncomingValueList());
        // 2020-09-09 16:30 edit by chonan.hu for yiwei.zhou
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getSplitMaterialLotCode())) {
            mtMaterialLotVO2.setMaterialLotCode(dto.getSplitMaterialLotCode());
        }

        MtMaterialLotVO13 mtMaterialLotVO13 = materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        String materialLotId = mtMaterialLotVO13.getMaterialLotId();

        // ????????????5.1???????????????????????????
        List<MtContainerLoadDetail> mtContainerLoadDetails = mtContainerLoadDetailRepository
                        .loadObjectLimitBatchGet(tenantId, "MATERIAL_LOT", Arrays.asList(dto.getSourceMaterialLotId()));
        if (CollectionUtils.isNotEmpty(mtContainerLoadDetails)) {

            // ???????????????????????????
            MtContainerLoadDetail mtContainerLoadDetail = mtContainerLoadDetails.get(0);
            if (mtContainerLoadDetail.getLocationRow() != null && dto.getLocationRow() == null) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "locationRow", "???API:materialLotSplit???"));
            }
            if (mtContainerLoadDetail.getLocationColumn() != null && dto.getLocationColumn() == null) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "locationColumn", "???API:materialLotSplit???"));
            }

            // ????????????????????????????????????
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(mtContainerLoadDetail.getContainerId());
            mtContainerVO24.setLocationRow(dto.getLocationRow());
            mtContainerVO24.setLocationColumn(dto.getLocationColumn());
            mtContainerVO24.setLoadObjectType("MATERIAL_LOT");
            mtContainerVO24.setLoadObjectId(materialLotId);
            mtContainerVO24.setWorkcellId(dto.getWorkcellId());
            mtContainerVO24.setParentEventId(dto.getParentEventId());
            mtContainerVO24.setEventRequestId(dto.getEventRequestId());
            mtContainerVO24.setShiftDate(dto.getShiftDate());
            mtContainerVO24.setShiftCode(dto.getShiftCode());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        // 6.???????????????????????????????????????????????????
        MtMaterialLotChangeHistory mtMaterialLotChangeHistory = new MtMaterialLotChangeHistory();
        mtMaterialLotChangeHistory.setMaterialLotId(materialLotId);
        mtMaterialLotChangeHistory.setSourceMaterialLotId(dto.getSourceMaterialLotId());
        mtMaterialLotChangeHistory.setReason("SPLIT");
        mtMaterialLotChangeHistory.setSequence(Long.valueOf(10L));
        mtMaterialLotChangeHistory.setEventId(eventId);
        mtMaterialLotChangeHistory.setTrxQty(dto.getSplitPrimaryQty());
        mtMaterialLotChangeHistory.setSourceTrxQty(-dto.getSplitPrimaryQty());
        mtMaterialLotChangeHistoryRepository.materialLotChangeHistoryCreate(tenantId, mtMaterialLotChangeHistory);
        return materialLotId;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<MtExtendAttrVO> materialLotLimitAttrQuery(Long tenantId, MtMaterialLotAttrVO2 dto) {

        if (dto == null || StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "???API:materialLotLimitAttrQuery???"));
        }

        // ????????????????????????????????????
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_material_lot_attr");
        extendVO.setKeyId(dto.getMaterialLotId());
        extendVO.setAttrName(dto.getAttrName());
        return mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLotLimitAttrUpdate(Long tenantId, MtMaterialLotAttrVO3 dto) {
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(),
                        dto.getEventId(), dto.getAttr());
    }

    @Override
    public List<String> attrLimitMaterialLotQuery(Long tenantId, MtMaterialLotAttrVO1 dto) {

        if (CollectionUtils.isEmpty(dto.getAttr())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "attrName", "???API:attrLimitMaterialLotQuery???"));

        }

        // ??????attrName????????????
        for (MtExtendAttrVO attr : dto.getAttr()) {
            if (StringUtils.isEmpty(attr.getAttrName())) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "attrName", "???API:attrLimitMaterialLotQuery???"));

            }
        }

        Map<String, String> property = dto.getAttr().stream().collect(Collectors.toMap(t -> t.getAttrName(),
                        t -> t.getAttrValue() == null ? "" : t.getAttrValue(), (k1, k2) -> k1));
        List<String> eoIds = mtExtendSettingsRepository.attrBatchPropertyLimitKidQuery(tenantId, "mt_material_lot_attr",
                        property);
        // ????????????????????????
        if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            eoIds = eoIds.stream().filter(t -> dto.getMaterialLotId().equals(t)).collect(Collectors.toList());
        }

        return eoIds;
    }

    @Override
    public List<MtMaterialLotAttrHisVO2> materialLotAttrHisQuery(Long tenantId, MtMaterialLotAttrHisVO1 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getEventId()) && StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                            "MATERIAL_LOT", "materialLotId???eventId", "???API:materialLotAttrHisQuery???"));

        }
        MtExtendAttrHisVO2 query = new MtExtendAttrHisVO2();
        BeanUtils.copyProperties(dto, query);
        query.setKid(dto.getMaterialLotId());
        List<MtMaterialLotAttrHisVO2> result = new ArrayList<MtMaterialLotAttrHisVO2>();
        List<MtExtendAttrHisVO> attrHisList =
                        mtExtendSettingsRepository.attrHisQuery(tenantId, query, "mt_material_lot_attr");
        attrHisList.stream().forEach(t -> {
            MtMaterialLotAttrHisVO2 mtEoAttrHisVO1 = new MtMaterialLotAttrHisVO2();
            BeanUtils.copyProperties(t, mtEoAttrHisVO1);
            mtEoAttrHisVO1.setMaterialLotId(t.getKid());
            result.add(mtEoAttrHisVO1);
        });
        return result;
    }

    @Override
    public List<MtMaterialLotAttrHisVO2> eventLimitMaterialLotAttrHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventId", "???API:eventLimitMaterialLotAttrHisBatchQuery???"));
        }
        List<MtMaterialLotAttrHisVO2> result = new ArrayList<MtMaterialLotAttrHisVO2>();
        List<MtExtendAttrHisVO> attrHisList =
                        mtExtendSettingsRepository.attrHisBatchQuery(tenantId, eventIds, "mt_material_lot_attr");
        attrHisList.stream().forEach(t -> {
            MtMaterialLotAttrHisVO2 mtEoAttrHisVO1 = new MtMaterialLotAttrHisVO2();
            BeanUtils.copyProperties(t, mtEoAttrHisVO1);
            mtEoAttrHisVO1.setMaterialLotId(t.getKid());
            result.add(mtEoAttrHisVO1);
        });

        return result;
    }

    @Override
    public String materialLotStocktakeVerify(Long tenantId, String materialLotId) {
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "???API:materialLotStocktakeVerify???"));
        }

        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, materialLotId);
        if (null == mtMaterialLot) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotStocktakeVerify???"));
        }

        return StringUtils.isEmpty(mtMaterialLot.getStocktakeFlag()) ? "N" : mtMaterialLot.getStocktakeFlag();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLotOwnerTransfer(Long tenantId, MtMaterialLotVO12 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "???API:materialLotOwnerTransfer???"));
        }

        // 2.????????????????????????????????????????????????
        // 2.a ?????????????????????
        MtMaterialLot mtMaterialLot = materialLotPropertyGet(tenantId, dto.getMaterialLotId());
        if (mtMaterialLot == null || !"Y".equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotOwnerTransfer???"));
        }

        if ("Y".equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0072",
                                            "MATERIAL_LOT", dto.getMaterialLotId(), "???API:materialLotOwnerTransfer???"));
        }

        // ???????????????????????????
        if (dto.getTargetOwnerType() == null || dto.getTargetOwnerType().equals(mtMaterialLot.getOwnerType())) {
            String targetOwnerId = null == dto.getTargetOwnerId() ? "" : dto.getTargetOwnerId();
            String ownerId = null == mtMaterialLot.getOwnerId() ? "" : mtMaterialLot.getOwnerId();
            if (targetOwnerId.equals(ownerId)) {
                throw new MtException("MT_MATERIAL_LOT_0073", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0073", "MATERIAL_LOT", "???API:materialLotOwnerTransfer???"));
            }
        }

        // 3.???????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_OWNER_TRANSFER_OUT");
        eventCreateVO.setLocatorId(mtMaterialLot.getLocatorId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventOutId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_OWNER_TRANSFER_IN");
        eventCreateVO.setLocatorId(mtMaterialLot.getLocatorId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setParentEventId(eventOutId);
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventInId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ?????????????????????????????????????????????????????????
        MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
        Double primaryUomQty = null;
        if (mtMaterialVO1.getPrimaryUomId().equals(mtMaterialLot.getPrimaryUomId())) {
            primaryUomQty = mtMaterialLot.getPrimaryUomQty();
        } else {
            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
            transferUomVO.setSourceValue(mtMaterialLot.getPrimaryUomQty());
            transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
            primaryUomQty = transferUomVO.getTargetValue();
        }

        // 5.???????????????????????????????????????????????????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO2.setEventId(eventOutId);
        materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

        // ???????????????????????????????????????????????????
        if ("Y".equals(mtMaterialLot.getReservedFlag())) {
            // ??????????????????
            MtInvOnhandHoldVO7 onhandReleaseVO1 = new MtInvOnhandHoldVO7();
            onhandReleaseVO1.setSiteId(mtMaterialLot.getSiteId());
            onhandReleaseVO1.setMaterialId(mtMaterialLot.getMaterialId());
            onhandReleaseVO1.setLocatorId(mtMaterialLot.getLocatorId());
            onhandReleaseVO1.setLotCode(mtMaterialLot.getLot());
            onhandReleaseVO1.setOwnerType(mtMaterialLot.getOwnerType());
            onhandReleaseVO1.setOwnerId(mtMaterialLot.getOwnerId());
            onhandReleaseVO1.setQuantity(primaryUomQty);
            onhandReleaseVO1.setHoldType("ORDER");
            onhandReleaseVO1.setOrderType(mtMaterialLot.getReservedObjectType());
            onhandReleaseVO1.setOrderId(mtMaterialLot.getReservedObjectId());
            onhandReleaseVO1.setEventId(eventOutId);
            mtInvOnhandHoldRepository.onhandReserveUseProcess(tenantId, onhandReleaseVO1);
        } else {
            MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
            updateOnhandVO.setSiteId(mtMaterialLot.getSiteId());
            updateOnhandVO.setMaterialId(mtMaterialLot.getMaterialId());
            updateOnhandVO.setLocatorId(mtMaterialLot.getLocatorId());
            updateOnhandVO.setLotCode(mtMaterialLot.getLot());
            updateOnhandVO.setOwnerType(mtMaterialLot.getOwnerType());
            updateOnhandVO.setOwnerId(mtMaterialLot.getOwnerId());
            updateOnhandVO.setChangeQuantity(primaryUomQty);
            updateOnhandVO.setEventId(eventOutId);
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
        }

        // ???????????????????????????????????????????????????????????????
        MtMaterialLotVO2 mtMaterialLotVO21 = new MtMaterialLotVO2();
        mtMaterialLotVO21.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO21.setOwnerType(null == dto.getTargetOwnerType() ? "" : dto.getTargetOwnerType());
        mtMaterialLotVO21.setOwnerId(null == dto.getTargetOwnerId() ? "" : dto.getTargetOwnerId());
        mtMaterialLotVO21.setEventId(eventInId);
        materialLotUpdate(tenantId, mtMaterialLotVO21, "N");

        // ???????????????????????????????????????????????????
        MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
        updateOnhandVO.setSiteId(mtMaterialLot.getSiteId());
        updateOnhandVO.setMaterialId(mtMaterialLot.getMaterialId());
        updateOnhandVO.setLocatorId(mtMaterialLot.getLocatorId());
        updateOnhandVO.setLotCode(mtMaterialLot.getLot());
        updateOnhandVO.setOwnerType(dto.getTargetOwnerType());
        updateOnhandVO.setOwnerId(dto.getTargetOwnerId());
        updateOnhandVO.setChangeQuantity(primaryUomQty);
        updateOnhandVO.setEventId(eventInId);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);

        if ("Y".equals(mtMaterialLot.getReservedFlag())) {
            // ??????????????????
            MtInvOnhandHoldVO8 onhandReleaseVO2 = new MtInvOnhandHoldVO8();
            onhandReleaseVO2.setSiteId(mtMaterialLot.getSiteId());
            onhandReleaseVO2.setMaterialId(mtMaterialLot.getMaterialId());
            onhandReleaseVO2.setLocatorId(mtMaterialLot.getLocatorId());
            onhandReleaseVO2.setLotCode(mtMaterialLot.getLot());
            onhandReleaseVO2.setOwnerType(dto.getTargetOwnerType());
            onhandReleaseVO2.setOwnerId(dto.getTargetOwnerId());
            onhandReleaseVO2.setHoldQuantity(primaryUomQty);
            onhandReleaseVO2.setHoldType("ORDER");
            onhandReleaseVO2.setOrderType(mtMaterialLot.getReservedObjectType());
            onhandReleaseVO2.setOrderId(mtMaterialLot.getReservedObjectId());
            onhandReleaseVO2.setEventId(eventInId);
            mtInvOnhandHoldRepository.onhandReserveCreateProcess(tenantId, onhandReleaseVO2);
        }
    }

    /**
     * containerLimitMaterialLotLoadSequenceQuery-???????????????????????????????????????
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.material_lot.view.MtMaterialLotVO18>
     */
    @Override
    public List<MtMaterialLotVO18> containerLimitMaterialLotLoadSequenceQuery(Long tenantId, MtMaterialLotVO17 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId",
                                            "???API:containerLimitMaterialLotLoadSequenceQuery???"));
        }

        return containerLimitMaterialLotSequenceQueryRecursion(tenantId, dto.getContainerId(), dto.getAllLevelFlag(),
                        0);
    }

    @Override
    public List<MtMaterialLotVO22> propertyLimitMaterialLotPropertyQuery(Long tenantId, MtMaterialLotVO21 dto) {
        List<MtMaterialLotVO22> voList = mtMaterialLotMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        List<String> siteIdList = voList.stream().map(MtMaterialLotVO22::getSiteId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(Collectors.toList());
        List<MtModSite> mtModSites = null;
        if (CollectionUtils.isNotEmpty(siteIdList)) {
            mtModSites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIdList);
        }

        List<MtModLocator> mtModLocators = null;
        List<String> locatorIdList = voList.stream().map(MtMaterialLotVO22::getLocatorId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(locatorIdList)) {
            mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIdList);
        }

        List<String> materialIdList = voList.stream().map(MtMaterialLotVO22::getMaterialId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<MtMaterialVO> mtMaterialVOS = null;
        if (CollectionUtils.isNotEmpty(materialIdList)) {
            mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIdList);
        }

        List<String> assemblePointIdList = voList.stream().map(MtMaterialLotVO22::getAssemblePointId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<MtAssemblePoint> pointList = null;
        if (CollectionUtils.isNotEmpty(assemblePointIdList)) {
            pointList = mtAssemblePointRepository.assemblePointPropertyBatchGet(tenantId, assemblePointIdList);
        }

        List<String> primaryUomIdList = voList.stream().map(MtMaterialLotVO22::getPrimaryUomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<MtUomVO> primaryUomVOS = null;
        if (CollectionUtils.isNotEmpty(primaryUomIdList)) {
            primaryUomVOS = mtUomRepository.uomPropertyBatchGet(tenantId, primaryUomIdList);
        }

        List<String> secondaryUomIdList = voList.stream().map(MtMaterialLotVO22::getSecondaryUomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<MtUomVO> secondaryUomVOS = null;
        if (CollectionUtils.isNotEmpty(secondaryUomIdList)) {
            secondaryUomVOS = mtUomRepository.uomPropertyBatchGet(tenantId, secondaryUomIdList);
        }

        Map<String, MtModSite> mtModSiteMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtModSites)) {
            mtModSiteMap = mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
        }

        Map<String, MtModLocator> modLocatorMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtModLocators)) {
            modLocatorMap = mtModLocators.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, t -> t));
        }

        Map<String, MtMaterialVO> mtMaterialVOMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
            mtMaterialVOMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, t -> t));
        }

        Map<String, MtAssemblePoint> mtAssemblePointMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(pointList)) {
            mtAssemblePointMap =
                            pointList.stream().collect(Collectors.toMap(MtAssemblePoint::getAssemblePointId, t -> t));
        }

        Map<String, MtUomVO> primaryVOMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(primaryUomVOS)) {
            primaryVOMap = primaryUomVOS.stream().collect(Collectors.toMap(MtUom::getUomId, t -> t));
        }

        Map<String, MtUomVO> secondaryVOMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(secondaryUomVOS)) {
            secondaryVOMap = secondaryUomVOS.stream().collect(Collectors.toMap(MtUom::getUomId, t -> t));
        }
        for (MtMaterialLotVO22 lotVO22 : voList) {
            lotVO22.setSiteCode(null == mtModSiteMap.get(lotVO22.getSiteId()) ? null
                            : mtModSiteMap.get(lotVO22.getSiteId()).getSiteCode());
            lotVO22.setSiteName(null == mtModSiteMap.get(lotVO22.getSiteId()) ? null
                            : mtModSiteMap.get(lotVO22.getSiteId()).getSiteName());
            lotVO22.setLocatorCode(null == modLocatorMap.get(lotVO22.getLocatorId()) ? null
                            : modLocatorMap.get(lotVO22.getLocatorId()).getLocatorCode());
            lotVO22.setLocatorName(null == modLocatorMap.get(lotVO22.getLocatorId()) ? null
                            : modLocatorMap.get(lotVO22.getLocatorId()).getLocatorName());
            lotVO22.setMaterialCode(null == mtMaterialVOMap.get(lotVO22.getMaterialId()) ? null
                            : mtMaterialVOMap.get(lotVO22.getMaterialId()).getMaterialCode());
            lotVO22.setMaterialName(null == mtMaterialVOMap.get(lotVO22.getMaterialId()) ? null
                            : mtMaterialVOMap.get(lotVO22.getMaterialId()).getMaterialName());
            lotVO22.setAssemblePointCode(null == mtAssemblePointMap.get(lotVO22.getAssemblePointId()) ? null
                            : mtAssemblePointMap.get(lotVO22.getAssemblePointId()).getAssemblePointCode());
            lotVO22.setAssemblePointDescription(null == mtAssemblePointMap.get(lotVO22.getAssemblePointId()) ? null
                            : mtAssemblePointMap.get(lotVO22.getAssemblePointId()).getDescription());
            lotVO22.setPrimaryUomCode(null == primaryVOMap.get(lotVO22.getPrimaryUomId()) ? null
                            : primaryVOMap.get(lotVO22.getPrimaryUomId()).getUomCode());
            lotVO22.setPrimaryUomName(null == primaryVOMap.get(lotVO22.getPrimaryUomId()) ? null
                            : primaryVOMap.get(lotVO22.getPrimaryUomId()).getUomName());
            lotVO22.setSecondaryUomCode(null == secondaryVOMap.get(lotVO22.getSecondaryUomId()) ? null
                            : secondaryVOMap.get(lotVO22.getSecondaryUomId()).getUomCode());
            lotVO22.setSecondaryUomName(null == secondaryVOMap.get(lotVO22.getSecondaryUomId()) ? null
                            : secondaryVOMap.get(lotVO22.getSecondaryUomId()).getUomName());

        }
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtMaterialLotVO13 materialLotInitialize(Long tenantId, MtMaterialLotVO23 dto) {
        MtMaterialLotVO13 result = null;
        // 1????????????
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "???API:materialLotInitialize???"));
        }
        if (null != dto.getPrimaryUomQty()
                        && new BigDecimal(dto.getPrimaryUomQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_MATERIAL_LOT_0094", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0094", "MATERIAL_LOT", "???API:materialLotInitialize???"));
        }
        if (null != dto.getSecondaryUomQty()
                        && new BigDecimal(dto.getSecondaryUomQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_MATERIAL_LOT_0094", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0094", "MATERIAL_LOT", "???API:materialLotInitialize???"));
        }
        // 2???????????????????????????????????????API{eventCreate}????????????eventId
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_INITIALIZE");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 3????????????????????????MT_MATERIAL_LOT???????????????
        MtMaterialLot materialLot = new MtMaterialLot();
        materialLot.setTenantId(tenantId);
        materialLot.setMaterialLotId(dto.getMaterialLotId());
        materialLot = mtMaterialLotMapper.selectOne(materialLot);
        if (null != materialLot) {
            if ("Y".equals(materialLot.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_LOT_0076", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0076", "MATERIAL_LOT", "???API:materialLotInitialize???"));
            } else if ("N".equals(materialLot.getEnableFlag())) {
                // 4??????????????????????????????API{materialLotUpdate}
                MtMaterialLotVO2 materialLotVO2 = new MtMaterialLotVO2();
                materialLotVO2.setMaterialLotId(dto.getMaterialLotId());
                materialLotVO2.setEventId(eventId);
                materialLotVO2.setEnableFlag("Y");
                materialLotVO2.setQualityStatus(dto.getQualityStatus());
                materialLotVO2.setPrimaryUomId(dto.getPrimaryUomId());
                materialLotVO2.setPrimaryUomQty(dto.getPrimaryUomQty());
                materialLotVO2.setSecondaryUomId(dto.getSecondaryUomId());
                materialLotVO2.setSecondaryUomQty(dto.getSecondaryUomQty());
                materialLotVO2.setLocatorId(dto.getLocatorId());
                materialLotVO2.setInLocatorTime(new Date());
                materialLotVO2.setReservedFlag(dto.getReservedFlag());
                materialLotVO2.setReservedObjectId(dto.getReservedObjectId());
                materialLotVO2.setReservedObjectType(dto.getReservedObjectType());
                materialLotVO2.setInLocatorTime(new Date());
                materialLotVO2.setInSiteTime(new Date());
                materialLotVO2.setCurrentContainerId("");
                materialLotVO2.setTopContainerId("");
                materialLotVO2.setInstructionDocId(dto.getInstructionDocId());
                result = self().materialLotUpdate(tenantId, materialLotVO2, "N");
                // 5.????????????????????????MT_MATERIAL_LOT???????????????????????????
                materialLot = new MtMaterialLot();
                materialLot.setTenantId(tenantId);
                materialLot.setMaterialLotId(dto.getMaterialLotId());
                materialLot = mtMaterialLotMapper.selectOne(materialLot);
                // 6?????????????????? primaryomQty ???0?????????
                if (new BigDecimal(materialLot.getPrimaryUomQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
                    throw new MtException("MT_MATERIAL_LOT_0094", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0094", "MATERIAL_LOT", "???API:materialLotInitialize???"));
                }
                // ??????????????????????????????????????????
                MtMaterialVO1 uomGet = mtMaterialRepository.materialUomGet(tenantId, materialLot.getMaterialId());
                MtUomVO1 uomVO1 = new MtUomVO1();
                uomVO1.setSourceUomId(materialLot.getPrimaryUomId());
                uomVO1.setSourceValue(materialLot.getPrimaryUomQty());
                uomVO1.setTargetUomId(uomGet.getPrimaryUomId());
                uomVO1 = mtUomRepository.uomConversion(tenantId, uomVO1);
                // ??????API{onhandQtyUpdateProcess }???????????????????????????
                MtInvOnhandQuantityVO9 vo9 = new MtInvOnhandQuantityVO9();
                vo9.setSiteId(materialLot.getSiteId());
                vo9.setMaterialId(materialLot.getMaterialId());
                vo9.setLocatorId(materialLot.getLocatorId());
                vo9.setLotCode(materialLot.getLot());
                vo9.setChangeQuantity(uomVO1.getTargetValue());
                vo9.setEventId(eventId);
                vo9.setOwnerId(materialLot.getOwnerId());
                vo9.setOwnerType(materialLot.getOwnerType());
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, vo9);
                // ??????API{onhandReserveCreateProcess}????????????????????????
                if ("Y".equals(materialLot.getReservedFlag())) {
                    MtInvOnhandHoldVO8 onhandReleaseVO2 = new MtInvOnhandHoldVO8();
                    onhandReleaseVO2.setSiteId(materialLot.getSiteId());
                    onhandReleaseVO2.setMaterialId(materialLot.getMaterialId());
                    onhandReleaseVO2.setLocatorId(materialLot.getLocatorId());
                    onhandReleaseVO2.setLotCode(materialLot.getLot());
                    onhandReleaseVO2.setHoldQuantity(uomVO1.getTargetValue());
                    onhandReleaseVO2.setOwnerType(materialLot.getOwnerType());
                    onhandReleaseVO2.setOwnerId(materialLot.getOwnerId());
                    onhandReleaseVO2.setHoldType("ORDER");
                    onhandReleaseVO2.setOrderType(materialLot.getReservedObjectType());
                    onhandReleaseVO2.setOrderId(materialLot.getReservedObjectId());
                    onhandReleaseVO2.setEventRequestId(dto.getEventRequestId());
                    onhandReleaseVO2.setShiftDate(dto.getShiftDate());
                    onhandReleaseVO2.setShiftCode(dto.getShiftCode());
                    onhandReleaseVO2.setEventId(eventId);
                    mtInvOnhandHoldRepository.onhandReserveCreateProcess(tenantId, onhandReleaseVO2);
                } else if ("N".equals(materialLot.getReservedFlag())) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * ??????????????????????????????????????? ????????????
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param tenantId
     * @param containerId
     * @param allLevelFlag
     * @param lastSequence
     * @return java.util.List<hmes.material_lot.view.MtMaterialLotVO18>
     */
    private List<MtMaterialLotVO18> containerLimitMaterialLotSequenceQueryRecursion(Long tenantId, String containerId,
                    String allLevelFlag, int lastSequence) {
        int currentSequence = lastSequence;

        // 2. ??????????????????, ??????????????????????????????
        List<MtContainerLoadDetail> containerLoadDetailList =
                        mtContainerLoadDetailRepository.containerLimitMaterialLotAndContainer(tenantId, containerId);

        List<MtMaterialLotVO18> resultList = new ArrayList<>();

        // 3. ??????????????????allLevelFlag
        if (!"Y".equals(allLevelFlag)) {
            // ??????????????????????????????
            List<MtContainerLoadDetail> materialLotDetailList = containerLoadDetailList.stream()
                            .filter(t -> "MATERIAL_LOT".equals(t.getLoadObjectType())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(materialLotDetailList)) {
                for (int i = 0; i < materialLotDetailList.size(); i++) {
                    MtContainerLoadDetail materialLot = materialLotDetailList.get(i);

                    MtMaterialLotVO18 result = new MtMaterialLotVO18();
                    result.setContainerId(materialLot.getContainerId());
                    // ????????????????????????10???????????????????????????10,20,30
                    result.setLoadSequence(Long.valueOf((i + 1) * 10));
                    result.setLocationColumn(materialLot.getLocationColumn());
                    result.setLocationRow(materialLot.getLocationRow());
                    result.setMaterialLotId(materialLot.getLoadObjectId());
                    resultList.add(result);
                }
            }

            return resultList;
        } else {
            for (MtContainerLoadDetail loadDetail : containerLoadDetailList) {
                if ("MATERIAL_LOT".equals(loadDetail.getLoadObjectType())) {
                    // ????????????????????????10???????????????????????????10,20,30
                    currentSequence += 10;

                    MtMaterialLotVO18 result = new MtMaterialLotVO18();
                    result.setContainerId(loadDetail.getContainerId());
                    result.setLoadSequence(Long.valueOf(currentSequence));
                    result.setLocationColumn(loadDetail.getLocationColumn());
                    result.setLocationRow(loadDetail.getLocationRow());
                    result.setMaterialLotId(loadDetail.getLoadObjectId());
                    resultList.add(result);
                } else {
                    List<MtMaterialLotVO18> recursionList = containerLimitMaterialLotSequenceQueryRecursion(tenantId,
                                    loadDetail.getLoadObjectId(), allLevelFlag, currentSequence);
                    if (CollectionUtils.isNotEmpty(recursionList)) {
                        // ??????????????????
                        resultList.addAll(recursionList);

                        // ??????????????????????????????loadSequence
                        Optional<MtMaterialLotVO18> first = recursionList.stream()
                                        .sorted(Comparator.comparing(MtMaterialLotVO18::getLoadSequence).reversed())
                                        .findFirst();
                        currentSequence = first.get().getLoadSequence().intValue();
                    }
                }
            }

            return resultList;
        }
    }

    /**
     * ??????????????? ??????????????????????????????
     *
     * @author chuang.yang
     * @date 2019/10/11
     * @return
     */
    private static class ReservedMaterialLot {
        private String siteId; // ???????????????????????????????????????ID
        private String materialId; // ?????????????????????????????????????????????ID
        private String locatorId; // ?????????????????????????????????ID???????????????????????????????????????
        private String lot; // ???????????????????????????????????????????????????
        private String ownerType; // ??????????????????????????????????????????????????????????????????????????????C??????????????????????????????????????????????????????S???????????????????????????????????????????????????????????????????????????????????????????????????
        private String ownerId; // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????ID???????????????ID
        private String reservedObjectType; // ?????????????????????????????????????????????????????????????????????????????????WO??????????????????????????????????????????EO??????????????????????????????????????????DRIVING??????????????????????????????????????????CUSTOM????????????????????????????????????OO??????????????????????????????????????????
        private String reservedObjectId; // ?????????????????????????????????????????????????????????????????????????????????????????????

        ReservedMaterialLot(String siteId, String materialId, String locatorId, String ownerType, String ownerId,
                        String lot, String reservedObjectType, String reservedObjectId) {
            this.siteId = siteId;
            this.materialId = materialId;
            this.locatorId = locatorId;
            this.ownerType = ownerType;
            this.ownerId = ownerId;
            this.lot = lot;
            this.reservedObjectType = reservedObjectType;
            this.reservedObjectId = reservedObjectId;
        }

        public String getSiteId() {
            return siteId;
        }

        public String getMaterialId() {
            return materialId;
        }

        public String getLocatorId() {
            return locatorId;
        }

        public String getOwnerType() {
            return ownerType;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public String getLot() {
            return lot;
        }

        public String getReservedObjectType() {
            return reservedObjectType;
        }

        public String getReservedObjectId() {
            return reservedObjectId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ReservedMaterialLot that = (ReservedMaterialLot) o;
            return com.google.common.base.Objects.equal(siteId, that.siteId)
                            && com.google.common.base.Objects.equal(materialId, that.materialId)
                            && com.google.common.base.Objects.equal(locatorId, that.locatorId)
                            && com.google.common.base.Objects.equal(lot, that.lot)
                            && com.google.common.base.Objects.equal(ownerType, that.ownerType)
                            && com.google.common.base.Objects.equal(ownerId, that.ownerId)
                            && com.google.common.base.Objects.equal(reservedObjectType, that.reservedObjectType)
                            && com.google.common.base.Objects.equal(reservedObjectId, that.reservedObjectId);
        }

        @Override
        public int hashCode() {
            return com.google.common.base.Objects.hashCode(siteId, materialId, locatorId, lot, ownerType, ownerId,
                            reservedObjectType, reservedObjectId);
        }
    }

    private static class UnReservedMaterialLot {
        private String siteId; // ???????????????????????????????????????ID
        private String materialId; // ?????????????????????????????????????????????ID
        private String locatorId; // ?????????????????????????????????ID???????????????????????????????????????
        private String ownerType; // ??????????????????????????????????????????????????????????????????????????????C??????????????????????????????????????????????????????S???????????????????????????????????????????????????????????????????????????????????????????????????
        private String ownerId; // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????ID???????????????ID
        private String lot; // ???????????????????????????????????????????????????


        UnReservedMaterialLot(String siteId, String materialId, String locatorId, String ownerType, String ownerId,
                        String lot) {
            this.siteId = siteId;
            this.materialId = materialId;
            this.locatorId = locatorId;
            this.ownerType = ownerType;
            this.ownerId = ownerId;
            this.lot = lot;
        }

        public String getSiteId() {
            return siteId;
        }

        public String getMaterialId() {
            return materialId;
        }

        public String getLocatorId() {
            return locatorId;
        }

        public String getOwnerType() {
            return ownerType;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public String getLot() {
            return lot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            UnReservedMaterialLot that = (UnReservedMaterialLot) o;
            return Objects.equals(siteId, that.siteId) && Objects.equals(materialId, that.materialId)
                            && Objects.equals(locatorId, that.locatorId) && Objects.equals(ownerType, that.ownerType)
                            && Objects.equals(ownerId, that.ownerId) && Objects.equals(lot, that.lot);
        }

        @Override
        public int hashCode() {
            return Objects.hash(siteId, materialId, locatorId, ownerType, ownerId, lot);
        }
    }

    @Override
    public List<MtMaterialLotVO24> lotLimitMaterialLotQuery(Long tenantId, String lot) {
        if (StringUtils.isEmpty(lot)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "lot", "???API:lotLimitMaterialLotQuery???"));
        }

        MtMaterialLot record = new MtMaterialLot();
        record.setLot(lot);
        List<MtMaterialLot> mtMaterialLots = this.mtMaterialLotMapper.select(record);
        if (CollectionUtils.isEmpty(mtMaterialLots)) {
            return Collections.emptyList();
        }

        final List<MtMaterialLotVO24> result = new ArrayList<MtMaterialLotVO24>();
        mtMaterialLots.stream().forEach(c -> {
            MtMaterialLotVO24 vo = new MtMaterialLotVO24();
            vo.setEnableFlag(c.getEnableFlag());
            vo.setInLocatorTime(c.getInLocatorTime());
            vo.setLocatorId(c.getLocatorId());
            vo.setMaterialId(c.getMaterialId());
            vo.setMaterialLotCode(c.getMaterialLotCode());
            vo.setMaterialLotId(c.getMaterialLotId());
            vo.setOwnerId(c.getOwnerId());
            vo.setOwnerType(c.getOwnerType());
            vo.setPrimaryUomId(c.getPrimaryUomId());
            vo.setPrimaryUomQty(c.getPrimaryUomQty());
            vo.setReservedFlag(c.getReservedFlag());
            vo.setReservedObjectId(c.getReservedObjectId());
            vo.setReservedObjectType(c.getReservedObjectType());
            vo.setSecondaryUomId(c.getSecondaryUomId());
            vo.setSecondaryUomQty(c.getSecondaryUomQty());
            vo.setSiteId(c.getSiteId());
            result.add(vo);
        });

        return result.stream().sorted(Comparator.comparing(c -> Double.valueOf(c.getMaterialId())))
                        .collect(Collectors.toList());
    }

    @Override
    public List<String> selectLotIdByTopContainerId(Long tenantId, String topContainerId) {
        return mtMaterialLotMapper.selectLotIdByTopContainerId(tenantId, topContainerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialLotAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "keyid", "???API:materialLotAttrPropertyUpdate???"));
        }
        MtMaterialLot lot = new MtMaterialLot();
        lot.setTenantId(tenantId);
        lot.setMaterialLotId(mtExtendVO10.getKeyId());
        MtMaterialLot materialLot = mtMaterialLotMapper.selectOne(lot);
        if (null == materialLot) {
            throw new MtException("MT_MATERIAL_LOT_0089",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0089",
                                            "MATERIAL_LOT", mtExtendVO10.getKeyId(), "mt_material_lot",
                                            "???API:materialLotAttrPropertyUpdate???"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", mtExtendVO10.getKeyId(),
                        mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());
    }

    @Override
    public List<MtMaterialLot> materialLotByCodeBatchGet(Long tenantId, List<String> materialLotCodes) {
        if (CollectionUtils.isEmpty(materialLotCodes)) {
            return new ArrayList<>();
        }
        return mtMaterialLotMapper.selectForMaterialLotCodes(tenantId, materialLotCodes);
    }

    /**
     * materialLotBatchCodeGet-????????????????????????
     *
     * @author chuang.yang
     * @date 2019/12/3
     * @param tenantId
     * @param dto
     * @return io.tarzan.common.domain.vo.MtNumrangeVO8
     */
    @Override
    public MtNumrangeVO8 materialLotBatchCodeGet(Long tenantId, MtMaterialLotVO27 dto) {
        // 1. ?????????????????????
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
            if (null == mtModSite) {
                throw new MtException("MT_MATERIAL_LOT_0091",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0091",
                                                "MATERIAL_LOT", "siteId:" + dto.getSiteId(),
                                                "???API:materialLotBatchCodeGet???"));
            }
        }

        // ??????callObjectCodeList????????????
        List<MtNumrangeVO10> numrangeVO10List = new ArrayList<>();
        // ??????objectTypeCode???????????????????????????
        List<String> objectTypeCodes = new ArrayList<>();

        // 2. ??????objectId????????????ID
        MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        numrangeObject.setObjectCode("MATERIAL_LOT_CODE");
        List<String> objectIds = mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, numrangeObject);
        if (CollectionUtils.isEmpty(objectIds)) {
            throw new MtException("MT_MATERIAL_LOT_0090", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0090", "MATERIAL_LOT", "???API:materialLotBatchCodeGet???"));
        }

        // ??????objectId????????????ID,??????????????????
        String objectId = objectIds.get(0);

        // 3.a ??????????????????????????????objectColumnCode
        MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
        objectColumn.setObjectId(objectId);
        List<MtNumrangeObjectColumn> mtNumrangeObjectColumns =
                        mtNumrangeObjectColumnRepository.propertyLimitNumrangeObjectColumnQuery(tenantId, objectColumn);

        if (CollectionUtils.isNotEmpty(mtNumrangeObjectColumns)) {
            String typeCode = null;
            String objectColumnCode = null;

            // ??????????????????TYPE_GROUP?????????????????????????????????
            // ????????????
            List<MtNumrangeObjectColumn> columnList = mtNumrangeObjectColumns.stream().filter(
                            t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(columnList)) {
                objectColumnCode = columnList.get(0).getObjectColumnCode();
                typeCode = columnList.get(0).getTypeGroup();
            }

            // ??????Map??????
            Map<String, MtNumrangeObjectColumn> mtNumrangeObjectColumnMap = mtNumrangeObjectColumns.stream()
                            .collect(Collectors.toMap(MtNumrangeObjectColumn::getObjectColumnCode, t -> t));

            // ??????????????????
            if (CollectionUtils.isNotEmpty(dto.getMaterialLotPropertyList())) {
                for (MtNumrangeVO10 mtNumrangeVO10 : dto.getMaterialLotPropertyList()) {
                    // ???????????????????????????map?????????????????????
                    Map<String, String> callObjectCodeMap = new HashMap<>(0);

                    for (Map.Entry<String, String> entry : mtNumrangeVO10.getCallObjectCode().entrySet()) {
                        // ????????????????????????
                        MtNumrangeObjectColumn mtNumrangeObjectColumn = mtNumrangeObjectColumnMap.get(entry.getKey());
                        if (mtNumrangeObjectColumn != null) {
                            callObjectCodeMap.put(entry.getKey(), entry.getValue());
                        }
                    }

                    if (MapUtils.isNotEmpty(callObjectCodeMap)) {
                        // ????????????
                        MtNumrangeVO10 calObjectNumrange = new MtNumrangeVO10();
                        calObjectNumrange.setCallObjectCode(callObjectCodeMap);
                        calObjectNumrange.setSequence(mtNumrangeVO10.getSequence());
                        numrangeVO10List.add(calObjectNumrange);

                        if (StringUtils.isNotEmpty(typeCode)) {
                            objectTypeCodes.add(callObjectCodeMap.get(objectColumnCode));
                        }
                    }
                }
            }
        }

        // 3.b ????????????objectTypeCode??????????????????????????????????????????
        String objectTypeCode = null;
        List<String> distinctObjectTypeCodes = objectTypeCodes.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(distinctObjectTypeCodes)) {
            if (distinctObjectTypeCodes.size() > 1) {
                throw new MtException("MT_MATERIAL_LOT_0092", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0092", "MATERIAL_LOT", "???API:materialLotBatchCodeGet???"));
            } else {
                objectTypeCode = distinctObjectTypeCodes.get(0);
            }
        }

        // 4. ???????????????????????????
        mtNumrangeVO9.setObjectCode("MATERIAL_LOT_CODE");
        mtNumrangeVO9.setObjectTypeCode(objectTypeCode);
        mtNumrangeVO9.setSiteId(dto.getSiteId());
        mtNumrangeVO9.setOutsideNumList(dto.getOutsideNumList());
        mtNumrangeVO9.setCallObjectCodeList(numrangeVO10List);
        mtNumrangeVO9.setIncomingValueList(dto.getIncomingValueList());
        mtNumrangeVO9.setObjectNumFlag(dto.getObjectNumFlag());
        mtNumrangeVO9.setNumQty(dto.getNumQty());
        return mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
    }

    /**
     * codeOrIdentificationLimitObjectGet-?????????????????????????????????????????????
     *
     * @author chuang.yang
     * @date 2020/1/13
     * @param tenantId
     * @param dto
     * @return tarzan.inventory.domain.vo.MtMaterialLotVO29
     */
    @Override
    public MtMaterialLotVO29 codeOrIdentificationLimitObjectGet(Long tenantId, MtMaterialLotVO30 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getCode()) && StringUtils.isEmpty(dto.getIdentification())) {
            throw new MtException("MT_MATERIAL_LOT_0097", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0097", "MATERIAL_LOT", "???API:codeOrIdentificationLimitObjectGet???"));
        }
        if (StringUtils.isNotEmpty(dto.getCode()) && StringUtils.isNotEmpty(dto.getIdentification())) {
            throw new MtException("MT_MATERIAL_LOT_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                                            "MATERIAL_LOT", "code???identification",
                                            "???API:codeOrIdentificationLimitObjectGet???"));
        }
        if (StringUtils.isEmpty(dto.getAllLevelFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "allLevelFlag",
                                            "???API:codeOrIdentificationLimitObjectGet???"));
        }
        if (!Arrays.asList(MtBaseConstants.YES, MtBaseConstants.NO).contains(dto.getAllLevelFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0098",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0098",
                                            "MATERIAL_LOT", "allLevelFlag",
                                            "???API:codeOrIdentificationLimitObjectGet???"));
        }

        MtMaterialLotVO29 result = new MtMaterialLotVO29();

        // 2. ?????????????????????????????????
        MtContainer condition = new MtContainer();
        condition.setTenantId(tenantId);
        condition.setContainerCode(dto.getCode());
        condition.setIdentification(dto.getIdentification());
        MtContainer orgContainer = mtContainerRepository.selectOne(condition);
        if (orgContainer != null) {
            result.setCodeId(orgContainer.getContainerId());
            result.setCodeType(MtBaseConstants.LOAD_TYPE.CONTAINER);

            // ????????????????????????
            MtContainerType mtContainerType = mtContainerTypeRepository.containerTypePropertyGet(tenantId,
                            orgContainer.getContainerTypeId());
            result.setContainerType(mtContainerType == null ? "" : mtContainerType.getContainerTypeDescription());

            // ??????????????????????????????
            MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
            mtContLoadDtlVO5.setLoadObjectId(orgContainer.getContainerId());
            mtContLoadDtlVO5.setLoadObjectType(MtBaseConstants.LOAD_TYPE.CONTAINER);
            List<String> containerIds = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId,
                            mtContLoadDtlVO5);
            if (CollectionUtils.isNotEmpty(containerIds)) {
                // ???????????????????????????
                result.setLoadingContainerId(containerIds.get(0));
            }

            MtContLoadDtlVO mtContLoadDtlVO = new MtContLoadDtlVO();
            mtContLoadDtlVO.setAllLevelFlag(dto.getAllLevelFlag());
            mtContLoadDtlVO.setContainerId(orgContainer.getContainerId());
            List<MtContLoadDtlVO6> contLoadDtlVO6List =
                            mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtContLoadDtlVO);
            if (CollectionUtils.isNotEmpty(contLoadDtlVO6List)) {
                result.setLoadingObjectlList(convertLoadDetialInfo(orgContainer.getContainerId(), contLoadDtlVO6List));
            }
            return result;
        }

        // 3. ????????????????????????????????????
        MtMaterialLot materialLotCondition = new MtMaterialLot();
        materialLotCondition.setTenantId(tenantId);
        materialLotCondition.setIdentification(dto.getIdentification());
        materialLotCondition.setMaterialLotCode(dto.getCode());
        MtMaterialLot orgMaterialLot = self().selectOne(materialLotCondition);
        if (orgMaterialLot == null) {
            throw new MtException("MT_MATERIAL_LOT_0099",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0099",
                                            "MATERIAL_LOT",
                                            "code:" + dto.getCode() + "identification:" + dto.getIdentification(),
                                            "???API:codeOrIdentificationLimitObjectGet???"));
        }

        result.setCodeId(orgMaterialLot.getMaterialLotId());
        result.setCodeType(MtBaseConstants.LOAD_TYPE.MATERIAL_LOT);

        // ??????????????????????????????
        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
        mtContLoadDtlVO5.setLoadObjectId(orgMaterialLot.getMaterialLotId());
        mtContLoadDtlVO5.setLoadObjectType(MtBaseConstants.LOAD_TYPE.MATERIAL_LOT);
        List<String> containerIds =
                        mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
        if (CollectionUtils.isNotEmpty(containerIds)) {
            // ???????????????????????????
            result.setLoadingContainerId(containerIds.get(0));
        }

        return result;
    }

    @Override
    public List<MtMaterialLot> selectListByEoId(Long tenantId, String eoId) {
        return mtMaterialLotMapper.selectListByEoId(tenantId, eoId);
    }

    /**
     * ????????? - codeOrIdentificationLimitObjectGet ????????????????????????
     *
     * @author chuang.yang
     * @date 2020/1/13
     * @param containerId
     * @param contLoadDtlVO6List
     * @return java.util.List<tarzan.inventory.domain.vo.MtMaterialLotVO28>
     */
    private List<MtMaterialLotVO28> convertLoadDetialInfo(String containerId,
                    List<MtContLoadDtlVO6> contLoadDtlVO6List) {
        List<MtMaterialLotVO28> result = new ArrayList<>();

        // ????????????????????????????????????
        List<MtContLoadDtlVO6> currContLoadDtlVO6s = contLoadDtlVO6List.stream()
                        .filter(t -> t.getContainerId().equals(containerId)).collect(Collectors.toList());

        MtMaterialLotVO28 loadDtl = null;
        for (MtContLoadDtlVO6 loadDtlVO6 : currContLoadDtlVO6s) {
            if (MtBaseConstants.LOAD_TYPE.CONTAINER.equals(loadDtlVO6.getLoadObjectType())) {
                loadDtl = new MtMaterialLotVO28();
                loadDtl.setLoadObjectId(loadDtlVO6.getLoadObjectId());
                loadDtl.setLoadObjectType(loadDtlVO6.getLoadObjectType());

                // ????????????????????????
                List<MtMaterialLotVO28> tempResult =
                                convertLoadDetialInfo(loadDtlVO6.getLoadObjectId(), contLoadDtlVO6List);
                if (CollectionUtils.isNotEmpty(tempResult)) {
                    loadDtl.setLoadContainer(tempResult);
                }

                result.add(loadDtl);
            } else {
                // ????????????????????????
                loadDtl = new MtMaterialLotVO28();
                loadDtl.setLoadObjectId(loadDtlVO6.getLoadObjectId());
                loadDtl.setLoadObjectType(loadDtlVO6.getLoadObjectType());
                result.add(loadDtl);
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String sequenceLimitMaterialLotBatchConsumeForNew(Long tenantId, MtMaterialLotVO31 dto) {
        // 1. ?????????????????????
        if (CollectionUtils.isEmpty(dto.getMtMaterialList())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "{materialLotId,sequence}",
                                            "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        Optional<MtMaterialLotVO33> any = dto.getMtMaterialList().stream()
                        .map(MtMaterialLotVO32::getMtMaterialLotVO33List).filter(CollectionUtils::isNotEmpty)
                        .flatMap(Collection::stream).filter(t -> StringUtils.isEmpty(t.getMaterialLotId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId",
                                            "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        if (StringUtils.isEmpty(dto.getAllConsume())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "allConsume",
                                            "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        if (!Arrays.asList("Y", "N").contains(dto.getAllConsume())) {
            throw new MtException("MT_MATERIAL_LOT_0080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0080", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        boolean isAllConsume = "Y".equals(dto.getAllConsume());
        // ???????????????????????????
        List<String> materialLotIds = dto.getMtMaterialList().stream().map(MtMaterialLotVO32::getMtMaterialLotVO33List)
                        .filter(CollectionUtils::isNotEmpty).flatMap(Collection::stream)
                        .map(MtMaterialLotVO33::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> mtMaterialLotList = this.materialLotPropertyBatchGetForUpdate(tenantId, materialLotIds);
        Map<String, MtMaterialLot> mtMaterialLotAllMap =
                        mtMaterialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId, c -> c));

        Map<String, List<MtMaterialLotVO33>> mtMaterialLotMap = dto.getMtMaterialList().stream().collect(Collectors
                        .toMap(MtMaterialLotVO32::getMaterialId, MtMaterialLotVO32::getMtMaterialLotVO33List));
        Map<String, Double> trxPrimaryUomQtyMap = dto.getMtMaterialList().stream().collect(
                        Collectors.toMap(MtMaterialLotVO32::getMaterialId, MtMaterialLotVO32::getTrxPrimaryUomQty));
        if (!isAllConsume) {
            // ?????????????????????????????????
            for (Map.Entry<String, List<MtMaterialLotVO33>> entry : mtMaterialLotMap.entrySet()) {
                any = entry.getValue().stream().filter(t -> t.getSequence() == null).findAny();
                if (any.isPresent()) {
                    throw new MtException("MT_MATERIAL_LOT_0081",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0081",
                                                    "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
                }
                // ??????????????????
                Map<Long, List<MtMaterialLotVO33>> longListMap = entry.getValue().stream()
                                .collect(Collectors.groupingBy(MtMaterialLotVO33::getSequence));
                for (Map.Entry<Long, List<MtMaterialLotVO33>> t : longListMap.entrySet()) {
                    if (t.getValue().size() > 1) {
                        throw new MtException("MT_MATERIAL_LOT_0079",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0079", "MATERIAL_LOT", "sequence",
                                                        "???API:sequenceLimitMaterialLotBatchConsume???"));
                    }
                }
                // ???????????????????????????????????????????????????
                if (trxPrimaryUomQtyMap.get(entry.getKey()) == null) {
                    throw new MtException("MT_MATERIAL_LOT_0081",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0081",
                                                    "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
                }
            }
        }

        // List<String> materialIds =
        // mtMaterialLotList.stream().map(MtMaterialLot::getMaterialId).distinct()
        // .collect(Collectors.toList());
        // if (materialIds.size() > 1) {
        // throw new MtException("MT_MATERIAL_LOT_0082",
        // mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
        // "MT_MATERIAL_LOT_0082", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        // }

        // ??????????????????
        mtMaterialLotList = mtMaterialLotList.stream().filter(t -> "Y".equals(t.getEnableFlag()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(mtMaterialLotList)) {
            throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        // ??????????????????????????????????????????
        List<MtMaterialLot> stocktakeMaterialLots = mtMaterialLotList.stream()
                        .filter(t -> "Y".equals(t.getStocktakeFlag())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(stocktakeMaterialLots)) {
            throw new MtException("MT_MATERIAL_LOT_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0072",
                                            "MATERIAL_LOT",
                                            stocktakeMaterialLots.stream().map(MtMaterialLot::getMaterialLotId)
                                                            .collect(Collectors.toList()).toString(),
                                            "???API:sequenceLimitMaterialLotBatchConsume???"));
        }

        // // ?????????????????????????????????????????????????????????
        // boolean isHasPrimaryUomId = StringUtils.isNotEmpty(dto.getPrimaryUomId());
        // if (isHasPrimaryUomId) {
        // MtMaterialVO2 mtMaterialVO2 = new MtMaterialVO2();
        // mtMaterialVO2.setMaterialId(mtMaterialLotList.get(0).getMaterialId());
        // mtMaterialVO2.setPrimaryUomId(dto.getPrimaryUomId());
        // this.mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
        // }
        //
        // boolean isHasSecondaryUomId = StringUtils.isNotEmpty(dto.getSecondaryUomId());
        // if (isHasSecondaryUomId) {
        // MtMaterialVO2 mtMaterialVO2 = new MtMaterialVO2();
        // mtMaterialVO2.setMaterialId(mtMaterialLotList.get(0).getMaterialId());
        // mtMaterialVO2.setSecondaryUomId(dto.getSecondaryUomId());
        // this.mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
        // }

        // ??????????????????
        // Optional<MtMaterialLot> materialAny =
        // mtMaterialLotList.stream().filter(t -> StringUtils.isNotEmpty(t.getSecondaryUomId())).findAny();
        // if (materialAny.isPresent() && dto.getTrxSecondaryUomQty() == null) {
        // throw new MtException("MT_MATERIAL_LOT_0017",
        // mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
        // "MT_MATERIAL_LOT_0017", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        // }
        // // ?????????????????????
        // Map<String, List<MtMaterialLot>> uomIdMap =
        // mtMaterialLotList.stream().collect(Collectors.groupingBy(MtMaterialLot::getPrimaryUomId));
        // if (uomIdMap.size() > 1) {
        // throw new MtException("MT_MATERIAL_LOT_0077",
        // mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
        // "MT_MATERIAL_LOT_0077", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        // }
        //
        // uomIdMap =
        // mtMaterialLotList.stream().collect(Collectors.groupingBy(MtMaterialLot::getSecondaryUomId));
        // if (uomIdMap.size() > 1) {
        // throw new MtException("MT_MATERIAL_LOT_0082",
        // mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
        // "MT_MATERIAL_LOT_0082", "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
        // }

        // // ?????????????????????
        // MtMaterialVO1 mtMaterialVO1 =
        // mtMaterialRepository.materialUomGet(tenantId, mtMaterialLotList.get(0).getMaterialId());
        // if (mtMaterialVO1 == null) {
        // throw new MtException("MT_MATERIAL_LOT_0004",
        // mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
        // "MT_MATERIAL_LOT_0004", "?????????????????????", "???API:sequenceLimitMaterialLotBatchConsume???"));
        // }

        // ????????????
        Map<String, BigDecimal> sumPrimaryUomQtyMap = mtMaterialLotList.stream()
                        .collect(Collectors.groupingBy(MtMaterialLot::getMaterialId,
                                        CollectorsUtil.summingBigDecimal(
                                                        t -> null == t.getPrimaryUomQty() ? BigDecimal.ZERO
                                                                        : BigDecimal.valueOf(t.getPrimaryUomQty()))));

        // ????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_BATCH_CONSUME");
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        String materialLotBatchConsumeEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        eventCreateVO.setEventTypeCode("CONTAINER_MATERIAL_LOT_UNLOAD");
        String containerMaterialLotUnLoadEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ????????????
        List<String> sqlList = new ArrayList<>();
        Date now = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();

        List<String> materialLotCids =
                        customDbRepository.getNextKeys("mt_material_lot_cid_s", mtMaterialLotList.size());
        List<String> materialLotHisIds =
                        customDbRepository.getNextKeys("mt_material_lot_his_s", mtMaterialLotList.size());
        List<String> materialLotHisCids =
                        customDbRepository.getNextKeys("mt_material_lot_his_cid_s", mtMaterialLotList.size());

        // ???????????????????????????????????????
        List<MtMaterialLot> onhandMaterialLotList = new ArrayList<MtMaterialLot>();
        int i = 0;
        if (isAllConsume) {
            // ?????????
            // ???????????????
            // ???????????????????????????
            List<MtMaterialLotVO33> mtMaterialLotVO16s = dto.getMtMaterialList().stream()
                            .map(MtMaterialLotVO32::getMtMaterialLotVO33List).filter(CollectionUtils::isNotEmpty)
                            .flatMap(Collection::stream).collect(Collectors.toList());
            for (MtMaterialLotVO33 materialLotSequence : mtMaterialLotVO16s) {
                MtMaterialLot mtMaterialLot = mtMaterialLotAllMap.get(materialLotSequence.getMaterialLotId());
                if (mtMaterialLot == null) {
                    continue;
                }

                MtMaterialLot currentMaterialLot = new MtMaterialLot();
                BeanUtils.copyProperties(mtMaterialLot, currentMaterialLot);

                // ????????????????????????????????????????????????????????????
                onhandMaterialLotList.add(mtMaterialLot);

                currentMaterialLot.setEnableFlag("N");
                currentMaterialLot.setUnloadTime(now);
                currentMaterialLot.setPrimaryUomQty(0.0D);
                currentMaterialLot.setSecondaryUomQty(0.0D);
                currentMaterialLot.setCurrentContainerId("");
                currentMaterialLot.setTopContainerId("");
                if (dto.getInstructionDocId() != null) {
                    currentMaterialLot.setInstructionDocId(dto.getInstructionDocId());
                }
                // ??????????????????
                currentMaterialLot.setLatestHisId(materialLotHisIds.get(i));
                currentMaterialLot.setCid(Long.valueOf(materialLotCids.get(i)));
                currentMaterialLot.setLastUpdatedBy(userId);
                currentMaterialLot.setLastUpdateDate(now);
                sqlList.addAll(customDbRepository.getUpdateSql(currentMaterialLot));

                MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                BeanUtils.copyProperties(currentMaterialLot, mtMaterialLotHis);
                mtMaterialLotHis.setMaterialLotHisId(materialLotHisIds.get(i));
                mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCids.get(i)));
                mtMaterialLotHis.setEventId(materialLotBatchConsumeEventId);
                mtMaterialLotHis.setTrxPrimaryQty(-mtMaterialLot.getPrimaryUomQty());
                mtMaterialLotHis.setTrxSecondaryQty(mtMaterialLot.getSecondaryUomQty() == null ? null
                                : -mtMaterialLot.getSecondaryUomQty());
                mtMaterialLotHis.setCreatedBy(userId);
                mtMaterialLotHis.setCreationDate(now);
                sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));
                i++;
            }

            // ???????????????????????????????????????
            materialLotIds = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId)
                            .collect(Collectors.toList());

            List<String> containerAndLoadDetailSqlList = this.dealContainerAndLoadDetail(tenantId, userId, now,
                            materialLotIds, containerMaterialLotUnLoadEventId);
            if (CollectionUtils.isNotEmpty(containerAndLoadDetailSqlList)) {
                sqlList.addAll(containerAndLoadDetailSqlList);
            }

        } else {
            List<MtMaterialLot> doneMtMaterialLotList = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : sumPrimaryUomQtyMap.entrySet()) {
                // ????????????
                // 2. ????????????????????????????????????????????????????????????????????????????????????
                // 2.1. ????????????????????????????????? trxPrimaryUomQty
                Double trxPrimaryUomQty = trxPrimaryUomQtyMap.get(entry.getKey());
                // if (isHasPrimaryUomId) {
                // MtUomVO1 transferUomVO = new MtUomVO1();
                // transferUomVO.setSourceUomId(dto.getPrimaryUomId());
                // transferUomVO.setSourceValue(trxPrimaryUomQty);
                // transferUomVO.setTargetUomId(mtMaterialLotList.get(0).getPrimaryUomId());
                // transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                // trxPrimaryUomQty = transferUomVO.getTargetValue();
                // }

                // 3. ?????????????????????????????????????????????
                if (BigDecimal.valueOf(trxPrimaryUomQty).compareTo(entry.getValue()) > 0) {
                    throw new MtException("MT_MATERIAL_LOT_0018",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0018",
                                                    "MATERIAL_LOT", "???API:sequenceLimitMaterialLotBatchConsume???"));
                }

                // 3.1. ???????????????????????????????????????????????????
                // ?????????????????????????????????????????????????????????????????????

                List<MtMaterialLotVO33> mtMaterialLotVO16s = mtMaterialLotMap.get(entry.getKey());
                List<MtMaterialLotVO33> materialLotSequenceList =
                                mtMaterialLotVO16s.stream().sorted(Comparator.comparing(MtMaterialLotVO33::getSequence))
                                                .collect(Collectors.toList());
                BigDecimal trxSumQty = BigDecimal.valueOf(trxPrimaryUomQty);
                BigDecimal sumQty = BigDecimal.ZERO;


                // ???????????????????????????
                for (MtMaterialLotVO33 materialLotSequence : materialLotSequenceList) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotAllMap.get(materialLotSequence.getMaterialLotId());
                    if (mtMaterialLot == null) {
                        continue;
                    }

                    MtMaterialLot currentMaterialLot = new MtMaterialLot();
                    BeanUtils.copyProperties(mtMaterialLot, currentMaterialLot);

                    BigDecimal primaryUomQty = BigDecimal.valueOf(currentMaterialLot.getPrimaryUomQty());

                    // ??????????????????
                    BigDecimal tempSumQty = primaryUomQty.add(sumQty);

                    // ?????????????????????????????????????????????????????????????????????
                    if (tempSumQty.compareTo(trxSumQty) > 0) {

                        // ???????????????????????????????????????????????????????????????
                        BigDecimal updatePrimaryUomQty = tempSumQty.subtract(trxSumQty);

                        currentMaterialLot.setPrimaryUomQty(updatePrimaryUomQty.doubleValue());

                        // ???????????????????????????
                        BigDecimal currentPrimaryQtyTrxQty = trxSumQty.subtract(sumQty);
                        // BigDecimal currentSecondQtyTrxQty = null;
                        // // ??????????????????
                        // if (StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
                        // // ????????????????????????????????????????????????????????????
                        // MtUomVO1 transferUomVO = new MtUomVO1();
                        // transferUomVO.setSourceUomId(currentMaterialLot.getPrimaryUomId());
                        // transferUomVO.setSourceValue(currentPrimaryQtyTrxQty.doubleValue());
                        // transferUomVO.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
                        // transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                        //
                        // // ????????????????????????????????????????????????????????????
                        // MtUomVO3 materialTransferUomVO = new MtUomVO3();
                        // materialTransferUomVO.setMaterialId(currentMaterialLot.getMaterialId());
                        // materialTransferUomVO.setSourceUomId(mtMaterialVO1.getPrimaryUomId());
                        // materialTransferUomVO.setSourceValue(transferUomVO.getTargetValue());
                        // materialTransferUomVO =
                        // mtUomRepository.materialUomConversion(tenantId, materialTransferUomVO);
                        //
                        // // ?????????????????????????????????????????????????????????????????????
                        // transferUomVO.setSourceUomId(mtMaterialVO1.getSecondaryUomId());
                        // transferUomVO.setSourceValue(materialTransferUomVO.getTargetValue());
                        // transferUomVO.setTargetUomId(currentMaterialLot.getSecondaryUomId());
                        // transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                        //
                        // currentSecondQtyTrxQty = BigDecimal.valueOf(transferUomVO.getTargetValue());
                        // currentMaterialLot.setSecondaryUomQty(
                        // BigDecimal.valueOf(materialLotOptional.get().getSecondaryUomQty())
                        // .subtract(currentSecondQtyTrxQty).doubleValue());
                        // }

                        // ??????????????????
                        currentMaterialLot.setLatestHisId(materialLotHisIds.get(i));
                        currentMaterialLot.setUnloadTime(now);
                        currentMaterialLot.setCid(Long.valueOf(materialLotCids.get(i)));
                        currentMaterialLot.setLastUpdateDate(now);
                        currentMaterialLot.setLastUpdatedBy(userId);
                        if (dto.getInstructionDocId() != null) {
                            currentMaterialLot.setInstructionDocId(dto.getInstructionDocId());
                        }
                        sqlList.addAll(customDbRepository.getUpdateSql(currentMaterialLot));

                        MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                        BeanUtils.copyProperties(currentMaterialLot, mtMaterialLotHis);
                        mtMaterialLotHis.setMaterialLotHisId(materialLotHisIds.get(i));
                        mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCids.get(i)));
                        mtMaterialLotHis.setTrxPrimaryQty(-currentPrimaryQtyTrxQty.doubleValue());
                        mtMaterialLotHis.setTrxSecondaryQty(null);
                        mtMaterialLotHis.setEventId(materialLotBatchConsumeEventId);
                        mtMaterialLotHis.setCreatedBy(userId);
                        mtMaterialLotHis.setCreationDate(now);
                        sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));

                        // ????????????????????????????????????????????????????????????
                        currentMaterialLot.setPrimaryUomQty(trxSumQty.subtract(sumQty).doubleValue());
                        onhandMaterialLotList.add(currentMaterialLot);
                        i++;
                        break;
                    } else {
                        // ????????????????????????????????????????????????????????????
                        onhandMaterialLotList.add(mtMaterialLot);
                        doneMtMaterialLotList.add(mtMaterialLot);

                        currentMaterialLot.setEnableFlag("N");
                        currentMaterialLot.setUnloadTime(now);
                        currentMaterialLot.setPrimaryUomQty(0.0D);
                        currentMaterialLot.setSecondaryUomQty(0.0D);
                        currentMaterialLot.setCurrentContainerId("");
                        currentMaterialLot.setTopContainerId("");
                        if (dto.getInstructionDocId() != null) {
                            currentMaterialLot.setInstructionDocId(dto.getInstructionDocId());
                        }
                        // ??????????????????Id
                        currentMaterialLot.setLatestHisId(materialLotHisIds.get(i));
                        currentMaterialLot.setCid(Long.valueOf(materialLotCids.get(i)));
                        currentMaterialLot.setLastUpdatedBy(userId);
                        currentMaterialLot.setLastUpdateDate(now);
                        sqlList.addAll(customDbRepository.getUpdateSql(currentMaterialLot));

                        MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
                        BeanUtils.copyProperties(currentMaterialLot, mtMaterialLotHis);
                        mtMaterialLotHis.setMaterialLotHisId(materialLotHisIds.get(i));
                        mtMaterialLotHis.setCid(Long.valueOf(materialLotHisCids.get(i)));
                        mtMaterialLotHis.setTrxPrimaryQty(-mtMaterialLot.getPrimaryUomQty());
                        mtMaterialLotHis.setTrxSecondaryQty(Objects.isNull(mtMaterialLot.getSecondaryUomQty()) ? 0
                                        : -mtMaterialLot.getSecondaryUomQty());
                        mtMaterialLotHis.setEventId(materialLotBatchConsumeEventId);
                        mtMaterialLotHis.setCreatedBy(userId);
                        mtMaterialLotHis.setCreationDate(now);
                        sqlList.addAll(customDbRepository.getInsertSql(mtMaterialLotHis));
                    }

                    sumQty = tempSumQty;
                    i++;
                    if (tempSumQty.compareTo(trxSumQty) == 0) {
                        break;
                    }
                }
            }
            // ???????????????????????????????????????
            if (CollectionUtils.isNotEmpty(doneMtMaterialLotList)) {
                materialLotIds = doneMtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId)
                                .collect(Collectors.toList());

                List<String> containerAndLoadDetailSqlList = this.dealContainerAndLoadDetail(tenantId, userId, now,
                                materialLotIds, containerMaterialLotUnLoadEventId);
                if (CollectionUtils.isNotEmpty(containerAndLoadDetailSqlList)) {
                    sqlList.addAll(containerAndLoadDetailSqlList);
                }
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        // ???????????????
        // ???????????????????????????????????????????????????
        Map<ReservedMaterialLot, List<MtMaterialLot>> reservedMaterialLotsMap = onhandMaterialLotList.stream()
                        .filter(t -> "Y".equals(t.getReservedFlag()))
                        .collect(Collectors.groupingBy(b -> new ReservedMaterialLot(b.getSiteId(), b.getMaterialId(),
                                        b.getLocatorId(), b.getOwnerType(), b.getOwnerId(), b.getLot(),
                                        b.getReservedObjectType(), b.getReservedObjectId())));

        Map<UnReservedMaterialLot, List<MtMaterialLot>> unReservedMaterialLotsMap =
                        onhandMaterialLotList.stream().filter(t -> !"Y".equals(t.getReservedFlag()))
                                        .collect(Collectors.groupingBy(b -> new UnReservedMaterialLot(b.getSiteId(),
                                                        b.getMaterialId(), b.getLocatorId(), b.getOwnerType(),
                                                        b.getOwnerId(), b.getLot())));

        // ????????????????????????????????????
        List<MtInvOnhandHoldVO> onhandReserveList = new ArrayList<>();
        // List<MtUomVO6> tempUoms = new ArrayList<>();
        for (Map.Entry<ReservedMaterialLot, List<MtMaterialLot>> entry : reservedMaterialLotsMap.entrySet()) {
            // ????????????????????????
            BigDecimal sumTrxPrimaryUomQty =
                            entry.getValue().stream()
                                            .collect(CollectorsUtil.summingBigDecimal(t -> null == t.getPrimaryUomQty()
                                                            ? BigDecimal.ZERO
                                                            : BigDecimal.valueOf(t.getPrimaryUomQty())));
            //
            // MtUomVO6 mtUomVO6 = new MtUomVO6();
            // mtUomVO6.setSourceUomId(entry.getValue().get(0).getPrimaryUomId());
            // mtUomVO6.setSourceValue(sumTrxPrimaryUomQty.doubleValue());
            // mtUomVO6.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            // tempUoms.add(mtUomVO6);

            // ?????? ????????????????????????
            MtInvOnhandHoldVO mtInvOnhandHoldVO = new MtInvOnhandHoldVO();
            mtInvOnhandHoldVO.setSiteId(entry.getKey().getSiteId());
            mtInvOnhandHoldVO.setMaterialId(entry.getKey().getMaterialId());
            mtInvOnhandHoldVO.setLocatorId(entry.getKey().getLocatorId());
            mtInvOnhandHoldVO.setLotCode(entry.getKey().getLot());
            mtInvOnhandHoldVO.setOwnerType(entry.getKey().getOwnerType());
            mtInvOnhandHoldVO.setOwnerId(entry.getKey().getOwnerId());
            mtInvOnhandHoldVO.setQuantity(sumTrxPrimaryUomQty.doubleValue());
            mtInvOnhandHoldVO.setHoldType("ORDER");
            mtInvOnhandHoldVO.setOrderType(entry.getKey().getReservedObjectType());
            mtInvOnhandHoldVO.setOrderId(entry.getKey().getReservedObjectId());
            // mtInvOnhandHoldVO.setPrimaryUomId(mtUomVO6.getSourceUomId());
            onhandReserveList.add(mtInvOnhandHoldVO);

        }

        if (CollectionUtils.isNotEmpty(onhandReserveList)) {
            // // ????????????
            // List<MtUomVO1> mtUomVO1s = mtUomRepository.uomBatchConversion(tenantId, tempUoms);
            // Map<MtUomVO7, MtUomVO1> mtUomVO1Map = mtUomVO1s.stream().collect(
            // Collectors.toMap(t -> new MtUomVO7(t.getSourceValue(), t.getSourceUomId()), t -> t));
            // for (MtInvOnhandHoldVO mtInvOnhandQuantityVO13 : onhandReserveList) {
            // MtUomVO7 mtUomVO7 = new MtUomVO7(mtInvOnhandQuantityVO13.getQuantity(),
            // mtInvOnhandQuantityVO13.getPrimaryUomId());
            // if (mtUomVO1Map.containsKey(mtUomVO7)) {
            // mtInvOnhandQuantityVO13.setQuantity(mtUomVO1Map.get(mtUomVO7).getTargetValue());
            // }
            // }
            MtInvOnhandHoldVO18 mtInvOnhandHoldVO18 = new MtInvOnhandHoldVO18();
            mtInvOnhandHoldVO18.setOnhandReserveList(Lists.newArrayList());
            mtInvOnhandHoldVO18.setEventId(materialLotBatchConsumeEventId);
            mtInvOnhandHoldVO18.setEventRequestId(dto.getEventRequestId());
            if (dto.getShiftDate() != null) {
                mtInvOnhandHoldVO18.setShiftDate(dto.getShiftDate());
            }
            mtInvOnhandHoldVO18.setShiftCode(dto.getShiftCode());
            mtInvOnhandHoldVO18.setWorkcellId(dto.getWorkcellId());
            mtInvOnhandHoldRepository.onhandReserveUseBatchProcess(tenantId, mtInvOnhandHoldVO18);
        }


        // ????????????????????????????????????
        // List<MtUomVO6> mtUomVO6s = new ArrayList<>();
        List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
        for (Map.Entry<UnReservedMaterialLot, List<MtMaterialLot>> entry : unReservedMaterialLotsMap.entrySet()) {
            // ????????????????????????
            BigDecimal sumTrxPrimaryUomQty =
                            entry.getValue().stream()
                                            .collect(CollectorsUtil.summingBigDecimal(t -> null == t.getPrimaryUomQty()
                                                            ? BigDecimal.ZERO
                                                            : BigDecimal.valueOf(t.getPrimaryUomQty())));
            // MtUomVO6 mtUomVO6 = new MtUomVO6();
            // mtUomVO6.setSourceUomId(entry.getValue().get(0).getPrimaryUomId());
            // mtUomVO6.setSourceValue(sumTrxPrimaryUomQty.doubleValue());
            // mtUomVO6.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
            // mtUomVO6s.add(mtUomVO6);

            UnReservedMaterialLot key = entry.getKey();
            MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
            mtInvOnhandQuantityVO13.setSiteId(key.getSiteId());
            mtInvOnhandQuantityVO13.setMaterialId(key.getMaterialId());
            mtInvOnhandQuantityVO13.setLocatorId(key.getLocatorId());
            mtInvOnhandQuantityVO13.setLotCode(key.getLot());
            mtInvOnhandQuantityVO13.setOwnerType(key.getOwnerType());
            mtInvOnhandQuantityVO13.setOwnerId(key.getOwnerId());
            mtInvOnhandQuantityVO13.setChangeQuantity(sumTrxPrimaryUomQty.doubleValue());
            // mtInvOnhandQuantityVO13.setPrimaryUomId(mtUomVO6.getSourceUomId());
            onhandList.add(mtInvOnhandQuantityVO13);

        }

        if (CollectionUtils.isNotEmpty(onhandList)) {
            // // ????????????
            // List<MtUomVO1> mtUomVO1s = mtUomRepository.uomBatchConversion(tenantId, mtUomVO6s);
            // Map<MtUomVO7, MtUomVO1> mtUomVO1Map = mtUomVO1s.stream().collect(
            // Collectors.toMap(t -> new MtUomVO7(t.getSourceValue(), t.getSourceUomId()), t -> t));
            // for (MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 : onhandList) {
            // MtUomVO7 mtUomVO7 = new MtUomVO7(mtInvOnhandQuantityVO13.getChangeQuantity(),
            // mtInvOnhandQuantityVO13.getPrimaryUomId());
            // if (mtUomVO1Map.containsKey(mtUomVO7)) {
            // mtInvOnhandQuantityVO13.setChangeQuantity(mtUomVO1Map.get(mtUomVO7).getTargetValue());
            // }
            // }
            MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
            mtInvOnhandQuantityVO16.setOnhandList(onhandList);
            mtInvOnhandQuantityVO16.setEventId(materialLotBatchConsumeEventId);
            mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
        }

        return materialLotBatchConsumeEventId;
    }

    @Override
    public List<MtMaterialLotVO19> materialLotAccumulate(Long tenantId, MtMaterialLotVO42 dto) {
        String apiName = "???API:materialLotAccumulate???";
        // ??????????????????????????????????????????
        if (MtIdHelper.isIdNull(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", apiName));
        }
        List<MtMaterialLotVO41> inputMaterialLotList = dto.getMaterialLotList();
        if (CollectionUtils.isEmpty(inputMaterialLotList)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotList", apiName));
        }
        Optional<MtMaterialLotVO41> pmrQtyLessThanZeroOp = inputMaterialLotList.stream()
                        .filter(t -> t.getTrxPrimaryUomQty() != null && t.getTrxPrimaryUomQty().compareTo(0.0D) < 0)
                        .findAny();
        if (pmrQtyLessThanZeroOp.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0007", "MATERIAL_LOT", "trxPrimaryUomQty", apiName));
        }

        Optional<MtMaterialLotVO41> scdQtyLessThanZeroOp = inputMaterialLotList.stream()
                        .filter(t -> t.getTrxSecondaryUomQty() != null && t.getTrxSecondaryUomQty().compareTo(0.0D) < 0)
                        .findAny();
        if (scdQtyLessThanZeroOp.isPresent()) {
            throw new MtException("MT_MATERIAL_LOT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0007", "MATERIAL_LOT", "trxSecondaryUomQty", apiName));
        }

        // ????????????????????????????????????ID
        List<String> materialLotCodes = inputMaterialLotList.stream().map(MtMaterialLotVO41::getMaterialLotCode)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialLotCodes)) {
            Map<String, String> materialLotCodeMap = self().selectByCondition(Condition.builder(MtMaterialLot.class)
                            .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                                            .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodes))
                            .build()).stream()
                            .collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode,
                                            MtMaterialLot::getMaterialLotId));
            inputMaterialLotList.forEach(t -> {
                if (MtIdHelper.isIdNull(t.getMaterialLotId())) {
                    t.setMaterialLotId(materialLotCodeMap.get(t.getMaterialLotCode()));
                }
            });
        }


        // ????????????????????????N?????????
        Set<String> enableFlagNSet = new HashSet<>(inputMaterialLotList.size());

        // ???????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????ID?????????????????????
        List<MtMaterialLotVO41> existInputMaterialLotIdList =
                        inputMaterialLotList.stream().filter(t -> MtIdHelper.isIdNotNull(t.getMaterialLotId()))
                                        .distinct().collect(Collectors.toList());
        List<String> materialLotIds = existInputMaterialLotIdList.stream().map(MtMaterialLotVO41::getMaterialLotId)
                        .filter(MtIdHelper::isIdNotNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            List<MtMaterialLot> materialLotList = self().materialLotPropertyBatchGet(tenantId, materialLotIds);
            // ????????????????????????????????????
            if (materialLotList.size() != materialLotIds.size()) {
                materialLotIds.removeAll(materialLotList.stream().map(MtMaterialLot::getMaterialLotId).distinct()
                                .collect(Collectors.toList()));
                String nonexistentIds = materialLotIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                throw new MtException("MT_MATERIAL_LOT_0074", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0074", "MATERIAL_LOT", nonexistentIds, apiName));
            }
            // ?????????????????????????????????????????????
            String rservedMaterialCodes = materialLotList.stream()
                            .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getReservedFlag()))
                            .map(MtMaterialLot::getMaterialLotCode).collect(Collectors.joining(","));
            if (rservedMaterialCodes.length() > 0) {
                throw new MtException("MT_MATERIAL_LOT_0148", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0148", "MATERIAL_LOT", rservedMaterialCodes, apiName));
            }

            Map<String, MtMaterialLot> materialLotMap =
                            materialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId, t -> t));
            for (MtMaterialLotVO41 inputMaterialLot : existInputMaterialLotIdList) {
                // ????????????????????????ID????????????????????????????????????????????????
                MtMaterialLot existMaterialLot = materialLotMap.get(inputMaterialLot.getMaterialLotId());
                if (MtBaseConstants.NO.equalsIgnoreCase(existMaterialLot.getEnableFlag())) {
                    // ?????????????????????????????????N???????????????????????????
                    enableFlagNSet.add(existMaterialLot.getMaterialLotId());

                } else if (MtBaseConstants.NO.equalsIgnoreCase(inputMaterialLot.getEnableFlag())) {
                    throw new MtException("MT_MATERIAL_LOT_0151",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0151",
                                                    "MATERIAL_LOT", existMaterialLot.getMaterialLotCode(), apiName));
                }
                // ??????materialId???????????????materialId??????????????????????????????
                if (MtIdHelper.isIdNotNull(inputMaterialLot.getMaterialId()) && MtIdHelper
                                .isNotSame(inputMaterialLot.getMaterialId(), existMaterialLot.getMaterialId())) {
                    throw new MtException("MT_MATERIAL_LOT_0150",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0150",
                                                    "MATERIAL_LOT", existMaterialLot.getMaterialLotCode(), apiName));
                }

            }
        }

        // ?????????????????????enableFlag???N,???????????????Y?????????????????????????????????????????????????????????????????????
        List<MtMaterialLotVO20> batchUpdateMaterialLotList = new ArrayList<>(inputMaterialLotList.size());
        inputMaterialLotList.forEach(t -> {
            MtMaterialLotVO20 mtMaterialLotVO20 = mtMaterialLotTransMapper.mtMaterialLotVO41ToMtMaterialLotVO20(t);
            if (MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag())) {
                if ((MtIdHelper.isIdNotNull(t.getMaterialLotId()) && enableFlagNSet.contains(t.getMaterialLotId()))) {
                    mtMaterialLotVO20.setTrxPrimaryUomQty(null);
                    mtMaterialLotVO20.setPrimaryUomQty(t.getTrxPrimaryUomQty());
                    mtMaterialLotVO20.setTrxSecondaryUomQty(null);
                    mtMaterialLotVO20.setIdentification(t.getIdentification());
                    mtMaterialLotVO20.setSecondaryUomQty(t.getTrxSecondaryUomQty());
                }
            }
            batchUpdateMaterialLotList.add(mtMaterialLotVO20);
        });
        // ???????????????????????????????????????????????????????????????????????????
        List<MtMaterialLotVO19> batchUpdateResult = self().materialLotBatchUpdate(tenantId, batchUpdateMaterialLotList,
                        dto.getEventId(), MtBaseConstants.NO);

        // ??????????????????????????????????????????????????????????????????????????????
        MtEventVO1 mtEventVO1 = mtEventRepository.eventGet(tenantId, dto.getEventId());
        if (mtEventVO1 == null) {
            throw new MtException("MT_MATERIAL_LOT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0004", "MATERIAL_LOT", "eventId", apiName));
        }

        MtEventType mtEventType = mtEventTypeRepository.eventTypeGet(tenantId, mtEventVO1.getEventTypeId());
        if (mtEventType == null) {
            throw new MtException("MT_MATERIAL_LOT_0153", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0153", "MATERIAL_LOT", apiName));
        }

        // ??????????????????????????????????????????onhandChangeType???????????????I,?????????????????????????????????????????????
        if (!(StringUtils.isEmpty(mtEventType.getOnhandChangeType())
                        || MtBaseConstants.ONHAND_CHANGE_TYPE.I.equalsIgnoreCase(mtEventType.getOnhandChangeType()))) {
            throw new MtException("MT_MATERIAL_LOT_0152", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0152", "MATERIAL_LOT", apiName));
        }

        // ???onhanChangeFlag
        if (MtBaseConstants.YES.equalsIgnoreCase(mtEventType.getOnhandChangeFlag())) {
            Map<String, Double> changeQtyMap = batchUpdateResult.stream().collect(Collectors
                            .toMap(MtMaterialLotVO19::getMaterialLotId, MtMaterialLotVO19::getTrxPrimaryUomQty));

            // enbaleFlag?????????Y????????????
            List<String> materialLotIdList =
                            changeQtyMap.keySet().stream().filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
            List<MtMaterialLot> enabledMaterialLotList = CollectionUtils.isEmpty(materialLotIdList)
                            ? Collections.emptyList()
                            : self().materialLotPropertyBatchGet(tenantId, materialLotIdList).stream()
                                            .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag()))
                                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(enabledMaterialLotList)) {
                List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>(enabledMaterialLotList.size());

                enabledMaterialLotList.forEach(t -> {
                    Double trxPrimaryUomQty = changeQtyMap.get(t.getMaterialLotId());
                    MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13(t.getSiteId(),
                                    t.getMaterialId(), t.getLocatorId(), t.getLot(), t.getOwnerType(), t.getOwnerId());
                    mtInvOnhandQuantityVO13.setChangeQuantity(trxPrimaryUomQty);
                    onhandList.add(mtInvOnhandQuantityVO13);
                });

                MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
                mtInvOnhandQuantityVO16.setEventId(dto.getEventId());
                mtInvOnhandQuantityVO16.setOnhandList(onhandList);
                mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
            }
        }
        return batchUpdateResult;
    }
}
