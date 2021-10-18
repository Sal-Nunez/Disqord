package com.cohort.disqord.models;


import java.text.SimpleDateFormat;
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

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="channel_messages")
@NoArgsConstructor
@RequiredArgsConstructor
public class ChannelMessage {
	
// Member Var.s ================================================

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NonNull
	@NotEmpty
	@NotNull
	@Column(columnDefinition="TEXT")
	String content;

	@Column(updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdAt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updatedAt;
	
	// auto update/create
	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

// Relationship ==========================================
	
	// channel can have many msgs
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;
	
	// user can create/send many msgs
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	
	public String getTime() {
		Date date = new Date();
		Date createdAt = this.createdAt;
		Long milliseconds = (date.getTime() - createdAt.getTime());
		Long days = milliseconds/(1000*60*60*24) % 365;
		SimpleDateFormat date_format = new SimpleDateFormat("hh:mm a");
		String date1 = date_format.format(createdAt);
		SimpleDateFormat date_format1 = new SimpleDateFormat("MM/dd/yyyy");
		String date2 = date_format1.format(createdAt);
		if (days == 0) {
			return "Today at " + date1;
		} else if (days == 1) {
			return "Yesterday at " + date1;
		} else {
			return date2;
		}
	}
	
	
	public String getDTime() {
		Date date = new Date();
		Date createdAt = this.createdAt;
		Long milliseconds = (date.getTime() - createdAt.getTime());
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
}
