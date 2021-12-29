package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeProductionGroupDTO;
import com.ruike.hme.api.dto.HmeProductionGroupDTO2;
import com.ruike.hme.api.dto.HmeProductionGroupDTO3;
import com.ruike.hme.app.service.HmeProductionGroupService;
import com.ruike.hme.domain.entity.HmeProductionGroup;
import com.ruike.hme.domain.entity.HmeProductionGroupLine;
import com.ruike.hme.domain.repository.HmeProductionGroupRepository;
import com.ruike.hme.domain.vo.HmeProductionGroupVO;
import com.ruike.hme.domain.vo.HmeProductionGroupVO2;
import com.ruike.hme.infra.mapper.HmeProductionGroupMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 产品组应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-05-27 13:47:50
 */
@Service
public class HmeProductionGroupServiceImpl implements HmeProductionGroupService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeProductionGroupRepository hmeProductionGroupRepository;

    @Autowired
    private HmeProductionGroupMapper hmeProductionGroupMapper;

    @Override
    public HmeProductionGroup createOrUpdate(Long tenantId, HmeProductionGroupDTO dto) {
        if(StringUtils.isBlank(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(StringUtils.isBlank(dto.getProductionGroupCode())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "产品组编码"));
        }
        if(StringUtils.isBlank(dto.getEnableFlag())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "有效性"));
        }
        return hmeProductionGroupRepository.createOrUpdate(tenantId, dto);
    }

    @Override
    public HmeProductionGroupLine createOrUpdateLine(Long tenantId, HmeProductionGroupDTO3 dto) {
        if(StringUtils.isBlank(dto.getProductionGroupId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "头Id"));
        }
        if(StringUtils.isBlank(dto.getMaterialId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "物料"));
        }
        if(StringUtils.isBlank(dto.getEnableFlag())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "有效性"));
        }
        if(StringUtils.isBlank(dto.getProductionVersion())){
            dto.setProductionVersion("");
        }
        return hmeProductionGroupRepository.createOrUpdateLine(tenantId, dto);
    }

    @Override
    @ProcessLovValue
    public Page<HmeProductionGroupVO> pageQuery(Long tenantId, HmeProductionGroupDTO2 dto, PageRequest pageRequest) {
        Page<HmeProductionGroupVO> resultPage = PageHelper.doPage(pageRequest, () -> hmeProductionGroupMapper.listQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    @ProcessLovValue
    public Page<HmeProductionGroupVO2> linePageQuery(Long tenantId, String productionGroupId, PageRequest pageRequest) {
        Page<HmeProductionGroupVO2> resultPage = PageHelper.doPage(pageRequest, () -> hmeProductionGroupMapper.linePageQuery(tenantId, productionGroupId));
        return resultPage;
    }
}
