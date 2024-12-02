package example.effective_java.item02.bads.bad2;

public class Main {
    public static void main(String[] args) {
        final int servingSize = 240;
        final int servings = 8;

        // 자바빈즈 패턴의 문제점
        // 1. 객체를 생성하기 위해 메서드(setter)를 여러 개 호출해야한다.
        // 2. 객체의 불완전한 상태로 사용될 수 있다.
        NutritionFacts coke = new NutritionFacts();
        coke.setServingSize(servingSize);
        coke.setServings(servings);
        coke.setCalories(100);
        coke.setSodium(35);
        coke.setCarbohydrate(27);
    }
}
