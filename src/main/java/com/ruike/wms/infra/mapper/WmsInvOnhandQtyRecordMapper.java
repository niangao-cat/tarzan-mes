package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.entity.WmsInvOnhandQtyRecord;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.api.dto.MtInvJournalDTO2;
import tarzan.inventory.api.dto.MtInvJournalDTO4;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 仓库物料每日进销存表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 09:53:55
 */
public interface WmsInvOnhandQtyRecordMapper extends BaseMapper<WmsInvOnhandQtyRecord> {


    /**
     * 获取JOB最后一次运行日期
     *
     * @param tenantId
     * @param jobCode
     * @author jiangling.zheng@hand-china.com 2020/11/18 14:59
     * @return java.util.Date
     */
    LocalDate jobLastRunDateGet(@Param(value = "tenantId") Long tenantId,
                                @Param(value = "jobCode") String jobCode);

    /**
     * 获取JOB最后一次运行成功日期
     *
     * @param tenantId
     * @param jobCode
     * @author jiangling.zheng@hand-china.com 2020/11/19 10:33
     * @return java.time.LocalDate
     */
    LocalDate jobLastSyncDateGet(@Param(value = "tenantId") Long tenantId,
                                 @Param(value = "jobCode") String jobCode);

    /**
     * 获取库存日记账记录
     *
     * @param tenantId
     * @param startDate
     * @author jiangling.zheng@hand-china.com 2020/11/18 12:40
     * @return java.util.List<com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO>
     */
    List<WmsInvOnhandQtyRecordDTO> invJournalQuery(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "startDate") LocalDate startDate);

    /**
     * 获取进销存数据
     *
     * @param tenantId
     * @param startDate
     * @author jiangling.zheng@hand-china.com 2020/11/18 16:26
     * @return java.util.List<com.ruike.wms.domain.entity.WmsInvOnhandQtyRecord>
     */
    List<WmsInvOnhandQtyRecord> invOnhandQtyRecordQuery(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "startDate") LocalDate startDate);

    /**
     * 获取现有量数据
     *
     * @param tenantId
     * @author jiangling.zheng@hand-china.com 2020/11/18 20:08
     * @return java.util.List<com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO>
     */
    List<WmsInvOnhandQtyRecordDTO> invOnhandQuantityQuery(@Param(value = "tenantId") Long tenantId);

    /**
     * 批量插入
     *
     * @param invOnhandQtyRecordList
     * @author jiangling.zheng@hand-china.com 2020/11/18 16:37
     * @return void
     */
    void batchInsertInvOnhandQtyRecord(@Param(value = "invOnhandQtyRecordList") List<WmsInvOnhandQtyRecord> invOnhandQtyRecordList);

    /**
     * 批量删除当天数据
     *
     * @param tenantId
     * @param showDate
     * @author jiangling.zheng@hand-china.com 2020/11/18 22:29
     * @return void
     */

    void batchDeleteList(@Param(value = "tenantId") Long tenantId,
                         @Param(value = "showDate") Date showDate);

    /**
     * 获取进销存基础数据
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/20 11:44
     * @return java.util.List<com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO2>
     */

    List<WmsInvOnhandQtyRecordDTO2> invOnhandQtyGroupQuery(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "dto") WmsInvOnhandQtyRecordDTO4 dto);

    /**
     * 获取每日进销存日期
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/20 15:31
     * @return java.util.List<java.util.Date>
     */
    List<String> invOnhandQtyDateQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsInvOnhandQtyRecordDTO4 dto);

    /**
     * 获取每日进销存数量
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/20 11:44
     * @return java.util.List<com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO3>
     */
    List<WmsInvOnhandQtyRecordDTO3> invOnhandQtyQuery(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "dto") WmsInvOnhandQtyRecordDTO4 dto);

    /**
     * 库存日记查询
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/21 15:59
     * @return java.util.List<tarzan.inventory.api.dto.MtInvJournalDTO2>
     */
    List<MtInvJournalDTO2> queryInvJournalReport(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "dto") WmsInvOnhandJournalDTO dto);

}
