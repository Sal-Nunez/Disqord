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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.cohort.disqord.annotations.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min=2, message="First Name must be at least 2 characters")
	@NonNull
	@NotNull
	private String firstName;
	
	@NotEmpty
	@Size(min=2, message="Last Name must be at least 2 characters")
	@NonNull
	@NotNull
	private String lastName;
	
	@NotEmpty
	@Size(min=5, message="User Name must be at least 5 characters")
	@NonNull
	@NotNull
	private String userName;
	
	@NotEmpty
	@Size(min=7, message="Email must be at least 7 characters")
	@NonNull
	@NotNull
	@Email
	private String email;
	
	@NotEmpty
    @NotNull
    @ValidPassword
    @JsonIgnore
    private String password;
	
	@Transient
	@NotEmpty(message="Confirm Password is required!")
	@Size(min=8, max=128, message="Confirm Password must be between 8 and 128 characters")
	private String confirm;
	
	private String aboutMe;
	
	private String profilePic;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="user_chat_rooms",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="chat_room_id"))
	private List<ChatRoom> chatRooms;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ChatRoom> chatRoomsIOwn;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="friendships",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="friend_id"))
	private List<User> friends;
	
	@JsonIgnore
	@OneToMany(mappedBy="owner", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Server> servers;
	
	@JsonIgnore
	@OneToMany(mappedBy="serverMember", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ServerMember> serverMembers;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user_id", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ChannelMessage> channelMessages;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user_id", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ChatMessage> chatMessages;
	
	
	
	
	@JsonIgnore
	@Column(updatable=false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createdAt;
	
	@JsonIgnore
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
    
    public String getFullName() {
    	return this.firstName + " " + this.lastName;
    }
    
    
}
