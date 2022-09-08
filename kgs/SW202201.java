package kgs;

import java.util.*;
import java.io.*;

public class SW202201 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    static int n,m,h,k;
    static int me[];
    static int nextDir[][];
    static int nextDir_reverse[][];
    static ArrayList<Integer> runners[][];
    static ArrayList<Integer> newRunners[][];
    static boolean trees[][];
    static boolean forward;
    public static void main(String[] args) throws Exception{
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        forward = true;
        me = new int[]{n/2,n/2};
        nextDir = new int[n][n];
        nextDir_reverse = new int[n][n];
        trees = new boolean[n][n];
        runners = new ArrayList[n][n];
        newRunners = new ArrayList[n][n];
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                newRunners[i][j] = new ArrayList<>();
                runners[i][j] = new ArrayList<>();
            }
        }

        for(int i = 0; i<m; i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            runners[x - 1][y - 1].add(d);
        }
        for(int i = 0; i<h; i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            trees[x - 1][y - 1] = true;
        }

        init();
        int ans = 0;
        for(int i = 1; i<=k; i++){
            moveRunner();
            moveMe();
            ans += getScore(i);
        }
        System.out.println(ans);
    }

    static int getScore(int k){
        int ret = 0;
        int dx[] = {-1,0,1,0};
        int dy[] = {0,1,0,-1};
        int dir = getDir();
        for(int dist = 0; dist<3; dist++){
            int nx = me[0] + dist * dx[dir];
            int ny = me[1] + dist * dy[dir];
            if(inRange(nx,ny) && !trees[nx][ny]){
                ret += k * runners[nx][ny].size();
                runners[nx][ny].clear();
            }
        }
        return ret;
    }
    static void moveMe(){ // 확인
        int dx[] = {-1,0,1,0};
        int dy[] = {0,1,0,-1};
        int dir = getDir();
        me[0] = me[0] + dx[dir];
        me[1] = me[1] + dy[dir];

        // 방향 전환
        if(me[0] == 0 && me[1] == 0 && forward) forward = false;
        else if(me[0] == n/2 && me[1] == n/2 && !forward) forward = true;
    }

    static int getDir(){ // 확인
        if(forward){
            return nextDir[me[0]][me[1]];
        }else{
            return nextDir_reverse[me[0]][me[1]];
        }
    }
    static boolean isIn3(int x, int y){
        return Math.abs(x - me[0]) + Math.abs(y - me[1]) <= 3;
    }
    static void moveRunner(){

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                newRunners[i][j] = new ArrayList<>();
            }
        }

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                if(isIn3(i,j)){
                    for(int k = 0; k<runners[i][j].size(); k++)
                        move(i,j,runners[i][j].get(k));
                }else{
                    for(int k = 0; k<runners[i][j].size(); k++){
                        newRunners[i][j].add(runners[i][j].get(k));
                    }
//                    newRunners[i][j] = runners[i][j]; 틀렸음
                }
            }
        }

        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                runners[i][j] = newRunners[i][j];
            }
        }
    }
    static boolean inRange(int x, int y){
        return x >= 0 && y >= 0 && x < n && y < n;
    }
    static void move(int x, int y, int dir){
        // 1우 2하
        int dx[] = {0,0,1,-1};
        int dy[] = {-1,1,0,0};

        int nx = x + dx[dir];
        int ny = y + dy[dir];

        if(!inRange(nx,ny)){
            dir = (dir < 2) ? 1 - dir : 5 - dir;
            nx = x + dx[dir];
            ny = y + dy[dir];
        }
        if(nx == me[0] && ny == me[1]){
            nx = x;
            ny = y;
        }
        newRunners[nx][ny].add(dir);
    }
    static void init(){
        int dx[] = {-1,0,1,0};
        int dy[] = {0,1,0,-1};
        int x = n/2;
        int y = n/2;
        int dir = 0;
        int dist = 1;
        while(true) {
            for(int i = 0; i<dist; i++){
                nextDir[x][y] = dir;
                x += dx[dir];
                y += dy[dir];
                nextDir_reverse[x][y] = (dir + 2)%4;
                if(x == 0 && y == 0) return;
            }
            dir = (dir + 1)%4;
            if(dir == 0 || dir == 2) dist++;
        }
    }
}
