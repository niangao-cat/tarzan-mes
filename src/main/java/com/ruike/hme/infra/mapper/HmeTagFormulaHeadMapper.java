package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeTagFormulaHead;
import com.ruike.hme.domain.vo.HmeSnBindEoVO2;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.domain.vo.HmeTagFormulaLineVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 数据采集项公式头表Mapper
 *
 * @author guiming.zhou@hand-china.com 2020-09-21 19:50:40
 */
public interface HmeTagFormulaHeadMapper extends BaseMapper<HmeTagFormulaHead> {
    /**
     * 获取查询公式头
     *
     * @param tenantId
     * @param operationId
     * @param tagGroupId
     * @param tagId
     * @author guiming.zhou@hand-china.com 2020/9/25 13:58
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeTagFormulaHeadVO>
     */
    List<HmeTagFormulaHeadVO> getTagHeadList(@Param("tenantId") Long tenantId, @Param("operationId") String operationId,
                                             @Param("tagGroupId") String tagGroupId, @Param("tagId") String tagId);

    /**
     *
     * @Description 工序作业平台查询头数据
     *
     * @author yuchao.wang
     * @date 2020/9/24 10:52
     * @param headQuery 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeTagFormulaHead>
     *
     */
    List<HmeTagFormulaHead> selectHeadForCalculation(HmeTagFormulaHead headQuery);

    /**
     * 查询公式头信息
     *
     * @param tenantId
     * @param operationId
     * @param tagGroupId
     * @param tagId
     * @author sanfeng.zhang@hand-china.com 2020/9/27 17:00
     * @return java.util.List<com.ruike.hme.domain.entity.HmeTagFormulaHead>
     */
    List<HmeTagFormulaHead> queryHmeTagFormulaHead(@Param("tenantId") Long tenantId, @Param("operationId") String operationId,
                                                   @Param("tagGroupId") String tagGroupId, @Param("tagId") String tagId);

    /**
     * 查询公式行信息
     *
     * @param tenantId
     * @param headTagList
     * @param tagGroupId
     * @author sanfeng.zhang@hand-china.com 2021/9/9 20:30
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagFormulaLineVO2>
     */
    List<HmeTagFormulaLineVO2> queryTagFormulaLineList(@Param("tenantId") Long tenantId, @Param("headTagList") Set<String> headTagList, @Param("tagGroupId") String tagGroupId);
}
