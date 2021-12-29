package tarzan.stocktake.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.stocktake.domain.entity.MtStocktakeDoc;
import tarzan.stocktake.domain.vo.MtStocktakeDocVO;

/**
 * 盘点单据头表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
public interface MtStocktakeDocMapper extends BaseMapper<MtStocktakeDoc> {

    /**
     * 输入属性获取对应的盘点单据
     *
     * @param dto
     * @return
     */
    List<String> selectStocktakeDocByStocktakeIdList(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtStocktakeDocVO dto);

    /**
     * select by id list
     *
     * @author benjamin
     * @date 2019-06-21 10:10
     * @param stocktakeIdList Id List
     * @return List
     */
    List<MtStocktakeDoc> selectByIdList(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "stocktakeIdList") List<String> stocktakeIdList);

    /**
     * select by unique fields
     *
     * condition: num or identification
     *
     * @author benjamin
     * @date 2019-07-18 11:21
     * @param stocktakeDoc MtStocktakeDoc
     * @return List
     */
    List<MtStocktakeDoc> selectByUnique(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "stocktakeDoc") MtStocktakeDoc stocktakeDoc);
}
