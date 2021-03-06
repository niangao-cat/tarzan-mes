package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeNcDisposePlatformDTO11;
import com.ruike.hme.app.service.HmeEoJobSnBatchOutSiteService;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.domain.entity.HmeEoJobContainer;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnLotMaterialMapper;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtAssembleProcessActualRepository;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO11;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO16;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO17;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterDoneStep;
import tarzan.method.domain.repository.MtRouterDoneStepRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * @Classname HmeEoJobSnBatchOutSiteServiceImpl
 * @Description ??????????????????-SN??????????????????
 * @Date 2020/11/17 16:29
 * @Author yuchao.wang
 */
@Slf4j
@Service
public class HmeEoJobSnBatchOutSiteServiceImpl implements HmeEoJobSnBatchOutSiteService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobContainerRepository hmeEoJobContainerRepository;

    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;

    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private HmeProcessNcHeaderRepository hmeProcessNcHeaderRepository;

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 16:49
     * @param tenantId ??????ID
     * @param dto ??????
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSn> outSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteScan.Start tenantId={},dto={}", tenantId, dto);
        //????????????
        paramsVerificationForOutSite(tenantId, dto);

        //????????????eoId jobId
        List<String> eoIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
        List<String> jobIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct().collect(Collectors.toList());

        //???????????????????????????
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO16 hmeEoJobSnVO16 = queryBasicData(tenantId, dto, eoIdList, jobIdList);
        log.info("=================================>????????????????????????-???????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");

        //????????????,??????????????????????????????????????????????????????
        startDate = System.currentTimeMillis();
        HmeEoJobSn validateResult = outSiteValidate(tenantId, jobIdList, hmeEoJobSnVO16, dto);
        if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
            return Collections.singletonList(validateResult);
        }
        log.info("=================================>????????????????????????-???????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //???????????????BOM ture????????????????????????????????????????????????????????? add by yuchao.wang for peng.zhao at 2021.1.29
        if (!hmeEoJobSnVO16.getOperationBomFlag()) {
            //???????????????,???????????????????????????
            self().batchBackFlushMaterialOutSite(tenantId, hmeEoJobSnVO16, dto);
        }

        //????????????????????????
        startDate = System.currentTimeMillis();
        hmeEoJobSnCommonService.siteOutBatchComplete(tenantId, dto.getWorkcellId(), eoIdList, hmeEoJobSnVO16.getEoMap());
        log.info("=================================>????????????????????????-??????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //???????????????????????????
        startDate = System.currentTimeMillis();
        List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.batchMainOutSite(tenantId, dto, hmeEoJobSnVO16);
        log.info("=================================>????????????????????????-?????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //????????????????????????
        if (hmeEoJobSnVO16.getContainerOutSiteFlag()) {
            startDate = System.currentTimeMillis();
            hmeEoJobSnCommonService.batchContainerOutSite(tenantId, hmeEoJobSnVO16, dto);
            log.info("=================================>????????????????????????-??????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }

        //??????????????????
        startDate = System.currentTimeMillis();
        hmeEoJobSnRepository.batchOutSite2(tenantId, hmeEoJobSnVO16.getUserId(), jobIdList, dto.getRemark());
        log.info("=================================>????????????????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //hmeEoJobSnRepository.batchOutSiteWithMaterialLot(tenantId, hmeEoJobSnVO16.getUserId(), dto.getSnLineList());

        //??????????????????
        startDate = System.currentTimeMillis();
        hmeEoJobEquipmentRepository.batchSaveEquipmentRecordForOutSite(tenantId, dto);
        log.info("=================================>????????????????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //??????????????????
        startDate = System.currentTimeMillis();
        hmeEoJobSnCommonService.batchWkcCompleteOutputRecord(tenantId, dto, hmeEoJobSnVO16);
        log.info("=================================>????????????????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //??????????????????
        startDate = System.currentTimeMillis();
        hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
        log.info("=================================>????????????????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //??????????????????
        startDate = System.currentTimeMillis();
        List<HmeEoJobSn> resultList = getResultData(tenantId, hmeEoJobSnVO16, dto);
        log.info("=================================>????????????????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
        return resultList;
    }

    /**
     *
     * @Description ???????????????
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:10
     * @param tenantId ??????ID
     * @param hmeEoJobSnVO16 ??????
     * @param dto ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchBackFlushMaterialOutSite(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite tenantId={},hmeEoJobSnVO16={},dto={}", tenantId, hmeEoJobSnVO16, dto);
        //?????????????????????????????????????????????
        if (CollectionUtils.isEmpty(hmeEoJobSnVO16.getBackFlushBomComponentList())) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:EO?????????????????????");
            return;
        }

        Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO16.getEoMap();
        Map<String, HmeWorkOrderVO2> woMap = hmeEoJobSnVO16.getWoMap();

        //??????????????????????????????????????????,????????????????????????Y?????????????????????
        List<String> backFlushWoIdList = woMap.values().stream()
                .filter(item -> !HmeConstants.ConstantValue.YES.equals(item.getBackflushNotFlag()))
                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(backFlushWoIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:???????????????BackflushNotFlag???Y wo={}", woMap.values());
            return;
        }
        //????????????eo????????????????????????
        List<String> backFlushEoIdList = eoMap.values().stream()
                .filter(item -> backFlushWoIdList.contains(item.getWorkOrderId())
                        && CollectionUtils.isNotEmpty(item.getBomComponentList()))
                .map(MtEo::getEoId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(backFlushEoIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:??????????????????????????????EO backFlushWoIdList={}", backFlushWoIdList);
            return;
        }
        List<HmeEoJobSnVO3> snLineList = dto.getSnLineList().stream().filter(item ->
                backFlushEoIdList.contains(item.getEoId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(snLineList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:????????????EO??????????????? backFlushEoIdList={}", backFlushEoIdList);
            return;
        }

        //????????????????????????ID
        List<String> backFlushBomComponentIdList = hmeEoJobSnVO16.getBackFlushBomComponentList().stream()
                .map(MtBomComponent::getBomComponentId).collect(Collectors.toList());

        //????????????????????????EO??????
        List<HmeBomComponentVO> allReleaseEoComponentList = new ArrayList<>();
        List<String> releaseEoComponentIdList = new ArrayList<>();
        Map<String, List<String>> componentEoMap = new HashMap<>();
        for (HmeEoVO4 item : eoMap.values()) {
            //???????????????????????????wo?????????EO
            if (backFlushEoIdList.contains(item.getEoId())
                    && CollectionUtils.isNotEmpty(item.getBomComponentList())) {
                for (HmeBomComponentVO eoComponent : item.getBomComponentList()) {
                    //????????????????????????????????????BomComponent
                    if (backFlushBomComponentIdList.contains(eoComponent.getBomComponentId())) {
                        //????????????????????????
                        if (!releaseEoComponentIdList.contains(eoComponent.getBomComponentId())) {
                            allReleaseEoComponentList.add(eoComponent);
                            releaseEoComponentIdList.add(eoComponent.getBomComponentId());

                            //?????????????????????id?????????eoId????????????
                            List<String> eoIdList = new ArrayList<>();
                            eoIdList.add(item.getEoId());
                            componentEoMap.put(eoComponent.getBomComponentId(), eoIdList);
                        } else {
                            //?????????????????????id?????????eoId????????????
                            componentEoMap.get(eoComponent.getBomComponentId()).add(item.getEoId());
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(releaseEoComponentIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:????????????????????????EO?????? backFlushBomComponentIdList={}", backFlushBomComponentIdList);
            return;
        }

        //??????BOM??????????????????????????????BOM????????????
        List<String> tempEoIdList = componentEoMap.get(allReleaseEoComponentList.get(0).getBomComponentId());
        HmeEoVO4 tempEo = eoMap.get(tempEoIdList.get(0));
        if (Objects.isNull(tempEo.getBomPrimaryQty())) {
            //${1}????????? ?????????${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "BOM", tempEo.getBomId()));
        }
        BigDecimal bomPrimaryQty = BigDecimal.valueOf(tempEo.getBomPrimaryQty());
        if(bomPrimaryQty.compareTo(BigDecimal.ZERO) == 0){
            //???????????????0,?????????
            throw new MtException("HME_EO_JOB_SN_082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_082", "HME"));
        }

        //?????????????????????ID
        List<String> backFlushMaterialIdList = hmeEoJobSnVO16.getBackFlushBomComponentList().stream()
                .filter(item -> releaseEoComponentIdList.contains(item.getBomComponentId()))
                .map(MtBomComponent::getMaterialId).collect(Collectors.toList());

        //??????????????????
        MtModLocator backFlushLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId,HmeConstants.LocaltorType.BACKFLUSH,dto.getWorkcellId());

        List<HmeMaterialLotVO3> backFlushMaterialLotList = hmeEoJobSnLotMaterialMapper
                .batchQueryBackFlushMaterialLot(tenantId, backFlushMaterialIdList, backFlushLocator.getLocatorId());
        if (CollectionUtils.isEmpty(backFlushMaterialLotList)) {
            List<MtMaterialVO> mtMaterialVOList = mtMaterialRepository.materialPropertyBatchGet(tenantId, backFlushMaterialIdList);
            List<String> backFlushMaterialCodeList = mtMaterialVOList.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
            //????????????????????????????????????{1}??????????????????
            throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_087", "HME" , String.join(",", backFlushMaterialCodeList)));
        }

        //??????????????????
        MtModLocator warehouse =  hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,backFlushLocator.getLocatorId());

        //????????????????????????
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());

        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        // ????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //??????????????????
        String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
        WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
        wmsTransactionTypePara.setTenantId(tenantId);
        wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);

        //???????????????WO??????????????????
        Map<String, List<MtWorkOrderComponentActual>> woComponentActualMap = new HashMap<>();
        List<String> backFlushWoBomComponentIdList = new ArrayList<>();
        for (HmeWorkOrderVO2 item : woMap.values()) {
            if (backFlushWoIdList.contains(item.getWorkOrderId()) && CollectionUtils.isNotEmpty(item.getBomComponentList())) {
                backFlushWoBomComponentIdList.addAll(item.getBomComponentList()
                        .stream().map(HmeBomComponentVO::getBomComponentId).collect(Collectors.toList()));
            }
        }
        if (CollectionUtils.isNotEmpty(backFlushWoBomComponentIdList)) {
            backFlushWoBomComponentIdList = backFlushWoBomComponentIdList.stream().distinct().collect(Collectors.toList());

            SecurityTokenHelper.close();
            List<MtWorkOrderComponentActual> woComponentActualList = mtWorkOrderComponentActualRepository
                    .selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                                    .andIn(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, backFlushWoIdList)
                                    .andIn(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, backFlushWoBomComponentIdList))
                            .build());
            if (CollectionUtils.isNotEmpty(woComponentActualList)) {
                woComponentActualMap = woComponentActualList.stream()
                        .collect(Collectors.groupingBy(item -> item.getWorkOrderId() + "," + item.getBomComponentId()));
            }
        }

        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite ??????????????????[{}]", allReleaseEoComponentList.size());

        //?????????????????????????????????
        Map<String, MtAssembleProcessActualVO17> eoAssembleProcessActualMap = new HashMap<>();
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnVO16.getMaterialLotMap();
        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList = new ArrayList<>();
        //??????????????????API??????
        List<MtMaterialLotVO32> mtMaterialList = new ArrayList<>();
        for (HmeBomComponentVO releaseEoComponent : allReleaseEoComponentList) {
            //??????????????????????????????????????????
            List<HmeMaterialLotVO3> releaseMaterialLotList = backFlushMaterialLotList.stream()
                    .filter(item -> releaseEoComponent.getBomComponentMaterialId().equals(item.getMaterialId()))
                    .sorted(Comparator.comparing(HmeMaterialLotVO3::getCreationDate)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(releaseMaterialLotList)) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(releaseEoComponent.getBomComponentMaterialId());
                //????????????????????????????????????{1}??????????????????
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            //???????????????????????????EO/WO/??????
            List<String> componentEoIdList = componentEoMap.get(releaseEoComponent.getBomComponentId());
            List<HmeEoVO4> currentEoList = new ArrayList<>();
            componentEoIdList.forEach(eoId -> currentEoList.add(eoMap.get(eoId)));
            HmeWorkOrderVO2 currentWo = woMap.get(currentEoList.get(0).getWorkOrderId());
            List<HmeEoJobSnVO3> currentSnLineList = snLineList.stream()
                    .filter(item -> componentEoIdList.contains(item.getEoId())).collect(Collectors.toList());

            //??????????????????????????????????????? key:eoId value:remainQty
            Map<String, BigDecimal> currentEoRemainQtyMap = new HashMap<>();
            BigDecimal releaseEoBomComponentQty = BigDecimal.valueOf(releaseEoComponent.getBomComponentQty());
            currentSnLineList.forEach(item -> {
                BigDecimal primaryUomQty = BigDecimal.valueOf(materialLotMap.get(item.getMaterialLotId()).getPrimaryUomQty());
                BigDecimal remain = releaseEoBomComponentQty.multiply(primaryUomQty).divide(bomPrimaryQty, 4 , BigDecimal.ROUND_HALF_UP);

                currentEoRemainQtyMap.put(item.getEoId(), remain);
            });

            //??????wo??????????????????eo???????????????????????????
            HmeBomComponentVO releaseWoComponent = null;
            if (CollectionUtils.isNotEmpty(currentWo.getBomComponentList())) {
                List<HmeBomComponentVO> releaseWoComponentList = currentWo.getBomComponentList().stream()
                        .filter(item -> item.getBomComponentMaterialId().equals(releaseEoComponent.getBomComponentMaterialId())
                                && item.getBomComponentLineNumber().equals(releaseEoComponent.getBomComponentLineNumber()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(releaseWoComponentList)) {
                    releaseWoComponent = releaseWoComponentList.get(0);
                }
            }
            if (Objects.isNull(releaseWoComponent)) {
                log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite continue:???????????? wo??????????????????eo??????????????????????????? releaseEoComponent={}", releaseEoComponent);
                continue;
            }

            //?????????????????????????????????????????????????????????0??????
            BigDecimal woDemandQty = StringUtils.isBlank(releaseWoComponent.getWoBomComponentDemandQty())
                    ? BigDecimal.ZERO : new BigDecimal(releaseWoComponent.getWoBomComponentDemandQty());
            if(woDemandQty.compareTo(BigDecimal.ZERO) <= 0){
                log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite continue:??????????????????????????????0 releaseWoComponent={}", releaseWoComponent);
                continue;
            }

            //??????????????????????????????
            BigDecimal remainQty = BigDecimal.ZERO;
            for (BigDecimal remain : currentEoRemainQtyMap.values()) {
                remainQty = remainQty.add(remain);
            }

            //??????WO????????????????????????
            if (woComponentActualMap.containsKey(currentWo.getWorkOrderId() + "," + releaseWoComponent.getBomComponentId())) {
                double assembleQty = woComponentActualMap.get(currentWo.getWorkOrderId() + "," + releaseWoComponent.getBomComponentId())
                        .stream().mapToDouble(MtWorkOrderComponentActual::getAssembleQty).sum();
                if ((BigDecimal.valueOf(assembleQty).add(remainQty)).compareTo(woDemandQty) > 0) {
                    log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite continue:??????WO?????????????????????????????????" +
                            " releaseWoComponent={}, assembleQty={}, remainQty={}", releaseWoComponent, assembleQty, remainQty);
                    continue;
                }
            }

            //??????????????????????????????
            double releaseMaterialLotPrimaryQtySum = releaseMaterialLotList.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
            if(BigDecimal.valueOf(releaseMaterialLotPrimaryQtySum).compareTo(remainQty) < 0){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(releaseEoComponent.getBomComponentMaterialId());
                //????????????????????????????????????{1}??????????????????
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite ???ID[{}],???????????????[{}]", releaseWoComponent.getBomComponentId(), remainQty);

            //API-????????????????????? key:MaterialLotId
//            Map<String, MtMaterialLotVO1> materialLotConsumeMap = new HashMap<>();
            //???????????????????????? key:MaterialLotId
            Map<String, WmsObjectTransactionRequestVO> objectTransactionMap = new HashMap<>();

            Long sequence = 0L;
            BigDecimal trxPrimaryUomQty = BigDecimal.ZERO;
            List<MtMaterialLotVO33> mtMaterialLotVO33List = new ArrayList<>();

            //????????????EO????????????????????????
            for (HmeEoVO4 currEo : currentEoList) {
                //??????EO????????????
                BigDecimal currRemainQty = currentEoRemainQtyMap.get(currEo.getEoId());

                //????????????EO???????????????
                Optional<HmeEoJobSnVO3> currSn = currentSnLineList.stream().filter(item -> currEo.getEoId().equals(item.getEoId())).findFirst();

                //??????API-??????????????????
                List<MtAssembleProcessActualVO11> materialInfo = new ArrayList<>();
                if (eoAssembleProcessActualMap.containsKey(currEo.getEoId())) {
                    //????????????EO???????????????????????????
                    materialInfo = eoAssembleProcessActualMap.get(currEo.getEoId()).getMaterialInfo();
                }

                //????????????????????????
                for (HmeMaterialLotVO3 releaseMaterialLot : releaseMaterialLotList) {
                    //?????????????????????????????????????????????????????????
                    if (releaseMaterialLot.isReleasedAll()) {
                        continue;
                    }

                    //????????????????????????????????????,????????????????????????????????????
                    if (Objects.isNull(releaseMaterialLot.getRemainPrimaryUomQty())) {
                        releaseMaterialLot.setRemainPrimaryUomQty(BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty()));
                    }

                    //??????????????????????????????
                    BigDecimal currentQty = currRemainQty;
                    if (currRemainQty.compareTo(releaseMaterialLot.getRemainPrimaryUomQty()) >= 0) {
                        //??????????????????????????????????????????
                        currentQty = releaseMaterialLot.getRemainPrimaryUomQty();

                        //??????????????????????????? ??????????????????,????????????
                        releaseMaterialLot.setRemainPrimaryUomQty(BigDecimal.ZERO);
                        releaseMaterialLot.setReleasedAll(true);
                    } else {
                        //?????????????????? ????????? ??????????????????=??????????????????????????????-??????????????????
                        releaseMaterialLot.setRemainPrimaryUomQty(releaseMaterialLot.getRemainPrimaryUomQty().subtract(currentQty));
                    }
                    currRemainQty = currRemainQty.subtract(currentQty);

                    //??????API-?????????????????????
//                    if (materialLotConsumeMap.containsKey(releaseMaterialLot.getMaterialLotId())) {
//                        //??????????????????
//                        MtMaterialLotVO1 mtMaterialLotVO1 = materialLotConsumeMap.get(releaseMaterialLot.getMaterialLotId());
//                        BigDecimal trxPrimaryUomQty = BigDecimal.valueOf(mtMaterialLotVO1.getTrxPrimaryUomQty()).add(currentQty);
//                        mtMaterialLotVO1.setTrxPrimaryUomQty(trxPrimaryUomQty.doubleValue());
//                        materialLotConsumeMap.replace(releaseMaterialLot.getMaterialLotId(), mtMaterialLotVO1);
//                    } else {
//                        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
//                        mtMaterialLotVO1.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
//                        mtMaterialLotVO1.setPrimaryUomId(releaseMaterialLot.getPrimaryUomId());
//                        mtMaterialLotVO1.setTrxPrimaryUomQty(currentQty.doubleValue());
//                        mtMaterialLotVO1.setEventRequestId(eventRequestId);
//                        if (StringUtils.isNotEmpty(releaseMaterialLot.getSecondaryUomId())) {
//                            mtMaterialLotVO1.setSecondaryUomId(releaseMaterialLot.getSecondaryUomId());
//                            mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
//                        }
//                        materialLotConsumeMap.put(releaseMaterialLot.getMaterialLotId(), mtMaterialLotVO1);
//                    }

                    //?????????????????????
                    if(currentQty.compareTo(BigDecimal.ZERO) > 0) {
                        List<MtMaterialLotVO33> mtMaterialLotVO33List2 = new ArrayList<>();
                        boolean isRecord = true;
                        if (CollectionUtils.isNotEmpty(mtMaterialLotVO33List)) {
                            mtMaterialLotVO33List2 = mtMaterialLotVO33List.stream().filter(item -> item.getMaterialLotId().equals(releaseMaterialLot.getMaterialLotId()))
                                    .collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(mtMaterialLotVO33List2)) {
                                isRecord = false;
                            }
                        }
                        if (isRecord) {
                            MtMaterialLotVO33 mtMaterialLotVO33 = new MtMaterialLotVO33();
                            mtMaterialLotVO33.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                            mtMaterialLotVO33.setConsumeFlag(HmeConstants.ConstantValue.YES);
                            mtMaterialLotVO33.setSequence(sequence++);
                            mtMaterialLotVO33List.add(mtMaterialLotVO33);
                        }
                        trxPrimaryUomQty = trxPrimaryUomQty.add(currentQty);
                    }

                    //??????????????????????????????
                    MtAssembleProcessActualVO11 assembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                    assembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                    assembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                    assembleProcessActualVO11.setBomComponentId(releaseEoComponent.getBomComponentId());
                    assembleProcessActualVO11.setMaterialId(releaseMaterialLot.getMaterialId());
                    assembleProcessActualVO11.setTrxAssembleQty(currentQty.doubleValue());
                    assembleProcessActualVO11.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                    assembleProcessActualVO11.setLocatorId(releaseMaterialLot.getLocatorId());
                    materialInfo.add(assembleProcessActualVO11);

                    //????????????????????????
                    if (objectTransactionMap.containsKey(releaseMaterialLot.getMaterialLotId())) {
                        //??????????????????
                        WmsObjectTransactionRequestVO objectTransactionVO = objectTransactionMap.get(releaseMaterialLot.getMaterialLotId());
                        BigDecimal transactionQty = objectTransactionVO.getTransactionQty().add(currentQty);
                        objectTransactionVO.setTransactionQty(transactionQty);
                        objectTransactionMap.replace(releaseMaterialLot.getMaterialLotId(), objectTransactionVO);
                    } else {
                        //??????????????????
                        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                        objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                        objectTransactionVO.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                        objectTransactionVO.setLotNumber(releaseMaterialLot.getLot());
                        objectTransactionVO.setEventId(eventId);
                        objectTransactionVO.setMaterialId(releaseMaterialLot.getMaterialId());
                        objectTransactionVO.setMaterialCode(releaseMaterialLot.getMaterialCode());
                        objectTransactionVO.setTransactionQty(currentQty);
                        objectTransactionVO.setTransactionUom(releaseMaterialLot.getMaterialPrimaryUomCode());
                        objectTransactionVO.setTransactionTime(new Date());
                        objectTransactionVO.setPlantId(dto.getSiteId());
                        objectTransactionVO.setLocatorId(backFlushLocator.getLocatorId());
                        objectTransactionVO.setLocatorCode(backFlushLocator.getLocatorCode());
                        if (Objects.nonNull(warehouse)) {
                            objectTransactionVO.setWarehouseId(warehouse.getLocatorId());
                            objectTransactionVO.setWarehouseCode(warehouse.getLocatorCode());
                        }
                        objectTransactionVO.setWorkOrderNum(currentWo.getWorkOrderNum());
                        objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                        objectTransactionVO.setProdLineCode(currentWo.getProdLineCode());
                        objectTransactionVO.setMoveType(Objects.isNull(wmsTransactionType) ? "261" : wmsTransactionType.getMoveType());
                        objectTransactionVO.setBomReserveNum(StringUtils.trimToEmpty(releaseEoComponent.getReserveNum()));
                        objectTransactionVO.setBomReserveLineNum(String.valueOf(releaseEoComponent.getBomComponentLineNumber()));
                        objectTransactionVO.setSoNum(releaseMaterialLot.getSoNum());
                        objectTransactionVO.setSoLineNum(releaseMaterialLot.getSoLineNum());
                        objectTransactionMap.put(releaseMaterialLot.getMaterialLotId(), objectTransactionVO);
                    }

                    //???????????????????????????
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setTenantId(tenantId);
                    hmeEoJobSnLotMaterial.setMaterialType("BACKFLUSH");
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    currSn.ifPresent(item -> hmeEoJobSnLotMaterial.setJobId(item.getJobId()));
                    hmeEoJobSnLotMaterial.setMaterialId(releaseMaterialLot.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setReleaseQty(currentQty);
                    hmeEoJobSnLotMaterial.setIsReleased(1);
                    hmeEoJobSnLotMaterial.setLocatorId(releaseMaterialLot.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(releaseMaterialLot.getLot());
                    hmeEoJobSnLotMaterial.setProductionVersion(releaseMaterialLot.getMaterialVersion());
                    hmeEoJobSnLotMaterial.setVirtualFlag(HmeConstants.ConstantValue.NO);
                    hmeEoJobSnLotMaterial.setObjectVersionNumber(1L);
                    hmeEoJobSnLotMaterial.setCreatedBy(hmeEoJobSnVO16.getUserId());
                    hmeEoJobSnLotMaterial.setCreationDate(hmeEoJobSnVO16.getCurrentDate());
                    hmeEoJobSnLotMaterial.setLastUpdatedBy(hmeEoJobSnVO16.getUserId());
                    hmeEoJobSnLotMaterial.setLastUpdateDate(hmeEoJobSnVO16.getCurrentDate());
                    hmeEoJobSnLotMaterialList.add(hmeEoJobSnLotMaterial);

                    //??????EO??????????????????
                    if (currRemainQty.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }
                }

                //????????????EO?????????????????????
                if (eoAssembleProcessActualMap.containsKey(currEo.getEoId())) {
                    //????????????map
                    MtAssembleProcessActualVO17 assembleProcessActualVO17 = eoAssembleProcessActualMap.get(currEo.getEoId());
                    assembleProcessActualVO17.setMaterialInfo(materialInfo);
                    eoAssembleProcessActualMap.replace(currEo.getEoId(), assembleProcessActualVO17);
                } else {
                    //????????????eo???????????????
                    MtAssembleProcessActualVO17 assembleProcessActualVO17 = new MtAssembleProcessActualVO17();
                    assembleProcessActualVO17.setAssembleRouterType(HmeConstants.ConstantValue.NO);
                    assembleProcessActualVO17.setEoId(currEo.getEoId());
                    assembleProcessActualVO17.setRouterId(currentEoList.get(0).getRouterId());
                    currSn.ifPresent(item -> assembleProcessActualVO17.setRouterStepId(item.getEoStepId()));
                    assembleProcessActualVO17.setMaterialInfo(materialInfo);
                    eoAssembleProcessActualMap.put(currEo.getEoId(), assembleProcessActualVO17);
                }
            }
            objectTransactionRequestList.addAll(objectTransactionMap.values());

            //???????????????????????????API
//            for (MtMaterialLotVO1 materialLotConsume : materialLotConsumeMap.values()) {
//                mtMaterialLotRepository.materialLotConsume(tenantId, materialLotConsume);
//            }

            if(CollectionUtils.isNotEmpty(mtMaterialLotVO33List)) {
                MtMaterialLotVO32 mtMaterialLotVO32 = new MtMaterialLotVO32();
                mtMaterialLotVO32.setMaterialId(releaseEoComponent.getBomComponentMaterialId());
                mtMaterialLotVO32.setMtMaterialLotVO33List(mtMaterialLotVO33List);
                mtMaterialLotVO32.setTrxPrimaryUomQty(trxPrimaryUomQty.doubleValue());
                mtMaterialList.add(mtMaterialLotVO32);
            }
        }

        //API-????????????????????????
        if (eoAssembleProcessActualMap.size() > 0) {
            MtAssembleProcessActualVO16 assembleProcessActualVO16 = new MtAssembleProcessActualVO16();
            assembleProcessActualVO16.setOperationId(dto.getOperationId());
            assembleProcessActualVO16.setWorkcellId(dto.getWorkcellId());
            assembleProcessActualVO16.setParentEventId(eventId);
            assembleProcessActualVO16.setEventRequestId(eventRequestId);
            assembleProcessActualVO16.setOperationBy(String.valueOf(hmeEoJobSnVO16.getUserId()));
            assembleProcessActualVO16.setEoInfo(new ArrayList<>(eoAssembleProcessActualMap.values()));
            assembleProcessActualVO16.setLocatorId(backFlushLocator.getLocatorId());
            if (Objects.nonNull(mtWkcShift)) {
                assembleProcessActualVO16.setShiftCode(mtWkcShift.getShiftCode());
                if (Objects.nonNull(mtWkcShift.getShiftDate())) {
                    assembleProcessActualVO16.setShiftDate(mtWkcShift.getShiftDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
            }
            mtAssembleProcessActualRepository.componentAssembleBatchProcess(tenantId, assembleProcessActualVO16);
        }

        if(CollectionUtils.isNotEmpty(mtMaterialList)) {
            MtMaterialLotVO31 mtMaterialLotVO31 = new MtMaterialLotVO31();
            mtMaterialLotVO31.setEventRequestId(eventRequestId);
            mtMaterialLotVO31.setAllConsume(HmeConstants.ConstantValue.NO);
            mtMaterialLotVO31.setMtMaterialList(mtMaterialList);
            long startDate = System.currentTimeMillis();
            mtMaterialLotRepository.sequenceLimitMaterialLotBatchConsumeForNew(tenantId, mtMaterialLotVO31);
            log.info("=================================>????????????-???????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
        }

        //??????????????????
        if (CollectionUtils.isNotEmpty(objectTransactionRequestList)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        }

        //?????????????????????
        if (CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterialList)) {
            int index = 0;
            List<String> idList = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_s", hmeEoJobSnLotMaterialList.size());
            List<String> cidList = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_cid_s", hmeEoJobSnLotMaterialList.size());
            for (HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial : hmeEoJobSnLotMaterialList) {
                hmeEoJobSnLotMaterial.setJobMaterialId(idList.get(index));
                hmeEoJobSnLotMaterial.setCid(Long.valueOf(cidList.get(index)));
                index++;
            }
            hmeEoJobSnLotMaterialRepository.myBatchInsert(hmeEoJobSnLotMaterialList);
        }
    }


    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/19 12:04
     * @param tenantId ??????ID
     * @param hmeEoJobSnVO16  ??????
     * @param dto ??????
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    private List<HmeEoJobSn> getResultData(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        List<HmeEoJobSn> hmeEoJobSnEntityList = hmeEoJobSnVO16.getHmeEoJobSnEntityList();

        //??????????????????/????????????
        List<String> eoStepIdList = hmeEoJobSnEntityList.stream().map(HmeEoJobSn::getEoStepId).collect(Collectors.toList());
        Map<String, HmeRouterStepVO4> routerStepMap = hmeEoJobSnCommonService.batchQueryCurrentAndNextStep(tenantId, eoStepIdList);

        //??????????????????
        Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnVO16.getMaterialLotMap();
        Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO16.getEoMap();
        Map<String, HmeWorkOrderVO2> woMap = hmeEoJobSnVO16.getWoMap();
        for (HmeEoJobSn eoJobSn : hmeEoJobSnEntityList) {
            HmeMaterialLotVO3 hmeMaterialLot = materialLotMap.get(eoJobSn.getMaterialLotId());
            HmeWorkOrderVO2 hmeWorkOrder = woMap.get(eoMap.get(eoJobSn.getEoId()).getWorkOrderId());
            HmeRouterStepVO4 hmeRouterStep = routerStepMap.get(eoJobSn.getEoStepId());

            eoJobSn.setSiteOutDate(hmeEoJobSnVO16.getCurrentDate());
            eoJobSn.setSiteOutBy(hmeEoJobSnVO16.getUserId());
            eoJobSn.setSnMaterialCode(hmeMaterialLot.getMaterialCode());
            eoJobSn.setSnMaterialName(hmeMaterialLot.getMaterialName());
            eoJobSn.setWorkOrderNum(hmeWorkOrder.getWorkOrderNum());
            eoJobSn.setRemark(hmeWorkOrder.getRemark());
            eoJobSn.setCurrentStepName(hmeRouterStep.getStepName());
            eoJobSn.setNextStepName(hmeRouterStep.getNextStepName());
        }
        return hmeEoJobSnEntityList;
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/19 12:03
     * @param tenantId ??????ID
     * @param dto ??????
     * @param eoIdList eoIdList
     * @param jobIdList jobIdList
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    private HmeEoJobSnVO16 queryBasicData(Long tenantId, HmeEoJobSnVO3 dto, List<String> eoIdList, List<String> jobIdList) {
        //??????????????????ID
        List<String> materialLotIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());

        //??????JobId??????EoJobSn??????
        long startDate = System.currentTimeMillis();
        SecurityTokenHelper.close();
        List<HmeEoJobSn> hmeEoJobSnEntityList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeEoJobSn.FIELD_JOB_ID, jobIdList)).build());
        log.info("=================================>????????????????????????-????????????JobId??????EoJobSn??????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if (CollectionUtils.isEmpty(hmeEoJobSnEntityList) || hmeEoJobSnEntityList.size() != jobIdList.size()) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //??????EoJobSn?????????????????????????????????
        List<HmeEoJobSn> siteOutList = hmeEoJobSnEntityList.stream().filter(item -> Objects.nonNull(item.getSiteOutDate()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(siteOutList)) {
            // 	?????????SN????????????????????????
            throw new MtException("HME_EO_JOB_SN_056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_056", "HME"));
        }

        //????????????EO???WO????????????
        String eoStepId = hmeEoJobSnEntityList.get(0).getEoStepId();
        startDate = System.currentTimeMillis();
        HmeEoJobSnVO16 hmeEoJobSnVO16 = hmeEoJobSnCommonService.batchQueryEoAndWoInfoWithComponentById(tenantId, eoStepId, eoIdList);
        log.info("=================================>????????????????????????-??????????????????EO???WO????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        hmeEoJobSnVO16.setHmeEoJobSnEntityList(hmeEoJobSnEntityList);

        //????????????????????????
        startDate = System.currentTimeMillis();
        hmeEoJobSnVO16.setContainerOutSiteFlag(hmeEoJobSnCommonService.isContainerOutSite(tenantId, dto.getWorkcellId()));
        log.info("=================================>????????????????????????-??????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //????????????????????????ID
        if (hmeEoJobSnVO16.getContainerOutSiteFlag()) {
            startDate = System.currentTimeMillis();
            List<HmeEoJobContainer> hmeEoJobContainers = hmeEoJobContainerRepository.selectByCondition(Condition.builder(HmeEoJobContainer.class)
                    .select(HmeEoJobContainer.FIELD_JOB_CONTAINER_ID, HmeEoJobContainer.FIELD_CONTAINER_ID)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobContainer.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobContainer.FIELD_WORKCELL_ID, dto.getWorkcellId())).build());
            log.info("=================================>????????????????????????-??????????????????????????????ID????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if (CollectionUtils.isEmpty(hmeEoJobContainers)
                    || Objects.isNull(hmeEoJobContainers.get(0))
                    || StringUtils.isBlank(hmeEoJobContainers.get(0).getJobContainerId())) {
                throw new MtException("HME_EO_JOB_SN_010", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_010", "HME"));
            }
            hmeEoJobSnVO16.setEoJobContainer(hmeEoJobContainers.get(0));
        }

        //?????????????????????BOM ture????????????????????????????????????????????????????????? add by yuchao.wang for peng.zhao at 2021.1.29
        startDate = System.currentTimeMillis();
        hmeEoJobSnVO16.setOperationBomFlag(hmeEoJobSnCommonService.queryOperationBomFlag(tenantId, dto.getOperationId()));
        log.info("=================================>????????????????????????-??????queryOperationBomFlag????????????"+(System.currentTimeMillis() - startDate) + "??????");

        if (!hmeEoJobSnVO16.getOperationBomFlag()) {

            //????????????routerId??????
            //V20210518 modify by penglin.sui for peng.zhao BOM_FLAG??????Y???????????????????????????????????????????????????????????????
            List<String> routerIdList = hmeEoJobSnVO16.getEoMap().values().stream().map(HmeEoVO4::getRouterId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(routerIdList) || routerIdList.size() > 1) {
                //????????????SN??????????????????????????????,?????????
                throw new MtException("HME_EO_JOB_SN_138", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_138", "HME"));
            }

            //???????????????BOM??????
            List<String> bomIdList = hmeEoJobSnVO16.getEoMap().values().stream().map(HmeEoVO4::getBomId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(bomIdList) || bomIdList.size() > 1) {
                //????????????SN??????????????????????????????,?????????
                throw new MtException("HME_EO_JOB_SN_136", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_136", "HME"));
            }

            //????????????bom??????
            List<HmeBomComponentVO> bomComponentList = new ArrayList<>();
            for (HmeEoVO4 item : hmeEoJobSnVO16.getEoMap().values()) {
                if (CollectionUtils.isNotEmpty(item.getBomComponentList())) {
                    bomComponentList.addAll(item.getBomComponentList());
                }
            }

            //???????????????????????????
            if (CollectionUtils.isNotEmpty(bomComponentList)) {
                List<String> bomComponentIdList = bomComponentList.stream().map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());
                startDate = System.currentTimeMillis();
                List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnLotMaterialMapper.selectBackFlash(tenantId, dto.getSiteId(), bomComponentIdList);
                log.info("=================================>????????????????????????-?????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                hmeEoJobSnVO16.setBackFlushBomComponentList(backFlushBomComponentList);
            } else {
                hmeEoJobSnVO16.setBackFlushBomComponentList(new ArrayList<MtBomComponent>());
            }
        }

        //????????????????????????
        startDate = System.currentTimeMillis();
        Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnCommonService.batchQueryMaterialLotInfo(tenantId, materialLotIdList);
        log.info("=================================>????????????????????????-??????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        hmeEoJobSnVO16.setMaterialLotMap(materialLotMap);

        //??????????????????
        Map<String, String> reworkFlagMap = new HashMap<>();
        for (Map.Entry<String, HmeMaterialLotVO3> entry : materialLotMap.entrySet()) {
            reworkFlagMap.put(entry.getKey(), entry.getValue().getReworkFlag());
        }
        hmeEoJobSnVO16.setReworkFlagMap(reworkFlagMap);

        // ??????????????????
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        hmeEoJobSnVO16.setUserId(userId);
        hmeEoJobSnVO16.setCurrentDate(new Date());

        //V20210312 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????
        startDate = System.currentTimeMillis();
        List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnCommonService.queryNgDataRecord(tenantId,dto.getWorkcellId(),jobIdList);
        log.info("=================================>????????????????????????-????????????????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if(CollectionUtils.isNotEmpty(ngDataRecordList)) {
            hmeEoJobSnVO16.setNgDataRecordList(ngDataRecordList);
        }

        Map<String,HmeEoJobDataRecordVO2> hmeEoJobDataRecordVO2Map = hmeEoJobDataRecordRepository.queryNcRecord(tenantId,jobIdList);
        hmeEoJobSnVO16.setNcJobDataRecordMap(hmeEoJobDataRecordVO2Map);

        return hmeEoJobSnVO16;
    }

    /**
     *
     * @Description ????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 17:13
     * @return void
     *
     */
    private HmeEoJobSn outSiteValidate(Long tenantId, List<String> jobIdList, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        //????????????????????????
        if (HmeConstants.OutSiteAction.REWORK.equals(dto.getOutSiteAction())) {
            throw new MtException("HME_EO_JOB_SN_133", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_133", "HME"));
        }

        //??????????????????NG???????????????
        Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnVO16.getMaterialLotMap();
        long ngCount = materialLotMap.values().stream()
                .filter(item -> HmeConstants.ConstantValue.NG.equals(item.getQualityStatus())
                        && !HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())).count();
        if (ngCount > 0) {
            throw new MtException("HME_EO_JOB_SN_060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_060", "HME"));
        }

        //?????????????????????????????????,????????????????????????
        long reworkCount = materialLotMap.values().stream()
                .filter(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())).count();
        if (reworkCount > 0) {
            throw new MtException("HME_EO_JOB_SN_133", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_133", "HME"));
        }

        //V20210630 modify by penglin.sui for peng.zhao ??????????????????????????????SN????????????
        StringBuilder materialLots = new StringBuilder();
        for(HmeMaterialLotVO3 item : materialLotMap.values()){
            if(StringUtils.isNotBlank(item.getStocktakeFlag()) &&
            HmeConstants.ConstantValue.YES.equals(item.getStocktakeFlag())){
                if(materialLots.length() == 0){
                    materialLots.append(item.getMaterialLotCode());
                }else{
                    materialLots.append("," + item.getMaterialLotCode());
                }
            }
        }
        if(materialLots.length() > 0){
            //???SN???${1}???????????????,????????????
            throw new MtException("HME_EO_JOB_SN_204", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_204", "HME", materialLots.toString()));
        }

        // 20210721 modify by sanfeng.zhang for xenxin.zhang ???????????????????????????Y???SN????????????
        StringBuilder materialLotFreezeFlags = new StringBuilder();
        for(HmeMaterialLotVO3 item : materialLotMap.values()){
            if(StringUtils.isNotBlank(item.getFreezeFlag()) &&
                    HmeConstants.ConstantValue.YES.equals(item.getFreezeFlag())){
                if(materialLotFreezeFlags.length() == 0){
                    materialLotFreezeFlags.append(item.getMaterialLotCode());
                }else{
                    materialLotFreezeFlags.append("," + item.getMaterialLotCode());
                }
            }
        }
        if(materialLotFreezeFlags.length() > 0){
            //??????SN???${1}???????????????,???????????????
            throw new MtException("HME_EO_JOB_SN_207", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_207", "HME", materialLotFreezeFlags.toString()));
        }

        //????????????????????????
//        if (hmeEoJobSnCommonService.hasMissingValue(tenantId, dto.getWorkcellId(), jobIdList)) {
//            // ????????????,??????????????????????????????????????????
//            throw new MtException("HME_EO_JOB_SN_009", mtErrorMessageRepository
//                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_009", "HME"));
//        }
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = hmeEoJobSnCommonService.hasMissingValueTag(tenantId, dto.getWorkcellId(), jobIdList);
        if(CollectionUtils.isNotEmpty(hmeEoJobDataRecordVOList)){
            List<HmeEoJobSnVO3> snLineList = dto.getSnLineList().stream().filter(item -> item.getJobId().equals(hmeEoJobDataRecordVOList.get(0).getJobId()))
                    .collect(Collectors.toList());
            // sn${1}???????????????${2}???????????????,???????????????!
            throw new MtException("HME_EO_JOB_SN_195", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_195", "HME",
                            snLineList.get(0).getSnNum() , hmeEoJobDataRecordVOList.get(0).getTagDescription()));
        }

        //V20210331 modify by penglin.sui for tianyang.xie ???????????????????????????????????????????????????????????????
        //?????????????????????????????????
        List<String> eoStepIdList = hmeEoJobSnVO16.getHmeEoJobSnEntityList().stream().map(HmeEoJobSn::getEoStepId).distinct().collect(Collectors.toList());
        List<MtRouterDoneStep> mtRouterDoneStepList = mtRouterDoneStepRepository.selectByCondition(Condition.builder(MtRouterDoneStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterDoneStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterDoneStep.FIELD_ROUTER_STEP_ID, eoStepIdList)).build());
        if(CollectionUtils.isNotEmpty(mtRouterDoneStepList)) {
            if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())
                && CollectionUtils.isNotEmpty(hmeEoJobSnVO16.getNgDataRecordList())) {

                //??????JobId?????????????????????????????????
                Map<String, HmeEoJobSn> eoJobSnEntityMap = new HashMap<String, HmeEoJobSn>();
                hmeEoJobSnVO16.getHmeEoJobSnEntityList().forEach(item -> eoJobSnEntityMap.put(item.getJobId(), item));

                //???????????????????????????
                List<String> doneStepIdList = mtRouterDoneStepList.stream().map(MtRouterDoneStep::getRouterStepId).distinct().collect(Collectors.toList());
                //???????????????????????????
                List<HmeEoJobSnVO3> doneSnLineList = dto.getSnLineList().stream()
                        .filter(item -> doneStepIdList.contains(eoJobSnEntityMap.get(item.getJobId()).getEoStepId())).collect(Collectors.toList());

                for (HmeEoJobSnVO3 hmeEoJobSnVO3 : doneSnLineList
                ) {
                    List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnVO16.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(hmeEoJobSnVO3.getJobId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
//                        // ?????????????????????????????????,???????????????!
//                        throw new MtException("HME_EO_JOB_SN_192", mtErrorMessageRepository
//                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_192", "HME"));
                        // 	sn${1}???????????????${2}?????????,???????????????!
                        throw new MtException("HME_EO_JOB_SN_196", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_196", "HME",
                                        hmeEoJobSnVO3.getSnNum(), ngDataRecordList.get(0).getTagDescription()));
                    }
                }
            }
        }
        Long startDate = System.currentTimeMillis();
        //??????????????????????????????????????? add by yuchao.wang for peng.zhao at 2021.1.25
        if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag())) {
            //???????????????????????????????????????
            List<HmeEoJobDataRecord> allEoJobDataRecordList = hmeEoJobDataRecordRepository
                    .batchQueryForNcRecordValidate(tenantId, dto.getWorkcellId(), jobIdList);
            if (CollectionUtils.isNotEmpty(allEoJobDataRecordList)) {
                int index = -1;
                StringBuilder errorMessages = new StringBuilder();
                Map<String , List<HmeProcessNcDetailVO2>> processNcDetailMap = new HashMap<>();
                //???????????????????????????????????????????????????
                if (hmeEoJobSnCommonService.isAgeingNc(tenantId, dto.getOperationId())) {
                    //????????????
                    //??????????????????
                    String stationId = hmeEoJobSnMapper.queryWkcStation(tenantId, dto.getSiteId(), dto.getWorkcellId());

                    List<String> materialIdList = materialLotMap.values().stream()
                            .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());

                    //??????????????????????????????
                    List<HmeProcessNcHeaderVO2> processNcInfoList = hmeProcessNcHeaderRepository
                            .batchQueryProcessNcInfoForAgeingNcRecordValidate(tenantId, dto.getOperationId(),materialIdList);

                    //?????????????????????????????????jobId??????
                    Map<String, List<HmeEoJobDataRecord>> eoJobDataRecordMap = allEoJobDataRecordList
                            .stream().collect(Collectors.groupingBy(HmeEoJobDataRecord::getJobId));

                    //?????????????????????????????????
                    for (HmeEoJobSn eoJobSn : hmeEoJobSnVO16.getHmeEoJobSnEntityList()) {
                        index++;
                        if (!eoJobDataRecordMap.containsKey(eoJobSn.getJobId())) {
                            continue;
                        }

                        //?????????????????????????????????????????????
                        HmeMaterialLotVO3 materialLot = materialLotMap.get(eoJobSn.getMaterialLotId());
                        String materialLotCode = materialLot.getMaterialLotCode();
                        List<HmeEoJobDataRecord> currEoJobDataRecordList = eoJobDataRecordMap.get(eoJobSn.getJobId());

                        //V20210607 modify by penglin.sui for peng.zhao ????????????material_id????????????????????????????????????material_id???+???cos_model???????????????????????????????????????????????????????????????material_id???+???cos_model???+???workcell_id?????????????????????????????????????????????????????????HME_EO_JOB_SN_202
                        List<HmeProcessNcHeaderVO2> subProcessNcInfoList = processNcInfoList.stream().filter(item -> item.getMaterialId().equals(materialLot.getMaterialId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(subProcessNcInfoList)){

                            List<String> headerIdList = subProcessNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());

                            if(headerIdList.size() > 1) {
                                String cosModel = materialLotCode.substring(4, 5);
                                subProcessNcInfoList = subProcessNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getCosModel()) && item.getCosModel().equals(cosModel)).collect(Collectors.toList());

                                if (CollectionUtils.isEmpty(subProcessNcInfoList)) {
                                    // 	???SN???${1}??????????????????????????????,?????????!
                                    throw new MtException("HME_EO_JOB_SN_202", mtErrorMessageRepository
                                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_202", "HME",
                                                    materialLot.getMaterialCode()));
                                }

                                headerIdList = subProcessNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());

                                if (headerIdList.size() > 1) {
                                    subProcessNcInfoList = subProcessNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getStationId()) && item.getStationId().equals(stationId)).collect(Collectors.toList());

                                    if(CollectionUtils.isNotEmpty(subProcessNcInfoList)){
                                        headerIdList = subProcessNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());
                                    }

                                    if (CollectionUtils.isEmpty(subProcessNcInfoList) || headerIdList.size() > 1) {
                                        // 	???SN???${1}??????????????????????????????,?????????!
                                        throw new MtException("HME_EO_JOB_SN_202", mtErrorMessageRepository
                                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_202", "HME",
                                                        materialLot.getMaterialCode()));
                                    }
                                }
                            }
                        }

                        HmeProcessNcHeaderVO2 processNcInfo = null;
                        if(CollectionUtils.isNotEmpty(subProcessNcInfoList)){
                            processNcInfo = subProcessNcInfoList.get(0);
                        }

                        //??????????????????????????????????????????
                        HmeEoJobSn dataRecordValidateResult = hmeEoJobSnCommonService
                                .dataRecordAgeingProcessNcValidate(tenantId, materialLotCode, currEoJobDataRecordList, processNcInfo);
                        if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {

                            //V20210408 modify by penglin.sui for peng.zhao ??????????????????????????????????????????????????????????????????????????????
                            dataRecordValidateResult.getHmeNcDisposePlatformDTO().setWorkcellId(dto.getWorkcellId());
                            dataRecordValidateResult.getHmeNcDisposePlatformDTO().setCurrentwWorkcellId(dto.getWorkcellId());
                            dataRecordValidateResult.getHmeNcDisposePlatformDTO().setFlag("2");
                            processNcDetailMap.put(eoJobSn.getJobId() , dataRecordValidateResult.getProcessNcDetailList());
                            if(errorMessages.length() == 0) {
                                errorMessages.append(dataRecordValidateResult.getErrorMessage());
                            }else{
                                errorMessages.append("<br>" + dataRecordValidateResult.getErrorMessage());
                            }
                            if(index == hmeEoJobSnVO16.getHmeEoJobSnEntityList().size() - 1) {
                                List<HmeProcessNcDetailVO2> processNcDetailList = new ArrayList<>();
                                processNcDetailMap.forEach((key, value) -> {
                                    value.forEach(item -> {
                                        HmeProcessNcDetailVO2 processNcDetail = new HmeProcessNcDetailVO2();
                                        BeanUtils.copyProperties(item , processNcDetail);
                                        processNcDetail.setJobId(key);
                                        processNcDetailList.add(processNcDetail);
                                    });
                                });
                                dataRecordValidateResult.setProcessNcDetailList(processNcDetailList);
                                dataRecordValidateResult.setErrorMessage(errorMessages.toString());
                                return dataRecordValidateResult;
                            }
                        }
                    }
                } else if (hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId())) {
                    //????????????
                    //??????????????????????????????
                    List<String> materialIdList = materialLotMap.values().stream()
                            .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());
                    Map<String, HmeProcessNcHeaderVO2> processNcInfoMap = hmeProcessNcHeaderRepository
                            .batchQueryProcessNcInfoForNcRecordValidate(tenantId, dto.getOperationId(), materialIdList);

                    //?????????????????????????????????jobId??????
                    Map<String, List<HmeEoJobDataRecord>> eoJobDataRecordMap = allEoJobDataRecordList
                            .stream().collect(Collectors.groupingBy(HmeEoJobDataRecord::getJobId));

                    //?????????????????????????????????
                    for (HmeEoJobSn eoJobSn : hmeEoJobSnVO16.getHmeEoJobSnEntityList()) {
                        index++;
                        if (!eoJobDataRecordMap.containsKey(eoJobSn.getJobId())) {
                            continue;
                        }

                        //???????????????????????????cos??????
                        HmeMaterialLotVO3 materialLot = materialLotMap.get(eoJobSn.getMaterialLotId());
                        String materialLotCode = materialLot.getMaterialLotCode();
                        String key = materialLot.getMaterialId() + "," + materialLotCode.substring(2, 4) + "," + materialLotCode.substring(4, 5) + "," + materialLotCode.substring(5, 6);
                        HmeProcessNcHeaderVO2 processNcInfo = processNcInfoMap.getOrDefault(key , null);
                        if(Objects.isNull(processNcInfo)){
                            key = materialLot.getMaterialId() + "," + materialLotCode.substring(2, 4) + "," + materialLotCode.substring(4, 5) + ",";
                            processNcInfo = processNcInfoMap.get(key);
                        }

                        //????????????????????????????????????????????????
                        List<HmeEoJobDataRecord> currEoJobDataRecordList = eoJobDataRecordMap.get(eoJobSn.getJobId());
                        HmeEoJobSn dataRecordValidateResult = hmeEoJobSnCommonService
                                .dataRecordProcessNcValidate(tenantId, materialLotCode, currEoJobDataRecordList, processNcInfo);
                        if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {

                            //V20210408 modify by penglin.sui for peng.zhao ??????????????????????????????????????????????????????????????????????????????
                            dataRecordValidateResult.getHmeNcDisposePlatformDTO().setWorkcellId(dto.getWorkcellId());
                            dataRecordValidateResult.getHmeNcDisposePlatformDTO().setCurrentwWorkcellId(dto.getWorkcellId());
                            dataRecordValidateResult.getHmeNcDisposePlatformDTO().setFlag("2");
                            processNcDetailMap.put(eoJobSn.getJobId() , dataRecordValidateResult.getProcessNcDetailList());
                            if(errorMessages.length() == 0) {
                                errorMessages.append(dataRecordValidateResult.getErrorMessage());
                            }else{
                                errorMessages.append("<br>" + dataRecordValidateResult.getErrorMessage());
                            }
                            if(index == hmeEoJobSnVO16.getHmeEoJobSnEntityList().size() - 1) {
                                List<HmeProcessNcDetailVO2> processNcDetailList = new ArrayList<>();
                                processNcDetailMap.forEach((key2, value) -> {
                                    value.forEach(item -> {
                                        HmeProcessNcDetailVO2 processNcDetail = new HmeProcessNcDetailVO2();
                                        BeanUtils.copyProperties(item , processNcDetail);
                                        processNcDetail.setJobId(key2);
                                        processNcDetailList.add(processNcDetail);
                                    });
                                });
                                dataRecordValidateResult.setProcessNcDetailList(processNcDetailList);
                                dataRecordValidateResult.setErrorMessage(errorMessages.toString());
                                return dataRecordValidateResult;
                            }
                        }
                    }
                }
            }
        }

        //??????????????????????????????????????????????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.9.12
        if (StringUtils.isNotBlank(dto.getContainerId())) {
            MtContLoadDtlVO10 contLoadDtlParam = new MtContLoadDtlVO10();
            contLoadDtlParam.setContainerId(dto.getContainerId());
            contLoadDtlParam.setAllLevelFlag(HmeConstants.ConstantValue.NO);
            startDate = System.currentTimeMillis();
            List<MtContLoadDtlVO4> mtContLoadDtlVo1List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlParam);
            log.info("=================================>????????????????????????-????????????containerLimitMaterialLotQuery????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if (CollectionUtils.isNotEmpty(mtContLoadDtlVo1List)) {
                List<String> materialLotIds = mtContLoadDtlVo1List.stream().filter(t -> t.getMaterialLotId() != null)
                        .map(MtContLoadDtlVO4::getMaterialLotId).distinct().collect(Collectors.toList());

                if (hmeEoJobSnCommonService.hasOtherOperationJob(tenantId, dto.getOperationId(), materialLotIds)) {
                    // ?????????????????????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_058", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_058", "HME"));
                }
            }
        }

        //???????????????BOM ture????????????????????????????????????????????????????????? add by yuchao.wang for peng.zhao at 2021.1.29
        if (!hmeEoJobSnVO16.getOperationBomFlag()) {
            //???????????????????????????????????????????????????
            if (CollectionUtils.isNotEmpty(hmeEoJobSnVO16.getBackFlushBomComponentList())) {
                List<String> backFlushBomComponentIdList = hmeEoJobSnVO16.getBackFlushBomComponentList().stream()
                        .map(MtBomComponent::getBomComponentId).collect(Collectors.toList());
                startDate = System.currentTimeMillis();
                List<MtMaterial> virtualMtMaterialList = hmeEoJobSnLotMaterialMapper.selectVirtualMaterial(tenantId, backFlushBomComponentIdList);
                log.info("=================================>????????????????????????-?????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                if (CollectionUtils.isNotEmpty(virtualMtMaterialList)) {
                    List<String> virtualMaterialCodeList = virtualMtMaterialList.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
                    String materialCodes = String.join(",", virtualMaterialCodeList);
                    //????????????${1}?????????????????????,?????????
                    throw new MtException("HME_EO_JOB_SN_080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_080", "HME", materialCodes));
                }

                //??????BOM??????????????????????????????BOM????????????
                ArrayList<HmeEoVO4> hmeEoVO4List = new ArrayList<>(hmeEoJobSnVO16.getEoMap().values());
                Double bomQty = hmeEoVO4List.get(0).getBomPrimaryQty();
                if (Objects.isNull(bomQty)) {
                    //${1}????????? ?????????${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "BOM", hmeEoVO4List.get(0).getBomId()));
                }
                BigDecimal bomPrimaryQty = BigDecimal.valueOf(bomQty);
                if (bomPrimaryQty.compareTo(BigDecimal.ZERO) == 0) {
                    //???????????????0,?????????
                    throw new MtException("HME_EO_JOB_SN_082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_082", "HME"));
                }
            }

            //????????????????????????
            startDate = System.currentTimeMillis();
            outSiteReleaseValidate(tenantId, hmeEoJobSnVO16, dto);
            log.info("=================================>????????????????????????-????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }

        //????????????????????????
        if (hmeEoJobSnVO16.getContainerOutSiteFlag()) {
            startDate = System.currentTimeMillis();
            outSiteContainerLoadBatchVerify(tenantId, hmeEoJobSnVO16, dto);
            log.info("=================================>????????????????????????-????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }
        return null;
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/3 15:06
     * @param tenantId ??????ID
     * @param hmeEoJobSnVO16 ??????
     * @param dto ??????
     * @return void
     *
     */
    private void outSiteContainerLoadBatchVerify(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        String containerId = hmeEoJobSnVO16.getEoJobContainer().getContainerId();

        //????????????????????????
        Long startDate = System.currentTimeMillis();
        HmeContainerVO containerInfo = hmeEoJobSnMapper.queryContainerInfo(tenantId, containerId);
        log.info("=================================>????????????????????????-????????????????????????????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if (Objects.isNull(containerInfo) || StringUtils.isBlank(containerInfo.getContainerId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //?????????????????????,??????????????????????????????
        if (!HmeConstants.StatusCode.CANRELEASE.equals(containerInfo.getStatus())) {
            throw new MtException("HME_LOAD_CONTAINER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_002", "HME", containerInfo.getContainerCode()));
        }

        //?????????????????????????????????????????????????????????????????????
        //????????????????????????,?????????????????????????????????
        double totalQty = 0;
        if (Objects.nonNull(containerInfo.getCapacityQty())) {
            totalQty = hmeEoJobSnVO16.getEoMap().values().stream().mapToDouble(MtEo::getQty).sum();
            if (totalQty > containerInfo.getCapacityQty()) {
                //??????????????????
                throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //????????????????????????????????????
        String materialId = "";
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedMaterialFlag())) {
            List<String> materialIdList = hmeEoJobSnVO16.getMaterialLotMap().values().stream()
                    .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());
            if (materialIdList.size() != 1) {
                //???????????????????????????
                throw new MtException("MT_MATERIAL_LOT_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0045", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
            materialId = materialIdList.get(0);
        }

        //??????????????????????????????EO
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedEoFlag())
                && hmeEoJobSnVO16.getEoMap().size() != 1) {
            //?????????????????????EO
            throw new MtException("MT_MATERIAL_LOT_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0044", "MATERIAL_LOT", containerInfo.getContainerCode()));
        }
        String eoId = dto.getSnLineList().get(0).getEoId();

        //??????????????????????????????WO
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedWoFlag())
                && hmeEoJobSnVO16.getWoMap().size() != 1) {
            //?????????????????????WO
            throw new MtException("MT_MATERIAL_LOT_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0047", "MATERIAL_LOT", containerInfo.getContainerCode()));
        }
        String woId = hmeEoJobSnVO16.getEoMap().get(eoId).getWorkOrderId();

        //??????????????????????????????
        startDate = System.currentTimeMillis();
        List<HmeContainerDetailVO> containerDetailList = hmeEoJobSnMapper.queryContainerDetails(tenantId, containerId);
        log.info("=================================>????????????????????????-????????????????????????????????????-??????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if (CollectionUtils.isEmpty(containerDetailList)) {
            return;
        }

        //????????????????????????,?????????????????????????????????
        if (Objects.nonNull(containerInfo.getCapacityQty())) {
            totalQty += containerDetailList.stream().filter(item -> Objects.nonNull(item.getPrimaryUomQty()))
                    .mapToDouble(HmeContainerDetailVO::getPrimaryUomQty).sum();
            if (totalQty > containerInfo.getCapacityQty()) {
                //??????????????????
                throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //????????????????????????????????????
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedMaterialFlag())) {
            List<String> materialIdList = containerDetailList.stream().map(HmeContainerDetailVO::getMaterialId)
                        .distinct().collect(Collectors.toList());

            if (materialIdList.size() != 1 || !materialIdList.contains(materialId)) {
                //???????????????????????????
                throw new MtException("MT_MATERIAL_LOT_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0045", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //??????????????????????????????EO
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedEoFlag())) {
            List<String> eoIdList = containerDetailList.stream().map(HmeContainerDetailVO::getEoId)
                    .distinct().collect(Collectors.toList());

            if (eoIdList.size() != 1 || !eoIdList.contains(eoId)) {
                //?????????????????????EO
                throw new MtException("MT_MATERIAL_LOT_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0044", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //??????????????????????????????WO
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedWoFlag())) {
            List<String> woIdList = containerDetailList.stream().map(HmeContainerDetailVO::getWorkOrderId)
                    .distinct().collect(Collectors.toList());

            if (woIdList.size() != 1 || !woIdList.contains(woId)) {
                //?????????????????????WO
                throw new MtException("MT_MATERIAL_LOT_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0047", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/25 17:00
     * @param tenantId ??????ID
     * @param hmeEoJobSnVO16 ??????
     * @return void
     *
     */
    private void outSiteReleaseValidate(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        //????????????,????????????????????????????????????
        List<LovValueDTO> inputWoTypeValues = lovAdapter.queryLovValue("HME.INPUT_WO_TYPE", tenantId);
        if (CollectionUtils.isEmpty(inputWoTypeValues)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Return:??????HME.INPUT_WO_TYPE??????");
            return;
        }
        List<String> valueList = inputWoTypeValues.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        //??????????????????????????????,??????????????????????????????,????????????????????????Y ????????????????????????
        List<String> validateWoIdList = hmeEoJobSnVO16.getWoMap().values().stream()
                .filter(item -> valueList.contains(item.getWorkOrderType()) && !HmeConstants.ConstantValue.YES.equals(item.getIssuedFlag()))
                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(validateWoIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Return:??????????????????????????? valueList={}", valueList);
            return;
        }

        //?????????????????????EO
        List<HmeEoVO4> validateEoList = hmeEoJobSnVO16.getEoMap().values().stream()
                .filter(item -> validateWoIdList.contains(item.getWorkOrderId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(validateEoList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Return:?????????????????????EO validateWoIdList={}", validateWoIdList);
            return;
        }

        //?????????????????????ID
        List<String> backFlushBomComponentIdList = hmeEoJobSnVO16.getBackFlushBomComponentList()
                .stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList());

        //??????EO?????????????????????,??????(??????????????????Y && ???????????????) mainComponentMap key:eoId,???????????????ID value:???????????????
        Map<String, HmeBomComponentVO> mainComponentMap = new HashMap<>();
        List<String> mainComponentIdList = new ArrayList<>();
        validateEoList.forEach(eo -> {
            if (CollectionUtils.isNotEmpty(eo.getBomComponentList())) {
                eo.getBomComponentList().forEach(component -> {
                    if (!backFlushBomComponentIdList.contains(component.getBomComponentId())
                            && !HmeConstants.ConstantValue.YES.equals(component.getIssuedFlag())
                            && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(component.getVirtualFlag())) {
                        //??????????????????ID
                        if (!mainComponentIdList.contains(component.getBomComponentId())) {
                            mainComponentIdList.add(component.getBomComponentId());
                        }
                        //??????EO-????????????
                        mainComponentMap.put(eo.getEoId() + "," + component.getBomComponentId(), component);
                    }
                });
            }
        });
        if (CollectionUtils.isEmpty(mainComponentIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Return:?????????????????????EO?????? validateEoList={}", validateEoList);
            return;
        }
        Long startDate = System.currentTimeMillis();
        //????????????????????????????????? substituteMaterialComponentMap key:???????????????ID value:????????????????????????ID
        Map<String, List<String>> substituteMaterialComponentMap = new HashMap<>();
        String eoStepId = hmeEoJobSnVO16.getHmeEoJobSnEntityList().get(0).getEoStepId();
        List<HmeBomComponentVO> substituteMaterialComponentList = hmeEoJobSnMapper.batchQuerySubstituteMaterialComponent(tenantId, eoStepId, mainComponentIdList);
        log.info("=================================>????????????????????????-????????????????????????????????????-?????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate ????????????????????????{}???,eoStepId={},mainComponentIdList={}",
                substituteMaterialComponentList.size(), eoStepId, mainComponentIdList);
        if (CollectionUtils.isNotEmpty(substituteMaterialComponentList)) {
            substituteMaterialComponentList.forEach(item -> {
                if (substituteMaterialComponentMap.containsKey(item.getMainBomComponentId())) {
                    substituteMaterialComponentMap.get(item.getMainBomComponentId()).add(item.getBomComponentId());
                } else {
                    List<String> substituteIdList = new ArrayList<>();
                    substituteIdList.add(item.getBomComponentId());
                    substituteMaterialComponentMap.put(item.getMainBomComponentId(), substituteIdList);
                }
            });
        }

        //??????????????????????????????????????????
        List<String> validateEoIdList = validateEoList.stream().map(MtEo::getEoId).collect(Collectors.toList());
        List<String> allComponentIdList = new ArrayList<>(mainComponentIdList);
        if (CollectionUtils.isNotEmpty(substituteMaterialComponentList)) {
            List<String> substituteMaterialComponentIdList = substituteMaterialComponentList.stream()
                    .map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());
            allComponentIdList.addAll(substituteMaterialComponentIdList);
        }
        startDate = System.currentTimeMillis();
        SecurityTokenHelper.close();
        List<MtEoComponentActual> eoComponentActualList = mtEoComponentActualRepository.selectByCondition(Condition.builder(MtEoComponentActual.class)
                .select(MtEoComponentActual.FIELD_EO_ID, MtEoComponentActual.FIELD_BOM_COMPONENT_ID, MtEoComponentActual.FIELD_ASSEMBLE_QTY)
                .andWhere(Sqls.custom().andEqualTo(MtEoComponentActual.FIELD_TENANT_ID, tenantId)
                        .andIn(MtEoComponentActual.FIELD_EO_ID, validateEoIdList)
                        .andIn(MtEoComponentActual.FIELD_BOM_COMPONENT_ID, allComponentIdList)).build());
        log.info("=================================>????????????????????????-????????????????????????????????????-mtEoComponentActualRepository.selectByCondition????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //?????????????????????????????????????????????
        List<String> mainComponentMaterialIdList = mainComponentMap.values().stream()
                .map(HmeBomComponentVO::getBomComponentMaterialId).distinct().collect(Collectors.toList());
        startDate = System.currentTimeMillis();
        List<HmeEoComponentActualVO> eoGlobalMaterialActualList = hmeEoJobSnMapper.batchQueryEoActualForGlobalMaterial(tenantId,
                dto.getOperationId(), eoStepId, mainComponentMaterialIdList, validateEoIdList);
        log.info("=================================>????????????????????????-????????????????????????????????????-?????????????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //????????????????????????????????????????????? ?????????????????? <= ????????????????????????????????????
        for (Map.Entry<String, HmeBomComponentVO> mainComponentEntry : mainComponentMap.entrySet()) {
            //????????????eoId componentId
            String[] keySplit = mainComponentEntry.getKey().split(",");
            String eoId = keySplit[0];
            String componentId = keySplit.length == 2 ? keySplit[1] : "";

            HmeBomComponentVO mainComponent = mainComponentEntry.getValue();

            //??????????????????????????????????????????ID
            List<String> currComponentIdList = new ArrayList<>();
            currComponentIdList.add(componentId);
            if (substituteMaterialComponentMap.containsKey(componentId)) {
                currComponentIdList.addAll(substituteMaterialComponentMap.get(componentId));
            }

            //????????????????????????????????????????????????
            double sumComponentActual = eoComponentActualList.stream()
                    .filter(item -> eoId.equals(item.getEoId()) && currComponentIdList.contains(item.getBomComponentId())
                            && Objects.nonNull(item.getAssembleQty())).mapToDouble(MtEoComponentActual::getAssembleQty).sum();

            //????????????????????????????????????
            List<HmeEoComponentActualVO> currEoGlobalMaterialActualList = eoGlobalMaterialActualList.stream()
                    .filter(item -> eoId.equals(item.getEoId()) && item.getMainMaterialId().equals(mainComponent.getBomComponentMaterialId())
                    && Objects.nonNull(item.getAssembleQty())).collect(Collectors.toList());

            //??????????????????????????????????????????
            if (currEoGlobalMaterialActualList.stream().map(HmeEoComponentActualVO::getSubstituteGroup).distinct().count() > 1) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mainComponent.getBomComponentMaterialId());
                //????????????{$1}??????????????????????????????,?????????
                throw new MtException("HME_EO_JOB_SN_167", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_167", "HME", mtMaterial.getMaterialCode()));
            }

            //?????????????????????????????????????????????????????????
            double sumGlobalMaterialActual = currEoGlobalMaterialActualList.stream().mapToDouble(HmeEoComponentActualVO::getAssembleQty).sum();

            if (mainComponent.getBomComponentQty() > (sumComponentActual + sumGlobalMaterialActual)) {
                log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Exception:BomComponentQty={},sumComponentActual={},sumGlobalMaterialActual={}",
                        mainComponent.getBomComponentQty(), sumComponentActual, sumGlobalMaterialActual);
                log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Exception:eoId={},mainComponentId={},currComponentIdList={}",
                        eoId, componentId, currComponentIdList);

                //??????eo?????????sn
                Optional<HmeEoJobSnVO3> firstSn = dto.getSnLineList().stream().filter(item -> eoId.equals(item.getEoId())).findFirst();
                String snNum = "";
                if (firstSn.isPresent()) {
                    snNum = firstSn.get().getSnNum();
                }

                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mainComponent.getBomComponentMaterialId());
                //SN???${1}??????????????????${2}??????????????????????????????,????????????
                throw new MtException("HME_EO_JOB_SN_139", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_139", "HME", snNum, mtMaterial.getMaterialCode()));
            }
        }
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/5 14:11
     * @param tenantId ??????ID
     * @param dto ??????
     * @return void
     *
     */
    private void paramsVerificationForOutSite(Long tenantId, HmeEoJobSnVO3 dto) {
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????ID"));
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????ID"));
        }
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????ID"));
        }
        if (StringUtils.isBlank(dto.getWkcShiftId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????ID"));
        }
        if (StringUtils.isBlank(dto.getJobType())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "????????????"));
        }
        if (CollectionUtils.isEmpty(dto.getSnLineList())) {
            throw new MtException("HME_EO_JOB_SN_018",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_018", "HME"));
        }
    }
}