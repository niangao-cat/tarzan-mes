package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO2;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO3;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO4;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserInfo;

import java.util.List;

/**
 * 不良处理平台应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-06-30 09:49:16
 */
public interface HmeNcDisposePlatformService {

    /**
     * 不良代码记录查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO7
     */
    HmeNcDisposePlatformDTO7 ncRecordQuery(Long tenantId, HmeNcDisposePlatformDTO dto, PageRequest pageRequest);

    /**
     * 工序LOV数据查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO4>
     */
    Page<HmeNcDisposePlatformDTO3> processLov(Long tenantId, HmeNcDisposePlatformDTO4 dto, PageRequest pageRequest);

    /**
     * 工位LOV数据查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO6>
     */
    Page<HmeNcDisposePlatformDTO6> workcellLov(Long tenantId, HmeNcDisposePlatformDTO5 dto, PageRequest pageRequest);

    /**
     * 根据序列号查询产品料号
     *
     * @param tenantId        租户ID
     * @param materialLotCode 批次号
     * @return com.ruike.hme.domain.vo.HmeNcDisposePlatformVO
     */
    HmeNcDisposePlatformVO getMaterialCode(Long tenantId, String materialLotCode);

    /**
     * 获取当前登录用户信息
     *
     * @param tenantId 租户ID
     * @return io.tarzan.common.domain.sys.MtUserInfo
     */
    MtUserInfo getCurrentUser(Long tenantId);

    /**
     * 查询其他的工序不良类型组
     *
     * @param workcellId  工位ID
     * @param description 不良类型组描述
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO8>
     */
    Page<HmeNcDisposePlatformDTO8> getOtherProcessNcType(String workcellId, String description, PageRequest pageRequest);

    /***
     * 查询其他的工序不良类型组
     * @param workcellId 工序ID
     * @param description 不良类型组描述
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO8>
     */
    Page<HmeNcDisposePlatformDTO8> getOtherMaterialNcType(String workcellId, String description, PageRequest pageRequest);

    /***
     * 查询其他工位
     * @param tenantId 租户Id
     * @param dto 查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO12>
     */
    Page<HmeNcDisposePlatformDTO12> getOtherWorkcell(Long tenantId, HmeNcDisposePlatformDTO10 dto, PageRequest pageRequest);

    /**
     * 工序不良提交
     *
     * @param tenantId 租户ID
     * @param dto      提交数据
     * @return String
     */
    String processNcTypeCreate(Long tenantId, HmeNcDisposePlatformDTO11 dto);

    /**
     * 物料行上的扫描条码逻辑 获得数量
     *
     * @param tenantId 租户ID
     * @param dto      扫码数据
     * @return java.lang.String
     */
    HmeNcDisposePlatformVO2 getMaterialLotId(Long tenantId, HmeNcDisposePlatformDTO14 dto);

    /**
     * 物料行上的扫描条码保存
     *
     * @param tenantId 租户ID
     * @param dto 保存数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/10 15:15:01
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO27
     */
    HmeNcDisposePlatformDTO27 materialLotScanLineSubmit(Long tenantId, HmeNcDisposePlatformDTO27 dto);

    /**
     * 材料清单扫描条码逻辑 (非序列号物料)
     *
     * @param tenantId 租户ID
     * @param dto      扫码数据
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO4
     */
    HmeNcDisposePlatformVO4 materialLotScan(Long tenantId, HmeNcDisposePlatformDTO15 dto);

    /**
     * 材料清单扫描条码逻辑 (序列号物料)
     *
     * @param tenantId 租户ID
     * @param dto      扫码数据
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO13
     */
    HmeNcDisposePlatformDTO13 materialLotScan2(Long tenantId, HmeNcDisposePlatformDTO16 dto);

    /**
     * 材料清单扫描条码保存
     *
     * @param tenantId 租户ID
     * @param dto      扫码数据
     * @return java.lang.String
     */
    String materialLotScanSubmit(Long tenantId, HmeNcDisposePlatformDTO20 dto);


    /**
     * 材料不良提交
     *
     * @param tenantId 租户ID
     * @param dto      提交数据
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO18
     */
    HmeNcDisposePlatformDTO18 materialNcTypeCreate(Long tenantId, HmeNcDisposePlatformDTO18 dto);

    /**
     * 不良代码记录单独查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO2>
     */
    List<HmeNcDisposePlatformDTO2> ncRecordSingleQuery(Long tenantId, HmeNcDisposePlatformDTO dto);

    /***
     * @Description 材料清单数据删除
     * @param tenantId
     * @param dtoList
     * @return java.lang.String
     * @auther chaonan.hu
     * @date 2020/7/6
     */
    String materialDataDelete(Long tenantId, List<HmeNcDisposePlatformDTO22> dtoList);

    /**
     * 单独查询材料清单数据
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/27 21:43:41
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO4>
     */
    List<HmeNcDisposePlatformVO4> materialDataSingleQuery(Long tenantId, HmeNcDisposePlatformDTO30 dto);

    /**
     * 工位输入框支持手动输入工位编码
     *
     * @param tenantId 租户ID
     * @param dto 工位编码，工序ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/26 02:53:40
     * @return java.lang.String
     */
    String workcellScan(Long tenantId, HmeNcDisposePlatformDTO5 dto);
}
