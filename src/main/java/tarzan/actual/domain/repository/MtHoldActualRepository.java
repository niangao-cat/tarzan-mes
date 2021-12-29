package tarzan.actual.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtHoldActual;
import tarzan.actual.domain.vo.MtHoldActualVO;
import tarzan.actual.domain.vo.MtHoldActualVO2;

/**
 * 保留实绩资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtHoldActualRepository extends BaseRepository<MtHoldActual>, AopProxy<MtHoldActualRepository> {

    /**
     * holdCreate-创建保留及保留明细
     * 
     * @Author lxs
     * @Date 2019/3/19
     * @param tenantId
     * @param dto
     * @return
     */

    MtHoldActualVO2 holdCreate(Long tenantId, MtHoldActualVO dto);

}
