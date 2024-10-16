package box.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import box.app.dto.message.MessageDto;
import box.app.dto.message.MessageRequestDto;
import box.app.exception.EntityNotFoundException;
import box.app.mapper.MessageMapper;
import box.app.model.message.Message;
import box.app.repository.MessageRepository;
import box.app.service.impl.MessageServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageServiceImpl messageService;

    private Message message;
    private MessageDto messageDto;
    private MessageRequestDto requestDto;

    @BeforeEach
    public void setup() {
        message = new Message();
        message.setId(1L);
        message.setName("Bob");
        message.setEmail("bob@example.com");
        message.setMessage("What do I need to do to start training ? ");

        messageDto = new MessageDto()
                .setId(message.getId())
                .setName(message.getName())
                .setEmail(message.getEmail())
                .setMessage(message.getMessage());
    }

    @Test
    @DisplayName("Verify the correct message was added")
    public void addMessage_WithValidMessageRequestDto_ReturnMessageDto() {
        requestDto = new MessageRequestDto()
                .setName(message.getName())
                .setEmail(message.getEmail())
                .setMessage(message.getMessage());

        when(messageRepository.save(message)).thenReturn(message);
        when(messageMapper.toDto(message)).thenReturn(messageDto);
        when(messageMapper.toModel(requestDto)).thenReturn(message);

        MessageDto actual = messageService.add(requestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(message.getId(), actual.getId());
    }

    @Test
    @DisplayName("Find all messages with pagination")
    void findAll_WithValidPageable_ReturnMessageList() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<Message> messagePage = new PageImpl<>(List.of(message), pageable, 1);

        when(messageRepository.findAll(pageable)).thenReturn((messagePage));
        List<MessageDto> messageList = messageService.findAll(pageable);

        assertEquals(1, messageList.size());
    }

    @Test
    @DisplayName("Verify the correct message was returned when message exists")
    public void getMessageById_WithValidId_ReturnMessageDto() {
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));
        when(messageMapper.toDto(message)).thenReturn(messageDto);

        MessageDto actual = messageService.getById(message.getId());

        Assertions.assertEquals(message.getId(), actual.getId());
    }

    @Test
    @DisplayName("Given incorrect id, check if returns exception")
    public void getMessageById_WithInvalidId_ThrowsEntityNotFoundException() {
        Long messageId = 100L;

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> messageService.getById(messageId)
        );

        String expected = "Can`t find message by id " + messageId;
        String actual = exception.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Given correct id, check if car is deleted")
    void deleteById_WithValidCarId_VerifyDeletion() {
        messageService.deleteById(message.getId());

        verify(messageRepository, times(1)).deleteById(message.getId());
    }
}
