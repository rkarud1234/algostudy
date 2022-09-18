package kgs;

import java.util.*;
import java.io.*;
public class SW220202 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int n;
    static int board[][];
    public static void main(String[] args) throws Exception{
        n = Integer.parseInt(br.readLine());
        board = new int[n][n];
        for(int i = 0; i<n; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int j = 0; j<n; j++){
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        int ans = 0;
        for(int i = 0; i<4; i++){
            ans += getScore();
            rotate();
        }
        System.out.println(ans);
    }
    static int getScore(){
        grouping();
        return getArtScore();
    }
    static int getArtScore(){
        int ret = 0;

        for(int x = 0; x<n; x++){
            for(int y = 0; y<n; y++){
                for(int dir = 0; dir<4; dir++){
                    int nx = x + dx[dir]; int ny = y + dy[dir];
                    if(inRange(nx,ny) && groupIdx[nx][ny] != groupIdx[x][y]){
                        int idx1 = groupIdx[x][y]; int idx2 = groupIdx[nx][ny];
                        int num1 = board[x][y]; int num2 = board[nx][ny];
                        int cnt1 = groupCnt[idx1]; int cnt2 = groupCnt[idx2];
                        ret += (cnt1 + cnt2) * num1 * num2;
                    }
                }
            }
        }

        return ret/2;
    }
    static int groupCnt[];
    static int groupIdx[][];
    static boolean visited[][];
    static void grouping(){

        visited = new boolean[n][n];
        groupIdx = new int[n][n];
        groupCnt = new int[n * n + 1];
        int idx = 0;
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(!visited[i][j]){
                    idx++;
                    groupCnt[idx] = 1;
                    groupIdx[i][j] = idx;
                    visited[i][j] = true;
                    dfs(i,j,idx);
                }
            }
        }
    }
    static int dx[] = {0,1,0,-1};
    static int dy[] = {1,0,-1,0};
    static void dfs(int x, int y, int idx){
        for(int i = 0; i<4; i++){
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(inRange(nx,ny) && !visited[nx][ny] && board[nx][ny] == board[x][y]) {
                visited[nx][ny] = true;
                groupCnt[idx]++;
                groupIdx[nx][ny] = idx;
                dfs(nx, ny, idx);
            }
        }
    }
    static boolean inRange(int x, int y){return x >= 0 && y >= 0 && x < n && y < n;}
    static void rotate(){
        int temp[][] = new int[n][n];

        // 십자가형 반시계 회전
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(i == n/2 || j == n/2)
                    temp[i][j] = board[j][n - i - 1];
            }
        }

        // 나머지 시계 회전
        squareRotate(0,0);
        squareRotate(n/2 + 1,n/2 + 1);
        squareRotate(0,n/2 + 1);
        squareRotate(n/2 + 1,0);

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(i == n/2 || j == n/2)
                    board[i][j] = temp[i][j];
            }
        }
    }
    static void squareRotate(int x, int y){
        int temp[][] = new int[n/2][n/2];
        for(int i = 0; i<n/2; i++){
            for(int j = 0; j<n/2; j++){
                temp[i][j] = board[i + x][j + y];
            }
        }

        for(int i = 0; i<n/2; i++){
            for(int j = 0; j<n/2; j++){
                board[x + i][y + j] = temp[n/2 - j - 1][i];
            }
        }
    }
}
