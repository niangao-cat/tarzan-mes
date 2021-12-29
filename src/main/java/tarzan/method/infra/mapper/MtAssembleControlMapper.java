package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtAssembleControl;
import tarzan.method.domain.vo.MtAssembleControlVO2;

/**
 * 装配控制，定义一组装配控制要求，包括装配组可安装位置和装配点可装载物料Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssembleControlMapper extends BaseMapper<MtAssembleControl> {

    MtAssembleControl selectByEnable(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "assembleControlId") String assembleControlId);

    MtAssembleControl mySelectOne(@Param(value = "tenantId") Long tenantId,
                                  @Param(value = "dto") MtAssembleControl dto);

    /**
     * 根据自定义条件查询
     *
     * @Author peng.yuan
     * @Date 2019/10/12 14:52
     * @param tenantId :
     * @param condition :
     * @return java.util.List<tarzan.method.domain.vo.MtAssembleControlVO2>
     */
    List<MtAssembleControlVO2> selectCondition(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "dto") MtAssembleControlVO2 condition);
}
