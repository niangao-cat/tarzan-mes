package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsWarehousePrivilegeQueryDTO;
import com.ruike.hme.domain.vo.HmeUserOrganizationVO;
import com.ruike.wms.domain.entity.WmsDocPrivilege;
import com.ruike.wms.domain.vo.WmsDocPrivilegeVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单据授权表Mapper
 *
 * @author junfeng.chen@hand-china.com 2021-01-19 20:21:30
 */
public interface WmsDocPrivilegeMapper extends BaseMapper<WmsDocPrivilege> {
    /**
     * 查询单据授权表
     *
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-19 15:05
     * @return com.ruike.wms.domain.vo.WmsDocPrivilegeVO
     */
    List<WmsDocPrivilegeVO> userPrivilegeForUi(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsDocPrivilegeVO dto);

    /**
     * 根据唯一性查询单据授权表,返回判断是否存在
     * @param tenantId
     * @param importVO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-21 13:05
     * @return com.ruike.wms.domain.vo.WmsDocPrivilegeVO
     */
    WmsDocPrivilege selectPrivilege(@Param(value = "tenantId") Long tenantId, @Param(value = "importVO") HmeUserOrganizationVO importVO);

    /**
     * 查询仓库权限
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/21 11:12:31
     */
    int selectWarehousePrivilegeCount(@Param(value = "tenantId") Long tenantId,
                                      @Param(value = "dto") WmsWarehousePrivilegeQueryDTO dto);
}
