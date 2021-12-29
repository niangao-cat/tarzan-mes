package com.ruike.qms.domain.repository;

import com.ruike.qms.api.dto.QmsIqcAuditDTO;
import com.ruike.qms.api.dto.QmsIqcAuditQueryDTO;
import com.ruike.qms.domain.entity.QmsIqcDetails;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryLineVO;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryVO;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: iqc审核repo层
 * @author: han.zhang
 * @create: 2020/05/19 11:41
 */
public interface QmsIqcAuditRepository {
    /**
     * @Description 查询iqc头信息
     * @param tenantId 租户id
     * @param dto 查询条件
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcAuditQueryVO>
     * @Date 2020-05-19 11:19
     * @Author han.zhang
     */
    Page<QmsIqcAuditQueryVO> selectIqcHeader(@Param("tenantId") Long tenantId , @Param("dto") QmsIqcAuditQueryDTO dto, PageRequest pageRequest);

    /**
     * @Description 查询iqc行信息
     * @param tenantId 租户Id
     * @param iqcHeaderId 头id
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcAuditQueryVO>
     * @Date 2020-05-19 18:46
     * @Author han.zhang
     */
    Page<QmsIqcAuditQueryLineVO> selectIqcLine(Long tenantId , String iqcHeaderId, PageRequest pageRequest);

    /**
     * @Description 查询明细信息
     * @param tenantId 租户id
     * @param iqcLineId 行id
     * @return java.util.List<QmsIqcAuditQueryDetailVO>
     * @Date 2020-05-19 19:18
     * @Author han.zhang
     */
    Page<QmsIqcDetails> selectIqcDetail(@Param("tenantId") Long tenantId , @Param("iqcLineId")String iqcLineId, PageRequest pageRequest);

    /**
     * @Description IQC审核 让步、挑选、退货
     * @param tenantId
     * @param giveInDTO
     * @return void
     * @Date 2020-05-20 14:07
     * @Author han.zhang
     */
    void audit(Long tenantId, QmsIqcAuditDTO giveInDTO);

    /**
     * 送货下条码处理
     * 
     * @param tenantId          租户id
     * @param qmsIqcHeader      质检单头
     * @param eventId           事件id
     * @param qualityStatus     物料批状态
     * @param enableFlag        标识
     * @param attrValueStr      扩展字段
     * @author sanfeng.zhang@hand-china.com 2020/8/7 10:56 
     * @return void
     */
    void handleMaterialLot(Long tenantId, QmsIqcHeader qmsIqcHeader, String eventId, String qualityStatus, String enableFlag, String attrValueStr);

    /**
     * IQC检验审核导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/29 09:54:11
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcAuditQueryVO2>
     */
    List<QmsIqcAuditQueryVO2> exportIqcHeader(Long tenantId, QmsIqcAuditQueryDTO dto);
}