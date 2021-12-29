package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeWoInputRecord;

import java.util.List;

/**
 * 工单投料记录表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 17:41:58
 */
public interface HmeWoInputRecordRepository extends BaseRepository<HmeWoInputRecord>, AopProxy<HmeWoInputRecordRepository> {

    /**
     * 查询工单信息
     *
     * @param tenantId
     * @param workOrderNum
     * @author jiangling.zheng@hand-china.com 2020/10/27 20:43
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO
     */
    HmeWoInputRecordDTO workOrderGet(Long tenantId, String workOrderNum);

    /**
     * 获取装配清单信息
     *
     * @param tenantId
     * @param workOrderId
     * @author jiangling.zheng@hand-china.com 2020/10/29 17:24
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoInputRecordDTO4>
     */
    List<HmeWoInputRecordDTO4> woBomCompInfoQuery(Long tenantId, String workOrderId);

    /**
     * 获取投料信息
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 11:32
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoInputRecordDTO2>
     */
    List<HmeWoInputRecordDTO2> woInputRecordQuery(Long tenantId, HmeWoInputRecordDTO3 dto);

    /**
     * 条码扫描
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 12:33
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO2
     */
    HmeWoInputRecordDTO2 materialLotGet(Long tenantId, HmeWoInputRecordDTO3 dto);

    /**
     * 工单投料
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 17:41
     * @return void
     */
    HmeWoInputRecordDTO5 woInputRecordUpdate(Long tenantId, HmeWoInputRecordDTO5 dto);

    /**
     * 主料投料
     *
     * @param tenantId
     * @param recordDTO
     * @param dtoList
     * @author jiangling.zheng@hand-china.com 2020/12/15 11:30
     * @return void
     */
    void mainPlanInput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, List<HmeWoInputRecordDTO2> dtoList);

    /**
     * 替代料
     *
     * @param tenantId
     * @param recordDTO
     * @param dtoList
     * @author jiangling.zheng@hand-china.com 2020/12/15 12:35
     * @return void
     */
    void substitutePlanInput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, List<HmeWoInputRecordDTO2> dtoList);

    /**
     * 计划内投料事务
     *
     * @param tenantId
     * @param recordDTO
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 20:16
     * @return void
     */
    WmsObjectTransactionRequestVO planInsideInputTrx(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto);

    /**
     * 计划外投料事务
     *
     * @param tenantId
     * @param recordDTO
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 23:56
     * @return void
     */
    WmsObjectTransactionRequestVO planOuterInputTrx(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto);

    /**
     * 主料计划内投料事务
     *
     * @param tenantId
     * @param recordDTO
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/12/15 14:54
     * @return com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO
     */

    WmsObjectTransactionRequestVO mainPlanInsInputTrx(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto);

    /**
     * 主料计划外退料
     *
     * @param tenantId
     * @param mainRecordDTO
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/12/15 15:47
     * @return com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO
     */

    WmsObjectTransactionRequestVO mainPlanOutputTrx(Long tenantId, HmeWoInputRecordDTO4 mainRecordDTO, HmeWoInputRecordDTO2 dto);

    /**
     * 工单退料
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 17:41
     * @return void
     */
    HmeWoInputRecordDTO5 woOutputRecordUpdate(Long tenantId, HmeWoInputRecordDTO5 dto);


    /**
     * 主料退料
     *
     * @param tenantId
     * @param recordDTO
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/12/15 20:43
     * @return void
     */
    void mainPlanOutput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto);

    /**
     * 替代料退料
     *
     * @param tenantId
     * @param recordDTO
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/12/15 20:45
     * @return void
     */
    void substitutePlanOutput(Long tenantId, HmeWoInputRecordDTO4 recordDTO, HmeWoInputRecordDTO2 dto);
}
