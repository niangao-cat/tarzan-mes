package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.vo.MtBomComponentVO;
import tarzan.method.domain.vo.MtBomComponentVO13;
import tarzan.method.domain.vo.MtBomComponentVO17;

/**
 * 装配清单行Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomComponentMapper extends BaseMapper<MtBomComponent> {

    List<MtBomComponent> selectBomComponents(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomComponent") MtBomComponentVO bomComponent);

    List<MtBomComponent> selectByBomCompCondition(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "siteId") String siteId, @Param(value = "materialId") String materialId,
                    @Param(value = "bomComponentType") String bomComponentType);

    List<MtBomComponent> selectEnableByBomId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomId") String bomId);

    List<MtBomComponentVO13> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomComponentIds") List<String> bomComponentIds);

    List<MtBomComponent> selectByBomIdAndComponentIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomId") String bomId,
                    @Param(value = "bomComponentIds") List<String> bomComponentIds);

    List<MtBomComponent> selectBomComponentByBomIds(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "bomIds") String bomIds);

    List<MtBomComponent> selectBomComponentByBomIdsAll(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "bomIds") List<String> bomIds);

    List<MtBomComponentVO17> bomComponentLimitOperationBatchGet(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "bomComponentIds") List<String> bomComponentIds);
}
