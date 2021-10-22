package com.cohort.disqord.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cohort.disqord.models.ChatRoomNotification;

@Repository
public interface ChatRoomNotificationRepo extends CrudRepository<ChatRoomNotification, Long> {

	
//	ChatRoomNotification getChatRoomNotificationWhereUserIdAndChatRoomId(Long userId, Long chatRoomId);
	
	@Query(value="SELECT * FROM chat_room_notifications WHERE user_id = ?1 AND chat_room_id = ?2",
			nativeQuery=true)
	ChatRoomNotification  getChatRoomNotificationWhereUserIdAndChatRoomId(Long userId, Long chatRoomId);
}
