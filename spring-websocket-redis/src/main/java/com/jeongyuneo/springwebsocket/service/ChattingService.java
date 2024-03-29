package com.jeongyuneo.springwebsocket.service;

import com.jeongyuneo.springwebsocket.dto.ChattingRoomCreateRequest;
import com.jeongyuneo.springwebsocket.dto.Message;
import com.jeongyuneo.springwebsocket.entity.ChattingRoom;
import com.jeongyuneo.springwebsocket.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChattingService {

    private final RedisPublisher redisPublisher;
    private final RedisMessageListener redisMessageListener;
    private final ChattingRoomRepository chattingRoomRepository;

    public void send(Message message) {
        redisPublisher.publish(redisMessageListener.getTopic(message.getChattingRoomId()), message);
        chattingRoomRepository.saveChatting(message.getChattingRoomId(), message.toChatting());
    }

    public void createChattingRoom(ChattingRoomCreateRequest chattingRoomCreateRequest) {
        chattingRoomRepository.createChattingRoom(chattingRoomCreateRequest.getName());
    }

    public void enterChattingRoom(String chattingRoomId) {
        redisMessageListener.enterChattingRoom(chattingRoomId);
    }

    public List<ChattingRoom> getChattingRooms() {
        return chattingRoomRepository.findAllRoom();
    }

    public ChattingRoom getChattingRoom(String chattingRoomId) {
        return chattingRoomRepository.findById(chattingRoomId);
    }

    public List<Message> getChattings(String chattingRoomId) {
        log.info("find chattings in chatting room: {}", chattingRoomId);
        return chattingRoomRepository.findChattingByChattingRoomId(chattingRoomId)
                .stream()
                .map(chatting -> Message.builder()
                        .chattingRoomId(chatting.getId())
                        .senderId(chatting.getSenderId())
                        .content(chatting.getContent())
                        .build())
                .collect(Collectors.toList());
    }
}
