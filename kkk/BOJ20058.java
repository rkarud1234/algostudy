import java.io.*;
import java.util.*;

public class BOJ20058 {

    private static int n, size;
    private static int[][] map;
    private static final int[][] move = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = parse(st.nextToken()); // 맵 크기
        size = 1 << n;
        int q = parse(st.nextToken()); // 파이어스톰 횟수

        map = new int[size][size];
        for (int i = 0; i < size; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < size; j++) {
                map[i][j] = parse(st.nextToken());
            }
        }

        st = new StringTokenizer(br.readLine());
        while (q-- > 0) {
            // 1. 격자로 나누기
            divideGrid(parse(st.nextToken()));
            // 3. 얼음 녹이기
            melting();
        }

        // 4. 정답 출력
        int ans = 0;
        int maxCnt = 0;
        boolean[][] visit = new boolean[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ans += map[i][j];
                if (map[i][j] > 0 && !visit[i][j]) {
                    // bfs 시작
                    Queue<Point> queue = new LinkedList<>();

                    queue.offer(new Point(i, j));
                    visit[i][j] = true;
                    int cnt = 0;
                    while (!queue.isEmpty()) {
                        cnt++;
                        Point p = queue.poll();
                        for (int d = 0; d < 4; d++) {
                            int ni = p.i + move[d][0];
                            int nj = p.j + move[d][1];

                            if (ni >= 0 && nj >= 0 && ni < size && nj < size && map[ni][nj] != 0 && !visit[ni][nj]) {
                                queue.offer(new Point(ni, nj));
                                visit[ni][nj] = true;
                            }
                        }
                    }

                    maxCnt = Math.max(maxCnt, cnt);
                }
            }
        }
        System.out.println(ans);
        System.out.println(maxCnt);
    }

    private static void melting() {
        Queue<Point> queue = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] <= 0) {
                    continue;
                }

                int cnt = 0;
                for (int d = 0; d < 4; d++) {
                    int ni = i + move[d][0];
                    int nj = j + move[d][1];

                    if (ni >= 0 && nj >= 0 && ni < size && nj < size && map[ni][nj] > 0) {
                        cnt++;
                    }
                }

                if (cnt < 3) {
                    queue.offer(new Point(i, j));
                }
            }
        }

        for (Point p : queue) {
            map[p.i][p.j]--;
        }
    }

    private static void divideGrid(int l) {
        int range = 1 << l;
        for (int i = 0; i < size; i += range) {
            for (int j = 0; j < size; j += range) {
                // 2. 시계방향으로 90도 회전하기
                // y=-x대칭, y축 대칭
                int[][] subArr = deepCopy(i, j, range, map);
                subArr = turn90(subArr);

                for (int a = 0; a < subArr.length; a++) {
                    for (int b = 0; b < subArr[a].length; b++) {
                        map[a + i][b + j] = subArr[a][b];
                    }
                }
            }
        }
    }

    private static int[][] turn90(int[][] subArr) {
        int subSize = subArr.length;
        int[][] tmp = new int[subSize][subSize];
        for (int i = 0; i < subSize; i++) {
            for (int j = 0; j < subSize; j++) {
                tmp[i][j] = subArr[subSize - j - 1][i];
            }
        }

        return tmp;
    }

    private static int[][] deepCopy(int si, int sj, int range, int[][] arr) {
        int[][] copy = new int[range][range];

        for (int i = si; i < si + range; i++) {
            for (int j = sj; j < sj + range; j++) {
                copy[i - si][j - sj] = arr[i][j];
            }
        }

        return copy;
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