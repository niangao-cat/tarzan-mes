package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.domain.entity.MtContainerLoadDetailHis;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO2;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO3;

/**
 * 容器装载明细历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerLoadDetailHisMapper extends BaseMapper<MtContainerLoadDetailHis> {


    /**
     * 根据事件id获取数据
     * 
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtContainerLoadDetailHis> containerLoadDetailBatchQueryByEventId(@Param("tenantId") Long tenantId,
                                                                          @Param("eventIds") List<String> eventIds);

    List<MtContLoadDtlHisVO3> containerLoadDetailHisQuery(@Param("tenantId") Long tenantId,
                                                          @Param("dto") MtContLoadDtlHisVO2 dto);
}
