package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.api.dto.HmeLoadJobDTO3;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO2;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO3;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO;
import com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO2;
import com.ruike.hme.infra.mapper.*;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO2;
import com.ruike.itf.domain.repository.ItfMaterialLotSiteInRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfMaterialLotSiteInMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO6;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO7;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;
import tarzan.modeling.domain.vo.MtModWorkcellVO2;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ItfMaterialLotSiteInRepositoryImpl
 *
 * @author: chaonan.hu@hand-china.com 2021-10-11 17:04:12
 **/
@Component
@Slf4j
public class ItfMaterialLotSiteInRepositoryImpl implements ItfMaterialLotSiteInRepository {

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private ItfMaterialLotSiteInMapper itfMaterialLotSiteInMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private HmeWorkcellEquipmentSwitchMapper hmeWorkcellEquipmentSwitchMapper;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
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
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeVisualInspectionMapper hmeVisualInspectionMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;
    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;
    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;
    @Autowired
    private HmeMaterialLotLoadRepository materialLotLoadRepository;
    @Autowired
    private HmeVisualInspectionRepository hmeVisualInspectionRepository;
    @Autowired
    private HmeEoJobEquipmentMapper hmeEoJobEquipmentMapper;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    @Override
    public void materialLotSiteInVerify(Long tenantId, ItfMaterialLotSiteDTO dto) {
        if(StringUtils.isNotBlank(dto.getScanAssetEncoding())){
            //当设备不为空时，需要校验设备是否存在
            List<String> assetEncodingList = Arrays.asList(dto.getScanAssetEncoding().split(","));
            List<HmeEquipment> hmeEquipmentList = hmeEquipmentRepository.selectByCondition(Condition.builder(HmeEquipment.class)
                    .andWhere(Sqls.custom()
                            .andIn(HmeEquipment.FIELD_ASSET_ENCODING, assetEncodingList)
                            .andEqualTo(HmeEquipment.FIELD_TENANT_ID, tenantId))
                    .build());
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
        //物料批存在
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
        }
        //用户存在
        Long userId = itfMaterialLotSiteInMapper.getUserIdByLoginName(dto.getUser());
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

    @Override
    public List<String> getEquipmentCategory(Long tenantId, String workcellCode, String siteId) {
        //根据当前登陆工位找到对应工艺
        String operationId = null;
        MtModWorkcellVO1 mtModWorkcellVO1 = new MtModWorkcellVO1();
        mtModWorkcellVO1.setWorkcellCode(workcellCode);
        mtModWorkcellVO1.setWorkcellType("STATION");
        mtModWorkcellVO1.setEnableFlag("Y");
        List<String> workcellIds = mtModWorkcellRepository.propertyLimitWorkcellQuery(tenantId, mtModWorkcellVO1);

        if (CollectionUtils.isEmpty(workcellIds)) {
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }

        //获取当前班次
        Date shiftDate = null;
        String shiftCode = null;
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
            setTopSiteId(siteId);
            setOrganizationType("WORKCELL");
            setOrganizationId(workcellIds.get(0));
            setParentOrganizationType("WORKCELL");
            setQueryType("TOP");
        }});
        if(CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)){
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, mtModOrganizationItemVOS.get(0).getOrganizationId());
            if(mtWkcShiftVO3 != null){
                shiftCode = mtWkcShiftVO3.getShiftCode();
                shiftDate = mtWkcShiftVO3.getShiftDate();
            }
        }

        MtModWorkcellVO2 workcellVO = mtModWorkcellMapper.selectWorkcellById(tenantId, workcellIds.get(0));
        // 获取工序
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setOrganizationId(workcellVO.getWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOList =
                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        if (CollectionUtils.isEmpty(mtModOrganizationItemVOList)) {
            // 请先维护Wkc工序工位关系
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        } else {
            // 获取工艺路线
            MtOpWkcDispatchRelVO7 mtOpWkcDispatchRelVO7 = new MtOpWkcDispatchRelVO7();
            mtOpWkcDispatchRelVO7.setWorkcellId(mtModOrganizationItemVOList.get(0).getOrganizationId());
            List<MtOpWkcDispatchRelVO6> opWkcDispatchRelVO6List =
                    mtOperationWkcDispatchRelRepository.wkcLimitAvailableOperationQuery(tenantId, mtOpWkcDispatchRelVO7);
            if (CollectionUtils.isNotEmpty(opWkcDispatchRelVO6List)) {
                // 获取当前工位对应的工艺信息
                operationId = opWkcDispatchRelVO6List.get(0).getOperationId();
            } else {
                // 请先维护Wkc工序工艺关系
                throw new MtException("HME_EO_JOB_SN_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_021", "HME"));
            }
        }
        if (StringUtils.isBlank(operationId)) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_001", "HME"));
        }
        //根据工艺Id查询设备类
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
            //校验设备的设备类别是否在该工位的设备类别下
            if(!equipmentCategoryList.contains(hmeEquipment.getEquipmentCategory())){
                String scanEquCateDesc = lovAdapter.queryLovMeaning("HME.EQUIPMENT_CATEGORY", tenantId, hmeEquipment.getEquipmentCategory());
                throw new MtException("HME_WKC_EQUIPMENT_SWITCH_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WKC_EQUIPMENT_SWITCH_006", "HME", hmeEquipment.getAssetEncoding(), scanEquCateDesc));
            }
        }
        //先删除原先的工位设备关系并记录历史
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
            itfMaterialLotSiteInMapper.batchDeleteEquipmentWkcRel(tenantId, equipmentWkcRelIdList);
            //记录历史
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
        //查询这些设备是否绑定在其他工位上，如果绑在其他工位上，则报错设备${1}已绑定在其他工位${2},请检查!
        ItfMaterialLotSiteInVO2 itfMaterialLotSiteInVO2 = itfMaterialLotSiteInMapper.alreadyEquipmentWkcRelQuery(tenantId, equipmentIdList);
        if(!Objects.isNull(itfMaterialLotSiteInVO2)){
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_007", "HME", itfMaterialLotSiteInVO2.getAssetEncoding(), itfMaterialLotSiteInVO2.getWorkcellCode()));
        }
        //再绑定现在的工位设备关系并记录历史
        List<String> ids = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_s", equipmentList.size());
        List<String> cids = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_cid_s", equipmentList.size());
        List<String> hisIds = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_his_s", equipmentList.size());
        List<String> hisCids = mtCustomDbRepository.getNextKeys("hme_equipment_wkc_rel_his_cid_s", equipmentList.size());
        int i= 0;
        //记录历史
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
            hmeEquipmentWkcRel.setEnableFlag("Y");
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
    public void scanMaterialLotVerify(Long tenantId, HmeVisualInspectionDTO2 dto) {
        long startDate = System.currentTimeMillis();
        if(StringUtils.isBlank(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getOperationId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工艺"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "物料批"));
        }
        if(StringUtils.isBlank(dto.getWkcShiftId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "班次"));
        }
        //条码校验
        long startDate2 = System.currentTimeMillis();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
        }
        if(!ItfConstant.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0015", "HME", dto.getMaterialLotCode()));
        }
        if (ItfConstant.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag()) || ItfConstant.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        long endDate2 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 条码校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate2 - startDate2));
        //在制品标识校验
        String mfFlagAttr = hmeVisualInspectionMapper.getMaterialLotAttrValueByAttrName(tenantId, mtMaterialLot.getMaterialLotId(), "MF_FLAG");
        if(StringUtils.isBlank(mfFlagAttr) || !ItfConstant.ConstantValue.YES.equals(mfFlagAttr)){
            throw new MtException("HME_NC_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0017", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        long endDate3 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 在制品标识校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate3 - endDate2));
        //是否存在未出站数据
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.queryEoJobSnByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
        if(CollectionUtils.isNotEmpty(hmeEoJobSnList)){
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeEoJobSnList.get(0).getWorkcellId());
            String jobTypeMeaning = lovAdapter.queryLovMeaning("HME.JOB_TYPE", tenantId, hmeEoJobSnList.get(0).getJobType());
            throw new MtException("HME_CHIP_TRANSFER_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_017", "HME", mtModWorkcell.getWorkcellCode(), jobTypeMeaning));
        }
        long endDate4 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 是否存在未出站数据校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate4 - endDate3));
        //条码物料与工单物料是否一致的校验
        String workOrderId = hmeVisualInspectionMapper.getMaterialLotAttrValueByAttrName(tenantId, mtMaterialLot.getMaterialLotId(), "WORK_ORDER_ID");
        if(StringUtils.isBlank(workOrderId)){
            throw new MtException("HME_COS_BARCODE_RETEST_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_002", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
        if(Objects.isNull(mtWorkOrder) || !mtMaterialLot.getMaterialId().equals(mtWorkOrder.getMaterialId())){
            throw new MtException("HME_COS_BARCODE_RETEST_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_002", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        long endDate6 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 条码物料与工单物料是否一致校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate6 - endDate4));
        //位置未录入热沉编号校验
        List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeVisualInspectionMapper.noHotSinkDataQuery(tenantId, mtMaterialLot.getMaterialLotId());
        if(CollectionUtils.isNotEmpty(hmeMaterialLotLoads)){
            List<String> positionList = new ArrayList<>();
            for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoads) {
                positionList.add(((char) (64 + hmeMaterialLotLoad.getLoadRow())) + String.valueOf(hmeMaterialLotLoad.getLoadColumn()));
            }
            String position = StringUtils.join(positionList, "-");
            throw new MtException("HME_COS_TEST_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_TEST_011", "HME", position));
        }
        long endDate7 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 位置未录入热沉编号校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate7 - endDate6));
        long endDate = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 校验总耗时：{}毫秒", dto.getMaterialLotCode(), (endDate - startDate));
    }

    @Override
    public void scanMaterialLot(Long tenantId, HmeVisualInspectionDTO2 dto, List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS) {
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
        hmeEoJobSn.setJobType("COS_TEST");
        hmeEoJobSn.setSourceJobId(hmeCosOperationRecordDb.getOperationRecordId());
        hmeEoJobSn.setSnQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
        // 20210311 edit by chaonan.hu for zhenyong.ban 工位、eo、条码、返修标识、job_type取出最大的eo_step_num + 1
        long startDate3 = System.currentTimeMillis();
        Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeEoJobSn.getWorkcellId(), hmeEoJobSn.getEoId(), hmeEoJobSn.getMaterialLotId(), ItfConstant.ConstantValue.NO, hmeEoJobSn.getJobType(), hmeEoJobSn.getOperationId());
        long endDate3 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 查询最大的eo_step_num耗时：{}毫秒", dto.getMaterialLotCode(), (endDate3 - startDate3));
        hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
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
        if(CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)){
            for (HmeWkcEquSwitchVO hmeWkcEquSwitchVO:hmeWkcEquSwitchVOS) {
                if(StringUtils.isNotBlank(hmeWkcEquSwitchVO.getEquipmentId())){
                    HmeEoJobEquipment hmeEoJobEquipment = new HmeEoJobEquipment();
                    hmeEoJobEquipment.setTenantId(tenantId);
                    hmeEoJobEquipment.setJobId(hmeEoJobSn.getJobId());
                    hmeEoJobEquipment.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobEquipment.setEquipmentId(hmeWkcEquSwitchVO.getEquipmentId());
                    hmeEoJobEquipment.setEquipmentStatus(hmeWkcEquSwitchVO.getColor());
                    hmeEoJobEquipmentRepository.insertSelective(hmeEoJobEquipment);
                }
            }
            long endDate6 = System.currentTimeMillis();
            log.info("<====目检完工 条码{}进站 新增SN进出站设备状态记录共{}条耗时：{}毫秒", dto.getMaterialLotCode(), hmeWkcEquSwitchVOS.size(), (endDate6 - startDate6));
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
            if (CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)) {
                List<String> assetEncodingList = hmeWkcEquSwitchVOS.stream().map(HmeWkcEquSwitchVO::getEquipmentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                hmeLoadJobDTO3.setAssetEncodingList(assetEncodingList);
                log.info("<====目检完工 条码{}进站 记录COS履历找到设备信息{}条", dto.getMaterialLotCode(), assetEncodingList.size());
            }
            if(CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)){
                List<String> sqlList = hmeVisualInspectionRepository.createLoadJob(tenantId, hmeMaterialLotLoadList, hmeLoadJobDTO3,
                        "COS_TEST_IN", hmeCosOperationRecord, now, userId, mtMaterialLot);
                this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
            long endDate7 = System.currentTimeMillis();
            log.info("<====目检完工 条码{}进站 记录COS履历耗时：{}毫秒 找到物料批装载信息{}条", dto.getMaterialLotCode(),  (endDate7 - startDate7), hmeMaterialLotLoadList.size());
        }
    }

    @Override
    public void materialLotSiteOutVerify(Long tenantId, ItfMaterialLotSiteDTO2 dto) {
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
        //物料批存在
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
        }
        dto.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        //用户存在
        Long userId = itfMaterialLotSiteInMapper.getUserIdByLoginName(dto.getUser());
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

    @Override
    public void materialLotCompleteVerify(Long tenantId, HmeVisualInspectionDTO3 dto) {
        if(StringUtils.isBlank(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getOperationId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工艺"));
        }
        if(CollectionUtils.isEmpty(dto.getMaterialLotList())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "勾选条码信息"));
        }
        List<String> materialLotIdList = dto.getMaterialLotList().stream().map(HmeVisualInspectionVO::getMaterialLotId).collect(Collectors.toList());
        //2021-05-31 09:56:06 add by chaonan.hu for zhenyong.ban 增加对条码在制品标识的校验
        String mfIsNMaterialLot = hmeVisualInspectionMapper.getMfIsNMaterialLot(tenantId, materialLotIdList);
        if(StringUtils.isNotBlank(mfIsNMaterialLot)){
            throw new MtException("HME_NC_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0017", "HME", mfIsNMaterialLot));
        }
        //2021-07-05 16:56:12 add by chaonan.hu for peng.zhao  增加对条码的冻结标识、盘点标识的校验
        List<MtMaterialLot> mtMaterialLotList = hmeVisualInspectionMapper.materialLotFreezeStocktakeFlagQuery(tenantId, materialLotIdList);
        mtMaterialLotList = mtMaterialLotList.stream().filter(item -> "Y".equals(item.getStocktakeFlag()) || "Y".equals(item.getFreezeFlag())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(mtMaterialLotList)){
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLotList.get(0).getMaterialLotCode()));
        }
        //2021-02-23 14:46 add by chaonan.hu for zhenyong.ban 增加位置未进行性能测试,不允许目检完工的校验
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeVisualInspectionMapper.noFunctionTestDataQuery(tenantId, materialLotIdList.get(0));
        if(CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)){
            List<String> positionList = new ArrayList<>();
            for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoadList) {
                positionList.add(((char) (64 + hmeMaterialLotLoad.getLoadRow())) + String.valueOf(hmeMaterialLotLoad.getLoadColumn()));
            }
            String position = StringUtils.join(positionList, "-");
            throw new MtException("HME_COS_TEST_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_TEST_012", "HME", position));
        }
    }

    @Override
    public void materialLotComplete(Long tenantId, HmeVisualInspectionDTO3 dto) {
        HmeVisualInspectionVO hmeVisualInspectionVO = dto.getMaterialLotList().get(0);
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
        //更新hme_eo_job_sn表，条码出站
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(hmeVisualInspectionVO.getJobId());
        hmeEoJobSn.setAttribute6(mtMaterialLot.getPrimaryUomQty().toString());
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Date now = new Date();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        hmeEoJobSn.setSiteOutBy(userId);
        hmeEoJobSn.setSiteOutDate(now);
        hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);
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
        //记录COS履历
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
            }
            List<String> sqlList = hmeVisualInspectionRepository.createLoadJob(tenantId, hmeMaterialLotLoadList, hmeLoadJobDTO3,
                    "COS_TEST_OUT", hmeCosOperationRecord,now, userId, mtMaterialLot);
            if(CollectionUtils.isNotEmpty(sqlList)){
                this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
    }
}
