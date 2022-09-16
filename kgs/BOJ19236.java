package kgs;

import java.util.*;
import java.io.*;
public class BOJ19236 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int dx[] = {-1,-1,0,1,1,1,0,-1};
    static int dy[] = {0,-1,-1,-1,0,1,1,1};
    static int max = 0;
    public static void main(String[] args) throws Exception{

        ArrayList<Fish> fishes = new ArrayList<>();
        StringTokenizer st;
        int board[][] = new int[4][4];
        for (int i = 0; i < 4; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < 4; j++) {

                int id = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken()) - 1;
                fishes.add(new Fish(i,j,id,dir,true));
                board[i][j] = id;
            }
        }

        Collections.sort(fishes);

        Fish f = fishes.get(board[0][0] - 1);
        Shark shark = new Shark(0, 0, f.dir, f.id);
        f.isAlive = false;
        board[0][0] = -1;

        // 백트래킹
        dfs(board, shark, fishes);
        System.out.println(max);
    }

    static void dfs(int[][] board, Shark shark, List<Fish> fishes) {

        if (max < shark.sum) {
            max = shark.sum;
        }

        fishes.forEach(e -> moveFish(e, board, fishes));

        for (int dist = 1; dist < 4; dist++) {
            int nx = shark.x + dx[shark.dir] * dist;
            int ny = shark.y + dy[shark.dir] * dist;

            if (inRange(nx,ny) && board[nx][ny] > 0) {

                // 복사 필수
                int[][] newBoard = copyBoard(board);
                List<Fish> newFishes = copyFishes(fishes);

                // 상어 움직이다
                Fish f = newFishes.get(board[nx][ny] - 1);
                Shark newShark = new Shark(nx, ny, f.dir, shark.sum + f.id);

                // 이전에 상어 머물렀던 자리 청소
                newBoard[shark.x][shark.y] = 0;
                // 물고기 죽음
                newBoard[nx][ny] = -1;
                f.isAlive = false;

                dfs(newBoard, newShark, newFishes);
            }
        }
    }

    static boolean inRange(int x, int y){
        return x >= 0 && y >= 0 && x < 4 && y < 4;
    }

    static void moveFish(Fish fish, int[][] arr, List<Fish> fishes) {
        if (fish.isAlive == false) return;

        for (int i = 0; i < 8; i++) {
            int nextDir = (fish.dir + i) % 8;
            int nx = fish.x + dx[nextDir];
            int ny = fish.y + dy[nextDir];

            if (inRange(nx,ny) && arr[nx][ny] >= 0) { // 빈칸 + 물고기 있는 자리
                arr[fish.x][fish.y] = 0; // 원래 있던 자리 치우고

                if (arr[nx][ny] > 0) { // 가려는 자리에 물고기 있으면 스왑
                    Fish temp = fishes.get(arr[nx][ny] - 1);
                    temp.x = fish.x;
                    temp.y = fish.y;
                    arr[fish.x][fish.y] = temp.id;
                }

                fish.x = nx;
                fish.y = ny;
                arr[nx][ny] = fish.id;
                fish.dir = nextDir;
                return;
            }
        }
    }

    static List<Fish> copyFishes(List<Fish> fishes) {
        List<Fish> temp = new ArrayList<>();
        fishes.forEach(e -> temp.add(new Fish(e.x, e.y, e.id, e.dir, e.isAlive)));
        return temp;
    }

    private static int[][] copyBoard(int[][] board) {
        int temp[][] = new int[4][4];
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                temp[i][j] = board[i][j];
            }
        }
        return temp;
    }

    static class Shark {
        int x, y, dir, sum;

        Shark(int x, int y, int dir, int sum) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.sum = sum;
        }
    }

    static class Fish implements Comparable<Fish>{
        int x, y, id, dir;
        boolean isAlive;
        Fish(int x, int y, int id, int dir, boolean isAlive) {
            this.x = x;
            this.y = y;
            this.id = id;
            this.dir = dir;
            this.isAlive = isAlive;
        }
        public int compareTo(Fish o){
            return Integer.compare(id,o.id);
        }
    }
}
