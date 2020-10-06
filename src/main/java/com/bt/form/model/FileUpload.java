package com.bt.form.model;

import java.io.File;

public class FileUpload {
	String id;
	File file;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
