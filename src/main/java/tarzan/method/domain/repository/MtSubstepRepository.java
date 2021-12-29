package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtSubstep;

/**
 * 子步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:23:13
 */
public interface MtSubstepRepository extends BaseRepository<MtSubstep>, AopProxy<MtSubstepRepository> {

    /**
     * substepGet-获取子步骤基础属性
     *
     * @param tenantId
     * @param substepId
     * @return
     */
    MtSubstep substepGet(Long tenantId, String substepId);

    /**
     * substepBatchGet-批量获取子步骤基础属性
     *
     * @param tenantId
     * @param substepIds
     * @return
     */
    List<MtSubstep> substepBatchGet(Long tenantId, List<String> substepIds);
}
