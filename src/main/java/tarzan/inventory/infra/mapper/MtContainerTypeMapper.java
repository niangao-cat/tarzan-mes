package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO4;
import tarzan.inventory.domain.vo.MtContainerTypeVO;
import tarzan.inventory.domain.vo.MtContainerTypeVO1;

/**
 * 容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerTypeMapper extends BaseMapper<MtContainerType> {

    List<MtContainerType> selectPropertyLimitContainerType(@Param("tenantId") Long tenantId,
                                                           @Param("dto") MtContainerType mtContainerType, @Param("fuzzyQueryFlag") String fuzzyQueryFlag);

    List<MtContainerTypeVO1> selectCondition(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "dto") MtContainerTypeVO dto);

    List<MtContainerTypeAttrVO4> selectByContainerId(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "containerIds") List<String> containerIds);

    List<MtContainerType> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "containerTypeIdList") List<String> containerTypeIdList);
}
