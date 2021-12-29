package tarzan.order.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.order.domain.entity.MtEoBom;

import org.apache.ibatis.annotations.Param;

/**
 * EO装配清单Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoBomMapper extends BaseMapper<MtEoBom> {
    List<MtEoBom> eoBomBatchGet(@Param(value = "tenantId") Long tenantId, @Param(value = "eoIds") List<String> eoIds);
}
