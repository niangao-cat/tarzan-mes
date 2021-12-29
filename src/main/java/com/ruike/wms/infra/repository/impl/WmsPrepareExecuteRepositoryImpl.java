package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.repository.WmsPrepareExecuteRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsInstructionMapper;
import com.ruike.wms.infra.mapper.WmsPrepareExecuteMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.CONTAINER;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.PREPARE_COMPLETE;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.PREPARE_EXECUTE;
import static com.ruike.wms.infra.constant.WmsConstant.MATERIAL_LOT;

/**
 * 备料执行 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 16:58
 */
@Component
public class WmsPrepareExecuteRepositoryImpl implements WmsPrepareExecuteRepository {

    private final WmsPrepareExecuteMapper wmsPrepareExecuteMapper;
    private final MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    private final MtModWorkcellRepository workcellRepository;
    private final WmsInstructionMapper wmsInstructionMapper;

    public WmsPrepareExecuteRepositoryImpl(WmsPrepareExecuteMapper wmsPrepareExecuteMapper, MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository, MtModWorkcellRepository workcellRepository, WmsInstructionMapper wmsInstructionMapper) {
        this.wmsPrepareExecuteMapper = wmsPrepareExecuteMapper;
        this.mtModLocatorOrgRelRepository = mtModLocatorOrgRelRepository;
        this.workcellRepository = workcellRepository;
        this.wmsInstructionMapper = wmsInstructionMapper;
    }

    @Override
    public WmsPrepareExecInsDocVO selectDistDocByNum(Long tenantId, String instructionDocNum) {
        WmsPrepareExecInsDocVO insDoc = wmsPrepareExecuteMapper.selectDistDocByNum(tenantId, instructionDocNum);
        WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(insDoc), "WMS_MATERIAL_ON_SHELF_0001", "WMS", instructionDocNum);
        return insDoc;
    }

    @Override
    public MtModLocator getRecommendLocator(Long tenantId, String instructionId) {
        WmsInstructionAttrVO instruction = wmsInstructionMapper.selectDetailById(tenantId, instructionId);
        return wmsPrepareExecuteMapper.selectDistLocatorBySiteId(tenantId, instruction.getMaterialId(), instruction.getMaterialVersion(), instruction.getSiteId(), instruction.getSoNum(), instruction.getSoLineNum());
    }

    @Override
    public List<WmsPrepareExecInsVO> selectInsListByDocId(Long tenantId, String instructionDocId) {
        List<WmsPrepareExecInsVO> list = wmsPrepareExecuteMapper.selectInsListByDocId(tenantId, instructionDocId);
        // 按照状态排序，若为配送完成的放到后面，其次按照货位ID排序
        list = list.stream().sorted((ins1, ins2) -> {
            int ins1Sort = PREPARE_COMPLETE.equals(ins1.getInstructionStatus()) ? 99 : (PREPARE_EXECUTE.equals(ins1.getInstructionStatus()) ? 1 : 2);
            int ins2Sort = PREPARE_COMPLETE.equals(ins2.getInstructionStatus()) ? 99 : (PREPARE_EXECUTE.equals(ins2.getInstructionStatus()) ? 1 : 2);
            return ins1Sort - ins2Sort;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public MtModLocator selectLocatorOnWarehouse(Long tenantId, String locatorId) {
        return wmsPrepareExecuteMapper.selectLocatorOnWarehouse(tenantId, locatorId);
    }

    @Override
    public List<WmsInstructionActualDetailVO> selectActualDetailByInstId(Long tenantId, String instructionId) {
        return wmsPrepareExecuteMapper.selectActualDetailByInstId(tenantId, instructionId);
    }

    @Override
    public List<WmsInstructionActualDetailVO> selectActualDetailByDocId(Long tenantId, String instructionDocId) {
        return wmsPrepareExecuteMapper.selectActualDetailByDocId(tenantId, instructionDocId);
    }

    @Override
    public WmsLocatorSiteVO selectTargetLocator(Long tenantId, String signFlag, WmsInstructionDocAttrVO instructionDoc, WmsInstructionActualDetailVO detail) {
        WmsLocatorSiteVO locatorSite;
        if (WmsConstant.CONSTANT_Y.equals(signFlag)) {
            boolean containerFlag = StringUtils.isNotBlank(detail.getContainerId());
            String loadTypeCode = containerFlag ? CONTAINER : MATERIAL_LOT;
            String loadTypeId = containerFlag ? detail.getContainerId() : detail.getMaterialLotId();
            locatorSite = wmsPrepareExecuteMapper.selectLocatorByBarcode(tenantId, loadTypeCode, loadTypeId);
        } else {
            locatorSite = wmsPrepareExecuteMapper.selectLocatorById(tenantId, instructionDoc.getToLocatorId());
        }
        if (Objects.isNull(locatorSite)) {
            WmsCommonUtils.processValidateMessage(tenantId, "WMS_DISTRIBUTION_0015", "WMS", detail.getMaterialLotCode());
        }
        return locatorSite;
    }

    @Override
    public MtModLocatorOrgRelVO3 selectSiteByLocator(Long tenantId, String locatorId) {
        MtModLocatorOrgRelVO2 siteCondition = new MtModLocatorOrgRelVO2();
        siteCondition.setLocatorId(locatorId);
        siteCondition.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
        List<MtModLocatorOrgRelVO3> siteList = mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, siteCondition);
        if (CollectionUtils.isEmpty(siteList)) {
            return new MtModLocatorOrgRelVO3();
        }
        return siteList.get(0);
    }

    @Override
    public MtModLocator selectDistLocatorBySiteId(Long tenantId, String materialId, String materialVersion, String siteId, String soNum, String soLineNum) {
        return wmsPrepareExecuteMapper.selectDistLocatorBySiteId(tenantId, materialId, materialVersion, siteId, soNum, soLineNum);
    }
}
