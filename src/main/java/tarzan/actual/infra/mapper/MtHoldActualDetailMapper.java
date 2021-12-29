package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtHoldActualDetail;
import tarzan.actual.domain.vo.MtHoldActualDetailVO;
import tarzan.actual.domain.vo.MtHoldActualDetailVO2;
import tarzan.actual.domain.vo.MtHoldActualDetailVO3;
import tarzan.actual.domain.vo.MtHoldActualDetailVO5;

/**
 * 保留实绩明细Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtHoldActualDetailMapper extends BaseMapper<MtHoldActualDetail> {
    /**
     * checkSavingHoldActrualDetail-根据对象获取正在保留的实绩明细
     * 
     * @Author lxs
     * @Date 2019/3/19
     */
    List<MtHoldActualDetail> checkSavingHoldActrualDetail(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtHoldActualDetailVO2 dto);

    /**
     * holdDetailPropertyGet-获取保留实绩明细属性(根据明细id获取明细和对应头信息)
     * 
     * @Author lxs
     * @Date 2019/3/19
     */
    MtHoldActualDetailVO holdDetailPropertyGet(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtHoldActualDetailVO dto);

    /**
     * propertyLimitHoldDetailQuery -根据对象获取正在保留的实绩明细
     * 
     * @Author lxs
     * @Date 2019/3/19
     */
    List<MtHoldActualDetailVO3> propertyLimitHoldDetailQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtHoldActualDetailVO dto);

    /**
     * holdExpiredReleaseTimeGet -获取保留到期释放时间
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    MtHoldActualDetailVO5 holdExpiredReleaseTimeGet(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtHoldActualDetailVO3 dto);

    /**
     * objectLimitAllHoldQuery -根据对象获取保留实绩明细,不包含HOLD_TYPE”为“FUTURE”类型的数据
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    List<MtHoldActualDetailVO3> objectLimitAllHoldQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtHoldActualDetailVO2 dto);

    /**
     * objectLimitFutureHoldDetailQuery -根据对象获取将来保留明细，筛选头“HOLD_TYPE”为“FUTURE”类型的数据
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @param
     * @Return
     */
    List<MtHoldActualDetailVO3> objectLimitFutureHoldDetailQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtHoldActualDetailVO2 dto);

    /**
     * futureHoldVerify -校验步骤是否为将来保留步骤
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    List<MtHoldActualDetailVO3> futureHoldVerify(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtHoldActualDetail mtHoldActualDetail);

    /**
     * queryHoldActrualDetailBySiteId -根据站点获取明细
     * 
     * @Author lxs
     * @Date 2019/3/20
     * @Return
     */
    List<MtHoldActualDetailVO3> queryHoldActrualDetailBySiteId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "siteId") String siteId);

}
