package nextstep.subway.line.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
	private LineRepository lineRepository;

	public LineService(LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	public LineResponse saveLine(LineRequest request) {
		Line persistLine = lineRepository.save(request.toLine());
		return LineResponse.of(persistLine);
	}

	@Transactional(readOnly = true)
	public List<LineResponse> findAllLines() {
		List<Line> lines = lineRepository.findAll();
		return lines.stream()
			.map(line -> LineResponse.of(line))
			.collect(Collectors.toList());
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public LineResponse findLineById(Long id) {
		return LineResponse.of(getLine(id));
	}

	public void updateLine(Long id, LineRequest lineRequest) {
		Line findLine = getLine(id);
		findLine.update(lineRequest.toLine());
	}

	private Line getLine(Long id) {
		return lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("없는 노선입니다."));
	}
}
