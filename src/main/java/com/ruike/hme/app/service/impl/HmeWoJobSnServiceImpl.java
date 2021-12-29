package com.ruike.hme.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.app.service.HmeWoJobSnService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeCosPatchPdaVO9;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ExportUtil;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.*;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.vo.MtRouterStepVO5;
import tarzan.method.infra.mapper.MtRouterOperationMapper;
import tarzan.method.infra.mapper.MtRouterStepMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModLocatorOrgRel;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static io.tarzan.common.domain.util.MtBaseConstants.QUALITY_STATUS.OK;

/**
 * wo工艺作业记录表应用服务默认实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-12 10:38:22
 */
@Service
public class HmeWoJobSnServiceImpl implements HmeWoJobSnService {

    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;
    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private MtModLocatorOrgRelRepository MtModLocatorOrgRelRepository;

    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private WmsCommonServiceComponent commonServiceComponent;

    @Autowired
    private MtContainerRepository containerRepository;

    @Autowired
    private MtExtendSettingsRepository extendSettingsRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository woComActualRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private MtRouterStepMapper mtRouterStepMapper;

    @Autowired
    private MtRouterOperationMapper mtRouterOperationMapper;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private  MtMaterialLotHisRepository mtMaterialLotHisRepository;

    @Autowired
    private HmeCosCommonService hmeCosCommonService;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Autowired
    private HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;

    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;

    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    private static final String INCOMING_FILE_NAME = "来料录入";
    private static final String INCOMING_SHEET_NAME = "来料录入";


    private static final String[] IGNORE_TABLE_FIELDS = new String[]{MtMaterialLot.FIELD_IDENTIFICATION,
            AuditDomain.FIELD_OBJECT_VERSION_NUMBER, AuditDomain.FIELD_LAST_UPDATED_BY, AuditDomain.FIELD_CREATED_BY,
            AuditDomain.FIELD_CREATION_DATE, AuditDomain.FIELD_LAST_UPDATE_DATE};
    private final String ATTR_TABLE = "mt_material_lot_attr";

