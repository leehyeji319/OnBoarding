package com.estgames.db.entiity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banner_file_info")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class BannerFileInfo extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "banner_file_info_id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "item_id")
	private Long itemId;

	@Column(name = "save_folder_path", nullable = false, length = 1000)
	private String saveFolderPath;

	@Column(name = "original_file", nullable = false, length = 500)
	private String originalFile;

	@Column(name = "save_file", nullable = false, length = 500)
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
