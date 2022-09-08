import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SW220203 {

    private static int[][] map, visit, sequences;
    private static int n, m, score;

    private static List<Point>[] route;
    private static boolean[] directions;
    private static int[] heads, cnt;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = parse(st.nextToken()); // 격자의 크기
        m = parse(st.nextToken()); // 팀의 개수
        int k = parse(st.nextToken()); // 라운드 수
        sequences = new int[m + 1][];

        route = new List[m + 1]; // 팀의 경로를 순서대로 담을 배열
        for (int i = 1; i <= m; i++) {
            route[i] = new ArrayList<>();
        }

        directions = new boolean[m + 1]; // 팀에서 머리->꼬리의 방향, false면 머리를 기준으로 인덱스가 증가하는 방향, true면 머리를 기준으로 인덱스가 감소하는 방향
        heads = new int[m + 1]; // 머리 위치
        cnt = new int[m + 1]; // 멤버 수

        int team = 1;
        map = new int[n][n];
        visit = new int[n][n]; // 방문 여부 및 무슨 팀의 경로에 포함되는지 저장할 배열
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = parse(st.nextToken());
            }
        } // end Input

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == 1) {
                    /// 머리를 찾았으면 꼬리들과 경로를 찾는다
                    findRoute(i, j, team);
                    team++;
                }
            }
        }
        // 경로 저장 완료

        for (int round = 0; round < k; round++) {
            // 1. 머리사람을 따라 한 칸 이동한다
            for (int i = 1; i <= m; i++) {
                heads[i] = heads[i] + (directions[i] ? 1 : -1);
            }
            // 1-2. 몇 번째 사람이 있는지 여부를 저장한다.
            for (int i = 1; i <= m; i++) {
                sequences[i] = new int[route[i].size()];
                findSequence(i);
            }

            // 2. 공이 던져진다.
            catchBall(round, findStartBallLocation(round));
        }

        System.out.println(score);
    }


    private static final int[][] move = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

    private static void findRoute(int i, int j, int team) {
        // 꼬리들과 경로를 찾아 저장하는 메서드
        visit[i][j] = team;
        route[team].add(new Point(i, j));
        if (map[i][j] != 4) {
            cnt[team]++;
        }

        int nexti = i;
        int nextj = j;
        int position = 0;
        for (int d = 0; d < 4; d++) {
            int ni = i + move[d][0];
            int nj = j + move[d][1];

            if (checkIdx(ni, nj) && map[ni][nj] != 0 && visit[ni][nj] == 0) {
                if (map[ni][nj] == 2) {
                    findRoute(ni, nj, team);
                    return;
                } else if (map[ni][nj] == 3) {
                    position = 3;
                    nexti = ni;
                    nextj = nj;
                } else if (position != 3) {
                    nexti = ni;
                    nextj = nj;
                }
            }
        }
        if (!(nexti == i && nextj == j)) {
            findRoute(nexti, nextj, team);
        }
    }

    private static void findSequence(int team) {
        // 지나갈 수 있는 팀을 찾았다면 해당 지점에 사람이 위치할 수 있는지 판단
        int size = route[team].size();
        int count = 0;
        int head = (heads[team] + size) % size; // 머리의 위치

        // 정방향일 때
        for (int i = head; i < head + cnt[team]; i++) {
            sequences[team][(head + count + size) % size] = directions[team] ? cnt[team] - count : count + 1;
            count++;
        }
    }

    private static Point findStartBallLocation(int round) {
        int seq = (round / n) % 4;
        Point start;
        switch (seq) {
            case 0:
                start = new Point(round % n, 0);
                break;
            case 1:
                start = new Point(n - 1, round % n);
                break;
            case 2:
                start = new Point(n - round % n - 1, n - 1);
                break;
            default:
                start = new Point(0, n - round % n - 1);
                break;
        }
        return start;
    }

    private static void catchBall(int round, Point p) {
        if (!checkIdx(p.i, p.j)) {
            // 공이 맵을 넘어갔다면 종료
            return;
        }

        // 현재 지점이 어느 팀의 경로인지 파악
        int team = visit[p.i][p.j];
        if (team == 0) {
            // 팀이 지나가는 경로가 아니면 그냥 지나감
            p.i = p.i + move[(round / n) % 4][0];
            p.j = p.j + move[(round / n) % 4][1];
            // 공을 잡은사람이 아무도 없다면 진행
            catchBall(round, p);
            return;
        }

        for (int i = 0; i < route[team].size(); i++) {
            Point now = route[team].get(i);
            if (now.i != p.i || now.j != p.j) {
                continue;
            }

            if (sequences[team][i] != 0) {
                // 공을 잡았다면
                score += sequences[team][i] * sequences[team][i];
                directions[team] = !directions[team];
                return;
            }
        }

        // 공을 잡은사람이 아무도 없다면 진행
        p.i = p.i + move[(round / n) % 4][0];
        p.j = p.j + move[(round / n) % 4][1];
        catchBall(round, p);
    }

    private static boolean checkIdx(int i, int j) {
        return i >= 0 && j >= 0 && i < n && j < n;
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
