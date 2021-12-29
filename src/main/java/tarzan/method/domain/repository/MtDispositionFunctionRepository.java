package tarzan.method.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtDispositionFunction;

/**
 * 处置方法资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
public interface MtDispositionFunctionRepository
                extends BaseRepository<MtDispositionFunction>, AopProxy<MtDispositionFunctionRepository> {

    /**
     * 获取处置方法基本属性/sen.luo 2018-04-01
     *
     * @param tenantId
     * @param dispositionFunctionId
     * @return
     */
    MtDispositionFunction dispositionFunctionPropertyGet(Long tenantId, String dispositionFunctionId);
}
