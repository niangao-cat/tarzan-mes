package tarzan.method.domain.repository;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtAssemblePoint;
import tarzan.method.domain.vo.MtAssemblePointVO;
import tarzan.method.domain.vo.MtAssemblePointVO1;
import tarzan.method.domain.vo.MtAssemblePointVO10;
import tarzan.method.domain.vo.MtAssemblePointVO2;
import tarzan.method.domain.vo.MtAssemblePointVO3;
import tarzan.method.domain.vo.MtAssemblePointVO4;
import tarzan.method.domain.vo.MtAssemblePointVO5;
import tarzan.method.domain.vo.MtAssemblePointVO6;
import tarzan.method.domain.vo.MtAssemblePointVO7;
import tarzan.method.domain.vo.MtAssemblePointVO9;

/**
 * 装配点，标识具体装配组下具体的装配位置资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssemblePointRepository
                extends BaseRepository<MtAssemblePoint>, AopProxy<MtAssemblePointRepository> {

    /**
     * 根据属性获取装配点
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitAssemblePointQuery(Long tenantId, MtAssemblePoint dto);

    /**
     * 获取装配点属性
     * 
     * @param tenantId
     * @param assemblePointId
     * @return
     */
    MtAssemblePoint assemblePointPropertyGet(Long tenantId, String assemblePointId);

    /**
     * 批量获取装配点属性
     * 
     * @param tenantId
     * @param assemblePointIds
     * @return
     */
    List<MtAssemblePoint> assemblePointPropertyBatchGet(Long tenantId, List<String> assemblePointIds);

    /**
     * 根据装配组获取有效装配点
     * 
     * @param tenantId
     * @param assembleGroupId
     * @return
     */
    List<Map<String, String>> assembleGroupLimitEnableAssemblePointQuery(Long tenantId, String assembleGroupId);

    /**
     * 装配点有效性校验
     * 
     * @param tenantId
     * @param assemblePointId
     */
    void assemblePointIsEnabledValidate(Long tenantId, String assemblePointId);

    /**
     * 新增更新装配点
     * 
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return
     */
    String assemblePointUpdate(Long tenantId, MtAssemblePoint dto, String fullUpdate);

    /**
     * 装配点编码生成
     * 
     * @param tenantId
     * @param assembleGroupId
     */
    String assemblePointCodeGenerate(Long tenantId, String assembleGroupId);

    /**
     * 装配点序号生成
     * 
     * @param tenantId
     * @param assembleGroupId
     * @return
     */
    Long assemblePointNextSequenceGenerate(Long tenantId, String assembleGroupId);

    /**
     * 装配点自动创建
     * 
     * @param tenantId
     * @param assembleGroupId
     * @return
     */
    String assemblePointAutoCreate(Long tenantId, String assembleGroupId);

    /**
     * 按顺序获取装配组下装配点
     * 
     * @param tenantId
     * @param assembleGroupId
     * @param sortBy
     * @return
     */
    List<MtAssemblePointVO> assembleGroupLimitAssemblePointOrderBySequenceSort(Long tenantId, String assembleGroupId,
                                                                               String sortBy);

    /**
     * 装配点最大装载量校验
     * 
     * @param tenantId
     * @param condition
     */
    void assemblePointMaxQtyVerify(Long tenantId, MtAssemblePointVO1 condition);

    /**
     * 装配点物料安装验证/sen.luo 2018-03-21
     * 
     * @param tenantId
     */
    void assemblePointMaterialLoadVerify(Long tenantId, MtAssemblePointVO2 condition);

    /**
     * 装配组物料按序上料装配/sen.luo 2018-03-21
     * 
     * @param tenantId
     * @param dto
     */
    void assemblePointMaterialSequenceLoad(Long tenantId, MtAssemblePointVO3 dto);

    /**
     * 装配点物料消耗/sen.luo 2018-03-21
     * 
     * @param tenantId
     * @param dto
     */
    void assemblePointMaterialConsume(Long tenantId, MtAssemblePointVO4 dto);

    /**
     * 装配点物料装载/sen.luo 2018-03-21
     * 
     * @param tenantId
     * @param dto
     */
    void assemblePointMaterialLoad(Long tenantId, MtAssemblePointVO5 dto);

    /**
     * 装配点物料卸载/sen.luo 2018-03-21
     * 
     * @param tenantId
     * @param dto
     */
    void assemblePointMaterialUnload(Long tenantId, MtAssemblePointVO6 dto);

    /**
     * 获取装配组下已装载指定物料的第一个装配点/sen.luo 2018-03-22
     * 
     * @param tenantId
     * @param assembleGroupId
     * @param materialId
     * @return
     */
    String firstAvailableLoadingAssemblePointGet(Long tenantId, String assembleGroupId, String materialId);

    /**
     * 顺序控制装配组物料消耗/sen.luo 2018-03-22
     * 
     * @param tenantId
     * @param dto
     */
    void assemblePointMaterialSequenceConsume(Long tenantId, MtAssemblePointVO7 dto);

    /**
     * 根据属性获取装配点信息
     * 
     * @Author peng.yuan
     * @Date 2019/10/9 14:05
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.method.domain.vo.MtAssemblePointVO10>
     */
    List<MtAssemblePointVO10> propertyLimitAssemblePointPropertyQuery(Long tenantId, MtAssemblePointVO9 dto);

}
