package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtStandardOperationIface;

/**
 * 标准工序接口表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtStandardOperationIfaceRepository
                extends BaseRepository<MtStandardOperationIface>, AopProxy<MtStandardOperationIfaceRepository> {

    /**
     * 入口函数
     *
     * @author benjamin
     * @date 2019-07-10 10:00
     * @param tenantId
     */
    void standardOperationInterfaceImport(Long tenantId);

    /**
     * 保存标准工序
     *
     * @author benjamin
     * @date 2019-07-10 10:50
     * @param tenantId
     * @param mtStandardOperationIfaceList List<MtStandardOperationIface>
     * @return List
     */
    List<AuditDomain> saveStandardOperationIface(Long tenantId,
                                                 List<MtStandardOperationIface> mtStandardOperationIfaceList);

    /**
     * 更新接口状态
     *
     * @author guichuan.li
     * @date 2019/7/11
     */
    List<MtStandardOperationIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-10 10:51
     * @param mtStandardOperationIfaceList List<MtStandardOperationIface>
     */
    void log(Long tenantId, List<AuditDomain> mtStandardOperationIfaceList);
}
