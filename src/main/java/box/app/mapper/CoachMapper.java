package box.app.mapper;

import box.app.config.MapperConfig;
import box.app.dto.coach.CoachDto;
import box.app.dto.coach.CoachRequestDto;
import box.app.model.coach.Coach;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CoachMapper {
    CoachDto toDto(Coach coach);

    Coach toModel(CoachRequestDto requestDto);
}
