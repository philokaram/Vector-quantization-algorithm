import java.util.Scanner;
class Main{
    public static void main(String[] args){
        System.out.println("    _________________________________________    ");
        System.out.println("   |                                         |   ");
        System.out.println("   |      Vector Quantization Algorithm      |   ");
        System.out.println("   |_________________________________________|   \n");
        boolean isRunning = true;
        int choice;
        String imagePath;
        Scanner input = new Scanner(System.in);
        while(isRunning){
            menu();
            choice = input.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter the image path: ");
                    imagePath = input.next();
                    System.out.println("Enter the block size: ");
                    System.out.print("  1-Enter the number of pixels for block width: ");
                    System.out.print("  1-Enter the number of pixels for block hight: ");
                    System.out.print("Enter the code book size: ");
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