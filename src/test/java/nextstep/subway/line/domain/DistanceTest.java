package nextstep.subway.line.domain;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @Test
    @DisplayName("길이를 주어진 길이의 차로 수정한다.")
    void minus() {
        // given
        int distanceValue = 10;
        Distance distance = new Distance(distanceValue);
        int minusDistanceValue = 3;

        // when
        distance = distance.minus(new Distance(minusDistanceValue));

        // then
        assertThat(distance.getDistance()).isEqualTo(distanceValue - minusDistanceValue);
    }

    @Test
    @DisplayName("감소시킨 길이가 0보다 같거나 작으면 실패한다.")
    void minus_fail() {
        // given
        int distanceValue = 10;
        Distance distance = new Distance(distanceValue);
        int minusDistanceValue = 11;

        // when, then
        assertThatThrownBy(() -> distance.minus(new Distance(minusDistanceValue)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("구간의 길이는 0보다 커야합니다. (입력값: " + (distanceValue - minusDistanceValue) + ")");
    }
}