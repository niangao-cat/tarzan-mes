package tarzan.iface.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.vo.MtMaterialBasisVO1;
import tarzan.iface.domain.vo.MtMaterialBasisVO2;

/**
 * 物料业务属性表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:54
 */
public interface MtMaterialBasisMapper extends BaseMapper<MtMaterialBasic> {

    List<MtMaterialBasisVO2> selectCondition(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "dto") MtMaterialBasisVO1 dto);

    void batchInsert(@Param("basic") List<MtMaterialBasic> basic);
}
