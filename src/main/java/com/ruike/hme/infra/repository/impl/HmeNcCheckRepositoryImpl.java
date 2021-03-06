package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.app.service.ItfRepairWorkOrderCreateService;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.entity.WmsMaterialSubstituteRel;
import com.ruike.wms.domain.repository.WmsMaterialLotFrozenRepository;
import com.ruike.wms.domain.repository.WmsMaterialSubstituteRelRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtNcIncident;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.actual.domain.entity.MtNcRecordHis;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtNcRecordMapper;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.MtRouterVO1;
import tarzan.modeling.domain.entity.*;
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
import tarzan.order.domain.vo.MtEoVO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static io.tarzan.common.domain.util.MtBaseConstants.QUALITY_STATUS.OK;

/**
 * HmeNcCheckRepositoryImpl
 *
 * @author: chaonan.hu@hand-china.com 2020-07-03 10:17:26
 **/
@Component
public class HmeNcCheckRepositoryImpl implements HmeNcCheckRepository {

    @Autowired
    private HmeNcDisposePlatformMapper hmeNcDisposePlatformMapper;
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
    private MtNcRecordRepository mtNcRecordRepository;
    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;
    @Autowired
    private MtNcGroupRepository mtNcGroupRepository;
    @Autowired
    private MtEoActualRepository mtEoActualRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeNcCheckMapper hmeNcCheckMapper;
    @Autowired
    private MtNcRecordMapper mtNcRecordMapper;
    @Autowired
    private MtNcRecordHisRepository mtNcRecordHisRepository;
    @Autowired
    private HmeNcRecordAttrRepository hmeNcRecordAttrRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;
    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;
    @Autowired
    private MtWorkOrderComponentActualRepository woComponentActualRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeNcDisposePlatformRepository hmeNcDisposePlatformRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private WmsMaterialLotFrozenRepository wmsMaterialLotFrozenRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private MtEoBomRepository mtEoBomRepository;
    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;
    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;
    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    @Autowired
    private WmsMaterialSubstituteRelRepository wmsMaterialSubstituteRelRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;
    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;
    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private HmeRepairWorkOrderCreateRepository hmeRepairWorkOrderCreateRepository;
    @Autowired
    private ItfRepairWorkOrderCreateService itfRepairWorkOrderCreateService;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private HmeEoRouterBomRelRepository hmeEoRouterBomRelRepository;
    @Autowired
    private MtRouterRepository mtRouterRepository;
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private MtNcIncidentRepository mtNcIncidentRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private HmeNcRecordAttrMapper hmeNcRecordAttrMapper;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;
    @Autowired
    private HmeMaterialLotNcLoadMapper hmeMaterialLotNcLoadMapper;
    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;
    @Autowired
    private HmeSelectionDetailsMapper hmeSelectionDetailsMapper;

    @Autowired
    private HmeEoJobSnReWorkMapper hmeEoJobSnReWorkMapper;
    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;

    private static final String[] IGNORE_TABLE_FIELDS = new String[]{MtMaterialLot.FIELD_IDENTIFICATION,
            AuditDomain.FIELD_OBJECT_VERSION_NUMBER, AuditDomain.FIELD_LAST_UPDATED_BY, AuditDomain.FIELD_CREATED_BY,
            AuditDomain.FIELD_CREATION_DATE, AuditDomain.FIELD_LAST_UPDATE_DATE};

    @Override
    public Page<HmeNcDisposePlatformDTO2> ncRecordQuery(Long tenantId, HmeNcCheckDTO dto, PageRequest pageRequest) {
        List<String> workcellIdList = new ArrayList<>();
        Boolean flag = false;
        Boolean flag1 = false;
        // ?????????????????? ??????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getAreaId()) && StringUtils.isBlank(dto.getProdLineId()) && StringUtils.isBlank(dto.getProcessId()) && StringUtils.isBlank(dto.getWorkcellId())) {
            flag = true;
            //??????API {subOrganizationRelQuery} ??????????????????????????????
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setParentOrganizationId(dto.getAreaId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //?????????????????????????????????
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //?????????????????????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getProdLineId())
                && StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            flag = true;
            //??????API {subOrganizationRelQuery} ??????????????????????????????
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationVO2.setParentOrganizationId(dto.getProdLineId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //??????????????????????????????
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //?????????????????????????????????????????????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getProcessId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            flag = true;
            //??????API {subOrganizationRelQuery} ??????????????????????????????
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
            mtModOrganizationVO2.setParentOrganizationId(dto.getProcessId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //?????????????????????
        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            flag1 = true;
            //??????????????????????????????mt_material_lot???material_lot_id
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(
                    Sqls.custom().andLikeRight(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getMaterialLotCode())
            ).build());
            dto.setMaterialLotIdList(mtMaterialLots.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList()));
        }
        //????????????
        //????????????????????????????????????????????????????????????????????????????????????Id???????????????????????????
        if ((flag && CollectionUtils.isEmpty(dto.getWorkcellIdList())) || (flag1 && CollectionUtils.isEmpty(dto.getMaterialLotIdList()))) {
            return new Page<HmeNcDisposePlatformDTO2>(Collections.EMPTY_LIST, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), 0);
        }
        Page<HmeNcDisposePlatformDTO2> ncRecordList = PageHelper.doPage(pageRequest, () -> hmeNcDisposePlatformMapper.ncRecordQuery(tenantId, dto));
        for (HmeNcDisposePlatformDTO2 hmeNcDisposePlatformDTO2 : ncRecordList) {
            //?????? ??????ROOT_CAUSE_WORKCELL_ID??????API{parentOrganizationRelQuery}
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(hmeNcDisposePlatformDTO2.getSiteId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setOrganizationId(hmeNcDisposePlatformDTO2.getWorkcellId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setQueryType("ALL");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
                if ("CJ".equals(mtModArea.getAreaCategory())) {
                    hmeNcDisposePlatformDTO2.setWorkShopName(mtModArea.getAreaName());
                    break;
                }
            }
            //?????????
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
                hmeNcDisposePlatformDTO2.setProdLineName(mtModProductionLine.getDescription());
            }
            //???????????????
            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
                if ("LINE".equals(mtModWorkcell.getWorkcellType())) {
                    hmeNcDisposePlatformDTO2.setLineWorkcellName(mtModWorkcell.getWorkcellName());
                } else if ("PROCESS".equals(mtModWorkcell.getWorkcellType())) {
                    hmeNcDisposePlatformDTO2.setProcessName(mtModWorkcell.getWorkcellName());
                }
            }
            //?????? ??????ROOT_CAUSE_WORKCELL_ID???WORKCELL_TYPE = STATION?????? mt_mod_workcell??????WORKCELL_NAME
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getWorkcellId());
            hmeNcDisposePlatformDTO2.setWorkcellName(mtModWorkcell.getWorkcellName());
            //?????????
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getCreatedBy());
            hmeNcDisposePlatformDTO2.setUserName(mtUserInfo.getRealName());
            //??????
            hmeNcDisposePlatformDTO2.setUserCode(mtUserInfo.getLoginName());
            //????????? MATERIAL_LOT_ID??????mt_material_lot??????material_lot_code?????????????????????material_Lot_id
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getMaterialLotId());
            if (mtMaterialLot != null && StringUtils.isNotBlank(mtMaterialLot.getMaterialLotCode())) {
                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            } else {
                hmeNcDisposePlatformDTO2.setMaterialLotCode(hmeNcDisposePlatformDTO2.getMaterialLotId());
            }
            //????????? ??????eo_id?????????mt_eo?????????work_order_id?????????work_order_id??????mt_work_order??????WORK_ORDER_NUM
//            MtEo mtEo = mtEoRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getEoId());
//            if (mtEo != null && StringUtils.isNotBlank(mtEo.getWorkOrderId())) {
//                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
//                if (mtWorkOrder != null) {
//                    hmeNcDisposePlatformDTO2.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
//                }
//            }
            //???????????? ????????????
            //??????mt_nc_record?????????PARENT_NC_RECORD_ID = NC_RECORD_ID????????????
            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, hmeNcDisposePlatformDTO2.getNcRecordId());
            List<String> ncCodeIdList = new ArrayList<>();
            List<String> ncCodeList = new ArrayList<>();
            List<String> ncReasonList = new ArrayList<>();
            //???????????????
            if (CollectionUtils.isNotEmpty(mtNcRecordList) && StringUtils.isNotEmpty(mtNcRecordList.get(0).getComments())) {
                hmeNcDisposePlatformDTO2.setDisposeComment(mtNcRecordList.get(0).getComments());
            } else {
                HmeNcDisposePlatformDTO26 hmeNcDisposePlatformDTO26 = hmeNcCheckMapper.commentsQuery(tenantId, hmeNcDisposePlatformDTO2.getMaterialId(), hmeNcDisposePlatformDTO2.getNcCodeId(),
                        hmeNcDisposePlatformDTO2.getRootCauseOperationId(), hmeNcDisposePlatformDTO2.getEoStepActualId());
                if (hmeNcDisposePlatformDTO26 != null) {
                    hmeNcDisposePlatformDTO2.setComments(hmeNcDisposePlatformDTO26.getComments());
                } else {
                    hmeNcDisposePlatformDTO2.setDisposeComment("");
                }
            }
            for (MtNcRecord mtNcRecord : mtNcRecordList) {
                if (StringUtils.isNotEmpty(mtNcRecord.getNcCodeId())) {
                    //??????NC_CODE_ID?????????mt_nc_code??????nc_code
                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
                    ncCodeIdList.add(mtNcCode.getNcCodeId());
                    ncCodeList.add(mtNcCode.getNcCode());
                    ncReasonList.add(mtNcCode.getDescription());
                }
            }
            hmeNcDisposePlatformDTO2.setNcCodeIdList(ncCodeIdList);
            hmeNcDisposePlatformDTO2.setNcCodeList(ncCodeList);
            hmeNcDisposePlatformDTO2.setNcReasonList(ncReasonList);
            //???????????????  ????????????
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getNcCodeId());
//                hmeNcDisposePlatformDTO2.setNcGroupDesc(mtNcGroup.getDescription());
            hmeNcDisposePlatformDTO2.setNcType(mtNcGroup.getComponentRequired());
            String meaning = lovAdapter.queryLovMeaning("HME.NC_TYPE", tenantId, mtNcGroup.getComponentRequired());
            hmeNcDisposePlatformDTO2.setNcTypeMeaning(meaning);
            //???????????? MATERIAL_ID??????mt_material??????material_code
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getMaterialId());
            hmeNcDisposePlatformDTO2.setMaterialCode(mtMaterial.getMaterialCode());
            //????????????
            hmeNcDisposePlatformDTO2.setMaterialName(mtMaterial.getMaterialName());
            //????????????
