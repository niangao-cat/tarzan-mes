package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeProductionGroupDTO;
import com.ruike.hme.api.dto.HmeProductionGroupDTO3;
import com.ruike.hme.domain.entity.HmeProductionGroupLine;
import com.ruike.hme.domain.repository.HmeProductionGroupLineRepository;
import com.ruike.hme.infra.mapper.HmeProductionGroupLineMapper;
import com.ruike.hme.infra.mapper.HmeProductionGroupMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeProductionGroup;
import com.ruike.hme.domain.repository.HmeProductionGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 产品组 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-05-27 13:47:50
 */
@Component
public class HmeProductionGroupRepositoryImpl extends BaseRepositoryImpl<HmeProductionGroup> implements HmeProductionGroupRepository {

    @Autowired
    private HmeProductionGroupMapper hmeProductionGroupMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeProductionGroupLineRepository hmeProductionGroupLineRepository;

    @Autowired
    private HmeProductionGroupLineMapper hmeProductionGroupLineMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeProductionGroup createOrUpdate(Long tenantId, HmeProductionGroupDTO dto) {
        HmeProductionGroup hmeProductionGroup = this.selectOne(new HmeProductionGroup() {{
            setSiteId(dto.getSiteId());
            setProductionGroupCode(dto.getProductionGroupCode());
            setTenantId(tenantId);
        }});
        if(StringUtils.isBlank(dto.getProductionGroupId())){
            //新增时，进行唯一性校验 站点+产品组编码+租户不允许重复
            if(Objects.nonNull(hmeProductionGroup)){
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0006", "GENERAL"));
            }
            hmeProductionGroup = new HmeProductionGroup();
            hmeProductionGroup.setTenantId(tenantId);
            BeanCopierUtil.copy(dto, hmeProductionGroup);
            this.insertSelective(hmeProductionGroup);
        }else {
            //更新时，进行唯一性校验 站点+产品组编码+租户不允许重复
            if(Objects.nonNull(hmeProductionGroup) && !hmeProductionGroup.getProductionGroupId().equals(dto.getProductionGroupId())){
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0006", "GENERAL"));
            }
            hmeProductionGroup = this.selectByPrimaryKey(dto.getProductionGroupId());
            BeanCopierUtil.copy(dto, hmeProductionGroup);
            hmeProductionGroupMapper.updateByPrimaryKey(hmeProductionGroup);
        }
        return hmeProductionGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeProductionGroupLine createOrUpdateLine(Long tenantId, HmeProductionGroupDTO3 dto) {
        HmeProductionGroupLine hmeProductionGroupLine = hmeProductionGroupLineRepository.selectOne(new HmeProductionGroupLine() {{
            setProductionGroupId(dto.getProductionGroupId());
            setMaterialId(dto.getMaterialId());
            setProductionVersion(dto.getProductionVersion());
            setTenantId(tenantId);
        }});
        if(StringUtils.isBlank(dto.getProductionGroupLineId())){
            //新增时，进行唯一性校验 头ID+物料+生产版本+租户不允许重复
            if(Objects.nonNull(hmeProductionGroupLine)){
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0006", "GENERAL"));
            }
            hmeProductionGroupLine = new HmeProductionGroupLine();
            hmeProductionGroupLine.setTenantId(tenantId);
            BeanCopierUtil.copy(dto, hmeProductionGroupLine);
            hmeProductionGroupLineRepository.insertSelective(hmeProductionGroupLine);
        }else {
            //更新时，进行唯一性校验 头ID+物料+生产版本+租户不允许重复
            if(Objects.nonNull(hmeProductionGroupLine) && !hmeProductionGroupLine.getProductionGroupLineId().equals(dto.getProductionGroupLineId())){
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0006", "GENERAL"));
            }
            hmeProductionGroupLine = hmeProductionGroupLineRepository.selectByPrimaryKey(dto.getProductionGroupLineId());
            BeanCopierUtil.copy(dto, hmeProductionGroupLine);
            hmeProductionGroupLineMapper.updateByPrimaryKey(hmeProductionGroupLine);
        }
        return hmeProductionGroupLine;
    }
}
