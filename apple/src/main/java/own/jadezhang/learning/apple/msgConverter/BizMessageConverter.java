package own.jadezhang.learning.apple.msgConverter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * Created by Zhang Junwei on 2017/2/23 0023.
 */
public class BizMessageConverter<T> extends AbstractHttpMessageConverter<T>{
    @Override
    protected boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
