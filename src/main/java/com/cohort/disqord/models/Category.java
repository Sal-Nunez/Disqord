package com.cohort.disqord.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Table(name= "categories")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NonNull
	@Size(min=5, message="Channel name must be at least 5 characters")
	@Pattern(regexp="^[a-zA-Z0-9-]{2,20}$", message="Letters and numbers only. 2-20 characters.")
	private String name;
	
	
// Relationships =========================
	@OneToMany(fetch=FetchType.LAZY, mappedBy="category", cascade=CascadeType.ALL)
	private List<Channel> channels;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="server_id")
	private Server server;
}
