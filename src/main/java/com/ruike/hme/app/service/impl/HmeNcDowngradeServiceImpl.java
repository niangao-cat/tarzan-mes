package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeNcDowngradeDTO;
import com.ruike.hme.api.dto.HmeNcDowngradeDTO2;
import com.ruike.hme.api.dto.HmeNcDowngradeDTO3;
import com.ruike.hme.app.service.HmeNcDowngradeService;
import com.ruike.hme.domain.entity.HmeNcDowngrade;
import com.ruike.hme.domain.repository.HmeNcDowngradeRepository;
import com.ruike.hme.domain.vo.HmeNcDowngradeVO;
import com.ruike.hme.domain.vo.HmeNcDowngradeVO2;
import com.ruike.hme.infra.mapper.HmeNcDowngradeMapper;
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
 * 产品降级关系维护应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-05-18 11:35:47
 */
@Service
public class HmeNcDowngradeServiceImpl implements HmeNcDowngradeService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeNcDowngradeRepository hmeNcDowngradeRepository;

    @Autowired
    private HmeNcDowngradeMapper hmeNcDowngradeMapper;

    @Override
    public HmeNcDowngrade createOrUpdate(Long tenantId, HmeNcDowngradeDTO dto) {
        if(StringUtils.isEmpty(dto.getMaterialId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "物料"));
        }
        if(StringUtils.isEmpty(dto.getNcCodeId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "不良代码"));
        }
        if(StringUtils.isEmpty(dto.getTransitionMaterialId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "降级物料"));
        }
        if(StringUtils.isEmpty(dto.getEnableFlag())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "有效性"));
        }
        return hmeNcDowngradeRepository.createOrUpdate(tenantId, dto);
    }

    @Override
    @ProcessLovValue
    public Page<HmeNcDowngradeVO> pageQuery(Long tenantId, HmeNcDowngradeDTO2 dto, PageRequest pageRequest) {
        Page<HmeNcDowngradeVO> resultPage = PageHelper.doPage(pageRequest, ()->hmeNcDowngradeMapper.pageQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    @ProcessLovValue
    public Page<HmeNcDowngradeVO2> hisPageQuery(Long tenantId, HmeNcDowngradeDTO3 dto, PageRequest pageRequest) {
        if(StringUtils.isEmpty(dto.getDowngradeId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "downgradeId"));
        }
        Page<HmeNcDowngradeVO2> resultPage = PageHelper.doPage(pageRequest, ()->hmeNcDowngradeMapper.hisPageQuery(tenantId, dto));
        return resultPage;
    }
}
