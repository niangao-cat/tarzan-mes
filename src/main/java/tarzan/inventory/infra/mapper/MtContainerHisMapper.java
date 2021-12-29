package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.domain.entity.MtContainerHis;
import tarzan.inventory.domain.vo.MtContainerHisVO1;
import tarzan.inventory.domain.vo.MtContainerHisVO3;

/**
 * 容器历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerHisMapper extends BaseMapper<MtContainerHis> {

    List<MtContainerHis> selectMtContainerHisByEventIds(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "eventIds") List<String> eventIds);

    List<MtContainerHis> selectMtContainerHisByEventTime(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "dto") MtContainerHisVO1 dto);

    MtContainerHisVO3 containerLatestHisGet(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "containerId") String containerId);
}
