package sample;

import parts.Game;
import players.ComputerPlayer;
import players.GeneticPlayer;
import players.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by liamkreiss on 3/18/19.
 */
public class TrainGeneticAlgorithm {
    public static final int WEIGHT_SIZE = 9;
    public static final int CHILDREN = 14;
    public static final int NUM_RANDOM_COMPUTERS = 4;
    Random r = new Random();

    public TrainGeneticAlgorithm(int generations) throws FileNotFoundException {
        double[] weights1 = new double[WEIGHT_SIZE];
        double[] weights2 = new double[WEIGHT_SIZE];
        for (int i = 0; i < weights1.length; i++) {
            weights1[i] = r.nextDouble();
            weights2[i] = r.nextDouble();
        }

        trainWithWeights(weights1, weights2, generations);
    }

    public TrainGeneticAlgorithm(String file, int generations) throws FileNotFoundException {
        Scanner console = new Scanner(new File(file));
        double[] weights1 = new double[WEIGHT_SIZE];
        double[] weights2 = new double[WEIGHT_SIZE];

        Scanner line = new Scanner(console.nextLine());
        for (int i = 0; i < weights1.length; i++) {
            weights1[i] = line.nextDouble();
        }

        line = new Scanner(console.nextLine());
        for (int i = 0; i < weights2.length; i++) {
            weights2[i] = line.nextDouble();
        }

        trainWithWeights(weights1, weights2, generations);
    }

    private void trainWithWeights(double[] weights1, double[] weights2, int generations) throws FileNotFoundException {
        if (generations == 0) {
            String filename = String.format("training%d.txt", System.currentTimeMillis() / 100);
            PrintStream ps = new PrintStream(new File(filename));
            for (double w : weights1) {
                ps.print(w + " ");
            }
            ps.println();
            for (double w : weights2) {
                ps.print(w + " ");
            }
        } else {
            ArrayList<double[]> weights = new ArrayList<>();
            weights.add(weights1);
            weights.add(weights2);

            for (int i = 0; i < CHILDREN; i++) {
                double[] childWeight = new double[WEIGHT_SIZE];
                for (int j = 0; j < WEIGHT_SIZE; j++) {
                    double nextWeight;
                    if (r.nextBoolean()) {
                        nextWeight = weights1[j];
                    } else {
                        nextWeight = weights2[j];
                    }
                    nextWeight += (r.nextDouble() - 0.5);
                    childWeight[j] = nextWeight;
                }
                weights.add(childWeight);
            }

            for (int i = 0; i < NUM_RANDOM_COMPUTERS; i++) {
                double[] newWeights = new double[WEIGHT_SIZE];
                for (int j = 0; j < newWeights.length; j++) {
                    newWeights[i] = r.nextDouble();
                }
                weights.add(newWeights);
            }

            double[] scores = new double[weights.size()];
            for (int i = 0; i < weights.size() - 1; i++) {
                for (int j = i + 1; j < weights.size(); j++) {
                    GeneticPlayer gp1 = new GeneticPlayer("A", weights.get(i));
                    GeneticPlayer gp2 = new GeneticPlayer("B", weights.get(j));
                    Game g = new Game(gp1, gp2);
                    Player winner = g.getWinner();
                    if (winner == null) {
                        scores[i] += 0.5;
                        scores[j] += 0.5;
                    } else if (winner.equals(gp1)) {
                        scores[i] += 1;
                    } else if (winner.equals(gp2)) {
                        scores[j] += 1;
                    } else {
                        throw new Error("illegal game ending");
                    }
                }
            }

            int bestWeight = -1;
            int secondBestWeight = -1;
            double bestScore = Double.MIN_VALUE;
            double secondBestScore = Double.MIN_VALUE;
            for (int i = 0; i < weights.size(); i++) {
                if (scores[i] > bestScore) {
                    secondBestScore = bestScore;
                    secondBestWeight = bestWeight;
                    bestScore = scores[i];
                    bestWeight = i;
                } else if (scores[i] > secondBestScore) {
                    secondBestScore = scores[i];
                    secondBestWeight = i;
                }
            }

            System.out.println(generations);
            for (double w : weights.get(bestWeight)) {
                System.out.print(w + " ");
            }
            System.out.println();
            for (double w : weights.get(secondBestWeight)) {
                System.out.print(w + " ");
            }
            System.out.println();
            System.out.println();

            trainWithWeights(weights.get(bestWeight), weights.get(secondBestWeight), generations - 1);
        }
    }
}
