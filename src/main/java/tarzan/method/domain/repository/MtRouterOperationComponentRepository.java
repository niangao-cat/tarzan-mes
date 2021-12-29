package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.vo.*;

/**
 * 工艺路线步骤对应工序组件资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterOperationComponentRepository
                extends BaseRepository<MtRouterOperationComponent>, AopProxy<MtRouterOperationComponentRepository> {

    /**
     * routerOperationComponentQuery-获取步骤的组件清单
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    List<MtRouterOperationComponent> routerOperationComponentQuery(Long tenantId, String routerStepId);

    /**
     * routerOperationComponentPerQtyQuery获取步骤的组件单位用量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterOpComponentVO> routerOperationComponentPerQtyQuery(Long tenantId, MtRouterOpComponentVO1 dto);

    /**
     * routerOperationComponentVerify-验证物料是否工艺路线步骤组件
     *
     * @author chuang.yang
     * @date 2019/3/21
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String routerOperationComponentVerify(Long tenantId, MtRouterOpComponentVO2 dto);


    /**
     * operationOrMaterialLimitBomComponentQuery-根据工艺或组件物料获取装配清单组件行ID
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> operationOrMaterialLimitBomComponentQuery(Long tenantId, MtRouterOpComponentVO3 condition);

    /**
     * 工艺路线步骤对应工序组件新增&更新扩展表属性
     *
     * @Author peng.yuan
     * @Date 2019/11/12 14:56
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void routerOperationCompAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * 自定义-根据routerId获取所有的工序组件信息
     */
    List<MtRouterOpComponentVO4> selectRouterOpComponentByRouterIds(Long tenantId, List<String> routerIds);

    /**
     * 自定义-根据装配清单组件获取所有的工序组件信息
     */
    List<MtRouterOperationComponent> selectRouterOpComponentByBomComponentId(Long tenantId,
                    List<String> bomComponentIds);

    /**
     * 自定义-根据工艺路线步骤+装配清单组件获取所有的工序组件信息
     */
    List<MtRouterOperationComponent> selectByRouterOperationIdsAndComponentIds(Long tenantId,
                    List<String> routerOperationIds, List<String> bomComponentIds);


    /**
     * routerOperationComponentPerQtyBatchQuery-批量获取步骤的组件单位用量
     *
     * @author benjamin
     * @date 2020/11/2 2:04 PM
     * @param tenantId
     * @param dtoList
     * @return
     */
    List<MtRouterOpComponentVO> routerOperationComponentPerQtyBatchQuery(Long tenantId,
                    List<MtRouterOpComponentVO1> dtoList);

    /**
     * operationOrMaterialLimitBomComponentBatchQuery-根据工艺或组件物料批量获取装配清单组件行ID
     *
     * @author chuang.yang
     * @date 2020/11/2
     */
    List<MtRouterOpComponentVO6> operationOrMaterialLimitBomComponentBatchQuery(Long tenantId,
                    List<MtRouterOpComponentVO3> condition);
}
