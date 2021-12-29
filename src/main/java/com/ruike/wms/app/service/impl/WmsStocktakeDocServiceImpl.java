package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsStocktakeActualService;
import com.ruike.wms.app.service.WmsStocktakeDocService;
import com.ruike.wms.domain.repository.WmsDocPrivilegeRepository;
import com.ruike.wms.domain.repository.WmsStocktakeDocRepository;
import com.ruike.wms.domain.repository.WmsStocktakeRangeRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotVO;
import com.ruike.wms.domain.vo.WmsStocktakeDocSelectVO;
import com.ruike.wms.domain.vo.WmsStocktakeDocVO;
import com.ruike.wms.domain.vo.WmsStocktakeMaterialDetailVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.repository.MtStocktakeActualRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.stocktake.domain.entity.MtStocktakeDoc;
import tarzan.stocktake.domain.repository.MtStocktakeDocRepository;
import tarzan.stocktake.domain.vo.MtStocktakeDocVO1;
import tarzan.stocktake.domain.vo.MtStocktakeDocVO2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.NO;
import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.YES;
import static com.ruike.wms.infra.constant.WmsConstant.DocType.STOCKTAKE_DOC;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.STOCKTAKE_MATERIALLOT_LOCK;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.STOCKTAKE_MATERIALLOT_UNLOCK;
import static com.ruike.wms.infra.constant.WmsConstant.OperationType.CREATE;

/**
 * 库存盘点单据 服务实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 12:24
 */
@Service
@Slf4j
public class WmsStocktakeDocServiceImpl implements WmsStocktakeDocService {

    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtEventRepository mtEventRepository;
    private final MtStocktakeDocRepository mtStocktakeDocRepository;
    private final MtStocktakeActualRepository mtStocktakeActualRepository;
    private final WmsStocktakeDocRepository wmsStocktakeDocRepository;
    private final WmsStocktakeRangeRepository wmsStocktakeRangeRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final WmsStocktakeActualService wmsStocktakeActualService;
    private final WmsDocPrivilegeRepository docPrivilegeRepository;

    @Autowired
    public WmsStocktakeDocServiceImpl(WmsStocktakeDocRepository wmsStocktakeDocRepository, MtEventRequestRepository mtEventRequestRepository, MtEventRepository mtEventRepository, MtStocktakeDocRepository mtStocktakeDocRepository, WmsStocktakeRangeRepository wmsStocktakeRangeRepository, MtStocktakeActualRepository mtStocktakeActualRepository, MtErrorMessageRepository mtErrorMessageRepository, MtMaterialLotRepository mtMaterialLotRepository, WmsStocktakeActualService wmsStocktakeActualService, WmsDocPrivilegeRepository docPrivilegeRepository) {
        this.wmsStocktakeDocRepository = wmsStocktakeDocRepository;
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.mtEventRepository = mtEventRepository;
        this.mtStocktakeDocRepository = mtStocktakeDocRepository;
        this.wmsStocktakeRangeRepository = wmsStocktakeRangeRepository;
        this.mtStocktakeActualRepository = mtStocktakeActualRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.wmsStocktakeActualService = wmsStocktakeActualService;
        this.docPrivilegeRepository = docPrivilegeRepository;
    }

    private final static String LOCK = "LOCK";
    private final static String UNLOCK = "UNLOCK";

    @Override
    public Page<WmsStocktakeDocVO> pageAndSort(Long tenantId, WmsStocktakeDocQueryDTO dto, PageRequest pageRequest) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        dto.setTenantId(tenantId);
        dto.setUserId(userDetails.getUserId());
        return wmsStocktakeDocRepository.pageAndSort(tenantId, dto, pageRequest);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public WmsStocktakeDocVO create(Long tenantId, MtStocktakeDocVO1 vo) {
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EventType.STOCKTAKE_DOC_UPDATE);

