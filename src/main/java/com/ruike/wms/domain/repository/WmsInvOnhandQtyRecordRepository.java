package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsInvOnhandJournalDTO;
import com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO2;
import com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO4;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.wms.domain.entity.WmsInvOnhandQtyRecord;
import tarzan.inventory.api.dto.MtInvJournalDTO2;
import tarzan.inventory.api.dto.MtInvJournalDTO4;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 仓库物料每日进销存表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 09:53:55
 */
public interface WmsInvOnhandQtyRecordRepository extends BaseRepository<WmsInvOnhandQtyRecord> , AopProxy<WmsInvOnhandQtyRecordRepository> {

    /**
     * 每日进销存数据同步
     *
     * @param tenantId
     * @param syncType
     * @author jiangling.zheng@hand-china.com 2020/11/18 10:06
     * @return void
     */
    void invOnhandQtyRecordSync(Long tenantId, String syncType);

    /**
     * 批量创建每日进销存
     *
     * @param recordList
     * @author jiangling.zheng@hand-china.com 2020/11/18 19:58
     * @return void
     */
    void batchInsertInvOnhandQtyRecord(List<WmsInvOnhandQtyRecord> recordList);

    /**
     * 获取进销存数据
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/20 12:43
     * @return java.util.List<com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO2>
     */
    List<WmsInvOnhandQtyRecordDTO2> invOnhandQtyQuery(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto);

    /**
     * 获取进销存数据
     *
     * @param tenantId
     * @param dto
     * @param response
     * @author jiangling.zheng@hand-china.com 2020/11/20 12:43
     * @return java.util.List<com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO2>
     */
    void invOnhandQtyExport(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto, HttpServletResponse response);

    /**
     * 库存日记报表
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author jiangling.zheng@hand-china.com 2020/11/21 15:56
     * @return java.util.List<tarzan.inventory.api.dto.MtInvJournalDTO2>
     */
    Page<MtInvJournalDTO2> queryInvJournalReport(Long tenantId, WmsInvOnhandJournalDTO dto, PageRequest pageRequest);
}
