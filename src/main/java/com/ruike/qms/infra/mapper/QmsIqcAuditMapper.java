package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsIqcAuditQueryDTO;
import com.ruike.qms.domain.entity.QmsIqcDetails;
import com.ruike.qms.domain.entity.QmsTransitionRule;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryLineVO;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryVO;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryVO2;
import com.ruike.qms.domain.vo.QmsIqcSelectLimitVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: iqc审核mapper层
 * @author: han.zhang
 * @create: 2020/05/19 11:10
 */
public interface QmsIqcAuditMapper {
    /**
     * @Description 查询iqc头信息
     * @param tenantId 租户id
     * @param dto 查询条件
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcAuditQueryVO>
     * @Date 2020-05-19 11:19
     * @Author han.zhang
     */
    List<QmsIqcAuditQueryVO> selectIqcHeader(@Param("tenantId") Long tenantId , @Param("dto") QmsIqcAuditQueryDTO dto,
                                             @Param("userId") Long userId );

    /**
     * @Description 查询iqc行信息
     * @param tenantId 租户Id
     * @param iqcHeaderId 头id
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcAuditQueryVO>
     * @Date 2020-05-19 18:46
     * @Author han.zhang
     */
    List<QmsIqcAuditQueryLineVO> selectIqcLine(@Param("tenantId") Long tenantId , @Param("iqcHeaderId")String iqcHeaderId);

    /**
     * @Description 查询明细信息
     * @param tenantId 租户id
     * @param iqcLineId 行id
     * @return java.util.List<QmsIqcAuditQueryDetailVO>
     * @Date 2020-05-19 19:18
     * @Author han.zhang
     */
    List<QmsIqcDetails> selectIqcDetail(@Param("tenantId") Long tenantId , @Param("iqcLineId")String iqcLineId);
    /**
     * @Description 查询最近N条记录中的不合格数量M
     * @param tenantId
     * @param vo
     * @return java.lang.Long
     * @Date 2020-05-20 11:25
     * @Author han.zhang
     */
    Long selectIqcByLimit(@Param("tenantId") Long tenantId , @Param("vo") QmsIqcSelectLimitVO vo);

    /**
     * 查询站点下的转移规则(物料为空的)
     *
     * @param tenantId          租户id
     * @param siteId            站点id
     * @author sanfeng.zhang@hand-china.com 2020/9/11 21:24
     * @return java.util.List<com.ruike.qms.domain.entity.QmsTransitionRule>
     */
    List<QmsTransitionRule> queryTransitionRule(@Param("tenantId") Long tenantId, @Param("siteId") String siteId);

    /**
     * 根据送货单行ID查询实绩明细下条码的库位ID
     *
     * @param tenantId 租户ID
     * @param instructionId 送货单行ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/22 17:47:06
     * @return java.lang.String
     */
    String getLocatorIdByInstructionId(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);

    /**
     * IQC检验审核导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/29 09:55:54
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcAuditQueryVO2>
     */
    List<QmsIqcAuditQueryVO2> exportIqcHeader(@Param("tenantId") Long tenantId , @Param("dto")QmsIqcAuditQueryDTO dto,
                                              @Param("userId") Long userId );
}