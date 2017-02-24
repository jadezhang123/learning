package own.jadezhang.common.domain.common;

/**
 * Created by Zhang Junwei on 2017/2/22 0022.
 */
public class SearchField extends BaseWrapper {
    private String field;
    private String op;
    private Object data;

    public SearchField(String field, String op, Object data) {
        this.field = field;
        this.op = op;
        this.data = data;
    }

    public SearchField() {
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOp() {
        return this.op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

