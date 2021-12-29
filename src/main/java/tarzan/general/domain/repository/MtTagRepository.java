package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.vo.*;

/**
 * 数据收集项表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagRepository extends BaseRepository<MtTag>, AopProxy<MtTagRepository> {
    /**
     * tagGet-获取数据项属性
     *
     * @param tenantId
     * @param tagId
     * @author guichuan.li
     * @date 2019/7/02
     */
    MtTag tagGet(Long tenantId, String tagId);

    /**
     * tagCodeAndGroupCodeLimitTagGroupAssignGet-根据组编码和项编码获取数据项组分配行唯一标识
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/7/02
     */
    List<String> tagCodeAndGroupCodeLimitTagGroupAssignGet(Long tenantId, MtTagVO dto);

    /**
     * propertyLimitTagQuery-根据属性获取收集项
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/8/13
     */
    List<String> propertyLimitTagQuery(Long tenantId, MtTag dto);

    /**
     * edginkMessageAnalysis-益擎消息解析
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/7/02
     */
    MtMqttMessageVO3 edginkMessageAnalysis(Long tenantId, MtMqttMessageVO1 dto);

    /**
     * edginkMessageAnalysisAndRecordProcess-益擎消息解析并生成数据实绩
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/7/02
     */
    List<String> edginkMessageAnalysisAndRecordProcess(Long tenantId, MtMqttMessageVO1 dto);

    /**
     * tagBatchUpdate-数据项批量更新
     *
     * @Author peng.yuan
     * @Date 2019/9/18 15:27
     * @param tenantId :
     * @param fullUpdate : 全量值
     * @return java.lang.Long
     */
    Long tagBatchUpdate(Long tenantId, List<MtTagVO2> mtTagVO2List, String fullUpdate);

    /**
     * 根据属性获取数据项信息
     *
     * @Author peng.yuan
     * @Date 2019/10/17 11:20
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.general.domain.vo.MtTagVO4>
     */
    List<MtTagVO4> propertyLimitTagPropertyQuery(Long tenantId, MtTagVO3 dto);

    /**
     * 根据编码批量获取
     *
     * @Author peng.yuan
     * @Date 2019/11/18 16:24
     * @param tenantId :
     * @param tagCodeList :
     * @return java.util.List<tarzan.general.domain.entity.MtTag>
     */
    List<MtTag> selectByCodeList(Long tenantId, List<String> tagCodeList);
}
