package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.service.BaseServiceImpl;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.app.service.MtBomSubstituteService;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.repository.MtBomSubstituteRepository;
import tarzan.method.domain.vo.MtBomSubstituteVO10;
import tarzan.method.domain.vo.MtBomSubstituteVO9;
import tarzan.method.infra.mapper.MtBomSubstituteMapper;

/**
 * 装配清单行替代项应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Service
public class MtBomSubstituteServiceImpl extends BaseServiceImpl<MtBomSubstitute> 
                                                    implements MtBomSubstituteService {
    
    @Autowired
    private MtBomSubstituteMapper mtBomSubstituteMapper;
    
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    
    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Override
    public List<MtBomSubstituteVO9> bomSubstituteRecordForUi(Long tenantId, String bomSubstituteGroupId) {
        MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
        mtBomSubstitute.setTenantId(tenantId);
        mtBomSubstitute.setBomSubstituteGroupId(bomSubstituteGroupId);
        List<MtBomSubstitute> mtBomSubstitutes = this.mtBomSubstituteMapper.select(mtBomSubstitute);
        if (CollectionUtils.isEmpty(mtBomSubstitutes)) {
            return Collections.emptyList();
        }

        List<MtBomSubstituteVO9> result = new ArrayList<MtBomSubstituteVO9>();
        MtBomSubstituteVO9 vo = null;

        List<String> materialIds =
                        mtBomSubstitutes.stream().map(MtBomSubstitute::getMaterialId).collect(Collectors.toList());
        List<MtMaterialVO> materials = this.mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);

        for (MtBomSubstitute bomSubstitute : mtBomSubstitutes) {
            vo = new MtBomSubstituteVO9();
            BeanUtils.copyProperties(bomSubstitute, vo);

            if (StringUtils.isNotEmpty(bomSubstitute.getMaterialId())) {
                Optional<MtMaterialVO> material = materials.stream()
                                .filter(t -> t.getMaterialId().equals(bomSubstitute.getMaterialId())).findFirst();
                if (material.isPresent()) {
                    vo.setMaterialCode(material.get().getMaterialCode());
                    vo.setMaterialName(material.get().getMaterialName());
                }
            }
            result.add(vo);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveBomSubstituteForUi(Long tenantId, MtBomSubstituteVO9 dto) {
        MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
        BeanUtils.copyProperties(dto, mtBomSubstitute);

        MtBomSubstituteVO10 mtBomSubstituteVO10 =
                this.mtBomSubstituteRepository.bomSubstituteUpdate(tenantId, mtBomSubstitute, "Y");
        return mtBomSubstituteVO10.getBomSubstituteId();
    }

}
