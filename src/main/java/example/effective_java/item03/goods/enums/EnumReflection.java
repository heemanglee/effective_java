package example.effective_java.item03.goods.enums;

import java.lang.reflect.Constructor;

public class EnumReflection {
    public static void main(String[] args) {
        try {
            Constructor<Elvis> declaredConstructor = Elvis.class.getDeclaredConstructor();
            System.out.println(declaredConstructor);
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
