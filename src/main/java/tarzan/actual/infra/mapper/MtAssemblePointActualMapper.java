package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtAssemblePointActual;
import tarzan.actual.domain.vo.MtAssemblePointActualVO1;
import tarzan.actual.domain.vo.MtAssemblePointActualVO11;
import tarzan.actual.domain.vo.MtAssemblePointActualVO8;
import tarzan.actual.domain.vo.MtAssemblePointActualVO9;

/**
 * 装配点实绩，记录装配组下装配点实际装配信息Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssemblePointActualMapper extends BaseMapper<MtAssemblePointActual> {

    List<MtAssemblePointActualVO1> selectPropertyByIds(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "assemblePointActualIds") List<String> assemblePointActualIds);

    MtAssemblePointActualVO1 selectPropertyById(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "assemblePointActualId") String assemblePointActualId);

    List<String> selectPropertyByCondition(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "condition") MtAssemblePointActualVO1 condition);

    void deleteByIdsCustom(@Param(value = "tenantId") Long tenantId,
                           @Param(value = "assemblePointActualIds") List<String> assemblePointActualIds);
    /**
     * 根据自定义条件查询
     * @Author peng.yuan
     * @Date 2019/10/10 17:08
     * @param tenantId :
     * @param condition :
     * @return java.util.List<tarzan.actual.domain.vo.MtAssemblePointActualVO9>
     */
    List<MtAssemblePointActualVO9> selectCondition(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "condition") MtAssemblePointActualVO8 condition);

    List<MtAssemblePointActualVO11> selectPropertyByConditions(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "materialIds") List<String> materialIds);
}
