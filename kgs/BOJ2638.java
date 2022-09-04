package kgs;

import java.util.*;
import java.io.*;

public class BOJ2638 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int n,m;
    static int board[][];
    public static void main(String[] args) throws Exception{

        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new int[n][m];
        for(int i = 0; i<n; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<m; j++){
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        // 모눈종이 맨 가쪽은 안녹는다! == 시작점은 모눈종이 맨 가쪽
        int ans = 0;
        while(check()){
            melt();
            ans++;
        }
        System.out.println(ans);
    }
    static void melt(){
        for(int i = 0; i<n; i++){
            for(int j = 0; j<m; j++){
                if(checked[i][j] >= 2) board[i][j] = 0;
            }
        }
    }
    static int dx[] = {0,1,0,-1};
    static int dy[] = {1,0,-1,0};
    static int checked[][];
    static boolean check(){
        boolean ret = false;
        boolean visited[][] = new boolean[n][m];
        checked = new int[n][m];
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{0,0});
        visited[0][0] = true;
        while(!q.isEmpty()){
            int[] now = q.poll();

            for(int i = 0; i<4; i++){
                int nx = now[0] + dx[i];
                int ny = now[1] + dy[i];
                if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] == 1){
                    checked[nx][ny]++;
                    if(checked[nx][ny] >= 2) ret = true;
                }else{
                    visited[nx][ny] = true;
                    q.add(new int[]{nx,ny});
                }
            }
        }

        return ret;
    }
}