//            MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getNcIncidentId());
//            hmeNcDisposePlatformDTO2.setNcNumber(mtNcIncident.getIncidentNumber());
            //????????????
            hmeNcDisposePlatformDTO2.setDateTime(hmeNcDisposePlatformDTO2.getDateTime());
            //????????????
            if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO2.getRootCauseWorkcellId())) {
                MtModWorkcell mtModWorkcell2 = mtModWorkcellRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getRootCauseWorkcellId());
                hmeNcDisposePlatformDTO2.setResponseWorkcellName(mtModWorkcell2.getWorkcellName());
            }
            //?????????
            MtUserInfo mtUserInfo2 = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getUserId());
            hmeNcDisposePlatformDTO2.setResponseUser(mtUserInfo2.getRealName());
            //??????
            hmeNcDisposePlatformDTO2.setStatus(hmeNcDisposePlatformDTO2.getNcStatus());
            MtGenStatus statusMeaning = mtGenStatusRepository.getGenStatus(tenantId, "NC_RECORD", "NC_RECORD_STATUS", hmeNcDisposePlatformDTO2.getNcStatus());
            hmeNcDisposePlatformDTO2.setStatusMeaning(statusMeaning.getDescription());
            //2020-08-28 add by chaonan.hu for zhenyun.ban ??????????????????????????????
            if (hmeNcDisposePlatformDTO2.getClosedUserId() != null) {
                MtUserInfo mtUserInfo3 = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getClosedUserId());
                hmeNcDisposePlatformDTO2.setDisposeUserId(hmeNcDisposePlatformDTO2.getClosedUserId().toString());
                hmeNcDisposePlatformDTO2.setDisposeUserName(mtUserInfo3.getRealName());
                hmeNcDisposePlatformDTO2.setDisposeDateTime(hmeNcDisposePlatformDTO2.getClosedDateTime());
            }
            //????????????
            HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrRepository.selectOne(new HmeNcRecordAttr() {{
                setTenantId(tenantId);
                setParentRecordId(hmeNcDisposePlatformDTO2.getNcRecordId());
            }});
            if (hmeNcRecordAttr != null) {
                hmeNcDisposePlatformDTO2.setDisposeMethod(hmeNcRecordAttr.getProcessMethod());
                String disposeMethodMeaning = lovAdapter.queryLovMeaning("HME.NC_PROCESS_METHOD", tenantId, hmeNcRecordAttr.getProcessMethod());
                hmeNcDisposePlatformDTO2.setDisposeMethodMeaning(disposeMethodMeaning);
                //????????????
                //2020-08-25 15:06 add by chaonan.hu for lu.bai ??????????????????????????????
                if (StringUtils.isNotEmpty(hmeNcRecordAttr.getAttribute1())) {
                    MtMaterial mtMaterialDb = mtMaterialRepository.selectByPrimaryKey(hmeNcRecordAttr.getAttribute1());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialId(hmeNcRecordAttr.getAttribute1());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialCode(mtMaterialDb.getMaterialCode());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialName(mtMaterialDb.getMaterialName());
                }
            }
            //2020-09-28 add by chaonan.hu for lu.bai ???????????????????????????????????????
            if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO2.getComponentMaterialId())) {
                MtMaterial mtMaterial2 = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getComponentMaterialId());
                hmeNcDisposePlatformDTO2.setScrapMaterialId(mtMaterial2.getMaterialId());
                hmeNcDisposePlatformDTO2.setScrapMaterialCode(mtMaterial2.getMaterialCode());
                hmeNcDisposePlatformDTO2.setScrapMaterialName(mtMaterial2.getMaterialName());
                hmeNcDisposePlatformDTO2.setScrapQty(hmeNcDisposePlatformDTO2.getQty() == null ? BigDecimal.ZERO : new BigDecimal(hmeNcDisposePlatformDTO2.getQty()));
            }
            // ????????????
            if (hmeNcDisposePlatformDTO2.getLoadRow() != null && hmeNcDisposePlatformDTO2.getLoadColumn() != null) {
                hmeNcDisposePlatformDTO2.setCosPosition(String.valueOf((char)(hmeNcDisposePlatformDTO2.getLoadRow()+64)) + hmeNcDisposePlatformDTO2.getLoadColumn());
            }
        }
        return ncRecordList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ncCheck(Long tenantId, HmeNcCheckDTO2 dto) {
        MtNcRecord mtNcRecord = mtNcRecordRepository.selectByPrimaryKey(dto.getNcRecordId());
        //????????????
        if (!HmeConstants.NcStatus.OPEN.equals(mtNcRecord.getNcStatus())) {
            throw new MtException("HME_NC_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0007", "HME"));
        }
        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CLOSE");

        //????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("NC_RECORD_CLOSE");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //??????????????????
        if (!dto.getNcGroupId().equals(mtNcRecord.getNcCodeId())) {
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????ncCodeId
            mtNcRecordMapper.updateByPrimaryKeySelective(new MtNcRecord() {{
                setTenantId(tenantId);
                setNcRecordId(dto.getNcRecordId());
                setNcCodeId(dto.getNcGroupId());
            }});
            MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
            BeanUtils.copyProperties(mtNcRecord, mtNcRecordHis);
            mtNcRecordHis.setNcCodeId(dto.getNcGroupId());
            mtNcRecordHis.setEventId(eventId);
            mtNcRecordHisRepository.insertSelective(mtNcRecordHis);
        }
        //??????????????????????????????????????????????????????
        List<HmeNcRecordAttr> recordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
            setTenantId(tenantId);
            setParentRecordId(mtNcRecord.getNcRecordId());
        }});
        if(CollectionUtils.isNotEmpty(recordAttrList)){
            //????????? ?????????
            HmeNcRecordAttr hmeNcRecordAttr = recordAttrList.get(0);
            hmeNcRecordAttr.setProcessMethod(dto.getProcessMethod());
            if ("4".equals(dto.getProcessMethod())) {
                hmeNcRecordAttr.setAttribute1(dto.getTransitionMaterialId());
            }
            if (StringUtils.isNotBlank(dto.getDispositionFunctionId())) {
                hmeNcRecordAttr.setAttribute4(dto.getDispositionFunctionId());
            }
            // ????????????????????? ???Attribute6???Y
            if (YES.equals(dto.getReworkRecordFlag())) {
                hmeNcRecordAttr.setAttribute6(YES);
            }
            hmeNcRecordAttrMapper.updateByPrimaryKeySelective(hmeNcRecordAttr);
        }else {
            //???????????????
            HmeNcRecordAttr hmeNcRecordAttr = new HmeNcRecordAttr();
            hmeNcRecordAttr.setTenantId(tenantId);
            hmeNcRecordAttr.setParentRecordId(dto.getNcRecordId());
            hmeNcRecordAttr.setProcessMethod(dto.getProcessMethod());
            if ("4".equals(dto.getProcessMethod())) {
                hmeNcRecordAttr.setAttribute1(dto.getTransitionMaterialId());
            }
            if (StringUtils.isNotBlank(dto.getDispositionFunctionId())) {
                hmeNcRecordAttr.setAttribute4(dto.getDispositionFunctionId());
            }
            // ????????????????????? ???Attribute6???Y
            if (YES.equals(dto.getReworkRecordFlag())) {
                hmeNcRecordAttr.setAttribute6(YES);
            }
            hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);
        }
        //??????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
        MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
        mtNcRecordVO2.setNcRecordId(dto.getNcRecordId());
        mtNcRecordVO2.setEventRequestId(eventRequestId);
        mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
        //???????????????????????????????????????
        //????????????????????????Id???????????????
        List<HmeNcCheckVO2> childNcRecordIdList = hmeNcCheckMapper.childNcRecordIdQuery(tenantId, dto.getNcRecordId());
        for (HmeNcCheckVO2 childNcRecord : childNcRecordIdList) {
            if (dto.getNcCodeIdList().contains(childNcRecord.getNcCodeId())) {
                //??????????????????????????????????????????????????????????????????????????????
                if (StringUtils.isNotEmpty(dto.getComment())) {
                    MtNcRecordVO7 mtNcRecordVO7 = new MtNcRecordVO7();
                    mtNcRecordVO7.setNcRecordId(childNcRecord.getNcRecordId());
                    mtNcRecordVO7.setComments(dto.getComment());
                    mtNcRecordVO7.setEventId(eventId);
                    mtNcRecordRepository.ncRecordAndHisUpdate(tenantId, mtNcRecordVO7);
                }
                //??????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                mtNcRecordVO2.setNcRecordId(childNcRecord.getNcRecordId());
                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
            } else {
                //?????????????????????????????????????????????????????????????????????????????????
                MtNcRecordVO2 mtNcRecordVO21 = new MtNcRecordVO2();
                mtNcRecordVO21.setNcRecordId(childNcRecord.getNcRecordId());
                mtNcRecordVO21.setParentEventId(eventId);
                mtNcRecordVO21.setEventRequestId(eventRequestId);
                mtNcRecordRepository.ncRecordCancel(tenantId, mtNcRecordVO21);
            }
        }
        //??????????????????????????????????????????????????????
        MtNcRecordVO3 mtNcRecordVO3 = new MtNcRecordVO3();
        mtNcRecordVO3.setEoId(mtNcRecord.getEoId());
        mtNcRecordVO3.setParentNcRecordId(mtNcRecord.getNcRecordId());
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        mtNcRecordVO3.setUserId(userId);
        mtNcRecordVO3.setNcIncidentId(mtNcRecord.getNcIncidentId());
        if (StringUtils.isNotEmpty(dto.getComment())) {
            mtNcRecordVO3.setComments(dto.getComment());
        }
        mtNcRecordVO3.setComponentMaterialId(mtNcRecord.getComponentMaterialId());
        mtNcRecordVO3.setMaterialLotId(mtNcRecord.getMaterialLotId());
        mtNcRecordVO3.setEoStepActualId(mtNcRecord.getEoStepActualId());
        mtNcRecordVO3.setRootCauseOperationId(mtNcRecord.getRootCauseOperationId());
        mtNcRecordVO3.setRootCauseWorkcellId(mtNcRecord.getRootCauseWorkcellId());
        mtNcRecordVO3.setEventId(eventId);
        for (String ncCodeId : dto.getNcCodeIdList()) {
            List<String> ncCodeIdList = childNcRecordIdList.stream().map(HmeNcCheckVO2::getNcCodeId).collect(Collectors.toList());
            if (!ncCodeIdList.contains(ncCodeId)) {
                //??????????????????????????????????????????????????????????????????
                mtNcRecordVO3.setNcCodeId(ncCodeId);
                MtNcRecordVO8 mtNcRecordVO8 = mtNcRecordRepository.ncRecordAndHisCreate(tenantId, mtNcRecordVO3);
                //??????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                mtNcRecordVO2.setNcRecordId(mtNcRecordVO8.getNcRecordId());
                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
            }
        }
        //????????????
        if (HmeConstants.ProcessStatus.ONE.equals(dto.getProcessMethod())) {
            // 2021-01-07 add by sanfeng.zhang for wang.can ????????????EO???????????????
            String reworkFlag = hmeNcCheckMapper.queryReworkFlagByEoId(tenantId, mtNcRecord.getEoId());
            if (YES.equals(reworkFlag)) {
                throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0049", "HME"));
            }
            //2020-08-25 add by chaonan.hu for lu.bai ???????????????????????????????????????????????????????????????
            updateNoSiteOutData(tenantId, mtNcRecord.getMaterialLotId(), userId);

            //?????????????????????????????????API{materialLotAttrPropertyUpdate}???????????????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(mtNcRecord.getMaterialLotId());
            mtExtendVO10.setEventId(eventId);
            List<MtExtendVO5> attrList = new ArrayList<>();
            // 2021/02/07 add by sanfeng.zhang wang.can ?????????????????? ????????????????????????
            if (!YES.equals(dto.getReworkRecordFlag())) {
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("REWORK_FLAG");
                mtExtendVO5.setAttrValue(HmeConstants.ConstantValue.YES);
                attrList.add(mtExtendVO5);
            }
            //2020/12/03 add by sanfeng.zhang for malifeng ???????????????
            if (StringUtils.isNotBlank(dto.getRouterId())) {
                MtExtendVO5 reworkAttr = new MtExtendVO5();
                reworkAttr.setAttrName("REWORK_ROUTER");
                reworkAttr.setAttrValue(dto.getRouterId());
                attrList.add(reworkAttr);

                // 2021/1/6 add by sanfeng.zahng for xietianyang ????????????????????????
                MtExtendVO5 flagAttr = new MtExtendVO5();
                flagAttr.setAttrName("DESIGNED_REWORK_FLAG");
                flagAttr.setAttrValue(YES);
                attrList.add(flagAttr);
            }
            mtExtendVO10.setAttrs(attrList);
            if (CollectionUtils.isNotEmpty(attrList)) {
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
            }
            // 2021/1/6 add by sanfeng.zhang for xietianyang ????????????EO????????????????????????????????????hme_eo_router_bom_rel
            if (StringUtils.isNotBlank(dto.getRouterId())) {
                this.bindEoRouterBomRel(tenantId, mtNcRecord, dto.getRouterId());
            }
            //2020-09-17 20:49 add by chgaonan.hu for zhenyong.ban ???????????????????????????????????????
            hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getProcessMethod())) {
            //?????????????????????
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
            if (NO.equals(mtNcGroup.getComponentRequired())) {
                // 2021-01-07 add by sanfeng.zhang for wang.can ????????????EO???????????????
                String reworkFlag = hmeNcCheckMapper.queryReworkFlagByEoId(tenantId, mtNcRecord.getEoId());
                if (YES.equals(reworkFlag)) {
                    throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0049", "HME"));
                }
                //???????????????????????????????????????API{materialLotUpdate}???????????????????????????
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(mtNcRecord.getMaterialLotId());
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setQualityStatus(OK);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            } else if (YES.equals(mtNcGroup.getComponentRequired())) {
                //?????????????????????
                Boolean feedFlag = judgeFeedByMaterialLot(tenantId, mtNcRecord.getEoId(), mtNcRecord.getMaterialLotId());
                //??????????????????
                Boolean handleFlag;
                //?????????????????????
                List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
                    setTenantId(tenantId);
                    setParentRecordId(mtNcRecord.getNcRecordId());
                }});
                String returnMaterialFlag = CollectionUtils.isNotEmpty(ncRecordAttrList) ? ncRecordAttrList.get(0).getAttribute2() : "";
                if (feedFlag) {
                    if (StringUtils.equals(returnMaterialFlag, YES)) {
                        //?????????????????? ?????????
                        handleFlag = false;
                    } else {
                        //?????????????????? ???????????? ?????? ?????????????????????
                        handleFlag = true;
                    }

                } else {
                    if (StringUtils.equals(returnMaterialFlag, YES)) {
                        //?????????????????? ?????????
                        handleFlag = false;
                    } else {
                        //????????? ???????????? ?????? ?????????????????????
                        handleFlag = true;
                    }
                }
                if (handleFlag) {
                    //????????????
                    MtMaterialLot mtMaterialLot1 = null;
                    if(StringUtils.isNotBlank(dto.getBarcode())){
                        mtMaterialLot1 = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getBarcode());
                    }
                    //???????????????????????????
                    String barcodeId = this.handleScanMaterialLot(tenantId, mtMaterialLot1, mtMaterialLot, mtNcRecord.getQty(), dto.getBarcode(), eventId, "FX");

                    List<HmeNcRecordAttr> hmeNcRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
                        setTenantId(tenantId);
                        setParentRecordId(mtNcRecord.getNcRecordId());
                    }});
                    if(CollectionUtils.isNotEmpty(hmeNcRecordAttrList)){
                        HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrList.get(0);
                        hmeNcRecordAttr.setAttribute3(barcodeId);
                        hmeNcRecordAttrMapper.updateByPrimaryKeySelective(hmeNcRecordAttr);
                    }

                    //??????API{onhandQtyUpdateProcess}??????????????????
                    MtEventCreateVO mtEventCreateVO2 = new MtEventCreateVO();
                    mtEventCreateVO2.setEventTypeCode("NC_RECORD_MATERIAL_FX");
                    mtEventCreateVO2.setEventRequestId(eventRequestId);
                    String eventId2 = mtEventRepository.eventCreate(tenantId, mtEventCreateVO2);
                    MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                    mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                    mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                    mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
                    mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                    mtInvOnhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                    mtInvOnhandQuantityVO9.setEventId(eventId2);
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                    // ?????????????????????????????? ???????????????????????????
                    String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                    // ??????API{onhandQtyUpdateProcess}?????????????????????
                    MtEventCreateVO returnEventVO = new MtEventCreateVO();
                    returnEventVO.setEventTypeCode("NC_RECORD_MATERIAL_CODE_RETURN");
                    returnEventVO.setEventRequestId(eventRequestId);
                    String returnEvent = mtEventRepository.eventCreate(tenantId, returnEventVO);
                    MtInvOnhandQuantityVO9 onhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                    onhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                    onhandQuantityVO9.setMaterialId(mtNcRecord.getComponentMaterialId());
                    onhandQuantityVO9.setLocatorId(locatorId);
                    onhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                    onhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                    onhandQuantityVO9.setEventId(returnEvent);
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, onhandQuantityVO9);
                }
            }
        } else if (HmeConstants.ProcessStatus.THREE.equals(dto.getProcessMethod())) {
            //?????????????????????
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
            if (NO.equals(mtNcGroup.getComponentRequired())) {
                //???????????????????????????????????????API{eoScrap}??????????????????API{materialLotUpdate}????????????
                MtEoActualVO6 mtEoActualVO6 = new MtEoActualVO6();
                mtEoActualVO6.setEoId(mtNcRecord.getEoId());
                mtEoActualVO6.setTrxScrappedQty(mtMaterialLot.getPrimaryUomQty());
                mtEoActualVO6.setParentEventId(eventId);
                mtEoActualRepository.eoScrap(tenantId, mtEoActualVO6);
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(mtNcRecord.getMaterialLotId());
                mtMaterialLotVO2.setEnableFlag(NO);
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
                //2020-09-08 21:00 add by chaonan.hu for lu.bai ?????????????????????????????????
                updateNoSiteOutData(tenantId, mtNcRecord.getMaterialLotId(), userId);
                //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban ???????????????????????????????????????
                hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
            } else if (YES.equals(mtNcGroup.getComponentRequired())) {
                //?????????????????????
                //2020-09-09 11:10 add by chaonan.hu for lu.bai ??????????????????????????????
                materialScrap(tenantId, mtMaterialLot, mtNcRecord, eventId, eventRequestId);
            }
        } else if ("4".equals(dto.getProcessMethod())) {
            //2020-08-25 add by chaonan.hu for lu.bai ???????????????????????????
            if (StringUtils.isEmpty(dto.getTransitionMaterialId())) {
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "????????????"));
            }
            //????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(mtNcRecord.getMaterialLotId());
            mtExtendVO10.setEventId(eventId);
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("PERFORMANCE_LEVEL");
            //?????????????????????ID
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            mtExtendVO5.setAttrValue(mtMaterialLot.getMaterialId());
            mtExtendVO10.setAttrs(Collections.singletonList(mtExtendVO5));
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(mtNcRecord.getMaterialLotId());
            //????????????ID
            mtMaterialLotVO2.setMaterialId(dto.getTransitionMaterialId());
            mtMaterialLotVO2.setQualityStatus(OK);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            //EO??????
            MtEoVO mtEoVO = new MtEoVO();
            mtEoVO.setEventId(eventId);
            mtEoVO.setEoId(mtNcRecord.getEoId());
            mtEoVO.setMaterialId(dto.getTransitionMaterialId());
            mtEoRepository.eoUpdate(tenantId, mtEoVO, NO);
        } else if (HmeConstants.ProcessStatus.FIVE.equals(dto.getProcessMethod())) {
            //???????????? ????????????????????????
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
            if (YES.equals(mtNcGroup.getComponentRequired())) {
                //?????????????????????
                Boolean feedFlag = judgeFeedByMaterialLot(tenantId, mtNcRecord.getEoId(), mtNcRecord.getMaterialLotId());
                //??????????????????
                Boolean handleFlag;
                //?????????????????????
                List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
                    setTenantId(tenantId);
                    setParentRecordId(mtNcRecord.getNcRecordId());
                }});
                String returnMaterialFlag = CollectionUtils.isNotEmpty(ncRecordAttrList) ? ncRecordAttrList.get(0).getAttribute2() : "";
                if (feedFlag) {
                    if (StringUtils.equals(returnMaterialFlag, YES)) {
                        //?????????????????? ?????????
                        handleFlag = false;
                    } else {
                        //?????????????????? ???????????? ?????? ?????????????????????
                        handleFlag = true;
                    }
                } else {
                    if (StringUtils.equals(returnMaterialFlag, YES)) {
                        //?????????????????? ?????????
                        handleFlag = false;
                    } else {
                        //????????? ???????????? ?????? ?????????????????????
                        handleFlag = true;
                    }
                }
                if (handleFlag) {
                    //????????????
                    MtMaterialLot materialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
                    //????????????
                    MtMaterialLot mtMaterialLot = null;
                    if(StringUtils.isNotBlank(dto.getBarcode())){
                        mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getBarcode());
                    }
                    //???????????????????????????
                    String barcodeId = this.handleScanMaterialLot(tenantId, mtMaterialLot, materialLot, mtNcRecord.getQty(), dto.getBarcode(), eventId, "TK");
                    List<HmeNcRecordAttr> hmeNcRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
                        setTenantId(tenantId);
                        setParentRecordId(mtNcRecord.getNcRecordId());
                    }});
                    if(CollectionUtils.isNotEmpty(hmeNcRecordAttrList)){
                        HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrList.get(0);
                        hmeNcRecordAttr.setAttribute3(barcodeId);
                        hmeNcRecordAttrMapper.updateByPrimaryKeySelective(hmeNcRecordAttr);
                    }
                    //??????????????????
                    MtEventCreateVO mtEventCreateVO2 = new MtEventCreateVO();
                    mtEventCreateVO2.setEventTypeCode("NC_RECORD_MATERIAL_TK");
                    mtEventCreateVO2.setEventRequestId(eventRequestId);
                    String eventId2 = mtEventRepository.eventCreate(tenantId, mtEventCreateVO2);
                    //???????????????
                    MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                    mtInvOnhandQuantityVO9.setSiteId(materialLot.getSiteId());
                    mtInvOnhandQuantityVO9.setMaterialId(materialLot.getMaterialId());
                    mtInvOnhandQuantityVO9.setLocatorId(materialLot.getLocatorId());
                    mtInvOnhandQuantityVO9.setLotCode(materialLot.getLot());
                    mtInvOnhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                    mtInvOnhandQuantityVO9.setEventId(eventId2);
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                    // ?????????????????????????????? ???????????????????????????
                    String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                    // ??????API{onhandQtyUpdateProcess}?????????????????????
                    MtEventCreateVO returnEventVO = new MtEventCreateVO();
                    returnEventVO.setEventTypeCode("NC_RECORD_MATERIAL_CODE_RETURN");
                    returnEventVO.setEventRequestId(eventRequestId);
                    String returnEvent = mtEventRepository.eventCreate(tenantId, returnEventVO);
                    MtInvOnhandQuantityVO9 onhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                    onhandQuantityVO9.setSiteId(materialLot.getSiteId());
                    onhandQuantityVO9.setMaterialId(mtNcRecord.getComponentMaterialId());
                    onhandQuantityVO9.setLocatorId(locatorId);
                    onhandQuantityVO9.setLotCode(materialLot.getLot());
                    onhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                    onhandQuantityVO9.setEventId(returnEvent);
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, onhandQuantityVO9);
                }
            }

        } else if (HmeConstants.ProcessStatus.SIX.equals(dto.getProcessMethod())) {
            // ??????????????? (??????????????????)
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtNcRecord.getComponentMaterialId());
            // ??????????????????1 ?????????
            if (mtNcRecord.getQty().compareTo(1D) != 0) {
                throw new MtException("HME_NC_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0051", "HME", mtMaterial != null ? mtMaterial.getMaterialName() : ""));
            }
            // ????????????????????????????????????
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            // ?????????????????????API{materialLotUpdate}
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotVO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            mtMaterialLotVO2.setSiteId(mtMaterialLot.getSiteId());
            mtMaterialLotVO2.setEnableFlag(YES);
            mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
            mtMaterialLotVO2.setTrxPrimaryUomQty(mtNcRecord.getQty());
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            // ????????????????????????Y
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(eventId);
            mtExtendVO10.setKeyId(mtMaterialLot.getMaterialLotId());
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("REWORK_FLAG");
            mtExtendVO5.setAttrValue(YES);
            attrList.add(mtExtendVO5);
            mtExtendVO10.setAttrs(attrList);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

            List<HmeRepairWorkOrderCreate> workOrderCreateList = hmeRepairWorkOrderCreateRepository.select(new HmeRepairWorkOrderCreate() {{
                setTenantId(tenantId);
                setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            }});

            if (CollectionUtils.isNotEmpty(workOrderCreateList)) {
                // ???????????? ????????????????????? ???NEW/RELEASED/EORELEASED ?????????
                List<MtWorkOrder> mtWorkOrderList = mtWorkOrderRepository.select(new MtWorkOrder() {{
                    setTenantId(tenantId);
                    setWorkOrderNum(workOrderCreateList.get(0).getWorkOrderNum());
                }});
                if (CollectionUtils.isNotEmpty(mtWorkOrderList)) {
                    MtWorkOrder mtWorkOrder = mtWorkOrderList.get(0);
                    if (HmeConstants.WorkOrderStatus.NEW.equals(mtWorkOrder.getStatus()) || HmeConstants.WorkOrderStatus.RELEASED.equals(mtWorkOrder.getStatus()) || HmeConstants.WorkOrderStatus.EORELEASED.equals(mtWorkOrder.getStatus())) {
                        throw new MtException("HME_NC_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0052", "HME", mtMaterialLot.getMaterialLotCode()));
                    }
                }
            }

            // ??????SAP???????????????????????????????????????????????????
            HmeRepairWorkOrderCreate workOrderCreate = new HmeRepairWorkOrderCreate();
            workOrderCreate.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            workOrderCreate.setQty(mtNcRecord.getQty());
            if (mtMaterial != null) {
                workOrderCreate.setMaterialCode(mtMaterial.getMaterialCode());
                // ????????????
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                workOrderCreate.setPrimaryUomCode(mtUom != null ? mtUom.getUomCode() : "");
            }
            // ??????
            MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(mtNcRecord.getSiteId());
            workOrderCreate.setSiteCode(mtModSite != null ? mtModSite.getSiteCode() : "");
            // ???????????? (?????????????????????????????????mt_mod_organization_rel???????????????)
            List<MtModLocator> locatorList = hmeNcCheckMapper.queryLocatorIdByWorkcellId(tenantId, mtNcRecord.getWorkcellId());
            String warehouseCode = "";
            if (CollectionUtils.isNotEmpty(locatorList)) {
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorList.get(0).getParentLocatorId());
                warehouseCode = mtModLocator != null ? mtModLocator.getLocatorCode() : "";
            }
            workOrderCreate.setLocatorCode(warehouseCode);
            Date startDate = new Date();
            workOrderCreate.setPlanStartTime(startDate);
            workOrderCreate.setPlanEndTime(DateUtils.addDays(startDate, 30));
            // ???????????????????????????????????????
            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.WO_PRODUCTION_VERSION", tenantId);
            if (CollectionUtils.isNotEmpty(valueDTOList)) {
                workOrderCreate.setProductionVersion(valueDTOList.get(0).getValue());
            }
            HmeRepairWorkOrderCreate resultData = itfRepairWorkOrderCreateService.hmeRepairWorkOrderCreateService(tenantId, workOrderCreate);
            // ????????????????????????, ???????????????????????????????????????,????????????
            if (StringUtils.isBlank(resultData.getStatus())) {
                throw new MtException("HME_NC_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0053", "HME"));
            }
            // ??????????????????E, ???????????????????????? ??????????????????
            if (StringUtils.equals(resultData.getStatus(), HmeConstants.ConstantValue.E)) {
                throw new CommonException(resultData.getMessage());
            }

            List<HmeNcRecordAttr> hmeNcRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
                setTenantId(tenantId);
                setParentRecordId(mtNcRecord.getNcRecordId());
            }});
            if (CollectionUtils.isNotEmpty(hmeNcRecordAttrList)) {
                HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrList.get(0);
                hmeNcRecordAttr.setAttribute5(resultData.getWorkOrderNum());
                hmeNcRecordAttrMapper.updateByPrimaryKeySelective(hmeNcRecordAttr);
            }

            // ?????????????????????????????? ???????????????????????????
            String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
            // ??????API{onhandQtyUpdateProcess}?????????????????????
            MtEventCreateVO returnEventVO = new MtEventCreateVO();
            returnEventVO.setEventTypeCode("NC_RECORD_MATERIAL_CODE_RETURN");
            returnEventVO.setEventRequestId(eventRequestId);
            String returnEvent = mtEventRepository.eventCreate(tenantId, returnEventVO);
            MtInvOnhandQuantityVO9 onhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            onhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
            onhandQuantityVO9.setMaterialId(mtNcRecord.getComponentMaterialId());
            onhandQuantityVO9.setLocatorId(locatorId);
            onhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
            onhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
            onhandQuantityVO9.setEventId(returnEvent);
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, onhandQuantityVO9);

            // ??????????????????????????????????????? ????????????
            String recordEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO(){{
                setEventRequestId(eventRequestId);
                setEventTypeCode("NC_RECORD_SELFMADE_MATERIAL_REPAIR");
            }});
            // ?????????????????????API???onhandQtyUpdateProcess
            MtInvOnhandQuantityVO9 recordQuantity = new MtInvOnhandQuantityVO9();
            recordQuantity.setSiteId(mtMaterialLot.getSiteId());
            recordQuantity.setMaterialId(mtMaterialLot.getMaterialId());
            recordQuantity.setLocatorId(mtMaterialLot.getLocatorId());
            recordQuantity.setLotCode(mtMaterialLot.getLot());
            recordQuantity.setChangeQuantity(mtNcRecord.getQty());
            recordQuantity.setEventId(recordEventId);
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, recordQuantity);
        }
    }

    public static <E, T> void copyPropertiesIgnoreNullAndTableFields(E src, T target) {
        // ????????????????????????????????????ID???????????????????????????
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(Arrays.asList(WmsCommonUtils.getNullPropertyNames(src)));
        hashSet.addAll(Arrays.asList(IGNORE_TABLE_FIELDS));
        String[] result = new String[hashSet.size()];
        BeanUtils.copyProperties(src, target, hashSet.toArray(result));
    }

    /**
     * ?????????????????????
     *
     * @param tenantId
     * @param inputMaterialLot
     * @param sourceMaterialLot
     * @param qty
     * @param materialLotCode
     * @param eventId
     * @param flag
     * @author sanfeng.zhang@hand-china.com 2020/11/25 19:30
     * @return void
     */
    private String handleScanMaterialLot(Long tenantId, MtMaterialLot inputMaterialLot ,MtMaterialLot sourceMaterialLot, Double qty, String materialLotCode, String eventId, String flag){
        //??????API{materialLotUpdate}????????????  ????????????????????? ??????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        if (StringUtils.isBlank(materialLotCode)) {
            // ?????????????????????
            copyPropertiesIgnoreNullAndTableFields(sourceMaterialLot, mtMaterialLotVO2);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setMaterialLotId(null);
            mtMaterialLotVO2.setMaterialLotCode(null);
            mtMaterialLotVO2.setEnableFlag(YES);
            mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
            mtMaterialLotVO2.setPrimaryUomQty(qty);
            mtMaterialLotVO2.setCurrentContainerId(null);
            mtMaterialLotVO2.setTopContainerId(null);
            mtMaterialLotVO2.setCreateReason("SPLIT");
        } else {
            if (inputMaterialLot == null) {
                copyPropertiesIgnoreNullAndTableFields(sourceMaterialLot, mtMaterialLotVO2);
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(null);
                mtMaterialLotVO2.setMaterialLotCode(materialLotCode);
                mtMaterialLotVO2.setEnableFlag(YES);
                mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO2.setPrimaryUomQty(qty);
                mtMaterialLotVO2.setCurrentContainerId(null);
                mtMaterialLotVO2.setTopContainerId(null);
                mtMaterialLotVO2.setCreateReason("SPLIT");
            } else if (NO.equals(inputMaterialLot.getEnableFlag())) {
                //???????????? ????????????0
                inputMaterialLot.setPrimaryUomQty(0D);
                mtMaterialLotMapper.updateByPrimaryKeySelective(inputMaterialLot);
                //????????????
                this.materialLotSiteInVerify(tenantId, inputMaterialLot, sourceMaterialLot, flag);
                copyPropertiesIgnoreNullAndTableFields(sourceMaterialLot, mtMaterialLotVO2);
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(inputMaterialLot.getMaterialLotId());
                mtMaterialLotVO2.setMaterialLotCode(inputMaterialLot.getMaterialLotCode());
                mtMaterialLotVO2.setSiteId(inputMaterialLot.getSiteId());
                mtMaterialLotVO2.setEnableFlag(YES);
                mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO2.setPrimaryUomQty(qty);
                mtMaterialLotVO2.setCurrentContainerId(null);
                mtMaterialLotVO2.setTopContainerId(null);
            } else if (YES.equals(inputMaterialLot.getEnableFlag())) {
                //??????????????????
                this.materialLotSiteInVerify(tenantId, inputMaterialLot, sourceMaterialLot, flag);
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(inputMaterialLot.getMaterialLotId());
                mtMaterialLotVO2.setMaterialLotCode(inputMaterialLot.getMaterialLotCode());
                mtMaterialLotVO2.setSiteId(inputMaterialLot.getSiteId());
                mtMaterialLotVO2.setEnableFlag(YES);
                mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO2.setMaterialId(inputMaterialLot.getMaterialId());
                mtMaterialLotVO2.setPrimaryUomId(sourceMaterialLot.getPrimaryUomId());
                mtMaterialLotVO2.setTrxPrimaryUomQty(qty);
                mtMaterialLotVO2.setLocatorId(inputMaterialLot.getLocatorId());
                mtMaterialLotVO2.setSupplierId(sourceMaterialLot.getSupplierId());
                mtMaterialLotVO2.setEoId(sourceMaterialLot.getEoId());
                mtMaterialLotVO2.setCurrentContainerId(sourceMaterialLot.getCurrentContainerId());
                mtMaterialLotVO2.setTopContainerId(sourceMaterialLot.getTopContainerId());
            }
        }
        MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);

        if (StringUtils.isEmpty(materialLotCode) || inputMaterialLot == null || StringUtils.equals(NO, inputMaterialLot.getEnableFlag())) {
            this.materialLotAttrUpdate(tenantId, materialLotCode, mtMaterialLotVO13.getMaterialLotId(), inputMaterialLot, sourceMaterialLot, eventId, flag);
        }
        return mtMaterialLotVO13.getMaterialLotId();
    }

    /**
     * ??????????????????
     *
     * @param materialLotCode
     * @param inputMaterialLot    ??????
     * @param sourceMaterialLot  ??????
     * @author sanfeng.zhang@hand-china.com 2020/11/25 18:15
     * @return void
     */
    private void materialLotAttrUpdate(Long tenantId, String materialLotCode, String materialLotId, MtMaterialLot inputMaterialLot, MtMaterialLot sourceMaterialLot,String eventId, String flag){
        //??????????????????
        //????????????????????????
        MtExtendVO1 mtExtendVO = new MtExtendVO1();
        mtExtendVO.setTableName("mt_material_lot_attr");
        mtExtendVO.setKeyIdList(Collections.singletonList(sourceMaterialLot.getMaterialLotId()));
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);

        MtExtendVO10 extendVO10 = new MtExtendVO10();
        extendVO10.setEventId(eventId);
        extendVO10.setKeyId(materialLotId);
        List<MtExtendVO5> attrs = new ArrayList<>();

        if(StringUtils.isBlank(materialLotCode) || inputMaterialLot == null){
            if(CollectionUtils.isNotEmpty(extendAttrVO1List)){
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    if(StringUtils.equals("STATUS", mtExtendAttrVO1.getAttrName()) || StringUtils.equals("NC_SUPPLIER_REPLACEMENT", mtExtendAttrVO1.getAttrName()) || StringUtils.equals("ORIGINAL_ID", mtExtendAttrVO1.getAttrName())){
                        continue;
                    }
                    MtExtendVO5 extendVO5 = new MtExtendVO5();
                    extendVO5.setAttrName(mtExtendAttrVO1.getAttrName());
                    extendVO5.setAttrValue(mtExtendAttrVO1.getAttrValue());
                    attrs.add(extendVO5);
                }
            }

            MtExtendVO5 attr1 = new MtExtendVO5();
            attr1.setAttrName("STATUS");
            attr1.setAttrValue(HmeConstants.StatusCode.NEW);
            if (StringUtils.equals(flag, "TK")) {
                attr1.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
            }
            attrs.add(attr1);

            //?????? ??????????????????
            if (StringUtils.equals(flag, "TK")) {
                MtExtendVO5 attr2 = new MtExtendVO5();
                attr2.setAttrName("NC_SUPPLIER_REPLACEMENT");
                attr2.setAttrValue(YES);
                attrs.add(attr2);
            }
            MtExtendVO5 attr5 = new MtExtendVO5();
            attr5.setAttrName("ORIGINAL_ID");
            attr5.setAttrValue(sourceMaterialLot.getMaterialLotId());
            attrs.add(attr5);
        } else if (NO.equals(inputMaterialLot.getEnableFlag())) {
            if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    if (StringUtils.equals("STATUS", mtExtendAttrVO1.getAttrName()) || StringUtils.equals("NC_SUPPLIER_REPLACEMENT", mtExtendAttrVO1.getAttrName()) || StringUtils.equals("ORIGINAL_ID", mtExtendAttrVO1.getAttrName())) {
                        continue;
                    }
                    MtExtendVO5 extendVO5 = new MtExtendVO5();
                    extendVO5.setAttrName(mtExtendAttrVO1.getAttrName());
                    extendVO5.setAttrValue(mtExtendAttrVO1.getAttrValue());
                    attrs.add(extendVO5);
                }
            }
            if(StringUtils.equals(flag, "TK")){
                MtExtendVO5 attr2 = new MtExtendVO5();
                attr2.setAttrName("NC_SUPPLIER_REPLACEMENT");
                attr2.setAttrValue(YES);
                attrs.add(attr2);
            }

            MtExtendVO5 attr5 = new MtExtendVO5();
            attr5.setAttrName("ORIGINAL_ID");
            attr5.setAttrValue(sourceMaterialLot.getMaterialLotId());
            attrs.add(attr5);
        }
        //??????????????????
        if(CollectionUtils.isNotEmpty(attrs)){
            extendVO10.setAttrs(attrs);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, extendVO10);
        }
    }


    /**
     * ??????????????????????????????
     *
     * @param tenantId
     * @param eoId
     * @param materialLotId
     * @return java.lang.Boolean
     * @author sanfeng.zhang@hand-china.com 2020/11/25 15:54
     */
    private Boolean judgeFeedByMaterialLot(Long tenantId, String eoId, String materialLotId) {
        Integer feedCount = hmeNcCheckMapper.judgeFeedByMaterialLot(tenantId, eoId, materialLotId);
        return feedCount.compareTo(0) > 0;
    }

    //????????????
    private void materialLotSiteInVerify(Long tenantId, MtMaterialLot inputMaterialLot, MtMaterialLot sourcerMaterialLot, String flag) {
        //??????
        if(!StringUtils.equals(inputMaterialLot.getMaterialId(), sourcerMaterialLot.getMaterialId())){
            throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0034", "HME", "??????", "??????"));
        }
        //??????
        if(!StringUtils.equals(inputMaterialLot.getLot(), sourcerMaterialLot.getLot())){
            throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0034", "HME", "??????", "??????"));
        }
        //??????
        if(!StringUtils.equals(inputMaterialLot.getLocatorId(), sourcerMaterialLot.getLocatorId())){
            throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0034", "HME", "??????", "??????"));
        }
        MtExtendVO1 mtExtendVO = new MtExtendVO1();
        mtExtendVO.setTableName("mt_material_lot_attr");
        List<String> keyIdList = new ArrayList<>();
        keyIdList.add(inputMaterialLot.getMaterialLotId());
        keyIdList.add(sourcerMaterialLot.getMaterialLotId());
        mtExtendVO.setKeyIdList(keyIdList);
        List<MtExtendVO5> queryAttrList = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("SO_NUM");
        queryAttrList.add(mtExtendVO5);
        MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
        mtExtendVO51.setAttrName("SO_LINE_NUM");
        queryAttrList.add(mtExtendVO51);
        MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
        mtExtendVO52.setAttrName("NC_SUPPLIER_REPLACEMENT");
        queryAttrList.add(mtExtendVO52);
        MtExtendVO5 mtExtendVO53 = new MtExtendVO5();
        mtExtendVO53.setAttrName("MATERIAL_VERSION");
        queryAttrList.add(mtExtendVO53);
        mtExtendVO.setAttrs(queryAttrList);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
        //???????????????
        if(CollectionUtils.isNotEmpty(extendAttrVO1List)){
            Optional<MtExtendAttrVO1> inputSoNumOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), inputMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "SO_NUM")).findFirst();
            String inputSoNum = inputSoNumOpt.isPresent() ? inputSoNumOpt.get().getAttrValue() : "";
            Optional<MtExtendAttrVO1> sourceSoNumOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), sourcerMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "SO_NUM")).findFirst();
            String sourceSoNum = sourceSoNumOpt.isPresent() ? sourceSoNumOpt.get().getAttrValue() : "";
            if(!StringUtils.equals(inputSoNum, sourceSoNum)){
                throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0034", "HME", "????????????", "????????????"));
            }
        }
        //???????????????
        if(CollectionUtils.isNotEmpty(extendAttrVO1List)){
            Optional<MtExtendAttrVO1> inputSoLineNumOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), inputMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "SO_LINE_NUM")).findFirst();
            String inputSoLineNum = inputSoLineNumOpt.isPresent() ? inputSoLineNumOpt.get().getAttrValue() : "";
            Optional<MtExtendAttrVO1> sourceSoLineNumOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), sourcerMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "SO_LINE_NUM")).findFirst();
            String sourceSoLineNum = sourceSoLineNumOpt.isPresent() ? sourceSoLineNumOpt.get().getAttrValue() : "";
            if(!StringUtils.equals(inputSoLineNum, sourceSoLineNum)){
                throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0034", "HME", "???????????????", "???????????????"));
            }
        }

        //?????????
        if(CollectionUtils.isNotEmpty(extendAttrVO1List)){
            Optional<MtExtendAttrVO1> inputVersionOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), inputMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "MATERIAL_VERSION")).findFirst();
            String inputVersion = inputVersionOpt.isPresent() ? inputVersionOpt.get().getAttrValue() : "";
            Optional<MtExtendAttrVO1> sourceVersionOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), sourcerMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "MATERIAL_VERSION")).findFirst();
            String sourceVersion = sourceVersionOpt.isPresent() ? sourceVersionOpt.get().getAttrValue() : "";
            if(!StringUtils.equals(inputVersion, sourceVersion)){
                throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0034", "HME", "????????????", "????????????"));
            }
        }

        if (YES.equals(inputMaterialLot.getEnableFlag())) {
            //?????? ????????? ??????????????????
            if (StringUtils.equals(HmeConstants.ConstantValue.TK, flag)) {
                Optional<MtExtendAttrVO1> ncOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), inputMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "NC_SUPPLIER_REPLACEMENT")).findFirst();
                String ncFlag = ncOpt.isPresent() ? ncOpt.get().getAttrValue() : "";
                if (!StringUtils.equals(ncFlag, YES)) {
                    throw new MtException("HME_NC_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0035", "HME"));
                }
            }

        }
    }


    @Override
    public Page<HmeNcCheckVO> ncGroupLovQuery(Long tenantId, HmeNcCheckDTO3 dto, PageRequest pageRequest) {
        Page<HmeNcCheckVO> result = new Page<HmeNcCheckVO>();
        if (StringUtils.isEmpty(dto.getRootCauseOperationId())) {
            //?????????Id?????????????????????mt_nc_group???componentRequired???Y????????????????????????
            result = PageHelper.doPageAndSort(pageRequest, () -> hmeNcCheckMapper.ncGroupQuery(tenantId, dto));
        } else {
            //?????????????????????Id???????????????????????????
            result = PageHelper.doPageAndSort(pageRequest, () -> hmeNcCheckMapper.ncGroupQueryByOperation(tenantId, dto));
        }
        return result;
    }

    @Override
    public List<MtGenStatus> ncStatusQuery(Long tenantId) {
        return mtGenStatusRepository.getGenStatuz(tenantId, "NC_RECORD", "NC_RECORD_STATUS");
    }

    @Override
    public void materialScrap(Long tenantId, MtMaterialLot mtMaterialLot, MtNcRecord mtNcRecord, String eventId, String eventRequestId) {
        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
        if (StringUtils.isEmpty(mtModLocator.getParentLocatorId())) {
            throw new MtException("HME_NC_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0027", "HME"));
        }
        //???????????? ????????????/????????? ??????????????????Y ???????????????
        Boolean handleFlag;
        //?????????????????????
        List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
            setTenantId(tenantId);
            setParentRecordId(mtNcRecord.getNcRecordId());
        }});
        String returnMaterialFlag = CollectionUtils.isNotEmpty(ncRecordAttrList) ? ncRecordAttrList.get(0).getAttribute2() : "";
        //??????LOV??????
        List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        // ?????????????????????????????????
        Boolean cosMaterialFlag = false;
        List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(mtNcRecord.getMaterialLotId());
        if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
            List<String> collect = itemGroupList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                cosMaterialFlag = true;
            }
        }
        //?????????????????? ?????????
        //????????? ???????????? ???????????? ????????????
        if (StringUtils.equals(returnMaterialFlag, YES)) {
            //?????????????????? ?????????
            handleFlag = false;
        } else {
            //?????????????????? ???????????? ????????????
            handleFlag = true;
        }

        if(handleFlag){
            String bomComponentId = null;
            //??????????????????
            String assembleExcessFlag = YES;
            //?????????????????????
            MtModOrganizationRel mtModOrganizationRel = mtModOrganizationRelRepository.selectOne(new MtModOrganizationRel() {{
                setTenantId(tenantId);
                setOrganizationId(mtNcRecord.getWorkcellId());
                setOrganizationType("WORKCELL");
                setParentOrganizationType("WORKCELL");
            }});
            if (mtModOrganizationRel == null) {
                throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_002", "HME"));
            }
            //??????bom??????id,????????????Y,?????????N
            List<String> bomComponentIdList = hmeNcCheckMapper.queryCurrentBomComponentId(tenantId, mtNcRecord.getEoId(), mtNcRecord.getComponentMaterialId(), mtModOrganizationRel.getParentOrganizationId());
            if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
                bomComponentId = bomComponentIdList.get(0);
            }
            if (StringUtils.isBlank(bomComponentId)) {
                assembleExcessFlag = YES;
            } else {
                assembleExcessFlag = NO;
            }

            // ???????????????  ??????api?????????????????????????????????
            //????????????
            this.createNcCheckTransaction(tenantId, mtNcRecord, eventId, mtMaterialLot, mtModLocator);

            // ?????????????????????
            MtRouterOperation routerOperation = this.queryEoRouteOperation(tenantId, mtNcRecord);
            //2021-02-09 add by sanfeng.zhang for wang.can ???????????? ??????????????????????????????API{eoComponentAssemble}????????????????????????
            MtAssembleProcessActualVO5 mtEoComponentActualVO5 = new MtAssembleProcessActualVO5();
            mtEoComponentActualVO5.setAssembleExcessFlag(assembleExcessFlag);
            mtEoComponentActualVO5.setAssembleRouterType("NC");
            if (StringUtils.isNotEmpty(bomComponentId)) {
                mtEoComponentActualVO5.setBomComponentId(bomComponentId);
            }
            mtEoComponentActualVO5.setEoId(mtNcRecord.getEoId());
            mtEoComponentActualVO5.setEventRequestId(eventRequestId);
            mtEoComponentActualVO5.setMaterialId(mtNcRecord.getComponentMaterialId());
            mtEoComponentActualVO5.setOperationId(routerOperation.getOperationId());
            mtEoComponentActualVO5.setRouterStepId(routerOperation.getRouterStepId());
            mtEoComponentActualVO5.setParentEventId(eventId);
            mtEoComponentActualVO5.setTrxAssembleQty(mtNcRecord.getQty());
            mtEoComponentActualVO5.setWorkcellId(mtNcRecord.getWorkcellId());
            mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, mtEoComponentActualVO5);

            //??????API{eoComponentScrap}????????????????????????
            MtAssembleProcessActualVO7 mtEoComponentActualVO4 = new MtAssembleProcessActualVO7();
            mtEoComponentActualVO4.setAssembleExcessFlag(assembleExcessFlag);
            if (!StringUtils.equals(assembleExcessFlag, YES)) {
                mtEoComponentActualVO4.setBomComponentId(bomComponentId);
            }
            mtEoComponentActualVO4.setEventRequestId(eventRequestId);
            List<MtEoBom> mtEoBomList = mtEoBomRepository.select(new MtEoBom() {{
                setTenantId(tenantId);
                setEoId(mtNcRecord.getEoId());
            }});
            if(CollectionUtils.isNotEmpty(mtEoBomList)){
                mtEoComponentActualVO4.setBomId(mtEoBomList.get(0).getBomId());
            }
            mtEoComponentActualVO4.setEoId(mtNcRecord.getEoId());
            mtEoComponentActualVO4.setMaterialId(mtNcRecord.getComponentMaterialId());
            mtEoComponentActualVO4.setParentEventId(eventId);
            //?????????????????????
            String proLineId = hmeNcCheckMapper.queryProLineByWorkcellId(tenantId, mtNcRecord.getWorkcellId());
            if (StringUtils.isBlank(proLineId)) {
                throw new MtException("HME_EO_JOB_SN_040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_040", "HME"));
            }

            mtEoComponentActualVO4.setOperationId(routerOperation.getOperationId());
            mtEoComponentActualVO4.setRouterStepId(routerOperation.getRouterStepId());
            mtEoComponentActualVO4.setTrxScrappedQty(mtNcRecord.getQty());
            mtEoComponentActualVO4.setWorkcellId(mtNcRecord.getWorkcellId());
            mtAssembleProcessActualRepository.componentScrapProcess(tenantId, mtEoComponentActualVO4);

            //????????????
            MtAssembleProcessActualVO5 vo5 = new MtAssembleProcessActualVO5();
            vo5.setEoId(mtNcRecord.getEoId());
            vo5.setMaterialId(mtNcRecord.getComponentMaterialId());
            vo5.setTrxAssembleQty(mtNcRecord.getQty());
            vo5.setAssembleExcessFlag(assembleExcessFlag);
            if (!StringUtils.equals(assembleExcessFlag, YES)) {
                vo5.setBomComponentId(bomComponentId);
            }
            vo5.setOperationId(routerOperation.getOperationId());
            vo5.setRouterStepId(routerOperation.getRouterStepId());
            if (CollectionUtils.isNotEmpty(mtEoBomList)) {
                vo5.setBomId(mtEoBomList.get(0).getBomId());
            }
            List<MtEoRouter> mtEoRouterList = mtEoRouterRepository.select(new MtEoRouter() {{
                setTenantId(tenantId);
                setEoId(mtNcRecord.getEoId());
            }});
            if (CollectionUtils.isNotEmpty(mtEoRouterList)) {
                vo5.setRouterId(mtEoRouterList.get(0).getRouterId());
            }
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            vo5.setOperationBy(userId);
            vo5.setWorkcellId(mtNcRecord.getWorkcellId());
            vo5.setParentEventId(eventId);
            vo5.setEventRequestId(eventRequestId);
            vo5.setMaterialLotId(mtNcRecord.getMaterialLotId());
            mtAssembleProcessActualRepository.componentAssembleCancelProcess(tenantId, vo5);

            // ?????????????????????????????? ???????????????????????????
            String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
            // ??????API{onhandQtyUpdateProcess}?????????????????????
            MtEventCreateVO returnEventVO = new MtEventCreateVO();
            returnEventVO.setEventTypeCode("NC_RECORD_MATERIAL_CODE_RETURN");
            returnEventVO.setEventRequestId(eventRequestId);
            String returnEvent = mtEventRepository.eventCreate(tenantId, returnEventVO);
            MtInvOnhandQuantityVO9 onhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            onhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
            onhandQuantityVO9.setMaterialId(mtNcRecord.getComponentMaterialId());
            onhandQuantityVO9.setLocatorId(locatorId);
            onhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
            onhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
            onhandQuantityVO9.setEventId(returnEvent);
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, onhandQuantityVO9);

            // ??????????????? ??????????????? materialLotId ??????
            if (cosMaterialFlag) {
                if (CollectionUtils.isNotEmpty(ncRecordAttrList)) {
                    HmeNcRecordAttr hmeNcRecordAttr = ncRecordAttrList.get(0);
                    List<HmeMaterialLotLoad> loadList = hmeMaterialLotLoadMapper.select(new HmeMaterialLotLoad() {{
                        setTenantId(tenantId);
                        setLoadSequence(hmeNcRecordAttr.getAttribute7());
                    }});
                    if (CollectionUtils.isNotEmpty(loadList)) {
                        List<HmeMaterialLotLoad> materialLotLoadList = loadList.stream().map(load -> {
                            load.setMaterialLotId("");
                            return load;
                        }).collect(Collectors.toList());
                        List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(materialLotLoadList, SQL_ITEM_COUNT_LIMIT);
                        for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                            hmeMaterialLotLoadMapper.batchLoadUntie(tenantId, userId, domains);
                        }
                        // ??????LoadSequence?????????eoId
                        List<String> eoIdList = hmeNcCheckMapper.queryEoIdByLoadSequence(tenantId, hmeNcRecordAttr.getAttribute7());
                        // ?????????eoId ??????????????????
                        if (CollectionUtils.isNotEmpty(eoIdList)) {
                            hmeNcDisposePlatformRepository.updateFunctionInfo(tenantId, mtNcRecord.getEoId(), mtNcRecord.getWorkcellId(), hmeNcRecordAttr.getAttribute7());
                        }
                    }
                }
            }
        }
    }

    private void createNcCheckTransaction(Long tenantId, MtNcRecord mtNcRecord, String eventId, MtMaterialLot mtMaterialLot, MtModLocator mtModLocator) {
        // ???????????????bom???
        MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
            setTenantId(tenantId);
            setEoId(mtNcRecord.getEoId());
        }});
        if (mtEoBom == null) {
            throw new MtException("HME_NC_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0050", "HME"));
        }
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(mtNcRecord.getEoId());
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());

        List<MtBomComponent> componentList = mtBomComponentRepository.select(new MtBomComponent() {{
            setTenantId(tenantId);
            setMaterialId(mtNcRecord.getComponentMaterialId());
            setBomId(mtEoBom.getBomId());
        }});

        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtNcRecord.getComponentMaterialId());
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
        if (CollectionUtils.isNotEmpty(componentList)) {
            // ???bom???
            // ????????? ??????????????????bom???????????????????????????????????????
            MtBomComponent mtBomComponent;
            if (componentList.size() == 1) {
                mtBomComponent = componentList.get(0);
            } else {
                // ???????????? ??????bomComponent???RouterOperationId???mt_router_operation_component????????????
                mtBomComponent = this.queryOnlyBomComponent(tenantId, componentList, mtNcRecord);
            }
            // ?????????
            BigDecimal scrappedQty = BigDecimal.ZERO;
            // ???????????????
            BigDecimal totalDemand = BigDecimal.ZERO;
            // ????????????
            BigDecimal assembleQty = BigDecimal.ZERO;

            List<WmsObjectTransactionRequestVO> requestVOList = new ArrayList<>();
            // ???????????????????????? ??????0 -> ????????? ?????????0 -> ????????????
            if (mtBomComponent.getQty().compareTo(0D) != 0) {
                // ????????????
                List<MtWorkOrderComponentActual> actualList = this.queryScrappedQtyOfNoSubstitute(tenantId, mtNcRecord, mtWorkOrder, mtBomComponent, mtEoBom);
                Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // ???????????? -> ????????????????????????????????????????????????????????????????????????material_id??????????????????work_order_id??????????????????scrapped_qty??????
                scrappedQty = totalScrappedQty != null ? BigDecimal.valueOf(totalScrappedQty) : BigDecimal.ZERO;
                Double totalAssembleQty = actualList.stream().map(MtWorkOrderComponentActual::getAssembleQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // ???????????? -> ????????????????????????????????????????????????????????????????????????material_id??????????????????work_order_id??????????????????assemble_qty??????
                assembleQty = totalAssembleQty != null ? BigDecimal.valueOf(totalAssembleQty) : BigDecimal.ZERO;
                // ???????????? ??????????????????
                MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                mtExtendVO1.setTableName("mt_bom_component_attr");
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 attrOne = new MtExtendVO5();
                attrOne.setAttrName("lineAttribute5");
                attrList.add(attrOne);
                mtExtendVO1.setAttrs(attrList);
                mtExtendVO1.setKeyIdList(Collections.singletonList(mtBomComponent.getBomComponentId()));
                List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);

                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    switch (mtExtendAttrVO1.getAttrName()) {
                        case "lineAttribute5":
                            if (StringUtils.isNotBlank(mtExtendAttrVO1.getAttrValue())) {
                                totalDemand = BigDecimal.valueOf(Double.valueOf(mtExtendAttrVO1.getAttrValue()));
                            }
                            break;
                    }
                }

                // ???????????? ?????????????????????/??? ????????????????????????????????????????????????????????????????????????
                if (scrappedQty.add(assembleQty).compareTo(totalDemand) >= 0) {
                    // ?????????????????????????????? ??????????????? ??????????????? ??????????????? ???????????????????????????
                    WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                    requestVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    requestVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                    requestVO.setMoveType("261");
                    requestVOList.add(requestVO);
                } else {
                    // ????????????????????????????????????????????? ??????????????? ?????????????????????????????? ?????????????????????????????????
                    if (scrappedQty.add(assembleQty).add(BigDecimal.valueOf(mtNcRecord.getQty())).compareTo(totalDemand) <= 0) {
                        WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                        requestVO.setTransactionTypeCode("HME_WO_ISSUE");
                        requestVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                        requestVO.setMoveType("261");

                        //??????
                        requestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
                        //bomReserveNum
                        MtExtendVO extendVO = new MtExtendVO();
                        extendVO.setTableName("mt_bom_component_attr");
                        extendVO.setKeyId(mtBomComponent.getBomComponentId());
                        extendVO.setAttrName("lineAttribute10");
                        List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                        if (CollectionUtils.isNotEmpty(extendAttrList)) {
                            requestVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                        }
                        //??????????????????
                        MtExtendVO bomExtend = new MtExtendVO();
                        bomExtend.setTableName("mt_bom_component_attr");
                        bomExtend.setKeyId(mtBomComponent.getBomComponentId());
                        bomExtend.setAttrName("lineAttribute11");
                        List<MtExtendAttrVO> extendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomExtend);
                        if (CollectionUtils.isNotEmpty(extendAttrVOList) && StringUtils.equals("E", extendAttrVOList.get(0).getAttrValue())) {
                            //??????????????? ???mt_work_order???????????????attribute1
                            MtExtendVO workExtend = new MtExtendVO();
                            workExtend.setTableName("mt_work_order_attr");
                            workExtend.setKeyId(mtEo.getWorkOrderId());
                            workExtend.setAttrName("attribute1");
                            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                                requestVO.setSoNum(mtExtendAttrVOList.get(0).getAttrValue());
                            }
                            //?????????????????? ???mt_work_order???????????????attribute7
                            workExtend.setAttrName("attribute7");
                            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                            if (CollectionUtils.isNotEmpty(attrVOList)) {
                                requestVO.setSoLineNum(attrVOList.get(0).getAttrValue());
                            }
                        }
                        requestVOList.add(requestVO);
                    } else {
                        // ????????????????????????????????? ????????????????????????????????????????????????????????????????????????
                        WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                        requestOutVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        requestOutVO.setTransactionQty(scrappedQty.add(assembleQty).add(BigDecimal.valueOf(mtNcRecord.getQty())).subtract(totalDemand));
                        requestOutVO.setMoveType("261");
                        requestVOList.add(requestOutVO);

                        // ???????????????
                        WmsObjectTransactionRequestVO requestInVO = new WmsObjectTransactionRequestVO();
                        requestInVO.setTransactionTypeCode("HME_WO_ISSUE");
                        requestInVO.setTransactionQty(totalDemand.subtract(scrappedQty.add(assembleQty)));
                        requestInVO.setMoveType("261");

                        //??????
                        requestInVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
                        //bomReserveNum
                        MtExtendVO extendVO = new MtExtendVO();
                        extendVO.setTableName("mt_bom_component_attr");
                        extendVO.setKeyId(mtBomComponent.getBomComponentId());
                        extendVO.setAttrName("lineAttribute10");
                        List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                        if (CollectionUtils.isNotEmpty(extendAttrList)) {
                            requestInVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                        }
                        //??????????????????
                        MtExtendVO bomExtend = new MtExtendVO();
                        bomExtend.setTableName("mt_bom_component_attr");
                        bomExtend.setKeyId(mtBomComponent.getBomComponentId());
                        bomExtend.setAttrName("lineAttribute11");
                        List<MtExtendAttrVO> extendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomExtend);
                        if (CollectionUtils.isNotEmpty(extendAttrVOList) && StringUtils.equals("E", extendAttrVOList.get(0).getAttrValue())) {
                            //??????????????? ???mt_work_order???????????????attribute1
                            MtExtendVO workExtend = new MtExtendVO();
                            workExtend.setTableName("mt_work_order_attr");
                            workExtend.setKeyId(mtEo.getWorkOrderId());
                            workExtend.setAttrName("attribute1");
                            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                                requestInVO.setSoNum(mtExtendAttrVOList.get(0).getAttrValue());
                            }
                            //?????????????????? ???mt_work_order???????????????attribute7
                            workExtend.setAttrName("attribute7");
                            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                            if (CollectionUtils.isNotEmpty(attrVOList)) {
                                requestInVO.setSoLineNum(attrVOList.get(0).getAttrValue());
                            }
                        }
                        requestVOList.add(requestInVO);
                    }
                }
            } else {
                // ????????? ????????????????????? ??????????????? ????????????
                WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                requestOutVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                requestOutVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                requestOutVO.setMoveType("261");
                requestVOList.add(requestOutVO);

                // ?????????????????????????????????
                List<String> materialIdList = hmeNcCheckMapper.queryPrimaryMaterial(tenantId, mtBomComponent.getBomId(), mtBomComponent.getMaterialId());
                // ?????????????????????????????????
                if (CollectionUtils.isEmpty(materialIdList)) {
                    throw new MtException("HME_NC_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0037", "HME"));
                }
                if (materialIdList.size() > 1) {
                    throw new MtException("HME_NC_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0038", "HME"));
                }
                List<MtBomComponent> mainBomComponentList = mtBomComponentRepository.selectByCondition(Condition.builder(MtBomComponent.class)
                        .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtBomComponent.FIELD_BOM_ID, mtBomComponent.getBomId())
                                .andEqualTo(MtBomComponent.FIELD_MATERIAL_ID, materialIdList.get(0))).build());
                // ???????????????????????????????????????, ?????????????????????????????????????????????????????? ???????????????????????????
                List<MtWorkOrderComponentActual> actualList = this.queryScrappedQtyOfNoSubstitute(tenantId, mtNcRecord, mtWorkOrder, mainBomComponentList.get(0), mtEoBom);
                Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // ???????????? -> ????????????????????????????????????????????????????????????????????????material_id??????????????????work_order_id??????????????????scrapped_qty??????
                scrappedQty = totalScrappedQty != null ? BigDecimal.valueOf(totalScrappedQty) : BigDecimal.ZERO;
                Double totalAssembleQty = actualList.stream().map(MtWorkOrderComponentActual::getAssembleQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // ???????????? -> ????????????????????????????????????????????????????????????????????????material_id??????????????????work_order_id??????????????????assemble_qty??????
                assembleQty = totalAssembleQty != null ? BigDecimal.valueOf(totalAssembleQty) : BigDecimal.ZERO;

                // ????????????????????????????????? ??????????????????
                MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                mtExtendVO1.setTableName("mt_bom_component_attr");
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 attrOne = new MtExtendVO5();
                attrOne.setAttrName("lineAttribute5");
                attrList.add(attrOne);
                mtExtendVO1.setAttrs(attrList);
                mtExtendVO1.setKeyIdList(Collections.singletonList(mainBomComponentList.get(0).getBomComponentId()));
                List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);

                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    switch (mtExtendAttrVO1.getAttrName()) {
                        case "lineAttribute5":
                            if (StringUtils.isNotBlank(mtExtendAttrVO1.getAttrValue())) {
                                totalDemand = BigDecimal.valueOf(Double.valueOf(mtExtendAttrVO1.getAttrValue()));
                            }
                            break;
                    }
                }

                MtMaterial primaryMtMaterial = mtMaterialRepository.selectByPrimaryKey(mtNcRecord.getComponentMaterialId());
                MtUom primaryMaterialUom = mtUomRepository.selectByPrimaryKey(primaryMtMaterial.getPrimaryUomId());

                // ????????? ???????????????
                List<WmsObjectTransactionRequestVO> primaryRequestVOList = this.assemblyPrimaryMaterialTransaction(tenantId, mtNcRecord, totalDemand, assembleQty, scrappedQty, mtBomComponent, mtEo.getWorkOrderId());

                // ????????????
                List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();

                String warehouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
                if (StringUtils.isEmpty(warehouseCode)) {
                    throw new MtException("HME_NC_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0056", "HME"));
                }
                MtModLocator warehouse = mtModLocatorRepository.selectOne(new MtModLocator() {{
                    setTenantId(tenantId);
                    setLocatorCode(warehouseCode);
                }});
                if (Objects.isNull(warehouse)) {
                    throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0057", "HME"));
                }
                List<MtModLocator> locatorList = mtModLocatorRepository.select(new MtModLocator() {{
                    setTenantId(tenantId);
                    setParentLocatorId(warehouse.getLocatorId());
                }});

                if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
                    throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0057", "HME"));
                }
                for (WmsObjectTransactionRequestVO requestVO : primaryRequestVOList) {
                    WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                    wmsObjectTransactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                    wmsObjectTransactionRequestVO.setEventId(eventId);
                    wmsObjectTransactionRequestVO.setMaterialId(materialIdList.get(0));
                    wmsObjectTransactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
                    wmsObjectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                    wmsObjectTransactionRequestVO.setTransactionUom(primaryMaterialUom != null ? primaryMaterialUom.getUomCode() : "");
                    wmsObjectTransactionRequestVO.setTransactionTime(new Date());
                    wmsObjectTransactionRequestVO.setTransactionReasonCode(requestVO.getTransactionReasonCode());
                    wmsObjectTransactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
                    wmsObjectTransactionRequestVO.setWarehouseId(warehouse.getLocatorId());
                    wmsObjectTransactionRequestVO.setLocatorId(locatorList.get(0).getLocatorId());
                    wmsObjectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    wmsObjectTransactionRequestVO.setMergeFlag(NO);
                    wmsObjectTransactionRequestVO.setMoveType(requestVO.getMoveType());
                    wmsObjectTransactionRequestVO.setMoveReason(requestVO.getMoveReason());
                    wmsObjectTransactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                    wmsObjectTransactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                    wmsObjectTransactionRequestVO.setSoNum(requestVO.getSoNum());
                    wmsObjectTransactionRequestVO.setSoLineNum(requestVO.getSoLineNum());
                    transactionRequestVOList.add(wmsObjectTransactionRequestVO);
                }
                // ?????????????????????
                if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
                }
            }

            // ??????????????????
            List<WmsObjectTransactionRequestVO> wmsObjectTransactionRequestVOList = new ArrayList<>();
            for (WmsObjectTransactionRequestVO requestVO : requestVOList) {
                WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                transactionRequestVO.setEventId(eventId);
                transactionRequestVO.setMaterialLotId(mtNcRecord.getMaterialLotId());
                transactionRequestVO.setMaterialId(mtNcRecord.getComponentMaterialId());
                transactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
                transactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
                transactionRequestVO.setTransactionTime(new Date());
                transactionRequestVO.setTransactionReasonCode("??????????????????");
                transactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                transactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
                transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                transactionRequestVO.setMergeFlag(NO);
                transactionRequestVO.setMoveType(requestVO.getMoveType());
                transactionRequestVO.setMoveReason("??????????????????");
                transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                transactionRequestVO.setSoNum(requestVO.getSoNum());
                transactionRequestVO.setSoLineNum(requestVO.getSoLineNum());
                wmsObjectTransactionRequestVOList.add(transactionRequestVO);
            }
            // ????????????
            if (CollectionUtils.isNotEmpty(wmsObjectTransactionRequestVOList)) {
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, wmsObjectTransactionRequestVOList);
            }
        } else {
            // ???bom??? ???????????????????????????
            MtBomComponent bomComponent = queryPrimaryMaterial(tenantId, mtWorkOrder, mtNcRecord, mtEoBom.getBomId());

            if (bomComponent != null) {
                // ????????? ??????????????? ???????????????????????????
                List<WmsObjectTransactionRequestVO> requestVOList = new ArrayList<>();
                WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                requestOutVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                requestOutVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                requestOutVO.setMoveType("261");
                requestVOList.add(requestOutVO);

                // ???????????????
                List<MtWorkOrderComponentActual> actualList = this.queryScrappedQtyOfNoSubstitute(tenantId, mtNcRecord, mtWorkOrder, bomComponent, mtEoBom);
                Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // ???????????? -> ????????????????????????????????????????????????????????????????????????material_id??????????????????work_order_id??????????????????scrapped_qty??????
                BigDecimal scrappedQty = totalScrappedQty != null ? BigDecimal.valueOf(totalScrappedQty) : BigDecimal.ZERO;
                Double totalAssembleQty = actualList.stream().map(MtWorkOrderComponentActual::getAssembleQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // ???????????? -> ????????????????????????????????????????????????????????????????????????material_id??????????????????work_order_id??????????????????assemble_qty??????
                BigDecimal assembleQty = totalAssembleQty != null ? BigDecimal.valueOf(totalAssembleQty) : BigDecimal.ZERO;

                MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                mtExtendVO1.setTableName("mt_bom_component_attr");
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 attrOne = new MtExtendVO5();
                attrOne.setAttrName("lineAttribute5");
                attrList.add(attrOne);
                mtExtendVO1.setAttrs(attrList);
                mtExtendVO1.setKeyIdList(Collections.singletonList(bomComponent.getBomComponentId()));
                List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);

                // ???????????????
                BigDecimal totalDemand = BigDecimal.ZERO;
                // ???????????????
                BigDecimal realDemand = BigDecimal.ZERO;
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    switch (mtExtendAttrVO1.getAttrName()) {
                        case "lineAttribute5":
                            if (StringUtils.isNotBlank(mtExtendAttrVO1.getAttrValue())) {
                                totalDemand = BigDecimal.valueOf(Double.valueOf(mtExtendAttrVO1.getAttrValue()));
                            }
                            break;
                    }
                }
                List<WmsObjectTransactionRequestVO> transactionRequestVOList = this.assemblyPrimaryMaterialTransaction(tenantId, mtNcRecord, totalDemand, assembleQty, scrappedQty, bomComponent, mtEo.getWorkOrderId());

                // ??????????????????????????????
                List<WmsObjectTransactionRequestVO> wmsObjectTransactionRequestVOList = new ArrayList<>();
                String warehouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
                if (StringUtils.isEmpty(warehouseCode)) {
                    throw new MtException("HME_NC_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0056", "HME"));
                }
                MtModLocator warehouse = mtModLocatorRepository.selectOne(new MtModLocator() {{
                    setTenantId(tenantId);
                    setLocatorCode(warehouseCode);
                }});
                if (Objects.isNull(warehouse)) {
                    throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0057", "HME"));
                }
                List<MtModLocator> locatorList = mtModLocatorRepository.select(new MtModLocator() {{
                    setTenantId(tenantId);
                    setParentLocatorId(warehouse.getLocatorId());
                }});

                if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
                    throw new MtException("HME_NC_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0057", "HME"));
                }
                MtMaterial primaryMaterial = mtMaterialRepository.selectByPrimaryKey(bomComponent.getMaterialId());
                MtUom primaryMaterialUom = mtUomRepository.selectByPrimaryKey(primaryMaterial.getPrimaryUomId());
                for (WmsObjectTransactionRequestVO requestVO : transactionRequestVOList) {
                    WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                    wmsObjectTransactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                    wmsObjectTransactionRequestVO.setEventId(eventId);
                    wmsObjectTransactionRequestVO.setMaterialId(bomComponent.getMaterialId());
                    wmsObjectTransactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
                    wmsObjectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                    wmsObjectTransactionRequestVO.setTransactionUom(primaryMaterialUom != null ? primaryMaterialUom.getUomCode() : "");
                    wmsObjectTransactionRequestVO.setTransactionTime(new Date());
                    wmsObjectTransactionRequestVO.setTransactionReasonCode(requestVO.getTransactionReasonCode());
                    wmsObjectTransactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
                    wmsObjectTransactionRequestVO.setWarehouseId(warehouse.getLocatorId());
                    wmsObjectTransactionRequestVO.setLocatorId(locatorList.get(0).getLocatorId());
                    wmsObjectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    wmsObjectTransactionRequestVO.setMergeFlag(NO);
                    wmsObjectTransactionRequestVO.setMoveType(requestVO.getMoveType());
                    wmsObjectTransactionRequestVO.setMoveReason(requestVO.getMoveReason());
                    wmsObjectTransactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                    wmsObjectTransactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                    wmsObjectTransactionRequestVO.setSoNum(requestVO.getSoNum());
                    wmsObjectTransactionRequestVO.setSoLineNum(requestVO.getSoLineNum());
                    wmsObjectTransactionRequestVOList.add(wmsObjectTransactionRequestVO);
                }
                if (CollectionUtils.isNotEmpty(wmsObjectTransactionRequestVOList)) {
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, wmsObjectTransactionRequestVOList);
                }

                // ???????????????
                List<WmsObjectTransactionRequestVO> substituteTransactionList = new ArrayList<>();
                for (WmsObjectTransactionRequestVO requestVO : requestVOList) {
                    WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                    transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                    transactionRequestVO.setEventId(eventId);
                    transactionRequestVO.setMaterialLotId(mtNcRecord.getMaterialLotId());
                    transactionRequestVO.setMaterialId(mtNcRecord.getComponentMaterialId());
                    transactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
                    transactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                    transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
                    transactionRequestVO.setTransactionTime(new Date());
                    transactionRequestVO.setTransactionReasonCode("??????????????????");
                    transactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                    transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                    transactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
                    transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    transactionRequestVO.setMergeFlag(NO);
                    transactionRequestVO.setMoveType(requestVO.getMoveType());
                    transactionRequestVO.setMoveReason("??????????????????");
                    transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                    transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                    transactionRequestVO.setSoNum(requestVO.getSoNum());
                    transactionRequestVO.setSoLineNum(requestVO.getSoLineNum());
                    substituteTransactionList.add(transactionRequestVO);
                }
                // ????????????
                if (CollectionUtils.isNotEmpty(substituteTransactionList)) {
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, substituteTransactionList);
                }
            } else {
                // ????????? ?????????????????? ???????????????(????????????: ????????????)
                WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                transactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                transactionRequestVO.setEventId(eventId);
                transactionRequestVO.setMaterialLotId(mtNcRecord.getMaterialLotId());
                transactionRequestVO.setMaterialId(mtNcRecord.getComponentMaterialId());
                transactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                transactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
                transactionRequestVO.setTransactionTime(new Date());
                transactionRequestVO.setTransactionReasonCode("??????????????????");
                transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
                transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                transactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
                transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                transactionRequestVO.setMergeFlag(NO);
                transactionRequestVO.setMoveType("261");
                transactionRequestVO.setMoveReason("??????????????????");
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(transactionRequestVO));
            }
        }
    }

    private List<MtWorkOrderComponentActual> queryScrappedQtyOfNoSubstitute(Long tenantId, MtNcRecord mtNcRecord, MtWorkOrder mtWorkOrder, MtBomComponent mtBomComponent, MtEoBom mtEoBom) {
        // ???????????????????????????????????????????????????
        List<String> materialIdList = new ArrayList<>();
        // ??????????????????????????????????????????
        List<String> substituteMaterialList = hmeCosPatchPdaMapper.getWorkOrderSubstituteMaterial(tenantId, mtBomComponent.getBomComponentId());
        List<MtBomComponent> bomComponentList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(substituteMaterialList)) {
            bomComponentList = mtBomComponentRepository.selectByCondition(Condition.builder(MtBomComponent.class)
                    .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtBomComponent.FIELD_BOM_ID, mtEoBom.getBomId())
                            .andIn(MtBomComponent.FIELD_MATERIAL_ID, substituteMaterialList)).build());
        }
        if (CollectionUtils.isNotEmpty(bomComponentList)) {
            // ????????? ????????????????????? ??????????????????
            materialIdList.addAll(bomComponentList.stream().map(MtBomComponent::getMaterialId).collect(Collectors.toList()));
        }
        // ??????????????????????????????????????????
        //??????BOM????????????ID+??????ID?????????????????????
        List<String> globalSubstituteMaterialList = hmeCosPatchPdaMapper.getGlobalSubstituteMaterial(tenantId, mtBomComponent.getMaterialId(), mtNcRecord.getSiteId());
        if (CollectionUtils.isNotEmpty(globalSubstituteMaterialList)) {
            materialIdList.addAll(globalSubstituteMaterialList);
        }
        materialIdList.add(mtBomComponent.getMaterialId());
        // ????????????????????????
        MtRouterOperation routerOperation = this.queryWoRouteOperation(tenantId, mtNcRecord, mtWorkOrder);

        List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                .andIn(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, materialIdList)
                .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerOperation.getRouterStepId())).build());
        return actualList;
    }

    /**
     * ??????bomComponent???RouterOperationId???mt_router_operation_component????????????
     *
     * @param tenantId
     * @param bomComponentList
     * @param mtNcRecord
     * @return tarzan.method.domain.entity.MtBomComponent
     * @author sanfeng.zhang@hand-china.com 2020/12/23 14:37
     */
    private MtBomComponent queryOnlyBomComponent(Long tenantId, List<MtBomComponent> bomComponentList, MtNcRecord mtNcRecord) {
        MtRouterOperation routerOperation = this.queryEoRouteOperation(tenantId, mtNcRecord);
        List<MtRouterOperationComponent> routerOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class).andWhere(Sqls.custom()
                .andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperation.getRouterOperationId())
                .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, bomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))).build());
        if (CollectionUtils.isEmpty(routerOperationComponentList)) {
            throw new MtException("HME_NC_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0055", "HME"));
        }
        String bomComponentId = routerOperationComponentList.get(0).getBomComponentId();
        return mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
    }


    private MtRouterOperation queryWoRouteOperation(Long tenantId, MtNcRecord mtNcRecord, MtWorkOrder mtWorkOrder) {
        // ????????????????????? ??????????????????
        String operationId = "";
        List<MtModOrganizationRel> processList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(mtNcRecord.getWorkcellId());
            setParentOrganizationType("WORKCELL");
        }});
        if (CollectionUtils.isEmpty(processList)) {
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        }
        // ??????EOId?????? ??????????????????????????????
        List<MtRouterOperation> mtRouterOperationList = hmeNcCheckMapper.queryWoRouterOperationListByRouterId(tenantId, mtWorkOrder.getRouterId());
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtRouterOperationList)) {
            operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                    .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())
                    .andIn(MtOperationWkcDispatchRel.FIELD_OPERATION_ID, mtRouterOperationList.stream().map(MtRouterOperation::getOperationId).collect(Collectors.toList()))).build());
        }
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

        //??????operationId???????????????RouterStepId
        MtRouterOperation routerOperation = null;
        for (MtRouterOperation mtRouterOperation : mtRouterOperationList) {
            if (StringUtils.equals(mtRouterOperation.getOperationId(), operationId)) {
                routerOperation = mtRouterOperation;
                break;
            }
        }

        if (routerOperation == null) {
            //????????????????????????{?????????????????????????????????????????????}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        }
        return routerOperation;
    }

    /**
     * ???eo???????????????????????????
     *
     * @param tenantId
     * @param mtNcRecord
     * @return tarzan.method.domain.entity.MtRouterOperation
     * @author sanfeng.zhang@hand-china.com 2020/12/23 17:50
     */
    private MtRouterOperation queryEoRouteOperation(Long tenantId, MtNcRecord mtNcRecord) {
        // ????????????????????? ??????????????????
        String operationId = "";
        List<MtModOrganizationRel> processList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(mtNcRecord.getWorkcellId());
            setParentOrganizationType("WORKCELL");
        }});
        if (CollectionUtils.isEmpty(processList)) {
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        }
        // ??????EOId?????? ??????????????????????????????
        List<MtRouterOperation> mtRouterOperationList = hmeNcCheckMapper.queryRouterOperationListByEoId(tenantId, mtNcRecord.getEoId());
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtRouterOperationList)) {
            operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                    .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())
                    .andIn(MtOperationWkcDispatchRel.FIELD_OPERATION_ID, mtRouterOperationList.stream().map(MtRouterOperation::getOperationId).collect(Collectors.toList()))).build());
        }
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

        //??????operationId???????????????RouterStepId
        MtRouterOperation routerOperation = null;
        for (MtRouterOperation mtRouterOperation : mtRouterOperationList) {
            if (StringUtils.equals(mtRouterOperation.getOperationId(), operationId)) {
                routerOperation = mtRouterOperation;
                break;
            }
        }

        if (routerOperation == null) {
            //????????????????????????{?????????????????????????????????????????????}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        }
        return routerOperation;
    }

    /**
     * ???bom??? ?????????????????????????????????
     *
     * @param tenantId
     * @param mtWorkOrder
     * @param mtNcRecord
     * @param bomId
     * @return tarzan.method.domain.entity.MtBomComponent
     * @author sanfeng.zhang@hand-china.com 2020/12/23 14:47
     */
    private MtBomComponent queryPrimaryMaterial(Long tenantId, MtWorkOrder mtWorkOrder, MtNcRecord mtNcRecord, String bomId) {
        // bom??????
        MtBomComponent mtBomComponent = null;
        //?????????????????????????????????????????????
        List<WmsMaterialSubstituteRel> substituteRelList = wmsMaterialSubstituteRelRepository.select(new WmsMaterialSubstituteRel() {{
            setTenantId(tenantId);
            setSiteId(mtWorkOrder.getSiteId());
            setMaterialId(mtNcRecord.getComponentMaterialId());
        }});
        List<String> substituteGroupList = substituteRelList.stream().map(WmsMaterialSubstituteRel::getSubstituteGroup).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        //???????????? ?????????
        if (CollectionUtils.isNotEmpty(substituteGroupList) && substituteGroupList.size() > 1) {
            throw new MtException("HME_NC_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0043", "HME"));
        }
        if (CollectionUtils.isNotEmpty(substituteGroupList)) {
            // ??????????????? ???????????????????????????
            List<WmsMaterialSubstituteRel> materialSubstituteRelList = wmsMaterialSubstituteRelRepository.select(new WmsMaterialSubstituteRel() {{
                setTenantId(tenantId);
                setSubstituteGroup(substituteGroupList.get(0));
            }});
            List<String> materialSubstituteIdList = materialSubstituteRelList.stream().map(WmsMaterialSubstituteRel::getMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<MtBomComponent> mtBomComponentList = new ArrayList<>();
            // ????????????????????????
            if (CollectionUtils.isNotEmpty(materialSubstituteIdList)) {
                mtBomComponentList = mtBomComponentRepository.selectByCondition(Condition.builder(MtBomComponent.class)
                        .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtBomComponent.FIELD_BOM_ID, bomId)
                                .andIn(MtBomComponent.FIELD_MATERIAL_ID, materialSubstituteIdList)).build());
            }
            if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
                if (mtBomComponentList.size() == 1) {
                    // ???????????? ????????????????????????????????????
                    mtBomComponent = mtBomComponentList.get(0);
                } else if (mtBomComponentList.size() > 1) {
                    // ????????????
                    // ????????????????????? ??????????????????
                    String operationId = "";
                    List<MtModOrganizationRel> processList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                        setTenantId(tenantId);
                        setOrganizationId(mtNcRecord.getWorkcellId());
                        setParentOrganizationType("WORKCELL");
                    }});
                    if (CollectionUtils.isEmpty(processList)) {
                        throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_002", "HME"));
                    }
                    List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
                        setTenantId(tenantId);
                        setWorkcellId(processList.get(0).getParentOrganizationId());
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

                    //??????eoId,operationId??????MtRouterOperation
                    List<MtRouterOperation> mtRouterOperationList = hmeNcCheckMapper.getRouterStepByEoId(tenantId, mtNcRecord.getEoId(), operationId);

                    if (CollectionUtils.isEmpty(mtRouterOperationList)) {
                        //????????????????????????{?????????????????????????????????????????????}
                        throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0046", "HME"));
                    } else if (mtRouterOperationList.size() > 1) {
                        //???????????????????????????{????????????????????????????????????????????????}
                        throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0047", "HME"));
                    }
                    String routerOperationId = mtRouterOperationList.get(0).getRouterOperationId();
                    //????????????bomComponentId???routerOperationId?????????mt_router_operation_component
                    List<MtRouterOperationComponent> mtRouterOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class)
                            .andWhere(Sqls.custom().andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                                    .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))
                                    .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperationId)).build());
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
                    mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
                }
            }
        }
        return mtBomComponent;
    }

    /**
     * ????????????????????????
     *
     * @param tenantId
     * @param mtNcRecord
     * @param totalDemand
     * @param assembleQty
     * @param scrappedQty
     * @param mtBomComponent
     * @param wordOrderId
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/17 9:19
     */
    private List<WmsObjectTransactionRequestVO> assemblyPrimaryMaterialTransaction(Long tenantId, MtNcRecord mtNcRecord, BigDecimal totalDemand, BigDecimal assembleQty, BigDecimal scrappedQty, MtBomComponent mtBomComponent, String wordOrderId) {
        List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();
        if (BigDecimal.valueOf(mtNcRecord.getQty()).compareTo(totalDemand.subtract(scrappedQty).subtract(assembleQty)) <= 0) {
            // ???????????? ??????????????? ???????????????????????????????????????????????? ????????????????????? ?????????????????????
            WmsObjectTransactionRequestVO requestOutReturnVO = new WmsObjectTransactionRequestVO();
            requestOutReturnVO.setTransactionTypeCode("HME_WO_ISSUE_R_EXT");
            requestOutReturnVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
            requestOutReturnVO.setTransactionReasonCode("????????????");
            requestOutReturnVO.setMoveReason("????????????");
            requestOutReturnVO.setMoveType("262");
            transactionRequestVOList.add(requestOutReturnVO);

            // ???????????????
            WmsObjectTransactionRequestVO requestInFeedVO = new WmsObjectTransactionRequestVO();
            requestInFeedVO.setTransactionTypeCode("HME_WO_ISSUE");
            requestInFeedVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
            requestInFeedVO.setTransactionReasonCode("??????????????????");
            requestOutReturnVO.setMoveReason("??????????????????");
            requestInFeedVO.setMoveType("261");
            //??????
            requestInFeedVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            //bomReserveNum
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName("mt_bom_component_attr");
            extendVO.setKeyId(mtBomComponent.getBomComponentId());
            extendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            if (CollectionUtils.isNotEmpty(extendAttrList)) {
                requestInFeedVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
            }
            //??????????????????
            MtExtendVO bomExtend = new MtExtendVO();
            bomExtend.setTableName("mt_bom_component_attr");
            bomExtend.setKeyId(mtBomComponent.getBomComponentId());
            bomExtend.setAttrName("lineAttribute11");
            List<MtExtendAttrVO> extendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomExtend);
            if (CollectionUtils.isNotEmpty(extendAttrVOList) && StringUtils.equals("E", extendAttrVOList.get(0).getAttrValue())) {
                //??????????????? ???mt_work_order???????????????attribute1
                MtExtendVO workExtend = new MtExtendVO();
                workExtend.setTableName("mt_work_order_attr");
                workExtend.setKeyId(wordOrderId);
                workExtend.setAttrName("attribute1");
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    requestInFeedVO.setSoNum(mtExtendAttrVOList.get(0).getAttrValue());
                }
                //?????????????????? ???mt_work_order???????????????attribute7
                workExtend.setAttrName("attribute7");
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                if (CollectionUtils.isNotEmpty(attrVOList)) {
                    requestInFeedVO.setSoLineNum(attrVOList.get(0).getAttrValue());
                }
            }
            transactionRequestVOList.add(requestInFeedVO);
        } else {
            // ???????????????????????????????????????????????????????????????????????????
            if (totalDemand.subtract(assembleQty).subtract(scrappedQty).compareTo(BigDecimal.ZERO) > 0) {
                // ????????????????????????????????????????????????????????????
                // ??????????????? (??????????????????????????????????????????????????????-????????????)
                WmsObjectTransactionRequestVO requestOutReturnVO = new WmsObjectTransactionRequestVO();
                requestOutReturnVO.setTransactionTypeCode("HME_WO_ISSUE_R_EXT");
                requestOutReturnVO.setTransactionQty(totalDemand.subtract(assembleQty).subtract(scrappedQty));
                requestOutReturnVO.setTransactionReasonCode("????????????");
                requestOutReturnVO.setMoveReason("????????????");
                requestOutReturnVO.setMoveType("262");
                transactionRequestVOList.add(requestOutReturnVO);

                // ??????????????? (??????????????????????????????????????????????????????-????????????)
                WmsObjectTransactionRequestVO requestInFeedVO = new WmsObjectTransactionRequestVO();
                requestInFeedVO.setTransactionTypeCode("HME_WO_ISSUE");
                requestInFeedVO.setTransactionQty(totalDemand.subtract(assembleQty).subtract(scrappedQty));
                requestInFeedVO.setTransactionReasonCode("??????????????????");
                requestInFeedVO.setMoveReason("??????????????????");
                requestInFeedVO.setMoveType("261");
                //??????
                requestInFeedVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
                //bomReserveNum
                MtExtendVO extendVO = new MtExtendVO();
                extendVO.setTableName("mt_bom_component_attr");
                extendVO.setKeyId(mtBomComponent.getBomComponentId());
                extendVO.setAttrName("lineAttribute10");
                List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                if (CollectionUtils.isNotEmpty(extendAttrList)) {
                    requestInFeedVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                }
                //??????????????????
                MtExtendVO bomExtend = new MtExtendVO();
                bomExtend.setTableName("mt_bom_component_attr");
                bomExtend.setKeyId(mtBomComponent.getBomComponentId());
                bomExtend.setAttrName("lineAttribute11");
                List<MtExtendAttrVO> extendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomExtend);
                if (CollectionUtils.isNotEmpty(extendAttrVOList) && StringUtils.equals("E", extendAttrVOList.get(0).getAttrValue())) {
                    //??????????????? ???mt_work_order???????????????attribute1
                    MtExtendVO workExtend = new MtExtendVO();
                    workExtend.setTableName("mt_work_order_attr");
                    workExtend.setKeyId(wordOrderId);
                    workExtend.setAttrName("attribute1");
                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                        requestInFeedVO.setSoNum(mtExtendAttrVOList.get(0).getAttrValue());
                    }
                    //?????????????????? ???mt_work_order???????????????attribute7
                    workExtend.setAttrName("attribute7");
                    List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                    if (CollectionUtils.isNotEmpty(attrVOList)) {
                        requestInFeedVO.setSoLineNum(attrVOList.get(0).getAttrValue());
                    }
                }
                transactionRequestVOList.add(requestInFeedVO);
            }
        }
        return transactionRequestVOList;
    }

    @Override
    public List<HmeModAreaVO3> areaUnitQuery(Long tenantId) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        mtUserOrganization.setUserId(userId);
        mtUserOrganization.setOrganizationType("AREA");
        List<MtUserOrganization> mtUserOrganizationList =
                mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        List<String> areaIds = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
        Map<String, List<MtModArea>> areaMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(areaIds)) {
            List<MtModArea> mtModAreaList = mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);
            areaMap = mtModAreaList.stream().collect(Collectors.groupingBy(dto -> dto.getAreaId()));
        }
        List<HmeModAreaVO3> hmeModAreaVO3List = new ArrayList<>();
        for (MtUserOrganization userOrganization : mtUserOrganizationList) {
            List<MtModArea> mtModAreas = areaMap.get(userOrganization.getOrganizationId());
            if (CollectionUtils.isNotEmpty(mtModAreas) && StringUtils.equals(mtModAreas.get(0).getAreaCategory(), "SYB")) {
                HmeModAreaVO3 hmeModAreaVO3 = new HmeModAreaVO3();
                hmeModAreaVO3.setDefaultOrganizationFlag(userOrganization.getDefaultOrganizationFlag());
                hmeModAreaVO3.setAreaId(mtModAreas.get(0).getAreaId());
                hmeModAreaVO3.setAreaCode(mtModAreas.get(0).getAreaCode());
                hmeModAreaVO3.setAreaName(mtModAreas.get(0).getAreaName());
                hmeModAreaVO3List.add(hmeModAreaVO3);
            }
        }
        return hmeModAreaVO3List;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCheckSubmit(Long tenantId, HmeNcCheckDTO4 dto) {
        List<String> ncRecordIdList = dto.getNcRecordIdList();
        // ??????????????????
        List<MtNcRecord> mtNcRecordList = mtNcRecordRepository.selectByCondition(Condition.builder(MtNcRecord.class).andWhere(Sqls.custom()
                .andEqualTo(MtNcRecord.FIELD_TENANT_ID, tenantId)
                .andIn(MtNcRecord.FIELD_NC_RECORD_ID, ncRecordIdList)).build());
        if (CollectionUtils.isEmpty(mtNcRecordList)) {
            throw new MtException("HME_NC_0070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_NC_0070", "HME"));
        }
        // ???????????? ?????????OPEN ????????????
        Optional<MtNcRecord> nonOpenOpt = mtNcRecordList.stream().filter(nr -> !HmeConstants.NcStatus.OPEN.equals(nr.getNcStatus())).findFirst();
        if (nonOpenOpt.isPresent()) {
            throw new MtException("HME_NC_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0007", "HME"));
        }
        // ????????????????????? ??????????????????????????????????????????????????????
        List<String> ncCodeIdList = mtNcRecordList.stream().map(MtNcRecord::getNcCodeId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ??????????????????????????? ????????????????????? ?????????
        if (CollectionUtils.isNotEmpty(ncCodeIdList) && ncCodeIdList.size() > 1) {
            throw new MtException("HME_NC_0061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0061", "HME"));
        }
        List<MtNcGroup> mtNcGroupList = mtNcGroupRepository.selectByCondition(Condition.builder(MtNcGroup.class).andWhere(Sqls.custom()
                .andEqualTo(MtNcGroup.FIELD_TENANT_ID, tenantId)
                .andIn(MtNcGroup.FIELD_NC_GROUP_ID, ncCodeIdList)).build());
        // ???????????????????????? ?????????????????? ????????????????????????
        List<String> componentRequiredList = mtNcGroupList.stream().map(MtNcGroup::getComponentRequired).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (componentRequiredList.size() > 1) {
            throw new MtException("HME_NC_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0067", "HME"));
        }
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CLOSE");
        //????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("NC_RECORD_CLOSE");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        // ??????????????????????????????????????????????????????
        this.batchHandleNcRecordClose(tenantId, mtNcRecordList, dto, userId, eventId, eventRequestId);
        // ComponentRequired ???N ?????????????????? ???Y ??????????????????
        if (NO.equals(componentRequiredList.get(0))) {
            // ?????????????????? ?????? ???????????????????????? ???????????????
            if (HmeConstants.ProcessStatus.THREE.equals(dto.getProcessMethod()) && ncRecordIdList.size() > 1) {
                throw new MtException("HME_NC_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0068", "HME"));
            }
            // ????????????????????????
            this.batchHandleProcessNc(tenantId, mtNcRecordList, dto, userId, eventId);
        } else if (YES.equals(componentRequiredList.get(0))) {
            // ??????????????????
            // ???????????????????????????????????????
            if (CollectionUtils.isNotEmpty(dto.getNcRecordIdList()) && dto.getNcRecordIdList().size() > 1) {
                if (HmeConstants.ProcessStatus.THREE.equals(dto.getProcessMethod())) {
                    throw new MtException("HME_NC_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0068", "HME"));
                }

                if (HmeConstants.ProcessStatus.SIX.equals(dto.getProcessMethod())) {
                    throw new MtException("HME_NC_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0068", "HME"));
                }
            }
            this.batchHandleMaterialNc(tenantId, mtNcRecordList, dto, userId, eventId, eventRequestId);
        }
    }

    @Override
    public List<HmeNcDisposePlatformDTO2> export(Long tenantId, HmeNcCheckDTO dto) {

        List<String> workcellIdList = new ArrayList<>();
        Boolean flag = false;
        Boolean flag1 = false;
        // ?????????????????? ??????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getAreaId()) && StringUtils.isBlank(dto.getProdLineId()) && StringUtils.isBlank(dto.getProcessId()) && StringUtils.isBlank(dto.getWorkcellId())) {
            flag = true;
            //??????API {subOrganizationRelQuery} ??????????????????????????????
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setParentOrganizationId(dto.getAreaId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //?????????????????????????????????
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //?????????????????????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getProdLineId())
                && StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            flag = true;
            //??????API {subOrganizationRelQuery} ??????????????????????????????
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationVO2.setParentOrganizationId(dto.getProdLineId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //??????????????????????????????
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //?????????????????????????????????????????????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getProcessId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            flag = true;
            //??????API {subOrganizationRelQuery} ??????????????????????????????
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
            mtModOrganizationVO2.setParentOrganizationId(dto.getProcessId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //?????????????????????
        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            flag1 = true;
            //??????????????????????????????mt_material_lot???material_lot_id
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(
                    Sqls.custom().andLikeRight(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getMaterialLotCode())
            ).build());
            dto.setMaterialLotIdList(mtMaterialLots.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList()));
        }
        //????????????
        //????????????????????????????????????????????????????????????????????????????????????Id???????????????????????????
        if ((flag && CollectionUtils.isEmpty(dto.getWorkcellIdList())) || (flag1 && CollectionUtils.isEmpty(dto.getMaterialLotIdList()))) {
            return new ArrayList<>();
        }

        //??????????????????
        List<HmeNcDisposePlatformDTO2> ncRecordList = hmeNcDisposePlatformMapper.ncRecordQuery(tenantId, dto);
        for (HmeNcDisposePlatformDTO2 hmeNcDisposePlatformDTO2 : ncRecordList) {
            //?????? ??????ROOT_CAUSE_WORKCELL_ID??????API{parentOrganizationRelQuery}
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(hmeNcDisposePlatformDTO2.getSiteId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setOrganizationId(hmeNcDisposePlatformDTO2.getWorkcellId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setQueryType("ALL");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
                if ("CJ".equals(mtModArea.getAreaCategory())) {
                    hmeNcDisposePlatformDTO2.setWorkShopName(mtModArea.getAreaName());
                    break;
                }
            }
            //?????????
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
                hmeNcDisposePlatformDTO2.setProdLineName(mtModProductionLine.getDescription());
            }
            //???????????????
            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
                if ("LINE".equals(mtModWorkcell.getWorkcellType())) {
                    hmeNcDisposePlatformDTO2.setLineWorkcellName(mtModWorkcell.getWorkcellName());
                } else if ("PROCESS".equals(mtModWorkcell.getWorkcellType())) {
                    hmeNcDisposePlatformDTO2.setProcessName(mtModWorkcell.getWorkcellName());
                }
            }
            //?????? ??????ROOT_CAUSE_WORKCELL_ID???WORKCELL_TYPE = STATION?????? mt_mod_workcell??????WORKCELL_NAME
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getWorkcellId());
            hmeNcDisposePlatformDTO2.setWorkcellName(mtModWorkcell.getWorkcellName());
            //?????????
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getCreatedBy());
            hmeNcDisposePlatformDTO2.setUserName(mtUserInfo.getRealName());
            //??????
            hmeNcDisposePlatformDTO2.setUserCode(mtUserInfo.getLoginName());
            //????????? MATERIAL_LOT_ID??????mt_material_lot??????material_lot_code?????????????????????material_Lot_id
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getMaterialLotId());
            if (mtMaterialLot != null && StringUtils.isNotBlank(mtMaterialLot.getMaterialLotCode())) {
                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            } else {
                hmeNcDisposePlatformDTO2.setMaterialLotCode(hmeNcDisposePlatformDTO2.getMaterialLotId());
            }
            //????????? ??????eo_id?????????mt_eo?????????work_order_id?????????work_order_id??????mt_work_order??????WORK_ORDER_NUM
