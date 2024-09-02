package box.app.service;

import box.app.dto.coach.CoachDto;
import box.app.dto.coach.CoachRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CoachService {
    CoachDto add(CoachRequestDto requestDto);

    List<CoachDto> findAll(Pageable pageable);

    CoachDto getById(Long id);

    CoachDto updateById(Long id, CoachRequestDto requestDto);

    void deleteById(Long id);
}
