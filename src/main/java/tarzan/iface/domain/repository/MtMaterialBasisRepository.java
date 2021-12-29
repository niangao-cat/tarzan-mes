package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.vo.MtMaterialBasisVO1;
import tarzan.iface.domain.vo.MtMaterialBasisVO2;

/**
 * 物料业务属性表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:54
 */
public interface MtMaterialBasisRepository
                extends BaseRepository<MtMaterialBasic>, AopProxy<MtMaterialBasisRepository> {

    /**
     * materialBasicPropertyGet-物料基础属性获取
     * 
     * @author benjamin
     * @date 2019-07-15 13:25
     * @param tenantId
     * @param siteId 站点Id
     * @param materialId 物料Id
     * @return MtMaterialBasis
     */
    MtMaterialBasic materialBasicPropertyGet(Long tenantId, String siteId, String materialId);

    /**
     * propertyLimitMaterialBasicPropertyQuery-根据属性获取物料ERP信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtMaterialBasisVO2> propertyLimitMaterialBasicPropertyQuery(Long tenantId, MtMaterialBasisVO1 dto);
}
