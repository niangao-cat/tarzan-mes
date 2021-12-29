package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeNcDetailDTO;
import com.ruike.hme.domain.entity.HmeNcDetail;
import com.ruike.hme.domain.entity.HmeNcRecordAttr;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.method.domain.entity.MtNcCode;

import java.util.List;

/**
 * @author Xiong Yi 2020/07/07 18:52
 * @description:
 */
public interface HmeNcDetailMapper {

    /**
     * 功能描述: <br>
     * <工序不良查询>
     *
     * @Param: [tenantId, dto, workCellIdList]
     * @Return: java.util.List<HmeNcDetail>
     * @Author: Xiong Yi
     * @Date: 7.11 17:45
     */

    List<HmeNcDetail> queryHmeNcDetailStation(@Param("tenantId") Long tenantId,
                                              @Param("dto") HmeNcDetailDTO dto,
                                              @Param("workCellIdList") List<String> workCellIdList);

    /**
     * 获取不良代码及描述
     *
     * @param tenantId          租户id
     * @param ncRecordId        不良记录id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 20:15
     * @return tarzan.method.domain.entity.MtNcCode
     */
    MtNcCode queryNcInfo(@Param("tenantId") Long tenantId,@Param("ncRecordId") String ncRecordId);

    /**
     * 获取扩展表信息
     *
     * @param tenantId          租户Id
     * @param ncRecordId        不良记录Id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/24 14:15
     * @return com.ruike.hme.domain.entity.HmeNcRecordAttr
     */
    HmeNcRecordAttr queryProcessMethodByRecordId(@Param("tenantId") Long tenantId,@Param("ncRecordId") String ncRecordId);

    /**
     * 获取子记录信息
     *
     * @param tenantId          租户Id
     * @param ncRecordId        不良记录id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/24 14:15
     * @return java.util.List<tarzan.method.domain.entity.MtNcRecord>
     */
    List<MtNcRecord> querySubCommentsByRecordId(@Param("tenantId") Long tenantId,@Param("ncRecordId") String ncRecordId);

    /**
     * 获取子记录信息
     *
     * @param tenantId          租户Id
     * @param ncRecordList      不良记录id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/24 14:15
     * @return java.util.List<tarzan.method.domain.entity.MtNcRecord>
     */
    List<MtNcRecord> querySubCommentsByRecordIds(@Param("tenantId") Long tenantId,@Param("ncRecordList") List<String> ncRecordList);
}
