import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class IOOperation {
    private int imageHight;
    private int imageWidth;
    private int blockHight;
    private int blockWidth;
    private int codeBookSize;
    private int[][] compressedMatrix; 
    private double[][][] codeBook; 

    public void writeCompressedDataInTxtFile(int[][] compressedMatrix ,double[][][] codeBook , String compressedFileName ,int imageWidth,int imageHight,int blockWidth ,int blockHight ,int codeBookSize){
        try {
            FileWriter outputFile = new FileWriter(compressedFileName);
            outputFile.write(Integer.toString(imageWidth));
            outputFile.write(' ');
            outputFile.write(Integer.toString(imageHight));
            outputFile.write(' ');
            outputFile.write(Integer.toString(blockWidth));
            outputFile.write(' ');
            outputFile.write(Integer.toString(blockHight));
            outputFile.write(' ');
            outputFile.write(Integer.toString(codeBookSize));
            outputFile.write('\n');
            int compressedMatrixWidth = imageWidth/blockWidth;
            int compressedMatrixHight = imageHight/blockHight;
            for (int i = 0; i < compressedMatrixWidth; i++) {
                for (int j = 0; j <compressedMatrixHight ; j++) {
                    outputFile.write(Integer.toString(compressedMatrix[i][j]) +' ');
                }
                
            }
            outputFile.write('\n');
            for (int n = 0; n < codeBookSize; n++) {
                for (int i = 0; i < blockWidth; i++) {
                    for (int j = 0; j < blockHight; j++) {
                        outputFile.write(Double.toString(codeBook[n][i][j])+' ');
                        System.out.println(n+" "+i+" "+j+" "+Double.toString(codeBook[n][i][j]));

                    }
                }
                outputFile.write('\n');
            }
        } catch (IOException ex) {
        }
    }
    //------------------------------------------------------------------------------------------------
    public int[][] readCompressedDataFromTxtFile(String compressedFileName){
        try {
            File outputFile = new File(compressedFileName);
            Scanner input = new Scanner(outputFile);
            imageWidth = input.nextInt();
            System.out.println(imageWidth);
            imageHight = input.nextInt();
            System.out.println(imageHight);
            blockWidth =input.nextInt();
            System.out.println(blockWidth);
            blockHight =input.nextInt();
            System.out.println(blockHight);
            codeBookSize = input.nextInt();
            System.out.println(codeBookSize);
            int compressedMatrixWidth = imageWidth/blockWidth;
            int compressedMatrixHight = imageHight/blockHight;
            compressedMatrix = new int[compressedMatrixWidth][compressedMatrixHight];
            for (int i = 0; i < compressedMatrixWidth; i++) {
                for (int j = 0; j <compressedMatrixHight ; j++) {
                    int x =input.nextInt(); 
                    System.out.println(x);
                    // compressedMatrix[i][j] =input.nextInt();
                }
                
            }

            codeBook = new double[codeBookSize][blockWidth][blockHight];
            for (int n = 0; n < codeBookSize; n++) {
                for (int i = 0; i < blockWidth; i++) {
                    for (int j = 0; j < blockHight; j++) {
                        double x =Double.parseDouble(input.next()); 
                    System.out.println(n+" "+i+" "+j+" "+x);
                        // codeBook[n][i][j] = Double.parseDouble(input.next());
                    }
                }
                System.out.println("---------------------------------------------------------------------");
            }
        } catch (IOException ex) {
        }
        return compressedMatrix ;
    }
    public int getImageHight() {
        return imageHight;
    }

    public void setImageHight(int imageHight) {
        this.imageHight = imageHight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getBlockHight() {
        return blockHight;
    }

    public void setBlockHight(int blockHight) {
        this.blockHight = blockHight;
    }

    public int getBlockWidth() {
        return blockWidth;
    }

    public void setBlockWidth(int blockWidth) {
        this.blockWidth = blockWidth;
    }

    public int getCodeBookSize() {
        return codeBookSize;
    }

    public void setCodeBookSize(int codeBookSize) {
        this.codeBookSize = codeBookSize;
    }

    public double[][][] getCodeBook() {
        return codeBook;
    }

    public void setCodeBook(double[][][] codeBook) {
        this.codeBook = codeBook;
    }
    
}
