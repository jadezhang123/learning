package own.jadezhang.common.domain.enums;

/**
 * Created by Zhang Junwei on 2017/2/22 0022.
 */
public enum MatchTypeEnum {
    /**
     * 精确匹配 content
     */
    EXACT,
    /**
     * 左匹配 模糊查询 %content
     */
    LEFT_FUZZY,
    /**
     * 右匹配 模糊查询 content%
     */
    RIGHT_FUZZY,
    /**
     * 模糊查询 %content%
     */
    ALL_FUZZY;
}
