package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO1;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO2;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO3;
import tarzan.inventory.domain.vo.MtContainerTypeVO;
import tarzan.inventory.domain.vo.MtContainerTypeVO1;

/**
 * 容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerTypeRepository
                extends BaseRepository<MtContainerType>, AopProxy<MtContainerTypeRepository> {

    /**
     * containerTypePropertyGet-获取容器类型属性
     *
     * @param tenantId
     * @param containerTypeId
     * @author guichuan.li
     * @date 2019/4/3
     */
    MtContainerType containerTypePropertyGet(Long tenantId, String containerTypeId);

    /**
     * propertyLimitContainerTypeQuery-根据属性限制获取容器类型
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/8
     */
    List<String> propertyLimitContainerTypeQuery(Long tenantId, MtContainerType dto, String fuzzyQueryFlag);

    /**
     * containerTypePropertyUpdate-容器类型属性更新&新增
     * 
     * update remarks
     * <ul>
     * <li>2019-09-16 benjamin 修改唯一性校验报错</li>
     * </ul>
     * 
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/8
     */
    String containerTypePropertyUpdate(Long tenantId, MtContainerType dto, String fullUpdate);

    /**
     * containerTypeEnableValidate-验证容器类型有效性
     *
     * @param tenantId
     * @param containerTypeId
     * @author guichuan.li
     * @date 2019/4/8
     */
    void containerTypeEnableValidate(Long tenantId, String containerTypeId);

    /**
     * containerTypeLimitAttrQuery-获取容器类型拓展属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/17
     */
    List<MtExtendAttrVO> containerTypeLimitAttrQuery(Long tenantId, MtContainerTypeAttrVO1 dto);

    /**
     * attrLimitContainerTypeQuery-根据容器类型拓展属性获取容器类型
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/17
     */
    List<String> attrLimitContainerTypeQuery(Long tenantId, MtContainerTypeAttrVO2 dto);

    /**
     * containerTypeLimitAttrUpdate-新增&更新容器类型拓展属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/17
     */
    void containerTypeLimitAttrUpdate(Long tenantId, MtContainerTypeAttrVO3 dto);

    /**
     * 根据属性获取容器类型信息
     * 
     * @Author peng.yuan
     * @Date 2019/10/18 16:14
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.inventory.domain.vo.MtContainerTypeVO1>
     */
    List<MtContainerTypeVO1> propertyLimitContainerTypePropertyQuery(Long tenantId, MtContainerTypeVO dto);

    /**
     * 批量获取容器类型属性
     * 
     * @Author peng.yuan
     * @Date 2019/10/23 18:51
     * @param tenantId :
     * @param containerTypeIdList :
     * @return MtContainerType
     */
    List<MtContainerType> containerTypePropertyBatchGet(Long tenantId, List<String> containerTypeIdList);

    /**
     * 容器类型新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/18 10:20
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void containerTypeAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);
}
