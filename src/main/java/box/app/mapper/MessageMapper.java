package box.app.mapper;

import box.app.config.MapperConfig;
import box.app.dto.message.MessageDto;
import box.app.dto.message.MessageRequestDto;
import box.app.model.message.Message;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface MessageMapper {
    MessageDto toDto(Message message);

    Message toModel(MessageRequestDto requestDto);
}
