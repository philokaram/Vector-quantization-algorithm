import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

class Main{
    public static void main(String[] args){
        System.out.println("    _________________________________________    ");
        System.out.println("   |                                         |   ");
        System.out.println("   |      Vector Quantization Algorithm      |   ");
        System.out.println("   |_________________________________________|   \n");
        boolean isRunning = true;
        int choice;
        String imagePath;
        String compressedFileName;
        int blockHight;
        int blockWidth;
        int codeBookSize;
        Scanner input = new Scanner(System.in);
        VectorQuantization vectorQuantization = new VectorQuantization();
        while(isRunning){
            menu();
            choice = input.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter the image path: ");
                    imagePath = input.next();
                    System.out.println("Enter the block size: ");
                    System.out.print("  1-Enter the number of pixels for block width: ");
                    blockWidth = input.nextInt();
                    System.out.print("  2-Enter the number of pixels for block hight: ");
                    blockHight = input.nextInt();
                    System.out.print("Enter the code book size: ");
                    codeBookSize = input.nextInt();
                    File imageFile = new File(imagePath);
                    try {
                        BufferedImage image = ImageIO.read(imageFile);
                        //-----------------------------------------------------
                        //1-take the pixels of image and put it in 2d array
                        //-----------------------------------------------------
                        int imageWidth = image.getWidth();
                        int imageHeight = image.getHeight();
                        int[][] pixelsArray = new int[imageWidth][imageHeight];
                        // System.out.println(imageHeight+" "+imageWidth);
                        for (int i = 0; i < imageWidth; i++) {
                            for (int j = 0; j < imageHeight; j++) {
                                // System.out.println(i+" "+j);
                                pixelsArray[i][j] = (image.getRGB(i,j) >> 16) & 0xFF;
                            }
                        }   
                        int[][] compressedMatrix = vectorQuantization.compress(pixelsArray,imageWidth, imageHeight,blockWidth, blockHight, codeBookSize);
                        System.out.print("Enter a name for compressed file: ");
                        compressedFileName = input.next();
                        writeCompressedDataInTxtFile(compressedMatrix,vectorQuantization.getCodeBook(),compressedFileName,imageWidth,imageHeight,blockWidth,blockHight,codeBookSize);
                        System.out.println("The Compression Done.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                case 2 -> {
                }
                case 0 -> isRunning = false;
                default -> System.out.println("invalid option, Please try again.");
            }
        }
        input.close();
    
    }
    static void menu(){
        System.out.println("Do you want:");
        System.out.println("   1- Compress");
        System.out.println("   2- Decompress");
        System.out.println("   0- Exit");
        System.out.print("Enter your choice: ");

    }
    public static void writeCompressedDataInTxtFile(int[][] compressedMatrix ,double[][][] codeBook , String compressedFileName ,int imageWidth,int imageHight,int blockWidth ,int blockHight ,int codeBookSize){
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
            for (int n = 0; n < codeBookSize; n++) {
                for (int i = 0; i < blockWidth; i++) {
                    for (int j = 0; j < blockHight; j++) {
                        outputFile.write(Double.toString(codeBook[n][i][j])+" ");
                    }
                }
                outputFile.write('\n');
            }
        } catch (IOException ex) {
        }

    }
}