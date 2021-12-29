package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtOperationComponentIface;

/**
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtOperationComponentIfaceRepository
                extends BaseRepository<MtOperationComponentIface>, AopProxy<MtOperationComponentIfaceRepository> {


    /**
     * operationComponentInterfaceImport-工序组件接口导入
     *
     * @author guichuan.li
     * @date 2019/7/11
     */
    void operationComponentInterfaceImport(Long tenantId);
    void myOperationComponentInterfaceImport(Long tenantId, Long batchId);

    /**
     * 更新接口状态
     *
     * @author guichuan.li
     * @date 2019/7/11
     */
    List<MtOperationComponentIface> updateIfaceStatus(Long tenantId, String status);
    List<MtOperationComponentIface> myUpdateIfaceStatus(Long tenantId, String status,Long batchId);
    /**
     * 保存接口数据
     *
     * @param operationComponentIfaceList
     * @author guichuan.li
     * @date 2019/7/11
     */
    List<AuditDomain> saveInterfaceData(Long tenantId, List<MtOperationComponentIface> operationComponentIfaceList);

    /**
     * 记录日志
     *
     * @param ifaceSqlList
     * @author guichuan.li
     * @date 2019/7/11
     */
    void log(Long tenantId, List<AuditDomain> ifaceSqlList);

}
