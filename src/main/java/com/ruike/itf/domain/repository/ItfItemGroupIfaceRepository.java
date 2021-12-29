package com.ruike.itf.domain.repository;

import com.ruike.wms.domain.entity.WmsItemGroup;
import io.choerodon.mybatis.domain.AuditDomain;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfItemGroupIface;
import tarzan.iface.domain.entity.MtBomComponentIface;

import java.util.List;

/**
 * 物料组接口表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 10:12:42
 */
public interface ItfItemGroupIfaceRepository extends BaseRepository<ItfItemGroupIface> , AopProxy<ItfItemGroupIfaceRepository> {

    /**
     * 入口函数
     *
     * @param tenantId
     * @author jiangling.zheng@hand-china.com 2020/7/17 12:58
     * @return
     */
    void itemGroupIfaceImport(Long tenantId);

    /**
     * 保存物料组
     *
     * @param tenantId
     * @param itfItemGroupIfaceList List<ItfItemGroupIface>
     * @author jiangling.zheng@hand-china.com 2020/7/17 11:48
     * @return
     */
    List<AuditDomain> saveItemGroupIface(Long tenantId,
                                            List<ItfItemGroupIface> itfItemGroupIfaceList);

    /**
     * 更新接口状态
     *
     * @param tenantId
     * @param status
     * @author jiangling.zheng@hand-china.com 2020/7/17 12:59
     * @return
     */
    List<ItfItemGroupIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @param tenantId
     * @param mtBomComponentIfaceList
     * @author jiangling.zheng@hand-china.com 2020/7/17 12:59
     * @return
     */
    void log(Long tenantId, List<AuditDomain> mtBomComponentIfaceList);
}
