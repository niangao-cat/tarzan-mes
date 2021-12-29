package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeCosInspectPlatformService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.*;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.repository.MtTagGroupObjectRepository;
import tarzan.general.domain.vo.MtDataRecordVO;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 作者：yapeng.yao@hand-china.com 时间：2020/8/24 17:30
 */
@Service
public class HmeCosInspectPlatformServiceImpl implements HmeCosInspectPlatformService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;
    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;
    @Autowired
    private HmeTagMapper hmeTagMapper;
    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private HmeTagDaqAttrRepository hmeTagDaqAttrRepository;
    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;
    @Autowired
    private HmeCosInspectPlatformMapper hmeCosInspectPlatformMapper;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;
    @Autowired
    private MtTagGroupObjectRepository mtTagGroupObjectRepository;
    @Autowired
    private HmeCosFunctionRepository hmeCosFunctionRepository;
    @Autowired
    private HmeCosFunctionMapper hmeCosFunctionMapper;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    /**
     * 工单工艺工位在制记录id，
     */
    private static final String COS_RECORD = "COS_RECORD";
    /**
     * Wafer编码
     */
    private static final String WAFER_NUM = "WAFER_NUM";
    /**
     * 行数
     */
    private static final String LOCATION_ROW = "LOCATION_ROW";
    /**
     * 列数
     */
    private static final String LOCATION_COLUMN = "LOCATION_COLUMN";
    /**
     * 芯片数
     */
    private static final String CHIP_NUM = "CHIP_NUM";

    /**
     * JOB_TYPE
     */
    private static final String COS_DETECT = "COS_DETECT";
    private static final String COS_UNIT_DETECT = "COS_UNIT_DETECT";

    /**
     * 进入界面，自动查询信息
     *
     * @param tenantId
     * @param requestVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosInspectPlatformAutoQueryInfoResponseVO> autoQueryInfo(Long tenantId,
                                                                            HmeCosInspectPlatformQueryInfoRequestVO requestVO) {
        if (Objects.isNull(requestVO)) {
            return Collections.EMPTY_LIST;
        }
        // 校验输入参数
        checkAutoQueryInfo(tenantId, requestVO);

        String workcellId = requestVO.getWorkcellId();
        String operationId = requestVO.getOperationId();
        String equipmentId = requestVO.getEquipmentId();

        // 根据WKC_ID+工艺_ID+设备_ID（可为空），查询最近一条数据
        HmeCosOperationRecord lastCosOperationRecord = hmeCosOperationRecordRepository.queryLastRecord(tenantId,
                workcellId, operationId, equipmentId, "Y");
        if (Objects.isNull(lastCosOperationRecord)
                || StringUtils.isEmpty(lastCosOperationRecord.getOperationRecordId())) {
            return Collections.EMPTY_LIST;
        }
        String operationRecordId = lastCosOperationRecord.getOperationRecordId();
        // 检索当前在制记录
        List<HmeCosInspectPlatformAutoQueryInfoResponseVO> recordList = hmeCosInspectPlatformMapper
                .queryAutoOperationRecord(tenantId, operationRecordId, workcellId, operationId);

        ArrayList<HmeCosInspectPlatformAutoQueryInfoResponseVO> returnList =
                new ArrayList<HmeCosInspectPlatformAutoQueryInfoResponseVO>(recordList.size());
        if (CollectionUtils.isNotEmpty(recordList)) {
            // set返回参数
            setHmeCosInspectPlatformAutoQueryInfoResponseVOList(tenantId, recordList, returnList);
        }

        return returnList;
    }

    /**
     * 检索按钮
     *
     * @param tenantId
     * @param requestVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosInspectPlatformAutoQueryInfoResponseVO> queryInfo(Long tenantId,
                                                                        HmeCosInspectPlatformQueryInfoRequestVO requestVO) {
        if (Objects.isNull(requestVO)) {
            return Collections.EMPTY_LIST;
        }
        // 校验输入参数
        checkAutoQueryInfo(tenantId, requestVO);

        String workcellId = requestVO.getWorkcellId();
        String operationId = requestVO.getOperationId();
        String workOrderNum = requestVO.getWorkOrderNum();
        String wafer = requestVO.getWafer();
        String equipmentId = requestVO.getEquipmentId();
        String beginDate = requestVO.getBeginDate();
        String endDate = requestVO.getEndDate();

        // 根据WKC_ID+工艺_ID+设备_ID（可为空），查询最近一条数据
        HmeCosOperationRecord lastCosOperationRecord = hmeCosOperationRecordRepository.queryLastRecord(tenantId,
                workcellId, operationId, equipmentId, "Y");
        if (Objects.isNull(lastCosOperationRecord)
                || StringUtils.isEmpty(lastCosOperationRecord.getOperationRecordId())) {
            return Collections.EMPTY_LIST;
        }

        String operationRecordId = lastCosOperationRecord.getOperationRecordId();
        // 根据查询条件查询记录
        List<HmeCosInspectPlatformAutoQueryInfoResponseVO> recordList =
                hmeCosInspectPlatformMapper.queryOperationRecord(tenantId, operationRecordId, workcellId,
                        operationId, workOrderNum, wafer, equipmentId, beginDate, endDate);

        ArrayList<HmeCosInspectPlatformAutoQueryInfoResponseVO> returnList =
                new ArrayList<HmeCosInspectPlatformAutoQueryInfoResponseVO>(recordList.size());
        if (CollectionUtils.isNotEmpty(recordList)) {
            // set返回参数
            setHmeCosInspectPlatformAutoQueryInfoResponseVOList(tenantId, recordList, returnList);
        }

        return returnList;
    }

    /**
     * set返回自动查询信息参数
     *
     * @param tenantId
     * @param hmeCosOperationRecordList
     * @param returnList
     */
    private void setHmeCosInspectPlatformAutoQueryInfoResponseVOList(Long tenantId,
                                                                     List<HmeCosInspectPlatformAutoQueryInfoResponseVO> hmeCosOperationRecordList,
                                                                     List<HmeCosInspectPlatformAutoQueryInfoResponseVO> returnList) {
        hmeCosOperationRecordList.forEach(record -> {
            HmeCosInspectPlatformAutoQueryInfoResponseVO responseVO =
                    new HmeCosInspectPlatformAutoQueryInfoResponseVO();
            responseVO.setOperationRecordId(record.getOperationRecordId());
            responseVO.setEoJobSnId(record.getEoJobSnId());
            responseVO.setWorkOrderId(record.getWorkOrderId());
            responseVO.setWorkOrderNum(record.getWorkOrderNum());
            responseVO.setMaterialLotId(record.getMaterialLotId());
            responseVO.setMaterialLotCode(record.getMaterialLotCode());
            responseVO.setPrimaryUomQty(record.getPrimaryUomQty());
            responseVO.setWafer(record.getWafer());
            responseVO.setLotNo(record.getLotNo());
            responseVO.setMaterialId(record.getMaterialId());
            responseVO.setMaterialCode(record.getMaterialCode());
            responseVO.setMaterialName(record.getMaterialName());
            responseVO.setRemark(record.getRemark());
            responseVO.setSiteInDate(record.getSiteInDate());
            responseVO.setSiteOutDate(record.getSiteOutDate());
            returnList.add(responseVO);
        });
    }

    /**
     * 自动查询-校验输入参数
     *
     * @param tenantId
     * @param requestVO
     */
    private void checkAutoQueryInfo(Long tenantId, HmeCosInspectPlatformQueryInfoRequestVO requestVO) {
        // 工位
        if (StringUtils.isBlank(requestVO.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "工位"));
        }
        // 工艺路线
        if (StringUtils.isBlank(requestVO.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺路线"));
        }
    }

    /**
     * @param tenantId  租户ID
     * @param requestVO 输入参数
     * @return
     * @Description COS检验工作台-扫描盒子
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosInspectPlatformScanMaterialLotCodeResponseVO scanMaterialLotCode(Long tenantId,
                                                                                  HmeCosInspectPlatformQueryInfoRequestVO requestVO) {
        if (Objects.isNull(requestVO)) {
            return new HmeCosInspectPlatformScanMaterialLotCodeResponseVO();
        }
        // 进站输入参数，非空校验
        checkScanMaterialLotCodeInfo(tenantId, requestVO);

        String materialLotCode = requestVO.getMaterialLotCode();
        // 1.调用 materialLotPropertyGet获取物料批属性
        MtMaterialLot mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);
        if (Objects.isNull(mtMaterialLot)) {
            // HME_COS_INSPECT_PLATFORM_002 -->未查询到${1}信息!
            throw new MtException("HME_COS_INSPECT_PLATFORM_002", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_002", "HME", "条码"));
        }
        String materialLotId = mtMaterialLot.getMaterialLotId();

        // 校验条码不能重复进站
        checkScan(tenantId, materialLotId, materialLotCode, requestVO.getOperationId(), requestVO.getWorkcellId());

        // 2.获取物料批扩展属性
        HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId,
                materialLotId, true, materialLotCode);
        Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();

        String cosRecord = materialLotAttrMap.get(COS_RECORD);
        // 3. 根据工单工艺工位在制记录id，从COS工单作业记录表内获取：来料备注，平均波长，TYPE等信息，并显示
        HmeCosOperationRecord cosOperationRecord = getCosOperationRecord(tenantId, cosRecord);

        String materialId = hmeCosMaterialLotVO.getMaterialId();

        String wkcShiftId = requestVO.getWkcShiftId();
        String workcellId = requestVO.getWorkcellId();
        String operationId = requestVO.getOperationId();
        boolean scanFlag = true;

        // 5. 创建/更新工单工艺在制记录
        String siteId = hmeCosMaterialLotVO.getSiteId();
        long primaryUomQty = Objects.isNull(hmeCosMaterialLotVO.getPrimaryUomQty()) ? 0L
                : hmeCosMaterialLotVO.getPrimaryUomQty().longValue();

        // 6. 创建/更新工单工艺工位在制记录
        HmeCosOperationRecord hmeCosOperationRecord = insertOrUpdateHmeCosOperationRecord(tenantId, siteId, primaryUomQty, materialId,
                cosOperationRecord, requestVO, materialLotAttrMap);

        String workOrderId = hmeCosOperationRecord.getWorkOrderId();
        cosRecord = hmeCosOperationRecord.getOperationRecordId();

        // 4.创建条码作业记录：
        HmeEoJobSn hmeEoJobSn = insertHmeEoJobSn(tenantId, materialId, materialLotId, cosRecord, wkcShiftId, workcellId,
                operationId, workOrderId, scanFlag, "");

        insertOrUpdateHmeWoJobSn(tenantId, siteId, primaryUomQty, workOrderId, operationId);
        String jobId = hmeEoJobSn.getJobId();

        // 进站-返回参数
        HmeCosInspectPlatformScanMaterialLotCodeResponseVO responseVO = setScanMaterialLotCodeResponseVO(tenantId,
                hmeCosMaterialLotVO, hmeCosOperationRecord, jobId);

        return responseVO;
    }

    /**
     * 校验条码不能重复进站
     *
     * @param tenantId
     * @param materialLotId
     * @param materialLotId
     * @param operationId
     * @param workcellId
     */
    private void checkScan(Long tenantId, String materialLotId, String materialLotCode, String operationId, String workcellId) {
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setMaterialLotId(materialLotId);
        hmeEoJobSn.setOperationId(operationId);
        hmeEoJobSn.setWorkcellId(workcellId);
        hmeEoJobSn.setJobType(COS_DETECT);
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.select(hmeEoJobSn);
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            // 存在出站时间为空
            List<HmeEoJobSn> siteOutDateNotNullList = hmeEoJobSnList.stream().filter(h -> Objects.isNull(h.getSiteOutDate())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(siteOutDateNotNullList)) {
                // HME_COS_INSPECT_PLATFORM_005 --> 条码：${1}不能重复进站!
                throw new MtException("HME_COS_INSPECT_PLATFORM_005", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_005", "HME", materialLotCode));
            }
        }
    }

    /**
     * 点击行数据，查询物料装载信息
     *
     * @param tenantId
     * @param requestVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosInspectPlatformScanMaterialLotCodeResponseVO queryLoadData(Long tenantId,
                                                                            HmeCosInspectPlatformQueryInfoRequestVO requestVO) {
        // 进站输入参数，非空校验
        if (Objects.isNull(requestVO)) {
            return new HmeCosInspectPlatformScanMaterialLotCodeResponseVO();
        }
        String materialLotId = requestVO.getMaterialLotId();
        String materialLotCode = requestVO.getMaterialLotCode();
        String operationRecordId = requestVO.getOperationRecordId();
        String eoJobSnId = requestVO.getEoJobSnId();
        if (StringUtils.isBlank(materialLotId)) {
            // HME_COS_INSPECT_PLATFORM_001 --> ${1}不能为空!
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "条码"));
        }

        HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId,
                materialLotId, true, materialLotCode);
        Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();

        // 3. 根据工单工艺工位在制记录id，从COS工单作业记录表内获取：来料备注，平均波长，TYPE等信息，并显示
        HmeCosOperationRecord cosOperationRecord = getCosOperationRecord(tenantId, operationRecordId);

        HmeCosInspectPlatformScanMaterialLotCodeResponseVO responseVO = setScanMaterialLotCodeResponseVO(tenantId,
                hmeCosMaterialLotVO, cosOperationRecord, eoJobSnId);

        return responseVO;
    }

    /**
     * 查询芯片及新增数据采集项
     *
     * @param tenantId  租户ID
     * @param requestVO 参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobDataRecordVO> cosInspectionQuery(Long tenantId,
                                                         HmeCosInspectPlatformCosInspectRequestVO requestVO) {
        if (Objects.isNull(requestVO)) {
            return Collections.EMPTY_LIST;
        }
        String materialId = requestVO.getMaterialId();
        String materialLotId = requestVO.getMaterialLotId();
        String cosRecord = requestVO.getCosRecord();
        String wkcShiftId = requestVO.getWkcShiftId();
        String workcellId = requestVO.getWorkcellId();
        String operationId = requestVO.getOperationId();
        String workOrderId = requestVO.getWorkOrderId();
        String eoJobSnId = requestVO.getEoJobSnId();
        String loadSequence = requestVO.getLoadSequence();
        String siteId = requestVO.getSiteId();

        MtEo mtEo = getMtEoByLoadSequence(tenantId, loadSequence);
        if (Objects.isNull(mtEo)) {
            // loadSequence:${1}关联不到EO
            throw new MtException("HME_COS_INSPECT_PLATFORM_004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_004", "HME", loadSequence));
        }
        if (StringUtils.isBlank(materialId)) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "物料"));
        }
        HmeEoJobSn hmeEoJobSnQuery = new HmeEoJobSn();
        hmeEoJobSnQuery.setEoId(mtEo.getEoId());
        hmeEoJobSnQuery.setWorkcellId(workcellId);
        hmeEoJobSnQuery.setTenantId(tenantId);
        hmeEoJobSnQuery.setOperationId(operationId);
        HmeEoJobSn hmeEoJobSnOne = hmeEoJobSnRepository.selectOne(hmeEoJobSnQuery);
        if (Objects.isNull(hmeEoJobSnOne)) {
            boolean scanFlag = false;
            // 物料批装载单元格记录至hme_eo_job_sn表
            HmeEoJobSn hmeEoJobSn = insertHmeEoJobSn(tenantId, materialId, materialLotId, eoJobSnId, wkcShiftId, workcellId,
                    operationId, workOrderId, scanFlag, loadSequence);

            MtWorkOrder mtWorkOrder = queryWorkOrder(tenantId, workOrderId);
            // 生产版本
            String productionVersion = mtWorkOrder.getProductionVersion();

            MtMaterialBasic mtMaterialBasic = getMtMaterialBasic(tenantId, materialId);
            // 物料类型
            String itemType = Objects.isNull(mtMaterialBasic) ? "" : mtMaterialBasic.getItemType();

            // 1.工艺+物料+版本
            //V20201106 modify by penglin.sui for zhenyong.ban COS只查询DATA类型数据
            List<String> businessTypeList = new ArrayList<>();
            businessTypeList.add("DATA");
            List<HmeTagVO> hmeTagVOList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, operationId,
                    materialId, productionVersion,businessTypeList);
            if (CollectionUtils.isEmpty(hmeTagVOList)) {
                // 2.工艺+物料
                hmeTagVOList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, operationId, materialId, "",businessTypeList);
                if (CollectionUtils.isEmpty(hmeTagVOList)) {
                    // 3.物料类+工艺
                    hmeTagVOList = hmeTagMapper.operationItemTypeLimitForEoJobSn(tenantId, operationId, itemType,businessTypeList);
                    if (CollectionUtils.isEmpty(hmeTagVOList)) {
                        // 4.工艺
                        hmeTagVOList = hmeTagMapper.operationLimitForEoJobSn(tenantId, operationId,businessTypeList);
                    }
                }
            }

            hmeTagVOList = hmeTagVOList.stream().sorted(Comparator.comparing(HmeTagVO::getValueType))
                    .collect(Collectors.toList());
            List<HmeEoJobDataRecordVO> dataRecordVOList = new ArrayList<>();
            for (HmeTagVO tagVO : hmeTagVOList) {
                HmeEoJobDataRecord hmeEoJobDataRecord = HmeEoJobDataRecord.builder().tenantId(tenantId)
                        .jobId(hmeEoJobSn.getJobId()).workcellId(workcellId).eoId(eoJobSnId)
                        .tagGroupId(tagVO.getTagGroupId()).tagId(tagVO.getTagId())
                        .minimumValue(tagVO.getMinimumValue()).maximalValue(tagVO.getMaximalValue())
                        .groupPurpose(tagVO.getGroupPurpose()).isSupplement("0").build();
                hmeEoJobDataRecordRepository.insertSelective(hmeEoJobDataRecord);

                HmeEoJobDataRecordVO hmeEoJobDataRecordVO = new HmeEoJobDataRecordVO();
                hmeEoJobDataRecordVO.setResultType(tagVO.getValueType());
                hmeEoJobDataRecordVO.setTagCode(tagVO.getTagCode());
                hmeEoJobDataRecordVO.setTagDescription(tagVO.getTagDescription());

                // 获取数据采集扩展属性
                HmeTagDaqAttr hmeTagDaqAttrParam = new HmeTagDaqAttr();
                hmeTagDaqAttrParam.setTenantId(tenantId);
                hmeTagDaqAttrParam.setTagId(tagVO.getTagId());
                HmeTagDaqAttr hmeTagDaqAttr = hmeTagDaqAttrRepository.selectOne(hmeTagDaqAttrParam);
                if (Objects.nonNull(hmeTagDaqAttr)) {
                    hmeEoJobDataRecordVO.setEquipmentCategory(hmeTagDaqAttr.getEquipmentCategory());
                    hmeEoJobDataRecordVO.setValueField(hmeTagDaqAttr.getValueField());
                    hmeEoJobDataRecordVO.setLimitCond1(hmeTagDaqAttr.getLimitCond1());
                    hmeEoJobDataRecordVO.setCond1Value(hmeTagDaqAttr.getCond1Value());
                    hmeEoJobDataRecordVO.setLimitCond2(hmeTagDaqAttr.getLimitCond2());
                    hmeEoJobDataRecordVO.setCond2Value(hmeTagDaqAttr.getCond2Value());
                }

                MtTagGroupAssign mtTagGroupAssignParam = new MtTagGroupAssign();
                mtTagGroupAssignParam.setTenantId(tenantId);
                mtTagGroupAssignParam.setTagId(tagVO.getTagId());
                mtTagGroupAssignParam.setTagGroupId(hmeEoJobDataRecord.getTagGroupId());
                MtTagGroupAssign mtTagGroupAssign = mtTagGroupAssignRepository.selectOne(mtTagGroupAssignParam);
                if (Objects.nonNull(mtTagGroupAssign)) {
                    hmeEoJobDataRecordVO.setSerialNumber(mtTagGroupAssign.getSerialNumber());
                    hmeEoJobDataRecordVO.setValueAllowMissing(mtTagGroupAssign.getValueAllowMissing());
                } else {
                    hmeEoJobDataRecordVO.setSerialNumber(-99d);
                }

                BeanUtils.copyProperties(hmeEoJobDataRecord, hmeEoJobDataRecordVO);
                hmeEoJobDataRecordVO.setLoadSequence(loadSequence);
                hmeEoJobDataRecordVO.setSiteId(siteId);
                dataRecordVOList.add(hmeEoJobDataRecordVO);
            }

            // 保存数据采集结果
            List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList =
                    hmeEoJobDataRecordRepository.batchSave(tenantId, dataRecordVOList);

            if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordVOList)) {
                hmeEoJobDataRecordVOList.stream()
                        .sorted(Comparator.comparing(HmeEoJobDataRecordVO::getIsSupplement)
                                .thenComparing(HmeEoJobDataRecordVO::getResultType)
                                .thenComparing(HmeEoJobDataRecordVO::getSerialNumber))
                        .collect(Collectors.toList());
            }else{
                hmeEoJobDataRecordVOList = Collections.EMPTY_LIST;
            }
            return hmeEoJobDataRecordVOList;
        } else {
            HmeEoJobMaterialVO2 hmeEoJobMaterialVO2 = new HmeEoJobMaterialVO2();
            hmeEoJobMaterialVO2.setJobId(hmeEoJobSnOne.getJobId());
            hmeEoJobMaterialVO2.setWorkcellId(workcellId);
            List<HmeEoJobDataRecordVO> dataRecordVOList = hmeEoJobDataRecordRepository.eoJobDataRecordQuery(tenantId, hmeEoJobMaterialVO2);
            if(CollectionUtils.isEmpty(dataRecordVOList)){
                dataRecordVOList = Collections.EMPTY_LIST;
            }
            return dataRecordVOList;
        }
    }

    /**
     * 查询工单信息
     *
     * @param tenantId
     * @param workOrderId
     * @return
     * @throws MtException
     */
    private MtWorkOrder queryWorkOrder(Long tenantId, String workOrderId) throws MtException {
        MtWorkOrder mtWorkOrder = new MtWorkOrder();
        mtWorkOrder.setTenantId(tenantId);
        mtWorkOrder.setWorkOrderId(workOrderId);
        mtWorkOrder = mtWorkOrderRepository.selectOne(mtWorkOrder);
        if (Objects.isNull(mtWorkOrder)) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "工单"));
        }
        return mtWorkOrder;
    }

    /**
     * COS芯片检验
     *
     * @param tenantId 参数
     * @param dto      租户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobDataRecordVO cosInspection(Long tenantId, HmeEoJobDataRecordVO dto) {
        if (Objects.isNull(dto)) {
            return dto;
        }
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = new ArrayList<>();

        HmeEoJobDataRecord param = new HmeEoJobDataRecord();
        param.setJobRecordId(dto.getJobRecordId());
        param.setTenantId(tenantId);
        HmeEoJobDataRecord hmeEoJobDataRecord = hmeEoJobDataRecordRepository.selectByPrimaryKey(param);
        hmeEoJobDataRecordList.add(hmeEoJobDataRecord);
        // 创建数据采集实绩
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_TAG_RECORD_CREATE");
        // 创建事件
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        for (HmeEoJobDataRecord dataRecord : hmeEoJobDataRecordList) {

            MtDataRecordVO mtDataRecordVO = new MtDataRecordVO();
            mtDataRecordVO.setEoId(dataRecord.getEoId());
            mtDataRecordVO.setWorkcellId(dataRecord.getWorkcellId());
            mtDataRecordVO.setTagGroupId(dataRecord.getTagGroupId());
            mtDataRecordVO.setTagId(dataRecord.getTagId());
            mtDataRecordVO.setTagValue(dto.getResult());
            mtDataRecordVO.setEventId(eventId);

            MtTagGroupObject tagGroupObjectParam = new MtTagGroupObject();
            tagGroupObjectParam.setTenantId(tenantId);
            tagGroupObjectParam.setTagGroupId(dataRecord.getTagGroupId());
            List<MtTagGroupObject> mtTagGroupObjects = mtTagGroupObjectRepository.select(tagGroupObjectParam);
            if (CollectionUtils.isNotEmpty(mtTagGroupObjects)) {
                MtTagGroupObject mtTagGroupObject = mtTagGroupObjects.get(0);
                mtDataRecordVO.setOperationId(mtTagGroupObject.getOperationId());
            }
            HmeRouterStepVO nearStepVO = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, dataRecord.getEoId());
            if (Objects.nonNull(nearStepVO)) {
                mtDataRecordVO.setEoStepActualId(nearStepVO.getEoStepActualId());
            }
            // MtDataRecordVO6 mtDataRecordVo6 = mtDataRecordRepository.dataRecordAndHisCreate(tenantId,
            // mtDataRecordVO);

            dataRecord.setResult(dto.getResult());
            // dataRecord.setDataRecordId(mtDataRecordVo6.getDataRecordId());
            hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(dataRecord);
            BeanUtils.copyProperties(dataRecord, dto);

            // 获取数据采集扩展属性
            HmeTagDaqAttr hmeTagDaqAttrParam = new HmeTagDaqAttr();
            hmeTagDaqAttrParam.setTenantId(tenantId);
            hmeTagDaqAttrParam.setTagId(dataRecord.getTagId());
            HmeTagDaqAttr hmeTagDaqAttr = hmeTagDaqAttrRepository.selectOne(hmeTagDaqAttrParam);
            if (Objects.nonNull(hmeTagDaqAttr)) {
                dto.setEquipmentCategory(hmeTagDaqAttr.getEquipmentCategory());
                dto.setValueField(hmeTagDaqAttr.getValueField());
                dto.setLimitCond1(hmeTagDaqAttr.getLimitCond1());
                dto.setCond1Value(hmeTagDaqAttr.getCond1Value());
                dto.setLimitCond2(hmeTagDaqAttr.getLimitCond2());
                dto.setCond2Value(hmeTagDaqAttr.getCond2Value());
            }
        }

        // 更新/插入 HME_COS_FUNCTION表
        updateOrInsertHmeCosFunction(tenantId, dto);
        return dto;
    }

    /**
     * COS检验平台-出站前校验
     *
     * @param tenantId
     * @param responseVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosInspectPlatformSiteOutRequestVO checkSiteOut(Long tenantId, HmeCosInspectPlatformSiteOutRequestVO responseVO) {
        if (Objects.isNull(responseVO)) {
            return responseVO;
        }
        String workcellId = responseVO.getWorkcellId();
        String operationId = responseVO.getOperationId();
        List<HmeMaterialLotLoadVO2> materialLotLoadList = responseVO.getMaterialLotLoadList();
        // 根据hme_material_lot_load表的load_sequence关联mt_eo表的EO_NUM取出EO_ID，
        // 根据workcell_id+EO_ID+ operation_id关联hme_eo_job_sn表
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            materialLotLoadList.stream().forEach(materialLotLoad -> {
                String loadSequence = materialLotLoad.getLoadSequence();
                MtEo mtEo = getMtEoByLoadSequence(tenantId, loadSequence);
                if (Objects.isNull(mtEo)) {
                    // 存在未检测芯片，是否确认完成？
                    throw new MtException("HME_COS_INSPECT_PLATFORM_003", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_003", "HME", ""));
                }
                HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
                hmeEoJobSn.setTenantId(tenantId);
                hmeEoJobSn.setWorkcellId(workcellId);
                hmeEoJobSn.setEoId(mtEo.getEoId());
                hmeEoJobSn.setOperationId(operationId);
                HmeEoJobSn hmeEoJobSnOne = hmeEoJobSnRepository.selectOne(hmeEoJobSn);
                if (Objects.isNull(hmeEoJobSnOne)) {
                    // 存在未检测芯片，是否确认完成？
                    throw new MtException("HME_COS_INSPECT_PLATFORM_003", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_003", "HME", ""));
                }
            });
        }
        return responseVO;
    }

    /**
     * TODO 更新/新增hme_cos_function表
     *
     * @param tenantId
     * @param dto
     */
    private void updateOrInsertHmeCosFunction(Long tenantId, HmeEoJobDataRecordVO dto) {
        String loadSequence = dto.getLoadSequence();
        // 数据项编码  3A-A01
        String tagCodes = dto.getTagCode();
        // 获取值集   含义：current   描述：字段名
        List<LovValueDTO> typeLovList = lovAdapter.queryLovValue("COS_RECORD_FUNCTION_REL", tenantId);
        LovValueDTO typeLov = null;
        for (LovValueDTO type : typeLovList) {
            if (StringUtils.equals(type.getValue(), tagCodes)) {
                typeLov = type;
            }
        }
        if (Objects.nonNull(typeLov)) {
            String current = typeLov.getMeaning();
            String tagCode = typeLov.getDescription();
            String siteId = dto.getSiteId();
            String result = dto.getResult();
            HmeCosFunction hmeCosFunction = hmeCosFunctionMapper.selectByLoadSequence(tenantId, loadSequence, current);
            if (checkTagCodeExist(tagCode)) {
                if (Objects.nonNull(hmeCosFunction)) {
                    // 更新
                    hmeCosFunction = setHmeCosFunctionTagCode(hmeCosFunction, tagCode, result);
                    hmeCosFunctionMapper.updateByPrimaryKey(hmeCosFunction);
                } else {
                    // 插入
                    HmeCosFunction hmeCosFunction1 = new HmeCosFunction();
                    hmeCosFunction1.setTenantId(tenantId);
                    hmeCosFunction1.setSiteId(siteId);
                    hmeCosFunction1.setLoadSequence(loadSequence);
                    hmeCosFunction1.setCurrent(current);
                    hmeCosFunction1 = setHmeCosFunctionTagCode(hmeCosFunction1, tagCode, result);
                    hmeCosFunctionRepository.insertSelective(hmeCosFunction1);
                }
            }
        }
    }

    /**
     * 更新该表字段名等于TAG_CODE的字段值
     *
     * @param hmeCosFunctionOne
     * @param tagCode
     * @param result
     * @return
     */
    private HmeCosFunction setHmeCosFunctionTagCode(HmeCosFunction hmeCosFunctionOne, String tagCode, String result) {
        switch (tagCode) {
            case "A01":
                hmeCosFunctionOne.setA01(result);
                break;
            case "A02":
                hmeCosFunctionOne.setA02(new BigDecimal(result));
                break;
            case "A03":
                hmeCosFunctionOne.setA03(result);
                break;
            case "A04":
                hmeCosFunctionOne.setA04(new BigDecimal(result));
                break;
            case "A05":
                hmeCosFunctionOne.setA05(new BigDecimal(result));
                break;
            case "A06":
                hmeCosFunctionOne.setA06(new BigDecimal(result));
                break;
            case "A07":
                hmeCosFunctionOne.setA07(new BigDecimal(result));
                break;
            case "A08":
                hmeCosFunctionOne.setA08(result);
                break;
            case "A09":
                hmeCosFunctionOne.setA09(result);
                break;
            case "A010":
                hmeCosFunctionOne.setA010(new BigDecimal(result));
                break;
            case "A011":
                hmeCosFunctionOne.setA011(new BigDecimal(result));
                break;
            case "A012":
                hmeCosFunctionOne.setA012(new BigDecimal(result));
                break;
            case "A013":
                hmeCosFunctionOne.setA013(new BigDecimal(result));
                break;
            case "A014":
                hmeCosFunctionOne.setA014(new BigDecimal(result));
                break;
            default:
                break;
        }
        return hmeCosFunctionOne;
    }

    /**
     * 校验是否存在属性名为tagCode值的字段
     *
     * @param tagCode
     * @return
     */
    private boolean checkTagCodeExist(String tagCode) {
        HmeCosFunction hmeCosFunction = new HmeCosFunction();
        AtomicBoolean tagCodExist = new AtomicBoolean(false);
        Field[] fields = hmeCosFunction.getClass().getFields();
        List<Field> fieldList = Arrays.asList(fields);
        fieldList.stream().forEach(field -> {
            try {
                if (tagCode.equalsIgnoreCase(field.get(hmeCosFunction).toString())) {
                    tagCodExist.set(true);
                }
            } catch (IllegalAccessException e) {
                throw new MtException(e);
            }
        });
        return tagCodExist.get();
    }

    /**
     * 获取mt_material_basic信息
     *
     * @param tenantId
     * @param materialId
     * @return
     */
    private MtMaterialBasic getMtMaterialBasic(Long tenantId, String materialId) {
        MtMaterialBasic mtMaterialBasic = new MtMaterialBasic();
        mtMaterialBasic.setTenantId(tenantId);
        mtMaterialBasic.setMaterialId(materialId);
        return mtMaterialBasisRepository.selectOne(mtMaterialBasic);
    }

    /**
     * 进站扫描盒子-返回参数
     *
     * @param tenantId
     * @param hmeCosMaterialLotVO
     * @param cosOperationRecord
     * @param jobId
     * @return
     */
    private HmeCosInspectPlatformScanMaterialLotCodeResponseVO setScanMaterialLotCodeResponseVO(Long tenantId, HmeCosMaterialLotVO hmeCosMaterialLotVO,
                                                                                                HmeCosOperationRecord cosOperationRecord, String jobId) {
        MtMaterialVO mtMaterialVO =
                mtMaterialRepository.materialPropertyGet(tenantId, hmeCosMaterialLotVO.getMaterialId());
        if (Objects.isNull(mtMaterialVO) || StringUtils.isEmpty(mtMaterialVO.getMaterialId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_002", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_002", "HME", "物料"));
        }
        Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();
        String workOrderId = cosOperationRecord.getWorkOrderId();
        String workOrderNum = "";
        MtWorkOrder mtWorkOrder = new MtWorkOrder();
        if (StringUtils.isNotBlank(workOrderId)) {
            mtWorkOrder.setTenantId(tenantId);
            mtWorkOrder.setWorkOrderId(workOrderId);
            mtWorkOrder = mtWorkOrderRepository.selectOne(mtWorkOrder);
            if (Objects.nonNull(mtWorkOrder)) {
                workOrderNum = mtWorkOrder.getWorkOrderNum();
            }
        }

        HmeCosInspectPlatformScanMaterialLotCodeResponseVO responseVO =
                new HmeCosInspectPlatformScanMaterialLotCodeResponseVO();

        HmeCosInspectPlatformAutoQueryInfoResponseVO queryInfoResponseVO =
                new HmeCosInspectPlatformAutoQueryInfoResponseVO();
        queryInfoResponseVO.setOperationRecordId(materialLotAttrMap.get(COS_RECORD));
        if (StringUtils.isNotBlank(workOrderId)) {
            queryInfoResponseVO.setWorkOrderId(workOrderId);
            queryInfoResponseVO.setWorkOrderNum(workOrderNum);
        }
        queryInfoResponseVO.setMaterialLotId(hmeCosMaterialLotVO.getMaterialLotId());
        queryInfoResponseVO.setMaterialLotCode(hmeCosMaterialLotVO.getMaterialLotCode());
        queryInfoResponseVO.setPrimaryUomQty(String.valueOf(hmeCosMaterialLotVO.getPrimaryUomQty() == null ? "0" : hmeCosMaterialLotVO.getPrimaryUomQty()));
        queryInfoResponseVO.setWafer(materialLotAttrMap.get(WAFER_NUM));
        queryInfoResponseVO.setLotNo(materialLotAttrMap.get("LOT_NO"));
        queryInfoResponseVO.setMaterialId(hmeCosMaterialLotVO.getMaterialId());
        queryInfoResponseVO.setMaterialCode(mtMaterialVO.getMaterialCode());
        queryInfoResponseVO.setMaterialName(mtMaterialVO.getMaterialName());
        queryInfoResponseVO.setRemark(cosOperationRecord.getRemark());

        responseVO.setQueryInfoResponseVO(queryInfoResponseVO);
        responseVO.setLocationRow(materialLotAttrMap.get(LOCATION_ROW));
        responseVO.setLocationColumn(materialLotAttrMap.get(LOCATION_COLUMN));
        responseVO.setChipNum(materialLotAttrMap.get(CHIP_NUM));
        responseVO.setEoJobSnId(jobId);

        // 根据物料批查询装载表数据
        List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository
                .queryLoadDetailByMaterialLotId(tenantId, hmeCosMaterialLotVO.getMaterialLotId());
        List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList1 = new ArrayList<>();
        for (int i = 1; i <= Integer.parseInt(responseVO.getLocationRow()); i++) {
            for (int j = 1; j <= Integer.parseInt(responseVO.getLocationColumn()); j++) {
                Long loadRow = (long) i;
                Long loadColumn = (long) j;
                List<HmeMaterialLotLoadVO2> collect = hmeMaterialLotLoadList.stream()
                        .filter(t -> t.getLoadRow().equals(loadRow) && t.getLoadColumn().equals(loadColumn))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    hmeMaterialLotLoadList1.add(collect.get(0));
                } else {
                    hmeMaterialLotLoadList1.add(new HmeMaterialLotLoadVO2());
                }
            }
        }
        responseVO.setMaterialLotLoadList(hmeMaterialLotLoadList1);
        return responseVO;
    }

    /**
     * COS条码完成检验出站
     *
     * @param tenantId
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void siteOut(Long tenantId, HmeCosInspectPlatformSiteOutRequestVO dto) {
        if (Objects.isNull(dto)) {
            return;
        }
        // 出站输入参数，非空校验
        checkSiteOutInfo(tenantId, dto);
        String workcellId = dto.getWorkcellId();
        String operationId = dto.getOperationId();

        // 循环处理
        List<String> materialLotIdList = new ArrayList<>();
        for (HmeMaterialLotLoadVO2 materialLotVO : dto.getMaterialLotLoadList()) {
            if (Objects.nonNull(materialLotVO)) {
                String loadSequence = materialLotVO.getLoadSequence();
                String eoId = "";
                MtEo mtEo = getMtEoByLoadSequence(tenantId, loadSequence);

                if (Objects.nonNull(mtEo)) {
                    eoId = mtEo.getEoId();
                }

                // 查询HmeEoJobSn表数据
                List<HmeEoJobSn> hmeEoJobSnList = queryHmeEoJobSnByCondition(tenantId, workcellId, eoId, operationId, materialLotVO.getMaterialLotId());

                if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                    CustomUserDetails userDetails = DetailsHelper.getUserDetails();
                    Long userId = Objects.isNull(userDetails) ? -1L : userDetails.getUserId();
                    Date nowDate = new Date();
                    List<HmeEoJobSn> objects = new ArrayList<HmeEoJobSn>(hmeEoJobSnList.size());
                    hmeEoJobSnList.stream().forEach(hmeEoJobSn -> {
                        hmeEoJobSn.setSiteOutDate(nowDate);
                        hmeEoJobSn.setSiteOutBy(userId);
                        objects.add(hmeEoJobSn);
                    });
                    hmeEoJobSnRepository.batchUpdateByPrimaryKeySelective(objects);
                }

                // 更新工单工艺在制记录
                HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
                hmeWoJobSn.setTenantId(tenantId);
                hmeWoJobSn.setWorkOrderId(dto.getWorkOrderId());
                hmeWoJobSn.setOperationId(dto.getOperationId());
                hmeWoJobSn = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
                if (Objects.isNull(hmeWoJobSn) || StringUtils.isEmpty(hmeWoJobSn.getWoJobSnId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_CHIP_TRANSFER_013", "HME", "工单工艺在制记录数据"));
                }
                Long processedNum = Objects.isNull(hmeWoJobSn.getProcessedNum()) ? 1L
                        : hmeWoJobSn.getProcessedNum() + 1L;
                hmeWoJobSn.setProcessedNum(processedNum);
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);

                materialLotIdList.add(materialLotVO.getMaterialLotId());
            }
        }
        // 更新工单工艺工位在制记录剩余芯片数
        HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
        hmeCosOperationRecord.setOperationRecordId(dto.getOperationRecordId());
        hmeCosOperationRecord = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecord);
        if (Objects.isNull(hmeCosOperationRecord)
                || StringUtils.isEmpty(hmeCosOperationRecord.getOperationRecordId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_CHIP_TRANSFER_013", "HME", "工单工艺工位在制记录数据"));
        }
        if (Objects.isNull(hmeCosOperationRecord.getSurplusCosNum())) {
            throw new MtException("Exception", "工单工艺工位在制记录剩余芯片数为空！");
        }
        MtMaterialLot mtMaterialLot = getMtMaterialLot(tenantId, dto.getMaterialLotId());
        Long primaryUomQty = 0L;
        if (Objects.nonNull(mtMaterialLot)) {
            primaryUomQty = mtMaterialLot.getPrimaryUomQty().longValue();
        }
        hmeCosOperationRecord.setSurplusCosNum(
                hmeCosOperationRecord.getSurplusCosNum() - primaryUomQty);
        hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHis.setTenantId(tenantId);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
    }

    private MtMaterialLot getMtMaterialLot(Long tenantId, String materialLotId) {
        return mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotId(materialLotId);
        }});
    }

    /**
     * 查询HmeEoJobSn表数据
     *
     * @param tenantId
     * @param workcellId
     * @param eoId
     * @param operationId
     * @param materialLotId
     * @return
     */
    private List<HmeEoJobSn> queryHmeEoJobSnByCondition(Long tenantId, String workcellId, String eoId, String operationId, String materialLotId) {
        List<String> jobTypeList = new ArrayList<>();
        jobTypeList.add(COS_DETECT);
        jobTypeList.add(COS_UNIT_DETECT);
        return hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, workcellId)
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, operationId)
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLotId)
                        .andEqualTo(HmeEoJobSn.FIELD_SITE_OUT_DATE, null)
                        .andIn(HmeEoJobSn.FIELD_JOB_TYPE, jobTypeList))
                .build());
    }

    /**
     * 更新/新增hme_cos_operation_record表
     *
     * @param tenantId
     * @param siteId
     * @param primaryUomQty
     * @param materialId
     * @param cosOperationRecord
     * @param requestVO
     * @param materialLotAttrMap
     * @return
     */
    private HmeCosOperationRecord insertOrUpdateHmeCosOperationRecord(Long tenantId, String siteId, long primaryUomQty,
                                                                      String materialId, HmeCosOperationRecord cosOperationRecord,
                                                                      HmeCosInspectPlatformQueryInfoRequestVO requestVO, Map<String, String> materialLotAttrMap) {
        HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
        hmeCosOperationRecord.setTenantId(tenantId);
        if (StringUtils.isNotBlank(cosOperationRecord.getWorkOrderId())) {
            hmeCosOperationRecord.setWorkOrderId(cosOperationRecord.getWorkOrderId());
        }
        hmeCosOperationRecord.setOperationId(requestVO.getOperationId());
        hmeCosOperationRecord.setWorkcellId(requestVO.getWorkcellId());
        if (StringUtils.isNotBlank(cosOperationRecord.getWafer())) {
            hmeCosOperationRecord.setWafer(cosOperationRecord.getWafer());
        }
        if (StringUtils.isNotBlank(requestVO.getEquipmentId())) {
            hmeCosOperationRecord.setEquipmentId(requestVO.getEquipmentId());
        }
        hmeCosOperationRecord = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecord);
        if (Objects.isNull(hmeCosOperationRecord)
                || StringUtils.isEmpty(hmeCosOperationRecord.getOperationRecordId())) {
            hmeCosOperationRecord = new HmeCosOperationRecord();
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setWorkOrderId(cosOperationRecord.getWorkOrderId());
            hmeCosOperationRecord.setWorkcellId(requestVO.getWorkcellId());
            hmeCosOperationRecord.setOperationId(requestVO.getOperationId());
            hmeCosOperationRecord.setWafer(cosOperationRecord.getWafer());
            hmeCosOperationRecord.setEquipmentId(requestVO.getEquipmentId());
            hmeCosOperationRecord.setSiteId(siteId);
            hmeCosOperationRecord.setCosNum(primaryUomQty);
            hmeCosOperationRecord.setSurplusCosNum(primaryUomQty);
            hmeCosOperationRecord.setMaterialId(materialId);
            hmeCosOperationRecord.setContainerTypeId(materialLotAttrMap.get("CONTAINER_TYPE"));
            hmeCosOperationRecord.setCosType(materialLotAttrMap.get("COS_TYPE"));
            if (StringUtils.isNotBlank(materialLotAttrMap.get("AVG_WAVE_LENGTH"))) {
                hmeCosOperationRecord.setAverageWavelength(new BigDecimal(materialLotAttrMap.get("AVG_WAVE_LENGTH")));
            }
            hmeCosOperationRecord.setType(materialLotAttrMap.get("TYPE"));
            hmeCosOperationRecord.setLotNo(materialLotAttrMap.get("LOTNO"));
            hmeCosOperationRecord.setRemark(materialLotAttrMap.get("REMARK"));
            hmeCosOperationRecord.setJobBatch(materialLotAttrMap.get("WORKING_LOT"));
            hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        } else {
            Long cosNum = Objects.isNull(hmeCosOperationRecord.getCosNum()) ? primaryUomQty
                    : hmeCosOperationRecord.getCosNum() + primaryUomQty;
            Long surplusCosNum = Objects.isNull(hmeCosOperationRecord.getSurplusCosNum()) ? primaryUomQty
                    : hmeCosOperationRecord.getSurplusCosNum() + primaryUomQty;
            hmeCosOperationRecord.setSiteId(siteId);
            hmeCosOperationRecord.setCosNum(cosNum);
            hmeCosOperationRecord.setSurplusCosNum(surplusCosNum);
            hmeCosOperationRecord.setMaterialId(materialId);
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHis.setTenantId(tenantId);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        }
        return hmeCosOperationRecord;
    }

    /**
     * 查询工单工艺工位在制记录信息
     *
     * @param tenantId
     * @param cosRecord
     */
    private HmeCosOperationRecord getCosOperationRecord(Long tenantId, String cosRecord) {
        HmeCosOperationRecord cosOperationRecord = new HmeCosOperationRecord();
        cosOperationRecord.setTenantId(tenantId);
        cosOperationRecord.setOperationRecordId(cosRecord);
        cosOperationRecord = hmeCosOperationRecordRepository.selectOne(cosOperationRecord);
        if (Objects.isNull(cosOperationRecord) || StringUtils.isEmpty(cosOperationRecord.getOperationRecordId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_002", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_002", "HME", "工单工艺工位在制记录"));
        }
        return cosOperationRecord;
    }

    /**
     * 扫描盒子-校验输入参数
     *
     * @param tenantId
     * @param inVO
     */
    private void checkScanMaterialLotCodeInfo(Long tenantId, HmeCosInspectPlatformQueryInfoRequestVO inVO) {
        if (StringUtils.isBlank(inVO.getMaterialLotCode())) {
            // HME_COS_INSPECT_PLATFORM_001 --> ${1}不能为空!
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "条码"));
        }
        if (StringUtils.isBlank(inVO.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺"));
        }
        if (StringUtils.isBlank(inVO.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "工位"));
        }
        if (StringUtils.isBlank(inVO.getWkcShiftId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "班组"));
        }
    }

    /**
     * 校验出站输入参数
     *
     * @param tenantId
     * @param dto
     */
    private void checkSiteOutInfo(Long tenantId, HmeCosInspectPlatformSiteOutRequestVO dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位"));
        }
        if (StringUtils.isEmpty(dto.getOperationRecordId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单工艺工位在制记录ID"));
        }
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单ID"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺ID"));
        }
    }

    /**
     * 创建/更新工单工艺在制记录
     *
     * @param tenantId
     * @param siteId
     * @param primaryUomQty
     * @param workOrderId
     * @param operationId
     */
    private void insertOrUpdateHmeWoJobSn(Long tenantId, String siteId, long primaryUomQty, String workOrderId,
                                          String operationId) {

        // 创建/更新工单工艺在制记录
        HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
        hmeWoJobSn.setTenantId(tenantId);
        hmeWoJobSn.setWorkOrderId(workOrderId);
        hmeWoJobSn.setOperationId(operationId);
        hmeWoJobSn = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
        if (Objects.isNull(hmeWoJobSn) || StringUtils.isEmpty(hmeWoJobSn.getWoJobSnId())) {
            hmeWoJobSn = new HmeWoJobSn();
            hmeWoJobSn.setTenantId(tenantId);
            hmeWoJobSn.setWorkOrderId(workOrderId);
            hmeWoJobSn.setOperationId(operationId);
            hmeWoJobSn.setSiteId(siteId);
            hmeWoJobSn.setSiteInNum(primaryUomQty);
            hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
        } else {
            hmeWoJobSn.setSiteId(siteId);
            hmeWoJobSn.setSiteInNum(primaryUomQty);
            hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
        }
    }

    /**
     * 创建条码作业记录
     *
     * @param tenantId
     * @param materialId
     * @param materialLotId
     * @param jobId
     * @param wkcShiftId
     * @param workcellId
     * @param operationId
     * @param workOrderId
     * @param scanFlag
     * @param loadSequence
     * @return
     */
    private HmeEoJobSn insertHmeEoJobSn(Long tenantId, String materialId, String materialLotId, String jobId,
                                        String wkcShiftId, String workcellId, String operationId, String workOrderId, boolean scanFlag,
                                        String loadSequence) {

        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = Objects.isNull(userDetails) ? -1L : userDetails.getUserId();

        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setShiftId(wkcShiftId);
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setWorkcellId(workcellId);
        hmeEoJobSn.setWorkOrderId(workOrderId);
        hmeEoJobSn.setOperationId(operationId);
        hmeEoJobSn.setSnMaterialId(materialId);
        hmeEoJobSn.setMaterialLotId(materialLotId);
        hmeEoJobSn.setEoStepNum(1);
        hmeEoJobSn.setReworkFlag("N");

        hmeEoJobSn.setSourceJobId(jobId);
        hmeEoJobSn.setSiteInDate(new Date());
        if (scanFlag) {
            hmeEoJobSn.setJobType(COS_DETECT);
        } else {
            hmeEoJobSn.setJobType(COS_UNIT_DETECT);
            MtEo mtEo = getMtEoByLoadSequence(tenantId, loadSequence);
            if (Objects.nonNull(mtEo)) {
                hmeEoJobSn.setEoId(mtEo.getEoId());
            }
        }
        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
        return hmeEoJobSn;
    }

    /**
     * 根据loadSequence获取mt_eo表的数据
     *
     * @param tenantId
     * @param loadSequence
     * @return
     */
    private MtEo getMtEoByLoadSequence(Long tenantId, String loadSequence) {
        MtEo mtEo = new MtEo();
        mtEo.setTenantId(tenantId);
        mtEo.setEoNum(loadSequence);
        mtEo = mtEoRepository.selectOne(mtEo);
        return mtEo;
    }

}