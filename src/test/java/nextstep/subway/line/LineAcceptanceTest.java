package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.utils.HttpTestStatusCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "GREEN");

        // then
        // 지하철_노선_생성됨
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        // 지하철_노선_등록되어_있음
        String lineName = "2호선";
        String lineColor = "GREEN";
        지하철_노선_생성_요청(lineName, lineColor);

        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineName, lineColor);

        // then
        // 지하철_노선_생성_실패됨
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        List<String> createdUrls = Arrays.asList(
                지하철_노선_등록되어_있음("2호선", "GREEN"),
                지하철_노선_등록되어_있음("1호선", "BLUE")
        );

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(createdUrls, response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        String createdUrl = 지하철_노선_등록되어_있음("2호선", "GREEN");

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createdUrl);

        // then
        // 지하철_노선_응답됨
        지하철_노선_응답됨(createdUrl, response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        String createdUrl = 지하철_노선_등록되어_있음("2호선", "GREEN");

        // when
        // 지하철_노선_수정_요청
        String expectedName = "1호선";
        String expectedColor = "BLUE";
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createdUrl, expectedName, expectedColor);

        // then
        // 지하철_노선_수정됨
        지하철_노선_수정됨(expectedName, expectedColor, response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        String createdUrl = 지하철_노선_등록되어_있음("1호선", "BLUE");

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createdUrl);

        // then
        // 지하철_노선_삭제됨
        지하철_노선_삭제됨(response);
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        컨텐츠_생성됨(response);
    }

    private void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        서버_내부_에러(response);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        요청_완료(response);
    }

    private void 지하철_노선_목록_포함됨(List<String> requestedUrls, ExtractableResponse<Response> response) {
        List<Long> expectedLineIds = requestedUrls.stream()
                .map(it -> Long.parseLong(it.split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private String 지하철_노선_등록되어_있음(String name, String color) {
        return 지하철_노선_생성_요청(name, color).header("Location");
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return RestAssured
                .given().log().all()
                .when().get(uri)
                .then().log().all().extract();
    }

    private void 지하철_노선_응답됨(String uri, ExtractableResponse<Response> response) {
        요청_완료(response);
        String expected = uri.split("/")[2];
        assertThat(String.valueOf((int) response.jsonPath().get("id"))).isEqualTo(expected);
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(String createdUrl, String name, String color) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LineRequest(name, color))
                .when().put(createdUrl)
                .then().log().all().extract();
    }

    private void 지하철_노선_수정됨(String expectedName, String expectedColor, ExtractableResponse<Response> response) {
        요청_완료(response);
        assertAll(
                () -> assertEquals(response.jsonPath().getString("name"), expectedName),
                () -> assertEquals(response.jsonPath().getString("color"), expectedColor)
        );
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(String createdUrl) {
        return RestAssured
                .given().log().all()
                .when().delete(createdUrl)
                .then().log().all().extract();
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        컨텐츠_없음(response);
    }
}
