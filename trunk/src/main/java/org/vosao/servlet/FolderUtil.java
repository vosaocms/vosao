package org.vosao.servlet;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;

/**
 * Folder utils.
 * 
 * @author Aleksandr Oleynik
 */
public class FolderUtil {
	
	public static String[] getPathChain(final String path) 
			throws UnsupportedEncodingException {
        String[] chain = path.split("/");
		return Arrays.copyOfRange(chain, 1, chain.length);
	}
	
	public static String[] getFolderChain(final String[] chain) {
		return Arrays.copyOfRange(chain, 0, chain.length - 1);
	}
	
	public static FileEntity getFile(final TreeItemDecorator<FolderEntity> tree,
			final String[] chain, final String filename) {

		FolderEntity folder = getFolder(chain, 0, tree);
		if (folder != null) {
			for(FileEntity file : folder.getFiles()) {
				if (file.getFile().getFilename().equals(filename)) {
					return file;
				}
			}
		}
		return null;
	}
	
	private static FolderEntity getFolder(final String[] chain, final int index, 
			final TreeItemDecorator<FolderEntity> tree) {

		if (chain.length == 0) {
			return tree.getEntity();
		}
		String folderName = chain[index];
		for(TreeItemDecorator<FolderEntity> folder : tree.getChildren()) {
			if (folder.getEntity().getName().equals(folderName)) {
				if (index == chain.length-1) {
					return folder.getEntity();
				}
				else {
					return getFolder(chain, index + 1, folder);
				}
			}
		}
		return null;
	}
	
	
}