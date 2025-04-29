package org.example;

import java.io.*;
import java.util.*;

public  class Ranked_IR {



    //쿼리를 받아 순위에 기반한 문서 탐색
    // 띄어쓰기로 구분
    public static void getRankedDocs(String query) throws IOException {
        query=query.trim();
        String[] words = query.split("\\s+");
        List<String> rankedDocs = new ArrayList<>();

        HashMap<String, Double> totalWeightMap = new HashMap<>();

        for(String word:words){
            HashMap<String, Double> weightMap=makeWeightedMat(word);
            for(String poetName: weightMap.keySet()){
              //  if(totalWeightMap.containsKey(poetName)){
                    totalWeightMap.put(poetName, totalWeightMap.getOrDefault(poetName, 0.)+weightMap.get(poetName));
              //  }

            }

        }

        List<Map.Entry<String, Double>> entryList = new ArrayList<>(totalWeightMap.entrySet());

        // 값 기준으로 정렬 (오름차순)
        entryList.sort(Map.Entry.<String, Double>comparingByValue().reversed());


        // 출력
        int i=1;
        for (Map.Entry<String, Double> entry : entryList) {
            System.out.println("["+i++ +"] " + entry.getKey()
//                    + ": " + entry.getValue()
            );
        }





    }

    // make weighted matrix
    // 한 단어(param: word)에 해당하는 행, 즉 시 제목-weight 쌍을 반환
    public static HashMap<String, Double> makeWeightedMat(String word) throws IOException {

        List<String[]> allPairTable= Boolean_IR.makeAllPairTable();
        HashMap<String, Set<String>> postingList = Boolean_IR.getPostingList(); //단어 별 시 목록

        HashMap<String, Double> tfMap = new HashMap<>();
        HashMap<String, Double> idfMap = new HashMap<>();
        HashMap<String, Double> wMap = new HashMap<>();

        // 1. create tfMap
        for(String[] pair: allPairTable){
            if(pair[0].equals(word)){ // 찾는 단어를 발견
                if(tfMap.containsKey(pair[1])){
                    tfMap.put(pair[1], tfMap.get(pair[1])+1);
                }
                else{
                    tfMap.put(pair[1], 1.0);
                }
            }
        }

        for(String poetName: tfMap.keySet()){
            tfMap.put(poetName, 1+ Math.log10(tfMap.get(poetName)));
        }


        // 2. create idfMap
        int df=postingList.get(word).size();
        long N=new File("poetry_data").length(); //전체 파일 개수
        double idf= Math.log10((double) N /df)/Math.log10(2);

        // 3. create Weight Matrix
        // tf맵에 바로 곱해서 만들어도될듯
        for(String poetName: tfMap.keySet()){
            tfMap.put(poetName, tfMap.get(poetName)*idf);
        }

        return tfMap;


    }

}
