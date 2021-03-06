package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.entity.WmsMaterialSubstituteRel;
import com.ruike.wms.domain.repository.WmsMaterialLotFrozenRepository;
import com.ruike.wms.domain.repository.WmsMaterialSubstituteRelRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.PageUtil;
import io.tarzan.common.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtNcIncident;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.actual.domain.entity.MtNcRecordHis;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtNcRecordMapper;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO1;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoAttrVO1;
import tarzan.order.domain.vo.MtEoAttrVO2;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;

/**
 * HmeNcDisposePlatformRepositoryImpl
 *
 * @author: chaonan.hu@hand-china.com 2020-06-30 09:44:36
 **/
@Component
@Slf4j
public class HmeNcDisposePlatformRepositoryImpl implements HmeNcDisposePlatformRepository {

    @Autowired
    private MtNcRecordRepository mtNcRecordRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;
    @Autowired
    private HmeNcDisposePlatformMapper hmeNcDisposePlatformMapper;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtNcIncidentRepository mtNcIncidentRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private HmeNcRecordAttachmentRelRepository hmeNcRecordAttachmentRelRepository;
    @Autowired
    private HmeNcComponentTempRepository hmeNcComponentTempRepository;
    @Autowired
    private HmeNcComponentSnTempRepository hmeNcComponentSnTempRepository;
    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;
    @Autowired
    private MtNcGroupRepository mtNcGroupRepository;
    @Autowired
    private MtNcRecordMapper mtNcRecordMapper;
    @Autowired
    private MtNcGroupRepository mtNcGroupRepo;
    @Autowired
    private MtNcRecordHisRepository mtNcRecordHisRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtEoActualRepository mtEoActualRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeNcRecordAttrRepository hmeNcRecordAttrRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private HmeNcCheckMapper hmeNcCheckMapper;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private WmsMaterialLotFrozenRepository wmsMaterialLotFrozenRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtPfepManufacturingRepository mtPfepManufacturingRepository;
    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;
    @Autowired
    private MtEoBomRepository mtEoBomRepository;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;
    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;
    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;
    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;
    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    @Autowired
    private WmsMaterialSubstituteRelRepository wmsMaterialSubstituteRelRepository;
    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;
    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private HmeLoadJobMapper hmeLoadJobMapper;
    @Autowired
    private HmeLoadJobObjectMapper hmeLoadJobObjectMapper;
    @Autowired
    private HmeSelectionDetailsMapper hmeSelectionDetailsMapper;
    @Autowired
    private HmeSelectionDetailsRepository hmeSelectionDetailsRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HmeEoJobSnReWorkMapper hmeEoJobSnReWorkMapper;

    @Override
    @ProcessLovValue
    public HmeNcDisposePlatformDTO7 ncRecordQuery(Long tenantId, HmeNcDisposePlatformDTO dto, PageRequest pageRequest) {
        //???????????????
        MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.select(new HmeEoJobSn() {{
            setTenantId(tenantId);
            setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        }});
        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            throw new MtException("HME_NC_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0008", "HME"));
        }
        HmeNcDisposePlatformDTO7 hmeNcDisposePlatformDTO7 = new HmeNcDisposePlatformDTO7();
        List<HmeNcDisposePlatformDTO2> resultList = new ArrayList<>();

        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(dto.getWorkcellId());
        }});
        String siteId = mtModOrganizationRels.get(0).getTopSiteId();
        //??????????????????????????????????????????????????????
        String shiftCode = null;
        Date shiftStartTime = null;
        MtModOrganizationVO2 mtModOrganizationVO = new MtModOrganizationVO2();
        mtModOrganizationVO.setTopSiteId(siteId);
        mtModOrganizationVO.setOrganizationType("WORKCELL");
        mtModOrganizationVO.setOrganizationId(dto.getProcessId());
        mtModOrganizationVO.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO.setQueryType("TOP");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS2 = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO);
        if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS2)) {
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, mtModOrganizationItemVOS2.get(0).getOrganizationId());
            if (mtWkcShiftVO3 != null) {
                MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(mtWkcShiftVO3.getWkcShiftId());
                shiftCode = mtWkcShift.getShiftCode();
                shiftStartTime = mtWkcShift.getShiftStartTime();
            }
        }
        if (StringUtils.isEmpty(shiftCode)) {
            hmeNcDisposePlatformDTO7.setShiftCode("");
        } else {
            hmeNcDisposePlatformDTO7.setShiftCode(shiftCode);
        }

        //????????????????????????????????????
//        if (shiftStartTime == null) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            calendar.set(Calendar.HOUR_OF_DAY, 0);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            shiftStartTime = calendar.getTime();
//        }
//        List<MtNcRecord> mtNcRecords = mtNcRecordMapper.mtNcRecordQuery(tenantId, shiftStartTime, dto.getWorkcellId());
//        for (MtNcRecord mtNcRecordDb : mtNcRecords) {
//            //????????????????????????????????????
//            HmeNcDisposePlatformDTO2 hmeNcDisposePlatformDTO2 = new HmeNcDisposePlatformDTO2();
//            hmeNcDisposePlatformDTO2.setNcRecordId(mtNcRecordDb.getNcRecordId());
//            //?????? ??????ROOT_CAUSE_WORKCELL_ID??????API{parentOrganizationRelQuery}
////            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
////            mtModOrganizationVO2.setTopSiteId(mtNcRecordDb.getSiteId());
////            mtModOrganizationVO2.setOrganizationType("WORKCELL");
////            mtModOrganizationVO2.setOrganizationId(mtNcRecordDb.getRootCauseWorkcellId());
////            mtModOrganizationVO2.setParentOrganizationType("AREA");
////            mtModOrganizationVO2.setQueryType("ALL");
////            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
////            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
////                MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
////                if ("CJ".equals(mtModArea.getAreaCategory())) {
////                    hmeNcDisposePlatformDTO2.setWorkShopName(mtModArea.getAreaName());
////                    break;
////                }
////            }
//            //?????????
////            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
////            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
////            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
////                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
////                hmeNcDisposePlatformDTO2.setProdLineName(mtModProductionLine.getDescription());
////            }
//            //???????????????
////            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
////            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
////            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
////                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
////                if ("LINE".equals(mtModWorkcell.getWorkcellType())) {
////                    hmeNcDisposePlatformDTO2.setLineWorkcellName(mtModWorkcell.getWorkcellName());
////                } else if ("PROCESS".equals(mtModWorkcell.getWorkcellType())) {
////                    hmeNcDisposePlatformDTO2.setProcessName(mtModWorkcell.getWorkcellName());
////                }
////            }
////            //?????? ??????ROOT_CAUSE_WORKCELL_ID???WORKCELL_TYPE = STATION?????? mt_mod_workcell??????WORKCELL_NAME
////            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtNcRecordDb.getWorkcellId());
////            hmeNcDisposePlatformDTO2.setWorkcellName(mtModWorkcell.getWorkcellName());
//            //?????????
//            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, mtNcRecordDb.getCreatedBy());
//            hmeNcDisposePlatformDTO2.setUserName(mtUserInfo.getRealName());
//            //??????
//            hmeNcDisposePlatformDTO2.setUserCode(mtUserInfo.getLoginName());
//            //???????????? MATERIAL_ID??????mt_material??????material_code
//            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtNcRecordDb.getMaterialId());
//            hmeNcDisposePlatformDTO2.setMaterialCode(mtMaterial.getMaterialCode());
//            //????????????
//            hmeNcDisposePlatformDTO2.setMaterialName(mtMaterial.getMaterialName());
//            //????????? MATERIAL_LOT_ID??????mt_material_lot??????material_lot_code?????????????????????material_Lot_id
//            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecordDb.getMaterialLotId());
//            if (mtMaterialLot != null && StringUtils.isNotBlank(mtMaterialLot.getMaterialLotCode())) {
//                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
//            } else {
//                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtNcRecordDb.getMaterialLotId());
//            }
//            //????????? ??????eo_id?????????mt_eo?????????work_order_id?????????work_order_id??????mt_work_order??????WORK_ORDER_NUM
//            MtEo mtEo = mtEoRepository.selectByPrimaryKey(mtNcRecordDb.getEoId());
//            if (mtEo != null && StringUtils.isNotBlank(mtEo.getWorkOrderId())) {
//                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
//                if (mtWorkOrder != null) {
//                    hmeNcDisposePlatformDTO2.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
//                }
//            }
//            //???????????? ????????????
//            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, hmeNcDisposePlatformDTO2.getNcRecordId());
//            List<String> ncCodeIdList = new ArrayList<>();
//            List<String> ncCodeList = new ArrayList<>();
//            List<String> ncReasonList = new ArrayList<>();
//            for (MtNcRecord mtNcRecord : mtNcRecordList) {
//                if (StringUtils.isNotEmpty(mtNcRecord.getNcCodeId())) {
//                    //??????NC_CODE_ID?????????mt_nc_code??????nc_code
//                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
//                    ncCodeIdList.add(mtNcCode.getNcCodeId());
//                    ncCodeList.add(mtNcCode.getNcCode());
//                    ncReasonList.add(mtNcCode.getDescription());
//                }
//            }
//            hmeNcDisposePlatformDTO2.setNcCodeIdList(ncCodeIdList);
//            hmeNcDisposePlatformDTO2.setNcCodeList(ncCodeList);
//            hmeNcDisposePlatformDTO2.setNcReasonList(ncReasonList);
//            //???????????????  ????????????
//            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecordDb.getNcCodeId());
//            if (mtNcGroup != null) {
//                hmeNcDisposePlatformDTO2.setNcGroupDesc(mtNcGroup.getDescription());
//                hmeNcDisposePlatformDTO2.setNcType(mtNcGroup.getComponentRequired());
//                String meaning = lovAdapter.queryLovMeaning("HME.NC_TYPE", tenantId, mtNcGroup.getComponentRequired());
//                hmeNcDisposePlatformDTO2.setNcTypeMeaning(meaning);
//            }
//            //????????????
//            MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(mtNcRecordDb.getNcIncidentId());
//            hmeNcDisposePlatformDTO2.setNcNumber(mtNcIncident.getIncidentNumber());
//            //????????????
//            hmeNcDisposePlatformDTO2.setDateTime(mtNcRecordDb.getDateTime());
//            //????????????
//            if (StringUtils.isNotEmpty(mtNcRecordDb.getRootCauseWorkcellId())) {
//                MtModWorkcell mtModWorkcell2 = mtModWorkcellRepository.selectByPrimaryKey(mtNcRecordDb.getRootCauseWorkcellId());
//                hmeNcDisposePlatformDTO2.setResponseWorkcellName(mtModWorkcell2.getWorkcellName());
//            }
//            //?????????
//            MtUserInfo mtUserInfo2 = mtUserRepository.userPropertyGet(tenantId, mtNcRecordDb.getUserId());
//            hmeNcDisposePlatformDTO2.setResponseUser(mtUserInfo2.getRealName());
//            //??????
//            hmeNcDisposePlatformDTO2.setStatus(mtNcRecordDb.getNcStatus());
//            MtGenStatus statusMeaning = mtGenStatusRepository.getGenStatus(tenantId, "NC_RECORD", "NC_RECORD_STATUS", mtNcRecordDb.getNcStatus());
//            hmeNcDisposePlatformDTO2.setStatusMeaning(statusMeaning.getDescription());
//            //????????????
//            HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrRepository.selectOne(new HmeNcRecordAttr() {{
//                setTenantId(tenantId);
//                setParentRecordId(mtNcRecordDb.getNcRecordId());
//            }});
//            if (hmeNcRecordAttr != null) {
//                hmeNcDisposePlatformDTO2.setDisposeMethod(hmeNcRecordAttr.getProcessMethod());
//                String disposeMethodMeaning = lovAdapter.queryLovMeaning("HME.NC_PROCESS_METHOD", tenantId, hmeNcRecordAttr.getProcessMethod());
//                hmeNcDisposePlatformDTO2.setDisposeMethodMeaning(disposeMethodMeaning);
//            }
//            //2020-09-28 add by chaonan.hu for lu.bai ???????????????????????????????????????
//            if (StringUtils.isNotEmpty(mtNcRecordDb.getComponentMaterialId())) {
//                MtMaterial mtMaterial2 = mtMaterialRepository.selectByPrimaryKey(mtNcRecordDb.getComponentMaterialId());
//                hmeNcDisposePlatformDTO2.setScrapMaterialId(mtMaterial2.getMaterialId());
//                hmeNcDisposePlatformDTO2.setScrapMaterialCode(mtMaterial2.getMaterialCode());
//                hmeNcDisposePlatformDTO2.setScrapMaterialName(mtMaterial2.getMaterialName());
//                hmeNcDisposePlatformDTO2.setScrapQty(mtNcRecordDb.getQty() == null ? BigDecimal.ZERO : new BigDecimal(mtNcRecordDb.getQty()));
//            }
//            resultList.add(hmeNcDisposePlatformDTO2);
//        }
        hmeNcDisposePlatformDTO7.setHmeNcDisposePlatformDTO2List(resultList);

        //???????????????????????????????????????
        HmeNcDisposePlatformDTO9 processNcCodeTypes = this.getProcessNcCodeTypes(tenantId, dto.getProcessId(), NO);
        hmeNcDisposePlatformDTO7.setProcessNcCodeTypes(processNcCodeTypes);
        //???????????????????????????????????????
        HmeNcDisposePlatformDTO9 materialNcCodeTypes = this.getProcessNcCodeTypes(tenantId, dto.getProcessId(), YES);
        hmeNcDisposePlatformDTO7.setMaterialNcCodeTypes(materialNcCodeTypes);
        //??????????????????
        //2020-09-27 edit by chaonan.hu for lu.bai ??????????????????
        //Page<HmeNcDisposePlatformDTO23> hmeNcDisposePlatformDTO13s = this.materialDataQuery(tenantId, siteId, dto.getWorkcellId(), dto.getMaterialLotCode(), pageRequest);
        //hmeNcDisposePlatformDTO7.setMaterialData(hmeNcDisposePlatformDTO13s);
        //2020-11-13 edit by chaonan.hu for can.wang ??????????????????
        List<HmeNcDisposePlatformVO4> hmeNcDisposePlatformVO4s = this.materialDataQuery2(tenantId, mtMaterialLot1, dto.getProcessId(), dto.getWorkcellId());
        hmeNcDisposePlatformDTO7.setMaterialData(hmeNcDisposePlatformVO4s);
        return hmeNcDisposePlatformDTO7;
    }

    @Override
    public Page<HmeNcDisposePlatformDTO3> processLov(Long tenantId, HmeNcDisposePlatformDTO4 dto, PageRequest pageRequest) {
        List<HmeNcDisposePlatformDTO3> hmeNcDisposePlatformDTO3List = new ArrayList<>();
        //??????????????????????????????
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setParentOrganizationId(dto.getProdLineId());
            setParentOrganizationType("PROD_LINE");
            setOrganizationType("WORKCELL");
        }});
        //????????????????????????
        for (MtModOrganizationRel mtModOrganizationRel : mtModOrganizationRels) {
            List<MtModOrganizationRel> mtModOrganizationRelList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                setTenantId(tenantId);
                setParentOrganizationId(mtModOrganizationRel.getOrganizationId());
                setParentOrganizationType("WORKCELL");
                setOrganizationType("WORKCELL");
            }});
            for (MtModOrganizationRel mtModOrganizationRel2 : mtModOrganizationRelList) {
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtModOrganizationRel2.getOrganizationId());
                if (mtModWorkcell != null && YES.equals(mtModWorkcell.getEnableFlag())
                        && "PROCESS".equals(mtModWorkcell.getWorkcellType())) {
                    //??????????????????
                    if (StringUtils.isNotBlank(dto.getProcessCode())
                            && !mtModWorkcell.getWorkcellCode().contains(dto.getProcessCode())) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(dto.getProcessName())
                            && !mtModWorkcell.getWorkcellName().contains(dto.getProcessName())) {
                        continue;
                    }
                    HmeNcDisposePlatformDTO3 hmeNcDisposePlatformDTO3 = new HmeNcDisposePlatformDTO3();
                    hmeNcDisposePlatformDTO3.setProcessId(mtModWorkcell.getWorkcellId());
                    hmeNcDisposePlatformDTO3.setProcessCode(mtModWorkcell.getWorkcellCode());
                    hmeNcDisposePlatformDTO3.setProcessName(mtModWorkcell.getWorkcellName());
                    hmeNcDisposePlatformDTO3List.add(hmeNcDisposePlatformDTO3);
                }
            }
        }
        //??????????????????
        List<HmeNcDisposePlatformDTO3> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmeNcDisposePlatformDTO3List);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), hmeNcDisposePlatformDTO3List.size());
    }

    @Override
    public Page<HmeNcDisposePlatformDTO6> workcellLov(Long tenantId, HmeNcDisposePlatformDTO5 dto, PageRequest pageRequest) {
        List<HmeNcDisposePlatformDTO6> hmeNcDisposePlatformDTO6List = new ArrayList<>();
        //????????????????????????
        List<MtModOrganizationRel> mtModOrganizationRelList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setParentOrganizationId(dto.getProcessId());
            setParentOrganizationType("WORKCELL");
            setOrganizationType("WORKCELL");
        }});
        for (MtModOrganizationRel mtModOrganizationRel : mtModOrganizationRelList) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtModOrganizationRel.getOrganizationId());
            if (mtModWorkcell != null && YES.equals(mtModWorkcell.getEnableFlag())
                    && "STATION".equals(mtModWorkcell.getWorkcellType())) {
                //????????????
                if (StringUtils.isNotBlank(dto.getWorkcellCode())
                        && !mtModWorkcell.getWorkcellCode().contains(dto.getWorkcellCode())) {
                    continue;
                }
                if (StringUtils.isNotBlank(dto.getWorkcellName())
                        && !mtModWorkcell.getWorkcellName().contains(dto.getWorkcellName())) {
                    continue;
                }
                HmeNcDisposePlatformDTO6 hmeNcDisposePlatformDTO6 = new HmeNcDisposePlatformDTO6();
                hmeNcDisposePlatformDTO6.setWorkcellId(mtModWorkcell.getWorkcellId());
                hmeNcDisposePlatformDTO6.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                hmeNcDisposePlatformDTO6.setWorkcellName(mtModWorkcell.getWorkcellName());
                hmeNcDisposePlatformDTO6List.add(hmeNcDisposePlatformDTO6);
            }
        }
        //??????????????????
        List<HmeNcDisposePlatformDTO6> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmeNcDisposePlatformDTO6List);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), hmeNcDisposePlatformDTO6List.size());
    }

    @Override
    public HmeNcDisposePlatformDTO9 getProcessNcCodeTypes(Long tenantId, String workcellId, String componentRequired) {
        HmeNcDisposePlatformDTO9 hmeNcDisposePlatformDto9 = new HmeNcDisposePlatformDTO9();
        List<HmeNcDisposePlatformDTO8> resultList = new ArrayList<>();
        //????????????????????????Id
        List<HmeNcDisposePlatformDTO8> processNcCodeTypes = hmeNcDisposePlatformMapper.getProcessNcCodeTypes2(workcellId, componentRequired);
        int maxSize = 7;
        if (processNcCodeTypes.size() > maxSize) {
            for (int i = 0; i < maxSize; i++) {
                resultList.add(processNcCodeTypes.get(i));
            }
        } else {
            resultList.addAll(processNcCodeTypes);
        }
        hmeNcDisposePlatformDto9.setHmeNcDisposePlatformDTO8List(resultList);
        hmeNcDisposePlatformDto9.setProcessNcTypeSize(Long.parseLong(processNcCodeTypes.size() + ""));
        return hmeNcDisposePlatformDto9;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String processNcTypeCreate(Long tenantId, HmeNcDisposePlatformDTO11 dto) {
        String result = "false";
        //?????????????????????mt_material_lot??????eoid???siteId
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        //???????????????????????????????????????????????????EO????????????????????????EO By ?????? 2021-11-15
        if(StringUtils.isEmpty(mtMaterialLot.getEoId())){
            List<String> eoIds = hmeEoJobSnReWorkMapper.queryReworkEoIdsByOldCode(tenantId,dto.getMaterialLotCode());
            if (CollectionUtils.isNotEmpty(eoIds)){
                mtMaterialLot.setEoId(eoIds.get(0));
            }
        }

        //2020-09-04 edit by chaonan.hu for lu.bai ??????????????????????????? ??????????????????????????????
        //????????????
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("NC_RECORD_CREATE");
        }});
        //????????????
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, new MtNumrangeVO2() {{
            setObjectCode("NC_INCIDENT_NUM");
            setSiteId(mtMaterialLot.getSiteId());
        }});
        MtNcIncidentVO3 mtNcIncidentVO3 = mtNcIncidentRepository.ncIncidentAndHisCreate(tenantId, new MtNcIncidentVO1() {{
            setSiteId(mtMaterialLot.getSiteId());
            setIncidentNumber(mtNumrangeVO5.getNumber());
            setEventId(eventId);
        }});
        //?????????????????????
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setSiteId(mtMaterialLot.getSiteId());
        mtNcRecord.setEoId(mtMaterialLot.getEoId());
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        mtNcRecord.setUserId(userId);
        Long sequence = mtNcRecordMapper.getMaxSequence(tenantId, mtMaterialLot.getEoId());
        if (sequence == null) {
            sequence = Long.valueOf(1L);
        }
        sequence++;
        mtNcRecord.setSequence(sequence);
        mtNcRecord.setNcIncidentId(mtNcIncidentVO3.getNcIncidentId());
        mtNcRecord.setDateTime(new Date());
        if (StringUtils.isNotEmpty(dto.getComments())) {
            mtNcRecord.setComments(dto.getComments());
        }
        mtNcRecord.setNcCodeId(dto.getNcGroupId());
        mtNcRecord.setNcType("DEFECT");
        mtNcRecord.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        //???????????????????????????
        if (dto.getCurrentwWorkcellId().equals(dto.getWorkcellId())) {
            //??????????????????material_lot_Id??????hme_eo_job_sn??????????????????????????????site_in_date??????eo_step_id???operation_id?????????????????????????????????
            List<HmeEoJobSn> hmeEoJobSns = hmeEoJobSnRepository.select(new HmeEoJobSn() {{
                setTenantId(tenantId);
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
            }});
            if (CollectionUtils.isNotEmpty(hmeEoJobSns)) {
                List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSns.stream().sorted(Comparator.comparing(HmeEoJobSn::getSiteInDate)).collect(Collectors.toList());
                mtNcRecord.setEoStepActualId(hmeEoJobSnList.get(hmeEoJobSnList.size() - 1).getEoStepId());
                mtNcRecord.setRootCauseOperationId(hmeEoJobSnList.get(hmeEoJobSnList.size() - 1).getOperationId());
            }
        } else {
            //??????????????????????????????????????????
            //eoStepActualId
            mtNcRecord.setEoStepActualId(dto.getEoStepActualId());
            //rootCauseOperationId
            mtNcRecord.setRootCauseOperationId(dto.getRootCauseOperationId());
        }
        mtNcRecord.setWorkcellId(dto.getWorkcellId());
        mtNcRecord.setRootCauseWorkcellId(dto.getCurrentwWorkcellId());
        mtNcRecord.setMaterialId(mtMaterialLot.getMaterialId());
        mtNcRecord.setNcStatus(HmeConstants.NcStatus.OPEN);
        MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, dto.getNcGroupId());
        mtNcRecord.setConfirmedStatus(mtNcGroup.getConfirmRequired());
        mtNcRecord.setClosureRequiredFlag(mtNcGroup.getClosureRequired());
        mtNcRecord.setDispositionDoneFlag(NO);
        mtNcRecordRepository.insertSelective(mtNcRecord);
        //????????????
        MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
        BeanUtils.copyProperties(mtNcRecord, mtNcRecordHis);
        mtNcRecordHis.setEventId(eventId);
        mtNcRecordHisRepository.insertSelective(mtNcRecordHis);
        //????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getUuid())) {
            HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel = new HmeNcRecordAttachmentRel();
            hmeNcRecordAttachmentRel.setTenantId(tenantId);
            hmeNcRecordAttachmentRel.setNcRecordId(mtNcRecord.getNcRecordId());
            hmeNcRecordAttachmentRel.setAttachmentId(dto.getUuid());
            hmeNcRecordAttachmentRelRepository.insertSelective(hmeNcRecordAttachmentRel);
        }
        //?????????????????????
        if (CollectionUtils.isNotEmpty(dto.getNcCodeIdList())) {
            //?????????????????????
            MtNcRecordVO3 mtNcRecordVO3 = new MtNcRecordVO3();
            mtNcRecordVO3.setEoId(mtNcRecord.getEoId());
            mtNcRecordVO3.setParentNcRecordId(mtNcRecord.getNcRecordId());
            mtNcRecordVO3.setUserId(userId);
            mtNcRecordVO3.setNcIncidentId(mtNcRecord.getNcIncidentId());
            mtNcRecordVO3.setComponentMaterialId(mtNcRecord.getComponentMaterialId());
            mtNcRecordVO3.setMaterialLotId(mtNcRecord.getMaterialLotId());
            mtNcRecordVO3.setEoStepActualId(mtNcRecord.getEoStepActualId());
            mtNcRecordVO3.setRootCauseOperationId(mtNcRecord.getRootCauseOperationId());
            mtNcRecordVO3.setRootCauseWorkcellId(mtNcRecord.getRootCauseWorkcellId());
            mtNcRecordVO3.setEventId(eventId);
            for (String ncCodeId : dto.getNcCodeIdList()) {
                mtNcRecordVO3.setNcCodeId(ncCodeId);
                MtNcRecordVO8 mtNcRecordVO8 = mtNcRecordRepository.ncRecordAndHisCreate(tenantId, mtNcRecordVO3);
                if (HmeConstants.ProcessStatus.THREE.equals(dto.getFlag()) || HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                    //?????????????????????????????????????????????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                    MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                    String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CLOSE");
                    mtNcRecordVO2.setEventRequestId(eventRequestId);
                    mtNcRecordVO2.setNcRecordId(mtNcRecordVO8.getNcRecordId());
                    mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
                }
            }
        }
        // 20210525 add by sanfeng.zhang for peng.zhao ?????????????????????SN???job_id
        HmeNcRecordAttr hmeNcRecordAttr = new HmeNcRecordAttr();
        hmeNcRecordAttr.setTenantId(tenantId);
        hmeNcRecordAttr.setParentRecordId(mtNcRecord.getNcRecordId());
        // ????????????eoId????????? ???????????????job_id
        String lastJobId = hmeNcDisposePlatformMapper.queryLatestJobIdByEoAndWorkcell(tenantId, mtNcRecord.getEoId(), mtNcRecord.getWorkcellId());
        hmeNcRecordAttr.setAttribute8(lastJobId);
        if (HmeConstants.ProcessStatus.THREE.equals(dto.getFlag()) || HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
            //????????????????????????????????????????????????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
            MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
            mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CLOSE");
            mtNcRecordVO2.setEventRequestId(eventRequestId);
            mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
            //2020-09-07 add by chaonan.hu for lu.bai
            //??????????????????????????????????????????????????????
            //???????????????????????????????????????
            if (HmeConstants.ProcessStatus.THREE.equals(dto.getFlag())) {
                hmeNcRecordAttr.setProcessMethod("1");
                // 2021-02-07 add by sanfeng.zhang for can.wang ???????????? ?????????????????? attribute6??????Y
                if (YES.equals(dto.getReworkRecordFlag())) {
                    hmeNcRecordAttr.setAttribute6(YES);
                }
            } else if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                hmeNcRecordAttr.setProcessMethod("3");
            }
            //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban ???????????????????????????????????????
            materialLotUnBindingContainer(tenantId, mtMaterialLot.getMaterialLotId());
        }
        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
            //2020-09-09 add by chaonan.hu for lu.bai ???????????????????????????????????????????????????????????????
            updateNoSiteOutData(tenantId, mtMaterialLot.getMaterialLotId(), userId, dto.getFlag());
            //??????API{eoScrap} ??????
            MtEoActualVO6 mtEoActualVO6 = new MtEoActualVO6();
            mtEoActualVO6.setEoId(mtMaterialLot.getEoId());
            mtEoActualVO6.setTrxScrappedQty(mtMaterialLot.getPrimaryUomQty());
            mtEoActualVO6.setParentEventId(eventId);
            mtEoActualRepository.eoScrap(tenantId, mtEoActualVO6);
            //??????API {materialLotUpdate} ????????????
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotVO2.setEnableFlag(NO);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            result = "true";
        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag()) ||
                HmeConstants.ProcessStatus.THREE.equals(dto.getFlag())) {
            //????????????/??????????????????
            // ???????????? ?????????????????? ??????????????????????????? ??????????????????
            if (!HmeConstants.ProcessStatus.THREE.equals(dto.getFlag()) || !YES.equals(dto.getReworkRecordFlag())) {
                //???????????????????????????
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.NG);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            }

            if (HmeConstants.ProcessStatus.THREE.equals(dto.getFlag())) {
                //2020-08-25 add by chaonan.hu for lu.bai ???????????????????????????????????????????????????????????????
                //2020-11-12 edit by chaonan.hu for lu.bai ????????????????????????????????????????????????????????????
                updateNoSiteOutData(tenantId, mtMaterialLot.getMaterialLotId(), userId, dto.getFlag());

                //????????????  ????????????????????????
                //2021-02-07 add by sanfeng.zhang for can.wang ???????????? ?????????????????? ????????????????????????
                if (!YES.equals(dto.getReworkRecordFlag())) {
                    MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
                    mtExtendVO10.setKeyId(mtMaterialLot.getMaterialLotId());
                    mtExtendVO10.setEventId(eventId);
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName("REWORK_FLAG");
                    mtExtendVO5.setAttrValue(YES);
                    mtExtendVO10.setAttrs(Collections.singletonList(mtExtendVO5));
                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
                }
            }
            result = "true";
        }
        hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);
        return mtNcRecord.getNcRecordId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<HmeNcDisposePlatformDTO23> materialDataQuery(Long tenantId, String siteId, String workcellId, String materialLotCode, PageRequest pageRequest) {
        List<HmeNcDisposePlatformDTO23> resultList = new ArrayList<>();
        //????????????+??????+?????????+???????????????????????????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        List<HmeNcComponentTemp> hmeNcComponentTemps = hmeNcComponentTempRepository.select(new HmeNcComponentTemp() {{
            setTenantId(tenantId);
            setSiteId(siteId);
            setWorkcellId(workcellId);
            setSn(materialLotCode);
            setUserId(userId.toString());
        }});
        if (CollectionUtils.isNotEmpty(hmeNcComponentTemps)) {
            for (HmeNcComponentTemp hmeNcComponentTemp : hmeNcComponentTemps) {
                List<HmeNcComponentSnTemp> hmeNcComponentSnTemps = hmeNcComponentSnTempRepository.select(new HmeNcComponentSnTemp() {{
                    setTenantId(tenantId);
                    setNcComponentTempId(hmeNcComponentTemp.getNcComponentTempId());
                }});
                hmeNcComponentSnTempRepository.batchDeleteByPrimaryKey(hmeNcComponentSnTemps);
            }
            hmeNcComponentTempRepository.batchDeleteByPrimaryKey(hmeNcComponentTemps);
        }
        //???????????????????????????
        List<String> routerStepList = hmeNcDisposePlatformMapper.getRouterStep(tenantId, materialLotCode);

        //????????????????????????
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(workcellId);
        }});
        //???????????????????????????????????????
        if (CollectionUtils.isNotEmpty(mtModOrganizationRels) && StringUtils.isNotBlank(mtModOrganizationRels.get(0).getParentOrganizationId())) {
            List<MtOperationWkcDispatchRel> mtOperationWkcDispatchRels = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
                setTenantId(tenantId);
                setWorkcellId(mtModOrganizationRels.get(0).getParentOrganizationId());
            }});
            if (CollectionUtils.isNotEmpty(routerStepList) && CollectionUtils.isNotEmpty(mtOperationWkcDispatchRels)) {
                for (MtOperationWkcDispatchRel MtOperationWkcDispatchRel : mtOperationWkcDispatchRels) {
                    for (String routerStepId : routerStepList) {
                        List<HmeNcDisposePlatformDTO23> materialData = hmeNcDisposePlatformMapper.getMaterialData(tenantId, MtOperationWkcDispatchRel.getOperationId(), routerStepId);
                        if (CollectionUtils.isNotEmpty(materialData)) {
                            resultList.addAll(materialData);
                        }
                    }
                }
            }
        }
        Long number = 1L;
        List<HmeNcDisposePlatformDTO24> materialLotList = new ArrayList<>();
        for (HmeNcDisposePlatformDTO23 HmeNcDisposePlatformDTO13 : resultList) {
            HmeNcDisposePlatformDTO13.setMaterialLotList(materialLotList);
            HmeNcDisposePlatformDTO13.setNumber(number);
            number++;
            //????????????????????????????????????????????????Hme_nc_component_temp???
            HmeNcComponentTemp hmeNcComponentTemp = new HmeNcComponentTemp();
            hmeNcComponentTemp.setTenantId(tenantId);
            hmeNcComponentTemp.setSiteId(siteId);
            hmeNcComponentTemp.setSn(materialLotCode);
            hmeNcComponentTemp.setWorkcellId(workcellId);
            hmeNcComponentTemp.setUserId(userId.toString());
            hmeNcComponentTemp.setMaterialId(HmeNcDisposePlatformDTO13.getMaterialId());
            hmeNcComponentTempRepository.insertSelective(hmeNcComponentTemp);
            HmeNcDisposePlatformDTO13.setNcComponentTempId(hmeNcComponentTemp.getNcComponentTempId());
        }
        //??????????????????
        List<HmeNcDisposePlatformDTO23> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
    }

    @Override
    public Page<HmeNcDisposePlatformDTO23> materialDataPageQuery(Long tenantId, HmeNcDisposePlatformDTO dto, PageRequest pageRequest) {
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(dto.getWorkcellId());
        }});
        String siteId = mtModOrganizationRels.get(0).getTopSiteId();

        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? 1L : curUser.getUserId();

        //?????????????????????
        List<HmeNcDisposePlatformDTO13> hmeNcDisposePlatformDTO13List = hmeNcDisposePlatformMapper.materialDataPageQuery(tenantId, siteId, userId.toString(), dto);
        //???????????????????????????
        Map<String, List<HmeNcDisposePlatformDTO13>> materialMap = hmeNcDisposePlatformDTO13List.stream().collect(Collectors.groupingBy(HmeNcDisposePlatformDTO13::getMaterialId));
        Long number = 1L;
        List<HmeNcDisposePlatformDTO23> hmeNcDisposePlatformDTO23List = new ArrayList<>();
        for (Map.Entry<String, List<HmeNcDisposePlatformDTO13>> entry : materialMap.entrySet()) {
            String key = entry.getKey();
            List<HmeNcDisposePlatformDTO13> value = entry.getValue();
            HmeNcDisposePlatformDTO23 hmeNcDisposePlatformDTO23 = new HmeNcDisposePlatformDTO23();
            hmeNcDisposePlatformDTO23.setNumber(number);
            number++;
            hmeNcDisposePlatformDTO23.setMaterialId(key);
            List<HmeNcDisposePlatformDTO24> hmeNcDisposePlatformDTO24List = new ArrayList<>();
            HmeNcDisposePlatformDTO24 hmeNcDisposePlatformDTO24 = null;
            for (HmeNcDisposePlatformDTO13 hmeNcDisposePlatformDTO13 : value) {
                if (StringUtils.isEmpty(hmeNcDisposePlatformDTO23.getMaterialCode())) {
                    hmeNcDisposePlatformDTO23.setMaterialCode(hmeNcDisposePlatformDTO13.getMaterialCode());
                }
                if (StringUtils.isEmpty(hmeNcDisposePlatformDTO23.getMaterialName())) {
                    hmeNcDisposePlatformDTO23.setMaterialName(hmeNcDisposePlatformDTO13.getMaterialName());
                }
                if (StringUtils.isEmpty(hmeNcDisposePlatformDTO23.getNcComponentTempId())) {
                    hmeNcDisposePlatformDTO23.setNcComponentTempId(hmeNcDisposePlatformDTO13.getNcComponentTempId());
                }
                if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO13.getMaterialLotId())) {
                    hmeNcDisposePlatformDTO24 = new HmeNcDisposePlatformDTO24();
                    hmeNcDisposePlatformDTO24.setMaterialLotCode(hmeNcDisposePlatformDTO13.getMaterialLotCode());
                    hmeNcDisposePlatformDTO24.setMaterialLotId(hmeNcDisposePlatformDTO13.getMaterialLotId());
                    //2020-09-10 add by choanan.hu ?????????????????????????????????
                    hmeNcDisposePlatformDTO24.setScrapQty(new BigDecimal(hmeNcDisposePlatformDTO13.getScrapQtyStr()));
                    hmeNcDisposePlatformDTO24List.add(hmeNcDisposePlatformDTO24);
                }
            }
            hmeNcDisposePlatformDTO23.setMaterialLotList(hmeNcDisposePlatformDTO24List);
            hmeNcDisposePlatformDTO23List.add(hmeNcDisposePlatformDTO23);
        }
        List<HmeNcDisposePlatformDTO23> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmeNcDisposePlatformDTO23List);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), hmeNcDisposePlatformDTO23List.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialDataDelete(Long tenantId, List<HmeNcDisposePlatformDTO22> dtoList) {
        for (HmeNcDisposePlatformDTO22 dto : dtoList) {
            //?????????????????????
            List<HmeNcComponentSnTemp> hmeNcComponentSnTemps = hmeNcComponentSnTempRepository.select(new HmeNcComponentSnTemp() {{
                setTenantId(tenantId);
                setNcComponentTempId(dto.getNcComponentTempId());
                setMaterialLotId(dto.getMaterialLotId());
            }});
            if (CollectionUtils.isNotEmpty(hmeNcComponentSnTemps)) {
                hmeNcComponentSnTempRepository.batchDeleteByPrimaryKey(hmeNcComponentSnTemps);
            }
        }
        return "true";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeNcDisposePlatformDTO18 materialNcTypeCreate(Long tenantId, HmeNcDisposePlatformDTO18 dto) {
        //?????????????????????mt_material_lot??????eoid???siteId
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getSnNumber());
        }});
        //????????????
