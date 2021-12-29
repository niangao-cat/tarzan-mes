package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModAreaPurchase;

/**
 * 区域采购属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModAreaPurchaseMapper extends BaseMapper<MtModAreaPurchase> {

    List<MtModAreaPurchase> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "areaIds") List<String> areaIds);
}
