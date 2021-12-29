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
 * 作业平台-公共方法
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
     * @Description 根据EO批量获取最近一步工艺
     *
     * @author yuchao.wang
     * @date 2020/10/28 18:18
     * @param tenantId 租户ID
     * @param stepType 工艺步骤类型 最近一步工艺/最近正常加工一步工艺
     * @param eoIdList eoIdList
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeRouterStepVO>
     *
     */
    @Override
    public Map<String, HmeRouterStepVO> batchQueryRouterStepByEoIds(Long tenantId, String stepType, List<String> eoIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryRouterStepByEoIds tenantId={},stepType={},eoIdList={}", tenantId, stepType, eoIdList);
        Map<String, HmeRouterStepVO> routerStepMap = new HashMap<String, HmeRouterStepVO>();
        List<HmeRouterStepVO> routerStepVOList = new ArrayList<>();

        //获取EO对应的所有的步骤
        if (HmeConstants.StepType.NEAR_STEP.equals(stepType)) {
            //取最近一步工艺
            routerStepVOList = hmeEoJobSnMapper.batchSelectStepByEoIds(tenantId, eoIdList);
        } else if (HmeConstants.StepType.NORMAL_STEP.equals(stepType)) {
            //取最近正常加工一步工艺
            routerStepVOList = hmeEoJobSnMapper.batchSelectNormalStepByEoIds(tenantId, eoIdList);
        }

        if (CollectionUtils.isEmpty(routerStepVOList)) {
            // 无法获取到最近加工步骤
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }

        Map<String, List<HmeRouterStepVO>> eoRouterStepMap = routerStepVOList
                .stream().collect(Collectors.groupingBy(HmeRouterStepVO::getEoId));

        //校验分组后的大小与eoId大小一致
        if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != eoIdList.size()) {
            // 无法获取到最近加工步骤
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }

        //取最近的步骤
        for (Map.Entry<String, List<HmeRouterStepVO>> entry : eoRouterStepMap.entrySet()) {
            if (CollectionUtils.isEmpty(entry.getValue())) {
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_045", "HME"));
            }

            //按照顺序倒序排序
            List<HmeRouterStepVO> routerSteps = entry.getValue().stream().sorted(Comparator
                    .comparing(HmeRouterStepVO::getSequence, Comparator.reverseOrder())).collect(Collectors.toList());

            //取最近更新时间最近的工艺步骤实绩ID
            routerStepMap.put(entry.getKey(), routerSteps.get(0));
        }

        return routerStepMap;
    }

    /**
     *
     * @Description 批量查询条码信息并校验EO状态
     *
     * @author yuchao.wang
     * @date 2020/11/3 11:36
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    @Override
    public HmeEoJobSnVO16 batchQueryAndCheckMaterialLotByIdsForTime(Long tenantId, List<String> materialLotIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryAndCheckMaterialLotByIdsForTime tenantId={},materialLotIdList={}", tenantId, materialLotIdList);
        List<HmeEoJobSnVO5> hmeEoJobSnVO5List = hmeEoJobSnMapper.batchQueryMaterialLotByIdsForTime(tenantId, materialLotIdList);
        if (CollectionUtils.isEmpty(hmeEoJobSnVO5List)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
        }

        Map<String, HmeEoJobSnVO5> eoJobSnMap = new HashMap<>();
        Map<String, HmeRouterStepVO2> nearStepMap = new HashMap<String, HmeRouterStepVO2>();
        Map<String, HmeRouterStepVO2> normalStepMap = new HashMap<String, HmeRouterStepVO2>();
        Map<String, List<HmeRouterStepVO2>> allNormalStepMap = new HashMap<String, List<HmeRouterStepVO2>>();

        for (HmeEoJobSnVO5 snVO5 : hmeEoJobSnVO5List) {
            //校验条码状态
            if (!HmeConstants.ConstantValue.YES.equals(snVO5.getMaterialLotEnableFlag())) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", snVO5.getMaterialLotCode()));
            }

            //V20210703 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可进站
            if(StringUtils.isNotBlank(snVO5.getStocktakeFlag()) &&
                    HmeConstants.ConstantValue.YES.equals(snVO5.getStocktakeFlag())){
                //此SN【${1}】正在盘点,不可进站
                throw new MtException("HME_EO_JOB_SN_205", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_205", "HME", snVO5.getMaterialLotCode()));
            }

            //时效增加返修逻辑 modify by yuchao.wang for tianyang.xie at 2020.12.21
            //if (HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag())) {
            //    //返修SN不允许入炉
            //    throw new MtException("HME_EO_JOB_SN_120", mtErrorMessageRepository
            //            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_120", "HME"));
            //}
            if (!HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag())
                    && !HmeConstants.ConstantValue.OK.equals(snVO5.getQualityStatus())) {
                // 质量状态不为OK, 报错:SN已判定不良，请判定处置方法后执行工序作业
                throw new MtException("HME_EO_JOB_SN_029", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_029", "HME"));
            }
            //返修标识限制为Y或N
            snVO5.setReworkFlag(HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag()) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);
            //指定工艺路线返修标识限制为Y或N
            snVO5.setDesignedReworkFlag((HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag()) && HmeConstants.ConstantValue.YES
                    .equals(snVO5.getDesignedReworkFlag())) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);

            //验证EO是否存在
            if (StringUtils.isBlank(snVO5.getEoId())) {
                throw new MtException("ITF_DATA_COLLECT_0013", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "ITF_DATA_COLLECT_0013", "HME", snVO5.getMaterialLotCode()));
            }

            //验证EO状态
            if (!HmeConstants.EoStatus.WORKING.equals(snVO5.getStatus())
                    || (!HmeConstants.EoStatus.RELEASED.equals(snVO5.getLastEoStatus())
                    && !HmeConstants.EoStatus.HOLD.equals(snVO5.getLastEoStatus()))) {
                throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
            }
            eoJobSnMap.put(snVO5.getMaterialLotId(), snVO5);

            //校验工艺步骤
            if (CollectionUtils.isEmpty(snVO5.getRouterStepList())) {
                // 无法获取到最近加工步骤
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_045", "HME"));
            }

            //筛选nearStep,按照顺序倒序排序
            List<HmeRouterStepVO2> nearSteps = snVO5.getRouterStepList().stream()
                    .filter(step -> StringUtils.isNotBlank(step.getEoStepWipId()))
                    .sorted(Comparator.comparing(HmeRouterStepVO2::getSequence, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(nearSteps)) {
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_045", "HME"));
            }
            nearStepMap.put(snVO5.getEoId(), nearSteps.get(0));

            //筛选normalStep,按照顺序倒序排序
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

        //校验查出的大小与materialLotIdList大小一致
        if (eoJobSnMap.size() != materialLotIdList.size()) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
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
     * @Description 查询条码及工序信息
     *
     * @author yuchao.wang
     * @date 2021/1/26 17:13
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    @Override
    public HmeEoJobSnVO16 batchQueryAndCheckMaterialLotByIdsForTimeRework(Long tenantId, String materialLotId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryAndCheckMaterialLotByIdsForTimeRework tenantId={},materialLotId={}", tenantId, materialLotId);
        Set<String> repairTypes = lovAdapter.queryLovValue("HME.REPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> unrepairedTypes = lovAdapter.queryLovValue("HME.UNREPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        // 分新条码和旧条码
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, mtMaterialLot.getMaterialLotCode());
        HmeEoJobSnVO5 snVO5 = null;
        if (newMaterialLotFlag) {
            snVO5 = hmeEoJobSnMapper.batchQueryMaterialLotByIdsForTimeRework2(tenantId, materialLotId);
            // 20210914 add by 增加校验 新条码的话 如果旧条码进行返修 则不允许新条码进站
            List<MtExtendAttrVO> attrVOList = hmeEoJobSnCommonService.queryOldCodeAttrList(tenantId, snVO5.getEoId());
            Optional<MtExtendAttrVO> oldBarcodeInflagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("OLD_BARCODE_IN_FLAG", attr.getAttrName())).findFirst();
            if (oldBarcodeInflagOpt.isPresent() && YES.equals(oldBarcodeInflagOpt.get().getAttrValue())) {
                // 已由旧条码进站,不允许用新条码进站
                throw new MtException("HME_EO_JOB_SN_241", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_241", "HME"));
            }
            // 20210914 modiy by sanfeng.zhang for wenxin.zhang 新条码 返修标识取旧条码上的
            Optional<MtExtendAttrVO> reworkFlagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("REWORK_FLAG", attr.getAttrName())).findFirst();
            String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getAttrValue() : "";
            snVO5.setReworkFlag(reworkFlag);
        } else {
            snVO5 = hmeEoJobSnMapper.batchQueryMaterialLotByIdsForTimeRework(tenantId, materialLotId);
        }
        if (Objects.isNull(snVO5) || StringUtils.isBlank(snVO5.getMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
        }
        if (!HmeConstants.ConstantValue.YES.equals(snVO5.getMaterialLotEnableFlag())) {
            //当前条码无效, 请确认
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        //V20210705 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可进站
        if(StringUtils.isNotBlank(snVO5.getStocktakeFlag()) &&
                HmeConstants.ConstantValue.YES.equals(snVO5.getStocktakeFlag())){
            //此SN【${1}】正在盘点,不可进站
            throw new MtException("HME_EO_JOB_SN_205", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_205", "HME", snVO5.getMaterialLotCode()));
        }

        //校验工单类型
        if (StringUtils.isBlank(snVO5.getEoId()) || !repairTypes.contains(snVO5.getWorkOrderType())) {
            //当前条码未生成返修工单，无法进行返修
            throw new MtException("HME_EO_JOB_SN_148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_148", "HME"));
        }
        //验证WO状态
        if (!HmeConstants.WorkOrderStatus.RELEASED.equals(snVO5.getWorkOrderStatus())
                && !HmeConstants.WorkOrderStatus.EORELEASED.equals(snVO5.getWorkOrderStatus())) {
            //当前条码的返修工单不为EO下达状态
            throw new MtException("HME_EO_JOB_SN_175", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_175", "HME"));
        }
        //验证EO状态
        if (!HmeConstants.EoStatus.WORKING.equals(snVO5.getStatus())
                || (!HmeConstants.EoStatus.RELEASED.equals(snVO5.getLastEoStatus())
                && !HmeConstants.EoStatus.HOLD.equals(snVO5.getLastEoStatus()))) {
            //EO状态必须为运行状态
            throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
        }

        //是否返修
        if (!unrepairedTypes.contains(snVO5.getWorkOrderType()) && !HmeConstants.ConstantValue.YES.equals(snVO5.getReworkFlag())) {
            //非返修状态条码不允许进行进站操作
            throw new MtException("HME_EO_JOB_SN_161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_161", "HME"));
        }

        Map<String, HmeEoJobSnVO5> eoJobSnMap = new HashMap<>();
        Map<String, HmeRouterStepVO2> nearStepMap = new HashMap<String, HmeRouterStepVO2>();
        Map<String, HmeRouterStepVO2> normalStepMap = new HashMap<String, HmeRouterStepVO2>();

        //返修标识限制为Y
        snVO5.setReworkFlag(HmeConstants.ConstantValue.YES);
        eoJobSnMap.put(snVO5.getMaterialLotId(), snVO5);

        //校验工艺步骤
        if (CollectionUtils.isEmpty(snVO5.getRouterStepList())) {
            // 无法获取到最近加工步骤
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }

        //筛选nearStep,按照最近更新时间倒序排序
        List<HmeRouterStepVO2> nearSteps = snVO5.getRouterStepList().stream()
                .filter(step -> StringUtils.isNotBlank(step.getEoStepWipId()))
                .sorted(Comparator.comparing(HmeRouterStepVO2::getLastUpdateDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(nearSteps)) {
            throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_045", "HME"));
        }
        nearStepMap.put(snVO5.getEoId(), nearSteps.get(0));

        //筛选normalStep,按照最近更新时间倒序排序
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
     * @Description 批量查询eo下当前步骤
     *
     * @author yuchao.wang
     * @date 2020/11/3 15:52
     * @param tenantId 租户ID
     * @param eoIdList eoId
     * @param operationIdList 工艺ID
     * @return java.util.Map<java.lang.String,java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO3>>
     *
     */
    @Override
    public Map<String, List<HmeRouterStepVO3>> batchQueryCurrentRouterStep(Long tenantId, List<String> eoIdList, List<String> operationIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryCurrentRouterStep tenantId={},eoIdList={},operationIdList={}", tenantId, eoIdList, operationIdList);
        List<HmeRouterStepVO3> hmeRouterStepVO3s = hmeEoJobSnMapper.batchQueryCurrentRouterStep(tenantId, eoIdList, operationIdList);
        if (CollectionUtils.isEmpty(hmeRouterStepVO3s)){
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        Map<String, List<HmeRouterStepVO3>> eoRouterStepMap = hmeRouterStepVO3s
                .stream().collect(Collectors.groupingBy(HmeRouterStepVO3::getEoId));

        //校验分组后的大小与eoId大小一致
        if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != eoIdList.size()) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }
        return eoRouterStepMap;
    }

    /**
     *
     * @Description 时效作业-批量查询eo下当前步骤
     *
     * @author yuchao.wang
     * @date 2020/11/3 20:25
     * @param tenantId 租户ID
     * @param eoIdList eoId
     * @param operationId 工艺ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeRouterStepVO3>
     *
     */
    @Override
    public Map<String, HmeRouterStepVO3> batchQueryCurrentRouterStepForTime(Long tenantId, List<String> eoIdList, String operationId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryCurrentRouterStepForTime tenantId={},eoIdList={},operationId={}", tenantId, eoIdList, operationId);
        List<HmeRouterStepVO3> hmeRouterStepVO3s = hmeEoJobSnMapper.batchQueryCurrentRouterStepForTime(tenantId, eoIdList, operationId);
        if (CollectionUtils.isEmpty(hmeRouterStepVO3s)) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        Map<String, List<HmeRouterStepVO3>> eoRouterStepMap = hmeRouterStepVO3s
                .stream().collect(Collectors.groupingBy(HmeRouterStepVO3::getEoId));

        //校验分组后的大小与eoId大小一致
        if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != eoIdList.size()) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        //判断是否有多个步骤
        Map<String, HmeRouterStepVO3> currentStepMap = new HashMap<String, HmeRouterStepVO3>();
        for (Map.Entry<String, List<HmeRouterStepVO3>> entry : eoRouterStepMap.entrySet()) {
            if (CollectionUtils.isEmpty(entry.getValue()) || Objects.isNull(entry.getValue().get(0))) {
                // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
            }
            if (entry.getValue().size() > 1) {
                //当前工艺下存在多步骤,请检查工艺步骤
                throw new MtException("HME_EO_JOB_SN_119", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_119", "HME"));
            }

            currentStepMap.put(entry.getKey(), entry.getValue().get(0));
        }
        return currentStepMap;
    }

    /**
     * @param tenantId 租户ID
     * @param eoId     eoId
     * @return boolean
     * @Description 判断当前EO关联的工单类型 是否为售后
     * @author yuchao.wang
     * @date 2020/9/28 14:47
     */
    @Override
    public boolean isAfterSalesWorkOrder(Long tenantId, String eoId) {
        String workOrderType = hmeEoJobSnMapper.queryWorkOrderTypeByEoId(tenantId, eoId);

        if (StringUtils.isNotBlank(workOrderType)) {
            //查询售后工单类型LOV
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
     * @Description 批量查询下一步骤
     *
     * @author yuchao.wang
     * @date 2020/11/3 20:16
     * @param tenantId 租户ID
     * @param routerStepIdList 步骤ID
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

        //批量查询当前步骤的下一步骤
        List<MtRouterNextStep> nextSteps = mtRouterNextStepRepository.selectByCondition(Condition.builder(MtRouterNextStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterNextStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterNextStep.FIELD_ROUTER_STEP_ID, routerStepIdList))
                .build());

        if (CollectionUtils.isNotEmpty(nextSteps)) {
            //可能有多个下一步骤，先对当前步骤分组
            Map<String, List<MtRouterNextStep>> groupMap = nextSteps.stream()
                    .collect(Collectors.groupingBy(MtRouterNextStep::getRouterStepId));
            for (Map.Entry<String, List<MtRouterNextStep>> entry : groupMap.entrySet()) {
                if (CollectionUtils.isNotEmpty(entry.getValue())) {
                    //按照sequence从小到大排序取最小的
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
     * @Description 查询下一步骤
     *
     * @author yuchao.wang
     * @date 2020/12/14 15:05
     * @param tenantId 租户ID
     * @param routerStepId 步骤ID
     * @return java.lang.String
     *
     */
    @Override
    public String queryNextStep(Long tenantId, String routerStepId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryNextStep tenantId={},routerStepId={}", tenantId, routerStepId);
        if (StringUtils.isBlank(routerStepId)) {
            return StringUtils.EMPTY;
        }

        //查询当前步骤的下一步骤
        SecurityTokenHelper.close();
        List<MtRouterNextStep> nextSteps = mtRouterNextStepRepository.selectByCondition(Condition.builder(MtRouterNextStep.class)
                .select(MtRouterNextStep.FIELD_ROUTER_STEP_ID, MtRouterNextStep.FIELD_NEXT_STEP_ID, MtRouterNextStep.FIELD_SEQUENCE)
                .andWhere(Sqls.custom().andEqualTo(MtRouterNextStep.FIELD_ROUTER_STEP_ID, routerStepId)
                        .andEqualTo(MtRouterNextStep.FIELD_TENANT_ID, tenantId))
                .build());
        if (CollectionUtils.isEmpty(nextSteps)) {
            return StringUtils.EMPTY;
        }

        //可能有多个下一步骤，按照sequence从小到大排序取最小的
        List<MtRouterNextStep> steps = nextSteps.stream().sorted(Comparator
                .comparing(MtRouterNextStep::getSequence)).collect(Collectors.toList());
        return steps.get(0).getNextStepId();
    }

    /**
     *
     * @Description 根据步骤ID查询当前及下一步骤信息
     *
     * @author yuchao.wang
     * @date 2020/12/17 16:42
     * @param tenantId 租户ID
     * @param routerStepId 步骤ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryCurrentAndNextStepByCurrentId(Long tenantId, String routerStepId) {
        return hmeEoJobSnMapper.queryCurrentAndNextStepByCurrentId(tenantId, routerStepId);
    }

    /**
     *
     * @Description 根据eoId和工艺ID查询当前及下一步骤信息
     *
     * @author yuchao.wang
     * @date 2020/12/17 17:03
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param operationId 工艺ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryCurrentAndNextStepByEoAndOperation(Long tenantId, String eoId, String operationId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryCurrentAndNextStepByEoAndOperation tenantId={},eoId={},operationId={}", tenantId, eoId, operationId);
        HmeRouterStepVO5 currentStep = hmeEoJobSnMapper.queryCurrentAndNextStepByEoAndOperation(tenantId, eoId, operationId);
        if (Objects.isNull(currentStep) || StringUtils.isBlank(currentStep.getRouterStepId())) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }
        return currentStep;
    }

    /**
     *
     * @Description 根据eoId和工艺ID查询当前步骤信息
     *
     * @author yuchao.wang
     * @date 2021/1/27 15:35
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param operationId 工艺ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryCurrentStepByEoAndOperation(Long tenantId, String eoId, String operationId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryCurrentStepByEoAndOperation tenantId={},eoId={},operationId={}", tenantId, eoId, operationId);
        HmeRouterStepVO5 currentStep = hmeEoJobSnMapper.queryCurrentStepByEoAndOperation(tenantId, eoId, operationId);
        if (Objects.isNull(currentStep) || StringUtils.isBlank(currentStep.getRouterStepId())) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }
        return currentStep;
    }

    /**
     *
     * @Description 根据eoId和工艺ID查询当前及下一步骤信息
     *
     * @author yuchao.wang
     * @date 2020/12/31 10:03
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param routerId 工艺路线ID
     * @param eoId 执行作业ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    @Override
    public HmeRouterStepVO5 queryCurrentAndNextStepForDesignedRework(Long tenantId, String operationId, String routerId, String eoId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryCurrentAndNextStepForDesignedRework tenantId={},operationId={},routerId={}", tenantId, operationId, routerId);
        List<HmeRouterStepVO5> currentStepList = hmeEoJobSnMapper.queryCurrentAndNextStepForDesignedRework(tenantId, operationId, routerId);
        if (CollectionUtils.isEmpty(currentStepList)) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        List<HmeRouterStepVO5> subCurrentStepList = currentStepList.stream().filter(item -> StringUtils.isNotBlank(item.getRouterStepId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(subCurrentStepList)) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
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
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }else{
            return subCurrentStepList.get(0);
        }
    }

    /**
     *
     * @Description 根据eoId和工艺路线ID查询上一道步骤
     *
     * @author yuchao.wang
     * @date 2021/2/2 10:03
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param routerId 工艺路线ID
     * @param startTime 发起时间
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
     * @Description 根据ID批量查询EO信息 以及EO对应的WO信息
     *
     * @author yuchao.wang
     * @date 2020/11/5 16:53
     * @param tenantId 租户ID
     * @param eoIdList eoIdList
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    @Override
    public HmeEoJobSnVO16 batchQueryEoAndWoInfoById(Long tenantId, List<String> eoIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryEoAndWoInfoById tenantId={},eoIdList={}", tenantId, eoIdList);
        //批量查询EO信息
        Map<String, HmeEoVO4> eoVO4Map = new HashMap<>();
        List<HmeEoVO4> eoList = hmeEoJobSnMapper.batchQueryEoInfoById(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(eoList)) {
            eoList.forEach(item -> eoVO4Map.put(item.getEoId(), item));
        }

        if (eoVO4Map.size() != eoIdList.size() || !eoVO4Map.keySet().containsAll(eoIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO信息"));
        }

        //批量查询WO信息
        Map<String, HmeWorkOrderVO2> workOrderVO2Map = new HashMap<>();
        List<String> woIdList = eoList.stream().map(HmeEoVO4::getWorkOrderId).distinct().collect(Collectors.toList());
        List<HmeWorkOrderVO2> woList = hmeEoJobSnMapper.batchQueryWoInfoById(tenantId, woIdList);
        if (CollectionUtils.isNotEmpty(woList)) {
            woList.forEach(item -> workOrderVO2Map.put(item.getWorkOrderId(), item));
        }

        if (workOrderVO2Map.size() != woIdList.size() || !workOrderVO2Map.keySet().containsAll(woIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "工单信息"));
        }

        HmeEoJobSnVO16 hmeEoJobSnVO16 = new HmeEoJobSnVO16();
        hmeEoJobSnVO16.setEoMap(eoVO4Map);
        hmeEoJobSnVO16.setWoMap(workOrderVO2Map);
        return hmeEoJobSnVO16;
    }

    /**
     *
     * @Description 根据eoId查询EO/WO信息
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:37
     * @param tenantId 租户ID
     * @param eoStepId 步骤ID
     * @param eoIdList eoId
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    @Override
    public HmeEoJobSnVO16 batchQueryEoAndWoInfoWithComponentById(Long tenantId, String eoStepId, List<String> eoIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryEoAndWoInfoWithComponentById tenantId={},eoStepId={},eoIdList={}", tenantId, eoStepId, eoIdList);
        //批量查询EO信息
        Map<String, HmeEoVO4> eoVO4Map = new HashMap<>();
        List<HmeEoVO4> eoList = hmeEoJobSnMapper.batchQueryEoInfoWithComponentById(tenantId, eoStepId, eoIdList);
        if (CollectionUtils.isNotEmpty(eoList)) {
            eoList.forEach(item -> eoVO4Map.put(item.getEoId(), item));
            /*eoList.forEach(item -> {
                //过滤组件ID为空的数据
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
                    "HME_CHIP_TRANSFER_013", "HME", "EO信息"));
        }

        //批量查询WO信息
        Map<String, HmeWorkOrderVO2> workOrderVO2Map = new HashMap<>();
        List<String> woIdList = eoList.stream().map(HmeEoVO4::getWorkOrderId).distinct().collect(Collectors.toList());
        List<HmeWorkOrderVO2> woList = hmeEoJobSnMapper.batchQueryWoInfoWithComponentById(tenantId, woIdList);
        if (CollectionUtils.isNotEmpty(woList)) {
            woList.forEach(item -> workOrderVO2Map.put(item.getWorkOrderId(), item));
        }

        if (workOrderVO2Map.size() != woIdList.size() || !workOrderVO2Map.keySet().containsAll(woIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "工单信息"));
        }

        //校验工单对应的产线有效性
        long count = woList.stream().filter(item -> StringUtils.isBlank(item.getProdLineCode())).count();
        if (count > 0) {
            //SN对应工单未分配产线或分配产线已失效,请检查!
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
     * @Description 批量查询条码返修标识
     *
     * @author yuchao.wang
     * @date 2020/11/6 15:52
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
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
     * @Description 时效作业平台批量查询条码返修标识
     *
     * @author yuchao.wang
     * @date 2020/12/21 15:52
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO3>
     *
     */
    @Override
    public List<HmeMaterialLotVO3> batchQueryMaterialLotReworkFlagForTime(Long tenantId, List<String> materialLotIdList) {
//        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryMaterialLotReworkFlagForTime tenantId={},materialLotIdList={}", tenantId, materialLotIdList);
        List<List<String>> splitMaterialLotIdList = InterfaceUtils.splitSqlList(materialLotIdList, 2000);
        //批量查询条码信息
//        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoForTime(tenantId, materialLotIdList);
        List<HmeMaterialLotVO3> materialLotList = new ArrayList<>(materialLotIdList.size());
        for (List<String> subSplitMaterialLotIdList : splitMaterialLotIdList
             ) {
            List<HmeMaterialLotVO3> subMaterialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoForTime(tenantId, subSplitMaterialLotIdList);
            if(CollectionUtils.isNotEmpty(subMaterialLotList)){
                materialLotList.addAll(subMaterialLotList);
            }
        }

        //校验是否有没查到的
        List<String> exitMaterialLotIdList = materialLotList.stream().map(HmeMaterialLotVO3::getMaterialLotId).distinct().collect(Collectors.toList());
        if (exitMaterialLotIdList.size() != materialLotList.size() || !exitMaterialLotIdList.containsAll(materialLotIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
        }

        return materialLotList;
    }

    /**
     *
     * @Description 批量执行订单完成
     *
     * @author yuchao.wang
     * @date 2020/11/5 19:09
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param eoIdList eoIdList
     * @param eoMap eo信息
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void siteOutBatchComplete(Long tenantId, String workcellId, List<String> eoIdList, Map<String, HmeEoVO4> eoMap) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.siteOutBatchComplete tenantId={},workcellId={},eoIdList={},eoMap={}", tenantId, workcellId, eoIdList, eoMap);
        //批量查询最近一步工艺步骤
        Map<String, HmeRouterStepVO> nearStepMap = batchQueryRouterStepByEoIds(tenantId, HmeConstants.StepType.NEAR_STEP, eoIdList);

        // 工艺完成移出时间请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //订单批量完成
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
     * @Description 执行订单完成
     *
     * @author yuchao.wang
     * @date 2021/1/27 14:52
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param eo eo信息
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void siteOutComplete(Long tenantId, String workcellId, HmeEoVO4 eo) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.siteOutComplete tenantId={},workcellId={},eo={}", tenantId, workcellId, eo);
        //查询最近一步工艺步骤
        HmeRouterStepVO nearStep = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, eo.getEoId());
        if (Objects.isNull(nearStep)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "最近加工步骤"));
        }

        // 创建事件请求
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
     * @Description 批量出站主程序
     *
     * @author yuchao.wang
     * @date 2020/11/5 20:36
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WmsObjectTransactionResponseVO> batchMainOutSite(Long tenantId,
                                                                 HmeEoJobSnVO3 dto,
                                                                 HmeEoJobSnVO16 hmeEoJobSnVO16) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSite tenantId={},dto={},hmeEoJobSnVO16={}", tenantId, dto, hmeEoJobSnVO16);

        //批量查询是否为完成步骤
        List<String> eoStepIdList = hmeEoJobSnVO16.getHmeEoJobSnEntityList().stream().map(HmeEoJobSn::getEoStepId).distinct().collect(Collectors.toList());
        List<MtRouterDoneStep> mtRouterDoneStepList = mtRouterDoneStepRepository.selectByCondition(Condition.builder(MtRouterDoneStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterDoneStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterDoneStep.FIELD_ROUTER_STEP_ID, eoStepIdList)).build());
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSite mtRouterDoneStepList={}", mtRouterDoneStepList);

        //按照JobId挑拣出要出站数据的信息
        Map<String, HmeEoJobSn> eoJobSnEntityMap = new HashMap<String, HmeEoJobSn>();
        hmeEoJobSnVO16.getHmeEoJobSnEntityList().forEach(item -> eoJobSnEntityMap.put(item.getJobId(), item));

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        Long startDate = System.currentTimeMillis();
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
        log.info("=================================>批量工序作业平台-出站SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //创建事件
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

        //根据是否完成步骤分两组分别执行出站操作
        if (CollectionUtils.isNotEmpty(mtRouterDoneStepList)) {
            //筛选出所有完成步骤
            List<String> doneStepIdList = mtRouterDoneStepList.stream().map(MtRouterDoneStep::getRouterStepId).distinct().collect(Collectors.toList());

            //筛选完成步骤的作业
            doneSnLineList = dto.getSnLineList().stream()
                    .filter(item -> doneStepIdList.contains(eoJobSnEntityMap.get(item.getJobId()).getEoStepId())).collect(Collectors.toList());
            //筛选正常步骤的作业
            normalSnLineList = dto.getSnLineList().stream()
                    .filter(item -> !doneStepIdList.contains(eoJobSnEntityMap.get(item.getJobId()).getEoStepId())).collect(Collectors.toList());

            if(CollectionUtils.isNotEmpty(doneSnLineList)){
                List<String> materialLotIdList = doneSnLineList.stream().map(HmeEoJobSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());
                //批量查询条码信息
                Map<String, HmeMaterialLotVO3> materialLotMap = hmeEoJobSnCommonService.batchQueryMaterialLotInfo(tenantId, materialLotIdList);
                hmeEoJobSnVO16.setMaterialLotMap(materialLotMap);
            }

            //执行完成步骤部分的完工出站
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
            log.info("=================================>批量工序作业平台-出站完工步骤出站总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            materialLotCompleteUpdateList.addAll(hmeEoJobSnVO17.getMaterialLotCompleteUpdateList());
            materialLotAttrCompleteUpdateList.addAll(hmeEoJobSnVO17.getMaterialLotAttrCompleteUpdateList());
        }

        //如果有正常步骤数据，执行正常步骤出站
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

        //V20210309 modify by penglin.sui for hui.ma 记录实验代码
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

            // 获取当前用户
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
            log.info("=================================>批量工序作业平台-出站记录实验代码总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        //如果有完成步骤，则所有数据一起更新；否则有正常作业数据才进行更新
        startDate = System.currentTimeMillis();
        if (CollectionUtils.isNotEmpty(mtRouterDoneStepList)) {
            //批量更新物料批
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, hmeEoJobSnVO17.getMaterialLotOnhandUpdateList(),
                    hmeEoJobSnVO17.getOnhandIncreaseEventId(), HmeConstants.ConstantValue.NO);
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotCompleteUpdateList,
                    completeEventId, HmeConstants.ConstantValue.NO);

            //批量更新物料批扩展属性
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr",
                    hmeEoJobSnVO17.getOnhandIncreaseEventId(), hmeEoJobSnVO17.getMaterialLotAttrOnhandUpdateList());
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr",
                    completeEventId, materialLotAttrCompleteUpdateList);

            //批量新增工单SN关系
            hmeWoSnRelRepository.myBatchInsert(hmeEoJobSnVO17.getHmeWoSnRelInsertList());

            //批量记录事务
            // 增加批次ID
            String batchId = Utils.getBatchId();
            hmeEoJobSnVO17.getObjectTransactionRequestList().forEach(obj ->{
                obj.setAttribute16(batchId);
            });
            transactionResponseList = wmsObjectTransactionRepository
                    .objectTransactionSync(tenantId, hmeEoJobSnVO17.getObjectTransactionRequestList());

            //发送降级品比例接口
            if (CollectionUtils.isNotEmpty(hmeEoJobSnVO17.getErpReducedSettleRadioUpdateDTOList())) {
                try {
                    itfWorkOrderIfaceService.erpReducedSettleRadioUpdateRestProxy(tenantId, hmeEoJobSnVO17.getErpReducedSettleRadioUpdateDTOList());
                } catch (Exception e) {
                    log.error("=================降级品比例接口调用失败================={}:" + e.getMessage(), hmeEoJobSnVO17.getErpReducedSettleRadioUpdateDTOList());
                }
            }
        } else if (CollectionUtils.isNotEmpty(normalSnLineList)) {
            //如果没有完成步骤，只需要单独更新EO_WKC_STEP_COMPLETE事件的条码信息及扩展属性信息
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotCompleteUpdateList,
                    completeEventId, HmeConstants.ConstantValue.NO);

            //批量更新EO_WKC_STEP_COMPLETE事件的物料批扩展属性
            if (CollectionUtils.isNotEmpty(materialLotAttrCompleteUpdateList)) {
                mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr",
                        completeEventId, materialLotAttrCompleteUpdateList);
            }
        }

        //不良记录数据项记录不良代码
        batchAutoJudgeNc(tenantId , dto , hmeEoJobSnVO16);

        log.info("=================================>批量工序作业平台-出站如果有完成步骤，则所有数据一起更新；否则有正常作业数据才进行更新总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return transactionResponseList;
    }

    /**
     *
     * @Description 时效返修平台入口步骤加工完成
     *
     * @author yuchao.wang
     * @date 2021/2/1 14:25
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @return void
     *
     */
    @Override
    public void batchMainOutSiteForTimeReworkEntryStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSiteForTimeReworkEntryStep tenantId={},dto={},hmeEoJobSnVO16={}", tenantId, dto, hmeEoJobSnVO16);
        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
        List<MtMaterialLotVO20> materialLotCompleteUpdateList = new ArrayList<>();
        for (HmeEoJobSnVO3 jobSn : dto.getSnLineList()) {
            MtMaterialLotVO20 mtLotCompleteUpdate = new MtMaterialLotVO20();
            mtLotCompleteUpdate.setMaterialLotId(jobSn.getMaterialLotId());
            mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            materialLotCompleteUpdateList.add(mtLotCompleteUpdate);
        }

        //如果没有完成步骤，只需要单独更新EO_WKC_STEP_COMPLETE事件的条码信息及扩展属性信息
        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotCompleteUpdateList,
                completeEventId, HmeConstants.ConstantValue.NO);
    }

    /**
     *
     * @Description 完工步骤出站
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:37
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO18 参数
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

        //查询货位对应的仓库
        MtModLocator warehouse = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtModLocator.getLocatorId());

        //创建事件
        String onhandIncreaseEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(hmeEoJobSnVO18.getInvUpdateEventRequestId());
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("INV_ONHAND_INCREASE");
            }
        });
        //完工事务回传
        String eoCompleteEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setWorkcellId(dto.getWorkcellId());
                setEventTypeCode("HME_EO_COMPLETE");
            }
        });

        //查询值集HME.NOT_CREATE_BATCH_NUM
        List<String> createBatchNumList = new ArrayList<>();
        List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.NOT_CREATE_BATCH_NUM", tenantId);
        if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
            createBatchNumList = lovValueDTOList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }

        //系统参数HME_MATERIAL_BATCH_NUM中的“系统”层的值
        String hmeMaterialBatchNum = profileClient.getProfileValueByOptions("HME_MATERIAL_BATCH_NUM");

        //批量查询所有条码容器信息
        Map<String, String> currentContainerMap = new HashMap<>();
        List<String> materialLotIdList = hmeEoJobSnVO18.getDoneSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId)
                .distinct().collect(Collectors.toList());
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
            mtMaterialLotList.forEach(item -> currentContainerMap.put(item.getMaterialLotId(), item.getCurrentContainerId()));
        }

        //批量查询工艺步骤名称
        Map<String, String> routerStepNameMap = new HashMap<String, String>();
        List<MtRouterStep> mtRouterStepList = mtRouterStepRepository.routerStepBatchGet(tenantId, eoStepIdList);
        if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
            mtRouterStepList.forEach(item -> routerStepNameMap.put(item.getRouterStepId(), item.getStepName()));
        }

        //批量查询移动类型
        Map<String, String> moveTypeMap = getMoveTypeForMainOutSite(tenantId);

        //获取MES_RK06对应的内部订单号或者SAP号
        Map<String, HmeServiceSplitRecordVO2> internalOrderNumMap = getRk06InternalOrderNum(tenantId, eoMap, woMap, hmeEoJobSnVO18.getDoneSnLineList());

        //获取联产品行号
        Map<String, String> relatedLineNumMap = getRelatedLineNum(tenantId, eoMap, woMap);

        //批量获取woSnRel Id
        List<String> woSnRelIdList = customSequence.getNextKeys("hme_wo_sn_rel_s", hmeEoJobSnVO18.getDoneSnLineList().size());

        //是否需要校验系统参数
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
                    //${1}不存在 请确认${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "routerStep", jobSn.getEoStepId()));
                }
            }

            HmeEoVO4 hmeEo = eoMap.get(jobSn.getEoId());
            HmeWorkOrderVO2 hmeWo = woMap.get(hmeEo.getWorkOrderId());

            //获取批次编码
            if (StringUtils.isBlank(hmeEo.getItemType()) || !createBatchNumList.contains(hmeEo.getItemType())) {
                lotCode = hmeMaterialBatchNum;
                //如果是第一次进入这段逻辑，先校验系统参数
                if (sysParameterVerificationFlag) {
                    if (StringUtils.isBlank(hmeMaterialBatchNum)) {
                        //默认批次获取失败,请联系管理员通过【配置维护】功能维护【HME_MATERIAL_BATCH_NUM】
                        throw new MtException("HME_EO_JOB_SN_113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_113", "HME"));
                    }
                    if (hmeMaterialBatchNum.length() != 10) {
                        //默认批次必须为10位,请联系管理员通过【配置维护】功能维护【HME_MATERIAL_BATCH_NUM】
                        throw new MtException("HME_EO_JOB_SN_114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_114", "HME"));
                    }
                    sysParameterVerificationFlag = false;
                }
            }
            if (hmeEoJobSnVO18.isTimeRework() && HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                // 20210812 add by sanfeng.zhang for tianyang.xie 返修作业平台 旧条码最后一道序出站时将新条码失效
                // 判断当前EO是否为旧条码 为空 则为新条码 否则旧条码
                Boolean newMaterialLotFlag = hmeEoJobSnVO18.isNewMaterialLotFlag();
                if (!newMaterialLotFlag) {
                    List<String> newMaterialLotIdList = hmeEoJobSnReWorkMapper.queryNewMaterialLotByOldMaterialLot(tenantId, jobSn.getMaterialLotId());
                    // 找到对应的新条码的ID
                    if (CollectionUtils.isEmpty(newMaterialLotIdList)) {
                        throw new MtException("HME_REPAIR_SN_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_REPAIR_SN_0008", "HME"));
                    }
                    // 失效新条码
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
            //构造条码批量更新参数-INV_ONHAND_INCREASE事件
            MtMaterialLotVO20 mtLotOnhandUpdate = new MtMaterialLotVO20();
            mtLotOnhandUpdate.setMaterialLotId(jobSn.getMaterialLotId());
            mtLotOnhandUpdate.setLot(lotCode);
            materialLotOnhandUpdateList.add(mtLotOnhandUpdate);

            //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
            MtMaterialLotVO20 mtLotCompleteUpdate = new MtMaterialLotVO20();
            mtLotCompleteUpdate.setMaterialLotId(jobSn.getMaterialLotId());
            mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie 存在数据项不良，置条码质量状态为NG
            if(CollectionUtils.isNotEmpty(hmeEoJobSnVO18.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnVO18.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(jobSn.getJobId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(ngDataRecordList)){
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
            materialLotCompleteUpdateList.add(mtLotCompleteUpdate);

            //构造条码扩展属性批量更新参数-INV_ONHAND_INCREASE事件
            List<MtCommonExtendVO5> mtLotExtendOnhandList = new ArrayList<>();
            MtCommonExtendVO5 mfFlagExtend = new MtCommonExtendVO5();
            mfFlagExtend.setAttrName("MF_FLAG");
            mfFlagExtend.setAttrValue("");
            mtLotExtendOnhandList.add(mfFlagExtend);

            //V20210525 modify by penglin.sui for fang.pan 末道序完工时，如果条码版本扩展字段有值，则不更新版本
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

            //构造条码扩展属性批量更新参数-EO_WKC_STEP_COMPLETE事件
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
                // 旧条码最后一道序返修出站 去掉旧条码进站标识
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

            //工单SN关系
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

            //构造报工/完工事务
            objectTransactionRequestList.addAll(getTransactionRequestVOList(tenantId, eoCompleteEventId, routerStepName, lotCode,
                    jobSn, hmeEo, hmeWo, mtModLocator, warehouse, moveTypeMap, currentContainerMap, internalOrderNumMap, relatedLineNumMap));
        }

        //汇总更新现有量
        // 20211130 add by sanfeng.zhang for wenxin.zhang 只有末道序才增加现有量
        List<HmeEoVO4> filterEoList = eoMap.values().stream().filter(vo -> snNumList.contains(vo.getIdentification())).collect(Collectors.toList());
        List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
        Map<String, List<HmeEoVO4>> onhandGroupMap = filterEoList.stream()
                .collect(Collectors.groupingBy(item -> item.getSiteId() + "-" + item.getMaterialId()));
        for (Map.Entry<String, List<HmeEoVO4>> onhandEntry : onhandGroupMap.entrySet()) {
            double sumQty = onhandEntry.getValue().stream().mapToDouble(MtEo::getQty).sum();

            // 更新现有量
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
            // 批量更新现有量
            MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
            mtInvOnhandQuantityVO16.setEventId(onhandIncreaseEventId);
            mtInvOnhandQuantityVO16.setOnhandList(onhandList);
            mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId,mtInvOnhandQuantityVO16);
        }

        //获取降级品接口请求报文
        List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOList = getErpReducedSettleRadioUpdateDTOList(tenantId, woMap);

        //构造返回数据
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
     * @Description 获取发送降级品比例接口的参数
     *
     * @author yuchao.wang
     * @date 2020/11/7 13:47
     * @param tenantId 租户ID
     * @param woMap wo信息
     * @return java.util.List<com.ruike.itf.api.dto.ErpReducedSettleRadioUpdateDTO>
     *
     */
    private List<ErpReducedSettleRadioUpdateDTO> getErpReducedSettleRadioUpdateDTOList(Long tenantId, Map<String, HmeWorkOrderVO2> woMap) {
        List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOList = new ArrayList<ErpReducedSettleRadioUpdateDTO>();

        //查询WO完工状态，看是否需要发送降级品接口
        List<MtWorkOrder> completedWoList = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class)
                .select(MtWorkOrder.FIELD_WORK_ORDER_ID)
                .andWhere(Sqls.custom().andEqualTo(MtWorkOrder.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtWorkOrder.FIELD_STATUS, HmeConstants.WorkOrderStatus.COMPLETED)
                        .andIn(MtWorkOrder.FIELD_WORK_ORDER_ID, woMap.keySet())).build());
        if (CollectionUtils.isEmpty(completedWoList)) {
            return erpReducedSettleRadioUpdateDTOList;
        }

        //查询出工单对应的完成状态的EO
        List<String> completeWoIdList = completedWoList.stream().map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
        List<HmeEoVO4> completeEoList = hmeEoJobSnMapper.batchQueryEoInfoByWoId(tenantId, HmeConstants.EoStatus.COMPLETED, completeWoIdList);
        if (CollectionUtils.isEmpty(completeEoList)) {
            return erpReducedSettleRadioUpdateDTOList;
        }

        //按照工单分组处理
        Map<String, List<HmeEoVO4>> eoGroupMap = completeEoList.stream().collect(Collectors.groupingBy(HmeEoVO4::getWorkOrderId));
        for (Map.Entry<String, List<HmeEoVO4>> woEntry : eoGroupMap.entrySet()) {
            //筛选物料不同的EO，看是否存在联产品
            List<HmeEoVO4> relatedEoList = woEntry.getValue().stream()
                    .filter(item -> !woMap.get(woEntry.getKey()).getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(relatedEoList)) {
                continue;
            }

            //存在联产品则构造降级品接口参数
            BigDecimal mainMaterialQty = new BigDecimal(100);

            //计算总数量
            BigDecimal sumQty = new BigDecimal(woEntry.getValue().stream().mapToDouble(MtEo::getQty).sum());

            //联产品按照物料分组，计算每个物料数量比例
            Map<String, List<HmeEoVO4>> materialGroup = relatedEoList.stream().collect(Collectors.groupingBy(HmeEoVO4::getMaterialCode));
            for (Map.Entry<String, List<HmeEoVO4>> materialEntry : materialGroup.entrySet()) {
                BigDecimal qty = new BigDecimal(materialEntry.getValue().stream().mapToDouble(MtEo::getQty).sum());
                BigDecimal radio = qty.multiply(BigDecimal.valueOf(100)).divide(sumQty, 0, BigDecimal.ROUND_HALF_UP);
                ErpReducedSettleRadioUpdateDTO erpReducedSettleRadioUpdateDTO = new ErpReducedSettleRadioUpdateDTO();
                erpReducedSettleRadioUpdateDTO.setAUFNR(woMap.get(woEntry.getKey()).getWorkOrderNum());
                erpReducedSettleRadioUpdateDTO.setMATNR(materialEntry.getKey());
                erpReducedSettleRadioUpdateDTO.setPROZS(String.valueOf(radio));
                erpReducedSettleRadioUpdateDTOList.add(erpReducedSettleRadioUpdateDTO);

                //计算主产品比例
                mainMaterialQty = mainMaterialQty.subtract(radio);
            }

            //最后添加主产品比例
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
     * @Description 非完工步骤出站
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:38
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO18 参数
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
            //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
            MtMaterialLotVO20 mtLotCompleteUpdate = new MtMaterialLotVO20();
            mtLotCompleteUpdate.setMaterialLotId(jobSn.getMaterialLotId());
            mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie 存在数据项不良，置条码质量状态为NG
            if(CollectionUtils.isNotEmpty(hmeEoJobSnVO18.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnVO18.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(jobSn.getJobId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(ngDataRecordList)){
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
            materialLotCompleteUpdateList.add(mtLotCompleteUpdate);

            //如果是返修出站 构造条码扩展属性批量更新参数-EO_WKC_STEP_COMPLETE事件
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
     * @Description 统一发送实时接口
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:43
     * @param tenantId 租户ID
     * @param transactionResponseList 事件
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

        //筛选事务ID，查询事务类型
        List<String> transactionIdList = transactionResponseList.stream().map(WmsObjectTransactionResponseVO::getTransactionId).collect(Collectors.toList());
        List<WmsObjectTransaction> transactionList = wmsObjectTransactionRepository.selectByCondition(Condition.builder(WmsObjectTransaction.class)
                .andWhere(Sqls.custom().andEqualTo(WmsObjectTransaction.FIELD_TENANT_ID, tenantId)
                        .andIn(WmsObjectTransaction.FIELD_TRANSACTION_ID, transactionIdList)).build());
        if (transactionIdList.size() != transactionList.size()) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "事件明细数据"));
        }

        //V20211009 modify by penglin.sui for hui.ma 工单所在事业部如在值集HME.REPORT_NON_REALTIME_BUSINESS_AREA中则非实时,否则实时
        List<String> nonRealTimeTransactionIdList = new ArrayList<>();
        //查询值集
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
        //筛选报工/完工事务
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

        //发送报工实时接口,发送完工实时接口
        log.info("<====== HmeEoJobSnCommonServiceImpl.sendSapProdMaterialMove 发送报工、完工实时接口 " +
                "woReportTransactionResponseVOList={},woCompleteTransactionResponseVOList={}",
                woReportTransactionResponseVOList, woCompleteTransactionResponseVOList);
        itfObjectTransactionIfaceService.sendSapProdMaterialMove(tenantId, woReportTransactionResponseVOList, woCompleteTransactionResponseVOList);
    }

    /**
     *
     * @Description 工段完工数据统计-工序作业平台/批量工序作业平台/时效工序作业平台/PDA工序作业平台
     *
     * @author yuchao.wang
     * @date 2020/11/7 14:02
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchWkcCompleteOutputRecord(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchWkcCompleteOutputRecord tenantId={},dto={},hmeEoJobSnVO16={}", tenantId, dto, hmeEoJobSnVO16);
        //筛选需要记录的数据
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

        //获取工段
        MtModOrganizationVO2 lineParam = new MtModOrganizationVO2();
        lineParam.setTopSiteId(dto.getSiteId());
        lineParam.setOrganizationId(dto.getWorkcellId());
        lineParam.setOrganizationType("WORKCELL");
        lineParam.setParentOrganizationType("WORKCELL");
        lineParam.setQueryType("TOP");
        List<MtModOrganizationItemVO> lineVOList = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, lineParam);
        if (CollectionUtils.isEmpty(lineVOList) || Objects.isNull(lineVOList.get(0))) {
            // 请先维护Wkc工序工段关系
            throw new MtException("HME_EO_JOB_SN_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_023", "HME"));
        }
        String workcellId = lineVOList.get(0).getOrganizationId();
        Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO16.getEoMap();
        Set<String> woIdSet = hmeEoJobSnVO16.getWoMap().keySet();
        List<String> eoIdList = new ArrayList<>(eoMap.keySet());

        //查询当前有记录的完工统计数据
        SecurityTokenHelper.close();
        List<HmeWkcCompleteOutputRecord> wkcCompleteOutputRecords = hmeWkcCompleteOutputRecordRepository.selectByCondition(Condition.builder(HmeWkcCompleteOutputRecord.class)
                .andWhere(Sqls.custom().andEqualTo(HmeWkcCompleteOutputRecord.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeWkcCompleteOutputRecord.FIELD_WORKCELL_ID, workcellId)
                        .andIn(HmeWkcCompleteOutputRecord.FIELD_WORK_ORDER_ID, woIdSet)).build());
        //按照workcellId+WorkOrderId+WkcShiftId+MaterialId分组，工段都是一致的，只按照后三个分组即可
        Map<String, HmeWkcCompleteOutputRecord> wkcCompleteOutputRecordMap = wkcCompleteOutputRecords.stream()
                .collect(Collectors.toMap(t -> t.getWorkOrderId() + "-" + t.getWkcShiftId() + "-" + t.getMaterialId(), t -> t));

        //批量查询未完工的其他工艺步骤对应的工段
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
            //查询EO
            HmeEoVO4 hmeEo = eoMap.get(eoJobSn.getEoId());
            if (Objects.isNull(hmeEo) || StringUtils.isBlank(hmeEo.getEoId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "EO信息"));
            }

            //获取未完工的其他工艺步骤对应的工段
            List<String> workcellIdList = new ArrayList<>();
            if (workcellIdMap.containsKey(hmeEo.getEoId())) {
                workcellIdList = workcellIdMap.get(hmeEo.getEoId()).stream()
                        .map(HmeEoJobSnDTO3::getWorkcellId).distinct().collect(Collectors.toList());
            }

            //如果有当前工段则不统计
            if (CollectionUtils.isNotEmpty(workcellIdList) && workcellIdList.contains(workcellId)) {
                continue;
            }

            //判断当前是否已存在统计数据
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
                //存在只累加当前数量,不存在添加一条update语句
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

        // 批量执行新增和更新
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(insertDataList)) {
            //将插入数据按唯一索引分组
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
        // 获取当前时间
        final Date currentDate = CommonUtils.currentTimeGet();

        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        List<HmeEoJobSn> snJobList = new ArrayList<>();

        //批量获取EoJobSn Id/Cid
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
            //V20201004 modify by penglin.sui for lu.bai 新增SN数量记录
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

        //批量新增
        if (CollectionUtils.isNotEmpty(snJobList)) {
            List<List<HmeEoJobSn>> splitSqlList = CommonUtils.splitSqlList(snJobList, 200);
            for (List<HmeEoJobSn> domains : splitSqlList) {
                hmeEoJobSnMapper.batchInsert(domains);
            }
        }
    }

    /**
     *
     * @Description 批量查询条码信息
     *
     * @author yuchao.wang
     * @date 2020/11/17 17:27
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeMaterialLotVO3>
     *
     */
    @Override
    public Map<String, HmeMaterialLotVO3> batchQueryMaterialLotInfo(Long tenantId, List<String> materialLotIdList) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchQueryMaterialLotInfo tenantId={},materialLotIdList={}", tenantId, materialLotIdList);
        //批量查询条码信息
        Map<String, HmeMaterialLotVO3> materialLotVO3Map = new HashMap<>();
        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoById(tenantId, materialLotIdList);
        if (CollectionUtils.isNotEmpty(materialLotList)) {
            materialLotList.forEach(item -> materialLotVO3Map.put(item.getMaterialLotId(), item));
        }

        if (materialLotVO3Map.size() != materialLotList.size() || !materialLotVO3Map.keySet().containsAll(materialLotIdList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
        }
        return materialLotVO3Map;
    }

    /**
     *
     * @Description 判断作业下是否有为空的数据采集项结果
     *
     * @author yuchao.wang
     * @date 2020/11/17 20:29
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobIdList 作业ID
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
     * @Description 查询作业下是否有为空的数据采集项结果
     *
     * @author yuchao.wang
     * @date 2020/11/17 20:29
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobIdList 作业ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     *
     */
    @Override
    public List<HmeEoJobDataRecordVO> hasMissingValueTag(Long tenantId, String workcellId, List<String> jobIdList) {
        return hmeEoJobSnMapper.queryValueMissingTag(tenantId, workcellId, jobIdList);
    }

    /**
     *
     * @Description 判断是否存在其他工艺下的作业
     *
     * @author yuchao.wang
     * @date 2020/11/17 21:07
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialLotIds 条码ID
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
     * @Description 是否容器出站
     *
     * @author yuchao.wang
     * @date 2020/11/17 21:41
     * @param tenantId 租户ID
     * @param workcellId 工位ID
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
     * @Description 容器出站
     *
     * @author yuchao.wang
     * @date 2020/11/17 22:24
     * @param tenantId 租户ID
     * @param hmeEoJobSnVO16 参数
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchContainerOutSite(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchContainerOutSite tenantId={},hmeEoJobSnVO16={},dto={}", tenantId, hmeEoJobSnVO16, dto);
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");
        Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO16.getEoMap();

        List<MtContainerVO31> containerLoadList = new ArrayList<>();
        //List<MtContainerVO9> containerLoadVerifyList = new ArrayList<>();
        for (HmeEoJobSnVO3 eoJobSnVO3 : dto.getSnLineList()) {
            /*// 构造{ containerLoadBatchVerify }参数
            MtContainerVO9 mtContainerVO9 = new MtContainerVO9();
            mtContainerVO9.setContainerId(hmeEoJobSnVO16.getEoJobContainer().getContainerId());
            mtContainerVO9.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO9.setLoadObjectId(eoJobSnVO3.getMaterialLotId());
            mtContainerVO9.setTrxLoadQty(eoMap.get(eoJobSnVO3.getEoId()).getQty());
            containerLoadVerifyList.add(mtContainerVO9);*/

            // 构造{ containerBatchLoad }参数
            MtContainerVO31 mtContainerVO31 = new MtContainerVO31();
            mtContainerVO31.setContainerId(hmeEoJobSnVO16.getEoJobContainer().getContainerId());
            mtContainerVO31.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO31.setLoadObjectId(eoJobSnVO3.getMaterialLotId());
            mtContainerVO31.setTrxLoadQty(eoMap.get(eoJobSnVO3.getEoId()).getQty());
            containerLoadList.add(mtContainerVO31);
        }

        // 调用{ containerLoadBatchVerify }进行容器装载验证
        //mtContainerRepository.containerLoadBatchVerify(tenantId, containerLoadVerifyList);

        // 调用{ containerBatchLoad }进行容器批量装载
        MtContainerVO30 mtContainerVO30 = new MtContainerVO30();
        mtContainerVO30.setEventRequestId(eventRequestId);
        mtContainerVO30.setContainerLoadList(containerLoadList);
        mtContainerRepository.containerBatchLoad(tenantId, mtContainerVO30);
    }

    /**
     *
     * @Description 批量查询当前步骤及下一步骤
     *
     * @author yuchao.wang
     * @date 2020/11/19 14:10
     * @param tenantId 租户ID
     * @param routerStepIdList 步骤ID
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

        //可能有多个下一步骤，先对当前步骤分组
        Map<String, List<HmeRouterStepVO4>> groupMap = routerStepList.stream()
                .collect(Collectors.groupingBy(HmeRouterStepVO4::getRouterStepId));
        for (Map.Entry<String, List<HmeRouterStepVO4>> entry : groupMap.entrySet()) {
            if (CollectionUtils.isNotEmpty(entry.getValue())) {
                //按照sequence从小到大排序取最小的
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
            // 未找到匹配的物料站点信息
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }
        MtExtendVO materialSiteExtend = new MtExtendVO();
        materialSiteExtend.setTableName("mt_material_site_attr");
        materialSiteExtend.setKeyId(materialSiteId);
        List<MtExtendAttrVO> materialSiteExtendAttr = mtExtendSettingsRepository.attrPropertyQuery(tenantId, materialSiteExtend);
        // 物料站点扩展字段：时效
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
        //查询条码扩展属性
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
        // 更新时效条码的启用日期
        MtExtendVO5 enableDateAttr = new MtExtendVO5();
        enableDateAttr.setAttrName("ENABLE_DATE");
        enableDateAttr.setAttrValue(currentTimeStr);
        mtExtendVO5List.add(enableDateAttr);
        // 更新时效条码的截止日期
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
        //查询工单组件清单
        HmeEoJobSnBatchDTO hmeEoJobSnBatchDTOPara = new HmeEoJobSnBatchDTO();
        hmeEoJobSnBatchDTOPara.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobSnBatchDTOPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSnBatchDTOPara.setSiteId(dto.getSiteId());
        SecurityTokenHelper.close();
        List<HmeEoJobSnBatchVO4> woComponentList = hmeEoJobSnBatchMapper.selectWoComponent(tenantId,hmeEoJobSnBatchDTOPara);
        //排除反冲料
        if(CollectionUtils.isNotEmpty(woComponentList)) {
            woComponentList = woComponentList.stream().filter(item -> !"2".equals(item.getBackflushFlag())).collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(woComponentList)){
            allComponentList.addAll(woComponentList);
            List<String> woBomComponentMaterialIdList = new ArrayList<>();
            List<String> bomSubstituteGroupIdList = new ArrayList<>();
            Map<String,String> componentSubstituteMap = new HashMap<>();
            Map<String,String> componentCodeSubstituteMap = new HashMap<>();
            //数量大于0的就是主料
            List<HmeEoJobSnBatchVO4> woComponentList2 = woComponentList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            woComponentList2.forEach(item -> {
                item.setComponentMaterialId(item.getMaterialId());
                item.setComponentMaterialCode(item.getComponentMaterialCode());
                //设置组件类型
                item.setComponentType(HmeConstants.ComponentType.A_MAIN);
                item.setIsSubstitute(HmeConstants.ConstantValue.NO);
                woBomComponentMaterialIdList.add(item.getMaterialId());
                if(StringUtils.isNotBlank(item.getBomSubstituteGroupId())) {
                    bomSubstituteGroupIdList.add(item.getBomSubstituteGroupId());
                    componentSubstituteMap.put(item.getBomSubstituteGroupId(),item.getComponentMaterialId());
                    componentCodeSubstituteMap.put(item.getBomSubstituteGroupId(),item.getComponentMaterialCode());
                }
            });
            //有主料，查询全局替代料
            if(CollectionUtils.isNotEmpty(woBomComponentMaterialIdList)) {
                SecurityTokenHelper.close();
                List<HmeEoJobSnBatchVO4> woSubstituteList = hmeEoJobSnBatchMapper.selectWoSubstitute(tenantId, dto.getSiteId(), woBomComponentMaterialIdList);
                if (CollectionUtils.isNotEmpty(woSubstituteList)) {
                    //排除反冲料
                    woSubstituteList = woSubstituteList.stream().filter(item -> !"2".equals(item.getBackflushFlag())).collect(Collectors.toList());
                    allComponentList.addAll(woSubstituteList);
                }
            }
            //组件清单中替代料
            List<HmeEoJobSnBatchVO4> woComponentList3 = woComponentList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) <= 0).collect(Collectors.toList());
            List<String> materialIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(woComponentList3)) {
                materialIdList = woComponentList3.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
            }
            //设置组件清单中替代料的替代组
            if(CollectionUtils.isNotEmpty(materialIdList) && CollectionUtils.isNotEmpty(bomSubstituteGroupIdList)) {
                List<HmeEoJobSnBatchVO5> componentSubstituteList = hmeEoJobSnBatchMapper.selectComponentSubstitute(tenantId, materialIdList, bomSubstituteGroupIdList);
                if(CollectionUtils.isNotEmpty(componentSubstituteList)){
                    woComponentList3.forEach(item->{
                        List<HmeEoJobSnBatchVO5> componentSubstituteList2 = componentSubstituteList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(componentSubstituteList2)) {
                            item.setBomSubstituteGroupId(componentSubstituteList2.get(0).getBomSubstituteGroupId());
                            item.setComponentMaterialId(componentSubstituteMap.getOrDefault(componentSubstituteList2.get(0).getBomSubstituteGroupId(),""));
                            item.setComponentMaterialCode(componentCodeSubstituteMap.getOrDefault(componentSubstituteList2.get(0).getBomSubstituteGroupId(),""));
                            //设置组件类型
                            item.setComponentType(HmeConstants.ComponentType.BOM_SUBSTITUTE);
                            item.setIsSubstitute(HmeConstants.ConstantValue.YES);
                        }
                    });
                }
            }
        }
        //排除没有主料的数据
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
        //查询EO组件清单
        HmeEoJobSnBatchDTO hmeEoJobSnBatchDTOPara = new HmeEoJobSnBatchDTO();
        hmeEoJobSnBatchDTOPara.setEoId(dto.getEoId());
        hmeEoJobSnBatchDTOPara.setSiteId(dto.getSiteId());
        hmeEoJobSnBatchDTOPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSnBatchDTOPara.setSelectedCount(1);
        hmeEoJobSnBatchDTOPara.setRouterId(dto.getRouterId());
        //查询组件清单投料信息
        SecurityTokenHelper.close();
        List<HmeEoJobSnBatchVO4> componentReleaseList = hmeEoJobSnBatchMapper.selectComponentRelease(tenantId,hmeEoJobSnBatchDTOPara);
        //过滤掉反冲料(虚拟件组件为反冲料时不过滤)
        componentReleaseList = componentReleaseList.stream().filter(item -> !"2".equals(item.getBackflushFlag())
                || ("2".equals(item.getBackflushFlag()) && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())))
                .collect(Collectors.toList());
        //全局替代料要投料信息
        List<HmeEoJobSnBatchVO4> substituteReleaseList = new ArrayList<>();
        //所有物料ID
        List<String> allMaterialIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(componentReleaseList)){
            //有替代组的主料
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
                //有主料，查询全局替代料
                if(CollectionUtils.isNotEmpty(bomComponentMaterialIdList)) {
                    SecurityTokenHelper.close();
                    substituteReleaseList = hmeEoJobSnBatchMapper.selectSubstituteRelease(tenantId, dto.getSiteId(), bomComponentMaterialIdList);
                    if (CollectionUtils.isNotEmpty(substituteReleaseList)) {
                        //排除反冲料
                        substituteReleaseList = substituteReleaseList.stream().filter(item -> !"2".equals(item.getBackflushFlag())
                                || ("2".equals(item.getBackflushFlag()) && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(substituteReleaseList)){
                            allMaterialIdList.addAll(substituteReleaseList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList()));
                        }
                    }
                }
            }

            //组件清单中替代料
            List<HmeEoJobSnBatchVO4> componentReleaseList3 = componentReleaseList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) <= 0).collect(Collectors.toList());
            List<String> materialIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(componentReleaseList3)) {
                materialIdList = componentReleaseList3.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
            }
            //设置组件清单中替代料的替代组
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
                            //设置组件类型
                            item.setComponentType(HmeConstants.ComponentType.BOM_SUBSTITUTE);
                            item.setIsSubstitute(HmeConstants.ConstantValue.YES);
                        }
                    }
                }
            }
            //筛选没有主料的数据
            componentReleaseList = componentReleaseList.stream().filter(item -> StringUtils.isNotBlank(item.getComponentMaterialId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(componentReleaseList)){
                //没有主料，直接返回
                return resultVOList;
            }
            //组件清单中的料
            if(CollectionUtils.isNotEmpty(componentReleaseList)) {
                for (HmeEoJobSnBatchVO4 item : componentReleaseList
                     ) {
                    if (StringUtils.isBlank(item.getProductionType())) {
                        item.setProductionType(HmeConstants.MaterialTypeCode.LOT);
                    }
                    if (item.getComponentMaterialId().equals(item.getMaterialId())) {
                        //设置组件类型
                        item.setComponentType(HmeConstants.ComponentType.A_MAIN);
                        item.setIsSubstitute(HmeConstants.ConstantValue.NO);
                    }
                }
                resultVOList.addAll(componentReleaseList);
            }
            //全局替代料
            if(CollectionUtils.isNotEmpty(substituteReleaseList)){
                for (HmeEoJobSnBatchVO4 item : substituteReleaseList
                ) {
                    if(StringUtils.isBlank(item.getProductionType())){
                        item.setProductionType(HmeConstants.MaterialTypeCode.LOT);
                    }
                    //设置替代料的序号与组件物料一致
                    List<HmeEoJobSnBatchVO4> singleComponentReleaseList = componentReleaseList.stream().filter(item2 -> item2.getComponentMaterialId().equals(item.getComponentMaterialId())).collect(Collectors.toList());
                    item.setLineNumber(singleComponentReleaseList.get(0).getLineNumber());
                    //设置组件类型
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
                // 进行拆解封装
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
     * @Description 根据eoId查询EO/WO信息
     *
     * @author yuchao.wang
     * @date 2020/11/21 14:38
     * @param tenantId 租户ID
     * @param eoStepId eoStepId
     * @param eoId eoId
     * @return com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO
     *
     */
    @Override
    public HmeEoJobSnSingleBasicVO queryEoAndWoInfoWithComponentByEoId(Long tenantId, String eoStepId, String eoId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryEoAndWoInfoWithComponentByEoId tenantId={},eoStepId={},eoId={}", tenantId, eoStepId, eoId);
        //批量查询EO信息
        List<HmeEoVO4> eoList = hmeEoJobSnMapper.batchQueryEoInfoWithComponentById(tenantId, eoStepId, Collections.singletonList(eoId));
        if (CollectionUtils.isEmpty(eoList) || Objects.isNull(eoList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO信息"));
        }

        //批量查询WO信息
        List<HmeWorkOrderVO2> woList = hmeEoJobSnMapper.batchQueryWoInfoWithComponentById(tenantId, Collections.singletonList(eoList.get(0).getWorkOrderId()));
        if (CollectionUtils.isEmpty(woList) || Objects.isNull(woList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "工单信息"));
        }

        //校验工单对应的产线有效性
        if (StringUtils.isBlank(woList.get(0).getProdLineCode())) {
            //SN对应工单未分配产线或分配产线已失效,请检查!
            throw new MtException("HME_EO_JOB_SN_142", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_142", "HME", "工单信息"));
        }

        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasicVO = new HmeEoJobSnSingleBasicVO();
        hmeEoJobSnSingleBasicVO.setEo(eoList.get(0));
        hmeEoJobSnSingleBasicVO.setWo(woList.get(0));
        return hmeEoJobSnSingleBasicVO;
    }

    /**
     *
     * @Description 指定工艺路线返修-根据eoId查询EO/WO信息
     *
     * @author yuchao.wang
     * @date 2021/1/4 15:52
     * @param tenantId 租户ID
     * @param eoStepId eoStepId
     * @param eoId eoId
     * @param eoRouterBomRel 关系数据
     * @return com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO
     *
     */
    @Override
    public HmeEoJobSnSingleBasicVO queryEoAndWoInfoWithCompByEoIdForDesignedRework(Long tenantId, String eoStepId, String eoId, HmeEoRouterBomRelVO eoRouterBomRel) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryEoAndWoInfoWithCompByEoIdForDesignedRework tenantId={},eoStepId={},eoId={},eoRouterBomRel={}",
                tenantId, eoStepId, eoId, eoRouterBomRel);
        //批量查询EO信息
        List<HmeEoVO4> eoList = hmeEoJobSnMapper.batchQueryEoInfoWithCompByIdForDesignedRework(tenantId, eoStepId, eoId, eoRouterBomRel.getBomId());
        if (CollectionUtils.isEmpty(eoList) || Objects.isNull(eoList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO信息"));
        }
        HmeEoVO4 eo = eoList.get(0);
        eo.setBomId(eoRouterBomRel.getBomId());
        eo.setRouterId(eoRouterBomRel.getRouterId());

        //批量查询WO信息
        List<HmeWorkOrderVO2> woList = hmeEoJobSnMapper.batchQueryWoInfoWithComponentById(tenantId, Collections.singletonList(eo.getWorkOrderId()));
        if (CollectionUtils.isEmpty(woList) || Objects.isNull(woList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "工单信息"));
        }

        //校验工单对应的产线有效性
        if (StringUtils.isBlank(woList.get(0).getProdLineCode())) {
            //SN对应工单未分配产线或分配产线已失效,请检查!
            throw new MtException("HME_EO_JOB_SN_142", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_142", "HME", "工单信息"));
        }

        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasicVO = new HmeEoJobSnSingleBasicVO();
        hmeEoJobSnSingleBasicVO.setEo(eo);
        hmeEoJobSnSingleBasicVO.setWo(woList.get(0));
        return hmeEoJobSnSingleBasicVO;
    }

    /**
     *
     * @Description 查询条码信息
     *
     * @author yuchao.wang
     * @date 2020/11/21 15:03
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return com.ruike.hme.domain.vo.HmeMaterialLotVO3
     *
     */
    @Override
    public HmeMaterialLotVO3 queryMaterialLotInfo(Long tenantId, String materialLotId) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.queryMaterialLotInfo tenantId={},materialLotId={}", tenantId, materialLotId);
        //批量查询EO信息
        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnMapper.batchQueryMaterialLotInfoById(tenantId, Collections.singletonList(materialLotId));
        if (CollectionUtils.isEmpty(materialLotList) || Objects.isNull(materialLotList.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
        }
        return materialLotList.get(0);
    }

    /**
     *
     * @Description 单件出站主程序
     *
     * @author yuchao.wang
     * @date 2020/11/21 18:05
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
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
            // 完工会更新eo状态 故提前判断新旧条码
            boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
            hmeEoJobSnSingleBasic.setNewMaterialLotFlag(newMaterialLotFlag);
        }
        // 创建事件请求
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        // API completeProcess
        MtEoRouterActualVO19 eoRouterActualCompleteParam = new MtEoRouterActualVO19();
        eoRouterActualCompleteParam.setWorkcellId(dto.getWorkcellId());
        eoRouterActualCompleteParam.setQty(eo.getQty());
        eoRouterActualCompleteParam.setEoStepActualId(nearStep.getEoStepActualId());
        eoRouterActualCompleteParam.setEventRequestId(completedEventRequestId);
        eoRouterActualCompleteParam.setSourceStatus("WORKING");
        mtEoRouterActualRepository.completeProcess(tenantId, eoRouterActualCompleteParam);

        //判断是否为完成步骤,非完成步骤时判断是否为返修平台
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getDoneStepFlag())) {
            // 20210402 add by sanfeng.zhang for fang.pan 客户机内部订单不存在整机产出，因此单独进行内部订单调拨接口回传不走完工报工
            if (StringUtils.isBlank(dto.getWorkOrderId())) {
                //未传入工单信息,请检查!
                throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_REWORK_0002", "HME"));
            }
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
            HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, mtWorkOrder.getWorkOrderNum());
            Boolean isInternal = false;
            // 判断工单是否存在于返修拆机记录中
            if (!Objects.isNull(splitRecord)) {
                //判断内部订单号是否为空
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

        //保存设备数据
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //是否容器出站逻辑
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // 创建事件请求
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // 调用{ containerLoad }进行容器装载
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //作业出站
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma 记录实验代码
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

        //V20210225 modify by penglin.sui for jian.zhang 记录数据采集全检/抽检
        if(StringUtils.isNotBlank(dto.getInspection())){
            HmeEoJobSn hmeEoJobSnPara = new HmeEoJobSn();
            hmeEoJobSnPara.setJobId(dto.getJobId());
            hmeEoJobSnPara.setAttribute2(dto.getInspection());
            hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSnPara);
        }

        // 首序 记录job_id 到条码上
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
        // 器件测试 勾选了交叉复测 完工时清除待交叉复测标识JCRETEST
        if (HmeConstants.ConstantValue.YES.equals(dto.getCrossRetestFlag())) {
            // 判断交叉复测标识是否为Y 为Y时 返修标识置为Y
            // 旧条码找新条码 查询交叉复测标识及更新扩展字段
            String materialLotId = dto.getMaterialLotId();
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
                // 完工会更新eo状态 故提前判断新旧条码
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
            // 查询交叉复测标识
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(finalMaterialLotId);
                setTableName("mt_material_lot_attr");
                setAttrName("JCRETEST");
            }});
            String jcRetest = CollectionUtils.isNotEmpty(mtExtendAttrVOS) ? mtExtendAttrVOS.get(0).getAttrValue() : "";
            // 创建交叉复测完成事件
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

        //自动判定不良
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
            // 完工会更新eo状态 故提前判断新旧条码
            boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
            hmeEoJobSnSingleBasic.setNewMaterialLotFlag(newMaterialLotFlag);
        }

        // 创建事件请求
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        // API completeProcess
        MtEoRouterActualVO19 eoRouterActualCompleteParam = new MtEoRouterActualVO19();
        eoRouterActualCompleteParam.setWorkcellId(dto.getWorkcellId());
        eoRouterActualCompleteParam.setQty(eo.getQty());
        eoRouterActualCompleteParam.setEoStepActualId(nearStep.getEoStepActualId());
        eoRouterActualCompleteParam.setEventRequestId(completedEventRequestId);
        eoRouterActualCompleteParam.setSourceStatus("WORKING");
        mtEoRouterActualRepository.completeProcess(tenantId, eoRouterActualCompleteParam);

        //判断是否为完成步骤,非完成步骤时判断是否为返修平台
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getDoneStepFlag())) {
            // 20210402 add by sanfeng.zhang for fang.pan 客户机内部订单不存在整机产出，因此单独进行内部订单调拨接口回传不走完工报工
            if (StringUtils.isBlank(dto.getWorkOrderId())) {
                //未传入工单信息,请检查!
                throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_REWORK_0002", "HME"));
            }
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
            HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, mtWorkOrder.getWorkOrderNum());
            Boolean isInternal = false;
            // 判断工单是否存在于返修拆机记录中
            if (!Objects.isNull(splitRecord)) {
                //判断内部订单号是否为空
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

        //保存设备数据
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //是否容器出站逻辑
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // 创建事件请求
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // 调用{ containerLoad }进行容器装载
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //作业出站
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma 记录实验代码
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

        //V20210225 modify by penglin.sui for jian.zhang 记录数据采集全检/抽检
        if(StringUtils.isNotBlank(dto.getInspection())){
            HmeEoJobSn hmeEoJobSnPara = new HmeEoJobSn();
            hmeEoJobSnPara.setJobId(dto.getJobId());
            hmeEoJobSnPara.setAttribute2(dto.getInspection());
            hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSnPara);
        }

        // 创建交叉复测事件
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
        // 旧条码找新条码 保存交叉复测标识
        String materialLotId = dto.getMaterialLotId();
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            // 完工会更新eo状态 故提前判断新旧条码
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

        //发起不良
        launchNc(tenantId, dto, hmeEoJobSnSingleBasic);

        return resultTransactionResponseList;
    }

    /**
     *
     * @Description 完工步骤出站
     *
     * @author yuchao.wang
     * @date 2020/11/21 22:42
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
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
        //返修作业平台完工出站时，SN的货位首先更新为当前工位WKC对应工段WKC对应的完工货位，
        // 若当前工位WKC对应工段WKC没有维护完工货位，则货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        // 当前工位WKC对应工段WKC对应的完工货位
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDoneStep  ReworkProcessFlag()  "+hmeEoJobSnSingleBasic.getReworkProcessFlag() );
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            // 当前工位WKC对应工段WKC对应的完工货位
            MtModLocator completeLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, "28", dto.getWorkcellId());

            if (ObjectUtil.isNotNull(completeLocator)) {
                BeanUtils.copyProperties(completeLocator, mtModLocator);
            } else {
                //未查询到当前工位WKC对应工段WKC对应的完工货位，查询当前工位WKC对应工段WKC对应的默认存储货位
                MtModLocator defaultLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
                BeanUtils.copyProperties(defaultLocator, mtModLocator);
            }
        } else {
            //非返修完工， 查询当前工位WKC对应工段WKC对应的默认存储货位
            MtModLocator defaultLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
            BeanUtils.copyProperties(defaultLocator, mtModLocator);
        }
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDoneStep  mtModLocator={}  "+  mtModLocator);

        //查询货位对应的仓库
        MtModLocator warehouse = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtModLocator.getLocatorId());

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //创建事件
        String changeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setEventTypeCode("HOME_MADE_PRODUCTION_REWORK_COMPLETE");
            }
        });

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });
        //创建事件
        String onhandIncreaseEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("INV_ONHAND_INCREASE");
            }
        });
        //完工事务回传
        String eoCompleteEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setWorkcellId(dto.getWorkcellId());
                setEventTypeCode("HME_EO_COMPLETE");
            }
        });

        //批量查询移动类型
        Map<String, String> moveTypeMap = getMoveTypeForMainOutSite(tenantId);

        //构造条码批量更新参数-INV_ONHAND_INCREASE事件
        MtMaterialLotVO2 mtLotOnhandUpdate = new MtMaterialLotVO2();
        mtLotOnhandUpdate.setEventId(onhandIncreaseEventId);
        mtLotOnhandUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotOnhandUpdate.setLot(StringUtils.trimToEmpty(hmeEoJobSnSingleBasic.getLotCode()));
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotOnhandUpdate, HmeConstants.ConstantValue.NO);

        // 更新现有量
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(eo.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(mtModLocator.getLocatorId());
        // 20211119 modify by sanfeng.zhang for wenxin.zhang 取条码物料 降级或升级 会导致条码和EO物料不一致 以条码为主
        mtInvOnhandQuantityVO9.setMaterialId(dto.getSnMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(eo.getQty());
        mtInvOnhandQuantityVO9.setEventId(onhandIncreaseEventId);
        mtInvOnhandQuantityVO9.setLotCode(StringUtils.trimToEmpty(hmeEoJobSnSingleBasic.getLotCode()));
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie 存在数据项不良，置条码质量状态为NG
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
            // 20210812 add by sanfeng.zhang for tianyang.xie 返修作业平台 旧条码最后一道序出站时将新条码失效
            // 判断当前EO是否为旧条码 为空 则为新条码 否则旧条码
            Boolean newMaterialLotFlag = hmeEoJobSnSingleBasic.getNewMaterialLotFlag();
            if (!newMaterialLotFlag) {
                List<String> newMaterialLotIdList = hmeEoJobSnReWorkMapper.queryNewMaterialLotByOldMaterialLot(tenantId, dto.getMaterialLotId());
                // 找到对应的新条码的ID
                if (CollectionUtils.isEmpty(newMaterialLotIdList)) {
                    throw new MtException("HME_REPAIR_SN_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_REPAIR_SN_0008", "HME"));
                }
                // 失效新条码
                List<MtMaterialLotVO20> materialLotList = new ArrayList<>();
                for (String newMaterialLotId : newMaterialLotIdList) {
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(newMaterialLotId);
                    mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.NO);
                    mtMaterialLotVO20.setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
                    materialLotList.add(mtMaterialLotVO20);
                }
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, changeEventId, HmeConstants.ConstantValue.NO);

                // 旧条码最后一道序返修出站 去掉旧条码进站标识
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

        //构造条码扩展属性批量更新参数-INV_ONHAND_INCREASE事件
        List<MtExtendVO5> mtLotExtendOnhandList = new ArrayList<>();
        MtExtendVO5 mfFlagExtend = new MtExtendVO5();
        mfFlagExtend.setAttrName("MF_FLAG");
        mfFlagExtend.setAttrValue("");
        mtLotExtendOnhandList.add(mfFlagExtend);
        //V20210525 modify by penglin.sui for fang.pan 末道序完工时，如果条码版本扩展字段有值，则不更新版本
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

        //构造条码扩展属性批量更新参数-EO_WKC_STEP_COMPLETE事件
        List<MtExtendVO5> mtLotExtendCompleteList = new ArrayList<>();
        MtExtendVO5 statusExtend = new MtExtendVO5();
        statusExtend.setAttrName("STATUS");
        statusExtend.setAttrValue("COMPLETED");
        mtLotExtendCompleteList.add(statusExtend);
        // 返修作业平台-销单 新条码如果新工单没有销单 则取旧工单的销单
        String soNum = wo.getSoNum();
        String soLineNum = wo.getSoLineNum();
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag()) && HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            // 旧条码不改变销单 新条码改变销单
            // 根据新EO 取对应原工单的销单信息
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
        // 将更改后的销单写入 方便记事务
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

        //工单SN关系
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

        //V20210302 modify by penglin.sui for fang.pan 返修工序作业平台更新售后返品拆机
        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getMaterialLot().getAfFlag())) {
                HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryLastSplitRecordOfMaterialLot2(tenantId, hmeEoJobSnSingleBasic.getMaterialLot().getMaterialLotId());
                if (Objects.nonNull(hmeServiceSplitRecord)) {
                    //当前时间
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

                        //记录历史
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

        //构造报工/完工事务
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = getTransactionRequestVOForSingleList(tenantId,
                eoCompleteEventId, dto, hmeEoJobSnSingleBasic, mtModLocator, warehouse, moveTypeMap);

        //批量记录事务
        List<WmsObjectTransactionResponseVO> transactionResponseList = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

        //获取降级品接口请求报文
        Map<String, HmeWorkOrderVO2> woMap = new HashMap<>();
        woMap.put(wo.getWorkOrderId(), wo);
        List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOList = getErpReducedSettleRadioUpdateDTOList(tenantId, woMap);

        //发送降级品比例接口
        if (CollectionUtils.isNotEmpty(erpReducedSettleRadioUpdateDTOList)) {
            try {
                itfWorkOrderIfaceService.erpReducedSettleRadioUpdateRestProxy(tenantId, erpReducedSettleRadioUpdateDTOList);
            } catch (Exception e) {
                log.error("=================降级品比例接口调用失败================={}:" + e.getMessage(), erpReducedSettleRadioUpdateDTOList);
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
     * @Description 非完工步骤出站
     *
     * @author yuchao.wang
     * @date 2020/11/22 0:45
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mainOutSiteForNormalStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForNormalStep tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();

        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie 存在数据项不良，置条码质量状态为NG
            if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //如果是返修完成则更新返修标识
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())
                && StringUtils.isNotBlank(materialLot.getReworkFlag())) {
            //构造条码扩展属性批量更新参数-EO_WKC_STEP_COMPLETE事件
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
     * @Description 工段完工数据统计-工序作业平台/PDA工序作业平台
     *
     * @author yuchao.wang
     * @date 2020/11/22 11:35
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wkcCompleteOutputRecord(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.wkcCompleteOutputRecord tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        //返修不统计
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag())) {
            return;
        }

        //获取工段
        MtModOrganizationVO2 lineParam = new MtModOrganizationVO2();
        lineParam.setTopSiteId(dto.getSiteId());
        lineParam.setOrganizationId(dto.getWorkcellId());
        lineParam.setOrganizationType("WORKCELL");
        lineParam.setParentOrganizationType("WORKCELL");
        lineParam.setQueryType("TOP");
        List<MtModOrganizationItemVO> lineVOList = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, lineParam);
        if (CollectionUtils.isEmpty(lineVOList) || Objects.isNull(lineVOList.get(0))) {
            // 请先维护Wkc工序工段关系
            throw new MtException("HME_EO_JOB_SN_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_023", "HME"));
        }
        String workcellId = lineVOList.get(0).getOrganizationId();

        //获取未完工的其他工艺步骤对应的工段
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        List<HmeEoJobSnDTO3> hmeEoJobSnDTO3s = hmeEoJobSnMapper.queryUnfinishedSection(tenantId, eo.getEoId(), dto.getSiteId());

        //如果有当前工段则不统计
        if(CollectionUtils.isNotEmpty(hmeEoJobSnDTO3s)) {
            List<String> workcellIdList = hmeEoJobSnDTO3s.stream()
                    .filter(t -> !HmeConstants.ConstantValue.YES.equals(t.getPrepareFlag()))
                    .map(HmeEoJobSnDTO3::getWorkcellId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(workcellIdList) && workcellIdList.contains(workcellId)) {
                return;
            }
        }

        //更新/新增工段完工数据统计
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
     * @Description 不良记录数据项记录不良代码
     *
     * @author penglin.sui
     * @date 2021/5/19 17:39
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
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
        //V20210519 modify by penglin.sui for peng.zhao 所有不良代码的不良代码组都为同一个
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
        // 不良代码去重
        ncCodeIdList = ncCodeIdList.stream().distinct().collect(Collectors.toList());

        if(StringUtils.isBlank(ncGroupId)){
            //【${1}】找不到不良代码组,请检查不良代码
            throw new MtException("HME_EO_JOB_SN_197", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_197", "HME", ncCodes.toString()));
        }

        if(ncCodes.length() > 0){
            //R:返修逻辑 N:发起不良 ND:发起不良 + 降级 ZD:指定工艺路线返修
            String flag = "N";
            if(singleFlag){
                if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                    flag = "R";
                }else if(HmeConstants.ConstantValue.YES.equals(downGradeFlag)){
                    flag = "ND";
                } else {
                    // 判断是否指定工艺路线返修
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
            // 反射镜不良 直接发起不良 写入单路状态
            if (hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId())) {
                flag = "N";

                // 查询出判定的记录
                List<HmeEoJobDataRecordVO2> hmeEoJobDataRecordList = hmeEoJobDataRecordMapper.queryJudgmentRecord(tenantId, dto.getJobId());
                log.info("<============hmeEoJobDataRecordList============>" + JSONArray.toJSONString(hmeEoJobDataRecordList));
                List<HmeDataRecordExtend> hmeDataRecordExtendList = new ArrayList<>();
                hmeEoJobDataRecordList.forEach(record -> {
                    HmeDataRecordExtend hmeDataRecordExtend = new HmeDataRecordExtend();
                    // 前面做过校验 这里认为就是数值的
                    BigDecimal result = BigDecimal.valueOf(Double.valueOf(record.getResult()));
                    if (result.compareTo(record.getStandardValue()) >= 0 ) {
                        // 大于标准值 为合格
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

            //自动判定不良
            HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO11 = new HmeNcDisposePlatformDTO11();
            // 20211115 modify by sanfeng.zhang 旧条码找对应的新条码发起不良
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
                    //发起不良
                    log.info("<============autoJudgeNc-发起不良 begin============>");
                    hmeNcDisposePlatformDTO11.setFlag("2");
                    hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                    log.info("<============autoJudgeNc-发起不良 end============>");
                    break;
                case "R":
                    //返修逻辑
                    log.info("<============autoJudgeNc-返修 begin============>");
                    hmeNcDisposePlatformDTO11.setFlag("3");
                    if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
                        hmeNcDisposePlatformDTO11.setReworkRecordFlag(YES);
                    }
                    hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                    log.info("<============autoJudgeNc-返修 end============>");
                    break;
                case "ND":
                    //发起不良
                    log.info("<============autoJudgeNc-发起不良 begin============>");
                    hmeNcDisposePlatformDTO11.setFlag("2");
                    String parentNcRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                    log.info("<============autoJudgeNc-发起不良 end============>");
                    if(StringUtils.isBlank(parentNcRecordId)){
                        //	${1}不存在.${2}
                        throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0004", "ASSEMBLE", "不良记录","autoJudgeNc"));
                    }
                    //降级
                    //查询不良记录
                    log.info("<============autoJudgeNc-降级 begin============>");
                    //查询降级物料ID
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
                    log.info("<============autoJudgeNc-降级 end============>");
                    break;
                case "ZD":
                    //发起不良
                    log.info("<============autoJudgeNc-发起不良 begin============>");
                    hmeNcDisposePlatformDTO11.setFlag("2");
                    String ncRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                    log.info("<============autoJudgeNc-发起不良 end============>");
                    if(StringUtils.isBlank(ncRecordId)){
                        //	${1}不存在.${2}
                        throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0004", "ASSEMBLE", "不良记录","autoJudgeNc"));
                    }
                    log.info("<============autoJudgeNc-指定工艺路线返修 begin============>");
                    HmeNcCheckDTO4 ncCheckDTO4 = new HmeNcCheckDTO4();
                    ncCheckDTO4.setNcGroupId(ncGroupId);
                    ncCheckDTO4.setNcCodeIdList(ncCodeIdList);
                    ncCheckDTO4.setNcRecordIdList(Collections.singletonList(ncRecordId));
                    ncCheckDTO4.setProcessMethod("7");
                    hmeNcCheckService.batchCheckSubmit(tenantId, ncCheckDTO4);
                    log.info("<============autoJudgeNc-指定工艺路线返修 end============>");
                    break;
            }
        }
    }

    private String getRouteByNcCode(Long tenantId, String ncGroupId, List<String> ncCodeIdList, HmeEoJobSnVO3 dto) {
        if (CollectionUtils.isEmpty(ncCodeIdList)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "不良代码"));
        }
        if (ncCodeIdList.size() > 1) {
            throw new MtException("HME_NC_0086", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0086", "HME"));
        }
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(dto.getEoId());
        if (mtEo.getIdentification().length() < 9) {
            throw new MtException("HME_CHIP_DATA_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0009", "HME", "序列号"));
        }
        // 根据eo 查询wo的产线
        List<String> proLineList = hmeNcCheckMapper.queryProLineByEoId(tenantId, dto.getEoId());
        if (CollectionUtils.isEmpty(proLineList)) {
            throw new MtException("HME_NC_0083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0083", "HME", mtEo.getIdentification()));
        }
        // 取SN 3,4位
        String deviceType = mtEo.getIdentification().substring(2, 4);
        // 取SN 5,6位
        String chipType = mtEo.getIdentification().substring(4, 6);
        // 获取最后正常工艺 eoJobSn 限制eo 非返修最近的工艺
        String operationId = hmeNcCheckMapper.queryLastNonReworkOperationId(tenantId, dto.getEoId());
        List<String> routeIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(operationId)) {
            routeIdList = hmeNcCheckRepository.queryRouteIdListLimitOperationId(tenantId, ncGroupId, ncCodeIdList, proLineList.get(0), deviceType, chipType, operationId);
        }
        // 以上限制工艺找不到 则不限制工艺去找
        if (CollectionUtils.isEmpty(routeIdList)) {
            routeIdList = hmeNcCheckRepository.queryRouteIdListNonLimitOperationId(tenantId, ncGroupId, ncCodeIdList, proLineList.get(0), deviceType, chipType);
        }
        return CollectionUtils.isNotEmpty(routeIdList) ?  routeIdList.get(0) : "";
    }

    /**
     *
     * @Description 批量不良记录数据项记录不良代码
     *
     * @author penglin.sui
     * @date 2021/5/19 17:39
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
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
            //V20210519 modify by penglin.sui for peng.zhao 所有不良代码的不良代码组都为同一个
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
                //【${1}】找不到不良代码组,请检查不良代码
                throw new MtException("HME_EO_JOB_SN_197", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_197", "HME", ncCodes.toString()));
            }

            if(ncCodes.length() > 0){

                //R:返修逻辑 N:发起不良 ND:发起不良 + 降级 ZD:指定工艺路线返修
                String flag = "N";
                if(singleFlag){
                    if(HmeConstants.ConstantValue.YES.equals(reworkFlag)){
                        flag = "R";
                    }else if(HmeConstants.ConstantValue.YES.equals(downGradeFlag)){
                        flag = "ND";
                    } else {
                        // 判断是否指定工艺路线返修
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

                //自动判定不良
                HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO11 = new HmeNcDisposePlatformDTO11();
                hmeNcDisposePlatformDTO11.setMaterialLotCode(hmeEoJobSnVO16.getMaterialLotMap().get(snLine.getMaterialLotId()).getMaterialLotCode());
                hmeNcDisposePlatformDTO11.setNcGroupId(ncGroupId);
                hmeNcDisposePlatformDTO11.setNcCodeIdList(ncCodeIdList);
                hmeNcDisposePlatformDTO11.setWorkcellId(dto.getWorkcellId());
                hmeNcDisposePlatformDTO11.setCurrentwWorkcellId(dto.getWorkcellId());
                switch (flag){
                    case "N":
                        //发起不良
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        break;
                    case "R":
                        //返修逻辑
                        hmeNcDisposePlatformDTO11.setFlag("3");
                        hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        break;
                    case "ND":
                        //发起不良
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        String parentNcRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        if(StringUtils.isBlank(parentNcRecordId)){
                            //	${1}不存在.${2}
                            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ASSEMBLE_0004", "ASSEMBLE", "不良记录","batchAutoJudgeNc"));
                        }
                        //降级
                        HmeWorkOrderVO2 hmeWorkOrderVO2 = hmeEoJobSnVO16.getWoMap().get(snLine.getWorkOrderId());
                        //查询降级物料ID
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
                        //发起不良
                        log.info("<============autoJudgeNc-发起不良 begin============>");
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        String ncRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        log.info("<============autoJudgeNc-发起不良 end============>");
                        if(StringUtils.isBlank(ncRecordId)){
                            //	${1}不存在.${2}
                            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ASSEMBLE_0004", "ASSEMBLE", "不良记录","autoJudgeNc"));
                        }
                        log.info("<============autoJudgeNc-指定工艺路线返修 begin============>");
                        HmeNcCheckDTO4 ncCheckDTO4 = new HmeNcCheckDTO4();
                        ncCheckDTO4.setNcGroupId(ncGroupId);
                        ncCheckDTO4.setNcCodeIdList(ncCodeIdList);
                        ncCheckDTO4.setNcRecordIdList(Collections.singletonList(ncRecordId));
                        ncCheckDTO4.setProcessMethod("7");
                        hmeNcCheckService.batchCheckSubmit(tenantId, ncCheckDTO4);
                        log.info("<============autoJudgeNc-指定工艺路线返修 end============>");
                        break;
                }
            }
        }
    }

    /**
     *
     * @Description 时效平台-不良记录数据项记录不良代码
     *
     * @author penglin.sui
     * @date 2021/5/21 10:49
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
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
            //V20210519 modify by penglin.sui for peng.zhao 所有不良代码的不良代码组都为同一个
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

                //R:返修逻辑 N:发起不良 ND:发起不良 + 降级
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

                //自动判定不良
                HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO11 = new HmeNcDisposePlatformDTO11();
                hmeNcDisposePlatformDTO11.setMaterialLotCode(hmeEoJobSnVO16.getMaterialLotMap().get(snLine.getMaterialLotId()).getMaterialLotCode());
                hmeNcDisposePlatformDTO11.setNcGroupId(ncGroupId);
                hmeNcDisposePlatformDTO11.setNcCodeIdList(ncCodeIdList);
                hmeNcDisposePlatformDTO11.setWorkcellId(dto.getWorkcellId());
                hmeNcDisposePlatformDTO11.setCurrentwWorkcellId(dto.getWorkcellId());
                switch (flag){
                    case "N":
                        //发起不良
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        break;
                    case "R":
                        //返修逻辑
                        hmeNcDisposePlatformDTO11.setFlag("3");
                        hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        break;
                    case "ND":
                        //发起不良
                        hmeNcDisposePlatformDTO11.setFlag("2");
                        String parentNcRecordId = hmeNcDisposePlatformService.processNcTypeCreate(tenantId, hmeNcDisposePlatformDTO11);
                        if(StringUtils.isBlank(parentNcRecordId)){
                            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ASSEMBLE_0004", "ASSEMBLE", "不良记录","batchAutoJudgeNc"));
                        }
                        //降级
                        //查询不良记录
                        List<MtNcRecord> mtNcRecordList = mtNcRecordRepository.selectByCondition(Condition.builder(MtNcRecord.class)
                                .andWhere(Sqls.custom().andEqualTo(MtNcRecord.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtNcRecord.FIELD_PARENT_NC_RECORD_ID, parentNcRecordId)).build());
                        if(CollectionUtils.isNotEmpty(mtNcRecordList)){
                            HmeWorkOrderVO2 hmeWorkOrderVO2 = hmeEoJobSnVO16.getWoMap().get(snLine.getWorkOrderId());
                            //查询降级物料ID
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
     * @Description 返修作业平台出站-非完工步骤出站
     *
     * @author yuchao.wang
     * @date 2021/1/7 20:44
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mainOutSiteForNormalStepForReworkProcess(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForNormalStepForReworkProcess tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        //V20210312 modify by penglin.sui for tianyang.xie 存在数据项不良，置条码质量状态为NG
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
     * @Description 单件返修出站主程序
     *
     * @author yuchao.wang
     * @date 2020/12/15 15:28
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mainOutSiteForRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForRework tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeRouterStepVO nearStep = hmeEoJobSnSingleBasic.getNearStep();

        // 创建事件请求
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            // 完工会更新eo状态 故提前判断新旧条码
            boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
            hmeEoJobSnSingleBasic.setNewMaterialLotFlag(newMaterialLotFlag);
        }

        //调用API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //是否容器出站逻辑
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // 创建事件请求
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // 调用{ containerLoad }进行容器装载
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //保存设备数据
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //作业出站
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        //返修作业平台 售后维修存在异常出站， 若售后维修出站方式为异常出站，attribute=Y 正常出站 attribute为空或null
        if (StringUtils.isNotBlank(dto.getIsAbnormalOutSite()) && HmeConstants.ConstantValue.YES.equals(dto.getIsAbnormalOutSite())) {
            updateEoJobSn.setAttribute7(dto.getIsAbnormalOutSite());
        }
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma 记录实验代码
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

        //自动判定不良
        autoJudgeNc(tenantId , dto , hmeEoJobSnSingleBasic);
    }

    /**
     *
     * @Description 单件指定工艺路线返修出站主程序
     *
     * @author yuchao.wang
     * @date 2021/1/4 20:42
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    @Override
    public void mainOutSiteForDesignedReworkComplete(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDesignedReworkComplete tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeRouterStepVO nearStep = hmeEoJobSnSingleBasic.getNearStep();

        // 创建事件请求
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //调用API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
        //V20210312 modify by penglin.sui for tianyang.xie 存在数据项不良，置条码质量状态为NG
        if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
            List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //构造条码扩展属性批量更新参数-EO_WKC_STEP_COMPLETE事件
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

        //是否容器出站逻辑
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // 创建事件请求
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // 调用{ containerLoad }进行容器装载
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //保存设备数据
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //作业出站
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma 记录实验代码
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
        // 器件测试 勾选了交叉复测 完工时清除待交叉复测标识JCRETEST
        if (HmeConstants.ConstantValue.YES.equals(dto.getCrossRetestFlag())) {
            // 判断交叉复测标识是否为Y 为Y时 返修标识置为Y
            // 查询交叉复测标识
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(dto.getMaterialLotId());
                setTableName("mt_material_lot_attr");
                setAttrName("JCRETEST");
            }});
            String jcRetest = CollectionUtils.isNotEmpty(mtExtendAttrVOS) ? mtExtendAttrVOS.get(0).getAttrValue() : "";
            // 创建交叉复测完成事件
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

        //自动判定不良
        autoJudgeNc(tenantId , dto , hmeEoJobSnSingleBasic);
    }

    @Override
    public void mainOutSiteForDesignedReworkComplete2(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.mainOutSiteForDesignedReworkComplete tenantId={},dto={},hmeEoJobSnSingleBasic={}", tenantId, dto, hmeEoJobSnSingleBasic);
        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeRouterStepVO nearStep = hmeEoJobSnSingleBasic.getNearStep();

        // 创建事件请求
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //调用API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
        //V20210312 modify by penglin.sui for tianyang.xie 存在数据项不良，置条码质量状态为NG
        if(CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
            List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //构造条码扩展属性批量更新参数-EO_WKC_STEP_COMPLETE事件
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

        //是否容器出站逻辑
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            HmeEoJobContainer eoJobContainer = hmeEoJobSnSingleBasic.getEoJobContainer();
            // 创建事件请求
            String outSiteEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

            // 调用{ containerLoad }进行容器装载
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            mtContainerVO24.setContainerId(eoJobContainer.getContainerId());
            mtContainerVO24.setEventRequestId(outSiteEventRequestId);
            mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());
            mtContainerVO24.setTrxLoadQty(eo.getQty());
            mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
        }

        //保存设备数据
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            log.info("<====== HmeEoJobSnCommonServiceImpl.snInToSiteEquipmentRecord equSwitchParams={}", equSwitchParams);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        }

        //作业出站
        HmeEoJobSn updateEoJobSn = new HmeEoJobSn();
        updateEoJobSn.setJobId(dto.getJobId());
        updateEoJobSn.setSiteOutDate(hmeEoJobSnSingleBasic.getCurrentDate());
        updateEoJobSn.setSiteOutBy(hmeEoJobSnSingleBasic.getUserId());
        updateEoJobSn.setMaterialLotId(dto.getMaterialLotId());
        hmeEoJobSnMapper.updateByPrimaryKeySelective(updateEoJobSn);

        //V20210126 modify by penglin.sui for hui.ma 记录实验代码
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
        //创建交叉复测事件
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

        //发起不良
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
        //V20210519 modify by penglin.sui for peng.zhao 所有不良代码的不良代码组都为同一个
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
            //【${1}】找不到不良代码组,请检查不良代码
            throw new MtException("HME_EO_JOB_SN_197", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_197", "HME", ncCodes.toString()));
        }

        if(ncCodes.length() > 0){
            //发起不良
            HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
            hmeEoJobDataRecord.setJobRecordId(hmeEoJobDataRecordVO2.getJobRecordId());
            hmeEoJobDataRecord.setRemark(ncCodes.toString());
            hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(hmeEoJobDataRecord);
            log.info("<============launchNc-返修 begin============>");
            HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO11 = new HmeNcDisposePlatformDTO11();
            // 20211115 modify by sanfeng.zhang 旧条码找对应新条码发起不良
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
            log.info("<============launchNc-返修 end============>");
        }
    }

    /**
     *
     * @Description 时效作业平台查询已进站的作业
     *
     * @author yuchao.wang
     * @date 2020/12/23 17:13
     * @param tenantId 租户ID
     * @param isRework 是否返修 true:是 false:否
     * @param operationId 工艺ID
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

        //如果是返修则只会有一个eo,走查询单个eo的逻辑
        if (isRework) {
            return hmeEoJobSnMapper.queryEoJobSnForTimeInSite(tenantId, isRework, operationId, eoIdList.get(0), new ArrayList<String>());
        } else {
            return hmeEoJobSnMapper.queryEoJobSnForTimeInSite(tenantId, isRework, operationId, StringUtils.EMPTY, eoIdList);
        }
    }

    /**
     *
     * @Description 时效作业-继续返修
     *
     * @author yuchao.wang
     * @date 2020/12/24 11:19
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO24 参数
     * @return void
     *
     */
    @Override
    public void batchMainOutSiteForRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO24 hmeEoJobSnVO24) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSiteForRework tenantId={},dto={},hmeEoJobSnVO24={}", tenantId, dto, hmeEoJobSnVO24);
        //继续返修只能单条
        HmeMaterialLotVO3 hmeMaterialLot = hmeEoJobSnVO24.getReworkMaterialLot();
        MtEo eo = hmeEoJobSnVO24.getReworkEo();

        //查询最近一步工艺步骤
        HmeRouterStepVO nearStep = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, eo.getEoId());
        if (Objects.isNull(nearStep) || StringUtils.isBlank(nearStep.getEoStepActualId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "最近加工步骤"));
        }

        // 创建事件请求
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //调用API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //如果是物料批并且有容器则先卸载
        if (StringUtils.isNotBlank(hmeMaterialLot.getCurrentContainerId())) {
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(hmeMaterialLot.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(hmeMaterialLot.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件 继续返修情况下只会有一条数据
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(hmeMaterialLot.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //批量更新出站
        List<String> jobIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct().collect(Collectors.toList());
        hmeEoJobSnRepository.batchOutSite2(tenantId, hmeEoJobSnVO24.getUserId(), jobIdList, dto.getRemark());
    }

    /**
     *
     * @Description 时效作业-指定工艺路线返修-加工完成
     *
     * @author yuchao.wang
     * @date 2021/1/8 10:24
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO24 参数
     * @return void
     *
     */
    @Override
    public void batchMainOutSiteForDesignedReworkComplete(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO24 hmeEoJobSnVO24) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.batchMainOutSiteForDesignedReworkComplete tenantId={},dto={},hmeEoJobSnVO24={}", tenantId, dto, hmeEoJobSnVO24);
        //调用指定工艺路线返修加工完成通用主方法,返修只能单个条码
        HmeMaterialLotVO3 hmeMaterialLot = hmeEoJobSnVO24.getReworkMaterialLot();
        MtEo eo = hmeEoJobSnVO24.getReworkEo();

        //查询最近一步工艺步骤
        HmeRouterStepVO nearStep = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, eo.getEoId());
        if (Objects.isNull(nearStep) || StringUtils.isBlank(nearStep.getEoStepActualId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "最近加工步骤"));
        }

        // 创建事件请求
        String completedEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //调用API eoWkcAndStepComplete
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(nearStep.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(eo.getQty());
        mtEoRouterActualVO19.setEventRequestId(completedEventRequestId);
        mtEoRouterActualVO19.setSourceStatus(nearStep.getStatus());
        mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        //创建事件请求
        String invUpdateEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

        //出站时，SN的货位更新为当前工位WKC对应工段WKC对应的默认存储货位
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(invUpdateEventRequestId);
                setLocatorId(mtModLocator.getLocatorId());
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件 返修情况下只会有一条数据
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(completeEventId);
        mtLotCompleteUpdate.setMaterialLotId(hmeMaterialLot.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(mtModLocator.getLocatorId());
        mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //构造条码扩展属性批量更新参数-EO_WKC_STEP_COMPLETE事件
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

        //批量更新出站
        List<String> jobIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct().collect(Collectors.toList());
        hmeEoJobSnRepository.batchOutSite2(tenantId, hmeEoJobSnVO24.getUserId(), jobIdList, dto.getRemark());

        //不良记录数据项记录不良代码
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
        //虚拟件 && 虚拟件组件关系
        Map<String,String> virtualAndComponentRelMap = new HashMap<>();
        //虚拟件组件
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
            //虚拟件
            List<HmeEoJobSnBatchVO4> virtualList = componentList.stream().filter(item -> HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                    && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
            virtualList.forEach(item -> {
                if(CollectionUtils.isNotEmpty(item.getMaterialLotList())){
                    virtualMaterialLotIdList.addAll(item.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getMaterialLotId).collect(Collectors.toList()));
                }
            });
            //序列物料
            List<HmeEoJobSnBatchVO4> allVirtualComponentList2 = allVirtualComponentList.stream().filter(item -> item.getProductionType().equals(HmeConstants.MaterialTypeCode.SN))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(allVirtualComponentList2)){
                allSnMaterialIdList = allVirtualComponentList2.stream().map(HmeEoJobSnBatchVO4::getMaterialId).collect(Collectors.toList());
            }
            //批次/时效物料
            List<HmeEoJobSnBatchVO4> allVirtualComponentList3 = allVirtualComponentList.stream().filter(item -> item.getProductionType().equals(HmeConstants.MaterialTypeCode.LOT)
                    || item.getProductionType().equals(HmeConstants.MaterialTypeCode.TIME)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(allVirtualComponentList3)){
                allMaterialIdList = allVirtualComponentList3.stream().map(HmeEoJobSnBatchVO4::getMaterialId).collect(Collectors.toList());
            }
            //查询虚拟件组件的JOB ID
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
     * @Description 创建工序作业-单件作业平台
     *
     * @author yuchao.wang
     * @date 2020/12/31 14:58
     * @param tenantId 租户ID
     * @param bomId bomId
     * @param snLine 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO2
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO2 createSnJobForSingle(Long tenantId, String bomId, HmeEoJobSnVO3 snLine) {
        log.info("<====== HmeEoJobSnCommonServiceImpl.createSnJobForSingle tenantId={},bomId={},snLine={}", tenantId, bomId, snLine);
        HmeEoJobSnVO2 resultVO = new HmeEoJobSnVO2();
        BeanUtils.copyProperties(snLine, resultVO);
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // 获取当前时间
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

        //V20210312 modify by penglin.sui for tianyang.xie 返修进站时，按tenant_id+eo_id+workcell_id+operation_id+rework_flag+job_type+material_lot_id取最大的eo_step_num+1没有为0
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

        //创建作业物料信息
        materialInSiteForSingle(tenantId, resultVO, bomId);

        //采集数据和自检数据
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = hmeEoJobDataRecordRepository.inSiteScan(tenantId, resultVO);
        resultVO.setDataRecordVOList(hmeEoJobDataRecordVOList);
        return resultVO;
    }

    /**
     *
     * @Description 判断条码是否为物料批条码或者容器条码
     *
     * @author yuchao.wang
     * @date 2021/1/15 10:35
     * @param tenantId 租户ID
     * @param code 条码
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
     * @Description 校验数据采集项不良判定
     *
     * @author yuchao.wang
     * @date 2021/1/25 13:47
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @param eoJobDataRecordList 数据采集项结果
     * @param processNcInfo 工序不良信息
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    @Override
    public HmeEoJobSn dataRecordProcessNcValidate(Long tenantId, String materialLotCode, List<HmeEoJobDataRecord> eoJobDataRecordList, HmeProcessNcHeaderVO2 processNcInfo) {
        //入参校验
        if (CollectionUtils.isEmpty(eoJobDataRecordList)
                || Objects.isNull(processNcInfo) || StringUtils.isBlank(processNcInfo.getHeaderId())) {
            return null;
        }
        if (CollectionUtils.isEmpty(processNcInfo.getLineList())) {
            //此SN【{$1}】查询不到判定标准，请检查！
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }

        //V20210308 modify by penglin.sui for peng.zhao 只校验需要不良判定的数据采集项
        List<String> tagIdList = processNcInfo.getLineList().stream().map(HmeProcessNcLineVO2::getTagId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(tagIdList)){
            return null;
        }
        eoJobDataRecordList = eoJobDataRecordList.stream().filter(item -> tagIdList.contains(item.getTagId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eoJobDataRecordList)) {
            return null;
        }

        //分别筛选有优先级的(从小到大排序)和没有优先级的
        List<HmeProcessNcLineVO2> noPriorityLineList = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isBlank(item.getPriority())).collect(Collectors.toList());
        List<HmeProcessNcLineVO2> priorityLineList = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isNotBlank(item.getPriority()))
                .sorted(Comparator.comparing(HmeProcessNcLineVO2::getPriority)).collect(Collectors.toList());

        Map<String, HmeProcessNcLineVO2> noPriorityLineMap = new HashMap<String, HmeProcessNcLineVO2>();
        if (CollectionUtils.isNotEmpty(noPriorityLineList)) {
            noPriorityLineList.forEach(item -> noPriorityLineMap.put(item.getStandardCode(), item));
        }

        //如果没有优先级是1的就报错
        if (CollectionUtils.isEmpty(priorityLineList) || !"1".equals(priorityLineList.get(0).getPriority())) {
            //此SN【{$1}】查询不到判定标准，请检查！
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }
        List<HmeProcessNcDetailVO2> hmeProcessNcDetailVO2List = new ArrayList<>();
        for (HmeProcessNcLineVO2 processNcLine : priorityLineList) {
            //筛选当前行的数据项对应的采集项结果
            Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                    .filter(item -> StringUtils.equals(processNcLine.getTagId(), item.getTagId())).findAny();

            if (!currEoJobDataRecordOptional.isPresent()) {
                //判定标准【${1}】在数据项中不存在
                throwNoProcessNcMtException(tenantId, processNcLine.getTagId(), "HME_EO_JOB_SN_177");
            }

            //取到当前行对应的数据采集项
            HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

            //尝试获取数据采集项结果
            try {
                BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
            } catch (Exception e) {
                //数据采集项【{$1}】结果有误,请检查
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
            }
            BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

            //先寻找明细中符合结果值的范围
            Optional<HmeProcessNcDetailVO2> processNcDetailFind = processNcLine.getDetailList().stream()
                    .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                            && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

            //如果没有找到明细数据则报错
            if (!processNcDetailFind.isPresent()) {
                //数据项【{$1}】查询不到判定标准,请检查!
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
            }

            //获取找到的明细数据
            HmeProcessNcDetailVO2 processNcDetail = processNcDetailFind.get();

            //如果不良代码和标准编码均为空则报错
            /*if (StringUtils.isBlank(noPriorityProcessNcDetail.getNcCodeId())
                    && StringUtils.isBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
                //数据项【{$1}】查询不到判定标准,请检查!
                throwNoProcessNcMtException(tenantId, errorTag, "HME_EO_JOB_SN_170");
            }*/

            //如果不良代码不为空则返回报错消息
            if (StringUtils.isNotBlank(processNcDetail.getNcCodeId())) {
//                return returnSnProcessNc(tenantId, materialLotCode, processNcDetail);
                hmeProcessNcDetailVO2List.add(processNcDetail);
            }

            //如果是1级别走特殊逻辑,需要递归校验优先级为空的行数据
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
     * @Description 校验数据采集项不良判定-老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 13:46
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @param eoJobDataRecordList 数据采集项结果
     * @param processNcInfo 工序不良信息
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    @Override
    public HmeEoJobSn dataRecordAgeingProcessNcValidate(Long tenantId, String materialLotCode, List<HmeEoJobDataRecord> eoJobDataRecordList, HmeProcessNcHeaderVO2 processNcInfo) {
        //入参校验
        if (CollectionUtils.isEmpty(eoJobDataRecordList)
                || Objects.isNull(processNcInfo) || StringUtils.isBlank(processNcInfo.getHeaderId())) {
            return null;
        }

        //V20210308 modify by penglin.sui for peng.zhao 只校验需要不良判定的数据采集项
        List<String> tagIdList = processNcInfo.getLineList().stream().map(HmeProcessNcLineVO2::getTagId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(tagIdList)){
            return null;
        }
        eoJobDataRecordList = eoJobDataRecordList.stream().filter(item -> tagIdList.contains(item.getTagId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eoJobDataRecordList)) {
            return null;
        }

        if (CollectionUtils.isEmpty(processNcInfo.getLineList())) {
            //此SN【{$1}】查询不到判定标准，请检查！
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }

        //校验是否有未维护优先级的行数据
        Optional<HmeProcessNcLineVO2> noPriorityLineOptional = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isBlank(item.getPriority())).findAny();
        if (noPriorityLineOptional.isPresent()) {
            HmeProcessNcLineVO2 noProcessNcLine = noPriorityLineOptional.get();
            //数据项【${1}】未定义优先级,请检查!
            throwNoProcessNcMtException(tenantId, noProcessNcLine.getTagId(), "HME_EO_JOB_SN_182");
        }

        //校验优先级是否相同
        long priorityCount = processNcInfo.getLineList().stream().map(HmeProcessNcLineVO2::getPriority).distinct().count();
        if (priorityCount != processNcInfo.getLineList().size()) {
            //存在多条同一优先级的数据项，请检查！
            throw new MtException("HME_EO_JOB_SN_183", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_183", "HME"));
        }

        //从小到大排序行数据
        List<HmeProcessNcLineVO2> priorityLineList = processNcInfo.getLineList().stream()
                .sorted(Comparator.comparing(HmeProcessNcLineVO2::getPriority)).collect(Collectors.toList());
        List<HmeProcessNcDetailVO2> hmeProcessNcDetailVO2List = new ArrayList<>();
        for (HmeProcessNcLineVO2 processNcLine : priorityLineList) {
            if (CollectionUtils.isEmpty(processNcLine.getDetailList())) {
                //数据项【${1}】未定义不良标准,请检查!
                throwNoProcessNcMtException(tenantId, processNcLine.getTagId(), "HME_EO_JOB_SN_184");
            }

            //筛选当前行的数据项对应的采集项结果
            Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                    .filter(item -> StringUtils.equals(processNcLine.getTagId(), item.getTagId())).findAny();

            if (!currEoJobDataRecordOptional.isPresent()) {
                //判定标准【${1}】在数据项中不存在
                throwNoProcessNcMtException(tenantId, processNcLine.getTagId(), "HME_EO_JOB_SN_177");
            }

            //取到当前行对应的数据采集项
            HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

            //尝试获取数据采集项结果
            try {
                BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
            } catch (Exception e) {
                //数据采集项【{$1}】结果有误,请检查
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
            }
            BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

            //先寻找明细中符合结果值的范围
            Optional<HmeProcessNcDetailVO2> processNcDetailFind = processNcLine.getDetailList().stream()
                    .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                            && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

            //如果没有找到明细数据则报错
            if (!processNcDetailFind.isPresent()) {
                //数据项【{$1}】查询不到判定标准,请检查!
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
            }

            //获取找到的明细数据
            HmeProcessNcDetailVO2 processNcDetail = processNcDetailFind.get();

            //如果不良代码不为空则返回报错消息
            //V20210519 modify by penglin.sui for peng.zhao 全部优先级的所有标准进行判定
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
     * @Description 根据工艺判断是器件不良还是老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 10:12
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return boolean true:老化不良 false:器件不良
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
                // 下达 运行中 则为新条码 判断扩展字段是否存在 找不到旧条码 则报错
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
        //入参校验
        if (CollectionUtils.isEmpty(eoJobDataRecordList)
                || Objects.isNull(processNcInfo) || StringUtils.isBlank(processNcInfo.getHeaderId())) {
            return null;
        }
        if (CollectionUtils.isEmpty(processNcInfo.getLineList())) {
            //此SN【{$1}】查询不到判定标准，请检查！
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }

        //V20210308 modify by penglin.sui for peng.zhao 只校验需要不良判定的数据采集项
        List<String> tagIdList = processNcInfo.getLineList().stream().map(HmeProcessNcLineVO2::getTagId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(tagIdList)){
            return null;
        }
        eoJobDataRecordList = eoJobDataRecordList.stream().filter(item -> tagIdList.contains(item.getTagId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eoJobDataRecordList)) {
            return null;
        }

        //分别筛选有优先级的(从小到大排序)和没有优先级的
        List<HmeProcessNcLineVO2> noPriorityLineList = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isBlank(item.getPriority())).collect(Collectors.toList());
        List<HmeProcessNcLineVO2> priorityLineList = processNcInfo.getLineList().stream()
                .filter(item -> StringUtils.isNotBlank(item.getPriority()))
                .sorted(Comparator.comparing(HmeProcessNcLineVO2::getPriority)).collect(Collectors.toList());

        Map<String, HmeProcessNcLineVO2> noPriorityLineMap = new HashMap<String, HmeProcessNcLineVO2>();
        if (CollectionUtils.isNotEmpty(noPriorityLineList)) {
            noPriorityLineList.forEach(item -> noPriorityLineMap.put(item.getStandardCode(), item));
        }

        //如果没有优先级是1的就报错
        if (CollectionUtils.isEmpty(priorityLineList) || !"1".equals(priorityLineList.get(0).getPriority())) {
            //此SN【{$1}】查询不到判定标准，请检查！
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_169", "HME", materialLotCode));
        }
        List<HmeProcessNcDetailVO2> hmeProcessNcDetailVO2List = new ArrayList<>();
        for (HmeProcessNcLineVO2 processNcLine : priorityLineList) {
            //筛选当前行的数据项对应的采集项结果
            Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                    .filter(item -> StringUtils.equals(processNcLine.getTagId(), item.getTagId())).findAny();

            if (!currEoJobDataRecordOptional.isPresent()) {
                //判定标准【${1}】在数据项中不存在
                throwNoProcessNcMtException(tenantId, processNcLine.getTagId(), "HME_EO_JOB_SN_177");
            }

            //取到当前行对应的数据采集项
            HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

            //尝试获取数据采集项结果
            try {
                BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
            } catch (Exception e) {
                //数据采集项【{$1}】结果有误,请检查
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
            }
            BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

            //先寻找明细中符合结果值的范围
            Optional<HmeProcessNcDetailVO2> processNcDetailFind = processNcLine.getDetailList().stream()
                    .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                            && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

            //如果没有找到明细数据则报错
            if (!processNcDetailFind.isPresent()) {
                //数据项【{$1}】查询不到判定标准,请检查!
                throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
            }

            //获取找到的明细数据
            HmeProcessNcDetailVO2 processNcDetail = processNcDetailFind.get();

            //如果不良代码不为空则返回报错消息
            if (StringUtils.isNotBlank(processNcDetail.getNcCodeId())) {
                processNcDetail.setTagId(currEoJobDataRecord.getTagId());
                hmeProcessNcDetailVO2List.add(processNcDetail);
            }

            //如果是1级别走特殊逻辑,需要递归校验优先级为空的行数据
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
            // 根据工位找对应的工序
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
            // 查询 不是例外放行 在此工序拦截的拦截单
            List<HmeEoJobSnCommonVO> interceptNumList = hmeEoJobSnCommonMapper.queryInterceptNumList(tenantId, processId, materialLotIdList);
            // 判断这些拦截单 的拦截对象是否包含了进站sn
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
     * 校验实验代码
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
     * 校验SN
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
     * 校验工单
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
     * 校验批次
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
            // 根据SN 获取投料条码的批次
            List<String> eoIdList = mtMaterialLotList.stream().map(MtMaterialLot::getEoId).collect(Collectors.toList());
            // 查询SN序列投料和时效批次投料记录表 找到SN 投料条码
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
     * 投料条码
     *
     * @param tenantId
     * @param eoIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/9 17:31
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnCommonVO2>
     */
    private List<HmeEoJobSnCommonVO2> queryFeedMaterialLotCodeList (Long tenantId, List<String> eoIdList) {
        List<HmeEoJobSnCommonVO2> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoIdList)) {
            // 查询SN序列投料条码信息
            List<HmeEoJobSnCommonVO2> snFeedMaterialLotList = hmeEoJobSnCommonMapper.querySnFeedMaterialLotCodeList(tenantId, eoIdList);
            if (CollectionUtils.isNotEmpty(snFeedMaterialLotList)) {
                resultList.addAll(snFeedMaterialLotList);
            }
            // 查询eo进站jobs 根据jobs找出批次时效投料条码
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
     * 校验供应商批次
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
     * @Description 递归校验优先级为空的行数据
     *
     * @author yuchao.wang
     * @date 2021/1/25 11:23
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @param eoJobDataRecordList 数据采集项
     * @param processNcDetail 上层行对应的明细
     * @param currNoPriorityLineMap 当前数据采集项对应的么有优先级的不良判定行数据
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn dataRecordNoPriorityRecursionValidate(Long tenantId,
                                                             String materialLotCode,
                                                             List<HmeEoJobDataRecord> eoJobDataRecordList,
                                                             HmeProcessNcDetailVO2 processNcDetail,
                                                             Map<String, HmeProcessNcLineVO2> currNoPriorityLineMap) {
        //如果没有找到对应行数据则报错
        if (!currNoPriorityLineMap.containsKey(processNcDetail.getDetailStandardCode())) {
            //标准编码【${1}】对应的行数据项不存在,请检查!
            throw new MtException("HME_EO_JOB_SN_178", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_178", "HME", processNcDetail.getDetailStandardCode()));
        }

        //取出当前标准编码对应的行数据
        HmeProcessNcLineVO2 noPriorityProcessNcLine = currNoPriorityLineMap.get(processNcDetail.getDetailStandardCode());

        //筛选当前行的数据项对应的采集项结果
        Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                .filter(item -> StringUtils.equals(noPriorityProcessNcLine.getTagId(), item.getTagId())).findAny();

        if (!currEoJobDataRecordOptional.isPresent()) {
            //判定标准【${1}】在数据项中不存在
            throwNoProcessNcMtException(tenantId, noPriorityProcessNcLine.getTagId(), "HME_EO_JOB_SN_177");
        }

        //取到当前行对应的数据采集项
        HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

        //尝试获取数据采集项结果
        try {
            BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
        } catch (Exception e) {
            //数据采集项【{$1}】结果有误,请检查
            throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
        }
        BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

        //先寻找明细中符合结果值的范围
        Optional<HmeProcessNcDetailVO2> noPriorityProcessNcDetailFind = noPriorityProcessNcLine.getDetailList().stream()
                .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                        && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

        //如果没有找到明细数据则报错
        if (!noPriorityProcessNcDetailFind.isPresent()) {
            //数据项【{$1}】查询不到判定标准,请检查!
            throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
        }

        //获取找到的明细数据
        HmeProcessNcDetailVO2 noPriorityProcessNcDetail = noPriorityProcessNcDetailFind.get();

        //如果不良代码和标准编码均为空则报错
        /*if (StringUtils.isBlank(noPriorityProcessNcDetail.getNcCodeId())
                && StringUtils.isBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
            //数据项【{$1}】查询不到判定标准,请检查!
            throwNoProcessNcMtException(tenantId, errorTag, "HME_EO_JOB_SN_170");
        }*/

        //如果不良代码不为空则返回报错消息
        if (StringUtils.isNotBlank(noPriorityProcessNcDetail.getNcCodeId())) {
            return returnSnProcessNc(tenantId, materialLotCode, noPriorityProcessNcDetail);
        }

        //如果标准编码不为空,就继续递归校验优先级为空的行数据
        if (StringUtils.isNotBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
            HmeEoJobSn validateResult = dataRecordNoPriorityRecursionValidate(tenantId, materialLotCode,
                    eoJobDataRecordList, noPriorityProcessNcDetail, currNoPriorityLineMap);
            if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
                //V20210409 modify by penglin.sui for hui.zhao 新增工序不良参数返回
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
     * @Description 递归校验优先级为空的行数据
     *
     * @author penglin.sui
     * @date 2021/5/19 14:47
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @param eoJobDataRecordList 数据采集项
     * @param processNcDetail 上层行对应的明细
     * @param currNoPriorityLineMap 当前数据采集项对应的么有优先级的不良判定行数据
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcDetailVO2>
     *
     */
    private List<HmeProcessNcDetailVO2> dataRecordNoPriorityRecursionValidate2(Long tenantId,
                                                                               String materialLotCode,
                                                                               List<HmeEoJobDataRecord> eoJobDataRecordList,
                                                                               HmeProcessNcDetailVO2 processNcDetail,
                                                                               Map<String, HmeProcessNcLineVO2> currNoPriorityLineMap) {
        //如果没有找到对应行数据则报错
        if (!currNoPriorityLineMap.containsKey(processNcDetail.getDetailStandardCode())) {
            //标准编码【${1}】对应的行数据项不存在,请检查!
            throw new MtException("HME_EO_JOB_SN_178", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_178", "HME", processNcDetail.getDetailStandardCode()));
        }

        //取出当前标准编码对应的行数据
        HmeProcessNcLineVO2 noPriorityProcessNcLine = currNoPriorityLineMap.get(processNcDetail.getDetailStandardCode());

        //筛选当前行的数据项对应的采集项结果
        Optional<HmeEoJobDataRecord> currEoJobDataRecordOptional = eoJobDataRecordList.stream()
                .filter(item -> StringUtils.equals(noPriorityProcessNcLine.getTagId(), item.getTagId())).findAny();

        if (!currEoJobDataRecordOptional.isPresent()) {
            //判定标准【${1}】在数据项中不存在
            throwNoProcessNcMtException(tenantId, noPriorityProcessNcLine.getTagId(), "HME_EO_JOB_SN_177");
        }

        //取到当前行对应的数据采集项
        HmeEoJobDataRecord currEoJobDataRecord = currEoJobDataRecordOptional.get();

        //尝试获取数据采集项结果
        try {
            BigDecimal tryResult = new BigDecimal(currEoJobDataRecord.getResult());
        } catch (Exception e) {
            //数据采集项【{$1}】结果有误,请检查
            throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_168");
        }
        BigDecimal result = new BigDecimal(currEoJobDataRecord.getResult());

        //先寻找明细中符合结果值的范围
        Optional<HmeProcessNcDetailVO2> noPriorityProcessNcDetailFind = noPriorityProcessNcLine.getDetailList().stream()
                .filter(item -> (Objects.isNull(item.getMinValue()) || result.compareTo(item.getMinValue()) >= 0)
                        && (Objects.isNull(item.getMaxValue()) || result.compareTo(item.getMaxValue()) < 0)).findAny();

        //如果没有找到明细数据则报错
        if (!noPriorityProcessNcDetailFind.isPresent()) {
            //数据项【{$1}】查询不到判定标准,请检查!
            throwNoProcessNcMtException(tenantId, currEoJobDataRecord.getTagId(), "HME_EO_JOB_SN_170");
        }

        //获取找到的明细数据
        HmeProcessNcDetailVO2 noPriorityProcessNcDetail = noPriorityProcessNcDetailFind.get();

        //如果不良代码和标准编码均为空则报错
        /*if (StringUtils.isBlank(noPriorityProcessNcDetail.getNcCodeId())
                && StringUtils.isBlank(noPriorityProcessNcDetail.getDetailStandardCode())) {
            //数据项【{$1}】查询不到判定标准,请检查!
            throwNoProcessNcMtException(tenantId, errorTag, "HME_EO_JOB_SN_170");
        }*/
        List<HmeProcessNcDetailVO2> resultList = new ArrayList<>();
        //如果不良代码不为空则返回报错消息
        if (StringUtils.isNotBlank(noPriorityProcessNcDetail.getNcCodeId())) {
            noPriorityProcessNcDetail.setTagId(currEoJobDataRecord.getTagId());
            resultList.add(noPriorityProcessNcDetail);
        }

        //如果标准编码不为空,就继续递归校验优先级为空的行数据
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
     * @Description 返回"器件已判定为不良"的消息
     *
     * @author yuchao.wang
     * @date 2021/1/24 16:33
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @param processNcDetail 不良明细信息
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn returnSnProcessNc(Long tenantId, String materialLotCode, HmeProcessNcDetailVO2 processNcDetail) {
        if (StringUtils.isBlank(processNcDetail.getNcCode())) {
            //不良代码【${1}】不存在,请检查!
            throw new MtException("HME_COS_RETEST_IMPORT_009", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_RETEST_IMPORT_009", "HME", processNcDetail.getNcCodeId()));
        }

        //器件【{$1}】已判定为不良,对应不良代码为【{$2}】
        HmeEoJobSn resultJobSn = new HmeEoJobSn();
        resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
        resultJobSn.setErrorMessage(mtErrorMessageRepository
                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_172", "HME", materialLotCode, processNcDetail.getNcCode(), processNcDetail.getNcDescription()));
        //新增工序不良参数返回
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
     * 返回"反射镜已判定为不良路数"的消息
     *
     * @param tenantId
     * @param materialLotCode
     * @param processNcDetailList
     * @author sanfeng.zhang@hand-china.com 2021/9/7 9:11
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     */
    private HmeEoJobSn returnSnMultipleReflectorProcessNc(Long tenantId, String materialLotCode, List<HmeProcessNcDetailVO2> processNcDetailList) {
        HmeEoJobSn resultJobSn = new HmeEoJobSn();
        // 反射镜不良提示不良路数 根据数据项取出路数
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
                //不良代码【${1}】不存在,请检查!
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
                        sb.append("【" + split[2] + "】");
                    } else {
                        // 已存在则不拼接
                        if (!sb.toString().contains(split[2])) {
                            sb.append("；【" + split[2] + "】");
                        }
                    }
                    processNcDetail.setCosCode(split[2]);
                    processNcDetail.setSpliceCosCode("【" + split[2] + "】");
                } else {
                    aceSb.append(tagCode);
                }
            }
            //新增工序不良参数返回
            HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO = new HmeNcDisposePlatformDTO11();
            hmeNcDisposePlatformDTO.setMaterialLotCode(materialLotCode);
            hmeNcDisposePlatformDTO.setNcGroupId(processNcDetail.getNcGroupId());
            List<String> ncCodeIdList = new ArrayList<>();
            ncCodeIdList.add(processNcDetail.getNcCodeId());
            hmeNcDisposePlatformDTO.setNcCodeIdList(ncCodeIdList);
            resultJobSn.setHmeNcDisposePlatformDTO(hmeNcDisposePlatformDTO);
        }
        if (sb.length() > 0) {
            // 进行排序 -> COS01 COS02 ...
            List<String> spliceCosCodeList = processNcDetailList.stream().filter(pnc -> StringUtils.isNotBlank(pnc.getCosCode()))
                    .sorted(Comparator.comparing(HmeProcessNcDetailVO2::getCosCode))
                    .map(HmeProcessNcDetailVO2::getSpliceCosCode).distinct().collect(Collectors.toList());
            String spliceCosCode = StringUtils.join(spliceCosCodeList, ";");
            if (aceSb.length() > 0) {
                // 器件【${1}】已判定为平均效率不良,且存在单路不良,路数为【${2}】
                resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
                resultJobSn.setErrorMessage(mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_243 ", "HME", materialLotCode, spliceCosCode));
                resultJobSn.setProcessNcDetailList(processNcDetailList);
            } else {
                // 器件【${1}】已判定为耦合效率不良,不良路数为【 ${2}】
                resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
                resultJobSn.setErrorMessage(mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_221 ", "HME", materialLotCode, spliceCosCode));
                resultJobSn.setProcessNcDetailList(processNcDetailList);
            }
        } else if (aceSb.length() > 0){
            // 只有平均效率
            // 器件【${1}】已判定为平均效率不良
            resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
            resultJobSn.setErrorMessage(mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_242 ", "HME", materialLotCode));
            resultJobSn.setProcessNcDetailList(processNcDetailList);
        }
        return resultJobSn;
    }
    /**
     *
     * @Description 返回"器件已判定为不良"的消息
     *
     * @author penglin.sui
     * @date 2021/5/19 14:11
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @param processNcDetailList 不良明细信息列表
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn returnSnMultipleProcessNc(Long tenantId, String materialLotCode, List<HmeProcessNcDetailVO2> processNcDetailList) {
        HmeEoJobSn resultJobSn = new HmeEoJobSn();
        StringBuilder stringBuilder = new StringBuilder();
        for (HmeProcessNcDetailVO2 processNcDetail : processNcDetailList) {
            if (StringUtils.isBlank(processNcDetail.getNcCode())) {
                //不良代码【${1}】不存在,请检查!
                throw new MtException("HME_COS_RETEST_IMPORT_009", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_RETEST_IMPORT_009", "HME", processNcDetail.getNcCodeId()));
            }

            if(stringBuilder.length() == 0){
                stringBuilder.append("【" + processNcDetail.getNcCode() + "|" + processNcDetail.getNcDescription() + "】");
            }else{
                stringBuilder.append("；【" + processNcDetail.getNcCode() + "|" + processNcDetail.getNcDescription() + "】");
            }

            //新增工序不良参数返回
            HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO = new HmeNcDisposePlatformDTO11();
            hmeNcDisposePlatformDTO.setMaterialLotCode(materialLotCode);
            hmeNcDisposePlatformDTO.setNcGroupId(processNcDetail.getNcGroupId());
            List<String> ncCodeIdList = new ArrayList<>();
            ncCodeIdList.add(processNcDetail.getNcCodeId());
            hmeNcDisposePlatformDTO.setNcCodeIdList(ncCodeIdList);
            resultJobSn.setHmeNcDisposePlatformDTO(hmeNcDisposePlatformDTO);
        }
        if(stringBuilder.length() > 0) {
            //器件【${1}】已判定为不良,对应不良码为 ${2}
            resultJobSn.setErrorCode("HME_EO_JOB_SN_172");
            resultJobSn.setErrorMessage(mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_172", "HME", materialLotCode, stringBuilder.toString()));
            resultJobSn.setProcessNcDetailList(processNcDetailList);
        }

        return resultJobSn;
    }

    /**
     *
     * @Description 抛出数据项相关异常
     *
     * @author yuchao.wang
     * @date 2021/1/24 16:27
     * @param tenantId 租户ID
     * @param errorTagId 数据项ID
     * @param errorCode 报错编码
     * @return void
     *
     */
    private void throwNoProcessNcMtException(Long tenantId, String errorTagId, String errorCode) {
        String errorTag = errorTagId;
        if (StringUtils.isNotBlank(errorTagId)) {
            MtTag mtTag = mtTagRepository.tagGet(tenantId, errorTagId);
            if (Objects.isNull(mtTag) || StringUtils.isBlank(mtTag.getTagId())) {
                //数据项【${1}】不存在,请检查!
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
     * @Description 初始化作业物料信息-单件作业平台
     *
     * @author yuchao.wang
     * @date 2020/12/31 15:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @param bomId BOM ID
     * @return void
     *
     */
    private void materialInSiteForSingle(Long tenantId, HmeEoJobSnVO2 dto, String bomId) {
        //查询所有组件清单
        List<HmeBomComponentVO2> allBomComponentList = hmeEoJobSnMapper.queryEoComponentForMaterialInSite(tenantId, dto.getSiteId(), bomId);
        if (CollectionUtils.isEmpty(allBomComponentList)) {
            return;
        }

        //排除 反冲料、虚拟件组件
        List<HmeBomComponentVO2> bomComponentList = allBomComponentList.stream().filter(item -> !"2".equals(item.getBackFlashFlag())
                && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualFlag())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bomComponentList)) {
            return;
        }

        //校验
        Optional<HmeBomComponentVO2> find = bomComponentList.stream().filter(item ->
                StringUtils.isBlank(item.getMaterialSiteId()) || StringUtils.isBlank(item.getMaterialCode())
                        || (HmeConstants.MaterialTypeCode.TIME.equals(item.getLotType()) && StringUtils.isBlank(item.getAvailableTime()))
        ).findAny();
        if (find.isPresent()) {
            HmeBomComponentVO2 findComponent = find.get();
            //校验是否查询到物料站点信息
            if (StringUtils.isBlank(findComponent.getMaterialSiteId())) {
                //未找到匹配的物料站点信息
                throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
            }

            //校验物料是否存在
            if (StringUtils.isBlank(findComponent.getMaterialCode())) {
                //${1}不存在 请确认${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "物料", findComponent.getMaterialId()));
            }

            //校验时效物料的时效时长必须有值
            if (HmeConstants.MaterialTypeCode.TIME.equals(findComponent.getLotType()) && StringUtils.isBlank(findComponent.getAvailableTime())) {
                //时效物料【${1}】没有维护开封有效期
                throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_070", "HME", findComponent.getMaterialCode()));
            }
        }

        //查询值集HME.EO_JOB_SN_UOM
        List<String> typeLovValueList = new ArrayList<>();
        List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.EO_JOB_SN_UOM", tenantId);
        if (CollectionUtils.isNotEmpty(typeLov)) {
            typeLovValueList = typeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }

        //循环所有组件清单初始化投料数据
        for (HmeBomComponentVO2 bomComponent : bomComponentList) {
            //查询不到物料类型，默认作为批次类型处理
            String lotType = StringUtils.isBlank(bomComponent.getLotType()) ? HmeConstants.MaterialTypeCode.LOT : bomComponent.getLotType();
            BigDecimal componentQty = new BigDecimal(String.valueOf(bomComponent.getQty()));

            //根据物料类型采用不同初始化方式
            if (HmeConstants.MaterialTypeCode.SN.equals(lotType)) {
                //序列号物料拆分限制单位类型为计数单位
                boolean isSplitLine = typeLovValueList.contains(bomComponent.getUomType());

                //初始化序列物料
                hmeEoJobMaterialRepository.initJobMaterialWithoutQuery(tenantId, bomComponent.getMaterialId(),
                        isSplitLine, componentQty, bomComponent.getBomComponentId(), dto);
            } else if (HmeConstants.MaterialTypeCode.LOT.equals(lotType)) {
                //初始化批次物料
                hmeEoJobLotMaterialRepository.initLotMaterialWithoutQuery(tenantId, bomComponent.getMaterialId(), componentQty, dto);
            } else if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())
                    && HmeConstants.MaterialTypeCode.TIME.equals(lotType)) {
                //初始化时效物料
                hmeEoJobTimeMaterialRepository.initTimeMaterialWithoutQuery(tenantId, bomComponent.getMaterialId(),
                        bomComponent.getAvailableTime(), componentQty, dto);
            }
        }
    }

    /**
     *
     * @Description 查询工艺扩展属性BOM_FLAG
     *
     * @author yuchao.wang
     * @date 2021/1/29 10:46
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return boolean BOM_FLAG=Y:true 否则:false
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
     * @Description 获取联产品行号
     *
     * @author yuchao.wang
     * @date 2020/11/6 15:29
     * @param tenantId 租户ID
     * @param eoMap eo
     * @param woMap wo
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    private Map<String, String> getRelatedLineNum(Long tenantId, Map<String, HmeEoVO4> eoMap, Map<String, HmeWorkOrderVO2> woMap) {
        Map<String, String> relatedLineNumMap = new HashMap<>();
        //筛选降级品，查询attribute30
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
     * @Description 获取MES_RK06对应的内部订单号或者SAP号 key:woId-snNum value:内部订单号或者SAP号
     *
     * @author yuchao.wang
     * @date 2020/11/6 14:07
     * @param tenantId 租户ID
     * @param eoMap eo
     * @param woMap wo
     * @param snLineList 条码信息
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    private Map<String, HmeServiceSplitRecordVO2> getRk06InternalOrderNum(Long tenantId,
                                                                          Map<String, HmeEoVO4> eoMap,
                                                                          Map<String, HmeWorkOrderVO2> woMap,
                                                                          List<HmeEoJobSnVO3> snLineList) {
        Map<String, HmeServiceSplitRecordVO2> internalOrderNumMap = new HashMap<>();

        //判断订单类型是否有MES_RK06
        List<HmeWorkOrderVO2> rk06WoList = woMap.values().stream().filter(item -> "MES_RK06".equals(item.getWorkOrderType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(rk06WoList)) {
            //筛选出rk06对应的条码
            List<String> rk06WoIdList = rk06WoList.stream().map(HmeWorkOrderVO2::getWorkOrderId).collect(Collectors.toList());
            List<String> rk06SnList = snLineList.stream()
                    .filter(item -> rk06WoIdList.contains(eoMap.get(item.getEoId()).getWorkOrderId()))
                    .map(HmeEoJobSnVO3::getSnNum).collect(Collectors.toList());

            //批量查询所有内部单号/SAP单号
            List<HmeServiceSplitRecordVO2> serviceSplitRecordVO2List = hmeServiceSplitRecordMapper.batchQueryWoNumBySnNumAndWoId(tenantId, rk06WoIdList, rk06SnList);
            if (CollectionUtils.isEmpty(serviceSplitRecordVO2List)) {
                //工单未查询到拆机记录
                throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_084", "HME"));
            }

            //按照工单+条码分组
            Map<String, List<HmeServiceSplitRecordVO2>> splitRecordMap = serviceSplitRecordVO2List
                    .stream().collect(Collectors.groupingBy(item -> item.getWorkOrderId() + "," + item.getSnNum()));

            //校验分组后的大小与woId大小一致
            if (Objects.isNull(splitRecordMap) || splitRecordMap.size() != rk06WoIdList.size()) {
                //工单未查询到拆机记录
                throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_084", "HME"));
            }

            //取内部单号/SAP单号
            for (Map.Entry<String, List<HmeServiceSplitRecordVO2>> entry : splitRecordMap.entrySet()) {
                if (CollectionUtils.isEmpty(entry.getValue())) {
                    throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_084", "HME"));
                }

                //按照创建时间倒序排序,取时间最近的
                List<HmeServiceSplitRecordVO2> values = entry.getValue().stream().sorted(Comparator
                        .comparing(HmeServiceSplitRecordVO2::getCreationDate, Comparator.reverseOrder())).collect(Collectors.toList());

                //如果内部订单号不为空则取内部订单号，为空取SAP单号
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
                    //工单未查询到内部订单号及维修工单号
                    throw new MtException("HME_EO_JOB_SN_085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_085", "HME"));
                }
            }
        }
        return internalOrderNumMap;
    }

    /**
     *
     * @Description 获取事务移动类型
     *
     * @author yuchao.wang
     * @date 2020/11/6 11:00
     * @param tenantId 租户ID
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
     * @Description 获取事务API参数
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

        //增加批次ID
        String batchId = Utils.getBatchId();

        //构造完工事务
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

        //判断订单类型是否为MES_RK06 是则不计报工事务 modify by yuchao.wang for jiao.chen at 2020.9.28
        if ("MES_RK06".equals(wo.getWorkOrderType())) {
            //售后需要替换工单号为内部订单号或SAP单号
            HmeServiceSplitRecordVO2 orderNum = hmeEoJobSnSingleBasic.getRk06InternalOrderNum();
            if ("INTERNAL_ORDER".equals(orderNum.getOrderNumType())) {
                //找到内部订单号则为客户机,更改事务类型
                woCompleteTransactionRequestVO.setTransactionTypeCode("WMS_INSDID_ORDER_S_R");
            }
            woCompleteTransactionRequestVO.setWorkOrderNum(orderNum.getOrderNum());
            woCompleteTransactionRequestVO.setMoveType(moveTypeMap.get(woCompleteTransactionRequestVO.getTransactionTypeCode()));
        } else {
            //构造报工事务
            WmsObjectTransactionRequestVO woReportTransactionRequestVO = new WmsObjectTransactionRequestVO();
            BeanUtils.copyProperties(woCompleteTransactionRequestVO, woReportTransactionRequestVO);
            woReportTransactionRequestVO.setTransactionTypeCode("HME_WORK_REPORT");
            woReportTransactionRequestVO.setMoveType(moveTypeMap.get(woReportTransactionRequestVO.getTransactionTypeCode()));

            woCompleteTransactionRequestVO.setMoveType(moveTypeMap.get(woCompleteTransactionRequestVO.getTransactionTypeCode()));
            objectTransactionRequestList.add(woReportTransactionRequestVO);
        }

        //101移动类型,EO和WO物料不一致时 增加特殊逻辑 add by yuchao.wang for tianyang.xie at 2020.11.04
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
     * @Description 获取事务API参数
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
        //构造完工事务
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

        //判断订单类型是否为MES_RK06 是则不计报工事务 modify by yuchao.wang for jiao.chen at 2020.9.28
        if ("MES_RK06".equals(hmeWo.getWorkOrderType())) {
            //售后需要替换工单号为内部订单号或SAP单号
            HmeServiceSplitRecordVO2 orderNum = internalOrderNumMap.get(hmeWo.getWorkOrderId() + "," + jobSn.getSnNum());
            if (Objects.isNull(orderNum) || StringUtils.isBlank(orderNum.getOrderNum())) {
                //工单未查询到内部订单号及维修工单号
                throw new MtException("HME_EO_JOB_SN_085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_085", "HME"));
            }
            if ("INTERNAL_ORDER".equals(orderNum.getOrderNumType())) {
                //找到内部订单号则为客户机,更改事务类型
                woCompleteTransactionRequestVO.setTransactionTypeCode("WMS_INSDID_ORDER_S_R");
            }
            woCompleteTransactionRequestVO.setWorkOrderNum(orderNum.getOrderNum());
            woCompleteTransactionRequestVO.setMoveType(moveTypeMap.get(woCompleteTransactionRequestVO.getTransactionTypeCode()));
        } else {
            //构造报工事务
            WmsObjectTransactionRequestVO woReportTransactionRequestVO = new WmsObjectTransactionRequestVO();
            BeanUtils.copyProperties(woCompleteTransactionRequestVO, woReportTransactionRequestVO);
            woReportTransactionRequestVO.setTransactionTypeCode("HME_WORK_REPORT");
            woReportTransactionRequestVO.setMoveType(moveTypeMap.get(woReportTransactionRequestVO.getTransactionTypeCode()));

            woCompleteTransactionRequestVO.setMoveType(moveTypeMap.get(woCompleteTransactionRequestVO.getTransactionTypeCode()));
            objectTransactionRequestList.add(woReportTransactionRequestVO);
        }

        //101移动类型,EO和WO物料不一致时 增加特殊逻辑 add by yuchao.wang for tianyang.xie at 2020.11.04
        if ("101".equals(woCompleteTransactionRequestVO.getMoveType())
                && !hmeEo.getMaterialId().equals(hmeWo.getMaterialId())) {
            woCompleteTransactionRequestVO.setAttribute30(relatedLineNumMap.get(hmeEo.getEoId() + "-" + hmeEo.getMaterialId()));
        }

        objectTransactionRequestList.add(woCompleteTransactionRequestVO);
        return objectTransactionRequestList;
    }

    /**
     * 客户机内部订单不存在整机产出，因此单独进行内部订单调拨接口回传不走完工报工
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
        // 获取客户已修仓货位、在线仓货位 都不存在 则报错
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
        // 已修仓货位
        String repairLocatorCode = repairLocatorOpt.get().getMeaning();

        MtModLocator repairLocator = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setTenantId(tenantId);
            setLocatorCode(repairLocatorCode);
        }});
        if (Objects.isNull(repairLocator)) {
            throw new MtException("HME_REPAIR_SN_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0005", "HME", repairLocatorCode));
        }
        // 已修仓货位对应的仓库
        MtModLocator repairWareHouse = mtModLocatorRepository.selectByPrimaryKey(repairLocator.getParentLocatorId());
        if (Objects.isNull(repairWareHouse)) {
            throw new MtException("HME_REPAIR_SN_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0006", "HME", repairLocatorCode));
        }

        //创建事件
        String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventTypeCode("EO_WKC_STEP_COMPLETE");
            }
        });

        // 创建内部订单完成事件
        String internalOrderCompleteEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("INTERNAL_ORDER_COMPLETE");
        }});

        //V20210302 modify by penglin.sui for fang.pan 返修工序作业平台更新售后返品拆机
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getReworkProcessFlag())) {
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getMaterialLot().getAfFlag())) {
                HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryLastSplitRecordOfMaterialLot2(tenantId, hmeEoJobSnSingleBasic.getMaterialLot().getMaterialLotId());
                if (Objects.nonNull(hmeServiceSplitRecord)) {
                    //当前时间
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

                        //记录历史
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
        //构造条码批量更新参数-EO_WKC_STEP_COMPLETE事件
        MtMaterialLotVO2 mtLotCompleteUpdate = new MtMaterialLotVO2();
        mtLotCompleteUpdate.setEventId(internalOrderCompleteEventId);
        mtLotCompleteUpdate.setMaterialLotId(dto.getMaterialLotId());
        mtLotCompleteUpdate.setLocatorId(repairLocator.getLocatorId());
        mtLotCompleteUpdate.setEnableFlag(HmeConstants.ConstantValue.NO);
        mtLotCompleteUpdate.setLot(StringUtils.trimToEmpty(hmeEoJobSnSingleBasic.getLotCode()));
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
            //V20210312 modify by penglin.sui for tianyang.xie 存在数据项不良，置条码质量状态为NG
            if (CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getNgDataRecordList())) {
                List<HmeEoJobDataRecordVO2> ngDataRecordList = hmeEoJobSnSingleBasic.getNgDataRecordList().stream().filter(item -> item.getJobId().equals(dto.getJobId()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ngDataRecordList)) {
                    mtLotCompleteUpdate.setQualityStatus(HmeConstants.ConstantValue.NG);
                }
            }
        }
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotCompleteUpdate, HmeConstants.ConstantValue.NO);

        //构造条码扩展属性批量更新参数
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
        //V20210525 modify by penglin.sui for fang.pan 末道序完工时，如果条码版本扩展字段有值，则不更新版本
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
        //工艺为空，不校验
        if(StringUtils.isBlank(operationId)){
            return;
        }

        //查询工艺扩展属性-BIND_FLAG
        List<MtExtendAttrVO1> operationAttrList = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_operation_attr", Collections.singletonList(operationId), "BIND_FLAG"));
        if(CollectionUtils.isEmpty(operationAttrList)){
            return;
        }

        if(!HmeConstants.ConstantValue.YES.equals(operationAttrList.get(0).getAttrValue())){
            //不为Y不进行校验
            return;
        }

        //根据工位查询设备
        List<String> wkcEquipmentList = hmeEquipmentMapper.queryEquipmentOfWkc(tenantId , workcellId);

        List<String> opOrWkcEquipmentList = new ArrayList<>();

        //查询工位扩展属性-EQ_CATEGORY
        List<MtExtendAttrVO1> wkcAttrList = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_mod_workcell_attr", Collections.singletonList(workcellId), "EQ_CATEGORY"));
        if(CollectionUtils.isEmpty(wkcAttrList) ||
                (CollectionUtils.isNotEmpty(wkcAttrList) && StringUtils.isBlank(wkcAttrList.get(0).getAttrValue()))){
            // 工位扩展属性没有值，查询工艺设备关系中的设备
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

            //【${1}】类设备未绑定工位,不允许进站!
            throw new MtException("HME_EO_JOB_SN_199", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_199", "HME" , equipments.toString()));
        }
    }
}
