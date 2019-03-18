package sample;

/**
 * Created by liamkreiss on 3/12/19.
 */
/**
 *
 * @author Deus Jeraldy
 * @Email: deusjeraldy@gmail.com
 * BSD License
 */

import java.util.Arrays;

public class NNTest {

    public static void main(String[] args) {

        double[][] X = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[][] Y = {{0}, {1}, {1}, {0}};

        int m = 4;
        int nodes = 400;
        double stepSize = 0.01;
        int iterations = 4000;

        X = np.T(X);
        Y = np.T(Y);

        double[][] W1 = np.random(nodes, 2);
        double[][] b1 = new double[nodes][m];

        double[][] W2 = np.random(1, nodes);
        double[][] b2 = new double[1][m];

        for (int i = 0; i < iterations; i++) {
            // Foward Prop
            // LAYER 1
            double[][] Z1 = np.add(np.dot(W1, X), b1);
            double[][] A1 = np.sigmoid(Z1);

            //LAYER 2
            double[][] Z2 = np.add(np.dot(W2, A1), b2);
            double[][] A2 = np.sigmoid(Z2);

            double cost = np.cross_entropy(m, Y, A2);
            //costs.getData().add(new XYChart.Data(i, cost));

            // Back Prop
            //LAYER 2
            double[][] dZ2 = np.subtract(A2, Y);
            double[][] dW2 = np.divide(np.dot(dZ2, np.T(A1)), m);
            double[][] db2 = np.divide(dZ2, m);

            //LAYER 1
            double[][] dZ1 = np.multiply(np.dot(np.T(W2), dZ2), np.subtract(1.0, np.power(A1, 2)));
            double[][] dW1 = np.divide(np.dot(dZ1, np.T(X)), m);
            double[][] db1 = np.divide(dZ1, m);

            // G.D

            W1 = np.subtract(W1, np.multiply(stepSize, dW1));
            b1 = np.subtract(b1, np.multiply(stepSize, db1));

            W2 = np.subtract(W2, np.multiply(stepSize, dW2));
            b2 = np.subtract(b2, np.multiply(stepSize, db2));

            if (i % (iterations / 10) == 0) {
                System.out.println("==============");
                System.out.println("Cost = " + cost);
                System.out.println("Predictions = " + Arrays.deepToString(A2));
            }
        }
    }
}