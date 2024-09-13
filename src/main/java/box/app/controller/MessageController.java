package box.app.controller;

import box.app.dto.message.MessageDto;
import box.app.dto.message.MessageRequestDto;
import box.app.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Message management", description = "Endpoints for messages managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new message", description = "Create a new message")
    public MessageDto createMessage(@RequestBody @Valid MessageRequestDto requestDto) {
        return messageService.add(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all messages", description = "Get a list of all messages")
    public List<MessageDto> getAllMessages(Pageable pageable) {
        return messageService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get the message by ID", description = "Get the message by ID")
    public MessageDto getMessageById(@PathVariable Long id) {
        return messageService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the message by ID", description = "Delete the message by ID")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteById(id);
    }
}
