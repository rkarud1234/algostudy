import java.io.*;
import java.util.*;

public class BOJ16235 {

    private static int n, m, k;
    private static int[][] grounds;
    private static int[][] arr;
    private static List<Tree> trees;
    private static List<Tree> dead;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = parse(st.nextToken()); // 맵 크기
        m = parse(st.nextToken()); // 나무 정보
        k = parse(st.nextToken()); // 년 수

        trees = new LinkedList<>();
        dead = new ArrayList<>();
        grounds = new int[n + 1][n + 1];
        // NxN크기의 맵 정보가 입력된다.
        arr = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= n; j++) {
                arr[i][j] = parse(st.nextToken());
                grounds[i][j] = 5;
            }
        }

        // 나무의 정보가 입력된다.
        while (m-- > 0) {
            st = new StringTokenizer(br.readLine());
            trees.add(new Tree(parse(st.nextToken()), parse(st.nextToken()), parse(st.nextToken())));
        }

        while (k-- > 0) {
            spring();
            summer();
            fall();
            winter();
        }

        System.out.println(trees.size());
    }

    // 봄에는 나무가 자신의 나이만큼 양분을 먹고, 나이가 1 증가한다.
    // 만약, 땅에 양분이 부족해 자신의 나이만큼 양분을 먹을 수 없는 나무는 양분을 먹지 못하고 즉시 죽는다.
    private static void spring() {
        Iterator<Tree> iterator = trees.iterator();
        while (iterator.hasNext()) {
            Tree tree = iterator.next();
            if (tree.age > grounds[tree.i][tree.j]) {
                // 먹어야 할 양분보다 땅에 있는 양분이 적다면 죽는다
                dead.add(tree);
                iterator.remove();
            } else {
                // 양분이 충분하다면 나이만큼 양분을 먹는다.
                grounds[tree.i][tree.j] -= tree.age;
                tree.age++;
            }
        }
    }

    // 여름에는 봄에 죽은 나무가 양분으로 변하게 된다.
    // 각각의 죽은 나무마다 나이를 2로 나눈 값이 나무가 있던 칸에 양분으로 추가된다. 소수점 아래는 버린다.
    private static void summer() {
        for (Tree tree : dead) {
            grounds[tree.i][tree.j] += tree.age / 2;
        }
        dead.clear();
    }

    // 가을에는 나무가 번식한다. 번식하는 나무는 나이가 5의 배수이어야 하며, 인접한 8개의 칸에 나이가 1인 나무가 생긴다.
    private static void fall() {
        final int[][] move = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        List<Tree> newTrees = new LinkedList<>();

        for (Tree tree : trees) {
            if (tree.age % 5 == 0) {
                for (int d = 0; d < 8; d++) {
                    int ni = tree.i + move[d][0];
                    int nj = tree.j + move[d][1];
                    if (ni >= 1 && ni <= n && nj >= 1 && nj <= n) {
                        newTrees.add(new Tree(ni, nj, 1));
                    }
                }
            }
        }

        trees.addAll(0, newTrees);
    }

    // 겨울에는 S2D2가 땅을 돌아다니면서 땅에 양분을 추가한다.
    private static void winter() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                grounds[i][j] += arr[i][j];
            }
        }
    }

    private static class Tree {
        int i, j, age;
        boolean isDead;

        Tree(int i, int j, int age) {
            this.i = i;
            this.j = j;
            this.age = age;
            this.isDead = false;
        }
    }

    private static int parse(String s) {
        return Integer.parseInt(s);
    }
}
