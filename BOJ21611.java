import java.io.*;
import java.util.*;
public class BOJ21611 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    static int n,m,mid,ans;
    static int board[][], s[], d[];
    static Block blocks[];
    static int mapping[][];
    static boolean delete[];
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        ans = 0;
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        mid = n/2;
        board = new int[n][n];
        s = new int[m];
        d = new int[m];
        blocks = new Block[n * n];

        mapping = new int[n][n];

        for(int i = 0; i<n; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j<n; j++){
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }


        for(int i = 0; i<m; i++){
            st = new StringTokenizer(br.readLine());
            d[i] = Integer.parseInt(st.nextToken());
            s[i] = Integer.parseInt(st.nextToken());
        }

        init();
        for(int i = 0; i<m; i++){
            magic(d[i], s[i]);
            reBatch();
            sequence4Remove();
            grouping();
        }
        System.out.println(ans);

    }
    static void grouping(){
        int idx = 1;
        int cnt = 1;
        int board2[][] = new int[n][n];
        for(int i = 2; i<n*n; i++){
            int x1 = blocks[i - 1].x;
            int y1 = blocks[i - 1].y;
            int x2 = blocks[i].x;
            int y2 = blocks[i].y;
            if(idx + 1 >= n * n) break;
            if(board[x1][y1] == 0) break;
            if(board[x1][y1] == board[x2][y2]){
                cnt++;
            }else{
                board2[blocks[idx].x][blocks[idx].y] = cnt;
                board2[blocks[idx + 1].x][blocks[idx + 1].y] = board[x1][y1];
                idx += 2;
                cnt = 1;
            }
        }

        for(int i = 0; i<n; i++){
            board[i] = board2[i].clone();
        }
    }

    static void sequence4Remove(){
        while(isExist()){
            reBatch();
        }
    }
    static boolean isExist(){
        boolean ret = false;

        int cnt = 0;
        delete = new boolean[n * n];
        for(int i = 2; i<n*n; i++){
            int x1 = blocks[i - 1].x;
            int y1 = blocks[i - 1].y;
            int x2 = blocks[i].x;
            int y2 = blocks[i].y;
            if(board[x2][y2] != 0 && board[x1][y1] == board[x2][y2]){
                cnt++;
            }else{
                if(cnt >= 3){
                    ret = true;
                    for(int j = 0; j<=cnt; j++) {
                        delete[i - 1 - j] = true;
                        ans += board[blocks[i - 1 - j].x][blocks[i - 1 - j].y];
                    }
                }
                cnt = 0;
            }
        }
        if(cnt >= 3) {
            for(int i = 0; i<=cnt; i++) {
                delete[n * n - 1 - i] = true;
                ans += board[blocks[n * n - 1 - i].x][blocks[n * n - 1 - i].y];
            }
            ret = true;
        }

        return ret;
    }

    static void reBatch(){
        int idx = 1;
        for(int i = 1; i<n*n; i++){
            if(delete[i]) continue;

            int x1 = blocks[idx].x;
            int y1 = blocks[idx].y;
            int x2 = blocks[i].x;
            int y2 = blocks[i].y;
            board[x1][y1] = board[x2][y2];
            idx++;
        }
        for(int i = idx; i<n*n; i++){
            int x = blocks[i].x;
            int y = blocks[i].y;
            board[x][y] = 0;
        }
    }
    static void magic(int dir, int dist){
        delete = new boolean[n * n];
        int x = mid;
        int y = mid;
        for(int i = 0; i<dist; i++){
            x += dx[dir];
            y += dy[dir];
            delete[mapping[x][y]] = true;
        }
    }
    static int dx[] = {0,-1,1,0,0};
    static int dy[] = {0,0,0,-1,1};
    static void init(){
        int x = mid;
        int y = mid;
        int dir = 3;
        int dist = 1;
        int idx = 1;
        int cnt = 0;
        while(true){
            for(int i = 0; i<dist; i++){
                x += dx[dir];
                y += dy[dir];
                if(y < 0) return;
                mapping[x][y] = idx;
                blocks[idx++] = new Block(x,y);

            }
            dir = changeDir(dir);
            cnt++;
            if(cnt == 2){
                dist++;
                cnt = 0;
            }
        }
    }
    static int changeDir(int dir){
        if(dir == 1) return 3;
        else if(dir == 2) return 4;
        else if(dir == 3) return 2;
        else return 1;
    }
    static class Block{
        int x, y;
        Block(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}