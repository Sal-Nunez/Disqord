package com.cohort.disqord.models;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="chat_messages")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class ChatMessage {
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotEmpty
	@NonNull
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="chat_room")
	private ChatRoom chatRoom;
	
	private int user_id;
	private int chat_room_id;
	
	@Transient
	private String sender;
	
	
	@Column(updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdAt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updatedAt;
	
	
	
	public String getTime() {
		Date date = new Date();
		System.out.println(date);
		Date createdAt = this.createdAt;
		Long milliseconds = (date.getTime() - createdAt.getTime());
		System.out.println(milliseconds);
		Long seconds = milliseconds/1000 % 60;
		Long minutes = milliseconds/1000 / 60 % 60;
		Long hours = milliseconds/1000 / 60 / 60 % 24;
		Long days = milliseconds/(1000*60*60*24) % 365;
		if (days == 0) {
			if (hours == 0) {
				if (minutes == 0) {
					return (seconds + " seconds ago");
				} else {
					if (minutes == 1) {
						return (minutes + " minute ago");	
					} else {
						return (minutes + " minutes ago");							
					}
				}
			} else {
				if (hours == 1) {
					return (hours + " hour ago");												
				} else {
					return (hours + " hours ago");						
				}
			}
		} else {
			if (days == 1) {
				return (days + " day ago");																	
			} else {
				return (days + " days ago");					
			}
		}
	}
	
	
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}


	
	
	
}
