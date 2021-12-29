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
 * @Description 批量作业平台-SN作业应用服务
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
     * @Description 批量作业平台出站
     *
     * @author yuchao.wang
     * @date 2020/11/17 16:49
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSn> outSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteScan.Start tenantId={},dto={}", tenantId, dto);
        //非空校验
        paramsVerificationForOutSite(tenantId, dto);

        //获取所有eoId jobId
        List<String> eoIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
        List<String> jobIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct().collect(Collectors.toList());

        //查询后续用到的数据
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO16 hmeEoJobSnVO16 = queryBasicData(tenantId, dto, eoIdList, jobIdList);
        log.info("=================================>批量工序作业平台-查询后续用到的数据总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

        //出站校验,如果校验结果不为空返回前台做确认操作
        startDate = System.currentTimeMillis();
        HmeEoJobSn validateResult = outSiteValidate(tenantId, jobIdList, hmeEoJobSnVO16, dto);
        if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
            return Collections.singletonList(validateResult);
        }
        log.info("=================================>批量工序作业平台-出站校验查询后续用到的数据总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //工艺是否卡BOM ture时出站跳过投料齐套校验和反冲料扣减逻辑 add by yuchao.wang for peng.zhao at 2021.1.29
        if (!hmeEoJobSnVO16.getOperationBomFlag()) {
            //扣减反冲料,目前批量不支持返修
            self().batchBackFlushMaterialOutSite(tenantId, hmeEoJobSnVO16, dto);
        }

        //批量执行订单完成
        startDate = System.currentTimeMillis();
        hmeEoJobSnCommonService.siteOutBatchComplete(tenantId, dto.getWorkcellId(), eoIdList, hmeEoJobSnVO16.getEoMap());
        log.info("=================================>批量工序作业平台-出站批量执行订单完成总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //调用出站通用主方法
        startDate = System.currentTimeMillis();
        List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.batchMainOutSite(tenantId, dto, hmeEoJobSnVO16);
        log.info("=================================>批量工序作业平台-出站调用出站通用主方法总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //是否容器出站逻辑
        if (hmeEoJobSnVO16.getContainerOutSiteFlag()) {
            startDate = System.currentTimeMillis();
            hmeEoJobSnCommonService.batchContainerOutSite(tenantId, hmeEoJobSnVO16, dto);
            log.info("=================================>批量工序作业平台-出站是否容器出站逻辑总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        //批量更新出站
        startDate = System.currentTimeMillis();
        hmeEoJobSnRepository.batchOutSite2(tenantId, hmeEoJobSnVO16.getUserId(), jobIdList, dto.getRemark());
        log.info("=================================>批量工序作业平台-出站批量更新出站总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //hmeEoJobSnRepository.batchOutSiteWithMaterialLot(tenantId, hmeEoJobSnVO16.getUserId(), dto.getSnLineList());

        //保存设备数据
        startDate = System.currentTimeMillis();
        hmeEoJobEquipmentRepository.batchSaveEquipmentRecordForOutSite(tenantId, dto);
        log.info("=================================>批量工序作业平台-出站保存设备数据总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //完工数据统计
        startDate = System.currentTimeMillis();
        hmeEoJobSnCommonService.batchWkcCompleteOutputRecord(tenantId, dto, hmeEoJobSnVO16);
        log.info("=================================>批量工序作业平台-出站完工数据统计总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //发送实时接口
        startDate = System.currentTimeMillis();
        hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
        log.info("=================================>批量工序作业平台-出站发送实时接口总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //构造返回数据
        startDate = System.currentTimeMillis();
        List<HmeEoJobSn> resultList = getResultData(tenantId, hmeEoJobSnVO16, dto);
        log.info("=================================>批量工序作业平台-出站构造返回数据总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
        return resultList;
    }

    /**
     *
     * @Description 反冲料投料
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:10
     * @param tenantId 租户ID
     * @param hmeEoJobSnVO16 参数
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchBackFlushMaterialOutSite(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite tenantId={},hmeEoJobSnVO16={},dto={}", tenantId, hmeEoJobSnVO16, dto);
        //如果没有反冲料组件直接返回即可
        if (CollectionUtils.isEmpty(hmeEoJobSnVO16.getBackFlushBomComponentList())) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:EO没有反冲料组件");
            return;
        }

        Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO16.getEoMap();
        Map<String, HmeWorkOrderVO2> woMap = hmeEoJobSnVO16.getWoMap();

        //过滤需要投料的工单对应的作业,产线扩展字段，为Y不做反冲料投料
        List<String> backFlushWoIdList = woMap.values().stream()
                .filter(item -> !HmeConstants.ConstantValue.YES.equals(item.getBackflushNotFlag()))
                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(backFlushWoIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:工单对应的BackflushNotFlag为Y wo={}", woMap.values());
            return;
        }
        //同时筛选eo组件不为空的作业
        List<String> backFlushEoIdList = eoMap.values().stream()
                .filter(item -> backFlushWoIdList.contains(item.getWorkOrderId())
                        && CollectionUtils.isNotEmpty(item.getBomComponentList()))
                .map(MtEo::getEoId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(backFlushEoIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:工单下没有符合条件的EO backFlushWoIdList={}", backFlushWoIdList);
            return;
        }
        List<HmeEoJobSnVO3> snLineList = dto.getSnLineList().stream().filter(item ->
                backFlushEoIdList.contains(item.getEoId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(snLineList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:没有找到EO对应的作业 backFlushEoIdList={}", backFlushEoIdList);
            return;
        }

        //获取所有反冲组件ID
        List<String> backFlushBomComponentIdList = hmeEoJobSnVO16.getBackFlushBomComponentList().stream()
                .map(MtBomComponent::getBomComponentId).collect(Collectors.toList());

        //查询所有要投料的EO组件
        List<HmeBomComponentVO> allReleaseEoComponentList = new ArrayList<>();
        List<String> releaseEoComponentIdList = new ArrayList<>();
        Map<String, List<String>> componentEoMap = new HashMap<>();
        for (HmeEoVO4 item : eoMap.values()) {
            //第一层筛选要投料的wo对应的EO
            if (backFlushEoIdList.contains(item.getEoId())
                    && CollectionUtils.isNotEmpty(item.getBomComponentList())) {
                for (HmeBomComponentVO eoComponent : item.getBomComponentList()) {
                    //第二层筛选是反冲料组件的BomComponent
                    if (backFlushBomComponentIdList.contains(eoComponent.getBomComponentId())) {
                        //添加要投料的组件
                        if (!releaseEoComponentIdList.contains(eoComponent.getBomComponentId())) {
                            allReleaseEoComponentList.add(eoComponent);
                            releaseEoComponentIdList.add(eoComponent.getBomComponentId());

                            //同时将每个组件id对应的eoId组装起来
                            List<String> eoIdList = new ArrayList<>();
                            eoIdList.add(item.getEoId());
                            componentEoMap.put(eoComponent.getBomComponentId(), eoIdList);
                        } else {
                            //同时将每个组件id对应的eoId组装起来
                            componentEoMap.get(eoComponent.getBomComponentId()).add(item.getEoId());
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(releaseEoComponentIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite Return:没有找到要投料的EO组件 backFlushBomComponentIdList={}", backFlushBomComponentIdList);
            return;
        }

        //获取BOM数量，当前已限制相同BOM出站投料
        List<String> tempEoIdList = componentEoMap.get(allReleaseEoComponentList.get(0).getBomComponentId());
        HmeEoVO4 tempEo = eoMap.get(tempEoIdList.get(0));
        if (Objects.isNull(tempEo.getBomPrimaryQty())) {
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "BOM", tempEo.getBomId()));
        }
        BigDecimal bomPrimaryQty = BigDecimal.valueOf(tempEo.getBomPrimaryQty());
        if(bomPrimaryQty.compareTo(BigDecimal.ZERO) == 0){
            //基本数量为0,请确认
            throw new MtException("HME_EO_JOB_SN_082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_082", "HME"));
        }

        //获取所有反冲料ID
        List<String> backFlushMaterialIdList = hmeEoJobSnVO16.getBackFlushBomComponentList().stream()
                .filter(item -> releaseEoComponentIdList.contains(item.getBomComponentId()))
                .map(MtBomComponent::getMaterialId).collect(Collectors.toList());

        //查询反冲货位
        MtModLocator backFlushLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId,HmeConstants.LocaltorType.BACKFLUSH,dto.getWorkcellId());

        List<HmeMaterialLotVO3> backFlushMaterialLotList = hmeEoJobSnLotMaterialMapper
                .batchQueryBackFlushMaterialLot(tenantId, backFlushMaterialIdList, backFlushLocator.getLocatorId());
        if (CollectionUtils.isEmpty(backFlushMaterialLotList)) {
            List<MtMaterialVO> mtMaterialVOList = mtMaterialRepository.materialPropertyBatchGet(tenantId, backFlushMaterialIdList);
            List<String> backFlushMaterialCodeList = mtMaterialVOList.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
            //当前反冲库位下反冲物料【{1}】现有量不足
            throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_087", "HME" , String.join(",", backFlushMaterialCodeList)));
        }

        //查询反冲库位
        MtModLocator warehouse =  hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,backFlushLocator.getLocatorId());

        //查询开班实绩编码
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());

        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //查询事务类型
        String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
        WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
        wmsTransactionTypePara.setTenantId(tenantId);
        wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);

        //查询所有的WO组件装配实绩
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

        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite 待投料组件数[{}]", allReleaseEoComponentList.size());

        //对于所有的组件进行投料
        Map<String, MtAssembleProcessActualVO17> eoAssembleProcessActualMap = new HashMap<>();
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnVO16.getMaterialLotMap();
        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList = new ArrayList<>();
        //条码批量消耗API参数
        List<MtMaterialLotVO32> mtMaterialList = new ArrayList<>();
        for (HmeBomComponentVO releaseEoComponent : allReleaseEoComponentList) {
            //筛选当前组件下要投的反冲条码
            List<HmeMaterialLotVO3> releaseMaterialLotList = backFlushMaterialLotList.stream()
                    .filter(item -> releaseEoComponent.getBomComponentMaterialId().equals(item.getMaterialId()))
                    .sorted(Comparator.comparing(HmeMaterialLotVO3::getCreationDate)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(releaseMaterialLotList)) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(releaseEoComponent.getBomComponentMaterialId());
                //当前反冲库位下反冲物料【{1}】现有量不足
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            //获取当前组件对应的EO/WO/作业
            List<String> componentEoIdList = componentEoMap.get(releaseEoComponent.getBomComponentId());
            List<HmeEoVO4> currentEoList = new ArrayList<>();
            componentEoIdList.forEach(eoId -> currentEoList.add(eoMap.get(eoId)));
            HmeWorkOrderVO2 currentWo = woMap.get(currentEoList.get(0).getWorkOrderId());
            List<HmeEoJobSnVO3> currentSnLineList = snLineList.stream()
                    .filter(item -> componentEoIdList.contains(item.getEoId())).collect(Collectors.toList());

            //获取当前组件对应的需求数量 key:eoId value:remainQty
            Map<String, BigDecimal> currentEoRemainQtyMap = new HashMap<>();
            BigDecimal releaseEoBomComponentQty = BigDecimal.valueOf(releaseEoComponent.getBomComponentQty());
            currentSnLineList.forEach(item -> {
                BigDecimal primaryUomQty = BigDecimal.valueOf(materialLotMap.get(item.getMaterialLotId()).getPrimaryUomQty());
                BigDecimal remain = releaseEoBomComponentQty.multiply(primaryUomQty).divide(bomPrimaryQty, 4 , BigDecimal.ROUND_HALF_UP);

                currentEoRemainQtyMap.put(item.getEoId(), remain);
            });

            //筛选wo组件中与当前eo组件相同物料行号的
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
                log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite continue:没有找到 wo组件中与当前eo组件相同物料行号的 releaseEoComponent={}", releaseEoComponent);
                continue;
            }

            //查询当前工单组件剩余数量，如果小于等于0不投
            BigDecimal woDemandQty = StringUtils.isBlank(releaseWoComponent.getWoBomComponentDemandQty())
                    ? BigDecimal.ZERO : new BigDecimal(releaseWoComponent.getWoBomComponentDemandQty());
            if(woDemandQty.compareTo(BigDecimal.ZERO) <= 0){
                log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite continue:前工单组件剩余数量为0 releaseWoComponent={}", releaseWoComponent);
                continue;
            }

            //计算所有剩余要投数量
            BigDecimal remainQty = BigDecimal.ZERO;
            for (BigDecimal remain : currentEoRemainQtyMap.values()) {
                remainQty = remainQty.add(remain);
            }

            //校验WO组件装配实绩数量
            if (woComponentActualMap.containsKey(currentWo.getWorkOrderId() + "," + releaseWoComponent.getBomComponentId())) {
                double assembleQty = woComponentActualMap.get(currentWo.getWorkOrderId() + "," + releaseWoComponent.getBomComponentId())
                        .stream().mapToDouble(MtWorkOrderComponentActual::getAssembleQty).sum();
                if ((BigDecimal.valueOf(assembleQty).add(remainQty)).compareTo(woDemandQty) > 0) {
                    log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite continue:校验WO组件装配实绩数量未通过" +
                            " releaseWoComponent={}, assembleQty={}, remainQty={}", releaseWoComponent, assembleQty, remainQty);
                    continue;
                }
            }

            //校验条码数量是否足够
            double releaseMaterialLotPrimaryQtySum = releaseMaterialLotList.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
            if(BigDecimal.valueOf(releaseMaterialLotPrimaryQtySum).compareTo(remainQty) < 0){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(releaseEoComponent.getBomComponentMaterialId());
                //当前反冲库位下反冲物料【{1}】现有量不足
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.batchBackFlushMaterialOutSite 组ID[{}],需投料数量[{}]", releaseWoComponent.getBomComponentId(), remainQty);

            //API-物料批消耗参数 key:MaterialLotId
//            Map<String, MtMaterialLotVO1> materialLotConsumeMap = new HashMap<>();
            //记录事务明细参数 key:MaterialLotId
            Map<String, WmsObjectTransactionRequestVO> objectTransactionMap = new HashMap<>();

            Long sequence = 0L;
            BigDecimal trxPrimaryUomQty = BigDecimal.ZERO;
            List<MtMaterialLotVO33> mtMaterialLotVO33List = new ArrayList<>();

            //循环所有EO计算要投料的情况
            for (HmeEoVO4 currEo : currentEoList) {
                //当前EO要投数量
                BigDecimal currRemainQty = currentEoRemainQtyMap.get(currEo.getEoId());

                //找到当前EO对应的作业
                Optional<HmeEoJobSnVO3> currSn = currentSnLineList.stream().filter(item -> currEo.getEoId().equals(item.getEoId())).findFirst();

                //构造API-批量装配参数
                List<MtAssembleProcessActualVO11> materialInfo = new ArrayList<>();
                if (eoAssembleProcessActualMap.containsKey(currEo.getEoId())) {
                    //获取当前EO对应的物料装配信息
                    materialInfo = eoAssembleProcessActualMap.get(currEo.getEoId()).getMaterialInfo();
                }

                //循环所有条码投料
                for (HmeMaterialLotVO3 releaseMaterialLot : releaseMaterialLotList) {
                    //如果条码已经全部投完，直接看下一个条码
                    if (releaseMaterialLot.isReleasedAll()) {
                        continue;
                    }

                    //计算当前条码剩余可投数量,如果为空则为条码当前数量
                    if (Objects.isNull(releaseMaterialLot.getRemainPrimaryUomQty())) {
                        releaseMaterialLot.setRemainPrimaryUomQty(BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty()));
                    }

                    //计算当前条码要投数量
                    BigDecimal currentQty = currRemainQty;
                    if (currRemainQty.compareTo(releaseMaterialLot.getRemainPrimaryUomQty()) >= 0) {
                        //条码数量不足，先投完当前条码
                        currentQty = releaseMaterialLot.getRemainPrimaryUomQty();

                        //同时更新当前条码的 剩余可投数量,投完标识
                        releaseMaterialLot.setRemainPrimaryUomQty(BigDecimal.ZERO);
                        releaseMaterialLot.setReleasedAll(true);
                    } else {
                        //条码数量充足 只更新 剩余可投数量=条码当前剩余可投数量-当前投料数量
                        releaseMaterialLot.setRemainPrimaryUomQty(releaseMaterialLot.getRemainPrimaryUomQty().subtract(currentQty));
                    }
                    currRemainQty = currRemainQty.subtract(currentQty);

                    //构造API-物料批消耗参数
//                    if (materialLotConsumeMap.containsKey(releaseMaterialLot.getMaterialLotId())) {
//                        //更新消耗数量
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

                    //物料批批量消耗
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

                    //构造当前条码装配信息
                    MtAssembleProcessActualVO11 assembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                    assembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                    assembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                    assembleProcessActualVO11.setBomComponentId(releaseEoComponent.getBomComponentId());
                    assembleProcessActualVO11.setMaterialId(releaseMaterialLot.getMaterialId());
                    assembleProcessActualVO11.setTrxAssembleQty(currentQty.doubleValue());
                    assembleProcessActualVO11.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                    assembleProcessActualVO11.setLocatorId(releaseMaterialLot.getLocatorId());
                    materialInfo.add(assembleProcessActualVO11);

                    //构造事务明细参数
                    if (objectTransactionMap.containsKey(releaseMaterialLot.getMaterialLotId())) {
                        //更新事务数量
                        WmsObjectTransactionRequestVO objectTransactionVO = objectTransactionMap.get(releaseMaterialLot.getMaterialLotId());
                        BigDecimal transactionQty = objectTransactionVO.getTransactionQty().add(currentQty);
                        objectTransactionVO.setTransactionQty(transactionQty);
                        objectTransactionMap.replace(releaseMaterialLot.getMaterialLotId(), objectTransactionVO);
                    } else {
                        //记录事务明细
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

                    //构造投料记录表参数
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

                    //当前EO投完直接退出
                    if (currRemainQty.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }
                }

                //构造当前EO的批量装配参数
                if (eoAssembleProcessActualMap.containsKey(currEo.getEoId())) {
                    //重新装入map
                    MtAssembleProcessActualVO17 assembleProcessActualVO17 = eoAssembleProcessActualMap.get(currEo.getEoId());
                    assembleProcessActualVO17.setMaterialInfo(materialInfo);
                    eoAssembleProcessActualMap.replace(currEo.getEoId(), assembleProcessActualVO17);
                } else {
                    //新增当前eo的装配数据
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

            //循环执行物料批消耗API
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

        //API-批量组件装配处理
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
            log.info("=================================>批量作业-出站批量消耗物料批总耗时：" + (System.currentTimeMillis() - startDate) + "毫秒");
        }

        //记录事务明细
        if (CollectionUtils.isNotEmpty(objectTransactionRequestList)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        }

        //记录投料记录表
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
     * @Description 构造返回数据
     *
     * @author yuchao.wang
     * @date 2020/11/19 12:04
     * @param tenantId 租户ID
     * @param hmeEoJobSnVO16  参数
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    private List<HmeEoJobSn> getResultData(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        List<HmeEoJobSn> hmeEoJobSnEntityList = hmeEoJobSnVO16.getHmeEoJobSnEntityList();

        //查询当前步骤/下一步骤
        List<String> eoStepIdList = hmeEoJobSnEntityList.stream().map(HmeEoJobSn::getEoStepId).collect(Collectors.toList());
        Map<String, HmeRouterStepVO4> routerStepMap = hmeEoJobSnCommonService.batchQueryCurrentAndNextStep(tenantId, eoStepIdList);

        //构造返回数据
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
     * @Description 出站查询基础数据
     *
     * @author yuchao.wang
     * @date 2020/11/19 12:03
     * @param tenantId 租户ID
     * @param dto 参数
     * @param eoIdList eoIdList
     * @param jobIdList jobIdList
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    private HmeEoJobSnVO16 queryBasicData(Long tenantId, HmeEoJobSnVO3 dto, List<String> eoIdList, List<String> jobIdList) {
        //获取所有条码ID
        List<String> materialLotIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());

        //根据JobId查询EoJobSn数据
        long startDate = System.currentTimeMillis();
        SecurityTokenHelper.close();
        List<HmeEoJobSn> hmeEoJobSnEntityList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeEoJobSn.FIELD_JOB_ID, jobIdList)).build());
        log.info("=================================>批量工序作业平台-出站根据JobId查询EoJobSn数据总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        if (CollectionUtils.isEmpty(hmeEoJobSnEntityList) || hmeEoJobSnEntityList.size() != jobIdList.size()) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "作业信息"));
        }

        //校验EoJobSn是否已经出站，有则报错
        List<HmeEoJobSn> siteOutList = hmeEoJobSnEntityList.stream().filter(item -> Objects.nonNull(item.getSiteOutDate()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(siteOutList)) {
            // 	已完成SN号不允许重复完成
            throw new MtException("HME_EO_JOB_SN_056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_056", "HME"));
        }

        //批量查询EO、WO相关信息
        String eoStepId = hmeEoJobSnEntityList.get(0).getEoStepId();
        startDate = System.currentTimeMillis();
        HmeEoJobSnVO16 hmeEoJobSnVO16 = hmeEoJobSnCommonService.batchQueryEoAndWoInfoWithComponentById(tenantId, eoStepId, eoIdList);
        log.info("=================================>批量工序作业平台-出站批量查询EO、WO相关信息总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        hmeEoJobSnVO16.setHmeEoJobSnEntityList(hmeEoJobSnEntityList);

        //查询是否容器出站
        startDate = System.currentTimeMillis();
        hmeEoJobSnVO16.setContainerOutSiteFlag(hmeEoJobSnCommonService.isContainerOutSite(tenantId, dto.getWorkcellId()));
        log.info("=================================>批量工序作业平台-出站查询是否容器出站总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //容器出站查询容器ID
        if (hmeEoJobSnVO16.getContainerOutSiteFlag()) {
            startDate = System.currentTimeMillis();
            List<HmeEoJobContainer> hmeEoJobContainers = hmeEoJobContainerRepository.selectByCondition(Condition.builder(HmeEoJobContainer.class)
                    .select(HmeEoJobContainer.FIELD_JOB_CONTAINER_ID, HmeEoJobContainer.FIELD_CONTAINER_ID)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobContainer.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobContainer.FIELD_WORKCELL_ID, dto.getWorkcellId())).build());
            log.info("=================================>批量工序作业平台-出站容器出站查询容器ID总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            if (CollectionUtils.isEmpty(hmeEoJobContainers)
                    || Objects.isNull(hmeEoJobContainers.get(0))
                    || StringUtils.isBlank(hmeEoJobContainers.get(0).getJobContainerId())) {
                throw new MtException("HME_EO_JOB_SN_010", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_010", "HME"));
            }
            hmeEoJobSnVO16.setEoJobContainer(hmeEoJobContainers.get(0));
        }

        //查询工艺是否卡BOM ture时出站跳过投料齐套校验和反冲料扣减逻辑 add by yuchao.wang for peng.zhao at 2021.1.29
        startDate = System.currentTimeMillis();
        hmeEoJobSnVO16.setOperationBomFlag(hmeEoJobSnCommonService.queryOperationBomFlag(tenantId, dto.getOperationId()));
        log.info("=================================>批量工序作业平台-出站queryOperationBomFlag总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

        if (!hmeEoJobSnVO16.getOperationBomFlag()) {

            //校验不同routerId出站
            //V20210518 modify by penglin.sui for peng.zhao BOM_FLAG不为Y，才做工艺路线一致性以及装配清单一致性校验
            List<String> routerIdList = hmeEoJobSnVO16.getEoMap().values().stream().map(HmeEoVO4::getRouterId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(routerIdList) || routerIdList.size() > 1) {
                //当前所选SN对应的工艺路线不一致,请检查
                throw new MtException("HME_EO_JOB_SN_138", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_138", "HME"));
            }

            //校验不相同BOM出站
            List<String> bomIdList = hmeEoJobSnVO16.getEoMap().values().stream().map(HmeEoVO4::getBomId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(bomIdList) || bomIdList.size() > 1) {
                //当前所选SN对应的装配清单不一致,请检查
                throw new MtException("HME_EO_JOB_SN_136", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_136", "HME"));
            }

            //获取所有bom组件
            List<HmeBomComponentVO> bomComponentList = new ArrayList<>();
            for (HmeEoVO4 item : hmeEoJobSnVO16.getEoMap().values()) {
                if (CollectionUtils.isNotEmpty(item.getBomComponentList())) {
                    bomComponentList.addAll(item.getBomComponentList());
                }
            }

            //查询所有反冲料组件
            if (CollectionUtils.isNotEmpty(bomComponentList)) {
                List<String> bomComponentIdList = bomComponentList.stream().map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());
                startDate = System.currentTimeMillis();
                List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnLotMaterialMapper.selectBackFlash(tenantId, dto.getSiteId(), bomComponentIdList);
                log.info("=================================>批量工序作业平台-出站查询所有反冲料组件总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
                hmeEoJobSnVO16.setBackFlushBomComponentList(backFlushBomComponentList);
            } else {
                hmeEoJobSnVO16.setBackFlushBomComponentList(new ArrayList<MtBomComponent>());
            }
        }

        //批量查询条码信息
        startDate = System.currentTimeMillis();
        Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnCommonService.batchQueryMaterialLotInfo(tenantId, materialLotIdList);
        log.info("=================================>批量工序作业平台-出站批量查询条码信息总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        hmeEoJobSnVO16.setMaterialLotMap(materialLotMap);

        //构造返修信息
        Map<String, String> reworkFlagMap = new HashMap<>();
        for (Map.Entry<String, HmeMaterialLotVO3> entry : materialLotMap.entrySet()) {
            reworkFlagMap.put(entry.getKey(), entry.getValue().getReworkFlag());
        }
        hmeEoJobSnVO16.setReworkFlagMap(reworkFlagMap);

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        hmeEoJobSnVO16.setUserId(userId);
        hmeEoJobSnVO16.setCurrentDate(new Date());

        //V20210312 modify by penglin.sui for tianyang.xie 判断是否存在不良的数据采集项
        startDate = System.currentTimeMillis();
        List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnCommonService.queryNgDataRecord(tenantId,dto.getWorkcellId(),jobIdList);
        log.info("=================================>批量工序作业平台-出站判断是否存在不良的数据采集项总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        if(CollectionUtils.isNotEmpty(ngDataRecordList)) {
            hmeEoJobSnVO16.setNgDataRecordList(ngDataRecordList);
        }

        Map<String,HmeEoJobDataRecordVO2> hmeEoJobDataRecordVO2Map = hmeEoJobDataRecordRepository.queryNcRecord(tenantId,jobIdList);
        hmeEoJobSnVO16.setNcJobDataRecordMap(hmeEoJobDataRecordVO2Map);

        return hmeEoJobSnVO16;
    }

    /**
     *
     * @Description 出站校验
     *
     * @author yuchao.wang
     * @date 2020/11/17 17:13
     * @return void
     *
     */
    private HmeEoJobSn outSiteValidate(Long tenantId, List<String> jobIdList, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        //批量暂不支持返修
        if (HmeConstants.OutSiteAction.REWORK.equals(dto.getOutSiteAction())) {
            throw new MtException("HME_EO_JOB_SN_133", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_133", "HME"));
        }

        //找到质量状态NG但是非返修
        Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnVO16.getMaterialLotMap();
        long ngCount = materialLotMap.values().stream()
                .filter(item -> HmeConstants.ConstantValue.NG.equals(item.getQualityStatus())
                        && !HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())).count();
        if (ngCount > 0) {
            throw new MtException("HME_EO_JOB_SN_060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_060", "HME"));
        }

        //校验条码状态及返修标识,批量暂不支持返修
        long reworkCount = materialLotMap.values().stream()
                .filter(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())).count();
        if (reworkCount > 0) {
            throw new MtException("HME_EO_JOB_SN_133", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_133", "HME"));
        }

        //V20210630 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可出站
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
            //此SN【${1}】正在盘点,不可出站
            throw new MtException("HME_EO_JOB_SN_204", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_204", "HME", materialLots.toString()));
        }

        // 20210721 modify by sanfeng.zhang for xenxin.zhang 出站校验冻结标识为Y的SN不可出站
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
            //【此SN【${1}】正在冻结,不可出站】
            throw new MtException("HME_EO_JOB_SN_207", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_207", "HME", materialLotFreezeFlags.toString()));
        }

        //校验数据采集结果
//        if (hmeEoJobSnCommonService.hasMissingValue(tenantId, dto.getWorkcellId(), jobIdList)) {
//            // 出站失败,存在未记录结果的数据采集记录
//            throw new MtException("HME_EO_JOB_SN_009", mtErrorMessageRepository
//                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_009", "HME"));
//        }
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = hmeEoJobSnCommonService.hasMissingValueTag(tenantId, dto.getWorkcellId(), jobIdList);
        if(CollectionUtils.isNotEmpty(hmeEoJobDataRecordVOList)){
            List<HmeEoJobSnVO3> snLineList = dto.getSnLineList().stream().filter(item -> item.getJobId().equals(hmeEoJobDataRecordVOList.get(0).getJobId()))
                    .collect(Collectors.toList());
            // sn${1}数据采集项${2}未记录结果,不允许出站!
            throw new MtException("HME_EO_JOB_SN_195", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_195", "HME",
                            snLineList.get(0).getSnNum() , hmeEoJobDataRecordVOList.get(0).getTagDescription()));
        }

        //V20210331 modify by penglin.sui for tianyang.xie 末道序点加工完成数据采集项不合格不允许出站
        //批量查询是否为完成步骤
        List<String> eoStepIdList = hmeEoJobSnVO16.getHmeEoJobSnEntityList().stream().map(HmeEoJobSn::getEoStepId).distinct().collect(Collectors.toList());
        List<MtRouterDoneStep> mtRouterDoneStepList = mtRouterDoneStepRepository.selectByCondition(Condition.builder(MtRouterDoneStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterDoneStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterDoneStep.FIELD_ROUTER_STEP_ID, eoStepIdList)).build());
        if(CollectionUtils.isNotEmpty(mtRouterDoneStepList)) {
            if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())
                && CollectionUtils.isNotEmpty(hmeEoJobSnVO16.getNgDataRecordList())) {

                //按照JobId挑拣出要出站数据的信息
                Map<String, HmeEoJobSn> eoJobSnEntityMap = new HashMap<String, HmeEoJobSn>();
                hmeEoJobSnVO16.getHmeEoJobSnEntityList().forEach(item -> eoJobSnEntityMap.put(item.getJobId(), item));

                //筛选出所有完成步骤
                List<String> doneStepIdList = mtRouterDoneStepList.stream().map(MtRouterDoneStep::getRouterStepId).distinct().collect(Collectors.toList());
                //筛选完成步骤的作业
                List<HmeEoJobSnVO3> doneSnLineList = dto.getSnLineList().stream()
                        .filter(item -> doneStepIdList.contains(eoJobSnEntityMap.get(item.getJobId()).getEoStepId())).collect(Collectors.toList());

                for (HmeEoJobSnVO3 hmeEoJobSnVO3 : doneSnLineList
                ) {
                    List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnVO16.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(hmeEoJobSnVO3.getJobId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
//                        // 存在不合格的数据采集项,不允许出站!
//                        throw new MtException("HME_EO_JOB_SN_192", mtErrorMessageRepository
//                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_192", "HME"));
                        // 	sn${1}数据采集项${2}不合格,不允许出站!
                        throw new MtException("HME_EO_JOB_SN_196", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_196", "HME",
                                        hmeEoJobSnVO3.getSnNum(), ngDataRecordList.get(0).getTagDescription()));
                    }
                }
            }
        }
        Long startDate = System.currentTimeMillis();
        //校验数据采集项工序不良判定 add by yuchao.wang for peng.zhao at 2021.1.25
        if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag())) {
            //查询所有有结果的数据采集项
            List<HmeEoJobDataRecord> allEoJobDataRecordList = hmeEoJobDataRecordRepository
                    .batchQueryForNcRecordValidate(tenantId, dto.getWorkcellId(), jobIdList);
            if (CollectionUtils.isNotEmpty(allEoJobDataRecordList)) {
                int index = -1;
                StringBuilder errorMessages = new StringBuilder();
                Map<String , List<HmeProcessNcDetailVO2>> processNcDetailMap = new HashMap<>();
                //根据工艺判断是器件不良还是老化不良
                if (hmeEoJobSnCommonService.isAgeingNc(tenantId, dto.getOperationId())) {
                    //老化不良
                    //查询工序信息
                    String stationId = hmeEoJobSnMapper.queryWkcStation(tenantId, dto.getSiteId(), dto.getWorkcellId());

                    List<String> materialIdList = materialLotMap.values().stream()
                            .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());

                    //查询所有工序不良信息
                    List<HmeProcessNcHeaderVO2> processNcInfoList = hmeProcessNcHeaderRepository
                            .batchQueryProcessNcInfoForAgeingNcRecordValidate(tenantId, dto.getOperationId(),materialIdList);

                    //对所有的数据采集项按照jobId分组
                    Map<String, List<HmeEoJobDataRecord>> eoJobDataRecordMap = allEoJobDataRecordList
                            .stream().collect(Collectors.groupingBy(HmeEoJobDataRecord::getJobId));

                    //遍历所有作业校验采集项
                    for (HmeEoJobSn eoJobSn : hmeEoJobSnVO16.getHmeEoJobSnEntityList()) {
                        index++;
                        if (!eoJobDataRecordMap.containsKey(eoJobSn.getJobId())) {
                            continue;
                        }

                        //获取当前作业的条码及数据采集项
                        HmeMaterialLotVO3 materialLot = materialLotMap.get(eoJobSn.getMaterialLotId());
                        String materialLotCode = materialLot.getMaterialLotCode();
                        List<HmeEoJobDataRecord> currEoJobDataRecordList = eoJobDataRecordMap.get(eoJobSn.getJobId());

                        //V20210607 modify by penglin.sui for peng.zhao 先根据【material_id】查，若有多笔：则根据【material_id】+【cos_model】，若唯一则取此数据；若有多笔，继续根据【material_id】+【cos_model】+【workcell_id】查，若唯一则取此数据，若多比，则报错HME_EO_JOB_SN_202
                        List<HmeProcessNcHeaderVO2> subProcessNcInfoList = processNcInfoList.stream().filter(item -> item.getMaterialId().equals(materialLot.getMaterialId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(subProcessNcInfoList)){

                            List<String> headerIdList = subProcessNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());

                            if(headerIdList.size() > 1) {
                                String cosModel = materialLotCode.substring(4, 5);
                                subProcessNcInfoList = subProcessNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getCosModel()) && item.getCosModel().equals(cosModel)).collect(Collectors.toList());

                                if (CollectionUtils.isEmpty(subProcessNcInfoList)) {
                                    // 	此SN【${1}】查询到多条判定标准,请检查!
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
                                        // 	此SN【${1}】查询到多条判定标准,请检查!
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

                        //当前作业的数据采集项进行校验
                        HmeEoJobSn dataRecordValidateResult = hmeEoJobSnCommonService
                                .dataRecordAgeingProcessNcValidate(tenantId, materialLotCode, currEoJobDataRecordList, processNcInfo);
                        if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {

                            //V20210408 modify by penglin.sui for peng.zhao 新增工序不良参数返回，根据工序判定结果去自动发起不良
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
                    //器件不良
                    //查询所有工序不良信息
                    List<String> materialIdList = materialLotMap.values().stream()
                            .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());
                    Map<String, HmeProcessNcHeaderVO2> processNcInfoMap = hmeProcessNcHeaderRepository
                            .batchQueryProcessNcInfoForNcRecordValidate(tenantId, dto.getOperationId(), materialIdList);

                    //对所有的数据采集项按照jobId分组
                    Map<String, List<HmeEoJobDataRecord>> eoJobDataRecordMap = allEoJobDataRecordList
                            .stream().collect(Collectors.groupingBy(HmeEoJobDataRecord::getJobId));

                    //遍历所有作业校验采集项
                    for (HmeEoJobSn eoJobSn : hmeEoJobSnVO16.getHmeEoJobSnEntityList()) {
                        index++;
                        if (!eoJobDataRecordMap.containsKey(eoJobSn.getJobId())) {
                            continue;
                        }

                        //获取当前产品编码和cos类型
                        HmeMaterialLotVO3 materialLot = materialLotMap.get(eoJobSn.getMaterialLotId());
                        String materialLotCode = materialLot.getMaterialLotCode();
                        String key = materialLot.getMaterialId() + "," + materialLotCode.substring(2, 4) + "," + materialLotCode.substring(4, 5) + "," + materialLotCode.substring(5, 6);
                        HmeProcessNcHeaderVO2 processNcInfo = processNcInfoMap.getOrDefault(key , null);
                        if(Objects.isNull(processNcInfo)){
                            key = materialLot.getMaterialId() + "," + materialLotCode.substring(2, 4) + "," + materialLotCode.substring(4, 5) + ",";
                            processNcInfo = processNcInfoMap.get(key);
                        }

                        //获取当前作业的数据采集项进行校验
                        List<HmeEoJobDataRecord> currEoJobDataRecordList = eoJobDataRecordMap.get(eoJobSn.getJobId());
                        HmeEoJobSn dataRecordValidateResult = hmeEoJobSnCommonService
                                .dataRecordProcessNcValidate(tenantId, materialLotCode, currEoJobDataRecordList, processNcInfo);
                        if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {

                            //V20210408 modify by penglin.sui for peng.zhao 新增工序不良参数返回，根据工序判定结果去自动发起不良
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

        //重新获取容器中的条码，防止前台缓存的条码已经被卸载 modify by yuchao.wang for tianyang.xie at 2020.9.12
        if (StringUtils.isNotBlank(dto.getContainerId())) {
            MtContLoadDtlVO10 contLoadDtlParam = new MtContLoadDtlVO10();
            contLoadDtlParam.setContainerId(dto.getContainerId());
            contLoadDtlParam.setAllLevelFlag(HmeConstants.ConstantValue.NO);
            startDate = System.currentTimeMillis();
            List<MtContLoadDtlVO4> mtContLoadDtlVo1List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlParam);
            log.info("=================================>批量工序作业平台-出站校验containerLimitMaterialLotQuery总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            if (CollectionUtils.isNotEmpty(mtContLoadDtlVo1List)) {
                List<String> materialLotIds = mtContLoadDtlVo1List.stream().filter(t -> t.getMaterialLotId() != null)
                        .map(MtContLoadDtlVO4::getMaterialLotId).distinct().collect(Collectors.toList());

                if (hmeEoJobSnCommonService.hasOtherOperationJob(tenantId, dto.getOperationId(), materialLotIds)) {
                    // 当前容器不允许绑定生产进度不一致的条码
                    throw new MtException("HME_EO_JOB_SN_058", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_058", "HME"));
                }
            }
        }

        //工艺是否卡BOM ture时出站跳过投料齐套校验和反冲料扣减逻辑 add by yuchao.wang for peng.zhao at 2021.1.29
        if (!hmeEoJobSnVO16.getOperationBomFlag()) {
            //判断反冲料中是否有虚拟件，有则报错
            if (CollectionUtils.isNotEmpty(hmeEoJobSnVO16.getBackFlushBomComponentList())) {
                List<String> backFlushBomComponentIdList = hmeEoJobSnVO16.getBackFlushBomComponentList().stream()
                        .map(MtBomComponent::getBomComponentId).collect(Collectors.toList());
                startDate = System.currentTimeMillis();
                List<MtMaterial> virtualMtMaterialList = hmeEoJobSnLotMaterialMapper.selectVirtualMaterial(tenantId, backFlushBomComponentIdList);
                log.info("=================================>批量工序作业平台-出站校验查询虚拟件物料总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
                if (CollectionUtils.isNotEmpty(virtualMtMaterialList)) {
                    List<String> virtualMaterialCodeList = virtualMtMaterialList.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
                    String materialCodes = String.join(",", virtualMaterialCodeList);
                    //反冲料【${1}】不可为虚拟件,请检查
                    throw new MtException("HME_EO_JOB_SN_080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_080", "HME", materialCodes));
                }

                //获取BOM数量，当前已限制相同BOM出站投料
                ArrayList<HmeEoVO4> hmeEoVO4List = new ArrayList<>(hmeEoJobSnVO16.getEoMap().values());
                Double bomQty = hmeEoVO4List.get(0).getBomPrimaryQty();
                if (Objects.isNull(bomQty)) {
                    //${1}不存在 请确认${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "BOM", hmeEoVO4List.get(0).getBomId()));
                }
                BigDecimal bomPrimaryQty = BigDecimal.valueOf(bomQty);
                if (bomPrimaryQty.compareTo(BigDecimal.ZERO) == 0) {
                    //基本数量为0,请确认
                    throw new MtException("HME_EO_JOB_SN_082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_082", "HME"));
                }
            }

            //出站投料齐套校验
            startDate = System.currentTimeMillis();
            outSiteReleaseValidate(tenantId, hmeEoJobSnVO16, dto);
            log.info("=================================>批量工序作业平台-出站校验出站投料齐套校验总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        //出站容器装载校验
        if (hmeEoJobSnVO16.getContainerOutSiteFlag()) {
            startDate = System.currentTimeMillis();
            outSiteContainerLoadBatchVerify(tenantId, hmeEoJobSnVO16, dto);
            log.info("=================================>批量工序作业平台-出站校验出站容器装载校验总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }
        return null;
    }

    /**
     *
     * @Description 出站容器装载校验
     *
     * @author yuchao.wang
     * @date 2020/12/3 15:06
     * @param tenantId 租户ID
     * @param hmeEoJobSnVO16 参数
     * @param dto 参数
     * @return void
     *
     */
    private void outSiteContainerLoadBatchVerify(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        String containerId = hmeEoJobSnVO16.getEoJobContainer().getContainerId();

        //查询当前容器信息
        Long startDate = System.currentTimeMillis();
        HmeContainerVO containerInfo = hmeEoJobSnMapper.queryContainerInfo(tenantId, containerId);
        log.info("=================================>批量工序作业平台-出站校验出站容器装载校验-查询当前容器信息总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        if (Objects.isNull(containerInfo) || StringUtils.isBlank(containerInfo.getContainerId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "容器信息"));
        }

        //校验容器可用性,容器状态需要为可下达
        if (!HmeConstants.StatusCode.CANRELEASE.equals(containerInfo.getStatus())) {
            throw new MtException("HME_LOAD_CONTAINER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_002", "HME", containerInfo.getContainerCode()));
        }

        //先校验当前一批数据，符合条件后再查询已装载数据
        //校验容器是否装满,容器可装数量为空不校验
        double totalQty = 0;
        if (Objects.nonNull(containerInfo.getCapacityQty())) {
            totalQty = hmeEoJobSnVO16.getEoMap().values().stream().mapToDouble(MtEo::getQty).sum();
            if (totalQty > containerInfo.getCapacityQty()) {
                //容器已经装满
                throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //校验容器是否可以混装物料
        String materialId = "";
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedMaterialFlag())) {
            List<String> materialIdList = hmeEoJobSnVO16.getMaterialLotMap().values().stream()
                    .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());
            if (materialIdList.size() != 1) {
                //容器不可以混装物料
                throw new MtException("MT_MATERIAL_LOT_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0045", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
            materialId = materialIdList.get(0);
        }

        //校验容器是否可以混装EO
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedEoFlag())
                && hmeEoJobSnVO16.getEoMap().size() != 1) {
            //容器不可以混装EO
            throw new MtException("MT_MATERIAL_LOT_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0044", "MATERIAL_LOT", containerInfo.getContainerCode()));
        }
        String eoId = dto.getSnLineList().get(0).getEoId();

        //校验容器是否可以混装WO
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedWoFlag())
                && hmeEoJobSnVO16.getWoMap().size() != 1) {
            //容器不可以混装WO
            throw new MtException("MT_MATERIAL_LOT_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0047", "MATERIAL_LOT", containerInfo.getContainerCode()));
        }
        String woId = hmeEoJobSnVO16.getEoMap().get(eoId).getWorkOrderId();

        //查询容器当前装载明细
        startDate = System.currentTimeMillis();
        List<HmeContainerDetailVO> containerDetailList = hmeEoJobSnMapper.queryContainerDetails(tenantId, containerId);
        log.info("=================================>批量工序作业平台-出站校验出站容器装载校验-查询容器当前装载明细总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        if (CollectionUtils.isEmpty(containerDetailList)) {
            return;
        }

        //校验容器是否装满,容器可装数量为空不校验
        if (Objects.nonNull(containerInfo.getCapacityQty())) {
            totalQty += containerDetailList.stream().filter(item -> Objects.nonNull(item.getPrimaryUomQty()))
                    .mapToDouble(HmeContainerDetailVO::getPrimaryUomQty).sum();
            if (totalQty > containerInfo.getCapacityQty()) {
                //容器已经装满
                throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //校验容器是否可以混装物料
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedMaterialFlag())) {
            List<String> materialIdList = containerDetailList.stream().map(HmeContainerDetailVO::getMaterialId)
                        .distinct().collect(Collectors.toList());

            if (materialIdList.size() != 1 || !materialIdList.contains(materialId)) {
                //容器不可以混装物料
                throw new MtException("MT_MATERIAL_LOT_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0045", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //校验容器是否可以混装EO
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedEoFlag())) {
            List<String> eoIdList = containerDetailList.stream().map(HmeContainerDetailVO::getEoId)
                    .distinct().collect(Collectors.toList());

            if (eoIdList.size() != 1 || !eoIdList.contains(eoId)) {
                //容器不可以混装EO
                throw new MtException("MT_MATERIAL_LOT_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0044", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //校验容器是否可以混装WO
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedWoFlag())) {
            List<String> woIdList = containerDetailList.stream().map(HmeContainerDetailVO::getWorkOrderId)
                    .distinct().collect(Collectors.toList());

            if (woIdList.size() != 1 || !woIdList.contains(woId)) {
                //容器不可以混装WO
                throw new MtException("MT_MATERIAL_LOT_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0047", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }
    }

    /**
     *
     * @Description 出站投料齐套校验
     *
     * @author yuchao.wang
     * @date 2020/11/25 17:00
     * @param tenantId 租户ID
     * @param hmeEoJobSnVO16 参数
     * @return void
     *
     */
    private void outSiteReleaseValidate(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        //查询值集,判断是否需要校验投料齐套
        List<LovValueDTO> inputWoTypeValues = lovAdapter.queryLovValue("HME.INPUT_WO_TYPE", tenantId);
        if (CollectionUtils.isEmpty(inputWoTypeValues)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Return:值集HME.INPUT_WO_TYPE为空");
            return;
        }
        List<String> valueList = inputWoTypeValues.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        //筛选所有要校验的工单,如果工单类型在值集中,并且产线标识不为Y 才去校验投料齐套
        List<String> validateWoIdList = hmeEoJobSnVO16.getWoMap().values().stream()
                .filter(item -> valueList.contains(item.getWorkOrderType()) && !HmeConstants.ConstantValue.YES.equals(item.getIssuedFlag()))
                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(validateWoIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Return:没有符合条件的工单 valueList={}", valueList);
            return;
        }

        //筛选所有对应的EO
        List<HmeEoVO4> validateEoList = hmeEoJobSnVO16.getEoMap().values().stream()
                .filter(item -> validateWoIdList.contains(item.getWorkOrderId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(validateEoList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Return:没有符合条件的EO validateWoIdList={}", validateWoIdList);
            return;
        }

        //获取反冲料组件ID
        List<String> backFlushBomComponentIdList = hmeEoJobSnVO16.getBackFlushBomComponentList()
                .stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList());

        //筛选EO对应的组件清单,去掉(不投料标识为Y && 虚拟件组件) mainComponentMap key:eoId,主产品组件ID value:主产品组件
        Map<String, HmeBomComponentVO> mainComponentMap = new HashMap<>();
        List<String> mainComponentIdList = new ArrayList<>();
        validateEoList.forEach(eo -> {
            if (CollectionUtils.isNotEmpty(eo.getBomComponentList())) {
                eo.getBomComponentList().forEach(component -> {
                    if (!backFlushBomComponentIdList.contains(component.getBomComponentId())
                            && !HmeConstants.ConstantValue.YES.equals(component.getIssuedFlag())
                            && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(component.getVirtualFlag())) {
                        //添加主料组件ID
                        if (!mainComponentIdList.contains(component.getBomComponentId())) {
                            mainComponentIdList.add(component.getBomComponentId());
                        }
                        //添加EO-组件信息
                        mainComponentMap.put(eo.getEoId() + "," + component.getBomComponentId(), component);
                    }
                });
            }
        });
        if (CollectionUtils.isEmpty(mainComponentIdList)) {
            log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Return:没有符合条件的EO组件 validateEoList={}", validateEoList);
            return;
        }
        Long startDate = System.currentTimeMillis();
        //查询对应组件物料替代料 substituteMaterialComponentMap key:主产品组件ID value:替代料对应的组件ID
        Map<String, List<String>> substituteMaterialComponentMap = new HashMap<>();
        String eoStepId = hmeEoJobSnVO16.getHmeEoJobSnEntityList().get(0).getEoStepId();
        List<HmeBomComponentVO> substituteMaterialComponentList = hmeEoJobSnMapper.batchQuerySubstituteMaterialComponent(tenantId, eoStepId, mainComponentIdList);
        log.info("=================================>批量工序作业平台-出站校验出站投料齐套校验-查询对应组件物料替代料总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate 查询到替代料数据{}条,eoStepId={},mainComponentIdList={}",
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

        //查询所有组件对应的实绩装配数
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
        log.info("=================================>批量工序作业平台-出站校验出站投料齐套校验-mtEoComponentActualRepository.selectByCondition总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //查询主料的全局替代料的装配实绩
        List<String> mainComponentMaterialIdList = mainComponentMap.values().stream()
                .map(HmeBomComponentVO::getBomComponentMaterialId).distinct().collect(Collectors.toList());
        startDate = System.currentTimeMillis();
        List<HmeEoComponentActualVO> eoGlobalMaterialActualList = hmeEoJobSnMapper.batchQueryEoActualForGlobalMaterial(tenantId,
                dto.getOperationId(), eoStepId, mainComponentMaterialIdList, validateEoIdList);
        log.info("=================================>批量工序作业平台-出站校验出站投料齐套校验-查询主料的全局替代料的装配实绩总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //循环主产品组件，对比各个主产品 组件需求数量 <= 主产品及其替代品实绩数量
        for (Map.Entry<String, HmeBomComponentVO> mainComponentEntry : mainComponentMap.entrySet()) {
            //获取当前eoId componentId
            String[] keySplit = mainComponentEntry.getKey().split(",");
            String eoId = keySplit[0];
            String componentId = keySplit.length == 2 ? keySplit[1] : "";

            HmeBomComponentVO mainComponent = mainComponentEntry.getValue();

            //获取当前主产品及其替代品所有ID
            List<String> currComponentIdList = new ArrayList<>();
            currComponentIdList.add(componentId);
            if (substituteMaterialComponentMap.containsKey(componentId)) {
                currComponentIdList.addAll(substituteMaterialComponentMap.get(componentId));
            }

            //获取当前主产品及其替代品实绩数量
            double sumComponentActual = eoComponentActualList.stream()
                    .filter(item -> eoId.equals(item.getEoId()) && currComponentIdList.contains(item.getBomComponentId())
                            && Objects.nonNull(item.getAssembleQty())).mapToDouble(MtEoComponentActual::getAssembleQty).sum();

            //找到当前料对应的实绩信息
            List<HmeEoComponentActualVO> currEoGlobalMaterialActualList = eoGlobalMaterialActualList.stream()
                    .filter(item -> eoId.equals(item.getEoId()) && item.getMainMaterialId().equals(mainComponent.getBomComponentMaterialId())
                    && Objects.nonNull(item.getAssembleQty())).collect(Collectors.toList());

            //如果当前料有多个替代组则报错
            if (currEoGlobalMaterialActualList.stream().map(HmeEoComponentActualVO::getSubstituteGroup).distinct().count() > 1) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mainComponent.getBomComponentMaterialId());
                //替代料【{$1}】存在于多个替代组中,请检查
                throw new MtException("HME_EO_JOB_SN_167", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_167", "HME", mtMaterial.getMaterialCode()));
            }

            //获取当前主产品对应的全局替代料实绩数量
            double sumGlobalMaterialActual = currEoGlobalMaterialActualList.stream().mapToDouble(HmeEoComponentActualVO::getAssembleQty).sum();

            if (mainComponent.getBomComponentQty() > (sumComponentActual + sumGlobalMaterialActual)) {
                log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Exception:BomComponentQty={},sumComponentActual={},sumGlobalMaterialActual={}",
                        mainComponent.getBomComponentQty(), sumComponentActual, sumGlobalMaterialActual);
                log.info("<====== HmeEoJobSnBatchOutSiteServiceImpl.outSiteReleaseValidate Exception:eoId={},mainComponentId={},currComponentIdList={}",
                        eoId, componentId, currComponentIdList);

                //找到eo对应的sn
                Optional<HmeEoJobSnVO3> firstSn = dto.getSnLineList().stream().filter(item -> eoId.equals(item.getEoId())).findFirst();
                String snNum = "";
                if (firstSn.isPresent()) {
                    snNum = firstSn.get().getSnNum();
                }

                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mainComponent.getBomComponentMaterialId());
                //SN【${1}】对应物料【${2}】投料数未满足需求数,无法出站
                throw new MtException("HME_EO_JOB_SN_139", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_139", "HME", snNum, mtMaterial.getMaterialCode()));
            }
        }
    }

    /**
     *
     * @Description 默认输入校验
     *
     * @author yuchao.wang
     * @date 2020/11/5 14:11
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    private void paramsVerificationForOutSite(Long tenantId, HmeEoJobSnVO3 dto) {
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位ID"));
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        }
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "站点ID"));
        }
        if (StringUtils.isBlank(dto.getWkcShiftId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "班次ID"));
        }
        if (StringUtils.isBlank(dto.getJobType())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "作业类型"));
        }
        if (CollectionUtils.isEmpty(dto.getSnLineList())) {
            throw new MtException("HME_EO_JOB_SN_018",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_018", "HME"));
        }
    }
}