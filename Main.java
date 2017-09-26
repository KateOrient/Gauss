import Gauss.*;

import java.io.IOException;

public class Main{
    public static void main (String[] args) throws IOException{
        Gauss x = new Gauss("1.txt");
        x.print();
        int n = x.getN();
        double[] I = new double[n];
        try{
           /* I = x.inverse();
            for (int i =0; i<n;i++){
                for(int j=0;j<n;j++){
                    System.out.print(I[i][j]+" ");
                }
                System.out.println();
            }*/
            I = x.solve();
            for (int i = 0; i < n; i++){
                System.out.print(I[i] + " ");
            }
            System.out.println();
            I = x.solveCol();
            for (int i = 0; i < n; i++){
                System.out.print(I[i] + " ");
            }
            System.out.println();
            I = x.solveRow();
            for (int i = 0; i < n; i++){
                System.out.print(I[i] + " ");
            }
            System.out.println();
            I = x.solveEl();
            for (int i = 0; i < n; i++){
                System.out.print(I[i] + " ");
            }
            // System.out.println(x.determinant());
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }


}
