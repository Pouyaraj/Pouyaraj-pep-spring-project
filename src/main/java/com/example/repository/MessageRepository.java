package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Message m WHERE m.messageId = :messageId")
    int deleteMessageById(Integer messageId);
    
    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.messageText = :newMessageText WHERE m.messageId = :messageId")
    int updateMessageText(Integer messageId, String newMessageText);

    @Query("SELECT m FROM Message m WHERE m.postedBy = :accountId")
    List<Message> findAllByPostedBy(Integer accountId);

}
