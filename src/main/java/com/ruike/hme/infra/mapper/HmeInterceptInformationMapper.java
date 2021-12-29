package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeInterceptInformationDTO;
import com.ruike.hme.domain.entity.HmeInterceptInformation;
import com.ruike.hme.domain.vo.HmeInterceptInformationVO;
import com.ruike.hme.domain.vo.HmeInterceptReleaseVO;
import com.ruike.hme.domain.vo.HmePopupWindowVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拦截单信息表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:07
 */
public interface HmeInterceptInformationMapper extends BaseMapper<HmeInterceptInformation> {
    /**
     * 拦截单信息表查询
     *
     * @param tenantId 租户id
     * @param dto      查询条件
     * @return
     */
    List<HmeInterceptInformationVO> queryHmeInterceptInformation(@Param("tenantId") Long tenantId, @Param("dto") HmeInterceptInformationDTO dto);


    /**
     * 拦截维度为实验时 查询sn MATERIAL_LOT_ID
     *
     * @param tenantId
     * @param interceptId
     * @return
     */
    List<HmePopupWindowVO> queryInterceptExperimentList(@Param("tenantId") Long tenantId, @Param("interceptId") String interceptId);

    /**
     * 拦截维度为 工单时 查询sn
     *
     * @param tenantId
     * @param interceptId
     * @return
     */
    List<HmePopupWindowVO> queryInterceptDimensionList(@Param("tenantId") Long tenantId, @Param("interceptId") String interceptId);

    /**
     * 拦截维度为 序列号时 查询sn
     *
     * @param tenantId
     * @param interceptId
     * @return
     */
    List<HmePopupWindowVO> queryInterceptSerialNumber(@Param("tenantId") Long tenantId, @Param("interceptId") String interceptId);

    /**
     * 拦截维度为 组件库存批次 查询sn
     *
     * @param tenantId
     * @param interceptId
     * @return
     */
    List<HmePopupWindowVO> queryInterceptComponentBatch(@Param("tenantId") Long tenantId, @Param("interceptId") String interceptId);

    /**
     * 拦截维度为 组件供应商批次 查询sn
     *
     * @param tenantId
     * @param interceptId
     * @return
     */
    List<HmePopupWindowVO> queryInterceptComponentSupplierBatch(@Param("tenantId") Long tenantId, @Param("interceptId") String interceptId);


    /**
     * 判断 materialLotId 是否在release表里
     *
     * @param tenantId
     * @param interceptId
     * @return
     */
    List<HmeInterceptReleaseVO> whetherExistence(@Param("tenantId") Long tenantId, @Param("interceptId") String interceptId);

    /**
     * 查询 WorkcellId
     *
     * @param tenantId      租户id
     * @param materialLotId 信息表主键
     * @return
     */
    List<HmePopupWindowVO> queryWorkcellId(@Param("tenantId") Long tenantId, @Param("materialLotId") List<String> materialLotId);

    /**
     * 查询workcellCode和workcellName
     *
     * @param tenantId
     * @param workcellId 工位id
     * @return
     */
    List<HmePopupWindowVO> queryWorkcellCodeAndName(@Param("tenantId") Long tenantId, @Param("workcellId") List<String> workcellId);

    /**
     * 拦截维度为组件库存批次查询序列SN
     *
     * @param tenantId
     * @param interceptId 拦截单ID
     * @param materialIdList 物料ID集合
     * @return
     */
    List<HmePopupWindowVO> querySnInterceptByLot(@Param("tenantId") Long tenantId,
                                                 @Param("interceptId") String interceptId,
                                                 @Param("materialIdList") List<String> materialIdList);

    /**
     * 拦截维度为组件库存批次查询批次时效SN
     *
     * @param tenantId
     * @param interceptId 拦截单ID
     * @param materialIdList 物料ID集合
     * @return
     */
    List<HmePopupWindowVO> queryLotTimeInterceptByLot(@Param("tenantId") Long tenantId,
                                                      @Param("interceptId") String interceptId,
                                                      @Param("materialIdList") List<String> materialIdList);

    /**
     * 拦截维度为供应商批次查询序列SN
     *
     * @param tenantId
     * @param interceptId 拦截单ID
     * @param materialIdList 物料ID集合
     * @return
     */
    List<HmePopupWindowVO> querySnInterceptBySupplierLot(@Param("tenantId") Long tenantId,
                                                         @Param("interceptId") String interceptId,
                                                         @Param("materialIdList") List<String> materialIdList);

    /**
     * 拦截维度为供应商批次查询批次时效SN
     *
     * @param tenantId
     * @param interceptId 拦截单ID
     * @param materialIdList 物料ID集合
     * @return
     */
    List<HmePopupWindowVO> queryLotTimeInterceptBySupplierLot(@Param("tenantId") Long tenantId,
                                                              @Param("interceptId") String interceptId,
                                                              @Param("materialIdList") List<String> materialIdList);
}
