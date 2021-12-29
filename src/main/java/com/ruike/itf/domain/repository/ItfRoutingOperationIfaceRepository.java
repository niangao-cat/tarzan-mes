package com.ruike.itf.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfRoutingOperationIface;

import java.util.List;

/**
 * 工艺路线接口表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfRoutingOperationIfaceRepository extends BaseRepository<ItfRoutingOperationIface> {

    /**
     * 查询未处理的批次
     *
     * @param tenantId
     * @author sanfeng.zhang@hand-china.com 2021/11/23 14:49
     * @return java.util.List<java.lang.Long>
     */
    List<Long> selectBatchId(Long tenantId);

    /**
     * 工艺路线同步
     *
     * @param tenantId
     * @param batchId
     * @author sanfeng.zhang@hand-china.com 2021/11/23 15:13
     * @return void
     */
    void myRoutingOperationImport(Long tenantId, Long batchId);
}
