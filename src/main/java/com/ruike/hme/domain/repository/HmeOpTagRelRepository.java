package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeOpTagRelVO;
import com.ruike.hme.domain.vo.HmeOpTagRelVO2;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeOpTagRel;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeOpTagRel;

import java.util.*;

/**
 * 工艺数据项关系表资源库
 *
 * @author yuchao.wang@hand-china.com 2020-12-24 15:43:25
 */
public interface HmeOpTagRelRepository extends BaseRepository<HmeOpTagRel>,AopProxy<HmeOpTagRelRepository> {

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
    List<HmeOpTagRelVO> queryTagInfoByOperationId(Long tenantId, String operationId);

    /**
     * 工艺下数据项列表
     *
     * @param tenantId
     * @param operationId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeOpTagRelVO2>
     * @author sanfeng.zhang@hand-china.com 2020/12/25 14:49
     */
    Page<HmeOpTagRelVO2> hmeOpTagRelQuery(Long tenantId, String operationId, PageRequest pageRequest);

    /**
     * 保存工艺下数据项关系
     *
     * @param tenantId
     * @param hmeOpTagRelList
     * @return java.util.List<com.ruike.hme.domain.entity.HmeOpTagRel>
     * @author sanfeng.zhang@hand-china.com 2020/12/25 14:54
     */
    List<HmeOpTagRel> saveOpTagRel(Long tenantId, List<HmeOpTagRel> hmeOpTagRelList);
}
