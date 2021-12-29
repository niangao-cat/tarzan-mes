package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeNameplatePrintRelHeaderHis;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 铭牌打印内部识别码对应关系头历史表应用服务
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:12
 */
public interface HmeNameplatePrintRelHeaderHisService {

    /**
    * 铭牌打印内部识别码对应关系头历史表查询参数
    *
    * @param tenantId 租户ID
    * @param hmeNameplatePrintRelHeaderHis 铭牌打印内部识别码对应关系头历史表
    * @param pageRequest 分页
    * @return 铭牌打印内部识别码对应关系头历史表列表
    */
    Page<HmeNameplatePrintRelHeaderHis> list(Long tenantId, HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis, PageRequest pageRequest);

    /**
     * 铭牌打印内部识别码对应关系头历史表详情
     *
     * @param tenantId 租户ID
     * @param nameplateHeaderHisId 主键
     * @return 铭牌打印内部识别码对应关系头历史表列表
     */
    HmeNameplatePrintRelHeaderHis detail(Long tenantId, Long nameplateHeaderHisId);

    /**
     * 创建铭牌打印内部识别码对应关系头历史表
     *
     * @param tenantId 租户ID
     * @param hmeNameplatePrintRelHeaderHis 铭牌打印内部识别码对应关系头历史表
     * @return 铭牌打印内部识别码对应关系头历史表
     */
    HmeNameplatePrintRelHeaderHis create(Long tenantId, HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis);

    /**
     * 更新铭牌打印内部识别码对应关系头历史表
     *
     * @param tenantId 租户ID
     * @param hmeNameplatePrintRelHeaderHis 铭牌打印内部识别码对应关系头历史表
     * @return 铭牌打印内部识别码对应关系头历史表
     */
    HmeNameplatePrintRelHeaderHis update(Long tenantId, HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis);

    /**
     * 删除铭牌打印内部识别码对应关系头历史表
     *
     * @param hmeNameplatePrintRelHeaderHis 铭牌打印内部识别码对应关系头历史表
     */
    void remove(HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis);
}