        // 创建盘点单
        vo.setEventRequestId(eventRequestId);
        vo.setAdjustTimelyFlag(WmsConstant.CONSTANT_N);
        // 如果未勾选物料，则将仓库下所有的物料纳入盘点范围
        if (CollectionUtils.isEmpty(vo.getMaterialIdList())) {
            List<MtMaterial> list = wmsStocktakeDocRepository.selectStocktakeMaterial(tenantId, vo.getAreaLocatorId());
            vo.setMaterialIdList(list.stream().map(MtMaterial::getMaterialId).collect(Collectors.toList()));
        }
        // 如果未勾选货位，则将仓库下所有的货位纳入盘点范围
        if (CollectionUtils.isEmpty(vo.getLocatorIdList())) {
            List<MtModLocator> list = wmsStocktakeDocRepository.selectStocktakeLocator(tenantId, vo.getAreaLocatorId());
            vo.setLocatorIdList(list.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList()));
        }
        String stocktakeId = mtStocktakeDocRepository.stocktakeDocCreate(tenantId, vo);

        // 查询返回创建出的盘点单数据
        return wmsStocktakeDocRepository.selectById(tenantId, stocktakeId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public WmsStocktakeDocVO update(Long tenantId, WmsStocktakeDocUpdateDTO vo) {
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EventType.STOCKTAKE_DOC_UPDATE);

        // 创建事件
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode(WmsConstant.EventType.STOCKTAKE_DOC_UPDATE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);

        // 更新盘点单
        vo.setEventId(eventId);
        mtStocktakeDocRepository.stocktakeDocUpdate(tenantId, vo);

        // 返回更新结果
        return wmsStocktakeDocRepository.selectById(tenantId, vo.getStocktakeId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void release(Long tenantId, List<String> stocktakeIdList) {
        long startDate = System.currentTimeMillis();
        // 验证是否有下达的权限
        Long userId = DetailsHelper.getUserDetails().getUserId();
        List<MtStocktakeDoc> docList = mtStocktakeDocRepository.selectByCondition(Condition.builder(MtStocktakeDoc.class).andWhere(Sqls.custom().andIn(MtStocktakeDoc.FIELD_STOCKTAKE_ID, stocktakeIdList)).build());
        docList.forEach(rec -> docPrivilegeRepository.isWarehousePrivileged(tenantId, WmsWarehousePrivilegeQueryDTO.builder().userId(userId).docType(STOCKTAKE_DOC).operationType(CREATE).locatorId(rec.getAreaLocatorId()).build()));
        long endDate = System.currentTimeMillis();
        log.info("===========>盘点单" + stocktakeIdList.get(0) + "验证是否有下达的权限耗时："+(endDate - startDate) + "毫秒");

        // 验证状态必须为NEW
        List<String> numList = wmsStocktakeDocRepository.notNewStocktakeNums(tenantId, stocktakeIdList);
        WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(numList),
                "WMS_STOCKTAKE_0001", "WMS", StringUtils.join(numList, ","));
        long endDate2 = System.currentTimeMillis();
        log.info("===========>盘点单" + stocktakeIdList.get(0) + "验证状态必须为NEW耗时："+(endDate2 - endDate) + "毫秒");

        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EventType.STOCKTAKE_DOC_UPDATE);
        long endDate3 = System.currentTimeMillis();
        log.info("===========>盘点单" + stocktakeIdList.get(0) + "创建事件请求耗时："+(endDate3 - endDate2) + "毫秒");

        // 循环执行下达操作并创建盘点实绩
        for (String stocktakeId : stocktakeIdList) {
            long startDate4 = System.currentTimeMillis();
            // 执行下达
            mtStocktakeDocRepository.stocktakeDocRelease(tenantId, stocktakeId, eventRequestId);
            long endDate4 = System.currentTimeMillis();
            log.info("===========>盘点单" + stocktakeId + "执行下达耗时："+(endDate4 - startDate4) + "毫秒");
            // 获取盘点范围内的条码
            List<WmsMaterialLotVO> materialLotList = wmsStocktakeRangeRepository.selectMaterialLotInRange(tenantId, stocktakeId);
            List<String> materialLotIdList = materialLotList.stream().map(WmsMaterialLotVO::getMaterialLotId).collect(Collectors.toList());
            long endDate5 = System.currentTimeMillis();
            log.info("===========>盘点单" + stocktakeId + "获取盘点范围内的"+ materialLotIdList.size()+ "个条码耗时："+(endDate5 - endDate4) + "毫秒");
            // 对盘点范围内的条码进行停用
            this.stocktakeMaterialLotLock(tenantId, stocktakeId, materialLotIdList, eventRequestId);
            long endDate6 = System.currentTimeMillis();
            log.info("===========>盘点单" + stocktakeId + "对盘点范围内的"+ materialLotIdList.size()+ "个条码进行停用耗时："+(endDate6 - endDate5) + "毫秒");
            // 批量创建盘点实绩
            wmsStocktakeActualService.batchInsert(tenantId, stocktakeId, eventRequestId, materialLotIdList);
            long endDate7 = System.currentTimeMillis();
            log.info("===========>盘点单" + stocktakeId + "批量创建"+ materialLotIdList.size()+ "个盘点实绩耗时："+(endDate7 - endDate6) + "毫秒");
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void complete(Long tenantId, List<String> stocktakeIdList) {
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EventType.STOCKTAKE_DOC_UPDATE);

        // 循环执行完成操作
        stocktakeIdList.forEach(stocktakeId -> {
            // 获取盘点实际下的条码
            MtStocktakeActual actualQuery = new MtStocktakeActual();
            actualQuery.setTenantId(tenantId);
            actualQuery.setStocktakeId(stocktakeId);
            List<MtStocktakeActual> actualList = mtStocktakeActualRepository.select(actualQuery);
            List<String> materialLotIdList = actualList.stream().map(MtStocktakeActual::getMaterialLotId).collect(Collectors.toList());
            // 将盘点范围内条码解冻
            this.stocktakeMaterialLotUnlock(tenantId, stocktakeId, materialLotIdList, eventRequestId);
            // 执行完成
            mtStocktakeDocRepository.stocktakeDocComplete(tenantId, stocktakeId, eventRequestId);
        });
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void close(Long tenantId, List<String> stocktakeIdList) {
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EventType.STOCKTAKE_DOC_UPDATE);
        // 查询不为New的单据
        List<String> idList = wmsStocktakeDocRepository.notNewStocktakeIds(tenantId, stocktakeIdList);

        // 循环执行关闭操作
        stocktakeIdList.forEach(stocktakeId -> {
            // 若状态不为NEW，须先将盘点范围内条码解冻
            if (idList.contains(stocktakeId)) {
                // 获取盘点实际下的条码
                MtStocktakeActual actualQuery = new MtStocktakeActual();
                actualQuery.setTenantId(tenantId);
                actualQuery.setStocktakeId(stocktakeId);
                List<MtStocktakeActual> actualList = mtStocktakeActualRepository.select(actualQuery);
                List<String> materialLotIdList = actualList.stream().map(MtStocktakeActual::getMaterialLotId).collect(Collectors.toList());
                // 将盘点范围内条码解冻
                this.stocktakeMaterialLotUnlock(tenantId, stocktakeId, materialLotIdList, eventRequestId);
            }
            // 执行关闭
            mtStocktakeDocRepository.stocktakeDocClose(tenantId, stocktakeId, eventRequestId);
        });
    }

    @Override
    @ProcessLovValue
    public WmsStocktakeDocSelectVO docScan(Long tenantId, String stocktakeNum) {
        return wmsStocktakeDocRepository.selectImplDocByNum(tenantId, stocktakeNum);
    }

    @Override
    public Page<WmsStocktakeMaterialDetailVO> pageMaterialVersion(Long tenantId, WmsStocktakeMaterialDetailQueryDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> wmsStocktakeDocRepository.selectMaterialDetailByIds(tenantId, dto));
    }

    @Override
    @ProcessLovValue
    public List<WmsStocktakeDetailExportDTO> exportDetail(Long tenantId, ExportParam exportParam, WmsStocktakeMaterialDetailQueryDTO condition) {
        List<WmsStocktakeMaterialDetailVO> list = wmsStocktakeDocRepository.selectMaterialDetailByIds(tenantId, condition);
        List<WmsStocktakeDetailExportDTO> result = new ArrayList<>(list.size());
        BeanCopier copier = BeanCopier.create(WmsStocktakeMaterialDetailVO.class, WmsStocktakeDetailExportDTO.class, false);
        list.forEach(src -> {
            WmsStocktakeDetailExportDTO dto = new WmsStocktakeDetailExportDTO();
            copier.copy(src, dto, null);
            result.add(dto);
        });
        return result;
    }

    /**
     * 条码锁定转换
     *
     * @param tenantId          租户
     * @param stocktakeId       盘点单ID
     * @param materialLotIdList 物料批ID列表
     * @param eventRequestId    事件请求
     * @param switchMode        转化模式
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 11:27:32
     */
    private void stocktakeMaterialLotSwitch(Long tenantId, String stocktakeId, List<String> materialLotIdList, String eventRequestId, String switchMode) {
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeLimitMaterialLotLock】"));
        }

        MtStocktakeDoc mtStocktakeDoc = mtStocktakeDocRepository.stocktakeDocPropertyGet(tenantId, stocktakeId);
        if (Objects.isNull(mtStocktakeDoc)) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_STOCKTAKE_0009", "STOCKTAKE", "stocktakeId", "【API：stocktakeLimitMaterialLotLock】"));
        }

        if ((LOCK.equals(switchMode) ? YES : NO).equals(mtStocktakeDoc.getMaterialLotLockFlag())) {
            throw new MtException("MT_STOCKTAKE_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_STOCKTAKE_0028", "STOCKTAKE", "【API：stocktakeLimitMaterialLotLock】"));
        }

        if (org.apache.commons.collections4.CollectionUtils.isEmpty(materialLotIdList)) {
            throw new MtException("MT_STOCKTAKE_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_STOCKTAKE_0029", "STOCKTAKE", "【API：stocktakeLimitMaterialLotLock】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(LOCK.equals(switchMode) ? STOCKTAKE_MATERIALLOT_LOCK : STOCKTAKE_MATERIALLOT_UNLOCK);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtStocktakeDocVO2 updateVO = new MtStocktakeDocVO2();
        updateVO.setStocktakeId(mtStocktakeDoc.getStocktakeId());
        updateVO.setEventId(eventId);
        updateVO.setMaterialLotLockFlag(LOCK.equals(switchMode) ? YES : NO);
        mtStocktakeDocRepository.stocktakeDocUpdate(tenantId, updateVO);

        List<MtMaterialLotVO20> updateList = new ArrayList<>();
        for (String materialLotId : materialLotIdList) {
            MtMaterialLotVO20 updateMaterialLotVO = new MtMaterialLotVO20();
            updateMaterialLotVO.setMaterialLotId(materialLotId);
            updateMaterialLotVO.setStocktakeFlag((LOCK.equals(switchMode) ? YES : NO));
            updateList.add(updateMaterialLotVO);
        }
        mtMaterialLotRepository.materialLotBatchUpdatePrecompile(tenantId, updateList, eventId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stocktakeMaterialLotLock(Long tenantId, String stocktakeId, List<String> materialLotIdList, String eventRequestId) {
        stocktakeMaterialLotSwitch(tenantId, stocktakeId, materialLotIdList, eventRequestId, LOCK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stocktakeMaterialLotUnlock(Long tenantId, String stocktakeId, List<String> materialLotIdList, String eventRequestId) {
        stocktakeMaterialLotSwitch(tenantId, stocktakeId, materialLotIdList, eventRequestId, UNLOCK);
    }
}
