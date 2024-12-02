package example.effective_java.item02.goods.good1;

public class Main {
    public static void main(String[] args) {
        final int servingSize = 240;
        final int servings = 8;

        // 객체 생성 시에 필수 값들은 생성자를 통해 전달하고,
        // 선택적인 값들은 메서드를 통해 설정한다.
        NutritionFacts coke = new NutritionFacts.Builder(servingSize, servings)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();
    }
}
