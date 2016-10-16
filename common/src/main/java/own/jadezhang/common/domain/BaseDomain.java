package own.jadezhang.common.domain;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public class BaseDomain<T> {
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
