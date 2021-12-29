package io.tarzan.common.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtNumrange;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO3;
import io.tarzan.common.domain.vo.MtNumrangeVO4;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import io.tarzan.common.domain.vo.MtNumrangeVO9;

/**
 * 号码段定义表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeRepository extends BaseRepository<MtNumrange>, AopProxy<MtNumrangeRepository> {
    /**
     * numrangeGenerate-号码段编码生成
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/6/25
     */
    MtNumrangeVO5 numrangeGenerate(Long tenantId, MtNumrangeVO2 dto);

    /**
     * numrangePropertyQuery-号码段属性查询
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/6/25
     */
    MtNumrangeVO3 numrangePropertyQuery(Long tenantId, String numrangeId);

    /**
     * numrangeIdQuery-依据属性获取号码段主键
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/25
     */
    List<String> numrangeIdQuery(Long tenantId, MtNumrangeVO4 dto);

    /**
     * numrangeBatchGenerate-号码段编码批量生成
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/11/14
     */
    MtNumrangeVO8 numrangeBatchGenerate(Long tenantId, MtNumrangeVO9 dto);
}
