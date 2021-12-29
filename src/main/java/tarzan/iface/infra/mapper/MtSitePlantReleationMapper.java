package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.vo.MtSitePlantReleationVO2;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;

/**
 * ERP工厂与站点映射关系Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:02
 */
public interface MtSitePlantReleationMapper extends BaseMapper<MtSitePlantReleation> {
    /***
     * 根据和materialIdList与siteId获取物料站点信息
     *
     */
    List<MtMaterialSite> getMaterialSitelList(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtSitePlantReleationVO2 dto);

    /***
     * 根据和materialCodeList 获取materialIds
     *
     */
    List<MtMaterial> getMaterialIdList(@Param(value = "tenantId") Long tenantId,
                                       @Param(value = "materialCodes") String materialCodes);

    /***
     * 根据和plantCodeList获取siteId
     *
     */
    List<MtSitePlantReleation> getsiteIdList(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "dto") MtSitePlantReleationVO3 dto);
}
