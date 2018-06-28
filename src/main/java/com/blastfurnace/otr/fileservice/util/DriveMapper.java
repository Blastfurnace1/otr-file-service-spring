package com.blastfurnace.otr.fileservice.util;

import java.util.HashMap;

import com.blastfurnace.otr.util.FileUtils;

public class DriveMapper {

	private static final String[] drivesToCheck = {"E:/", "F:/", "G:/", "H:/", "I:/"};

	public static HashMap<String, String> getDriveMappings() {
		HashMap<String, String> drives = new HashMap<String, String>();

		for (int i = 0; i < drivesToCheck.length; i++) {
			try {
				String volume = FileUtils.getDiscVolumeLabel(drivesToCheck[i]);
				drives.put(volume, drivesToCheck[i]);
			} catch (Exception e) {
				// don't care
			}
		}
		return drives;
	}
}
