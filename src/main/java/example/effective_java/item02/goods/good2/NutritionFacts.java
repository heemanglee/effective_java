package example.effective_java.item02.goods.good2;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

// 3. 빌더 패턴 (Lombok)
// @Builder의 경우 모든 파라미터를 받는 생성자를 자동으로 생성하기 때문에,
// 외부에어 해당 생성자를 사용하지 못하도록 하려면 생성자에 접근 가능한 레벨을 private으로 설정해야 한다.
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;
}
