package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModWorkcellManufacturing;

/**
 * 工作单元生产属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModWorkcellManufacturingMapper extends BaseMapper<MtModWorkcellManufacturing> {

    List<MtModWorkcellManufacturing> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "ids") List<String> ids);
}
