package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.vo.MaterialSiteVO;

/**
 * 物料站点分配Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialSiteMapper extends BaseMapper<MtMaterialSite> {

    List<MtMaterialSite> selectEnableMaterial(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtMaterialSite dto);

    MtMaterialSite selectPfepItem(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtMaterialSite dto);

    List<MaterialSiteVO> selectMaterialSiteById(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "materialId") String materialId);

    List<MtMaterialSite> queryMaterialSiteByMaterialId(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "materialIdList") List<String> materialIdList);


    List<MtMaterialSite> queryMaterialSiteByItemId(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "itemIds") String itemIds);

    List<MtMaterialSite> queryByMaterialSiteId(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "materialSiteIds") List<String> materialSiteIds);

    void batchInsert(@Param("sites") List<MtMaterialSite> sites);
}
