import java.awt.image.BufferedImage;
import java.io.File;
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
        IOOperation ioOperation = new IOOperation();
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
                        ioOperation.writeCompressedDataInTxtFile(compressedMatrix,vectorQuantization.getCodeBook(),compressedFileName,imageWidth,imageHeight,blockWidth,blockHight,codeBookSize);
                        System.out.println("The Compression Done.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                case 2 -> {
                    System.out.print("Enter the compressed file name: ");
                    compressedFileName = input.next();
                    int[][] compressedMatrix = ioOperation.readCompressedDataFromTxtFile(compressedFileName);
                    double[][][] codeBook = ioOperation.getCodeBook();
                    int imageWidth = ioOperation.getImageWidth();
                    int imageHeight = ioOperation.getImageHight();
                    blockWidth = ioOperation.getBlockWidth();
                    blockHight = ioOperation.getBlockHight();
                    codeBookSize = ioOperation.getCodeBookSize();
                    double[][] decompressedMatrix = vectorQuantization.decompress(compressedMatrix, codeBook, imageWidth, imageHeight, blockWidth, blockHight);
                    BufferedImage decompressedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
                    for (int i = 0; i < imageWidth; i++) {
                        for (int j = 0; j < imageHeight; j++) {
                            int pixel =(int) decompressedMatrix[i][j];
                            int rgb = (pixel << 16) | (pixel << 8) | pixel;
                            decompressedImage.setRGB(i, j, rgb);
                        }
                    }
                    try {
                        File outputImage = new File("decompressedImage.jpg");
                        ImageIO.write(decompressedImage, "jpg", outputImage);
                        System.out.println("The Decompression Done.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
    
}