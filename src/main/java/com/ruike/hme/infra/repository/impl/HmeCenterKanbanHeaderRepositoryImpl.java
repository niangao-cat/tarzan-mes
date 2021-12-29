package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO;
import com.ruike.hme.api.dto.HmeCenterKanbanLineDTO;
import com.ruike.hme.domain.entity.HmeCenterKanbanLine;
import com.ruike.hme.domain.repository.HmeCenterKanbanLineRepository;
import com.ruike.hme.infra.mapper.HmeCenterKanbanHeaderMapper;
import com.ruike.hme.infra.mapper.HmeCenterKanbanLineMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCenterKanbanHeader;
import com.ruike.hme.domain.repository.HmeCenterKanbanHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 制造中心看板信息头表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-05-28 13:49:51
 */
@Component
public class HmeCenterKanbanHeaderRepositoryImpl extends BaseRepositoryImpl<HmeCenterKanbanHeader> implements HmeCenterKanbanHeaderRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeCenterKanbanHeaderMapper hmeCenterKanbanHeaderMapper;
    @Autowired
    private HmeCenterKanbanLineRepository hmeCenterKanbanLineRepository;
    @Autowired
    private HmeCenterKanbanLineMapper hmeCenterKanbanLineMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCenterKanbanHeader createOrUpdateHeader(Long tenantId, HmeCenterKanbanHeaderDTO dto) {
        HmeCenterKanbanHeader hmeCenterKanbanHeader = this.selectOne(new HmeCenterKanbanHeader() {{
            setTenantId(tenantId);
            setSiteId(dto.getSiteId());
            setKanbanArea(dto.getKanbanArea());
            setProdLineId(dto.getProdLineId());
            setBusinessId(dto.getBusinessId());
            setMaterialId(dto.getMaterialId());
            setProductionGroupId(dto.getProductionGroupId());
            setWorkcellId(dto.getWorkcellId());
        }});
        if(StringUtils.isBlank(dto.getCenterKanbanHeaderId())){
            //新增时，进行唯一性校验
            if(Objects.nonNull(hmeCenterKanbanHeader)){
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0006", "GENERAL"));
            }
            hmeCenterKanbanHeader = new HmeCenterKanbanHeader();
            hmeCenterKanbanHeader.setTenantId(tenantId);
            BeanCopierUtil.copy(dto, hmeCenterKanbanHeader);
            this.insertSelective(hmeCenterKanbanHeader);
        }else {
            //更新时，进行唯一性校验
            if(Objects.nonNull(hmeCenterKanbanHeader) && !hmeCenterKanbanHeader.getCenterKanbanHeaderId().equals(dto.getCenterKanbanHeaderId())){
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0006", "GENERAL"));
            }
            //这里只会更新有效性和目标直通率,因目标直通率可能置为空，故使用updateByPrimaryKey
            hmeCenterKanbanHeader = this.selectByPrimaryKey(dto.getCenterKanbanHeaderId());
            hmeCenterKanbanHeader.setEnableFlag(dto.getEnableFlag());
            hmeCenterKanbanHeader.setThroughRate(dto.getThroughRate());
            hmeCenterKanbanHeaderMapper.updateByPrimaryKey(hmeCenterKanbanHeader);
        }
        return hmeCenterKanbanHeader;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCenterKanbanLine createOrUpdateLine(Long tenantId, HmeCenterKanbanLineDTO dto) {
        HmeCenterKanbanLine hmeCenterKanbanLine = hmeCenterKanbanLineRepository.selectOne(new HmeCenterKanbanLine() {{
            setTenantId(tenantId);
            setCenterKanbanHeaderId(dto.getCenterKanbanHeaderId());
            setWorkcellId(dto.getWorkcellId());
        }});
        if(StringUtils.isBlank(dto.getCenterKanbanLineId())){
            //新增时，进行唯一性校验
            if(Objects.nonNull(hmeCenterKanbanLine)){
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0006", "GENERAL"));
            }
            hmeCenterKanbanLine = new HmeCenterKanbanLine();
            hmeCenterKanbanLine.setTenantId(tenantId);
            BeanCopierUtil.copy(dto, hmeCenterKanbanLine);
            hmeCenterKanbanLineRepository.insertSelective(hmeCenterKanbanLine);
        }else {
            //更新时，进行唯一性校验
            if(Objects.nonNull(hmeCenterKanbanLine) && !hmeCenterKanbanLine.getCenterKanbanLineId().equals(dto.getCenterKanbanLineId())){
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0006", "GENERAL"));
            }
            //这里只会更新有效性和直通率
            hmeCenterKanbanLine = hmeCenterKanbanLineRepository.selectByPrimaryKey(dto.getCenterKanbanLineId());
            hmeCenterKanbanLine.setThroughRate(dto.getThroughRate());
            hmeCenterKanbanLine.setEnableFlag(dto.getEnableFlag());
            hmeCenterKanbanLineMapper.updateByPrimaryKeySelective(hmeCenterKanbanLine);
        }
        return hmeCenterKanbanLine;
    }
}
