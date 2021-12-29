package tarzan.method.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtOperationSubstep;

/**
 * 工艺子步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
public interface MtOperationSubstepRepository
                extends BaseRepository<MtOperationSubstep>, AopProxy<MtOperationSubstepRepository> {

}
