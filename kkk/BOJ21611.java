import java.io.*;
import java.util.*;

public class BOJ21611 {

    private static int n, ans;
    private static List<Integer> marbles;
    private static Point[] position;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        n = parse(st.nextToken()); // 맵 크기
        int m = parse(st.nextToken()); // 블리자드 마법 횟수

        int[][] map = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = parse(st.nextToken());
            }
        }

        marbles = new LinkedList<>(); // 구슬 정보를 저장할 리스트
        init(map);

        while (m-- > 0) {
            st = new StringTokenizer(br.readLine());
            blizzard(parse(st.nextToken()) - 1, parse(st.nextToken()));
            while (explodeMarbles()) ;
            changeMarbles();
        }
        System.out.println(ans);
    }

    private static void blizzard(int d, int s) {
        final int[][] dir = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        int si = n / 2 + dir[d][0];
        int sj = n / 2 + dir[d][1]; // 블리자드 마법 시작 인덱스
        int ei = n / 2 + s * dir[d][0];
        int ej = n / 2 + s * dir[d][1]; // 블리자드 마법 끝나는 인덱스

        Iterator<Integer> iterator = marbles.iterator();
        int idx = 0;
        while (iterator.hasNext()) {
            iterator.next();

            int i = position[idx].i;
            int j = position[idx].j;

            if (i >= Math.min(si, ei) && i <= Math.max(si, ei) && j >= Math.min(sj, ej) && j <= Math.max(sj, ej)) {
                // 블리자드 마법의 사정거리 안에 있다면 구슬을 파괴한다.
                iterator.remove();
            }
            idx++;
        }
    }

    private static boolean explodeMarbles() {
        int before = 0;
        int cnt = 0;
        boolean isExploded = false;

        for (int s = 0; s < marbles.size(); s++) {
            int marble = marbles.get(s);
            if (marble != before) {
                // 구슬이 바뀌었을 때
                if (cnt >= 4) {
                    isExploded = true;
                    // 이전에 있던 구슬들이 4개 이상일 때 폭발
                    int idx = s - cnt;
                    s -= cnt;
                    while (cnt-- > 0) {
                        ans += before;
                        marbles.remove(idx);
                    }
                }
                cnt = 1;
                before = marble;
            } else {
                // 구슬이 바뀌지 않았다면 cnt만 더해주면 된다.
                cnt++;
            }
        }

        if (cnt >= 4) {
            int idx = marbles.size() - cnt;
            while (cnt-- > 0) {
                ans += before;
                marbles.remove(idx);
            }
        }

        return isExploded;
    }

    private static void changeMarbles() {
        List<Integer> newMarbles = new LinkedList<>();
        int before = 0;
        int cnt = 0;

        for (int marble : marbles) {
            if (newMarbles.size() >= n * n - 1) {
                break;
            }
            if (before != marble) {
                if (before != 0) {
                    newMarbles.add(cnt);
                    newMarbles.add(before);
                }
                before = marble;
                cnt = 1;
            } else {
                cnt++;
            }
        }
        if (before != 0 && newMarbles.size() < n * n - 1) {
            newMarbles.add(cnt);
            newMarbles.add(before);
        }

        marbles = newMarbles;
    }

    private static void init(int[][] map) {
        final int[][] move = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        boolean[][] visit = new boolean[n][n];
        position = new Point[n * n - 1];
        int number = n * n - 2;

        int i = 0, j = 0, d = 0;

        while (number >= 0) {
            int ni = i + move[d][0];
            int nj = j + move[d][1];

            if (ni < 0 || ni >= n || nj < 0 || nj >= n || visit[ni][nj]) {
                d = (d + 1) % 4;
                continue;
            }

            position[number--] = new Point(i, j);
            visit[i][j] = true;
            if (map[i][j] != 0) {
                marbles.add(0, map[i][j]);
            }
            i = ni;
            j = nj;
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
