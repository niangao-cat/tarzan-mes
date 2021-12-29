package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.vo.MtSitePlantReleationVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO1;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;

/**
 * ERP工厂与站点映射关系资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:02
 */
public interface MtSitePlantReleationRepository
                extends BaseRepository<MtSitePlantReleation>, AopProxy<MtSitePlantReleationRepository> {
    /**
     * itemMaterialSiteIdBatchQuery-根据ERP物料查找站点物料ID批量查询
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/6/20
     */
    List<MtSitePlantReleationVO1> itemMaterialSiteIdBatchQuery(Long tenantId, MtSitePlantReleationVO dto);

    /**
     * 根据plantCode与siteType批量获取站点工厂关系
     * 
     * @param tenantId
     * @author guichuan.li
     */
    List<MtSitePlantReleation> getRelationByPlantAndSiteType(Long tenantId, MtSitePlantReleationVO3 dto);
}
