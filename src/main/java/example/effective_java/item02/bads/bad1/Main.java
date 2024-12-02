package example.effective_java.item02.bads.bad1;

public class Main {
    public static void main(String[] args) {
        final int servingSize = 240;
        final int servings = 8;

        // 매개변수 순서를 바꾸어 삽입하더라도,
        // 파라미터 타입에 맞춰 삽입했기 때문에 컴파일 에러가 발생하지 않는다.
        // 이는 런타임 시점에 문제가 발생할 수 있음을 의미한다.
        NutritionFacts coke = new NutritionFacts(servingSize, servings, 100, 0, 35, 27);
        NutritionFacts pepsi = new NutritionFacts(servings, servingSize, 100, 0, 35, 27);
    }
}
