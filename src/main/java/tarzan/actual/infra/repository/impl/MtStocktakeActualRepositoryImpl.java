package tarzan.actual.infra.repository.impl;

import static io.tarzan.common.domain.util.StringHelper.isSame;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.repository.MtStocktakeActualHisRepository;
import tarzan.actual.domain.repository.MtStocktakeActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtStocktakeActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO3;
import tarzan.instruction.domain.vo.MtInstructionVO4;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.*;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO1;
import tarzan.stocktake.domain.entity.MtStocktakeDoc;
import tarzan.stocktake.domain.repository.MtStocktakeDocRepository;

/**
 * 盘点实绩表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtStocktakeActualRepositoryImpl extends BaseRepositoryImpl<MtStocktakeActual>
                implements MtStocktakeActualRepository {

    private static final String STOCKTAKE_ACTUAL_CREATE_EVENT_TYPE_CODE = "STOCKTAKE_ACTUAL_CREATE";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Autowired
    private MtEventRepository mtEventService;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotService;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailService;

    @Autowired
    private MtStocktakeActualHisRepository mtStocktakeActualHisService;

    @Autowired
    private MtStocktakeDocRepository mtStocktakeDocService;

    @Autowired
    private MtStocktakeActualMapper mtStocktakeActualMapper;

    @Autowired
    private MtMaterialRepository mtMaterialService;

    @Autowired
    private MtInstructionRepository mtLogisticInstructionService;

    @Autowired
    private MtContainerRepository mtContainerService;

    @Autowired
    private MtUomRepository mtUomService;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityService;

    @Autowired
    private MtInvOnhandHoldRepository mtInvOnhandHoldService;

    @Autowired
    private MtEventRequestRepository mtEventRequestService;

    @Override
    public List<MtStocktakeActual> stocktakeActualQuery(Long tenantId, MtStocktakeActual mtStocktakeActual) {
        mtStocktakeActual.setTenantId(tenantId);
        return mtStocktakeActualMapper.select(mtStocktakeActual);
    }

    @Override
    public List<MtStocktakeActual> stocktakeLimitActualPropertyQuery(Long tenantId, String stocktakeId) {
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001", "STOCKTAKE",
                                            "stocktakeId", "【API:stocktakeLimitActualPropertyQuery】"));
        }
        MtStocktakeActual queryActual = new MtStocktakeActual();
        queryActual.setTenantId(tenantId);
        queryActual.setStocktakeId(stocktakeId);
        List<MtStocktakeActual> stocktakeActualList = mtStocktakeActualMapper.select(queryActual);
        if (CollectionUtils.isEmpty(stocktakeActualList)) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0009", "STOCKTAKE", stocktakeId, "【API:stocktakeLimitActualPropertyQuery】"));
        }
        return stocktakeActualList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stocktakeActualUpdate(Long tenantId, MtStocktakeActualVO updateVO) {
        // step 1 - verify
        stocktakeActualUpdateVerify(tenantId, updateVO);

        // step 2 - check stocktake doc
        MtStocktakeDoc mtStocktakeDoc =
                        mtStocktakeDocService.stocktakeDocPropertyGet(tenantId, updateVO.getStocktakeId());
        if (mtStocktakeDoc == null) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0009", "STOCKTAKE", "stocktakeId", "【API:stocktakeActualUpdate】"));
        }

        // step 3,4 - check material lot
        MtMaterialLot mtMaterialLot =
                        mtMaterialLotService.materialLotPropertyGet(tenantId, updateVO.getMaterialLotId());
        if (mtMaterialLot == null) {
            throw new MtException("MT_STOCKTAKE_0019",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0019", "STOCKTAKE",
                                            updateVO.getMaterialLotId(), "【API:stocktakeActualUpdate】"));
        }

        // step 5,9,10 - construct stocktake actual object, do insert or update
        MtStocktakeActual mtStocktakeActual;
        // use stocktake & material lot to query stock actual
        MtStocktakeActual queryStocktakeActual = new MtStocktakeActual();
        queryStocktakeActual.setTenantId(tenantId);
        queryStocktakeActual.setStocktakeId(updateVO.getStocktakeId());
        queryStocktakeActual.setMaterialLotId(updateVO.getMaterialLotId());
        queryStocktakeActual = mtStocktakeActualMapper.selectOne(queryStocktakeActual);
        if (queryStocktakeActual == null) {
            mtStocktakeActual = constructStocktakeActual(tenantId, updateVO, mtMaterialLot, null);

            // step 8 - create event
            String eventId = updateVO.getEventId();
            if (StringUtils.isEmpty(updateVO.getEventId())) {
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventRequestId(updateVO.getEventRequestId());
                eventCreateVO.setEventTypeCode(STOCKTAKE_ACTUAL_CREATE_EVENT_TYPE_CODE);
                eventId = mtEventService.eventCreate(tenantId, eventCreateVO);
            }

            self().insertSelective(mtStocktakeActual);

            mtStocktakeActualHisService.saveStocktakeActualHistory(tenantId, mtStocktakeActual, eventId);
        } else {
            mtStocktakeActual = constructStocktakeActual(tenantId, updateVO, mtMaterialLot, queryStocktakeActual);

            self().updateByPrimaryKeySelective(mtStocktakeActual);

            mtStocktakeActual = new MtStocktakeActual();
            mtStocktakeActual.setTenantId(tenantId);
            mtStocktakeActual.setStocktakeActualId(queryStocktakeActual.getStocktakeActualId());
            mtStocktakeActual = mtStocktakeActualMapper.selectOne(mtStocktakeActual);

            mtStocktakeActualHisService.saveStocktakeActualHistory(tenantId, mtStocktakeActual, updateVO.getEventId());
        }
    }

    /**
     * 更新新增前校验
     *
     * @param tenantId IRequest
     * @param updateVO 更新&新增VO
     * @author benjamin
     * @date 2019-07-04 17:40
     */
    private void stocktakeActualUpdateVerify(Long tenantId, MtStocktakeActualVO updateVO) {
        if (StringUtils.isEmpty(updateVO.getMaterialLotId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "materialLotId", "【API:stocktakeActualUpdate】"));
        }
        if (StringUtils.isEmpty(updateVO.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API:stocktakeActualUpdate】"));
        }

        if (StringUtils.isNotEmpty(updateVO.getFirstcountMaterialId())) {
            if (updateVO.getFirstcountQuantity() == null) {
                throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0001", "STOCKTAKE", "firstcountQuantity", "【API:stocktakeActualUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(updateVO.getRecountMaterialId())) {
            if (updateVO.getRecountQuantity() == null) {
                throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0001", "STOCKTAKE", "recountQuantity", "【API:stocktakeActualUpdate】"));
            }
        }

        doubleFieldsStringValueCheck(tenantId, "firstcountOwnerType", updateVO.getFirstcountOwnerType(),
                        "firstcountOwnerId", updateVO.getFirstcountOwnerId());

        doubleFieldsStringValueCheck(tenantId, "firstcountReservedObjectTy", updateVO.getFirstcountReservedObjectTy(),
                        "firstcountReservedObjectId", updateVO.getFirstcountReservedObjectId());

        doubleFieldsStringValueCheck(tenantId, "recountReservedObjectType", updateVO.getRecountReservedObjectType(),
                        "recountReservedObjectId", updateVO.getRecountReservedObjectId());

        doubleFieldsStringValueCheck(tenantId, "recountOwnerType", updateVO.getRecountOwnerType(), "recountOwnerId",
                        updateVO.getRecountOwnerId());
    }

    /**
     * 校验两个String类型成员变量方法 两个变量不满足同时为空或同时非空时，抛出异常
     *
     * @param tenantId IRequest
     * @param firstFieldName 前一个字段名
     * @param firstFieldValue 前一个字段值
     * @param lastFieldName 后一个字段名
     * @param lastFieldValue 后一个字段值
     * @author benjamin
     * @date 2019-06-19 16:02
     */
    private void doubleFieldsStringValueCheck(Long tenantId, String firstFieldName, String firstFieldValue,
                    String lastFieldName, String lastFieldValue) {
        boolean emptyCheck = StringUtils.isEmpty(firstFieldValue) && StringUtils.isEmpty(lastFieldValue);
        boolean notEmptyCheck = StringUtils.isNotEmpty(firstFieldValue) && StringUtils.isNotEmpty(lastFieldValue);
        if (!(emptyCheck || notEmptyCheck)) {
            throw new MtException("MT_STOCKTAKE_0018",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0018", "STOCKTAKE",
                                            firstFieldName, lastFieldName, "【API:stocktakeActualUpdate】"));
        }
    }

    /**
     * 构建盘点指令实绩对象
     *
     * @param tenantId IRequest
     * @param updateVO 更新&新增VO
     * @param mtMaterialLot MtMaterialLot
     * @param originStocktakeActual 原始盘点指令实绩
     * @return MtStocktakeActual
     * @author benjamin
     * @date 2019-07-04 17:42
     */
    private MtStocktakeActual constructStocktakeActual(Long tenantId, MtStocktakeActualVO updateVO,
                    MtMaterialLot mtMaterialLot, MtStocktakeActual originStocktakeActual) {
        MtStocktakeActual mtStocktakeActual = new MtStocktakeActual();
        if (originStocktakeActual == null) {
            mtStocktakeActual.setSiteId(mtMaterialLot.getSiteId());
            mtStocktakeActual.setLotCode(mtMaterialLot.getLot());
            mtStocktakeActual.setMaterialId(mtMaterialLot.getMaterialId());
            mtStocktakeActual.setLocatorId(mtMaterialLot.getLocatorId());

            // get container
            MtContLoadDtlVO5 queryContLoadDtlVO = new MtContLoadDtlVO5();
            queryContLoadDtlVO.setLoadObjectType("MATERIAL_LOT");
            queryContLoadDtlVO.setLoadObjectId(mtMaterialLot.getMaterialLotId());
            queryContLoadDtlVO.setTopLevelFlag("N");
            List<String> containerIdList =
                            mtContainerLoadDetailService.objectLimitLoadingContainerQuery(tenantId, queryContLoadDtlVO);
            mtStocktakeActual.setContainerId(CollectionUtils.isEmpty(containerIdList) ? null : containerIdList.get(0));

            mtStocktakeActual.setOwnerType(mtMaterialLot.getOwnerType());
            mtStocktakeActual.setOwnerId(mtMaterialLot.getOwnerId());
            mtStocktakeActual.setReservedObjectType(mtMaterialLot.getReservedObjectType());
            mtStocktakeActual.setReservedObjectId(mtMaterialLot.getReservedObjectId());
            mtStocktakeActual.setUomId(mtMaterialLot.getPrimaryUomId());
            mtStocktakeActual.setCurrentQuantity(mtMaterialLot.getPrimaryUomQty());
            mtStocktakeActual.setAdjustFlag(
                            StringUtils.isEmpty(updateVO.getAdjustFlag()) ? "N" : updateVO.getAdjustFlag());
        } else {
            if (StringUtils.isEmpty(updateVO.getEventId())) {
                throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0001", "STOCKTAKE", "eventId", "【API:stocktakeActualUpdate】"));
            }
            mtStocktakeActual.setStocktakeActualId(originStocktakeActual.getStocktakeActualId());
            mtStocktakeActual.setAdjustFlag(updateVO.getAdjustFlag());
        }

        mtStocktakeActual.setTenantId(tenantId);
        mtStocktakeActual.setStocktakeId(updateVO.getStocktakeId());
        mtStocktakeActual.setMaterialLotId(updateVO.getMaterialLotId());
        mtStocktakeActual.setFirstcountMaterialId(updateVO.getFirstcountMaterialId());
        mtStocktakeActual.setFirstcountUomId(updateVO.getFirstcountUomId());
        mtStocktakeActual.setFirstcountLocatorId(updateVO.getFirstcountLocatorId());
        mtStocktakeActual.setFirstcountContainerId(updateVO.getFirstcountContainerId());
        mtStocktakeActual.setFirstcountLocationRow(updateVO.getFirstcountLocationRow());
        mtStocktakeActual.setFirstcountLocationColumn(updateVO.getFirstcountLocationColumn());
        mtStocktakeActual.setFirstcountOwnerType(updateVO.getFirstcountOwnerType());
        mtStocktakeActual.setFirstcountOwnerId(updateVO.getFirstcountOwnerId());
        mtStocktakeActual.setFirstcountReservedObjectTy(updateVO.getFirstcountReservedObjectTy());
        mtStocktakeActual.setFirstcountReservedObjectId(updateVO.getFirstcountReservedObjectId());
        mtStocktakeActual.setFirstcountQuantity(updateVO.getFirstcountQuantity());
        mtStocktakeActual.setFirstcountRemark(updateVO.getFirstcountRemark());
        mtStocktakeActual.setRecountMaterialId(updateVO.getRecountMaterialId());
        mtStocktakeActual.setRecountUomId(updateVO.getRecountUomId());
        mtStocktakeActual.setRecountLocatorId(updateVO.getRecountLocatorId());
        mtStocktakeActual.setRecountContainerId(updateVO.getRecountContainerId());
        mtStocktakeActual.setRecountLocationRow(updateVO.getRecountLocationRow());
        mtStocktakeActual.setRecountLocationColumn(updateVO.getRecountLocationColumn());
        mtStocktakeActual.setRecountOwnerType(updateVO.getRecountOwnerType());
        mtStocktakeActual.setRecountOwnerId(updateVO.getRecountOwnerId());
        mtStocktakeActual.setRecountReservedObjectType(updateVO.getRecountReservedObjectType());
        mtStocktakeActual.setRecountReservedObjectId(updateVO.getRecountReservedObjectId());
        mtStocktakeActual.setRecountQuantity(updateVO.getRecountQuantity());
        mtStocktakeActual.setRecountRemark(updateVO.getRecountRemark());

        return mtStocktakeActual;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stocktaketActualDifferenceCreate(Long tenantId, String stocktakeId, String eventRequestId) {
        // 校验参数的有效性
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API:stocktaketActualDifferenceCreate】"));
        }

        // 1.调用API{stocktakePropertyGet}
        MtStocktakeDoc mtStocktakeDoc = mtStocktakeDocService.stocktakeDocPropertyGet(tenantId, stocktakeId);
        if (mtStocktakeDoc == null || !"COUNTCOMPLETED".equals(mtStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0025", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0025", "STOCKTAKE", "【API:stocktaketActualDifferenceCreate】"));

        }
        // 2.调用API{stocktaketAndMaterialLotDifferenceQuery}
        List<String> list = mtStocktakeDocService.stocktaketAndMaterialLotDifferenceQuery(tenantId, stocktakeId);
        MtStocktakeActualVO mtStocktakeActualVO = new MtStocktakeActualVO();
        mtStocktakeActualVO.setStocktakeId(stocktakeId);
        // 3.创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("STOCKTAKE_ACTUAL_DIFFERENCE_CREATE");
        String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);
        mtStocktakeActualVO.setEventId(eventId);
        // 4. 对于第三步中每一个获取到的物料批信息，依次调用API{stocktakeActualUpdate}
        for (String s : list) {
            mtStocktakeActualVO.setMaterialLotId(s);
            stocktakeActualUpdate(tenantId, mtStocktakeActualVO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stocktakeFirstcount(Long tenantId, MtStocktakeActualVO1 dto) {
        // 1.校验参数的有效性
        if (StringUtils.isEmpty(dto.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API:stocktakeFirstcount】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "materialLotId", "【API:stocktakeFirstcount】"));
        }
        if (StringUtils.isEmpty(dto.getFirstcountMaterialId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "firstcountMaterialId", "【API:stocktakeFirstcount】"));
        }
        if (StringUtils.isEmpty(dto.getFirstcountLocatorId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "firstcountLocatorId", "【API:stocktakeFirstcount】"));
        }
        if (dto.getFirstcountQuantity() == null) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "firstcountQuantity", "【API:stocktakeFirstcount】"));
        }
        MtStocktakeDoc mtStocktakeDoc = mtStocktakeDocService.stocktakeDocPropertyGet(tenantId, dto.getStocktakeId());
        if (mtStocktakeDoc == null || !"RELEASED".equals(mtStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0017", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0017", "STOCKTAKE", "【API:stocktakeFirstcount】"));

        }

        // 2.获取物料批的信息
        MtMaterialLot mtMaterialLot = mtMaterialLotService.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
        if (mtMaterialLot == null) {
            throw new MtException("MT_STOCKTAKE_0019", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0019", "STOCKTAKE", dto.getMaterialLotId(), "【API:stocktakeFirstcount】"));
        }

        // 3.创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setEventTypeCode("STOCKTAKE_FIRSTCOUNT");
        String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

        // 4.根据物料批的信息更新实绩
        MtStocktakeActualVO mtStocktakeActualVO = new MtStocktakeActualVO();
        mtStocktakeActualVO.setStocktakeId(dto.getStocktakeId());
        mtStocktakeActualVO.setFirstcountMaterialId(dto.getFirstcountMaterialId());
        mtStocktakeActualVO.setFirstcountLocatorId(dto.getFirstcountLocatorId());
        mtStocktakeActualVO.setFirstcountContainerId(dto.getFirstcountContainerId());
        mtStocktakeActualVO.setFirstcountLocationRow(dto.getFirstcountLocationRow());
        mtStocktakeActualVO.setFirstcountLocationColumn(dto.getFirstcountLocationColumn());
        mtStocktakeActualVO.setFirstcountOwnerType(dto.getFirstcountOwnerType());
        mtStocktakeActualVO.setFirstcountOwnerId(dto.getFirstcountOwnerId());
        mtStocktakeActualVO.setFirstcountReservedObjectTy(dto.getFirstcountReservedObjectTy());
        mtStocktakeActualVO.setFirstcountReservedObjectId(dto.getFirstcountReservedObjectId());
        mtStocktakeActualVO.setFirstcountQuantity(dto.getFirstcountQuantity());
        mtStocktakeActualVO.setFirstcountRemark(dto.getFirstcountRemark());
        mtStocktakeActualVO.setAdjustFlag(null);
        mtStocktakeActualVO.setEventId(eventId);

        if (dto.getFirstcountUomId() == null) {
            if (dto.getFirstcountMaterialId().equals(mtMaterialLot.getMaterialId())) {
                mtStocktakeActualVO.setFirstcountUomId(mtMaterialLot.getPrimaryUomId());
            } else {
                MtMaterialVO mtMaterialVO =
                                mtMaterialService.materialPropertyGet(tenantId, dto.getFirstcountMaterialId());
                if (mtMaterialVO == null || StringUtils.isEmpty(mtMaterialVO.getPrimaryUomId())) {
                    throw new MtException("MT_STOCKTAKE_0027",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0027",
                                                    "STOCKTAKE", dto.getFirstcountMaterialId(),
                                                    "【API:stocktakeFirstcount】"));
                }
                mtStocktakeActualVO.setFirstcountUomId(mtMaterialVO.getPrimaryUomId());
            }
        } else {
            mtStocktakeActualVO.setFirstcountUomId(dto.getFirstcountUomId());
        }

        mtStocktakeActualVO.setMaterialLotId(dto.getMaterialLotId());

        stocktakeActualUpdate(tenantId, mtStocktakeActualVO);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stocktakeRecount(Long tenantId, MtStocktakeActualVO1 dto) {
        // 1.校验参数的有效性
        if (StringUtils.isEmpty(dto.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API:stocktakeRecount】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "materialLotId", "【API:stocktakeRecount】"));
        }
        if (StringUtils.isEmpty(dto.getRecountMaterialId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "recountMaterialId", "【API:stocktakeRecount】"));
        }
        if (StringUtils.isEmpty(dto.getRecountLocatorId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "recountLocatorId", "【API:stocktakeRecount】"));
        }
        if (dto.getRecountQuantity() == null) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "recountQuantity", "【API:stocktakeRecount】"));
        }
