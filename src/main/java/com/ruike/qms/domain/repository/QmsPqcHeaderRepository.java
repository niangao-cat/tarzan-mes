package com.ruike.qms.domain.repository;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.domain.entity.QmsPqcInspectionScheme;
import com.ruike.qms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.qms.domain.entity.QmsPqcHeader;
import tarzan.order.domain.entity.MtWorkOrder;

import java.util.List;

/**
 * 巡检单头表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-08-17 11:49:31
 */
public interface QmsPqcHeaderRepository extends BaseRepository<QmsPqcHeader> {

    /**
     * 巡检单生成
     *
     * @param tenantId 租户ID
     * @param processId 工序ID
     * @param mtWorkOrder Wo信息
     * @param materialLotCode 物料批编码
     * @param qmsPqcInspectionScheme 巡检检验计划
     * @param materialCode 物料编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/17 06:41:40
     * @return void
     */
    void pgcCreate(Long tenantId, String processId, MtWorkOrder mtWorkOrder, String materialLotCode,
                   QmsPqcInspectionScheme qmsPqcInspectionScheme, String materialCode);

    /**
     * 产线树状图查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/17 19:53:47
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcHeaderVO>
     */
    List<QmsPqcHeaderVO> prodLineQuery(Long tenantId, QmsPqcHeaderDTO2 dto);

    /**
     * 工序树状图查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/17 20:45:44
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcHeaderVO2>
     */
    List<QmsPqcHeaderVO2> processQuery(Long tenantId, QmsPqcHeaderDTO3 dto);

    /**
     * 巡检列表分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/18 09:37:15
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsPqcHeaderVO3>
     */
    Page<QmsPqcHeaderVO3> pqcListQuery(Long tenantId, QmsPqcHeaderDTO4 dto, PageRequest pageRequest);

    /**
     * 巡检信息查询
     *
     * @param tenantId 租户ID
     * @param pqcHeaderId 巡检单头Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/18 10:54:49
     * @return com.ruike.qms.domain.vo.QmsPqcHeaderVO6
     */
    QmsPqcHeaderVO6 pqcInfoQuery(Long tenantId, String pqcHeaderId, PageRequest pageRequest);

    /**
     * 巡检结果查询
     * 
     * @param tenantId 租户Id
     * @param pqcLineId 巡检单行Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/18 11:51:18 
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcHeaderVO7>
     */
    Page<QmsPqcHeaderVO7> pqcResultQuery(Long tenantId, String pqcLineId, PageRequest pageRequest);

    /**
     * 文件上传
     *
     * @param tenantId 租户Id
     * @param dto 文件信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/18 14:12:05
     * @return void
     */
    void attachmentUpload(Long tenantId, QmsPqcHeaderDTO5 dto);

    /**
     * 巡检保存
     *
     * @param tenantId 租户Id
     * @param dto 保存信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/24 21:40:07
     * @return com.ruike.qms.api.dto.QmsPqcHeaderDTO7
     */
    QmsPqcHeaderDTO7 pqcSave(Long tenantId, QmsPqcHeaderDTO7 dto);

    /**
     * 巡检提交
     * 
     * @param tenantId 租户ID
     * @param dto 巡检信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/18 02:45:54 
     * @return void
     */
    void pqcSubmit(Long tenantId, QmsPqcHeaderDTO6 dto);

    /**
     * 巡检平台海马汇版事业部LOV查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/23 10:07:24
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcHeaderVO10>
     */
    List<QmsPqcHeaderVO10> areaLovQuery(Long tenantId, QmsPqcHeaderVO10 dto);

    /**
     * 巡检平台海马汇版车间LOV查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/23 10:21:31
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcHeaderVO11>
     */
    List<QmsPqcHeaderVO11> workshopLovQuery(Long tenantId, QmsPqcHeaderVO11 dto);

    /**
     * 巡检平台海马汇版产线LOV查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/23 10:32:42
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcHeaderVO12>
     */
    List<QmsPqcHeaderVO12> prodLineLovQuery(Long tenantId, QmsPqcHeaderVO12 dto);
}
