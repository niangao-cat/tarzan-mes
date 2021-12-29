package com.ruike.hme.app.service;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnSingleDTO;
import com.ruike.hme.api.dto.HmeEoLovQueryDTO;
import com.ruike.hme.api.dto.HmeMaterialLotLovQueryDTO;
import com.ruike.hme.api.dto.HmeWoLovQueryDTO;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.*;

/**
 * @Classname HmeEoJobFirstProcessService
 * @Description 首序作业平台
 * @Date 2020/9/1 9:42
 * @Author yuchao.wang
 */
public interface HmeEoJobFirstProcessService {

    /**
     *
     * @Description 首道序作业平台SN条码扫描
     *
     * @author yuchao.wang
     * @date 2020/9/1 10:31
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO
     *
     */
    HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 序列物料投料扫描
     *
     * @author yuchao.wang
     * @date 2020/9/1 20:17
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobMaterialVO
     *
     */
    List<HmeEoJobMaterialVO> releaseScan(Long tenantId, HmeEoJobMaterialVO dto);

    /**
     *
     * @Description 查询工单LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 13:52
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWorkOrderVO>
     *
     */
    Page<HmeWorkOrderVO> workOrderQuery(Long tenantId, HmeWoLovQueryDTO dto, PageRequest pageRequest);

    /**
     *
     * @Description 查询SN号是否在物料批中
     *
     * @author yuchao.wang
     * @date 2020/9/1 15:48
     * @param tenantId 租户ID
     * @param barcode 条码
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     *
     */
    MtMaterialLot queryMaterialLot(Long tenantId, String barcode);

    /**
     *
     * @Description 查询条码是否为容器条码
     *
     * @author yuchao.wang
     * @date 2020/9/4 17:34
     * @param tenantId 租户ID
     * @param barcode 条码
     * @return tarzan.inventory.domain.entity.MtContainer
     *
     */
    MtContainer queryContainer(Long tenantId, String barcode);

    /**
     *
     * @Description EO查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 16:29
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEoVO>
     *
     */
    Page<HmeEoVO> eoQuery(Long tenantId, HmeEoLovQueryDTO dto, PageRequest pageRequest);

    /**
     *
     * @Description 物料批查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 19:34
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeMaterialLotVO2>
     *
     */
    Page<HmeMaterialLotVO2> materialLotLovQuery(Long tenantId, HmeMaterialLotLovQueryDTO dto, PageRequest pageRequest);

    /**
     *
     * @Description 查询条码下的虚拟号
     *
     * @author yuchao.wang
     * @date 2020/9/29 21:38
     * @param tenantId 租户ID
     * @param materialLotCodeList 条码号
     * @return java.lang.Object
     *
     */
    List<HmeVirtualNumVO> virtualNumQuery(Long tenantId, List<String> materialLotCodeList);

    String firstPartQuery(Long tenantId, String materialId);

    /**
     *
     * @Description 投料
     *
     * @author penglin.sui
     * @date 2021/7/6 11:37
     * @param tenantId 租户ID
     * @param dto 参数
     * @return List<HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO4> release(Long tenantId, HmeEoJobSnSingleDTO dto);

    /**
     * 投料退回
     *
     * @param tenantId  租户ID
     * @param dto       退料参数
     * @return HmeEoJobSnVO9
     */
    HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto);
}
