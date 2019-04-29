package preprocessing.dictionaryManage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import preprocessing.utility.FileOperate;

/**
 * ���������еĴ�,�γ�һ���ʻ��(�ʵ�)
 * 
 * @author Alex
 *
 */
public class VocabularyGeneration {

	public static void main(String[] args) throws IOException, FileNotFoundException {
		List<String> CorpusWords = new ArrayList<String>(); // ���ڱ����������������еĴ���
		List<String> Vocabulary = new ArrayList<String>(); // �ʻ�������ʱ�
		String dictionaryPath = "E:\\EclipseJ2EEWorkspace\\SentimentAnalysisPreprocess\\file\\Dictionary\\"; // �����ļ��ĸ�·��

		// ��������
		System.out.println("���������С���");
		File file1 = new File(dictionaryPath + "part1.txt");

		// �����ʽ
		String charset = "GBK";// GBK----0
		// String charset = "UTF-8";// UTF-8

		if (file1.isFile() && file1.exists()) {
			InputStreamReader read;
			read = new InputStreamReader(new FileInputStream(file1), charset); // ���ǵ������ʽ
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			
			// ѭ�������ļ��е�ÿһ�У������浽List������
			while ((lineTxt = bufferedReader.readLine()) != null) {
				// ��if����ж϶�����ַ����Ƿ�Ϊ�մ����մ������ж���
				if (!lineTxt.isEmpty()) { // �����ж��Ƿ����մ�
					lineTxt = lineTxt.trim();
					String[] words = lineTxt.split(" ");
					for (String word : words) {
						CorpusWords.add(word); // ����ÿһ������
					}
				}
			}
			bufferedReader.close();
		}
		System.out.println("���϶������");

		// ��ȡ�����еĴʻ�
		System.out.println("�ʻ���ȡ�С���");
		boolean IsRepeated;	// �����жϵ�ǰ���Ƿ����ظ���
        Vocabulary.add(CorpusWords.get(0));
        for (int i = 1; i != CorpusWords.size(); ++i)
        {
            IsRepeated = false;
            for (int j = 0; j != Vocabulary.size(); ++j)
            {
                if (CorpusWords.get(i).equals(Vocabulary.get(j)))
                {
                    IsRepeated = true;
                    break;
                }
            }
            if (!IsRepeated)
            {
                Vocabulary.add(CorpusWords.get(i));
            }
        }
        System.out.println("��ȡ���");

		/**
		 * ���
		 */
		System.out.println("���ʻ��д��txt�С���");
		File file2 = new File(dictionaryPath + "FeatureWords.txt");
		FileOperate.Print2Txt(Vocabulary, file2, false);
		System.out.println("д�����");
	}

}