    /**
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO>
     * @description 获取工单数据
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/12 16:27
     */
    @Override
    public Page<HmeWoJobSnReturnDTO> workList(Long tenantId, HmeWoJobSnDTO dto, PageRequest pageRequest) {
        Page<HmeWoJobSnReturnDTO> result = PageHelper.doPageAndSort(pageRequest, () -> hmeWoJobSnRepository.workList(tenantId, dto));
        return result;
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO2
     * @description 获取剩余数量
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/24 9:50
     **/
    @Override
    @ProcessLovValue
    public HmeWoJobSnReturnDTO2 remainingQty(Long tenantId, HmeWoJobSnDTO2 dto) {
        HmeWoJobSnReturnDTO2 hmeWoJobSnReturnDTO2 = new HmeWoJobSnReturnDTO2();
        //获取剩余数量
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
        hmeCosOperationRecord.setWorkOrderId(dto.getWorkOrderId());
        hmeCosOperationRecord.setOperationId(dto.getOperationId());
        List<HmeCosOperationRecord> selects = hmeCosOperationRecordRepository.select(hmeCosOperationRecord);
        Long a = 0L;
        for (HmeCosOperationRecord select : selects) {
            a += select.getCosNum();
        }
        //获取值集数据
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
        List<LovValueDTO> itemGroup = list.stream().filter(t -> "芯片".equals(t.getMeaning())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(itemGroup)) {
            BigDecimal qty = BigDecimal.ZERO;
            //获取工单芯片需求数量
            BigDecimal workQty = hmeWoJobSnMapper.queryWorkQty(tenantId, dto.getSiteId(), itemGroup.get(0).getValue(), mtWorkOrder.getBomId());
            if (workQty != null) {
                qty = workQty;
            }
            hmeWoJobSnReturnDTO2.setRemainingQty(qty.subtract(BigDecimal.valueOf(a)).toString());

            List<String> cosType = hmeWoJobSnMapper.selectCosType(tenantId, dto.getSiteId(), itemGroup.get(0).getValue(), mtWorkOrder.getBomId());
            if (CollectionUtils.isNotEmpty(cosType)) {
                hmeWoJobSnReturnDTO2.setCosType(cosType.get(0));
            }
        }
        return hmeWoJobSnReturnDTO2;
    }

    /**
     * @param tenantId
     * @param dto
     * @return void
     * @description 更新数量
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/12 9:13
     **/
    @Override
    public void updateQty(Long tenantId, HmeWoJobSnDTO5 dto) {

        HmeCosOperationRecord hmeCosOperationRecord = hmeCosOperationRecordRepository.selectByPrimaryKey(dto.getOperationRecordId());

        //如果Wafer改变，校验EoJobSn表是否有数据
        if (!StringUtils.trimToEmpty(hmeCosOperationRecord.getWafer()).equals(StringUtils.trimToEmpty(dto.getWafer()))) {
            List<HmeEoJobSn> hmeEoJobSns = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_SOURCE_JOB_ID, dto.getOperationRecordId()))
                    .build());
            if (CollectionUtils.isNotEmpty(hmeEoJobSns)) {
                throw new MtException("HME_COS_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_013", "HME"));
            }
        }

        Long cosNum = hmeCosOperationRecord.getCosNum() - hmeCosOperationRecord.getSurplusCosNum();
        if (dto.getCosNum() < cosNum) {
            throw new MtException("HME_COS_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_011", "HME"));
        }
        if (hmeCosOperationRecord.getSurplusCosNum() == 0) {
            throw new MtException("HME_COS_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_012", "HME"));
        }
        long updateNum = hmeCosOperationRecord.getCosNum() - dto.getCosNum();
        hmeCosOperationRecord.setCosNum(dto.getCosNum());
        hmeCosOperationRecord.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum() - updateNum);
        hmeCosOperationRecord.setWafer(dto.getWafer());
        hmeCosOperationRecord.setBarNum(dto.getBarNum());
        hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHis.setTenantId(tenantId);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
    }

    /**
     * @param tenantId
     * @param materialLotCode
     * @param workOrderId
     * @param wkcLinetId
     * @param siteId
     * @return java.lang.String
     * @description 根据条码获取数量
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/26 10:26
     **/
    @Override
    public MtMaterialLot materiallotQtyQuery(Long tenantId, String materialLotCode, String workOrderId, String wkcLinetId, String siteId) {
        //校验不能为空
        if (StringUtils.isBlank(materialLotCode)) {
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (StringUtils.isBlank(workOrderId)) {
            throw new MtException("HME_COS_CHIP_IMP_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_CHIP_IMP_0007", "HME"));
        }
        //校验条码是存在 Y OK
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLot.setSiteId(siteId);
        mtMaterialLot.setMaterialLotCode(materialLotCode);
        mtMaterialLot.setEnableFlag("Y");
        MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectOne(mtMaterialLot);
        if (ObjectUtil.isNotEmpty(mtMaterialLot1)) {
            if (!OK.equals(mtMaterialLot1.getQualityStatus())) {
                throw new MtException("HME_WO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_002", "HME", materialLotCode));
            }
            if ("Y".equals(mtMaterialLot1.getFreezeFlag())) {
                throw new MtException("HME_WO_JOB_SN_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_014", "HME", materialLotCode));
            }
            if ("Y".equals(mtMaterialLot1.getStocktakeFlag())||"Y".equals(mtMaterialLot1.getFreezeFlag())) {
                throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_003", "HME", materialLotCode));
            }
            MtExtendVO extendAttrQuery = new MtExtendVO();
            extendAttrQuery.setKeyId(mtMaterialLot1.getMaterialLotId());
            extendAttrQuery.setTableName(ATTR_TABLE);
            extendAttrQuery.setAttrName("MF_FLAG");
            List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);
            //来料条码扫描，增加条码扩展字段MF_FLAG校验，若为Y，则提示来料条码为在制品条码，不允许扫描！
            if(extendAttrList.size() > 0) {
                if ("Y".equals(extendAttrList.get(0).getAttrValue())) {
                    //实物条码${1}为在制品,请检查!
                    throw new MtException("WMS_INV_TRANSFER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0035", "WMS", materialLotCode));
                }
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_001", "HME", materialLotCode));
        }

        //来料条码所在仓库不为该工段下线边仓
        MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
        mtModLocatorOrgRel.setOrganizationId(wkcLinetId);
        mtModLocatorOrgRel.setOrganizationType("WORKCELL");
        List<MtModLocatorOrgRel> select1 = MtModLocatorOrgRelRepository.select(mtModLocatorOrgRel);
        if (CollectionUtils.isNotEmpty(select1)) {
            List<String> collect = select1.stream().map(MtModLocatorOrgRel::getLocatorId).collect(Collectors.toList());
            if (!collect.contains(mtMaterialLot1.getLocatorId())) {
                throw new MtException("HME_WO_JOB_SN_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_010", "HME"));
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_011", "HME"));
        }
        //该条码不属于当前工单组件物料
        List<String> materialIds = hmeWoJobSnMapper.getMaterialsByWorkOrder(workOrderId);
        if (CollectionUtils.isEmpty(materialIds)) {
            throw new MtException("HME_WO_JOB_SN_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_007", "HME"));
        } else {
            if (!materialIds.contains(mtMaterialLot1.getMaterialId())) {
                throw new MtException("HME_WO_JOB_SN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_008", "HME"));
            }
        }
        return mtMaterialLot1;
    }

    /**
     * <strong>Title : materialLotSplit</strong><br/>
     * <strong>Description : 来料录入 </strong><br/>
     * <strong>Create on : 2020/12/18 上午11:01</strong><br/>
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO7
     * @author wenzhang.yu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * kejin.liu | 2020年12月18日11:02:19 | 增加校验，保存ROUTER_STEP_ID<br/>
     * 拆分创建条码时<br/>
     * 1、通过条码扩展表取出WORK_ORDER_ID关联mt_work_order表取出ROUTER_ID，
     * 如果未找到ROUTER_ID，则报错“生产指令工艺路线为空${1}” MT_ORDER_0025，
     * 关联mt_router_step取出ROUTER_STEP_ID，
     * 关联mt_router_operation取出每个工序的OPERATION_ID。<br/>
     * 2、根据当前登录工位取出工艺OPERATION_ID，根据以上关联关系取出mt_router_step的ROUTER_STEP_ID，
     * 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWoJobSnReturnDTO7 materialLotSplit(Long tenantId, HmeWoJobSnDTO6 dto) {
        HmeWoJobSnReturnDTO7 hmeWoJobSnReturnDTO7 = new HmeWoJobSnReturnDTO7();
        List<HmeWoJobSnDTO7> resultList = new ArrayList<>();

        List<HmeWoJobSnDTO7> targetList = dto.getTargetList();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());

        // 20210729 add by sanfeng.zhang for peng.zhao 来源条码加锁
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("来料录入拆分");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
        hmeObjectRecordLockDTO.setObjectRecordId(mtMaterialLot.getMaterialLotId());
        hmeObjectRecordLockDTO.setObjectRecordCode(mtMaterialLot.getMaterialLotCode());
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //加锁
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);

        try {
            if (StringUtils.isEmpty(dto.getMaterialLotId())) {
                throw new MtException("HME_COS_026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_026", "HME"));
            }
            // 20210817 add by sanfeng.zhang for peng.zhao 已维护COS筛选电流点
            Long cosSelectCurrentNum = hmeCosOperationRecordMapper.queryCosSelectCurrent(tenantId, dto.getCosType());
            if (cosSelectCurrentNum.compareTo(0L) == 0) {
                throw new MtException("HME_COS_064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_064", "HME", dto.getCosType()));
            }
            //     * 1、通过条码扩展表取出WORK_ORDER_ID关联mt_work_order表取出ROUTER_ID，
            //     * 如果未找到ROUTER_ID，则报错“生产指令工艺路线为空${1}” MT_ORDER_0025，
            //     * 关联mt_router_step取出ROUTER_STEP_ID，
            //     * 关联mt_router_operation取出每个工序的OPERATION_ID。<br/>
            //     * 2、根据当前登录工位取出工艺OPERATION_ID，根据以上关联关系取出mt_router_step的ROUTER_STEP_ID，
            //     * 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP<br/>
            MtRouterStepVO5 mtRouterStepVO5 = selectUniRouterStepId(tenantId, dto);
            // 校验工单状态是否为下达作业 不为则报错
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
            if (!StringUtils.equals(mtWorkOrder.getStatus(), HmeConstants.StatusCode.EORELEASED)) {
                throw new MtException("HME_COS_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_057", "HME"));
            }
            //1.生成来料记录表
            // 获取当前用户站点信息
            Long userId = DetailsHelper.getUserDetails().getUserId();
            String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
            //获取容器类型Id
            MtContainerType mtContainerType = new MtContainerType();
            mtContainerType.setContainerTypeCode(dto.getContainerTypeCode());
            MtContainerType mtContainerType1 = mtContainerTypeRepository.selectOne(mtContainerType);
            //获取数量
            Double totalTransferQty = targetList.stream().map(HmeWoJobSnDTO7::getTransferQuantity)
                    .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
                    .summaryStatistics().getSum();
            Double totalBarNum = targetList.stream().map(HmeWoJobSnDTO7::getTargetBarNum)
                    .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
                    .summaryStatistics().getSum();
            HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
            BeanUtils.copyProperties(dto, hmeCosOperationRecord);
            hmeCosOperationRecord.setContainerTypeId(mtContainerType1.getContainerTypeId());
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setSiteId(siteId);
            hmeCosOperationRecord.setCosNum(totalTransferQty.longValue());
            hmeCosOperationRecord.setSurplusCosNum(totalTransferQty.longValue());
            hmeCosOperationRecord.setBarNum(totalBarNum.longValue());
            hmeCosOperationRecord.setMaterialId(mtMaterialLot != null ? mtMaterialLot.getMaterialId() : "");
            hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);
            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

            //2.拆分条码
            resultList = materiallotSplitEnd(tenantId, dto, totalTransferQty);
            List<String> materialLotList = new ArrayList<>();
            materialLotList.add(mtMaterialLot.getMaterialLotId());
            List<HmeCosPatchPdaVO9> labCodeAndRemarkAttrList = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, materialLotList);
            String labCode = null;
            String labRemark = null;
            if(CollectionUtils.isNotEmpty(labCodeAndRemarkAttrList)){
                labCode = labCodeAndRemarkAttrList.get(0).getLabCode();
                labRemark = labCodeAndRemarkAttrList.get(0).getRemark();
            }

            List<LovValueDTO> list = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
            List<LovValueDTO> itemGroup = list.stream().filter(t -> "芯片".equals(t.getMeaning())).collect(Collectors.toList());
            //3.条码进出站
            for (HmeWoJobSnDTO7 temp :
                    resultList) {
                //条码进站
                HmeWoJobSnDTO3 hmeWoJobSnDTO3 = new HmeWoJobSnDTO3();
                hmeWoJobSnDTO3.setMaterialLotCode(temp.getMaterialLotCode());
                hmeWoJobSnDTO3.setOperationId(dto.getOperationId());
                hmeWoJobSnDTO3.setOperationRecordId(hmeCosOperationRecord.getOperationRecordId());
                hmeWoJobSnDTO3.setShiftId(dto.getWkcShiftId());
                hmeWoJobSnDTO3.setSiteId(dto.getSiteId());
                hmeWoJobSnDTO3.setWkcLinetId(dto.getWkcLinetId());
                hmeWoJobSnDTO3.setWorkcellId(dto.getWorkcellId());
                hmeWoJobSnDTO3.setRequestId(temp.getRequestId());
                hmeWoJobSnDTO3.setParentEventId(temp.getParentEventId());
                hmeWoJobSnDTO3.setItemGroup(CollectionUtils.isNotEmpty(itemGroup) ? itemGroup.get(0).getValue() : "");
                hmeWoJobSnDTO3.setCosType(dto.getCosType());
                hmeWoJobSnDTO3.setWafer(dto.getWafer());
                // modif liukejin 2020年12月18日13:53:57
                // 将RouterStepId 插入目标物料批的扩展表
                // 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP
                HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = incomeScanMaterialLot(tenantId, hmeWoJobSnDTO3, mtRouterStepVO5, labCode, labRemark, mtMaterialLot);
                //条码出站
                HmeWojobSnDTO4 hmeWojobSnDTO4 = new HmeWojobSnDTO4();
                hmeWojobSnDTO4.setEoJobSnId(hmeWoJobSnReturnDTO3.getEoJobSnId());
                hmeWojobSnDTO4.setWoJobSnId(hmeWoJobSnReturnDTO3.getWoJobSnId());
                hmeWojobSnDTO4.setMaterialLotId(hmeWoJobSnReturnDTO3.getMaterialLotId());
                hmeWojobSnDTO4.setOperationRecordId(hmeCosOperationRecord.getOperationRecordId());
                hmeWojobSnDTO4.setProcessedNum(temp.getTransferQuantity().toString());
                hmeWojobSnDTO4.setOperationId(dto.getOperationId());
                hmeWojobSnDTO4.setWorkcellId(dto.getWorkcellId());
                HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO31 = siteOut(tenantId, hmeWojobSnDTO4);
            }
            hmeWoJobSnReturnDTO7.setOperationRecordId(hmeCosOperationRecord.getOperationRecordId());
            hmeWoJobSnReturnDTO7.setTargetList(resultList);

            //2021-10-28 19:45 add by chaonan.hu for yiming.zhang 如果拆分后原条码数量为0时清空来料条码的实验代码与备注
            MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLot.getMaterialLotId());
            if(mtMaterialLot1.getPrimaryUomQty() == 0){
                MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
                mtMaterialLotAttrVO3.setMaterialLotId(dto.getMaterialLotId());
                //创建事件
                MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
                mtEventCreateVO.setEventTypeCode("EMPTY_LAB_CODE");
                String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
                mtMaterialLotAttrVO3.setEventId(eventId);
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 labCodeAttr = new MtExtendVO5();
                labCodeAttr.setAttrName("LAB_CODE");
                labCodeAttr.setAttrValue("");
                attrList.add(labCodeAttr);
                MtExtendVO5 labRemarkAttr = new MtExtendVO5();
                labRemarkAttr.setAttrName("LAB_REMARK");
                labRemarkAttr.setAttrValue("");
                attrList.add(labRemarkAttr);
                mtMaterialLotAttrVO3.setAttr(attrList);
                mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
            }
        } catch (Exception e) {
           throw new CommonException(e.getMessage());
        } finally {
            HmeObjectRecordLockDTO lockDTO = new HmeObjectRecordLockDTO();
            lockDTO.setFunctionName("来料录入拆分");
            lockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            lockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            lockDTO.setObjectRecordId(mtMaterialLot.getMaterialLotId());
            lockDTO.setObjectRecordCode(mtMaterialLot.getMaterialLotCode());
            HmeObjectRecordLock recordLock = hmeObjectRecordLockService.getRecordLock(tenantId, lockDTO);
            //解锁
            hmeObjectRecordLockRepository.releaseLock(recordLock, HmeConstants.ConstantValue.YES);
        }
        return hmeWoJobSnReturnDTO7;
    }

    /**
     * <strong>Title : selectUniRouterStepId</strong><br/>
     * <strong>Description : 条码拆分查询唯一一条RouterStepId </strong><br/>
     * <strong>Create on : 2020/12/18 上午11:44</strong><br/>
     *
     * @param tenantId
     * @param dto
     * @return tarzan.method.domain.vo.MtRouterStepVO5
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    private MtRouterStepVO5 selectUniRouterStepId(Long tenantId, HmeWoJobSnDTO6 dto) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        if (StringUtils.isEmpty(mtWorkOrder.getRouterId())) {
            throw new CommonException("生产指令工艺路线为空！");
        }
        List<String> routerStepIds = mtRouterStepMapper.selectByWoRouter(tenantId, mtWorkOrder.getRouterId());
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new CommonException("根据RouterId找不到RouterStepId！");
        }
        List<MtRouterStepVO5> mtRouterStepVO5s = mtRouterOperationMapper.routerOperationBatch(tenantId, routerStepIds);
        MtRouterStepVO5 mtRouterStepVO5 = null;
        for (MtRouterStepVO5 mtRouterStepVO5f : mtRouterStepVO5s) {
            if (dto.getOperationId().equals(mtRouterStepVO5f.getOperationId())) {
                mtRouterStepVO5 = mtRouterStepVO5f;
                break;
            }
        }
        if (Objects.isNull(mtRouterStepVO5)) {
            throw new CommonException("没有查询到RouterStepId，请核查数据！");
        }
        return mtRouterStepVO5;
    }

    @Override
    public HmeWoJobSnReturnDTO7 updateBarcodeRecord(Long tenantId, HmeWoJobSnReturnDTO7 dto) {
        //更新记录表
        HmeCosOperationRecord record = hmeCosOperationRecordRepository.selectByPrimaryKey(dto.getOperationRecordId());
        if (record == null) {
            throw new MtException("HME_SPLIT_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0001", "HME", "operationRecordId"));
        }
        record.setWafer(dto.getWafer());
        record.setCosType(dto.getCosType());
        record.setLotNo(dto.getLotNo());
        record.setAverageWavelength(StringUtils.isNotBlank(dto.getAverageWavelength()) ? BigDecimal.valueOf(Double.valueOf(dto.getAverageWavelength())) : null);
        record.setType(dto.getType());
        record.setRemark(dto.getRemark());
        record.setJobBatch(dto.getJobBatch());
        hmeCosOperationRecordMapper.updateByPrimaryKeySelective(record);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(record, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_INCOMING_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getTargetList())) {
            for (HmeWoJobSnDTO7 hmeWoJobSnDTO7 : dto.getTargetList()) {
                MtCommonExtendVO6 vo6 = new MtCommonExtendVO6();
                vo6.setKeyId(hmeWoJobSnDTO7.getMaterialLotId());
                List<MtCommonExtendVO5> extendVO5List = new ArrayList<>();
                MtCommonExtendVO5 attr1 = new MtCommonExtendVO5();
                attr1.setAttrName("WAFER_NUM");
                attr1.setAttrValue(record.getWafer());
                extendVO5List.add(attr1);
                MtCommonExtendVO5 attr2 = new MtCommonExtendVO5();
                attr2.setAttrName("COS_TYPE");
                attr2.setAttrValue(record.getCosType());
                extendVO5List.add(attr2);
                MtCommonExtendVO5 attr3 = new MtCommonExtendVO5();
                attr3.setAttrName("LOTNO");
                attr3.setAttrValue(record.getLotNo());
                extendVO5List.add(attr3);
                MtCommonExtendVO5 attr4 = new MtCommonExtendVO5();
                attr4.setAttrName("AVG_WAVE_LENGTH");
                attr4.setAttrValue(record.getAverageWavelength() != null ? record.getAverageWavelength().toString() : "");
                extendVO5List.add(attr4);
                MtCommonExtendVO5 attr5 = new MtCommonExtendVO5();
                attr5.setAttrName("TYPE");
                attr5.setAttrValue(record.getType());
                extendVO5List.add(attr5);
                MtCommonExtendVO5 attr6 = new MtCommonExtendVO5();
                attr6.setAttrName("REMARK");
                attr6.setAttrValue(record.getRemark());
                extendVO5List.add(attr6);
                MtCommonExtendVO5 attr7 = new MtCommonExtendVO5();
                attr7.setAttrName("WORKING_LOT");
                attr7.setAttrValue(record.getJobBatch());
                extendVO5List.add(attr7);
                vo6.setAttrs(extendVO5List);
                attrPropertyList.add(vo6);
            }
        }
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            extendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
        return dto;
    }

    @Override
    public HmeWoJobSnReturnDTO7 materiallotQuery(Long tenantId, String operationRecordId) {
        //返回值
        HmeWoJobSnReturnDTO7 hmeWoJobSnReturnDTO7 = hmeWoJobSnMapper.selectByOperationRecordId(tenantId, operationRecordId);
        if(ObjectUtils.isEmpty(hmeWoJobSnReturnDTO7))
        {
            return null;
        }
        HmeWoJobSnDTO2 hmeWoJobSnDTO2 = new HmeWoJobSnDTO2();
        hmeWoJobSnDTO2.setCosType(hmeWoJobSnReturnDTO7.getCosType());
        hmeWoJobSnDTO2.setOperationId(hmeWoJobSnReturnDTO7.getOperationId());
        hmeWoJobSnDTO2.setSiteId(hmeWoJobSnReturnDTO7.getSiteId());
        hmeWoJobSnDTO2.setWorkOrderId(hmeWoJobSnReturnDTO7.getWorkOrderId());
        HmeWoJobSnReturnDTO2 hmeWoJobSnReturnDTO2 = remainingQty(tenantId, hmeWoJobSnDTO2);
        if (!ObjectUtils.isEmpty(hmeWoJobSnReturnDTO2)) {
            hmeWoJobSnReturnDTO7.setRemainingQty(hmeWoJobSnReturnDTO2.getRemainingQty());
        }
        if(Strings.isNotBlank(hmeWoJobSnReturnDTO7.getContainerTypeCode())) {
            hmeWoJobSnDTO2.setContainerTypeCode(hmeWoJobSnReturnDTO7.getContainerTypeCode());
            HmeWoJobSnReturnDTO2 hmeWoJobSnReturnDTO21 = cosNum(tenantId, hmeWoJobSnDTO2);
            if (!ObjectUtils.isEmpty(hmeWoJobSnReturnDTO21)) {
                hmeWoJobSnReturnDTO7.setIncomingQty(hmeWoJobSnReturnDTO21.getIncomingQty());
            }
        }

        HmeWoJobSnDTO3 hmeWoJobSnDTO3 = new HmeWoJobSnDTO3();
        hmeWoJobSnDTO3.setSiteId(hmeWoJobSnReturnDTO7.getSiteId());
        hmeWoJobSnDTO3.setOperationRecordId(operationRecordId);
        List<HmeWoJobSnReturnDTO4> hmeWoJobSnReturnDTO4s = hmeWoJobSnRepository.workDetails(tenantId, hmeWoJobSnDTO3);
        List<HmeWoJobSnDTO7> hmeWoJobSnDTO7s = new ArrayList<>();
        for (HmeWoJobSnReturnDTO4 temp :
                hmeWoJobSnReturnDTO4s) {
            HmeWoJobSnDTO7 hmeWoJobSnDTO7 = new HmeWoJobSnDTO7();
            hmeWoJobSnDTO7.setMaterialLotId(temp.getMaterialLotId());
            hmeWoJobSnDTO7.setMaterialLotCode(temp.getMaterialLotCode());
            if (StringUtils.isNotEmpty(temp.getChipNum())) {
                hmeWoJobSnDTO7.setTargetBarNum(temp.getSnQty() / Double.parseDouble(temp.getChipNum()));
            } else {
                hmeWoJobSnDTO7.setTargetBarNum(0d);
            }
            hmeWoJobSnDTO7.setTransferQuantity(temp.getSnQty());
            hmeWoJobSnDTO7s.add(hmeWoJobSnDTO7);
        }
        for (HmeWoJobSnReturnDTO4 temp :
                hmeWoJobSnReturnDTO4s) {
            //获取来源条码
            MtExtendVO extendAttrQuery = new MtExtendVO();
            extendAttrQuery.setKeyId(temp.getMaterialLotId());
            extendAttrQuery.setTableName(ATTR_TABLE);
            extendAttrQuery.setAttrName("ORIGINAL_ID");
            List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);
            if (CollectionUtils.isNotEmpty(extendAttrList) && StringUtils.isNotEmpty(extendAttrList.get(0).getAttrValue())) {
                hmeWoJobSnReturnDTO7.setMaterialLotId(extendAttrList.get(0).getAttrValue());
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(extendAttrList.get(0).getAttrValue());
                hmeWoJobSnReturnDTO7.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                hmeWoJobSnReturnDTO7.setQty(mtMaterialLot.getPrimaryUomQty());
            }
        }
        hmeWoJobSnReturnDTO7.setTargetList(hmeWoJobSnDTO7s);
        return hmeWoJobSnReturnDTO7;
    }

    private List<HmeWoJobSnDTO7> materiallotSplitEnd(Long tenantId, HmeWoJobSnDTO6 dto, Double totalTransferQty) {
        List<HmeWoJobSnDTO7> resultList = new ArrayList<>();

        // 创建请求事件
        String requestId = commonServiceComponent.generateEventRequest(tenantId,
                HmeConstants.EventType.COS_IO_SPLIT);
        // 创建物料转移扣减事件
        String outEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.COS_IO_SPLIT_OUT,
                requestId);

        //校验数量
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        if (mtMaterialLot.getPrimaryUomQty() < totalTransferQty) {
            throw new MtException("HME_COS_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_021", "HME"));
        }
        // 扣减转移物料批数量
        Double primaryUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(totalTransferQty)).doubleValue();
        MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
        mtMaterialLotVo2.setEventId(outEventId);
        mtMaterialLotVo2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVo2.setPrimaryUomQty(primaryUomQty);
        mtMaterialLotVo2.setEnableFlag(primaryUomQty.compareTo(0.0) == 0 ? HmeConstants.ConstantValue.NO
                : HmeConstants.ConstantValue.YES);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2, HmeConstants.ConstantValue.NO);
        MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());

        //删除装载表信息
        List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, dto.getMaterialLotId()))
                .build());
        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoads)) {
            hmeMaterialLotLoadRepository.batchDeleteByPrimaryKey(hmeMaterialLotLoads);
        }

        // 卸载容器
        if (HmeConstants.ConstantValue.NO.equals(mtMaterialLot1.getEnableFlag())
                && org.apache.commons.lang.StringUtils.isNotBlank(mtMaterialLot1.getCurrentContainerId())) {
            MtContainerVO25 cnt = new MtContainerVO25();
            cnt.setContainerId(mtMaterialLot1.getCurrentContainerId());
            cnt.setLoadObjectId(mtMaterialLot1.getMaterialLotId());
            cnt.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            cnt.setEventRequestId(requestId);
            containerRepository.containerUnload(tenantId, cnt);
        }
        // 创建物料转移新增事件
        String inEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.COS_IO_SPLIT_IN,
                requestId);
        MtMaterialLotVO2 targetMaterialLotVo = new MtMaterialLotVO2();

        //循环拆分物料
        for (HmeWoJobSnDTO7 temp :
                dto.getTargetList()) {
            //获取目标条码
            MtMaterialLot TransferMaterialLot = new MtMaterialLot();
            if (org.apache.commons.lang.StringUtils.isNotBlank(temp.getMaterialLotCode())) {
                TransferMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                    setTenantId(tenantId);
                    setMaterialLotCode(temp.getMaterialLotCode());
                }});
            }
            if (StringUtils.isBlank(temp.getMaterialLotCode())) {
                copyPropertiesIgnoreNullAndTableFields(mtMaterialLot, targetMaterialLotVo);
                targetMaterialLotVo.setEventId(inEventId);
                targetMaterialLotVo.setMaterialLotId(null);
                targetMaterialLotVo.setMaterialLotCode(null);
                targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
                targetMaterialLotVo.setPrimaryUomQty(temp.getTransferQuantity());
                targetMaterialLotVo.setCreateReason("SPLIT");
            } else {
                if (Objects.isNull(TransferMaterialLot)) {
                    copyPropertiesIgnoreNullAndTableFields(mtMaterialLot, targetMaterialLotVo);
                    targetMaterialLotVo.setEventId(inEventId);
                    targetMaterialLotVo.setMaterialLotId(null);
                    targetMaterialLotVo.setMaterialLotCode(temp.getMaterialLotCode());
                    targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
                    targetMaterialLotVo.setPrimaryUomQty(temp.getTransferQuantity());
                    targetMaterialLotVo.setCreateReason("SPLIT");
                } else if (HmeConstants.ConstantValue.NO.equals(TransferMaterialLot.getEnableFlag())) {
                    //无效条码 数量置为0 2020/10/15 add by sanfeng.zhang for banzhenyong
                    TransferMaterialLot.setPrimaryUomQty(0D);
                    mtMaterialLotRepository.updateByPrimaryKeySelective(TransferMaterialLot);
                    //校验条码进站
                    copyPropertiesIgnoreNullAndTableFields(mtMaterialLot, targetMaterialLotVo);
                    targetMaterialLotVo.setEventId(inEventId);
                    targetMaterialLotVo.setMaterialLotId(TransferMaterialLot.getMaterialLotId());
                    targetMaterialLotVo.setMaterialLotCode(TransferMaterialLot.getMaterialLotCode());
                    targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
                    targetMaterialLotVo.setPrimaryUomQty(temp.getTransferQuantity());
                } else if (HmeConstants.ConstantValue.YES.equals(TransferMaterialLot.getEnableFlag())) {
                    throw new MtException("HME_COS_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_020", "HME", TransferMaterialLot.getMaterialLotCode()));
                }
            }
            // 创建后目标条码
            MtMaterialLotVO13 vo = mtMaterialLotRepository.materialLotUpdate(tenantId, targetMaterialLotVo,
                    HmeConstants.ConstantValue.NO);
            // 更新目标条码拓展字段
            if (!(!Objects.isNull(TransferMaterialLot) && HmeConstants.ConstantValue.YES.equals(TransferMaterialLot.getEnableFlag()))) {
                this.updateMaterialLotExtendAttr(tenantId, mtMaterialLot.getMaterialLotId(), vo.getMaterialLotId(),
                        inEventId);
            }
            MtMaterialLot mtMaterialLotResult = mtMaterialLotRepository.materialLotPropertyGet(tenantId, vo.getMaterialLotId());
            HmeWoJobSnDTO7 hmeWoJobSnDTO7 = new HmeWoJobSnDTO7();
            hmeWoJobSnDTO7.setMaterialLotCode(mtMaterialLotResult.getMaterialLotCode());
            hmeWoJobSnDTO7.setTransferQuantity(temp.getTransferQuantity());
            hmeWoJobSnDTO7.setTargetBarNum(temp.getTargetBarNum());
            hmeWoJobSnDTO7.setMaterialLotId(mtMaterialLotResult.getMaterialLotId());
            hmeWoJobSnDTO7.setRequestId(requestId);
            hmeWoJobSnDTO7.setParentEventId(inEventId);
            resultList.add(hmeWoJobSnDTO7);
        }
        return resultList;
    }

    /**
     * @param tenantId
     * @param workOrderId
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO6>
     * @description 查询工单组件
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/23 11:21
     **/
    @Override
    public List<HmeWoJobSnReturnDTO6> component(Long tenantId, String workOrderId) {
        return hmeWoJobSnRepository.component(tenantId, workOrderId);
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO2
     * @description 获取数量
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/13 9:34
     **/
    @Override
    public HmeWoJobSnReturnDTO2 cosNum(Long tenantId, HmeWoJobSnDTO2 dto) {
        HmeWoJobSnReturnDTO2 hmeWoJobSnReturnDTO2 = new HmeWoJobSnReturnDTO2();

        //工单来料芯片数
        MtContainerType mtContainerType = new MtContainerType();
        mtContainerType.setContainerTypeCode(dto.getContainerTypeCode());
        MtContainerType mtContainerType1 = mtContainerTypeRepository.selectOne(mtContainerType);
        if (ObjectUtil.isEmpty(mtContainerType1)) {
            throw new MtException("HME_WO_JOB_SN_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_006", "HME"));
        }
        HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
        hmeContainerCapacity.setContainerTypeId(mtContainerType1.getContainerTypeId());
        hmeContainerCapacity.setOperationId(dto.getOperationId());
        hmeContainerCapacity.setCosType(dto.getCosType());
        HmeContainerCapacity hmeContainerCapacity1 = hmeContainerCapacityRepository.selectOne(hmeContainerCapacity);
        if (ObjectUtil.isEmpty(hmeContainerCapacity1)) {
            throw new MtException("HME_WO_JOB_SN_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_006", "HME"));
        }
        hmeWoJobSnReturnDTO2.setIncomingQty(hmeContainerCapacity1.getCapacity() == null ? "" : hmeContainerCapacity1.getCapacity().toString());

        return hmeWoJobSnReturnDTO2;
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.entity.HmeCosOperationRecord
     * @description 新增来料记录表
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/17 17:54
     **/
    @Override
    public HmeCosOperationRecord addIncoming(Long tenantId, HmeCosOperationRecordDTO dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        //获取容器类型Id
        MtContainerType mtContainerType = new MtContainerType();
        mtContainerType.setContainerTypeCode(dto.getContainerTypeCode());
        MtContainerType mtContainerType1 = mtContainerTypeRepository.selectOne(mtContainerType);
        HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
        BeanUtils.copyProperties(dto, hmeCosOperationRecord);
        hmeCosOperationRecord.setContainerTypeId(mtContainerType1.getContainerTypeId());
        hmeCosOperationRecord.setTenantId(tenantId);
        hmeCosOperationRecord.setSiteId(siteId);
        hmeCosOperationRecord.setSurplusCosNum(dto.getCosNum());
        hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        return hmeCosOperationRecord;
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3
     * @description 点击工单获取信息
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/13 10:41
     **/
    @Override
    public HmeWoJobSnReturnDTO3 workDetails(Long tenantId, HmeWoJobSnDTO3 dto) {
        HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = new HmeWoJobSnReturnDTO3();
        List<HmeWoJobSnReturnDTO4> hmeWoJobSnReturnDTO4s = hmeWoJobSnRepository.workDetails(tenantId, dto);
        if (CollectionUtils.isNotEmpty(hmeWoJobSnReturnDTO4s)) {
            hmeWoJobSnReturnDTO3.setDtoList(hmeWoJobSnReturnDTO4s);
            //第一条为进站的数据
            if (StringUtils.isEmpty(hmeWoJobSnReturnDTO4s.get(0).getSiteOutDate())) {
                hmeWoJobSnReturnDTO3.setMaterialLotCode(hmeWoJobSnReturnDTO4s.get(0).getMaterialLotCode());
                hmeWoJobSnReturnDTO3.setMaterialName(hmeWoJobSnReturnDTO4s.get(0).getMaterialName());
                hmeWoJobSnReturnDTO3.setPrimaryUonQty(hmeWoJobSnReturnDTO4s.get(0).getPrimaryUonQty());
                hmeWoJobSnReturnDTO3.setMaterialLotId(hmeWoJobSnReturnDTO4s.get(0).getMaterialLotId());
                hmeWoJobSnReturnDTO3.setEoJobSnId(hmeWoJobSnReturnDTO4s.get(0).getEoJobId());
                hmeWoJobSnReturnDTO3.setMaterialCode(hmeWoJobSnReturnDTO4s.get(0).getMaterialCode());

                HmeCosOperationRecord hmeCosOperationRecord = hmeCosOperationRecordRepository.selectByPrimaryKey(dto.getOperationRecordId());
                HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
                hmeWoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
                hmeWoJobSn.setSiteId(dto.getSiteId());
                hmeWoJobSn.setOperationId(dto.getOperationId());
                HmeWoJobSn hmeWoJobSn1 = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
                hmeWoJobSnReturnDTO3.setWoJobSnId(hmeWoJobSn1.getWoJobSnId());
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeWoJobSnReturnDTO4s.get(0).getMaterialLotId());
                String materialType = hmeWoJobSnMapper.queryMaterialType(mtMaterialLot.getMaterialId(), dto.getSiteId());

                //校验费否存在未出站物料批
                //获取来料信息
                hmeWoJobSnReturnDTO3.setMaterialType(materialType);
                //查询装载位置
                List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
                HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                hmeMaterialLotLoad.setMaterialLotId(hmeWoJobSnReturnDTO4s.get(0).getMaterialLotId());
                List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
                List<HmeMaterialLotLoad> collect = select.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());
                for (HmeMaterialLotLoad collecttemp :
                        collect) {
                    HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                    hmeWoJobSnReturnDTO5.setMaterialLotLoadId(collecttemp.getMaterialLotLoadId());
                    hmeWoJobSnReturnDTO5.setLoadSequence(collecttemp.getLoadSequence());
                    hmeWoJobSnReturnDTO5.setLoadRow(collecttemp.getLoadRow());
                    hmeWoJobSnReturnDTO5.setLoadColumn(collecttemp.getLoadColumn());
                    HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
                    hmeMaterialLotNcLoad.setLoadSequence(collecttemp.getLoadSequence());
                    List<HmeMaterialLotNcLoad> select1 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
                    if (CollectionUtils.isNotEmpty(select1)) {
                        hmeWoJobSnReturnDTO5.setDocList(select1);
                    }
                    hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                }
                MtContainerType mtContainerType = mtContainerTypeRepository.selectByPrimaryKey(hmeCosOperationRecord.getContainerTypeId());
                HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
                hmeContainerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
                hmeContainerCapacity.setOperationId(dto.getOperationId());
                hmeContainerCapacity.setCosType(hmeCosOperationRecord.getCosType());
                HmeContainerCapacity hmeContainerCapacity1 = hmeContainerCapacityRepository.selectOne(hmeContainerCapacity);
                List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s1 = groupBy(hmeWoJobSnReturnDTO5s, hmeContainerCapacity1);
                hmeWoJobSnReturnDTO3.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s1);

            }
        }

        return hmeWoJobSnReturnDTO3;
    }

    // modif liukejin 2020年12月18日13:53:57
    // 将RouterStepId 插入目标物料批的扩展表
    // 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP
    private HmeWoJobSnReturnDTO3 incomeScanMaterialLot(Long tenantId, HmeWoJobSnDTO3 dto, MtRouterStepVO5 mtRouterStepVO5,
                                                       String labCode, String labRemark, MtMaterialLot sourceMaterialLot) {
        HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = new HmeWoJobSnReturnDTO3();
        //创建事件
        MtEventCreateVO createVO = new MtEventCreateVO();
        createVO.setEventTypeCode("COS_INCOMING");
        String eventId = mtEventRepository.eventCreate(tenantId, createVO);

        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        // 获取当前时间
        final Date currentDate = new Date(System.currentTimeMillis());

        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (StringUtils.isBlank(dto.getOperationRecordId())) {
            throw new MtException("HME_COS_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_007", "HME"));
        }

        //校验条码是存在 Y OK
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLot.setSiteId(dto.getSiteId());
        mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
        mtMaterialLot.setEnableFlag("Y");
        MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectOne(mtMaterialLot);
        if (ObjectUtil.isNotEmpty(mtMaterialLot1)) {
            if (!OK.equals(mtMaterialLot1.getQualityStatus())) {
                throw new MtException("HME_WO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_002", "HME", dto.getMaterialLotCode()));
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_001", "HME", dto.getMaterialLotCode()));
        }
        //校验条码在条码拓展表里面是否已存在ATTR_NAME 为WORK_ORDER_ID且ATTR_VALUE不为空的数据，如果存在则报错“该条码已录入，不允许重复录入”
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
        List<MtExtendAttrVO> materialLotAttrVOTemp = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(materialLotAttrVOTemp)) {
            throw new MtException("HME_WO_JOB_SN_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_003", "HME"));
        }
        //条码物料是工单BOM的组件，且与容器芯片扩展属性内的物料匹配（暂时不加）(暂时不加)

        //来料条码所在仓库不为该工段下线边仓
        MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
        mtModLocatorOrgRel.setOrganizationId(dto.getWkcLinetId());
        mtModLocatorOrgRel.setOrganizationType("WORKCELL");
        List<MtModLocatorOrgRel> select1 = MtModLocatorOrgRelRepository.select(mtModLocatorOrgRel);
        if (CollectionUtils.isNotEmpty(select1)) {
            List<String> collect = select1.stream().map(MtModLocatorOrgRel::getLocatorId).collect(Collectors.toList());
            if (!collect.contains(mtMaterialLot1.getLocatorId())) {
                throw new MtException("HME_WO_JOB_SN_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_010", "HME"));
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_011", "HME"));
        }


        //校验条码数量（条码号从MT_MATERIAL_LOT表取出PRIMARY_UOM_QTY）是否等于器具容量（从器具拓展表HME_CONTAINER_CAPACITY取出LINE_NUM* COLUMN_NUM* CAPACITY），如果不等于则报错“该条码数量与容器容量不一致”
        HmeCosOperationRecord hmeCosOperationRecord = hmeCosOperationRecordRepository.selectByPrimaryKey(dto.getOperationRecordId());
        if (hmeCosOperationRecord.getSurplusCosNum() < mtMaterialLot1.getPrimaryUomQty()) {
            throw new MtException("HME_WO_JOB_SN_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_009", "HME"));
        }
        if (ObjectUtils.isEmpty(hmeCosOperationRecord.getAttribute1())) {
            hmeCosOperationRecord.setAttribute1(mtMaterialLot1.getLot());
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHis.setTenantId(tenantId);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        } else {
            if (!hmeCosOperationRecord.getAttribute1().equals(mtMaterialLot1.getLot())) {
                throw new MtException("HME_COS_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_005", "HME"));
            }
        }

        MtContainerType mtContainerType = mtContainerTypeRepository.selectByPrimaryKey(hmeCosOperationRecord.getContainerTypeId());
        HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
        hmeContainerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
        hmeContainerCapacity.setOperationId(dto.getOperationId());
        hmeContainerCapacity.setCosType(hmeCosOperationRecord.getCosType());
        HmeContainerCapacity hmeContainerCapacity1 = hmeContainerCapacityRepository.selectOne(hmeContainerCapacity);
        Long a = hmeContainerCapacity1.getColumnNum() * hmeContainerCapacity1.getLineNum() * hmeContainerCapacity1.getCapacity();
        if (a.longValue() < mtMaterialLot1.getPrimaryUomQty().doubleValue()) {
            throw new MtException("HME_WO_JOB_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_004", "HME"));
        }
        if (mtMaterialLot1.getPrimaryUomQty() % hmeContainerCapacity1.getCapacity() != 0) {
            throw new MtException("HME_WO_JOB_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_013", "HME"));
        }
        //校验当前工位+工单是否存在未出站数据，如果存在则报错“当前条码未出站，不允许同时进站多个条码”
        HmeEoJobSn hmeEoJobSn1 = new HmeEoJobSn();
        hmeEoJobSn1.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSn1.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeEoJobSn1.setJobType("IO");
        List<HmeEoJobSn> select = hmeEoJobSnRepository.select(hmeEoJobSn1);
        long count = select.stream().filter(t -> ObjectUtil.isEmpty(t.getSiteOutDate())).count();
        if (count > 0) {
            throw new MtException("HME_WO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_005", "HME"));
        }
        String materialType = hmeWoJobSnMapper.queryMaterialType(mtMaterialLot1.getMaterialId(), dto.getSiteId());

        //该条码不属于当前工单组件物料
        List<String> materialIds = hmeWoJobSnMapper.getMaterials(dto.getOperationRecordId());
        if (CollectionUtils.isEmpty(materialIds)) {
            throw new MtException("HME_WO_JOB_SN_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_007", "HME"));
        } else {
            if (!materialIds.contains(mtMaterialLot1.getMaterialId())) {
                throw new MtException("HME_WO_JOB_SN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_008", "HME"));
            }
        }

        List<MtMaterialSite> mtMaterialSites = mtMaterialSiteRepository.selectByCondition(Condition.builder(MtMaterialSite.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(MtMaterialSite.FIELD_MATERIAL_ID, mtMaterialLot1.getMaterialId())
                        .andEqualTo(MtMaterialSite.FIELD_SITE_ID, mtMaterialLot1.getSiteId()))
                .build());
        if (CollectionUtils.isNotEmpty(mtMaterialSites)) {
            List<MtMaterialBasic> mtMaterialBasics = mtMaterialBasisRepository.selectByCondition(Condition.builder(MtMaterialBasic.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtMaterialBasic.FIELD_MATERIAL_SITE_ID, mtMaterialSites.get(0).getMaterialSiteId()))
                    .build());
            if (CollectionUtils.isNotEmpty(mtMaterialBasics) && StringUtils.isNotEmpty(mtMaterialBasics.get(0).getItemGroup())) {
                //获取LOV的值
                List<LovValueDTO> list = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
                List<String> collect = list.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (!collect.contains(mtMaterialBasics.get(0).getItemGroup())) {
                    throw new MtException("HME_WO_JOB_SN_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_WO_JOB_SN_012", "HME"));
                }
            } else {
                throw new MtException("HME_WO_JOB_SN_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_012", "HME"));
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_012", "HME"));
        }
        //校验费否存在未出站物料批
        //获取来料信息
        hmeWoJobSnReturnDTO3.setMaterialType(materialType);
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setSiteInDate(currentDate);
        hmeEoJobSn.setShiftId(dto.getShiftId());
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeEoJobSn.setOperationId(dto.getOperationId());
        hmeEoJobSn.setSnMaterialId(mtMaterialLot1.getMaterialId());
        hmeEoJobSn.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        //hmeEoJobSn.setEoStepNum(1);
        hmeEoJobSn.setReworkFlag("N");
        hmeEoJobSn.setJobType("IO");
        hmeEoJobSn.setSourceJobId(dto.getOperationRecordId());
        hmeEoJobSn.setSnQty(new BigDecimal(mtMaterialLot1.getPrimaryUomQty()));
