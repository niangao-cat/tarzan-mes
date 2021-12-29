package tarzan.modeling.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModAreaDistribution;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO2;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO3;

/**
 * 区域配送属性资源库
 *
 * @author yiyang.xie 2020-02-04 11:36:01
 */
public interface MtModAreaDistributionRepository
                extends BaseRepository<MtModAreaDistribution>, AopProxy<MtModAreaDistributionRepository> {

    /**
     * areaDistributionPropertyGet-获取区域配送属性
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtModAreaDistributionVO2 areaDistributionPropertyGet(Long tenantId, MtModAreaDistributionVO dto);

    /**
     * areaDistributionPropertyUpdate-区域配送属性新增&更新
     *
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return
     */
    String areaDistributionPropertyUpdate(Long tenantId, MtModAreaDistributionVO3 dto, String fullUpdate);
}
