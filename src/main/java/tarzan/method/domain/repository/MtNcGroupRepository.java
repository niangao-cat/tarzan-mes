package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtNcGroup;
import tarzan.method.domain.vo.MtNcGroupVO;
import tarzan.method.domain.vo.MtNcGroupVO1;

/**
 * 不良代码组资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcGroupRepository extends BaseRepository<MtNcGroup>, AopProxy<MtNcGroupRepository> {

    /**
     * ncGroupPropertyGet-获取不良代码组基础属性
     *
     * @param tenantId
     * @param ncGroupId
     * @return
     * @author guichuan.li
     * @date 2019/4/1
     */
    MtNcGroup ncGroupPropertyGet(Long tenantId, String ncGroupId);

    /**
     * 根据属性获取不良代码组信息
     * 
     * @Author peng.yuan
     * @Date 2019/10/17 17:47
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.method.domain.vo.MtNcGroupVO1>
     */
    List<MtNcGroupVO1> propertyLimitNcGroupPropertyQuery(Long tenantId, MtNcGroupVO dto);

    /**
     * 批量获取不良代码组基础属性
     * 
     * @Author peng.yuan
     * @Date 2019/10/23 17:29
     * @param tenantId :
     * @param ncGroupList :
     * @return java.util.List<tarzan.method.domain.entity.MtNcGroup>
     */
    List<MtNcGroup> ncGroupPropertyBatchGet(Long tenantId, List<String> ncGroupList);

    /**
     * 不良组新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/20 11:36
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void ncGroupAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

}
