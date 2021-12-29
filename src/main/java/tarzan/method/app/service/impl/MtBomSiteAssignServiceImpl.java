package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import tarzan.method.api.dto.MtBomSiteAssignDTO;
import tarzan.method.app.service.MtBomSiteAssignService;
import tarzan.method.domain.entity.MtBomSiteAssign;
import tarzan.method.domain.repository.MtBomSiteAssignRepository;
import tarzan.method.domain.vo.MtBomSiteAssignVO;
import tarzan.method.infra.mapper.MtBomSiteAssignMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 装配清单站点分配应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Service
public class MtBomSiteAssignServiceImpl extends BaseServiceImpl<MtBomSiteAssign>
                            implements MtBomSiteAssignService {
    
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    
    @Autowired
    private MtBomSiteAssignRepository mtBomSiteAssignRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtBomSiteAssignMapper mtBomSiteAssignMapper;


    @Override
    public List<MtBomSiteAssignVO> listBomSiteAssignUi(Long tenantId, String bomId) {
        List<MtBomSiteAssign> bomSiteAssigns =
                        this.mtBomSiteAssignRepository.bomLimitBomSiteAssignBatchQuery(tenantId, Arrays.asList(bomId));
        if (CollectionUtils.isEmpty(bomSiteAssigns)) {
            return Collections.emptyList();
        }

        List<MtBomSiteAssignVO> result = new ArrayList<MtBomSiteAssignVO>();
        MtBomSiteAssignVO mtBomSiteAssignVO = null;
        for (MtBomSiteAssign bomSiteAssign : bomSiteAssigns) {
            mtBomSiteAssignVO = new MtBomSiteAssignVO();
            BeanUtils.copyProperties(bomSiteAssign, mtBomSiteAssignVO);
            result.add(mtBomSiteAssignVO);
        }

        List<String> siteIds = bomSiteAssigns.stream().map(MtBomSiteAssign::getSiteId).collect(Collectors.toList());
        List<MtModSite> sites = this.mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIds);
        if (CollectionUtils.isEmpty(sites)) {
            return result;
        }

        List<MtGenType> siteTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_REL_TYPE");

        for (MtBomSiteAssignVO vo : result) {
            Optional<MtModSite> optional = sites.stream().filter(t -> t.getSiteId().equals(vo.getSiteId())).findFirst();
            if (optional.isPresent()) {
                MtModSite mtModSite = optional.get();
                vo.setSiteCode(mtModSite.getSiteCode());
                vo.setSiteName(mtModSite.getSiteName());
                vo.setSiteType(mtModSite.getSiteType());

                Optional<MtGenType> siteType =
                                siteTypes.stream().filter(t -> t.getTypeCode().equals(vo.getSiteType())).findFirst();
                if (siteType.isPresent()) {
                    vo.setSiteTypeDesc(siteType.get().getDescription());
                }
            }
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveBomSiteAssignForUi(Long tenantId, MtBomSiteAssignDTO dto) {
        MtBomSiteAssign condition = new MtBomSiteAssign();
        BeanUtils.copyProperties(dto, condition);
        this.mtBomSiteAssignRepository.bomSiteAssign(tenantId, condition);

        MtBomSiteAssign mtBomSiteAssign = new MtBomSiteAssign();
        mtBomSiteAssign.setTenantId(tenantId);
        mtBomSiteAssign.setBomId(dto.getBomId());
        mtBomSiteAssign.setSiteId(dto.getSiteId());
        mtBomSiteAssign = this.mtBomSiteAssignMapper.selectOne(mtBomSiteAssign);
        return mtBomSiteAssign.getAssignId();
    }
    
}
