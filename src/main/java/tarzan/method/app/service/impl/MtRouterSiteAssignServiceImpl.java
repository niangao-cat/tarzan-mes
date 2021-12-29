package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.api.dto.MtRouterSiteAssignDTO2;
import tarzan.method.app.service.MtRouterSiteAssignService;
import tarzan.method.domain.entity.MtRouterSiteAssign;
import tarzan.method.domain.entity.MtRouterSiteAssignHis;
import tarzan.method.domain.repository.MtRouterSiteAssignHisRepository;
import tarzan.method.domain.repository.MtRouterSiteAssignRepository;
import tarzan.method.infra.mapper.MtRouterSiteAssignMapper;

/**
 * 工艺路线站点分配表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@Service
public class MtRouterSiteAssignServiceImpl implements MtRouterSiteAssignService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepo;

    @Autowired
    private MtRouterSiteAssignRepository mtRouterSiteAssignRepo;

    @Autowired
    private MtRouterSiteAssignHisRepository mtRouterSiteAssignHisRepo;

    @Autowired
    private MtRouterSiteAssignMapper mtRouterSiteAssignMapper;

    @Override
    public List<MtRouterSiteAssignDTO2> queryRouterSiteAssignListForUi(Long tenantId, String routerId,
                                                                       PageRequest pageRequest) {
        return mtRouterSiteAssignMapper.queryRouterSiteAssignForUi(tenantId, routerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRouterSiteAssignForUi(Long tenantId, MtRouterSiteAssignDTO2 dto) {
        MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
        BeanUtils.copyProperties(dto, mtRouterSiteAssign);
        mtRouterSiteAssign.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtRouterSiteAssign);
        List<WhereField> whereFields = new ArrayList<>();
        if (StringUtils.isNotEmpty(dto.getRouterSiteAssignId())) {
            whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_ROUTER_SITE_ASSIGN_ID, Comparison.NOT_EQUAL));
        }
        whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_ROUTER_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_SITE_ID, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        if (CollectionUtils.isNotEmpty(mtRouterSiteAssignRepo.selectOptional(mtRouterSiteAssign, criteria))) {
            throw new MtException("MT_ROUTER_0067",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0067", "ROUTER"));
        }

        if (StringUtils.isEmpty(dto.getRouterSiteAssignId())) {
            mtRouterSiteAssignRepo.insertSelective(mtRouterSiteAssign);
        } else {
            mtRouterSiteAssignRepo.updateByPrimaryKeySelective(mtRouterSiteAssign);
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ROUTER_UPDATE");
        String eventId = mtEventRepo.eventCreate(tenantId, eventCreateVO);

        // save his
        MtRouterSiteAssignHis his = new MtRouterSiteAssignHis();
        BeanUtils.copyProperties(mtRouterSiteAssign, his);
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        mtRouterSiteAssignHisRepo.insertSelective(his);

        return mtRouterSiteAssign.getRouterSiteAssignId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterSiteAssignForUi(Long tenantId, String routerSiteAssignId) {
        if (StringUtils.isEmpty(routerSiteAssignId)) {
            return;
        }

        MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
        mtRouterSiteAssign.setTenantId(tenantId);
        mtRouterSiteAssign.setRouterSiteAssignId(routerSiteAssignId);

        mtRouterSiteAssignRepo.delete(mtRouterSiteAssign);
    }
}
