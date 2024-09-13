package box.app.service.impl;

import box.app.dto.message.MessageDto;
import box.app.dto.message.MessageRequestDto;
import box.app.exception.EntityNotFoundException;
import box.app.mapper.MessageMapper;
import box.app.model.message.Message;
import box.app.repository.MessageRepository;
import box.app.service.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageDto add(MessageRequestDto requestDto) {
        Message message = messageMapper.toModel(requestDto);
        return messageMapper.toDto(messageRepository.save(message));
    }

    @Override
    public List<MessageDto> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Override
    public MessageDto getById(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find message by id " + id));
        return messageMapper.toDto(message);
    }

    @Override
    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }
}
