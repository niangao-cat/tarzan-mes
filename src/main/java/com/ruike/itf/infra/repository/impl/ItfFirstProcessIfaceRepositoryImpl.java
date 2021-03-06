package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoJobFirstProcessService;
import com.ruike.hme.app.service.HmeEoJobSnBatchService;
import com.ruike.hme.app.service.HmeEoJobSnSingleService;
import com.ruike.hme.app.service.HmeWorkcellEquipmentSwitchService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.HmeEoJobContainerRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.mapper.HmeWorkcellEquipmentSwitchMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO2;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO3;
import com.ruike.itf.domain.repository.ItfFirstProcessIfaceRepository;
import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO;
import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO2;
import com.ruike.itf.infra.mapper.ItfFirstProcessIfaceMapper;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO6;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO7;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;
import tarzan.modeling.domain.vo.MtModWorkcellVO2;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:08
 */
@Component
public class ItfFirstProcessIfaceRepositoryImpl implements ItfFirstProcessIfaceRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private ItfFirstProcessIfaceMapper itfFirstProcessIfaceMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private HmeWorkcellEquipmentSwitchMapper hmeWorkcellEquipmentSwitchMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeWorkcellEquipmentSwitchService hmeWorkcellEquipmentSwitchService;
    @Autowired
    private HmeEoJobFirstProcessService hmeEoJobFirstProcessService;
    @Autowired
    private HmeEoJobSnBatchService hmeEoJobSnBatchService;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private HmeEoJobContainerRepository hmeEoJobContainerRepository;
    @Autowired
    private HmeEoJobSnSingleService hmeEoJobSnSingleService;

    @Override
    public void firstProcessInSiteVerify(Long tenantId, ItfFirstProcessIfaceDTO dto) {
        //????????????
        if(StringUtils.isBlank(dto.getWorkcellCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "SN???"));
        }
        if(StringUtils.isBlank(dto.getUser())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        if(StringUtils.isBlank(dto.getWorkOrderNum())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        if(StringUtils.isNotBlank(dto.getScanAssetEncoding())){
            //??????????????????????????????????????????????????????
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
        //????????????
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setWorkcellCode(dto.getWorkcellCode());
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtModWorkcell)){
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }
        dto.setWorkcellId(mtModWorkcell.getWorkcellId());
        //????????????
        Long userId = itfFirstProcessIfaceMapper.getUserIdByLoginName(dto.getUser());
        if(Objects.isNull(userId)){
            throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_INSPECTOR_ITEM_GROUP_REL_0002", "HME", dto.getUser()));
        }
        dto.setUserId(userId);
        DetailsHelper.setCustomUserDetails(userId, "zh_CN");
        //??????????????????
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if(Objects.isNull(defaultSiteId)){
            throw new MtException("HME_TAG_CHECK_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_019", "HME"));
        }
        dto.setDefaultSiteId(defaultSiteId);

        //??????????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
            setWorkOrderNum(dto.getWorkOrderNum());
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtWorkOrder)){
            throw new MtException("HME_CHIP_DATA_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0001", "HME", dto.getWorkOrderNum()));
        }
        dto.setWorkOrderId(mtWorkOrder.getWorkOrderId());
    }

    @Override
    public List<String> getEquipmentCategory(Long tenantId, String workcellCode, String siteId) {
        //??????????????????????????????????????????
        String operationId = null;
        MtModWorkcellVO1 mtModWorkcellVO1 = new MtModWorkcellVO1();
        mtModWorkcellVO1.setWorkcellCode(workcellCode);
        mtModWorkcellVO1.setWorkcellType("STATION");
        mtModWorkcellVO1.setEnableFlag(YES);
        List<String> workcellIds = mtModWorkcellRepository.propertyLimitWorkcellQuery(tenantId, mtModWorkcellVO1);

        if (CollectionUtils.isEmpty(workcellIds)) {
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }

        MtModWorkcellVO2 workcellVO = mtModWorkcellMapper.selectWorkcellById(tenantId, workcellIds.get(0));
        // ????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setOrganizationId(workcellVO.getWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOList = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        if (CollectionUtils.isEmpty(mtModOrganizationItemVOList)) {
            // ????????????Wkc??????????????????
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        } else {
            // ??????????????????
            MtOpWkcDispatchRelVO7 mtOpWkcDispatchRelVO7 = new MtOpWkcDispatchRelVO7();
            mtOpWkcDispatchRelVO7.setWorkcellId(mtModOrganizationItemVOList.get(0).getOrganizationId());
            List<MtOpWkcDispatchRelVO6> opWkcDispatchRelVO6List = mtOperationWkcDispatchRelRepository.wkcLimitAvailableOperationQuery(tenantId, mtOpWkcDispatchRelVO7);
            if (CollectionUtils.isNotEmpty(opWkcDispatchRelVO6List)) {
                // ???????????????????????????????????????
                operationId = opWkcDispatchRelVO6List.get(0).getOperationId();
            } else {
                // ????????????Wkc??????????????????
                throw new MtException("HME_EO_JOB_SN_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_021", "HME"));
            }
        }
        if (StringUtils.isBlank(operationId)) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_001", "HME"));
        }
        //????????????Id???????????????
        List<HmeOpEqRel> hmeOpEqRels = hmeWorkcellEquipmentSwitchMapper.equipmentCategoryQuery(tenantId, operationId);
        if(CollectionUtils.isEmpty(hmeOpEqRels)){
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_001", "HME"));
        }
        return hmeOpEqRels.stream().map(HmeOpEqRel::getEquipmentCategory).distinct().collect(Collectors.toList());
    }

    @Override
    public void bindEquipment(Long tenantId, List<HmeEquipment> equipmentList, String workcellId, String siteId, List<String> equipmentCategoryList) {
        for (HmeEquipment hmeEquipment:equipmentList) {
            //???????????????????????????????????????????????????????????????
            if(!equipmentCategoryList.contains(hmeEquipment.getEquipmentCategory())){
                String scanEquCateDesc = lovAdapter.queryLovMeaning("HME.EQUIPMENT_CATEGORY", tenantId, hmeEquipment.getEquipmentCategory());
                throw new MtException("HME_WKC_EQUIPMENT_SWITCH_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WKC_EQUIPMENT_SWITCH_006", "HME", hmeEquipment.getAssetEncoding(), scanEquCateDesc));
            }
        }
        //???????????????????????????????????????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        Date nowDate = new Date();
        List<HmeEquipmentWkcRel> deleteEquipmentWkcRelList= hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
            setStationId(workcellId);
            setTenantId(tenantId);
        }});
        List<String> sqlList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(deleteEquipmentWkcRelList)){
            List<String> equipmentWkcRelIdList = deleteEquipmentWkcRelList.stream().map(HmeEquipmentWkcRel::getEquipmentWkcRelId).collect(Collectors.toList());
            itfFirstProcessIfaceMapper.batchDeleteEquipmentWkcRel(tenantId, equipmentWkcRelIdList);
            //????????????
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("STATION_EQUIPMENT_UNLOAD");
            }});
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_his_s", deleteEquipmentWkcRelList.size());
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_his_cid_s", deleteEquipmentWkcRelList.size());
            int i= 0;
            for (HmeEquipmentWkcRel HmeEquipmentWkcRel:deleteEquipmentWkcRelList) {
                HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
                BeanUtils.copyProperties(HmeEquipmentWkcRel, hmeEquipmentWkcRelHis);
                hmeEquipmentWkcRelHis.setEquipmentWkcRelHisId(ids.get(i));
                hmeEquipmentWkcRelHis.setEventId(eventId);
                hmeEquipmentWkcRelHis.setCid(Long.valueOf(cids.get(i)));
                hmeEquipmentWkcRelHis.setObjectVersionNumber(1L);
                hmeEquipmentWkcRelHis.setCreatedBy(userId);
                hmeEquipmentWkcRelHis.setCreationDate(nowDate);
                hmeEquipmentWkcRelHis.setLastUpdatedBy(userId);
                hmeEquipmentWkcRelHis.setLastUpdateDate(nowDate);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeEquipmentWkcRelHis));
                i++;
            }
        }
        List<String> equipmentIdList = equipmentList.stream().map(HmeEquipment::getEquipmentId).collect(Collectors.toList());
        //????????????????????????????????????????????????????????????????????????????????????????????????${1}????????????????????????${2},?????????!
        ItfFirstProcessIfaceVO2 firstProcessIfaceVO2 = itfFirstProcessIfaceMapper.alreadyEquipmentWkcRelQuery(tenantId, equipmentIdList);
        if(!Objects.isNull(firstProcessIfaceVO2)){
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_007", "HME", firstProcessIfaceVO2.getAssetEncoding(), firstProcessIfaceVO2.getWorkcellCode()));
        }
        //???????????????????????????????????????????????????
        List<String> ids = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_s", equipmentList.size());
        List<String> cids = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_cid_s", equipmentList.size());
        List<String> hisIds = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_his_s", equipmentList.size());
        List<String> hisCids = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_his_cid_s", equipmentList.size());
        int i= 0;
        //????????????
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("STATION_EQUIPMENT_LOAD");
        }});
        for (HmeEquipment hmeEquipment:equipmentList) {
            HmeEquipmentWkcRel hmeEquipmentWkcRel = new HmeEquipmentWkcRel();
            hmeEquipmentWkcRel.setTenantId(tenantId);
            hmeEquipmentWkcRel.setEquipmentWkcRelId(ids.get(i));
            hmeEquipmentWkcRel.setSiteId(siteId);
            hmeEquipmentWkcRel.setEquipmentId(hmeEquipment.getEquipmentId());
            hmeEquipmentWkcRel.setStationId(workcellId);
            hmeEquipmentWkcRel.setEnableFlag(HmeConstants.ConstantValue.YES);
            hmeEquipmentWkcRel.setCid(Long.valueOf(cids.get(i)));
            hmeEquipmentWkcRel.setObjectVersionNumber(1L);
            hmeEquipmentWkcRel.setCreatedBy(userId);
            hmeEquipmentWkcRel.setCreationDate(nowDate);
            hmeEquipmentWkcRel.setLastUpdatedBy(userId);
            hmeEquipmentWkcRel.setLastUpdateDate(nowDate);
            sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeEquipmentWkcRel));
            HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
            BeanUtils.copyProperties(hmeEquipmentWkcRel, hmeEquipmentWkcRelHis);
            hmeEquipmentWkcRelHis.setEquipmentWkcRelHisId(hisIds.get(i));
            hmeEquipmentWkcRelHis.setEventId(eventId);
            hmeEquipmentWkcRelHis.setCid(Long.valueOf(hisCids.get(i)));
            hmeEquipmentWkcRelHis.setObjectVersionNumber(1L);
            hmeEquipmentWkcRelHis.setCreatedBy(userId);
            hmeEquipmentWkcRelHis.setCreationDate(nowDate);
            hmeEquipmentWkcRelHis.setLastUpdatedBy(userId);
            hmeEquipmentWkcRelHis.setLastUpdateDate(nowDate);
            sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeEquipmentWkcRelHis));
            i++;
        }
        if(CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Override
    public void firstProcessReleaseVerify(Long tenantId, ItfFirstProcessIfaceDTO2 dto) {
        //????????????
        if(StringUtils.isBlank(dto.getWorkcellCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "????????????"));
        }
        if(StringUtils.isBlank(dto.getUser())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        if(StringUtils.isBlank(dto.getSnNum())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "SN???"));
        }
        if(StringUtils.isBlank(dto.getWorkOrderNum())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        //????????????
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setWorkcellCode(dto.getWorkcellCode());
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtModWorkcell)){
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }
        dto.setWorkcellId(mtModWorkcell.getWorkcellId());
        //????????????
        List<String> materialLotCodeList = Arrays.asList(StringUtils.split(dto.getMaterialLotCode(), ","));
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).select(MtMaterialLot.FIELD_MATERIAL_LOT_ID).andWhere(Sqls.custom()
                .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodeList)).build());
        if(CollectionUtils.isEmpty(mtMaterialLotList) || mtMaterialLotList.size() < materialLotCodeList.size()){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
        }
        //????????????
        Long userId = itfFirstProcessIfaceMapper.getUserIdByLoginName(dto.getUser());
        if(Objects.isNull(userId)){
            throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_INSPECTOR_ITEM_GROUP_REL_0002", "HME", dto.getUser()));
        }
        dto.setUserId(userId);
        DetailsHelper.setCustomUserDetails(userId, "zh_CN");
        //??????????????????
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if(Objects.isNull(defaultSiteId)){
            throw new MtException("HME_TAG_CHECK_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_019", "HME"));
        }
        dto.setDefaultSiteId(defaultSiteId);
        //??????SN
        MtMaterialLot snMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getSnNum());
        }});
        if(Objects.isNull(snMaterialLot)){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getSnNum()));
        }
        // SN????????? ???????????????
        this.verifySnInSite(tenantId, dto.getSnNum());
        //??????????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
            setWorkOrderNum(dto.getWorkOrderNum());
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtWorkOrder)){
            throw new MtException("HME_CHIP_DATA_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0001", "HME", dto.getWorkOrderNum()));
        }
        dto.setWorkOrderId(mtWorkOrder.getWorkOrderId());
    }

    @Override
    public void firstProcessSiteOutVerify(Long tenantId, ItfFirstProcessIfaceDTO3 dto) {
        //????????????
        if(StringUtils.isBlank(dto.getWorkcellCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "????????????"));
        }
        if(StringUtils.isBlank(dto.getUser())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        if(StringUtils.isBlank(dto.getWorkOrderNum())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????"));
        }
        //????????????
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setWorkcellCode(dto.getWorkcellCode());
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtModWorkcell)){
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }
        dto.setWorkcellId(mtModWorkcell.getWorkcellId());
        //????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
        }
        // SN????????? ???????????????
        this.verifySnInSite(tenantId, dto.getMaterialLotCode());
        //????????????
        Long userId = itfFirstProcessIfaceMapper.getUserIdByLoginName(dto.getUser());
        if(Objects.isNull(userId)){
            throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_INSPECTOR_ITEM_GROUP_REL_0002", "HME", dto.getUser()));
        }
        dto.setUserId(userId);
        DetailsHelper.setCustomUserDetails(userId, "zh_CN");
        //??????????????????
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if(Objects.isNull(defaultSiteId)){
            throw new MtException("HME_TAG_CHECK_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_CHECK_019", "HME"));
        }
        dto.setDefaultSiteId(defaultSiteId);
        if (StringUtils.isNotBlank(dto.getContainerCode())) {
            // ?????????????????? ??????????????????
            List<MtContainer> containerList = mtContainerRepository.selectByCondition(Condition.builder(MtContainer.class).select(MtContainer.FIELD_CONTAINER_ID, MtContainer.FIELD_CONTAINER_CODE).andWhere(Sqls.custom()
                    .andEqualTo(MtContainer.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtContainer.FIELD_CONTAINER_CODE, dto.getContainerCode())).build());
            if (CollectionUtils.isEmpty(containerList)) {
                throw new MtException("WMS_CONTAINER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_CONTAINER_001", "HME", dto.getContainerCode()));
            }
            // ??????????????????????????????????????????
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(dto.getWorkcellId());
                setAttrName("CONTAINER_OUT");
                setTableName("mt_mod_workcell_attr");
            }});
            String containerOut = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            if (!YES.equals(containerOut)) {
                throw new MtException("HME_CONTAINER_IMPORT_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_IMPORT_011", "HME"));
            }
        }
        //??????????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
            setWorkOrderNum(dto.getWorkOrderNum());
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtWorkOrder)){
            throw new MtException("HME_CHIP_DATA_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0001", "HME", dto.getWorkOrderNum()));
        }
        dto.setWorkOrderId(mtWorkOrder.getWorkOrderId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItfFirstProcessIfaceVO inSiteInvoke(Long tenantId, ItfFirstProcessIfaceDTO dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        if(StringUtils.isNotBlank(dto.getScanAssetEncoding())){
            //??????????????????
            List<String> equipmentCategoryList = this.getEquipmentCategory(tenantId, dto.getWorkcellCode(), dto.getDefaultSiteId());
            //??????????????????
            this.bindEquipment(tenantId, dto.getHmeEquipmentList(), dto.getWorkcellId(), dto.getDefaultSiteId(), equipmentCategoryList);
        }
        //???????????????????????????????????????????????????
        HmeWkcEquSwitchDTO hmeWkcEquSwitchDTO = new HmeWkcEquSwitchDTO();
        hmeWkcEquSwitchDTO.setSiteId(dto.getDefaultSiteId());
        hmeWkcEquSwitchDTO.setWorkcellCode(dto.getWorkcellCode());
        HmeWkcEquSwitchVO2 hmeWkcEquSwitchVO2 = hmeWorkcellEquipmentSwitchService.getEquCategoryAndAssetEncoding(tenantId, hmeWkcEquSwitchDTO);
        List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS = hmeWkcEquSwitchVO2.getHmeWkcEquSwitchVOS();
        //SN??????
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setSnNum(dto.getMaterialLotCode());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3.setOperationIdList(hmeEoJobSnVO4.getOperationIdList());
        hmeEoJobSnVO3.setWorkcellCode(hmeEoJobSnVO4.getWorkcellCode());
        hmeEoJobSnVO3.setWorkcellName(hmeEoJobSnVO4.getWorkcellName());
        hmeEoJobSnVO3.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnVO3.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeEoJobSnVO3.setJobType("SINGLE_PROCESS");
        hmeEoJobSnVO3.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        if (hmeEoJobSnVO4.getHmeEoJobContainerVO2() != null) {
            hmeEoJobSnVO3.setJobContainerId(hmeEoJobSnVO4.getHmeEoJobContainerVO2().getJobContainerId());
        }
        if(CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)){
            List<HmeWkcEquSwitchDTO6> equipmentList = new ArrayList<>();
            for (HmeWkcEquSwitchVO hmeWkcEquSwitchVO:hmeWkcEquSwitchVOS) {
                // ????????????????????????
                if (StringUtils.isNotBlank(hmeWkcEquSwitchVO.getEquipmentId())) {
                    HmeWkcEquSwitchDTO6 hmeWkcEquSwitchDTO6 = new HmeWkcEquSwitchDTO6();
                    hmeWkcEquSwitchDTO6.setEquipmentId(hmeWkcEquSwitchVO.getEquipmentId());
                    hmeWkcEquSwitchDTO6.setEquipmentStatus(hmeWkcEquSwitchVO.getColor());
                    equipmentList.add(hmeWkcEquSwitchDTO6);
                }
            }
            hmeEoJobSnVO3.setEquipmentList(equipmentList);
        }
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobFirstProcessService.inSiteScan(tenantId, hmeEoJobSnVO3);
        //????????????
        ItfFirstProcessIfaceVO firstProcessIfaceVO = new ItfFirstProcessIfaceVO();
        firstProcessIfaceVO.setResult(true);
        firstProcessIfaceVO.setCurrentStepName(StringUtils.isNotBlank(hmeEoJobSnVO.getCurrentStepName())?hmeEoJobSnVO.getCurrentStepName():"");
        firstProcessIfaceVO.setCurrentStepDescription(StringUtils.isNotBlank(hmeEoJobSnVO.getCurrentStepDescription())?hmeEoJobSnVO.getCurrentStepDescription():"");
        firstProcessIfaceVO.setNextStepName(StringUtils.isNotBlank(hmeEoJobSnVO.getNextStepName())?hmeEoJobSnVO.getNextStepName():"");
        firstProcessIfaceVO.setNextStepDescription(StringUtils.isNotBlank(hmeEoJobSnVO.getNextStepDescription())?hmeEoJobSnVO.getNextStepDescription():"");
        firstProcessIfaceVO.setEoStepNum(Objects.isNull(hmeEoJobSnVO.getEoStepNum())?"":hmeEoJobSnVO.getEoStepNum().toString());
        firstProcessIfaceVO.setWoQuantityOut(Objects.isNull(hmeEoJobSnVO.getWoQuantityOut())?"":hmeEoJobSnVO.getWoQuantityOut().toString());
        firstProcessIfaceVO.setWoQuantity(Objects.isNull(hmeEoJobSnVO.getWoQuantity())?"":hmeEoJobSnVO.getWoQuantity().toString());
        firstProcessIfaceVO.setSnMaterialCode(StringUtils.isNotEmpty(hmeEoJobSnVO.getSnMaterialCode())?hmeEoJobSnVO.getSnMaterialCode():"");
        Date siteInDate = hmeEoJobSnVO.getSiteInDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!Objects.isNull(siteInDate)){
            firstProcessIfaceVO.setSiteInDate(simpleDateFormat.format(siteInDate));
        }else {
            firstProcessIfaceVO.setSiteInDate("");
        }
        Date siteOutDate = hmeEoJobSnVO.getSiteOutDate();
        if(!Objects.isNull(siteOutDate)){
            firstProcessIfaceVO.setSiteOutDate(simpleDateFormat.format(siteOutDate));
        }else {
            firstProcessIfaceVO.setSiteOutDate("");
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeEoJobSnVO.getWorkOrderId());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtWorkOrder.getMaterialId());
        if (mtMaterial != null) {
            firstProcessIfaceVO.setSapMaterialCode(mtMaterial.getMaterialCode());
            firstProcessIfaceVO.setSapMaterialName(mtMaterial.getMaterialName());
        }
        firstProcessIfaceVO.setWorkOrderNum(StringUtils.isNotBlank(hmeEoJobSnVO.getWorkOrderNum())?hmeEoJobSnVO.getWorkOrderNum():"");
        firstProcessIfaceVO.setMeterTimeStr(StringUtils.isNotBlank(hmeEoJobSnVO.getMeterTimeStr())?hmeEoJobSnVO.getMeterTimeStr():"");
        firstProcessIfaceVO.setRemark(StringUtils.isNotBlank(hmeEoJobSnVO.getRemark())?hmeEoJobSnVO.getRemark():"");
        firstProcessIfaceVO.setSnMaterialName(StringUtils.isNotBlank(hmeEoJobSnVO.getSnMaterialName())?hmeEoJobSnVO.getSnMaterialName():"");
        firstProcessIfaceVO.setWorkcellName(StringUtils.isNotBlank(hmeEoJobSnVO.getWorkcellName())?hmeEoJobSnVO.getWorkcellName():"");
        firstProcessIfaceVO.setRouterStepRemark(StringUtils.isNotBlank(hmeEoJobSnVO.getRouterStepRemark())?hmeEoJobSnVO.getRouterStepRemark():"");
        firstProcessIfaceVO.setLabCode(StringUtils.isNotBlank(hmeEoJobSnVO.getLabCode())?hmeEoJobSnVO.getLabCode():"");
        return firstProcessIfaceVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItfFirstProcessIfaceVO releaseInvoke(Long tenantId, ItfFirstProcessIfaceDTO2 dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        // ???????????????????????????
        List<String> materialLotCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            materialLotCodeList = Arrays.asList(dto.getMaterialLotCode().split(","));
        }
        ItfFirstProcessIfaceVO firstProcessIfaceVO = new ItfFirstProcessIfaceVO();
        firstProcessIfaceVO.setResult(true);
        //???????????????????????????????????????????????????
        HmeWkcEquSwitchDTO hmeWkcEquSwitchDTO = new HmeWkcEquSwitchDTO();
        hmeWkcEquSwitchDTO.setSiteId(dto.getDefaultSiteId());
        hmeWkcEquSwitchDTO.setWorkcellCode(dto.getWorkcellCode());
        HmeWkcEquSwitchVO2 hmeWkcEquSwitchVO2 = hmeWorkcellEquipmentSwitchService.getEquCategoryAndAssetEncoding(tenantId, hmeWkcEquSwitchDTO);
        List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS = hmeWkcEquSwitchVO2.getHmeWkcEquSwitchVOS();
        //SN??????
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setSnNum(dto.getSnNum());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3.setOperationIdList(hmeEoJobSnVO4.getOperationIdList());
        hmeEoJobSnVO3.setWorkcellCode(hmeEoJobSnVO4.getWorkcellCode());
        hmeEoJobSnVO3.setWorkcellName(hmeEoJobSnVO4.getWorkcellName());
        hmeEoJobSnVO3.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnVO3.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeEoJobSnVO3.setJobType("SINGLE_PROCESS");
        hmeEoJobSnVO3.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        if (hmeEoJobSnVO4.getHmeEoJobContainerVO2() != null) {
            hmeEoJobSnVO3.setJobContainerId(hmeEoJobSnVO4.getHmeEoJobContainerVO2().getJobContainerId());
        }
        if(CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)){
            List<HmeWkcEquSwitchDTO6> equipmentList = new ArrayList<>();
            for (HmeWkcEquSwitchVO hmeWkcEquSwitchVO:hmeWkcEquSwitchVOS) {
                // ????????????????????????
                if (StringUtils.isNotBlank(hmeWkcEquSwitchVO.getEquipmentId())) {
                    HmeWkcEquSwitchDTO6 hmeWkcEquSwitchDTO6 = new HmeWkcEquSwitchDTO6();
                    hmeWkcEquSwitchDTO6.setEquipmentId(hmeWkcEquSwitchVO.getEquipmentId());
                    hmeWkcEquSwitchDTO6.setEquipmentStatus(hmeWkcEquSwitchVO.getColor());
                    equipmentList.add(hmeWkcEquSwitchDTO6);
                }
            }
            hmeEoJobSnVO3.setEquipmentList(equipmentList);
        }
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobFirstProcessService.inSiteScan(tenantId, hmeEoJobSnVO3);
        if (StringUtils.isBlank(hmeEoJobSnVO.getSiteId())) {
            hmeEoJobSnVO.setSiteId(wmsSiteRepository.userDefaultSite(tenantId));
        }
        // ??????bom??????
        HmeEoJobSnBatchDTO3 batchDTO3 = new HmeEoJobSnBatchDTO3();
        batchDTO3.setSiteId(dto.getDefaultSiteId());
        batchDTO3.setDtoList(Collections.singletonList(hmeEoJobSnVO));
        List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4s = hmeEoJobSnBatchService.releaseQuery(tenantId, batchDTO3);
        // ????????????bom?????? ???????????????
        List<HmeEoJobSnBatchVO4> componentList = new ArrayList<>();
        Boolean virtualNumFlag = false;
        for (String materialLotCode : materialLotCodeList) {
            // ??????????????? ????????? ???????????????
            HmeEoJobSnBatchDTO2 batchDTO2 = new HmeEoJobSnBatchDTO2();
            batchDTO2.setComponentList(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4s) ? hmeEoJobSnBatchVO4s : Collections.EMPTY_LIST);
            batchDTO2.setMaterialLotCode(materialLotCode);
            batchDTO2.setSiteId(dto.getDefaultSiteId());
            batchDTO2.setWorkcellId(dto.getWorkcellId());
            batchDTO2.setSnLineList(Collections.singletonList(hmeEoJobSnVO));
            HmeEoJobSnBatchVO14 hmeEoJobSnBatchVO14 = hmeEoJobSnBatchService.releaseScan(tenantId, batchDTO2);
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO14.getDeleteFlag())) {
                // ????????? ???????????????
                HmeEoJobSnBatchVO4 batchVO4 = new HmeEoJobSnBatchVO4();
                HmeEoJobSnBatchVO4 component = hmeEoJobSnBatchVO14.getComponent();
                BeanUtils.copyProperties(component, batchVO4);
                hmeEoJobSnBatchService.deleteMaterial(tenantId, batchVO4);

                HmeEoJobSnBatchDTO2 batchDTO2Two = new HmeEoJobSnBatchDTO2();
                batchDTO2Two.setComponentList(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4s) ? hmeEoJobSnBatchVO4s : Collections.EMPTY_LIST);
                batchDTO2Two.setMaterialLotCode(materialLotCode);
                batchDTO2Two.setSiteId(dto.getDefaultSiteId());
                batchDTO2Two.setWorkcellId(dto.getWorkcellId());
                batchDTO2Two.setSnLineList(Collections.singletonList(hmeEoJobSnVO));
                hmeEoJobSnBatchVO14 = hmeEoJobSnBatchService.releaseScan(tenantId, batchDTO2Two);
            }
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO14.getComponent().getCosMaterialLotFlag())) {
                // ???????????????????????????
                List<HmeVirtualNumVO> virtualNumVOList = hmeEoJobFirstProcessService.virtualNumQuery(tenantId, Collections.singletonList(materialLotCode));
                // ????????????????????????????????????  ??????????????????
                Optional<HmeVirtualNumVO> firstOpt = virtualNumVOList.stream().filter(vn -> (vn.getChipLocation().startsWith(dto.getChipLocation()))).findFirst();
                if (!firstOpt.isPresent()) {
                    throw new MtException("HME_EQUIPMENT_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_017", "HME"));
                }
                // ??????????????????????????? ??????????????????
                if (StringUtils.equals(firstOpt.get().getEnableFlag(), "N")) {
                    throw new MtException("HME_EQUIPMENT_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_020", "HME"));
                }
                hmeEoJobSnBatchVO14.getComponent().setVirtualIdList(Collections.singletonList(firstOpt.get().getVirtualId()));
                virtualNumFlag = true;
            }
            hmeEoJobSnBatchVO14.getComponent().setIsReleased(HmeConstants.ConstantValue.ONE);
            String materialId = hmeEoJobSnBatchVO14.getComponent().getMaterialId();
            Optional<HmeEoJobSnBatchVO4> firstOpt = componentList.stream().filter(component -> StringUtils.equals(component.getMaterialId(), materialId)).findFirst();
            Optional<HmeEoJobSnBatchVO6> releaseMaterialLotListOpt = hmeEoJobSnBatchVO14.getComponent().getMaterialLotList().stream().filter(code -> StringUtils.equals(code.getMaterialLotCode(), materialLotCode)).findFirst();
            if (firstOpt.isPresent()) {
                // ??????????????????
                componentList.stream().forEach(component -> {
                    if (StringUtils.equals(component.getMaterialId(), firstOpt.get().getMaterialId())) {
                        component.setSelectedSnCount(component.getSelectedSnCount() != null ? component.getSelectedSnCount().add(BigDecimal.ONE) : BigDecimal.ONE);
                        component.setSelectedSnQty(component.getSelectedSnQty() != null ? component.getSelectedSnQty().add(releaseMaterialLotListOpt.get().getPrimaryUomQty()) : releaseMaterialLotListOpt.get().getPrimaryUomQty());
                    }
                });
            } else {
                componentList.add(hmeEoJobSnBatchVO14.getComponent());
            }
        }
        // ????????? ???????????? ???????????? ???????????????
        Date currentDate = CommonUtils.currentTimeGet();
        List<String> finalMaterialLotCodeList = materialLotCodeList;
        for (HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4 : componentList) {
            if (HmeConstants.MaterialTypeCode.TIME.equals(hmeEoJobSnBatchVO4.getProductionType())) {
                if (CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4.getMaterialLotList())) {
                    Optional<HmeEoJobSnBatchVO6> firstOpt = hmeEoJobSnBatchVO4.getMaterialLotList().stream().filter(vo -> {
                        if (StringUtils.isNotBlank(vo.getDeadLineDate())) {
                            Date deadDate = DateUtil.string2Date(vo.getDeadLineDate(), "yyyy-MM-dd HH:mm:ss");
                            if (finalMaterialLotCodeList.contains(vo.getMaterialLotCode())) {
                                vo.setIsReleased(HmeConstants.ConstantValue.ONE);
                            } else {
                                vo.setIsReleased(HmeConstants.ConstantValue.ZERO);
                            }
                            return HmeConstants.ConstantValue.ONE.compareTo(vo.getIsReleased()) == 0 && deadDate.before(currentDate);
                        }
                        return false;
                    }).findFirst();
                    if (firstOpt.isPresent()) {
                        // ??????????????????${1}????????????,?????????!
                        throw new MtException("HME_EQUIPMENT_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_021", "HME", firstOpt.get().getMaterialLotCode()));
                    }
                }
            }
            // ???????????????????????? is_release ??????0
            for (HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 : hmeEoJobSnBatchVO4.getMaterialLotList()) {
                if (materialLotCodeList.contains(hmeEoJobSnBatchVO6.getMaterialLotCode())) {
                    hmeEoJobSnBatchVO6.setIsReleased(HmeConstants.ConstantValue.ONE);
                } else {
                    hmeEoJobSnBatchVO6.setIsReleased(HmeConstants.ConstantValue.ZERO);
                }
            }
        }

        // ??????????????????????????? ????????????????????????
        HmeEoJobSnSingleDTO snSingleDTO = new HmeEoJobSnSingleDTO();
        snSingleDTO.setSnLineDto(hmeEoJobSnVO);
        snSingleDTO.setComponentList(componentList);
        if (virtualNumFlag) {
            snSingleDTO.setUseSourceFlag(HmeConstants.ConstantValue.NO);
            snSingleDTO.setIsEquipmentFirstProcess(HmeConstants.ConstantValue.YES);
        }
        snSingleDTO.setIsPumpProcess(HmeConstants.ConstantValue.NO);
        snSingleDTO.setIsFirstProcess(HmeConstants.ConstantValue.YES);
        hmeEoJobFirstProcessService.release(tenantId, snSingleDTO);
        return firstProcessIfaceVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItfFirstProcessIfaceVO outSiteInvoke(Long tenantId, ItfFirstProcessIfaceDTO3 dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        ItfFirstProcessIfaceVO firstProcessIfaceVO = new ItfFirstProcessIfaceVO();
        firstProcessIfaceVO.setResult(true);
        // ????????????????????????  ???????????????
        if (StringUtils.isNotBlank(dto.getContainerCode())) {
            HmeEoJobContainerVO hmeEoJobContainerVO = new HmeEoJobContainerVO();
            hmeEoJobContainerVO.setWorkcellId(dto.getWorkcellId());
            hmeEoJobContainerVO.setContainerCode(dto.getContainerCode());
            hmeEoJobContainerRepository.updateEoJobContainer(tenantId, hmeEoJobContainerVO);
        }
        // ??????????????????
        if (isContainerControl(tenantId, hmeEoJobSnVO4.getWorkcellId())) {
            // ??????????????????????????????
            hmeEoJobSnVO4.setIsContainerOut(1);
            HmeEoJobContainer eoJobContainer = hmeEoJobContainerRepository.wkcLimitJobContainerGet(tenantId,
                    hmeEoJobSnVO4.getWorkcellId());
            if (Objects.nonNull(eoJobContainer)) {
                hmeEoJobSnVO4.setJobContainerId(eoJobContainer.getJobContainerId());
                hmeEoJobSnVO4.setContainerId(eoJobContainer.getContainerId());
                hmeEoJobSnVO4.setContainerCode(eoJobContainer.getContainerCode());

                // ?????????????????????????????????
                HmeEoJobContainerVO2 eoJobContainerVO2 = hmeEoJobContainerRepository
                        .eoJobContainerPropertyGet(tenantId, eoJobContainer.getJobContainerId());
                hmeEoJobSnVO4.setHmeEoJobContainerVO2(eoJobContainerVO2);
            }
        } else {
            hmeEoJobSnVO4.setIsContainerOut(0);
        }
        //???????????????????????????????????????????????????
        HmeWkcEquSwitchDTO hmeWkcEquSwitchDTO = new HmeWkcEquSwitchDTO();
        hmeWkcEquSwitchDTO.setSiteId(dto.getDefaultSiteId());
        hmeWkcEquSwitchDTO.setWorkcellCode(dto.getWorkcellCode());
        HmeWkcEquSwitchVO2 hmeWkcEquSwitchVO2 = hmeWorkcellEquipmentSwitchService.getEquCategoryAndAssetEncoding(tenantId, hmeWkcEquSwitchDTO);
        List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS = hmeWkcEquSwitchVO2.getHmeWkcEquSwitchVOS();
        //SN??????
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setSnNum(dto.getMaterialLotCode());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3.setOperationIdList(hmeEoJobSnVO4.getOperationIdList());
        hmeEoJobSnVO3.setWorkcellCode(hmeEoJobSnVO4.getWorkcellCode());
        hmeEoJobSnVO3.setWorkcellName(hmeEoJobSnVO4.getWorkcellName());
        hmeEoJobSnVO3.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnVO3.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeEoJobSnVO3.setJobType("SINGLE_PROCESS");
        hmeEoJobSnVO3.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        if (hmeEoJobSnVO4.getHmeEoJobContainerVO2() != null) {
            hmeEoJobSnVO3.setJobContainerId(hmeEoJobSnVO4.getHmeEoJobContainerVO2().getJobContainerId());
        }
        if(CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)){
            List<HmeWkcEquSwitchDTO6> equipmentList = new ArrayList<>();
            for (HmeWkcEquSwitchVO hmeWkcEquSwitchVO:hmeWkcEquSwitchVOS) {
                // ????????????????????????
                if (StringUtils.isNotBlank(hmeWkcEquSwitchVO.getEquipmentId())) {
                    HmeWkcEquSwitchDTO6 hmeWkcEquSwitchDTO6 = new HmeWkcEquSwitchDTO6();
                    hmeWkcEquSwitchDTO6.setEquipmentId(hmeWkcEquSwitchVO.getEquipmentId());
                    hmeWkcEquSwitchDTO6.setEquipmentStatus(hmeWkcEquSwitchVO.getColor());
                    equipmentList.add(hmeWkcEquSwitchDTO6);
                }
            }
            hmeEoJobSnVO3.setEquipmentList(equipmentList);
        }
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobFirstProcessService.inSiteScan(tenantId, hmeEoJobSnVO3);

        HmeEoJobSnVO3 eoJobSnVO3 = new HmeEoJobSnVO3();
        BeanUtils.copyProperties(hmeEoJobSnVO, eoJobSnVO3);
        eoJobSnVO3.setEquipmentList(hmeEoJobSnVO3.getEquipmentList());
        eoJobSnVO3.setHmeEoJobContainerVO2(hmeEoJobSnVO4.getHmeEoJobContainerVO2());
        eoJobSnVO3.setOutSiteAction(HmeConstants.OutSiteAction.COMPLETE);
        eoJobSnVO3.setJobType("SINGLE_PROCESS");
        eoJobSnVO3.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        eoJobSnVO3.setIsRecordLabCode(StringUtils.isBlank(hmeEoJobSnVO.getLabCode()) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);
        eoJobSnVO3.setContainerId(hmeEoJobSnVO4.getContainerId());
        eoJobSnVO3.setSiteId(dto.getDefaultSiteId());
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnSingleService.outSiteScan(tenantId, eoJobSnVO3);
        if (StringUtils.isNotBlank(hmeEoJobSn.getErrorCode())) {
            // ????????????????????? ??????????????????
            if ("HME_EO_JOB_SN_172".equals(hmeEoJobSn.getErrorCode())) {
                // ??????????????? ??????????????????
                eoJobSnVO3.setProcessNcDetailList(hmeEoJobSn.getProcessNcDetailList());
            }
            eoJobSnVO3.setContinueFlag(HmeConstants.ConstantValue.YES);
            eoJobSnVO3.setErrorCode(hmeEoJobSn.getErrorCode());
            hmeEoJobSnSingleService.outSiteScan(tenantId, eoJobSnVO3);
        }
        return firstProcessIfaceVO;
    }

    private void verifySnInSite (Long tenantId, String materialLotCode) {
        List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).select(MtEo.FIELD_EO_ID).andWhere(Sqls.custom()
                .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtEo.FIELD_IDENTIFICATION, materialLotCode)).build());
        if (CollectionUtils.isNotEmpty(mtEoList)) {
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).select(HmeEoJobSn.FIELD_JOB_ID).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(HmeEoJobSn.FIELD_EO_ID, mtEoList.get(0).getEoId())
                    .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());
            if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
                // ??????????????????,?????????!
                throw new MtException("HME_EQUIPMENT_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_019", "HME"));
            }
        }
    }

    private boolean isContainerControl(Long tenantId, String workcellId) {
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName("mt_mod_workcell_attr");
        mtExtendVO.setKeyId(workcellId);
        mtExtendVO.setAttrName("CONTAINER_OUT");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        // ??????????????????
        return CollectionUtils.isNotEmpty(mtExtendAttrVOList)
                && YES.equals(mtExtendAttrVOList.get(0).getAttrValue());
    }

}
