package com.example.websocket.chatroom;

import com.example.websocket.user.UserEntity;
import com.example.websocket.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public Optional<Long> getChatRoomId(
            Long senderId,
            Long recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoomEntity::getId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        Long chatId = createChatRoom(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });

    }
    private Long createChatRoom(Long senderId, Long recipientId) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        UserEntity recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                .sender(sender)
                .recipient(recipient)
                .chatMessages(new ArrayList<>())
                .build();

        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }
}
