package com.ruike.hme.domain.service.impl;

import com.ruike.hme.api.dto.HmeFreezePrivilegeSaveCommandDTO;
import com.ruike.hme.domain.entity.HmeFreezePrivilegeDetail;
import com.ruike.hme.domain.repository.HmeFreezePrivilegeDetailRepository;
import com.ruike.hme.domain.repository.HmeFreezePrivilegeRepository;
import com.ruike.hme.domain.service.HmeFreezePrivilegeDomainService;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 条码冻结权限 领域服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 11:22
 */
@Service
public class HmeFreezePrivilegeDomainServiceImpl implements HmeFreezePrivilegeDomainService {
    private final HmeFreezePrivilegeRepository privilegeRepository;
    private final HmeFreezePrivilegeDetailRepository detailRepository;

    public HmeFreezePrivilegeDomainServiceImpl(HmeFreezePrivilegeRepository privilegeRepository, HmeFreezePrivilegeDetailRepository detailRepository) {
        this.privilegeRepository = privilegeRepository;
        this.detailRepository = detailRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public HmeFreezePrivilegeVO save(HmeFreezePrivilegeSaveCommandDTO dto) {
        // 执行保存
        HmeFreezePrivilegeVO vo = privilegeRepository.save(HmeFreezePrivilegeSaveCommandDTO.toEntity(dto));
        List<HmeFreezePrivilegeSaveCommandDTO.LineCommand> lineList = dto.getLineList();
        if (CollectionUtils.isNotEmpty(lineList)) {
            lineList.forEach(r -> {
                r.setPrivilegeId(StringUtils.isBlank(r.getPrivilegeId()) ? vo.getPrivilegeId() : r.getPrivilegeId());
                r.setTenantId(dto.getTenantId());
            });
            List<HmeFreezePrivilegeDetail> detailList = HmeFreezePrivilegeSaveCommandDTO.lineBatchToEntity(lineList);
            detailRepository.batchSave(detailList);
        }
        return vo;
    }
}
