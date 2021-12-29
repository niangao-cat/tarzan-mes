package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModEnterprise;
import tarzan.modeling.domain.vo.MtModEnterpriseVO;

/**
 * 企业资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModEnterpriseRepository
                extends BaseRepository<MtModEnterprise>, AopProxy<MtModEnterpriseRepository> {


    /**
     * enterpriseBasicPropertyGet获取企业基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param enterpriseId
     * @return
     */
    MtModEnterprise enterpriseBasicPropertyGet(Long tenantId, String enterpriseId);

    /**
     * propertyLimitEnterpriseQuery根据企业属性获取企业
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitEnterpriseQuery(Long tenantId, MtModEnterpriseVO condition);

    /**
     * enterpriseBasicPropertyUpdate新增更新企业及基础属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     */
    String enterpriseBasicPropertyUpdate(Long tenantId, MtModEnterprise dto);
}
