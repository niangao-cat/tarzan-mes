package com.ruike.qms.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.qms.domain.entity.QmsIqcHeader;

/**
 * 质检单头表 资源库
 *
 * @author tong.li05@hand-china.com 2020-04-28 19:39:00
 */
public interface QmsIqcHeaderRepository extends BaseRepository<QmsIqcHeader> {
    /**
     * @param tenantId 租户ID
     * @param iqcHeader 质检单头
     * @return : com.ruike.qms.domain.entity.QmsIqcHeader
     * @Description: 质检单生成
     * @author: tong.li
     * @date 2020/4/29 14:44
     * @version 1.0
     */
    void createIqcBill(Long tenantId,QmsIqcHeader iqcHeader);
}
