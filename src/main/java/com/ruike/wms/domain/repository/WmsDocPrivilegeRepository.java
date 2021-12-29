package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsWarehousePrivilegeBatchQueryDTO;
import com.ruike.wms.api.dto.WmsWarehousePrivilegeQueryDTO;
import com.ruike.wms.domain.entity.WmsDocPrivilege;
import com.ruike.wms.domain.vo.WmsDocPrivilegeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 单据授权表资源库
 *
 * @author junfeng.chen@hand-china.com 2021-01-19 20:21:30
 */
public interface WmsDocPrivilegeRepository extends BaseRepository<WmsDocPrivilege> {
    /**
     * 查询单据授权表
     *
     * @param tenantId
     * @param dto
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsDocPrivilegeVO>
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-19 15:05
     */
    Page<WmsDocPrivilegeVO> userPrivilegeForUi(Long tenantId, PageRequest pageRequest, WmsDocPrivilegeVO dto);

    /**
     * 保存单据授权表
     *
     * @param tenantId          租户ID
     * @param wmsDocPrivilegeVO 前端传参
     * @return
     * @author junfeng.chen@hand-china.com 2021/1/19 21:00:00
     */
    WmsDocPrivilegeVO save(Long tenantId, WmsDocPrivilegeVO wmsDocPrivilegeVO);

    /**
     * 判断仓库是否有权限
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/21 11:12:31
     */
    void isWarehousePrivileged(Long tenantId,
                               WmsWarehousePrivilegeQueryDTO dto);

    /**
     * 判断是否存在仓库有权限
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.lang.Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/26 10:21:18
     */
    Boolean existsWarehousePrivileged(Long tenantId,
                                      WmsWarehousePrivilegeBatchQueryDTO dto);

    /**
     * 判断是否全部仓库有权限
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.lang.Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/26 10:21:18
     */
    Boolean allWarehousePrivileged(Long tenantId,
                                   WmsWarehousePrivilegeBatchQueryDTO dto);
}
