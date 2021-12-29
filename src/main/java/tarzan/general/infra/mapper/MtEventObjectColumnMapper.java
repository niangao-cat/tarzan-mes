package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.domain.entity.MtEventObjectColumn;
import tarzan.general.domain.vo.MtEventObjectColumnVO;

/**
 * 对象列定义Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectColumnMapper extends BaseMapper<MtEventObjectColumn> {

    List<String> propertyLimitObjectColumnQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEventObjectColumnVO dto);
}
