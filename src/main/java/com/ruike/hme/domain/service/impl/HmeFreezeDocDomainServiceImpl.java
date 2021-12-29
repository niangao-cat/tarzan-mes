package com.ruike.hme.domain.service.impl;

import com.ruike.hme.api.dto.HmeFreezeDocQueryDTO;
import com.ruike.hme.api.dto.HmeFreezeDocSaveCommandDTO;
import com.ruike.hme.api.dto.HmeFreezeDocSnUnfreezeCommandDTO;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.service.HmeFreezeDocDomainService;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeFreezeDocMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.JsonUtils;
import com.ruike.itf.app.service.ItfFreezeDocIfaceService;
import com.ruike.itf.domain.entity.ItfFreezeDocIface;
import com.ruike.itf.domain.repository.ItfFreezeDocIfaceRepository;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.vo.WmsEventVO;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.message.entity.Receiver;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO13;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO16;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static com.ruike.hme.infra.constant.HmeConstants.Event.*;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeApprovalType.UNAPPROVAL;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeDocStatus.NEW;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeDocStatus.NUFREEZED;
import static com.ruike.hme.infra.constant.HmeConstants.FreezePrivilegeObjectType.PROD_LINE;
import static com.ruike.hme.infra.constant.HmeConstants.FreezePrivilegeObjectType.WAREHOUSE;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeType.*;
import static com.ruike.hme.infra.constant.HmeConstants.MessageCode.FREEZE_EMAIL_NOTIFICATION;
import static com.ruike.hme.infra.constant.HmeConstants.MessageCode.UNFREEZE_EMAIL_NOTIFICATION;
import static com.ruike.hme.infra.constant.HmeConstants.ObjectCode.FREEZE_DOC_NUM;

/**
 * <p>
 * 条码冻结单 领域服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:45
 */
