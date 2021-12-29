package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.order.domain.entity.MtEoBomHis;
import tarzan.order.domain.vo.MtEoBomHisVO;

/**
 * EO装配清单历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoBomHisRepository extends BaseRepository<MtEoBomHis>, AopProxy<MtEoBomHisRepository> {
    /**
     * eoBomHisQuery-获取指定执行作业装配清单变更记录
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtEoBomHis> eoBomHisQuery(Long tenantId, MtEoBomHisVO condition);
}
