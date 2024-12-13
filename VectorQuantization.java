import java.awt.image.BufferedImage;

class VectorQuantization{
    private int[][] LGBAlgorithm(int[][] arr,int blockWidth , int blockHight , int codeBookSize){
        int[][][] codeBook = new int[blockWidth][blockHight][codeBookSize];
        int[][] block = new int[blockWidth][blockWidth];
        for(int i = 0; i < blockHight ; i++){
            for(int j = 0 ; j < blockWidth ; j++){
                block[i%blockHight][j%blockWidth] += arr[i][j];
            }
        }
        return block;

    }
    public void compress(BufferedImage image,int blockWidth , int blockHight , int codeBookSize ){
        //1-take the pixels of image and put it in 2d array
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixelsArray = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelsArray[j][i] = (image.getRGB(j,i) >> 16) & 0xFF;
            }
        }
        int [][] arr = LGBAlgorithm(pixelsArray, blockWidth, blockHight, codeBookSize);
        for (int i = 0; i < blockHight; i++) {
            for (int j = 0; j < blockWidth; j++) {
                System.out.print(arr[j][i]+" ");
            }
            System.out.println();
        }
    }
    public void decompress(){

    }
}