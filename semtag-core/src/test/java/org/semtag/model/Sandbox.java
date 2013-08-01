package org.semtag.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ari Weiland
 */
public class Sandbox {

    @Test
    public void circle() {
        int[] counts = new int[50];
        for (int r=1; r<50; r++) {
            Set<Pair> boxes = new HashSet<Pair>();
            for (double t=0.01; t<90; t += 0.01) {
                int x = (int) (r * Math.cos(t/180 * Math.PI));
                int y = (int) (r * Math.sin(t / 180 * Math.PI));
                boxes.add(new Pair(x, y));
            }
            counts[r] = boxes.size()*4;
        }
        System.out.println(Arrays.toString(counts));
        for (int i=1; i<50; i++) {
            if (counts[i] - counts[i-1] < 0) {
                System.out.println("R: " + i + ", boxes: " + counts[i] + ", last: " + counts[i-1]);
            }
        }
    }

    public static class Pair implements Comparable<Pair> {
        private final int x;
        private final int y;

        private Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;

            Pair pair = (Pair) o;

            return x == pair.x && y == pair.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public int compareTo(Pair pair) {
            if (this.x > pair.x) {
                return 1;
            } else if (this.x < pair.x) {
                return -1;
            } else if (this.y > pair.y) {
                return 1;
            } else if (this.y < pair.y) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
}
