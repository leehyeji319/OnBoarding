package com.estgames.db.entiity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item_file_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class ItemFileInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_file_info_id")
	private long id;

	@Column(name = "save_folder_path")
	private String saveFolderPath;

	@Column(name = "original_file")
	private String originalFile;

	@Column(name = "save_file")
	private String saveFile;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Item item;

	public void updateItemFileInfo(String originalFile, String saveFile, String saveFolderPath) {
		this.originalFile = originalFile;
		this.saveFile = saveFile;
		this.saveFolderPath = saveFolderPath;
	}
}
