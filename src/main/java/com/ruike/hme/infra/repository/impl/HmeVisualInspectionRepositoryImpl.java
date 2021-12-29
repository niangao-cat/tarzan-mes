package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO2;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO3;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO9;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
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
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderVO25;
import tarzan.order.domain.vo.MtWorkOrderVO4;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 目检完工资源库实现
 *
 * @author: chaonan.hu@hand-china.com 2021-01-20 14:55:12
 **/
@Component
@Slf4j
public class HmeVisualInspectionRepositoryImpl implements HmeVisualInspectionRepository {

    @Autowired
    private HmeVisualInspectionMapper hmeVisualInspectionMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeTimeProcessPdaMapper hmeTimeProcessPdaMapper;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;
    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;
    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private WmsCommonServiceComponent commonServiceComponent;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeFinishProductsInStorageMapper mapper;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private WmsTransactionTypeRepository transactionTypeRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;
    @Autowired
    private HmeMaterialLotLoadRepository materialLotLoadRepository;
    @Autowired
    private HmeEoJobEquipmentMapper hmeEoJobEquipmentMapper;
    @Autowired
    private HmeLoadJobRepository hmeLoadJobRepository;
    @Autowired
    private HmeLoadJobObjectRepository hmeLoadJobObjectRepository;
    @Autowired
    private HmeLoadJobMapper hmeLoadJobMapper;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;
    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    @Override
    public List<HmeVisualInspectionVO> materialLotQuery(Long tenantId, HmeVisualInspectionDTO dto, String jobType) {
        return hmeVisualInspectionMapper.materialLotQuery(tenantId, dto, jobType);
    }

    @Override
    public List<HmeVisualInspectionVO> materialLotQuery2(Long tenantId, HmeVisualInspectionDTO dto, String jobType) {
        return hmeVisualInspectionMapper.materialLotQuery2(tenantId, dto, jobType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeVisualInspectionDTO2 scanMaterialLot(Long tenantId, HmeVisualInspectionDTO2 dto) {
        long startDate = System.currentTimeMillis();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot(){{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        String cosType = null;
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("COS_TYPE");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())){
            cosType = mtExtendAttrVOS.get(0).getAttrValue();
        }
        String workOrderId = null;
        mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())){
            workOrderId = mtExtendAttrVOS.get(0).getAttrValue();
        }
        String wafer = null;
        mtMaterialLotAttrVO2.setAttrName("WAFER_NUM");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())){
            wafer = mtExtendAttrVOS.get(0).getAttrValue();
        }
        long endDate = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 查询条码扩展属性耗时：{}毫秒", dto.getMaterialLotCode(), (endDate - startDate));
        //新增或更新工单工位工艺在制表hme_cos_operation_record
        HmeCosOperationRecord hmeCosOperationRecordDb = new HmeCosOperationRecord();
        HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
        hmeCosOperationRecord.setTenantId(tenantId);
        hmeCosOperationRecord.setSiteId(dto.getSiteId());
        hmeCosOperationRecord.setWorkOrderId(workOrderId);
        hmeCosOperationRecord.setCosType(cosType);
        hmeCosOperationRecord.setWafer(wafer);
        hmeCosOperationRecord.setOperationId(dto.getOperationId());
        hmeCosOperationRecord.setWorkcellId(dto.getWorkcellId());
        List<HmeCosOperationRecord> hmeCosOperationRecordList = hmeCosOperationRecordRepository.select(hmeCosOperationRecord);
        if(CollectionUtils.isEmpty(hmeCosOperationRecordList)){
            //新增
            hmeCosOperationRecordDb.setTenantId(tenantId);
            hmeCosOperationRecordDb.setSiteId(dto.getSiteId());
            hmeCosOperationRecordDb.setWorkOrderId(workOrderId);
            hmeCosOperationRecordDb.setWafer(wafer);
            hmeCosOperationRecordDb.setCosNum(mtMaterialLot.getPrimaryUomQty().longValue());
            hmeCosOperationRecordDb.setOperationId(dto.getOperationId());
            hmeCosOperationRecordDb.setWorkcellId(dto.getWorkcellId());
            hmeCosOperationRecordDb.setCosType(cosType);
            hmeCosOperationRecordDb.setSurplusCosNum(mtMaterialLot.getPrimaryUomQty().longValue());
            hmeCosOperationRecordDb.setMaterialId(mtMaterialLot.getMaterialId());
            //container_type_id
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO22 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO22.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotAttrVO22.setAttrName("CONTAINER_TYPE");
            List<MtExtendAttrVO> mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                String containerTypeCode = mtExtendAttrVOS2.get(0).getAttrValue();
                MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
                    setTenantId(tenantId);
                    setContainerTypeCode(containerTypeCode);
                }});
                if (mtContainerType != null) {
                    hmeCosOperationRecordDb.setContainerTypeId(mtContainerType.getContainerTypeId());
                }
            }
            //average_wavelength
            mtMaterialLotAttrVO22.setAttrName("AVG_WAVE_LENGTH");
            mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                hmeCosOperationRecordDb.setAverageWavelength(new BigDecimal(mtExtendAttrVOS2.get(0).getAttrValue()));
            }
            //type
            mtMaterialLotAttrVO22.setAttrName("TYPE");
            mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                hmeCosOperationRecordDb.setType(mtExtendAttrVOS2.get(0).getAttrValue());
            }
            //lot_no
            mtMaterialLotAttrVO22.setAttrName("LOTNO");
            mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                hmeCosOperationRecordDb.setLotNo(mtExtendAttrVOS2.get(0).getAttrValue());
            }
            //remark
            mtMaterialLotAttrVO22.setAttrName("REMARK");
            mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                hmeCosOperationRecordDb.setRemark(mtExtendAttrVOS2.get(0).getAttrValue());
            }
            //job_batch
            mtMaterialLotAttrVO22.setAttrName("WORKING_LOT");
            mtExtendAttrVOS2 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO22);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                hmeCosOperationRecordDb.setJobBatch(mtExtendAttrVOS2.get(0).getAttrValue());
            }
            hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecordDb);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecordDb, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            long endDate2 = System.currentTimeMillis();
            log.info("<====目检完工 条码{}进站 新增hmeCosOperationRecord耗时：{}毫秒", dto.getMaterialLotCode(), (endDate2 - startDate));
        }else{
            //更新
            hmeCosOperationRecordDb = hmeCosOperationRecordList.get(0);
            hmeCosOperationRecordDb.setCosNum(hmeCosOperationRecordDb.getCosNum() +
                    mtMaterialLot.getPrimaryUomQty().longValue());
            hmeCosOperationRecordDb.setSurplusCosNum(hmeCosOperationRecordDb.getSurplusCosNum() +
                    mtMaterialLot.getPrimaryUomQty().longValue());
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecordDb);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecordDb, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            long endDate2 = System.currentTimeMillis();
            log.info("<====目检完工 条码{}进站 更新hmeCosOperationRecord耗时：{}毫秒", dto.getMaterialLotCode(), (endDate2 - startDate));
        }
        long startDate2 = System.currentTimeMillis();
        //新增hme_eo_job_sn表数据
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setSiteInDate(new Date());
        hmeEoJobSn.setShiftId(dto.getWkcShiftId());
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSn.setWorkOrderId(workOrderId);
        hmeEoJobSn.setOperationId(dto.getOperationId());
        hmeEoJobSn.setSnMaterialId(mtMaterialLot.getMaterialId());
        hmeEoJobSn.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        hmeEoJobSn.setReworkFlag("N");
        hmeEoJobSn.setJobType("COS_MJ_COMPLETED");
        hmeEoJobSn.setSourceJobId(hmeCosOperationRecordDb.getOperationRecordId());
        hmeEoJobSn.setSnQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
        // 20210311 edit by chaonan.hu for zhenyong.ban 工位、eo、条码、返修标识、job_type取出最大的eo_step_num + 1
        long startDate3 = System.currentTimeMillis();
        Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeEoJobSn.getWorkcellId(), hmeEoJobSn.getEoId(), hmeEoJobSn.getMaterialLotId(), HmeConstants.ConstantValue.NO, hmeEoJobSn.getJobType(), hmeEoJobSn.getOperationId());
        long endDate3 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 查询最大的eo_step_num耗时：{}毫秒", dto.getMaterialLotCode(), (endDate3 - startDate3));
        hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
