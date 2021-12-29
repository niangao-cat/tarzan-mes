package tarzan.method.domain.repository;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.vo.*;

/**
 * 工艺路线资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterRepository extends BaseRepository<MtRouter>, AopProxy<MtRouterRepository> {

    /**
     * routerGet-获取工艺路线基础属性
     *
     * @param tenantId
     * @param routerId
     * @return
     */
    MtRouter routerGet(Long tenantId, String routerId);

    /**
     * routerBatchGet-批量获取工艺路线基础属性
     *
     * @param tenantId
     * @param routerIds
     * @return
     */
    List<MtRouter> routerBatchGet(Long tenantId, List<String> routerIds);

    /**
     * routerCurrentVersionGet-基于工艺路线名称获取当前版本
     *
     * @param tenantId
     * @param router
     * @return
     */
    String routerCurrentVersionGet(Long tenantId, String router);

    /**
     * routerAllVersionQuery-基于工艺路线名称获取所有版本
     *
     * @param tenantId
     * @param router
     * @return
     */
    List<MtRouterVO5> routerAllVersionQuery(Long tenantId, String router);

    /**
     * routerReleasedFlagValidate-工艺路线下达标识检验
     *
     * @param tenantId
     * @param routerId
     * @return
     */
    String routerReleasedFlagValidate(Long tenantId, String routerId);

    /**
     * routerAvailabilityValidate-工艺路线可用性校验
     *
     * @param tenantId
     * @param routerId
     * @return
     */
    void routerAvailabilityValidate(Long tenantId, String routerId);

    /**
     * routerTypeQuery-基于工艺路线类型查询工艺路线
     *
     * @param tenantId
     * @param routerType
     * @return
     */
    List<String> routerTypeQuery(Long tenantId, String routerType);

    /**
     * routerBomGet-基于工艺路线ID获取BOM ID
     *
     * @param tenantId
     * @param routerId
     * @return
     */
    String routerBomGet(Long tenantId, String routerId);

    /**
     * routerCurrentVersionUpdate-工艺路线当前版本更新
     *
     * @Author peng.yuan
     * @Date 2019/9/18 14:23
     * @param tenantId : 租户id
     * @param routerId : 工艺id
     * @return java.util.List<java.lang.String>
     */
    List<String> routerCurrentVersionUpdate(Long tenantId, String routerId);

    /**
     * routerReleasedFlagUpdate-更新工艺路线下达标识
     *
     * @Author peng.yuan
     * @Date 2019/9/18 14:25
     * @param tenantId : 租户id
     * @param routerId : 工艺id
     * @return <java.lang.String>
     */
    String routerReleasedFlagUpdate(Long tenantId, String routerId);

    /**
     * routerDataValidate-工艺路线数据验证
     *
     * @param tenantId
     * @param routerData
     */
    void routerDataValidate(Long tenantId, MtRouterVO10 routerData);

    /**
     * routerCopy-工艺路线复制
     *
     * @param tenantId
     * @param condition
     */
    String routerCopy(Long tenantId, MtRouterVO1 condition);

    /**
     * routerAllUpdate-工艺路线保存
     *
     * @param tenantId
     * @param routerData
     * @update 2019.12.31 增加输入参数 copyFlag，目前服务于 routerCut、sourceRouterLimitRouterAllUpdate
     *         逻辑变更：传入Y或者N，如果为Y就保留原有 attr属性，复制一份出来新的，如果不为Y则更新Attr属性原有的主键ID
     */
    String routerAllUpdate(Long tenantId, MtRouterVO10 routerData, String copyFlag);

    /**
     * primaryRouterLimitNextStepQuery-获取主工艺路线下的下一步骤清单
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterNextStepVO2> primaryRouterLimitNextStepQuery(Long tenantId, MtRouterStep dto);


    /**
     * 获取主工艺路线下的当前决策步骤
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterStepVO8> primaryRouterLimitRouterStepGet(Long tenantId, MtRouterStep dto);

    /**
     * eoNextStepQuery-根据执行作业及当前步骤获取下一步骤
     *
     * @author chuang.yang
     * @date 2019/3/7
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.router.view.MtRouterNextStepVO2>
     */
    List<MtRouterNextStep> eoNextStepQuery(Long tenantId, MtRouterVO4 dto);

    /**
     * routerAllQuery-工艺路线详细查询
     *
     * @author chuang.yang
     * @date 2019/4/25
     * @param tenantId
     * @param routerId
     * @return hmes.router.view.MtRouterVO10
     */
    MtRouterVO10 routerAllQuery(Long tenantId, String routerId);

    /**
     * routerAutoRevisionGet-获取工艺路线是否自动升版本策略
     *
     * @author guichuan.li
     * @date 2019/4/25
     * @param tenantId
     * @param routerId
     * @return java.util.String
     */
    String routerAutoRevisionGet(Long tenantId, String routerId);

    /**
     * sourceRouterLimitRouterAllUpdate-根据已有工艺路线更新目标工艺路线所有属性
     *
     * @author chuang.yang
     * @date 2019/4/27
     * @param tenantId
     * @param dto
     * @return void
     */
    String sourceRouterLimitRouterAllUpdate(Long tenantId, MtRouterVO6 dto);

    /**
     * eoRelaxedFlowVerify-验证执行作业是否松散模式
     *
     * @param tenantId
     * @param eoId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/7
     */
    String eoRelaxedFlowVerify(Long tenantId, String eoId);

    /**
     * eoPrimaryRouterValidate-验证是否执行作业主工艺路线
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/7
     */
    String eoPrimaryRouterValidate(Long tenantId, MtRouterVO8 dto);

    /**
     * eoPrimaryRouterValidate-验证是否执行作业主工艺路线
     *
     * @param tenantId
     * @param dtoList
     * @return Map<String,String>
     * @author penglin.sui
     * @date 2020/10/29
     */
    Map<String,String> eoBatchPrimaryRouterValidate(Long tenantId, List<MtRouterVO8> dtoList);

    /**
     * routerCut-工艺路线裁剪
     *
     * @author benjamin
     * @date 2019-07-03 09:43
     * @param tenantId IRequest
     * @param cutVO 工艺路线裁剪传入参数VO
     * @return String
     */
    String routerCut(Long tenantId, MtRouterVO9 cutVO);

    /**
     * propertyLimitRouterPropertyQuery-根据属性获取工艺路线信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterVO12> propertyLimitRouterPropertyQuery(Long tenantId, MtRouterVO11 dto);

    /**
     * routerAttrPropertyUpdate-工艺路线新增&更新扩展表属性
     *
     * @Author peng.yuan
     * @Date 2019/11/12 9:59
     * @return tarzan.method.domain.vo.MtRouterVO13
     */
    void routerAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * eoPrimaryRouterBatchValidate-批量验证是否执行作业主工艺路线
     *
     * @author benjamin
     * @date 2020/11/4 10:49 AM
     * @param tenantId 租户Id
     * @param dtoList 校验数据列表
     * @return List
     */
    List<MtRouterVO21> eoPrimaryRouterBatchValidate(Long tenantId, List<MtRouterVO8> dtoList);

    /**
     * routerCurrentVersionQuery-基于工艺路线名称批量获取当前版本
     *
     * @param tenantId
     * @param routerNme
     * @return
     */
    List<MtRouter> routerCurrentVersionQuery(Long tenantId, List<String> routerNme);

}
