package preprocessing.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * ������һ��ʵ���࣬�����ṩ�����Ķ�д�ļ�����
 * 
 * @author Alex ���ļ��е����ݰ��ж��뵽List�����У����߽�List�����е�����д���ļ���
 */
public class FileOperate {

	/**
	 * �ú������ڽ��н�����������ļ��Ĳ���
	 * @param list ���潫Ҫ������ݵ��б�
	 * @param outFile ����ļ�·��
	 * @param append �Ƿ�׷��
	 */
	public static void Print2Txt(List<String> list, File outFile, boolean append) {

		try {
			OutputStreamWriter writer;
//			writer = new OutputStreamWriter(new FileOutputStream(outFile, true)); // ��txt��׷������
			writer = new OutputStreamWriter(new FileOutputStream(outFile, append)); // ���appendΪtrue�������׷��;���Ϊfalse���򸲸�ԭ�����ݡ�
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			// ��ͣ�ôʹ��˺�Ľ��д�뵽txt�ĵ���
			for (int i = 0; i < list.size(); i++) {
				bufferedWriter.write(list.get(i) + "\r\n");
			}
			bufferedWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * �ú������ڴ�txt�ж�ȡ���ݲ����浽List��
	 * 
	 * @param list
	 *            ���ڱ�����������
	 * @param inFile
	 *            ��Ҫд����ļ�
	 */
	/**
	 * �ú������ڴ�txt�ж�ȡ���ݲ����浽List��
	 * @param list ���ڱ�����������
	 * @param inFile ��Ҫд����ļ�
	 * @param flag �����ж��Ƿ����մ�����Ϊtrueʱ��ʾ���Զ���մ�����Ϊfalseʱ��ʾ������մ�
	 */
	public static void ReadFromTxt(List<String> list, File inFile, boolean flag) {

		// �����ʽ
		String charset = "GBK";// GBK----0
		// String charset = "UTF-8";// UTF-8

		if (inFile.isFile() && inFile.exists()) {
			try {
				InputStreamReader read;
				read = new InputStreamReader(new FileInputStream(inFile), charset); // ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				// ѭ�������ļ��е�ÿһ�У������浽List������
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// ��if����ж϶�����ַ����Ƿ�Ϊ�մ����մ������ж���
					if (!flag && lineTxt.isEmpty()) {	// flag����ָʾ�Ƿ����մ�
						
					} else {
						list.add(lineTxt.trim()); // ����һ�У������浽list��
					}
				}
				bufferedReader.close();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
