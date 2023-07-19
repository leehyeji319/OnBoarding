package com.estgames.db.entiity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banner_file_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class BannerFileInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "banner_file_info_id")
	private Long id;

	@Column(name = "item_id")
	private Long itemId;

	@Column(name = "save_folder_path")
	private String saveFolderPath;

	@Column(name = "original_file")
	private String originalFile;

	@Column(name = "save_file")
	private String saveFile;

	public void updateBannerFileInfo(String originalFile, String saveFile, String saveFolderPath) {
		this.originalFile = originalFile;
		this.saveFile = saveFile;
		this.saveFolderPath = saveFolderPath;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
}
