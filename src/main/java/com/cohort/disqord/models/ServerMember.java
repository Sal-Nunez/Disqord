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

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="server_members")
@NoArgsConstructor
public class ServerMember {
	
//  Member Var.s ================================================

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private int serverLevel;
	
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
	
	// a user can be a member of many servers
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private User serverMember;
	
	// a server can have many members
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "server_id")
	private Server server;
	
	// one server member can have many roles
	@OneToMany(mappedBy = "serverMember", fetch = FetchType.LAZY)
	private List<Role> roles;

}
