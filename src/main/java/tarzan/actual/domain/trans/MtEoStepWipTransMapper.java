package tarzan.actual.domain.trans;

import tarzan.actual.domain.vo.MtEoStepActualVO47;
import tarzan.actual.domain.vo.MtEoStepWipVO14;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 17:52
 * @Description:
 */
public interface MtEoStepWipTransMapper {

    /**
     * 构建历史
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param dto
     */
    MtEoStepWipVO14 eoStepActualVO44ToEoStepWipVO14(MtEoStepActualVO47 dto);
}