//        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_WIP_RETURN");
        String createEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CREATE");
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("NC_WIP_RETURN");
        mtEventCreateVO.setEventRequestId(createEventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        mtEventCreateVO.setEventTypeCode("NC_RECORD_CREATE");
        String transferEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        mtEventCreateVO.setEventTypeCode("NC_RECORD_CLOSE");
        String transferCloseEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setSiteId(mtMaterialLot.getSiteId());
        mtNcRecord.setEoId(mtMaterialLot.getEoId());
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        mtNcRecord.setUserId(userId);
        Long sequence = mtNcRecordMapper.getMaxSequence(tenantId, mtMaterialLot.getEoId());
        if (sequence == null) {
            sequence = Long.valueOf(1L);
        }
        sequence++;
        mtNcRecord.setSequence(sequence);
        mtNcRecord.setDateTime(new Date());
        if (StringUtils.isNotEmpty(dto.getComments())) {
            mtNcRecord.setComments(dto.getComments());
        }
        mtNcRecord.setNcCodeId(dto.getNcCodeId());
        mtNcRecord.setNcType("DEFECT");
        mtNcRecord.setWorkcellId(dto.getWorkcellId());
        //V20211223 modify by penglin.sui for peng.zhao ????????????????????????
        mtNcRecord.setRootCauseWorkcellId(dto.getWorkcellId());
        mtNcRecord.setMaterialId(mtMaterialLot.getMaterialId());
        mtNcRecord.setNcStatus(HmeConstants.NcStatus.OPEN);
        MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, dto.getNcCodeId());
        mtNcRecord.setConfirmedStatus(mtNcGroup.getConfirmRequired());
        mtNcRecord.setClosureRequiredFlag(mtNcGroup.getClosureRequired());
        mtNcRecord.setDispositionDoneFlag(NO);
        //2020-09-20 add by chaonan.hu fou lu.bai ??????RootCauseOperationId???????????????
        //??????material_lot_Id??????hme_eo_job_sn??????????????????????????????site_in_date??????eo_step_id???operation_id?????????????????????????????????
        List<HmeEoJobSn> hmeEoJobSns = hmeEoJobSnRepository.select(new HmeEoJobSn() {{
            setTenantId(tenantId);
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
        }});
        if (CollectionUtils.isNotEmpty(hmeEoJobSns)) {
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSns.stream().sorted(Comparator.comparing(HmeEoJobSn::getSiteInDate)).collect(Collectors.toList());
            mtNcRecord.setOperationId(hmeEoJobSnList.get(hmeEoJobSnList.size() - 1).getOperationId());
        }
        List<HmeLoadJob> hmeLoadJobList = new ArrayList<>();
        List<HmeLoadJobObject> hmeLoadJobObjectList = new ArrayList<>();
        List<HmeMaterialLotNcLoad> ncLoadList = new ArrayList<>();
        List<HmeMaterialLotLoad> materialLotLoadList = new ArrayList<>();
        //??????????????????mt_operation_wkc_dispatch_rel??????operation_id
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getProcessId());
        }});
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
            //????????????????????????{???????????????????????????,?????????}
            throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0044", "HME"));
        } else if (operationWkcDispatchRelList.size() > 1) {
            //???????????????????????????{??????????????????????????????,?????????}
            throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0045", "HME"));
        }
        String operationId = operationWkcDispatchRelList.get(0).getOperationId();
        // ??????????????????eo
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        Date now = CommonUtils.currentTimeGet();
        //??????LOV??????
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<String> loadSequenceList = new ArrayList<>();
        // ????????????jobId
        String lastJobId = hmeNcDisposePlatformMapper.queryLatestJobIdByEoAndWorkcell(tenantId, mtNcRecord.getEoId(), mtNcRecord.getWorkcellId());
        for (HmeNcDisposePlatformVO4 material : dto.getMaterilalList()) {
            if (CollectionUtils.isEmpty(material.getDetailList()) && Objects.nonNull(material.getApplyQty())) {
                //????????? 1???????????????????????????
                HmeNcDisposePlatformDTO32 hmeNcDisposePlatformDTO32 = new HmeNcDisposePlatformDTO32();
                hmeNcDisposePlatformDTO32.setMaterialId(material.getMaterialId());
                hmeNcDisposePlatformDTO32.setSiteId(mtMaterialLot.getSiteId());
                hmeNcDisposePlatformDTO32.setMaterialLotId(material.getMaterialLotId());
                hmeNcDisposePlatformDTO32.setApplyQty(material.getApplyQty());
                hmeNcDisposePlatformDTO32.setUuid(dto.getUuid());
                hmeNcDisposePlatformDTO32.setChildNcCodeIdList(dto.getChildNcCodeIdList());
                hmeNcDisposePlatformDTO32.setUserId(userId);
                hmeNcDisposePlatformDTO32.setFlag(dto.getFlag());
                hmeNcDisposePlatformDTO32.setEventId(eventId);
                hmeNcDisposePlatformDTO32.setEventRequestId(createEventRequestId);
                if (CollectionUtils.isEmpty(material.getMaterialLotLoadIdList())) {
                    mtNcRecord = ncRecordCreate(tenantId, mtNcRecord, hmeNcDisposePlatformDTO32);
                }
                HmeNcRecordAttr hmeNcRecordAttr = new HmeNcRecordAttr();
                hmeNcRecordAttr.setTenantId(tenantId);
                hmeNcRecordAttr.setParentRecordId(mtNcRecord.getNcRecordId());
                hmeNcRecordAttr.setAttribute8(lastJobId);
                if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                    // ???????????????????????? ?????????????????????
                    if (!YES.equals(material.getNoReturnMaterialFlag())) {
                        //2???????????????
                        HmeNcDisposePlatformDTO33 hmeNcDisposePlatformDTO33 = new HmeNcDisposePlatformDTO33();
                        hmeNcDisposePlatformDTO33.setEventId(transferEventId);
                        hmeNcDisposePlatformDTO33.setScrapTransactionId(transferCloseEventId);
                        hmeNcDisposePlatformDTO33.setSnNumber(dto.getSnNumber());
                        hmeNcDisposePlatformDTO33.setProcessId(dto.getProcessId());
                        noScrap(tenantId, hmeNcDisposePlatformDTO33, material);
                        //3?????????API{materiallotconsume}??????????????????
                        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                        mtMaterialLotVO1.setMaterialLotId(material.getMaterialLotId());
                        mtMaterialLotVO1.setTrxPrimaryUomQty(material.getApplyQty().doubleValue());
                        mtMaterialLotVO1.setParentEventId(eventId);
                        mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);

                        //4?????????API{componentAssembleProcess}??????????????????????????????
                        MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
                        //?????????????????????????????????????????????eo?????????????????????
                        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
                        if (StringUtils.isNotEmpty(bomComponentId)) {
                            mtAssembleProcessActualVO5.setAssembleExcessFlag(NO);
                            mtAssembleProcessActualVO5.setBomComponentId(bomComponentId);
                        } else {
                            mtAssembleProcessActualVO5.setAssembleExcessFlag(YES);
                        }
                        //2020-11-27 edit by chaonan.hu for can.wang ??????OperationId???RouterStepId????????????
                        //2020-12-25 edit by chaonan.hu for can.wang OperationId???RouterStepId??????????????????????????????
                        //??????eoId??????router_step_id
                        List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, mtEo.getEoId());
                        //????????????routerStepId???operationId?????????mt_router_operation???ROUTER_STEP_ID
                        List<MtRouterOperation> mtRouterOperationList = new ArrayList<>();
                        MtRouterOperation mtRouterOperation = new MtRouterOperation();
                        mtRouterOperation.setTenantId(tenantId);
                        mtRouterOperation.setOperationId(operationId);
                        for (String routerStepId2 : routerStepIdList) {
                            mtRouterOperation.setRouterStepId(routerStepId2);
                            MtRouterOperation mtRouterOperationDb = mtRouterOperationRepository.selectOne(mtRouterOperation);
                            if (Objects.nonNull(mtRouterOperationDb)) {
                                mtRouterOperationList.add(mtRouterOperationDb);
                            }
                        }
                        if (CollectionUtils.isEmpty(mtRouterOperationList)) {
                            //????????????????????????{?????????????????????????????????????????????}
                            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_NC_0046", "HME"));
                        } else if (mtRouterOperationList.size() > 1) {
                            //???????????????????????????{????????????????????????????????????????????????}
                            throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_NC_0047", "HME"));
                        }
                        mtAssembleProcessActualVO5.setRouterStepId(mtRouterOperationList.get(0).getRouterStepId());
                        mtAssembleProcessActualVO5.setOperationId(mtRouterOperationList.get(0).getOperationId());
                        mtAssembleProcessActualVO5.setAssembleRouterType("NC");
                        mtAssembleProcessActualVO5.setEoId(mtEo.getEoId());
                        mtAssembleProcessActualVO5.setEventRequestId(createEventRequestId);
                        mtAssembleProcessActualVO5.setMaterialId(material.getMaterialId());
                        mtAssembleProcessActualVO5.setParentEventId(eventId);
                        mtAssembleProcessActualVO5.setTrxAssembleQty(material.getApplyQty().doubleValue());
                        mtAssembleProcessActualVO5.setWorkcellId(dto.getWorkcellId());
                        mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, mtAssembleProcessActualVO5);
                        //5?????????API{eocomponentscrap}??????????????????????????????
                        HmeNcDisposePlatformVO5 hmeNcDisposePlatformVO5 = new HmeNcDisposePlatformVO5();
                        hmeNcDisposePlatformVO5.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformVO5.setApplyQty(material.getApplyQty());
                        HmeNcDisposePlatformDTO31 hmeNcDisposePlatformDTO31 = new HmeNcDisposePlatformDTO31();
                        hmeNcDisposePlatformDTO31.setSnNumber(dto.getSnNumber());
                        hmeNcDisposePlatformDTO31.setWorkcerllId(dto.getWorkcellId());
                        hmeNcDisposePlatformDTO31.setProdLineId(dto.getProdLineId());
                        hmeNcDisposePlatformDTO31.setUserId(userId);
                        hmeNcDisposePlatformDTO31.setEventId(eventId);
                        hmeNcDisposePlatformDTO31.setEventRequestId(createEventRequestId);
                        hmeNcDisposePlatformDTO31.setCreateEventRequestId(createEventRequestId);
                        hmeNcDisposePlatformDTO31.setProcessId(dto.getProcessId());
                        orangeScrap(tenantId, hmeNcDisposePlatformVO5, hmeNcDisposePlatformDTO31);
                    }

                    // ????????????????????????????????????????????? ????????????
                    if (CollectionUtils.isEmpty(material.getMaterialLotLoadIdList())) {
                        //?????????????????????,?????????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                        MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                        mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
                        mtNcRecordVO2.setEventRequestId(createEventRequestId);
                        mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
                        //????????????????????????????????????????????????????????????????????????
                        hmeNcRecordAttr.setProcessMethod("3");
                        if (HmeConstants.ConstantValue.YES.equals(material.getNoReturnMaterialFlag())) {
                            hmeNcRecordAttr.setAttribute2(HmeConstants.ConstantValue.YES);
                        }
                    }
                } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag())) {
                    //???????????????????????????
                    // ???????????????????????? ????????????????????????
                    if (!YES.equals(material.getNoReturnMaterialFlag())) {
                        //2?????????API{materiallotconsume}??????????????????
                        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                        mtMaterialLotVO1.setMaterialLotId(material.getMaterialLotId());
                        mtMaterialLotVO1.setTrxPrimaryUomQty(material.getApplyQty().doubleValue());
                        mtMaterialLotVO1.setParentEventId(eventId);
                        mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                        //2020-01-07 add by chaonan.hu for can.wang ????????????API{ onhandQtyUpdateProcess }????????????????????????
                        locatorTransfer(tenantId, material.getMaterialLotId(), material.getApplyQty(), createEventRequestId);
                    } else {
                        // ?????????????????????????????????????????? ????????????
                        if (CollectionUtils.isEmpty(material.getMaterialLotLoadIdList())) {
                            hmeNcRecordAttr.setAttribute2(HmeConstants.ConstantValue.YES);
                        }
                    }
                }
                if (CollectionUtils.isEmpty(material.getMaterialLotLoadIdList())) {
                    hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);
                }
                // ?????????????????????????????? ?????????????????????????????????????????????
                if (CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList())) {
                    // ??????????????????????????????????????????????????????????????????????????????????????????????????????
                    Integer applyQty = material.getApplyQty().intValue();
                    Integer loadSize = CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList()) ? material.getMaterialLotLoadIdList().size() : 0;
                    if (loadSize.compareTo(applyQty) != 0) {
                        throw new MtException("HME_NC_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0074", "HME"));
                    }
                    // ??????????????????
                    List<String> loadJobIdList = customSequence.getNextKeys("hme_load_job_s", applyQty);
                    List<String> loadJobCidList = customSequence.getNextKeys("hme_load_job_cid_s", applyQty);
                    List<String> loadJobObjectIdList = new ArrayList<>();
                    List<String> loadJobObjectCidList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(dto.getChildNcCodeIdList())) {
                        loadJobObjectIdList = customSequence.getNextKeys("hme_load_job_object_s", applyQty * dto.getChildNcCodeIdList().size());
                        loadJobObjectCidList = customSequence.getNextKeys("hme_load_job_object_cid_s", applyQty * dto.getChildNcCodeIdList().size());
                    }
                    List<String> ncLoadIdList = customSequence.getNextKeys("hme_material_lot_nc_load_s", applyQty);
                    List<String> ncLoadCidList = customSequence.getNextKeys("hme_material_lot_nc_load_cid_s", applyQty);
                    // ??????????????????
                    List<HmeMaterialLotLoad> loadList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList())) {
                        loadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class).andWhere(Sqls.custom()
                                .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID, material.getMaterialLotLoadIdList())).build());
                    }
                    for (int i = 0; i < applyQty; i++) {
                        HmeMaterialLotLoad materialLotLoad = loadList.get(i);
                        // ??????????????????????????????????????????????????????
                        List<String> ncRecordIdList = hmeNcDisposePlatformMapper.queryNcRecordByLoadSequence(tenantId, materialLotLoad.getLoadSequence());
                        if (CollectionUtils.isNotEmpty(ncRecordIdList)) {
                            throw new MtException("HME_NC_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_NC_0011", "HME", material.getMaterialLotCode()));
                        }

                        if (loadSequenceList.contains(materialLotLoad.getLoadSequence())) {
                            throw new MtException("HME_NC_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_NC_0075", "HME", material.getMaterialLotCode()));
                        } else {
                            loadSequenceList.add(materialLotLoad.getLoadSequence());
                        }

                        // ?????????????????? ??????????????? ?????????1
                        hmeNcDisposePlatformDTO32.setApplyQty(BigDecimal.ONE);
                        mtNcRecord = ncRecordCreate(tenantId, mtNcRecord, hmeNcDisposePlatformDTO32);
                        // ?????????????????? ??????????????? ??????????????????  ?????????????????? ??????????????????
                        HmeNcRecordAttr ncRecordAttr = new HmeNcRecordAttr();
                        ncRecordAttr.setTenantId(tenantId);
                        ncRecordAttr.setParentRecordId(mtNcRecord.getNcRecordId());
                        ncRecordAttr.setAttribute8(lastJobId);
                        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                            // ????????????
                            //?????????????????????,?????????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                            MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                            mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
                            mtNcRecordVO2.setEventRequestId(createEventRequestId);
                            mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);

                            //?????????????????????????????????????????????????????????????????????????????????
                            ncRecordAttr.setProcessMethod("3");
                            if (HmeConstants.ConstantValue.YES.equals(material.getNoReturnMaterialFlag())) {
                                ncRecordAttr.setAttribute2(HmeConstants.ConstantValue.YES);
                            }
                            ncRecordAttr.setAttribute7(materialLotLoad.getLoadSequence());
                        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag())) {
                            //????????????????????????????????????????????????
                            if (HmeConstants.ConstantValue.YES.equals(material.getNoReturnMaterialFlag())) {
                                ncRecordAttr.setAttribute2(HmeConstants.ConstantValue.YES);
                            }
                            ncRecordAttr.setAttribute7(materialLotLoad.getLoadSequence());
                        }
                        hmeNcRecordAttrRepository.insertSelective(ncRecordAttr);

                        // ???????????????????????????
                        HmeLoadJob hmeLoadJob = new HmeLoadJob();
                        hmeLoadJob.setSiteId(mtMaterialLot.getSiteId());
                        hmeLoadJob.setLoadSequence(materialLotLoad.getLoadSequence());
                        hmeLoadJob.setLoadJobType("PUMP_NC_MATERIAL_COS");
                        hmeLoadJob.setMaterialId(material.getMaterialId());
                        hmeLoadJob.setMaterialLotId(material.getMaterialLotId());
                        hmeLoadJob.setLoadRow(materialLotLoad.getLoadRow());
                        hmeLoadJob.setLoadColumn(materialLotLoad.getLoadColumn());
                        hmeLoadJob.setCosNum(1L);
                        hmeLoadJob.setHotSinkCode(materialLotLoad.getHotSinkCode());
                        hmeLoadJob.setStatus(STRING_ZERO);
                        hmeLoadJob.setOperationId(operationId);
                        hmeLoadJob.setWorkcellId(dto.getWorkcellId());
                        hmeLoadJob.setWorkOrderId(mtEo.getWorkOrderId());
                        hmeLoadJob.setWaferNum(materialLotLoad.getAttribute2());
                        hmeLoadJob.setCosType(materialLotLoad.getAttribute1());
                        hmeLoadJob.setLoadJobId(loadJobIdList.get(i));
                        hmeLoadJob.setCid(Long.valueOf(loadJobCidList.get(i)));
                        hmeLoadJob.setObjectVersionNumber(1L);
                        hmeLoadJob.setCreatedBy(userId);
                        hmeLoadJob.setCreationDate(now);
                        hmeLoadJob.setLastUpdatedBy(userId);
                        hmeLoadJob.setLastUpdateDate(now);
                        hmeLoadJob.setTenantId(tenantId);
                        hmeLoadJobList.add(hmeLoadJob);
                        // ???????????????????????????
                        if (CollectionUtils.isNotEmpty(dto.getChildNcCodeIdList())) {
                            for (String ncCodeId : dto.getChildNcCodeIdList()) {
                                HmeLoadJobObject loadJobObject = new HmeLoadJobObject();
                                loadJobObject.setLoadJobId(loadJobIdList.get(i));
                                loadJobObject.setObjectType("NC");
                                loadJobObject.setObjectId(ncCodeId);
                                loadJobObject.setLoadObjectId(loadJobObjectIdList.get(i));
                                loadJobObject.setCid(Long.valueOf(loadJobObjectCidList.get(i)));
                                loadJobObject.setObjectVersionNumber(1L);
                                loadJobObject.setCreatedBy(userId);
                                loadJobObject.setCreationDate(now);
                                loadJobObject.setLastUpdatedBy(userId);
                                loadJobObject.setLastUpdateDate(now);
                                loadJobObject.setTenantId(tenantId);
                                hmeLoadJobObjectList.add(loadJobObject);
                            }
                        }
                        // ??????????????????
                        HmeMaterialLotNcLoad ncLoad = new HmeMaterialLotNcLoad();
                        ncLoad.setLoadSequence(materialLotLoad.getLoadSequence());
                        ncLoad.setLoadNum(STRING_ONE);
                        ncLoad.setNcLoadId(ncLoadIdList.get(i));
                        ncLoad.setCid(Long.valueOf(ncLoadCidList.get(i)));
                        ncLoad.setObjectVersionNumber(1L);
                        ncLoad.setCreatedBy(userId);
                        ncLoad.setCreationDate(now);
                        ncLoad.setLastUpdatedBy(userId);
                        ncLoad.setLastUpdateDate(now);
                        ncLoad.setTenantId(tenantId);
                        ncLoadList.add(ncLoad);

                        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag()) && !StringUtils.equals(YES, material.getNoReturnMaterialFlag())) {
                            // ???????????????????????????????????? ?????????????????? ????????????
                            materialLotLoad.setMaterialLotId("");
                            materialLotLoadList.add(materialLotLoad);
                        }
                    }
                }
            } else if (CollectionUtils.isNotEmpty(material.getDetailList())) {
                //?????????
                // ????????????????????? ??????????????????
                Boolean loadFlag = false;
                for (HmeNcDisposePlatformVO5 hmeNcDisposePlatformVO5 : material.getDetailList()) {
                    if (Objects.nonNull(hmeNcDisposePlatformVO5.getApplyQty())) {
                        // ????????????????????????????????????????????? ???-> ?????????????????????????????????????????? ???-> ???????????????
                        Boolean cosMaterialAndFirstFlag = false;
                        //1???????????????????????????
                        HmeNcDisposePlatformDTO32 hmeNcDisposePlatformDTO32 = new HmeNcDisposePlatformDTO32();
                        hmeNcDisposePlatformDTO32.setMaterialId(hmeNcDisposePlatformVO5.getMaterialId());
                        hmeNcDisposePlatformDTO32.setSiteId(mtMaterialLot.getSiteId());
                        hmeNcDisposePlatformDTO32.setMaterialLotId(hmeNcDisposePlatformVO5.getMaterialLotId());
                        hmeNcDisposePlatformDTO32.setApplyQty(hmeNcDisposePlatformVO5.getApplyQty());
                        hmeNcDisposePlatformDTO32.setUuid(dto.getUuid());
                        hmeNcDisposePlatformDTO32.setChildNcCodeIdList(dto.getChildNcCodeIdList());
                        hmeNcDisposePlatformDTO32.setUserId(userId);
                        hmeNcDisposePlatformDTO32.setFlag(dto.getFlag());
                        hmeNcDisposePlatformDTO32.setEventId(eventId);
                        hmeNcDisposePlatformDTO32.setEventRequestId(createEventRequestId);
                        if (!loadFlag) {
                            List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(hmeNcDisposePlatformVO5.getMaterialLotId());
                            if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                                List<String> collect = list.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                                if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                                    loadFlag = true;
                                    cosMaterialAndFirstFlag = true;
                                    // ??????????????????????????????????????????????????????????????????????????????????????????????????????
                                    Integer applyQty = hmeNcDisposePlatformVO5.getApplyQty().intValue();
                                    Integer loadSize = CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList()) ? material.getMaterialLotLoadIdList().size() : 0;
                                    if (loadSize.compareTo(applyQty) != 0) {
                                        throw new MtException("HME_NC_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "HME_NC_0074", "HME"));
                                    }
                                    // ??????????????????
                                    List<String> loadJobIdList = customSequence.getNextKeys("hme_load_job_s", applyQty);
                                    List<String> loadJobCidList = customSequence.getNextKeys("hme_load_job_cid_s", applyQty);
                                    List<String> loadJobObjectIdList = new ArrayList<>();
                                    List<String> loadJobObjectCidList = new ArrayList<>();
                                    if (CollectionUtils.isNotEmpty(dto.getChildNcCodeIdList())) {
                                        loadJobObjectIdList = customSequence.getNextKeys("hme_load_job_object_s", applyQty * dto.getChildNcCodeIdList().size());
                                        loadJobObjectCidList = customSequence.getNextKeys("hme_load_job_object_cid_s", applyQty * dto.getChildNcCodeIdList().size());
                                    }
                                    List<String> ncLoadIdList = customSequence.getNextKeys("hme_material_lot_nc_load_s", applyQty);
                                    List<String> ncLoadCidList = customSequence.getNextKeys("hme_material_lot_nc_load_cid_s", applyQty);
                                    // ??????????????????
                                    List<HmeMaterialLotLoad> loadList = new ArrayList<>();
                                    if (CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList())) {
                                        loadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class).andWhere(Sqls.custom()
                                                .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID, material.getMaterialLotLoadIdList())).build());
                                    }
                                    for (int i = 0; i < applyQty; i++) {
                                        HmeMaterialLotLoad materialLotLoad = loadList.get(i);
                                        if (loadSequenceList.contains(materialLotLoad.getLoadSequence())) {
                                            throw new MtException("HME_NC_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                    "HME_NC_0075", "HME", hmeNcDisposePlatformVO5.getMaterialLotCode()));
                                        } else {
                                            loadSequenceList.add(materialLotLoad.getLoadSequence());
                                        }
                                        hmeNcDisposePlatformDTO32.setApplyQty(BigDecimal.ONE);
                                        mtNcRecord = ncRecordCreate(tenantId, mtNcRecord, hmeNcDisposePlatformDTO32);
                                        HmeNcRecordAttr hmeNcRecordAttr = new HmeNcRecordAttr();
                                        hmeNcRecordAttr.setTenantId(tenantId);
                                        hmeNcRecordAttr.setParentRecordId(mtNcRecord.getNcRecordId());
                                        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                                            // ????????????
                                            //?????????????????????,?????????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                                            MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                                            mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
                                            mtNcRecordVO2.setEventRequestId(createEventRequestId);
                                            mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);

                                            //?????????????????????????????????????????????????????????????????????????????????
                                            hmeNcRecordAttr.setProcessMethod("3");
                                            if (YES.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                                hmeNcRecordAttr.setAttribute2(YES);
                                            }
                                            hmeNcRecordAttr.setAttribute7(materialLotLoad.getLoadSequence());
                                            //2021-03-18 add by chaonan.hu for yongjiang.wu ????????????????????????????????????????????????????????????
                                            updateFunctionInfo(tenantId, mtEo.getEoId(), dto.getWorkcellId(), materialLotLoad.getLoadSequence());
                                        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag())) {
                                            if (YES.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                                // ??????????????????Y??? ?????????????????????????????????????????????????????????????????????
                                                //????????????????????????????????????????????????
                                                hmeNcRecordAttr.setAttribute2(YES);
                                            }
                                            hmeNcRecordAttr.setAttribute7(materialLotLoad.getLoadSequence());
                                        }
                                        hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);

                                        // ???????????????????????????
                                        HmeLoadJob hmeLoadJob = new HmeLoadJob();
                                        hmeLoadJob.setSiteId(mtMaterialLot.getSiteId());
                                        hmeLoadJob.setLoadSequence(materialLotLoad.getLoadSequence());
                                        hmeLoadJob.setLoadJobType("PUMP_NC_MATERIAL_COS");
                                        hmeLoadJob.setMaterialId(hmeNcDisposePlatformVO5.getMaterialId());
                                        hmeLoadJob.setMaterialLotId(hmeNcDisposePlatformVO5.getMaterialLotId());
                                        hmeLoadJob.setLoadRow(materialLotLoad.getLoadRow());
                                        hmeLoadJob.setLoadColumn(materialLotLoad.getLoadColumn());
                                        hmeLoadJob.setCosNum(1L);
                                        hmeLoadJob.setHotSinkCode(materialLotLoad.getHotSinkCode());
                                        hmeLoadJob.setStatus(STRING_ZERO);
                                        hmeLoadJob.setOperationId(operationId);
                                        hmeLoadJob.setWorkcellId(dto.getWorkcellId());
                                        hmeLoadJob.setWorkOrderId(mtEo.getWorkOrderId());
                                        hmeLoadJob.setWaferNum(materialLotLoad.getAttribute2());
                                        hmeLoadJob.setCosType(materialLotLoad.getAttribute1());
                                        hmeLoadJob.setLoadJobId(loadJobIdList.get(i));
                                        hmeLoadJob.setCid(Long.valueOf(loadJobCidList.get(i)));
                                        hmeLoadJob.setObjectVersionNumber(1L);
                                        hmeLoadJob.setCreatedBy(userId);
                                        hmeLoadJob.setCreationDate(now);
                                        hmeLoadJob.setLastUpdatedBy(userId);
                                        hmeLoadJob.setLastUpdateDate(now);
                                        hmeLoadJob.setTenantId(tenantId);
                                        hmeLoadJobList.add(hmeLoadJob);
                                        // ???????????????????????????
                                        if (CollectionUtils.isNotEmpty(dto.getChildNcCodeIdList())) {
                                            for (String ncCodeId : dto.getChildNcCodeIdList()) {
                                                HmeLoadJobObject loadJobObject = new HmeLoadJobObject();
                                                loadJobObject.setLoadJobId(loadJobIdList.get(i));
                                                loadJobObject.setObjectType("NC");
                                                loadJobObject.setObjectId(ncCodeId);
                                                loadJobObject.setLoadObjectId(loadJobObjectIdList.get(i));
                                                loadJobObject.setCid(Long.valueOf(loadJobObjectCidList.get(i)));
                                                loadJobObject.setObjectVersionNumber(1L);
                                                loadJobObject.setCreatedBy(userId);
                                                loadJobObject.setCreationDate(now);
                                                loadJobObject.setLastUpdatedBy(userId);
                                                loadJobObject.setLastUpdateDate(now);
                                                loadJobObject.setTenantId(tenantId);
                                                hmeLoadJobObjectList.add(loadJobObject);
                                            }
                                        }
                                        // ??????????????????
                                        HmeMaterialLotNcLoad ncLoad = new HmeMaterialLotNcLoad();
                                        ncLoad.setLoadSequence(materialLotLoad.getLoadSequence());
                                        ncLoad.setLoadNum(STRING_ONE);
                                        ncLoad.setNcLoadId(ncLoadIdList.get(i));
                                        ncLoad.setCid(Long.valueOf(ncLoadCidList.get(i)));
                                        ncLoad.setObjectVersionNumber(1L);
                                        ncLoad.setCreatedBy(userId);
                                        ncLoad.setCreationDate(now);
                                        ncLoad.setLastUpdatedBy(userId);
                                        ncLoad.setLastUpdateDate(now);
                                        ncLoad.setTenantId(tenantId);
                                        ncLoadList.add(ncLoad);
                                        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag()) && !StringUtils.equals(YES, hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                            // ???????????????????????????????????? ?????????????????? ????????????
                                            materialLotLoad.setMaterialLotId("");
                                            materialLotLoadList.add(materialLotLoad);
                                        }
                                    }
                                }
                            }
                        }
                        if (!cosMaterialAndFirstFlag) {
                            mtNcRecord = ncRecordCreate(tenantId, mtNcRecord, hmeNcDisposePlatformDTO32);
                        }

                        HmeNcDisposePlatformDTO31 hmeNcDisposePlatformDTO31 = new HmeNcDisposePlatformDTO31();
                        hmeNcDisposePlatformDTO31.setSnNumber(dto.getSnNumber());
                        hmeNcDisposePlatformDTO31.setWorkcerllId(dto.getWorkcellId());
                        hmeNcDisposePlatformDTO31.setProdLineId(dto.getProdLineId());
                        hmeNcDisposePlatformDTO31.setUserId(userId);
                        hmeNcDisposePlatformDTO31.setEventId(eventId);
                        hmeNcDisposePlatformDTO31.setEventRequestId(createEventRequestId);
                        hmeNcDisposePlatformDTO31.setCreateEventRequestId(createEventRequestId);
                        hmeNcDisposePlatformDTO31.setProcessId(dto.getProcessId());
                        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                            //?????????????????????,?????????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                            if (!cosMaterialAndFirstFlag) {
                                MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                                mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
                                mtNcRecordVO2.setEventRequestId(createEventRequestId);
                                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
                            }
                            if (NO.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                //2???2020-12-13 edit by chaonan.hu for can.wang ????????? ????????????????????????????????????
                                HmeNcDisposePlatformDTO33 hmeNcDisposePlatformDTO33 = new HmeNcDisposePlatformDTO33();
                                hmeNcDisposePlatformDTO33.setEventId(transferEventId);
                                hmeNcDisposePlatformDTO33.setScrapTransactionId(transferCloseEventId);
                                hmeNcDisposePlatformDTO33.setSnNumber(dto.getSnNumber());
                                hmeNcDisposePlatformDTO33.setProcessId(dto.getProcessId());
                                //2021-02-03 edit by chaonan.hu for can.wang ????????????????????????????????????????????????
                                //alreadyAssembleScrap(tenantId, hmeNcDisposePlatformDTO33, hmeNcDisposePlatformVO5);
                                //3?????????API{componentScrapProcess}????????????????????????
                                HmeNcDisposePlatformVO6 hmeNcDisposePlatformVO6 = orangeScrap(tenantId, hmeNcDisposePlatformVO5, hmeNcDisposePlatformDTO31);
                                hmeNcDisposePlatformVO6.setEventId(eventId);
                                hmeNcDisposePlatformVO6.setMaterialId(hmeNcDisposePlatformVO5.getMaterialId());
                                hmeNcDisposePlatformVO6.setMaterialLotId(hmeNcDisposePlatformVO5.getMaterialLotId());
                                hmeNcDisposePlatformVO6.setApplyQty(hmeNcDisposePlatformVO5.getApplyQty());
                                //4?????????????????????
                                updateAssembleRecord(tenantId, mtMaterialLot.getSiteId(), dto.getWorkcellId(), hmeNcDisposePlatformVO5);
//                                //4???????????????
//                                transactionSync(tenantId, "RETURN_MATERIAL", hmeNcDisposePlatformVO6);
//                                //5???????????????
//                                transactionSync(tenantId, "ASSEMBLE_MATERIAL", hmeNcDisposePlatformVO6);
                            }
                            //6?????????????????????????????????????????????????????????
                            if (!cosMaterialAndFirstFlag) {
                                HmeNcRecordAttr hmeNcRecordAttr = new HmeNcRecordAttr();
                                hmeNcRecordAttr.setTenantId(tenantId);
                                hmeNcRecordAttr.setParentRecordId(mtNcRecord.getNcRecordId());
                                hmeNcRecordAttr.setProcessMethod("3");
                                if (YES.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                    hmeNcRecordAttr.setAttribute2(YES);
                                }
                                hmeNcRecordAttr.setAttribute8(lastJobId);
                                hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);
                            }
                        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag())) {
                            //???????????????????????????
                            if (NO.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                //2???2020-12-16 edit by chaonan.hu for can.wang ????????????????????????
                                HmeNcDisposePlatformDTO33 hmeNcDisposePlatformDTO33 = new HmeNcDisposePlatformDTO33();
                                hmeNcDisposePlatformDTO33.setEventId(transferEventId);
                                hmeNcDisposePlatformDTO33.setScrapTransactionId(transferCloseEventId);
                                hmeNcDisposePlatformDTO33.setSnNumber(dto.getSnNumber());
                                hmeNcDisposePlatformDTO33.setProcessId(dto.getProcessId());
                                alreadyAssembleNotice(tenantId, hmeNcDisposePlatformDTO33, hmeNcDisposePlatformVO5);
                                //3?????????????????????
                                updateAssembleRecord(tenantId, mtMaterialLot.getSiteId(), dto.getWorkcellId(), hmeNcDisposePlatformVO5);
                                //4?????????API{componentAssembleCancelProcess}??????????????????????????????
                                orangeAssemble(tenantId, hmeNcDisposePlatformVO5, hmeNcDisposePlatformDTO31);
                                //2020-01-07 add by chaonan.hu for can.wang ????????????API{ onhandQtyUpdateProcess }????????????????????????
                                locatorTransfer(tenantId, hmeNcDisposePlatformVO5.getMaterialLotId(), hmeNcDisposePlatformVO5.getApplyQty(), createEventRequestId);
                            } else {
                                //5?????????????????????????????????????????????????????????
                                if (!cosMaterialAndFirstFlag) {
                                    HmeNcRecordAttr hmeNcRecordAttr = new HmeNcRecordAttr();
                                    hmeNcRecordAttr.setTenantId(tenantId);
                                    hmeNcRecordAttr.setParentRecordId(mtNcRecord.getNcRecordId());
                                    hmeNcRecordAttr.setAttribute2(YES);
                                    hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);
                                }
                            }
                        }

                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(hmeLoadJobList)) {
            List<List<HmeLoadJob>> splitSqlList = InterfaceUtils.splitSqlList(hmeLoadJobList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeLoadJob> domains : splitSqlList) {
                hmeLoadJobMapper.batchInsert(domains);
            }
        }
        if (CollectionUtils.isNotEmpty(hmeLoadJobObjectList)) {
            List<List<HmeLoadJobObject>> splitSqlList = InterfaceUtils.splitSqlList(hmeLoadJobObjectList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeLoadJobObject> domains : splitSqlList) {
                hmeLoadJobObjectMapper.batchInsert(domains);
            }
        }
        if (CollectionUtils.isNotEmpty(ncLoadList)) {
            List<List<HmeMaterialLotNcLoad>> splitSqlList = InterfaceUtils.splitSqlList(ncLoadList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotNcLoad> domains : splitSqlList) {
                hmeMaterialLotNcLoadRepository.myBatchInsert(domains);
            }
        }
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(materialLotLoadList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                hmeNcDisposePlatformMapper.batchUpdate(tenantId, userId, domains);
            }
        }

        return dto;
    }

    @Override
    public HmeNcDisposePlatformDTO26 commentsQuery(Long tenantId, HmeNcDisposePlatformDTO25 dto) {
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        String materialId = mtMaterialLot.getMaterialId();
        String rootCauseOperationId = null;
        String eoStepActualId = null;
        //???????????????????????????
        if (dto.getCurrentwWorkcellId().equals(dto.getWorkcellId())) {
            //??????????????????material_lot_Id??????hme_eo_job_sn??????????????????????????????site_in_date??????eo_step_id???operation_id?????????????????????????????????
            List<HmeEoJobSn> hmeEoJobSns = hmeEoJobSnRepository.select(new HmeEoJobSn() {{
                setTenantId(tenantId);
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
            }});
            if (CollectionUtils.isNotEmpty(hmeEoJobSns)) {
                List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSns.stream().sorted(Comparator.comparing(HmeEoJobSn::getSiteInDate)).collect(Collectors.toList());
                eoStepActualId = hmeEoJobSnList.get(hmeEoJobSnList.size() - 1).getEoStepId();
                rootCauseOperationId = hmeEoJobSnList.get(hmeEoJobSnList.size() - 1).getOperationId();
            }
        } else {
            //??????????????????????????????????????????
            //eoStepActualId
            eoStepActualId = dto.getEoStepActualId();
            //rootCauseOperationId
            rootCauseOperationId = dto.getRootCauseOperationId();
        }
        return hmeNcDisposePlatformMapper.commentsQuery(tenantId, materialId, dto.getNcGroupId(), rootCauseOperationId, eoStepActualId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialLotUnBindingContainer(Long tenantId, String materialLotId) {
        //????????????????????????????????????CURRENT_CONTAINER_ID??? TOP_CONTAINER_ID?????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
        boolean updateFlag = false;
        if (StringUtils.isNotEmpty(mtMaterialLot.getCurrentContainerId())) {
            mtMaterialLot.setCurrentContainerId("");
            updateFlag = true;
        }
        if (StringUtils.isNotEmpty(mtMaterialLot.getTopContainerId())) {
            mtMaterialLot.setTopContainerId("");
            updateFlag = true;
        }
        if (updateFlag) {
            mtMaterialLotRepository.updateByPrimaryKey(mtMaterialLot);
        }
        //???????????????mt_container_load_detail???LOAD_OBJECT_TYPE???MATERIAL_LOT???????????????
        List<MtContainerLoadDetail> mtContainerLoadDetails = mtContainerLoadDetailRepository.select(new MtContainerLoadDetail() {{
            setTenantId(tenantId);
            setLoadObjectId(materialLotId);
            setLoadObjectType("MATERIAL_LOT");
        }});
        if (CollectionUtils.isNotEmpty(mtContainerLoadDetails)) {
            mtContainerLoadDetailRepository.batchDeleteByPrimaryKey(mtContainerLoadDetails);
        }
    }

    @Override
    public List<HmeNcDisposePlatformVO4> materialDataQuery2(Long tenantId, MtMaterialLot mtMaterialLot, String processId, String workcellId) {
        List<HmeNcDisposePlatformVO4> resultList = new ArrayList<>();
        //???????????????????????????????????????
        List<HmeNcDisposePlatformVO4> ncMaterialLotList = hmeNcDisposePlatformMapper.ncMaterialLotQuery(tenantId, mtMaterialLot.getEoId(), workcellId);
        //???????????????ID??????
        List<String> ncMaterialLotIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ncMaterialLotList)) {
            ncMaterialLotList = ncMaterialLotList.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(item -> item.getMaterialLotId()))), ArrayList::new));
            ncMaterialLotIdList = ncMaterialLotList.stream().map(HmeNcDisposePlatformVO4::getMaterialLotId).collect(Collectors.toList());
            for (HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 : ncMaterialLotList) {
                //???????????????
                BigDecimal waitAuditQty = hmeNcDisposePlatformMapper.waitAuditQtyQuery(tenantId, hmeNcDisposePlatformVO4.getMaterialLotId(), mtMaterialLot.getEoId());
                hmeNcDisposePlatformVO4.setWaitAuditQty(waitAuditQty);
            }
            ncMaterialLotList = ncMaterialLotList.stream().filter(item -> item.getWaitAuditQty().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList());
        }
        //??????????????????????????????
        List<HmeNcDisposePlatformVO4> eoComponentActualAssembleList = hmeNcDisposePlatformMapper.eoComponentActualAssembleQuery(tenantId, mtMaterialLot.getEoId(), workcellId);
        List<HmeNcDisposePlatformVO4> eoComponentActualAssembleGroupList = new ArrayList<>();
        //????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(eoComponentActualAssembleList)) {
            Map<String, List<HmeNcDisposePlatformVO4>> eoComponentActualAssembleMap = eoComponentActualAssembleList.stream().collect(Collectors.groupingBy(HmeNcDisposePlatformVO4::getMaterialId));
            for (Map.Entry<String, List<HmeNcDisposePlatformVO4>> entry : eoComponentActualAssembleMap.entrySet()) {
                List<HmeNcDisposePlatformVO4> value = entry.getValue();
                HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 = value.get(0);
                BigDecimal scrapQty = value.stream().collect(CollectorsUtil.summingBigDecimal(c -> c.getScrapQty()));
                BigDecimal releaseQty = value.stream().collect(CollectorsUtil.summingBigDecimal(c -> c.getReleaseQty()));
                hmeNcDisposePlatformVO4.setScrapQty(scrapQty);
                hmeNcDisposePlatformVO4.setReleaseQty(releaseQty);
                eoComponentActualAssembleGroupList.add(hmeNcDisposePlatformVO4);
            }
            for (HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 : eoComponentActualAssembleGroupList) {
                //???????????????
                BigDecimal waitAuditQty = hmeNcDisposePlatformMapper.waitAuditQtyReleaseQuery(tenantId, hmeNcDisposePlatformVO4.getMaterialId(), mtMaterialLot.getEoId(), ncMaterialLotIdList);
                hmeNcDisposePlatformVO4.setWaitAuditQty(waitAuditQty);
            }
            eoComponentActualAssembleGroupList = eoComponentActualAssembleGroupList.stream()
                    .filter(item -> (item.getReleaseQty().compareTo(BigDecimal.ZERO) == 1) || (item.getScrapQty().compareTo(BigDecimal.ZERO) == 1)
                            || (item.getWaitAuditQty().compareTo(BigDecimal.ZERO) == 1)).collect(Collectors.toList());
        }

        List<HmeNcDisposePlatformVO5> detailList = new ArrayList<>();
        List<HmeNcDisposePlatformVO5> detailGroupList = new ArrayList<>();
        //???????????????????????????ID?????????mt_operation_wkc_dispatch_rel?????????????????????operation_id
        String operationId = hmeNcDisposePlatformMapper.getOperationId(tenantId, processId);
        if (StringUtils.isNotEmpty(operationId) && CollectionUtils.isNotEmpty(eoComponentActualAssembleGroupList)) {
            //????????????SN???Id?????????ID?????????hme_eo_job_sn??????jobId
            List<String> jobIdList = hmeNcDisposePlatformMapper.getJobIdList(tenantId, mtMaterialLot.getMaterialLotId(), operationId);
            for (String jobId : jobIdList) {
                //???????????????????????????
                List<HmeNcDisposePlatformVO5> HmeNcDisposePlatformVO5s = hmeNcDisposePlatformMapper.snMaterialQuery(tenantId, jobId);
                if (CollectionUtils.isNotEmpty(HmeNcDisposePlatformVO5s)) {
                    detailList.addAll(HmeNcDisposePlatformVO5s);
                }
                //???????????????????????????
                List<HmeNcDisposePlatformVO5> hmeNcDisposePlatformVO5s2 = hmeNcDisposePlatformMapper.timeMaterialQuery(tenantId, jobId);
                if (CollectionUtils.isNotEmpty(hmeNcDisposePlatformVO5s2)) {
                    detailList.addAll(hmeNcDisposePlatformVO5s2);
                }
            }
        }
        //????????????????????????????????????/???????????????????????????????????????????????????, ?????????????????????jobId????????????
        if (CollectionUtils.isNotEmpty(detailList)) {
            Map<String, List<HmeNcDisposePlatformVO5>> detailMap = detailList.stream().collect(Collectors.groupingBy(HmeNcDisposePlatformVO5::getMaterialLotId));
            for (Map.Entry<String, List<HmeNcDisposePlatformVO5>> entry : detailMap.entrySet()) {
                List<HmeNcDisposePlatformVO5> value = entry.getValue();
                value = value.stream().sorted(Comparator.comparing(HmeNcDisposePlatformVO5::getJobId).reversed()).collect(Collectors.toList());
                HmeNcDisposePlatformVO5 hmeNcDisposePlatformVO5 = value.get(0);
                BigDecimal releaseQty = value.stream().collect(CollectorsUtil.summingBigDecimal(c -> c.getReleaseQty()));
                hmeNcDisposePlatformVO5.setReleaseQty(releaseQty);
                detailGroupList.add(hmeNcDisposePlatformVO5);
            }
        }
        if (CollectionUtils.isNotEmpty(eoComponentActualAssembleGroupList) && CollectionUtils.isNotEmpty(detailGroupList)) {
            for (HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 : eoComponentActualAssembleGroupList) {
                List<HmeNcDisposePlatformVO5> singleDetailList = new ArrayList<>();
                for (HmeNcDisposePlatformVO5 hmeNcDisposePlatformVO5 : detailGroupList) {
                    if (hmeNcDisposePlatformVO4.getMaterialId().equals(hmeNcDisposePlatformVO5.getMaterialId())) {
                        singleDetailList.add(hmeNcDisposePlatformVO5);
                    }
                }
                hmeNcDisposePlatformVO4.setDetailList(singleDetailList);
            }
        }
        resultList.addAll(eoComponentActualAssembleGroupList);
        resultList.addAll(ncMaterialLotList);
        return resultList;
    }

    @Override
    public Map<String, Object> cosMaterialJudge(Long tenantId, HmeNcDisposePlatformDTO18 dto) {
        Map<String, Object> resultMap = new HashMap<>();
        // ?????????????????????
        String cosMaterialFlag = STRING_ZERO;
        if (CollectionUtils.isEmpty(dto.getMaterilalList())) {
            throw new MtException("HME_NC_0071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0071", "HME"));
        }
        //??????LOV??????
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        // ????????????????????? ??????????????????
        for (HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 : dto.getMaterilalList()) {
            if (CollectionUtils.isNotEmpty(hmeNcDisposePlatformVO4.getDetailList())) {
                // ????????????
                for (HmeNcDisposePlatformVO5 hmeNcDisposePlatformVO5 : hmeNcDisposePlatformVO4.getDetailList()) {
                    List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(hmeNcDisposePlatformVO5.getMaterialLotId());
                    if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                        List<String> collect = list.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                        if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                            cosMaterialFlag = STRING_ONE;
                        }
                    }
                }
            } else {
                // ????????????
                List<MtMaterialBasic> mtMaterialBasicsList = queryMaterialBasicList(hmeNcDisposePlatformVO4.getMaterialLotId());
                if (CollectionUtils.isNotEmpty(mtMaterialBasicsList) && StringUtils.isNotEmpty(mtMaterialBasicsList.get(0).getItemGroup())) {
                    List<String> collect = list.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    if (collect.contains(mtMaterialBasicsList.get(0).getItemGroup())) {
                        cosMaterialFlag = STRING_ONE;
                    }
                }
            }
        }
        if (STRING_ZERO.equals(cosMaterialFlag)) {
            throw new MtException("HME_NC_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0072", "HME"));
        } else {
            resultMap.put("cosMaterialFlag", cosMaterialFlag);
        }
        return resultMap;
    }

    private List<MtMaterialBasic> queryMaterialBasicList(String materialLotId) {
        List<MtMaterialBasic> mtMaterialBasics = new ArrayList<>();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
        if (mtMaterialLot != null) {
            List<MtMaterialSite> mtMaterialSites = mtMaterialSiteRepository.selectByCondition(Condition.builder(MtMaterialSite.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtMaterialSite.FIELD_MATERIAL_ID, mtMaterialLot.getMaterialId())
                            .andEqualTo(MtMaterialSite.FIELD_SITE_ID, mtMaterialLot.getSiteId()))
                    .build());
            if (CollectionUtils.isNotEmpty(mtMaterialSites)) {
                mtMaterialBasics = mtMaterialBasisRepository.selectByCondition(Condition.builder(MtMaterialBasic.class)
                        .andWhere(Sqls.custom()
                                .andEqualTo(MtMaterialBasic.FIELD_MATERIAL_SITE_ID, mtMaterialSites.get(0).getMaterialSiteId()))
                        .build());
            }
        }
        return mtMaterialBasics;
    }

    @Override
    public HmeNcDisposePlatformVO7 cosLoadQuery(Long tenantId, HmeNcDisposePlatformDTO18 dto) {
        if (CollectionUtils.isEmpty(dto.getMaterilalList())) {
            throw new MtException("HME_NC_0071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0071", "HME"));
        }
        //??????LOV??????
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        // ????????????????????? ??????????????????
        String materialLotId = "";
        // ????????????
        Boolean feelFlag = false;
        for (HmeNcDisposePlatformVO4 platformVO4 : dto.getMaterilalList()) {
            if (CollectionUtils.isNotEmpty(platformVO4.getDetailList())) {
                // ????????????
                for (HmeNcDisposePlatformVO5 hmeNcDisposePlatformVO5 : platformVO4.getDetailList()) {
                    List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(hmeNcDisposePlatformVO5.getMaterialLotId());
                    if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                        List<String> collect = list.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                        if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                            materialLotId = hmeNcDisposePlatformVO5.getMaterialLotId();
                            feelFlag = false;
                            break;
                        }
                    }
                }
                if (StringUtils.isNotBlank(materialLotId)) {
                    // ??????????????????????????????
                    break;
                }
            } else {
                // ????????????
                List<MtMaterialBasic> mtMaterialBasicsList = queryMaterialBasicList(platformVO4.getMaterialLotId());
                if (CollectionUtils.isNotEmpty(mtMaterialBasicsList) && StringUtils.isNotEmpty(mtMaterialBasicsList.get(0).getItemGroup())) {
                    List<String> collect = list.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    if (collect.contains(mtMaterialBasicsList.get(0).getItemGroup())) {
                        materialLotId = platformVO4.getMaterialLotId();
                        feelFlag = true;
                        break;
                    }
                }
            }
        }
        if (StringUtils.isBlank(materialLotId)) {
            throw new MtException("HME_NC_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0072", "HME"));
        }

        // ??????????????????
        HmeNcDisposePlatformVO7 hmeNcDisposePlatformVO7 = hmeNcDisposePlatformMapper.queryMaterialLotInfo(tenantId, materialLotId);
        // ???????????????????????????????????????????????????
        BigDecimal totalNonOpenNcQty = hmeNcDisposePlatformMapper.queryOpenNcRecordQty(tenantId, materialLotId);
        hmeNcDisposePlatformVO7.setTotalQty(hmeNcDisposePlatformVO7.getPrimaryUomQty().add(totalNonOpenNcQty));
        // ??????????????????
        //????????????????????????
        List<HmeMaterialLotLoad> loadList = new ArrayList<>();
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        if (feelFlag) {
            // ????????? ???????????????
            HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
            hmeMaterialLotLoad.setMaterialLotId(hmeNcDisposePlatformVO7.getMaterialLotId());
            loadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        } else {
            // ??????
            loadList = hmeNcDisposePlatformMapper.queryMaterialLotLoadInfo(tenantId, mtEo.getEoId(), hmeNcDisposePlatformVO7.getMaterialLotId());
        }

        List<HmeMaterialLotLoad> nonEqualOneList = loadList.stream().filter(loadCosNum -> Long.valueOf(1L).compareTo(loadCosNum.getCosNum()) != 0).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(nonEqualOneList)) {
            throw new MtException("HME_NC_0073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0073", "HME"));
        }
        List<HmeMaterialLotLoad> loadSortList = loadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());
        if (hmeNcDisposePlatformVO7.getLocationRow() != null && hmeNcDisposePlatformVO7.getLocationColumn() != null) {
            Long rowNum = hmeNcDisposePlatformVO7.getLocationRow();
            Long columnNum = hmeNcDisposePlatformVO7.getLocationColumn();
            for (int i = 0; i < rowNum; i++) {
                for (int j = 0; j < columnNum; j++) {
                    int rowIndex = i + 1;
                    int columnIndex = j + 1;
                    List<HmeMaterialLotLoad> filterLoadList = loadSortList.stream().filter(load -> Long.valueOf(rowIndex).compareTo(load.getLoadRow()) == 0 && Long.valueOf(columnIndex).compareTo(load.getLoadColumn()) == 0).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(filterLoadList)) {
                        HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                        hmeWoJobSnReturnDTO5.setMaterialLotLoadId(filterLoadList.get(0).getMaterialLotLoadId());
                        hmeWoJobSnReturnDTO5.setLoadSequence(filterLoadList.get(0).getLoadSequence());
                        hmeWoJobSnReturnDTO5.setLoadRow(filterLoadList.get(0).getLoadRow());
                        hmeWoJobSnReturnDTO5.setLoadColumn(filterLoadList.get(0).getLoadColumn());
                        hmeWoJobSnReturnDTO5.setCosNum(filterLoadList.get(0).getCosNum());
                        hmeWoJobSnReturnDTO5.setHotSinkCode(filterLoadList.get(0).getHotSinkCode());
                        HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
                        hmeMaterialLotNcLoad.setLoadSequence(filterLoadList.get(0).getLoadSequence());
                        // ????????????
                        List<HmeSelectionDetails> selectionDetails = hmeSelectionDetailsMapper.select(new HmeSelectionDetails() {{
                            setTenantId(tenantId);
                            setLoadSequence(filterLoadList.get(0).getLoadSequence());
                        }});
                        if (CollectionUtils.isNotEmpty(selectionDetails)) {
                            hmeWoJobSnReturnDTO5.setChipSequence(selectionDetails.get(0).getAttribute2());
                        }
                        List<HmeMaterialLotNcLoad> ncLoadList = new ArrayList<>();
                        if (feelFlag) {
                            ncLoadList = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
                        } else {
                            ncLoadList = hmeNcDisposePlatformMapper.queryMaterialLotNcLoadInfo(tenantId, mtEo.getEoId(), hmeNcDisposePlatformVO7.getMaterialLotId(), filterLoadList.get(0).getLoadSequence());
                        }
                        if (CollectionUtils.isNotEmpty(ncLoadList)) {
                            hmeWoJobSnReturnDTO5.setDocList(ncLoadList);
                        }
                        hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                    } else {
                        hmeWoJobSnReturnDTO5s.add(new HmeWoJobSnReturnDTO5());
                    }
                }
            }
        }
        hmeNcDisposePlatformVO7.setLoadLocationList(hmeWoJobSnReturnDTO5s);
        return hmeNcDisposePlatformVO7;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFunctionInfo(Long tenantId, String eoId, String workcellId, String loadSequence) {
        //??????loadSequence?????????hme_selection_details???ATTRIBUTE2
        List<HmeSelectionDetails> hmeSelectionDetails = hmeSelectionDetailsRepository.select(new HmeSelectionDetails() {{
            setTenantId(tenantId);
            setLoadSequence(loadSequence);
        }});
        if(CollectionUtils.isEmpty(hmeSelectionDetails) || StringUtils.isBlank(hmeSelectionDetails.get(0).getAttribute2())){
            throw new MtException("HME_NC_0078", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0078", "HME", loadSequence));
        }
        String attribute2 = hmeSelectionDetails.get(0).getAttribute2();
        //??????eoid???workcellId???attribute2?????????hme_eo_job_data_record
        List<HmeNcDisposePlatformVO8> hmeNcDisposePlatformVO8List = hmeNcDisposePlatformMapper.eoJobDataRecordQuery(tenantId, eoId, workcellId, attribute2);
        if(CollectionUtils.isEmpty(hmeNcDisposePlatformVO8List)){
            throw new MtException("HME_NC_0079", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0079", "HME", attribute2));
        }
        List<HmeEoJobDataRecord> updateList = new ArrayList<>();
        for (HmeNcDisposePlatformVO8 hmeNcDisposePlatformVO8:hmeNcDisposePlatformVO8List) {
            String tagCode = hmeNcDisposePlatformVO8.getTagCode();
            //??????-??????tagCode,????????????????????????attribute2???1???????????????????????????
            List<String> splitList = Arrays.asList(tagCode.split("-"));
            int i = splitList.indexOf(attribute2) - 1 ;
            if(i < 0){
                MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
                throw new MtException("HME_NC_0081", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0081", "HME", mtEo.getIdentification(), tagCode));
            }
            String splitNumber = splitList.get(i);
            //??????eoid???workcellId???splitNumber_SUMP?????????hme_eo_job_data_record,?????????????????????????????????,????????????????????????
            List<HmeNcDisposePlatformVO8> hmeNcDisposePlatformVO8s = hmeNcDisposePlatformMapper.eoJobDataRecordQuery(tenantId, eoId, workcellId, "-"+splitNumber+"_SUMP");
            if(CollectionUtils.isEmpty(hmeNcDisposePlatformVO8s)){
                MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
                throw new MtException("HME_NC_0080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0080", "HME", mtEo.getIdentification(), splitNumber));
            }
            //???????????????????????????Result????????????????????????Result-?????????????????????Result
            HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
            hmeEoJobDataRecord.setJobRecordId(hmeNcDisposePlatformVO8s.get(0).getJobRecordId());
            hmeEoJobDataRecord.setResult(hmeNcDisposePlatformVO8s.get(0).getResult().subtract(hmeNcDisposePlatformVO8.getResult()).toString());
            updateList.add(hmeEoJobDataRecord);
            //????????????????????????Result??????0
            HmeEoJobDataRecord hmeEoJobDataRecord2 = new HmeEoJobDataRecord();
            hmeEoJobDataRecord2.setJobRecordId(hmeNcDisposePlatformVO8.getJobRecordId());
            hmeEoJobDataRecord2.setResult("0");
            updateList.add(hmeEoJobDataRecord2);
        }
        //????????????
        if(CollectionUtils.isNotEmpty(updateList)){
            int count = 0;
            // ??????????????????
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_data_record_cid_s", updateList.size());
            List<String> updateSnDataSqlList = new ArrayList<>();
            for (HmeEoJobDataRecord updateData : updateList) {
                updateData.setCid(Long.valueOf(cidS.get(count)));
                updateData.setLastUpdatedBy(userId);
                updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                updateSnDataSqlList.addAll(customDbRepository.getUpdateSql(updateData));
                count++;
            }
            jdbcTemplate.batchUpdate(updateSnDataSqlList.toArray(new String[updateSnDataSqlList.size()]));
        }
    }

    void updateNoSiteOutData(Long tenantId, String materialLotId, Long userId, String flag) {
        List<String> noSiteOutJobId = hmeNcDisposePlatformMapper.getNoSiteOutJobId(tenantId, materialLotId);
        if (CollectionUtils.isNotEmpty(noSiteOutJobId)) {
            for (String jobId : noSiteOutJobId) {
                HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(jobId);
                hmeEoJobSn.setSiteOutDate(new Date());
                hmeEoJobSn.setSiteOutBy(userId);
                if (HmeConstants.ProcessStatus.THREE.equals(flag)) {
//                    hmeEoJobSn.setReworkFlag(YES);
                }
                hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);
            }
        }
    }

    void orangeAssemble(Long tenantId, HmeNcDisposePlatformVO5 material, HmeNcDisposePlatformDTO31 dto) {
        //??????API{componentAssembleCancelProcess}??????????????????????????????
        MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        mtAssembleProcessActualVO5.setEoId(mtEo.getEoId());
        mtAssembleProcessActualVO5.setTrxAssembleQty(material.getApplyQty().doubleValue());
        //?????????????????????????????????????????????eo?????????????????????
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isEmpty(bomComponentId)) {
            //????????????????????????Y
            mtAssembleProcessActualVO5.setAssembleExcessFlag(YES);
            mtAssembleProcessActualVO5.setMaterialId(material.getMaterialId());
        } else {
            //2020-12-04 edit by chaonan.hu for can.wang ????????????????????????N
            mtAssembleProcessActualVO5.setAssembleExcessFlag(NO);
            mtAssembleProcessActualVO5.setBomComponentId(bomComponentId);
//            //????????????????????????????????????????????????????????????????????????????????????
//            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
//            String woBomComponentId = hmeNcDisposePlatformMapper.woBomComponentIdQuery(tenantId, mtEo.getWorkOrderId(), mtBomComponent.getLineNumber(), mtBomComponent.getMaterialId());
//            BigDecimal assembleQtySum = hmeNcDisposePlatformMapper.getAssembleQtySum(tenantId, woBomComponentId, mtEo.getWorkOrderId());
//            BigDecimal lineAttribute5 = BigDecimal.ZERO;
//            MtExtendVO mtExtendVO = new MtExtendVO();
//            mtExtendVO.setKeyId(bomComponentId);
//            mtExtendVO.setTableName("mt_bom_component_attr");
//            mtExtendVO.setAttrName("lineAttribute5");
//            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
//            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
//                lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
//            }
//            //??????????????????+?????????????????????????????????????????????N????????????Y
//            if (assembleQtySum.add(material.getApplyQty()).compareTo(lineAttribute5) < 1) {
//                mtAssembleProcessActualVO5.setAssembleExcessFlag(NO);
//            } else {
//                mtAssembleProcessActualVO5.setAssembleExcessFlag(YES);
//            }
        }
//        if (NO.equals(mtAssembleProcessActualVO5.getAssembleExcessFlag())) {
//            mtAssembleProcessActualVO5.setBomComponentId(bomComponentId);
//        } else if (YES.equals(mtAssembleProcessActualVO5.getAssembleExcessFlag())) {
//            mtAssembleProcessActualVO5.setMaterialId(material.getMaterialId());
//        }
        //2020-12-25 edit by chaonan.hu for can.wang operationId???routerStepId????????????????????????????????????????????????
//        MtMaterialLot snMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
//            setTenantId(tenantId);
//            setMaterialLotCode(dto.getSnNumber());
//        }});
//        MtMaterialSite mtMaterialSite = mtMaterialSiteRepository.selectOne(new MtMaterialSite() {{
//            setTenantId(tenantId);
//            setMaterialId(snMaterialLot.getMaterialId());
//            setSiteId(mtEo.getSiteId());
//        }});
//        if (Objects.isNull(mtMaterialSite)) {
//            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(snMaterialLot.getMaterialId());
//            throw new MtException("MT_MATERIAL_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "MT_MATERIAL_0074", "MATERIAL", mtMaterial.getMaterialCode()));
//        }
//        //??????????????????ID??????organizationtype = PRODUCTIONLINE??????organizationid = ?????????ID?????????mt_pfep_manufacturing????????? OPERATION_ASSEMBLE_FLAG
//        MtPfepManufacturing mtPfepManufacturing = mtPfepManufacturingRepository.selectOne(new MtPfepManufacturing() {{
//            setTenantId(tenantId);
//            setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
//            setOrganizationType("PRODUCTIONLINE");
//            setOrganizationId(dto.getProdLineId());
//        }});
//        if (Objects.nonNull(mtPfepManufacturing) && YES.equals(mtPfepManufacturing.getOperationAssembleFlag())) {
//            //??????OperationAssembleFlag???Y????????????operation_id???routerStepId
//            if (NO.equals(mtAssembleProcessActualVO5.getAssembleExcessFlag())) {
//                //??????????????????????????????bom_component_id?????????mt_eo_component_actual ???????????????????????????operation_id??????????????????????????????
//                List<MtEoComponentActual> mtEoComponentActualList = mtEoComponentActualRepository.select(new MtEoComponentActual() {{
//                    setTenantId(tenantId);
//                    setBomComponentId(bomComponentId);
//                    setEoId(mtEo.getEoId());
//                }});
//                if (CollectionUtils.isNotEmpty(mtEoComponentActualList)) {
//                    mtAssembleProcessActualVO5.setOperationId(mtEoComponentActualList.get(0).getOperationId());
//                }
//            } else {
//                //??????????????????
//                List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
//                    setTenantId(tenantId);
//                    setOrganizationId(dto.getWorkcerllId());
//                    setOrganizationType("WORKCELL");
//                    setParentOrganizationType("WORKCELL");
//                }});
//                if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
//                    //????????????ID?????????mt_operation_wkc_dispatch_rel?????????????????????operation_id
//                    String operationId = hmeNcDisposePlatformMapper.getOperationId(tenantId, mtModOrganizationRels.get(0).getParentOrganizationId());
//                    if (StringUtils.isNotEmpty(operationId)) {
//                        mtAssembleProcessActualVO5.setOperationId(operationId);
//                    }
//                }
//            }
//        }
        //??????????????????mt_operation_wkc_dispatch_rel??????operation_id
        String operationId = null;
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getProcessId());
        }});
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
            //????????????????????????{???????????????????????????,?????????}
            throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0044", "HME"));
        } else if (operationWkcDispatchRelList.size() > 1) {
            //???????????????????????????{??????????????????????????????,?????????}
            throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0045", "HME"));
        }
        operationId = operationWkcDispatchRelList.get(0).getOperationId();
        //??????eoId??????router_step_id
        List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, mtEo.getEoId());
        //????????????routerStepId???operationId?????????mt_router_operation???ROUTER_STEP_ID
        List<MtRouterOperation> mtRouterOperationList = new ArrayList<>();
        MtRouterOperation mtRouterOperation = new MtRouterOperation();
        mtRouterOperation.setTenantId(tenantId);
        mtRouterOperation.setOperationId(operationId);
        for (String routerStepId2 : routerStepIdList) {
            mtRouterOperation.setRouterStepId(routerStepId2);
            MtRouterOperation mtRouterOperationDb = mtRouterOperationRepository.selectOne(mtRouterOperation);
            if (Objects.nonNull(mtRouterOperationDb)) {
                mtRouterOperationList.add(mtRouterOperationDb);
            }
        }
        if (CollectionUtils.isEmpty(mtRouterOperationList)) {
            //????????????????????????{?????????????????????????????????????????????}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        } else if (mtRouterOperationList.size() > 1) {
            //???????????????????????????{????????????????????????????????????????????????}
            throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0047", "HME"));
        }
        mtAssembleProcessActualVO5.setOperationId(mtRouterOperationList.get(0).getOperationId());
        mtAssembleProcessActualVO5.setRouterStepId(mtRouterOperationList.get(0).getRouterStepId());
        MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
            setTenantId(tenantId);
            setEoId(mtEo.getEoId());
        }});
        if (Objects.nonNull(mtEoBom)) {
            mtAssembleProcessActualVO5.setBomId(mtEoBom.getBomId());
        }
        MtEoRouter mtEoRouter = mtEoRouterRepository.selectOne(new MtEoRouter() {{
            setTenantId(tenantId);
            setEoId(mtEo.getEoId());
        }});
        if (Objects.nonNull(mtEoRouter)) {
            mtAssembleProcessActualVO5.setRouterId(mtEoRouter.getRouterId());
        }
        mtAssembleProcessActualVO5.setOperationBy(dto.getUserId());
        mtAssembleProcessActualVO5.setWorkcellId(dto.getWorkcerllId());
        mtAssembleProcessActualVO5.setParentEventId(dto.getEventId());
        mtAssembleProcessActualVO5.setEventRequestId(dto.getCreateEventRequestId());
        mtAssembleProcessActualVO5.setMaterialLotId(material.getMaterialLotId());
        log.info("<====== HmeNcDisposePlatformRepositoryImpl.componentAssembleCancelProces.mtAssembleProcessActualVO5:{}", mtAssembleProcessActualVO5);
        mtAssembleProcessActualRepository.componentAssembleCancelProcess(tenantId, mtAssembleProcessActualVO5);
    }

    HmeNcDisposePlatformVO6 orangeScrap(Long tenantId, HmeNcDisposePlatformVO5 material, HmeNcDisposePlatformDTO31 dto) {
        HmeNcDisposePlatformVO6 hmeNcDisposePlatformVO6 = new HmeNcDisposePlatformVO6();
        MtEoComponentActualVO4 mtEoComponentActualVO4 = new MtEoComponentActualVO4();
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        hmeNcDisposePlatformVO6.setWorkOrderId(mtEo.getWorkOrderId());
        mtEoComponentActualVO4.setEoId(mtEo.getEoId());
        //?????????????????????????????????????????????eo?????????????????????
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isEmpty(bomComponentId)) {
            //????????????????????????Y
            mtEoComponentActualVO4.setAssembleExcessFlag(YES);
        } else {
            //2020-12-04 edit by chaonan.hu for can.wang ?????????????????????N
            mtEoComponentActualVO4.setAssembleExcessFlag(NO);
            //????????????????????????????????????????????????????????????????????????????????????
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            String woBomComponentId = hmeNcDisposePlatformMapper.woBomComponentIdQuery(tenantId, mtEo.getWorkOrderId(), mtBomComponent.getLineNumber(), mtBomComponent.getMaterialId());
            BigDecimal assembleQtySum = hmeNcDisposePlatformMapper.getAssembleQtySum(tenantId, woBomComponentId, mtEo.getWorkOrderId());
            BigDecimal lineAttribute5 = BigDecimal.ZERO;
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(bomComponentId);
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute5");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
            }
            hmeNcDisposePlatformVO6.setAssembleQtySum(assembleQtySum);
            hmeNcDisposePlatformVO6.setLineAttribute5(lineAttribute5);
            //??????????????????+?????????????????????????????????????????????N????????????Y
            if (assembleQtySum.add(material.getApplyQty()).compareTo(lineAttribute5) < 1) {
                hmeNcDisposePlatformVO6.setAssembleExcessFlag(NO);
                hmeNcDisposePlatformVO6.setBomComponentId(bomComponentId);
            } else {
                hmeNcDisposePlatformVO6.setAssembleExcessFlag(YES);
            }
        }
        if (NO.equals(mtEoComponentActualVO4.getAssembleExcessFlag())) {
            mtEoComponentActualVO4.setBomComponentId(bomComponentId);
        }
        MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
            setTenantId(tenantId);
            setEoId(mtEo.getEoId());
        }});
        if (Objects.nonNull(mtEoBom)) {
            mtEoComponentActualVO4.setBomId(mtEoBom.getBomId());
        }
        mtEoComponentActualVO4.setEventRequestId(dto.getCreateEventRequestId());
        mtEoComponentActualVO4.setMaterialId(material.getMaterialId());
        //??????????????????mt_operation_wkc_dispatch_rel??????operation_id
        String operationId = null;
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getProcessId());
        }});
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
            //????????????????????????{???????????????????????????,?????????}
            throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0044", "HME"));
        } else if (operationWkcDispatchRelList.size() > 1) {
            //???????????????????????????{??????????????????????????????,?????????}
            throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0045", "HME"));
        }
        operationId = operationWkcDispatchRelList.get(0).getOperationId();
        //??????eoId??????router_step_id
        List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, mtEo.getEoId());
        //????????????routerStepId???operationId?????????mt_router_operation???ROUTER_STEP_ID
        List<MtRouterOperation> mtRouterOperationList = new ArrayList<>();
        MtRouterOperation mtRouterOperation = new MtRouterOperation();
        mtRouterOperation.setTenantId(tenantId);
        mtRouterOperation.setOperationId(operationId);
        for (String routerStepId2 : routerStepIdList) {
            mtRouterOperation.setRouterStepId(routerStepId2);
            MtRouterOperation mtRouterOperationDb = mtRouterOperationRepository.selectOne(mtRouterOperation);
            if (Objects.nonNull(mtRouterOperationDb)) {
                mtRouterOperationList.add(mtRouterOperationDb);
            }
        }
        if (CollectionUtils.isEmpty(mtRouterOperationList)) {
            //????????????????????????{?????????????????????????????????????????????}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        } else if (mtRouterOperationList.size() > 1) {
            //???????????????????????????{????????????????????????????????????????????????}
            throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0047", "HME"));
        }
        mtEoComponentActualVO4.setOperationId(mtRouterOperationList.get(0).getOperationId());
        mtEoComponentActualVO4.setRouterStepId(mtRouterOperationList.get(0).getRouterStepId());
        mtEoComponentActualVO4.setParentEventId(dto.getEventId());
        mtEoComponentActualVO4.setTrxScrappedQty(material.getApplyQty().doubleValue());
        mtEoComponentActualVO4.setWorkcellId(dto.getWorkcerllId());
        log.info("<====== HmeNcDisposePlatformRepositoryImpl.eoComponentScrap.mtEoComponentActualVO4:{}", mtEoComponentActualVO4);
        MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
        BeanUtils.copyProperties(mtEoComponentActualVO4, mtAssembleProcessActualVO5);
        mtAssembleProcessActualVO5.setTrxAssembleQty(material.getApplyQty().doubleValue());
        mtAssembleProcessActualRepository.componentAssembleCancelProcess(tenantId, mtAssembleProcessActualVO5);
        //edit by chaonan.hu for lu.bai ?????????API{eocomponentscrap}??????{componentScrapProcess},???????????????
        MtAssembleProcessActualVO7 mtAssembleProcessActualVO7 = new MtAssembleProcessActualVO7();
        BeanUtils.copyProperties(mtEoComponentActualVO4, mtAssembleProcessActualVO7);
        mtAssembleProcessActualRepository.componentScrapProcess(tenantId, mtAssembleProcessActualVO7);
        return hmeNcDisposePlatformVO6;
    }

    MtNcRecord ncRecordCreate(Long tenantId, MtNcRecord mtNcRecord, HmeNcDisposePlatformDTO32 dto) {
        mtNcRecord.setComponentMaterialId(dto.getMaterialId());
        //????????????
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, new MtNumrangeVO2() {{
            setObjectCode("NC_INCIDENT_NUM");
            setSiteId(dto.getSiteId());
        }});
        MtNcIncidentVO3 mtNcIncidentVO3 = mtNcIncidentRepository.ncIncidentAndHisCreate(tenantId, new MtNcIncidentVO1() {{
            setSiteId(dto.getSiteId());
            setIncidentNumber(mtNumrangeVO5.getNumber());
            setEventId(dto.getEventId());
        }});
        mtNcRecord.setNcIncidentId(mtNcIncidentVO3.getNcIncidentId());
        mtNcRecord.setMaterialLotId(dto.getMaterialLotId());
        //2020-09-10 add by chaonan.hu fou lu.bai ?????????????????????????????????
        mtNcRecord.setQty(dto.getApplyQty().doubleValue());
        mtNcRecordRepository.insertSelective(mtNcRecord);
        //????????????
        MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
        BeanUtils.copyProperties(mtNcRecord, mtNcRecordHis);
        mtNcRecordHis.setEventId(dto.getEventId());
        mtNcRecordHisRepository.insertSelective(mtNcRecordHis);
        //??????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getUuid())) {
            HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel = new HmeNcRecordAttachmentRel();
            hmeNcRecordAttachmentRel.setTenantId(tenantId);
            hmeNcRecordAttachmentRel.setNcRecordId(mtNcRecord.getNcRecordId());
            hmeNcRecordAttachmentRel.setAttachmentId(dto.getUuid());
            hmeNcRecordAttachmentRelRepository.insertSelective(hmeNcRecordAttachmentRel);
        }
        //?????????????????????
        if (CollectionUtils.isNotEmpty(dto.getChildNcCodeIdList())) {
            //?????????????????????
            MtNcRecordVO3 mtNcRecordVO3 = new MtNcRecordVO3();
            mtNcRecordVO3.setEoId(mtNcRecord.getEoId());
            mtNcRecordVO3.setParentNcRecordId(mtNcRecord.getNcRecordId());
            mtNcRecordVO3.setUserId(dto.getUserId());
            mtNcRecordVO3.setNcIncidentId(mtNcRecord.getNcIncidentId());
            mtNcRecordVO3.setComponentMaterialId(mtNcRecord.getComponentMaterialId());
            mtNcRecordVO3.setMaterialLotId(mtNcRecord.getMaterialLotId());
            mtNcRecordVO3.setEoStepActualId(mtNcRecord.getEoStepActualId());
            mtNcRecordVO3.setRootCauseOperationId(mtNcRecord.getRootCauseOperationId());
            mtNcRecordVO3.setRootCauseWorkcellId(mtNcRecord.getRootCauseWorkcellId());
            mtNcRecordVO3.setEventId(dto.getEventId());
            for (String ncCodeId : dto.getChildNcCodeIdList()) {
                mtNcRecordVO3.setNcCodeId(ncCodeId);
                MtNcRecordVO8 mtNcRecordVO8 = mtNcRecordRepository.ncRecordAndHisCreate(tenantId, mtNcRecordVO3);
                if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                    //??????????????????????????????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                    MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                    mtNcRecordVO2.setEventRequestId(dto.getEventRequestId());
                    mtNcRecordVO2.setNcRecordId(mtNcRecordVO8.getNcRecordId());
                    mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
                }
            }
        }
        return mtNcRecord;
    }

    void updateAssembleRecord(Long tenantId, String siteId, String workcellId, HmeNcDisposePlatformVO5 dto) {
        //????????????Id?????????ID?????????????????????????????????attribute14???????????????????????????lot
        MtMaterialSite mtMaterialSite = mtMaterialSiteRepository.selectOne(new MtMaterialSite() {{
            setTenantId(tenantId);
            setMaterialId(dto.getMaterialId());
            setSiteId(siteId);
        }});
        if (Objects.isNull(mtMaterialSite)) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
            throw new MtException("MT_MATERIAL_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_0074", "MATERIAL", mtMaterial.getMaterialCode()));
        }
        String attribute14 = "LOT";
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setKeyId(mtMaterialSite.getMaterialSiteId());
        mtExtendVO.setTableName("mt_material_site_attr");
        mtExtendVO.setAttrName("attribute14");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
            attribute14 = mtExtendAttrVOS.get(0).getAttrValue();
        }
        if ("SN".equals(attribute14)) {
            //????????????????????????????????????jobId???materialLotId???materialId???isissued=1???????????????????????????????????????isissued??????0??????????????????????????????
            List<HmeEoJobMaterial> hmeEoJobMaterials = hmeEoJobMaterialRepository.select(new HmeEoJobMaterial() {{
                setTenantId(tenantId);
                setJobId(dto.getJobId().toString());
                setMaterialLotId(dto.getMaterialLotId());
                setMaterialId(dto.getMaterialId());
                setIsIssued(1);
            }});
            if (CollectionUtils.isNotEmpty(hmeEoJobMaterials)) {
                if (hmeEoJobMaterials.size() > 1) {
                    throw new MtException("HME_NC_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0033", "HME"));
                }
                HmeEoJobMaterial hmeEoJobMaterial = hmeEoJobMaterials.get(0);
                hmeEoJobMaterial.setIsIssued(0);
                hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial);
                //????????????????????????
                HmeEoJobMaterial returnMaterial = new HmeEoJobMaterial();
                BeanUtils.copyProperties(hmeEoJobMaterial, returnMaterial);
                returnMaterial.setJobMaterialId(null);
                returnMaterial.setReleaseQty(dto.getApplyQty().multiply(new BigDecimal(-1)));
                returnMaterial.setIsIssued(null);
                returnMaterial.setCreatedBy(null);
                returnMaterial.setCreationDate(null);
                returnMaterial.setLastUpdatedBy(null);
                returnMaterial.setLastUpdateDate(null);
                hmeEoJobMaterialRepository.insertSelective(returnMaterial);
            }
        } else if ("LOT".equals(attribute14) || "TIME".equals(attribute14)) {
            //???????????????/????????????????????????HME_EO_JOB_SN_LOT_MATERIAL?????????jobId???materialLotId???ReleaseQty??????0???????????????????????????
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterials = hmeNcDisposePlatformMapper.eoJobSnLotMaterialQuery(tenantId, dto.getJobId().toString(), dto.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterials)) {
                BigDecimal releaseQty = dto.getApplyQty();
                for (HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial : hmeEoJobSnLotMaterials) {
                    if (hmeEoJobSnLotMaterial.getReleaseQty().compareTo(releaseQty) >= 0) {
                        hmeEoJobSnLotMaterial.setReleaseQty(hmeEoJobSnLotMaterial.getReleaseQty().subtract(releaseQty));
                        hmeEoJobSnLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobSnLotMaterial);
                        break;
                    } else {
                        hmeEoJobSnLotMaterial.setReleaseQty(BigDecimal.ZERO);
                        hmeEoJobSnLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobSnLotMaterial);
                        releaseQty = releaseQty.subtract(hmeEoJobSnLotMaterial.getReleaseQty());
                    }
                }
            }
            //???????????????HME_EO_JOB_SN_LOT_MATERIAL????????????????????????
            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
            hmeEoJobSnLotMaterial.setTenantId(tenantId);
            hmeEoJobSnLotMaterial.setMaterialType(attribute14);
            hmeEoJobSnLotMaterial.setWorkcellId(workcellId);
            hmeEoJobSnLotMaterial.setJobId(dto.getJobId().toString());
            hmeEoJobSnLotMaterial.setMaterialId(dto.getMaterialId());
            hmeEoJobSnLotMaterial.setMaterialLotId(dto.getMaterialLotId());
            hmeEoJobSnLotMaterial.setReleaseQty(dto.getApplyQty().multiply(new BigDecimal(-1)));
            hmeEoJobSnLotMaterial.setIsReleased(0);
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
            hmeEoJobSnLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
            hmeEoJobSnLotMaterial.setLotCode(mtMaterialLot.getLot());
            MtExtendVO mtExtendVO2 = new MtExtendVO();
            mtExtendVO2.setKeyId(mtMaterialLot.getMaterialLotId());
            mtExtendVO2.setTableName("mt_material_lot_attr");
            mtExtendVO2.setAttrName("MATERIAL_VERSION");
            mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO2);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                hmeEoJobSnLotMaterial.setProductionVersion(mtExtendAttrVOS.get(0).getAttrValue());
            }
            hmeEoJobSnLotMaterial.setRemainQty(BigDecimal.ZERO);
            hmeEoJobSnLotMaterialRepository.insertSelective(hmeEoJobSnLotMaterial);
            //???????????????????????????????????????????????????????????????released_qty
            if ("LOT".equals(attribute14)) {
                List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
                    setTenantId(tenantId);
                    setWorkcellId(workcellId);
                    setMaterialId(dto.getMaterialId());
                }});
                for (HmeEoJobLotMaterial hmeEoJobLotMaterial : hmeEoJobLotMaterials) {
                    BigDecimal releaseQty = Objects.isNull(hmeEoJobLotMaterial.getReleaseQty()) ? BigDecimal.ZERO : hmeEoJobLotMaterial.getReleaseQty();
                    hmeEoJobLotMaterial.setReleaseQty(releaseQty.add(dto.getApplyQty()));
                    hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterial);
                }
            } else {
                List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterials = hmeEoJobTimeMaterialRepository.select(new HmeEoJobTimeMaterial() {{
                    setTenantId(tenantId);
                    setWorkcellId(workcellId);
                    setMaterialId(dto.getMaterialId());
                }});
                for (HmeEoJobTimeMaterial hmeEoJobTimeMaterial : hmeEoJobTimeMaterials) {
                    BigDecimal releaseQty = Objects.isNull(hmeEoJobTimeMaterial.getReleaseQty()) ? BigDecimal.ZERO : hmeEoJobTimeMaterial.getReleaseQty();
                    hmeEoJobTimeMaterial.setReleaseQty(releaseQty.add(dto.getApplyQty()));
                    hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(hmeEoJobTimeMaterial);
                }
            }
        }
    }

    /**
     * ???????????????????????? ????????????
     *
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @param material ?????????????????????
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 10:58:50
     */
    void alreadyAssembleScrap(Long tenantId, HmeNcDisposePlatformDTO33 dto, HmeNcDisposePlatformVO5 material) {
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        //?????????????????????????????????????????????eo?????????????????????
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isNotBlank(bomComponentId)) {
            //????????????????????????BOM???
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            if (mtBomComponent.getQty() != 0)   {
                //???????????????????????????0?????????????????????,???????????????????????????????????????
                //?????????????????????????????????????????????????????????????????????????????????
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
                //???????????????material_id??????????????????work_order_id?????????????????????mt_work_order_component_actual.assemble_qty+SCRAPPED_QTY??????
                BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //??????bomComponentId?????????????????????lineAttribute4 2021-02-02 edit by chaonan.hu for can.wang ?????????lineAttribute5
                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setKeyId(bomComponentId);
                mtExtendVO.setTableName("mt_bom_component_attr");
                mtExtendVO.setAttrName("lineAttribute5");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                }
                if (assembleQty.compareTo(lineAttribute5) <= 0) {
                    //???assembleQty????????????lineAttribute5??????????????????????????????????????????
                    HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                    hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R", mtBomComponent, hmeNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                } else {
                    //???assembleQty??????lineAttribute5???????????????????????????
                    if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) <= 0) {
                        //???????????????????????????(assemble_qty - lineAttribute5)????????????????????????????????????
                        HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                        hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                        hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", mtBomComponent, hmeNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                    } else {
                        //?????????????????????(assemble_qty - lineAttribute5)????????????????????????(assemble_qty - lineAttribute5)?????????????????????[???????????? ??? (assemble_qty - lineAttribute5)]??????????????????
                        HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                        hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty).add(lineAttribute5));
                        hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R", mtBomComponent, hmeNcDisposePlatformDTO34);
                        WmsObjectTransactionRequestVO objectTransactionRequestVOExt = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(objectTransactionRequestVO, objectTransactionRequestVOExt);
                        objectTransactionRequestVOExt.setTransactionTypeCode("HME_WO_ISSUE_R_EXT");
                        objectTransactionRequestVOExt.setTransactionQty(assembleQty.subtract(lineAttribute5));
                        objectTransactionRequestVOExt.setBomReserveLineNum(null);
                        objectTransactionRequestVOExt.setBomReserveNum(null);
                        objectTransactionRequestVOExt.setSoNum(null);
                        objectTransactionRequestVOExt.setSoLineNum(null);
                        List<WmsObjectTransactionRequestVO> objectTransactionRequestVOList = new ArrayList<>();
                        objectTransactionRequestVOList.add(objectTransactionRequestVO);
                        objectTransactionRequestVOList.add(objectTransactionRequestVOExt);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVOExt:{}", objectTransactionRequestVOExt);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestVOList);
                    }
                }
                //????????????????????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //2021-02-02 edit by chaonan.hu for can.wang lineAttribute???????????????lineAttribute5,?????????lineAttribute5 - lineAttribute4
                BigDecimal lineAttribute = lineAttribute5;
                if (scrapQty.compareTo(lineAttribute) >= 0) {
                    //??????scrapQty????????????lineAttribute,????????????????????????????????????
                    HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    hmeNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                    hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    WmsObjectTransactionRequestVO objectTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", mtBomComponent, hmeNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                } else {
                    //??????scrapQty??????lineAttribute?????????????????????
                    if (scrapQty.add(material.getApplyQty()).compareTo(lineAttribute) <= 0) {
                        //??????(scrapped_qty + ????????????)????????????lineAttribute???????????????????????????????????????
                        HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        hmeNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                        hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE", mtBomComponent, hmeNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                    } else {
                        //??????(scrapped_qty + ????????????)??????lineAttribute?????????????????????????????????[(scrapped_qty + ????????????) - lineAttribute]???????????????????????????lineAttribute5-scrapped_qty??????????????????
                        HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        hmeNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        hmeNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                        hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE", mtBomComponent, hmeNcDisposePlatformDTO34);
                        WmsObjectTransactionRequestVO objectTransactionRequestVOExt = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(objectTransactionRequestVO, objectTransactionRequestVOExt);
                        objectTransactionRequestVOExt.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        objectTransactionRequestVOExt.setTransactionQty(scrapQty.add(material.getApplyQty()).subtract(lineAttribute));
                        objectTransactionRequestVOExt.setBomReserveLineNum(null);
                        objectTransactionRequestVOExt.setBomReserveNum(null);
                        objectTransactionRequestVOExt.setSoNum(null);
                        objectTransactionRequestVOExt.setSoLineNum(null);
                        List<WmsObjectTransactionRequestVO> objectTransactionRequestVOList = new ArrayList<>();
                        objectTransactionRequestVOList.add(objectTransactionRequestVO);
                        objectTransactionRequestVOList.add(objectTransactionRequestVOExt);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVOExt:{}", objectTransactionRequestVOExt);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestVOList);
                    }
                }
            } else {
                //????????????????????????0????????????????????????????????????????????????????????????
                MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
                    setTenantId(tenantId);
                    setEoId(mtEo.getEoId());
                }});
                List<String> primaryMaterial = hmeNcDisposePlatformMapper.getPrimaryMaterial(tenantId, mtBomComponent.getBomComponentId(), mtEoBom.getBomId());
                if (CollectionUtils.isEmpty(primaryMaterial)) {
                    throw new MtException("HME_NC_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0037", "HME"));
                } else if (primaryMaterial.size() > 1) {
                    throw new MtException("HME_NC_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0038", "HME"));
                }
                MtBomComponent primaryBomComponent = mtBomComponentRepository.selectByPrimaryKey(primaryMaterial.get(0));
                //?????????????????????????????????????????????
                HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", mtBomComponent, hmeNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                //????????????????????????material_id??????????????????????????????????????????????????????
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
                //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.assemble_qty
                BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //???????????????bomComponentId?????????????????????lineAttribute5
                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setKeyId(primaryBomComponent.getBomComponentId());
                mtExtendVO.setTableName("mt_bom_component_attr");
                mtExtendVO.setAttrName("lineAttribute5");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                }
                if (assembleQty.compareTo(lineAttribute5) <= 0) {
                    //??????assembleQty????????????lineAttribute5??????????????????????????????????????????????????????????????????????????????????????????
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //??????????????????????????????
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryObjectTransactionRequestVO2);
                    primaryObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    primaryObjectTransactionRequestVO2.setTransactionReasonCode("??????????????????");
                    primaryObjectTransactionRequestVO2.setMoveType("261");
                    primaryObjectTransactionRequestVO2.setMoveReason("??????????????????");
                    primaryObjectTransactionRequestVO2.setBomReserveNum(null);
                    primaryObjectTransactionRequestVO2.setBomReserveLineNum(null);
                    primaryObjectTransactionRequestVO2.setSoNum(null);
                    primaryObjectTransactionRequestVO2.setSoLineNum(null);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO2);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO2));
                } else {
                    //??????assembleQty??????lineAttribute5????????????????????????
                    if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) == 1) {
                        //????????????????????????(assembleQty-lineAttribute5),?????????????????????,??????????????????????????????????????????
                        //????????????-(assembleQty-lineAttribute5)?????????????????????????????????-(assembleQty-lineAttribute5)??????????????????
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty.subtract(lineAttribute5)));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //????????????-(assembleQty-lineAttribute5)??????????????????
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryObjectTransactionRequestVO2);
                        primaryObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        primaryObjectTransactionRequestVO2.setTransactionReasonCode("??????????????????");
                        primaryObjectTransactionRequestVO2.setMoveType("261");
                        primaryObjectTransactionRequestVO2.setMoveReason("??????????????????");
                        primaryObjectTransactionRequestVO2.setBomReserveNum(null);
                        primaryObjectTransactionRequestVO2.setBomReserveLineNum(null);
                        primaryObjectTransactionRequestVO2.setSoNum(null);
                        primaryObjectTransactionRequestVO2.setSoLineNum(null);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO2);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO2));
                    }
                }
                //?????????????????????
                HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", mtBomComponent, hmeNcScrapDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
                //?????????????????????
                //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                BigDecimal lineAttribute = lineAttribute5;
                if (material.getApplyQty().compareTo(lineAttribute.subtract(scrapQty)) <= 0) {
                    //??????????????????????????????lineAttribute-scrapQty,????????????????????????????????????????????????????????? ??????????????????????????????
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //??????????????????????????????
                    HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                    primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryInAssemble.setEventId(dto.getScrapTransactionId());
                    primaryInAssemble.setTransactionQty(material.getApplyQty());
                    primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                } else {
                    //????????????????????????lineAttribute-scrapQty,?????????????????????
                    if(lineAttribute.compareTo(scrapQty) == 1){
                        //??????lineAttribute??????scrapQty???????????????????????????,??????????????????????????????
                        //lineAttribute-scrapQty???HME_WO_ISSUE_R_EXT???????????????
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.??????alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //lineAttribute-scrapQty???HME_WO_ISSUE???????????????
                        HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                        primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryInAssemble.setEventId(dto.getScrapTransactionId());
                        primaryInAssemble.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                    }
//                    if (lineAttribute.compareTo(BigDecimal.ZERO) <= 0) {
//                        //??????lineAttribute5-lineAttribute4????????????0????????????{SAP??????????????????????????????????????????}
//                        throw new MtException("HME_NC_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_NC_0041", "HME"));
//                    } else {
//                        //??????lineAttribute5-lineAttribute4??????0????????????????????????
//                        //??????????????????????????????
//                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
//                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
//                        primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
//                        primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
//                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
//                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
//                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
//                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
//                        //scrapQty+????????????-(lineAttribute5-lineAttribute4)??????????????????
//                        WmsObjectTransactionRequestVO primaryOutAssembleDTO = new WmsObjectTransactionRequestVO();
//                        BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssembleDTO);
//                        primaryOutAssembleDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
//                        primaryOutAssembleDTO.setTransactionReasonCode("??????????????????");
//                        primaryOutAssembleDTO.setMoveType("261");
//                        primaryOutAssembleDTO.setMoveReason("??????????????????");
//                        primaryOutAssembleDTO.setTransactionQty(scrapQty.add(material.getApplyQty()).subtract(lineAttribute));
//                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryOutAssembleTransaction.objectTransactionRequestVO:{}", primaryOutAssembleDTO);
//                        //(lineAttribute5-lineAttribute4)-scrapQty??????????????????
//                        HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
//                        primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
//                        primaryInAssemble.setEventId(dto.getScrapTransactionId());
//                        primaryInAssemble.setTransactionQty(lineAttribute.subtract(scrapQty));
//                        primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
//                        WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
//                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
//                        List<WmsObjectTransactionRequestVO> objectTransactionRequestVOList = new ArrayList<>();
//                        objectTransactionRequestVOList.add(primaryOutAssembleDTO);
//                        objectTransactionRequestVOList.add(primaryInAssembleTransactionRequestVO);
//                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestVOList);
//                    }
                }
            }
        } else {
            //??????????????????BOM???????????????????????????????????????
            MtBomComponent primaryBomComponent = getPrimaryMaterial(tenantId, material.getMaterialId(), mtEo, dto.getProcessId());
            if (Objects.isNull(primaryBomComponent)) {
                //???????????????????????????{??????????????????????????????????????????????????????}
                throw new MtException("HME_NC_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0042", "HME"));
            }
            //????????????????????? ???????????????HME_WO_ISSUE_R_EXT???????????????
            HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
            hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
            hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
            hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
            hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
            hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
            WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", null, hmeNcDisposePlatformDTO34);
            log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransactionS.objectTransactionRequestVO:{}", objectTransactionRequestVO);
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
            //????????????????????????????????????????????????material_id??????????????????????????????????????????????????????
            HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
            //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.assemble_qty
            BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
            //???????????????bomComponentId?????????????????????lineAttribute5
            BigDecimal lineAttribute5 = BigDecimal.ZERO;
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(primaryBomComponent.getBomComponentId());
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute5");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
            }
            if (assembleQty.compareTo(lineAttribute5) <= 0) {
                //??????assembleQty????????????lineAttribute5????????????????????????
                //???????????????HME_WO_ISSUE_R???????????????
                HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                //???????????????HME_WO_ISSUE_EXT???????????????
                WmsObjectTransactionRequestVO primaryOutAssemble = new WmsObjectTransactionRequestVO();
                BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssemble);
                primaryOutAssemble.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                primaryOutAssemble.setTransactionReasonCode("??????????????????");
                primaryOutAssemble.setMoveType("261");
                primaryOutAssemble.setMoveReason("??????????????????");
                primaryOutAssemble.setBomReserveNum(null);
                primaryOutAssemble.setBomReserveLineNum(null);
                primaryOutAssemble.setSoNum(null);
                primaryOutAssemble.setSoLineNum(null);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryAssembleOutTransaction.objectTransactionRequestVO:{}", primaryOutAssemble);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryOutAssemble));
            } else {
                //??????assembleQty??????lineAttribute5????????????????????????
                if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) == 1) {
                    //?????????????????????(assembleQty-lineAttribute5)????????????????????????????????????????????????????????????
                    //????????????-(assembleQty-lineAttribute5)???HME_WO_ISSUE_R???????????????
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty.subtract(lineAttribute5)));
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //????????????-(assembleQty-lineAttribute5)???HME_WO_ISSUE_ EXT???????????????
                    WmsObjectTransactionRequestVO primaryOutAssemble = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssemble);
                    primaryOutAssemble.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    primaryOutAssemble.setTransactionReasonCode("??????????????????");
                    primaryOutAssemble.setMoveType("261");
                    primaryOutAssemble.setMoveReason("??????????????????");
                    primaryOutAssemble.setBomReserveLineNum(null);
                    primaryOutAssemble.setBomReserveNum(null);
                    primaryOutAssemble.setSoLineNum(null);
                    primaryOutAssemble.setSoNum(null);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryAssembleOutTransaction.objectTransactionRequestVO:{}", primaryOutAssemble);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryOutAssemble));
                }
            }
            //?????????????????????  ???????????????HME_WO_ISSUE_EXT???????????????
            HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
            hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
            hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
            hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
            hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
            hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
            WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", null, hmeNcScrapDisposePlatformDTO34);
            log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
            //????????????????????? ?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.scrapped_qty
            BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
            BigDecimal lineAttribute = lineAttribute5;
            if (material.getApplyQty().compareTo(lineAttribute.subtract(scrapQty)) <= 0) {
                //???????????????????????????lineAttribute-scrapQty, ?????????????????????
                //???????????????HME_WO_ISSUE_R_EXT???????????????
                HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryOutReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                //???????????????HME_WO_ISSUE???????????????
                HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                primaryInAssemble.setEventId(dto.getScrapTransactionId());
                primaryInAssemble.setTransactionQty(material.getApplyQty());
                primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
            } else {
                //?????????????????????lineAttribute-scrapQty, ?????????????????????
                if(lineAttribute.compareTo(scrapQty) == 1){
                    //??????lineAttribute??????scrapQty???????????????????????????,??????????????????????????????
                    //lineAttribute-scrapQty???HME_WO_ISSUE_R_EXT???????????????
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.??????alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //lineAttribute-scrapQty???HME_WO_ISSUE???????????????
                    HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                    primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryInAssemble.setEventId(dto.getScrapTransactionId());
                    primaryInAssemble.setTransactionQty(lineAttribute.subtract(scrapQty));
                    primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                }
            }
        }
    }

    /**
     * ?????????????????????????????? ????????????
     *
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @param material ?????????????????????
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 10:58:50
     */
    void alreadyAssembleNotice(Long tenantId, HmeNcDisposePlatformDTO33 dto, HmeNcDisposePlatformVO5 material) {
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        //?????????????????????????????????????????????eo?????????????????????
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isNotBlank(bomComponentId)) {
            //????????????????????????BOM???
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            if (mtBomComponent.getQty() != 0) {
                //???????????????????????????0?????????????????????,???????????????????????????????????????
                //???????????????????????????????????????material_id??????????????????????????????????????????????????????
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
                //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.assemble_qty
                BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //??????bomComponentId?????????????????????lineAttribute5
                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setKeyId(bomComponentId);
                mtExtendVO.setTableName("mt_bom_component_attr");
                mtExtendVO.setAttrName("lineAttribute5");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                }
                if (assembleQty.compareTo(lineAttribute5) <= 0) {
                    //???assembleQty????????????lineAttribute5??????????????????????????????????????????
                    HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                    hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R", mtBomComponent, hmeNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                } else {
                    //???assembleQty??????lineAttribute5???????????????????????????
                    if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) <= 0) {
                        //???????????????????????????(assemble_qty - lineAttribute5)????????????????????????????????????
                        HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                        hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                        hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", mtBomComponent, hmeNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                    } else {
                        //?????????????????????(assemble_qty - lineAttribute5)????????????????????????(assemble_qty - lineAttribute5)?????????????????????[???????????? ??? (assemble_qty - lineAttribute4)]??????????????????
                        HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                        hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty).add(lineAttribute5));
                        hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R", mtBomComponent, hmeNcDisposePlatformDTO34);
                        WmsObjectTransactionRequestVO objectTransactionRequestVOExt = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(objectTransactionRequestVO, objectTransactionRequestVOExt);
                        objectTransactionRequestVOExt.setTransactionTypeCode("HME_WO_ISSUE_R_EXT");
                        objectTransactionRequestVOExt.setTransactionQty(assembleQty.subtract(lineAttribute5));
                        objectTransactionRequestVOExt.setBomReserveLineNum(null);
                        objectTransactionRequestVOExt.setBomReserveNum(null);
                        objectTransactionRequestVOExt.setSoNum(null);
                        objectTransactionRequestVOExt.setSoLineNum(null);
                        List<WmsObjectTransactionRequestVO> objectTransactionRequestVOList = new ArrayList<>();
                        objectTransactionRequestVOList.add(objectTransactionRequestVO);
                        objectTransactionRequestVOList.add(objectTransactionRequestVOExt);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVOExt:{}", objectTransactionRequestVOExt);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestVOList);
                    }
                }
            } else {
                //????????????????????????0????????????????????????????????????????????????????????????
                MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
                    setTenantId(tenantId);
                    setEoId(mtEo.getEoId());
                }});
                List<String> primaryMaterial = hmeNcDisposePlatformMapper.getPrimaryMaterial(tenantId, mtBomComponent.getBomComponentId(), mtEoBom.getBomId());
                if (CollectionUtils.isEmpty(primaryMaterial)) {
                    throw new MtException("HME_NC_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0037", "HME"));
                } else if (primaryMaterial.size() > 1) {
                    throw new MtException("HME_NC_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0038", "HME"));
                }
                MtBomComponent primaryBomComponent = mtBomComponentRepository.selectByPrimaryKey(primaryMaterial.get(0));
                //?????????????????????????????????????????????
                HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", mtBomComponent, hmeNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                //????????????????????????material_id??????????????????????????????????????????????????????
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
                //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.assemble_qty
                BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //???????????????bomComponentId?????????????????????lineAttribute4
                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setKeyId(primaryBomComponent.getBomComponentId());
                mtExtendVO.setTableName("mt_bom_component_attr");
                mtExtendVO.setAttrName("lineAttribute5");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                }
                if (assembleQty.compareTo(lineAttribute5) <= 0) {
                    //??????assembleQty????????????lineAttribute5??????????????????????????????????????????????????????????????????????????????????????????
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //??????????????????????????????
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryObjectTransactionRequestVO2);
                    primaryObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    primaryObjectTransactionRequestVO2.setTransactionReasonCode("??????????????????");
                    primaryObjectTransactionRequestVO2.setMoveType("261");
                    primaryObjectTransactionRequestVO2.setMoveReason("??????????????????");
                    primaryObjectTransactionRequestVO2.setBomReserveNum(null);
                    primaryObjectTransactionRequestVO2.setBomReserveLineNum(null);
                    primaryObjectTransactionRequestVO2.setSoNum(null);
                    primaryObjectTransactionRequestVO2.setSoLineNum(null);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO2);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO2));
                } else {
                    //??????assembleQty??????lineAttribute5????????????????????????
                    if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) == 1) {
                        //????????????????????????(assembleQty-lineAttribute5),?????????????????????,??????????????????????????????????????????
                        //????????????-(assembleQty-lineAttribute5)?????????????????????????????????-(assembleQty-lineAttribute5)??????????????????
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty.subtract(lineAttribute5)));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //????????????-(assembleQty-lineAttribute5)??????????????????
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryObjectTransactionRequestVO2);
                        primaryObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        primaryObjectTransactionRequestVO2.setTransactionReasonCode("??????????????????");
                        primaryObjectTransactionRequestVO2.setMoveType("261");
                        primaryObjectTransactionRequestVO2.setMoveReason("??????????????????");
                        primaryObjectTransactionRequestVO2.setBomReserveNum(null);
                        primaryObjectTransactionRequestVO2.setBomReserveLineNum(null);
                        primaryObjectTransactionRequestVO2.setSoNum(null);
                        primaryObjectTransactionRequestVO2.setSoLineNum(null);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO2);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO2));
                    }
                }
            }
        } else {
            //??????????????????BOM???????????????????????????????????????
            MtBomComponent primaryBomComponent = getPrimaryMaterial(tenantId, material.getMaterialId(), mtEo, dto.getProcessId());
            if (Objects.isNull(primaryBomComponent)) {
                //???????????????????????????{??????????????????????????????????????????????????????}
                throw new MtException("HME_NC_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0042", "HME"));
            }
            //????????????????????? ???????????????HME_WO_ISSUE_R_EXT???????????????
            HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
            hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
            hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
            hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
            hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
            hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
            WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", null, hmeNcDisposePlatformDTO34);
            log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransactionS.objectTransactionRequestVO:{}", objectTransactionRequestVO);
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
            //????????????????????????????????????????????????material_id??????????????????????????????????????????????????????
            HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
            //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.assemble_qty
            BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
            //???????????????bomComponentId?????????????????????lineAttribute5
            BigDecimal lineAttribute5 = BigDecimal.ZERO;
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(primaryBomComponent.getBomComponentId());
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute5");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
            }
            if (assembleQty.compareTo(lineAttribute5) <= 0) {
                //??????assembleQty????????????lineAttribute5????????????????????????
                //???????????????HME_WO_ISSUE_R???????????????
                HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                //???????????????HME_WO_ISSUE_EXT???????????????
                WmsObjectTransactionRequestVO primaryOutAssemble = new WmsObjectTransactionRequestVO();
                BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssemble);
                primaryOutAssemble.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                primaryOutAssemble.setTransactionReasonCode("??????????????????");
                primaryOutAssemble.setMoveType("261");
                primaryOutAssemble.setMoveReason("??????????????????");
                primaryOutAssemble.setBomReserveNum(null);
                primaryOutAssemble.setBomReserveLineNum(null);
                primaryOutAssemble.setSoNum(null);
                primaryOutAssemble.setSoLineNum(null);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryAssembleOutTransaction.objectTransactionRequestVO:{}", primaryOutAssemble);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryOutAssemble));
            } else {
                //??????assembleQty??????lineAttribute4????????????????????????
                if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) == 1) {
                    //?????????????????????(assembleQty-lineAttribute5)????????????????????????????????????????????????????????????
                    //????????????-(assembleQty-lineAttribute5)???HME_WO_ISSUE_R???????????????
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty.subtract(lineAttribute5)));
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //????????????-(assembleQty-lineAttribute5)???HME_WO_ISSUE_ EXT???????????????
                    WmsObjectTransactionRequestVO primaryOutAssemble = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssemble);
                    primaryOutAssemble.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    primaryOutAssemble.setTransactionReasonCode("??????????????????");
                    primaryOutAssemble.setMoveType("261");
                    primaryOutAssemble.setMoveReason("??????????????????");
                    primaryOutAssemble.setBomReserveLineNum(null);
                    primaryOutAssemble.setBomReserveNum(null);
                    primaryOutAssemble.setSoLineNum(null);
                    primaryOutAssemble.setSoNum(null);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryAssembleOutTransaction.objectTransactionRequestVO:{}", primaryOutAssemble);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryOutAssemble));
                }
            }
        }
    }

    /**
     * ???????????????????????? ????????????
     *
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @param material ?????????????????????
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 11:01:28
     */
    void noScrap(Long tenantId, HmeNcDisposePlatformDTO33 dto, HmeNcDisposePlatformVO4 material) {
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        //?????????????????????????????????????????????eo?????????????????????
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isNotBlank(bomComponentId)) {
            //????????????????????????BOM???
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            if (mtBomComponent.getQty() != 0) {
                //?????????????????????0 ????????????????????????????????????????????????material_id??????????????????????????????????????????????????????
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
                //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //??????bomComponentId?????????????????????lineAttribute5
                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setKeyId(bomComponentId);
                mtExtendVO.setTableName("mt_bom_component_attr");
                mtExtendVO.setAttrName("lineAttribute5");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                }
                BigDecimal lineAttribute = lineAttribute5;
                if (scrapQty.compareTo(lineAttribute) >= 0) {
                    //??????scrapQty????????????lineAttribute,????????????????????????????????????
                    HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    hmeNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                    hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    WmsObjectTransactionRequestVO objectTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", mtBomComponent, hmeNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                } else {
                    //??????scrapQty??????lineAttribute?????????????????????
                    if (scrapQty.add(material.getApplyQty()).compareTo(lineAttribute) <= 0) {
                        //??????(scrapped_qty + ????????????)????????????lineAttribute???????????????????????????????????????
                        HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        hmeNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                        hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE", mtBomComponent, hmeNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                    } else {
                        //??????(scrapped_qty + ????????????)??????lineAttribute?????????????????????????????????[(scrapped_qty + ????????????) - (lineAttribute5 - lineAttribute4)]???????????????????????????(lineAttribute5 - lineAttribute4)-scrapped_qty??????????????????
                        HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        hmeNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                        hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        hmeNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                        hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE", mtBomComponent, hmeNcDisposePlatformDTO34);
                        WmsObjectTransactionRequestVO objectTransactionRequestVOExt = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(objectTransactionRequestVO, objectTransactionRequestVOExt);
                        objectTransactionRequestVOExt.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        objectTransactionRequestVOExt.setTransactionQty(scrapQty.add(material.getApplyQty()).subtract(lineAttribute));
                        objectTransactionRequestVOExt.setBomReserveLineNum(null);
                        objectTransactionRequestVOExt.setBomReserveNum(null);
                        objectTransactionRequestVOExt.setSoNum(null);
                        objectTransactionRequestVOExt.setSoLineNum(null);
                        List<WmsObjectTransactionRequestVO> objectTransactionRequestVOList = new ArrayList<>();
                        objectTransactionRequestVOList.add(objectTransactionRequestVO);
                        objectTransactionRequestVOList.add(objectTransactionRequestVOExt);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVOExt:{}", objectTransactionRequestVOExt);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestVOList);
                    }
                }
            } else {
                //????????????????????????0????????????????????????????????????????????????????????????
                MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
                    setTenantId(tenantId);
                    setEoId(mtEo.getEoId());
                }});
                List<String> primaryMaterial = hmeNcDisposePlatformMapper.getPrimaryMaterial(tenantId, mtBomComponent.getBomComponentId(), mtEoBom.getBomId());
                if (CollectionUtils.isEmpty(primaryMaterial)) {
                    throw new MtException("HME_NC_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0037", "HME"));
                } else if (primaryMaterial.size() > 1) {
                    throw new MtException("HME_NC_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0038", "HME"));
                }
                MtBomComponent primaryBomComponent = mtBomComponentRepository.selectByPrimaryKey(primaryMaterial.get(0));
                //?????????????????????
                HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", mtBomComponent, hmeNcScrapDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
                //????????????????????? ????????????????????????material_id??????????????????????????????????????????????????????
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
                //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //???????????????bomComponentId?????????????????????lineAttribute4
                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setKeyId(primaryBomComponent.getBomComponentId());
                mtExtendVO.setTableName("mt_bom_component_attr");
                mtExtendVO.setAttrName("lineAttribute5");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                }
                BigDecimal lineAttribute = lineAttribute5;
                if (material.getApplyQty().compareTo(lineAttribute.subtract(scrapQty)) <= 0) {
                    //??????????????????????????????lineAttribute5-scrapQty,????????????????????????????????????????????????????????? ??????????????????????????????
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //??????????????????????????????
                    HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                    primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryInAssemble.setEventId(dto.getScrapTransactionId());
                    primaryInAssemble.setTransactionQty(material.getApplyQty());
                    primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                } else {
                    //????????????????????????lineAttribute-scrapQty,?????????????????????
                    if(lineAttribute.compareTo(scrapQty) == 1){
                        //??????lineAttribute??????scrapQty???????????????????????????,??????????????????????????????
                        //lineAttribute-scrapQty???HME_WO_ISSUE_R_EXT???????????????
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.??????alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //lineAttribute-scrapQty???HME_WO_ISSUE???????????????
                        HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                        primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryInAssemble.setEventId(dto.getScrapTransactionId());
                        primaryInAssemble.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                    }
                }
            }
        } else {
            //????????????????????????BOM???,?????????????????????????????????
            MtBomComponent primaryBomComponent = getPrimaryMaterial(tenantId, material.getMaterialId(), mtEo, dto.getProcessId());
            if (Objects.isNull(primaryBomComponent)) {
                //???????????????????????????????????? ????????????????????? ???????????????HME_WO_ISSUE_EXT???????????????
                HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", null, hmeNcScrapDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
            } else {
                //????????????????????? ???????????????HME_WO_ISSUE_EXT???????????????
                HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", null, hmeNcScrapDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
                //????????????????????? ????????????????????????material_id??????????????????????????????????????????????????????
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
                //?????????????????????material_id??????????????????work_order_id?????????Id????????????mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //???????????????bomComponentId?????????????????????lineAttribute4
                BigDecimal lineAttribute5 = BigDecimal.ZERO;
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setKeyId(primaryBomComponent.getBomComponentId());
                mtExtendVO.setTableName("mt_bom_component_attr");
                mtExtendVO.setAttrName("lineAttribute5");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                    lineAttribute5 = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
                }
                BigDecimal lineAttribute = lineAttribute5;
                if (material.getApplyQty().compareTo(lineAttribute.subtract(scrapQty)) <= 0) {
                    //???????????????????????????lineAttribute-scrapQty, ?????????????????????
                    //???????????????HME_WO_ISSUE_R_EXT???????????????
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryOutReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //???????????????HME_WO_ISSUE???????????????
                    HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                    primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryInAssemble.setEventId(dto.getScrapTransactionId());
                    primaryInAssemble.setTransactionQty(material.getApplyQty());
                    primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                } else {
                    //?????????????????????lineAttribute-scrapQty, ?????????????????????
                    if(lineAttribute.compareTo(scrapQty) == 1){
                        //??????lineAttribute??????scrapQty???????????????????????????,??????????????????????????????
                        //lineAttribute-scrapQty???HME_WO_ISSUE_R_EXT???????????????
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.??????alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //lineAttribute-scrapQty???HME_WO_ISSUE???????????????
                        HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                        primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryInAssemble.setEventId(dto.getScrapTransactionId());
                        primaryInAssemble.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                    }
                }
            }
        }
    }

    /**
     * Bom???????????????????????????  Bom??????????????????????????? ???????????????????????????
     *
     * @param tenantId            ??????ID
     * @param transactionTypeCode ??????????????????
     * @param mtBomComponent      Bom??????
     * @param dto                 ????????????
     * @return com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 11:02:32
     */
    WmsObjectTransactionRequestVO returnTransaction(Long tenantId, String transactionTypeCode, MtBomComponent mtBomComponent, HmeNcDisposePlatformDTO34 dto) {
        WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        objectTransactionRequestVO.setTransactionTypeCode(transactionTypeCode);
        objectTransactionRequestVO.setEventId(dto.getEventId());
        objectTransactionRequestVO.setMaterialLotId(dto.getMaterialLotId());
        objectTransactionRequestVO.setMaterialId(dto.getMaterialId());
        objectTransactionRequestVO.setTransactionQty(dto.getTransactionQty());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
        objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
        objectTransactionRequestVO.setTransactionTime(new Date());
        objectTransactionRequestVO.setTransactionReasonCode("????????????");
        objectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
        objectTransactionRequestVO.setLocatorId(mtMaterialLot.getLocatorId());
        List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, mtMaterialLot.getLocatorId(), "TOP");
        if (CollectionUtils.isNotEmpty(pLocatorIds)) {
            MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
            if (ploc != null) {
                objectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
            }
        }
        objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        objectTransactionRequestVO.setMergeFlag(NO);
        objectTransactionRequestVO.setMoveType("262");
        objectTransactionRequestVO.setMoveReason("????????????");
        if ("HME_WO_ISSUE_R".equals(transactionTypeCode)) {
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(mtBomComponent.getBomComponentId());
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                objectTransactionRequestVO.setBomReserveNum(mtExtendAttrVOS.get(0).getAttrValue());
            }
            objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            String lineAttribute11 = null;
            mtExtendVO.setAttrName("lineAttribute11");
            mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                lineAttribute11 = mtExtendAttrVOS.get(0).getAttrValue();
            }
            if (E.equals(lineAttribute11)) {
                MtExtendVO mtWorkOrderExtend = new MtExtendVO();
                mtWorkOrderExtend.setTableName("mt_work_order_attr");
                mtWorkOrderExtend.setKeyId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderExtend.setAttrName("attribute1");
                List<MtExtendAttrVO> mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
                mtWorkOrderExtend.setAttrName("attribute7");
                mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoLineNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
            }
        }
        return objectTransactionRequestVO;
    }

    /**
     * Bom???????????????????????????  Bom??????????????????????????? ???????????????????????????
     *
     * @param tenantId            ??????ID
     * @param transactionTypeCode ??????????????????
     * @param mtBomComponent      Bom??????
     * @param dto                 ????????????
     * @return com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 11:06:50
     */
    WmsObjectTransactionRequestVO scrapTransaction(Long tenantId, String transactionTypeCode, MtBomComponent mtBomComponent, HmeNcDisposePlatformDTO34 dto) {
        WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        objectTransactionRequestVO.setTransactionTypeCode(transactionTypeCode);
        objectTransactionRequestVO.setEventId(dto.getEventId());
        objectTransactionRequestVO.setMaterialLotId(dto.getMaterialLotId());
        objectTransactionRequestVO.setMaterialId(dto.getMaterialId());
        objectTransactionRequestVO.setTransactionQty(dto.getTransactionQty());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
        objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
        objectTransactionRequestVO.setTransactionTime(new Date());
        objectTransactionRequestVO.setTransactionReasonCode("??????????????????");
        objectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
        objectTransactionRequestVO.setLocatorId(mtMaterialLot.getLocatorId());
        List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, mtMaterialLot.getLocatorId(), "TOP");
        if (CollectionUtils.isNotEmpty(pLocatorIds)) {
            MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
            if (ploc != null) {
                objectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
            }
        }
        objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        objectTransactionRequestVO.setMergeFlag(NO);
        objectTransactionRequestVO.setMoveType("261");
        objectTransactionRequestVO.setMoveReason("??????????????????");
        if ("HME_WO_ISSUE".equals(transactionTypeCode)) {
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(mtBomComponent.getBomComponentId());
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                objectTransactionRequestVO.setBomReserveNum(mtExtendAttrVOS.get(0).getAttrValue());
            }
            objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            String lineAttribute11 = null;
            mtExtendVO.setAttrName("lineAttribute11");
            mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                lineAttribute11 = mtExtendAttrVOS.get(0).getAttrValue();
            }
            if (E.equals(lineAttribute11)) {
                MtExtendVO mtWorkOrderExtend = new MtExtendVO();
                mtWorkOrderExtend.setTableName("mt_work_order_attr");
                mtWorkOrderExtend.setKeyId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderExtend.setAttrName("attribute1");
                List<MtExtendAttrVO> mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
                mtWorkOrderExtend.setAttrName("attribute7");
                mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoLineNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
            }
        }
        return objectTransactionRequestVO;
    }

    /**
     * Bom??????????????????????????? ???????????????????????????
     *
     * @param tenantId            ??????ID
     * @param transactionTypeCode ??????????????????
     * @param mtBomComponent      Bom??????
     * @param dto                 ????????????
     * @return com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 11:11:04
     */
    WmsObjectTransactionRequestVO primaryReturnTransaction(Long tenantId, String transactionTypeCode, MtBomComponent mtBomComponent, HmeNcDisposePlatformDTO34 dto) {
        WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        objectTransactionRequestVO.setTransactionTypeCode(transactionTypeCode);
        objectTransactionRequestVO.setEventId(dto.getEventId());
        objectTransactionRequestVO.setMaterialId(mtBomComponent.getMaterialId());
        objectTransactionRequestVO.setTransactionQty(dto.getTransactionQty());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtBomComponent.getMaterialId());
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
        objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
        objectTransactionRequestVO.setTransactionTime(new Date());
        objectTransactionRequestVO.setTransactionReasonCode("????????????");
        objectTransactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
        //2020-01-07 edit by chaonan.hu for can.wang ????????????????????????????????????ISSUE_WAREHOUSE_CODE??????
        String warehouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
        if (StringUtils.isEmpty(warehouseCode)) {
            throw new MtException("HME_NC_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0056", "HME"));
        }
        MtModLocator warehouse = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setTenantId(tenantId);
            setLocatorCode(warehouseCode);
        }});
        if(Objects.isNull(warehouse)){
            throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0057", "HME"));
        }
        objectTransactionRequestVO.setWarehouseId(warehouse.getLocatorId());
        List<String> locatorIdList = hmeNcDisposePlatformMapper.getSubLocatorByLocatorId(tenantId, warehouse.getLocatorId());
        if(CollectionUtils.isEmpty(locatorIdList) || locatorIdList.size() > 1){
            throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0057", "HME"));
        }
        objectTransactionRequestVO.setLocatorId(locatorIdList.get(0));
        objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        objectTransactionRequestVO.setMergeFlag(NO);
        objectTransactionRequestVO.setMoveType("262");
        objectTransactionRequestVO.setMoveReason("????????????");
        if ("HME_WO_ISSUE_R".equals(transactionTypeCode)) {
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(mtBomComponent.getBomComponentId());
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                objectTransactionRequestVO.setBomReserveNum(mtExtendAttrVOS.get(0).getAttrValue());
            }
            objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            String lineAttribute11 = null;
            mtExtendVO.setAttrName("lineAttribute11");
            mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                lineAttribute11 = mtExtendAttrVOS.get(0).getAttrValue();
            }
            if (E.equals(lineAttribute11)) {
                MtExtendVO mtWorkOrderExtend = new MtExtendVO();
                mtWorkOrderExtend.setTableName("mt_work_order_attr");
                mtWorkOrderExtend.setKeyId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderExtend.setAttrName("attribute1");
                List<MtExtendAttrVO> mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
                mtWorkOrderExtend.setAttrName("attribute7");
                mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoLineNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
            }
        }
        return objectTransactionRequestVO;
    }

    /**
     * Bom??????????????????????????? ???????????????????????????
     *
     * @param tenantId            ??????ID
     * @param transactionTypeCode ??????????????????
     * @param mtBomComponent      Bom??????
     * @param dto                 ????????????
     * @return com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 11:11:04
     */
    WmsObjectTransactionRequestVO primaryAssembleTransaction(Long tenantId, String transactionTypeCode, MtBomComponent mtBomComponent, HmeNcDisposePlatformDTO34 dto) {
        WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        objectTransactionRequestVO.setTransactionTypeCode(transactionTypeCode);
        objectTransactionRequestVO.setEventId(dto.getEventId());
        objectTransactionRequestVO.setMaterialId(mtBomComponent.getMaterialId());
        objectTransactionRequestVO.setTransactionQty(dto.getTransactionQty());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtBomComponent.getMaterialId());
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
        objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
        objectTransactionRequestVO.setTransactionTime(new Date());
        objectTransactionRequestVO.setTransactionReasonCode("??????????????????");
        objectTransactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
        //2020-01-07 edit by chaonan.hu for can.wang ????????????????????????????????????ISSUE_WAREHOUSE_CODE??????
        String warehouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
        if (StringUtils.isEmpty(warehouseCode)) {
            throw new MtException("HME_NC_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0056", "HME"));
        }
        MtModLocator warehouse = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setTenantId(tenantId);
            setLocatorCode(warehouseCode);
        }});
        if(Objects.isNull(warehouse)){
            throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0057", "HME"));
        }
        objectTransactionRequestVO.setWarehouseId(warehouse.getLocatorId());
        List<String> locatorIdList = hmeNcDisposePlatformMapper.getSubLocatorByLocatorId(tenantId, warehouse.getLocatorId());
        if(CollectionUtils.isEmpty(locatorIdList) || locatorIdList.size() > 1){
            throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0057", "HME"));
        }
        objectTransactionRequestVO.setLocatorId(locatorIdList.get(0));
        objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        objectTransactionRequestVO.setMergeFlag(NO);
        objectTransactionRequestVO.setMoveType("261");
        objectTransactionRequestVO.setMoveReason("??????????????????");
        if ("HME_WO_ISSUE".equals(transactionTypeCode)) {
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(mtBomComponent.getBomComponentId());
            mtExtendVO.setTableName("mt_bom_component_attr");
            mtExtendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                objectTransactionRequestVO.setBomReserveNum(mtExtendAttrVOS.get(0).getAttrValue());
            }
            objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            String lineAttribute11 = null;
            mtExtendVO.setAttrName("lineAttribute11");
            mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                lineAttribute11 = mtExtendAttrVOS.get(0).getAttrValue();
            }
            if (E.equals(lineAttribute11)) {
                MtExtendVO mtWorkOrderExtend = new MtExtendVO();
                mtWorkOrderExtend.setTableName("mt_work_order_attr");
                mtWorkOrderExtend.setKeyId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderExtend.setAttrName("attribute1");
                List<MtExtendAttrVO> mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
                mtWorkOrderExtend.setAttrName("attribute7");
                mtWorkOrderExtendAttr =
                        mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtWorkOrderExtend);
                if (CollectionUtils.isNotEmpty(mtWorkOrderExtendAttr)) {
                    objectTransactionRequestVO.setSoLineNum(mtWorkOrderExtendAttr.get(0).getAttrValue());
                }
            }
        }
        return objectTransactionRequestVO;
    }

    /**
     * BOM??? ????????????????????????
     *
     * @param tenantId             ??????ID
     * @param substituteMaterialId ?????????ID
     * @param mtEo                 eo
     * @param mtEo                 eoj
     * @param processId            ??????ID
     * @return tarzan.method.domain.entity.MtBomComponent
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/16 09:49:33
     */
    MtBomComponent getPrimaryMaterial(Long tenantId, String substituteMaterialId, MtEo mtEo, String processId) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
        MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
            setTenantId(tenantId);
            setEoId(mtEo.getEoId());
        }});
        //????????????Id????????????Id?????????wms_material_substitute_rel??????SUBSTITUTE_GROUP
        List<WmsMaterialSubstituteRel> wmsMaterialSubstituteRelList = wmsMaterialSubstituteRelRepository.select(new WmsMaterialSubstituteRel() {{
            setTenantId(tenantId);
            setSiteId(mtWorkOrder.getSiteId());
            setMaterialId(substituteMaterialId);
        }});
        if (CollectionUtils.isEmpty(wmsMaterialSubstituteRelList)) {
            return null;
        } else if (wmsMaterialSubstituteRelList.size() > 1) {
            //??????????????????????????????{????????????????????????????????????????????????????????????}
            throw new MtException("HME_NC_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0043", "HME"));
        }
        //????????????????????????Id??????????????????????????????
        List<String> substituteMaterialList = hmeNcDisposePlatformMapper.getSubstituteMaterialByGroup(tenantId, wmsMaterialSubstituteRelList.get(0).getSubstituteGroup());
        //????????????????????????Id???bomId??????mt_bom_component???????????????
        List<MtBomComponent> mtBomComponentList = new ArrayList<>();
        for (String substituteMaterial : substituteMaterialList) {
            List<MtBomComponent> mtBomComponents = mtBomComponentRepository.select(new MtBomComponent() {{
                setTenantId(tenantId);
                setMaterialId(substituteMaterial);
                setBomId(mtEoBom.getBomId());
            }});
            if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                mtBomComponentList.addAll(mtBomComponents);
            }
        }
        if (CollectionUtils.isEmpty(mtBomComponentList)) {
            //??????????????????????????????
            return null;
        } else if (mtBomComponentList.size() == 1) {
            //????????????????????????????????????
            return mtBomComponentList.get(0);
        } else {
            //???????????????????????????????????????,??????????????????mt_operation_wkc_dispatch_rel??????operation_id
            String operationId = null;
            List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
                setTenantId(tenantId);
                setWorkcellId(processId);
            }});
            if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
                //????????????????????????{???????????????????????????,?????????}
                throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0044", "HME"));
            } else if (operationWkcDispatchRelList.size() > 1) {
                //???????????????????????????{??????????????????????????????,?????????}
                throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0045", "HME"));
            }
            operationId = operationWkcDispatchRelList.get(0).getOperationId();
            //??????eoId??????router_step_id
            List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, mtEo.getEoId());
            //????????????routerStepId???operationId?????????mt_router_operation???ROUTER_OPERATION_ID
            String routerOperationId = null;
            List<MtRouterOperation> mtRouterOperationList = new ArrayList<>();
            MtRouterOperation mtRouterOperation = new MtRouterOperation();
            mtRouterOperation.setTenantId(tenantId);
            mtRouterOperation.setOperationId(operationId);
            for (String routerStepId : routerStepIdList) {
                mtRouterOperation.setRouterStepId(routerStepId);
                MtRouterOperation mtRouterOperationDb = mtRouterOperationRepository.selectOne(mtRouterOperation);
                if (Objects.nonNull(mtRouterOperationDb)) {
                    mtRouterOperationList.add(mtRouterOperationDb);
                }
            }
            if (CollectionUtils.isEmpty(mtRouterOperationList)) {
                //????????????????????????{?????????????????????????????????????????????}
                throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0046", "HME"));
            } else if (mtRouterOperationList.size() > 1) {
                //???????????????????????????{????????????????????????????????????????????????}
                throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0047", "HME"));
            }
            routerOperationId = mtRouterOperationList.get(0).getRouterOperationId();
            //????????????bomComponentId???routerOperationId?????????mt_router_operation_component
            List<MtRouterOperationComponent> mtRouterOperationComponentList = new ArrayList<>();
            for (MtBomComponent mtBomComponent : mtBomComponentList) {
                MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
                mtRouterOperationComponent.setTenantId(tenantId);
                mtRouterOperationComponent.setBomComponentId(mtBomComponent.getBomComponentId());
                mtRouterOperationComponent.setRouterOperationId(routerOperationId);
                MtRouterOperationComponent mtRouterOperationComponentDb = mtRouterOperationComponentRepository.selectOne(mtRouterOperationComponent);
                if (Objects.nonNull(mtRouterOperationComponentDb)) {
                    mtRouterOperationComponentList.add(mtRouterOperationComponentDb);
                }
            }
            if (CollectionUtils.isEmpty(mtRouterOperationComponentList)) {
                //???????????????????????????{?????????????????????????????????????????????,?????????}
                throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_073", "HME"));
            } else if (mtRouterOperationComponentList.size() > 1) {
                //???????????????????????????{????????????????????????????????????????????????????????????}
                throw new MtException("HME_NC_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0048", "HME"));
            }
            String bomComponentId = mtRouterOperationComponentList.get(0).getBomComponentId();
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            return mtBomComponent;
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param tenantId   ??????Id
     * @param eoId       eoId
     * @param materialId ??????Id
     * @param processId  ??????Id
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO35
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/23 13:52:41
     */
    HmeNcDisposePlatformDTO35 getAllMaterialByPrimary(Long tenantId, String eoId, String materialId, String processId) {
        HmeNcDisposePlatformDTO35 result = new HmeNcDisposePlatformDTO35();
        List<String> allMaterialList = new ArrayList<>();
        allMaterialList.add(materialId);
        String bomComponentId = null;
        List<String> bomComponentIdList = hmeNcDisposePlatformMapper.getBomComponentIdByMaterial(tenantId, eoId, materialId);
        if (CollectionUtils.isEmpty(bomComponentIdList)) {
            throw new MtException("HME_NC_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0054", "HME"));
        } else if (bomComponentIdList.size() == 1) {
            bomComponentId = bomComponentIdList.get(0);
        } else {
            //??????????????????mt_operation_wkc_dispatch_rel??????operation_id
            String operationId = null;
            List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
                setTenantId(tenantId);
                setWorkcellId(processId);
            }});
            if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
                //????????????????????????{???????????????????????????,?????????}
                throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0044", "HME"));
            } else if (operationWkcDispatchRelList.size() > 1) {
                //???????????????????????????{??????????????????????????????,?????????}
                throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0045", "HME"));
            }
            operationId = operationWkcDispatchRelList.get(0).getOperationId();
            //??????eoId??????router_step_id
            List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, eoId);
            //????????????routerStepId???operationId?????????mt_router_operation???ROUTER_OPERATION_ID
            String routerOperationId = null;
            List<MtRouterOperation> mtRouterOperationList = new ArrayList<>();
            MtRouterOperation mtRouterOperation = new MtRouterOperation();
            mtRouterOperation.setTenantId(tenantId);
            mtRouterOperation.setOperationId(operationId);
            for (String routerStepId2 : routerStepIdList) {
                mtRouterOperation.setRouterStepId(routerStepId2);
                MtRouterOperation mtRouterOperationDb = mtRouterOperationRepository.selectOne(mtRouterOperation);
                if (Objects.nonNull(mtRouterOperationDb)) {
                    mtRouterOperationList.add(mtRouterOperationDb);
                }
            }
            if (CollectionUtils.isEmpty(mtRouterOperationList)) {
                //????????????????????????{?????????????????????????????????????????????}
                throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0046", "HME"));
            } else if (mtRouterOperationList.size() > 1) {
                //???????????????????????????{????????????????????????????????????????????????}
                throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0047", "HME"));
            }
            routerOperationId = mtRouterOperationList.get(0).getRouterOperationId();
            //????????????bomComponentId???routerOperationId?????????mt_router_operation_component
            List<MtRouterOperationComponent> mtRouterOperationComponentList = new ArrayList<>();
            for (String bomComponentId2 : bomComponentIdList) {
                MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
                mtRouterOperationComponent.setTenantId(tenantId);
                mtRouterOperationComponent.setBomComponentId(bomComponentId2);
                mtRouterOperationComponent.setRouterOperationId(routerOperationId);
                MtRouterOperationComponent mtRouterOperationComponentDb = mtRouterOperationComponentRepository.selectOne(mtRouterOperationComponent);
                if (Objects.nonNull(mtRouterOperationComponentDb)) {
                    mtRouterOperationComponentList.add(mtRouterOperationComponentDb);
                }
            }
            if (CollectionUtils.isEmpty(mtRouterOperationComponentList)) {
                //???????????????????????????{?????????????????????????????????????????????,?????????}
                throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_073", "HME"));
            } else if (mtRouterOperationComponentList.size() > 1) {
                //???????????????????????????{????????????????????????????????????????????????????????????}
                throw new MtException("HME_NC_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0048", "HME"));
            }
            bomComponentId = mtRouterOperationComponentList.get(0).getBomComponentId();
        }
        MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
            setTenantId(tenantId);
            setEoId(eoId);
        }});
        //???????????????bom?????????????????????
        List<String> woSubstituteMaterial = hmeNcDisposePlatformMapper.getWoSubstituteByPrimary(tenantId, bomComponentId, mtEoBom.getBomId());
        allMaterialList.addAll(woSubstituteMaterial);
        //????????????????????????????????????
        List<String> globalSubstituteMaterial = hmeNcDisposePlatformMapper.getGlobalSubstituteByPrimary(tenantId, materialId);
        allMaterialList.addAll(globalSubstituteMaterial);
        allMaterialList = allMaterialList.stream().distinct().collect(Collectors.toList());
        result.setAllMaterialList(allMaterialList);
        String routerStepId = null;
        //??????????????????mt_operation_wkc_dispatch_rel??????operation_id
        String operationId = null;
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
            setTenantId(tenantId);
            setWorkcellId(processId);
        }});
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
            //????????????????????????{???????????????????????????,?????????}
            throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0044", "HME"));
        } else if (operationWkcDispatchRelList.size() > 1) {
            //???????????????????????????{??????????????????????????????,?????????}
            throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0045", "HME"));
        }
        operationId = operationWkcDispatchRelList.get(0).getOperationId();
        //??????woId??????router_step_id
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
        List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByWo(tenantId, mtEo.getWorkOrderId());
        //????????????routerStepId???operationId?????????mt_router_operation???ROUTER_OPERATION_ID
        List<MtRouterOperation> mtRouterOperationList = new ArrayList<>();
        MtRouterOperation mtRouterOperation = new MtRouterOperation();
        mtRouterOperation.setTenantId(tenantId);
        mtRouterOperation.setOperationId(operationId);
        for (String routerStepId2 : routerStepIdList) {
            mtRouterOperation.setRouterStepId(routerStepId2);
            MtRouterOperation mtRouterOperationDb = mtRouterOperationRepository.selectOne(mtRouterOperation);
            if (Objects.nonNull(mtRouterOperationDb)) {
                mtRouterOperationList.add(mtRouterOperationDb);
            }
        }
        if (CollectionUtils.isEmpty(mtRouterOperationList)) {
            //????????????????????????{?????????????????????????????????????????????}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        } else if (mtRouterOperationList.size() > 1) {
            //???????????????????????????{????????????????????????????????????????????????}
            throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0047", "HME"));
        }
        routerStepId = mtRouterOperationList.get(0).getRouterStepId();
        result.setRouterStepId(routerStepId);
        return result;
    }

    /**
     * ?????????????????????????????????????????????eo?????????????????????
     *
     * @param tenantId ??????ID
     * @param eoId eoId
     * @param materialId ??????Id
     * @param processId ??????Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/23 14:01:23
     * @return java.lang.String
     */
    String getBomComponentIdByMaterial(Long tenantId, String eoId, String materialId, String processId) {
        List<String> bomComponentIdList = hmeNcDisposePlatformMapper.getBomComponentIdByMaterial(tenantId, eoId, materialId);
        if (CollectionUtils.isEmpty(bomComponentIdList)) {
            return null;
        } else if (bomComponentIdList.size() == 1) {
            return bomComponentIdList.get(0);
        } else {
            //??????????????????mt_operation_wkc_dispatch_rel??????operation_id
            String operationId = null;
            List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
                setTenantId(tenantId);
                setWorkcellId(processId);
            }});
            if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
                //????????????????????????{???????????????????????????,?????????}
                throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0044", "HME"));
            } else if (operationWkcDispatchRelList.size() > 1) {
                //???????????????????????????{??????????????????????????????,?????????}
                throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0045", "HME"));
            }
            operationId = operationWkcDispatchRelList.get(0).getOperationId();
            //??????eoId??????router_step_id
            List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, eoId);
            //????????????routerStepId???operationId?????????mt_router_operation???ROUTER_OPERATION_ID
            String routerOperationId = null;
            List<MtRouterOperation> mtRouterOperationList = new ArrayList<>();
            MtRouterOperation mtRouterOperation = new MtRouterOperation();
            mtRouterOperation.setTenantId(tenantId);
            mtRouterOperation.setOperationId(operationId);
            for (String routerStepId2 : routerStepIdList) {
                mtRouterOperation.setRouterStepId(routerStepId2);
                MtRouterOperation mtRouterOperationDb = mtRouterOperationRepository.selectOne(mtRouterOperation);
                if (Objects.nonNull(mtRouterOperationDb)) {
                    mtRouterOperationList.add(mtRouterOperationDb);
                }
            }
            if (CollectionUtils.isEmpty(mtRouterOperationList)) {
                //????????????????????????{?????????????????????????????????????????????}
                throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0046", "HME"));
            } else if (mtRouterOperationList.size() > 1) {
                //???????????????????????????{????????????????????????????????????????????????}
                throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0047", "HME"));
            }
            routerOperationId = mtRouterOperationList.get(0).getRouterOperationId();
            //????????????bomComponentId???routerOperationId?????????mt_router_operation_component
            List<MtRouterOperationComponent> mtRouterOperationComponentList = new ArrayList<>();
            for (String bomComponentId2 : bomComponentIdList) {
                MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
                mtRouterOperationComponent.setTenantId(tenantId);
                mtRouterOperationComponent.setBomComponentId(bomComponentId2);
                mtRouterOperationComponent.setRouterOperationId(routerOperationId);
                MtRouterOperationComponent mtRouterOperationComponentDb = mtRouterOperationComponentRepository.selectOne(mtRouterOperationComponent);
                if (Objects.nonNull(mtRouterOperationComponentDb)) {
                    mtRouterOperationComponentList.add(mtRouterOperationComponentDb);
                }
            }
            if (CollectionUtils.isEmpty(mtRouterOperationComponentList)) {
                //???????????????????????????{?????????????????????????????????????????????,?????????}
                throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_073", "HME"));
            } else if (mtRouterOperationComponentList.size() > 1) {
                //???????????????????????????{????????????????????????????????????????????????????????????}
                throw new MtException("HME_NC_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0048", "HME"));
            }
            return mtRouterOperationComponentList.get(0).getBomComponentId();
        }
    }

    /**
     * ??????API{ onhandQtyUpdateProcess }??????????????????
     *
     * @param tenantId ??????ID
     * @param materialLotId ??????ID
     * @param applyQty ????????????
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/7 14:43:09
     * @return void
     */
    void locatorTransfer(Long tenantId, String materialLotId, BigDecimal applyQty, String eventRequestId){
        //???????????????????????????????????????
        List<String> ncStorageLocatorIdList = hmeNcDisposePlatformMapper.getNcStorageByLocatorId(tenantId, materialLotId);
        if(CollectionUtils.isEmpty(ncStorageLocatorIdList) || ncStorageLocatorIdList.size() > 1){
            throw new MtException("HME_NC_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0058", "HME"));
        }
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
        mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
        mtInvOnhandQuantityVO9.setLocatorId(ncStorageLocatorIdList.get(0));
        mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
        mtInvOnhandQuantityVO9.setChangeQuantity(applyQty.doubleValue());
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventRequestId(eventRequestId);
        mtEventCreateVO.setEventTypeCode("NC_RECORD_JUDGE");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        mtInvOnhandQuantityVO9.setEventId(eventId);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
    }
}
