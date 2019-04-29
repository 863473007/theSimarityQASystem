package preprocessing.resultIntegration;

import java.io.File;
import java.util.ArrayList;

import preprocessing.utility.FileOperate;
import preprocessing.utility.GetFileName;

/**
 * ��Ԥ�������ļ��������ϣ�������һ���������Ĵ���
 * @author Alex
 *
 */
public class ResultIntegration {
	ArrayList<String> comment = new ArrayList<String>(); 		// ���ڱ���ÿ������µ���������
	ArrayList<String> filesPath = new ArrayList<String>();		// ���ڱ��浱ǰ�ļ������������ļ���·��
	ArrayList<String> filesName = new ArrayList<String>();		// ���ڱ��浱ǰ�ļ������������ļ����ļ���
	ArrayList<String> featureWords = new ArrayList<String>();	// ���ڱ���������
	String headInformation = "@data";							// ���ڱ������˵����Ϣ����Ϊ����ļ��ĵ�һ��
	
	
	/**
	 * ���뵱ǰ�ļ����µ��������ļ��������ǵ�ǰ�ļ����µ����ļ���
	 * @param filePath �����ļ��У�����ǰ�ļ���
	 * @return �Ƿ��ȡ�ɹ�
	 */
	private boolean obtainFile(String filePath){
		File files = new File(filePath);
		if(!files.isDirectory()) {
			System.out.println("�����·����һ���ļ������Ϸ������������룡����");
			return false;
		} else if(files.isDirectory()) {
            String[] filelist = files.list();
            for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filePath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                    	filesPath.add(readfile.getPath());
                    	filesName.add(readfile.getName());
                    } else if (readfile.isDirectory()) {
                    	
                    }
            }
		}
		return true;
	}
	
	/**
	 * ���������µ����������ϲ����γ�һ�����壬������������йص���Ϣ
	 * @param inputFilePath �����ļ���·��
	 */
	private void integration(String inputFilePath) {
		System.out.println("���Ϻϲ��С���");
		obtainFile(inputFilePath);
		// ����ÿһ������µ��ļ����ϲ���һ���ļ�
		int oldSize = 0;
		for(int i = 0; i < filesPath.size(); ++i) {
			// ����һ������µ������ı�
			File inFile = new File(filesPath.get(i));
			FileOperate.ReadFromTxt(comment, inFile, true);
			// ���������µ������Ϣ
			String tempClass = filesName.get(i).substring(0, filesName.get(i).indexOf("_"));
			headInformation += ("," + tempClass + ":" + (comment.size() - oldSize));
			oldSize = comment.size();
		}
		System.out.println("���Ϻϲ���ɡ�");
	}
	
	/**
	 * ������Ϻ������
	 * @param outputFilePath ���·��
	 */
	private void printCorpus(String outputFilePath) {
		System.out.println("���ϲ�����д��txt�С���");
		// ��ͷ����Ϣ��ӵ��ײ�
		comment.add(0, headInformation);
		// ��������µ������ı���������յ��ļ���
		File outFile = new File(outputFilePath += "Corpus.txt");
		// ���������ļ��ĸ�·�������ڣ��򴴽�֮
		File fileParent = outFile.getParentFile();
		if(!fileParent.exists()) {
			fileParent.mkdirs();
		}
		FileOperate.Print2Txt(comment, outFile, false);
		comment.remove(0);
		System.out.println("д����ɡ�");
	}
	
	/**
	 * �������ϲ���һ����֤�ʵ䣨���д�ȥ��ȥ�أ�
	 */
	private void FeatureWordsGeneration(){
		ArrayList<String> corpusWords = new ArrayList<String>();
		for(int i = 0; i < comment.size(); ++i) {
			// ��if����ж϶�����ַ����Ƿ�Ϊ�մ����մ������ж���
			if (!comment.get(i).isEmpty()) { // �����ж��Ƿ����մ�
				String line = comment.get(i);
				line = line.trim();
				String[] words = line.split(" ");
				for (String word : words) {
					corpusWords.add(word); // ����ÿһ������
				}
			}
		}
		
		// ��ȡ�����еĴʻ�
		System.out.println("�����ʴʵ������С���");
		boolean IsRepeated;	// �����жϵ�ǰ���Ƿ����ظ���
        featureWords.add(corpusWords.get(0));
        for (int i = 1; i != corpusWords.size(); ++i)
        {
            IsRepeated = false;
            for (int j = 0; j != featureWords.size(); ++j)
            {
                if (corpusWords.get(i).equals(featureWords.get(j)))
                {
                    IsRepeated = true;
                    break;
                }
            }
            if (!IsRepeated)
            {
                featureWords.add(corpusWords.get(i));
            }
        }
        System.out.println("������ɡ�");
	}
	
	/**
	 * ����ȡ����֤��������ı��ĵ���
	 * @param outputFilePath ���·��
	 */
	private void printFeatureWords(String outputFilePath){
		System.out.println("�������ʴʵ�д��txt�С���");
		File outFile = new File(outputFilePath + "FeatureWords.txt");
		// ���������ļ��ĸ�·�������ڣ��򴴽�֮
		File fileParent = outFile.getParentFile();
		if(!fileParent.exists()) {
			fileParent.mkdirs();
		}
		FileOperate.Print2Txt(featureWords, outFile, false);
		System.out.println("д����ɡ�");
	}
	
	public void run(String inputFilePath, String outputFilePath){
		integration(inputFilePath);
		printCorpus(outputFilePath);
		FeatureWordsGeneration();
		printFeatureWords(outputFilePath);
	}
	
	public static void main(String[] args) {
		String rootPath = ".\\file\\Corpus\\"; // ��������ļ��ĸ�·��
		String inputFilePath = rootPath + "FAQ\\stopWordsFilter10000";// �����ļ���·��
		String outputFilePath = rootPath + "FAQ";// �������Ϻ�Ľ��
		
		ResultIntegration rt = new ResultIntegration();
		outputFilePath += ("\\preprocessed\\preprocessed"); // ȷ������ļ�������·��
		rt.run(inputFilePath, outputFilePath);
	}
	
}
