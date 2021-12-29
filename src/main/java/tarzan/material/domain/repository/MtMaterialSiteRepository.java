package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.vo.MtMaterialSiteVO;
import tarzan.material.domain.vo.MtMaterialSiteVO3;
import tarzan.material.domain.vo.MtMaterialSiteVO4;

/**
 * 物料站点分配资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialSiteRepository extends BaseRepository<MtMaterialSite>, AopProxy<MtMaterialSiteRepository> {

    /**
     * materialSiteLimitRelationGet-根据物料和站点限制验证并获取物料站点关系
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String materialSiteLimitRelationGet(Long tenantId, MtMaterialSite dto);

    /**
     * relationLimitMaterialSiteGet-根据物料站点关系获取物料和站点
     *
     * @param tenantId
     * @param materialSiteId
     * @return
     */
    MtMaterialSite relationLimitMaterialSiteGet(Long tenantId, String materialSiteId);

    /**
     * siteLimitMaterialQuery-获取站点已分配的有效物料
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> siteLimitMaterialQuery(Long tenantId, MtMaterialSite dto);

    /**
     * materialLimitSiteQuery-获取物料已分配的有效站点
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> materialLimitSiteQuery(Long tenantId, MtMaterialSite dto);

    /**
     * materialSitePfepExistValidate-验证物料是否在站点下存在PFEP属性
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String materialSitePfepExistValidate(Long tenantId, MtMaterialSite dto);

    /**
     * 根据物料Id集合批量查询物料站点
     *
     * @param tenantId 租户Id
     * @param materialIdList 物料Id集合
     * @return list
     * @author benjamin
     * @date 2019-09-03 14:49
     */
    List<MtMaterialSite> queryMaterialSiteByMaterialId(Long tenantId, List<String> materialIdList);

    /**
     * 物料站点分配
     *
     * @param tenantId : 租户Id
     * @param vo : 物料vo
     * @return java.lang.String
     * @Author peng.yuan
     * @Date 2019/9/17 11:03
     */
    String materialSiteAssign(Long tenantId, MtMaterialSiteVO vo);

    /**
     * 自定义根据物料站点id批量查询
     *
     * @Author Xie.yiyang
     * @Date 2019/11/11 14:17
     * @param tenantId, materialSiteIds
     * @return java.util.List<tarzan.material.domain.entity.MtMaterialSite>
     */
    List<MtMaterialSite> queryByMaterialSiteId(Long tenantId, List<String> materialSiteIds);

    /**
     * materialSiteAttrPropertyUpdate-物料站点新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/19 15:02
     * @param tenantId
     * @param dto
     * @return void
     */
    void materialSiteAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * materialSiteLimitRelationBatchGet-物料站点批量获取
     *
     * @author chuang.yang
     * @date 2020/4/7
     * @param tenantId
     * @param materialSiteIds
     * @return java.util.List<tarzan.material.domain.vo.MtMaterialSiteVO4>
     */
    List<MtMaterialSiteVO4> materialSiteLimitRelationBatchGet(Long tenantId, List<MtMaterialSiteVO3> materialSiteIds);
}
