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
                        int[][] compressedMatrix = vectorQuantization.compress(image, blockWidth, blockHight, codeBookSize);
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
}