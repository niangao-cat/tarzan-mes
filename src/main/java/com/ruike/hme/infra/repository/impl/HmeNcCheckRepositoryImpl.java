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
        // 只选择事业部 则查询该事业部下所有工位显示
        if (StringUtils.isNotBlank(dto.getAreaId()) && StringUtils.isBlank(dto.getProdLineId()) && StringUtils.isBlank(dto.getProcessId()) && StringUtils.isBlank(dto.getWorkcellId())) {
            flag = true;
            //调用API {subOrganizationRelQuery} 查询该产线下所有工位
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setParentOrganizationId(dto.getAreaId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //查询事业部下所有的工位
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //只选择产线，则查询该产线下所有工位显示
        if (StringUtils.isNotBlank(dto.getProdLineId())
                && StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            flag = true;
            //调用API {subOrganizationRelQuery} 查询该产线下所有工位
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationVO2.setParentOrganizationId(dto.getProdLineId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //查询工序下所有的工位
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //选择了工序，但没有选择工位，则查询该工序下所有工位展示
        if (StringUtils.isNotBlank(dto.getProcessId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            flag = true;
            //调用API {subOrganizationRelQuery} 查询该工序下所有工位
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
        //序列号模糊查询
        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            flag1 = true;
            //根据序列号模糊查询表mt_material_lot的material_lot_id
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(
                    Sqls.custom().andLikeRight(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getMaterialLotCode())
            ).build());
            dto.setMaterialLotIdList(mtMaterialLots.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList()));
        }
        //查询数据
        //当选择了事业部、产线、工序等条件，但其对应的工位、物料批Id为空时，直接返回空
        if ((flag && CollectionUtils.isEmpty(dto.getWorkcellIdList())) || (flag1 && CollectionUtils.isEmpty(dto.getMaterialLotIdList()))) {
            return new Page<HmeNcDisposePlatformDTO2>(Collections.EMPTY_LIST, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), 0);
        }
        Page<HmeNcDisposePlatformDTO2> ncRecordList = PageHelper.doPage(pageRequest, () -> hmeNcDisposePlatformMapper.ncRecordQuery(tenantId, dto));
        for (HmeNcDisposePlatformDTO2 hmeNcDisposePlatformDTO2 : ncRecordList) {
            //车间 根据ROOT_CAUSE_WORKCELL_ID调用API{parentOrganizationRelQuery}
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
            //生产线
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
                hmeNcDisposePlatformDTO2.setProdLineName(mtModProductionLine.getDescription());
            }
            //工段和工序
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
            //工位 根据ROOT_CAUSE_WORKCELL_ID、WORKCELL_TYPE = STATION关联 mt_mod_workcell获取WORKCELL_NAME
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getWorkcellId());
            hmeNcDisposePlatformDTO2.setWorkcellName(mtModWorkcell.getWorkcellName());
            //操作者
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getCreatedBy());
            hmeNcDisposePlatformDTO2.setUserName(mtUserInfo.getRealName());
            //工号
            hmeNcDisposePlatformDTO2.setUserCode(mtUserInfo.getLoginName());
            //序列号 MATERIAL_LOT_ID关联mt_material_lot显示material_lot_code，没关联到显示material_Lot_id
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getMaterialLotId());
            if (mtMaterialLot != null && StringUtils.isNotBlank(mtMaterialLot.getMaterialLotCode())) {
                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            } else {
                hmeNcDisposePlatformDTO2.setMaterialLotCode(hmeNcDisposePlatformDTO2.getMaterialLotId());
            }
            //工单号 根据eo_id关联表mt_eo，找到work_order_id，根据work_order_id在表mt_work_order找到WORK_ORDER_NUM
//            MtEo mtEo = mtEoRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getEoId());
//            if (mtEo != null && StringUtils.isNotBlank(mtEo.getWorkOrderId())) {
//                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
//                if (mtWorkOrder != null) {
//                    hmeNcDisposePlatformDTO2.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
//                }
//            }
            //不良代码 不良原因
            //在表mt_nc_record，根据PARENT_NC_RECORD_ID = NC_RECORD_ID查询数据
            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, hmeNcDisposePlatformDTO2.getNcRecordId());
            List<String> ncCodeIdList = new ArrayList<>();
            List<String> ncCodeList = new ArrayList<>();
            List<String> ncReasonList = new ArrayList<>();
            //处理人备注
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
                    //根据NC_CODE_ID关联表mt_nc_code查询nc_code
                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
                    ncCodeIdList.add(mtNcCode.getNcCodeId());
                    ncCodeList.add(mtNcCode.getNcCode());
                    ncReasonList.add(mtNcCode.getDescription());
                }
            }
            hmeNcDisposePlatformDTO2.setNcCodeIdList(ncCodeIdList);
            hmeNcDisposePlatformDTO2.setNcCodeList(ncCodeList);
            hmeNcDisposePlatformDTO2.setNcReasonList(ncReasonList);
            //不良组描述  不良类型
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getNcCodeId());
//                hmeNcDisposePlatformDTO2.setNcGroupDesc(mtNcGroup.getDescription());
            hmeNcDisposePlatformDTO2.setNcType(mtNcGroup.getComponentRequired());
            String meaning = lovAdapter.queryLovMeaning("HME.NC_TYPE", tenantId, mtNcGroup.getComponentRequired());
            hmeNcDisposePlatformDTO2.setNcTypeMeaning(meaning);
            //产品料号 MATERIAL_ID关联mt_material获取material_code
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getMaterialId());
            hmeNcDisposePlatformDTO2.setMaterialCode(mtMaterial.getMaterialCode());
            //产品描述
            hmeNcDisposePlatformDTO2.setMaterialName(mtMaterial.getMaterialName());
            //不良单号
