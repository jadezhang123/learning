package generic;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Zhang Junwei on 2017/2/23 0023.
 */
public class FieldGeneric<T> {
    public List<String> stringList;

    public T data;

    public static void main(String[] args) throws Exception {
        FieldGeneric<Integer> generic = new FieldGeneric<Integer>();
        Field data = generic.getClass().getField("data");
        Type genericFieldType = data.getGenericType();

        if(genericFieldType instanceof ParameterizedType){
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            for(Type fieldArgType : fieldArgTypes){
                Class fieldArgClass = (Class) fieldArgType;
                System.out.println("fieldArgClass = " + fieldArgClass);
            }
        }
    }
}
