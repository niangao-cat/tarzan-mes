package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtNcValidOper;
import tarzan.method.domain.vo.MtNcValidOperVO1;

/**
 * 不良代码工艺分配资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcValidOperRepository extends BaseRepository<MtNcValidOper>, AopProxy<MtNcValidOperRepository> {

    /**
     * ncCodeOperationValidate-根据工艺验证不良代码是否可用
     *
     * @author chuang.yang
     * @date 2019/4/1
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String ncCodeOperationValidate(Long tenantId, MtNcValidOperVO1 dto);


    /**
     * ncValidOperationQuery-根据不良代码获取可用工艺清单
     *
     * @param tenantId
     * @param ncCodeId
     * @return
     */
    List<MtNcValidOper> ncValidOperationQuery(Long tenantId, String ncCodeId);

    /**
     * operationValidNcQuery-根据工艺获取可用不良代码清单
     *
     * @author chuang.yang
     * @date 2019/4/4
     * @param tenantId
     * @param operationId
     * @return java.util.List<hmes.nc_valid_oper.dto.MtNcValidOper>
     */
    List<MtNcValidOper> operationValidNcQuery(Long tenantId, String operationId);
}
