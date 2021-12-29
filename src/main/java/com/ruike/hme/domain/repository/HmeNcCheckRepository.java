package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.HmeModAreaVO2;
import com.ruike.hme.domain.vo.HmeModAreaVO3;
import com.ruike.hme.domain.vo.HmeNcCheckVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenStatus;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModArea;

import java.util.List;

/**
 * 不良申请单审核-资源库
 *
 * @author chaonan.hu@hand-china.com 2020-07-03 10:16:35
 */
public interface HmeNcCheckRepository {

    /**
     * 不良代码记录查询
     *
     * @param tenantId    租户Id
     * @param dto         查询条件
     * @param pageRequest 分页
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO2>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/3 10:23:12
     */
    Page<HmeNcDisposePlatformDTO2> ncRecordQuery(Long tenantId, HmeNcCheckDTO dto, PageRequest pageRequest);

    /**
     * 不良审核提交
     *
     * @param tenantId 租户ID
     * @param dto 提交数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/3 11:18:34
     */
    void ncCheck(Long tenantId, HmeNcCheckDTO2 dto);

    /**
     * 不良代码组LOV数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页参数
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/20 14:58:17
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNcCheckVO>
     */
    Page<HmeNcCheckVO> ncGroupLovQuery(Long tenantId, HmeNcCheckDTO3 dto, PageRequest pageRequest);

    /**
     * 不良状态查询
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/3 10:15:55 
     * @return java.util.List<io.tarzan.common.domain.entity.MtGenStatus>
     */
    List<MtGenStatus> ncStatusQuery(Long tenantId);


    void materialScrap(Long tenantId, MtMaterialLot mtMaterialLot, MtNcRecord mtNcRecord, String eventId, String eventRequestId);

    /**
     * 事业部LOV
     *
     * @param tenantId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeModAreaVO3>
     * @author sanfeng.zhang@hand-china.com 2020/12/24 17:03
     */
    List<HmeModAreaVO3> areaUnitQuery(Long tenantId);

    /**
     * 批量不良处理审核提交
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/27 16:11
     */
    void batchCheckSubmit(Long tenantId, HmeNcCheckDTO4 dto);

    /**
     * 根据工艺等信息查询指定工艺路线信息
     * @param tenantId
     * @param ncGroupId
     * @param ncCodeIdList
     * @param prodLineId
     * @param deviceType
     * @param chipType
     * @param operationId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/8/18
     */
    List<String> queryRouteIdListLimitOperationId(Long tenantId, String ncGroupId, List<String> ncCodeIdList, String prodLineId, String deviceType, String chipType, String operationId);

    /**
     * 查询指定工艺路线信息
     * @param tenantId
     * @param ncGroupId
     * @param ncCodeIdList
     * @param prodLineId
     * @param deviceType
     * @param chipType
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/8/18
     */
    List<String> queryRouteIdListNonLimitOperationId(Long tenantId, String ncGroupId, List<String> ncCodeIdList, String prodLineId, String deviceType, String chipType);

    /**
     * 根据工艺等信息查询指定工艺路线信息
     * @param tenantId
     * @param dto
     * @return
     * @author xin.t@raycuslaser.com 2021-11-16 20:27
     */
    List<HmeNcDisposePlatformDTO2> export(Long tenantId, HmeNcCheckDTO dto);
}
