package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.mapper.HmeWorkcellEquipmentSwitchMapper;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO2;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO3;
import com.ruike.itf.domain.repository.ItfSingleIfaceRepository;
import com.ruike.itf.domain.vo.ItfSingleIfaceVO2;
import com.ruike.itf.infra.mapper.ItfSingleIfaceMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
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
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO6;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO7;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;
import tarzan.modeling.domain.vo.MtModWorkcellVO2;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * 单件设备接口资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-09-27 10:08:12
 */
@Component
public class ItfSingleIfaceRepositoryImpl implements ItfSingleIfaceRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private ItfSingleIfaceMapper itfSingleIfaceMapper;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HmeWorkcellEquipmentSwitchMapper hmeWorkcellEquipmentSwitchMapper;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Override
    public void singleInSiteVerify(Long tenantId, ItfSingleIfaceDTO dto) {
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
        Long userId = itfSingleIfaceMapper.getUserIdByLoginName(dto.getUser());
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
        mtModWorkcellVO1.setEnableFlag(YES);
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
            itfSingleIfaceMapper.batchDeleteEquipmentWkcRel(tenantId, equipmentWkcRelIdList);
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
        ItfSingleIfaceVO2 itfSingleIfaceVO2 = itfSingleIfaceMapper.alreadyEquipmentWkcRelQuery(tenantId, equipmentIdList);
        if(!Objects.isNull(itfSingleIfaceVO2)){
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_007", "HME", itfSingleIfaceVO2.getAssetEncoding(), itfSingleIfaceVO2.getWorkcellCode()));
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
    public void singleReleaseVerify(Long tenantId, ItfSingleIfaceDTO2 dto) {
        //必输校验
        if(StringUtils.isBlank(dto.getWorkcellCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "扫描条码"));
        }
        if(StringUtils.isBlank(dto.getUser())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "用户"));
        }
        if(StringUtils.isBlank(dto.getSnNum())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "SN号"));
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
        //扫描条码
        List<String> materialLotCodeList = Arrays.asList(StringUtils.split(dto.getMaterialLotCode(), ","));
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).select(MtMaterialLot.FIELD_MATERIAL_LOT_ID).andWhere(Sqls.custom()
                .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodeList)).build());
        if(CollectionUtils.isEmpty(mtMaterialLotList) || mtMaterialLotList.size() < materialLotCodeList.size()){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
        }
        //用户存在
        Long userId = itfSingleIfaceMapper.getUserIdByLoginName(dto.getUser());
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
        //进站SN
        MtMaterialLot snMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getSnNum());
        }});
        if(Objects.isNull(snMaterialLot)){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getSnNum()));
        }
        this.verifySnInSite(tenantId, dto.getSnNum());
    }

    @Override
    public void singleSiteOutVerify(Long tenantId, ItfSingleIfaceDTO3 dto) {
        //必输校验
        if(StringUtils.isBlank(dto.getWorkcellCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "扫描条码"));
        }
        if(StringUtils.isBlank(dto.getUser())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "用户"));
        }
        if(StringUtils.isBlank(dto.getOutSiteAction())){
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "出站类型"));
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
        //扫描条码
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
        }
        // 如果sn未进站 则不允许出站
        this.verifySnInSite(tenantId, dto.getMaterialLotCode());
        //用户存在
        Long userId = itfSingleIfaceMapper.getUserIdByLoginName(dto.getUser());
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
        if (StringUtils.isNotBlank(dto.getContainerCode())) {
            // 容器编码有值 则校验存在性
            List<MtContainer> containerList = mtContainerRepository.selectByCondition(Condition.builder(MtContainer.class).select(MtContainer.FIELD_CONTAINER_ID, MtContainer.FIELD_CONTAINER_CODE).andWhere(Sqls.custom()
                    .andEqualTo(MtContainer.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtContainer.FIELD_CONTAINER_CODE, dto.getContainerCode())).build());
            if (CollectionUtils.isEmpty(containerList)) {
                throw new MtException("WMS_CONTAINER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_CONTAINER_001", "HME", dto.getContainerCode()));
            }
            // 判断这个工位是否可以绑定容器
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
                // 该条码未进站,请检查!
                throw new MtException("HME_EQUIPMENT_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_019", "HME"));
            }
        }
    }
}