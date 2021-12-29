package tarzan.material.domain.repository;

import java.util.List;

import io.tarzan.common.domain.vo.MtExtendVO10;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.vo.*;

/**
 * 物料基础属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialRepository extends BaseRepository<MtMaterial>, AopProxy<MtMaterialRepository> {

    /**
     * materialPropertyGet-获取物料基础属性
     *
     * @param tenantId
     * @param materialId
     * @return
     */
    MtMaterialVO materialPropertyGet(Long tenantId, String materialId);

    /**
     * nameLimitMaterialQuery-根据物料名称限制获取有效物料
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> nameLimitMaterialQuery(Long tenantId, MtMaterial dto);

    /**
     * uomLimitMaterialQuery-根据物料单位信息获取有效物料
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> uomLimitMaterialQuery(Long tenantId, MtMaterialVO dto);

    /**
     * propertyLimitMaterialQuery-根据基础属性限制获取物料
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitMaterialQuery(Long tenantId, MtMaterialVO dto);

    /**
     * materialDualUomValidate-验证物料是否启用双单位管控
     *
     * @param tenantId
     * @param materialId
     * @return
     */
    String materialDualUomValidate(Long tenantId, String materialId);

    /**
     * materialPropertyBatchGet-批量获取物料基础属性
     *
     * @param tenantId
     * @param materialIds
     * @return
     */
    List<MtMaterialVO> materialPropertyBatchGet(Long tenantId, List<String> materialIds);

    /**
     * materialUomGet-获取物料单位
     *
     * @param tenantId
     * @param materialId
     * @return
     */
    MtMaterialVO1 materialUomGet(Long tenantId, String materialId);


    /**
     * 批量获取物料单位
     *
     * @Author peng.yuan
     * @Date 2019/11/27 10:19
     * @param tenantId :
     * @param materialId :
     * @return java.util.List<tarzan.material.domain.vo.MtMaterialVO6>
     */
    List<MtMaterialVO1> materialUomBatchGet(Long tenantId, List<String> materialId);

    /**
     * materialUomTypeValidate-验证指定单位与物料单位类型一致性
     *
     * @param tenantId
     * @param dto
     * @return
     */
    void materialUomTypeValidate(Long tenantId, MtMaterialVO2 dto);

    /**
     * 根据物料编码获取物料属性
     *
     * @author benjamin
     * @date 2019-09-03 14:40
     * @param tenantId 租户Id
     * @param materialCodeList 物料编码集合
     * @return list
     */
    List<MtMaterial> queryMaterialByCode(Long tenantId, List<String> materialCodeList);

    /**
     * propertyLimitMaterialPropertyQuery-根据属性获取物料信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtMaterialVO5> propertyLimitMaterialPropertyQuery(Long tenantId, MtMaterialVO4 dto);

    /**
     * 自定义基础属性批量获取
     *
     * @Author peng.yuan
     * @Date 2019/11/8 17:53
     * @param tenantId :
     * @param materialIds :
     * @return java.util.List<tarzan.material.domain.vo.MtMaterialVO>
     */
    List<MtMaterialVO> materialBasicInfoBatchGet(Long tenantId, List<String> materialIds);

    /**
     * -物料新增&更新扩展表属性
     *
     * @Author peng.yuan
     * @Date 2019/11/19 10:16
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void materialAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);


}
