package example.effective_java.item03.goods.constructor;

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

            // Reflection API를 통해 인스턴스를 여러 개 생성하게되면 -> 싱글톤이 깨지기 때문에
            // 클래스 내부에 인스턴스 생성 여부를 나타내는 필드를 통해 인스턴스 생성을 제어할 수 있음.
            // elvis1 생성 시점에 예외가 발생한다.
            Elvis elvis1 = defaultConstructor.newInstance();
            Elvis elvis2 = defaultConstructor.newInstance();

            System.out.println(elvis1 == elvis2);
            System.out.println(elvis1 == Elvis.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
