package tarzan.method.domain.repository;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtAssembleControl;
import tarzan.method.domain.vo.MtAssembleControlVO;
import tarzan.method.domain.vo.MtAssembleControlVO1;
import tarzan.method.domain.vo.MtAssembleControlVO2;

/**
 * 装配控制，定义一组装配控制要求，包括装配组可安装位置和装配点可装载物料资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssembleControlRepository
                extends BaseRepository<MtAssembleControl>, AopProxy<MtAssembleControlRepository> {

    /**
     * 装配点物料用量计算
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<Map<String, Object>> allAssemblePointMaterialQtyCalculate(Long tenantId, MtAssembleControlVO condition);

    /**
     * 新增更新装配控制
     * 
     * @param tenantId
     * @param dto
     * @Param fullUpdate
     * @return
     */
    String assembleControlUpdate(Long tenantId, MtAssembleControl dto, String fullUpdate);

    /**
     * 获取装配控制
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    String assembleControlGet(Long tenantId, MtAssembleControlVO1 condition);

    /**
     * 根据装配控制属性获取装配控制信息
     * @Author peng.yuan
     * @Date 2019/10/10 16:33
     * @param tenantId :
     * @param condition :
     * @return java.util.List<tarzan.method.domain.vo.MtAssembleControlVO2>
     */
    List<MtAssembleControlVO2> propertyLimitAssembleControlPropertyQuery(Long tenantId, MtAssembleControlVO2 condition);

}
