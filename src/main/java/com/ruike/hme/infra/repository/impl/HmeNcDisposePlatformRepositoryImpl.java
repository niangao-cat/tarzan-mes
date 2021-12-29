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
        //序列号校验
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
        //先根据工序找工段，再根据工段查询班次
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

        //根据工位查询不良代码记录
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
//            //对查询出来的记录关联查询
//            HmeNcDisposePlatformDTO2 hmeNcDisposePlatformDTO2 = new HmeNcDisposePlatformDTO2();
//            hmeNcDisposePlatformDTO2.setNcRecordId(mtNcRecordDb.getNcRecordId());
//            //车间 根据ROOT_CAUSE_WORKCELL_ID调用API{parentOrganizationRelQuery}
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
//            //生产线
////            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
////            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
////            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
////                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
////                hmeNcDisposePlatformDTO2.setProdLineName(mtModProductionLine.getDescription());
////            }
//            //工段和工序
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
////            //工位 根据ROOT_CAUSE_WORKCELL_ID、WORKCELL_TYPE = STATION关联 mt_mod_workcell获取WORKCELL_NAME
////            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtNcRecordDb.getWorkcellId());
////            hmeNcDisposePlatformDTO2.setWorkcellName(mtModWorkcell.getWorkcellName());
//            //操作者
//            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, mtNcRecordDb.getCreatedBy());
//            hmeNcDisposePlatformDTO2.setUserName(mtUserInfo.getRealName());
//            //工号
//            hmeNcDisposePlatformDTO2.setUserCode(mtUserInfo.getLoginName());
//            //产品料号 MATERIAL_ID关联mt_material获取material_code
//            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtNcRecordDb.getMaterialId());
//            hmeNcDisposePlatformDTO2.setMaterialCode(mtMaterial.getMaterialCode());
//            //产品描述
//            hmeNcDisposePlatformDTO2.setMaterialName(mtMaterial.getMaterialName());
//            //序列号 MATERIAL_LOT_ID关联mt_material_lot显示material_lot_code，没关联到显示material_Lot_id
//            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecordDb.getMaterialLotId());
//            if (mtMaterialLot != null && StringUtils.isNotBlank(mtMaterialLot.getMaterialLotCode())) {
//                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
//            } else {
//                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtNcRecordDb.getMaterialLotId());
//            }
//            //工单号 根据eo_id关联表mt_eo，找到work_order_id，根据work_order_id在表mt_work_order找到WORK_ORDER_NUM
//            MtEo mtEo = mtEoRepository.selectByPrimaryKey(mtNcRecordDb.getEoId());
//            if (mtEo != null && StringUtils.isNotBlank(mtEo.getWorkOrderId())) {
//                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
//                if (mtWorkOrder != null) {
//                    hmeNcDisposePlatformDTO2.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
//                }
//            }
//            //不良代码 不良原因
//            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, hmeNcDisposePlatformDTO2.getNcRecordId());
//            List<String> ncCodeIdList = new ArrayList<>();
//            List<String> ncCodeList = new ArrayList<>();
//            List<String> ncReasonList = new ArrayList<>();
//            for (MtNcRecord mtNcRecord : mtNcRecordList) {
//                if (StringUtils.isNotEmpty(mtNcRecord.getNcCodeId())) {
//                    //根据NC_CODE_ID关联表mt_nc_code查询nc_code
//                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
//                    ncCodeIdList.add(mtNcCode.getNcCodeId());
//                    ncCodeList.add(mtNcCode.getNcCode());
//                    ncReasonList.add(mtNcCode.getDescription());
//                }
//            }
//            hmeNcDisposePlatformDTO2.setNcCodeIdList(ncCodeIdList);
//            hmeNcDisposePlatformDTO2.setNcCodeList(ncCodeList);
//            hmeNcDisposePlatformDTO2.setNcReasonList(ncReasonList);
//            //不良组描述  不良类型
//            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecordDb.getNcCodeId());
//            if (mtNcGroup != null) {
//                hmeNcDisposePlatformDTO2.setNcGroupDesc(mtNcGroup.getDescription());
//                hmeNcDisposePlatformDTO2.setNcType(mtNcGroup.getComponentRequired());
//                String meaning = lovAdapter.queryLovMeaning("HME.NC_TYPE", tenantId, mtNcGroup.getComponentRequired());
//                hmeNcDisposePlatformDTO2.setNcTypeMeaning(meaning);
//            }
//            //不良单号
//            MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(mtNcRecordDb.getNcIncidentId());
//            hmeNcDisposePlatformDTO2.setNcNumber(mtNcIncident.getIncidentNumber());
//            //发生时间
//            hmeNcDisposePlatformDTO2.setDateTime(mtNcRecordDb.getDateTime());
//            //责任工位
//            if (StringUtils.isNotEmpty(mtNcRecordDb.getRootCauseWorkcellId())) {
//                MtModWorkcell mtModWorkcell2 = mtModWorkcellRepository.selectByPrimaryKey(mtNcRecordDb.getRootCauseWorkcellId());
//                hmeNcDisposePlatformDTO2.setResponseWorkcellName(mtModWorkcell2.getWorkcellName());
//            }
//            //责任人
//            MtUserInfo mtUserInfo2 = mtUserRepository.userPropertyGet(tenantId, mtNcRecordDb.getUserId());
//            hmeNcDisposePlatformDTO2.setResponseUser(mtUserInfo2.getRealName());
//            //状态
//            hmeNcDisposePlatformDTO2.setStatus(mtNcRecordDb.getNcStatus());
//            MtGenStatus statusMeaning = mtGenStatusRepository.getGenStatus(tenantId, "NC_RECORD", "NC_RECORD_STATUS", mtNcRecordDb.getNcStatus());
//            hmeNcDisposePlatformDTO2.setStatusMeaning(statusMeaning.getDescription());
//            //处理办法
//            HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrRepository.selectOne(new HmeNcRecordAttr() {{
//                setTenantId(tenantId);
//                setParentRecordId(mtNcRecordDb.getNcRecordId());
//            }});
//            if (hmeNcRecordAttr != null) {
//                hmeNcDisposePlatformDTO2.setDisposeMethod(hmeNcRecordAttr.getProcessMethod());
//                String disposeMethodMeaning = lovAdapter.queryLovMeaning("HME.NC_PROCESS_METHOD", tenantId, hmeNcRecordAttr.getProcessMethod());
//                hmeNcDisposePlatformDTO2.setDisposeMethodMeaning(disposeMethodMeaning);
//            }
//            //2020-09-28 add by chaonan.hu for lu.bai 增加报废物料、报废数量显示
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

        //根据工位查询工序不良类型组
        HmeNcDisposePlatformDTO9 processNcCodeTypes = this.getProcessNcCodeTypes(tenantId, dto.getProcessId(), NO);
        hmeNcDisposePlatformDTO7.setProcessNcCodeTypes(processNcCodeTypes);
        //根据工位查询材料不良类型组
        HmeNcDisposePlatformDTO9 materialNcCodeTypes = this.getProcessNcCodeTypes(tenantId, dto.getProcessId(), YES);
        hmeNcDisposePlatformDTO7.setMaterialNcCodeTypes(materialNcCodeTypes);
        //查询材料清单
        //2020-09-27 edit by chaonan.hu for lu.bai 查询逻辑大改
        //Page<HmeNcDisposePlatformDTO23> hmeNcDisposePlatformDTO13s = this.materialDataQuery(tenantId, siteId, dto.getWorkcellId(), dto.getMaterialLotCode(), pageRequest);
        //hmeNcDisposePlatformDTO7.setMaterialData(hmeNcDisposePlatformDTO13s);
        //2020-11-13 edit by chaonan.hu for can.wang 查询逻辑迭代
        List<HmeNcDisposePlatformVO4> hmeNcDisposePlatformVO4s = this.materialDataQuery2(tenantId, mtMaterialLot1, dto.getProcessId(), dto.getWorkcellId());
        hmeNcDisposePlatformDTO7.setMaterialData(hmeNcDisposePlatformVO4s);
        return hmeNcDisposePlatformDTO7;
    }

    @Override
    public Page<HmeNcDisposePlatformDTO3> processLov(Long tenantId, HmeNcDisposePlatformDTO4 dto, PageRequest pageRequest) {
        List<HmeNcDisposePlatformDTO3> hmeNcDisposePlatformDTO3List = new ArrayList<>();
        //根据所选产线查询工段
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setParentOrganizationId(dto.getProdLineId());
            setParentOrganizationType("PROD_LINE");
            setOrganizationType("WORKCELL");
        }});
        //根据工段查询工序
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
                    //模糊查询数据
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
        //封装分页数据
        List<HmeNcDisposePlatformDTO3> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmeNcDisposePlatformDTO3List);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), hmeNcDisposePlatformDTO3List.size());
    }

    @Override
    public Page<HmeNcDisposePlatformDTO6> workcellLov(Long tenantId, HmeNcDisposePlatformDTO5 dto, PageRequest pageRequest) {
        List<HmeNcDisposePlatformDTO6> hmeNcDisposePlatformDTO6List = new ArrayList<>();
        //根据工序查询工位
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
                //模糊查询
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
        //封装分页数据
        List<HmeNcDisposePlatformDTO6> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmeNcDisposePlatformDTO6List);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), hmeNcDisposePlatformDTO6List.size());
    }

    @Override
    public HmeNcDisposePlatformDTO9 getProcessNcCodeTypes(Long tenantId, String workcellId, String componentRequired) {
        HmeNcDisposePlatformDTO9 hmeNcDisposePlatformDto9 = new HmeNcDisposePlatformDTO9();
        List<HmeNcDisposePlatformDTO8> resultList = new ArrayList<>();
        //根据工位查询工艺Id
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
        //根据序列号在表mt_material_lot查询eoid、siteId
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        //返修历史杂收产品没有对应生产订单和EO信息，记录返修新EO By 田欣 2021-11-15
        if(StringUtils.isEmpty(mtMaterialLot.getEoId())){
            List<String> eoIds = hmeEoJobSnReWorkMapper.queryReworkEoIdsByOldCode(tenantId,dto.getMaterialLotCode());
            if (CollectionUtils.isNotEmpty(eoIds)){
                mtMaterialLot.setEoId(eoIds.get(0));
            }
        }

        //2020-09-04 edit by chaonan.hu for lu.bai 增加直接报废的逻辑 对代码先进行初步整理
        //事件创建
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("NC_RECORD_CREATE");
        }});
        //事故创建
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, new MtNumrangeVO2() {{
            setObjectCode("NC_INCIDENT_NUM");
            setSiteId(mtMaterialLot.getSiteId());
        }});
        MtNcIncidentVO3 mtNcIncidentVO3 = mtNcIncidentRepository.ncIncidentAndHisCreate(tenantId, new MtNcIncidentVO1() {{
            setSiteId(mtMaterialLot.getSiteId());
            setIncidentNumber(mtNumrangeVO5.getNumber());
            setEventId(eventId);
        }});
        //父不良记录生成
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
        //判断是否是当前库位
        if (dto.getCurrentwWorkcellId().equals(dto.getWorkcellId())) {
            //如果是，根据material_lot_Id在表hme_eo_job_sn找到其最近一条数据（site_in_date）的eo_step_id和operation_id，如果没有找到，则为空
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
            //如果不是，则取前台传入的数据
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
        //记录历史
        MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
        BeanUtils.copyProperties(mtNcRecord, mtNcRecordHis);
        mtNcRecordHis.setEventId(eventId);
        mtNcRecordHisRepository.insertSelective(mtNcRecordHis);
        //不良记录与附件之间的关系
        if (StringUtils.isNotBlank(dto.getUuid())) {
            HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel = new HmeNcRecordAttachmentRel();
            hmeNcRecordAttachmentRel.setTenantId(tenantId);
            hmeNcRecordAttachmentRel.setNcRecordId(mtNcRecord.getNcRecordId());
            hmeNcRecordAttachmentRel.setAttachmentId(dto.getUuid());
            hmeNcRecordAttachmentRelRepository.insertSelective(hmeNcRecordAttachmentRel);
        }
        //子不良记录生成
        if (CollectionUtils.isNotEmpty(dto.getNcCodeIdList())) {
            //子不良记录生成
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
                    //如果是重新返修或直接报废，调用API{ncRecordAndIncidentClose}进行子不良记录关闭、事故关闭。
                    MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                    String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CLOSE");
                    mtNcRecordVO2.setEventRequestId(eventRequestId);
                    mtNcRecordVO2.setNcRecordId(mtNcRecordVO8.getNcRecordId());
                    mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
                }
            }
        }
        // 20210525 add by sanfeng.zhang for peng.zhao 记录发起返修的SN的job_id
        HmeNcRecordAttr hmeNcRecordAttr = new HmeNcRecordAttr();
        hmeNcRecordAttr.setTenantId(tenantId);
        hmeNcRecordAttr.setParentRecordId(mtNcRecord.getNcRecordId());
        // 根据当前eoId及工位 取最新的的job_id
        String lastJobId = hmeNcDisposePlatformMapper.queryLatestJobIdByEoAndWorkcell(tenantId, mtNcRecord.getEoId(), mtNcRecord.getWorkcellId());
        hmeNcRecordAttr.setAttribute8(lastJobId);
        if (HmeConstants.ProcessStatus.THREE.equals(dto.getFlag()) || HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
            //如果是重新返修或直接报废，则调用API{ncRecordAndIncidentClose}进行父不良记录关闭、事故关闭。
            MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
            mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "NC_RECORD_CLOSE");
            mtNcRecordVO2.setEventRequestId(eventRequestId);
            mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
            //2020-09-07 add by chaonan.hu for lu.bai
            //将处理方法记录到不良记录扩展属性表中
            //这里的值没有传错，不要怀疑
            if (HmeConstants.ProcessStatus.THREE.equals(dto.getFlag())) {
                hmeNcRecordAttr.setProcessMethod("1");
                // 2021-02-07 add by sanfeng.zhang for can.wang 直接返修 勾选返修记录 attribute6置为Y
                if (YES.equals(dto.getReworkRecordFlag())) {
                    hmeNcRecordAttr.setAttribute6(YES);
                }
            } else if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                hmeNcRecordAttr.setProcessMethod("3");
            }
            //2020-09-17 20:49 add by chaonan.hu for zhenyong.ban 增加将条码与容器解绑的逻辑
            materialLotUnBindingContainer(tenantId, mtMaterialLot.getMaterialLotId());
        }
        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
            //2020-09-09 add by chaonan.hu for lu.bai 如果勾选直接报废，则将找到的未出站数据出站
            updateNoSiteOutData(tenantId, mtMaterialLot.getMaterialLotId(), userId, dto.getFlag());
            //调用API{eoScrap} 报废
            MtEoActualVO6 mtEoActualVO6 = new MtEoActualVO6();
            mtEoActualVO6.setEoId(mtMaterialLot.getEoId());
            mtEoActualVO6.setTrxScrappedQty(mtMaterialLot.getPrimaryUomQty());
            mtEoActualVO6.setParentEventId(eventId);
            mtEoActualRepository.eoScrap(tenantId, mtEoActualVO6);
            //调用API {materialLotUpdate} 条码更新
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotVO2.setEnableFlag(NO);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            result = "true";
        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag()) ||
                HmeConstants.ProcessStatus.THREE.equals(dto.getFlag())) {
            //直接返修/通知工艺判定
            // 直接返修 勾选返修记录 不更新条码质量状态 其他情况更新
            if (!HmeConstants.ProcessStatus.THREE.equals(dto.getFlag()) || !YES.equals(dto.getReworkRecordFlag())) {
                //更新条码的质量状态
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.NG);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
            }

            if (HmeConstants.ProcessStatus.THREE.equals(dto.getFlag())) {
                //2020-08-25 add by chaonan.hu for lu.bai 如果勾选直接返修，则将找到的未出站数据出站
                //2020-11-12 edit by chaonan.hu for lu.bai 当是工序不良返修时，出站需要记录返修标识
                updateNoSiteOutData(tenantId, mtMaterialLot.getMaterialLotId(), userId, dto.getFlag());

                //重新返修  更新条码返修标识
                //2021-02-07 add by sanfeng.zhang for can.wang 直接返修 勾选返修记录 则不更新返修标识
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
        //按照组织+工位+序列号+用户为唯一性，清空临时数据
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
        //根据序列号查询工步
        List<String> routerStepList = hmeNcDisposePlatformMapper.getRouterStep(tenantId, materialLotCode);

        //根据工位查询工序
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(workcellId);
        }});
        //根据工序与工艺关系找到工艺
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
            //查询的新的材料清单数据插入临时表Hme_nc_component_temp中
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
        //封装返回结果
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

        //查询临时表数据
        List<HmeNcDisposePlatformDTO13> hmeNcDisposePlatformDTO13List = hmeNcDisposePlatformMapper.materialDataPageQuery(tenantId, siteId, userId.toString(), dto);
        //将数据按照物料分组
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
                    //2020-09-10 add by choanan.hu 增加返回报废数量的逻辑
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
            //删除临时表数据
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
        //根据序列号在表mt_material_lot查询eoid、siteId
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getSnNumber());
        }});
        //事件创建
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
        //V20211223 modify by penglin.sui for peng.zhao 记录不良发起工位
        mtNcRecord.setRootCauseWorkcellId(dto.getWorkcellId());
        mtNcRecord.setMaterialId(mtMaterialLot.getMaterialId());
        mtNcRecord.setNcStatus(HmeConstants.NcStatus.OPEN);
        MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, dto.getNcCodeId());
        mtNcRecord.setConfirmedStatus(mtNcGroup.getConfirmRequired());
        mtNcRecord.setClosureRequiredFlag(mtNcGroup.getClosureRequired());
        mtNcRecord.setDispositionDoneFlag(NO);
        //2020-09-20 add by chaonan.hu fou lu.bai 增加RootCauseOperationId记录到表中
        //根据material_lot_Id在表hme_eo_job_sn找到其最近一条数据（site_in_date）的eo_step_id和operation_id，如果没有找到，则为空
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
        //根据工序在表mt_operation_wkc_dispatch_rel查询operation_id
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getProcessId());
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
        String operationId = operationWkcDispatchRelList.get(0).getOperationId();
        // 根据序列号找eo
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        Date now = CommonUtils.currentTimeGet();
        //获取LOV的值
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<String> loadSequenceList = new ArrayList<>();
        // 取最近的jobId
        String lastJobId = hmeNcDisposePlatformMapper.queryLatestJobIdByEoAndWorkcell(tenantId, mtNcRecord.getEoId(), mtNcRecord.getWorkcellId());
        for (HmeNcDisposePlatformVO4 material : dto.getMaterilalList()) {
            if (CollectionUtils.isEmpty(material.getDetailList()) && Objects.nonNull(material.getApplyQty())) {
                //未投料 1、父子不良记录生成
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
                    // 勾选了不退料标识 则不做以下逻辑
                    if (!YES.equals(material.getNoReturnMaterialFlag())) {
                        //2、报废事务
                        HmeNcDisposePlatformDTO33 hmeNcDisposePlatformDTO33 = new HmeNcDisposePlatformDTO33();
                        hmeNcDisposePlatformDTO33.setEventId(transferEventId);
                        hmeNcDisposePlatformDTO33.setScrapTransactionId(transferCloseEventId);
                        hmeNcDisposePlatformDTO33.setSnNumber(dto.getSnNumber());
                        hmeNcDisposePlatformDTO33.setProcessId(dto.getProcessId());
                        noScrap(tenantId, hmeNcDisposePlatformDTO33, material);
                        //3、调用API{materiallotconsume}扣减条码数量
                        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                        mtMaterialLotVO1.setMaterialLotId(material.getMaterialLotId());
                        mtMaterialLotVO1.setTrxPrimaryUomQty(material.getApplyQty().doubleValue());
                        mtMaterialLotVO1.setParentEventId(eventId);
                        mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);

                        //4、调用API{componentAssembleProcess}进行执行作业组件装配
                        MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
                        //判断勾选物料是否存在条码对应的eo下的装配清单下
                        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
                        if (StringUtils.isNotEmpty(bomComponentId)) {
                            mtAssembleProcessActualVO5.setAssembleExcessFlag(NO);
                            mtAssembleProcessActualVO5.setBomComponentId(bomComponentId);
                        } else {
                            mtAssembleProcessActualVO5.setAssembleExcessFlag(YES);
                        }
                        //2020-11-27 edit by chaonan.hu for can.wang 增加OperationId和RouterStepId传入参数
                        //2020-12-25 edit by chaonan.hu for can.wang OperationId和RouterStepId传入参数取值逻辑修改
                        //根据eoId查询router_step_id
                        List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, mtEo.getEoId());
                        //根据多个routerStepId、operationId查询表mt_router_operation的ROUTER_STEP_ID
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
                            //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
                            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_NC_0046", "HME"));
                        } else if (mtRouterOperationList.size() > 1) {
                            //如果找到多个，报错{当前工艺找到多个工艺步骤，请检查}
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
                        //5、调用API{eocomponentscrap}进行执行作业组件报废
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

                    // 芯片物料下面会记录扩展表及关闭 判断跳过
                    if (CollectionUtils.isEmpty(material.getMaterialLotLoadIdList())) {
                        //如果是直接报废,则调用API{ncRecordAndIncidentClose}进行父不良记录关闭、事故关闭。
                        MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                        mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
                        mtNcRecordVO2.setEventRequestId(createEventRequestId);
                        mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
                        //将处理方法、不退料标识记录到不良记录扩展属性表中
                        hmeNcRecordAttr.setProcessMethod("3");
                        if (HmeConstants.ConstantValue.YES.equals(material.getNoReturnMaterialFlag())) {
                            hmeNcRecordAttr.setAttribute2(HmeConstants.ConstantValue.YES);
                        }
                    }
                } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag())) {
                    //如果是通知工艺判定
                    // 勾选了不退料标识 则不进行以下逻辑
                    if (!YES.equals(material.getNoReturnMaterialFlag())) {
                        //2、调用API{materiallotconsume}扣减条码数量
                        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                        mtMaterialLotVO1.setMaterialLotId(material.getMaterialLotId());
                        mtMaterialLotVO1.setTrxPrimaryUomQty(material.getApplyQty().doubleValue());
                        mtMaterialLotVO1.setParentEventId(eventId);
                        mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                        //2020-01-07 add by chaonan.hu for can.wang 增加调用API{ onhandQtyUpdateProcess }进行库位转移逻辑
                        locatorTransfer(tenantId, material.getMaterialLotId(), material.getApplyQty(), createEventRequestId);
                    } else {
                        // 芯片物料下面会记录不退料标识 判断跳过
                        if (CollectionUtils.isEmpty(material.getMaterialLotLoadIdList())) {
                            hmeNcRecordAttr.setAttribute2(HmeConstants.ConstantValue.YES);
                        }
                    }
                }
                if (CollectionUtils.isEmpty(material.getMaterialLotLoadIdList())) {
                    hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);
                }
                // 条码物料为芯片物料时 则根据申请数量生成多个不良记录
                if (CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList())) {
                    // 更新装载信息作业记录表、不良位置、来料装载位置表、装载信息作业对象表
                    Integer applyQty = material.getApplyQty().intValue();
                    Integer loadSize = CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList()) ? material.getMaterialLotLoadIdList().size() : 0;
                    if (loadSize.compareTo(applyQty) != 0) {
                        throw new MtException("HME_NC_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0074", "HME"));
                    }
                    // 批量获取序列
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
                    // 查询装载信息
                    List<HmeMaterialLotLoad> loadList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList())) {
                        loadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class).andWhere(Sqls.custom()
                                .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID, material.getMaterialLotLoadIdList())).build());
                    }
                    for (int i = 0; i < applyQty; i++) {
                        HmeMaterialLotLoad materialLotLoad = loadList.get(i);
                        // 校验当前序列对应的不良记录是否已关闭
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

                        // 生成不良记录 芯片物料的 数量为1
                        hmeNcDisposePlatformDTO32.setApplyQty(BigDecimal.ONE);
                        mtNcRecord = ncRecordCreate(tenantId, mtNcRecord, hmeNcDisposePlatformDTO32);
                        // 对直接报废的 根据申请数 生成多个记录  通知工艺判定 记录序列信息
                        HmeNcRecordAttr ncRecordAttr = new HmeNcRecordAttr();
                        ncRecordAttr.setTenantId(tenantId);
                        ncRecordAttr.setParentRecordId(mtNcRecord.getNcRecordId());
                        ncRecordAttr.setAttribute8(lastJobId);
                        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
                            // 直接报废
                            //如果是直接报废,则调用API{ncRecordAndIncidentClose}进行父不良记录关闭、事故关闭。
                            MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                            mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
                            mtNcRecordVO2.setEventRequestId(createEventRequestId);
                            mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);

                            //将处理方法、不退料标识及序列记录到不良记录扩展属性表中
                            ncRecordAttr.setProcessMethod("3");
                            if (HmeConstants.ConstantValue.YES.equals(material.getNoReturnMaterialFlag())) {
                                ncRecordAttr.setAttribute2(HmeConstants.ConstantValue.YES);
                            }
                            ncRecordAttr.setAttribute7(materialLotLoad.getLoadSequence());
                        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag())) {
                            //将序列记录到不良记录扩展属性表中
                            if (HmeConstants.ConstantValue.YES.equals(material.getNoReturnMaterialFlag())) {
                                ncRecordAttr.setAttribute2(HmeConstants.ConstantValue.YES);
                            }
                            ncRecordAttr.setAttribute7(materialLotLoad.getLoadSequence());
                        }
                        hmeNcRecordAttrRepository.insertSelective(ncRecordAttr);

                        // 装载信息作业记录表
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
                        // 装载信息作业对象表
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
                        // 新增不良记录
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
                            // 直接报废未勾选不退料标识 更新装载信息 条码为空
                            materialLotLoad.setMaterialLotId("");
                            materialLotLoadList.add(materialLotLoad);
                        }
                    }
                }
            } else if (CollectionUtils.isNotEmpty(material.getDetailList())) {
                //已投料
                // 第一个芯片物料 生成装载信息
                Boolean loadFlag = false;
                for (HmeNcDisposePlatformVO5 hmeNcDisposePlatformVO5 : material.getDetailList()) {
                    if (Objects.nonNull(hmeNcDisposePlatformVO5.getApplyQty())) {
                        // 是否是芯片物料且第一条投料条码 是-> 根据申请数量记录多个不良记录 否-> 按之前逻辑
                        Boolean cosMaterialAndFirstFlag = false;
                        //1、父子不良记录生成
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
                                    // 更新装载信息作业记录表、不良位置、来料装载位置表、装载信息作业对象表
                                    Integer applyQty = hmeNcDisposePlatformVO5.getApplyQty().intValue();
                                    Integer loadSize = CollectionUtils.isNotEmpty(material.getMaterialLotLoadIdList()) ? material.getMaterialLotLoadIdList().size() : 0;
                                    if (loadSize.compareTo(applyQty) != 0) {
                                        throw new MtException("HME_NC_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "HME_NC_0074", "HME"));
                                    }
                                    // 批量获取序列
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
                                    // 查询装载信息
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
                                            // 直接报废
                                            //如果是直接报废,则调用API{ncRecordAndIncidentClose}进行父不良记录关闭、事故关闭。
                                            MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                                            mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
                                            mtNcRecordVO2.setEventRequestId(createEventRequestId);
                                            mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);

                                            //将处理方法、不退料标识及序列记录到不良记录扩展属性表中
                                            hmeNcRecordAttr.setProcessMethod("3");
                                            if (YES.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                                hmeNcRecordAttr.setAttribute2(YES);
                                            }
                                            hmeNcRecordAttr.setAttribute7(materialLotLoad.getLoadSequence());
                                            //2021-03-18 add by chaonan.hu for yongjiang.wu 烧结已投料的芯片发生报废时，更新性能信息
                                            updateFunctionInfo(tenantId, mtEo.getEoId(), dto.getWorkcellId(), materialLotLoad.getLoadSequence());
                                        } else if (HmeConstants.ProcessStatus.TWO.equals(dto.getFlag())) {
                                            if (YES.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                                // 不退料标识为Y是 记录不退料标识、序列记录到不良记录扩展属性表中
                                                //将序列记录到不良记录扩展属性表中
                                                hmeNcRecordAttr.setAttribute2(YES);
                                            }
                                            hmeNcRecordAttr.setAttribute7(materialLotLoad.getLoadSequence());
                                        }
                                        hmeNcRecordAttrRepository.insertSelective(hmeNcRecordAttr);

                                        // 装载信息作业记录表
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
                                        // 装载信息作业对象表
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
                                        // 新增不良记录
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
                                            // 直接报废未勾选不退料标识 更新装载信息 条码为空
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
                            //如果是直接报废,则调用API{ncRecordAndIncidentClose}进行父不良记录关闭、事故关闭。
                            if (!cosMaterialAndFirstFlag) {
                                MtNcRecordVO2 mtNcRecordVO2 = new MtNcRecordVO2();
                                mtNcRecordVO2.setNcRecordId(mtNcRecord.getNcRecordId());
                                mtNcRecordVO2.setEventRequestId(createEventRequestId);
                                mtNcRecordRepository.ncRecordAndIncidentClose(tenantId, mtNcRecordVO2);
                            }
                            if (NO.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                //2、2020-12-13 edit by chaonan.hu for can.wang 已投料 报废时，事务传输逻辑大改
                                HmeNcDisposePlatformDTO33 hmeNcDisposePlatformDTO33 = new HmeNcDisposePlatformDTO33();
                                hmeNcDisposePlatformDTO33.setEventId(transferEventId);
                                hmeNcDisposePlatformDTO33.setScrapTransactionId(transferCloseEventId);
                                hmeNcDisposePlatformDTO33.setSnNumber(dto.getSnNumber());
                                hmeNcDisposePlatformDTO33.setProcessId(dto.getProcessId());
                                //2021-02-03 edit by chaonan.hu for can.wang 已投料直接报废时事务传输逻辑去掉
                                //alreadyAssembleScrap(tenantId, hmeNcDisposePlatformDTO33, hmeNcDisposePlatformVO5);
                                //3、调用API{componentScrapProcess}进行组件报废处理
                                HmeNcDisposePlatformVO6 hmeNcDisposePlatformVO6 = orangeScrap(tenantId, hmeNcDisposePlatformVO5, hmeNcDisposePlatformDTO31);
                                hmeNcDisposePlatformVO6.setEventId(eventId);
                                hmeNcDisposePlatformVO6.setMaterialId(hmeNcDisposePlatformVO5.getMaterialId());
                                hmeNcDisposePlatformVO6.setMaterialLotId(hmeNcDisposePlatformVO5.getMaterialLotId());
                                hmeNcDisposePlatformVO6.setApplyQty(hmeNcDisposePlatformVO5.getApplyQty());
                                //4、投料记录更新
                                updateAssembleRecord(tenantId, mtMaterialLot.getSiteId(), dto.getWorkcellId(), hmeNcDisposePlatformVO5);
//                                //4、退料事务
//                                transactionSync(tenantId, "RETURN_MATERIAL", hmeNcDisposePlatformVO6);
//                                //5、投料事务
//                                transactionSync(tenantId, "ASSEMBLE_MATERIAL", hmeNcDisposePlatformVO6);
                            }
                            //6、将处理方法记录到不良记录扩展属性表中
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
                            //如果是通知工艺判定
                            if (NO.equals(hmeNcDisposePlatformVO5.getNoReturnMaterialFlag())) {
                                //2、2020-12-16 edit by chaonan.hu for can.wang 退料事务逻辑大改
                                HmeNcDisposePlatformDTO33 hmeNcDisposePlatformDTO33 = new HmeNcDisposePlatformDTO33();
                                hmeNcDisposePlatformDTO33.setEventId(transferEventId);
                                hmeNcDisposePlatformDTO33.setScrapTransactionId(transferCloseEventId);
                                hmeNcDisposePlatformDTO33.setSnNumber(dto.getSnNumber());
                                hmeNcDisposePlatformDTO33.setProcessId(dto.getProcessId());
                                alreadyAssembleNotice(tenantId, hmeNcDisposePlatformDTO33, hmeNcDisposePlatformVO5);
                                //3、投料记录更新
                                updateAssembleRecord(tenantId, mtMaterialLot.getSiteId(), dto.getWorkcellId(), hmeNcDisposePlatformVO5);
                                //4、调用API{componentAssembleCancelProcess}进行组件装配撤销处理
                                orangeAssemble(tenantId, hmeNcDisposePlatformVO5, hmeNcDisposePlatformDTO31);
                                //2020-01-07 add by chaonan.hu for can.wang 增加调用API{ onhandQtyUpdateProcess }进行库位转移逻辑
                                locatorTransfer(tenantId, hmeNcDisposePlatformVO5.getMaterialLotId(), hmeNcDisposePlatformVO5.getApplyQty(), createEventRequestId);
                            } else {
                                //5、将处理方法记录到不良记录扩展属性表中
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
        //判断是否是当前库位
        if (dto.getCurrentwWorkcellId().equals(dto.getWorkcellId())) {
            //如果是，根据material_lot_Id在表hme_eo_job_sn找到其最近一条数据（site_in_date）的eo_step_id和operation_id，如果没有找到，则为空
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
            //如果不是，则取前台传入的数据
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
        //将条码在物料批表中对应的CURRENT_CONTAINER_ID， TOP_CONTAINER_ID置为空
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
        //将条码在表mt_container_load_detail中LOAD_OBJECT_TYPE为MATERIAL_LOT的数据删除
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
        //不良申请物料条码待审核记录
        List<HmeNcDisposePlatformVO4> ncMaterialLotList = hmeNcDisposePlatformMapper.ncMaterialLotQuery(tenantId, mtMaterialLot.getEoId(), workcellId);
        //未投料条码ID集合
        List<String> ncMaterialLotIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ncMaterialLotList)) {
            ncMaterialLotList = ncMaterialLotList.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(item -> item.getMaterialLotId()))), ArrayList::new));
            ncMaterialLotIdList = ncMaterialLotList.stream().map(HmeNcDisposePlatformVO4::getMaterialLotId).collect(Collectors.toList());
            for (HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 : ncMaterialLotList) {
                //待审核数量
                BigDecimal waitAuditQty = hmeNcDisposePlatformMapper.waitAuditQtyQuery(tenantId, hmeNcDisposePlatformVO4.getMaterialLotId(), mtMaterialLot.getEoId());
                hmeNcDisposePlatformVO4.setWaitAuditQty(waitAuditQty);
            }
            ncMaterialLotList = ncMaterialLotList.stream().filter(item -> item.getWaitAuditQty().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList());
        }
        //装配实绩表的投料记录
        List<HmeNcDisposePlatformVO4> eoComponentActualAssembleList = hmeNcDisposePlatformMapper.eoComponentActualAssembleQuery(tenantId, mtMaterialLot.getEoId(), workcellId);
        List<HmeNcDisposePlatformVO4> eoComponentActualAssembleGroupList = new ArrayList<>();
        //装配实绩表的投料记录根据物料汇总
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
                //待审核数量
                BigDecimal waitAuditQty = hmeNcDisposePlatformMapper.waitAuditQtyReleaseQuery(tenantId, hmeNcDisposePlatformVO4.getMaterialId(), mtMaterialLot.getEoId(), ncMaterialLotIdList);
                hmeNcDisposePlatformVO4.setWaitAuditQty(waitAuditQty);
            }
            eoComponentActualAssembleGroupList = eoComponentActualAssembleGroupList.stream()
                    .filter(item -> (item.getReleaseQty().compareTo(BigDecimal.ZERO) == 1) || (item.getScrapQty().compareTo(BigDecimal.ZERO) == 1)
                            || (item.getWaitAuditQty().compareTo(BigDecimal.ZERO) == 1)).collect(Collectors.toList());
        }

        List<HmeNcDisposePlatformVO5> detailList = new ArrayList<>();
        List<HmeNcDisposePlatformVO5> detailGroupList = new ArrayList<>();
        //根据前台选择的工序ID查询表mt_operation_wkc_dispatch_rel中优先级最高的operation_id
        String operationId = hmeNcDisposePlatformMapper.getOperationId(tenantId, processId);
        if (StringUtils.isNotEmpty(operationId) && CollectionUtils.isNotEmpty(eoComponentActualAssembleGroupList)) {
            //根据扫描SN的Id、工艺ID查询表hme_eo_job_sn中的jobId
            List<String> jobIdList = hmeNcDisposePlatformMapper.getJobIdList(tenantId, mtMaterialLot.getMaterialLotId(), operationId);
            for (String jobId : jobIdList) {
                //序列号物料投料记录
                List<HmeNcDisposePlatformVO5> HmeNcDisposePlatformVO5s = hmeNcDisposePlatformMapper.snMaterialQuery(tenantId, jobId);
                if (CollectionUtils.isNotEmpty(HmeNcDisposePlatformVO5s)) {
                    detailList.addAll(HmeNcDisposePlatformVO5s);
                }
                //批次、时效投料记录
                List<HmeNcDisposePlatformVO5> hmeNcDisposePlatformVO5s2 = hmeNcDisposePlatformMapper.timeMaterialQuery(tenantId, jobId);
                if (CollectionUtils.isNotEmpty(hmeNcDisposePlatformVO5s2)) {
                    detailList.addAll(hmeNcDisposePlatformVO5s2);
                }
            }
        }
        //序列号物料投料记录和批次/时效物料投料记录集合根据物料批汇总, 投入数量累计，jobId取最大的
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
        // 默认非芯片物料
        String cosMaterialFlag = STRING_ZERO;
        if (CollectionUtils.isEmpty(dto.getMaterilalList())) {
            throw new MtException("HME_NC_0071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0071", "HME"));
        }
        //获取LOV的值
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        // 找到为芯片物料 找不到则报错
        for (HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 : dto.getMaterilalList()) {
            if (CollectionUtils.isNotEmpty(hmeNcDisposePlatformVO4.getDetailList())) {
                // 已投料的
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
                // 未投料的
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
        //获取LOV的值
        List<LovValueDTO> list = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        // 找到为芯片物料 找不到则报错
        String materialLotId = "";
        // 默认已投
        Boolean feelFlag = false;
        for (HmeNcDisposePlatformVO4 platformVO4 : dto.getMaterilalList()) {
            if (CollectionUtils.isNotEmpty(platformVO4.getDetailList())) {
                // 已投料的
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
                    // 以上找到跳出整个循环
                    break;
                }
            } else {
                // 未投料的
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

        // 左侧展示信息
        HmeNcDisposePlatformVO7 hmeNcDisposePlatformVO7 = hmeNcDisposePlatformMapper.queryMaterialLotInfo(tenantId, materialLotId);
        // 查询该条码未关闭的材料不良单据数量
        BigDecimal totalNonOpenNcQty = hmeNcDisposePlatformMapper.queryOpenNcRecordQty(tenantId, materialLotId);
        hmeNcDisposePlatformVO7.setTotalQty(hmeNcDisposePlatformVO7.getPrimaryUomQty().add(totalNonOpenNcQty));
        // 装载位置信息
        //物料批行列装载图
        List<HmeMaterialLotLoad> loadList = new ArrayList<>();
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        if (feelFlag) {
            // 未投的 从装载表取
            HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
            hmeMaterialLotLoad.setMaterialLotId(hmeNcDisposePlatformVO7.getMaterialLotId());
            loadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        } else {
            // 已投
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
                        // 芯片序列
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
        //根据loadSequence获取表hme_selection_details的ATTRIBUTE2
        List<HmeSelectionDetails> hmeSelectionDetails = hmeSelectionDetailsRepository.select(new HmeSelectionDetails() {{
            setTenantId(tenantId);
            setLoadSequence(loadSequence);
        }});
        if(CollectionUtils.isEmpty(hmeSelectionDetails) || StringUtils.isBlank(hmeSelectionDetails.get(0).getAttribute2())){
            throw new MtException("HME_NC_0078", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0078", "HME", loadSequence));
        }
        String attribute2 = hmeSelectionDetails.get(0).getAttribute2();
        //根据eoid、workcellId、attribute2获取表hme_eo_job_data_record
        List<HmeNcDisposePlatformVO8> hmeNcDisposePlatformVO8List = hmeNcDisposePlatformMapper.eoJobDataRecordQuery(tenantId, eoId, workcellId, attribute2);
        if(CollectionUtils.isEmpty(hmeNcDisposePlatformVO8List)){
            throw new MtException("HME_NC_0079", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0079", "HME", attribute2));
        }
        List<HmeEoJobDataRecord> updateList = new ArrayList<>();
        for (HmeNcDisposePlatformVO8 hmeNcDisposePlatformVO8:hmeNcDisposePlatformVO8List) {
            String tagCode = hmeNcDisposePlatformVO8.getTagCode();
            //根据-拆分tagCode,根据拆分规则获取attribute2前1个索引的拆分字符串
            List<String> splitList = Arrays.asList(tagCode.split("-"));
            int i = splitList.indexOf(attribute2) - 1 ;
            if(i < 0){
                MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
                throw new MtException("HME_NC_0081", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0081", "HME", mtEo.getIdentification(), tagCode));
            }
            String splitNumber = splitList.get(i);
            //根据eoid、workcellId、splitNumber_SUMP获取表hme_eo_job_data_record,这里说只会取到一条数据,所以默认取第一个
            List<HmeNcDisposePlatformVO8> hmeNcDisposePlatformVO8s = hmeNcDisposePlatformMapper.eoJobDataRecordQuery(tenantId, eoId, workcellId, "-"+splitNumber+"_SUMP");
            if(CollectionUtils.isEmpty(hmeNcDisposePlatformVO8s)){
                MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
                throw new MtException("HME_NC_0080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0080", "HME", mtEo.getIdentification(), splitNumber));
            }
            //将现在找到的记录的Result更新为当前记录的Result-当前循环记录的Result
            HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
            hmeEoJobDataRecord.setJobRecordId(hmeNcDisposePlatformVO8s.get(0).getJobRecordId());
            hmeEoJobDataRecord.setResult(hmeNcDisposePlatformVO8s.get(0).getResult().subtract(hmeNcDisposePlatformVO8.getResult()).toString());
            updateList.add(hmeEoJobDataRecord);
            //将当前循环记录的Result置为0
            HmeEoJobDataRecord hmeEoJobDataRecord2 = new HmeEoJobDataRecord();
            hmeEoJobDataRecord2.setJobRecordId(hmeNcDisposePlatformVO8.getJobRecordId());
            hmeEoJobDataRecord2.setResult("0");
            updateList.add(hmeEoJobDataRecord2);
        }
        //批量更新
        if(CollectionUtils.isNotEmpty(updateList)){
            int count = 0;
            // 获取当前用户
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
        //调用API{componentAssembleCancelProcess}进行组件装配撤销处理
        MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        mtAssembleProcessActualVO5.setEoId(mtEo.getEoId());
        mtAssembleProcessActualVO5.setTrxAssembleQty(material.getApplyQty().doubleValue());
        //判断勾选物料是否存在条码对应的eo下的装配清单下
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isEmpty(bomComponentId)) {
            //如果不存在，则传Y
            mtAssembleProcessActualVO5.setAssembleExcessFlag(YES);
            mtAssembleProcessActualVO5.setMaterialId(material.getMaterialId());
        } else {
            //2020-12-04 edit by chaonan.hu for can.wang 如果存在，直接传N
            mtAssembleProcessActualVO5.setAssembleExcessFlag(NO);
            mtAssembleProcessActualVO5.setBomComponentId(bomComponentId);
//            //如果存在，则进一步判断投料数量是否超过其工单的总需求数量
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
//            //如果投料数量+申请数量未超过总需求数量，则为N，否则为Y
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
        //2020-12-25 edit by chaonan.hu for can.wang operationId、routerStepId取值逻辑修改，先把原先逻辑注释掉
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
//        //根据物料站点ID以及organizationtype = PRODUCTIONLINE并且organizationid = 生产线ID找到表mt_pfep_manufacturing对应的 OPERATION_ASSEMBLE_FLAG
//        MtPfepManufacturing mtPfepManufacturing = mtPfepManufacturingRepository.selectOne(new MtPfepManufacturing() {{
//            setTenantId(tenantId);
//            setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
//            setOrganizationType("PRODUCTIONLINE");
//            setOrganizationId(dto.getProdLineId());
//        }});
//        if (Objects.nonNull(mtPfepManufacturing) && YES.equals(mtPfepManufacturing.getOperationAssembleFlag())) {
//            //如果OperationAssembleFlag为Y则需要传operation_id和routerStepId
//            if (NO.equals(mtAssembleProcessActualVO5.getAssembleExcessFlag())) {
//                //如果是计划内，则根据bom_component_id、在表mt_eo_component_actual 找到对应数据，取其operation_id（找到多条取第一条）
//                List<MtEoComponentActual> mtEoComponentActualList = mtEoComponentActualRepository.select(new MtEoComponentActual() {{
//                    setTenantId(tenantId);
//                    setBomComponentId(bomComponentId);
//                    setEoId(mtEo.getEoId());
//                }});
//                if (CollectionUtils.isNotEmpty(mtEoComponentActualList)) {
//                    mtAssembleProcessActualVO5.setOperationId(mtEoComponentActualList.get(0).getOperationId());
//                }
//            } else {
//                //如果是计划外
//                List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
//                    setTenantId(tenantId);
//                    setOrganizationId(dto.getWorkcerllId());
//                    setOrganizationType("WORKCELL");
//                    setParentOrganizationType("WORKCELL");
//                }});
//                if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
//                    //根据工序ID查询表mt_operation_wkc_dispatch_rel中优先级最高的operation_id
//                    String operationId = hmeNcDisposePlatformMapper.getOperationId(tenantId, mtModOrganizationRels.get(0).getParentOrganizationId());
//                    if (StringUtils.isNotEmpty(operationId)) {
//                        mtAssembleProcessActualVO5.setOperationId(operationId);
//                    }
//                }
//            }
//        }
        //根据工序在表mt_operation_wkc_dispatch_rel查询operation_id
        String operationId = null;
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getProcessId());
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
        //根据eoId查询router_step_id
        List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, mtEo.getEoId());
        //根据多个routerStepId、operationId查询表mt_router_operation的ROUTER_STEP_ID
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
            //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        } else if (mtRouterOperationList.size() > 1) {
            //如果找到多个，报错{当前工艺找到多个工艺步骤，请检查}
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
        //判断勾选物料是否存在条码对应的eo下的装配清单下
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isEmpty(bomComponentId)) {
            //如果不存在，则传Y
            mtEoComponentActualVO4.setAssembleExcessFlag(YES);
        } else {
            //2020-12-04 edit by chaonan.hu for can.wang 如果存在，则传N
            mtEoComponentActualVO4.setAssembleExcessFlag(NO);
            //如果存在，则进一步判断投料数量是否超过其工单的总需求数量
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
            //如果投料数量+申请数量未超过总需求数量，则为N，否则为Y
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
        //根据工序在表mt_operation_wkc_dispatch_rel查询operation_id
        String operationId = null;
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getProcessId());
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
        //根据eoId查询router_step_id
        List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, mtEo.getEoId());
        //根据多个routerStepId、operationId查询表mt_router_operation的ROUTER_STEP_ID
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
            //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        } else if (mtRouterOperationList.size() > 1) {
            //如果找到多个，报错{当前工艺找到多个工艺步骤，请检查}
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
        //edit by chaonan.hu for lu.bai 由调用API{eocomponentscrap}改为{componentScrapProcess},参数值未变
        MtAssembleProcessActualVO7 mtAssembleProcessActualVO7 = new MtAssembleProcessActualVO7();
        BeanUtils.copyProperties(mtEoComponentActualVO4, mtAssembleProcessActualVO7);
        mtAssembleProcessActualRepository.componentScrapProcess(tenantId, mtAssembleProcessActualVO7);
        return hmeNcDisposePlatformVO6;
    }

    MtNcRecord ncRecordCreate(Long tenantId, MtNcRecord mtNcRecord, HmeNcDisposePlatformDTO32 dto) {
        mtNcRecord.setComponentMaterialId(dto.getMaterialId());
        //事故创建
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
        //2020-09-10 add by chaonan.hu fou lu.bai 增加记录报废数量的逻辑
        mtNcRecord.setQty(dto.getApplyQty().doubleValue());
        mtNcRecordRepository.insertSelective(mtNcRecord);
        //记录历史
        MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
        BeanUtils.copyProperties(mtNcRecord, mtNcRecordHis);
        mtNcRecordHis.setEventId(dto.getEventId());
        mtNcRecordHisRepository.insertSelective(mtNcRecordHis);
        //记录不良代码与附件之间的关系
        if (StringUtils.isNotBlank(dto.getUuid())) {
            HmeNcRecordAttachmentRel hmeNcRecordAttachmentRel = new HmeNcRecordAttachmentRel();
            hmeNcRecordAttachmentRel.setTenantId(tenantId);
            hmeNcRecordAttachmentRel.setNcRecordId(mtNcRecord.getNcRecordId());
            hmeNcRecordAttachmentRel.setAttachmentId(dto.getUuid());
            hmeNcRecordAttachmentRelRepository.insertSelective(hmeNcRecordAttachmentRel);
        }
        //子不良记录生成
        if (CollectionUtils.isNotEmpty(dto.getChildNcCodeIdList())) {
            //子不良记录生成
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
                    //如果是直接报废，调用API{ncRecordAndIncidentClose}进行子不良记录关闭、事故关闭。
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
        //根据物料Id和站点ID查询物料站点的扩展属性attribute14，如果找不到，默认lot
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
            //如果是序列号物料，则根据jobId、materialLotId、materialId、isissued=1找到投料数据，将投料数据的isissued置为0，并复制生成退料数据
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
                //复制生成退料数据
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
            //如果是批次/时效物料，则在表HME_EO_JOB_SN_LOT_MATERIAL中根据jobId、materialLotId、ReleaseQty大于0查找数据，循环扣减
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
            //再在表在表HME_EO_JOB_SN_LOT_MATERIAL中将插入一条数据
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
            //对于批次物料需要更新对应工位与物料关系表的released_qty
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
     * 已投料的直接报废 事务传输
     *
     * @param tenantId 租户ID
     * @param dto      参数信息
     * @param material 勾选的物料信息
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 10:58:50
     */
    void alreadyAssembleScrap(Long tenantId, HmeNcDisposePlatformDTO33 dto, HmeNcDisposePlatformVO5 material) {
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        //判断勾选物料是否存在条码对应的eo下的装配清单下
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isNotBlank(bomComponentId)) {
            //如果能找到，则在BOM内
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            if (mtBomComponent.getQty() != 0)   {
                //如果组件数量不等于0，则不为替代料,则须记录退料事务和报废事务
                //退料事务：根据主键料查询工单替代料、全局替代料、工步等
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
                //根据所有的material_id和对应工单的work_order_id和工步取对应的mt_work_order_component_actual.assemble_qty+SCRAPPED_QTY之和
                BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //根据bomComponentId获取其扩展属性lineAttribute4 2021-02-02 edit by chaonan.hu for can.wang 改为取lineAttribute5
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
                    //当assembleQty小于等于lineAttribute5时，则为申请数量的计划内退料
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
                    //当assembleQty大于lineAttribute5时，需要进一步判断
                    if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) <= 0) {
                        //当申请数量小于等于(assemble_qty - lineAttribute5)则为申请数量的计划外退料
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
                        //当申请数量大于(assemble_qty - lineAttribute5)则需记两笔事务，(assemble_qty - lineAttribute5)的计划外退料，[申请数量 – (assemble_qty - lineAttribute5)]的计划内退料
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
                //报废事务：根据所有物料的material_id和对应工单的work_order_id和工步Id取对应的mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //2021-02-02 edit by chaonan.hu for can.wang lineAttribute取值直接为lineAttribute5,不在为lineAttribute5 - lineAttribute4
                BigDecimal lineAttribute = lineAttribute5;
                if (scrapQty.compareTo(lineAttribute) >= 0) {
                    //如果scrapQty大于等于lineAttribute,则为申请数量的计划外投料
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
                    //如果scrapQty小于lineAttribute，需进一步判断
                    if (scrapQty.add(material.getApplyQty()).compareTo(lineAttribute) <= 0) {
                        //如果(scrapped_qty + 申请数量)小于等于lineAttribute，则为申请数量的计划内投料
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
                        //如果(scrapped_qty + 申请数量)大于lineAttribute，则需记两笔事务，一笔[(scrapped_qty + 申请数量) - lineAttribute]的计划外投料，一笔lineAttribute5-scrapped_qty的计划内投料
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
                //如果组件数量等于0，则为替代料，根据替代料找到对应的主键料
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
                //替代料需记录一笔计划外退料事务
                HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", mtBomComponent, hmeNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                //根据找到的主料的material_id查询到全局替代料、工单替代料、工步等
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
                //根据找到所有的material_id和对应工单的work_order_id、工步Id取对应的mt_work_order_component_actual.assemble_qty
                BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //根据主料的bomComponentId获取其扩展属性lineAttribute5
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
                    //如果assembleQty小于等于lineAttribute5，则需记两笔事务，申请数量的计划内退料，申请数量的计划外投料
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //申请数量的计划外投料
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryObjectTransactionRequestVO2);
                    primaryObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    primaryObjectTransactionRequestVO2.setTransactionReasonCode("不良报废投料");
                    primaryObjectTransactionRequestVO2.setMoveType("261");
                    primaryObjectTransactionRequestVO2.setMoveReason("不良报废投料");
                    primaryObjectTransactionRequestVO2.setBomReserveNum(null);
                    primaryObjectTransactionRequestVO2.setBomReserveLineNum(null);
                    primaryObjectTransactionRequestVO2.setSoNum(null);
                    primaryObjectTransactionRequestVO2.setSoLineNum(null);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO2);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO2));
                } else {
                    //如果assembleQty大于lineAttribute5，则需进一步判断
                    if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) == 1) {
                        //如果申请数量大于(assembleQty-lineAttribute5),则需记两笔事务,其他情况主料不做任何事务处理
                        //申请数量-(assembleQty-lineAttribute5)的计划内退料，申请数量-(assembleQty-lineAttribute5)的计划外投料
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty.subtract(lineAttribute5)));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //申请数量-(assembleQty-lineAttribute5)的计划外投料
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryObjectTransactionRequestVO2);
                        primaryObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        primaryObjectTransactionRequestVO2.setTransactionReasonCode("不良报废投料");
                        primaryObjectTransactionRequestVO2.setMoveType("261");
                        primaryObjectTransactionRequestVO2.setMoveReason("不良报废投料");
                        primaryObjectTransactionRequestVO2.setBomReserveNum(null);
                        primaryObjectTransactionRequestVO2.setBomReserveLineNum(null);
                        primaryObjectTransactionRequestVO2.setSoNum(null);
                        primaryObjectTransactionRequestVO2.setSoLineNum(null);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO2);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO2));
                    }
                }
                //替代料报废事务
                HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", mtBomComponent, hmeNcScrapDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
                //主键料报废事务
                //根据所有物料的material_id和对应工单的work_order_id和工步Id取对应的mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                BigDecimal lineAttribute = lineAttribute5;
                if (material.getApplyQty().compareTo(lineAttribute.subtract(scrapQty)) <= 0) {
                    //如果申请数量小于等于lineAttribute-scrapQty,则需记两笔事务，申请数量的计划外退料， 申请数量的计划内投料
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //申请数量的计划内投料
                    HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                    primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryInAssemble.setEventId(dto.getScrapTransactionId());
                    primaryInAssemble.setTransactionQty(material.getApplyQty());
                    primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                } else {
                    //如果申请数量大于lineAttribute-scrapQty,则需进一步判断
                    if(lineAttribute.compareTo(scrapQty) == 1){
                        //如果lineAttribute大于scrapQty时，则需记两笔事务,其他情况下则不作事务
                        //lineAttribute-scrapQty的HME_WO_ISSUE_R_EXT计划外退料
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.处理alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //lineAttribute-scrapQty的HME_WO_ISSUE计划内投料
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
//                        //如果lineAttribute5-lineAttribute4小于等于0，则报错{SAP维护总需求数量与需求数量错误}
//                        throw new MtException("HME_NC_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_NC_0041", "HME"));
//                    } else {
//                        //如果lineAttribute5-lineAttribute4大于0，则需记三笔事务
//                        //申请数量的计划外退料
//                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
//                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
//                        primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
//                        primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
//                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
//                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
//                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
//                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
//                        //scrapQty+申请数量-(lineAttribute5-lineAttribute4)的计划外投料
//                        WmsObjectTransactionRequestVO primaryOutAssembleDTO = new WmsObjectTransactionRequestVO();
//                        BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssembleDTO);
//                        primaryOutAssembleDTO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
//                        primaryOutAssembleDTO.setTransactionReasonCode("不良报废投料");
//                        primaryOutAssembleDTO.setMoveType("261");
//                        primaryOutAssembleDTO.setMoveReason("不良报废投料");
//                        primaryOutAssembleDTO.setTransactionQty(scrapQty.add(material.getApplyQty()).subtract(lineAttribute));
//                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryOutAssembleTransaction.objectTransactionRequestVO:{}", primaryOutAssembleDTO);
//                        //(lineAttribute5-lineAttribute4)-scrapQty的计划内投料
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
            //找不到，则在BOM外，先根据替代料找到主键料
            MtBomComponent primaryBomComponent = getPrimaryMaterial(tenantId, material.getMaterialId(), mtEo, dto.getProcessId());
            if (Objects.isNull(primaryBomComponent)) {
                //如果找不到，则报错{未在已投料信息中找到对应物料，请检查}
                throw new MtException("HME_NC_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0042", "HME"));
            }
            //替代料退料事务 申请数量的HME_WO_ISSUE_R_EXT计划外退料
            HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
            hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
            hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
            hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
            hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
            hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
            WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", null, hmeNcDisposePlatformDTO34);
            log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransactionS.objectTransactionRequestVO:{}", objectTransactionRequestVO);
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
            //主键料退料事务，根据找到的主料的material_id查询到全局替代料、工单替代料、工步等
            HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
            //根据找到所有的material_id和对应工单的work_order_id、工步Id取对应的mt_work_order_component_actual.assemble_qty
            BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
            //根据主料的bomComponentId获取其扩展属性lineAttribute5
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
                //如果assembleQty小于等于lineAttribute5，则需记两笔事务
                //申请数量的HME_WO_ISSUE_R计划内退料
                HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                //申请数量的HME_WO_ISSUE_EXT计划外投料
                WmsObjectTransactionRequestVO primaryOutAssemble = new WmsObjectTransactionRequestVO();
                BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssemble);
                primaryOutAssemble.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                primaryOutAssemble.setTransactionReasonCode("不良报废投料");
                primaryOutAssemble.setMoveType("261");
                primaryOutAssemble.setMoveReason("不良报废投料");
                primaryOutAssemble.setBomReserveNum(null);
                primaryOutAssemble.setBomReserveLineNum(null);
                primaryOutAssemble.setSoNum(null);
                primaryOutAssemble.setSoLineNum(null);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryAssembleOutTransaction.objectTransactionRequestVO:{}", primaryOutAssemble);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryOutAssemble));
            } else {
                //如果assembleQty大于lineAttribute5，则需进一步判断
                if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) == 1) {
                    //当申请数量大于(assembleQty-lineAttribute5)时，需做两笔事务，其他情况主键料不做处理
                    //申请数量-(assembleQty-lineAttribute5)的HME_WO_ISSUE_R计划内退料
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty.subtract(lineAttribute5)));
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //申请数量-(assembleQty-lineAttribute5)的HME_WO_ISSUE_ EXT计划外投料
                    WmsObjectTransactionRequestVO primaryOutAssemble = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssemble);
                    primaryOutAssemble.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    primaryOutAssemble.setTransactionReasonCode("不良报废投料");
                    primaryOutAssemble.setMoveType("261");
                    primaryOutAssemble.setMoveReason("不良报废投料");
                    primaryOutAssemble.setBomReserveLineNum(null);
                    primaryOutAssemble.setBomReserveNum(null);
                    primaryOutAssemble.setSoLineNum(null);
                    primaryOutAssemble.setSoNum(null);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryAssembleOutTransaction.objectTransactionRequestVO:{}", primaryOutAssemble);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryOutAssemble));
                }
            }
            //替代料报废事务  申请数量的HME_WO_ISSUE_EXT计划外投料
            HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
            hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
            hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
            hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
            hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
            hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
            WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", null, hmeNcScrapDisposePlatformDTO34);
            log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
            //主键料报废事务 根据所有物料的material_id和对应工单的work_order_id和工步Id取对应的mt_work_order_component_actual.scrapped_qty
            BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
            BigDecimal lineAttribute = lineAttribute5;
            if (material.getApplyQty().compareTo(lineAttribute.subtract(scrapQty)) <= 0) {
                //当申请数量小于等于lineAttribute-scrapQty, 则需记两笔事务
                //申请数量的HME_WO_ISSUE_R_EXT计划外退料
                HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryOutReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                //申请数量的HME_WO_ISSUE计划内投料
                HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                primaryInAssemble.setEventId(dto.getScrapTransactionId());
                primaryInAssemble.setTransactionQty(material.getApplyQty());
                primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
            } else {
                //当申请数量大于lineAttribute-scrapQty, 则需进一步判断
                if(lineAttribute.compareTo(scrapQty) == 1){
                    //如果lineAttribute大于scrapQty时，则需记两笔事务,其他情况下则不作事务
                    //lineAttribute-scrapQty的HME_WO_ISSUE_R_EXT计划外退料
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.处理alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //lineAttribute-scrapQty的HME_WO_ISSUE计划内投料
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
     * 已投料的通知工艺判定 事务传输
     *
     * @param tenantId 租户ID
     * @param dto      参数信息
     * @param material 勾选的物料信息
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 10:58:50
     */
    void alreadyAssembleNotice(Long tenantId, HmeNcDisposePlatformDTO33 dto, HmeNcDisposePlatformVO5 material) {
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        //判断勾选物料是否存在条码对应的eo下的装配清单下
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isNotBlank(bomComponentId)) {
            //如果能找到，则在BOM内
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            if (mtBomComponent.getQty() != 0) {
                //如果组件数量不等于0，则不为替代料,则须记录退料事务和报废事务
                //退料事务：根据找到的主料的material_id查询到全局替代料、工单替代料、工步等
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
                //根据找到所有的material_id和对应工单的work_order_id、工步Id取对应的mt_work_order_component_actual.assemble_qty
                BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //根据bomComponentId获取其扩展属性lineAttribute5
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
                    //当assembleQty小于等于lineAttribute5时，则为申请数量的计划内退料
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
                    //当assembleQty大于lineAttribute5时，需要进一步判断
                    if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) <= 0) {
                        //当申请数量小于等于(assemble_qty - lineAttribute5)则为申请数量的计划外退料
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
                        //当申请数量大于(assemble_qty - lineAttribute5)则需记两笔事务，(assemble_qty - lineAttribute5)的计划外退料，[申请数量 – (assemble_qty - lineAttribute4)]的计划内退料
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
                //如果组件数量等于0，则为替代料，根据替代料找到对应的主键料
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
                //替代料需记录一笔计划外退料事务
                HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
                hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", mtBomComponent, hmeNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.returnTransaction.objectTransactionRequestVO:{}", objectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                //根据找到的主料的material_id查询到全局替代料、工单替代料、工步等
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
                //根据找到所有的material_id和对应工单的work_order_id、工步Id取对应的mt_work_order_component_actual.assemble_qty
                BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //根据主料的bomComponentId获取其扩展属性lineAttribute4
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
                    //如果assembleQty小于等于lineAttribute5，则需记两笔事务，申请数量的计划内退料，申请数量的计划外投料
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //申请数量的计划外投料
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryObjectTransactionRequestVO2);
                    primaryObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    primaryObjectTransactionRequestVO2.setTransactionReasonCode("不良报废投料");
                    primaryObjectTransactionRequestVO2.setMoveType("261");
                    primaryObjectTransactionRequestVO2.setMoveReason("不良报废投料");
                    primaryObjectTransactionRequestVO2.setBomReserveNum(null);
                    primaryObjectTransactionRequestVO2.setBomReserveLineNum(null);
                    primaryObjectTransactionRequestVO2.setSoNum(null);
                    primaryObjectTransactionRequestVO2.setSoLineNum(null);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO2);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO2));
                } else {
                    //如果assembleQty大于lineAttribute5，则需进一步判断
                    if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) == 1) {
                        //如果申请数量大于(assembleQty-lineAttribute5),则需记两笔事务,其他情况主料不做任何事务处理
                        //申请数量-(assembleQty-lineAttribute5)的计划内退料，申请数量-(assembleQty-lineAttribute5)的计划外投料
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty.subtract(lineAttribute5)));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //申请数量-(assembleQty-lineAttribute5)的计划外投料
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO2 = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryObjectTransactionRequestVO2);
                        primaryObjectTransactionRequestVO2.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        primaryObjectTransactionRequestVO2.setTransactionReasonCode("不良报废投料");
                        primaryObjectTransactionRequestVO2.setMoveType("261");
                        primaryObjectTransactionRequestVO2.setMoveReason("不良报废投料");
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
            //找不到，则在BOM外，先根据替代料找到主键料
            MtBomComponent primaryBomComponent = getPrimaryMaterial(tenantId, material.getMaterialId(), mtEo, dto.getProcessId());
            if (Objects.isNull(primaryBomComponent)) {
                //如果找不到，则报错{未在已投料信息中找到对应物料，请检查}
                throw new MtException("HME_NC_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0042", "HME"));
            }
            //替代料退料事务 申请数量的HME_WO_ISSUE_R_EXT计划外退料
            HmeNcDisposePlatformDTO34 hmeNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
            hmeNcDisposePlatformDTO34.setEventId(dto.getEventId());
            hmeNcDisposePlatformDTO34.setMaterialId(material.getMaterialId());
            hmeNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
            hmeNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
            hmeNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
            WmsObjectTransactionRequestVO objectTransactionRequestVO = returnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", null, hmeNcDisposePlatformDTO34);
            log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransactionS.objectTransactionRequestVO:{}", objectTransactionRequestVO);
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
            //主键料退料事务，根据找到的主料的material_id查询到全局替代料、工单替代料、工步等
            HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
            //根据找到所有的material_id和对应工单的work_order_id、工步Id取对应的mt_work_order_component_actual.assemble_qty
            BigDecimal assembleQty = hmeNcDisposePlatformMapper.getAssembleQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
            //根据主料的bomComponentId获取其扩展属性lineAttribute5
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
                //如果assembleQty小于等于lineAttribute5，则需记两笔事务
                //申请数量的HME_WO_ISSUE_R计划内退料
                HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                //申请数量的HME_WO_ISSUE_EXT计划外投料
                WmsObjectTransactionRequestVO primaryOutAssemble = new WmsObjectTransactionRequestVO();
                BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssemble);
                primaryOutAssemble.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                primaryOutAssemble.setTransactionReasonCode("不良报废投料");
                primaryOutAssemble.setMoveType("261");
                primaryOutAssemble.setMoveReason("不良报废投料");
                primaryOutAssemble.setBomReserveNum(null);
                primaryOutAssemble.setBomReserveLineNum(null);
                primaryOutAssemble.setSoNum(null);
                primaryOutAssemble.setSoLineNum(null);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryAssembleOutTransaction.objectTransactionRequestVO:{}", primaryOutAssemble);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryOutAssemble));
            } else {
                //如果assembleQty大于lineAttribute4，则需进一步判断
                if (material.getApplyQty().compareTo(assembleQty.subtract(lineAttribute5)) == 1) {
                    //当申请数量大于(assembleQty-lineAttribute5)时，需做两笔事务，其他情况主键料不做处理
                    //申请数量-(assembleQty-lineAttribute5)的HME_WO_ISSUE_R计划内退料
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getEventId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty().subtract(assembleQty.subtract(lineAttribute5)));
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnInTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //申请数量-(assembleQty-lineAttribute5)的HME_WO_ISSUE_ EXT计划外投料
                    WmsObjectTransactionRequestVO primaryOutAssemble = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(primaryObjectTransactionRequestVO, primaryOutAssemble);
                    primaryOutAssemble.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                    primaryOutAssemble.setTransactionReasonCode("不良报废投料");
                    primaryOutAssemble.setMoveType("261");
                    primaryOutAssemble.setMoveReason("不良报废投料");
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
     * 未投料的直接报废 事务传输
     *
     * @param tenantId 租户ID
     * @param dto      参数信息
     * @param material 勾选的物料信息
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/17 11:01:28
     */
    void noScrap(Long tenantId, HmeNcDisposePlatformDTO33 dto, HmeNcDisposePlatformVO4 material) {
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getSnNumber());
        }});
        //判断勾选物料是否存在条码对应的eo下的装配清单下
        String bomComponentId = getBomComponentIdByMaterial(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
        if (StringUtils.isNotBlank(bomComponentId)) {
            //如果能找到，则在BOM内
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            if (mtBomComponent.getQty() != 0) {
                //如果数量不等于0 ，则不为替代料。根据找到的主料的material_id查询到全局替代料、工单替代料、工步等
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), material.getMaterialId(), dto.getProcessId());
                //根据所有物料的material_id和对应工单的work_order_id和工步Id取对应的mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //根据bomComponentId获取其扩展属性lineAttribute5
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
                    //如果scrapQty大于等于lineAttribute,则为申请数量的计划外投料
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
                    //如果scrapQty小于lineAttribute，需进一步判断
                    if (scrapQty.add(material.getApplyQty()).compareTo(lineAttribute) <= 0) {
                        //如果(scrapped_qty + 申请数量)小于等于lineAttribute，则为申请数量的计划内投料
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
                        //如果(scrapped_qty + 申请数量)大于lineAttribute，则需记两笔事务，一笔[(scrapped_qty + 申请数量) - (lineAttribute5 - lineAttribute4)]的计划外投料，一笔(lineAttribute5 - lineAttribute4)-scrapped_qty的计划内投料
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
                //如果组件数量等于0，则为替代料，根据替代料找到对应的主键料
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
                //替代料报废事务
                HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", mtBomComponent, hmeNcScrapDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
                //主键料报废事务 根据找到的主料的material_id查询到全局替代料、工单替代料、工步等
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
                //根据所有物料的material_id和对应工单的work_order_id和工步Id取对应的mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //根据主料的bomComponentId获取其扩展属性lineAttribute4
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
                    //如果申请数量小于等于lineAttribute5-scrapQty,则需记两笔事务，申请数量的计划外退料， 申请数量的计划内投料
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //申请数量的计划内投料
                    HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                    primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryInAssemble.setEventId(dto.getScrapTransactionId());
                    primaryInAssemble.setTransactionQty(material.getApplyQty());
                    primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                } else {
                    //如果申请数量大于lineAttribute-scrapQty,则需进一步判断
                    if(lineAttribute.compareTo(scrapQty) == 1){
                        //如果lineAttribute大于scrapQty时，则需记两笔事务,其他情况下则不作事务
                        //lineAttribute-scrapQty的HME_WO_ISSUE_R_EXT计划外退料
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.处理alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //lineAttribute-scrapQty的HME_WO_ISSUE计划内投料
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
            //如果找不到，则在BOM外,先根据替代料找到主键料
            MtBomComponent primaryBomComponent = getPrimaryMaterial(tenantId, material.getMaterialId(), mtEo, dto.getProcessId());
            if (Objects.isNull(primaryBomComponent)) {
                //如果找不到，只记一笔事务 替代料报废事务 申请数量的HME_WO_ISSUE_EXT计划外投料
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
                //替代料报废事务 申请数量的HME_WO_ISSUE_EXT计划外投料
                HmeNcDisposePlatformDTO34 hmeNcScrapDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                hmeNcScrapDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                hmeNcScrapDisposePlatformDTO34.setMaterialId(material.getMaterialId());
                hmeNcScrapDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                hmeNcScrapDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                hmeNcScrapDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                WmsObjectTransactionRequestVO objectScrapTransactionRequestVO = scrapTransaction(tenantId, "HME_WO_ISSUE_EXT", null, hmeNcScrapDisposePlatformDTO34);
                log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.scrapTransaction.objectTransactionRequestVO:{}", objectScrapTransactionRequestVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectScrapTransactionRequestVO));
                //主键料报废事务 根据找到的主料的material_id查询到全局替代料、工单替代料、工步等
                HmeNcDisposePlatformDTO35 allMaterial = getAllMaterialByPrimary(tenantId, mtEo.getEoId(), primaryBomComponent.getMaterialId(), dto.getProcessId());
                //根据所有物料的material_id和对应工单的work_order_id和工步Id取对应的mt_work_order_component_actual.scrapped_qty
                BigDecimal scrapQty = hmeNcDisposePlatformMapper.getScrapQty(tenantId, allMaterial.getAllMaterialList(), mtEo.getWorkOrderId(), allMaterial.getRouterStepId());
                //根据主料的bomComponentId获取其扩展属性lineAttribute4
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
                    //当申请数量小于等于lineAttribute-scrapQty, 则需记两笔事务
                    //申请数量的HME_WO_ISSUE_R_EXT计划外退料
                    HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                    primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                    primaryNcDisposePlatformDTO34.setTransactionQty(material.getApplyQty());
                    primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryOutReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                    //申请数量的HME_WO_ISSUE计划内投料
                    HmeNcDisposePlatformDTO34 primaryInAssemble = new HmeNcDisposePlatformDTO34();
                    primaryInAssemble.setWorkOrderId(mtEo.getWorkOrderId());
                    primaryInAssemble.setEventId(dto.getScrapTransactionId());
                    primaryInAssemble.setTransactionQty(material.getApplyQty());
                    primaryInAssemble.setMaterialLotId(material.getMaterialLotId());
                    WmsObjectTransactionRequestVO primaryInAssembleTransactionRequestVO = primaryAssembleTransaction(tenantId, "HME_WO_ISSUE", primaryBomComponent, primaryInAssemble);
                    log.info("<====== HmeNcDisposePlatformRepositoryImpl.alreadyAssembleScrap.primaryInAssembleTransaction.objectTransactionRequestVO:{}", primaryInAssembleTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryInAssembleTransactionRequestVO));
                } else {
                    //当申请数量大于lineAttribute-scrapQty, 则需进一步判断
                    if(lineAttribute.compareTo(scrapQty) == 1){
                        //如果lineAttribute大于scrapQty时，则需记两笔事务,其他情况下则不作事务
                        //lineAttribute-scrapQty的HME_WO_ISSUE_R_EXT计划外退料
                        HmeNcDisposePlatformDTO34 primaryNcDisposePlatformDTO34 = new HmeNcDisposePlatformDTO34();
                        primaryNcDisposePlatformDTO34.setWorkOrderId(mtEo.getWorkOrderId());
                        primaryNcDisposePlatformDTO34.setEventId(dto.getScrapTransactionId());
                        primaryNcDisposePlatformDTO34.setTransactionQty(lineAttribute.subtract(scrapQty));
                        primaryNcDisposePlatformDTO34.setMaterialLotId(material.getMaterialLotId());
                        WmsObjectTransactionRequestVO primaryObjectTransactionRequestVO = primaryReturnTransaction(tenantId, "HME_WO_ISSUE_R_EXT", primaryBomComponent, primaryNcDisposePlatformDTO34);
                        log.info("<====== HmeNcDisposePlatformRepositoryImpl.处理alreadyAssembleScrap.primaryReturnTransaction.objectTransactionRequestVO:{}", primaryObjectTransactionRequestVO);
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(primaryObjectTransactionRequestVO));
                        //lineAttribute-scrapQty的HME_WO_ISSUE计划内投料
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
     * Bom内主键料的退料事务  Bom外替代料的退料事务 包含计划内，计划外
     *
     * @param tenantId            租户ID
     * @param transactionTypeCode 事务类型编码
     * @param mtBomComponent      Bom组件
     * @param dto                 参数信息
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
        objectTransactionRequestVO.setTransactionReasonCode("不良退料");
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
        objectTransactionRequestVO.setMoveReason("不良退料");
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
     * Bom内主键料的投料事务  Bom外替代料的投料事务 包含计划内，计划外
     *
     * @param tenantId            租户ID
     * @param transactionTypeCode 事务类型编码
     * @param mtBomComponent      Bom组件
     * @param dto                 参数信息
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
        objectTransactionRequestVO.setTransactionReasonCode("不良报废投料");
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
        objectTransactionRequestVO.setMoveReason("不良报废投料");
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
     * Bom外主键料的退料事务 包含计划内、计划外
     *
     * @param tenantId            租户ID
     * @param transactionTypeCode 事务类型编码
     * @param mtBomComponent      Bom组件
     * @param dto                 参数信息
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
        objectTransactionRequestVO.setTransactionReasonCode("不良退料");
        objectTransactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
        //2020-01-07 edit by chaonan.hu for can.wang 仓库编码改为取配置维护中ISSUE_WAREHOUSE_CODE的值
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
        objectTransactionRequestVO.setMoveReason("不良退料");
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
     * Bom外主键料的投料事务 包含计划内、计划外
     *
     * @param tenantId            租户ID
     * @param transactionTypeCode 事务类型编码
     * @param mtBomComponent      Bom组件
     * @param dto                 参数信息
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
        objectTransactionRequestVO.setTransactionReasonCode("不良报废投料");
        objectTransactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
        //2020-01-07 edit by chaonan.hu for can.wang 仓库编码改为取配置维护中ISSUE_WAREHOUSE_CODE的值
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
        objectTransactionRequestVO.setMoveReason("不良报废投料");
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
     * BOM外 根据替代料找主料
     *
     * @param tenantId             租户ID
     * @param substituteMaterialId 替代料ID
     * @param mtEo                 eo
     * @param mtEo                 eoj
     * @param processId            工序ID
     * @return tarzan.method.domain.entity.MtBomComponent
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/16 09:49:33
     */
    MtBomComponent getPrimaryMaterial(Long tenantId, String substituteMaterialId, MtEo mtEo, String processId) {
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
        MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
            setTenantId(tenantId);
            setEoId(mtEo.getEoId());
        }});
        //根据站点Id、替代料Id关联表wms_material_substitute_rel查询SUBSTITUTE_GROUP
        List<WmsMaterialSubstituteRel> wmsMaterialSubstituteRelList = wmsMaterialSubstituteRelRepository.select(new WmsMaterialSubstituteRel() {{
            setTenantId(tenantId);
            setSiteId(mtWorkOrder.getSiteId());
            setMaterialId(substituteMaterialId);
        }});
        if (CollectionUtils.isEmpty(wmsMaterialSubstituteRelList)) {
            return null;
        } else if (wmsMaterialSubstituteRelList.size() > 1) {
            //如果找到多个，则报错{当前全局替代料有多个替代组关系，请检查！}
            throw new MtException("HME_NC_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0043", "HME"));
        }
        //根据找的到替代组Id查询其下所有的替代料
        List<String> substituteMaterialList = hmeNcDisposePlatformMapper.getSubstituteMaterialByGroup(tenantId, wmsMaterialSubstituteRelList.get(0).getSubstituteGroup());
        //根据所有的替代料Id、bomId在表mt_bom_component中查找数据
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
            //如果找不到，则返回空
            return null;
        } else if (mtBomComponentList.size() == 1) {
            //如果找到一个，则直接返回
            return mtBomComponentList.get(0);
        } else {
            //如果找到多个，则需继续判断,根据工序在表mt_operation_wkc_dispatch_rel查询operation_id
            String operationId = null;
            List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
                setTenantId(tenantId);
                setWorkcellId(processId);
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
            //根据eoId查询router_step_id
            List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, mtEo.getEoId());
            //根据多个routerStepId、operationId查询表mt_router_operation的ROUTER_OPERATION_ID
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
                //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
                throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0046", "HME"));
            } else if (mtRouterOperationList.size() > 1) {
                //如果找到多个，报错{当前工艺找到多个工艺步骤，请检查}
                throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0047", "HME"));
            }
            routerOperationId = mtRouterOperationList.get(0).getRouterOperationId();
            //根据多个bomComponentId、routerOperationId查询表mt_router_operation_component
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
                //如果找不到，则报错{所扫描条码物料与装配清单不匹配,请检查}
                throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_073", "HME"));
            } else if (mtRouterOperationComponentList.size() > 1) {
                //如果找不到，则报错{当前统一工艺步骤存在多个相同组件，请检查}
                throw new MtException("HME_NC_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0048", "HME"));
            }
            String bomComponentId = mtRouterOperationComponentList.get(0).getBomComponentId();
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            return mtBomComponent;
        }
    }

    /**
     * 根据主键料查询工单替代料、全局替代料等
     *
     * @param tenantId   租户Id
     * @param eoId       eoId
     * @param materialId 物料Id
     * @param processId  工序Id
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
            //根据工序在表mt_operation_wkc_dispatch_rel查询operation_id
            String operationId = null;
            List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
                setTenantId(tenantId);
                setWorkcellId(processId);
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
            //根据eoId查询router_step_id
            List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, eoId);
            //根据多个routerStepId、operationId查询表mt_router_operation的ROUTER_OPERATION_ID
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
                //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
                throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0046", "HME"));
            } else if (mtRouterOperationList.size() > 1) {
                //如果找到多个，报错{当前工艺找到多个工艺步骤，请检查}
                throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0047", "HME"));
            }
            routerOperationId = mtRouterOperationList.get(0).getRouterOperationId();
            //根据多个bomComponentId、routerOperationId查询表mt_router_operation_component
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
                //如果找不到，则报错{所扫描条码物料与装配清单不匹配,请检查}
                throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_073", "HME"));
            } else if (mtRouterOperationComponentList.size() > 1) {
                //如果找不到，则报错{当前统一工艺步骤存在多个相同组件，请检查}
                throw new MtException("HME_NC_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0048", "HME"));
            }
            bomComponentId = mtRouterOperationComponentList.get(0).getBomComponentId();
        }
        MtEoBom mtEoBom = mtEoBomRepository.selectOne(new MtEoBom() {{
            setTenantId(tenantId);
            setEoId(eoId);
        }});
        //根据主键料bom查询工单替代料
        List<String> woSubstituteMaterial = hmeNcDisposePlatformMapper.getWoSubstituteByPrimary(tenantId, bomComponentId, mtEoBom.getBomId());
        allMaterialList.addAll(woSubstituteMaterial);
        //根据主键料查询全局替代料
        List<String> globalSubstituteMaterial = hmeNcDisposePlatformMapper.getGlobalSubstituteByPrimary(tenantId, materialId);
        allMaterialList.addAll(globalSubstituteMaterial);
        allMaterialList = allMaterialList.stream().distinct().collect(Collectors.toList());
        result.setAllMaterialList(allMaterialList);
        String routerStepId = null;
        //根据工序在表mt_operation_wkc_dispatch_rel查询operation_id
        String operationId = null;
        List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
            setTenantId(tenantId);
            setWorkcellId(processId);
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
        //根据woId查询router_step_id
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
        List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByWo(tenantId, mtEo.getWorkOrderId());
        //根据多个routerStepId、operationId查询表mt_router_operation的ROUTER_OPERATION_ID
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
            //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
            throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0046", "HME"));
        } else if (mtRouterOperationList.size() > 1) {
            //如果找到多个，报错{当前工艺找到多个工艺步骤，请检查}
            throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0047", "HME"));
        }
        routerStepId = mtRouterOperationList.get(0).getRouterStepId();
        result.setRouterStepId(routerStepId);
        return result;
    }

    /**
     * 判断勾选物料是否存在条码对应的eo下的装配清单下
     *
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param materialId 物料Id
     * @param processId 工序Id
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
            //根据工序在表mt_operation_wkc_dispatch_rel查询operation_id
            String operationId = null;
            List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.select(new MtOperationWkcDispatchRel() {{
                setTenantId(tenantId);
                setWorkcellId(processId);
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
            //根据eoId查询router_step_id
            List<String> routerStepIdList = hmeNcDisposePlatformMapper.getRouterStepByEo(tenantId, eoId);
            //根据多个routerStepId、operationId查询表mt_router_operation的ROUTER_OPERATION_ID
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
                //如果未找到，报错{当前工艺未找到工艺步骤，请检查}
                throw new MtException("HME_NC_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0046", "HME"));
            } else if (mtRouterOperationList.size() > 1) {
                //如果找到多个，报错{当前工艺找到多个工艺步骤，请检查}
                throw new MtException("HME_NC_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0047", "HME"));
            }
            routerOperationId = mtRouterOperationList.get(0).getRouterOperationId();
            //根据多个bomComponentId、routerOperationId查询表mt_router_operation_component
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
                //如果找不到，则报错{所扫描条码物料与装配清单不匹配,请检查}
                throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_073", "HME"));
            } else if (mtRouterOperationComponentList.size() > 1) {
                //如果找不到，则报错{当前统一工艺步骤存在多个相同组件，请检查}
                throw new MtException("HME_NC_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0048", "HME"));
            }
            return mtRouterOperationComponentList.get(0).getBomComponentId();
        }
    }

    /**
     * 调用API{ onhandQtyUpdateProcess }进行库位转移
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @param applyQty 申请数量
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/7 14:43:09
     * @return void
     */
    void locatorTransfer(Long tenantId, String materialLotId, BigDecimal applyQty, String eventRequestId){
        //根据条码查询到不良虚拟货位
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
