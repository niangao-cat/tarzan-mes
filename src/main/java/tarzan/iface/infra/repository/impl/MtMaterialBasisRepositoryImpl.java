package tarzan.iface.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.iface.domain.vo.MtMaterialBasisVO1;
import tarzan.iface.domain.vo.MtMaterialBasisVO2;
import tarzan.iface.infra.mapper.MtMaterialBasisMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 物料业务属性表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:54
 */
@Component
public class MtMaterialBasisRepositoryImpl extends BaseRepositoryImpl<MtMaterialBasic>
                implements MtMaterialBasisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtMaterialBasisMapper mtMaterialBasisMapper;

    @Override
    public MtMaterialBasic materialBasicPropertyGet(Long tenantId, String siteId, String materialId) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_MATERIAL_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0017", "MATERIAL", "siteId", "【API:materialBasicPropertyGet】"));
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialBasicPropertyGet】"));
        }

        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, siteId);
        if (mtModSite == null) {
            throw new MtException("MT_MATERIAL_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0067", "MATERIAL", siteId, "【API:materialBasicPropertyGet】"));
        }
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setMaterialId(materialId);
        mtMaterial.setTenantId(tenantId);
        mtMaterial = mtMaterialRepository.selectOne(mtMaterial);
        if (mtMaterial == null) {
            throw new MtException("MT_MATERIAL_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0068", "MATERIAL", "materialId", "【API:materialBasicPropertyGet】"));
        }

        MtMaterialSite mtMaterialSite = new MtMaterialSite();
        mtMaterialSite.setSiteId(siteId);
        mtMaterialSite.setMaterialId(materialId);
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
        if (StringUtils.isNotEmpty(materialSiteId)) {
            MtMaterialBasic mtMaterialBasic = new MtMaterialBasic();
            mtMaterialBasic.setMaterialSiteId(materialSiteId);
            mtMaterialBasic.setTenantId(tenantId);
            return mtMaterialBasisMapper.selectOne(mtMaterialBasic);
        }
        return null;
    }

    @Override
    public List<MtMaterialBasisVO2> propertyLimitMaterialBasicPropertyQuery(Long tenantId, MtMaterialBasisVO1 dto) {
        List<MtMaterialBasisVO2> voList = mtMaterialBasisMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        // 获取materialId列表
        List<String> materialIds = voList.stream().map(MtMaterialBasisVO2::getMaterialId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        Map<String, MtMaterialVO> materialMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(materialIds)) {
            List<MtMaterialVO> materialVOList = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
            if (CollectionUtils.isNotEmpty(materialVOList)) {
                materialMap = materialVOList.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
            }
        }

        // 组装数据
        MtMaterialVO materialVO;
        for (MtMaterialBasisVO2 vo2 : voList) {
            materialVO = materialMap.get(vo2.getMaterialId());
            vo2.setMaterialCode(null != materialVO ? materialVO.getMaterialCode() : null);
            vo2.setMaterialName(null != materialVO ? materialVO.getMaterialName() : null);
        }
        voList.sort(Comparator.comparingDouble((MtMaterialBasisVO2 t) -> Double
                        .valueOf(StringUtils.isEmpty(t.getMaterialId()) ? "0" : t.getMaterialId())));
        return voList;
    }

}
