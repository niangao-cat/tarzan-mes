package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtAssemblePointControl;
import tarzan.method.domain.vo.MtAssemblePointControlVO;
import tarzan.method.domain.vo.MtAssemblePointControlVO1;
import tarzan.method.domain.vo.MtAssemblePointControlVO2;
import tarzan.method.domain.vo.MtAssemblePointControlVO3;
import tarzan.method.domain.vo.MtAssemblePointControlVO4;
import tarzan.method.domain.vo.MtAssemblePointControlVO5;
import tarzan.method.domain.vo.MtAssemblePointControlVO6;
import tarzan.method.domain.vo.MtAssemblePointControlVO7;

/**
 * 装配点控制，指示具体装配控制下装配点可装载的物料资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssemblePointControlRepository
                extends BaseRepository<MtAssemblePointControl>, AopProxy<MtAssemblePointControlRepository> {

    /**
     * 根据装配点获取装配控制属性
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    MtAssemblePointControl assemblePointLimitAssembleControlPropertyGet(Long tenantId,
                                                                        MtAssemblePointControlVO condition);

    /**
     * 获取装配点物料单位用量
     *
     * @param tenantId
     * @param condition
     * @return
     */
    Double assemblePointMaterialUnitQtyGet(Long tenantId, MtAssemblePointControlVO condition);

    /**
     * 根据物料获取可使用装配点
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> materialLimitAvailableAssemblePointQuery(Long tenantId, MtAssemblePointControlVO1 condition);

    /**
     * 批量根据装配点获取装配控制属性
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtAssemblePointControl> assemblePointLimitAssembleControlPropertyBatchGet(Long tenantId,
                                                                                   MtAssemblePointControlVO2 condition);

    /**
     * 新增更新装配点控制
     *
     * @param tenantId
     * @param dto
     */
    String assemblePointControlUpdate(Long tenantId, MtAssemblePointControl dto);

    /**
     * 根据物料校验装配点可用性
     *
     * @param tenantId
     * @param condition
     */
    void materialLimitAvailableAssemblePointValidate(Long tenantId, MtAssemblePointControlVO3 condition);

    /**
     * 根据装配组获取装配控制属性
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtAssemblePointControlVO4> assembleGroupLimitAssembleControlPropertyQuery(Long tenantId,
                                                                                   MtAssemblePointControlVO5 condition);

    /**
     * 根据属性获取装配点控制信息
     *
     * @Author peng.yuan
     * @Date 2019/10/9 16:27
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.method.domain.vo.MtAssemblePointControlVO7>
     */
    List<MtAssemblePointControlVO7> propertyLimitAssemblePointControlPropertyQuery(Long tenantId,
                                                                                   MtAssemblePointControlVO6 dto);
}
