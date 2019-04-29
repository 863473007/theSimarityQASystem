package preprocessing.extractionOfFeatureWords;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import preprocessing.utility.FileOperate;
import preprocessing.utility.GetFileName;

public class ExtractionOfFeatureWords {
	List<String> data = new ArrayList<String>();   // ԭ����
    List<String> FeatureWordsList = new ArrayList<String>(); // �������б�
    List<String> FinishedFeatureWordList = new ArrayList<String>(); // ��������ȡ�������
    List<String> FeatureWordList1 = new ArrayList<String>(); // ��ȡ�����������ı��е�������
    List<String> FeatureWordList2 = new ArrayList<String>(); // ��ȡ�����������ı��е������ʣ�ȥ�غ�
    
    /**
     * ׼��������������Ҫ������ļ���������дʵ����ڽ���������ȡ
     * @param inputFilePath	�����ļ���·��������Ҫ�����ļ���·��
     */
    private void prepare(String inputFilePath){

        String dictionaryPath = ".\\file\\Dictionary\\��дʴʵ�\\�Ľ�����дʵ�ȥ��ȥ�ذ�20161219.txt";	// �ʵ��·��

        /**
         * ����ͣ�ôʹ��˺������
         */
//		File inFile1 = new File(corpusPath + "SemiSuperviesdLearning\\posStopWordsFilter.txt");
		File inFile1 = new File(inputFilePath);
		FileOperate.ReadFromTxt(data, inFile1, true);

        /**
         *  ������дʵ��е���д�
         */
//		File inFile2 = new File(dictionaryPath + "��дʴʵ�\\�Ľ�����дʵ�ȥ��ȥ�ذ�20161219.txt");
		File inFile2 = new File(dictionaryPath);
		FileOperate.ReadFromTxt(FeatureWordsList, inFile2, false);
    }
    
    /**
     * ������������ȡ
     * @param outputFilePath ����ļ���·����������֤����ȡ�������������ᶨ·�����ļ���
     */
    private void extraction(String outputFilePath){
    	/**
		 * ��֤����ȡ
		 */
        System.out.println("��������ȡ�С�������");
        //��ѭ�����������ʵ���ȡ
        for (int i = 0; i < data.size(); i++)
        {
            String FinishedFeatureWordLine = ""; //���ڱ����������������ȡ��һ������
            if (data.get(i) != "")
            {
                String[] words = data.get(i).split(" ");
                for (String word : words)
                {
                    if (word != "")
                    {
                    	// ������Ĵʵ����ƥ�䣬�жϵ�ǰ���Ƿ�Ϊ������
                        for (int j = 0; j < FeatureWordsList.size(); j++)
                        {
                            if (word.equals(FeatureWordsList.get(j)))
                            {
                                FeatureWordList1.add(word);
                                FinishedFeatureWordLine += word + " ";
                                break;
                            }
                        }

                    }
                }

                FinishedFeatureWordList.add(FinishedFeatureWordLine);//����������ȡ��ĵ������ۼ��뵽List�н��б���
            }//if
            else //����ǿմ�����ֱ��д��մ�
            {
                FinishedFeatureWordList.add("");
            }

        }//for
        System.out.println("��������ȡ��ɡ�");

//        //����ȡ�����������ʽ���ȥ��
//        System.out.println("�Դ��ı�����ȡ�����������ʽ���ȥ���С�������");
//        boolean IsRepeated;	// �����жϵ�ǰ���Ƿ����ظ���
//        FeatureWordList2.add(FeatureWordList1.get(0));
//        for (int i = 1; i < FeatureWordList1.size(); i++)
//        {
//            IsRepeated = false;
//            for (int j = 0; j < i; j++)
//            {
//                if (FeatureWordList1.get(i).equals(FeatureWordList1.get(j)))
//                {
//                    IsRepeated = true;
//                    break;
//                }
//            }
//            if (!IsRepeated)
//            {
//                FeatureWordList2.add(FeatureWordList1.get(i));
//            }
//        }
//        System.out.println("ȥ����ɡ�");

        //���������ȡ����ı��ļ�
        System.out.println("������������ȡ������ϡ���");
        File outFile1 = new File(outputFilePath);
		// ���������ļ��ĸ�·�������ڣ��򴴽�֮
		File fileParent = outFile1.getParentFile();
		if(!fileParent.exists()) {
			fileParent.mkdirs();
		}
        FileOperate.Print2Txt(FinishedFeatureWordList, outFile1, false);
        System.out.println("������ɡ�");
//
//        //����������ı�����ȡ������������
//        System.out.println("�������������ȡ������������");
//        File outFile2 = new File(outputFilePath + "FeatureWords.txt");
//        FileOperate.Print2Txt(FeatureWordList2, outFile2);
    }
    
    public void run(String inputFilePath, String outputFilePath){
		prepare(inputFilePath);
		extraction(outputFilePath);
    }

	public static void main(String[] args) {
    	String rootPath = ".\\file\\Corpus\\";	// ���ϸ�·��
    	String inputFilePath = rootPath + "HotelTest\\stopWordsFilter\\neg_Segmentation_stopWordsFilter.txt";	 // �����ļ���·��������Ҫ������ļ�·��
		String outputFilePath = rootPath + "HotelTest"; // ������������ȡ��������ı�
		
		/*
		 * ��ȡ�����ļ����ļ���
		 */
		GetFileName gfp = new GetFileName();
		String fileName = gfp.getFileName(inputFilePath);
		
		outputFilePath += ("\\extractionOfFeatureWords\\" + fileName + "_extOfFeaWor.txt");
    	
    	ExtractionOfFeatureWords eofw = new ExtractionOfFeatureWords();
    	eofw.run(inputFilePath, outputFilePath);
	}
	

}
