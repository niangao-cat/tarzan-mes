package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeWoDispatchRecode;
import com.ruike.hme.domain.vo.HmeWoDispatchWkcVO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 工单派工记录表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
public interface HmeWoDispatchRecodeRepository extends BaseRepository<HmeWoDispatchRecode>, AopProxy<HmeWoDispatchRecodeRepository> {
    /**
     * 查询派工明细
     *
     * @param tenantId        租户
     * @param prodLineId      产线
     * @param topSiteId       顶层站点
     * @param userId          用户ID
     * @param workOrderIdList 工单列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoDispatchDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/2 01:52:28
     */
    List<HmeWoDispatchWkcVO> selectDispatchDetailList(Long tenantId,
                                                      String topSiteId,
                                                      String prodLineId,
                                                      Long userId,
                                                      List<String> workOrderIdList);
}
