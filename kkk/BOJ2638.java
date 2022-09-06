import java.io.*;
import java.util.*;

public class BOJ2638 {

    private static final int[][] move = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    private static int[][] map, melt;
    private static int n, m, cheese;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = parse(st.nextToken()); // 세로
        m = parse(st.nextToken()); // 가로

        map = new int[n][m];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                map[i][j] = parse(st.nextToken());
                if (map[i][j] == 1) {
                    cheese++;
                }
            }
        }

        int ans = 0;
        while (cheese > 0) {
            bfs(); // 1. 탐색하면서 맞닿은 면이 몇 개인지 체크한다.
            meltingCheese(); // 2. 맞닿은 면이 2개 이상인 치즈를 녹인다.
            ans++;
        }

        System.out.println(ans);
    }

    private static void meltingCheese() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (melt[i][j] >= 2) {
                    map[i][j] = 0;
                    cheese--;
                }
            }
        }
    }

    private static void bfs() {
        Queue<Point> queue = new LinkedList<>();
        boolean[][] visit = new boolean[n][m];
        melt = new int[n][m];

        queue.add(new Point(0, 0));
        visit[0][0] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            for (int d = 0; d < 4; d++) {
                int ni = p.i + move[d][0];
                int nj = p.j + move[d][1];

                if (ni < 0 || ni >= n || nj < 0 || nj >= m || visit[ni][nj]) {
                    continue;
                }

                if (map[ni][nj] == 1) {
                    melt[ni][nj]++;
                } else if (map[ni][nj] == 0) {
                    queue.add(new Point(ni, nj));
                    visit[ni][nj] = true;
                }
            }
        }
    }

    private static class Point {
        int i, j;

        Point(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private static int parse(String s) {
        return Integer.parseInt(s);
    }
}
