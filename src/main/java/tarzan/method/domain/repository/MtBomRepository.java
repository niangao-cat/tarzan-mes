package tarzan.method.domain.repository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.vo.*;

import java.util.List;

/**
 * 装配清单头资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomRepository extends BaseRepository<MtBom>, AopProxy<MtBomRepository> {

    /**
     * bomBasicGet-获取装配清单基础属性
     *
     * @param tenantId
     * @param bomId
     * @return
     */
    MtBomVO7 bomBasicGet(Long tenantId, String bomId);

    /**
     * bomBasicBatchGet-批量获取装配清单基础属性
     *
     * @param tenantId
     * @param bomIds
     * @return
     */
    List<MtBomVO7> bomBasicBatchGet(Long tenantId, List<String> bomIds);

    /**
     * propertyLimitBomQuery-根据装配清单属性获取装配清单
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitBomQuery(Long tenantId, MtBomVO condition);

    /**
     * bomCopy-复制装配清单
     *
     * @param tenantId
     * @param condition
     * @return
     */
    String bomCopy(Long tenantId, MtBomVO2 condition);

    /**
     * bomAvailableVerify-校验装配清单可用性
     *
     * @param tenantId
     * @param condition
     * @return
     */
    void bomAvailableVerify(Long tenantId, MtBomVO3 condition);

    /**
     * bomUpdateVerify-校验装配清单可修改性
     *
     * @param tenantId
     * @param bomId
     * @return
     */
    void bomUpdateVerify(Long tenantId, String bomId);

    /**
     * bomStatusUpdate-更新装配清单状态
     *
     * @param tenantId
     * @param bomId
     * @param status
     */
    void bomStatusUpdate(Long tenantId, String bomId, String status);

    /**
     * bomCurrentFlagUpdate-更新装配清单当前版本标识
     *
     * @param tenantId
     * @param bomId
     */
    void bomCurrentFlagUpdate(Long tenantId, String bomId);

    /**
     * bomReleasedFlagUpdate更新装配清单关联EO下达标识
     *
     * @param tenantId
     * @param bomId
     * @param releasedFlag
     */
    void bomReleasedFlagUpdate(Long tenantId, String bomId, String releasedFlag);

    /**
     * bomAutoRevisionGet-获取装配清单是否自动升版本策略
     *
     * @param tenantId
     * @param bomId
     * @return
     */
    String bomAutoRevisionGet(Long tenantId, String bomId);

    /**
     * bomRevisionGenerate-生成装配清单版本号
     *
     * @param tenantId
     * @param bomVO4
     * @return
     */
    String bomRevisionGenerate(Long tenantId, MtBomVO4 bomVO4);

    /**
     * bomPropertyUpdate-新增更新装配清单基础属性
     * 
     * update remarks
     * <ul>
     * <li>2019-3-21 lxs 名称修改 bomUpdate -> bomPropertyUpdate</li>
     * <li>2019-7-22 benjamin 修改更新逻辑 输入字段为null或空字符则不更新该字段</li>
     * <li>2019-7-25 benjamin 修改更新逻辑 修改对BomName和BomType更新时的判断</li>
     * </ul>
     * 
     * @param tenantId tenantId
     * @param dto MtBom
     * @return 处理的BomId
     */
    String bomPropertyUpdate(Long tenantId, MtBom dto,String fullUpdate);

    /**
     * sourceBomLimitTargetBomUpdate-根据来源BOM更新目标BOM
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String sourceBomLimitTargetBomUpdate(Long tenantId, MtBomVO5 dto);

    /**
     * bomAllUpdate-新增更新整体装配清单
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String bomAllUpdate(Long tenantId, MtBomVO6 dto);


    /**
     * bomLimitSourceBomGet-获取复制来源BOM
     *
     * @Author peng.yuan
     * @Date 2019/11/11 14:12
     * @param tenantId :
     * @param bomId :
     * @return java.lang.String
     */
    String bomLimitSourceBomGet(Long tenantId, String bomId);

    /**
     * bomAttrPropertyUpdate-装配清单新增&更新扩展表属性
     *
     * @author chuang.yang
     * @date 2019/11/13
     * @param tenantId
     * @param dto
     * @return void
     */
    void bomAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * 根据bomName批量获取bom
     *
     * @param tenantId
     * @param bomNames
     * @return
     */
    List<MtBom> bomLimitBomNameQuery(Long tenantId, List<String> bomNames);

    /**
     * 根据bomId批量获取bom
     *
     * @param tenantId
     * @param bomIds
     * @return
     */
    List<MtBom> bomLimitBomIdQuery(Long tenantId, List<String> bomIds);

    /**
     * bomAllBatchUpdate-批量新增更新装配清单
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/05
     */
    List<String> bomAllBatchUpdate(Long tenantId, List<MtBomVO11> dto);

    /**
     * bomAllBatchUpdate-批量新增更新装配清单
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/05
     */
    List<String> myBomAllBatchUpdate(Long tenantId, List<MtBomVO11> dto);
}
