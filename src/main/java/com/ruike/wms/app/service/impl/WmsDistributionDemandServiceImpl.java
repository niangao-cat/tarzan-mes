package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeNcDisposePlatformMapper;
import com.ruike.wms.api.dto.WmsDistDemandQueryDTO;
import com.ruike.wms.api.dto.WmsDistributionDocCreateDTO;
import com.ruike.wms.app.service.WmsDistributionDemandDetailService;
import com.ruike.wms.app.service.WmsDistributionDemandService;
import com.ruike.wms.domain.entity.WmsDistributionBasicDataProductionLine;
import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;
import com.ruike.wms.domain.repository.WmsDistributionBasicDataProductionLineRepository;
import com.ruike.wms.domain.repository.WmsDistributionDemandDetailRepository;
import com.ruike.wms.domain.repository.WmsDistributionDemandRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsDistributionDemandDetailMapper;
import com.ruike.wms.infra.mapper.WmsDistributionDemandMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.runtime.directive.Foreach;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.engine.TextElementName;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.print.DocFlavor;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.Profile.WMS_DISTRIBUTION_SIGN_FLAG;
import static java.util.Calendar.DAY_OF_MONTH;

/**
 * ???????????????????????????????????????
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 11:09:51
 */
@Service
public class WmsDistributionDemandServiceImpl implements WmsDistributionDemandService {
    private final WmsDistributionDemandRepository wmsDistributionDemandRepository;
    private final WmsDistributionDemandDetailRepository wmsDistributionDemandDetailRepository;
    private final LovAdapter lovAdapter;
    private final MtEventRepository mtEventRepository;
    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtNumrangeRepository mtNumrangeRepository;
    private final MtInstructionDocRepository instructionDocRepository;
    private final MtInstructionRepository instructionRepository;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final MtInstructionActualRepository instructionActualRepository;
    private final ProfileClient profileClient;
    private final WmsDistributionBasicDataProductionLineRepository wmsDistributionBasicDataProductionLineRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtCalendarShiftRepository mtCalendarShiftRepository;
    private final WmsDistributionDemandMapper wmsDistributionDemandMapper;
    private final MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    private final MtWorkOrderRepository mtWorkOrderRepository;
    private final WmsDistributionDemandDetailMapper wmsDistributionDemandDetailMapper;
    private final MtModOrganizationRelRepository mtModOrganizationRelRepository;
    private final HmeNcDisposePlatformMapper hmeNcDisposePlatformMapper;
    private final MtBomComponentRepository mtBomComponentRepository;
    private final WmsDistributionDemandDetailService wmsDistributionDemandDetailService;

    public WmsDistributionDemandServiceImpl(WmsDistributionDemandRepository wmsDistributionDemandRepository, WmsDistributionDemandDetailRepository wmsDistributionDemandDetailRepository, LovAdapter lovAdapter, MtEventRepository mtEventRepository
            , MtEventRequestRepository mtEventRequestRepository, MtNumrangeRepository mtNumrangeRepository, MtInstructionDocRepository instructionDocRepository, MtInstructionRepository instructionRepository, MtExtendSettingsRepository mtExtendSettingsRepository
            , MtInstructionActualRepository instructionActualRepository, ProfileClient profileClient, WmsDistributionBasicDataProductionLineRepository wmsDistributionBasicDataProductionLineRepository, MtErrorMessageRepository mtErrorMessageRepository
            , MtCalendarShiftRepository mtCalendarShiftRepository, WmsDistributionDemandMapper wmsDistributionDemandMapper, MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository, MtWorkOrderRepository mtWorkOrderRepository
            , WmsDistributionDemandDetailMapper wmsDistributionDemandDetailMapper,MtModOrganizationRelRepository mtModOrganizationRelRepository,HmeNcDisposePlatformMapper hmeNcDisposePlatformMapper,MtBomComponentRepository mtBomComponentRepository
            ,WmsDistributionDemandDetailService wmsDistributionDemandDetailService) {
        this.wmsDistributionDemandRepository = wmsDistributionDemandRepository;
        this.wmsDistributionDemandDetailRepository = wmsDistributionDemandDetailRepository;
        this.lovAdapter = lovAdapter;
        this.mtEventRepository = mtEventRepository;
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.mtNumrangeRepository = mtNumrangeRepository;
        this.instructionDocRepository = instructionDocRepository;
        this.instructionRepository = instructionRepository;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.instructionActualRepository = instructionActualRepository;
        this.profileClient = profileClient;
        this.wmsDistributionBasicDataProductionLineRepository = wmsDistributionBasicDataProductionLineRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtCalendarShiftRepository = mtCalendarShiftRepository;
        this.wmsDistributionDemandMapper = wmsDistributionDemandMapper;
        this.mtWorkOrderComponentActualRepository = mtWorkOrderComponentActualRepository;
        this.mtWorkOrderRepository = mtWorkOrderRepository;
        this.wmsDistributionDemandDetailMapper = wmsDistributionDemandDetailMapper;
        this.mtModOrganizationRelRepository = mtModOrganizationRelRepository;
        this.hmeNcDisposePlatformMapper = hmeNcDisposePlatformMapper;
        this.mtBomComponentRepository = mtBomComponentRepository;
        this.wmsDistributionDemandDetailService= wmsDistributionDemandDetailService;
    }

    @Override
    @ProcessLovValue
    public Page<WmsDistributionDemandVO> selectListByCondition(Long tenantId, WmsDistDemandQueryDTO dto, PageRequest pageRequest) {
        // ????????????????????????
        List<LovValueDTO> distributionDateList = lovAdapter.queryLovValue("WMS.DISTRIBUTION_DATE", tenantId);
        int days = 0;
        if (CollectionUtils.isNotEmpty(distributionDateList)) {
            days = Integer.parseInt(distributionDateList.get(0).getValue());
        }
        dto.setEndDate(DateUtils.addDays(dto.getStartDate(), days));
        return wmsDistributionDemandRepository.selectListByCondition(tenantId, dto, pageRequest);
    }

