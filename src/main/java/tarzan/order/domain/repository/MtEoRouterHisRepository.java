package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.order.domain.entity.MtEoRouterHis;
import tarzan.order.domain.vo.MtEoRouterHisVO;

/**
 * EO工艺路线历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoRouterHisRepository extends BaseRepository<MtEoRouterHis>, AopProxy<MtEoRouterHisRepository> {
    /**
     * eoRouterHisQuery-获取指定执行作业工艺路线变更记录
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtEoRouterHis> eoRouterHisQuery(Long tenantId, MtEoRouterHisVO condition);
}
