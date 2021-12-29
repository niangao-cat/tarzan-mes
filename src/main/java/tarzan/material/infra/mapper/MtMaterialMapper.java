package tarzan.material.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO4;
import tarzan.material.domain.vo.MtMaterialVO5;
import tarzan.material.domain.vo.MtMaterialVO6;

import java.util.List;

/**
 * 物料基础属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialMapper extends BaseMapper<MtMaterial> {

    List<MtMaterialVO> selectMaterialView(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "dto") MtMaterialVO dto);

    List<MtMaterial> selectMaterialViewForUi(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "dto") MtMaterialVO dto);

    List<MtMaterialVO> selectMaterialByIds(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "materialIds") List<String> materialIds);

    List<MtMaterial> queryMaterialByCode(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "materialCodeList") String materialCodeList);

    List<MtMaterialVO5> selectCondition(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "dto") MtMaterialVO4 dto);

    /**
     * 自定义基础属性批量获取
     *
     * @Author peng.yuan
     * @Date 2019/11/8 17:53
     * @param tenantId :
     * @param materialIds :
     * @return java.util.List<tarzan.material.domain.vo.MtMaterialVO>
     */
    List<MtMaterialVO> materialBasicInfoBatchGetByIdList(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "materialIds") String materialIds);

    void batchInsert(@Param("materials") List<MtMaterialVO6> materials);

    void batchInsertTl(@Param("materials") List<MtMaterialVO6> tempList);
}
