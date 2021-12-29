package com.ruike.itf.app.service.impl;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.app.service.HmeEoJobSnTimeReworkService;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO2;
import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.ItfReworkTimeIfaceDTO;
import com.ruike.itf.api.dto.ItfReworkTimeIfaceDTO2;
import com.ruike.itf.app.service.ItfReworkTimeIfaceService;
import com.ruike.itf.domain.repository.ItfFirstProcessIfaceRepository;
import com.ruike.itf.domain.repository.ItfTimeProcessIfaceRepository;
import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO;
import com.ruike.itf.infra.mapper.ItfFirstProcessIfaceMapper;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/29 16:04
 */
@Service
public class ItfReworkTimeIfaceServiceImpl implements ItfReworkTimeIfaceService {

    @Autowired
    private HmeEoJobSnTimeReworkService hmeEoJobSnTimeReworkService;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private ItfFirstProcessIfaceMapper itfFirstProcessIfaceMapper;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private ItfFirstProcessIfaceRepository itfFirstProcessIfaceRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private ItfTimeProcessIfaceRepository itfTimeProcessIfaceRepository;

    @Override
    public ItfProcessReturnIfaceVO inSiteInvoke(Long tenantId, ItfReworkTimeIfaceDTO dto) {
        reworkTimeSiteInVerify(tenantId, dto);
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setJobType(HmeConstants.JobType.REPAIR_TIME_PROCESS);
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleInSiteInvoke(tenantId, dto, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfProcessReturnIfaceVO handleInSiteInvoke (Long tenantId, ItfReworkTimeIfaceDTO dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        ItfProcessReturnIfaceVO itfProcessReturnIfaceVO = new ItfProcessReturnIfaceVO();
        itfProcessReturnIfaceVO.setResult(true);
        if(StringUtils.isNotBlank(dto.getScanAssetEncoding())){
            //查询设备类型
            List<String> equipmentCategoryList = itfFirstProcessIfaceRepository.getEquipmentCategory(tenantId, dto.getWorkcellCode(), dto.getDefaultSiteId());
            //工位绑定设备
            itfFirstProcessIfaceRepository.bindEquipment(tenantId, dto.getHmeEquipmentList(), dto.getWorkcellId(), dto.getDefaultSiteId(), equipmentCategoryList);
        }

        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        hmeEoJobSnVO3.setSnNum(dto.getMaterialLotCode());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3.setJobType(HmeConstants.JobType.REPAIR_TIME_PROCESS);
        hmeEoJobSnVO3.setInOutType("IN");
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        hmeEoJobSnVO3.setSiteId(defaultSiteId);
        HmeEoJobTimeSnVO2 hmeEoJobTimeSnVO2 = hmeEoJobSnTimeReworkService.timeSnScan(tenantId, hmeEoJobSnVO3);

        HmeEoJobSnVO3 hmeEoJobSnVO3In = new HmeEoJobSnVO3();
        hmeEoJobSnVO3In.setContainerId(hmeEoJobTimeSnVO2.getContainerId());
        hmeEoJobSnVO3In.setJobContainerId(hmeEoJobTimeSnVO2.getJobContainerId());
        hmeEoJobSnVO3In.setJobType(HmeConstants.JobType.REPAIR_TIME_PROCESS);
        hmeEoJobSnVO3In.setOperationId(hmeEoJobSnVO4.getOperationId());
        hmeEoJobSnVO3In.setOperationIdList(hmeEoJobSnVO4.getOperationIdList());
        hmeEoJobSnVO3In.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnVO3In.setSiteInBy(dto.getUserId());

        List<HmeEoJobSnVO3> snLineList =new ArrayList<>();
        for (HmeEoJobTimeSnVO3 hmeEoJobTimeSnVO3:
                hmeEoJobTimeSnVO2.getLineList()) {
            HmeEoJobSnVO3 hmeEoJobSnVO31 = new HmeEoJobSnVO3();
            BeanUtils.copyProperties(hmeEoJobTimeSnVO3,hmeEoJobSnVO31);
            hmeEoJobSnVO31.setSnNum(dto.getMaterialLotCode());
            hmeEoJobSnVO31.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
            hmeEoJobSnVO31.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
            snLineList.add(hmeEoJobSnVO31);
        }
        hmeEoJobSnVO3In.setSnLineList(snLineList);
        hmeEoJobSnVO3In.setSnNum(dto.getMaterialLotCode());
        hmeEoJobSnVO3In.setSumEoQty(new BigDecimal(hmeEoJobTimeSnVO2.getSumEoCount()));
        hmeEoJobSnVO3In.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeEoJobSnVO3In.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3In.setSnType(hmeEoJobTimeSnVO2.getSnType());

        hmeEoJobSnTimeReworkService.inSiteScan(tenantId, hmeEoJobSnVO3In);

        // 返回数据
        itfProcessReturnIfaceVO.setMaterialLotList(itfTimeProcessIfaceRepository.handleProcessReturnData(tenantId, hmeEoJobTimeSnVO2.getLineList()));
        itfProcessReturnIfaceVO.setSumEoCount(hmeEoJobTimeSnVO2.getSumEoCount());
        itfProcessReturnIfaceVO.setStandardReqdTimeInProcess(hmeEoJobTimeSnVO2.getLineList().get(0).getStandardReqdTimeInProcess());
        itfProcessReturnIfaceVO.setSiteInByName(hmeEoJobTimeSnVO2.getSiteInByName());
        itfProcessReturnIfaceVO.setNcRecordWorkcellCode(hmeEoJobTimeSnVO2.getNcRecordWorkcellCode());
        itfProcessReturnIfaceVO.setNcRecordWorkcellName(hmeEoJobTimeSnVO2.getNcRecordWorkcellName());
        return itfProcessReturnIfaceVO;
    }

    @Override
    public ItfProcessReturnIfaceVO outSiteInvoke(Long tenantId, ItfReworkTimeIfaceDTO2 request) {
        this.reworkTimeSiteOutVerify(tenantId, request);
        // 工位登录
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setJobType(HmeConstants.JobType.REPAIR_TIME_PROCESS);
        hmeEoJobSnDTO.setWorkcellCode(request.getWorkcellCode());
        hmeEoJobSnDTO.setSiteId(request.getDefaultSiteId());
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleOutSiteInvoke(tenantId, request, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfProcessReturnIfaceVO handleOutSiteInvoke (Long tenantId, ItfReworkTimeIfaceDTO2 request, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        ItfProcessReturnIfaceVO itfProcessReturnIfaceVO = new ItfProcessReturnIfaceVO();
        itfProcessReturnIfaceVO.setResult(true);
        // 查询入炉信息
        HmeEoJobSnVO3 eoJobSnVO3 = new HmeEoJobSnVO3();
        eoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        eoJobSnVO3.setSnNum(request.getSnNum());
        eoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        eoJobSnVO3.setJobType(HmeConstants.JobType.REPAIR_TIME_PROCESS);
        eoJobSnVO3.setInOutType("OUT");
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        eoJobSnVO3.setSiteId(defaultSiteId);
        HmeEoJobTimeSnVO2 hmeEoJobTimeSnVO2 = hmeEoJobSnTimeReworkService.timeSnScan(tenantId, eoJobSnVO3);
        // 校验出站操作
        if ("REWORK".equals(request.getOutSiteAction())) {
            if (HmeConstants.ConstantValue.NO.equals(hmeEoJobTimeSnVO2.getReworkFlag())) {
                throw new MtException("HME_EQUIPMENT_022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_022", "HME"));
            }
        } else if ("COMPLETE".equals(request.getOutSiteAction())) {
            if (HmeConstants.ConstantValue.NO.equals(hmeEoJobTimeSnVO2.getIsClickProcessComplete())) {
                throw new MtException("HME_EQUIPMENT_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_023", "HME"));
            }
        }
        Long processingTime = 0L;
        // 校验时长
        Date date = CommonUtils.currentTimeGet();
        Date siteInDate = null;
        if ("MATERIAL_LOT".equals(hmeEoJobTimeSnVO2.getSnType()) || "EQUIPMENT".equals(hmeEoJobTimeSnVO2.getSnType())) {
            siteInDate = hmeEoJobTimeSnVO2.getLineList().get(0).getSiteInDate();
        } else {
            siteInDate = hmeEoJobTimeSnVO2.getSiteInDate();
        }
        processingTime = date.getTime() - siteInDate.getTime();
        BigDecimal divideTime = BigDecimal.valueOf(processingTime).divide(BigDecimal.valueOf(60 * 1000), 6, BigDecimal.ROUND_HALF_DOWN);
        if (divideTime.compareTo(hmeEoJobTimeSnVO2.getLineList().get(0).getStandardReqdTimeInProcess()) <= 0) {
            throw new MtException("HME_EQUIPMENT_024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_024", "HME"));
        }
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setContainerId(hmeEoJobTimeSnVO2.getContainerId());
        hmeEoJobSnVO3.setJobContainerId(hmeEoJobTimeSnVO2.getJobContainerId());
        hmeEoJobSnVO3.setJobType(HmeConstants.JobType.REPAIR_TIME_PROCESS);
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        hmeEoJobSnVO3.setSiteId(request.getDefaultSiteId());
        hmeEoJobSnVO3.setSiteInBy(hmeEoJobTimeSnVO2.getSiteInBy());
        hmeEoJobSnVO3.setSnNum(request.getSnNum());
        hmeEoJobSnVO3.setSnType(hmeEoJobTimeSnVO2.getSnType());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        BeanCopier beanCopier = BeanCopier.create(HmeEoJobTimeSnVO3.class, HmeEoJobSnVO3.class, false);
        if (CollectionUtils.isNotEmpty(hmeEoJobTimeSnVO2.getLineList())) {
            hmeEoJobSnVO3.setSumEoQty(BigDecimal.valueOf(hmeEoJobTimeSnVO2.getLineList().size()));
            List<HmeEoJobSnVO3> snLineList = hmeEoJobTimeSnVO2.getLineList().stream().map(timeVO -> {
                HmeEoJobSnVO3 jobSnVO3 = new HmeEoJobSnVO3();
                beanCopier.copy(timeVO, jobSnVO3, null);
                jobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
                jobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
                jobSnVO3.setSnNum(request.getSnNum());
                return jobSnVO3;
            }).collect(Collectors.toList());
            hmeEoJobSnVO3.setSnLineList(snLineList);
        }
        if ("REWORK".equals(request.getOutSiteAction())) {
            hmeEoJobSnTimeReworkService.continueRework(tenantId, hmeEoJobSnVO3);
        } else {
            hmeEoJobSnTimeReworkService.outSiteScan(tenantId, hmeEoJobSnVO3);
        }
        return itfProcessReturnIfaceVO;
    }

    void reworkTimeSiteOutVerify(Long tenantId, ItfReworkTimeIfaceDTO2 dto) {
        //必输校验
        if(StringUtils.isBlank(dto.getWorkcellCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getSnNum())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "扫描条码"));
        }
        if(StringUtils.isBlank(dto.getUser())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "用户"));
        }

        //工位存在
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setWorkcellCode(dto.getWorkcellCode());
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtModWorkcell)){
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }
        //用户存在
        Long userId = itfFirstProcessIfaceMapper.getUserIdByLoginName(dto.getUser());
        if(Objects.isNull(userId)){
            throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_INSPECTOR_ITEM_GROUP_REL_0002", "HME", dto.getUser()));
        }
        dto.setUserId(userId);
        DetailsHelper.setCustomUserDetails(userId, "zh_CN");
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if(Objects.isNull(defaultSiteId)){
            throw new MtException("HME_TAG_CHECK_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_019", "HME"));
        }
        dto.setDefaultSiteId(defaultSiteId);
    }

    void reworkTimeSiteInVerify(Long tenantId, ItfReworkTimeIfaceDTO dto) {
        //必输校验
        if(StringUtils.isBlank(dto.getWorkcellCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "SN号"));
        }
        if(StringUtils.isBlank(dto.getUser())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "用户"));
        }
        if(StringUtils.isNotBlank(dto.getScanAssetEncoding())){
            //当设备不为空时，需要校验设备是否存在
            List<String> assetEncodingList = Arrays.asList(dto.getScanAssetEncoding().split(","));
            List<HmeEquipment> hmeEquipmentList = hmeEquipmentRepository.selectByCondition(Condition.builder(HmeEquipment.class).andWhere(Sqls.custom()
                    .andIn(HmeEquipment.FIELD_ASSET_ENCODING, assetEncodingList)
                    .andEqualTo(HmeEquipment.FIELD_TENANT_ID, tenantId)).build());
            if(hmeEquipmentList.size() != assetEncodingList.size()){
                throw new MtException("HME_TIME_PROCESS_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TIME_PROCESS_0001", "HME"));
            }
            dto.setHmeEquipmentList(hmeEquipmentList);
        }
        //工位存在
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setWorkcellCode(dto.getWorkcellCode());
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtModWorkcell)){
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }
        dto.setWorkcellId(mtModWorkcell.getWorkcellId());
        //用户存在
        Long userId = itfFirstProcessIfaceMapper.getUserIdByLoginName(dto.getUser());
        if(Objects.isNull(userId)){
            throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_INSPECTOR_ITEM_GROUP_REL_0002", "HME", dto.getUser()));
        }
        dto.setUserId(userId);
        DetailsHelper.setCustomUserDetails(userId, "zh_CN");
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if(Objects.isNull(defaultSiteId)){
            throw new MtException("HME_TAG_CHECK_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_019", "HME"));
        }
        dto.setDefaultSiteId(defaultSiteId);
    }
}
