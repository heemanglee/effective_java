package example.effective_java.item03.bads.constructor;

import java.lang.reflect.Constructor;

// 생성자 방식
public class ElvisReflection {

    public static void main(String[] args) {
        try {
            // Reflection API 사용
            // getConstructor() : public 생성자에만 접근 가능
            /* getDeclaredConstructor() : 접근 지시자 상관없이 선언된 생성자에 접근 가능
            * () : 파라미터가 없는 기본 생성자, (args1, args2, ...) 생성자 호출 가능
            */
            Constructor<Elvis> defaultConstructor = Elvis.class.getDeclaredConstructor();

            // Java Reflection API를 사용하여 private 접근 지시자를 무시하고 해당 생성자에 접근을 허용한다.
            defaultConstructor.setAccessible(true);

            // 생성자를 호출할 때마다 각기 다른 인스턴스가 생성된다.
            Elvis elvis1 = defaultConstructor.newInstance();
            Elvis elvis2 = defaultConstructor.newInstance();

            System.out.println(elvis1 == elvis2); // false
            System.out.println(elvis1 == Elvis.INSTANCE); // false
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