@Service
public class HmeFreezeDocDomainServiceImpl implements HmeFreezeDocDomainService {
    private final HmeFreezeDocRepository docRepository;
    private final HmeFreezeDocLineRepository docLineRepository;
    private final CodeRuleBuilder codeRuleBuilder;
    private final WmsEventService eventService;
    private final WmsObjectTransactionRepository transactionRepository;
    private final MtInvOnhandQuantityRepository invOnhandQuantityRepository;
    private final MtMaterialLotRepository materialLotRepository;
    private final MtExtendSettingsRepository extendSettingsRepository;
    private final HmeFreezePrivilegeRepository privilegeRepository;
    private final HmeFreezePrivilegeDetailRepository privilegeDetailRepository;
    private final MessageClient messageClient;
    private final ItfFreezeDocIfaceRepository freezeDocIfaceRepository;
    private final ItfFreezeDocIfaceService freezeDocIfaceService;
    private final HmeMaterialLotLoadRepository materialLotLoadRepository;
    private final HmeFreezeDocMapper hmeFreezeDocMapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    public HmeFreezeDocDomainServiceImpl(HmeFreezeDocRepository docRepository, HmeFreezeDocLineRepository docLineRepository, CodeRuleBuilder codeRuleBuilder, WmsEventService eventService, WmsObjectTransactionRepository transactionRepository, MtInvOnhandQuantityRepository invOnhandQuantityRepository, MtMaterialLotRepository materialLotRepository, MtExtendSettingsRepository extendSettingsRepository, HmeFreezePrivilegeRepository privilegeRepository, HmeFreezePrivilegeDetailRepository privilegeDetailRepository, MessageClient messageClient, ItfFreezeDocIfaceRepository freezeDocIfaceRepository, ItfFreezeDocIfaceService freezeDocIfaceService, HmeMaterialLotLoadRepository materialLotLoadRepository, HmeFreezeDocMapper hmeFreezeDocMapper, MtErrorMessageRepository mtErrorMessageRepository) {
        this.docRepository = docRepository;
        this.docLineRepository = docLineRepository;
        this.codeRuleBuilder = codeRuleBuilder;
        this.eventService = eventService;
        this.transactionRepository = transactionRepository;
        this.invOnhandQuantityRepository = invOnhandQuantityRepository;
        this.materialLotRepository = materialLotRepository;
        this.extendSettingsRepository = extendSettingsRepository;
        this.privilegeRepository = privilegeRepository;
        this.privilegeDetailRepository = privilegeDetailRepository;
        this.messageClient = messageClient;
        this.freezeDocIfaceRepository = freezeDocIfaceRepository;
        this.freezeDocIfaceService = freezeDocIfaceService;
        this.materialLotLoadRepository = materialLotLoadRepository;
        this.hmeFreezeDocMapper = hmeFreezeDocMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public HmeFreezeDocVO docCreation(Long tenantId, HmeFreezeDocSaveCommandDTO dto) {
        // 筛选出未冻结的条码，对这些条码执行冻结
        materialLotFreeze(tenantId, dto);
        // 执行创建单据
        HmeFreezeDoc doc = createDoc(tenantId, dto);
        // 推送邮件
        sendMail(tenantId, doc.getFreezeDocId(), true);
        return docRepository.byId(tenantId, doc.getFreezeDocId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void docCreationNew(Long tenantId, HmeFreezeDocQueryDTO dto) {
        //先根据查询条件确定条码
        List<String> freezeMaterialLotIdList = docRepository.selectFreezeMaterialLotIdList(tenantId, dto);
        if(CollectionUtils.isEmpty(freezeMaterialLotIdList)){
            throw new MtException("HME_FREEZE_005", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_FREEZE_005", "HME"));
        }
        // 执行创建单据
        HmeFreezeDocSaveCommandDTO hmeFreezeDocSaveCommandDTO = new HmeFreezeDocSaveCommandDTO();
        //为了考虑到COS贴片后冻结时，输入了筛选规则，生产时间只为筛选时间的情况
        if(Objects.nonNull(dto.getPreSelectionDateFrom())){
            dto.setProductionDateFrom(dto.getPreSelectionDateFrom());
        }
        if(Objects.nonNull(dto.getPreSelectionDateTo())){
            dto.setProductionDateTo(dto.getPreSelectionDateTo());
        }
        BeanCopierUtil.copy(dto, hmeFreezeDocSaveCommandDTO);
        List<HmeFreezeDocSaveCommandDTO.SaveLineCommand> lineList = new ArrayList<>();
        for (String materialLotId:freezeMaterialLotIdList) {
            HmeFreezeDocSaveCommandDTO.SaveLineCommand line = new HmeFreezeDocSaveCommandDTO.SaveLineCommand();
            line.setMaterialLotId(materialLotId);
            lineList.add(line);
        }
        hmeFreezeDocSaveCommandDTO.setLineList(lineList);
        createDoc(tenantId, hmeFreezeDocSaveCommandDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public HmeFreezeDocQueryDTO docFreeze(Long tenantId, String freezeDocId) {
        //更新头表状态
        HmeFreezeDoc hmeFreezeDoc = docRepository.selectByPrimaryKey(freezeDocId);
        hmeFreezeDoc.setFreezeDocStatus("FREEZED");
        hmeFreezeDocMapper.updateByPrimaryKeySelective(hmeFreezeDoc);
        HmeFreezeDocSaveCommandDTO dto = new HmeFreezeDocSaveCommandDTO();
        BeanCopierUtil.copy(hmeFreezeDoc, dto);
        List<HmeFreezeDocVO2> hmeFreezeDocVO2List = hmeFreezeDocMapper.selectFreeLineInfo(tenantId, freezeDocId);
        //更新行表的冻结标识
        List<String> freezeDocLineIdList = hmeFreezeDocVO2List.stream().map(HmeFreezeDocVO2::getFreezeDocLineId).collect(Collectors.toList());
        List<List<String>> splitFreezeDocLineIdList = CommonUtils.splitSqlList(freezeDocLineIdList, 1000);
        for (List<String> splitFreezeDocLineId:splitFreezeDocLineIdList) {
            hmeFreezeDocMapper.batchUpdataFreezeDocLine(splitFreezeDocLineId);
        }
        List<HmeFreezeDocSaveCommandDTO.SaveLineCommand> lineList = new ArrayList<>();
        for (HmeFreezeDocVO2 hmeFreezeDocVO2:hmeFreezeDocVO2List) {
            HmeFreezeDocSaveCommandDTO.SaveLineCommand line = new HmeFreezeDocSaveCommandDTO.SaveLineCommand();
            BeanCopierUtil.copy(hmeFreezeDocVO2, line);
            lineList.add(line);
        }
        dto.setLineList(lineList);
        // 筛选出未冻结的条码，对这些条码执行冻结
        materialLotFreeze(tenantId, dto);
        // 推送邮件
        sendMail(tenantId, freezeDocId, true);
        HmeFreezeDocQueryDTO vo = new HmeFreezeDocQueryDTO();
        vo.setFreezeDocId(freezeDocId);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void docUnfrozen(Long tenantId, String freezeDocId) {
        HmeFreezeDoc hmeFreezeDoc = docRepository.selectByPrimaryKey(freezeDocId);
        if(!"FREEZED".equals(hmeFreezeDoc.getFreezeDocStatus())){
            throw new MtException("HME_FREEZE_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_FREEZE_006", "HME"));
        }
        List<HmeFreezeDocLine> list = docLineRepository.byDocId(tenantId, freezeDocId).stream().filter(r -> YES.equals(r.getFreezeFlag())).collect(Collectors.toList());
        unfreezeLines(tenantId, freezeDocId, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void selectionUnfrozen(Long tenantId, HmeFreezeDocSnUnfreezeCommandDTO command) {
        List<HmeFreezeDocLine> list = command.getLineList().stream().filter(r -> YES.equals(r.getFreezeFlag())).map(HmeFreezeDocSnUnfreezeCommandDTO.LineCommand::toEntity).collect(Collectors.toList());
        unfreezeLines(tenantId, command.getFreezeDocId(), list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void apply(Long tenantId, String freezeDocId) {
        // 修改单据状态并记录接口表
        HmeFreezeDoc doc = docRepository.selectByPrimaryKey(freezeDocId);
        // 20210809 add by sanfeng.zhang 提交审批校验单据已冻结 单据未冻结，不允许提交审批
        if (!"FREEZED".equals(doc.getFreezeDocStatus())) {
            throw new MtException("HME_FREEZE_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_FREEZE_007", "HME", doc.getFreezeDocNum()));
        }
        ItfFreezeDocIface iface = ItfFreezeDocIface.toInterface(doc, freezeDocIfaceRepository);
        iface.processInit();

        // 保存数据
        doc.applyApproval();
        docRepository.save(doc);
        freezeDocIfaceRepository.save(iface);

        // 推送接口数据
        freezeDocIfaceService.send(tenantId, iface);
    }

    private void sendMail(Long tenantId, String freezeDocId, boolean freezeFlag) {
        List<HmeFreezeDocLineVO> lines = docLineRepository.listGet(tenantId, freezeDocId);
        Set<String> prodIds = lines.stream().filter(r -> YES.equals(r.getMfFlag())).map(HmeFreezeDocLineVO::getProdLineId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> warehouseIds = lines.stream().filter(r -> NO.equals(r.getMfFlag())).map(HmeFreezeDocLineVO::getWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> privilegeIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(prodIds)) {
            privilegeIds.addAll(privilegeDetailRepository.selectByCondition(Condition.builder(HmeFreezePrivilegeDetail.class).andWhere(Sqls.custom().andEqualTo(HmeFreezePrivilegeDetail.FIELD_DETAIL_OBJECT_TYPE, PROD_LINE).andIn(HmeFreezePrivilegeDetail.FIELD_DETAIL_OBJECT_ID, prodIds)).build()).stream().map(HmeFreezePrivilegeDetail::getPrivilegeId).collect(Collectors.toSet()));
        }
        if (CollectionUtils.isNotEmpty(warehouseIds)) {
            privilegeIds.addAll(privilegeDetailRepository.selectByCondition(Condition.builder(HmeFreezePrivilegeDetail.class).andWhere(Sqls.custom().andEqualTo(HmeFreezePrivilegeDetail.FIELD_DETAIL_OBJECT_TYPE, WAREHOUSE).andIn(HmeFreezePrivilegeDetail.FIELD_DETAIL_OBJECT_ID, warehouseIds)).build()).stream().map(HmeFreezePrivilegeDetail::getPrivilegeId).collect(Collectors.toSet()));
        }

        if (CollectionUtils.isNotEmpty(privilegeIds)) {
            HmeFreezeDoc doc = docRepository.selectByPrimaryKey(freezeDocId);
            List<HmeFreezePrivilege> privilegeList = privilegeRepository.selectByIds(Strings.join(privilegeIds, ','));
            //privilegeList = privilegeList.stream().filter(rec -> isUserUnfreezePrivileged(doc, rec)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(privilegeList)) {
                List<Receiver> receiverList = privilegeList.stream().map(r -> new Receiver().setEmail(r.getEmail())).collect(Collectors.toList());
                messageClient.sendEmail(tenantId, MES_MAIL_SERVER, freezeFlag ? FREEZE_EMAIL_NOTIFICATION : UNFREEZE_EMAIL_NOTIFICATION, receiverList, docFieldsMapGet(tenantId, freezeDocId));
            }
        }
    }

    @Override
    public Boolean isUserUnfreezePrivileged(HmeFreezeDoc doc, HmeFreezePrivilege privilege) {
        if (Objects.isNull(privilege)) {
            return false;
        }

        if (StringCommonUtils.contains(doc.getFreezeType(), PROCESS, INVENTORY)) {
            // 普通冻结单
            if (ALL.equals(privilege.getPrivilegeType())) {
                return true;
            } else if (PROCESS.equals(doc.getFreezeType()) && PROCESS.equals(privilege.getPrivilegeType())) {
                return true;
            } else {
                return false;
            }
        } else if (StringCommonUtils.contains(doc.getFreezeType(), COS_PROCESS, COS_INVENTORY)) {
            // COS冻结单
            if (StringUtils.isBlank(privilege.getCosPrivilegeType())) {
                return false;
            }
            if (ALL.equals(privilege.getCosPrivilegeType())) {
                return true;
            } else if (COS_PROCESS.equals(doc.getFreezeType()) && COS_PROCESS.equals(privilege.getCosPrivilegeType())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private Map<String, String> docFieldsMapGet(Long tenantId, String freezeDocId) {
        HmeFreezeDocVO doc = docRepository.byId(tenantId, freezeDocId);
        Map<String, String> fieldsMap = JsonUtils.toStringMap(doc);
        fieldsMap.put("senderName", DetailsHelper.getUserDetails().getRealName());
        return fieldsMap;
    }

    private void unfreezeLines(Long tenantId, String freezeDocId, List<HmeFreezeDocLine> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            HmeFreezeDoc doc = unfreezeValidation(tenantId, freezeDocId);
            materialLotUnfreeze(tenantId, doc, list);
            // 修改整单状态为已解冻
            unfreezeDoc(tenantId, freezeDocId, list);
        }
    }

    private HmeFreezeDoc unfreezeValidation(Long tenantId, String freezeDocId) {
        HmeFreezeDoc doc = docRepository.selectByPrimaryKey(freezeDocId);
        HmeFreezePrivilege privilege = privilegeRepository.byUserId(tenantId, DetailsHelper.getUserDetails().getUserId());
        doc.unfreezeValidation(this, privilege);
        return doc;
    }

    private void materialLotUnfreeze(Long tenantId, HmeFreezeDoc doc, List<HmeFreezeDocLine> list) {
        List<String> materialLotIds = list.stream().map(HmeFreezeDocLine::getMaterialLotId).collect(Collectors.toList());
        List<HmeFreezeDocLineSnUnfreezeVO> pendingLines = docLineRepository.unfreezeSnGet(tenantId, materialLotIds);
        if (CollectionUtils.isNotEmpty(pendingLines)) {
            // 筛选非在制条码
            List<HmeFreezeDocLineSnUnfreezeVO> invLines = pendingLines.stream().filter(r -> NO.equals(r.getMfFlag())).collect(Collectors.toList());
            WmsEventVO freeEventRequest = eventService.createEventRequest(tenantId, MATERIAL_FREE);
            WmsEventVO freeOutRequest = eventService.createEventByRequestId(tenantId, MATERIAL_FREE_OUT, freeEventRequest.getEventRequestId());

            // 构造批量更新条码数据
            List<MtMaterialLotVO20> materialLotUpdateList = pendingLines.stream().map(HmeFreezeDocLineSnUnfreezeVO::toMaterialLot).collect(Collectors.toList());
            materialLotRepository.materialLotBatchUpdatePrecompile(tenantId, materialLotUpdateList, freeOutRequest.getEventId());
            //materialLotAndAttrsBatchUpdate(tenantId, freeOutRequest.getEventId(), materialLotUpdateList, new ArrayList<>());

            // 对于仓库条码产生冻结事务
            if (invLines.size() > 0) {
                invSnUnFreezeTransaction(tenantId, doc.getRemark(), invLines, freeEventRequest.getEventRequestId(), freeOutRequest.getEventId());
            }

            // COS类型需要冻结对应的loadSequence
            if (StringCommonUtils.contains(doc.getFreezeType(), "COS_P_INVENTORY", "COS_CHIP_INVENTORY", "COS_M_INVENTORY")) {
                this.unfreezeLoadSequence(tenantId, HmeFreezeDocSaveCommandDTO.toCommand(doc), pendingLines.stream().map(HmeFreezeDocLineSnUnfreezeVO::getMaterialLotId).collect(Collectors.toSet()));
            }
        }
    }

    private void unfreezeDoc(Long tenantId, String freezeDocId, List<HmeFreezeDocLine> list) {
        // 构建保存参数
        HmeFreezeDocSaveCommandDTO command = new HmeFreezeDocSaveCommandDTO();
        command.setFreezeDocId(freezeDocId);

        // 查询数据库数据，将本次解冻的数据剔除，若没有剩下数据，或者剩下的数据全部为非冻结，则修改单据状态为未冻结
        List<HmeFreezeDocLine> allList = docLineRepository.byDocId(tenantId, freezeDocId);
        Set<String> ids = list.stream().map(HmeFreezeDocLine::getFreezeDocLineId).collect(Collectors.toSet());
        allList.removeIf(rec -> ids.contains(rec.getFreezeDocLineId()));
        boolean unfreezeDocFlag = CollectionUtils.isEmpty(allList) || allList.stream().allMatch(r -> NO.equals(r.getFreezeFlag()));
        if (unfreezeDocFlag) {
            command.setFreezeDocStatus(NUFREEZED);
        }

        // 构建行保存参数
        List<HmeFreezeDocSaveCommandDTO.SaveLineCommand> lineList = list.stream().map(HmeFreezeDocSaveCommandDTO.SaveLineCommand::toCommand).collect(Collectors.toList());
        lineList.forEach(r -> r.setFreezeFlag(NO));
        command.setLineList(lineList);
        command.setTenantId(tenantId);
        saveDocData(command);

        // 若要解冻单据则发送邮件
        if (unfreezeDocFlag) {
            sendMail(tenantId, freezeDocId, false);
        }
    }

    private void materialLotFreeze(Long tenantId, HmeFreezeDocSaveCommandDTO dto) {
        List<HmeFreezeDocSaveCommandDTO.SaveLineCommand> pendingLines = dto.getLineList().stream().filter(r -> NO.equals(r.getSnFreezeFlag())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(pendingLines)) {
            // 筛选非在制条码
            List<HmeFreezeDocSaveCommandDTO.SaveLineCommand> invLines = pendingLines.stream().filter(r -> NO.equals(r.getMfFlag())).collect(Collectors.toList());
            WmsEventVO freezeEventRequest = eventService.createEventRequest(tenantId, MATERIAL_FREEZE);
            WmsEventVO freezeOutRequest = eventService.createEventByRequestId(tenantId, MATERIAL_FREEZE_OUT, freezeEventRequest.getEventRequestId());

            // 构造批量更新条码数据
            List<MtMaterialLotVO20> materialLotUpdateList = pendingLines.stream().map(HmeFreezeDocSaveCommandDTO.SaveLineCommand::toMaterialLot).collect(Collectors.toList());
            // 批量更新冻结标识
            materialLotRepository.materialLotBatchUpdatePrecompile(tenantId, materialLotUpdateList, freezeOutRequest.getEventId());
            //materialLotAndAttrsBatchUpdate(tenantId, freezeOutRequest.getEventId(), materialLotUpdateList, new ArrayList<>());

            // 对于仓库条码产生冻结事务
            if (invLines.size() > 0) {
                invSnFreezeTransaction(tenantId, dto.getRemark(), invLines, freezeEventRequest.getEventRequestId(), freezeOutRequest.getEventId());
            }

            // COS类型需要冻结对应的loadSequence
            if (StringCommonUtils.contains(dto.getFreezeType(), "COS_P_INVENTORY", "COS_CHIP_INVENTORY", "COS_M_INVENTORY")) {
                this.freezeLoadSequence(tenantId, dto, pendingLines.stream().map(HmeFreezeDocSaveCommandDTO.SaveLineCommand::getMaterialLotId).collect(Collectors.toSet()));
            }
        }
    }

    private void freezeLoadSequence(Long tenantId, HmeFreezeDocSaveCommandDTO dto, Set<String> materialLotIds) {
        List<HmeMaterialLotLoad> loadList = new ArrayList<>();
        HmeFreezeDocQueryDTO hmeFreezeDocQueryDTO = HmeFreezeDocSaveCommandDTO.toQuery(dto);
        List<String> materialLotIdList = new ArrayList<>(materialLotIds);
        List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 500);
        for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
            loadList.addAll(docRepository.selectRelationLoadList(tenantId, hmeFreezeDocQueryDTO, splitMaterialLotId));
            //2021-06-27 19:51 add by chaonan.hu for peng.zhao 增加两种根据物料批ID查询loadSequence的情况
            loadList.addAll(hmeFreezeDocMapper.selectRelationLoadList2(tenantId, hmeFreezeDocQueryDTO, splitMaterialLotId));
            loadList.addAll(hmeFreezeDocMapper.selectRelationLoadList3(tenantId, hmeFreezeDocQueryDTO, splitMaterialLotId));
        }
        if(CollectionUtils.isNotEmpty(loadList)){
            List<String> materialLotLoadIdList = loadList.stream().map(HmeMaterialLotLoad::getMaterialLotLoadId).distinct().collect(Collectors.toList());
            List<List<String>> splitMaterialLotLoadIdList = CommonUtils.splitSqlList(materialLotLoadIdList, 500);
            for (List<String> splitMaterialLotLoadId:splitMaterialLotLoadIdList) {
                hmeFreezeDocMapper.batchUpdateMaterialLotLoad(splitMaterialLotLoadId, YES);
            }
        }
    }

    private void unfreezeLoadSequence(Long tenantId, HmeFreezeDocSaveCommandDTO dto, Set<String> materialLotIds) {
        List<HmeMaterialLotLoad> loadList = new ArrayList<>();
        HmeFreezeDocQueryDTO hmeFreezeDocQueryDTO = HmeFreezeDocSaveCommandDTO.toQuery(dto);
        List<String> materialLotIdList = new ArrayList<>(materialLotIds);
        List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 500);
        for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
            loadList.addAll(docRepository.selectRelationLoadList(tenantId, hmeFreezeDocQueryDTO, splitMaterialLotId));
            //2021-06-27 19:51 add by chaonan.hu for peng.zhao 增加两种根据物料批ID查询loadSequence的情况
            loadList.addAll(hmeFreezeDocMapper.selectRelationLoadList2(tenantId, hmeFreezeDocQueryDTO, splitMaterialLotId));
            loadList.addAll(hmeFreezeDocMapper.selectRelationLoadList3(tenantId, hmeFreezeDocQueryDTO, splitMaterialLotId));
        }
        if(CollectionUtils.isNotEmpty(loadList)){
            List<String> materialLotLoadIdList = loadList.stream().map(HmeMaterialLotLoad::getMaterialLotLoadId).distinct().collect(Collectors.toList());
            List<List<String>> splitMaterialLotLoadIdList = CommonUtils.splitSqlList(materialLotLoadIdList, 500);
            for (List<String> splitMaterialLotLoadId:splitMaterialLotLoadIdList) {
                hmeFreezeDocMapper.batchUpdateMaterialLotLoad(splitMaterialLotLoadId, NO);
            }
        }
    }

    private void materialLotAndAttrsBatchUpdate(Long tenantId, String eventId, List<MtMaterialLotVO20> materialLotUpdateList, List<MtCommonExtendVO6> attrPropertyList) {
        if (CollectionUtils.isEmpty(materialLotUpdateList)) {
            return;
        }
        // 批量更新冻结标识
        materialLotRepository.materialLotBatchUpdate(tenantId, materialLotUpdateList, eventId, NO);

        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            extendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
    }

    private void invSnUnFreezeTransaction(Long tenantId, String remark, List<HmeFreezeDocLineSnUnfreezeVO> invLines, String eventRequestId, String outEventId) {
        // 新建转入事务
        WmsEventVO freezeInRequest = eventService.createEventRequestWithParent(tenantId, outEventId, MATERIAL_FREE_IN, eventRequestId);
        // 增加解冻事务
        List<String> invSnIds = invLines.stream().map(HmeFreezeDocLineSnUnfreezeVO::getMaterialLotId).collect(Collectors.toList());
        List<HmeFreezeDocTrxVO> trxList = docRepository.materialLotUnfreezeTrxListGet(tenantId, invSnIds);
        trxList.forEach(rec -> {
            rec.setTransactionReasonCode(remark);
            rec.setEventId(freezeInRequest.getEventId());
        });
        transactionRepository.objectTransactionSync(tenantId, trxList.stream().map(HmeFreezeDocTrxVO::unFreezeTransactionBuilder).collect(Collectors.toList()));

        // 批量现有量更新，先批量扣减，再批量新增
        onhandQtyBatchUpdate(tenantId, outEventId, trxList, freezeInRequest.getEventId());

        // 构造批量更新条码数据
        List<MtMaterialLotVO20> materialLotUpdateList = trxList.stream().map(HmeFreezeDocTrxVO::toUnfreezeMaterialLot).collect(Collectors.toList());
        List<MtCommonExtendVO6> materialLotAttrList = trxList.stream().map(HmeFreezeDocTrxVO::toUnfreezeMaterialLotAttr).collect(Collectors.toList());
        materialLotAndAttrsBatchUpdate(tenantId, freezeInRequest.getEventId(), materialLotUpdateList, materialLotAttrList);
    }

    private void invSnFreezeTransaction(Long tenantId, String remark, List<HmeFreezeDocSaveCommandDTO.SaveLineCommand> invLines, String eventRequestId, String outEventId) {
        // 新建转入事务
        WmsEventVO freezeInRequest = eventService.createEventRequestWithParent(tenantId, outEventId, MATERIAL_FREEZE_IN, eventRequestId);

        // 增加冻结事务
        List<String> invSnIds = invLines.stream().map(HmeFreezeDocSaveCommandDTO.SaveLineCommand::getMaterialLotId).collect(Collectors.toList());
        List<HmeFreezeDocTrxVO> trxList = docRepository.materialLotFreezeTrxListGet(tenantId, invSnIds);
        trxList.forEach(rec -> {
            rec.setTransactionReasonCode(remark);
            rec.setEventId(freezeInRequest.getEventId());
        });
        transactionRepository.objectTransactionSync(tenantId, trxList.stream().map(HmeFreezeDocTrxVO::freezeTransactionBuilder).collect(Collectors.toList()));

        // 批量现有量更新，先批量扣减，再批量新增
        onhandQtyBatchUpdate(tenantId, outEventId, trxList, freezeInRequest.getEventId());

        // 构造批量更新条码数据
        List<MtMaterialLotVO20> materialLotUpdateList = trxList.stream().map(HmeFreezeDocTrxVO::toFreezeMaterialLot).collect(Collectors.toList());
        List<MtCommonExtendVO6> materialLotAttrList = trxList.stream().map(HmeFreezeDocTrxVO::toFreezeMaterialLotAttr).collect(Collectors.toList());
        materialLotAndAttrsBatchUpdate(tenantId, freezeInRequest.getEventId(), materialLotUpdateList, materialLotAttrList);
    }

    private void onhandQtyBatchUpdate(Long tenantId, String outEventId, List<HmeFreezeDocTrxVO> trxList, String inEventId) {
        MtInvOnhandQuantityVO16 onHandReduce = new MtInvOnhandQuantityVO16();
        List<MtInvOnhandQuantityVO13> onhandReduceList = new ArrayList<>();
        onHandReduce.setEventId(outEventId);
        onHandReduce.setOnhandList(onhandReduceList);

        MtInvOnhandQuantityVO16 onHandIncrease = new MtInvOnhandQuantityVO16();
        List<MtInvOnhandQuantityVO13> onhandIncreaseList = new ArrayList<>();
        onHandIncrease.setEventId(inEventId);
        onHandIncrease.setOnhandList(onhandIncreaseList);

        trxList.forEach(rec -> {
            onhandReduceList.add(new MtInvOnhandQuantityVO13() {{
                setSiteId(rec.getPlantId());
                setMaterialId(rec.getMaterialId());
                setLocatorId(rec.getReduceLocatorId());
                setLotCode(rec.getLotNumber());
                setChangeQuantity(rec.getTransactionQty().negate().doubleValue());
            }});
            onhandIncreaseList.add(new MtInvOnhandQuantityVO13() {{
                setSiteId(rec.getPlantId());
                setMaterialId(rec.getMaterialId());
                setLocatorId(rec.getIncreaseLocatorId());
                setLotCode(rec.getLotNumber());
                setChangeQuantity(rec.getTransactionQty().doubleValue());
            }});
        });

        invOnhandQuantityRepository.onhandQtyBatchUpdate(tenantId, onHandReduce);
        invOnhandQuantityRepository.onhandQtyBatchUpdate(tenantId, onHandIncrease);
    }

    private HmeFreezeDoc createDoc(Long tenantId, HmeFreezeDocSaveCommandDTO dto) {
        // 转换数据
        String docNum = codeRuleBuilder.generateCode(FREEZE_DOC_NUM, new HashMap<>(0));
        dto.setFreezeDocNum(docNum);
        dto.setTenantId(tenantId);
        dto.setFreezeDocStatus(NEW);
        dto.setApprovalStatus(UNAPPROVAL);

        //V20211129 modify by penglin.sui 将获取用户提取到循环外
        Long userId = -1L;
        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
        if(Objects.nonNull(customUserDetails)
                && Objects.nonNull(customUserDetails.getUserId())){
            userId = customUserDetails.getUserId();
        }

        for (HmeFreezeDocSaveCommandDTO.SaveLineCommand r : dto.getLineList()
             ) {
            r.setTenantId(tenantId);
            r.setFreezeFlag(NO);
            r.setFrozenBy(userId);
        }

        return saveDocData(dto);
    }

    /**
     * 保存单据数据
     *
     * @param dto 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 11:42:19
     */
    private HmeFreezeDoc saveDocData(HmeFreezeDocSaveCommandDTO dto) {
        HmeFreezeDoc doc = HmeFreezeDocSaveCommandDTO.toEntity(dto);
        // 执行头保存
        docRepository.save(doc);
        dto.getLineList().forEach(r -> {
            if (StringUtils.isBlank(r.getFreezeDocId())) {
                r.setFreezeDocId(doc.getFreezeDocId());
            }
        });
        List<HmeFreezeDocLine> lineList = HmeFreezeDocSaveCommandDTO.lineBatchToEntity(dto.getLineList());
        // 执行行保存
        docLineRepository.batchSave(lineList);
        return doc;
    }
}
