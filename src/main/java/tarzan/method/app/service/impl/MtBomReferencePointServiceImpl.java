package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.service.BaseServiceImpl;
import tarzan.method.api.dto.MtBomReferencePointDTO;
import tarzan.method.app.service.MtBomReferencePointService;
import tarzan.method.domain.entity.MtBomReferencePoint;
import tarzan.method.domain.repository.MtBomReferencePointRepository;
import tarzan.method.infra.mapper.MtBomReferencePointMapper;

/**
 * 装配清单行参考点关系应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Service
public class MtBomReferencePointServiceImpl extends BaseServiceImpl<MtBomReferencePoint>
                                                        implements MtBomReferencePointService {

    
    @Autowired
    private MtBomReferencePointRepository mtBomReferencePointRepository;
    
    @Autowired
    private MtBomReferencePointMapper mtBomReferencePointMapper;
    
    @Override
    public List<MtBomReferencePointDTO> listbomReferencePointUi(Long tenantId, String bomComponentId) {
        MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
        mtBomReferencePoint.setTenantId(tenantId);
        mtBomReferencePoint.setBomComponentId(bomComponentId);
        List<MtBomReferencePoint> mtBomReferencePoints = mtBomReferencePointMapper.select(mtBomReferencePoint);
        if (CollectionUtils.isEmpty(mtBomReferencePoints)) {
            return Collections.emptyList();
        }

        List<MtBomReferencePointDTO> result = new ArrayList<MtBomReferencePointDTO>();
        MtBomReferencePointDTO dto = null;
        for (MtBomReferencePoint bomReferencePoint : mtBomReferencePoints) {
            dto = new MtBomReferencePointDTO();
            BeanUtils.copyProperties(bomReferencePoint, dto);
            result.add(dto);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveReferencePointForUi(Long tenantId, MtBomReferencePointDTO dto) {
        MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
        BeanUtils.copyProperties(dto, mtBomReferencePoint);
        return this.mtBomReferencePointRepository.bomReferencePointUpdate(tenantId, mtBomReferencePoint);
    }

}
