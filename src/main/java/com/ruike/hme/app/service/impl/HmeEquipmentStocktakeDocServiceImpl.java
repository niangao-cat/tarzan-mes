package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocActionCommand;
import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocCreateCommand;
import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocModifyCommand;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;
import com.ruike.hme.app.assembler.HmeEquipmentStocktakeDocAssembler;
import com.ruike.hme.app.service.HmeEquipmentStocktakeDocService;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeDocRepository;
import com.ruike.hme.domain.service.HmeEquipmentStocktakeDomainService;
import com.ruike.hme.domain.vo.HmeEquipmentVO;
import com.ruike.hme.infra.mapper.HmeEquipmentMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 设备盘点单应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
@Service
public class HmeEquipmentStocktakeDocServiceImpl implements HmeEquipmentStocktakeDocService {
    private final HmeEquipmentStocktakeDocRepository repository;
    private final HmeEquipmentStocktakeDocAssembler assembler;
    private final HmeEquipmentMapper hmeEquipmentMapper;
    private final HmeEquipmentStocktakeDomainService hmeEquipmentStocktakeDomainService;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    public HmeEquipmentStocktakeDocServiceImpl(HmeEquipmentStocktakeDocRepository repository, HmeEquipmentStocktakeDocAssembler assembler, HmeEquipmentMapper hmeEquipmentMapper, HmeEquipmentStocktakeDomainService hmeEquipmentStocktakeDomainService, MtErrorMessageRepository mtErrorMessageRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.hmeEquipmentMapper = hmeEquipmentMapper;
        this.hmeEquipmentStocktakeDomainService = hmeEquipmentStocktakeDomainService;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    @Override
    public void update(HmeEquipmentStocktakeDocModifyCommand command) {
        repository.save(assembler.modifyCommandToEntity(command));
    }

    @Override
    public void complete(HmeEquipmentStocktakeDocActionCommand command) {
        repository.save(assembler.actionCommandToEntity(command));
    }

    @Override
    public void cancel(HmeEquipmentStocktakeDocActionCommand command) {
        repository.save(assembler.actionCommandToEntity(command));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEquipmentStocktakeDocRepresentation createDoc(Long tenantId, HmeEquipmentVO equipmentVO) {
        // 查询设备信息
        equipmentVO.setTenantId(tenantId);
        equipmentVO.setBusinessIdList(StringUtils.isNotBlank(equipmentVO.getBusinessId()) ? Arrays.asList(equipmentVO.getBusinessId().split(",")) : null);
        equipmentVO.setWorkcellCodeIdList(StringUtils.isNotBlank(equipmentVO.getWorkcellCodeId()) ? Arrays.asList(equipmentVO.getWorkcellCodeId().split(",")) : null);
        equipmentVO.setEquipmentCategoryList(StringUtils.isNotBlank(equipmentVO.getEquipmentCategory()) ? Arrays.asList(equipmentVO.getEquipmentCategory().split(",")) : null);
        List<HmeEquipment> hmeEquipmentList = hmeEquipmentMapper.queryEquipmentList2(equipmentVO);
        if (StringUtils.isBlank(equipmentVO.getStocktakeType())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "盘点类型"));
        }
        if (StringUtils.isBlank(equipmentVO.getStocktakeStatus())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "单据状态"));
        }
        // 创建盘点
        HmeEquipmentStocktakeDocCreateCommand command = new HmeEquipmentStocktakeDocCreateCommand();
        command.setBusinessId(equipmentVO.getBusinessId());
        command.setEquipmentList(hmeEquipmentList);
        command.setLedgerType(CollectionUtils.isNotEmpty(equipmentVO.getLedgerTypeList()) ? StringUtils.join(equipmentVO.getLedgerTypeList(), ",") : null);
        command.setPostingDateFrom(StringUtils.isNotBlank(equipmentVO.getPostingDateStart()) ? DateUtil.string2Date(equipmentVO.getPostingDateStart(), "yyyy-MM-dd HH:mm:ss") : null);
        command.setPostingDateTo(StringUtils.isNotBlank(equipmentVO.getPostingDateEnd()) ? DateUtil.string2Date(equipmentVO.getPostingDateEnd(), "yyyy-MM-dd HH:mm:ss") : null);
        command.setStocktakeId(equipmentVO.getStocktakeId());
        command.setStocktakeStatus(equipmentVO.getStocktakeStatus());
        command.setStocktakeType(equipmentVO.getStocktakeType());
        command.setStocktakeRange(equipmentVO.getStocktakeRange());
        command.setTenantId(tenantId);
        HmeEquipmentStocktakeDocRepresentation representation = hmeEquipmentStocktakeDomainService.createDoc(command);
        return representation;
    }
}
