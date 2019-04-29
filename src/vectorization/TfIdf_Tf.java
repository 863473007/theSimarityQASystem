package vectorization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import preprocessing.utility.FileOperate;

/**
 * ��Ԥ�������ı���������������
 * ����������������������㷨��TF, TFIDF
 * @author Alex
 *
 */
public class TfIdf_Tf {
	List<String> corpus = new ArrayList<String>(); // Ԥ������ԭʼ����
	List<String> featureWords = new ArrayList<String>(); // ������
	List<Float> idf = new ArrayList<Float>(); // �����ĵ�����
	List<String> labels = new ArrayList<String>(); // ��ǩ��Ϣ
	List<Integer> amounts = new ArrayList<Integer>(); // ÿ������µ���������
	
	/**
	 * ׼������������Ҫ����������������������
	 * @param inputFilePath �����ļ���ŵ�·��
	 */
	private void prepare(String inputFilePath) {
		/**
		 * ����Ԥ�������ı�
		 */
		System.out.println("׼���������������Ϻ������ʣ���������Ӧ�ĳ�ʼ������");
		File infile1 = new File(inputFilePath + "\\preprocessedCorpus.txt");
		FileOperate.ReadFromTxt(corpus, infile1, true);		
		System.out.println("������ = " + (corpus.size() - 1));
		
		// ���������Ϣ������Ϣ��洢�������ı��ĵ�һ�У�
		// @data,neg:2000,pos:2000
		String classInformation = corpus.get(0);
		String fields[] = classInformation.split(",");
		amounts.add(0); // ��ÿ�������������������������Σ����磺0-2000��2001-4000
		for(int i = 1; i != fields.length; ++i) {
			String field = fields[i];
			String str[] = field.split(":");
			labels.add(str[0]);
			amounts.add(Integer.parseInt(str[1]) + amounts.get(amounts.size() - 1));
		}

		/**
		 * ����������
		 */
		File infile2 = new File(inputFilePath + "\\preprocessedFeatureWords.txt");
		FileOperate.ReadFromTxt(featureWords, infile2, false);
		System.out.println("�������� = " + featureWords.size());
		System.out.println("׼��������ɡ�");
		
	}
	
	/**
	 * �ǹ�һ����Ƶ
	 * @param bufferedWriter д���ļ���
	 * @throws IOException
	 */
	private void tfCompute(BufferedWriter bufferedWriter) throws IOException{
		System.out.println("TF����������");
		// ��ƵTF�������й�һ��
		int tf_ij_count = 0;
		for (int i = 1; i < corpus.size(); ++i) {	// ѭ����1��ʼ����Ϊ��0�б���������ϵ������Ϣ
			for (int j = 0; j < featureWords.size(); ++j) {
				tf_ij_count = 0; // ��¼��j���������ڵ�i�������г��ֵĴ������������й�һ���Ĵ�Ƶ

				String[] words = corpus.get(i).split(" "); // ���ո�Ծ��ӽ��л���
				for (String word : words) {
					if (word != "") // ȥ��һ������ĩβ�Ŀմ������зֵ�ʱ���ĩβ�ո����Ŀմ�����������/���ԣ��Դ����word��
					{
						if (featureWords.get(j).equals(word)) {
							++tf_ij_count;
						}
					}
				}

				bufferedWriter.write(tf_ij_count + ",");
			}
			// ���ÿһ�������ı�ǩ
			for(int z = 0; z != (amounts.size() - 1); ++z) {
				if(i > amounts.get(z) && i <= amounts.get(z + 1)) {
					bufferedWriter.write(labels.get(z));
					break;
				}
			}
			// ����
			bufferedWriter.newLine();
		}
		System.out.println("TF��������ɡ�");
	}
	
