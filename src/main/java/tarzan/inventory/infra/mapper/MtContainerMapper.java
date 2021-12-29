package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.vo.MtContainerVO1;
import tarzan.inventory.domain.vo.MtContainerVO13;
import tarzan.inventory.domain.vo.MtContainerVO27;
import tarzan.inventory.domain.vo.MtContainerVO28;

/**
 * 容器，一个具体的容器并记录容器的业务属性，包括容器装载实物所有者、预留对象、位置状态等，提供执行作业、物料批、容器的装载结构Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerMapper extends BaseMapper<MtContainer> {

    List<MtContainer> selectLocatorLimitQuery(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtContainerVO1 dto);

    List<MtContainer> selectByContainerIds(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "containerIds") List<String> containerIds);

    List<String> propertyLimitQuery(@Param(value = "tenantId") Long tenantId,
                                    @Param(value = "dto") MtContainerVO13 dto);

    String containerNextCodeGet(@Param(value = "tenantId") Long tenantId, @Param(value = "siteCode") String siteCode);

    List<MtContainerVO28> selectCondition(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "dto") MtContainerVO27 dto);

    List<MtContainer> selectByContainerCodes(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "containerCodes") List<String> containerCodes);


    List<MtContainer> selectByTopContainerIds(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "topContainerIds") List<String> topContainerIds);


}
