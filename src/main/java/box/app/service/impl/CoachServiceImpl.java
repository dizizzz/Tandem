package box.app.service.impl;

import box.app.dto.coach.CoachDto;
import box.app.dto.coach.CoachRequestDto;
import box.app.exception.EntityNotFoundException;
import box.app.mapper.CoachMapper;
import box.app.model.coach.Coach;
import box.app.repository.CoachRepository;
import box.app.service.CoachService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CoachServiceImpl implements CoachService {
    private final CoachRepository coachRepository;
    private final CoachMapper coachMapper;

    @Override
    public CoachDto add(CoachRequestDto requestDto) {
        Coach coach = coachMapper.toModel(requestDto);
        return coachMapper.toDto(coachRepository.save(coach));
    }

    @Override
    public List<CoachDto> findAll(Pageable pageable) {
        return coachRepository.findAll(pageable).stream()
                .map(coachMapper::toDto)
                .toList();
    }

    @Override
    public CoachDto getById(Long id) {
        Coach coach = coachRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find coach by id " + id));
        return coachMapper.toDto(coach);
    }

    @Override
    public CoachDto updateById(Long id, CoachRequestDto requestDto) {
        Coach coach = coachRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find coach by id " + id));
        Coach updatedCoach = coachMapper.toModel(requestDto);
        updatedCoach.setId(id);
        return coachMapper.toDto(coachRepository.save(updatedCoach));
    }

    @Override
    public void deleteById(Long id) {
        coachRepository.deleteById(id);
    }
}