	/**
	 * ����TF-IDF����������
	 * @param bufferedWriter д���ļ���
	 * @throws IOException
	 */
	private void tfidfCompute(BufferedWriter bufferedWriter) throws IOException {
		/**
		 * ������Ȩֵ����
		 */
		
		File outFile = new File("file\\Corpus\\FAQ\\vectorization\\IDF.txt");
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFile, false)); // ��txt��׷������
		BufferedWriter bufferedWriterIDF = new BufferedWriter(writer); // ��ʹ��Tf-Idf��������Ľ��д�뵽txt�ĵ���
		
		System.out.println("TFIDF��������������");
		// ���������ĵ�Ƶ��IDF
		for (int j = 0; j < featureWords.size(); ++j) {
			float df_i = 0; // df_iΪ���ٺ���һ��������t_j�������ı���
			float idf_j = 0; // ������t_j�����ۼ��е������ĵ�Ƶ��
			for (int i = 0; i < corpus.size(); ++i) {
				String[] words = corpus.get(i).split(" "); // ���ո�Ծ��ӽ��л���
				for (String word : words) {
					if (featureWords.get(j).equals(word)) {
						++df_i;
						break;
					}
				}
			}
			idf_j = (float) (Math.log((corpus.size() / (df_i + 1))) / Math.log(2)); // ���ݹ�ʽ����IDF��ȡ����ʱ��ȡ��Ϊ2��Math.logĬ�ϵ���Ϊe
			if (idf_j >= 0) // �����������IDF<0ʱ��ֱ��ȡ�㡣��Ϊ��ʱ�ô���ÿһ���ĵ��ж������ˣ����ķ����ĵ�����Ӧ��Ϊ0
			{
				idf.add(idf_j);
				bufferedWriterIDF.write(featureWords.get(j) + "," + idf_j);
				bufferedWriterIDF.newLine();
			} else {
				idf.add(0f);
				bufferedWriterIDF.write(featureWords.get(j) + "," + 0f);
				bufferedWriterIDF.newLine();
			}
		}
		bufferedWriterIDF.close();
		
		// ����TF��һ����Ƶ��Ϊ�����Ч�ʣ���û�����ô���
		// ѭ�������һ����ƵTF
		for (int i = 1; i < corpus.size(); ++i) {	// ѭ����1��ʼ����Ϊ��0�б���������ϵ������Ϣ
			for (int j = 0; j < featureWords.size(); ++j) {
				float tf_ij = 0; // ��j���������ڵ�i�������еĹ�һ����Ƶ
				float tf_ij_count = 0; // ��¼��j���������ڵ�i�������г��ֵĴ���
				float tf_i_count = 0; // ��¼��i�����������д�����ִ���֮�� 

				String[] words = corpus.get(i).split(" "); // ���ո�Ծ��ӽ��л���
				for (String word : words) {
					if (word != "") // ȥ��һ������ĩβ�Ŀմ������зֵ�ʱ���ĩβ�ո����Ŀմ�����������/���ԣ��Դ����word��
					{
						if (featureWords.get(j).equals(word)) {
							++tf_ij_count;
						}
						++tf_i_count;
					}
				}
				// ���ݼ��������tf_ij_count��tf_i_count�������һ����Ƶtf_ij
				if (tf_ij_count != 0) // ����tf_ijֵΪ0�Ĵʣ�ֱ�Ӹ�0ֵ������Ҳ�ܿ��˷�ĸtf_iΪ0�����
				{
					tf_ij = tf_ij_count / tf_i_count; // �����һ����Ƶ
					
				} else {
					tf_ij = 0;
				}
				bufferedWriter.write((tf_ij * idf.get(j)) + ",");
			}			

			// ����
			bufferedWriter.newLine();
		}
		System.out.println("TFIDF��������ɡ�");
	}
	
	/**
	 * ������������ı���Arff�ļ��ĸ�ʽ���
	 * @param outputFilePath ����ļ���·��
	 * @param type �����������ͣ�������������֣�TF, TFIDF��
	 * @throws IOException
	 */
	public void print2Arff(String outputFilePath, String type) throws IOException {
		File outFile = new File(outputFilePath + "\\" + type + ".txt");
		// ���������ļ��ĸ�·�������ڣ��򴴽�֮
		File fileParent = outFile.getParentFile();
		if(!fileParent.exists()) {
			fileParent.mkdirs();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFile, false)); // ��txt��׷������
		BufferedWriter bufferedWriter = new BufferedWriter(writer); // ��ʹ��Tf-Idf��������Ľ��д�뵽txt�ĵ���
		
		
		// ������Ϣ
		if(type == "Tf") {
			tfCompute(bufferedWriter);
		} else if(type == "TfIdf") {
			tfidfCompute(bufferedWriter);
		}
		bufferedWriter.close();
		System.out.println("�������Ľ������Arff��ʽд���ļ���");
	}
	
	// ����TF����������
	public void runTf(String inputFilePath, String outputFilePath) throws IOException {
		prepare(inputFilePath);
		print2Arff(outputFilePath, "Tf");
	}
	
	// ����TFIDF����������
	public void runTfIdf(String inputFilePath, String outputFilePath) throws IOException {
		prepare(inputFilePath);
		print2Arff(outputFilePath, "TfIdf");
	}

	public static void main(String[] args) throws IOException {
		String rootPath = ".\\file\\Corpus\\"; // ������ϵĸ�·��
		String inputFilePath = rootPath + "FAQ\\preprocessed";
		String outputFilePath = rootPath + "FAQ";
		
		TfIdf_Tf tfidf_tf = new TfIdf_Tf();
		outputFilePath += "\\vectorization";
		tfidf_tf.runTfIdf(inputFilePath, outputFilePath);
//		tfidf_tf.runTf(inputFilePath, outputFilePath);

	}
}
