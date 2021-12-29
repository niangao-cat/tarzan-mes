package tarzan.method.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterOperationHis;

/**
 * 工艺路线步骤对应工序历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterOperationHisRepository
                extends BaseRepository<MtRouterOperationHis>, AopProxy<MtRouterOperationHisRepository> {

}
