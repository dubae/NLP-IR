package org.example;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("CAU_SW_NLP HW2");
        System.out.println("MADE BY 20202927 KIM DUHOE(김두회)");
        System.out.println("Query form should be like that: [MODE(Boolean/Vector)] [Query]");
        System.out.println("e.g. Boolean love AND NOT worlds");
        System.out.println("e.g. Boolean love OR worlds");
        System.out.println("e.g. Vector love worlds");


        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("==================================");
            System.out.print("Please chat your query:");
            String query = scanner.nextLine();

            if(query.contains("Boolean")){
                query = query.replace("Boolean","");
                Set<String> set=Boolean_IR.getDocByBooleanQuery(query);
                System.out.println("Number of document: " + set.size());
                set.forEach(System.out::println);
            }else if(query.contains("Vector")){
                query = query.replace("Vector","");
                System.out.println("Poet Name || Relevance Score");
                Ranked_IR.getRankedDocs(query);
            }

        }while(true);



    }
}