    /**
     * ????????????
     *
     * @param tenantId ??????
     * @return com.ruike.wms.domain.vo.WmsEventVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/1 10:06:58
     */
    private WmsEventVO createEvent(Long tenantId) {
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EventType.INSTRUCTION_DOC_CREATE);
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode(WmsConstant.EventType.INSTRUCTION_DOC_CREATE);
        eventCreate.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        WmsEventVO event = new WmsEventVO();
        event.setEventRequestId(eventRequestId);
        event.setRequestTypeCode(WmsConstant.EventType.INSTRUCTION_DOC_CREATE);
        event.setEventId(eventId);
        return event;
    }

    /**
     * ???????????????
     *
     * @param tenantId ??????
     * @param siteId   ??????
     * @return MtNumrangeVO5
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/1 10:07:11
     */
    private MtNumrangeVO5 numberRangeCreate(Long tenantId, String siteId) {
        MtNumrangeVO2 mtNumRangeCreate = new MtNumrangeVO2();
        mtNumRangeCreate.setObjectCode("INSTRUCTION_DOC_NUM");
        mtNumRangeCreate.setSiteId(siteId);
        mtNumRangeCreate.setObjectTypeCode("DISTRIBUTION_DOC");
        return mtNumrangeRepository.numrangeGenerate(tenantId, mtNumRangeCreate);
    }

    /**
     * ???????????????
     *
     * @param tenantId         ??????
     * @param instructionDocId ??????ID
     * @param instructionNum   ????????????
     * @param summary          ?????????
     * @param quantity         ??????
     * @param siteId           ??????ID
     * @param eventId          ??????ID
     * @return tarzan.instruction.domain.vo.MtInstructionVO6
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/7 07:54:27
     */
    private MtInstructionVO6 processCreateInstruction(Long tenantId, String instructionDocId, Integer instructionNum, WmsDistributionDocCreateSumVO summary, BigDecimal quantity, String siteId, String eventId) {
        // ????????????
        MtInstructionVO instructionCreate = new MtInstructionVO();
        instructionCreate.setSourceDocId(instructionDocId);
        instructionCreate.setSiteId(siteId);
        instructionCreate.setMaterialId(summary.getMaterialId());
        instructionCreate.setUomId(summary.getUomId());
        instructionCreate.setFromSiteId(siteId);
        instructionCreate.setToSiteId(siteId);
        instructionCreate.setFromLocatorId(summary.getLocatorId());
        instructionCreate.setToLocatorId(summary.getLocatorId());
        instructionCreate.setQuantity(quantity.doubleValue());
        instructionCreate.setDemandTime(summary.getDemandDate());
        instructionCreate.setShiftCode(summary.getShiftCode());
        instructionCreate.setShiftDate(summary.getShiftDate());
        instructionCreate.setBusinessType(WmsConstant.BusinessType.DISTRIBUTION_EXECUTE);
        instructionCreate.setInstructionType(WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR);
        MtInstructionVO6 instruction = instructionRepository.instructionUpdate(tenantId, instructionCreate, WmsConstant.CONSTANT_N);

        // ????????????????????????
        MtExtendVO10 extendAttr = new MtExtendVO10();
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 materialVersion = new MtExtendVO5();
        materialVersion.setAttrName("MATERIAL_VERSION");
        materialVersion.setAttrValue(summary.getMaterialVersion());
        attrs.add(materialVersion);
        MtExtendVO5 lineNum = new MtExtendVO5();
        lineNum.setAttrName("INSTRUCTION_LINE_NUM");
        lineNum.setAttrValue(String.valueOf(instructionNum));
        attrs.add(lineNum);
        MtExtendVO5 so = new MtExtendVO5();
        so.setAttrName("SO_NUM");
        so.setAttrValue(summary.getSoNum());
        attrs.add(so);
        MtExtendVO5 soLine = new MtExtendVO5();
        soLine.setAttrName("SO_LINE_NUM");
        soLine.setAttrValue(summary.getSoLineNum());
        attrs.add(soLine);
        MtExtendVO5 backFlushFlag = new MtExtendVO5();
        backFlushFlag.setAttrName("BACKFLUSH_FLAG");
        backFlushFlag.setAttrValue(WmsConstant.CONSTANT_N);
        attrs.add(backFlushFlag);
        extendAttr.setKeyId(instruction.getInstructionId());
        extendAttr.setEventId(eventId);
        extendAttr.setAttrs(attrs);
        instructionRepository.instructionAttrPropertyUpdate(tenantId, extendAttr);

        // ??????????????????
        MtInstructionActualVO actualCreate = new MtInstructionActualVO();
        actualCreate.setInstructionId(instruction.getInstructionId());
        actualCreate.setEventId(eventId);
        actualCreate.setActualQty(0.0);
        actualCreate.setMaterialId(summary.getMaterialId());
        actualCreate.setTenantId(tenantId);
        actualCreate.setFromLocatorId(summary.getLocatorId());
        actualCreate.setToLocatorId(summary.getLocatorId());
        actualCreate.setBusinessType(WmsConstant.BusinessType.DISTRIBUTION_EXECUTE);
        actualCreate.setInstructionType(WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR);
        actualCreate.setUomId(summary.getUomId());
        actualCreate.setFromSiteId(siteId);
        actualCreate.setSourceOrderType(WmsConstant.DocType.DISTRIBUTION_DOC);
        actualCreate.setSourceOrderId(instructionDocId);
        instructionActualRepository.instructionActualUpdate(tenantId, actualCreate);

        return instruction;
    }

    /**
     * ????????????????????????
     *
     * @param tenantId         ??????
     * @param instructionDocId ??????ID
     * @param eventId          ??????ID
     * @param detail           ?????????
     * @param signFlagProfile  ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/7 07:55:26
     */
    private void updateDocExtendAttr(Long tenantId, String instructionDocId, String eventId, WmsDistDemandCreateVO detail, String signFlagProfile) {
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 signFlag = new MtExtendVO5();
        signFlag.setAttrName("SIGN_FLAG");
        signFlag.setAttrValue(signFlagProfile);
        attrs.add(signFlag);
        MtExtendVO5 prodLine = new MtExtendVO5();
        prodLine.setAttrName("PROD_LINE_CODE");
        prodLine.setAttrValue(detail.getProdLineCode());
        attrs.add(prodLine);
        MtExtendVO5 workcell = new MtExtendVO5();
        workcell.setAttrName("WORKCELL");
        workcell.setAttrValue(detail.getWorkcellCode());
        attrs.add(workcell);
        MtExtendVO5 locator = new MtExtendVO5();
        locator.setAttrName("TO_LOCATOR_ID");
        locator.setAttrValue(detail.getLocatorId());
        attrs.add(locator);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", instructionDocId, eventId, attrs);
    }

    /**
     * ????????????
     *
     * @param tenantId   ??????ID
     * @param event      ??????
     * @param siteId     ??????ID
     * @param demandList ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/7 07:55:50
     */
    private void instructionDocCreate(Long tenantId, WmsEventVO event, String siteId, List<WmsDistDemandCreateVO> demandList, String signFlagProfile) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        // ??????????????????
        MtInstructionDocDTO2 insDocCreate = new MtInstructionDocDTO2();
        insDocCreate.setSiteId(siteId);
        insDocCreate.setInstructionDocType(WmsConstant.DocType.DISTRIBUTION_DOC);
        insDocCreate.setDemandTime(demandList.get(0).getDemandDate());
        insDocCreate.setPersonId(userDetails.getUserId());
        insDocCreate.setEventRequestId(event.getEventRequestId());
        insDocCreate.setInstructionDocStatus(WmsConstant.InstructionStatus.NEW);
        MtInstructionDocVO3 instructionDoc = instructionDocRepository.instructionDocUpdate(tenantId, insDocCreate, WmsConstant.CONSTANT_N);

        // ??????????????????????????????
        this.updateDocExtendAttr(tenantId, instructionDoc.getInstructionDocId(), event.getEventId(), demandList.get(0), signFlagProfile);

        Map<WmsDistributionDocCreateSumVO, List<WmsDistDemandCreateVO>> map = demandList.stream().collect(Collectors.groupingBy(WmsDistDemandCreateVO::summaryCreate));
        int instructionSeq = 0;
        for (Map.Entry<WmsDistributionDocCreateSumVO, List<WmsDistDemandCreateVO>> entry : map.entrySet()) {
            WmsDistributionDocCreateSumVO summary = entry.getKey();
            List<WmsDistDemandCreateVO> list = entry.getValue();
            instructionSeq += 10;
            BigDecimal quantity = list.stream().map(WmsDistDemandCreateVO::getRequirementQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            // ?????????????????????
            MtInstructionVO6 instruction = this.processCreateInstruction(tenantId, instructionDoc.getInstructionDocId(), instructionSeq, summary, quantity, siteId, event.getEventId());
            // ????????????ID???????????????
            List<String> detailIdList = list.stream().map(WmsDistDemandCreateVO::getDemandDetailId).collect(Collectors.toList());
            List<WmsDistributionDemandDetail> detailList = wmsDistributionDemandDetailRepository.selectListByDetailIdList(tenantId, detailIdList);
            detailList.forEach(rec -> {
                rec.setInstructionDocId(instructionDoc.getInstructionDocId());
                rec.setInstructionId(instruction.getInstructionId());
            });
            detailList.forEach(wmsDistributionDemandDetailRepository::updateByPrimaryKey);
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     * @param tenantId
     * @param dto
     * @author yifan.xiong@hand-china.com 2021-3-16 17:05:33
     */
    private void distributionDocCreateOld(Long tenantId, WmsDistributionDocCreateDTO dto){
        List<String> distDemandIdList = new ArrayList<>();
        for (WmsDistributionDemandVO demand : dto.getDemandList()) {
            demand.getShiftQtyList().forEach(rec -> {
                if (dto.getCalendarShiftList().stream().anyMatch(cal -> cal.getShiftCode().equals(rec.getShiftCode()) && cal.getShiftDate().equals(rec.getShiftDate())) && StringUtils.isNotBlank(rec.getCalendarShiftId())) {
                    distDemandIdList.add(rec.getDistDemandId());
                }
            });
        }
        List<WmsDistDemandCreateVO> createList = wmsDistributionDemandDetailRepository.selectCreateListByDemandIdList(tenantId, distDemandIdList);
        // ??????
        if (CollectionUtils.isEmpty(createList)) {
            throw new CommonException("????????????????????????????????????");
        }
        createList.forEach(rec -> {
            if (Objects.isNull(rec.getLocatorId())) {
                throw new CommonException("?????????" + rec.getWorkcellCode() + "???????????????????????????");
            }
        });

        // ????????????
        WmsEventVO event = this.createEvent(tenantId);
        String signFlagProfile = Optional.ofNullable(profileClient.getProfileValueByOptions(WMS_DISTRIBUTION_SIGN_FLAG)).orElse(WmsConstant.CONSTANT_Y);
        dto.getCalendarShiftList().forEach(calendarShift -> {
            List<WmsDistDemandCreateVO> docList = createList.stream().filter(rec -> rec.getShiftCode().equals(calendarShift.getShiftCode()) && rec.getShiftDate().equals(calendarShift.getShiftDate())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(docList)) {
                // ??????????????????
                Map<String, List<WmsDistDemandCreateVO>> docMap = docList.stream().collect(Collectors.groupingBy(WmsDistDemandCreateVO::getWorkcellId));
                docMap.forEach((workcellId, list) -> {
                    String siteId = list.get(0).getSiteId();
                    // ???????????????????????????
                    this.instructionDocCreate(tenantId, event, siteId, list, signFlagProfile);
                });
            }
        });
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param tenantId         ??????
     * @param instructionDocId ??????ID
     * @param instructionNum   ????????????
     * @param summary          ?????????
     * @param quantity         ??????
     * @param siteId           ??????ID
     * @param eventId          ??????ID
     * @return tarzan.instruction.domain.vo.MtInstructionVO6
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/7 07:54:27
     */
    private MtInstructionVO6 processCreateInstructionNew(Long tenantId, String instructionDocId, Integer instructionNum, WmsDistDemandOffsetVO summary, BigDecimal quantity, String siteId, String eventId) {
        // ????????????
        MtInstructionVO instructionCreate = new MtInstructionVO();
        instructionCreate.setSourceDocId(instructionDocId);
        instructionCreate.setSiteId(siteId);
        instructionCreate.setMaterialId(summary.getMaterialId());
        instructionCreate.setUomId(summary.getUomId());
        instructionCreate.setFromSiteId(siteId);
        instructionCreate.setToSiteId(siteId);
        instructionCreate.setFromLocatorId(summary.getLocatorId());
        instructionCreate.setToLocatorId(summary.getLocatorId());
        instructionCreate.setQuantity(quantity.doubleValue());
        instructionCreate.setDemandTime(summary.getDemandDate());
        instructionCreate.setShiftCode(summary.getShiftCode());
        instructionCreate.setShiftDate(summary.getShiftDate());
        instructionCreate.setBusinessType(WmsConstant.BusinessType.DISTRIBUTION_EXECUTE);
        instructionCreate.setInstructionType(WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR);
        MtInstructionVO6 instruction = instructionRepository.instructionUpdate(tenantId, instructionCreate, WmsConstant.CONSTANT_N);

        // ????????????????????????
        MtExtendVO10 extendAttr = new MtExtendVO10();
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 materialVersion = new MtExtendVO5();
        materialVersion.setAttrName("MATERIAL_VERSION");
        materialVersion.setAttrValue(summary.getMaterialVersion());
        attrs.add(materialVersion);
        MtExtendVO5 lineNum = new MtExtendVO5();
        lineNum.setAttrName("INSTRUCTION_LINE_NUM");
        lineNum.setAttrValue(String.valueOf(instructionNum));
        attrs.add(lineNum);
        MtExtendVO5 so = new MtExtendVO5();
        so.setAttrName("SO_NUM");
        so.setAttrValue(summary.getSoNum());
        attrs.add(so);
        MtExtendVO5 soLine = new MtExtendVO5();
        soLine.setAttrName("SO_LINE_NUM");
        soLine.setAttrValue(summary.getSoLineNum());
        attrs.add(soLine);
        MtExtendVO5 backFlushFlag = new MtExtendVO5();
        backFlushFlag.setAttrName("BACKFLUSH_FLAG");
        backFlushFlag.setAttrValue(WmsConstant.CONSTANT_Y);
        attrs.add(backFlushFlag);
        extendAttr.setKeyId(instruction.getInstructionId());
        extendAttr.setEventId(eventId);
        extendAttr.setAttrs(attrs);
        instructionRepository.instructionAttrPropertyUpdate(tenantId, extendAttr);

        // ??????????????????
        MtInstructionActualVO actualCreate = new MtInstructionActualVO();
        actualCreate.setInstructionId(instruction.getInstructionId());
        actualCreate.setEventId(eventId);
        actualCreate.setActualQty(0.0);
        actualCreate.setMaterialId(summary.getMaterialId());
        actualCreate.setTenantId(tenantId);
        actualCreate.setFromLocatorId(summary.getLocatorId());
        actualCreate.setToLocatorId(summary.getLocatorId());
        actualCreate.setBusinessType(WmsConstant.BusinessType.DISTRIBUTION_EXECUTE);
        actualCreate.setInstructionType(WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR);
        actualCreate.setUomId(summary.getUomId());
        actualCreate.setFromSiteId(siteId);
        actualCreate.setSourceOrderType(WmsConstant.DocType.DISTRIBUTION_DOC);
        actualCreate.setSourceOrderId(instructionDocId);
        instructionActualRepository.instructionActualUpdate(tenantId, actualCreate);

        return instruction;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param tenantId         ??????
     * @param instructionDocId ??????ID
     * @param eventId          ??????ID
     * @param detail           ?????????
     * @param signFlagProfile  ????????????
     * @author yifan.xiong@hand-china.com 2021-3-18 16:00:29
     */
    private void updateDocExtendAttrNew(Long tenantId, String instructionDocId, String eventId, WmsDistDemandOffsetVO detail, String signFlagProfile) {
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 signFlag = new MtExtendVO5();
        signFlag.setAttrName("SIGN_FLAG");
        signFlag.setAttrValue(signFlagProfile);
        attrs.add(signFlag);
        MtExtendVO5 prodLine = new MtExtendVO5();
        prodLine.setAttrName("PROD_LINE_CODE");
        prodLine.setAttrValue(detail.getProdLineCode());
        attrs.add(prodLine);
        MtExtendVO5 workcell = new MtExtendVO5();
        workcell.setAttrName("WORKCELL");
        workcell.setAttrValue(detail.getWorkcellCode());
        attrs.add(workcell);
        MtExtendVO5 locator = new MtExtendVO5();
        locator.setAttrName("TO_LOCATOR_ID");
        locator.setAttrValue(detail.getLocatorId());
        attrs.add(locator);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", instructionDocId, eventId, attrs);
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param tenantId   ??????ID
     * @param event      ??????
     * @param siteId     ??????ID
     * @param demandList ????????????
     * @author yifan.xiong@hand-china.com 2021-3-18 15:58:45
     */
    private void instructionDocCreateNew(Long tenantId, WmsEventVO event, String siteId, List<WmsDistDemandOffsetVO> demandList, String signFlagProfile) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        // ??????????????????
        MtInstructionDocDTO2 insDocCreate = new MtInstructionDocDTO2();
        insDocCreate.setSiteId(siteId);
        insDocCreate.setInstructionDocType(WmsConstant.DocType.DISTRIBUTION_DOC);
        insDocCreate.setDemandTime(demandList.get(0).getDemandDate());
        insDocCreate.setPersonId(userDetails.getUserId());
        insDocCreate.setEventRequestId(event.getEventRequestId());
        insDocCreate.setInstructionDocStatus(WmsConstant.InstructionStatus.NEW);
        MtInstructionDocVO3 instructionDoc = instructionDocRepository.instructionDocUpdate(tenantId, insDocCreate, WmsConstant.CONSTANT_N);

        // ??????????????????????????????
        this.updateDocExtendAttrNew(tenantId, instructionDoc.getInstructionDocId(), event.getEventId(), demandList.get(0), signFlagProfile);

        int instructionSeq = 0;
        for (WmsDistDemandOffsetVO vo : demandList) {
            if(vo.getInstructionQty().compareTo(BigDecimal.ZERO)>0) {
                instructionSeq += 10;
                BigDecimal quantity = vo.getInstructionQty();
                // ?????????????????????
                MtInstructionVO6 instruction = this.processCreateInstructionNew(tenantId, instructionDoc.getInstructionDocId(), instructionSeq, vo, quantity, siteId, event.getEventId());
                // ????????????ID????????????????????????????????????
                List<String> detailIdList = vo.getDemandDetailIdList();
                List<WmsDistributionDemandDetail> detailList = wmsDistributionDemandDetailRepository.selectListByDetailIdList(tenantId, detailIdList);
                detailList.forEach(rec -> {
                    rec.setInstructionDocId(instructionDoc.getInstructionDocId());
                    rec.setInstructionId(instruction.getInstructionId());
                    rec.setAttribute1(WmsConstant.CONSTANT_Y);
                    rec.setAttribute2(vo.getInstockQty().toString());
                    rec.setAttribute3(vo.getRequirementQty().toString());
                    rec.setAttribute4(vo.getOffsetQty().toString());
                    rec.setAttribute5(vo.getInstructionQty().toString());
                    rec.setAttribute6(vo.getActualReplaceQty().toString());
                    rec.setAttribute7(vo.getWorkshopDemandQty().toString());
                });
                detailList.forEach(wmsDistributionDemandDetailRepository::updateByPrimaryKey);
            }else{
                // ??????????????????????????????????????????
                List<String> detailIdList = vo.getDemandDetailIdList();
                List<WmsDistributionDemandDetail> detailList = wmsDistributionDemandDetailRepository.selectListByDetailIdList(tenantId, detailIdList);
                detailList.forEach(rec -> {
                    rec.setAttribute1(WmsConstant.CONSTANT_N);
                    rec.setAttribute2(vo.getInstockQty().toString());
                    rec.setAttribute3(vo.getRequirementQty().toString());
                    rec.setAttribute4(vo.getOffsetQty().toString());
                    rec.setAttribute5(vo.getInstructionQty().toString());
                    rec.setAttribute6(vo.getActualReplaceQty().toString());
                    rec.setAttribute7(vo.getWorkshopDemandQty().toString());
                });
                detailList.forEach(wmsDistributionDemandDetailRepository::updateByPrimaryKey);
            }
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     * @param tenantId
     * @param dto
     * @author yifan.xiong@hand-china.com 2021-3-16 17:05:33
     */
    private void distributionDocCreateNew(Long tenantId, WmsDistributionDocCreateDTO dto,Date tomorrow){
        List<String> distDemandIdList = new ArrayList<>();
        Map<String,String> workcellIdMap=new HashMap<>();
        //????????????????????????
        for (WmsDistributionDemandVO demand : dto.getDemandList()) {
            demand.getShiftQtyList().forEach(rec -> {
                if (dto.getCalendarShiftList().stream().anyMatch(cal -> cal.getShiftDate().equals(rec.getShiftDate())) && StringUtils.isNotBlank(rec.getCalendarShiftId())) {
                    distDemandIdList.add(rec.getDistDemandId());
                    workcellIdMap.put(rec.getDistDemandId(),demand.getWorkcellId());
                }
            });
        }
        //wmsDistributionDemandDetailRepository.
        //????????????????????????
        for (String distDemandId:
        distDemandIdList) {
            List<WmsDistributionDemandDetail> wmsDistributionDemandDetails = wmsDistributionDemandDetailRepository.selectListByDemandId(tenantId, distDemandId);
            if (!CollectionUtils.isEmpty(wmsDistributionDemandDetails)) {

                List<String> materialIds = wmsDistributionDemandDetailRepository.getSubstitute(tenantId, distDemandId, wmsDistributionDemandDetails.get(0).getWorkOrderId());
                List<WmsDistributionDemandDetail> collect = wmsDistributionDemandDetails.stream().filter(t -> t.getSubstituteFlag().equals(WmsConstant.CONSTANT_N)).collect(Collectors.toList());
                List<String> materialIdsLast = materialIds.stream().filter(t ->
                        !wmsDistributionDemandDetails.stream().map(WmsDistributionDemandDetail::getMaterialId).collect(Collectors.toList()).contains(t)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(materialIdsLast)) {
                    materialIdsLast.forEach(materialId -> {
                        WmsDistributionDemandDetail wmsDistributionDemandDetail = new WmsDistributionDemandDetail();
                        wmsDistributionDemandDetail.setTenantId(collect.get(0).getTenantId());
                        wmsDistributionDemandDetail.setDistDemandId(collect.get(0).getDistDemandId());
                        wmsDistributionDemandDetail.setMaterialId(materialId);
                        wmsDistributionDemandDetail.setWorkOrderId(collect.get(0).getWorkOrderId());
                        wmsDistributionDemandDetail.setDispatchQty(collect.get(0).getDispatchQty());
                        wmsDistributionDemandDetail.setUsageQty(collect.get(0).getUsageQty());
                        wmsDistributionDemandDetail.setRequirementQty(BigDecimal.ZERO);
                        wmsDistributionDemandDetail.setSubstituteFlag(WmsConstant.CONSTANT_Y);
                        wmsDistributionDemandDetail.setInstructionQty(collect.get(0).getInstructionQty());
                        wmsDistributionDemandDetail.setInstructionDocId(collect.get(0).getInstructionDocId());
                        wmsDistributionDemandDetail.setInstructionId(collect.get(0).getInstructionId());
                        wmsDistributionDemandDetail.setDemandRecordId(collect.get(0).getDemandRecordId());
                        wmsDistributionDemandDetail.setSourceDetailId(collect.get(0).getDemandDetailId());
                        wmsDistributionDemandDetail.setComponentDemandQty(collect.get(0).getComponentDemandQty());
                        String workcellId = workcellIdMap.get(distDemandId);
                        wmsDistributionDemandDetail.setWorkcellId(workcellId);
                        wmsDistributionDemandDetailService.insertOrUpdateSubstitution(tenantId, wmsDistributionDemandDetail);

                    });
                }
            }

        }

        List<WmsDistDemandCreateVO> createList = wmsDistributionDemandDetailRepository.selectCreateListByDemandIdListNew(tenantId, distDemandIdList);
        // ??????
        if (CollectionUtils.isEmpty(createList)) {
            throw new CommonException("????????????????????????????????????");
        }

        //???????????????????????????????????????
        createList.forEach(rec -> {
            if (Objects.isNull(rec.getLocatorId())) {
                throw new CommonException("?????????" + rec.getWorkcellCode() + "???????????????????????????");
            }
            MtCalendarShift mtCalendarShift = new MtCalendarShift();
            mtCalendarShift.setTenantId(tenantId);
            mtCalendarShift.setShiftDate(rec.getShiftDate());
            List<MtCalendarShift> shiftList = mtCalendarShiftRepository.select(mtCalendarShift);
            shiftList = shiftList.stream().sorted(Comparator.comparing(MtCalendarShift::getShiftStartTime)).collect(Collectors.toList());
            rec.setDemandDate(shiftList.get(0).getShiftStartTime());
            rec.setCalendarShiftId(shiftList.get(0).getCalendarShiftId());
            rec.setShiftCode(shiftList.get(0).getShiftCode());
        });

        //?????????????????????
        List<WmsDistDemandOffsetVO> resultCreateVO = new ArrayList<>();
        //????????????????????????
//        List<WmsDistDemandCreateVO> details = wmsDistributionDemandDetailMapper.selectAllCreateList(tenantId);
//        List<MtWorkOrderComponentActual> componentActuals = mtWorkOrderComponentActualRepository.selectAll();
        //?????????????????????????????????????????????
        //????????????????????????N?????????????????????????????????
        Map<WmsDistributionDocCreateSumVO, List<WmsDistDemandCreateVO>> mainMap = createList.stream()
                .filter(a -> WmsConstant.CONSTANT_N.equals(a.getSubstituteFlag()))
                .collect(Collectors.groupingBy(WmsDistDemandCreateVO::summaryCreate));
        // ?????????????????????????????????????????????
        Set<WmsDistributionDocCreateSumVO> mainKeyList = mainMap.keySet();
        Map<String, List<WmsDistributionQtyVO>> wmsDistributionQtyMap = new HashMap<>();
        List<String> workOrderList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mainKeyList)) {
            List<WmsDistributionQtyVO> wmsDistributionQtyVOS = wmsDistributionDemandMapper.summaryMaterialDispatchQty(tenantId, mainKeyList);
            wmsDistributionQtyMap = wmsDistributionQtyVOS.stream().collect(Collectors.groupingBy(vo -> vo.getMaterialId()));
            // ??????????????????
            workOrderList = wmsDistributionDemandMapper.queryWorkOrderIdByDemandList(tenantId, mainKeyList);
        }
        // ????????????????????????
        List<String> wkcShiftIdList = wmsDistributionDemandMapper.queryCurrentWkcShift(tenantId);

        Map<String, List<WmsDistributionQtyVO>> releaseQtyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(wkcShiftIdList) && CollectionUtils.isNotEmpty(workOrderList)) {
            List<String> jobIdList = wmsDistributionDemandMapper.queryJobIdList(tenantId, wkcShiftIdList, workOrderList);
            List<String> materialIdList = mainKeyList.stream().map(WmsDistributionDocCreateSumVO::getMaterialId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(jobIdList)) {
                // ????????????????????????
                List<WmsDistributionQtyVO> wmsDistributionQtyVOS = wmsDistributionDemandMapper.queryReleaseQtyByMaterialAndJob(tenantId, jobIdList, materialIdList);
                releaseQtyMap = wmsDistributionQtyVOS.stream().collect(Collectors.groupingBy(WmsDistributionQtyVO::getMaterialId));
            }
        }
        for (Map.Entry<WmsDistributionDocCreateSumVO, List<WmsDistDemandCreateVO>> entry : mainMap.entrySet()) {
            WmsDistDemandOffsetVO offsetVO = new WmsDistDemandOffsetVO();
            WmsDistributionDocCreateSumVO summary = entry.getKey();
            List<WmsDistDemandCreateVO> list = entry.getValue();

            //???????????????
            BigDecimal quantity = list.stream().map(WmsDistDemandCreateVO::getRequirementQty).reduce(BigDecimal.ZERO, BigDecimal::add);
//            Date today = DateUtils.truncate(new Date(), Calendar.DATE);

//            //????????????
//            List<WmsDistDemandCreateVO> workIds = details.stream()
//                    .filter(a->a.getMaterialId().equals(summary.getMaterialId())&&a.getWorkcellId().equals(summary.getWorkcellId())
//                            &&StringUtils.equals(summary.getMaterialVersion(),a.getMaterialVersion())&&a.getDemandDate().compareTo(today)<0)
//                    .collect(Collectors.toList());
//            //??????????????????????????????????????????
//            BigDecimal dispatchQty = workIds.stream().map(WmsDistDemandCreateVO::getRequirementQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            //????????????
            // ?????????????????????????????????????????? = ??????????????? - ?????????????????????
            // ???????????????
            List<WmsDistributionQtyVO> dispatchQtyList = wmsDistributionQtyMap.getOrDefault(summary.getMaterialId(), new ArrayList<>());
            BigDecimal dispatchQty = CollectionUtils.isNotEmpty(dispatchQtyList) ? dispatchQtyList.get(0).getQuantity() : BigDecimal.ZERO;
            // ?????????????????????
            List<WmsDistributionQtyVO> releaseQtyList = releaseQtyMap.getOrDefault(summary.getMaterialId(), new ArrayList<>());
            BigDecimal releaseQty = releaseQtyList.stream().map(WmsDistributionQtyVO::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal workshopDemandQty = dispatchQty.subtract(releaseQty);
//            List<MtWorkOrderComponentActual> assembleQtys = new ArrayList<>();
//            List<String> workOrderIds = list.stream().map(WmsDistDemandCreateVO::getWorkOrderId).distinct().collect(Collectors.toList());
//            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
//            mtModOrganizationVO2.setTopSiteId(list.get(0).getSiteId());
//            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
//            mtModOrganizationVO2.setParentOrganizationId(summary.getWorkcellId());
//            mtModOrganizationVO2.setOrganizationType("WORKCELL");
//            mtModOrganizationVO2.setQueryType("TOP");
//            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
//            List<String> workcellIdList = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
//            for(String workcellId:workcellIdList){
//                String operationId = hmeNcDisposePlatformMapper.getOperationId(tenantId,workcellId);
//                for(String workOrderId:workOrderIds) {
//                    MtWorkOrder mtWorkOrder = new MtWorkOrder() {{
//                        setWorkOrderId(workOrderId);
//                    }};
//                    mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtWorkOrder);
//                    MtBomComponent mtBomComponent = new MtBomComponent();
//                    mtBomComponent.setBomId(mtWorkOrder.getBomId());
//                    mtBomComponent.setMaterialId(summary.getMaterialId());
//                    List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(mtBomComponent);
//                    List<String> bomComponentIds = mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).distinct().collect(Collectors.toList());
//                    for(String bomComponentId:bomComponentIds) {
//                        List<MtWorkOrderComponentActual> actuals = componentActuals.stream()
//                                .filter(a ->StringUtils.equals(a.getWorkOrderId(),workOrderId)&&StringUtils.equals(a.getBomComponentId(),bomComponentId)&&StringUtils.equals(operationId,a.getOperationId()))
//                                .collect(Collectors.toList());
//                        assembleQtys.addAll(actuals);
//                    }
//                }
//            }
//            List<BigDecimal> bigDecimalAssembleQtys = new ArrayList<>();
//            assembleQtys.forEach(a ->{
//                bigDecimalAssembleQtys.add(BigDecimal.valueOf(a.getAssembleQty()));
//            });
//            BigDecimal assembleQty = bigDecimalAssembleQtys.stream().reduce(BigDecimal.ZERO,BigDecimal::add);
//            BigDecimal workshopDemandQty = dispatchQty.subtract(assembleQty);

            //??????????????????ID
            List<String> demandDetailIdList = list.stream().map(WmsDistDemandCreateVO::getDemandDetailId).collect(Collectors.toList());
            //????????????????????????ID
            List<String> demandIdList = list.stream().map(WmsDistDemandCreateVO::getDistDemandId).collect(Collectors.toList());

            //??????????????????
            WmsDistributionDemandQueryVO vo = new WmsDistributionDemandQueryVO();
            vo.setSoLineNum(summary.getSoLineNum());
            vo.setSoNum(summary.getSoNum());
            vo.setMaterialId(summary.getMaterialId());
            vo.setMaterialVersion(summary.getMaterialVersion());
            vo.setSiteId(list.get(0).getSiteId());
            vo.setWorkcellId(summary.getWorkcellId());
            BigDecimal inStockQty = wmsDistributionDemandMapper.selectInStockByDemand(tenantId,vo);

            BeanUtils.copyProperties(summary,offsetVO);
            offsetVO.setWorkcellCode(list.get(0).getWorkcellCode());
            offsetVO.setProdLineCode(list.get(0).getProdLineCode());
            offsetVO.setSiteId(list.get(0).getSiteId());
            offsetVO.setDemandDetailIdList(demandDetailIdList);
            offsetVO.setDistDemandIdList(demandIdList);
            offsetVO.setSubstituteFlag(list.get(0).getSubstituteFlag());
            offsetVO.setRequirementQty(quantity);
            offsetVO.setInstockQty(inStockQty);
            offsetVO.setWorkshopDemandQty(workshopDemandQty);

            //????????????????????????????????????????????????????????????????????????
            if(quantity.add(workshopDemandQty).compareTo(inStockQty) > -1){
                offsetVO.setOffsetQty(inStockQty);//?????????????????????????????????
                offsetVO.setReplaceQty(BigDecimal.ZERO);//??????????????????0
                offsetVO.setActualReplaceQty(BigDecimal.ZERO);//?????????????????????0
                offsetVO.setInstructionQty(quantity.add(workshopDemandQty).subtract(inStockQty));//??????????????????????????????????????????????????????????????????????????????
            }
            //??????????????????????????????????????????
            else{
                offsetVO.setOffsetQty(quantity.add(workshopDemandQty));//????????????????????????????????????????????????
                offsetVO.setReplaceQty(inStockQty.subtract(quantity).subtract(workshopDemandQty));//???????????????????????????????????????????????????????????????????????????
                offsetVO.setActualReplaceQty(BigDecimal.ZERO);//?????????????????????0
                offsetVO.setInstructionQty(BigDecimal.ZERO);//??????????????????0
            }

            //??????????????????DistDemandId????????????????????????????????????????????????
            List<String> detailIdList = list.stream().map(e -> e.getDistDemandId()).collect(Collectors.toList());
            Map<WmsDistributionDocCreateSumVO, List<WmsDistDemandCreateVO>> replaceMap = createList.stream()
                    .filter(a -> WmsConstant.CONSTANT_Y.equals(a.getSubstituteFlag())&&detailIdList.contains(a.getDistDemandId()))
                    .collect(Collectors.groupingBy(WmsDistDemandCreateVO::summaryCreate));
            List<WmsDistDemandOffsetVO> replaceOffsetList = new ArrayList<>();
            for (Map.Entry<WmsDistributionDocCreateSumVO, List<WmsDistDemandCreateVO>> replaceEntry : replaceMap.entrySet()) {
                WmsDistDemandOffsetVO replaceOffsetVO = new WmsDistDemandOffsetVO();
                WmsDistributionDocCreateSumVO replaceSummary = replaceEntry.getKey();
                List<WmsDistDemandCreateVO> replaceList = replaceEntry.getValue();

                //????????????
                BigDecimal replaceQuantity = replaceList.stream().map(WmsDistDemandCreateVO::getRequirementQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                //??????????????????ID
                List<String> replaceDemandDetailIdList = replaceList.stream().map(WmsDistDemandCreateVO::getDemandDetailId).collect(Collectors.toList());
                //????????????????????????ID
                List<String> replaceDemandIdList = replaceList.stream().map(WmsDistDemandCreateVO::getDistDemandId).collect(Collectors.toList());

                //??????????????????????????????????????????
                List<WmsDistDemandOffsetVO> instockQtyList = resultCreateVO.stream()
                        .filter(a -> a.getSoLineNum().equals(replaceSummary.getSoLineNum())&&a.getSoNum().equals(replaceSummary.getSoNum())&&a.getMaterialId().equals(replaceSummary.getMaterialId())
                                &&(a.getMaterialVersion().equals(replaceSummary.getMaterialVersion())||(StringUtils.isEmpty(a.getMaterialVersion())&&StringUtils.isEmpty(replaceSummary.getMaterialVersion())))
                                &&a.getSiteId().equals(replaceList.get(0).getSiteId())&&a.getWorkcellId().equals(replaceSummary.getWorkcellId()))
                        .sorted(Comparator.comparing(WmsDistDemandOffsetVO::getReplaceQty)).collect(Collectors.toList());

                //??????????????????????????????
                if(CollectionUtils.isNotEmpty(instockQtyList)){
                    resultCreateVO.removeAll(instockQtyList);
                    BeanUtils.copyProperties(instockQtyList.get(0),replaceOffsetVO);
                    replaceOffsetVO.getDemandDetailIdList().addAll(replaceDemandDetailIdList);
                    replaceOffsetVO.getDistDemandIdList().addAll(replaceDemandIdList);
                    //????????????
                    replaceOffsetVO.setRequirementQty(replaceOffsetVO.getRequirementQty().add(replaceQuantity));

                    //?????????????????????????????????????????????
                    if(replaceQuantity.compareTo(replaceOffsetVO.getReplaceQty()) > -1){
                        replaceOffsetVO.setOffsetQty(replaceOffsetVO.getOffsetQty().add(replaceOffsetVO.getReplaceQty()));//???????????????????????????????????????????????????
                        replaceOffsetVO.setInstructionQty(replaceOffsetVO.getInstructionQty().add(replaceOffsetVO.getReplaceQty()));//?????????????????????????????????????????????????????????
                        replaceOffsetVO.setReplaceQty(BigDecimal.ZERO);//??????????????????0
                    }
                    //????????????????????????????????????
                    else{
                        replaceOffsetVO.setOffsetQty(replaceOffsetVO.getOffsetQty().add(replaceQuantity));//????????????????????????????????????????????????
                        replaceOffsetVO.setReplaceQty(replaceOffsetVO.getReplaceQty().subtract(replaceQuantity));//??????????????????????????????????????????????????????
                    }
                }
                else{
                    BeanUtils.copyProperties(replaceSummary,replaceOffsetVO);
                    replaceOffsetVO.setWorkcellCode(replaceList.get(0).getWorkcellCode());
                    replaceOffsetVO.setProdLineCode(replaceList.get(0).getProdLineCode());
                    replaceOffsetVO.setSiteId(replaceList.get(0).getSiteId());
                    replaceOffsetVO.setDemandDetailIdList(replaceDemandDetailIdList);
                    replaceOffsetVO.setDistDemandIdList(replaceDemandIdList);
                    replaceOffsetVO.setSiteId(replaceList.get(0).getSiteId());
                    replaceOffsetVO.setSubstituteFlag(replaceList.get(0).getSubstituteFlag());
                    replaceOffsetVO.setRequirementQty(replaceQuantity);
                    //????????????
                    WmsDistributionDemandQueryVO replaceVo = new WmsDistributionDemandQueryVO();
                    replaceVo.setSoLineNum(replaceSummary.getSoLineNum());
                    replaceVo.setSoNum(replaceSummary.getSoNum());
                    replaceVo.setMaterialId(replaceSummary.getMaterialId());
                    replaceVo.setMaterialVersion(replaceSummary.getMaterialVersion());
                    replaceVo.setSiteId(replaceList.get(0).getSiteId());
                    replaceVo.setWorkcellId(replaceSummary.getWorkcellId());
                    BigDecimal replaceInStockQty = wmsDistributionDemandMapper.selectInStockByDemand(tenantId, replaceVo);
                    replaceOffsetVO.setInstockQty(replaceInStockQty);
                    replaceOffsetVO.setWorkshopDemandQty(BigDecimal.ZERO);

                    //???????????????????????????????????????????????????
                    if(replaceQuantity.compareTo(replaceInStockQty) > -1){
                        replaceOffsetVO.setOffsetQty(replaceInStockQty);//?????????????????????????????????
                        replaceOffsetVO.setReplaceQty(BigDecimal.ZERO);//??????????????????0
                        replaceOffsetVO.setActualReplaceQty(BigDecimal.ZERO);//?????????????????????0
                        replaceOffsetVO.setInstructionQty(replaceQuantity.subtract(replaceInStockQty));//?????????????????????????????????????????????????????????
                    }
                    //??????????????????????????????????????????
                    else{
                        replaceOffsetVO.setOffsetQty(replaceQuantity);//???????????????????????????
                        replaceOffsetVO.setReplaceQty(replaceInStockQty.subtract(replaceQuantity));//?????????????????????????????????????????????????????????
                        replaceOffsetVO.setActualReplaceQty(BigDecimal.ZERO);//?????????????????????0
                        replaceOffsetVO.setInstructionQty(BigDecimal.ZERO);//??????????????????0
                    }
                }
                replaceOffsetList.add(replaceOffsetVO);
            }

            //????????????????????????
            for(WmsDistDemandOffsetVO rec:replaceOffsetList){
                //????????????????????????????????????0?????????
                if(offsetVO.getInstructionQty().equals(BigDecimal.ZERO)){
                    break;
                }

                //?????????????????????0?????????
                if(rec.getReplaceQty().equals(BigDecimal.ZERO)){
                    continue;
                }

                //?????????????????????????????????????????????????????????????????????
                if(rec.getReplaceQty().compareTo(offsetVO.getInstructionQty()) > -1){
                    //????????????????????????????????????????????????????????????????????????????????????
                    rec.setActualReplaceQty(offsetVO.getInstructionQty().add(rec.getActualReplaceQty()));
                    //???????????????????????????????????????????????????????????????????????????
                    rec.setReplaceQty(rec.getReplaceQty().subtract(offsetVO.getInstructionQty()));
                    //????????????????????????????????????????????????????????????????????????????????????????????????
                    offsetVO.setActualReplaceQty(offsetVO.getActualReplaceQty().subtract(offsetVO.getInstructionQty()));
                    //??????????????????????????????0
                    offsetVO.setInstructionQty(BigDecimal.ZERO);
                    break;
                }
                //???????????????????????????????????????????????????????????????
                else{
                    //?????????????????????????????????????????????????????????????????????????????????????????????
                    offsetVO.setActualReplaceQty(offsetVO.getActualReplaceQty().subtract(rec.getReplaceQty()));
                    //?????????????????????????????????????????????????????????????????????????????????
                    offsetVO.setInstructionQty(offsetVO.getInstructionQty().subtract(rec.getReplaceQty()));
                    //?????????????????????????????????????????????????????????????????????????????????
                    rec.setActualReplaceQty(rec.getReplaceQty().add(rec.getActualReplaceQty()));
                    //??????????????????????????????0
                    rec.setReplaceQty(BigDecimal.ZERO);

                }
            }
            resultCreateVO.add(offsetVO);
            resultCreateVO.addAll(replaceOffsetList);
        }

        //???????????????????????????????????????????????????
        //?????????????????????????????????
        List<WmsDistDemandOffsetVO> mainList = resultCreateVO.stream().filter(a -> WmsConstant.CONSTANT_N.equals(a.getSubstituteFlag())&&StringUtils.isEmpty(a.getMaterialVersion())).collect(Collectors.toList());
        resultCreateVO.removeAll(mainList);
        for(WmsDistDemandOffsetVO vo:mainList){
            if(vo.getInstructionQty().equals(BigDecimal.ZERO)){
                continue;
            }
            //???????????????????????????????????????????????????
            List<WmsDistDemandOffsetVO> replaceMainList = resultCreateVO.stream()
                    .filter(a -> WmsConstant.CONSTANT_N.equals(a.getSubstituteFlag())&&StringUtils.isNotEmpty(a.getMaterialVersion())&&vo.getMaterialId().equals(a.getMaterialId()))
                    .collect(Collectors.toList());
            List<WmsDistDemandOffsetVO> replaceList = new ArrayList<>(replaceMainList);
            //????????????????????????????????????
            for(WmsDistDemandOffsetVO replaceVO:replaceMainList){
                List<WmsDistDemandOffsetVO> versionReplaceList = resultCreateVO.stream()
                        .filter(a -> WmsConstant.CONSTANT_Y.equals(a.getSubstituteFlag())&&CollectionUtils.isEmpty(replaceVO.getDistDemandIdList().stream().filter(t->!a.getDistDemandIdList().contains(t)).collect(Collectors.toList())))
                        .collect(Collectors.toList());
                replaceList.addAll(versionReplaceList);
            }
            replaceList = replaceList.stream().distinct().collect(Collectors.toList());
            resultCreateVO.removeAll(replaceList);
            for(WmsDistDemandOffsetVO rec:replaceList){
                //?????????????????????????????????????????????????????????????????????
                if(rec.getReplaceQty().compareTo(vo.getInstructionQty()) > -1){
                    //????????????????????????????????????????????????????????????+?????????????????????
                    rec.setActualReplaceQty(vo.getInstructionQty().add(rec.getActualReplaceQty()));
                    //???????????????????????????????????????????????????????????????????????????
                    rec.setReplaceQty(rec.getReplaceQty().subtract(vo.getInstructionQty()));
                    //????????????????????????????????????????????????????????????????????????????????????
                    vo.setActualReplaceQty(vo.getActualReplaceQty().subtract(vo.getInstructionQty()));
                    //??????????????????????????????0
                    vo.setInstructionQty(BigDecimal.ZERO);
                    break;
                }
                //???????????????????????????????????????????????????????????????
                else{
                    //????????????????????????????????????????????????????????????????????????????????????????????????
                    vo.setActualReplaceQty(vo.getActualReplaceQty().subtract(rec.getReplaceQty()));
                    //?????????????????????????????????????????????????????????????????????????????????
                    vo.setInstructionQty(vo.getInstructionQty().subtract(rec.getReplaceQty()));
                    //????????????????????????????????????????????????????????????????????????
                    rec.setActualReplaceQty(rec.getReplaceQty().add(rec.getActualReplaceQty()));
                    //??????????????????????????????0
                    rec.setReplaceQty(BigDecimal.ZERO);

                }
            }
            resultCreateVO.addAll(replaceList);
        }
        resultCreateVO.addAll(mainList);


        // ????????????
        WmsEventVO event = this.createEvent(tenantId);
        String signFlagProfile = Optional.ofNullable(profileClient.getProfileValueByOptions(WMS_DISTRIBUTION_SIGN_FLAG)).orElse(WmsConstant.CONSTANT_Y);

        if (CollectionUtils.isNotEmpty(resultCreateVO)) {
            // ??????????????????
            Map<String, List<WmsDistDemandOffsetVO>> docMap = resultCreateVO.stream().collect(Collectors.groupingBy(WmsDistDemandOffsetVO::getWorkcellId));
            docMap.forEach((workcellId, list) -> {

                String siteId = list.get(0).getSiteId();
                // ???????????????????????????
                this.instructionDocCreateNew(tenantId, event, siteId, list, signFlagProfile);
            });
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void distributionDocCreate(Long tenantId, WmsDistributionDocCreateDTO dto) {
        if (CollectionUtils.isEmpty(dto.getDemandList())) {
            return;
        }
        List<WmsDistributionDemandVO> distributionDocCreateOldList = new ArrayList<>();
        List<WmsDistributionDemandVO> distributionDocCreateNewList = new ArrayList<>();
        for(WmsDistributionDemandVO demand: dto.getDemandList()){
            WmsDistributionBasicDataProductionLine basicDataProductionLine = new WmsDistributionBasicDataProductionLine();
            basicDataProductionLine.setTenantId(tenantId);
            if (StringUtils.isBlank(demand.getDistributionBasicId())) {
                distributionDocCreateOldList.add(demand);
            } else {
                basicDataProductionLine.setHeaderId(demand.getDistributionBasicId());
                basicDataProductionLine.setProductionLineId(demand.getProdLineId());
                List<WmsDistributionBasicDataProductionLine> lineList = wmsDistributionBasicDataProductionLineRepository.select(basicDataProductionLine);
                if(CollectionUtils.isNotEmpty(lineList)) {
                    String backFlushFlag = StringUtils.isNotEmpty(lineList.get(0).getBackflushFlag()) ? lineList.get(0).getBackflushFlag() : "";
                    if (HmeConstants.ConstantValue.YES.equals(backFlushFlag)) {
                        distributionDocCreateNewList.add(demand);
                    } else {
                        distributionDocCreateOldList.add(demand);
                    }
                }
            }
        }
        List<WmsDistributionDocCreateDTO> distributionDocCreateDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(distributionDocCreateNewList)) {
            WmsDistributionDocCreateDTO createDTO = new WmsDistributionDocCreateDTO();
            BeanUtils.copyProperties(dto, createDTO);
            createDTO.setFlag(WmsConstant.CONSTANT_Y);
            createDTO.setDemandList(distributionDocCreateNewList);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(DAY_OF_MONTH,calendar.get(DAY_OF_MONTH) +1);
            Date tomorrow =calendar.getTime();
            //????????????????????????????????????
            List<MtCalendarShift> newShiftList = createDTO.getCalendarShiftList().stream().filter(a -> tomorrow.toString().equals(a.getShiftDate().toString())).collect(Collectors.toList());
            List<MtCalendarShift> oldShiftList = createDTO.getCalendarShiftList().stream().filter(a -> !tomorrow.toString().equals(a.getShiftDate().toString())).collect(Collectors.toList());
            //????????????????????????????????????????????????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(newShiftList)){
                createDTO.setCalendarShiftList(newShiftList);
                distributionDocCreateNew(tenantId,createDTO,tomorrow);
            }
            //???????????????????????????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(oldShiftList)){
                createDTO.setCalendarShiftList(oldShiftList);
                distributionDocCreateDTOList.add(createDTO);
            }
        }

        // ?????????N????????????????????????????????????
        if (CollectionUtils.isNotEmpty(distributionDocCreateOldList)) {
            WmsDistributionDocCreateDTO createDTO = new WmsDistributionDocCreateDTO();
            BeanUtils.copyProperties(dto, createDTO);
            createDTO.setFlag(WmsConstant.CONSTANT_N);
            createDTO.setDemandList(distributionDocCreateOldList);
            distributionDocCreateDTOList.add(createDTO);
        }

        if (CollectionUtils.isNotEmpty(distributionDocCreateDTOList)) {
            distributionDocCreateOld2(tenantId, distributionDocCreateDTOList);
        }
    }

    /**
     * ?????????????????????????????????????????????????????????(???????????????????????????????????????)
     * @param tenantId
     * @param dtoList
     * @author yifan.xiong@hand-china.com 2021-3-16 17:05:33
     */
    private void distributionDocCreateOld2(Long tenantId, List<WmsDistributionDocCreateDTO> dtoList){
        List<WmsDistributionDemandVO> demandVOList = new ArrayList<>();
        List<MtCalendarShift> calendarShiftList = dtoList.get(0).getCalendarShiftList();
        for (WmsDistributionDocCreateDTO createDTO : dtoList) {
            if (CollectionUtils.isNotEmpty(createDTO.getDemandList())) {
                List<WmsDistributionDemandVO> demands = createDTO.getDemandList().stream().map(demand -> {
                    demand.setFlag(createDTO.getFlag());
                    return demand;
                }).collect(Collectors.toList());
                demandVOList.addAll(demands);
            }
            if (WmsConstant.CONSTANT_N.equals(createDTO.getFlag())) {
                calendarShiftList = createDTO.getCalendarShiftList();
            }
        }
        // ?????????????????????ID
        List<String> newDistDemandIdList = new ArrayList<>();
        List<String> distDemandIdList = new ArrayList<>();
        List<MtCalendarShift> finalCalendarShiftList = calendarShiftList;
        for (WmsDistributionDemandVO demand : demandVOList) {
            demand.getShiftQtyList().forEach(rec -> {
                if (finalCalendarShiftList.stream().anyMatch(cal -> cal.getShiftCode().equals(rec.getShiftCode()) && cal.getShiftDate().equals(rec.getShiftDate())) && StringUtils.isNotBlank(rec.getCalendarShiftId())) {
                    distDemandIdList.add(rec.getDistDemandId());
                    if (WmsConstant.CONSTANT_Y.equals(demand.getFlag())) {
                        newDistDemandIdList.add(rec.getDistDemandId());
                    }
                }
            });
        }
        List<WmsDistDemandCreateVO> createList = wmsDistributionDemandDetailRepository.selectCreateListByDemandIdList(tenantId, distDemandIdList);
        // ??????
        if (CollectionUtils.isEmpty(createList)) {
            throw new CommonException("????????????????????????????????????");
        }
        createList.forEach(rec -> {
            if (Objects.isNull(rec.getLocatorId())) {
                throw new CommonException("?????????" + rec.getWorkcellCode() + "???????????????????????????");
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(DAY_OF_MONTH,calendar.get(DAY_OF_MONTH) +1);
        Date tomorrow =calendar.getTime();

        // ????????????
        WmsEventVO event = this.createEvent(tenantId);
        String signFlagProfile = Optional.ofNullable(profileClient.getProfileValueByOptions(WMS_DISTRIBUTION_SIGN_FLAG)).orElse(WmsConstant.CONSTANT_Y);
        calendarShiftList.forEach(calendarShift -> {
            List<WmsDistDemandCreateVO> docList = createList.stream().filter(rec -> rec.getShiftCode().equals(calendarShift.getShiftCode()) && rec.getShiftDate().equals(calendarShift.getShiftDate())).collect(Collectors.toList());
            // ???????????????????????????????????????  ????????????
            if (tomorrow.toString().equals(calendarShift.getShiftDate().toString())) {
                docList = docList.stream().filter(doc -> !newDistDemandIdList.contains(doc.getDistDemandId())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(docList)) {
                // ??????????????????
                Map<String, List<WmsDistDemandCreateVO>> docMap = docList.stream().collect(Collectors.groupingBy(WmsDistDemandCreateVO::getWorkcellId));
                docMap.forEach((workcellId, list) -> {
                    String siteId = list.get(0).getSiteId();
                    // ???????????????????????????
                    this.instructionDocCreate(tenantId, event, siteId, list, signFlagProfile);
                });
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsDistributionDocCreateDTO distributionDocCheck(Long tenantId, List<WmsDistributionDemandVO> demandList) {
        WmsDistributionDocCreateDTO resultDto = new WmsDistributionDocCreateDTO();

        List<String> flagList = new ArrayList<>();
        for(WmsDistributionDemandVO demand:demandList){
            WmsDistributionBasicDataProductionLine basicDataProductionLine = new WmsDistributionBasicDataProductionLine();
            basicDataProductionLine.setTenantId(tenantId);
            if (StringUtils.isBlank(demand.getDistributionBasicId())) {
                flagList.add(WmsConstant.CONSTANT_N);
            } else {
                basicDataProductionLine.setHeaderId(demand.getDistributionBasicId());
                basicDataProductionLine.setProductionLineId(demand.getProdLineId());
//                basicDataProductionLine.setWorkcellId(demand.getWorkcellId());
                List<WmsDistributionBasicDataProductionLine> lineList = wmsDistributionBasicDataProductionLineRepository.select(basicDataProductionLine);
                if(CollectionUtils.isNotEmpty(lineList)) {
                    if(StringUtils.isNotEmpty(lineList.get(0).getBackflushFlag())) {
                        flagList.add(lineList.get(0).getBackflushFlag());
                    }
                }
            }
        }
        // 20211112 modify by sanfeng.zhang ???????????? ?????? ???????????????????????????
        //?????????????????????????????????null,?????????N
        if(flagList.size() == 0){
            resultDto.setFlag(WmsConstant.CONSTANT_N);
            return resultDto;
        }
//        //???????????????????????????????????????????????????null????????????
//        if(flagList.size() != demandList.size()){
//            throw new MtException("WMS_DISTRIBUTION_0021",
//                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DISTRIBUTION_0021","WMS"));
//        }
//        //????????????????????????????????????????????????
//        flagList = flagList.stream().distinct().collect(Collectors.toList());
//        if(flagList.size() > 1){
//            throw new MtException("WMS_DISTRIBUTION_0021",
//                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DISTRIBUTION_0021","WMS"));
//        }
        //??????????????????????????????????????????????????????
        resultDto.setFlag(flagList.get(0));
        return resultDto;
    }
}
