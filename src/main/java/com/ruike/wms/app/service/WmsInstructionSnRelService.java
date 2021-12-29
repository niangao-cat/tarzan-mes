package com.ruike.wms.app.service;


import com.ruike.wms.domain.entity.WmsInstructionSnRel;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 单据SN指定表应用服务
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:07:45
 */
public interface WmsInstructionSnRelService {

    /**
    * 单据SN指定表查询参数
    *
    * @param tenantId 租户ID
    * @param wmsInstructionSnRel 单据SN指定表
    * @param pageRequest 分页
    * @return 单据SN指定表列表
    */
    Page<WmsInstructionSnRel> list(Long tenantId, WmsInstructionSnRel wmsInstructionSnRel, PageRequest pageRequest);

    /**
     * 单据SN指定表详情
     *
     * @param tenantId 租户ID
     * @param wmsInstructionSnRelId 主键
     * @return 单据SN指定表列表
     */
    WmsInstructionSnRel detail(Long tenantId, Long wmsInstructionSnRelId);

    /**
     * 创建单据SN指定表
     *
     * @param tenantId 租户ID
     * @param wmsInstructionSnRel 单据SN指定表
     * @return 单据SN指定表
     */
    WmsInstructionSnRel create(Long tenantId, WmsInstructionSnRel wmsInstructionSnRel);

    /**
     * 更新单据SN指定表
     *
     * @param tenantId 租户ID
     * @param wmsInstructionSnRel 单据SN指定表
     * @return 单据SN指定表
     */
    WmsInstructionSnRel update(Long tenantId, WmsInstructionSnRel wmsInstructionSnRel);

    /**
     * 删除单据SN指定表
     *
     * @param wmsInstructionSnRel 单据SN指定表
     */
    void remove(WmsInstructionSnRel wmsInstructionSnRel);
}
