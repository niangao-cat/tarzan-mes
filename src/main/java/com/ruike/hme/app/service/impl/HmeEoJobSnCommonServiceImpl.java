package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.app.service.HmeNcCheckService;
import com.ruike.hme.app.service.HmeNcDisposePlatformService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.ErpReducedSettleRadioUpdateDTO;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.app.service.ItfServiceTransferIfaceService;
import com.ruike.itf.app.service.ItfWorkOrderIfaceService;
import com.ruike.itf.domain.vo.ServiceTransferIfaceInvokeVO;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtNcIncident;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtNcRecordRepository;
import tarzan.actual.domain.vo.MtEoRouterActualVO19;
import tarzan.actual.domain.vo.MtEoRouterActualVO39;
import tarzan.actual.domain.vo.MtEoRouterActualVO40;
import tarzan.actual.domain.vo.MtEoStepActualVO22;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.method.domain.entity.MtRouterDoneStep;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterDoneStepRepository;
import tarzan.method.domain.repository.MtRouterNextStepRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import cn.hutool.core.util.ObjectUtil;

import java.math.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;


/**
 * ????????????-????????????
 *
 * @author yuchao.wang@hand-china.com 2020-11-03 00:04:39
 */
@Slf4j
@Service
public class HmeEoJobSnCommonServiceImpl implements HmeEoJobSnCommonService {

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtRouterNextStepRepository mtRouterNextStepRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;

    @Autowired
    private WmsTransactionTypeRepository transactionTypeRepository;

    @Autowired
    private HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private HmeWoSnRelRepository hmeWoSnRelRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private ItfWorkOrderIfaceService itfWorkOrderIfaceService;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private HmeWkcCompleteOutputRecordRepository hmeWkcCompleteOutputRecordRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private HmeEoJobSnBatchMapper hmeEoJobSnBatchMapper;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;

    @Autowired
    private HmeWkcCompleteOutputRecordMapper hmeWkcCompleteOutputRecordMapper;

    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @Autowired
    private HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;

    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;

    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;

    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;

    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;

    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;

    @Autowired
    private HmeWkcEoRelRepository hmeWkcEoRelRepository;

    @Autowired
    private HmeServiceReceiveMapper hmeServiceReceiveMapper;

    @Autowired
    private HmeServiceReceiveHisRepository hmeServiceReceiveHisRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private HmeEoJobSnReWorkMapper hmeEoJobSnReWorkMapper;

    @Autowired
    private ItfServiceTransferIfaceService itfServiceTransferIfaceService;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtExtendSettingsRepository extendSettingsRepository;

    @Autowired
    private HmeOpEqRelRepository hmeOpEqRelRepository;

    @Autowired
    private HmeEquipmentMapper hmeEquipmentMapper;

    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private HmeNcDisposePlatformService hmeNcDisposePlatformService;

    @Autowired
    private MtNcRecordRepository mtNcRecordRepository;

    @Autowired
    private HmeNcCheckService hmeNcCheckService;

    @Autowired
    private HmeNcDowngradeRepository hmeNcDowngradeRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private HmeEoRelMapper hmeEoRelMapper;

    @Autowired
    private HmeNcCheckMapper hmeNcCheckMapper;

    @Autowired
    private HmeNcCheckRepository hmeNcCheckRepository;

    @Autowired
    private HmeDataRecordExtendMapper hmeDataRecordExtendMapper;

    @Autowired
    private WmsCommonApiService wmsCommonApiService;

    @Autowired
    private HmeEoJobSnCommonMapper hmeEoJobSnCommonMapper;

    private static final String SYMBOL = "#";

    private static String fetchGroupKey2(String str1, String str2) {
        return str1 + SYMBOL + str2;
    }

    private static String fetchGroupKey3(String str1, String str2, String str3) {
        return str1 + SYMBOL + str2 + SYMBOL + str3;
    }

