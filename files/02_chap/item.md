## Bad1. 점층적 생성자 패턴

```java
// 1. 점층적 생성재 패턴
// 인스턴스를 생성하기 위해서, 원하는 매개변수를 모두 포함한 생성자를 호출하면 된다.
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```

```java
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
```

생성자를 통해 필수 값 삽입을 강제할 수 있으나, 선택적인 필드 삽입에 맞춰 여러 개의 생성자를 생성하게 된다.

생성자가 많은 것의 문제점으로는

- 생성자 파라미터가 동일한 타입이 여러 개가 존재하는 경우, 생성자에 값을 넘겨줄 때 어떤 값을 넘겨줘야 하는지 알 수 없다.
    - 예를 들어, NutritionFacts(int servings, int servingSize) 생성자의 경우 (int, int) 타입의 매개변수를 받고 있다. 클라이언트 코드에서 해당 생성자를 호출할 때, int 타입의 값을 넘겨야하는 것은 알고있지만, 어떤 값을 의미하는지는 알 수 없다.
    - 인텔리J의 경우 (command + p)를 통해 생성자 필드 이름을 확인할 수 있으나, 인텔리j를 쓰지 않는 경우 알 수 없다.

아래 클라이언트 코드를 확인해보자.

- (1) : (servingSize, servings)
- (2) : (servings, servingSize) 순서로 매개변수를 삽입한다.

생성자 파라미터의 타입이 같기 때문에 순서를 바꿔서 삽입해도 컴파일 에러가 발생하지 않는다. 컴파일 시점에 문제를 인지하지 못하기 때문에, 런타임 시점에 문제가 발생하게 된다.

```java
(1) NutritionFacts coke = new NutritionFacts(servingSize, servings, 100, 0, 35, 27);
(2) NutritionFacts coke = new NutritionFacts(servingSize, servings, 100, 0, 35, 27);
```

## Bad2. 자바빈즈 패턴 (세터 주입)

```java
// 2. 자바빈즈 패턴
// setter 메서드를 이용하여 인스턴스를 생성한다.
public class NutritionFacts {
    private int servingSize;
    private int servings;
    private int calories;
    private int fat;
    private int sodium;
    private int carbohydrate;

    public NutritionFacts() {
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }
}
```

```java
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
```

세터 주입의 문제점으로는

- 객체가 불완전한 상태로 사용이 가능하다. 객체 생성 시점에 필드를 주입하지 않기 때문이다.
- 객체를 사용하기 위해 필수로 삽입되어야 하는 값을 알 수 없다. 문서 또는 주석을 통해 확인해야 한다.

그렇다면 객체 생성에 필수적인 값은 생성자로 받고, 선택적인 값은 setter로 주입받으면 될까?

하나의 문제점이 존재하는데, setter를 통해 값을 수정할 수 있으므로 불변 객체를 만들 수 없다.

## Good1. Builder 클래스 사용

```java
// 3. 빌더 패턴
// 필수 매개변수만으로 Builder 객체를 생성하고, 선택적인 값들은 메서드를 통해 설정한다. (메서드 체이닝)
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    static class Builder {
        private final int servingSize;
        private final int servings;
        private int calories;
        private int fat;
        private int sodium;
        private int carbohydrate;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }
}
```

클래스 내부에 Builder 클래스를 직접 정의한다. 

빌더 클래스와 세터 주입의 차이점은

- 세터 주입은 void 타입을 반환한다.
- 빌더는 Builder(객체 자신) 타입을 반환한다.

따라서 빌더 패턴을 사용하는 경우 필드 삽입 결과로 자신을 반환하기 때문에, 메서드 체이닝이 가능하다.

```java
NutritionFacts coke = new NutritionFacts.Builder(servingSize, servings)
        .calories(100)
        .sodium(35)
        .carbohydrate(27)
        .build();
```

빌더 패턴의 장점으로는

- 빌더 생성자를 통해 필수적으로 삽입해야하는 값을 강제할 수 있다.
- 객체 생성 시에 선택적인 값들은 메서드 체이닝 형식으로 삽입할 수 있다.

빌더 패턴은 아래일 경우 사용

- 필수적인 값과 선택적인 값이 존재하다보니 생성자의 개수가 많아지는 경우 사용한다.
    - 빌더 객체 생성 시에 필수적인 값을 삽입하고, 메서드 체이닝으로 선택적인 값을 삽입하면 된다.
- 빌더 패턴을 사용하지 않을 경우
    - 점층적 생성자 패턴에서는 선택적 필드 삽입에 맞춘 여러 개의 생성자를 생성해야 한다.
    - 세터 주입 방식에서는 필수적인 값을 세터를 직접 호출하여 삽입해야 한다.

## Good2. Lombok에서 제공하는 @Builder 애너테이션

```java
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
```

- @Builder를 사용하면 클래스 내부에 모든 파라미터를 받는 생성자가 자동으로 생성된다.

![%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-12-01_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_7 36 11](https://github.com/user-attachments/assets/ea8c7882-0fe3-4a74-88d6-bf298b887080)


클라이언트 코드에서 생성자 호출을 막기 위해서 생성자 접근 레벨을 private으로 설정하면 된다.

```java
@AllArgsConstructor(access = AccessLevel.PRIVATE)
```

### @Builder는 파라미터 삽입을 강제할 수 없다.

빌더 객체를 생성할 때, 필수적으로 삽입해야하는 값(servings, servingSize)을 강제할 수 없다. 

**오류 코드**

```java
NutritionFacts coke = NutritionFacts.builder(servings, servingSize)
        .calories(100)
        .sodium(35)
        .carbohydrate(27)
        .build();
```

**정상 코드**

```java
NutritionFacts coke = NutritionFacts.builder()
        .servingSize(servingSize)
        .servings(servings)
        .calories(100)
        .sodium(35)
        .carbohydrate(27)
        .build();
```
