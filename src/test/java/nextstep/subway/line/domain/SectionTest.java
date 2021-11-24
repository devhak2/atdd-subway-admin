package nextstep.subway.line.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.line.exception.DuplicationStationException;
import nextstep.subway.station.domain.Station;

public class SectionTest {

    @Test
    void 동일한_상행_하행_입력시_예외() {
        //given
        Station gangNam = new Station(1L, "강남역");

        // then
        Assertions.assertThatThrownBy(() -> {
                      Section section = new Section(gangNam, gangNam);
                  }).isInstanceOf(DuplicationStationException.class)
                  .hasMessage("상행선과 하행선은 동일할 수 없습니다.");

    }
}