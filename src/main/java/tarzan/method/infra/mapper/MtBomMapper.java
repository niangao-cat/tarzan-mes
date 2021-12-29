package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.vo.MtBomVO;
import tarzan.method.domain.vo.MtBomVO7;
import tarzan.method.domain.vo.MtBomVO8;
import tarzan.method.domain.vo.MtBomVO9;

/**
 * 装配清单头Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomMapper extends BaseMapper<MtBom> {

    List<MtBom> selectBoms(@Param(value = "tenantId") Long tenantId, @Param(value = "bom") MtBomVO bom);

    List<MtBomVO8> selectBomTL(@Param(value = "tenantId") Long tenantId, @Param(value = "bomId") String bomId);

    List<MtBomVO7> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "bomIds") List<String> bomIds);

    MtBomVO9 selectBomAndCompById(@Param(value = "tenantId") Long tenantId, @Param(value = "bomId") String bomId);

    List<MtBom> selectByBomNameCustom(@Param(value = "tenantId") Long tenantId,
                                      @Param(value = "bomNames") String bomNames);

    List<MtBom> selectByBomIds(@Param(value = "tenantId") Long tenantId,
                               @Param(value = "bomIds") String bomIds);

    String selectSiteByBomId(@Param(value = "bomId") String bomId);
}
