package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtEoActual;
import tarzan.actual.domain.vo.MtEoActualVO8;

/**
 * 执行作业【执行作业需求和实绩拆分开】Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoActualMapper extends BaseMapper<MtEoActual> {
    /**
     * 根据属性获取执行作业实绩信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtEoActual> propertyLimitEoActualPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "vo") MtEoActualVO8 vo);

    /**
     * 批量获取执行作业实绩
     *
     * @Author Xie.yiyang
     * @Date 2019/11/26 10:14
     * @param tenantId
     * @param eoActualIds
     * @return java.util.List<tarzan.actual.domain.entity.MtEoActual>
     */
    List<MtEoActual> eoActualBatchGetByActualIds(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "eoActualIds") List<String> eoActualIds);

    List<MtEoActual> eoActualBatchGetByEoIds(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "eoIds") List<String> eoIds);
}
