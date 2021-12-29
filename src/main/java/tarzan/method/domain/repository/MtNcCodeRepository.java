package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.vo.MtNcCodeVO;
import tarzan.method.domain.vo.MtNcCodeVO1;

/**
 * 不良代码数据资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcCodeRepository extends BaseRepository<MtNcCode>, AopProxy<MtNcCodeRepository> {

    /**
     * ncCodeLimitNcGroupGet-根据代码获取所属不良代码组
     *
     * @param tenantId
     * @param ncCodeId
     * @return
     * @author guichuan.li
     * @date 2019/4/1
     */
    String ncCodeLimitNcGroupGet(Long tenantId, String ncCodeId);

    /**
     * propertyLimitNcCodeQuery-根据属性查询不良代码
     * 
     * @author benjamin
     * @date 2019-07-03 18:07
     * @param tenantId tenantId
     * @param mtNcCode MtNcCode
     * @return List
     */
    List<MtNcCode> propertyLimitNcCodeQuery(Long tenantId, MtNcCode mtNcCode);

    /**
     * ncCodePropertyGet -获取不良代码基础属性
     *
     * @param tenantId
     * @param ncCodeId
     * @return
     * @author lxs
     * @date 2019/4/1
     */
    MtNcCode ncCodePropertyGet(Long tenantId, String ncCodeId);

    /**
     * ncCodeGet -获取不良代码（组）基础属性
     *
     * @param tenantId
     * @param ncCodeId
     * @return
     * @author lxs
     * @date 2019/4/1
     */

    MtNcCode ncCodeGet(Long tenantId, String ncCodeId);

    /**
     * ncCodeGroupMemberQuery-获取不良代码组内已分配代码清单
     *
     * @param tenantId
     * @param ncGroupId
     * @return
     * @author guichuan.li
     * @date 2019/4/1
     */
    List<MtNcCode> ncCodeGroupMemberQuery(Long tenantId, String ncGroupId);


    /**
     * ncCodeClosedRequiredValidate -验证不良代码是否需要关闭
     *
     * @param tenantId
     * @param ncCodeId
     * @author lxs
     * @date 2019/4/1
     * @return
     */
    String ncCodeClosedRequiredValidate(Long tenantId, String ncCodeId);

    /**
     * ncCodeComponentRequiredValidate -验证不良代码是否需要记录组件
     *
     * @param tenantId
     * @param ncCodeId
     * @author lxs
     * @date 2019/4/1
     * @return
     */
    String ncCodeComponentRequiredValidate(Long tenantId, String ncCodeId);

    /**
     * ncCodeAllowNoDispositionValidate-验证不良代码是否允许无处置
     *
     * @param tenantId
     * @param ncCodeId
     * @author lxs
     * @date 2019/4/1
     * @return
     */
    String ncCodeAllowNoDispositionValidate(Long tenantId, String ncCodeId);


    /**
     * ncCodeEnableValidate-验证不良代码有效性
     *
     * @author chuang.yang
     * @date 2019/4/1
     * @param tenantId
     * @param ncCodeId
     * @return java.lang.String
     */
    String ncCodeEnableValidate(Long tenantId, String ncCodeId);

    /**
     * ncCodeConfirmRequiredValidate-验证不良代码是否需要复核
     *
     * @author chuang.yang
     * @date 2019/4/1
     * @param tenantId
     * @param ncCodeId
     * @return java.lang.String
     */
    String ncCodeConfirmRequiredValidate(Long tenantId, String ncCodeId);

    /**
     * ncCodeIncidentAutoCloseValidate-验证是否需要自动关闭事故
     *
     * @author chuang.yang
     * @date 2019/4/1
     * @param tenantId
     * @param ncCodeId
     * @return java.lang.String
     */
    String ncCodeIncidentAutoCloseValidate(Long tenantId, String ncCodeId);

    /**
     * ncCodePrimaryCodeAutoCloseValidate-验证是否需要自动关闭主代码
     *
     * @author chuang.yang
     * @date 2019/4/1
     * @param tenantId
     * @param ncCodeId
     * @return java.lang.String
     */
    String ncCodePrimaryCodeAutoCloseValidate(Long tenantId, String ncCodeId);

    /**
     * ncCodeSecondaryCodeRequiredValidate-验证不良代码关闭是否需要次级代码
     *
     * @author chuang.yang
     * @date 2019/4/1
     * @param tenantId
     * @param ncCodeId
     * @return java.lang.String
     */
    String ncCodeSecondaryCodeRequiredValidate(Long tenantId, String ncCodeId);

    /**
     * ncCodeValidAtAllOperationValidate-验证不良代码是否对所有工艺有效
     *
     * @author chuang.yang
     * @date 2019/4/1
     * @param tenantId
     * @param ncCodeId
     * @return java.lang.String
     */
    String ncCodeValidAtAllOperationValidate(Long tenantId, String ncCodeId);

    /**
     * 根据属性获取不良代码信息
     * 
     * @Author peng.yuan
     * @Date 2019/10/17 15:03
     * @return java.util.List<tarzan.method.domain.vo.MtNcCodeVO1>
     */
    List<MtNcCodeVO1> propertyLimitNcCodePropertyQuery(Long tenantId, MtNcCodeVO dto);

    /**
     * 根据NcCode批量查询不良代码
     * 
     * @return List
     */
    List<MtNcCode> ncCodeByMcCodeQuery(Long tenantId, List<String> mtNcCode);

    /**
     * 不良代码新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/20 11:15
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void ncCodeAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * ncCodeBatchGet -批量获取不良代码（组）基础属性
     */

    List<MtNcCode> ncCodeBatchGet(Long tenantId, List<String> ncCodeIds);
}
