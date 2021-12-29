package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeOpTagRel;
import com.ruike.hme.domain.vo.HmeOpTagRelVO;
import com.ruike.hme.domain.vo.HmeOpTagRelVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 工艺数据项关系表Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-12-24 15:43:25
 */
public interface HmeOpTagRelMapper extends BaseMapper<HmeOpTagRel> {

    /**
     *
     * @Description 查询工艺下所有的采集项
     *
     * @author yuchao.wang
     * @date 2020/12/24 15:54
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpTagRelVO>
     *
     */
    List<HmeOpTagRelVO> queryTagInfoByOperationId(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);

    /**
     * 工艺数据项关系列表
     *
     * @param tenantId
     * @param operationId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpTagRelVO2>
     * @author sanfeng.zhang@hand-china.com 2020/12/25 14:58
     */
    List<HmeOpTagRelVO2> hmeOpTagRelQuery(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);
}
