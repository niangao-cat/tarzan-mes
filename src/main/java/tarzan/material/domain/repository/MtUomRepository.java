package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.vo.*;

/**
 * 单位资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:10:08
 */
public interface MtUomRepository extends BaseRepository<MtUom>, AopProxy<MtUomRepository> {

    /**
     * uomPropertyGet-获取单位属性信息
     *
     * @param tenantId
     * @param uomId
     * @return
     */
    MtUomVO uomPropertyGet(Long tenantId, String uomId);

    /**
     * propertyLimitUomQuery-根据属性限制获取单位
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtUomVO> propertyLimitUomQuery(Long tenantId, MtUomVO dto);

    /**
     * uomDecimalProcess-根据单位小数位数处理单位值
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtUomVO1 uomDecimalProcess(Long tenantId, MtUomVO1 dto);

    /**
     * uomConversion-单位换算
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtUomVO1 uomConversion(Long tenantId, MtUomVO1 dto);

    /**
     * primaryUomGet-获取当前单位主单位
     *
     * @param tenantId
     * @param uomId
     * @return
     */
    MtUomVO2 primaryUomGet(Long tenantId, String uomId);

    /**
     * sameTypeUomQuery-获取同类别的单位
     *
     * @param tenantId
     * @param sourceUomId
     * @return
     */
    List<MtUom> sameTypeUomQuery(Long tenantId, String sourceUomId);

    /**
     * materialUomConversion-物料主辅单位换算
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtUomVO3 materialUomConversion(Long tenantId, MtUomVO3 dto);

    /**
     * uomPropertyBatchGet-批量获取单位属性信息
     *
     * @param uomIds
     * @return
     */
    List<MtUomVO> uomPropertyBatchGet(Long tenantId, List<String> uomIds);

    /**
     * propertyLimitUomPropertyQuery-根据属性获取单位信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtUomVO5> propertyLimitUomPropertyQuery(Long tenantId, MtUomVO4 dto);

    /**
     * 根据单位编码，批量获取单位信息
     *
     * @author chuang.yang
     * @date 2019/11/18
     * @param tenantId
     * @param uomCodes
     * @return java.util.List<tarzan.material.domain.entity.MtUom>
     */
    List<MtUom> uomPropertyBatchGetByCodes(Long tenantId, List<String> uomCodes);

    /**
     * uomAttrPropertyUpdate-单位新增&更新扩展表属性
     *
     * @author chuang.yang
     * @date 2019/11/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void uomAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * uomDecimalBatchProcess-根据单位小数位数批量处理单位值
     *
     * @author chuang.yang
     * @date 2021/4/22
     */
    List<MtUomVO8> uomDecimalBatchProcess(Long tenantId, List<MtUomVO7> uomList);
}
