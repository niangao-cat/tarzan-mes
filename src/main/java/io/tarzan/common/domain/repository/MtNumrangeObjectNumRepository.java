package io.tarzan.common.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtNumrangeObjectNum;

/**
 * 号码段按对象序列号记录表资源库
 *
 * @author MrZ 2019-08-22 21:38:58
 */
public interface MtNumrangeObjectNumRepository
                extends BaseRepository<MtNumrangeObjectNum>, AopProxy<MtNumrangeObjectNumRepository> {

}