//            MtEo mtEo = mtEoRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getEoId());
//            if (mtEo != null && StringUtils.isNotBlank(mtEo.getWorkOrderId())) {
//                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
//                if (mtWorkOrder != null) {
//                    hmeNcDisposePlatformDTO2.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
//                }
//            }
            //???????????? ????????????
            //??????mt_nc_record?????????PARENT_NC_RECORD_ID = NC_RECORD_ID????????????
            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, hmeNcDisposePlatformDTO2.getNcRecordId());
            List<String> ncCodeIdList = new ArrayList<>();
            List<String> ncCodeList = new ArrayList<>();
            List<String> ncReasonList = new ArrayList<>();
            //???????????????
            if (CollectionUtils.isNotEmpty(mtNcRecordList) && StringUtils.isNotEmpty(mtNcRecordList.get(0).getComments())) {
                hmeNcDisposePlatformDTO2.setDisposeComment(mtNcRecordList.get(0).getComments());
            } else {
                HmeNcDisposePlatformDTO26 hmeNcDisposePlatformDTO26 = hmeNcCheckMapper.commentsQuery(tenantId, hmeNcDisposePlatformDTO2.getMaterialId(), hmeNcDisposePlatformDTO2.getNcCodeId(),
                        hmeNcDisposePlatformDTO2.getRootCauseOperationId(), hmeNcDisposePlatformDTO2.getEoStepActualId());
                if (hmeNcDisposePlatformDTO26 != null) {
                    hmeNcDisposePlatformDTO2.setComments(hmeNcDisposePlatformDTO26.getComments());
                } else {
                    hmeNcDisposePlatformDTO2.setDisposeComment("");
                }
            }
            for (MtNcRecord mtNcRecord : mtNcRecordList) {
                if (StringUtils.isNotEmpty(mtNcRecord.getNcCodeId())) {
                    //??????NC_CODE_ID?????????mt_nc_code??????nc_code
                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
                    ncCodeIdList.add(mtNcCode.getNcCodeId());
                    ncCodeList.add(mtNcCode.getNcCode());
                    ncReasonList.add(mtNcCode.getDescription());
                }
            }
            hmeNcDisposePlatformDTO2.setNcCodeIdList(ncCodeIdList);
            hmeNcDisposePlatformDTO2.setNcCodeList(ncCodeList);
            hmeNcDisposePlatformDTO2.setNcReasonList(ncReasonList);

            //?????????????????? & ????????????
            if(CollectionUtils.isNotEmpty(ncCodeList)){
                String ncCode = String.join(",", ncCodeList);
                hmeNcDisposePlatformDTO2.setNcCode(ncCode);
            }
            if (CollectionUtils.isNotEmpty(ncReasonList)){
                String ncReason = String.join(",",ncReasonList);
                hmeNcDisposePlatformDTO2.setNcReason(ncReason);
            }

            //???????????????  ????????????
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getNcCodeId());
//                hmeNcDisposePlatformDTO2.setNcGroupDesc(mtNcGroup.getDescription());
            hmeNcDisposePlatformDTO2.setNcType(mtNcGroup.getComponentRequired());
            String meaning = lovAdapter.queryLovMeaning("HME.NC_TYPE", tenantId, mtNcGroup.getComponentRequired());
            hmeNcDisposePlatformDTO2.setNcTypeMeaning(meaning);
            //???????????? MATERIAL_ID??????mt_material??????material_code
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getMaterialId());
            hmeNcDisposePlatformDTO2.setMaterialCode(mtMaterial.getMaterialCode());
            //????????????
            hmeNcDisposePlatformDTO2.setMaterialName(mtMaterial.getMaterialName());
            //????????????
