package box.app.service;

import box.app.dto.message.MessageDto;
import box.app.dto.message.MessageRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    MessageDto add(MessageRequestDto requestDto);

    List<MessageDto> findAll(Pageable pageable);

    MessageDto getById(Long id);

    void deleteById(Long id);
}
