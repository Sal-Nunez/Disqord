package com.cohort.disqord.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="channels")
@NoArgsConstructor
@RequiredArgsConstructor
public class Channel {
	
// Member Var.s ================================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@NonNull
	@Pattern(regexp="^[a-zA-Z0-9-]{2,20}$", message="Letters and numbers only. 2-20 characters.")
	private String name;
	
	private int channelLevel;
	
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

// 	Relationships ===============================================
	
	// a server can have many channels
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "server_id")
	private Server server;
	
	// one chat room can have many messages
	@OneToMany(mappedBy = "channel_id", fetch = FetchType.LAZY)
	private List<ChannelMessage> channelMessages;

	// a category can have many channels
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	
}
