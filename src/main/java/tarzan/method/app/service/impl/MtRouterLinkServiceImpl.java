package tarzan.method.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtRouterLinkDTO;
import tarzan.method.api.dto.MtRouterLinkDTO2;
import tarzan.method.app.service.MtRouterLinkService;
import tarzan.method.domain.entity.MtRouterLink;
import tarzan.method.domain.entity.MtRouterLinkHis;
import tarzan.method.domain.repository.MtRouterLinkHisRepository;
import tarzan.method.domain.repository.MtRouterLinkRepository;
import tarzan.method.infra.mapper.MtRouterLinkMapper;

/**
 * 嵌套工艺路线步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterLinkServiceImpl implements MtRouterLinkService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterLinkRepository mtRouterLinkRepo;

    @Autowired
    private MtRouterLinkHisRepository mtRouterLinkHisRepo;

    @Autowired
    private MtRouterLinkMapper mtRouterLinkMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterLinkForUi(Long tenantId, String routerStepId, MtRouterLinkDTO dto, String eventId) {
        MtRouterLink mtRouterLink = new MtRouterLink();
        BeanUtils.copyProperties(dto, mtRouterLink);
        mtRouterLink.setTenantId(tenantId);
        mtRouterLink.setRouterStepId(routerStepId);

        MtRouterLink queryRouterLink = new MtRouterLink();
        queryRouterLink.setTenantId(tenantId);
        queryRouterLink.setRouterStepId(routerStepId);
        queryRouterLink = mtRouterLinkRepo.selectOne(queryRouterLink);
        if (StringUtils.isEmpty(mtRouterLink.getRouterLinkId())) {
            if (null != queryRouterLink) {
                throw new MtException("MT_ROUTER_0062",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0062", "ROUTER"));
            }
            mtRouterLinkRepo.insertSelective(mtRouterLink);
        } else {
            if (queryRouterLink != null && !mtRouterLink.getRouterLinkId().equals(queryRouterLink.getRouterLinkId())) {
                throw new MtException("MT_ROUTER_0062",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0062", "ROUTER"));
            }
            mtRouterLinkRepo.updateByPrimaryKeySelective(mtRouterLink);
        }

        MtRouterLinkHis his = new MtRouterLinkHis();
        BeanUtils.copyProperties(mtRouterLink, his);
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        mtRouterLinkHisRepo.insertSelective(his);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterLinkForUi(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterLink mtRouterLink = new MtRouterLink();
        mtRouterLink.setTenantId(tenantId);
        mtRouterLink.setRouterStepId(routerStepId);

        mtRouterLinkRepo.delete(mtRouterLink);
    }

    @Override
    public MtRouterLinkDTO2 queryRouterLinkDetailForUi(Long tenantId, String routerId) {

        return mtRouterLinkMapper.queryRouterLinkDetailForUi(tenantId, routerId);
    }
}
