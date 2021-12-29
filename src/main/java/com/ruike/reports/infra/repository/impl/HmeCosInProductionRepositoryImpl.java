package com.ruike.reports.infra.repository.impl;

import com.ruike.reports.api.dto.HmeCosInProductionDTO;
import com.ruike.reports.domain.repository.HmeCosInProductionRepository;
import com.ruike.reports.domain.vo.HmeCosInProductionVO;
import com.ruike.reports.infra.mapper.HmeCosInProductionMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserClient;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * COS在制报表 资源库实现
 *
 * @author 35113 2021/01/27 12:50
 */
@Component
public class HmeCosInProductionRepositoryImpl implements HmeCosInProductionRepository {

    @Autowired
    private HmeCosInProductionMapper hmeCosInProductionMapper;

    @Autowired
    private MtUserClient userClient;

    /**
     * COS在制报表 资源库查询
     *
     * @param tenant
     * @param hmeCosInProductionDTO
     * @param pagerequest
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.HmeCosInProductionVO>
     * @auther wenqiang.yin@hand-china.com 2021/1/27 12:49
     */
    @Override
    @ProcessLovValue
    public Page<HmeCosInProductionVO> pageList(Long tenant, HmeCosInProductionDTO hmeCosInProductionDTO, PageRequest pagerequest) {
        Page<HmeCosInProductionVO> hmeCosInProductionVOs = PageHelper.doPageAndSort(pagerequest, () -> hmeCosInProductionMapper.selectListByCondition(tenant, hmeCosInProductionDTO));
        for (HmeCosInProductionVO hmeCosInProductionVO:
             hmeCosInProductionVOs) {
            String userRealName = userClient.userInfoGet(tenant, hmeCosInProductionVO.getCreatedBy()).getRealName();
            hmeCosInProductionVO.setCreatedByName(userRealName);
        }
        return hmeCosInProductionVOs;
    }
}
