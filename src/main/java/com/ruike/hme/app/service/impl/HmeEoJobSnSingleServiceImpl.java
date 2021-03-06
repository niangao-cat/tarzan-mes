package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeEoJobDataCalculationResultDTO;
import com.ruike.hme.api.dto.HmeEoJobSnSingleDTO;
import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.api.dto.HmeNcDisposePlatformDTO11;
import com.ruike.hme.app.service.HmeEoJobSnBatchService;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.app.service.HmeEoJobSnSingleService;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtAssembleProcessActualRepository;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterDoneStepRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.repository.MtEoRouterRepository;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * ??????????????????-SN??????????????????????????????
 *
 * @author yuchao.wang@hand-china.com 2020-11-21 00:04:39
 */
@Slf4j
@Service
public class HmeEoJobSnSingleServiceImpl implements HmeEoJobSnSingleService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private HmeEoJobContainerRepository hmeEoJobContainerRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;

    @Autowired
    private HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper;

    @Autowired
    private HmeEoJobSnBatchMapper hmeEoJobSnBatchMapper;

    @Autowired
    private HmeEoJobSnBatchService hmeEoJobSnBatchService;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private HmeEoRouterBomRelRepository hmeEoRouterBomRelRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;

    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;

    @Autowired
    private HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;

    @Autowired
    private HmeFacYkMapper hmeFacYkMapper;

    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;

    @Autowired
    private HmeWkcEoRelRepository hmeWkcEoRelRepository;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeProcessNcHeaderRepository hmeProcessNcHeaderRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmePumpModPositionLineRepository hmePumpModPositionLineRepository;

    @Autowired
    private HmePumpModPositionHeaderRepository hmePumpModPositionHeaderRepository;

    @Autowired
    private HmeEoJobPumpCombRepository hmeEoJobPumpCombRepository;

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private static final String SYMBOL = "#";

    private static String fetchGroupKey2(String str1 , String str2){
        return str1 + SYMBOL + str2;
    }

    private static String fetchGroupKey3(String str1 ,String str2 ,String str3){
        return str1 + SYMBOL + str2 + SYMBOL + str3;
    }
    /**
     *
     * @Description ????????????-??????
     *
     * @author yuchao.wang
     * @date 2020/11/21 13:59
     * @param tenantId ??????ID
     * @param dto ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSn outSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteScan.Start tenantId={},dto={}", tenantId, dto);
        //????????????
        paramsVerificationForOutSite(tenantId, dto);

        long startDate = System.currentTimeMillis();
        //???????????????????????????
        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic = queryBasicData(tenantId, dto);
        log.info("=================================>????????????-?????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        startDate = System.currentTimeMillis();
        //????????????,??????????????????????????????????????????????????????
        HmeEoJobSn validateResult = outSiteValidate(tenantId, hmeEoJobSnSingleBasic, dto);
        log.info("=================================>????????????-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
            return validateResult;
        }
        // 20210818 add by sanfeng.zhang for peng.zhao ??????????????????????????? ??????????????????????????????
        if (hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId())) {
            String outSiteAction = this.queryOutSiteAction(tenantId, dto, hmeEoJobSnSingleBasic);
            dto.setOutSiteAction(outSiteAction);
        }
        //2021-08-25 14:03 add by chaonan.hu for wenxin.zhang ?????????????????????????????????????????????
        List<HmeEoJobPumpCombVO5> hmeEoJobPumpCombVO5List = new ArrayList<>();
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsPumpProcess())){
            hmeEoJobPumpCombVO5List = hmeEoJobPumpCombRepository.pumpFilterRuleVerify(tenantId, dto);
        }

        boolean isDeleteWkcEoRel = false;
        //??????????????????????????????????????????????????????????????????????????????
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();

            //???????????????????????? ???????????????????????????
            if (HmeConstants.ConstantValue.YES.equals(materialLot.getDesignedReworkFlag())) {
                startDate = System.currentTimeMillis();
                hmeEoJobSnCommonService.mainOutSiteForDesignedReworkComplete(tenantId, dto, hmeEoJobSnSingleBasic);
                log.info("=================================>????????????-?????????????????????????????? ????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            } else {
                //????????????????????????
                if (!HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag())) {
                    startDate = System.currentTimeMillis();
                    self().backFlushMaterialOutSite(tenantId, hmeEoJobSnSingleBasic, dto);
                    log.info("=================================>????????????-??????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                }

                //???????????????????????????
                startDate = System.currentTimeMillis();
                List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.mainOutSite(tenantId, dto, hmeEoJobSnSingleBasic);
                log.info("=================================>????????????-?????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                startDate = System.currentTimeMillis();
                //??????????????????
                hmeEoJobSnCommonService.wkcCompleteOutputRecord(tenantId, dto, hmeEoJobSnSingleBasic);
                log.info("=================================>????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                startDate = System.currentTimeMillis();
                //??????????????????
                hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
                log.info("=================================>????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                isDeleteWkcEoRel = true;
            }
        } else if (HmeConstants.OutSiteAction.REWORK_COMPLETE.equals(dto.getOutSiteAction())) {
            // ????????????
            HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
            //???????????????????????? ??????????????????????????? ??????????????????
            if (HmeConstants.ConstantValue.YES.equals(materialLot.getDesignedReworkFlag())) {
                startDate = System.currentTimeMillis();
                hmeEoJobSnCommonService.mainOutSiteForDesignedReworkComplete2(tenantId, dto, hmeEoJobSnSingleBasic);
                log.info("=================================>????????????-?????????????????????????????? ????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            } else {
                //???????????????????????????
                startDate = System.currentTimeMillis();
                List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.mainOutSite2(tenantId, dto, hmeEoJobSnSingleBasic);
                log.info("=================================>????????????-?????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                startDate = System.currentTimeMillis();
                //??????????????????
                hmeEoJobSnCommonService.wkcCompleteOutputRecord(tenantId, dto, hmeEoJobSnSingleBasic);
                log.info("=================================>????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                startDate = System.currentTimeMillis();
                //??????????????????
                hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
                log.info("=================================>????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                isDeleteWkcEoRel = true;
            }
        } else {
            startDate = System.currentTimeMillis();
            hmeEoJobSnCommonService.mainOutSiteForRework(tenantId, dto, hmeEoJobSnSingleBasic);
            log.info("=================================>????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            isDeleteWkcEoRel = true;
        }

        //V20210428 modify by penglin.sui for tianyang.xie ????????????EO??????,???????????????????????????????????????????????????
        if(isDeleteWkcEoRel){
            List<HmeWkcEoRel> hmeWkcEoRelList = hmeWkcEoRelRepository.selectByCondition(Condition.builder(HmeWkcEoRel.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeWkcEoRel.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeWkcEoRel.FIELD_WKC_ID, dto.getWorkcellId())
                            .andEqualTo(HmeWkcEoRel.FIELD_OPERATION_ID, dto.getOperationId())).build());
            if(CollectionUtils.isNotEmpty(hmeWkcEoRelList)){
                //??????
                hmeWkcEoRelRepository.batchDeleteByPrimaryKey(hmeWkcEoRelList);
            }
        }

        //2021-09-07 10:27 add by chaonan.hu for wenxin.zhang ???????????????????????????????????????????????????????????????????????????,????????????????????????
        if(CollectionUtils.isNotEmpty(hmeEoJobPumpCombVO5List)){
            startDate = System.currentTimeMillis();
            HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity();
            List<HmeEoJobDataRecord> hmeEoJobDataRecordList = new ArrayList<>();
            for (HmeEoJobPumpCombVO5 hmeEoJobPumpCombVO5:hmeEoJobPumpCombVO5List) {
                HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
                hmeEoJobDataRecord.setTenantId(tenantId);
                hmeEoJobDataRecord.setJobId(hmeEoJobSnEntity.getJobId());
                hmeEoJobDataRecord.setWorkcellId(hmeEoJobSnEntity.getWorkcellId());
                hmeEoJobDataRecord.setEoId(hmeEoJobSnEntity.getEoId());
                hmeEoJobDataRecord.setTagId(hmeEoJobPumpCombVO5.getTagId());
                hmeEoJobDataRecord.setMinimumValue(hmeEoJobPumpCombVO5.getMinValue());
                hmeEoJobDataRecord.setMaximalValue(hmeEoJobPumpCombVO5.getMaxValue());
                hmeEoJobDataRecord.setGroupPurpose("DATA");
                hmeEoJobDataRecord.setResult(hmeEoJobPumpCombVO5.getResult().toString());
                hmeEoJobDataRecordList.add(hmeEoJobDataRecord);
            }
            hmeEoJobDataRecordRepository.batchInsertSelective(hmeEoJobDataRecordList);
            log.info("=================================>???????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }
        // ?????????????????????
        if (StringUtils.isNotBlank(dto.getRemark()) && StringUtils.isNotBlank(dto.getJobId()) ) {
            HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
            updateEoJobSn.setJobId(dto.getJobId());
            updateEoJobSn.setAttribute8(dto.getRemark());
            hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);
        }
        //??????????????????
        HmeEoJobSn result = getResultData(tenantId, hmeEoJobSnSingleBasic, dto);
        log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
        return result;
    }

    @Override
    public String queryOutSiteAction (Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        String outSiteAction = dto.getOutSiteAction();
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
        // ?????????????????????????????????
        if (HmeConstants.ConstantValue.YES.equals(dto.getCrossRetestFlag())) {
            // ????????? ????????????????????????
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(materialLot.getMaterialLotId());
                setTableName("mt_material_lot_attr");
                setAttrName("JCRETEST");
            }});
            String jcRetest = CollectionUtils.isNotEmpty(mtExtendAttrVOS) ? mtExtendAttrVOS.get(0).getAttrValue() : "";
            if (HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag())) {
                // ????????? ???????????????????????????
                String flag = judgeDowngrade(tenantId, dto, hmeEoJobSnSingleBasic);
                if (StringUtils.equals("ND", flag)) {
                    if (HmeConstants.ConstantValue.YES.equals(jcRetest)) {
                        // ???Y ???????????? ???????????????????????? ??????????????? ?????????????????????
                        boolean nearNormalProcess = hmeEoJobSnCommonService.isNearNormalProcess(tenantId, dto.getEoId(), dto.getWorkcellId());
                        if (nearNormalProcess) {
                            outSiteAction = HmeConstants.OutSiteAction.COMPLETE;
                        } else {
                            outSiteAction = HmeConstants.OutSiteAction.REWORK;
                        }
                    }
                } else {
                    outSiteAction = HmeConstants.OutSiteAction.COMPLETE;
                }
            } else {
                // ?????????
                if (HmeConstants.ConstantValue.YES.equals(jcRetest)) {
                    // ???Y ???????????? ???????????????????????? ??????????????? ?????????????????????
                    boolean nearNormalProcess = hmeEoJobSnCommonService.isNearNormalProcess(tenantId, dto.getEoId(), dto.getWorkcellId());
                    if (nearNormalProcess) {
                        outSiteAction = HmeConstants.OutSiteAction.COMPLETE;
                    } else {
                        outSiteAction = HmeConstants.OutSiteAction.REWORK;
                    }
                }
            }
        } else {
            if (HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag())) {
                if (HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag())) {
                    outSiteAction = HmeConstants.OutSiteAction.REWORK_COMPLETE;
                } else {
                    // ????????? ??????????????????????????????????????? ?????????????????? ??????????????????
                    boolean nearNormalProcess = hmeEoJobSnCommonService.isNearNormalProcess(tenantId, dto.getEoId(), dto.getWorkcellId());
                    if (nearNormalProcess) {
                        outSiteAction = HmeConstants.OutSiteAction.COMPLETE;
                    } else {
                        outSiteAction = HmeConstants.OutSiteAction.REWORK;
                    }
                }
            } else {
                // ???????????? ????????? ??????????????? ????????? ????????? ?????????????????????????????????
                if (HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag())) {
                    // ???????????? ??????????????????
                    outSiteAction = HmeConstants.OutSiteAction.REWORK_COMPLETE;
                } else {
                    // ??? ?????????????????????
                    outSiteAction = HmeConstants.OutSiteAction.COMPLETE;
                }
            }
        }
        return outSiteAction;
    }

    private String judgeDowngrade(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        String flag = "N";
        if(!"HME_EO_JOB_SN_172".equals(dto.getErrorCode()) || CollectionUtils.isEmpty(dto.getProcessNcDetailList())){
            return flag;
        }

        HmeEoJobDataRecordVO2 hmeEoJobDataRecordVO2 = hmeEoJobSnSingleBasic.getNcJobDataRecordMap().getOrDefault(dto.getJobId() , null);
        if(Objects.isNull(hmeEoJobDataRecordVO2)){
            return flag;
        }

        StringBuilder ncCodes = new StringBuilder();
        boolean singleFlag = true;
        String reworkFlag = HmeConstants.ConstantValue.NO;
        String downGradeFlag = HmeConstants.ConstantValue.NO;
        String ncGroupId = "";
        for (HmeProcessNcDetailVO2 item : dto.getProcessNcDetailList()
        ) {
            if(StringUtils.isNotBlank(item.getNcCode())){
                if(ncCodes.length() == 0){
                    ncCodes.append(String.join(";",item.getNcCode()));
                    ncGroupId = item.getNcGroupId();
                }else{
                    ncCodes.append(";" + String.join(";",item.getNcCode()));
                    singleFlag = false;
                }
            }
            reworkFlag = HmeConstants.ConstantValue.YES.equals(reworkFlag) ? reworkFlag : item.getReworkFlag();
            downGradeFlag = HmeConstants.ConstantValue.YES.equals(downGradeFlag) ? downGradeFlag : item.getDowngradeFlag();
        }

        if(StringUtils.isBlank(ncGroupId)){
            //???${1}???????????????????????????,?????????????????????
            throw new MtException("HME_EO_JOB_SN_197", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_197", "HME", ncCodes.toString()));
        }

        if(ncCodes.length() > 0){
            //R:???????????? N:???????????? ND:???????????? + ?????? ZD:????????????????????????
            if(singleFlag){
                if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                    flag = "R";
                }else if(HmeConstants.ConstantValue.YES.equals(downGradeFlag)){
                    flag = "ND";
                } else {
                    flag = "ZD";
                }
            }else{
                if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                    flag = "R";
                }
            }
        }
        return flag;
    }

    /**
     *
     * @Description ???????????????
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:10
     * @param tenantId ??????ID
     * @param hmeEoJobSnSingleBasic ??????
     * @param dto ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backFlushMaterialOutSite(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite tenantId={},hmeEoJobSnSingleBasic={},dto={}", tenantId, hmeEoJobSnSingleBasic, dto);
        //?????????????????????????????????????????????
        List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnSingleBasic.getBackFlushBomComponentList();
        if (CollectionUtils.isEmpty(backFlushBomComponentList)) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite Return:EO?????????????????????");
            return;
        }

        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
        HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity();

        //???????????????????????????????????????Y?????????????????????
        if (HmeConstants.ConstantValue.YES.equals(wo.getBackflushNotFlag())) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite Return:???????????????BackflushNotFlag???Y wo={}", wo);
            return;
        }

        //eo????????????wo????????????????????????
        if (CollectionUtils.isEmpty(eo.getBomComponentList())
                || CollectionUtils.isEmpty(wo.getBomComponentList())) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite Return:EO/WO???????????? " +
                    "eoComponentList={},woComponentList={}", eo.getBomComponentList(), wo.getBomComponentList());
            return;
        }

        //????????????????????????ID
        List<String> backFlushBomComponentIdList = backFlushBomComponentList.stream()
                .map(MtBomComponent::getBomComponentId).collect(Collectors.toList());

        //????????????????????????EO??????
        List<HmeBomComponentVO> allReleaseEoComponentList = eo.getBomComponentList().stream()
                .filter(item -> backFlushBomComponentIdList.contains(item.getBomComponentId())).collect(Collectors.toList());
        List<String> releaseEoComponentIdList = allReleaseEoComponentList.stream()
                .map(HmeBomComponentVO::getBomComponentId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(allReleaseEoComponentList)) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite Return:?????????????????????EO?????? " +
                    "eoComponentList={},backFlushBomComponentIdList={}", eo.getBomComponentList(), backFlushBomComponentIdList);
            return;
        }

        //?????????????????????ID
        List<String> backFlushMaterialIdList = backFlushBomComponentList.stream()
                .filter(item -> releaseEoComponentIdList.contains(item.getBomComponentId()))
                .map(MtBomComponent::getMaterialId).collect(Collectors.toList());
        Map<String, List<String>> backFlushMaterialMap = backFlushMaterialIdList.stream().collect(Collectors.groupingBy(materialId -> materialId));
        backFlushMaterialMap.forEach((mKey, mValue) -> {
            if (mValue.size() > 1) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mKey);
                throw new MtException("HME_EO_JOB_SN_258", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_258", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : ""));
            }
        });
        //??????????????????
        long startDate = System.currentTimeMillis();
        MtModLocator backFlushLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.BACKFLUSH,dto.getWorkcellId());
        log.info("=================================>????????????-??????????????????????????????-selectLocator????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //??????????????????
        startDate = System.currentTimeMillis();
        List<HmeMaterialLotVO3> backFlushMaterialLotList = hmeEoJobSnLotMaterialMapper
                .batchQueryBackFlushMaterialLot(tenantId, backFlushMaterialIdList, backFlushLocator.getLocatorId());
        log.info("=================================>????????????-??????????????????????????????-batchQueryBackFlushMaterialLot????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if (CollectionUtils.isEmpty(backFlushMaterialLotList)) {
            List<MtMaterialVO> mtMaterialVOList = mtMaterialRepository.materialPropertyBatchGet(tenantId, backFlushMaterialIdList);
            List<String> backFlushMaterialCodeList = mtMaterialVOList.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
            //????????????????????????????????????{1}??????????????????
            throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_087", "HME" , String.join(",", backFlushMaterialCodeList)));
        }

        //??????????????????
        startDate = System.currentTimeMillis();
        MtModLocator warehouse =  hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,backFlushLocator.getLocatorId());
        log.info("=================================>????????????-??????????????????????????????-queryAreaLocator????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //????????????????????????
        startDate = System.currentTimeMillis();
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());
        log.info("=================================>????????????-??????????????????????????????-selectByPrimaryKey????????????"+(System.currentTimeMillis() - startDate) + "??????");
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
        startDate = System.currentTimeMillis();
        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);
        log.info("=================================>????????????-??????????????????????????????-wmsTransactionTypeRepository.selectOne????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //??????BOM??????
        BigDecimal bomPrimaryQty = BigDecimal.valueOf(eo.getBomPrimaryQty());

        //???????????????WO??????????????????
        Map<String, List<MtWorkOrderComponentActual>> woComponentActualMap = new HashMap<>();
        List<String> backFlushWoBomComponentIdList = wo.getBomComponentList().stream().map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(backFlushWoBomComponentIdList)) {
            startDate = System.currentTimeMillis();
            SecurityTokenHelper.close();
            List<MtWorkOrderComponentActual> woComponentActualList = mtWorkOrderComponentActualRepository
                    .selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, wo.getWorkOrderId())
                                    .andIn(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, backFlushWoBomComponentIdList))
                            .build());
            log.info("=================================>????????????-??????????????????????????????-mtWorkOrderComponentActualRepository\n" +
                    "                    .selectByCondition????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if (CollectionUtils.isNotEmpty(woComponentActualList)) {
                woComponentActualMap = woComponentActualList.stream()
                        .collect(Collectors.groupingBy(MtWorkOrderComponentActual::getBomComponentId));
            }
        }

        log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite ??????????????????[{}]", allReleaseEoComponentList.size());

        //?????????????????????????????????
        List<MtAssembleProcessActualVO11> materialInfo = new ArrayList<>();
        Map<String, MtAssembleProcessActualVO17> eoAssembleProcessActualMap = new HashMap<>();
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList = new ArrayList<>();
        //??????????????????API??????
        List<MtMaterialLotVO32> mtMaterialList = new ArrayList<>();
        for (HmeBomComponentVO releaseEoComponent : allReleaseEoComponentList) {
            //??????????????????????????????????????????
            //V20210318 modify by penglin.sui for hui.ma ??????????????????0?????????
            List<HmeMaterialLotVO3> releaseMaterialLotList = backFlushMaterialLotList.stream()
                    .filter(item -> releaseEoComponent.getBomComponentMaterialId().equals(item.getMaterialId())
                    && (BigDecimal.valueOf(item.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) > 0))
                    .sorted(Comparator.comparing(HmeMaterialLotVO3::getCreationDate)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(releaseMaterialLotList)) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(releaseEoComponent.getBomComponentMaterialId());
                //????????????????????????????????????{1}??????????????????
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            //????????????????????????/??????????????????
            BigDecimal materialUomQty = BigDecimal.valueOf(materialLot.getPrimaryUomQty());
            BigDecimal remainQty = BigDecimal.valueOf(releaseEoComponent.getBomComponentQty())
                    .multiply(materialUomQty).divide(bomPrimaryQty, 4 , BigDecimal.ROUND_HALF_UP);

            //??????????????????????????????
            double releaseMaterialLotPrimaryQtySum = releaseMaterialLotList.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
            if(BigDecimal.valueOf(releaseMaterialLotPrimaryQtySum).compareTo(remainQty) < 0){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(releaseEoComponent.getBomComponentMaterialId());
                //????????????????????????????????????{1}??????????????????
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            //??????wo??????????????????eo???????????????????????????
            HmeBomComponentVO releaseWoComponent = null;
            List<HmeBomComponentVO> releaseWoComponentList = wo.getBomComponentList().stream()
                    .filter(item -> item.getBomComponentMaterialId().equals(releaseEoComponent.getBomComponentMaterialId())
                            && item.getBomComponentLineNumber().equals(releaseEoComponent.getBomComponentLineNumber()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(releaseWoComponentList)) {
                releaseWoComponent = releaseWoComponentList.get(0);
            }
            if (Objects.isNull(releaseWoComponent)) {
                log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite continue:???????????? wo??????????????????eo??????????????????????????? releaseEoComponent={}", releaseEoComponent);
                continue;
            }

            //?????????????????????????????????????????????????????????0??????
            BigDecimal woDemandQty = StringUtils.isBlank(releaseWoComponent.getWoBomComponentDemandQty())
                    ? BigDecimal.ZERO : new BigDecimal(releaseWoComponent.getWoBomComponentDemandQty());
            if(woDemandQty.compareTo(BigDecimal.ZERO) <= 0){
                log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite continue:??????????????????????????????0 releaseWoComponent={}", releaseWoComponent);
                continue;
            }

            //??????WO????????????????????????
            if (woComponentActualMap.containsKey(releaseWoComponent.getBomComponentId())) {
                double assembleQty = woComponentActualMap.get(releaseWoComponent.getBomComponentId())
                        .stream().mapToDouble(MtWorkOrderComponentActual::getAssembleQty).sum();
                if ((BigDecimal.valueOf(assembleQty).add(remainQty)).compareTo(woDemandQty) > 0) {
                    log.info("<====== HmeEoJobSnSingleServiceImpl.backFlushMaterialOutSite continue:??????WO?????????????????????????????????" +
                            " releaseWoComponent={}, assembleQty={}, remainQty={}", releaseWoComponent, assembleQty, remainQty);
                    continue;
                }
            }

            log.info("<====== HmeEoJobSnSingleServiceImpl.batchBackFlushMaterialOutSite ???ID[{}],???????????????[{}]", releaseWoComponent.getBomComponentId(), remainQty);

            List<MtMaterialLotVO33> mtMaterialLotVO33List = new ArrayList<>();
            Long sequence = 0L;
            BigDecimal trxPrimaryUomQty = BigDecimal.ZERO;

            //????????????????????????
            for (HmeMaterialLotVO3 releaseMaterialLot : releaseMaterialLotList) {
                //????????????
                BigDecimal currentQty = remainQty;
                if (remainQty.compareTo(BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty())) >= 0) {
                    currentQty = BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty());
                }
                remainQty = remainQty.subtract(currentQty);

                //API-???????????????
//                MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
//                mtMaterialLotVO1.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
//                mtMaterialLotVO1.setPrimaryUomId(releaseMaterialLot.getPrimaryUomId());
//                mtMaterialLotVO1.setTrxPrimaryUomQty(currentQty.doubleValue());
//                mtMaterialLotVO1.setEventRequestId(eventRequestId);
//                if (StringUtils.isNotEmpty(releaseMaterialLot.getSecondaryUomId())) {
//                    mtMaterialLotVO1.setSecondaryUomId(releaseMaterialLot.getSecondaryUomId());
//                    mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
//                }
//                startDate = System.currentTimeMillis();
//                mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
//                log.info("=================================>????????????-??????????????????????????????-mtMaterialLotRepository.materialLotConsume????????????"+(System.currentTimeMillis() - startDate) + "??????");

                //?????????????????????
                MtMaterialLotVO33 mtMaterialLotVO33 = new MtMaterialLotVO33();
                mtMaterialLotVO33.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                mtMaterialLotVO33.setConsumeFlag(HmeConstants.ConstantValue.YES);
                mtMaterialLotVO33.setSequence(sequence++);
                mtMaterialLotVO33List.add(mtMaterialLotVO33);
                trxPrimaryUomQty = trxPrimaryUomQty.add(currentQty);

                //??????API-??????????????????-????????????????????????
                MtAssembleProcessActualVO11 assembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                assembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                assembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                assembleProcessActualVO11.setBomComponentId(releaseEoComponent.getBomComponentId());
                assembleProcessActualVO11.setMaterialId(releaseMaterialLot.getMaterialId());
                assembleProcessActualVO11.setTrxAssembleQty(currentQty.doubleValue());
                assembleProcessActualVO11.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                assembleProcessActualVO11.setLocatorId(releaseMaterialLot.getLocatorId());
                materialInfo.add(assembleProcessActualVO11);

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
                objectTransactionVO.setWorkOrderNum(wo.getWorkOrderNum());
                objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                objectTransactionVO.setProdLineCode(wo.getProdLineCode());
                objectTransactionVO.setMoveType(Objects.isNull(wmsTransactionType) ? "261" : wmsTransactionType.getMoveType());
                objectTransactionVO.setBomReserveNum(StringUtils.trimToEmpty(releaseEoComponent.getReserveNum()));
                objectTransactionVO.setBomReserveLineNum(String.valueOf(releaseEoComponent.getBomComponentLineNumber()));
                objectTransactionVO.setSoNum(releaseMaterialLot.getSoNum());
                objectTransactionVO.setSoLineNum(releaseMaterialLot.getSoLineNum());
                objectTransactionRequestList.add(objectTransactionVO);

                //???????????????????????????
                HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                hmeEoJobSnLotMaterial.setTenantId(tenantId);
                hmeEoJobSnLotMaterial.setMaterialType("BACKFLUSH");
                hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobSnLotMaterial.setJobId(hmeEoJobSnEntity.getJobId());
                hmeEoJobSnLotMaterial.setMaterialId(releaseMaterialLot.getMaterialId());
                hmeEoJobSnLotMaterial.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                hmeEoJobSnLotMaterial.setReleaseQty(currentQty);
                hmeEoJobSnLotMaterial.setIsReleased(1);
                hmeEoJobSnLotMaterial.setLocatorId(releaseMaterialLot.getLocatorId());
                hmeEoJobSnLotMaterial.setLotCode(releaseMaterialLot.getLot());
                hmeEoJobSnLotMaterial.setProductionVersion(releaseMaterialLot.getMaterialVersion());
                hmeEoJobSnLotMaterial.setVirtualFlag(HmeConstants.ConstantValue.NO);
                hmeEoJobSnLotMaterial.setObjectVersionNumber(1L);
                hmeEoJobSnLotMaterial.setCreatedBy(hmeEoJobSnSingleBasic.getUserId());
                hmeEoJobSnLotMaterial.setCreationDate(hmeEoJobSnSingleBasic.getCurrentDate());
                hmeEoJobSnLotMaterial.setLastUpdatedBy(hmeEoJobSnSingleBasic.getUserId());
                hmeEoJobSnLotMaterial.setLastUpdateDate(hmeEoJobSnSingleBasic.getCurrentDate());
                hmeEoJobSnLotMaterialList.add(hmeEoJobSnLotMaterial);

                //V20210318 modify by penglin.sui for hui.ma ???????????????????????????
                releaseMaterialLot.setPrimaryUomQty((BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty()).subtract(currentQty)).doubleValue());

                if (remainQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }

            if(CollectionUtils.isNotEmpty(mtMaterialLotVO33List)) {
                MtMaterialLotVO32 mtMaterialLotVO32 = new MtMaterialLotVO32();
                mtMaterialLotVO32.setMaterialId(releaseEoComponent.getBomComponentMaterialId());
                mtMaterialLotVO32.setMtMaterialLotVO33List(mtMaterialLotVO33List);
                mtMaterialLotVO32.setTrxPrimaryUomQty(trxPrimaryUomQty.doubleValue());
                mtMaterialList.add(mtMaterialLotVO32);
            }
        }

        //??????API-????????????????????????
        if (CollectionUtils.isNotEmpty(materialInfo)) {
            MtAssembleProcessActualVO17 assembleProcessActualVO17 = new MtAssembleProcessActualVO17();
            assembleProcessActualVO17.setAssembleRouterType(HmeConstants.ConstantValue.NO);
            assembleProcessActualVO17.setEoId(eo.getEoId());
            assembleProcessActualVO17.setRouterId(eo.getRouterId());
            assembleProcessActualVO17.setRouterStepId(hmeEoJobSnEntity.getEoStepId());
            assembleProcessActualVO17.setMaterialInfo(materialInfo);
            eoAssembleProcessActualMap.put(eo.getEoId(), assembleProcessActualVO17);

            MtAssembleProcessActualVO16 assembleProcessActualVO16 = new MtAssembleProcessActualVO16();
            assembleProcessActualVO16.setOperationId(dto.getOperationId());
            assembleProcessActualVO16.setWorkcellId(dto.getWorkcellId());
            assembleProcessActualVO16.setParentEventId(eventId);
            assembleProcessActualVO16.setEventRequestId(eventRequestId);
            assembleProcessActualVO16.setOperationBy(String.valueOf(hmeEoJobSnSingleBasic.getUserId()));
            assembleProcessActualVO16.setEoInfo(new ArrayList<>(eoAssembleProcessActualMap.values()));
            assembleProcessActualVO16.setLocatorId(backFlushLocator.getLocatorId());
            if (Objects.nonNull(mtWkcShift)) {
                assembleProcessActualVO16.setShiftCode(mtWkcShift.getShiftCode());
                if (Objects.nonNull(mtWkcShift.getShiftDate())) {
                    assembleProcessActualVO16.setShiftDate(mtWkcShift.getShiftDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
            }
            startDate = System.currentTimeMillis();
            mtAssembleProcessActualRepository.componentAssembleBatchProcess(tenantId, assembleProcessActualVO16);
            log.info("=================================>????????????-??????????????????????????????-componentAssembleBatchProcess????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }

        if(CollectionUtils.isNotEmpty(mtMaterialList)) {
            MtMaterialLotVO31 mtMaterialLotVO31 = new MtMaterialLotVO31();
            mtMaterialLotVO31.setEventRequestId(eventRequestId);
            mtMaterialLotVO31.setMtMaterialList(mtMaterialList);
            mtMaterialLotVO31.setAllConsume(HmeConstants.ConstantValue.NO);
            startDate = System.currentTimeMillis();
            mtMaterialLotRepository.sequenceLimitMaterialLotBatchConsumeForNew(tenantId, mtMaterialLotVO31);
            log.info("=================================>????????????-???????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
        }

        //??????????????????
        if (CollectionUtils.isNotEmpty(objectTransactionRequestList)) {
            startDate = System.currentTimeMillis();
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
            log.info("=================================>????????????-??????????????????????????????-objectTransactionSync????????????"+(System.currentTimeMillis() - startDate) + "??????");
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
            startDate = System.currentTimeMillis();
            hmeEoJobSnLotMaterialRepository.myBatchInsert(hmeEoJobSnLotMaterialList);
            log.info("=================================>????????????-??????????????????????????????-?????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }
    }

    @Override
    public HmeEoJobSnSingleVO2 releaseValidate(Long tenantId, HmeEoJobSnSingleDTO dto) {
        if(CollectionUtils.isEmpty(dto.getComponentList())){
            //??????????????????????????????
            throw new MtException("HME_EO_JOB_SN_129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_129", "HME"));
        }

        //????????????????????????
        List<String> materialIdList = dto.getComponentList().stream().map(HmeEoJobSnBatchVO4::getMaterialId).collect(Collectors.toList());
        List<String> materialCodeList = new ArrayList<>();
        for (HmeEoJobSnBatchVO4 item:dto.getComponentList()
        ) {
            long count = materialIdList.stream().filter(item2 -> item2.equals(item.getMaterialId())).count();
            if(count > 1 && !materialCodeList.contains(item.getMaterialCode())){
                materialCodeList.add(item.getMaterialCode());
            }
        }
        if(CollectionUtils.isNotEmpty(materialCodeList)){
            //??????????????????${1}??????,?????????BOM!
            throw new MtException("HME_EO_JOB_SN_157", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_157", "HME", StringUtils.join(materialCodeList.toArray(), ",")));
        }

        List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(hmeEoJobSnBatchVO4List)){
            //??????????????????????????????
            throw new MtException("HME_EO_JOB_SN_129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_129", "HME"));
        }

        //V20210308 modify by penglin.sui for hui.ma ????????????????????????????????????COUNT???????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getReworkFlag())){
            List<HmeEoJobSnBatchVO4> uomComponentList = hmeEoJobSnBatchVO4List.stream().filter(item -> HmeConstants.UomType.COUNT.equals(item.getUomType()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(uomComponentList)){
                StringBuilder stringBuilder = new StringBuilder();
                for (HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4:uomComponentList
                     ) {
                    if(hmeEoJobSnBatchVO4.getWillReleaseQty().compareTo(BigDecimal.ZERO) <= 0 || hmeEoJobSnBatchVO4.getWillReleaseQty().stripTrailingZeros().scale() > 0) {
                        if (stringBuilder.length() == 0) {
                            stringBuilder.append(hmeEoJobSnBatchVO4.getUomCode());
                        }else{
                            stringBuilder.append("," + hmeEoJobSnBatchVO4.getUomCode());
                        }
                    }
                }
                if(stringBuilder.length() > 0) {
                    //???????????????${1}??????????????????,??????????????????,?????????!
                    throw new MtException("HME_EO_JOB_SN_189", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_189", "HME", stringBuilder.toString()));
                }
            }
        }

        if(StringUtils.isBlank(dto.getSnLineDto().getOperationId())){
            //????????????WKC??????????????????
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        }

        if(StringUtils.isBlank(dto.getSnLineDto().getEoStepId())){
            // SN?????????????????????,?????????!
            throw new MtException("HME_EO_JOB_SN_146", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_146", "HME"));
        }

        //??????EO????????????????????????????????????2?????????
//        List<HmeEoJobSnBatchVO4> normalComponentList = dto.getComponentList().stream().filter(item -> !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
//                && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())).collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(normalComponentList)) {
//            List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List2 = normalComponentList.stream().filter(item -> item.getReleasedQty().compareTo(BigDecimal.ZERO) > 0
//                    || (item.getReleasedQty().compareTo(BigDecimal.ZERO) <= 0 && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()) && item.getWillReleaseQty().compareTo(BigDecimal.ZERO) > 0)
//            ).collect(Collectors.toList());
//            Map<String, List<HmeEoJobSnBatchVO4>> componentReleaseMap = new HashMap<>();
//            if (CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4List2)) {
//                componentReleaseMap = hmeEoJobSnBatchVO4List2.stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
//                componentReleaseMap.entrySet().forEach(entry -> {
//                    if (entry.getValue().size() > 2) {
//                        //????????????????????????????????????????????????????????????,?????????
//                        throw new MtException("HME_EO_JOB_SN_130", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_130", "HME"));
//                    }
//                });
//            }
//        }
        //????????????????????????
        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> StringUtils.isNotBlank(item.getBomComponentId())).collect(Collectors.toList());
        List<String> bomComponentIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(componentList)) {
            bomComponentIdList = componentList.stream().map(HmeEoJobSnBatchVO4::getBomComponentId).collect(Collectors.toList());
        }

        //???????????????
//        List<HmeEoJobSnBatchVO4> substituteList = dto.getComponentList().stream().filter(item -> StringUtils.isBlank(item.getBomComponentId())).collect(Collectors.toList());
//        List<String> substituteMaterialIdList = new ArrayList<>();
//        if(CollectionUtils.isNotEmpty(substituteList)) {
//            substituteMaterialIdList = componentList.stream().map(HmeEoJobSnBatchVO4::getBomComponentId).collect(Collectors.toList());
//        }
        long startDate = System.currentTimeMillis();
        //??????BOM????????????
        //Map<String,String> bomExtendAttrMap = new HashMap<>();
        Map<String, MtExtendAttrVO1> bomExtendAttrMap2 = new HashMap<>();
        if(CollectionUtils.isNotEmpty(bomComponentIdList)) {
            List<String> bomAttrNameList = new ArrayList<>();
            bomAttrNameList.add(HmeConstants.BomComponentExtendAttr.SAP_REQUIREMENT_QTY);
            bomAttrNameList.add(HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG);
            startDate = System.currentTimeMillis();
            List<MtExtendAttrVO1> bomExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_bom_component_attr", "BOM_COMPONENT_ID"
                    , bomComponentIdList, bomAttrNameList);
            log.info("<=============HmeEoJobSnBatchController.release ????????????????????????BOM?????????????????????=============>" + (System.currentTimeMillis() - startDate) + "ms");
            if (CollectionUtils.isNotEmpty(bomExtendAttrVO1List)) {
                //bomExtendAttrMap = bomExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), MtExtendAttrVO1::getAttrValue));
                bomExtendAttrMap2 = bomExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), t -> t));
            }
        }

        //????????????????????????
//        List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = new ArrayList<>();
//        Map<String,BigDecimal> workOrderComponentActualMap = new HashMap<>();
//        if(CollectionUtils.isNotEmpty(bomComponentIdList)) {
//            startDate = System.currentTimeMillis();
//            mtWorkOrderComponentActualList = hmeEoJobSnBatchMapper.selectWorkOrderComponentActual(tenantId,dto.getSnLineDto().getWorkOrderId(),new ArrayList<>(),bomComponentIdList);
//            log.info("<=============HmeEoJobSnBatchController.release ??????????????????????????????????????????-BOMCOMPONENT?????????=============>" + (System.currentTimeMillis() - startDate) + "ms");
//            if(CollectionUtils.isNotEmpty(mtWorkOrderComponentActualList)) {
//                workOrderComponentActualMap = mtWorkOrderComponentActualList.stream().collect(Collectors.toMap(MtWorkOrderComponentActual::getBomComponentId, item -> BigDecimal.valueOf(item.getAssembleQty()).add(BigDecimal.valueOf(item.getScrappedQty()))));
//            }
//        }
//        List<MtWorkOrderComponentActual> mtWorkOrderSubstituteActualList = new ArrayList<>();
//        Map<String,BigDecimal> workOrderSubstituteActualMap = new HashMap<>();
//        if(CollectionUtils.isNotEmpty(substituteMaterialIdList)) {
//            startDate = System.currentTimeMillis();
//            mtWorkOrderSubstituteActualList = hmeEoJobSnBatchMapper.selectWorkOrderComponentActual(tenantId,dto.getSnLineDto().getWorkOrderId(),substituteMaterialIdList,new ArrayList<>());
//            log.info("<=============HmeEoJobSnBatchController.release ??????????????????????????????????????????-MATERIAL?????????=============>" + (System.currentTimeMillis() - startDate) + "ms");
//            if(CollectionUtils.isNotEmpty(mtWorkOrderSubstituteActualList)) {
//                workOrderSubstituteActualMap = mtWorkOrderSubstituteActualList.stream().collect(Collectors.toMap(MtWorkOrderComponentActual::getMaterialId, item -> BigDecimal.valueOf(item.getAssembleQty()).add(BigDecimal.valueOf(item.getScrappedQty()))));
//            }
//        }

        //????????????,????????????????????????
        if(!HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getReworkFlag())) {
            Map<String, List<HmeEoJobSnBatchVO4>> componentOverReleaseMap = dto.getComponentList().stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
            componentOverReleaseMap.entrySet().forEach(entry -> {
                //????????????????????????????????????????????????
                List<HmeEoJobSnBatchVO4> qtyAlterFlagList = entry.getValue().stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getQtyAlterFlag()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isEmpty(qtyAlterFlagList)){
                    BigDecimal willReleaseQtySum = entry.getValue().stream().map(HmeEoJobSnBatchVO4::getWillReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal releasedQtySum = entry.getValue().stream().map(HmeEoJobSnBatchVO4::getReleasedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    List<HmeEoJobSnBatchVO4> componentList2 = entry.getValue().stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
                    BigDecimal requirementQtySum = componentList2.get(0).getRequirementQty();
                    if (willReleaseQtySum.add(releasedQtySum).compareTo(requirementQtySum) > 0) {
                        log.info("<============?????? + ?????? < ??????==========>" + willReleaseQtySum + "-" + releasedQtySum + "-" + requirementQtySum);
                        //??????${1}??????????????????????????????,?????????
                        throw new MtException("HME_EO_JOB_SN_131", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_131", "HME", componentList2.get(0).getMaterialCode()));
                    }
                }
            });
            // V20210730 modify by sanfeng.zhang for wenxin.zhang ????????????????????????????????????????????? ?????????????????????
            List<String> materialLotIds = new ArrayList<>();
            hmeEoJobSnBatchVO4List.stream().forEach(vo -> {
                if (CollectionUtils.isNotEmpty(vo.getMaterialLotList())) {
                    materialLotIds.addAll(vo.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getMaterialLotId).collect(Collectors.toList()));
                }
            });
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
            Map<String, List<MtMaterialLot>> mtMaterialLotMap = mtMaterialLots.stream().collect(Collectors.groupingBy(MtMaterialLot::getMaterialLotId));
            for (HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4:hmeEoJobSnBatchVO4List) {
                if (CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4.getMaterialLotList())) {
                    hmeEoJobSnBatchVO4.getMaterialLotList().forEach(mml -> {
                        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotMap.getOrDefault(mml.getMaterialLotId(), Collections.emptyList());
                        if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                            if (!HmeConstants.ConstantValue.YES.equals(mtMaterialLotList.get(0).getEnableFlag())) {
                                throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_WO_INPUT_0004", "HME", mtMaterialLotList.get(0).getMaterialLotCode()));
                            }
                            mml.setPrimaryUomQty(BigDecimal.valueOf(mtMaterialLotList.get(0).getPrimaryUomQty()));
                        }
                    });
                }
            }
        } else {
            //????????????????????????????????????
            for (HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4:hmeEoJobSnBatchVO4List
            ) {
                if(Objects.isNull(hmeEoJobSnBatchVO4.getWillReleaseQty())){
                    //????????????????????????????????????,?????????
                    throw new MtException("WMS_OUTSOURCE_PLATFORM_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_OUTSOURCE_PLATFORM_0008", "WMS"));
                }
                if(hmeEoJobSnBatchVO4.getWillReleaseQty().compareTo(BigDecimal.ZERO) <= 0){
                    //????????????????????????????????????,?????????
                    throw new MtException("WMS_OUTSOURCE_PLATFORM_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_OUTSOURCE_PLATFORM_0008", "WMS"));
                }

                BigDecimal materialLotQtySum = hmeEoJobSnBatchVO4.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getPrimaryUomQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                if(hmeEoJobSnBatchVO4.getWillReleaseQty().compareTo(materialLotQtySum) > 0){
                    //????????????????????????????????????,?????????!
                    throw new MtException("HME_EO_JOB_SN_153", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_153", "HME"));
                }
            }
        }

        String snLineBomId = dto.getSnLineDto().getBomId();
        //???????????????BOM??????
        List<String> finalMaterialLotIdList = new ArrayList<>();
        List<String> finalMaterialLotIdList2 = new ArrayList<>();
        dto.getComponentList().forEach(item -> {
            List<HmeEoJobSnBatchVO6> releaseMaterialLotList;
            if(CollectionUtils.isNotEmpty(item.getMaterialLotList())) {
                releaseMaterialLotList = item.getMaterialLotList().stream().filter(item2 -> HmeConstants.ConstantValue.ONE.equals(item2.getIsReleased()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(releaseMaterialLotList)) {
                    List<String> releaseMaterialLotIdList = releaseMaterialLotList.stream().map(HmeEoJobSnBatchVO6::getMaterialLotId).distinct()
                            .collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(releaseMaterialLotIdList)){
                        finalMaterialLotIdList.addAll(releaseMaterialLotIdList);
                        if(HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())) {
                            finalMaterialLotIdList2.addAll(releaseMaterialLotIdList);
                        }
                    }
                }
            }

            //BOM??????
            if(StringUtils.isNotBlank(item.getBomId())) {
                if (!snLineBomId.equals(item.getBomId())) {
                    //EO??????????????????BOM?????????,?????????!
                    throw new MtException("HME_EO_JOB_SN_145", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_145", "HME"));
                }
            }
        });

        Map<String, String> materialLotExtendAttrMap = new HashMap<>();
        Map<String,List<HmeMaterialLotLabCode>> hmeMaterialLotLabCodeMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(finalMaterialLotIdList)) {
            // 20211111 add by sanfeng.zhang for yiming.zhang ?????? ???????????? ???????????????????????????????????????????????????(??????????????????????????????????????? ???????????????????????????????????????)
//            if (HmeConstants.ConstantValue.YES.equals(dto.getIsFirstProcess()) && !HmeConstants.ConstantValue.YES.equals(dto.getIsPumpProcess())) {
//                // ?????????????????? ????????????????????????id
//                List<HmePreSelectionMaterialVO> preSelectionMaterialList = hmeMaterialLotLoadMapper.queryPreSelectionMaterialList(tenantId, finalMaterialLotIdList);
//                Map<String, String> preSelectionMaterialMap = preSelectionMaterialList.stream().collect(Collectors.toMap(HmePreSelectionMaterialVO::getMaterialLotId, HmePreSelectionMaterialVO::getMaterialId));
//                finalMaterialLotIdList.forEach(releaseCode -> {
//                    String preMaterialId = preSelectionMaterialMap.getOrDefault(releaseCode, "");
//                    if (!preMaterialId.equals(dto.getSnLineDto().getSnMaterialId())) {
//                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(preMaterialId);
//                        throw new MtException("HME_MATERIAL_LOT_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_MATERIAL_LOT_009", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "'"));
//                    }
//                });
//            }

            List<String> materialLotIdList = finalMaterialLotIdList;
            //????????????????????????
            materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
            List<String> materialLotAttrNameList = new ArrayList<>();
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.SO_NUM);
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.SO_LINE_NUM);
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG);
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.MATERIAL_VERSION);
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.LAB_CODE);
            startDate = System.currentTimeMillis();
            List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_lot_attr", "MATERIAL_LOT_ID"
                    , materialLotIdList, materialLotAttrNameList);
            log.info("<=============HmeEoJobSnBatchController.release ???????????????????????????????????????????????????=============>" + (System.currentTimeMillis() - startDate) + "ms");
            if (CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)) {
                materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), MtExtendAttrVO1::getAttrValue));
            }

            //V20210125 modify by penglin.sui for hui.ma ??????????????????????????????
            //??????????????????
            if(CollectionUtils.isNotEmpty(finalMaterialLotIdList2)) {
                List<String> snMaterialLotIdList = new ArrayList<>();
                snMaterialLotIdList.add(dto.getSnLineDto().getMaterialLotId());
                List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = hmeMaterialLotLabCodeMapper.batchSelectLabCode(tenantId, dto.getSnLineDto().getEoStepId(), snMaterialLotIdList);
                if (CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList)) {
                    hmeMaterialLotLabCodeMap = hmeMaterialLotLabCodeList.stream().collect(Collectors.groupingBy(e -> e.getMaterialLotId()));
                }
                List<String> materialLotIdList2 = finalMaterialLotIdList2.stream().distinct().collect(Collectors.toList());
                for (String materialLotId : materialLotIdList2
                ) {
                    String labCode = materialLotExtendAttrMap.get(fetchGroupKey2(materialLotId, HmeConstants.ExtendAttr.LAB_CODE));
                    if (StringUtils.isNotBlank(labCode)) {
                        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList1 = hmeMaterialLotLabCodeMap.getOrDefault(dto.getSnLineDto().getMaterialLotId(), new ArrayList<>());
                        if (CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList1)) {
                            List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList2 = hmeMaterialLotLabCodeList1.stream().filter(item -> labCode.equals(item.getLabCode()))
                                    .collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(hmeMaterialLotLabCodeList2)) {
                                //????????????????????????${1}??????????????????????????????${2}????????????,?????????
                                throw new MtException("HME_EO_JOB_SN_173", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_173", "HME", labCode, hmeMaterialLotLabCodeList1.get(0).getLabCode()));
                            }
                        }
                    }
                }
            }
        }

        //????????????????????????
        List<String> workOrderIdList = new ArrayList<>();
        workOrderIdList.add(dto.getSnLineDto().getWorkOrderId());
        List<String> woAttrNameList = new ArrayList<>();
        woAttrNameList.add("attribute1");
        woAttrNameList.add("attribute7");
        startDate = System.currentTimeMillis();
        List<MtExtendAttrVO1> woExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_work_order_attr", "WORK_ORDER_ID"
                , workOrderIdList, woAttrNameList);
        log.info("<=============HmeEoJobSnBatchController.release ???????????????????????????????????????????????????=============>" + (System.currentTimeMillis() - startDate) + "ms");
        Map<String, String> woExtendAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(woExtendAttrVO1List)) {
            woExtendAttrMap = woExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName, MtExtendAttrVO1::getAttrValue));
        }
        String woSoNum = woExtendAttrMap.getOrDefault("attribute1","");
        String woSoLineNum = woExtendAttrMap.getOrDefault("attribute7","");

        Map<String, MtExtendAttrVO1> finalBomExtendAttrMap2 = bomExtendAttrMap2;
        Map<String, String> finalMaterialLotExtendAttrMap = materialLotExtendAttrMap;
        finalBomExtendAttrMap2.forEach((k, v) -> {
            if("E".equals(v.getAttrValue())){
                List<HmeEoJobSnBatchVO4> currComponentList = hmeEoJobSnBatchVO4List.stream().filter(item -> StringUtils.isNotBlank(item.getBomComponentId())
                        && item.getBomComponentId().equals(v.getKeyId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(currComponentList)){
                    currComponentList.forEach(item -> {
                        if(CollectionUtils.isNotEmpty(item.getMaterialLotList())){
                            List<HmeEoJobSnBatchVO6> releasedMaterialLotList = item.getMaterialLotList().stream().filter(item2 -> HmeConstants.ConstantValue.ONE.equals(item2.getIsReleased()))
                                    .collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(releasedMaterialLotList)) {
                                releasedMaterialLotList.forEach(item2 -> {
                                    String materialLotSoNum = finalMaterialLotExtendAttrMap.getOrDefault(fetchGroupKey2(item2.getMaterialLotId(), "SO_NUM"), "");
                                    String materialLotSoLineNum = finalMaterialLotExtendAttrMap.getOrDefault(fetchGroupKey2(item2.getMaterialLotId(), "SO_LINE_NUM"), "");
                                    if (!(woSoNum + "-" + woSoLineNum).equals(materialLotSoNum + "-" + materialLotSoLineNum)) {
                                        //?????????????????????${1}???????????????????????????${2}????????????,?????????
                                        throw new MtException("HME_EO_JOB_SN_112", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "HME_EO_JOB_SN_112", "HME", woSoNum + "-" + woSoLineNum, materialLotSoNum + "-" + materialLotSoLineNum));
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        //????????????
        //V20210127 modify by penglin.sui for hui.ma ??????????????????
//        List<HmeEoJobSnBatchVO4> upgradeComponentList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getUpgradeFlag()))
//                .collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(upgradeComponentList)){
//            if(upgradeComponentList.size() > 1) {
//                MtRouterStep currentRouterStep =
//                        mtRouterStepRepository.routerStepGet(tenantId, dto.getSnLineDto().getEoStepId());
//                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,upgradeComponentList.get(1).getMaterialId());
//                // ?????????????????????${1}???SN??????????????????????????????????????????1???,??????????????????${2}??????????????????
//                throw new MtException("HME_EO_JOB_SN_027",
//                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_027", "HME", currentRouterStep.getStepName(),
//                                mtMaterial.getMaterialName()));
//            }
//        }

        //V20210218 modify by penglin.sui for hui.ma ??????????????????????????????????????????????????????????????????
        Map<String, List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap = new HashMap<>();
        Map<String, String> virtualJobMap = new HashMap<>();
        Map<String, HmeEoJobSn> virtualEoJobSnMap = new HashMap<>();
        //?????????
        List<HmeEoJobSnBatchVO4> virtualList = dto.getComponentList().stream().filter(item ->
                HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(virtualList)) {
            //????????? && ???????????????
            List<HmeEoJobSnBatchVO4> virtualComponentList = dto.getComponentList().stream().filter(item -> (
                    (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()))
                            || HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()))).collect(Collectors.toList());
            HmeEoJobSnBatchVO16 hmeEoJobSnBatchVO16 = hmeEoJobSnCommonService.selectVirtualComponent(tenantId, virtualComponentList);
            if (MapUtils.isNotEmpty(hmeEoJobSnBatchVO16.getVirtualJobMap())) {
                virtualJobMap = hmeEoJobSnBatchVO16.getVirtualJobMap();
            }
            if (MapUtils.isNotEmpty(hmeEoJobSnBatchVO16.getVirtualComponentMaterialLotMap())) {
                virtualComponentMaterialLotMap = hmeEoJobSnBatchVO16.getVirtualComponentMaterialLotMap();
            }else{
                //???????????????????????????
                throw new MtException("HME_EO_JOB_SN_066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_066", "HME"));
            }

            if (MapUtils.isNotEmpty(hmeEoJobSnBatchVO16.getVirtualEoJobSnMap())) {
                virtualEoJobSnMap = hmeEoJobSnBatchVO16.getVirtualEoJobSnMap();
            }

            //???????????????
            List<HmeEoJobSnBatchVO4> virtualComponentList2 = dto.getComponentList().stream().filter(item -> HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()))
                    .collect(Collectors.toList());

            for (HmeEoJobSnBatchVO4 virtual : virtualList
                 ) {
                List<HmeEoJobSnBatchVO4> currVirtualComponentList = virtualComponentList2.stream().filter(item -> item.getTopVirtualMaterialCode().equals(virtual.getMaterialCode()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isEmpty(currVirtualComponentList)){
                    //???????????????????????????
                    throw new MtException("HME_EO_JOB_SN_066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_066", "HME"));
                }

                List<String> eoVirtualComponentMaterialIdList = currVirtualComponentList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct()
                        .collect(Collectors.toList());

                List<String> virtualComponentMaterialIdList = new ArrayList<>();
                for(List<HmeEoJobSnBatchVO6> hmeEoJobSnBatchVO6List: virtualComponentMaterialLotMap.values()){
                    List<HmeEoJobSnBatchVO6> hmeEoJobSnBatchVO6List2 = hmeEoJobSnBatchVO6List.stream().filter(item -> item.getTopVirtualMaterialCode().equals(virtual.getMaterialCode()))
                            .collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO6List2)){
                        virtualComponentMaterialIdList.addAll(hmeEoJobSnBatchVO6List2.stream().map(HmeEoJobSnBatchVO6::getMaterialId).distinct().collect(Collectors.toList()));
                    }
                }

                if(CollectionUtils.isEmpty(virtualComponentMaterialIdList)){
                    //????????????????????????????????????EO??????????????????????????????,?????????!
                    throw new MtException("HME_EO_JOB_SN_186", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_186", "HME"));
                }

                virtualComponentMaterialIdList = virtualComponentMaterialIdList.stream().distinct().collect(Collectors.toList());

                if(eoVirtualComponentMaterialIdList.size() != virtualComponentMaterialIdList.size()){
                    //????????????????????????????????????EO??????????????????????????????,?????????!
                    throw new MtException("HME_EO_JOB_SN_186", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_186", "HME"));
                }

                if(!eoVirtualComponentMaterialIdList.containsAll(virtualComponentMaterialIdList)){
                    //????????????????????????????????????EO??????????????????????????????,?????????!
                    throw new MtException("HME_EO_JOB_SN_186", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_186", "HME"));
                }
            }
        }

        // ???????????? ??????????????????????????????
        List<String> virtualIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getComponentList())) {
            for (HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4 : dto.getComponentList()) {
                if (CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4.getVirtualIdList())) {
                    virtualIdList.addAll(hmeEoJobSnBatchVO4.getVirtualIdList());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(virtualIdList)) {
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadMapper.queryMaterialLotNcLoadByVirtualId(tenantId, virtualIdList);
            if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                StringBuilder stringBuilder = new StringBuilder();
                for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoadList
                     ) {
                    String location = String.valueOf((char) (64 + (Objects.isNull(hmeMaterialLotLoad.getLoadRow()) ? 1 : hmeMaterialLotLoad.getLoadRow()))) +
                            hmeMaterialLotLoad.getLoadColumn() + "-" + hmeMaterialLotLoad.getHotSinkCode();
                    if(stringBuilder.length() == 0){
                        stringBuilder.append(location);
                    }else{
                        stringBuilder.append("|" + location);
                    }
                }
                throw new MtException("HME_COS_NC_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_NC_001", "HME", stringBuilder.toString()));
            }
        }
        HmeEoJobSnSingleVO2 resultVO = new HmeEoJobSnSingleVO2();
        resultVO.setMaterialLotAttrMap(materialLotExtendAttrMap);
        resultVO.setHmeMaterialLotLabCodeMap(hmeMaterialLotLabCodeMap);
        resultVO.setVirtualJobMap(virtualJobMap);
        resultVO.setVirtualEoJobSnMap(virtualEoJobSnMap);
        resultVO.setVirtualComponentMaterialLotMap(virtualComponentMaterialLotMap);
        return resultVO;
    }

    @Override
    public List<HmeEoJobSnBatchVO4> wkcBindMaterialLotQuery(Long tenantId, String workcellId, String siteId) {
        List<HmeEoJobSnBatchVO4> resultVOList = new ArrayList<>();
        List<HmeEoJobSnSingleVO5> allVOList = new ArrayList<>();
        //????????????
        List<HmeEoJobSnSingleVO5> lotVOList = hmeEoJobLotMaterialMapper.selectWkcBindJobMaterial(tenantId, workcellId, siteId);
        if(CollectionUtils.isNotEmpty(lotVOList)) {
            lotVOList = lotVOList.stream().filter(item -> !"2".equals(item.getBackflushFlag()) && HmeConstants.MaterialTypeCode.LOT.equals(item.getProductionType()))
                    .collect(Collectors.toList());
            allVOList.addAll(lotVOList);
        }
        //????????????
        List<HmeEoJobSnSingleVO5> timeVOList = hmeEoJobTimeMaterialMapper.selectWkcBindJobMaterial(tenantId, workcellId, siteId);
        if(CollectionUtils.isNotEmpty(timeVOList)) {
            timeVOList = timeVOList.stream().filter(item -> !"2".equals(item.getBackflushFlag()) && HmeConstants.MaterialTypeCode.TIME.equals(item.getProductionType()))
                    .collect(Collectors.toList());
            allVOList.addAll(timeVOList);
        }

        if(CollectionUtils.isNotEmpty(allVOList)){
            String materialId = "";
            for (HmeEoJobSnSingleVO5 item : allVOList
            ) {
                if(StringUtils.isBlank(materialId) || !materialId.equals(item.getMaterialId())) {
                    HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4 = new HmeEoJobSnBatchVO4();
                    hmeEoJobSnBatchVO4.setProductionType(item.getProductionType());
                    hmeEoJobSnBatchVO4.setMaterialId(item.getMaterialId());
                    hmeEoJobSnBatchVO4.setMaterialCode(item.getMaterialCode());
                    hmeEoJobSnBatchVO4.setMaterialName(item.getMaterialName());
                    hmeEoJobSnBatchVO4.setUomId(item.getUomId());
                    hmeEoJobSnBatchVO4.setUomCode(item.getUomCode());
                    hmeEoJobSnBatchVO4.setUomName(item.getUomName());

                    List<HmeEoJobSnBatchVO6> materialLotList = new ArrayList<>();
                    List<HmeEoJobSnSingleVO5> lotVOList2 = allVOList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId()))
                            .collect(Collectors.toList());
                    String deadLineDate = "";
                    for (HmeEoJobSnSingleVO5 materialLot:lotVOList2
                         ) {
                        HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 = new HmeEoJobSnBatchVO6();
                        hmeEoJobSnBatchVO6.setJobMaterialId(materialLot.getJobMaterialId());
                        hmeEoJobSnBatchVO6.setWorkCellId(workcellId);
                        hmeEoJobSnBatchVO6.setMaterialId(materialLot.getMaterialId());
                        hmeEoJobSnBatchVO6.setMaterialType(materialLot.getProductionType());
                        hmeEoJobSnBatchVO6.setMaterialLotId(materialLot.getMaterialLotId());
                        hmeEoJobSnBatchVO6.setMaterialLotCode(materialLot.getMaterialLotCode());
                        hmeEoJobSnBatchVO6.setLot(materialLot.getLot());
                        hmeEoJobSnBatchVO6.setSupplierLot(materialLot.getSupplierLot());
                        hmeEoJobSnBatchVO6.setPrimaryUomQty(materialLot.getPrimaryUomQty());
                        hmeEoJobSnBatchVO6.setDeadLineDate(materialLot.getDeadLineDate());
                        materialLotList.add(hmeEoJobSnBatchVO6);
                        hmeEoJobSnBatchVO4.setMaterialLotList(materialLotList);

                        if (StringUtils.isBlank(materialLot.getDeadLineDate())) {
                            continue;
                        }
                        if (StringUtils.isBlank(deadLineDate)) {
                            deadLineDate = materialLot.getDeadLineDate();
                            continue;
                        }

                        if (deadLineDate.compareTo(materialLot.getDeadLineDate()) > 0) {
                            deadLineDate = materialLot.getDeadLineDate();
                        }
                    }
                    if (StringUtils.isNotBlank(deadLineDate)) {
                        hmeEoJobSnBatchVO4.setDeadLineDate(deadLineDate);
                    }

                    resultVOList.add(hmeEoJobSnBatchVO4);
                }

                materialId = item.getMaterialId();
            }
        }
        return resultVOList;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author penglin.sui
     * @date 2020/12/16 11:27
     * @param tenantId ??????ID
     * @param transactionTypeCodeList ????????????
     * @return Map<String,WmsTransactionTypeDTO>
     *
     */
    private Map<String, WmsTransactionTypeDTO> selectTransactionType(Long tenantId , List<String> transactionTypeCodeList){
        //????????????????????????
        List<WmsTransactionTypeDTO> wmsTransactionTypeList = new ArrayList<>();
        for (String transactionTypeCode:transactionTypeCodeList
             ) {
            WmsTransactionTypeDTO wmsTransactionTypeDTO = wmsTransactionTypeRepository.getTransactionType(tenantId,transactionTypeCode);
            wmsTransactionTypeList.add(wmsTransactionTypeDTO);
        }
        if(wmsTransactionTypeList.size() != transactionTypeCodeList.size()){
            //?????????????????????${1},?????????!
            throw new MtException("WMS_COST_CENTER_0066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0066", "WMS",""));
        }
        Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap = wmsTransactionTypeList.stream().collect(Collectors.toMap(WmsTransactionTypeDTO::getTransactionTypeCode, t -> t));
        return wmsTransactionTypeMap;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author penglin.sui
     * @date 2020/11/26 16:46
     * @param tenantId ??????ID
     * @param dto ??????
     * @return Map<String,HmeEoJobSnBatchVO15>
     *
     */
    private HmeEoJobSnBatchVO17 selectReleased(Long tenantId, HmeEoJobSnSingleDTO dto){
        HmeEoJobSnBatchVO17 resultVO = new HmeEoJobSnBatchVO17();
        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList();
        HmeEoJobSnVO vo = dto.getSnLineDto();
        List<String> eoIdList = new ArrayList<String>(1);
        eoIdList.add(vo.getEoId());
        List<String> routerStepIdList = new ArrayList<>(1);
        routerStepIdList.add(vo.getEoStepId());
        List<String> materialIdList = componentList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
        //??????????????????
        SecurityTokenHelper.close();
        List<MtEoComponentActual> mtEoComponentActualList = hmeEoJobSnBatchMapper.selectComponentAssemble(tenantId,eoIdList,routerStepIdList,materialIdList);
        if(CollectionUtils.isNotEmpty(mtEoComponentActualList)){
            Map<String, MtEoComponentActual> mtEoComponentActualMap = mtEoComponentActualList.stream().collect(Collectors.toMap(item -> fetchGroupKey3(item.getEoId(),item.getRouterStepId(),item.getMaterialId()), t -> t));
            resultVO.setMtEoComponentActualMap(mtEoComponentActualMap);
        }

        String workcellId = vo.getWorkcellId();
        List<String> jobIdList = new ArrayList<>(1);
        jobIdList.add(vo.getJobId());
        List<HmeEoJobSnBatchVO15> releasedRecordList = hmeEoJobSnBatchMapper.selectReleased(tenantId,workcellId,eoIdList,jobIdList);
        if(CollectionUtils.isNotEmpty(releasedRecordList)){
            Map<String, HmeEoJobSnBatchVO15> releasedRecordMap = releasedRecordList.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getEoId(),item.getMaterialId()), t -> t));
            resultVO.setReleasedRecordMap(releasedRecordMap);
        }
        return resultVO;
    }

    /**
     *
     * @Description ??????????????????????????????
     *
     * @author penglin.sui
     * @date 2020/11/30 20:09
     * @param tenantId ??????ID
     * @param dto ??????
     * @return HmeEoJobSnBatchVO17
     *
     */
    private HmeEoJobSnBatchVO17 selectWoReleased(Long tenantId, HmeEoJobSnSingleDTO dto){
        HmeEoJobSnBatchVO17 resultVO = new HmeEoJobSnBatchVO17();
        List<String> workOrderIdList = new ArrayList<>(1);
        workOrderIdList.add(dto.getSnLineDto().getWorkOrderId());
        List<String> materialIdList = dto.getComponentList().stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
        //??????????????????
        List<MtWorkOrderComponentActual> mtWoComponentActualList = hmeEoJobSnBatchMapper.selectWoComponentActual(tenantId,dto.getSnLineDto().getOperationId(),materialIdList,workOrderIdList);
        if(CollectionUtils.isNotEmpty(mtWoComponentActualList)){
            Map<String, MtWorkOrderComponentActual> mtWoComponentActualMap = mtWoComponentActualList.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getWorkOrderId(),item.getMaterialId()), t -> t));
            resultVO.setMtWoComponentActualMap(mtWoComponentActualMap);
        }
        return resultVO;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author penglin.sui
     * @date 2020/12/16 11:21
     * @param tenantId ??????ID
     * @param dto ??????
     * @return HmeEoJobSnSingleVO3
     *
     */
    @Override
    public HmeEoJobSnSingleVO3 releaseDataGet(Long tenantId, HmeEoJobSnSingleDTO dto){
        HmeEoJobSnSingleVO3 resultVO = new HmeEoJobSnSingleVO3();

        //????????????????????????
        List<String> materialIdList = dto.getComponentList().stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
        long startDate = System.currentTimeMillis();
        List<MtMaterialVO> mtMaterialList = mtMaterialRepository.materialPropertyBatchGet(tenantId,materialIdList);
        log.info("=================================>????????????-??????????????????-materialPropertyBatchGet????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if(materialIdList.size() != mtMaterialList.size()){
            //???????????????${1}
            throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_0053", "MATERIAL", "???releaseDataGet???"));
        }
        resultVO.setMaterialMap(mtMaterialList.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t)));

        //??????????????????
        startDate = System.currentTimeMillis();
        MtModLocator areaLocator =  hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,dto.getComponentList().get(0).getMaterialLotList().get(0).getLocatorId());
        log.info("=================================>????????????-??????????????????-queryAreaLocator????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if(Objects.isNull(areaLocator)){
            //????????????????????????
            throw new MtException("HME_EO_JOB_SN_132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_132", "HME"));
        }
        resultVO.setAreaLocator(areaLocator);

        //????????????????????????
        List<String> locatorIdList = new ArrayList<>();
        for (HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4:dto.getComponentList()
             ) {
            if(CollectionUtils.isEmpty(hmeEoJobSnBatchVO4.getMaterialLotList())){
                continue;
            }
            locatorIdList.addAll(hmeEoJobSnBatchVO4.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getLocatorId).distinct().collect(Collectors.toList()));
        }
        locatorIdList = locatorIdList.stream().distinct().collect(Collectors.toList());
        startDate = System.currentTimeMillis();
        List<HmeModLocatorVO2> hmeModLocatorVO2List =  hmeEoJobSnLotMaterialMapper.batchQueryAreaLocator(tenantId,locatorIdList);
        log.info("=================================>????????????-??????????????????-batchQueryAreaLocator????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if(CollectionUtils.isEmpty(hmeModLocatorVO2List) || hmeModLocatorVO2List.size() != locatorIdList.size()){
            //????????????????????????
            throw new MtException("HME_EO_JOB_SN_132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_132", "HME"));
        }
        Map<String, HmeModLocatorVO2> areaLocatorMap = hmeModLocatorVO2List.stream().collect(Collectors.toMap(item -> item.getSubLocatorId(), t -> t));
        resultVO.setAreaLocatorMap(areaLocatorMap);

        //????????????
        List<String> transactionTypeCodeList = new ArrayList<>();
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R);
        resultVO.setWmsTransactionTypeDTOMap(selectTransactionType(tenantId , transactionTypeCodeList));

        //??????:??????????????????,??????????????????????????????????????????
        if(!HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getReworkFlag())) {
            startDate = System.currentTimeMillis();
            HmeEoJobSnBatchVO17 hmeEoJobSnBatchVO17 = selectReleased(tenantId, dto);
            log.info("=================================>????????????-??????????????????-selectReleased????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if (MapUtils.isNotEmpty(hmeEoJobSnBatchVO17.getMtEoComponentActualMap())) {
                resultVO.setMtEoComponentActualMap(hmeEoJobSnBatchVO17.getMtEoComponentActualMap());
            }else{
                resultVO.setMtEoComponentActualMap(new HashMap<String, MtEoComponentActual>());
            }
            if(MapUtils.isNotEmpty(hmeEoJobSnBatchVO17.getReleasedRecordMap())){
                resultVO.setReleasedRecordMap(hmeEoJobSnBatchVO17.getReleasedRecordMap());
            }else{
                resultVO.setReleasedRecordMap(new HashMap<String, HmeEoJobSnBatchVO15>());
            }
        }

        //????????????
        HmeEoJobSnVO22 hmeEoJobSnVO22 = new HmeEoJobSnVO22();
        hmeEoJobSnVO22.setWorkOrderId(dto.getSnLineDto().getWorkOrderId());
        hmeEoJobSnVO22.setWorkcellId(dto.getSnLineDto().getWorkcellId());
        hmeEoJobSnVO22.setSiteId(dto.getSnLineDto().getSiteId());
        startDate = System.currentTimeMillis();
        List<HmeEoJobSnBatchVO4> woBomComponentList = hmeEoJobSnCommonService.selectWoBomComponent(tenantId,hmeEoJobSnVO22);
        log.info("=================================>????????????-??????????????????-selectWoBomComponent????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if(CollectionUtils.isNotEmpty(woBomComponentList)){
            Map<String, HmeEoJobSnBatchVO4> woBomComponentMap = woBomComponentList.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getMaterialId(),item.getLineNumber()), t -> t));
            resultVO.setWoBomComponentMap(woBomComponentMap);
            Map<String,List<HmeEoJobSnBatchVO4>> woMainBomComponentMap = woBomComponentList.stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
            resultVO.setWoMainBomComponentMap(woMainBomComponentMap);
            //????????????????????????
            HmeEoJobSnSingleDTO woDto = new HmeEoJobSnSingleDTO();
            woDto.setComponentList(woBomComponentList);
            woDto.setSnLineDto(dto.getSnLineDto());
            startDate = System.currentTimeMillis();
            HmeEoJobSnBatchVO17 woReleased = selectWoReleased(tenantId,woDto);
            log.info("=================================>????????????-??????????????????-selectWoReleased????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if(MapUtils.isNotEmpty(woReleased.getMtWoComponentActualMap())) {
                resultVO.setWoComponentActualMap(woReleased.getMtWoComponentActualMap());
            } else {
                resultVO.setWoComponentActualMap(new HashMap<String, MtWorkOrderComponentActual>());
            }
        }else{
            resultVO.setWoBomComponentMap(new HashMap<String, HmeEoJobSnBatchVO4>());
            resultVO.setWoMainBomComponentMap(new HashMap<String, List<HmeEoJobSnBatchVO4>>());
            resultVO.setWoComponentActualMap(new HashMap<String, MtWorkOrderComponentActual>());
        }

        //?????????????????????
        String wareHouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
        if(StringUtils.isBlank(wareHouseCode)){
            //???????????????????????? ${1}.${2}
            throw new MtException("MT_GENERAL_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0011", "GENERAL","ISSUE_WAREHOUSE_CODE",""));
        }
        startDate = System.currentTimeMillis();
        HmeEoJobSnBatchVO21 hmeEoJobSnBatchVO21 = hmeEoJobSnBatchMapper.selectInventoryLocator(tenantId,wareHouseCode);
        log.info("=================================>????????????-??????????????????-selectInventoryLocator????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if(Objects.isNull(hmeEoJobSnBatchVO21)) {
            //??????${1}?????????,?????????!
            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0012", "WMS"));
        }
        resultVO.setMainLocator(hmeEoJobSnBatchVO21);

        //??????
        startDate = System.currentTimeMillis();
        HmeEoJobSnBatchVO11 hmeEoJobShift = hmeEoJobSnBatchMapper.selectJobShift(tenantId,dto.getSnLineDto().getJobId());
        log.info("=================================>????????????-??????????????????-selectJobShift????????????"+(System.currentTimeMillis() - startDate) + "??????");
        if(Objects.nonNull(hmeEoJobShift)){
            resultVO.setHmeEoJobShift(hmeEoJobShift);
        }

        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(dto.getSnLineDto().getEoId());
        startDate = System.currentTimeMillis();
        List<MtEoRouter> eoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId,eoIdList);
        log.info("=================================>????????????-??????????????????-eoRouterBatchGet????????????"+(System.currentTimeMillis() - startDate) + "??????");
        Map<String, MtEoRouter> eoRouterMap = eoRouterList.stream().collect(Collectors.toMap(MtEoRouter::getEoId, t -> t));
        if(eoRouterMap.size() != eoIdList.size()){
            //??????????????????????????????${1}
            throw new MtException("MT_ORDER_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0036", "HME",""));
        }
        resultVO.setEoRouterMap(eoRouterMap);

        //????????????
        List<String> virtualIdList = new ArrayList<>();
        for (HmeEoJobSnBatchVO4 component:dto.getComponentList()
             ) {
            if(CollectionUtils.isNotEmpty(component.getVirtualIdList())){
                virtualIdList.addAll(component.getVirtualIdList());
            }
        }
        List<HmeEoJobSnSingleVO7> hmeMaterialLotLoadList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(virtualIdList)) {
            virtualIdList = virtualIdList.stream().distinct().collect(Collectors.toList());
            startDate = System.currentTimeMillis();
            hmeMaterialLotLoadList = hmeMaterialLotLoadMapper.queryMaterialLotLoadByVirtualId(tenantId, virtualIdList);
            log.info("=================================>????????????-??????????????????-queryMaterialLotLoadByVirtualId????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }
        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
            resultVO.setHmeMaterialLotLoadList(hmeMaterialLotLoadList);
        } else {
            resultVO.setHmeMaterialLotLoadList(new ArrayList<>());
        }

        //V20210204 modify by penglin.sui for tianyang.xie ??????FAC??????????????????
        Map<String, HmeEoJobSnVO25> materialSiteAttrMap = new HashMap<>();
        Map<String,HmeFacYk> hmeFacYkMap = new HashMap<>();
        List<HmeEoJobSnBatchVO4> releasedComponentList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
        List snMaterialIdList = new ArrayList();
        if(CollectionUtils.isNotEmpty(releasedComponentList)){
            snMaterialIdList = releasedComponentList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(snMaterialIdList)) {
            startDate = System.currentTimeMillis();
            List<HmeEoJobSnVO25> hmeEoJobSnVO25List = hmeEoJobSnMapper.queryMaterialSiteAttr(tenantId, snMaterialIdList, dto.getSnLineDto().getSiteId());
            log.info("=================================>????????????-??????????????????-queryMaterialSiteAttr????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if (CollectionUtils.isNotEmpty(hmeEoJobSnVO25List)) {
                List<HmeEoJobSnVO25> hmeEoJobSnVO25ListOfFac = hmeEoJobSnVO25List.stream().filter(item -> HmeConstants.ConstantValue.B05_FAC.equals(item.getFac())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(hmeEoJobSnVO25ListOfFac)) {
                    materialSiteAttrMap = hmeEoJobSnVO25ListOfFac.stream().collect(Collectors.toMap(item -> fetchGroupKey3(item.getMaterialId(), item.getSiteId(), HmeConstants.MaterialSiteExtendAttr.FAC), t -> t));

                    //??????FAC-Y???????????????
                    HmeFacYk hmeFacYkPara = new HmeFacYk();
                    hmeFacYkPara.setTenantId(tenantId);
                    hmeFacYkPara.setMaterialId(dto.getSnLineDto().getSnMaterialId());
                    hmeFacYkPara.setFacMaterialId(dto.getComponentList().get(0).getMaterialId());
                    hmeFacYkPara.setCosType(dto.getSnLineDto().getSnNum().substring(4, 5));
                    hmeFacYkPara.setWorkcellId(dto.getSnLineDto().getWorkcellId());
                    startDate = System.currentTimeMillis();
                    HmeFacYk hmeFacYk = hmeFacYkMapper.selectMaxMinValue(tenantId, hmeFacYkPara);
                    log.info("=================================>????????????-??????????????????-selectMaxMinValue????????????"+(System.currentTimeMillis() - startDate) + "??????");
                    if (Objects.nonNull(hmeFacYk)) {
                        hmeFacYkMap.put(hmeFacYk.getMaterialId(), hmeFacYk);
                    }
                }
            }
        }
        resultVO.setMaterialSiteAttrMap(materialSiteAttrMap);
        resultVO.setHmeFacYkMap(hmeFacYkMap);

        return resultVO;
    }

    /**
     *
     * @Description ???????????????
     *
     * @author penglin.sui
     * @date 2020/12/16 11:00
     * @param tenantId ??????ID
     * @param dto ??????
     * @param releaseData ?????????????????????
     * @return List<HmeEoJobSnBatchVO4>
     *
     */
    private HmeEoJobSnSingleVO4 normalRelease(Long tenantId, HmeEoJobSnSingleDTO dto, HmeEoJobSnSingleVO3 releaseData){
        HmeEoJobSnSingleVO4 resultVO = new HmeEoJobSnSingleVO4();
        resultVO.setExecReleaseFlag(false);
        if(CollectionUtils.isEmpty(dto.getComponentList())){
            return resultVO;
        }
        //???????????????????????????????????????
        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE == item.getIsReleased())
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(componentList)){
            return resultVO;
        }

        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList();
        Map<String, MtMaterialLotVO1> materialLotConsumeMap = new HashMap<>();
        List<HmeEoJobMaterial> updateSnDataList = new ArrayList<>();
        List<HmeEoJobLotMaterial> deleteLotDataList = new ArrayList<>();
        List<HmeEoJobTimeMaterial> deleteTimeDataList = new ArrayList<>();
        List<HmeEoJobSnLotMaterial> eoJobSnInsertDataList = new ArrayList<>();
        List<HmeEoJobSnVO21> upGradeMaterialLotList = new ArrayList<>();
        List<MtAssembleProcessActualVO11> materialInfoList = new ArrayList<>();
        List<HmeMaterialLotLabCode> insertMaterialLotLabCodeList = new ArrayList<>();
        List<HmeMaterialLotLabCode> updateMaterialLotLabCodeList = new ArrayList<>();
        //COS???????????????
        List<HmeEoJobSnSingleVO6> cosVirtualNumList = new ArrayList<>();
        //???????????????????????????????????????
        List<HmeEoJobDataCalculationResultDTO> hmeEoJobDataCalculationResultDTOList= new ArrayList<>();
        //????????????
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = new ArrayList<>();
        List<MtEoComponentActualVO5> eoComponentActualVO5List = new ArrayList<>();
        List<MtWoComponentActualVO1> woComponentActualVO1List = new ArrayList<>();
        List<MtCommonExtendVO6> materialLotAttrList = new ArrayList<>();
        //??????????????????
        Boolean execReleaseFlag = false;

        //????????????????????????
        // 20211206 add by sanfeng.zhang for wenxin.zhang ?????????????????????????????? ????????????
        Map<String, List<HmeEoJobSnBatchVO4>> materialComponentMap = dto.getComponentList().stream().collect(Collectors.groupingBy(HmeEoJobSnBatchVO4::getMaterialId));
        materialComponentMap.forEach((mKey, mValue) -> {
            List<String> valueList = mValue.stream().map(HmeEoJobSnBatchVO4::getComponentMaterialId).distinct().collect(Collectors.toList());
            if (valueList.size() > 1) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mKey);
                throw new MtException("HME_EO_JOB_SN_258", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_258", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : ""));
            }
        });
        Map<String,String> materialRelMap = dto.getComponentList().stream().collect(Collectors.toMap(HmeEoJobSnBatchVO4::getMaterialId , HmeEoJobSnBatchVO4::getComponentMaterialId));
        //??????????????????(????????????)
        Map<String,List<HmeEoJobSnBatchVO4>> sourceComponentMap = dto.getComponentList().stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
        //????????????????????????map
        Map<String, HmeEoJobSnBatchVO4> componentMap = dto.getComponentList().stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getMaterialId(),""), t -> t));

        HmeEoJobSnVO snLine = dto.getSnLineDto();

        for (HmeEoJobSnBatchVO4 component : componentList
        ) {
            //????????????????????????????????????
            List<HmeEoJobSnBatchVO6> materialLotList = component.getMaterialLotList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                    && HmeConstants.ConstantValue.YES.equals(item.getEnableFlag())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(materialLotList)) {
                continue;
            }
            log.info("<========??????????????????============>" + component.getMaterialId() + "," + component.getMaterialCode());
            //??????
            String componentMaterialId = materialRelMap.get(component.getMaterialId());
            log.info("<=============release?????????============>" + componentMaterialId);
            //??????????????????????????????(????????? = ?????????)
            HmeEoJobSnBatchVO4 sourceComponent = componentMap.get(fetchGroupKey2(componentMaterialId , ""));
            //?????????????????????(???????????????BOM?????????????????????)
            List<HmeEoJobSnBatchVO4> releasedQtySumList = sourceComponentMap.get(componentMaterialId);
            //??????
            HmeEoJobSnBatchVO4 mainComponentMaterial = releasedQtySumList.stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId())).collect(Collectors.toList()).get(0);
            //????????????????????? = ???????????????????????????????????? = ?????????????????? - ???????????????????????????????????????????????????
            BigDecimal currentReleaseQty = component.getWillReleaseQty();
            if(Objects.isNull(currentReleaseQty)){
                currentReleaseQty = BigDecimal.ZERO;
            }
            if(!HmeConstants.ConstantValue.YES.equals(snLine.getReworkFlag()) && !HmeConstants.ConstantValue.YES.equals(component.getQtyAlterFlag())) {
                //???????????????????????????????????????????????????????????????????????????
                BigDecimal releasedQtySum = BigDecimal.ZERO;
                for (HmeEoJobSnBatchVO4 singleReleasedQtySum : releasedQtySumList
                ) {
                    if(MapUtils.isNotEmpty(releaseData.getMtEoComponentActualMap())) {
                        MtEoComponentActual mtEoComponentActual = releaseData.getMtEoComponentActualMap().getOrDefault(fetchGroupKey3(snLine.getEoId(), snLine.getEoStepId(), singleReleasedQtySum.getMaterialId()), null);
                        if (Objects.nonNull(mtEoComponentActual)) {
                            releasedQtySum = releasedQtySum.add(BigDecimal.valueOf(mtEoComponentActual.getAssembleQty()));
                        }
                    }
                }
                log.info("<=============release????????????============>" + releasedQtySum);
                BigDecimal qty = sourceComponent.getQty();
                log.info("<=============release????????????============>" + qty);
                //?????????????????????????????????
                currentReleaseQty = qty.subtract(releasedQtySum);
            }
            log.info("<=============release???????????????============>" + currentReleaseQty);
            if (currentReleaseQty.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            //????????????????????????????????????????????????
            BigDecimal inReleaseQty = BigDecimal.ZERO;
            BigDecimal inReleaseQty2 = BigDecimal.ZERO;
            boolean isExecMainMaterialFlag = false;
            HmeEoJobSnBatchVO4 woBomComponent = releaseData.getWoBomComponentMap().getOrDefault(fetchGroupKey2(mainComponentMaterial.getComponentMaterialId(),mainComponentMaterial.getLineNumber()),null);
            MtWorkOrderComponentActual modifyWorkOrderComponentActual = null;
            if(MapUtils.isNotEmpty(releaseData.getWoComponentActualMap())) {
                modifyWorkOrderComponentActual = releaseData.getWoComponentActualMap().getOrDefault(fetchGroupKey2(snLine.getWorkOrderId(), component.getMaterialId()), null);
            }
            if(!HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getDesignedReworkFlag()) && Objects.nonNull(woBomComponent)){
                BigDecimal woRequirementQty = woBomComponent.getRequirementQty();
                // 20211208 modify by sanfeng.zhang for wenxin.zhang ????????????????????? ?????????????????????
                List<HmeEoJobSnBatchVO4> woMainBomComponentList = releaseData.getWoMainBomComponentMap().getOrDefault(mainComponentMaterial.getComponentMaterialId(), new ArrayList<>());
                BigDecimal woReleasedQty = BigDecimal.ZERO;
                if(CollectionUtils.isNotEmpty(woMainBomComponentList)){
                    for (HmeEoJobSnBatchVO4 woMainBomComponent:woMainBomComponentList
                    ) {
                        if(MapUtils.isNotEmpty(releaseData.getWoComponentActualMap())) {
                            MtWorkOrderComponentActual workOrderComponentActual = releaseData.getWoComponentActualMap().getOrDefault(fetchGroupKey2(snLine.getWorkOrderId(), woMainBomComponent.getMaterialId()), null);
                            if(Objects.nonNull(workOrderComponentActual)){
                                woReleasedQty = woReleasedQty.add(BigDecimal.valueOf(workOrderComponentActual.getAssembleQty()).add(BigDecimal.valueOf(workOrderComponentActual.getScrappedQty())));
                            }
                        }
                    }
                }
                if(woRequirementQty.compareTo(woReleasedQty) > 0){
                    //?????????
                    inReleaseQty = woRequirementQty.subtract(woReleasedQty);
                    inReleaseQty2 = woRequirementQty.subtract(woReleasedQty);
                    if(HmeConstants.ComponentType.BOM_SUBSTITUTE.equals(component.getComponentType()) || HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(component.getComponentType())) {
                        isExecMainMaterialFlag = true;
                    }
                }
            }
            log.info("<==================?????????????????????????????????=================>" + inReleaseQty);

            //?????????????????????
            BigDecimal remainReleaseQty = currentReleaseQty;

            //????????????????????????EO???WO????????????
            if(HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getDesignedReworkFlag())) {
                MtEoComponentActualVO5 mtEoComponentActualVO5 = new MtEoComponentActualVO5();
                MtWoComponentActualVO1 woComponentActualVO1 = new MtWoComponentActualVO1();

                mtEoComponentActualVO5.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                if (HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(component.getComponentType())) {
                    mtEoComponentActualVO5.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                }else{
                    mtEoComponentActualVO5.setBomComponentId(component.getBomComponentId());
                }
                mtEoComponentActualVO5.setEoId(snLine.getEoId());
                mtEoComponentActualVO5.setEventRequestId(releaseData.getEventRequestId());
                mtEoComponentActualVO5.setLocatorId(materialLotList.get(0).getLocatorId());
                mtEoComponentActualVO5.setMaterialId(component.getMaterialId());
                mtEoComponentActualVO5.setOperationId(snLine.getOperationId());
                mtEoComponentActualVO5.setParentEventId(releaseData.getEventId());
                mtEoComponentActualVO5.setRouterStepId(snLine.getEoStepId());
                if (Objects.nonNull(releaseData.getHmeEoJobShift())) {
                    mtEoComponentActualVO5.setShiftCode(releaseData.getHmeEoJobShift().getShiftCode());
                    mtEoComponentActualVO5.setShiftDate(releaseData.getHmeEoJobShift().getShiftDate());

                    woComponentActualVO1.setShiftCode(releaseData.getHmeEoJobShift().getShiftCode());
                    woComponentActualVO1.setShiftDate(releaseData.getHmeEoJobShift().getShiftDate());
                }
                mtEoComponentActualVO5.setTrxAssembleQty(remainReleaseQty.doubleValue());
                mtEoComponentActualVO5.setWorkcellId(snLine.getWorkcellId());
                eoComponentActualVO5List.add(mtEoComponentActualVO5);

                woComponentActualVO1.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                woComponentActualVO1.setWorkOrderId(snLine.getWorkOrderId());
                woComponentActualVO1.setEventRequestId(releaseData.getEventRequestId());
                woComponentActualVO1.setLocatorId(materialLotList.get(0).getLocatorId());
                woComponentActualVO1.setMaterialId(component.getMaterialId());
                woComponentActualVO1.setOperationId(snLine.getOperationId());
                woComponentActualVO1.setParentEventId(releaseData.getEventId());
                woComponentActualVO1.setRouterStepId(snLine.getEoStepId());
                woComponentActualVO1.setTrxAssembleQty(remainReleaseQty.doubleValue());
                woComponentActualVO1.setWorkcellId(snLine.getWorkcellId());
                woComponentActualVO1List.add(woComponentActualVO1);
            }

            int loopCount = 0;
            for(int i = 0 ; i < materialLotList.size(); i++)
            {
                HmeEoJobSnBatchVO6 componentMaterialLot = materialLotList.get(i);
                BigDecimal materialLotCurrentReleaseQty = componentMaterialLot.getPrimaryUomQty().compareTo(remainReleaseQty) > 0 ? remainReleaseQty : componentMaterialLot.getPrimaryUomQty();
                if(materialLotCurrentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                    continue;
                }
                log.info("<========????????????????????????============>???" + componentMaterialLot.getMaterialLotCode() + "," + componentMaterialLot.getPrimaryUomQty());
                log.info("<========??????????????????1============>???" + materialLotCurrentReleaseQty);
                String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                if (inReleaseQty.compareTo(BigDecimal.ZERO) > 0) {
                    if(HmeConstants.ComponentType.A_MAIN.equals(component.getComponentType())) {
                        //??????
                        if (inReleaseQty.compareTo(materialLotCurrentReleaseQty) < 0) {
                            materialLotCurrentReleaseQty = inReleaseQty;
                            log.info("<========??????????????????????????????============>???" + materialLotCurrentReleaseQty);
                            i--;
                        }
                        inReleaseQty = inReleaseQty.subtract(materialLotCurrentReleaseQty);
                        log.info("<========?????????????????????????????????============>???" + inReleaseQty);
                        transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
                    }
                }
                remainReleaseQty = remainReleaseQty.subtract(materialLotCurrentReleaseQty);
                log.info("<========??????????????????2============>???" + materialLotCurrentReleaseQty);
                log.info("<========????????????????????????============>???" + remainReleaseQty);

                if(!HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getDesignedReworkFlag())) {
                    MtAssembleProcessActualVO11 mtAssembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                    mtAssembleProcessActualVO11.setMaterialId(component.getMaterialId());
                    mtAssembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                    if (HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(component.getComponentType())) {
                        mtAssembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                    }else{
                        mtAssembleProcessActualVO11.setBomComponentId(component.getBomComponentId());
                    }
                    mtAssembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                    mtAssembleProcessActualVO11.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                    mtAssembleProcessActualVO11.setTrxAssembleQty(materialLotCurrentReleaseQty.doubleValue());
                    materialInfoList.add(mtAssembleProcessActualVO11);
                }

                //??????????????????EO??????????????????
                WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                objectTransactionVO.setEventId(releaseData.getEventId());
                objectTransactionVO.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                objectTransactionVO.setMaterialId(component.getMaterialId());
                objectTransactionVO.setMaterialCode(releaseData.getMaterialMap().get(component.getMaterialId()).getMaterialCode());
                objectTransactionVO.setTransactionQty(materialLotCurrentReleaseQty);
                objectTransactionVO.setLotNumber(componentMaterialLot.getLot());
                objectTransactionVO.setTransactionUom(releaseData.getMaterialMap().get(component.getMaterialId()).getPrimaryUomCode());
                objectTransactionVO.setTransactionTime(CommonUtils.currentTimeGet());
                objectTransactionVO.setPlantId(componentMaterialLot.getSiteId());
                objectTransactionVO.setPlantCode(snLine.getSiteCode());
                objectTransactionVO.setLocatorId(componentMaterialLot.getLocatorId());
                objectTransactionVO.setLocatorCode(componentMaterialLot.getLocatorCode());
                HmeModLocatorVO2 hmeModLocatorVO2 = releaseData.getAreaLocatorMap().get(componentMaterialLot.getLocatorId());
                objectTransactionVO.setWarehouseId(hmeModLocatorVO2.getLocatorId());
                objectTransactionVO.setWarehouseCode(hmeModLocatorVO2.getLocatorCode());
                if(StringUtils.isBlank(snLine.getWorkOrderNum())){
                    //??????${1}?????????,?????????!
                    throw new MtException("HME_COS_CHIP_IMP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_CHIP_IMP_0020", "HME"));
                }
                objectTransactionVO.setWorkOrderNum(snLine.getWorkOrderNum());
                objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                objectTransactionVO.setProdLineCode(snLine.getProdLineCode());
                objectTransactionVO.setMoveType(releaseData.getWmsTransactionTypeDTOMap().get(transactionTypeCode).getMoveType());
                if (HmeConstants.TransactionTypeCode.HME_WO_ISSUE.equals(transactionTypeCode)) {
                    objectTransactionVO.setBomReserveNum(component.getBomReserveNum());
                    objectTransactionVO.setBomReserveLineNum(String.valueOf(component.getLineNumber()));
                }
                //???????????????????????????
                if(MapUtils.isNotEmpty(releaseData.getMaterialLotAttrMap())) {
                    objectTransactionVO.setSoNum(releaseData.getMaterialLotAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.SO_NUM), ""));
                    objectTransactionVO.setSoLineNum(releaseData.getMaterialLotAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.SO_LINE_NUM), ""));
                }
                objectTransactionRequestList.add(objectTransactionVO);

                if(isExecMainMaterialFlag) {
                    BigDecimal mainReleaseQty = BigDecimal.ZERO;
                    if(inReleaseQty2.compareTo(materialLotCurrentReleaseQty) >= 0){
                        mainReleaseQty = materialLotCurrentReleaseQty;
                    }else{
                        mainReleaseQty = inReleaseQty2;
                    }
                    //???????????????????????????
                    WmsObjectTransactionRequestVO objectTransactionVO2 = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(objectTransactionVO,objectTransactionVO2);
                    objectTransactionVO2.setMaterialId(componentMaterialId);
                    objectTransactionVO2.setMaterialCode(releaseData.getMaterialMap().get(componentMaterialId).getMaterialCode());
                    objectTransactionVO2.setTransactionUom(releaseData.getMaterialMap().get(componentMaterialId).getPrimaryUomCode());
                    if(Objects.nonNull(releaseData.getMainLocator())) {
                        objectTransactionVO2.setLocatorId(releaseData.getMainLocator().getLocatorId());
                        objectTransactionVO2.setLocatorCode(releaseData.getMainLocator().getLocatorCode());
                        objectTransactionVO2.setWarehouseId(releaseData.getMainLocator().getAreaLocatorId());
                        objectTransactionVO2.setWarehouseCode(releaseData.getMainLocator().getAreaLocatorCode());
                    }
                    objectTransactionVO2.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE);
                    objectTransactionVO2.setMoveType(releaseData.getWmsTransactionTypeDTOMap().get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE).getMoveType());
                    objectTransactionVO2.setBomReserveNum(sourceComponent.getBomReserveNum());
                    objectTransactionVO2.setBomReserveLineNum(String.valueOf(sourceComponent.getLineNumber()));
                    objectTransactionVO2.setTransactionQty(mainReleaseQty);
                    objectTransactionRequestList.add(objectTransactionVO2);
                    //???????????????????????????
                    WmsObjectTransactionRequestVO objectTransactionVO3 = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(objectTransactionVO2,objectTransactionVO3);
                    objectTransactionVO3.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
                    objectTransactionVO3.setMoveType(releaseData.getWmsTransactionTypeDTOMap().get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT).getMoveType());
                    objectTransactionVO3.setBomReserveNum("");
                    objectTransactionVO3.setBomReserveLineNum("");
                    objectTransactionRequestList.add(objectTransactionVO3);
                }

                //?????????????????????/???????????????????????????
                MtMaterialLotVO1 mtMaterialLotVO1 = materialLotConsumeMap.getOrDefault(componentMaterialLot.getMaterialLotId(),null);
                if(Objects.nonNull(mtMaterialLotVO1)){
                    log.info("<========???????????????????????????============>???" + mtMaterialLotVO1.getMaterialLotId());
                    mtMaterialLotVO1.setTrxPrimaryUomQty((BigDecimal.valueOf(mtMaterialLotVO1.getTrxPrimaryUomQty()).add(materialLotCurrentReleaseQty)).doubleValue());
                }else{
                    mtMaterialLotVO1 = new MtMaterialLotVO1();
                    mtMaterialLotVO1.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                    mtMaterialLotVO1.setPrimaryUomId(releaseData.getMaterialMap().get(component.getMaterialId()).getPrimaryUomId());
                    mtMaterialLotVO1.setTrxPrimaryUomQty(materialLotCurrentReleaseQty.doubleValue());
                    mtMaterialLotVO1.setEventRequestId(releaseData.getEventRequestId());
                    if (StringUtils.isNotEmpty(releaseData.getMaterialMap().get(component.getMaterialId()).getSecondaryUomId())) {
                        mtMaterialLotVO1.setSecondaryUomId(releaseData.getMaterialMap().get(component.getMaterialId()).getSecondaryUomId());
                        mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
                    }
                    materialLotConsumeMap.put(componentMaterialLot.getMaterialLotId(),mtMaterialLotVO1);

                    //V20210706 modify by penglin.sui for peng.zhao ??????????????????????????????????????????????????????
                    if(HmeConstants.ConstantValue.YES.equals(dto.getIsFirstProcess())) {
                        MtCommonExtendVO5 usedFlagExtend = new MtCommonExtendVO5();
                        usedFlagExtend.setAttrName("USED_FLAG");
                        usedFlagExtend.setAttrValue(HmeConstants.ConstantValue.YES);

                        List<MtCommonExtendVO5> mtLotExtendUsedFlagList = new ArrayList<>();
                        mtLotExtendUsedFlagList.add(usedFlagExtend);

                        MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
                        mtCommonExtendVO6.setKeyId(componentMaterialLot.getMaterialLotId());
                        mtCommonExtendVO6.setAttrs(mtLotExtendUsedFlagList);
                        materialLotAttrList.add(mtCommonExtendVO6);
                    }
                }

                if (componentMaterialLot.getPrimaryUomQty().compareTo(materialLotCurrentReleaseQty) <= 0) {
                    componentMaterialLot.setEnableFlag(HmeConstants.ConstantValue.NO);
                    if (HmeConstants.MaterialTypeCode.SN.equals(componentMaterialLot.getMaterialType())) {
                        HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                        hmeEoJobMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                        hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                        hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ONE);
                        hmeEoJobMaterial.setLastUpdatedBy(userId);
                        hmeEoJobMaterial.setLastUpdateDate(CommonUtils.currentTimeGet());
                        updateSnDataList.add(hmeEoJobMaterial);
                    } else if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType())) {
                        HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
                        hmeEoJobLotMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                        deleteLotDataList.add(hmeEoJobLotMaterial);
                    } else if (HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                        HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
                        hmeEoJobTimeMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                        deleteTimeDataList.add(hmeEoJobTimeMaterial);
                    }
                }

                //????????????/??????????????????
                if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType()) ||
                        HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setTenantId(tenantId);
                    if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType())) {
                        hmeEoJobSnLotMaterial.setLotMaterialId(componentMaterialLot.getJobMaterialId());
                    } else if (HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                        hmeEoJobSnLotMaterial.setTimeMaterialId(componentMaterialLot.getJobMaterialId());
                    }
                    hmeEoJobSnLotMaterial.setMaterialType(componentMaterialLot.getMaterialType());
                    hmeEoJobSnLotMaterial.setWorkcellId(snLine.getWorkcellId());
                    hmeEoJobSnLotMaterial.setMaterialId(component.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setReleaseQty(materialLotCurrentReleaseQty);
                    hmeEoJobSnLotMaterial.setIsReleased(componentMaterialLot.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(snLine.getJobId());
                    hmeEoJobSnLotMaterial.setLocatorId(componentMaterialLot.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(componentMaterialLot.getLot());
                    hmeEoJobSnLotMaterial.setVirtualFlag(component.getVirtualFlag());
                    if(MapUtils.isNotEmpty(releaseData.getMaterialLotAttrMap())) {
                        hmeEoJobSnLotMaterial.setProductionVersion(releaseData.getMaterialLotAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.MATERIAL_VERSION), ""));
                    }
                    eoJobSnInsertDataList.add(hmeEoJobSnLotMaterial);
                }

                //????????????,???????????????
                componentMaterialLot.setPrimaryUomQty(componentMaterialLot.getPrimaryUomQty().subtract(materialLotCurrentReleaseQty));
                component.setReleasedQty(component.getReleasedQty().add(materialLotCurrentReleaseQty));
                if(HmeConstants.ConstantValue.NO.equals(component.getIsSubstitute())) {
                    component.setWillReleaseQty(component.getRequirementQty().subtract(component.getReleasedQty()));
                    if(component.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                        component.setWillReleaseQty(BigDecimal.ZERO);
                    }
                }else{
                    if(!component.getMaterialId().equals(component.getComponentMaterialId())){
                        mainComponentMaterial.setWillReleaseQty(mainComponentMaterial.getWillReleaseQty().subtract(materialLotCurrentReleaseQty));
                        if(mainComponentMaterial.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                            mainComponentMaterial.setWillReleaseQty(BigDecimal.ZERO);
                        }
                    }
                }
                //?????????????????????????????????
                if(Objects.nonNull(modifyWorkOrderComponentActual)) {
                    modifyWorkOrderComponentActual.setAssembleQty((BigDecimal.valueOf(modifyWorkOrderComponentActual.getAssembleQty()).add(materialLotCurrentReleaseQty)).doubleValue());
                }else{
                    MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
                    mtWorkOrderComponentActual.setWorkOrderId(snLine.getWorkOrderId());
                    mtWorkOrderComponentActual.setMaterialId(component.getMaterialId());
                    mtWorkOrderComponentActual.setAssembleQty(materialLotCurrentReleaseQty.doubleValue());
                    mtWorkOrderComponentActual.setScrappedQty(BigDecimal.ZERO.doubleValue());
                    releaseData.getWoComponentActualMap().put(fetchGroupKey2(snLine.getWorkOrderId(),component.getMaterialId()),mtWorkOrderComponentActual);
                }
                //???????????????????????????
                if (HmeConstants.MaterialTypeCode.SN.equals(component.getProductionType()) && HmeConstants.ConstantValue.YES.equals(component.getUpgradeFlag())){
                    if(loopCount++ == 0) {
                        HmeEoJobSnVO21 updateMaterialLot = new HmeEoJobSnVO21();
                        updateMaterialLot.setMaterialLotCode(componentMaterialLot.getMaterialLotCode());
                        updateMaterialLot.setMaterialId(componentMaterialLot.getMaterialId());
                        upGradeMaterialLotList.add(updateMaterialLot);
                    }
                }

                //????????????????????????
                List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = releaseData.getHmeMaterialLotLabCodeMap().getOrDefault(componentMaterialLot.getMaterialLotId(),new ArrayList<>());
                String labCode = releaseData.getMaterialLotAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId() , HmeConstants.ExtendAttr.LAB_CODE), "");
                if(CollectionUtils.isEmpty(hmeMaterialLotLabCodeList) && StringUtils.isNotBlank(labCode)){
                    HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
                    hmeMaterialLotLabCodePara.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                    hmeMaterialLotLabCodePara.setRouterStepId(snLine.getEoStepId());
                    hmeMaterialLotLabCodePara.setLabCode(labCode);
                    HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
                    if (Objects.isNull(hmeMaterialLotLabCode)) {
                        List<HmeMaterialLotLabCode> existsInsertMaterialLotLabCodeList = new ArrayList<>();
                        if(CollectionUtils.isNotEmpty(insertMaterialLotLabCodeList)) {
                            existsInsertMaterialLotLabCodeList = insertMaterialLotLabCodeList.stream().filter(item -> item.getTenantId().equals(tenantId)
                                    && item.getMaterialLotId().equals(componentMaterialLot.getMaterialLotId())
                                    && item.getRouterStepId().equals(snLine.getEoStepId())
                                    && item.getLabCode().equals(labCode)).collect(Collectors.toList());
                        }
                        if(CollectionUtils.isEmpty(existsInsertMaterialLotLabCodeList)) {
                            HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                            insertHmeMaterialLotLabCode.setTenantId(tenantId);
                            insertHmeMaterialLotLabCode.setMaterialLotId(snLine.getMaterialLotId());
                            insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                            insertHmeMaterialLotLabCode.setLabCode(labCode);
                            insertHmeMaterialLotLabCode.setJobId(snLine.getJobId());
                            insertHmeMaterialLotLabCode.setWorkcellId(snLine.getWorkcellId());
                            insertHmeMaterialLotLabCode.setWorkOrderId(snLine.getWorkOrderId());
                            insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.MA);
                            insertHmeMaterialLotLabCode.setRouterStepId(snLine.getEoStepId());
                            insertHmeMaterialLotLabCode.setSourceMaterialLotId(componentMaterialLot.getMaterialLotId());
                            insertHmeMaterialLotLabCode.setSourceMaterialId(componentMaterialLot.getMaterialId());
                            insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                            insertMaterialLotLabCodeList.add(insertHmeMaterialLotLabCode);
                        }
                    } else {
                        if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                            List<HmeMaterialLotLabCode> existsUpdateMaterialLotLabCodeList = new ArrayList<>();
                            if(CollectionUtils.isNotEmpty(updateMaterialLotLabCodeList)) {
                                existsUpdateMaterialLotLabCodeList = updateMaterialLotLabCodeList.stream().filter(item -> item.getLabCodeId().equals(hmeMaterialLotLabCode.getLabCodeId()))
                                        .collect(Collectors.toList());
                            }
                            if(CollectionUtils.isEmpty(existsUpdateMaterialLotLabCodeList)) {
                                HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                                updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                                updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                                updateMaterialLotLabCodeList.add(updateHmeMaterialLotLabCode);
                            }
                        }
                    }
                }

                execReleaseFlag = true;
                if (remainReleaseQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }

            if(execReleaseFlag && HmeConstants.ConstantValue.YES.equals(component.getCosMaterialLotFlag())) {
                //??????COS???????????????
                HmeEoJobSnSingleVO6 cosVirtualNum = new HmeEoJobSnSingleVO6();
                cosVirtualNum.setEoId(snLine.getEoId());
                cosVirtualNum.setVirtualIdList(component.getVirtualIdList());
                cosVirtualNumList.add(cosVirtualNum);
                log.info("??????COS???????????????-eoId:" + snLine.getEoId());

                //????????????
                List<HmeEoJobSnSingleVO7> hmeMaterialLotLoadList2 = releaseData.getHmeMaterialLotLoadList().stream().filter(item -> component.getVirtualIdList().contains(item.getVirtualId()))
                        .collect(Collectors.toList());
                for (HmeEoJobSnSingleVO7 hmeEoJobSnSingleVO7:hmeMaterialLotLoadList2
                ) {
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotLoadId(hmeEoJobSnSingleVO7.getMaterialLotLoadId());
                    hmeMaterialLotLoad.setMaterialLotId("");
                    hmeMaterialLotLoad.setStatus("USED");
                    hmeMaterialLotLoadList.add(hmeMaterialLotLoad);
                }

                //???????????????????????????
                HmeEoJobDataCalculationResultDTO hmeEoJobDataCalculationResultDTO = new HmeEoJobDataCalculationResultDTO();
                hmeEoJobDataCalculationResultDTO.setEoId(snLine.getEoId());
                hmeEoJobDataCalculationResultDTO.setReworkFlag(snLine.getReworkFlag());
                hmeEoJobDataCalculationResultDTO.setSiteId(snLine.getSiteId());
                hmeEoJobDataCalculationResultDTO.setOperationId(snLine.getOperationId());
                hmeEoJobDataCalculationResultDTO.setMaterialLotId(snLine.getMaterialLotId());
                hmeEoJobDataCalculationResultDTO.setEoJobDataRecordVOList(snLine.getDataRecordVOList());
                hmeEoJobDataCalculationResultDTOList.add(hmeEoJobDataCalculationResultDTO);
            }
        }
        if(execReleaseFlag){
            resultVO.setExecReleaseFlag(execReleaseFlag);
            resultVO.setObjectTransactionRequestList(objectTransactionRequestList);
            resultVO.setUpdateSnDataList(updateSnDataList);
            resultVO.setDeleteLotDataList(deleteLotDataList);
            resultVO.setDeleteTimeDataList(deleteTimeDataList);
            resultVO.setEoJobSnInsertDataList(eoJobSnInsertDataList);
            resultVO.setMaterialLotConsumeMap(materialLotConsumeMap);
            resultVO.setUpGradeMaterialLotList(upGradeMaterialLotList);
            resultVO.setMaterialInfoList(materialInfoList);
            resultVO.setCosVirtualNumList(cosVirtualNumList);
            resultVO.setHmeEoJobDataCalculationResultDTOList(hmeEoJobDataCalculationResultDTOList);
            resultVO.setHmeMaterialLotLoadList(hmeMaterialLotLoadList);
            resultVO.setInsertMaterialLotLabCodeList(insertMaterialLotLabCodeList);
            resultVO.setUpdateMaterialLotLabCodeList(updateMaterialLotLabCodeList);
            resultVO.setEoComponentActualVO5List(eoComponentActualVO5List);
            resultVO.setWoComponentActualVO1List(woComponentActualVO1List);
            resultVO.setMaterialLotAttrList(materialLotAttrList);
        }

        return resultVO;
    }

    /**
     *
     * @Description ???????????????
     *
     * @author penglin.sui
     * @date 2020/12/16 11:00
     * @param tenantId ??????ID
     * @param dto ??????
     * @param releaseData ?????????????????????
     * @return List<HmeEoJobSnBatchVO4>
     *
     */
    private HmeEoJobSnSingleVO4 virtualRelease(Long tenantId, HmeEoJobSnSingleDTO dto, HmeEoJobSnSingleVO3 releaseData){
        HmeEoJobSnSingleVO4 resultVO = new HmeEoJobSnSingleVO4();
        resultVO.setExecReleaseFlag(false);
        if(CollectionUtils.isEmpty(dto.getComponentList())){
            return resultVO;
        }

        //???????????????????????????????????????
        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE == item.getIsReleased())
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(componentList)){
            return resultVO;
        }

        Map<String,List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap = releaseData.getVirtualComponentMaterialLotMap();
        Map<String,String> virtualJobMap = releaseData.getVirtualJobMap();
        Map<String,HmeEoJobSn> virtualEoJobSnMap = releaseData.getVirtualEoJobSnMap();

        //??????????????????
        List<MtModLocator> preLoadLocatorList = hmeEoJobSnLotMaterialMapper.queryPreLoadLocator(tenantId, dto.getComponentList().get(0).getMaterialLotList().get(0).getLocatorId(), dto.getSnLineDto().getWorkcellId());
        if (CollectionUtils.isEmpty(preLoadLocatorList) || Objects.isNull(preLoadLocatorList.get(0))){
            //????????????????????????
            throw new MtException("HME_EO_JOB_SN_083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_083", "HME"));
        }
        if (preLoadLocatorList.size() > 1) {
            //?????????????????????${1}??????????????????????????????,?????????
            throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.PRE_LOAD));
        }
        MtModLocator preLoadLocator = preLoadLocatorList.get(0);

        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList();
        List<HmeEoJobMaterial> updateSnDataList = new ArrayList<>();
        List<HmeEoJobLotMaterial> deleteLotDataList = new ArrayList<>();
        List<HmeEoJobTimeMaterial> deleteTimeDataList = new ArrayList<>();
        List<HmeEoJobSnLotMaterial> eoJobSnInsertDataList = new ArrayList<>();
        List<HmeEoJobSnVO21> upGradeMaterialLotList = new ArrayList<>();
        List<MtAssembleProcessActualVO11> materialInfoList = new ArrayList<>();
        List<MtMaterialLotVO20> updateMaterialLotList = new ArrayList<>();
        List<HmeEoJobSnLotMaterial> updateRemainQtyList = new ArrayList<>();
        List<MtInvOnhandQuantityVO9> mtInvOnhandQuantityVO9List = new ArrayList<>();
        //COS???????????????
        List<HmeEoJobSnSingleVO6> cosVirtualNumList = new ArrayList<>();
        //???????????????????????????????????????
        List<HmeEoJobDataCalculationResultDTO> hmeEoJobDataCalculationResultDTOList = new ArrayList<>();
        //????????????
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = new ArrayList<>();
        List<MtEoComponentActualVO5> eoComponentActualVO5List = new ArrayList<>();
        List<MtWoComponentActualVO1> woComponentActualVO1List = new ArrayList<>();
        //??????????????????
        Boolean execReleaseFlag = false;

        //????????????????????????
        Map<String,String> materialRelMap = dto.getComponentList().stream().collect(Collectors.toMap(HmeEoJobSnBatchVO4::getMaterialId , HmeEoJobSnBatchVO4::getComponentMaterialId));
        //??????????????????(????????????)
        Map<String,List<HmeEoJobSnBatchVO4>> sourceComponentMap = dto.getComponentList().stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
        //????????????????????????map
        Map<String, HmeEoJobSnBatchVO4> componentMap = dto.getComponentList().stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getMaterialId(),""), t -> t));

        HmeEoJobSnVO snLine = dto.getSnLineDto();

        for (HmeEoJobSnBatchVO4 component : componentList
        ) {
            //???????????????????????????
            List<HmeEoJobSnBatchVO4> virtualComponentList = dto.getComponentList().stream().filter(item -> !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                    && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && item.getTopVirtualMaterialCode().equals(component.getMaterialCode()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(virtualComponentList)) {
                //???????????????????????????
                log.info("<=========MaterialCode:{}",component.getMaterialCode());
                throw new MtException("HME_EO_JOB_SN_066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_066", "HME"));
            }

            //????????????????????????????????????
            List<HmeEoJobSnBatchVO6> materialLotList = component.getMaterialLotList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                    && HmeConstants.ConstantValue.YES.equals(item.getEnableFlag())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(materialLotList)) {
                continue;
            }

            //??????ID
            String componentMaterialId = materialRelMap.get(component.getMaterialId());
            log.info("<=============?????????release?????????============>" + componentMaterialId);

            //?????????????????????(???????????????BOM?????????????????????)
            List<HmeEoJobSnBatchVO4> releasedQtySumList = sourceComponentMap.get(componentMaterialId);
            //??????
            HmeEoJobSnBatchVO4 mainComponentMaterial = releasedQtySumList.stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId())).collect(Collectors.toList()).get(0);
            BigDecimal currentReleaseQty = component.getWillReleaseQty();
            if(Objects.isNull(currentReleaseQty)){
                currentReleaseQty = BigDecimal.ZERO;
            }
            if(!HmeConstants.ConstantValue.YES.equals(snLine.getReworkFlag()) && !HmeConstants.ConstantValue.YES.equals(component.getQtyAlterFlag())) {
                //?????????????????????????????????????????????????????????????????????????????????
                BigDecimal releasedQtySum = BigDecimal.ZERO;
                for (HmeEoJobSnBatchVO4 singleReleasedQtySum : releasedQtySumList
                ) {
                    HmeEoJobSnBatchVO15 rleasedRecord = releaseData.getReleasedRecordMap().getOrDefault(fetchGroupKey2(snLine.getEoId(), singleReleasedQtySum.getMaterialId()), null);
                    if (Objects.nonNull(rleasedRecord)) {
                        releasedQtySum = releasedQtySum.add(rleasedRecord.getReleaseQty());
                    }
                }
                log.info("<=============?????????release????????????============>" + releasedQtySum);
                //??????????????????????????????(????????? = ?????????)
                BigDecimal qty = componentMap.get(fetchGroupKey2(componentMaterialId, "")).getQty();
                log.info("<=============?????????release????????????============>" + qty);
                //?????????????????????????????????
                currentReleaseQty = qty.subtract(releasedQtySum);
            }
            log.info("<=============?????????release???????????????============>" + currentReleaseQty);
            if(currentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            //?????????????????????
            BigDecimal remainReleaseQty = currentReleaseQty;
            int loopCount = 0;
            for (HmeEoJobSnBatchVO6 componentMaterialLot : materialLotList
            ) {
                String jobId = virtualJobMap.get(componentMaterialLot.getMaterialLotId());
                BigDecimal snQty = virtualEoJobSnMap.get(componentMaterialLot.getMaterialLotId()).getSnQty();

                //???????????????????????????
                for (HmeEoJobSnBatchVO4 virtualComponent:virtualComponentList
                ) {
                    List<HmeEoJobSnBatchVO6> virtualComponentMaterialLotList = virtualComponentMaterialLotMap.getOrDefault(fetchGroupKey3(jobId,virtualComponent.getMaterialId(),virtualComponent.getProductionType()),new ArrayList<>());
                    if(CollectionUtils.isNotEmpty(virtualComponentMaterialLotList)){
                        if(CollectionUtils.isNotEmpty(virtualComponent.getMaterialLotList())){
                            virtualComponent.getMaterialLotList().addAll(virtualComponentMaterialLotList);
                        }else {
                            virtualComponent.setMaterialLotList(virtualComponentMaterialLotList);
                        }
                    }
                }

                BigDecimal materialLotCurrentReleaseQty = componentMaterialLot.getPrimaryUomQty().compareTo(remainReleaseQty) > 0 ? remainReleaseQty : componentMaterialLot.getPrimaryUomQty();

                //???????????????
                for (HmeEoJobSnBatchVO4 virtualComponent: virtualComponentList
                ) {
                    //???????????????????????????????????????????????????
                    List<HmeEoJobSnBatchVO6> virtualMaterialLotList = virtualComponent.getMaterialLotList();
                    if (CollectionUtils.isEmpty(virtualMaterialLotList)) {
                        continue;
                    }
                    log.info("<========?????????????????????????????????============>" + virtualComponent.getMaterialId() + "," + virtualComponent.getMaterialCode());
                    //?????????
                    String virtualComponentMaterialId = materialRelMap.get(virtualComponent.getMaterialId());
                    log.info("<==========????????????????????????==========>" + virtualComponentMaterialId);
                    List<HmeEoJobSnBatchVO4> virtualReleasedQtySumList = sourceComponentMap.get(virtualComponentMaterialId);
                    HmeEoJobSnBatchVO4 mainVirtualComponentMaterial = virtualReleasedQtySumList.stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId())).collect(Collectors.toList()).get(0);
                    Map<String,BigDecimal> remainQtySumMap = new HashMap<>();
                    Map<String,BigDecimal> releaseQtySumMap = new HashMap<>();
                    List<HmeEoJobSnBatchVO6> hmeEoJobSnBatchVO6List = virtualComponentMaterialLotMap.getOrDefault(fetchGroupKey3(jobId,virtualComponent.getMaterialId(),virtualComponent.getProductionType()),new ArrayList<>());
                    if(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO6List)) {
                        BigDecimal remainQty = remainQtySumMap.getOrDefault(virtualComponent.getMaterialId(), null);
                        BigDecimal releaseQty = releaseQtySumMap.getOrDefault(virtualComponent.getMaterialId(), null);
                        for (HmeEoJobSnBatchVO6 virtualMaterialLot : virtualMaterialLotList
                        ) {
                            if (Objects.isNull(remainQty)) {
                                remainQtySumMap.put(virtualMaterialLot.getMaterialId(), hmeEoJobSnBatchVO6List.stream().map(HmeEoJobSnBatchVO6::getRemainQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                            } else {
                                remainQty = remainQty.add(hmeEoJobSnBatchVO6List.stream().map(HmeEoJobSnBatchVO6::getRemainQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                            }

                            if (Objects.isNull(releaseQty)) {
                                releaseQtySumMap.put(virtualMaterialLot.getMaterialId(), hmeEoJobSnBatchVO6List.stream().map(HmeEoJobSnBatchVO6::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                            } else {
                                releaseQty = releaseQty.add(hmeEoJobSnBatchVO6List.stream().map(HmeEoJobSnBatchVO6::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                            }
                        }
                    }
                    log.info("<==========?????????==========>" + componentMaterialLot.getMaterialLotCode());
                    log.info("<==========????????????????????????==========>" + materialLotCurrentReleaseQty);
                    log.info("<==========???????????????==========>" + componentMaterialLot.getPrimaryUomQty());

                    //????????????????????????????????????????????????
                    BigDecimal inReleaseQty = BigDecimal.ZERO;
                    HmeEoJobSnBatchVO4 woBomComponent = releaseData.getWoBomComponentMap().getOrDefault(fetchGroupKey2(mainVirtualComponentMaterial.getComponentMaterialId(),mainVirtualComponentMaterial.getLineNumber()),null);
                    MtWorkOrderComponentActual modifyWorkOrderComponentActual = null;
                    if(MapUtils.isNotEmpty(releaseData.getWoComponentActualMap())) {
                        modifyWorkOrderComponentActual = releaseData.getWoComponentActualMap().getOrDefault(fetchGroupKey2(snLine.getWorkOrderId(), virtualComponent.getMaterialId()), null);
                    }
                    if(!HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getDesignedReworkFlag()) && Objects.nonNull(woBomComponent)){
                        BigDecimal woRequirementQty = woBomComponent.getRequirementQty();
                        List<HmeEoJobSnBatchVO4> woMainBomComponentList = releaseData.getWoMainBomComponentMap().getOrDefault(mainVirtualComponentMaterial.getComponentMaterialId(),new ArrayList<>());
                        BigDecimal woReleasedQty = BigDecimal.ZERO;
                        if(CollectionUtils.isNotEmpty(woMainBomComponentList)){
                            for (HmeEoJobSnBatchVO4 woMainBomComponent:woMainBomComponentList
                            ) {
                                if(MapUtils.isNotEmpty(releaseData.getWoComponentActualMap())) {
                                    MtWorkOrderComponentActual workOrderComponentActual = releaseData.getWoComponentActualMap().getOrDefault(fetchGroupKey2(snLine.getWorkOrderId(), woMainBomComponent.getMaterialId()), null);
                                    if (Objects.nonNull(workOrderComponentActual)) {
                                        woReleasedQty = woReleasedQty.add(BigDecimal.valueOf(workOrderComponentActual.getAssembleQty()).add(BigDecimal.valueOf(workOrderComponentActual.getScrappedQty())));
                                    }
                                }
                            }
                        }
                        if(woRequirementQty.compareTo(woReleasedQty) > 0){
                            //?????????
                            inReleaseQty = woRequirementQty.subtract(woReleasedQty);
                        }
                    }
                    log.info("<==================?????????????????????????????????=================>" + inReleaseQty);

//                    BigDecimal componentReleaseQty = remainQtySumMap.getOrDefault(virtualComponent.getMaterialId(),BigDecimal.ZERO).multiply(materialLotCurrentReleaseQty)
//                            .divide(componentMaterialLot.getPrimaryUomQty(),3 , BigDecimal.ROUND_HALF_EVEN);
                    BigDecimal componentReleaseQty = releaseQtySumMap.getOrDefault(virtualComponent.getMaterialId(),BigDecimal.ZERO).multiply(materialLotCurrentReleaseQty)
                            .divide(snQty,3 , BigDecimal.ROUND_HALF_EVEN);
                    log.info("<==========???????????????????????????????????????==========>" + virtualComponent.getMaterialId() + "-" + componentReleaseQty);
                    if(componentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                        continue;
                    }

                    List<HmeEoJobSnBatchVO6> virtualMaterialLotList2 = virtualMaterialLotList.stream().filter(item -> item.getJobId().equals(jobId))
                            .collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(virtualMaterialLotList2)){
                        continue;
                    }

                    BigDecimal consumeQtySum = BigDecimal.ZERO;

                    for(int i = 0 ; i < virtualMaterialLotList2.size() ; i++)
                    {
                        HmeEoJobSnBatchVO6 virtualMaterialLot = virtualMaterialLotList2.get(i);

                        BigDecimal currRemainQty = virtualMaterialLot.getRemainQty();
                        BigDecimal componentCurrQty = componentReleaseQty.compareTo(currRemainQty) > 0 ? currRemainQty : componentReleaseQty;
                        if(componentCurrQty.compareTo(BigDecimal.ZERO) <= 0){
                            continue;
                        }
                        log.info("<========???????????????????????????????????????============>" + virtualMaterialLot.getMaterialLotCode());
                        String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                        if(inReleaseQty.compareTo(BigDecimal.ZERO) > 0){
                            if(inReleaseQty.compareTo(componentCurrQty) < 0){
                                componentCurrQty = inReleaseQty;
                                i--;
                                log.info("<=========??????????????????????????????========>" + componentCurrQty);
                            }
                            inReleaseQty = inReleaseQty.subtract(componentCurrQty);
                            transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
                        }
                        log.info("<=========?????????????????????????????????componentCurrQty========>" + componentCurrQty);
                        log.info("<========????????????????????????????????????????????????????????????===========>" + inReleaseQty);
                        //?????????????????????????????????
                        BigDecimal remainQty = virtualMaterialLot.getRemainQty().subtract(componentCurrQty);
                        if(HmeConstants.MaterialTypeCode.LOT.equals(virtualComponent.getProductionType())
                                || HmeConstants.MaterialTypeCode.TIME.equals(virtualComponent.getProductionType())) {
                            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial1 = new HmeEoJobSnLotMaterial();
                            hmeEoJobSnLotMaterial1.setJobMaterialId(virtualMaterialLot.getJobMaterialId());
                            hmeEoJobSnLotMaterial1.setRemainQty(remainQty);
                            updateRemainQtyList.add(hmeEoJobSnLotMaterial1);
                        }else if(HmeConstants.MaterialTypeCode.SN.equals(virtualComponent.getProductionType())) {
                            HmeEoJobMaterial hmeEoJobMaterial1 = new HmeEoJobMaterial();
                            hmeEoJobMaterial1.setJobMaterialId(virtualMaterialLot.getJobMaterialId());
                            hmeEoJobMaterial1.setRemainQty(remainQty);
                            updateSnDataList.add(hmeEoJobMaterial1);
                        }
                        virtualMaterialLot.setRemainQty(remainQty);

                        //???????????????
                        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                        mtInvOnhandQuantityVO9.setSiteId(snLine.getSiteId());
                        mtInvOnhandQuantityVO9.setLocatorId(preLoadLocator.getLocatorId());
                        mtInvOnhandQuantityVO9.setMaterialId(virtualMaterialLot.getMaterialId());
                        mtInvOnhandQuantityVO9.setLotCode(virtualMaterialLot.getLot());
                        mtInvOnhandQuantityVO9.setChangeQuantity(componentCurrQty.doubleValue());
                        mtInvOnhandQuantityVO9.setEventId(releaseData.getEventId());
                        mtInvOnhandQuantityVO9List.add(mtInvOnhandQuantityVO9);

                        if(!HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getDesignedReworkFlag())) {
                            MtAssembleProcessActualVO11 mtAssembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                            mtAssembleProcessActualVO11.setMaterialId(virtualMaterialLot.getMaterialId());
                            if (!HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(virtualComponent.getComponentType())) {
                                mtAssembleProcessActualVO11.setBomComponentId(virtualComponent.getBomComponentId());
                            }else{
                                mtAssembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                            }
                            mtAssembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                            mtAssembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                            mtAssembleProcessActualVO11.setMaterialLotId(virtualMaterialLot.getMaterialLotId());
                            mtAssembleProcessActualVO11.setTrxAssembleQty(componentCurrQty.doubleValue());
                            materialInfoList.add(mtAssembleProcessActualVO11);
                        }

                        //??????????????????EO??????????????????
                        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                        objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                        objectTransactionVO.setEventId(releaseData.getEventId());
                        objectTransactionVO.setMaterialLotId(virtualMaterialLot.getMaterialLotId());
                        objectTransactionVO.setMaterialId(virtualComponent.getMaterialId());
                        objectTransactionVO.setMaterialCode(releaseData.getMaterialMap().get(virtualComponent.getMaterialId()).getMaterialCode());
                        objectTransactionVO.setTransactionQty(componentCurrQty);
                        objectTransactionVO.setLotNumber(virtualMaterialLot.getLot());
                        objectTransactionVO.setTransactionUom(releaseData.getMaterialMap().get(virtualComponent.getMaterialId()).getPrimaryUomCode());
                        objectTransactionVO.setTransactionTime(CommonUtils.currentTimeGet());
                        objectTransactionVO.setPlantId(virtualMaterialLot.getSiteId());
                        objectTransactionVO.setPlantCode(snLine.getSiteCode());
                        objectTransactionVO.setLocatorId(virtualMaterialLot.getLocatorId());
                        objectTransactionVO.setLocatorCode(virtualMaterialLot.getLocatorCode());
                        HmeModLocatorVO2 hmeModLocatorVO2 = releaseData.getAreaLocatorMap().get(componentMaterialLot.getLocatorId());
                        objectTransactionVO.setWarehouseId(hmeModLocatorVO2.getLocatorId());
                        objectTransactionVO.setWarehouseCode(hmeModLocatorVO2.getLocatorCode());
                        if(StringUtils.isBlank(snLine.getWorkOrderNum())){
                            //??????${1}?????????,?????????!
                            throw new MtException("HME_COS_CHIP_IMP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_COS_CHIP_IMP_0020", "HME"));
                        }
                        objectTransactionVO.setWorkOrderNum(snLine.getWorkOrderNum());
                        objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                        objectTransactionVO.setProdLineCode(snLine.getProdLineCode());
                        objectTransactionVO.setMoveType(releaseData.getWmsTransactionTypeDTOMap().get(transactionTypeCode).getMoveType());
                        if (HmeConstants.TransactionTypeCode.HME_WO_ISSUE.equals(transactionTypeCode)) {
                            objectTransactionVO.setBomReserveNum(virtualComponent.getBomReserveNum());
                            objectTransactionVO.setBomReserveLineNum(String.valueOf(virtualComponent.getLineNumber()));
                        }
                        //???????????????????????????
                        if(MapUtils.isNotEmpty(releaseData.getMaterialLotAttrMap())) {
                            objectTransactionVO.setSoNum(releaseData.getMaterialLotAttrMap().getOrDefault(fetchGroupKey2(virtualMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.SO_NUM), ""));
                            objectTransactionVO.setSoLineNum(releaseData.getMaterialLotAttrMap().getOrDefault(fetchGroupKey2(virtualMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.SO_LINE_NUM), ""));
                        }
                        objectTransactionRequestList.add(objectTransactionVO);

                        //????????????,???????????????
                        virtualMaterialLot.setPrimaryUomQty(virtualMaterialLot.getPrimaryUomQty().subtract(componentCurrQty));
                        virtualComponent.setReleasedQty(virtualComponent.getReleasedQty().add(componentCurrQty));
                        if(HmeConstants.ConstantValue.NO.equals(virtualComponent.getIsSubstitute())) {
                            virtualComponent.setWillReleaseQty(virtualComponent.getRequirementQty().subtract(virtualComponent.getReleasedQty()));
                            if(virtualComponent.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                                virtualComponent.setWillReleaseQty(BigDecimal.ZERO);
                            }
                        }else{
                            mainVirtualComponentMaterial.setWillReleaseQty(mainVirtualComponentMaterial.getWillReleaseQty().subtract(componentCurrQty));
                            if(mainVirtualComponentMaterial.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                                mainVirtualComponentMaterial.setWillReleaseQty(BigDecimal.ZERO);
                            }
                        }
                        //?????????????????????????????????
                        if(Objects.nonNull(modifyWorkOrderComponentActual)) {
                            modifyWorkOrderComponentActual.setAssembleQty((BigDecimal.valueOf(modifyWorkOrderComponentActual.getAssembleQty()).add(componentCurrQty)).doubleValue());
                        }else{
                            MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
                            mtWorkOrderComponentActual.setWorkOrderId(snLine.getWorkOrderId());
                            mtWorkOrderComponentActual.setMaterialId(component.getMaterialId());
                            mtWorkOrderComponentActual.setAssembleQty(componentCurrQty.doubleValue());
                            mtWorkOrderComponentActual.setScrappedQty(BigDecimal.ZERO.doubleValue());
                            releaseData.getWoComponentActualMap().put(fetchGroupKey2(snLine.getWorkOrderId(),component.getMaterialId()),mtWorkOrderComponentActual);
                        }
                        execReleaseFlag = true;
                        componentReleaseQty = componentReleaseQty.subtract(componentCurrQty);
                        consumeQtySum = consumeQtySum.add(componentCurrQty);
                        if(componentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                            break;
                        }
                    }

                    //????????????????????????EO???WO????????????
                    if(HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getDesignedReworkFlag())) {
                        MtEoComponentActualVO5 mtEoComponentActualVO5 = new MtEoComponentActualVO5();
                        MtWoComponentActualVO1 woComponentActualVO1 = new MtWoComponentActualVO1();

                        mtEoComponentActualVO5.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                        if (HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(virtualComponent.getComponentType())) {
                            mtEoComponentActualVO5.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                        }else{
                            mtEoComponentActualVO5.setBomComponentId(virtualComponent.getBomComponentId());
                        }
                        mtEoComponentActualVO5.setEoId(snLine.getEoId());
                        mtEoComponentActualVO5.setEventRequestId(releaseData.getEventRequestId());
                        mtEoComponentActualVO5.setLocatorId(virtualMaterialLotList2.get(0).getLocatorId());
                        mtEoComponentActualVO5.setMaterialId(virtualComponent.getMaterialId());
                        mtEoComponentActualVO5.setOperationId(snLine.getOperationId());
                        mtEoComponentActualVO5.setParentEventId(releaseData.getEventId());
                        mtEoComponentActualVO5.setRouterStepId(snLine.getEoStepId());
                        if(Objects.nonNull(releaseData.getHmeEoJobShift())) {
                            mtEoComponentActualVO5.setShiftCode(releaseData.getHmeEoJobShift().getShiftCode());
                            mtEoComponentActualVO5.setShiftDate(releaseData.getHmeEoJobShift().getShiftDate());

                            woComponentActualVO1.setShiftCode(releaseData.getHmeEoJobShift().getShiftCode());
                            woComponentActualVO1.setShiftDate(releaseData.getHmeEoJobShift().getShiftDate());
                        }
                        mtEoComponentActualVO5.setTrxAssembleQty(consumeQtySum.doubleValue());
                        mtEoComponentActualVO5.setWorkcellId(snLine.getWorkcellId());
                        eoComponentActualVO5List.add(mtEoComponentActualVO5);

                        woComponentActualVO1.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                        woComponentActualVO1.setWorkOrderId(snLine.getWorkOrderId());
                        woComponentActualVO1.setEventRequestId(releaseData.getEventRequestId());
                        woComponentActualVO1.setLocatorId(virtualMaterialLotList2.get(0).getLocatorId());
                        woComponentActualVO1.setMaterialId(virtualComponent.getMaterialId());
                        woComponentActualVO1.setOperationId(snLine.getOperationId());
                        woComponentActualVO1.setParentEventId(releaseData.getEventId());
                        woComponentActualVO1.setRouterStepId(snLine.getEoStepId());
                        woComponentActualVO1.setTrxAssembleQty(consumeQtySum.doubleValue());
                        woComponentActualVO1.setWorkcellId(snLine.getWorkcellId());
                        woComponentActualVO1List.add(woComponentActualVO1);
                    }
                }

                if(execReleaseFlag) {
                    //?????????????????????????????????
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                    mtMaterialLotVO20.setMaterialLotCode(componentMaterialLot.getMaterialLotCode());
                    mtMaterialLotVO20.setTrxPrimaryUomQty(-materialLotCurrentReleaseQty.doubleValue());
                    mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.YES);
                    if (componentMaterialLot.getPrimaryUomQty().compareTo(materialLotCurrentReleaseQty) <= 0) {
                        mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.NO);
                        componentMaterialLot.setEnableFlag(HmeConstants.ConstantValue.NO);
                        if (HmeConstants.MaterialTypeCode.SN.equals(componentMaterialLot.getMaterialType())) {
                            HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                            hmeEoJobMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                            hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                            hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ONE);
                            updateSnDataList.add(hmeEoJobMaterial);
                        } else if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType())) {
                            HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
                            hmeEoJobLotMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                            deleteLotDataList.add(hmeEoJobLotMaterial);
                        } else if (HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                            HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
                            hmeEoJobTimeMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                            deleteTimeDataList.add(hmeEoJobTimeMaterial);
                        }
                    }
                    List<MtMaterialLotVO20> singleUpdateMaterialLotList = updateMaterialLotList.stream().filter(item2 -> item2.getMaterialLotId().equals(componentMaterialLot.getMaterialLotId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(singleUpdateMaterialLotList)) {
                        updateMaterialLotList.add(mtMaterialLotVO20);
                    } else {
                        singleUpdateMaterialLotList.get(0).setTrxPrimaryUomQty((BigDecimal.valueOf(singleUpdateMaterialLotList.get(0).getTrxPrimaryUomQty()).subtract(materialLotCurrentReleaseQty)).doubleValue());
                        singleUpdateMaterialLotList.get(0).setEnableFlag(mtMaterialLotVO20.getEnableFlag());
                    }

                    //????????????/??????????????????
                    if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType()) ||
                            HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                        HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                        hmeEoJobSnLotMaterial.setTenantId(tenantId);
                        if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType())) {
                            hmeEoJobSnLotMaterial.setLotMaterialId(componentMaterialLot.getJobMaterialId());
                        } else {
                            hmeEoJobSnLotMaterial.setTimeMaterialId(componentMaterialLot.getJobMaterialId());
                        }
                        hmeEoJobSnLotMaterial.setMaterialType(componentMaterialLot.getMaterialType());
                        hmeEoJobSnLotMaterial.setWorkcellId(snLine.getWorkcellId());
                        hmeEoJobSnLotMaterial.setMaterialId(component.getMaterialId());
                        hmeEoJobSnLotMaterial.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                        hmeEoJobSnLotMaterial.setReleaseQty(materialLotCurrentReleaseQty);
                        hmeEoJobSnLotMaterial.setIsReleased(componentMaterialLot.getIsReleased());
                        hmeEoJobSnLotMaterial.setJobId(snLine.getJobId());
                        hmeEoJobSnLotMaterial.setLocatorId(componentMaterialLot.getLocatorId());
                        hmeEoJobSnLotMaterial.setLotCode(componentMaterialLot.getLot());
                        hmeEoJobSnLotMaterial.setVirtualFlag(component.getVirtualFlag());
                        if (MapUtils.isNotEmpty(releaseData.getMaterialLotAttrMap())) {
                            hmeEoJobSnLotMaterial.setProductionVersion(releaseData.getMaterialLotAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.MATERIAL_VERSION), ""));
                        }
                        eoJobSnInsertDataList.add(hmeEoJobSnLotMaterial);
                    }

                    //???????????????????????????
                    if (HmeConstants.MaterialTypeCode.SN.equals(component.getProductionType()) && HmeConstants.ConstantValue.YES.equals(component.getUpgradeFlag())) {
                        if (loopCount++ == 0) {
                            HmeEoJobSnVO21 updateMaterialLot = new HmeEoJobSnVO21();
                            updateMaterialLot.setMaterialLotCode(componentMaterialLot.getMaterialLotCode());
                            updateMaterialLot.setMaterialId(componentMaterialLot.getMaterialId());
                            upGradeMaterialLotList.add(updateMaterialLot);
                        }
                    }

                    //????????????
                    componentMaterialLot.setPrimaryUomQty(componentMaterialLot.getPrimaryUomQty().subtract(materialLotCurrentReleaseQty));
                    component.setReleasedQty(component.getReleasedQty().add(materialLotCurrentReleaseQty));
                    if (HmeConstants.ConstantValue.NO.equals(component.getIsSubstitute())) {
                        component.setWillReleaseQty(component.getRequirementQty().subtract(component.getReleasedQty()));
                        if (component.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0) {
                            component.setWillReleaseQty(BigDecimal.ZERO);
                        }
                    } else {
                        if (!component.getMaterialId().equals(component.getComponentMaterialId())) {
                            mainComponentMaterial.setWillReleaseQty(mainComponentMaterial.getWillReleaseQty().subtract(materialLotCurrentReleaseQty));
                            if (mainComponentMaterial.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0) {
                                mainComponentMaterial.setWillReleaseQty(BigDecimal.ZERO);
                            }
                        }
                    }
                    if (!HmeConstants.ConstantValue.YES.equals(snLine.getReworkFlag())) {
                        HmeEoJobSnBatchVO15 rleasedRecord = releaseData.getReleasedRecordMap().getOrDefault(fetchGroupKey2(snLine.getEoId(), componentMaterialLot.getMaterialId()), null);
                        if (Objects.nonNull(rleasedRecord)) {
                            rleasedRecord.setReleaseQty(rleasedRecord.getReleaseQty().add(materialLotCurrentReleaseQty));
                        }
                    }
                }

                remainReleaseQty = remainReleaseQty.subtract(materialLotCurrentReleaseQty);
                if(remainReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                    break;
                }
            }

            //??????COS???????????????
            if(execReleaseFlag && HmeConstants.ConstantValue.YES.equals(component.getCosMaterialLotFlag())) {
                HmeEoJobSnSingleVO6 cosVirtualNum = new HmeEoJobSnSingleVO6();
                cosVirtualNum.setEoId(snLine.getEoId());
                cosVirtualNum.setVirtualIdList(component.getVirtualIdList());
                cosVirtualNumList.add(cosVirtualNum);

                //????????????
                List<HmeEoJobSnSingleVO7> hmeMaterialLotLoadList2 = releaseData.getHmeMaterialLotLoadList().stream().filter(item -> component.getVirtualIdList().contains(item.getVirtualId()))
                        .collect(Collectors.toList());
                for (HmeEoJobSnSingleVO7 hmeEoJobSnSingleVO7:hmeMaterialLotLoadList2
                ) {
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotLoadId(hmeEoJobSnSingleVO7.getMaterialLotLoadId());
                    hmeMaterialLotLoad.setMaterialLotId("");
                    hmeMaterialLotLoad.setStatus("USED");
                    hmeMaterialLotLoadList.add(hmeMaterialLotLoad);
                }

                //???????????????????????????
                HmeEoJobDataCalculationResultDTO hmeEoJobDataCalculationResultDTO = new HmeEoJobDataCalculationResultDTO();
                hmeEoJobDataCalculationResultDTO.setEoId(snLine.getEoId());
                hmeEoJobDataCalculationResultDTO.setReworkFlag(snLine.getReworkFlag());
                hmeEoJobDataCalculationResultDTO.setSiteId(snLine.getSiteId());
                hmeEoJobDataCalculationResultDTO.setOperationId(snLine.getOperationId());
                hmeEoJobDataCalculationResultDTO.setMaterialLotId(snLine.getMaterialLotId());
                hmeEoJobDataCalculationResultDTO.setEoJobDataRecordVOList(snLine.getDataRecordVOList());
                hmeEoJobDataCalculationResultDTOList.add(hmeEoJobDataCalculationResultDTO);
            }
        }

        if(execReleaseFlag){
            resultVO.setExecReleaseFlag(execReleaseFlag);
            resultVO.setObjectTransactionRequestList(objectTransactionRequestList);
            resultVO.setUpdateSnDataList(updateSnDataList);
            resultVO.setDeleteLotDataList(deleteLotDataList);
            resultVO.setDeleteTimeDataList(deleteTimeDataList);
            resultVO.setEoJobSnInsertDataList(eoJobSnInsertDataList);
            resultVO.setUpGradeMaterialLotList(upGradeMaterialLotList);
            resultVO.setMaterialInfoList(materialInfoList);
            resultVO.setUpdateMaterialLotList(updateMaterialLotList);
            resultVO.setUpdateRemainQtyList(updateRemainQtyList);
            resultVO.setMtInvOnhandQuantityVO9List(mtInvOnhandQuantityVO9List);
            resultVO.setCosVirtualNumList(cosVirtualNumList);
            resultVO.setHmeEoJobDataCalculationResultDTOList(hmeEoJobDataCalculationResultDTOList);
            resultVO.setHmeMaterialLotLoadList(hmeMaterialLotLoadList);
            resultVO.setEoComponentActualVO5List(eoComponentActualVO5List);
            resultVO.setWoComponentActualVO1List(woComponentActualVO1List);
        }

        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSnBatchVO4> release(Long tenantId, HmeEoJobSnSingleDTO dto) {
        //??????????????????
        if(CollectionUtils.isNotEmpty(dto.getComponentList())){
            dto.setComponentList(dto.getComponentList().stream().distinct().collect(Collectors.toList()));
            dto.setComponentList(dto.getComponentList().stream().filter(distinctByKey(item -> fetchGroupKey2(item.getMaterialId(),item.getLineNumber())))
                    .collect(Collectors.toList()));
        }
        long startDate = System.currentTimeMillis();
        //??????
        HmeEoJobSnSingleVO2 hmeEoJobSnSingleVO2 = releaseValidate(tenantId,dto);
        log.info("=================================>????????????-????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");

        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        startDate = System.currentTimeMillis();
        //????????????
        HmeEoJobSnSingleVO3 releaseData = releaseDataGet(tenantId,dto);
        log.info("=================================>????????????-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        releaseData.setMaterialLotAttrMap(hmeEoJobSnSingleVO2.getMaterialLotAttrMap());
        releaseData.setHmeMaterialLotLabCodeMap(hmeEoJobSnSingleVO2.getHmeMaterialLotLabCodeMap());
        releaseData.setVirtualJobMap(hmeEoJobSnSingleVO2.getVirtualJobMap());
        releaseData.setVirtualEoJobSnMap(hmeEoJobSnSingleVO2.getVirtualEoJobSnMap());
        releaseData.setVirtualComponentMaterialLotMap(hmeEoJobSnSingleVO2.getVirtualComponentMaterialLotMap());

        //????????????????????????
        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()))
                .collect(Collectors.toList());

        //?????????
        List<HmeEoJobSnBatchVO4> normalComponentList = dto.getComponentList().stream().filter(item -> !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())).collect(Collectors.toList());
        //?????????
        List<HmeEoJobSnBatchVO4> virtualList = dto.getComponentList().stream().filter(item ->
                HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
        //????????? && ???????????????
        List<HmeEoJobSnBatchVO4> virtualComponentList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(virtualList)) {
            virtualComponentList = dto.getComponentList().stream().filter(item -> (
                    (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()))
                            || HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()))).collect(Collectors.toList());
        }

        //?????????????????????
        HmeEoJobSnSingleDTO releaseDto = new HmeEoJobSnSingleDTO();
        releaseDto.setSnLineDto(dto.getSnLineDto());
        releaseDto.setIsFirstProcess(StringUtils.isBlank(dto.getIsFirstProcess()) ? HmeConstants.ConstantValue.NO : HmeConstants.ConstantValue.YES);

        //?????????????????????
        if(CollectionUtils.isNotEmpty(componentList)){
            // ??????????????????
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");

            // ????????????
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            releaseData.setEventRequestId(eventRequestId);
            releaseData.setEventId(eventId);
        }

        //???????????????
        Boolean normalExecApiFlag = false;
        HmeEoJobSnSingleVO4 normalResultVO = null;
        if(CollectionUtils.isNotEmpty(normalComponentList)){
            releaseDto.setComponentList(normalComponentList);
            startDate = System.currentTimeMillis();
            normalResultVO = normalRelease(tenantId,releaseDto,releaseData);
            log.info("=================================>????????????-?????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if(Objects.nonNull(normalResultVO)) {
                normalExecApiFlag = normalResultVO.getExecReleaseFlag();
            }
        }

        //???????????????
        Boolean virtualExecApiFlag = false;
        HmeEoJobSnSingleVO4 virtualResultVO = null;
        if(CollectionUtils.isNotEmpty(virtualComponentList)){
            releaseDto.setComponentList(virtualComponentList);
            startDate = System.currentTimeMillis();
            virtualResultVO = virtualRelease(tenantId,releaseDto,releaseData);
            log.info("=================================>????????????-?????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if(Objects.nonNull(virtualResultVO)) {
                virtualExecApiFlag = virtualResultVO.getExecReleaseFlag();
            }
        }

        //??????API
        if(normalExecApiFlag || virtualExecApiFlag) {
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList();
            List<HmeEoJobMaterial> updateSnDataList = new ArrayList<>();
            List<HmeEoJobLotMaterial> deleteLotDataList = new ArrayList<>();
            List<HmeEoJobTimeMaterial> deleteTimeDataList = new ArrayList<>();
            List<HmeEoJobSnLotMaterial> eoJobSnInsertDataList = new ArrayList<>();
            List<HmeEoJobSnVO21> upGradeMaterialLotList = new ArrayList<>();
            List<MtAssembleProcessActualVO11> materialInfoList = new ArrayList<>();
            List<MtMaterialLotVO20> updateMaterialLotList = new ArrayList<>();
            List<HmeEoJobSnLotMaterial> updateRemainQtyList = new ArrayList<>();
            List<MtInvOnhandQuantityVO9> mtInvOnhandQuantityVO9List = new ArrayList<>();
            List<HmeMaterialLotLabCode> insertMaterialLotLabCodeList = new ArrayList<>();
            List<HmeMaterialLotLabCode> updateMaterialLotLabCodeList = new ArrayList<>();
            //COS???????????????
            List<HmeEoJobSnSingleVO6> cosVirtualNumList = new ArrayList<>();
            //???????????????????????????????????????
            List<HmeEoJobDataCalculationResultDTO> hmeEoJobDataCalculationResultDTOList= new ArrayList<>();
            //????????????
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = new ArrayList<>();
            List<MtEoComponentActualVO5> eoComponentActualVO5List = new ArrayList<>();
            List<MtWoComponentActualVO1> woComponentActualVO1List = new ArrayList<>();
            //????????????API??????
            MtAssembleProcessActualVO16 mtAssembleProcessActualVO16 = new MtAssembleProcessActualVO16();
            MtAssembleProcessActualVO17 mtAssembleProcessActualVO17 = new MtAssembleProcessActualVO17();
            List<MtAssembleProcessActualVO17> eoInfoList = new ArrayList<>();
            List<MtMaterialLotVO32> mtMaterialList = new ArrayList<>();
            List<HmeObjectRecordLock> recordLockList = new ArrayList<>();
            List<MtCommonExtendVO6> materialLotAttrList = new ArrayList<>();
            // ??????SN
            List<MtMaterialLotVO33> snMaterialLotList = new ArrayList<>();
            if(normalExecApiFlag) {

                //V20210819 modify by penglin.sui for hui.ma ?????????????????????????????????????????????????????????
                if(CollectionUtils.isEmpty(normalResultVO.getObjectTransactionRequestList())){
                    //??????????????????,?????????
                    throw new MtException("HME_EO_JOB_SN_213", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_213", "HME"));
                }
                List<String> objectMaterialLotIdList = normalResultVO.getObjectTransactionRequestList().stream()
                        .map(WmsObjectTransactionRequestVO::getMaterialLotId)
                        .distinct()
                        .collect(Collectors.toList());
                for (HmeEoJobSnLotMaterial eoJobSnInsertData : normalResultVO.getEoJobSnInsertDataList()
                     ) {
                    if(!objectMaterialLotIdList.contains(eoJobSnInsertData.getMaterialLotId())){

                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(eoJobSnInsertData.getMaterialLotId());

                        //??????${1}??????????????????,?????????
                        throw new MtException("HME_EO_JOB_SN_214", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_214", "HME", mtMaterialLot.getMaterialLotCode()));
                    }
                }

                objectTransactionRequestList.addAll(normalResultVO.getObjectTransactionRequestList());
                updateSnDataList.addAll(normalResultVO.getUpdateSnDataList());
                deleteLotDataList.addAll(normalResultVO.getDeleteLotDataList());
                deleteTimeDataList.addAll(normalResultVO.getDeleteTimeDataList());
                eoJobSnInsertDataList.addAll(normalResultVO.getEoJobSnInsertDataList());
                upGradeMaterialLotList.addAll(normalResultVO.getUpGradeMaterialLotList());
                materialInfoList.addAll(normalResultVO.getMaterialInfoList());
                if(CollectionUtils.isNotEmpty(normalResultVO.getCosVirtualNumList())){
                    cosVirtualNumList.addAll(normalResultVO.getCosVirtualNumList());
                }
                if(CollectionUtils.isNotEmpty(normalResultVO.getHmeEoJobDataCalculationResultDTOList())){
                    hmeEoJobDataCalculationResultDTOList.addAll(normalResultVO.getHmeEoJobDataCalculationResultDTOList());
                }
                if(CollectionUtils.isNotEmpty(normalResultVO.getHmeMaterialLotLoadList())){
                    hmeMaterialLotLoadList.addAll(normalResultVO.getHmeMaterialLotLoadList());
                }
                if(CollectionUtils.isNotEmpty(normalResultVO.getInsertMaterialLotLabCodeList())){
                    insertMaterialLotLabCodeList.addAll(normalResultVO.getInsertMaterialLotLabCodeList());
                }
                if(CollectionUtils.isNotEmpty(normalResultVO.getUpdateMaterialLotLabCodeList())){
                    updateMaterialLotLabCodeList.addAll(normalResultVO.getUpdateMaterialLotLabCodeList());
                }
                eoComponentActualVO5List.addAll(normalResultVO.getEoComponentActualVO5List());
                woComponentActualVO1List.addAll(normalResultVO.getWoComponentActualVO1List());
                //???????????????
//                startDate = System.currentTimeMillis();
//                for (MtMaterialLotVO1 value : normalResultVO.getMaterialLotConsumeMap().values()) {
//                    mtMaterialLotRepository.materialLotConsume(tenantId, value);
//                }
//                log.info("=================================>????????????-?????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                //?????????????????????
                if(MapUtils.isNotEmpty(normalResultVO.getMaterialLotConsumeMap())) {
                    log.info("=================================>????????????-???????????????????????? IN");

                    for (HmeEoJobSnBatchVO4 component : dto.getComponentList()
                         ) {

                        if(CollectionUtils.isEmpty(component.getMaterialLotList())){
                            continue;
                        }

                        List<MtMaterialLotVO33> mtMaterialLotVO33List = new ArrayList<>();
                        Long sequence = 0L;
                        BigDecimal trxPrimaryUomQty = BigDecimal.ZERO;
                        for (HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 : component.getMaterialLotList()
                             ) {
                            if(!normalResultVO.getMaterialLotConsumeMap().containsKey(hmeEoJobSnBatchVO6.getMaterialLotId())){
                                continue;
                            }
                            MtMaterialLotVO1 mtMaterialLotVO1 = normalResultVO.getMaterialLotConsumeMap().get(hmeEoJobSnBatchVO6.getMaterialLotId());
                            MtMaterialLotVO33 mtMaterialLotVO33 = new MtMaterialLotVO33();
                            mtMaterialLotVO33.setMaterialLotId(mtMaterialLotVO1.getMaterialLotId());
                            mtMaterialLotVO33.setConsumeFlag(HmeConstants.ConstantValue.YES);
                            mtMaterialLotVO33.setSequence(sequence++);
                            mtMaterialLotVO33List.add(mtMaterialLotVO33);
                            trxPrimaryUomQty = trxPrimaryUomQty.add(BigDecimal.valueOf(mtMaterialLotVO1.getTrxPrimaryUomQty()));

                            if (HmeConstants.ConstantValue.SN.equals(hmeEoJobSnBatchVO6.getMaterialType())) {
                                snMaterialLotList.add(mtMaterialLotVO33);
                            }

                            //??????????????????
                            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                            hmeObjectRecordLockDTO.setFunctionName("??????/??????????????????????????????");
                            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
                            hmeObjectRecordLockDTO.setObjectRecordId(mtMaterialLotVO1.getMaterialLotId());
                            hmeObjectRecordLockDTO.setObjectRecordCode(hmeEoJobSnBatchVO6.getMaterialLotCode());
                            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                            recordLockList.add(hmeObjectRecordLock);
                        }

                        if(CollectionUtils.isNotEmpty(mtMaterialLotVO33List)) {
                            MtMaterialLotVO32 mtMaterialLotVO32 = new MtMaterialLotVO32();
                            mtMaterialLotVO32.setMaterialId(component.getMaterialId());
                            mtMaterialLotVO32.setMtMaterialLotVO33List(mtMaterialLotVO33List);
                            mtMaterialLotVO32.setTrxPrimaryUomQty(trxPrimaryUomQty.doubleValue());
                            mtMaterialList.add(mtMaterialLotVO32);
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(normalResultVO.getMaterialLotAttrList())) {
                    materialLotAttrList.addAll(normalResultVO.getMaterialLotAttrList());
                }
            }
            if(virtualExecApiFlag){
                objectTransactionRequestList.addAll(virtualResultVO.getObjectTransactionRequestList());
                updateSnDataList.addAll(virtualResultVO.getUpdateSnDataList());
                deleteLotDataList.addAll(virtualResultVO.getDeleteLotDataList());
                deleteTimeDataList.addAll(virtualResultVO.getDeleteTimeDataList());
                eoJobSnInsertDataList.addAll(virtualResultVO.getEoJobSnInsertDataList());
                upGradeMaterialLotList.addAll(virtualResultVO.getUpGradeMaterialLotList());
                materialInfoList.addAll(virtualResultVO.getMaterialInfoList());
                updateMaterialLotList.addAll(virtualResultVO.getUpdateMaterialLotList());
                updateRemainQtyList.addAll(virtualResultVO.getUpdateRemainQtyList());
                mtInvOnhandQuantityVO9List.addAll(virtualResultVO.getMtInvOnhandQuantityVO9List());
                if(CollectionUtils.isNotEmpty(virtualResultVO.getCosVirtualNumList())){
                    cosVirtualNumList.addAll(virtualResultVO.getCosVirtualNumList());
                }
                if(CollectionUtils.isNotEmpty(virtualResultVO.getHmeEoJobDataCalculationResultDTOList())){
                    hmeEoJobDataCalculationResultDTOList.addAll(virtualResultVO.getHmeEoJobDataCalculationResultDTOList());
                }
                if(CollectionUtils.isNotEmpty(virtualResultVO.getHmeMaterialLotLoadList())){
                    hmeMaterialLotLoadList.addAll(virtualResultVO.getHmeMaterialLotLoadList());
                }
                eoComponentActualVO5List.addAll(virtualResultVO.getEoComponentActualVO5List());
                woComponentActualVO1List.addAll(virtualResultVO.getWoComponentActualVO1List());

                //??????????????????
                if(CollectionUtils.isNotEmpty(updateMaterialLotList)) {
                    for (MtMaterialLotVO20 updateMaterialLot : updateMaterialLotList
                         ) {
                        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                        hmeObjectRecordLockDTO.setFunctionName("??????/??????????????????????????????");
                        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
                        hmeObjectRecordLockDTO.setObjectRecordId(updateMaterialLot.getMaterialLotId());
                        hmeObjectRecordLockDTO.setObjectRecordCode(updateMaterialLot.getMaterialLotCode());
                        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                        recordLockList.add(hmeObjectRecordLock);
                    }
                }
            }

            //V20210618 modify by penglin.sui for hui.ma ????????????
            if(CollectionUtils.isNotEmpty(recordLockList)) {
                hmeObjectRecordLockRepository.batchCommonLockObject(tenantId, recordLockList);
            }

            try {
                //?????????????????????
                if (CollectionUtils.isNotEmpty(mtMaterialList)) {
                    MtMaterialLotVO31 mtMaterialLotVO31 = new MtMaterialLotVO31();
                    mtMaterialLotVO31.setEventRequestId(releaseData.getEventRequestId());
                    mtMaterialLotVO31.setMtMaterialList(mtMaterialList);
                    mtMaterialLotVO31.setAllConsume(HmeConstants.ConstantValue.NO);
                    startDate = System.currentTimeMillis();
                    mtMaterialLotRepository.sequenceLimitMaterialLotBatchConsumeForNew(tenantId, mtMaterialLotVO31);
                    log.info("=================================>????????????-???????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                }

                //?????????????????????(???????????????)
                if (CollectionUtils.isNotEmpty(updateMaterialLotList)) {
                    startDate = System.currentTimeMillis();
                    mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateMaterialLotList, releaseData.getEventId(), HmeConstants.ConstantValue.NO);
                    log.info("=================================>????????????-???????????????????????????(???????????????)????????????" + (System.currentTimeMillis() - startDate) + "??????");
                }

                //???????????????????????????
                if(CollectionUtils.isNotEmpty(materialLotAttrList)){
                    mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr",
                            releaseData.getEventId(), materialLotAttrList);
                }

                //?????????????????????
                if (CollectionUtils.isNotEmpty(mtInvOnhandQuantityVO9List)) {
                    List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
                    for (MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 : mtInvOnhandQuantityVO9List
                    ) {
                        MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
                        mtInvOnhandQuantityVO13.setSiteId(mtInvOnhandQuantityVO9.getSiteId());
                        mtInvOnhandQuantityVO13.setLocatorId(mtInvOnhandQuantityVO9.getLocatorId());
                        mtInvOnhandQuantityVO13.setMaterialId(mtInvOnhandQuantityVO9.getMaterialId());
                        mtInvOnhandQuantityVO13.setLotCode(mtInvOnhandQuantityVO9.getLotCode());
                        mtInvOnhandQuantityVO13.setChangeQuantity(mtInvOnhandQuantityVO9.getChangeQuantity());
                        onhandList.add(mtInvOnhandQuantityVO13);
                    }

                    MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
                    mtInvOnhandQuantityVO16.setEventId(releaseData.getEventId());
                    mtInvOnhandQuantityVO16.setOnhandList(onhandList);
                    startDate = System.currentTimeMillis();
                    mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
                    log.info("=================================>????????????-?????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                }

                startDate = System.currentTimeMillis();
                if (!HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getDesignedReworkFlag())) {
                    //??????????????????API??????
                    mtAssembleProcessActualVO16.setOperationId(dto.getSnLineDto().getOperationId());
                    mtAssembleProcessActualVO16.setOperationBy(String.valueOf(userId));
                    mtAssembleProcessActualVO16.setWorkcellId(dto.getSnLineDto().getWorkcellId());
                    mtAssembleProcessActualVO16.setEventRequestId(releaseData.getEventRequestId());
                    mtAssembleProcessActualVO16.setParentEventId(releaseData.getEventId());
                    if (Objects.nonNull(releaseData.getHmeEoJobShift())) {
                        mtAssembleProcessActualVO16.setShiftCode(releaseData.getHmeEoJobShift().getShiftCode());
                        mtAssembleProcessActualVO16.setShiftDate(releaseData.getHmeEoJobShift().getShiftDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }
                    mtAssembleProcessActualVO16.setLocatorId(dto.getComponentList().get(0).getMaterialLotList().get(0).getLocatorId());

                    mtAssembleProcessActualVO17.setEoId(dto.getSnLineDto().getEoId());
                    mtAssembleProcessActualVO17.setRouterId(releaseData.getEoRouterMap().get(dto.getSnLineDto().getEoId()).getRouterId());
                    mtAssembleProcessActualVO17.setRouterStepId(dto.getSnLineDto().getEoStepId());
                    mtAssembleProcessActualVO17.setMaterialInfo(materialInfoList);
                    eoInfoList.add(mtAssembleProcessActualVO17);
                    mtAssembleProcessActualVO16.setEoInfo(eoInfoList);
                    //??????????????????API
                    mtAssembleProcessActualRepository.componentAssembleBatchProcess(tenantId, mtAssembleProcessActualVO16);
                } else {
                    //EO??????
                    for (MtEoComponentActualVO5 eoComponentActualVO5 : eoComponentActualVO5List
                    ) {
                        mtEoComponentActualRepository.eoComponentAssemble(tenantId, eoComponentActualVO5);
                    }
                    //WO??????
                    for (MtWoComponentActualVO1 woComponentActualVO1 : woComponentActualVO1List
                    ) {
                        mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, woComponentActualVO1);
                    }
                }
                log.info("=================================>????????????-????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                startDate = System.currentTimeMillis();
                //??????????????????API
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                log.info("=================================>????????????-??????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                int count = 0;
                startDate = System.currentTimeMillis();
                if (CollectionUtils.isNotEmpty(updateSnDataList)) {
                    List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_material_cid_s", updateSnDataList.size());
                    List<String> updateSnDataSqlList = new ArrayList<>();
                    for (HmeEoJobMaterial updateData : updateSnDataList) {
                        updateData.setCid(Long.valueOf(cidS.get(count)));
                        updateData.setLastUpdatedBy(userId);
                        updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                        updateSnDataSqlList.addAll(customDbRepository.getUpdateSql(updateData));
                        count++;
                    }
                    jdbcTemplate.batchUpdate(updateSnDataSqlList.toArray(new String[updateSnDataSqlList.size()]));
                }
                log.info("=================================>????????????-??????hme_eo_job_material????????????" + (System.currentTimeMillis() - startDate) + "??????");
                if (CollectionUtils.isNotEmpty(deleteLotDataList)) {
                    List<String> deleteLotDataSqlList = new ArrayList<>();
                    for (HmeEoJobLotMaterial deleteData : deleteLotDataList) {
                        deleteLotDataSqlList.addAll(customDbRepository.getDeleteSql(deleteData));
                    }
                    jdbcTemplate.batchUpdate(deleteLotDataSqlList.toArray(new String[deleteLotDataSqlList.size()]));
                }

                if (CollectionUtils.isNotEmpty(deleteTimeDataList)) {
                    List<String> deleteTimeDataSqlList = new ArrayList<>();
                    for (HmeEoJobTimeMaterial deleteData : deleteTimeDataList) {
                        deleteTimeDataSqlList.addAll(customDbRepository.getDeleteSql(deleteData));
                    }
                    jdbcTemplate.batchUpdate(deleteTimeDataSqlList.toArray(new String[deleteTimeDataSqlList.size()]));
                }

                if (CollectionUtils.isNotEmpty(updateRemainQtyList)) {
                    List<String> updateRemainQtySqlList = new ArrayList<>();
                    for (HmeEoJobSnLotMaterial deleteData : updateRemainQtyList) {
                        updateRemainQtySqlList.addAll(customDbRepository.getUpdateSql(deleteData));
                    }
                    jdbcTemplate.batchUpdate(updateRemainQtySqlList.toArray(new String[updateRemainQtySqlList.size()]));
                }
                startDate = System.currentTimeMillis();
                if (CollectionUtils.isNotEmpty(eoJobSnInsertDataList)) {
                    List<String> eoJobSnLotMaterialIdS = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_s", eoJobSnInsertDataList.size());
                    List<String> eoJobSnLotMaterialCidS = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_cid_s", eoJobSnInsertDataList.size());
                    count = 0;
                    List<String> eoJobSnInsertDataSqlList = new ArrayList<>();
                    for (HmeEoJobSnLotMaterial insertData : eoJobSnInsertDataList) {
                        insertData.setJobMaterialId(eoJobSnLotMaterialIdS.get(count));
                        insertData.setCid(Long.valueOf(eoJobSnLotMaterialCidS.get(count)));
                        insertData.setObjectVersionNumber(1L);
                        insertData.setCreatedBy(userId);
                        insertData.setLastUpdatedBy(userId);
                        Date date = CommonUtils.currentTimeGet();
                        insertData.setCreationDate(date);
                        insertData.setLastUpdateDate(date);
                        eoJobSnInsertDataSqlList.addAll(customDbRepository.getInsertSql(insertData));
                        count++;
                    }
                    jdbcTemplate.batchUpdate(eoJobSnInsertDataSqlList.toArray(new String[eoJobSnInsertDataSqlList.size()]));
                }
                log.info("=================================>????????????-??????hme_eo_job_sn_lot_material????????????" + (System.currentTimeMillis() - startDate) + "??????");
                startDate = System.currentTimeMillis();
                //????????????
                if (CollectionUtils.isNotEmpty(upGradeMaterialLotList)) {
                    HmeEoJobSnVO20 hmeEoJobSnVO20 = new HmeEoJobSnVO20();
                    hmeEoJobSnVO20.setSiteId(dto.getSnLineDto().getSiteId());
                    hmeEoJobSnVO20.setEoId(dto.getSnLineDto().getEoId());
                    hmeEoJobSnVO20.setWorkOrderId(dto.getSnLineDto().getWorkOrderId());
                    hmeEoJobSnVO20.setEoStepId(dto.getSnLineDto().getEoStepId());
                    hmeEoJobSnVO20.setSnMaterialLotId(dto.getSnLineDto().getMaterialLotId());
                    hmeEoJobSnVO20.setJobId(dto.getSnLineDto().getJobId());
                    hmeEoJobSnVO20.setMaterialLotList(upGradeMaterialLotList);
                    hmeEoJobSnRepository.snBatchUpgrade(tenantId, hmeEoJobSnVO20);
                }
                log.info("=================================>????????????-??????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                startDate = System.currentTimeMillis();
                //V20201229 add by penglin.sui for hui.ma ????????????????????? COS??????????????????????????????
                log.info("???????????? COS??????????????????????????????begin");
                for (HmeEoJobSnSingleVO6 cosVirtualNum : cosVirtualNumList
                ) {
                    log.info("????????????COS??????????????????????????????eoId:" + cosVirtualNum.getEoId());
                    hmeVirtualNumRepository.batchUpdateVirtualNumForRelease(tenantId, cosVirtualNum.getEoId(), cosVirtualNum.getVirtualIdList());
                }
                log.info("=================================>????????????-??????COS??????????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                //V20201229 add by penglin.sui for hui.ma ????????????????????? COS???????????????????????????????????????
                startDate = System.currentTimeMillis();
                boolean isCalculate = false;
                for (HmeEoJobDataCalculationResultDTO hmeEoJobDataCalculationResultDTO : hmeEoJobDataCalculationResultDTOList
                ) {
                    //V20210629 modify by penglin.sui for peng.zhao ??????????????????
//                    try {
                        hmeEoJobDataCalculationResultDTO.setUseSourceFlag(dto.getUseSourceFlag());
                        hmeEoJobDataCalculationResultDTO.setIsEquipmentFirstProcess(dto.getIsEquipmentFirstProcess());
                        hmeEoJobDataRecordRepository.queryResultForFirstProcess(tenantId, hmeEoJobDataCalculationResultDTO);
                        isCalculate = true;
//                    } catch (Exception ex) {
//                        log.error("<===== HmeEoJobSnSingleServiceImpl.release ?????????????????????????????????????????????", ex);
//                        isCalculate = false;
//                    }
                }
                log.info("=================================>????????????-??????????????????COS???????????????????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");

                //V20210628 modify by penglin.sui for peng.zhao ????????????
                if(isCalculate) {
                    // 20210723 modify by sanfeng.zhang for peng.zhao ????????? ?????????????????????
                    if (CollectionUtils.isNotEmpty(dto.getSnLineDto().getDataRecordVOList())) {
                        List<HmeEoJobDataRecordVO> dataRecordVOList = dto.getSnLineDto().getDataRecordVOList().stream().filter(item -> HmeConstants.ValueType.FORMULA.equals(item.getResultType()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(dataRecordVOList)) {
                            hmeEoJobDataRecordRepository.batchCalculationFormulaData(tenantId, dataRecordVOList);
                        }
                    }
                }

                //??????????????????
                if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                    List<String> updateMaterialLotLoadSqlList = new ArrayList<>();
                    List<String> cidS = customDbRepository.getNextKeys("hme_material_lot_load_cid_s", hmeMaterialLotLoadList.size());
                    count = 0;
                    for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoadList
                    ) {
                        hmeMaterialLotLoad.setLastUpdatedBy(userId);
                        hmeMaterialLotLoad.setLastUpdateDate(CommonUtils.currentTimeGet());
                        hmeMaterialLotLoad.setCid(Long.valueOf(cidS.get(count)));
                        updateMaterialLotLoadSqlList.addAll(customDbRepository.getUpdateSql(hmeMaterialLotLoad));
                        count++;
                    }
                    if (CollectionUtils.isNotEmpty(updateMaterialLotLoadSqlList)) {
                        jdbcTemplate.batchUpdate(updateMaterialLotLoadSqlList.toArray(new String[updateMaterialLotLoadSqlList.size()]));
                    }
                }

                //??????????????????
                startDate = System.currentTimeMillis();
                if (CollectionUtils.isNotEmpty(insertMaterialLotLabCodeList)) {
                    List<String> labCodeIdS = customDbRepository.getNextKeys("hme_material_lot_lab_code_s", insertMaterialLotLabCodeList.size());
                    List<String> labCodeCidS = customDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", insertMaterialLotLabCodeList.size());
                    count = 0;
                    List<String> insertMaterialLotLabCodeSqlList = new ArrayList<>();
                    for (HmeMaterialLotLabCode insertData : insertMaterialLotLabCodeList) {
                        insertData.setLabCodeId(labCodeIdS.get(count));
                        insertData.setCid(Long.valueOf(labCodeCidS.get(count)));
                        insertData.setObjectVersionNumber(1L);
                        insertData.setCreatedBy(userId);
                        insertData.setLastUpdatedBy(userId);
                        Date date = CommonUtils.currentTimeGet();
                        insertData.setCreationDate(date);
                        insertData.setLastUpdateDate(date);
                        insertMaterialLotLabCodeSqlList.addAll(customDbRepository.getInsertSql(insertData));
                        count++;
                    }
                    jdbcTemplate.batchUpdate(insertMaterialLotLabCodeSqlList.toArray(new String[insertMaterialLotLabCodeSqlList.size()]));
                }
                log.info("=================================>????????????-??????????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                //??????????????????
                startDate = System.currentTimeMillis();
                if (CollectionUtils.isNotEmpty(updateMaterialLotLabCodeList)) {
                    List<String> labCodeCidS = customDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", updateMaterialLotLabCodeList.size());
                    count = 0;
                    List<String> updateMaterialLotLabCodeSqlList = new ArrayList<>();
                    for (HmeMaterialLotLabCode updateData : updateMaterialLotLabCodeList
                    ) {
                        updateData.setLastUpdatedBy(userId);
                        updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                        updateData.setCid(Long.valueOf(labCodeCidS.get(count)));
                        updateMaterialLotLabCodeSqlList.addAll(customDbRepository.getUpdateSql(updateData));
                        count++;
                    }
                    if (CollectionUtils.isNotEmpty(updateMaterialLotLabCodeSqlList)) {
                        jdbcTemplate.batchUpdate(updateMaterialLotLabCodeSqlList.toArray(new String[updateMaterialLotLabCodeSqlList.size()]));
                    }
                }
                log.info("=================================>????????????-??????????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                //??????????????????????????????????????????
                startDate = System.currentTimeMillis();
                HmeFacYk hmeFacYk = releaseData.getHmeFacYkMap().getOrDefault(dto.getSnLineDto().getSnMaterialId(), null);
                if (Objects.nonNull(hmeFacYk)) {
                    if (Objects.nonNull(hmeFacYk.getStandardValue())) {
                        BigDecimal maxValue = hmeFacYk.getStandardValue();
                        BigDecimal minValue = hmeFacYk.getStandardValue();
                        if (Objects.nonNull(hmeFacYk.getAllowDiffer())) {
                            maxValue = maxValue.add(hmeFacYk.getAllowDiffer());
                            minValue = minValue.subtract(hmeFacYk.getAllowDiffer());
                            List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobDataRecordMapper.queryEoJobDataRecordOfJobId(tenantId, dto.getSnLineDto().getJobId());
                            if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                                List<String> updateEoJobDataRecordSqlList = new ArrayList<>();
                                List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_data_record_cid_s", hmeEoJobDataRecordList.size());
                                count = 0;
                                for (HmeEoJobDataRecord hmeEoJobDataRecord : hmeEoJobDataRecordList
                                ) {
                                    hmeEoJobDataRecord.setMaximalValue(maxValue);
                                    hmeEoJobDataRecord.setMinimumValue(minValue);
                                    hmeEoJobDataRecord.setLastUpdatedBy(userId);
                                    hmeEoJobDataRecord.setLastUpdateDate(CommonUtils.currentTimeGet());
                                    hmeEoJobDataRecord.setCid(Long.valueOf(cidS.get(count)));
                                    updateEoJobDataRecordSqlList.addAll(customDbRepository.getUpdateSql(hmeEoJobDataRecord));
                                    count++;
                                }
                                if (CollectionUtils.isNotEmpty(updateEoJobDataRecordSqlList)) {
                                    jdbcTemplate.batchUpdate(updateEoJobDataRecordSqlList.toArray(new String[updateEoJobDataRecordSqlList.size()]));
                                }
                            }
                        }
                    }
                }

                // 20210831 add by sanfeng.zhang for hui.gu ?????????????????????
                this.savePumpPosition(tenantId, snMaterialLotList, dto);
            }catch (Exception e){
                log.error("<=======HmeEoJobSingleServiceImpl.release Exception======>" + e.getMessage());
                throw new CommonException(e.getMessage());
            }finally {
                //??????
                hmeObjectRecordLockRepository.batchReleaseLock(tenantId , recordLockList , HmeConstants.ConstantValue.YES);
            }
            log.info("=================================>????????????-????????????????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            //?????????
            List<HmeEoJobSnBatchVO4> resultVOList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                    || (HmeConstants.ConstantValue.ZERO.equals(item.getIsReleased()) &&
                    (HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) ||
                            (!HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && item.getMaterialId().equals(item.getComponentMaterialId())))))
                    .collect(Collectors.toList());
            //???????????????????????????????????????????????????
            resultVOList.forEach(item -> {
                //??????/??????????????????????????????????????????????????????0
                if((HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getReworkFlag())
                        || HmeConstants.ConstantValue.YES.equals(item.getQtyAlterFlag()))
                        && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())){
                    item.setWillReleaseQty(BigDecimal.ZERO);
                }
                //?????????????????????
                if(CollectionUtils.isNotEmpty(item.getMaterialLotList())) {
                    item.getMaterialLotList().removeIf(item2 -> item2.getPrimaryUomQty().compareTo(BigDecimal.ZERO) <= 0);
                    item.setSelectedSnCount(BigDecimal.valueOf(item.getMaterialLotList().size()));
                    if (item.getSelectedSnCount().compareTo(BigDecimal.ZERO) <= 0) {
                        item.setSelectedSnQty(BigDecimal.ZERO);
                    } else {
                        item.setSelectedSnQty(item.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                    }
                }
            });
            return resultVOList;
        }

        //?????????
        List<HmeEoJobSnBatchVO4> resultVOList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                || (HmeConstants.ConstantValue.ZERO.equals(item.getIsReleased()) &&
                (HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) ||
                        (!HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && item.getMaterialId().equals(item.getComponentMaterialId())))))
                .collect(Collectors.toList());
        //??????/??????????????????????????????????????????????????????0
        resultVOList.forEach(item -> {
            if((HmeConstants.ConstantValue.YES.equals(dto.getSnLineDto().getReworkFlag())
                    || HmeConstants.ConstantValue.YES.equals(item.getQtyAlterFlag()))
                    && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())) {
                item.setWillReleaseQty(BigDecimal.ZERO);
            }
        });
        return resultVOList;
    }

    /**
     * ?????????????????????????????????
     *
     * @param tenantId
     * @param snMaterialLotList
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/8/31 14:27
     * @return void
     */
    private void savePumpPosition(Long tenantId, List<MtMaterialLotVO33> snMaterialLotList, HmeEoJobSnSingleDTO dto) {
        List<HmePumpModPositionHeader> headerList = new ArrayList<>();
        List<HmePumpModPositionLine> lineList = new ArrayList<>();
        List<LovValueDTO> positionList = lovAdapter.queryLovValue("HME.PUMP_POSITION", tenantId);
        Map<String, List<HmePumpCombVO>> pumpCombVOMap = new HashMap<>();
        for (MtMaterialLotVO33 mtMaterialLotVO33 : snMaterialLotList) {
            List<HmePumpCombVO> pumpCombVOList = hmeEoJobSnBatchMapper.queryPumpCombListByMaterialLotId(tenantId, mtMaterialLotVO33.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                HmePumpModPositionHeader positionHeader = new HmePumpModPositionHeader();
                positionHeader.setWorkOrderId(dto.getSnLineDto().getWorkOrderId());
                positionHeader.setEoId(dto.getSnLineDto().getEoId());
                positionHeader.setCombMaterialLotId(mtMaterialLotVO33.getMaterialLotId());
                positionHeader.setQty(BigDecimal.ONE);
                headerList.add(positionHeader);
                pumpCombVOMap.put(mtMaterialLotVO33.getMaterialLotId(), pumpCombVOList);
            }
        }

        if (CollectionUtils.isNotEmpty(headerList)) {
            List<String> positionHeaderIds = customDbRepository.getNextKeys("hme_pump_mod_position_header_s", headerList.size());
            String positionHeaderCid = customDbRepository.getNextKey("hme_pump_mod_position_header_cid_s");
            Integer indexNum = 0;
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            Date nowDate = CommonUtils.currentTimeGet();
            // ??????SN?????????????????????????????????  ?????????26 ?????????????????????????????? ??????????????????
            List<String> pumpModPositionLineList = hmePumpModPositionLineRepository.queryPumpPositionLineByEoId(tenantId, dto.getSnLineDto().getEoId());
            List<LovValueDTO> filterLovList = positionList.stream().filter(lov -> !pumpModPositionLineList.contains(lov.getValue())).sorted(Comparator.comparing(LovValueDTO::getOrderSeq)).collect(Collectors.toList());
            Integer subIndex = 0;
            for (HmePumpModPositionHeader positionHeader : headerList) {
                positionHeader.setTenantId(tenantId);
                positionHeader.setCreatedBy(userId);
                positionHeader.setCreationDate(nowDate);
                positionHeader.setLastUpdatedBy(userId);
                positionHeader.setLastUpdateDate(nowDate);
                positionHeader.setObjectVersionNumber(1L);
                positionHeader.setCid(Long.valueOf(positionHeaderCid));
                positionHeader.setPositionHeaderId(positionHeaderIds.get(indexNum++));

                List<HmePumpCombVO> pumpCombVOList = pumpCombVOMap.get(positionHeader.getCombMaterialLotId());
                if (filterLovList.size() < subIndex + pumpCombVOList.size()) {
                    throw new MtException("HME_PUMP_POSITION_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_PUMP_POSITION_001", "HME", "HME.PUMP_POSITION"));
                }
                List<LovValueDTO> valueDTOList = filterLovList.subList(subIndex, subIndex + pumpCombVOList.size());
                Integer lineIndex = 0;
                for (HmePumpCombVO line : pumpCombVOList) {
                    HmePumpModPositionLine positionLine = new HmePumpModPositionLine();
                    positionLine.setTenantId(tenantId);
                    positionLine.setPositionHeaderId(positionHeader.getPositionHeaderId());
                    positionLine.setMaterialLotId(line.getMaterialLotId());
                    positionLine.setSubBarcodeSeq(line.getSubBarcodeSeq());
                    positionLine.setPosition(valueDTOList.get(lineIndex++).getValue());
                    lineList.add(positionLine);
                }
                subIndex += pumpCombVOList.size();
            }
            // ????????????
            hmePumpModPositionHeaderRepository.myBatchInsert(headerList);
        }
        // ?????????
        if (CollectionUtils.isNotEmpty(lineList)) {
            hmePumpModPositionLineRepository.batchInsertSelective(lineList);
        }
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/22 14:24
     * @param tenantId ??????ID
     * @param hmeEoJobSnSingleBasic ??????
     * @param dto ??????
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn getResultData(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity();
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();

        hmeEoJobSnEntity.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        hmeEoJobSnEntity.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        hmeEoJobSnEntity.setSnMaterialCode(materialLot.getMaterialCode());
        hmeEoJobSnEntity.setSnMaterialName(materialLot.getMaterialName());
        hmeEoJobSnEntity.setWorkOrderNum(wo.getWorkOrderNum());
        hmeEoJobSnEntity.setRemark(wo.getRemark());

        //??????????????????/????????????
        Map<String, HmeRouterStepVO4> routerStepMap = hmeEoJobSnCommonService.batchQueryCurrentAndNextStep(tenantId, Collections.singletonList(hmeEoJobSnEntity.getEoStepId()));
        HmeRouterStepVO4 hmeRouterStep = routerStepMap.get(hmeEoJobSnEntity.getEoStepId());
        if (Objects.nonNull(hmeRouterStep)) {
            hmeEoJobSnEntity.setCurrentStepName(hmeRouterStep.getStepName());
            hmeEoJobSnEntity.setNextStepName(hmeRouterStep.getNextStepName());
        }

        return hmeEoJobSnEntity;
    }

    /**
     *
     * @Description ????????????
     *
     * @author yuchao.wang
     * @date 2020/11/21 15:11
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn outSiteValidate(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
        HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity();

        //V20210630 modify by penglin.sui for peng.zhao ??????????????????????????????SN????????????
        if(StringUtils.isNotBlank(materialLot.getStocktakeFlag()) &&
                HmeConstants.ConstantValue.YES.equals(materialLot.getStocktakeFlag())){
            //???SN???${1}???????????????,????????????
            throw new MtException("HME_EO_JOB_SN_204", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_204", "HME", materialLot.getMaterialLotCode()));
        }
        // 20210721 modify by sanfeng.zhang for xenxin.zhang ???????????????????????????Y???SN????????????
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getFreezeFlag())) {
            //??????SN???${1}???????????????,???????????????
            throw new MtException("HME_EO_JOB_SN_207", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_207", "HME", materialLot.getMaterialLotCode()));
        }

        //???????????????NG???????????????????????????
        if (HmeConstants.ConstantValue.NG.equals(materialLot.getQualityStatus())) {
            if (!HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag())
                    && !HmeConstants.ConstantValue.YES.equals(hmeEoJobSnEntity.getReworkFlag())) {
                //????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_060", "HME"));
            }

            SecurityTokenHelper.close();
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_EO_ID, dto.getEoId())
                            .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                            .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)
                            .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG, HmeConstants.ConstantValue.YES)).build());
            if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
                //??????????????????
                throw new MtException("HME_EO_JOB_SN_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_061", "HME"));
            }
        }

        //??????EoJobSn?????????????????????????????????
        if (Objects.nonNull(hmeEoJobSnEntity.getSiteOutDate())) {
            // 	?????????SN????????????????????????
            throw new MtException("HME_EO_JOB_SN_056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_056", "HME"));
        }

        // ??????????????????????????????????????????
        // 20211019 add by sanfeng.zhang for wenxin.zhang ?????? ????????????????????????????????????Y ?????????????????????
        boolean dataRecordValidateFlag = true;
        if (dto.getOutSiteAction() != null) {
            // ??????????????????????????????????????????
            boolean processValidateFlag = hmeEoJobSnCommonService.queryProcessValidateFlag(tenantId, dto.getWorkcellId());
            if (HmeConstants.OutSiteAction.REWORK.equals(dto.getOutSiteAction())) {
                if (HmeConstants.ConstantValue.NO.equals(materialLot.getReworkFlag())) {
                    // ??????????????????SN??????????????????????????????????????????????????????????????????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_037", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_037", "HME"));
                }
                if (!processValidateFlag) {
                    dataRecordValidateFlag = false;
                }
            }

            if (HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag())) {
                if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                    if (StringUtils.isBlank(dto.getContinueFlag())) {
                        dto.setContinueFlag(HmeConstants.ConstantValue.NO);
                    }
                    if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag())) {
                        HmeEoJobSn resultJobSn = new HmeEoJobSn();
                        // 20210825 modify by sanfeng.zhang for peng.zhao ?????????????????????????????????
                        if (!hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId()) && !hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId())) {
                            //??????????????????????????????????????? ????????????????????????
                            if (HmeConstants.ConstantValue.YES.equals(materialLot.getDesignedReworkFlag())) {
                                //???????????????????????????????????????,????????????
                                resultJobSn.setErrorCode("HME_EO_JOB_SN_164");
                                resultJobSn.setErrorMessage(mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_164", "HME"));
                            } else {
                                //?????????????????????,??????????????????????????????????????????, ????????????
                                resultJobSn.setErrorCode("HME_EO_JOB_SN_038");
                                resultJobSn.setErrorMessage(mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_038", "HME"));
                            }
                            return resultJobSn;
                        }
                    }
                    if (!processValidateFlag) {
                        dataRecordValidateFlag = false;
                    }
                }
            }
        }

        //?????????????????????
//        if (dataRecordValidateFlag
//                && hmeEoJobSnCommonService.hasMissingValue(tenantId, dto.getWorkcellId(), Collections.singletonList(dto.getJobId()))) {
//            // ????????????,??????????????????????????????????????????
//            throw new MtException("HME_EO_JOB_SN_009", mtErrorMessageRepository
//                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_009", "HME"));
//        }
        if(dataRecordValidateFlag){
            List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = hmeEoJobSnCommonService.hasMissingValueTag(tenantId, dto.getWorkcellId(), Collections.singletonList(dto.getJobId()));
            if(CollectionUtils.isNotEmpty(hmeEoJobDataRecordVOList)){
                // sn${1}???????????????${2}???????????????,???????????????!
                throw new MtException("HME_EO_JOB_SN_195", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_195", "HME",
                                dto.getSnNum() , hmeEoJobDataRecordVOList.get(0).getTagDescription()));
            }
        }

        //V20210331 modify by penglin.sui for tianyang.xie ???????????????????????????????????????????????????????????????
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())
            && HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getDoneStepFlag())) {
            if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                    // ?????????????????????????????????,???????????????!
//                    throw new MtException("HME_EO_JOB_SN_192", mtErrorMessageRepository
//                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_192", "HME"));
                    // 	sn${1}???????????????${2}?????????,???????????????!
                    throw new MtException("HME_EO_JOB_SN_196", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_196", "HME",
                                    dto.getSnNum(), ngDataRecordList.get(0).getTagDescription()));
                }
            }
        }

//        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            //????????????????????????????????? add by yuchao.wang for peng.zhao at 2021.1.22
            if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag()) || !"HME_EO_JOB_SN_172".equals(dto.getErrorCode())) {
                //???????????????????????????????????????
                List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobDataRecordRepository.queryForNcRecordValidate(tenantId, dto.getWorkcellId(), dto.getJobId());
                if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                    HmeEoJobSn dataRecordValidateResult = null;

                    HmeProcessNcHeaderVO2 processNcInfo = null;
                    //???????????????????????????????????????????????????
                    if (hmeEoJobSnCommonService.isAgeingNc(tenantId, dto.getOperationId())) {
                        //????????????
                        //??????????????????
                        String stationId = hmeEoJobSnMapper.queryWkcStation(tenantId, dto.getSiteId(), dto.getWorkcellId());

                        //??????????????????????????????
//                        String productCode = materialLot.getMaterialLotCode().substring(2, 4);

                        String cosModel = materialLot.getMaterialLotCode().substring(4, 5);

                        processNcInfo = hmeProcessNcHeaderRepository
                                .queryProcessNcInfoForAgeingNcRecordValidate(tenantId, materialLot.getMaterialId(), stationId, cosModel , materialLot.getMaterialLotCode(), dto.getOperationId());
                        //????????????????????????
                        dataRecordValidateResult = hmeEoJobSnCommonService
                                .dataRecordAgeingProcessNcValidate(tenantId, materialLot.getMaterialLotCode(), hmeEoJobDataRecordList, processNcInfo);
                    } else if (hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId())) {
                        //????????????
                        //??????????????????????????????
                        String productCode = materialLot.getMaterialLotCode().substring(2, 4);
                        String cosModel = materialLot.getMaterialLotCode().substring(4, 5);
                        String chipCombination = materialLot.getMaterialLotCode().substring(5, 6);
                        processNcInfo = hmeProcessNcHeaderRepository
                                .queryProcessNcInfoForNcRecordValidate(tenantId, dto.getOperationId(), materialLot.getMaterialId(), productCode, cosModel, chipCombination);
                        //????????????????????????
                        dataRecordValidateResult = hmeEoJobSnCommonService
                                .dataRecordProcessNcValidate(tenantId, materialLot.getMaterialLotCode(), hmeEoJobDataRecordList, processNcInfo);
                    } else if (hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId())) {
                        // ???????????????
                        //??????????????????????????????
                        String cosModel = materialLot.getMaterialLotCode().substring(4, 5);
                        String chipCombination = materialLot.getMaterialLotCode().substring(5, 6);
                        processNcInfo = hmeProcessNcHeaderRepository
                                .queryProcessNcInfoForReflectorNcRecordValidate(tenantId, dto.getOperationId(), materialLot.getMaterialId(), cosModel, chipCombination);
                        //????????????????????????
                        dataRecordValidateResult = hmeEoJobSnCommonService
                                .dataRecordReflectorProcessNcValidate(tenantId, materialLot.getMaterialLotCode(), hmeEoJobDataRecordList, processNcInfo);
                    }

                    if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {
                        //??????????????????????????????
                        dataRecordValidateResult.getHmeNcDisposePlatformDTO().setWorkcellId(dto.getWorkcellId());
                        dataRecordValidateResult.getHmeNcDisposePlatformDTO().setCurrentwWorkcellId(dto.getWorkcellId());
                        dataRecordValidateResult.getHmeNcDisposePlatformDTO().setFlag("2");
                        return dataRecordValidateResult;
                    }
                }
            }
//        }

        //??????????????????????????????????????????????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.9.12
        if (StringUtils.isNotBlank(dto.getContainerId())) {
            MtContLoadDtlVO10 contLoadDtlParam = new MtContLoadDtlVO10();
            contLoadDtlParam.setContainerId(dto.getContainerId());
            contLoadDtlParam.setAllLevelFlag(HmeConstants.ConstantValue.NO);
            List<MtContLoadDtlVO4> mtContLoadDtlVo1List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlParam);

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

        if (!HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag())) {
            //???????????????????????????????????????????????????
            if (CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getBackFlushBomComponentList())) {
                List<String> backFlushBomComponentIdList = hmeEoJobSnSingleBasic.getBackFlushBomComponentList().stream()
                        .map(MtBomComponent::getBomComponentId).collect(Collectors.toList());
                List<MtMaterial> virtualMtMaterialList = hmeEoJobSnLotMaterialMapper.selectVirtualMaterial(tenantId, backFlushBomComponentIdList);
                if (CollectionUtils.isNotEmpty(virtualMtMaterialList)) {
                    List<String> virtualMaterialCodeList = virtualMtMaterialList.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
                    String materialCodes = String.join(",", virtualMaterialCodeList);
                    //????????????${1}?????????????????????,?????????
                    throw new MtException("HME_EO_JOB_SN_080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_080", "HME", materialCodes));
                }

                //??????bom??????
                HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
                HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
                //???????????????????????????????????????Y?????????????????????
                if (!HmeConstants.ConstantValue.YES.equals(wo.getBackflushNotFlag())
                        && CollectionUtils.isNotEmpty(eo.getBomComponentList())
                        && CollectionUtils.isNotEmpty(wo.getBomComponentList())) {
                    Double bomQty = eo.getBomPrimaryQty();
                    if (Objects.isNull(bomQty)) {
                        //${1}????????? ?????????${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "BOM", eo.getBomId()));
                    }
                    BigDecimal bomPrimaryQty = BigDecimal.valueOf(bomQty);
                    if(bomPrimaryQty.compareTo(BigDecimal.ZERO) == 0){
                        //???????????????0,?????????
                        throw new MtException("HME_EO_JOB_SN_082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_082", "HME"));
                    }
                }
            }

            //????????????????????????
            outSiteReleaseValidate(tenantId, hmeEoJobSnSingleBasic, dto);
        }else{
            //????????????????????????
            if(!HmeConstants.ConstantValue.YES.equals(materialLot.getDesignedReworkFlag())) {
                outSiteReworkReleaseValidate(tenantId, hmeEoJobSnSingleBasic, dto);
            }
        }

        //????????????????????????
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            outSiteContainerLoadVerify(tenantId, hmeEoJobSnSingleBasic, dto);
        }

        //V20210129 modify by penglin.sui for hui.ma ??????????????????
        if(StringUtils.isNotBlank(dto.getLabCode())){
            //??????????????????
            List<String> snMaterialLotIdList = new ArrayList<>();
            snMaterialLotIdList.add(dto.getMaterialLotId());
            Map<String , List<HmeMaterialLotLabCode>> hmeMaterialLotLabCodeMap = new HashMap<>();
            List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = hmeMaterialLotLabCodeMapper.batchSelectLabCode(tenantId,dto.getEoStepId(),snMaterialLotIdList);
            if(CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList)){
                hmeMaterialLotLabCodeMap = hmeMaterialLotLabCodeList.stream().collect(Collectors.groupingBy(e -> e.getMaterialLotId()));
            }
            List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList1 = hmeMaterialLotLabCodeMap.getOrDefault(dto.getMaterialLotId() , new ArrayList<>());
            if(CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList1)){
                List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList2 = hmeMaterialLotLabCodeList1.stream().filter(item -> dto.getLabCode().equals(item.getLabCode()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isEmpty(hmeMaterialLotLabCodeList2)){
                    //????????????????????????????????????${1}??????????????????????????????${2}????????????,?????????
                    throw new MtException("HME_EO_JOB_SN_180", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_180", "HME" , dto.getLabCode() , hmeMaterialLotLabCodeList1.get(0).getLabCode()));
                }
            }
        }
        return null;
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/14 9:26
     * @param tenantId ??????ID
     * @param hmeEoJobSnSingleBasic ??????
     * @param dto ??????
     * @return void
     *
     */
    private void outSiteContainerLoadVerify(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        String containerId = hmeEoJobSnSingleBasic.getEoJobContainer().getContainerId();

        //????????????????????????
        HmeContainerVO containerInfo = hmeEoJobSnMapper.queryContainerInfo(tenantId, containerId);
        if (Objects.isNull(containerInfo) || StringUtils.isBlank(containerInfo.getContainerId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //?????????????????????,??????????????????????????????
        if (!HmeConstants.StatusCode.CANRELEASE.equals(containerInfo.getStatus())) {
            throw new MtException("HME_LOAD_CONTAINER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_002", "HME", containerInfo.getContainerCode()));
        }

        //??????????????????????????????
        List<HmeContainerDetailVO> containerDetailList = hmeEoJobSnMapper.queryContainerDetails(tenantId, containerId);
        if (CollectionUtils.isEmpty(containerDetailList)) {
            return;
        }

        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();

        //????????????????????????,?????????????????????????????????
        if (Objects.nonNull(containerInfo.getCapacityQty())) {
            //????????????????????????
            double totalQty = containerDetailList.stream().filter(item -> Objects.nonNull(item.getPrimaryUomQty()))
                    .mapToDouble(HmeContainerDetailVO::getPrimaryUomQty).sum();

            //???????????????EO??????
            totalQty += eo.getQty();

            if (totalQty > containerInfo.getCapacityQty()) {
                //??????????????????
                throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //????????????????????????????????????
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedMaterialFlag())) {
            //?????????????????????
            String materialId = materialLot.getMaterialId();

            //??????????????????????????????
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
            //???????????????EO
            String eoId = eo.getEoId();

            //??????????????????EO??????
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
            //???????????????WO
            String woId = hmeEoJobSnSingleBasic.getWo().getWorkOrderId();

            //??????????????????WO??????
            List<String> woIdList = containerDetailList.stream().map(HmeContainerDetailVO::getWorkOrderId)
                    .distinct().collect(Collectors.toList());

            if (woIdList.size() != 1 || !woIdList.contains(woId)) {
                //?????????????????????WO
                throw new MtException("MT_MATERIAL_LOT_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0047", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //??????????????????????????????????????????,????????????????????????????????????OK????????????
        String materialLotQualityStatus = materialLot.getQualityStatus();
        String materialLotReworkFlag = materialLot.getReworkFlag();
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            materialLotQualityStatus = HmeConstants.ConstantValue.OK;
            materialLotReworkFlag = HmeConstants.ConstantValue.NO;
        }

        //????????????????????????????????????
        String finalStatus = materialLotQualityStatus;
        long diffCount = containerDetailList.stream().filter(item -> !finalStatus.equals(item.getQualityStatus())).count();
        if (diffCount > 0) {
            //??????????????????????????????????????????????????????????????????!
            throw new MtException("HME_EO_JOB_SN_156", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_156", "HME"));
        }

        //????????????????????????????????????
        if (HmeConstants.ConstantValue.YES.equals(materialLotReworkFlag)) {
            diffCount = containerDetailList.stream().filter(item -> !HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())).count();
        } else {
            diffCount = containerDetailList.stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())).count();
        }
        if (diffCount > 0) {
            //??????????????????????????????????????????????????????????????????!
            throw new MtException("HME_EO_JOB_SN_156", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_156", "HME"));
        }
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/9 15:22
     * @param tenantId ??????ID
     * @param hmeEoJobSnSingleBasic ??????
     * @param dto ??????
     * @return void
     *
     */
    private void outSiteReleaseValidate(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        //????????????,????????????????????????????????????
        List<LovValueDTO> inputWoTypeValues = lovAdapter.queryLovValue("HME.INPUT_WO_TYPE", tenantId);
        if (CollectionUtils.isEmpty(inputWoTypeValues)) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Return:??????HME.INPUT_WO_TYPE??????");
            return;
        }
        List<String> valueList = inputWoTypeValues.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        //??????????????????????????????,??????????????????????????????,????????????????????????Y ????????????????????????
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
        if (!valueList.contains(wo.getWorkOrderType()) || HmeConstants.ConstantValue.YES.equals(wo.getIssuedFlag())) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Return:??????????????????????????? valueList={}", valueList);
            return;
        }

        //?????????????????????ID
        List<String> backFlushBomComponentIdList = hmeEoJobSnSingleBasic.getBackFlushBomComponentList()
                .stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList());

        //??????EO?????????????????????,??????(??????????????????Y && ???????????????) mainComponentMap key:???????????????ID value:???????????????
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        Map<String, HmeBomComponentVO> mainComponentMap = new HashMap<>();
        List<String> mainComponentIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eo.getBomComponentList())) {
            eo.getBomComponentList().forEach(component -> {
                if (!backFlushBomComponentIdList.contains(component.getBomComponentId())
                        && !HmeConstants.ConstantValue.YES.equals(component.getIssuedFlag())
                        && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(component.getVirtualFlag())) {
                    //??????????????????ID
                    mainComponentIdList.add(component.getBomComponentId());
                    //??????????????????
                    mainComponentMap.put(component.getBomComponentId(), component);
                }
            });
        }
        if (CollectionUtils.isEmpty(mainComponentIdList)) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Return:?????????????????????EO?????? eo={}", eo);
            return;
        }

        //????????????????????????????????? substituteMaterialComponentMap key:???????????????ID value:????????????????????????ID
        Map<String, List<String>> substituteMaterialComponentMap = new HashMap<>();
        String eoStepId = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity().getEoStepId();
        List<HmeBomComponentVO> substituteMaterialComponentList = hmeEoJobSnMapper.batchQuerySubstituteMaterialComponent(tenantId, eoStepId, mainComponentIdList);
        log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate ????????????????????????{}???,eoStepId={},mainComponentIdList={}",
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
        List<String> allComponentIdList = new ArrayList<>(mainComponentIdList);
        if (CollectionUtils.isNotEmpty(substituteMaterialComponentList)) {
            List<String> substituteMaterialComponentIdList = substituteMaterialComponentList.stream()
                    .map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());
            allComponentIdList.addAll(substituteMaterialComponentIdList);
        }
        SecurityTokenHelper.close();
        List<MtEoComponentActual> eoComponentActualList = mtEoComponentActualRepository.selectByCondition(Condition.builder(MtEoComponentActual.class)
                .select(MtEoComponentActual.FIELD_BOM_COMPONENT_ID, MtEoComponentActual.FIELD_ASSEMBLE_QTY)
                .andWhere(Sqls.custom().andEqualTo(MtEoComponentActual.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtEoComponentActual.FIELD_EO_ID, eo.getEoId())
                        .andIn(MtEoComponentActual.FIELD_BOM_COMPONENT_ID, allComponentIdList)).build());

        //?????????????????????????????????????????????
        List<String> mainComponentMaterialIdList = mainComponentMap.values().stream()
                .map(HmeBomComponentVO::getBomComponentMaterialId).distinct().collect(Collectors.toList());
        List<HmeEoComponentActualVO> eoGlobalMaterialActualList = hmeEoJobSnMapper.queryEoActualForGlobalMaterial(tenantId,
                dto.getOperationId(), eoStepId, mainComponentMaterialIdList, eo.getEoId());

        //????????????????????????????????????????????? ?????????????????? <= ????????????????????????????????????
        for (Map.Entry<String, HmeBomComponentVO> mainComponentEntry : mainComponentMap.entrySet()) {
            //????????????component
            String componentId = mainComponentEntry.getKey();
            HmeBomComponentVO mainComponent = mainComponentEntry.getValue();

            //??????????????????????????????????????????ID
            List<String> currComponentIdList = new ArrayList<>();
            currComponentIdList.add(componentId);
            if (substituteMaterialComponentMap.containsKey(componentId)) {
                currComponentIdList.addAll(substituteMaterialComponentMap.get(componentId));
            }

            //????????????????????????????????????????????????
            double sumComponentActual = eoComponentActualList.stream()
                    .filter(item -> currComponentIdList.contains(item.getBomComponentId())
                            && Objects.nonNull(item.getAssembleQty())).mapToDouble(MtEoComponentActual::getAssembleQty).sum();

            //????????????????????????????????????
            List<HmeEoComponentActualVO> currEoGlobalMaterialActualList = eoGlobalMaterialActualList.stream()
                    .filter(item -> item.getMainMaterialId().equals(mainComponent.getBomComponentMaterialId())
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
            double addQty = BigDecimal.valueOf(sumComponentActual).add(BigDecimal.valueOf(sumGlobalMaterialActual)).doubleValue();
            if (mainComponent.getBomComponentQty() > (addQty)) {
                log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Exception:BomComponentQty={},sumComponentActual={},sumGlobalMaterialActual={}",
                        mainComponent.getBomComponentQty(), sumComponentActual, sumGlobalMaterialActual);
                log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Exception:eoId={},mainComponentId={},currComponentIdList={}",
                        eo.getEoId(), componentId, currComponentIdList);

                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mainComponent.getBomComponentMaterialId());
                //SN???${1}??????????????????${2}??????????????????????????????,????????????
                throw new MtException("HME_EO_JOB_SN_139", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_139", "HME", dto.getSnNum(), mtMaterial.getMaterialCode()));
            }
        }
    }

    /**
     *
     * @Description ????????????????????????-????????????
     *
     * @author yuchao.wang
     * @date 2020/12/9 15:22
     * @param tenantId ??????ID
     * @param hmeEoJobSnSingleBasic ??????
     * @param dto ??????
     * @return void
     *
     */
    private void outSiteReworkReleaseValidate(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        //????????????,????????????????????????????????????
        List<LovValueDTO> inputWoTypeValues = lovAdapter.queryLovValue("HME.INPUT_WO_TYPE", tenantId);
        if (CollectionUtils.isEmpty(inputWoTypeValues)) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Return:??????HME.INPUT_WO_TYPE??????");
            return;
        }
        List<String> valueList = inputWoTypeValues.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        //??????????????????????????????,??????????????????????????????,????????????????????????Y ????????????????????????
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
        if (!valueList.contains(wo.getWorkOrderType()) || HmeConstants.ConstantValue.YES.equals(wo.getIssuedFlag())) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Return:??????????????????????????? valueList={}", valueList);
            return;
        }

        //?????????????????????ID
        List<String> backFlushBomComponentIdList = hmeEoJobSnSingleBasic.getBackFlushBomComponentList()
                .stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList());

        //????????????,????????????????????????????????????
        List<LovValueDTO> cosItemGroupValues = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<String> cosItemGroupValue = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(cosItemGroupValues)) {
            cosItemGroupValue = cosItemGroupValues.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }

        //??????EO?????????????????????,??????(??????????????????Y && ???????????????) mainComponentMap key:???????????????ID value:???????????????
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        Map<String, HmeBomComponentVO> mainComponentMap = new HashMap<>();
        List<String> mainComponentIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eo.getBomComponentList())) {
            for (HmeBomComponentVO component:eo.getBomComponentList()
                 ) {
                if (!backFlushBomComponentIdList.contains(component.getBomComponentId())
                        && !HmeConstants.ConstantValue.YES.equals(component.getIssuedFlag())
                        && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(component.getVirtualFlag())
                        && !cosItemGroupValue.contains(component.getItemGroup())) {
                    //??????????????????ID
                    mainComponentIdList.add(component.getBomComponentId());
                    //??????????????????
                    mainComponentMap.put(component.getBomComponentId(), component);
                }
            }
        }
        if (CollectionUtils.isEmpty(mainComponentIdList)) {
            log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Return:?????????????????????EO?????? eo={}", eo);
            return;
        }

        //????????????????????????????????? substituteMaterialComponentMap key:???????????????ID value:????????????????????????ID
        Map<String, List<String>> substituteMaterialComponentMap = new HashMap<>();
        String eoStepId = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity().getEoStepId();
        List<HmeBomComponentVO> substituteMaterialComponentList = hmeEoJobSnMapper.batchQuerySubstituteMaterialComponent(tenantId, eoStepId, mainComponentIdList);
        log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate ????????????????????????{}???,eoStepId={},mainComponentIdList={}",
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
        List<String> allComponentIdList = new ArrayList<>(mainComponentIdList);
        if (CollectionUtils.isNotEmpty(substituteMaterialComponentList)) {
            List<String> substituteMaterialComponentIdList = substituteMaterialComponentList.stream()
                    .map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());
            allComponentIdList.addAll(substituteMaterialComponentIdList);
        }
        SecurityTokenHelper.close();
        List<MtEoComponentActual> eoComponentActualList = mtEoComponentActualRepository.selectByCondition(Condition.builder(MtEoComponentActual.class)
                .select(MtEoComponentActual.FIELD_BOM_COMPONENT_ID, MtEoComponentActual.FIELD_ASSEMBLE_QTY)
                .andWhere(Sqls.custom().andEqualTo(MtEoComponentActual.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtEoComponentActual.FIELD_EO_ID, eo.getEoId())
                        .andIn(MtEoComponentActual.FIELD_BOM_COMPONENT_ID, allComponentIdList)).build());

        //?????????????????????????????????????????????
        List<String> mainComponentMaterialIdList = mainComponentMap.values().stream()
                .map(HmeBomComponentVO::getBomComponentMaterialId).distinct().collect(Collectors.toList());
        List<HmeEoComponentActualVO> eoGlobalMaterialActualList = hmeEoJobSnMapper.queryEoActualForGlobalMaterial(tenantId,
                dto.getOperationId(), eoStepId, mainComponentMaterialIdList, eo.getEoId());

        //????????????????????????????????????????????? ?????????????????? <= ????????????????????????????????????
        for (Map.Entry<String, HmeBomComponentVO> mainComponentEntry : mainComponentMap.entrySet()) {
            //????????????component
            String componentId = mainComponentEntry.getKey();
            HmeBomComponentVO mainComponent = mainComponentEntry.getValue();

            //??????????????????????????????????????????ID
            List<String> currComponentIdList = new ArrayList<>();
            currComponentIdList.add(componentId);
            if (substituteMaterialComponentMap.containsKey(componentId)) {
                currComponentIdList.addAll(substituteMaterialComponentMap.get(componentId));
            }

            //????????????????????????????????????????????????
            double sumComponentActual = eoComponentActualList.stream()
                    .filter(item -> currComponentIdList.contains(item.getBomComponentId())
                            && Objects.nonNull(item.getAssembleQty())).mapToDouble(MtEoComponentActual::getAssembleQty).sum();

            //????????????????????????????????????
            List<HmeEoComponentActualVO> currEoGlobalMaterialActualList = eoGlobalMaterialActualList.stream()
                    .filter(item -> item.getMainMaterialId().equals(mainComponent.getBomComponentMaterialId())
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
                log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Exception:BomComponentQty={},sumComponentActual={},sumGlobalMaterialActual={}",
                        mainComponent.getBomComponentQty(), sumComponentActual, sumGlobalMaterialActual);
                log.info("<====== HmeEoJobSnSingleServiceImpl.outSiteReleaseValidate Exception:eoId={},mainComponentId={},currComponentIdList={}",
                        eo.getEoId(), componentId, currComponentIdList);

                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mainComponent.getBomComponentMaterialId());
                //SN???${1}??????????????????${2}??????????????????????????????,????????????
                throw new MtException("HME_EO_JOB_SN_139", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_139", "HME", dto.getSnNum(), mtMaterial.getMaterialCode()));
            }
        }
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/21 14:34
     * @param tenantId ??????ID
     * @param dto ??????
     * @return com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO
     *
     */
    private HmeEoJobSnSingleBasicVO queryBasicData(Long tenantId, HmeEoJobSnVO3 dto) {
        //??????JobId??????EoJobSn??????
        SecurityTokenHelper.close();
        HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
        if (Objects.isNull(hmeEoJobSnEntity) || StringUtils.isBlank(hmeEoJobSnEntity.getJobId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //?????????????????? ?????????????????????????????????????????????sn??????????????????????????????
        HmeMaterialLotVO3 materialLot = hmeEoJobSnCommonService.queryMaterialLotInfo(tenantId, hmeEoJobSnEntity.getMaterialLotId());
        dto.setMaterialLotId(materialLot.getMaterialLotId());
        dto.setSnNum(materialLot.getMaterialLotCode());

        //???????????????????????????????????????
        HmeEoRouterBomRelVO eoRouterBomRel = null;
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag()) && HmeConstants.ConstantValue.YES.equals(materialLot.getDesignedReworkFlag())) {
            materialLot.setDesignedReworkFlag(HmeConstants.ConstantValue.YES);

            eoRouterBomRel = hmeEoRouterBomRelRepository.queryLastRelByEoId(tenantId, dto.getEoId());
            if (Objects.isNull(eoRouterBomRel) || StringUtils.isBlank(eoRouterBomRel.getEoRouterBomRelId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "EO ???????????? ??????????????????"));
            }
        } else {
            materialLot.setDesignedReworkFlag(HmeConstants.ConstantValue.NO);
        }

        //??????EO???WO????????????
        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic = new HmeEoJobSnSingleBasicVO();
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getDesignedReworkFlag())) {
            hmeEoJobSnSingleBasic = hmeEoJobSnCommonService.queryEoAndWoInfoWithCompByEoIdForDesignedRework(tenantId, hmeEoJobSnEntity.getEoStepId(), dto.getEoId(), eoRouterBomRel);
        } else {
            hmeEoJobSnSingleBasic = hmeEoJobSnCommonService.queryEoAndWoInfoWithComponentByEoId(tenantId, hmeEoJobSnEntity.getEoStepId(), dto.getEoId());
        }
        hmeEoJobSnSingleBasic.setHmeEoJobSnEntity(hmeEoJobSnEntity);
        hmeEoJobSnSingleBasic.setMaterialLot(materialLot);
        hmeEoJobSnSingleBasic.setEoRouterBomRel(eoRouterBomRel);

        //????????????????????????
        hmeEoJobSnSingleBasic.setContainerOutSiteFlag(hmeEoJobSnCommonService.isContainerOutSite(tenantId, dto.getWorkcellId()));

        //????????????????????????ID
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            List<HmeEoJobContainer> hmeEoJobContainers = hmeEoJobContainerRepository.selectByCondition(Condition.builder(HmeEoJobContainer.class)
                    .select(HmeEoJobContainer.FIELD_JOB_CONTAINER_ID, HmeEoJobContainer.FIELD_CONTAINER_ID)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobContainer.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobContainer.FIELD_WORKCELL_ID, dto.getWorkcellId())).build());
            if (CollectionUtils.isEmpty(hmeEoJobContainers)
                    || Objects.isNull(hmeEoJobContainers.get(0))
                    || StringUtils.isBlank(hmeEoJobContainers.get(0).getJobContainerId())) {
                throw new MtException("HME_EO_JOB_SN_010", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_010", "HME"));
            }
            hmeEoJobSnSingleBasic.setEoJobContainer(hmeEoJobContainers.get(0));
        }

        //??????????????????????????????????????????????????????
        if (!HmeConstants.ConstantValue.YES.equals(materialLot.getDesignedReworkFlag()) && CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getEo().getBomComponentList())) {
            List<String> bomComponentIdList = hmeEoJobSnSingleBasic.getEo().getBomComponentList().stream()
                    .map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());
            List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnLotMaterialMapper.selectBackFlash(tenantId, dto.getSiteId(), bomComponentIdList);
            hmeEoJobSnSingleBasic.setBackFlushBomComponentList(backFlushBomComponentList);
        } else {
            hmeEoJobSnSingleBasic.setBackFlushBomComponentList(new ArrayList<MtBomComponent>());
        }

        //?????????????????????
        HmeRouterStepVO nearStepVO = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, dto.getEoId());
        if (Objects.isNull(nearStepVO) || StringUtils.isBlank(nearStepVO.getEoStepActualId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }
        hmeEoJobSnSingleBasic.setNearStep(nearStepVO);

        //????????????????????????
        MtRouterStep currentRouterStep = mtRouterStepRepository.routerStepGet(tenantId, hmeEoJobSnEntity.getEoStepId());
        if (Objects.isNull(currentRouterStep) || StringUtils.isBlank(currentRouterStep.getRouterStepId())) {
            //${1}????????? ?????????${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "routerStep", hmeEoJobSnEntity.getEoStepId()));
        }
        hmeEoJobSnSingleBasic.setCurrentStep(currentRouterStep);

        //????????????????????????
        hmeEoJobSnSingleBasic.setDoneStepFlag(mtRouterDoneStepRepository.doneStepValidate(tenantId, dto.getEoStepId()));

        //????????????????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getDoneStepFlag())) {
            //????????????HME.NOT_CREATE_BATCH_NUM
            List<String> createBatchNumList = new ArrayList<>();
            List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.NOT_CREATE_BATCH_NUM", tenantId);
            if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                createBatchNumList = lovValueDTOList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            }

            //??????????????????
            String itemType = hmeEoJobSnSingleBasic.getEo().getItemType();
            if (StringUtils.isBlank(itemType) || !createBatchNumList.contains(itemType)) {
                //?????????????????? HME_MATERIAL_BATCH_NUM ????????????
                hmeEoJobSnSingleBasic.setLotCode(profileClient.getProfileValueByOptions("HME_MATERIAL_BATCH_NUM"));
                if (StringUtils.isBlank(hmeEoJobSnSingleBasic.getLotCode())) {
                    //????????????????????????,?????????????????????????????????????????????????????????HME_MATERIAL_BATCH_NUM???
                    throw new MtException("HME_EO_JOB_SN_113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_113", "HME"));
                } else if(hmeEoJobSnSingleBasic.getLotCode().length() != 10){
                    //?????????????????????10???,?????????????????????????????????????????????????????????HME_MATERIAL_BATCH_NUM???
                    throw new MtException("HME_EO_JOB_SN_114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_114", "HME"));
                }
            }

            //???????????????????????????MES_RK06 ????????????????????????
            HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
            if ("MES_RK06".equals(wo.getWorkOrderType())) {
                //????????????????????????????????????????????????SAP??????
                HmeServiceSplitRecordVO2 serviceSplitRecordVO2 = hmeServiceSplitRecordMapper.queryOrderNumBySnNumAndWoId(tenantId, wo.getWorkOrderId(), dto.getSnNum());
                if (Objects.isNull(serviceSplitRecordVO2)) {
                    //??????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_084", "HME"));
                }

                //???????????????????????????????????????????????????????????????SAP??????
                HmeServiceSplitRecordVO2 orderNum = new HmeServiceSplitRecordVO2();
                if (StringUtils.isNotBlank(serviceSplitRecordVO2.getInternalOrderNum())) {
                    orderNum.setOrderNum(serviceSplitRecordVO2.getInternalOrderNum());
                    orderNum.setOrderNumType("INTERNAL_ORDER");
                } else if (StringUtils.isNotBlank(serviceSplitRecordVO2.getSapOrderNum())) {
                    orderNum.setOrderNum(serviceSplitRecordVO2.getSapOrderNum());
                    orderNum.setOrderNumType("SAP_ORDER");
                } else {
                    //???????????????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_085", "HME"));
                }
                hmeEoJobSnSingleBasic.setRk06InternalOrderNum(orderNum);
            }
        }

        // ??????????????????
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        hmeEoJobSnSingleBasic.setUserId(userId);
        hmeEoJobSnSingleBasic.setCurrentDate(new Date());

        //V20210312 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????
        List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnCommonService.queryNgDataRecord(tenantId,dto.getWorkcellId(),Collections.singletonList(dto.getJobId()));
        if(CollectionUtils.isNotEmpty(ngDataRecordList)) {
            hmeEoJobSnSingleBasic.setNgDataRecordList(ngDataRecordList);
        }

        Map<String,HmeEoJobDataRecordVO2> hmeEoJobDataRecordVO2Map = hmeEoJobDataRecordRepository.queryNcRecord(tenantId,Collections.singletonList(dto.getJobId()));
        hmeEoJobSnSingleBasic.setNcJobDataRecordMap(hmeEoJobDataRecordVO2Map);

        return hmeEoJobSnSingleBasic;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/21 14:09
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
        if (StringUtils.isBlank(dto.getOutSiteAction())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "????????????"));
        }
        if (!HmeConstants.OutSiteAction.REWORK.equals(dto.getOutSiteAction())
                && !HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction()) && !HmeConstants.OutSiteAction.REWORK_COMPLETE.equals(dto.getOutSiteAction())) {
            throw new MtException("HME_EO_JOB_SN_149", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_149", "HME"));
        }
    }
}