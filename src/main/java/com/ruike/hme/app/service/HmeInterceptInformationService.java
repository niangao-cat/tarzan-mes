package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeInterceptInformationDTO;
import com.ruike.hme.domain.vo.HmeInterceptInformationVO;
import com.ruike.hme.domain.vo.HmeInterceptObjectVO;
import com.ruike.hme.domain.vo.HmePopupWindowNumberVO;
import com.ruike.hme.domain.vo.HmePopupWindowVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 拦截单信息表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:07
 */
public interface HmeInterceptInformationService {

    /**
     * 拦截单信息表查询参数
     *
     * @param tenantId    租户id
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeInterceptInformationVO> queryInterceptInformation(Long tenantId, HmeInterceptInformationDTO dto, PageRequest pageRequest);


    /**
     * 创建拦截单信息表
     *
     * @param tenantId                  租户id
     * @param hmeInterceptInformationVO 拦截信息表数据
     */
    void saveInterceptInformation(Long tenantId, HmeInterceptInformationVO hmeInterceptInformationVO);

    /**
     * 拦截弹窗查询
     *
     * @param tenantId    租户id
     * @param interceptId 拦截单id
     * @param pageRequest 分页参数
     * @return
     */
    HmePopupWindowNumberVO queryInterceptPopupWindow(Long tenantId, String interceptId, PageRequest pageRequest);

    /**
     * 数据导出
     *
     * @param tenantId    租户id
     * @param interceptId 拦截单id
     * @return
     */
    List<HmePopupWindowVO> export(Long tenantId, String interceptId);

    /**
     * 查询物料类型
     *
     * @param tenantId    租户id
     * @param interceptId 拦截单id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInterceptObjectVO>
     */
    List<HmeInterceptObjectVO> selectMaterialType(Long tenantId, String interceptId);
}
