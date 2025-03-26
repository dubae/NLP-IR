package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Boolean_IR {



    // Inverted Index: Step1
    public static List<String[]> makeAllPairTable() throws IOException {

        File dir = new File("poetry_data");

        List<String[]> allPairTable = new ArrayList<>();

        for(File file : dir.listFiles()){
            List<String[]> pairTable = makeOnePairTable(file);
            allPairTable.addAll(pairTable);
        }

        allPairTable.sort(Comparator.comparing(arr -> arr[0])); //sort by Term




        return allPairTable;
    }

    // posting list(key: term, value: Set<name of poetry>)
    public static HashMap<String, Set<String>> getPostingList() throws IOException {
        List<String[]> allPairTable= makeAllPairTable();

        HashMap<String, Set<String>> postingList = new HashMap<>();
        for(String[] pair : allPairTable){
            if(!postingList.containsKey(pair[0])){
                postingList.put(pair[0], new HashSet<>());
            }
            postingList.get(pair[0]).add(pair[1]);

        }




        return postingList;
    }

    //get one pair table for one poem.
    public static List<String[]> makeOnePairTable(File file) throws IOException {
        String fileName = file.getName();
        List<String[]> pairTable = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));

        String str;
        while ((str = br.readLine()) != null) {
            String[] tokens = str.split("\\s+");
            for(int i = 0; i < tokens.length; i++){
                String[] pair={tokens[i], fileName};
                pairTable.add(pair);
            }
        }







        br.close();



        return pairTable;
    }



    //get all documents by parsing input query.
    //query must be: Brutus AND Caesar AND NOT Calpurnia
    public static  Set<String> getDocByBooleanQuery(String query) throws IOException {

        HashMap<String, Set<String>> postingList= getPostingList();

        // 1. parsing
        query = query.replaceAll("\\s+", ""); //replace all space to null
        String[] andQuaries=query.split("AND");
        String[] orQuaries=query.split("OR");

        // and operation
        if(andQuaries.length>=orQuaries.length){
            HashMap<String, Set<String>> trueMap = new HashMap<>(); //포함된 것이 사실인 맵
            HashMap<String, Set<String>> falseMap = new HashMap<>(); //포함된 것이 가짜인 맵(NOT)

            Set<String> trueSet = new HashSet<>();
            Set<String> falseSet = new HashSet<>();

            for(String s : andQuaries){
                if(s.contains("NOT")){
                    s=s.replace("NOT",""); //NOT 제거 후 NOT MAP에 투입
                    Set<String> set = postingList.get(s); //키워드에 해당하는 set 추출(시 제목 set)
                    falseMap.put(s, set);
                    falseSet.addAll(set);

                }
                else{
                    Set<String> set = postingList.get(s); //키워드에 해당하는 set 추출(시 제목 set)
                    trueMap.put(s, set); //추출해서 결과맵에 저장

                    //trueSet.addAll(set);
                    Set<String> tempSet=new HashSet<>();
                    //AND OP MERGE
                    for(String poetryName: set){
                        if(trueSet.contains(poetryName)||trueSet.isEmpty()){ tempSet.add(poetryName); }
                    }

                    trueSet=tempSet;
                }
            }

            for(String s: falseSet){ //falseSet은 NOT 연산이므로 trueSet에서 이를 제거
                trueSet.remove(s);
            }



            //trueset에는 키워드에 해당하는 것만 남아있는 상태
            return trueSet;


        }

        //or operation
        else{
            Set<String> trueSet = new HashSet<>();
            Set<String> falseSet = new HashSet<>();
            for(String s : orQuaries){


                if(s.contains("NOT")){
                    s=s.replaceAll("NOT","");
                    for(String key:postingList.keySet()){
                        if(!key.equals(s)){
                            falseSet.addAll(postingList.get(key));
                        }
                    }
                }
                else{
                    Set<String> set = postingList.get(s); //키워드에 해당하는 set 추출(시 제목 set)
                    trueSet.addAll(set);
                }
            }

            trueSet.addAll(falseSet);
            return trueSet;
        }

        // 2. check "NOT"


        //return List.of(null);

    }

}
