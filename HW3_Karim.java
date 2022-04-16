
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Random;

public class HW3_Karim {

    static int cr = 0;

    public static void mergeSort(ArrayList<Integer> A) {

        if (A.size() <= 1)
            return;
        int m = A.size() / 2;
        ArrayList B = new ArrayList<Integer>();
        ArrayList C = new ArrayList<Integer>();
        cr++;
        for (int i = 0; i < m; i++) {
            B.add(A.remove(0));
            cr++;

        }

        while (!A.isEmpty()) {
            C.add(A.remove(0));
        }

        mergeSort(B);
        mergeSort(C);

        merge(B, C, A);
    }

    private static void merge(ArrayList<Integer> B, ArrayList<Integer> C, ArrayList<Integer> A) {

        while (!B.isEmpty() && !C.isEmpty()) {
            if (B.get(0) < C.get(0)) {
                A.add(B.remove(0));
                cr++;
            } else if (B.get(0) > C.get(0)) {
                A.add(C.remove(0));
                cr++;

            } else {
                A.add(B.remove(0));
                C.remove(0);
                cr += 2;// if it wen to this condition it means that it performed 2 comparisons

            }

        }
        while (!B.isEmpty()) {
            A.add(B.remove(0));

        }
        while (!C.isEmpty()) {
            A.add(C.remove(0));

        }

    }

    public static void QuickSort(ArrayList<Integer> A, boolean Ran) {

        int n = A.size();

        if (n <= 1) {
            cr++;
            return;
        }
        Random rand = new Random();
        int index = rand.nextInt(n);
        Integer pivot = Ran ? A.get(index) : A.get(n - 1);
        // partition

        ArrayList L = new ArrayList<Integer>();
        ArrayList E = new ArrayList<Integer>();
        ArrayList G = new ArrayList<Integer>();
        cr++;
        for (int i = 0; i < n; i++) {

            if (A.get(i) < pivot) {
                L.add(A.get(i));
                cr++;
            } else if (A.get(i) > pivot) {
                G.add(A.get(i));
                cr++;
            } else {
                E.add(A.get(i));
                cr += 2;
            }
            cr++;
        }

        QuickSort(L, Ran);
        QuickSort(G, Ran);

        A.removeAll(A);
        while (!L.isEmpty()) {
            A.add((Integer) L.remove(0));
        }

        while (!E.isEmpty()) {

            A.add((Integer) E.remove(0));
        }
        while (!G.isEmpty()) {
            A.add((Integer) G.remove(0));
        }

    }

    public static void randomizer(ArrayList<Integer> A, int n, double p) {
        int x;
        int y;
        Integer temp;
        Random rand = new Random();

        for (int i = 0; i < n * p; i++) {
            x = rand.nextInt(n);
            y = rand.nextInt(n);

            temp = A.get(x);
            A.set(x, A.get(y));
            A.set(y, temp);

        }

    }

    public static void main(String[] args) {

        int[] N = { 10, 20, 50, 100, 200, 500, 1000 };
        double[] P = { 0.0, 0.1, 0.2, 0.5, 1.0 };
        double M = 25;

        long initial;// start pt to calc time
        long finall;// end pt to calc time

        double avgtime;// avg time of cpu time for the 25 instances
        double std_cputime = 0; // std cpu time for the 25 instance
        double CPUCI_L;
        double CPUCI_R;

        double avgcmp;
        double std_cmp = 0;
        double CompCI_L;
        double CompCI_R;

        long Sum = 0;

        int i;

        ArrayList A = new ArrayList<Integer>();
        long[] Comp = new long[25];
        long[] time = new long[25];

        for (int n : N) {
            System.out.println();

            for (double r : P) { // the two nested loops give me n,p pair

                i = 0;
                Sum = 0;

                System.out.println();
                while (i < M) { // repeat 25 times to create 25 instance

                    A.clear();
                    // created array A sorted of n elements that is different than the instance
                    // before
                    for (int j = 0; j < n; j++) {
                        A.add(j + i);

                    }
                    randomizer(A, n, r); // randomize A by r{0.1,0.2,0.5,1.0}

                    cr = 0;// reset compare counter to zero

                    initial = System.nanoTime(); // set the initial time
                    //mergeSort(A);
                    QuickSort(A, true);
                    finall = System.nanoTime(); // set the final time

                    Comp[i] = cr;

                    time[i] = finall - initial; // add the time taken for each instance for a certain n,p
                    i++;
                }
                // calculate sum of the number of comparisons of the 25 instance
                for (long x : Comp) {
                    Sum += x;
                }
                // calculate avg of the number of comparisons of the 25 instance
                avgcmp = Sum / M;
                // calculate summation formula of the std function of comparisons of the 25
                // instance
                for (long x : Comp) {
                    std_cmp += Math.pow((x - avgcmp), 2);
                }
                // calculate std of comparisons of the 25 instance
                std_cmp = Math.sqrt(std_cmp / M);

                CompCI_L = avgcmp - 1.96 * std_cmp / Math.sqrt(M);
                CompCI_R = avgcmp + 1.96 * std_cmp / Math.sqrt(M);

                System.out.println("The mean of #comparisons for (" + n + "," + r + ") is:" + avgcmp);
                System.out.println(
                        "The 95% CI of #Comparisons for (" + n + "," + r + ") is:[" + CompCI_L + "," + CompCI_R + "]");
                System.out.println("The 95% CIis: " + String.format("%.2f", CompCI_R - CompCI_L));

                Sum = 0;
                // calculate sum of time taken of the 25 instance
                for (long x : time) {
                    Sum += x;
                }
                // calculate avg of time taken of the 25 instance
                avgtime = Sum / M;
                // calculate std summation formula of time taken of the 25 instance
                for (long x : time) {
                    std_cputime += Math.pow((x - avgtime), 2);
                }
                // calculate std formula of time taken of the 25 instance
                std_cputime = Math.sqrt(std_cputime / M);
                CPUCI_L = avgtime - 1.96 * std_cputime / Math.sqrt(M);
                CPUCI_R = avgtime + 1.96 * std_cputime / Math.sqrt(M);
                System.out.println(
                        "The 95% CI of Cputime for (" + n + "," + r + ") is:[" + CPUCI_L + "," + CPUCI_R + "]");
                System.out.println("The mean of Cpu Time for (" + n + "," + r + ") is:" + avgtime);
                System.out.println("The 95% CIis: " + String.format("%.2f", CPUCI_R - CPUCI_L));

            }
        }

    }
}
