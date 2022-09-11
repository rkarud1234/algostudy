package kgs;

import java.util.*;
import java.io.*;
public class BOJ20056 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int n;
    static List<FireBall> fireBalls;
    static List<FireBall>[][] board;
    static int dx[] = {-1,-1,0,1,1,1,0,-1};
    static int dy[] = {0,1,1,1,0,-1,-1,-1};
    public static void main(String[] args) throws Exception{
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        fireBalls = new ArrayList<>();
        board = new ArrayList[n][n];
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                board[i][j] = new ArrayList();
            }
        }
        int M = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        for(int i = 0; i<M; i++){
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            int m = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            fireBalls.add(new FireBall(r,c,m,s,d));
        }

        for(int i = 0; i<k; i++){
            move();
            checkTwo();
        }
        int ans = 0;
        for(FireBall f : fireBalls){
            ans += f.m;
        }
        System.out.println(ans);
    }
    static void checkTwo(){
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(board[i][j].size()>=2) {
                    int Msum = 0;
                    int Ssum = 0;
                    boolean odd = true;
                    boolean even = true;
                    int cnt = board[i][j].size();
                    for(FireBall f : board[i][j]) {
                        Msum += f.m;
                        Ssum += f.s;
                        even = even & (f.d % 2 == 0 ? true : false);
                        odd = odd & (f.d % 2 == 1 ? true : false);
                        fireBalls.remove(f);
                    }
                    if(Msum / 5 == 0) {
                        board[i][j].clear();
                        continue;
                    }
                    if (even | odd) {
                        for (int d = 0; d < 8; d += 2) {
                            fireBalls.add(new FireBall(i, j, Msum / 5, Ssum / cnt, d));
                        }
                    } else {
                        for (int d = 1; d < 8; d += 2) {
                            fireBalls.add(new FireBall(i, j, Msum / 5, Ssum / cnt, d));
                        }
                    }
                }
                board[i][j].clear();
            }
        }

    }
    static void move(){
        for(FireBall f : fireBalls){
            int nx = (f.r + n + dx[f.d] * (f.s%n))%n;
            int ny = (f.c + n + dy[f.d] * (f.s%n))%n;
            f.r = nx;
            f.c = ny;
            board[nx][ny].add(f);
        }
    }
    static class FireBall{
        int r,c,m,d,s;
        FireBall(int r, int c, int m, int s, int d){
            this.r = r;
            this.c = c;
            this.m = m;
            this.d = d;
            this.s = s;
        }
    }
}
