package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.domain.entity.MtEventObjectType;
import tarzan.general.domain.vo.MtEventObjectTypeColumnVO;
import tarzan.general.domain.vo.MtEventObjectTypeVO;

/**
 * 对象类型定义Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectTypeMapper extends BaseMapper<MtEventObjectType> {

    List<String> propertyLimitObjectTypeQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEventObjectTypeVO dto);

    List<MtEventObjectType> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "objectTypeIds") List<String> objectTypeIds);

    /**
     * 根据对象类型Id查询对象类型及对象列
     * 
     * @author benjamin
     * @date 2019-08-12 14:13
     * @param tenantId 租户Id
     * @param objectTypeId 对象类型Id
     * @return MtEventObjectTypeColumnVO
     */
    MtEventObjectTypeColumnVO queryEventObjectColumnById(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "objectTypeId") String objectTypeId);
}
