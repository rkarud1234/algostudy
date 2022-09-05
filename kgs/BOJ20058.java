package kgs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
 
public class BOJ20058 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    static int n,q,m;
    static int board[][];
    static int L[];
    public static void main(String[] args) throws Exception{

        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        L = new int[q];
        m = (int)Math.pow(2,n);
        board = new int[m][m];
        for(int i = 0; i<m; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<m; j++){
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i<q; i++){
            L[i] = Integer.parseInt(st.nextToken());
        }

        for(int i = 0; i<q; i++){
            divide((int)Math.pow(2, L[i]));
            board = remove();
        }
        int max = getBiggest();
        System.out.println(sum() +"\n"+max);
    }
    static boolean visited[][];
    static int getBiggest(){
        visited = new boolean[m][m];
        int ret = 0;
        for(int i = 0; i<m; i++){
            for(int j = 0; j<m; j++){
                if(visited[i][j]) continue;
                if(board[i][j] == 0) continue;
                ret = Math.max(ret, bfs(i,j));
            }
        }
        return ret;
    }
    static int bfs(int x, int y){
        int ret = 1;
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{x,y});
        visited[x][y] = true;
        while(!q.isEmpty()){
            int now[] = q.poll();
            for(int i = 0; i<4; i++){
                int nx = now[0] + dx[i];
                int ny = now[1] + dy[i];
                if(nx < 0 || ny < 0 || ny >= m || nx >= m) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] == 0) continue;
                visited[nx][ny] = true;
                ret++;
                q.add(new int[]{nx,ny});
            }
        }

        return ret;
    }
    static int sum(){
        int ret = 0;
        for(int i = 0; i<m; i++){
            for(int j = 0; j<m; j++){
                ret += board[i][j];
            }
        }
        return ret;
    }
    static void print(){
        for(int i = 0; i<m; i++){
            for(int j = 0; j<m; j++){
                System.out.print(board[i][j] +" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    static void divide(int dist){
        for(int i = 0; i<m; i+=dist){
            for(int j = 0; j<m; j+=dist){
                rotate(i,j,dist);
            }
        }
    }
    static void rotate(int x, int y, int dist){
        int temp[][] = new int[dist][dist];
        for(int i = 0; i<dist; i++){
            for(int j = 0; j<dist; j++){
                temp[i][j] = board[i+x][j+y];
            }
        }

        for(int i = 0; i<dist; i++){
            for(int j = 0; j<dist; j++){
                board[x + (j)][y + (dist - i - 1)] = temp[i][j];
            }
        }

    }
    static final int dx[] = {0,0,1,-1};
    static final int dy[] = {1,-1,0,0};
    static int[][] remove(){ // 영향
        int temp[][] = new int[m][m];
        for(int i = 0; i<m; i++){
            temp[i] = board[i].clone();
        }

        for(int i = 0; i<m; i++){
            for(int j = 0; j<m; j++){
                int cnt = 0;
                if(board[i][j] == 0) continue;
                for(int k = 0; k<4; k++){
                    int nx = i + dx[k];
                    int ny = j + dy[k];
                    if(nx < 0 || ny < 0 || nx >= m || ny >= m) continue;
                    if(board[nx][ny] > 0) cnt++;
                }
                if(cnt < 3) temp[i][j]--;
            }
        }
        return temp;
    }
}
