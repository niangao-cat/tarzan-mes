package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeNameplatePrintRelLine;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 铭牌打印内部识别码对应关系行表应用服务
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:13
 */
public interface HmeNameplatePrintRelLineService {

    /**
     * 铭牌打印内部识别码对应关系行表查询参数
     *
     * @param tenantId          租户ID
     * @param nameplateHeaderId 头表id
     * @param pageRequest       分页
     * @return 铭牌打印内部识别码对应关系行表列表
     */
    Page<HmeNameplatePrintRelLineVO> queryPrintRelLine(Long tenantId, String nameplateHeaderId, PageRequest pageRequest);

    /**
     * 创建铭牌打印内部识别码对应关系行表
     *
     * @param tenantId                 租户ID
     * @param hmeNameplatePrintRelLineVO 铭牌打印内部识别码对应关系行表
     * @return 铭牌打印内部识别码对应关系行表
     */
    void savePrintRelLine(Long tenantId, HmeNameplatePrintRelLineVO hmeNameplatePrintRelLineVO);

}
