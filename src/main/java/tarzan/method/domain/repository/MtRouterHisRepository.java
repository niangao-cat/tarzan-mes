package tarzan.method.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterHis;

/**
 * 工艺路线历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterHisRepository extends BaseRepository<MtRouterHis>, AopProxy<MtRouterHisRepository> {

    /**
     * routerHisCreate-创建工艺路线历史
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/25
     */
    void routerHisCreate(Long tenantId, MtRouterHis dto);
}
