package faq.tfidfSimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import preprocessing.chineseWordSegmentation.code.NlpirTest;
import preprocessing.stopWordsFilter.StopWordsFilter;
import preprocessing.utility.FileOperate;

public class CosineSimilarity {

	public static void main(String[] args) {
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		List<Double> questionVector = new ArrayList<Double>();
		List<String> preComputeVector = new ArrayList<String>();
		Map<String, String> idf = new HashMap<String, String>();
		List<String> featureWords = new ArrayList<String>();
		List<String> answers = new ArrayList<String>();
		// ��������
		System.out.println("���������⣺");
		Scanner cin = new Scanner(System.in);
		String questionStr = cin.nextLine();
		System.out.println(questionStr);
		// �ִ�����Ա�ע
		String questionStrSegment = NlpirTest.singleSentence(questionStr);
		System.out.println(questionStrSegment);
		// ͣ�ôʹ���
		StopWordsFilter swf = new StopWordsFilter();
		String questionStrStopWords = swf.singleRun(questionStrSegment);
		System.out.println(questionStrStopWords);
		// �ʾ�������
		// ����IDFֵ
		String charset = "GBK";// GBK----0
		File idfFile = new File("file\\Corpus\\FAQ\\vectorization\\IDF.txt");
		if (idfFile.isFile() && idfFile.exists()) {
			try {
				InputStreamReader read;
				read = new InputStreamReader(new FileInputStream(idfFile), charset); // ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				// ѭ�������ļ��е�ÿһ�У������浽List������
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// ��if����ж϶�����ַ����Ƿ�Ϊ�մ����մ������ж���
					if (lineTxt.isEmpty()) {	
						
					} else {
						String[] words = lineTxt.split(",");
						featureWords.add(words[0]);
						questionVector.add(0d);
						idf.put(words[0], words[1]);
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
		
		String[] questionWords = questionStrStopWords.split(" ");
		
		// �����ʾ��TFIDFֵ
		double y = 0d;
		for(String word : questionWords) {
			double tf = 0;
			for(String _word : questionWords) {
				if(word == _word) {
					++tf;
				}
			}
			if(idf.get(word) != null) {
				double tfidfValue = (tf / questionWords.length) * Double.parseDouble(idf.get(word));
				y += (tfidfValue * tfidfValue);
				for(int i = 0; i != featureWords.size(); ++i) {
					//System.out.println(featureWords.get(i));
					if(featureWords.get(i).equals(word)) {
						questionVector.set(i, tfidfValue);
//						System.out.println("---" + word);
					}
				}
			}

		}
		
		// �����������ƶ�
		File squareRoot = new File("file\\Corpus\\FAQ\\precomputation\\squareRoot.txt");
		FileOperate.ReadFromTxt(preComputeVector, squareRoot, true);
		
		File tfidfInputFile = new File("file\\Corpus\\FAQ\\vectorization\\TfIdf.txt");
		if (tfidfInputFile.isFile() && tfidfInputFile.exists()) {
			try {
				InputStreamReader read;
				read = new InputStreamReader(new FileInputStream(tfidfInputFile), charset); // ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				double dvalue = 0d;
				// ѭ�������ļ��е�ÿһ�У��������������ƶ�
				int iter = 0;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					double xy = 0d;
					double cosine = 0d;
					String[] demensions = lineTxt.split(",");
					for(int i = 0; i != demensions.length; ++i) {
						dvalue = Double.parseDouble(demensions[i]);
						if(dvalue > 0){
//							System.out.println(dvalue);
							xy += (dvalue * questionVector.get(i));
						}
					}
					
//					System.out.println(xy);
//					System.out.println(x);
					// �����������ƶ�
					cosine = xy / (Math.sqrt(y) * Double.parseDouble(preComputeVector.get(iter)));
					if(cosine > 0) {
						result.put((iter + 1), cosine);
						System.out.println((iter + 1) + ":" + cosine);
					}
					++iter;
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
		
		// �����ƶȵĽ����������
		System.out.println();
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(result.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			// ��������
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		// ���������
		File aInputFile = new File("file\\Corpus\\FAQ\\A_.txt");
		FileOperate.ReadFromTxt(answers, aInputFile, true);
		
		// ��ʾ�����Ľ��������TopN����
		int top = 5;
		int current = -1;
		boolean flag1 = false;
		System.out.println("Top" + top);
		for(Map.Entry<Integer, Double> mapping: list) {
			if((++current) == top) {
				break;
			} else if(mapping.getValue() > 0.5) {
				flag1 = true;
				System.out.println(mapping.getKey() + ": " + mapping.getValue());
				System.out.println(answers.get(mapping.getKey() - 1));
			}
			
		}
		
		if(current == -1 || flag1 == false) {
			System.err.println("�Բ��𣬱�FAQ������ʱû��������������ص����ݣ����ǽ�Ŭ���Ľ���");
		}
		
	}
}
