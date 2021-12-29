package com.ruike.wms.app.service;

import com.ruike.wms.domain.vo.WmsInvJournalExportVO;
import com.ruike.wms.domain.vo.WmsInvOnhandQuantityVO;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import tarzan.inventory.api.dto.MtInvJournalDTO4;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10;

import java.util.List;

/**
 * 库存量 管理 服务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/14 10:49
 */
public interface WmsInvOnhandQuantityService {

    /**
     * 现有量查询
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页条件
     * @return tarzan.inventory.domain.vo.WmsInvOnhandQuantityVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/14 10:54:17
     */
    WmsInvOnhandQuantityVO onhandQuantityQuery(Long tenantId, MtInvOnhandQuantityVO10 dto, PageRequest pageRequest);

    /**
     * 现有量导出
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param exportParam 导出参数
     * @return java.util.List<tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/14 10:54:17
     */
    List<MtInvOnhandQuantityVO10> export(Long tenantId, MtInvOnhandQuantityVO10 dto, ExportParam exportParam);

    /**
     * 库存日记账导出
     * 
     * @param tenantId          租户
     * @param dto               参数
     * @author sanfeng.zhang@hand-china.com 2020/10/14 15:44 
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInvJournalExportVO>
     */
    List<WmsInvJournalExportVO> invJournalExcelExport(Long tenantId, MtInvJournalDTO4 dto);
}
