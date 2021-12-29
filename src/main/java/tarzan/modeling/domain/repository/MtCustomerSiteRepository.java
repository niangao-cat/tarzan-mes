package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtCustomerSite;

/**
 * 客户地点资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
public interface MtCustomerSiteRepository extends BaseRepository<MtCustomerSite>, AopProxy<MtCustomerSiteRepository> {

    /**
     * 根据客户Id批量查询客户地点
     *
     * @author benjamin
     * @date 2019-07-24 09:30
     * @param customerIdList 客户Id集合
     * @return List
     */
    List<MtCustomerSite> queryCustomerSiteByCustomerId(Long tenantId,List<String> customerIdList);
    
}
