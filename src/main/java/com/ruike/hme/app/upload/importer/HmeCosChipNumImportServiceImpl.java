package com.ruike.hme.app.upload.importer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosOperationRecordMapper;
import com.ruike.hme.infra.mapper.HmeWoJobSnMapper;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.CustomUserDetails;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.BatchImportHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.choerodon.core.oauth.DetailsHelper;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.api.dto.MtTagGroupDTO5;
import tarzan.general.api.dto.MtTagGroupObjectDTO2;
import tarzan.general.app.service.MtTagGroupService;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * COS?????????-????????????
 *
 * @author jiangling.zheng@hand-china.com 2020-9-16 16:45:59
 */
@ImportService(templateCode = "HME.COS_CHIP_NUM_IMP")
public class HmeCosChipNumImportServiceImpl extends BatchImportHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;
    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // ????????????Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // ??????????????????????????????
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        // ?????????????????????
        Map<String, Object> args = getArgs();
        if (args.isEmpty()) {
            throw new MtException("HME_COS_CHIP_IMP_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_CHIP_IMP_0001", "HME"));
        }
        String workcellId = args.get("workcellId").toString();
        String operationId = args.get("operationId").toString();
        String wkcShiftId = args.get("wkcShiftId").toString();
        if (StringUtils.isBlank(workcellId)) {
            throw new MtException("HME_COS_CHIP_IMP_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_CHIP_IMP_0001", "HME"));
        }
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeCosChipNumImportVO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeCosChipNumImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeCosChipNumImportVO.class);
                } catch (IOException e) {
                    // ??????
                    return false;
                }
                importVOList.add(importVO);
            }
            // ??????????????????
            Map<String, List<HmeCosChipNumImportVO>> importVOMap = importVOList.stream()
                    .collect(Collectors.groupingBy(HmeCosChipNumImportVO::getMaterialLotCode));
            for (Map.Entry<String, List<HmeCosChipNumImportVO>> entry : importVOMap.entrySet()) {
                // ??????????????????????????????????????????WAFER
                List<HmeCosChipNumImportVO> impVoList = entry.getValue();
                List<String> wafers = impVoList.stream().map(HmeCosChipNumImportVO::getWafer).distinct()
                        .collect(Collectors.toList());
                List<String> loadPositions = impVoList.stream().map(HmeCosChipNumImportVO::getLoadPosition).distinct()
                        .collect(Collectors.toList());
                if (wafers.size() > 1) {
                    throw new MtException("HME_COS_CHIP_IMP_0021", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_COS_CHIP_IMP_0021", "HME", entry.getKey()));
                }
                if (loadPositions.size() < impVoList.size()) {
                    throw new MtException("HME_COS_CHIP_IMP_0022", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_COS_CHIP_IMP_0022", "HME", entry.getKey()));
                }
                HmeCosChipNumImportVO2 vo2;
                for (HmeCosChipNumImportVO vo : impVoList) {
                    vo2 = new HmeCosChipNumImportVO2();
                    BeanUtils.copyProperties(vo, vo2);
                    vo.setSiteId(siteId);
                    vo.setOperationId(operationId);
                    vo.setWorkcellId(workcellId);
                    vo.setWkcShiftId(wkcShiftId);
                    // ???????????????
                    HmeCosChipNumImportVO4 properties = getProperties(tenantId, vo);
                    vo.setMaterialId(properties.getMaterialId());
                    vo.setMaterialLotId(properties.getMaterialLotId());
                    vo.setContainerTypeId(properties.getContainerTypeId());
                    vo.setWorkOrderId(properties.getWorkOrderId());
                    vo.setCapacity(properties.getCapacity());
                    vo.setColumnNum(properties.getColumnNum());
                    vo.setLineNum(properties.getLineNum());
                    vo.setLot(properties.getLot());
                    // ??????????????????????????????
                    HmeCosChipNumImportVO3 qtyVo = new HmeCosChipNumImportVO3();
                    try {
                        qtyVo = allFieldProcess(vo2);
                    } catch (Exception e) {
                        throw new MtException("HME_COS_CHIP_IMP_0018", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_COS_CHIP_IMP_0018", "HME"));
                    }
                    // ?????????????????????????????????????????????
                    if (!qtyVo.getTotalQty().equals(vo.getCapacity())) {
                        throw new MtException("HME_COS_CHIP_IMP_0019", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_COS_CHIP_IMP_0019", "HME"));
                    }
                    vo.setBadQty(qtyVo.getBadQty());
                    // ??????WO jobSn
                    jobSnUpdate(tenantId, vo);
                    // ????????????????????????
                    String operationRecordId = cosOperationUpdate(tenantId, vo);
                    vo.setOperationRecordId(operationRecordId);
                    // ???????????????????????????
                    materialLotAttrUpdate(tenantId, vo);
                    // ???????????????
                    String loadSequence = materialLotLoadUpdate(tenantId, vo);
                    vo.setLoadSequence(loadSequence);
                    // ????????????????????????
                    try {
                        isAllFieldBadPos(tenantId, vo, vo2);
                    } catch (Exception e) {
                        throw new MtException("HME_COS_CHIP_IMP_0023", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_COS_CHIP_IMP_0023", "HME"));
                    }
                    //??????EO jonSn
                        eoJobSnUpdate(tenantId, vo, userId);
                }
            }
        }
        return true;
    }

    /**
     * ???????????????
     *
     * @param tenantId
     * @param vo
     * @return java.lang.Long
     * @author jiangling.zheng@hand-china.com 2020/9/17 18:53
     */
    private HmeCosChipNumImportVO4 getProperties(Long tenantId, HmeCosChipNumImportVO vo) {
        // ??????
        String workOrderId = mtWorkOrderRepository.numberLimitWoGet(tenantId, vo.getWorkOrderNum());
        // ????????????
        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {
            {
                setContainerTypeCode(vo.getContainerType());
                setTenantId(tenantId);
            }
        });
        // ?????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {
            {
                setTenantId(tenantId);
                setMaterialLotCode(vo.getMaterialLotCode());
                setEnableFlag(HmeConstants.ConstantValue.YES);
                setQualityStatus(HmeConstants.ConstantValue.OK);
            }
        });
        // ????????????????????????????????????
        HmeContainerCapacity hmeContainerCapacity =
                hmeContainerCapacityRepository.selectOne(new HmeContainerCapacity() {
                    {
                        setOperationId(vo.getOperationId());
                        setContainerTypeId(mtContainerType.getContainerTypeId());
                        setCosType(vo.getCosType());
                        setTenantId(tenantId);
                        setEnableFlag(HmeConstants.ConstantValue.YES);
                    }
                });
        HmeCosChipNumImportVO4 vo4 = new HmeCosChipNumImportVO4();
        vo4.setWorkOrderId(workOrderId);
        vo4.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        vo4.setMaterialId(mtMaterialLot.getMaterialId());
        vo4.setContainerTypeId(mtContainerType.getContainerTypeId());
        vo4.setCapacity(hmeContainerCapacity.getCapacity());
        vo4.setColumnNum(hmeContainerCapacity.getColumnNum());
        vo4.setLineNum(hmeContainerCapacity.getLineNum());
        vo4.setLot(mtMaterialLot.getLot());
        return vo4;
    }

    /**
     * ??????????????????????????????
     *
     * @param obj
     * @return java.lang.Long
     * @author jiangling.zheng@hand-china.com 2020/9/17 18:54
     */
    private static HmeCosChipNumImportVO3 allFieldProcess(Object obj) throws Exception {
        Class<?> clazz = obj.getClass();
        // ??????????????????
        Field[] fs = clazz.getDeclaredFields();
        Long totalQty = 0L;
        Long badQty = 0L;
        // ????????????
        for (Field f : fs) {
            f.setAccessible(true);
            // ?????????????????????
            Object val = f.get(obj);
            // ????????????????????????
            if ("".equals(Optional.ofNullable(val).orElse(""))) {
                break;
            }
            // ?????????0?????????
            if ("0".equals(Optional.ofNullable(val).orElse(""))) {
                badQty++;
            }
            // ???????????????
            totalQty++;
        }
        HmeCosChipNumImportVO3 vo = new HmeCosChipNumImportVO3();
        vo.setTotalQty(totalQty);
        vo.setBadQty(badQty);
        return vo;
    }

    /**
     * ??????WO JobSn
     *
     * @param tenantId
     * @param vo
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/18 11:51
     */

    private void jobSnUpdate(Long tenantId, HmeCosChipNumImportVO vo) {
        // ??????sn????????????
        HmeWoJobSn jobSn = hmeWoJobSnRepository.selectOne(new HmeWoJobSn() {
            {
                setSiteId(vo.getSiteId());
                setTenantId(tenantId);
                setOperationId(vo.getOperationId());
                setWorkOrderId(vo.getWorkOrderId());
            }
        });
        HmeWoJobSn newJonSn = !Objects.isNull(jobSn) ? jobSn : new HmeWoJobSn();
        newJonSn.setTenantId(tenantId);
        newJonSn.setSiteId(vo.getSiteId());
        newJonSn.setWorkOrderId(vo.getWorkOrderId());
        newJonSn.setOperationId(vo.getOperationId());
        newJonSn.setSiteInNum(vo.getCapacity());
        newJonSn.setUnqualifiedNum(vo.getBadQty());
        newJonSn.setProcessedNum(vo.getCapacity());
        if (!Objects.isNull(jobSn)) {
            hmeWoJobSnMapper.updateByPrimaryKey(newJonSn);
        } else {
            hmeWoJobSnRepository.insertSelective(newJonSn);
        }
    }

    private String cosOperationUpdate(Long tenantId, HmeCosChipNumImportVO vo) {
        //????????????+??????+WAFER+??????????????????????????????????????????
        HmeCosOperationRecord cosOperationRecord = hmeCosOperationRecordRepository.selectOne(new HmeCosOperationRecord() {{
            setTenantId(tenantId);
            setWorkOrderId(vo.getWorkOrderId());
            setOperationId(vo.getOperationId());
            setWafer(vo.getWafer());
            setWorkcellId(vo.getWorkcellId());
            setAttribute1(vo.getLot());
        }});
        cosOperationRecord.setSurplusCosNum(cosOperationRecord.getSurplusCosNum() - vo.getCapacity());
        hmeCosOperationRecordMapper.updateByPrimaryKeySelective(cosOperationRecord);

        // ??????????????????
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(cosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        return cosOperationRecord.getOperationRecordId();
    }

    /**
     * ???????????????????????????
     *
     * @param tenantId
     * @param vo
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/18 11:51
     */

    private void materialLotAttrUpdate(Long tenantId, HmeCosChipNumImportVO vo) {
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode("COS_INCOMING");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        List<MtExtendVO5> mtLotExtendList = new ArrayList<>();
        MtExtendVO5 tmpVo = null;
        if (StringUtils.isNotBlank(vo.getOperationRecordId())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("COS_RECORD");
            tmpVo.setAttrValue(vo.getOperationRecordId());
            mtLotExtendList.add(tmpVo);
        }
        if (StringUtils.isNotBlank(vo.getWafer())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("WAFER_NUM");
            tmpVo.setAttrValue(vo.getWafer());
            mtLotExtendList.add(tmpVo);
        }
        if (StringUtils.isNotBlank(vo.getCosType())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("COS_TYPE");
            tmpVo.setAttrValue(vo.getCosType());
            mtLotExtendList.add(tmpVo);
        }
        if (vo.getLineNum() != null) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("LOCATION_ROW");
            tmpVo.setAttrValue(String.valueOf(vo.getLineNum()));
            mtLotExtendList.add(tmpVo);
        }
        if (vo.getColumnNum() != null) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("LOCATION_COLUMN");
            tmpVo.setAttrValue(String.valueOf(vo.getColumnNum()));
            mtLotExtendList.add(tmpVo);
        }
        if (vo.getCapacity() != null) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("CHIP_NUM");
            tmpVo.setAttrValue(String.valueOf(vo.getCapacity()));
            mtLotExtendList.add(tmpVo);
        }
        tmpVo = new MtExtendVO5();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tmpVo.setAttrName("PRODUCT_DATE");
        tmpVo.setAttrValue(sdf.format(new Date()));
        mtLotExtendList.add(tmpVo);
        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("STATUS");
        tmpVo.setAttrValue(HmeConstants.StatusCode.NEW);
        mtLotExtendList.add(tmpVo);
        if (StringUtils.isNotBlank(vo.getAverageWavelength())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("AVG_WAVE_LENGTH");
            tmpVo.setAttrValue(vo.getAverageWavelength());
            mtLotExtendList.add(tmpVo);
        }
        if (StringUtils.isNotEmpty(vo.getType())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("TYPE");
            tmpVo.setAttrValue(vo.getType());
            mtLotExtendList.add(tmpVo);
        }
        if (StringUtils.isNotBlank(vo.getLotNo())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("LOTNO");
            tmpVo.setAttrValue(vo.getLotNo());
            mtLotExtendList.add(tmpVo);
        }
        if(StringUtils.isNotEmpty(vo.getJobBatch())){
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("WORKING_LOT");
            tmpVo.setAttrValue(vo.getJobBatch());
            mtLotExtendList.add(tmpVo);
        }
        if (StringUtils.isNotBlank(vo.getRemark())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("REMARK");
            tmpVo.setAttrValue(vo.getRemark());
            mtLotExtendList.add(tmpVo);
        }
        if (StringUtils.isNotBlank(vo.getWorkOrderId())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("WORK_ORDER_ID");
            tmpVo.setAttrValue(vo.getWorkOrderId());
            mtLotExtendList.add(tmpVo);
        }
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
            {
                setKeyId(vo.getMaterialLotId());
                setEventId(eventId);
                setAttrs(mtLotExtendList);
            }
        });
    }

    /**
     * ??????
     *
     * @param tenantId
     * @param vo
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/9/18 11:50
     */

    private String materialLotLoadUpdate(Long tenantId, HmeCosChipNumImportVO vo) {
        // ????????????
        String loadPosition = vo.getLoadPosition();
        Long loadRow = Long.valueOf(loadPosition.substring(0, 1));
        Long loadColumn = Long.valueOf(loadPosition.substring(loadPosition.length() - 1));
        // ?????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
        String materialLotId = vo.getMaterialLotId();
        String materialLotIdNum = materialLotId.substring(0, materialLotId.indexOf("."));
        String loadSequence = materialLotIdNum + loadPosition + sdf.format(new Date());
        HmeMaterialLotLoad load = new HmeMaterialLotLoad();
        load.setMaterialLotId(vo.getMaterialLotId());
        load.setLoadSequence(loadSequence);
        load.setLoadRow(loadRow);
        load.setLoadColumn(loadColumn);
        load.setCosNum(vo.getCapacity());
        load.setTenantId(tenantId);
        hmeMaterialLotLoadRepository.insertSelective(load);
        return loadSequence;
    }

    /**
     * ??????????????????
     *
     * @param obj
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/18 11:41
     */
    private void isAllFieldBadPos(Long tenantId, HmeCosChipNumImportVO vo, Object obj) throws Exception {
        Class<?> clazz = obj.getClass();
        // ??????????????????
        Field[] fs = clazz.getDeclaredFields();
        // ????????????
        int index = 0;
        for (Field f : fs) {
            index++;
            f.setAccessible(true);
            // ?????????????????????
            Object val = f.get(obj);
            // ????????????????????????
            if ("".equals(Optional.ofNullable(val).orElse(""))) {
                break;
            }
            // ???0????????????
            if ("0".equals(val)) {
                materialLotNcLoadUpdate(tenantId, index, vo);
            }
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param tenantId
     * @param index
     * @param vo
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/18 11:50
     */
    private void materialLotNcLoadUpdate(Long tenantId, int index, HmeCosChipNumImportVO vo) {
        // ????????????
        HmeMaterialLotNcLoad load = new HmeMaterialLotNcLoad();
        load.setLoadSequence(vo.getLoadSequence());
        load.setLoadNum(String.valueOf(index));
        load.setTenantId(tenantId);
        hmeMaterialLotNcLoadRepository.insertSelective(load);
    }

    /**
     * ??????EO JobSn
     *
     * @param tenantId
     * @param vo
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/18 13:42:37
     */
    private void eoJobSnUpdate(Long tenantId, HmeCosChipNumImportVO vo, Long userId) {
        //????????????ID+jobType=IO?????????HME_EO_JOB_SN????????????????????????????????????????????????
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.select(new HmeEoJobSn() {{
            setTenantId(tenantId);
            setMaterialLotId(vo.getMaterialLotId());
            setJobType("IO");
        }});
        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            Date nowDate = new Date();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(nowDate);
            hmeEoJobSn.setSiteOutDate(nowDate);
            hmeEoJobSn.setShiftId(vo.getWkcShiftId());
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setWorkcellId(vo.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(vo.getWorkOrderId());
            hmeEoJobSn.setOperationId(vo.getOperationId());
            hmeEoJobSn.setSnMaterialId(vo.getMaterialId());
            hmeEoJobSn.setMaterialLotId(vo.getMaterialLotId());
            hmeEoJobSn.setJobType("IO");
            hmeEoJobSn.setSourceJobId(vo.getOperationRecordId());
            hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
        }
    }
}
