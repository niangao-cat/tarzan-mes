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
 * 自制件返修-SN作业应用服务默认实现
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

    // 获取当前用户
    CustomUserDetails curUser = DetailsHelper.getUserDetails();
    Long userId = curUser == null ? -1L : curUser.getUserId();

    private HmeEoJobSnSingleVO inSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto) {
        Set<String> repairTypes = lovAdapter.queryLovValue("HME.REPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> unrepairedTypes = lovAdapter.queryLovValue("HME.UNREPAIR_WO_TYPE", tenantId).stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getSnNum())) {
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (CollectionUtils.isEmpty(dto.getOperationIdList())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        } else if (dto.getOperationIdList().size() > 1) {
            //当前工位对应多个工艺,请检查
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
            //扫描条码为空,请确认
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        if (!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())) {
            //当前条码无效, 请确认
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        //V20210705 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可进站
        if(StringUtils.isNotBlank(mtMaterialLot.getStocktakeFlag()) &&
                HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
            //此SN【${1}】正在盘点,不可进站
            throw new MtException("HME_EO_JOB_SN_205", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_205", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 20210721 modify by sanfeng.zhang for xenxin.zhang 出站校验冻结标识为Y的SN不可进站
        if (HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())) {
            //【此SN【${1}】正在冻结,不可进站】
            throw new MtException("HME_EO_JOB_SN_208", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_208", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        dto.setMaterialLotId(mtMaterialLot.getMaterialLotId());

        // 20210908 add by sanfeng.zhang for hui.gu 判断SN在该工序是否已拦截
        hmeEoJobSnCommonService.interceptValidate(tenantId, dto.getWorkcellId(), Collections.singletonList(mtMaterialLot));

        // 20210813 modify by sanfeng.zhang for tianyang.xie 去掉多条返修执行作业校验  多个取最新一条eo
        // 20210813 modify by sanfeng.zhang for tianyang.xie 判断新条码和旧条码 新条码 则直接取对应的EO
//        //校验当前条码是否仅维护了一个返修EO
//        int reworkEoCount = hmeEoJobSnMapper.queryReworkEoCount(tenantId, dto.getSnNum());
//        if (reworkEoCount > 1) {
//            //当前条码维护多条返修执行作业,请检查!
//            throw new MtException("HME_EO_JOB_SN_179", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EO_JOB_SN_179", "HME"));
//        }
        // 判断新条码还是旧条码 为空 为新条码 否则 旧条码
        // 20210914 modify by sanfeng.zhang for wenxin.zhang 更改新旧条码的判断逻辑 先判断eo状态 完成的为旧条码 下达 运行中的为新条码
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        HmeEoJobSnVO5 snVO = null;
        if (!newMaterialLotFlag) {
            //查询条码相关信息
            snVO = hmeEoJobSnMapper.queryMaterialLotInfoForRework2(tenantId, dto.getMaterialLotId(), dto.getSiteId());
            if (Objects.isNull(snVO)) {
                //所扫描条码不存在或不为有效状态,请核实!
                throw new MtException("HME_NC_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0009", "HME"));
            }
            // 查询最新eo的信息
            List<HmeEoJobSnVO5> lastEoVOList = hmeEoJobSnMapper.queryLastEoByReworkMaterialLot(tenantId, snVO.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(lastEoVOList)) {
                // 20211125 add by sanfeng.zhang for wenxin.zhang 绑定了多个运行新条码时报错
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
            // 20210914 add by 增加校验 新条码的话 如果旧条码进行返修 则不允许新条码进站
            List<MtExtendAttrVO> attrVOList = hmeEoJobSnCommonService.queryOldCodeAttrList(tenantId, snVO.getEoId());
            Optional<MtExtendAttrVO> oldBarcodeInflagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("OLD_BARCODE_IN_FLAG", attr.getAttrName())).findFirst();
            if (oldBarcodeInflagOpt.isPresent() && YES.equals(oldBarcodeInflagOpt.get().getAttrValue())) {
                // 已由旧条码进站,不允许用新条码进站
                throw new MtException("HME_EO_JOB_SN_241", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_241", "HME"));
            }
            // 查询旧条码是否还有绑定其他状态为运行的EO
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
            // 20210914 modiy by sanfeng.zhang for wenxin.zhang 新条码 返修标识取旧条码上的
            Optional<MtExtendAttrVO> reworkFlagOpt = attrVOList.stream().filter(attr -> StringUtils.equals("REWORK_FLAG", attr.getAttrName())).findFirst();
            String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getAttrValue() : "";
            snVO.setReworkFlag(reworkFlag);
        }

        if (StringUtils.isBlank(snVO.getEoId()) || !repairTypes.contains(snVO.getWorkOrderType())
                || (!HmeConstants.StatusCode.RELEASED.equals(snVO.getWorkOrderStatus()) && !HmeConstants.StatusCode.EORELEASED.equals(snVO.getWorkOrderStatus()))) {
            //当前条码未生成返修工单，无法进行返修
            throw new MtException("HME_EO_JOB_SN_148", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_148", "HME"));
        }

        //是否返修进站
        // 20210914 modiy by sanfeng.zhang for wenxin.zhang 新条码 则找旧条码的的返修标识
        boolean isRework = HmeConstants.ConstantValue.YES.equals(snVO.getReworkFlag());
        if (!unrepairedTypes.contains(snVO.getWorkOrderType()) && !isRework) {
            //非返修状态条码不允许进行进站操作
            throw new MtException("HME_EO_JOB_SN_161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_161", "HME"));
        }

        //查询不良记录
        HmeNcRecordVO hmeNcRecordVO = hmeEoJobSnMapper.selectLastestNcRecordProcess(tenantId, dto.getMaterialLotId(), dto.getWorkcellId());
        if (Objects.nonNull(hmeNcRecordVO)) {
            resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
            resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
        }

        // 20211102 add by sanfeng.zhang for peng.zhao 查询器件测试标识
        String operationDeviceFlag = hmeEoJobSnMapper.queryOperationDeviceFlag(tenantId,  dto.getOperationId());
        snVO.setTestFlag(operationDeviceFlag);

        // 20211102 add by sanfeng.zhang for peng.zhao 交叉复测按钮显示 器件测试 返修 判定交叉复测为空 则显示
        snVO.setIsShowCrossRetestBtn(HmeConstants.ConstantValue.NO);
        if (HmeConstants.ConstantValue.YES.equals(operationDeviceFlag) &&
                HmeConstants.ConstantValue.YES.equals(snVO.getReworkFlag()) &&
                "".equals(snVO.getCrossRetestFlag())) {
            snVO.setIsShowCrossRetestBtn(HmeConstants.ConstantValue.YES);
        }

        //获取当前步骤
        String eoId = snVO.getEoId();
        HmeRouterStepVO5 currentStep = hmeEoJobSnCommonService.queryCurrentAndNextStepByEoAndOperation(tenantId, eoId, dto.getOperationId());
        resultVO.setEntryStepFlag(currentStep.getEntryStepFlag());

        //查询是否完成步骤
        resultVO.setDoneStepFlag(mtRouterDoneStepRepository.doneStepValidate(tenantId, currentStep.getRouterStepId()));

        //如果非完成步骤不允许点加工完成
        if (!HmeConstants.ConstantValue.YES.equals(resultVO.getDoneStepFlag()) && !HmeConstants.ConstantValue.YES.equals(resultVO.getEntryStepFlag())) {
            resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.NO);
        }

        //重复进站校验
        List<HmeEoJobSnVO23> hmeEoJobSns = hmeEoJobSnMapper.selectEoJobSnForInSite(tenantId, dto.getOperationId(), eoId, isRework);

        //如果不为空校验是否当前工位平台进站
        if (CollectionUtils.isNotEmpty(hmeEoJobSns)) {
            long count = 0;
            //返修进站时不允许多工艺同时进站
            count = hmeEoJobSns.stream().filter(item -> !dto.getOperationId().equals(item.getOperationId())).count();
            if (count > 0) {
                List<HmeEoJobSnVO23> exitEoJobSn = hmeEoJobSns.stream()
                        .filter(item -> !dto.getOperationId().equals(item.getOperationId())).collect(Collectors.toList());
                MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId, exitEoJobSn.get(0).getEoStepId());
                //当前SN已在${1}进站,不允许在此工位重复进站
                throw new MtException("HME_EO_JOB_SN_151", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_151", "HME", routerStep.getStepName()));
            }

            //校验工位是否一致
            count = hmeEoJobSns.stream().filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).count();
            if (count > 0) {
                List<HmeEoJobSnVO23> exitEoJobSn = hmeEoJobSns.stream()
                        .filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, exitEoJobSn.get(0).getWorkcellId());
                //当前SN已在${1}进站,不允许在此工位重复进站
                throw new MtException("HME_EO_JOB_SN_144", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_144", "HME", mtModWorkcell.getWorkcellName()));
            }

            //校验平台是否一致
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

                //当前SN已在${1}进站,不允许在此平台重复进站
                throw new MtException("HME_EO_JOB_SN_143", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_143", "HME", jobTypeDesc));
            }

            //SN已经进站，走查询逻辑
            resultVO.setSnVO(snVO);
            resultVO.setIsQuery(HmeConstants.ConstantValue.YES);
            resultVO.setExitEoJobSn(hmeEoJobSns.get(0));
            resultVO.setIsContainer(false);
            resultVO.setIsRework(isRework);
            return resultVO;
        }

        // 验证EO状态
        if (!HmeConstants.EoStatus.WORKING.equals(snVO.getStatus())
                || (!HmeConstants.EoStatus.RELEASED.equals(snVO.getLastEoStatus())
                && !HmeConstants.EoStatus.HOLD.equals(snVO.getLastEoStatus()))) {
            //EO状态必须为运行状态
            throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
        }

        //查询某个物料是否在某个工序被限制返修进站次数
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
        //查询返修记录
        HmeRepairRecord queryRepairRecord = new HmeRepairRecord();
        queryRepairRecord.setTenantId(tenantId);
        queryRepairRecord.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        queryRepairRecord.setMaterialId(mtMaterialLot.getMaterialId());
        queryRepairRecord.setWorkcellId(dto.getProcessId());
        HmeRepairRecord repairRecord = repairRecordRepository.selectOne(queryRepairRecord);
        resultVO.setRepairRecord(repairRecord);
        // 判断 SN 返修次数是否达到返修限制次数
        if (Objects.nonNull(repairRecord)) {
            // SN 达到返修次数后被 禁止返修
            if ("STOP".equals(repairRecord.getStatus()) ){
                throw new MtException("HME_EO_JOB_SN_246", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_246", "HME"));
            }
            // SN 达到返修次数后 不允许进站 等待工艺判定
            if ( "WAITING".equals(repairRecord.getStatus())) {
                throw new MtException("HME_EO_JOB_SN_245", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_245", "HME"));
            }
        }

        //获取EO下所有的正常加工工艺步骤
        List<String> eoIdList = new ArrayList<>();
        eoIdList.add(eoId);
        List<HmeRouterStepVO> normalStepList = hmeEoJobSnMapper.batchSelectNormalStepByEoIds(tenantId, eoIdList);
        if (CollectionUtils.isEmpty(normalStepList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "最近正常加工步骤"));
        }

        //按照最近更新时间倒序排序
        normalStepList = normalStepList.stream()
                .sorted(Comparator.comparing(HmeRouterStepVO::getLastUpdateDate, Comparator.reverseOrder())
                        .thenComparing(HmeRouterStepVO::getCreationDate, Comparator.reverseOrder())).collect(Collectors.toList());

        //批量查询最近一步工艺步骤
        Map<String, HmeRouterStepVO> nearStepMap = hmeEoJobSnCommonService.batchQueryRouterStepByEoIds(tenantId, HmeConstants.StepType.NEAR_STEP, eoIdList);

        //构造返回数据
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
     * @Description 工序作业-单个进站扫描-返修
     *
     * @author jianmin.hong
     * @date 2020/11/23 16:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @param singleVO 参数
     * @return HmeEoJobSnVO
     *
     */
    private HmeEoJobSnVO reworkInSiteScan(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO singleVO) {
        //构造对象
        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        HmeEoJobSnVO3 snLine = singleVO.getSnLine();
        HmeRouterStepVO normalNearStep = singleVO.getNormalStepList().get(0);
        HmeRouterStepVO nearStepVO = singleVO.getNearStep();
        HmeEoJobSnVO5 snVO = singleVO.getSnVO();

        snLine.setSiteId(snVO.getSiteId());
        snLine.setWorkOrderId(snVO.getWorkOrderId());
        snLine.setMaterialId(snVO.getMaterialId());
        snLine.setEoStepNum(HmeConstants.ConstantValue.ONE);

        //执行工艺步骤移入
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

        //工序排队加工
        eoWorking(tenantId, snVO.getEoQty(), snLine);

        //容器卸载
        if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
            snLine.setSourceContainerId(snVO.getCurrentContainerId());
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(snVO.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(snVO.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }

        //创建作业
        HmeEoJobSnVO2 currentJobSnVO = batchSnService.createSnJob(tenantId, snLine);

        // 创建设备数据
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(currentJobSnVO.getJobId());
        equSwitchParams.setWorkcellId(dto.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);

        //V20210302 modify by penglin.sui for fang.pan 更新售后返品拆机
        if(HmeConstants.ConstantValue.YES.equals(snVO.getAfFlag()) && HmeConstants.ConstantValue.YES.equals(singleVO.getEntryStepFlag())){
            HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryLastSplitRecordOfMaterialLot(tenantId,snVO.getMaterialLotId());
            if(Objects.nonNull(hmeServiceSplitRecord)){
                //当前时间
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

                    //记录历史
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

        //更新 SN 在工序、 物料 下的返修次数
        if(Objects.isNull(singleVO.getRepairRecord())) {
            //新增记录
            HmeRepairRecord repairRecord = new HmeRepairRecord();
            repairRecord.setTenantId(tenantId);
            repairRecord.setMaterialLotId(snLine.getMaterialLotId());
            repairRecord.setMaterialId(snLine.getMaterialId());
            repairRecord.setWorkcellId(dto.getProcessId());
            repairRecord.setRepairCount(1L);
            repairRecord.setStatus("UNDEFINED");
            HmeRepairLimitCount repairLimit = singleVO.getRepairLimitCount();
            if (!Objects.isNull(repairLimit)) {
                //限制次数
                repairRecord.setLimitCount(repairLimit.getLimitCount());
                // 放行次数 初始值为 0
                repairRecord.setPassCount(0L);
                repairRecord.setPermitCount(repairLimit.getLimitCount());
                //允许返修的总次数 初始值为 返修限制次数
                repairRecord.setPermitRepairCount(repairLimit.getLimitCount());
                if (repairRecord.getRepairCount() >= repairLimit.getLimitCount()) {
                    repairRecord.setStatus("WAITING");
                }
            }
            repairRecordRepository.insertSelective(repairRecord);
        } else {
            //更新
            HmeRepairRecord repairRecord = singleVO.getRepairRecord();
            HmeRepairLimitCount repairLimit = singleVO.getRepairLimitCount();
            Long repairCount = 1L;
            if (!Objects.isNull(repairRecord.getRepairCount())) {
                repairCount = repairRecord.getRepairCount()+1;
            }
            repairRecord.setRepairCount(repairCount);
            // 判断进站次数是否达到返修 限制的进站次数 若是，更新状态
            if (!Objects.isNull(repairRecord.getPermitRepairCount()) && !Objects.isNull(repairLimit)) {
                if (repairRecord.getPermitRepairCount() <= repairCount) {
                    repairRecord.setStatus("WAITING");
                }
            }
            repairRecordMapper.updateByPrimaryKeySelective(repairRecord);
        }

        // 获取当前作业批次物料
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

        // 获取当前作业时效物料
        List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOListAll = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, jobCondition, null);
        if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialVOListAll)) {
            resultVO.setTimeMaterialVOList(hmeEoJobTimeMaterialVOListAll);
        }

        // 获取当前作业序列物料
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

        // 如存在工艺步骤，需获取步骤名称
        HmeRouterStepVO5 currentStep = singleVO.getCurrentStep();
        if (Objects.nonNull(currentStep)) {
            resultVO.setDoneStepFlag(singleVO.getDoneStepFlag());
            resultVO.setCurrentStepName(currentStep.getStepName());
            resultVO.setCurrentStepDescription(currentStep.getDescription());
            resultVO.setCurrentStepSequence(currentStep.getSequence());
            resultVO.setNextStepName(currentStep.getNextStepName());
            resultVO.setNextStepDescription(currentStep.getNextDescription());
        }

        //数据采集项
        if (CollectionUtils.isNotEmpty(currentJobSnVO.getDataRecordVOList())) {
            resultVO.setDataRecordVOList(currentJobSnVO.getDataRecordVOList());
        }

        // 班次
        if (currentJobSnVO.getShiftId() != null) {
            MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, currentJobSnVO.getShiftId());
            resultVO.setShiftId(currentJobSnVO.getShiftId());
            resultVO.setShiftCode(mtWkcShift.getShiftCode());
        }

        //工位信息
        if (StringUtils.isBlank(dto.getWorkcellCode())) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        } else {
            resultVO.setWorkcellCode(dto.getWorkcellCode());
            resultVO.setWorkcellName(dto.getWorkcellName());
        }

        // 进出站相关
        resultVO.setOperationId(currentJobSnVO.getOperationId());
        resultVO.setEoStepId(currentJobSnVO.getEoStepId());
        resultVO.setEoStepNum(currentJobSnVO.getEoStepNum());
        resultVO.setJobId(currentJobSnVO.getJobId());
        resultVO.setEoId(currentJobSnVO.getEoId());
        resultVO.setSiteInBy(currentJobSnVO.getSiteInBy());
        // 操作员
        String userRealName = userClient.userInfoGet(tenantId, currentJobSnVO.getSiteInBy()).getRealName();
        resultVO.setSiteInByName(userRealName);
        resultVO.setSiteInDate(currentJobSnVO.getSiteInDate());
        resultVO.setSiteOutDate(currentJobSnVO.getSiteOutDate());
        resultVO.setSourceContainerId(currentJobSnVO.getSourceContainerId());

        //V20201208 modify by penglin.sui 返回BOM信息
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(currentJobSnVO.getEoId());
        List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(hmeEoBomVOList)) {
            resultVO.setBomId(hmeEoBomVOList.get(0).getBomId());
            resultVO.setBomName(hmeEoBomVOList.get(0).getBomName());
        }

        //判断进站工位是否在值集 HME.WORKCELL_ABNORMAL_OUT_SITE_FLAG 中， 值集内的工位允许异常出站
        List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.WORKCELL_ABNORMAL_OUT_SITE_FLAG", tenantId);
        Optional<LovValueDTO> firstOpt = lovValue.stream().filter(lov->StringUtils.equals(dto.getWorkcellCode(), lov.getValue())).findFirst();
        if (firstOpt.isPresent()) {
            resultVO.setIsShowAbnormalOutBtn(HmeConstants.ConstantValue.YES);
        } else {
            resultVO.setIsShowAbnormalOutBtn(HmeConstants.ConstantValue.NO);
        }

        // 执行作业相关
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
     * @Description 工序排队加工
     *
     * @author yuchao.wang
     * @date 2020/12/17 10:53
     * @return void
     *
     */
    private void eoWorking(Long tenantId, BigDecimal eoQty, HmeEoJobSnVO3 snLine) {
        //创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_IN");

        // 取当前步骤的步骤实绩
        MtEoRouterActualVO2 routerActualParam = new MtEoRouterActualVO2();
        routerActualParam.setEoId(snLine.getEoId());
        List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                mtEoRouterActualMapper.eoOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList)) {
            throw new MtException("HME_EO_JOB_SN_039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
        }

        // 多条步骤实绩时，取最新的
        String eoStepActualId;
        if (eoOperationLimitCurrentRouterList.size() > 1) {
            eoStepActualId = eoOperationLimitCurrentRouterList.stream()
                    .max(Comparator.comparing(MtEoRouterActualVO5::getLastUpdateDate)).get()
                    .getEoStepActualId();
        } else {
            eoStepActualId = eoOperationLimitCurrentRouterList.get(0).getEoStepActualId();
        }
        log.debug("当前步骤的步骤实绩:" + eoStepActualId);

        // 工序排队
        MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
        mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO4.setQueueQty(new Double(String.valueOf(eoQty)));
        mtEoStepWipVO4.setWorkcellId(snLine.getWorkcellId());
        mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);

        // 工序在制
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
     * @Description 工序作业-单个进站扫描
     *
     * @author jianmin.hong
     * @date 2020/11/23 16:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @return HmeEoJobSnVO
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        //校验
        HmeEoJobSnSingleVO hmeEoJobSnSingleVO = inSiteScanValidate(tenantId, dto);
        String showTagModelFlag = hmeTagCheckRepository.getShowTagModelFlag(tenantId, dto.getWorkcellId());
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleVO.getIsQuery())) {
            HmeEoJobSnVO hmeEoJobSnVO = inSiteQuery(tenantId, dto, hmeEoJobSnSingleVO);
            hmeEoJobSnVO.setShowTagModelFlag(showTagModelFlag);
            return hmeEoJobSnVO;
        }

        //返修进站逻辑
        HmeEoJobSnVO result = reworkInSiteScan(tenantId, dto, hmeEoJobSnSingleVO);

        //如果是第一次进站，将自身投料
        int inSiteCount = hmeEoJobSnMapper.queryEoJobSnCountByEoId(tenantId, result.getEoId());
        if (inSiteCount == 1) {
            // 20210812 add by sanfeng.zhang for tianyang.xie 新创建事件 更新条码
            String eventRequestId = this.updateMaterialLot(tenantId, dto.getSnNum());
            // 20210322 add by sanfeng.zhang for fang.pan 构建eo返修关系
            this.createEoRel(tenantId, dto, result.getEoId());
            releaseSelfForFirstInSite(tenantId, dto, hmeEoJobSnSingleVO, result, eventRequestId);
            // 如果是新条码第一次进站 则将泵浦源位置eo更新
            this.updatePumpPositionHeader(tenantId, result.getEoId(), dto.getSnNum());
        }
        // 计算反射镜采集项
        hmeEoJobSnSingleInService.calculationReflectorRecord(tenantId, dto, result);

        // 非第一次进站 校验进站SN必须为在制品
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
            // 条码[${1}]不为在制品,不允许进站!
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
                // 根据旧条码eo 找到泵浦源组合位置信息
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
     * @Description 如果是第一次进站，将自身投料
     *
     * @author yuchao.wang
     * @date 2021/1/5 20:29
     * @param tenantId 租户ID
     * @param dto 参数
     * @param singleVO 参数
     * @param result 参数
     * @return void
     *
     */
    private void releaseSelfForFirstInSite(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO singleVO, HmeEoJobSnVO result, String barcodeEventRequestId) {
        HmeEoJobSnVO5 snVO = singleVO.getSnVO();
        HmeEoJobSnVO3 snLine = singleVO.getSnLine();

        // 20210915 add by  sanfeng.zhang for wenxin.zhang 新条码 则找旧条码的物料
        // 20210815 add by  sanfeng.zhang for tianyang.xie 新条码 则找旧条码 进行投料
        boolean newMaterialLotFlag = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        String materialId = snVO.getMaterialId();
        String materialLotId = dto.getMaterialLotId();
        if (newMaterialLotFlag) {
            // ture则为新条码
            String reworkMaterialLotCode = hmeEoJobSnReWorkMapper.queryReworkMaterialLotCode(tenantId, dto.getMaterialLotId());
            List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, Collections.singletonList(reworkMaterialLotCode));
            if (CollectionUtils.isEmpty(materialLotList)) {
                throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_004", "HME", reworkMaterialLotCode));
            }
            // 新条码第一次进站 如果旧条码为在制品 不允许进站
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
        //查询EO组件中与当前条码物料对应的组件
        List<HmeBomComponentVO3> eoComponentList = hmeEoJobSnMapper.queryEoComponentByEoIdAndMaterialId(tenantId, snVO.getEoId(), materialId, snLine.getEoStepId());
        if (CollectionUtils.isEmpty(eoComponentList) || eoComponentList.size() != 1) {
            throw new MtException("HME_EO_JOB_SN_162", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_162", "HME"));
        }
        HmeBomComponentVO3 eoComponent = eoComponentList.get(0);

        //查询条码相关信息
        HmeMaterialLotVO4 materialLotInfo = hmeEoJobSnMapper.queryMaterialLotInfoForInSiteRelease(tenantId, materialLotId);
        if (Objects.isNull(eoComponent.getBomComponentQty()) || Objects.isNull(materialLotInfo.getPrimaryUomQty())
                || !eoComponent.getBomComponentQty().equals(materialLotInfo.getPrimaryUomQty())) {
            throw new MtException("HME_EO_JOB_SN_162", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_162", "HME"));
        }

        //查询开班实绩编码
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());

        //创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        //创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        eventCreateVO.setEventRequestId(barcodeEventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        if (newMaterialLotFlag) {
            // 新条码找旧条码进行投料时  将旧条码失效 数量置为0
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
        // 更新条码在制标识
        List<MtExtendVO5> extendAttrList = new ArrayList<>(1);
        MtExtendVO5 inworkFlagAttr = new MtExtendVO5();
        inworkFlagAttr.setAttrName("MF_FLAG");
        inworkFlagAttr.setAttrValue(YES);
        extendAttrList.add(inworkFlagAttr);
        //V20210324 modify by penglin.sui for fang.pan 返修第一次进站将进站sn返修标志改为Y
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

        //更新现有量
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(materialLotInfo.getLocatorId());
        mtInvOnhandQuantityVO9.setMaterialId(materialLotInfo.getMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(eoComponent.getBomComponentQty());
        mtInvOnhandQuantityVO9.setEventId(eventId);
        mtInvOnhandQuantityVO9.setLotCode(materialLotInfo.getLot());
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        // 客户机内部订单不存在投自身，因此单独获取事务用于mes客户机库存冲销，不传送sap
        if (StringUtils.isBlank(result.getWorkOrderId())) {
            //未传入工单信息,请检查!
            throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0002", "HME"));
        }

        HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, result.getWorkOrderNum());
        String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
        // 判断工单是否存在于返修拆机记录中
        if (!Objects.isNull(splitRecord)) {
            //判断内部订单号是否为空
            if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())) {
                transactionTypeCode = HmeConstants.TransactionTypeCode.AF_ZSD005_ISSUE;
            }
        }
        //查询事务类型
        WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
        wmsTransactionTypePara.setTenantId(tenantId);
        wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);

        //记录事务明细
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
        // 事务类型为AF_ZSD005_ISSUE 移动类型置为空
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

        //装配API
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

        //记录投料记录

        //查询条码扩展属性
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
        // 改造工单更新料号 判断eo物料和条码物料是否相同 不一致则更新条码物料
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
            //该条码【${1}】在该工位未进站作业,请检查!
            throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_010", "HME", dto.getSnNum()));
        }

        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        BeanUtils.copyProperties(exitEoJobSn, resultVO);

        // 获取当前作业批次物料
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

        // 获取当前作业时效物料
        List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOListAll = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, jobCondition, null);
        if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialVOListAll)) {
            resultVO.setTimeMaterialVOList(hmeEoJobTimeMaterialVOListAll);
        }

        // 获取当前作业序列物料
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

        // 获取当前作业数据采集
        HmeEoJobMaterialVO2 jobDataRecordCondition = new HmeEoJobMaterialVO2();
        jobDataRecordCondition.setWorkcellId(dto.getWorkcellId());
        jobDataRecordCondition.setJobId(resultVO.getJobId());
        jobDataRecordCondition.setJobType(dto.getJobType());
        List<HmeEoJobDataRecordVO> eoJobDataRecordVOList = hmeEoJobDataRecordRepository.eoJobDataRecordQuery(tenantId, jobDataRecordCondition);
        if (CollectionUtils.isNotEmpty(eoJobDataRecordVOList)) {
            resultVO.setDataRecordVOList(eoJobDataRecordVOList);
        }

        // 如存在工艺步骤，需获取步骤信息
        if (StringUtils.isNotBlank(exitEoJobSn.getEoStepId())) {
            HmeRouterStepVO5 currentStep = hmeEoJobSnCommonService.queryCurrentAndNextStepByCurrentId(tenantId, exitEoJobSn.getEoStepId());
            if (Objects.nonNull(currentStep)) {
                //查询是否完成步骤
                resultVO.setDoneStepFlag(mtRouterDoneStepRepository.doneStepValidate(tenantId, exitEoJobSn.getEoStepId()));
                resultVO.setCurrentStepName(currentStep.getStepName());
                resultVO.setCurrentStepDescription(currentStep.getDescription());
                resultVO.setCurrentStepSequence(currentStep.getSequence());
                resultVO.setNextStepName(currentStep.getNextStepName());
                resultVO.setNextStepDescription(currentStep.getNextDescription());
            }
        }

        // 节拍
        if (resultVO.getSiteOutDate() != null) {
            String meterTimeStr = DateUtil.getDistanceTime(resultVO.getSiteInDate(),
                    resultVO.getSiteOutDate());
            resultVO.setMeterTimeStr(meterTimeStr);
        }

        // 操作员
        if (Objects.nonNull(resultVO.getSiteInBy())) {
            String userRealName = userClient.userInfoGet(tenantId, resultVO.getSiteInBy()).getRealName();
            resultVO.setSiteInByName(userRealName);
        }

        //工位信息
        if (StringUtils.isBlank(dto.getWorkcellCode())) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        } else {
            resultVO.setWorkcellCode(dto.getWorkcellCode());
            resultVO.setWorkcellName(dto.getWorkcellName());
        }

        //查询班次、生产版本
        HmeEoJobSnVO jobSnInfo = hmeEoJobSnMapper.queryEoJobSnInfoForInSiteQuery(tenantId, resultVO.getJobId());
        if (Objects.nonNull(jobSnInfo)) {
            resultVO.setShiftCode(jobSnInfo.getShiftCode());
            resultVO.setProductionVersion(jobSnInfo.getProductionVersion());
        }

        //V20201208 modify by penglin.sui 返回BOM信息
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(hmeEoJobSnSingleVO.getSnVO().getEoId());
        List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(hmeEoBomVOList)) {
            resultVO.setBomId(hmeEoBomVOList.get(0).getBomId());
            resultVO.setBomName(hmeEoBomVOList.get(0).getBomName());
        }

        //判断进站工位是否在值集 HME.WORKCELL_ABNORMAL_OUT_SITE_FLAG 中， 值集内的工位允许异常出站
        List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.WORKCELL_ABNORMAL_OUT_SITE_FLAG", tenantId);
        Optional<LovValueDTO> firstOpt = lovValue.stream().filter(lov->StringUtils.equals(dto.getWorkcellCode(), lov.getValue())).findFirst();
        if (firstOpt.isPresent()) {
            resultVO.setIsShowAbnormalOutBtn(HmeConstants.ConstantValue.YES);
        } else {
            resultVO.setIsShowAbnormalOutBtn(HmeConstants.ConstantValue.NO);
        }

        // 执行作业相关
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
     * @Description 单件作业-出炉
     *
     * @author yuchao.wang
     * @date 2020/11/21 13:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSn outSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnReworkServiceImpl.outSiteScan.Start tenantId={},dto={}", tenantId, dto);
        //非空校验
        paramsVerificationForOutSite(tenantId, dto);

        //查询后续用到的数据
        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic = queryBasicData(tenantId, dto);

        //出站校验,如果校验结果不为空返回前台做确认操作
        HmeEoJobSn validateResult = outSiteValidate(tenantId, hmeEoJobSnSingleBasic, dto);
        if (Objects.nonNull(validateResult) && StringUtils.isNotBlank(validateResult.getErrorCode())) {
            return validateResult;
        }
        // 20210818 add by sanfeng.zhang for peng.zhao 器件测试不良做判断 加工完成还是在线返修
        if (hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId())) {
            String outSiteAction = hmeEoJobSnSingleService.queryOutSiteAction(tenantId, dto, hmeEoJobSnSingleBasic);
            dto.setOutSiteAction(outSiteAction);
        }
        // 2021-09-13 add by sanfeng.zhang for wenxin.zhang 返修作业平台-泵浦源性能数据校验 判断物料是否在泵浦源筛选规则头表及是否是组合SN 存在则走泵浦源出站校验
        Long pumpCount = hmeEoJobSnReWorkMapper.queryPumpProcessFlag(tenantId, hmeEoJobSnSingleBasic.getMaterialLot().getMaterialId());
        List<HmePumpCombDTO> pumpCombVOList = hmeEoJobSnReWorkMapper.queryPumpCombListByMaterialLotId(tenantId, hmeEoJobSnSingleBasic.getMaterialLot().getMaterialLotId());
        List<HmeEoJobPumpCombVO5> hmeEoJobPumpCombVO5List = new ArrayList<>();
        if(pumpCount.compareTo(0L) > 0 && CollectionUtils.isNotEmpty(pumpCombVOList)){
            hmeEoJobPumpCombVO5List = hmeEoJobPumpCombRepository.pumpFilterRuleVerify(tenantId, dto);
        }
        // 返修作业平台标识
        hmeEoJobSnSingleBasic.setReworkProcessFlag(HmeConstants.ConstantValue.YES);
        //如果完工出站执行完整出站逻辑，继续返修走单独出站逻辑
        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            //非返修扣减反冲料
            if (!HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getMaterialLot().getReworkFlag())) {
                self().backFlushMaterialOutSite(tenantId, hmeEoJobSnSingleBasic, dto);
            }

            //调用出站通用主方法
            List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.mainOutSite(tenantId, dto, hmeEoJobSnSingleBasic);

            //发送实时接口
            hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
        } else if (HmeConstants.OutSiteAction.REWORK_COMPLETE.equals(dto.getOutSiteAction())) {
            // 在线返修
            //调用出站通用主方法
            List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.mainOutSite2(tenantId, dto, hmeEoJobSnSingleBasic);
            //发送实时接口
            hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
        } else {
            hmeEoJobSnCommonService.mainOutSiteForRework(tenantId, dto, hmeEoJobSnSingleBasic);
        }

        //V20210428 modify by penglin.sui for tianyang.xie 删除工位EO关系,放在最后，防止锁表影响进站更新此表
        List<HmeWkcEoRel> hmeWkcEoRelList = hmeWkcEoRelRepository.selectByCondition(Condition.builder(HmeWkcEoRel.class)
                .andWhere(Sqls.custom().andEqualTo(HmeWkcEoRel.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeWkcEoRel.FIELD_WKC_ID, dto.getWorkcellId())
                        .andEqualTo(HmeWkcEoRel.FIELD_OPERATION_ID, dto.getOperationId())).build());
        if(CollectionUtils.isNotEmpty(hmeWkcEoRelList)){
            //删除
            hmeWkcEoRelRepository.batchDeleteByPrimaryKey(hmeWkcEoRelList);
        }

        // 2021-09-13 add by sanfeng.zhang for wenxin.zhang 出站时增加记录功率和电压数据到数据采集项记录表逻辑,最多新增两条数据
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
            log.info("=================================>泵浦源作业-记录功率和电压数据耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        //构造返回数据
        HmeEoJobSn result = getResultData(tenantId, hmeEoJobSnSingleBasic, dto);
        log.info("<====== HmeEoJobSnReworkServiceImpl.outSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
        return result;
    }

    /**
     *
     * @Description 反冲料投料
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:10
     * @param tenantId 租户ID
     * @param hmeEoJobSnSingleBasic 参数
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backFlushMaterialOutSite(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite tenantId={},hmeEoJobSnSingleBasic={},dto={}", tenantId, hmeEoJobSnSingleBasic, dto);
        //如果没有反冲料组件直接返回即可
        List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnSingleBasic.getBackFlushBomComponentList();
        if (CollectionUtils.isEmpty(backFlushBomComponentList)) {
            log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite Return:EO没有反冲料组件");
            return;
        }

        HmeEoVO4 eo = hmeEoJobSnSingleBasic.getEo();
        HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
        HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity();

        //工单对应的产线扩展字段，为Y不做反冲料投料
        if (HmeConstants.ConstantValue.YES.equals(wo.getBackflushNotFlag())) {
            log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite Return:工单对应的BackflushNotFlag为Y wo={}", wo);
            return;
        }

        //eo组件或者wo组件为空直接返回
        if (CollectionUtils.isEmpty(eo.getBomComponentList())
                || CollectionUtils.isEmpty(wo.getBomComponentList())) {
            log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite Return:EO/WO组件为空 " +
                    "eoComponentList={},woComponentList={}", eo.getBomComponentList(), wo.getBomComponentList());
            return;
        }

        //获取所有反冲组件ID
        List<String> backFlushBomComponentIdList = backFlushBomComponentList.stream()
                .map(MtBomComponent::getBomComponentId).collect(Collectors.toList());

        //查询所有要投料的EO组件
        List<HmeBomComponentVO> allReleaseEoComponentList = eo.getBomComponentList().stream()
                .filter(item -> backFlushBomComponentIdList.contains(item.getBomComponentId())).collect(Collectors.toList());
        List<String> releaseEoComponentIdList = allReleaseEoComponentList.stream()
                .map(HmeBomComponentVO::getBomComponentId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(allReleaseEoComponentList)) {
            log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite Return:未找到要投料的EO组件 " +
                    "eoComponentList={},backFlushBomComponentIdList={}", eo.getBomComponentList(), backFlushBomComponentIdList);
            return;
        }

        //获取所有反冲料ID
        List<String> backFlushMaterialIdList = backFlushBomComponentList.stream()
                .filter(item -> releaseEoComponentIdList.contains(item.getBomComponentId()))
                .map(MtBomComponent::getMaterialId).collect(Collectors.toList());

        //查询反冲货位
        MtModLocator backFlushLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.BACKFLUSH,dto.getWorkcellId());

        //查询反冲条码
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

        //获取BOM数量
        BigDecimal bomPrimaryQty = BigDecimal.valueOf(eo.getBomPrimaryQty());

        //查询所有的WO组件装配实绩
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

        log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite 待投料组件数[{}]", allReleaseEoComponentList.size());

        //对于所有的组件进行投料
        List<MtAssembleProcessActualVO11> materialInfo = new ArrayList<>();
        Map<String, MtAssembleProcessActualVO17> eoAssembleProcessActualMap = new HashMap<>();
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
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

            //计算所有条码数量/剩余要投数量
            BigDecimal materialUomQty = BigDecimal.valueOf(materialLot.getPrimaryUomQty());
            BigDecimal remainQty = BigDecimal.valueOf(releaseEoComponent.getBomComponentQty())
                    .multiply(materialUomQty).divide(bomPrimaryQty, 4 , BigDecimal.ROUND_HALF_UP);

            //校验条码数量是否足够
            double releaseMaterialLotPrimaryQtySum = releaseMaterialLotList.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
            if(BigDecimal.valueOf(releaseMaterialLotPrimaryQtySum).compareTo(remainQty) < 0){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(releaseEoComponent.getBomComponentMaterialId());
                //当前反冲库位下反冲物料【{1}】现有量不足
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            //筛选wo组件中与当前eo组件相同物料行号的
            HmeBomComponentVO releaseWoComponent = null;
            List<HmeBomComponentVO> releaseWoComponentList = wo.getBomComponentList().stream()
                    .filter(item -> item.getBomComponentMaterialId().equals(releaseEoComponent.getBomComponentMaterialId())
                            && item.getBomComponentLineNumber().equals(releaseEoComponent.getBomComponentLineNumber()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(releaseWoComponentList)) {
                releaseWoComponent = releaseWoComponentList.get(0);
            }
            if (Objects.isNull(releaseWoComponent)) {
                log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite continue:没有找到 wo组件中与当前eo组件相同物料行号的 releaseEoComponent={}", releaseEoComponent);
                continue;
            }

            //查询当前工单组件剩余数量，如果小于等于0不投
            BigDecimal woDemandQty = StringUtils.isBlank(releaseWoComponent.getWoBomComponentDemandQty())
                    ? BigDecimal.ZERO : new BigDecimal(releaseWoComponent.getWoBomComponentDemandQty());
            if(woDemandQty.compareTo(BigDecimal.ZERO) <= 0){
                log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite continue:前工单组件剩余数量为0 releaseWoComponent={}", releaseWoComponent);
                continue;
            }

            //校验WO组件装配实绩数量
            if (woComponentActualMap.containsKey(releaseWoComponent.getBomComponentId())) {
                double assembleQty = woComponentActualMap.get(releaseWoComponent.getBomComponentId())
                        .stream().mapToDouble(MtWorkOrderComponentActual::getAssembleQty).sum();
                if ((BigDecimal.valueOf(assembleQty).add(remainQty)).compareTo(woDemandQty) > 0) {
                    log.info("<====== HmeEoJobSnReworkServiceImpl.backFlushMaterialOutSite continue:校验WO组件装配实绩数量未通过" +
                            " releaseWoComponent={}, assembleQty={}, remainQty={}", releaseWoComponent, assembleQty, remainQty);
                    continue;
                }
            }

            log.info("<====== HmeEoJobSnReworkServiceImpl.batchBackFlushMaterialOutSite 组ID[{}],需投料数量[{}]", releaseWoComponent.getBomComponentId(), remainQty);

            //循环所有条码投料
            for (HmeMaterialLotVO3 releaseMaterialLot : releaseMaterialLotList) {
                //计算数量
                BigDecimal currentQty = remainQty;
                if (remainQty.compareTo(BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty())) >= 0) {
                    currentQty = BigDecimal.valueOf(releaseMaterialLot.getPrimaryUomQty());
                }
                remainQty = remainQty.subtract(currentQty);

                //API-物料批消耗
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

                //构造API-批量组件装配-当前条码装配信息
                MtAssembleProcessActualVO11 assembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                assembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                assembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                assembleProcessActualVO11.setBomComponentId(releaseEoComponent.getBomComponentId());
                assembleProcessActualVO11.setMaterialId(releaseMaterialLot.getMaterialId());
                assembleProcessActualVO11.setTrxAssembleQty(currentQty.doubleValue());
                assembleProcessActualVO11.setMaterialLotId(releaseMaterialLot.getMaterialLotId());
                assembleProcessActualVO11.setLocatorId(releaseMaterialLot.getLocatorId());
                materialInfo.add(assembleProcessActualVO11);

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

        //构造API-批量组件装配数据
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
        log.info("<================投料开始时间====================>");
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO3 hmeEoJobSnVO = hmeEoJobSnLotMaterialRepository.lotMaterialOutSite2(tenantId, dto);
        long endDate = System.currentTimeMillis();
        log.info("===================================>投料总耗时：" + (endDate - startDate)+ "ms");
        log.info("<================投料结束时间====================>");
        return hmeEoJobSnVO;
    }

    /**
     *
     * @Description 投料校验
     *
     * @author penglin.sui
     * @date 2020/12/24 21:19
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    private void releaseValidate(Long tenantId, HmeEoJobSnReworkVO4 dto){
        if(CollectionUtils.isEmpty(dto.getComponentList())){
            //请先勾选要投料的条码
            throw new MtException("HME_EO_JOB_SN_129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_129", "HME"));
        }

        List<HmeEoJobSnReworkVO> hmeEoJobSnReworkVOList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(hmeEoJobSnReworkVOList)){
            //请先勾选要投料的条码
            throw new MtException("HME_EO_JOB_SN_129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_129", "HME"));
        }
    }

    /**
     *
     * @Description 查询事务类型
     *
     * @author penglin.sui
     * @date 2020/11/26 16:46
     * @param tenantId 租户ID
     * @return Map<String,WmsTransactionTypeDTO>
     *
     */
    private Map<String, WmsTransactionTypeDTO> selectTransactionType(Long tenantId){
        //批量查询移动类型
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
            //未找到事务类型${1},请检查!
            throw new MtException("WMS_COST_CENTER_0066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0066", "WMS",""));
        }
        Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap = wmsTransactionTypeList.stream().collect(Collectors.toMap(WmsTransactionTypeDTO::getTransactionTypeCode, t -> t));
        return wmsTransactionTypeMap;
    }

    /**
     *
     * @Description 执行作业实绩
     *
     * @author penglin.sui
     * @date 2020/12/24 22:53
     * @param tenantId 租户ID
     * @param dto 参数
     * @return Map<String,MtEoComponentActual>
     *
     */
    private Map<String, MtEoComponentActual> eoComponentActualGet(Long tenantId, HmeEoJobSnReworkVO4 dto){
        //查询已投数量
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
        //查询已投数量
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
     * @Description 查询工艺路线
     *
     * @author penglin.sui
     * @date 2020/12/24 23:06
     * @param tenantId 租户ID
     * @return Map<String,MtEoRouter>
     *
     */
    private Map<String, MtEoRouter> selectRouterMap(Long tenantId, HmeEoJobSnVO dto){
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(dto.getEoId());
        List<MtEoRouter> eoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId,eoIdList);
        Map<String, MtEoRouter> eoRouterMap = eoRouterList.stream().collect(Collectors.toMap(MtEoRouter::getEoId, t -> t));
        if(eoRouterMap.size() != eoIdList.size()){
            //执行作业工艺路线为空${1}
            throw new MtException("MT_ORDER_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0036", "HME",""));
        }
        return eoRouterMap;
    }

    /**
     *
     * @Description 数据准备
     *
     * @author penglin.sui
     * @date 2020/12/24 22:31
     * @param tenantId 租户ID
     * @param dto 参数
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
            //未查询到区域库位
            throw new MtException("HME_EO_JOB_SN_132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_132", "HME"));
        }
        resultVO.setAreaLocator(areaLocator);

        //批量查询区域库位
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
            //未查询到区域库位
            throw new MtException("HME_EO_JOB_SN_132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_132", "HME"));
        }
        Map<String, HmeModLocatorVO2> areaLocatorMap = hmeModLocatorVO2List.stream().collect(Collectors.toMap(item -> item.getSubLocatorId(), t -> t));
        resultVO.setAreaLocatorMap(areaLocatorMap);

        //查询条码扩展属性
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

        //查询已投数量
        resultVO.setMtEoComponentActualMap(eoComponentActualGet(tenantId,dto));

        //班次
        HmeEoJobSnBatchVO11 hmeEoJobShift = hmeEoJobSnBatchMapper.selectJobShift(tenantId,dto.getSnLine().getJobId());
        if(Objects.nonNull(hmeEoJobShift)){
            resultVO.setHmeEoJobShift(hmeEoJobShift);
        }

        //工艺路线
        resultVO.setEoRouterMap(selectRouterMap(tenantId,dto.getSnLine()));

        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSnReworkVO> release(Long tenantId, HmeEoJobSnReworkVO4 dto) {
        //组件物料去重
        if(CollectionUtils.isNotEmpty(dto.getComponentList())){
            dto.setComponentList(dto.getComponentList().stream().distinct().collect(Collectors.toList()));
            dto.setComponentList(dto.getComponentList().stream().filter(distinctByKey(item -> item.getMaterialId()))
                    .collect(Collectors.toList()));
        }

        //校验
        releaseValidate(tenantId,dto);

        //数据准备
        HmeEoJobSnReworkVO5 releaseData = releaseDataGet(tenantId,dto);

        //事件、事件请求
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");

        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        releaseData.setEventRequestId(eventRequestId);
        releaseData.setEventId(eventId);
        releaseData.setDto(dto);

        //正常料投料
        Boolean normalExecApiFlag = false;
        HmeEoJobSnSingleVO4 normalResultVO = normalRelease(tenantId, releaseData);
        if (Objects.nonNull(normalResultVO)) {
            normalExecApiFlag = normalResultVO.getExecReleaseFlag();
        }
        // 升级物料
        List<HmeEoJobSnVO21> upGradeMaterialLotList = new ArrayList<>();

        //返回值
        List<HmeEoJobSnReworkVO> resultVOList = dto.getComponentList();

        //执行API
        if (normalExecApiFlag) {
            // 查询出升级物料
            for (HmeEoJobSnReworkVO component : dto.getComponentList()) {
                for (HmeEoJobSnReworkVO3 componentMaterialLot : component.getMaterialLotList()) {
                    //记录序列号物料条码
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
            //消耗物料批
            for (MtMaterialLotVO1 value : normalResultVO.getMaterialLotConsumeMap().values()) {
                mtMaterialLotRepository.materialLotConsume(tenantId, value);
            }
            //批量装配API参数
            MtAssembleProcessActualVO16 mtAssembleProcessActualVO16 = new MtAssembleProcessActualVO16();
            MtAssembleProcessActualVO17 mtAssembleProcessActualVO17 = new MtAssembleProcessActualVO17();
            List<MtAssembleProcessActualVO17> eoInfoList = new ArrayList<>();
            //组装批量装配API参数
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
            //调用批量装配API
            mtAssembleProcessActualRepository.componentAssembleBatchProcess(tenantId, mtAssembleProcessActualVO16);

            //调用批量事务API
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

            int count = 0;
            // 删除批次/时效类型投料记录
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
            //新增批次/时效类型投料记录
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
                // 20210922 add by sanfeng.zhang for hui.gu 新增泵浦源位置
                this.savePumpPosition(tenantId, eoJobMaterialInsertDataList, dto);
            }

            //重新设置勾选条码个数和勾选条码数量
            resultVOList.forEach(item -> {
                //移除已失效条码
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

        // 对升级物料进行升级
        if (CollectionUtils.isNotEmpty(upGradeMaterialLotList)) {
            // 工单管理员在值集内且不是售后工单 则进行升级
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
        //返回值
        return resultVOList;
    }

    /**
     * 新增泵浦源组合投料位置
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
            // 如果是旧条码 找到对应的旧条码对应的eo 工单 （返修作业平台 返回的是新eo及工单）
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
            // 查询SN下泵浦源模块位置空缺值  最多到26 根据值集过滤掉已存在 正序取出位置
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
            // 批量插入
            hmePumpModPositionHeaderRepository.myBatchInsert(headerList);
        }
        // 保存行
        if (CollectionUtils.isNotEmpty(lineList)) {
            hmePumpModPositionLineRepository.batchInsertSelective(lineList);
        }
    }

    /**
     * 投料退回数据准备
     *
     * @param tenantId  租户ID
     * @param dto       退料参数
     * @return HmeEoJobSnVO9
     */
    private HmeEoJobSnReworkVO6 releaseBackDataGet(Long tenantId, HmeEoJobSnVO9 dto, HmeEoJobSnBatchVO20 hmeEoJobSnBatchVO20){
        HmeEoJobSnReworkVO6 resultVO = new HmeEoJobSnReworkVO6();
        //查询工单扩展属性
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
        //查询事务类型
        Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap = selectTransactionType(tenantId);
        //set返回结果
        resultVO.setWoExtendAttrMap(woExtendAttrMap);
        resultVO.setWmsTransactionTypeMap(wmsTransactionTypeMap);
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto) {
        //校验
        dto.setReworkSourceFlag(YES);
        HmeEoJobSnBatchVO20 hmeEoJobSnBatchVO20 = hmeEoJobSnBatchValidateService.releaseBackValidate(tenantId,dto);
        //退料物料批
        HmeEoJobSnBatchVO21 backMaterialLot = hmeEoJobSnBatchVO20.getBackMaterialLot();
        //退料物料批扩展属性
        Map<String,String> materialLotAttrMap = hmeEoJobSnBatchVO20.getMaterialLotAttrMap();
        //数据准备
        HmeEoJobSnReworkVO6 queryDataResultVO = releaseBackDataGet(tenantId,dto,hmeEoJobSnBatchVO20);
        //工单扩展属性
        Map<String,String> woExtendAttrMap = queryDataResultVO.getWoExtendAttrMap();
        //事务类型
        Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap = queryDataResultVO.getWmsTransactionTypeMap();

        //生成事件、事件请求
        MtEventCreateVO wipReturnEventCreateVO = new MtEventCreateVO();
        wipReturnEventCreateVO.setEventTypeCode("WIP_RETURN");
        String wipReturnEventId = mtEventRepository.eventCreate(tenantId, wipReturnEventCreateVO);
        String wipReturnEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_RETURN");

        Long userId = -1L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        String batchId = Utils.getBatchId();

        //当前退料数量
        BigDecimal currComponentReleaseBackQty = dto.getBackQty();

        // 工位在值集内的、且工位所在工段维护了退料货位，则退料到工位所在工段下的退料货位
        // 若工位在值集内，工位所在工段没有维护退料货位，则退料到当前工位对应工段下的配送货位
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getCurrentWorkcellId());
        List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.WORKCELL_ISSUE_RETURN_EX", tenantId);
        Optional<LovValueDTO> firstOpt = lovValue.stream().filter(lov -> StringUtils.equals(mtModWorkcell.getWorkcellCode(), lov.getValue())).findFirst();
        String locatorId = backMaterialLot.getLocatorId();
        String locatorCode = backMaterialLot.getLocatorCode();
        String warehouseId = backMaterialLot.getAreaLocatorId();
        String warehouseCode = backMaterialLot.getAreaLocatorCode();
        if (firstOpt.isPresent()) {
            //查询工位对应工段下的退料货位
            List<MtModLocator> relBackLocatorList = hmeEoJobSnReWorkMapper.queryReleaseBackLocator(tenantId, mtModWorkcell.getWorkcellId(), dto.getSiteId());
            if (CollectionUtils.isEmpty(relBackLocatorList)) {
                // 查询工位对应工段下配送货位
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
                // 根据货位查询对应仓库
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
                // 根据货位查询对应仓库
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(relBackLocatorList.get(0).getParentLocatorId());
                warehouseId = mtModLocator.getLocatorId();
                warehouseCode = mtModLocator.getLocatorCode();

            }

            // 判断退料 物料批次码 是否还有库存
            if (Objects.nonNull(backMaterialLot) && backMaterialLot.getPrimaryUomQty()>0) {
                // 判断库存是否一致  有库存且库存不一致时报错
                if (!locatorId.equals(backMaterialLot.getLocatorId()) ) {
                    throw new MtException("HME_RELAESE_BACK_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_RELAESE_BACK_001", HME, backMaterialLot.getMaterialLotCode()));
                }
            }

        }

        //构建现有量API参数
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(locatorId);
        mtInvOnhandQuantityVO9.setMaterialId(dto.getMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(currComponentReleaseBackQty.doubleValue());
        mtInvOnhandQuantityVO9.setEventId(wipReturnEventId);
        mtInvOnhandQuantityVO9.setLotCode(backMaterialLot.getLot());

        // 查询工单拆机记录
        String workOrderId = dto.getWorkOrderId();
        if (StringUtils.isBlank(workOrderId)){
            //未传入工单信息,请检查!
            throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0002", "HME"));
        }

        HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, dto.getWorkOrderNum());
        String transactionCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT;
        String trxWorkOrderNum = dto.getCurrentWorkOrderNum();
        String trxInsideOrder = null;
        // 判断工单是否存在于返修拆机记录中
        if (!Objects.isNull(splitRecord)){
            //判断内部订单号是否为空
            if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())){
                transactionCode = HmeConstants.TransactionTypeCode.WMS_INSDID_ORDER_S_R;
                trxInsideOrder = splitRecord.getInternalOrderNum();
                trxWorkOrderNum = "";
            }
        }

        //事务API
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
        objectTransactionVO.setMoveReason("自制件返修");
        objectTransactionVO.setRemark("自制件返修");
        objectTransactionVO.setTransactionTypeCode(transactionCode);
        objectTransactionVO.setMoveType(wmsTransactionTypeMap.get(transactionCode).getMoveType());
        objectTransactionVO.setTransactionQty(currComponentReleaseBackQty);
        objectTransactionRequestList.add(objectTransactionVO);

        // 升级物料 将退料物料批信息还原
        List<MtExtendVO5> attrList = new ArrayList<>();
        Boolean upgradeSn = false;
        if (YES.equals(dto.getUpgradeFlag()) && HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            // 工单管理员在值集内且不是售后工单 则进行升级
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
                // 还原条码上的物料为之前投料上的物料
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

                // 20210412 add by sanfeng.zhang for fang.pan 更新扩展字段 置为空
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
            //退料条码更新
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
        //当前物料投料记录更新
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //序列号物料
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

            //更新关系（记录）表is_issued
            existsHmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ZERO);
            if (Objects.nonNull(existsHmeEoJobMaterial.getRemainQty())) {
                existsHmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty().subtract(dto.getBackQty()));
            }
            existsHmeEoJobMaterial.setMaterialLotId(null);
            existsHmeEoJobMaterial.setMaterialLotCode(null);
            hmeEoJobMaterialMapper.updateByPrimaryKey(existsHmeEoJobMaterial);

            //组合SN退料删除泵浦源投料位置信息 先判断是否是组合SN
            List<HmePumpCombVO> pumpCombVOList = hmeEoJobSnBatchMapper.queryPumpCombListByMaterialLotId(tenantId, backMaterialLot.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                // 如果是旧条码 找到对应的旧条码对应的eo 工单 （返修作业平台 返回的是新eo及工单）
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

            //根据泵浦源物料批Id查询数据，将找到的第一笔数据的泵浦源物料批、泵浦源物料清空
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
            //2021-09-07 17:18 add by chaonan.hu for wenxin.zhang 更新与删除物料属于同一筛选顺序的筛选明细数据的状态
            List<String> selectionDetailsIdList = hmeEoJobPumpCombMapper.getSameSelectionOrderDetailsIdByMaterialLotId(tenantId, backMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(selectionDetailsIdList)){
                CustomUserDetails currentUser = DetailsHelper.getUserDetails();
                hmeEoJobPumpCombMapper.updatePumbSelectionDetailsStatus(tenantId, selectionDetailsIdList, userId, "RETURNED");
            }
        } else {
            //批次/时效物料
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
            //更新投料记录表release_qty、remain_qty
            existsHmeEoJobSnLotMaterial.setReleaseQty((Objects.isNull(existsHmeEoJobSnLotMaterial.getReleaseQty()) ? BigDecimal.ZERO : existsHmeEoJobSnLotMaterial.getReleaseQty()).subtract(dto.getBackQty()));
            if (Objects.nonNull(existsHmeEoJobSnLotMaterial.getRemainQty())) {
                existsHmeEoJobSnLotMaterial.setRemainQty(existsHmeEoJobSnLotMaterial.getRemainQty().subtract(dto.getBackQty()));
            }
            hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(existsHmeEoJobSnLotMaterial);
        }
        if (StringUtils.equals(dto.getEoId(), dto.getSiteInEoId())) {
            // eoId 一致时 校验eoStepId和工艺operationId要一致 否则报错
            if (!StringUtils.equals(dto.getEoStepId(), dto.getSiteInEoStepId()) || !StringUtils.equals(dto.getOperationId(), dto.getBackOperationId())) {
                throw new MtException("HME_SPLIT_RECORD_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SPLIT_RECORD_0030", "HME", mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : ""));
            }
            //EO装配取消
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
            // 调用装配取消API
            mtAssembleProcessActualRepository.componentAssembleCancelProcess(tenantId, mtAssembleProcessActualVO5);
        }

        //调用现有量API
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
        //调用事务API
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        HmeEoJobSnVO9 resultVO = dto;
        resultVO.setReleaseQty(resultVO.getReleaseQty().subtract(dto.getBackQty()));
        // 20210529 add by sanfeng.zhang for fang.pan 返回当前Eo的装配
        resultVO.setAssembleQty(hmeEoJobSnReWorkMapper.queryAssembleQty(tenantId, dto.getSiteInEoId(), dto.getSiteInEoStepId(), dto.getMaterialId()));
        return resultVO;
    }

    /**
     * 批量工序作业平台-条码绑定校验
     *
     * @param tenantId 租户Id
     * @param dto 参数
     * @return HmeEoJobSnReworkVO
     */
    private HmeEoJobSnReworkVO2 releaseScanValidate(Long tenantId, HmeEoJobSnReworkDTO dto){
        HmeEoJobSnReworkVO2 resultVO = new HmeEoJobSnReworkVO2();
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            // 扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        HmeEoJobSnReworkVO3 mtMaterialLot = hmeEoJobSnReWorkMapper.selectMaterialLot(tenantId,dto.getMaterialLotCode());
        if(Objects.isNull(mtMaterialLot)){
            //扫描条码不存在
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
            //条码${1}已失效,请检查!
            throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0004", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
            // 条码号【${1}】不为OK状态,请核实所录入条码
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //盘点冻结标识校验
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())){
            // 物料批${1}已被冻结,不可对物料批进行操作!
            throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0005", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //盘点停用标识校验
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
            // 物料批${1}正在被盘点,不可对物料批进行操作!
            throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0006", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        //查询物料类型
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());
        HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4 = new HmeEoJobSnBatchVO4();
        Boolean addComponentFlag = false;
        if (CollectionUtils.isNotEmpty(dto.getComponentList())) {
            List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> item.getMaterialId().equals(mtMaterialLot.getMaterialId())).collect(Collectors.toList());
            // 如果不在bom内 则新增一条
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
                //当前条码已绑定其他SN号【${1}】
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
                    //当前条码已与工位【${1}】绑定
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
                    //当前条码已与工位【${1}】绑定
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

        //库位校验
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //物料站点关系
        MtMaterialSite mtMaterialSitePara = new MtMaterialSite();
        mtMaterialSitePara.setTenantId(tenantId);
        mtMaterialSitePara.setSiteId(dto.getSiteId());
        mtMaterialSitePara.setMaterialId(mtMaterialLot.getMaterialId());
        mtMaterialSitePara.setEnableFlag(YES);
        MtMaterialSite mtMaterialSite = mtMaterialSiteRepository.selectOne(mtMaterialSitePara);
        if(Objects.isNull(mtMaterialSite)){
            //物料站点关系${1}不存在或不为有效状态,请检查!${2}
            throw new MtException("MT_INVENTORY_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0029", "INVENTORY","",""));
        }

        //查询条码扩展属性
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

        //在制品校验
        if(HmeConstants.ConstantValue.YES.equals(materialLotExtendAttrMap.getOrDefault("MF_FLAG",""))){
            //当前物料仍为在制品,尚未加工完成,无法进行投料
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

            //查询已投
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
        //校验
        HmeEoJobSnReworkVO2 releaseScanValidateResultVO = releaseScanValidate(tenantId,dto);
        HmeEoJobSnReworkVO3 mtMaterialLot = releaseScanValidateResultVO.getMaterialLot();

        resultVO.setComponent(releaseScanValidateResultVO.getComponent());
        if(YES.equals(releaseScanValidateResultVO.getDeleteFlag())){
            //2021-11-09 14:20 edit by chaonan.hu for wenxin.zhang 根据物料批ID查询表hme_eo_job_pump_comb中的数据
            HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombMapper.eoJobPumpCombQueryByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            if(Objects.nonNull(hmeEoJobPumpComb)){
                //如果能找到组合物料批，且snLineList中materialLotId不等于组合物料批CombMaterialLotId则报错
                if(CollectionUtils.isNotEmpty(dto.getSnLineList())
                        && !hmeEoJobPumpComb.getCombMaterialLotId().equals(dto.getSnLineList().get(0).getMaterialLotId())){
                    //当前条码已绑定其他SN号【${1}】
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
            // 20210831 add by sanfeng.zhang for hui.gu 组合SN 显示功率和电压值 先判断是否为组合SN
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
                //当前扫描条码已绑定其它进站的SN
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
            //未查询到区域库位
            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0012",WmsConstant.ConstantValue.WMS, mtMaterialLot.getLocatorId()));
        }

        //返回结果
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
                //新增
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
                //更新
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
                //新增
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
                //更新
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
                //新增
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
                //更新
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
            // 20210831 add by sanfeng.zhang for hui.gu 组合SN 显示功率和电压值 先判断是否为组合SN
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
            //泵浦源工序作业平台独有的逻辑
            HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8 = new HmeEoJobSnBatchVO8();
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getSnLineList().get(0).getJobId());
            hmeEoJobSnBatchVO8.setHmeEoJobSn(hmeEoJobSn);
            //组合物料是否维护了筛选规则及是否在泵浦源组合中
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
        //条码排序
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
     * @Description 构造返回数据
     *
     * @author yuchao.wang
     * @date 2020/11/22 14:24
     * @param tenantId 租户ID
     * @param hmeEoJobSnSingleBasic 参数
     * @param dto 参数
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

        //查询当前步骤/下一步骤
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
     * @Description 出站校验
     *
     * @author yuchao.wang
     * @date 2020/11/21 15:11
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn outSiteValidate(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        HmeMaterialLotVO3 materialLot = hmeEoJobSnSingleBasic.getMaterialLot();
        HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnSingleBasic.getHmeEoJobSnEntity();

        //V20210630 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可出站
        if(StringUtils.isNotBlank(materialLot.getStocktakeFlag()) &&
                HmeConstants.ConstantValue.YES.equals(materialLot.getStocktakeFlag())){
            //此SN【${1}】正在盘点,不可出站
            throw new MtException("HME_EO_JOB_SN_204", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_204", "HME", materialLot.getMaterialLotCode()));
        }
        // 20210721 modify by sanfeng.zhang for xenxin.zhang 出站校验冻结标识为Y的SN不可出站
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getFreezeFlag())) {
            //【此SN【${1}】正在冻结,不可出站】
            throw new MtException("HME_EO_JOB_SN_207", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_207", "HME", materialLot.getMaterialLotCode()));
        }

        //质量状态为NG，进行返修相关校验
        if (HmeConstants.ConstantValue.NG.equals(materialLot.getQualityStatus())) {
            if (!HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag())
                    && !HmeConstants.ConstantValue.YES.equals(hmeEoJobSnEntity.getReworkFlag())) {
                //正常加工过程中不允许不良产品出站
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
                //请先返修进站
                throw new MtException("HME_EO_JOB_SN_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_061", "HME"));
            }
        }

        //校验EoJobSn是否已经出站，有则报错
        if (Objects.nonNull(hmeEoJobSnEntity.getSiteOutDate())) {
            // 	已完成SN号不允许重复完成
            throw new MtException("HME_EO_JOB_SN_056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_056", "HME"));
        }

        // 判断是否需要校验数据采集结果
        boolean dataRecordValidateFlag = true;
        if (dto.getOutSiteAction() != null) {
            boolean processValidateFlag = hmeEoJobSnCommonService.queryProcessValidateFlag(tenantId, dto.getWorkcellId());
            if (HmeConstants.OutSiteAction.REWORK.equals(dto.getOutSiteAction())) {
                if (HmeConstants.ConstantValue.NO.equals(materialLot.getReworkFlag())) {
                    // 只有返修中的SN可以点检继续返修按键，正常加工及无需继续返修的产品请点击加工完成按键
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
                        // 20211109 modify by sanfeng.zhang for peng.zhao 器件测试和反射镜不提示
                        if (!hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId()) && !hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId())) {
                            // 产品将返修完成，后续以正常加工的方式继续生产，是否确认
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

        //校验数据采集项
        if (dataRecordValidateFlag
                && hmeEoJobSnCommonService.hasMissingValue(tenantId, dto.getWorkcellId(), Collections.singletonList(dto.getJobId()))) {
            // 出站失败,存在未记录结果的数据采集记录
            throw new MtException("HME_EO_JOB_SN_009", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_009", "HME"));
        }
        // 校验数据采集项不良判定
        if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag()) || !"HME_EO_JOB_SN_172".equals(dto.getErrorCode())) {
            //查询所有有结果的数据采集项
            List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobDataRecordRepository.queryForNcRecordValidate(tenantId, dto.getWorkcellId(), dto.getJobId());
            if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                HmeEoJobSn dataRecordValidateResult = null;

                HmeProcessNcHeaderVO2 processNcInfo = null;
                //根据工艺判断是器件不良还是老化不良
                if (hmeEoJobSnCommonService.isAgeingNc(tenantId, dto.getOperationId())) {
                    //老化不良
                    //查询工序信息
                    String stationId = hmeEoJobSnMapper.queryWkcStation(tenantId, dto.getSiteId(), dto.getWorkcellId());

                    String cosModel = materialLot.getMaterialLotCode().substring(4, 5);

                    processNcInfo = hmeProcessNcHeaderRepository
                            .queryProcessNcInfoForAgeingNcRecordValidate(tenantId, materialLot.getMaterialId(), stationId, cosModel , materialLot.getMaterialLotCode(), dto.getOperationId());
                    //对数据采集线校验
                    dataRecordValidateResult = hmeEoJobSnCommonService
                            .dataRecordAgeingProcessNcValidate(tenantId, materialLot.getMaterialLotCode(), hmeEoJobDataRecordList, processNcInfo);
                } else if (hmeEoJobSnCommonService.isDeviceNc(tenantId, dto.getOperationId())) {
                    //器件不良
                    //查询所有工序不良信息
                    String productCode = materialLot.getMaterialLotCode().substring(2, 4);
                    String cosModel = materialLot.getMaterialLotCode().substring(4, 5);
                    String chipCombination = materialLot.getMaterialLotCode().substring(5, 6);
                    processNcInfo = hmeProcessNcHeaderRepository
                            .queryProcessNcInfoForNcRecordValidate(tenantId, dto.getOperationId(), materialLot.getMaterialId(), productCode, cosModel, chipCombination);
                    //对数据采集项校验
                    dataRecordValidateResult = hmeEoJobSnCommonService
                            .dataRecordProcessNcValidate(tenantId, materialLot.getMaterialLotCode(), hmeEoJobDataRecordList, processNcInfo);
                } else if (hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId())) {
                    // 反射镜不良
                    //查询所有工序不良信息
                    String cosModel = materialLot.getMaterialLotCode().substring(4, 5);
                    String chipCombination = materialLot.getMaterialLotCode().substring(5, 6);
                    processNcInfo = hmeProcessNcHeaderRepository
                            .queryProcessNcInfoForReflectorNcRecordValidate(tenantId, dto.getOperationId(), materialLot.getMaterialId(), cosModel, chipCombination);
                    //对数据采集项校验
                    dataRecordValidateResult = hmeEoJobSnCommonService
                            .dataRecordReflectorProcessNcValidate(tenantId, materialLot.getMaterialLotCode(), hmeEoJobDataRecordList, processNcInfo);
                }

                if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {
                    //新增工序不良参数返回
                    dataRecordValidateResult.getHmeNcDisposePlatformDTO().setWorkcellId(dto.getWorkcellId());
                    dataRecordValidateResult.getHmeNcDisposePlatformDTO().setCurrentwWorkcellId(dto.getWorkcellId());
                    dataRecordValidateResult.getHmeNcDisposePlatformDTO().setFlag("2");
                    return dataRecordValidateResult;
                }
            }
        }

        //重新获取容器中的条码，防止前台缓存的条码已经被卸载 modify by yuchao.wang for tianyang.xie at 2020.9.12
        if (StringUtils.isNotBlank(dto.getContainerId())) {
            MtContLoadDtlVO10 contLoadDtlParam = new MtContLoadDtlVO10();
            contLoadDtlParam.setContainerId(dto.getContainerId());
            contLoadDtlParam.setAllLevelFlag(HmeConstants.ConstantValue.NO);
            List<MtContLoadDtlVO4> mtContLoadDtlVo1List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlParam);

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

        //出站容器装载校验
        if (hmeEoJobSnSingleBasic.getContainerOutSiteFlag()) {
            outSiteContainerLoadVerify(tenantId, hmeEoJobSnSingleBasic, dto);
        }
        // 20211125 add by  sanfeng.zhang for wenxin.zhang 旧条码绑定多个运行条码不允许出站
        boolean newMaterialLot = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, dto.getSnNum());
        String oldMaterialLotCode = "";
        if (!newMaterialLot) {
            oldMaterialLotCode = dto.getSnNum();
        } else {
            // 查询旧条码是否还有绑定其他状态为运行的EO
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
     * @Description 出站容器装载校验
     *
     * @author yuchao.wang
     * @date 2020/12/14 9:26
     * @param tenantId 租户ID
     * @param hmeEoJobSnSingleBasic 参数
     * @param dto 参数
     * @return void
     *
     */
    private void outSiteContainerLoadVerify(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto) {
        String containerId = hmeEoJobSnSingleBasic.getEoJobContainer().getContainerId();

        //查询当前容器信息
        HmeContainerVO containerInfo = hmeEoJobSnMapper.queryContainerInfo(tenantId, containerId);
        if (Objects.isNull(containerInfo) || StringUtils.isBlank(containerInfo.getContainerId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "容器信息"));
        }

        //校验容器可用性,容器状态需要为可下达
        if (!HmeConstants.StatusCode.CANRELEASE.equals(containerInfo.getStatus())) {
            throw new MtException("HME_LOAD_CONTAINER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_002", "HME", containerInfo.getContainerCode()));
        }

        //查询容器当前装载明细
        List<HmeContainerDetailVO> containerDetailList = hmeEoJobSnMapper.queryContainerDetails(tenantId, containerId);
        if (CollectionUtils.isEmpty(containerDetailList)) {
            return;
        }

        //校验容器是否装满,容器可装数量为空不校验
        if (Objects.nonNull(containerInfo.getCapacityQty())) {
            //当前容器中总数量
            double totalQty = containerDetailList.stream().filter(item -> Objects.nonNull(item.getPrimaryUomQty()))
                    .mapToDouble(HmeContainerDetailVO::getPrimaryUomQty).sum();

            //当前作业的EO数量
            totalQty += hmeEoJobSnSingleBasic.getEo().getQty();

            if (totalQty > containerInfo.getCapacityQty()) {
                //容器已经装满
                throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", containerInfo.getContainerCode()));
            }
        }

        //校验容器是否可以混装物料
        if (!HmeConstants.ConstantValue.YES.equals(containerInfo.getMixedMaterialFlag())) {
            //当前作业的物料
            String materialId = hmeEoJobSnSingleBasic.getMaterialLot().getMaterialId();

            //当前容器中的物料清单
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
            //当前作业的EO
            String eoId = hmeEoJobSnSingleBasic.getEo().getEoId();

            //当前容器中的EO清单
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
            //当前作业的WO
            String woId = hmeEoJobSnSingleBasic.getWo().getWorkOrderId();

            //当前容器中的WO清单
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
     * @Description 出站查询基础数据
     *
     * @author yuchao.wang
     * @date 2020/11/21 14:34
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO
     *
     */
    private HmeEoJobSnSingleBasicVO queryBasicData(Long tenantId, HmeEoJobSnVO3 dto) {
        //根据JobId查询EoJobSn数据
        SecurityTokenHelper.close();
        HmeEoJobSn hmeEoJobSnEntity = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
        if (Objects.isNull(hmeEoJobSnEntity) || StringUtils.isBlank(hmeEoJobSnEntity.getJobId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "作业信息"));
        }

        //查询EO、WO相关信息
        HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic = hmeEoJobSnCommonService.queryEoAndWoInfoWithComponentByEoId(tenantId, hmeEoJobSnEntity.getEoStepId(), dto.getEoId());
        hmeEoJobSnSingleBasic.setHmeEoJobSnEntity(hmeEoJobSnEntity);

        //查询是否容器出站
        hmeEoJobSnSingleBasic.setContainerOutSiteFlag(hmeEoJobSnCommonService.isContainerOutSite(tenantId, dto.getWorkcellId()));

        //容器出站查询容器ID
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

        //批量查询条码信息
        HmeMaterialLotVO3 materialLot = hmeEoJobSnCommonService.queryMaterialLotInfo(tenantId, dto.getMaterialLotId());
        hmeEoJobSnSingleBasic.setMaterialLot(materialLot);

        //非返修查询所有反冲料组件
        if (!HmeConstants.ConstantValue.YES.equals(materialLot.getReworkFlag()) && CollectionUtils.isNotEmpty(hmeEoJobSnSingleBasic.getEo().getBomComponentList())) {
            List<String> bomComponentIdList = hmeEoJobSnSingleBasic.getEo().getBomComponentList().stream()
                    .map(HmeBomComponentVO::getBomComponentId).distinct().collect(Collectors.toList());
            List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnLotMaterialMapper.selectBackFlash(tenantId, dto.getSiteId(), bomComponentIdList);
            hmeEoJobSnSingleBasic.setBackFlushBomComponentList(backFlushBomComponentList);
        }

        //查询最近的步骤
        HmeRouterStepVO nearStepVO = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, dto.getEoId());
        if (Objects.isNull(nearStepVO)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "最近加工步骤"));
        }
        hmeEoJobSnSingleBasic.setNearStep(nearStepVO);

        //查询工艺步骤名称
        MtRouterStep currentRouterStep = mtRouterStepRepository.routerStepGet(tenantId, hmeEoJobSnEntity.getEoStepId());
        if (Objects.isNull(currentRouterStep) || StringUtils.isBlank(currentRouterStep.getRouterStepId())) {
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "routerStep", hmeEoJobSnEntity.getEoStepId()));
        }
        hmeEoJobSnSingleBasic.setCurrentStep(currentRouterStep);

        //查询是否完成步骤
        hmeEoJobSnSingleBasic.setDoneStepFlag(mtRouterDoneStepRepository.doneStepValidate(tenantId, dto.getEoStepId()));

        //如果是完成步骤，获取批次
        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleBasic.getDoneStepFlag())) {
            //查询值集HME.NOT_CREATE_BATCH_NUM
            List<String> createBatchNumList = new ArrayList<>();
            List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.NOT_CREATE_BATCH_NUM", tenantId);
            if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                createBatchNumList = lovValueDTOList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            }

            //获取批次编码
            String itemType = hmeEoJobSnSingleBasic.getEo().getItemType();
            if (StringUtils.isBlank(itemType) || !createBatchNumList.contains(itemType)) {
                //查询系统参数 HME_MATERIAL_BATCH_NUM 作为批次
                hmeEoJobSnSingleBasic.setLotCode(profileClient.getProfileValueByOptions("HME_MATERIAL_BATCH_NUM"));
                if (StringUtils.isBlank(hmeEoJobSnSingleBasic.getLotCode())) {
                    //默认批次获取失败,请联系管理员通过【配置维护】功能维护【HME_MATERIAL_BATCH_NUM】
                    throw new MtException("HME_EO_JOB_SN_113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_113", "HME"));
                } else if(hmeEoJobSnSingleBasic.getLotCode().length() != 10){
                    //默认批次必须为10位,请联系管理员通过【配置维护】功能维护【HME_MATERIAL_BATCH_NUM】
                    throw new MtException("HME_EO_JOB_SN_114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_114", "HME"));
                }
            }

            //判断订单类型是否为MES_RK06 是则不计报工事务
            HmeWorkOrderVO2 wo = hmeEoJobSnSingleBasic.getWo();
            if ("MES_RK06".equals(wo.getWorkOrderType())) {
                //售后需要替换工单号为内部订单号或SAP单号
                HmeServiceSplitRecordVO2 serviceSplitRecordVO2 = hmeServiceSplitRecordMapper.queryOrderNumBySnNumAndWoId(tenantId, wo.getWorkOrderId(), dto.getSnNum());
                if (Objects.isNull(serviceSplitRecordVO2)) {
                    //工单未查询到拆机记录
                    throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_084", "HME"));
                }

                //如果内部订单号不为空则取内部订单号，为空取SAP单号
                HmeServiceSplitRecordVO2 orderNum = new HmeServiceSplitRecordVO2();
                if (StringUtils.isNotBlank(serviceSplitRecordVO2.getInternalOrderNum())) {
                    orderNum.setOrderNum(serviceSplitRecordVO2.getInternalOrderNum());
                    orderNum.setOrderNumType("INTERNAL_ORDER");
                } else if (StringUtils.isNotBlank(serviceSplitRecordVO2.getSapOrderNum())) {
                    orderNum.setOrderNum(serviceSplitRecordVO2.getSapOrderNum());
                    orderNum.setOrderNumType("SAP_ORDER");
                } else {
                    //工单未查询到内部订单号及维修工单号
                    throw new MtException("HME_EO_JOB_SN_085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_085", "HME"));
                }
                hmeEoJobSnSingleBasic.setRk06InternalOrderNum(orderNum);
            }
        }

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        hmeEoJobSnSingleBasic.setUserId(userId);
        hmeEoJobSnSingleBasic.setCurrentDate(new Date());

        //V20210312 modify by penglin.sui for tianyang.xie 判断是否存在不良的数据采集项
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
     * @Description 默认输入校验
     *
     * @author yuchao.wang
     * @date 2020/11/21 14:09
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
        if (StringUtils.isBlank(dto.getOutSiteAction())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "出站动作"));
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
     * @Description 正常料投料
     *
     * @author penglin.sui
     * @date 2020/12/16 11:00
     * @param tenantId 租户ID
     * @param releaseData 投料需要的数据
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
        //是否执行投料
        Boolean execReleaseFlag = false;

        HmeEoJobSnVO snLine = releaseData.getDto().getSnLine();

        // 查询工单拆机记录
        String workOrderId = snLine.getWorkOrderId();
        if (StringUtils.isBlank(workOrderId)){
            //未传入工单信息,请检查!
            throw new MtException("HME_EO_JOB_REWORK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0002", "HME"));
        }
        HmeServiceSplitRecord splitRecord = hmeEoJobSnReWorkMapper.lastServiceSplitRecordGet(tenantId, snLine.getWorkOrderNum());
        String transactionCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
        String trxWorkOrderNum = snLine.getWorkOrderNum();
        String trxInsideOrder = null;
        // 判断工单是否存在于返修拆机记录中
        if (!Objects.isNull(splitRecord)){
            //判断内部订单号是否为空
            if (StringUtils.isNotBlank(splitRecord.getInternalOrderNum())){
                transactionCode = HmeConstants.TransactionTypeCode.WMS_INSDID_ORDER_S_I;
                trxInsideOrder = splitRecord.getInternalOrderNum();
                trxWorkOrderNum = "";
            }
        }

        for (HmeEoJobSnReworkVO component : releaseData.getDto().getComponentList()
        ) {
            //查询当前物料要投料的条码
            List<HmeEoJobSnReworkVO3> materialLotList = component.getMaterialLotList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                    && HmeConstants.ConstantValue.YES.equals(item.getEnableFlag())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(materialLotList)) {
                continue;
            }
            // 获取条码总数量
            BigDecimal sumPrimaryUomQty = materialLotList.stream().map(HmeEoJobSnReworkVO3::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            //净需求数：返修 = 前台传的将投数量
            BigDecimal currentReleaseQty = component.getWillReleaseQty();
            if (currentReleaseQty == null || currentReleaseQty.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            //当前剩余投料数
            BigDecimal remainReleaseQty = currentReleaseQty;
            int loopCount = 0;
            // 对比投料数量
            if (remainReleaseQty.compareTo(sumPrimaryUomQty) > 0){
                //物料【${1}】投料数量【${2}】不能超过对应条码数据量总和【${3}】,请检查!
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
                // 扣减剩余投料数
                remainReleaseQty = remainReleaseQty.subtract(materialLotCurrentReleaseQty);

                MtAssembleProcessActualVO11 mtAssembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                mtAssembleProcessActualVO11.setMaterialId(component.getMaterialId());
                mtAssembleProcessActualVO11.setAssembleExcessFlag(YES);
                mtAssembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                mtAssembleProcessActualVO11.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                mtAssembleProcessActualVO11.setTrxAssembleQty(materialLotCurrentReleaseQty.doubleValue());
                materialInfoList.add(mtAssembleProcessActualVO11);

                //记录物料条码EO生产投料事务
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
                    //工单${1}不存在,请检查!
                    throw new MtException("HME_COS_CHIP_IMP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_CHIP_IMP_0020", "HME", trxWorkOrderNum));
                }
                objectTransactionVO.setWorkOrderNum(trxWorkOrderNum);
                objectTransactionVO.setInsideOrder(trxInsideOrder);
                objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                objectTransactionVO.setProdLineCode(snLine.getProdLineCode());
                objectTransactionVO.setMoveType(releaseData.getWmsTransactionTypeMap().get(transactionCode).getMoveType());
                objectTransactionVO.setMoveReason("自制件返修");
                objectTransactionVO.setRemark("自制件返修");
                objectTransactionVO.setSoNum(releaseData.getMaterialLotExtendAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.SO_NUM), ""));
                objectTransactionVO.setSoLineNum(releaseData.getMaterialLotExtendAttrMap().getOrDefault(fetchGroupKey2(componentMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.SO_LINE_NUM), ""));
                objectTransactionRequestList.add(objectTransactionVO);

                //记录要更新条码的信息
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

                //新增批次/时效投料记录
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

                //修改数量,返回给前台
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
            //工位不能为空!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if (StringUtils.isBlank(dto.getSnNum())) {
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        List<HmeEoJobSnVO9> allBackMaterialList = new ArrayList<>();
        // 20210411 add by sanfeng.zhang for fang.pan 根据当前SN 找到top_eo_id下所有的作业 按jobId 分组
        List<HmeEoJobSnVO9> eoJobSnVO9List = hmeEoJobMaterialMapper.queryReleaseEoJobSnBySnNumOfRework(tenantId, dto.getSnNum());
        Map<String, List<HmeEoJobSnVO9>> eoJobSnVO9Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoJobSnVO9List)) {
            eoJobSnVO9Map = eoJobSnVO9List.stream().collect(Collectors.groupingBy(HmeEoJobSnVO9::getJobId));
        }
        List<String> jobIdList = eoJobSnVO9List.stream().map(HmeEoJobSnVO9::getJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //序列号物料
            List<HmeEoJobSnVO9> backMaterialList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(jobIdList)) {
                backMaterialList = hmeEoJobMaterialMapper.selectReleaseEoJobMaterialOfRework2(tenantId, dto, jobIdList);
            }
            if(CollectionUtils.isNotEmpty(backMaterialList)){
                // 根据jobId获取作业信息
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
                //批次物料、时效物料
                backMaterialList = hmeEoJobSnLotMaterialMapper.selectReleaseEoJobSnLotMaterialOfRework2(tenantId, dto, jobIdList);
            }
            if (CollectionUtils.isNotEmpty(backMaterialList)) {
                // 根据jobId获取作业信息
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
     * 构建eo返修关系
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/22 10:51
     */
    @Override
    public void createEoRel(Long tenantId, HmeEoJobSnVO3 dto, String eoId) {
        // 查询hme_eo_rel 是否存在  不存在则插入
        List<HmeEoRel> hmeEoRelList = hmeEoRelRepository.select(new HmeEoRel() {{
            setTenantId(tenantId);
            setEoId(eoId);
        }});
        if (CollectionUtils.isEmpty(hmeEoRelList)) {
            HmeEoRel hmeEoRel = new HmeEoRel();
            hmeEoRel.setTenantId(tenantId);
            hmeEoRel.setEoId(eoId);
            // 获取顶层EOId
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto.getSnNum());
            }});
            // 获取条码扩展字段TOP_EO_ID
            List<MtExtendAttrVO> topEoIdAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_material_lot_attr");
                setAttrName("TOP_EO_ID");
                setKeyId(mtMaterialLot.getMaterialLotId());
            }});

            // 返修eoId 都为新eoId 查询对应的返修条码
            List<MtExtendAttrVO> reworkMaterialLotList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_eo_attr");
                setAttrName("REWORK_MATERIAL_LOT");
                setKeyId(eoId);
            }});
            String reworkMaterialLot = CollectionUtils.isNotEmpty(reworkMaterialLotList) ? reworkMaterialLotList.get(0).getAttrValue() : "";
            List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                    .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtEo.FIELD_IDENTIFICATION, reworkMaterialLot)).build());
            // 取不到就取条码的eoId 条码eoId为空 则自身就是顶层eoId
            if (CollectionUtils.isNotEmpty(topEoIdAttrList) && StringUtils.isNotBlank(topEoIdAttrList.get(0).getAttrValue())) {
                hmeEoRel.setTopEoId(topEoIdAttrList.get(0).getAttrValue());
            } else if (StringUtils.isNotBlank(reworkMaterialLot) && CollectionUtils.isNotEmpty(mtEoList)) {
                hmeEoRel.setTopEoId(mtEoList.get(0).getEoId());
            }  else if (StringUtils.isNotBlank(mtMaterialLot.getEoId())) {
                hmeEoRel.setTopEoId(mtMaterialLot.getEoId());
            } else {
                hmeEoRel.setTopEoId(eoId);
            }
            // 根据顶层EO查询 是否有自身关联的数据 不存在 则新建一条
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
            // 不是从条码扩展字段获取的 则更新进站条码的顶层eoId
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
                //当前条码【${1}】已投料,无法进行条码解绑
                throw new MtException("HME_EO_JOB_SN_077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_077", "HME", deleteMaterialLotList.get(0).getMaterialLotCode()));
            }
            hmeEoJobMaterialMapper.deleteByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
            //根据泵浦源物料批Id查询数据，将找到的第一笔数据的泵浦源物料批、泵浦源物料清空
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
            //2021-09-07 17:18 add by chaonan.hu for wenxin.zhang 更新与删除物料属于同一筛选顺序的筛选明细数据的状态
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
        //批次物料
        List<HmeEoJobSnSingleVO5> lotVOList = hmeEoJobLotMaterialMapper.selectWkcBindJobMaterial(tenantId, workcellId, siteId);
        if(CollectionUtils.isNotEmpty(lotVOList)) {
            lotVOList = lotVOList.stream().filter(item -> !"2".equals(item.getBackflushFlag()) && HmeConstants.MaterialTypeCode.LOT.equals(item.getProductionType()))
                    .collect(Collectors.toList());
            allVOList.addAll(lotVOList);
        }
        //时效物料
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
     * 返修工单-物料升级
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/9 14:37
     */
    private void snReworkBatchUpgrade(Long tenantId, HmeEoJobSnVO20 dto) {
        log.debug("<==============物料升级begin==============>");

        if (Objects.isNull(dto)) {
            return;
        }

        if (CollectionUtils.isEmpty(dto.getMaterialLotList())) {
            return;
        }

        //批量查询物料站点
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
            //未找到匹配的物料站点信息
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_033", "HME"));
        }
        Map<String, String> materialSiteMap = materialSiteList.stream().collect(Collectors.toMap(MtMaterialSiteVO4::getMaterialSiteId, MtMaterialSiteVO4::getMaterialId));
        List<String> materialSiteIdList = materialSiteList.stream().map(MtMaterialSiteVO4::getMaterialSiteId).collect(Collectors.toList());
        List<String> attrNamelist = new ArrayList<>();
        attrNamelist.add("attribute17");
        //批量查询升级标识
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
                    // 当前工艺步骤【${1}】SN升级标志属性的物料应有且只有1个,请检查物料【${2}】的扩展属性
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
            log.debug("物料升级标识处理");
            MtMaterialLot currentSn =
                    mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getSnMaterialLotId());
            final Date currentDate = CommonUtils.currentTimeGet();

            String upgradeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setLocatorId(currentSn.getLocatorId());
                    setEventTypeCode("EO_MATERIAL_LOT_UPGRADE");
                }
            });

            // 更新进站条码的有效性为N
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
            //V20200826 modify by penglin.sui for tianyang.xie 升级时需要重新查询物料
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

            // 物料批扩展表-扩展属性更新
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
                // 状态
                if (HmeConstants.ExtendAttr.STATUS.equals(extendAttr.getAttrName())) {
                    statusAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // 在制品标识
                if ("MF_FLAG".equals(extendAttr.getAttrName())) {
                    mfFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // 返修标识
                if ("REWORK_FLAG".equals(extendAttr.getAttrName())) {
                    reworkFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // 性能等级
                if ("PERFORMANCE_LEVEL".equals(extendAttr.getAttrName())) {
                    pfmLevelAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // 顶层EO
                if ("TOP_EO_ID".equals(extendAttr.getAttrName())) {
                    toEoIdAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // 翻新标记
                if ("RENOVATE_FLAG".equals(extendAttr.getAttrName())) {
                    renovateFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // 返修SN来源ID
                if ("SOURCE_REPAIRSN_ID".equals(extendAttr.getAttrName())) {
                    sourceRepairAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // 售后拆机生成条码标志
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
            // 更新物料批扩展字段
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

            // eo更新
            String eoUpdateEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setLocatorId(currentSn.getLocatorId());
                    setEventTypeCode("EO_UPDATE");
                }
            });

            // 更新eo扩展字段REWORK_MATERIAL_LOT
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("REWORK_MATERIAL_LOT");
            mtExtendVO5.setAttrValue(upGradeMaterialLot.getMaterialLotCode());
            mtExtendVO5List.add(mtExtendVO5);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_eo_attr", dto.getEoId(), eoUpdateEventId, mtExtendVO5List);

            //V20201005 modify by penglin.sui for for lu.bai 更新hme_eo_job_sn 的material_lot_id
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnMapper.selectByPrimaryKey(dto.getJobId());
            if (Objects.isNull(hmeEoJobSn)) {
                //${1}不存在 请确认${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "eoJobSn", dto.getJobId()));
            }
            hmeEoJobSn.setMaterialLotId(materialLotResult.getMaterialLotId());
            hmeEoJobSnMapper.updateByPrimaryKey(hmeEoJobSn);
        }
    }


    private String getVirtualFlag(Long tenantId, String eoId, String operationId, String materialId){
        //根据工艺路线名称查询工艺路线步骤
        List<String> routerStepIdList = hmeEoJobSnReWorkMapper.getRouterStepIdByEoId(tenantId, eoId);
        if (CollectionUtils.isNotEmpty(routerStepIdList)) {
            //根据工步、工艺查询Bom组件
            List<MtBomComponent> mtBomComponentList
                    = hmeEoJobSnReWorkMapper.getBomComponentIdByRouterStepAndOperation(tenantId, operationId, routerStepIdList);
            if(CollectionUtils.isNotEmpty(mtBomComponentList)){
                //如果找到的Bom组件中组件物料没有与扫描条码物料一致的，则报错请先查询投料组件清单
                mtBomComponentList = mtBomComponentList.stream().filter(item -> materialId.equals(item.getMaterialId())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(mtBomComponentList)){
                    throw new MtException("HME_EO_JOB_SN_128", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_128", "HME"));
                }
                String bomComponentId = mtBomComponentList.get(0).getBomComponentId();
                //查询BOM扩展属性
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
