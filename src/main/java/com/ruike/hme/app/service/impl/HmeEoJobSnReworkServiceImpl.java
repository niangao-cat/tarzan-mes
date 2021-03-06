package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.*;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.HmeEoJobSnUtils;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
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
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.MtMaterialSiteVO3;
import tarzan.material.domain.vo.MtMaterialSiteVO4;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterDoneStepRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;

/**
 * ???????????????-SN??????????????????????????????
 *
 * @author yuchao.wang@hand-china.com 2020-11-03 00:04:39
 */
@Slf4j
@Service
public class HmeEoJobSnReworkServiceImpl implements HmeEoJobSnReworkService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEoJobContainerRepository hmeEoJobContainerRepository;

    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private HmeEoJobSnBatchService batchSnService;

    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;

    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;

    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;

    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;

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
    private ProfileClient profileClient;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;

    @Autowired
    private HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper;

    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;

    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private HmeEoJobSnReWorkMapper hmeEoJobSnReWorkMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private HmeEoJobSnBatchMapper hmeEoJobSnBatchMapper;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private HmeEoJobSnBatchValidateService hmeEoJobSnBatchValidateService;

    @Autowired
    private MtExtendSettingsRepository extendSettingsRepository;

    @Autowired
    private HmeServiceReceiveMapper hmeServiceReceiveMapper;

    @Autowired
    private HmeServiceReceiveHisRepository hmeServiceReceiveHisRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeEoRelRepository hmeEoRelRepository;

    @Autowired
    private HmeWkcEoRelRepository hmeWkcEoRelRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private HmeTagCheckRepository hmeTagCheckRepository;

    @Autowired
    private HmePumpModPositionLineRepository hmePumpModPositionLineRepository;

    @Autowired
    private HmePumpModPositionHeaderRepository hmePumpModPositionHeaderRepository;

    @Autowired
    private HmePumpModPositionHeaderMapper hmePumpModPositionHeaderMapper;

    @Autowired
    private HmePumpModPositionLineMapper hmePumpModPositionLineMapper;

    @Autowired
    private HmeEoJobPumpCombRepository hmeEoJobPumpCombRepository;

    @Autowired
    private HmeEoJobPumpCombMapper hmeEoJobPumpCombMapper;

    @Autowired
    private HmeRepairLimitCountRepository repairLimitCountRepository;

    @Autowired
    private HmeRepairRecordRepository repairRecordRepository;

    @Autowired
    private HmeRepairRecordMapper repairRecordMapper;

    @Autowired
    private HmeMaterialTransferRepository materialTransferRepository;

    @Autowired
    private HmeEoJobSnSingleInService hmeEoJobSnSingleInService;

    @Autowired
    private HmeEoJobSnSingleService hmeEoJobSnSingleService;

    @Autowired
    private HmeProcessNcHeaderRepository hmeProcessNcHeaderRepository;

    private static final String SYMBOL = "#";

    private static String fetchGroupKey2(String str1, String str2) {
        return str1 + SYMBOL + str2;
    }

    private static String fetchGroupKey3(String str1, String str2, String str3) {
        return str1 + SYMBOL + str2 + SYMBOL + str3;
    }

    // ??????????????????
    CustomUserDetails curUser = DetailsHelper.getUserDetails();
    Long userId = curUser == null ? -1L : curUser.getUserId();

    private HmeEoJobSnSingleVO inSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto) {
        Set<String> repairTypes = lovAdapter.queryLovValue("HME.REPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> unrepairedTypes = lovAdapter.queryLovValue("HME.UNREPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        // ????????????????????????
        if (StringUtils.isBlank(dto.getSnNum())) {
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (CollectionUtils.isEmpty(dto.getOperationIdList())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????ID"));
        } else if (dto.getOperationIdList().size() > 1) {
            //??????????????????????????????,?????????
            throw new MtException("HME_EO_JOB_SN_150", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_150", "HME"));
        }
        dto.setOperationId(dto.getOperationIdList().get(0));

        HmeEoJobSnSingleVO resultVO = new HmeEoJobSnSingleVO();
        resultVO.setIsContainer(false);
        resultVO.setIsQuery(HmeConstants.ConstantValue.NO);
        resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.YES);

        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getSnNum());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        if (Objects.isNull(mtMaterialLot)) {
            //??????????????????,?????????
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        if (!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())) {
            //??????????????????, ?????????
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        //V20210705 modify by penglin.sui for peng.zhao ??????????????????????????????SN????????????
        if(StringUtils.isNotBlank(mtMaterialLot.getStocktakeFlag()) &&
                HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
            //???SN???${1}???????????????,????????????
            throw new MtException("HME_EO_JOB_SN_205", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_205", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 20210721 modify by sanfeng.zhang for xenxin.zhang ???????????????????????????Y???SN????????????
        if (HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())) {
            //??????SN???${1}???????????????,???????????????
            throw new MtException("HME_EO_JOB_SN_208", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_208", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        dto.setMaterialLotId(mtMaterialLot.getMaterialLotId());

        // 20210908 add by sanfeng.zhang for hui.gu ??????SN???????????????????????????
        hmeEoJobSnCommonService.interceptValidate(tenantId, dto.getWorkcellId(), Collections.singletonList(mtMaterialLot));

        // 20210813 modify by sanfeng.zhang for tianyang.xie ????????????????????????????????????  ?????????????????????eo
        // 20210813 modify by sanfeng.zhang for tianyang.xie ??????????????????????????? ????????? ?????????????????????EO
//        //????????????????????????????????????????????????EO
//        int reworkEoCount = hmeEoJobSnMapper.queryReworkEoCount(tenantId, dto.getSnNum());
//        if (reworkEoCount > 1) {
//            //??????????????????????????????????????????,?????????!
//            throw new MtException("HME_EO_JOB_SN_179", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EO_JOB_SN_179", "HME"));
//        }
        // ?????????????????????????????? ?????? ???????????? ?????? ?????????
        // 20210914 modify by sanfeng.zhang for wenxin.zhang ????????????????????????????????? ?????????eo?????? ????????????????????? ?????? ????????????????????????
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        HmeEoJobSnVO5 snVO = null;
        if (!newMaterialLotFlag) {
            //????????????????????????
            snVO = hmeEoJobSnMapper.queryMaterialLotInfoForRework2(tenantId, dto.getMaterialLotId(), dto.getSiteId());
            if (Objects.isNull(snVO)) {
                //?????????????????????????????????????????????,?????????!
                throw new MtException("HME_NC_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0009", "HME"));
            }
            // ????????????eo?????????
            List<HmeEoJobSnVO5> lastEoVOList = hmeEoJobSnMapper.queryLastEoByReworkMaterialLot(tenantId, snVO.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(lastEoVOList)) {
                // 20211125 add by sanfeng.zhang for wenxin.zhang ???????????????????????????????????????
                if (lastEoVOList.size() > 1) {
                    throw new MtException("HME_EO_JOB_REWORK_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_REWORK_0003", "HME"));
                }
                HmeEoJobSnVO5 lastEoVO = lastEoVOList.get(0);
                snVO.setProdLineCode(lastEoVO.getProdLineCode());
                snVO.setSiteId(lastEoVO.getSiteId());
                snVO.setWorkOrderId(lastEoVO.getWorkOrderId());
                snVO.setWorkOrderNum(lastEoVO.getWorkOrderNum());
                snVO.setWoQty(lastEoVO.getWoQty());
                snVO.setLocatorId(lastEoVO.getLocatorId());
                snVO.setWorkOrderType(lastEoVO.getWorkOrderType());
                snVO.setWorkOrderStatus(lastEoVO.getWorkOrderStatus());
                snVO.setWoProductionVersion(lastEoVO.getWoProductionVersion());
                snVO.setCompletedQty(lastEoVO.getCompletedQty());
                snVO.setRemark(lastEoVO.getRemark());
                snVO.setEoId(lastEoVO.getEoId());
                snVO.setEoQty(lastEoVO.getEoQty());
                snVO.setWorkOrderStatus(lastEoVO.getWorkOrderStatus());
                snVO.setStatus(lastEoVO.getStatus());
                snVO.setLastEoStatus(lastEoVO.getLastEoStatus());
            }
        } else {
            snVO = hmeEoJobSnMapper.queryMaterialLotInfoForRework3(tenantId, dto.getMaterialLotId(), dto.getSiteId());
            // 20210914 add by ???????????? ??????????????? ??????????????????????????? ???????????????????????????
            List<MtExtendAttrVO> attrVOList = hmeEoJobSnCommonService.queryOldCodeAttrList(tenantId, snVO.getEoId());
            Optional<MtExtendAttrVO> oldBarcodeInflagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("OLD_BARCODE_IN_FLAG", attr.getAttrName())).findFirst();
            if (oldBarcodeInflagOpt.isPresent() && YES.equals(oldBarcodeInflagOpt.get().getAttrValue())) {
                // ?????????????????????,???????????????????????????
                throw new MtException("HME_EO_JOB_SN_241", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_241", "HME"));
            }
            // ?????????????????????????????????????????????????????????EO
            String eoId = snVO.getEoId();
            List<MtExtendAttrVO> reworkMaterialLotList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(eoId);
                setAttrName("REWORK_MATERIAL_LOT");
                setTableName("mt_eo_attr");
            }});
            if (hmeEoJobSnCommonService.isBindMoreWorkingEo(tenantId, reworkMaterialLotList.get(0).getAttrValue())) {
                throw new MtException("HME_EO_JOB_REWORK_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_REWORK_0003", "HME"));
            }
            // 20210914 modiy by sanfeng.zhang for wenxin.zhang ????????? ??????????????????????????????
            Optional<MtExtendAttrVO> reworkFlagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("REWORK_FLAG", attr.getAttrName())).findFirst();
            String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getAttrValue() : "";
            snVO.setReworkFlag(reworkFlag);
        }

        if (StringUtils.isBlank(snVO.getEoId()) || !repairTypes.contains(snVO.getWorkOrderType())
                || (!HmeConstants.StatusCode.RELEASED.equals(snVO.getWorkOrderStatus()) && !HmeConstants.StatusCode.EORELEASED.equals(snVO.getWorkOrderStatus()))) {
            //??????????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_148", "HME"));
        }

        //??????????????????
        // 20210914 modiy by sanfeng.zhang for wenxin.zhang ????????? ?????????????????????????????????
        boolean isRework = HmeConstants.ConstantValue.YES.equals(snVO.getReworkFlag());
        if (!unrepairedTypes.contains(snVO.getWorkOrderType()) && !isRework) {
            //????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_161", "HME"));
        }

        //??????????????????
        HmeNcRecordVO hmeNcRecordVO = hmeEoJobSnMapper.selectLastestNcRecordProcess(tenantId, dto.getMaterialLotId(), dto.getWorkcellId());
        if (Objects.nonNull(hmeNcRecordVO)) {
            resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
            resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
        }

        // 20211102 add by sanfeng.zhang for peng.zhao ????????????????????????
        String operationDeviceFlag = hmeEoJobSnMapper.queryOperationDeviceFlag(tenantId,  dto.getOperationId());
        snVO.setTestFlag(operationDeviceFlag);

        // 20211102 add by sanfeng.zhang for peng.zhao ???????????????????????? ???????????? ?????? ???????????????????????? ?????????
        snVO.setIsShowCrossRetestBtn(HmeConstants.ConstantValue.NO);
        if (HmeConstants.ConstantValue.YES.equals(operationDeviceFlag) &&
                HmeConstants.ConstantValue.YES.equals(snVO.getReworkFlag()) &&
                "".equals(snVO.getCrossRetestFlag())) {
            snVO.setIsShowCrossRetestBtn(HmeConstants.ConstantValue.YES);
        }

        //??????????????????
        String eoId = snVO.getEoId();
        HmeRouterStepVO5 currentStep = hmeEoJobSnCommonService.queryCurrentAndNextStepByEoAndOperation(tenantId, eoId, dto.getOperationId());
        resultVO.setEntryStepFlag(currentStep.getEntryStepFlag());

        //????????????????????????
        resultVO.setDoneStepFlag(mtRouterDoneStepRepository.doneStepValidate(tenantId, currentStep.getRouterStepId()));

        //?????????????????????????????????????????????
        if (!HmeConstants.ConstantValue.YES.equals(resultVO.getDoneStepFlag()) && !HmeConstants.ConstantValue.YES.equals(resultVO.getEntryStepFlag())) {
            resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.NO);
        }

        //??????????????????
        List<HmeEoJobSnVO23> hmeEoJobSns = hmeEoJobSnMapper.selectEoJobSnForInSite(tenantId, dto.getOperationId(), eoId, isRework);

        //???????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(hmeEoJobSns)) {
            long count = 0;
            //?????????????????????????????????????????????
            count = hmeEoJobSns.stream().filter(item -> !dto.getOperationId().equals(item.getOperationId())).count();
            if (count > 0) {
                List<HmeEoJobSnVO23> exitEoJobSn = hmeEoJobSns.stream()
                        .filter(item -> !dto.getOperationId().equals(item.getOperationId())).collect(Collectors.toList());
                MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId, exitEoJobSn.get(0).getEoStepId());
                //??????SN??????${1}??????,?????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_151", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_151", "HME", routerStep.getStepName()));
            }

            //????????????????????????
            count = hmeEoJobSns.stream().filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).count();
            if (count > 0) {
                List<HmeEoJobSnVO23> exitEoJobSn = hmeEoJobSns.stream()
                        .filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, exitEoJobSn.get(0).getWorkcellId());
                //??????SN??????${1}??????,?????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_144", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_144", "HME", mtModWorkcell.getWorkcellName()));
            }

            //????????????????????????
            count = hmeEoJobSns.stream().filter(item -> !dto.getJobType().equals(item.getJobType())).count();
            if (count > 0) {
                String jobTypeDesc = "";
                List<HmeEoJobSnVO23> exitEoJobSn = hmeEoJobSns.stream()
                        .filter(item -> !dto.getJobType().equals(item.getJobType())).collect(Collectors.toList());
                switch (exitEoJobSn.get(0).getJobType()) {
                    case HmeConstants.JobType.BATCH_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.BATCH_PROCESS;
                        break;
                    case HmeConstants.JobType.TIME_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.TIME_PROCESS;
                        break;
                    case HmeConstants.JobType.PREPARE_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.PREPARE_PROCESS;
                        break;
                    case HmeConstants.JobType.COS_COMPLETED:
                        jobTypeDesc = HmeConstants.JobTypeDesc.COS_COMPLETED;
                        break;
                    case HmeConstants.JobType.PACKAGE_PROCESS_PDA:
                        jobTypeDesc = HmeConstants.JobTypeDesc.PACKAGE_PROCESS_PDA;
                        break;
                    case HmeConstants.JobType.REPAIR_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.REPAIR_PROCESS;
                        break;
                    default:
                        break;
                }

                //??????SN??????${1}??????,?????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_143", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_143", "HME", jobTypeDesc));
            }

            //SN??????????????????????????????
            resultVO.setSnVO(snVO);
            resultVO.setIsQuery(HmeConstants.ConstantValue.YES);
            resultVO.setExitEoJobSn(hmeEoJobSns.get(0));
            resultVO.setIsContainer(false);
            resultVO.setIsRework(isRework);
            return resultVO;
        }

        // ??????EO??????
        if (!HmeConstants.EoStatus.WORKING.equals(snVO.getStatus())
                || (!HmeConstants.EoStatus.RELEASED.equals(snVO.getLastEoStatus())
                && !HmeConstants.EoStatus.HOLD.equals(snVO.getLastEoStatus()))) {
            //EO???????????????????????????
            throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
        }

        //??????????????????????????????????????????????????????????????????
        HmeRepairLimitCount queryRepairLimit = new HmeRepairLimitCount();
        queryRepairLimit.setTenantId(tenantId);
        queryRepairLimit.setMaterialId(mtMaterialLot.getMaterialId());
        queryRepairLimit.setWorkcellId(dto.getProcessId());
        queryRepairLimit.setEnableFlag(HmeConstants.ConstantValue.YES);
        HmeRepairLimitCount repairLimit = repairLimitCountRepository.selectOne(queryRepairLimit);
        resultVO.setRepairLimitCount(repairLimit);
        if (Objects.nonNull(repairLimit)) {
            if (Objects.isNull(repairLimit.getLimitCount()) || repairLimit.getLimitCount() <=0L ) {
                throw new MtException("HME_EO_JOB_SN_247", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_247", "HME", dto.getProcessId(), mtMaterialLot.getMaterialId() ));
            }
        }
        //??????????????????
        HmeRepairRecord queryRepairRecord = new HmeRepairRecord();
        queryRepairRecord.setTenantId(tenantId);
        queryRepairRecord.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        queryRepairRecord.setMaterialId(mtMaterialLot.getMaterialId());
        queryRepairRecord.setWorkcellId(dto.getProcessId());
        HmeRepairRecord repairRecord = repairRecordRepository.selectOne(queryRepairRecord);
        resultVO.setRepairRecord(repairRecord);
        // ?????? SN ??????????????????????????????????????????
        if (Objects.nonNull(repairRecord)) {
            // SN ???????????????????????? ????????????
            if ("STOP".equals(repairRecord.getStatus()) ){
                throw new MtException("HME_EO_JOB_SN_246", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_246", "HME"));
            }
            // SN ????????????????????? ??????????????? ??????????????????
            if ( "WAITING".equals(repairRecord.getStatus())) {
                throw new MtException("HME_EO_JOB_SN_245", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_245", "HME"));
            }
        }

        //??????EO????????????????????????????????????
        List<String> eoIdList = new ArrayList<>();
        eoIdList.add(eoId);
        List<HmeRouterStepVO> normalStepList = hmeEoJobSnMapper.batchSelectNormalStepByEoIds(tenantId, eoIdList);
        if (CollectionUtils.isEmpty(normalStepList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????????????????"));
        }

        //????????????????????????????????????
        normalStepList = normalStepList.stream()
                .sorted(Comparator.comparing(HmeRouterStepVO::getLastUpdateDate, Comparator.reverseOrder())
                        .thenComparing(HmeRouterStepVO::getCreationDate, Comparator.reverseOrder())).collect(Collectors.toList());

        //????????????????????????????????????
        Map<String, HmeRouterStepVO> nearStepMap = hmeEoJobSnCommonService.batchQueryRouterStepByEoIds(tenantId, HmeConstants.StepType.NEAR_STEP, eoIdList);

        //??????????????????
        dto.setEoId(eoId);
        HmeEoJobSnVO3 snLine = new HmeEoJobSnVO3();
        BeanUtils.copyProperties(dto, snLine);
        snLine.setEoStepId(currentStep.getRouterStepId());
        snLine.setOperationId(currentStep.getOperationId());
        snLine.setReworkFlag(HmeConstants.ConstantValue.YES);
        resultVO.setSnLine(snLine);
        resultVO.setSnVO(snVO);
        resultVO.setIsContainer(false);
        resultVO.setNearStep(nearStepMap.getOrDefault(eoId, new HmeRouterStepVO()));
        resultVO.setCurrentStep(currentStep);
        resultVO.setNormalStepList(normalStepList);
        resultVO.setIsRework(true);
        return resultVO;
    }

    /**
     *
     * @Description ????????????-??????????????????-??????
     *
     * @author jianmin.hong
     * @date 2020/11/23 16:32
     * @param tenantId ??????ID
     * @param dto ??????
     * @param singleVO ??????
     * @return HmeEoJobSnVO
     *
     */
    private HmeEoJobSnVO reworkInSiteScan(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO singleVO) {
        //????????????
        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        HmeEoJobSnVO3 snLine = singleVO.getSnLine();
        HmeRouterStepVO normalNearStep = singleVO.getNormalStepList().get(0);
        HmeRouterStepVO nearStepVO = singleVO.getNearStep();
        HmeEoJobSnVO5 snVO = singleVO.getSnVO();

        snLine.setSiteId(snVO.getSiteId());
        snLine.setWorkOrderId(snVO.getWorkOrderId());
        snLine.setMaterialId(snVO.getMaterialId());
        snLine.setEoStepNum(HmeConstants.ConstantValue.ONE);

        //????????????????????????
        MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
        eoRouterActual.setRouterStepId(snLine.getEoStepId());
        eoRouterActual.setEoId(snLine.getEoId());
        eoRouterActual.setQty(snVO.getEoQty().doubleValue());
        eoRouterActual.setPreviousStepId(nearStepVO.getEoStepActualId());
        eoRouterActual.setWorkcellId(nearStepVO.getWorkcellId());
        eoRouterActual.setReworkStepFlag(HmeConstants.ConstantValue.YES);
        eoRouterActual.setSourceEoStepActualId(normalNearStep.getEoStepActualId());
        eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
        mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);

        //??????????????????
        eoWorking(tenantId, snVO.getEoQty(), snLine);

        //????????????
        if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
            snLine.setSourceContainerId(snVO.getCurrentContainerId());
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(snVO.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(snVO.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }

        //????????????
        HmeEoJobSnVO2 currentJobSnVO = batchSnService.createSnJob(tenantId, snLine);

        // ??????????????????
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(currentJobSnVO.getJobId());
        equSwitchParams.setWorkcellId(dto.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);

        //V20210302 modify by penglin.sui for fang.pan ????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(snVO.getAfFlag()) && HmeConstants.ConstantValue.YES.equals(singleVO.getEntryStepFlag())){
            HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryLastSplitRecordOfMaterialLot(tenantId,snVO.getMaterialLotId());
            if(Objects.nonNull(hmeServiceSplitRecord)){
                //????????????
                String currTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(CommonUtils.currentTimeGet());

                HmeServiceSplitRecord hmeServiceSplitRecordPara = new HmeServiceSplitRecord();
                hmeServiceSplitRecordPara.setSplitRecordId(hmeServiceSplitRecord.getSplitRecordId());
                hmeServiceSplitRecordPara.setSplitStatus(HmeConstants.SplitStatus.REPAIRING);
                hmeServiceSplitRecordPara.setAttribute1(currTime);
                hmeServiceSplitRecordMapper.updateByPrimaryKeySelective(hmeServiceSplitRecordPara);
                if(StringUtils.isNotBlank(hmeServiceSplitRecord.getServiceReceiveId()) && StringUtils.equals(hmeServiceSplitRecord.getTopSplitRecordId(), hmeServiceSplitRecord.getSplitRecordId())){

                    HmeServiceReceive hmeServiceReceivePara = new HmeServiceReceive();
                    hmeServiceReceivePara.setServiceReceiveId(hmeServiceSplitRecord.getServiceReceiveId());
                    hmeServiceReceivePara.setReceiveStatus(HmeConstants.ReceiveStatus.REPAIRING);
                    hmeServiceReceivePara.setAttribute1(currTime);
                    hmeServiceReceiveMapper.updateByPrimaryKeySelective(hmeServiceReceivePara);

                    //????????????
                    HmeServiceReceive hmeServiceReceive = hmeServiceReceiveMapper.selectByPrimaryKey(hmeServiceSplitRecord.getServiceReceiveId());

                    MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                    eventCreateVO.setEventTypeCode(HmeConstants.EventType.HME_AF_REPAIRING);
                    String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                    HmeServiceReceiveHis hmeServiceReceiveHisPara = new HmeServiceReceiveHis();
                    BeanUtils.copyProperties(hmeServiceReceive, hmeServiceReceiveHisPara);
                    hmeServiceReceiveHisPara.setEventId(eventId);
                    hmeServiceReceiveHisRepository.insertSelective(hmeServiceReceiveHisPara);
                }
            }
        }

        //?????? SN ???????????? ?????? ??????????????????
        if(Objects.isNull(singleVO.getRepairRecord())) {
            //????????????
            HmeRepairRecord repairRecord = new HmeRepairRecord();
            repairRecord.setTenantId(tenantId);
            repairRecord.setMaterialLotId(snLine.getMaterialLotId());
            repairRecord.setMaterialId(snLine.getMaterialId());
            repairRecord.setWorkcellId(dto.getProcessId());
            repairRecord.setRepairCount(1L);
            repairRecord.setStatus("UNDEFINED");
            HmeRepairLimitCount repairLimit = singleVO.getRepairLimitCount();
            if (!Objects.isNull(repairLimit)) {
                //????????????
                repairRecord.setLimitCount(repairLimit.getLimitCount());
                // ???????????? ???????????? 0
                repairRecord.setPassCount(0L);
                repairRecord.setPermitCount(repairLimit.getLimitCount());
                //???????????????????????? ???????????? ??????????????????
                repairRecord.setPermitRepairCount(repairLimit.getLimitCount());
                if (repairRecord.getRepairCount() >= repairLimit.getLimitCount()) {
                    repairRecord.setStatus("WAITING");
                }
            }
            repairRecordRepository.insertSelective(repairRecord);
        } else {
            //??????
            HmeRepairRecord repairRecord = singleVO.getRepairRecord();
            HmeRepairLimitCount repairLimit = singleVO.getRepairLimitCount();
            Long repairCount = 1L;
            if (!Objects.isNull(repairRecord.getRepairCount())) {
                repairCount = repairRecord.getRepairCount()+1;
            }
            repairRecord.setRepairCount(repairCount);
            // ???????????????????????????????????? ????????????????????? ?????????????????????
            if (!Objects.isNull(repairRecord.getPermitRepairCount()) && !Objects.isNull(repairLimit)) {
                if (repairRecord.getPermitRepairCount() <= repairCount) {
                    repairRecord.setStatus("WAITING");
                }
            }
            repairRecordMapper.updateByPrimaryKeySelective(repairRecord);
        }

        // ??????????????????????????????
        HmeEoJobMaterialVO jobCondition = new HmeEoJobMaterialVO();
        jobCondition.setWorkcellId(dto.getWorkcellId());
        jobCondition.setJobId(currentJobSnVO.getJobId());
        jobCondition.setEoId(currentJobSnVO.getEoId());
        jobCondition.setOperationId(dto.getOperationId());
        jobCondition.setJobType(dto.getJobType());
        jobCondition.setEoStepId(currentJobSnVO.getEoStepId());
        jobCondition.setSiteId(dto.getSiteId());
        jobCondition.setReworkFlag(HmeConstants.ConstantValue.YES);
        List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId, jobCondition, null);
        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialVOList)) {
            resultVO.setLotMaterialVOList(hmeEoJobLotMaterialVOList);
        }

        // ??????????????????????????????
        List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOListAll = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, jobCondition, null);
        if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialVOListAll)) {
            resultVO.setTimeMaterialVOList(hmeEoJobTimeMaterialVOListAll);
        }

        // ??????????????????????????????
        HmeEoJobMaterialVO2 jobMaterialCondition = new HmeEoJobMaterialVO2();
        jobMaterialCondition.setJobId(currentJobSnVO.getJobId());
        jobMaterialCondition.setWorkcellId(dto.getWorkcellId());
        jobMaterialCondition.setSiteId(dto.getSiteId());
        jobMaterialCondition.setJobType(dto.getJobType());
        jobMaterialCondition.setEoId(currentJobSnVO.getEoId());
        jobMaterialCondition.setOperationId(dto.getOperationId());
        List<HmeEoJobMaterialVO> hmeEoJobMaterialVOAllList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId, jobMaterialCondition);
        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialVOAllList)) {
            resultVO.setMaterialVOList(hmeEoJobMaterialVOAllList);
        }

        // ?????????????????????????????????????????????
        HmeRouterStepVO5 currentStep = singleVO.getCurrentStep();
        if (Objects.nonNull(currentStep)) {
            resultVO.setDoneStepFlag(singleVO.getDoneStepFlag());
            resultVO.setCurrentStepName(currentStep.getStepName());
            resultVO.setCurrentStepDescription(currentStep.getDescription());
            resultVO.setCurrentStepSequence(currentStep.getSequence());
            resultVO.setNextStepName(currentStep.getNextStepName());
            resultVO.setNextStepDescription(currentStep.getNextDescription());
        }

        //???????????????
        if (CollectionUtils.isNotEmpty(currentJobSnVO.getDataRecordVOList())) {
            resultVO.setDataRecordVOList(currentJobSnVO.getDataRecordVOList());
        }

        // ??????
        if (currentJobSnVO.getShiftId() != null) {
            MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, currentJobSnVO.getShiftId());
            resultVO.setShiftId(currentJobSnVO.getShiftId());
            resultVO.setShiftCode(mtWkcShift.getShiftCode());
        }

        //????????????
        if (StringUtils.isBlank(dto.getWorkcellCode())) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        } else {
            resultVO.setWorkcellCode(dto.getWorkcellCode());
            resultVO.setWorkcellName(dto.getWorkcellName());
        }

        // ???????????????
        resultVO.setOperationId(currentJobSnVO.getOperationId());
        resultVO.setEoStepId(currentJobSnVO.getEoStepId());
        resultVO.setEoStepNum(currentJobSnVO.getEoStepNum());
        resultVO.setJobId(currentJobSnVO.getJobId());
        resultVO.setEoId(currentJobSnVO.getEoId());
        resultVO.setSiteInBy(currentJobSnVO.getSiteInBy());
        // ?????????
        String userRealName = userClient.userInfoGet(tenantId, currentJobSnVO.getSiteInBy()).getRealName();
        resultVO.setSiteInByName(userRealName);
        resultVO.setSiteInDate(currentJobSnVO.getSiteInDate());
        resultVO.setSiteOutDate(currentJobSnVO.getSiteOutDate());
        resultVO.setSourceContainerId(currentJobSnVO.getSourceContainerId());

        //V20201208 modify by penglin.sui ??????BOM??????
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(currentJobSnVO.getEoId());
        List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(hmeEoBomVOList)) {
            resultVO.setBomId(hmeEoBomVOList.get(0).getBomId());
            resultVO.setBomName(hmeEoBomVOList.get(0).getBomName());
        }

        //????????????????????????????????? HME.WORKCELL_ABNORMAL_OUT_SITE_FLAG ?????? ????????????????????????????????????
        List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.WORKCELL_ABNORMAL_OUT_SITE_FLAG", tenantId);
        Optional<LovValueDTO> firstOpt = lovValue.stream().filter(lov->StringUtils.equals(dto.getWorkcellCode(), lov.getValue())).findFirst();
        if (firstOpt.isPresent()) {
            resultVO.setIsShowAbnormalOutBtn(HmeConstants.ConstantValue.YES);
        } else {
            resultVO.setIsShowAbnormalOutBtn(HmeConstants.ConstantValue.NO);
        }

        // ??????????????????
        resultVO.setWorkcellId(dto.getWorkcellId());
        resultVO.setMaterialLotId(dto.getMaterialLotId());
        resultVO.setWorkOrderId(snVO.getWorkOrderId());
        resultVO.setWorkOrderNum(snVO.getWorkOrderNum());
        resultVO.setSnMaterialId(snVO.getMaterialId());
        resultVO.setSnMaterialCode(snVO.getMaterialCode());
        resultVO.setSnMaterialName(snVO.getMaterialName());
        resultVO.setWoQuantity(snVO.getWoQty());
        resultVO.setWoQuantityOut(snVO.getCompletedQty());
        resultVO.setQualityStatus(snVO.getQualityStatus());
        resultVO.setQualityStatusMeaning(snVO.getQualityStatusMeaning());
        resultVO.setRemark(snVO.getRemark());
        resultVO.setProductionVersion(snVO.getWoProductionVersion());
        resultVO.setIsContainer(singleVO.getIsContainer());
        resultVO.setReworkFlag(HmeConstants.ConstantValue.YES);
        resultVO.setIsClickProcessComplete(singleVO.getIsClickProcessComplete());
        resultVO.setSnNum(dto.getSnNum());
        resultVO.setEntryStepFlag(singleVO.getEntryStepFlag());
        resultVO.setTestFlag(snVO.getTestFlag());
        resultVO.setIsShowCrossRetestBtn(snVO.getIsShowCrossRetestBtn());
        return resultVO;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/17 10:53
     * @return void
     *
     */
    private void eoWorking(Long tenantId, BigDecimal eoQty, HmeEoJobSnVO3 snLine) {
        //??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_IN");

        // ??????????????????????????????
        MtEoRouterActualVO2 routerActualParam = new MtEoRouterActualVO2();
        routerActualParam.setEoId(snLine.getEoId());
        List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                mtEoRouterActualMapper.eoOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList)) {
            throw new MtException("HME_EO_JOB_SN_039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
        }

        // ????????????????????????????????????
        String eoStepActualId;
        if (eoOperationLimitCurrentRouterList.size() > 1) {
            eoStepActualId = eoOperationLimitCurrentRouterList.stream()
                    .max(Comparator.comparing(MtEoRouterActualVO5::getLastUpdateDate)).get()
                    .getEoStepActualId();
        } else {
            eoStepActualId = eoOperationLimitCurrentRouterList.get(0).getEoStepActualId();
        }
        log.debug("???????????????????????????:" + eoStepActualId);

        // ????????????
        MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
        mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO4.setQueueQty(new Double(String.valueOf(eoQty)));
        mtEoStepWipVO4.setWorkcellId(snLine.getWorkcellId());
        mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);

        // ????????????
        MtEoRouterActualVO19 workingParam = new MtEoRouterActualVO19();
        workingParam.setEventRequestId(eventRequestId);
        workingParam.setEoStepActualId(eoStepActualId);
        workingParam.setWorkcellId(snLine.getWorkcellId());
        workingParam.setQty(new Double(String.valueOf(eoQty)));
        workingParam.setSourceStatus("QUEUE");
        workingParam.setLastWorkcellId(snLine.getWorkcellId());
        mtEoRouterActualRepository.eoWkcAndStepWorking(tenantId, workingParam);
    }

    /**
     *
     * @Description ????????????-??????????????????
     *
     * @author jianmin.hong
     * @date 2020/11/23 16:32
     * @param tenantId ??????ID
     * @param dto ??????
     * @return HmeEoJobSnVO
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        //??????
        HmeEoJobSnSingleVO hmeEoJobSnSingleVO = inSiteScanValidate(tenantId, dto);
        String showTagModelFlag = hmeTagCheckRepository.getShowTagModelFlag(tenantId, dto.getWorkcellId());
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleVO.getIsQuery())) {
            HmeEoJobSnVO hmeEoJobSnVO = inSiteQuery(tenantId, dto, hmeEoJobSnSingleVO);
            hmeEoJobSnVO.setShowTagModelFlag(showTagModelFlag);
            return hmeEoJobSnVO;
        }

        //??????????????????
        HmeEoJobSnVO result = reworkInSiteScan(tenantId, dto, hmeEoJobSnSingleVO);

        //??????????????????????????????????????????
        int inSiteCount = hmeEoJobSnMapper.queryEoJobSnCountByEoId(tenantId, result.getEoId());
        if (inSiteCount == 1) {
            // 20210812 add by sanfeng.zhang for tianyang.xie ??????????????? ????????????
            String eventRequestId = this.updateMaterialLot(tenantId, dto.getSnNum());
            // 20210322 add by sanfeng.zhang for fang.pan ??????eo????????????
            this.createEoRel(tenantId, dto, result.getEoId());
            releaseSelfForFirstInSite(tenantId, dto, hmeEoJobSnSingleVO, result, eventRequestId);
            // ????????????????????????????????? ?????????????????????eo??????
            this.updatePumpPositionHeader(tenantId, result.getEoId(), dto.getSnNum());
        }
        // ????????????????????????
        hmeEoJobSnSingleInService.calculationReflectorRecord(tenantId, dto, result);

        // ?????????????????? ????????????SN??????????????????
        this.validateMfFlag(tenantId, dto);
        result.setShowTagModelFlag(showTagModelFlag);
        return result;
    }

    private void validateMfFlag (Long tenantId,  HmeEoJobSnVO3 dto) {
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setKeyId(dto.getMaterialLotId());
            setAttrName("MF_FLAG");
            setTableName("mt_material_lot_attr");
        }});
        String mfFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
        if (!YES.equals(mfFlag)) {
            // ??????[${1}]???????????????,???????????????!
            throw new MtException("HME_EO_JOB_SN_253", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_253", "HME", dto.getSnNum()));
        }
    }

    private void updatePumpPositionHeader(Long tenantId, String eoId, String snNum) {
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, snNum);
        if (newMaterialLotFlag) {
            List<MtExtendSettings> attrList = new ArrayList<>();
            MtExtendSettings extendSetting = new MtExtendSettings();
            extendSetting.setAttrName("REWORK_MATERIAL_LOT");
            attrList.add(extendSetting);
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId, "mt_eo_attr", "eo_id", eoId, attrList);
            String reworkMaterialLotCode = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).select(MtEo.FIELD_EO_ID, MtEo.FIELD_WORK_ORDER_ID).andWhere(Sqls.custom()
                    .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtEo.FIELD_IDENTIFICATION, reworkMaterialLotCode)).build());
            if (CollectionUtils.isNotEmpty(mtEoList)) {
                String oldEoId = mtEoList.get(0).getEoId();
                // ???????????????eo ?????????????????????????????????
                List<HmePumpModPositionHeader> positionHeaders = hmePumpModPositionHeaderRepository.selectByCondition(Condition.builder(HmePumpModPositionHeader.class).andWhere(Sqls.custom()
                        .andEqualTo(HmePumpModPositionHeader.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmePumpModPositionHeader.FIELD_EO_ID, oldEoId)).build());
                String finalEoId = eoId;
                positionHeaders.forEach(ph -> {
                    ph.setEoId(finalEoId);
                });
                Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
                if (CollectionUtils.isNotEmpty(positionHeaders)) {
                    hmePumpModPositionHeaderMapper.myBatchUpdateEo(tenantId, userId, positionHeaders);
                }
            }
        }
    }

    private String updateMaterialLot(Long tenantId, String snNum) {
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "HOME_MADE_PRODUCTION_REWORK_START");
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventRequestId(eventRequestId);
            setEventTypeCode("HOME_MADE_PRODUCTION_REWORK_START");
        }});
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(snNum);
        }});
        if (mtMaterialLot != null) {
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2(){{
                setEventId(eventId);
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
                setLastUpdateDate(CommonUtils.currentTimeGet());
            }}, "N");
        }
        return eventRequestId;
    }

    /**
     *
     * @Description ??????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/5 20:29
     * @param tenantId ??????ID
     * @param dto ??????
     * @param singleVO ??????
     * @param result ??????
     * @return void
     *
     */
    private void releaseSelfForFirstInSite(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO singleVO, HmeEoJobSnVO result, String barcodeEventRequestId) {
        HmeEoJobSnVO5 snVO = singleVO.getSnVO();
        HmeEoJobSnVO3 snLine = singleVO.getSnLine();

        // 20210915 add by  sanfeng.zhang for wenxin.zhang ????????? ????????????????????????
        // 20210815 add by  sanfeng.zhang for tianyang.xie ????????? ??????????????? ????????????
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        String materialId = snVO.getMaterialId();
        String materialLotId = dto.getMaterialLotId();
        if (newMaterialLotFlag) {
            // ture???????????????
            String reworkMaterialLotCode = hmeEoJobSnReWorkMapper.queryReworkMaterialLotCode(tenantId, dto.getMaterialLotId());
            List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, Collections.singletonList(reworkMaterialLotCode));
            if (CollectionUtils.isEmpty(materialLotList)) {
                throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_004", "HME", reworkMaterialLotCode));
            }
            // ???????????????????????? ??????????????????????????? ???????????????
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_material_lot_attr");
                setAttrName("MF_FLAG");
                setKeyId(materialLotList.get(0).getMaterialLotId());
            }});
            String mfFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            if (YES.equals(mfFlag)) {
                throw new MtException("HME_EO_JOB_SN_249", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_249", "HME", materialLotList.get(0).getMaterialLotCode()));
            }
            materialLotId = materialLotList.get(0).getMaterialLotId();
            materialId = materialLotList.get(0).getMaterialId();
        }
        //??????EO?????????????????????????????????????????????
        List<HmeBomComponentVO3> eoComponentList = hmeEoJobSnMapper.queryEoComponentByEoIdAndMaterialId(tenantId, snVO.getEoId(), materialId, snLine.getEoStepId());
        if (CollectionUtils.isEmpty(eoComponentList) || eoComponentList.size() != 1) {
            throw new MtException("HME_EO_JOB_SN_162", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_162", "HME"));
        }
        HmeBomComponentVO3 eoComponent = eoComponentList.get(0);

        //????????????????????????
        HmeMaterialLotVO4 materialLotInfo = hmeEoJobSnMapper.queryMaterialLotInfoForInSiteRelease(tenantId, materialLotId);
        if (Objects.isNull(eoComponent.getBomComponentQty()) || Objects.isNull(materialLotInfo.getPrimaryUomQty())
                || !eoComponent.getBomComponentQty().equals(materialLotInfo.getPrimaryUomQty())) {
            throw new MtException("HME_EO_JOB_SN_162", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_162", "HME"));
        }

        //????????????????????????
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());

        //??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        //????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        eventCreateVO.setEventRequestId(barcodeEventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        if (newMaterialLotFlag) {
            // ????????????????????????????????????  ?????????????????? ????????????0
            String startEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventRequestId(barcodeEventRequestId);
                setEventTypeCode("HOME_MADE_PRODUCTION_REWORK_START");
            }});
            String finalMaterialLotId = materialLotId;
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2(){{
                setEventId(startEventId);
                setMaterialLotId(finalMaterialLotId);
                setEnableFlag(NO);
                setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
            }}, "N");
        }
        // ????????????????????????
        List<MtExtendVO5> extendAttrList = new ArrayList<>(1);
        MtExtendVO5 inworkFlagAttr = new MtExtendVO5();
        inworkFlagAttr.setAttrName("MF_FLAG");
        inworkFlagAttr.setAttrValue(YES);
        extendAttrList.add(inworkFlagAttr);
        //V20210324 modify by penglin.sui for fang.pan ??????????????????????????????sn??????????????????Y
        MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
        reworkFlagAttr.setAttrName("REWORK_FLAG");
        reworkFlagAttr.setAttrValue(YES);
        extendAttrList.add(reworkFlagAttr);
        MtExtendVO5 statusFlagAttr = new MtExtendVO5();
        statusFlagAttr.setAttrName("STATUS");
        statusFlagAttr.setAttrValue("NEW");
        extendAttrList.add(statusFlagAttr);
        if (!newMaterialLotFlag) {
            MtExtendVO5 inFlagAttr = new MtExtendVO5();
            inFlagAttr.setAttrName("OLD_BARCODE_IN_FLAG");
            inFlagAttr.setAttrValue(YES);
            extendAttrList.add(inFlagAttr);
        }
        extendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", materialLotInfo.getMaterialLotId(), eventId, extendAttrList);

        //???????????????
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(materialLotInfo.getLocatorId());
        mtInvOnhandQuantityVO9.setMaterialId(materialLotInfo.getMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(eoComponent.getBomComponentQty());
        mtInvOnhandQuantityVO9.setEventId(eventId);
        mtInvOnhandQuantityVO9.setLotCode(materialLotInfo.getLot());
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        // ????????????????????????????????????????????????????????????????????????mes?????????????????????????????????sap
        if (StringUtils.isBlank(result.getWorkOrderId())) {
            //?????????????????????,?????????!
            throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0002", "HME"));
        }

        HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, result.getWorkOrderNum());
        String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
        // ????????????????????????????????????????????????
        if (!Objects.isNull(splitRecord)) {
            //?????????????????????????????????
            if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())) {
                transactionTypeCode = HmeConstants.TransactionTypeCode.AF_ZSD005_ISSUE;
            }
        }
        //??????????????????
        WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
        wmsTransactionTypePara.setTenantId(tenantId);
        wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);

        //??????????????????
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
        objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
        objectTransactionVO.setMaterialLotId(materialLotInfo.getMaterialLotId());
        objectTransactionVO.setLotNumber(materialLotInfo.getLot());
        objectTransactionVO.setEventId(eventId);
        objectTransactionVO.setMaterialId(materialLotInfo.getMaterialId());
        objectTransactionVO.setMaterialCode(materialLotInfo.getMaterialCode());
        objectTransactionVO.setTransactionQty(BigDecimal.valueOf(eoComponent.getBomComponentQty()));
        objectTransactionVO.setTransactionUom(materialLotInfo.getMaterialPrimaryUomCode());
        objectTransactionVO.setTransactionTime(new Date());
        objectTransactionVO.setPlantId(dto.getSiteId());
        objectTransactionVO.setLocatorId(materialLotInfo.getLocatorId());
        objectTransactionVO.setLocatorCode(materialLotInfo.getLocatorCode());
        objectTransactionVO.setWarehouseId(materialLotInfo.getWarehouseId());
        objectTransactionVO.setWarehouseCode(materialLotInfo.getWarehouseCode());
        objectTransactionVO.setWorkOrderNum(snVO.getWorkOrderNum());
        objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
        objectTransactionVO.setProdLineCode(snVO.getProdLineCode());
        String moveType = "";
        // ???????????????AF_ZSD005_ISSUE ?????????????????????
        if (StringUtils.equals(transactionTypeCode, HmeConstants.TransactionTypeCode.AF_ZSD005_ISSUE)) {
            moveType = "";
        } else {
            moveType = Objects.isNull(wmsTransactionType) ? "261" : wmsTransactionType.getMoveType();
        }
        objectTransactionVO.setMoveType(moveType);
        objectTransactionVO.setBomReserveNum(StringUtils.trimToEmpty(eoComponent.getReserveNum()));
        objectTransactionVO.setBomReserveLineNum(String.valueOf(eoComponent.getLineNumber()));
        objectTransactionVO.setSoNum(materialLotInfo.getSoNum());
        objectTransactionVO.setSoLineNum(materialLotInfo.getSoLineNum());
        objectTransactionRequestList.add(objectTransactionVO);
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

        //??????API
        MtAssembleProcessActualVO5 assembleProcessVo5 = new MtAssembleProcessActualVO5();
        assembleProcessVo5.setEoId(snVO.getEoId());
        assembleProcessVo5.setMaterialId(eoComponent.getBomComponentMaterialId());
        assembleProcessVo5.setBomComponentId(eoComponent.getBomComponentId());
        assembleProcessVo5.setRouterId(eoComponent.getRouterId());
        assembleProcessVo5.setOperationId(dto.getOperationId());
        assembleProcessVo5.setRouterStepId(snLine.getEoStepId());
        assembleProcessVo5.setTrxAssembleQty(eoComponent.getBomComponentQty());
        assembleProcessVo5.setAssembleExcessFlag(NO);
        assembleProcessVo5.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
        assembleProcessVo5.setOperationBy(result.getSiteInBy());
        assembleProcessVo5.setWorkcellId(dto.getWorkcellId());
        assembleProcessVo5.setEventRequestId(eventRequestId);
        assembleProcessVo5.setParentEventId(eventId);
        assembleProcessVo5.setMaterialLotId(materialLotInfo.getMaterialLotId());
        if (Objects.nonNull(mtWkcShift)) {
            assembleProcessVo5.setShiftCode(mtWkcShift.getShiftCode());
            if (Objects.nonNull(mtWkcShift.getShiftDate())) {
                assembleProcessVo5.setShiftDate(mtWkcShift.getShiftDate());
            }
        }
        assembleProcessVo5.setLocatorId(materialLotInfo.getLocatorId());
        mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVo5);

        //??????????????????

        //????????????????????????
        Map<String, String> materialLotExtendAttrMap = new HashMap<>();
        List<String> materialLotIdList = new ArrayList<>();
        materialLotIdList.add(materialLotInfo.getMaterialLotId());
        List<String> materialLotAttrNameList = new ArrayList<>();
        materialLotAttrNameList.add(HmeConstants.ExtendAttr.MATERIAL_VERSION);
        List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_lot_attr", "MATERIAL_LOT_ID"
                , materialLotIdList, materialLotAttrNameList);
        if (CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)) {
            materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(item -> item.getKeyId() + "-" + item.getAttrName(), MtExtendAttrVO1::getAttrValue));
        }
        // ???????????????????????? ??????eo????????????????????????????????? ??????????????????????????????
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(result.getEoId());
        if (!StringUtils.equals(mtEo.getMaterialId(), materialLotInfo.getMaterialId())) {
            String woEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("WO_MATERIAL_CHANGE");
            }});
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                setMaterialLotId(materialLotInfo.getMaterialLotId());
                setMaterialId(mtEo.getMaterialId());
                setPrimaryUomId(mtEo.getUomId());
                setEventId(woEventId);
            }}, NO);
        }

        HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
        hmeEoJobSnLotMaterial.setTenantId(tenantId);
        hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.LOT);
        hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSnLotMaterial.setMaterialId(materialLotInfo.getMaterialId());
        hmeEoJobSnLotMaterial.setMaterialLotId(materialLotInfo.getMaterialLotId());
        hmeEoJobSnLotMaterial.setReleaseQty(BigDecimal.valueOf(eoComponent.getBomComponentQty()));
        hmeEoJobSnLotMaterial.setJobId(result.getJobId());
        hmeEoJobSnLotMaterial.setLocatorId(materialLotInfo.getLocatorId());
        hmeEoJobSnLotMaterial.setLotCode(materialLotInfo.getLot());
        hmeEoJobSnLotMaterial.setProductionVersion(materialLotExtendAttrMap.getOrDefault(fetchGroupKey2(materialLotInfo.getMaterialLotId(), HmeConstants.ExtendAttr.MATERIAL_VERSION), ""));
        hmeEoJobSnLotMaterialRepository.insertSelective(hmeEoJobSnLotMaterial);
    }

    @Override
    public HmeEoJobSnVO inSiteQuery(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO hmeEoJobSnSingleVO) {
        HmeEoJobSnVO23 exitEoJobSn = hmeEoJobSnSingleVO.getExitEoJobSn();
        if (Objects.isNull(exitEoJobSn) || StringUtils.isBlank(exitEoJobSn.getJobId())) {
            //????????????${1}??????????????????????????????,?????????!
            throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_010", "HME", dto.getSnNum()));
        }

        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        BeanUtils.copyProperties(exitEoJobSn, resultVO);

        // ??????????????????????????????
        HmeEoJobMaterialVO jobCondition = new HmeEoJobMaterialVO();
        jobCondition.setWorkcellId(dto.getWorkcellId());
        jobCondition.setJobId(resultVO.getJobId());
        jobCondition.setEoId(resultVO.getEoId());
        jobCondition.setOperationId(dto.getOperationId());
        jobCondition.setJobType(dto.getJobType());
        jobCondition.setEoStepId(resultVO.getEoStepId());
        jobCondition.setSiteId(dto.getSiteId());
        jobCondition.setReworkFlag(resultVO.getReworkFlag());
        List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId, jobCondition, null);
        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialVOList)) {
            resultVO.setLotMaterialVOList(hmeEoJobLotMaterialVOList);
        }

        // ??????????????????????????????
        List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOListAll = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, jobCondition, null);
        if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialVOListAll)) {
            resultVO.setTimeMaterialVOList(hmeEoJobTimeMaterialVOListAll);
        }

        // ??????????????????????????????
        HmeEoJobMaterialVO2 jobMaterialCondition = new HmeEoJobMaterialVO2();
        jobMaterialCondition.setJobId(resultVO.getJobId());
        jobMaterialCondition.setWorkcellId(dto.getWorkcellId());
        jobMaterialCondition.setSiteId(dto.getSiteId());
        jobMaterialCondition.setJobType(dto.getJobType());
        jobMaterialCondition.setEoId(resultVO.getEoId());
        jobMaterialCondition.setOperationId(dto.getOperationId());
        List<HmeEoJobMaterialVO> hmeEoJobMaterialVOAllList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId, jobMaterialCondition);
        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialVOAllList)) {
            resultVO.setMaterialVOList(hmeEoJobMaterialVOAllList);
        }

        // ??????????????????????????????
        HmeEoJobMaterialVO2 jobDataRecordCondition = new HmeEoJobMaterialVO2();
        jobDataRecordCondition.setWorkcellId(dto.getWorkcellId());
        jobDataRecordCondition.setJobId(resultVO.getJobId());
        jobDataRecordCondition.setJobType(dto.getJobType());
        List<HmeEoJobDataRecordVO> eoJobDataRecordVOList = hmeEoJobDataRecordRepository.eoJobDataRecordQuery(tenantId, jobDataRecordCondition);
        if (CollectionUtils.isNotEmpty(eoJobDataRecordVOList)) {
            resultVO.setDataRecordVOList(eoJobDataRecordVOList);
        }

        // ?????????????????????????????????????????????
        if (StringUtils.isNotBlank(exitEoJobSn.getEoStepId())) {
            HmeRouterStepVO5 currentStep = hmeEoJobSnCommonService.queryCurrentAndNextStepByCurrentId(tenantId, exitEoJobSn.getEoStepId());
            if (Objects.nonNull(currentStep)) {
                //????????????????????????
                resultVO.setDoneStepFlag(mtRouterDoneStepRepository.doneStepValidate(tenantId, exitEoJobSn.getEoStepId()));
                resultVO.setCurrentStepName(currentStep.getStepName());
                resultVO.setCurrentStepDescription(currentStep.getDescription());
                resultVO.setCurrentStepSequence(currentStep.getSequence());
                resultVO.setNextStepName(currentStep.getNextStepName());
                resultVO.setNextStepDescription(currentStep.getNextDescription());
            }
        }

        // ??????
        if (resultVO.getSiteOutDate() != null) {
            String meterTimeStr = DateUtil.getDistanceTime(resultVO.getSiteInDate(),
                    resultVO.getSiteOutDate());
            resultVO.setMeterTimeStr(meterTimeStr);
        }

        // ?????????
        if (Objects.nonNull(resultVO.getSiteInBy())) {
            String userRealName = userClient.userInfoGet(tenantId, resultVO.getSiteInBy()).getRealName();
            resultVO.setSiteInByName(userRealName);
        }

        //????????????
        if (StringUtils.isBlank(dto.getWorkcellCode())) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        } else {
            resultVO.setWorkcellCode(dto.getWorkcellCode());
            resultVO.setWorkcellName(dto.getWorkcellName());
        }

        //???????????????????????????
        HmeEoJobSnVO jobSnInfo = hmeEoJobSnMapper.queryEoJobSnInfoForInSiteQuery(tenantId, resultVO.getJobId());
        if (Objects.nonNull(jobSnInfo)) {
            resultVO.setShiftCode(jobSnInfo.getShiftCode());
            resultVO.setProductionVersion(jobSnInfo.getProductionVersion());
        }

        //V20201208 modify by penglin.sui ??????BOM??????
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(hmeEoJobSnSingleVO.getSnVO().getEoId());
        List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(hmeEoBomVOList)) {
            resultVO.setBomId(hmeEoBomVOList.get(0).getBomId());
            resultVO.setBomName(hmeEoBomVOList.get(0).getBomName());
        }

        //????????????????????????????????? HME.WORKCELL_ABNORMAL_OUT_SITE_FLAG ?????? ????????????????????????????????????
        List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.WORKCELL_ABNORMAL_OUT_SITE_FLAG", tenantId);
        Optional<LovValueDTO> firstOpt = lovValue.stream().filter(lov->StringUtils.equals(dto.getWorkcellCode(), lov.getValue())).findFirst();
        if (firstOpt.isPresent()) {
            resultVO.setIsShowAbnormalOutBtn(HmeConstants.ConstantValue.YES);
        } else {
            resultVO.setIsShowAbnormalOutBtn(HmeConstants.ConstantValue.NO);
        }

        // ??????????????????
        HmeEoJobSnVO5 snVO = hmeEoJobSnSingleVO.getSnVO();
        resultVO.setWorkcellId(dto.getWorkcellId());
        resultVO.setMaterialLotId(dto.getMaterialLotId());
        resultVO.setSnNum(dto.getSnNum());
        resultVO.setWorkOrderId(snVO.getWorkOrderId());
        resultVO.setWorkOrderNum(snVO.getWorkOrderNum());
        resultVO.setSnMaterialId(snVO.getMaterialId());
        resultVO.setSnMaterialCode(snVO.getMaterialCode());
        resultVO.setSnMaterialName(snVO.getMaterialName());
        resultVO.setWoQuantity(snVO.getWoQty());
        resultVO.setWoQuantityOut(snVO.getCompletedQty());
        resultVO.setQualityStatus(snVO.getQualityStatus());
        resultVO.setQualityStatusMeaning(snVO.getQualityStatusMeaning());
        resultVO.setRemark(snVO.getRemark());
        resultVO.setIsContainer(hmeEoJobSnSingleVO.getIsContainer());
        resultVO.setReworkFlag(HmeConstants.ConstantValue.YES);
        resultVO.setIsClickProcessComplete(hmeEoJobSnSingleVO.getIsClickProcessComplete());
        resultVO.setEntryStepFlag(hmeEoJobSnSingleVO.getEntryStepFlag());
        resultVO.setTestFlag(snVO.getTestFlag());
        resultVO.setIsShowCrossRetestBtn(snVO.getIsShowCrossRetestBtn());
        return resultVO;
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
        log.info("<====== HmeEoJobSnReworkServiceImpl.outSiteScan.Start tenantId={},dto={}", tenantId, dto);
        //????????????
        paramsVerificationForOutSite(tenantId, dto);

        //???????????????????????????
        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic = queryBasicData(tenantId, dto);

        //????????????,??????????????????????????????????????????????????????
        HmeEoJobSn validateResult = outSiteValidate(tenantId, hmeEoJobSnSingleBasic, dto);
        if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
            return validateResult;
        }
        // 20210818 add by sanfeng.zhang for peng.zhao ??????????????????????????? ??????????????????????????????
        if (hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId())) {
            String outSiteAction = hmeEoJobSnSingleService.queryOutSiteAction(tenantId, dto, hmeEoJobSnSingleBasic);
            dto.setOutSiteAction(outSiteAction);
        }
        // 2021-09-13 add by sanfeng.zhang for wenxin.zhang ??????????????????-??????????????????????????? ??????????????????????????????????????????????????????????????????SN ?????????????????????????????????
        Long pumpCount = hmeEoJobSnReWorkMapper.queryPumpProcessFlag(tenantId, hmeEoJobSnSingleBasic.getMaterialLot().getMaterialId());
        List<HmePumpCombDTO> pumpCombVOList = hmeEoJobSnReWorkMapper.queryPumpCombListByMaterialLotId(tenantId, hmeEoJobSnSingleBasic.getMaterialLot().getMaterialLotId());
        List<HmeEoJobPumpCombVO5> hmeEoJobPumpCombVO5List = new ArrayList<>();
        if(pumpCount.compareTo(0L) > 0 && CollectionUtils.isNotEmpty(pumpCombVOList)){
            hmeEoJobPumpCombVO5List = hmeEoJobPumpCombRepository.pumpFilterRuleVerify(tenantId, dto);
        }
        // ????????????????????????
        hmeEoJobSnSingleBasic.setReworkProcessFlag(HmeConstants.ConstantValue.YES);
        //??????????????????????????????????????????????????????????????????????????????
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            //????????????????????????
            if (!HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getMaterialLot().getReworkFlag())) {
                self().backFlushMaterialOutSite(tenantId, hmeEoJobSnSingleBasic, dto);
            }

            //???????????????????????????
            List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.mainOutSite(tenantId, dto, hmeEoJobSnSingleBasic);

            //??????????????????
            hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
        } else if (HmeConstants.OutSiteAction.REWORK_COMPLETE.equals(dto.getOutSiteAction())) {
            // ????????????
            //???????????????????????????
            List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.mainOutSite2(tenantId, dto, hmeEoJobSnSingleBasic);
            //??????????????????
            hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
        } else {
            hmeEoJobSnCommonService.mainOutSiteForRework(tenantId, dto, hmeEoJobSnSingleBasic);
        }

        //V20210428 modify by penglin.sui for tianyang.xie ????????????EO??????,???????????????????????????????????????????????????
        List<HmeWkcEoRel> hmeWkcEoRelList = hmeWkcEoRelRepository.selectByCondition(Condition.builder(HmeWkcEoRel.class)
                .andWhere(Sqls.custom().andEqualTo(HmeWkcEoRel.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeWkcEoRel.FIELD_WKC_ID, dto.getWorkcellId())
                        .andEqualTo(HmeWkcEoRel.FIELD_OPERATION_ID, dto.getOperationId())).build());
        if(CollectionUtils.isNotEmpty(hmeWkcEoRelList)){
            //??????
            hmeWkcEoRelRepository.batchDeleteByPrimaryKey(hmeWkcEoRelList);
        }

        // 2021-09-13 add by sanfeng.zhang for wenxin.zhang ???????????????????????????????????????????????????????????????????????????,????????????????????????
        Long startDate = System.currentTimeMillis();
        if(CollectionUtils.isNotEmpty(hmeEoJobPumpCombVO5List)){
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

        //??????????????????
        HmeEoJobSn result = getResultData(tenantId, hmeEoJobSnSingleBasic, dto);
        log.info("<====== HmeEoJobSnReworkServiceImpl.outSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
        return result;
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
        log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite tenantId={},hmeEoJobSnSingleBasic={},dto={}", tenantId, hmeEoJobSnSingleBasic, dto);
        //?????????????????????????????????????????????
        List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnSingleBasic.getBackFlushBomComponentList();
        if (CollectionUtils.isEmpty(backFlushBomComponentList)) {
            log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite Return:EO?????????????????????");
            return;
        }

        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
        HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity();

        //???????????????????????????????????????Y?????????????????????
        if (HmeConstants.ConstantValue.YES.equals(wo.getBackflushNotFlag())) {
            log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite Return:???????????????BackflushNotFlag???Y wo={}", wo);
            return;
        }

        //eo????????????wo????????????????????????
        if (CollectionUtils.isEmpty(eo.getBomComponentList())
                || CollectionUtils.isEmpty(wo.getBomComponentList())) {
            log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite Return:EO/WO???????????? " +
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
            log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite Return:?????????????????????EO?????? " +
                    "eoComponentList={},backFlushBomComponentIdList={}", eo.getBomComponentList(), backFlushBomComponentIdList);
            return;
        }

        //?????????????????????ID
        List<String> backFlushMaterialIdList = backFlushBomComponentList.stream()
                .filter(item -> releaseEoComponentIdList.contains(item.getBomComponentId()))
                .map(MtBomComponent::getMaterialId).collect(Collectors.toList());

        //??????????????????
        MtModLocator backFlushLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.BACKFLUSH,dto.getWorkcellId());

        //??????????????????
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

        //??????BOM??????
        BigDecimal bomPrimaryQty = BigDecimal.valueOf(eo.getBomPrimaryQty());

        //???????????????WO??????????????????
        Map<String, List<MtWorkOrderComponentActual>> woComponentActualMap = new HashMap<>();
        List<String> backFlushWoBomComponentIdList = wo.getBomComponentList().stream().map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(backFlushWoBomComponentIdList)) {
            SecurityTokenHelper.close();
            List<MtWorkOrderComponentActual> woComponentActualList = mtWorkOrderComponentActualRepository
                    .selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, wo.getWorkOrderId())
                                    .andIn(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, backFlushWoBomComponentIdList))
                            .build());
            if (CollectionUtils.isNotEmpty(woComponentActualList)) {
                woComponentActualMap = woComponentActualList.stream()
                        .collect(Collectors.groupingBy(MtWorkOrderComponentActual::getBomComponentId));
            }
        }

        log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite ??????????????????[{}]", allReleaseEoComponentList.size());

        //?????????????????????????????????
        List<MtAssembleProcessActualVO11> materialInfo = new ArrayList<>();
        Map<String, MtAssembleProcessActualVO17> eoAssembleProcessActualMap = new HashMap<>();
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
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
                log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite continue:???????????? wo??????????????????eo??????????????????????????? releaseEoComponent={}", releaseEoComponent);
                continue;
            }

            //?????????????????????????????????????????????????????????0??????
            BigDecimal woDemandQty = StringUtils.isBlank(releaseWoComponent.getWoBomComponentDemandQty())
                    ? BigDecimal.ZERO : new BigDecimal(releaseWoComponent.getWoBomComponentDemandQty());
            if(woDemandQty.compareTo(BigDecimal.ZERO) <= 0){
                log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite continue:??????????????????????????????0 releaseWoComponent={}", releaseWoComponent);
                continue;
            }

            //??????WO????????????????????????
            if (woComponentActualMap.containsKey(releaseWoComponent.getBomComponentId())) {
                double assembleQty = woComponentActualMap.get(releaseWoComponent.getBomComponentId())
                        .stream().mapToDouble(MtWorkOrderComponentActual::getAssembleQty).sum();
                if ((BigDecimal.valueOf(assembleQty).add(remainQty)).compareTo(woDemandQty) > 0) {
                    log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite continue:??????WO?????????????????????????????????" +
                            " releaseWoComponent={}, assembleQty={}, remainQty={}", releaseWoComponent, assembleQty, remainQty);
                    continue;
                }
            }

            log.info("<====== HmeEoJobSnReworkServiceImpl.batchBackFlushMaterialOutSite ???ID[{}],???????????????[{}]", releaseWoComponent.getBomComponentId(), remainQty);

            //????????????????????????
            for (HmeMaterialLotVO3 releaseMaterialLot : releaseMaterialLotList) {
                //????????????
                BigDecimal currentQty = remainQty;
                if (remainQty.compareTo(BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty())) >= 0) {
                    currentQty = BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty());
                }
                remainQty = remainQty.subtract(currentQty);

                //API-???????????????
                MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                mtMaterialLotVO1.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                mtMaterialLotVO1.setPrimaryUomId(releaseMaterialLot.getPrimaryUomId());
                mtMaterialLotVO1.setTrxPrimaryUomQty(currentQty.doubleValue());
                mtMaterialLotVO1.setEventRequestId(eventRequestId);
                if (StringUtils.isNotEmpty(releaseMaterialLot.getSecondaryUomId())) {
                    mtMaterialLotVO1.setSecondaryUomId(releaseMaterialLot.getSecondaryUomId());
                    mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
                }
                mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);

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

                if (remainQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
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
            mtAssembleProcessActualRepository.componentAssembleBatchProcess(tenantId, assembleProcessActualVO16);
        }

        if (CollectionUtils.isNotEmpty(objectTransactionRequestList)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO3 release(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<================??????????????????====================>");
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO3 hmeEoJobSnVO = hmeEoJobSnLotMaterialRepository.lotMaterialOutSite2(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("===================================>??????????????????" + (endDate - startDate)+ "ms");
        log.info("<================??????????????????====================>");
        return hmeEoJobSnVO;
    }

    /**
     *
     * @Description ????????????
     *
     * @author penglin.sui
     * @date 2020/12/24 21:19
     * @param tenantId ??????ID
     * @param dto ??????
     * @return void
     *
     */
    private void releaseValidate(Long tenantId, HmeEoJobSnReworkVO4 dto){
        if(CollectionUtils.isEmpty(dto.getComponentList())){
            //??????????????????????????????
            throw new MtException("HME_EO_JOB_SN_129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_129", "HME"));
        }

        List<HmeEoJobSnReworkVO> hmeEoJobSnReworkVOList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(hmeEoJobSnReworkVOList)){
            //??????????????????????????????
            throw new MtException("HME_EO_JOB_SN_129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_129", "HME"));
        }
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author penglin.sui
     * @date 2020/11/26 16:46
     * @param tenantId ??????ID
     * @return Map<String,WmsTransactionTypeDTO>
     *
     */
    private Map<String, WmsTransactionTypeDTO> selectTransactionType(Long tenantId){
        //????????????????????????
        List<String> transactionTypeCodeList = new ArrayList<>();
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.WMS_INSDID_ORDER_S_I);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.WMS_INSDID_ORDER_S_R);
        List<WmsTransactionTypeDTO> wmsTransactionTypeList = new ArrayList<>();
        for (String transactionTypeCode:transactionTypeCodeList
             ) {
            WmsTransactionTypeDTO wmsTransactionTypeDTO = wmsTransactionTypeRepository.getTransactionType(tenantId,transactionTypeCode);
            if(Objects.nonNull(wmsTransactionTypeDTO)) {
                wmsTransactionTypeList.add(wmsTransactionTypeDTO);
            }
        }
        //List<WmsTransactionTypeDTO> wmsTransactionTypeList = wmsTransactionTypeRepository.batchGetTransactionType(tenantId,transactionTypeCodeList);

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
     * @date 2020/12/24 22:53
     * @param tenantId ??????ID
     * @param dto ??????
     * @return Map<String,MtEoComponentActual>
     *
     */
    private Map<String, MtEoComponentActual> eoComponentActualGet(Long tenantId, HmeEoJobSnReworkVO4 dto){
        //??????????????????
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(dto.getSnLine().getEoId());
        List<String> routerStepIdList = new ArrayList<>(1);
        routerStepIdList.add(dto.getSnLine().getEoStepId());
        List<String> materialIdList = new ArrayList<>();
        materialIdList.add(dto.getComponentList().stream().map(HmeEoJobSnReworkVO::getMaterialId).collect(Collectors.joining()));
        SecurityTokenHelper.close();
        List<MtEoComponentActual> mtEoComponentActualList = hmeEoJobSnBatchMapper.selectComponentAssemble(tenantId,eoIdList,routerStepIdList,materialIdList);
        Map<String, MtEoComponentActual> mtEoComponentActualMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mtEoComponentActualList)){
            mtEoComponentActualMap = mtEoComponentActualList.stream().collect(Collectors.toMap(item -> fetchGroupKey3(item.getEoId(),item.getRouterStepId(),item.getMaterialId()),t -> t));
        }
        return mtEoComponentActualMap;
    }

    private Map<String, MtEoComponentActual> eoComponentActualQuery(Long tenantId, HmeEoJobSnReworkVO4 dto, String materialId){
        //??????????????????
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(dto.getSnLine().getEoId());
        List<String> routerStepIdList = new ArrayList<>(1);
        routerStepIdList.add(dto.getSnLine().getEoStepId());
        List<String> materialIdList = new ArrayList<>();
        materialIdList.add(materialId);
        SecurityTokenHelper.close();
        List<MtEoComponentActual> mtEoComponentActualList = hmeEoJobSnBatchMapper.selectComponentAssemble(tenantId,eoIdList,routerStepIdList,materialIdList);
        Map<String, MtEoComponentActual> mtEoComponentActualMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mtEoComponentActualList)){
            mtEoComponentActualMap = mtEoComponentActualList.stream().collect(Collectors.toMap(item -> fetchGroupKey3(item.getEoId(),item.getRouterStepId(),item.getMaterialId()),t -> t));
        }
        return mtEoComponentActualMap;
    }


    /**
     *
     * @Description ??????????????????
     *
     * @author penglin.sui
     * @date 2020/12/24 23:06
     * @param tenantId ??????ID
     * @return Map<String,MtEoRouter>
     *
     */
    private Map<String, MtEoRouter> selectRouterMap(Long tenantId, HmeEoJobSnVO dto){
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(dto.getEoId());
        List<MtEoRouter> eoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId,eoIdList);
        Map<String, MtEoRouter> eoRouterMap = eoRouterList.stream().collect(Collectors.toMap(MtEoRouter::getEoId, t -> t));
        if(eoRouterMap.size() != eoIdList.size()){
            //??????????????????????????????${1}
            throw new MtException("MT_ORDER_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0036", "HME",""));
        }
        return eoRouterMap;
    }

    /**
     *
     * @Description ????????????
     *
     * @author penglin.sui
     * @date 2020/12/24 22:31
     * @param tenantId ??????ID
     * @param dto ??????
     * @return HmeEoJobSnReworkVO5
     *
     */
    private HmeEoJobSnReworkVO5 releaseDataGet(Long tenantId, HmeEoJobSnReworkVO4 dto){
        HmeEoJobSnReworkVO5 resultVO = new HmeEoJobSnReworkVO5();
        Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap = selectTransactionType(tenantId);
        resultVO.setWmsTransactionTypeMap(wmsTransactionTypeMap);
        SecurityTokenHelper.close();
        MtModLocator areaLocator =  hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,dto.getComponentList().get(0).getMaterialLotList().get(0).getLocatorId());
        if(Objects.isNull(areaLocator)){
            //????????????????????????
            throw new MtException("HME_EO_JOB_SN_132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_132", "HME"));
        }
        resultVO.setAreaLocator(areaLocator);

        //????????????????????????
        List<String> locatorIdList = new ArrayList<>();
        for (HmeEoJobSnReworkVO hmeEoJobSnBatchVO4:dto.getComponentList()
        ) {
            if(CollectionUtils.isEmpty(hmeEoJobSnBatchVO4.getMaterialLotList())){
                continue;
            }
            locatorIdList.addAll(hmeEoJobSnBatchVO4.getMaterialLotList().stream().map(HmeEoJobSnReworkVO3::getLocatorId).distinct().collect(Collectors.toList()));
        }
        locatorIdList = locatorIdList.stream().distinct().collect(Collectors.toList());
        List<HmeModLocatorVO2> hmeModLocatorVO2List =  hmeEoJobSnLotMaterialMapper.batchQueryAreaLocator(tenantId,locatorIdList);
        if(CollectionUtils.isEmpty(hmeModLocatorVO2List)  || hmeModLocatorVO2List.size() != locatorIdList.size()){
            //????????????????????????
            throw new MtException("HME_EO_JOB_SN_132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_132", "HME"));
        }
        Map<String, HmeModLocatorVO2> areaLocatorMap = hmeModLocatorVO2List.stream().collect(Collectors.toMap(item -> item.getSubLocatorId(), t -> t));
        resultVO.setAreaLocatorMap(areaLocatorMap);

        //????????????????????????
        List<String> materialLotIdList = new ArrayList<>();
        for (HmeEoJobSnReworkVO component:dto.getComponentList()
             ) {
            materialLotIdList.addAll(component.getMaterialLotList().stream().map(HmeEoJobSnReworkVO3::getMaterialLotId).collect(Collectors.toList()));
        }
        List<String> materialLotAttrNameList = new ArrayList<>();
        materialLotAttrNameList.add(HmeConstants.ExtendAttr.MATERIAL_VERSION);
        materialLotAttrNameList.add(HmeConstants.ExtendAttr.SO_NUM);
        materialLotAttrNameList.add(HmeConstants.ExtendAttr.SO_LINE_NUM);
        SecurityTokenHelper.close();
        List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_material_lot_attr","MATERIAL_LOT_ID"
                ,materialLotIdList,materialLotAttrNameList);
        Map<String,String> materialLotExtendAttrMap = new HashMap<String,String>();
        if(CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)){
            materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), MtExtendAttrVO1::getAttrValue));
        }
        resultVO.setMaterialLotExtendAttrMap(materialLotExtendAttrMap);

        //??????????????????
        resultVO.setMtEoComponentActualMap(eoComponentActualGet(tenantId,dto));

        //??????
        HmeEoJobSnBatchVO11 hmeEoJobShift = hmeEoJobSnBatchMapper.selectJobShift(tenantId,dto.getSnLine().getJobId());
        if(Objects.nonNull(hmeEoJobShift)){
            resultVO.setHmeEoJobShift(hmeEoJobShift);
        }

        //????????????
        resultVO.setEoRouterMap(selectRouterMap(tenantId,dto.getSnLine()));

        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSnReworkVO> release(Long tenantId, HmeEoJobSnReworkVO4 dto) {
        //??????????????????
        if(CollectionUtils.isNotEmpty(dto.getComponentList())){
            dto.setComponentList(dto.getComponentList().stream().distinct().collect(Collectors.toList()));
            dto.setComponentList(dto.getComponentList().stream().filter(distinctByKey(item -> item.getMaterialId()))
                    .collect(Collectors.toList()));
        }

        //??????
        releaseValidate(tenantId,dto);

        //????????????
        HmeEoJobSnReworkVO5 releaseData = releaseDataGet(tenantId,dto);

        //?????????????????????
        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");

        // ????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        releaseData.setEventRequestId(eventRequestId);
        releaseData.setEventId(eventId);
        releaseData.setDto(dto);

        //???????????????
        Boolean normalExecApiFlag = false;
        HmeEoJobSnSingleVO4 normalResultVO = normalRelease(tenantId, releaseData);
        if (Objects.nonNull(normalResultVO)) {
            normalExecApiFlag = normalResultVO.getExecReleaseFlag();
        }
        // ????????????
        List<HmeEoJobSnVO21> upGradeMaterialLotList = new ArrayList<>();

        //?????????
        List<HmeEoJobSnReworkVO> resultVOList = dto.getComponentList();

        //??????API
        if (normalExecApiFlag) {
            // ?????????????????????
            for (HmeEoJobSnReworkVO component : dto.getComponentList()) {
                for (HmeEoJobSnReworkVO3 componentMaterialLot : component.getMaterialLotList()) {
                    //???????????????????????????
                    if (HmeConstants.MaterialTypeCode.SN.equals(component.getProductionType()) && HmeConstants.ConstantValue.YES.equals(component.getUpgradeFlag())) {
                        HmeEoJobSnVO21 updateMaterialLot = new HmeEoJobSnVO21();
                        updateMaterialLot.setMaterialLotCode(componentMaterialLot.getMaterialLotCode());
                        updateMaterialLot.setMaterialId(componentMaterialLot.getMaterialId());
                        upGradeMaterialLotList.add(updateMaterialLot);
                    }
                }
            }

            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = normalResultVO.getObjectTransactionRequestList();
            List<HmeEoJobSnLotMaterial> eoJobSnInsertDataList = normalResultVO.getEoJobSnInsertDataList();
            List<HmeEoJobMaterial> eoJobMaterialInsertDataList = normalResultVO.getEoJobMaterialInsertDataList();
            List<HmeEoJobLotMaterial> deleteLotDataList = normalResultVO.getDeleteLotDataList();
            List<HmeEoJobTimeMaterial> deleteTimeDataList = normalResultVO.getDeleteTimeDataList();
            List<HmeEoJobMaterial> updateSnDataList = normalResultVO.getUpdateSnDataList();

            List<MtAssembleProcessActualVO11> materialInfoList = normalResultVO.getMaterialInfoList();
            //???????????????
            for (MtMaterialLotVO1 value : normalResultVO.getMaterialLotConsumeMap().values()) {
                mtMaterialLotRepository.materialLotConsume(tenantId, value);
            }
            //????????????API??????
            MtAssembleProcessActualVO16 mtAssembleProcessActualVO16 = new MtAssembleProcessActualVO16();
            MtAssembleProcessActualVO17 mtAssembleProcessActualVO17 = new MtAssembleProcessActualVO17();
            List<MtAssembleProcessActualVO17> eoInfoList = new ArrayList<>();
            //??????????????????API??????
            mtAssembleProcessActualVO16.setOperationId(dto.getSnLine().getOperationId());
            mtAssembleProcessActualVO16.setOperationBy(String.valueOf(userId));
            mtAssembleProcessActualVO16.setWorkcellId(dto.getSnLine().getWorkcellId());
            mtAssembleProcessActualVO16.setEventRequestId(releaseData.getEventRequestId());
            mtAssembleProcessActualVO16.setParentEventId(releaseData.getEventId());
            if (Objects.nonNull(releaseData.getHmeEoJobShift())) {
                mtAssembleProcessActualVO16.setShiftCode(releaseData.getHmeEoJobShift().getShiftCode());
                mtAssembleProcessActualVO16.setShiftDate(releaseData.getHmeEoJobShift().getShiftDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            mtAssembleProcessActualVO16.setLocatorId(dto.getComponentList().get(0).getMaterialLotList().get(0).getLocatorId());

            mtAssembleProcessActualVO17.setEoId(dto.getSnLine().getEoId());
            mtAssembleProcessActualVO17.setRouterId(releaseData.getEoRouterMap().get(dto.getSnLine().getEoId()).getRouterId());
            mtAssembleProcessActualVO17.setRouterStepId(dto.getSnLine().getEoStepId());
            mtAssembleProcessActualVO17.setMaterialInfo(materialInfoList);
            eoInfoList.add(mtAssembleProcessActualVO17);
            mtAssembleProcessActualVO16.setEoInfo(eoInfoList);
            //??????????????????API
            mtAssembleProcessActualRepository.componentAssembleBatchProcess(tenantId, mtAssembleProcessActualVO16);

            //??????????????????API
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

            int count = 0;
            // ????????????/????????????????????????
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
            //????????????/????????????????????????
            if(CollectionUtils.isNotEmpty(eoJobSnInsertDataList)) {
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

            if (CollectionUtils.isNotEmpty(updateSnDataList)) {
                List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_material_cid_s", updateSnDataList.size());
                List<String> updateSnDataSqlList = new ArrayList<>();
                count = 0;
                for (HmeEoJobMaterial updateData : updateSnDataList) {
                    updateData.setCid(Long.valueOf(cidS.get(count)));
                    updateData.setLastUpdatedBy(userId);
                    updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                    updateSnDataSqlList.addAll(customDbRepository.getUpdateSql(updateData));
                    count++;
                }
                jdbcTemplate.batchUpdate(updateSnDataSqlList.toArray(new String[updateSnDataSqlList.size()]));
                eoJobMaterialInsertDataList = updateSnDataList;
                // 20210922 add by sanfeng.zhang for hui.gu ?????????????????????
                this.savePumpPosition(tenantId, eoJobMaterialInsertDataList, dto);
            }

            //???????????????????????????????????????????????????
            resultVOList.forEach(item -> {
                //?????????????????????
                if(CollectionUtils.isNotEmpty(item.getMaterialLotList())) {
                    item.getMaterialLotList().removeIf(item2 -> item2.getPrimaryUomQty().compareTo(BigDecimal.ZERO) <= 0);
                    item.setSelectedSnCount(BigDecimal.valueOf(item.getMaterialLotList().size()));
                    if (item.getSelectedSnCount().compareTo(BigDecimal.ZERO) <= 0) {
                        item.setSelectedSnQty(BigDecimal.ZERO);
                    } else {
                        item.setSelectedSnQty(item.getMaterialLotList().stream().map(HmeEoJobSnReworkVO3::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                    }
                }
            });
        }

        // ???????????????????????????
        if (CollectionUtils.isNotEmpty(upGradeMaterialLotList)) {
            // ???????????????????????????????????????????????? ???????????????
            List<HmeServiceSplitRecord> hmeServiceSplitRecordList = hmeServiceSplitRecordMapper.select(new HmeServiceSplitRecord() {{
                setTenantId(tenantId);
                setWorkOrderNum(dto.getSnLine().getWorkOrderNum());
            }});
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(dto.getSnLine().getWorkOrderId());
                setTableName("mt_work_order_attr");
                setAttrName("attribute5");
            }});
            String admin = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.REWORK_UPGRADE_WOADMIN", tenantId);
            Optional<LovValueDTO> adminOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(admin, lov.getValue())).findFirst();
            if (adminOpt.isPresent() && CollectionUtils.isEmpty(hmeServiceSplitRecordList)) {
                HmeEoJobSnVO20 hmeEoJobSnVO20 = new HmeEoJobSnVO20();
                hmeEoJobSnVO20.setSiteId(dto.getSnLine().getSiteId());
                hmeEoJobSnVO20.setEoId(dto.getSnLine().getEoId());
                hmeEoJobSnVO20.setWorkOrderId(dto.getSnLine().getWorkOrderId());
                hmeEoJobSnVO20.setEoStepId(dto.getSnLine().getEoStepId());
                hmeEoJobSnVO20.setSnMaterialLotId(dto.getSnLine().getMaterialLotId());
                hmeEoJobSnVO20.setJobId(dto.getSnLine().getJobId());
                hmeEoJobSnVO20.setMaterialLotList(upGradeMaterialLotList);
                this.snReworkBatchUpgrade(tenantId, hmeEoJobSnVO20);
            }
        }
        //?????????
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
    private void savePumpPosition(Long tenantId, List<HmeEoJobMaterial> snMaterialLotList, HmeEoJobSnReworkVO4 dto) {
        List<HmePumpModPositionHeader> headerList = new ArrayList<>();
        List<HmePumpModPositionLine> lineList = new ArrayList<>();
        List<LovValueDTO> positionList = lovAdapter.queryLovValue("HME.PUMP_POSITION", tenantId);
        String eoId = dto.getSnLine().getEoId();
        String workOrderId = dto.getSnLine().getWorkOrderId();
        Map<String, List<HmePumpCombVO>> pumpCombVOMap = new HashMap<>();
        for (HmeEoJobMaterial eoJobMaterial : snMaterialLotList) {
            List<HmePumpCombVO> pumpCombVOList = hmeEoJobSnBatchMapper.queryPumpCombListByMaterialLotId(tenantId, eoJobMaterial.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                pumpCombVOMap.put(eoJobMaterial.getMaterialLotId(), pumpCombVOList);
            }
        }
        if (MapUtils.isNotEmpty(pumpCombVOMap)) {
            // ?????????????????? ?????????????????????????????????eo ?????? ????????????????????? ???????????????eo????????????
            boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnLine().getSnNum());
            if (!newMaterialLotFlag) {
                List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).select(MtEo.FIELD_EO_ID, MtEo.FIELD_WORK_ORDER_ID).andWhere(Sqls.custom()
                        .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtEo.FIELD_IDENTIFICATION, dto.getSnLine().getSnNum())).build());
                if (CollectionUtils.isEmpty(mtEoList)) {
                    throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_004", "HME", dto.getSnLine().getSnNum()));
                }
                eoId = mtEoList.get(0).getEoId();
                workOrderId = mtEoList.get(0).getWorkOrderId();
            }
            for (String pumpMapKey : pumpCombVOMap.keySet()) {
                HmePumpModPositionHeader positionHeader = new HmePumpModPositionHeader();
                positionHeader.setWorkOrderId(workOrderId);
                positionHeader.setEoId(eoId);
                positionHeader.setCombMaterialLotId(pumpMapKey);
                positionHeader.setQty(BigDecimal.ONE);
                headerList.add(positionHeader);
            }
        }
        if (CollectionUtils.isNotEmpty(headerList)) {
            List<String> positionHeaderIds = customDbRepository.getNextKeys("hme_pump_mod_position_header_s", headerList.size());
            String positionHeaderCid = customDbRepository.getNextKey("hme_pump_mod_position_header_cid_s");
            Integer indexNum = 0;
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            Date nowDate = CommonUtils.currentTimeGet();
            // ??????SN?????????????????????????????????  ?????????26 ?????????????????????????????? ??????????????????
            List<String> pumpModPositionLineList = hmePumpModPositionLineRepository.queryPumpPositionLineByEoId(tenantId, eoId);
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
     * ????????????????????????
     *
     * @param tenantId  ??????ID
     * @param dto       ????????????
     * @return HmeEoJobSnVO9
     */
    private HmeEoJobSnReworkVO6 releaseBackDataGet(Long tenantId, HmeEoJobSnVO9 dto, HmeEoJobSnBatchVO20 hmeEoJobSnBatchVO20){
        HmeEoJobSnReworkVO6 resultVO = new HmeEoJobSnReworkVO6();
        //????????????????????????
        List<String> woAttrNameList = new ArrayList<>();
        woAttrNameList.add(HmeConstants.woExtendAttr.SO_NUM);
        woAttrNameList.add(HmeConstants.woExtendAttr.SO_LINE_NUM);
        Map<String, String> woExtendAttrMap = new HashMap<>(woAttrNameList.size());
        List<String> workOrderIdList = new ArrayList<>(1);
        workOrderIdList.add(dto.getWorkOrderId());
        SecurityTokenHelper.close();
        List<MtExtendAttrVO1> woExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_work_order_attr", "WORK_ORDER_ID"
                , workOrderIdList, woAttrNameList);
        if (CollectionUtils.isNotEmpty(woExtendAttrVO1List)) {
            woExtendAttrMap = woExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), MtExtendAttrVO1::getAttrValue));
        }
        //??????????????????
        Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap = selectTransactionType(tenantId);
        //set????????????
        resultVO.setWoExtendAttrMap(woExtendAttrMap);
        resultVO.setWmsTransactionTypeMap(wmsTransactionTypeMap);
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto) {
        //??????
        dto.setReworkSourceFlag(YES);
        HmeEoJobSnBatchVO20 hmeEoJobSnBatchVO20 = hmeEoJobSnBatchValidateService.releaseBackValidate(tenantId,dto);
        //???????????????
        HmeEoJobSnBatchVO21 backMaterialLot = hmeEoJobSnBatchVO20.getBackMaterialLot();
        //???????????????????????????
        Map<String,String> materialLotAttrMap = hmeEoJobSnBatchVO20.getMaterialLotAttrMap();
        //????????????
        HmeEoJobSnReworkVO6 queryDataResultVO = releaseBackDataGet(tenantId,dto,hmeEoJobSnBatchVO20);
        //??????????????????
        Map<String,String> woExtendAttrMap = queryDataResultVO.getWoExtendAttrMap();
        //????????????
        Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap = queryDataResultVO.getWmsTransactionTypeMap();

        //???????????????????????????
        MtEventCreateVO wipReturnEventCreateVO = new MtEventCreateVO();
        wipReturnEventCreateVO.setEventTypeCode("WIP_RETURN");
        String wipReturnEventId = mtEventRepository.eventCreate(tenantId, wipReturnEventCreateVO);
        String wipReturnEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_RETURN");

        Long userId = -1L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        String batchId = Utils.getBatchId();

        //??????????????????
        BigDecimal currComponentReleaseBackQty = dto.getBackQty();

        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getCurrentWorkcellId());
        List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.WORKCELL_ISSUE_RETURN_EX", tenantId);
        Optional<LovValueDTO> firstOpt = lovValue.stream().filter(lov -> StringUtils.equals(mtModWorkcell.getWorkcellCode(), lov.getValue())).findFirst();
        String locatorId = backMaterialLot.getLocatorId();
        String locatorCode = backMaterialLot.getLocatorCode();
        String warehouseId = backMaterialLot.getAreaLocatorId();
        String warehouseCode = backMaterialLot.getAreaLocatorCode();
        if (firstOpt.isPresent()) {
            //??????????????????????????????????????????
            List<MtModLocator> relBackLocatorList = hmeEoJobSnReWorkMapper.queryReleaseBackLocator(tenantId, mtModWorkcell.getWorkcellId(), dto.getSiteId());
            if (CollectionUtils.isEmpty(relBackLocatorList)) {
                // ???????????????????????????????????????
                List<MtModLocator> locatorList = hmeEoJobSnReWorkMapper.queryDeliveryLocator(tenantId, mtModWorkcell.getWorkcellId(), dto.getSiteId());
                if (CollectionUtils.isEmpty(locatorList)) {
                    throw new MtException("HME_EO_JOB_SN_088", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_088", HME));
                }
                if (locatorList.size() > 1) {
                    throw new MtException("HME_EO_JOB_SN_209", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_209", HME));
                }
                locatorId = locatorList.get(0).getLocatorId();
                locatorCode = locatorList.get(0).getLocatorCode();
                // ??????????????????????????????
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorList.get(0).getParentLocatorId());
                warehouseId = mtModLocator.getLocatorId();
                warehouseCode = mtModLocator.getLocatorCode();
            } else {
                if (relBackLocatorList.size() > 1) {
                    throw new MtException("HME_EO_JOB_SN_248", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_248", HME, mtModWorkcell.getWorkcellCode() ));
                }
                if (StringUtils.isEmpty(relBackLocatorList.get(0).getParentLocatorId())) {
                    throw new MtException("HME_SPLIT_RECORD_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SPLIT_RECORD_0019", HME, relBackLocatorList.get(0).getLocatorCode()));
                }
                locatorId = relBackLocatorList.get(0).getLocatorId();
                locatorCode = relBackLocatorList.get(0).getLocatorCode();
                // ??????????????????????????????
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(relBackLocatorList.get(0).getParentLocatorId());
                warehouseId = mtModLocator.getLocatorId();
                warehouseCode = mtModLocator.getLocatorCode();

            }

            // ???????????? ??????????????? ??????????????????
            if (Objects.nonNull(backMaterialLot) && backMaterialLot.getPrimaryUomQty()>0) {
                // ????????????????????????  ????????????????????????????????????
                if (!locatorId.equals(backMaterialLot.getLocatorId()) ) {
                    throw new MtException("HME_RELAESE_BACK_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_RELAESE_BACK_001", HME, backMaterialLot.getMaterialLotCode()));
                }
            }

        }

        //???????????????API??????
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(locatorId);
        mtInvOnhandQuantityVO9.setMaterialId(dto.getMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(currComponentReleaseBackQty.doubleValue());
        mtInvOnhandQuantityVO9.setEventId(wipReturnEventId);
        mtInvOnhandQuantityVO9.setLotCode(backMaterialLot.getLot());

        // ????????????????????????
        String workOrderId = dto.getWorkOrderId();
        if (StringUtils.isBlank(workOrderId)){
            //?????????????????????,?????????!
            throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0002", "HME"));
        }

        HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, dto.getWorkOrderNum());
        String transactionCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT;
        String trxWorkOrderNum = dto.getCurrentWorkOrderNum();
        String trxInsideOrder = null;
        // ????????????????????????????????????????????????
        if (!Objects.isNull(splitRecord)){
            //?????????????????????????????????
            if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())){
                transactionCode = HmeConstants.TransactionTypeCode.WMS_INSDID_ORDER_S_R;
                trxInsideOrder = splitRecord.getInternalOrderNum();
                trxWorkOrderNum = "";
            }
        }

        //??????API
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
        objectTransactionVO.setTransactionTypeCode(transactionCode);
        objectTransactionVO.setEventId(wipReturnEventId);
        objectTransactionVO.setMaterialLotId(backMaterialLot.getMaterialLotId());
        objectTransactionVO.setMaterialId(dto.getMaterialId());
        objectTransactionVO.setMaterialCode(dto.getMaterialCode());
        objectTransactionVO.setLotNumber(backMaterialLot.getLot());
        objectTransactionVO.setTransactionUom(dto.getUomCode());
        objectTransactionVO.setTransactionTime(CommonUtils.currentTimeGet());
        objectTransactionVO.setPlantId(backMaterialLot.getSiteId());
        objectTransactionVO.setPlantCode(backMaterialLot.getSiteCode());
        objectTransactionVO.setLocatorId(locatorId);
        objectTransactionVO.setLocatorCode(locatorCode);
        objectTransactionVO.setWarehouseId(warehouseId);
        objectTransactionVO.setWarehouseCode(warehouseCode);
        objectTransactionVO.setWorkOrderNum(trxWorkOrderNum);
        objectTransactionVO.setInsideOrder(trxInsideOrder);
        objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
        objectTransactionVO.setProdLineCode(dto.getProdLineCode());
        objectTransactionVO.setSoNum(woExtendAttrMap.get(fetchGroupKey2(dto.getWorkOrderId(), HmeConstants.woExtendAttr.SO_NUM)));
        objectTransactionVO.setSoLineNum(woExtendAttrMap.get(fetchGroupKey2(dto.getWorkOrderId(), HmeConstants.woExtendAttr.SO_LINE_NUM)));
        objectTransactionVO.setAttribute16(batchId);
        objectTransactionVO.setMoveReason("???????????????");
        objectTransactionVO.setRemark("???????????????");
        objectTransactionVO.setTransactionTypeCode(transactionCode);
        objectTransactionVO.setMoveType(wmsTransactionTypeMap.get(transactionCode).getMoveType());
        objectTransactionVO.setTransactionQty(currComponentReleaseBackQty);
        objectTransactionRequestList.add(objectTransactionVO);

        // ???????????? ??????????????????????????????
        List<MtExtendVO5> attrList = new ArrayList<>();
        Boolean upgradeSn = false;
        if (YES.equals(dto.getUpgradeFlag()) && HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            // ???????????????????????????????????????????????? ???????????????
            List<HmeServiceSplitRecord> hmeServiceSplitRecordList = hmeServiceSplitRecordMapper.select(new HmeServiceSplitRecord() {{
                setTenantId(tenantId);
                setWorkOrderNum(dto.getWorkOrderNum());
            }});
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(dto.getWorkOrderId());
                setTableName("mt_work_order_attr");
                setAttrName("attribute5");
            }});
            String admin = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.REWORK_UPGRADE_WOADMIN", tenantId);
            Optional<LovValueDTO> adminOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(admin, lov.getValue())).findFirst();
            if (adminOpt.isPresent() && CollectionUtils.isEmpty(hmeServiceSplitRecordList)) {
                String eoSnReductionEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("EO_SN_REDUCTION");
                }});
                MtMaterial releaseMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
                // ???????????????????????????????????????????????????
                MtMaterialLotVO2 update = new MtMaterialLotVO2();
                update.setPrimaryUomQty(dto.getBackQty().doubleValue());
                update.setMaterialLotId(backMaterialLot.getMaterialLotId());
                update.setMaterialId(dto.getMaterialId());
                update.setPrimaryUomId(releaseMaterial.getPrimaryUomId());
                update.setEventId(eoSnReductionEventId);
                update.setSiteId(dto.getSiteId());
                update.setQualityStatus(OK);
                update.setLoadTime(null);
                update.setInSiteTime(null);
                update.setEoId("");
                update.setEnableFlag(YES);
                mtMaterialLotRepository.materialLotUpdate(tenantId, update, NO);

                // 20210412 add by sanfeng.zhang for fang.pan ?????????????????? ?????????
                MtExtendVO5 mfFlagAttr = new MtExtendVO5();
                mfFlagAttr.setAttrName("MF_FLAG");
                mfFlagAttr.setAttrValue("");
                attrList.add(mfFlagAttr);
                MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
                reworkFlagAttr.setAttrName("REWORK_FLAG");
                reworkFlagAttr.setAttrValue("");
                attrList.add(reworkFlagAttr);
                MtExtendVO5 performanceLevelAttr = new MtExtendVO5();
                performanceLevelAttr.setAttrName("PERFORMANCE_LEVEL");
                performanceLevelAttr.setAttrValue("");
                attrList.add(performanceLevelAttr);
                MtExtendVO5 topEoAttr = new MtExtendVO5();
                topEoAttr.setAttrName("TOP_EO_ID");
                topEoAttr.setAttrValue("");
                attrList.add(topEoAttr);
                MtExtendVO5 renovateFlagAttr = new MtExtendVO5();
                renovateFlagAttr.setAttrName("RENOVATE_FLAG");
                renovateFlagAttr.setAttrValue("");
                attrList.add(renovateFlagAttr);
                MtExtendVO5 afFlagAttr = new MtExtendVO5();
                afFlagAttr.setAttrName("AF_FLAG");
                afFlagAttr.setAttrValue("");
                attrList.add(afFlagAttr);
                MtExtendVO5 statusAttr = new MtExtendVO5();
                statusAttr.setAttrName("STATUS");
                statusAttr.setAttrValue("INSTOCK");
                attrList.add(statusAttr);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", backMaterialLot.getMaterialLotId(), eoSnReductionEventId, attrList);
                upgradeSn = true;
            }
        }
        if (!upgradeSn) {
            //??????????????????
            MtMaterialLotVO2 materialLotVO = new MtMaterialLotVO2();
            materialLotVO.setTenantId(tenantId);
            materialLotVO.setMaterialLotId(backMaterialLot.getMaterialLotId());
            materialLotVO.setEventId(wipReturnEventId);
            materialLotVO.setQualityStatus(HmeConstants.ConstantValue.OK);
            materialLotVO.setTrxPrimaryUomQty(dto.getBackQty().doubleValue());
            materialLotVO.setEnableFlag(HmeConstants.ConstantValue.YES);
            materialLotVO.setLocatorId(locatorId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO, HmeConstants.ConstantValue.NO);
        }
        //??????????????????????????????
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //???????????????
            HmeEoJobMaterial existsHmeEoJobMaterial = hmeEoJobMaterialRepository.selectByPrimaryKey(dto.getJobMaterialId());

            HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
            hmeEoJobMaterial.setTenantId(tenantId);
            hmeEoJobMaterial.setJobId(dto.getCurrentJobId());
            hmeEoJobMaterial.setWorkcellId(existsHmeEoJobMaterial.getWorkcellId());
            hmeEoJobMaterial.setSnMaterialId(existsHmeEoJobMaterial.getSnMaterialId());
            hmeEoJobMaterial.setMaterialId(existsHmeEoJobMaterial.getMaterialId());
            hmeEoJobMaterial.setMaterialLotId(backMaterialLot.getMaterialLotId());
            hmeEoJobMaterial.setMaterialLotCode(backMaterialLot.getMaterialLotCode());
            hmeEoJobMaterial.setReleaseQty(dto.getBackQty().multiply(new BigDecimal(-1)));
            hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ZERO);
            hmeEoJobMaterial.setEoId(existsHmeEoJobMaterial.getEoId());
            hmeEoJobMaterial.setBomComponentId(existsHmeEoJobMaterial.getBomComponentId());
            hmeEoJobMaterial.setBydMaterialId(existsHmeEoJobMaterial.getBydMaterialId());
            hmeEoJobMaterial.setLocatorId(existsHmeEoJobMaterial.getLocatorId());
            hmeEoJobMaterial.setLotCode(existsHmeEoJobMaterial.getLotCode());
            hmeEoJobMaterial.setProductionVersion(existsHmeEoJobMaterial.getProductionVersion());
            hmeEoJobMaterial.setVirtualFlag(existsHmeEoJobMaterial.getVirtualFlag());
            hmeEoJobMaterial.setParentMaterialLotId(existsHmeEoJobMaterial.getParentMaterialLotId());
            hmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty());
            hmeEoJobMaterialRepository.insert(hmeEoJobMaterial);

            //???????????????????????????is_issued
            existsHmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ZERO);
            if (Objects.nonNull(existsHmeEoJobMaterial.getRemainQty())) {
                existsHmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty().subtract(dto.getBackQty()));
            }
            existsHmeEoJobMaterial.setMaterialLotId(null);
            existsHmeEoJobMaterial.setMaterialLotCode(null);
            hmeEoJobMaterialMapper.updateByPrimaryKey(existsHmeEoJobMaterial);

            //??????SN??????????????????????????????????????? ????????????????????????SN
            List<HmePumpCombVO> pumpCombVOList = hmeEoJobSnBatchMapper.queryPumpCombListByMaterialLotId(tenantId, backMaterialLot.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                // ?????????????????? ?????????????????????????????????eo ?????? ????????????????????? ???????????????eo????????????
                boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getMaterialLotCode());
                String eoId = dto.getEoId();
                if (!newMaterialLotFlag) {
                    List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).select(MtEo.FIELD_EO_ID, MtEo.FIELD_WORK_ORDER_ID).andWhere(Sqls.custom()
                            .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtEo.FIELD_IDENTIFICATION, dto.getMaterialLotCode())).build());
                    if (CollectionUtils.isEmpty(mtEoList)) {
                        throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
                    }
                    eoId = mtEoList.get(0).getEoId();
                }
                List<HmePumpModPositionHeader> headerList = hmeEoJobSnBatchMapper.queryPumpPositionHeaderByBackCodeAndEoId(tenantId, backMaterialLot.getMaterialLotId(), eoId);
                if (CollectionUtils.isNotEmpty(headerList)) {
                    hmePumpModPositionHeaderMapper.deleteByPrimaryKey(headerList.get(0).getPositionHeaderId());

                    List<HmePumpModPositionLine> positionLines = hmePumpModPositionLineMapper.select(new HmePumpModPositionLine() {{
                        setTenantId(tenantId);
                        setPositionHeaderId(headerList.get(0).getPositionHeaderId());
                    }});
                    if (CollectionUtils.isNotEmpty(positionLines)) {
                        List<String> positionLineIds = positionLines.stream().map(HmePumpModPositionLine::getPositionLineId).collect(Collectors.toList());
                        hmePumpModPositionLineMapper.myBatchDelete(tenantId, positionLineIds);
                    }
                }
            }

            //????????????????????????Id???????????????????????????????????????????????????????????????????????????????????????
            List<HmeEoJobPumpComb> hmeEoJobPumpCombList = hmeEoJobPumpCombRepository.select(new HmeEoJobPumpComb() {{
                setMaterialLotId(backMaterialLot.getMaterialLotId());
                setTenantId(tenantId);
            }});
            if(CollectionUtils.isNotEmpty(hmeEoJobPumpCombList)){
                HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombList.get(0);
                hmeEoJobPumpComb.setMaterialLotId(null);
                hmeEoJobPumpComb.setMaterialId(null);
                hmeEoJobPumpCombMapper.updateByPrimaryKey(hmeEoJobPumpComb);
            }
            //2021-09-07 17:18 add by chaonan.hu for wenxin.zhang ???????????????????????????????????????????????????????????????????????????
            List<String> selectionDetailsIdList = hmeEoJobPumpCombMapper.getSameSelectionOrderDetailsIdByMaterialLotId(tenantId, backMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(selectionDetailsIdList)){
                CustomUserDetails currentUser = DetailsHelper.getUserDetails();
                hmeEoJobPumpCombMapper.updatePumbSelectionDetailsStatus(tenantId, selectionDetailsIdList, userId, "RETURNED");
            }
        } else {
            //??????/????????????
            HmeEoJobSnLotMaterial existsHmeEoJobSnLotMaterial = hmeEoJobSnLotMaterialRepository.selectByPrimaryKey(dto.getJobMaterialId());

            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
            hmeEoJobSnLotMaterial.setTenantId(tenantId);
            hmeEoJobSnLotMaterial.setLotMaterialId(existsHmeEoJobSnLotMaterial.getLotMaterialId());
            hmeEoJobSnLotMaterial.setTimeMaterialId(existsHmeEoJobSnLotMaterial.getTimeMaterialId());
            hmeEoJobSnLotMaterial.setMaterialType(existsHmeEoJobSnLotMaterial.getMaterialType());
            hmeEoJobSnLotMaterial.setWorkcellId(existsHmeEoJobSnLotMaterial.getWorkcellId());
            hmeEoJobSnLotMaterial.setJobId(dto.getCurrentJobId());
            hmeEoJobSnLotMaterial.setMaterialId(existsHmeEoJobSnLotMaterial.getMaterialId());
            hmeEoJobSnLotMaterial.setMaterialLotId(backMaterialLot.getMaterialLotId());
            hmeEoJobSnLotMaterial.setReleaseQty(dto.getBackQty().multiply(new BigDecimal(-1)));
            hmeEoJobSnLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobSnLotMaterial.setLocatorId(existsHmeEoJobSnLotMaterial.getLocatorId());
            hmeEoJobSnLotMaterial.setLotCode(existsHmeEoJobSnLotMaterial.getLotCode());
            hmeEoJobSnLotMaterial.setProductionVersion(existsHmeEoJobSnLotMaterial.getProductionVersion());
            hmeEoJobSnLotMaterial.setVirtualFlag(existsHmeEoJobSnLotMaterial.getVirtualFlag());
            hmeEoJobSnLotMaterial.setParentMaterialLotId(existsHmeEoJobSnLotMaterial.getParentMaterialLotId());
            hmeEoJobSnLotMaterial.setRemainQty(existsHmeEoJobSnLotMaterial.getRemainQty());
            hmeEoJobSnLotMaterialRepository.insert(hmeEoJobSnLotMaterial);
            //?????????????????????release_qty???remain_qty
            existsHmeEoJobSnLotMaterial.setReleaseQty((Objects.isNull(existsHmeEoJobSnLotMaterial.getReleaseQty()) ? BigDecimal.ZERO : existsHmeEoJobSnLotMaterial.getReleaseQty()).subtract(dto.getBackQty()));
            if (Objects.nonNull(existsHmeEoJobSnLotMaterial.getRemainQty())) {
                existsHmeEoJobSnLotMaterial.setRemainQty(existsHmeEoJobSnLotMaterial.getRemainQty().subtract(dto.getBackQty()));
            }
            hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(existsHmeEoJobSnLotMaterial);
        }
        if (StringUtils.equals(dto.getEoId(), dto.getSiteInEoId())) {
            // eoId ????????? ??????eoStepId?????????operationId????????? ????????????
            if (!StringUtils.equals(dto.getEoStepId(), dto.getSiteInEoStepId()) || !StringUtils.equals(dto.getOperationId(), dto.getBackOperationId())) {
                throw new MtException("HME_SPLIT_RECORD_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SPLIT_RECORD_0030", "HME", mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : ""));
            }
            //EO????????????
            MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
            mtAssembleProcessActualVO5.setEoId(dto.getEoId());
            mtAssembleProcessActualVO5.setMaterialId(dto.getMaterialId());
            mtAssembleProcessActualVO5.setOperationId(dto.getOperationId());
            mtAssembleProcessActualVO5.setRouterStepId(dto.getEoStepId());
            mtAssembleProcessActualVO5.setLocatorId(locatorId);
            mtAssembleProcessActualVO5.setOperationBy(userId);
            mtAssembleProcessActualVO5.setWorkcellId(dto.getWorkcellId());
            mtAssembleProcessActualVO5.setParentEventId(wipReturnEventId);
            mtAssembleProcessActualVO5.setEventRequestId(wipReturnEventRequestId);
            if (StringUtils.isNotBlank(dto.getShiftId())) {
                mtAssembleProcessActualVO5.setShiftCode(dto.getShiftCode());
                mtAssembleProcessActualVO5.setShiftDate(dto.getShiftDate());
            }
            mtAssembleProcessActualVO5.setMaterialLotId(backMaterialLot.getMaterialLotId());
            mtAssembleProcessActualVO5.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
            mtAssembleProcessActualVO5.setTrxAssembleQty(currComponentReleaseBackQty.doubleValue());
            // ??????????????????API
            mtAssembleProcessActualRepository.componentAssembleCancelProcess(tenantId, mtAssembleProcessActualVO5);
        }

        //???????????????API
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
        //????????????API
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        HmeEoJobSnVO9 resultVO = dto;
        resultVO.setReleaseQty(resultVO.getReleaseQty().subtract(dto.getBackQty()));
        // 20210529 add by sanfeng.zhang for fang.pan ????????????Eo?????????
        resultVO.setAssembleQty(hmeEoJobSnReWorkMapper.queryAssembleQty(tenantId, dto.getSiteInEoId(), dto.getSiteInEoStepId(), dto.getMaterialId()));
        return resultVO;
    }

    /**
     * ????????????????????????-??????????????????
     *
     * @param tenantId ??????Id
     * @param dto ??????
     * @return HmeEoJobSnReworkVO
     */
    private HmeEoJobSnReworkVO2 releaseScanValidate(Long tenantId, HmeEoJobSnReworkDTO dto){
        HmeEoJobSnReworkVO2 resultVO = new HmeEoJobSnReworkVO2();
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            // ??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        HmeEoJobSnReworkVO3 mtMaterialLot = hmeEoJobSnReWorkMapper.selectMaterialLot(tenantId,dto.getMaterialLotCode());
        if(Objects.isNull(mtMaterialLot)){
            //?????????????????????
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
            //??????${1}?????????,?????????!
            throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0004", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
            // ????????????${1}?????????OK??????,????????????????????????
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())){
            // ?????????${1}????????????,??????????????????????????????!
            throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0005", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
            // ?????????${1}???????????????,??????????????????????????????!
            throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0006", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        //??????????????????
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());
        HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4 = new HmeEoJobSnBatchVO4();
        Boolean addComponentFlag = false;
        if (CollectionUtils.isNotEmpty(dto.getComponentList())) {
            List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> item.getMaterialId().equals(mtMaterialLot.getMaterialId())).collect(Collectors.toList());
            // ????????????bom??? ???????????????
            if (CollectionUtils.isNotEmpty(componentList)) {
                hmeEoJobSnBatchVO4 = componentList.get(0);
            } else {
                addComponentFlag = true;
            }
        } else {
            addComponentFlag = true;
        }
        resultVO.setMaterialType(materialType);

        if(HmeConstants.MaterialTypeCode.SN.equals(materialType)){
            List<HmeEoJobSnBatchVO9> materialLotCodeList = hmeEoJobMaterialMapper.selectMaterialLotBindMaterialLot2OfRework(tenantId,dto.getMaterialLotCode());
            if(CollectionUtils.isNotEmpty(materialLotCodeList)) {
                //???????????????????????????SN??????${1}???
                List<String> materialLotCodeList2 = materialLotCodeList.stream().map(HmeEoJobSnBatchVO9::getMaterialLotCode).collect(Collectors.toList());
                if (!materialLotCodeList2.contains(dto.getSnLineList().get(0).getSnNum())) {
                    throw new MtException("HME_EO_JOB_SN_076", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_076", "HME", String.join(",", materialLotCodeList2)));
                }
                List<HmeEoJobSnBatchVO9> existJobMaterialList2 = materialLotCodeList.stream().filter(item -> !item.getJobId().equals(dto.getSnLineList().get(0).getJobId()))
                        .collect(Collectors.toList());
                resultVO.setDeleteFlag(HmeConstants.ConstantValue.YES);
                String jobMaterialId = null;
                if (CollectionUtils.isNotEmpty(existJobMaterialList2)) {
                    jobMaterialId = existJobMaterialList2.get(0).getJobMaterialId();
                } else {
                    jobMaterialId = materialLotCodeList.get(0).getJobMaterialId();
                }
                resultVO.setDeleteFlag(HmeConstants.ConstantValue.YES);
                resultVO.setJobMaterialId(jobMaterialId);
            }
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(materialType)){
            List<HmeEoJobSnBatchVO10> workcellCodeList = hmeEoJobLotMaterialMapper.queryHaveBindWorkcell2(tenantId,mtMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(workcellCodeList)){
                List<HmeEoJobSnBatchVO10> existWorkcellList2 = workcellCodeList.stream().filter(item -> !item.getWorkcellId().equals(dto.getWorkcellId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(existWorkcellList2)){
                    //???????????????????????????${1}?????????
                    List<String> workcellCodeList2 = existWorkcellList2.stream().map(HmeEoJobSnBatchVO10::getWorkcellCode).collect(Collectors.toList());
                    throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_110", "HME",String.join(",", workcellCodeList2) ));
                }
                resultVO.setDeleteFlag(HmeConstants.ConstantValue.YES);
                resultVO.setJobMaterialId(workcellCodeList.get(0).getJobMaterialId());
            }else{
                HmeEoJobLotMaterial notBindJobLotMaterial = hmeEoJobLotMaterialMapper.selectNotBindJobMaterial(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
                if (Objects.nonNull(notBindJobLotMaterial)) {
                    resultVO.setJobMaterialId(notBindJobLotMaterial.getJobMaterialId());
                }
            }
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(materialType)){
            List<HmeEoJobSnBatchVO10> workcellCodeList = hmeEoJobTimeMaterialMapper.queryHaveBindWorkcell2(tenantId,mtMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(workcellCodeList)){
                List<HmeEoJobSnBatchVO10> existWorkcellList2 = workcellCodeList.stream().filter(item -> !item.getWorkcellId().equals(dto.getWorkcellId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(existWorkcellList2)){
                    //???????????????????????????${1}?????????
                    List<String> workcellCodeList2 = existWorkcellList2.stream().map(HmeEoJobSnBatchVO10::getWorkcellCode).collect(Collectors.toList());
                    throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_110", "HME",String.join(",", workcellCodeList2) ));
                }
                resultVO.setDeleteFlag(HmeConstants.ConstantValue.YES);
                resultVO.setJobMaterialId(workcellCodeList.get(0).getJobMaterialId());
            }else{
                HmeEoJobTimeMaterial notBindJobTimeMaterial = hmeEoJobTimeMaterialMapper.selectNotBindJobMaterial(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
                if (Objects.nonNull(notBindJobTimeMaterial)) {
                    resultVO.setJobMaterialId(notBindJobTimeMaterial.getJobMaterialId());
                }
            }
        }

        //????????????
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //??????????????????
        MtMaterialSite mtMaterialSitePara = new MtMaterialSite();
        mtMaterialSitePara.setTenantId(tenantId);
        mtMaterialSitePara.setSiteId(dto.getSiteId());
        mtMaterialSitePara.setMaterialId(mtMaterialLot.getMaterialId());
        mtMaterialSitePara.setEnableFlag(YES);
        MtMaterialSite mtMaterialSite = mtMaterialSiteRepository.selectOne(mtMaterialSitePara);
        if(Objects.isNull(mtMaterialSite)){
            //??????????????????${1}??????????????????????????????,?????????!${2}
            throw new MtException("MT_INVENTORY_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0029", "INVENTORY","",""));
        }

        //????????????????????????
        List<String> materialLotIdList = new ArrayList<>();
        materialLotIdList.add(mtMaterialLot.getMaterialLotId());
        List<String> materialLotAttrNameList = new ArrayList<>();
        materialLotAttrNameList.add("MATERIAL_VERSION");
        materialLotAttrNameList.add("SO_NUM");
        materialLotAttrNameList.add("SO_LINE_NUM");
        materialLotAttrNameList.add("MF_FLAG");
        materialLotAttrNameList.add("DEADLINE_DATE");
        materialLotAttrNameList.add("SUPPLIER_LOT");
        List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_material_lot_attr","MATERIAL_LOT_ID"
                ,materialLotIdList,materialLotAttrNameList);
        Map<String,String> materialLotExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)){
            materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName, MtExtendAttrVO1::getAttrValue));
        }

        //???????????????
        if(HmeConstants.ConstantValue.YES.equals(materialLotExtendAttrMap.getOrDefault("MF_FLAG",""))){
            //???????????????????????????,??????????????????,??????????????????
            throw new MtException("HME_EO_JOB_SN_117", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_117", "HME"));
        }
        if (addComponentFlag) {
            hmeEoJobSnBatchVO4 = new HmeEoJobSnBatchVO4();
            HmeEoJobSnReworkVO eoJobSnReworkVO = hmeEoJobSnReWorkMapper.selectMaterial(tenantId, mtMaterialLot.getMaterialId(), dto.getSiteId());
            hmeEoJobSnBatchVO4.setMaterialId(eoJobSnReworkVO.getMaterialId());
            hmeEoJobSnBatchVO4.setMaterialCode(eoJobSnReworkVO.getMaterialCode());
            hmeEoJobSnBatchVO4.setMaterialName(eoJobSnReworkVO.getMaterialName());
            hmeEoJobSnBatchVO4.setProductionType(materialType);
            hmeEoJobSnBatchVO4.setUpgradeFlag(eoJobSnReworkVO.getUpgradeFlag());
            hmeEoJobSnBatchVO4.setUomId(eoJobSnReworkVO.getUomId());
            hmeEoJobSnBatchVO4.setCurrBindSnNum(dto.getSnLineList().get(0).getSnNum());
            hmeEoJobSnBatchVO4.setUomCode(eoJobSnReworkVO.getUomCode());
            hmeEoJobSnBatchVO4.setUomName(eoJobSnReworkVO.getUomName());
            hmeEoJobSnBatchVO4.setWillReleaseQty(eoJobSnReworkVO.getWillReleaseQty());
            hmeEoJobSnBatchVO4.setIsReleased(eoJobSnReworkVO.getIsReleased());
            hmeEoJobSnBatchVO4.setReleasedQty(BigDecimal.ZERO);

            //????????????
            HmeEoJobSnReworkVO4 hmeEoJobSnReworkVO4 = new HmeEoJobSnReworkVO4();
            hmeEoJobSnReworkVO4.setSnLine(dto.getSnLineList().get(0));
            Map<String, MtEoComponentActual> eoComponentActualMap = eoComponentActualQuery(tenantId,hmeEoJobSnReworkVO4, hmeEoJobSnBatchVO4.getMaterialId());
            MtEoComponentActual mtEoComponentActual = eoComponentActualMap.getOrDefault(fetchGroupKey3(dto.getSnLineList().get(0).getEoId(), dto.getSnLineList().get(0).getEoStepId(), hmeEoJobSnBatchVO4.getMaterialId()),null);
            if(Objects.nonNull(mtEoComponentActual)){
                hmeEoJobSnBatchVO4.setReleasedQty(BigDecimal.valueOf(mtEoComponentActual.getAssembleQty()));
            }
            List<HmeEoJobSnBatchVO6> materialLotList = new ArrayList<>();
            if (StringUtils.isNotBlank(resultVO.getJobMaterialId())) {
                HmeEoJobSnBatchVO6 materialLot = new HmeEoJobSnBatchVO6();
                materialLot.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                materialLot.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                materialLot.setMaterialType(materialType);
                materialLot.setLocatorId(mtMaterialLot.getLocatorId());
                materialLot.setLocatorCode(mtMaterialLot.getLocatorCode());
                materialLot.setLot(mtMaterialLot.getLot());
                materialLot.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty());
                materialLot.setCreationDate(CommonUtils.currentTimeGet());
                materialLot.setSupplierLot(materialLotExtendAttrMap.getOrDefault("SUPPLIER_LOT",""));
                materialLot.setEnableFlag(mtMaterialLot.getEnableFlag());
                if(HmeConstants.MaterialTypeCode.TIME.equals(materialType)){
                    materialLot.setDeadLineDate(materialLotExtendAttrMap.getOrDefault("DEADLINE_DATE",""));
                }
                materialLot.setJobMaterialId(resultVO.getJobMaterialId());
                materialLotList.add(materialLot);
            }
            hmeEoJobSnBatchVO4.setMaterialLotList(materialLotList);
        }

        resultVO.setComponent(hmeEoJobSnBatchVO4);

        resultVO.setMaterialLot(mtMaterialLot);
        resultVO.setMaterialType(materialType);
        resultVO.setMaterialLotExtendAttrMap(materialLotExtendAttrMap);
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnBatchVO14 releaseScan(Long tenantId, HmeEoJobSnReworkDTO dto) {
        HmeEoJobSnBatchVO14 resultVO = new HmeEoJobSnBatchVO14();
        //??????
        HmeEoJobSnReworkVO2 releaseScanValidateResultVO = releaseScanValidate(tenantId,dto);
        HmeEoJobSnReworkVO3 mtMaterialLot = releaseScanValidateResultVO.getMaterialLot();

        resultVO.setComponent(releaseScanValidateResultVO.getComponent());
        if(YES.equals(releaseScanValidateResultVO.getDeleteFlag())){
            //2021-11-09 14:20 edit by chaonan.hu for wenxin.zhang ???????????????ID?????????hme_eo_job_pump_comb????????????
            HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombMapper.eoJobPumpCombQueryByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            if(Objects.nonNull(hmeEoJobPumpComb)){
                //????????????????????????????????????snLineList???materialLotId????????????????????????CombMaterialLotId?????????
                if(CollectionUtils.isNotEmpty(dto.getSnLineList())
                        && !hmeEoJobPumpComb.getCombMaterialLotId().equals(dto.getSnLineList().get(0).getMaterialLotId())){
                    //???????????????????????????SN??????${1}???
                    MtMaterialLot combMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobPumpComb.getCombMaterialLotId());
                    throw new MtException("HME_EO_JOB_SN_076", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_076", "HME", combMaterialLot.getMaterialLotCode()));
                }
            }

            resultVO.setDeleteFlag(releaseScanValidateResultVO.getDeleteFlag());
            List<HmeEoJobSnBatchVO6> deleteMaterialLotList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())) {
                deleteMaterialLotList = resultVO.getComponent().getMaterialLotList().stream().filter(item -> item.getJobMaterialId().equals(releaseScanValidateResultVO.getJobMaterialId()))
                        .collect(Collectors.toList());
            }
            // 20210831 add by sanfeng.zhang for hui.gu ??????SN ???????????????????????? ????????????????????????SN
            List<HmePumpCombDTO> pumpCombVOList = hmeEoJobSnReWorkMapper.queryPumpCombListByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            Double powerValue = null;
            Double voltageValue = null;
            if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                List<HmePumpCombDTO2> hmePumpTagList = hmeEoJobSnReWorkMapper.queryPumpTagRecordResult(tenantId, pumpCombVOList.stream().map(HmePumpCombDTO::getJobId).collect(Collectors.toList()));
                List<HmePumpCombDTO2> powerList = hmePumpTagList.stream().filter(vo -> StringUtils.equals(vo.getTagCode(), "B05-BPYZH-P")).collect(Collectors.toList());
                List<HmePumpCombDTO2> voltageList = hmePumpTagList.stream().filter(vo -> StringUtils.equals(vo.getTagCode(), "B05-BPYZH-V")).collect(Collectors.toList());
                powerValue = powerList.stream().map(HmePumpCombDTO2::getResult).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                voltageValue = voltageList.stream().map(HmePumpCombDTO2::getResult).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
            }
            if(CollectionUtils.isNotEmpty(deleteMaterialLotList)) {
                for (HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 : deleteMaterialLotList) {
                    hmeEoJobSnBatchVO6.setDeleteFlag(resultVO.getDeleteFlag());
                    if (voltageValue != null) {
                        hmeEoJobSnBatchVO6.setVoltageValue(BigDecimal.valueOf(voltageValue));
                    }
                    if (powerValue != null) {
                        hmeEoJobSnBatchVO6.setPowerValue(BigDecimal.valueOf(powerValue));
                    }
                }
            }else{
                //??????????????????????????????????????????SN
                HmeEoJobSnBatchVO6 deleteMaterialLot = new HmeEoJobSnBatchVO6();
                deleteMaterialLot.setJobMaterialId(releaseScanValidateResultVO.getJobMaterialId());
                deleteMaterialLot.setMaterialType(releaseScanValidateResultVO.getMaterialType());
                deleteMaterialLot.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                deleteMaterialLot.setDeleteFlag(releaseScanValidateResultVO.getDeleteFlag());
                if (voltageValue != null) {
                    deleteMaterialLot.setVoltageValue(BigDecimal.valueOf(voltageValue));
                }
                if (powerValue != null) {
                    deleteMaterialLot.setPowerValue(BigDecimal.valueOf(powerValue));
                }
                deleteMaterialLotList.add(deleteMaterialLot);
                resultVO.getComponent().setMaterialLotList(deleteMaterialLotList);
            }
            return resultVO;
        }

        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
        if(Objects.isNull(mtModLocator)){
            //????????????????????????
            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0012",WmsConstant.ConstantValue.WMS, mtMaterialLot.getLocatorId()));
        }

        //????????????
        HmeEoJobSnBatchVO6 singleMaterialLot = new HmeEoJobSnBatchVO6();
        singleMaterialLot.setMaterialType(releaseScanValidateResultVO.getMaterialType());
        singleMaterialLot.setWorkCellId(dto.getWorkcellId());
        singleMaterialLot.setMaterialId(mtMaterialLot.getMaterialId());
        singleMaterialLot.setIsReleased(HmeConstants.ConstantValue.ONE);
        singleMaterialLot.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        singleMaterialLot.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        singleMaterialLot.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty());
        singleMaterialLot.setDeleteFlag(HmeConstants.ConstantValue.NO);
        singleMaterialLot.setLocatorId(mtMaterialLot.getLocatorId());
        singleMaterialLot.setLocatorCode(mtModLocator.getLocatorCode());
        singleMaterialLot.setLot(mtMaterialLot.getLot());
        singleMaterialLot.setSiteId(dto.getSiteId());
        singleMaterialLot.setEnableFlag(mtMaterialLot.getEnableFlag());
        singleMaterialLot.setFreezeFlag(mtMaterialLot.getFreezeFlag());
        singleMaterialLot.setStocktakeFlag(mtMaterialLot.getStocktakeFlag());
        List<HmeEoJobSnBatchVO6> materialLotList = new ArrayList<>();
        boolean insertFlag = false;

        if(HmeConstants.MaterialTypeCode.TIME.equals(releaseScanValidateResultVO.getMaterialType())){
            HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
            String availableTime = hmeEoJobSnCommonService.getAvailableTime(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());
            String deadLineDate = hmeEoJobSnCommonService.getDeadLineDate(tenantId,availableTime,mtMaterialLot.getMaterialLotId());
            if(StringUtils.isBlank(releaseScanValidateResultVO.getJobMaterialId())) {
                //??????
                hmeEoJobTimeMaterial.setReleaseQty(releaseScanValidateResultVO.getComponent().getReleasedQty());
                hmeEoJobTimeMaterial.setTenantId(tenantId);
                hmeEoJobTimeMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobTimeMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobTimeMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setProductionVersion(releaseScanValidateResultVO.getMaterialLotExtendAttrMap().getOrDefault("MATERIAL_VERSION",""));
                hmeEoJobTimeMaterial.setWorkDate(new Date(System.currentTimeMillis()));
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setDeadLineDate(deadLineDate);
                String virtualFlag = getVirtualFlag(tenantId, dto.getSnLineList().get(0).getEoId(), dto.getOperationId(), mtMaterialLot.getMaterialId());
                if(StringUtils.isNotBlank(virtualFlag)){
                    hmeEoJobTimeMaterial.setVirtualFlag(virtualFlag);
                }
                hmeEoJobTimeMaterialRepository.insertSelective(hmeEoJobTimeMaterial);
                releaseScanValidateResultVO.setJobMaterialId(hmeEoJobTimeMaterial.getJobMaterialId());
                singleMaterialLot.setJobMaterialId(hmeEoJobTimeMaterial.getJobMaterialId());
                singleMaterialLot.setCreationDate(CommonUtils.currentTimeGet());
                singleMaterialLot.setDeadLineDate(deadLineDate);
                insertFlag = true;
            }else{
                //??????
                hmeEoJobTimeMaterial.setJobMaterialId(releaseScanValidateResultVO.getJobMaterialId());
                hmeEoJobTimeMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobTimeMaterial.setReleaseQty(releaseScanValidateResultVO.getComponent().getReleasedQty());
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setProductionVersion(releaseScanValidateResultVO.getMaterialLotExtendAttrMap().getOrDefault("MATERIAL_VERSION",""));
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setDeadLineDate(deadLineDate);
                String virtualFlag = getVirtualFlag(tenantId, dto.getSnLineList().get(0).getEoId(), dto.getOperationId(), mtMaterialLot.getMaterialId());
                if(StringUtils.isNotBlank(virtualFlag)){
                    hmeEoJobTimeMaterial.setVirtualFlag(virtualFlag);
                }
                hmeEoJobTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(hmeEoJobTimeMaterial);
                insertFlag = false;
            }
        } if(HmeConstants.MaterialTypeCode.LOT.equals(releaseScanValidateResultVO.getMaterialType())){
            HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
            if(StringUtils.isBlank(releaseScanValidateResultVO.getJobMaterialId())) {
                //??????
                hmeEoJobLotMaterial.setReleaseQty(releaseScanValidateResultVO.getComponent().getReleasedQty());
                hmeEoJobLotMaterial.setTenantId(tenantId);
                hmeEoJobLotMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobLotMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobLotMaterial.setSnMaterialId(dto.getSnLineList().get(0).getSnMaterialId());
                hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
                String virtualFlag = getVirtualFlag(tenantId, dto.getSnLineList().get(0).getEoId(), dto.getOperationId(), mtMaterialLot.getMaterialId());
                if(StringUtils.isNotBlank(virtualFlag)){
                    hmeEoJobLotMaterial.setVirtualFlag(virtualFlag);
                }
                hmeEoJobLotMaterial.setProductionVersion(releaseScanValidateResultVO.getMaterialLotExtendAttrMap().getOrDefault("MATERIAL_VERSION",""));
                hmeEoJobLotMaterialRepository.insertSelective(hmeEoJobLotMaterial);
                singleMaterialLot.setJobMaterialId(hmeEoJobLotMaterial.getJobMaterialId());
                releaseScanValidateResultVO.setJobMaterialId(hmeEoJobLotMaterial.getJobMaterialId());
            }else{
                //??????
                hmeEoJobLotMaterial.setJobMaterialId(releaseScanValidateResultVO.getJobMaterialId());
                hmeEoJobLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobLotMaterial.setReleaseQty(releaseScanValidateResultVO.getComponent().getReleasedQty());
                hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobLotMaterial.setProductionVersion(releaseScanValidateResultVO.getMaterialLotExtendAttrMap().getOrDefault("MATERIAL_VERSION",""));
                String virtualFlag = getVirtualFlag(tenantId, dto.getSnLineList().get(0).getEoId(), dto.getOperationId(), mtMaterialLot.getMaterialId());
                if(StringUtils.isNotBlank(virtualFlag)){
                    hmeEoJobLotMaterial.setVirtualFlag(virtualFlag);
                }
                hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterial);
            }
        } else if (SN.equals(releaseScanValidateResultVO.getMaterialType())) {
            if(StringUtils.isBlank(releaseScanValidateResultVO.getJobMaterialId())){
                //??????
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setTenantId(tenantId);
                hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobMaterial.setSnMaterialId(dto.getSnLineList().get(0).getSnMaterialId());
                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobMaterial.setReleaseQty(BigDecimal.ONE);
                hmeEoJobMaterial.setJobId(dto.getSnLineList().get(0).getJobId());
                hmeEoJobMaterial.setEoId(dto.getSnLineList().get(0).getEoId());
                hmeEoJobMaterial.setProductionVersion(releaseScanValidateResultVO.getMaterialLotExtendAttrMap().getOrDefault("MATERIAL_VERSION",""));
                hmeEoJobMaterialRepository.insertSelective(hmeEoJobMaterial);
                releaseScanValidateResultVO.setJobMaterialId(hmeEoJobMaterial.getJobMaterialId());
                singleMaterialLot.setCreationDate(CommonUtils.currentTimeGet());
                singleMaterialLot.setJobMaterialId(hmeEoJobMaterial.getJobMaterialId());
                singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                insertFlag = true;
            }else{
                //??????
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setJobMaterialId(releaseScanValidateResultVO.getJobMaterialId());
                hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobMaterial.setProductionVersion(releaseScanValidateResultVO.getMaterialLotExtendAttrMap().getOrDefault("MATERIAL_VERSION",""));
                hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial);
                insertFlag = false;
            }
            MtMaterialLot mtMaterialLotObj = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLot.getMaterialLotId());
            // 20210831 add by sanfeng.zhang for hui.gu ??????SN ???????????????????????? ????????????????????????SN
            List<HmePumpCombDTO> pumpCombVOList = hmeEoJobSnReWorkMapper.queryPumpCombListByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                List<HmePumpCombDTO2> hmePumpTagList = hmeEoJobSnReWorkMapper.queryPumpTagRecordResult(tenantId, pumpCombVOList.stream().map(HmePumpCombDTO::getJobId).collect(Collectors.toList()));
                List<HmePumpCombDTO2> powerList = hmePumpTagList.stream().filter(vo -> StringUtils.equals(vo.getTagCode(), "B05-BPYZH-P")).collect(Collectors.toList());
                List<HmePumpCombDTO2> voltageList = hmePumpTagList.stream().filter(vo -> StringUtils.equals(vo.getTagCode(), "B05-BPYZH-V")).collect(Collectors.toList());
                Double powerValue = powerList.stream().map(HmePumpCombDTO2::getResult).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                Double voltageValue = voltageList.stream().map(HmePumpCombDTO2::getResult).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                singleMaterialLot.setPowerValue(powerValue != null ? BigDecimal.valueOf(powerValue) : null);
                singleMaterialLot.setVoltageValue(voltageValue != null ? BigDecimal.valueOf(voltageValue) : null);
            }
            //??????????????????????????????????????????
            HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8 = new HmeEoJobSnBatchVO8();
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getSnLineList().get(0).getJobId());
            hmeEoJobSnBatchVO8.setHmeEoJobSn(hmeEoJobSn);
            //?????????????????????????????????????????????????????????????????????
            HmeEoJobPumpCombVO2 hmeEoJobPumpCombVO2 = hmeEoJobPumpCombMapper.pumpFilterRuleHeaderByMaterial(tenantId, hmeEoJobSn.getMaterialLotId());
            List<HmePumpCombDTO> hmePumpCombDTOList = hmeEoJobSnReWorkMapper.queryPumpCombListByMaterialLotId(tenantId, hmeEoJobSn.getMaterialLotId());
            if(hmeEoJobPumpCombVO2 != null && CollectionUtils.isNotEmpty(hmePumpCombDTOList)){
                hmeEoJobSnBatchVO8.setMaterialId(hmeEoJobPumpCombVO2.getMaterialId());
                hmeEoJobSnBatchVO8.setQty(hmeEoJobPumpCombVO2.getQty().longValue());
                hmeEoJobSnBatchVO8.setHmeEoJobSn(hmeEoJobSn);
                hmeEoJobSnBatchVO8.setMtMaterialLot(mtMaterialLotObj);
                hmeEoJobSnBatchVO8.setMaterialType(releaseScanValidateResultVO.getMaterialType());
                hmeEoJobSnBatchVO8.setIsRepairProcess(YES);
                HmeEoJobSnBatchVO14 hmeEoJobSnBatchVO14 = hmeEoJobPumpCombRepository.releaseScan(tenantId, hmeEoJobSnBatchVO8);
                resultVO.setPrintFlag(hmeEoJobSnBatchVO14.getPrintFlag());
                resultVO.setSubCode(hmeEoJobSnBatchVO14.getSubCode());
            }
        }

        if(insertFlag){
            if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())){
                resultVO.getComponent().getMaterialLotList().add(singleMaterialLot);
            }else {
                materialLotList.add(singleMaterialLot);
                resultVO.getComponent().setMaterialLotList(materialLotList);
            }
        }else{
            if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())){
                materialLotList = resultVO.getComponent().getMaterialLotList().stream().filter(item -> item.getJobMaterialId().equals(releaseScanValidateResultVO.getJobMaterialId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(materialLotList)){
                    materialLotList.get(0).setIsReleased(HmeConstants.ConstantValue.ONE);
                }else{
                    singleMaterialLot.setJobMaterialId(releaseScanValidateResultVO.getJobMaterialId());
                    if(HmeConstants.MaterialTypeCode.SN.equals(releaseScanValidateResultVO.getMaterialType())) {
                        singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                    }
                    resultVO.getComponent().getMaterialLotList().add(singleMaterialLot);
                }
            }else{
                singleMaterialLot.setJobMaterialId(releaseScanValidateResultVO.getJobMaterialId());
                if(HmeConstants.MaterialTypeCode.SN.equals(releaseScanValidateResultVO.getMaterialType())) {
                    singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                }
                materialLotList.add(singleMaterialLot);
                resultVO.getComponent().setMaterialLotList(materialLotList);
            }
        }
        //????????????
        if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())) {
            List<HmeEoJobSnBatchVO6> materialLotList2 = resultVO.getComponent().getMaterialLotList().stream()
                    .sorted(Comparator.comparing(HmeEoJobSnBatchVO6::getCreationDate)).collect(Collectors.toList());
            resultVO.getComponent().setMaterialLotList(materialLotList2);
            materialLotList2.forEach(item -> item.setLineNumber(resultVO.getComponent().getLineNumber()));
        }
        resultVO.setDeleteFlag(HmeConstants.ConstantValue.NO);
        return resultVO;
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
        boolean dataRecordValidateFlag = true;
        if (dto.getOutSiteAction() != null) {
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
                        // 20211109 modify by sanfeng.zhang for peng.zhao ?????????????????????????????????
                        if (!hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId()) && !hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId())) {
                            // ?????????????????????????????????????????????????????????????????????????????????
                            HmeEoJobSn resultJobSn = new HmeEoJobSn();
                            resultJobSn.setErrorCode("HME_EO_JOB_SN_038");
                            resultJobSn.setErrorMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_038", "HME"));
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
        if (dataRecordValidateFlag
                && hmeEoJobSnCommonService.hasMissingValue(tenantId, dto.getWorkcellId(), Collections.singletonList(dto.getJobId()))) {
            // ????????????,??????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_009", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_009", "HME"));
        }
        // ?????????????????????????????????
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

        //????????????????????????
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            outSiteContainerLoadVerify(tenantId, hmeEoJobSnSingleBasic, dto);
        }
        // 20211125 add by  sanfeng.zhang for wenxin.zhang ????????????????????????????????????????????????
        boolean newMaterialLot = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        String oldMaterialLotCode = "";
        if (!newMaterialLot) {
            oldMaterialLotCode = dto.getSnNum();
        } else {
            // ?????????????????????????????????????????????????????????EO
            String eoId = dto.getEoId();
            List<MtExtendAttrVO> reworkMaterialLotList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(eoId);
                setAttrName("REWORK_MATERIAL_LOT");
                setTableName("mt_eo_attr");
            }});
            oldMaterialLotCode = reworkMaterialLotList.get(0).getAttrValue();
        }
        if(hmeEoJobSnCommonService.isBindMoreWorkingEo(tenantId, oldMaterialLotCode)) {
            throw new MtException("HME_EO_JOB_REWORK_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0003", "HME"));
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

        //????????????????????????,?????????????????????????????????
        if (Objects.nonNull(containerInfo.getCapacityQty())) {
            //????????????????????????
            double totalQty = containerDetailList.stream().filter(item -> Objects.nonNull(item.getPrimaryUomQty()))
                    .mapToDouble(HmeContainerDetailVO::getPrimaryUomQty).sum();

            //???????????????EO??????
            totalQty += hmeEoJobSnSingleBasic.getEo().getQty();

            if (totalQty > containerInfo.getCapacityQty()) {
                //??????????????????
                throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //????????????????????????????????????
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedMaterialFlag())) {
            //?????????????????????
            String materialId = hmeEoJobSnSingleBasic.getMaterialLot().getMaterialId();

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
            String eoId = hmeEoJobSnSingleBasic.getEo().getEoId();

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

        //??????EO???WO????????????
        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic = hmeEoJobSnCommonService.queryEoAndWoInfoWithComponentByEoId(tenantId, hmeEoJobSnEntity.getEoStepId(), dto.getEoId());
        hmeEoJobSnSingleBasic.setHmeEoJobSnEntity(hmeEoJobSnEntity);

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

        //????????????????????????
        HmeMaterialLotVO3 materialLot = hmeEoJobSnCommonService.queryMaterialLotInfo(tenantId, dto.getMaterialLotId());
        hmeEoJobSnSingleBasic.setMaterialLot(materialLot);

        //????????????????????????????????????
        if (!HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag()) && CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getEo().getBomComponentList())) {
            List<String> bomComponentIdList = hmeEoJobSnSingleBasic.getEo().getBomComponentList().stream()
                    .map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());
            List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnLotMaterialMapper.selectBackFlash(tenantId, dto.getSiteId(), bomComponentIdList);
            hmeEoJobSnSingleBasic.setBackFlushBomComponentList(backFlushBomComponentList);
        }

        //?????????????????????
        HmeRouterStepVO nearStepVO = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, dto.getEoId());
        if (Objects.isNull(nearStepVO)) {
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
                && !HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            throw new MtException("HME_EO_JOB_SN_149", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_149", "HME"));
        }
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     *
     * @Description ???????????????
     *
     * @author penglin.sui
     * @date 2020/12/16 11:00
     * @param tenantId ??????ID
     * @param releaseData ?????????????????????
     * @return List<HmeEoJobSnBatchVO4>
     *
     */
    private HmeEoJobSnSingleVO4 normalRelease(Long tenantId, HmeEoJobSnReworkVO5 releaseData){
        HmeEoJobSnSingleVO4 resultVO = new HmeEoJobSnSingleVO4();
        resultVO.setExecReleaseFlag(false);
        if(CollectionUtils.isEmpty(releaseData.getDto().getComponentList())){
            return resultVO;
        }
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList();
        Map<String, MtMaterialLotVO1> materialLotConsumeMap = new HashMap<>();
        List<HmeEoJobSnLotMaterial> eoJobSnInsertDataList = new ArrayList<>();
        List<MtAssembleProcessActualVO11> materialInfoList = new ArrayList<>();
//        List<HmeEoJobMaterial> eoJobMaterialInsertDataList = new ArrayList<>();
        List<HmeEoJobMaterial> updateSnDataList = new ArrayList<>();
        List<HmeEoJobLotMaterial> deleteLotDataList = new ArrayList<>();
        List<HmeEoJobTimeMaterial> deleteTimeDataList = new ArrayList<>();
        //??????????????????
        Boolean execReleaseFlag = false;

        HmeEoJobSnVO snLine = releaseData.getDto().getSnLine();

        // ????????????????????????
        String workOrderId = snLine.getWorkOrderId();
        if (StringUtils.isBlank(workOrderId)){
            //?????????????????????,?????????!
            throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0002", "HME"));
        }
        HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, snLine.getWorkOrderNum());
        String transactionCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
        String trxWorkOrderNum = snLine.getWorkOrderNum();
        String trxInsideOrder = null;
        // ????????????????????????????????????????????????
        if (!Objects.isNull(splitRecord)){
            //?????????????????????????????????
            if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())){
                transactionCode = HmeConstants.TransactionTypeCode.WMS_INSDID_ORDER_S_I;
                trxInsideOrder = splitRecord.getInternalOrderNum();
                trxWorkOrderNum = "";
            }
        }

        for (HmeEoJobSnReworkVO component : releaseData.getDto().getComponentList()
        ) {
            //????????????????????????????????????
            List<HmeEoJobSnReworkVO3> materialLotList = component.getMaterialLotList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                    && HmeConstants.ConstantValue.YES.equals(item.getEnableFlag())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(materialLotList)) {
                continue;
            }
            // ?????????????????????
            BigDecimal sumPrimaryUomQty = materialLotList.stream().map(HmeEoJobSnReworkVO3::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            //????????????????????? = ????????????????????????
            BigDecimal currentReleaseQty = component.getWillReleaseQty();
            if (currentReleaseQty == null || currentReleaseQty.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            //?????????????????????
            BigDecimal remainReleaseQty = currentReleaseQty;
            int loopCount = 0;
            // ??????????????????
            if (remainReleaseQty.compareTo(sumPrimaryUomQty) > 0){
                //?????????${1}??????????????????${2}?????????????????????????????????????????????${3}???,?????????!
                throw new MtException("HME_EO_JOB_REWORK_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_REWORK_0001", "HME", component.getMaterialCode(), remainReleaseQty.toString(), sumPrimaryUomQty.toString()));
            }

            for(int i = 0 ; i < materialLotList.size(); i++)
            {
                if(remainReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                    continue;
                }
                HmeEoJobSnReworkVO3 componentMaterialLot = materialLotList.get(i);
                BigDecimal materialLotCurrentReleaseQty = componentMaterialLot.getPrimaryUomQty().compareTo(remainReleaseQty) > 0 ? remainReleaseQty : componentMaterialLot.getPrimaryUomQty();

                if (remainReleaseQty.compareTo(materialLotCurrentReleaseQty) < 0) {
                    materialLotCurrentReleaseQty = remainReleaseQty;
                }
                // ?????????????????????
                remainReleaseQty = remainReleaseQty.subtract(materialLotCurrentReleaseQty);

                MtAssembleProcessActualVO11 mtAssembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                mtAssembleProcessActualVO11.setMaterialId(component.getMaterialId());
                mtAssembleProcessActualVO11.setAssembleExcessFlag(YES);
                mtAssembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                mtAssembleProcessActualVO11.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                mtAssembleProcessActualVO11.setTrxAssembleQty(materialLotCurrentReleaseQty.doubleValue());
                materialInfoList.add(mtAssembleProcessActualVO11);

                //??????????????????EO??????????????????
                WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                objectTransactionVO.setTransactionTypeCode(transactionCode);
                objectTransactionVO.setEventId(releaseData.getEventId());
                objectTransactionVO.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                objectTransactionVO.setMaterialId(component.getMaterialId());
                objectTransactionVO.setMaterialCode(component.getMaterialCode());
                objectTransactionVO.setTransactionQty(materialLotCurrentReleaseQty);
                objectTransactionVO.setLotNumber(componentMaterialLot.getLot());
                objectTransactionVO.setTransactionUom(component.getUomCode());
                objectTransactionVO.setTransactionTime(CommonUtils.currentTimeGet());
                objectTransactionVO.setPlantId(snLine.getSiteId());
                objectTransactionVO.setPlantCode(snLine.getSiteCode());
                objectTransactionVO.setLocatorId(componentMaterialLot.getLocatorId());
                objectTransactionVO.setLocatorCode(componentMaterialLot.getLocatorCode());
                HmeModLocatorVO2 hmeModLocatorVO2 = releaseData.getAreaLocatorMap().get(componentMaterialLot.getLocatorId());
                objectTransactionVO.setWarehouseId(hmeModLocatorVO2.getLocatorId());
                objectTransactionVO.setWarehouseCode(hmeModLocatorVO2.getLocatorCode());
                if (StringUtils.isBlank(trxInsideOrder) && StringUtils.isBlank(trxWorkOrderNum)) {
                    //??????${1}?????????,?????????!
                    throw new MtException("HME_COS_CHIP_IMP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_CHIP_IMP_0020", "HME", trxWorkOrderNum));
                }
                objectTransactionVO.setWorkOrderNum(trxWorkOrderNum);
                objectTransactionVO.setInsideOrder(trxInsideOrder);
                objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                objectTransactionVO.setProdLineCode(snLine.getProdLineCode());
                objectTransactionVO.setMoveType(releaseData.getWmsTransactionTypeMap().get(transactionCode).getMoveType());
                objectTransactionVO.setMoveReason("???????????????");
                objectTransactionVO.setRemark("???????????????");
                objectTransactionVO.setSoNum(releaseData.getMaterialLotExtendAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.SO_NUM), ""));
                objectTransactionVO.setSoLineNum(releaseData.getMaterialLotExtendAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.SO_LINE_NUM), ""));
                objectTransactionRequestList.add(objectTransactionVO);

                //??????????????????????????????
                MtMaterialLotVO1 mtMaterialLotVO1 = materialLotConsumeMap.getOrDefault(componentMaterialLot.getMaterialLotId(),null);
                if(Objects.nonNull(mtMaterialLotVO1)){
                    mtMaterialLotVO1.setTrxPrimaryUomQty((BigDecimal.valueOf(mtMaterialLotVO1.getTrxPrimaryUomQty()).add(materialLotCurrentReleaseQty)).doubleValue());
                }else{
                    mtMaterialLotVO1 = new MtMaterialLotVO1();
                    mtMaterialLotVO1.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                    mtMaterialLotVO1.setPrimaryUomId(component.getUomId());
                    mtMaterialLotVO1.setTrxPrimaryUomQty(materialLotCurrentReleaseQty.doubleValue());
                    mtMaterialLotVO1.setEventRequestId(releaseData.getEventRequestId());
                    if (StringUtils.isNotEmpty(component.getSecondaryUomId())) {
                        mtMaterialLotVO1.setSecondaryUomId(component.getSecondaryUomId());
                        mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
                    }
                    materialLotConsumeMap.put(componentMaterialLot.getMaterialLotId(),mtMaterialLotVO1);
                }

                if (componentMaterialLot.getPrimaryUomQty().compareTo(materialLotCurrentReleaseQty) <= 0) {
                    componentMaterialLot.setEnableFlag(HmeConstants.ConstantValue.NO);
                    if (HmeConstants.MaterialTypeCode.SN.equals(component.getProductionType())) {
                        HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                        hmeEoJobMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                        hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                        hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ONE);
                        updateSnDataList.add(hmeEoJobMaterial);
                    } else if (HmeConstants.MaterialTypeCode.LOT.equals(component.getProductionType())) {
                        HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
                        hmeEoJobLotMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                        deleteLotDataList.add(hmeEoJobLotMaterial);
                    } else if (HmeConstants.MaterialTypeCode.TIME.equals(component.getProductionType())) {
                        HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
                        hmeEoJobTimeMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                        deleteTimeDataList.add(hmeEoJobTimeMaterial);
                    }
                }

                //????????????/??????????????????
                if (HmeConstants.MaterialTypeCode.LOT.equals(component.getProductionType()) ||
                        HmeConstants.MaterialTypeCode.TIME.equals(component.getProductionType())) {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setTenantId(tenantId);
                    if (HmeConstants.MaterialTypeCode.LOT.equals(component.getProductionType())) {
                        hmeEoJobSnLotMaterial.setLotMaterialId(componentMaterialLot.getJobMaterialId());
                    } else if (HmeConstants.MaterialTypeCode.TIME.equals(component.getProductionType())) {
                        hmeEoJobSnLotMaterial.setTimeMaterialId(componentMaterialLot.getJobMaterialId());
                    }
                    hmeEoJobSnLotMaterial.setMaterialType(component.getProductionType());
                    hmeEoJobSnLotMaterial.setWorkcellId(snLine.getWorkcellId());
                    hmeEoJobSnLotMaterial.setMaterialId(component.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setReleaseQty(materialLotCurrentReleaseQty);
                    hmeEoJobSnLotMaterial.setIsReleased(componentMaterialLot.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(snLine.getJobId());
                    hmeEoJobSnLotMaterial.setLocatorId(componentMaterialLot.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(componentMaterialLot.getLot());
                    hmeEoJobSnLotMaterial.setProductionVersion(releaseData.getMaterialLotExtendAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.MATERIAL_VERSION), ""));
                    eoJobSnInsertDataList.add(hmeEoJobSnLotMaterial);
                }else if(HmeConstants.MaterialTypeCode.SN.equals(component.getProductionType())){
                    //eoJobMaterialInsertDataList;
                    HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                    hmeEoJobMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                    hmeEoJobMaterial.setReleaseQty(materialLotCurrentReleaseQty);
                    hmeEoJobMaterial.setIsReleased(componentMaterialLot.getIsReleased());
                    hmeEoJobMaterial.setIsIssued(ONE);
                    updateSnDataList.add(hmeEoJobMaterial);
                }

                //????????????,???????????????
                componentMaterialLot.setPrimaryUomQty(componentMaterialLot.getPrimaryUomQty().subtract(materialLotCurrentReleaseQty));
                component.setReleasedQty(component.getReleasedQty().add(materialLotCurrentReleaseQty));
                component.setWillReleaseQty(BigDecimal.ZERO);

                execReleaseFlag = true;
                if (remainReleaseQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }
        }
        if(execReleaseFlag){
            resultVO.setExecReleaseFlag(execReleaseFlag);
            resultVO.setObjectTransactionRequestList(objectTransactionRequestList);
            resultVO.setEoJobSnInsertDataList(eoJobSnInsertDataList);
            resultVO.setUpdateSnDataList(updateSnDataList);
            resultVO.setDeleteLotDataList(deleteLotDataList);
            resultVO.setDeleteTimeDataList(deleteTimeDataList);
            resultVO.setMaterialLotConsumeMap(materialLotConsumeMap);
            resultVO.setMaterialInfoList(materialInfoList);
        }

        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO9 releaseRecordScrap(Long tenantId, HmeEoJobSnVO9 dto) {
        Integer isScrap = ZERO;
        if(Objects.isNull(dto.getIsScrap())){
            isScrap = ONE;
        }else{
            if(ONE == dto.getIsScrap()){
                isScrap = ZERO;
            }else{
                isScrap = ONE;
            }
        }
        if(HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())){
            HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
            hmeEoJobMaterial.setJobMaterialId(dto.getJobMaterialId());
            hmeEoJobMaterial.setIsScrap(isScrap);
            hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial);
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType()) || HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())){
            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
            hmeEoJobSnLotMaterial.setJobMaterialId(dto.getJobMaterialId());
            hmeEoJobSnLotMaterial.setIsScrap(isScrap);
            hmeEoJobSnLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobSnLotMaterial);
        }
        HmeEoJobSnVO9 resultVO = new HmeEoJobSnVO9();
        resultVO.setIsScrap(isScrap);
        return resultVO;
    }

    @Override
    public List<HmeEoJobSnVO9> releaseBackQuery(Long tenantId, HmeEoJobSnVO3 dto) {
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            //??????????????????!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if (StringUtils.isBlank(dto.getSnNum())) {
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        List<HmeEoJobSnVO9> allBackMaterialList = new ArrayList<>();
        // 20210411 add by sanfeng.zhang for fang.pan ????????????SN ??????top_eo_id?????????????????? ???jobId ??????
        List<HmeEoJobSnVO9> eoJobSnVO9List = hmeEoJobMaterialMapper.queryReleaseEoJobSnBySnNumOfRework(tenantId, dto.getSnNum());
        Map<String, List<HmeEoJobSnVO9>> eoJobSnVO9Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoJobSnVO9List)) {
            eoJobSnVO9Map = eoJobSnVO9List.stream().collect(Collectors.groupingBy(HmeEoJobSnVO9::getJobId));
        }
        List<String> jobIdList = eoJobSnVO9List.stream().map(HmeEoJobSnVO9::getJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //???????????????
            List<HmeEoJobSnVO9> backMaterialList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(jobIdList)) {
                backMaterialList = hmeEoJobMaterialMapper.selectReleaseEoJobMaterialOfRework2(tenantId, dto, jobIdList);
            }
            if(CollectionUtils.isNotEmpty(backMaterialList)){
                // ??????jobId??????????????????
                for (HmeEoJobSnVO9 hmeEoJobSnVO9 : backMaterialList) {
                    List<HmeEoJobSnVO9> eoJobSnVO9s = eoJobSnVO9Map.get(hmeEoJobSnVO9.getJobId());
                    if (CollectionUtils.isNotEmpty(eoJobSnVO9s)) {
                        HmeEoJobSnVO9 hmeEoJobSnVO91 = eoJobSnVO9s.get(0);
                        hmeEoJobSnVO9.setWorkOrderId(hmeEoJobSnVO91.getWorkOrderId());
                        hmeEoJobSnVO9.setWorkOrderNum(hmeEoJobSnVO91.getWorkOrderNum());
                        hmeEoJobSnVO9.setWorkcellId(hmeEoJobSnVO91.getWorkcellId());
                        hmeEoJobSnVO9.setOperationId(hmeEoJobSnVO91.getOperationId());
                        hmeEoJobSnVO9.setEoStepId(hmeEoJobSnVO91.getEoStepId());
                        hmeEoJobSnVO9.setShiftId(hmeEoJobSnVO91.getShiftId());
                        hmeEoJobSnVO9.setEoId(hmeEoJobSnVO91.getEoId());
                        hmeEoJobSnVO9.setEoNum(hmeEoJobSnVO91.getEoNum());
                        hmeEoJobSnVO9.setIdentification(hmeEoJobSnVO91.getIdentification());
                        hmeEoJobSnVO9.setShiftCode(hmeEoJobSnVO91.getShiftCode());
                        hmeEoJobSnVO9.setShiftDate(hmeEoJobSnVO91.getShiftDate());
                        hmeEoJobSnVO9.setProdLineCode(hmeEoJobSnVO91.getProdLineCode());
                        hmeEoJobSnVO9.setProdLineEnableFlag(hmeEoJobSnVO91.getProdLineEnableFlag());
                        hmeEoJobSnVO9.setSourceMaterialLotId(hmeEoJobSnVO91.getSourceMaterialLotId());
                        hmeEoJobSnVO9.setSiteId(hmeEoJobSnVO91.getSiteId());

                        hmeEoJobSnVO9.setCurrentWorkcellId(dto.getWorkcellId());
                    }
                }
                allBackMaterialList.addAll(backMaterialList);
            }
        } else if (HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType()) || HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())) {
            List<HmeEoJobSnVO9> backMaterialList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(jobIdList)) {
                //???????????????????????????
                backMaterialList = hmeEoJobSnLotMaterialMapper.selectReleaseEoJobSnLotMaterialOfRework2(tenantId, dto, jobIdList);
            }
            if (CollectionUtils.isNotEmpty(backMaterialList)) {
                // ??????jobId??????????????????
                for (HmeEoJobSnVO9 hmeEoJobSnVO9 : backMaterialList) {
                    List<HmeEoJobSnVO9> eoJobSnVO9s = eoJobSnVO9Map.get(hmeEoJobSnVO9.getJobId());
                    if (CollectionUtils.isNotEmpty(eoJobSnVO9s)) {
                        HmeEoJobSnVO9 hmeEoJobSnVO91 = eoJobSnVO9s.get(0);
                        hmeEoJobSnVO9.setWorkOrderId(hmeEoJobSnVO91.getWorkOrderId());
                        hmeEoJobSnVO9.setWorkOrderNum(hmeEoJobSnVO91.getWorkOrderNum());
                        hmeEoJobSnVO9.setWorkcellId(hmeEoJobSnVO91.getWorkcellId());
                        hmeEoJobSnVO9.setOperationId(hmeEoJobSnVO91.getOperationId());
                        hmeEoJobSnVO9.setEoStepId(hmeEoJobSnVO91.getEoStepId());
                        hmeEoJobSnVO9.setShiftId(hmeEoJobSnVO91.getShiftId());
                        hmeEoJobSnVO9.setEoId(hmeEoJobSnVO91.getEoId());
                        hmeEoJobSnVO9.setEoNum(hmeEoJobSnVO91.getEoNum());
                        hmeEoJobSnVO9.setIdentification(hmeEoJobSnVO91.getIdentification());
                        hmeEoJobSnVO9.setShiftCode(hmeEoJobSnVO91.getShiftCode());
                        hmeEoJobSnVO9.setShiftDate(hmeEoJobSnVO91.getShiftDate());
                        hmeEoJobSnVO9.setProdLineCode(hmeEoJobSnVO91.getProdLineCode());
                        hmeEoJobSnVO9.setProdLineEnableFlag(hmeEoJobSnVO91.getProdLineEnableFlag());
                        hmeEoJobSnVO9.setSourceMaterialLotId(hmeEoJobSnVO91.getSourceMaterialLotId());
                        hmeEoJobSnVO9.setSiteId(hmeEoJobSnVO91.getSiteId());

                        hmeEoJobSnVO9.setCurrentWorkcellId(dto.getWorkcellId());
                    }
                }
                allBackMaterialList.addAll(backMaterialList);
            }
        }
        return allBackMaterialList;
    }

    /**
     * ??????eo????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/22 10:51
     */
    @Override
    public void createEoRel(Long tenantId, HmeEoJobSnVO3 dto, String eoId) {
        // ??????hme_eo_rel ????????????  ??????????????????
        List<HmeEoRel> hmeEoRelList = hmeEoRelRepository.select(new HmeEoRel() {{
            setTenantId(tenantId);
            setEoId(eoId);
        }});
        if (CollectionUtils.isEmpty(hmeEoRelList)) {
            HmeEoRel hmeEoRel = new HmeEoRel();
            hmeEoRel.setTenantId(tenantId);
            hmeEoRel.setEoId(eoId);
            // ????????????EOId
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto.getSnNum());
            }});
            // ????????????????????????TOP_EO_ID
            List<MtExtendAttrVO> topEoIdAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_material_lot_attr");
                setAttrName("TOP_EO_ID");
                setKeyId(mtMaterialLot.getMaterialLotId());
            }});

            // ??????eoId ?????????eoId ???????????????????????????
            List<MtExtendAttrVO> reworkMaterialLotList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_eo_attr");
                setAttrName("REWORK_MATERIAL_LOT");
                setKeyId(eoId);
            }});
            String reworkMaterialLot = CollectionUtils.isNotEmpty(reworkMaterialLotList) ? reworkMaterialLotList.get(0).getAttrValue() : "";
            List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                    .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtEo.FIELD_IDENTIFICATION, reworkMaterialLot)).build());
            // ????????????????????????eoId ??????eoId?????? ?????????????????????eoId
            if (CollectionUtils.isNotEmpty(topEoIdAttrList) && StringUtils.isNotBlank(topEoIdAttrList.get(0).getAttrValue())) {
                hmeEoRel.setTopEoId(topEoIdAttrList.get(0).getAttrValue());
            } else if (StringUtils.isNotBlank(reworkMaterialLot) && CollectionUtils.isNotEmpty(mtEoList)) {
                hmeEoRel.setTopEoId(mtEoList.get(0).getEoId());
            }  else if (StringUtils.isNotBlank(mtMaterialLot.getEoId())) {
                hmeEoRel.setTopEoId(mtMaterialLot.getEoId());
            } else {
                hmeEoRel.setTopEoId(eoId);
            }
            // ????????????EO?????? ?????????????????????????????? ????????? ???????????????
            List<HmeEoRel> queryEoRelList = hmeEoRelRepository.select(new HmeEoRel() {{
                setTenantId(tenantId);
                setEoId(hmeEoRel.getTopEoId());
                setTopEoId(hmeEoRel.getTopEoId());
            }});
            if (CollectionUtils.isEmpty(queryEoRelList)) {
                HmeEoRel hmeTopEoRel = new HmeEoRel();
                hmeTopEoRel.setTenantId(tenantId);
                hmeTopEoRel.setEoId(hmeEoRel.getTopEoId());
                hmeTopEoRel.setTopEoId(hmeEoRel.getTopEoId());
                hmeEoRelRepository.insertSelective(hmeTopEoRel);
            }
            if (!StringUtils.equals(eoId, hmeEoRel.getTopEoId())) {
                hmeEoRelRepository.insertSelective(hmeEoRel);
            }
            // ???????????????????????????????????? ??????????????????????????????eoId
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("EO_REL_CREATE");
            }});
            List<MtExtendVO5> attrList = new ArrayList<>();
            if (CollectionUtils.isEmpty(topEoIdAttrList) || StringUtils.isBlank(topEoIdAttrList.get(0).getAttrValue())) {
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("TOP_EO_ID");
                mtExtendVO5.setAttrValue(hmeEoRel.getTopEoId());
                attrList.add(mtExtendVO5);
            }
            MtExtendVO5 flagAttr = new MtExtendVO5();
            flagAttr.setAttrName("RENOVATE_FLAG");
            flagAttr.setAttrValue(YES);
            attrList.add(flagAttr);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", mtMaterialLot.getMaterialLotId(), eventId, attrList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnBatchVO4 deleteMaterial(Long tenantId, HmeEoJobSnBatchVO4 dto) {
        List<HmeEoJobSnBatchVO6> deleteMaterialLotList = dto.getMaterialLotList().stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getDeleteFlag()))
                .collect(Collectors.toList());
        if(HmeConstants.MaterialTypeCode.SN.equals(deleteMaterialLotList.get(0).getMaterialType())){
            HmeEoJobMaterial hmeEoJobMaterial = hmeEoJobMaterialMapper.selectByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
            if (HmeConstants.ConstantValue.ONE.equals(hmeEoJobMaterial.getIsIssued())) {
                //???????????????${1}????????????,????????????????????????
                throw new MtException("HME_EO_JOB_SN_077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_077", "HME", deleteMaterialLotList.get(0).getMaterialLotCode()));
            }
            hmeEoJobMaterialMapper.deleteByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
            //????????????????????????Id???????????????????????????????????????????????????????????????????????????????????????
            List<HmeEoJobPumpComb> hmeEoJobPumpCombList = hmeEoJobPumpCombRepository.select(new HmeEoJobPumpComb() {{
                setMaterialLotId(deleteMaterialLotList.get(0).getMaterialLotId());
                setTenantId(tenantId);
            }});
            if(CollectionUtils.isNotEmpty(hmeEoJobPumpCombList)){
                HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombList.get(0);
                hmeEoJobPumpComb.setMaterialLotId(null);
                hmeEoJobPumpComb.setMaterialId(null);
                hmeEoJobPumpCombMapper.updateByPrimaryKey(hmeEoJobPumpComb);
            }
            //2021-09-07 17:18 add by chaonan.hu for wenxin.zhang ???????????????????????????????????????????????????????????????????????????
            List<String> selectionDetailsIdList = hmeEoJobPumpCombMapper.getSameSelectionOrderDetailsIdByMaterialLotId(tenantId, deleteMaterialLotList.get(0).getMaterialLotId());
            if(CollectionUtils.isNotEmpty(selectionDetailsIdList)){
                CustomUserDetails currentUser = DetailsHelper.getUserDetails();
                Long userId = Objects.isNull(currentUser) ? -1L : currentUser.getUserId();
                hmeEoJobPumpCombMapper.updatePumbSelectionDetailsStatus(tenantId, selectionDetailsIdList, userId, "RETURNED");
            }
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(deleteMaterialLotList.get(0).getMaterialType())){
            hmeEoJobLotMaterialMapper.deleteByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(deleteMaterialLotList.get(0).getMaterialType())){
            hmeEoJobTimeMaterialMapper.deleteByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
        }
        dto.getMaterialLotList().removeIf(item -> item.getJobMaterialId().equals(deleteMaterialLotList.get(0).getJobMaterialId()));
        return dto;
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
                    hmeEoJobSnBatchVO4.setReleasedQty(BigDecimal.ZERO);

                    List<HmeEoJobSnBatchVO6> materialLotList = new ArrayList<>();
                    List<HmeEoJobSnSingleVO5> lotVOList2 = allVOList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId()))
                            .collect(Collectors.toList());
                    String deadLineDate = "";
                    for (HmeEoJobSnSingleVO5 materialLot:lotVOList2
                    ) {
                        HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 = new HmeEoJobSnBatchVO6();
                        hmeEoJobSnBatchVO6.setJobMaterialId(materialLot.getJobMaterialId());
                        hmeEoJobSnBatchVO6.setMaterialType(materialLot.getProductionType());
                        hmeEoJobSnBatchVO6.setWorkCellId(workcellId);
                        hmeEoJobSnBatchVO6.setMaterialId(materialLot.getMaterialId());
                        hmeEoJobSnBatchVO6.setIsReleased(HmeConstants.ConstantValue.ONE);
                        hmeEoJobSnBatchVO6.setMaterialLotId(materialLot.getMaterialLotId());
                        hmeEoJobSnBatchVO6.setMaterialLotCode(materialLot.getMaterialLotCode());
                        hmeEoJobSnBatchVO6.setPrimaryUomQty(materialLot.getPrimaryUomQty());
                        hmeEoJobSnBatchVO6.setDeleteFlag(HmeConstants.ConstantValue.NO);
                        hmeEoJobSnBatchVO6.setLocatorId(materialLot.getLocatorId());
                        hmeEoJobSnBatchVO6.setLocatorCode(materialLot.getLocatorCode());
                        hmeEoJobSnBatchVO6.setLot(materialLot.getLot());
                        hmeEoJobSnBatchVO6.setSiteId(siteId);
                        hmeEoJobSnBatchVO6.setEnableFlag(materialLot.getEnableFlag());
                        hmeEoJobSnBatchVO6.setFreezeFlag(materialLot.getFreezeFlag());
                        hmeEoJobSnBatchVO6.setStocktakeFlag(materialLot.getStocktakeFlag());
                        hmeEoJobSnBatchVO6.setSupplierLot(materialLot.getSupplierLot());
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
     * ????????????-????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/9 14:37
     */
    private void snReworkBatchUpgrade(Long tenantId, HmeEoJobSnVO20 dto) {
        log.debug("<==============????????????begin==============>");

        if (Objects.isNull(dto)) {
            return;
        }

        if (CollectionUtils.isEmpty(dto.getMaterialLotList())) {
            return;
        }

        //????????????????????????
        List<MtMaterialSiteVO3> materialSiteIds = new ArrayList<>();
        dto.getMaterialLotList().forEach(item -> {
            MtMaterialSiteVO3 mtMaterialSiteVO3 = new MtMaterialSiteVO3();
            mtMaterialSiteVO3.setSiteId(dto.getSiteId());
            mtMaterialSiteVO3.setMaterialId(item.getMaterialId());
            materialSiteIds.add(mtMaterialSiteVO3);
        });
        List<MtMaterialSiteVO4> materialSiteList = mtMaterialSiteRepository.materialSiteLimitRelationBatchGet(tenantId, materialSiteIds);
        if (CollectionUtils.isEmpty(materialSiteList) ||
                (CollectionUtils.isNotEmpty(materialSiteList) && materialSiteList.size() != materialSiteIds.size())) {
            //????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_033", "HME"));
        }
        Map<String, String> materialSiteMap = materialSiteList.stream().collect(Collectors.toMap(MtMaterialSiteVO4::getMaterialSiteId, MtMaterialSiteVO4::getMaterialId));
        List<String> materialSiteIdList = materialSiteList.stream().map(MtMaterialSiteVO4::getMaterialSiteId).collect(Collectors.toList());
        List<String> attrNamelist = new ArrayList<>();
        attrNamelist.add("attribute17");
        //????????????????????????
        List<MtExtendAttrVO1> suUpGradeFlagList = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_site_attr", "MATERIAL_SITE_ID", materialSiteIdList, attrNamelist);
        boolean isUpGrade = false;
        HmeEoJobSnVO21 upGradeMaterialLot = new HmeEoJobSnVO21();
        if (CollectionUtils.isNotEmpty(suUpGradeFlagList)) {
            List<MtExtendAttrVO1> suUpGradeFlagList2 = suUpGradeFlagList.stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getAttrValue()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(suUpGradeFlagList2)) {
                if (suUpGradeFlagList2.size() > 1) {
                    MtRouterStep currentRouterStep =
                            mtRouterStepRepository.routerStepGet(tenantId, dto.getEoStepId());
                    MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,
                            materialSiteMap.get(suUpGradeFlagList2.get(1).getKeyId()));
                    // ?????????????????????${1}???SN??????????????????????????????????????????1???,??????????????????${2}??????????????????
                    throw new MtException("HME_EO_JOB_SN_027",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_027", "HME", currentRouterStep.getStepName(),
                                    mtMaterial.getMaterialName()));
                }
                List<HmeEoJobSnVO21> upGradeMaterialLotList = dto.getMaterialLotList().stream().filter(item -> materialSiteMap.get(suUpGradeFlagList2.get(0).getKeyId()).equals(item.getMaterialId()))
                        .collect(Collectors.toList());
                upGradeMaterialLot = upGradeMaterialLotList.get(0);
                isUpGrade = true;
            }
        }

        if (isUpGrade) {
            log.debug("????????????????????????");
            MtMaterialLot currentSn =
                    mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getSnMaterialLotId());
            final Date currentDate = CommonUtils.currentTimeGet();

            String upgradeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setLocatorId(currentSn.getLocatorId());
                    setEventTypeCode("EO_MATERIAL_LOT_UPGRADE");
                }
            });

            // ?????????????????????????????????N
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                {
                    setEventId(upgradeEventId);
                    setMaterialLotId(dto.getSnMaterialLotId());
                    setEnableFlag(HmeConstants.ConstantValue.NO);
                }
            }, "N");

            MtMaterialLotVO2 materialLotVO = new MtMaterialLotVO2();
            materialLotVO.setTenantId(tenantId);
            materialLotVO.setEventId(upgradeEventId);
            materialLotVO.setSiteId(dto.getSiteId());
            materialLotVO.setEnableFlag(HmeConstants.ConstantValue.YES);
            materialLotVO.setQualityStatus(HmeConstants.ConstantValue.OK);
            materialLotVO.setMaterialLotCode(upGradeMaterialLot.getMaterialLotCode());
            //V20200826 modify by penglin.sui for tianyang.xie ?????????????????????????????????
            materialLotVO.setMaterialId(currentSn.getMaterialId());
            //materialLotVO.setMaterialId(dto.getSnMaterialId());
            materialLotVO.setPrimaryUomId(currentSn.getPrimaryUomId());
            materialLotVO.setPrimaryUomQty(1D);
            materialLotVO.setLocatorId(currentSn.getLocatorId());
            materialLotVO.setEoId(dto.getEoId());
            materialLotVO.setLoadTime(currentDate);
            materialLotVO.setInSiteTime(currentDate);
            materialLotVO.setCreateReason("INITIALIZE");
            materialLotVO.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_cid_s")));
            MtMaterialLotVO13 materialLotResult = mtMaterialLotRepository.materialLotUpdate(tenantId,
                    materialLotVO, HmeConstants.ConstantValue.NO);

            // ??????????????????-??????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(materialLotResult.getMaterialLotId());
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName(HmeConstants.ExtendAttr.STATUS);
            MtExtendVO5 mfFlagAttr = new MtExtendVO5();
            mfFlagAttr.setAttrName("MF_FLAG");
            MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
            reworkFlagAttr.setAttrName("REWORK_FLAG");
            MtExtendVO5 pfmLevelAttr = new MtExtendVO5();
            pfmLevelAttr.setAttrName("PERFORMANCE_LEVEL");
            MtExtendVO5 toEoIdAttr = new MtExtendVO5();
            toEoIdAttr.setAttrName("TOP_EO_ID");
            MtExtendVO5 renovateFlagAttr = new MtExtendVO5();
            renovateFlagAttr.setAttrName("RENOVATE_FLAG");
            MtExtendVO5 sourceRepairAttr = new MtExtendVO5();
            sourceRepairAttr.setAttrName("SOURCE_REPAIRSN_ID");
            MtExtendVO5 afFlagAttr = new MtExtendVO5();
            afFlagAttr.setAttrName("AF_FLAG");
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsMapper.attrPropertyBatchQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID",
                    Collections.singletonList(dto.getSnMaterialLotId()));
            for (MtExtendAttrVO1 extendAttr : mtExtendAttrVO1s) {
                // ??????
                if (HmeConstants.ExtendAttr.STATUS.equals(extendAttr.getAttrName())) {
                    statusAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ???????????????
                if ("MF_FLAG".equals(extendAttr.getAttrName())) {
                    mfFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ????????????
                if ("REWORK_FLAG".equals(extendAttr.getAttrName())) {
                    reworkFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ????????????
                if ("PERFORMANCE_LEVEL".equals(extendAttr.getAttrName())) {
                    pfmLevelAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ??????EO
                if ("TOP_EO_ID".equals(extendAttr.getAttrName())) {
                    toEoIdAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ????????????
                if ("RENOVATE_FLAG".equals(extendAttr.getAttrName())) {
                    renovateFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ??????SN??????ID
                if ("SOURCE_REPAIRSN_ID".equals(extendAttr.getAttrName())) {
                    sourceRepairAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ??????????????????????????????
                if ("AF_FLAG".equals(extendAttr.getAttrName())) {
                    afFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
            }

            List<MtExtendVO5> list = new ArrayList<>(16);
            list.add(statusAttr);
            list.add(mfFlagAttr);
            list.add(reworkFlagAttr);
            list.add(pfmLevelAttr);
            list.add(toEoIdAttr);
            list.add(renovateFlagAttr);
            list.add(sourceRepairAttr);
            list.add(afFlagAttr);
            mtExtendVO10.setAttrs(list);
            // ???????????????????????????
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

            // eo??????
            String eoUpdateEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setLocatorId(currentSn.getLocatorId());
                    setEventTypeCode("EO_UPDATE");
                }
            });

            // ??????eo????????????REWORK_MATERIAL_LOT
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("REWORK_MATERIAL_LOT");
            mtExtendVO5.setAttrValue(upGradeMaterialLot.getMaterialLotCode());
            mtExtendVO5List.add(mtExtendVO5);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_eo_attr", dto.getEoId(), eoUpdateEventId, mtExtendVO5List);

            //V20201005 modify by penglin.sui for for lu.bai ??????hme_eo_job_sn ???material_lot_id
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnMapper.selectByPrimaryKey(dto.getJobId());
            if (Objects.isNull(hmeEoJobSn)) {
                //${1}????????? ?????????${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "eoJobSn", dto.getJobId()));
            }
            hmeEoJobSn.setMaterialLotId(materialLotResult.getMaterialLotId());
            hmeEoJobSnMapper.updateByPrimaryKey(hmeEoJobSn);
        }
    }


    private String getVirtualFlag(Long tenantId, String eoId, String operationId, String materialId){
        //????????????????????????????????????????????????
        List<String> routerStepIdList = hmeEoJobSnReWorkMapper.getRouterStepIdByEoId(tenantId, eoId);
        if (CollectionUtils.isNotEmpty(routerStepIdList)) {
            //???????????????????????????Bom??????
            List<MtBomComponent> mtBomComponentList
                    = hmeEoJobSnReWorkMapper.getBomComponentIdByRouterStepAndOperation(tenantId, operationId, routerStepIdList);
            if(CollectionUtils.isNotEmpty(mtBomComponentList)){
                //???????????????Bom???????????????????????????????????????????????????????????????????????????????????????????????????
                mtBomComponentList = mtBomComponentList.stream().filter(item -> materialId.equals(item.getMaterialId())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(mtBomComponentList)){
                    throw new MtException("HME_EO_JOB_SN_128", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_128", "HME"));
                }
                String bomComponentId = mtBomComponentList.get(0).getBomComponentId();
                //??????BOM????????????
                List<String> bomComponentIdList = new ArrayList<>();
                bomComponentIdList.add(bomComponentId);
                List<String> bomAttrNameList = new ArrayList<>();
                bomAttrNameList.add("lineAttribute8");
                List<MtExtendAttrVO1> bomExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID"
                        ,bomComponentIdList,bomAttrNameList);
                if(CollectionUtils.isNotEmpty(bomExtendAttrVO1List)){
                    return bomExtendAttrVO1List.get(0).getAttrValue();
                }
            }
        }
        return null;
    }
}
