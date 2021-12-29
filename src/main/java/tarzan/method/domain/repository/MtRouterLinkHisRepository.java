package tarzan.method.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterLinkHis;

/**
 * 嵌套工艺路线步骤历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterLinkHisRepository
                extends BaseRepository<MtRouterLinkHis>, AopProxy<MtRouterLinkHisRepository> {

}