//        MtStocktakeDoc mtStocktakeDoc = mtStocktakeDocService.stocktakeDocPropertyGet(tenantId, dto.getStocktakeId());
//        if (mtStocktakeDoc == null || !"FIRSTCOUNTCOMPLETED".equals(mtStocktakeDoc.getStocktakeStatus())) {
//            throw new MtException("MT_STOCKTAKE_0026", mtErrorMessageService.getErrorMessageWithModule(tenantId,
//                            "MT_STOCKTAKE_0026", "STOCKTAKE", "【API:stocktakeRecount】"));
//
//        }

        // 2.获取物料批的信息
        MtStocktakeActual queryStocktakeAcutal = new MtStocktakeActual();
        queryStocktakeAcutal.setTenantId(tenantId);
        queryStocktakeAcutal.setStocktakeId(dto.getStocktakeId());
        queryStocktakeAcutal.setMaterialLotId(dto.getMaterialLotId());

        Criteria criteria = new Criteria(queryStocktakeAcutal);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtStocktakeActual.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtStocktakeActual.FIELD_STOCKTAKE_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtStocktakeActual.FIELD_MATERIAL_LOT_ID, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        // 3.创建事件
        List<MtStocktakeActual> stocktakeActualList = self().selectOptional(queryStocktakeAcutal, criteria);
        if (CollectionUtils.isNotEmpty(stocktakeActualList)) {
            queryStocktakeAcutal = stocktakeActualList.get(0);
        }
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setEventTypeCode("STOCKTAKE_RECOUNT");
        String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

        // 4.根据物料批的信息更新实绩
        MtStocktakeActualVO mtStocktakeActualVO = new MtStocktakeActualVO();
        mtStocktakeActualVO.setStocktakeId(dto.getStocktakeId());
        mtStocktakeActualVO.setRecountMaterialId(dto.getRecountMaterialId());
        mtStocktakeActualVO.setRecountLocatorId(dto.getRecountLocatorId());
        mtStocktakeActualVO.setRecountContainerId(dto.getRecountContainerId());
        mtStocktakeActualVO.setRecountLocationColumn(dto.getRecountLocationColumn());
        mtStocktakeActualVO.setRecountLocationRow(dto.getRecountLocationRow());
        mtStocktakeActualVO.setRecountOwnerType(dto.getRecountOwnerType());
        mtStocktakeActualVO.setRecountOwnerId(dto.getRecountOwnerId());
        mtStocktakeActualVO.setRecountReservedObjectType(dto.getRecountReservedObjectType());
        mtStocktakeActualVO.setRecountReservedObjectId(dto.getRecountReservedObjectId());
        mtStocktakeActualVO.setRecountQuantity(dto.getRecountQuantity());
        mtStocktakeActualVO.setRecountRemark(dto.getRecountRemark());
        mtStocktakeActualVO.setAdjustFlag(null);
        mtStocktakeActualVO.setEventId(eventId);

        if (dto.getRecountUomId() == null) {
            if (dto.getRecountMaterialId().equals(queryStocktakeAcutal.getFirstcountMaterialId())) {
                mtStocktakeActualVO.setRecountUomId(queryStocktakeAcutal.getFirstcountUomId());
            } else {
                MtMaterialVO mtMaterialVO = mtMaterialService.materialPropertyGet(tenantId, dto.getRecountMaterialId());
                if (mtMaterialVO == null || StringUtils.isEmpty(mtMaterialVO.getPrimaryUomId())) {
                    throw new MtException("MT_STOCKTAKE_0027",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0027",
                                                    "STOCKTAKE", dto.getRecountMaterialId(), "【API:stocktakeRecount】"));
                }
                mtStocktakeActualVO.setRecountUomId(mtMaterialVO.getPrimaryUomId());
            }
        } else {
            mtStocktakeActualVO.setRecountUomId(dto.getRecountUomId());
        }
        mtStocktakeActualVO.setMaterialLotId(dto.getMaterialLotId());
        stocktakeActualUpdate(tenantId, mtStocktakeActualVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stocktakeActualDifferenceAdjust(Long tenantId, MtStocktakeActualVO2 dto) {
        // 1.校验参数的有效性
        if (StringUtils.isEmpty(dto.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API:stocktakeActualDifferenceAdjust】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_STOCKTAKE_0001",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001", "STOCKTAKE",
                                            "materialLotId", "【API:stocktakeActualDifferenceAdjust】"));
        }
        // 补充生成REQUEST_id
        String eventRequestId = mtEventRequestService.eventRequestCreate(tenantId, "STOCKTAKE_ADJUST");
        // 2.校验AdjustFlag
        if (!("Y".equals(dto.getFirstCountAdjustFlag()) && !"Y".equals(dto.getRecountAdjustFlag())
                        || !"Y".equals(dto.getFirstCountAdjustFlag()) && "Y".equals(dto.getRecountAdjustFlag()))) {
            throw new MtException("MT_STOCKTAKE_0021", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0021", "STOCKTAKE", "【API:stocktakeActualDifferenceAdjust】"));
        }

        // 3.调用API{stocktakeActualQuery}
        MtStocktakeActual mtStocktakeActual = new MtStocktakeActual();
        mtStocktakeActual.setStocktakeId(dto.getStocktakeId());
        mtStocktakeActual.setMaterialLotId(dto.getMaterialLotId());
        List<MtStocktakeActual> actuals = stocktakeActualQuery(tenantId, mtStocktakeActual);
        if (CollectionUtils.isEmpty(actuals)) {
            throw new MtException("MT_STOCKTAKE_0019",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0019", "STOCKTAKE",
                                            dto.getMaterialLotId(), "【API:stocktakeActualDifferenceAdjust】"));
        }

        // 仅会获取到一条数据
        MtStocktakeActual stocktakeActual = actuals.get(0);

        MtStocktakeActualVO3 mtStocktakeActualVO3 = new MtStocktakeActualVO3();
        // 4.初盘数据
        if ("Y".equals(dto.getFirstCountAdjustFlag())) {
            if (stocktakeActual.getFirstcountQuantity() == null) {
                throw new MtException("MT_STOCKTAKE_0001",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001",
                                                "STOCKTAKE", "firstcountQuantity",
                                                "【API:stocktakeActualDifferenceAdjust】"));

            }
            mtStocktakeActualVO3.setAdjustMaterialid(stocktakeActual.getFirstcountMaterialId());
            mtStocktakeActualVO3.setAdjustUomId(stocktakeActual.getFirstcountUomId());
            mtStocktakeActualVO3.setAdjustLocatorid(stocktakeActual.getFirstcountLocatorId());
            mtStocktakeActualVO3.setAdjustContainerid(stocktakeActual.getFirstcountContainerId());
            mtStocktakeActualVO3.setAdjustcountLocationRow(stocktakeActual.getFirstcountLocationRow());
            mtStocktakeActualVO3.setAdjustcountLocationColumn(stocktakeActual.getFirstcountLocationColumn());
            mtStocktakeActualVO3.setAdjustOwnerType(stocktakeActual.getFirstcountOwnerType());
            mtStocktakeActualVO3.setAdjustOwnerId(stocktakeActual.getFirstcountOwnerId());
            mtStocktakeActualVO3.setAdjustReservedObjectType(stocktakeActual.getFirstcountReservedObjectTy());
            mtStocktakeActualVO3.setAdjustReservedObjectId(stocktakeActual.getFirstcountReservedObjectId());
            mtStocktakeActualVO3.setAdjustQuantity(stocktakeActual.getFirstcountQuantity());
        }

        // 5.复盘数据
        if ("Y".equals(dto.getRecountAdjustFlag())) {
            if (stocktakeActual.getRecountQuantity() == null) {
                throw new MtException("MT_STOCKTAKE_0001",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001",
                                                "STOCKTAKE", "recountQuantity",
                                                "【API:stocktakeActualDifferenceAdjust】"));
            }

            mtStocktakeActualVO3.setAdjustMaterialid(stocktakeActual.getRecountMaterialId());
            mtStocktakeActualVO3.setAdjustUomId(stocktakeActual.getRecountUomId());
            mtStocktakeActualVO3.setAdjustLocatorid(stocktakeActual.getRecountLocatorId());
            mtStocktakeActualVO3.setAdjustContainerid(stocktakeActual.getRecountContainerId());
            mtStocktakeActualVO3.setAdjustcountLocationRow(stocktakeActual.getRecountLocationRow());
            mtStocktakeActualVO3.setAdjustcountLocationColumn(stocktakeActual.getRecountLocationColumn());
            mtStocktakeActualVO3.setAdjustOwnerType(stocktakeActual.getRecountOwnerType());
            mtStocktakeActualVO3.setAdjustOwnerId(stocktakeActual.getRecountOwnerId());
            mtStocktakeActualVO3.setAdjustReservedObjectType(stocktakeActual.getRecountReservedObjectType());
            mtStocktakeActualVO3.setAdjustReservedObjectId(stocktakeActual.getRecountReservedObjectId());
            mtStocktakeActualVO3.setAdjustQuantity(stocktakeActual.getRecountQuantity());
        }

        // 6.调用API{materialLotPropertyGet}
        MtMaterialLot mtMaterialLot = mtMaterialLotService.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
        if (null == mtMaterialLot) {
            throw new MtException("MT_STOCKTAKE_0019",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0019", "STOCKTAKE",
                                            dto.getMaterialLotId(), "【API:stocktakeActualDifferenceAdjust】"));
        }

        // 7.比较第三步的数据同时为空表示相同

        // 8.调用API{objectLimitLoadingContainerQuery}
        MtContLoadDtlVO5 queryContLoadDtlVO = new MtContLoadDtlVO5();
        queryContLoadDtlVO.setLoadObjectType("MATERIAL_LOT");
        queryContLoadDtlVO.setLoadObjectId(mtMaterialLot.getMaterialLotId());
        queryContLoadDtlVO.setTopLevelFlag("N");
        List<String> containerIdList =
                        mtContainerLoadDetailService.objectLimitLoadingContainerQuery(tenantId, queryContLoadDtlVO);
        String containerId = null;
        if (CollectionUtils.isEmpty(containerIdList)) {
            containerId = "";
        } else {
            containerId = containerIdList.get(0);
        }

        // 9.将第三步的containerId与第八步的containerId比较

        String mtMaterialLotEnableFlag = null == mtMaterialLot.getEnableFlag() ? "" : mtMaterialLot.getEnableFlag();
        boolean checkStocktakeFlag = false;
        // 10.第8.1步，如果adjustQuantity>0或者第六步primaryUomQty>0且第六步stocktakeFlag=Y：
        if ((BigDecimal.valueOf(mtStocktakeActualVO3.getAdjustQuantity() == null ? 0.0D
                        : mtStocktakeActualVO3.getAdjustQuantity())
                        .compareTo(BigDecimal.ZERO) > 0
                        || (BigDecimal
                                        .valueOf(mtMaterialLot.getPrimaryUomQty() == null ? 0.0D
                                                        : mtMaterialLot.getPrimaryUomQty())
                                        .compareTo(BigDecimal.ZERO) > 0 && "Y".equals(mtMaterialLotEnableFlag)))
                        && "Y".equals(mtMaterialLot.getStocktakeFlag())) {
            // 创建事件
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCKTAKE_MATERIALLOT_UNLOCK");
            eventCreateVO.setEventRequestId(eventRequestId);
            String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

            // 对物料批进行更新
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLotVO2.setStocktakeFlag("N");
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotService.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

            checkStocktakeFlag = true;
        }

        // 10.如果第三步currentQuantity>0，则进行杂出
        Boolean flag1 = false;
        Double primaryUomQty = mtMaterialLot.getPrimaryUomQty() == null ? Double.valueOf(0.0D)
                        : mtMaterialLot.getPrimaryUomQty();
        if (BigDecimal.valueOf(primaryUomQty).compareTo(BigDecimal.ZERO) > 0 && "Y".equals(mtMaterialLotEnableFlag)) {
            if (StringUtils.isEmpty(dto.getMiscellaneousIssueCostCenterId())) {
                throw new MtException("MT_STOCKTAKE_0001",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001",
                                                "STOCKTAKE", "miscellaneousIssueCostCenterId",
                                                "【API:stocktakeActualDifferenceAdjust】"));
            }

            // 指令创建
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            mtInstructionVO.setInstructionType("SHIP_TO_MISCELLANEOUS");
            mtInstructionVO.setSiteId(mtMaterialLot.getSiteId());
            mtInstructionVO.setMaterialId(mtMaterialLot.getMaterialId());
            mtInstructionVO.setFromSiteId(mtMaterialLot.getSiteId());
            mtInstructionVO.setFromLocatorId(mtMaterialLot.getLocatorId());
            mtInstructionVO.setCostCenterId(dto.getMiscellaneousIssueCostCenterId());
            mtInstructionVO.setOrderType("STOCKTAKE");
            mtInstructionVO.setOrderId(dto.getStocktakeId());
            mtInstructionVO.setBusinessType(dto.getMiscellaneousIssueBusinessType());
            mtInstructionVO.setRemark(dto.getMiscellaneousIssueRemark());
            mtInstructionVO.setFromOwnerType(mtMaterialLot.getOwnerType());
            mtInstructionVO.setToOwnerType(mtMaterialLot.getOwnerType());

            mtInstructionVO.setNumIncomingValueList(dto.getIssueNumIncomingValueList());

            MtMaterialVO mtMaterialVO = mtMaterialService.materialPropertyGet(tenantId, mtMaterialLot.getMaterialId());
            if (mtMaterialVO != null) {
                mtInstructionVO.setUomId(mtMaterialVO.getPrimaryUomId());
            }

            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
            transferUomVO.setSourceValue(mtMaterialLot.getPrimaryUomQty());
            transferUomVO.setTargetUomId(mtInstructionVO.getUomId());
            transferUomVO = mtUomService.uomConversion(tenantId, transferUomVO);
            mtInstructionVO.setQuantity(transferUomVO.getTargetValue());

            mtInstructionVO.setEventRequestId(eventRequestId);
            String instructionId = mtLogisticInstructionService.instructionUpdate(tenantId, mtInstructionVO, "N")
                            .getInstructionId();

            // 下达指令
            mtLogisticInstructionService.instructionRelease(tenantId, instructionId, eventRequestId);

            // 执行指令
            MtInstructionVO3 logisticInstructionVO3 = new MtInstructionVO3();
            List<MtInstructionVO3.MaterialLotList> list = new ArrayList<>();
            MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();

            materialLotList.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            materialLotList.setQty(mtMaterialLot.getPrimaryUomQty());
            materialLotList.setContainerId(containerId);
            //materialLotList.setLotCode(mtMaterialLot.getLot());
            materialLotList.setFromLocatorId(null == mtMaterialLot.getLocatorId() ? "" : mtMaterialLot.getLocatorId());
            materialLotList.setUomId(mtMaterialLot.getPrimaryUomId());
            list.add(materialLotList);

            logisticInstructionVO3.setInstructionId(instructionId);
            logisticInstructionVO3.setMaterialLotMessageList(list);
            logisticInstructionVO3.setEventRequestId(eventRequestId);

            mtLogisticInstructionService.instructionExecute(tenantId, logisticInstructionVO3);

            // 更新完成状态
            mtLogisticInstructionService.instructionComplete(tenantId, instructionId, eventRequestId);

            // 对物料批进行消耗
            MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
            mtMaterialLotVO1.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotVO1.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty());
            mtMaterialLotVO1.setEventRequestId(eventRequestId);
            if (mtMaterialVO != null && StringUtils.isNotEmpty(mtMaterialVO.getSecondaryUomId())) {
                mtMaterialLotVO1.setSecondaryUomId(mtMaterialVO.getSecondaryUomId());
                mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
            }
            mtMaterialLotService.materialLotConsume(tenantId, mtMaterialLotVO1);
        }

        // 11.如果adjustQuantity>0,则进行杂收业
        if (BigDecimal.valueOf(mtStocktakeActualVO3.getAdjustQuantity() == null ? 0.0D
                        : mtStocktakeActualVO3.getAdjustQuantity()).compareTo(BigDecimal.ZERO) == 1) {
            if (StringUtils.isEmpty(dto.getMiscellaneousReceiptCostCenterId())) {
                throw new MtException("MT_STOCKTAKE_0001",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001",
                                                "STOCKTAKE", "miscellaneousReceiptCostCenterId",
                                                "【API:stocktakeActualDifferenceAdjust】"));
            }
            flag1 = true;
            // 指令创建
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            mtInstructionVO.setInstructionType("RECEIVE_FROM_COSTCENTER");
            mtInstructionVO.setSiteId(mtMaterialLot.getSiteId());
            mtInstructionVO.setMaterialId(mtStocktakeActualVO3.getAdjustMaterialid());
            mtInstructionVO.setToSiteId(mtMaterialLot.getSiteId());
            mtInstructionVO.setToLocatorId(mtStocktakeActualVO3.getAdjustLocatorid());
            mtInstructionVO.setCostCenterId(dto.getMiscellaneousReceiptCostCenterId());
            mtInstructionVO.setOrderType("STOCKTAKE");
            mtInstructionVO.setOrderId(dto.getStocktakeId());
            mtInstructionVO.setBusinessType(dto.getMiscellaneousReceiptBusinessType());
            mtInstructionVO.setRemark(dto.getMiscellaneousReceiptRemark());
            mtInstructionVO.setFromOwnerType(mtStocktakeActualVO3.getAdjustOwnerType());
            mtInstructionVO.setToOwnerType(mtStocktakeActualVO3.getAdjustOwnerType());

            mtInstructionVO.setNumIncomingValueList(dto.getReceiptNumIncomingValueList());

            MtMaterialVO mtMaterialVO =
                            mtMaterialService.materialPropertyGet(tenantId, mtStocktakeActualVO3.getAdjustMaterialid());
            if (mtMaterialVO != null) {
                mtInstructionVO.setUomId(mtMaterialVO.getPrimaryUomId());
            }

            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtStocktakeActualVO3.getAdjustUomId());
            transferUomVO.setSourceValue(mtStocktakeActualVO3.getAdjustQuantity());
            transferUomVO.setTargetUomId(mtInstructionVO.getUomId());
            transferUomVO = mtUomService.uomConversion(tenantId, transferUomVO);
            mtInstructionVO.setQuantity(transferUomVO.getTargetValue());

            mtInstructionVO.setEventRequestId(eventRequestId);
            String instructionId = mtLogisticInstructionService.instructionUpdate(tenantId, mtInstructionVO, "N")
                            .getInstructionId();

            // 下达指令
            mtLogisticInstructionService.instructionRelease(tenantId, instructionId, eventRequestId);

            // 执行指令
            MtInstructionVO3 logisticInstructionVO3 = new MtInstructionVO3();

            List<MtInstructionVO3.MaterialLotList> list = new ArrayList<>();
            MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();
            materialLotList.setMaterialLotId(stocktakeActual.getMaterialLotId());
            materialLotList.setQty(mtStocktakeActualVO3.getAdjustQuantity());
            materialLotList.setContainerId(mtStocktakeActualVO3.getAdjustContainerid());
            //materialLotList.setLotCode(stocktakeActual.getLotCode());
            materialLotList.setToLocatorId(
                    null == stocktakeActual.getLocatorId() ? "" : stocktakeActual.getLocatorId());
            materialLotList.setUomId(mtStocktakeActualVO3.getAdjustUomId());
            list.add(materialLotList);

            logisticInstructionVO3.setInstructionId(instructionId);
            logisticInstructionVO3.setMaterialLotMessageList(list);
            logisticInstructionVO3.setEventRequestId(eventRequestId);

            MtInstructionVO4 mtInstructionVO4 =
                            mtLogisticInstructionService.instructionExecute(tenantId, logisticInstructionVO3);

            // 更新完成状态
            mtLogisticInstructionService.instructionComplete(tenantId, instructionId, eventRequestId);

            // 对物料批进行更新
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLotVO2.setEnableFlag("Y");
            mtMaterialLotVO2.setMaterialId(mtStocktakeActualVO3.getAdjustMaterialid());
            mtMaterialLotVO2.setPrimaryUomId(mtStocktakeActualVO3.getAdjustUomId());
            mtMaterialLotVO2.setPrimaryUomQty(mtStocktakeActualVO3.getAdjustQuantity());
            mtMaterialLotVO2.setLocatorId(mtStocktakeActualVO3.getAdjustLocatorid());
            mtMaterialLotVO2.setOwnerType(mtStocktakeActualVO3.getAdjustOwnerType());
            mtMaterialLotVO2.setOwnerId(mtStocktakeActualVO3.getAdjustOwnerId());
            mtMaterialLotVO2.setReservedObjectType(mtStocktakeActualVO3.getAdjustReservedObjectType());
            mtMaterialLotVO2.setReservedObjectId(mtStocktakeActualVO3.getAdjustReservedObjectId());
            if (StringUtils.isNotEmpty(mtMaterialLotVO2.getReservedObjectId())) {
                mtMaterialLotVO2.setReservedFlag("Y");
            } else {
                mtMaterialLotVO2.setReservedFlag("N");
            }
            if (mtInstructionVO4 != null) {
                mtMaterialLotVO2.setEventId(mtInstructionVO4.getEventId());
            }
            if (StringUtils.isEmpty(mtMaterialLot.getSecondaryUomId())
                            && StringUtils.isNotEmpty(mtMaterialVO.getSecondaryUomId())) {
                mtMaterialLotVO2.setSecondaryUomId(mtMaterialVO.getSecondaryUomId());
                mtMaterialLotVO2.setSecondaryUomQty(0.0D);
            }
            mtMaterialLotService.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

            // 如果adjustContainerId不为空，则进行容器装载
            if (StringUtils.isNotEmpty(mtStocktakeActualVO3.getAdjustContainerid())) {
                MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
                mtContainerVO24.setContainerId(mtStocktakeActualVO3.getAdjustContainerid());
                mtContainerVO24.setLocationRow(mtStocktakeActualVO3.getAdjustcountLocationRow());
                mtContainerVO24.setLocationColumn(mtStocktakeActualVO3.getAdjustcountLocationColumn());
                mtContainerVO24.setLoadObjectType("MATERIAL_LOT");
                mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
                mtContainerVO24.setEventRequestId(eventRequestId);
                mtContainerService.containerLoad(tenantId, mtContainerVO24);
            }

            // 现有量更新
            transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(mtStocktakeActualVO3.getAdjustUomId());
            transferUomVO.setSourceValue(mtStocktakeActualVO3.getAdjustQuantity());
            transferUomVO.setTargetUomId(mtInstructionVO.getUomId());
            transferUomVO = mtUomService.uomConversion(tenantId, transferUomVO);

            MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
            updateOnhandVO.setSiteId(mtMaterialLot.getSiteId());
            updateOnhandVO.setMaterialId(mtStocktakeActualVO3.getAdjustMaterialid());
            updateOnhandVO.setLocatorId(mtStocktakeActualVO3.getAdjustLocatorid());
            updateOnhandVO.setLotCode(mtMaterialLot.getLot());
            if (transferUomVO != null) {
                updateOnhandVO.setChangeQuantity(transferUomVO.getTargetValue());
            }
            updateOnhandVO.setOwnerType(mtStocktakeActualVO3.getAdjustOwnerType());
            updateOnhandVO.setOwnerId(mtStocktakeActualVO3.getAdjustOwnerId());
            if (mtInstructionVO4 != null) {
                updateOnhandVO.setEventId(mtInstructionVO4.getEventId());
            }
            mtInvOnhandQuantityService.onhandQtyUpdateProcess(tenantId, updateOnhandVO);


            if (StringUtils.isNotEmpty(mtStocktakeActualVO3.getAdjustReservedObjectId())) {
                MtInvOnhandHoldVO8 onhandReleaseVO2 = new MtInvOnhandHoldVO8();
                onhandReleaseVO2.setSiteId(mtMaterialLot.getSiteId());
                onhandReleaseVO2.setMaterialId(mtStocktakeActualVO3.getAdjustMaterialid());
                onhandReleaseVO2.setLocatorId(mtStocktakeActualVO3.getAdjustLocatorid());
                onhandReleaseVO2.setLotCode(mtMaterialLot.getLot());
                onhandReleaseVO2.setHoldQuantity(transferUomVO.getTargetValue());
                onhandReleaseVO2.setOwnerType(mtStocktakeActualVO3.getAdjustOwnerType());
                onhandReleaseVO2.setOwnerId(mtStocktakeActualVO3.getAdjustOwnerId());
                onhandReleaseVO2.setHoldType("ORDER");
                onhandReleaseVO2.setOrderType(mtStocktakeActualVO3.getAdjustReservedObjectType());
                onhandReleaseVO2.setOrderId(mtStocktakeActualVO3.getAdjustReservedObjectId());
                onhandReleaseVO2.setEventId(mtInstructionVO4.getEventId());
                mtInvOnhandHoldService.onhandReserveCreateProcess(tenantId, onhandReleaseVO2);
            }
        }

        if (checkStocktakeFlag) {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCKTAKE_MATERIALLOT_LOCK");
            eventCreateVO.setEventRequestId(eventRequestId);
            String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

            // 对物料批进行更新
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLotVO2.setStocktakeFlag("Y");
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotService.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        }

        // 12.如adjustQuantity>0或者currentQuantity>0，更新调整标识
        if (flag1 || (BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) > 0
                        && "Y".equals(mtMaterialLotEnableFlag))) {
            // 创建事件
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCKTAKE_ADJUST");
            eventCreateVO.setEventRequestId(eventRequestId);
            String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

            MtStocktakeActualVO mtStocktakeActualVO = new MtStocktakeActualVO();
            mtStocktakeActualVO.setStocktakeId(dto.getStocktakeId());
            mtStocktakeActualVO.setMaterialLotId(dto.getMaterialLotId());
            mtStocktakeActualVO.setAdjustFlag("Y");
            mtStocktakeActualVO.setEventId(eventId);
            stocktakeActualUpdate(tenantId, mtStocktakeActualVO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stocktakeActualDifferenceBatchAdjust(Long tenantId, MtStocktakeActualVO4 dto) {
        // 校验参数的有效性
        if (StringUtils.isEmpty(dto.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0001",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001", "STOCKTAKE",
                                            "stocktakeId", "【API:stocktakeActualDifferenceBatchAdjust】"));
        }

        // 1.调用API{stocktakePropertyGet}
        MtStocktakeDoc mtStocktakeDoc = mtStocktakeDocService.stocktakeDocPropertyGet(tenantId, dto.getStocktakeId());
        if (mtStocktakeDoc == null || !"COUNTCOMPLETED".equals(mtStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0025", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0025", "STOCKTAKE", "【API:stocktakeActualDifferenceBatchAdjust】"));
        }

        String eventRequestId = this.mtEventRequestService.eventRequestCreate(tenantId, "STOCKTAKE_BATCH_ADJUST");

        // 2.如果allAdjustFlag=”Y”
        List<MtStocktakeActual> list = new ArrayList<>();
        if ("Y".equals(dto.getAllAdjustFlag())) {
            MtStocktakeActual mtStocktakeActual = new MtStocktakeActual();
            mtStocktakeActual.setStocktakeId(dto.getStocktakeId());
            List<MtStocktakeActual> mtStocktakeActuals = stocktakeActualQuery(tenantId, mtStocktakeActual);
            list.addAll(mtStocktakeActuals.stream().filter(t -> !"Y".equals(t.getAdjustFlag()))
                            .collect(Collectors.toList()));
            dto.setMaterialLotIdList(mtStocktakeActuals.stream().map(MtStocktakeActual::getMaterialLotId)
                            .collect(Collectors.toList()));
        } else {
            // 3.如果allAdjustFlag≠”Y”（为空或者N）
            if (CollectionUtils.isEmpty(dto.getMaterialLotIdList())) {
                throw new MtException("MT_STOCKTAKE_0001",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001",
                                                "STOCKTAKE", "materialLotIdList",
                                                "【API:stocktakeActualDifferenceBatchAdjust】"));
            }

            MtStocktakeActual mtStocktakeActual = new MtStocktakeActual();
            mtStocktakeActual.setStocktakeId(dto.getStocktakeId());
            List<MtStocktakeActual> mtStocktakeActuals = stocktakeActualQuery(tenantId, mtStocktakeActual);

            list.addAll(mtStocktakeActuals.stream()
                            .filter(t -> dto.getMaterialLotIdList().contains(t.getMaterialLotId()))
                            .collect(Collectors.toList()));
            List<MtStocktakeActual> temp =
                            list.stream().filter(t -> "Y".equals(t.getAdjustFlag())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(temp)) {
                throw new MtException("MT_STOCKTAKE_0023",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0023",
                                                "STOCKTAKE", temp.stream().map(MtStocktakeActual::getMaterialLotId)
                                                                .collect(Collectors.toList()).toString(),
                                                "【API:stocktakeActualDifferenceBatchAdjust】"));
            }
        }

        // 4.根据allAdjustFlag是否等于Y来判断获取的是第二步的实绩信息还是第三步的实绩信息
        if (list.stream().anyMatch(
                        t -> StringUtils.isNotEmpty(t.getRecountMaterialId()) && t.getRecountQuantity() == null)) {
            throw new MtException("MT_STOCKTAKE_0001",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001", "STOCKTAKE",
                                            "recountQuantity", "【API:stocktakeActualDifferenceBatchAdjust】"));

        }
        if (list.stream().anyMatch(t -> StringUtils.isEmpty(t.getRecountMaterialId())
                        && StringUtils.isNotEmpty(t.getFirstcountMaterialId()) && t.getFirstcountQuantity() == null)) {
            throw new MtException("MT_STOCKTAKE_0001",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001", "STOCKTAKE",
                                            "firstcountQuantity", "【API:stocktakeActualDifferenceBatchAdjust】"));
        }

        String materialLotLockFlag = StringUtils.isEmpty(mtStocktakeDoc.getMaterialLotLockFlag()) ? "N"
                        : mtStocktakeDoc.getMaterialLotLockFlag();

        MtStocktakeActualVO3 mtStocktakeActualVO3 = null;
        MtStocktakeActualVO5 mtStocktakeActualVO5 = null;
        List<MtStocktakeActualVO5> actualVO5List = new ArrayList<MtStocktakeActualVO5>();

        // 盘点调整标识
        Boolean adjustFlag = false;
        for (MtStocktakeActual t : list) {
            mtStocktakeActualVO3 = new MtStocktakeActualVO3();
            mtStocktakeActualVO5 = new MtStocktakeActualVO5();
            if (StringUtils.isNotEmpty(t.getRecountMaterialId())) {
                mtStocktakeActualVO3.setAdjustMaterialid(t.getRecountMaterialId());
                mtStocktakeActualVO3.setAdjustUomId(t.getRecountUomId());
                mtStocktakeActualVO3.setAdjustLocatorid(t.getRecountLocatorId());
                mtStocktakeActualVO3.setAdjustContainerid(t.getRecountContainerId());
                mtStocktakeActualVO3.setAdjustOwnerType(t.getRecountOwnerType());
                mtStocktakeActualVO3.setAdjustOwnerId(t.getRecountOwnerId());
                mtStocktakeActualVO3.setAdjustReservedObjectType(t.getRecountReservedObjectType());
                mtStocktakeActualVO3.setAdjustReservedObjectId(t.getRecountReservedObjectId());
                mtStocktakeActualVO3.setAdjustQuantity(t.getRecountQuantity());
                mtStocktakeActualVO3.setAdjustRemark(t.getRecountRemark());
            }
            if (StringUtils.isEmpty(t.getRecountMaterialId()) && StringUtils.isNotEmpty(t.getFirstcountMaterialId())) {
                mtStocktakeActualVO3.setAdjustMaterialid(t.getFirstcountMaterialId());
                mtStocktakeActualVO3.setAdjustUomId(t.getFirstcountUomId());
                mtStocktakeActualVO3.setAdjustLocatorid(t.getFirstcountLocatorId());
                mtStocktakeActualVO3.setAdjustContainerid(t.getFirstcountContainerId());
                mtStocktakeActualVO3.setAdjustOwnerType(t.getFirstcountOwnerType());
                mtStocktakeActualVO3.setAdjustOwnerId(t.getFirstcountOwnerId());
                mtStocktakeActualVO3.setAdjustReservedObjectType(t.getFirstcountReservedObjectTy());
                mtStocktakeActualVO3.setAdjustReservedObjectId(t.getFirstcountReservedObjectId());
                mtStocktakeActualVO3.setAdjustQuantity(t.getFirstcountQuantity());
                mtStocktakeActualVO3.setAdjustRemark(t.getFirstcountRemark());
            }
            if (StringUtils.isEmpty(t.getFirstcountMaterialId()) && StringUtils.isEmpty(t.getRecountMaterialId())) {
                mtStocktakeActualVO3.setAdjustMaterialid(t.getMaterialId());
                mtStocktakeActualVO3.setAdjustQuantity(0.0D);
                adjustFlag = true;
            } else {
                adjustFlag = false;
            }

            // 5.调用materialLotPropertyGet
            MtMaterialLot mtMaterialLot = mtMaterialLotService.materialLotPropertyGet(tenantId, t.getMaterialLotId());
            Map<String, String> tempMap = new HashMap<String, String>();
            Boolean flag = false;
            if (!isSame(mtMaterialLot.getSiteId(), t.getSiteId())) {
                tempMap.put("materialLotId/siteId", mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getSiteId());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getMaterialLotId(), t.getMaterialLotId())) {
                tempMap.put("materialLotId", mtMaterialLot.getMaterialLotId());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getLot(), t.getLotCode())) {
                tempMap.put("materialLotId/lotCode", mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getLot());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getMaterialId(), t.getMaterialId())) {
                tempMap.put("materialLotId/materialId",
                                mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getMaterialId());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getLocatorId(), t.getLocatorId())) {
                tempMap.put("materialLotId/locatorId",
                                mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getLocatorId());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getOwnerType(), t.getOwnerType())) {
                tempMap.put("materialLotId/ownerType",
                                mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getOwnerType());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getOwnerId(), t.getOwnerId())) {
                tempMap.put("materialLotId/ownerId",
                                mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getOwnerId());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getReservedObjectId(), t.getReservedObjectId())) {
                tempMap.put("materialLotId/reservedObjectId",
                                mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getReservedObjectId());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getReservedObjectType(), t.getReservedObjectType())) {
                tempMap.put("materialLotId/reservedObjectType",
                                mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getReservedObjectType());
                flag = true;
            }
            if (!isSame(mtMaterialLot.getPrimaryUomId(), t.getUomId())) {
                tempMap.put("materialLotId/primaryUomId",
                                mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getPrimaryUomId());
                flag = true;
            }
            if (!(mtMaterialLot.getPrimaryUomQty() == null && t.getCurrentQuantity() == null) && BigDecimal
                            .valueOf(mtMaterialLot.getPrimaryUomQty() == null ? 0.0D : mtMaterialLot.getPrimaryUomQty())
                            .compareTo(BigDecimal.valueOf(
                                            t.getCurrentQuantity() == null ? 0.0D : t.getCurrentQuantity())) != 0) {
                tempMap.put("materialLotId/primaryUomQty",
                                mtMaterialLot.getMaterialLotId() + "/" + mtMaterialLot.getPrimaryUomQty());
                flag = true;
            }
            if (flag) {
                throw new MtException("MT_STOCKTAKE_0022",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0022",
                                                "STOCKTAKE", tempMap.toString(),
                                                "【API:stocktakeActualDifferenceBatchAdjust】"));
            }

            MtContLoadDtlVO5 queryContLoadDtlVO = new MtContLoadDtlVO5();
            queryContLoadDtlVO.setLoadObjectType("MATERIAL_LOT");
            queryContLoadDtlVO.setLoadObjectId(mtMaterialLot.getMaterialLotId());
            queryContLoadDtlVO.setTopLevelFlag("N");
            List<String> containerIdList =
                            mtContainerLoadDetailService.objectLimitLoadingContainerQuery(tenantId, queryContLoadDtlVO);
            String queryContainerId = CollectionUtils.isEmpty(containerIdList) ? "" : containerIdList.get(0);
            if (!isSame(t.getContainerId(), queryContainerId)) {
                throw new MtException("MT_STOCKTAKE_0022", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0022", "STOCKTAKE", "materialLotId/containerId:"
                                                + mtMaterialLot.getMaterialLotId() + "/" + t.getContainerId(),
                                "【API:stocktakeActualDifferenceBatchAdjust】"));
            }

            // 2019-07-25 add by zijin.liang
            if ("Y".equals(materialLotLockFlag)) {
                // 创建事件
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("STOCKTAKE_MATERIALLOT_UNLOCK");
                eventCreateVO.setEventRequestId(eventRequestId);
                String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

                // 对物料批进行更新
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotVO2.setStocktakeFlag("N");
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotService.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
            }

            // 获取消耗数量
            MtMaterialVO consumeMaterialVO = mtMaterialService.materialPropertyGet(tenantId, t.getMaterialId());
            MtUomVO1 comsumeTransferUomVO = new MtUomVO1();
            comsumeTransferUomVO.setSourceUomId(mtMaterialLot.getPrimaryUomId());
            comsumeTransferUomVO.setSourceValue(mtMaterialLot.getPrimaryUomQty());
            if (consumeMaterialVO != null) {
                comsumeTransferUomVO.setTargetUomId(consumeMaterialVO.getPrimaryUomId());
                mtStocktakeActualVO5.setMaterialUomId(consumeMaterialVO.getPrimaryUomId());
            }
            comsumeTransferUomVO = mtUomService.uomConversion(tenantId, comsumeTransferUomVO);
            if (comsumeTransferUomVO != null) {
                mtStocktakeActualVO5.setMaterialQty(comsumeTransferUomVO.getTargetValue());
            }

            if (!adjustFlag) {
                // 获取调整数量
                MtMaterialVO adjustMaterialVO = mtMaterialService.materialPropertyGet(tenantId,
                                mtStocktakeActualVO3.getAdjustMaterialid());
                MtUomVO1 adjustTransferUomVO = new MtUomVO1();
                adjustTransferUomVO.setSourceUomId(mtStocktakeActualVO3.getAdjustUomId());
                adjustTransferUomVO.setSourceValue(mtStocktakeActualVO3.getAdjustQuantity());
                if (adjustMaterialVO != null) {
                    adjustTransferUomVO.setTargetUomId(adjustMaterialVO.getPrimaryUomId());
                    mtStocktakeActualVO5.setAdjustMaterialUomId(adjustMaterialVO.getPrimaryUomId());
                }
                adjustTransferUomVO = mtUomService.uomConversion(tenantId, adjustTransferUomVO);
                if (adjustTransferUomVO != null) {
                    mtStocktakeActualVO5.setAdjustTargetValue(adjustTransferUomVO.getTargetValue());
                }

                if (StringUtils.isEmpty(mtMaterialLot.getSecondaryUomId())
                                && StringUtils.isNotEmpty(adjustMaterialVO.getSecondaryUomId())) {
                    mtStocktakeActualVO5.setAdjustMaterialSecondaryUomId(adjustMaterialVO.getSecondaryUomId());
                }
            } else {
                mtStocktakeActualVO5.setAdjustMaterialUomId(null);
                mtStocktakeActualVO5.setAdjustTargetValue(0.0D);
            }


            mtStocktakeActualVO5.setActualVO3(mtStocktakeActualVO3);
            mtStocktakeActualVO5.setActual(t);
            mtStocktakeActualVO5.setMtMaterialLot(mtMaterialLot);
            mtStocktakeActualVO5.setAdjustFlag(adjustFlag);
            actualVO5List.add(mtStocktakeActualVO5);
        }

        // 7.
        // 第七步，根据第二、三步中获取到实绩的物料批信息，获取第五步enableFlag=“Y”的物料批信息，先按materialId、siteId、locatorId、ownerType、ownerId一致的物料批进行分类
        Map<String, Double> collect = actualVO5List.stream()
                        .filter(t -> "Y".equals(StringUtils.isEmpty(t.getMtMaterialLot().getEnableFlag()) ? ""
                                        : t.getMtMaterialLot().getEnableFlag()))
                        .collect(Collectors.groupingBy(
                                        t -> t.getActual().getMaterialId() + t.getActual().getSiteId()
                                                        + t.getActual().getLocatorId() + t.getActual().getOwnerType()
                                                        + t.getActual().getOwnerId(),
                                        Collectors.reducing(0.0D, (MtStocktakeActualVO5 p) -> p.getMaterialQty(),
                                                        Double::sum)));

        Map<String, List<MtStocktakeActualVO5>> collect1 = actualVO5List.stream()
                        .filter(t -> "Y".equals(StringUtils.isEmpty(t.getMtMaterialLot().getEnableFlag()) ? ""
                                        : t.getMtMaterialLot().getEnableFlag()))
                        .collect(Collectors.groupingBy(t -> t.getActual().getMaterialId() + t.getActual().getSiteId()
                                        + t.getActual().getLocatorId() + t.getActual().getOwnerType()
                                        + t.getActual().getOwnerId()));

        // 7.根据第二、三步中获取到实绩的物料批信息，先按materialId、siteId、locatorId一致的物料批进行分类，依次进行处理

        // 校验miscellaneousIssueBusinessId
        if (!collect1.isEmpty() && StringUtils.isEmpty(dto.getMiscellaneousIssueCostCenterId())) {
            throw new MtException("MT_STOCKTAKE_0001",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001", "STOCKTAKE",
                                            "miscellaneousIssueCostCenterId",
                                            "【API:stocktakeActualDifferenceBatchAdjust】"));
        }

        for (Map.Entry<String, List<MtStocktakeActualVO5>> t : collect1.entrySet()) {
            List<MtInstructionVO3.MaterialLotList> temp = new ArrayList<>();
            // 指令创建
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            mtInstructionVO.setInstructionType("SHIP_TO_MISCELLANEOUS");
            mtInstructionVO.setSiteId(t.getValue().get(0).getActual().getSiteId());
            mtInstructionVO.setMaterialId(t.getValue().get(0).getActual().getMaterialId());
            mtInstructionVO.setUomId(t.getValue().get(0).getMaterialUomId());
            mtInstructionVO.setFromSiteId(t.getValue().get(0).getActual().getSiteId());
            mtInstructionVO.setFromLocatorId(t.getValue().get(0).getActual().getLocatorId());
            mtInstructionVO.setCostCenterId(dto.getMiscellaneousIssueCostCenterId());
            mtInstructionVO.setQuantity(collect.get(t.getKey()));
            mtInstructionVO.setOrderType("STOCKTAKE");
            mtInstructionVO.setOrderId(dto.getStocktakeId());
            mtInstructionVO.setBusinessType(dto.getMiscellaneousIssueBusinessType());
            mtInstructionVO.setRemark(dto.getMiscellaneousIssueRemark());
            mtInstructionVO.setFromOwnerType(t.getValue().get(0).getActual().getOwnerType());
            mtInstructionVO.setToOwnerType(t.getValue().get(0).getActual().getOwnerType());

            mtInstructionVO.setNumIncomingValueList(dto.getIssueNumIncomingValueList());

            mtInstructionVO.setEventRequestId(eventRequestId);
            String instructionId = mtLogisticInstructionService.instructionUpdate(tenantId, mtInstructionVO, "N")
                            .getInstructionId();

            // 指令下达
            mtLogisticInstructionService.instructionRelease(tenantId, instructionId, eventRequestId);

            for (MtStocktakeActualVO5 tt : t.getValue()) {
                MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();
                materialLotList.setMaterialLotId(tt.getActual().getMaterialLotId());
                materialLotList.setQty(tt.getActual().getCurrentQuantity());
                materialLotList.setContainerId(tt.getActual().getContainerId());
                //materialLotList.setLotCode(tt.getActual().getLotCode());
                materialLotList.setToLocatorId(
                        null == tt.getActual().getLocatorId() ? "" : tt.getActual().getLocatorId());
                materialLotList.setUomId(tt.getActual().getUomId());
                temp.add(materialLotList);

                // 对物料批进行消耗
                MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                mtMaterialLotVO1.setMaterialLotId(tt.getActual().getMaterialLotId());
                mtMaterialLotVO1.setPrimaryUomId(tt.getActual().getUomId());
                mtMaterialLotVO1.setTrxPrimaryUomQty(tt.getActual().getCurrentQuantity());
                mtMaterialLotVO1.setEventRequestId(eventRequestId);

                MtMaterialVO mtMaterialVO =
                                mtMaterialService.materialPropertyGet(tenantId, tt.getMtMaterialLot().getMaterialId());
                if (null != mtMaterialVO && StringUtils.isNotEmpty(mtMaterialVO.getSecondaryUomId())) {
                    mtMaterialLotVO1.setSecondaryUomId(mtMaterialVO.getSecondaryUomId());
                    mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
                }
                mtMaterialLotService.materialLotConsume(tenantId, mtMaterialLotVO1);
            }

            // 执行指令
            MtInstructionVO3 logisticInstructionVO3 = new MtInstructionVO3();
            logisticInstructionVO3.setInstructionId(instructionId);
            logisticInstructionVO3.setMaterialLotMessageList(temp);
            logisticInstructionVO3.setEventRequestId(eventRequestId);
            mtLogisticInstructionService.instructionExecute(tenantId, logisticInstructionVO3);

            // 更新完成状态
            mtLogisticInstructionService.instructionComplete(tenantId, instructionId, eventRequestId);
        }

        // 8.数据调整
        Long adjustCount = actualVO5List.stream().filter(t -> !t.getAdjustFlag()).count();
        if (0 < adjustCount) {
            /**
             * 指令创建/下达/执行/完成
             */
            // 根据第二、三步中获取到实绩的物料批信息，先按adjustMaterialId、siteId、adjustLocatorId、adjustOwnerType、adjustOwnerId一致的物料批进行分类，依次进行处理
            Map<String, Double> adjustCollect = actualVO5List.stream().filter(t -> !t.getAdjustFlag())
                            .collect(Collectors.groupingBy(
                                            t -> t.getActualVO3().getAdjustMaterialid() + t.getActual().getSiteId()
                                                            + t.getActualVO3().getAdjustLocatorid()
                                                            + t.getActualVO3().getAdjustOwnerType()
                                                            + t.getActualVO3().getAdjustOwnerId(),
                                            Collectors.reducing(0.0D,
                                                            (MtStocktakeActualVO5 p) -> p.getAdjustTargetValue(),
                                                            Double::sum)));

            Map<String, List<MtStocktakeActualVO5>> adjustCollect1 = actualVO5List.stream()
                            .filter(t -> !t.getAdjustFlag())
                            .collect(Collectors.groupingBy(t -> t.getActualVO3().getAdjustMaterialid()
                                            + t.getActual().getSiteId() + t.getActualVO3().getAdjustLocatorid()
                                            + t.getActualVO3().getAdjustOwnerType()
                                            + t.getActualVO3().getAdjustOwnerId()));

            // 校验miscellaneousReceiptCostCenterId
            if (StringUtils.isEmpty(dto.getMiscellaneousReceiptCostCenterId())) {
                throw new MtException("MT_STOCKTAKE_0001",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001",
                                                "STOCKTAKE", "miscellaneousReceiptCostCenterId",
                                                "【API:stocktakeActualDifferenceBatchAdjust】"));
            }

            for (Map.Entry<String, List<MtStocktakeActualVO5>> t : adjustCollect1.entrySet()) {
                List<MtInstructionVO3.MaterialLotList> temp = new ArrayList<>();
                // 指令创建
                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                mtInstructionVO.setInstructionType("RECEIVE_FROM_COSTCENTER");
                mtInstructionVO.setSiteId(t.getValue().get(0).getActual().getSiteId());
                mtInstructionVO.setMaterialId(t.getValue().get(0).getActualVO3().getAdjustMaterialid());
                mtInstructionVO.setUomId(t.getValue().get(0).getAdjustMaterialUomId());
                mtInstructionVO.setToSiteId(t.getValue().get(0).getActual().getSiteId());
                mtInstructionVO.setToLocatorId(t.getValue().get(0).getActualVO3().getAdjustLocatorid());
                mtInstructionVO.setCostCenterId(dto.getMiscellaneousReceiptCostCenterId());
                mtInstructionVO.setQuantity(adjustCollect.get(t.getKey()));
                mtInstructionVO.setOrderType("STOCKTAKE");
                mtInstructionVO.setOrderId(dto.getStocktakeId());
                mtInstructionVO.setBusinessType(dto.getMiscellaneousReceiptBusinessType());
                mtInstructionVO.setRemark(dto.getMiscellaneousReceiptRemark());
                mtInstructionVO.setFromOwnerType(t.getValue().get(0).getActualVO3().getAdjustOwnerType());
                mtInstructionVO.setToOwnerType(t.getValue().get(0).getActualVO3().getAdjustOwnerType());

                mtInstructionVO.setNumIncomingValueList(dto.getReceiptNumIncomingValueList());

                mtInstructionVO.setEventRequestId(eventRequestId);

                String instructionId = mtLogisticInstructionService.instructionUpdate(tenantId, mtInstructionVO, "N")
                                .getInstructionId();

                // 指令下达
                mtLogisticInstructionService.instructionRelease(tenantId, instructionId, eventRequestId);

                for (MtStocktakeActualVO5 tt : t.getValue()) {
                    MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();
                    materialLotList.setMaterialLotId(tt.getActual().getMaterialLotId());
                    materialLotList.setQty(tt.getActualVO3().getAdjustQuantity());
                    materialLotList.setContainerId(tt.getActualVO3().getAdjustContainerid());
                    //materialLotList.setLotCode(tt.getActual().getLotCode());
                    materialLotList.setToLocatorId(
                            null == tt.getActual().getLocatorId() ? "" : tt.getActual().getLocatorId());
                    materialLotList.setUomId(tt.getActualVO3().getAdjustUomId());
                    temp.add(materialLotList);
                }

                // 执行指令
                MtInstructionVO3 logisticInstructionVO3 = new MtInstructionVO3();
                logisticInstructionVO3.setInstructionId(instructionId);
                logisticInstructionVO3.setMaterialLotMessageList(temp);
                logisticInstructionVO3.setEventRequestId(eventRequestId);

                MtInstructionVO4 mtInstructionVO4 =
                                mtLogisticInstructionService.instructionExecute(tenantId, logisticInstructionVO3);

                // 更新完成状态
                mtLogisticInstructionService.instructionComplete(tenantId, instructionId, eventRequestId);

                // 对物料批进行更新
                for (MtStocktakeActualVO5 tt : t.getValue()) {
                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotId(tt.getActual().getMaterialLotId());
                    mtMaterialLotVO2.setEnableFlag("Y");
                    mtMaterialLotVO2.setMaterialId(tt.getActualVO3().getAdjustMaterialid());
                    mtMaterialLotVO2.setPrimaryUomId(tt.getActualVO3().getAdjustUomId());
                    mtMaterialLotVO2.setPrimaryUomQty(tt.getActualVO3().getAdjustQuantity());
                    mtMaterialLotVO2.setLocatorId(tt.getActualVO3().getAdjustLocatorid());
                    mtMaterialLotVO2.setOwnerType(tt.getActualVO3().getAdjustOwnerType());
                    mtMaterialLotVO2.setOwnerId(tt.getActualVO3().getAdjustOwnerId());
                    mtMaterialLotVO2.setReservedObjectType(tt.getActualVO3().getAdjustReservedObjectType());
                    mtMaterialLotVO2.setReservedObjectId(tt.getActualVO3().getAdjustReservedObjectId());
                    if (StringUtils.isNotEmpty(mtMaterialLotVO2.getReservedObjectId())) {
                        mtMaterialLotVO2.setReservedFlag("Y");
                    } else {
                        mtMaterialLotVO2.setReservedFlag("N");
                    }
                    if (mtInstructionVO4 != null) {
                        mtMaterialLotVO2.setEventId(mtInstructionVO4.getEventId());
                    }
                    if (StringUtils.isNotEmpty(tt.getAdjustMaterialSecondaryUomId())) {
                        mtMaterialLotVO2.setSecondaryUomId(tt.getAdjustMaterialSecondaryUomId());
                        mtMaterialLotVO2.setSecondaryUomQty(0.0D);
                    }
                    mtMaterialLotService.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

                    // 如果adjustContainerId不为空，则进行容器装载
                    if (StringUtils.isNotEmpty(tt.getActualVO3().getAdjustContainerid())) {
                        MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
                        mtContainerVO24.setContainerId(tt.getActualVO3().getAdjustContainerid());
                        mtContainerVO24.setLocationColumn(tt.getActualVO3().getAdjustcountLocationColumn());
                        mtContainerVO24.setLocationRow(tt.getActualVO3().getAdjustcountLocationRow());
                        mtContainerVO24.setLoadObjectType("MATERIAL_LOT");
                        mtContainerVO24.setLoadObjectId(tt.getActual().getMaterialLotId());
                        mtContainerVO24.setEventRequestId(eventRequestId);
                        mtContainerService.containerLoad(tenantId, mtContainerVO24);
                    }
                }
            }


            /**
             * 库存现有量更新
             */
            Map<String, Double> handCollect = actualVO5List.stream().filter(t -> !t.getAdjustFlag())
                            .collect(Collectors.groupingBy(t -> t.getActualVO3().getAdjustMaterialid()
                                            + t.getActual().getSiteId() + t.getActualVO3().getAdjustLocatorid()
                                            + t.getActualVO3().getAdjustOwnerType()
                                            + t.getActualVO3().getAdjustOwnerId() + t.getActual().getLotCode(),
                                            Collectors.reducing(0.0D,
                                                            (MtStocktakeActualVO5 p) -> p.getAdjustTargetValue(),
                                                            Double::sum)));

            Map<String, List<MtStocktakeActualVO5>> handCollect1 = actualVO5List.stream()
                            .filter(t -> !t.getAdjustFlag())
                            .collect(Collectors.groupingBy(t -> t.getActualVO3().getAdjustMaterialid()
                                            + t.getActual().getSiteId() + t.getActualVO3().getAdjustLocatorid()
                                            + t.getActualVO3().getAdjustOwnerType()
                                            + t.getActualVO3().getAdjustOwnerId() + t.getActual().getLotCode()));

            for (Map.Entry<String, List<MtStocktakeActualVO5>> t : handCollect1.entrySet()) {
                List<MtStocktakeActualVO5> stocktakeActualGroup = t.getValue();
                MtStocktakeActualVO5 tempMtStocktakeActualVO5 = stocktakeActualGroup.get(0);

                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("STOCKTAKE_ONHAND_UPDATE");
                eventCreateVO.setEventRequestId(eventRequestId);
                String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

                MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
                updateOnhandVO.setSiteId(tempMtStocktakeActualVO5.getActual().getSiteId());
                updateOnhandVO.setMaterialId(tempMtStocktakeActualVO5.getActualVO3().getAdjustMaterialid());
                updateOnhandVO.setLotCode(tempMtStocktakeActualVO5.getActual().getLotCode());
                updateOnhandVO.setLocatorId(tempMtStocktakeActualVO5.getActualVO3().getAdjustLocatorid());
                updateOnhandVO.setChangeQuantity(handCollect.get(t.getKey()));
                updateOnhandVO.setOwnerId(tempMtStocktakeActualVO5.getActualVO3().getAdjustOwnerId());
                updateOnhandVO.setOwnerType(tempMtStocktakeActualVO5.getActualVO3().getAdjustOwnerType());
                updateOnhandVO.setEventId(eventId);
                mtInvOnhandQuantityService.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
            }

            /**
             * 预留库存现有量更新
             */
            Map<String, Double> reservedCollect = actualVO5List.stream().filter(t -> !t.getAdjustFlag())
                            .collect(Collectors.groupingBy(t -> t.getActualVO3().getAdjustMaterialid()
                                            + t.getActual().getSiteId() + t.getActualVO3().getAdjustLocatorid()
                                            + t.getActualVO3().getAdjustOwnerType()
                                            + t.getActualVO3().getAdjustOwnerId() + t.getActual().getLotCode()
                                            + t.getActualVO3().getAdjustReservedObjectType()
                                            + t.getActualVO3().getAdjustReservedObjectId(),
                                            Collectors.reducing(0.0D,
                                                            (MtStocktakeActualVO5 p) -> p.getAdjustTargetValue(),
                                                            Double::sum)));

            Map<String, List<MtStocktakeActualVO5>> reservedCollect1 = actualVO5List.stream()
                            .filter(t -> !t.getAdjustFlag())
                            .collect(Collectors.groupingBy(t -> t.getActualVO3().getAdjustMaterialid()
                                            + t.getActual().getSiteId() + t.getActualVO3().getAdjustLocatorid()
                                            + t.getActualVO3().getAdjustOwnerType()
                                            + t.getActualVO3().getAdjustOwnerId() + t.getActual().getLotCode()
                                            + t.getActualVO3().getAdjustReservedObjectType()
                                            + t.getActualVO3().getAdjustReservedObjectId()));

            for (Map.Entry<String, List<MtStocktakeActualVO5>> t : reservedCollect1.entrySet()) {
                List<MtStocktakeActualVO5> stocktakeActualGroup = t.getValue();
                MtStocktakeActualVO5 tempMtStocktakeActualVO5 = stocktakeActualGroup.get(0);

                if (StringUtils.isNotEmpty(tempMtStocktakeActualVO5.getActualVO3().getAdjustReservedObjectId())) {
                    MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                    eventCreateVO.setEventTypeCode("STOCKTAKE_RESERVED_ONHAND_UPDATE");
                    eventCreateVO.setEventRequestId(eventRequestId);
                    String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

                    MtInvOnhandHoldVO8 onhandReleaseVO2 = new MtInvOnhandHoldVO8();
                    onhandReleaseVO2.setSiteId(tempMtStocktakeActualVO5.getActual().getSiteId());
                    onhandReleaseVO2.setMaterialId(tempMtStocktakeActualVO5.getActualVO3().getAdjustMaterialid());
                    onhandReleaseVO2.setLocatorId(tempMtStocktakeActualVO5.getActualVO3().getAdjustLocatorid());
                    onhandReleaseVO2.setLotCode(tempMtStocktakeActualVO5.getActual().getLotCode());
                    onhandReleaseVO2.setOwnerType(tempMtStocktakeActualVO5.getActualVO3().getAdjustOwnerType());
                    onhandReleaseVO2.setOwnerId(tempMtStocktakeActualVO5.getActualVO3().getAdjustOwnerId());
                    onhandReleaseVO2.setHoldType("ORDER");
                    onhandReleaseVO2.setOrderType(
                                    tempMtStocktakeActualVO5.getActualVO3().getAdjustReservedObjectType());
                    onhandReleaseVO2.setOrderId(tempMtStocktakeActualVO5.getActualVO3().getAdjustReservedObjectId());
                    onhandReleaseVO2.setEventId(eventId);
                    onhandReleaseVO2.setHoldQuantity(reservedCollect.get(t.getKey()));
                    mtInvOnhandHoldService.onhandReserveCreateProcess(tenantId, onhandReleaseVO2);
                }
            }

        }

        // 9.对于每个materialLotId，更新调整标识
        for (String s : dto.getMaterialLotIdList()) {

            // 2019-07-25 add by zijin.liang
            if ("Y".equals(materialLotLockFlag)) {
                // 创建事件
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("STOCKTAKE_MATERIALLOT_LOCK");
                eventCreateVO.setEventRequestId(eventRequestId);
                String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

                // 对物料批进行更新
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(s);
                mtMaterialLotVO2.setStocktakeFlag("Y");
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotService.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
            }

            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCKTAKE_ADJUST");
            eventCreateVO.setEventRequestId(eventRequestId);
            String eventId = mtEventService.eventCreate(tenantId, eventCreateVO);

            MtStocktakeActualVO mtStocktakeActualVO = new MtStocktakeActualVO();
            mtStocktakeActualVO.setStocktakeId(dto.getStocktakeId());
            mtStocktakeActualVO.setMaterialLotId(s);
            mtStocktakeActualVO.setAdjustFlag("Y");
            mtStocktakeActualVO.setEventId(eventId);
            stocktakeActualUpdate(tenantId, mtStocktakeActualVO);
        }
    }
}
