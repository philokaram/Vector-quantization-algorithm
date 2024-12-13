import java.awt.image.BufferedImage;

class VectorQuantization{
    private double[][] LGBAlgorithm(int[][] arr,int imageHight , int imageWidth ,int blockWidth , int blockHight , int codeBookSize){
        int[][][] codeBook = new int[blockWidth][blockHight][codeBookSize];
        double[][] block = new double[blockWidth][blockWidth];
        //1-calculate the average
        for(int i = 0; i < imageWidth ; i++){
            for(int j = 0 ; j < imageHight ; j++){
                block[i%blockHight][j%blockWidth] += arr[i][j];
                System.out.println(i%blockHight + " "+ j%blockWidth + " "+ i + " "+ j + " "+arr[i][j]);
            }
        }
        double blocksNumber = (imageHight*imageWidth)/(blockHight*blockWidth);
        System.out.println(blocksNumber);
        for(int i = 0; i < blockWidth ; i++){
            for(int j = 0 ; j < blockHight ; j++){
                block[i][j] /= blocksNumber;
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
        double [][] arr = LGBAlgorithm(pixelsArray,height,width, blockWidth, blockHight, codeBookSize);
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