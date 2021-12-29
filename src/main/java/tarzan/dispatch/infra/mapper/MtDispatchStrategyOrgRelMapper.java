package tarzan.dispatch.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;
import tarzan.dispatch.domain.entity.MtDispatchStrategyOrgRel;
import tarzan.dispatch.domain.vo.MtDispatchStrategyOrgRelVO2;
import tarzan.dispatch.domain.vo.MtDispatchStrategyOrgRelVO3;
import tarzan.dispatch.domain.vo.MtDispatchStrategyOrgRelVO4;
import tarzan.dispatch.domain.vo.MtDispatchStrategyOrgRelVO5;
import java.util.List;

/**
 * 调度策略与组织关系表Mapper
 *
 * @author yiyang.xie 2020-02-03 19:42:38
 */
public interface MtDispatchStrategyOrgRelMapper extends BaseMapper<MtDispatchStrategyOrgRel> {

    /**
     * 根据组织id和类型查找数据
     * 
     * @Author peng.yuan
     * @Date 2020/2/4 9:44
     * @param tenantId :
     * @param vo :
     * @return java.util.List<tarzan.dispatch.domain.vo.MtDispatchStrategyOrgRelVO3>
     */
    List<MtDispatchStrategyOrgRelVO3> selectByIdAndType(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "dto") MtDispatchStrategyOrgRelVO2 vo);

    /**
     * 根据自定义条件查询列表
     * 
     * @Author peng.yuan
     * @Date 2020/2/4 10:22
     * @param tenantId :
     * @param vo :
     * @return java.util.List<tarzan.dispatch.domain.vo.MtDispatchStrategyOrgRelVO5>
     */
    List<MtDispatchStrategyOrgRelVO5> selectByMyCondition(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "dto") MtDispatchStrategyOrgRelVO4 vo);
}
