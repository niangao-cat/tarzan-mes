package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeNameplatePrintRelLineHis;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 铭牌打印内部识别码对应关系行历史表应用服务
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:14
 */
public interface HmeNameplatePrintRelLineHisService {

    /**
    * 铭牌打印内部识别码对应关系行历史表查询参数
    *
    * @param tenantId 租户ID
    * @param hmeNameplatePrintRelLineHis 铭牌打印内部识别码对应关系行历史表
    * @param pageRequest 分页
    * @return 铭牌打印内部识别码对应关系行历史表列表
    */
    Page<HmeNameplatePrintRelLineHis> list(Long tenantId, HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis, PageRequest pageRequest);

    /**
     * 铭牌打印内部识别码对应关系行历史表详情
     *
     * @param tenantId 租户ID
     * @param nameplateLineHisId 主键
     * @return 铭牌打印内部识别码对应关系行历史表列表
     */
    HmeNameplatePrintRelLineHis detail(Long tenantId, Long nameplateLineHisId);

    /**
     * 创建铭牌打印内部识别码对应关系行历史表
     *
     * @param tenantId 租户ID
     * @param hmeNameplatePrintRelLineHis 铭牌打印内部识别码对应关系行历史表
     * @return 铭牌打印内部识别码对应关系行历史表
     */
    HmeNameplatePrintRelLineHis create(Long tenantId, HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis);

    /**
     * 更新铭牌打印内部识别码对应关系行历史表
     *
     * @param tenantId 租户ID
     * @param hmeNameplatePrintRelLineHis 铭牌打印内部识别码对应关系行历史表
     * @return 铭牌打印内部识别码对应关系行历史表
     */
    HmeNameplatePrintRelLineHis update(Long tenantId, HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis);

    /**
     * 删除铭牌打印内部识别码对应关系行历史表
     *
     * @param hmeNameplatePrintRelLineHis 铭牌打印内部识别码对应关系行历史表
     */
    void remove(HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis);
}
