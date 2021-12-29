package tarzan.actual.domain.trans;

import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtEoStepActualHis;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 17:52
 * @Description:
 */
public interface MtEoStepActualTransMapper {

    /**
     * 构建历史
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param dto
     */
    MtEoStepActualHis mtEoStepActualTransToHis(MtEoStepActual dto);
}