//        // 查询最近的条码历史记录到hme_eo_job_sn表中attribute3 2021-05-12 10:40 edit by chaonan.hu for zhenyong.ban 去掉查询历史逻辑
//        String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, mtMaterialLot.getMaterialLotId());
        hmeEoJobSn.setAttribute3(cosType);
        hmeEoJobSn.setAttribute5(wafer);
        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
        long endDate2 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 新增hmeEoJobSn耗时：{}毫秒", dto.getMaterialLotCode(), (endDate2 - startDate2));
        //新增或更新hme_wo_job_sn表
        HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
        HmeWoJobSn hmeWoJobSnQuery = new HmeWoJobSn();
        hmeWoJobSnQuery.setTenantId(tenantId);
        hmeWoJobSnQuery.setSiteId(dto.getSiteId());
        hmeWoJobSnQuery.setWorkOrderId(workOrderId);
        hmeWoJobSnQuery.setOperationId(dto.getOperationId());
        List<HmeWoJobSn> hmeWoJobSnList = hmeWoJobSnRepository.select(hmeWoJobSnQuery);
        if(CollectionUtils.isEmpty(hmeWoJobSnList)){
            //新增
            hmeWoJobSn.setTenantId(tenantId);
            hmeWoJobSn.setSiteId(dto.getSiteId());
            hmeWoJobSn.setWorkOrderId(workOrderId);
            hmeWoJobSn.setOperationId(dto.getOperationId());
            hmeWoJobSn.setSiteInNum(mtMaterialLot.getPrimaryUomQty().longValue());
            hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
            long endDate5 = System.currentTimeMillis();
            log.info("<====目检完工 条码{}进站 新增hmeWoJobSn耗时：{}毫秒", dto.getMaterialLotCode(), (endDate5 - endDate2));
        }else{
            //更新
            hmeWoJobSn = hmeWoJobSnList.get(0);
            hmeWoJobSn.setSiteInNum(hmeWoJobSn.getSiteInNum() + mtMaterialLot.getPrimaryUomQty().longValue());
            hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
            long endDate5 = System.currentTimeMillis();
            log.info("<====目检完工 条码{}进站 更新hmeWoJobSn耗时：{}毫秒", dto.getMaterialLotCode(), (endDate5 - endDate2));
        }
        //新增SN进出站设备状态记录表hme_eo_job_equipment
        long startDate6 = System.currentTimeMillis();
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRelList = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
            setTenantId(tenantId);
            setStationId(dto.getWorkcellId());
        }});
        if(CollectionUtils.isNotEmpty(hmeEquipmentWkcRelList)){
            for (HmeEquipmentWkcRel hmeEquipmentWkcRel:hmeEquipmentWkcRelList) {
                HmeEoJobEquipment hmeEoJobEquipment = new HmeEoJobEquipment();
                hmeEoJobEquipment.setTenantId(tenantId);
                hmeEoJobEquipment.setJobId(hmeEoJobSn.getJobId());
                hmeEoJobEquipment.setWorkcellId(dto.getWorkcellId());
                hmeEoJobEquipment.setEquipmentId(hmeEquipmentWkcRel.getEquipmentId());
                HmeEquipment hmeEquipment = hmeEquipmentRepository.selectByPrimaryKey(hmeEquipmentWkcRel.getEquipmentId());
                hmeEoJobEquipment.setEquipmentStatus(hmeEquipment.getEquipmentStatus());
                hmeEoJobEquipmentRepository.insertSelective(hmeEoJobEquipment);
            }
            long endDate6 = System.currentTimeMillis();
            log.info("<====目检完工 条码{}进站 新增SN进出站设备状态记录共{}条耗时：{}毫秒", dto.getMaterialLotCode(), hmeEquipmentWkcRelList.size(), (endDate6 - startDate6));
        }
        //2021-02-22 add by chaonan.hu for zhenyong.ban 记录COS履历
        long startDate7 = System.currentTimeMillis();
        Date now = new Date();
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = materialLotLoadRepository.select(new HmeMaterialLotLoad() {{
            setTenantId(tenantId);
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
        }});
        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
            HmeLoadJobDTO3 hmeLoadJobDTO3 = new HmeLoadJobDTO3();
            hmeLoadJobDTO3.setSiteId(dto.getSiteId());
            hmeLoadJobDTO3.setOperationId(dto.getOperationId());
            hmeLoadJobDTO3.setWorkcellId(dto.getWorkcellId());
            List<HmeEquipment> hmeEquipmentList = hmeEoJobEquipmentMapper.queryEquipmentListByWorkCellId(tenantId, dto.getWorkcellId());
            if (CollectionUtils.isNotEmpty(hmeEquipmentList)) {
                List<String> assetEncodingList = hmeEquipmentList.stream().map(HmeEquipment::getEquipmentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                hmeLoadJobDTO3.setAssetEncodingList(assetEncodingList);
                log.info("<====目检完工 条码{}进站 记录COS履历找到设备信息{}条", dto.getMaterialLotCode(), hmeEquipmentList.size());
            }
            List<String> sqlList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)){
                sqlList = createLoadJob(tenantId, hmeMaterialLotLoadList, hmeLoadJobDTO3,
                        "COS_MJ_COMPLETED_IN", hmeCosOperationRecord, now, userId, mtMaterialLot);
                this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
            long endDate7 = System.currentTimeMillis();
            log.info("<====目检完工 条码{}进站 记录COS履历耗时：{}毫秒 找到物料批装载信息{}条", dto.getMaterialLotCode(),  (endDate7 - startDate7), hmeMaterialLotLoadList.size());
        }
        return dto;
    }

    @Override
    public HmeVisualInspectionVO2 scanContainer(Long tenantId, String containerId) {
        //调用API{containerAvailableValidate}进行容器可用性验证
        mtContainerRepository.containerAvailableValidate(tenantId, containerId);
        //通过容器Id查询汇总条码总数
        Long count = hmeVisualInspectionMapper.getCountByContainerId(tenantId, containerId);
        HmeVisualInspectionVO2 hmeVisualInspectionVO2 = new HmeVisualInspectionVO2();
        hmeVisualInspectionVO2.setCount(count);
        return hmeVisualInspectionVO2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeVisualInspectionDTO3 materialLotComplete(Long tenantId, HmeVisualInspectionDTO3 dto) {
        List<HmeVisualInspectionVO> materialLotList = dto.getMaterialLotList();
        log.info("<====目检完工 完工出站{}个条码",materialLotList.size());
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Date now = new Date();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        String objectTransactionBatchId = Utils.getBatchId();
        //报工
        List<WmsObjectTransactionResponseVO> reportList = new ArrayList<>();
        //完工
        List<WmsObjectTransactionResponseVO> workList = new ArrayList<>();
        MtContainer mtContainer = null;
        List<MtContainerVO31> mtContainerVO31List = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getContainerCode())){
            mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
                setTenantId(tenantId);
                setContainerCode(dto.getContainerCode());
            }});
        }
        HmeLoadJobDTO3 hmeLoadJobDTO3 = new HmeLoadJobDTO3();
        hmeLoadJobDTO3.setSiteId(dto.getSiteId());
        hmeLoadJobDTO3.setOperationId(dto.getOperationId());
        hmeLoadJobDTO3.setWorkcellId(dto.getWorkcellId());
        List<HmeEquipment> hmeEquipmentList = hmeEoJobEquipmentMapper.queryEquipmentListByWorkCellId(tenantId, dto.getWorkcellId());
        // 创建请求事件
        String requestId = commonServiceComponent.generateEventRequest(tenantId, "HME_COS_COMPLETE");
        //调用{eventCreate}创建事件
        MtEventCreateVO processEvent = new MtEventCreateVO();
        processEvent.setEventTypeCode("HME_EO_COMPLETE");
        processEvent.setEventRequestId(requestId);
        String eventId = mtEventRepository.eventCreate(tenantId, processEvent);
        int i = 1;
        long startDate12 = System.currentTimeMillis();
        List<String> sqlList = new ArrayList<>();
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtInvOnhandQuantityVO13> mtInvOnhandQuantityVO13List = new ArrayList<>();
        //记录下循环后，需要手动再更新工单状态的工单Id集合
        List<String> workOrderIdList = new ArrayList<>();

        for (HmeVisualInspectionVO hmeVisualInspectionVO:materialLotList) {
            //2021-06-02 11:01 add by chaonan.hu for zhenyong.ban 加锁
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("目检完工");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            hmeObjectRecordLockDTO.setObjectRecordId(hmeVisualInspectionVO.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(hmeVisualInspectionVO.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            //加锁
            hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);
        }
        try {
            for (HmeVisualInspectionVO hmeVisualInspectionVO:materialLotList) {
                long startDate = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}",i, hmeVisualInspectionVO.getMaterialLotId());
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeVisualInspectionVO.getMaterialLotId());
                //更新工单工位工艺在制表hme_cos_operation_record
                HmeCosOperationRecord hmeCosOperationRecord = hmeVisualInspectionMapper.cosOperationRecordQuery(tenantId, dto.getWorkcellId(),
                        dto.getOperationId(), hmeVisualInspectionVO);
                if(Objects.isNull(hmeCosOperationRecord)){
                    throw new MtException("HME_COS_PATCH_PDA_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_PDA_0010", "HME"));
                }
                hmeCosOperationRecord.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum() - mtMaterialLot.getPrimaryUomQty().longValue());
                hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

                // 保存历史记录
                HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
                BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
                hmeCosOperationRecordHis.setTenantId(tenantId);
                hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
                long endDate3 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}更新工单工位工艺在制表耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate3 - startDate));
                //更新hme_eo_job_sn表，条码出站
                HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(hmeVisualInspectionVO.getJobId());
                hmeEoJobSn.setAttribute6(mtMaterialLot.getPrimaryUomQty().toString());
                hmeEoJobSn.setSiteOutBy(userId);
                hmeEoJobSn.setSiteOutDate(now);
                hmeEoJobSn.setLastUpdatedBy(userId);
                hmeEoJobSn.setLastUpdateDate(now);
                hmeEoJobSn.setObjectVersionNumber(hmeEoJobSn.getObjectVersionNumber() + 1);
                sqlList.addAll(mtCustomDbRepository.getUpdateSql(hmeEoJobSn));
                long endDate4 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}更新hme_eo_job_sn表耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate4 - endDate3));
                //更新hme_wo_job_sn表
                List<HmeWoJobSn> hmeWoJobSnList = hmeWoJobSnRepository.select(new HmeWoJobSn() {{
                    setTenantId(tenantId);
                    setSiteId(dto.getSiteId());
                    setWorkOrderId(hmeVisualInspectionVO.getWorkOrderId());
                    setOperationId(dto.getOperationId());
                }});
                if(CollectionUtils.isNotEmpty(hmeWoJobSnList)){
                    HmeWoJobSn hmeWoJobSn = hmeWoJobSnList.get(0);
                    hmeWoJobSn.setProcessedNum(hmeWoJobSn.getProcessedNum() + mtMaterialLot.getPrimaryUomQty().longValue());
                    hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
                }
                long endDate5 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}更新hme_wo_job_sn表耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate5 - endDate4));
                //更新条码表货位
                List<String> locatorIdList = mapper.queryLocatorByWorkCell(tenantId, hmeEoJobSn.getWorkcellId());
                String locatorId = mtMaterialLot.getLocatorId();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(locatorIdList)) {
                    //货位不一致 更新条码的货位
                    if (!StringUtils.equals(mtMaterialLot.getLocatorId(), locatorIdList.get(0))) {
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        mtMaterialLotVO20.setLocatorId(locatorIdList.get(0));
                        mtMaterialLotVO20List.add(mtMaterialLotVO20);
                        locatorId = locatorIdList.get(0);
                    }
                }
                long endDate6 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}更新条码表货位耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate6 - endDate5));
                //更新条码扩展表mt_material_lot_attr，MF_FLAG改为N
                MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
                mtExtendVO10.setEventId(eventId);
                mtExtendVO10.setKeyId(hmeVisualInspectionVO.getMaterialLotId());
                List<MtExtendVO5> mtExtendVO5List =new ArrayList<>();
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("MF_FLAG");
                mtExtendVO5.setAttrValue(HmeConstants.ConstantValue.NO);
                mtExtendVO5List.add(mtExtendVO5);
                MtExtendVO5 statusExtend = new MtExtendVO5();
                statusExtend.setAttrName("STATUS");
                statusExtend.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
                mtExtendVO5List.add(statusExtend);
                mtExtendVO10.setAttrs(mtExtendVO5List);
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
                long endDate7 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}更新条码扩展表耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate7 - endDate6));
                //调用API{onhandQtyUpdateProcess}进行库存现有量更新
                MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
                mtInvOnhandQuantityVO13.setSiteId(mtMaterialLot.getSiteId());
                mtInvOnhandQuantityVO13.setMaterialId(mtMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO13.setLocatorId(locatorId);
                mtInvOnhandQuantityVO13.setLotCode(mtMaterialLot.getLot());
                mtInvOnhandQuantityVO13.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
                mtInvOnhandQuantityVO13List.add(mtInvOnhandQuantityVO13);
                long endDate8 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}进行库存现有量更新耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate8 - endDate7));
                //调用API{woComplete}进行生产指令完成
                MtWorkOrderVO4 mtWorkOrderVO4 = new MtWorkOrderVO4();
                mtWorkOrderVO4.setWorkOrderId(hmeEoJobSn.getWorkOrderId());
                mtWorkOrderVO4.setTrxCompletedQty(mtMaterialLot.getPrimaryUomQty());
                mtWorkOrderVO4.setWorkcellId(hmeEoJobSn.getWorkcellId());
                mtWorkOrderVO4.setEventRequestId(requestId);
                mtWorkOrderRepository.woComplete(tenantId, mtWorkOrderVO4);
                workOrderIdList.add(hmeEoJobSn.getWorkOrderId());
                long endDate9 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}进行生产指令完成耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate9 - endDate8));
                //完工事务
                List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
                WmsObjectTransactionRequestVO woCompleteTransactionRequestVO = new WmsObjectTransactionRequestVO();
                woCompleteTransactionRequestVO.setTransactionTypeCode("HME_WO_COMPLETE");
                woCompleteTransactionRequestVO.setEventId(eventId);
                woCompleteTransactionRequestVO.setMaterialLotId(hmeVisualInspectionVO.getMaterialLotId());
                woCompleteTransactionRequestVO.setMaterialId(mtMaterialLot.getMaterialId());
                woCompleteTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
                woCompleteTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                if (Objects.nonNull(mtUom)) {
                    woCompleteTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
                }
                woCompleteTransactionRequestVO.setTransactionTime(new Date());
                woCompleteTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());

                MtModLocator mtModLocator1 = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtMaterialLot.getLocatorId());
                if (Objects.nonNull(mtModLocator1)) {
                    woCompleteTransactionRequestVO.setWarehouseId(mtModLocator1.getLocatorId());
                }
                woCompleteTransactionRequestVO.setLocatorId(locatorId);
                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeVisualInspectionVO.getWorkOrderId());
                if (mtWorkOrder != null) {
                    woCompleteTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    MtModProductionLine mtModProductionLine = mtModProductionLineRepository
                            .prodLineBasicPropertyGet(tenantId, mtWorkOrder.getProductionLineId());
                    if (Objects.nonNull(mtModProductionLine)) {
                        woCompleteTransactionRequestVO.setProdLineCode(mtModProductionLine.getProdLineCode());
                    }
                }
                //工序号
                List<String> operationSequenceList = mapper.queryOperationSequence(tenantId, hmeVisualInspectionVO.getWorkOrderId(), dto.getOperationId());
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(operationSequenceList)) {
                    woCompleteTransactionRequestVO.setOperationSequence(operationSequenceList.get(0));
                }

                //批次
                woCompleteTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());

                //报工事务
                WmsObjectTransactionRequestVO woReportTransactionRequestVO = new WmsObjectTransactionRequestVO();
                BeanUtils.copyProperties(woCompleteTransactionRequestVO, woReportTransactionRequestVO);
                woReportTransactionRequestVO.setTransactionTypeCode("HME_WORK_REPORT");

                //查询移动类型
                List<WmsTransactionType> transactionTypes = transactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class)
                        .andWhere(Sqls.custom().andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, woCompleteTransactionRequestVO.getTransactionTypeCode())
                        ).build());
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(transactionTypes)) {
                    woCompleteTransactionRequestVO.setMoveType(transactionTypes.get(0).getMoveType());
                }

                //完工
                woCompleteTransactionRequestVO.setContainerId(mtMaterialLot.getCurrentContainerId());
                woCompleteTransactionRequestVO.setAttribute16(objectTransactionBatchId);
                objectTransactionRequestList.add(woCompleteTransactionRequestVO);
                List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS1 = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                workList.addAll(wmsObjectTransactionResponseVOS1);
                //报工
                List<WmsObjectTransactionRequestVO> objectTransactionRequestList1 = new ArrayList<>();
                woReportTransactionRequestVO.setAttribute16(objectTransactionBatchId);
                objectTransactionRequestList1.add(woReportTransactionRequestVO);
                List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList1);
                reportList.addAll(wmsObjectTransactionResponseVOS);
                long endDate10 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{}组装完工事务报工事务数据耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate10 - endDate9));
                //容器装载条码
                if(Objects.nonNull(mtContainer)){
                    MtContainerVO9 mtContainerVO9 = new MtContainerVO9();
                    mtContainerVO9.setContainerId(mtContainer.getContainerId());
                    mtContainerVO9.setLoadObjectType("MATERIAL_LOT");
                    mtContainerVO9.setLoadObjectId(hmeVisualInspectionVO.getMaterialLotId());
                    mtContainerRepository.containerLoadVerify(tenantId, mtContainerVO9);
                    MtContainerVO31 mtContainerVO31 = new MtContainerVO31();
                    mtContainerVO31.setContainerId(mtContainer.getContainerId());
                    mtContainerVO31.setLoadObjectType("MATERIAL_LOT");
                    mtContainerVO31.setLoadObjectId(hmeVisualInspectionVO.getMaterialLotId());
                    mtContainerVO31List.add(mtContainerVO31);
                    long endDate11 = System.currentTimeMillis();
                    log.info("<====目检完工 完工出站当前循环第{}个条码{}容器装载验证耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate11 - endDate10));
                }
                //2021-02-22 add by chaonan.hu for zhenyong.ban 记录COS履历
                long startDate11 = System.currentTimeMillis();
                List<HmeMaterialLotLoad> hmeMaterialLotLoadList = materialLotLoadRepository.select(new HmeMaterialLotLoad() {{
                    setTenantId(tenantId);
                    setMaterialLotId(mtMaterialLot.getMaterialLotId());
                }});
                if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                    if (CollectionUtils.isNotEmpty(hmeEquipmentList)) {
                        List<String> assetEncodingList = hmeEquipmentList.stream().map(HmeEquipment::getEquipmentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                        hmeLoadJobDTO3.setAssetEncodingList(assetEncodingList);
                    }
                    if(CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)){
                        List<String> sql = createLoadJob(tenantId, hmeMaterialLotLoadList, hmeLoadJobDTO3,
                                "COS_MJ_COMPLETED_OUT", hmeCosOperationRecord,now, userId, mtMaterialLot);
                        sqlList.addAll(sql);
                    }
                    long endDate11 = System.currentTimeMillis();
                    log.info("<====目检完工 完工出站当前循环第{}个条码{}组装COS履历耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate11 - startDate11));
                }
                long endDate = System.currentTimeMillis();
                log.info("<====目检完工 完工出站当前循环第{}个条码{} 总耗时：{}毫秒",i, hmeVisualInspectionVO.getMaterialLotId(), (endDate - startDate));
                i++;
            }
            long startDate14 = System.currentTimeMillis();
            if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, "N");
            }
            long endDate14 = System.currentTimeMillis();
            log.info("<====目检完工 完工出站批量更新物料批耗时：{}毫秒",(endDate14 - startDate14));
            if(CollectionUtils.isNotEmpty(mtInvOnhandQuantityVO13List)){
                MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
                mtInvOnhandQuantityVO16.setEventId(eventId);
                mtInvOnhandQuantityVO16.setOnhandList(mtInvOnhandQuantityVO13List);
                mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
            }
            long endDate15 = System.currentTimeMillis();
            log.info("<====目检完工 完工出站批量更新库存现有量耗时：{}毫秒",(endDate15 - endDate14));
            if(CollectionUtils.isNotEmpty(sqlList)){
                this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
            long endDate12 = System.currentTimeMillis();
            log.info("<====目检完工 完工出站批量插入更新数据耗时：{}毫秒",(endDate12 - endDate14));
            log.info("<====目检完工 完工出站循环{}个条码 耗时：{}毫秒",materialLotList.size(), (endDate12 - startDate12));
            //容器批量装载条码
            if(Objects.nonNull(mtContainer)){
                MtContainerVO30 mtContainerVO30 = new MtContainerVO30();
                mtContainerVO30.setWorkcellId(dto.getWorkcellId());
                mtContainerVO30.setParentEventId(eventId);
                mtContainerVO30.setEventRequestId(requestId);
                mtContainerVO30.setContainerLoadList(mtContainerVO31List);
                mtContainerRepository.containerBatchLoad(tenantId, mtContainerVO30);
                long endDate13 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站 容器装载耗时：{}毫秒", (endDate13 - endDate12));
            }
            //2021-06-11 09:36 add by chaonan.hu for peng.zhao 如果进行生产指令完成后，工单状态为RELEASED，则需再次更新为EORELEASED
            if(CollectionUtils.isNotEmpty(workOrderIdList)){
                long startDate13 = System.currentTimeMillis();
                workOrderIdList = workOrderIdList.stream().distinct().collect(Collectors.toList());
                for (String workOrderId:workOrderIdList) {
                    MtWorkOrder workOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
                    if("RELEASED".equals(workOrder.getStatus())){
                        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
                        statusUpdateVO.setWorkOrderId(workOrderId);
                        statusUpdateVO.setStatus("EORELEASED");
                        statusUpdateVO.setEventId(eventId);
                        mtWorkOrderRepository.woStatusUpdate(tenantId, statusUpdateVO);
                    }
                }
                long endDate13 = System.currentTimeMillis();
                log.info("<====目检完工 完工出站手动更新{}个工单耗时：{}毫秒",workOrderIdList.size(), (endDate13 - startDate13));
            }
            //报工实时接口，完工实时接口
            long startDate13 = System.currentTimeMillis();
            itfObjectTransactionIfaceService.sendSapProdMaterialMove(tenantId,reportList,workList);
            long endDate13 = System.currentTimeMillis();
            log.info("<====目检完工 完工出站 报工完工实时接口耗时：{}毫秒", (endDate13 - startDate13));
        }catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            for (HmeVisualInspectionVO hmeVisualInspectionVO:materialLotList) {
                HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                hmeObjectRecordLockDTO.setFunctionName("目检完工");
                hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
                hmeObjectRecordLockDTO.setObjectRecordId(hmeVisualInspectionVO.getMaterialLotId());
                hmeObjectRecordLockDTO.setObjectRecordCode(hmeVisualInspectionVO.getMaterialLotCode());
                HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                //解锁
                hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
            }
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeVisualInspectionDTO3 siteInCancel(Long tenantId, HmeVisualInspectionDTO3 dto) {
        for (HmeVisualInspectionVO hmeVisualInspectionVO: dto.getMaterialLotList()) {
            //更新工单工位工艺在制表
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeVisualInspectionVO.getMaterialLotId());
            HmeCosOperationRecord cosOperationRecord = hmeVisualInspectionMapper.cosOperationRecordQuery(tenantId, dto.getWorkcellId(),
                    dto.getOperationId(), hmeVisualInspectionVO);
            if(Objects.nonNull(cosOperationRecord)){
                cosOperationRecord.setCosNum(cosOperationRecord.getCosNum() - mtMaterialLot.getPrimaryUomQty().longValue());
                cosOperationRecord.setSurplusCosNum(cosOperationRecord.getSurplusCosNum() - mtMaterialLot.getPrimaryUomQty().longValue());
                hmeCosOperationRecordMapper.updateByPrimaryKeySelective(cosOperationRecord);

                // 保存历史记录
                HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
                BeanUtils.copyProperties(cosOperationRecord, hmeCosOperationRecordHis);
                hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            }else{
                throw new MtException("HME_COS_042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_042", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            //删除hme_eo_job_sn表
            hmeEoJobSnRepository.deleteByPrimaryKey(hmeVisualInspectionVO.getJobId());
            //更新hme_wo_job_sn表
            List<HmeWoJobSn> hmeWoJobSnList = hmeWoJobSnRepository.select(new HmeWoJobSn() {{
                setTenantId(tenantId);
                setSiteId(dto.getSiteId());
                setWorkOrderId(hmeVisualInspectionVO.getWorkOrderId());
                setOperationId(dto.getOperationId());
            }});
            if(CollectionUtils.isEmpty(hmeWoJobSnList)){
                throw new MtException("HME_COS_040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_040", "HME", mtMaterialLot.getMaterialLotCode()));
            }else{
                HmeWoJobSn hmeWoJobSn = hmeWoJobSnList.get(0);
                hmeWoJobSn.setSiteInNum(hmeWoJobSn.getSiteInNum() - mtMaterialLot.getPrimaryUomQty().longValue());
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
            }
            //删除hme_eo_job_equipment表
            List<HmeEoJobEquipment> hmeEoJobEquipmentList = hmeEoJobEquipmentRepository.select(new HmeEoJobEquipment() {{
                setTenantId(tenantId);
                setJobId(hmeVisualInspectionVO.getJobId());
            }});
            if(CollectionUtils.isNotEmpty(hmeEoJobEquipmentList)){
                hmeEoJobEquipmentRepository.batchDeleteByPrimaryKey(hmeEoJobEquipmentList);
            }
            //2021-02-22 add by chaonan.hu for zhenyong.ban 更新COS履历数据
            List<HmeLoadJob> hmeLoadJobList = hmeLoadJobRepository.select(new HmeLoadJob() {{
                setTenantId(tenantId);
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
                setLoadJobType("COS_MJ_COMPLETED_IN");
            }});
            for (HmeLoadJob hmeLoadJob : hmeLoadJobList) {
                hmeLoadJob.setStatus("1");
                hmeLoadJobMapper.updateByPrimaryKeySelective(hmeLoadJob);
            }
        }
        return dto;
    }

    @Override
    public List<String> createLoadJob(Long tenantId, List<HmeMaterialLotLoad> hmeMaterialLotLoadList, HmeLoadJobDTO3 dto,
                                      String loadJobType, HmeCosOperationRecord hmeCosOperationRecord, Date now, Long userId,
                                      MtMaterialLot mtMaterialLot) {
        List<String> sqlList = new ArrayList<>();
        List<String> loadJobIdList = mtCustomDbRepository.getNextKeys("hme_load_job_s", hmeMaterialLotLoadList.size());
        List<String> loadJobCidList = mtCustomDbRepository.getNextKeys("hme_load_job_cid_s", hmeMaterialLotLoadList.size());
        List<String> loadEquipmentIdS = new ArrayList<>();
        List<String> loadEquipmentCidS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getAssetEncodingList())) {
            loadEquipmentIdS = mtCustomDbRepository.getNextKeys("hme_load_job_object_s", dto.getAssetEncodingList().size() * hmeMaterialLotLoadList.size());
            loadEquipmentCidS = mtCustomDbRepository.getNextKeys("hme_load_job_object_cid_s", dto.getAssetEncodingList().size() * hmeMaterialLotLoadList.size());
        }
        List<String> loadSequenceList = hmeMaterialLotLoadList.stream().map(HmeMaterialLotLoad::getLoadSequence).distinct().collect(Collectors.toList());
        List<HmeVisualInspectionVO9> ncCodeList = new ArrayList<>();
        List<String> loadNcIdS = new ArrayList<>();
        List<String> loadNcCidS = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(loadSequenceList) && "COS_MJ_COMPLETED_IN".equals(loadJobType)){
            ncCodeList = hmeVisualInspectionMapper.ncCodeQuery(tenantId, loadSequenceList);
            if(CollectionUtils.isNotEmpty(ncCodeList)){
                loadNcIdS = mtCustomDbRepository.getNextKeys("hme_load_job_object_s", ncCodeList.size());
                loadNcCidS = mtCustomDbRepository.getNextKeys("hme_load_job_object_cid_s", ncCodeList.size());
            }
        }
        int i = 0;
        int j = 0;
        int k = 0;
        for (HmeMaterialLotLoad hmeMaterialLotLoad: hmeMaterialLotLoadList) {
            HmeLoadJob hmeLoadJob = new HmeLoadJob();
            hmeLoadJob.setTenantId(tenantId);
            hmeLoadJob.setLoadJobId(loadJobIdList.get(j));
            hmeLoadJob.setSiteId(dto.getSiteId());
            hmeLoadJob.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
            hmeLoadJob.setLoadJobType(loadJobType);
            hmeLoadJob.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
            hmeLoadJob.setMaterialId(mtMaterialLot.getMaterialId());
            hmeLoadJob.setLoadRow(hmeMaterialLotLoad.getLoadRow());
            hmeLoadJob.setLoadColumn(hmeMaterialLotLoad.getLoadColumn());
            hmeLoadJob.setCosNum(1L);
            hmeLoadJob.setHotSinkCode(hmeMaterialLotLoad.getHotSinkCode());
            hmeLoadJob.setOperationId(dto.getOperationId());
            hmeLoadJob.setWorkcellId(dto.getWorkcellId());
            hmeLoadJob.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
            hmeLoadJob.setWaferNum(hmeCosOperationRecord.getWafer());
            hmeLoadJob.setCosType(hmeCosOperationRecord.getCosType());
            hmeLoadJob.setCid(Long.valueOf(loadJobCidList.get(j)));
            hmeLoadJob.setCreationDate(now);
            hmeLoadJob.setCreatedBy(userId);
            hmeLoadJob.setLastUpdateDate(now);
            hmeLoadJob.setLastUpdatedBy(userId);
            hmeLoadJob.setStatus("0");
            if("COS_MJ_COMPLETED_OUT".equals(loadJobType) || "COS_TEST_OUT".equals(loadJobType)){
                hmeLoadJob.setBomMaterialId(hmeMaterialLotLoad.getAttribute11());
                if(StringUtils.isNotBlank(hmeMaterialLotLoad.getAttribute7())){
                    MtMaterialLot bomMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                        setTenantId(tenantId);
                        setMaterialLotCode(hmeMaterialLotLoad.getAttribute7());
                    }});
                    if(Objects.nonNull(bomMaterialLot)){
                        hmeLoadJob.setBomMaterialLotId(bomMaterialLot.getMaterialLotId());
                    }
                }
                hmeLoadJob.setBomMaterialLotSupplier(hmeMaterialLotLoad.getAttribute8());
            }
            sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeLoadJob));
            j++;
            //设备
            if (CollectionUtils.isNotEmpty(dto.getAssetEncodingList())) {
                for (String assetEncoding : dto.getAssetEncodingList()) {
                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                    hmeLoadJobObject.setTenantId(tenantId);
                    hmeLoadJobObject.setLoadObjectId(loadEquipmentIdS.get(i));
                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                    hmeLoadJobObject.setObjectType("EQUIPMENT");
                    hmeLoadJobObject.setObjectId(assetEncoding);
                    hmeLoadJobObject.setCid(Long.valueOf(loadEquipmentCidS.get(i)));
                    hmeLoadJobObject.setCreationDate(now);
                    hmeLoadJobObject.setCreatedBy(userId);
                    hmeLoadJobObject.setLastUpdateDate(now);
                    hmeLoadJobObject.setLastUpdatedBy(userId);
                    sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeLoadJobObject));
                    i++;
                }
            }
            //不良
            if(CollectionUtils.isNotEmpty(ncCodeList)){
                List<HmeVisualInspectionVO9> singleNcCode = ncCodeList.stream().filter(item -> hmeMaterialLotLoad.getLoadSequence().equals(item.getLoadSequence())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(singleNcCode)){
                    List<String> singleNcCodeList = singleNcCode.stream().map(HmeVisualInspectionVO9::getNcCodeId).collect(Collectors.toList());
                    for (String ncCode:singleNcCodeList) {
                        HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                        hmeLoadJobObject.setTenantId(tenantId);
                        hmeLoadJobObject.setLoadObjectId(loadNcIdS.get(k));
                        hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                        hmeLoadJobObject.setObjectType("NC");
                        hmeLoadJobObject.setObjectId(ncCode);
                        hmeLoadJobObject.setCid(Long.valueOf(loadNcCidS.get(k)));
                        hmeLoadJobObject.setCreationDate(now);
                        hmeLoadJobObject.setCreatedBy(userId);
                        hmeLoadJobObject.setLastUpdateDate(now);
                        hmeLoadJobObject.setLastUpdatedBy(userId);
                        sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeLoadJobObject));
                        k++;
                    }
                }
            }
        }
        return sqlList;
    }
}
