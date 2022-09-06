package kgs;

import java.io.*;
import java.util.*;

public class BOJ2143 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int t,n,m;
    public static void main(String[] args) throws IOException {
        t = Integer.parseInt(br.readLine());
        n = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        int A[] = new int[n];
        for(int i = 0; i<n; i++){
            A[i] = Integer.parseInt(st.nextToken());
        }
        m = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        int B[] = new int[m];
        for(int i = 0; i<m; i++){
            B[i] = Integer.parseInt(st.nextToken());
        }
        ArrayList<Integer> aList = new ArrayList<>();
        ArrayList<Integer> bList = new ArrayList<>();
        for(int i = 0; i<n; i++){
            int sum = 0;
            for(int j = i; j<n; j++){
                sum += A[j];
                aList.add(sum);
            }
        }
        for(int i = 0; i<m; i++){
            int sum = 0;
            for(int j = i; j<m; j++){
                sum += B[j];
                bList.add(sum);
            }
        }
        Collections.sort(aList);
        Collections.sort(bList);
        long ans = 0;
        int pa = 0;
        int pb = bList.size() - 1;

        while(pa < aList.size() && pb >= 0){

            long sum = aList.get(pa) + bList.get(pb);
            if(t == sum){
                long cntA = 0;
                long cntB = 0;
                int a = aList.get(pa);
                int b = bList.get(pb);
                while(pa < aList.size() && aList.get(pa) == a){
                    pa++;
                    cntA++;
                }
                while(pb >= 0 && bList.get(pb) == b){
                    pb--;
                    cntB++;
                }
                ans += cntA * cntB;

            }else if(sum < t){
                pa++;
            }else if(sum > t){
                pb--;
            }
        }

        System.out.println(ans);
    }
}
