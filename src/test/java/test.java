import org.example.Boolean_IR;
import org.example.Ranked_IR;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class test {

    public static void main(String[] args) throws IOException {


        System.out.println("test 실행");

        포스팅리스트테스트();


        //parsingTest("snail OR bends OR NOT nectar");
        //parsingTest("love");

       // tfMapTest("love");
        //Ranked_IR.getRankedDocs("nectar");
    }

    public static void 포스팅리스트테스트() throws IOException {
        Boolean_IR.getPostingList().keySet().stream().forEach(key -> {
            try {
                Set set=Boolean_IR.getPostingList().get(key);

                for(Object o:set) {
                    System.out.println("key: " + key + " , poet : " + o);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void parsingTest(String query) throws IOException {

        for(String s: Boolean_IR.getDocByBooleanQuery(query)){
            System.out.println(s);
        }
    }

    public static void tfMapTest(String word) throws IOException {
        Map<String, Double> wMap= Ranked_IR.makeWeightedMat(word);

        for(String key :wMap.keySet()){
            System.out.println("poet name:" + key + ", value:" + wMap.get(key));
        }
    }
}