//            MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getNcIncidentId());
//            hmeNcDisposePlatformDTO2.setNcNumber(mtNcIncident.getIncidentNumber());
            //????????????
            hmeNcDisposePlatformDTO2.setDateTime(hmeNcDisposePlatformDTO2.getDateTime());
            //????????????
            if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO2.getRootCauseWorkcellId())) {
                MtModWorkcell mtModWorkcell2 = mtModWorkcellRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getRootCauseWorkcellId());
                hmeNcDisposePlatformDTO2.setResponseWorkcellName(mtModWorkcell2.getWorkcellName());
            }
            //?????????
            MtUserInfo mtUserInfo2 = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getUserId());
            hmeNcDisposePlatformDTO2.setResponseUser(mtUserInfo2.getRealName());
            //??????
            hmeNcDisposePlatformDTO2.setStatus(hmeNcDisposePlatformDTO2.getNcStatus());
            MtGenStatus statusMeaning = mtGenStatusRepository.getGenStatus(tenantId, "NC_RECORD", "NC_RECORD_STATUS", hmeNcDisposePlatformDTO2.getNcStatus());
            hmeNcDisposePlatformDTO2.setStatusMeaning(statusMeaning.getDescription());
            //2020-08-28 add by chaonan.hu for zhenyun.ban ??????????????????????????????
            if (hmeNcDisposePlatformDTO2.getClosedUserId() != null) {
                MtUserInfo mtUserInfo3 = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getClosedUserId());
                hmeNcDisposePlatformDTO2.setDisposeUserId(hmeNcDisposePlatformDTO2.getClosedUserId().toString());
                hmeNcDisposePlatformDTO2.setDisposeUserName(mtUserInfo3.getRealName());
                hmeNcDisposePlatformDTO2.setDisposeDateTime(hmeNcDisposePlatformDTO2.getClosedDateTime());
            }
            //????????????
            HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrRepository.selectOne(new HmeNcRecordAttr() {{
                setTenantId(tenantId);
                setParentRecordId(hmeNcDisposePlatformDTO2.getNcRecordId());
            }});
            if (hmeNcRecordAttr != null) {
                hmeNcDisposePlatformDTO2.setDisposeMethod(hmeNcRecordAttr.getProcessMethod());
                String disposeMethodMeaning = lovAdapter.queryLovMeaning("HME.NC_PROCESS_METHOD", tenantId, hmeNcRecordAttr.getProcessMethod());
                hmeNcDisposePlatformDTO2.setDisposeMethodMeaning(disposeMethodMeaning);
                //????????????
                //2020-08-25 15:06 add by chaonan.hu for lu.bai ??????????????????????????????
                if (StringUtils.isNotEmpty(hmeNcRecordAttr.getAttribute1())) {
                    MtMaterial mtMaterialDb = mtMaterialRepository.selectByPrimaryKey(hmeNcRecordAttr.getAttribute1());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialId(hmeNcRecordAttr.getAttribute1());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialCode(mtMaterialDb.getMaterialCode());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialName(mtMaterialDb.getMaterialName());
                }
            }
            //2020-09-28 add by chaonan.hu for lu.bai ???????????????????????????????????????
            if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO2.getComponentMaterialId())) {
                MtMaterial mtMaterial2 = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getComponentMaterialId());
                hmeNcDisposePlatformDTO2.setScrapMaterialId(mtMaterial2.getMaterialId());
                hmeNcDisposePlatformDTO2.setScrapMaterialCode(mtMaterial2.getMaterialCode());
                hmeNcDisposePlatformDTO2.setScrapMaterialName(mtMaterial2.getMaterialName());
                hmeNcDisposePlatformDTO2.setScrapQty(hmeNcDisposePlatformDTO2.getQty() == null ? BigDecimal.ZERO : new BigDecimal(hmeNcDisposePlatformDTO2.getQty()));
            }
            // ????????????
            if (hmeNcDisposePlatformDTO2.getLoadRow() != null && hmeNcDisposePlatformDTO2.getLoadColumn() != null) {
                hmeNcDisposePlatformDTO2.setCosPosition(String.valueOf((char)(hmeNcDisposePlatformDTO2.getLoadRow()+64)) + hmeNcDisposePlatformDTO2.getLoadColumn());
            }
        }
        return ncRecordList;
    }

    private void batchHandleMaterialNc(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId, String eventRequestId) {
        // ????????????  2-?????? 3-?????? 5-?????? 6-???????????????
        switch (dto.getProcessMethod()) {
            case HmeConstants.ProcessStatus.TWO:
                this.materialRelease(tenantId, mtNcRecordList, dto, eventId, eventRequestId);
                break;
            case HmeConstants.ProcessStatus.THREE:
                this.materialScrap(tenantId, mtNcRecordList, dto, userId, eventId, eventRequestId);
                break;
            case HmeConstants.ProcessStatus.FIVE:
                this.materialBackLibrary(tenantId, mtNcRecordList, dto, eventId, eventRequestId);
                break;
            case HmeConstants.ProcessStatus.SIX:
                this.materialRework(tenantId, mtNcRecordList, dto, userId, eventId, eventRequestId);
                break;
            default:
                break;
        }
    }

    private void batchHandleProcessNc(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId) {
        // ???????????? 1-?????? 2-?????? 3-?????? 4-???????????? 7-????????????????????????
        switch (dto.getProcessMethod()) {
            case HmeConstants.ProcessStatus.ONE:
                this.processRework(tenantId, mtNcRecordList, dto, userId, eventId);
                break;
            case HmeConstants.ProcessStatus.TWO:
                this.processRelease(tenantId, mtNcRecordList, dto, userId, eventId);
                break;
            case HmeConstants.ProcessStatus.THREE:
                this.processScrap(tenantId, mtNcRecordList, dto, userId, eventId);
                break;
            case HmeConstants.ProcessStatus.FOUR:
                this.processDownturn(tenantId, mtNcRecordList, dto, userId, eventId);
                break;
            case HmeConstants.ProcessStatus.SEVEN:
                this.processRouteRework(tenantId, mtNcRecordList, dto, userId, eventId);
                break;
            default:
                break;
        }
    }

    private String getRouteByNcCode(Long tenantId, HmeNcCheckDTO4 dto, String eoId, String ncIncidentId) {
        if (CollectionUtils.isEmpty(dto.getNcCodeIdList())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        if (dto.getNcCodeIdList().size() > 1) {
            throw new MtException("HME_NC_0086", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0086", "HME"));
        }
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
        if (mtEo.getIdentification().length() < 9) {
            throw new MtException("HME_CHIP_DATA_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0009", "HME", "?????????"));
        }
        // ??????eo ??????wo?????????
        List<String> proLineList = hmeNcCheckMapper.queryProLineByEoId(tenantId, eoId);
        if (CollectionUtils.isEmpty(proLineList)) {
            throw new MtException("HME_NC_0083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0083", "HME", mtEo.getIdentification()));
        }
        // ???SN 3,4???
        String deviceType = mtEo.getIdentification().substring(2, 4);
        // ???SN 5,6???
        String chipType = mtEo.getIdentification().substring(4, 6);
        // ???????????????????????? eoJobSn ??????eo ????????????????????????
        String operationId = hmeNcCheckMapper.queryLastNonReworkOperationId(tenantId, eoId);
        List<String> routeIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(operationId)) {
            routeIdList = this.queryRouteIdListLimitOperationId(tenantId, dto.getNcGroupId(), dto.getNcCodeIdList(), proLineList.get(0), deviceType, chipType, operationId);
        }
        // ??????????????????????????? ????????????????????????
        if (CollectionUtils.isEmpty(routeIdList)) {
            routeIdList = this.queryRouteIdListNonLimitOperationId(tenantId, dto.getNcGroupId(), dto.getNcCodeIdList(), proLineList.get(0), deviceType, chipType);
        }
        MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(ncIncidentId);
        if (CollectionUtils.isEmpty(routeIdList)) {
            throw new MtException("HME_NC_0084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0084", "HME", mtNcIncident != null ? mtNcIncident.getIncidentNumber() : ""));
        }
        if (routeIdList.size() > 1) {
            throw new MtException("HME_NC_0085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0085", "HME", mtNcIncident != null ? mtNcIncident.getIncidentNumber() : ""));
        }
        return routeIdList.get(0);
    }

    @Override
    public List<String> queryRouteIdListLimitOperationId(Long tenantId, String ncGroupId, List<String> ncCodeIdList, String prodLineId, String deviceType, String chipType, String operationId) {
        List<String> routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, deviceType, chipType, operationId);
        if (CollectionUtils.isEmpty(routeIdList)) {
            // ????????? ???chipType????????????
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, deviceType, null, operationId);
        }
        if (CollectionUtils.isEmpty(routeIdList)) {
            // ????????? ???deviceType????????????
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, null, chipType, operationId);
        }
        if (CollectionUtils.isEmpty(routeIdList)) {
            // ????????? ???deviceType???chipType????????????
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, null, null, operationId);
        }
        return routeIdList;
    }

    @Override
    public List<String> queryRouteIdListNonLimitOperationId(Long tenantId, String ncGroupId, List<String> ncCodeIdList, String prodLineId, String deviceType, String chipType) {
        List<String> routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, deviceType, chipType, null);
        if (CollectionUtils.isEmpty(routeIdList)) {
            // ????????? ???chipType????????????
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, deviceType, null, null);
        }
        if (CollectionUtils.isEmpty(routeIdList)) {
            // ????????? ???deviceType????????????
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, null, chipType, null);
        }
        if (CollectionUtils.isEmpty(routeIdList)) {
            // ????????? ???deviceType???chipType????????????
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, null, null, null);
        }
        return routeIdList;
    }

    private MtRouter handleRouteCopyAndAssemblyComponent(Long tenantId, String routeId, String eoId) {
        MtRouter mtRouter = mtRouterRepository.selectByPrimaryKey(routeId);
        MtWorkOrder mtWorkOrder = hmeNcCheckMapper.queryWorkOrderByEoId(tenantId, eoId);
        // ??????????????????????????????bom???????????? ????????????????????????-????????????
        String routerName = mtRouter.getRouterName() + "-" + mtWorkOrder.getWorkOrderNum();
        List<MtRouter> mtRouterList = mtRouterRepository.selectByCondition(Condition.builder(MtRouter.class).andWhere(Sqls.custom()
                .andEqualTo(MtRouter.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtRouter.FIELD_ROUTER_NAME, routerName)).build());
        if (CollectionUtils.isNotEmpty(mtRouterList)) {
            // ?????? ????????????????????????????????????????????????????????????????????????-????????????-cid
            List<String> routerIdList = mtRouterList.stream().map(MtRouter::getRouterId).collect(Collectors.toList());
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmeNcCheckMapper.updateRouterNameByRouterIdList(tenantId, routerIdList, userId, routerName);
        }
        // ?????????????????????????????? ??????api???routerCopy
        MtRouterVO1 routerVO1 = new MtRouterVO1();
        routerVO1.setRouterId(routeId);
        routerVO1.setRouterName(routerName);
        routerVO1.setRevision("MAIN");
        routerVO1.setRouterType("MATERIAL");
        routerVO1.setSiteIds(Arrays.asList(mtWorkOrder.getSiteId()));
        routerVO1.setBomId(mtWorkOrder.getBomId());
        String newRouterId = mtRouterRepository.routerCopy(tenantId, routerVO1);
        // ????????????
        this.assemblyComponent(tenantId, newRouterId, mtWorkOrder);
        MtRouter newRouter = new MtRouter();
        newRouter.setRouterId(newRouterId);
        newRouter.setBomId(mtWorkOrder.getBomId());
        return newRouter;
    }

    private void assemblyComponent(Long tenantId, String newRouterId, MtWorkOrder mtWorkOrder) {
        // ??????????????????????????????????????????????????????
        List<HmeNcCheckVO4> newRouteOperationList = hmeNcCheckMapper.queryOperationIdAndStepName(tenantId, newRouterId);
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("HME.DESIGNED_ROUTER_OPERATION_MAPPING", tenantId);
        // ????????????????????????????????????
        newRouteOperationList.forEach(nro -> {
            Optional<LovValueDTO> firstOpt = lovValueList.stream().filter(lov -> StringUtils.equals(lov.getValue(), nro.getOperationName())).findFirst();
            if (firstOpt.isPresent()) {
                nro.setOperationName(firstOpt.get().getMeaning());
            }
        });
        List<String> operationNameList = newRouteOperationList.stream().map(HmeNcCheckVO4::getOperationName).collect(Collectors.toList());
        Map<String, List<HmeNcCheckVO4>> mtRouterOperationComponentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(operationNameList)) {
            // ??????????????????????????????????????????
            List<HmeNcCheckVO4> mtRouterOperationComponentList = hmeNcCheckMapper.queryRouterOperationComponent(tenantId, mtWorkOrder.getRouterId(), operationNameList);
            mtRouterOperationComponentMap = mtRouterOperationComponentList.stream().collect(Collectors.groupingBy(HmeNcCheckVO4::getOperationName));
        }
        // ????????????????????? ????????????
        List<MtRouterOperationComponent> insetOperationComponentList = new ArrayList<>();
        for (HmeNcCheckVO4 hmeNcCheckVO4 : newRouteOperationList) {
            List<HmeNcCheckVO4> mtRouterOperationComponents = mtRouterOperationComponentMap.getOrDefault(hmeNcCheckVO4.getOperationName(), new ArrayList<>());
            if (CollectionUtils.isNotEmpty(mtRouterOperationComponents)) {
                mtRouterOperationComponents.forEach(routerOperationComponent -> {
                    MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
                    mtRouterOperationComponent.setRouterOperationId(hmeNcCheckVO4.getRouterOperationId());
                    mtRouterOperationComponent.setBomComponentId(routerOperationComponent.getBomComponentId());
                    mtRouterOperationComponent.setSequence(routerOperationComponent.getSequence());
                    mtRouterOperationComponent.setEnableFlag(routerOperationComponent.getEnableFlag());
                    insetOperationComponentList.add(mtRouterOperationComponent);
                });
            }
        }
        // ????????????????????????
        if (CollectionUtils.isNotEmpty(insetOperationComponentList)) {
            // ?????????????????????
            List<String> componentIdList = customSequence.getNextKeys("mt_router_operation_component_s", insetOperationComponentList.size());
            List<String> componentCidList = customSequence.getNextKeys("mt_router_operation_component_cid_s", insetOperationComponentList.size());
            Integer componentIndex = 0;
            Date curDate = CommonUtils.currentTimeGet();
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            for (MtRouterOperationComponent mtRouterOperationComponent : insetOperationComponentList) {
                mtRouterOperationComponent.setCreatedBy(userId);
                mtRouterOperationComponent.setCreationDate(curDate);
                mtRouterOperationComponent.setLastUpdatedBy(userId);
                mtRouterOperationComponent.setLastUpdateDate(curDate);
                mtRouterOperationComponent.setRouterOperationComponentId(componentIdList.get(componentIndex));
                mtRouterOperationComponent.setCid(Long.valueOf(componentCidList.get(componentIndex++)));
            }

            List<List<MtRouterOperationComponent>> sqlList = CommonUtils.splitSqlList(insetOperationComponentList, 500);
            for (List<MtRouterOperationComponent> mtRouterOperationComponents : sqlList) {
                mtRouterOperationComponentRepository.batchInsertSelective(mtRouterOperationComponents);
            }
        }
    }

    private void processRouteRework(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId) {
//        List<String> eoIdList = mtNcRecordList.stream().map(MtNcRecord::getEoId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//        // ????????????????????????
//        List<HmeBatchNcCheckVO> hmeBatchNcCheckVOList = hmeNcCheckMapper.batchQueryReworkFlagByEoId(tenantId, eoIdList);
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        List<HmeEoRouterBomRel> hmeEoRouterBomRelList = new ArrayList<>();
        // ?????????????????????
        List<String> relIdList = customSequence.getNextKeys("hme_eo_router_bom_rel_s", mtNcRecordList.size());
        List<String> relCidList = customSequence.getNextKeys("hme_eo_router_bom_rel_cid_s", mtNcRecordList.size());
        Integer relIndex = 0;
        Date curDate = CommonUtils.currentTimeGet();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
//            // 2021-01-07 add by sanfeng.zhang for wang.can ????????????EO???????????????
//            // 2021-02-24 add by sanfeng.zhang for bai.lu ????????????????????? ???????????????EO???????????????
//            if (!YES.equals(dto.getReworkRecordFlag())) {
//                Optional<HmeBatchNcCheckVO> reworkFlagOpt = hmeBatchNcCheckVOList.stream().filter(vo -> StringUtils.equals(vo.getEoId(), mtNcRecord.getEoId())).findFirst();
//                String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getReworkFlag() : "";
//                if (YES.equals(reworkFlag)) {
//                    throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "HME_NC_0049", "HME"));
//                }
//            }
            //?????????????????????????????????API{materialLotAttrPropertyUpdate}???????????????????????????
            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(mtNcRecord.getMaterialLotId());
            List<MtCommonExtendVO5> attrList = new ArrayList<>();
            // ?????????????????? ???????????????????????????
            if (!YES.equals(dto.getReworkRecordFlag())) {
                MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                mtCommonExtendVO5.setAttrName("REWORK_FLAG");
                mtCommonExtendVO5.setAttrValue(YES);
                attrList.add(mtCommonExtendVO5);
            }
            // ??????????????????????????????
            String routeId = this.getRouteByNcCode(tenantId, dto, mtNcRecord.getEoId(), mtNcRecord.getNcIncidentId());
            // ?????????????????????????????????
            MtRouter mtRouter = this.handleRouteCopyAndAssemblyComponent(tenantId, routeId, mtNcRecord.getEoId());
            //2020/12/03 add by sanfeng.zhang for malifeng ???????????????
            MtCommonExtendVO5 reworkAttr = new MtCommonExtendVO5();
            reworkAttr.setAttrName("REWORK_ROUTER");
            reworkAttr.setAttrValue(mtRouter.getRouterId());
            attrList.add(reworkAttr);

            // 2021/1/6 add by sanfeng.zahng for xietianyang ????????????????????????
            MtCommonExtendVO5 flagAttr = new MtCommonExtendVO5();
            flagAttr.setAttrName("DESIGNED_REWORK_FLAG");
            flagAttr.setAttrValue(YES);
            attrList.add(flagAttr);

            // 2021/1/6 add by sanfeng.zhang for xietianyang ????????????EO????????????????????????????????????hme_eo_router_bom_rel
            HmeEoRouterBomRel hmeEoRouterBomRel = new HmeEoRouterBomRel();
            hmeEoRouterBomRel.setTenantId(tenantId);
            hmeEoRouterBomRel.setEoId(mtNcRecord.getEoId());
            hmeEoRouterBomRel.setRouterId(mtRouter.getRouterId());
            hmeEoRouterBomRel.setBomId(mtRouter.getBomId());
            hmeEoRouterBomRel.setNcRecordId(mtNcRecord.getNcRecordId());
            hmeEoRouterBomRel.setCreatedBy(userId);
            hmeEoRouterBomRel.setCreationDate(curDate);
            hmeEoRouterBomRel.setLastUpdatedBy(userId);
            hmeEoRouterBomRel.setLastUpdateDate(curDate);
            hmeEoRouterBomRel.setEoRouterBomRelId(relIdList.get(relIndex));
            hmeEoRouterBomRel.setCid(Long.valueOf(relCidList.get(relIndex++)));
            hmeEoRouterBomRelList.add(hmeEoRouterBomRel);

            if (CollectionUtils.isNotEmpty(attrList)) {
                mtCommonExtendVO6.setAttrs(attrList);
                attrPropertyList.add(mtCommonExtendVO6);
            }
            //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban ???????????????????????????????????????
            hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
        }
        //2020-08-25 add by chaonan.hu for lu.bai ???????????????????????????????????????????????????????????????
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        batchUpdateNoSiteOutData(tenantId, materialLotIdList, userId);

        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }

        if (CollectionUtils.isNotEmpty(hmeEoRouterBomRelList)) {
            List<List<HmeEoRouterBomRel>> splitSqlList = InterfaceUtils.splitSqlList(hmeEoRouterBomRelList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeEoRouterBomRel> domains : splitSqlList) {
                hmeEoRouterBomRelRepository.batchInsert(domains);
            }
        }
    }

    /**
     * ???????????? - ??????
     *
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 14:52
     */
    private void processRework(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId) {
        List<String> eoIdList = mtNcRecordList.stream().map(MtNcRecord::getEoId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ????????????????????????
        List<HmeBatchNcCheckVO> hmeBatchNcCheckVOList = hmeNcCheckMapper.batchQueryReworkFlagByEoId(tenantId, eoIdList);
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        List<HmeEoRouterBomRel> hmeEoRouterBomRelList = new ArrayList<>();
        // ?????????????????????
        List<String> relIdList = new ArrayList<>();
        List<String> relCidList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getRouterId())) {
            relIdList = customSequence.getNextKeys("hme_eo_router_bom_rel_s", mtNcRecordList.size());
            relCidList = customSequence.getNextKeys("hme_eo_router_bom_rel_cid_s", mtNcRecordList.size());
        }
        Integer relIndex = 0;
        Date curDate = CommonUtils.currentTimeGet();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            // 2021-01-07 add by sanfeng.zhang for wang.can ????????????EO???????????????
            // 2021-02-24 add by sanfeng.zhang for bai.lu ????????????????????? ???????????????EO???????????????
            if (!YES.equals(dto.getReworkRecordFlag())) {
                Optional<HmeBatchNcCheckVO> reworkFlagOpt = hmeBatchNcCheckVOList.stream().filter(vo -> StringUtils.equals(vo.getEoId(), mtNcRecord.getEoId())).findFirst();
                String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getReworkFlag() : "";
                if (YES.equals(reworkFlag)) {
                    throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0049", "HME"));
                }
            }
            //?????????????????????????????????API{materialLotAttrPropertyUpdate}???????????????????????????
            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(mtNcRecord.getMaterialLotId());
            List<MtCommonExtendVO5> attrList = new ArrayList<>();
            // ?????????????????? ???????????????????????????
            if (!YES.equals(dto.getReworkRecordFlag())) {
                MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                mtCommonExtendVO5.setAttrName("REWORK_FLAG");
                mtCommonExtendVO5.setAttrValue(YES);
                attrList.add(mtCommonExtendVO5);
            }
            //2020/12/03 add by sanfeng.zhang for malifeng ???????????????
            if (StringUtils.isNotBlank(dto.getRouterId())) {
                MtCommonExtendVO5 reworkAttr = new MtCommonExtendVO5();
                reworkAttr.setAttrName("REWORK_ROUTER");
                reworkAttr.setAttrValue(dto.getRouterId());
                attrList.add(reworkAttr);

                // 2021/1/6 add by sanfeng.zahng for xietianyang ????????????????????????
                MtCommonExtendVO5 flagAttr = new MtCommonExtendVO5();
                flagAttr.setAttrName("DESIGNED_REWORK_FLAG");
                flagAttr.setAttrValue(YES);
                attrList.add(flagAttr);
            }
            if (CollectionUtils.isNotEmpty(attrList)) {
                mtCommonExtendVO6.setAttrs(attrList);
                attrPropertyList.add(mtCommonExtendVO6);
            }
            // 2021/1/6 add by sanfeng.zhang for xietianyang ????????????EO????????????????????????????????????hme_eo_router_bom_rel
            if (StringUtils.isNotBlank(dto.getRouterId())) {
                HmeEoRouterBomRel hmeEoRouterBomRel = new HmeEoRouterBomRel();
                hmeEoRouterBomRel.setTenantId(tenantId);
                hmeEoRouterBomRel.setEoId(mtNcRecord.getEoId());
                hmeEoRouterBomRel.setRouterId(dto.getRouterId());
                MtRouter mtRouter = mtRouterRepository.selectByPrimaryKey(dto.getRouterId());
                hmeEoRouterBomRel.setBomId(mtRouter.getBomId());
                hmeEoRouterBomRel.setNcRecordId(mtNcRecord.getNcRecordId());
                hmeEoRouterBomRel.setCreatedBy(userId);
                hmeEoRouterBomRel.setCreationDate(curDate);
                hmeEoRouterBomRel.setLastUpdatedBy(userId);
                hmeEoRouterBomRel.setLastUpdateDate(curDate);
                hmeEoRouterBomRel.setEoRouterBomRelId(relIdList.get(relIndex));
                hmeEoRouterBomRel.setCid(Long.valueOf(relCidList.get(relIndex++)));
                hmeEoRouterBomRelList.add(hmeEoRouterBomRel);
            }
            //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban ???????????????????????????????????????
            hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
        }
        //2020-08-25 add by chaonan.hu for lu.bai ???????????????????????????????????????????????????????????????
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        batchUpdateNoSiteOutData(tenantId, materialLotIdList, userId);

        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }

        if (CollectionUtils.isNotEmpty(hmeEoRouterBomRelList)) {
            List<List<HmeEoRouterBomRel>> splitSqlList = InterfaceUtils.splitSqlList(hmeEoRouterBomRelList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeEoRouterBomRel> domains : splitSqlList) {
                hmeEoRouterBomRelRepository.batchInsert(domains);
            }
        }
    }

    /**
     * ????????????-??????
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param userId
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 15:39
     */
    private void processRelease(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId) {
        // materialLotBatchUpdate APi?????? ????????????????????? ???????????????
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            //???????????????????????????????????????API{materialLotUpdate}???????????????????????????
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(mtNcRecord.getMaterialLotId());
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setQualityStatus(OK);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
        }
    }

    /**
     * ????????????-??????
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param userId
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 15:43
     */
    private void processScrap(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId) {
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            Optional<MtMaterialLot> materialLotOpt = materialLotList.stream().filter(mml -> StringUtils.equals(mml.getMaterialLotId(), mtNcRecord.getMaterialLotId())).findFirst();
            MtMaterialLot mtMaterialLot = materialLotOpt.get();
            //???????????????????????????????????????API{eoScrap}??????????????????API{materialLotUpdate}????????????
            MtEoActualVO6 mtEoActualVO6 = new MtEoActualVO6();
            mtEoActualVO6.setEoId(mtNcRecord.getEoId());
            mtEoActualVO6.setTrxScrappedQty(mtMaterialLot.getPrimaryUomQty());
            mtEoActualVO6.setParentEventId(eventId);
            mtEoActualRepository.eoScrap(tenantId, mtEoActualVO6);
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(mtNcRecord.getMaterialLotId());
            mtMaterialLotVO20.setEnableFlag(NO);
            mtMaterialLotVO20List.add(mtMaterialLotVO20);
            //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban ???????????????????????????????????????
            hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
            //????????????????????????????????????EO ??????????????????EO???????????? By ?????? 2021-11-15
            //???????????????
            MtEo mtEo = mtEoRepository.selectByPrimaryKey(mtNcRecord.getEoId());
            boolean newMaterialLot = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, mtEo.getIdentification());
            if (!newMaterialLot){
                List<String> eoIds = hmeEoJobSnReWorkMapper.queryReworkEoIdsByOldCode(tenantId,mtEo.getIdentification());
                //??????????????????????????????EO????????????
                eoIds.forEach(e->{
                    MtEoActualVO6 mtEoActualVO7 = new MtEoActualVO6();
                    mtEoActualVO7.setEoId(e);
                    mtEoActualVO7.setTrxScrappedQty(mtMaterialLot.getPrimaryUomQty());
                    mtEoActualVO7.setParentEventId(eventId);
                    mtEoActualRepository.eoScrap(tenantId, mtEoActualVO7);
                });
                //??????????????? && ??????
                List<String> materialLotIds = hmeEoJobSnReWorkMapper.queryNewMaterialLotByOldMaterialLot(tenantId,mtMaterialLot.getMaterialLotId());
                materialLotIds.forEach(e->{
                    MtMaterialLotVO20 mtMaterialLotVO21 = new MtMaterialLotVO20();
                    mtMaterialLotVO21.setMaterialLotId(e);
                    mtMaterialLotVO21.setEnableFlag(NO);
                    mtMaterialLotVO20List.add(mtMaterialLotVO21);
                    hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, e);
                });
            }
        }
        if (CollectionUtils.isNotEmpty(mtMaterialLotVO20List)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
        //2020-09-08 21:00 add by chaonan.hu for lu.bai ?????????????????????????????????
        batchUpdateNoSiteOutData(tenantId, materialLotIdList, userId);
    }

    /**
     * ????????????
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param userId
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 16:13
     */
    private void processDownturn(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId) {
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        //2020-08-25 add by chaonan.hu for lu.bai ???????????????????????????
        if (StringUtils.isEmpty(dto.getTransitionMaterialId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "????????????"));
        }
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList();
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            //?????????????????????ID
            Optional<MtMaterialLot> materialLotOpt = materialLotList.stream().filter(mml -> StringUtils.equals(mml.getMaterialLotId(), mtNcRecord.getMaterialLotId())).findFirst();
            MtMaterialLot mtMaterialLot = materialLotOpt.get();
            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(mtNcRecord.getMaterialLotId());
            List<MtCommonExtendVO5> attrList = new ArrayList<>();
            MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
            mtCommonExtendVO5.setAttrName("PERFORMANCE_LEVEL");
            mtCommonExtendVO5.setAttrValue(mtMaterialLot.getMaterialId());
            attrList.add(mtCommonExtendVO5);
            mtCommonExtendVO6.setAttrs(attrList);
            attrPropertyList.add(mtCommonExtendVO6);

            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(mtNcRecord.getMaterialLotId());
            mtMaterialLotVO20.setMaterialId(dto.getTransitionMaterialId());
            mtMaterialLotVO20.setQualityStatus(OK);
            mtMaterialLotVO20List.add(mtMaterialLotVO20);

            //EO??????
            MtEoVO mtEoVO = new MtEoVO();
            mtEoVO.setEventId(eventId);
            mtEoVO.setEoId(mtNcRecord.getEoId());
            mtEoVO.setMaterialId(dto.getTransitionMaterialId());
            mtEoRepository.eoUpdate(tenantId, mtEoVO, NO);
        }
        // ??????????????????
        if (CollectionUtils.isNotEmpty(mtMaterialLotVO20List)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
    }

    /**
     * ????????????-??????
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 16:15
     */
    private void materialRelease(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, String eventId, String eventRequestId) {
        // ????????????????????????????????????
        List<String> mtNcRecordIdList = mtNcRecordList.stream().map(MtNcRecord::getNcRecordId).distinct().collect(Collectors.toList());
        List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.selectByCondition(Condition.builder(HmeNcRecordAttr.class).andWhere(Sqls.custom()
                .andEqualTo(HmeNcRecordAttr.FIELD_TENANT_ID, tenantId)
                .andIn(HmeNcRecordAttr.FIELD_PARENT_RECORD_ID, mtNcRecordIdList)).build());
        // ???????????????
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);

        //????????????????????????????????????
        MtExtendVO1 mtExtendVO = new MtExtendVO1();
        mtExtendVO.setTableName("mt_material_lot_attr");
        mtExtendVO.setKeyIdList(materialLotIdList);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
        // ????????????????????????????????????
        this.verifyMaterialNcList(tenantId, mtNcRecordList, materialLotIdList, extendAttrVO1List, HmeConstants.ConstantValue.FX);
        // ??????????????????
        List<HmeNcRecordAttr> hmeNcRecordAttrList = new ArrayList<>();
        Map<String, List<HmeNcRecordAttr>> recordAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ncRecordAttrList)) {
            recordAttrMap = ncRecordAttrList.stream().collect(Collectors.groupingBy(HmeNcRecordAttr::getParentRecordId));
        }
        //??????LOV??????
        List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            //??????????????????
            Boolean handleFlag;
            //?????????????????????
            Optional<HmeNcRecordAttr> nrAttrOpt = ncRecordAttrList.stream().filter(nrAttr -> StringUtils.equals(nrAttr.getParentRecordId(), mtNcRecord.getNcRecordId())).findFirst();
            HmeNcRecordAttr ncRecordAttr = nrAttrOpt.isPresent() ? nrAttrOpt.get() : null;
            String returnMaterialFlag = ncRecordAttr != null ? ncRecordAttr.getAttribute2() : "";
            // ?????????????????????????????????
            Boolean cosMaterialFlag = false;
            List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(mtNcRecord.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                List<String> collect = itemGroupList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                    cosMaterialFlag = true;
                }
            }

            if (StringUtils.equals(returnMaterialFlag, YES)) {
                //?????????????????? ?????????
                handleFlag = false;
            } else {
                //?????????????????? ???????????? ?????? ?????????????????????
                handleFlag = true;
            }

            if (handleFlag) {
                String nrCloseEventId = "";
                if (mtNcRecordList.size() == 1) {
                    // ????????????
                    nrCloseEventId = eventId;
                } else {
                    MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
                    mtEventCreateVO.setEventTypeCode("NC_RECORD_CLOSE");
                    mtEventCreateVO.setEventRequestId(eventRequestId);
                    nrCloseEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
                }
                Optional<MtMaterialLot> mtMaterialLotOpt = materialLotList.stream().filter(barcode -> StringUtils.equals(barcode.getMaterialLotId(), mtNcRecord.getMaterialLotId())).findFirst();
                if (!mtMaterialLotOpt.isPresent()) {
                    MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(mtNcRecord.getNcIncidentId());
                    throw new MtException("HME_NC_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0069", "HME", mtNcIncident != null ? mtNcIncident.getIncidentNumber() : ""));
                }
                MtMaterialLot mtMaterialLot = mtMaterialLotOpt.get();
                List<HmeNcRecordAttr> ncRecordAttrs = recordAttrMap.get(mtNcRecord.getNcRecordId());
                if (!cosMaterialFlag) {
                    //????????????
                    MtMaterialLot inputMaterialLot = null;
                    if (StringUtils.isNotBlank(dto.getBarcode())) {
                        inputMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getBarcode());
                    }
                    // ???????????????????????????
                    String barcodeId = this.handleInputBarcode(tenantId, inputMaterialLot, mtMaterialLot, mtNcRecord.getQty(), dto.getBarcode(), nrCloseEventId, HmeConstants.ConstantValue.FX);
                    // ????????????????????????
                    List<MtCommonExtendVO6> mtCommonExtendVO6List = this.batchMaterialLotAttrUpdate(tenantId, dto.getBarcode(), barcodeId, inputMaterialLot, mtMaterialLot, extendAttrVO1List, nrCloseEventId, HmeConstants.ConstantValue.FX);
                    // ??????????????????????????? ?????????????????? ???????????????????????????
                    if (CollectionUtils.isNotEmpty(mtCommonExtendVO6List)) {
                        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", nrCloseEventId,
                                mtCommonExtendVO6List);
                    }
                    // ?????????????????????????????? ?????????id??????????????????Attribute3
                    if (CollectionUtils.isNotEmpty(ncRecordAttrs)) {
                        HmeNcRecordAttr hmeNcRecordAttr = ncRecordAttrs.get(0);
                        hmeNcRecordAttr.setAttribute3(barcodeId);
                        hmeNcRecordAttrList.add(hmeNcRecordAttr);
                    }
                } else {
                    // ??????????????????
                    Boolean feelFlag = judgeFeedByMaterialLot(tenantId, mtNcRecord.getEoId(), mtNcRecord.getMaterialLotId());
                    if (!feelFlag) {
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        mtMaterialLotVO20.setTrxPrimaryUomQty(mtNcRecord.getQty());
                        mtMaterialLotVO20List.add(mtMaterialLotVO20);
                    }
                }

                //??????API{onhandQtyUpdateProcess}??????????????????
                MtEventCreateVO mtEventCreateVO2 = new MtEventCreateVO();
                mtEventCreateVO2.setEventTypeCode("NC_RECORD_MATERIAL_FX");
                mtEventCreateVO2.setEventRequestId(eventRequestId);
                String eventId2 = mtEventRepository.eventCreate(tenantId, mtEventCreateVO2);
                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
                mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                mtInvOnhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                mtInvOnhandQuantityVO9.setEventId(eventId2);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                // ????????????????????????????????? ???????????????????????????
                String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                // ??????API{onhandQtyUpdateProcess}?????????????????????
                MtEventCreateVO returnEventVO = new MtEventCreateVO();
                returnEventVO.setEventTypeCode("NC_RECORD_MATERIAL_CODE_RETURN");
                returnEventVO.setEventRequestId(eventRequestId);
                String returnEvent = mtEventRepository.eventCreate(tenantId, returnEventVO);
                MtInvOnhandQuantityVO9 onhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                onhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                onhandQuantityVO9.setMaterialId(mtNcRecord.getComponentMaterialId());
                onhandQuantityVO9.setLocatorId(locatorId);
                onhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                onhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                onhandQuantityVO9.setEventId(returnEvent);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, onhandQuantityVO9);
                // ??????????????? ????????????????????????
                if (cosMaterialFlag) {
                    if (CollectionUtils.isNotEmpty(ncRecordAttrs)) {
                        HmeNcRecordAttr hmeNcRecordAttr = ncRecordAttrs.get(0);
                        if (StringUtils.isNotBlank(hmeNcRecordAttr.getAttribute7())) {
                            List<HmeMaterialLotNcLoad> lotNcLoadList = hmeMaterialLotNcLoadMapper.select(new HmeMaterialLotNcLoad() {{
                                setTenantId(tenantId);
                                setLoadSequence(hmeNcRecordAttr.getAttribute7());
                            }});
                            if (CollectionUtils.isNotEmpty(lotNcLoadList)) {
                                hmeNcCheckMapper.batchDeleteLoadByIds(tenantId, lotNcLoadList.stream().map(HmeMaterialLotNcLoad::getNcLoadId).collect(Collectors.toList()));
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(mtMaterialLotVO20List)) {
            // ???????????? ???????????????????????????
            List<MtMaterialLotVO20> mtMaterialLotVO20s = new ArrayList<>();
            Map<String, List<MtMaterialLotVO20>> materialLotMap = mtMaterialLotVO20List.stream().collect(Collectors.groupingBy(MtMaterialLotVO20::getMaterialLotId));
            for (Map.Entry<String, List<MtMaterialLotVO20>> materialLotListEntry : materialLotMap.entrySet()) {
                List<MtMaterialLotVO20> value = materialLotListEntry.getValue();
                // ???????????????
                double totalQty = value.stream().map(MtMaterialLotVO20::getTrxPrimaryUomQty).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(value.get(0).getMaterialLotId());
                mtMaterialLotVO20.setTrxPrimaryUomQty(totalQty);
                mtMaterialLotVO20s.add(mtMaterialLotVO20);
            }
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20s, eventId, NO);
        }
        // ??????????????????
        if (CollectionUtils.isNotEmpty(hmeNcRecordAttrList)) {
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmeNcRecordAttrMapper.myBatchUpdate(tenantId, userId, hmeNcRecordAttrList);
        }
    }

    /**
     * ????????????- ??????
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param userId
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 16:15
     */
    private void materialScrap(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId, String eventRequestId) {
        // ????????????????????? ???????????????
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            //?????????????????????
            //2020-09-09 11:10 add by chaonan.hu for lu.bai ??????????????????????????????
            materialScrap(tenantId, mtMaterialLot, mtNcRecord, eventId, eventRequestId);
        }
    }

    /**
     * ????????????-??????
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 16:20
     */
    private void materialBackLibrary(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, String eventId, String eventRequestId) {
        // ????????????
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        //????????????????????????????????????
        MtExtendVO1 mtExtendVO = new MtExtendVO1();
        mtExtendVO.setTableName("mt_material_lot_attr");
        mtExtendVO.setKeyIdList(materialLotIdList);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
        // ?????????????????????????????????????????????
        this.verifyMaterialNcList(tenantId, mtNcRecordList, materialLotIdList, extendAttrVO1List, HmeConstants.ConstantValue.TK);
        // ????????????????????????????????????
        List<String> mtNcRecordIdList = mtNcRecordList.stream().map(MtNcRecord::getNcRecordId).distinct().collect(Collectors.toList());
        List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.selectByCondition(Condition.builder(HmeNcRecordAttr.class).andWhere(Sqls.custom()
                .andEqualTo(HmeNcRecordAttr.FIELD_TENANT_ID, tenantId)
                .andIn(HmeNcRecordAttr.FIELD_PARENT_RECORD_ID, mtNcRecordIdList)).build());
        Map<String, List<HmeNcRecordAttr>> recordAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ncRecordAttrList)) {
            recordAttrMap = ncRecordAttrList.stream().collect(Collectors.groupingBy(HmeNcRecordAttr::getParentRecordId));
        }

        // ??????????????????
        List<HmeNcRecordAttr> hmeNcRecordAttrList = new ArrayList<>();
        //??????LOV??????
        List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            // ?????????????????????????????????
            Boolean cosMaterialFlag = false;
            List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(mtNcRecord.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                List<String> collect = itemGroupList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                    cosMaterialFlag = true;
                }
            }
            // ???????????????????????????
            if (cosMaterialFlag) {
                throw new MtException("HME_NC_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0075", "HME"));

            }
            //??????????????????
            Boolean handleFlag;
            //?????????????????????
            List<HmeNcRecordAttr> hmeNcRecordAttrs = recordAttrMap.get(mtNcRecord.getNcRecordId());
            String returnMaterialFlag = CollectionUtils.isNotEmpty(hmeNcRecordAttrs) ? hmeNcRecordAttrs.get(0).getAttribute2() : "";
            //?????????????????? ?????????
            //????????? ???????????? ?????? ?????????????????????
            if (StringUtils.equals(returnMaterialFlag, YES)) {
                //?????????????????? ?????????
                handleFlag = false;
            } else {
                //?????????????????? ???????????? ?????? ?????????????????????
                handleFlag = true;
            }

            if (handleFlag) {
                String nrCloseEventId = "";
                if (mtNcRecordList.size() == 1) {
                    // ????????????
                    nrCloseEventId = eventId;
                } else {
                    MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
                    mtEventCreateVO.setEventTypeCode("NC_RECORD_CLOSE");
                    mtEventCreateVO.setEventRequestId(eventRequestId);
                    nrCloseEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
                }
                //????????????
                Optional<MtMaterialLot> mtMaterialLotOpt = materialLotList.stream().filter(barcode -> StringUtils.equals(barcode.getMaterialLotId(), mtNcRecord.getMaterialLotId())).findFirst();
                if (!mtMaterialLotOpt.isPresent()) {
                    MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(mtNcRecord.getNcIncidentId());
                    throw new MtException("HME_NC_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0069", "HME", mtNcIncident != null ? mtNcIncident.getIncidentNumber() : ""));
                }
                MtMaterialLot sourceMaterialLot = mtMaterialLotOpt.get();
                //????????????
                MtMaterialLot inputMaterialLot = null;
                if (StringUtils.isNotBlank(dto.getBarcode())) {
                    inputMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getBarcode());
                }
                // ???????????????????????????
                String barcodeId = this.handleInputBarcode(tenantId, inputMaterialLot, sourceMaterialLot, mtNcRecord.getQty(), dto.getBarcode(), nrCloseEventId, HmeConstants.ConstantValue.TK);
                // ????????????????????????
                List<MtCommonExtendVO6> mtCommonExtendVO6List = this.batchMaterialLotAttrUpdate(tenantId, dto.getBarcode(), barcodeId, inputMaterialLot, sourceMaterialLot, extendAttrVO1List, nrCloseEventId, HmeConstants.ConstantValue.TK);
                // ??????????????????????????? ?????????????????? ???????????????????????????
                if (CollectionUtils.isNotEmpty(mtCommonExtendVO6List)) {
                    mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", nrCloseEventId,
                            mtCommonExtendVO6List);
                }
                // ?????????????????????????????? ?????????id??????????????????Attribute3
                List<HmeNcRecordAttr> ncRecordAttrs = recordAttrMap.get(mtNcRecord.getNcRecordId());
                if (CollectionUtils.isNotEmpty(ncRecordAttrs)) {
                    HmeNcRecordAttr hmeNcRecordAttr = ncRecordAttrs.get(0);
                    hmeNcRecordAttr.setAttribute3(barcodeId);
                    hmeNcRecordAttrList.add(hmeNcRecordAttr);
                }

                //??????????????????
                MtEventCreateVO mtEventCreateVO2 = new MtEventCreateVO();
                mtEventCreateVO2.setEventTypeCode("NC_RECORD_MATERIAL_TK");
                mtEventCreateVO2.setEventRequestId(eventRequestId);
                String eventId2 = mtEventRepository.eventCreate(tenantId, mtEventCreateVO2);
                //???????????????
                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO9.setSiteId(sourceMaterialLot.getSiteId());
                mtInvOnhandQuantityVO9.setMaterialId(sourceMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO9.setLocatorId(sourceMaterialLot.getLocatorId());
                mtInvOnhandQuantityVO9.setLotCode(sourceMaterialLot.getLot());
                mtInvOnhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                mtInvOnhandQuantityVO9.setEventId(eventId2);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                // ????????????????????????????????? ???????????????????????????
                String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                // ??????API{onhandQtyUpdateProcess}?????????????????????
                MtEventCreateVO returnEventVO = new MtEventCreateVO();
                returnEventVO.setEventTypeCode("NC_RECORD_MATERIAL_CODE_RETURN");
                returnEventVO.setEventRequestId(eventRequestId);
                String returnEvent = mtEventRepository.eventCreate(tenantId, returnEventVO);
                MtInvOnhandQuantityVO9 onhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                onhandQuantityVO9.setSiteId(sourceMaterialLot.getSiteId());
                onhandQuantityVO9.setMaterialId(mtNcRecord.getComponentMaterialId());
                onhandQuantityVO9.setLocatorId(locatorId);
                onhandQuantityVO9.setLotCode(sourceMaterialLot.getLot());
                onhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                onhandQuantityVO9.setEventId(returnEvent);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, onhandQuantityVO9);
            }
        }
        // ??????????????????
        if (CollectionUtils.isNotEmpty(hmeNcRecordAttrList)) {
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmeNcRecordAttrMapper.myBatchUpdate(tenantId, userId, hmeNcRecordAttrList);
        }
    }

    /**
     * ????????????????????? ????????????????????????????????????
     *
     * @param tenantId
     * @param workcellId
     * @author sanfeng.zhang@hand-china.com 2021/4/21 14:58
     * @return java.lang.String
     */
    private String queryNcStorageLocatorByProdLine(Long tenantId, String workcellId) {
        String prodLineId = hmeNcCheckMapper.queryProLineByWorkcellId(tenantId, workcellId);
        if (StringUtils.isBlank(prodLineId)) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
            throw new MtException("HME_CHIP_DATA_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0012", "HME", mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : ""));
        }
        List<String> locatorIdList = hmeNcCheckMapper.queryNcStorageLocatorByProdLine(tenantId, prodLineId);
        if (CollectionUtils.isEmpty(locatorIdList) || locatorIdList.size() > 1) {
            throw new MtException("HME_NC_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0058", "HME"));
        }
        return locatorIdList.get(0);
    }


    private void verifyMaterialNcList(Long tenantId, List<MtNcRecord> mtNcRecordList, List<String> materialLotIdList, List<MtExtendAttrVO1> extendAttrVO1List, String flag) {
        // ?????????????????????????????????????????????????????????????????????
        List<String> workcellIdList = mtNcRecordList.stream().map(MtNcRecord::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        //??????????????????
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        List<HmeBatchNcCheckVO2> hmeBatchNcCheckVO2s = hmeNcCheckMapper.batchQueryLineWorkcellByWorkcell(tenantId, defaultSiteId, workcellIdList);
        Set<String> wkcList = new HashSet<>();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            Optional<HmeBatchNcCheckVO2> wkcOpt = hmeBatchNcCheckVO2s.stream().filter(wkc -> StringUtils.equals(wkc.getWorkcellId(), mtNcRecord.getWorkcellId())).findFirst();
            if (!wkcOpt.isPresent()) {
                throw new MtException("HME_NC_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0059", "HME"));
            }
            wkcList.add(wkcOpt.get().getLineWorkcellId());
        }
        if (wkcList.size() > 1) {
            throw new MtException("HME_NC_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0062", "HME"));
        }
        List<String> componentMaterialIdList = mtNcRecordList.stream().map(MtNcRecord::getComponentMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(componentMaterialIdList) && componentMaterialIdList.size() > 1) {
            throw new MtException("HME_NC_0063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0063", "HME"));
        }
        Map<String, List<MtExtendAttrVO1>> attrMap = extendAttrVO1List.stream().collect(Collectors.groupingBy(attr -> attr.getKeyId() + "_" + attr.getAttrName()));
        Set<String> mvStrList = new HashSet<>();
        Set<String> sLotStrList = new HashSet<>();
        for (String materialLotId : materialLotIdList) {
            // ????????????
            List<MtExtendAttrVO1> mvList = attrMap.get(materialLotId + "_" + "MATERIAL_VERSION");
            if (CollectionUtils.isNotEmpty(mvList)) {
                mvStrList.add(mvList.get(0).getAttrValue());
            } else {
                mvStrList.add("");
            }
            // ?????? ?????????????????????
            if (StringUtils.equals(flag, HmeConstants.ConstantValue.TK)) {
                // ???????????????
                List<MtExtendAttrVO1> sLotList = attrMap.get(materialLotId + "_" + "SUPPLIER_LOT");
                if (CollectionUtils.isNotEmpty(sLotList)) {
                    sLotStrList.add(sLotList.get(0).getAttrValue());
                } else {
                    sLotStrList.add("");
                }
            }
        }
        if (mvStrList.size() > 1) {
            throw new MtException("HME_NC_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0064", "HME"));
        }
        if (StringUtils.equals(flag, HmeConstants.ConstantValue.TK) && sLotStrList.size() > 1) {
            throw new MtException("HME_NC_0065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0065", "HME"));
        }
    }


    /**
     * ????????????-???????????????
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param userId
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 16:20
     */
    private void materialRework(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId, String eventRequestId) {
        // ????????????
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        // ???????????????
        List<String> componentMaterialIdList = mtNcRecordList.stream().map(MtNcRecord::getComponentMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<MtMaterial> mtMaterialList = mtMaterialRepository.selectByCondition(Condition.builder(MtMaterial.class).andWhere(Sqls.custom()
                .andEqualTo(MtMaterial.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtMaterial.FIELD_MATERIAL_ID, componentMaterialIdList)).build());
        List<String> materialLotCodeList = materialLotList.stream().map(MtMaterialLot::getMaterialLotCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ????????????????????????
        List<HmeRepairWorkOrderCreate> workOrderCreateList = hmeRepairWorkOrderCreateRepository.selectByCondition(Condition.builder(HmeRepairWorkOrderCreate.class).andWhere(Sqls.custom()
                .andEqualTo(HmeRepairWorkOrderCreate.FIELD_TENANT_ID, tenantId)
                .andIn(HmeRepairWorkOrderCreate.FIELD_MATERIAL_LOT_CODE, materialLotCodeList)).build());
        List<String> workOrderNumList = workOrderCreateList.stream().map(HmeRepairWorkOrderCreate::getWorkOrderNum).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ??????
        List<MtWorkOrder> mtWorkOrderList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workOrderNumList)) {
            mtWorkOrderList = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class).andWhere(Sqls.custom()
                    .andEqualTo(MtWorkOrder.FIELD_TENANT_ID, tenantId)
                    .andIn(MtWorkOrder.FIELD_WORK_ORDER_NUM, workOrderNumList)).build());
        }
        // ??????????????????????????????
        List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.WO_PRODUCTION_VERSION", tenantId);
        //??????LOV??????
        List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtCommonExtendVO7> mtCommonExtendVO7List = new ArrayList<>();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            // ?????????????????????????????????
            Boolean cosMaterialFlag = false;
            List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(mtNcRecord.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                List<String> collect = itemGroupList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                    cosMaterialFlag = true;
                }
            }
            // ???????????????????????????
            if (cosMaterialFlag) {
                throw new MtException("HME_NC_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0075", "HME"));

            }
            Optional<MtMaterial> materialOpt = mtMaterialList.stream().filter(mm -> StringUtils.equals(mm.getMaterialId(), mtNcRecord.getComponentMaterialId())).findFirst();
            MtMaterial mtMaterial = materialOpt.isPresent() ? materialOpt.get() : null;
            // ??????????????????1 ?????????
            if (mtNcRecord.getQty().compareTo(1D) != 0) {
                throw new MtException("HME_NC_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0051", "HME", mtMaterial != null ? mtMaterial.getMaterialName() : ""));
            }
            // ????????????????????????????????????
            Optional<MtMaterialLot> materialLotOpt = materialLotList.stream().filter(mml -> StringUtils.equals(mtNcRecord.getMaterialLotId(), mml.getMaterialLotId())).findFirst();
            MtMaterialLot mtMaterialLot = materialLotOpt.isPresent() ? materialLotOpt.get() : null;
            if (mtMaterialLot != null) {
                List<HmeRepairWorkOrderCreate> wocList = workOrderCreateList.stream().filter(woc -> StringUtils.equals(woc.getMaterialLotCode(), mtMaterialLot.getMaterialLotCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(wocList)) {
                    // ???????????? ????????????????????? ???NEW/RELEASED/EORELEASED ?????????
                    List<MtWorkOrder> woList = mtWorkOrderList.stream().filter(wo -> StringUtils.equals(wo.getWorkOrderNum(), wocList.get(0).getWorkOrderNum())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(woList)) {
                        MtWorkOrder mtWorkOrder = woList.get(0);
                        if (HmeConstants.WorkOrderStatus.NEW.equals(mtWorkOrder.getStatus()) || HmeConstants.WorkOrderStatus.RELEASED.equals(mtWorkOrder.getStatus()) || HmeConstants.WorkOrderStatus.EORELEASED.equals(mtWorkOrder.getStatus())) {
                            throw new MtException("HME_NC_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_NC_0052", "HME", mtMaterialLot.getMaterialLotCode()));
                        }
                    }
                }
                // ?????????????????????API{materialLotUpdate}
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotVO20.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                mtMaterialLotVO20.setSiteId(mtMaterialLot.getSiteId());
                mtMaterialLotVO20.setEnableFlag(YES);
                mtMaterialLotVO20.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO20.setTrxPrimaryUomQty(mtNcRecord.getQty());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);

                // ????????????????????????Y
                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(mtMaterialLot.getMaterialLotId());
                List<MtCommonExtendVO4> attrList = new ArrayList<>();
                MtCommonExtendVO4 extendVO4 = new MtCommonExtendVO4();
                extendVO4.setAttrName("REWORK_FLAG");
                extendVO4.setAttrValue(YES);
                attrList.add(extendVO4);
                mtCommonExtendVO7.setAttrs(attrList);

                // ??????SAP???????????????????????????????????????????????????
                HmeRepairWorkOrderCreate workOrderCreate = new HmeRepairWorkOrderCreate();
                workOrderCreate.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                workOrderCreate.setQty(mtNcRecord.getQty());
                if (mtMaterial != null) {
                    workOrderCreate.setMaterialCode(mtMaterial.getMaterialCode());
                    // ????????????
                    MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                    workOrderCreate.setPrimaryUomCode(mtUom != null ? mtUom.getUomCode() : "");
                }
                // ??????
                MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(mtNcRecord.getSiteId());
                workOrderCreate.setSiteCode(mtModSite != null ? mtModSite.getSiteCode() : "");
                // ???????????? (?????????????????????????????????mt_mod_organization_rel???????????????)
                List<MtModLocator> locatorList = hmeNcCheckMapper.queryLocatorIdByWorkcellId(tenantId, mtNcRecord.getWorkcellId());
                String warehouseCode = "";
                if (CollectionUtils.isNotEmpty(locatorList)) {
                    MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorList.get(0).getParentLocatorId());
                    warehouseCode = mtModLocator != null ? mtModLocator.getLocatorCode() : "";
                }
                workOrderCreate.setLocatorCode(warehouseCode);
                Date startDate = new Date();
                workOrderCreate.setPlanStartTime(startDate);
                workOrderCreate.setPlanEndTime(DateUtils.addDays(startDate, 30));
                // ???????????????????????????????????????
                if (CollectionUtils.isNotEmpty(valueDTOList)) {
                    workOrderCreate.setProductionVersion(valueDTOList.get(0).getValue());
                }
                HmeRepairWorkOrderCreate resultData = itfRepairWorkOrderCreateService.hmeRepairWorkOrderCreateService(tenantId, workOrderCreate);
                // ????????????????????????, ???????????????????????????????????????,????????????
                if (StringUtils.isBlank(resultData.getStatus())) {
                    throw new MtException("HME_NC_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0053", "HME"));
                }
                // ??????????????????E, ???????????????????????? ??????????????????
                if (StringUtils.equals(resultData.getStatus(), HmeConstants.ConstantValue.E)) {
                    throw new CommonException(resultData.getMessage());
                }

                List<HmeNcRecordAttr> hmeNcRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
                    setTenantId(tenantId);
                    setParentRecordId(mtNcRecord.getNcRecordId());
                }});
                if (CollectionUtils.isNotEmpty(hmeNcRecordAttrList)) {
                    HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrList.get(0);
                    hmeNcRecordAttr.setAttribute5(resultData.getWorkOrderNum());
                    hmeNcRecordAttrMapper.updateByPrimaryKeySelective(hmeNcRecordAttr);
                }

                // ?????????????????????????????? ???????????????????????????
                String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                // ??????API{onhandQtyUpdateProcess}?????????????????????
                MtEventCreateVO returnEventVO = new MtEventCreateVO();
                returnEventVO.setEventTypeCode("NC_RECORD_MATERIAL_CODE_RETURN");
                returnEventVO.setEventRequestId(eventRequestId);
                String returnEvent = mtEventRepository.eventCreate(tenantId, returnEventVO);
                MtInvOnhandQuantityVO9 onhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                onhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                onhandQuantityVO9.setMaterialId(mtNcRecord.getComponentMaterialId());
                onhandQuantityVO9.setLocatorId(locatorId);
                onhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                onhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                onhandQuantityVO9.setEventId(returnEvent);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, onhandQuantityVO9);

                // ??????????????????????????????????????? ????????????
                String recordEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO(){{
                    setEventRequestId(eventRequestId);
                    setEventTypeCode("NC_RECORD_SELFMADE_MATERIAL_REPAIR");
                }});
                // ?????????????????????API???onhandQtyUpdateProcess
                MtInvOnhandQuantityVO9 recordQuantity = new MtInvOnhandQuantityVO9();
                recordQuantity.setSiteId(mtMaterialLot.getSiteId());
                recordQuantity.setMaterialId(mtMaterialLot.getMaterialId());
                recordQuantity.setLocatorId(mtMaterialLot.getLocatorId());
                recordQuantity.setLotCode(mtMaterialLot.getLot());
                recordQuantity.setChangeQuantity(mtNcRecord.getQty());
                recordQuantity.setEventId(recordEventId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, recordQuantity);
            }
        }
        if (CollectionUtils.isNotEmpty(mtMaterialLotVO20List)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
        if (CollectionUtils.isNotEmpty(mtCommonExtendVO7List)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId,  mtCommonExtendVO7List);
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param eventId
     * @param eventRequestId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 14:34
     */
    private void batchHandleNcRecordClose(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId, String eventRequestId) {
        List<String> ncRecordIdList = mtNcRecordList.stream().map(MtNcRecord::getNcRecordId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // ????????????????????????????????????????????????????????????????????????????????????
        List<MtNcRecord> nonNcCodeList = mtNcRecordList.stream().filter(nr -> !StringUtils.equals(nr.getNcCodeId(), dto.getNcGroupId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(nonNcCodeList)) {
            List<MtNcRecord> updateNcRecordList = new ArrayList<>();
            List<MtNcRecordHis> mtNcRecordHisList = new ArrayList<>();
            List<String> hisIdList = customSequence.getNextKeys("mt_nc_record_his_s", nonNcCodeList.size());
            List<String> hisCidList = customSequence.getNextKeys("mt_nc_record_his_cid_s", nonNcCodeList.size());
            Integer hisIndex = 0;
            for (MtNcRecord mtNcRecord : nonNcCodeList) {
                mtNcRecord.setNcCodeId(dto.getNcGroupId());
                updateNcRecordList.add(mtNcRecord);
                // ????????????
                MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
                BeanUtils.copyProperties(mtNcRecord, mtNcRecordHis);
                mtNcRecordHis.setNcCodeId(dto.getNcGroupId());
                mtNcRecordHis.setEventId(eventId);
                mtNcRecordHis.setNcRecordHisId(hisIdList.get(hisIndex));
                mtNcRecordHis.setCid(Long.valueOf(hisCidList.get(hisIndex++)));
                mtNcRecordHis.setObjectVersionNumber(1L);
                mtNcRecordHis.setCreatedBy(userId);
                mtNcRecordHis.setCreationDate(new Date());
                mtNcRecordHis.setLastUpdatedBy(userId);
                mtNcRecordHis.setLastUpdateDate(new Date());
                mtNcRecordHisList.add(mtNcRecordHis);
            }
            // ????????????????????????
            if (CollectionUtils.isNotEmpty(updateNcRecordList)) {
                hmeNcCheckMapper.batchUpdateNcCodeId(tenantId, userId, updateNcRecordList);
            }
            // ??????????????????????????????
            if (CollectionUtils.isNotEmpty(mtNcRecordHisList)) {
                mtNcRecordHisRepository.batchInsertSelective(mtNcRecordHisList);
            }
        }
        // ??????????????????????????????????????????????????????
        List<HmeNcRecordAttr> recordAttrList = hmeNcRecordAttrRepository.selectByCondition(Condition.builder(HmeNcRecordAttr.class).andWhere(Sqls.custom()
                .andEqualTo(HmeNcRecordAttr.FIELD_TENANT_ID, tenantId)
                .andIn(HmeNcRecordAttr.FIELD_PARENT_RECORD_ID, ncRecordIdList)).build());
        Map<String, List<HmeNcRecordAttr>> recordAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(recordAttrList)) {
            recordAttrMap = recordAttrList.stream().collect(Collectors.groupingBy(HmeNcRecordAttr::getParentRecordId));
        }
        //????????????????????????Id???????????????
        List<MtNcRecord> subNcRecordList = mtNcRecordRepository.selectByCondition(Condition.builder(MtNcRecord.class).andWhere(Sqls.custom()
                .andEqualTo(MtNcRecord.FIELD_TENANT_ID, tenantId)
                .andIn(MtNcRecord.FIELD_PARENT_NC_RECORD_ID, ncRecordIdList)).build());
        Map<String, List<MtNcRecord>> subNcRecordMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(subNcRecordList)) {
            subNcRecordMap = subNcRecordList.stream().collect(Collectors.groupingBy(MtNcRecord::getParentNcRecordId));
        }

        List<HmeNcRecordAttr> updateNcRecordAttrList = new ArrayList<>();
        List<HmeNcRecordAttr> insertNcRecordAttrList = new ArrayList<>();
        List<MtNcRecordVO3> mtNcRecordVO3List = new ArrayList<>();
        // ????????????????????????????????????
        int diffNum = mtNcRecordList.size() - recordAttrList.size();
        List<String> nrAttrIdList = new ArrayList<>();
        List<String> nrAttrCidList = new ArrayList<>();
        if (diffNum != 0) {
            nrAttrIdList = customSequence.getNextKeys("hme_nc_record_attr_s", diffNum);
            nrAttrCidList = customSequence.getNextKeys("hme_nc_record_attr_cid_s", diffNum);
        }
        Integer attrIndex = 0;
        for (MtNcRecord nr : mtNcRecordList) {
            List<HmeNcRecordAttr> hmeNcRecordAttrList = recordAttrMap.get(nr.getNcRecordId());
            if (CollectionUtils.isNotEmpty(hmeNcRecordAttrList)) {
                //????????? ?????????
                HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrList.get(0);
                hmeNcRecordAttr.setProcessMethod(dto.getProcessMethod());
                if ("4".equals(dto.getProcessMethod())) {
                    hmeNcRecordAttr.setAttribute1(dto.getTransitionMaterialId());
                }
                if (StringUtils.isNotBlank(dto.getDispositionFunctionId())) {
                    hmeNcRecordAttr.setAttribute4(dto.getDispositionFunctionId());
                }
                // ????????????????????? ???Attribute6???Y
                if (YES.equals(dto.getReworkRecordFlag())) {
                    hmeNcRecordAttr.setAttribute6(YES);
                }
                updateNcRecordAttrList.add(hmeNcRecordAttr);
            } else {
                //???????????????
                HmeNcRecordAttr hmeNcRecordAttr = new HmeNcRecordAttr();
                hmeNcRecordAttr.setTenantId(tenantId);
                hmeNcRecordAttr.setParentRecordId(nr.getNcRecordId());
                hmeNcRecordAttr.setProcessMethod(dto.getProcessMethod());
                if ("4".equals(dto.getProcessMethod())) {
                    hmeNcRecordAttr.setAttribute1(dto.getTransitionMaterialId());
                }
                if (StringUtils.isNotBlank(dto.getDispositionFunctionId())) {
                    hmeNcRecordAttr.setAttribute4(dto.getDispositionFunctionId());
                }
                // ????????????????????? ???Attribute6???Y
                if (YES.equals(dto.getReworkRecordFlag())) {
                    hmeNcRecordAttr.setAttribute6(YES);
                }
                hmeNcRecordAttr.setAttrId(nrAttrIdList.get(attrIndex));
                hmeNcRecordAttr.setCid(Long.valueOf(nrAttrCidList.get(attrIndex++)));
                hmeNcRecordAttr.setObjectVersionNumber(1L);
                hmeNcRecordAttr.setCreatedBy(userId);
                hmeNcRecordAttr.setCreationDate(new Date());
                hmeNcRecordAttr.setLastUpdatedBy(userId);
                hmeNcRecordAttr.setLastUpdateDate(new Date());
                insertNcRecordAttrList.add(hmeNcRecordAttr);
            }

            //??????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
            MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
            mtNcRecordVO2.setNcRecordId(nr.getNcRecordId());
            mtNcRecordVO2.setEventRequestId(eventRequestId);
            mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);

            //??????????????????????????????????????????????????????
            MtNcRecordVO3 mtNcRecordVO3 = new MtNcRecordVO3();
            mtNcRecordVO3.setEoId(nr.getEoId());
            mtNcRecordVO3.setParentNcRecordId(nr.getNcRecordId());
            mtNcRecordVO3.setUserId(userId);
            mtNcRecordVO3.setNcIncidentId(nr.getNcIncidentId());
            if (StringUtils.isNotEmpty(dto.getComment())) {
                mtNcRecordVO3.setComments(dto.getComment());
            }
            mtNcRecordVO3.setComponentMaterialId(nr.getComponentMaterialId());
            mtNcRecordVO3.setMaterialLotId(nr.getMaterialLotId());
            mtNcRecordVO3.setEoStepActualId(nr.getEoStepActualId());
            mtNcRecordVO3.setRootCauseOperationId(nr.getRootCauseOperationId());
            mtNcRecordVO3.setRootCauseWorkcellId(nr.getRootCauseWorkcellId());
            mtNcRecordVO3.setEventId(eventId);
            // ?????????????????????
            List<MtNcRecord> subNcRecords = subNcRecordMap.get(nr.getNcRecordId());
            for (String ncCodeId : dto.getNcCodeIdList()) {
                List<String> ncCodeIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(subNcRecords)) {
                    ncCodeIdList = subNcRecords.stream().map(MtNcRecord::getNcCodeId).collect(Collectors.toList());
                }
                if (!ncCodeIdList.contains(ncCodeId)) {
                    mtNcRecordVO3.setNcCodeId(ncCodeId);
                    mtNcRecordVO3List.add(mtNcRecordVO3);
                }
            }
        }
        // ???????????????????????????
        if (CollectionUtils.isNotEmpty(updateNcRecordAttrList)) {
            hmeNcRecordAttrMapper.myBatchUpdate(tenantId, userId, updateNcRecordAttrList);
        }
        // ???????????????????????????
        if (CollectionUtils.isNotEmpty(insertNcRecordAttrList)) {
            hmeNcRecordAttrRepository.batchInsertSelective(insertNcRecordAttrList);
        }
        //???????????????????????????????????????
        for (MtNcRecord subNcRecord : subNcRecordList) {
            if (dto.getNcCodeIdList().contains(subNcRecord.getNcCodeId())) {
                //??????????????????????????????????????????????????????????????????????????????
                if (StringUtils.isNotEmpty(dto.getComment())) {
                    MtNcRecordVO7 mtNcRecordVO7 = new MtNcRecordVO7();
                    mtNcRecordVO7.setNcRecordId(subNcRecord.getNcRecordId());
                    mtNcRecordVO7.setComments(dto.getComment());
                    mtNcRecordVO7.setEventId(eventId);
                    mtNcRecordRepository.ncRecordAndHisUpdate(tenantId, mtNcRecordVO7);
                }
                //??????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                mtNcRecordVO2.setNcRecordId(subNcRecord.getNcRecordId());
                mtNcRecordVO2.setEventRequestId(eventRequestId);
                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
            } else {
                //?????????????????????????????????????????????????????????????????????????????????
                MtNcRecordVO2 mtNcRecordVO21 = new MtNcRecordVO2();
                mtNcRecordVO21.setNcRecordId(subNcRecord.getNcRecordId());
                mtNcRecordVO21.setParentEventId(eventId);
                mtNcRecordVO21.setEventRequestId(eventRequestId);
                mtNcRecordRepository.ncRecordCancel(tenantId, mtNcRecordVO21);
            }
        }
        if (CollectionUtils.isNotEmpty(mtNcRecordVO3List)) {
            for (MtNcRecordVO3 mtNcRecordVO3 : mtNcRecordVO3List) {
                //??????????????????????????????????????????????????????????????????
                MtNcRecordVO8 mtNcRecordVO8 = mtNcRecordRepository.ncRecordAndHisCreate(tenantId, mtNcRecordVO3);
                //??????API{ncRecordAndIncidentClose}?????????????????????????????????????????????
                MtNcRecordVO2 ncRecordVO2 = new MtNcRecordVO2();
                ncRecordVO2.setEventRequestId(eventRequestId);
                ncRecordVO2.setNcRecordId(mtNcRecordVO8.getNcRecordId());
                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, ncRecordVO2);
            }
        }
    }

    private String handleInputBarcode(Long tenantId, MtMaterialLot inputMaterialLot, MtMaterialLot sourceMaterialLot, Double ncQty, String barcode, String eventId, String flag) {
        //??????API{materialLotUpdate}????????????  ????????????????????? ??????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        if (StringUtils.isBlank(barcode)) {
            // ?????????????????????
            copyPropertiesIgnoreNullAndTableFields(sourceMaterialLot, mtMaterialLotVO2);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setMaterialLotId(null);
            mtMaterialLotVO2.setMaterialLotCode(null);
            mtMaterialLotVO2.setEnableFlag(YES);
            mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
            mtMaterialLotVO2.setPrimaryUomQty(ncQty);
            mtMaterialLotVO2.setCurrentContainerId(null);
            mtMaterialLotVO2.setTopContainerId(null);
            mtMaterialLotVO2.setCreateReason("SPLIT");
        } else {
            if (inputMaterialLot == null) {
                copyPropertiesIgnoreNullAndTableFields(sourceMaterialLot, mtMaterialLotVO2);
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(null);
                mtMaterialLotVO2.setMaterialLotCode(barcode);
                mtMaterialLotVO2.setEnableFlag(YES);
                mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO2.setPrimaryUomQty(ncQty);
                mtMaterialLotVO2.setCurrentContainerId(null);
                mtMaterialLotVO2.setTopContainerId(null);
                mtMaterialLotVO2.setCreateReason("SPLIT");
            } else if (NO.equals(inputMaterialLot.getEnableFlag())) {
                //???????????? ????????????0
                inputMaterialLot.setPrimaryUomQty(0D);
                mtMaterialLotMapper.updateByPrimaryKeySelective(inputMaterialLot);
                //????????????
                this.materialLotSiteInVerify(tenantId, inputMaterialLot, sourceMaterialLot, flag);
                copyPropertiesIgnoreNullAndTableFields(sourceMaterialLot, mtMaterialLotVO2);
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(inputMaterialLot.getMaterialLotId());
                mtMaterialLotVO2.setMaterialLotCode(inputMaterialLot.getMaterialLotCode());
                mtMaterialLotVO2.setSiteId(inputMaterialLot.getSiteId());
                mtMaterialLotVO2.setEnableFlag(YES);
                mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO2.setPrimaryUomQty(ncQty);
                mtMaterialLotVO2.setCurrentContainerId(null);
                mtMaterialLotVO2.setTopContainerId(null);
            } else if (YES.equals(inputMaterialLot.getEnableFlag())) {
                //??????????????????
                this.materialLotSiteInVerify(tenantId, inputMaterialLot, sourceMaterialLot, flag);
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(inputMaterialLot.getMaterialLotId());
                mtMaterialLotVO2.setMaterialLotCode(inputMaterialLot.getMaterialLotCode());
                mtMaterialLotVO2.setSiteId(inputMaterialLot.getSiteId());
                mtMaterialLotVO2.setEnableFlag(YES);
                mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO2.setMaterialId(inputMaterialLot.getMaterialId());
                mtMaterialLotVO2.setPrimaryUomId(sourceMaterialLot.getPrimaryUomId());
                mtMaterialLotVO2.setTrxPrimaryUomQty(ncQty);
                mtMaterialLotVO2.setLocatorId(inputMaterialLot.getLocatorId());
                mtMaterialLotVO2.setSupplierId(sourceMaterialLot.getSupplierId());
                mtMaterialLotVO2.setEoId(sourceMaterialLot.getEoId());
                mtMaterialLotVO2.setCurrentContainerId(sourceMaterialLot.getCurrentContainerId());
                mtMaterialLotVO2.setTopContainerId(sourceMaterialLot.getTopContainerId());
            }
        }
        MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
        return mtMaterialLotVO13.getMaterialLotId();
    }

    private List<MtCommonExtendVO6> batchMaterialLotAttrUpdate(Long tenantId, String materialLotCode, String materialLotId, MtMaterialLot inputMaterialLot, MtMaterialLot sourceMaterialLot, List<MtExtendAttrVO1> sourceExtendAttrList, String eventId, String flag) {
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        //??????????????????
        MtCommonExtendVO6 commonExtendVO6 = new MtCommonExtendVO6();
        commonExtendVO6.setKeyId(materialLotId);
        List<MtCommonExtendVO5> attrs = new ArrayList<>();
        List<MtExtendAttrVO1> extendAttrVO1List = sourceExtendAttrList.stream().filter(extendAttr -> StringUtils.equals(extendAttr.getKeyId(), sourceMaterialLot.getMaterialLotId())).collect(Collectors.toList());
        if (StringUtils.isBlank(materialLotCode) || inputMaterialLot == null) {
            if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    if (StringUtils.equals("STATUS", mtExtendAttrVO1.getAttrName()) || StringUtils.equals("NC_SUPPLIER_REPLACEMENT", mtExtendAttrVO1.getAttrName()) || StringUtils.equals("ORIGINAL_ID", mtExtendAttrVO1.getAttrName())) {
                        continue;
                    }
                    MtCommonExtendVO5 commonExtendVO5 = new MtCommonExtendVO5();
                    commonExtendVO5.setAttrName(mtExtendAttrVO1.getAttrName());
                    commonExtendVO5.setAttrValue(mtExtendAttrVO1.getAttrValue());
                    attrs.add(commonExtendVO5);
                }
            }

            MtCommonExtendVO5 attr1 = new MtCommonExtendVO5();
            attr1.setAttrName("STATUS");
            attr1.setAttrValue(HmeConstants.StatusCode.NEW);
            if (StringUtils.equals(flag, HmeConstants.ConstantValue.TK)) {
                attr1.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
            }
            attrs.add(attr1);

            //?????? ??????????????????
            if (StringUtils.equals(flag, "TK")) {
                MtCommonExtendVO5 attr2 = new MtCommonExtendVO5();
                attr2.setAttrName("NC_SUPPLIER_REPLACEMENT");
                attr2.setAttrValue(YES);
                attrs.add(attr2);
            }
            MtCommonExtendVO5 attr5 = new MtCommonExtendVO5();
            attr5.setAttrName("ORIGINAL_ID");
            attr5.setAttrValue(sourceMaterialLot.getMaterialLotId());
            attrs.add(attr5);
        } else if (NO.equals(inputMaterialLot.getEnableFlag())) {
            if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                    if (StringUtils.equals("STATUS", mtExtendAttrVO1.getAttrName()) || StringUtils.equals("NC_SUPPLIER_REPLACEMENT", mtExtendAttrVO1.getAttrName()) || StringUtils.equals("ORIGINAL_ID", mtExtendAttrVO1.getAttrName())) {
                        continue;
                    }
                    MtCommonExtendVO5 commonExtendVO5 = new MtCommonExtendVO5();
                    commonExtendVO5.setAttrName(mtExtendAttrVO1.getAttrName());
                    commonExtendVO5.setAttrValue(mtExtendAttrVO1.getAttrValue());
                    attrs.add(commonExtendVO5);
                }
            }
            if (StringUtils.equals(flag, HmeConstants.ConstantValue.TK)) {
                MtCommonExtendVO5 attr2 = new MtCommonExtendVO5();
                attr2.setAttrName("NC_SUPPLIER_REPLACEMENT");
                attr2.setAttrValue(YES);
                attrs.add(attr2);
            }

            MtCommonExtendVO5 attr5 = new MtCommonExtendVO5();
            attr5.setAttrName("ORIGINAL_ID");
            attr5.setAttrValue(sourceMaterialLot.getMaterialLotId());
            attrs.add(attr5);
        } else if (YES.equals(inputMaterialLot.getEnableFlag())) {
            MtCommonExtendVO5 attr6 = new MtCommonExtendVO5();
            attr6.setAttrName("ORIGINAL_ID");
            attr6.setAttrValue(sourceMaterialLot.getMaterialLotId());
            attrs.add(attr6);
        }
        //??????????????????
        if (CollectionUtils.isNotEmpty(attrs)) {
            commonExtendVO6.setAttrs(attrs);
        }
        attrPropertyList.add(commonExtendVO6);
        return attrPropertyList;
    }

    void batchUpdateNoSiteOutData(Long tenantId, List<String> materialLotIdList, Long userId) {
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.selectByCondition(Condition.builder(HmeEoJobSn.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                    .andIn(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLotIdList)
                    .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());
            if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                List<String> jobIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                hmeEoJobSnMapper.batchOutSite(tenantId, userId, jobIdList);
            }
        }
    }

    void updateNoSiteOutData(Long tenantId, String materialLotId, Long userId) {
        List<String> noSiteOutJobId = hmeNcDisposePlatformMapper.getNoSiteOutJobId(tenantId, materialLotId);
        if (CollectionUtils.isNotEmpty(noSiteOutJobId)) {
            for (String jobId : noSiteOutJobId) {
                HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(jobId);
                hmeEoJobSn.setSiteOutDate(new Date());
                hmeEoJobSn.setSiteOutBy(userId);
                hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);
            }
        }

    }

    /**
     * ??????Eo????????????????????????????????????
     *
     * @param tenantId
     * @param mtNcRecord
     * @param routeId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/6 11:38
     */
    private void bindEoRouterBomRel(Long tenantId, MtNcRecord mtNcRecord, String routeId) {
        HmeEoRouterBomRel hmeEoRouterBomRel = new HmeEoRouterBomRel();
        hmeEoRouterBomRel.setTenantId(tenantId);
        hmeEoRouterBomRel.setEoId(mtNcRecord.getEoId());
        hmeEoRouterBomRel.setRouterId(routeId);
        MtRouter mtRouter = mtRouterRepository.selectByPrimaryKey(routeId);
        hmeEoRouterBomRel.setBomId(mtRouter.getBomId());
        hmeEoRouterBomRel.setNcRecordId(mtNcRecord.getNcRecordId());
        hmeEoRouterBomRelRepository.insertSelective(hmeEoRouterBomRel);
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
}