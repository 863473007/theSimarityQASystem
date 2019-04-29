package preprocessing.utility;

import java.io.File;

public class GetFileName {
	
	/**
	 * ��ȡ�����ļ����ļ���
	 * @param filePath �����ļ�����·��
	 * @return ���ز�����չ�����ļ���
	 */
	public String getFileName(String filePath){
		// ��õ��ļ������ļ����͵���չ�������磺pos.txt��
		File tempFile = new File(filePath.trim()); 
		String fileNameExtension = tempFile.getName(); // ���ڱ������չ�����ļ���
		// ȥ���ļ����͵���չ�������磺pos��
		String fileName = null;						   // ���ڱ��治����չ�����ļ���
		int dot = fileNameExtension.lastIndexOf(".");
		if((dot > -1) && (dot < fileNameExtension.length())) {
			fileName = fileNameExtension.substring(0, dot);
		}
//		System.out.println(fileName);
		return fileName;
	}
	
}
