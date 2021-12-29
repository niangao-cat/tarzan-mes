package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.vo.MtOperationVO;
import tarzan.method.domain.vo.MtOperationVO1;
import tarzan.method.domain.vo.MtOperationVO2;
import tarzan.method.domain.vo.MtOperationVO3;

/**
 * 工序资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
public interface MtOperationRepository extends BaseRepository<MtOperation>, AopProxy<MtOperationRepository> {

    /**
     * 获取工艺基础属性
     *
     * @param tenantId
     * @param operationId
     * @return
     */
    MtOperation operationGet(Long tenantId, String operationId);

    /**
     * operationBatchGet-批量获取工艺基础属性
     *
     * @param tenantId
     * @param operationIds
     * @return
     */
    List<MtOperation> operationBatchGet(Long tenantId, List<String> operationIds);

    /**
     * operationCurrentVersionGet-基于工艺名称获取当前有效版本工艺ID
     *
     * @param tenantId
     * @param operationName
     * @param siteId
     * @return
     */
    String operationCurrentVersionGet(Long tenantId, String operationName, String siteId);

    /**
     * operationAllVersionQuery-基于工艺名称获取所有版本
     *
     * @param tenantId
     * @param operationName
     * @param siteId
     * @return
     */
    List<String> operationAllVersionQuery(Long tenantId, String operationName, String siteId);

    /**
     * operationTypeQuery-获取特定类型的工艺
     *
     * @param tenantId
     * @param operationType
     * @param siteId
     * @return
     */
    List<String> operationTypeQuery(Long tenantId, String operationType, String siteId);

    /**
     * operationSpecialRouterGet-基于工艺ID获取特殊工艺路线ID
     *
     * @param tenantId
     * @param operationId
     * @return
     */
    String operationSpecialRouterGet(Long tenantId, String operationId);

    /**
     * operationAvailabilityValidate-工艺可用性校验
     *
     * @param tenantId
     * @param operationId
     * @return
     */
    String operationAvailabilityValidate(Long tenantId, String operationId);

    /**
     * opeationCurrentVersionUpdate-工艺当前版本更新
     *
     * @param tenantId
     * @param operationId
     * @return operationId
     */
    String operationCurrentVersionUpdate(Long tenantId, String operationId);

    /**
     * propertyLimitOperationPropertyQuery-根据属性获取工艺信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtOperationVO1> propertyLimitOperationPropertyQuery(Long tenantId, MtOperationVO dto);

    /**
     * 基于工艺名称获批量获取前有效版本工艺ID
     */
    List<MtOperation> operationCurrentVersionQuery(Long tenantId, List<String> operationName, String siteId);

    /**
     * operationAttrPropertyUpdate-工艺新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/20 16:03
     * @param tenantId
     * @param vo
     * @return void
     */
    void operationAttrPropertyUpdate(Long tenantId, MtExtendVO10 vo);

    /**
     * 获取工艺属性获取工艺
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<String> propertyLimitOperationQuery(Long tenantId, MtOperationVO2 vo);

    /**
     * 根据工艺属性批量获取工艺
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<String> propertyLimitOperationBatchQuery(Long tenantId, MtOperationVO3 vo);

    /**
     * 基于工艺名称获批量工艺ID
     * @param tenantId
     * @param operationName
     * @return
     */
    List<MtOperation> selectByOperationName(Long tenantId, List<String> operationName);

}
