package tarzan.order.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.order.domain.entity.MtEoRouter;

import org.apache.ibatis.annotations.Param;

/**
 * EO工艺路线Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoRouterMapper extends BaseMapper<MtEoRouter> {
    /**
     * select by eoId
     */
    List<MtEoRouter> selectEoRouterByEoId(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "eoIds") List<String> eoIds);
}