//        List<MtMaterialLotHis> mtMaterialLotHis = mtMaterialLotHisRepository.selectByCondition(Condition.builder(MtMaterialLotHis.class)
//                .andWhere(Sqls.custom()
//                        .andEqualTo(MtMaterialLotHis.FIELD_TENANT_ID, tenantId)
//                        .andEqualTo(MtMaterialLotHis.FIELD_MATERIAL_LOT_ID, mtMaterialLot1.getMaterialLotId()))
//                .orderByDesc(MtMaterialLotHis.FIELD_CREATION_DATE)
//                .build());
//        if(CollectionUtils.isNotEmpty(mtMaterialLotHis))
//        {
//            hmeEoJobSn.setAttribute3(mtMaterialLotHis.get(0).getMaterialLotHisId());
//
//        }
        hmeEoJobSn.setAttribute3(dto.getCosType());
        hmeEoJobSn.setAttribute5(dto.getWafer());
        // 20210311 eidt by wenzhang.yu for zhenyong.ban 工位、eo、条码、返修标识、job_type取出最大的eo_step_num + 1
        Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeEoJobSn.getWorkcellId(),
                hmeEoJobSn.getEoId(), hmeEoJobSn.getMaterialLotId(), HmeConstants.ConstantValue.NO, hmeEoJobSn.getJobType(), hmeEoJobSn.getOperationId());
        hmeEoJobSn.setEoStepNum(maxEoStepNum+1);
        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);

        hmeWoJobSnReturnDTO3.setEoJobSnId(hmeEoJobSn.getJobId());
        HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
        hmeWoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeWoJobSn.setSiteId(dto.getSiteId());
        hmeWoJobSn.setOperationId(dto.getOperationId());
        HmeWoJobSn hmeWoJobSn1 = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
        if (ObjectUtil.isNotEmpty(hmeWoJobSn1)) {
            hmeWoJobSn1.setSiteInNum(hmeWoJobSn1.getSiteInNum() + mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeWoJobSnMapper.updateByPrimaryKey(hmeWoJobSn1);
            hmeWoJobSnReturnDTO3.setWoJobSnId(hmeWoJobSn1.getWoJobSnId());

        } else {
            hmeWoJobSn.setSiteInNum(mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
            hmeWoJobSnReturnDTO3.setWoJobSnId(hmeWoJobSn.getWoJobSnId());
        }
        //生成右侧位置图的数据
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s1 = setMaterialLotLoad(tenantId, hmeContainerCapacity1, mtMaterialLot1, hmeCosOperationRecord, labCode, labRemark);
        //返回右侧结果
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = groupBy(hmeWoJobSnReturnDTO5s1, hmeContainerCapacity1);

        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        MtExtendVO10 materialLotExtend = new MtExtendVO10();
        materialLotExtend.setKeyId(mtMaterialLot1.getMaterialLotId());
        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
        MtExtendVO5 statusAttr = new MtExtendVO5();
        statusAttr.setAttrName("COS_TYPE");
        statusAttr.setAttrValue(hmeCosOperationRecord.getCosType());
        mtExtendVO5s.add(statusAttr);
        MtExtendVO5 statusAttr1 = new MtExtendVO5();
        statusAttr1.setAttrName("WAFER_NUM");
        statusAttr1.setAttrValue(hmeCosOperationRecord.getWafer());
        mtExtendVO5s.add(statusAttr1);
        MtExtendVO5 statusAttr2 = new MtExtendVO5();
        statusAttr2.setAttrName("COS_RECORD");
        statusAttr2.setAttrValue(hmeCosOperationRecord.getOperationRecordId());
        mtExtendVO5s.add(statusAttr2);
        MtExtendVO5 statusAttr3 = new MtExtendVO5();
        statusAttr3.setAttrName("LOCATION_ROW");
        statusAttr3.setAttrValue(hmeContainerCapacity1.getLineNum().toString());
        mtExtendVO5s.add(statusAttr3);
        MtExtendVO5 statusAttr4 = new MtExtendVO5();
        statusAttr4.setAttrName("LOCATION_COLUMN");
        statusAttr4.setAttrValue(hmeContainerCapacity1.getColumnNum().toString());
        mtExtendVO5s.add(statusAttr4);
        MtExtendVO5 statusAttr5 = new MtExtendVO5();
        statusAttr5.setAttrName("CHIP_NUM");
        statusAttr5.setAttrValue(hmeContainerCapacity1.getCapacity().toString());
        mtExtendVO5s.add(statusAttr5);
        MtExtendVO5 statusAttr6 = new MtExtendVO5();
        statusAttr6.setAttrName("CONTAINER_TYPE");
        statusAttr6.setAttrValue(mtContainerType.getContainerTypeCode());
        mtExtendVO5s.add(statusAttr6);
        MtExtendVO5 statusAttr7 = new MtExtendVO5();
        statusAttr7.setAttrName("AVG_WAVE_LENGTH");
        statusAttr7.setAttrValue(hmeCosOperationRecord.getAverageWavelength() == null ? "" : hmeCosOperationRecord.getAverageWavelength().toString());
        mtExtendVO5s.add(statusAttr7);
        MtExtendVO5 statusAttr8 = new MtExtendVO5();
        statusAttr8.setAttrName("TYPE");
        statusAttr8.setAttrValue(hmeCosOperationRecord.getType());
        mtExtendVO5s.add(statusAttr8);
        MtExtendVO5 statusAttr9 = new MtExtendVO5();
        statusAttr9.setAttrName("LOTNO");
        statusAttr9.setAttrValue(hmeCosOperationRecord.getLotNo());
        mtExtendVO5s.add(statusAttr9);
        MtExtendVO5 statusAttr10 = new MtExtendVO5();
        statusAttr10.setAttrName("WORKING_LOT");
        statusAttr10.setAttrValue(hmeCosOperationRecord.getJobBatch());
        mtExtendVO5s.add(statusAttr10);
        MtExtendVO5 statusAttr11 = new MtExtendVO5();
        statusAttr11.setAttrName("REMARK");
        statusAttr11.setAttrValue(hmeCosOperationRecord.getRemark());
        mtExtendVO5s.add(statusAttr11);
        MtExtendVO5 statusAttr12 = new MtExtendVO5();
        statusAttr12.setAttrName("WORK_ORDER_ID");
        statusAttr12.setAttrValue(hmeCosOperationRecord.getWorkOrderId());
        mtExtendVO5s.add(statusAttr12);
        MtExtendVO5 statusAttr13 = new MtExtendVO5();
        statusAttr13.setAttrName("MF_FLAG");
        statusAttr13.setAttrValue(HmeConstants.ConstantValue.YES);
        mtExtendVO5s.add(statusAttr13);
        // modif liukejin 2020年12月18日13:53:57
        // 将RouterStepId 插入目标物料批的扩展表
        // 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP
        MtExtendVO5 statusAttr14 = new MtExtendVO5();
        statusAttr14.setAttrName("CURRENT_ROUTER_STEP");
        statusAttr14.setAttrValue(mtRouterStepVO5.getRouterStepId());
        mtExtendVO5s.add(statusAttr14);

        //2021-10-28 08:37 edit bychaonan.hu for yiming.zhang 将来源条码的实验代码与备注覆盖到目标条码
        if(StringUtils.isNotBlank(labCode)){
            MtExtendVO5 labCodeAttr = new MtExtendVO5();
            labCodeAttr.setAttrName("LAB_CODE");
            labCodeAttr.setAttrValue(labCode);
            mtExtendVO5s.add(labCodeAttr);
        }
        if(StringUtils.isNotBlank(labRemark)){
            MtExtendVO5 labRemarkAttr = new MtExtendVO5();
            labRemarkAttr.setAttrName("LAB_REMARK");
            labRemarkAttr.setAttrValue(labRemark);
            mtExtendVO5s.add(labRemarkAttr);
        }

        materialLotExtend.setAttrs(mtExtendVO5s);
        materialLotExtend.setEventId(eventId);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);

        //2021-10-28 08:37 edit bychaonan.hu for yiming.zhang 记录实验代码
        if(StringUtils.isNotBlank(labCode)){
            HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCode.setTenantId(tenantId);
            hmeMaterialLotLabCode.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
            hmeMaterialLotLabCode.setLabCode(labCode);
            hmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
            hmeMaterialLotLabCode.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
            hmeMaterialLotLabCode.setSourceObject("MA");
            hmeMaterialLotLabCode.setRouterStepId(mtRouterStepVO5.getRouterStepId());
            hmeMaterialLotLabCode.setSourceMaterialLotId(sourceMaterialLot.getMaterialLotId());
            hmeMaterialLotLabCode.setSourceMaterialId(sourceMaterialLot.getMaterialId());
            hmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
            hmeMaterialLotLabCodeRepository.insertSelective(hmeMaterialLotLabCode);
        }

        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeCosOperationRecord.getWorkOrderId());
        String bomComponentId = "";
        //获取Bom_component_id逻辑修改以及传入API参数修改 原先逻辑下方先暂留
        if (StringUtils.isNotEmpty(mtWorkOrder.getBomId())) {
            List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
                setTenantId(tenantId);
                setBomId(mtWorkOrder.getBomId());
                setMaterialId(mtMaterialLot1.getMaterialId());
            }});
            if (CollectionUtils.isEmpty(mtBomComponentList)) {
                throw new MtException("HME_COS_PATCH_PDA_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0011", "HME"));
            } else if (mtBomComponentList.size() > 1) {
                throw new MtException("HME_COS_PATCH_PDA_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0012", "HME"));
            }
            bomComponentId = mtBomComponentList.get(0).getBomComponentId();
        }
        //调用API{woComponentAssemble}进行生产指令组件装配
        MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
        mtWoComponentActualVO1.setBomComponentId(bomComponentId);
        mtWoComponentActualVO1.setEventRequestId(dto.getRequestId());
        mtWoComponentActualVO1.setLocatorId(mtMaterialLot1.getLocatorId());
        mtWoComponentActualVO1.setMaterialId(mtMaterialLot1.getMaterialId());
        mtWoComponentActualVO1.setOperationId(dto.getOperationId());
        mtWoComponentActualVO1.setParentEventId(dto.getParentEventId());
        List<String> routerStepIdList = hmeWoJobSnMapper.queryRouterStepId(tenantId, hmeCosOperationRecord.getWorkOrderId(), dto.getOperationId());
        if (CollectionUtils.isNotEmpty(routerStepIdList)) {
            mtWoComponentActualVO1.setRouterStepId(routerStepIdList.get(0));
        }
        mtWoComponentActualVO1.setOperationId(dto.getOperationId());
        //根据wkc_shift_id查询班次编码、班次日期
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getShiftId());
        if (mtWkcShift != null) {
            mtWoComponentActualVO1.setShiftCode(mtWkcShift.getShiftCode());
            mtWoComponentActualVO1.setShiftDate(mtWkcShift.getShiftDate());
        }
        mtWoComponentActualVO1.setTrxAssembleQty(mtMaterialLot1.getPrimaryUomQty());
        mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
        mtWoComponentActualVO1.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        woComActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);

        //记录工单投料事务
        WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
        //工单行号
        MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
        MtWorkOrderComponentActual componentActual = mtWorkOrderComponentActualRepository.selectOne(new MtWorkOrderComponentActual() {{
            setTenantId(tenantId);
            setBomComponentId(mtBomComponent.getBomComponentId());
            setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        }});
        //判断计划内投料与计划外投料
        //总需求=组件需求*工单数量

        BigDecimal workQty = hmeWoJobSnMapper.queryWorkQty(tenantId, dto.getSiteId(), dto.getItemGroup(), mtWorkOrder.getBomId());
        BigDecimal totalQty = workQty != null ? workQty : BigDecimal.ZERO;
        BigDecimal assembleQty = componentActual != null ? BigDecimal.valueOf(componentActual.getAssembleQty()) : BigDecimal.ZERO;
        String transactionTypeCode = "";
        if (totalQty.compareTo(assembleQty.add(BigDecimal.valueOf(mtMaterialLot1.getPrimaryUomQty()))) >= 0) {
            transactionTypeCode = "HME_WO_ISSUE";
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setKeyId(bomComponentId);
            mtExtendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> mtExtendAttrVOS = extendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                objectTransactionRequestVO.setBomReserveNum(mtExtendAttrVOS.get(0).getAttrValue());
            }

            objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
        } else {
            transactionTypeCode = "HME_WO_ISSUE_EXT";
        }
        objectTransactionRequestVO.setTransactionTypeCode(transactionTypeCode);
        objectTransactionRequestVO.setEventId(eventId);
        objectTransactionRequestVO.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        objectTransactionRequestVO.setMaterialId(mtMaterialLot1.getMaterialId());
        objectTransactionRequestVO.setTransactionQty(new BigDecimal(mtMaterialLot1.getPrimaryUomQty()));
        objectTransactionRequestVO.setLotNumber(mtMaterialLot1.getLot());
        if (StringUtils.isNotEmpty(mtMaterialLot1.getPrimaryUomId())) {
            MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot1.getPrimaryUomId());
            objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
        }
        objectTransactionRequestVO.setTransactionTime(new Date());
        objectTransactionRequestVO.setPlantId(mtMaterialLot1.getSiteId());
        objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        objectTransactionRequestVO.setMergeFlag("N");
        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
            setTenantId(tenantId);
            setTransactionTypeCode(objectTransactionRequestVO.getTransactionTypeCode());
        }});
        objectTransactionRequestVO.setMoveType(wmsTransactionType.getMoveType());

        //仓库
        List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, mtMaterialLot1.getLocatorId(), "TOP");
        if (CollectionUtils.isNotEmpty(pLocatorIds)) {
            MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
            if (ploc != null) {
                objectTransactionRequestVO.setWarehouseCode(ploc.getLocatorCode());
                objectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
            }
        }
        //货位
        MtModLocator loc = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot1.getLocatorId());
        if (loc != null) {
            objectTransactionRequestVO.setLocatorId(mtMaterialLot1.getLocatorId());
            objectTransactionRequestVO.setLocatorCode(loc.getLocatorCode());
        }
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));

        //来源条码现有量扣减
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot1.getSiteId());
        mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot1.getMaterialId());
        mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot1.getLocatorId());
        mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot1.getLot());
        mtInvOnhandQuantityVO9.setChangeQuantity(mtMaterialLot1.getPrimaryUomQty());
        mtInvOnhandQuantityVO9.setEventId(eventId);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        hmeWoJobSnReturnDTO3.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s);
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot1.getMaterialId());

        hmeWoJobSnReturnDTO3.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        hmeWoJobSnReturnDTO3.setMaterialName(mtMaterial.getMaterialName());
        hmeWoJobSnReturnDTO3.setPrimaryUonQty(mtMaterialLot1.getPrimaryUomQty().toString());
        hmeWoJobSnReturnDTO3.setMaterialCode(mtMaterial.getMaterialCode());

        return hmeWoJobSnReturnDTO3;
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3
     * @description 扫描物料批
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/13 11:21
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWoJobSnReturnDTO3 scanMaterialLot(Long tenantId, HmeWoJobSnDTO3 dto) {
        HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = new HmeWoJobSnReturnDTO3();

        //创建事件
        MtEventCreateVO createVO = new MtEventCreateVO();
        createVO.setEventTypeCode("COS_INCOMING");
        String eventId = mtEventRepository.eventCreate(tenantId, createVO);

        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        // 获取当前时间
        final Date currentDate = new Date(System.currentTimeMillis());

        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (StringUtils.isBlank(dto.getOperationRecordId())) {
            throw new MtException("HME_COS_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_007", "HME"));
        }

        //校验条码是存在 Y OK
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLot.setSiteId(dto.getSiteId());
        mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
        mtMaterialLot.setEnableFlag("Y");
        MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectOne(mtMaterialLot);
        if (ObjectUtil.isNotEmpty(mtMaterialLot1)) {
            if (!OK.equals(mtMaterialLot1.getQualityStatus())) {
                throw new MtException("HME_WO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_002", "HME", dto.getMaterialLotCode()));
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_001", "HME", dto.getMaterialLotCode()));
        }
        //校验条码在条码拓展表里面是否已存在ATTR_NAME 为WORK_ORDER_ID且ATTR_VALUE不为空的数据，如果存在则报错“该条码已录入，不允许重复录入”
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
        List<MtExtendAttrVO> materialLotAttrVOTemp = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(materialLotAttrVOTemp)) {
            throw new MtException("HME_WO_JOB_SN_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_003", "HME"));
        }
        //条码物料是工单BOM的组件，且与容器芯片扩展属性内的物料匹配（暂时不加）(暂时不加)

        //来料条码所在仓库不为该工段下线边仓
        MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
        mtModLocatorOrgRel.setOrganizationId(dto.getWkcLinetId());
        mtModLocatorOrgRel.setOrganizationType("WORKCELL");
        List<MtModLocatorOrgRel> select1 = MtModLocatorOrgRelRepository.select(mtModLocatorOrgRel);
        if (CollectionUtils.isNotEmpty(select1)) {
            List<String> collect = select1.stream().map(MtModLocatorOrgRel::getLocatorId).collect(Collectors.toList());
            if (!collect.contains(mtMaterialLot1.getLocatorId())) {
                throw new MtException("HME_WO_JOB_SN_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_010", "HME"));
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_011", "HME"));
        }


        //校验条码数量（条码号从MT_MATERIAL_LOT表取出PRIMARY_UOM_QTY）是否等于器具容量（从器具拓展表HME_CONTAINER_CAPACITY取出LINE_NUM* COLUMN_NUM* CAPACITY），如果不等于则报错“该条码数量与容器容量不一致”
        HmeCosOperationRecord hmeCosOperationRecord = hmeCosOperationRecordRepository.selectByPrimaryKey(dto.getOperationRecordId());
        if (hmeCosOperationRecord.getSurplusCosNum() < mtMaterialLot1.getPrimaryUomQty()) {
            throw new MtException("HME_WO_JOB_SN_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_009", "HME"));
        }
        if (ObjectUtils.isEmpty(hmeCosOperationRecord.getAttribute1())) {
            hmeCosOperationRecord.setAttribute1(mtMaterialLot1.getLot());
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHis.setTenantId(tenantId);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        } else {
            if (!hmeCosOperationRecord.getAttribute1().equals(mtMaterialLot1.getLot())) {
                throw new MtException("HME_COS_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_005", "HME"));
            }
        }

        MtContainerType mtContainerType = mtContainerTypeRepository.selectByPrimaryKey(hmeCosOperationRecord.getContainerTypeId());
        HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
        hmeContainerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
        hmeContainerCapacity.setOperationId(dto.getOperationId());
        hmeContainerCapacity.setCosType(hmeCosOperationRecord.getCosType());
        HmeContainerCapacity hmeContainerCapacity1 = hmeContainerCapacityRepository.selectOne(hmeContainerCapacity);
        Long a = hmeContainerCapacity1.getColumnNum() * hmeContainerCapacity1.getLineNum() * hmeContainerCapacity1.getCapacity();
        if (a.longValue() < mtMaterialLot1.getPrimaryUomQty().doubleValue()) {
            throw new MtException("HME_WO_JOB_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_004", "HME"));
        }
        if (mtMaterialLot1.getPrimaryUomQty() % hmeContainerCapacity1.getCapacity() != 0) {
            throw new MtException("HME_WO_JOB_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_013", "HME"));
        }
        //校验当前工位+工单是否存在未出站数据，如果存在则报错“当前条码未出站，不允许同时进站多个条码”
        HmeEoJobSn hmeEoJobSn1 = new HmeEoJobSn();
        hmeEoJobSn1.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSn1.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeEoJobSn1.setJobType("IO");
        List<HmeEoJobSn> select = hmeEoJobSnRepository.select(hmeEoJobSn1);
        long count = select.stream().filter(t -> ObjectUtil.isEmpty(t.getSiteOutDate())).count();
        if (count > 0) {
            throw new MtException("HME_WO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_005", "HME"));
        }
        String materialType = hmeWoJobSnMapper.queryMaterialType(mtMaterialLot1.getMaterialId(), dto.getSiteId());

        //该条码不属于当前工单组件物料
        List<String> materialIds = hmeWoJobSnMapper.getMaterials(dto.getOperationRecordId());
        if (CollectionUtils.isEmpty(materialIds)) {
            throw new MtException("HME_WO_JOB_SN_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_007", "HME"));
        } else {
            if (!materialIds.contains(mtMaterialLot1.getMaterialId())) {
                throw new MtException("HME_WO_JOB_SN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_008", "HME"));
            }
        }

        List<MtMaterialSite> mtMaterialSites = mtMaterialSiteRepository.selectByCondition(Condition.builder(MtMaterialSite.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(MtMaterialSite.FIELD_MATERIAL_ID, mtMaterialLot1.getMaterialId())
                        .andEqualTo(MtMaterialSite.FIELD_SITE_ID, mtMaterialLot1.getSiteId()))
                .build());
        if (CollectionUtils.isNotEmpty(mtMaterialSites)) {
            List<MtMaterialBasic> mtMaterialBasics = mtMaterialBasisRepository.selectByCondition(Condition.builder(MtMaterialBasic.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtMaterialBasic.FIELD_MATERIAL_SITE_ID, mtMaterialSites.get(0).getMaterialSiteId()))
                    .build());
            if (CollectionUtils.isNotEmpty(mtMaterialBasics) && StringUtils.isNotEmpty(mtMaterialBasics.get(0).getItemGroup())) {
                //获取LOV的值
                List<LovValueDTO> list = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
                List<String> collect = list.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (!collect.contains(mtMaterialBasics.get(0).getItemGroup())) {
                    throw new MtException("HME_WO_JOB_SN_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_WO_JOB_SN_012", "HME"));
                }
            } else {
                throw new MtException("HME_WO_JOB_SN_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_012", "HME"));
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_012", "HME"));
        }
        //校验费否存在未出站物料批
        //获取来料信息
        hmeWoJobSnReturnDTO3.setMaterialType(materialType);
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setSiteInDate(currentDate);
        hmeEoJobSn.setShiftId(dto.getShiftId());
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeEoJobSn.setOperationId(dto.getOperationId());
        hmeEoJobSn.setSnMaterialId(mtMaterialLot1.getMaterialId());
        hmeEoJobSn.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        hmeEoJobSn.setEoStepNum(1);
        hmeEoJobSn.setReworkFlag("N");
        hmeEoJobSn.setJobType("IO");
        hmeEoJobSn.setSourceJobId(dto.getOperationRecordId());
        hmeEoJobSn.setSnQty(new BigDecimal(mtMaterialLot1.getPrimaryUomQty()));
        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);

        hmeWoJobSnReturnDTO3.setEoJobSnId(hmeEoJobSn.getJobId());
        HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
        hmeWoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeWoJobSn.setSiteId(dto.getSiteId());
        hmeWoJobSn.setOperationId(dto.getOperationId());
        HmeWoJobSn hmeWoJobSn1 = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
        if (ObjectUtil.isNotEmpty(hmeWoJobSn1)) {
            hmeWoJobSn1.setSiteInNum(hmeWoJobSn1.getSiteInNum() + mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeWoJobSnMapper.updateByPrimaryKey(hmeWoJobSn1);
            hmeWoJobSnReturnDTO3.setWoJobSnId(hmeWoJobSn1.getWoJobSnId());

        } else {
            hmeWoJobSn.setSiteInNum(mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
            hmeWoJobSnReturnDTO3.setWoJobSnId(hmeWoJobSn.getWoJobSnId());
        }
        //生成右侧位置图的数据
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s1 = setMaterialLotLoad(tenantId, hmeContainerCapacity1, mtMaterialLot1, hmeCosOperationRecord, null, null);
        //返回右侧结果
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = groupBy(hmeWoJobSnReturnDTO5s1, hmeContainerCapacity1);

        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        MtExtendVO10 materialLotExtend = new MtExtendVO10();
        materialLotExtend.setKeyId(mtMaterialLot1.getMaterialLotId());
        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
        MtExtendVO5 statusAttr = new MtExtendVO5();
        statusAttr.setAttrName("COS_TYPE");
        statusAttr.setAttrValue(hmeCosOperationRecord.getCosType());
        mtExtendVO5s.add(statusAttr);
        MtExtendVO5 statusAttr1 = new MtExtendVO5();
        statusAttr1.setAttrName("WAFER_NUM");
        statusAttr1.setAttrValue(hmeCosOperationRecord.getWafer());
        mtExtendVO5s.add(statusAttr1);
        MtExtendVO5 statusAttr2 = new MtExtendVO5();
        statusAttr2.setAttrName("COS_RECORD");
        statusAttr2.setAttrValue(hmeCosOperationRecord.getOperationRecordId());
        mtExtendVO5s.add(statusAttr2);
        MtExtendVO5 statusAttr3 = new MtExtendVO5();
        statusAttr3.setAttrName("LOCATION_ROW");
        statusAttr3.setAttrValue(hmeContainerCapacity1.getLineNum().toString());
        mtExtendVO5s.add(statusAttr3);
        MtExtendVO5 statusAttr4 = new MtExtendVO5();
        statusAttr4.setAttrName("LOCATION_COLUMN");
        statusAttr4.setAttrValue(hmeContainerCapacity1.getColumnNum().toString());
        mtExtendVO5s.add(statusAttr4);
        MtExtendVO5 statusAttr5 = new MtExtendVO5();
        statusAttr5.setAttrName("CHIP_NUM");
        statusAttr5.setAttrValue(hmeContainerCapacity1.getCapacity().toString());
        mtExtendVO5s.add(statusAttr5);
        MtExtendVO5 statusAttr6 = new MtExtendVO5();
        statusAttr6.setAttrName("CONTAINER_TYPE");
        statusAttr6.setAttrValue(mtContainerType.getContainerTypeCode());
        mtExtendVO5s.add(statusAttr6);
        MtExtendVO5 statusAttr7 = new MtExtendVO5();
        statusAttr7.setAttrName("AVG_WAVE_LENGTH");
        statusAttr7.setAttrValue(hmeCosOperationRecord.getAverageWavelength() == null ? "" : hmeCosOperationRecord.getAverageWavelength().toString());
        mtExtendVO5s.add(statusAttr7);
        MtExtendVO5 statusAttr8 = new MtExtendVO5();
        statusAttr8.setAttrName("TYPE");
        statusAttr8.setAttrValue(hmeCosOperationRecord.getType());
        mtExtendVO5s.add(statusAttr8);
        MtExtendVO5 statusAttr9 = new MtExtendVO5();
        statusAttr9.setAttrName("LOTNO");
        statusAttr9.setAttrValue(hmeCosOperationRecord.getLotNo());
        mtExtendVO5s.add(statusAttr9);
        MtExtendVO5 statusAttr10 = new MtExtendVO5();
        statusAttr10.setAttrName("WORKING_LOT");
        statusAttr10.setAttrValue(hmeCosOperationRecord.getJobBatch());
        mtExtendVO5s.add(statusAttr10);
        MtExtendVO5 statusAttr11 = new MtExtendVO5();
        statusAttr11.setAttrName("REMARK");
        statusAttr11.setAttrValue(hmeCosOperationRecord.getRemark());
        mtExtendVO5s.add(statusAttr11);
        MtExtendVO5 statusAttr12 = new MtExtendVO5();
        statusAttr12.setAttrName("WORK_ORDER_ID");
        statusAttr12.setAttrValue(hmeCosOperationRecord.getWorkOrderId());
        mtExtendVO5s.add(statusAttr12);

        materialLotExtend.setAttrs(mtExtendVO5s);
        materialLotExtend.setEventId(eventId);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);

        hmeWoJobSnReturnDTO3.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s);
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot1.getMaterialId());

        hmeWoJobSnReturnDTO3.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        hmeWoJobSnReturnDTO3.setMaterialName(mtMaterial.getMaterialName());
        hmeWoJobSnReturnDTO3.setPrimaryUonQty(mtMaterialLot1.getPrimaryUomQty().toString());
        hmeWoJobSnReturnDTO3.setMaterialCode(mtMaterial.getMaterialCode());

        return hmeWoJobSnReturnDTO3;
    }


    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeWoJobSnReturnDTO3
     * @description 出站
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/13 20:29
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWoJobSnReturnDTO3 siteOut(Long tenantId, HmeWojobSnDTO4 dto) {
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // 获取当前时间
        final Date currentDate = new Date(System.currentTimeMillis());
        HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = new HmeWoJobSnReturnDTO3();

        //更新hme_eo_job_sn
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getEoJobSnId());
        hmeEoJobSn.setSiteOutBy(userId);
        hmeEoJobSn.setSiteOutDate(currentDate);
        hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);

        //更新hme_wo_job_sn
        HmeWoJobSn hmeWoJobSn1 = hmeWoJobSnRepository.selectByPrimaryKey(dto.getWoJobSnId());
        hmeWoJobSn1.setProcessedNum(hmeWoJobSn1.getProcessedNum() + Long.valueOf(dto.getProcessedNum().substring(0, dto.getProcessedNum().indexOf("."))));
        hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn1);
        //更新记录表
        HmeCosOperationRecord hmeCosOperationRecord = hmeCosOperationRecordRepository.selectByPrimaryKey(dto.getOperationRecordId());
        hmeCosOperationRecord.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum() - Long.valueOf(dto.getProcessedNum().substring(0, dto.getProcessedNum().indexOf("."))));
        hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHis.setTenantId(tenantId);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

        //物料批扩展表
        MtContainerType mtContainerType = mtContainerTypeRepository.selectByPrimaryKey(hmeCosOperationRecord.getContainerTypeId());
        HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
        hmeContainerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
        hmeContainerCapacity.setOperationId(dto.getOperationId());
        hmeContainerCapacity.setCosType(hmeCosOperationRecord.getCosType());

        //判断设备是否为空
        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(dto.getEoJobSnId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);

        }
        return hmeWoJobSnReturnDTO3;
    }

    /**
     * @param tenantId
     * @param dto
     * @return void
     * @description 不良确认
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/24 9:49
     **/
    @Override
    public void ncLoad(Long tenantId, List<HmeMaterialLotNcLoad> dto) {
        hmeMaterialLotNcLoadRepository.batchInsertSelective(dto);
    }

    /**
     * 组装LoadSequence
     *
     * @param materialLotId
     * @param row
     * @param column
     * @param dateStr
     * @author sanfeng.zhang@hand-china.com 2021/4/23 20:02
     * @return java.lang.String
     */
    private String assemblyLoadSequence(String materialLotId, int row, int column, String dateStr) {
        StringBuffer sb = new StringBuffer();
        sb.append(materialLotId.substring(0, materialLotId.length() - 2));
        // 20210423 modify by sanfeng.zhang for kang.wang 按目前组装 LoadSequence可能重复 行 列补成3位数 只改wafer装载
        sb.append(String.format("%03d", row));
        sb.append(String.format("%03d", column));
        sb.append(dateStr);
        return sb.toString();
    }

    private List<HmeWoJobSnReturnDTO5> setMaterialLotLoad(Long tenantId, HmeContainerCapacity hmeContainerCapacity, MtMaterialLot mtMaterialLot,
                                                          HmeCosOperationRecord hmeCosOperationRecord, String labCode, String labRemark) {
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
        String data = sdf.format(new Date());

        int num = (int) Math.ceil(mtMaterialLot.getPrimaryUomQty() / hmeContainerCapacity.getCapacity());
        if (StringUtils.isEmpty(hmeContainerCapacity.getAttribute1()) || "A".equals(hmeContainerCapacity.getAttribute1())) {
            for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                    HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    String loadSequence = this.assemblyLoadSequence(mtMaterialLot.getMaterialLotId(), i, j, data);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    hmeWoJobSnReturnDTO5.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                    hmeWoJobSnReturnDTO5.setMaterialLotLoadId(hmeMaterialLotLoad.getMaterialLotLoadId());
                    hmeWoJobSnReturnDTO5.setLoadRow((long) i);
                    hmeWoJobSnReturnDTO5.setLoadColumn((long) j);
                    hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        } else if ("B".equals(hmeContainerCapacity.getAttribute1())) {
            for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                    HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    String loadSequence = this.assemblyLoadSequence(mtMaterialLot.getMaterialLotId(), i, j, data);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    hmeWoJobSnReturnDTO5.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                    hmeWoJobSnReturnDTO5.setMaterialLotLoadId(hmeMaterialLotLoad.getMaterialLotLoadId());
                    hmeWoJobSnReturnDTO5.setLoadRow((long) i);
                    hmeWoJobSnReturnDTO5.setLoadColumn((long) j);
                    hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        } else if ("C".equals(hmeContainerCapacity.getAttribute1())) {
            for (int j = hmeContainerCapacity.getColumnNum().intValue(); j > 0; j--) {
                for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                    HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    String loadSequence = this.assemblyLoadSequence(mtMaterialLot.getMaterialLotId(), i, j, data);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    hmeWoJobSnReturnDTO5.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                    hmeWoJobSnReturnDTO5.setMaterialLotLoadId(hmeMaterialLotLoad.getMaterialLotLoadId());
                    hmeWoJobSnReturnDTO5.setLoadRow((long) i);
                    hmeWoJobSnReturnDTO5.setLoadColumn((long) j);
                    hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        } else if ("D".equals(hmeContainerCapacity.getAttribute1())) {
            for (int j = hmeContainerCapacity.getColumnNum().intValue(); j > 0; j--) {
                for (int i = hmeContainerCapacity.getLineNum().intValue(); i > 0; i--) {
                    HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    String loadSequence = this.assemblyLoadSequence(mtMaterialLot.getMaterialLotId(), i, j, data);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    hmeWoJobSnReturnDTO5.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                    hmeWoJobSnReturnDTO5.setMaterialLotLoadId(hmeMaterialLotLoad.getMaterialLotLoadId());
                    hmeWoJobSnReturnDTO5.setLoadRow((long) i);
                    hmeWoJobSnReturnDTO5.setLoadColumn((long) j);
                    hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        } else {
            for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                for (int i = hmeContainerCapacity.getLineNum().intValue(); i > 0; i--) {
                    HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    String loadSequence = this.assemblyLoadSequence(mtMaterialLot.getMaterialLotId(), i, j, data);
                    hmeMaterialLotLoad.setLoadSequence(loadSequence);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    hmeWoJobSnReturnDTO5.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                    hmeWoJobSnReturnDTO5.setMaterialLotLoadId(hmeMaterialLotLoad.getMaterialLotLoadId());
                    hmeWoJobSnReturnDTO5.setLoadRow((long) i);
                    hmeWoJobSnReturnDTO5.setLoadColumn((long) j);
                    hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        }
        return hmeWoJobSnReturnDTO5s;
    }

    private List<HmeWoJobSnReturnDTO5> groupBy(List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s, HmeContainerCapacity hmeContainerCapacity) {
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5sResult = new ArrayList<>();
        for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
            for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                int loadColumn = j;
                int loadRow = i;
                List<HmeWoJobSnReturnDTO5> collect = hmeWoJobSnReturnDTO5s.stream()
                        .filter(t -> t.getLoadRow().intValue() == loadRow && t.getLoadColumn().intValue() == loadColumn)
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    hmeWoJobSnReturnDTO5sResult.add(collect.get(0));
                } else {
                    hmeWoJobSnReturnDTO5sResult.add(new HmeWoJobSnReturnDTO5());
                }
            }
        }
        return hmeWoJobSnReturnDTO5sResult;
    }


    public static <E, T> void copyPropertiesIgnoreNullAndTableFields(E src, T target) {
        // 对象值转换时屏蔽表字段：ID，创建更新人等信息
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(Arrays.asList(WmsCommonUtils.getNullPropertyNames(src)));
        hashSet.addAll(Arrays.asList(IGNORE_TABLE_FIELDS));
        String[] result = new String[hashSet.size()];
        BeanUtils.copyProperties(src, target, hashSet.toArray(result));
    }

    /**
     * 更新拓展字段
     *
     * @param tenantId            租户
     * @param sourceMaterialLotId 来源物料批ID
     * @param materialLotId       生成物料批ID
     * @param eventId             事件ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/20 10:32:56
     */
    private void updateMaterialLotExtendAttr(Long tenantId, String sourceMaterialLotId, String materialLotId,
                                             String eventId) {
        // 查询原属性
        MtExtendVO extendAttrQuery = new MtExtendVO();
        extendAttrQuery.setKeyId(sourceMaterialLotId);
        extendAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);

        // 覆盖目标属性
        List<MtExtendVO5> targetExtendAttrList = new ArrayList<>();
        MtExtendVO5 newAttr = null;
        Boolean mfFlag = false;
        for (MtExtendAttrVO extendAttrVO : extendAttrList) {
            if (StringUtils.equals(extendAttrVO.getAttrName(), "ORIGINAL_ID") || StringUtils.equals(extendAttrVO.getAttrName(), "SUPPLIER_LOT") || StringUtils.equals(extendAttrVO.getAttrName(), "WORK_ORDER_ID")) {
                continue;
            }

            if (org.apache.commons.lang.StringUtils.equals(extendAttrVO.getAttrName(), "MF_FLAG")) {
                mfFlag = true;
            }
            newAttr = new MtExtendVO5();
            newAttr.setAttrName(extendAttrVO.getAttrName());
            newAttr.setAttrValue(extendAttrVO.getAttrValue());
            targetExtendAttrList.add(newAttr);
        }

        if (!mfFlag) {
            newAttr = new MtExtendVO5();
            newAttr.setAttrName("MF_FLAG");
            newAttr.setAttrValue("");
            targetExtendAttrList.add(newAttr);
        }

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("ORIGINAL_ID");
        newAttr.setAttrValue(sourceMaterialLotId);
        targetExtendAttrList.add(newAttr);

        extendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE, materialLotId, eventId, targetExtendAttrList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWoJobSnReturnDTO7 waferSplit(Long tenantId, HmeWoJobSnDTO6 dto) {
        HmeWoJobSnReturnDTO7 hmeWoJobSnReturnDTO7 = new HmeWoJobSnReturnDTO7();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());

        // 20210729 add by sanfeng.zhang for peng.zhao 来源条码加锁
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("wafer拆分");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
        hmeObjectRecordLockDTO.setObjectRecordId(mtMaterialLot.getMaterialLotId());
        hmeObjectRecordLockDTO.setObjectRecordCode(mtMaterialLot.getMaterialLotCode());
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //加锁
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);
        try{
            // 创建请求事件
            String requestId = commonServiceComponent.generateEventRequest(tenantId,
                    HmeConstants.EventType.COS_IO_SPLIT);
            // 创建物料转移扣减事件
            String outEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.COS_INCOMING,
                    requestId);

            List<HmeWoJobSnDTO7> resultList = new ArrayList<>();

            List<HmeWoJobSnDTO7> targetList = dto.getTargetList();

            if (StringUtils.isEmpty(dto.getMaterialLotId())) {
                throw new MtException("HME_COS_026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_026", "HME"));
            }
            // 20210817 add by sanfeng.zhang for peng.zhao 已维护COS筛选电流点
            Long cosSelectCurrentNum = hmeCosOperationRecordMapper.queryCosSelectCurrent(tenantId, dto.getCosType());
            if (cosSelectCurrentNum.compareTo(0L) == 0) {
                throw new MtException("HME_COS_064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_064", "HME", dto.getCosType()));
            }
            MtRouterStepVO5 mtRouterStepVO5 = selectUniRouterStepId(tenantId, dto);
            // 校验工单状态是否为下达作业 不为则报错
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
            if (!StringUtils.equals(mtWorkOrder.getStatus(), HmeConstants.StatusCode.EORELEASED)) {
                throw new MtException("HME_COS_057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_057", "HME"));
            }
            //1.生成来料记录表
            // 获取当前用户站点信息
            Long userId = DetailsHelper.getUserDetails().getUserId();
            String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);

            //获取数量
            Double totalTransferQty = targetList.stream().map(HmeWoJobSnDTO7::getTransferQuantity)
                    .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
                    .summaryStatistics().getSum();

            HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
            BeanUtils.copyProperties(dto, hmeCosOperationRecord);
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setSiteId(siteId);
            hmeCosOperationRecord.setCosNum(totalTransferQty.longValue());
            hmeCosOperationRecord.setMaterialId(mtMaterialLot != null ? mtMaterialLot.getMaterialId() : "");
            hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);
            dto.setOperationRecordId(hmeCosOperationRecord.getOperationRecordId());
            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

            //拆分条码
            resultList = waferSplitEnd(requestId, outEventId, tenantId, dto, totalTransferQty, mtRouterStepVO5, hmeCosOperationRecord.getOperationRecordId());

            //3.条码进出站
            updateOther(tenantId,requestId, outEventId, resultList, dto,totalTransferQty);

            hmeWoJobSnReturnDTO7.setOperationRecordId(hmeCosOperationRecord.getOperationRecordId());
            hmeWoJobSnReturnDTO7.setTargetList(resultList);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            HmeObjectRecordLockDTO lockDTO = new HmeObjectRecordLockDTO();
            lockDTO.setFunctionName("wafer拆分");
            lockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            lockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            lockDTO.setObjectRecordId(mtMaterialLot.getMaterialLotId());
            lockDTO.setObjectRecordCode(mtMaterialLot.getMaterialLotCode());
            HmeObjectRecordLock recordLock = hmeObjectRecordLockService.getRecordLock(tenantId, lockDTO);
            //解锁
            hmeObjectRecordLockRepository.releaseLock(recordLock, HmeConstants.ConstantValue.YES);
        }
        return hmeWoJobSnReturnDTO7;
    }

    @Override
    public void incomingExport(Long tenantId, HmeWoJobSnDTO dto, HttpServletResponse response) {
        List<HmeWoJobSnReturnDTO> returnDTOS = hmeWoJobSnRepository.workList(tenantId, dto);
        String fileName = INCOMING_FILE_NAME;
        String sheetName = INCOMING_SHEET_NAME;
        // 写入数据,文件流会自动关闭
        ExportUtil.writeExcelOneSheet(response, returnDTOS, fileName, sheetName, HmeWoJobSnReturnDTO.class);
    }

    private void updateOther(Long tenantId, String requestId, String outEventId, List<HmeWoJobSnDTO7> resultList, HmeWoJobSnDTO6 dto, Double totalTransferQty) {
        MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());

        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        String bomComponentId = "";
        //获取Bom_component_id逻辑修改以及传入API参数修改 原先逻辑下方先暂留
        if (StringUtils.isNotEmpty(mtWorkOrder.getBomId())) {
            List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
                setTenantId(tenantId);
                setBomId(mtWorkOrder.getBomId());
                setMaterialId(mtMaterialLot1.getMaterialId());
            }});
            if (CollectionUtils.isEmpty(mtBomComponentList)) {
                throw new MtException("HME_COS_PATCH_PDA_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0011", "HME"));
            } else if (mtBomComponentList.size() > 1) {
                throw new MtException("HME_COS_PATCH_PDA_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0012", "HME"));
            }
            bomComponentId = mtBomComponentList.get(0).getBomComponentId();
        }
        List<String> routerStepIdList = hmeWoJobSnMapper.queryRouterStepId(tenantId, dto.getWorkOrderId(), dto.getOperationId());



        List<WmsObjectTransactionRequestVO>  wmsObjectTransactionRequestVOs=new ArrayList<>();
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
        List<LovValueDTO> itemGroup = list.stream().filter(t -> "芯片".equals(t.getMeaning())).collect(Collectors.toList());

        MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
        MtWorkOrderComponentActual componentActual = mtWorkOrderComponentActualRepository.selectOne(new MtWorkOrderComponentActual() {{
            setTenantId(tenantId);
            setBomComponentId(mtBomComponent.getBomComponentId());
            setWorkOrderId(dto.getWorkOrderId());
        }});
        BigDecimal workQty = hmeWoJobSnMapper.queryWorkQty(tenantId, dto.getSiteId(), (CollectionUtils.isNotEmpty(itemGroup) ? itemGroup.get(0).getValue() : ""), mtWorkOrder.getBomId());
        BigDecimal totalQty = workQty != null ? workQty : BigDecimal.ZERO;
        BigDecimal assembleQty = componentActual != null ? BigDecimal.valueOf(componentActual.getAssembleQty()) : BigDecimal.ZERO;
        String transactionTypeCode = "";

        for (HmeWoJobSnDTO7 temp:
         resultList){

            WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
            //工单行号
            if (totalQty.compareTo(assembleQty.add(BigDecimal.valueOf(temp.getTransferQuantity()))) >= 0) {
                transactionTypeCode = "HME_WO_ISSUE";
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setTableName("mt_bom_component_attr");
                mtExtendVO.setKeyId(bomComponentId);
                mtExtendVO.setAttrName("lineAttribute10");
                List<MtExtendAttrVO> mtExtendAttrVOS = extendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                    objectTransactionRequestVO.setBomReserveNum(mtExtendAttrVOS.get(0).getAttrValue());
                }
                objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            } else {
                transactionTypeCode = "HME_WO_ISSUE_EXT";
            }

            objectTransactionRequestVO.setTransactionTypeCode(transactionTypeCode);
            objectTransactionRequestVO.setEventId(outEventId);
            objectTransactionRequestVO.setMaterialLotId(temp.getMaterialLotId());
            objectTransactionRequestVO.setMaterialId(mtMaterialLot1.getMaterialId());
            objectTransactionRequestVO.setTransactionQty(new BigDecimal(temp.getTransferQuantity()));
            objectTransactionRequestVO.setLotNumber(mtMaterialLot1.getLot());
            objectTransactionRequestVO.setTransactionUom(mtMaterialLot1.getLot());
            objectTransactionRequestVO.setTransactionTime(new Date());
            if (StringUtils.isNotEmpty(mtMaterialLot1.getPrimaryUomId())) {
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot1.getPrimaryUomId());
                objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
            }
            objectTransactionRequestVO.setPlantId(mtMaterialLot1.getSiteId());
             //仓库
            List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, mtMaterialLot1.getLocatorId(), "TOP");
            if (CollectionUtils.isNotEmpty(pLocatorIds)) {
                objectTransactionRequestVO.setWarehouseId(pLocatorIds.get(0));
            }
            //货位
            objectTransactionRequestVO.setLocatorId(mtMaterialLot1.getLocatorId());
            objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
            MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());
            if(!Objects.isNull(mtModProductionLine)) {
                objectTransactionRequestVO.setProdLineCode(mtModProductionLine.getProdLineCode());
            }
            WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                setTenantId(tenantId);
                setTransactionTypeCode(objectTransactionRequestVO.getTransactionTypeCode());
            }});
            objectTransactionRequestVO.setMoveType(wmsTransactionType.getMoveType());

            wmsObjectTransactionRequestVOs.add(objectTransactionRequestVO);
            assembleQty=assembleQty.add(new BigDecimal(temp.getTransferQuantity()));
            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();

            // 获取当前时间
            final Date currentDate = new Date(System.currentTimeMillis());

            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(currentDate);
            hmeEoJobSn.setShiftId(dto.getWkcShiftId());
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setSiteOutDate(currentDate);
            hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobSn.setOperationId(dto.getOperationId());
            hmeEoJobSn.setSnMaterialId(mtMaterialLot1.getMaterialId());
            hmeEoJobSn.setMaterialLotId(temp.getMaterialLotId());
            hmeEoJobSn.setEoStepNum(1);
            hmeEoJobSn.setReworkFlag("N");
            hmeEoJobSn.setJobType("IO");
            hmeEoJobSn.setSourceJobId(dto.getOperationRecordId());
            hmeEoJobSn.setSnQty(new BigDecimal(temp.getTransferQuantity()));