//            MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getNcIncidentId());
//            hmeNcDisposePlatformDTO2.setNcNumber(mtNcIncident.getIncidentNumber());
            //发生时间
            hmeNcDisposePlatformDTO2.setDateTime(hmeNcDisposePlatformDTO2.getDateTime());
            //责任工位
            if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO2.getRootCauseWorkcellId())) {
                MtModWorkcell mtModWorkcell2 = mtModWorkcellRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getRootCauseWorkcellId());
                hmeNcDisposePlatformDTO2.setResponseWorkcellName(mtModWorkcell2.getWorkcellName());
            }
            //责任人
            MtUserInfo mtUserInfo2 = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getUserId());
            hmeNcDisposePlatformDTO2.setResponseUser(mtUserInfo2.getRealName());
            //状态
            hmeNcDisposePlatformDTO2.setStatus(hmeNcDisposePlatformDTO2.getNcStatus());
            MtGenStatus statusMeaning = mtGenStatusRepository.getGenStatus(tenantId, "NC_RECORD", "NC_RECORD_STATUS", hmeNcDisposePlatformDTO2.getNcStatus());
            hmeNcDisposePlatformDTO2.setStatusMeaning(statusMeaning.getDescription());
            //2020-08-28 add by chaonan.hu for zhenyun.ban 增加处理人、处理时间
            if (hmeNcDisposePlatformDTO2.getClosedUserId() != null) {
                MtUserInfo mtUserInfo3 = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getClosedUserId());
                hmeNcDisposePlatformDTO2.setDisposeUserId(hmeNcDisposePlatformDTO2.getClosedUserId().toString());
                hmeNcDisposePlatformDTO2.setDisposeUserName(mtUserInfo3.getRealName());
                hmeNcDisposePlatformDTO2.setDisposeDateTime(hmeNcDisposePlatformDTO2.getClosedDateTime());
            }
            //处理办法
            HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrRepository.selectOne(new HmeNcRecordAttr() {{
                setTenantId(tenantId);
                setParentRecordId(hmeNcDisposePlatformDTO2.getNcRecordId());
            }});
            if (hmeNcRecordAttr != null) {
                hmeNcDisposePlatformDTO2.setDisposeMethod(hmeNcRecordAttr.getProcessMethod());
                String disposeMethodMeaning = lovAdapter.queryLovMeaning("HME.NC_PROCESS_METHOD", tenantId, hmeNcRecordAttr.getProcessMethod());
                hmeNcDisposePlatformDTO2.setDisposeMethodMeaning(disposeMethodMeaning);
                //转型物料
                //2020-08-25 15:06 add by chaonan.hu for lu.bai 增加转型物料查询逻辑
                if (StringUtils.isNotEmpty(hmeNcRecordAttr.getAttribute1())) {
                    MtMaterial mtMaterialDb = mtMaterialRepository.selectByPrimaryKey(hmeNcRecordAttr.getAttribute1());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialId(hmeNcRecordAttr.getAttribute1());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialCode(mtMaterialDb.getMaterialCode());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialName(mtMaterialDb.getMaterialName());
                }
            }
            //2020-09-28 add by chaonan.hu for lu.bai 增加不良物料、申请数量显示
            if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO2.getComponentMaterialId())) {
                MtMaterial mtMaterial2 = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getComponentMaterialId());
                hmeNcDisposePlatformDTO2.setScrapMaterialId(mtMaterial2.getMaterialId());
                hmeNcDisposePlatformDTO2.setScrapMaterialCode(mtMaterial2.getMaterialCode());
                hmeNcDisposePlatformDTO2.setScrapMaterialName(mtMaterial2.getMaterialName());
                hmeNcDisposePlatformDTO2.setScrapQty(hmeNcDisposePlatformDTO2.getQty() == null ? BigDecimal.ZERO : new BigDecimal(hmeNcDisposePlatformDTO2.getQty()));
            }
            // 芯片位置
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
        //状态校验
        if (!HmeConstants.NcStatus.OPEN.equals(mtNcRecord.getNcStatus())) {
            throw new MtException("HME_NC_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0007", "HME"));
        }
        // 创建事务请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CLOSE");

        //事件创建
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("NC_RECORD_CLOSE");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //不良记录更新
        if (!dto.getNcGroupId().equals(mtNcRecord.getNcCodeId())) {
            //如果不良类型组发生改变，则更新父不良记录并记录历史。注意：对应的确实是ncCodeId
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
        //将处理方法记录到不良记录扩展属性表中
        List<HmeNcRecordAttr> recordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
            setTenantId(tenantId);
            setParentRecordId(mtNcRecord.getNcRecordId());
        }});
        if(CollectionUtils.isNotEmpty(recordAttrList)){
            //不为空 则更新
            HmeNcRecordAttr hmeNcRecordAttr = recordAttrList.get(0);
            hmeNcRecordAttr.setProcessMethod(dto.getProcessMethod());
            if ("4".equals(dto.getProcessMethod())) {
                hmeNcRecordAttr.setAttribute1(dto.getTransitionMaterialId());
            }
            if (StringUtils.isNotBlank(dto.getDispositionFunctionId())) {
                hmeNcRecordAttr.setAttribute4(dto.getDispositionFunctionId());
            }
            // 勾选了返修记录 则Attribute6记Y
            if (YES.equals(dto.getReworkRecordFlag())) {
                hmeNcRecordAttr.setAttribute6(YES);
            }
            hmeNcRecordAttrMapper.updateByPrimaryKeySelective(hmeNcRecordAttr);
        }else {
            //没有则新增
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
            // 勾选了返修记录 则Attribute6记Y
            if (YES.equals(dto.getReworkRecordFlag())) {
                hmeNcRecordAttr.setAttribute6(YES);
            }
            hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);
        }
        //调用API{ncRecordAndIncidentClose}进行父不良记录关闭、事故关闭。
        MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
        mtNcRecordVO2.setNcRecordId(dto.getNcRecordId());
        mtNcRecordVO2.setEventRequestId(eventRequestId);
        mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
        //子不良记录取消或生成或更新
        //查询原子不良记录Id和不良代码
        List<HmeNcCheckVO2> childNcRecordIdList = hmeNcCheckMapper.childNcRecordIdQuery(tenantId, dto.getNcRecordId());
        for (HmeNcCheckVO2 childNcRecord : childNcRecordIdList) {
            if (dto.getNcCodeIdList().contains(childNcRecord.getNcCodeId())) {
                //如果原子不良记录的不良代码存在新子不良代码中，则更新
                if (StringUtils.isNotEmpty(dto.getComment())) {
                    MtNcRecordVO7 mtNcRecordVO7 = new MtNcRecordVO7();
                    mtNcRecordVO7.setNcRecordId(childNcRecord.getNcRecordId());
                    mtNcRecordVO7.setComments(dto.getComment());
                    mtNcRecordVO7.setEventId(eventId);
                    mtNcRecordRepository.ncRecordAndHisUpdate(tenantId, mtNcRecordVO7);
                }
                //调用API{ncRecordAndIncidentClose}进行子不良记录关闭、事故关闭。
                mtNcRecordVO2.setNcRecordId(childNcRecord.getNcRecordId());
                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
            } else {
                //如果原子不良记录的不良代码不存在新子不良代码中，则取消
                MtNcRecordVO2 mtNcRecordVO21 = new MtNcRecordVO2();
                mtNcRecordVO21.setNcRecordId(childNcRecord.getNcRecordId());
                mtNcRecordVO21.setParentEventId(eventId);
                mtNcRecordVO21.setEventRequestId(eventRequestId);
                mtNcRecordRepository.ncRecordCancel(tenantId, mtNcRecordVO21);
            }
        }
        //为了万一存在新建的情况，提前设置属性
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
                //如果新子不良代码不存在原子不良记录中，则新建
                mtNcRecordVO3.setNcCodeId(ncCodeId);
                MtNcRecordVO8 mtNcRecordVO8 = mtNcRecordRepository.ncRecordAndHisCreate(tenantId, mtNcRecordVO3);
                //调用API{ncRecordAndIncidentClose}进行子不良记录关闭、事故关闭。
                mtNcRecordVO2.setNcRecordId(mtNcRecordVO8.getNcRecordId());
                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
            }
        }
        //条码处理
        if (HmeConstants.ProcessStatus.ONE.equals(dto.getProcessMethod())) {
            // 2021-01-07 add by sanfeng.zhang for wang.can 校验当前EO是否返修中
            String reworkFlag = hmeNcCheckMapper.queryReworkFlagByEoId(tenantId, mtNcRecord.getEoId());
            if (YES.equals(reworkFlag)) {
                throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0049", "HME"));
            }
            //2020-08-25 add by chaonan.hu for lu.bai 如果勾选直接返修，则将找到的未出站数据出站
            updateNoSiteOutData(tenantId, mtNcRecord.getMaterialLotId(), userId);

            //如果勾选了返修，则调用API{materialLotAttrPropertyUpdate}更新条码的返修标识
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(mtNcRecord.getMaterialLotId());
            mtExtendVO10.setEventId(eventId);
            List<MtExtendVO5> attrList = new ArrayList<>();
            // 2021/02/07 add by sanfeng.zhang wang.can 勾选返修记录 则不更新返修标识
            if (!YES.equals(dto.getReworkRecordFlag())) {
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("REWORK_FLAG");
                mtExtendVO5.setAttrValue(HmeConstants.ConstantValue.YES);
                attrList.add(mtExtendVO5);
            }
            //2020/12/03 add by sanfeng.zhang for malifeng 增加处置组
            if (StringUtils.isNotBlank(dto.getRouterId())) {
                MtExtendVO5 reworkAttr = new MtExtendVO5();
                reworkAttr.setAttrName("REWORK_ROUTER");
                reworkAttr.setAttrValue(dto.getRouterId());
                attrList.add(reworkAttr);

                // 2021/1/6 add by sanfeng.zahng for xietianyang 增加指定返修标识
                MtExtendVO5 flagAttr = new MtExtendVO5();
                flagAttr.setAttrName("DESIGNED_REWORK_FLAG");
                flagAttr.setAttrValue(YES);
                attrList.add(flagAttr);
            }
            mtExtendVO10.setAttrs(attrList);
            if (CollectionUtils.isNotEmpty(attrList)) {
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
            }
            // 2021/1/6 add by sanfeng.zhang for xietianyang 增加关联EO、工艺路线及装配清单关系hme_eo_router_bom_rel
            if (StringUtils.isNotBlank(dto.getRouterId())) {
                this.bindEoRouterBomRel(tenantId, mtNcRecord, dto.getRouterId());
            }
            //2020-09-17 20:49 add by chgaonan.hu for zhenyong.ban 增加将条码与容器解绑的逻辑
            hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getProcessMethod())) {
            //如果勾选了放行
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
            if (NO.equals(mtNcGroup.getComponentRequired())) {
                // 2021-01-07 add by sanfeng.zhang for wang.can 校验当前EO是否返修中
                String reworkFlag = hmeNcCheckMapper.queryReworkFlagByEoId(tenantId, mtNcRecord.getEoId());
                if (YES.equals(reworkFlag)) {
                    throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0049", "HME"));
                }
                //如果是工序不良放行，则调用API{materialLotUpdate}更新条码的质量状态
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(mtNcRecord.getMaterialLotId());
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setQualityStatus(OK);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            } else if (YES.equals(mtNcGroup.getComponentRequired())) {
                //先判断是否投料
                Boolean feedFlag = judgeFeedByMaterialLot(tenantId, mtNcRecord.getEoId(), mtNcRecord.getMaterialLotId());
                //是否处理条码
                Boolean handleFlag;
                //获取不退料标识
                List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
                    setTenantId(tenantId);
                    setParentRecordId(mtNcRecord.getNcRecordId());
                }});
                String returnMaterialFlag = CollectionUtils.isNotEmpty(ncRecordAttrList) ? ncRecordAttrList.get(0).getAttribute2() : "";
                if (feedFlag) {
                    if (StringUtils.equals(returnMaterialFlag, YES)) {
                        //有不退料标识 不处理
                        handleFlag = false;
                    } else {
                        //无不退料标识 更新条码 库存 及增加退库标识
                        handleFlag = true;
                    }

                } else {
                    if (StringUtils.equals(returnMaterialFlag, YES)) {
                        //有不退料标识 不处理
                        handleFlag = false;
                    } else {
                        //未投料 更新条码 库存 及增加退库标识
                        handleFlag = true;
                    }
                }
                if (handleFlag) {
                    //输入条码
                    MtMaterialLot mtMaterialLot1 = null;
                    if(StringUtils.isNotBlank(dto.getBarcode())){
                        mtMaterialLot1 = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getBarcode());
                    }
                    //创建条码及扩展字段
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

                    //调用API{onhandQtyUpdateProcess}进行库存恢复
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
                    // 取产线的不良虚拟库位 多个或找不到则报错
                    String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                    // 调用API{onhandQtyUpdateProcess}扣减虚拟仓库存
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
            //如果勾选了报废
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
            if (NO.equals(mtNcGroup.getComponentRequired())) {
                //如果是工序不良报废，则调用API{eoScrap}报废，并调用API{materialLotUpdate}条码更新
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
                //2020-09-08 21:00 add by chaonan.hu for lu.bai 将找到的未出站数据出站
                updateNoSiteOutData(tenantId, mtNcRecord.getMaterialLotId(), userId);
                //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban 增加将条码与容器解绑的逻辑
                hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
            } else if (YES.equals(mtNcGroup.getComponentRequired())) {
                //如果是材料报废
                //2020-09-09 11:10 add by chaonan.hu for lu.bai 增加原材料报废的逻辑
                materialScrap(tenantId, mtMaterialLot, mtNcRecord, eventId, eventRequestId);
            }
        } else if ("4".equals(dto.getProcessMethod())) {
            //2020-08-25 add by chaonan.hu for lu.bai 增加降级转型的逻辑
            if (StringUtils.isEmpty(dto.getTransitionMaterialId())) {
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "转型物料"));
            }
            //条码处理
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(mtNcRecord.getMaterialLotId());
            mtExtendVO10.setEventId(eventId);
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("PERFORMANCE_LEVEL");
            //条码上的原物料ID
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            mtExtendVO5.setAttrValue(mtMaterialLot.getMaterialId());
            mtExtendVO10.setAttrs(Collections.singletonList(mtExtendVO5));
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(mtNcRecord.getMaterialLotId());
            //转型物料ID
            mtMaterialLotVO2.setMaterialId(dto.getTransitionMaterialId());
            mtMaterialLotVO2.setQualityStatus(OK);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            //EO处理
            MtEoVO mtEoVO = new MtEoVO();
            mtEoVO.setEventId(eventId);
            mtEoVO.setEoId(mtNcRecord.getEoId());
            mtEoVO.setMaterialId(dto.getTransitionMaterialId());
            mtEoRepository.eoUpdate(tenantId, mtEoVO, NO);
        } else if (HmeConstants.ProcessStatus.FIVE.equals(dto.getProcessMethod())) {
            //退库逻辑 只有材料不良处理
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
            if (YES.equals(mtNcGroup.getComponentRequired())) {
                //先判断是否投料
                Boolean feedFlag = judgeFeedByMaterialLot(tenantId, mtNcRecord.getEoId(), mtNcRecord.getMaterialLotId());
                //是否处理条码
                Boolean handleFlag;
                //获取不退料标识
                List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
                    setTenantId(tenantId);
                    setParentRecordId(mtNcRecord.getNcRecordId());
                }});
                String returnMaterialFlag = CollectionUtils.isNotEmpty(ncRecordAttrList) ? ncRecordAttrList.get(0).getAttribute2() : "";
                if (feedFlag) {
                    if (StringUtils.equals(returnMaterialFlag, YES)) {
                        //有不退料标识 不处理
                        handleFlag = false;
                    } else {
                        //无不退料标识 更新条码 库存 及增加退库标识
                        handleFlag = true;
                    }
                } else {
                    if (StringUtils.equals(returnMaterialFlag, YES)) {
                        //有不退料标识 不处理
                        handleFlag = false;
                    } else {
                        //未投料 更新条码 库存 及增加退库标识
                        handleFlag = true;
                    }
                }
                if (handleFlag) {
                    //来源条码
                    MtMaterialLot materialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
                    //输入条码
                    MtMaterialLot mtMaterialLot = null;
                    if(StringUtils.isNotBlank(dto.getBarcode())){
                        mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getBarcode());
                    }
                    //创建条码及扩展字段
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
                    //创建退库事件
                    MtEventCreateVO mtEventCreateVO2 = new MtEventCreateVO();
                    mtEventCreateVO2.setEventTypeCode("NC_RECORD_MATERIAL_TK");
                    mtEventCreateVO2.setEventRequestId(eventRequestId);
                    String eventId2 = mtEventRepository.eventCreate(tenantId, mtEventCreateVO2);
                    //更新库存量
                    MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                    mtInvOnhandQuantityVO9.setSiteId(materialLot.getSiteId());
                    mtInvOnhandQuantityVO9.setMaterialId(materialLot.getMaterialId());
                    mtInvOnhandQuantityVO9.setLocatorId(materialLot.getLocatorId());
                    mtInvOnhandQuantityVO9.setLotCode(materialLot.getLot());
                    mtInvOnhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                    mtInvOnhandQuantityVO9.setEventId(eventId2);
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                    // 取产线的不良虚拟库位 多个或找不到则报错
                    String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                    // 调用API{onhandQtyUpdateProcess}扣减虚拟仓库存
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
            // 自制件返修 (材料不良才有)
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtNcRecord.getComponentMaterialId());
            // 申请数量不为1 则报错
            if (mtNcRecord.getQty().compareTo(1D) != 0) {
                throw new MtException("HME_NC_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0051", "HME", mtMaterial != null ? mtMaterial.getMaterialName() : ""));
            }
            // 校验当前条码对应工单状态
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            // 条码更新：调用API{materialLotUpdate}
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotVO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            mtMaterialLotVO2.setSiteId(mtMaterialLot.getSiteId());
            mtMaterialLotVO2.setEnableFlag(YES);
            mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
            mtMaterialLotVO2.setTrxPrimaryUomQty(mtNcRecord.getQty());
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            // 条码返修标识置为Y
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
                // 继续判断 对应的工单状态 为NEW/RELEASED/EORELEASED 则报错
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

            // 调用SAP生成返修工单接口，自动生成返修工单
            HmeRepairWorkOrderCreate workOrderCreate = new HmeRepairWorkOrderCreate();
            workOrderCreate.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            workOrderCreate.setQty(mtNcRecord.getQty());
            if (mtMaterial != null) {
                workOrderCreate.setMaterialCode(mtMaterial.getMaterialCode());
                // 物料单位
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                workOrderCreate.setPrimaryUomCode(mtUom != null ? mtUom.getUomCode() : "");
            }
            // 站点
            MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(mtNcRecord.getSiteId());
            workOrderCreate.setSiteCode(mtModSite != null ? mtModSite.getSiteCode() : "");
            // 获取仓库 (先根据工位找到其工段在mt_mod_organization_rel对应的货位)
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
            // 返修工单物料版本从值集获取
            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.WO_PRODUCTION_VERSION", tenantId);
            if (CollectionUtils.isNotEmpty(valueDTOList)) {
                workOrderCreate.setProductionVersion(valueDTOList.get(0).getValue());
            }
            HmeRepairWorkOrderCreate resultData = itfRepairWorkOrderCreateService.hmeRepairWorkOrderCreateService(tenantId, workOrderCreate);
            // 接口返回状态为空, 则为返修工单实时接口未启用,报错提示
            if (StringUtils.isBlank(resultData.getStatus())) {
                throw new MtException("HME_NC_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0053", "HME"));
            }
            // 接口返回状态E, 创建返修工单异常 返回报错信息
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

            // 取产线的不良虚拟库位 多个或找不到则报错
            String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
            // 调用API{onhandQtyUpdateProcess}扣减虚拟仓库存
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

            // 创建材料不良自制件返修事件 增加库存
            String recordEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO(){{
                setEventRequestId(eventRequestId);
                setEventTypeCode("NC_RECORD_SELFMADE_MATERIAL_REPAIR");
            }});
            // 调用更新现有量API：onhandQtyUpdateProcess
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
        // 对象值转换时屏蔽表字段：ID，创建更新人等信息
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(Arrays.asList(WmsCommonUtils.getNullPropertyNames(src)));
        hashSet.addAll(Arrays.asList(IGNORE_TABLE_FIELDS));
        String[] result = new String[hashSet.size()];
        BeanUtils.copyProperties(src, target, hashSet.toArray(result));
    }

    /**
     * 新建或更新条码
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
        //调用API{materialLotUpdate}更新条码  分条码有效无效 不存在则新增
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        if (StringUtils.isBlank(materialLotCode)) {
            // 创建物料批信息
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
                //无效条码 条码数置0
                inputMaterialLot.setPrimaryUomQty(0D);
                mtMaterialLotMapper.updateByPrimaryKeySelective(inputMaterialLot);
                //校验条码
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
                //校验条码进站
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
     * 更新扩展字段
     *
     * @param materialLotCode
     * @param inputMaterialLot    输入
     * @param sourceMaterialLot  来源
     * @author sanfeng.zhang@hand-china.com 2020/11/25 18:15
     * @return void
     */
    private void materialLotAttrUpdate(Long tenantId, String materialLotCode, String materialLotId, MtMaterialLot inputMaterialLot, MtMaterialLot sourceMaterialLot,String eventId, String flag){
        //保存扩展字段
        //来源条码扩展字段
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

            //退库 保存料废标识
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
        //更新扩展字段
        if(CollectionUtils.isNotEmpty(attrs)){
            extendVO10.setAttrs(attrs);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, extendVO10);
        }
    }


    /**
     * 判断当前条码是否投料
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

    //验证条码
    private void materialLotSiteInVerify(Long tenantId, MtMaterialLot inputMaterialLot, MtMaterialLot sourcerMaterialLot, String flag) {
        //料号
        if(!StringUtils.equals(inputMaterialLot.getMaterialId(), sourcerMaterialLot.getMaterialId())){
            throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0034", "HME", "料号", "料号"));
        }
        //批次
        if(!StringUtils.equals(inputMaterialLot.getLot(), sourcerMaterialLot.getLot())){
            throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0034", "HME", "批次", "批次"));
        }
        //货位
        if(!StringUtils.equals(inputMaterialLot.getLocatorId(), sourcerMaterialLot.getLocatorId())){
            throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0034", "HME", "货位", "货位"));
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
        //销售订单头
        if(CollectionUtils.isNotEmpty(extendAttrVO1List)){
            Optional<MtExtendAttrVO1> inputSoNumOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), inputMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "SO_NUM")).findFirst();
            String inputSoNum = inputSoNumOpt.isPresent() ? inputSoNumOpt.get().getAttrValue() : "";
            Optional<MtExtendAttrVO1> sourceSoNumOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), sourcerMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "SO_NUM")).findFirst();
            String sourceSoNum = sourceSoNumOpt.isPresent() ? sourceSoNumOpt.get().getAttrValue() : "";
            if(!StringUtils.equals(inputSoNum, sourceSoNum)){
                throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0034", "HME", "销售订单", "销售订单"));
            }
        }
        //销售订单行
        if(CollectionUtils.isNotEmpty(extendAttrVO1List)){
            Optional<MtExtendAttrVO1> inputSoLineNumOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), inputMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "SO_LINE_NUM")).findFirst();
            String inputSoLineNum = inputSoLineNumOpt.isPresent() ? inputSoLineNumOpt.get().getAttrValue() : "";
            Optional<MtExtendAttrVO1> sourceSoLineNumOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), sourcerMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "SO_LINE_NUM")).findFirst();
            String sourceSoLineNum = sourceSoLineNumOpt.isPresent() ? sourceSoLineNumOpt.get().getAttrValue() : "";
            if(!StringUtils.equals(inputSoLineNum, sourceSoLineNum)){
                throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0034", "HME", "销售订单行", "销售订单行"));
            }
        }

        //版本号
        if(CollectionUtils.isNotEmpty(extendAttrVO1List)){
            Optional<MtExtendAttrVO1> inputVersionOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), inputMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "MATERIAL_VERSION")).findFirst();
            String inputVersion = inputVersionOpt.isPresent() ? inputVersionOpt.get().getAttrValue() : "";
            Optional<MtExtendAttrVO1> sourceVersionOpt = extendAttrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), sourcerMaterialLot.getMaterialLotId()) && StringUtils.equals(dto.getAttrName(), "MATERIAL_VERSION")).findFirst();
            String sourceVersion = sourceVersionOpt.isPresent() ? sourceVersionOpt.get().getAttrValue() : "";
            if(!StringUtils.equals(inputVersion, sourceVersion)){
                throw new MtException("HME_NC_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0034", "HME", "物料版本", "物料版本"));
            }
        }

        if (YES.equals(inputMaterialLot.getEnableFlag())) {
            //有效 且退库 校验料废标识
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
            //当工艺Id为空时，查询表mt_nc_group中componentRequired为Y的不良代码组数据
            result = PageHelper.doPageAndSort(pageRequest, () -> hmeNcCheckMapper.ncGroupQuery(tenantId, dto));
        } else {
            //否则，根据工艺Id查询不良代码组数据
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
        //是否处理 当已投料/未投料 不退料标识为Y 即不做处理
        Boolean handleFlag;
        //获取不退料标识
        List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.select(new HmeNcRecordAttr() {{
            setTenantId(tenantId);
            setParentRecordId(mtNcRecord.getNcRecordId());
        }});
        String returnMaterialFlag = CollectionUtils.isNotEmpty(ncRecordAttrList) ? ncRecordAttrList.get(0).getAttribute2() : "";
        //获取LOV的值
        List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        // 判断条码是否是芯片物料
        Boolean cosMaterialFlag = false;
        List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(mtNcRecord.getMaterialLotId());
        if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
            List<String> collect = itemGroupList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                cosMaterialFlag = true;
            }
        }
        //有不退料标识 不处理
        //未投料 组件装配 组件报废 记录事务
        if (StringUtils.equals(returnMaterialFlag, YES)) {
            //有不退料标识 不处理
            handleFlag = false;
        } else {
            //无不退料标识 组件报废 记录事务
            handleFlag = true;
        }

        if(handleFlag){
            String bomComponentId = null;
            //强制装配标识
            String assembleExcessFlag = YES;
            //根据工位找工序
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
            //查询bom组件id,找不到为Y,否则为N
            List<String> bomComponentIdList = hmeNcCheckMapper.queryCurrentBomComponentId(tenantId, mtNcRecord.getEoId(), mtNcRecord.getComponentMaterialId(), mtModOrganizationRel.getParentOrganizationId());
            if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
                bomComponentId = bomComponentIdList.get(0);
            }
            if (StringUtils.isBlank(bomComponentId)) {
                assembleExcessFlag = YES;
            } else {
                assembleExcessFlag = NO;
            }

            // 先记录事务  后调api进组件装配或报废及取消
            //记录事务
            this.createNcCheckTransaction(tenantId, mtNcRecord, eventId, mtMaterialLot, mtModLocator);

            // 工艺及工艺步骤
            MtRouterOperation routerOperation = this.queryEoRouteOperation(tenantId, mtNcRecord);
            //2021-02-09 add by sanfeng.zhang for wang.can 是否投料 都要进行组件装配调用API{eoComponentAssemble}进行工单组件装配
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

            //调用API{eoComponentScrap}进行工单组件报废
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
            //根据工位找产线
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

            //组件取消
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

            // 取产线的不良虚拟库位 多个或找不到则报错
            String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
            // 调用API{onhandQtyUpdateProcess}扣减虚拟仓库存
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

            // 芯片物料时 将装载位置 materialLotId 值空
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
                        // 获取LoadSequence对应的eoId
                        List<String> eoIdList = hmeNcCheckMapper.queryEoIdByLoadSequence(tenantId, hmeNcRecordAttr.getAttribute7());
                        // 找得到eoId 则进行下一步
                        if (CollectionUtils.isNotEmpty(eoIdList)) {
                            hmeNcDisposePlatformRepository.updateFunctionInfo(tenantId, mtNcRecord.getEoId(), mtNcRecord.getWorkcellId(), hmeNcRecordAttr.getAttribute7());
                        }
                    }
                }
            }
        }
    }

    private void createNcCheckTransaction(Long tenantId, MtNcRecord mtNcRecord, String eventId, MtMaterialLot mtMaterialLot, MtModLocator mtModLocator) {
        // 判断是否在bom内
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
            // 在bom内
            // 先判断 如果找到多条bom组件则继续判断找到唯一一条
            MtBomComponent mtBomComponent;
            if (componentList.size() == 1) {
                mtBomComponent = componentList.get(0);
            } else {
                // 继续判断 根据bomComponent和RouterOperationId在mt_router_operation_component找到唯一
                mtBomComponent = this.queryOnlyBomComponent(tenantId, componentList, mtNcRecord);
            }
            // 已报废
            BigDecimal scrappedQty = BigDecimal.ZERO;
            // 总需求数量
            BigDecimal totalDemand = BigDecimal.ZERO;
            // 装配数量
            BigDecimal assembleQty = BigDecimal.ZERO;

            List<WmsObjectTransactionRequestVO> requestVOList = new ArrayList<>();
            // 判断是否为替代料 等于0 -> 替代料 不等于0 -> 非替代料
            if (mtBomComponent.getQty().compareTo(0D) != 0) {
                // 装配实绩
                List<MtWorkOrderComponentActual> actualList = this.queryScrappedQtyOfNoSubstitute(tenantId, mtNcRecord, mtWorkOrder, mtBomComponent, mtEoBom);
                Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // 报废数量 -> 根据勾选主键物料以及对应工单替代料和全局替代料的material_id和对应工单的work_order_id在装配实绩取scrapped_qty汇总
                scrappedQty = totalScrappedQty != null ? BigDecimal.valueOf(totalScrappedQty) : BigDecimal.ZERO;
                Double totalAssembleQty = actualList.stream().map(MtWorkOrderComponentActual::getAssembleQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // 装配数量 -> 根据勾选主键物料以及对应工单替代料和全局替代料的material_id和对应工单的work_order_id在装配实绩取assemble_qty汇总
                assembleQty = totalAssembleQty != null ? BigDecimal.valueOf(totalAssembleQty) : BigDecimal.ZERO;
                // 总需求数 根据主料去查
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

                // 非替代料 投料事务计划内/外 根据已报废数量、申请数量及装配数量与总需求做对比
                if (scrappedQty.add(assembleQty).compareTo(totalDemand) >= 0) {
                    // 已报废数量加装配数量 大于或等于 总需求数量 计划外投料 申请数量全做计划外
                    WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                    requestVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    requestVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                    requestVO.setMoveType("261");
                    requestVOList.add(requestVO);
                } else {
                    // 已报废数量加装配数量及申请数量 小于或等于 总需求数量计划内投料 保存预留编码及项目编码
                    if (scrappedQty.add(assembleQty).add(BigDecimal.valueOf(mtNcRecord.getQty())).compareTo(totalDemand) <= 0) {
                        WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                        requestVO.setTransactionTypeCode("HME_WO_ISSUE");
                        requestVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                        requestVO.setMoveType("261");

                        //行号
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
                        //传入销售订单
                        MtExtendVO bomExtend = new MtExtendVO();
                        bomExtend.setTableName("mt_bom_component_attr");
                        bomExtend.setKeyId(mtBomComponent.getBomComponentId());
                        bomExtend.setAttrName("lineAttribute11");
                        List<MtExtendAttrVO> extendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomExtend);
                        if (CollectionUtils.isNotEmpty(extendAttrVOList) && StringUtils.equals("E", extendAttrVOList.get(0).getAttrValue())) {
                            //销售订单号 取mt_work_order的扩展字段attribute1
                            MtExtendVO workExtend = new MtExtendVO();
                            workExtend.setTableName("mt_work_order_attr");
                            workExtend.setKeyId(mtEo.getWorkOrderId());
                            workExtend.setAttrName("attribute1");
                            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                                requestVO.setSoNum(mtExtendAttrVOList.get(0).getAttrValue());
                            }
                            //销售订单行号 取mt_work_order的扩展字段attribute7
                            workExtend.setAttrName("attribute7");
                            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                            if (CollectionUtils.isNotEmpty(attrVOList)) {
                                requestVO.setSoLineNum(attrVOList.get(0).getAttrValue());
                            }
                        }
                        requestVOList.add(requestVO);
                    } else {
                        // 超出的部分做计划外投料 已报废数量加装配数量与总需求数量差值做计划内投料
                        WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                        requestOutVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        requestOutVO.setTransactionQty(scrappedQty.add(assembleQty).add(BigDecimal.valueOf(mtNcRecord.getQty())).subtract(totalDemand));
                        requestOutVO.setMoveType("261");
                        requestVOList.add(requestOutVO);

                        // 计划内投料
                        WmsObjectTransactionRequestVO requestInVO = new WmsObjectTransactionRequestVO();
                        requestInVO.setTransactionTypeCode("HME_WO_ISSUE");
                        requestInVO.setTransactionQty(totalDemand.subtract(scrappedQty.add(assembleQty)));
                        requestInVO.setMoveType("261");

                        //行号
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
                        //传入销售订单
                        MtExtendVO bomExtend = new MtExtendVO();
                        bomExtend.setTableName("mt_bom_component_attr");
                        bomExtend.setKeyId(mtBomComponent.getBomComponentId());
                        bomExtend.setAttrName("lineAttribute11");
                        List<MtExtendAttrVO> extendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomExtend);
                        if (CollectionUtils.isNotEmpty(extendAttrVOList) && StringUtils.equals("E", extendAttrVOList.get(0).getAttrValue())) {
                            //销售订单号 取mt_work_order的扩展字段attribute1
                            MtExtendVO workExtend = new MtExtendVO();
                            workExtend.setTableName("mt_work_order_attr");
                            workExtend.setKeyId(mtEo.getWorkOrderId());
                            workExtend.setAttrName("attribute1");
                            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                                requestInVO.setSoNum(mtExtendAttrVOList.get(0).getAttrValue());
                            }
                            //销售订单行号 取mt_work_order的扩展字段attribute7
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
                // 替代料 全做计划外投料 事务数量为 申请数量
                WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                requestOutVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                requestOutVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                requestOutVO.setMoveType("261");
                requestVOList.add(requestOutVO);

                // 通过替代料找对应主键料
                List<String> materialIdList = hmeNcCheckMapper.queryPrimaryMaterial(tenantId, mtBomComponent.getBomId(), mtBomComponent.getMaterialId());
                // 找不到或找到多个则报错
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
                // 根据工单替代料找对应主键料, 再根据主键料找工单替代料、全局替代料 再汇总计算报废数量
                List<MtWorkOrderComponentActual> actualList = this.queryScrappedQtyOfNoSubstitute(tenantId, mtNcRecord, mtWorkOrder, mainBomComponentList.get(0), mtEoBom);
                Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // 报废数量 -> 根据勾选主键物料以及对应工单替代料和全局替代料的material_id和对应工单的work_order_id在装配实绩取scrapped_qty汇总
                scrappedQty = totalScrappedQty != null ? BigDecimal.valueOf(totalScrappedQty) : BigDecimal.ZERO;
                Double totalAssembleQty = actualList.stream().map(MtWorkOrderComponentActual::getAssembleQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // 装配数量 -> 根据勾选主键物料以及对应工单替代料和全局替代料的material_id和对应工单的work_order_id在装配实绩取assemble_qty汇总
                assembleQty = totalAssembleQty != null ? BigDecimal.valueOf(totalAssembleQty) : BigDecimal.ZERO;

                // 总需求数量和净需求数量 根据主料去查
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

                // 主键料 投退料事务
                List<WmsObjectTransactionRequestVO> primaryRequestVOList = this.assemblyPrimaryMaterialTransaction(tenantId, mtNcRecord, totalDemand, assembleQty, scrappedQty, mtBomComponent, mtEo.getWorkOrderId());

                // 事务集合
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
                // 记录投退料事务
                if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
                }
            }

            // 填充事务集合
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
                transactionRequestVO.setTransactionReasonCode("不良报废投料");
                transactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                transactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
                transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                transactionRequestVO.setMergeFlag(NO);
                transactionRequestVO.setMoveType(requestVO.getMoveType());
                transactionRequestVO.setMoveReason("不良报废投料");
                transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                transactionRequestVO.setSoNum(requestVO.getSoNum());
                transactionRequestVO.setSoLineNum(requestVO.getSoLineNum());
                wmsObjectTransactionRequestVOList.add(transactionRequestVO);
            }
            // 记录事务
            if (CollectionUtils.isNotEmpty(wmsObjectTransactionRequestVOList)) {
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, wmsObjectTransactionRequestVOList);
            }
        } else {
            // 在bom外 先判断是否为替代料
            MtBomComponent bomComponent = queryPrimaryMaterial(tenantId, mtWorkOrder, mtNcRecord, mtEoBom.getBomId());

            if (bomComponent != null) {
                // 替代料 计划外投料 事务数量：申请数量
                List<WmsObjectTransactionRequestVO> requestVOList = new ArrayList<>();
                WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                requestOutVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                requestOutVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                requestOutVO.setMoveType("261");
                requestVOList.add(requestOutVO);

                // 已报废数量
                List<MtWorkOrderComponentActual> actualList = this.queryScrappedQtyOfNoSubstitute(tenantId, mtNcRecord, mtWorkOrder, bomComponent, mtEoBom);
                Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // 报废数量 -> 根据勾选主键物料以及对应工单替代料和全局替代料的material_id和对应工单的work_order_id在装配实绩取scrapped_qty汇总
                BigDecimal scrappedQty = totalScrappedQty != null ? BigDecimal.valueOf(totalScrappedQty) : BigDecimal.ZERO;
                Double totalAssembleQty = actualList.stream().map(MtWorkOrderComponentActual::getAssembleQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                // 装配数量 -> 根据勾选主键物料以及对应工单替代料和全局替代料的material_id和对应工单的work_order_id在装配实绩取assemble_qty汇总
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

                // 总需求数量
                BigDecimal totalDemand = BigDecimal.ZERO;
                // 净需求数量
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

                // 记录主键料投退料事务
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

                // 替代料事务
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
                    transactionRequestVO.setTransactionReasonCode("不良报废投料");
                    transactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                    transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                    transactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
                    transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    transactionRequestVO.setMergeFlag(NO);
                    transactionRequestVO.setMoveType(requestVO.getMoveType());
                    transactionRequestVO.setMoveReason("不良报废投料");
                    transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                    transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                    transactionRequestVO.setSoNum(requestVO.getSoNum());
                    transactionRequestVO.setSoLineNum(requestVO.getSoLineNum());
                    substituteTransactionList.add(transactionRequestVO);
                }
                // 记录事务
                if (CollectionUtils.isNotEmpty(substituteTransactionList)) {
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, substituteTransactionList);
                }
            } else {
                // 没取到 则不为替代料 计划外投料(事务数量: 申请数量)
                WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
                transactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                transactionRequestVO.setEventId(eventId);
                transactionRequestVO.setMaterialLotId(mtNcRecord.getMaterialLotId());
                transactionRequestVO.setMaterialId(mtNcRecord.getComponentMaterialId());
                transactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
                transactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
                transactionRequestVO.setTransactionTime(new Date());
                transactionRequestVO.setTransactionReasonCode("不良报废投料");
                transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
                transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                transactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
                transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                transactionRequestVO.setMergeFlag(NO);
                transactionRequestVO.setMoveType("261");
                transactionRequestVO.setMoveReason("不良报废投料");
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(transactionRequestVO));
            }
        }
    }

    private List<MtWorkOrderComponentActual> queryScrappedQtyOfNoSubstitute(Long tenantId, MtNcRecord mtNcRecord, MtWorkOrder mtWorkOrder, MtBomComponent mtBomComponent, MtEoBom mtEoBom) {
        // 主键料、工单替代料、全局替代料集合
        List<String> materialIdList = new ArrayList<>();
        // 勾选主键物料找对应工单替代料
        List<String> substituteMaterialList = hmeCosPatchPdaMapper.getWorkOrderSubstituteMaterial(tenantId, mtBomComponent.getBomComponentId());
        List<MtBomComponent> bomComponentList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(substituteMaterialList)) {
            bomComponentList = mtBomComponentRepository.selectByCondition(Condition.builder(MtBomComponent.class)
                    .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtBomComponent.FIELD_BOM_ID, mtEoBom.getBomId())
                            .andIn(MtBomComponent.FIELD_MATERIAL_ID, substituteMaterialList)).build());
        }
        if (CollectionUtils.isNotEmpty(bomComponentList)) {
            // 不为空 则有工单替代料 可能存在多条
            materialIdList.addAll(bomComponentList.stream().map(MtBomComponent::getMaterialId).collect(Collectors.toList()));
        }
        // 勾选主键物料找对应全局替代料
        //根据BOM组件物料ID+站点ID查询全局替代料
        List<String> globalSubstituteMaterialList = hmeCosPatchPdaMapper.getGlobalSubstituteMaterial(tenantId, mtBomComponent.getMaterialId(), mtNcRecord.getSiteId());
        if (CollectionUtils.isNotEmpty(globalSubstituteMaterialList)) {
            materialIdList.addAll(globalSubstituteMaterialList);
        }
        materialIdList.add(mtBomComponent.getMaterialId());
        // 找对应的工艺步骤
        MtRouterOperation routerOperation = this.queryWoRouteOperation(tenantId, mtNcRecord, mtWorkOrder);

        List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                .andIn(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, materialIdList)
                .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerOperation.getRouterStepId())).build());
        return actualList;
    }

    /**
     * 根据bomComponent和RouterOperationId在mt_router_operation_component找到唯一
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
        // 根据工序找工艺 找到多个报错
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
        // 根据EOId找到 工艺路线步骤对应工序
        List<MtRouterOperation> mtRouterOperationList = hmeNcCheckMapper.queryWoRouterOperationListByRouterId(tenantId, mtWorkOrder.getRouterId());
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtRouterOperationList)) {
            operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                    .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())
                    .andIn(MtOperationWkcDispatchRel.FIELD_OPERATION_ID, mtRouterOperationList.stream().map(MtRouterOperation::getOperationId).collect(Collectors.toList()))).build());
        }
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
            //如果找不到，报错{当前工序未找到工艺,请检查}
            throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0044", "HME"));
        } else if (operationWkcDispatchRelList.size() > 1) {
            //如果找到多个，报错{当前工序对应多个工艺,请检查}
            throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0045", "HME"));
        }
        operationId = operationWkcDispatchRelList.get(0).getOperationId();

        //根据operationId过滤唯一的RouterStepId
        MtRouterOperation routerOperation = null;
        for (MtRouterOperation mtRouterOperation : mtRouterOperationList) {
            if (StringUtils.equals(mtRouterOperation.getOperationId(), operationId)) {
                routerOperation = mtRouterOperation;
                break;
            }
        }

        if (routerOperation == null) {
            //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        }
        return routerOperation;
    }

    /**
     * 找eo对应的工艺路线步骤
     *
     * @param tenantId
     * @param mtNcRecord
     * @return tarzan.method.domain.entity.MtRouterOperation
     * @author sanfeng.zhang@hand-china.com 2020/12/23 17:50
     */
    private MtRouterOperation queryEoRouteOperation(Long tenantId, MtNcRecord mtNcRecord) {
        // 根据工序找工艺 找到多个报错
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
        // 根据EOId找到 工艺路线步骤对应工序
        List<MtRouterOperation> mtRouterOperationList = hmeNcCheckMapper.queryRouterOperationListByEoId(tenantId, mtNcRecord.getEoId());
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtRouterOperationList)) {
            operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                    .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())
                    .andIn(MtOperationWkcDispatchRel.FIELD_OPERATION_ID, mtRouterOperationList.stream().map(MtRouterOperation::getOperationId).collect(Collectors.toList()))).build());
        }
        if (CollectionUtils.isEmpty(operationWkcDispatchRelList)) {
            //如果找不到，报错{当前工序未找到工艺,请检查}
            throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0044", "HME"));
        } else if (operationWkcDispatchRelList.size() > 1) {
            //如果找到多个，报错{当前工序对应多个工艺,请检查}
            throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0045", "HME"));
        }
        operationId = operationWkcDispatchRelList.get(0).getOperationId();

        //根据operationId过滤唯一的RouterStepId
        MtRouterOperation routerOperation = null;
        for (MtRouterOperation mtRouterOperation : mtRouterOperationList) {
            if (StringUtils.equals(mtRouterOperation.getOperationId(), operationId)) {
                routerOperation = mtRouterOperation;
                break;
            }
        }

        if (routerOperation == null) {
            //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        }
        return routerOperation;
    }

    /**
     * 在bom外 全局替代料找对应主键料
     *
     * @param tenantId
     * @param mtWorkOrder
     * @param mtNcRecord
     * @param bomId
     * @return tarzan.method.domain.entity.MtBomComponent
     * @author sanfeng.zhang@hand-china.com 2020/12/23 14:47
     */
    private MtBomComponent queryPrimaryMaterial(Long tenantId, MtWorkOrder mtWorkOrder, MtNcRecord mtNcRecord, String bomId) {
        // bom组件
        MtBomComponent mtBomComponent = null;
        //根据站点及组件物料找替代组关系
        List<WmsMaterialSubstituteRel> substituteRelList = wmsMaterialSubstituteRelRepository.select(new WmsMaterialSubstituteRel() {{
            setTenantId(tenantId);
            setSiteId(mtWorkOrder.getSiteId());
            setMaterialId(mtNcRecord.getComponentMaterialId());
        }});
        List<String> substituteGroupList = substituteRelList.stream().map(WmsMaterialSubstituteRel::getSubstituteGroup).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        //取到多个 则报错
        if (CollectionUtils.isNotEmpty(substituteGroupList) && substituteGroupList.size() > 1) {
            throw new MtException("HME_NC_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0043", "HME"));
        }
        if (CollectionUtils.isNotEmpty(substituteGroupList)) {
            // 根据替代组 找下面所有替代物料
            List<WmsMaterialSubstituteRel> materialSubstituteRelList = wmsMaterialSubstituteRelRepository.select(new WmsMaterialSubstituteRel() {{
                setTenantId(tenantId);
                setSubstituteGroup(substituteGroupList.get(0));
            }});
            List<String> materialSubstituteIdList = materialSubstituteRelList.stream().map(WmsMaterialSubstituteRel::getMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<MtBomComponent> mtBomComponentList = new ArrayList<>();
            // 替代料去找主键料
            if (CollectionUtils.isNotEmpty(materialSubstituteIdList)) {
                mtBomComponentList = mtBomComponentRepository.selectByCondition(Condition.builder(MtBomComponent.class)
                        .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtBomComponent.FIELD_BOM_ID, bomId)
                                .andIn(MtBomComponent.FIELD_MATERIAL_ID, materialSubstituteIdList)).build());
            }
            if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
                if (mtBomComponentList.size() == 1) {
                    // 只有一个 则为当前替代料物料的主料
                    mtBomComponent = mtBomComponentList.get(0);
                } else if (mtBomComponentList.size() > 1) {
                    // 继续判断
                    // 根据工序找工艺 找到多个报错
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
                        //如果找不到，报错{当前工序未找到工艺,请检查}
                        throw new MtException("HME_NC_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0044", "HME"));
                    } else if (operationWkcDispatchRelList.size() > 1) {
                        //如果找到多个，报错{当前工序对应多个工艺,请检查}
                        throw new MtException("HME_NC_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0045", "HME"));
                    }
                    operationId = operationWkcDispatchRelList.get(0).getOperationId();

                    //根据eoId,operationId查询MtRouterOperation
                    List<MtRouterOperation> mtRouterOperationList = hmeNcCheckMapper.getRouterStepByEoId(tenantId, mtNcRecord.getEoId(), operationId);

                    if (CollectionUtils.isEmpty(mtRouterOperationList)) {
                        //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
                        throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0046", "HME"));
                    } else if (mtRouterOperationList.size() > 1) {
                        //如果找到多个，报错{当前工艺找到多个工艺步骤，请检查}
                        throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0047", "HME"));
                    }
                    String routerOperationId = mtRouterOperationList.get(0).getRouterOperationId();
                    //根据多个bomComponentId、routerOperationId查询表mt_router_operation_component
                    List<MtRouterOperationComponent> mtRouterOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class)
                            .andWhere(Sqls.custom().andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                                    .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))
                                    .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperationId)).build());
                    if (CollectionUtils.isEmpty(mtRouterOperationComponentList)) {
                        //如果找不到，则报错{所扫描条码物料与装配清单不匹配,请检查}
                        throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_073", "HME"));
                    } else if (mtRouterOperationComponentList.size() > 1) {
                        //如果找不到，则报错{当前统一工艺步骤存在多个相同组件，请检查}
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
     * 组装主键料的事务
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
            // 申请数量 小于或等于 总需求数量减装配数量减已报废数量 先做计划外退料 再是计划内投料
            WmsObjectTransactionRequestVO requestOutReturnVO = new WmsObjectTransactionRequestVO();
            requestOutReturnVO.setTransactionTypeCode("HME_WO_ISSUE_R_EXT");
            requestOutReturnVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
            requestOutReturnVO.setTransactionReasonCode("不良退料");
            requestOutReturnVO.setMoveReason("不良退料");
            requestOutReturnVO.setMoveType("262");
            transactionRequestVOList.add(requestOutReturnVO);

            // 计划内投料
            WmsObjectTransactionRequestVO requestInFeedVO = new WmsObjectTransactionRequestVO();
            requestInFeedVO.setTransactionTypeCode("HME_WO_ISSUE");
            requestInFeedVO.setTransactionQty(BigDecimal.valueOf(mtNcRecord.getQty()));
            requestInFeedVO.setTransactionReasonCode("不良报废投料");
            requestOutReturnVO.setMoveReason("不良报废投料");
            requestInFeedVO.setMoveType("261");
            //行号
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
            //传入销售订单
            MtExtendVO bomExtend = new MtExtendVO();
            bomExtend.setTableName("mt_bom_component_attr");
            bomExtend.setKeyId(mtBomComponent.getBomComponentId());
            bomExtend.setAttrName("lineAttribute11");
            List<MtExtendAttrVO> extendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomExtend);
            if (CollectionUtils.isNotEmpty(extendAttrVOList) && StringUtils.equals("E", extendAttrVOList.get(0).getAttrValue())) {
                //销售订单号 取mt_work_order的扩展字段attribute1
                MtExtendVO workExtend = new MtExtendVO();
                workExtend.setTableName("mt_work_order_attr");
                workExtend.setKeyId(wordOrderId);
                workExtend.setAttrName("attribute1");
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    requestInFeedVO.setSoNum(mtExtendAttrVOList.get(0).getAttrValue());
                }
                //销售订单行号 取mt_work_order的扩展字段attribute7
                workExtend.setAttrName("attribute7");
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                if (CollectionUtils.isNotEmpty(attrVOList)) {
                    requestInFeedVO.setSoLineNum(attrVOList.get(0).getAttrValue());
                }
            }
            transactionRequestVOList.add(requestInFeedVO);
        } else {
            // 进一步判断总需求数量减净需求数量差值与报废数量对比
            if (totalDemand.subtract(assembleQty).subtract(scrappedQty).compareTo(BigDecimal.ZERO) > 0) {
                // 总需求数量减净需求数量差值大于报废数量时
                // 计划外退料 (事务数量：总需求数量减净需求数量差值-报废数量)
                WmsObjectTransactionRequestVO requestOutReturnVO = new WmsObjectTransactionRequestVO();
                requestOutReturnVO.setTransactionTypeCode("HME_WO_ISSUE_R_EXT");
                requestOutReturnVO.setTransactionQty(totalDemand.subtract(assembleQty).subtract(scrappedQty));
                requestOutReturnVO.setTransactionReasonCode("不良退料");
                requestOutReturnVO.setMoveReason("不良退料");
                requestOutReturnVO.setMoveType("262");
                transactionRequestVOList.add(requestOutReturnVO);

                // 计划内投料 (事务数量：总需求数量减净需求数量差值-报废数量)
                WmsObjectTransactionRequestVO requestInFeedVO = new WmsObjectTransactionRequestVO();
                requestInFeedVO.setTransactionTypeCode("HME_WO_ISSUE");
                requestInFeedVO.setTransactionQty(totalDemand.subtract(assembleQty).subtract(scrappedQty));
                requestInFeedVO.setTransactionReasonCode("不良报废投料");
                requestInFeedVO.setMoveReason("不良报废投料");
                requestInFeedVO.setMoveType("261");
                //行号
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
                //传入销售订单
                MtExtendVO bomExtend = new MtExtendVO();
                bomExtend.setTableName("mt_bom_component_attr");
                bomExtend.setKeyId(mtBomComponent.getBomComponentId());
                bomExtend.setAttrName("lineAttribute11");
                List<MtExtendAttrVO> extendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, bomExtend);
                if (CollectionUtils.isNotEmpty(extendAttrVOList) && StringUtils.equals("E", extendAttrVOList.get(0).getAttrValue())) {
                    //销售订单号 取mt_work_order的扩展字段attribute1
                    MtExtendVO workExtend = new MtExtendVO();
                    workExtend.setTableName("mt_work_order_attr");
                    workExtend.setKeyId(wordOrderId);
                    workExtend.setAttrName("attribute1");
                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, workExtend);
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                        requestInFeedVO.setSoNum(mtExtendAttrVOList.get(0).getAttrValue());
                    }
                    //销售订单行号 取mt_work_order的扩展字段attribute7
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
        // 不良记录列表
        List<MtNcRecord> mtNcRecordList = mtNcRecordRepository.selectByCondition(Condition.builder(MtNcRecord.class).andWhere(Sqls.custom()
                .andEqualTo(MtNcRecord.FIELD_TENANT_ID, tenantId)
                .andIn(MtNcRecord.FIELD_NC_RECORD_ID, ncRecordIdList)).build());
        if (CollectionUtils.isEmpty(mtNcRecordList)) {
            throw new MtException("HME_NC_0070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_NC_0070", "HME"));
        }
        // 不良记录 状态为OPEN 才能提交
        Optional<MtNcRecord> nonOpenOpt = mtNcRecordList.stream().filter(nr -> !HmeConstants.NcStatus.OPEN.equals(nr.getNcStatus())).findFirst();
        if (nonOpenOpt.isPresent()) {
            throw new MtException("HME_NC_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0007", "HME"));
        }
        // 查询不良代码组 通过不良代码组区分材料不良和工序不良
        List<String> ncCodeIdList = mtNcRecordList.stream().map(MtNcRecord::getNcCodeId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 勾选的多个不良记录 不良代码不一致 则报错
        if (CollectionUtils.isNotEmpty(ncCodeIdList) && ncCodeIdList.size() > 1) {
            throw new MtException("HME_NC_0061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0061", "HME"));
        }
        List<MtNcGroup> mtNcGroupList = mtNcGroupRepository.selectByCondition(Condition.builder(MtNcGroup.class).andWhere(Sqls.custom()
                .andEqualTo(MtNcGroup.FIELD_TENANT_ID, tenantId)
                .andIn(MtNcGroup.FIELD_NC_GROUP_ID, ncCodeIdList)).build());
        // 批量要么材料不良 要么工序不良 其他情况报错处理
        List<String> componentRequiredList = mtNcGroupList.stream().map(MtNcGroup::getComponentRequired).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (componentRequiredList.size() > 1) {
            throw new MtException("HME_NC_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0067", "HME"));
        }
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        // 创建事务请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CLOSE");
        //事件创建
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("NC_RECORD_CLOSE");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        // 批量更新不良记录、不良关闭及事故关闭
        this.batchHandleNcRecordClose(tenantId, mtNcRecordList, dto, userId, eventId, eventRequestId);
        // ComponentRequired 为N 则为工序不良 为Y 则为材料不良
        if (NO.equals(componentRequiredList.get(0))) {
            // 工序不良处理 放行 降级转型支持批量 报废不支持
            if (HmeConstants.ProcessStatus.THREE.equals(dto.getProcessMethod()) && ncRecordIdList.size() > 1) {
                throw new MtException("HME_NC_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0068", "HME"));
            }
            // 批量处理工序不良
            this.batchHandleProcessNc(tenantId, mtNcRecordList, dto, userId, eventId);
        } else if (YES.equals(componentRequiredList.get(0))) {
            // 材料不良处理
            // 报废、自制件返修不支持批量
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
        // 只选择事业部 则查询该事业部下所有工位显示
        if (StringUtils.isNotBlank(dto.getAreaId()) && StringUtils.isBlank(dto.getProdLineId()) && StringUtils.isBlank(dto.getProcessId()) && StringUtils.isBlank(dto.getWorkcellId())) {
            flag = true;
            //调用API {subOrganizationRelQuery} 查询该产线下所有工位
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setParentOrganizationId(dto.getAreaId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //查询事业部下所有的工位
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //只选择产线，则查询该产线下所有工位显示
        if (StringUtils.isNotBlank(dto.getProdLineId())
                && StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            flag = true;
            //调用API {subOrganizationRelQuery} 查询该产线下所有工位
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationVO2.setParentOrganizationId(dto.getProdLineId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //查询工序下所有的工位
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                workcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            dto.setWorkcellIdList(workcellIdList);
        }
        //选择了工序，但没有选择工位，则查询该工序下所有工位展示
        if (StringUtils.isNotBlank(dto.getProcessId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            flag = true;
            //调用API {subOrganizationRelQuery} 查询该工序下所有工位
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
        //序列号模糊查询
        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            flag1 = true;
            //根据序列号模糊查询表mt_material_lot的material_lot_id
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(
                    Sqls.custom().andLikeRight(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getMaterialLotCode())
            ).build());
            dto.setMaterialLotIdList(mtMaterialLots.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList()));
        }
        //查询数据
        //当选择了事业部、产线、工序等条件，但其对应的工位、物料批Id为空时，直接返回空
        if ((flag && CollectionUtils.isEmpty(dto.getWorkcellIdList())) || (flag1 && CollectionUtils.isEmpty(dto.getMaterialLotIdList()))) {
            return new ArrayList<>();
        }

        //拆分工位集合
        List<HmeNcDisposePlatformDTO2> ncRecordList = hmeNcDisposePlatformMapper.ncRecordQuery(tenantId, dto);
        for (HmeNcDisposePlatformDTO2 hmeNcDisposePlatformDTO2 : ncRecordList) {
            //车间 根据ROOT_CAUSE_WORKCELL_ID调用API{parentOrganizationRelQuery}
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
            //生产线
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
                hmeNcDisposePlatformDTO2.setProdLineName(mtModProductionLine.getDescription());
            }
            //工段和工序
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
            //工位 根据ROOT_CAUSE_WORKCELL_ID、WORKCELL_TYPE = STATION关联 mt_mod_workcell获取WORKCELL_NAME
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getWorkcellId());
            hmeNcDisposePlatformDTO2.setWorkcellName(mtModWorkcell.getWorkcellName());
            //操作者
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getCreatedBy());
            hmeNcDisposePlatformDTO2.setUserName(mtUserInfo.getRealName());
            //工号
            hmeNcDisposePlatformDTO2.setUserCode(mtUserInfo.getLoginName());
            //序列号 MATERIAL_LOT_ID关联mt_material_lot显示material_lot_code，没关联到显示material_Lot_id
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getMaterialLotId());
            if (mtMaterialLot != null && StringUtils.isNotBlank(mtMaterialLot.getMaterialLotCode())) {
                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            } else {
                hmeNcDisposePlatformDTO2.setMaterialLotCode(hmeNcDisposePlatformDTO2.getMaterialLotId());
            }
            //工单号 根据eo_id关联表mt_eo，找到work_order_id，根据work_order_id在表mt_work_order找到WORK_ORDER_NUM
