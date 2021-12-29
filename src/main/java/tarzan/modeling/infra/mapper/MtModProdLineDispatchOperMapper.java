package tarzan.modeling.infra.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModProdLineDispatchOper;

/**
 * 生产线调度指定工艺Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModProdLineDispatchOperMapper extends BaseMapper<MtModProdLineDispatchOper> {

    List<MtModProdLineDispatchOper> prodLineIdLimitDispatchOperationQueryForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "prodLineId") String prodLineId);
}
