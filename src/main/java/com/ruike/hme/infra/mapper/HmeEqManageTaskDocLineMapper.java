package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeEqTaskDocLineQueryDTO;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLine;
import com.ruike.hme.domain.vo.HmeEqTaskHisVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 设备管理任务单行表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:10
 */
public interface HmeEqManageTaskDocLineMapper extends BaseMapper<HmeEqManageTaskDocLine> {

    /**
     * 任务单行查询
     *
     * @param tenantId
     * @param taskDocId
     * @return
     */
    List<HmeEqTaskDocLineQueryDTO> queryTaskDocLineList(@Param("tenantId") Long tenantId,
                                                        @Param("taskDocId") String taskDocId);

    /**
     * 任务行历史查询
     *
     * @param tenantId
     * @param taskDocLineId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEqTaskHisVO>
     * @author sanfeng.zhang@hand-china.com 2021/3/4 22:51
     */
    List<HmeEqTaskHisVO> taskHistoryListQuery(@Param("tenantId") Long tenantId,
                                              @Param("taskDocLineId") String taskDocLineId);

    /**
     * 任务单行表查询
     * @param tenantId
     * @param taskDocIds
     * @return
     */
    List<HmeEqTaskDocLineQueryDTO> queryTaskDocLineListBatchGet(@Param("tenantId") Long tenantId,
                                                                @Param("taskDocIds") List<String> taskDocIds);
}
