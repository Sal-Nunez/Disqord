package com.cohort.disqord.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="servers")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Server {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@NonNull
	@NotEmpty
	@Size(min=5, message="Server Name must be at least 5 characters")
	private String name;

	private String banner;

	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="owner_id")
	private User owner;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="server", cascade=CascadeType.ALL)
	private List<Channel> channels;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="server", cascade=CascadeType.ALL)
	private List<Role> roles;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="server", cascade=CascadeType.ALL)
	private List<ServerMember> serverMembers;
	
	
	@Column(updatable=false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createdAt;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date updatedAt;
	
	
	
    //Dates
    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
}
