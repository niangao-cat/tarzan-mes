package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtDispositionGroup;

/**
 * 处置组资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
public interface MtDispositionGroupRepository
                extends BaseRepository<MtDispositionGroup>, AopProxy<MtDispositionGroupRepository> {

    /**
     * dispositionGroupPropertyGet-获取处置组基本属性
     *
     * @param tenantId
     * @param dispositionGroupId
     * @author guichuan.li
     * @date 2019/4/1
     */
    MtDispositionGroup dispositionGroupPropertyGet(Long tenantId, String dispositionGroupId);

    /**
     * dispositionGroupRouterQuery-根据处置组获取处置工艺路线
     *
     * @param tenantId
     * @param dispositionGroupId
     * @author guichuan.li
     * @date 2019/4/1
     */
    List<String> dispositionGroupRouterQuery(Long tenantId, String dispositionGroupId);
}
