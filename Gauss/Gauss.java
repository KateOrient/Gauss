package Gauss;

import com.sun.javafx.binding.StringFormatter;

import java.io.*;
import java.util.StringTokenizer;
import java.text.*;

public class Gauss{
    private int n;
    private double[][] Equation;

    public Gauss (String fileName) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        n = Integer.parseInt(reader.readLine());
        Equation = new double[n][n + 1];
        String s;
        for (int i = 0; i < n; i++){
            s = reader.readLine();
            StringTokenizer st = new StringTokenizer(s);
            int j = 0;
            while (st.hasMoreTokens()){
                Equation[i][j] = Double.parseDouble(st.nextToken());
                j++;
            }
        }
    }

    public void loadEquation (String fileName) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        n = Integer.parseInt(reader.readLine());
        Equation = new double[n][n + 1];
        String s;
        for (int i = 0; i < n; i++){
            s = reader.readLine();
            StringTokenizer st = new StringTokenizer(s);
            int j = 0;
            while (st.hasMoreTokens()){
                Equation[i][j] = Integer.parseInt(st.nextToken());
                j++;
            }
        }
        reader.close();
    }

    public void writeEquation (String fileName) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n + 1; j++){
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(5);
                writer.write(nf.format(Equation[i][j]) + " ");
            }
            writer.write("\r\n");
        }
        writer.close();
    }

    public void print (){
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n + 1; j++){
                System.out.format("%10.5f ", Equation[i][j]);
            }
            System.out.println();
        }
    }

    private int findMaxInCol (int col){
        int max;
        max = 0;
        for (int i = 0; i < n; i++){
            if (Math.abs(Equation[i][col]) > Math.abs(Equation[max][col])){
                max = i;
            }
        }
        return max;
    }

    private void changeRows (int row1, int row2){
        double tmp;
        for (int i = 0; i < n + 1; i++){
            tmp = Equation[row1][i];
            Equation[row1][i] = Equation[row2][i];
            Equation[row2][i] = tmp;
        }
    }

    private int findMaxInRow (int row){
        int max;
        max = 0;
        for (int i = 0; i < n; i++){
            if (Math.abs(Equation[row][i]) > Math.abs(Equation[row][max])){
                max = i;
            }
        }
        return max;
    }

    private void changeCols (int col1, int col2, int[] order){
        double tmp;
        int indx = order[col1];
        order[col1] = order[col2];
        order[col2] = indx;
        for (int i = 0; i < n; i++){
            tmp = Equation[i][col1];
            Equation[i][col1] = Equation[i][col2];
            Equation[i][col2] = tmp;
        }
    }

    public double[] solve () throws Exception{
        double[] res = new double[n];
        double[][] T;
        T = save();
        //прямой ход
        for (int i = 0; i < n; i++){
            int l = i + 1;
            while (Equation[i][i] == 0 && l < n - 1){
                changeRows(i, l);
                l++;
            }
            if (Equation[i][i] != 0){
                for (int j = n; j >= i; j--){
                    Equation[i][j] /= Equation[i][i];
                }
            }
            for (int j = i + 1; j < n; j++){
                for (int k = n; k >= 0; k--){
                    Equation[j][k] -= Equation[j][i] * Equation[i][k];
                }
            }
        }
        //обратный ход
        for (int i = n - 1; i >= 0; i--){
            checkLine(i);
            res[i] = Equation[i][n];
            for (int j = n - 1; j > i; j--){
                res[i] -= res[j] * Equation[i][j];
            }
        }
        Equation = T;
        return res;
    }

    public double[] solveCol () throws Exception{
        double[] res = new double[n];
        double[][] T;
        T = save();

        //прямой ход
        for (int i = 0; i < n; i++){
            changeRows(findMaxInCol(i), i);
            if (Equation[i][i] != 0){
                for (int j = n; j >= i; j--){
                    Equation[i][j] /= Equation[i][i];
                }
            }
            for (int j = i + 1; j < n; j++){
                for (int k = n; k >= 0; k--){
                    Equation[j][k] -= Equation[j][i] * Equation[i][k];
                }
            }
        }
        //обратный ход
        for (int i = n - 1; i >= 0; i--){
            checkLine(i);
            res[i] = Equation[i][n];
            for (int j = n - 1; j > i; j--){
                res[i] -= res[j] * Equation[i][j];
            }
        }
        Equation = T;
        return res;
    }

    public double[] solveRow () throws Exception{
        double[] res = new double[n];
        double[][] T;
        T = save();
        int[] order = new int[n];
        for (int i = 0; i < n; i++){
            order[i] = i;
        }
        //прямой ход
        for (int i = 0; i < n; i++){
            changeCols(findMaxInRow(i), i, order);
            if (Equation[i][i] != 0){
                for (int j = n; j >= i; j--){
                    Equation[i][j] /= Equation[i][i];
                }
            }
            for (int j = i + 1; j < n; j++){
                for (int k = n; k >= 0; k--){
                    Equation[j][k] -= Equation[j][i] * Equation[i][k];
                }
            }
        }
        //обратный ход
        for (int i = n - 1; i >= 0; i--){
            checkLine(i);
            res[order[i]] = Equation[i][n];
            for (int j = n - 1; j > i; j--){
                res[order[i]] -= res[j] * Equation[i][j];
            }
        }
        Equation = T;
        return res;
    }

    public double[] solveEl () throws Exception{
        double[] res = new double[n];
        double[][] T;
        T = save();
        int[] order = new int[n];
        for (int i = 0; i < n; i++){
            order[i] = i;
        }
        //прямой ход
        for (int i = 0; i < n; i++){
            findMaxElAndMakeMain(i, order);
            /*System.out.println();
            print();*/
            if (Equation[i][i] != 0){
                for (int j = n; j >= i; j--){
                    Equation[i][j] /= Equation[i][i];
                }
            }
            /*System.out.println();
            print();*/
            for (int j = i + 1; j < n; j++){
                for (int k = n; k >= 0; k--){
                    Equation[j][k] -= Equation[j][i] * Equation[i][k];
                }
            }
        }
        //обратный ход
        for (int i = n - 1; i >= 0; i--){
            checkLine(i);
            res[order[i]] = Equation[i][n];
            for (int j = n - 1; j > i; j--){
                res[order[i]] -= res[j] * Equation[i][j];
            }
        }
        Equation = T;
        return res;
    }

    private void findMaxElAndMakeMain (int main, int[] order){
        int maxcol = main;
        int maxrow = main;
        for (int i = main;i < n; i++){
            for (int j = main;i<n;i++)
            if (Math.abs(Equation[maxrow][maxcol]) < Math.abs(Equation[i][j])){
                maxrow = i;
                maxcol = j;
            }
        }
        changeRows(main, maxrow);
        changeCols(main, maxcol, order);
    }

    private void checkLine (int i) throws Exception{
        boolean sol = false;
        for (int j = 0; j < n; j++){
            if (Equation[i][j] != 0)
                sol = true;
        }
        if (sol == false){
            if (Equation[i][n] == 0)
                throw new Exception("An infinite number of solutions.");
            else
                throw new Exception("No solutions.");
        }
    }

    private double[][] save (){
        double[][] T = new double[n][n + 1];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n + 1; j++){
                T[i][j] = Equation[i][j];
            }
        }
        return T;
    }

    public double[][] inverse () throws Exception{
        double[][] I = new double[n][n];
        double[][] T = new double[n][n + 1];
        T = save();
        double[] res = new double[n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                Equation[j][n] = 0;
            }
            Equation[i][n] = 1;
            try{
                res = solve();
            }catch (Exception e){
                throw new Exception("No inverse matrix.");
            }
            for (int j = 0; j < n; j++){
                I[j][i] = res[j];
            }
        }
        Equation = T;
        return I;
    }

    public int getN (){
        return n;
    }

    public double determinant () throws Exception{
        double det = 1;
        double[][] T = new double[n][n + 1];
        T = save();
        //прямой ход
        for (int i = 0; i < n; i++){
            det *= Equation[i][i];
            if (Equation[i][i] != 0){
                for (int j = n; j >= i; j--){
                    Equation[i][j] /= Equation[i][i];
                }
            }
            for (int j = i + 1; j < n; j++){
                for (int k = n; k >= 0; k--){
                    Equation[j][k] -= Equation[j][i] * Equation[i][k];
                }
            }
        }
        Equation = T;
        return det;
    }
}
