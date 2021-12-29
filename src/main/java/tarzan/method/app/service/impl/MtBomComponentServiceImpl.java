package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.method.app.service.MtBomComponentService;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.vo.MtBomComponentVO10;
import tarzan.method.domain.vo.MtBomComponentVO15;
import tarzan.method.domain.vo.MtBomComponentVO4;
import tarzan.method.domain.vo.MtBomComponentVO8;
import tarzan.method.domain.vo.MtBomComponentVO9;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

/**
 * 装配清单行应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Service
public class MtBomComponentServiceImpl extends BaseServiceImpl<MtBomComponent> implements MtBomComponentService {

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Override
    public MtBomComponentVO4 bomComponentRecordForUi(Long tenantId, String bomComponentId) {
        MtBomComponentVO8 mtBomComponentVO8 =
                        this.mtBomComponentRepository.bomComponentBasicGet(tenantId, bomComponentId);
        if (null == mtBomComponentVO8) {
            return null;
        }

        MtBomComponentVO4 result = new MtBomComponentVO4();
        BeanUtils.copyProperties(mtBomComponentVO8, result);

        if (StringUtils.isNotEmpty(mtBomComponentVO8.getMaterialId())) {
            MtMaterialVO mtMaterialVO = this.mtMaterialRepository.materialPropertyGet(tenantId, result.getMaterialId());
            result.setMaterialCode(mtMaterialVO.getMaterialCode());
            result.setMaterialName(mtMaterialVO.getMaterialName());

            if (StringUtils.isNotEmpty(mtMaterialVO.getPrimaryUomId())) {
                MtUomVO mtUomVO = this.mtUomRepository.uomPropertyGet(tenantId, mtMaterialVO.getPrimaryUomId());
                if (null != mtUomVO) {
                    result.setUomName(mtUomVO.getUomName());
                }
            }
        }

        if (StringUtils.isNotEmpty(mtBomComponentVO8.getBomComponentType())) {
            List<MtGenType> bomCompTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "BOM", "BOM_COMPONENT_TYPE");
            Optional<MtGenType> optional = bomCompTypes.stream()
                            .filter(t -> t.getTypeCode().equals(mtBomComponentVO8.getBomComponentType())).findFirst();
            if (optional.isPresent()) {
                result.setBomComponentTypeDesc(optional.get().getDescription());
            }
        }

        if (StringUtils.isNotEmpty(mtBomComponentVO8.getAssembleMethod())) {
            List<MtGenType> assembleMethods = this.mtGenTypeRepository.getGenTypes(tenantId, "MATERIAL", "ASSY_METHOD");
            Optional<MtGenType> optional = assembleMethods.stream()
                            .filter(t -> t.getTypeCode().equals(mtBomComponentVO8.getAssembleMethod())).findFirst();
            if (optional.isPresent()) {
                result.setAssembleMethodDesc(optional.get().getDescription());
            }
        }

        if (StringUtils.isNotEmpty(mtBomComponentVO8.getIssuedLocatorId())) {
            MtModLocator mtModLocator = this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId,
                            mtBomComponentVO8.getIssuedLocatorId());
            if (null != mtModLocator) {
                result.setIssuedLocatorCode(mtModLocator.getLocatorCode());
                result.setIssuedLocatorName(mtModLocator.getLocatorName());
            }
        }
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveBomComponentForUi(Long tenantId, MtBomComponentVO4 dto) {
        MtBomComponentVO10 vo = new MtBomComponentVO10();
        vo.setBomId(dto.getBomId());

        List<MtBomComponentVO9> bomComponents = new ArrayList<MtBomComponentVO9>();
        MtBomComponentVO9 bomComponent = new MtBomComponentVO9();
        BeanUtils.copyProperties(dto, bomComponent);
        bomComponents.add(bomComponent);
        vo.setBomComponents(bomComponents);

        MtBomComponentVO15 mtBomComponentVO15 = this.mtBomComponentRepository.bomComponentUpdate(tenantId, vo,"Y");
        if (null == mtBomComponentVO15 || CollectionUtils.isEmpty(mtBomComponentVO15.getBomComponentId())) {
            return null;
        }
        return mtBomComponentVO15.getBomComponentId().get(0);
    }
}
