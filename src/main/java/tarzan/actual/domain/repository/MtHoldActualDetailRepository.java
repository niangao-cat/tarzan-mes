package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtHoldActualDetail;
import tarzan.actual.domain.vo.*;

/**
 * 保留实绩明细资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtHoldActualDetailRepository
                extends BaseRepository<MtHoldActualDetail>, AopProxy<MtHoldActualDetailRepository> {

    /**
     * holdDetailPropertyGet-获取保留实绩明细属性(根据明细id获取明细和对应头信息)
     * 
     * @Author lxs
     * @Date 2019/3/19
     */
    MtHoldActualDetailVO holdDetailPropertyGet(Long tenantId, MtHoldActualDetailVO dto);

    /**
     * objectLimitHoldingDetailGet -根据对象获取正在保留的实绩明细
     * 
     * @Author lxs
     * @Date 2019/3/19
     */
    String objectLimitHoldingDetailGet(Long tenantId, MtHoldActualDetailVO2 dto);

    /**
     * objectLimitHoldingDetailGet -根据属性获取保留实绩
     * 
     * @Author lxs
     * @Date 2019/3/19
     */
    List<MtHoldActualDetailVO3> propertyLimitHoldDetailQuery(Long tenantId, MtHoldActualDetailVO dto);

    /**
     * holdRelease -释放保留明细
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    void holdRelease(Long tenantId, MtHoldActualDetailVO4 dto);

    /**
     * holdIsExpiredVerify-校验保留是否到期
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    void holdIsExpiredVerify(Long tenantId, MtHoldActualDetailVO3 dto);

    /**
     * holdExpiredReleaseTimeGet -获取保留到期释放时间
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    MtHoldActualDetailVO5 holdExpiredReleaseTimeGet(Long tenantId, MtHoldActualDetailVO3 dto);

    /**
     * objectLimitAllHoldQuery-根据对象获取保留实绩明细,包含“HOLD_TYPE”为“FUTURE”类型的数据
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return List<String>
     */
    List<MtHoldActualDetailVO3> objectLimitAllHoldQuery(Long tenantId, MtHoldActualDetailVO2 dto);

    /**
     * objectLimitFutureHoldDetailQuery-根据对象获取将来保留明细，筛选头“HOLD_TYPE”为“FUTURE”类型的数据
     *
     * @Author lxs
     * @Date 2019/3/20
     * @param tenantId
     * @param dto
     * @Return List<String>
     */
    List<MtHoldActualDetailVO3> objectLimitFutureHoldDetailQuery(Long tenantId, MtHoldActualDetailVO2 dto);

    /**
     * futureHoldVerify -校验步骤是否为将来保留步骤
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    void futureHoldVerify(Long tenantId, MtHoldActualDetailVO6 dto);

    /**
     * expiredReleaseTimeLimitHoldRelease -根据保留到期释放时间释放保留
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    List<MtHoldActualDetailVO7> expiredReleaseTimeLimitHoldRelease(Long tenantId, MtHoldActualDetailVO8 dto);


    /**
     * eoStepFutureHold -执行作业步骤将来保留
     * 
     * @Author lxs
     * @Date 2019/3/26
     * @Return
     */
    void eoStepFutureHold(Long tenantId, MtHoldActualDetailVO11 dto);

    /**
     * eoStepFutureHoldCancel -执行作业步骤将来取消
     * 
     * @Author lxs
     * @Date 2019/3/26
     * @Return
     */
    void eoStepFutureHoldCancel(Long tenantId, MtHoldActualDetailVO9 dto);

    /**
     * eoStepHold -执行作业步骤保留
     * 
     * @Author lxs
     * @Date 2019/3/26
     * @Return
     */
    void eoStepHold(Long tenantId, MtHoldActualDetailVO10 dto);

    /**
     * eoStepHold -执行作业步骤取消
     * 
     * @Author lxs
     * @Date 2019/3/26
     * @Return
     */
    void eoStepHoldCancel(Long tenantId, MtHoldActualDetailVO10 dto);


}
