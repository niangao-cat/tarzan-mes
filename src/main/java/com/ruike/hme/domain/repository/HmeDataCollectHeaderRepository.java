package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeDataCollectHeader;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO2;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO3;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO4;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import java.util.Map;

/**
 * 生产数据采集头表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:35:58
 */
public interface HmeDataCollectHeaderRepository extends BaseRepository<HmeDataCollectHeader>, AopProxy<HmeDataCollectHeaderRepository> {


    /**
     * 生产数据采集列表
     *
     * @param tenantId      租户Id
     * @param lineVO        查询条件
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/16 20:44
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeDataCollectLineVO3>
     */
    HmeDataCollectLineVO3 queryDataCollectLineList(Long tenantId, HmeDataCollectLineVO lineVO);

    /**
     * 物料Id判断物料是否为SN物料 0-否 1-是
     *
     * @param tenantId      租户Id
     * @param materialId    物料编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/16 20:48
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String,Object> querySnMaterialQty(Long tenantId,String materialId);

    /**
     * 更新行表信息
     *
     * @param tenantId 租户Id
     * @param lineVo   更新参数
     * @return void
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/16 20:50
     */
    void updateDataCollectLineInfo(Long tenantId, HmeDataCollectLineVO2 lineVo);

    /**
     * 更新头表信息
     *
     * @param tenantId
     * @param lineVO
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/16 20:56
     * @return void
     */
    void updateDataCollectHeaderInfo(Long tenantId,HmeDataCollectLineVO lineVO);

    /**
     * 生产数据采集-工位扫描
     *
     * @param tenantId          租户id
     * @param workcellCode      工位编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/17 17:23
     * @return com.ruike.hme.api.dto.HmeDataCollectLineVO4
     */
    HmeDataCollectLineVO4 workcellCodeScan(Long tenantId, String workcellCode);
}
