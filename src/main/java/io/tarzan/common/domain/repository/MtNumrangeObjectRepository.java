package io.tarzan.common.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO1;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO2;

/**
 * 编码对象属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeObjectRepository
                extends BaseRepository<MtNumrangeObject>, AopProxy<MtNumrangeObjectRepository> {
    /**
     * propertyLimitNumrangeObjectQuery-根据编码对象属性获取对象ID
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/19
     */
    List<String> propertyLimitNumrangeObjectQuery(Long tenantId, MtNumrangeObject dto);

    /**
     * numrangeObjectGet-获取编码对象属性
     *
     * @param tenantId
     * @param objectId
     * @author guichuan.li
     * @date 2019/6/19
     */
    MtNumrangeObject numrangeObjectGet(Long tenantId, String objectId);

    /**
     * 根据属性获取编码对象信息
     * @Author peng.yuan
     * @Date 2019/10/17 10:27
     * @param tenantId : 
     * @param dto :
     * @return java.util.List<io.tarzan.common.domain.vo.MtNumrangeObjectVO2>
     */
    List<MtNumrangeObjectVO2> propertyLimitNumrangeObjectPropertyQuery (Long tenantId, MtNumrangeObjectVO1 dto);

}
