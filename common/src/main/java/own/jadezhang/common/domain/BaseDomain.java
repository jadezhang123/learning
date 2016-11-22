package own.jadezhang.common.domain;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public class BaseDomain<T> implements Serializable {
    private static final long serialVersionUID = 6667600871541308622L;
    private T id;

    public T getId() {
        return this.id instanceof BigInteger ? (T) Long.valueOf(String.valueOf(this.id)) :this.id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