//            List<MtMaterialLotHis> mtMaterialLotHis = mtMaterialLotHisRepository.selectByCondition(Condition.builder(MtMaterialLotHis.class)
//                    .andWhere(Sqls.custom()
//                            .andEqualTo(MtMaterialLotHis.FIELD_TENANT_ID, tenantId)
//                            .andEqualTo(MtMaterialLotHis.FIELD_MATERIAL_LOT_ID, temp.getMaterialLotId()))
//                    .orderByDesc(MtMaterialLotHis.FIELD_CREATION_DATE)
//                    .build());
//            if(CollectionUtils.isNotEmpty(mtMaterialLotHis))
//            {
//                hmeEoJobSn.setAttribute3(mtMaterialLotHis.get(0).getMaterialLotHisId());
//
//            }
            hmeEoJobSn.setAttribute3(dto.getCosType());
            hmeEoJobSn.setAttribute5(dto.getWafer());
            hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
        }
        if(CollectionUtils.isNotEmpty(wmsObjectTransactionRequestVOs)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, wmsObjectTransactionRequestVOs);
        }

        MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
        // 20210423 modify by sanfeng.zahng for kang.wang 强制装配标识修改 bom组件找不到 则强制装配 否则不是
        if (StringUtils.isNotBlank(bomComponentId)) {
            mtWoComponentActualVO1.setAssembleExcessFlag("N");
        } else {
            mtWoComponentActualVO1.setAssembleExcessFlag("Y");
        }
        mtWoComponentActualVO1.setBomComponentId(bomComponentId);
        mtWoComponentActualVO1.setEventRequestId(requestId);
        mtWoComponentActualVO1.setMaterialId(mtMaterialLot1.getMaterialId());
        mtWoComponentActualVO1.setOperationId(dto.getOperationId());
        mtWoComponentActualVO1.setParentEventId(outEventId);
        if (CollectionUtils.isNotEmpty(routerStepIdList)) {
            mtWoComponentActualVO1.setRouterStepId(routerStepIdList.get(0));
        }
        mtWoComponentActualVO1.setTrxAssembleQty(totalTransferQty);
        mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
        mtWoComponentActualVO1.setWorkOrderId(dto.getWorkOrderId());
        woComActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);

    }

    private List<HmeWoJobSnDTO7> waferSplitEnd(String requestId, String outEventId, Long tenantId, HmeWoJobSnDTO6 dto, Double totalTransferQty, MtRouterStepVO5 mtRouterStepVO5, String operationRecordId) {
        List<HmeWoJobSnDTO7> resultList = new ArrayList<>();

        //校验数量
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        if (mtMaterialLot.getPrimaryUomQty() < totalTransferQty) {
            throw new MtException("HME_COS_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_021", "HME"));
        }
        // 扣减转移物料批数量
        BigDecimal primaryUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(totalTransferQty));
        MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
        mtMaterialLotVo2.setEventId(outEventId);
        mtMaterialLotVo2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVo2.setPrimaryUomQty(primaryUomQty.doubleValue());
        mtMaterialLotVo2.setEnableFlag(primaryUomQty.compareTo(BigDecimal.ZERO) == 0 ? HmeConstants.ConstantValue.NO
                : HmeConstants.ConstantValue.YES);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2, HmeConstants.ConstantValue.NO);
        MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());

        //来源条码现有量扣减
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot1.getSiteId());
        mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot1.getMaterialId());
        mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot1.getLocatorId());
        mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot1.getLot());
        mtInvOnhandQuantityVO9.setChangeQuantity(totalTransferQty);
        mtInvOnhandQuantityVO9.setEventId(outEventId);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        // 卸载容器
        if (HmeConstants.ConstantValue.NO.equals(mtMaterialLot1.getEnableFlag())
                && StringUtils.isNotBlank(mtMaterialLot1.getCurrentContainerId())) {
            MtContainerVO25 cnt = new MtContainerVO25();
            cnt.setContainerId(mtMaterialLot1.getCurrentContainerId());
            cnt.setLoadObjectId(mtMaterialLot1.getMaterialLotId());
            cnt.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            cnt.setEventRequestId(requestId);
            containerRepository.containerUnload(tenantId, cnt);
        }
        // 创建物料转移新增事件
        MtMaterialLotVO2 targetMaterialLotVo = new MtMaterialLotVO2();
        // 更新目标条码
        List<MtMaterialLotVO20> updateTargetCodeList = new ArrayList<>();
        // 更新目标扩展字段
        List<MtCommonExtendVO7> updateTargetCodeAttrList = new ArrayList<>();
        //循环拆分物料
        for (HmeWoJobSnDTO7 temp : dto.getTargetList()) {
            //获取目标条码
            MtMaterialLot TransferMaterialLot = null;
            if (org.apache.commons.lang.StringUtils.isNotBlank(temp.getMaterialLotCode())) {
                TransferMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                    setTenantId(tenantId);
                    setMaterialLotCode(temp.getMaterialLotCode());
                }});
            }
            if (StringUtils.isBlank(temp.getMaterialLotCode())) {
                copyPropertiesIgnoreNullAndTableFields(mtMaterialLot, targetMaterialLotVo);
                targetMaterialLotVo.setEventId(outEventId);
                targetMaterialLotVo.setMaterialLotId(null);
                targetMaterialLotVo.setMaterialLotCode(null);
                targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
                targetMaterialLotVo.setPrimaryUomQty(temp.getTransferQuantity());
                targetMaterialLotVo.setCreateReason("SPLIT");
            } else {
                if (Objects.isNull(TransferMaterialLot)) {
                    copyPropertiesIgnoreNullAndTableFields(mtMaterialLot, targetMaterialLotVo);
                    targetMaterialLotVo.setEventId(outEventId);
                    targetMaterialLotVo.setMaterialLotId(null);
                    targetMaterialLotVo.setMaterialLotCode(temp.getMaterialLotCode());
                    targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
                    targetMaterialLotVo.setPrimaryUomQty(temp.getTransferQuantity());
                    targetMaterialLotVo.setCreateReason("SPLIT");
                } else if (HmeConstants.ConstantValue.NO.equals(TransferMaterialLot.getEnableFlag())) {
                    TransferMaterialLot.setPrimaryUomQty(0D);
                    mtMaterialLotRepository.updateByPrimaryKeySelective(TransferMaterialLot);
                    //校验条码进站
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();

                    copyPropertiesIgnoreNullAndTableFields(mtMaterialLot, mtMaterialLotVO20);
                    mtMaterialLotVO20.setMaterialLotId(TransferMaterialLot.getMaterialLotId());
                    mtMaterialLotVO20.setMaterialLotCode(TransferMaterialLot.getMaterialLotCode());
                    mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.YES);
                    mtMaterialLotVO20.setPrimaryUomQty(temp.getTransferQuantity());
                    updateTargetCodeList.add(mtMaterialLotVO20);
                } else if (HmeConstants.ConstantValue.YES.equals(TransferMaterialLot.getEnableFlag())) {
                    throw new MtException("HME_COS_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_020", "HME", TransferMaterialLot.getMaterialLotCode()));
                }
            }
            // 返回值
            HmeWoJobSnDTO7 hmeWoJobSnDTO7 = new HmeWoJobSnDTO7();

            if (Objects.isNull(TransferMaterialLot)) {
                // 创建后目标条码
                MtMaterialLotVO13 vo = mtMaterialLotRepository.materialLotUpdate(tenantId, targetMaterialLotVo,
                        HmeConstants.ConstantValue.NO);
                // 更新目标条码拓展字段
                this.updateWaferAttr(tenantId, mtMaterialLot.getMaterialLotId(), vo.getMaterialLotId(),dto,mtRouterStepVO5,
                        outEventId,operationRecordId, mtMaterialLot.getMaterialId());
                MtMaterialLot mtMaterialLotResult = mtMaterialLotRepository.materialLotPropertyGet(tenantId, vo.getMaterialLotId());
                hmeWoJobSnDTO7.setMaterialLotCode(mtMaterialLotResult.getMaterialLotCode());
                hmeWoJobSnDTO7.setMaterialLotId(mtMaterialLotResult.getMaterialLotId());
            } else {
                // 更新无效目标条码拓展字段
                MtCommonExtendVO7 mtCommonExtendVO7 = this.assemblyTargetCodeAttr(tenantId, mtMaterialLot.getMaterialLotId(),
                        TransferMaterialLot.getMaterialLotId(), dto, mtRouterStepVO5, operationRecordId, mtMaterialLot.getMaterialId());
                updateTargetCodeAttrList.add(mtCommonExtendVO7);
                hmeWoJobSnDTO7.setMaterialLotCode(TransferMaterialLot.getMaterialLotCode());
                hmeWoJobSnDTO7.setMaterialLotId(TransferMaterialLot.getMaterialLotId());
            }
            hmeWoJobSnDTO7.setTransferQuantity(temp.getTransferQuantity());
            hmeWoJobSnDTO7.setTargetBarNum(temp.getTargetBarNum());
            hmeWoJobSnDTO7.setRequestId(requestId);
            hmeWoJobSnDTO7.setParentEventId(outEventId);
            resultList.add(hmeWoJobSnDTO7);
        }
        // 更新无效的条码
        if (CollectionUtils.isNotEmpty(updateTargetCodeList)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateTargetCodeList, outEventId, HmeConstants.ConstantValue.NO);
        }
        // 更新扩展字段
        if (CollectionUtils.isNotEmpty(updateTargetCodeAttrList)) {
            extendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", outEventId, updateTargetCodeAttrList);
        }
        //2021-10-28 19:45 add by chaonan.hu for yiming.zhang 来料条码数量为0时清空来料条码的实验代码与备注
        if(primaryUomQty.compareTo(BigDecimal.ZERO) == 0){
            MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
            mtMaterialLotAttrVO3.setMaterialLotId(dto.getMaterialLotId());
            //创建事件
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("EMPTY_LAB_CODE");
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            mtMaterialLotAttrVO3.setEventId(eventId);
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 labCodeAttr = new MtExtendVO5();
            labCodeAttr.setAttrName("LAB_CODE");
            labCodeAttr.setAttrValue("");
            attrList.add(labCodeAttr);
            MtExtendVO5 labRemarkAttr = new MtExtendVO5();
            labRemarkAttr.setAttrName("LAB_REMARK");
            labRemarkAttr.setAttrValue("");
            attrList.add(labRemarkAttr);
            mtMaterialLotAttrVO3.setAttr(attrList);
            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
        }
        return resultList;
    }

    private MtCommonExtendVO7 assemblyTargetCodeAttr(Long tenantId, String sourceMaterialLotId, String targetMaterialLotId,
                                                     HmeWoJobSnDTO6 dto, MtRouterStepVO5 mtRouterStepVO5, String operationRecordId, String sourceMaterialId) {
        // 查询原属性
        MtExtendVO extendAttrQuery = new MtExtendVO();
        extendAttrQuery.setKeyId(sourceMaterialLotId);
        extendAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);

        // 覆盖目标属性
        MtCommonExtendVO7 targetExtend = new MtCommonExtendVO7();
        targetExtend.setKeyId(targetMaterialLotId);
        List<MtCommonExtendVO4> targetExtendAttrList = new ArrayList<>();
        MtCommonExtendVO4 newAttr = null;
        Boolean mfFlag = false;
        String labCode = null;
        for (MtExtendAttrVO extendAttrVO : extendAttrList) {
            if (StringUtils.equals(extendAttrVO.getAttrName(), "PRODUCT_DATE")
                    || StringUtils.equals(extendAttrVO.getAttrName(), "LAB_CODE")
                    || StringUtils.equals(extendAttrVO.getAttrName(), "LAB_REMARK")) {
                newAttr = new MtCommonExtendVO4();
                newAttr.setAttrName(extendAttrVO.getAttrName());
                newAttr.setAttrValue(extendAttrVO.getAttrValue());
                targetExtendAttrList.add(newAttr);
                if(StringUtils.equals(extendAttrVO.getAttrName(), "LAB_CODE")){
                    labCode = extendAttrVO.getAttrValue();
                }
            }
        }

        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("WORK_ORDER_ID");
        newAttr.setAttrValue(dto.getWorkOrderId());
        targetExtendAttrList.add(newAttr);

        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("LOTNO");
        newAttr.setAttrValue(dto.getLotNo());
        targetExtendAttrList.add(newAttr);

        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("WAFER_NUM");
        newAttr.setAttrValue(dto.getWafer());
        targetExtendAttrList.add(newAttr);

        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("TYPE");
        newAttr.setAttrValue(dto.getType());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("STATUS");
        newAttr.setAttrValue("INSTOCK");
        targetExtendAttrList.add(newAttr);


        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("REMARK");
        newAttr.setAttrValue(dto.getRemark());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("MF_FLAG");
        newAttr.setAttrValue("Y");
        targetExtendAttrList.add(newAttr);


        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("WORKING_LOT");
        newAttr.setAttrValue(dto.getJobBatch());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("CURRENT_ROUTER_STEP");
        newAttr.setAttrValue(mtRouterStepVO5.getRouterStepId());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("COS_TYPE");
        newAttr.setAttrValue(dto.getCosType());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("COS_RECORD");
        newAttr.setAttrValue(operationRecordId);
        targetExtendAttrList.add(newAttr);

        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("AVG_WAVE_LENGTH");
        newAttr.setAttrValue(null==dto.getAverageWavelength()?"":dto.getAverageWavelength().toString());
        targetExtendAttrList.add(newAttr);

        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("ORIGINAL_ID");
        newAttr.setAttrValue(sourceMaterialLotId);
        targetExtendAttrList.add(newAttr);
        targetExtend.setAttrs(targetExtendAttrList);

        //2021-10-28 20:01 add by chaonan.hu for yiming.zhang 记录实验代码
        if(StringUtils.isNotBlank(labCode)){
            HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCode.setTenantId(tenantId);
            hmeMaterialLotLabCode.setMaterialLotId(targetMaterialLotId);
            hmeMaterialLotLabCode.setLabCode(labCode);
            hmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
            hmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
            hmeMaterialLotLabCode.setSourceObject("MA");
            hmeMaterialLotLabCode.setRouterStepId(mtRouterStepVO5.getRouterStepId());
            hmeMaterialLotLabCode.setSourceMaterialLotId(sourceMaterialLotId);
            hmeMaterialLotLabCode.setSourceMaterialId(sourceMaterialId);
            hmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
            hmeMaterialLotLabCodeRepository.insertSelective(hmeMaterialLotLabCode);
        }
        return targetExtend;
    }

    private void updateWaferAttr(Long tenantId, String sourceMaterialLotId, String materialLotId, HmeWoJobSnDTO6 dto, MtRouterStepVO5 mtRouterStepVO5,
                                 String outEventId, String operationRecordId, String sourceMaterialId) {
        // 查询原属性
        MtExtendVO extendAttrQuery = new MtExtendVO();
        extendAttrQuery.setKeyId(sourceMaterialLotId);
        extendAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);

        // 覆盖目标属性
        List<MtExtendVO5> targetExtendAttrList = new ArrayList<>();
        MtExtendVO5 newAttr = null;
        Boolean mfFlag = false;
        String labCode = null;
        for (MtExtendAttrVO extendAttrVO : extendAttrList) {
            if (StringUtils.equals(extendAttrVO.getAttrName(), "PRODUCT_DATE")
                    || StringUtils.equals(extendAttrVO.getAttrName(), "LAB_CODE")
                    || StringUtils.equals(extendAttrVO.getAttrName(), "LAB_REMARK")) {
                newAttr = new MtExtendVO5();
                newAttr.setAttrName(extendAttrVO.getAttrName());
                newAttr.setAttrValue(extendAttrVO.getAttrValue());
                targetExtendAttrList.add(newAttr);
                if(StringUtils.equals(extendAttrVO.getAttrName(), "LAB_CODE")){
                    labCode = extendAttrVO.getAttrValue();
                }
            }
        }

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("WORK_ORDER_ID");
        newAttr.setAttrValue(dto.getWorkOrderId());
        targetExtendAttrList.add(newAttr);

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("LOTNO");
        newAttr.setAttrValue(dto.getLotNo());
        targetExtendAttrList.add(newAttr);

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("WAFER_NUM");
        newAttr.setAttrValue(dto.getWafer());
        targetExtendAttrList.add(newAttr);

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("TYPE");
        newAttr.setAttrValue(dto.getType());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtExtendVO5();
        newAttr.setAttrName("STATUS");
        newAttr.setAttrValue("INSTOCK");
        targetExtendAttrList.add(newAttr);


        newAttr = new MtExtendVO5();
        newAttr.setAttrName("REMARK");
        newAttr.setAttrValue(dto.getRemark());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtExtendVO5();
        newAttr.setAttrName("MF_FLAG");
        newAttr.setAttrValue("Y");
        targetExtendAttrList.add(newAttr);


        newAttr = new MtExtendVO5();
        newAttr.setAttrName("WORKING_LOT");
        newAttr.setAttrValue(dto.getJobBatch());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtExtendVO5();
        newAttr.setAttrName("CURRENT_ROUTER_STEP");
        newAttr.setAttrValue(mtRouterStepVO5.getRouterStepId());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtExtendVO5();
        newAttr.setAttrName("COS_TYPE");
        newAttr.setAttrValue(dto.getCosType());
        targetExtendAttrList.add(newAttr);


        newAttr = new MtExtendVO5();
        newAttr.setAttrName("COS_RECORD");
        newAttr.setAttrValue(operationRecordId);
        targetExtendAttrList.add(newAttr);

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("AVG_WAVE_LENGTH");
        newAttr.setAttrValue(null==dto.getAverageWavelength()?"":dto.getAverageWavelength().toString());
        targetExtendAttrList.add(newAttr);

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("ORIGINAL_ID");
        newAttr.setAttrValue(sourceMaterialLotId);
        targetExtendAttrList.add(newAttr);

        extendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE, materialLotId, outEventId, targetExtendAttrList);

        //2021-10-28 20:01 add by chaonan.hu for yiming.zhang 记录实验代码
        if(StringUtils.isNotBlank(labCode)){
            HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCode.setTenantId(tenantId);
            hmeMaterialLotLabCode.setMaterialLotId(materialLotId);
            hmeMaterialLotLabCode.setLabCode(labCode);
            hmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
            hmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
            hmeMaterialLotLabCode.setSourceObject("MA");
            hmeMaterialLotLabCode.setRouterStepId(mtRouterStepVO5.getRouterStepId());
            hmeMaterialLotLabCode.setSourceMaterialLotId(sourceMaterialLotId);
            hmeMaterialLotLabCode.setSourceMaterialId(sourceMaterialId);
            hmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
            hmeMaterialLotLabCodeRepository.insertSelective(hmeMaterialLotLabCode);
        }
    }

}
