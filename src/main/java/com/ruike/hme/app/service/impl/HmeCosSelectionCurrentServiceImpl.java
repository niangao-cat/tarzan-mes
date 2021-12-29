package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosSelectionCurrentDTO;
import com.ruike.hme.app.service.HmeCosSelectionCurrentService;
import com.ruike.hme.domain.repository.HmeCosSelectionCurrentRepository;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * COS筛选电流点维护表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-08-18 11:07:41
 */
@Service
public class HmeCosSelectionCurrentServiceImpl implements HmeCosSelectionCurrentService {

    @Autowired
    private HmeCosSelectionCurrentRepository hmeCosSelectionCurrentRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public Page<HmeCosSelectionCurrentVO> cosSelectionCurrentPageQuery(Long tenantId, HmeCosSelectionCurrentDTO dto, PageRequest pageRequest) {
        dto.initParam();
        return hmeCosSelectionCurrentRepository.cosSelectionCurrentPageQuery(tenantId, dto, pageRequest);
    }

    @Override
    public void cosSelectionCurrentCreateOrUpdate(Long tenantId, HmeCosSelectionCurrentVO dto) {
        if(StringUtils.isBlank(dto.getCosType())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "COS类型"));
        }
        if(StringUtils.isBlank(dto.getCurrent())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "电流点"));
        }
        if(StringUtils.isBlank(dto.getEnbaleFlag())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "有效性"));
        }
        hmeCosSelectionCurrentRepository.cosSelectionCurrentCreateOrUpdate(tenantId, dto);
    }

    @Override
    public Page<HmeCosSelectionCurrentVO2> cosSelectionCurrentHisPageQuery(Long tenantId, String cosId, PageRequest pageRequest) {
        return hmeCosSelectionCurrentRepository.cosSelectionCurrentHisPageQuery(tenantId, cosId, pageRequest);
    }
}