    /**
     *
     * @Description ??????EO??????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/10/28 18:18
     * @param tenantId ??????ID
     * @param stepType ?????????????????? ??????????????????/??????????????????????????????
     * @param eoIdList eoIdList
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeRouterStepVO>
     *
     */
    @Override
    public Map<String, HmeRouterStepVO> batchQueryRouterStepByEoIds(Long tenantId, String stepType, List<String> eoIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryRouterStepByEoIds tenantId={},stepType={},eoIdList={}", tenantId, stepType, eoIdList);
        Map<String, HmeRouterStepVO> routerStepMap = new HashMap<String, HmeRouterStepVO>();
        List<HmeRouterStepVO> routerStepVOList = new ArrayList<>();

        //??????EO????????????????????????
        if (HmeConstants.StepType.NEAR_STEP.equals(stepType)) {
            //?????????????????????
            routerStepVOList = hmeEoJobSnMapper.batchSelectStepByEoIds(tenantId, eoIdList);
        } else if (HmeConstants.StepType.NORMAL_STEP.equals(stepType)) {
            //?????????????????????????????????
            routerStepVOList = hmeEoJobSnMapper.batchSelectNormalStepByEoIds(tenantId, eoIdList);
        }

        if (CollectionUtils.isEmpty(routerStepVOList)) {
            // ?????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }

        Map<String, List<HmeRouterStepVO>> eoRouterStepMap = routerStepVOList
                .stream().collect(Collectors.groupingBy(HmeRouterStepVO::getEoId));

        //???????????????????????????eoId????????????
        if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != eoIdList.size()) {
            // ?????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }

        //??????????????????
        for (Map.Entry<String, List<HmeRouterStepVO>> entry : eoRouterStepMap.entrySet()) {
            if (CollectionUtils.isEmpty(entry.getValue())) {
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_045", "HME"));
            }

            //????????????????????????
            List<HmeRouterStepVO> routerSteps = entry.getValue().stream().sorted(Comparator
                    .comparing(HmeRouterStepVO::getSequence, Comparator.reverseOrder())).collect(Collectors.toList());

            //????????????????????????????????????????????????ID
            routerStepMap.put(entry.getKey(), routerSteps.get(0));
        }

        return routerStepMap;
    }

    /**
     *
     * @Description ?????????????????????????????????EO??????
     *
     * @author yuchao.wang
     * @date 2020/11/3 11:36
     * @param tenantId ??????ID
     * @param materialLotIdList ??????ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    @Override
    public HmeEoJobSnVO16 batchQueryAndCheckMaterialLotByIdsForTime(Long tenantId, List<String> materialLotIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryAndCheckMaterialLotByIdsForTime tenantId={},materialLotIdList={}", tenantId, materialLotIdList);
        List<HmeEoJobSnVO5> hmeEoJobSnVO5List = hmeEoJobSnMapper.batchQueryMaterialLotByIdsForTime(tenantId, materialLotIdList);
        if (CollectionUtils.isEmpty(hmeEoJobSnVO5List)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        Map<String, HmeEoJobSnVO5> eoJobSnMap = new HashMap<>();
        Map<String, HmeRouterStepVO2> nearStepMap = new HashMap<String, HmeRouterStepVO2>();
        Map<String, HmeRouterStepVO2> normalStepMap = new HashMap<String, HmeRouterStepVO2>();
        Map<String, List<HmeRouterStepVO2>> allNormalStepMap = new HashMap<String, List<HmeRouterStepVO2>>();

        for (HmeEoJobSnVO5 snVO5 : hmeEoJobSnVO5List) {
            //??????????????????
            if (!HmeConstants.ConstantValue.YES.equals(snVO5.getMaterialLotEnableFlag())) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", snVO5.getMaterialLotCode()));
            }

            //V20210703 modify by penglin.sui for peng.zhao ??????????????????????????????SN????????????
            if(StringUtils.isNotBlank(snVO5.getStocktakeFlag()) &&
                    HmeConstants.ConstantValue.YES.equals(snVO5.getStocktakeFlag())){
                //???SN???${1}???????????????,????????????
                throw new MtException("HME_EO_JOB_SN_205", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_205", "HME", snVO5.getMaterialLotCode()));
            }

            //???????????????????????? modify by yuchao.wang for tianyang.xie at 2020.12.21
            //if (HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag())) {
            //    //??????SN???????????????
            //    throw new MtException("HME_EO_JOB_SN_120", mtErrorMessageRepository
            //            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_120", "HME"));
            //}
            if (!HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag())
                    && !HmeConstants.ConstantValue.OK.equals(snVO5.getQualityStatus())) {
                // ??????????????????OK, ??????:SN????????????????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_029", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_029", "HME"));
            }
            //?????????????????????Y???N
            snVO5.setReworkFlag(HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag()) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);
            //???????????????????????????????????????Y???N
            snVO5.setDesignedReworkFlag((HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag()) && HmeConstants.ConstantValue.YES
                    .equals(snVO5.getDesignedReworkFlag())) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);

            //??????EO????????????
            if (StringUtils.isBlank(snVO5.getEoId())) {
                throw new MtException("ITF_DATA_COLLECT_0013", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "ITF_DATA_COLLECT_0013", "HME", snVO5.getMaterialLotCode()));
            }

            //??????EO??????
            if (!HmeConstants.EoStatus.WORKING.equals(snVO5.getStatus())
                    || (!HmeConstants.EoStatus.RELEASED.equals(snVO5.getLastEoStatus())
                    && !HmeConstants.EoStatus.HOLD.equals(snVO5.getLastEoStatus()))) {
                throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
            }
            eoJobSnMap.put(snVO5.getMaterialLotId(), snVO5);

            //??????????????????
            if (CollectionUtils.isEmpty(snVO5.getRouterStepList())) {
                // ?????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_045", "HME"));
            }

            //??????nearStep,????????????????????????
            List<HmeRouterStepVO2> nearSteps = snVO5.getRouterStepList().stream()
                    .filter(step -> StringUtils.isNotBlank(step.getEoStepWipId()))
                    .sorted(Comparator.comparing(HmeRouterStepVO2::getSequence, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(nearSteps)) {
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_045", "HME"));
            }
            nearStepMap.put(snVO5.getEoId(), nearSteps.get(0));

            //??????normalStep,????????????????????????
            List<HmeRouterStepVO2> normalSteps = snVO5.getRouterStepList().stream()
                    .filter(step -> StringUtils.isBlank(step.getReworkStepFlag()))
                    .sorted(Comparator.comparing(HmeRouterStepVO2::getSequence, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(normalSteps)) {
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_045", "HME"));
            }
            normalStepMap.put(snVO5.getEoId(), normalSteps.get(0));
            allNormalStepMap.put(snVO5.getEoId(), normalSteps);
        }

        //????????????????????????materialLotIdList????????????
        if (eoJobSnMap.size() != materialLotIdList.size()) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }
        HmeEoJobSnVO16 result = new HmeEoJobSnVO16();
        result.setEoJobSnMap(eoJobSnMap);
        result.setNearStepMap(nearStepMap);
        result.setNormalStepMap(normalStepMap);
        result.setAllNormalStepMap(allNormalStepMap);
        return result;
    }

    /**
     *
     * @Description ???????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/26 17:13
     * @param tenantId ??????ID
     * @param materialLotId ??????ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    @Override
    public HmeEoJobSnVO16 batchQueryAndCheckMaterialLotByIdsForTimeRework(Long tenantId, String materialLotId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryAndCheckMaterialLotByIdsForTimeRework tenantId={},materialLotId={}", tenantId, materialLotId);
        Set<String> repairTypes = lovAdapter.queryLovValue("HME.REPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> unrepairedTypes = lovAdapter.queryLovValue("HME.UNREPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        // ????????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, mtMaterialLot.getMaterialLotCode());
        HmeEoJobSnVO5 snVO5 = null;
        if (newMaterialLotFlag) {
            snVO5 = hmeEoJobSnMapper.batchQueryMaterialLotByIdsForTimeRework2(tenantId, materialLotId);
            // 20210914 add by ???????????? ??????????????? ??????????????????????????? ???????????????????????????
            List<MtExtendAttrVO> attrVOList = hmeEoJobSnCommonService.queryOldCodeAttrList(tenantId, snVO5.getEoId());
            Optional<MtExtendAttrVO> oldBarcodeInflagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("OLD_BARCODE_IN_FLAG", attr.getAttrName())).findFirst();
            if (oldBarcodeInflagOpt.isPresent() && YES.equals(oldBarcodeInflagOpt.get().getAttrValue())) {
                // ?????????????????????,???????????????????????????
                throw new MtException("HME_EO_JOB_SN_241", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_241", "HME"));
            }
            // 20210914 modiy by sanfeng.zhang for wenxin.zhang ????????? ??????????????????????????????
            Optional<MtExtendAttrVO> reworkFlagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("REWORK_FLAG", attr.getAttrName())).findFirst();
            String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getAttrValue() : "";
            snVO5.setReworkFlag(reworkFlag);
        } else {
            snVO5 = hmeEoJobSnMapper.batchQueryMaterialLotByIdsForTimeRework(tenantId, materialLotId);
        }
        if (Objects.isNull(snVO5) || StringUtils.isBlank(snVO5.getMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }
        if (!HmeConstants.ConstantValue.YES.equals(snVO5.getMaterialLotEnableFlag())) {
            //??????????????????, ?????????
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        //V20210705 modify by penglin.sui for peng.zhao ??????????????????????????????SN????????????
        if(StringUtils.isNotBlank(snVO5.getStocktakeFlag()) &&
                HmeConstants.ConstantValue.YES.equals(snVO5.getStocktakeFlag())){
            //???SN???${1}???????????????,????????????
            throw new MtException("HME_EO_JOB_SN_205", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_205", "HME", snVO5.getMaterialLotCode()));
        }

        //??????????????????
        if (StringUtils.isBlank(snVO5.getEoId()) || !repairTypes.contains(snVO5.getWorkOrderType())) {
            //??????????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_148", "HME"));
        }
        //??????WO??????
        if (!HmeConstants.WorkOrderStatus.RELEASED.equals(snVO5.getWorkOrderStatus())
                && !HmeConstants.WorkOrderStatus.EORELEASED.equals(snVO5.getWorkOrderStatus())) {
            //?????????????????????????????????EO????????????
            throw new MtException("HME_EO_JOB_SN_175", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_175", "HME"));
        }
        //??????EO??????
        if (!HmeConstants.EoStatus.WORKING.equals(snVO5.getStatus())
                || (!HmeConstants.EoStatus.RELEASED.equals(snVO5.getLastEoStatus())
                && !HmeConstants.EoStatus.HOLD.equals(snVO5.getLastEoStatus()))) {
            //EO???????????????????????????
            throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
        }

        //????????????
        if (!unrepairedTypes.contains(snVO5.getWorkOrderType()) && !HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag())) {
            //????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_161", "HME"));
        }

        Map<String, HmeEoJobSnVO5> eoJobSnMap = new HashMap<>();
        Map<String, HmeRouterStepVO2> nearStepMap = new HashMap<String, HmeRouterStepVO2>();
        Map<String, HmeRouterStepVO2> normalStepMap = new HashMap<String, HmeRouterStepVO2>();

        //?????????????????????Y
        snVO5.setReworkFlag(HmeConstants.ConstantValue.YES);
        eoJobSnMap.put(snVO5.getMaterialLotId(), snVO5);

        //??????????????????
        if (CollectionUtils.isEmpty(snVO5.getRouterStepList())) {
            // ?????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }

        //??????nearStep,????????????????????????????????????
        List<HmeRouterStepVO2> nearSteps = snVO5.getRouterStepList().stream()
                .filter(step -> StringUtils.isNotBlank(step.getEoStepWipId()))
                .sorted(Comparator.comparing(HmeRouterStepVO2::getLastUpdateDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(nearSteps)) {
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }
        nearStepMap.put(snVO5.getEoId(), nearSteps.get(0));

        //??????normalStep,????????????????????????????????????
        List<HmeRouterStepVO2> normalSteps = snVO5.getRouterStepList().stream()
                .filter(step -> StringUtils.isBlank(step.getReworkStepFlag()))
                .sorted(Comparator.comparing(HmeRouterStepVO2::getLastUpdateDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(normalSteps)) {
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }
        normalStepMap.put(snVO5.getEoId(), normalSteps.get(0));

        HmeEoJobSnVO16 result = new HmeEoJobSnVO16();
        result.setEoJobSnMap(eoJobSnMap);
        result.setNearStepMap(nearStepMap);
        result.setNormalStepMap(normalStepMap);
        return result;
    }

    /**
     *
     * @Description ????????????eo???????????????
     *
     * @author yuchao.wang
     * @date 2020/11/3 15:52
     * @param tenantId ??????ID
     * @param eoIdList eoId
     * @param operationIdList ??????ID
     * @return java.util.Map<java.lang.String,java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO3>>
     *
     */
    @Override
    public Map<String, List<HmeRouterStepVO3>> batchQueryCurrentRouterStep(Long tenantId, List<String> eoIdList, List<String> operationIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryCurrentRouterStep tenantId={},eoIdList={},operationIdList={}", tenantId, eoIdList, operationIdList);
        List<HmeRouterStepVO3> hmeRouterStepVO3s = hmeEoJobSnMapper.batchQueryCurrentRouterStep(tenantId, eoIdList, operationIdList);
        if (CollectionUtils.isEmpty(hmeRouterStepVO3s)){
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        Map<String, List<HmeRouterStepVO3>> eoRouterStepMap = hmeRouterStepVO3s
                .stream().collect(Collectors.groupingBy(HmeRouterStepVO3::getEoId));

        //???????????????????????????eoId????????????
        if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != eoIdList.size()) {
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }
        return eoRouterStepMap;
    }

    /**
     *
     * @Description ????????????-????????????eo???????????????
     *
     * @author yuchao.wang
     * @date 2020/11/3 20:25
     * @param tenantId ??????ID
     * @param eoIdList eoId
     * @param operationId ??????ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeRouterStepVO3>
     *
     */
    @Override
    public Map<String, HmeRouterStepVO3> batchQueryCurrentRouterStepForTime(Long tenantId, List<String> eoIdList, String operationId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryCurrentRouterStepForTime tenantId={},eoIdList={},operationId={}", tenantId, eoIdList, operationId);
        List<HmeRouterStepVO3> hmeRouterStepVO3s = hmeEoJobSnMapper.batchQueryCurrentRouterStepForTime(tenantId, eoIdList, operationId);
        if (CollectionUtils.isEmpty(hmeRouterStepVO3s)) {
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        Map<String, List<HmeRouterStepVO3>> eoRouterStepMap = hmeRouterStepVO3s
                .stream().collect(Collectors.groupingBy(HmeRouterStepVO3::getEoId));

        //???????????????????????????eoId????????????
        if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != eoIdList.size()) {
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        //???????????????????????????
        Map<String, HmeRouterStepVO3> currentStepMap = new HashMap<String, HmeRouterStepVO3>();
        for (Map.Entry<String, List<HmeRouterStepVO3>> entry : eoRouterStepMap.entrySet()) {
            if (CollectionUtils.isEmpty(entry.getValue()) || Objects.isNull(entry.getValue().get(0))) {
                // ????????????????????????????????????,??????????????????????????????/????????????????????????
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
            }
            if (entry.getValue().size() > 1) {
                //??????????????????????????????,?????????????????????
                throw new MtException("HME_EO_JOB_SN_119", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_119", "HME"));
            }

            currentStepMap.put(entry.getKey(), entry.getValue().get(0));
        }
        return currentStepMap;
    }

    /**
     * @param tenantId ??????ID
     * @param eoId     eoId
     * @return boolean
     * @Description ????????????EO????????????????????? ???????????????
     * @author yuchao.wang
     * @date 2020/9/28 14:47
     */
    @Override
    public boolean isAfterSalesWorkOrder(Long tenantId, String eoId) {
        String workOrderType = hmeEoJobSnMapper.queryWorkOrderTypeByEoId(tenantId, eoId);

        if (StringUtils.isNotBlank(workOrderType)) {
            //????????????????????????LOV
            List<LovValueDTO> woTypeLovs = lovAdapter.queryLovValue("HME.NOT_CHECK_JUMP_WOTYPE", tenantId);
            if (CollectionUtils.isNotEmpty(woTypeLovs)) {
                for (LovValueDTO lov : woTypeLovs) {
                    if (workOrderType.equals(lov.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, HmeEoJobSnBatchVO3> batchQtyAfterSalesWorkOrder(Long tenantId, List<String> eoIdList) {
        Map<String, HmeEoJobSnBatchVO3> resultMap = new HashMap<>();

        List<HmeEoJobSnBatchVO3> hmeEoJobSnBatchVO3List = hmeEoJobSnMapper.batchQueryWorkOrderTypeByEoId(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO3List)) {
            resultMap = hmeEoJobSnBatchVO3List.stream()
                    .collect(Collectors.toMap(HmeEoJobSnBatchVO3::getEoId, t -> t));
        }

        return resultMap;
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/3 20:16
     * @param tenantId ??????ID
     * @param routerStepIdList ??????ID
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    @Override
    public Map<String, String> batchQueryNextStep(Long tenantId, List<String> routerStepIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryNextStep tenantId={},routerStepIdList={}", tenantId, routerStepIdList);
        Map<String, String> nextStepMap = new HashMap<String, String>();
        if (CollectionUtils.isEmpty(routerStepIdList)) {
            return nextStepMap;
        }

        //???????????????????????????????????????
        List<MtRouterNextStep> nextSteps = mtRouterNextStepRepository.selectByCondition(Condition.builder(MtRouterNextStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterNextStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterNextStep.FIELD_ROUTER_STEP_ID, routerStepIdList))
                .build());

        if (CollectionUtils.isNotEmpty(nextSteps)) {
            //??????????????????????????????????????????????????????
            Map<String, List<MtRouterNextStep>> groupMap = nextSteps.stream()
                    .collect(Collectors.groupingBy(MtRouterNextStep::getRouterStepId));
            for (Map.Entry<String, List<MtRouterNextStep>> entry : groupMap.entrySet()) {
                if (CollectionUtils.isNotEmpty(entry.getValue())) {
                    //??????sequence??????????????????????????????
                    List<MtRouterNextStep> values = entry.getValue().stream().sorted(Comparator
                            .comparing(MtRouterNextStep::getSequence)).collect(Collectors.toList());
                    nextStepMap.put(entry.getKey(), values.get(0).getNextStepId());
                }
            }
        }
        return nextStepMap;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/14 15:05
     * @param tenantId ??????ID
     * @param routerStepId ??????ID
     * @return java.lang.String
     *
     */
    @Override
    public String queryNextStep(Long tenantId, String routerStepId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryNextStep tenantId={},routerStepId={}", tenantId, routerStepId);
        if (StringUtils.isBlank(routerStepId)) {
            return StringUtils.EMPTY;
        }

        //?????????????????????????????????
        SecurityTokenHelper.close();
        List<MtRouterNextStep> nextSteps = mtRouterNextStepRepository.selectByCondition(Condition.builder(MtRouterNextStep.class)
                .select(MtRouterNextStep.FIELD_ROUTER_STEP_ID, MtRouterNextStep.FIELD_NEXT_STEP_ID, MtRouterNextStep.FIELD_SEQUENCE)
                .andWhere(Sqls.custom().andEqualTo(MtRouterNextStep.FIELD_ROUTER_STEP_ID, routerStepId)
                        .andEqualTo(MtRouterNextStep.FIELD_TENANT_ID, tenantId))
                .build());
        if (CollectionUtils.isEmpty(nextSteps)) {
            return StringUtils.EMPTY;
        }

        //????????????????????????????????????sequence??????????????????????????????
        List<MtRouterNextStep> steps = nextSteps.stream().sorted(Comparator
                .comparing(MtRouterNextStep::getSequence)).collect(Collectors.toList());
        return steps.get(0).getNextStepId();
    }

    /**
     *
     * @Description ????????????ID?????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/17 16:42
     * @param tenantId ??????ID
     * @param routerStepId ??????ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryCurrentAndNextStepByCurrentId(Long tenantId, String routerStepId) {
        return hmeEoJobSnMapper.queryCurrentAndNextStepByCurrentId(tenantId, routerStepId);
    }

    /**
     *
     * @Description ??????eoId?????????ID?????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/17 17:03
     * @param tenantId ??????ID
     * @param eoId eoId
     * @param operationId ??????ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryCurrentAndNextStepByEoAndOperation(Long tenantId, String eoId, String operationId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryCurrentAndNextStepByEoAndOperation tenantId={},eoId={},operationId={}", tenantId, eoId, operationId);
        HmeRouterStepVO5 currentStep = hmeEoJobSnMapper.queryCurrentAndNextStepByEoAndOperation(tenantId, eoId, operationId);
        if (Objects.isNull(currentStep) || StringUtils.isBlank(currentStep.getRouterStepId())) {
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }
        return currentStep;
    }

    /**
     *
     * @Description ??????eoId?????????ID????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/27 15:35
     * @param tenantId ??????ID
     * @param eoId eoId
     * @param operationId ??????ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryCurrentStepByEoAndOperation(Long tenantId, String eoId, String operationId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryCurrentStepByEoAndOperation tenantId={},eoId={},operationId={}", tenantId, eoId, operationId);
        HmeRouterStepVO5 currentStep = hmeEoJobSnMapper.queryCurrentStepByEoAndOperation(tenantId, eoId, operationId);
        if (Objects.isNull(currentStep) || StringUtils.isBlank(currentStep.getRouterStepId())) {
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }
        return currentStep;
    }

    /**
     *
     * @Description ??????eoId?????????ID?????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/31 10:03
     * @param tenantId ??????ID
     * @param operationId ??????ID
     * @param routerId ????????????ID
     * @param eoId ????????????ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryCurrentAndNextStepForDesignedRework(Long tenantId, String operationId, String routerId, String eoId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryCurrentAndNextStepForDesignedRework tenantId={},operationId={},routerId={}", tenantId, operationId, routerId);
        List<HmeRouterStepVO5> currentStepList = hmeEoJobSnMapper.queryCurrentAndNextStepForDesignedRework(tenantId, operationId, routerId);
        if (CollectionUtils.isEmpty(currentStepList)) {
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        List<HmeRouterStepVO5> subCurrentStepList = currentStepList.stream().filter(item -> StringUtils.isNotBlank(item.getRouterStepId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(subCurrentStepList)) {
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.selectEoStepOfEo(tenantId , eoId);

        if(CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            for (HmeRouterStepVO5 hmeRouterStepVO5 : subCurrentStepList
            ) {
                List<HmeEoJobSn> subEoJobSnList = hmeEoJobSnList.stream().filter(item -> item.getEoStepId().equals(hmeRouterStepVO5.getRouterStepId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isEmpty(subEoJobSnList)){
                    return hmeRouterStepVO5;
                }
                if(Objects.isNull(subEoJobSnList.get(0).getSiteOutDate())){
                    return hmeRouterStepVO5;
                }
            }
            // ????????????????????????????????????,??????????????????????????????/????????????????????????
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }else{
            return subCurrentStepList.get(0);
        }
    }

    /**
     *
     * @Description ??????eoId???????????????ID?????????????????????
     *
     * @author yuchao.wang
     * @date 2021/2/2 10:03
     * @param tenantId ??????ID
     * @param eoId eoId
     * @param routerId ????????????ID
     * @param startTime ????????????
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryPreviousStepForDesignedRework(Long tenantId, String eoId, String routerId, Date startTime) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryPreviousStepForDesignedRework tenantId={},eoId={},routerId={}", tenantId, eoId, routerId);
        return hmeEoJobSnMapper.queryPreviousStepForDesignedRework(tenantId, eoId, routerId, startTime);
    }

    /**
     *
     * @Description ??????ID????????????EO?????? ??????EO?????????WO??????
     *
     * @author yuchao.wang
     * @date 2020/11/5 16:53
     * @param tenantId ??????ID
     * @param eoIdList eoIdList
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    @Override
    public HmeEoJobSnVO16 batchQueryEoAndWoInfoById(Long tenantId, List<String> eoIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryEoAndWoInfoById tenantId={},eoIdList={}", tenantId, eoIdList);
        //????????????EO??????
        Map<String, HmeEoVO4> eoVO4Map = new HashMap<>();
        List<HmeEoVO4> eoList = hmeEoJobSnMapper.batchQueryEoInfoById(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(eoList)) {
            eoList.forEach(item -> eoVO4Map.put(item.getEoId(), item));
        }

        if (eoVO4Map.size() != eoIdList.size() || !eoVO4Map.keySet().containsAll(eoIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO??????"));
        }

        //????????????WO??????
        Map<String, HmeWorkOrderVO2> workOrderVO2Map = new HashMap<>();
        List<String> woIdList = eoList.stream().map(HmeEoVO4::getWorkOrderId).distinct().collect(Collectors.toList());
        List<HmeWorkOrderVO2> woList = hmeEoJobSnMapper.batchQueryWoInfoById(tenantId, woIdList);
        if (CollectionUtils.isNotEmpty(woList)) {
            woList.forEach(item -> workOrderVO2Map.put(item.getWorkOrderId(), item));
        }

        if (workOrderVO2Map.size() != woIdList.size() || !workOrderVO2Map.keySet().containsAll(woIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        HmeEoJobSnVO16 hmeEoJobSnVO16 = new HmeEoJobSnVO16();
        hmeEoJobSnVO16.setEoMap(eoVO4Map);
        hmeEoJobSnVO16.setWoMap(workOrderVO2Map);
        return hmeEoJobSnVO16;
    }

    /**
     *
     * @Description ??????eoId??????EO/WO??????
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:37
     * @param tenantId ??????ID
     * @param eoStepId ??????ID
     * @param eoIdList eoId
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    @Override
    public HmeEoJobSnVO16 batchQueryEoAndWoInfoWithComponentById(Long tenantId, String eoStepId, List<String> eoIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryEoAndWoInfoWithComponentById tenantId={},eoStepId={},eoIdList={}", tenantId, eoStepId, eoIdList);
        //????????????EO??????
        Map<String, HmeEoVO4> eoVO4Map = new HashMap<>();
        List<HmeEoVO4> eoList = hmeEoJobSnMapper.batchQueryEoInfoWithComponentById(tenantId, eoStepId, eoIdList);
        if (CollectionUtils.isNotEmpty(eoList)) {
            eoList.forEach(item -> eoVO4Map.put(item.getEoId(), item));
            /*eoList.forEach(item -> {
                //????????????ID???????????????
                if (CollectionUtils.isNotEmpty(item.getBomComponentList())) {
                    List<HmeBomComponentVO> filterList = item.getBomComponentList().stream()
                            .filter(component -> Objects.nonNull(component) && StringUtils.isNotBlank(component.getBomComponentId()))
                            .collect(Collectors.toList());
                    if (filterList.size() != item.getBomComponentList().size()) {
                        item.setBomComponentList(filterList);
                    }
                }
                eoVO4Map.put(item.getEoId(), item);
            });*/
        }

        if (eoVO4Map.size() != eoIdList.size() || !eoVO4Map.keySet().containsAll(eoIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO??????"));
        }

        //????????????WO??????
        Map<String, HmeWorkOrderVO2> workOrderVO2Map = new HashMap<>();
        List<String> woIdList = eoList.stream().map(HmeEoVO4::getWorkOrderId).distinct().collect(Collectors.toList());
        List<HmeWorkOrderVO2> woList = hmeEoJobSnMapper.batchQueryWoInfoWithComponentById(tenantId, woIdList);
        if (CollectionUtils.isNotEmpty(woList)) {
            woList.forEach(item -> workOrderVO2Map.put(item.getWorkOrderId(), item));
        }

        if (workOrderVO2Map.size() != woIdList.size() || !workOrderVO2Map.keySet().containsAll(woIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //????????????????????????????????????
        long count = woList.stream().filter(item -> StringUtils.isBlank(item.getProdLineCode())).count();
        if (count > 0) {
            //SN???????????????????????????????????????????????????,?????????!
            throw new MtException("HME_EO_JOB_SN_142", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_142", "HME"));
        }

        HmeEoJobSnVO16 hmeEoJobSnVO16 = new HmeEoJobSnVO16();
        hmeEoJobSnVO16.setEoMap(eoVO4Map);
        hmeEoJobSnVO16.setWoMap(workOrderVO2Map);
        return hmeEoJobSnVO16;
    }

    /**
     *
     * @Description ??????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/6 15:52
     * @param tenantId ??????ID
     * @param materialLotIdList ??????ID
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    @Override
    public Map<String, String> batchQueryMaterialLotReworkFlag(Long tenantId, List<String> materialLotIdList) {
        List<MtExtendAttrVO1> mtExtendAttrVO1List = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_material_lot_attr", materialLotIdList, "REWORK_FLAG"));

        Map<String, String> reworkFlagMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
            List<MtExtendAttrVO1> attrList = mtExtendAttrVO1List.stream()
                    .filter(t -> "REWORK_FLAG".equals(t.getAttrName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(attrList)) {
                reworkFlagMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }
        }

        return reworkFlagMap;
    }

    /**
     *
     * @Description ????????????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/21 15:52
     * @param tenantId ??????ID
     * @param materialLotIdList ??????ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO3>
     *
     */
    @Override
    public List<HmeMaterialLotVO3> batchQueryMaterialLotReworkFlagForTime(Long tenantId, List<String> materialLotIdList) {
//        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryMaterialLotReworkFlagForTime tenantId={},materialLotIdList={}", tenantId, materialLotIdList);
        List<List<String>> splitMaterialLotIdList = InterfaceUtils.splitSqlList(materialLotIdList, 2000);
        //????????????????????????
//        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoForTime(tenantId, materialLotIdList);
        List<HmeMaterialLotVO3> materialLotList = new ArrayList<>(materialLotIdList.size());
        for (List<String> subSplitMaterialLotIdList : splitMaterialLotIdList
             ) {
            List<HmeMaterialLotVO3> subMaterialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoForTime(tenantId, subSplitMaterialLotIdList);
            if(CollectionUtils.isNotEmpty(subMaterialLotList)){
                materialLotList.addAll(subMaterialLotList);
            }
        }

        //???????????????????????????
        List<String> exitMaterialLotIdList = materialLotList.stream().map(HmeMaterialLotVO3::getMaterialLotId).distinct().collect(Collectors.toList());
        if (exitMaterialLotIdList.size() != materialLotList.size() || !exitMaterialLotIdList.containsAll(materialLotIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        return materialLotList;
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/5 19:09
     * @param tenantId ??????ID
     * @param workcellId ??????ID
     * @param eoIdList eoIdList
     * @param eoMap eo??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void siteOutBatchComplete(Long tenantId, String workcellId, List<String> eoIdList, Map<String, HmeEoVO4> eoMap) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.siteOutBatchComplete tenantId={},workcellId={},eoIdList={},eoMap={}", tenantId, workcellId, eoIdList, eoMap);
        //????????????????????????????????????
        Map<String, HmeRouterStepVO> nearStepMap = batchQueryRouterStepByEoIds(tenantId, HmeConstants.StepType.NEAR_STEP, eoIdList);

        // ??????????????????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //??????????????????
        List<MtEoRouterActualVO40> eoRouterActualList = new ArrayList<>();
        eoIdList.forEach(eoId -> {
            MtEoRouterActualVO40 eoRouterActualVO40 = new MtEoRouterActualVO40();
            eoRouterActualVO40.setQty(eoMap.get(eoId).getQty());
            eoRouterActualVO40.setEoStepActualId(nearStepMap.get(eoId).getEoStepActualId());
            eoRouterActualVO40.setWorkcellId(workcellId);
            eoRouterActualList.add(eoRouterActualVO40);
        });
        MtEoRouterActualVO39 eoRouterActualCompleteParam = new MtEoRouterActualVO39();
        eoRouterActualCompleteParam.setEventRequestId(eventRequestId);
        eoRouterActualCompleteParam.setSourceStatus("WORKING");
        eoRouterActualCompleteParam.setEoRouterActualList(eoRouterActualList);
        mtEoRouterActualRepository.completeBatchProcess(tenantId, eoRouterActualCompleteParam);
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/27 14:52
     * @param tenantId ??????ID
     * @param workcellId ??????ID
     * @param eo eo??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void siteOutComplete(Long tenantId, String workcellId, HmeEoVO4 eo) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.siteOutComplete tenantId={},workcellId={},eo={}", tenantId, workcellId, eo);
        //??????????????????????????????
        HmeRouterStepVO nearStep = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, eo.getEoId());
        if (Objects.isNull(nearStep)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }

        // ??????????????????
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        // API completeProcess
        MtEoRouterActualVO19 eoRouterActualCompleteParam = new MtEoRouterActualVO19();
        eoRouterActualCompleteParam.setWorkcellId(workcellId);
        eoRouterActualCompleteParam.setQty(eo.getQty());
        eoRouterActualCompleteParam.setEoStepActualId(nearStep.getEoStepActualId());
        eoRouterActualCompleteParam.setEventRequestId(completedEventRequestId);
        eoRouterActualCompleteParam.setSourceStatus("WORKING");
        mtEoRouterActualRepository.completeProcess(tenantId, eoRouterActualCompleteParam);

    }

    /**
     *
     * @Description ?????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/5 20:36
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO16 ??????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WmsObjectTransactionResponseVO> batchMainOutSite(Long tenantId,
                                                                 HmeEoJobSnVO3 dto,
                                                                 HmeEoJobSnVO16 hmeEoJobSnVO16) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSite tenantId={},dto={},hmeEoJobSnVO16={}", tenantId, dto, hmeEoJobSnVO16);

        //?????????????????????????????????
        List<String> eoStepIdList = hmeEoJobSnVO16.getHmeEoJobSnEntityList().stream().map(HmeEoJobSn::getEoStepId).distinct().collect(Collectors.toList());
        List<MtRouterDoneStep> mtRouterDoneStepList = mtRouterDoneStepRepository.selectByCondition(Condition.builder(MtRouterDoneStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterDoneStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterDoneStep.FIELD_ROUTER_STEP_ID, eoStepIdList)).build());
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSite mtRouterDoneStepList={}", mtRouterDoneStepList);

        //??????JobId?????????????????????????????????
        Map<String, HmeEoJobSn> eoJobSnEntityMap = new HashMap<String, HmeEoJobSn>();
        hmeEoJobSnVO16.getHmeEoJobSnEntityList().forEach(item -> eoJobSnEntityMap.put(item.getJobId(), item));

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        Long startDate = System.currentTimeMillis();
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
        log.info("=================================>????????????????????????-??????SN??????????????????????????????WKC????????????WKC???????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        HmeEoJobSnVO17 hmeEoJobSnVO17 = new HmeEoJobSnVO17();
        List<HmeEoJobSnVO3> normalSnLineList = dto.getSnLineList();
        List<HmeEoJobSnVO3> doneSnLineList = new ArrayList<>();
        List<MtMaterialLotVO20> materialLotCompleteUpdateList = new ArrayList<>();
        List<MtCommonExtendVO6> materialLotAttrCompleteUpdateList = new ArrayList<>();
        List<WmsObjectTransactionResponseVO> transactionResponseList = new ArrayList<WmsObjectTransactionResponseVO>();

        //?????????????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(mtRouterDoneStepList)) {
            //???????????????????????????
            List<String> doneStepIdList = mtRouterDoneStepList.stream().map(MtRouterDoneStep::getRouterStepId).distinct().collect(Collectors.toList());

            //???????????????????????????
            doneSnLineList = dto.getSnLineList().stream()
                    .filter(item -> doneStepIdList.contains(eoJobSnEntityMap.get(item.getJobId()).getEoStepId())).collect(Collectors.toList());
            //???????????????????????????
            normalSnLineList = dto.getSnLineList().stream()
                    .filter(item -> !doneStepIdList.contains(eoJobSnEntityMap.get(item.getJobId()).getEoStepId())).collect(Collectors.toList());

            if(CollectionUtils.isNotEmpty(doneSnLineList)){
                List<String> materialLotIdList = doneSnLineList.stream().map(HmeEoJobSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());
                //????????????????????????
                Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnCommonService.batchQueryMaterialLotInfo(tenantId, materialLotIdList);
                hmeEoJobSnVO16.setMaterialLotMap(materialLotMap);
            }

            //???????????????????????????????????????
            HmeEoJobSnVO18 hmeEoJobSnVO18 = new HmeEoJobSnVO18();
            BeanUtils.copyProperties(hmeEoJobSnVO16, hmeEoJobSnVO18);
            hmeEoJobSnVO18.setDoneSnLineList(doneSnLineList);
            hmeEoJobSnVO18.setDefaultStorageLocator(mtModLocator);
            hmeEoJobSnVO18.setInvUpdateEventRequestId(invUpdateEventRequestId);
            hmeEoJobSnVO18.setEoJobSnEntityMap(eoJobSnEntityMap);
            hmeEoJobSnVO18.setDoneEoStepIdList(doneStepIdList);
            hmeEoJobSnVO18.setNgDataRecordList(hmeEoJobSnVO16.getNgDataRecordList());
            startDate = System.currentTimeMillis();
            hmeEoJobSnVO17 = self().batchMainOutSiteForDoneStep(tenantId, dto, hmeEoJobSnVO18);
            log.info("=================================>????????????????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            materialLotCompleteUpdateList.addAll(hmeEoJobSnVO17.getMaterialLotCompleteUpdateList());
            materialLotAttrCompleteUpdateList.addAll(hmeEoJobSnVO17.getMaterialLotAttrCompleteUpdateList());
        }

        //??????????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(normalSnLineList)) {
            HmeEoJobSnVO18 hmeEoJobSnVO18 = new HmeEoJobSnVO18();
            hmeEoJobSnVO18.setReworkFlagMap(hmeEoJobSnVO16.getReworkFlagMap());
            hmeEoJobSnVO18.setNormalSnLineList(normalSnLineList);
            hmeEoJobSnVO18.setDefaultStorageLocator(mtModLocator);
            hmeEoJobSnVO18.setNgDataRecordList(hmeEoJobSnVO16.getNgDataRecordList());
            HmeEoJobSnVO17 normalEoJobSnVO17 = self().batchMainOutSiteForNormalStep(tenantId, dto, hmeEoJobSnVO18);

            materialLotCompleteUpdateList.addAll(normalEoJobSnVO17.getMaterialLotCompleteUpdateList());
            if (CollectionUtils.isNotEmpty(normalEoJobSnVO17.getMaterialLotAttrCompleteUpdateList())) {
                materialLotAttrCompleteUpdateList.addAll(normalEoJobSnVO17.getMaterialLotAttrCompleteUpdateList());
            }
        }

        //V20210309 modify by penglin.sui for hui.ma ??????????????????
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsRecordLabCode()) && StringUtils.isNotBlank(dto.getLabCode())) {
            startDate = System.currentTimeMillis();
            List<String> materialLotIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());
            List<String> eoStepIdList2 = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getEoStepId).distinct().collect(Collectors.toList());

            List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = hmeMaterialLotLabCodeMapper.batchSelectLabCode2(tenantId,materialLotIdList,eoStepIdList2,dto.getLabCode());
            List<HmeMaterialLotLabCode> updateMaterialLotLabList = new ArrayList<>();
            List<HmeMaterialLotLabCode> insertMaterialLotLabList = new ArrayList<>();
            for (HmeEoJobSnVO3 hmeEoJobSnVO3:dto.getSnLineList()
                 ) {
                List<HmeMaterialLotLabCode> existsHmeMaterialLotLabCodeList = hmeMaterialLotLabCodeList.stream().filter(item -> item.getMaterialLotId().equals(hmeEoJobSnVO3.getMaterialLotId())
                && item.getRouterStepId().equals(hmeEoJobSnVO3.getEoStepId()) && item.getLabCode().equals(dto.getLabCode())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(existsHmeMaterialLotLabCodeList)){
                    HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    updateHmeMaterialLotLabCode.setLabCodeId(existsHmeMaterialLotLabCodeList.get(0).getLabCodeId());
                    updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    updateMaterialLotLabList.add(updateHmeMaterialLotLabCode);
                }else{
                    HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    insertHmeMaterialLotLabCode.setTenantId(tenantId);
                    insertHmeMaterialLotLabCode.setMaterialLotId(hmeEoJobSnVO3.getMaterialLotId());
                    insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                    insertHmeMaterialLotLabCode.setLabCode(dto.getLabCode());
                    insertHmeMaterialLotLabCode.setJobId(hmeEoJobSnVO3.getJobId());
                    insertHmeMaterialLotLabCode.setWorkcellId(hmeEoJobSnVO3.getWorkcellId());
                    insertHmeMaterialLotLabCode.setWorkOrderId(hmeEoJobSnVO3.getWorkOrderId());
                    insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                    insertHmeMaterialLotLabCode.setRouterStepId(hmeEoJobSnVO3.getEoStepId());
                    insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    insertMaterialLotLabList.add(insertHmeMaterialLotLabCode);
                }
            }

            // ??????????????????
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();

            int count = 0;
            if(CollectionUtils.isNotEmpty(updateMaterialLotLabList)) {
                List<String> cidS = customDbRepository.getNextKeys("hme_material_lot_load_cid_s", updateMaterialLotLabList.size());
                List<String> updateDataSqlList = new ArrayList<>();
                for (HmeMaterialLotLabCode updateData : updateMaterialLotLabList) {
                    updateData.setCid(Long.valueOf(cidS.get(count)));
                    updateData.setLastUpdatedBy(userId);
                    updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                    updateDataSqlList.addAll(customDbRepository.getUpdateSql(updateData));
                    count++;
                }
                jdbcTemplate.batchUpdate(updateDataSqlList.toArray(new String[updateDataSqlList.size()]));
            }

            if(CollectionUtils.isNotEmpty(insertMaterialLotLabList)) {
                List<String> idS = customDbRepository.getNextKeys("hme_material_lot_load_s", insertMaterialLotLabList.size());
                List<String> cidS = customDbRepository.getNextKeys("hme_material_lot_load_cid_s", insertMaterialLotLabList.size());
                count = 0;
                List<String> insertDataSqlList = new ArrayList<>();
                for (HmeMaterialLotLabCode insertData : insertMaterialLotLabList) {
                    insertData.setLabCodeId(idS.get(count));
                    insertData.setCid(Long.valueOf(cidS.get(count)));
                    insertData.setObjectVersionNumber(1L);
                    insertData.setCreatedBy(userId);
                    insertData.setLastUpdatedBy(userId);
                    Date date = CommonUtils.currentTimeGet();
                    insertData.setCreationDate(date);
                    insertData.setLastUpdateDate(date);
                    insertDataSqlList.addAll(customDbRepository.getInsertSql(insertData));
                    count++;
                }
                jdbcTemplate.batchUpdate(insertDataSqlList.toArray(new String[insertDataSqlList.size()]));
            }
            log.info("=================================>????????????????????????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }

        //????????????????????????????????????????????????????????????????????????????????????????????????
        startDate = System.currentTimeMillis();
        if (CollectionUtils.isNotEmpty(mtRouterDoneStepList)) {
            //?????????????????????
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, hmeEoJobSnVO17.getMaterialLotOnhandUpdateList(),
                    hmeEoJobSnVO17.getOnhandIncreaseEventId(), HmeConstants.ConstantValue.NO);
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotCompleteUpdateList,
                    completeEventId, HmeConstants.ConstantValue.NO);

            //?????????????????????????????????
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr",
                    hmeEoJobSnVO17.getOnhandIncreaseEventId(), hmeEoJobSnVO17.getMaterialLotAttrOnhandUpdateList());
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr",
                    completeEventId, materialLotAttrCompleteUpdateList);

            //??????????????????SN??????
            hmeWoSnRelRepository.myBatchInsert(hmeEoJobSnVO17.getHmeWoSnRelInsertList());

            //??????????????????
            // ????????????ID
            String batchId = Utils.getBatchId();
            hmeEoJobSnVO17.getObjectTransactionRequestList().forEach(obj ->{
                obj.setAttribute16(batchId);
            });
            transactionResponseList = wmsObjectTransactionRepository
                    .objectTransactionSync(tenantId, hmeEoJobSnVO17.getObjectTransactionRequestList());

            //???????????????????????????
            if (CollectionUtils.isNotEmpty(hmeEoJobSnVO17.getErpReducedSettleRadioUpdateDTOList())) {
                try {
                    itfWorkOrderIfaceService.erpReducedSettleRadioUpdateRestProxy(tenantId, hmeEoJobSnVO17.getErpReducedSettleRadioUpdateDTOList());
                } catch (Exception e) {
                    log.error("=================?????????????????????????????????================={}:" + e.getMessage(), hmeEoJobSnVO17.getErpReducedSettleRadioUpdateDTOList());
                }
            }
        } else if (CollectionUtils.isNotEmpty(normalSnLineList)) {
            //????????????????????????????????????????????????EO_WKC_STEP_COMPLETE??????????????????????????????????????????
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotCompleteUpdateList,
                    completeEventId, HmeConstants.ConstantValue.NO);

            //????????????EO_WKC_STEP_COMPLETE??????????????????????????????
            if (CollectionUtils.isNotEmpty(materialLotAttrCompleteUpdateList)) {
                mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr",
                        completeEventId, materialLotAttrCompleteUpdateList);
            }
        }

        //???????????????????????????????????????
        batchAutoJudgeNc(tenantId , dto , hmeEoJobSnVO16);

        log.info("=================================>????????????????????????-??????????????????????????????????????????????????????????????????????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        return transactionResponseList;
    }

    /**
     *
     * @Description ??????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/2/1 14:25
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO16 ??????
     * @return void
     *
     */
    @Override
    public void batchMainOutSiteForTimeReworkEntryStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSiteForTimeReworkEntryStep tenantId={},dto={},hmeEoJobSnVO16={}", tenantId, dto, hmeEoJobSnVO16);
        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
        List<MtMaterialLotVO20> materialLotCompleteUpdateList = new ArrayList<>();
        for (HmeEoJobSnVO3 jobSn : dto.getSnLineList()) {
            MtMaterialLotVO20 mtLotCompleteUpdate = new MtMaterialLotVO20();
            mtLotCompleteUpdate.setMaterialLotId(jobSn.getMaterialLotId());
            mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            materialLotCompleteUpdateList.add(mtLotCompleteUpdate);
        }

        //????????????????????????????????????????????????EO_WKC_STEP_COMPLETE??????????????????????????????????????????
        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotCompleteUpdateList,
                completeEventId, HmeConstants.ConstantValue.NO);
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:37
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO18 ??????
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO17
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO17 batchMainOutSiteForDoneStep(Long tenantId,
                                                      HmeEoJobSnVO3 dto,
                                                      HmeEoJobSnVO18 hmeEoJobSnVO18) {
        MtModLocator mtModLocator = hmeEoJobSnVO18.getDefaultStorageLocator();
        List<String> eoStepIdList = hmeEoJobSnVO18.getDoneEoStepIdList();
        Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO18.getEoMap();
        Map<String, HmeWorkOrderVO2> woMap = hmeEoJobSnVO18.getWoMap();
        Map<String, String> reworkFlagMap = hmeEoJobSnVO18.getReworkFlagMap();
        Map<String, HmeEoJobSn> eoJobSnEntityMap = hmeEoJobSnVO18.getEoJobSnEntityMap();

        //???????????????????????????
        MtModLocator warehouse = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtModLocator.getLocatorId());

        //????????????
        String onhandIncreaseEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(hmeEoJobSnVO18.getInvUpdateEventRequestId());
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("INV_ONHAND_INCREASE");
            }
        });
        //??????????????????
        String eoCompleteEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setWorkcellId(dto.getWorkcellId());
                setEventTypeCode("HME_EO_COMPLETE");
            }
        });

        //????????????HME.NOT_CREATE_BATCH_NUM
        List<String> createBatchNumList = new ArrayList<>();
        List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.NOT_CREATE_BATCH_NUM", tenantId);
        if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
            createBatchNumList = lovValueDTOList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }

        //????????????HME_MATERIAL_BATCH_NUM???????????????????????????
        String hmeMaterialBatchNum = profileClient.getProfileValueByOptions("HME_MATERIAL_BATCH_NUM");

        //????????????????????????????????????
        Map<String, String> currentContainerMap = new HashMap<>();
        List<String> materialLotIdList = hmeEoJobSnVO18.getDoneSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId)
                .distinct().collect(Collectors.toList());
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
            mtMaterialLotList.forEach(item -> currentContainerMap.put(item.getMaterialLotId(), item.getCurrentContainerId()));
        }

        //??????????????????????????????
        Map<String, String> routerStepNameMap = new HashMap<String, String>();
        List<MtRouterStep> mtRouterStepList = mtRouterStepRepository.routerStepBatchGet(tenantId, eoStepIdList);
        if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
            mtRouterStepList.forEach(item -> routerStepNameMap.put(item.getRouterStepId(), item.getStepName()));
        }

        //????????????????????????
        Map<String, String> moveTypeMap = getMoveTypeForMainOutSite(tenantId);

        //??????MES_RK06??????????????????????????????SAP???
        Map<String, HmeServiceSplitRecordVO2> internalOrderNumMap = getRk06InternalOrderNum(tenantId, eoMap, woMap, hmeEoJobSnVO18.getDoneSnLineList());

        //?????????????????????
        Map<String, String> relatedLineNumMap = getRelatedLineNum(tenantId, eoMap, woMap);

        //????????????woSnRel Id
        List<String> woSnRelIdList = customSequence.getNextKeys("hme_wo_sn_rel_s", hmeEoJobSnVO18.getDoneSnLineList().size());

        //??????????????????????????????
        boolean sysParameterVerificationFlag = true;

        int index = 0;
        String lotCode = "";
        List<HmeWoSnRel> hmeWoSnRelInsertList = new ArrayList<>();
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        List<MtMaterialLotVO20> materialLotOnhandUpdateList = new ArrayList<>();
        List<MtCommonExtendVO6> materialLotAttrOnhandUpdateList = new ArrayList<>();
        List<MtMaterialLotVO20> materialLotCompleteUpdateList = new ArrayList<>();
        List<MtCommonExtendVO6> materialLotAttrCompleteUpdateList = new ArrayList<>();

        List<String> siteIdList = new ArrayList<>();
        List<String> snNumList = new ArrayList<>();
        List<String> workOrderNumList = new ArrayList<>();
        for (HmeEoJobSnVO3 jobSn : hmeEoJobSnVO18.getDoneSnLineList()) {
            HmeEoVO4 hmeEo = eoMap.get(jobSn.getEoId());
            HmeWorkOrderVO2 hmeWo = woMap.get(hmeEo.getWorkOrderId());

            siteIdList.add(hmeEo.getSiteId());
            snNumList.add(hmeEo.getIdentification());
            workOrderNumList.add(hmeWo.getWorkOrderNum());
        }
        Map<String , HmeWoSnRel> hmeWoSnRelMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(hmeEoJobSnVO18.getDoneSnLineList())){
            List<HmeWoSnRel> hmeWoSnRelList = hmeWoSnRelRepository.selectByCondition(Condition.builder(HmeWoSnRel.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeWoSnRel.FIELD_TENANT_ID, tenantId)
                            .andIn(HmeWoSnRel.FIELD_SITE_ID, siteIdList)
                            .andIn(HmeWoSnRel.FIELD_WORK_ORDER_NUM, workOrderNumList)
                            .andIn(HmeWoSnRel.FIELD_SN_NUM,snNumList)).build());
            if(CollectionUtils.isNotEmpty(hmeWoSnRelList)) {
                hmeWoSnRelMap = hmeWoSnRelList.stream()
                        .collect(Collectors.toMap(t -> t.getTenantId() + "-" + t.getSiteId() + "-" + t.getWorkOrderNum() + "-" + t.getSnNum(), t -> t));
            }
        }
        for (HmeEoJobSnVO3 jobSn : hmeEoJobSnVO18.getDoneSnLineList()) {
            jobSn.setEoStepId(eoJobSnEntityMap.get(jobSn.getJobId()).getEoStepId());
            String routerStepName = null;
            if (StringUtils.isNotBlank(jobSn.getEoStepId())) {
                routerStepName = routerStepNameMap.get(jobSn.getEoStepId());
                if (StringUtils.isBlank(routerStepName)) {
                    //${1}????????? ?????????${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "routerStep", jobSn.getEoStepId()));
                }
            }

            HmeEoVO4 hmeEo = eoMap.get(jobSn.getEoId());
            HmeWorkOrderVO2 hmeWo = woMap.get(hmeEo.getWorkOrderId());

            //??????????????????
            if (StringUtils.isBlank(hmeEo.getItemType()) || !createBatchNumList.contains(hmeEo.getItemType())) {
                lotCode = hmeMaterialBatchNum;
                //????????????????????????????????????????????????????????????
                if (sysParameterVerificationFlag) {
                    if (StringUtils.isBlank(hmeMaterialBatchNum)) {
                        //????????????????????????,?????????????????????????????????????????????????????????HME_MATERIAL_BATCH_NUM???
                        throw new MtException("HME_EO_JOB_SN_113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_113", "HME"));
                    }
                    if (hmeMaterialBatchNum.length() != 10) {
                        //?????????????????????10???,?????????????????????????????????????????????????????????HME_MATERIAL_BATCH_NUM???
                        throw new MtException("HME_EO_JOB_SN_114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_114", "HME"));
                    }
                    sysParameterVerificationFlag = false;
                }
            }
            if (hmeEoJobSnVO18.isTimeRework() && HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                // 20210812 add by sanfeng.zhang for tianyang.xie ?????????????????? ???????????????????????????????????????????????????
                // ????????????EO?????????????????? ?????? ??????????????? ???????????????
                Boolean newMaterialLotFlag = hmeEoJobSnVO18.isNewMaterialLotFlag();
                if (!newMaterialLotFlag) {
                    List<String> newMaterialLotIdList = hmeEoJobSnReWorkMapper.queryNewMaterialLotByOldMaterialLot(tenantId, jobSn.getMaterialLotId());
                    // ???????????????????????????ID
                    if (CollectionUtils.isEmpty(newMaterialLotIdList)) {
                        throw new MtException("HME_REPAIR_SN_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_REPAIR_SN_0008", "HME"));
                    }
                    // ???????????????
                    List<MtMaterialLotVO20> materialLotList = new ArrayList<>();
                    for (String newMaterialLotId : newMaterialLotIdList) {
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setMaterialLotId(newMaterialLotId);
                        mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.NO);
                        mtMaterialLotVO20.setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
                        materialLotList.add(mtMaterialLotVO20);
                    }
                    if (CollectionUtils.isNotEmpty(materialLotList)) {
                        materialLotCompleteUpdateList.addAll(materialLotList);
                    }
                }
            }
            //??????????????????????????????-INV_ONHAND_INCREASE??????
            MtMaterialLotVO20 mtLotOnhandUpdate = new MtMaterialLotVO20();
            mtLotOnhandUpdate.setMaterialLotId(jobSn.getMaterialLotId());
            mtLotOnhandUpdate.setLot(lotCode);
            materialLotOnhandUpdateList.add(mtLotOnhandUpdate);

            //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
            MtMaterialLotVO20 mtLotCompleteUpdate = new MtMaterialLotVO20();
            mtLotCompleteUpdate.setMaterialLotId(jobSn.getMaterialLotId());
            mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????NG
            if(CollectionUtils.isNotEmpty(hmeEoJobSnVO18.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnVO18.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(jobSn.getJobId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(ngDataRecordList)){
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
            materialLotCompleteUpdateList.add(mtLotCompleteUpdate);

            //??????????????????????????????????????????-INV_ONHAND_INCREASE??????
            List<MtCommonExtendVO5> mtLotExtendOnhandList = new ArrayList<>();
            MtCommonExtendVO5 mfFlagExtend = new MtCommonExtendVO5();
            mfFlagExtend.setAttrName("MF_FLAG");
            mfFlagExtend.setAttrValue("");
            mtLotExtendOnhandList.add(mfFlagExtend);

            //V20210525 modify by penglin.sui for fang.pan ??????????????????????????????????????????????????????????????????????????????
            if(hmeEoJobSnVO18.isTimeRework()) {
                HmeMaterialLotVO3 hmeMaterialLotVO3 = null;
                if (MapUtils.isNotEmpty(hmeEoJobSnVO18.getMaterialLotMap())) {
                    hmeMaterialLotVO3 = hmeEoJobSnVO18.getMaterialLotMap().getOrDefault(jobSn.getMaterialLotId(), null);
                }
                if (Objects.isNull(hmeMaterialLotVO3) ||
                        (Objects.nonNull(hmeMaterialLotVO3) && StringUtils.isBlank(hmeMaterialLotVO3.getCompleteProductionVersion()))) {
                    MtCommonExtendVO5 materialVersionExtend = new MtCommonExtendVO5();
                    materialVersionExtend.setAttrName("MATERIAL_VERSION");
                    materialVersionExtend.setAttrValue(hmeWo.getProductionVersion());
                    mtLotExtendOnhandList.add(materialVersionExtend);
                }
            }else{
                MtCommonExtendVO5 materialVersionExtend = new MtCommonExtendVO5();
                materialVersionExtend.setAttrName("MATERIAL_VERSION");
                materialVersionExtend.setAttrValue(hmeWo.getProductionVersion());
                mtLotExtendOnhandList.add(materialVersionExtend);
            }
            MtCommonExtendVO6 materialLotAttrOnhandUpdate = new MtCommonExtendVO6();
            materialLotAttrOnhandUpdate.setKeyId(jobSn.getMaterialLotId());
            materialLotAttrOnhandUpdate.setAttrs(mtLotExtendOnhandList);
            materialLotAttrOnhandUpdateList.add(materialLotAttrOnhandUpdate);

            //??????????????????????????????????????????-EO_WKC_STEP_COMPLETE??????
            List<MtCommonExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
            MtCommonExtendVO5 statusExtend = new MtCommonExtendVO5();
            statusExtend.setAttrName("STATUS");
            statusExtend.setAttrValue("COMPLETED");
            mtLotExtendCompleteList.add(statusExtend);
            MtCommonExtendVO5 soNumExtend = new MtCommonExtendVO5();
            soNumExtend.setAttrName("SO_NUM");
            soNumExtend.setAttrValue(hmeWo.getSoNum());
            mtLotExtendCompleteList.add(soNumExtend);
            MtCommonExtendVO5 soLineNumExtend = new MtCommonExtendVO5();
            soLineNumExtend.setAttrName("SO_LINE_NUM");
            soLineNumExtend.setAttrValue(hmeWo.getSoLineNum());
            mtLotExtendCompleteList.add(soLineNumExtend);
            if (HmeConstants.ConstantValue.YES.equals(reworkFlagMap.getOrDefault(jobSn.getMaterialLotId(), ""))) {
                MtCommonExtendVO5 reworkFlagExtend = new MtCommonExtendVO5();
                reworkFlagExtend.setAttrName("REWORK_FLAG");
                reworkFlagExtend.setAttrValue("");
                mtLotExtendCompleteList.add(reworkFlagExtend);
            }
            if (hmeEoJobSnVO18.isTimeRework() && HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                // ???????????????????????????????????? ???????????????????????????
                if(!hmeEoJobSnVO18.isNewMaterialLotFlag()) {
                    MtCommonExtendVO5 reworkFlagExtend = new MtCommonExtendVO5();
                    reworkFlagExtend.setAttrName("OLD_BARCODE_IN_FLAG");
                    reworkFlagExtend.setAttrValue("");
                    mtLotExtendCompleteList.add(reworkFlagExtend);
                }
            }
            MtCommonExtendVO6 materialLotAttrCompleteUpdate = new MtCommonExtendVO6();
            materialLotAttrCompleteUpdate.setKeyId(jobSn.getMaterialLotId());
            materialLotAttrCompleteUpdate.setAttrs(mtLotExtendCompleteList);
            materialLotAttrCompleteUpdateList.add(materialLotAttrCompleteUpdate);

            //??????SN??????
            HmeWoSnRel existsHmeWoSnRel = hmeWoSnRelMap.getOrDefault(tenantId + "-" + hmeEo.getSiteId() + "-" + hmeWo.getWorkOrderNum() + "-" + hmeEo.getIdentification() , null);
            if(Objects.isNull(existsHmeWoSnRel)) {
                HmeWoSnRel hmeWoSnRel = new HmeWoSnRel();
                hmeWoSnRel.setTenantId(tenantId);
                hmeWoSnRel.setRelId(woSnRelIdList.get(index++));
                hmeWoSnRel.setSiteId(hmeEo.getSiteId());
                hmeWoSnRel.setWorkOrderNum(hmeWo.getWorkOrderNum());
                hmeWoSnRel.setSnNum(hmeEo.getIdentification());
                hmeWoSnRel.setObjectVersionNumber(1L);
                hmeWoSnRel.setCreatedBy(hmeEoJobSnVO18.getUserId());
                hmeWoSnRel.setCreationDate(hmeEoJobSnVO18.getCurrentDate());
                hmeWoSnRel.setLastUpdatedBy(hmeEoJobSnVO18.getUserId());
                hmeWoSnRel.setLastUpdateDate(hmeEoJobSnVO18.getCurrentDate());
                hmeWoSnRelInsertList.add(hmeWoSnRel);
            }

            //????????????/????????????
            objectTransactionRequestList.addAll(getTransactionRequestVOList(tenantId, eoCompleteEventId, routerStepName, lotCode,
                    jobSn, hmeEo, hmeWo, mtModLocator, warehouse, moveTypeMap, currentContainerMap, internalOrderNumMap, relatedLineNumMap));
        }

        //?????????????????????
        // 20211130 add by sanfeng.zhang for wenxin.zhang ?????????????????????????????????
        List<HmeEoVO4> filterEoList = eoMap.values().stream().filter(vo -> snNumList.contains(vo.getIdentification())).collect(Collectors.toList());
        List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
        Map<String, List<HmeEoVO4>> onhandGroupMap = filterEoList.stream()
                .collect(Collectors.groupingBy(item -> item.getSiteId() + "-" + item.getMaterialId()));
        for (Map.Entry<String, List<HmeEoVO4>> onhandEntry : onhandGroupMap.entrySet()) {
            double sumQty = onhandEntry.getValue().stream().mapToDouble(MtEo::getQty).sum();

            // ???????????????
//            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
//            mtInvOnhandQuantityVO9.setSiteId(onhandEntry.getValue().get(0).getSiteId());
//            mtInvOnhandQuantityVO9.setLocatorId(mtModLocator.getLocatorId());
//            mtInvOnhandQuantityVO9.setMaterialId(onhandEntry.getValue().get(0).getMaterialId());
//            mtInvOnhandQuantityVO9.setChangeQuantity(sumQty);
//            mtInvOnhandQuantityVO9.setEventId(onhandIncreaseEventId);
//            mtInvOnhandQuantityVO9.setLotCode(lotCode);
//            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

            MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
            mtInvOnhandQuantityVO13.setSiteId(onhandEntry.getValue().get(0).getSiteId());
            mtInvOnhandQuantityVO13.setLocatorId(mtModLocator.getLocatorId());
            mtInvOnhandQuantityVO13.setMaterialId(onhandEntry.getValue().get(0).getMaterialId());
            mtInvOnhandQuantityVO13.setLotCode(lotCode);
            mtInvOnhandQuantityVO13.setChangeQuantity(sumQty);
            onhandList.add(mtInvOnhandQuantityVO13);
        }
        if(CollectionUtils.isNotEmpty(onhandList)){
            // ?????????????????????
            MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
            mtInvOnhandQuantityVO16.setEventId(onhandIncreaseEventId);
            mtInvOnhandQuantityVO16.setOnhandList(onhandList);
            mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId,mtInvOnhandQuantityVO16);
        }

        //?????????????????????????????????
        List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOList = getErpReducedSettleRadioUpdateDTOList(tenantId, woMap);

        //??????????????????
        HmeEoJobSnVO17 result = new HmeEoJobSnVO17();
        result.setOnhandIncreaseEventId(onhandIncreaseEventId);
        result.setHmeWoSnRelInsertList(hmeWoSnRelInsertList);
        result.setMaterialLotOnhandUpdateList(materialLotOnhandUpdateList);
        result.setMaterialLotAttrOnhandUpdateList(materialLotAttrOnhandUpdateList);
        result.setMaterialLotCompleteUpdateList(materialLotCompleteUpdateList);
        result.setMaterialLotAttrCompleteUpdateList(materialLotAttrCompleteUpdateList);
        result.setObjectTransactionRequestList(objectTransactionRequestList);
        result.setErpReducedSettleRadioUpdateDTOList(erpReducedSettleRadioUpdateDTOList);
        return result;
    }

    /**
     *
     * @Description ??????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/7 13:47
     * @param tenantId ??????ID
     * @param woMap wo??????
     * @return java.util.List<com.ruike.itf.api.dto.ErpReducedSettleRadioUpdateDTO>
     *
     */
    private List<ErpReducedSettleRadioUpdateDTO> getErpReducedSettleRadioUpdateDTOList(Long tenantId, Map<String, HmeWorkOrderVO2> woMap) {
        List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOList = new ArrayList<ErpReducedSettleRadioUpdateDTO>();

        //??????WO???????????????????????????????????????????????????
        List<MtWorkOrder> completedWoList = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class)
                .select(MtWorkOrder.FIELD_WORK_ORDER_ID)
                .andWhere(Sqls.custom().andEqualTo(MtWorkOrder.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtWorkOrder.FIELD_STATUS, HmeConstants.WorkOrderStatus.COMPLETED)
                        .andIn(MtWorkOrder.FIELD_WORK_ORDER_ID, woMap.keySet())).build());
        if (CollectionUtils.isEmpty(completedWoList)) {
            return erpReducedSettleRadioUpdateDTOList;
        }

        //???????????????????????????????????????EO
        List<String> completeWoIdList = completedWoList.stream().map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
        List<HmeEoVO4> completeEoList = hmeEoJobSnMapper.batchQueryEoInfoByWoId(tenantId, HmeConstants.EoStatus.COMPLETED, completeWoIdList);
        if (CollectionUtils.isEmpty(completeEoList)) {
            return erpReducedSettleRadioUpdateDTOList;
        }

        //????????????????????????
        Map<String, List<HmeEoVO4>> eoGroupMap = completeEoList.stream().collect(Collectors.groupingBy(HmeEoVO4::getWorkOrderId));
        for (Map.Entry<String, List<HmeEoVO4>> woEntry : eoGroupMap.entrySet()) {
            //?????????????????????EO???????????????????????????
            List<HmeEoVO4> relatedEoList = woEntry.getValue().stream()
                    .filter(item -> !woMap.get(woEntry.getKey()).getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(relatedEoList)) {
                continue;
            }

            //?????????????????????????????????????????????
            BigDecimal mainMaterialQty = new BigDecimal(100);

            //???????????????
            BigDecimal sumQty = new BigDecimal(woEntry.getValue().stream().mapToDouble(MtEo::getQty).sum());

            //????????????????????????????????????????????????????????????
            Map<String, List<HmeEoVO4>> materialGroup = relatedEoList.stream().collect(Collectors.groupingBy(HmeEoVO4::getMaterialCode));
            for (Map.Entry<String, List<HmeEoVO4>> materialEntry : materialGroup.entrySet()) {
                BigDecimal qty = new BigDecimal(materialEntry.getValue().stream().mapToDouble(MtEo::getQty).sum());
                BigDecimal radio = qty.multiply(BigDecimal.valueOf(100)).divide(sumQty, 0, BigDecimal.ROUND_HALF_UP);
                ErpReducedSettleRadioUpdateDTO erpReducedSettleRadioUpdateDTO = new ErpReducedSettleRadioUpdateDTO();
                erpReducedSettleRadioUpdateDTO.setAUFNR(woMap.get(woEntry.getKey()).getWorkOrderNum());
                erpReducedSettleRadioUpdateDTO.setMATNR(materialEntry.getKey());
                erpReducedSettleRadioUpdateDTO.setPROZS(String.valueOf(radio));
                erpReducedSettleRadioUpdateDTOList.add(erpReducedSettleRadioUpdateDTO);

                //?????????????????????
                mainMaterialQty = mainMaterialQty.subtract(radio);
            }

            //???????????????????????????
            ErpReducedSettleRadioUpdateDTO erpReducedSettleRadioUpdateDTO = new ErpReducedSettleRadioUpdateDTO();
            erpReducedSettleRadioUpdateDTO.setAUFNR(woMap.get(woEntry.getKey()).getWorkOrderNum());
            erpReducedSettleRadioUpdateDTO.setMATNR(woMap.get(woEntry.getKey()).getMaterialCode());
            erpReducedSettleRadioUpdateDTO.setPROZS(String.valueOf(mainMaterialQty));
            erpReducedSettleRadioUpdateDTOList.add(erpReducedSettleRadioUpdateDTO);
        }
        return erpReducedSettleRadioUpdateDTOList;
    }

    /**
     *
     * @Description ?????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:38
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO18 ??????
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO17
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO17 batchMainOutSiteForNormalStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO18 hmeEoJobSnVO18) {
        MtModLocator mtModLocator = hmeEoJobSnVO18.getDefaultStorageLocator();
        Map<String, String> reworkFlagMap = hmeEoJobSnVO18.getReworkFlagMap();
        List<MtMaterialLotVO20> materialLotCompleteUpdateList = new ArrayList<>();
        List<MtCommonExtendVO6> materialLotAttrCompleteUpdateList = new ArrayList<>();

        for (HmeEoJobSnVO3 jobSn : hmeEoJobSnVO18.getNormalSnLineList()) {
            //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
            MtMaterialLotVO20 mtLotCompleteUpdate = new MtMaterialLotVO20();
            mtLotCompleteUpdate.setMaterialLotId(jobSn.getMaterialLotId());
            mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????NG
            if(CollectionUtils.isNotEmpty(hmeEoJobSnVO18.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnVO18.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(jobSn.getJobId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(ngDataRecordList)){
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
            materialLotCompleteUpdateList.add(mtLotCompleteUpdate);

            //????????????????????? ??????????????????????????????????????????-EO_WKC_STEP_COMPLETE??????
            if (HmeConstants.ConstantValue.YES.equals(reworkFlagMap.getOrDefault(jobSn.getMaterialLotId(), ""))) {
                List<MtCommonExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
                MtCommonExtendVO5 reworkFlagExtend = new MtCommonExtendVO5();
                reworkFlagExtend.setAttrName("REWORK_FLAG");
                reworkFlagExtend.setAttrValue("");
                mtLotExtendCompleteList.add(reworkFlagExtend);
                MtCommonExtendVO6 materialLotAttrCompleteUpdate = new MtCommonExtendVO6();
                materialLotAttrCompleteUpdate.setKeyId(jobSn.getMaterialLotId());
                materialLotAttrCompleteUpdate.setAttrs(mtLotExtendCompleteList);
                materialLotAttrCompleteUpdateList.add(materialLotAttrCompleteUpdate);
            }
        }

        HmeEoJobSnVO17 result = new HmeEoJobSnVO17();
        result.setMaterialLotCompleteUpdateList(materialLotCompleteUpdateList);
        result.setMaterialLotAttrCompleteUpdateList(materialLotAttrCompleteUpdateList);
        return result;
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:43
     * @param tenantId ??????ID
     * @param transactionResponseList ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendTransactionInterface(Long tenantId, List<WmsObjectTransactionResponseVO> transactionResponseList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.sendTransactionInterface tenantId={},transactionResponseList={}", tenantId, transactionResponseList);
        if (CollectionUtils.isEmpty(transactionResponseList) || !transactionResponseList.get(0).getSuccess()) {
            return;
        }

        //????????????ID?????????????????????
        List<String> transactionIdList = transactionResponseList.stream().map(WmsObjectTransactionResponseVO::getTransactionId).collect(Collectors.toList());
        List<WmsObjectTransaction> transactionList = wmsObjectTransactionRepository.selectByCondition(Condition.builder(WmsObjectTransaction.class)
                .andWhere(Sqls.custom().andEqualTo(WmsObjectTransaction.FIELD_TENANT_ID, tenantId)
                        .andIn(WmsObjectTransaction.FIELD_TRANSACTION_ID, transactionIdList)).build());
        if (transactionIdList.size() != transactionList.size()) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }

        //V20211009 modify by penglin.sui for hui.ma ?????????????????????????????????HME.REPORT_NON_REALTIME_BUSINESS_AREA???????????????,????????????
        List<String> nonRealTimeTransactionIdList = new ArrayList<>();
        //????????????
        List<LovValueDTO> lovValueDTOList = wmsCommonApiService.queryLovValueList(tenantId, "HME.REPORT_NON_REALTIME_BUSINESS_AREA", "");
        if(CollectionUtils.isNotEmpty(lovValueDTOList)) {
            List<String> valueList = lovValueDTOList.stream()
                    .map(LovValueDTO::getValue)
                    .collect(Collectors.toList());
            List<List<String>> splitTransactionIdList = InterfaceUtils.splitSqlList(transactionIdList, 3000);
            for (List<String> subTransactionIdList : splitTransactionIdList) {
                List<String> subNonRealTimeTransactionIdList = wmsObjectTransactionRepository.selectNonRealTimeTransaction(tenantId, subTransactionIdList, valueList);
                if(CollectionUtils.isNotEmpty(subNonRealTimeTransactionIdList)){
                    nonRealTimeTransactionIdList.addAll(subNonRealTimeTransactionIdList);
                }
            }
        }
        //????????????/????????????
        List<String> reportIdList = transactionList.stream()
                .filter(item -> "HME_WORK_REPORT".equals(item.getTransactionTypeCode()))
                .map(WmsObjectTransaction::getTransactionId)
                .collect(Collectors.toList());
        List<WmsObjectTransactionResponseVO> woReportTransactionResponseVOList = transactionResponseList.stream()
                .filter(item -> reportIdList.contains(item.getTransactionId())
                        && !nonRealTimeTransactionIdList.contains(item.getTransactionId()))
                .collect(Collectors.toList());
        List<WmsObjectTransactionResponseVO> woCompleteTransactionResponseVOList = transactionResponseList.stream()
                .filter(item -> !reportIdList.contains(item.getTransactionId())
                        && !nonRealTimeTransactionIdList.contains(item.getTransactionId()))
                .collect(Collectors.toList());

        //????????????????????????,????????????????????????
        log.info("<====== HmeEoJobSnCommonServiceImpl.sendSapProdMaterialMove ????????????????????????????????? " +
                "woReportTransactionResponseVOList={},woCompleteTransactionResponseVOList={}",
                woReportTransactionResponseVOList, woCompleteTransactionResponseVOList);
        itfObjectTransactionIfaceService.sendSapProdMaterialMove(tenantId, woReportTransactionResponseVOList, woCompleteTransactionResponseVOList);
    }

    /**
     *
     * @Description ????????????????????????-??????????????????/????????????????????????/????????????????????????/PDA??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/7 14:02
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO16 ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchWkcCompleteOutputRecord(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchWkcCompleteOutputRecord tenantId={},dto={},hmeEoJobSnVO16={}", tenantId, dto, hmeEoJobSnVO16);
        //???????????????????????????
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        Map<String, String> reworkFlagMap = hmeEoJobSnVO16.getReworkFlagMap();
        for (HmeEoJobSn eoJobSn : hmeEoJobSnVO16.getHmeEoJobSnEntityList()) {
            if (!HmeConstants.ConstantValue.YES.equals(reworkFlagMap.getOrDefault(eoJobSn.getMaterialLotId(), ""))) {
                hmeEoJobSnList.add(eoJobSn);
            }
        }
        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            return;
        }

        //????????????
        MtModOrganizationVO2 lineParam = new MtModOrganizationVO2();
        lineParam.setTopSiteId(dto.getSiteId());
        lineParam.setOrganizationId(dto.getWorkcellId());
        lineParam.setOrganizationType("WORKCELL");
        lineParam.setParentOrganizationType("WORKCELL");
        lineParam.setQueryType("TOP");
        List<MtModOrganizationItemVO> lineVOList = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, lineParam);
        if (CollectionUtils.isEmpty(lineVOList) || Objects.isNull(lineVOList.get(0))) {
            // ????????????Wkc??????????????????
            throw new MtException("HME_EO_JOB_SN_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_023", "HME"));
        }
        String workcellId = lineVOList.get(0).getOrganizationId();
        Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO16.getEoMap();
        Set<String> woIdSet = hmeEoJobSnVO16.getWoMap().keySet();
        List<String> eoIdList = new ArrayList<>(eoMap.keySet());

        //??????????????????????????????????????????
        SecurityTokenHelper.close();
        List<HmeWkcCompleteOutputRecord> wkcCompleteOutputRecords = hmeWkcCompleteOutputRecordRepository.selectByCondition(Condition.builder(HmeWkcCompleteOutputRecord.class)
                .andWhere(Sqls.custom().andEqualTo(HmeWkcCompleteOutputRecord.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeWkcCompleteOutputRecord.FIELD_WORKCELL_ID, workcellId)
                        .andIn(HmeWkcCompleteOutputRecord.FIELD_WORK_ORDER_ID, woIdSet)).build());
        //??????workcellId+WorkOrderId+WkcShiftId+MaterialId???????????????????????????????????????????????????????????????
        Map<String, HmeWkcCompleteOutputRecord> wkcCompleteOutputRecordMap = wkcCompleteOutputRecords.stream()
                .collect(Collectors.toMap(t -> t.getWorkOrderId() + "-" + t.getWkcShiftId() + "-" + t.getMaterialId(), t -> t));

        //?????????????????????????????????????????????????????????
        Map<String, List<HmeEoJobSnDTO3>> workcellIdMap = new HashMap<String, List<HmeEoJobSnDTO3>>();
        List<HmeEoJobSnDTO3> allWorkcellIdList = hmeEoJobSnMapper.batchQueryUnfinishedSection(tenantId, eoIdList, dto.getSiteId());
        if (CollectionUtils.isNotEmpty(allWorkcellIdList)) {
            workcellIdMap = allWorkcellIdList.stream()
                    .filter(t->!HmeConstants.ConstantValue.YES.equals(t.getPrepareFlag()))
                    .collect(Collectors.groupingBy(HmeEoJobSnDTO3::getEoId));
        }

        List<HmeWkcCompleteOutputRecord> insertDataList = new ArrayList<>(hmeEoJobSnList.size());
        List<HmeWkcCompleteOutputRecord> updateDataList = new ArrayList<>(hmeEoJobSnList.size());
        Map<String, Long> updateQtyMap = new HashMap<String, Long>();
        for (HmeEoJobSn eoJobSn : hmeEoJobSnList) {
            //??????EO
            HmeEoVO4 hmeEo = eoMap.get(eoJobSn.getEoId());
            if (Objects.isNull(hmeEo) || StringUtils.isBlank(hmeEo.getEoId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "EO??????"));
            }

            //???????????????????????????????????????????????????
            List<String> workcellIdList = new ArrayList<>();
            if (workcellIdMap.containsKey(hmeEo.getEoId())) {
                workcellIdList = workcellIdMap.get(hmeEo.getEoId()).stream()
                        .map(HmeEoJobSnDTO3::getWorkcellId).distinct().collect(Collectors.toList());
            }

            //?????????????????????????????????
            if (CollectionUtils.isNotEmpty(workcellIdList) && workcellIdList.contains(workcellId)) {
                continue;
            }

            //???????????????????????????????????????
            HmeWkcCompleteOutputRecord wkcCompleteOutputRecord = wkcCompleteOutputRecordMap
                    .get(hmeEo.getWorkOrderId() + "-" + eoJobSn.getShiftId() + "-" + hmeEo.getMaterialId());

            if (Objects.isNull(wkcCompleteOutputRecord) || StringUtils.isBlank(wkcCompleteOutputRecord.getWkcOutputRecordId())) {
                wkcCompleteOutputRecord = new HmeWkcCompleteOutputRecord();
                wkcCompleteOutputRecord.setTenantId(tenantId);
                wkcCompleteOutputRecord.setSiteId(dto.getSiteId());
                wkcCompleteOutputRecord.setWorkOrderId(hmeEo.getWorkOrderId());
                wkcCompleteOutputRecord.setWkcShiftId(eoJobSn.getShiftId());
                wkcCompleteOutputRecord.setMaterialId(hmeEo.getMaterialId());
                wkcCompleteOutputRecord.setWorkcellId(workcellId);
                wkcCompleteOutputRecord.setQty(Objects.isNull(hmeEo.getQty()) ? 0L : hmeEo.getQty().longValue());
                insertDataList.add(wkcCompleteOutputRecord);
            } else {
                //???????????????????????????,?????????????????????update??????
                if (updateQtyMap.containsKey(wkcCompleteOutputRecord.getWkcOutputRecordId())) {
                    Long qty = updateQtyMap.get(wkcCompleteOutputRecord.getWkcOutputRecordId())
                            + (Objects.isNull(hmeEo.getQty()) ? 0L : hmeEo.getQty().longValue());

                    updateQtyMap.put(wkcCompleteOutputRecord.getWkcOutputRecordId(), qty);
                } else {
                    updateQtyMap.put(wkcCompleteOutputRecord.getWkcOutputRecordId(),
                            Objects.isNull(hmeEo.getQty()) ? 0L : hmeEo.getQty().longValue());

                    HmeWkcCompleteOutputRecord update = new HmeWkcCompleteOutputRecord();
                    update.setTenantId(tenantId);
                    update.setWkcOutputRecordId(wkcCompleteOutputRecord.getWkcOutputRecordId());
                    update.setQty(Objects.isNull(wkcCompleteOutputRecord.getQty()) ? 0L : wkcCompleteOutputRecord.getQty());
                    updateDataList.add(update);
                }
            }
        }

        // ???????????????????????????
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(insertDataList)) {
            //????????????????????????????????????
            Map<String, List<HmeWkcCompleteOutputRecord>> groupMap = insertDataList.stream()
                    .collect(Collectors.groupingBy(item -> item.getTenantId() + "-" + item.getWorkOrderId()
                            + "-" + item.getWorkcellId() + "-" + item.getWkcShiftId() + "-" + item.getMaterialId()));

            List<String> idS = customDbRepository.getNextKeys("hme_wkc_complete_output_record_s", groupMap.size());
            List<String> cidS = customDbRepository.getNextKeys("hme_wkc_complete_output_record_cid_s", groupMap.size());
            int count = 0;
            for (Map.Entry<String, List<HmeWkcCompleteOutputRecord>> entry : groupMap.entrySet()) {
                if (CollectionUtils.isEmpty(entry.getValue()) || Objects.isNull(entry.getValue().get(0))) {
                    continue;
                }

                HmeWkcCompleteOutputRecord insertData = entry.getValue().get(0);
                insertData.setQty(entry.getValue().stream().mapToLong(HmeWkcCompleteOutputRecord::getQty).sum());
                insertData.setWkcOutputRecordId(idS.get(count));
                insertData.setCid(Long.valueOf(cidS.get(count)));
                insertData.setCreatedBy(hmeEoJobSnVO16.getUserId());
                insertData.setCreationDate(hmeEoJobSnVO16.getCurrentDate());
                insertData.setLastUpdatedBy(hmeEoJobSnVO16.getUserId());
                insertData.setLastUpdateDate(hmeEoJobSnVO16.getCurrentDate());
                sqlList.addAll(customDbRepository.getInsertSql(insertData));
                count++;
            }
        }

        if (CollectionUtils.isNotEmpty(updateDataList)) {
            List<String> cidS = customDbRepository.getNextKeys("hme_wkc_complete_output_record_cid_s",
                    updateDataList.size());
            int count = 0;
            for (HmeWkcCompleteOutputRecord updateData : updateDataList) {
                updateData.setQty(updateData.getQty() + updateQtyMap.get(updateData.getWkcOutputRecordId()));
                updateData.setCid(Long.valueOf(cidS.get(count)));
                updateData.setLastUpdatedBy(hmeEoJobSnVO16.getUserId());
                updateData.setLastUpdateDate(hmeEoJobSnVO16.getCurrentDate());
                sqlList.addAll(customDbRepository.getUpdateSql(updateData));
                count++;
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateSnJob(Long tenantId, List<HmeEoJobSnVO3> dtoList) {
        // ??????????????????
        final Date currentDate = CommonUtils.currentTimeGet();

        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        List<HmeEoJobSn> snJobList = new ArrayList<>();

        //????????????EoJobSn Id/Cid
        List<String> eoJobSnIdList = customSequence.getNextKeys("hme_eo_job_sn_s", dtoList.size());
        List<String> eoJobSnCidList = customSequence.getNextKeys("hme_eo_job_sn_cid_s", dtoList.size());
        int index = 0;
        for (HmeEoJobSnVO3 dto : dtoList) {
            HmeEoJobSn snJob = new HmeEoJobSn();
            snJob.setTenantId(tenantId);
            snJob.setJobId(eoJobSnIdList.get(index));
            snJob.setEoId(dto.getEoId());
            snJob.setSiteInDate(currentDate);
            snJob.setShiftId(dto.getWkcShiftId());
            snJob.setSiteInBy(userId);
            snJob.setWorkcellId(dto.getWorkcellId());
            snJob.setWorkOrderId(dto.getWorkOrderId());
            snJob.setOperationId(dto.getOperationId());
            snJob.setMaterialLotId(dto.getMaterialLotId());
            snJob.setSnMaterialId(dto.getMaterialId());
            snJob.setJobType(dto.getJobType());
            snJob.setEoStepId(dto.getEoStepId());
            snJob.setEoStepNum(dto.getEoStepNum());
            snJob.setReworkFlag(dto.getReworkFlag());
            snJob.setSourceContainerId(dto.getSourceContainerId());
            snJob.setJobContainerId(dto.getJobContainerId());
            //V20201004 modify by penglin.sui for lu.bai ??????SN????????????
            if (Objects.nonNull(dto.getPrepareQty())) {
                snJob.setSnQty(dto.getPrepareQty());
            }
            snJob.setCid(Long.parseLong(eoJobSnCidList.get(index)));
            snJob.setObjectVersionNumber(1L);
            snJob.setCreatedBy(userId);
            snJob.setCreationDate(currentDate);
            snJob.setLastUpdatedBy(userId);
            snJob.setLastUpdateDate(currentDate);
            snJobList.add(snJob);
            index++;
        }

        //????????????
        if (CollectionUtils.isNotEmpty(snJobList)) {
            List<List<HmeEoJobSn>> splitSqlList = CommonUtils.splitSqlList(snJobList, 200);
            for (List<HmeEoJobSn> domains : splitSqlList) {
                hmeEoJobSnMapper.batchInsert(domains);
            }
        }
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 17:27
     * @param tenantId ??????ID
     * @param materialLotIdList ??????ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeMaterialLotVO3>
     *
     */
    @Override
    public Map<String, HmeMaterialLotVO3> batchQueryMaterialLotInfo(Long tenantId, List<String> materialLotIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryMaterialLotInfo tenantId={},materialLotIdList={}", tenantId, materialLotIdList);
        //????????????????????????
        Map<String, HmeMaterialLotVO3> materialLotVO3Map = new HashMap<>();
        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoById(tenantId, materialLotIdList);
        if (CollectionUtils.isNotEmpty(materialLotList)) {
            materialLotList.forEach(item -> materialLotVO3Map.put(item.getMaterialLotId(), item));
        }

        if (materialLotVO3Map.size() != materialLotList.size() || !materialLotVO3Map.keySet().containsAll(materialLotIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }
        return materialLotVO3Map;
    }

    /**
     *
     * @Description ??????????????????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 20:29
     * @param tenantId ??????ID
     * @param workcellId ??????ID
     * @param jobIdList ??????ID
     * @return boolean
     *
     */
    @Override
    public boolean hasMissingValue(Long tenantId, String workcellId, List<String> jobIdList) {
        int count = hmeEoJobSnMapper.queryValueMissingCount(tenantId, workcellId, jobIdList);
        return count > 0;
    }

    /**
     *
     * @Description ??????????????????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 20:29
     * @param tenantId ??????ID
     * @param workcellId ??????ID
     * @param jobIdList ??????ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     *
     */
    @Override
    public List<HmeEoJobDataRecordVO> hasMissingValueTag(Long tenantId, String workcellId, List<String> jobIdList) {
        return hmeEoJobSnMapper.queryValueMissingTag(tenantId, workcellId, jobIdList);
    }

    /**
     *
     * @Description ??????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 21:07
     * @param tenantId ??????ID
     * @param operationId ??????ID
     * @param materialLotIds ??????ID
     * @return boolean
     *
     */
    @Override
    public boolean hasOtherOperationJob(Long tenantId, String operationId, List<String> materialLotIds) {
        int count = hmeEoJobSnMapper.queryOtherOperationJobCount(tenantId, operationId, materialLotIds);
        return count > 0;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 21:41
     * @param tenantId ??????ID
     * @param workcellId ??????ID
     * @return boolean
     *
     */
    @Override
    public boolean isContainerOutSite(Long tenantId, String workcellId) {
        String containerOut = hmeEoJobSnMapper.queryWkcContainerOutValue(tenantId, workcellId);
        return HmeConstants.ConstantValue.YES.equals(StringUtils.trimToEmpty(containerOut));
    }

    /**
     *
     * @Description ????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 22:24
     * @param tenantId ??????ID
     * @param hmeEoJobSnVO16 ??????
     * @param dto ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchContainerOutSite(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchContainerOutSite tenantId={},hmeEoJobSnVO16={},dto={}", tenantId, hmeEoJobSnVO16, dto);
        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");
        Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO16.getEoMap();

        List<MtContainerVO31> containerLoadList = new ArrayList<>();
        //List<MtContainerVO9> containerLoadVerifyList = new ArrayList<>();
        for (HmeEoJobSnVO3 eoJobSnVO3 : dto.getSnLineList()) {
            /*// ??????{ containerLoadBatchVerify }??????
            MtContainerVO9 mtContainerVO9 = new MtContainerVO9();
            mtContainerVO9.setContainerId(hmeEoJobSnVO16.getEoJobContainer().getContainerId());
            mtContainerVO9.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO9.setLoadObjectId(eoJobSnVO3.getMaterialLotId());
            mtContainerVO9.setTrxLoadQty(eoMap.get(eoJobSnVO3.getEoId()).getQty());
            containerLoadVerifyList.add(mtContainerVO9);*/

            // ??????{ containerBatchLoad }??????
            MtContainerVO31 mtContainerVO31 = new MtContainerVO31();
            mtContainerVO31.setContainerId(hmeEoJobSnVO16.getEoJobContainer().getContainerId());
            mtContainerVO31.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO31.setLoadObjectId(eoJobSnVO3.getMaterialLotId());
            mtContainerVO31.setTrxLoadQty(eoMap.get(eoJobSnVO3.getEoId()).getQty());
            containerLoadList.add(mtContainerVO31);
        }

        // ??????{ containerLoadBatchVerify }????????????????????????
        //mtContainerRepository.containerLoadBatchVerify(tenantId, containerLoadVerifyList);

        // ??????{ containerBatchLoad }????????????????????????
        MtContainerVO30 mtContainerVO30 = new MtContainerVO30();
        mtContainerVO30.setEventRequestId(eventRequestId);
        mtContainerVO30.setContainerLoadList(containerLoadList);
        mtContainerRepository.containerBatchLoad(tenantId, mtContainerVO30);
    }

    /**
     *
     * @Description ???????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/19 14:10
     * @param tenantId ??????ID
     * @param routerStepIdList ??????ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeRouterStepVO4>
     *
     */
    @Override
    public Map<String, HmeRouterStepVO4> batchQueryCurrentAndNextStep(Long tenantId, List<String> routerStepIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryCurrentAndNextStep tenantId={},routerStepIdList={}", tenantId, routerStepIdList);
        Map<String, HmeRouterStepVO4> routerStepMap = new HashMap<>();
        if (CollectionUtils.isEmpty(routerStepIdList)) {
            return routerStepMap;
        }

        List<HmeRouterStepVO4> routerStepList = hmeEoJobSnMapper.batchQueryCurrentAndNextStep(tenantId, routerStepIdList);
        if (CollectionUtils.isEmpty(routerStepList)) {
            return routerStepMap;
        }

        //??????????????????????????????????????????????????????
        Map<String, List<HmeRouterStepVO4>> groupMap = routerStepList.stream()
                .collect(Collectors.groupingBy(HmeRouterStepVO4::getRouterStepId));
        for (Map.Entry<String, List<HmeRouterStepVO4>> entry : groupMap.entrySet()) {
            if (CollectionUtils.isNotEmpty(entry.getValue())) {
                //??????sequence??????????????????????????????
                List<HmeRouterStepVO4> values = entry.getValue().stream().sorted(Comparator
                        .comparing(HmeRouterStepVO4::getSequence)).collect(Collectors.toList());
                routerStepMap.put(entry.getKey(), values.get(0));
            }
        }

        return routerStepMap;
    }

    @Override
    public String getAvailableTime(Long tenantId, String siteId, String materialId) {
        MtMaterialSite materialSiteParam = new MtMaterialSite();
        materialSiteParam.setTenantId(tenantId);
        materialSiteParam.setSiteId(siteId);
        materialSiteParam.setMaterialId(materialId);
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId,materialSiteParam);
        if (StringUtils.isBlank(materialSiteId)) {
            // ????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }
        MtExtendVO materialSiteExtend = new MtExtendVO();
        materialSiteExtend.setTableName("mt_material_site_attr");
        materialSiteExtend.setKeyId(materialSiteId);
        List<MtExtendAttrVO> materialSiteExtendAttr = mtExtendSettingsRepository.attrPropertyQuery(tenantId, materialSiteExtend);
        // ?????????????????????????????????
        List<MtExtendAttrVO> mtAvailableTimeAttr = materialSiteExtendAttr.stream()
                .filter(result -> "attribute9".equals(result.getAttrName()))
                .collect(Collectors.toList());
        String availableTime = "0";
        if(CollectionUtils.isNotEmpty(mtAvailableTimeAttr)){
            availableTime = mtAvailableTimeAttr.get(0).getAttrValue();
        }
        return availableTime;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getDeadLineDate(Long tenantId, String availableTime, String materialLotId) {
        //????????????????????????
        List<String> materialLotIdList = new ArrayList<>();
        materialLotIdList.add(materialLotId);
        List<String> materialLotAttrNameList = new ArrayList<>();
        materialLotAttrNameList.add("DEADLINE_DATE");
        List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_material_lot_attr","MATERIAL_LOT_ID"
                ,materialLotIdList,materialLotAttrNameList);
        Map<String,String> materialLotExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)){
            materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName,MtExtendAttrVO1::getAttrValue));
        }
        String deadLineDateStr = materialLotExtendAttrMap.getOrDefault("DEADLINE_DATE","");
        if(StringUtils.isNotBlank(deadLineDateStr)){
            return deadLineDateStr;
        }
        Date dateTime = CommonUtils.currentTimeGet();
        String currentTimeStr = DateUtil.format(dateTime, BaseConstants.Pattern.DATETIME);
        DateTime newDate3 = DateUtil.offsetMinute(dateTime, Integer.parseInt(availableTime));
        deadLineDateStr = newDate3.toString(BaseConstants.Pattern.DATETIME);
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_LOT_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventRequestId(eventRequestId);
            setEventTypeCode("MATERIAL_LOT_UPDATE");
        }});
        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
        // ?????????????????????????????????
        MtExtendVO5 enableDateAttr = new MtExtendVO5();
        enableDateAttr.setAttrName("ENABLE_DATE");
        enableDateAttr.setAttrValue(currentTimeStr);
        mtExtendVO5List.add(enableDateAttr);
        // ?????????????????????????????????
        MtExtendVO5 deadLineDateAttr = new MtExtendVO5();
        deadLineDateAttr.setAttrName("DEADLINE_DATE");
        deadLineDateAttr.setAttrValue(deadLineDateStr);
        mtExtendVO5List.add(deadLineDateAttr);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
            setKeyId(materialLotId);
            setEventId(eventId);
            setAttrs(mtExtendVO5List);
        }});
        return deadLineDateStr;
    }

    @Override
    public List<HmeEoJobSnBatchVO4> selectWoBomComponent(Long tenantId, HmeEoJobSnVO22 dto) {
        List<HmeEoJobSnBatchVO4> allComponentList = new ArrayList<>();
        //????????????????????????
        HmeEoJobSnBatchDTO hmeEoJobSnBatchDTOPara = new HmeEoJobSnBatchDTO();
        hmeEoJobSnBatchDTOPara.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobSnBatchDTOPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSnBatchDTOPara.setSiteId(dto.getSiteId());
        SecurityTokenHelper.close();
        List<HmeEoJobSnBatchVO4> woComponentList = hmeEoJobSnBatchMapper.selectWoComponent(tenantId,hmeEoJobSnBatchDTOPara);
        //???????????????
        if(CollectionUtils.isNotEmpty(woComponentList)) {
            woComponentList = woComponentList.stream().filter(item -> !"2".equals(item.getBackflushFlag())).collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(woComponentList)){
            allComponentList.addAll(woComponentList);
            List<String> woBomComponentMaterialIdList = new ArrayList<>();
            List<String> bomSubstituteGroupIdList = new ArrayList<>();
            Map<String,String> componentSubstituteMap = new HashMap<>();
            Map<String,String> componentCodeSubstituteMap = new HashMap<>();
            //????????????0???????????????
            List<HmeEoJobSnBatchVO4> woComponentList2 = woComponentList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            woComponentList2.forEach(item -> {
                item.setComponentMaterialId(item.getMaterialId());
                item.setComponentMaterialCode(item.getComponentMaterialCode());
                //??????????????????
                item.setComponentType(HmeConstants.ComponentType.A_MAIN);
                item.setIsSubstitute(HmeConstants.ConstantValue.NO);
                woBomComponentMaterialIdList.add(item.getMaterialId());
                if(StringUtils.isNotBlank(item.getBomSubstituteGroupId())) {
                    bomSubstituteGroupIdList.add(item.getBomSubstituteGroupId());
                    componentSubstituteMap.put(item.getBomSubstituteGroupId(),item.getComponentMaterialId());
                    componentCodeSubstituteMap.put(item.getBomSubstituteGroupId(),item.getComponentMaterialCode());
                }
            });
            //?????????????????????????????????
            if(CollectionUtils.isNotEmpty(woBomComponentMaterialIdList)) {
                SecurityTokenHelper.close();
                List<HmeEoJobSnBatchVO4> woSubstituteList = hmeEoJobSnBatchMapper.selectWoSubstitute(tenantId, dto.getSiteId(), woBomComponentMaterialIdList);
                if (CollectionUtils.isNotEmpty(woSubstituteList)) {
                    //???????????????
                    woSubstituteList = woSubstituteList.stream().filter(item -> !"2".equals(item.getBackflushFlag())).collect(Collectors.toList());
                    allComponentList.addAll(woSubstituteList);
                }
            }
            //????????????????????????
            List<HmeEoJobSnBatchVO4> woComponentList3 = woComponentList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) <= 0).collect(Collectors.toList());
            List<String> materialIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(woComponentList3)) {
                materialIdList = woComponentList3.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
            }
            //??????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(materialIdList) && CollectionUtils.isNotEmpty(bomSubstituteGroupIdList)) {
                List<HmeEoJobSnBatchVO5> componentSubstituteList = hmeEoJobSnBatchMapper.selectComponentSubstitute(tenantId, materialIdList, bomSubstituteGroupIdList);
                if(CollectionUtils.isNotEmpty(componentSubstituteList)){
                    woComponentList3.forEach(item->{
                        List<HmeEoJobSnBatchVO5> componentSubstituteList2 = componentSubstituteList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(componentSubstituteList2)) {
                            item.setBomSubstituteGroupId(componentSubstituteList2.get(0).getBomSubstituteGroupId());
                            item.setComponentMaterialId(componentSubstituteMap.getOrDefault(componentSubstituteList2.get(0).getBomSubstituteGroupId(),""));
                            item.setComponentMaterialCode(componentCodeSubstituteMap.getOrDefault(componentSubstituteList2.get(0).getBomSubstituteGroupId(),""));
                            //??????????????????
                            item.setComponentType(HmeConstants.ComponentType.BOM_SUBSTITUTE);
                            item.setIsSubstitute(HmeConstants.ConstantValue.YES);
                        }
                    });
                }
            }
        }
        //???????????????????????????
        if(CollectionUtils.isNotEmpty(allComponentList)){
            allComponentList = allComponentList.stream().filter(item -> StringUtils.isNotBlank(item.getComponentMaterialId())).collect(Collectors.toList());
        }
        return allComponentList;
    }

    @Override
    public List<HmeEoJobSnBatchVO4> selectEoBomComponent(Long tenantId, HmeEoJobSnBatchDTO dto) {
        List<HmeEoJobSnBatchVO4> resultVOList = new ArrayList<>();
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(dto.getEoId());
        List<String> routerStepIdList = new ArrayList<>(1);
        routerStepIdList.add(dto.getRouterStepId());
        List<String> jobIdList = new ArrayList<>();
        jobIdList.add(dto.getJobId());
        //??????EO????????????
        HmeEoJobSnBatchDTO hmeEoJobSnBatchDTOPara = new HmeEoJobSnBatchDTO();
        hmeEoJobSnBatchDTOPara.setEoId(dto.getEoId());
        hmeEoJobSnBatchDTOPara.setSiteId(dto.getSiteId());
        hmeEoJobSnBatchDTOPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSnBatchDTOPara.setSelectedCount(1);
        hmeEoJobSnBatchDTOPara.setRouterId(dto.getRouterId());
        //??????????????????????????????
        SecurityTokenHelper.close();
        List<HmeEoJobSnBatchVO4> componentReleaseList = hmeEoJobSnBatchMapper.selectComponentRelease(tenantId,hmeEoJobSnBatchDTOPara);
        //??????????????????(???????????????????????????????????????)
        componentReleaseList = componentReleaseList.stream().filter(item -> !"2".equals(item.getBackflushFlag())
                || ("2".equals(item.getBackflushFlag()) && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())))
                .collect(Collectors.toList());
        //??????????????????????????????
        List<HmeEoJobSnBatchVO4> substituteReleaseList = new ArrayList<>();
        //????????????ID
        List<String> allMaterialIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(componentReleaseList)){
            //?????????????????????
            List<HmeEoJobSnBatchVO4> componentReleaseList2 = componentReleaseList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            List<String> bomSubstituteGroupIdList = new ArrayList<>();
            Map<String,HmeEoJobSnBatchVO4> componentSubstituteMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(componentReleaseList2)) {
                List<HmeEoJobSnBatchVO4> componentReleaseList3 = componentReleaseList2.stream().filter(item -> StringUtils.isNotBlank(item.getBomSubstituteGroupId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(componentReleaseList3)) {
                    bomSubstituteGroupIdList = componentReleaseList3.stream().map(HmeEoJobSnBatchVO4::getBomSubstituteGroupId).distinct().collect(Collectors.toList());
                    componentSubstituteMap = componentReleaseList3.stream().collect(Collectors.toMap(HmeEoJobSnBatchVO4::getBomSubstituteGroupId, t -> t));
                }
                List<String> bomComponentMaterialIdList = componentReleaseList2.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
                //?????????????????????????????????
                if(CollectionUtils.isNotEmpty(bomComponentMaterialIdList)) {
                    SecurityTokenHelper.close();
                    substituteReleaseList = hmeEoJobSnBatchMapper.selectSubstituteRelease(tenantId, dto.getSiteId(), bomComponentMaterialIdList);
                    if (CollectionUtils.isNotEmpty(substituteReleaseList)) {
                        //???????????????
                        substituteReleaseList = substituteReleaseList.stream().filter(item -> !"2".equals(item.getBackflushFlag())
                                || ("2".equals(item.getBackflushFlag()) && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(substituteReleaseList)){
                            allMaterialIdList.addAll(substituteReleaseList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList()));
                        }
                    }
                }
            }

            //????????????????????????
            List<HmeEoJobSnBatchVO4> componentReleaseList3 = componentReleaseList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) <= 0).collect(Collectors.toList());
            List<String> materialIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(componentReleaseList3)) {
                materialIdList = componentReleaseList3.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
            }
            //??????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(materialIdList) && CollectionUtils.isNotEmpty(bomSubstituteGroupIdList)) {
                List<HmeEoJobSnBatchVO5> componentSubstituteList = hmeEoJobSnBatchMapper.selectComponentSubstitute(tenantId, materialIdList, bomSubstituteGroupIdList);
                if(CollectionUtils.isNotEmpty(componentSubstituteList)){
                    for (HmeEoJobSnBatchVO4 item : componentReleaseList3
                         ) {
                        List<HmeEoJobSnBatchVO5> componentSubstituteList2 = componentSubstituteList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(componentSubstituteList2)) {
                            item.setBomSubstituteGroupId(componentSubstituteList2.get(0).getBomSubstituteGroupId());
                            item.setSubstituteGroup(componentSubstituteList2.get(0).getSubstituteGroup());
                            HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4 = componentSubstituteMap.getOrDefault(componentSubstituteList2.get(0).getBomSubstituteGroupId(),null);
                            if(Objects.nonNull(hmeEoJobSnBatchVO4)) {
                                item.setComponentMaterialId(hmeEoJobSnBatchVO4.getComponentMaterialId());
                                item.setComponentMaterialCode(hmeEoJobSnBatchVO4.getComponentMaterialCode());
                            }
                            //??????????????????
                            item.setComponentType(HmeConstants.ComponentType.BOM_SUBSTITUTE);
                            item.setIsSubstitute(HmeConstants.ConstantValue.YES);
                        }
                    }
                }
            }
            //???????????????????????????
            componentReleaseList = componentReleaseList.stream().filter(item -> StringUtils.isNotBlank(item.getComponentMaterialId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(componentReleaseList)){
                //???????????????????????????
                return resultVOList;
            }
            //?????????????????????
            if(CollectionUtils.isNotEmpty(componentReleaseList)) {
                for (HmeEoJobSnBatchVO4 item : componentReleaseList
                     ) {
                    if (StringUtils.isBlank(item.getProductionType())) {
                        item.setProductionType(HmeConstants.MaterialTypeCode.LOT);
                    }
                    if (item.getComponentMaterialId().equals(item.getMaterialId())) {
                        //??????????????????
                        item.setComponentType(HmeConstants.ComponentType.A_MAIN);
                        item.setIsSubstitute(HmeConstants.ConstantValue.NO);
                    }
                }
                resultVOList.addAll(componentReleaseList);
            }
            //???????????????
            if(CollectionUtils.isNotEmpty(substituteReleaseList)){
                for (HmeEoJobSnBatchVO4 item : substituteReleaseList
                ) {
                    if(StringUtils.isBlank(item.getProductionType())){
                        item.setProductionType(HmeConstants.MaterialTypeCode.LOT);
                    }
                    //?????????????????????????????????????????????
                    List<HmeEoJobSnBatchVO4> singleComponentReleaseList = componentReleaseList.stream().filter(item2 -> item2.getComponentMaterialId().equals(item.getComponentMaterialId())).collect(Collectors.toList());
                    item.setLineNumber(singleComponentReleaseList.get(0).getLineNumber());
                    //??????????????????
                    item.setComponentType(HmeConstants.ComponentType.GLOBAL_SUBSTITUTE);
                    item.setIsSubstitute(HmeConstants.ConstantValue.YES);
                }
                resultVOList.addAll(substituteReleaseList);
            }
        }
        return resultVOList;
    }

    @Override
    public HmeEoJobSnBatchVO22 selectVritualComponent(Long tenantId, String materialLotId, String workcellId) {
        HmeEoJobSnBatchVO22 resultVO = new HmeEoJobSnBatchVO22();
        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList = new ArrayList<HmeEoJobSnLotMaterial>();
        SecurityTokenHelper.close();
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLotId)
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.PREPARE_PROCESS)).build());
        BigDecimal virtualSnQtySum = hmeEoJobSnList.stream().map(HmeEoJobSn::getSnQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            List<String> jobIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).collect(Collectors.toList());
            SecurityTokenHelper.close();
            List<HmeEoJobMaterial> hmeEoJobMaterialList2 = hmeEoJobMaterialMapper.selectVirtualComponent(tenantId, jobIdList);
            if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList2)) {
                // ??????????????????
                List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList3 = hmeEoJobMaterialList2.stream().map(material2 -> {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setIsReleased(material2.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(material2.getJobId());
                    hmeEoJobSnLotMaterial.setJobMaterialId(material2.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialId(material2.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialCode(material2.getMaterialCode());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material2.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.SN);
                    hmeEoJobSnLotMaterial.setReleaseQty(material2.getReleaseQty());
                    hmeEoJobSnLotMaterial.setWorkcellId(workcellId);
                    hmeEoJobSnLotMaterial.setLocatorId(material2.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material2.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(StringUtils.isBlank(material2.getVirtualFlag()) ? HmeConstants.ConstantValue.NO : material2.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setProductionVersion(material2.getProductionVersion());
                    hmeEoJobSnLotMaterial.setRemainQty(Objects.isNull(material2.getRemainQty()) ? BigDecimal.ZERO : material2.getRemainQty());
                    hmeEoJobSnLotMaterial.setPrimaryUomId(material2.getPrimaryUomId());
                    hmeEoJobSnLotMaterial.setPrimaryUomCode(material2.getPrimaryUomCode());
                    hmeEoJobSnLotMaterial.setSiteId(material2.getSiteId());
                    hmeEoJobSnLotMaterial.setSiteCode(material2.getSiteCode());
                    return hmeEoJobSnLotMaterial;
                }).collect(Collectors.toList());
                hmeEoJobSnLotComponentMaterialList.addAll(hmeEoJobSnLotMaterialList3);
            }
            SecurityTokenHelper.close();
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList2 = hmeEoJobSnLotMaterialMapper.selectVirtualComponent(tenantId, jobIdList);
            if (CollectionUtils.isNotEmpty(hmeEoJobSnLotComponentMaterialList2)) {
                hmeEoJobSnLotComponentMaterialList.addAll(hmeEoJobSnLotComponentMaterialList2);
            }
        }
        resultVO.setVirtualSnQtySum(virtualSnQtySum);
        resultVO.setVirtualComponentMaterialList(hmeEoJobSnLotComponentMaterialList);
        return resultVO;
    }

    /**
     *
     * @Description ??????eoId??????EO/WO??????
     *
     * @author yuchao.wang
     * @date 2020/11/21 14:38
     * @param tenantId ??????ID
     * @param eoStepId eoStepId
     * @param eoId eoId
     * @return com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO
     *
     */
    @Override
    public HmeEoJobSnSingleBasicVO queryEoAndWoInfoWithComponentByEoId(Long tenantId, String eoStepId, String eoId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryEoAndWoInfoWithComponentByEoId tenantId={},eoStepId={},eoId={}", tenantId, eoStepId, eoId);
        //????????????EO??????
        List<HmeEoVO4> eoList = hmeEoJobSnMapper.batchQueryEoInfoWithComponentById(tenantId, eoStepId, Collections.singletonList(eoId));
        if (CollectionUtils.isEmpty(eoList) || Objects.isNull(eoList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO??????"));
        }

        //????????????WO??????
        List<HmeWorkOrderVO2> woList = hmeEoJobSnMapper.batchQueryWoInfoWithComponentById(tenantId, Collections.singletonList(eoList.get(0).getWorkOrderId()));
        if (CollectionUtils.isEmpty(woList) || Objects.isNull(woList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //????????????????????????????????????
        if (StringUtils.isBlank(woList.get(0).getProdLineCode())) {
            //SN???????????????????????????????????????????????????,?????????!
            throw new MtException("HME_EO_JOB_SN_142", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_142", "HME", "????????????"));
        }

        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasicVO = new HmeEoJobSnSingleBasicVO();
        hmeEoJobSnSingleBasicVO.setEo(eoList.get(0));
        hmeEoJobSnSingleBasicVO.setWo(woList.get(0));
        return hmeEoJobSnSingleBasicVO;
    }

    /**
     *
     * @Description ????????????????????????-??????eoId??????EO/WO??????
     *
     * @author yuchao.wang
     * @date 2021/1/4 15:52
     * @param tenantId ??????ID
     * @param eoStepId eoStepId
     * @param eoId eoId
     * @param eoRouterBomRel ????????????
     * @return com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO
     *
     */
    @Override
    public HmeEoJobSnSingleBasicVO queryEoAndWoInfoWithCompByEoIdForDesignedRework(Long tenantId, String eoStepId, String eoId, HmeEoRouterBomRelVO eoRouterBomRel) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryEoAndWoInfoWithCompByEoIdForDesignedRework tenantId={},eoStepId={},eoId={},eoRouterBomRel={}",
                tenantId, eoStepId, eoId, eoRouterBomRel);
        //????????????EO??????
        List<HmeEoVO4> eoList = hmeEoJobSnMapper.batchQueryEoInfoWithCompByIdForDesignedRework(tenantId, eoStepId, eoId, eoRouterBomRel.getBomId());
        if (CollectionUtils.isEmpty(eoList) || Objects.isNull(eoList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO??????"));
        }
        HmeEoVO4 eo = eoList.get(0);
        eo.setBomId(eoRouterBomRel.getBomId());
        eo.setRouterId(eoRouterBomRel.getRouterId());

        //????????????WO??????
        List<HmeWorkOrderVO2> woList = hmeEoJobSnMapper.batchQueryWoInfoWithComponentById(tenantId, Collections.singletonList(eo.getWorkOrderId()));
        if (CollectionUtils.isEmpty(woList) || Objects.isNull(woList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //????????????????????????????????????
        if (StringUtils.isBlank(woList.get(0).getProdLineCode())) {
            //SN???????????????????????????????????????????????????,?????????!
            throw new MtException("HME_EO_JOB_SN_142", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_142", "HME", "????????????"));
        }

        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasicVO = new HmeEoJobSnSingleBasicVO();
        hmeEoJobSnSingleBasicVO.setEo(eo);
        hmeEoJobSnSingleBasicVO.setWo(woList.get(0));
        return hmeEoJobSnSingleBasicVO;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/21 15:03
     * @param tenantId ??????ID
     * @param materialLotId ??????ID
     * @return com.ruike.hme.domain.vo.HmeMaterialLotVO3
     *
     */
    @Override
    public HmeMaterialLotVO3 queryMaterialLotInfo(Long tenantId, String materialLotId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryMaterialLotInfo tenantId={},materialLotId={}", tenantId, materialLotId);
        //????????????EO??????
        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoById(tenantId, Collections.singletonList(materialLotId));
        if (CollectionUtils.isEmpty(materialLotList) || Objects.isNull(materialLotList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }
        return materialLotList.get(0);
    }

    /**
     *
     * @Description ?????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/21 18:05
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnSingleBasic ??????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WmsObjectTransactionResponseVO> mainOutSite(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSite tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeRouterStepVO nearStep = hmeEoJobSnSingleBasic.getNearStep();
        List<WmsObjectTransactionResponseVO> resultTransactionResponseList = new ArrayList<>();

        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            // ???????????????eo?????? ???????????????????????????
            boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
            hmeEoJobSnSingleBasic.setNewMaterialLotFlag(newMaterialLotFlag);
        }
        // ??????????????????
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        // API completeProcess
        MtEoRouterActualVO19 eoRouterActualCompleteParam = new MtEoRouterActualVO19();
        eoRouterActualCompleteParam.setWorkcellId(dto.getWorkcellId());
        eoRouterActualCompleteParam.setQty(eo.getQty());
        eoRouterActualCompleteParam.setEoStepActualId(nearStep.getEoStepActualId());
        eoRouterActualCompleteParam.setEventRequestId(completedEventRequestId);
        eoRouterActualCompleteParam.setSourceStatus("WORKING");
        mtEoRouterActualRepository.completeProcess(tenantId, eoRouterActualCompleteParam);

        //???????????????????????????,?????????????????????????????????????????????
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getDoneStepFlag())) {
            // 20210402 add by sanfeng.zhang for fang.pan ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (StringUtils.isBlank(dto.getWorkOrderId())) {
                //?????????????????????,?????????!
                throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_REWORK_0002", "HME"));
            }
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
            HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, mtWorkOrder.getWorkOrderNum());
            Boolean isInternal = false;
            // ????????????????????????????????????????????????
            if (!Objects.isNull(splitRecord)) {
                //?????????????????????????????????
                if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())) {
                    isInternal = true;
                }
            }
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag()) && isInternal) {
                resultTransactionResponseList = self().mainOutSiteForInternalReworkProcess(tenantId, dto, hmeEoJobSnSingleBasic, splitRecord);
            } else {
                resultTransactionResponseList = self().mainOutSiteForDoneStep(tenantId, dto, hmeEoJobSnSingleBasic);
            }
        } else if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            self().mainOutSiteForNormalStepForReworkProcess(tenantId, dto, hmeEoJobSnSingleBasic);
        } else {
            self().mainOutSiteForNormalStep(tenantId, dto, hmeEoJobSnSingleBasic);
        }

        //??????????????????
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //????????????????????????
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // ??????????????????
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // ??????{ containerLoad }??????????????????
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //????????????
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma ??????????????????
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsRecordLabCode()) && StringUtils.isNotBlank(dto.getLabCode())) {
            HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
            hmeMaterialLotLabCodePara.setRouterStepId(dto.getEoStepId());
            hmeMaterialLotLabCodePara.setLabCode(dto.getLabCode());
            HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
            if (Objects.isNull(hmeMaterialLotLabCode)) {
                HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                insertHmeMaterialLotLabCode.setTenantId(tenantId);
                insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                insertHmeMaterialLotLabCode.setLabCode(dto.getLabCode());
                insertHmeMaterialLotLabCode.setJobId(dto.getJobId());
                insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                insertHmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
                insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                insertHmeMaterialLotLabCode.setRouterStepId(dto.getEoStepId());
                insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeMaterialLotLabCodeRepository.insertSelective(insertHmeMaterialLotLabCode);
            } else {
                if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                    HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                    updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    hmeMaterialLotLabCodeMapper.updateByPrimaryKeySelective(updateHmeMaterialLotLabCode);
                }
            }
        }

        //V20210225 modify by penglin.sui for jian.zhang ????????????????????????/??????
        if(StringUtils.isNotBlank(dto.getInspection())){
            HmeEoJobSn hmeEoJobSnPara = new HmeEoJobSn();
            hmeEoJobSnPara.setJobId(dto.getJobId());
            hmeEoJobSnPara.setAttribute2(dto.getInspection());
            hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSnPara);
        }

        // ?????? ??????job_id ????????????
        if (hmeEoJobSnCommonService.isFirstProcess(tenantId, dto.getOperationId())) {
            String eventCreateId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("HME_FIRST_SITE_OUT_JOB");
            }});

            List<MtExtendVO5> dtoList = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("SX_PROCESS");
            mtExtendVO5.setAttrValue(dto.getJobId());
            dtoList.add(mtExtendVO5);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(), eventCreateId, dtoList);
        }
        // ???????????? ????????????????????? ????????????????????????????????????JCRETEST
        if (HmeConstants.ConstantValue.YES.equals(dto.getCrossRetestFlag())) {
            // ?????????????????????????????????Y ???Y??? ??????????????????Y
            // ????????????????????? ?????????????????????????????????????????????
            String materialLotId = dto.getMaterialLotId();
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
                // ???????????????eo?????? ???????????????????????????
                if (!hmeEoJobSnSingleBasic.getNewMaterialLotFlag()) {
                    List<String> newMaterialLotIds = hmeEoJobSnReWorkMapper.queryNewMaterialLotByOldMaterialLot(tenantId, materialLotId);
                    if (CollectionUtils.isEmpty(newMaterialLotIds)) {
                        throw new MtException("HME_REPAIR_SN_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_REPAIR_SN_0008", "HME"));
                    }
                    materialLotId = newMaterialLotIds.get(0);
                }
            }
            String finalMaterialLotId = materialLotId;
            // ????????????????????????
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(finalMaterialLotId);
                setTableName("mt_material_lot_attr");
                setAttrName("JCRETEST");
            }});
            String jcRetest = CollectionUtils.isNotEmpty(mtExtendAttrVOS) ? mtExtendAttrVOS.get(0).getAttrValue() : "";
            // ??????????????????????????????
            String eventCreateId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setEventTypeCode("SN_CROSS_TEST_COMPLETED");
                }
            });
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 jcRetestAttr = new MtExtendVO5();
            jcRetestAttr.setAttrName("JCRETEST");
            jcRetestAttr.setAttrValue("");
            attrList.add(jcRetestAttr);
            if (HmeConstants.ConstantValue.YES.equals(jcRetest)) {
                MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
                reworkFlagAttr.setAttrName("REWORK_FLAG");
                reworkFlagAttr.setAttrValue(HmeConstants.ConstantValue.YES);
                attrList.add(reworkFlagAttr);
            }
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setEventId(eventCreateId);
            mtExtendVO10.setKeyId(finalMaterialLotId);
            mtExtendVO10.setAttrs(attrList);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        }

        //??????????????????
        autoJudgeNc(tenantId , dto , hmeEoJobSnSingleBasic);

        return resultTransactionResponseList;
    }

    @Override
    public List<WmsObjectTransactionResponseVO> mainOutSite2(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSite tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeRouterStepVO nearStep = hmeEoJobSnSingleBasic.getNearStep();
        List<WmsObjectTransactionResponseVO> resultTransactionResponseList = new ArrayList<>();

        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            // ???????????????eo?????? ???????????????????????????
            boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
            hmeEoJobSnSingleBasic.setNewMaterialLotFlag(newMaterialLotFlag);
        }

        // ??????????????????
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        // API completeProcess
        MtEoRouterActualVO19 eoRouterActualCompleteParam = new MtEoRouterActualVO19();
        eoRouterActualCompleteParam.setWorkcellId(dto.getWorkcellId());
        eoRouterActualCompleteParam.setQty(eo.getQty());
        eoRouterActualCompleteParam.setEoStepActualId(nearStep.getEoStepActualId());
        eoRouterActualCompleteParam.setEventRequestId(completedEventRequestId);
        eoRouterActualCompleteParam.setSourceStatus("WORKING");
        mtEoRouterActualRepository.completeProcess(tenantId, eoRouterActualCompleteParam);

        //???????????????????????????,?????????????????????????????????????????????
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getDoneStepFlag())) {
            // 20210402 add by sanfeng.zhang for fang.pan ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (StringUtils.isBlank(dto.getWorkOrderId())) {
                //?????????????????????,?????????!
                throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_REWORK_0002", "HME"));
            }
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
            HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, mtWorkOrder.getWorkOrderNum());
            Boolean isInternal = false;
            // ????????????????????????????????????????????????
            if (!Objects.isNull(splitRecord)) {
                //?????????????????????????????????
                if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())) {
                    isInternal = true;
                }
            }
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag()) && isInternal) {
                resultTransactionResponseList = self().mainOutSiteForInternalReworkProcess(tenantId, dto, hmeEoJobSnSingleBasic, splitRecord);
            } else {
                resultTransactionResponseList = self().mainOutSiteForDoneStep(tenantId, dto, hmeEoJobSnSingleBasic);
            }
        } else if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            self().mainOutSiteForNormalStepForReworkProcess(tenantId, dto, hmeEoJobSnSingleBasic);
        } else {
            self().mainOutSiteForNormalStep(tenantId, dto, hmeEoJobSnSingleBasic);
        }

        //??????????????????
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //????????????????????????
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // ??????????????????
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // ??????{ containerLoad }??????????????????
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //????????????
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma ??????????????????
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsRecordLabCode()) && StringUtils.isNotBlank(dto.getLabCode())) {
            HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
            hmeMaterialLotLabCodePara.setRouterStepId(dto.getEoStepId());
            hmeMaterialLotLabCodePara.setLabCode(dto.getLabCode());
            HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
            if (Objects.isNull(hmeMaterialLotLabCode)) {
                HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                insertHmeMaterialLotLabCode.setTenantId(tenantId);
                insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                insertHmeMaterialLotLabCode.setLabCode(dto.getLabCode());
                insertHmeMaterialLotLabCode.setJobId(dto.getJobId());
                insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                insertHmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
                insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                insertHmeMaterialLotLabCode.setRouterStepId(dto.getEoStepId());
                insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeMaterialLotLabCodeRepository.insertSelective(insertHmeMaterialLotLabCode);
            } else {
                if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                    HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                    updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    hmeMaterialLotLabCodeMapper.updateByPrimaryKeySelective(updateHmeMaterialLotLabCode);
                }
            }
        }

        //V20210225 modify by penglin.sui for jian.zhang ????????????????????????/??????
        if(StringUtils.isNotBlank(dto.getInspection())){
            HmeEoJobSn hmeEoJobSnPara = new HmeEoJobSn();
            hmeEoJobSnPara.setJobId(dto.getJobId());
            hmeEoJobSnPara.setAttribute2(dto.getInspection());
            hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSnPara);
        }

        // ????????????????????????
        String eventCreateId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventTypeCode("SN_CROSS_TEST");
            }
        });

        List<MtExtendVO5> attrList = new ArrayList<>();
        MtExtendVO5 jcRetestAttr = new MtExtendVO5();
        jcRetestAttr.setAttrName("JCRETEST");
        if (HmeConstants.ConstantValue.YES.equals(dto.getReworkFlag())) {
            jcRetestAttr.setAttrValue(HmeConstants.ConstantValue.YES);
        } else {
            jcRetestAttr.setAttrValue(HmeConstants.ConstantValue.NO);
        }
        attrList.add(jcRetestAttr);
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
        mtExtendVO10.setEventId(eventCreateId);
        // ????????????????????? ????????????????????????
        String materialLotId = dto.getMaterialLotId();
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            // ???????????????eo?????? ???????????????????????????
            if (!hmeEoJobSnSingleBasic.getNewMaterialLotFlag()) {
                List<String> newMaterialLotIds = hmeEoJobSnReWorkMapper.queryNewMaterialLotByOldMaterialLot(tenantId, materialLotId);
                if (CollectionUtils.isEmpty(newMaterialLotIds)) {
                    throw new MtException("HME_REPAIR_SN_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_REPAIR_SN_0008", "HME"));
                }
                materialLotId = newMaterialLotIds.get(0);
            }
        }
        mtExtendVO10.setKeyId(materialLotId);
        mtExtendVO10.setAttrs(attrList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

        //????????????
        launchNc(tenantId, dto, hmeEoJobSnSingleBasic);

        return resultTransactionResponseList;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/21 22:42
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnSingleBasic ??????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WmsObjectTransactionResponseVO> mainOutSiteForDoneStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDoneStep tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();

        MtModLocator mtModLocator = new MtModLocator();
        //????????????????????????????????????SN????????????????????????????????????WKC????????????WKC????????????????????????
        // ???????????????WKC????????????WKC?????????????????????????????????????????????????????????WKC????????????WKC???????????????????????????
        // ????????????WKC????????????WKC?????????????????????
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDoneStep  ReworkProcessFlag()  "+hmeEoJobSnSingleBasic.getReworkProcessFlag() );
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            // ????????????WKC????????????WKC?????????????????????
            MtModLocator completeLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, "28", dto.getWorkcellId());

            if (ObjectUtil.isNotNull(completeLocator)) {
                BeanUtils.copyProperties(completeLocator, mtModLocator);
            } else {
                //????????????????????????WKC????????????WKC??????????????????????????????????????????WKC????????????WKC???????????????????????????
                MtModLocator defaultLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
                BeanUtils.copyProperties(defaultLocator, mtModLocator);
            }
        } else {
            //?????????????????? ??????????????????WKC????????????WKC???????????????????????????
            MtModLocator defaultLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
            BeanUtils.copyProperties(defaultLocator, mtModLocator);
        }
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDoneStep  mtModLocator={}  "+  mtModLocator);

        //???????????????????????????
        MtModLocator warehouse = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtModLocator.getLocatorId());

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????
        String changeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setEventTypeCode("HOME_MADE_PRODUCTION_REWORK_COMPLETE");
            }
        });

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });
        //????????????
        String onhandIncreaseEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("INV_ONHAND_INCREASE");
            }
        });
        //??????????????????
        String eoCompleteEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setWorkcellId(dto.getWorkcellId());
                setEventTypeCode("HME_EO_COMPLETE");
            }
        });

        //????????????????????????
        Map<String, String> moveTypeMap = getMoveTypeForMainOutSite(tenantId);

        //??????????????????????????????-INV_ONHAND_INCREASE??????
        MtMaterialLotVO2 mtLotOnhandUpdate = new MtMaterialLotVO2();
        mtLotOnhandUpdate.setEventId(onhandIncreaseEventId);
        mtLotOnhandUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotOnhandUpdate.setLot(StringUtils.trimToEmpty(hmeEoJobSnSingleBasic.getLotCode()));
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotOnhandUpdate, HmeConstants.ConstantValue.NO);

        // ???????????????
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(eo.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(mtModLocator.getLocatorId());
        // 20211119 modify by sanfeng.zhang for wenxin.zhang ??????????????? ??????????????? ??????????????????EO??????????????? ???????????????
        mtInvOnhandQuantityVO9.setMaterialId(dto.getSnMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(eo.getQty());
        mtInvOnhandQuantityVO9.setEventId(onhandIncreaseEventId);
        mtInvOnhandQuantityVO9.setLotCode(StringUtils.trimToEmpty(hmeEoJobSnSingleBasic.getLotCode()));
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????NG
            if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag()) && HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            // 20210812 add by sanfeng.zhang for tianyang.xie ?????????????????? ???????????????????????????????????????????????????
            // ????????????EO?????????????????? ?????? ??????????????? ???????????????
            Boolean newMaterialLotFlag = hmeEoJobSnSingleBasic.getNewMaterialLotFlag();
            if (!newMaterialLotFlag) {
                List<String> newMaterialLotIdList = hmeEoJobSnReWorkMapper.queryNewMaterialLotByOldMaterialLot(tenantId, dto.getMaterialLotId());
                // ???????????????????????????ID
                if (CollectionUtils.isEmpty(newMaterialLotIdList)) {
                    throw new MtException("HME_REPAIR_SN_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_REPAIR_SN_0008", "HME"));
                }
                // ???????????????
                List<MtMaterialLotVO20> materialLotList = new ArrayList<>();
                for (String newMaterialLotId : newMaterialLotIdList) {
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(newMaterialLotId);
                    mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.NO);
                    mtMaterialLotVO20.setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
                    materialLotList.add(mtMaterialLotVO20);
                }
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, changeEventId, HmeConstants.ConstantValue.NO);

                // ???????????????????????????????????? ???????????????????????????
                if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
                    List<MtExtendVO5> dtoList = new ArrayList<>();
                    MtExtendVO5 inFlagAttr = new MtExtendVO5();
                    inFlagAttr.setAttrName("OLD_BARCODE_IN_FLAG");
                    inFlagAttr.setAttrValue("");
                    dtoList.add(inFlagAttr);
                    mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(), changeEventId, dtoList);
                }
            }
        }

        //??????????????????????????????????????????-INV_ONHAND_INCREASE??????
        List<MtExtendVO5> mtLotExtendOnhandList = new ArrayList<>();
        MtExtendVO5 mfFlagExtend = new MtExtendVO5();
        mfFlagExtend.setAttrName("MF_FLAG");
        mfFlagExtend.setAttrValue("");
        mtLotExtendOnhandList.add(mfFlagExtend);
        //V20210525 modify by penglin.sui for fang.pan ??????????????????????????????????????????????????????????????????????????????
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            String completeProductionVersion = "";
            if (Objects.nonNull(hmeEoJobSnSingleBasic.getMaterialLot())) {
                completeProductionVersion = hmeEoJobSnSingleBasic.getMaterialLot().getCompleteProductionVersion();
            }
            if (StringUtils.isBlank(completeProductionVersion)) {
                MtExtendVO5 materialVersionExtend = new MtExtendVO5();
                materialVersionExtend.setAttrName("MATERIAL_VERSION");
                materialVersionExtend.setAttrValue(wo.getProductionVersion());
                mtLotExtendOnhandList.add(materialVersionExtend);
            }
        }else{
            MtExtendVO5 materialVersionExtend = new MtExtendVO5();
            materialVersionExtend.setAttrName("MATERIAL_VERSION");
            materialVersionExtend.setAttrValue(wo.getProductionVersion());
            mtLotExtendOnhandList.add(materialVersionExtend);
        }
        MtExtendVO10 materialLotAttrOnhandUpdate = new MtExtendVO10();
        materialLotAttrOnhandUpdate.setEventId(onhandIncreaseEventId);
        materialLotAttrOnhandUpdate.setKeyId(dto.getMaterialLotId());
        materialLotAttrOnhandUpdate.setAttrs(mtLotExtendOnhandList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotAttrOnhandUpdate);

        //??????????????????????????????????????????-EO_WKC_STEP_COMPLETE??????
        List<MtExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
        MtExtendVO5 statusExtend = new MtExtendVO5();
        statusExtend.setAttrName("STATUS");
        statusExtend.setAttrValue("COMPLETED");
        mtLotExtendCompleteList.add(statusExtend);
        // ??????????????????-?????? ???????????????????????????????????? ????????????????????????
        String soNum = wo.getSoNum();
        String soLineNum = wo.getSoLineNum();
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag()) && HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            // ???????????????????????? ?????????????????????
            // ?????????EO ?????????????????????????????????
            HmeWorkOrderVO2 workOrderSoNum = hmeEoJobSnReWorkMapper.querySourceMaterialLotSoNum(tenantId, dto.getEoId());
            if (workOrderSoNum != null) {
                if (StringUtils.isBlank(wo.getSoNum())) {
                    soNum = workOrderSoNum.getSoNum();
                }
                if (StringUtils.isBlank(wo.getSoLineNum())) {
                    soLineNum = workOrderSoNum.getSoLineNum();
                }
            }
        }
        // ??????????????????????????? ???????????????
        hmeEoJobSnSingleBasic.getWo().setSoNum(soNum);
        hmeEoJobSnSingleBasic.getWo().setSoLineNum(soLineNum);
        MtExtendVO5 soNumExtend = new MtExtendVO5();
        soNumExtend.setAttrName("SO_NUM");
        soNumExtend.setAttrValue(soNum);
        mtLotExtendCompleteList.add(soNumExtend);
        MtExtendVO5 soLineNumExtend = new MtExtendVO5();
        soLineNumExtend.setAttrName("SO_LINE_NUM");
        soLineNumExtend.setAttrValue(soLineNum);
        mtLotExtendCompleteList.add(soLineNumExtend);
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            MtExtendVO5 reworkFlag = new MtExtendVO5();
            reworkFlag.setAttrName("REWORK_FLAG");
            reworkFlag.setAttrValue("");
            mtLotExtendCompleteList.add(reworkFlag);
        }
        MtExtendVO10 materialLotAttrCompleteUpdate = new MtExtendVO10();
        materialLotAttrCompleteUpdate.setEventId(completeEventId);
        materialLotAttrCompleteUpdate.setKeyId(dto.getMaterialLotId());
        materialLotAttrCompleteUpdate.setAttrs(mtLotExtendCompleteList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotAttrCompleteUpdate);

        //??????SN??????
        int count = hmeWoSnRelRepository.selectCountByCondition(Condition.builder(HmeWoSnRel.class)
                .andWhere(Sqls.custom().andEqualTo(HmeWoSnRel.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeWoSnRel.FIELD_SITE_ID, eo.getSiteId())
                        .andEqualTo(HmeWoSnRel.FIELD_WORK_ORDER_NUM, wo.getWorkOrderNum())
                        .andEqualTo(HmeWoSnRel.FIELD_SN_NUM,eo.getIdentification())).build());
        if(count == 0) {
            HmeWoSnRel hmeWoSnRel = new HmeWoSnRel();
            hmeWoSnRel.setTenantId(tenantId);
            hmeWoSnRel.setSiteId(eo.getSiteId());
            hmeWoSnRel.setWorkOrderNum(wo.getWorkOrderNum());
            hmeWoSnRel.setSnNum(eo.getIdentification());
            hmeWoSnRelRepository.insert(hmeWoSnRel);
        }

        //V20210302 modify by penglin.sui for fang.pan ????????????????????????????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getMaterialLot().getAfFlag())) {
                HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryLastSplitRecordOfMaterialLot2(tenantId, hmeEoJobSnSingleBasic.getMaterialLot().getMaterialLotId());
                if (Objects.nonNull(hmeServiceSplitRecord)) {
                    //????????????
                    String currTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(CommonUtils.currentTimeGet());

                    HmeServiceSplitRecord hmeServiceSplitRecordPara = new HmeServiceSplitRecord();
                    hmeServiceSplitRecordPara.setSplitRecordId(hmeServiceSplitRecord.getSplitRecordId());
                    hmeServiceSplitRecordPara.setSplitStatus(HmeConstants.SplitStatus.REPAIR_COMPLETE);
                    hmeServiceSplitRecordPara.setAttribute2(currTime);
                    hmeServiceSplitRecordMapper.updateByPrimaryKeySelective(hmeServiceSplitRecordPara);
                    if (StringUtils.isNotBlank(hmeServiceSplitRecord.getServiceReceiveId()) && StringUtils.equals(hmeServiceSplitRecord.getTopSplitRecordId(), hmeServiceSplitRecord.getSplitRecordId())) {

                        HmeServiceReceive hmeServiceReceivePara = new HmeServiceReceive();
                        hmeServiceReceivePara.setServiceReceiveId(hmeServiceSplitRecord.getServiceReceiveId());
                        hmeServiceReceivePara.setReceiveStatus(HmeConstants.ReceiveStatus.REPAIR_COMPLETE);
                        hmeServiceReceivePara.setAttribute2(currTime);
                        hmeServiceReceiveMapper.updateByPrimaryKeySelective(hmeServiceReceivePara);

                        //????????????
                        HmeServiceReceive hmeServiceReceive = hmeServiceReceiveMapper.selectByPrimaryKey(hmeServiceSplitRecord.getServiceReceiveId());

                        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                        eventCreateVO.setEventTypeCode(HmeConstants.EventType.HME_AF_REPAIR_COMPLETE);
                        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                        HmeServiceReceiveHis hmeServiceReceiveHisPara = new HmeServiceReceiveHis();
                        BeanUtils.copyProperties(hmeServiceReceive, hmeServiceReceiveHisPara);
                        hmeServiceReceiveHisPara.setEventId(eventId);
                        hmeServiceReceiveHisRepository.insertSelective(hmeServiceReceiveHisPara);
                    }
                }
            }
        }

        //????????????/????????????
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = getTransactionRequestVOForSingleList(tenantId,
                eoCompleteEventId, dto, hmeEoJobSnSingleBasic, mtModLocator, warehouse, moveTypeMap);

        //??????????????????
        List<WmsObjectTransactionResponseVO> transactionResponseList = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

        //?????????????????????????????????
        Map<String, HmeWorkOrderVO2> woMap = new HashMap<>();
        woMap.put(wo.getWorkOrderId(), wo);
        List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOList = getErpReducedSettleRadioUpdateDTOList(tenantId, woMap);

        //???????????????????????????
        if (CollectionUtils.isNotEmpty(erpReducedSettleRadioUpdateDTOList)) {
            try {
                itfWorkOrderIfaceService.erpReducedSettleRadioUpdateRestProxy(tenantId, erpReducedSettleRadioUpdateDTOList);
            } catch (Exception e) {
                log.error("=================?????????????????????????????????================={}:" + e.getMessage(), erpReducedSettleRadioUpdateDTOList);
            }
        }

        return transactionResponseList;
    }

    private Boolean isRepairWorkOrder (Long tenantId, String eoId) {
        Boolean repairWorkOrderFlag = false;
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
        if (mtEo != null) {
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
            if (StringUtils.equals("RK10", mtWorkOrder.getWorkOrderType())) {
                repairWorkOrderFlag = true;
            }
        }
        return repairWorkOrderFlag;
    }

    /**
     *
     * @Description ?????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/22 0:45
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnSingleBasic ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mainOutSiteForNormalStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForNormalStep tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();

        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????NG
            if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //??????????????????????????????????????????
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())
                && StringUtils.isNotBlank(materialLot.getReworkFlag())) {
            //??????????????????????????????????????????-EO_WKC_STEP_COMPLETE??????
            List<MtExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
            MtExtendVO5 reworkFlag = new MtExtendVO5();
            reworkFlag.setAttrName("REWORK_FLAG");
            reworkFlag.setAttrValue("");
            mtLotExtendCompleteList.add(reworkFlag);
            MtExtendVO10 materialLotAttrCompleteUpdate = new MtExtendVO10();
            materialLotAttrCompleteUpdate.setEventId(completeEventId);
            materialLotAttrCompleteUpdate.setKeyId(dto.getMaterialLotId());
            materialLotAttrCompleteUpdate.setAttrs(mtLotExtendCompleteList);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotAttrCompleteUpdate);
        }
    }

    /**
     *
     * @Description ????????????????????????-??????????????????/PDA??????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/22 11:35
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnSingleBasic ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wkcCompleteOutputRecord(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.wkcCompleteOutputRecord tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        //???????????????
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag())) {
            return;
        }

        //????????????
        MtModOrganizationVO2 lineParam = new MtModOrganizationVO2();
        lineParam.setTopSiteId(dto.getSiteId());
        lineParam.setOrganizationId(dto.getWorkcellId());
        lineParam.setOrganizationType("WORKCELL");
        lineParam.setParentOrganizationType("WORKCELL");
        lineParam.setQueryType("TOP");
        List<MtModOrganizationItemVO> lineVOList = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, lineParam);
        if (CollectionUtils.isEmpty(lineVOList) || Objects.isNull(lineVOList.get(0))) {
            // ????????????Wkc??????????????????
            throw new MtException("HME_EO_JOB_SN_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_023", "HME"));
        }
        String workcellId = lineVOList.get(0).getOrganizationId();

        //???????????????????????????????????????????????????
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        List<HmeEoJobSnDTO3> hmeEoJobSnDTO3s = hmeEoJobSnMapper.queryUnfinishedSection(tenantId, eo.getEoId(), dto.getSiteId());

        //?????????????????????????????????
        if(CollectionUtils.isNotEmpty(hmeEoJobSnDTO3s)) {
            List<String> workcellIdList = hmeEoJobSnDTO3s.stream()
                    .filter(t -> !HmeConstants.ConstantValue.YES.equals(t.getPrepareFlag()))
                    .map(HmeEoJobSnDTO3::getWorkcellId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(workcellIdList) && workcellIdList.contains(workcellId)) {
                return;
            }
        }

        //??????/??????????????????????????????
        HmeWkcCompleteOutputRecord queryParam = new HmeWkcCompleteOutputRecord();
        queryParam.setTenantId(tenantId);
        queryParam.setWorkOrderId(eo.getWorkOrderId());
        queryParam.setWkcShiftId(hmeEoJobSnSingleBasic.getHmeEoJobSnEntity().getShiftId());
        queryParam.setMaterialId(eo.getMaterialId());
        queryParam.setWorkcellId(workcellId);
        HmeWkcCompleteOutputRecord wkcCompleteOutputRecord = hmeWkcCompleteOutputRecordRepository.selectOne(queryParam);

        if(Objects.isNull(wkcCompleteOutputRecord) || StringUtils.isBlank(wkcCompleteOutputRecord.getWkcOutputRecordId())){
            queryParam.setSiteId(dto.getSiteId());
            queryParam.setQty(Objects.isNull(eo.getQty()) ? 0L : eo.getQty().longValue());
            hmeWkcCompleteOutputRecordRepository.insertSelective(queryParam);
        } else {
            Long qty = (Objects.isNull(wkcCompleteOutputRecord.getQty()) ? 0L : wkcCompleteOutputRecord.getQty())
                    + (Objects.isNull(eo.getQty()) ? 0L : eo.getQty().longValue());

            HmeWkcCompleteOutputRecord update = new HmeWkcCompleteOutputRecord();
            update.setWkcOutputRecordId(wkcCompleteOutputRecord.getWkcOutputRecordId());
            update.setQty(qty);
            hmeWkcCompleteOutputRecordMapper.updateByPrimaryKeySelective(update);
        }
    }

    /**
     *
     * @Description ???????????????????????????????????????
     *
     * @author penglin.sui
     * @date 2021/5/19 17:39
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnSingleBasic ??????
     * @return void
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void autoJudgeNc(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic){

        if(!"HME_EO_JOB_SN_172".equals(dto.getErrorCode()) || CollectionUtils.isEmpty(dto.getProcessNcDetailList())){
            return;
        }

        HmeEoJobDataRecordVO2 hmeEoJobDataRecordVO2 = hmeEoJobSnSingleBasic.getNcJobDataRecordMap().getOrDefault(dto.getJobId() , null);
        if(Objects.isNull(hmeEoJobDataRecordVO2)){
            return;
        }

        StringBuilder ncCodes = new StringBuilder();
        List<String> ncCodeIdList = new ArrayList<>();
        boolean singleFlag = true;
        String reworkFlag = HmeConstants.ConstantValue.NO;
        String downGradeFlag = HmeConstants.ConstantValue.NO;
        //V20210519 modify by penglin.sui for peng.zhao ???????????????????????????????????????????????????
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
            ncCodeIdList.add(item.getNcCodeId());
            reworkFlag = HmeConstants.ConstantValue.YES.equals(reworkFlag) ? reworkFlag : item.getReworkFlag();
            downGradeFlag = HmeConstants.ConstantValue.YES.equals(downGradeFlag) ? downGradeFlag : item.getDowngradeFlag();
        }
        // ??????????????????
        ncCodeIdList = ncCodeIdList.stream().distinct().collect(Collectors.toList());

        if(StringUtils.isBlank(ncGroupId)){
            //???${1}???????????????????????????,?????????????????????
            throw new MtException("HME_EO_JOB_SN_197", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_197", "HME", ncCodes.toString()));
        }

        if(ncCodes.length() > 0){
            //R:???????????? N:???????????? ND:???????????? + ?????? ZD:????????????????????????
            String flag = "N";
            if(singleFlag){
                if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                    flag = "R";
                }else if(HmeConstants.ConstantValue.YES.equals(downGradeFlag)){
                    flag = "ND";
                } else {
                    // ????????????????????????????????????
                    String routerId = this.getRouteByNcCode(tenantId, ncGroupId, ncCodeIdList, dto);
                    if (StringUtils.isNotBlank(routerId)) {
                        flag = "ZD";
                    }
                }
            }else{
                if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                    flag = "R";
                }
            }
            // ??????????????? ?????????????????? ??????????????????
            if (hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId())) {
                flag = "N";

                // ????????????????????????
                List<HmeEoJobDataRecordVO2> hmeEoJobDataRecordList = hmeEoJobDataRecordMapper.queryJudgmentRecord(tenantId, dto.getJobId());
                log.info("<============hmeEoJobDataRecordList============>" + JSONArray.toJSONString(hmeEoJobDataRecordList));
                List<HmeDataRecordExtend> hmeDataRecordExtendList = new ArrayList<>();
                hmeEoJobDataRecordList.forEach(record -> {
                    HmeDataRecordExtend hmeDataRecordExtend = new HmeDataRecordExtend();
                    // ?????????????????? ???????????????????????????
                    BigDecimal result = BigDecimal.valueOf(Double.valueOf(record.getResult()));
                    if (result.compareTo(record.getStandardValue()) >= 0 ) {
                        // ??????????????? ?????????
                        hmeDataRecordExtend.setCosStatus("OK");
                    } else if (result.compareTo(record.getStandardValue()) < 0 && result.compareTo(record.getMinimumValue()) >= 0) {
                        hmeDataRecordExtend.setCosStatus("RELEASE");
                    } else if (result.compareTo(record.getMinimumValue()) < 0) {
                        hmeDataRecordExtend.setCosStatus("REWORK");
                    }
                    hmeDataRecordExtend.setDataRecordExtendId(record.getDataRecordExtendId());
                    hmeDataRecordExtendList.add(hmeDataRecordExtend);
                });
                log.info("<============hmeDataRecordExtendList============>" + JSONArray.toJSONString(hmeDataRecordExtendList));
                if (CollectionUtils.isNotEmpty(hmeDataRecordExtendList)) {
                    Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
                    hmeDataRecordExtendMapper.batchUpdateStandardValue(tenantId, userId, hmeDataRecordExtendList);
                }
            }

            log.info("<============autoJudgeNc-flag============>" + flag);

            HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
            hmeEoJobDataRecord.setJobRecordId(hmeEoJobDataRecordVO2.getJobRecordId());
            hmeEoJobDataRecord.setRemark(ncCodes.toString());
            hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(hmeEoJobDataRecord);

            //??????????????????
            HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO11 = new HmeNcDisposePlatformDTO11();
            // 20211115 modify by sanfeng.zhang ??????????????????????????????????????????
            String materialLotCode = hmeEoJobSnSingleBasic.getMaterialLot().getMaterialLotCode();
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag()) && !hmeEoJobSnSingleBasic.getNewMaterialLotFlag()) {
                materialLotCode = hmeEoJobSnReWorkMapper.queryNearNewMaterialLotByOldMaterialLot(tenantId, materialLotCode);
            }
            hmeNcDisposePlatformDTO11.setMaterialLotCode(materialLotCode);
            hmeNcDisposePlatformDTO11.setNcGroupId(ncGroupId);
            hmeNcDisposePlatformDTO11.setNcCodeIdList(ncCodeIdList);
            hmeNcDisposePlatformDTO11.setWorkcellId(dto.getWorkcellId());
            hmeNcDisposePlatformDTO11.setCurrentwWorkcellId(dto.getWorkcellId());
            switch (flag){
                case "N":
                    //????????????
                    log.info("<============autoJudgeNc-???????????? begin============>");
                    hmeNcDisposePlatformDTO11.setFlag("2");
                    hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                    log.info("<============autoJudgeNc-???????????? end============>");
                    break;
                case "R":
                    //????????????
                    log.info("<============autoJudgeNc-?????? begin============>");
                    hmeNcDisposePlatformDTO11.setFlag("3");
                    if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
                        hmeNcDisposePlatformDTO11.setReworkRecordFlag(YES);
                    }
                    hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                    log.info("<============autoJudgeNc-?????? end============>");
                    break;
                case "ND":
                    //????????????
                    log.info("<============autoJudgeNc-???????????? begin============>");
                    hmeNcDisposePlatformDTO11.setFlag("2");
                    String parentNcRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                    log.info("<============autoJudgeNc-???????????? end============>");
                    if(StringUtils.isBlank(parentNcRecordId)){
                        //	${1}?????????.${2}
                        throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????","autoJudgeNc"));
                    }
                    //??????
                    //??????????????????
                    log.info("<============autoJudgeNc-?????? begin============>");
                    //??????????????????ID
                    List<HmeNcDowngrade> hmeNcDowngradeList = hmeNcDowngradeRepository.selectByCondition(Condition.builder(HmeNcDowngrade.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeNcDowngrade.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(HmeNcDowngrade.FIELD_NC_CODE_ID, ncCodeIdList.get(0))
                                    .andEqualTo(HmeNcDowngrade.FIELD_MATERIAL_ID, hmeEoJobSnSingleBasic.getWo().getMaterialId())).build());
                    if(CollectionUtils.isEmpty(hmeNcDowngradeList) || hmeNcDowngradeList.size() > 1){
                        throw new MtException("HME_EO_JOB_SN_198", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_198", "HME", hmeEoJobSnSingleBasic.getWo().getMaterialCode()));
                    }

                    HmeNcCheckDTO4 hmeNcCheckDTO4 = new HmeNcCheckDTO4();
                    hmeNcCheckDTO4.setNcGroupId(ncGroupId);
                    hmeNcCheckDTO4.setNcCodeIdList(ncCodeIdList);
                    hmeNcCheckDTO4.setNcRecordIdList(Collections.singletonList(parentNcRecordId));
                    hmeNcCheckDTO4.setProcessMethod("4");
                    hmeNcCheckDTO4.setTransitionMaterialId(hmeNcDowngradeList.get(0).getTransitionMaterialId());
                    hmeNcCheckService.batchCheckSubmit(tenantId, hmeNcCheckDTO4);
                    log.info("<============autoJudgeNc-?????? end============>");
                    break;
                case "ZD":
                    //????????????
                    log.info("<============autoJudgeNc-???????????? begin============>");
                    hmeNcDisposePlatformDTO11.setFlag("2");
                    String ncRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                    log.info("<============autoJudgeNc-???????????? end============>");
                    if(StringUtils.isBlank(ncRecordId)){
                        //	${1}?????????.${2}
                        throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????","autoJudgeNc"));
                    }
                    log.info("<============autoJudgeNc-???????????????????????? begin============>");
                    HmeNcCheckDTO4 ncCheckDTO4 = new HmeNcCheckDTO4();
                    ncCheckDTO4.setNcGroupId(ncGroupId);
                    ncCheckDTO4.setNcCodeIdList(ncCodeIdList);
                    ncCheckDTO4.setNcRecordIdList(Collections.singletonList(ncRecordId));
                    ncCheckDTO4.setProcessMethod("7");
                    hmeNcCheckService.batchCheckSubmit(tenantId, ncCheckDTO4);
                    log.info("<============autoJudgeNc-???????????????????????? end============>");
                    break;
            }
        }
    }

    private String getRouteByNcCode(Long tenantId, String ncGroupId, List<String> ncCodeIdList, HmeEoJobSnVO3 dto) {
        if (CollectionUtils.isEmpty(ncCodeIdList)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        if (ncCodeIdList.size() > 1) {
            throw new MtException("HME_NC_0086", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0086", "HME"));
        }
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(dto.getEoId());
        if (mtEo.getIdentification().length() < 9) {
            throw new MtException("HME_CHIP_DATA_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0009", "HME", "?????????"));
        }
        // ??????eo ??????wo?????????
        List<String> proLineList = hmeNcCheckMapper.queryProLineByEoId(tenantId, dto.getEoId());
        if (CollectionUtils.isEmpty(proLineList)) {
            throw new MtException("HME_NC_0083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0083", "HME", mtEo.getIdentification()));
        }
        // ???SN 3,4???
        String deviceType = mtEo.getIdentification().substring(2, 4);
        // ???SN 5,6???
        String chipType = mtEo.getIdentification().substring(4, 6);
        // ???????????????????????? eoJobSn ??????eo ????????????????????????
        String operationId = hmeNcCheckMapper.queryLastNonReworkOperationId(tenantId, dto.getEoId());
        List<String> routeIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(operationId)) {
            routeIdList = hmeNcCheckRepository.queryRouteIdListLimitOperationId(tenantId, ncGroupId, ncCodeIdList, proLineList.get(0), deviceType, chipType, operationId);
        }
        // ??????????????????????????? ????????????????????????
        if (CollectionUtils.isEmpty(routeIdList)) {
            routeIdList = hmeNcCheckRepository.queryRouteIdListNonLimitOperationId(tenantId, ncGroupId, ncCodeIdList, proLineList.get(0), deviceType, chipType);
        }
        return CollectionUtils.isNotEmpty(routeIdList) ?  routeIdList.get(0) : "";
    }

    /**
     *
     * @Description ?????????????????????????????????????????????
     *
     * @author penglin.sui
     * @date 2021/5/19 17:39
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO16 ??????
     * @return void
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchAutoJudgeNc(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16){

        if(!"HME_EO_JOB_SN_172".equals(dto.getErrorCode()) || CollectionUtils.isEmpty(dto.getProcessNcDetailList())){
            return;
        }

        for ( HmeEoJobSnVO3 snLine : dto.getSnLineList()
             ) {
            HmeEoJobDataRecordVO2 hmeEoJobDataRecordVO2 = hmeEoJobSnVO16.getNcJobDataRecordMap().getOrDefault(snLine.getJobId() , null);
            if(Objects.isNull(hmeEoJobDataRecordVO2)){
                return;
            }

            StringBuilder ncCodes = new StringBuilder();
            List<String> ncCodeIdList = new ArrayList<>();
            boolean singleFlag = true;
            String reworkFlag = HmeConstants.ConstantValue.NO;
            String downGradeFlag = HmeConstants.ConstantValue.NO;
            //V20210519 modify by penglin.sui for peng.zhao ???????????????????????????????????????????????????
            String ncGroupId = "";

            List<HmeProcessNcDetailVO2> processNcDetailVO2List = dto.getProcessNcDetailList().stream().filter(item -> item.getJobId().equals(snLine.getJobId()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isEmpty(processNcDetailVO2List)){
                continue;
            }

            for (HmeProcessNcDetailVO2 item : processNcDetailVO2List
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
                ncCodeIdList.add(item.getNcCodeId());
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
                String flag = "N";
                if(singleFlag){
                    if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                        flag = "R";
                    }else if(HmeConstants.ConstantValue.YES.equals(downGradeFlag)){
                        flag = "ND";
                    } else {
                        // ????????????????????????????????????
                        String routerId = this.getRouteByNcCode(tenantId, ncGroupId, ncCodeIdList, dto);
                        if (StringUtils.isNotBlank(routerId)) {
                            flag = "ZD";
                        }
                    }
                }else{
                    if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                        flag = "R";
                    }
                }

                HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
                hmeEoJobDataRecord.setJobRecordId(hmeEoJobDataRecordVO2.getJobRecordId());
                hmeEoJobDataRecord.setRemark(ncCodes.toString());
                hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(hmeEoJobDataRecord);

                //??????????????????
                HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO11 = new HmeNcDisposePlatformDTO11();
                hmeNcDisposePlatformDTO11.setMaterialLotCode(hmeEoJobSnVO16.getMaterialLotMap().get(snLine.getMaterialLotId()).getMaterialLotCode());
                hmeNcDisposePlatformDTO11.setNcGroupId(ncGroupId);
                hmeNcDisposePlatformDTO11.setNcCodeIdList(ncCodeIdList);
                hmeNcDisposePlatformDTO11.setWorkcellId(dto.getWorkcellId());
                hmeNcDisposePlatformDTO11.setCurrentwWorkcellId(dto.getWorkcellId());
                switch (flag){
                    case "N":
                        //????????????
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        break;
                    case "R":
                        //????????????
                        hmeNcDisposePlatformDTO11.setFlag("3");
                        hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        break;
                    case "ND":
                        //????????????
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        String parentNcRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        if(StringUtils.isBlank(parentNcRecordId)){
                            //	${1}?????????.${2}
                            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????","batchAutoJudgeNc"));
                        }
                        //??????
                        HmeWorkOrderVO2 hmeWorkOrderVO2 = hmeEoJobSnVO16.getWoMap().get(snLine.getWorkOrderId());
                        //??????????????????ID
                        List<HmeNcDowngrade> hmeNcDowngradeList = hmeNcDowngradeRepository.selectByCondition(Condition.builder(HmeNcDowngrade.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeNcDowngrade.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(HmeNcDowngrade.FIELD_NC_CODE_ID, ncCodeIdList.get(0))
                                        .andEqualTo(HmeNcDowngrade.FIELD_MATERIAL_ID, hmeWorkOrderVO2.getMaterialId())).build());
                        if(CollectionUtils.isEmpty(hmeNcDowngradeList) || hmeNcDowngradeList.size() > 1){
                            throw new MtException("HME_EO_JOB_SN_198", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_198", "HME", hmeWorkOrderVO2.getMaterialCode()));
                        }

                        HmeNcCheckDTO4 hmeNcCheckDTO4 = new HmeNcCheckDTO4();
                        hmeNcCheckDTO4.setNcGroupId(ncGroupId);
                        hmeNcCheckDTO4.setNcCodeIdList(ncCodeIdList);
                        hmeNcCheckDTO4.setNcRecordIdList(Collections.singletonList(parentNcRecordId));
                        hmeNcCheckDTO4.setProcessMethod("4");
                        hmeNcCheckDTO4.setTransitionMaterialId(hmeNcDowngradeList.get(0).getTransitionMaterialId());
                        hmeNcCheckService.batchCheckSubmit(tenantId, hmeNcCheckDTO4);
                        break;
                    case "ZD":
                        //????????????
                        log.info("<============autoJudgeNc-???????????? begin============>");
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        String ncRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        log.info("<============autoJudgeNc-???????????? end============>");
                        if(StringUtils.isBlank(ncRecordId)){
                            //	${1}?????????.${2}
                            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????","autoJudgeNc"));
                        }
                        log.info("<============autoJudgeNc-???????????????????????? begin============>");
                        HmeNcCheckDTO4 ncCheckDTO4 = new HmeNcCheckDTO4();
                        ncCheckDTO4.setNcGroupId(ncGroupId);
                        ncCheckDTO4.setNcCodeIdList(ncCodeIdList);
                        ncCheckDTO4.setNcRecordIdList(Collections.singletonList(ncRecordId));
                        ncCheckDTO4.setProcessMethod("7");
                        hmeNcCheckService.batchCheckSubmit(tenantId, ncCheckDTO4);
                        log.info("<============autoJudgeNc-???????????????????????? end============>");
                        break;
                }
            }
        }
    }

    /**
     *
     * @Description ????????????-???????????????????????????????????????
     *
     * @author penglin.sui
     * @date 2021/5/21 10:49
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO16 ??????
     * @return void
     *
     */
    private void timeAutoJudgeNc(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16){

        if(!"HME_EO_JOB_SN_172".equals(dto.getErrorCode()) || CollectionUtils.isEmpty(dto.getProcessNcDetailList())){
            return;
        }

        for ( HmeEoJobSnVO3 snLine : dto.getSnLineList()
        ) {
            HmeEoJobDataRecordVO2 hmeEoJobDataRecordVO2 = hmeEoJobSnVO16.getNcJobDataRecordMap().getOrDefault(snLine.getJobId() , null);
            if(Objects.isNull(hmeEoJobDataRecordVO2)){
                return;
            }

            StringBuilder ncCodes = new StringBuilder();
            List<String> ncCodeIdList = new ArrayList<>();
            boolean singleFlag = true;
            String reworkFlag = HmeConstants.ConstantValue.NO;
            String downGradeFlag = HmeConstants.ConstantValue.NO;
            //V20210519 modify by penglin.sui for peng.zhao ???????????????????????????????????????????????????
            String ncGroupId = "";

            List<HmeProcessNcDetailVO2> processNcDetailVO2List = dto.getProcessNcDetailList().stream().filter(item -> item.getJobId().equals(snLine.getJobId()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isEmpty(processNcDetailVO2List)){
                continue;
            }

            for (HmeProcessNcDetailVO2 item : processNcDetailVO2List
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
                ncCodeIdList.add(item.getNcCodeId());
                reworkFlag = HmeConstants.ConstantValue.YES.equals(reworkFlag) ? reworkFlag : item.getReworkFlag();
                downGradeFlag = HmeConstants.ConstantValue.YES.equals(downGradeFlag) ? downGradeFlag : item.getDowngradeFlag();
            }

            if(ncCodes.length() > 0){

                //R:???????????? N:???????????? ND:???????????? + ??????
                String flag = "N";
                if(singleFlag){
                    if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                        flag = "R";
                    }else if(HmeConstants.ConstantValue.YES.equals(downGradeFlag)){
                        flag = "ND";
                    }
                }else{
                    if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                        flag = "R";
                    }
                }

                HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
                hmeEoJobDataRecord.setJobRecordId(hmeEoJobDataRecordVO2.getJobRecordId());
                hmeEoJobDataRecord.setRemark(ncCodes.toString());
                hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(hmeEoJobDataRecord);

                //??????????????????
                HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO11 = new HmeNcDisposePlatformDTO11();
                hmeNcDisposePlatformDTO11.setMaterialLotCode(hmeEoJobSnVO16.getMaterialLotMap().get(snLine.getMaterialLotId()).getMaterialLotCode());
                hmeNcDisposePlatformDTO11.setNcGroupId(ncGroupId);
                hmeNcDisposePlatformDTO11.setNcCodeIdList(ncCodeIdList);
                hmeNcDisposePlatformDTO11.setWorkcellId(dto.getWorkcellId());
                hmeNcDisposePlatformDTO11.setCurrentwWorkcellId(dto.getWorkcellId());
                switch (flag){
                    case "N":
                        //????????????
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        break;
                    case "R":
                        //????????????
                        hmeNcDisposePlatformDTO11.setFlag("3");
                        hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        break;
                    case "ND":
                        //????????????
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        String parentNcRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        if(StringUtils.isBlank(parentNcRecordId)){
                            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????","batchAutoJudgeNc"));
                        }
                        //??????
                        //??????????????????
                        List<MtNcRecord> mtNcRecordList = mtNcRecordRepository.selectByCondition(Condition.builder(MtNcRecord.class)
                                .andWhere(Sqls.custom().andEqualTo(MtNcRecord.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtNcRecord.FIELD_PARENT_NC_RECORD_ID, parentNcRecordId)).build());
                        if(CollectionUtils.isNotEmpty(mtNcRecordList)){
                            HmeWorkOrderVO2 hmeWorkOrderVO2 = hmeEoJobSnVO16.getWoMap().get(snLine.getWorkOrderId());
                            //??????????????????ID
                            List<HmeNcDowngrade> hmeNcDowngradeList = hmeNcDowngradeRepository.selectByCondition(Condition.builder(HmeNcDowngrade.class)
                                    .andWhere(Sqls.custom().andEqualTo(HmeNcDowngrade.FIELD_TENANT_ID, tenantId)
                                            .andEqualTo(HmeNcDowngrade.FIELD_NC_CODE_ID, ncCodeIdList.get(0))
                                            .andEqualTo(HmeNcDowngrade.FIELD_MATERIAL_ID, hmeWorkOrderVO2.getMaterialId())).build());
                            if(CollectionUtils.isEmpty(hmeNcDowngradeList) || hmeNcDowngradeList.size() > 1){
                                throw new MtException("HME_EO_JOB_SN_198", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_198", "HME", hmeWorkOrderVO2.getMaterialCode()));
                            }

                            HmeNcCheckDTO4 hmeNcCheckDTO4 = new HmeNcCheckDTO4();
                            hmeNcCheckDTO4.setNcGroupId(ncGroupId);
                            hmeNcCheckDTO4.setNcCodeIdList(ncCodeIdList);
                            hmeNcCheckDTO4.setNcRecordIdList(mtNcRecordList.stream().map(MtNcRecord::getNcRecordId).distinct().collect(Collectors.toList()));
                            hmeNcCheckDTO4.setProcessMethod("4");
                            hmeNcCheckDTO4.setTransitionMaterialId(hmeNcDowngradeList.get(0).getTransitionMaterialId());
                            hmeNcCheckService.batchCheckSubmit(tenantId, hmeNcCheckDTO4);
                        }
                        break;
                }
            }
        }
    }

    /**
     *
     * @Description ????????????????????????-?????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/7 20:44
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnSingleBasic ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mainOutSiteForNormalStepForReworkProcess(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForNormalStepForReworkProcess tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        //V20210312 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????NG
        if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
            List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);
    }

    /**
     *
     * @Description ???????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/15 15:28
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnSingleBasic ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mainOutSiteForRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForRework tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeRouterStepVO nearStep = hmeEoJobSnSingleBasic.getNearStep();

        // ??????????????????
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            // ???????????????eo?????? ???????????????????????????
            boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
            hmeEoJobSnSingleBasic.setNewMaterialLotFlag(newMaterialLotFlag);
        }

        //??????API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //????????????????????????
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // ??????????????????
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // ??????{ containerLoad }??????????????????
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //??????????????????
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //????????????
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        //?????????????????? ????????????????????????????????? ?????????????????????????????????????????????attribute=Y ???????????? attribute?????????null
        if (StringUtils.isNotBlank(dto.getIsAbnormalOutSite()) && HmeConstants.ConstantValue.YES.equals(dto.getIsAbnormalOutSite())) {
            updateEoJobSn.setAttribute7(dto.getIsAbnormalOutSite());
        }
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma ??????????????????
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsRecordLabCode()) && StringUtils.isNotBlank(dto.getLabCode())) {
            HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
            hmeMaterialLotLabCodePara.setRouterStepId(dto.getEoStepId());
            hmeMaterialLotLabCodePara.setLabCode(dto.getLabCode());
            HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
            if (Objects.isNull(hmeMaterialLotLabCode)) {
                HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                insertHmeMaterialLotLabCode.setTenantId(tenantId);
                insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                insertHmeMaterialLotLabCode.setLabCode(dto.getLabCode());
                insertHmeMaterialLotLabCode.setJobId(dto.getJobId());
                insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                insertHmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
                insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                insertHmeMaterialLotLabCode.setRouterStepId(dto.getEoStepId());
                insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeMaterialLotLabCodeRepository.insertSelective(insertHmeMaterialLotLabCode);
            } else {
                if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                    HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                    updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    hmeMaterialLotLabCodeMapper.updateByPrimaryKeySelective(updateHmeMaterialLotLabCode);
                }
            }
        }

        //??????????????????
        autoJudgeNc(tenantId , dto , hmeEoJobSnSingleBasic);
    }

    /**
     *
     * @Description ?????????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/4 20:42
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnSingleBasic ??????
     * @return void
     *
     */
    @Override
    public void mainOutSiteForDesignedReworkComplete(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDesignedReworkComplete tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeRouterStepVO nearStep = hmeEoJobSnSingleBasic.getNearStep();

        // ??????????????????
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //??????API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
        //V20210312 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????NG
        if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
            List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //??????????????????????????????????????????-EO_WKC_STEP_COMPLETE??????
        List<MtExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
        /*MtExtendVO5 reworkFlag = new MtExtendVO5();
        reworkFlag.setAttrName("REWORK_FLAG");
        reworkFlag.setAttrValue("");
        mtLotExtendCompleteList.add(reworkFlag);*/
        MtExtendVO5 designedReworkFlag = new MtExtendVO5();
        designedReworkFlag.setAttrName("DESIGNED_REWORK_FLAG");
        designedReworkFlag.setAttrValue("");
        mtLotExtendCompleteList.add(designedReworkFlag);
        MtExtendVO10 materialLotAttrCompleteUpdate = new MtExtendVO10();
        materialLotAttrCompleteUpdate.setEventId(completeEventId);
        materialLotAttrCompleteUpdate.setKeyId(dto.getMaterialLotId());
        materialLotAttrCompleteUpdate.setAttrs(mtLotExtendCompleteList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotAttrCompleteUpdate);

        //????????????????????????
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // ??????????????????
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // ??????{ containerLoad }??????????????????
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //??????????????????
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //????????????
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma ??????????????????
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsRecordLabCode()) && StringUtils.isNotBlank(dto.getLabCode())) {
            HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
            hmeMaterialLotLabCodePara.setRouterStepId(dto.getEoStepId());
            hmeMaterialLotLabCodePara.setLabCode(dto.getLabCode());
            HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
            if (Objects.isNull(hmeMaterialLotLabCode)) {
                HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                insertHmeMaterialLotLabCode.setTenantId(tenantId);
                insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                insertHmeMaterialLotLabCode.setLabCode(dto.getLabCode());
                insertHmeMaterialLotLabCode.setJobId(dto.getJobId());
                insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                insertHmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
                insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                insertHmeMaterialLotLabCode.setRouterStepId(dto.getEoStepId());
                insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeMaterialLotLabCodeRepository.insertSelective(insertHmeMaterialLotLabCode);
            } else {
                if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                    HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                    updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    hmeMaterialLotLabCodeMapper.updateByPrimaryKeySelective(updateHmeMaterialLotLabCode);
                }
            }
        }
        // ???????????? ????????????????????? ????????????????????????????????????JCRETEST
        if (HmeConstants.ConstantValue.YES.equals(dto.getCrossRetestFlag())) {
            // ?????????????????????????????????Y ???Y??? ??????????????????Y
            // ????????????????????????
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(dto.getMaterialLotId());
                setTableName("mt_material_lot_attr");
                setAttrName("JCRETEST");
            }});
            String jcRetest = CollectionUtils.isNotEmpty(mtExtendAttrVOS) ? mtExtendAttrVOS.get(0).getAttrValue() : "";
            // ??????????????????????????????
            String eventCreateId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setEventTypeCode("SN_CROSS_TEST_COMPLETED");
                }
            });
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 jcRetestAttr = new MtExtendVO5();
            jcRetestAttr.setAttrName("JCRETEST");
            jcRetestAttr.setAttrValue("");
            attrList.add(jcRetestAttr);
            if (HmeConstants.ConstantValue.YES.equals(jcRetest)) {
                MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
                reworkFlagAttr.setAttrName("REWORK_FLAG");
                reworkFlagAttr.setAttrValue(HmeConstants.ConstantValue.YES);
                attrList.add(reworkFlagAttr);
            }
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setEventId(eventCreateId);
            mtExtendVO10.setKeyId(dto.getMaterialLotId());
            mtExtendVO10.setAttrs(attrList);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        }

        //??????????????????
        autoJudgeNc(tenantId , dto , hmeEoJobSnSingleBasic);
    }

    @Override
    public void mainOutSiteForDesignedReworkComplete2(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDesignedReworkComplete tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeRouterStepVO nearStep = hmeEoJobSnSingleBasic.getNearStep();

        // ??????????????????
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //??????API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
        //V20210312 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????NG
        if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
            List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //??????????????????????????????????????????-EO_WKC_STEP_COMPLETE??????
        List<MtExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
        /*MtExtendVO5 reworkFlag = new MtExtendVO5();
        reworkFlag.setAttrName("REWORK_FLAG");
        reworkFlag.setAttrValue("");
        mtLotExtendCompleteList.add(reworkFlag);*/
        MtExtendVO5 designedReworkFlag = new MtExtendVO5();
        designedReworkFlag.setAttrName("DESIGNED_REWORK_FLAG");
        designedReworkFlag.setAttrValue("");
        mtLotExtendCompleteList.add(designedReworkFlag);
        MtExtendVO10 materialLotAttrCompleteUpdate = new MtExtendVO10();
        materialLotAttrCompleteUpdate.setEventId(completeEventId);
        materialLotAttrCompleteUpdate.setKeyId(dto.getMaterialLotId());
        materialLotAttrCompleteUpdate.setAttrs(mtLotExtendCompleteList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotAttrCompleteUpdate);

        //????????????????????????
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // ??????????????????
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // ??????{ containerLoad }??????????????????
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //??????????????????
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //????????????
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma ??????????????????
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsRecordLabCode()) && StringUtils.isNotBlank(dto.getLabCode())) {
            HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
            hmeMaterialLotLabCodePara.setRouterStepId(dto.getEoStepId());
            hmeMaterialLotLabCodePara.setLabCode(dto.getLabCode());
            HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
            if (Objects.isNull(hmeMaterialLotLabCode)) {
                HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                insertHmeMaterialLotLabCode.setTenantId(tenantId);
                insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                insertHmeMaterialLotLabCode.setLabCode(dto.getLabCode());
                insertHmeMaterialLotLabCode.setJobId(dto.getJobId());
                insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                insertHmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
                insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                insertHmeMaterialLotLabCode.setRouterStepId(dto.getEoStepId());
                insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeMaterialLotLabCodeRepository.insertSelective(insertHmeMaterialLotLabCode);
            } else {
                if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                    HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                    updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    hmeMaterialLotLabCodeMapper.updateByPrimaryKeySelective(updateHmeMaterialLotLabCode);
                }
            }
        }
        //????????????????????????
        String eventCreateId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventTypeCode("SN_CROSS_TEST");
            }
        });

        List<MtExtendVO5> attrList = new ArrayList<>();
        MtExtendVO5 jcRetestAttr = new MtExtendVO5();
        jcRetestAttr.setAttrName("JCRETEST");
        jcRetestAttr.setAttrValue(HmeConstants.ConstantValue.YES);
        attrList.add(jcRetestAttr);
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
        mtExtendVO10.setEventId(eventCreateId);
        mtExtendVO10.setKeyId(dto.getMaterialLotId());
        mtExtendVO10.setAttrs(attrList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

        //????????????
        launchNc(tenantId , dto , hmeEoJobSnSingleBasic);
    }

    private void launchNc(Long tenantId , HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        if(!"HME_EO_JOB_SN_172".equals(dto.getErrorCode()) || CollectionUtils.isEmpty(dto.getProcessNcDetailList())){
            return;
        }

        HmeEoJobDataRecordVO2 hmeEoJobDataRecordVO2 = hmeEoJobSnSingleBasic.getNcJobDataRecordMap().getOrDefault(dto.getJobId() , null);
        if(Objects.isNull(hmeEoJobDataRecordVO2)){
            return;
        }

        StringBuilder ncCodes = new StringBuilder();
        List<String> ncCodeIdList = new ArrayList<>();
        String reworkFlag = HmeConstants.ConstantValue.NO;
        String downGradeFlag = HmeConstants.ConstantValue.NO;
        //V20210519 modify by penglin.sui for peng.zhao ???????????????????????????????????????????????????
        String ncGroupId = "";
        for (HmeProcessNcDetailVO2 item : dto.getProcessNcDetailList()
        ) {
            if(StringUtils.isNotBlank(item.getNcCode())){
                if(ncCodes.length() == 0){
                    ncCodes.append(String.join(";",item.getNcCode()));
                    ncGroupId = item.getNcGroupId();
                }else{
                    ncCodes.append(";" + String.join(";",item.getNcCode()));
                }
            }
            ncCodeIdList.add(item.getNcCodeId());
            reworkFlag = HmeConstants.ConstantValue.YES.equals(reworkFlag) ? reworkFlag : item.getReworkFlag();
            downGradeFlag = HmeConstants.ConstantValue.YES.equals(downGradeFlag) ? downGradeFlag : item.getDowngradeFlag();
        }

        if(StringUtils.isBlank(ncGroupId)){
            //???${1}???????????????????????????,?????????????????????
            throw new MtException("HME_EO_JOB_SN_197", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_197", "HME", ncCodes.toString()));
        }

        if(ncCodes.length() > 0){
            //????????????
            HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
            hmeEoJobDataRecord.setJobRecordId(hmeEoJobDataRecordVO2.getJobRecordId());
            hmeEoJobDataRecord.setRemark(ncCodes.toString());
            hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(hmeEoJobDataRecord);
            log.info("<============launchNc-?????? begin============>");
            HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO11 = new HmeNcDisposePlatformDTO11();
            // 20211115 modify by sanfeng.zhang ???????????????????????????????????????
            String materialLotCode = hmeEoJobSnSingleBasic.getMaterialLot().getMaterialLotCode();
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag()) && !HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getMaterialLot().getDesignedReworkFlag()) && !hmeEoJobSnSingleBasic.getNewMaterialLotFlag()) {
                materialLotCode = hmeEoJobSnReWorkMapper.queryNearNewMaterialLotByOldMaterialLot(tenantId, materialLotCode);
            }
            hmeNcDisposePlatformDTO11.setMaterialLotCode(materialLotCode);
            hmeNcDisposePlatformDTO11.setNcGroupId(ncGroupId);
            hmeNcDisposePlatformDTO11.setNcCodeIdList(ncCodeIdList);
            hmeNcDisposePlatformDTO11.setWorkcellId(dto.getWorkcellId());
            hmeNcDisposePlatformDTO11.setCurrentwWorkcellId(dto.getWorkcellId());
            if (HmeConstants.ConstantValue.YES.equals(dto.getReworkFlag()) || HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
                hmeNcDisposePlatformDTO11.setReworkRecordFlag(HmeConstants.ConstantValue.YES);
            }
            hmeNcDisposePlatformDTO11.setFlag("3");
            hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
            log.info("<============launchNc-?????? end============>");
        }
    }

    /**
     *
     * @Description ??????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/23 17:13
     * @param tenantId ??????ID
     * @param isRework ???????????? true:??? false:???
     * @param operationId ??????ID
     * @param eoIdList eoIdList
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    @Override
    public List<HmeEoJobSn> queryEoJobSnForTimeInSite(Long tenantId, boolean isRework, String operationId, List<String> eoIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryCurrentAndNextStep tenantId={},isRework={},operationId={},eoIdList={}", tenantId, isRework, operationId, eoIdList);
        if (CollectionUtils.isEmpty(eoIdList)) {
            return new ArrayList<HmeEoJobSn>();
        }

        //?????????????????????????????????eo,???????????????eo?????????
        if (isRework) {
            return hmeEoJobSnMapper.queryEoJobSnForTimeInSite(tenantId, isRework, operationId, eoIdList.get(0), new ArrayList<String>());
        } else {
            return hmeEoJobSnMapper.queryEoJobSnForTimeInSite(tenantId, isRework, operationId, StringUtils.EMPTY, eoIdList);
        }
    }

    /**
     *
     * @Description ????????????-????????????
     *
     * @author yuchao.wang
     * @date 2020/12/24 11:19
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO24 ??????
     * @return void
     *
     */
    @Override
    public void batchMainOutSiteForRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO24 hmeEoJobSnVO24) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSiteForRework tenantId={},dto={},hmeEoJobSnVO24={}", tenantId, dto, hmeEoJobSnVO24);
        //????????????????????????
        HmeMaterialLotVO3 hmeMaterialLot = hmeEoJobSnVO24.getReworkMaterialLot();
        MtEo eo = hmeEoJobSnVO24.getReworkEo();

        //??????????????????????????????
        HmeRouterStepVO nearStep = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, eo.getEoId());
        if (Objects.isNull(nearStep) || StringUtils.isBlank(nearStep.getEoStepActualId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }

        // ??????????????????
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //??????API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //?????????????????????????????????????????????
        if (StringUtils.isNotBlank(hmeMaterialLot.getCurrentContainerId())) {
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(hmeMaterialLot.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(hmeMaterialLot.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //??????????????????????????????-EO_WKC_STEP_COMPLETE?????? ??????????????????????????????????????????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(hmeMaterialLot.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //??????????????????
        List<String> jobIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct().collect(Collectors.toList());
        hmeEoJobSnRepository.batchOutSite2(tenantId, hmeEoJobSnVO24.getUserId(), jobIdList, dto.getRemark());
    }

    /**
     *
     * @Description ????????????-????????????????????????-????????????
     *
     * @author yuchao.wang
     * @date 2021/1/8 10:24
     * @param tenantId ??????ID
     * @param dto ??????
     * @param hmeEoJobSnVO24 ??????
     * @return void
     *
     */
    @Override
    public void batchMainOutSiteForDesignedReworkComplete(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO24 hmeEoJobSnVO24) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSiteForDesignedReworkComplete tenantId={},dto={},hmeEoJobSnVO24={}", tenantId, dto, hmeEoJobSnVO24);
        //?????????????????????????????????????????????????????????,????????????????????????
        HmeMaterialLotVO3 hmeMaterialLot = hmeEoJobSnVO24.getReworkMaterialLot();
        MtEo eo = hmeEoJobSnVO24.getReworkEo();

        //??????????????????????????????
        HmeRouterStepVO nearStep = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, eo.getEoId());
        if (Objects.isNull(nearStep) || StringUtils.isBlank(nearStep.getEoStepActualId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }

        // ??????????????????
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //??????API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //??????????????????
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //??????????????????????????????-EO_WKC_STEP_COMPLETE?????? ????????????????????????????????????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(hmeMaterialLot.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //??????????????????????????????????????????-EO_WKC_STEP_COMPLETE??????
        List<MtExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
        /*MtExtendVO5 reworkFlag = new MtExtendVO5();
        reworkFlag.setAttrName("REWORK_FLAG");
        reworkFlag.setAttrValue("");
        mtLotExtendCompleteList.add(reworkFlag);*/
        MtExtendVO5 designedReworkFlag = new MtExtendVO5();
        designedReworkFlag.setAttrName("DESIGNED_REWORK_FLAG");
        designedReworkFlag.setAttrValue("");
        mtLotExtendCompleteList.add(designedReworkFlag);
        MtExtendVO10 materialLotAttrCompleteUpdate = new MtExtendVO10();
        materialLotAttrCompleteUpdate.setEventId(completeEventId);
        materialLotAttrCompleteUpdate.setKeyId(hmeMaterialLot.getMaterialLotId());
        materialLotAttrCompleteUpdate.setAttrs(mtLotExtendCompleteList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotAttrCompleteUpdate);

        //??????????????????
        List<String> jobIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct().collect(Collectors.toList());
        hmeEoJobSnRepository.batchOutSite2(tenantId, hmeEoJobSnVO24.getUserId(), jobIdList, dto.getRemark());

        //???????????????????????????????????????
        Map<String, HmeMaterialLotVO3> materialLotMap = new HashMap<>();
        materialLotMap.put(hmeMaterialLot.getMaterialLotId() , hmeMaterialLot);
        HmeEoJobSnVO16 hmeEoJobSnVO16 = new HmeEoJobSnVO16();
        hmeEoJobSnVO16.setMaterialLotMap(materialLotMap);
        hmeEoJobSnVO16.setNcJobDataRecordMap(hmeEoJobSnVO24.getNcJobDataRecordMap());
        hmeEoJobSnVO16.setWoMap(hmeEoJobSnVO24.getWoMap());
        batchAutoJudgeNc(tenantId , dto , hmeEoJobSnVO16);
    }

    @Override
    public HmeEoJobSnBatchVO16 selectVirtualComponent(Long tenantId, List<HmeEoJobSnBatchVO4> componentList) {
        HmeEoJobSnBatchVO16 resultVO = new HmeEoJobSnBatchVO16();
        resultVO.setVirtualJobMap(new HashMap<>());
        resultVO.setVirtualComponentMaterialLotMap(new HashMap<>());
        resultVO.setVirtualEoJobSnMap(new HashMap<>());
        //????????? && ?????????????????????
        Map<String,String> virtualAndComponentRelMap = new HashMap<>();
        //???????????????
        List<HmeEoJobSnBatchVO4> allVirtualComponentList = componentList.stream().filter(item -> !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(allVirtualComponentList)) {
            virtualAndComponentRelMap = allVirtualComponentList.stream().collect(Collectors.toMap(HmeEoJobSnBatchVO4::getMaterialId,
                    HmeEoJobSnBatchVO4::getTopVirtualMaterialCode));
            Map<String,List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap = new HashMap<>();
            List<String> virtualJobIdList = new ArrayList<>();
            Map<String,String> virtualJobMap = new HashMap<>();
            Map<String,HmeEoJobSn> virtualEoJobSnMap = new HashMap<>();
            List<String> allMaterialIdList = new ArrayList<>();
            List<String> allSnMaterialIdList = new ArrayList<>();
            List<String> virtualMaterialLotIdList = new ArrayList<>();
            //?????????
            List<HmeEoJobSnBatchVO4> virtualList = componentList.stream().filter(item -> HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                    && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
            virtualList.forEach(item -> {
                if(CollectionUtils.isNotEmpty(item.getMaterialLotList())){
                    virtualMaterialLotIdList.addAll(item.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getMaterialLotId).collect(Collectors.toList()));
                }
            });
            //????????????
            List<HmeEoJobSnBatchVO4> allVirtualComponentList2 = allVirtualComponentList.stream().filter(item -> item.getProductionType().equals(HmeConstants.MaterialTypeCode.SN))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(allVirtualComponentList2)){
                allSnMaterialIdList = allVirtualComponentList2.stream().map(HmeEoJobSnBatchVO4::getMaterialId).collect(Collectors.toList());
            }
            //??????/????????????
            List<HmeEoJobSnBatchVO4> allVirtualComponentList3 = allVirtualComponentList.stream().filter(item -> item.getProductionType().equals(HmeConstants.MaterialTypeCode.LOT)
                    || item.getProductionType().equals(HmeConstants.MaterialTypeCode.TIME)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(allVirtualComponentList3)){
                allMaterialIdList = allVirtualComponentList3.stream().map(HmeEoJobSnBatchVO4::getMaterialId).collect(Collectors.toList());
            }
            //????????????????????????JOB ID
            if(CollectionUtils.isNotEmpty(virtualMaterialLotIdList)) {
                List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                                .andIn(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, virtualMaterialLotIdList)
                                .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.PREPARE_PROCESS)).build());
                if(CollectionUtils.isNotEmpty(hmeEoJobSnList)){
                    virtualJobIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).collect(Collectors.toList());
                    virtualJobMap = hmeEoJobSnList.stream().collect(Collectors.toMap(HmeEoJobSn::getMaterialLotId,
                            HmeEoJobSn::getJobId));
                    resultVO.setVirtualJobMap(virtualJobMap);

                    virtualEoJobSnMap = hmeEoJobSnList.stream().collect(Collectors.toMap(HmeEoJobSn::getMaterialLotId,t -> t));
                    resultVO.setVirtualEoJobSnMap(virtualEoJobSnMap);

                    if(CollectionUtils.isNotEmpty(allMaterialIdList)) {
                        SecurityTokenHelper.close();
                        List<HmeEoJobSnBatchVO6> lotTimeMaterialLotList = hmeEoJobSnBatchMapper.selectLotTimeVirtualComponentMaterialLot(tenantId,allMaterialIdList,virtualJobIdList);
                        if(CollectionUtils.isNotEmpty(lotTimeMaterialLotList)){
                            for (HmeEoJobSnBatchVO6 item : lotTimeMaterialLotList
                            ) {
                                item.setTopVirtualMaterialCode(virtualAndComponentRelMap.getOrDefault(item.getMaterialId(),""));
                            }
                            Map<String,List<HmeEoJobSnBatchVO6>> lotTimeMaterialMap = lotTimeMaterialLotList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey3(e.getJobId(),e.getMaterialId(),e.getMaterialType())));
                            if(MapUtils.isNotEmpty(lotTimeMaterialMap)){
                                virtualComponentMaterialLotMap.putAll(lotTimeMaterialMap);
                            }
                        }
                    }
                    if(CollectionUtils.isNotEmpty(allSnMaterialIdList)) {
                        SecurityTokenHelper.close();
                        List<HmeEoJobSnBatchVO6> snMaterialLotList = hmeEoJobSnBatchMapper.selectSnVirtualComponentMaterialLot(tenantId,allSnMaterialIdList,virtualJobIdList);
                        if(CollectionUtils.isNotEmpty(snMaterialLotList)){
                            for (HmeEoJobSnBatchVO6 item : snMaterialLotList
                                 ) {
                                item.setTopVirtualMaterialCode(virtualAndComponentRelMap.getOrDefault(item.getMaterialId(),""));
                            }
                            Map<String,List<HmeEoJobSnBatchVO6>> snMaterialMap = snMaterialLotList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey3(e.getJobId(),e.getMaterialId(),e.getMaterialType())));
                            if(MapUtils.isNotEmpty(snMaterialMap)){
                                virtualComponentMaterialLotMap.putAll(snMaterialMap);
                            }
                        }
                    }

                    if(MapUtils.isNotEmpty(virtualComponentMaterialLotMap)){
                        resultVO.setVirtualComponentMaterialLotMap(virtualComponentMaterialLotMap);
                    }
                }
            }
        }
        return resultVO;
    }

    /**
     *
     * @Description ??????????????????-??????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/31 14:58
     * @param tenantId ??????ID
     * @param bomId bomId
     * @param snLine ??????
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO2
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO2 createSnJobForSingle(Long tenantId, String bomId, HmeEoJobSnVO3 snLine) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.createSnJobForSingle tenantId={},bomId={},snLine={}", tenantId, bomId, snLine);
        HmeEoJobSnVO2 resultVO = new HmeEoJobSnVO2();
        BeanUtils.copyProperties(snLine, resultVO);
        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // ??????????????????
        final Date currentDate = CommonUtils.currentTimeGet();

        HmeEoJobSn snJob = new HmeEoJobSn();
        snJob.setTenantId(tenantId);
        snJob.setEoId(snLine.getEoId());
        snJob.setSiteInDate(currentDate);
        snJob.setShiftId(snLine.getWkcShiftId());
        snJob.setSiteInBy(userId);
        snJob.setWorkcellId(snLine.getWorkcellId());
        snJob.setWorkOrderId(snLine.getWorkOrderId());
        snJob.setOperationId(snLine.getOperationId());
        snJob.setMaterialLotId(snLine.getMaterialLotId());
        snJob.setSnMaterialId(snLine.getMaterialId());
        snJob.setJobType(snLine.getJobType());
        snJob.setEoStepId(snLine.getEoStepId());
        snJob.setReworkFlag(snLine.getReworkFlag());

        //V20210312 modify by penglin.sui for tianyang.xie ?????????????????????tenant_id+eo_id+workcell_id+operation_id+rework_flag+job_type+material_lot_id????????????eo_step_num+1?????????0
        if(HmeConstants.ConstantValue.YES.equals(snLine.getReworkFlag())){
            int maxEoStepNum = hmeEoJobSnMapper.queryReworkMaxEoStepNum(tenantId,snJob);
            snJob.setEoStepNum(maxEoStepNum + 1);
        }else{
            snJob.setEoStepNum(snLine.getEoStepNum());
        }

        snJob.setSourceContainerId(snLine.getSourceContainerId());
        snJob.setJobContainerId(snLine.getJobContainerId());
        hmeEoJobSnRepository.insertSelective(snJob);

        BeanUtils.copyProperties(snJob, resultVO);
        resultVO.setMaterialId(snLine.getMaterialId());
        resultVO.setProductionVersion(snLine.getWoProductionVersion());

        //????????????????????????
        materialInSiteForSingle(tenantId, resultVO, bomId);

        //???????????????????????????
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = hmeEoJobDataRecordRepository.inSiteScan(tenantId, resultVO);
        resultVO.setDataRecordVOList(hmeEoJobDataRecordVOList);
        return resultVO;
    }

    /**
     *
     * @Description ??????????????????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/15 10:35
     * @param tenantId ??????ID
     * @param code ??????
     * @return boolean
     *
     */
    @Override
    public boolean codeIsMaterialLotOrContainer(Long tenantId, String code) {
        Integer count = hmeEoJobSnMapper.queryCountFromMaterialLotAndContainerByCode(tenantId, code);
        return (Objects.nonNull(count) && count > 0);
    }

    /**
     *
     * @Description ?????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/25 13:47
     * @param tenantId ??????ID
     * @param materialLotCode ??????
     * @param eoJobDataRecordList ?????????????????????
     * @param processNcInfo ??????????????????
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    @Override
    public HmeEoJobSn dataRecordProcessNcValidate(Long tenantId, String materialLotCode, List<HmeEoJobDataRecord> eoJobDataRecordList, HmeProcessNcHeaderVO2 processNcInfo) {
        //????????????
        if (CollectionUtils.isEmpty(eoJobDataRecordList)
                || Objects.isNull(processNcInfo) || StringUtils.isBlank(processNcInfo.getHeaderId())) {
            return null;
        }
        if (CollectionUtils.isEmpty(processNcInfo.getLineList())) {
            //???SN???{$1}??????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }

        //V20210308 modify by penglin.sui for peng.zhao ?????????????????????????????????????????????
        List<String> tagIdList = processNcInfo.getLineList().stream().map(HmeProcessNcLineVO2::getTagId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(tagIdList)){
            return null;
        }
        eoJobDataRecordList = eoJobDataRecordList.stream().filter(item -> tagIdList.contains(item.getTagId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eoJobDataRecordList)) {
            return null;
        }

        //???????????????????????????(??????????????????)?????????????????????
        List<HmeProcessNcLineVO2> noPriorityLineList = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isBlank(item.getPriority())).collect(Collectors.toList());
        List<HmeProcessNcLineVO2> priorityLineList = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isNotBlank(item.getPriority()))
                .sorted(Comparator.comparing(HmeProcessNcLineVO2::getPriority)).collect(Collectors.toList());

        Map<String, HmeProcessNcLineVO2> noPriorityLineMap = new HashMap<String, HmeProcessNcLineVO2>();
        if (CollectionUtils.isNotEmpty(noPriorityLineList)) {
            noPriorityLineList.forEach(item -> noPriorityLineMap.put(item.getStandardCode(), item));
        }

        //????????????????????????1????????????
        if (CollectionUtils.isEmpty(priorityLineList) || !"1".equals(priorityLineList.get(0).getPriority())) {
            //???SN???{$1}??????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }
        List<HmeProcessNcDetailVO2> hmeProcessNcDetailVO2List = new ArrayList<>();
        for (HmeProcessNcLineVO2 processNcLine : priorityLineList) {
            //???????????????????????????????????????????????????
            Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                    .filter(item -> StringUtils.equals(processNcLine.getTagId(), item.getTagId())).findAny();

            if (!currEoJobDataRecordOptional.isPresent()) {
                //???????????????${1}???????????????????????????
                throwNoProcessNcMtException(tenantId, processNcLine.getTagId(), "HME_EO_JOB_SN_177");
            }

            //???????????????????????????????????????
            HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

            //?????????????????????????????????
            try {
                BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
            } catch (Exception e) {
                //??????????????????{$1}???????????????,?????????
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
            }
            BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

            //??????????????????????????????????????????
            Optional<HmeProcessNcDetailVO2> processNcDetailFind = processNcLine.getDetailList().stream()
                    .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                            && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

            //???????????????????????????????????????
            if (!processNcDetailFind.isPresent()) {
                //????????????{$1}???????????????????????????,?????????!
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
            }

            //???????????????????????????
            HmeProcessNcDetailVO2 processNcDetail = processNcDetailFind.get();

            //???????????????????????????????????????????????????
            /*if (StringUtils.isBlank(noPriorityProcessNcDetail.getNcCodeId())
                    && StringUtils.isBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
                //????????????{$1}???????????????????????????,?????????!
                throwNoProcessNcMtException(tenantId, errorTag, "HME_EO_JOB_SN_170");
            }*/

            //????????????????????????????????????????????????
            if (StringUtils.isNotBlank(processNcDetail.getNcCodeId())) {
//                return returnSnProcessNc(tenantId, materialLotCode, processNcDetail);
                hmeProcessNcDetailVO2List.add(processNcDetail);
            }

            //?????????1?????????????????????,?????????????????????????????????????????????
            if ("1".equals(processNcLine.getPriority()) && StringUtils.isNotBlank(processNcDetail.getDetailStandardCode())) {
//                HmeEoJobSn validateResult = dataRecordNoPriorityRecursionValidate(tenantId, materialLotCode,
//                        eoJobDataRecordList, processNcDetail, noPriorityLineMap);
//                if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
//                    return validateResult;
//                }

                List<HmeProcessNcDetailVO2> resultList = dataRecordNoPriorityRecursionValidate2(tenantId, materialLotCode,
                        eoJobDataRecordList, processNcDetail, noPriorityLineMap);
                if(CollectionUtils.isNotEmpty(resultList)){
                    hmeProcessNcDetailVO2List.addAll(resultList);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(hmeProcessNcDetailVO2List)){
            return returnSnMultipleProcessNc(tenantId,materialLotCode,hmeProcessNcDetailVO2List);
        }

        return null;
    }

    /**
     *
     * @Description ?????????????????????????????????-????????????
     *
     * @author yuchao.wang
     * @date 2021/2/4 13:46
     * @param tenantId ??????ID
     * @param materialLotCode ??????
     * @param eoJobDataRecordList ?????????????????????
     * @param processNcInfo ??????????????????
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    @Override
    public HmeEoJobSn dataRecordAgeingProcessNcValidate(Long tenantId, String materialLotCode, List<HmeEoJobDataRecord> eoJobDataRecordList, HmeProcessNcHeaderVO2 processNcInfo) {
        //????????????
        if (CollectionUtils.isEmpty(eoJobDataRecordList)
                || Objects.isNull(processNcInfo) || StringUtils.isBlank(processNcInfo.getHeaderId())) {
            return null;
        }

        //V20210308 modify by penglin.sui for peng.zhao ?????????????????????????????????????????????
        List<String> tagIdList = processNcInfo.getLineList().stream().map(HmeProcessNcLineVO2::getTagId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(tagIdList)){
            return null;
        }
        eoJobDataRecordList = eoJobDataRecordList.stream().filter(item -> tagIdList.contains(item.getTagId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eoJobDataRecordList)) {
            return null;
        }

        if (CollectionUtils.isEmpty(processNcInfo.getLineList())) {
            //???SN???{$1}??????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }

        //?????????????????????????????????????????????
        Optional<HmeProcessNcLineVO2> noPriorityLineOptional = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isBlank(item.getPriority())).findAny();
        if (noPriorityLineOptional.isPresent()) {
            HmeProcessNcLineVO2 noProcessNcLine = noPriorityLineOptional.get();
            //????????????${1}?????????????????????,?????????!
            throwNoProcessNcMtException(tenantId, noProcessNcLine.getTagId(), "HME_EO_JOB_SN_182");
        }

        //???????????????????????????
        long priorityCount = processNcInfo.getLineList().stream().map(HmeProcessNcLineVO2::getPriority).distinct().count();
        if (priorityCount != processNcInfo.getLineList().size()) {
            //??????????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_183", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_183", "HME"));
        }

        //???????????????????????????
        List<HmeProcessNcLineVO2> priorityLineList = processNcInfo.getLineList().stream()
                .sorted(Comparator.comparing(HmeProcessNcLineVO2::getPriority)).collect(Collectors.toList());
        List<HmeProcessNcDetailVO2> hmeProcessNcDetailVO2List = new ArrayList<>();
        for (HmeProcessNcLineVO2 processNcLine : priorityLineList) {
            if (CollectionUtils.isEmpty(processNcLine.getDetailList())) {
                //????????????${1}????????????????????????,?????????!
                throwNoProcessNcMtException(tenantId, processNcLine.getTagId(), "HME_EO_JOB_SN_184");
            }

            //???????????????????????????????????????????????????
            Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                    .filter(item -> StringUtils.equals(processNcLine.getTagId(), item.getTagId())).findAny();

            if (!currEoJobDataRecordOptional.isPresent()) {
                //???????????????${1}???????????????????????????
                throwNoProcessNcMtException(tenantId, processNcLine.getTagId(), "HME_EO_JOB_SN_177");
            }

            //???????????????????????????????????????
            HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

            //?????????????????????????????????
            try {
                BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
            } catch (Exception e) {
                //??????????????????{$1}???????????????,?????????
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
            }
            BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

            //??????????????????????????????????????????
            Optional<HmeProcessNcDetailVO2> processNcDetailFind = processNcLine.getDetailList().stream()
                    .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                            && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

            //???????????????????????????????????????
            if (!processNcDetailFind.isPresent()) {
                //????????????{$1}???????????????????????????,?????????!
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
            }

            //???????????????????????????
            HmeProcessNcDetailVO2 processNcDetail = processNcDetailFind.get();

            //????????????????????????????????????????????????
            //V20210519 modify by penglin.sui for peng.zhao ??????????????????????????????????????????
            if (StringUtils.isNotBlank(processNcDetail.getNcCodeId())) {
//                return returnSnProcessNc(tenantId, materialLotCode, processNcDetail);
                hmeProcessNcDetailVO2List.add(processNcDetail);
            }
        }

        if(CollectionUtils.isNotEmpty(hmeProcessNcDetailVO2List)){
            return returnSnMultipleProcessNc(tenantId,materialLotCode,hmeProcessNcDetailVO2List);
        }

        return null;
    }

    /**
     *
     * @Description ???????????????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/2/4 10:12
     * @param tenantId ??????ID
     * @param operationId ??????ID
     * @return boolean true:???????????? false:????????????
     *
     */
    @Override
    public boolean isAgeingNc(Long tenantId, String operationId) {
        String operationAgeingFlag = hmeEoJobSnMapper.queryOperationAgeingFlag(tenantId, operationId);
        return StringUtils.isNotBlank(operationAgeingFlag) && HmeConstants.ConstantValue.YES.equals(operationAgeingFlag);
    }

    @Override
    public boolean isFirstProcess(Long tenantId, String operationId) {
        String firstProcess = hmeEoJobSnMapper.queryFirstProcessFlag(tenantId, operationId);
        return StringUtils.isNotBlank(firstProcess) && HmeConstants.ConstantValue.YES.equals(firstProcess);
    }

    @Override
    public boolean isNewMaterialLot(Long tenantId, String snNum) {
        List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtEo.FIELD_IDENTIFICATION, snNum)).build());
        boolean newFlag = false;
        if (CollectionUtils.isNotEmpty(mtEoList)) {
            if (StringCommonUtils.contains(mtEoList.get(0).getStatus(), "RELEASED", "WORKING")) {
                // ?????? ????????? ??????????????? ?????????????????????????????? ?????????????????? ?????????
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                    setAttrName("REWORK_MATERIAL_LOT");
                    setKeyId(mtEoList.get(0).getEoId());
                    setTableName("mt_eo_attr");
                }});
                String reworkMaterialLot = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                if (StringUtils.isBlank(reworkMaterialLot)) {
                    throw new MtException("HME_EO_JOB_SN_240", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_240", "HME"));
                }
                newFlag = true;
            } else if (StringUtils.equals(mtEoList.get(0).getStatus(), "COMPLETED")) {
                newFlag = false;
            }
        }
        return newFlag;
    }

    @Override
    public List<MtExtendAttrVO> queryOldCodeAttrList(Long tenantId, String eoId) {
        return hmeEoRelMapper.queryOldCodeAttrList(tenantId, eoId);
    }

    @Override
    public boolean isDeviceNc(Long tenantId, String operationId) {
        String operationDeviceFlag = hmeEoJobSnMapper.queryOperationDeviceFlag(tenantId, operationId);
        return StringUtils.isNotBlank(operationDeviceFlag) && HmeConstants.ConstantValue.YES.equals(operationDeviceFlag);
    }

    @Override
    public boolean isNearNormalProcess(Long tenantId, String eoId, String workcellId) {
        String processId = hmeEoJobSnMapper.queryNearNormalProcess(tenantId, eoId);
        Long countNum = hmeEoJobSnMapper.judgeProcessConformity(tenantId, processId, workcellId);
        return countNum.compareTo(0L) > 0;
    }

    @Override
    public boolean isReflectorNc(Long tenantId, String operationId) {
        String operationReflectorFlag = hmeEoJobSnMapper.queryOperationReflectorFlag(tenantId, operationId);
        return StringUtils.isNotBlank(operationReflectorFlag) && HmeConstants.ConstantValue.YES.equals(operationReflectorFlag);
    }

    @Override
    public HmeEoJobSn dataRecordReflectorProcessNcValidate(Long tenantId, String materialLotCode, List<HmeEoJobDataRecord> eoJobDataRecordList, HmeProcessNcHeaderVO2 processNcInfo) {
        //????????????
        if (CollectionUtils.isEmpty(eoJobDataRecordList)
                || Objects.isNull(processNcInfo) || StringUtils.isBlank(processNcInfo.getHeaderId())) {
            return null;
        }
        if (CollectionUtils.isEmpty(processNcInfo.getLineList())) {
            //???SN???{$1}??????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }

        //V20210308 modify by penglin.sui for peng.zhao ?????????????????????????????????????????????
        List<String> tagIdList = processNcInfo.getLineList().stream().map(HmeProcessNcLineVO2::getTagId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(tagIdList)){
            return null;
        }
        eoJobDataRecordList = eoJobDataRecordList.stream().filter(item -> tagIdList.contains(item.getTagId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eoJobDataRecordList)) {
            return null;
        }

        //???????????????????????????(??????????????????)?????????????????????
        List<HmeProcessNcLineVO2> noPriorityLineList = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isBlank(item.getPriority())).collect(Collectors.toList());
        List<HmeProcessNcLineVO2> priorityLineList = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isNotBlank(item.getPriority()))
                .sorted(Comparator.comparing(HmeProcessNcLineVO2::getPriority)).collect(Collectors.toList());

        Map<String, HmeProcessNcLineVO2> noPriorityLineMap = new HashMap<String, HmeProcessNcLineVO2>();
        if (CollectionUtils.isNotEmpty(noPriorityLineList)) {
            noPriorityLineList.forEach(item -> noPriorityLineMap.put(item.getStandardCode(), item));
        }

        //????????????????????????1????????????
        if (CollectionUtils.isEmpty(priorityLineList) || !"1".equals(priorityLineList.get(0).getPriority())) {
            //???SN???{$1}??????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }
        List<HmeProcessNcDetailVO2> hmeProcessNcDetailVO2List = new ArrayList<>();
        for (HmeProcessNcLineVO2 processNcLine : priorityLineList) {
            //???????????????????????????????????????????????????
            Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                    .filter(item -> StringUtils.equals(processNcLine.getTagId(), item.getTagId())).findAny();

            if (!currEoJobDataRecordOptional.isPresent()) {
                //???????????????${1}???????????????????????????
                throwNoProcessNcMtException(tenantId, processNcLine.getTagId(), "HME_EO_JOB_SN_177");
            }

            //???????????????????????????????????????
            HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

            //?????????????????????????????????
            try {
                BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
            } catch (Exception e) {
                //??????????????????{$1}???????????????,?????????
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
            }
            BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

            //??????????????????????????????????????????
            Optional<HmeProcessNcDetailVO2> processNcDetailFind = processNcLine.getDetailList().stream()
                    .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                            && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

            //???????????????????????????????????????
            if (!processNcDetailFind.isPresent()) {
                //????????????{$1}???????????????????????????,?????????!
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
            }

            //???????????????????????????
            HmeProcessNcDetailVO2 processNcDetail = processNcDetailFind.get();

            //????????????????????????????????????????????????
            if (StringUtils.isNotBlank(processNcDetail.getNcCodeId())) {
                processNcDetail.setTagId(currEoJobDataRecord.getTagId());
                hmeProcessNcDetailVO2List.add(processNcDetail);
            }

            //?????????1?????????????????????,?????????????????????????????????????????????
            if ("1".equals(processNcLine.getPriority()) && StringUtils.isNotBlank(processNcDetail.getDetailStandardCode())) {
                List<HmeProcessNcDetailVO2> resultList = dataRecordNoPriorityRecursionValidate2(tenantId, materialLotCode,
                        eoJobDataRecordList, processNcDetail, noPriorityLineMap);
                if(CollectionUtils.isNotEmpty(resultList)){
                    hmeProcessNcDetailVO2List.addAll(resultList);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(hmeProcessNcDetailVO2List)){
            return returnSnMultipleReflectorProcessNc(tenantId,materialLotCode,hmeProcessNcDetailVO2List);
        }
        return null;
    }

    @Override
    public void interceptValidate(Long tenantId, String workcellId, List<MtMaterialLot> mtMaterialLotList) {
        if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
            // ??????????????????????????????
            List<MtModOrganizationRel> organizationRelList = mtModOrganizationRelRepository.selectByCondition(Condition.builder(MtModOrganizationRel.class).select(MtModOrganizationRel.FIELD_PARENT_ORGANIZATION_ID).andWhere(Sqls.custom()
                    .andEqualTo(MtModOrganizationRel.FIELD_PARENT_ORGANIZATION_TYPE, "WORKCELL")
                    .andEqualTo(MtModOrganizationRel.FIELD_ORGANIZATION_TYPE, "WORKCELL")
                    .andEqualTo(MtModOrganizationRel.FIELD_ORGANIZATION_ID, workcellId)
                    .andEqualTo(MtModOrganizationRel.FIELD_TENANT_ID, tenantId)).build());
            if (CollectionUtils.isEmpty(organizationRelList)) {
                throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_002", "HME"));
            }
            List<String> materialLotIdList = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId).distinct().collect(Collectors.toList());

            String processId = organizationRelList.get(0).getParentOrganizationId();
            // ?????? ?????????????????? ??????????????????????????????
            List<HmeEoJobSnCommonVO> interceptNumList = hmeEoJobSnCommonMapper.queryInterceptNumList(tenantId, processId, materialLotIdList);
            // ????????????????????? ????????????????????????????????????sn
            Map<String, List<HmeEoJobSnCommonVO>> interceptNumMap = interceptNumList.stream().collect(Collectors.groupingBy(HmeEoJobSnCommonVO::getDimension));
            interceptNumMap.forEach((key, value) -> {
                switch (key) {
                    case "LAB_CODE" : this.labCodeValidate(tenantId, materialLotIdList, value); break;
                    case "SN" : this.snValidate(tenantId, mtMaterialLotList, value); break;
                    case "WO" : this.woValidate(tenantId, mtMaterialLotList, value); break;
                    case "LOT" : this.lotValidate(tenantId, mtMaterialLotList, value); break;
                    case "SUPPLIER_LOT": this.supplierLotValidate(tenantId, mtMaterialLotList, value); break;
                    default: break;
                }
            });
        }
    }

    @Override
    public boolean queryProcessValidateFlag(Long tenantId, String workcellId) {
        String validateFlag = hmeEoJobSnCommonMapper.queryProcessValidateFlag(tenantId, workcellId);
        return YES.equals(validateFlag) ? true : false;
    }

    @Override
    public Boolean isBindMoreWorkingEo(Long tenantId, String materialLotCode) {
        List<String> newEoList = hmeEoJobSnCommonMapper.isBindMoreWorkingEo(tenantId, materialLotCode);
        return CollectionUtils.isNotEmpty(newEoList) && newEoList.size() > 1;
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param materialLotIdList
     * @param interceptNumList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 15:18
     * @return void
     */
    private void labCodeValidate (Long tenantId, List<String> materialLotIdList, List<HmeEoJobSnCommonVO> interceptNumList) {
        if (CollectionUtils.isNotEmpty(interceptNumList)) {
            List<String> interceptIdList = interceptNumList.stream().map(HmeEoJobSnCommonVO::getInterceptId).distinct().collect(Collectors.toList());
            List<String> interceptIds = hmeEoJobSnCommonMapper.queryInterceptNumByLabCode(tenantId, materialLotIdList, interceptIdList);
            if (CollectionUtils.isNotEmpty(interceptIds)) {
                Optional<HmeEoJobSnCommonVO> firstOpt = interceptNumList.stream().filter(vo -> StringUtils.equals(vo.getInterceptId(), interceptIds.get(0))).findFirst();
                throw new MtException("HME_EO_JOB_SN_235", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_235", "HME", firstOpt.get().getRemark()));
            }
        }
    }

    /**
     * ??????SN
     *
     * @param tenantId
     * @param mtMaterialLotList
     * @param interceptNumList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 15:48
     * @return void
     */
    private void snValidate (Long tenantId, List<MtMaterialLot> mtMaterialLotList, List<HmeEoJobSnCommonVO> interceptNumList) {
        if (CollectionUtils.isNotEmpty(interceptNumList)) {
            List<String> interceptIdList = interceptNumList.stream().map(HmeEoJobSnCommonVO::getInterceptId).distinct().collect(Collectors.toList());
            List<String> materialLotCodeList = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotCode).collect(Collectors.toList());
            List<String> interceptIds = hmeEoJobSnCommonMapper.queryInterceptNumBySn(tenantId, materialLotCodeList, interceptIdList);
            if (CollectionUtils.isNotEmpty(interceptIds)) {
                Optional<HmeEoJobSnCommonVO> firstOpt = interceptNumList.stream().filter(vo -> StringUtils.equals(vo.getInterceptId(), interceptIds.get(0))).findFirst();
                throw new MtException("HME_EO_JOB_SN_235", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_235", "HME", firstOpt.get().getRemark()));
            }
        }
    }

    /**
     * ????????????
     *
     * @param tenantId
     * @param mtMaterialLotList
     * @param interceptNumList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 15:53
     * @return void
     */
    private void woValidate (Long tenantId, List<MtMaterialLot> mtMaterialLotList, List<HmeEoJobSnCommonVO> interceptNumList) {
        if (CollectionUtils.isNotEmpty(interceptNumList)) {
            List<String> interceptIdList = interceptNumList.stream().map(HmeEoJobSnCommonVO::getInterceptId).distinct().collect(Collectors.toList());
            List<String> materialLotCodeList = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotCode).collect(Collectors.toList());
            List<String> interceptIds = hmeEoJobSnCommonMapper.queryInterceptNumByWo(tenantId, materialLotCodeList, interceptIdList);
            if (CollectionUtils.isNotEmpty(interceptIds)) {
                Optional<HmeEoJobSnCommonVO> firstOpt = interceptNumList.stream().filter(vo -> StringUtils.equals(vo.getInterceptId(), interceptIds.get(0))).findFirst();
                throw new MtException("HME_EO_JOB_SN_235", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_235", "HME", firstOpt.get().getRemark()));
            }
        }
    }

    /**
     * ????????????
     *
     * @param tenantId
     * @param mtMaterialLotList
     * @param interceptNumList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 15:54
     * @return void
     */
    private void lotValidate (Long tenantId, List<MtMaterialLot> mtMaterialLotList, List<HmeEoJobSnCommonVO> interceptNumList) {
        if (CollectionUtils.isNotEmpty(interceptNumList)) {
            List<String> interceptIdList = interceptNumList.stream().map(HmeEoJobSnCommonVO::getInterceptId).distinct().collect(Collectors.toList());
            // ??????SN ???????????????????????????
            List<String> eoIdList = mtMaterialLotList.stream().map(MtMaterialLot::getEoId).collect(Collectors.toList());
            // ??????SN?????????????????????????????????????????? ??????SN ????????????
            List<HmeEoJobSnCommonVO2> feedMaterialLotList = this.queryFeedMaterialLotCodeList(tenantId, eoIdList);
            if (CollectionUtils.isNotEmpty(feedMaterialLotList)) {
                List<String> interceptIds = hmeEoJobSnCommonMapper.queryInterceptNumByLot(tenantId, feedMaterialLotList, interceptIdList);
                if (CollectionUtils.isNotEmpty(interceptIds)) {
                    Optional<HmeEoJobSnCommonVO> firstOpt = interceptNumList.stream().filter(vo -> StringUtils.equals(vo.getInterceptId(), interceptIds.get(0))).findFirst();
                    throw new MtException("HME_EO_JOB_SN_235", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_235", "HME", firstOpt.get().getRemark()));
                }
            }
        }
    }

    /**
     * ????????????
     *
     * @param tenantId
     * @param eoIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/9 17:31
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnCommonVO2>
     */
    private List<HmeEoJobSnCommonVO2> queryFeedMaterialLotCodeList (Long tenantId, List<String> eoIdList) {
        List<HmeEoJobSnCommonVO2> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoIdList)) {
            // ??????SN????????????????????????
            List<HmeEoJobSnCommonVO2> snFeedMaterialLotList = hmeEoJobSnCommonMapper.querySnFeedMaterialLotCodeList(tenantId, eoIdList);
            if (CollectionUtils.isNotEmpty(snFeedMaterialLotList)) {
                resultList.addAll(snFeedMaterialLotList);
            }
            // ??????eo??????jobs ??????jobs??????????????????????????????
            List<String> jobIdList = hmeEoJobSnCommonMapper.queryJobIdListByEoIdList(tenantId, eoIdList);
            if (CollectionUtils.isNotEmpty(jobIdList)) {
                List<HmeEoJobSnCommonVO2> lotTimeFeedMaterialLotList = hmeEoJobSnCommonMapper.queryLotAndTimeFeedMaterialLotCodeList(tenantId, jobIdList);
                if (CollectionUtils.isNotEmpty(lotTimeFeedMaterialLotList)) {
                    resultList.addAll(lotTimeFeedMaterialLotList);
                }
            }
        }
        return resultList;
    }

    /**
     * ?????????????????????
     *
     * @param tenantId
     * @param mtMaterialLotList
     * @param interceptNumList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 16:12
     * @return void
     */
    private void supplierLotValidate (Long tenantId, List<MtMaterialLot> mtMaterialLotList, List<HmeEoJobSnCommonVO> interceptNumList) {
        if (CollectionUtils.isNotEmpty(interceptNumList)) {
            List<String> interceptIdList = interceptNumList.stream().map(HmeEoJobSnCommonVO::getInterceptId).distinct().collect(Collectors.toList());
            List<String> eoIdList = mtMaterialLotList.stream().map(MtMaterialLot::getEoId).collect(Collectors.toList());
            List<HmeEoJobSnCommonVO2> feedMaterialLotList = this.queryFeedMaterialLotCodeList(tenantId, eoIdList);
            if (CollectionUtils.isNotEmpty(feedMaterialLotList)) {
                List<String> supplierLotList = feedMaterialLotList.stream().map(HmeEoJobSnCommonVO2::getSupplierLot).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(supplierLotList)) {
                    List<String> interceptIds = hmeEoJobSnCommonMapper.queryInterceptNumBySupplierLot(tenantId, supplierLotList, interceptIdList);
                    if (CollectionUtils.isNotEmpty(interceptIds)) {
                        Optional<HmeEoJobSnCommonVO> firstOpt = interceptNumList.stream().filter(vo -> StringUtils.equals(vo.getInterceptId(), interceptIds.get(0))).findFirst();
                        throw new MtException("HME_EO_JOB_SN_235", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_235", "HME", firstOpt.get().getRemark()));
                    }
                }
            }
        }
    }

    /**
     *
     * @Description ???????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/25 11:23
     * @param tenantId ??????ID
     * @param materialLotCode ??????
     * @param eoJobDataRecordList ???????????????
     * @param processNcDetail ????????????????????????
     * @param currNoPriorityLineMap ?????????????????????????????????????????????????????????????????????
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn dataRecordNoPriorityRecursionValidate(Long tenantId,
                                                             String materialLotCode,
                                                             List<HmeEoJobDataRecord> eoJobDataRecordList,
                                                             HmeProcessNcDetailVO2 processNcDetail,
                                                             Map<String, HmeProcessNcLineVO2> currNoPriorityLineMap) {
        //??????????????????????????????????????????
        if (!currNoPriorityLineMap.containsKey(processNcDetail.getDetailStandardCode())) {
            //???????????????${1}?????????????????????????????????,?????????!
            throw new MtException("HME_EO_JOB_SN_178", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_178", "HME", processNcDetail.getDetailStandardCode()));
        }

        //??????????????????????????????????????????
        HmeProcessNcLineVO2 noPriorityProcessNcLine = currNoPriorityLineMap.get(processNcDetail.getDetailStandardCode());

        //???????????????????????????????????????????????????
        Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                .filter(item -> StringUtils.equals(noPriorityProcessNcLine.getTagId(), item.getTagId())).findAny();

        if (!currEoJobDataRecordOptional.isPresent()) {
            //???????????????${1}???????????????????????????
            throwNoProcessNcMtException(tenantId, noPriorityProcessNcLine.getTagId(), "HME_EO_JOB_SN_177");
        }

        //???????????????????????????????????????
        HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

        //?????????????????????????????????
        try {
            BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
        } catch (Exception e) {
            //??????????????????{$1}???????????????,?????????
            throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
        }
        BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

        //??????????????????????????????????????????
        Optional<HmeProcessNcDetailVO2> noPriorityProcessNcDetailFind = noPriorityProcessNcLine.getDetailList().stream()
                .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                        && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

        //???????????????????????????????????????
        if (!noPriorityProcessNcDetailFind.isPresent()) {
            //????????????{$1}???????????????????????????,?????????!
            throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
        }

        //???????????????????????????
        HmeProcessNcDetailVO2 noPriorityProcessNcDetail = noPriorityProcessNcDetailFind.get();

        //???????????????????????????????????????????????????
        /*if (StringUtils.isBlank(noPriorityProcessNcDetail.getNcCodeId())
                && StringUtils.isBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
            //????????????{$1}???????????????????????????,?????????!
            throwNoProcessNcMtException(tenantId, errorTag, "HME_EO_JOB_SN_170");
        }*/

        //????????????????????????????????????????????????
        if (StringUtils.isNotBlank(noPriorityProcessNcDetail.getNcCodeId())) {
            return returnSnProcessNc(tenantId, materialLotCode, noPriorityProcessNcDetail);
        }

        //???????????????????????????,????????????????????????????????????????????????
        if (StringUtils.isNotBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
            HmeEoJobSn validateResult = dataRecordNoPriorityRecursionValidate(tenantId, materialLotCode,
                    eoJobDataRecordList, noPriorityProcessNcDetail, currNoPriorityLineMap);
            if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
                //V20210409 modify by penglin.sui for hui.zhao ??????????????????????????????
                HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO = new HmeNcDisposePlatformDTO11();
                hmeNcDisposePlatformDTO.setMaterialLotCode(materialLotCode);
                hmeNcDisposePlatformDTO.setNcGroupId(processNcDetail.getNcGroupId());
                List<String> ncCodeIdList = new ArrayList<>();
                ncCodeIdList.add(processNcDetail.getNcCodeId());
                hmeNcDisposePlatformDTO.setNcCodeIdList(ncCodeIdList);
                validateResult.setHmeNcDisposePlatformDTO(hmeNcDisposePlatformDTO);
                return validateResult;
            }
        }

        return null;
    }

    /**
     *
     * @Description ???????????????????????????????????????
     *
     * @author penglin.sui
     * @date 2021/5/19 14:47
     * @param tenantId ??????ID
     * @param materialLotCode ??????
     * @param eoJobDataRecordList ???????????????
     * @param processNcDetail ????????????????????????
     * @param currNoPriorityLineMap ?????????????????????????????????????????????????????????????????????
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcDetailVO2>
     *
     */
    private List<HmeProcessNcDetailVO2> dataRecordNoPriorityRecursionValidate2(Long tenantId,
                                                                               String materialLotCode,
                                                                               List<HmeEoJobDataRecord> eoJobDataRecordList,
                                                                               HmeProcessNcDetailVO2 processNcDetail,
                                                                               Map<String, HmeProcessNcLineVO2> currNoPriorityLineMap) {
        //??????????????????????????????????????????
        if (!currNoPriorityLineMap.containsKey(processNcDetail.getDetailStandardCode())) {
            //???????????????${1}?????????????????????????????????,?????????!
            throw new MtException("HME_EO_JOB_SN_178", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_178", "HME", processNcDetail.getDetailStandardCode()));
        }

        //??????????????????????????????????????????
        HmeProcessNcLineVO2 noPriorityProcessNcLine = currNoPriorityLineMap.get(processNcDetail.getDetailStandardCode());

        //???????????????????????????????????????????????????
        Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                .filter(item -> StringUtils.equals(noPriorityProcessNcLine.getTagId(), item.getTagId())).findAny();

        if (!currEoJobDataRecordOptional.isPresent()) {
            //???????????????${1}???????????????????????????
            throwNoProcessNcMtException(tenantId, noPriorityProcessNcLine.getTagId(), "HME_EO_JOB_SN_177");
        }

        //???????????????????????????????????????
        HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

        //?????????????????????????????????
        try {
            BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
        } catch (Exception e) {
            //??????????????????{$1}???????????????,?????????
            throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
        }
        BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

        //??????????????????????????????????????????
        Optional<HmeProcessNcDetailVO2> noPriorityProcessNcDetailFind = noPriorityProcessNcLine.getDetailList().stream()
                .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                        && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

        //???????????????????????????????????????
        if (!noPriorityProcessNcDetailFind.isPresent()) {
            //????????????{$1}???????????????????????????,?????????!
            throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
        }

        //???????????????????????????
        HmeProcessNcDetailVO2 noPriorityProcessNcDetail = noPriorityProcessNcDetailFind.get();

        //???????????????????????????????????????????????????
        /*if (StringUtils.isBlank(noPriorityProcessNcDetail.getNcCodeId())
                && StringUtils.isBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
            //????????????{$1}???????????????????????????,?????????!
            throwNoProcessNcMtException(tenantId, errorTag, "HME_EO_JOB_SN_170");
        }*/
        List<HmeProcessNcDetailVO2> resultList = new ArrayList<>();
        //????????????????????????????????????????????????
        if (StringUtils.isNotBlank(noPriorityProcessNcDetail.getNcCodeId())) {
            noPriorityProcessNcDetail.setTagId(currEoJobDataRecord.getTagId());
            resultList.add(noPriorityProcessNcDetail);
        }

        //???????????????????????????,????????????????????????????????????????????????
        if (StringUtils.isNotBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
            List<HmeProcessNcDetailVO2> hmeProcessNcDetailVOList = dataRecordNoPriorityRecursionValidate2(tenantId, materialLotCode,
                    eoJobDataRecordList, noPriorityProcessNcDetail, currNoPriorityLineMap);
            if (CollectionUtils.isNotEmpty(hmeProcessNcDetailVOList)) {
                resultList.addAll(hmeProcessNcDetailVOList);
                return resultList;
            }
        }

        return resultList;
    }

    /**
     *
     * @Description ??????"????????????????????????"?????????
     *
     * @author yuchao.wang
     * @date 2021/1/24 16:33
     * @param tenantId ??????ID
     * @param materialLotCode ??????
     * @param processNcDetail ??????????????????
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn returnSnProcessNc(Long tenantId, String materialLotCode, HmeProcessNcDetailVO2 processNcDetail) {
        if (StringUtils.isBlank(processNcDetail.getNcCode())) {
            //???????????????${1}????????????,?????????!
            throw new MtException("HME_COS_RETEST_IMPORT_009", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_RETEST_IMPORT_009", "HME", processNcDetail.getNcCodeId()));
        }

        //?????????{$1}?????????????????????,????????????????????????{$2}???
        HmeEoJobSn resultJobSn = new HmeEoJobSn();
        resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
        resultJobSn.setErrorMessage(mtErrorMessageRepository
                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_172", "HME", materialLotCode, processNcDetail.getNcCode(), processNcDetail.getNcDescription()));
        //??????????????????????????????
        HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO = new HmeNcDisposePlatformDTO11();
        hmeNcDisposePlatformDTO.setMaterialLotCode(materialLotCode);
        hmeNcDisposePlatformDTO.setNcGroupId(processNcDetail.getNcGroupId());
        List<String> ncCodeIdList = new ArrayList<>();
        ncCodeIdList.add(processNcDetail.getNcCodeId());
        hmeNcDisposePlatformDTO.setNcCodeIdList(ncCodeIdList);
        resultJobSn.setHmeNcDisposePlatformDTO(hmeNcDisposePlatformDTO);
        return resultJobSn;
    }


    /**
     * ??????"?????????????????????????????????"?????????
     *
     * @param tenantId
     * @param materialLotCode
     * @param processNcDetailList
     * @author sanfeng.zhang@hand-china.com 2021/9/7 9:11
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     */
    private HmeEoJobSn returnSnMultipleReflectorProcessNc(Long tenantId, String materialLotCode, List<HmeProcessNcDetailVO2> processNcDetailList) {
        HmeEoJobSn resultJobSn = new HmeEoJobSn();
        // ????????????????????????????????? ???????????????????????????
        List<String> ncTagIds = processNcDetailList.stream().map(HmeProcessNcDetailVO2::getTagId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, String>  mtTagMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ncTagIds)) {
            List<MtTag> mtTags = mtTagRepository.selectByCondition(Condition.builder(MtTag.class).select(MtTag.FIELD_TAG_ID, MtTag.FIELD_TAG_CODE).andWhere(Sqls.custom()
                    .andEqualTo(MtTag.FIELD_TENANT_ID, tenantId)
                    .andIn(MtTag.FIELD_TAG_ID, ncTagIds)).build());
            mtTagMap = mtTags.stream().collect(Collectors.toMap(MtTag::getTagId, MtTag::getTagCode));
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder aceSb = new StringBuilder();
        for (HmeProcessNcDetailVO2 processNcDetail : processNcDetailList
        ) {
            if (StringUtils.isBlank(processNcDetail.getNcCode())) {
                //???????????????${1}????????????,?????????!
                throw new MtException("HME_COS_RETEST_IMPORT_009", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_RETEST_IMPORT_009", "HME", processNcDetail.getNcCodeId()));
            }
            String tagCode = mtTagMap.get(processNcDetail.getTagId());
            if (StringUtils.isNotBlank(tagCode)) {
                String[] split = StringUtils.split(tagCode, "-");
                if (split.length < 3) {
                    throw new MtException("HME_EO_JOB_SN_230", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_230", "HME", tagCode));
                }
                if (split[2].startsWith("COS")) {
                    if (sb.length() == 0) {
                        sb.append("???" + split[2] + "???");
                    } else {
                        // ?????????????????????
                        if (!sb.toString().contains(split[2])) {
                            sb.append("??????" + split[2] + "???");
                        }
                    }
                    processNcDetail.setCosCode(split[2]);
                    processNcDetail.setSpliceCosCode("???" + split[2] + "???");
                } else {
                    aceSb.append(tagCode);
                }
            }
            //??????????????????????????????
            HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO = new HmeNcDisposePlatformDTO11();
            hmeNcDisposePlatformDTO.setMaterialLotCode(materialLotCode);
            hmeNcDisposePlatformDTO.setNcGroupId(processNcDetail.getNcGroupId());
            List<String> ncCodeIdList = new ArrayList<>();
            ncCodeIdList.add(processNcDetail.getNcCodeId());
            hmeNcDisposePlatformDTO.setNcCodeIdList(ncCodeIdList);
            resultJobSn.setHmeNcDisposePlatformDTO(hmeNcDisposePlatformDTO);
        }
        if (sb.length() > 0) {
            // ???????????? -> COS01 COS02 ...
            List<String> spliceCosCodeList = processNcDetailList.stream().filter(pnc -> StringUtils.isNotBlank(pnc.getCosCode()))
                    .sorted(Comparator.comparing(HmeProcessNcDetailVO2::getCosCode))
                    .map(HmeProcessNcDetailVO2::getSpliceCosCode).distinct().collect(Collectors.toList());
            String spliceCosCode = StringUtils.join(spliceCosCodeList, ";");
            if (aceSb.length() > 0) {
                // ?????????${1}?????????????????????????????????,?????????????????????,????????????${2}???
                resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
                resultJobSn.setErrorMessage(mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_243 ", "HME", materialLotCode, spliceCosCode));
                resultJobSn.setProcessNcDetailList(processNcDetailList);
            } else {
                // ?????????${1}?????????????????????????????????,?????????????????? ${2}???
                resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
                resultJobSn.setErrorMessage(mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_221 ", "HME", materialLotCode, spliceCosCode));
                resultJobSn.setProcessNcDetailList(processNcDetailList);
            }
        } else if (aceSb.length() > 0){
            // ??????????????????
            // ?????????${1}?????????????????????????????????
            resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
            resultJobSn.setErrorMessage(mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_242 ", "HME", materialLotCode));
            resultJobSn.setProcessNcDetailList(processNcDetailList);
        }
        return resultJobSn;
    }
    /**
     *
     * @Description ??????"????????????????????????"?????????
     *
     * @author penglin.sui
     * @date 2021/5/19 14:11
     * @param tenantId ??????ID
     * @param materialLotCode ??????
     * @param processNcDetailList ????????????????????????
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn returnSnMultipleProcessNc(Long tenantId, String materialLotCode, List<HmeProcessNcDetailVO2> processNcDetailList) {
        HmeEoJobSn resultJobSn = new HmeEoJobSn();
        StringBuilder stringBuilder = new StringBuilder();
        for (HmeProcessNcDetailVO2 processNcDetail : processNcDetailList) {
            if (StringUtils.isBlank(processNcDetail.getNcCode())) {
                //???????????????${1}????????????,?????????!
                throw new MtException("HME_COS_RETEST_IMPORT_009", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_RETEST_IMPORT_009", "HME", processNcDetail.getNcCodeId()));
            }

            if(stringBuilder.length() == 0){
                stringBuilder.append("???" + processNcDetail.getNcCode() + "|" + processNcDetail.getNcDescription() + "???");
            }else{
                stringBuilder.append("??????" + processNcDetail.getNcCode() + "|" + processNcDetail.getNcDescription() + "???");
            }

            //??????????????????????????????
            HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO = new HmeNcDisposePlatformDTO11();
            hmeNcDisposePlatformDTO.setMaterialLotCode(materialLotCode);
            hmeNcDisposePlatformDTO.setNcGroupId(processNcDetail.getNcGroupId());
            List<String> ncCodeIdList = new ArrayList<>();
            ncCodeIdList.add(processNcDetail.getNcCodeId());
            hmeNcDisposePlatformDTO.setNcCodeIdList(ncCodeIdList);
            resultJobSn.setHmeNcDisposePlatformDTO(hmeNcDisposePlatformDTO);
        }
        if(stringBuilder.length() > 0) {
            //?????????${1}?????????????????????,?????????????????? ${2}
            resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
            resultJobSn.setErrorMessage(mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_172", "HME", materialLotCode, stringBuilder.toString()));
            resultJobSn.setProcessNcDetailList(processNcDetailList);
        }

        return resultJobSn;
    }

    /**
     *
     * @Description ???????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/24 16:27
     * @param tenantId ??????ID
     * @param errorTagId ?????????ID
     * @param errorCode ????????????
     * @return void
     *
     */
    private void throwNoProcessNcMtException(Long tenantId, String errorTagId, String errorCode) {
        String errorTag = errorTagId;
        if (StringUtils.isNotBlank(errorTagId)) {
            MtTag mtTag = mtTagRepository.tagGet(tenantId, errorTagId);
            if (Objects.isNull(mtTag) || StringUtils.isBlank(mtTag.getTagId())) {
                //????????????${1}????????????,?????????!
                throw new MtException("HME_EXCEL_IMPORT_028", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EXCEL_IMPORT_028", "HME", errorTagId));
            }
            errorTag = mtTag.getTagCode();
        }

        throw new MtException(errorCode, mtErrorMessageRepository
                .getErrorMessageWithModule(tenantId, errorCode, "HME", errorTag));
    }

    /**
     *
     * @Description ???????????????????????????-??????????????????
     *
     * @author yuchao.wang
     * @date 2020/12/31 15:59
     * @param tenantId ??????ID
     * @param dto ??????
     * @param bomId BOM ID
     * @return void
     *
     */
    private void materialInSiteForSingle(Long tenantId, HmeEoJobSnVO2 dto, String bomId) {
        //????????????????????????
        List<HmeBomComponentVO2> allBomComponentList = hmeEoJobSnMapper.queryEoComponentForMaterialInSite(tenantId, dto.getSiteId(), bomId);
        if (CollectionUtils.isEmpty(allBomComponentList)) {
            return;
        }

        //?????? ???????????????????????????
        List<HmeBomComponentVO2> bomComponentList = allBomComponentList.stream().filter(item -> !"2".equals(item.getBackFlashFlag())
                && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualFlag())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bomComponentList)) {
            return;
        }

        //??????
        Optional<HmeBomComponentVO2> find = bomComponentList.stream().filter(item ->
                StringUtils.isBlank(item.getMaterialSiteId()) || StringUtils.isBlank(item.getMaterialCode())
                        || (HmeConstants.MaterialTypeCode.TIME.equals(item.getLotType()) && StringUtils.isBlank(item.getAvailableTime()))
        ).findAny();
        if (find.isPresent()) {
            HmeBomComponentVO2 findComponent = find.get();
            //???????????????????????????????????????
            if (StringUtils.isBlank(findComponent.getMaterialSiteId())) {
                //????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
            }

            //????????????????????????
            if (StringUtils.isBlank(findComponent.getMaterialCode())) {
                //${1}????????? ?????????${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "??????", findComponent.getMaterialId()));
            }

            //?????????????????????????????????????????????
            if (HmeConstants.MaterialTypeCode.TIME.equals(findComponent.getLotType()) && StringUtils.isBlank(findComponent.getAvailableTime())) {
                //???????????????${1}??????????????????????????????
                throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_070", "HME", findComponent.getMaterialCode()));
            }
        }

        //????????????HME.EO_JOB_SN_UOM
        List<String> typeLovValueList = new ArrayList<>();
        List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.EO_JOB_SN_UOM", tenantId);
        if (CollectionUtils.isNotEmpty(typeLov)) {
            typeLovValueList = typeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }

        //?????????????????????????????????????????????
        for (HmeBomComponentVO2 bomComponent : bomComponentList) {
            //?????????????????????????????????????????????????????????
            String lotType = StringUtils.isBlank(bomComponent.getLotType()) ? HmeConstants.MaterialTypeCode.LOT : bomComponent.getLotType();
            BigDecimal componentQty = new BigDecimal(String.valueOf(bomComponent.getQty()));

            //?????????????????????????????????????????????
            if (HmeConstants.MaterialTypeCode.SN.equals(lotType)) {
                //??????????????????????????????????????????????????????
                boolean isSplitLine = typeLovValueList.contains(bomComponent.getUomType());

                //?????????????????????
                hmeEoJobMaterialRepository.initJobMaterialWithoutQuery(tenantId, bomComponent.getMaterialId(),
                        isSplitLine, componentQty, bomComponent.getBomComponentId(), dto);
            } else if (HmeConstants.MaterialTypeCode.LOT.equals(lotType)) {
                //?????????????????????
                hmeEoJobLotMaterialRepository.initLotMaterialWithoutQuery(tenantId, bomComponent.getMaterialId(), componentQty, dto);
            } else if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())
                    && HmeConstants.MaterialTypeCode.TIME.equals(lotType)) {
                //?????????????????????
                hmeEoJobTimeMaterialRepository.initTimeMaterialWithoutQuery(tenantId, bomComponent.getMaterialId(),
                        bomComponent.getAvailableTime(), componentQty, dto);
            }
        }
    }

    /**
     *
     * @Description ????????????????????????BOM_FLAG
     *
     * @author yuchao.wang
     * @date 2021/1/29 10:46
     * @param tenantId ??????ID
     * @param operationId ??????ID
     * @return boolean BOM_FLAG=Y:true ??????:false
     *
     */
    @Override
    public boolean queryOperationBomFlag(Long tenantId, String operationId) {
        String operationBomFlag = hmeEoJobSnMapper.queryOperationBomFlag(tenantId, operationId);
        return StringUtils.isNotBlank(operationBomFlag) && HmeConstants.ConstantValue.YES.equals(operationBomFlag);
    }

    @Override
    public List<HmeEoJobDataRecordVO2> queryNgDataRecord(Long tenantId, String workcellId, List<String> jobIdList) {
        List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnMapper.queryValueNgDataRecord(tenantId,workcellId,jobIdList);
        List<HmeEoJobDataRecordVO2> ngDataRecordList2 = new ArrayList<>();
        for (HmeEoJobDataRecordVO2 hmeEoJobDataRecordVO2:ngDataRecordList
        ) {
            if(HmeConstants.ValueType.VALUE.equals(hmeEoJobDataRecordVO2.getValueType())
                    || HmeConstants.ValueType.FORMULA.equals(hmeEoJobDataRecordVO2.getValueType())){
                if(StringUtils.isNotBlank(hmeEoJobDataRecordVO2.getResult())){
                    BigDecimal result = new BigDecimal(hmeEoJobDataRecordVO2.getResult());
                    if((Objects.nonNull(hmeEoJobDataRecordVO2.getMinimumValue()) && result.compareTo(hmeEoJobDataRecordVO2.getMinimumValue()) < 0)
                            || (Objects.nonNull(hmeEoJobDataRecordVO2.getMaximalValue()) && result.compareTo(hmeEoJobDataRecordVO2.getMaximalValue()) > 0)){
                        ngDataRecordList2.add(hmeEoJobDataRecordVO2);
                    }
                }
            }else{
                if(HmeConstants.ConstantValue.NG.equals(hmeEoJobDataRecordVO2.getResult())) {
                    ngDataRecordList2.add(hmeEoJobDataRecordVO2);
                }
            }
        }
        return ngDataRecordList2;
    }

    /**
     *
     * @Description ?????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/6 15:29
     * @param tenantId ??????ID
     * @param eoMap eo
     * @param woMap wo
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    private Map<String, String> getRelatedLineNum(Long tenantId, Map<String, HmeEoVO4> eoMap, Map<String, HmeWorkOrderVO2> woMap) {
        Map<String, String> relatedLineNumMap = new HashMap<>();
        //????????????????????????attribute30
        List<HmeEoVO4> eoVO4List = eoMap.values().stream()
                .filter(item -> !item.getMaterialId().equals(woMap.get(item.getWorkOrderId()).getMaterialId()))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(eoVO4List)) {
            List<String> eoIds = eoVO4List.stream().map(HmeEoVO4::getEoId).distinct().collect(Collectors.toList());
            List<String> materialIds = eoVO4List.stream().map(HmeEoVO4::getMaterialId).distinct().collect(Collectors.toList());
            List<HmeEoVO4> attrList = hmeEoJobSnMapper.batchQueryBomComponentAttrByEo(tenantId, eoIds, materialIds, "lineAttribute24");
            if (CollectionUtils.isNotEmpty(attrList)) {
                attrList.forEach(item -> relatedLineNumMap.put(item.getEoId() + "-" + item.getMaterialId(), item.getRelatedLineNum()));
            }
        }
        return relatedLineNumMap;
    }

    /**
     *
     * @Description ??????MES_RK06??????????????????????????????SAP??? key:woId-snNum value:?????????????????????SAP???
     *
     * @author yuchao.wang
     * @date 2020/11/6 14:07
     * @param tenantId ??????ID
     * @param eoMap eo
     * @param woMap wo
     * @param snLineList ????????????
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    private Map<String, HmeServiceSplitRecordVO2> getRk06InternalOrderNum(Long tenantId,
                                                                          Map<String, HmeEoVO4> eoMap,
                                                                          Map<String, HmeWorkOrderVO2> woMap,
                                                                          List<HmeEoJobSnVO3> snLineList) {
        Map<String, HmeServiceSplitRecordVO2> internalOrderNumMap = new HashMap<>();

        //???????????????????????????MES_RK06
        List<HmeWorkOrderVO2> rk06WoList = woMap.values().stream().filter(item -> "MES_RK06".equals(item.getWorkOrderType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(rk06WoList)) {
            //?????????rk06???????????????
            List<String> rk06WoIdList = rk06WoList.stream().map(HmeWorkOrderVO2::getWorkOrderId).collect(Collectors.toList());
            List<String> rk06SnList = snLineList.stream()
                    .filter(item -> rk06WoIdList.contains(eoMap.get(item.getEoId()).getWorkOrderId()))
                    .map(HmeEoJobSnVO3::getSnNum).collect(Collectors.toList());

            //??????????????????????????????/SAP??????
            List<HmeServiceSplitRecordVO2> serviceSplitRecordVO2List = hmeServiceSplitRecordMapper.batchQueryWoNumBySnNumAndWoId(tenantId, rk06WoIdList, rk06SnList);
            if (CollectionUtils.isEmpty(serviceSplitRecordVO2List)) {
                //??????????????????????????????
                throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_084", "HME"));
            }

            //????????????+????????????
            Map<String, List<HmeServiceSplitRecordVO2>> splitRecordMap = serviceSplitRecordVO2List
                    .stream().collect(Collectors.groupingBy(item -> item.getWorkOrderId() + "," + item.getSnNum()));

            //???????????????????????????woId????????????
            if (Objects.isNull(splitRecordMap) || splitRecordMap.size() != rk06WoIdList.size()) {
                //??????????????????????????????
                throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_084", "HME"));
            }

            //???????????????/SAP??????
            for (Map.Entry<String, List<HmeServiceSplitRecordVO2>> entry : splitRecordMap.entrySet()) {
                if (CollectionUtils.isEmpty(entry.getValue())) {
                    throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_084", "HME"));
                }

                //??????????????????????????????,??????????????????
                List<HmeServiceSplitRecordVO2> values = entry.getValue().stream().sorted(Comparator
                        .comparing(HmeServiceSplitRecordVO2::getCreationDate, Comparator.reverseOrder())).collect(Collectors.toList());

                //???????????????????????????????????????????????????????????????SAP??????
                HmeServiceSplitRecordVO2 item = new HmeServiceSplitRecordVO2();
                if (StringUtils.isNotBlank(values.get(0).getInternalOrderNum())) {
                    item.setOrderNum(values.get(0).getInternalOrderNum());
                    item.setOrderNumType("INTERNAL_ORDER");
                    internalOrderNumMap.put(entry.getKey(), item);
                } else if (StringUtils.isNotBlank(values.get(0).getSapOrderNum())) {
                    item.setOrderNum(values.get(0).getSapOrderNum());
                    item.setOrderNumType("SAP_ORDER");
                    internalOrderNumMap.put(entry.getKey(), item);
                } else {
                    //???????????????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_085", "HME"));
                }
            }
        }
        return internalOrderNumMap;
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/6 11:00
     * @param tenantId ??????ID
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    private Map<String, String> getMoveTypeForMainOutSite(Long tenantId) {
        Map<String, String> moveTypeMap = new HashMap<>();
        List<String> transactionTypeCodeList = new ArrayList<>();
        transactionTypeCodeList.add("HME_WO_COMPLETE");
        transactionTypeCodeList.add("HME_WORK_REPORT");
        transactionTypeCodeList.add("WMS_INSDID_ORDER_S_R");
        List<WmsTransactionType> transactionTypes = transactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class)
                .andWhere(Sqls.custom().andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                        .andIn(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, transactionTypeCodeList)
                ).build());
        if (CollectionUtils.isNotEmpty(transactionTypes)) {
            transactionTypes.forEach(item -> moveTypeMap.put(item.getTransactionTypeCode(), item.getMoveType()));
        }
        return moveTypeMap;
    }

    /**
     *
     * @Description ????????????API??????
     *
     * @author yuchao.wang
     * @date 2020/11/6 15:33
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO>
     *
     */
    private List<WmsObjectTransactionRequestVO> getTransactionRequestVOForSingleList(Long tenantId,
                                                                                     String eoCompleteEventId,
                                                                                     HmeEoJobSnVO3 dto,
                                                                                     HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic,
                                                                                     MtModLocator mtModLocator,
                                                                                     MtModLocator warehouse,
                                                                                     Map<String, String> moveTypeMap) {
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
        MtRouterStep currentStep = hmeEoJobSnSingleBasic.getCurrentStep();

        //????????????ID
        String batchId = Utils.getBatchId();

        //??????????????????
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        WmsObjectTransactionRequestVO woCompleteTransactionRequestVO = new WmsObjectTransactionRequestVO();
        woCompleteTransactionRequestVO.setTransactionTypeCode("HME_WO_COMPLETE");
        woCompleteTransactionRequestVO.setEventId(eoCompleteEventId);
        woCompleteTransactionRequestVO.setMaterialLotId(dto.getMaterialLotId());
        woCompleteTransactionRequestVO.setMaterialId(eo.getMaterialId());
        woCompleteTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(eo.getQty()));
        woCompleteTransactionRequestVO.setLotNumber(StringUtils.trimToEmpty(hmeEoJobSnSingleBasic.getLotCode()));
        woCompleteTransactionRequestVO.setTransactionUom(eo.getUomCode());
        woCompleteTransactionRequestVO.setTransactionTime(new Date());
        woCompleteTransactionRequestVO.setPlantId(eo.getSiteId());
        woCompleteTransactionRequestVO.setWarehouseId(warehouse.getLocatorId());
        woCompleteTransactionRequestVO.setWarehouseCode(warehouse.getLocatorCode());
        woCompleteTransactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
        woCompleteTransactionRequestVO.setLocatorCode(mtModLocator.getLocatorCode());
        woCompleteTransactionRequestVO.setWorkOrderNum(wo.getWorkOrderNum());
        woCompleteTransactionRequestVO.setOperationSequence(currentStep.getStepName());
        woCompleteTransactionRequestVO.setContainerId(materialLot.getCurrentContainerId());
        woCompleteTransactionRequestVO.setProdLineCode(eo.getProdLineCode());
        woCompleteTransactionRequestVO.setSoNum(wo.getSoNum());
        woCompleteTransactionRequestVO.setSoLineNum(wo.getSoLineNum());
        woCompleteTransactionRequestVO.setAttribute16(batchId);

        //???????????????????????????MES_RK06 ???????????????????????? modify by yuchao.wang for jiao.chen at 2020.9.28
        if ("MES_RK06".equals(wo.getWorkOrderType())) {
            //????????????????????????????????????????????????SAP??????
            HmeServiceSplitRecordVO2 orderNum = hmeEoJobSnSingleBasic.getRk06InternalOrderNum();
            if ("INTERNAL_ORDER".equals(orderNum.getOrderNumType())) {
                //????????????????????????????????????,??????????????????
                woCompleteTransactionRequestVO.setTransactionTypeCode("WMS_INSDID_ORDER_S_R");
            }
            woCompleteTransactionRequestVO.setWorkOrderNum(orderNum.getOrderNum());
            woCompleteTransactionRequestVO.setMoveType(moveTypeMap.get(woCompleteTransactionRequestVO.getTransactionTypeCode()));
        } else {
            //??????????????????
            WmsObjectTransactionRequestVO woReportTransactionRequestVO = new WmsObjectTransactionRequestVO();
            BeanUtils.copyProperties(woCompleteTransactionRequestVO, woReportTransactionRequestVO);
            woReportTransactionRequestVO.setTransactionTypeCode("HME_WORK_REPORT");
            woReportTransactionRequestVO.setMoveType(moveTypeMap.get(woReportTransactionRequestVO.getTransactionTypeCode()));

            woCompleteTransactionRequestVO.setMoveType(moveTypeMap.get(woCompleteTransactionRequestVO.getTransactionTypeCode()));
            objectTransactionRequestList.add(woReportTransactionRequestVO);
        }

        //101????????????,EO???WO?????????????????? ?????????????????? add by yuchao.wang for tianyang.xie at 2020.11.04
        if("101".equals(woCompleteTransactionRequestVO.getMoveType())
                && !eo.getMaterialId().equals(wo.getMaterialId())) {
            String attrName = hmeEoJobSnMapper.queryBomComponentAttrByEoId(tenantId, eo.getEoId(), eo.getMaterialId(), "lineAttribute24");
            woCompleteTransactionRequestVO.setAttribute30(attrName);
        }

        objectTransactionRequestList.add(woCompleteTransactionRequestVO);
        return objectTransactionRequestList;
    }

    /**
     *
     * @Description ????????????API??????
     *
     * @author yuchao.wang
     * @date 2020/11/6 15:33
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO>
     *
     */
    private List<WmsObjectTransactionRequestVO> getTransactionRequestVOList(Long tenantId,
                                                                            String eoCompleteEventId,
                                                                            String routerStepName,
                                                                            String lotCode,
                                                                            HmeEoJobSnVO3 jobSn,
                                                                            HmeEoVO4 hmeEo,
                                                                            HmeWorkOrderVO2 hmeWo,
                                                                            MtModLocator mtModLocator,
                                                                            MtModLocator warehouse,
                                                                            Map<String, String> moveTypeMap,
                                                                            Map<String, String> currentContainerMap,
                                                                            Map<String, HmeServiceSplitRecordVO2> internalOrderNumMap,
                                                                            Map<String, String> relatedLineNumMap) {
        //??????????????????
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        WmsObjectTransactionRequestVO woCompleteTransactionRequestVO = new WmsObjectTransactionRequestVO();
        woCompleteTransactionRequestVO.setTransactionTypeCode("HME_WO_COMPLETE");
        woCompleteTransactionRequestVO.setEventId(eoCompleteEventId);
        woCompleteTransactionRequestVO.setMaterialLotId(jobSn.getMaterialLotId());
        woCompleteTransactionRequestVO.setMaterialId(hmeEo.getMaterialId());
        woCompleteTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(hmeEo.getQty()));
        woCompleteTransactionRequestVO.setLotNumber(lotCode);
        woCompleteTransactionRequestVO.setTransactionUom(hmeEo.getUomCode());
        woCompleteTransactionRequestVO.setTransactionTime(new Date());
        woCompleteTransactionRequestVO.setPlantId(hmeEo.getSiteId());
        woCompleteTransactionRequestVO.setWarehouseId(warehouse.getLocatorId());
        woCompleteTransactionRequestVO.setWarehouseCode(warehouse.getLocatorCode());
        woCompleteTransactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
        woCompleteTransactionRequestVO.setLocatorCode(mtModLocator.getLocatorCode());
        woCompleteTransactionRequestVO.setWorkOrderNum(hmeWo.getWorkOrderNum());
        woCompleteTransactionRequestVO.setOperationSequence(routerStepName);
        woCompleteTransactionRequestVO.setContainerId(currentContainerMap.get(jobSn.getMaterialLotId()));
        woCompleteTransactionRequestVO.setProdLineCode(hmeEo.getProdLineCode());
        woCompleteTransactionRequestVO.setSoNum(hmeWo.getSoNum());
        woCompleteTransactionRequestVO.setSoLineNum(hmeWo.getSoLineNum());

        //???????????????????????????MES_RK06 ???????????????????????? modify by yuchao.wang for jiao.chen at 2020.9.28
        if ("MES_RK06".equals(hmeWo.getWorkOrderType())) {
            //????????????????????????????????????????????????SAP??????
            HmeServiceSplitRecordVO2 orderNum = internalOrderNumMap.get(hmeWo.getWorkOrderId() + "," + jobSn.getSnNum());
            if (Objects.isNull(orderNum) || StringUtils.isBlank(orderNum.getOrderNum())) {
                //???????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_085", "HME"));
            }
            if ("INTERNAL_ORDER".equals(orderNum.getOrderNumType())) {
                //????????????????????????????????????,??????????????????
                woCompleteTransactionRequestVO.setTransactionTypeCode("WMS_INSDID_ORDER_S_R");
            }
            woCompleteTransactionRequestVO.setWorkOrderNum(orderNum.getOrderNum());
            woCompleteTransactionRequestVO.setMoveType(moveTypeMap.get(woCompleteTransactionRequestVO.getTransactionTypeCode()));
        } else {
            //??????????????????
            WmsObjectTransactionRequestVO woReportTransactionRequestVO = new WmsObjectTransactionRequestVO();
            BeanUtils.copyProperties(woCompleteTransactionRequestVO, woReportTransactionRequestVO);
            woReportTransactionRequestVO.setTransactionTypeCode("HME_WORK_REPORT");
            woReportTransactionRequestVO.setMoveType(moveTypeMap.get(woReportTransactionRequestVO.getTransactionTypeCode()));

            woCompleteTransactionRequestVO.setMoveType(moveTypeMap.get(woCompleteTransactionRequestVO.getTransactionTypeCode()));
            objectTransactionRequestList.add(woReportTransactionRequestVO);
        }

        //101????????????,EO???WO?????????????????? ?????????????????? add by yuchao.wang for tianyang.xie at 2020.11.04
        if ("101".equals(woCompleteTransactionRequestVO.getMoveType())
                && !hmeEo.getMaterialId().equals(hmeWo.getMaterialId())) {
            woCompleteTransactionRequestVO.setAttribute30(relatedLineNumMap.get(hmeEo.getEoId() + "-" + hmeEo.getMaterialId()));
        }

        objectTransactionRequestList.add(woCompleteTransactionRequestVO);
        return objectTransactionRequestList;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @param hmeEoJobSnSingleBasic
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/2 15:18
     */
    @Override
    public List<WmsObjectTransactionResponseVO> mainOutSiteForInternalReworkProcess(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeServiceSplitRecord splitRecord) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDoneStep tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
//        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
        // ????????????????????????????????????????????? ???????????? ?????????
        List<LovValueDTO> locatorLovList = lovAdapter.queryLovValue("HME.CUSTMER_LOCATOR", tenantId);
        Optional<LovValueDTO> repairLocatorOpt = locatorLovList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "REPAIR_COMPLETE")).findFirst();
        if (!repairLocatorOpt.isPresent()) {
            throw new MtException("HME_REPAIR_SN_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0004", "HME"));
        }
        Optional<LovValueDTO> repairingOpt = locatorLovList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "REPAIRING")).findFirst();
        if (!repairingOpt.isPresent()) {
            throw new MtException("HME_REPAIR_SN_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0004", "HME"));
        }
        // ???????????????
        String repairLocatorCode = repairLocatorOpt.get().getMeaning();

        MtModLocator repairLocator = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setTenantId(tenantId);
            setLocatorCode(repairLocatorCode);
        }});
        if (Objects.isNull(repairLocator)) {
            throw new MtException("HME_REPAIR_SN_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0005", "HME", repairLocatorCode));
        }
        // ??????????????????????????????
        MtModLocator repairWareHouse = mtModLocatorRepository.selectByPrimaryKey(repairLocator.getParentLocatorId());
        if (Objects.isNull(repairWareHouse)) {
            throw new MtException("HME_REPAIR_SN_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0006", "HME", repairLocatorCode));
        }

        //????????????
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        // ??????????????????????????????
        String internalOrderCompleteEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("INTERNAL_ORDER_COMPLETE");
        }});

        //V20210302 modify by penglin.sui for fang.pan ????????????????????????????????????????????????
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getMaterialLot().getAfFlag())) {
                HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryLastSplitRecordOfMaterialLot2(tenantId, hmeEoJobSnSingleBasic.getMaterialLot().getMaterialLotId());
                if (Objects.nonNull(hmeServiceSplitRecord)) {
                    //????????????
                    String currTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(CommonUtils.currentTimeGet());

                    HmeServiceSplitRecord hmeServiceSplitRecordPara = new HmeServiceSplitRecord();
                    hmeServiceSplitRecordPara.setSplitRecordId(hmeServiceSplitRecord.getSplitRecordId());
                    hmeServiceSplitRecordPara.setSplitStatus(HmeConstants.SplitStatus.REPAIR_COMPLETE);
                    hmeServiceSplitRecordPara.setAttribute2(currTime);
                    hmeServiceSplitRecordMapper.updateByPrimaryKeySelective(hmeServiceSplitRecordPara);
                    if (StringUtils.isNotBlank(hmeServiceSplitRecord.getServiceReceiveId()) && StringUtils.equals(hmeServiceSplitRecord.getTopSplitRecordId(), hmeServiceSplitRecord.getSplitRecordId())) {

                        HmeServiceReceive hmeServiceReceivePara = new HmeServiceReceive();
                        hmeServiceReceivePara.setServiceReceiveId(hmeServiceSplitRecord.getServiceReceiveId());
                        hmeServiceReceivePara.setReceiveStatus(HmeConstants.ReceiveStatus.REPAIR_COMPLETE);
                        hmeServiceReceivePara.setAttribute2(currTime);
                        hmeServiceReceiveMapper.updateByPrimaryKeySelective(hmeServiceReceivePara);

                        //????????????
                        HmeServiceReceive hmeServiceReceive = hmeServiceReceiveMapper.selectByPrimaryKey(hmeServiceSplitRecord.getServiceReceiveId());

                        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                        eventCreateVO.setEventTypeCode(HmeConstants.EventType.HME_AF_REPAIR_COMPLETE);
                        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                        HmeServiceReceiveHis hmeServiceReceiveHisPara = new HmeServiceReceiveHis();
                        BeanUtils.copyProperties(hmeServiceReceive, hmeServiceReceiveHisPara);
                        hmeServiceReceiveHisPara.setEventId(eventId);
                        hmeServiceReceiveHisRepository.insertSelective(hmeServiceReceiveHisPara);
                    }
                }
            }
        }
        //??????????????????????????????-EO_WKC_STEP_COMPLETE??????
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(internalOrderCompleteEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(repairLocator.getLocatorId());
        mtLotCompleteUpdate.setEnableFlag(HmeConstants.ConstantValue.NO);
        mtLotCompleteUpdate.setLot(StringUtils.trimToEmpty(hmeEoJobSnSingleBasic.getLotCode()));
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????NG
            if (CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //??????????????????????????????????????????
        List<MtExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
        MtExtendVO5 statusExtend = new MtExtendVO5();
        statusExtend.setAttrName("STATUS");
        statusExtend.setAttrValue("COMPLETED");
        mtLotExtendCompleteList.add(statusExtend);
        MtExtendVO5 soNumExtend = new MtExtendVO5();
        soNumExtend.setAttrName("SO_NUM");
        soNumExtend.setAttrValue(wo.getSoNum());
        mtLotExtendCompleteList.add(soNumExtend);
        MtExtendVO5 soLineNumExtend = new MtExtendVO5();
        soLineNumExtend.setAttrName("SO_LINE_NUM");
        soLineNumExtend.setAttrValue(wo.getSoLineNum());
        mtLotExtendCompleteList.add(soLineNumExtend);
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            MtExtendVO5 reworkFlag = new MtExtendVO5();
            reworkFlag.setAttrName("REWORK_FLAG");
            reworkFlag.setAttrValue("");
            mtLotExtendCompleteList.add(reworkFlag);
        }
        MtExtendVO5 mfFlagExtend = new MtExtendVO5();
        mfFlagExtend.setAttrName("MF_FLAG");
        mfFlagExtend.setAttrValue("");
        mtLotExtendCompleteList.add(mfFlagExtend);
        //V20210525 modify by penglin.sui for fang.pan ??????????????????????????????????????????????????????????????????????????????
        String completeProductionVersion = "";
        if(Objects.nonNull(hmeEoJobSnSingleBasic.getMaterialLot())){
            completeProductionVersion = hmeEoJobSnSingleBasic.getMaterialLot().getCompleteProductionVersion();
        }
        if(StringUtils.isBlank(completeProductionVersion)) {
            MtExtendVO5 materialVersionExtend = new MtExtendVO5();
            materialVersionExtend.setAttrName("MATERIAL_VERSION");
            materialVersionExtend.setAttrValue(wo.getProductionVersion());
            mtLotExtendCompleteList.add(materialVersionExtend);
        }
        MtExtendVO10 materialLotAttrCompleteUpdate = new MtExtendVO10();
        materialLotAttrCompleteUpdate.setEventId(completeEventId);
        materialLotAttrCompleteUpdate.setKeyId(dto.getMaterialLotId());
        materialLotAttrCompleteUpdate.setAttrs(mtLotExtendCompleteList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotAttrCompleteUpdate);
        return Collections.emptyList();
    }

    @Override
    public void workcellBindEquipmentValidate(Long tenantId, String operationId, String workcellId) {
        //????????????????????????
        if(StringUtils.isBlank(operationId)){
            return;
        }

        //????????????????????????-BIND_FLAG
        List<MtExtendAttrVO1> operationAttrList = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_operation_attr", Collections.singletonList(operationId), "BIND_FLAG"));
        if(CollectionUtils.isEmpty(operationAttrList)){
            return;
        }

        if(!HmeConstants.ConstantValue.YES.equals(operationAttrList.get(0).getAttrValue())){
            //??????Y???????????????
            return;
        }

        //????????????????????????
        List<String> wkcEquipmentList = hmeEquipmentMapper.queryEquipmentOfWkc(tenantId , workcellId);

        List<String> opOrWkcEquipmentList = new ArrayList<>();

        //????????????????????????-EQ_CATEGORY
        List<MtExtendAttrVO1> wkcAttrList = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_mod_workcell_attr", Collections.singletonList(workcellId), "EQ_CATEGORY"));
        if(CollectionUtils.isEmpty(wkcAttrList) ||
                (CollectionUtils.isNotEmpty(wkcAttrList) && StringUtils.isBlank(wkcAttrList.get(0).getAttrValue()))){
            // ??????????????????????????????????????????????????????????????????
            List<HmeOpEqRel> hmeOpEqRelList = hmeOpEqRelRepository.selectByCondition(Condition.builder(HmeOpEqRel.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeOpEqRel.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeOpEqRel.FIELD_OPERATION_ID, operationId))
                    .build());
            if(CollectionUtils.isNotEmpty(hmeOpEqRelList)){
                opOrWkcEquipmentList.addAll(hmeOpEqRelList.stream().map(HmeOpEqRel::getEquipmentCategory)
                        .filter(Objects::nonNull).distinct().collect(Collectors.toList()));
            }
        }else {
            opOrWkcEquipmentList.addAll(Arrays.asList(wkcAttrList.get(0).getAttrValue().split(",")).stream()
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        }

        List<String> errorEqCategoryList = new ArrayList<>();
        for (String opOrWkcEquipment : opOrWkcEquipmentList
             ) {

            if(!wkcEquipmentList.contains(opOrWkcEquipment)){
                errorEqCategoryList.add(opOrWkcEquipment);
            }
        }

        if(CollectionUtils.isNotEmpty(errorEqCategoryList)){
            List<LovValueDTO> eqCategoryLovs = lovAdapter.queryLovValue("HME.EQUIPMENT_CATEGORY", tenantId);
            Map<String,String> eqCategoryMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(eqCategoryLovs)){
                eqCategoryMap = eqCategoryLovs.stream().collect(Collectors.toMap(LovValueDTO::getValue, LovValueDTO::getMeaning));
            }
            StringBuilder equipments = new StringBuilder();
            for (String opOrWkcEquipment2 : errorEqCategoryList
            ) {
                if(equipments.length() == 0){
                    equipments.append(eqCategoryMap.getOrDefault(opOrWkcEquipment2 , opOrWkcEquipment2));
                }else{
                    equipments.append("|" + eqCategoryMap.getOrDefault(opOrWkcEquipment2 , opOrWkcEquipment2));
                }
            }

            //???${1}???????????????????????????,???????????????!
            throw new MtException("HME_EO_JOB_SN_199", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_199", "HME" , equipments.toString()));
        }
    }
}