//            MtEo mtEo = mtEoRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getEoId());
//            if (mtEo != null && StringUtils.isNotBlank(mtEo.getWorkOrderId())) {
//                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
//                if (mtWorkOrder != null) {
//                    hmeNcDisposePlatformDTO2.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
//                }
//            }
            //不良代码 不良原因
            //在表mt_nc_record，根据PARENT_NC_RECORD_ID = NC_RECORD_ID查询数据
            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, hmeNcDisposePlatformDTO2.getNcRecordId());
            List<String> ncCodeIdList = new ArrayList<>();
            List<String> ncCodeList = new ArrayList<>();
            List<String> ncReasonList = new ArrayList<>();
            //处理人备注
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
                    //根据NC_CODE_ID关联表mt_nc_code查询nc_code
                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
                    ncCodeIdList.add(mtNcCode.getNcCodeId());
                    ncCodeList.add(mtNcCode.getNcCode());
                    ncReasonList.add(mtNcCode.getDescription());
                }
            }
            hmeNcDisposePlatformDTO2.setNcCodeIdList(ncCodeIdList);
            hmeNcDisposePlatformDTO2.setNcCodeList(ncCodeList);
            hmeNcDisposePlatformDTO2.setNcReasonList(ncReasonList);

            //拼接不良代码 & 不良原因
            if(CollectionUtils.isNotEmpty(ncCodeList)){
                String ncCode = String.join(",", ncCodeList);
                hmeNcDisposePlatformDTO2.setNcCode(ncCode);
            }
            if (CollectionUtils.isNotEmpty(ncReasonList)){
                String ncReason = String.join(",",ncReasonList);
                hmeNcDisposePlatformDTO2.setNcReason(ncReason);
            }

            //不良组描述  不良类型
            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getNcCodeId());
