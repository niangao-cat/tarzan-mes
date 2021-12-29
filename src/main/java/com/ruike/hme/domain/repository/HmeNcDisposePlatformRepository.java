package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO3;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO4;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO7;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;
import java.util.Map;

/**
 * 不良处理平台-资源库
 *
 * @author chaonan.hu@hand-china.com 2020-06-30 09:44:18
 */
public interface HmeNcDisposePlatformRepository {

    /***
     * @Description 不良代码记录查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO7
     * @auther chaonan.hu
     * @date 2020/6/30
     */
    HmeNcDisposePlatformDTO7 ncRecordQuery(Long tenantId, HmeNcDisposePlatformDTO dto, PageRequest pageRequest);

    /***
     * @Description 工序LOV数据查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO4>
     * @auther chaonan.hu
     * @date 2020/6/30
     */
    Page<HmeNcDisposePlatformDTO3> processLov(Long tenantId, HmeNcDisposePlatformDTO4 dto, PageRequest pageRequest);

    /***
     * @Description 工位LOV数据查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO6>
     * @auther chaonan.hu
     * @date 2020/6/30
     */
    Page<HmeNcDisposePlatformDTO6> workcellLov(Long tenantId, HmeNcDisposePlatformDTO5 dto, PageRequest pageRequest);

    /***
     * @Description //根据工位查询不良类型组
     * @param tenantId
     * @param workcellId
     * @param componentRequired
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO9
     * @auther chaonan.hu
     * @date 2020/6/30
    */
    HmeNcDisposePlatformDTO9 getProcessNcCodeTypes(Long tenantId, String workcellId, String componentRequired);

    /***
     * @Description 工序不良提交
     * @param tenantId
     * @param dto
     * @return String
     * @auther chaonan.hu
     * @date 2020/7/1
     */
    String processNcTypeCreate(Long tenantId, HmeNcDisposePlatformDTO11 dto);

    /***
     * @Description 材料清单查询(用于页面点击查询加载数据)
     * @param siteId 组织Id
     * @param tenantId 租户Id
     * @param materialLotCode 序列号
     * @param workcellId 工位Id     \
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO23>
     * @auther chaonan.hu
     * @date 2020/7/1
    */
    Page<HmeNcDisposePlatformDTO23> materialDataQuery(Long tenantId, String siteId, String workcellId, String materialLotCode, PageRequest pageRequest);

    /***
     * @Description 材料清单分页查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO23>
     * @auther chaonan.hu
     * @date 2020/7/2
    */
    Page<HmeNcDisposePlatformDTO23> materialDataPageQuery(Long tenantId, HmeNcDisposePlatformDTO dto, PageRequest pageRequest);

    /***
     * @Description 材料清单数据删除
     * @param tenantId
     * @param dtoList
     * @return java.lang.String
     * @auther chaonan.hu
     * @date 2020/7/6
    */
    String materialDataDelete(Long tenantId, List<HmeNcDisposePlatformDTO22> dtoList);

    /***
     * @Description 材料不良提交
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO18
     * @auther chaonan.hu
     * @date 2020/7/2
    */
    HmeNcDisposePlatformDTO18 materialNcTypeCreate(Long tenantId, HmeNcDisposePlatformDTO18 dto);

    /**
     * 查询备注
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/15 16:46:30
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO26
     */
    HmeNcDisposePlatformDTO26 commentsQuery(Long tenantId, HmeNcDisposePlatformDTO25 dto);

    /**
     * 物料批解绑容器
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批条码
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/17 20:39:52
     * @return void
     */
    void materialLotUnBindingContainer(Long tenantId, String materialLotId);

    /**
     * 材料清单查询-第二版逻辑
     *
     * @param tenantId      租户Id
     * @param mtMaterialLot 扫描的SN
     * @param processId     工序ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO4>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/27 05:21:24
     */
    List<HmeNcDisposePlatformVO4> materialDataQuery2(Long tenantId, MtMaterialLot mtMaterialLot, String processId, String workcellId);

    /**
     * 是否芯片物料
     *
     * @param dto
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author sanfeng.zhang@hand-china.com 2021/3/1 14:51
     */
    Map<String, Object> cosMaterialJudge(Long tenantId, HmeNcDisposePlatformDTO18 dto);

    /**
     * 芯片装载信息查询
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.vo.HmeNcDisposePlatformVO7
     * @author sanfeng.zhang@hand-china.com 2021/3/1 15:20
     */
    HmeNcDisposePlatformVO7 cosLoadQuery(Long tenantId, HmeNcDisposePlatformDTO18 dto);

    /**
     * 烧结已投料的芯片报废时，更新性能信息
     *
     * @param tenantId 租户ID
     * @param eoId eoID
     * @param workcellId 工位ID
     * @param loadSequence loadSequence
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/18 10:01:18
     * @return void
     */
    void updateFunctionInfo(Long tenantId, String eoId, String workcellId, String loadSequence);

}
