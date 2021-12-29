package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ItfWcsTaskIfaceDTO1;
import com.ruike.itf.domain.entity.ItfWcsTaskIface;
import io.choerodon.mybatis.common.BaseMapper;

/**
 * 出库任务状态回传接口
 *
 * @author taowen.wang@hand-china.com 2021/7/2 16:31
 */
public interface ItfWcsTaskIfaceMapper extends BaseMapper<ItfWcsTaskIface> {

    int updateByTaskNum(ItfWcsTaskIfaceDTO1 record);

    String selectByTaskNum(String taskNum);

}