//                hmeNcDisposePlatformDTO2.setNcGroupDesc(mtNcGroup.getDescription());
            hmeNcDisposePlatformDTO2.setNcType(mtNcGroup.getComponentRequired());
            String meaning = lovAdapter.queryLovMeaning("HME.NC_TYPE", tenantId, mtNcGroup.getComponentRequired());
            hmeNcDisposePlatformDTO2.setNcTypeMeaning(meaning);
            //产品料号 MATERIAL_ID关联mt_material获取material_code
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getMaterialId());
            hmeNcDisposePlatformDTO2.setMaterialCode(mtMaterial.getMaterialCode());
            //产品描述
            hmeNcDisposePlatformDTO2.setMaterialName(mtMaterial.getMaterialName());
            //不良单号
//            MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getNcIncidentId());
//            hmeNcDisposePlatformDTO2.setNcNumber(mtNcIncident.getIncidentNumber());
            //发生时间
            hmeNcDisposePlatformDTO2.setDateTime(hmeNcDisposePlatformDTO2.getDateTime());
            //责任工位
            if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO2.getRootCauseWorkcellId())) {
                MtModWorkcell mtModWorkcell2 = mtModWorkcellRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getRootCauseWorkcellId());
                hmeNcDisposePlatformDTO2.setResponseWorkcellName(mtModWorkcell2.getWorkcellName());
            }
            //责任人
            MtUserInfo mtUserInfo2 = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getUserId());
            hmeNcDisposePlatformDTO2.setResponseUser(mtUserInfo2.getRealName());
            //状态
            hmeNcDisposePlatformDTO2.setStatus(hmeNcDisposePlatformDTO2.getNcStatus());
            MtGenStatus statusMeaning = mtGenStatusRepository.getGenStatus(tenantId, "NC_RECORD", "NC_RECORD_STATUS", hmeNcDisposePlatformDTO2.getNcStatus());
            hmeNcDisposePlatformDTO2.setStatusMeaning(statusMeaning.getDescription());
            //2020-08-28 add by chaonan.hu for zhenyun.ban 增加处理人、处理时间
            if (hmeNcDisposePlatformDTO2.getClosedUserId() != null) {
                MtUserInfo mtUserInfo3 = mtUserRepository.userPropertyGet(tenantId, hmeNcDisposePlatformDTO2.getClosedUserId());
                hmeNcDisposePlatformDTO2.setDisposeUserId(hmeNcDisposePlatformDTO2.getClosedUserId().toString());
                hmeNcDisposePlatformDTO2.setDisposeUserName(mtUserInfo3.getRealName());
                hmeNcDisposePlatformDTO2.setDisposeDateTime(hmeNcDisposePlatformDTO2.getClosedDateTime());
            }
            //处理办法
            HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrRepository.selectOne(new HmeNcRecordAttr() {{
                setTenantId(tenantId);
                setParentRecordId(hmeNcDisposePlatformDTO2.getNcRecordId());
            }});
            if (hmeNcRecordAttr != null) {
                hmeNcDisposePlatformDTO2.setDisposeMethod(hmeNcRecordAttr.getProcessMethod());
                String disposeMethodMeaning = lovAdapter.queryLovMeaning("HME.NC_PROCESS_METHOD", tenantId, hmeNcRecordAttr.getProcessMethod());
                hmeNcDisposePlatformDTO2.setDisposeMethodMeaning(disposeMethodMeaning);
                //转型物料
                //2020-08-25 15:06 add by chaonan.hu for lu.bai 增加转型物料查询逻辑
                if (StringUtils.isNotEmpty(hmeNcRecordAttr.getAttribute1())) {
                    MtMaterial mtMaterialDb = mtMaterialRepository.selectByPrimaryKey(hmeNcRecordAttr.getAttribute1());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialId(hmeNcRecordAttr.getAttribute1());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialCode(mtMaterialDb.getMaterialCode());
                    hmeNcDisposePlatformDTO2.setTransitionMaterialName(mtMaterialDb.getMaterialName());
                }
            }
            //2020-09-28 add by chaonan.hu for lu.bai 增加不良物料、申请数量显示
            if (StringUtils.isNotEmpty(hmeNcDisposePlatformDTO2.getComponentMaterialId())) {
                MtMaterial mtMaterial2 = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO2.getComponentMaterialId());
                hmeNcDisposePlatformDTO2.setScrapMaterialId(mtMaterial2.getMaterialId());
                hmeNcDisposePlatformDTO2.setScrapMaterialCode(mtMaterial2.getMaterialCode());
                hmeNcDisposePlatformDTO2.setScrapMaterialName(mtMaterial2.getMaterialName());
                hmeNcDisposePlatformDTO2.setScrapQty(hmeNcDisposePlatformDTO2.getQty() == null ? BigDecimal.ZERO : new BigDecimal(hmeNcDisposePlatformDTO2.getQty()));
            }
            // 芯片位置
            if (hmeNcDisposePlatformDTO2.getLoadRow() != null && hmeNcDisposePlatformDTO2.getLoadColumn() != null) {
                hmeNcDisposePlatformDTO2.setCosPosition(String.valueOf((char)(hmeNcDisposePlatformDTO2.getLoadRow()+64)) + hmeNcDisposePlatformDTO2.getLoadColumn());
            }
        }
        return ncRecordList;
    }

    private void batchHandleMaterialNc(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId, String eventRequestId) {
        // 材料不良  2-放行 3-报废 5-退库 6-自制件返修
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
        // 工序不良 1-返修 2-放行 3-报废 4-降级转型 7-指定工艺路线返修
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
                    "HME_NC_0006", "HME", "不良代码"));
        }
        if (dto.getNcCodeIdList().size() > 1) {
            throw new MtException("HME_NC_0086", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0086", "HME"));
        }
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
        if (mtEo.getIdentification().length() < 9) {
            throw new MtException("HME_CHIP_DATA_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0009", "HME", "序列号"));
        }
        // 根据eo 查询wo的产线
        List<String> proLineList = hmeNcCheckMapper.queryProLineByEoId(tenantId, eoId);
        if (CollectionUtils.isEmpty(proLineList)) {
            throw new MtException("HME_NC_0083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0083", "HME", mtEo.getIdentification()));
        }
        // 取SN 3,4位
        String deviceType = mtEo.getIdentification().substring(2, 4);
        // 取SN 5,6位
        String chipType = mtEo.getIdentification().substring(4, 6);
        // 获取最后正常工艺 eoJobSn 限制eo 非返修最近的工艺
        String operationId = hmeNcCheckMapper.queryLastNonReworkOperationId(tenantId, eoId);
        List<String> routeIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(operationId)) {
            routeIdList = this.queryRouteIdListLimitOperationId(tenantId, dto.getNcGroupId(), dto.getNcCodeIdList(), proLineList.get(0), deviceType, chipType, operationId);
        }
        // 以上限制工艺找不到 则不限制工艺去找
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
            // 找不到 将chipType置空查询
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, deviceType, null, operationId);
        }
        if (CollectionUtils.isEmpty(routeIdList)) {
            // 找不到 将deviceType置空查询
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, null, chipType, operationId);
        }
        if (CollectionUtils.isEmpty(routeIdList)) {
            // 找不到 将deviceType、chipType置空查询
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, null, null, operationId);
        }
        return routeIdList;
    }

    @Override
    public List<String> queryRouteIdListNonLimitOperationId(Long tenantId, String ncGroupId, List<String> ncCodeIdList, String prodLineId, String deviceType, String chipType) {
        List<String> routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, deviceType, chipType, null);
        if (CollectionUtils.isEmpty(routeIdList)) {
            // 找不到 将chipType置空查询
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, deviceType, null, null);
        }
        if (CollectionUtils.isEmpty(routeIdList)) {
            // 找不到 将deviceType置空查询
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, null, chipType, null);
        }
        if (CollectionUtils.isEmpty(routeIdList)) {
            // 找不到 将deviceType、chipType置空查询
            routeIdList = hmeNcCheckMapper.queryRouteIdByNcCode(tenantId, ncGroupId, ncCodeIdList, prodLineId, null, null, null);
        }
        return routeIdList;
    }

    private MtRouter handleRouteCopyAndAssemblyComponent(Long tenantId, String routeId, String eoId) {
        MtRouter mtRouter = mtRouterRepository.selectByPrimaryKey(routeId);
        MtWorkOrder mtWorkOrder = hmeNcCheckMapper.queryWorkOrderByEoId(tenantId, eoId);
        // 判断指定工艺路线工单bom是否存在 根据工艺路线编码-工单编码
        String routerName = mtRouter.getRouterName() + "-" + mtWorkOrder.getWorkOrderNum();
        List<MtRouter> mtRouterList = mtRouterRepository.selectByCondition(Condition.builder(MtRouter.class).andWhere(Sqls.custom()
                .andEqualTo(MtRouter.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtRouter.FIELD_ROUTER_NAME, routerName)).build());
        if (CollectionUtils.isNotEmpty(mtRouterList)) {
            // 存在 则将这些工艺路线工艺路线编码都更新成工艺路线编码-工单编码-cid
            List<String> routerIdList = mtRouterList.stream().map(MtRouter::getRouterId).collect(Collectors.toList());
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmeNcCheckMapper.updateRouterNameByRouterIdList(tenantId, routerIdList, userId, routerName);
        }
        // 复制指定返修工艺路线 调用api：routerCopy
        MtRouterVO1 routerVO1 = new MtRouterVO1();
        routerVO1.setRouterId(routeId);
        routerVO1.setRouterName(routerName);
        routerVO1.setRevision("MAIN");
        routerVO1.setRouterType("MATERIAL");
        routerVO1.setSiteIds(Arrays.asList(mtWorkOrder.getSiteId()));
        routerVO1.setBomId(mtWorkOrder.getBomId());
        String newRouterId = mtRouterRepository.routerCopy(tenantId, routerVO1);
        // 组件分配
        this.assemblyComponent(tenantId, newRouterId, mtWorkOrder);
        MtRouter newRouter = new MtRouter();
        newRouter.setRouterId(newRouterId);
        newRouter.setBomId(mtWorkOrder.getBomId());
        return newRouter;
    }

    private void assemblyComponent(Long tenantId, String newRouterId, MtWorkOrder mtWorkOrder) {
        // 取复制后工艺路线所有工艺及步骤识别码
        List<HmeNcCheckVO4> newRouteOperationList = hmeNcCheckMapper.queryOperationIdAndStepName(tenantId, newRouterId);
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("HME.DESIGNED_ROUTER_OPERATION_MAPPING", tenantId);
        // 工艺路线工艺映射关系处理
        newRouteOperationList.forEach(nro -> {
            Optional<LovValueDTO> firstOpt = lovValueList.stream().filter(lov -> StringUtils.equals(lov.getValue(), nro.getOperationName())).findFirst();
            if (firstOpt.isPresent()) {
                nro.setOperationName(firstOpt.get().getMeaning());
            }
        });
        List<String> operationNameList = newRouteOperationList.stream().map(HmeNcCheckVO4::getOperationName).collect(Collectors.toList());
        Map<String, List<HmeNcCheckVO4>> mtRouterOperationComponentMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(operationNameList)) {
            // 按工艺取工单工艺路线步骤组件
            List<HmeNcCheckVO4> mtRouterOperationComponentList = hmeNcCheckMapper.queryRouterOperationComponent(tenantId, mtWorkOrder.getRouterId(), operationNameList);
            mtRouterOperationComponentMap = mtRouterOperationComponentList.stream().collect(Collectors.groupingBy(HmeNcCheckVO4::getOperationName));
        }
        // 复制后工艺路线 组建分配
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
        // 批量新增组件数据
        if (CollectionUtils.isNotEmpty(insetOperationComponentList)) {
            // 批量查询序列号
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
//        // 批量查询返修标识
//        List<HmeBatchNcCheckVO> hmeBatchNcCheckVOList = hmeNcCheckMapper.batchQueryReworkFlagByEoId(tenantId, eoIdList);
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        List<HmeEoRouterBomRel> hmeEoRouterBomRelList = new ArrayList<>();
        // 批量查询序列号
        List<String> relIdList = customSequence.getNextKeys("hme_eo_router_bom_rel_s", mtNcRecordList.size());
        List<String> relCidList = customSequence.getNextKeys("hme_eo_router_bom_rel_cid_s", mtNcRecordList.size());
        Integer relIndex = 0;
        Date curDate = CommonUtils.currentTimeGet();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
//            // 2021-01-07 add by sanfeng.zhang for wang.can 校验当前EO是否返修中
//            // 2021-02-24 add by sanfeng.zhang for bai.lu 勾选了返修记录 不校验当前EO是否返修中
//            if (!YES.equals(dto.getReworkRecordFlag())) {
//                Optional<HmeBatchNcCheckVO> reworkFlagOpt = hmeBatchNcCheckVOList.stream().filter(vo -> StringUtils.equals(vo.getEoId(), mtNcRecord.getEoId())).findFirst();
//                String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getReworkFlag() : "";
//                if (YES.equals(reworkFlag)) {
//                    throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "HME_NC_0049", "HME"));
//                }
//            }
            //如果勾选了返修，则调用API{materialLotAttrPropertyUpdate}更新条码的返修标识
            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(mtNcRecord.getMaterialLotId());
            List<MtCommonExtendVO5> attrList = new ArrayList<>();
            // 勾选返修记录 不更新条码返修标识
            if (!YES.equals(dto.getReworkRecordFlag())) {
                MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                mtCommonExtendVO5.setAttrName("REWORK_FLAG");
                mtCommonExtendVO5.setAttrValue(YES);
                attrList.add(mtCommonExtendVO5);
            }
            // 指定工艺路线取值修改
            String routeId = this.getRouteByNcCode(tenantId, dto, mtNcRecord.getEoId(), mtNcRecord.getNcIncidentId());
            // 复制工艺路线及组件分配
            MtRouter mtRouter = this.handleRouteCopyAndAssemblyComponent(tenantId, routeId, mtNcRecord.getEoId());
            //2020/12/03 add by sanfeng.zhang for malifeng 增加处置组
            MtCommonExtendVO5 reworkAttr = new MtCommonExtendVO5();
            reworkAttr.setAttrName("REWORK_ROUTER");
            reworkAttr.setAttrValue(mtRouter.getRouterId());
            attrList.add(reworkAttr);

            // 2021/1/6 add by sanfeng.zahng for xietianyang 增加指定返修标识
            MtCommonExtendVO5 flagAttr = new MtCommonExtendVO5();
            flagAttr.setAttrName("DESIGNED_REWORK_FLAG");
            flagAttr.setAttrValue(YES);
            attrList.add(flagAttr);

            // 2021/1/6 add by sanfeng.zhang for xietianyang 增加关联EO、工艺路线及装配清单关系hme_eo_router_bom_rel
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
            //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban 增加将条码与容器解绑的逻辑
            hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
        }
        //2020-08-25 add by chaonan.hu for lu.bai 如果勾选直接返修，则将找到的未出站数据出站
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
     * 工序不良 - 返修
     *
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 14:52
     */
    private void processRework(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, Long userId, String eventId) {
        List<String> eoIdList = mtNcRecordList.stream().map(MtNcRecord::getEoId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 批量查询返修标识
        List<HmeBatchNcCheckVO> hmeBatchNcCheckVOList = hmeNcCheckMapper.batchQueryReworkFlagByEoId(tenantId, eoIdList);
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        List<HmeEoRouterBomRel> hmeEoRouterBomRelList = new ArrayList<>();
        // 批量查询序列号
        List<String> relIdList = new ArrayList<>();
        List<String> relCidList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getRouterId())) {
            relIdList = customSequence.getNextKeys("hme_eo_router_bom_rel_s", mtNcRecordList.size());
            relCidList = customSequence.getNextKeys("hme_eo_router_bom_rel_cid_s", mtNcRecordList.size());
        }
        Integer relIndex = 0;
        Date curDate = CommonUtils.currentTimeGet();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            // 2021-01-07 add by sanfeng.zhang for wang.can 校验当前EO是否返修中
            // 2021-02-24 add by sanfeng.zhang for bai.lu 勾选了返修记录 不校验当前EO是否返修中
            if (!YES.equals(dto.getReworkRecordFlag())) {
                Optional<HmeBatchNcCheckVO> reworkFlagOpt = hmeBatchNcCheckVOList.stream().filter(vo -> StringUtils.equals(vo.getEoId(), mtNcRecord.getEoId())).findFirst();
                String reworkFlag = reworkFlagOpt.isPresent() ? reworkFlagOpt.get().getReworkFlag() : "";
                if (YES.equals(reworkFlag)) {
                    throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0049", "HME"));
                }
            }
            //如果勾选了返修，则调用API{materialLotAttrPropertyUpdate}更新条码的返修标识
            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(mtNcRecord.getMaterialLotId());
            List<MtCommonExtendVO5> attrList = new ArrayList<>();
            // 勾选返修记录 不更新条码返修标识
            if (!YES.equals(dto.getReworkRecordFlag())) {
                MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                mtCommonExtendVO5.setAttrName("REWORK_FLAG");
                mtCommonExtendVO5.setAttrValue(YES);
                attrList.add(mtCommonExtendVO5);
            }
            //2020/12/03 add by sanfeng.zhang for malifeng 增加处置组
            if (StringUtils.isNotBlank(dto.getRouterId())) {
                MtCommonExtendVO5 reworkAttr = new MtCommonExtendVO5();
                reworkAttr.setAttrName("REWORK_ROUTER");
                reworkAttr.setAttrValue(dto.getRouterId());
                attrList.add(reworkAttr);

                // 2021/1/6 add by sanfeng.zahng for xietianyang 增加指定返修标识
                MtCommonExtendVO5 flagAttr = new MtCommonExtendVO5();
                flagAttr.setAttrName("DESIGNED_REWORK_FLAG");
                flagAttr.setAttrValue(YES);
                attrList.add(flagAttr);
            }
            if (CollectionUtils.isNotEmpty(attrList)) {
                mtCommonExtendVO6.setAttrs(attrList);
                attrPropertyList.add(mtCommonExtendVO6);
            }
            // 2021/1/6 add by sanfeng.zhang for xietianyang 增加关联EO、工艺路线及装配清单关系hme_eo_router_bom_rel
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
            //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban 增加将条码与容器解绑的逻辑
            hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
        }
        //2020-08-25 add by chaonan.hu for lu.bai 如果勾选直接返修，则将找到的未出站数据出站
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
     * 工序报废-放行
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
        // materialLotBatchUpdate APi缺陷 无法做批量更新 会校验报错
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            //如果是工序不良放行，则调用API{materialLotUpdate}更新条码的质量状态
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(mtNcRecord.getMaterialLotId());
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setQualityStatus(OK);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
        }
    }

    /**
     * 工序报废-报废
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
            //如果是工序不良报废，则调用API{eoScrap}报废，并调用API{materialLotUpdate}条码更新
            MtEoActualVO6 mtEoActualVO6 = new MtEoActualVO6();
            mtEoActualVO6.setEoId(mtNcRecord.getEoId());
            mtEoActualVO6.setTrxScrappedQty(mtMaterialLot.getPrimaryUomQty());
            mtEoActualVO6.setParentEventId(eventId);
            mtEoActualRepository.eoScrap(tenantId, mtEoActualVO6);
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(mtNcRecord.getMaterialLotId());
            mtMaterialLotVO20.setEnableFlag(NO);
            mtMaterialLotVO20List.add(mtMaterialLotVO20);
            //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban 增加将条码与容器解绑的逻辑
            hmeNcDisposePlatformRepository.materialLotUnBindingContainer(tenantId, mtNcRecord.getMaterialLotId());
            //原业务为报废进站条码对应EO 修改：增加新EO报废功能 By 田欣 2021-11-15
            //判断新条码
            MtEo mtEo = mtEoRepository.selectByPrimaryKey(mtNcRecord.getEoId());
            boolean newMaterialLot = hmeEoJobSnCommonService.isNewMaterialLot(tenantId, mtEo.getIdentification());
            if (!newMaterialLot){
                List<String> eoIds = hmeEoJobSnReWorkMapper.queryReworkEoIdsByOldCode(tenantId,mtEo.getIdentification());
                //旧条码发起不良，将新EO进行报废
                eoIds.forEach(e->{
                    MtEoActualVO6 mtEoActualVO7 = new MtEoActualVO6();
                    mtEoActualVO7.setEoId(e);
                    mtEoActualVO7.setTrxScrappedQty(mtMaterialLot.getPrimaryUomQty());
                    mtEoActualVO7.setParentEventId(eventId);
                    mtEoActualRepository.eoScrap(tenantId, mtEoActualVO7);
                });
                //新条码失效 && 解绑
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
        //2020-09-08 21:00 add by chaonan.hu for lu.bai 将找到的未出站数据出站
        batchUpdateNoSiteOutData(tenantId, materialLotIdList, userId);
    }

    /**
     * 降级转型
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
        //2020-08-25 add by chaonan.hu for lu.bai 增加降级转型的逻辑
        if (StringUtils.isEmpty(dto.getTransitionMaterialId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "转型物料"));
        }
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList();
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            //条码上的原物料ID
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

            //EO处理
            MtEoVO mtEoVO = new MtEoVO();
            mtEoVO.setEventId(eventId);
            mtEoVO.setEoId(mtNcRecord.getEoId());
            mtEoVO.setMaterialId(dto.getTransitionMaterialId());
            mtEoRepository.eoUpdate(tenantId, mtEoVO, NO);
        }
        // 批量更新条码
        if (CollectionUtils.isNotEmpty(mtMaterialLotVO20List)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
    }

    /**
     * 材料不良-放行
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 16:15
     */
    private void materialRelease(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, String eventId, String eventRequestId) {
        // 批量获取不良记录扩展信息
        List<String> mtNcRecordIdList = mtNcRecordList.stream().map(MtNcRecord::getNcRecordId).distinct().collect(Collectors.toList());
        List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.selectByCondition(Condition.builder(HmeNcRecordAttr.class).andWhere(Sqls.custom()
                .andEqualTo(HmeNcRecordAttr.FIELD_TENANT_ID, tenantId)
                .andIn(HmeNcRecordAttr.FIELD_PARENT_RECORD_ID, mtNcRecordIdList)).build());
        // 序列号信息
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);

        //批量获取来源条码扩展字段
        MtExtendVO1 mtExtendVO = new MtExtendVO1();
        mtExtendVO.setTableName("mt_material_lot_attr");
        mtExtendVO.setKeyIdList(materialLotIdList);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
        // 材料料号及物料版本要一致
        this.verifyMaterialNcList(tenantId, mtNcRecordList, materialLotIdList, extendAttrVO1List, HmeConstants.ConstantValue.FX);
        // 不良记录扩展
        List<HmeNcRecordAttr> hmeNcRecordAttrList = new ArrayList<>();
        Map<String, List<HmeNcRecordAttr>> recordAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ncRecordAttrList)) {
            recordAttrMap = ncRecordAttrList.stream().collect(Collectors.groupingBy(HmeNcRecordAttr::getParentRecordId));
        }
        //获取LOV的值
        List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            //是否处理条码
            Boolean handleFlag;
            //获取不退料标识
            Optional<HmeNcRecordAttr> nrAttrOpt = ncRecordAttrList.stream().filter(nrAttr -> StringUtils.equals(nrAttr.getParentRecordId(), mtNcRecord.getNcRecordId())).findFirst();
            HmeNcRecordAttr ncRecordAttr = nrAttrOpt.isPresent() ? nrAttrOpt.get() : null;
            String returnMaterialFlag = ncRecordAttr != null ? ncRecordAttr.getAttribute2() : "";
            // 判断条码是否是芯片物料
            Boolean cosMaterialFlag = false;
            List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(mtNcRecord.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                List<String> collect = itemGroupList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                    cosMaterialFlag = true;
                }
            }

            if (StringUtils.equals(returnMaterialFlag, YES)) {
                //有不退料标识 不处理
                handleFlag = false;
            } else {
                //无不退料标识 更新条码 库存 及增加退库标识
                handleFlag = true;
            }

            if (handleFlag) {
                String nrCloseEventId = "";
                if (mtNcRecordList.size() == 1) {
                    // 创建事件
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
                    //输入条码
                    MtMaterialLot inputMaterialLot = null;
                    if (StringUtils.isNotBlank(dto.getBarcode())) {
                        inputMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getBarcode());
                    }
                    // 生成或更新输入条码
                    String barcodeId = this.handleInputBarcode(tenantId, inputMaterialLot, mtMaterialLot, mtNcRecord.getQty(), dto.getBarcode(), nrCloseEventId, HmeConstants.ConstantValue.FX);
                    // 更新条码扩展字段
                    List<MtCommonExtendVO6> mtCommonExtendVO6List = this.batchMaterialLotAttrUpdate(tenantId, dto.getBarcode(), barcodeId, inputMaterialLot, mtMaterialLot, extendAttrVO1List, nrCloseEventId, HmeConstants.ConstantValue.FX);
                    // 输入条码会多次更新 记同一个事件 事件历史保存会报错
                    if (CollectionUtils.isNotEmpty(mtCommonExtendVO6List)) {
                        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", nrCloseEventId,
                                mtCommonExtendVO6List);
                    }
                    // 更新不良记录扩展字段 将条码id写入扩展字段Attribute3
                    if (CollectionUtils.isNotEmpty(ncRecordAttrs)) {
                        HmeNcRecordAttr hmeNcRecordAttr = ncRecordAttrs.get(0);
                        hmeNcRecordAttr.setAttribute3(barcodeId);
                        hmeNcRecordAttrList.add(hmeNcRecordAttr);
                    }
                } else {
                    // 判断是否投料
                    Boolean feelFlag = judgeFeedByMaterialLot(tenantId, mtNcRecord.getEoId(), mtNcRecord.getMaterialLotId());
                    if (!feelFlag) {
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        mtMaterialLotVO20.setTrxPrimaryUomQty(mtNcRecord.getQty());
                        mtMaterialLotVO20List.add(mtMaterialLotVO20);
                    }
                }

                //调用API{onhandQtyUpdateProcess}进行库存恢复
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
                // 取产线下的不良虚拟库位 多个或找不到则报错
                String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                // 调用API{onhandQtyUpdateProcess}扣减虚拟仓库存
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
                // 芯片物料时 删除不良位置信息
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
            // 组装数据 同一条码的累加数量
            List<MtMaterialLotVO20> mtMaterialLotVO20s = new ArrayList<>();
            Map<String, List<MtMaterialLotVO20>> materialLotMap = mtMaterialLotVO20List.stream().collect(Collectors.groupingBy(MtMaterialLotVO20::getMaterialLotId));
            for (Map.Entry<String, List<MtMaterialLotVO20>> materialLotListEntry : materialLotMap.entrySet()) {
                List<MtMaterialLotVO20> value = materialLotListEntry.getValue();
                // 总转移数量
                double totalQty = value.stream().map(MtMaterialLotVO20::getTrxPrimaryUomQty).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(value.get(0).getMaterialLotId());
                mtMaterialLotVO20.setTrxPrimaryUomQty(totalQty);
                mtMaterialLotVO20s.add(mtMaterialLotVO20);
            }
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20s, eventId, NO);
        }
        // 保存扩展字段
        if (CollectionUtils.isNotEmpty(hmeNcRecordAttrList)) {
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmeNcRecordAttrMapper.myBatchUpdate(tenantId, userId, hmeNcRecordAttrList);
        }
    }

    /**
     * 材料不良- 报废
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
        // 报废不支持批量 只会有一个
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecord.getMaterialLotId());
            //如果是材料报废
            //2020-09-09 11:10 add by chaonan.hu for lu.bai 增加原材料报废的逻辑
            materialScrap(tenantId, mtMaterialLot, mtNcRecord, eventId, eventRequestId);
        }
    }

    /**
     * 材料不良-退库
     *
     * @param tenantId
     * @param mtNcRecordList
     * @param dto
     * @param eventId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 16:20
     */
    private void materialBackLibrary(Long tenantId, List<MtNcRecord> mtNcRecordList, HmeNcCheckDTO4 dto, String eventId, String eventRequestId) {
        // 条码信息
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        //批量获取来源条码扩展字段
        MtExtendVO1 mtExtendVO = new MtExtendVO1();
        mtExtendVO.setTableName("mt_material_lot_attr");
        mtExtendVO.setKeyIdList(materialLotIdList);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
        // 校验料号、物料版本及供应商批次
        this.verifyMaterialNcList(tenantId, mtNcRecordList, materialLotIdList, extendAttrVO1List, HmeConstants.ConstantValue.TK);
        // 批量获取不良记录扩展信息
        List<String> mtNcRecordIdList = mtNcRecordList.stream().map(MtNcRecord::getNcRecordId).distinct().collect(Collectors.toList());
        List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.selectByCondition(Condition.builder(HmeNcRecordAttr.class).andWhere(Sqls.custom()
                .andEqualTo(HmeNcRecordAttr.FIELD_TENANT_ID, tenantId)
                .andIn(HmeNcRecordAttr.FIELD_PARENT_RECORD_ID, mtNcRecordIdList)).build());
        Map<String, List<HmeNcRecordAttr>> recordAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ncRecordAttrList)) {
            recordAttrMap = ncRecordAttrList.stream().collect(Collectors.groupingBy(HmeNcRecordAttr::getParentRecordId));
        }

        // 不良记录扩展
        List<HmeNcRecordAttr> hmeNcRecordAttrList = new ArrayList<>();
        //获取LOV的值
        List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            // 判断条码是否是芯片物料
            Boolean cosMaterialFlag = false;
            List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(mtNcRecord.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                List<String> collect = itemGroupList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                    cosMaterialFlag = true;
                }
            }
            // 芯片物料不支持退库
            if (cosMaterialFlag) {
                throw new MtException("HME_NC_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0075", "HME"));

            }
            //是否处理条码
            Boolean handleFlag;
            //获取不退料标识
            List<HmeNcRecordAttr> hmeNcRecordAttrs = recordAttrMap.get(mtNcRecord.getNcRecordId());
            String returnMaterialFlag = CollectionUtils.isNotEmpty(hmeNcRecordAttrs) ? hmeNcRecordAttrs.get(0).getAttribute2() : "";
            //有不退料标识 不处理
            //未投料 更新条码 库存 及增加退库标识
            if (StringUtils.equals(returnMaterialFlag, YES)) {
                //有不退料标识 不处理
                handleFlag = false;
            } else {
                //无不退料标识 更新条码 库存 及增加退库标识
                handleFlag = true;
            }

            if (handleFlag) {
                String nrCloseEventId = "";
                if (mtNcRecordList.size() == 1) {
                    // 创建事件
                    nrCloseEventId = eventId;
                } else {
                    MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
                    mtEventCreateVO.setEventTypeCode("NC_RECORD_CLOSE");
                    mtEventCreateVO.setEventRequestId(eventRequestId);
                    nrCloseEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
                }
                //来源条码
                Optional<MtMaterialLot> mtMaterialLotOpt = materialLotList.stream().filter(barcode -> StringUtils.equals(barcode.getMaterialLotId(), mtNcRecord.getMaterialLotId())).findFirst();
                if (!mtMaterialLotOpt.isPresent()) {
                    MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(mtNcRecord.getNcIncidentId());
                    throw new MtException("HME_NC_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0069", "HME", mtNcIncident != null ? mtNcIncident.getIncidentNumber() : ""));
                }
                MtMaterialLot sourceMaterialLot = mtMaterialLotOpt.get();
                //输入条码
                MtMaterialLot inputMaterialLot = null;
                if (StringUtils.isNotBlank(dto.getBarcode())) {
                    inputMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getBarcode());
                }
                // 生成或更新输入条码
                String barcodeId = this.handleInputBarcode(tenantId, inputMaterialLot, sourceMaterialLot, mtNcRecord.getQty(), dto.getBarcode(), nrCloseEventId, HmeConstants.ConstantValue.TK);
                // 更新条码扩展字段
                List<MtCommonExtendVO6> mtCommonExtendVO6List = this.batchMaterialLotAttrUpdate(tenantId, dto.getBarcode(), barcodeId, inputMaterialLot, sourceMaterialLot, extendAttrVO1List, nrCloseEventId, HmeConstants.ConstantValue.TK);
                // 输入条码会多次更新 记同一个事件 事件历史保存会报错
                if (CollectionUtils.isNotEmpty(mtCommonExtendVO6List)) {
                    mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", nrCloseEventId,
                            mtCommonExtendVO6List);
                }
                // 更新不良记录扩展字段 将条码id写入扩展字段Attribute3
                List<HmeNcRecordAttr> ncRecordAttrs = recordAttrMap.get(mtNcRecord.getNcRecordId());
                if (CollectionUtils.isNotEmpty(ncRecordAttrs)) {
                    HmeNcRecordAttr hmeNcRecordAttr = ncRecordAttrs.get(0);
                    hmeNcRecordAttr.setAttribute3(barcodeId);
                    hmeNcRecordAttrList.add(hmeNcRecordAttr);
                }

                //创建退库事件
                MtEventCreateVO mtEventCreateVO2 = new MtEventCreateVO();
                mtEventCreateVO2.setEventTypeCode("NC_RECORD_MATERIAL_TK");
                mtEventCreateVO2.setEventRequestId(eventRequestId);
                String eventId2 = mtEventRepository.eventCreate(tenantId, mtEventCreateVO2);
                //更新库存量
                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO9.setSiteId(sourceMaterialLot.getSiteId());
                mtInvOnhandQuantityVO9.setMaterialId(sourceMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO9.setLocatorId(sourceMaterialLot.getLocatorId());
                mtInvOnhandQuantityVO9.setLotCode(sourceMaterialLot.getLot());
                mtInvOnhandQuantityVO9.setChangeQuantity(mtNcRecord.getQty());
                mtInvOnhandQuantityVO9.setEventId(eventId2);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                // 取产线下的不良虚拟库位 多个或找不到则报错
                String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                // 调用API{onhandQtyUpdateProcess}扣减虚拟仓库存
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
        // 保存扩展字段
        if (CollectionUtils.isNotEmpty(hmeNcRecordAttrList)) {
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmeNcRecordAttrMapper.myBatchUpdate(tenantId, userId, hmeNcRecordAttrList);
        }
    }

    /**
     * 根据工位找产线 在找产线下的不良虚拟货位
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
        // 发起工段、材料料号、物料版本及供应商批次要一致
        List<String> workcellIdList = mtNcRecordList.stream().map(MtNcRecord::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        //用户默认站点
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
            // 物料版本
            List<MtExtendAttrVO1> mvList = attrMap.get(materialLotId + "_" + "MATERIAL_VERSION");
            if (CollectionUtils.isNotEmpty(mvList)) {
                mvStrList.add(mvList.get(0).getAttrValue());
            } else {
                mvStrList.add("");
            }
            // 退库 验证供应商批次
            if (StringUtils.equals(flag, HmeConstants.ConstantValue.TK)) {
                // 供应商批次
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
     * 材料不良-自制件返修
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
        // 查询条码
        List<String> materialLotIdList = mtNcRecordList.stream().map(MtNcRecord::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        // 查询自制件
        List<String> componentMaterialIdList = mtNcRecordList.stream().map(MtNcRecord::getComponentMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<MtMaterial> mtMaterialList = mtMaterialRepository.selectByCondition(Condition.builder(MtMaterial.class).andWhere(Sqls.custom()
                .andEqualTo(MtMaterial.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtMaterial.FIELD_MATERIAL_ID, componentMaterialIdList)).build());
        List<String> materialLotCodeList = materialLotList.stream().map(MtMaterialLot::getMaterialLotCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 查询工单创建信息
        List<HmeRepairWorkOrderCreate> workOrderCreateList = hmeRepairWorkOrderCreateRepository.selectByCondition(Condition.builder(HmeRepairWorkOrderCreate.class).andWhere(Sqls.custom()
                .andEqualTo(HmeRepairWorkOrderCreate.FIELD_TENANT_ID, tenantId)
                .andIn(HmeRepairWorkOrderCreate.FIELD_MATERIAL_LOT_CODE, materialLotCodeList)).build());
        List<String> workOrderNumList = workOrderCreateList.stream().map(HmeRepairWorkOrderCreate::getWorkOrderNum).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 工单
        List<MtWorkOrder> mtWorkOrderList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workOrderNumList)) {
            mtWorkOrderList = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class).andWhere(Sqls.custom()
                    .andEqualTo(MtWorkOrder.FIELD_TENANT_ID, tenantId)
                    .andIn(MtWorkOrder.FIELD_WORK_ORDER_NUM, workOrderNumList)).build());
        }
        // 返修工单物料版本值集
        List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.WO_PRODUCTION_VERSION", tenantId);
        //获取LOV的值
        List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtCommonExtendVO7> mtCommonExtendVO7List = new ArrayList<>();
        for (MtNcRecord mtNcRecord : mtNcRecordList) {
            // 判断条码是否是芯片物料
            Boolean cosMaterialFlag = false;
            List<MtMaterialBasic> mtMaterialBasicList = queryMaterialBasicList(mtNcRecord.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasicList) && StringUtils.isNotEmpty(mtMaterialBasicList.get(0).getItemGroup())) {
                List<String> collect = itemGroupList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (collect.contains(mtMaterialBasicList.get(0).getItemGroup())) {
                    cosMaterialFlag = true;
                }
            }
            // 芯片物料不支持退库
            if (cosMaterialFlag) {
                throw new MtException("HME_NC_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0075", "HME"));

            }
            Optional<MtMaterial> materialOpt = mtMaterialList.stream().filter(mm -> StringUtils.equals(mm.getMaterialId(), mtNcRecord.getComponentMaterialId())).findFirst();
            MtMaterial mtMaterial = materialOpt.isPresent() ? materialOpt.get() : null;
            // 申请数量不为1 则报错
            if (mtNcRecord.getQty().compareTo(1D) != 0) {
                throw new MtException("HME_NC_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0051", "HME", mtMaterial != null ? mtMaterial.getMaterialName() : ""));
            }
            // 校验当前条码对应工单状态
            Optional<MtMaterialLot> materialLotOpt = materialLotList.stream().filter(mml -> StringUtils.equals(mtNcRecord.getMaterialLotId(), mml.getMaterialLotId())).findFirst();
            MtMaterialLot mtMaterialLot = materialLotOpt.isPresent() ? materialLotOpt.get() : null;
            if (mtMaterialLot != null) {
                List<HmeRepairWorkOrderCreate> wocList = workOrderCreateList.stream().filter(woc -> StringUtils.equals(woc.getMaterialLotCode(), mtMaterialLot.getMaterialLotCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(wocList)) {
                    // 继续判断 对应的工单状态 为NEW/RELEASED/EORELEASED 则报错
                    List<MtWorkOrder> woList = mtWorkOrderList.stream().filter(wo -> StringUtils.equals(wo.getWorkOrderNum(), wocList.get(0).getWorkOrderNum())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(woList)) {
                        MtWorkOrder mtWorkOrder = woList.get(0);
                        if (HmeConstants.WorkOrderStatus.NEW.equals(mtWorkOrder.getStatus()) || HmeConstants.WorkOrderStatus.RELEASED.equals(mtWorkOrder.getStatus()) || HmeConstants.WorkOrderStatus.EORELEASED.equals(mtWorkOrder.getStatus())) {
                            throw new MtException("HME_NC_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_NC_0052", "HME", mtMaterialLot.getMaterialLotCode()));
                        }
                    }
                }
                // 条码更新：调用API{materialLotUpdate}
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotVO20.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                mtMaterialLotVO20.setSiteId(mtMaterialLot.getSiteId());
                mtMaterialLotVO20.setEnableFlag(YES);
                mtMaterialLotVO20.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO20.setTrxPrimaryUomQty(mtNcRecord.getQty());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);

                // 条码返修标识置为Y
                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(mtMaterialLot.getMaterialLotId());
                List<MtCommonExtendVO4> attrList = new ArrayList<>();
                MtCommonExtendVO4 extendVO4 = new MtCommonExtendVO4();
                extendVO4.setAttrName("REWORK_FLAG");
                extendVO4.setAttrValue(YES);
                attrList.add(extendVO4);
                mtCommonExtendVO7.setAttrs(attrList);

                // 调用SAP生成返修工单接口，自动生成返修工单
                HmeRepairWorkOrderCreate workOrderCreate = new HmeRepairWorkOrderCreate();
                workOrderCreate.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                workOrderCreate.setQty(mtNcRecord.getQty());
                if (mtMaterial != null) {
                    workOrderCreate.setMaterialCode(mtMaterial.getMaterialCode());
                    // 物料单位
                    MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                    workOrderCreate.setPrimaryUomCode(mtUom != null ? mtUom.getUomCode() : "");
                }
                // 站点
                MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(mtNcRecord.getSiteId());
                workOrderCreate.setSiteCode(mtModSite != null ? mtModSite.getSiteCode() : "");
                // 获取仓库 (先根据工位找到其工段在mt_mod_organization_rel对应的货位)
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
                // 返修工单物料版本从值集获取
                if (CollectionUtils.isNotEmpty(valueDTOList)) {
                    workOrderCreate.setProductionVersion(valueDTOList.get(0).getValue());
                }
                HmeRepairWorkOrderCreate resultData = itfRepairWorkOrderCreateService.hmeRepairWorkOrderCreateService(tenantId, workOrderCreate);
                // 接口返回状态为空, 则为返修工单实时接口未启用,报错提示
                if (StringUtils.isBlank(resultData.getStatus())) {
                    throw new MtException("HME_NC_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0053", "HME"));
                }
                // 接口返回状态E, 创建返修工单异常 返回报错信息
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

                // 取产线的不良虚拟库位 多个或找不到则报错
                String locatorId = this.queryNcStorageLocatorByProdLine(tenantId, mtNcRecord.getWorkcellId());
                // 调用API{onhandQtyUpdateProcess}扣减虚拟仓库存
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

                // 创建材料不良自制件返修事件 增加库存
                String recordEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO(){{
                    setEventRequestId(eventRequestId);
                    setEventTypeCode("NC_RECORD_SELFMADE_MATERIAL_REPAIR");
                }});
                // 调用更新现有量API：onhandQtyUpdateProcess
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
     * 批量更新不良记录、不良关闭及事故关闭
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
        // 查询出不良类型组发生改变的数据，更新父不良记录并记录历史
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
                // 记录历史
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
            // 批量保存不良记录
            if (CollectionUtils.isNotEmpty(updateNcRecordList)) {
                hmeNcCheckMapper.batchUpdateNcCodeId(tenantId, userId, updateNcRecordList);
            }
            // 批量保存不良记录历史
            if (CollectionUtils.isNotEmpty(mtNcRecordHisList)) {
                mtNcRecordHisRepository.batchInsertSelective(mtNcRecordHisList);
            }
        }
        // 将处理方法记录到不良记录扩展属性表中
        List<HmeNcRecordAttr> recordAttrList = hmeNcRecordAttrRepository.selectByCondition(Condition.builder(HmeNcRecordAttr.class).andWhere(Sqls.custom()
                .andEqualTo(HmeNcRecordAttr.FIELD_TENANT_ID, tenantId)
                .andIn(HmeNcRecordAttr.FIELD_PARENT_RECORD_ID, ncRecordIdList)).build());
        Map<String, List<HmeNcRecordAttr>> recordAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(recordAttrList)) {
            recordAttrMap = recordAttrList.stream().collect(Collectors.groupingBy(HmeNcRecordAttr::getParentRecordId));
        }
        //查询原子不良记录Id和不良代码
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
        // 差值即为要新增的记录条数
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
                //不为空 则更新
                HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrList.get(0);
                hmeNcRecordAttr.setProcessMethod(dto.getProcessMethod());
                if ("4".equals(dto.getProcessMethod())) {
                    hmeNcRecordAttr.setAttribute1(dto.getTransitionMaterialId());
                }
                if (StringUtils.isNotBlank(dto.getDispositionFunctionId())) {
                    hmeNcRecordAttr.setAttribute4(dto.getDispositionFunctionId());
                }
                // 勾选了返修记录 则Attribute6记Y
                if (YES.equals(dto.getReworkRecordFlag())) {
                    hmeNcRecordAttr.setAttribute6(YES);
                }
                updateNcRecordAttrList.add(hmeNcRecordAttr);
            } else {
                //没有则新增
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
                // 勾选了返修记录 则Attribute6记Y
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

            //调用API{ncRecordAndIncidentClose}进行父不良记录关闭、事故关闭。
            MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
            mtNcRecordVO2.setNcRecordId(nr.getNcRecordId());
            mtNcRecordVO2.setEventRequestId(eventRequestId);
            mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);

            //为了万一存在新建的情况，提前设置属性
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
            // 查询子不良记录
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
        // 更新不良记录扩展表
        if (CollectionUtils.isNotEmpty(updateNcRecordAttrList)) {
            hmeNcRecordAttrMapper.myBatchUpdate(tenantId, userId, updateNcRecordAttrList);
        }
        // 新增不良记录扩展表
        if (CollectionUtils.isNotEmpty(insertNcRecordAttrList)) {
            hmeNcRecordAttrRepository.batchInsertSelective(insertNcRecordAttrList);
        }
        //子不良记录取消或生成或更新
        for (MtNcRecord subNcRecord : subNcRecordList) {
            if (dto.getNcCodeIdList().contains(subNcRecord.getNcCodeId())) {
                //如果原子不良记录的不良代码存在新子不良代码中，则更新
                if (StringUtils.isNotEmpty(dto.getComment())) {
                    MtNcRecordVO7 mtNcRecordVO7 = new MtNcRecordVO7();
                    mtNcRecordVO7.setNcRecordId(subNcRecord.getNcRecordId());
                    mtNcRecordVO7.setComments(dto.getComment());
                    mtNcRecordVO7.setEventId(eventId);
                    mtNcRecordRepository.ncRecordAndHisUpdate(tenantId, mtNcRecordVO7);
                }
                //调用API{ncRecordAndIncidentClose}进行子不良记录关闭、事故关闭。
                MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                mtNcRecordVO2.setNcRecordId(subNcRecord.getNcRecordId());
                mtNcRecordVO2.setEventRequestId(eventRequestId);
                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
            } else {
                //如果原子不良记录的不良代码不存在新子不良代码中，则取消
                MtNcRecordVO2 mtNcRecordVO21 = new MtNcRecordVO2();
                mtNcRecordVO21.setNcRecordId(subNcRecord.getNcRecordId());
                mtNcRecordVO21.setParentEventId(eventId);
                mtNcRecordVO21.setEventRequestId(eventRequestId);
                mtNcRecordRepository.ncRecordCancel(tenantId, mtNcRecordVO21);
            }
        }
        if (CollectionUtils.isNotEmpty(mtNcRecordVO3List)) {
            for (MtNcRecordVO3 mtNcRecordVO3 : mtNcRecordVO3List) {
                //如果新子不良代码不存在原子不良记录中，则新建
                MtNcRecordVO8 mtNcRecordVO8 = mtNcRecordRepository.ncRecordAndHisCreate(tenantId, mtNcRecordVO3);
                //调用API{ncRecordAndIncidentClose}进行子不良记录关闭、事故关闭。
                MtNcRecordVO2 ncRecordVO2 = new MtNcRecordVO2();
                ncRecordVO2.setEventRequestId(eventRequestId);
                ncRecordVO2.setNcRecordId(mtNcRecordVO8.getNcRecordId());
                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, ncRecordVO2);
            }
        }
    }

    private String handleInputBarcode(Long tenantId, MtMaterialLot inputMaterialLot, MtMaterialLot sourceMaterialLot, Double ncQty, String barcode, String eventId, String flag) {
        //调用API{materialLotUpdate}更新条码  分条码有效无效 不存在则新增
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        if (StringUtils.isBlank(barcode)) {
            // 创建物料批信息
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
                //无效条码 条码数置0
                inputMaterialLot.setPrimaryUomQty(0D);
                mtMaterialLotMapper.updateByPrimaryKeySelective(inputMaterialLot);
                //校验条码
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
                //校验条码进站
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
        //保存扩展字段
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

            //退库 保存料废标识
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
        //更新扩展字段
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
     * 绑定Eo、工艺路线及装配清单关系
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