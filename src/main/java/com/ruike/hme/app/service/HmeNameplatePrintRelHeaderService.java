package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeNameplatePrintRelHeaderDTO;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelHeaderAndLineVO;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelHeaderVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 铭牌打印内部识别码对应关系头表应用服务
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:11
 */
public interface HmeNameplatePrintRelHeaderService {

    /**
     * 铭牌打印内部识别码对应关系头表查询参数
     *
     * @param tenantId                      租户id
     * @param hmeNameplatePrintRelHeaderDTO 查询参数
     * @param pageRequest                   分页参数
     * @return
     */
    Page<HmeNameplatePrintRelHeaderVO> queryPrintRelHeader(Long tenantId, HmeNameplatePrintRelHeaderDTO hmeNameplatePrintRelHeaderDTO, PageRequest pageRequest);

    /**
     * 铭牌打印内部识别码历史表表查询
     *
     * @param tenantId          租户id
     * @param nameplateHeaderId 头表id
     * @param pageRequest       分页参数
     * @return
     */
    Page<HmeNameplatePrintRelHeaderAndLineVO> queryPrintRelHeaderAndLine(Long tenantId, String nameplateHeaderId, PageRequest pageRequest);

    /**
     * 创建铭牌打印内部识别码对应关系头表
     *
     * @param tenantId                     租户id
     * @param hmeNameplatePrintRelHeaderVO 保存数据
     */
    void savePrintRelHeader(Long tenantId, HmeNameplatePrintRelHeaderVO hmeNameplatePrintRelHeaderVO);

}
