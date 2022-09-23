package kgs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class BOJ5021 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static HashMap<String, ArrayList<String>> hm;
    static HashMap<String, Double> nameScore;
    public static void main(String[] args) throws Exception{
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        String king = br.readLine();
        hm = new HashMap<>();
        nameScore = new HashMap<>();
        for(int i = 0; i<n; i++){
            st = new StringTokenizer(br.readLine());
            String child = st.nextToken();
            String p1 = st.nextToken();
            String p2 = st.nextToken();
            hm.putIfAbsent(child, new ArrayList<>());
            hm.get(child).add(p1);
            hm.get(child).add(p2);
            nameScore.put(child, 0.0);
            nameScore.put(p1, 0.0);
            nameScore.put(p2, 0.0);
        }
        nameScore.put(king, 1.0);
        double max = 0;
        String ans = "";
        for(int j = 0; j<m; j++){
            String candidate = br.readLine();
            double score = dfs(candidate);
            if(max < score){
                max = score;
                ans = candidate;
            }
        }
        System.out.println(ans);
    }
    static double dfs(String name){
        if(hm.get(name) == null){ // 부모가 없음
            return nameScore.get(name) == null ? 0.0 : nameScore.get(name);
        }
        ArrayList<String> parents = hm.get(name);
        String p1 = parents.get(0);
        String p2 = parents.get(1);
        double ret = (dfs(p1) + dfs(p2))/2;
        nameScore.put(name, ret);
        return ret;
    }
}
